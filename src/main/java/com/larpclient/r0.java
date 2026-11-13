package com.larpclient;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerSkinType;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;

public final class r0 {
   private PlayerEntityModel x;
   private PlayerEntityModel q;
   private int D = -1;

   public r0() {
   }

   public void SAsVoid() {
      this.x = this.q = null;
      this.D = -1;
   }

   public void sAsVoid() {
      if (this.D != BloomQuality.J || this.x == null) {
         LoadedEntityModels var1 = MinecraftClient.getInstance().getLoadedEntityModels();
         this.x = new PlayerEntityModel(var1.getModelPart(EntityModelLayers.PLAYER), false);
         this.q = new PlayerEntityModel(var1.getModelPart(new net.minecraft.client.render.entity.model.EntityModelLayer(net.minecraft.util.Identifier.ofVanilla("player_slim"), "main")), true);
         this.D = BloomQuality.J;
      }
   }

   public void MAsVoid(PlayerEntityRenderState var1, az var2, az.Type_x[] var3, Vec3d var4, MatrixStack var5, EffectDrawBuffers var6, int var7, boolean var8) {
      if (this.D == BloomQuality.J && this.x != null) {
         int var9 = var1.skinTextures.model() == PlayerSkinType.SLIM ? 1 : 0;
         PlayerEntityModel var10 = var9 != 0 ? this.q : this.x;

         for (ModelPart var12 : var10.getRootPart().traverse()) {
            var12.resetTransform();
            var12.visible = true;
         }

         var10.hat.visible = var1.hatVisible;
         var10.jacket.visible = var1.jacketVisible;
         var10.leftSleeve.visible = var1.leftSleeveVisible;
         var10.rightSleeve.visible = var1.rightSleeveVisible;
         var10.leftPants.visible = var1.leftPantsLegVisible;
         var10.rightPants.visible = var1.rightPantsLegVisible;
         ModelPart[] var17 = new ModelPart[]{var10.head, var10.body, var10.rightArm, var10.leftArm, var10.rightLeg, var10.leftLeg};

         for (int var18 = 0; var18 < var3.length; var18++) {
            az.Type_x var13 = var3[var18];
            if (!(var13.alpha() <= 0.001) && !(var13.scale() <= 0.001) && !(var13.dissolve() >= 1.0)) {
               var5.push();

               try {
                  EAsVoid(var5, var13, var2.M, var18, var9 != 0, var4);
                  CosmeticSubmissionHandler.TAsVoid(
                     var17[var18], var5, var6, var1.skinTextures.body().texturePath(), var7, var13.alpha(), var13.dissolve(), var8
                  );
               } finally {
                  var5.pop();
               }
            }
         }
      }
   }

   public static void EAsVoid(MatrixStack var0, az.Type_x var1, double var2, int var4, boolean var5, Vec3d var6) {
      var0.translate(var1.position().x - var6.x, var1.position().y - var6.y, var1.position().z - var6.z);
      if (var1.orientation() != null) {
         var0.multiply(var1.orientation());
      } else {
         var0.multiply(new Quaternionf().rotateY((float)(Math.PI - var2)).rotateXYZ((float)var1.rx(), (float)var1.ry(), (float)var1.rz()));
      }

      float var7 = (float)(0.9375 * var1.scale());
      if (var1.stretch() == null) {
         var0.scale(-var7, -var7, var7);
      } else {
         var0.scale((float)((double)(-var7) * var1.stretch().x), (float)((double)(-var7) * var1.stretch().y), (float)((double)var7 * var1.stretch().z));
      }

      var0.translate(-az.mAsDouble(var4, var5) / 16.0, -az.A[var4][1] / 16.0, 0.0);
   }
}
