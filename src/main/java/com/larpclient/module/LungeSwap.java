package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.KeybindNames;
import com.larpclient.LarpClient;
import com.larpclient.LungeController;
import com.larpclient.ServerBrandMatcher;
import com.larpclient.ModuleImportBroker;
import com.larpclient.ParticleGeometry;
import com.larpclient.ParticleKind;
import com.larpclient.mixin.LivingEntityAccessor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PiercingWeaponComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry.Reference;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

public final class LungeSwap extends Module {
   private static LungeSwap Z;
   public static final long e = 15L;
   public static final long C = 30L;
   public static final long n = 10L;
   public static final int R = 10;
   public static final int h = 12;
   public static final double G = 0.4;
   private final ModeSetting mode = this.addSetting(
      new ModeSetting("Mode", "Legit waits for full charges like vanilla; Blatant jabs as soon as the server accepts it", "Legit", "Legit", "Blatant")
   );
   private final KeybindSetting activationKey = this.addSetting(
      new KeybindSetting("Activation Key", "Press to swap to your Lunge spear and jab; separate from the module toggle", 86)
   );
   private final BooleanSetting swapBack = this.addSetting(new BooleanSetting("Swap Back", "Return after the jab; Blatant always returns", true));
   private final BooleanSetting swapBackToSlot = this.addSetting(
      new BooleanSetting("Swap Back To Slot", "Return to a fixed hotbar slot instead of your previous one", false)
   );
   private final NumberSetting swapBackSlot = this.addSetting(new NumberSetting("Swap Back Slot", "Hotbar slot to return to", 1.0, 1.0, 9.0, 1.0));
   private final BooleanSetting holdToLunge = this.addSetting(new BooleanSetting("Hold To Lunge", "Keep jabbing while the activation key is held", false));
   private final BooleanSetting chargeAfterSwap = this.addSetting(
      new BooleanSetting(
         "Charge After Swap", "On a server that resets charge on a swap, hold the spear until its own charge is ready. Off always jabs in the swap tick", true
      )
   );
   private final ModeSetting swapRule = this.addSetting(
      new ModeSetting(
         "Swap Rule",
         "How the server treats charge across a swap. Detect jabs in the swap tick and waits for the spear's charge on a server where that did not lunge",
         "Detect",
         "Detect",
         "Kept",
         "Reset"
      )
   );
   private static final Map<String, Boolean> A = new HashMap<>();
   private LungeSwap.Type_f x = LungeSwap.Type_f.IDLE;
   private boolean V;
   private long T;
   private int F = -1;
   private int Q = -1;
   private int W;
   private int j;
   private int r;
   private int S;
   private int E;
   private LungeController.Type_n f = LungeController.Type_n.KEPT;
   private String y = "";
   private String q = "";
   private float U;
   private ClientPlayerEntity z;
   private ClientWorld H;
   private String i = "Ready";
   private String X = "";
   private int N;
   private int o;
   private double s;
   private boolean K;
   private boolean Y;
   private Vec3d k = Vec3d.ZERO;

   public LungeSwap() {
      super("Lunge Swap", "Swap to your Lunge spear and jab with an activation key", Category.COMBAT, -1);
      Z = this;
      this.swapBack.visibleWhen(this::BAsBoolean);
      this.swapBackToSlot.visibleWhen(this::oAsBoolean);
      this.swapBackSlot.visibleWhen(() -> this.oAsBoolean() && this.swapBackToSlot.getValue());
      this.swapRule.visibleWhen(this.chargeAfterSwap::getValue);
      this.mode.onChanged(var1 -> this.uAsVoid(true));
      this.activationKey.onChanged(var1 -> {
         this.uAsVoid(true);
         this.N = 0;
      });
      this.swapRule.onChanged(var1 -> this.uAsVoid(true));
   }

   private boolean BAsBoolean() {
      return this.mode.pAsBoolean("Legit");
   }

   private boolean YAsBoolean() {
      return this.mode.pAsBoolean("Blatant");
   }

   private boolean oAsBoolean() {
      return this.YAsBoolean() || this.swapBack.getValue();
   }

   public static int yAsInt(boolean var0) {
      return LungeController.NAsInt(var0);
   }

