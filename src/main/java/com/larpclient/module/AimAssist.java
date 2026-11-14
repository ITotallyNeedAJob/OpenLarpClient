package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.LarpClient;
import com.larpclient.ModuleDetector;
import com.larpclient.AngleMath;
import com.larpclient.ParticleEmitter;
import com.larpclient.WaveEffect;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public final class AimAssist extends Module {
   private static AimAssist U;
   private final NumberSetting anubisRange = this.addSetting(
      new NumberSetting("Anubis Range", "Maximum eye-to-hitbox acquisition distance", 4.0, 3.0, 6.0, 0.1) {
         @Override
         public String getDisplayName() {
            return "Range";
         }
      }
   );
   private final NumberSetting anubisFov = this.addSetting(
      new NumberSetting("Anubis FOV", "Total acquisition width around the crosshair", 60.0, 10.0, 180.0, 5.0) {
         @Override
         public String getDisplayName() {
            return "FOV";
         }
      }
   );
   private final NumberSetting anubisSpeed = this.addSetting(
      new NumberSetting("Anubis Speed", "Maximum correction in degrees per second, varied slightly for each target", 60.0, 10.0, 180.0, 5.0) {
         @Override
         public String getDisplayName() {
            return "Speed";
         }
      }
   );
   private final BooleanSetting anubisWeaponsOnly = this.addSetting(
      new BooleanSetting("Anubis Weapons Only", "Require a sword, axe, spear, mace or trident", true) {
         @Override
         public String getDisplayName() {
            return "Weapons Only";
         }
      }
   );
   private final BooleanSetting anubisVertical = this.addSetting(new BooleanSetting("Anubis Vertical", "Also ease pitch toward the target's core", true) {
      @Override
      public String getDisplayName() {
         return "Vertical";
      }
   });
   private final w7.Type_p z = new w7.Type_p();
   private PlayerEntity b;
   private PlayerEntity q;
   private long C;
   private long M;
   private double e = 1.0;
   private ClientPlayerEntity n;
   private Object D;
   private String B = "Waiting for input";

   public AimAssist() {
      super("Aim Assist", "Smooth visible aim correction while you actively aim", Category.COMBAT, -1);
      U = this;

      for (Setting var2 : this.getSettings()) {
         var2.onChanged(var1 -> this.zAsVoid("Settings changed"));
      }

      this.HAsVoid();
   }

   private void HAsVoid() {
      for (Setting var2 : List.of(
         new BooleanSetting("Require Attack", "Retired profile value", true),
         new BooleanSetting("Weapons Only", "Retired profile value", true),
         new BooleanSetting("Players Only", "Retired profile value", true),
         new BooleanSetting("Vertical Assistance", "Retired profile value", false),
         new NumberSetting("Range", "Retired profile value", 3.0, 0.5, 6.0, 0.25),
         new NumberSetting("Aim Cone", "Retired profile value", 60.0, 10.0, 180.0, 5.0),
         new NumberSetting("Correction Speed", "Retired profile value", 60.0, 5.0, 180.0, 5.0)
      )) {
         this.addSetting(var2).visibleWhen(() -> false);
      }
   }

   private String NAsString() {
      MinecraftClient var1 = getMc();
      if (var1.player == null || var1.world == null || var1.interactionManager == null) {
         return "Waiting for world";
      } else if (!var1.player.isAlive() || var1.player.isSpectator() || var1.player.isSleeping()) {
         return "Paused: player unavailable";
      } else if (var1.currentScreen != null || var1.getOverlay() != null) {
         return "Paused: menu open";
      } else if (var1.isWindowFocused() && var1.mouse.isCursorLocked()) {
         if (var1.getCameraEntity() != var1.player || p5.NAsP5_Type_u() != p5.Type_u.NONE || dq.YAsBoolean()) {
            return "Paused: rotation owned";
         } else if (!var1.player.isUsingItem() && !var1.options.useKey.isPressed()) {
            return this.anubisWeaponsOnly.getValue() && !oAsBoolean(var1.player) ? "Waiting for a melee weapon" : null;
         } else {
            return "Paused: using an item";
         }
      } else {
         return "Paused: mouse not captured";
      }
   }

   private static boolean oAsBoolean(PlayerEntity var0) {
      ItemStack var1 = var0.getMainHandStack();
      return var1.isIn(ItemTags.SWORDS) || var1.isIn(ItemTags.AXES) || var1.isIn(ItemTags.SPEARS) || var1.isOf(Items.MACE) || var1.isOf(Items.TRIDENT);
   }

   public static void dAsVoid(PlayerEntity var0, Entity var1) {
      if (U != null && U.isEnabled()) {
         AimAssist var2 = U;
         MinecraftClient var3 = getMc();
         if (var2 != null
            && var0 == var3.player
            && var1 instanceof PlayerEntity var4
            && var4.isAlive()
            && !var4.isRemoved()
            && var4.getEntityWorld() == var3.world) {
            var2.q = var4;
            var2.C = System.nanoTime() / 1000000L;
         }
      }
   }

   public static w7.Type_h OAsW7_Type_h(ClientPlayerEntity var0, double var1, double var3, double var5) {
      if (U != null && U.isEnabled()) {
         AimAssist var7 = U;
         if (var7 != null && var7.isRuntimeActive()) {
            try {
               return var7.tAsW7_Type_h(var0, var1, var3, var5);
            } catch (RuntimeException var9) {
               var7.zAsVoid("Paused: aim correction failed");
               ClientLog.lAsVoid("AimAssist", "Mouse correction failed; disabling and retaining native input", var9);
               var7.disable();
               return w7.Type_h.STILL;
            }
         } else {
            return w7.Type_h.STILL;
         }
      } else {
         return w7.Type_h.STILL;
      }
   }

   private w7.Type_h tAsW7_Type_h(ClientPlayerEntity var1, double var2, double var4, double var6) {
      String var8 = this.NAsString();
      if (var8 == null && var1 == mc.player) {
         this.rAsVoid(var1);
         long var9 = System.nanoTime() / 1000000L;
         int var11 = var2 == 0.0 && var4 == 0.0 && !this.z.qAsBoolean(var9) ? 0 : 1;
         if (var11 == 0) {
            this.z.BAsVoid();
            this.WAsVoid("Waiting for mouse movement");
            return w7.Type_h.STILL;
         } else {
            float[] var12 = w7.kAsFloatArray(var1.getYaw(), var1.getPitch(), var2, var4);
            Vec3d var13 = Vec3d.fromPolar(var12[1], var12[0]);
            this.nAsVoid(this.wAsPlayerEntity(var1, var9, var12[0], var12[1], var13));
            float[] var14 = this.b == null ? null : w7.AAsFloatArray(var1.getEyePos(), var13, w7.YAsBox(this.b.getBoundingBox()), this.anubisRange.getValue());
            double var15 = var14 == null ? 0.0 : l_.iAsDouble((double)(var14[0] - var12[0]));
            double var17 = var14 == null ? 0.0 : (double)(var14[1] - var12[1]);
            double var19 = w7.KAsDouble(
               (Double)mc.options.getMouseSensitivity().getValue(), mc.options.getPerspective().isFirstPerson(), var1.isUsingSpyglass()
            );
            w7.Type_h var21 = this.z
               .iAsW7_Type_h(var2, var4, var15, var17, var14 != null, this.anubisVertical.getValue(), var6, this.anubisSpeed.getValue(), this.e, var19, var9);
            if (this.b == null) {
               this.WAsVoid("No eligible target");
            } else if (var14 == null) {
               this.WAsVoid("On target");
            } else if (this.z.DAsBoolean(var15, var17, this.anubisVertical.getValue())) {
               this.WAsVoid("Following your mouse direction");
            } else {
               this.WAsVoid("Assisting " + this.b.getName().getString());
            }

            return var21;
         }
      } else {
         this.zAsVoid(var8 == null ? "Waiting for input" : var8);
         return w7.Type_h.STILL;
      }
   }

   private void rAsVoid(ClientPlayerEntity var1) {
      if (this.n != var1 || this.D != mc.world) {
         if (this.n != null || this.q != null && this.q.getEntityWorld() != mc.world) {
            this.q = null;
         }

         this.zAsVoid("Waiting for input");
         this.n = var1;
         this.D = mc.world;
      }
   }

   private PlayerEntity wAsPlayerEntity(ClientPlayerEntity var1, long var2, float var4, float var5, Vec3d var6) {
      ArrayList var7 = new ArrayList(2);
      long var8 = var2 - this.C;
      if (var8 >= 0L && var8 <= 1500L) {
         w7.Type_c var10 = this.nAsW7_Type_c(this.q, var4, var5, var6);
         if (var10 != null) {
            var7.add(var10);
         }
      }

      w7.Type_c var16 = this.nAsW7_Type_c(this.b, var4, var5, var6);
      if (var16 != null) {
         var7.add(var16);
      }

      PlayerEntity var11 = w7.aAsObject(var7, this.q, var8, this.b, this.anubisRange.getValue(), this.anubisFov.getValue());
      if (var11 != null) {
         return var11;
      } else {
         ArrayList var12 = new ArrayList();

         for (PlayerEntity var14 : mc.world.getNonSpectatingEntities(PlayerEntity.class, var1.getBoundingBox().expand(this.anubisRange.getValue() + 0.5))) {
            w7.Type_c var15 = this.nAsW7_Type_c(var14, var4, var5, var6);
            if (var15 != null) {
               var12.add(var15);
            }
         }

         return w7.aAsObject(var12, this.q, var2 - this.C, this.b, this.anubisRange.getValue(), this.anubisFov.getValue());
      }
   }

   private void nAsVoid(PlayerEntity var1) {
      if (this.b != var1) {
         this.b = var1;
         this.e = ThreadLocalRandom.current().nextDouble(0.85, 1.15);
         this.z.sAsVoid();
      }
   }

   private w7.Type_c<PlayerEntity> nAsW7_Type_c(PlayerEntity var1, float var2, float var3, Vec3d var4) {
      if (var1 != null
         && var1 != mc.player
         && var1.isAlive()
         && !var1.isRemoved()
         && var1.getEntityWorld() == mc.world
         && !var1.isSpectator()
         && !var1.isCreative()
         && var1.isAttackable()
         && !var1.isInvisible()
         && mc.player.getVehicle() != var1
         && mc.player.shouldDamagePlayer(var1)
         && mc.player.networkHandler.getPlayerListEntry(var1.getUuid()) != null) {
         LarpClient var5 = LarpClient.CAsLarpClient();
         MCFriend var6 = var5 != null && var5.mAsF() != null ? var5.mAsF().EAsModule(MCFriend.class) : null;
         if (var6 != null && var6.SAsBoolean(var1.getName().getString())) {
            return null;
         } else {
            Vec3d var7 = mc.player.getEyePos();
            double var8 = var7.distanceTo(w7.gAsVec3d(var7, var1.getBoundingBox()));
            if (var8 > this.anubisRange.getValue() + 0.5) {
               return null;
            } else {
               Box var10 = w7.YAsBox(var1.getBoundingBox());
               float[] var11 = w7.AAsFloatArray(var7, var4, var10, this.anubisRange.getValue());
               double var12 = w7.oAsDouble(var2, var3, var11);
               if (var12 > this.anubisFov.getValue() / 2.0 * 1.3) {
                  return null;
               } else {
                  Vec3d var14 = w7.JAsVec3d(var7, var4, var10, this.anubisRange.getValue());
                  BlockHitResult var15 = mc.world.raycast(new RaycastContext(var7, var14, ShapeType.COLLIDER, FluidHandling.NONE, mc.player));
                  return var15.getType() != Type.MISS && var15.getPos().squaredDistanceTo(var7) < var14.squaredDistanceTo(var7) - 0.001
                     ? null
                     : new w7.Type_c<>(var1, var8, var12);
               }
            }
         }
      } else {
         return null;
      }
   }

   private void WAsVoid(String var1) {
      if (!this.B.equals(var1)) {
         this.B = var1;
         long var2 = System.nanoTime() / 1000000L;
         if (var2 - this.M >= 1000L) {
            this.M = var2;
            ClientLog.XAsVoid("AimAssist", "{}", var1);
         }
      }
   }

   private void zAsVoid(String var1) {
      this.z.BAsVoid();
      this.b = null;
      this.WAsVoid(var1);
   }

   public static void fAsVoid(ClientPlayerEntity var0) {
      AimAssist var1 = U;
      if (var1 != null && var1.n == var0) {
         var1.zAsVoid("Waiting for input");
      }
   }

   public static void aAsVoid() {
      AimAssist var0 = U;
      if (var0 != null) {
         MinecraftClient var1 = getMc();
         if (var0.q != null
            && (!var0.q.isAlive() || var0.q.isRemoved() || var0.q.getEntityWorld() != var1.world || System.nanoTime() / 1000000L - var0.C > 30000L)) {
            var0.q = null;
         }

         if (!var0.isRuntimeActive()) {
            var0.zAsVoid("Waiting for input");
         } else {
            String var2 = var0.NAsString();
            if (var2 != null || var0.n != var1.player || var0.D != var1.world) {
               var0.zAsVoid(var2 == null ? "Waiting for input" : var2);
            }
         }
      }
   }

   @Override
   public void onTick() {
      aAsVoid();
   }

   @Override
   public void onEnable() {
      this.zAsVoid("Waiting for input");
      ClientLog.XAsVoid(
         "AimAssist",
         "Aim assistance active; range={}, FOV={}, speed={}, weapons={}, vertical={}",
         this.anubisRange.getValue(),
         this.anubisFov.getValue(),
         this.anubisSpeed.getValue(),
         this.anubisWeaponsOnly.getValue(),
         this.anubisVertical.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.zAsVoid("Waiting for input");
      this.n = null;
      this.D = null;
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(k.VANILLA_INPUT);
   }

   @Override
   public String getStatus() {
      return this.B;
   }
}
