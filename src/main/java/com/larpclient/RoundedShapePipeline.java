package com.larpclient;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.render.state.SimpleGuiElementRenderState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.TextureSetup;
import net.minecraft.util.Identifier;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fc;

public final class RoundedShapePipeline {
   private static boolean A;
   private static boolean g;
   private static boolean z;
   private static boolean S;
   private static boolean p;
   private static boolean R;
   public static final int F = 2;
   private static final VertexFormat K = VertexFormat.builder()
      .add("Position", VertexFormatElement.POSITION)
      .add("UV0", VertexFormatElement.UV0)
      .add("Color", VertexFormatElement.COLOR)
      .add("UV1", VertexFormatElement.UV1)
      .build();
   private static final VertexFormat q = VertexFormat.builder()
      .add("Position", VertexFormatElement.POSITION)
      .add("Color", VertexFormatElement.COLOR)
      .add("UV0", VertexFormatElement.UV0)
      .add("UV2", VertexFormatElement.UV2)
      .add("UV1", VertexFormatElement.UV1)
      .build();
   private static final VertexFormat L = VertexFormat.builder()
      .add("Position", VertexFormatElement.POSITION)
      .add("UV0", VertexFormatElement.UV0)
      .add("Color", VertexFormatElement.COLOR)
      .add("UV1", VertexFormatElement.UV1)
      .add("UV2", VertexFormatElement.UV2)
      .build();
   private static boolean G;
   private static boolean C;
   private static boolean w;
   public static final float f = 64.0F;
   private static boolean Q;
   private static boolean W;
   private static boolean N;
   private static boolean X;
   private static boolean c;

   private RoundedShapePipeline() {
   }

   public static boolean PAsBoolean(RenderPipeline var0) {
      return var0 == RoundedShapePipeline.Type_j.q
         || var0 == RoundedShapePipeline.Type_j.r
         || var0 == RoundedShapePipeline.Type_j.G
         || var0 == RoundedShapePipeline.Type_j.V
         || var0 == RoundedShapePipeline.Type_j.m
         || var0 == RoundedShapePipeline.Type_j.O;
   }

   public static RenderPipeline RAsRenderPipeline(RenderPipeline var0) {
      if (PAsBoolean(var0) && var0 != RoundedShapePipeline.Type_j.O) {
         Q = true;
         if (var0 == RoundedShapePipeline.Type_j.m) {
            return X ? RoundedShapePipeline.Type_j.K : null;
         } else if (!W) {
            return null;
         } else {
            return var0 == RoundedShapePipeline.Type_j.q
               ? RoundedShapePipeline.Type_j.j
               : (
                  var0 == RoundedShapePipeline.Type_j.r
                     ? RoundedShapePipeline.Type_j.v
                     : (var0 == RoundedShapePipeline.Type_j.G ? RoundedShapePipeline.Type_j.N : RoundedShapePipeline.Type_j.S)
               );
         }
      } else {
         return null;
      }
   }

