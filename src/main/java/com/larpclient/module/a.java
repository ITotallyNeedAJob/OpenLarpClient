package com.larpclient.module;

import java.util.SplittableRandom;
import net.minecraft.util.math.MathHelper;

public final class a {
   public static final a.Type_l A = new a.Type_l(
      5, 0.68F, 1.1F, 2, 5, 0.09F, 0.56F, 0.04F, 18.0F, 0.34, 0.78F, 0.12F, 0.48F, 0.19F, 0.23F, 0.1, 0.07F, 0.042F, 0.32F, 0.062F
   );
   public static final a.Type_l m = new a.Type_l(
      9, 0.74F, 1.14F, 1, 4, 0.125F, 0.66F, 0.048F, 24.0F, 0.28, 0.72F, 0.15F, 0.54F, 0.24F, 0.27F, 0.28, 0.055F, 0.055F, 0.35F, 0.078F
   );
   private final SplittableRandom E = new SplittableRandom();
   private final a.Type_l P;
   private final a.Type_e C;
   private final a.Type_e c;
   private float b;
   private boolean j;
   private float k;
   private float g;

   public a(a.Type_l var1) {
      this.P = var1;
      this.C = new a.Type_e(var1.noiseSources());
      this.c = new a.Type_e(var1.noiseSources());
      this.VAsVoid();
   }

   public void VAsVoid() {
      this.nAsVoid(this.C, 1.0F);
      this.nAsVoid(this.c, 0.86F);
      this.b = this.cAsFloat(-this.P.wristLimit(), this.P.wristLimit());
      this.j = false;
      this.k = 0.0F;
      this.g = 1.0F;
   }

   public float OAsFloat(a.Type_b var1) {
      a.Type_e var2 = this.tAsA_Type_e(var1);
      this.IAsVoid(var2);
      if (this.E.nextDouble() < this.P.additionalNoiseRefreshChance()) {
         this.IAsVoid(var2);
      }

      float var3 = 0.0F;

      for (float var7 : var2.U) {
         var3 += var7;
      }

      return MathHelper.clamp(var3 / (float)var2.U.length, -1.0F, 1.0F);
   }

   public float pAsFloat(a.Type_b var1, boolean var2) {
      a.Type_e var3 = this.tAsA_Type_e(var1);
      if (var3.M <= 0) {
         float var4 = var1 == a.Type_b.PITCH ? 0.86F : 1.0F;
         var3.Y = 1.0F + (this.cAsFloat(this.P.minimumGain(), this.P.maximumGain()) - 1.0F) * var4;
         var3.M = this.E.nextInt(this.P.minimumGainDuration(), this.P.maximumGainDuration() + 1);
      }

      var3.M--;
      float var6 = this.P.gainAdjustmentPerTick();
      var3.o = var3.o + MathHelper.clamp(var3.Y - var3.o, -var6, var6);
      if (!var2) {
         return var3.o;
      } else {
         float var5 = (var3.o - 1.0F) * this.P.precisionGainCompression();
         return 1.0F + MathHelper.clamp(var5, -this.P.maximumPrecisionGainDeviation(), this.P.maximumPrecisionGainDeviation());
      }
   }

   public float AAsFloat(float var1, boolean var2) {
      if (!var2 && Float.isFinite(var1)) {
         if (var1 > this.P.subMovementThreshold() && !this.j && this.E.nextDouble() < this.P.subMovementProbability()) {
            this.j = true;
            this.k = 0.0F;
            this.g = this.P.ballisticMinimum() + (float)this.E.nextDouble() * this.P.ballisticRange();
         }

         if (!this.j) {
            return 1.0F;
         } else {
            this.k = this.k + this.P.subMovementProgressPerTick();
            if (this.k < 1.0F) {
               return this.g;
            } else if (this.k < 1.75F) {
               return this.P.correctionMinimum() + (float)this.E.nextDouble() * this.P.correctionRange();
            } else {
               this.KAsVoid();
               return 1.0F;
            }
         }
      } else {
         this.KAsVoid();
         return 1.0F;
      }
   }

   public float dAsFloat(float var1) {
      return var1 * this.P.wristPitchCoupling() * MathHelper.sin((double)this.b);
   }

   public void qAsVoid() {
      if (this.E.nextDouble() < (double)this.P.wristUpdateChance()) {
         this.b = MathHelper.clamp(this.b + this.cAsFloat(-this.P.wristAngleChange(), this.P.wristAngleChange()), -this.P.wristLimit(), this.P.wristLimit());
      }
   }

   private void nAsVoid(a.Type_e var1, float var2) {
      for (int var3 = 0; var3 < var1.U.length; var3++) {
         var1.U[var3] = this.ZAsFloat();
      }

      var1.o = 1.0F;
      var1.Y = 1.0F + (this.cAsFloat(this.P.minimumGain(), this.P.maximumGain()) - 1.0F) * var2;
      var1.M = this.E.nextInt(this.P.minimumGainDuration(), this.P.maximumGainDuration() + 1);
   }

   private void IAsVoid(a.Type_e var1) {
      var1.U[this.E.nextInt(var1.U.length)] = this.ZAsFloat();
   }

   private a.Type_e tAsA_Type_e(a.Type_b var1) {
      return var1 == a.Type_b.YAW ? this.C : this.c;
   }

   private void KAsVoid() {
      this.j = false;
      this.k = 0.0F;
      this.g = 1.0F;
   }

   private float ZAsFloat() {
      return (float)(this.E.nextDouble() * 2.0 - 1.0);
   }

   private float cAsFloat(float var1, float var2) {
      return var1 + (float)this.E.nextDouble() * (var2 - var1);
   }

   public static enum Type_b {
      YAW,
      PITCH;

      private Type_b() {
      }
   }

   private static final class Type_e {
      final float[] U;
      float o;
      float Y;
      int M;

      Type_e(int var1) {
         this.U = new float[var1];
      }
   }

   public static record Type_l(
      int noiseSources,
      float minimumGain,
      float maximumGain,
      int minimumGainDuration,
      int maximumGainDuration,
      float gainAdjustmentPerTick,
      float precisionGainCompression,
      float maximumPrecisionGainDeviation,
      float subMovementThreshold,
      double subMovementProbability,
      float ballisticMinimum,
      float ballisticRange,
      float correctionMinimum,
      float correctionRange,
      float subMovementProgressPerTick,
      double additionalNoiseRefreshChance,
      float wristUpdateChance,
      float wristAngleChange,
      float wristLimit,
      float wristPitchCoupling
   ) {
   }
}
