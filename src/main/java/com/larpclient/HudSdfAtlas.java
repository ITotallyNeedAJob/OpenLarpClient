package com.larpclient;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.render.state.SimpleGuiElementRenderState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.TextureSetup;
import net.minecraft.util.Identifier;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fc;

public final class HudSdfAtlas {
   public static final Identifier r = HAsIdentifier("textures/gui/hud_sdf.png");
   public static final Identifier F = HAsIdentifier("textures/gui/hud_sdf.json");
   public static final float l = 0.72727275F;
   private static final int M = 8704;
   private static boolean k;
   private static boolean z;
   private static boolean G;
   private static HudSdfAtlas.Type_l L;

   private HudSdfAtlas() {
   }

   private static Identifier HAsIdentifier(String var0) {
      return Identifier.of("larp-client", var0);
   }

   public static void FAsVoid() {
      if (k && !z && !G) {
         try {
            WorldLabelLayout.DAsVoid(HudSdfAtlas.Type_t.J);
            if (MinecraftClient.getInstance().getResourceManager().getResource(r).isEmpty()) {
               throw new IllegalStateException("Missing " + r);
            }

            try (InputStreamReader var0 = new InputStreamReader(MinecraftClient.getInstance().getResourceManager().open(F), StandardCharsets.UTF_8)) {
               L = HudSdfAtlas.Type_l.parse(JsonParser.parseReader(var0).getAsJsonObject());
            }

            z = true;
            TextDrawing.XAsVoid();
            ClientLog.GAsVoid("HUD", "Distance-field HUD text and icons ready; native Inter remains the fallback");
         } catch (Exception var51) {
            G = true;
            L = null;
            ClientLog.lAsVoid("HUD", "Distance-field HUD text unavailable; native Inter text and icon textures until resource reload", var51);
         }
      }
   }

   public static void hAsVoid() {
      boolean var0 = z;
      G = false;
      z = false;
      L = null;
      if (var0) {
         TextDrawing.XAsVoid();
      }
   }

   private static HudSdfAtlas.Type_l PAsOa_Type_l() {
      k = true;
      return z && FontPreset.wAsV_Type_x() == TextDrawing.Type_x.MEDIUM ? L : null;
   }

   public static float jAsFloat(String var0, double var1) {
      HudSdfAtlas.Type_l var3 = PAsOa_Type_l();
      if (var3 == null) {
         return (float)FontPreset.cAsInt(var0, var1);
      } else {
         float var4 = 0.0F;
         int var5 = -1;
         int var6 = 0;

         while (var6 < var0.length()) {
            int var7 = var0.codePointAt(var6);
            HudSdfAtlas.Type_a var8 = var3.glyph(var7);
            if (var8 == null) {
               if (var5 < 0) {
                  var5 = var6;
               }
            } else {
               if (var5 >= 0) {
                  var4 += UAsFloat(var0.substring(var5, var6)) * (float)(var1 / 11.0);
                  var5 = -1;
               }

               var4 += var8.advance() * (float)var1;
            }

            var6 += Character.charCount(var7);
         }

         if (var5 >= 0) {
            var4 += UAsFloat(var0.substring(var5)) * (float)(var1 / 11.0);
         }

         return var4;
      }
   }

   private static float UAsFloat(String var0) {
      return (float)TextDrawing.qAsInt(var0, TextDrawing.Type_x.MEDIUM);
   }

   public static String bAsString(String var0, double var1, double var3) {
      if ((double)jAsFloat(var0, var3) <= var1) {
         return var0;
      } else {
         int var5 = var0.length();

         while (var5 > 0 && (double)jAsFloat(var0.substring(0, var5) + "…", var3) > var1) {
            var5 = var0.offsetByCodePoints(var5, -1);
         }

         return var5 == 0 ? "" : var0.substring(0, var5) + "…";
      }
   }

   public static void DAsVoid(DrawContext var0, String var1, double var2, double var4, double var6, int var8) {
      var8 = QueueGroup.iAsInt(var8);
      final int var8f = var8;
      if (var8 >>> 24 >= 4 && !var1.isEmpty()) {
         HudSdfAtlas.Type_l var9 = PAsOa_Type_l();
         if (var9 == null) {
            dAsVoid(var0, var1, var2, var4, var6, var8);
         } else {
            float[] var11 = new float[var1.length() * 16];
            int var12 = UAsInt(var9, var1, (float)var2, (float)var4, (float)var6, var11, (var6xx, var7) -> {
               dAsVoid(var0, var6xx, (double)var7, var4, var6, var8f);
               return UAsFloat(var6xx) * (float)(var6 / 11.0);
            });
            CAsVoid(var0, var11, var12, var8);
         }
      }
   }

