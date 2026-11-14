package com.larpclient;

import java.util.function.Predicate;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;

public final class OutlineEffect {
   private static final SkyModeState h = new SkyModeState();

   public static void cAsVoid(SkyModeState.Type_q var0, int var1, Predicate<ItemStack> var2) {
      final MinecraftClient var3 = MinecraftClient.getInstance();
      final ClientPlayerEntity var4 = var3.player;
      if (var4 == null) {
         h.SAsVoid(var0);
      } else {
         h.qAsVoid(var0, new SkyModeState.Type_b() {
            @Override
            public Object player() {
               return var4;
            }

            @Override
            public int tick() {
               return var4.age;
            }

            @Override
            public boolean current() {
               return var3.player == var4 && var3.world == var4.getEntityWorld() && var4.isAlive();
            }

            @Override
            public boolean allowed() {
               return this.current() && !var4.isSpectator() && var3.currentScreen == null && !ParticleEmitter.oAsBoolean();
            }

            @Override
            public int selected() {
               return var4.getInventory().selectedSlot;
            }

            @Override
            public void select(int var1) {
               var4.getInventory().selectedSlot = var1;
            }

            @Override
            public boolean usable(int var1) {
               ItemStack var2x = var4.getInventory().getStack(var1);
               return !var2x.isEmpty() && var2.test(var2x);
            }

            @Override
            public boolean usingItem() {
               return var4.isUsingItem();
            }

            @Override
            public boolean manualUse() {
               return ModuleKeybindChecker.pAsBoolean(var3.options.useKey);
            }

            @Override
            public void holdUse(boolean var1) {
               var3.options.useKey.setPressed(var1);
            }

            @Override
            public void releaseUse() {
               int var1x = var3.player != null && var3.currentScreen == null && this.manualUse() ? 1 : 0;
               var3.options.useKey.setPressed(var1x != 0);
               if (var3.player == var4 && var3.interactionManager != null && var4.isUsingItem() && var1x == 0) {
                  var3.interactionManager.stopUsingItem(var4);
               }
            }
         }, var1);
      }
   }

   public static void cAsVoid(SkyModeState.Type_q var0) {
      h.SAsVoid(var0);
   }

   public static void yAsVoid() {
      h.ZAsVoid();
   }

   private OutlineEffect() {
   }
}
