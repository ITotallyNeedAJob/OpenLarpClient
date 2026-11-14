package com.larpclient;

import com.larpclient.module.SwordModels;
import com.larpclient.module.ViewModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.MatrixStack.Entry;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.entity.player.PlayerSkinType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.SwingAnimationType;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class BladeGeometry {
   static final float k = (float)(Math.tan(Math.toRadians(35.0)) / (0.75 * Math.tan(Math.toRadians(34.0))));
   static final float[][] g = new float[][]{
      {0.36F, 0.16F, 0.78F, 1.0F, 0.25F, 0.78F, 0.2F, 0.82F, 1.0F},
      {0.42F, 0.14F, 0.8F, 0.85F, 0.2F, 0.95F, 0.55F, 0.4F, 1.0F},
      {0.62F, 0.07F, 0.25F, 1.0F, 0.42F, 0.55F, 1.0F, 0.7F, 0.3F},
      {0.12F, 0.25F, 0.7F, 0.15F, 0.85F, 1.0F, 0.7F, 0.95F, 1.0F},
      {0.28F, 0.31F, 0.36F, 0.62F, 0.68F, 0.78F, 0.92F, 0.95F, 1.0F}
   };
   private static final float[] i = new float[]{1.3F, 1.0F, 1.9F};
   private static final float[] J = new float[]{0.011F, -0.017F, 0.007F};
   private static final float[] S = new float[]{-0.031F, -0.052F, -0.095F};

   public static boolean KAsBoolean(
      AbstractClientPlayerEntity var0,
      Hand var1,
      ItemStack var2,
      float var3,
      float var4,
      float var5,
      MatrixStack var6,
      OrderedRenderCommandQueue var7,
      int var8
   ) {
      SwordModels var9 = SwordModels.vAsSwordModels();
      MinecraftClient var10 = MinecraftClient.getInstance();
      if (var9 != null && var9.rAsBoolean(var2) && var0 == var10.player && !var0.isUsingSpyglass()) {
         KnifeType var11 = var9.fAsBc();
         int var12 = var0.getSkin().model() == PlayerSkinType.SLIM ? 1 : 0;
         KnifeMesh var13 = KnifeAssets.RAsDe(var11, var12 != 0);
         if (var13 == null) {
            return false;
         } else {
            Arm var14 = var1 == Hand.MAIN_HAND ? var0.getMainArm() : var0.getMainArm().getOpposite();
            int var15 = var14 == Arm.RIGHT ? 1 : -1;
            if (var11.XAsBoolean()) {
               return GAsBoolean(var0, var1, var14, var15, var2, var3, var4, var5, var6, var7, var8, var9, var13);
            } else {
               KnifeState var16 = var9.WAsBj(var1);
               if (var0.isUsingItem() || !var10.isWindowFocused()) {
                  var16.sAsVoid();
               }

               double var17 = MediaSession.dAsDouble() + (double)Math.clamp(var5, 0.0F, 1.0F) * 0.05;
               KnifeState.Type_i var19 = var16.CAsBj_Type_i(var2, var11, var17, var3, var9.idleMotion.getValue());
               Matrix4f[] var20 = var9.NAsR8(var1).yAsMatrix4fArray(var13, var11, var19, var15, var9.modelScale.getValue().floatValue(), var17);
               Vector3f var21 = new Vector3f(
                  (float)var15 * var9.handX.getValue().floatValue(), var9.handY.getValue().floatValue() + var19.v, var9.handZ.getValue().floatValue()
               );
               Vector3f var22 = var20[0].transformPosition(new Vector3f((float)var15 * var11.Q, var11.a, var11.d));
               var6.push();

               try {
                  nAsVoid(var6, var1, var15, new Vector3f(var22).add(var21).mul(k, k, 1.0F));
                  var6.scale(k, k, 1.0F);
                  var6.translate(var21.x, var21.y, var21.z);
                  if (!var0.isInvisible()) {
                     var6.push();

                     try {
                        var6.multiplyPositionMatrix(var20[1]);
                        var6.scale(0.72F, 0.72F, 0.72F);
                        oAsVoid(var0, var1, var14, var15, var11, var6, var7, var8);
                     } finally {
                        var6.pop();
                     }
                  }

                  var6.multiplyPositionMatrix(var20[0]);

                  for (int var23 = 0; var23 < var13.parts().size(); var23++) {
                     KnifeMesh.Type_m var24 = var13.parts().get(var23);
                     var6.push();

                     try {
                        var6.multiplyPositionMatrix(var20[var23 + 2]);
                        LayerIndex.dAsVoid(var1, var15 < 0, var6, var24.vertices(), var15);
                        if (var24.name().equals("blade")) {
                           WeaponTrailRenderer.lAsVoid(var1, var2, var24.vertices(), var15, var6, var3, var19.J == 3, var7);
                        }

                        if (var24.tint()) {
                           float[] var25 = g[var9.ZAsInt()];
                           int var26 = LightmapTextureManager.pack(
                              Math.max(LightmapTextureManager.getBlockLightCoordinates(var8), 11), LightmapTextureManager.getSkyLightCoordinates(var8)
                           );
                           var7.submitCustom(
                              var6,
                              RenderLayers.entitySolid(KnifeAssets.kAsIdentifier(0)),
                              (var6x, var7x) -> BAsVoid(var24, var6x, var7x, var26, var15, 0, var25, var17)
                           );

                           for (int var27 = 1; var27 <= 2; var27++) {
                              int var28 = var27;
                              var7.getBatchingQueue(1)
                                 .submitCustom(
                                    var6,
                                    RenderLayers.eyes(KnifeAssets.kAsIdentifier(var27)),
                                    (var7x, var8x) -> BAsVoid(var24, var7x, var8x, var26, var15, var28, var25, var17)
                                 );
                           }
                        } else {
                           var7.submitCustom(
                              var6,
                              RenderLayers.entitySolid(KnifeAssets.nAsIdentifier(var11)),
                              (var3x, var4x) -> FAsVoid(var24, var3x, var4x, var8, var15, false)
                           );
                        }

                        if (var9.enchantmentGlint.getValue() && var2.hasGlint()) {
                           var7.getBatchingQueue(2).submitCustom(var6, RenderLayers.glint(), (var3x, var4x) -> FAsVoid(var24, var3x, var4x, var8, var15, true));
                        }
                     } finally {
                        var6.pop();
                     }
                  }
               } finally {
                  var6.pop();
               }

               return true;
            }
         }
      } else {
         return false;
      }
   }

   private static boolean GAsBoolean(
      AbstractClientPlayerEntity var0,
      Hand var1,
      Arm var2,
      int var3,
      ItemStack var4,
      float var5,
      float var6,
      float var7,
      MatrixStack var8,
      OrderedRenderCommandQueue var9,
      int var10,
      SwordModels var11,
      KnifeMesh var12
   ) {
      if (!var0.isUsingItem() && !var0.isUsingRiptide() && var4.getSwingAnimation().type() == SwingAnimationType.WHACK) {
         var8.push();

         try {
            KnifeType var13 = var11.fAsBc();
            WalkAnimation var14 = var13 == KnifeType.LEDGER ? ModelTranslation.lAsWa(var3, var5, var6, var11.modelScale.getValue().floatValue()) : ModelTranslation.BAsWa(var3, var5, var6);
            Vector3f var15 = new Vector3f(
               (float)var3 * var11.handX.getValue().floatValue(), var11.handY.getValue().floatValue(), var11.handZ.getValue().floatValue()
            );
            nAsVoid(var8, var1, var3, var14.weapon().getTranslation(new Vector3f()).add(var15));
            var8.translate(var15.x, var15.y, var15.z);
            if (var13 == KnifeType.WATCH) {
               KnifeState var16 = var11.WAsBj(var1);
               if (!MinecraftClient.getInstance().isWindowFocused()) {
                  var16.sAsVoid();
               }

               double var17 = MediaSession.dAsDouble() + (double)Math.clamp(var7, 0.0F, 1.0F) * 0.05;
               WatchInspectAnimation.Type_g var19 = var11.NAsQt(var1).sAsQt_Type_g(var16.CAsBj_Type_i(var4, var13, var17, var5, false), var17);
               var14 = ModelTranslation.YAsWa(var14, var3, var19.phase(), var19.weight());
            }

            if (!var0.isInvisible()) {
               var8.push();

               try {
                  var8.multiplyPositionMatrix(var14.arm());
                  var8.scale(0.72F, 0.72F, 0.72F);
                  oAsVoid(var0, var1, var2, var3, var13, var8, var9, var10);
               } finally {
                  var8.pop();
               }
            }

            var8.multiplyPositionMatrix(var14.weapon());

            for (KnifeMesh.Type_m var36 : var12.parts()) {
               var8.push();

               try {
                  if (var36.name().equals("case")) {
                     ModelTranslation.jAsVoid(var8, var11.modelScale.getValue().floatValue());
                  }

                  int var18 = var13 != KnifeType.LEDGER && !var36.name().equals("case") ? var3 : 1;
                  LayerIndex.dAsVoid(var1, var3 < 0, var8, var36.vertices(), var18);
                  var9.submitCustom(
                     var8, RenderLayers.entitySolid(KnifeAssets.nAsIdentifier(var13)), (var3x, var4x) -> FAsVoid(var36, var3x, var4x, var10, var18, false)
                  );
                  if (var11.enchantmentGlint.getValue() && var4.hasGlint()) {
                     var9.getBatchingQueue(1).submitCustom(var8, RenderLayers.glint(), (var3x, var4x) -> FAsVoid(var36, var3x, var4x, var10, var18, true));
                  }
               } finally {
                  var8.pop();
               }
            }
         } finally {
            var8.pop();
         }

         return true;
      } else {
         return false;
      }
   }

   private static void nAsVoid(MatrixStack var0, Hand var1, int var2, Vector3f var3) {
      ViewModel var4 = ViewModel.qAsViewModel();
      if (var4 != null && var4.isRuntimeActive()) {
         var4.GAsVoid(var0, var1, var2 < 0);
         var0.translate(var3.x, var3.y, var3.z);
         var4.yAsVoid(var0, var1, var2 < 0);
         var0.translate(-var3.x, -var3.y, -var3.z);
      }
   }

   private static void oAsVoid(
      AbstractClientPlayerEntity var0, Hand var1, Arm var2, int var3, KnifeType var4, MatrixStack var5, OrderedRenderCommandQueue var6, int var7
   ) {
      PlayerEntityRenderer var8 = MinecraftClient.getInstance().getEntityRenderDispatcher().getPlayerRenderer(var0);
      var5.push();

      try {
         int var9 = var0.getSkin().model() == PlayerSkinType.SLIM ? 1 : 0;
         BuildHelper.JAsVoid(var5, var4, var3, var9 != 0);
         Identifier var10 = var0.getSkin().body().texturePath();
         if (var2 == Arm.RIGHT) {
            var8.renderRightArm(var5, var6, var7, var10, var0.isModelPartVisible(PlayerModelPart.RIGHT_SLEEVE));
         } else {
            var8.renderLeftArm(var5, var6, var7, var10, var0.isModelPartVisible(PlayerModelPart.LEFT_SLEEVE));
         }

         PlayerEntityModel var11 = (PlayerEntityModel)var8.getModel();
         LayerIndex.WAsVoid(var1, var3 < 0, var5, var2 == Arm.RIGHT ? var11.rightArm : var11.leftArm);
         FlameParticles.PAsVoid(var2, var5, var6, true);
      } finally {
         var5.pop();
      }
   }

   private static void FAsVoid(KnifeMesh.Type_m var0, Entry var1, VertexConsumer var2, int var3, int var4, boolean var5) {
      float[] var6 = var0.vertices();

      for (int var7 = 0; var7 < var6.length; var7 += 32) {
         for (int var8 = 0; var8 < 4; var8++) {
            int var9 = var7 + KnifeMesh.vertexIndex(var4, var8) * 8;
            var2.vertex(var1, (float)var4 * var6[var9], var6[var9 + 1], var6[var9 + 2])
               .color(-1)
               .texture(var5 ? (float)var4 * var6[var9] : var6[var9 + 3], var5 ? var6[var9 + 1] : var6[var9 + 4])
               .overlay(OverlayTexture.DEFAULT_UV)
               .light(var3)
               .normal(var1, (float)var4 * var6[var9 + 5], var6[var9 + 6], var6[var9 + 7]);
         }
      }
   }

   static void BAsVoid(KnifeMesh.Type_m var0, Entry var1, VertexConsumer var2, int var3, int var4, int var5, float[] var6, double var7) {
      float[] var9 = var0.vertices();
      float[] var10 = var0.finishUv();
      int[] var11 = var0.finishColor();
      float var12 = i[var5];
      float var13 = (float)(var7 * (double)J[var5] % 1.0) + (var5 == 1 ? 0.37F : 0.0F);
      float var14 = (float)(var7 * (double)S[var5] % 1.0);
      float var15 = (float)(var7 % 3600.0);

      for (int var16 = 0; var16 < var9.length; var16 += 32) {
         for (int var17 = 0; var17 < 4; var17++) {
            int var18 = var16 + KnifeMesh.vertexIndex(var4, var17) * 8;
            float var19 = var10[var18 / 4];
            float var20 = var10[var18 / 4 + 1];
            int var21;
            if (var5 == 0) {
               float var22 = 1.25F * (float)(var11[var18 / 8] & 0xFF) / 255.0F;
               var21 = cAsInt(1.0F, var6[0] * var22, var6[1] * var22, var6[2] * var22);
            } else if (var5 == 1) {
               float var23 = 0.5F + 0.5F * (float)Math.sin((double)(var20 * 7.0F - var15 * 1.3F));
               var21 = cAsInt(0.85F, var6[3] + (var6[6] - var6[3]) * var23, var6[4] + (var6[7] - var6[4]) * var23, var6[5] + (var6[8] - var6[5]) * var23);
            } else {
               var21 = cAsInt(0.75F + 0.25F * (float)Math.sin((double)(var20 * 23.0F + var19 * 11.0F - var15 * 3.1F)), 1.0F, 1.0F, 1.0F);
            }

            var2.vertex(var1, (float)var4 * var9[var18], var9[var18 + 1], var9[var18 + 2])
               .color(var21)
               .texture(var19 * var12 + var13, var20 * var12 + var14)
               .overlay(OverlayTexture.DEFAULT_UV)
               .light(var3)
               .normal(var1, (float)var4 * var9[var18 + 5], var9[var18 + 6], var9[var18 + 7]);
         }
      }
   }

   private static int cAsInt(float var0, float var1, float var2, float var3) {
      return Math.round(Math.clamp(var0, 0.0F, 1.0F) * 255.0F) << 24
         | Math.round(Math.clamp(var1, 0.0F, 1.0F) * 255.0F) << 16
         | Math.round(Math.clamp(var2, 0.0F, 1.0F) * 255.0F) << 8
         | Math.round(Math.clamp(var3, 0.0F, 1.0F) * 255.0F);
   }

   private BladeGeometry() {
   }
}