   public static int UAsInt(HudSdfAtlas.Type_l var0, String var1, float var2, float var3, float var4, float[] var5, HudSdfAtlas.Type_y var6) {
      int var7 = 0;
      int var8 = -1;
      float var9 = var2;
      float var10 = var3 + var4 * 0.72727275F;
      int var11 = 0;

      while (var11 < var1.length()) {
         int var12 = var1.codePointAt(var11);
         HudSdfAtlas.Type_a var13 = var0.glyph(var12);
         if (var13 == null) {
            if (var8 < 0) {
               var8 = var11;
            }
         } else {
            if (var8 >= 0) {
               var9 += var6.NAsFloat(var1.substring(var8, var11), var9);
               var8 = -1;
            }

            if (var13.visible()) {
               var7 = jAsInt(
                  var5, var7, var9 + var13.left() * var4, var10 - var13.top() * var4, var9 + var13.right() * var4, var10 - var13.bottom() * var4, var13
               );
            }

            var9 += var13.advance() * var4;
         }

         var11 += Character.charCount(var12);
      }

      if (var8 >= 0) {
         var6.NAsFloat(var1.substring(var8), var9);
      }

      return var7;
   }

   private static void dAsVoid(DrawContext var0, String var1, double var2, double var4, double var6, int var8) {
      float var9 = (float)(var6 / FontPreset.rAsDouble());
      var0.getMatrices().pushMatrix();
      var0.getMatrices().translate((float)var2, (float)var4).scale(var9, var9);
      TextDrawing.oAsVoid(var0, var1, 0, 0, var8, FontPreset.wAsV_Type_x());
      var0.getMatrices().popMatrix();
   }

   public static boolean NAsBoolean(DrawContext var0, String var1, double var2, double var4, double var6, int var8) {
      HudSdfAtlas.Type_l var9 = PAsOa_Type_l();
      HudSdfAtlas.Type_a var10 = var9 == null ? null : var9.icons().get(var1);
      if (var10 == null) {
         return false;
      } else {
         var8 = QueueGroup.iAsInt(var8);
         if (var8 >>> 24 < 4) {
            return true;
         } else {
            float[] var11 = new float[16];
            CAsVoid(var0, var11, QAsInt(var10, (float)var2, (float)var4, (float)var6, var11), var8);
            return true;
         }
      }
   }

   public static int QAsInt(HudSdfAtlas.Type_a var0, float var1, float var2, float var3, float[] var4) {
      return jAsInt(var4, 0, var1 + var0.left() * var3, var2 + var0.top() * var3, var1 + var0.right() * var3, var2 + var0.bottom() * var3, var0);
   }

   private static int jAsInt(float[] var0, int var1, float var2, float var3, float var4, float var5, HudSdfAtlas.Type_a var6) {
      int var7 = var1 * 16;
      var0[var7] = var2;
      var0[var7 + 1] = var3;
      var0[var7 + 2] = var6.u0();
      var0[var7 + 3] = var6.v0();
      var0[var7 + 4] = var2;
      var0[var7 + 5] = var5;
      var0[var7 + 6] = var6.u0();
      var0[var7 + 7] = var6.v1();
      var0[var7 + 8] = var4;
      var0[var7 + 9] = var5;
      var0[var7 + 10] = var6.u1();
      var0[var7 + 11] = var6.v1();
      var0[var7 + 12] = var4;
      var0[var7 + 13] = var3;
      var0[var7 + 14] = var6.u1();
      var0[var7 + 15] = var6.v0();
      return var1 + 1;
   }

   private static void CAsVoid(DrawContext var0, float[] var1, int var2, int var3) {
      if (var2 != 0) {
         AbstractTexture var4 = MinecraftClient.getInstance().getTextureManager().getTexture(r);
         TextureSetup var5 = TextureSetup.of(var4.getGlTextureView(), RenderSystem.getSamplerCache().get(FilterMode.LINEAR));
         var0.state.addSimpleElement(new HudSdfAtlas.Type_v(var0.getMatrices(), Arrays.copyOf(var1, var2 * 16), var3, var5, var0.scissorStack.peekLast()));
      }
   }

   public static record Type_a(float advance, float left, float top, float right, float bottom, float u0, float v0, float u1, float v1) {
      boolean visible() {
         return this.u1 > this.u0;
      }
   }

   public static record Type_l(HudSdfAtlas.Type_a[] direct, Map<Integer, HudSdfAtlas.Type_a> extended, Map<String, HudSdfAtlas.Type_a> icons) {
      public HudSdfAtlas.Type_a glyph(int var1) {
         return var1 >= 0 && var1 < this.direct.length ? this.direct[var1] : this.extended.get(var1);
      }

