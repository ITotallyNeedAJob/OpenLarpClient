package com.larpclient;

import com.larpclient.mixin.feature.entity.ModelPartAccessor;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.model.ModelPart.Cuboid;
import net.minecraft.client.model.ModelPart.Quad;
import net.minecraft.client.model.ModelPart.Vertex;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;

public final class PlayerJointTransforms {
   private static final float n = 4.0F;
   private static final float v = 6.0F;
   private final ModelPart[] b;
   private final ModelPart[] M;
   private final ModelPart[] H;
   private final ModelPart[] f;
   private final boolean[] d = new boolean[8];
   private final boolean[] j = new boolean[8];
   private boolean Y;

   public PlayerJointTransforms(PlayerEntityModel var1) {
      this.b = new ModelPart[]{var1.rightArm, var1.leftArm, var1.rightLeg, var1.leftLeg, var1.rightSleeve, var1.leftSleeve, var1.rightPants, var1.leftPants};
      this.M = new ModelPart[4];
      this.H = new ModelPart[4];
      this.f = new ModelPart[8];

      for (int var2 = 0; var2 < 4; var2++) {
         float var3 = var2 < 2 ? 4.0F : 6.0F;
         PlayerJointTransforms.Type_u var4 = PlayerJointTransforms.Type_u.ofAsNz_Type_u(this.b[var2]);
         if (var4 != null) {
            ModelPart var5 = this.b[var2 + 4];
            PlayerJointTransforms.Type_u var6 = var5.getDefaultTransform().equals(ModelTransform.NONE)
               ? PlayerJointTransforms.Type_u.ofAsNz_Type_u(var5)
               : null;
            LinkedHashMap var7 = new LinkedHashMap();
            LinkedHashMap var8 = new LinkedHashMap();
            if (var6 != null) {
               this.f[var2] = new ModelPart(List.of(var6.upper(var3)), Map.of());
               this.f[var2 + 4] = new ModelPart(List.of(var6.lower(var3)), Map.of());
               var8.put("layer", this.f[var2 + 4]);
               this.j[var2 + 4] = true;
            }

            this.H[var2] = new ModelPart(List.of(var4.lower(var3)), var8);
            this.H[var2].setDefaultTransform(ModelTransform.origin(0.0F, var3, 0.0F));
            this.H[var2].resetTransform();
            var7.put("joint", this.H[var2]);
            if (this.f[var2] != null) {
               var7.put("layer", this.f[var2]);
            }

            ModelPart var9 = new ModelPart(List.of(var4.upper(var3)), var7);
            var9.visible = false;
            this.M[var2] = var9;
            this.j[var2] = true;
            ((ModelPartAccessor)(Object)this.b[var2]).larpWalkChildren().put("larp_walk_joint", var9);
         }
      }
   }

   public static ModelPart UAsModelPart(int var0, boolean var1) {
      int var2 = var0 < 2 ? 1 : 0;
      int var3 = (var0 & 1) == 1 ? 1 : 0;
      int var4 = var2 != 0 && var1 ? 3 : 4;
      float var5 = var2 != 0 ? (var3 != 0 ? -1.0F : (var1 ? -2.0F : -3.0F)) : -2.0F;
      float var6 = var2 != 0 ? -2.0F : 0.0F;
      float var7 = var2 != 0 ? 4.0F : 6.0F;
      int var8 = var2 != 0 ? (var3 != 0 ? 32 : 40) : (var3 != 0 ? 16 : 0);
      int var9 = var3 != 0 ? 48 : 16;
      int var10 = var2 != 0 ? (var3 != 0 ? 48 : 40) : (var3 != 0 ? 0 : 0);
      int var11 = var3 != 0 ? 48 : 32;
      ModelData var12 = new ModelData();
      ModelPartData var13 = var12.getRoot()
         .addChild("upper", ModelPartBuilder.create().uv(var8, var9).cuboid(var5, var6, -2.0F, (float)var4, 6.0F, 4.0F), ModelTransform.NONE);
      var13.addChild(
         "layer", ModelPartBuilder.create().uv(var10, var11).cuboid(var5, var6, -2.0F, (float)var4, 6.0F, 4.0F, new Dilation(0.25F)), ModelTransform.NONE
      );
      ModelPartData var14 = var13.addChild(
         "joint", ModelPartBuilder.create().uv(var8, var9 + 5).cuboid(var5, -1.0F, -2.0F, (float)var4, 7.0F, 4.0F), ModelTransform.origin(0.0F, var7, 0.0F)
      );
      var14.addChild(
         "layer", ModelPartBuilder.create().uv(var10, var11 + 5).cuboid(var5, -1.0F, -2.0F, (float)var4, 7.0F, 4.0F, new Dilation(0.25F)), ModelTransform.NONE
      );
      return TexturedModelData.of(var12, 64, 64).createModel().getChild("upper");
   }

   public static Cuboid UAsModelPart_Cuboid(List<Cuboid> var0) {
      PlayerJointTransforms.Type_u var1 = PlayerJointTransforms.Type_u.ofAsNz_Type_u(var0);
      return var1 == null ? null : var1.whole();
   }

   public void SAsVoid() {
      if (this.Y) {
         for (int var1 = 0; var1 < 8; var1++) {
            if (this.j[var1]) {
               this.b[var1].hidden = this.d[var1];
            }
         }
      }

      for (ModelPart var4 : this.M) {
         if (var4 != null) {
            var4.visible = false;
         }
      }

      this.Y = false;
   }

