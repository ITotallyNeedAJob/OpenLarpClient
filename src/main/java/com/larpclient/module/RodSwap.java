package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.RodAttemptState;
import com.larpclient.b_;
import com.larpclient.dq;
import com.larpclient.m3;
import com.larpclient.o5;
import com.larpclient.pg;
import com.larpclient.pk;
import com.larpclient.mixin.ClientPlayerInteractionManagerAccessor;
import com.larpclient.mixin.MinecraftClientAccessor;
import java.util.List;
import java.util.Set;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public final class RodSwap extends Module {
   private static RodSwap f;
   private final ModeSetting mode = this.addSetting(
      new ModeSetting(
         "Mode", "Cast Only returns after contact or a miss; Hook and Reel pulls a confirmed hooked player", "Cast Only", "Cast Only", "Hook and Reel"
      )
   );
   private final KeybindSetting castBind = this.addSetting(
      new KeybindSetting("Cast Bind", "Press once for a rod attempt at the player in your cast path; separate from the module toggle", -1)
   );
   private final BooleanSetting automatic = this.addSetting(
      new BooleanSetting("Automatic", "Cast once per encounter at an approaching player beyond sword reach", false)
   );
   private final BooleanSetting attackHeld = this.addSetting(new BooleanSetting("Attack Held", "Require holding attack for automatic casts", true));
   private final NumberSetting range = this.addSetting(new NumberSetting("Range", "Maximum target distance; aim the rod yourself", 6.0, 3.0, 8.0, 0.25));
   private final NumberSetting cooldown = this.addSetting(
      new NumberSetting("Cooldown", "Minimum milliseconds between cast requests", 750.0, 250.0, 2000.0, 50.0)
   );
   private final NumberSetting timeout = this.addSetting(
      new NumberSetting("Timeout", "Maximum milliseconds before returning to the sword, including delayed server replies", 1000.0, 250.0, 2000.0, 50.0)
   );
   private final pg d = new pg(this);
   private final RodAttemptState H = new RodAttemptState();
   private final RodAttemptState.Type_g P = new RodAttemptState.Type_g();
   private ClientPlayerEntity c;
   private ClientWorld T;
   private PlayerEntity U;
   private FishingBobberEntity a;
   private ItemStack V = ItemStack.EMPTY;
   private int K = -1;
   private int S = -1;
   private long h;
   private String J = "Set a Cast Bind";

   public RodSwap() {
      super("Rod Swap", "Cast a hotbar rod at a player, then return to your sword", Category.COMBAT, -1);
      f = this;
      this.attackHeld.visibleWhen(this.automatic::getValue);
      this.mode.onChanged(var1 -> this.lAsVoid("Mode changed", true));
      this.castBind.onChanged(var1 -> this.lAsVoid("Bind changed", true));
      this.automatic.onChanged(var1 -> {
         this.lAsVoid("Automatic changed", true);
         this.P.oAsVoid();
      });
   }

   public List<Setting<?>> qAsList() {
      return List.of(this.mode, this.castBind, this.automatic, this.attackHeld);
   }

   public List<Setting<?>> jAsList() {
      return List.of(this.range, this.cooldown, this.timeout);
   }

   public static void WAsVoid(int var0) {
      if (f != null && f.isEnabled()) {
         RodSwap var1 = f;
         if (var1 != null && var1.isRuntimeActive() && var0 > 0 && var0 == var1.castBind.getValue() && var1.FAsBoolean() && !var1.H.yAsBoolean()) {
            var1.H.kAsVoid(System.nanoTime());
         }
      }
   }

   public static void bAsVoid() {
      if (f != null && f.isRuntimeActive()) {
         f.lAsVoid("Manual slot selection", false);
         f.H.YAsVoid(System.nanoTime());
      }
   }

   public static boolean DAsBoolean() {
      return f != null && f.isEnabled() ? f != null && f.isRuntimeActive() && f.H.yAsBoolean() && f.d.PAsBoolean() : false;
   }

   public static void xAsVoid() {
      if (DAsBoolean()) {
         f.lAsVoid("Sword input", true);
      }
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(k.VANILLA_INPUT, k.SERVER_STATE_MODIFYING);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.J : "";
   }

   @Override
   public void onEnable() {
      this.c = getMc().player;
      this.T = mc.world;
      this.H.BAsVoid();
      this.P.oAsVoid();
      this.h = System.nanoTime();
      this.J = "Ready";
      ClientLog.GAsVoid("RodSwap", "Enabled; mode={}, automatic={}, manual aim, bounded hotbar return", this.mode.getValue(), this.automatic.getValue());
   }

   @Override
   public void onDisable() {
      this.lAsVoid("Disabled or disconnected", true);
      this.P.oAsVoid();
      this.c = null;
      this.T = null;
   }

   private boolean FAsBoolean() {
      MinecraftClient var1 = getMc();
      return this.c != null
         && this.c == var1.player
         && this.T != null
         && this.T == var1.world
         && pk.eAsBoolean(var1)
         && !this.c.isSpectator()
         && this.c.isAlive();
   }

   private boolean QAsBoolean() {
      for (KeyBinding var4 : mc.options.hotbarKeys) {
         if (pk.fAsBoolean(var4)) {
            return true;
         }
      }

      return false;
   }

   private boolean JAsBoolean() {
      return this.c != null
         && this.K >= 0
         && this.K < 9
         && this.c.getInventory().getStack(this.K).isIn(ItemTags.SWORDS)
         && ItemStack.areItemsAndComponentsEqual(this.V, this.c.getInventory().getStack(this.K));
   }

   private boolean KAsBoolean(int var1) {
      if (this.c != null && this.T != null && var1 >= 0 && var1 < 9) {
         ItemStack var2 = this.c.getInventory().getStack(var1);
         return var2.isOf(Items.FISHING_ROD)
            && !var2.willBreakNextUse()
            && var2.isItemEnabled(this.T.getEnabledFeatures())
            && !this.c.getItemCooldownManager().isCoolingDown(var2);
      } else {
         return false;
      }
   }

   private int jAsInt() {
      for (int var1 = 0; var1 < 9; var1++) {
         if (this.KAsBoolean(var1)) {
            return var1;
         }
      }

      return -1;
   }

   private boolean LAsBoolean(Entity var1) {
      if (var1 instanceof PlayerEntity var2 && pk.FAsBoolean(this.c, var2) && !this.c.isTeammate(var2) && var2.canHit()) {
         return true;
      }

      return false;
   }

   private static Vec3d pAsVec3d(Entity var0) {
      return var0.getEntityPos().subtract(var0.lastX, var0.lastY, var0.lastZ);
   }

   private PlayerEntity dAsPlayerEntity() {
      m3.Type_d var1 = m3.kAsM3_Type_d(this.c.getEyePos(), this.c.getYaw(), this.c.getPitch());
      List var2 = this.T
         .getOtherEntities(
            this.c,
            this.c.getBoundingBox().expand(this.range.getValue() + 4.0),
            var1x -> var1x.isAlive() && !var1x.isSpectator() && var1x.canBeHitByProjectile() && var1x != this.c.getVehicle()
         );

      for (int var3 = 0; var3 < 20; var3++) {
         Vec3d var4 = var1.position();
         Vec3d var5 = var1.collisionEnd();
         BlockHitResult var6 = this.T.raycast(new RaycastContext(var4, var5, ShapeType.COLLIDER, FluidHandling.ANY, this.c));
         double var7 = var6.getType() == Type.MISS ? Double.POSITIVE_INFINITY : var4.squaredDistanceTo(var6.getPos());
         Entity var9 = null;

         for (Entity var11 : (Iterable<Entity>) (var2)) {
            Box var12 = var11.getBoundingBox().offset(m3.EAsVec3d(pAsVec3d(var11), var3)).expand(m3.XAsDouble(var3));
            double var13 = m3.rAsDouble(var4, var5, var12);
            if (var13 < var7) {
               var7 = var13;
               var9 = var11;
            }
         }

         if (var9 != null) {
            return this.LAsBoolean(var9) && o5.lAsDouble(this.c.getEyePos(), var9.getBoundingBox()) <= this.range.getValue() ? (PlayerEntity)var9 : null;
         }

         if (var6.getType() != Type.MISS) {
            return null;
         }

         var1 = var1.next();
      }

      return null;
   }

   private boolean xAsBoolean(long var1) {
      return this.isRuntimeActive()
         && this.FAsBoolean()
         && !this.H.yAsBoolean()
         && this.H.fAsBoolean(var1)
         && var1 - this.h >= 0L
         && this.c.getMainHandStack().isIn(ItemTags.SWORDS)
         && this.c.fishHook == null
         && !this.c.getOffHandStack().isOf(Items.FISHING_ROD)
         && !this.c.isTouchingWater()
         && pk.hAsBoolean(mc, this.c)
         && !pk.BAsBoolean(this.c)
         && !pk.BAsBoolean(mc)
         && !pk.IAsBoolean(mc)
         && !this.QAsBoolean()
         && !dq.YAsBoolean()
         && pg.yAsBoolean(this)
         && !b_.MAsBoolean()
         && ((MinecraftClientAccessor)mc).larpUseDelay() <= 0;
   }

   private boolean KAsBoolean(PlayerEntity var1) {
      return var1 != null
         && this.automatic.getValue()
         && (!this.attackHeld.getValue() || mc.options.attackKey.isPressed())
         && this.P.gAsBoolean(var1.getId())
         && m3.kAsBoolean(o5.lAsDouble(this.c.getEyePos(), var1.getBoundingBox()), this.c.getEntityInteractionRange(), this.range.getValue())
         && m3.EAsBoolean(var1.getEntityPos().subtract(this.c.getEntityPos()), pAsVec3d(var1).subtract(pAsVec3d(this.c)));
   }

   public void CAsVoid() {
      if (this.isEnabled() && this.isRuntimeActive()) {
         if (this.c != getMc().player || this.T != mc.world) {
            this.lAsVoid("World changed", false);
            this.P.oAsVoid();
            this.c = mc.player;
            this.T = mc.world;
         }

         if (!this.FAsBoolean()) {
            this.lAsVoid("Paused", true);
         } else if (this.QAsBoolean()) {
            this.lAsVoid("Manual slot selection", false);
            this.H.YAsVoid(System.nanoTime());
         } else if (this.H.yAsBoolean()) {
            if (!this.d.PAsBoolean()) {
               this.lAsVoid("Slot ownership changed", false);
               this.H.YAsVoid(System.nanoTime());
            } else if (this.JAsBoolean()
               && this.KAsBoolean(this.S)
               && pg.yAsBoolean(this)
               && pk.hAsBoolean(mc, this.c)
               && !pk.IAsBoolean(mc)
               && !pk.BAsBoolean(mc)
               && (!mc.options.attackKey.isPressed() || mc.crosshairTarget == null || mc.crosshairTarget.getType() != Type.BLOCK)
               && (!this.LAsBoolean(this.U) || !(o5.lAsDouble(this.c.getEyePos(), this.U.getBoundingBox()) <= this.c.getEntityInteractionRange() + 0.15))) {
               if (!this.LAsBoolean(this.U)) {
                  this.lAsVoid("Target unavailable", true);
               } else {
                  FishingBobberEntity var1 = this.c.fishHook;
                  if (var1 == null || var1.getPlayerOwner() == this.c && var1.getEntityWorld() == this.T && !var1.isRemoved()) {
                     if (this.a != null && var1 != null && var1 != this.a) {
                        this.lAsVoid("Bobber replaced", true);
                     } else {
                        if (var1 != null && this.a == null) {
                           this.a = var1;
                           ClientLog.XAsVoid("RodSwap", "Owned bobber observed; entity={}", this.a.getId());
                        }

                        Entity var2 = var1 == null ? null : var1.getHookedEntity();
                        int var3 = var1 != null
                              && var1.age > 2
                              && var1.getEntityPos().subtract(this.U.getBoundingBox().getCenter()).dotProduct(var1.getVelocity()) > 0.0
                              && var1.squaredDistanceTo(this.U) > 2.25
                           ? 1
                           : 0;
                        RodAttemptState.Type_a var4 = this.H
                           .bAsAn_Type_a(
                              System.nanoTime(), var1 != null, var2 == this.U, var2 != null && var2 != this.U, var1 != null && var1.isOnGround(), var3 != 0
                           );
                        if (var4 == RodAttemptState.Type_a.RESTORE) {
                           this.lAsVoid(var2 == this.U ? "Contact observed" : "Attempt ended or timed out", true);
                        } else if (var4 == RodAttemptState.Type_a.REEL) {
                           this.J = "Player hooked";
                        }
                     }
                  } else {
                     this.lAsVoid("Bobber unavailable", true);
                  }
               }
            } else {
               this.lAsVoid("Sword or hand action takes priority", true);
            }
         }
      }
   }

   @Override
   public void onTick() {
      if (!this.FAsBoolean()) {
         this.lAsVoid("Paused", true);
      } else if (this.H.yAsBoolean()) {
         if (this.H.QAsAn_Type_o() == RodAttemptState.Type_o.REEL) {
            b_.UAsVoid(this, this::VAsVoid, this::VAsBoolean);
         }
      } else {
         long var1 = System.nanoTime();
         byte var3 = (byte)(this.H.vAsBoolean(var1) ? 1 : 0);
         if (var3 != 0 || this.automatic.getValue() && (!this.attackHeld.getValue() || mc.options.attackKey.isPressed())) {
            if (!this.xAsBoolean(var1)) {
               this.H.vAsBoolean(var1);
               this.J = !this.c.getMainHandStack().isIn(ItemTags.SWORDS)
                  ? "Equip a sword"
                  : (
                     this.c.getOffHandStack().isOf(Items.FISHING_ROD)
                        ? "Move offhand rod to hotbar"
                        : (this.c.fishHook != null ? "Existing bobber" : (var1 - this.h < 0L ? "Cast cooldown" : "Waiting for free hands"))
                  );
            } else {
               PlayerEntity var4 = this.dAsPlayerEntity();
               this.P.YAsVoid(var4 == null ? -1 : var4.getId());
               if (var4 == null) {
                  this.J = "Aim at a player in rod range";
               } else if (var3 == 0 && !this.KAsBoolean(var4)) {
                  this.J = "Ready";
               } else {
                  int var5 = this.jAsInt();
                  if (var5 < 0) {
                     this.J = "No usable rod in hotbar";
                  } else {
                     b_.UAsVoid(
                        this,
                        () -> this.cAsVoid(var4, var5),
                        () -> this.xAsBoolean(System.nanoTime())
                              && this.KAsBoolean(var5)
                              && this.LAsBoolean(var4)
                              && (var3 != 0 ? this.H.vAsBoolean(System.nanoTime()) : this.KAsBoolean(var4))
                              && this.dAsPlayerEntity() == var4
                     );
                  }
               }
            }
         } else {
            this.J = this.automatic.getValue() ? "Hold attack to auto cast" : "Ready";
         }
      }
   }

   private void cAsVoid(PlayerEntity var1, int var2) {
      this.K = this.c.getInventory().getSelectedSlot();
      this.V = this.c.getMainHandStack().copy();
      this.S = var2;
      if (!this.d.uAsBoolean(this.c, var2)) {
         this.lAsVoid("Hotbar busy", false);
      } else {
         this.U = var1;
         this.a = null;
         this.H.lAsVoid(System.nanoTime(), this.timeout.getValue().longValue() * 1000000L, this.mode.pAsBoolean("Hook and Reel"));
         this.P.tAsVoid(var1.getId());
         this.h = System.nanoTime() + this.cooldown.getValue().longValue() * 1000000L;
         boolean var3 = mc.interactionManager.interactItem(this.c, Hand.MAIN_HAND).isAccepted();
         if (!var3) {
            this.lAsVoid("Cast rejected locally", true);
         } else {
            this.c.swingHand(Hand.MAIN_HAND);
            ((MinecraftClientAccessor)mc).larpUseDelay(4);
            this.J = "Cast requested";
            ClientLog.XAsVoid(
               "RodSwap", "Cast requested; mode={}, rodSlot={}, swordSlot={}, target={}", this.mode.getValue(), var2 + 1, this.K + 1, this.U.getId()
            );
         }
      }
   }

   private boolean VAsBoolean() {
      return this.isRuntimeActive()
         && this.FAsBoolean()
         && this.H.QAsAn_Type_o() == RodAttemptState.Type_o.REEL
         && this.d.PAsBoolean()
         && this.JAsBoolean()
         && this.KAsBoolean(this.S)
         && pg.yAsBoolean(this)
         && pk.hAsBoolean(mc, this.c)
         && !pk.IAsBoolean(mc)
         && !pk.BAsBoolean(mc)
         && !pk.BAsBoolean(this.c)
         && ((MinecraftClientAccessor)mc).larpUseDelay() <= 0
         && this.a != null
         && this.c.fishHook == this.a
         && this.a.getHookedEntity() == this.U
         && this.LAsBoolean(this.U);
   }

   private void VAsVoid() {
      if (this.VAsBoolean()) {
         this.H.aAsVoid();
         if (!mc.interactionManager.interactItem(this.c, Hand.MAIN_HAND).isAccepted()) {
            this.lAsVoid("Reel rejected locally", true);
         } else {
            this.c.swingHand(Hand.MAIN_HAND);
            ((MinecraftClientAccessor)mc).larpUseDelay(4);
            this.J = "Reel requested";
            ClientLog.XAsVoid("RodSwap", "Reel requested for confirmed target {}; awaiting bobber removal", this.U.getId());
         }
      }
   }

   private void lAsVoid(String var1, boolean var2) {
      b_.KAsVoid(this);
      int var3 = !this.H.yAsBoolean() && !this.d.FAsBoolean() ? 0 : 1;
      RodAttemptState.Type_o var4 = this.H.QAsAn_Type_o();
      int var5 = this.c != null && this.c == getMc().player && this.T == mc.world && this.c.isAlive() ? 1 : 0;
      int var6 = var5 != 0 ? this.c.getInventory().getSelectedSlot() : -1;
      this.d.vAsVoid(getMc(), var2 && var5 != 0 && this.JAsBoolean());
      if (var5 != 0 && mc.interactionManager != null && var6 != this.c.getInventory().getSelectedSlot()) {
         ((ClientPlayerInteractionManagerAccessor)mc.interactionManager).larpSyncCarriedItem();
      }

      this.H.BAsVoid();
      this.U = null;
      this.a = null;
      this.V = ItemStack.EMPTY;
      this.K = -1;
      this.S = -1;
      this.J = var1;
      if (var3 != 0) {
         ClientLog.XAsVoid(
            "RodSwap",
            "{} at {}; return requested={}, original slot restored={}",
            var1,
            var4,
            var2,
            var5 != 0 && var6 != this.c.getInventory().getSelectedSlot()
         );
      }
   }
}
