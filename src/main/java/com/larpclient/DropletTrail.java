package com.larpclient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.util.math.Vec3d;

public final class DropletTrail {
   public static final int A = 6;
   private final List<DropletTrail.Type_t> s = new ArrayList<>();

   public DropletTrail() {
   }

   public void NAsVoid(DropletTrail.Type_t var1) {
      if (this.s.size() == 6) {
         this.s.removeFirst();
      }

      this.s.add(var1);
   }

   public void gAsVoid(double var1) {
      this.s.removeIf(var2 -> var1 - var2.born >= var2.duration);
   }

   public void bAsVoid() {
      this.s.clear();
   }

   public List<DropletTrail.Type_t> zAsList() {
      return Collections.unmodifiableList(this.s);
   }

   public static double zAsDouble(DropletTrail.Type_t var0, double var1) {
      double var3 = Math.clamp((var1 - var0.born) / var0.duration, 0.0, 1.0);
      return var0.radius * (1.0 - Math.pow(1.0 - var3, 3.0));
   }

   public static double lAsDouble(DropletTrail.Type_t var0, double var1) {
      double var3 = (var1 - var0.born) / var0.duration;
      return !(var3 < 0.0) && !(var3 >= 1.0) ? FadeCurve.Type_r.HOLD_FADE.fAsDouble(var3) : 0.0;
   }

   public static double AAsDouble(DropletTrail.Type_t var0, double var1, double var3) {
      double var5 = (var3 - var0.born) / var0.duration;
      if (!(var5 < 0.0) && !(var5 >= 1.0) && !(var1 > var0.radius)) {
         double var7 = var0.radius * (1.0 - Math.pow(1.0 - var5, 3.0));
         double var9 = FadeCurve.RAsDouble(1.0 - Math.abs(var1 - var7) / var0.waveWidth);
         return var9 * FadeCurve.Type_r.HOLD_FADE.fAsDouble(var5);
      } else {
         return 0.0;
      }
   }

   public static enum Type_g {
      TERRAIN,
      STAR_OF_DAVID;

      private Type_g() {
      }
   }

   public static record Type_t(
      Vec3d source,
      double born,
      double radius,
      double duration,
      double waveWidth,
      double thickness,
      int from,
      int to,
      double opacity,
      double glow,
      List<LightmapAtlas.Type_w> edges,
      DropletTrail.Type_g style
   ) {
      public Type_t(
         Vec3d var1, double var2, double var4, double var6, double var8, double var10, int var12, int var13, double var14, double var16, List<LightmapAtlas.Type_w> var18
      ) {
         this(var1, var2, var4, var6, var8, var10, var12, var13, var14, var16, var18, DropletTrail.Type_g.TERRAIN);
      }
   }
}
