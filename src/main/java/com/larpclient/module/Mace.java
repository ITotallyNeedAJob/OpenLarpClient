package com.larpclient.module;

import com.larpclient.ActionOwnership;
import com.larpclient.ClientLog;
import com.larpclient.ServerBrandMatcher;
import com.larpclient.ModuleImportBroker;
import com.larpclient.ModuleDetector;
import com.larpclient.OptifineBridge;
import com.larpclient.ParticleEmitter;
import com.larpclient.ParticleGeometry;
import com.larpclient.ParticleKind;
import com.larpclient.mixin.ClientPlayerEntityAccessor;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.util.Locale;
import java.util.Set;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MaceItem;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Hand;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public final class Mace extends Module {
   private static Mace t;
   private final BooleanSetting autoSwap = this.addSetting(pAsBooleanSetting("auto-swap", "Auto Swap", "Select a mace immediately before a smash hit", true));
   private final ModeSetting pick = this.addSetting(
      QAsModeSetting("pick", "Pick", "Prefer damage or a Wind Burst mace for smash hits", "Damage", "Damage", "Wind Burst")
   );
   private final BooleanSetting breachSwap = this.addSetting(
      pAsBooleanSetting(
         "breach-swap",
         "Breach Swap",
         "Use a Breach mace for ordinary hits when expected damage improves by 15%. Skipped on Paper servers, which reset the charge on a swap",
         false
      )
   );
   private final BooleanSetting swapBack = this.addSetting(
      pAsBooleanSetting("swap-back", "Swap Back", "Return to the previous hotbar slot after the attack", true)
   );
   private final BooleanSetting smashAssist = this.addSetting(
      pAsBooleanSetting("smash-assist", "Smash Assist", "Attack the enemy under the crosshair during a qualifying fall", false)
   );
   private final ModeSetting smashTiming = this.addSetting(
      QAsModeSetting("smash-timing", "Smash Timing", "Delay while the next movement step can still reach the target", "Delayed", "Direct", "Delayed")
   );
   private final NumberSetting minFall = this.addSetting(new NumberSetting("min-fall", "Minimum fall distance for automatic smash hits", 3.0, 1.5, 30.0, 0.5) {
      @Override
      public String getDisplayName() {
         return "Min Fall";
      }
   });
   private final BooleanSetting mobs = this.addSetting(pAsBooleanSetting("mobs", "Mobs", "Allow smash assistance against non-player enemies", false));
   private final BooleanSetting silentAim = this.addSetting(
      pAsBooleanSetting("silent-aim", "Silent Aim", "Turn toward a nearby enemy and smash it during a fall while your view stays free", false)
   );
   private final NumberSetting aimRange = this.addSetting(
      DAsNumberSetting("aim-range", "Aim Range", "Distance at which Silent Aim starts turning toward an enemy", 6.0, 3.0, 10.0, 0.5)
   );
   private final NumberSetting turnSpeed = this.addSetting(
      DAsNumberSetting("turn-speed", "Turn Speed", "Maximum Silent Aim turn in degrees per tick", 60.0, 10.0, 180.0, 5.0)
   );
   private final ModeSetting rotation = this.addSetting(
      QAsModeSetting(
         "rotation", "Rotation", "Kill Aura rotation engine that turns the player toward the target", "Sloth", "Sloth", "FunTime", "Polar", "Matrix", "Legit"
      )
   );
   private final BooleanSetting x = this.addSetting(
      pAsBooleanSetting("lead-turn", "Lead Turn", "In a fast fall, start turning early enough that the turn finishes before the target is in reach", true)
   );
   private final ModeSetting y = this.addSetting(
      QAsModeSetting(
         "stun-slam",
         "Stun Slam",
         "When a smash target raises a shield, hit it with your best axe to disable the shield, then smash. Same Tick sends both hits at once; Next Tick smashes a tick later, which Grim's experimental packet order check does not flag",
         "Same Tick",
         "Same Tick",
         "Next Tick",
         "Off"
      )
   );
   private final ModeSetting rangeRing = this.addSetting(
      QAsModeSetting(
         "range-ring",
         "Range Ring",
         "Draw Silent Aim's reach on the ground below you: an enemy standing inside the ring is in range. In Air shows it only while you are off the ground",
         "In Air",
         "In Air",
         "Always",
         "Off"
      )
   );
   private final BooleanSetting targetMark = this.addSetting(
      pAsBooleanSetting(
         "target-mark",
         "Target Mark",
         "Outline the enemy Silent Aim is turning toward and mark where your real facing points; green once that facing is on the enemy",
         true
      )
   );
   private final ParticleGeometry S = new ParticleGeometry(this);
   private final r f = new r();
   private final c N = new c();
   private final t g = new t();
   private final x A = new x();
   private final p k = new p();
   private h I;
   private boolean e;
   private boolean z;
   private ClientPlayerEntity R;
   private Object h;
   private int l = Integer.MIN_VALUE;
   private LivingEntity Z;
   private String s = "Waiting";
   private LivingEntity a;
   private int b;
   private int X;
   private int j;
   private double d;
   private boolean E;
   private LivingEntity T;
   private int n = Integer.MIN_VALUE;
   private boolean p;
   private String B = "";
   private String w = "";

   public Mace() {
      super("Mace", "Select a mace for smash hits, with optional smash assistance and Silent Aim", Category.COMBAT, -1);
      t = this;
      this.pick.visibleWhen(this.autoSwap::getValue);
      this.breachSwap.visibleWhen(this.autoSwap::getValue);
      this.swapBack.visibleWhen(() -> this.autoSwap.getValue() || this.yAsBoolean());
      this.smashTiming.visibleWhen(this::yAsBoolean);
      this.minFall.visibleWhen(this::yAsBoolean);
      this.mobs.visibleWhen(this::yAsBoolean);
      this.aimRange.visibleWhen(this.silentAim::getValue);
      this.x.visibleWhen(this.silentAim::getValue);
      this.turnSpeed.visibleWhen(this.silentAim::getValue);
      this.rotation.visibleWhen(this.silentAim::getValue);
      this.rangeRing.visibleWhen(this.silentAim::getValue);
      this.targetMark.visibleWhen(this.silentAim::getValue);
      this.silentAim.onChanged(var1 -> this.tAsVoid());
   }

   public static Mace yAsMace() {
      return t;
   }

   @Override
   public String getDisplayName() {
      return "Auto Mace";
   }

   @Override
   public Module.Type_s getTickPhase() {
      return Module.Type_s.INTENT;
   }

   private boolean yAsBoolean() {
      return this.smashAssist.getValue() || this.silentAim.getValue();
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(com.larpclient.module.k.VANILLA_INPUT);
   }

   @Override
   public String getStatus() {
      return this.s;
   }

   @Override
   public void onEnable() {
      this.uAsVoid();
      ClientLog.GAsVoid(
         "Mace",
         "Active; autoSwap={}, smashAssist={}, silentAim={}, timing={}, minFall={}, stunSlam={}, leadTurn={}, rangeRing={}, targetMark={}",
         this.autoSwap.getValue(),
         this.smashAssist.getValue(),
         this.silentAim.getValue(),
         this.smashTiming.getValue(),
         this.minFall.getValue(),
         this.y.getValue(),
         this.x.getValue(),
         this.rangeRing.getValue(),
         this.targetMark.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.uAsVoid();
      this.tAsVoid();
      this.S.vAsVoid(getMc(), this.swapBack.getValue());
      ClientLog.XAsVoid("Mace", "Disabled or context suspended; hotbar, view and sprint ownership released");
   }

   public static void eAsVoid() {
      if (t != null && t.isEnabled()) {
         Mace var0 = t;
         if (var0 != null && var0.isRuntimeActive()) {
            MinecraftClient var1 = getMc();
            var0.R = null;
            var0.h = null;
            var0.l = Integer.MIN_VALUE;
            if (var1.player != null && var1.player.isOnGround()) {
               var0.YAsVoid();
            }

            if (var0.T == null) {
               var0.S.IAsVoid(var1, var0.swapBack.getValue());
            }
         }
      }
   }

   private void YAsVoid() {
      if (this.E) {
         ClientLog.GAsVoid("Mace", "Landed before the server confirmed the smash on {}", xAsString(this.a));
      }

      this.e = this.E = false;
      this.a = null;
      this.j = 0;
      this.T = null;
      this.B = "";
   }

   public void LAsVoid(ClientPlayerEntity var1, Entity var2) {
      if (this.isEnabled() && this.isRuntimeActive() && var1 == getMc().player && var2 instanceof LivingEntity var3) {
         boolean var4 = MaceItem.shouldDealAdditionalDamage(var1);
         if (this.lAsBoolean(var1, var2)) {
            int var7 = ParticleKind.JAsInt(var1);
            if (this.S.uAsBoolean(var1, var7)) {
               ParticleKind.wAsVoid(var3);
               this.T = var3;
               this.n = var1.age;
               this.p = this.y.pAsBoolean("Same Tick");
               this.s = "Stun Slam";
               ClientLog.GAsVoid(
                  "Mace", "Stun Slam on {}: axe slot {} for your hit, smash {}", xAsString(var3), var7 + 1, this.p ? "in the same tick" : "next tick"
               );
            }
         } else if (this.autoSwap.getValue()
            && (!ParticleKind.PAsBoolean(var3, var1) || ParticleKind.NAsBoolean(var3))
            && (var4 || this.breachSwap.getValue() && !this.FAsBoolean("Breach Swap"))) {
            int var5 = this.CAsInt(var1, var3, var4);
            if (var5 >= 0
               && ActionOwnership.uAsBoolean(var4, EAsFloat(var1, var1.getInventory().getStack(var5), var3), EAsFloat(var1, var1.getMainHandStack(), var3))) {
               int var6 = var1.getInventory().getSelectedSlot();
               if (this.S.uAsBoolean(var1, var5) && var6 != var5) {
                  ClientLog.XAsVoid("Mace", "Selected hotbar slot {} for {} hit", var5 + 1, var4 ? "smash" : "Breach");
               }

               if (var4) {
                  this.dAsVoid(var1, var3);
               }
            }
         }
      }
   }

   private boolean FAsBoolean(String var1) {
      if (!ServerBrandMatcher.YAsBoolean()) {
         return false;
      } else {
         if (!var1.equals(this.w)) {
            ClientLog.GAsVoid("Mace", "{} skipped: {} resets the charge on a swap", var1, ServerBrandMatcher.AAsString());
         }

         this.w = var1;
         return true;
      }
   }

   public boolean lAsBoolean(ClientPlayerEntity var1, Entity var2) {
      if (!this.isEnabled()) {
         return false;
      } else {
         if (this.isRuntimeActive()
            && !this.y.pAsBoolean("Off")
            && var1 == getMc().player
            && var2 instanceof PlayerEntity var3
            && !ModuleImportBroker.pAsBoolean()
            && MaceItem.shouldDealAdditionalDamage(var1)) {
            ActionOwnership.Type_s var4 = ActionOwnership.iAsO3_Type_s(
               this.y.getValue(), ParticleKind.PAsBoolean(var3, var1), ParticleKind.NAsBoolean(var3), ParticleKind.JAsInt(var1) >= 0, this.fAsInt(var1, var3) >= 0
            );
            return var4 == ActionOwnership.Type_s.SAME_TICK || var4 == ActionOwnership.Type_s.NEXT_TICK;
         }

         return false;
      }
   }

   public void wAsVoid(ClientPlayerEntity var1, Entity var2) {
      if (this.isEnabled() && this.isRuntimeActive() && var2 == this.T && this.p && this.n == var1.age) {
         LivingEntity var3 = this.T;
         this.T = null;
         this.KAsBoolean(getMc(), var1, var3, "Stun Slam smash after your axe hit");
      }
   }

   private boolean KAsBoolean(MinecraftClient var1, ClientPlayerEntity var2, LivingEntity var3, String var4) {
      int var5 = this.fAsInt(var2, var3);
      if (var5 >= 0 && this.S.uAsBoolean(var2, var5)) {
         boolean var6 = ModuleImportBroker.DAsBoolean(this, var3, () -> {
            var1.interactionManager.attackEntity(var2, var3);
            var2.swingHand(Hand.MAIN_HAND);
         });
         if (var6) {
            this.dAsVoid(var2, var3);
            ClientLog.GAsVoid("Mace", "{}: fall {}, mace slot {}", var4, IAsString(var2.fallDistance), var5 + 1);
         }

         return var6;
      } else {
         ClientLog.GAsVoid("Mace", "{} skipped: no usable mace", var4);
         return false;
      }
   }

   private int fAsInt(ClientPlayerEntity var1, LivingEntity var2) {
      if (var1.getMainHandStack().isOf(Items.MACE) && !var1.getMainHandStack().willBreakNextUse()) {
         return var1.getInventory().getSelectedSlot();
      } else {
         return this.autoSwap.getValue() ? this.CAsInt(var1, var2, true) : -1;
      }
   }

   private int CAsInt(ClientPlayerEntity var1, LivingEntity var2, boolean var3) {
      ActionOwnership.Type_g[] var4 = new ActionOwnership.Type_g[PlayerInventory.getHotbarSize()];

      for (int var5 = 0; var5 < var4.length; var5++) {
         ItemStack var6 = var1.getInventory().getStack(var5);
         if (var6.isOf(Items.MACE) && !var6.willBreakNextUse()) {
            int var7 = vAsInt(var6, Enchantments.BREACH);
            if (var3 || var7 != 0) {
               var4[var5] = new ActionOwnership.Type_g(var5, true, false, var7, vAsInt(var6, Enchantments.WIND_BURST), EAsFloat(var1, var6, var2));
            }
         }
      }

      return ActionOwnership.JAsInt(var4, var3, this.pick.pAsBoolean("Wind Burst"));
   }

   private static float EAsFloat(ClientPlayerEntity var0, ItemStack var1, LivingEntity var2) {
      return ActionOwnership.AAsFloat(
         ParticleKind.CAsDouble(var0.getMainHandStack()),
         ParticleKind.jAsFloat(var0),
         var0.fallDistance,
         var1.isOf(Items.MACE),
         MaceItem.shouldDealAdditionalDamage(var0),
         vAsInt(var1, Enchantments.DENSITY),
         ParticleKind.CAsBoolean(var0, var2),
         vAsInt(var1, Enchantments.SHARPNESS),
         (float)var2.getArmor(),
         (float)var2.getAttributeValue(EntityAttributes.ARMOR_TOUGHNESS),
         vAsInt(var1, Enchantments.BREACH)
      );
   }

   private static int vAsInt(ItemStack var0, RegistryKey<Enchantment> var1) {
      for (Entry var3 : var0.getEnchantments().getEnchantmentEntries()) {
         if (((RegistryEntry)var3.getKey()).matchesKey(var1)) {
            return var3.getIntValue();
         }
      }

      return 0;
   }

   @Override
   public void onTick() {
      boolean var1 = this.z;
      this.z = false;
      MinecraftClient var2 = getMc();
      if (var2.player != null) {
         this.bAsVoid(var2.player);
      }

      this.s = this.e ? (this.E ? "Waiting for the server" : "Smash used this fall") : "Waiting";
      if (this.T != null && !this.p) {
         this.JAsVoid(var2);
      } else {
         this.T = this.p && this.T != null && var2.player != null && this.n == var2.player.age ? this.T : null;
         if (!this.yAsBoolean()) {
            this.tAsVoid();
         } else {
            LivingEntity var3 = this.silentAim.getValue() ? this.YAsLivingEntity(var2) : null;
            if (var3 == null) {
               this.tAsVoid();
            }

            if (ParticleKind.FAsBoolean(var2, ParticleEmitter.Type_u.MACE) && ParticleGeometry.yAsBoolean(this)) {
               ClientPlayerEntity var4 = var2.player;
               EntityHitResult var5 = ParticleKind.CAsEntityHitResult(var2);
               LivingEntity var6 = this.eAsLivingEntity(var4, var5, var3);
               int var7 = var6 == null && var3 == null ? 0 : 1;
               String var8 = ActionOwnership.WAsString(
                  ParticleKind.BAsBoolean(var4),
                  ParticleKind.BAsBoolean(var2),
                  ParticleKind.IAsBoolean(var2),
                  var3 == null && ModuleDetector.YAsBoolean(),
                  ParticleKind.hAsBoolean(var2, var4),
                  var4.getVelocity().y < 0.0,
                  MaceItem.shouldDealAdditionalDamage(var4),
                  var4.fallDistance,
                  this.minFall.getValue(),
                  this.e && !this.E,
                  this.E,
                  var6 != null && this.fAsInt(var4, var6) >= 0,
                  var6 != null
               );
               if (var8 == null) {
                  this.hAsVoid(var2, var4, var5, var6, var3, var1);
               } else if (var7 != 0) {
                  this.pAsVoid(var8, var4, var3);
               }

               if (var3 != null && (!this.e || this.E)) {
                  if (!ParticleKind.BAsBoolean(var4)) {
                     this.CAsVoid(var2, var3);
                  }
               } else {
                  this.tAsVoid();
               }
            } else {
               this.tAsVoid();
            }
         }
      }
   }

   private LivingEntity eAsLivingEntity(ClientPlayerEntity var1, EntityHitResult var2, LivingEntity var3) {
      if (var2 == null || !(var2.getEntity() instanceof LivingEntity var4)) {
         return null;
      } else if (!this.smashAssist.getValue() && var4 != var3) {
         return null;
      } else {
         return ParticleKind.FAsBoolean(var1, var4) && (var4 instanceof PlayerEntity || this.mobs.getValue()) ? var4 : null;
      }
   }

   private void pAsVoid(String var1, ClientPlayerEntity var2, LivingEntity var3) {
      if (!var1.equals(this.B)) {
         this.B = var1;
         ClientLog.GAsVoid(
            "Mace",
            "Smash held: {}; fall {}, speed {}{}",
            var1,
            IAsString(var2.fallDistance),
            IAsString(var2.getVelocity().y),
            var3 == null ? "" : ", Silent Aim target " + xAsString(var3) + " at " + IAsString(OptifineBridge.lAsDouble(var2.getEyePos(), var3.getBoundingBox()))
         );
      }
   }

   private void hAsVoid(MinecraftClient var1, ClientPlayerEntity var2, EntityHitResult var3, LivingEntity var4, LivingEntity var5, boolean var6) {
      this.B = "";
      ActionOwnership.Type_s var7 = ActionOwnership.iAsO3_Type_s(
         this.y.getValue(), ParticleKind.PAsBoolean(var4, var2), ParticleKind.NAsBoolean(var4), ParticleKind.JAsInt(var2) >= 0, this.fAsInt(var2, var4) >= 0
      );
      if ((var7 != ActionOwnership.Type_s.NEXT_TICK || !PAsBoolean(var2, var4)) && var7 != ActionOwnership.Type_s.BREAK_ONLY) {
         if (var7 == ActionOwnership.Type_s.NONE && ParticleKind.PAsBoolean(var4, var2) && !ParticleKind.NAsBoolean(var4)) {
            this.pAsVoid("raised shield and no axe in the hotbar", var2, var5);
         } else if (this.jAsBoolean(var2, var4)) {
            this.z = true;
            this.R = var2;
            this.h = var1.world;
            this.l = var2.age;
            var2.setSprinting(false);
            ParticleKind.uAsVoid(var1, Math.max(ParticleKind.kAsInt(var1), 1));
            this.s = "Delaying smash";
            if (!var6) {
               ClientLog.XAsVoid("Mace", "Delaying smash while the predicted next step remains in reach");
            }
         } else if (var7 != ActionOwnership.Type_s.NONE) {
            this.SAsVoid(var1, var2, var4, var7);
         } else {
            int var8 = this.fAsInt(var2, var4);
            if (var8 == var2.getInventory().getSelectedSlot() || this.S.uAsBoolean(var2, var8)) {
               if (ParticleKind.MAsBoolean(var1, var3)) {
                  this.dAsVoid(var2, var4);
                  ParticleKind.DAsVoid(var1);
                  ClientLog.GAsVoid(
                     "Mace",
                     "Smash sent on {}: fall {}, speed {}, silent={}",
                     xAsString(var4),
                     IAsString(var2.fallDistance),
                     IAsString(var2.getVelocity().y),
                     var4 == var5
                  );
               } else {
                  ClientLog.GAsVoid("Mace", "Smash attack refused by the client gate on {}", xAsString(var4));
               }
            }
         }
      } else {
         this.SAsVoid(var1, var2, var4, var7);
      }
   }

   private void SAsVoid(MinecraftClient var1, ClientPlayerEntity var2, LivingEntity var3, ActionOwnership.Type_s var4) {
      int var5 = ParticleKind.JAsInt(var2);
      int var6 = this.fAsInt(var2, var3);
      if (this.S.uAsBoolean(var2, var5)) {
         int var7 = var4 == ActionOwnership.Type_s.SAME_TICK ? 1 : 0;
         boolean var8 = ModuleImportBroker.WAsBoolean(this, var3, () -> {
            var1.interactionManager.attackEntity(var2, var3);
            var2.swingHand(Hand.MAIN_HAND);
            ParticleKind.wAsVoid(var3);
            if (var7 != 0 && this.S.uAsBoolean(var2, var6)) {
               var1.interactionManager.attackEntity(var2, var3);
               var2.swingHand(Hand.MAIN_HAND);
            }
         });
         if (!var8) {
            this.S.vAsVoid(var1, true);
            ClientLog.GAsVoid("Mace", "Stun Slam refused: this tick's attack belongs to another owner");
         } else {
            ParticleKind.DAsVoid(var1);
            if (var7 != 0) {
               this.dAsVoid(var2, var3);
               this.s = "Stun Slam";
               ClientLog.GAsVoid(
                  "Mace",
                  "Stun Slam on {}: axe slot {} then mace slot {} in one tick, fall {}",
                  xAsString(var3),
                  var5 + 1,
                  var6 + 1,
                  IAsString(var2.fallDistance)
               );
            } else if (var4 == ActionOwnership.Type_s.NEXT_TICK) {
               this.T = var3;
               this.n = var2.age;
               this.p = false;
               this.s = "Stun Slam";
               ClientLog.GAsVoid("Mace", "Stun Slam on {}: axe slot {} now, smash next tick", xAsString(var3), var5 + 1);
            } else {
               this.s = "Shield break hit";
               ClientLog.GAsVoid("Mace", "Shield break hit on {} with axe slot {}; no mace for the smash", xAsString(var3), var5 + 1);
            }
         }
      }
   }

   private void JAsVoid(MinecraftClient var1) {
      ClientPlayerEntity var2 = var1.player;
      LivingEntity var3 = this.T;
      if (var2 != null && var2.age == this.n + 1) {
         this.T = null;
         EntityHitResult var4 = ParticleKind.CAsEntityHitResult(var1);
         if (var4 != null && var4.getEntity() == var3 && MaceItem.shouldDealAdditionalDamage(var2) && !var2.isOnGround()) {
            int var5 = this.fAsInt(var2, var3);
            if (var5 >= 0 && this.S.uAsBoolean(var2, var5)) {
               boolean var6 = ModuleImportBroker.WAsBoolean(this, var3, () -> {
                  var1.interactionManager.attackEntity(var2, var3);
                  var2.swingHand(Hand.MAIN_HAND);
               });
               if (var6) {
                  this.dAsVoid(var2, var3);
                  ParticleKind.DAsVoid(var1);
                  ClientLog.GAsVoid("Mace", "Stun Slam smash on {} one tick after the axe: fall {}", xAsString(var3), IAsString(var2.fallDistance));
               } else {
                  ClientLog.GAsVoid("Mace", "Next Tick smash on {} refused: this tick's attack belongs to another owner", xAsString(var3));
               }
            } else {
               this.S.vAsVoid(var1, this.swapBack.getValue());
            }
         } else {
            ClientLog.GAsVoid(
               "Mace",
               "Next Tick smash on {} skipped: {}",
               xAsString(var3),
               var2.isOnGround() ? "landed" : (!MaceItem.shouldDealAdditionalDamage(var2) ? "fall ended" : "facing left the target")
            );
            this.S.vAsVoid(var1, this.swapBack.getValue());
         }
      } else if (var2 == null || var2.age > this.n + 1) {
         this.T = null;
         this.S.vAsVoid(var1, this.swapBack.getValue());
      }
   }

   private void dAsVoid(ClientPlayerEntity var1, LivingEntity var2) {
      this.e = this.E = true;
      this.a = var2;
      this.b = var1.age;
      this.X = var2.hurtTime;
      this.d = var1.getVelocity().y;
      this.s = "Waiting for the server";
   }

   private void bAsVoid(ClientPlayerEntity var1) {
      if (this.E && this.a != null) {
         if (ActionOwnership.tAsBoolean(this.d, var1.getVelocity().y, this.X, this.a.hurtTime)) {
            this.E = false;
            ClientLog.GAsVoid("Mace", "Smash confirmed on {} after {} ticks", xAsString(this.a), var1.age - this.b);
         } else if (var1.age - this.b >= ActionOwnership.XAsInt(this.xAsInt(var1))) {
            this.E = false;
            if (this.j < 1 && !var1.isOnGround() && MaceItem.shouldDealAdditionalDamage(var1)) {
               this.j++;
               this.e = false;
               ClientLog.GAsVoid(
                  "Mace", "No server confirmation for the smash on {} after {} ticks; retrying once this fall", xAsString(this.a), var1.age - this.b
               );
            } else {
               ClientLog.GAsVoid(
                  "Mace", "No server confirmation for the smash on {}; not retrying ({})", xAsString(this.a), this.j >= 1 ? "already retried" : "fall ended"
               );
            }
         }
      }
   }

   private int xAsInt(ClientPlayerEntity var1) {
      ClientPlayNetworkHandler var2 = getMc().getNetworkHandler();
      PlayerListEntry var3 = var2 == null ? null : var2.getPlayerListEntry(var1.getUuid());
      return var3 == null ? 0 : Math.round((float)var3.getLatency() / 50.0F);
   }

   private LivingEntity YAsLivingEntity(MinecraftClient var1) {
      ClientPlayerEntity var2 = var1.player;
      if (var2 != null && var1.world != null && (!this.e || this.E) && !var2.isOnGround() && !(var2.getVelocity().y >= 0.0) && ParticleEmitter.fAsBoolean(ParticleEmitter.Type_u.MACE)) {
         if (this.Z != null && this.nAsBoolean(var2, this.Z, this.CAsDouble(var2, this.Z))) {
            return this.Z;
         } else {
            Vec3d var3 = var2.getEyePos();
            LivingEntity var6 = null;
            double var7 = Double.MAX_VALUE;

            for (Entity var10 : var1.world
               .getOtherEntities(var2, var2.getBoundingBox().expand(Math.max(this.aimRange.getValue(), 16.0)), var0 -> var0 instanceof LivingEntity)) {
               LivingEntity var11 = (LivingEntity)var10;
               double var12 = OptifineBridge.lAsDouble(var3, var11.getBoundingBox());
               if (var12 < var7 && this.nAsBoolean(var2, var11, this.CAsDouble(var2, var11))) {
                  var6 = var11;
                  var7 = var12;
               }
            }

            return var6;
         }
      } else {
         return null;
      }
   }

   private double CAsDouble(ClientPlayerEntity var1, LivingEntity var2) {
      RotationAngles var3 = new RotationAngles(var1.getYaw(), var1.getPitch());
      int var4 = this.nAsH().ticksToReach(var3, lk.XAsL6(eAsVec3d(var1, var2)));
      return ActionOwnership.lAsDouble(this.aimRange.getValue(), this.x.getValue(), -var1.getVelocity().y, var4, var1.getEntityInteractionRange());
   }

   public double YAsDouble(ClientPlayerEntity var1) {
      if (!this.isEnabled()) {
         return 0.0;
      } else if (!this.isRuntimeActive()
         || !this.silentAim.getValue()
         || var1 == null
         || this.rangeRing.pAsBoolean("Off")
         || this.rangeRing.pAsBoolean("In Air") && var1.isOnGround()) {
         return 0.0;
      } else {
         RotationAngles var2 = new RotationAngles(var1.getYaw(), var1.getPitch());
         int var3 = this.nAsH().ticksToReach(var2, new RotationAngles(var1.getYaw(), 90.0F));
         return ActionOwnership.lAsDouble(this.aimRange.getValue(), this.x.getValue(), -var1.getVelocity().y, var3, var1.getEntityInteractionRange());
      }
   }

   public LivingEntity JAsLivingEntity() {
      if (!this.isEnabled()) {
         return null;
      } else {
         return this.targetMark.getValue() && this.rAsBoolean() ? this.Z : null;
      }
   }

   private boolean nAsBoolean(ClientPlayerEntity var1, LivingEntity var2, double var3) {
      Vec3d var5 = var1.getEyePos();
      return ParticleKind.FAsBoolean(var1, var2)
         && (var2 instanceof PlayerEntity || this.mobs.getValue())
         && OptifineBridge.lAsDouble(var5, var2.getBoundingBox()) <= var3
         && this.aAsBoolean(var1, var2)
         && ParticleKind.WAsBoolean(var1, var5, eAsVec3d(var1, var2));
   }

   private static Vec3d eAsVec3d(ClientPlayerEntity var0, LivingEntity var1) {
      Box var2 = var1.getBoundingBox();
      Vec3d var3 = var0.getEyePos();
      Box var4 = var2.contract(Math.min(0.1, var2.getLengthX() * 0.25), Math.min(0.1, var2.getLengthY() * 0.25), Math.min(0.1, var2.getLengthZ() * 0.25));
      Vec3d var5 = new Vec3d(Math.clamp(var3.x, var4.minX, var4.maxX), Math.clamp(var3.y, var4.minY, var4.maxY), Math.clamp(var3.z, var4.minZ, var4.maxZ));
      Vec3d var6 = new Vec3d(var2.getCenter().x, var2.minY + var2.getLengthY() * 0.6, var2.getCenter().z);
      return ModuleImportBroker.nAsOptional(var1, var0.getEntityInteractionRange(), var6, com.larpclient.BoxUtils.sAsVec3d(var2, var5)).orElse(var6);
   }

   private h nAsH() {
      String var1 = this.rotation.getValue();

      return (h)(switch (var1) {
         case "FunTime" -> this.N;
         case "Polar" -> this.g;
         case "Matrix" -> this.A;
         case "Legit" -> this.k;
         default -> this.f;
      });
   }

   private void CAsVoid(MinecraftClient var1, LivingEntity var2) {
      ClientPlayerEntity var3 = var1.player;
      if (!ModuleImportBroker.nAsBoolean(this)) {
         this.tAsVoid();
      } else {
         h var4 = this.nAsH();
         if (var4 != this.I) {
            if (this.I != null) {
               this.I.reset();
            }

            this.I = var4;
         }

         Box var5 = var2.getBoundingBox();
         if (var2 != this.Z) {
            this.I.targetChanged(var2);
            ClientLog.GAsVoid(
               "Mace",
               "Silent Aim target {} at {} blocks, falling {} blocks/tick, rotation={}",
               xAsString(var2),
               IAsString(OptifineBridge.lAsDouble(var3.getEyePos(), var5)),
               IAsString(-var3.getVelocity().y),
               this.rotation.getValue()
            );
         }

         this.Z = var2;
         ParticleEmitter.jAsVoid(ParticleEmitter.Type_u.MACE);
         if (ParticleEmitter.NAsP5_Type_u() != ParticleEmitter.Type_u.MACE) {
            this.Z = null;
         } else {
            RotationAngles var6 = new RotationAngles(var3.getYaw(), var3.getPitch());
            double var7 = var3.getEntityInteractionRange();
            int var9 = OptifineBridge.lAsDouble(var3.getEyePos(), var5) <= var7 ? 1 : 0;
            ClientPlayerEntityAccessor var10 = (ClientPlayerEntityAccessor)var3;
            boolean var11 = com.larpclient.module.y.BAsBoolean(
               var3, var5.expand((double)var2.getTargetingMargin()), var10.larpYRotLast(), var10.larpXRotLast(), var7 + 1.0, false
            );
            RotationAngles var12 = this.I.rotate(var6, lk.XAsL6(eAsVec3d(var3, var2)), var2, var9 != 0, var11);
            float[] var13 = com.larpclient.SmoothInput.JAsFloatArray(var6.n, var6.m, var12.n, var12.m, this.turnSpeed.getValue());
            var12 = new RotationAngles(var13[0], var13[1]);
            var12 = this.I == this.k ? this.k.vAsL6(var6, var12) : lk.UAsL6(var6, var12);
            ModuleDetector.RAsVoid(this, ModuleDetector.Type_s.MACE, var12.n, var12.m, 1.0);
            if (!ModuleDetector.CAsBoolean(ModuleDetector.Type_s.MACE)) {
               ClientLog.XAsVoid("Mace", "Silent Aim yielded to a higher rotation owner");
               this.tAsVoid();
            } else if (!this.z) {
               this.s = "Silent aim";
            }
         }
      }
   }

   private void tAsVoid() {
      if (this.Z != null && this.I != null) {
         this.I.reset();
      }

      this.Z = null;
      ParticleEmitter.GAsVoid(ParticleEmitter.Type_u.MACE);
   }

   public float UAsFloat(float var1, float var2) {
      if (this.isEnabled()) {
         return this.rAsBoolean() ? Aura.WAsFloat(var1, var2, 35.0F, 0.25F) : var1;
      } else {
         return var1;
      }
   }

   public boolean rAsBoolean() {
      return this.isEnabled()
         ? this.isRuntimeActive() && this.silentAim.getValue() && this.Z != null && this.Z.isAlive() && !this.Z.isRemoved() && ModuleImportBroker.fAsBoolean(this)
         : false;
   }

   public void jAsVoid() {
      if (this.isEnabled()) {
         this.e = true;
         this.s = "Smash used this fall";
      }
   }

   public boolean aAsBoolean(ClientPlayerEntity var1, LivingEntity var2) {
      if (!this.isEnabled()) {
         return false;
      } else {
         boolean var3 = var1.getMainHandStack().isOf(Items.MACE);
         int var4 = !var3
               && this.autoSwap.getValue()
               && this.isRuntimeActive()
               && !this.e
               && MaceItem.shouldDealAdditionalDamage(var1)
               && var1.fallDistance >= this.minFall.getValue()
               && this.CAsInt(var1, var2, true) >= 0
            ? 1
            : 0;
         return ActionOwnership.TAsBoolean(
            this.isRuntimeActive(),
            this.e,
            MaceItem.shouldDealAdditionalDamage(var1),
            var1.fallDistance,
            this.minFall.getValue(),
            var3,
            this.autoSwap.getValue(),
            var4 != 0
         );
      }
   }

   public boolean jAsBoolean(ClientPlayerEntity var1, LivingEntity var2) {
      return this.isEnabled()
         ? ActionOwnership.wAsBoolean(
            PAsBoolean(var1, var2),
            this.smashTiming.pAsBoolean("Delayed"),
            ParticleKind.jAsFloat(var1),
            var1.getAttackCooldownProgress(1.5F),
            var2.hurtTime,
            var1.isSprinting()
         )
         : false;
   }

   private static boolean PAsBoolean(ClientPlayerEntity var0, LivingEntity var1) {
      Vec3d var2 = var0.getVelocity();
      return ActionOwnership.bAsBoolean(
         var0.getEntityWorld().isSpaceEmpty(var0, var0.getBoundingBox().offset(var2)),
         var0.getEntityInteractionRange(),
         var0.getEyePos(),
         var2,
         var1.getBoundingBox(),
         var1.getEntityPos().subtract(var1.lastX, var1.lastY, var1.lastZ),
         var1.getTargetingMargin(),
         var0.getRotationVec(1.0F),
         (var1x, var2x) -> ParticleKind.WAsBoolean(var0, var1x, var2x)
      );
   }

   public static boolean uAsBoolean() {
      if (t != null && t.isEnabled()) {
         Mace var0 = t;
         if (var0 != null && var0.isRuntimeActive() && var0.yAsBoolean()) {
            MinecraftClient var1 = getMc();
            if (var1.player == null) {
               return false;
            } else {
               return ParticleEmitter.NAsP5_Type_u() == ParticleEmitter.Type_u.MACE
                  ? true
                  : var1.player == var0.R
                     && var1.world == var0.h
                     && var1.currentScreen == null
                     && var1.player.age >= var0.l
                     && (long)var1.player.age - (long)var0.l <= 1L;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static PlayerInput TAsPlayerInput(ClientPlayerEntity var0, PlayerInput var1) {
      if (t != null && t.isEnabled()) {
         Mace var2 = t;
         if (var2 != null && var2.isRuntimeActive() && var0 != null && var0 == getMc().player && ParticleEmitter.NAsP5_Type_u() == ParticleEmitter.Type_u.MACE) {
            if (var0.isSprinting()) {
               var0.setSprinting(false);
               ClientLog.XAsVoid("Mace", "Silent Aim lock released sprint");
            }

            return ParticleKind.cAsPlayerInput(var1);
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   private void uAsVoid() {
      this.e = this.E = false;
      this.z = false;
      this.a = null;
      this.j = 0;
      this.T = null;
      this.B = "";
      this.R = null;
      this.h = null;
      this.l = Integer.MIN_VALUE;
      this.s = "Waiting";
   }

   private static String xAsString(Entity var0) {
      return var0 == null ? "?" : var0.getName().getString();
   }

   private static String IAsString(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   private static BooleanSetting pAsBooleanSetting(String var0, String var1, String var2, boolean var3) {
      return new BooleanSetting(var0, var2, var3) {
         @Override
         public String getDisplayName() {
            return var1;
         }
      };
   }

   private static NumberSetting DAsNumberSetting(String var0, String var1, String var2, double var3, double var5, double var7, double var9) {
      return new NumberSetting(var0, var2, var3, var5, var7, var9) {
         @Override
         public String getDisplayName() {
            return var1;
         }
      };
   }

   private static ModeSetting QAsModeSetting(String var0, String var1, String var2, String var3, String... var4) {
      return new ModeSetting(var0, var2, var3, var4) {
         @Override
         public String getDisplayName() {
            return var1;
         }
      };
   }
}
