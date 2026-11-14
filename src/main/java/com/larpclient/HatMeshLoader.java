package com.larpclient;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import org.joml.Matrix4f;

public final class HatMeshLoader {
   public static final double p = 0.995;
   public static final float F = 0.13F;
   public static final float B = 0.21666665F;
   private static final int[] g = new int[]{0, 1, 3, 2, 0, 1, 3, 2};
   private static final List<HatMeshLoader.Type_a> E = BAsList(0.37F, 0.072F);
   static final int o = 827149390;
   static final int m = 8192;
   static final float r = 0.065F;
   static final float u = 4.0F;
   private static final EnumMap<HatMeshLoader.Type_l, HatMeshLoader.Type_y> A = new EnumMap<>(HatMeshLoader.Type_l.class);
   private static final EnumMap<HatMeshLoader.Type_l, Boolean> k = new EnumMap<>(HatMeshLoader.Type_l.class);

   private static List<HatMeshLoader.Type_a> BAsList(float var0, float var1) {
      ArrayList var2 = new ArrayList();

      for (int var3 = 0; var3 < g.length; var3++) {
         float var4 = (float)Math.toRadians(22.5 + (double)(45 * var3));
         var2.add(new HatMeshLoader.Type_a(g[var3], var0 * (float)Math.sin((double)var4), var1, -var0 * (float)Math.cos((double)var4), var4));
      }

      return List.copyOf(var2);
   }

   public static List<HatMeshLoader.Type_a> aAsList(HatMeshLoader.Type_l var0) {
      return var0 == HatMeshLoader.Type_l.CROWN ? E : List.of();
   }

   public static Matrix4f kAsMatrix4f(Matrix4f var0, HatMeshLoader.Type_a var1, double var2, Matrix4f var4) {
      float var5 = (float)var2;
      return var4.set(var0)
         .translate(var1.x() * var5, -0.5F - var1.rise() * var5, var1.z() * var5)
         .rotateY((float) Math.PI - var1.angle())
         .rotateZ((float) Math.PI)
         .scale(0.21666665F * var5);
   }

   private HatMeshLoader() {
   }

   public static HatMeshLoader.Type_y tAsNu_Type_y(HatMeshLoader.Type_l var0) {
      HatMeshLoader.Type_y var1 = A.get(var0);
      if (var1 == null && !k.containsKey(var0)) {
         try (InputStream var2 = HatMeshLoader.class.getResourceAsStream("/assets/larp-client/models/hats/" + var0.v + ".bin")) {
            if (var2 == null) {
               throw new IOException("missing resource");
            }

            var1 = bAsNu_Type_y(var2.readAllBytes(), var0);
            A.put(var0, var1);
            ClientLog.GAsVoid(
               "ChinaHat", "{} mesh loaded; {} triangles, {} blocks tall", var0.m, var1.triangles(), String.format(Locale.ROOT, "%.3f", var1.rise())
            );
         } catch (RuntimeException | IOException var71) {
            k.put(var0, true);
            ClientLog.lAsVoid("ChinaHat", var0.m + " mesh unavailable; accessory hidden until restart", var71);
         }

         return var1;
      } else {
         return var1;
      }
   }

   public static HatMeshLoader.Type_y bAsNu_Type_y(byte[] var0, HatMeshLoader.Type_l var1) {
      HatMeshLoader.Type_y var2 = zAsNu_Type_y(var0);
      if (var1.w == 1.0F) {
         return var2;
      } else {
         float[] var3 = (float[])var2.points.clone();
         float var4 = Float.MAX_VALUE;
         float var5 = -Float.MAX_VALUE;

         for (int var6 = 1; var6 < var3.length; var6 += 3) {
            var4 = Math.min(var4, var3[var6]);
            var5 = Math.max(var5, var3[var6]);
         }

         float var9 = (var4 + var5) / 2.0F;
         float var7 = 0.0F;

         for (int var8 = 0; var8 < var3.length; var8 += 3) {
            var3[var8] *= var1.w;
            var3[var8 + 2] = var3[var8 + 2] * var1.w;
            var3[var8 + 1] = var1.o + (var3[var8 + 1] - var9) * var1.w;
            var7 = Math.max(var7, var3[var8 + 1]);
         }

         return new HatMeshLoader.Type_y(var3, var2.colors, var2.emissive, var7);
      }
   }

