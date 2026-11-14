package com.larpclient;

import com.larpclient.module.SkyShader;
import java.nio.ByteBuffer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;

public final class DropletTrailRenderer extends SkyShaderEffect {
   private final SkyShader U;

   public DropletTrailRenderer(String var1, SkyShader var2) {
      super("larp/" + var1);
      this.U = var2;
   }

   @Override
   protected void advance(BloomSettings var1) {
      this.i = this.i + var1.a * (double)this.U.TAsFloat();
   }

   @Override
   protected void write(BloomSettings var1, ByteBuffer var2) {
      Camera var3 = MinecraftClient.getInstance().gameRenderer.getCamera();
      YAsVoid(var2, (float)this.i, (float)var1.L.textureWidth, (float)var1.L.textureHeight, 0.0F);
      int var4 = this.U.ZAsInt();
      YAsVoid(var2, (float)(var4 >> 16 & 0xFF) / 255.0F, (float)(var4 >> 8 & 0xFF) / 255.0F, (float)(var4 & 0xFF) / 255.0F, this.U.jAsFloat());
      YAsVoid(var2, 1.0F, this.U.NAsFloat(), this.U.FAsFloat(), this.U.BAsFloat());
      YAsVoid(var2, (float)Math.toRadians((double)var3.getYaw()), (float)Math.toRadians((double)var3.getPitch()), WebRenderer.pAsFloat(), 0.0F);
   }
}
