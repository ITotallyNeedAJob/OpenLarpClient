package com.larpclient;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public final class VolumetricWaterEngine {
   public static final int h = 4096;
   private static final int z = 16384;
   private static final int U = 48;
   private static final int SkyChunkCache = 8192;
   private static final double PetEffectRenderer = 0.1;
   private static final double M = 0.064;
   private static final double Y = 0.18;
   private static final double[] LineSegment = new double[4096];
   private static final double[] O = new double[4096];
   private static final double[] v = new double[4096];
   private static final double[] GroundSample = new double[4096];
   private static final double[] K = new double[4096];
   private static final double[] F = new double[4096];
   private static final double[] BloomUtility = new double[4096];
   private static final double[] AuctionNotifier = new double[4096];
   private static final double[] P = new double[4096];
   private static final double[] w = new double[4096];
   private static final double[] SmoothInput = new double[4096];
   private static final double[] q = new double[4096];
   private static final double[] Q = new double[4096];
   private static final double[] ScreenScale = new double[4096];
   private static final double[] J = new double[4096];
   private static final int[] AuraModule = new int[16384];
   private static final int[] SahurDanceEngine = new int[4096];
   private static final int[] L = new int[4096];
   private static final int[] B = new int[4096];
   private static final int[] H = new int[4096];
   private static final Set<Long> X = ConcurrentHashMap.newKeySet();
   private static final Map<Long, List<Box>> ChinaHatCodec = new HashMap<>();
   private static final Map<Long, Long> S = new HashMap<>();
   private static ClientWorld Z;
   private static int I;
   private static long n;
   private static boolean f;

   private static boolean jAsBoolean() {
      RealisticWater var0 = RealisticWater.tAsRealisticWater();
      return var0 != null && var0.isRuntimeActive() && var0.volumetric.getValue() && !WetReflectionEffect.NAsBoolean();
   }

   public static int jAsInt() {
      return I;
   }

   public static boolean LAsBoolean(BlockPos var0) {
      return jAsBoolean() && X.contains(var0.asLong());
   }

   public static double fAsDouble() {
      return 0.064;
   }

   public static double HAsDouble(int var0, float var1) {
      return k[var0] + (e[var0] - k[var0]) * (double)var1;
   }

   public static double RAsDouble(int var0, float var1) {
      return K[var0] + (O[var0] - K[var0]) * (double)var1;
   }

   public static double fAsDouble(int var0, float var1) {
      return F[var0] + (v[var0] - F[var0]) * (double)var1;
   }

   public static void jAsVoid(BlockPos var0, BlockState var1, BlockState var2) {
      if (jAsBoolean()) {
         MinecraftClient var3 = MinecraftClient.getInstance();
         if (var3 != null
            && var3.world != null
            && var3.player != null
            && !(var3.player.squaredDistanceTo((double)var0.getX() + 0.5, (double)var0.getY() + 0.5, (double)var0.getZ() + 0.5) > 49.0)) {
            kAsVoid(var3.world);
            boolean var4 = var1.getFluidState().isIn(FluidTags.WATER);
            boolean var5 = var2.getFluidState().isIn(FluidTags.WATER);
            if (!var1.isAir() && !var4 && var2.isAir()) {
               if (S.size() < 128) {
                  S.put(var0.asLong(), n + 20L);
               }

               for (Direction var9 : Direction.values()) {
                  if (var3.world.getFluidState(var0.offset(var9)).isIn(FluidTags.WATER)) {
                     SAsVoid(var0);
                     break;
                  }
               }
            }

            if (var5 && (!var4 || var2.getFluidState().isStill() && !var1.getFluidState().isStill())) {
               byte var11 = 0;

               for (Direction var10 : Direction.values()) {
                  if (X.contains(var0.offset(var10).asLong())) {
                     var11 = 1;
                     break;
                  }
               }

               int var13;
               byte var10000;
               label132: {
                  var13 = !var3.player.getMainHandStack().isOf(Items.WATER_BUCKET)
                        && !var3.player.getOffHandStack().isOf(Items.WATER_BUCKET)
                        && !var3.player.getMainHandStack().isOf(Items.BUCKET)
                        && !var3.player.getOffHandStack().isOf(Items.BUCKET)
                     ? 0
                     : 1;
                  if (var3.crosshairTarget instanceof BlockHitResult var17
                     && (var0.equals(var17.getBlockPos()) || var0.equals(var17.getBlockPos().offset(var17.getSide())))) {
                     var10000 = 1;
                     break label132;
                  }

                  var10000 = 0;
               }

               int var15 = var10000;
               int var18 = var2.getFluidState().isStill() && var3.options.useKey.isPressed() && var13 != 0 && var15 != 0 ? 1 : 0;
               if (var18 != 0) {
                  iAsVoid(var0, true);
               } else if (S.containsKey(var0.asLong())) {
                  SAsVoid(var0);
               } else if (var11 != 0) {
                  PAsVoid(var0);
               }
            }

            if (!var5) {
               X.remove(var0.asLong());
            }
         }
      }
   }

   private static void PAsVoid(BlockPos var0) {
      if (X.size() < 4096 && X.add(var0.asLong())) {
         MinecraftClient var1 = MinecraftClient.getInstance();
         if (var1 != null && var1.world != null) {
            var1.worldRenderer.scheduleChunkRenders3x3x3(var0.getX() >> 4, var0.getY() >> 4, var0.getZ() >> 4);
         }
      }
   }

   private static void SAsVoid(BlockPos var0) {
      iAsVoid(var0, false);
   }

   private static void iAsVoid(BlockPos var0, boolean var1) {
      if (var1 || !X.contains(var0.asLong())) {
         if (I + 1000 > 4096) {
            if (!f) {
               ClientLog.GAsVoid("TerrainMaterials", "Volumetric water capacity reached ({} droplets); further pours retain native water", 4096);
               MenuPopupProvider = true;
            }
         } else {
            for (int var2 = 0; var2 < 10; var2++) {
               for (int var3 = 0; var3 < 10; var3++) {
                  for (int var4 = 0; var4 < 10; var4++) {
                     int var5 = I++;
                     double var6 = (double)((var4 * 7 + var2 * 13 + var3 * 17) % 11 - 5) * 5.0E-4;
                     e[var5] = k[var5] = (double)var0.getX() + ((double)var4 + 0.5) * 0.1 + var6;
                     O[var5] = K[var5] = (double)var0.getY() + ((double)var2 + 0.5) * 0.1 - var6;
                     v[var5] = F[var5] = (double)var0.getZ() + ((double)var3 + 0.5) * 0.1;
                     b[var5] = o[var5] = P[var5] = 0.0;
                  }
               }
            }

            PAsVoid(var0);
            ClientLog.XAsVoid("TerrainMaterials", "Volumetric pour accepted at {}; droplets={}", var0, I);
         }
      }
   }

   public static void WAsVoid(MinecraftClient var0) {
      if (!jAsBoolean()) {
         if (I > 0 || !X.isEmpty()) {
            YAsVoid();
         }
      } else if (var0.world != null && var0.player != null && !var0.isPaused()) {
         kAsVoid(var0.world);
         n++;
         S.entrySet().removeIf(var0x -> var0x.getValue() < n);
         if (I != 0) {
            System.arraycopy(e, 0, k, 0, I);
            System.arraycopy(O, 0, K, 0, I);
            System.arraycopy(v, 0, F, 0, I);
            ChinaHatCodec.clear();

            for (int var1 = 0; var1 < 2; var1++) {
               QAsVoid(var0, 0.025);
            }
         }
      }
   }

   private static void kAsVoid(ClientWorld var0) {
      if (Z != var0) {
         YAsVoid();
         Z = var0;
      }
   }

   private static void QAsVoid(MinecraftClient var0, double var1) {
      for (int var3 = 0; var3 < I; var3++) {
         w[var3] = e[var3];
         t[var3] = O[var3];
         q[var3] = v[var3];
         if (!Z.isChunkLoaded(BlockPos.ofFloored(e[var3], O[var3], v[var3]))) {
            b[var3] = o[var3] = P[var3] = 0.0;
         } else {
            o[var3] = Math.max(-6.0, o[var3] - 9.8 * var1);
            double var4 = e[var3];
            double var6 = O[var3];
            double var8 = v[var3];
            e[var3] = e[var3] + b[var3] * var1;
            O[var3] = O[var3] + o[var3] * var1;
            v[var3] = v[var3] + P[var3] * var1;
            HAsVoid(var3, var4, var6, var8);
         }
      }

      for (int var27 = 0; var27 < 2; var27++) {
         DAsVoid();
         Arrays.fill(Q, 0, I, 0.0);
         Arrays.fill(y, 0, I, 0.0);
         Arrays.fill(J, 0, I, 0.0);

         label106:
         for (int var29 = 0; var29 < I; var29++) {
            int var5 = 0;

            for (int var34 = -1; var34 <= 1; var34++) {
               for (int var7 = -1; var7 <= 1; var7++) {
                  for (int var36 = -1; var36 <= 1; var36++) {
                     int var9 = L[var29] + var36;
                     int var10 = B[var29] + var7;
                     int var11 = H[var29] + var34;

                     for (int var12 = x[SAsInt(var9, var10, var11)]; var12 >= 0; var12 = d[var12]) {
                        if (var12 != var29 && L[var12] == var9 && B[var12] == var10 && H[var12] == var11) {
                           double var13 = e[var29] - e[var12];
                           double var15 = O[var29] - O[var12];
                           double var17 = v[var29] - v[var12];
                           double var19 = var13 * var13 + var15 * var15 + var17 * var17;
                           if (!(var19 <= 1.0E-10) && !(var19 > 0.0289)) {
                              if (++var5 > 48) {
                                 continue label106;
                              }

                              double var21 = Math.sqrt(var19);
                              double var23 = var21 < 0.1 ? (0.1 - var21) * 0.22 : -(var21 - 0.1) * 0.014 * (1.0 - var21 / 0.17);
                              double var25 = var23 / var21;
                              Q[var29] = Q[var29] + var13 * var25;
                              y[var29] = y[var29] + var15 * var25;
                              J[var29] = J[var29] + var17 * var25;
                           }
                        }
                     }
                  }
               }
            }
         }

         for (int var30 = 0; var30 < I; var30++) {
            double var32 = e[var30];
            double var35 = O[var30];
            double var37 = v[var30];
            e[var30] = e[var30] + Math.clamp(Q[var30], -0.015, 0.015);
            O[var30] = O[var30] + Math.clamp(y[var30], -0.015, 0.015);
            v[var30] = v[var30] + Math.clamp(J[var30], -0.015, 0.015);
            HAsVoid(var30, var32, var35, var37);
         }
      }

      Box var28 = var0.player.getBoundingBox().expand(0.064);
      Vec3d var31 = var0.player.getVelocity();

      for (int var33 = 0; var33 < I; var33++) {
         b[var33] = (e[var33] - w[var33]) / var1;
         o[var33] = (O[var33] - t[var33]) / var1;
         P[var33] = (v[var33] - q[var33]) / var1;
         if (var28.contains(e[var33], O[var33], v[var33])) {
            b[var33] = b[var33] + var31.x * 3.0;
            P[var33] = P[var33] + var31.z * 3.0;
            o[var33] = o[var33] + Math.max(0.0, var31.y) * 2.0;
         }

         b[var33] = Math.clamp(b[var33] * 0.99, -6.0, 6.0);
         o[var33] = Math.clamp(o[var33] * 0.99, -6.0, 6.0);
         P[var33] = Math.clamp(P[var33] * 0.99, -6.0, 6.0);
         if (!Double.isFinite(e[var33] + O[var33] + v[var33]) || O[var33] < (double)(Z.getBottomY() - 8)) {
            fAsVoid(var33--);
         }
      }
   }

   private static void DAsVoid() {
      Arrays.fill(x, -1);
      int var0 = 0;

      while (var0 < I) {
         L[var0] = (int)Math.floor(e[var0] / 0.18);
         B[var0] = (int)Math.floor(O[var0] / 0.18);
         H[var0] = (int)Math.floor(v[var0] / 0.18);
         int var1 = SAsInt(L[var0], B[var0], H[var0]);
         d[var0] = x[var1];
         x[var1] = var0++;
      }
   }

   private static int SAsInt(int var0, int var1, int var2) {
      return (var0 * 73856093 ^ var1 * 19349663 ^ var2 * 83492791) & 16383;
   }

   private static void HAsVoid(int var0, double var1, double var3, double var5) {
      int var7 = (int)Math.floor(e[var0]);
      int var8 = (int)Math.floor(O[var0]);
      int var9 = (int)Math.floor(v[var0]);

      for (int var10 = -1; var10 <= 1; var10++) {
         for (int var11 = -1; var11 <= 1; var11++) {
            for (int var12 = -1; var12 <= 1; var12++) {
               BlockPos var13 = new BlockPos(var7 + var12, var8 + var11, var9 + var10);
               List var14 = ChinaHatCodec.get(var13.asLong());
               if (var14 == null) {
                  if (ChinaHatCodec.size() >= 8192 || !Z.isChunkLoaded(var13)) {
                     e[var0] = var1;
                     O[var0] = var3;
                     v[var0] = var5;
                     b[var0] = o[var0] = P[var0] = 0.0;
                     return;
                  }

                  var14 = Z.getBlockState(var13).getCollisionShape(Z, var13).getBoundingBoxes();
                  ChinaHatCodec.put(var13.asLong(), var14);
               }

               for (Box var16 : (Iterable<Box>) (var14)) {
                  double var19 = var16.minX + (double)var13.getX() - 0.0352;
                  double var21 = var16.maxX + (double)var13.getX() + 0.0352;
                  double var23 = var16.minY + (double)var13.getY() - 0.0352;
                  double var25 = var16.maxY + (double)var13.getY() + 0.0352;
                  double var27 = var16.minZ + (double)var13.getZ() - 0.0352;
                  double var29 = var16.maxZ + (double)var13.getZ() + 0.0352;
                  if (!(e[var0] <= var19) && !(e[var0] >= var21) && !(O[var0] <= var23) && !(O[var0] >= var25) && !(v[var0] <= var27) && !(v[var0] >= var29)) {
                     byte var31 = 0;
                     double var32 = e[var0] - var19;
                     double var34 = var21 - e[var0];
                     if (var34 < var32) {
                        var32 = var34;
                        var31 = 1;
                     }

                     var34 = O[var0] - var23;
                     if (var34 < var32) {
                        var32 = var34;
                        var31 = 2;
                     }

                     var34 = var25 - O[var0];
                     if (var34 < var32) {
                        var32 = var34;
                        var31 = 3;
                     }

                     var34 = v[var0] - var27;
                     if (var34 < var32) {
                        var32 = var34;
                        var31 = 4;
                     }

                     var34 = var29 - v[var0];
                     if (var34 < var32) {
                        var31 = 5;
                     }

                     switch (var31) {
                        case 0:
                           e[var0] = var19 - 1.0E-4;
                           b[var0] = Math.min(0.0, b[var0]) * 0.15;
                           break;
                        case 1:
                           e[var0] = var21 + 1.0E-4;
                           b[var0] = Math.max(0.0, b[var0]) * 0.15;
                           break;
                        case 2:
                           O[var0] = var23 - 1.0E-4;
                           o[var0] = Math.min(0.0, o[var0]) * 0.08;
                           break;
                        case 3:
                           O[var0] = var25 + 1.0E-4;
                           o[var0] = Math.max(0.0, o[var0]) * 0.08;
                           b[var0] = b[var0] * 0.97;
                           P[var0] = P[var0] * 0.97;
                           break;
                        case 4:
                           v[var0] = var27 - 1.0E-4;
                           P[var0] = Math.min(0.0, P[var0]) * 0.15;
                           break;
                        case 5:
                           v[var0] = var29 + 1.0E-4;
                           P[var0] = Math.max(0.0, P[var0]) * 0.15;
                     }
                  }
               }
            }
         }
      }
   }

   private static void fAsVoid(int var0) {
      int var1 = --I;
      e[var0] = e[var1];
      O[var0] = O[var1];
      v[var0] = v[var1];
      k[var0] = k[var1];
      K[var0] = K[var1];
      F[var0] = F[var1];
      b[var0] = b[var1];
      o[var0] = o[var1];
      P[var0] = P[var1];
   }

   public static void YAsVoid() {
      int var0 = !X.isEmpty() ? 1 : 0;
      I = 0;
      n = 0L;
      MenuPopupProvider = false;
      X.clear();
      ChinaHatCodec.clear();
      S.clear();
      Z = null;
      if (var0 != 0) {
         WetReflectionEffect.TAsVoid();
      }

      DropletSurfaceRenderer.EAsVoid();
   }

   private VolumetricWaterEngine() {
   }
}