   public void dAsVoid(Arm var1, MatrixStack var2) {
      ModelPart var3 = this.H[var1 == Arm.RIGHT ? 0 : 1];
      if (this.Y && var3 != null) {
         var2.translate(0.0, 0.25, 0.0);
         var2.multiply(RotationAxis.POSITIVE_X.rotation(var3.pitch));
         var2.translate(0.0, -0.25, 0.0);
      }
   }

   public void fAsVoid(float[] var1, PlayerEntityRenderState var2) {
      if (var1 != null) {
         for (int var3 = 0; var3 < 8; var3++) {
            if (this.j[var3]) {
               this.d[var3] = this.b[var3].hidden;
               this.b[var3].hidden = true;
            }
         }

         for (int var4 = 0; var4 < 4; var4++) {
            if (this.M[var4] != null) {
               this.M[var4].visible = true;
               this.H[var4].pitch = var1[var4];
               if (this.f[var4] != null) {
                  this.f[var4].visible = this.f[var4 + 4].visible = this.b[var4 + 4].visible;
               }
            }
         }

         this.Y = true;
      }
   }

   private static record Type_u(
      int u, int v, float x, float y, float z, float w, float h, float d, float growX, float growY, float growZ, boolean mirror, float texW, float texH
   ) {
      static PlayerJointTransforms.Type_u ofAsNz_Type_u(ModelPart var0) {
         return ofAsNz_Type_u(((ModelPartAccessor)(Object)var0).larpWalkCubes());
      }

      static PlayerJointTransforms.Type_u ofAsNz_Type_u(List<Cuboid> var0) {
         if (var0.size() != 1) {
            return null;
         } else {
            Cuboid var1 = var0.getFirst();
            float var2 = var1.maxX - var1.minX;
            float var3 = var1.maxY - var1.minY;
            float var4 = var1.maxZ - var1.minZ;
            if (!(var2 <= 0.0F) && !(var3 <= 0.0F) && !(var4 <= 0.0F)) {
               float var5 = Float.MAX_VALUE;
               float var6 = -Float.MAX_VALUE;
               float var7 = Float.MAX_VALUE;
               float var8 = -Float.MAX_VALUE;
               float var9 = Float.MAX_VALUE;
               float var10 = -Float.MAX_VALUE;
               float var11 = Float.MAX_VALUE;
               float var12 = Float.MAX_VALUE;

               for (Quad var16 : var1.sides) {
                  for (Vertex var20 : var16.vertices()) {
                     var5 = Math.min(var5, var20.x());
                     var6 = Math.max(var6, var20.x());
                     var7 = Math.min(var7, var20.y());
                     var8 = Math.max(var8, var20.y());
                     var9 = Math.min(var9, var20.z());
                     var10 = Math.max(var10, var20.z());
                     var11 = Math.min(var11, var20.u());
                     var12 = Math.min(var12, var20.v());
                  }
               }

               float var31 = 0.0F;
               float var32 = 0.0F;
               Boolean var33 = null;

               for (Quad var37 : var1.sides) {
                  Vertex[] var38 = var37.vertices();
                  float var21 = Float.MAX_VALUE;
                  float var22 = -Float.MAX_VALUE;
                  float var23 = Float.MAX_VALUE;
                  float var24 = -Float.MAX_VALUE;
                  byte var25 = 1;
                  byte var26 = 1;

                  for (Vertex var30 : var38) {
                     var21 = Math.min(var21, var30.u());
                     var22 = Math.max(var22, var30.u());
                     var23 = Math.min(var23, var30.v());
                     var24 = Math.max(var24, var30.v());
                     var25 = (byte)(var25 & ((double)Math.abs(var30.y() - var7) < 1.0E-4 ? 1 : 0));
                     var26 = (byte)(var26 & ((double)Math.abs(var30.x() - var38[0].x()) < 1.0E-4 ? 1 : 0));
                  }

                  if (var25 != 0 && var22 > var21 && var24 > var23) {
                     var31 = var2 / (var22 - var21);
                     var32 = var4 / (var24 - var23);
                  }

                  if (var26 != 0 && (double)Math.abs(var21 - var11) < 1.0E-5) {
                     var33 = (double)Math.abs(var38[0].x() - var6) < 1.0E-4;
                  }
               }

               return !(var31 <= 0.0F) && !(var32 <= 0.0F) && var33 != null
                  ? new PlayerJointTransforms.Type_u(
                     Math.round(var11 * var31),
                     Math.round(var12 * var32),
                     var1.minX,
                     var1.minY,
                     var1.minZ,
                     var2,
                     var3,
                     var4,
                     (var6 - var5 - var2) / 2.0F,
                     (var8 - var7 - var3) / 2.0F,
                     (var10 - var9 - var4) / 2.0F,
                     var33,
                     (float)Math.round(var31),
                     (float)Math.round(var32)
                  )
                  : null;
            } else {
               return null;
            }
         }
      }

      Cuboid whole() {
         return this.cube(this.y, this.h, 0);
      }

      Cuboid upper(float var1) {
         return this.cube(this.y, var1 - this.y, 0);
      }

      Cuboid lower(float var1) {
         float var2 = var1 - 1.0F;
         return this.cube(var2 - var1, this.y + this.h - var2, Math.round(var2 - this.y));
      }

      private Cuboid cube(float var1, float var2, int var3) {
         return new Cuboid(
            this.u,
            this.v + var3,
            this.x,
            var1,
            this.z,
            this.w,
            var2,
            this.d,
            this.growX,
            this.growY,
            this.growZ,
            this.mirror,
            this.texW,
            this.texH,
            EnumSet.allOf(Direction.class)
         );
      }
   }
}
