package com.larpclient;

import net.minecraft.item.ItemStack;

public final class w0 {
   private w0() {
   }

   public static boolean dAsBoolean(ItemStack var0) {
      if (var0 != null && (Object)var0 instanceof w0.Type_l var1 && var1.fakeSpawner$isWithdrawnBone()) {
         return true;
      }

      return false;
   }

   public static void HAsVoid(ItemStack var0, boolean var1) {
      if (var0 != null && !var0.isEmpty() && (Object)var0 instanceof w0.Type_l var2) {
         var2.fakeSpawner$setWithdrawnBone(var1);
      }
   }

   public interface Type_l {
      boolean fakeSpawner$isWithdrawnBone();

      void fakeSpawner$setWithdrawnBone(boolean var1);
   }
}
