package com.larpclient;

import java.util.function.Function;
import java.util.function.ToIntFunction;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public final class XClipEffect {
   public static boolean qAsBoolean(BlockPos var0, ToIntFunction<BlockPos> var1, Function<BlockPos, BlockState> var2) {
      int var3 = var1.applyAsInt(var0);
      if (var3 >= 3 && var3 <= 5 && !oAsBoolean(var2.apply(var0))) {
         byte var4 = 0;

         for (Direction var8 : Direction.values()) {
            BlockPos var9 = var0.offset(var8);
            int var10 = var1.applyAsInt(var9);
            if (var10 < 0 || var10 > var3 || oAsBoolean(var2.apply(var9))) {
               return false;
            }

            var4 = (byte)(var4 | (var10 == var3 - 1 ? 1 : 0));
         }

         return var4 != 0;
      } else {
         return false;
      }
   }

   private static boolean oAsBoolean(BlockState var0) {
      return var0.getLuminance() > 0 || var0.isOf(Blocks.MAGMA_BLOCK) || var0.isOf(Blocks.CRYING_OBSIDIAN);
   }

   private XClipEffect() {
   }
}