   public static long qAsLong(boolean var0, double var1) {
      return var0 ? 10000000L : (15L + Math.min(15L, (long)(Math.clamp(var1, 0.0, 1.0) * 16.0))) * 1000000L;
   }

   public static int AAsInt(boolean var0, boolean var1, int var2, int var3, int var4, int var5) {
      int var6 = var1 ? var2 : var3;
      return var0 && var4 == var5 && var6 >= 0 && var6 < 9 && var6 != var4 ? var6 : -1;
   }

   public static LungeController.Type_n pAsMh_Type_n(boolean var0, String var1, Boolean var2, boolean var3) {
      if (!var0 || "Kept".equals(var1)) {
         return LungeController.Type_n.KEPT;
      } else if ("Reset".equals(var1)) {
         return LungeController.Type_n.RESET;
      } else if (var2 != null) {
         return var2 ? LungeController.Type_n.RESET : LungeController.Type_n.KEPT;
      } else {
         return var3 ? LungeController.Type_n.RESET : LungeController.Type_n.KEPT;
      }
   }

   public static void SAsVoid(int var0) {
      if (Z != null && Z.isEnabled()) {
         LungeSwap var1 = Z;
         if (var1 != null && var1.isRuntimeActive() && KeybindNames.hAsBoolean(var0) && var0 == var1.activationKey.getValue()) {
            var1.V = true;
         }
      }
   }

   public boolean yAsBoolean() {
      return this.isEnabled() ? this.isRuntimeActive() && this.x != LungeSwap.Type_f.IDLE && this.z != null && this.z == getMc().player : false;
   }

   @Override
   public Module.Type_s getTickPhase() {
      return Module.Type_s.INTENT;
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(com.larpclient.module.k.VANILLA_INPUT, com.larpclient.module.k.SERVER_STATE_MODIFYING);
   }

   @Override
   public String getStatus() {
      if (!this.isEnabled()) {
         return "";
      } else {
         return this.x == LungeSwap.Type_f.IDLE && !this.X.isEmpty()
            ? KeybindNames.eAsString(this.activationKey.getValue()) + " also triggers " + this.X
            : this.i;
      }
   }

