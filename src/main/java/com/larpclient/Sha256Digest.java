package com.larpclient;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.stream.Collectors;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;

public final class Sha256Digest {
   public static void RAsVoid(ItemStack var0, boolean var1) {
      if (!var0.isEmpty() && (Object)var0 instanceof Sha256Digest.Type_c var2) {
         var2.fakeElytra$setRealPurchase(var1);
      }
   }

   private Sha256Digest() {
   }

   public static ItemStack fAsItemStack(ItemStack var0) {
      if (!var0.isEmpty() && (Object)var0 instanceof Sha256Digest.Type_c var1) {
         var1.fakeSpawner$setPreview(true);
      }

      return var0;
   }

   public static boolean oAsBoolean(ItemStack var0) {
      if (var0 != null && !var0.isEmpty() && (Object)var0 instanceof Sha256Digest.Type_c var1 && var1.fakeSpawner$isPreview()) {
         return true;
      }

      return false;
   }

   public static String DAsString(ItemStack var0) {
      String var1 = var0.getComponents()
         .stream()
         .filter(
            var0x -> var0x.type() != DataComponentTypes.LORE
                  && var0x.type() != DataComponentTypes.CUSTOM_NAME
                  && var0x.type() != DataComponentTypes.ITEM_NAME
                  && var0x.type() != DataComponentTypes.DAMAGE
                  && var0x.type() != DataComponentTypes.REPAIR_COST
         )
         .map(Object::toString)
         .sorted()
         .collect(Collectors.joining("\n"));
      return EAsString(var0.getItem().getRegistryEntry().registryKey().getValue() + "\n" + var1);
   }

   public static String EAsString(String var0) {
      try {
         return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(var0.getBytes(StandardCharsets.UTF_8)));
      } catch (NoSuchAlgorithmException var2) {
         throw new IllegalStateException(var2);
      }
   }

   public static boolean RAsBoolean(ItemStack var0) {
      if (var0 != null && !var0.isEmpty() && (Object)var0 instanceof Sha256Digest.Type_c var1 && var1.fakeElytra$isRealPurchase()) {
         return true;
      }

      return false;
   }

   public interface Type_c {
      boolean fakeElytra$isRealPurchase();

      void fakeElytra$setRealPurchase(boolean var1);

      boolean fakeSpawner$isPreview();

      void fakeSpawner$setPreview(boolean var1);
   }
}
