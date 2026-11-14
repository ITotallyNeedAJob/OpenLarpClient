package com.larpclient;

import com.larpclient.module.LARP;
import com.larpclient.module.NameChanger;
import java.util.UUID;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerSkinType;
import net.minecraft.entity.player.SkinTextures;
import net.minecraft.util.AssetInfo.TextureAsset;
import net.minecraft.util.AssetInfo.TextureAssetInfo;

public final class ModuleList {
   public ModuleList() {
   }

   public static nq.Type_i dAsNq_Type_i(UUID var0) {
      LARP var1 = LARP.BAsLARP();
      return var1 != null && var1.isOperational() ? nq.EAsNq_Type_i(var0, MinecraftClient.getInstance().getSession().getUuidOrNull()) : null;
   }

   public static SkinTextures HAsSkinTextures(UUID var0, SkinTextures var1) {
      nq.Type_i var2 = dAsNq_Type_i(var0);
      return var2 == null
         ? var1
         : new SkinTextures(
            (TextureAsset)(var2.skin() == null ? var1.body() : new TextureAssetInfo(var2.skin(), var2.skin())),
            (TextureAsset)(var2.hideCape() ? null : (var2.cape() == null ? var1.cape() : new TextureAssetInfo(var2.cape(), var2.cape()))),
            (TextureAsset)(var2.hideCape() ? null : (var2.cape() == null ? var1.elytra() : new TextureAssetInfo(var2.cape(), var2.cape()))),
            var2.skin() == null ? var1.model() : (var2.slim() ? PlayerSkinType.SLIM : PlayerSkinType.WIDE),
            var1.secure()
         );
   }

   public static String cAsString(UUID var0, String var1) {
      String var2 = QAsString(var0);
      return var2.isEmpty() ? var1 : var2;
   }

   public static String QAsString(UUID var0) {
      NameChanger var1 = NameChanger.WAsNameChanger();
      String var2 = var1 == null ? "" : var1.nAsString();
      if (!var2.isEmpty()) {
         MinecraftClient var3 = MinecraftClient.getInstance();
         if (var0.equals(var3.getSession().getUuidOrNull()) || var3.player != null && var0.equals(var3.player.getUuid())) {
            return var2;
         }
      }

      nq.Type_i var4 = dAsNq_Type_i(var0);
      return var4 == null ? "" : var4.name();
   }

   public static boolean jAsBoolean() {
      LARP var0 = LARP.BAsLARP();
      NameChanger var1 = NameChanger.WAsNameChanger();
      return var0 != null && var0.isOperational() || var1 != null && var1.isOperational();
   }
}
