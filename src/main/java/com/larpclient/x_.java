package com.larpclient;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public final class x_ {
   public static final int A = 4;

   public static boolean zAsBoolean(BlockPos var0, int var1, x_.Type_e var2) {
      int var3 = var1 == 1 ? 1 : 0;
      int var4 = var1 == 2 ? 1 : 0;
      int var5 = var1 == 0 ? 1 : 3;

      for (int var6 = 0; var6 < var5; var6++) {
         BlockPos var7 = var0.add(var3 * var6, 0, var4 * var6);
         if (var2.GAsX__Type_m(var7) != x_.Type_m.OPEN) {
            return false;
         }

         if (var1 != 2 && (var2.GAsX__Type_m(var7.north()) != x_.Type_m.FULL || var2.GAsX__Type_m(var7.south()) != x_.Type_m.FULL)) {
            return false;
         }

         if (var1 != 1 && (var2.GAsX__Type_m(var7.east()) != x_.Type_m.FULL || var2.GAsX__Type_m(var7.west()) != x_.Type_m.FULL)) {
            return false;
         }
      }

      return var1 == 0
         || var2.GAsX__Type_m(var0.add(-var3, 0, -var4)) == x_.Type_m.FULL && var2.GAsX__Type_m(var0.add(var3 * 3, 0, var4 * 3)) == x_.Type_m.FULL;
   }

   public static int LAsInt(BlockPos var0, int var1, x_.Type_e var2) {
      if (var0.getY() <= var1 - 3 && var2.GAsX__Type_m(var0) == x_.Type_m.OPEN) {
         for (int var3 = 0; var3 < 3; var3++) {
            byte var4 = 1;

            for (int var5 = 0; var5 < 4; var5++) {
               if (!zAsBoolean(var0.up(var5), var3, var2)) {
                  var4 = 0;
                  break;
               }
            }

            if (var4 != 0) {
               return var3;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   public static Box BAsBox(BlockPos var0, int var1) {
      return new Box(
         (double)var0.getX(),
         (double)var0.getY(),
         (double)var0.getZ(),
         (double)(var0.getX() + (var1 == 1 ? 3 : 1)),
         (double)(var0.getY() + 4),
         (double)(var0.getZ() + (var1 == 2 ? 3 : 1))
      );
   }

   private x_() {
   }

   @FunctionalInterface
   public interface Type_e {
      x_.Type_m GAsX__Type_m(BlockPos var1);
   }

   public static enum Type_m {
      OPEN,
      FULL,
      UNKNOWN;

      private Type_m() {
      }
   }
}