   @Override
   public void onEnable() {
      this.vAsVoid();
      this.z = mc.player;
      this.H = mc.world;
      this.i = "Ready";
      this.X = "";
      this.N = 0;
      ClientLog.GAsVoid(
         "LungeSwap",
         "Enabled; mode={}, activation key={}, hold={}, charge after swap={}, swap rule={}",
         this.mode.getValue(),
         this.activationKey.getValue(),
         this.holdToLunge.getValue(),
         this.chargeAfterSwap.getValue(),
         this.swapRule.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.uAsVoid(true);
      this.z = null;
      this.H = null;
   }

   private boolean HAsBoolean() {
      return this.isInGame()
         && this.z == mc.player
         && this.H == mc.world
         && mc.player.isAlive()
         && !mc.player.isSpectator()
         && mc.currentScreen == null
         && pk.eAsBoolean(mc);
   }

   @Override
   public void onTick() {
      boolean var1 = this.V;
      this.V = false;
      if (--this.N <= 0) {
         this.N = 20;
         this.iAsVoid();
      }

      if (this.z != mc.player || this.H != mc.world) {
         this.uAsVoid(false);
         this.z = mc.player;
         this.H = mc.world;
         this.o = 0;
      }

      if (this.o > 0) {
         this.nAsVoid();
      }

      if (!this.HAsBoolean()) {
         this.uAsVoid(true);
         this.i = "Paused";
      } else {
         long var2 = System.nanoTime();
         boolean var4 = KeybindNames.sAsBoolean(mc.getWindow().getHandle(), this.activationKey.getValue());
         if (this.x == LungeSwap.Type_f.IDLE && (var1 || this.holdToLunge.getValue() && var4)) {
            this.RAsVoid(var1);
         }

         if (this.x == LungeSwap.Type_f.WAITING) {
            this.kAsVoid();
         } else if (this.x == LungeSwap.Type_f.CHARGING) {
            this.wAsVoid();
         } else {
            long var5;
            if (this.x == LungeSwap.Type_f.SWAPPED && ((var5 = var2 - this.T - 0L) == 0L ? 0 : (var5 < 0L ? -1 : 1)) >= 0) {
               this.NAsVoid();
               this.i = "Ready";
            } else if (this.x == LungeSwap.Type_f.IDLE && !KeybindNames.hAsBoolean(this.activationKey.getValue())) {
               this.i = "Set an Activation Key";
            }
         }
      }
   }

   public static String VAsString(Module var0, int var1, Iterable<Module> var2) {
      if (!KeybindNames.hAsBoolean(var1)) {
         return "";
      } else if (var0.getKeyBind() == var1) {
         return "the Lunge Swap toggle";
      } else {
         for (Module var4 : var2) {
            if (var4 != var0) {
               if (var4.getKeyBind() == var1) {
                  return var4.getName();
               }

               for (Setting var6 : var4.getSettings()) {
                  if (var6 instanceof KeybindSetting var7 && var7.getValue() == var1) {
                     return var4.getName();
                  }
               }
            }
         }

         return "";
      }
   }

   private void iAsVoid() {
      LarpClient var1 = LarpClient.CAsLarpClient();
      int var2 = this.activationKey.getValue();
      String var3 = var1 != null && var1.mAsF() != null ? VAsString(this, var2, var1.mAsF().NAsList()) : "";
      if (!var3.isEmpty() && !var3.equals(this.X)) {
         ClientLog.oAsVoid(
            "LungeSwap",
            "Activation Key {} is also bound to {}; one press triggers both, and a movement module such as Fly cancels the lunge. Rebind one of them",
            KeybindNames.eAsString(var2),
            var3
         );
      }

      this.X = var3;
   }

   private void nAsVoid() {
      ClientPlayerEntity var1 = mc.player;
      this.s = Math.max(this.s, var1.getVelocity().horizontalLength());
      if (--this.o <= 0) {
         int var2 = this.s >= 0.4 ? 1 : 0;
         ClientLog.GAsVoid(
            "LungeSwap",
            "Jab result: peak horizontal speed {} blocks/tick, moved {} blocks in {} ticks ({})",
            String.format(Locale.ROOT, "%.3f", this.s),
            String.format(Locale.ROOT, "%.2f", var1.getEntityPos().subtract(this.k).horizontalLength()),
            12,
            var2 != 0 ? "lunge push arrived" : "no lunge push; the server refused it or another movement owner replaced it"
         );
         if (this.K && this.chargeAfterSwap.getValue() && this.swapRule.pAsBoolean("Detect")) {
            String var3 = this.YAsString();
            Boolean var4 = A.get(var3);
            A.put(var3, var2 == 0);
            if (var2 == 0 && !Boolean.TRUE.equals(var4)) {
               ClientLog.GAsVoid("LungeSwap", "A swap-tick jab did not lunge on {}; later presses there wait for the spear's own charge", var3);
            } else if (var2 != 0 && var4 == null) {
               ClientLog.GAsVoid("LungeSwap", "{} keeps the charge across a swap; presses there jab in the swap tick", var3);
            }
         }
      }
   }

   private String YAsString() {
      ServerInfo var1 = mc.getCurrentServerEntry();
      return var1 != null ? var1.address : (mc.isIntegratedServerRunning() ? "singleplayer" : "this server");
   }

   private void RAsVoid(boolean var1) {
      String var2 = this.PAsString();
      if (var2 != null) {
         this.i = var2;
         if (var1) {
            ClientLog.GAsVoid("LungeSwap", "Press refused: {}", var2);
         }
      } else if (this.EAsMh_Type_g() == null) {
         this.i = "No Lunge spear in hotbar";
         if (var1) {
            ClientLog.GAsVoid("LungeSwap", "Press ignored: no Lunge spear in the hotbar");
         }
      } else {
         this.F = mc.player.getInventory().getSelectedSlot();
         this.y = mc.player.getMainHandStack().getName().getString();
         this.U = mc.player.getAttackCooldownProgress(0.0F);
         this.f = pAsMh_Type_n(this.chargeAfterSwap.getValue(), this.swapRule.getValue(), A.get(this.YAsString()), ServerBrandMatcher.YAsBoolean());
         this.W = this.j = 0;
         this.r = -1;
         this.q = "";
         this.x = LungeSwap.Type_f.WAITING;
      }
   }

   private String PAsString() {
      ClientPlayerEntity var1 = mc.player;
      return LungeController.NAsString(
         var1.getHungerManager().getFoodLevel(), var1.getAbilities().creativeMode, var1.isTouchingWater(), var1.hasVehicle(), var1.isGliding()
      );
   }

   private LungeController.Type_g EAsMh_Type_g() {
      ClientPlayerEntity var1 = mc.player;
      ItemStack var2 = var1.getMainHandStack();
      ArrayList var3 = new ArrayList();

      for (int var4 = 0; var4 < 9; var4++) {
         int var5 = this.tAsInt(var4);
         if (var5 > 0) {
            ItemStack var6 = var1.getInventory().getStack(var4);
            if (var6.isItemEnabled(this.H.getEnabledFeatures())) {
               var3.add(
                  new LungeController.Type_c(
                     var4,
                     JAsFloat(var1, var6),
                     (Float)var6.getOrDefault(DataComponentTypes.MINIMUM_ATTACK_CHARGE, 0.0F),
                     var5,
                     var6.isDamageable() ? var6.getMaxDamage() - var6.getDamage() : Integer.MAX_VALUE,
                     var6.willBreakNextUse(),
                     ItemStack.areItemsEqual(var6, var2)
                  )
               );
            }
         }
      }

      return LungeController.fAsMh_Type_g(
         var3,
         var1.getInventory().getSelectedSlot(),
         this.f,
         ((LivingEntityAccessor)var1).larpAttackStrengthTicker(),
         var1.getAttackCooldownProgressPerTick(),
         this.YAsBoolean()
      );
   }

   private static float JAsFloat(ClientPlayerEntity var0, ItemStack var1) {
      EntityAttributeInstance var2 = var0.getAttributeInstance(EntityAttributes.ATTACK_SPEED);
      if (var2 == null) {
         return var0.getAttackCooldownProgressPerTick();
      } else {
         ArrayList var3 = new ArrayList();

         for (EntityAttributeModifier var5 : var2.getModifiers()) {
            if (!var5.id().equals(Item.BASE_ATTACK_SPEED_MODIFIER_ID)) {
               var3.add(var5);
            }
         }

         var1.applyAttributeModifiers(EquipmentSlot.MAINHAND, (var1x, var2x) -> {
            if (var1x.equals(EntityAttributes.ATTACK_SPEED)) {
               var3.add(var2x);
            }
         });
         double var10 = var2.getBaseValue();

         for (EntityAttributeModifier var9 : (Iterable<EntityAttributeModifier>) (var3)) {
            if (var9.operation() == Operation.ADD_VALUE) {
               var10 += var9.value();
            }
         }

         double var6 = var10;

         for (EntityAttributeModifier var13 : (Iterable<EntityAttributeModifier>) (var3)) {
            if (var13.operation() == Operation.ADD_MULTIPLIED_BASE) {
               var6 += var10 * var13.value();
            }
         }

         for (EntityAttributeModifier var14 : (Iterable<EntityAttributeModifier>) (var3)) {
            if (var14.operation() == Operation.ADD_MULTIPLIED_TOTAL) {
               var6 *= 1.0 + var14.value();
            }
         }

         return (float)(1.0 / ((EntityAttribute)var2.getAttribute().value()).clamp(var6) * 20.0);
      }
   }

   private void kAsVoid() {
      LungeController.Type_g var1 = this.EAsMh_Type_g();
      if (var1 == null) {
         this.vAsVoid();
         this.i = "No Lunge spear in hotbar";
      } else {
         if (this.r < 0) {
            this.r = var1.ticks();
            ClientLog.GAsVoid(
               "LungeSwap",
               "Press: {} rule, slot {} (Lunge {}) from slot {} ({}, charge {}); {}",
               this.f,
               var1.slot() + 1,
               var1.lunge(),
               this.F + 1,
               this.y,
               nAsString((double)this.U),
               var1.ticks() == 0 ? (var1.swap() ? "swap and jab now" : "jab now") : "server needs " + var1.ticks() + " more ticks"
            );
         }

         this.Q = var1.slot();
         if (var1.ticks() > 0 && !var1.resets()) {
            this.W++;
            this.i = "Lunge in " + LungeController.kAsString(var1.ticks());
            this.CAsVoid("charge");
            if (this.W > this.r + 10 + 2) {
               this.WAsVoid("the charge never passed");
            }
         } else {
            String var2 = this.mAsString();
            if (var2 != null) {
               this.CAsVoid(var2);
               this.i = "Waiting for " + var2;
               if (++this.j > 10) {
                  this.WAsVoid(var2);
               }
            } else {
               this.i = "Lunging";
               b_.UAsVoid(this, this::EAsVoid, () -> this.mAsString() == null && this.x == LungeSwap.Type_f.WAITING);
            }
         }
      }
   }

   private String mAsString() {
      if (!this.isRuntimeActive() || !this.HAsBoolean()) {
         return "the game";
      } else if (!pg.yAsBoolean(this)) {
         return "the hotbar";
      } else if (!pk.hAsBoolean(mc, mc.player)) {
         return "free hands";
      } else if (this.BAsBoolean() && pk.kAsInt(mc) > 0) {
         return "the miss delay";
      } else if (this.PAsString() != null) {
         return "the Lunge conditions";
      } else {
         return this.Q >= 0 && this.XAsBoolean(this.Q) ? null : "the spear";
      }
   }

   private void CAsVoid(String var1) {
      if (!var1.equals(this.q)) {
         this.q = var1;
         ClientLog.GAsVoid("LungeSwap", "Jab held by {}", var1);
      }
   }

   private void WAsVoid(String var1) {
      ClientLog.GAsVoid("LungeSwap", "Press lapsed after {} ticks, held by {}", this.W + this.j, var1);
      this.uAsVoid(true);
      this.i = "Lunge blocked by " + var1;
   }

   private void EAsVoid() {
      LungeController.Type_g var1 = this.EAsMh_Type_g();
      if (this.x == LungeSwap.Type_f.WAITING && var1 != null && (var1.ticks() <= 0 || var1.resets()) && this.mAsString() == null) {
         ClientPlayerEntity var2 = mc.player;
         this.Q = var1.slot();
         boolean var3 = var1.swap();
         if (var1.resets()) {
            this.E = LungeController.qAsInt(this.KAsMh_Type_c(this.Q), false, this.f, 0, 0.0F, this.YAsBoolean());
            var2.getInventory().setSelectedSlot(this.Q);
            this.x = LungeSwap.Type_f.CHARGING;
            this.S = 0;
            this.i = "Charging spear " + LungeController.kAsString(this.E);
            ClientLog.GAsVoid("LungeSwap", "Selected spear slot {}; the server restarts its charge, so the jab waits {} ticks", this.Q + 1, this.E);
         } else {
            this.Y = var3 && !ItemStack.areItemsEqual(var2.getMainHandStack(), var2.getInventory().getStack(this.Q));
            var2.getInventory().setSelectedSlot(this.Q);
            this.vAsVoid(var3);
         }
      }
   }

   private LungeController.Type_c KAsMh_Type_c(int var1) {
      ClientPlayerEntity var2 = mc.player;
      ItemStack var3 = var2.getInventory().getStack(var1);
      return new LungeController.Type_c(
         var1, JAsFloat(var2, var3), (Float)var3.getOrDefault(DataComponentTypes.MINIMUM_ATTACK_CHARGE, 0.0F), this.tAsInt(var1), 0, false, false
      );
   }

   private void wAsVoid() {
      ClientPlayerEntity var1 = mc.player;
      this.S++;
      if (var1.getInventory().getSelectedSlot() == this.Q && this.XAsBoolean(this.Q)) {
         int var2 = this.E - this.S;
         if (var2 > 0) {
            this.i = "Charging spear " + LungeController.kAsString(var2);
         } else {
            String var3 = this.mAsString();
            if (var3 != null) {
               this.CAsVoid(var3);
               this.i = "Waiting for " + var3;
               if (++this.j > 10) {
                  ClientLog.GAsVoid("LungeSwap", "Charged jab lapsed, held by {}", var3);
                  b_.KAsVoid(this);
                  this.NAsVoid();
                  this.i = "Lunge blocked by " + var3;
               }
            } else {
               b_.UAsVoid(this, () -> {
                  if (this.x == LungeSwap.Type_f.CHARGING && this.mAsString() == null) {
                     this.vAsVoid(true);
                  }
               }, () -> this.x == LungeSwap.Type_f.CHARGING && this.mAsString() == null);
            }
         }
      } else {
         ClientLog.GAsVoid("LungeSwap", "Charge interrupted after {} ticks: slot {} selected", this.S, var1.getInventory().getSelectedSlot() + 1);
         b_.KAsVoid(this);
         this.vAsVoid();
         this.i = "Swap interrupted";
      }
   }

   private void vAsVoid(boolean var1) {
      ClientPlayerEntity var2 = mc.player;
      ItemStack var3 = var2.getInventory().getStack(this.Q);
      int var4 = var1 && this.x == LungeSwap.Type_f.WAITING ? 1 : 0;
      if (var3.isItemEnabled(this.H.getEnabledFeatures())) {
         mc.interactionManager.attackWithPiercingWeapon((PiercingWeaponComponent)var3.get(DataComponentTypes.PIERCING_WEAPON));
         var2.swingHand(Hand.MAIN_HAND);
         this.i = "Lunged";
         this.o = 12;
         this.s = 0.0;
         this.k = var2.getEntityPos();
         this.K = var4 != 0 && this.Y;
      } else {
         this.i = "Spear not enabled here";
      }

      int var5 = this.W + this.j + this.S;
      this.x = LungeSwap.Type_f.SWAPPED;
      this.T = System.nanoTime() + qAsLong(this.YAsBoolean(), ThreadLocalRandom.current().nextDouble());
      ClientLog.GAsVoid(
         "LungeSwap",
         "{}: {} {} rule, slot {} ({}, charge {}) to spear slot {} (Lunge {}) {} ticks after the press{}; food {}, ground {}, sprint {}",
         this.i,
         this.mode.getValue(),
         this.f,
         this.F + 1,
         this.y,
         nAsString((double)this.U),
         this.Q + 1,
         this.tAsInt(this.Q),
         var5,
         var4 != 0 ? ", swapped in the jab tick" : (var1 ? ", spear charged after the swap" : ", spear already held"),
         var2.getHungerManager().getFoodLevel(),
         var2.isOnGround(),
         var2.isSprinting()
      );
   }

   private void NAsVoid() {
      ClientPlayerEntity var1 = getMc() == null ? null : mc.player;
      if (var1 != null && var1 == this.z && var1.getEntityWorld() == this.H) {
         int var2 = AAsInt(
            this.oAsBoolean(),
            this.swapBackToSlot.getValue(),
            this.swapBackSlot.getValue().intValue() - 1,
            this.F,
            var1.getInventory().getSelectedSlot(),
            this.Q
         );
         if (var2 >= 0) {
            var1.getInventory().setSelectedSlot(var2);
         }
      }

      this.vAsVoid();
   }

   private void uAsVoid(boolean var1) {
      b_.KAsVoid(this);
      if (this.x != LungeSwap.Type_f.IDLE) {
         ClientLog.GAsVoid("LungeSwap", "Cancelled at {}; restore={}, status {}", this.x, var1, this.i);
      }

      if (var1 && (this.x == LungeSwap.Type_f.SWAPPED || this.x == LungeSwap.Type_f.CHARGING)) {
         this.NAsVoid();
      } else {
         this.vAsVoid();
      }
   }

   private void vAsVoid() {
      this.x = LungeSwap.Type_f.IDLE;
      this.Y = false;
      this.F = this.Q = -1;
      this.T = 0L;
      this.W = this.j = this.S = this.E = 0;
      this.r = -1;
      this.V = false;
      this.q = "";
   }

   private boolean XAsBoolean(int var1) {
      return this.tAsInt(var1) > 0;
   }

   private int tAsInt(int var1) {
      if (var1 >= 0 && var1 < 9 && mc.player != null && mc.world != null) {
         ItemStack var2 = mc.player.getInventory().getStack(var1);
         if (!var2.contains(DataComponentTypes.PIERCING_WEAPON)) {
            return 0;
         } else {
            Optional<Reference<Enchantment>> var3 = mc.world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOptional(Enchantments.LUNGE);
            return var3.<Integer>map(var1x -> EnchantmentHelper.getLevel(var1x, var2)).orElse(0);
         }
      } else {
         return 0;
      }
   }

   private static String nAsString(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   public static enum Type_f {
      IDLE,
      WAITING,
      CHARGING,
      SWAPPED;

      private Type_f() {
      }
   }
}
