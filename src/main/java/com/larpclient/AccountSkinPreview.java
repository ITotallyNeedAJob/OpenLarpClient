package com.larpclient;

import com.mojang.authlib.GameProfile;
import java.util.function.Supplier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.entity.player.SkinTextures;

public final class AccountSkinPreview {
   private static final long c = 10000000000L;
   private Supplier<SkinTextures> h;
   private GameProfile T;
   private int U = -1;
   private SkinTextures G;
   private long l = -1L;

   public AccountSkinPreview() {
   }

   public SkinTextures FAsSkinTextures() {
      MinecraftClient var1 = MinecraftClient.getInstance();
      int var2 = BloomQuality.J;
      GameProfile var3 = var1.getGameProfile();
      if (this.h == null || !var3.equals(this.T) || this.U != var2) {
         if (this.T != null && !this.T.id().equals(var3.id())) {
            this.G = DefaultSkinHelper.getSkinTextures(var3);
            this.l = System.nanoTime();
            ClientLog.GAsVoid("Accounts", "Game account changed; menu skin lookup restarted, native fallback shown until the texture loads");
         }

         this.T = var3;
         this.U = var2;
         this.h = var1.getSkinProvider().supplySkinTextures(this.T, false);
      }

      SkinTextures var4 = this.h.get();
      if (this.l >= 0L) {
         this.pAsVoid(var4);
      }

      return var4;
   }

   private void pAsVoid(SkinTextures var1) {
      long var2 = System.nanoTime() - this.l;
      if (!var1.equals(this.G)) {
         ClientLog.GAsVoid("Accounts", "Switched account skin ready after {} ms", var2 / 1000000L);
         this.l = -1L;
      } else if (var2 > 10000000000L) {
         ClientLog.oAsVoid(
            "Accounts",
            "Switched account still shows the default skin after {} s; it has no custom skin or the skin service is unavailable",
            var2 / 1000000000L
         );
         this.l = -1L;
      }
   }

   public void fAsVoid(DrawContext var1, int var2, int var3, int var4) {
      PlayerSkinDrawer.draw(var1, this.FAsSkinTextures(), var2, var3, var4, QueueGroup.iAsInt(-1));
   }
}
