package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.ParticleEmitter;
import java.util.Locale;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

public final class SmoothCamera extends Module {
   private static SmoothCamera w;
   private final ModeSetting mode = this.addSetting(new ModeSetting("Mode", "How the camera follows movement", "Default", "Default", "Smooth"));
   private final BooleanSetting enableFirstPOV = this.addSetting(new BooleanSetting("Enable First POV", "Also smooth camera movement in first person", false));
   private final BooleanSetting resetOnPerspective = this.addSetting(
      new BooleanSetting("Reset On Perspective", "Reset smoothing when switching perspective", true)
   );
   private final NumberSetting d = this.EAsNumberSetting("Horizontal Factor", "Higher values add more horizontal lag", 0.9);
   private final NumberSetting e = this.EAsNumberSetting("Vertical Factor", "Higher values add more vertical lag", 0.93);
   private final NumberSetting smoothHorizontalSpeed = this.addSetting(
      new NumberSetting("Smooth Horizontal Speed", "Horizontal follow speed in blocks per second", 2.2, 0.1, 10.0, 0.1)
   );
   private final NumberSetting smoothVerticalSpeed = this.addSetting(
      new NumberSetting("Smooth Vertical Speed", "Higher values make vertical movement catch up faster", 1.4, 0.1, 10.0, 0.1)
   );
   private final SmoothCamera.Type_z H = new SmoothCamera.Type_z();
   private Object m;
   private Object I;
   private Object l;
   private Vec3d B;
   private long x;

   public SmoothCamera() {
      super("SmoothCamera", "Smooth camera movement as you move", Category.PLAYER, -1);
      w = this;
      this.d.visibleWhen(() -> this.mode.pAsBoolean("Default"));
      this.e.visibleWhen(() -> this.mode.pAsBoolean("Default"));
      this.smoothHorizontalSpeed.visibleWhen(() -> this.mode.pAsBoolean("Smooth"));
      this.smoothVerticalSpeed.visibleWhen(() -> this.mode.pAsBoolean("Smooth"));

      for (Setting var2 : this.getSettings()) {
         var2.onChanged(var1 -> this.wAsVoid());
      }
   }

   public static SmoothCamera XAsSmoothCamera() {
      return w;
   }

   public static boolean oAsBoolean(p5.Type_u var0) {
      return var0 == p5.Type_u.NONE
         || var0 == p5.Type_u.AURA
         || var0 == p5.Type_u.AUTOTRAP
         || var0 == p5.Type_u.AUTOWEB
         || var0 == p5.Type_u.AUTOANCHOR
         || var0 == p5.Type_u.MACE
         || var0 == p5.Type_u.AUTOCRYSTAL
         || var0 == p5.Type_u.PEARLCATCH
         || var0 == p5.Type_u.WINDHOP;
   }

