package com.larpclient;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderSetup;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Quaternionfc;

public final class SahurPetRenderer {
   public static final String ScreenScale = "Tung Tung Tung Sahur";
   private static final Identifier X = Identifier.of("larp-client", "models/pets/sahur/mesh.bin");
   private static final RenderLayer K = QAsRenderLayer(false);
   private static final RenderLayer PriceDetector = QAsRenderLayer(true);
   private static SahurMeshReader.Type_u g;
   private static long SmoothInput = -1L;
   private static long N = -1L;

   private static RenderLayer QAsRenderLayer(boolean var0) {
      Builder var1 = RenderPipeline.builder(new Snippet[]{RenderPipelines.POSITION_TEX_COLOR_SNIPPET})
         .withLocation(Identifier.of("larp-client", var0 ? "sahur_strike" : "sahur_pet"))
         .withFragmentShader(Identifier.of("larp-client", "particle/sprite"))
         .withVertexFormat(VertexFormats.POSITION_TEXTURE_COLOR, DrawMode.TRIANGLES)
         .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
         .withDepthWrite(true)
         .withoutBlend()
         .withCull(true);
      if (var0) {
         var1.withShaderDefine("DITHER_FADE");
      }

      return RenderLayer.of(
         var0 ? "larp_sahur_strike" : "larp_sahur_pet",
         RenderSetup.builder(var1.build()).texture("Sampler0", Identifier.of("larp-client", "models/pets/sahur/albedo.png")).build()
      );
   }

   public static boolean SAsBoolean() {
      return ChatPayParser != null && SmoothInput == (long)BloomQuality.J;
   }

   public static SahurMeshReader.Type_u TAsAo_Type_u() {
      if (!SAsBoolean()) {
         throw new IllegalStateException("Sahur asset is not prepared");
      } else {
         return g;
      }
   }

   public static void bAsVoid() {
      if (ChatPayParser == null || SmoothInput != (long)BloomQuality.J) {
         ChatPayParser = null;

         try (InputStream var0 = MinecraftClient.getInstance().getResourceManager().getResourceOrThrow(X).getInputStream()) {
            SahurMeshReader.Type_u var1 = SahurMeshReader.sAsAo_Type_u(var0);
            WorldLabelLayout.DAsVoid(K.getRenderPipeline());
            ChatPayParser = var1;
            SmoothInput = (long)BloomQuality.J;
            ClientLog.XAsVoid("Sahur", "Static mesh ready: {} triangles, resource generation {}", ChatPayParser.triangles(), t);
         } catch (IOException var51) {
            throw new IllegalStateException("Sahur asset load failed; reload resources to retry", var51);
         }
      }
   }

   public static void OAsVoid() {
      bAsVoid();
      if (N != (long)BloomQuality.J) {
         WorldLabelLayout.DAsVoid(PriceDetector.getRenderPipeline());
         N = (long)BloomQuality.J;
      }
   }

   public static void AAsVoid(EffectGeometryRenderer var0, Vec3d var1, Quaternionfc var2, float var3) {
      if (SAsBoolean()) {
         var0.oAsVoid(K, var1, var2, var3, ChatPayParser.vertices(), ChatPayParser.colors());
         op.FAsVoid("Sahur triangles", (long)ChatPayParser.triangles());
      }
   }

   public static void JAsVoid(MatrixStack var0, EffectDrawBuffers var1, Matrix4f var2) {
      if (SAsBoolean()) {
         Matrix4f var3 = new Matrix4f(var0.peek().getPositionMatrix()).mul(var2);
         EffectGeometryRenderer.GAsVoid(var1, K, var3, ChatPayParser.vertices(), ChatPayParser.colors());
         op.FAsVoid("Sahur triangles", (long)ChatPayParser.triangles());
      }
   }

   public static void qAsVoid(MatrixStack var0, EffectDrawBuffers var1, Matrix4f var2, float[] var3, int[] var4) {
      if (SAsBoolean()) {
         Matrix4f var5 = new Matrix4f(var0.peek().getPositionMatrix()).mul(var2);
         EffectGeometryRenderer.GAsVoid(var1, K, var5, var3, var4);
         op.FAsVoid("Sahur triangles", (long)(var4.length / 3));
      }
   }

   public static void QAsVoid(MatrixStack var0, EffectDrawBuffers var1, Matrix4f var2, float[] var3, int[] var4, double var5) {
      if (SAsBoolean() && !(var5 <= 0.001)) {
         Matrix4f var7 = new Matrix4f(var0.peek().getPositionMatrix()).mul(var2);
         VertexConsumer var8 = var1.XAsVertexConsumer(m);

         for (int var9 = 0; var9 < var4.length; var9++) {
            int var10 = var9 * 5;
            var8.vertex(var7, var3[var10], var3[var10 + 1], var3[var10 + 2]).texture(var3[var10 + 3], var3[var10 + 4]).color(bg.pAsInt(var4[var9], var5));
         }

         op.FAsVoid("Sahur triangles", (long)(var4.length / 3));
      }
   }

   private SahurPetRenderer() {
   }
}
