package com.larpclient;

import com.larpclient.module.PlayerModel;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.MatrixStack.Entry;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import org.joml.Matrix3f;
import org.joml.Vector3f;

public final class RiflePropRenderer {
   private static final Identifier MenuPopupProvider = zAsIdentifier("models/emotes/ak47/mesh.bin");
   private static final Identifier Y = zAsIdentifier("models/emotes/ak47/albedo.png");
   private static final int R = 15728880;
   private static float[] y;
   private static long D = -1L;
   private static long J = -1L;

   private RiflePropRenderer() {
   }

   private static Identifier zAsIdentifier(String var0) {
      return Identifier.of("larp-client", var0);
   }

   public static boolean vAsBoolean(PlayerEntityModel var0, PlayerEntityRenderState var1, MatrixStack var2, OrderedRenderCommandQueue var3, int var4) {
      o6 var5 = (o6)var1;
      float var6 = var5.larp$emoteWeight();
      if (!(var6 <= 0.0F) && "ak47".equals(var5.larp$emoteProp())) {
         PlayerModel var7 = PlayerModel.hAsPlayerModel();
         if (var7 != null && var7.FAsBoolean(var1)) {
            return false;
         } else if (!rAsBoolean()) {
            return false;
         } else {
            float[] var8 = var5.larp$emoteSample();
            int var9 = EmotecraftJsonReader.Type_r.PROP.ordinal() * 10;
            double var10 = qAsDouble(var8[var9 + 7], 1.0) * (double)var6;
            if (var10 < 0.001) {
               return true;
            } else {
               int var12 = qAsDouble(var8[var9 + 9], 0.0) >= 0.5 ? 1 : 0;
               var2.push();

               try {
                  if (var12 != 0) {
                     var0.setArmAngle(var1, Arm.RIGHT, var2);
                  } else {
                     var0.getRootPart().applyTransform(var2);
                     var0.body.applyTransform(var2);
                  }

                  RifleMeshConstraints.uAsVoid(
                     var2, var12 != 0, qAsDouble(var8[var9 + 3], 0.0), qAsDouble(var8[var9 + 4], 0.0), qAsDouble(var8[var9 + 5], 0.0), var10
                  );
                  float[] var13 = y;
                  var3.submitCustom(var2, RenderLayers.entitySolid(Y), (var2x, var3x) -> DAsVoid(var13, var2x, var3x, var4));
                  op.YAsVoid("Rifle quads", (long)(var13.length / 8 / 4));
                  if (var12 != 0 && var6 >= 0.999F) {
                     MAsVoid(var2, var3, var4, var5.larp$emoteCycle());
                  }
               } finally {
                  var2.pop();
               }

               return true;
            }
         }
      } else {
         return false;
      }
   }

   private static void MAsVoid(MatrixStack var0, OrderedRenderCommandQueue var1, int var2, float var3) {
      float[] var4 = RifleMeshConstraints.pAsFloatArray(var3);
      if (var4.length > 0) {
         var1.getBatchingQueue(1).submitCustom(var0, RenderLayers.eyes(Y), (var1x, var2x) -> DAsVoid(var4, var1x, var2x, 15728880));
      }

      Vector3f var5 = new Matrix3f(var0.peek().getNormalMatrix()).invert().transform(new Vector3f(0.0F, -1.0F, 0.0F));
      if (var5.lengthSquared() > 1.0E-8F) {
         float[] var6 = RifleMeshConstraints.rAsFloatArray(var3, var5.normalize());
         if (var6.length > 0) {
            var1.submitCustom(var0, RenderLayers.entitySolid(Y), (var2xx, var3x) -> DAsVoid(var6, var2xx, var3x, var2));
         }
      }
   }

   private static double qAsDouble(float var0, double var1) {
      return Float.isNaN(var0) ? var1 : (double)var0;
   }

   private static void DAsVoid(float[] var0, Entry var1, VertexConsumer var2, int var3) {
      for (int var4 = 0; var4 < var0.length; var4 += 8) {
         var2.vertex(var1, var0[var4], var0[var4 + 1], var0[var4 + 2])
            .color(-1)
            .texture(var0[var4 + 3], var0[var4 + 4])
            .overlay(OverlayTexture.DEFAULT_UV)
            .light(var3)
            .normal(var1, var0[var4 + 5], var0[var4 + 6], var0[var4 + 7]);
      }
   }

   private static boolean rAsBoolean() {
      if (J == (long)BloomQuality.J) {
         return false;
      } else if (ScreenScale != null && D == (long)BloomQuality.J) {
         return true;
      } else {
         try {
            try (InputStream var0 = MinecraftClient.getInstance().getResourceManager().getResourceOrThrow(f).getInputStream()) {
               MinecraftClient.getInstance().getResourceManager().getResourceOrThrow(Y);
               ScreenScale = RifleMeshConstraints.FAsFloatArray(PropMeshReader.dAsNj_Type_a(var0));
               D = (long)BloomQuality.J;
               ClientLog.GAsVoid("Emotes", "Rifle ready: {} quads, resource generation {}", ScreenScale.length / 8 / 4, D);
            }

            return true;
         } catch (RuntimeException | IOException var51) {
            J = (long)BloomQuality.J;
            ScreenScale = null;
            ClientLog.lAsVoid("Emotes", "Rifle model unavailable; AK Spray shows held items until resource reload", var51);
            return false;
         }
      }
   }
}
