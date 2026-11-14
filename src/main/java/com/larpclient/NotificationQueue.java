package com.larpclient;

import com.google.gson.JsonObject;
import java.util.UUID;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.util.Identifier;

public final class NotificationQueue {
   public static NotificationQueue.Type_i J;

   public static NotificationQueue.Type_i EAsNq_Type_i(UUID var0, UUID var1) {
      return var1.equals(var0) ? J : null;
   }

   public static NotificationQueue.Type_i aAsNq_Type_i(String var0, AppearanceCodec var1, NativeImage var2, NativeImage var3) {
      return new NotificationQueue.Type_i(
         SkinImageValidation.JAsIdentifier(var0 + "/skin", var2),
         SkinImageValidation.JAsIdentifier(var0 + "/cape", var3),
         var1.slim(),
         var1.hideCape(),
         var1.name(),
         var1.cosmetics()
      );
   }

   public static void DAsVoid() {
      if (J != null) {
         J.close();
      }

      J = null;
   }

   private NotificationQueue() {
   }

   public static record Type_i(Identifier skin, Identifier cape, boolean slim, boolean hideCape, String name, JsonObject cosmetics) {
      void close() {
         SkinImageValidation.XAsVoid(this.skin);
         SkinImageValidation.XAsVoid(this.cape);
      }
   }
}
