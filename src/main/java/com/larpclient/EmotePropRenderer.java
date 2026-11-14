package com.larpclient;

import com.larpclient.module.Emotes;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderSetup;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class EmotePropRenderer {
   private static final String Y = "models/emotes/";
   private static final float z = 0.86F;
   private static final float MenuPopupProvider = 2.25F;
   private static final float SmoothInput = 0.05F;
   private static final int DropletTrail = 9;
   private static final RenderPipeline L = RenderPipeline.builder(new Snippet[]{RenderPipelines.POSITION_TEX_COLOR_SNIPPET})
      .withLocation(AAsIdentifier("emote_prop"))
      .withFragmentShader(AAsIdentifier("particle/sprite"))
      .withVertexFormat(VertexFormats.POSITION_TEXTURE_COLOR, DrawMode.TRIANGLES)
      .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
      .withDepthWrite(true)
      .withoutBlend()
      .withCull(true)
      .build();
   private static final Map<String, EmotePropRenderer.Type_w> ChatPayParser = new HashMap<>();
   private static final Map<String, RenderLayer> V = new HashMap<>();
   private static EmotePropRenderer.Type_z N;

   private static Identifier AAsIdentifier(String var0) {
      return Identifier.of("larp-client", var0);
   }

   private EmotePropRenderer() {
   }

   public static void lAsVoid() {
      N = null;
   }

   private static EmotePropRenderer.Type_w mAsBs_Type_w(String var0) {
      EmotePropRenderer.Type_w var1 = ChatPayParser.get(var0);
      if (var1 != null && var1.V == (long)BloomQuality.J) {
         return var1;
      } else {
         ResourceManager var2 = MinecraftClient.getInstance().getResourceManager();
         Identifier var3 = AAsIdentifier("models/emotes/" + var0 + "/albedo.png");

         try {
            EmotePropRenderer.Type_w var7;
            try (InputStream var4 = var2.getResourceOrThrow(AAsIdentifier("models/emotes/" + var0 + "/mesh.bin")).getInputStream()) {
               var2.getResourceOrThrow(var3);
               PropMeshReader.Type_a var5 = PropMeshReader.dAsNj_Type_a(var4);
               if (var5.parts().size() != 2) {
                  throw new IOException("Prop needs PetEffectRenderer body and PetEffectRenderer wheel part");
               }

               WorldLabelLayout.DAsVoid(L);
               RenderLayer var6 = V.computeIfAbsent(
                  var0, var1x -> RenderLayer.of("larp_emote_prop_" + var1x, RenderSetup.builder(L).texture("Sampler0", var3).build())
               );
               var1 = new EmotePropRenderer.Type_w(var5, var6, (long)BloomQuality.J);
               ChatPayParser.put(var0, var1);
               ClientLog.GAsVoid("Emotes", "Prop {} ready: {} triangles, resource generation {}", var0, var5.triangles(), var1.V);
               var7 = var1;
            }

            return var7;
         } catch (IOException var101) {
            throw new IllegalStateException("Emote prop " + var0 + " failed to load; reload resources to retry", var101);
         }
      }
   }

   public static void iAsVoid(float var0) {
      N = null;
      Emotes.Type_d var1 = Emotes.rAsEmotes_Type_d(var0);
      if (var1 != null) {
         float var2 = var1.track()[6] * var1.fade() * var1.avatarScale();
         float var3 = Math.min(1.0F, var1.age() / 9.0F);
         float var4 = var1.stopping() ? Math.min(1.0F, var1.stopAge() / 4.0F) : var3;
         float var5 = var1.stopping() ? (1.0F - var4) * 0.45F : (var3 < 1.0F ? (1.0F - var3) * (1.0F - var3) * 0.55F : 0.0F);
         if (!(var2 < 0.001F) || !(var5 <= 0.0F)) {
            EmotePropRenderer.Type_w var6 = mAsBs_Type_w(var1.prop());
            float[] var7 = var1.track();
            Quaternionf var8 = new Quaternionf().rotationY((float)Math.toRadians((double)(180.0F - var1.facing())));
            Quaternionf var9 = new Quaternionf(var8).rotateY(var7[4]);
            Quaternionf var10 = new Quaternionf(var9).rotateZ(var7[5]).rotateX(var7[3]);
            Vector3f var11 = var8.transform(new Vector3f(var7[0] * var2, 0.0F, var7[2] * var2));
            Vec3d var12 = var1.feet().add((double)var11.x, (double)var11.y, (double)var11.z);
            Vector3f var13 = var9.transform(new Vector3f(0.0F, var7[1] * var2, 0.0F));
            ClientWorld var14 = MinecraftClient.getInstance().world;
            int var15 = 15;
            if (var14 != null) {
               BlockPos var16 = BlockPos.ofFloored(var12.x, var12.y + 1.2, var12.z);
               var15 = var14.getBaseLightLevel(var16, var14.getAmbientDarkness());
            }

            yAsVoid(var6, var15);
            N = new EmotePropRenderer.Type_z(
               var6,
               var12,
               var9,
               var10,
               var12.add((double)var13.x, (double)var13.y, (double)var13.z),
               var2,
               Math.clamp(0.42F - var7[1] * 0.9F, 0.0F, 0.5F) * Math.min(1.0F, var2),
               0.55F + 1.9F * var4,
               var5
            );
         }
      }
   }

   private static void yAsVoid(EmotePropRenderer.Type_w var0, int var1) {
      if (var0.N != var1) {
         float var2 = 0.22F + 0.78F * (float)Math.pow((double)var1 / 15.0, 1.3);

         for (int var3 = 0; var3 < var0.S.length; var3++) {
            int[] var4 = var0.Z.parts().get(var3).colors();
            int[] var5 = var0.S[var3];

            for (int var6 = 0; var6 < var4.length; var6++) {
               int var7 = Math.round((float)(var4[var6] & 0xFF) * var2);
               var5[var6] = 0xFF000000 | var7 << 16 | var7 << 8 | var7;
            }
         }

         var0.N = var1;
      }
   }

   public static void AAsVoid(EffectGeometryRenderer var0, boolean var1) {
      EmotePropRenderer.Type_z var2 = N;
      if (var2 != null && !var1) {
         if (var2.puffAlpha > 0.0F) {
            var0.bAsVoid(
               var2.ground.x,
               var2.ground.y + 0.02,
               var2.ground.z,
               1.1 * (double)var2.puff,
               0.5 + 0.4 * (double)var2.puff,
               bg.XAsInt(-1446155, (double)var2.puffAlpha)
            );
            var0.bAsVoid(var2.ground.x, var2.ground.y + 0.03, var2.ground.z, 0.6 * (double)var2.puff, 0.35, bg.XAsInt(-1, (double)var2.puffAlpha * 0.7));
         }

         if (!(var2.scale < 0.001F)) {
            if (var2.shadow > 0.0F) {
               mAsVoid(var0, var2);
            }

            List var3 = var2.prop.Z.parts();
            var0.oAsVoid(var2.prop.y, var2.bodyOrigin, var2.bodyRotation, var2.scale, ((PropMeshReader.Type_h)var3.get(0)).vertices(), var2.prop.S[0]);
            var0.oAsVoid(var2.prop.y, var2.ground, var2.carRotation, var2.scale, ((PropMeshReader.Type_h)var3.get(1)).vertices(), var2.prop.S[1]);
            op.FAsVoid("Emote prop triangles", (long)var2.prop.Z.triangles());
         }
      }
   }

   private static void mAsVoid(EffectGeometryRenderer var0, EmotePropRenderer.Type_z var1) {
      float var2 = 0.32F * var1.scale;
      float var3 = 0.86F * var1.scale;
      float var4 = 2.25F * var1.scale;
      float var5 = 0.05F * var1.scale;
      int var6 = bg.XAsInt(-16777216, (double)var1.shadow);
      float[][] var8 = new float[][]{{-var3, var5 - var4}, {var3, var5 - var4}, {var3, var5 + var4}, {-var3, var5 + var4}};
      float[][] var9 = new float[][]{
         {-var3 - var2, var5 - var4 - var2}, {var3 + var2, var5 - var4 - var2}, {var3 + var2, var5 + var4 + var2}, {-var3 - var2, var5 + var4 + var2}
      };
      pAsVoid(var0, var1, var8[3], var6, var8[2], var6, var8[1], var6, var8[0], var6);

      for (int var10 = 0; var10 < 4; var10++) {
         int var11 = (var10 + 1) % 4;
         pAsVoid(var0, var1, var9[var10], 0, var9[var11], 0, var8[var11], var6, var8[var10], var6);
      }
   }

   private static void pAsVoid(
      EffectGeometryRenderer var0,
      EmotePropRenderer.Type_z var1,
      float[] var2,
      int var3,
      float[] var4,
      int var5,
      float[] var6,
      int var7,
      float[] var8,
      int var9
   ) {
      CAsVoid(var0, var1, var2, var3);
      CAsVoid(var0, var1, var4, var5);
      CAsVoid(var0, var1, var6, var7);
      CAsVoid(var0, var1, var8, var9);
   }

   private static void CAsVoid(EffectGeometryRenderer var0, EmotePropRenderer.Type_z var1, float[] var2, int var3) {
      Vector3f var4 = var1.carRotation.transform(new Vector3f(var2[0], 0.0F, var2[1]));
      var0.vertex(var1.ground.x + (double)var4.x, var1.ground.y + 0.015, var1.ground.z + (double)var4.z, var3);
   }

   private static final class Type_w {
      final PropMeshReader.Type_a Z;
      final RenderLayer y;
      final long V;
      final int[][] S;
      int N = -1;

      Type_w(PropMeshReader.Type_a var1, RenderLayer var2, long var3) {
         this.Z = var1;
         this.y = var2;
         this.V = var3;
         this.S = new int[var1.parts().size()][];

         for (int var5 = 0; var5 < this.S.length; var5++) {
            this.S[var5] = new int[var1.parts().get(var5).colors().length];
         }
      }
   }

   private static record Type_z(
      EmotePropRenderer.Type_w prop,
      Vec3d ground,
      Quaternionf carRotation,
      Quaternionf bodyRotation,
      Vec3d bodyOrigin,
      float scale,
      float shadow,
      float puff,
      float puffAlpha
   ) {
   }
}
