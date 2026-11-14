package com.larpclient;

import com.larpclient.module.CustomSky;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.lwjgl.system.MemoryStack;

public abstract class SkyShaderEffect implements SkyEffect {
   private final RenderPipeline t;
   private boolean Q;
   protected double i;
   protected double T;

   protected SkyShaderEffect(String var1, String... var2) {
      this.t = SkyPalette.QAsRenderPipeline(var1, var2);
   }

   public final RenderPipeline LAsRenderPipeline() {
      return this.t;
   }

   @Override
   public final void render(BloomSettings var1) throws IOException {
      if (!this.Q) {
         WorldLabelLayout.DAsVoid(this.t);
         this.load(var1);
         this.Q = true;
         ClientLog.GAsVoid("Sky", "Shader compiled and resources ready: {}", this.t.getFragmentShader());
      }

      this.advance(var1);
      MemoryStack var2 = MemoryStack.stackPush();

      try {
         ByteBuffer var3 = var2.calloc(160);
         this.write(var1, var3);
         var3.position(0).limit(160);
         var1.I.dAsVoid(var1, this, var3);
      } catch (Throwable var61) {
         if (var2 != null) {
            try {
               var2.close();
            } catch (Throwable var5) {
               var61.addSuppressed(var5);
            }
         }

         throw var61;
      }

      if (var2 != null) {
         var2.close();
      }
   }

   protected void advance(BloomSettings var1) {
      this.i = this.i + var1.a * (double)var1.U.animationSpeed();
      this.T = (this.T + var1.a * Math.toRadians((double)var1.U.DAsFloat())) % (Math.PI * 2);
   }

   protected void load(BloomSettings var1) throws IOException {
   }

   public void bindTextures(RenderPass var1) {
   }

   protected abstract void write(BloomSettings var1, ByteBuffer var2);

   protected void AAsVoid(BloomSettings var1, ByteBuffer var2) {
      this.NAsVoid(var1, var2, 1.0F, 1.0F);
   }

   protected void NAsVoid(BloomSettings var1, ByteBuffer var2, float var3, float var4) {
      CustomSky var5 = var1.U;
      fAsVoid(var2, var5.YAsNv().zenith());
      fAsVoid(var2, var5.YAsNv().horizon());
      YAsVoid(var2, (float)this.i, (float)this.T, var5.tAsFloat(), var5.horizonBlend() ? 1.0F : 0.0F);
      YAsVoid(var2, var5.KAsFloat() * var3, var5.starDensity() * var4, 0.0F, var5.AAsFloat());
      fAsVoid(var2, var5.oceanBaseColor());
      fAsVoid(var2, var5.oceanHighlightColor());
   }

   protected static void YAsVoid(ByteBuffer var0, float var1, float var2, float var3, float var4) {
      var0.putFloat(var1).putFloat(var2).putFloat(var3).putFloat(var4);
   }

   protected static void fAsVoid(ByteBuffer var0, int var1) {
      YAsVoid(var0, (float)(var1 >> 16 & 0xFF) / 255.0F, (float)(var1 >> 8 & 0xFF) / 255.0F, (float)(var1 & 0xFF) / 255.0F, 1.0F);
   }

   @Override
   public final void reload() {
      this.close();
   }

   @Override
   public void close() {
      this.Q = false;
   }
}
