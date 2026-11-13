package com.larpclient;

import java.util.function.Function;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.BlockPos.Mutable;

public final class u {
   public static boolean gAsBoolean(BlockState var0) {
      return var0.isOf(Blocks.KELP)
         || var0.isOf(Blocks.KELP_PLANT)
         || var0.isOf(Blocks.BAMBOO)
         || var0.isOf(Blocks.SWEET_BERRY_BUSH)
         || var0.isOf(Blocks.VINE)
         || var0.isOf(Blocks.POINTED_DRIPSTONE);
   }

   public static u.Type_o IAsU_Type_o(BlockState var0, BlockPos var1, Function<BlockPos, BlockState> var2) {
      if (!var0.isOf(Blocks.KELP) && !var0.isOf(Blocks.KELP_PLANT)) {
         if (var0.isOf(Blocks.BAMBOO)) {
            if (var2.apply(var1.down()).isOf(Blocks.BAMBOO)) {
               return null;
            } else {
               return xAsInt(var1, Direction.UP, var2, Blocks.BAMBOO) >= 12 ? u.Type_o.BAMBOO : null;
            }
         } else if (var0.isOf(Blocks.SWEET_BERRY_BUSH)) {
            return var0.get(Properties.AGE_3) == 3 ? u.Type_o.BERRIES : null;
         } else if (var0.isOf(Blocks.VINE)) {
            if (var2.apply(var1.up()).isOf(Blocks.VINE)) {
               return null;
            } else {
               return xAsInt(var1, Direction.DOWN, var2, Blocks.VINE) >= 7 ? u.Type_o.VINES : null;
            }
         } else if (var0.isOf(Blocks.POINTED_DRIPSTONE) && var0.get(Properties.VERTICAL_DIRECTION) == Direction.DOWN) {
            BlockState var4 = var2.apply(var1.up());
            if (var4.isOf(Blocks.POINTED_DRIPSTONE) && var4.get(Properties.VERTICAL_DIRECTION) == Direction.DOWN) {
               return null;
            } else {
               return xAsInt(
                        var1,
                        Direction.DOWN,
                        var1x -> {
                           BlockState var2x = var2.apply(var1x);
                           return var2x.isOf(Blocks.POINTED_DRIPSTONE) && var2x.get(Properties.VERTICAL_DIRECTION) != Direction.DOWN
                              ? Blocks.AIR.getDefaultState()
                              : var2x;
                        },
                        Blocks.POINTED_DRIPSTONE
                     )
                     >= 5
                  ? u.Type_o.DRIPSTONE
                  : null;
            }
         } else {
            return null;
         }
      } else if (KAsBoolean(var2.apply(var1.up()))) {
         return null;
      } else {
         int var3 = xAsInt(var1, Direction.DOWN, var2, Blocks.KELP, Blocks.KELP_PLANT);
         return var3 >= 14 || var0.isOf(Blocks.KELP) && var0.get(Properties.AGE_25) == 25 && var3 >= 8 ? u.Type_o.KELP : null;
      }
   }

   private static boolean KAsBoolean(BlockState var0) {
      return var0.isOf(Blocks.KELP) || var0.isOf(Blocks.KELP_PLANT);
   }

   public static int xAsInt(BlockPos var0, Direction var1, Function<BlockPos, BlockState> var2, Block... var3) {
      int var4 = 1;

      for (Mutable var5 = var0.mutableCopy(); var4 < 40; var4++) {
         var5.move(var1);
         BlockState var6 = var2.apply(var5);
         byte var7 = 0;

         for (Block var11 : var3) {
            if (var6.isOf(var11)) {
               var7 = 1;
               break;
            }
         }

         if (var7 == 0) {
            break;
         }
      }

      return var4;
   }

   public static boolean DAsBoolean(BlockState var0, int var1, BlockPos var2, Function<BlockPos, BlockState> var3) {
      if (var1 == 5 && (var0.isAir() || var0.isOf(Blocks.AMETHYST_CLUSTER))) {
         for (int var4 = -1; var4 <= 1; var4++) {
            for (int var5 = -1; var5 <= 1; var5++) {
               for (int var6 = -1; var6 <= 1; var6++) {
                  if (var4 != 0 || var5 != 0 || var6 != 0) {
                     BlockState var7 = var3.apply(var2.add(var4, var5, var6));
                     if (var7.isOf(Blocks.AMETHYST_BLOCK) || var7.isOf(Blocks.BUDDING_AMETHYST)) {
                        return true;
                     }
                  }
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private u() {
   }

   public static enum Type_o {
      KELP,
      BAMBOO,
      BERRIES,
      VINES,
      DRIPSTONE;

      private Type_o() {
      }
   }
}