   public static void EAsVoid() {
      if (A && !g && !z) {
         try {
            WorldLabelLayout.DAsVoid(RoundedShapePipeline.Type_j.q);
            WorldLabelLayout.DAsVoid(RoundedShapePipeline.Type_j.r);
            WorldLabelLayout.DAsVoid(RoundedShapePipeline.Type_j.G);
            WorldLabelLayout.DAsVoid(RoundedShapePipeline.Type_j.V);
            g = true;
            ClientLog.GAsVoid("UI", "Smooth GUI/HUD fills, one-pixel rims, focus rings and soft glows ready; native batching and clipping retained");
         } catch (RuntimeException var5) {
            z = true;
            ClientLog.lAsVoid("UI", "Rounded shape pipeline unavailable; smooth texture fallback until resource reload", var5);
         }
      }

      if (S && g && !p && !R) {
         try {
            WorldLabelLayout.DAsVoid(RoundedShapePipeline.Type_j.m);
            p = true;
            ClientLog.GAsVoid("UI", "Selected-card contour light ready");
         } catch (RuntimeException var4) {
            R = true;
            ClientLog.lAsVoid("UI", "Contour light pipeline unavailable; the selected card keeps its accent contour until resource reload", var4);
         }
      }

      if (G && g && !C && !w) {
         try {
            WorldLabelLayout.DAsVoid(RoundedShapePipeline.Type_j.O);
            C = true;
            ClientLog.GAsVoid("UI", "Rounded click ripples ready");
         } catch (RuntimeException var3) {
            w = true;
            ClientLog.lAsVoid("UI", "Ripple pipeline unavailable; module cards show no click ripple until resource reload", var3);
         }
      }

      if (Q && g && !W && !N) {
         try {
            WorldLabelLayout.DAsVoid(RoundedShapePipeline.Type_j.j);
            WorldLabelLayout.DAsVoid(RoundedShapePipeline.Type_j.v);
            WorldLabelLayout.DAsVoid(RoundedShapePipeline.Type_j.N);
            WorldLabelLayout.DAsVoid(RoundedShapePipeline.Type_j.S);
            W = true;
            ClientLog.GAsVoid("UI", "World Menu rounded shapes ready: split and clipped exactly on the panel");
         } catch (RuntimeException var2) {
            N = true;
            ClientLog.lAsVoid("UI", "World Menu shape pipeline unavailable; panel shapes keep their four corners until resource reload", var2);
         }
      }

      if (Q && W && p && !X && !c) {
         try {
            WorldLabelLayout.DAsVoid(RoundedShapePipeline.Type_j.K);
            X = true;
         } catch (RuntimeException var1) {
            c = true;
            ClientLog.lAsVoid("UI", "World Menu contour light pipeline unavailable; it keeps its four corners until resource reload", var1);
         }
      }
   }

   public static void qAsVoid() {
      w = false;
      C = false;
      R = false;
      p = false;
      z = false;
      g = false;
      c = false;
      X = false;
      N = false;
      W = false;
   }

