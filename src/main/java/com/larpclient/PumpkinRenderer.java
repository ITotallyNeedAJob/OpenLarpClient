package com.larpclient;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
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
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public final class PumpkinRenderer {
   private static final String d = "models/halloween/pumpkin/";
   private static final RenderLayer V = DAsRenderLayer(false);
   private static final RenderLayer x = DAsRenderLayer(true);
   private static PumpkinMeshReader.Type_u Q;
   private static long s = -1L;

   private static Identifier oAsIdentifier(String var0) {
      return Identifier.of("larp-client", var0);
   }

   private static RenderLayer DAsRenderLayer(boolean var0) {
      RenderPipeline var1 = RenderPipeline.builder(new Snippet[]{RenderPipelines.POSITION_TEX_COLOR_SNIPPET})
         .withLocation(oAsIdentifier(var0 ? "pumpkin_emission" : "pumpkin"))
         .withFragmentShader(oAsIdentifier("particle/sprite"))
         .withShaderDefine("DITHER_FADE")
         .withVertexFormat(VertexFormats.POSITION_TEXTURE_COLOR, DrawMode.TRIANGLES)
         .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
         .withDepthWrite(!var0)
         .withBlend(var0 ? BlendFunction.LIGHTNING : BlendFunction.TRANSLUCENT)
         .withCull(true)
         .build();
      return RenderLayer.of(
         var0 ? "larp_pumpkin_emission" : "larp_pumpkin",
         RenderSetup.builder(var1).texture("Sampler0", oAsIdentifier("models/halloween/pumpkin/" + (var0 ? "emission.png" : "albedo.png"))).build()
      );
   }

   public static void GAsVoid() {
      if (Q == null || s != (long)BloomQuality.J) {
         Q = null;

         try (InputStream var0 = MinecraftClient.getInstance()
               .getResourceManager()
               .getResourceOrThrow(oAsIdentifier("models/halloween/pumpkin/mesh.bin"))
               .getInputStream()) {
            ResourceManager var1 = MinecraftClient.getInstance().getResourceManager();
            var1.getResourceOrThrow(oAsIdentifier("models/halloween/pumpkin/albedo.png"));
            var1.getResourceOrThrow(oAsIdentifier("models/halloween/pumpkin/emission.png"));
            PumpkinMeshReader.Type_u var2 = PumpkinMeshReader.XAsA7_Type_u(var0);
            WorldLabelLayout.DAsVoid(V.getRenderPipeline());
            WorldLabelLayout.DAsVoid(x.getRenderPipeline());
            Q = var2;
            s = (long)BloomQuality.J;
            ClientLog.XAsVoid(
               "Halloween", "Intact pumpkin ready: {} original triangles, {} static mesh groups, generation={}", var2.triangles(), var2.chunks().size(), s
            );
         } catch (IOException var51) {
            throw new IllegalStateException("Halloween pumpkin asset failed; existing cosmetic quarantine retries after resource reload", var51);
         }
      }
   }

   public static void gAsVoid(MatrixStack var0, EffectDrawBuffers var1, HalloweenShockwaveGeometry.Type_k var2, Vec3d var3, boolean var4) {
      if (Q != null && s == (long)BloomQuality.J && var2.pumpkin() != null) {
         if (!var4) {
            VAsVoid(var0, var1, var2, var3, false);
         }

         VAsVoid(var0, var1, var2, var3, true);
      }
   }

   private static void VAsVoid(MatrixStack var0, EffectDrawBuffers var1, HalloweenShockwaveGeometry.Type_k var2, Vec3d var3, boolean var4) {
      VertexConsumer var5 = var1.XAsVertexConsumer(var4 ? x : V);
      Matrix4f var6 = new Matrix4f(var0.peek().getPositionMatrix()).mul(HalloweenShockwaveGeometry.JAsMatrix4f(var2, var3));

      for (int var7 = 0; var7 < Q.chunks().size(); var7++) {
         PumpkinMeshReader.Type_l var8 = Q.chunks().get(var7);
         float[] var9 = var8.vertices();
         int[] var10 = var8.colors();

         for (int var11 = 0; var11 < var10.length; var11++) {
            int var12 = var11 * 5;
            int var13 = ColorUtil.XAsInt(var4 ? -9416661 : var10[var11], var2.pumpkin().alpha());
            var5.vertex(var6, var9[var12], var9[var12 + 1], var9[var12 + 2]).texture(var9[var12 + 3], var9[var12 + 4]).color(var13);
         }

         OpenScreen.FAsVoid(var4 ? "Halloween pumpkin emission triangles" : "Halloween pumpkin triangles", (long)var8.triangles());
      }
   }

   private PumpkinRenderer() {
   }
}
