package com.larpclient;

import com.larpclient.module.Halloween;
import java.nio.ByteBuffer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;

public final class dy extends SkyShaderEffect {
   private static int o = -15260879;

   public dy() {
      super("larp/after_dark");
   }

   public static void EAsVoid(int var0) {
      o = var0;
   }

   @Override
   protected void advance(bl var1) {
      this.i = this.i + var1.a;
   }

   @Override
   protected void write(bl var1, ByteBuffer var2) {
      Camera var3 = MinecraftClient.getInstance().gameRenderer.getCamera();
      CinematicQuality var4 = Halloween.HAsHalloween().fAsM7();
      YAsVoid(var2, (float)this.i, (float)var1.L.textureWidth, (float)var1.L.textureHeight, 0.0F);
      fAsVoid(var2, o);
      YAsVoid(var2, (float)var4.n, (float)var4.s, var4.EAsInt() > 12 ? 4.0F : 3.0F, 0.0F);
      YAsVoid(var2, (float)Math.toRadians((double)var3.getYaw()), (float)Math.toRadians((double)var3.getPitch()), w3.pAsFloat(), 0.0F);
   }
}
