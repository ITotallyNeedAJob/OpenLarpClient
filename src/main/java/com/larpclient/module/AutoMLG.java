package com.larpclient.module;

import com.larpclient.BlockPlacementController;
import com.larpclient.ClientLog;
import com.larpclient.b_;
import com.larpclient.dq;
import com.larpclient.p5;
import com.larpclient.pg;
import com.larpclient.mixin.ClientPlayerInteractionManagerAccessor;
import com.larpclient.mixin.MinecraftClientAccessor;
import java.util.Locale;
import java.util.Set;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import net.minecraft.world.attribute.EnvironmentAttributes;

public final class AutoMLG extends Module {
   public static final float t = 89.5F;
   public static final double H = -0.6;
   public static final int w = 3;
   public static final int o = 40;
   private final NumberSetting fallDistance = this.addSetting(
      new NumberSetting("Fall Distance", "Blocks fallen before the water bucket is readied", 8.0, 3.0, 40.0, 1.0)
   );
   private final BooleanSetting pickUp = this.addSetting(new BooleanSetting("Pick Up", "Pick the water back up after landing", true));
   private final pg A = new pg(this);
   private AutoMLG.Type_c d = AutoMLG.Type_c.IDLE;
   private ClientPlayerEntity e;
   private ClientWorld g;
   private float l;
   private int r;
   private String k = "Ready";

   public AutoMLG() {
      super("Auto MLG", "Place water below you before a long fall lands", Category.MOVEMENT, -1);
   }

   public static boolean wAsBoolean(boolean var0, boolean var1, double var2, double var4, double var6) {
      return !var0 && !var1 && var2 >= var6 && var4 < -0.6;
   }

   public static boolean lAsBoolean(boolean var0, boolean var1, int var2) {
      return (var0 || var1) && var2 >= 3;
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
      return this.isEnabled() ? this.k : "";
   }

   @Override
   public void onEnable() {
      this.tAsVoid();
      this.e = mc.player;
      this.g = mc.world;
      this.k = "Ready";
      ClientLog.GAsVoid("AutoMLG", "Enabled; fall distance {} blocks, pick up={}", this.fallDistance.getValue().intValue(), this.pickUp.getValue());
   }

   @Override
   public void onDisable() {
      b_.KAsVoid(this);
      if (this.d != AutoMLG.Type_c.IDLE && this.e != null && this.e == getMc().player) {
         this.A.vAsVoid(mc, true);
         this.e.setPitch(this.l);
      }

      this.tAsVoid();
      this.e = null;
      this.g = null;
   }

   private void tAsVoid() {
      b_.KAsVoid(this);
      this.d = AutoMLG.Type_c.IDLE;
      this.r = 0;
   }

   private boolean FAsBoolean() {
      p5.Type_u var1 = p5.NAsP5_Type_u();
      return !b_.jAsBoolean() || mc.currentScreen != null || var1 != p5.Type_u.NONE;
   }

   @Override
   public void onTick() {
      if (this.e != mc.player || this.g != mc.world) {
         this.A.vAsVoid(getMc(), false);
         this.tAsVoid();
         this.e = mc.player;
         this.g = mc.world;
      }

      if (this.FAsBoolean()) {
         if (this.d != AutoMLG.Type_c.IDLE) {
            this.k = "Paused";
         }
      } else {
         switch (this.d) {
            case IDLE:
               this.QAsVoid();
               break;
            case ARMED:
               this.NAsVoid();
               break;
            case PICKUP:
               this.hAsVoid();
               break;
            case RESTORE:
               this.mAsVoid();
         }
      }
   }

   private void QAsVoid() {
      PlayerAbilities var1 = this.e.getAbilities();
      if (!var1.flying
         && !var1.invulnerable
         && !this.e.isGliding()
         && !this.e.hasVehicle()
         && wAsBoolean(this.e.isOnGround(), this.e.isTouchingWater(), this.e.fallDistance, this.e.getVelocity().y, this.fallDistance.getValue())) {
         if (BlockPlacementController.qAsInt(Items.WATER_BUCKET, true) == -1) {
            this.k = "No water bucket in hotbar or hands";
         } else if ((Boolean)this.g.getEnvironmentAttributes().getAttributeValue(EnvironmentAttributes.WATER_EVAPORATES_GAMEPLAY, this.e.getBlockPos())) {
            this.k = "Water evaporates here";
         } else {
            this.l = this.e.getPitch();
            this.r = 0;
            this.d = AutoMLG.Type_c.ARMED;
            this.k = "Falling";
            this.BAsHand(Items.WATER_BUCKET);
            ClientLog.XAsVoid(
               "AutoMLG",
               "Armed after {} blocks at {} blocks per tick",
               String.format(Locale.ROOT, "%.1f", this.e.fallDistance),
               String.format(Locale.ROOT, "%.2f", this.e.getVelocity().y)
            );
            this.NAsVoid();
         }
      }
   }

