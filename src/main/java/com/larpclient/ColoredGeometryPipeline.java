package com.larpclient;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.render.state.SimpleGuiElementRenderState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.TextureSetup;
import net.minecraft.client.util.Window;
import net.minecraft.util.Identifier;

public final class ColoredGeometryPipeline {
   private static final VertexFormat Q = VertexFormat.builder()
      .add("Position", VertexFormatElement.POSITION)
      .add("UV0", VertexFormatElement.UV0)
      .add("Color", VertexFormatElement.COLOR)
      .add("UV1", VertexFormatElement.UV1)
      .add("UV2", VertexFormatElement.UV2)
      .build();
   private static final RenderPipeline b = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_SNIPPET})
      .withLocation(Identifier.of("larp-client", "gui_exit_snapshot"))
      .withVertexShader(Identifier.of("larp-client", "gui/snapshot"))
      .withFragmentShader(Identifier.of("larp-client", "gui/snapshot"))
      .withSampler("Sampler0")
      .withVertexFormat(Q, DrawMode.QUADS)
      .build();
   static final double M = 2.0;
   static final double o = 1.6;
   private static SimpleFramebuffer c;
   private static boolean U;
   private static int A = -1;
   private final String h;
   private final long v;
   private final double j;
   private final List<ColoredGeometryPipeline.Type_i> V;
   private final List<ColoredGeometryPipeline.Type_c> t = new ArrayList<>();
   private boolean d;

   private ColoredGeometryPipeline(String var1, List<ColoredGeometryPipeline.Type_i> var2, int var3) {
      this.h = var1;
      this.V = List.copyOf(var2);
      this.v = System.nanoTime();
      this.j = (double)var3 * (var1.equals("CRT") ? 1.6 : 2.0) * 1000000.0;
      Random var4 = new Random(this.v);

      for (int var5 = 0; var5 < var2.size(); var5++) {
         this.AAsVoid(var5, var2.get(var5), var4);
      }
   }

   static ColoredGeometryPipeline YAsQ1(String var0, List<ColoredGeometryPipeline.Type_i> var1, int var2) {
      if (!var1.isEmpty() && mAsBoolean()) {
         ClientLog.GAsVoid("GUI", "{} close effect over {} window(s), {} ms", var0, var1.size(), Math.round((double)var2 * (var0.equals("CRT") ? 1.6 : 2.0)));
         return new ColoredGeometryPipeline(var0, var1, var2);
      } else {
         return null;
      }
   }

   private static boolean mAsBoolean() {
      if (A != BloomQuality.J) {
         IAsVoid();
         A = BloomQuality.J;
         U = false;
      }

      if (U) {
         return false;
      } else {
         try {
            Framebuffer var0 = MinecraftClient.getInstance().getFramebuffer();
            if (c == null || c.textureWidth != var0.textureWidth || c.textureHeight != var0.textureHeight) {
               IAsVoid();
               WorldLabelLayout.DAsVoid(b);
               c = new SimpleFramebuffer("Larp ClickGUI exit", var0.textureWidth, var0.textureHeight, false);
            }

            BloomQuality.ZAsVoid(c);
            return true;
         } catch (RuntimeException var11) {
            U = true;
            IAsVoid();
            ClientLog.lAsVoid("GUI", "Close effect unavailable; ClickGUI plays its reverse motion until resource reload", var11);
            return false;
         }
      }
   }

   double BAsDouble() {
      return Math.clamp((double)(System.nanoTime() - this.v) / this.j, 0.0, 1.0);
   }

   boolean fAsBoolean() {
      return this.BAsDouble() >= 1.0;
   }

   public static void IAsVoid() {
      if (c != null) {
         c.delete();
         c = null;
      }
   }

   void kAsVoid(DrawContext var1) {
      if (c != null) {
         double var2 = this.BAsDouble();
         Window var4 = MinecraftClient.getInstance().getWindow();
         float var5 = (float)var4.getScaleFactor();
         float var6 = (float)c.textureWidth;
         float var7 = (float)c.textureHeight;
         TextureSetup var8 = TextureSetup.of(c.getColorAttachmentView(), RenderSystem.getSamplerCache().get(FilterMode.NEAREST));
         ArrayList var9 = new ArrayList();
         float var10 = Float.MAX_VALUE;
         float var11 = Float.MAX_VALUE;
         float var12 = -Float.MAX_VALUE;
         float var13 = -Float.MAX_VALUE;

         for (ColoredGeometryPipeline.Type_c var15 : this.t) {
            float[] var16 = var15.atAsFloatArray(this, var2);
            if (var16 != null) {
               ColoredGeometryPipeline.Type_i var17 = this.V.get(var15.window);
               float var18 = var17.x + var17.width / 2.0F;
               float var19 = var17.y + var17.height / 2.0F;
               ColoredGeometryPipeline.Type_r var20 = new ColoredGeometryPipeline.Type_r(new float[16], var16[8], var17);

               for (int var21 = 0; var21 < 4; var21++) {
                  var20.data[var21 * 4] = var16[var21 * 2];
                  var20.data[var21 * 4 + 1] = var16[var21 * 2 + 1];
                  var20.data[var21 * 4 + 2] = var15.xs[var21] - var18;
                  var20.data[var21 * 4 + 3] = var15.ys[var21] - var19;
                  var10 = Math.min(var10, var16[var21 * 2]);
                  var11 = Math.min(var11, var16[var21 * 2 + 1]);
                  var12 = Math.max(var12, var16[var21 * 2]);
                  var13 = Math.max(var13, var16[var21 * 2 + 1]);
               }

               var9.add(var20);
            }
         }

         if (!this.d) {
            this.d = true;
            ClientLog.XAsVoid(
               "GUI", "{} close effect drawing {} of {} pieces at {}", this.h, var9.size(), this.t.size(), String.format(Locale.ROOT, "%.2f", var2)
            );
         }

         if (!var9.isEmpty()) {
            int var22 = (int)Math.floor((double)var10);
            int var23 = (int)Math.floor((double)var11);
            ScreenRect var24 = new ScreenRect(var22, var23, (int)Math.ceil((double)var12) - var22 + 1, (int)Math.ceil((double)var13) - var23 + 1);
            var1.state.addSimpleElement(new ColoredGeometryPipeline.Type_u(var8, List.copyOf(var9), var5, var6, var7, var24));
         }

         if (this.h.equals("CRT")) {
            this.xAsVoid(var1, var2);
         }
      }
   }

   private void xAsVoid(DrawContext var1, double var2) {
      double var4 = Math.min(1.0, var2 / 0.55);
      double var6 = 0.9 * var4 * (1.0 - nAsDouble(0.85, 1.0, var2));
      if (!(var6 < 0.01)) {
         for (ColoredGeometryPipeline.Type_i var9 : this.V) {
            float[] var10 = fAsFloatArray(var2);
            float var11 = var9.x + var9.width / 2.0F;
            float var12 = var9.y + var9.height / 2.0F;
            float var13 = var9.width * var10[0];
            float var14 = Math.max(1.0F, var9.height * var10[1]);
            RoundedShapePipeline.nAsVoid(
               var1, var11 - var13 / 2.0F, var12 - var14 / 2.0F, var13, var14, Math.min(var9.radius, Math.min(var13, var14) / 2.0F), ColorUtil.XAsInt(-1, var6)
            );
         }
      }
   }

   private static float[] fAsFloatArray(double var0) {
      double var2 = Math.min(1.0, var0 / 0.55);
      float var4 = (float)(1.0 - WeatherRenderer.Type_e.CUBIC_IN.zAsDouble(var2) * 0.99);
      float var5 = var0 < 0.55 ? 1.0F : (float)Math.max(0.003, 1.0 - WeatherRenderer.Type_e.CUBIC_IN.zAsDouble((var0 - 0.55) / 0.45));
      return new float[]{var5, var4};
   }

   private static double nAsDouble(double var0, double var2, double var4) {
      double var6 = Math.clamp((var4 - var0) / (var2 - var0), 0.0, 1.0);
      return var6 * var6 * (3.0 - 2.0 * var6);
   }

   private void AAsVoid(int var1, ColoredGeometryPipeline.Type_i var2, Random var3) {
      String var4 = this.h;
      switch (var4) {
         case "Shatter":
            this.cAsVoid(var1, var2, var3);
            break;
         case "Slice":
            for (int var17 = 0; var17 < 7; var17++) {
               float var18 = var2.y + var2.height * (float)var17 / 7.0F;
               float var19 = var2.y + var2.height * (float)(var17 + 1) / 7.0F;
               this.t
                  .add(
                     new ColoredGeometryPipeline.Type_c(
                        var1,
                        new float[]{var2.x, var2.x, var2.x + var2.width, var2.x + var2.width},
                        new float[]{var18, var19, var19, var18},
                        var17 % 2 == 0 ? -1.0F : 1.0F,
                        (float)var17 - 3.0F,
                        0.0F,
                        0.06F * (float)var17,
                        var3.nextFloat()
                     )
                  );
            }
            break;
         case "Crumble":
         case "Implode":
            float var6 = Math.max(18.0F, Math.min(var2.width, var2.height) / (float)(this.h.equals("Crumble") ? 9 : 6));
            int var7 = Math.max(2, Math.round(var2.width / var6));
            int var8 = Math.max(2, Math.round(var2.height / var6));

            for (int var9 = 0; var9 < var8; var9++) {
               for (int var10 = 0; var10 < var7; var10++) {
                  float var11 = var2.x + var2.width * (float)var10 / (float)var7;
                  float var12 = var2.x + var2.width * (float)(var10 + 1) / (float)var7;
                  float var13 = var2.y + var2.height * (float)var9 / (float)var8;
                  float var14 = var2.y + var2.height * (float)(var9 + 1) / (float)var8;
                  float var15 = this.h.equals("Crumble")
                     ? 0.42F * (1.0F - ((float)var9 + 0.5F) / (float)var8) + 0.12F * var3.nextFloat()
                     : 0.22F * var3.nextFloat();
                  this.t
                     .add(
                        new ColoredGeometryPipeline.Type_c(
                           var1,
                           new float[]{var11, var11, var12, var12},
                           new float[]{var13, var14, var14, var13},
                           var3.nextFloat() - 0.5F,
                           var3.nextFloat() - 0.5F,
                           var3.nextFloat() * 2.0F - 1.0F,
                           var15,
                           var3.nextFloat()
                        )
                     );
               }
            }
            break;
         default:
            this.t
               .add(
                  new ColoredGeometryPipeline.Type_c(
                     var1,
                     new float[]{var2.x, var2.x, var2.x + var2.width, var2.x + var2.width},
                     new float[]{var2.y, var2.y + var2.height, var2.y + var2.height, var2.y},
                     0.0F,
                     0.0F,
                     0.0F,
                     0.0F,
                     0.0F
                  )
               );
      }
   }

   private void cAsVoid(int var1, ColoredGeometryPipeline.Type_i var2, Random var3) {
      int var4 = Math.clamp((long)Math.round(5.0F * var2.width / Math.max(1.0F, var2.height)), 3, 9);
      float[][] var6 = new float[6][var4 + 1];
      float[][] var7 = new float[6][var4 + 1];
      float var8 = var2.width / (float)var4;
      float var9 = var2.height / 5.0F;

      for (int var10 = 0; var10 <= 5; var10++) {
         for (int var11 = 0; var11 <= var4; var11++) {
            int var12 = var10 > 0 && var10 < 5 && var11 > 0 && var11 < var4 ? 1 : 0;
            var6[var10][var11] = var2.x + (float)var11 * var8 + (var12 != 0 ? (var3.nextFloat() - 0.5F) * 0.64F * var8 : 0.0F);
            var7[var10][var11] = var2.y + (float)var10 * var9 + (var12 != 0 ? (var3.nextFloat() - 0.5F) * 0.64F * var9 : 0.0F);
         }
      }

      float var28 = var2.x + var2.width * (0.4F + 0.2F * var3.nextFloat());
      float var29 = var2.y + var2.height * (0.4F + 0.2F * var3.nextFloat());
      float var30 = (float)Math.hypot((double)var2.width, (double)var2.height) / 2.0F;

      for (int var13 = 0; var13 < 5; var13++) {
         for (int var14 = 0; var14 < var4; var14++) {
            float[][] var15 = new float[][]{
               {var6[var13][var14], var7[var13][var14]},
               {var6[var13 + 1][var14], var7[var13 + 1][var14]},
               {var6[var13 + 1][var14 + 1], var7[var13 + 1][var14 + 1]},
               {var6[var13][var14 + 1], var7[var13][var14 + 1]}
            };
            int[][] var16 = var3.nextBoolean() ? new int[][]{{0, 1, 2}, {0, 2, 3}} : new int[][]{{0, 1, 3}, {1, 2, 3}};

            for (int[] var20 : var16) {
               float[] var21 = new float[]{var15[var20[0]][0], var15[var20[1]][0], var15[var20[2]][0], var15[var20[2]][0]};
               float[] var22 = new float[]{var15[var20[0]][1], var15[var20[1]][1], var15[var20[2]][1], var15[var20[2]][1]};
               float var23 = (var21[0] + var21[1] + var21[2]) / 3.0F;
               float var24 = (var22[0] + var22[1] + var22[2]) / 3.0F;
               float var25 = var23 - var28;
               float var26 = var24 - var29;
               float var27 = (float)Math.hypot((double)var25, (double)var26);
               if (var27 < 0.001F) {
                  var25 = var3.nextFloat() - 0.5F;
                  var26 = -0.5F;
                  var27 = (float)Math.hypot((double)var25, -0.5);
               }

               this.t
                  .add(
                     new ColoredGeometryPipeline.Type_c(
                        var1,
                        var21,
                        var22,
                        var25 / var27,
                        var26 / var27,
                        var3.nextFloat() * 2.0F - 1.0F,
                        0.18F * Math.min(1.0F, var27 / var30),
                        var3.nextFloat()
                     )
                  );
            }
         }
      }
   }

   private static record Type_c(int window, float[] xs, float[] ys, float dirX, float dirY, float spin, float delay, float speed) {
      float[] atAsFloatArray(ColoredGeometryPipeline var1, double var2) {
         double var4 = Math.clamp((var2 - (double)this.delay) / Math.max(0.001, (double)(1.0F - this.delay)), 0.0, 1.0);
         ColoredGeometryPipeline.Type_i var6 = var1.V.get(this.window);
         float var7 = 0.0F;
         float var8 = 0.0F;
         int var9 = this.xs[2] == this.xs[3] && this.ys[2] == this.ys[3] ? 3 : 4;

         for (int var10 = 0; var10 < var9; var10++) {
            var7 += this.xs[var10] / (float)var9;
            var8 += this.ys[var10] / (float)var9;
         }

         float var12 = 0.0F;
         float var13 = 1.0F;
         float var16 = Math.max(var6.width, var6.height);
         String var33 = var1.h;
         float var11;
         double var14;
         float var37;
         switch (var33) {
            case "Shatter":
               float var37x = (float)(WeatherRenderer.Type_e.CUBIC_OUT.zAsDouble(var4) * (0.35 + 0.45 * (double)this.speed) * (double)var16 * 0.9);
               var37 = this.dirX * var37x;
               var11 = this.dirY * var37x + (float)(var4 * var4 * (double)var6.height * 0.9);
               var12 = (float)((double)this.spin * 2.4 * var4);
               var13 = (float)(1.0 - 0.25 * var4);
               var14 = 1.0 - ColoredGeometryPipeline.nAsDouble(0.55, 1.0, var4);
               break;
            case "Crumble":
               double var36 = var4 * var4;
               var37 = (float)((double)this.dirX * 1.2 * (double)(this.xs[3] - this.xs[0]) * var4);
               var11 = (float)(var36 * ((double)var6.height * 0.9 + 60.0));
               var12 = (float)((double)this.spin * 1.4 * var4);
               var14 = 1.0 - ColoredGeometryPipeline.nAsDouble(0.6, 1.0, var4);
               break;
            case "Slice":
               var37 = (float)((double)this.dirX * WeatherRenderer.Type_e.CUBIC_IN.zAsDouble(var4) * (double)var6.width * 0.75);
               var11 = (float)((double)(this.dirY * 4.0F) * var4);
               var14 = 1.0 - ColoredGeometryPipeline.nAsDouble(0.35, 1.0, var4);
               break;
            case "Implode":
               double var35 = WeatherRenderer.Type_e.CUBIC_IN.zAsDouble(var4);
               float var40 = var6.x + var6.width / 2.0F;
               float var42 = var6.y + var6.height / 2.0F;
               double var44 = var35 * 1.6;
               float var25 = (float)((double)(var7 - var40) * (1.0 - var35));
               float var26 = (float)((double)(var8 - var42) * (1.0 - var35));
               float var27 = (float)((double)var25 * Math.cos(var44) - (double)var26 * Math.sin(var44));
               float var28 = (float)((double)var25 * Math.sin(var44) + (double)var26 * Math.cos(var44));
               var37 = var40 + var27 - var7;
               var11 = var42 + var28 - var8;
               var12 = (float)(var44 + (double)this.spin * 0.6 * var35);
               var13 = (float)(1.0 - var35);
               var14 = 1.0 - ColoredGeometryPipeline.nAsDouble(0.5, 1.0, var4);
               break;
            default:
               float[] var19 = ColoredGeometryPipeline.fAsFloatArray(var2);
               float var20 = var6.x + var6.width / 2.0F;
               float var21 = var6.y + var6.height / 2.0F;
               float[] var22 = new float[9];

               for (int var23 = 0; var23 < 4; var23++) {
                  var22[var23 * 2] = var20 + (this.xs[var23] - var20) * var19[0];
                  var22[var23 * 2 + 1] = var21 + (this.ys[var23] - var21) * var19[1];
               }

               var22[8] = (float)(1.0 - ColoredGeometryPipeline.nAsDouble(0.85, 1.0, var2));
               return var22[8] <= 0.002F ? null : var22;
         }

         if (!(var14 <= 0.002) && !(var13 <= 0.002F)) {
            float var33_r2 = (float)Math.cos((double)var12);
            float var34 = (float)Math.sin((double)var12);
            float[] var38 = new float[9];

            for (int var39 = 0; var39 < 4; var39++) {
               float var41 = (this.xs[var39] - var7) * var13;
               float var43 = (this.ys[var39] - var8) * var13;
               var38[var39 * 2] = var7 + var37 + var41 * var33_r2 - var43 * var34;
               var38[var39 * 2 + 1] = var8 + var11 + var41 * var34 + var43 * var33_r2;
            }

            var38[8] = (float)var14;
            return var38;
         } else {
            return null;
         }
      }
   }

   static record Type_i(float x, float y, float width, float height, float radius) {
   }

   private static record Type_r(float[] data, float alpha, ColoredGeometryPipeline.Type_i window) {
   }

   private static record Type_u(
      TextureSetup textureSetup, List<ColoredGeometryPipeline.Type_r> quads, float guiScale, float textureWidth, float textureHeight, ScreenRect bounds
   ) implements SimpleGuiElementRenderState {
      public RenderPipeline pipeline() {
         return ColoredGeometryPipeline.b;
      }

      public ScreenRect scissorArea() {
         return null;
      }

      public void setupVertices(VertexConsumer vertices) {
         for (ColoredGeometryPipeline.Type_r var3 : this.quads) {
            ColoredGeometryPipeline.Type_i var4 = var3.window;
            float var5 = var4.x() + var4.width() / 2.0F;
            float var6 = var4.y() + var4.height() / 2.0F;
            int var7 = Math.clamp((long)Math.round(var3.alpha * 255.0F), 0, 255) << 24 | 16777215;

            for (int var8 = 0; var8 < 4; var8++) {
               float var9 = var3.data[var8 * 4 + 2];
               float var10 = var3.data[var8 * 4 + 3];
               float var11 = (var5 + var9) * this.guiScale / this.textureWidth;
               float var12 = 1.0F - (var6 + var10) * this.guiScale / this.textureHeight;
               vertices.vertex(var3.data[var8 * 4], var3.data[var8 * 4 + 1], var4.radius())
                  .texture(var11, var12)
                  .color(var7)
                  .overlay(fixed(var9), fixed(var10))
                  .light(fixed(var4.width() / 2.0F), fixed(var4.height() / 2.0F));
            }
         }
      }

      private static int fixed(float var0) {
         return Math.clamp((long)Math.round(var0 * 16.0F), -32768, 32767);
      }
   }
}
