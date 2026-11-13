package com.larpclient;

import com.larpclient.module.CustomSky;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import java.io.IOException;
import java.nio.ByteBuffer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.util.Identifier;

public final class NebulaSkyRenderer extends SkyShaderEffect {
   private static final Identifier I = Identifier.of("larp-client", "textures/sky/nebula/nebula_clouds.png");
   private ResourceTexture b;

   public NebulaSkyRenderer() {
      super("nebula", "NebulaClouds");
   }

   @Override
   protected void load(bl var1) throws IOException {
      this.b = new ResourceTexture(I);
      this.b.reload(this.b.loadContents(MinecraftClient.getInstance().getResourceManager()));
   }

   @Override
   protected void advance(bl var1) {
      this.i = this.i + var1.a * (double)var1.U.nebulaAnimationSpeed();
      this.T = (this.T + var1.a * Math.toRadians((double)var1.U.nebulaRotationSpeed())) % (Math.PI * 2);
   }

   @Override
   protected void write(bl var1, ByteBuffer var2) {
      CustomSky var3 = var1.U;
      ThemePreset var4 = var3.YAsNv();
      fAsVoid(var2, var4.zenith());
      fAsVoid(var2, var4.horizon());
      YAsVoid(var2, var3.fAsFloat(), 0.0F, var3.KAsFloat(), 0.0F);
      YAsVoid(var2, var3.nebulaBrightness(), var3.nebulaSaturation(), var3.nebulaIntensity(), var3.starDensity());
      YAsVoid(var2, (float)this.i, (float)this.T, 0.0F, 0.0F);
      YAsVoid(var2, var3.XAsBoolean() ? 1.0F : 0.0F, var3.yAsFloat(), var3.xAsFloat(), 0.0F);
      fAsVoid(var2, var3.nebulaColor());
   }

   @Override
   public void bindTextures(RenderPass var1) {
      var1.bindTexture("NebulaClouds", this.b.getGlTextureView(), RenderSystem.getSamplerCache().get(FilterMode.LINEAR));
   }

   @Override
   public void close() {
      super.close();
      if (this.b != null) {
         this.b.close();
         this.b = null;
      }
   }
}