   public static HatMeshLoader.Type_y zAsNu_Type_y(byte[] var0) {
      ByteBuffer var1 = ByteBuffer.wrap(var0).order(ByteOrder.LITTLE_ENDIAN);
      if (var0.length >= 8 && var1.getInt() == 827149390) {
         int var2 = var1.getInt();
         if (var2 > 0 && var2 <= 8192 && (long)var0.length == 8L + (long)var2 * 56L) {
            float[] var3 = new float[var2 * 9];
            int[] var4 = new int[var2];
            boolean[] var5 = new boolean[var2];
            float var6 = 0.0F;

            for (int var7 = 0; var7 < var2; var7++) {
               for (int var8 = 0; var8 < 3; var8++) {
                  float var9 = var1.getFloat();
                  float var10 = var1.getFloat();
                  float var11 = var1.getFloat();
                  if (!Float.isFinite(var9)
                     || !Float.isFinite(var10)
                     || !Float.isFinite(var11)
                     || Math.abs(var9) > 64.0F
                     || Math.abs(var10) > 64.0F
                     || Math.abs(var11) > 64.0F) {
                     throw new IllegalArgumentException("Invalid NLM1 vertex");
                  }

                  float var12 = (var10 + 4.0F) * 0.065F - 0.5F;
                  var3[var7 * 9 + var8 * 3] = var9 * 0.065F;
                  var3[var7 * 9 + var8 * 3 + 1] = var12;
                  var3[var7 * 9 + var8 * 3 + 2] = -var11 * 0.065F;
                  var6 = Math.max(var6, var12);
               }

               float var13 = var1.getFloat();
               float var14 = var1.getFloat();
               float var15 = var1.getFloat();
               int var16 = var1.getInt() & 16777215;
               var5[var7] = var1.getInt() != 0;
               var4[var7] = var5[var7] ? var16 : ModuleOption.wAsInt(0xFF000000 | var16, kAsDouble(var13, var14, var15)) & 16777215;
            }

            return new HatMeshLoader.Type_y(var3, var4, var5, var6);
         } else {
            throw new IllegalArgumentException("Invalid NLM1 triangle count " + var2);
         }
      } else {
         throw new IllegalArgumentException("Not an NLM1 mesh");
      }
   }

   static double kAsDouble(float var0, float var1, float var2) {
      double var3 = Math.sqrt((double)(var0 * var0 + var1 * var1 + var2 * var2));
      if (!(var3 > 1.0E-6)) {
         return 0.8;
      } else {
         double var5 = (double)var0 / var3;
         double var7 = (double)var1 / var3;
         double var9 = (double)var2 / var3;
         return Math.clamp(var5 * var5 * 0.6 + var9 * var9 * 0.8 + var7 * var7 * (var7 > 0.0 ? 1.0 : 0.5), 0.5, 1.0);
      }
   }

   public static double LAsDouble(HatMeshLoader.Type_l var0, double var1) {
      HatMeshLoader.Type_y var3 = tAsNu_Type_y(var0);
      return var3 == null ? 0.0 : (double)var3.rise() * var1;
   }

   public static void gAsVoid(RenderPipeline var0, Matrix4f var1, HatMeshLoader.Type_l var2, double var3, double var5, boolean var7) {
      HatMeshLoader.Type_y var8 = tAsNu_Type_y(var2);
      if (var8 != null) {
         float var9 = (float)var3;
         int var10 = (int)(Math.clamp(var5, 0.0, 1.0) * 255.0) << 24;
         float[] var11 = var8.points;
         var0.blend(EffectRenderParams.Type_g.ALPHA);
         var0.solid(var5 >= 0.995);

         try {
            int var12 = !var7 && !aAsList(var2).isEmpty() ? 1 : 0;
            int var13 = 0;

            for (int var14 = var8.colors.length; var13 < var14; var13++) {
               if (var12 == 0 || !var8.emissive[var13]) {
                  int var15 = var10 | var8.colors[var13];
                  int var16 = var13 * 9;

                  for (int var17 = 0; var17 < 4; var17++) {
                     int var18 = var16 + Math.min(var17, 2) * 3;
                     var0.modelVertex(var1, var11[var18] * var9, -0.5F - var11[var18 + 1] * var9, var11[var18 + 2] * var9, var15);
                  }
               }
            }
         } finally {
            var0.solid(false);
         }
      }
   }

   public static record Type_a(int coin, float x, float rise, float z, float angle) {
   }

   public static enum Type_l {
      CROWN("Crown", "crown", 1.0F, Float.NaN),
      HALO("Halo", "halo", 1.6F, 0.3F);

      public final String m;
      final String v;
      final float w;
      final float o;

      private Type_l(String var3, String var4, float var5, float var6) {
         this.m = var3;
         this.v = var4;
         this.w = var5;
         this.o = var6;
      }
   }

   public static record Type_y(float[] points, int[] colors, boolean[] emissive, float rise) {
      public int triangles() {
         return this.colors.length;
      }
   }
}
