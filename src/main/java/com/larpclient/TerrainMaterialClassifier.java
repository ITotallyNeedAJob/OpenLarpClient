package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.BlockRenderLayers;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public final class TerrainMaterialClassifier {
   private static final int g = 1;
   private static final int J = 2;
   private static final int X = 3;
   private static final int D = 4;
   private static final Set<String> W = Set.of(
      "short_grass",
      "tall_grass",
      "short_dry_grass",
      "tall_dry_grass",
      "fern",
      "large_fern",
      "dead_bush",
      "sweet_berry_bush",
      "azalea",
      "flowering_azalea",
      "pink_petals",
      "leaf_litter",
      "wildflowers",
      "bush",
      "firefly_bush",
      "cactus_flower",
      "torchflower_crop",
      "pitcher_crop",
      "pitcher_plant"
   );
   private static final ThreadLocal<TerrainMaterialClassifier.Type_b> O = new ThreadLocal<>();
   private static final ThreadLocal<Integer> U = new ThreadLocal<>();
   private static final ThreadLocal<TerrainMaterialClassifier.Type_e> z = new ThreadLocal<>();
   private static volatile boolean b;
   private static volatile int L = 3;
   public static final int M = 252;
   public static final int B = 251;
   public static final int f = 249;
   private static volatile boolean T;
   private static volatile boolean V;
   private static volatile boolean n;
   private static volatile boolean C;
   private static volatile boolean s;

   public static void hAsVoid() {
      SnowGround var0 = SnowGround.vAsSnowGround();
      b = var0 != null && var0.isRuntimeActive();
      L = var0 != null && var0.pAsInt() >= 2 ? 3 : 2;
      WavingFoliage var1 = WavingFoliage.DAsWavingFoliage();
      T = var1 != null && var1.isRuntimeActive();
      V = T && var1.leaves.getValue();
      n = V && var1.trees.getValue();
      C = T && var1.plants.getValue();
      RealisticWater var2 = RealisticWater.tAsRealisticWater();
      s = var2 != null && var2.isRuntimeActive();
   }

   public static void QAsVoid(BlockState var0, float var1, BlockView var2, BlockPos var3) {
      QAsVoid(var0, 0.0F, var1, 0.0F, var2, var3);
   }

   public static void QAsVoid(BlockState var0, float var1, float var2, float var3, BlockView var4, BlockPos var5) {
      O.remove();
      z.remove();
      if (b && WetReflectionEffect.RAsBoolean() && var0.getFluidState().isEmpty()) {
         VoxelShape var6 = var0.getCollisionShape(var4, var5);
         if (!var6.isEmpty()) {
            float var7 = (float)var6.getMax(Axis.Y);
            boolean var8 = var4.getBlockState(var5.up()).isSideSolidFullSquare(var4, var5.up(), Direction.DOWN);
            ArrayList var9 = new ArrayList();
            if (!var8) {
               for (int var10 = 0; var10 <= 1; var10++) {
                  for (int var11 = -1; var11 <= 1; var11++) {
                     for (int var12 = -1; var12 <= 1; var12++) {
                        if (var11 != 0 || var10 != 0 || var12 != 0) {
                           BlockPos var13 = var5.add(var11, var10, var12);
                           BlockState var14 = var4.getBlockState(var13);
                           if (!var14.isAir()) {
                              for (Box var16 : var14.getCollisionShape(var4, var13).getBoundingBoxes()) {
                                 if (!((double)var10 + var16.minY > (double)(var7 + 0.55F)) && !((double)var10 + var16.maxY <= (double)var7 + 0.001)) {
                                    var9.add(var16.offset((double)var11, (double)var10, (double)var12));
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }

            z.set(
               new TerrainMaterialClassifier.Type_e(
                  var1,
                  var2,
                  var3,
                  var8 ? Float.POSITIVE_INFINITY : var2 + var7,
                  BlockRenderLayers.getBlockLayer(var0) == BlockRenderLayer.SOLID,
                  List.copyOf(var9)
               )
            );
         }
      }

      if (T && !WetReflectionEffect.NAsBoolean()) {
         int var17 = hAsInt(var0);
         if ((var17 != 1 && var17 != 2 || V) && (var17 != 3 || C) && (var17 != 4 || n) && var17 != 0) {
            float var18 = n && (var17 == 4 || var17 == 1) ? NAsFloat(var0, var17, var4, var5) : -1.0F;
            if (var17 != 4 || !(var18 < 0.0F)) {
               int var19 = var0.contains(Properties.DOUBLE_BLOCK_HALF) && var0.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER ? 1 : 0;
               O.set(new TerrainMaterialClassifier.Type_b(var17, var2, var18, var19 != 0));
            }
         }
      }
   }

   public static void zAsVoid() {
      O.remove();
      z.remove();
   }

   static boolean gAsBoolean() {
      return b && WetReflectionEffect.RAsBoolean();
   }

   public static void NAsVoid(BlockState var0, BlockPos var1, BlockView var2, BlockPos var3) {
      if (b) {
         if (var2 != null && BlockRenderLayers.getBlockLayer(var0) != BlockRenderLayer.TRANSLUCENT) {
            QAsVoid(var0, (float)var1.getX(), (float)var1.getY(), (float)var1.getZ(), var2, var3);
         } else {
            zAsVoid();
         }
      }
   }

   public static void aAsVoid() {
      if (b) {
         zAsVoid();
      }
   }

   static boolean jAsBoolean() {
      return b && z.get() != null;
   }

   public static boolean aAsBoolean() {
      TerrainMaterialClassifier.Type_e var0 = z.get();
      return b && var0 != null && Float.isFinite(var0.top) && O.get() == null;
   }

   public static int MAsInt(BakedQuad var0, int[] var1) {
      TerrainMaterialClassifier.Type_e var2 = z.get();
      if (b && var2 != null && Float.isFinite(var2.top) && var2.solid && O.get() == null && var0.face() != Direction.DOWN) {
         int var3 = 0;
         int var4 = 0;

         for (int var5 = 0; var5 < 4; var5++) {
            if (Math.abs(var0.getPosition(var5).y() + var2.y - var2.top) < 0.001F) {
               var3++;
            }

            var4 = Math.max(var4, var1[var5] >>> 16 & 65535);
         }

         return var3 >= 2 && var4 >= 224 ? L : 0;
      } else {
         return 0;
      }
   }

   public static int DAsInt(int var0, float var1) {
      return OAsInt(var0, Float.NaN, var1, Float.NaN);
   }

   public static int OAsInt(int var0, float var1, float var2, float var3) {
      TerrainMaterialClassifier.Type_b var4 = O.get();
      if (var4 == null) {
         TerrainMaterialClassifier.Type_e var11 = z.get();
         if (var11 == null) {
            return var0;
         } else {
            short var12 = 251;
            if (var2 >= var11.top - 0.001F) {
               var12 = 252;
               float var14 = var1 - var11.x;
               float var8 = var3 - var11.z;

               for (Box var10 : var11.blockers) {
                  if ((double)var14 >= var10.minX - 0.001
                     && (double)var14 <= var10.maxX + 0.001
                     && (double)var8 >= var10.minZ - 0.001
                     && (double)var8 <= var10.maxZ + 0.001) {
                     var12 = 249;
                     break;
                  }
               }
            }

            return var0 & 16777215 | var12 << 24;
         }
      } else {
         float var5 = Math.clamp(var2 - var4.blockY, 0.0F, 1.0F);
         int var6;
         if (var4.height >= 0.0F) {
            float var7 = var4.height + var5;
            var6 = var4.kind == 4 ? 100 + Math.clamp((long)Math.round(var7 * 2.0F), 0, 49) : 2 + Math.clamp((long)Math.round(var7 * 4.0F), 0, 97);
         } else if (var4.kind == 1) {
            var6 = 254;
         } else if (var4.kind == 2) {
            var6 = 253;
         } else {
            float var13 = var4.upper ? 1.0F + var5 * 0.8F : var5;
            if (var13 < 0.001F) {
               return var0;
            }

            var6 = 150 + Math.clamp((long)Math.round(var13 * 50.0F), 0, 90);
         }

         return var0 & 16777215 | var6 << 24;
      }
   }

   public static void QAsVoid(BlockView var0, BlockPos var1, FluidState var2) {
      U.remove();
      if (s && !WetReflectionEffect.NAsBoolean() && var2.isIn(FluidTags.WATER)) {
         short var3;
         if (VolumetricWaterEngine.LAsBoolean(var1)) {
            var3 = 150;
         } else if (!var2.isStill() && var2.contains(FlowableFluid.FALLING) && (Boolean)var2.get(FlowableFluid.FALLING)) {
            var3 = 160;
         } else {
            Vec3d var4 = var2.getVelocity(var0, var1);
            var3 = (short)(var4.x == 0.0 && var4.z == 0.0 ? 224 : 192 + ((int)Math.round(Math.atan2(var4.z, var4.x) * 32.0 / (Math.PI * 2)) & 31));
         }

         U.set(Integer.valueOf(var3));
      }
   }

   public static int iAsInt(int var0) {
      Integer var1 = U.get();
      return var1 == null ? var0 : var0 & 16777215 | var1 << 24;
   }

   public static void gAsVoid() {
      U.remove();
   }

   private static int hAsInt(BlockState var0) {
      String var1 = Registries.BLOCK.getId(var0.getBlock()).getPath();
      if (var1.endsWith("_leaves")) {
         return 1;
      } else if (var1.equals("vine")) {
         return 2;
      } else if ((var1.endsWith("_log") || var1.endsWith("_stem")) && !var1.startsWith("stripped_")) {
         return 4;
      } else {
         return !W.contains(var1)
               && !var1.endsWith("_sapling")
               && !var0.isIn(BlockTags.FLOWERS)
               && !var0.isIn(BlockTags.CROPS)
               && !(var0.getBlock() instanceof CropBlock)
            ? 0
            : 3;
      }
   }

   private static boolean YAsBoolean(BlockState var0) {
      return hAsInt(var0) == 1 && (!var0.contains(Properties.PERSISTENT) || !(Boolean)var0.get(Properties.PERSISTENT));
   }

   private static float NAsFloat(BlockState var0, int var1, BlockView var2, BlockPos var3) {
      if (var1 == 1 && !YAsBoolean(var0)) {
         return -1.0F;
      } else {
         Mutable var4 = var3.mutableCopy();
         int var5 = 0;
         if (var1 == 1) {
            byte var6;
            for (var6 = 0; var5 < 24; var5++) {
               var4.move(0, -1, 0);
               BlockState var7 = var2.getBlockState(var4);
               if (hAsInt(var7) == 4) {
                  var5++;
                  var6 = 1;
                  break;
               }

               if (!var7.isAir() && hAsInt(var7) != 1) {
                  return -1.0F;
               }
            }

            if (var6 == 0) {
               return -1.0F;
            }
         } else {
            Mutable var12 = var3.mutableCopy();

            for (int var14 = 0; var14 < 24 && hAsInt(var2.getBlockState(var12.up())) == 4; var14++) {
               var12.move(0, 1, 0);
            }

            byte var15 = 0;

            for (Direction var11 : Direction.values()) {
               if (var11 != Direction.DOWN && YAsBoolean(var2.getBlockState(var12.offset(var11)))) {
                  var15 = 1;
                  break;
               }
            }

            if (var15 == 0) {
               return -1.0F;
            }
         }

         int var13;
         for (var13 = 0; var13 < 24 && hAsInt(var2.getBlockState(var4.down())) == 4; var13++) {
            var4.move(0, -1, 0);
         }

         return (float)(var5 + var13);
      }
   }

   private TerrainMaterialClassifier() {
   }

   private static record Type_b(int kind, float blockY, float height, boolean upper) {
   }

   private static record Type_e(float x, float y, float z, float top, boolean solid, List<Box> blockers) {
   }
}
