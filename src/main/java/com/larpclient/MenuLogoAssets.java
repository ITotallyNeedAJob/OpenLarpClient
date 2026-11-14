package com.larpclient;

import com.larpclient.module.Halloween;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.render.state.SimpleGuiElementRenderState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.TextureSetup;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fc;

public final class MenuLogoAssets {
   private static final Identifier r = Identifier.of("larp-client", "textures/gui/logo.png");
   private static final Identifier Y = Identifier.of("larp-client", "textures/gui/logo-halloween.png");
   private static final BloomUtility GroundSample = new BloomUtility();
   private static int O = -1;
   private static boolean V;
   private static boolean N;
   private static boolean S;
   private static Identifier u;

   private MenuLogoAssets() {
   }

   public static Identifier oAsIdentifier() {
      if (O != BloomQuality.J) {
         O = BloomQuality.J;
         ResourceManager var0 = MinecraftClient.getInstance().getResourceManager();
         V = var0.getResource(r).isPresent();
         N = var0.getResource(Y).isPresent();
         if (!V) {
            ClientLog.oAsVoid("Menu", "Logo texture missing; monochrome waves remain available, retry after resource reload");
         }

         if (!N) {
            ClientLog.oAsVoid("Menu", "Halloween logo missing; using the regular logo, retry after resource reload");
         }

         CropHelper = null;
         S = false;
      }

      Identifier var1 = Halloween.vAsBoolean() && N ? Y : r;
      if (!var1.equals(u)) {
         CropHelper = var1;
         ClientLog.GAsVoid("Menu", "Menu logo: {}; standard backdrop and navigation retained", var1);
      }

      return var1;
   }

   public static void gAsVoid(DrawContext var0, int var1, int var2) {
      if (var1 > 0 && var2 > 0) {
         MinecraftClient var3 = MinecraftClient.getInstance();
         ClientPreferences var4 = LarpClient.CAsLarpClient().fAsBo().yAsN7();
         int var5 = var4.Z && var4.L != WorldTimePreset.Type_b.STATIC ? 1 : 0;
         Identifier var6 = oAsIdentifier();
         double var7 = var3.currentScreen instanceof ClientTitleScreen var9 ? var9.ZAsNc().BAsDouble() : 1.0;
         GroundSample.nAsVoid(System.nanoTime(), var5 != 0, (Double)var3.options.getPanoramaSpeed().getValue());
         var0.fill(0, 0, var1, var2, bg.xAsInt(-16777216, -16185079, var7));
         var0.createNewRootLayer();
         float var19 = (float)(1 / Math.max(1, var3.getWindow().getScaleFactor()));

         for (int var20 = 0; var20 < 2; var20++) {
            int var11 = var20 == 1 ? 1 : 0;
            float var12 = (float)((1.0 - var7) * BloomUtility.VAsDouble(var1, var2) * (double)(var11 != 0 ? 1 : -1));
            Matrix3x2f var13 = new Matrix3x2f(var0.getMatrices()).translate(var12, 0.0F);
            int var14 = (int)Math.ceil(BloomUtility.VAsDouble(var1, var2) + (double)var19);
            ScreenRect var15 = new ScreenRect(var11 != 0 ? var1 - var14 : 0, 0, var14, var2).transformEachVertex(var13);
            if (var0.scissorStack.peekLast() != null) {
               var15 = var0.scissorStack.peekLast().intersection(var15);
            }

            var0.state.addSimpleElement(new MenuLogoAssets.Type_v(var13, var1, var2, var11 != 0, GroundSample.DAsDouble(), var19, var0.scissorStack.peekLast(), var15));
         }

         if ((var6.equals(Y) || V) && var4.O) {
            var0.createNewRootLayer();

            for (int var21 = 0; var21 < 18; var21++) {
               BloomUtility.Type_h var22 = GroundSample.zAsB_Type_h(var21, var1, var2);
               if (!(var22.alpha() < 0.001)) {
                  int var23 = (int)Math.round(var22.size());
                  var0.getMatrices().pushMatrix();

                  try {
                     double var24 = (1.0 - var7) * BloomUtility.VAsDouble(var1, var2) * (double)(var21 % 2 == 0 ? -1 : 1);
                     var0.getMatrices().translate((float)(var22.x() + var24), (float)var22.y());
                     var0.getMatrices().rotate((float)var22.rotation());
                     var0.drawTexture(
                        RenderPipelines.GUI_TEXTURED,
                        var6,
                        -var23 / 2,
                        -var23 / 2,
                        0.0F,
                        0.0F,
                        var23,
                        var23,
                        64,
                        64,
                        64,
                        64,
                        bg.XAsInt(-1, var22.alpha() * var7)
                     );
                  } finally {
                     var0.getMatrices().popMatrix();
                  }
               }
            }
         }

         if (!S) {
            S = true;
            ClientLog.GAsVoid("Menu", "Lit monochrome side waves ready; {} logo slots, shaded folds, Minecraft panorama speed controls motion", 18);
         }
      }
   }

