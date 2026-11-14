package com.larpclient;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.DragonEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public final class DragonWingRenderer {
   public static final Identifier C = Identifier.ofVanilla("textures/entity/enderdragon/dragon.png");
   public static final float j = 0.15F;
   public static final float c = 2.25F;
   public static final float S = 3.4F;
   public static final float P = 4.2F;
   public static final int K = 48;
   public static final float a = 50.0F;
   public static final float Y = 64.0F;

   public static float oAsFloat(boolean var0) {
      return var0 ? 4.2F : 3.4F;
   }

   public static float YAsFloat(float var0) {
      return 2.1F + 4.0F * (float)Math.sqrt(2.0) * 0.15F * var0;
   }

   public static float MAsFloat(float var0, float var1, float var2, float var3) {
      float var4 = clamp(var0);
      float var5 = clamp(var1);
      float var6 = clamp(var2);
      float var7 = clamp(var3);
      float var8 = 0.12566371F * (1.0F + 0.45F * var4 + 1.1F * var6) * (1.0F - 0.25F * var5);
      return aAsFloat(var8, 0.09817477F, var7);
   }

   public static DragonWingRenderer.Type_n NAsMe_Type_n(float var0, float var1, float var2, float var3, float var4) {
      float var5 = clamp(var1);
      float var6 = clamp(var2);
      float var7 = clamp(var3);
      float var8 = clamp(var4);
      float var9 = aAsFloat(-85.0F, -78.0F, var5);
      float var10 = -12.0F - 6.0F * var5 - 10.0F * var6 - 4.0F * var7 * (1.0F - var5);
      float var11 = -28.0F + 16.0F * var6;
      float var12 = -6.0F + 6.0F * var5 + 2.0F * var6;
      float var13 = (0.16F + 0.06F * var5 + 0.3F * var7 * (1.0F - 0.35F * var5)) * (1.0F - 0.6F * var6);
      var9 = aAsFloat(var9, -85.0F, var8);
      var10 = aAsFloat(var10, -8.0F, var8);
      var11 = aAsFloat(var11, -4.0F, var8);
      var12 = aAsFloat(var12, -5.0F, var8);
      var13 = aAsFloat(var13, 0.1F, var8);
      return new DragonWingRenderer.Type_n(
         CAsFloat(var9) - (float)Math.cos((double)var0) * 0.2F * var13,
         CAsFloat(var10) + (float)Math.sin((double)(var0 - 0.6F)) * 0.24F * var13,
         CAsFloat(var11) - (float)(Math.sin((double)var0) + 0.125) * 0.8F * var13,
         CAsFloat(var12) - (float)(Math.sin((double)(var0 + 2.0F)) + 0.5) * 0.75F * 0.6F * var13
      );
   }

   public static float IAsFloat(float var0, boolean var1, float var2) {
      return var1 ? var0 * Math.clamp(var2 * var2 / 100.0F, 0.0F, 1.0F) : var0;
   }

   public static float XAsFloat(int var0) {
      double var1 = (double)var0 * 0.6180339887498949 % 1.0;
      return (float)((var1 < 0.0 ? var1 + 1.0 : var1) * 2.0 * Math.PI);
   }

   public static RenderLayer mAsRenderLayer(int var0) {
      return var0 >>> 24 == 255 ? RenderLayers.entityCutoutNoCull(C) : RenderLayers.entityTranslucent(C);
   }

   private static float clamp(float var0) {
      return Math.clamp(var0, 0.0F, 1.0F);
   }

   private static float aAsFloat(float var0, float var1, float var2) {
      return var0 + (var1 - var0) * var2;
   }

   private static float CAsFloat(float var0) {
      return var0 * (float) (Math.PI / 180.0);
   }

   private DragonWingRenderer() {
   }

   public static record Type_n(float wingX, float wingY, float wingZ, float tipZ) {
   }

   public static final class Type_q {
      private final ModelPart j;
      private final ModelPart R;
      private final ModelPart G;
      private final ModelPart o;

      public Type_q(ModelPart var1) {
         ModelPart var2 = var1.getChild("body");
         this.j = var2.getChild("left_wing");
         this.R = this.j.getChild("left_wing_tip");
         this.G = var2.getChild("right_wing");
         this.o = this.G.getChild("right_wing_tip");
      }

      public static DragonWingRenderer.Type_q XAsMe_Type_q() {
         return new DragonWingRenderer.Type_q(DragonEntityModel.createTexturedModelData().createModel());
      }

      public ModelPart zAsModelPart(int var1) {
         return var1 > 0 ? this.j : this.G;
      }

      public void eAsVoid(DragonWingRenderer.Type_n var1) {
         this.j.setOrigin(0.0F, 0.0F, 0.0F);
         this.G.setOrigin(0.0F, 0.0F, 0.0F);
         this.j.setAngles(var1.wingX(), var1.wingY(), var1.wingZ());
         this.G.setAngles(var1.wingX(), -var1.wingY(), -var1.wingZ());
         this.R.setAngles(0.0F, 0.0F, var1.tipZ());
         this.o.setAngles(0.0F, 0.0F, -var1.tipZ());
      }

      public static void SAsVoid(MatrixStack var0, int var1, float var2, boolean var3) {
         var0.translate((float)var1 * 2.25F / 16.0F, DragonWingRenderer.oAsFloat(var3) / 16.0F, DragonWingRenderer.YAsFloat(var2) / 16.0F);
         float var4 = 0.15F * var2;
         var0.scale(var4, var4, var4);
      }

      public void mAsVoid(MatrixStack var1, VertexConsumer var2, float var3, boolean var4, int var5, int var6) {
         for (int var7 = -1; var7 <= 1; var7 += 2) {
            var1.push();

            try {
               SAsVoid(var1, var7, var3, var4);
               this.zAsModelPart(var7).render(var1, var2, var5, OverlayTexture.DEFAULT_UV, var6);
            } finally {
               var1.pop();
            }
         }
      }
   }
}