   private NumberSetting EAsNumberSetting(String var1, String var2, double var3) {
      return this.addSetting(new NumberSetting(var1, var2, var3, 0.0, 1.0, 0.01) {
         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, "%.2f", this.getValue());
         }
      });
   }

   public Vec3d JAsVec3d(Camera var1, Vec3d var2) {
      if (!this.isEnabled()) {
         return var2;
      } else {
         MinecraftClient var3 = getMc();
         if (this.isRuntimeActive()
            && var3 != null
            && var3.world != null
            && var3.player != null
            && var3.player.isAlive()
            && var1.getFocusedEntity() != null
            && (!(var1.getFocusedEntity() instanceof LivingEntity var4) || !var4.isSleeping())
            && !mc.gameRenderer.isRenderingPanorama()
            && oAsBoolean(p5.NAsP5_Type_u())
            && (!var3.options.getPerspective().isFirstPerson() || this.enableFirstPOV.getValue())) {
            long var10 = System.nanoTime();
            Perspective var6 = var3.options.getPerspective();
            double var7 = (double)(var10 - this.x) * 1.0E-9;
            int var9 = this.m == var3.world
                  && this.I == var1.getFocusedEntity()
                  && (!this.resetOnPerspective.getValue() || this.l == var6)
                  && this.B != null
                  && !(this.B.squaredDistanceTo(var2) > 256.0)
                  && !(var7 > 0.25)
               ? 0
               : 1;
            this.m = var3.world;
            this.I = var1.getFocusedEntity();
            this.l = var6;
            this.B = var2;
            this.x = var10;
            if (var9 != 0) {
               this.H.MAsVoid();
            }

            return this.H
               .BAsVec3d(
                  var2,
                  var7,
                  this.mode.pAsBoolean("Smooth"),
                  this.d.getValue(),
                  this.e.getValue(),
                  this.smoothHorizontalSpeed.getValue(),
                  this.smoothVerticalSpeed.getValue()
               );
         } else {
            this.wAsVoid();
            return var2;
         }
      }
   }

   private void wAsVoid() {
      this.H.MAsVoid();
      this.m = this.I = this.l = null;
      this.B = null;
      this.x = 0L;
   }

   @Override
   public void onEnable() {
      this.wAsVoid();
      ClientLog.XAsVoid("SmoothCamera", "Position smoothing enabled; mode={}, first POV={}", this.mode.getValue(), this.enableFirstPOV.getValue());
   }

   @Override
   public void onDisable() {
      this.wAsVoid();
   }

   public static final class Type_z {
      public static final double B = 1.0;
      private Vec3d Y;
      private double k;

      public Type_z() {
      }

      public void MAsVoid() {
         this.Y = null;
         this.k = 0.0;
      }

      public Vec3d BAsVec3d(Vec3d var1, double var2, boolean var4, double var5, double var7, double var9, double var11) {
         if (!Double.isFinite(var1.x) || !Double.isFinite(var1.y) || !Double.isFinite(var1.z)) {
            this.MAsVoid();
            return var1;
         } else if (this.Y == null) {
            this.Y = var1;
            return var1;
         } else {
            double var13 = Math.clamp(var2, 0.0, 0.05);
            if (var13 <= 0.0) {
               return this.Y;
            } else if (!var4) {
               double var39 = Math.pow(var5, var13 * 60.0);
               double var40 = Math.pow(var7, var13 * 60.0);
               this.Y = new Vec3d(var1.x + (this.Y.x - var1.x) * var39, var1.y + (this.Y.y - var1.y) * var40, var1.z + (this.Y.z - var1.z) * var39);
               return this.Y;
            } else {
               double var15 = var1.x - this.Y.x;
               double var17 = var1.z - this.Y.z;
               double var19 = Math.hypot(var15, var17);
               double var21 = Math.max(var9 * var13, var19 - 1.0);
               double var23 = !(var19 <= var21) && !(var19 < 1.0E-5) ? var21 / var19 : 1.0;
               double var25 = this.Y.y;
               if (Math.abs(var1.y - var25) > 6.0) {
                  double var27 = var11 * var13 * 4.0;
                  var25 += Math.clamp(var1.y - var25, -var27, var27);
                  this.k = 0.0;
               } else {
                  double var42 = 2.0 / Math.max(0.06, 0.42 / Math.max(0.1, var11));
                  double var29 = var42 * var13;
                  double var31 = 1.0 / (1.0 + var29 + 0.48 * var29 * var29 + 0.235 * var29 * var29 * var29);
                  double var33 = var25 - var1.y;
                  double var35 = (this.k + var42 * var33) * var13;
                  this.k = (this.k - var42 * var35) * var31;
                  double var37 = var1.y + (var33 + var35) * var31;
                  if (var1.y - var25 > 0.0 == var37 > var1.y) {
                     var37 = var1.y;
                     this.k = 0.0;
                  }

                  var25 = var37;
               }

               this.Y = new Vec3d(this.Y.x + var15 * var23, var25, this.Y.z + var17 * var23);
               return this.Y;
            }
         }
      }
   }
}