   private void NAsVoid() {
      if (!this.e.isOnGround() && !this.e.isTouchingWater() && !(this.e.fallDistance <= 0.0)) {
         this.CAsVoid();
         this.k = "Falling";
         if (this.sAsVec3d(this.e.getYaw(), 89.5F) != null) {
            b_.UAsVoid(this, this::XAsVoid, this::cAsBoolean);
         }
      } else {
         this.d = AutoMLG.Type_c.RESTORE;
         this.mAsVoid();
      }
   }

   private void hAsVoid() {
      this.CAsVoid();
      this.r++;
      if (this.r > 40) {
         this.k = "Pick up timed out";
         this.d = AutoMLG.Type_c.RESTORE;
      } else if (lAsBoolean(this.e.isOnGround(), this.e.isTouchingWater(), this.r)) {
         b_.UAsVoid(this, this::yAsVoid, () -> this.d == AutoMLG.Type_c.PICKUP && this.NAsBoolean(Items.BUCKET));
      }
   }

   private void mAsVoid() {
      int var1 = this.e.getInventory().getSelectedSlot();
      this.A.vAsVoid(mc, true);
      if (mc.interactionManager != null && var1 != this.e.getInventory().getSelectedSlot()) {
         ((ClientPlayerInteractionManagerAccessor)mc.interactionManager).larpSyncCarriedItem();
      }

      dq.RAsVoid(this, dq.Type_s.AUTOMLG, this.e.getYaw(), this.l, 1.0);
      this.tAsVoid();
      this.k = "Ready";
   }

   private void CAsVoid() {
      dq.RAsVoid(this, dq.Type_s.AUTOMLG, this.e.getYaw(), 89.5F, 1.0);
   }

   private Vec3d sAsVec3d(float var1, float var2) {
      Vec3d var3 = this.e.getEyePos();
      Vec3d var4 = var3.add(Vec3d.fromPolar(var2, var1).multiply(this.e.getBlockInteractionRange()));
      BlockHitResult var5 = this.g.raycast(new RaycastContext(var3, var4, ShapeType.OUTLINE, FluidHandling.NONE, this.e));
      return var5.getType() == Type.BLOCK ? var5.getPos() : null;
   }

   private boolean cAsBoolean() {
      return this.d == AutoMLG.Type_c.ARMED
         && !this.e.isOnGround()
         && !this.e.isTouchingWater()
         && Math.abs(this.e.getPitch() - 89.5F) < 0.5F
         && !this.e.isUsingItem()
         && this.NAsBoolean(Items.WATER_BUCKET)
         && this.sAsVec3d(this.e.getYaw(), this.e.getPitch()) != null;
   }

   private boolean NAsBoolean(Item var1) {
      int var2 = BlockPlacementController.qAsInt(var1, true);
      return var2 == -2 || var2 >= 0 && (var2 == this.e.getInventory().getSelectedSlot() || pg.yAsBoolean(this));
   }

   private Hand BAsHand(Item var1) {
      int var2 = BlockPlacementController.qAsInt(var1, true);
      if (var2 == -2) {
         return Hand.OFF_HAND;
      } else if (var2 < 0) {
         return null;
      } else {
         return var2 != this.e.getInventory().getSelectedSlot() && !this.A.uAsBoolean(this.e, var2) ? null : Hand.MAIN_HAND;
      }
   }

   private boolean AAsBoolean(Item var1) {
      Hand var2 = this.BAsHand(var1);
      if (var2 == null) {
         return false;
      } else {
         boolean var3 = mc.interactionManager.interactItem(this.e, var2).isAccepted();
         if (var3) {
            this.e.swingHand(var2);
            ((MinecraftClientAccessor)mc).larpUseDelay(4);
         }

         return var3;
      }
   }

   private void XAsVoid() {
      if (this.cAsBoolean()) {
         if (!this.AAsBoolean(Items.WATER_BUCKET)) {
            this.k = "Water placement rejected";
         } else {
            this.d = this.pickUp.getValue() ? AutoMLG.Type_c.PICKUP : AutoMLG.Type_c.RESTORE;
            this.r = 0;
            this.k = "Water placed";
            ClientLog.XAsVoid("AutoMLG", "Water placed after {} blocks", String.format(Locale.ROOT, "%.1f", this.e.fallDistance));
         }
      }
   }

   private void yAsVoid() {
      boolean var1 = this.AAsBoolean(Items.BUCKET);
      this.k = var1 ? "Water picked up" : "Pick up rejected";
      this.d = AutoMLG.Type_c.RESTORE;
      ClientLog.XAsVoid("AutoMLG", "Pick up {}", var1 ? "requested" : "rejected locally");
   }

   public static enum Type_c {
      IDLE,
      ARMED,
      PICKUP,
      RESTORE;

      private Type_c() {
      }
   }
}
