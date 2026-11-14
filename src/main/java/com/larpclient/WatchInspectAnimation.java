package com.larpclient;

public final class WatchInspectAnimation {
   public static final double CropHelper = 0.14;
   private static final WatchInspectAnimation.Type_g SahurDanceEngine = new WatchInspectAnimation.Type_g(0.0F, 0.0F);
   private float p;
   private float Y;
   private float k;
   private double B = Double.NaN;

   public WatchInspectAnimation() {
   }

   public void KAsVoid() {
      this.p = this.Y = this.k = 0.0F;
      this.B = Double.NaN;
   }

   public WatchInspectAnimation.Type_g sAsQt_Type_g(bj.Type_i var1, double var2) {
      if (var1.J == 2) {
         this.B = Double.NaN;
         this.p = var1.L;
         this.Y = m_.eAsFloat(this.p);
         return new WatchInspectAnimation.Type_g(this.p, this.Y);
      } else {
         if (var1.q && this.Y > 0.0F) {
            this.B = var2;
            this.k = this.Y;
            ClientLog.XAsVoid("SwordModels", "Watch inspect interrupted at phase {}, releasing over {}s", String.format("%.2f", this.p), 0.14);
         }

         this.Y = 0.0F;
         if (Double.isFinite(this.B) && Double.isFinite(var2)) {
            float var4 = (float)Math.clamp((var2 - this.B) / 0.14, 0.0, 1.0);
            if (!(var4 >= 1.0F) && !(var2 < this.B)) {
               float var5 = 1.0F - var4 * var4 * (3.0F - 2.0F * var4);
               return new WatchInspectAnimation.Type_g(this.p, this.k * var5);
            } else {
               this.B = Double.NaN;
               return d;
            }
         } else {
            return d;
         }
      }
   }

   public static record Type_g(float phase, float weight) {
   }
}
