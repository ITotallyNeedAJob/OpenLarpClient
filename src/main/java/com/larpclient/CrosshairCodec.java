package com.larpclient;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.TextureFormat;
import java.nio.ByteBuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.texture.GlTexture;
import org.lwjgl.opengl.GL11;

public final class CrosshairCodec extends SimpleFramebuffer {
   public CrosshairCodec(String var1, int var2, int var3, boolean var4) {
      super(var1, var2, var3, var4);
   }

   public void initFbo(int width, int height) {
      super.initFbo(width, height);
      this.getColorAttachmentView().close();
      this.getColorAttachment().close();
      this.colorAttachmentView = null;
      this.colorAttachment = null;
      int var3 = GL11.glGenTextures();
      int var4 = GL11.glGetInteger(32873);

      try {
         GL11.glBindTexture(3553, var3);
         GL11.glTexParameteri(3553, 33084, 0);
         GL11.glTexParameteri(3553, 33085, 0);
         GL11.glTexImage2D(3553, 0, 34842, width, height, 0, 6408, 5126, (ByteBuffer)null);
         if (GL11.glGetTexLevelParameteri(3553, 0, 4099) != 34842) {
            throw new IllegalStateException("RGBA16F mask allocation did not retain floating storage");
         }

         this.colorAttachment = new CrosshairCodec.Type_e(this.name, width, height, var3);
         this.colorAttachmentView = RenderSystem.getDevice().createTextureView(this.colorAttachment);
      } catch (RuntimeException var9) {
         if (this.colorAttachment == null) {
            GL11.glDeleteTextures(var3);
         }

         this.delete();
         throw var9;
      } finally {
         GL11.glBindTexture(3553, var4);
      }
   }

   private static final class Type_e extends GlTexture {
      Type_e(String var1, int var2, int var3, int var4) {
         super(14, var1, TextureFormat.RGBA8, var2, var3, 1, 1, var4);
      }
   }
}