   public static void nAsVoid(DrawContext var0, float var1, float var2, float var3, float var4, float var5, int var6) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         float var7 = Math.clamp(var5, 0.0F, Math.min(var3, var4) * 0.5F);
         if (var7 == 0.0F && var1 == (float)((int)var1) && var2 == (float)((int)var2) && var3 == (float)((int)var3) && var4 == (float)((int)var4)) {
            GuiTextureDrawing.CAsVoid(var0, (int)var1, (int)var2, (int)var3, (int)var4, var6);
         } else {
            A = true;
            if (!g) {
               GuiDrawing.KAsVoid(var0, Math.round(var1), Math.round(var2), Math.round(var3), Math.round(var4), Math.max(1, Math.round(var7)), var6);
            } else {
               int var8 = QueueGroup.iAsInt(var6);
               if (var8 >>> 24 != 0) {
                  var0.state
                     .addSimpleElement(
                        new RoundedShapePipeline.Type_n(
                           var0.getMatrices(), var1, var2, var3, var4, mAsFloat(var7, var3, var4), var8, var0.scissorStack.peekLast()
                        )
                     );
               }
            }
         }
      }
   }

   private static float mAsFloat(float var0, float var1, float var2) {
      return Math.min(Math.max(var0, Math.max(0.5F, Math.max(var1, var2) / 1000.0F)), Math.min(var1, var2) * 0.5F);
   }

   public static void tAsVoid(DrawContext var0, float var1, float var2, float var3, float var4, float var5, int var6, int var7) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         if (var6 == var7) {
            nAsVoid(var0, var1, var2, var3, var4, var5, var6);
         } else {
            float var8 = Math.clamp(var5, 0.0F, Math.min(var3, var4) * 0.5F);
            if (var8 == 0.0F && var1 == (float)((int)var1) && var2 == (float)((int)var2) && var3 == (float)((int)var3) && var4 == (float)((int)var4)) {
               GuiTextureDrawing.mAsVoid(var0, (int)var1, (int)var2, (int)var3, (int)var4, var6, var7);
            } else {
               var8 = mAsFloat(var8, var3, var4);
               A = true;
               if (!g) {
                  GuiDrawing.KAsVoid(
                     var0, Math.round(var1), Math.round(var2), Math.round(var3), Math.round(var4), Math.max(1, Math.round(var8)), ColorUtil.xAsInt(var6, var7, 0.5)
                  );
               } else {
                  var6 = QueueGroup.iAsInt(var6);
                  var7 = QueueGroup.iAsInt(var7);
                  if (var6 >>> 24 != 0 || var7 >>> 24 != 0) {
                     var0.state
                        .addSimpleElement(
                           new RoundedShapePipeline.Type_n(var0.getMatrices(), var1, var2, var3, var4, var8, var6, var7, var0.scissorStack.peekLast())
                        );
                  }
               }
            }
         }
      }
   }

   public static void gAsVoid(DrawContext var0, float var1, float var2, float var3, float var4, float var5, int var6, int var7) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         var0.getMatrices().pushMatrix();

         try {
            var0.getMatrices().translate(var1, var2 + var4).rotate((float) (-Math.PI / 2));
            tAsVoid(var0, 0.0F, 0.0F, var4, var3, var5, var6, var7);
         } finally {
            var0.getMatrices().popMatrix();
         }
      }
   }

   public static void SAsVoid(DrawContext var0, float var1, float var2, float var3, float var4, float var5, int var6, int var7) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         float var8 = Math.clamp(var5, 0.5F, Math.max(0.5F, Math.min(var3, var4) * 0.5F));
         A = true;
         if (!g) {
            GuiDrawing.WAsVoid(var0, Math.round(var1), Math.round(var2), Math.round(var3), Math.round(var4), Math.max(1, Math.round(var8)), var6);
         } else {
            var6 = QueueGroup.iAsInt(var6);
            var7 = QueueGroup.iAsInt(var7);
            if (var6 >>> 24 != 0 || var7 >>> 24 != 0) {
               var0.state
                  .addSimpleElement(
                     new RoundedShapePipeline.Type_n(var0.getMatrices(), var1, var2, var3, var4, var8, var6, var7, true, var0.scissorStack.peekLast())
                  );
            }
         }
      }
   }

   public static void cAsVoid(DrawContext var0, float var1, float var2, float var3, float var4, float var5, int var6) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         float var7 = Math.clamp(var5, 0.5F, Math.max(0.5F, Math.min(var3, var4) * 0.5F));
         A = true;
         if (!g) {
            GuiDrawing.WAsVoid(var0, Math.round(var1), Math.round(var2), Math.round(var3), Math.round(var4), Math.max(1, Math.round(var7)), var6);
         } else {
            var6 = QueueGroup.iAsInt(var6);
            if (var6 >>> 24 != 0) {
               var0.state
                  .addSimpleElement(
                     new RoundedShapePipeline.Type_n(
                        new Matrix3x2f(var0.getMatrices()),
                        var1,
                        var2,
                        var3,
                        var4,
                        var7,
                        var6,
                        var6,
                        true,
                        true,
                        var0.scissorStack.peekLast(),
                        RoundedShapePipeline.Type_n.bounds(var0.getMatrices(), var1, var2, var3, var4, var0.scissorStack.peekLast())
                     )
                  );
            }
         }
      }
   }

   public static void CAsVoid(DrawContext var0, float var1, float var2, float var3, float var4, float var5, float var6, int var7, int var8) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F) && !(var6 <= 0.0F)) {
         float var9 = Math.clamp(var5, 0.5F, Math.max(0.5F, Math.min(var3, var4) * 0.5F));
         A = true;
         if (g) {
            var7 = QueueGroup.iAsInt(var7);
            var8 = QueueGroup.iAsInt(var8);
            if (var7 >>> 24 != 0 || var8 >>> 24 != 0) {
               float var10 = var6 + 1.0F;
               var0.state
                  .addSimpleElement(
                     new RoundedShapePipeline.Type_n(
                        new Matrix3x2f(var0.getMatrices()),
                        var1,
                        var2,
                        var3,
                        var4,
                        var9,
                        var7,
                        var8,
                        false,
                        false,
                        var10,
                        var0.scissorStack.peekLast(),
                        RoundedShapePipeline.Type_n.bounds(var0.getMatrices(), var1, var2, var3, var4, var10, var0.scissorStack.peekLast())
                     )
                  );
            }
         }
      }
   }

   public static void tAsVoid(DrawContext var0, float var1, float var2, float var3, float var4, float var5, float var6, double var7, int var9) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         float var10 = Math.clamp(var5, 0.5F, Math.max(0.5F, Math.min(var3, var4) * 0.5F));
         A = true;
         S = true;
         if (p) {
            var9 = QueueGroup.iAsInt(var9);
            if (var9 >>> 24 != 0) {
               float var11 = Math.max(0.0F, var6) + 1.0F;
               var0.state
                  .addSimpleElement(
                     new RoundedShapePipeline.Type_n(
                        new Matrix3x2f(var0.getMatrices()),
                        var1,
                        var2,
                        var3,
                        var4,
                        var10,
                        var9,
                        var9,
                        false,
                        false,
                        var11,
                        (float)(var7 - Math.floor(var7)),
                        var0.scissorStack.peekLast(),
                        RoundedShapePipeline.Type_n.bounds(var0.getMatrices(), var1, var2, var3, var4, var11, var0.scissorStack.peekLast())
                     )
                  );
            }
         }
      }
   }

   public static void pAsVoid(DrawContext var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, int var9) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F) && !(var8 <= 0.0F)) {
         float var10 = mAsFloat(Math.clamp(var5, 0.0F, Math.min(var3, var4) * 0.5F), var3, var4);
         A = true;
         G = true;
         if (C) {
            var9 = QueueGroup.iAsInt(var9);
            if (var9 >>> 24 != 0) {
               var0.state
                  .addSimpleElement(
                     new RoundedShapePipeline.Type_d(
                        new Matrix3x2f(var0.getMatrices()),
                        var1,
                        var2,
                        var3,
                        var4,
                        var10,
                        (var6 - var1 - var3 / 2.0F) / var10,
                        (var7 - var2 - var4 / 2.0F) / var10,
                        var8 / var10,
                        var9,
                        var0.scissorStack.peekLast(),
                        RoundedShapePipeline.Type_n.bounds(var0.getMatrices(), var1, var2, var3, var4, var0.scissorStack.peekLast())
                     )
                  );
            }
         }
      }
   }

   public static record Type_d(
      Matrix3x2fc pose,
      float x,
      float y,
      float width,
      float height,
      float radius,
      float circleX,
      float circleY,
      float circle,
      int color,
      ScreenRect scissorArea,
      ScreenRect bounds
   ) implements SimpleGuiElementRenderState {
      public RenderPipeline pipeline() {
         return RoundedShapePipeline.Type_j.O;
      }

      public TextureSetup textureSetup() {
         return TextureSetup.empty();
      }

      public void setupVertices(VertexConsumer vertices) {
         float var3 = (this.width * 0.5F + 1.0F) / this.radius;
         float var4 = (this.height * 0.5F + 1.0F) / this.radius;
         this.vertex(vertices, this.x - 1.0F, this.y - 1.0F, -var3, -var4);
         this.vertex(vertices, this.x - 1.0F, this.y + this.height + 1.0F, -var3, var4);
         this.vertex(vertices, this.x + this.width + 1.0F, this.y + this.height + 1.0F, var3, var4);
         this.vertex(vertices, this.x + this.width + 1.0F, this.y - 1.0F, var3, -var4);
      }

      private void vertex(VertexConsumer var1, float var2, float var3, float var4, float var5) {
         var1.vertex(
               this.pose.m00() * var2 + this.pose.m10() * var3 + this.pose.m20(),
               this.pose.m01() * var2 + this.pose.m11() * var3 + this.pose.m21(),
               1.0F / this.radius
            )
            .texture(var4, var5)
            .color(this.color)
            .overlay(fixed(this.circleX), fixed(this.circleY))
            .light(fixed(this.circle), 0);
      }

      private static int fixed(float var0) {
         return Math.clamp((long)Math.round(var0 * 64.0F), -32768, 32767);
      }
   }

   private static final class Type_j {
      static final RenderPipeline q = iAsRenderPipeline(0, false);
      static final RenderPipeline r = iAsRenderPipeline(1, false);
      static final RenderPipeline G = iAsRenderPipeline(2, false);
      static final RenderPipeline V = iAsRenderPipeline(-1, false);
      static final RenderPipeline m = fAsRenderPipeline(false);
      static final RenderPipeline O = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_SNIPPET})
         .withLocation(Identifier.of("larp-client", "gui_rounded_ripple"))
         .withVertexShader(Identifier.of("larp-client", "gui/rounded"))
         .withFragmentShader(Identifier.of("larp-client", "gui/rounded"))
         .withVertexFormat(RoundedShapePipeline.L, DrawMode.QUADS)
         .withShaderDefine("LARP_UI_RIPPLE")
         .build();
      static final RenderPipeline j = iAsRenderPipeline(0, true);
      static final RenderPipeline v = iAsRenderPipeline(1, true);
      static final RenderPipeline N = iAsRenderPipeline(2, true);
      static final RenderPipeline S = iAsRenderPipeline(-1, true);
      static final RenderPipeline K = fAsRenderPipeline(true);

      private Type_j() {
      }

      private static RenderPipeline fAsRenderPipeline(boolean var0) {
         Builder var1 = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_SNIPPET})
            .withLocation(Identifier.of("larp-client", var0 ? "gui_rounded_sweep_sized" : "gui_rounded_sweep"))
            .withVertexShader(Identifier.of("larp-client", "gui/rounded"))
            .withFragmentShader(Identifier.of("larp-client", "gui/rounded"))
            .withVertexFormat(var0 ? RoundedShapePipeline.q : VertexFormats.POSITION_COLOR_TEXTURE_LIGHT, DrawMode.QUADS)
            .withShaderDefine("LARP_UI_SWEEP");
         if (var0) {
            var1.withShaderDefine("LARP_UI_SIZED");
         }

         return var1.build();
      }

      private static RenderPipeline iAsRenderPipeline(int var0, boolean var1) {
         String var2 = var0 < 0 ? "gui_rounded_glow" : (var0 == 0 ? "gui_rounded" : (var0 == 1 ? "gui_rounded_rim" : "gui_rounded_ring"));
         Builder var3 = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_SNIPPET})
            .withLocation(Identifier.of("larp-client", var1 ? var2 + "_sized" : var2))
            .withVertexShader(Identifier.of("larp-client", "gui/rounded"))
            .withFragmentShader(Identifier.of("larp-client", "gui/rounded"))
            .withVertexFormat(var1 ? RoundedShapePipeline.K : VertexFormats.POSITION_TEXTURE_COLOR, DrawMode.QUADS);
         if (var0 < 0) {
            var3.withShaderDefine("LARP_UI_GLOW");
         }

         if (var0 > 0) {
            var3.withShaderDefine("LARP_UI_RIM");
         }

         if (var0 > 1) {
            var3.withShaderDefine("LARP_UI_RIM_WIDTH", var0);
         }

         if (var1) {
            var3.withShaderDefine("LARP_UI_SIZED");
         }

         return var3.build();
      }
   }

   public static record Type_n(
      Matrix3x2fc pose,
      float x,
      float y,
      float width,
      float height,
      float radius,
      int color,
      int bottomColor,
      boolean rim,
      boolean ring,
      float padding,
      float sweep,
      ScreenRect scissorArea,
      ScreenRect bounds
   ) implements SimpleGuiElementRenderState {
      public Type_n(
         Matrix3x2fc var1,
         float var2,
         float var3,
         float var4,
         float var5,
         float var6,
         int var7,
         int var8,
         boolean var9,
         boolean var10,
         float var11,
         ScreenRect var12,
         ScreenRect var13
      ) {
         this(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, -1.0F, var12, var13);
      }

      public Type_n(
         Matrix3x2fc var1,
         float var2,
         float var3,
         float var4,
         float var5,
         float var6,
         int var7,
         int var8,
         boolean var9,
         boolean var10,
         ScreenRect var11,
         ScreenRect var12
      ) {
         this(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, 1.0F, var11, var12);
      }

      public Type_n(Matrix3x2fc var1, float var2, float var3, float var4, float var5, float var6, int var7, ScreenRect var8) {
         this(var1, var2, var3, var4, var5, var6, var7, var7, var8);
      }

      public Type_n(Matrix3x2fc var1, float var2, float var3, float var4, float var5, float var6, int var7, int var8, ScreenRect var9) {
         this(var1, var2, var3, var4, var5, var6, var7, var8, false, var9);
      }

      public Type_n(Matrix3x2fc var1, float var2, float var3, float var4, float var5, float var6, int var7, int var8, boolean var9, ScreenRect var10) {
         this(new Matrix3x2f(var1), var2, var3, var4, var5, var6, var7, var8, var9, false, var10, bounds(var1, var2, var3, var4, var5, var10));
      }

      static ScreenRect bounds(Matrix3x2fc var0, float var1, float var2, float var3, float var4, ScreenRect var5) {
         return bounds(var0, var1, var2, var3, var4, 1.0F, var5);
      }

      static ScreenRect bounds(Matrix3x2fc var0, float var1, float var2, float var3, float var4, float var5, ScreenRect var6) {
         int var7 = (int)Math.floor((double)(var1 - var5));
         int var8 = (int)Math.floor((double)(var2 - var5));
         int var9 = (int)Math.ceil((double)(var1 + var3 + var5));
         int var10 = (int)Math.ceil((double)(var2 + var4 + var5));
         ScreenRect var11 = new ScreenRect(var7, var8, var9 - var7, var10 - var8).transformEachVertex(var0);
         return var6 == null ? var11 : var6.intersection(var11);
      }

      public boolean glow() {
         return this.padding > 1.0F;
      }

      public boolean contourLight() {
         return this.sweep >= 0.0F;
      }

      public RenderPipeline pipeline() {
         return this.contourLight()
            ? RoundedShapePipeline.Type_j.m
            : (
               this.glow()
                  ? RoundedShapePipeline.Type_j.V
                  : (this.ring ? RoundedShapePipeline.Type_j.G : (this.rim ? RoundedShapePipeline.Type_j.r : RoundedShapePipeline.Type_j.q))
            );
      }

      public TextureSetup textureSetup() {
         return TextureSetup.empty();
      }

      public void setupVertices(VertexConsumer vertices) {
         float var2 = this.padding;
         float var3 = (this.width * 0.5F + var2) / this.radius;
         float var4 = (this.height * 0.5F + var2) / this.radius;
         this.vertex(vertices, this.x - var2, this.y - var2, -var3, -var4, this.color);
         this.vertex(vertices, this.x - var2, this.y + this.height + var2, -var3, var4, this.bottomColor);
         this.vertex(vertices, this.x + this.width + var2, this.y + this.height + var2, var3, var4, this.bottomColor);
         this.vertex(vertices, this.x + this.width + var2, this.y - var2, var3, -var4, this.color);
      }

      private void vertex(VertexConsumer var1, float var2, float var3, float var4, float var5, int var6) {
         VertexConsumer var7 = var1.vertex(
               this.pose.m00() * var2 + this.pose.m10() * var3 + this.pose.m20(),
               this.pose.m01() * var2 + this.pose.m11() * var3 + this.pose.m21(),
               this.padding / this.radius
            )
            .texture(var4, var5)
            .color(var6);
         if (this.contourLight()) {
            var7.light(Math.round(this.sweep * 32768.0F) & 32767, this.glow() ? 1 : 0);
         }
      }
   }
}
