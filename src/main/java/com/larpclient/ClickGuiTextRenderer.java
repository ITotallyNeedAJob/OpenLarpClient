package com.larpclient;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.textures.TextureFormat;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.GpuSampler;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.render.state.SimpleGuiElementRenderState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.TextureSetup;
import net.minecraft.client.texture.NativeImage.Format;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.joml.Matrix3x2fStack;
import org.joml.Matrix3x2fc;
import org.lwjgl.CLongBuffer;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.freetype.FT_Bitmap;
import org.lwjgl.util.freetype.FT_Face;
import org.lwjgl.util.freetype.FT_GlyphSlot;
import org.lwjgl.util.freetype.FT_Vector;
import org.lwjgl.util.freetype.FreeType;

public final class ClickGuiTextRenderer {
   private static final int O = 65544;
   private static final int C = 512;
   private static final int Z = 6;
   private static final int e = 4;
   private static long L;
   private static final List<ByteBuffer> F = new ArrayList<>();
   private static final Map<TextDrawing.Type_x, ClickGuiTextRenderer.Type_g> b = new EnumMap<>(TextDrawing.Type_x.class);
   private static final Map<Long, ClickGuiTextRenderer.Type_l> K = new HashMap<>();
   private static final Map<Long, Float> y = new HashMap<>();
   private static final Map<Long, Boolean> P = new HashMap<>();
   private static final List<GpuTexture> I = new ArrayList<>();
   private static final List<GpuTextureView> V = new ArrayList<>();
   private static int H;
   private static int M;
   private static int J;
   private static final Map<Long, Integer> D = new HashMap<>();
   private static int q = -1;
   private static boolean a;
   private static boolean U;

   private ClickGuiTextRenderer() {
   }

