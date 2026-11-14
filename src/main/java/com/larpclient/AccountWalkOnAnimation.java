package com.larpclient;

import com.larpclient.module.ChinaHat;
import com.larpclient.module.Wings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.entity.model.PlayerCapeModel;
import net.minecraft.entity.player.PlayerSkinType;
import net.minecraft.entity.player.SkinTextures;

public final class AccountWalkOnAnimation {
   private final WindEffect f;
   private final AccountCosmeticsRenderer r = new AccountCosmeticsRenderer();
   private CosmeticPlayerModel d;
   private CosmeticPlayerModel q;
   private PlayerCapeModel C;
   private int w = -1;
   private int K = -1;
   private WindEffect.Type_c s;
   private double D;

   public AccountWalkOnAnimation(WindEffect var1) {
      this.f = var1;
   }

   public void lAsVoid(DrawContext var1, RootMenuLayout var2, NetClient var3, AccountSkinPreview var4, int var5, int var6, boolean var7, boolean var8) {
      MinecraftClient var9 = MinecraftClient.getInstance();
      ChinaHat.Type_a var10 = AccountCosmeticsRenderer.AAsChinaHat_Type_a();
      Wings.Type_n var11 = AccountCosmeticsRenderer.oAsWings_Type_n();
      WindEffect.Type_i var12 = WindEffect.WAsW9_Type_i(var2, AccountCosmeticsRenderer.jAsDouble(var10, var11));
      int var13 = var12.visible() && var9.getOverlay() == null && var9.isWindowFocused() ? 1 : 0;
      WindEffect.Type_m var14 = this.f.pAsW9_Type_m(System.nanoTime(), var13 != 0, var7, var8, var3.wAsBoolean(), var12, var2.width(), var5, var6);
      this.D = var14.nameOpacity();
      if (var14.stage() != this.s) {
         if (var14.stage() == WindEffect.Type_c.WALKING && this.s == WindEffect.Type_c.HIDDEN) {
            ClientLog.GAsVoid("Menu", "Account walk-on started after menu reveal; native left-facing stride");
         } else if (var14.stage() == WindEffect.Type_c.IDLE && this.s == WindEffect.Type_c.TURNING) {
            ClientLog.GAsVoid("Menu", "Account walk-on settled; native idle and pointer tracking active");
         }

         this.s = var14.stage();
         ClientLog.XAsVoid("Menu", "Account preview: {}; vanilla gait, visible-time walk-on and pointer turn", this.s);
      }

      if (var12.visible() && var14.stage() != WindEffect.Type_c.HIDDEN) {
         MenuLayout.Type_c var15 = var12.model();
         int var16 = Math.round(var12.centerX() + (float)var14.xOffset());
         int var17 = Math.max(var12.renderWidth(), (int)Math.ceil(2.0 * AccountCosmeticsRenderer.DAsDouble(var10, var11) * (double)var12.scale()));
         int var18 = var16 - var17 / 2;
         var1.enableScissor(var2.navigation(0).x() + var2.menuWidth() + 8, var15.y(), var2.width(), var15.y() + var15.height() + 8);

         try {
            pAsVoid(var1, (float)var16, var12.floorY(), Math.min((float)var15.width() * 0.3F, var12.scale() * 0.62F), 1.0);
            if (this.SAsBoolean(var9)) {
               SkinTextures var19 = AccountCosmeticsRenderer.DAsSkinTextures(var9.getGameProfile().id(), var4.FAsSkinTextures());
               CosmeticPlayerModel var20 = var19.model() == PlayerSkinType.SLIM ? this.q : this.d;
               var20.jAsVoid(var14.pose());
               LimbData var21 = AccountCosmeticsRenderer.pAsLa();
               if (var21 != null && !KorbloxLegRenderer.DAsBoolean()) {
                  var21 = null;
               }

               var20.mAsVoid(
                  this.r
                     .OAsB1_Type_b(
                        AccountCosmeticsRenderer.TAsIdentifier(var19), var10, var11, AccountCosmeticsRenderer.AAsBoolean(), var21, var14.pose(), this.C
                     )
               );
               var1.addPlayerSkin(
                  var20,
                  var19.body().texturePath(),
                  var12.scale(),
                  0.0F,
                  var14.pose().bodyYaw(),
                  -1.0625F,
                  var18,
                  var15.y(),
                  var18 + var17,
                  var15.y() + var15.height()
               );
            } else {
               int var25 = Math.min(48, var15.width() / 2);
               var4.fAsVoid(var1, var16 - var25 / 2, var15.y() + var15.height() / 3, var25);
            }
         } finally {
            var1.disableScissor();
         }
      }
   }

   public double AAsDouble() {
      return this.D;
   }

   private boolean SAsBoolean(MinecraftClient var1) {
      int var2 = BloomQuality.J;
      if (this.K == var2) {
         return false;
      } else if (this.w == var2 && this.d != null) {
         return true;
      } else {
         try {
            LoadedEntityModels var3 = var1.getLoadedEntityModels();
            this.d = new CosmeticPlayerModel(var3.getModelPart(EntityModelLayers.PLAYER));
            this.q = new CosmeticPlayerModel(var3.getModelPart(new net.minecraft.client.render.entity.model.EntityModelLayer(net.minecraft.util.Identifier.ofVanilla("player_slim"), "main")), true);
            this.C = new PlayerCapeModel(var3.getModelPart(EntityModelLayers.PLAYER_CAPE));
            this.w = var2;
            ClientLog.GAsVoid("Menu", "Account skin preview ready; native wide/slim models and cape, resource generation {}", var2);
            return true;
         } catch (RuntimeException var41) {
            this.d = this.q = null;
            this.C = null;
            this.K = var2;
            ClientLog.lAsVoid("Menu", "Account model bake failed; showing the account portrait until reopen or resource reload", var41);
            return false;
         }
      }
   }

   private static void pAsVoid(DrawContext var0, float var1, int var2, float var3, double var4) {
      var0.getMatrices().pushMatrix();

      try {
         var0.getMatrices().translate(var1, (float)var2);
         var0.getMatrices().scale(1.0F, 0.13F);

         for (int var6 = 8; var6 >= 1; var6--) {
            GuiTextureDrawing.RAsVoid(var0, 0, 0, Math.max(1, Math.round(var3 * (float)var6 / 8.0F)), ColorUtil.pAsInt(110663832, var4));
         }
      } finally {
         var0.getMatrices().popMatrix();
      }
   }

   public void KAsVoid() {
      this.f.lAsVoid();
   }

   public void hAsVoid(boolean var1) {
      this.d = this.q = null;
      this.C = null;
      this.w = this.K = -1;
      this.f.NAsVoid(var1);
   }
}