      public static HudSdfAtlas.Type_l parse(JsonObject var0) {
         JsonObject var1 = var0.getAsJsonObject("atlas");
         float var2 = var1.get("width").getAsFloat();
         float var3 = var1.get("height").getAsFloat();
         HudSdfAtlas.Type_a[] var4 = new HudSdfAtlas.Type_a[8704];
         HashMap var5 = new HashMap();

         for (JsonElement var7 : var0.getAsJsonArray("glyphs")) {
            HudSdfAtlas.Type_a var8 = glyph(var7.getAsJsonObject(), var2, var3);
            int var9 = var7.getAsJsonObject().get("unicode").getAsInt();
            if (var9 < 8704) {
               var4[var9] = var8;
            } else {
               var5.put(var9, var8);
            }
         }

         HashMap var10 = new HashMap();

         for (JsonElement var12 : var0.getAsJsonArray("icons")) {
            var10.put(var12.getAsJsonObject().get("name").getAsString(), glyph(var12.getAsJsonObject(), var2, var3));
         }

         return new HudSdfAtlas.Type_l(var4, Map.copyOf(var5), Map.copyOf(var10));
      }

      private static HudSdfAtlas.Type_a glyph(JsonObject var0, float var1, float var2) {
         float var3 = var0.has("advance") ? var0.get("advance").getAsFloat() : 0.0F;
         if (!var0.has("plane")) {
            return new HudSdfAtlas.Type_a(var3, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
         } else {
            JsonArray var4 = var0.getAsJsonArray("plane");
            JsonArray var5 = var0.getAsJsonArray("atlas");
            float var6 = var5.get(0).getAsFloat();
            float var7 = var5.get(1).getAsFloat();
            return new HudSdfAtlas.Type_a(
               var3,
               var4.get(0).getAsFloat(),
               var4.get(1).getAsFloat(),
               var4.get(2).getAsFloat(),
               var4.get(3).getAsFloat(),
               var6 / var1,
               var7 / var2,
               (var6 + var5.get(2).getAsFloat()) / var1,
               (var7 + var5.get(3).getAsFloat()) / var2
            );
         }
      }
   }

   private static final class Type_t {
      static final RenderPipeline J = RenderPipeline.builder(new Snippet[]{RenderPipelines.POSITION_TEX_COLOR_SNIPPET})
         .withLocation(HudSdfAtlas.HAsIdentifier("gui_sdf"))
         .withFragmentShader(HudSdfAtlas.HAsIdentifier("gui/sdf"))
         .build();

      private Type_t() {
      }
   }

   public static record Type_v(Matrix3x2fc pose, float[] vertices, int color, TextureSetup textureSetup, ScreenRect scissorArea, ScreenRect bounds)
      implements SimpleGuiElementRenderState {
      public Type_v(Matrix3x2fc var1, float[] var2, int var3, TextureSetup var4, ScreenRect var5) {
         this(new Matrix3x2f(var1), var2, var3, var4, var5, bounds(var1, var2, var5));
      }

      public RenderPipeline pipeline() {
         return HudSdfAtlas.Type_t.J;
      }

      public void setupVertices(VertexConsumer vertices) {
         for (int var2 = 0; var2 < this.vertices.length; var2 += 4) {
            float var3 = this.vertices[var2];
            float var4 = this.vertices[var2 + 1];
            vertices.vertex(
                  this.pose.m00() * var3 + this.pose.m10() * var4 + this.pose.m20(), this.pose.m01() * var3 + this.pose.m11() * var4 + this.pose.m21(), 0.0F
               )
               .texture(this.vertices[var2 + 2], this.vertices[var2 + 3])
               .color(this.color);
         }
      }

      static ScreenRect bounds(Matrix3x2fc var0, float[] var1, ScreenRect var2) {
         float var3 = Float.MAX_VALUE;
         float var4 = Float.MAX_VALUE;
         float var5 = -Float.MAX_VALUE;
         float var6 = -Float.MAX_VALUE;

         for (int var7 = 0; var7 < var1.length; var7 += 4) {
            var3 = Math.min(var3, var1[var7]);
            var5 = Math.max(var5, var1[var7]);
            var4 = Math.min(var4, var1[var7 + 1]);
            var6 = Math.max(var6, var1[var7 + 1]);
         }

         int var10 = (int)Math.floor((double)var3);
         int var8 = (int)Math.floor((double)var4);
         ScreenRect var9 = new ScreenRect(var10, var8, (int)Math.ceil((double)var5) - var10, (int)Math.ceil((double)var6) - var8).transformEachVertex(var0);
         return var2 == null ? var9 : var2.intersection(var9);
      }
   }

   public interface Type_y {
      float NAsFloat(String var1, float var2);
   }
}