   static boolean QAsBoolean(TextDrawing.Type_x var0, String var1) {
      if (var0.name().startsWith("CLICK_") && MinecraftClient.getInstance() != null) {
         if (XAsBoolean() && !var1.isEmpty() && var1.indexOf(167) < 0) {
            ClickGuiTextRenderer.Type_g var2 = b.get(var0);
            if (var2 == null) {
               return false;
            } else {
               int var3 = 0;

               while (var3 < var1.length()) {
                  int var4 = var1.codePointAt(var3);
                  var3 += Character.charCount(var4);
                  if (nAsInt(var2, var4) == 0) {
                     return false;
                  }
               }

               return true;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   static int TAsInt(String var0, TextDrawing.Type_x var1) {
      ClickGuiTextRenderer.Type_g var2 = b.get(var1);
      float var3 = 0.0F;
      int var4 = 0;

      while (var4 < var0.length()) {
         int var5 = var0.codePointAt(var4);
         var4 += Character.charCount(var5);
         var3 += IAsFloat(var2, var5);
      }

      return (int)Math.ceil((double)var3);
   }

   static boolean kAsBoolean(DrawContext var0, String var1, int var2, int var3, int var4, TextDrawing.Type_x var5) {
      if (!QAsBoolean(var5, var1)) {
         return false;
      } else {
         try {
            return OAsBoolean(var0, var1, var2, var3, var4, b.get(var5));
         } catch (RuntimeException var7) {
            nAsVoid("Exact-size ClickGUI text failed; native text until resource reload", var7);
            return false;
         }
      }
   }

   private static boolean OAsBoolean(DrawContext var0, String var1, int var2, int var3, int var4, ClickGuiTextRenderer.Type_g var5) {
      if (U) {
         GAsVoid();
      }

      Matrix3x2fStack var6 = var0.getMatrices();
      float var7 = (float)MinecraftClient.getInstance().getWindow().getScaleFactor();
      int var8 = var6.m01() == 0.0F && var6.m10() == 0.0F && var6.m00() > 0.0F && Math.abs(var6.m00() - var6.m11()) < 1.0E-4F && !XTexture.zAsBoolean() ? 1 : 0;
      float var9 = var8 != 0 ? var6.m00() : (float)Math.sqrt((double)Math.abs(var6.m00() * var6.m11() - var6.m01() * var6.m10()));
      float var10 = var9 * var7;
      int var11 = Math.max(64, var8 != 0 ? Math.round(var5.size * var10 * 4.0F) * 16 : Math.round(var5.size * var10) * 64);
      float var12 = (float)var11 / 64.0F / var5.size;
      HashMap<Integer, ArrayList<float[]>> var13 = new HashMap<>();
      if (var8 != 0) {
         float var14 = (var6.m00() * ((float)var2 + var5.shiftX) + var6.m20()) * var7;
         float var15 = (float)Math.round((var6.m11() * ((float)(var3 + 7) + var5.shiftY) + var6.m21()) * var7);
         int var16 = 0;

         while (var16 < var1.length()) {
            int var17 = var1.codePointAt(var16);
            var16 += Character.charCount(var17);
            float var18 = (float)Math.floor((double)var14);
            int var19 = Math.min(3, (int)((var14 - var18) * 4.0F));
            ClickGuiTextRenderer.Type_l var20 = yAsNp_Type_l(var5, var11, var17, var19);
            if (var20 == null) {
               return false;
            }

            if (!var20.empty()) {
               float var21 = (var18 + (float)var20.left) / var7;
               float var22 = (var15 - (float)var20.top) / var7;
               float var23 = var21 + (float)var20.width / var7;
               float var24 = var22 + (float)var20.height / var7;
               var13.computeIfAbsent(var20.page, var0x -> new ArrayList<>()).add(aAsFloatArray(var20, var21, var22, var21, var24, var23, var24, var23, var22));
            }

            var14 += IAsFloat(var5, var17) * var12;
         }
      } else {
         float var25 = (float)var2 + var5.shiftX;
         float var27 = (float)(var3 + 7) + var5.shiftY;
         int var29 = 0;

         while (var29 < var1.length()) {
            int var31 = var1.codePointAt(var29);
            var29 += Character.charCount(var31);
            ClickGuiTextRenderer.Type_l var33 = yAsNp_Type_l(var5, var11, var31, 0);
            if (var33 == null) {
               return false;
            }

            if (!var33.empty()) {
               float var35 = var25 + (float)var33.left / var12;
               float var37 = var27 - (float)var33.top / var12;
               float var39 = var35 + (float)var33.width / var12;
               float var42 = var37 + (float)var33.height / var12;
               var13.computeIfAbsent(var33.page, var0x -> new ArrayList<>())
                  .add(
                     aAsFloatArray(
                        var33,
                        WAsFloat(var6, var35, var37),
                        iAsFloat(var6, var35, var37),
                        WAsFloat(var6, var35, var42),
                        iAsFloat(var6, var35, var42),
                        WAsFloat(var6, var39, var42),
                        iAsFloat(var6, var39, var42),
                        WAsFloat(var6, var39, var37),
                        iAsFloat(var6, var39, var37)
                     )
                  );
            }

            var25 += IAsFloat(var5, var31);
         }
      }

      GpuSampler var26 = RenderSystem.getSamplerCache().get(var8 != 0 ? FilterMode.NEAREST : FilterMode.LINEAR);

      for (Entry var30 : var13.entrySet()) {
         float var32 = Float.MAX_VALUE;
         float var34 = Float.MAX_VALUE;
         float var36 = -Float.MAX_VALUE;
         float var38 = -Float.MAX_VALUE;

         for (float[] var43 : (Iterable<float[]>) ((List)var30.getValue())) {
            for (int var45 = 0; var45 < 4; var45++) {
               var32 = Math.min(var32, var43[var45 * 2]);
               var36 = Math.max(var36, var43[var45 * 2]);
               var34 = Math.min(var34, var43[var45 * 2 + 1]);
               var38 = Math.max(var38, var43[var45 * 2 + 1]);
            }
         }

         int var41 = (int)Math.floor((double)var32);
         int var44 = (int)Math.floor((double)var34);
         ScreenRect var46 = new ScreenRect(var41, var44, (int)Math.ceil((double)var36) - var41 + 1, (int)Math.ceil((double)var38) - var44 + 1);
         ScreenRect var47 = var0.scissorStack.peekLast();
         var0.state
            .addSimpleElement(
               new ClickGuiTextRenderer.Type_r(
                  TextureSetup.of(V.get((Integer)var30.getKey()), var26),
                  List.copyOf((Collection<? extends float[]>)var30.getValue()),
                  var4,
                  var47,
                  var47 == null ? var46 : var47.intersection(var46)
               )
            );
      }

      return true;
   }

   private static float WAsFloat(Matrix3x2fc var0, float var1, float var2) {
      return var0.m00() * var1 + var0.m10() * var2 + var0.m20();
   }

   private static float iAsFloat(Matrix3x2fc var0, float var1, float var2) {
      return var0.m01() * var1 + var0.m11() * var2 + var0.m21();
   }

   private static float[] aAsFloatArray(
      ClickGuiTextRenderer.Type_l var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8
   ) {
      return new float[]{
         var1,
         var2,
         var3,
         var4,
         var5,
         var6,
         var7,
         var8,
         (float)var0.u / 512.0F,
         (float)var0.v / 512.0F,
         (float)(var0.u + var0.width) / 512.0F,
         (float)(var0.v + var0.height) / 512.0F
      };
   }

   private static int nAsInt(ClickGuiTextRenderer.Type_g var0, int var1) {
      long var2 = (long)var0.index << 32 | (long)var1;
      Boolean var4 = P.get(var2);
      if (var4 != null) {
         return var4 ? 1 : 0;
      } else {
         int var5 = FreeType.FT_Get_Char_Index(var0.face, (long)var1) != 0 ? 1 : 0;
         P.put(var2, var5 != 0);
         return var5 != 0 ? 1 : 0;
      }
   }

   private static float IAsFloat(ClickGuiTextRenderer.Type_g var0, int var1) {
      long var2 = (long)var0.index << 32 | (long)var1;
      Float var4 = y.get(var2);
      if (var4 != null) {
         return var4;
      } else {
         MemoryStack var6 = MemoryStack.stackPush();

         float var5;
         try {
            CLongBuffer var7 = var6.mallocCLong(1);
            int var8 = FreeType.FT_Get_Char_Index(var0.face, (long)var1);
            oAsVoid(FreeType.FT_Get_Advance(var0.face, var8, 1, var7), "advance");
            var5 = (float)var7.get(0) * var0.size / (float)var0.unitsPerEm;
         } catch (Throwable var101) {
            if (var6 != null) {
               try {
                  var6.close();
               } catch (Throwable var91) {
                  var101.addSuppressed(var91);
               }
            }

            throw var101;
         }

         if (var6 != null) {
            var6.close();
         }

         y.put(var2, var5);
         return var5;
      }
   }

   private static ClickGuiTextRenderer.Type_l yAsNp_Type_l(ClickGuiTextRenderer.Type_g var0, int var1, int var2, int var3) {
      long var4 = (long)var0.index << 60 | (long)(var1 & 1048575) << 32 | (long)var3 << 24 | (long)var2;
      ClickGuiTextRenderer.Type_l var6 = K.get(var4);
      if (var6 != null) {
         return var6;
      } else {
         Integer var7 = D.get(var0.face.address());
         if (var7 == null || var7 != var1) {
            oAsVoid(FreeType.FT_Set_Char_Size(var0.face, 0L, (long)var1, 72, 72), "size");
            D.put(var0.face.address(), var1);
         }

         MemoryStack var9 = MemoryStack.stackPush();

         label96: {
            ClickGuiTextRenderer.Type_l var8;
            try {
               FT_Vector var10 = FT_Vector.malloc(var9).x((long)(var3 * 64 / 4)).y(0L);
               FreeType.FT_Set_Transform(var0.face, null, var10);
               oAsVoid(FreeType.FT_Load_Glyph(var0.face, FreeType.FT_Get_Char_Index(var0.face, (long)var2), 65544), "load");
               FT_GlyphSlot var11 = var0.face.glyph();
               oAsVoid(FreeType.FT_Render_Glyph(var11, 1), "render");
               FT_Bitmap var12 = var11.bitmap();
               int var13 = var12.width();
               int var14 = var12.rows();
               if (var13 != 0 && var14 != 0) {
                  int[] var15 = SAsIntArray(var13 + 2, var14 + 2);
                  if (var15 == null) {
                     U = true;
                     break label96;
                  }

                  int var16 = var12.pitch();
                  ByteBuffer var17 = var12.buffer(Math.abs(var16) * var14);
                  NativeImage var18 = new NativeImage(Format.RGBA, var13 + 2, var14 + 2, true);

                  try {
                     for (int var19 = 0; var19 < var14; var19++) {
                        for (int var20 = 0; var20 < var13; var20++) {
                           int var21 = var17.get(var19 * var16 + var20) & 255;
                           var18.setColorArgb(var20 + 1, var19 + 1, var21 << 24 | 16777215);
                        }
                     }

                     RenderSystem.getDevice()
                        .createCommandEncoder()
                        .writeToTexture(I.get(var15[0]), var18, 0, 0, var15[1], var15[2], var18.getWidth(), var18.getHeight(), 0, 0);
                  } catch (Throwable var251) {
                     try {
                        var18.close();
                     } catch (Throwable var241) {
                        var251.addSuppressed(var241);
                     }

                     throw var251;
                  }

                  var18.close();
                  var8 = new ClickGuiTextRenderer.Type_l(var15[0], var15[1] + 1, var15[2] + 1, var13, var14, var11.bitmap_left(), var11.bitmap_top());
               } else {
                  var8 = new ClickGuiTextRenderer.Type_l(0, 0, 0, 0, 0, 0, 0);
               }
            } catch (Throwable var26) {
               if (var9 != null) {
                  try {
                     var9.close();
                  } catch (Throwable var231) {
                     var26.addSuppressed(var231);
                  }
               }

               throw var26;
            }

            if (var9 != null) {
               var9.close();
            }

            K.put(var4, var8);
            return var8;
         }

         if (var9 != null) {
            var9.close();
         }

         return (ClickGuiTextRenderer.Type_l)null;
      }
   }

   private static int[] SAsIntArray(int var0, int var1) {
      if (I.isEmpty()) {
         LAsVoid();
      }

      if (J + var0 > 512) {
         H = H + M;
         J = 0;
         M = 0;
      }

      if (H + var1 > 512) {
         if (I.size() >= 6) {
            return null;
         }

         LAsVoid();
      }

      int[] var2 = new int[]{I.size() - 1, J, H};
      J += var0;
      M = Math.max(M, var1);
      return var2;
   }

   private static void LAsVoid() {
      GpuDevice var0 = RenderSystem.getDevice();
      GpuTexture var1 = var0.createTexture(() -> "Larp ClickGUI text " + I.size(), 5, TextureFormat.RGBA8, 512, 512, 1, 1);
      I.add(var1);
      V.add(var0.createTextureView(var1));
      M = 0;
      H = 0;
      J = 0;
      ClientLog.XAsVoid("Font", "Exact-size ClickGUI text page {} opened", I.size());
   }

   private static void GAsVoid() {
      U = false;
      K.clear();

      for (GpuTextureView var1 : V) {
         var1.close();
      }

      for (GpuTexture var3 : I) {
         var3.close();
      }

      V.clear();
      I.clear();
      M = 0;
      H = 0;
      J = 0;
      ClientLog.XAsVoid("Font", "Exact-size ClickGUI text pages full; glyphs will be rasterized again");
   }

   private static boolean XAsBoolean() {
      if (q != BloomQuality.J) {
         uAsVoid();
         q = BloomQuality.J;
         a = false;

         try {
            DAsVoid();
         } catch (IOException | RuntimeException var1) {
            nAsVoid("Exact-size ClickGUI text unavailable; native text until resource reload", var1);
         }
      }

      return !a && !b.isEmpty();
   }

   private static void DAsVoid() throws IOException {
      ResourceManager var0 = MinecraftClient.getInstance().getResourceManager();
      MemoryStack var1 = MemoryStack.stackPush();

      try {
         PointerBuffer var2 = var1.mallocPointer(1);
         oAsVoid(FreeType.FT_Init_FreeType(var2), "init");
         L = var2.get(0);
      } catch (Throwable var25) {
         if (var1 != null) {
            try {
               var1.close();
            } catch (Throwable var211) {
               var25.addSuppressed(var211);
            }
         }

         throw var25;
      }

      if (var1 != null) {
         var1.close();
      }

      HashMap var24 = new HashMap();

      for (TextDrawing.Type_x var5 : TextDrawing.Type_x.values()) {
         if (var5.name().startsWith("CLICK_")) {
            String var6 = var5.name().toLowerCase(Locale.ROOT);
            Resource var7 = var0.getResourceOrThrow(Identifier.of("larp-client", "font/" + var6 + ".json"));
            InputStreamReader var9 = new InputStreamReader(var7.getInputStream(), StandardCharsets.UTF_8);

            JsonObject var8;
            try {
               var8 = JsonParser.parseReader(var9).getAsJsonObject().getAsJsonArray("providers").get(0).getAsJsonObject();
            } catch (Throwable var22) {
               try {
                  var9.close();
               } catch (Throwable var20) {
                  var22.addSuppressed(var20);
               }

               throw var22;
            }

            var9.close();
            Identifier var26 = Identifier.of(var8.get("file").getAsString());
            FT_Face var10 = (FT_Face)var24.get(var26.toString());
            if (var10 == null) {
               byte[] var11;
               try (InputStream var12 = var0.getResourceOrThrow(var26.withPrefixedPath("font/")).getInputStream()) {
                  var11 = var12.readAllBytes();
               }

               ByteBuffer var28 = MemoryUtil.memAlloc(var11.length).put(var11).flip();
               F.add(var28);
               MemoryStack var13 = MemoryStack.stackPush();

               try {
                  PointerBuffer var14 = var13.mallocPointer(1);
                  oAsVoid(FreeType.FT_New_Memory_Face(L, var28, 0L, var14), "face " + var26);
                  var10 = FT_Face.create(var14.get(0));
               } catch (Throwable var23) {
                  if (var13 != null) {
                     try {
                        var13.close();
                     } catch (Throwable var18) {
                        var23.addSuppressed(var18);
                     }
                  }

                  throw var23;
               }

               if (var13 != null) {
                  var13.close();
               }

               var24.put(var26.toString(), var10);
            }

            JsonArray var27 = var8.getAsJsonArray("shift");
            int var29 = b.size();
            b.put(
               var5,
               new ClickGuiTextRenderer.Type_g(
                  var29,
                  var10,
                  var8.get("size").getAsFloat(),
                  var27 == null ? 0.0F : var27.get(0).getAsFloat(),
                  var27 == null ? 0.0F : var27.get(1).getAsFloat(),
                  var10.units_per_EM()
               )
            );
         }
      }

      ClientLog.GAsVoid(
         "Font", "Exact-size ClickGUI text ready: {} faces from {} files, rasterized by FreeType at the on-screen pixel size", b.size(), var24.size()
      );
   }

   private static void oAsVoid(int var0, String var1) {
      if (var0 != 0) {
         throw new IllegalStateException("FreeType " + var1 + " failed with error " + var0);
      }
   }

   private static void nAsVoid(String var0, Exception var1) {
      a = true;
      ClientLog.lAsVoid("Font", var0, var1);
      uAsVoid();
   }

   public static void uAsVoid() {
      for (GpuTextureView var1 : V) {
         var1.close();
      }

      for (GpuTexture var5 : I) {
         var5.close();
      }

      V.clear();
      I.clear();
      K.clear();
      y.clear();
      P.clear();
      D.clear();
      M = 0;
      H = 0;
      J = 0;
      HashSet var4 = new HashSet();

      for (ClickGuiTextRenderer.Type_g var2 : b.values()) {
         if (var4.add(var2.face.address())) {
            FreeType.FT_Done_Face(var2.face);
         }
      }

      b.clear();
      if (L != 0L) {
         FreeType.FT_Done_FreeType(L);
         L = 0L;
      }

      for (ByteBuffer var8 : F) {
         MemoryUtil.memFree(var8);
      }

      F.clear();
   }

   private static record Type_g(int index, FT_Face face, float size, float shiftX, float shiftY, int unitsPerEm) {
   }

   private static record Type_l(int page, int u, int v, int width, int height, int left, int top) {
      boolean empty() {
         return this.width == 0 || this.height == 0;
      }
   }

   private static record Type_r(TextureSetup textureSetup, List<float[]> quads, int color, ScreenRect scissorArea, ScreenRect bounds)
      implements SimpleGuiElementRenderState {
      public RenderPipeline pipeline() {
         return RenderPipelines.GUI_TEXTURED;
      }

      public void setupVertices(VertexConsumer vertices) {
         for (float[] var3 : this.quads) {
            vertices.vertex(var3[0], var3[1], 0.0F).texture(var3[8], var3[9]).color(this.color);
            vertices.vertex(var3[2], var3[3], 0.0F).texture(var3[8], var3[11]).color(this.color);
            vertices.vertex(var3[4], var3[5], 0.0F).texture(var3[10], var3[11]).color(this.color);
            vertices.vertex(var3[6], var3[7], 0.0F).texture(var3[10], var3[9]).color(this.color);
         }
      }
   }
}
