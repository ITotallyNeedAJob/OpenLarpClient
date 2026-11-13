package com.larpclient;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public enum SpawnerEntityCatalog {
   SKELETON("Skeleton", EntityType.SKELETON, Items.BONE, Items.ARROW, null),
   IRON_GOLEM("Iron Golem", EntityType.IRON_GOLEM, Items.IRON_INGOT, Items.AIR, "MHF_Golem"),
   PIGLIN("Zombified Piglin", EntityType.ZOMBIFIED_PIGLIN, Items.ROTTEN_FLESH, Items.GOLD_NUGGET, "MHF_PigZombie");

   public final String g;
   public final EntityType<?> w;
   public final Item a;
   public final Item o;
   private final String N;
   private ProfileComponent h;

   private SpawnerEntityCatalog(String var3, EntityType<?> var4, Item var5, Item var6, String var7) {
      this.g = var3;
      this.w = var4;
      this.a = var5;
      this.o = var6;
      this.N = var7;
   }

   public ItemStack AAsItemStack() {
      ItemStack var1 = new ItemStack(this.N == null ? Items.SKELETON_SKULL : Items.PLAYER_HEAD);
      if (this.N != null) {
         if (this.h == null) {
            JsonObject var2 = new JsonObject();
            var2.addProperty("name", this.N);
            this.h = (ProfileComponent)ProfileComponent.CODEC.parse(JsonOps.INSTANCE, var2).getOrThrow();
         }

         var1.set(DataComponentTypes.PROFILE, this.h);
         var1.set(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplayComponent.DEFAULT.with(DataComponentTypes.PROFILE, true));
      }

      return var1;
   }

   public static boolean oAsBoolean(Item var0) {
      for (SpawnerEntityCatalog var4 : values()) {
         if (var0 == var4.a || var0 != Items.AIR && var0 == var4.o) {
            return true;
         }
      }

      return false;
   }
}
