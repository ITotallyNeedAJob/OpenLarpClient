package com.larpclient;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderSetup;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Quaternionfc;

public final class BitcoinPetMesh {
   public static final String J = "Bitcoin";
   public static final double z = 0.6;
   public static final double R = 1.6;
   public static final int K = -552166;
   private static final String v = "models/pets/";
   private static final RenderPipeline T = RenderPipeline.builder(new Snippet[]{RenderPipelines.POSITION_TEX_COLOR_SNIPPET})
      .withLocation(KAsIdentifier("bitcoin_pet"))
      .withFragmentShader(KAsIdentifier("particle/sprite"))
      .withVertexFormat(VertexFormats.POSITION_TEXTURE_COLOR, DrawMode.TRIANGLES)
      .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
      .withDepthWrite(true)
      .withoutBlend()
      .withCull(true)
      .build();
   public static final List<BitcoinPetMesh.Type_w> h = List.of(
      gAsBy_Type_w("Bitcoin", "bitcoin", 0.6, -552166),
      gAsBy_Type_w("Solana", "solana", 0.6, -6732289),
      gAsBy_Type_w("Ethereum", "ethereum", 0.6, -10322198),
      gAsBy_Type_w("Litecoin", "litecoin", 0.6, -13345379)
   );
   private static BitcoinMeshReader.Type_g V;
   private static long I = -1L;
   private static long N = -1L;

   private static Identifier KAsIdentifier(String var0) {
      return Identifier.of("larp-client", var0);
   }

   private static BitcoinPetMesh.Type_w gAsBy_Type_w(String var0, String var1, double var2, int var4) {
      return new BitcoinPetMesh.Type_w(
         var0,
         var1,
         var2,
         var4,
         RenderLayer.of("larp_" + var1 + "_pet", RenderSetup.builder(T).texture("Sampler0", KAsIdentifier("models/pets/" + var1 + "/albedo.png")).build()),
         var0 + " triangles"
      );
   }

   public static BitcoinPetMesh.Type_w AAsBy_Type_w(String var0) {
      for (BitcoinPetMesh.Type_w var2 : h) {
         if (var2.style().equals(var0)) {
            return var2;
         }
      }

      return null;
   }

   public static boolean NAsBoolean() {
      return V != null && I == (long)BloomQuality.J;
   }

   public static void hAsVoid() {
      if (!NAsBoolean()) {
         V = null;
         ResourceManager var0 = MinecraftClient.getInstance().getResourceManager();

         try (InputStream var1 = var0.getResourceOrThrow(KAsIdentifier("models/pets/bitcoin/mesh.bin")).getInputStream()) {
            for (BitcoinPetMesh.Type_w var3 : h) {
               var0.getResourceOrThrow(KAsIdentifier("models/pets/" + var3.folder() + "/albedo.png"));
            }

            BitcoinMeshReader.Type_g var7 = BitcoinMeshReader.SAsQ6_Type_g(var1);
            WorldLabelLayout.DAsVoid(T);
            V = var7;
            I = (long)BloomQuality.J;
            ClientLog.XAsVoid("Pets", "Crypto coins ready: {} faces, {} triangles, resource generation {}", h.size(), V.triangles(), I);
         } catch (IOException var61) {
            throw new IllegalStateException("Crypto coin asset load failed; reload resources to retry", var61);
         }
      }
   }

   public static boolean nAsBoolean(String var0) {
      if (NAsBoolean()) {
         return true;
      } else if (N == (long)BloomQuality.J) {
         return false;
      } else {
         try {
            hAsVoid();
            return true;
         } catch (RuntimeException var2) {
            N = (long)BloomQuality.J;
            ClientLog.lAsVoid(var0, "Crypto coins unavailable; using fallback until resource reload", var2);
            return false;
         }
      }
   }

   public static BitcoinMeshReader.Type_g TAsQ6_Type_g() {
      return NAsBoolean() ? V : null;
   }

   public static void rAsVoid(EffectGeometryRenderer var0, BitcoinPetMesh.Type_w var1, Matrix4f var2) {
      if (NAsBoolean()) {
         var0.SAsVoid(var1.type(), var2, V.vertices(), V.colors());
         op.FAsVoid(var1.profile(), (long)V.triangles());
      }
   }

   public static void uAsVoid(EffectGeometryRenderer var0, BitcoinPetMesh.Type_w var1, Vec3d var2, Quaternionfc var3, float var4) {
      if (NAsBoolean()) {
         var0.oAsVoid(var1.type(), var2, var3, var4, V.vertices(), V.colors());
         op.FAsVoid(var1.profile(), (long)V.triangles());
      }
   }

   private BitcoinPetMesh() {
   }

   public static record Type_w(String style, String folder, double scale, int spark, RenderLayer type, String profile) {
   }
}
