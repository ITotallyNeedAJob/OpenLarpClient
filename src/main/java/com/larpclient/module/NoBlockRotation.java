package com.larpclient.module;

import com.larpclient.ClientLog;
import net.minecraft.client.MinecraftClient;

public final class NoBlockRotation extends Module {
   private static volatile NoBlockRotation f;
   public static final long Y = 42L;
   private static volatile boolean M;

   public NoBlockRotation() {
      super("No Block Rotation", "Keep randomized block textures facing consistently", Category.RENDER, -1);
      f = this;
   }

   public static boolean EAsBoolean() {
      return f != null && f.isEnabled() ? M : false;
   }

   @Override
   public void onEnable() {
      M = true;
      this.lAsVoid();
   }

   @Override
   public void onDisable() {
      M = false;
      this.lAsVoid();
   }

   private void lAsVoid() {
      MinecraftClient var1 = getMc();
      if (var1 != null && var1.world != null && var1.worldRenderer != null) {
         var1.worldRenderer.reload();
         ClientLog.XAsVoid("Terrain", "No Block Rotation: {} uniform seed; rebuilding chunk models", M ? "enabled" : "disabled");
      }
   }
}
