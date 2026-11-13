package com.larpclient;

import com.larpclient.module.AutoTotem;
import com.larpclient.module.LungeSwap;
import com.larpclient.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

public final class pg {
   private static pg B;
   private final Module P;
   private final xd T = new xd();
   private ClientPlayerEntity S;
   private Object x;

   public pg(Module var1) {
      this.P = var1;
   }

   public boolean FAsBoolean() {
      return this.T.qAsBoolean();
   }

   public boolean PAsBoolean() {
      return this.FAsBoolean() && this.pAsBoolean() && B == this && this.T.qAsBoolean(this.S.getInventory().getSelectedSlot());
   }

   public static boolean yAsBoolean(Module var0) {
      if (B != null && !B.pAsBoolean()) {
         B.tAsVoid();
      }

      if ((B == null || B.P == var0) && !AutoTotem.IAsBoolean()) {
         LarpClient var1 = LarpClient.CAsLarpClient();
         LungeSwap var2 = var1 != null && var1.mAsF() != null ? var1.mAsF().EAsModule(LungeSwap.class) : null;
         return var2 == null || var2 == var0 || !var2.yAsBoolean();
      } else {
         return false;
      }
   }

   public boolean uAsBoolean(ClientPlayerEntity var1, int var2) {
      if (var2 >= 0 && var2 < 9 && var1 != null && yAsBoolean(this.P)) {
         MinecraftClient var3 = MinecraftClient.getInstance();
         if (var1 == var3.player && var1.getEntityWorld() == var3.world) {
            if (this.FAsBoolean() && !this.pAsBoolean()) {
               this.tAsVoid();
            }

            int var4 = var1.getInventory().getSelectedSlot();
            if (var4 == var2 && !this.FAsBoolean()) {
               return true;
            } else {
               this.T.nAsVoid(var4, var2);
               this.S = var1;
               this.x = var3.world;
               B = this;
               var1.getInventory().setSelectedSlot(var2);
               return true;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public void IAsVoid(MinecraftClient var1, boolean var2) {
      if (this.FAsBoolean()) {
         if (!this.pAsBoolean()) {
            this.tAsVoid();
         } else if (AutoTotem.IAsBoolean()) {
            if (B == this) {
               B = null;
            }
         } else if (var1.currentScreen == null && !pk.BAsBoolean(this.S)) {
            this.vAsVoid(var1, var2);
         }
      }
   }

   public void vAsVoid(MinecraftClient var1, boolean var2) {
      boolean var3 = this.pAsBoolean();
      int var4 = this.T.AAsInt(var3 ? this.S.getInventory().getSelectedSlot() : -1, var2 && var3);
      if (var4 >= 0) {
         this.S.getInventory().setSelectedSlot(var4);
      }

      this.tAsVoid();
   }

   private boolean pAsBoolean() {
      MinecraftClient var1 = MinecraftClient.getInstance();
      return this.S != null && this.S == var1.player && this.x == var1.world && this.S.isAlive();
   }

   private void tAsVoid() {
      if (B == this) {
         B = null;
      }

      this.T.lAsVoid();
      this.S = null;
      this.x = null;
   }
}