   public static void ZAsVoid() {
      O = -1;
      CropHelper = null;
      S = false;
   }

   static record Type_v(Matrix3x2fc pose, int width, int height, boolean right, double time, float feather, ScreenRect scissorArea, ScreenRect bounds)
      implements SimpleGuiElementRenderState {
      public RenderPipeline pipeline() {
         return RenderPipelines.GUI;
      }

      public TextureSetup textureSetup() {
         return TextureSetup.empty();
      }

      public void setupVertices(VertexConsumer vertices) {
         float var2 = (float)BloomUtility.VAsDouble(this.width, this.height);
         float var3 = this.right ? (float)this.width : 0.0F;

         for (int var4 = 0; var4 < 6; var4++) {
            for (int var5 = 0; var5 < 96; var5++) {
               double var6 = (double)var5 / 96.0;
               double var8 = ((double)var5 + 1.0) / 96.0;
               float var10 = (float)(var6 * (double)this.height);
               float var11 = (float)(var8 * (double)this.height);
               float var12 = (float)((double)var2 * BloomUtility.sAsDouble(var6, this.time, var4, this.right));
               float var13 = (float)((double)var2 * BloomUtility.sAsDouble(var8, this.time, var4, this.right));
               if (this.right) {
                  var12 = (float)this.width - var12;
                  var13 = (float)this.width - var13;
               }

               int var14 = BloomUtility.hAsInt(var6, var4, this.right);
               int var15 = BloomUtility.hAsInt(var8, var4, this.right);

               for (int var16 = 0; var16 < 4; var16++) {
                  double var17 = BloomUtility.xAsDouble(var16);
                  double var19 = BloomUtility.xAsDouble(var16 + 1);
                  this.quad(
                     vertices,
                     (float)((double)var3 + (double)(var12 - var3) * var17),
                     var10,
                     (float)((double)var3 + (double)(var13 - var3) * var17),
                     var11,
                     (float)((double)var3 + (double)(var13 - var3) * var19),
                     var11,
                     (float)((double)var3 + (double)(var12 - var3) * var19),
                     var10,
                     BloomUtility.eAsInt(var6, var4, this.right, var17),
                     BloomUtility.eAsInt(var8, var4, this.right, var17),
                     BloomUtility.eAsInt(var8, var4, this.right, var19),
                     BloomUtility.eAsInt(var6, var4, this.right, var19)
                  );
               }

               float var21 = this.right ? -this.feather : this.feather;
               this.quad(vertices, var12, var10, var13, var11, var13 + var21, var11, var12 + var21, var10, var14, var15, var15 & 16777215, var14 & 16777215);
            }
         }
      }

      private void quad(
         VertexConsumer var1,
         float var2,
         float var3,
         float var4,
         float var5,
         float var6,
         float var7,
         float var8,
         float var9,
         int var10,
         int var11,
         int var12,
         int var13
      ) {
         if (this.right) {
            var1.vertex(this.pose, var8, var9).color(var13);
            var1.vertex(this.pose, var6, var7).color(var12);
            var1.vertex(this.pose, var4, var5).color(var11);
            var1.vertex(this.pose, var2, var3).color(var10);
         } else {
            var1.vertex(this.pose, var2, var3).color(var10);
            var1.vertex(this.pose, var4, var5).color(var11);
            var1.vertex(this.pose, var6, var7).color(var12);
            var1.vertex(this.pose, var8, var9).color(var13);
         }
      }
   }
}
