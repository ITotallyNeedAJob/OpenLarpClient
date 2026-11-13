package com.larpclient;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;

public final class CosmeticPlayerModel extends PlayerEntityModel {
   private final BipedEntityModel<BipedEntityRenderState> M;
   private final ModelPart F;
   private final ModelPart u;
   private final ModelPart V;
   private final ModelPart c;
   private final PlayerEntityRenderState C = new PlayerEntityRenderState();
   private AccountCosmeticsRenderer.Type_b J;

   public CosmeticPlayerModel(ModelPart var1) {
      this(var1, false);
   }

   public CosmeticPlayerModel(ModelPart var1, boolean var2) {
      super(var1, var2);
      java.util.function.Function<net.minecraft.util.Identifier, net.minecraft.client.render.RenderLayer> larpLayerFn = RenderLayers::entityTranslucent;
      this.M = new BipedEntityModel(var1, larpLayerFn);
      this.F = var1.getChild("head");
      this.u = var1.getChild("body");
      this.V = var1.getChild("right_leg");
      this.c = var1.getChild("left_leg");
   }

   public void jAsVoid(w9.Type_w var1) {
      this.C.age = var1.ageInTicks();
      this.C.limbSwingAnimationProgress = var1.walkPosition();
      this.C.limbSwingAmplitude = var1.walkSpeed();
      this.C.relativeHeadYaw = (float)Math.toDegrees((double)var1.headYaw());
      this.C.pitch = (float)Math.toDegrees((double)var1.headPitch());
      AccountCosmeticsRenderer.DAsVoid(this.C, var1);
      this.M.setAngles(this.C);
   }

   PlayerEntityRenderState QAsPlayerEntityRenderState() {
      return this.C;
   }

   ModelPart XAsModelPart() {
      return this.F;
   }

   ModelPart vAsModelPart() {
      return this.u;
   }

   ModelPart tAsModelPart() {
      return this.V;
   }

   ModelPart PAsModelPart() {
      return this.c;
   }

   void mAsVoid(AccountCosmeticsRenderer.Type_b var1) {
      this.J = var1;
      la var2 = var1 == null ? null : var1.korblox();
      this.F.visible = var1 == null || !var1.headless();
      this.V.visible = var2 == null || !var2.right();
      this.c.visible = var2 == null || !var2.left();
   }

   AccountCosmeticsRenderer.Type_b HAsB1_Type_b() {
      return this.J;
   }
}
