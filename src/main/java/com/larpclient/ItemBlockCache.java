package com.larpclient;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.BlockPos;

public final class ItemBlockCache {
   private static Object t;
   private static final Map<BlockPos, Long> X = new HashMap<>();

   public ItemBlockCache() {
   }

   private static void yAsVoid() {
      ClientWorld var0 = MinecraftClient.getInstance().world;
      if (var0 != t) {
         SmoothInput = var0;
         X.clear();
      }

      long var1 = System.currentTimeMillis();
      X.values().removeIf(var2 -> var2 < var1);
   }

   public static void xAsVoid(BlockPos var0) {
      yAsVoid();
      X.put(var0.toImmutable(), System.currentTimeMillis() + 10000L);
   }

   public static void RAsVoid() {
      yAsVoid();
      MinecraftClient var0 = MinecraftClient.getInstance();
      if (var0.world != null && !X.isEmpty()) {
         for (Entity var2 : var0.world.getEntities()) {
            if (var2 instanceof ItemEntity var3) {
               tAsVoid(var3);
            }
         }
      }
   }

   public static void tAsVoid(ItemEntity var0) {
      yAsVoid();
      if (var0 != null && FakeSpawnerController.ZAsMa().LAsNa(var0.getStack().getItem()) != null) {
         for (BlockPos var2 : X.keySet()) {
            if (var0.squaredDistanceTo((double)var2.getX() + 0.5, (double)var2.getY() + 0.5, (double)var2.getZ() + 0.5) < 4.0) {
               Sha256Digest.RAsVoid(var0.getStack(), true);
               return;
            }
         }
      }
   }
}
