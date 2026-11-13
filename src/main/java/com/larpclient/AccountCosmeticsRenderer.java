package com.larpclient;

import com.larpclient.module.Capes;
import com.larpclient.module.ChinaHat;
import com.larpclient.module.HeadlessKorblox;
import com.larpclient.module.SkinChanger;
import com.larpclient.module.Wings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.render.state.special.PlayerSkinGuiElementRenderState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.render.entity.model.PlayerCapeModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.MatrixStack.Entry;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.entity.player.SkinTextures;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;

public final class AccountCosmeticsRenderer {
   private static final int X = 15728880;
   static final float T = 1.85F;
   static final float H = 0.15F;
   static final float v = 1.4F;
   static final float W = 0.15F;
   private final ro.Type_j r = new ro.Type_j();
   private DragonWingRenderer.Type_q J;
   private String I = "";
   private static int c = -1;
   private static final ChinaHatFireRenderer S = new ChinaHatFireRenderer(0L);

   public AccountCosmeticsRenderer() {
   }

   static ChinaHat.Type_a AAsChinaHat_Type_a() {
      ChinaHat var0 = ChinaHat.uAsChinaHat();
      return var0 == null ? null : var0.VAsChinaHat_Type_a();
   }

   static Wings.Type_n oAsWings_Type_n() {
      Wings var0 = Wings.tAsWings();
      return var0 == null ? null : var0.PAsWings_Type_n();
   }

   static boolean AAsBoolean() {
      HeadlessKorblox var0 = HeadlessKorblox.lAsHeadlessKorblox();
      return var0 != null && var0.AAsBoolean();
   }

   static la pAsLa() {
      HeadlessKorblox var0 = HeadlessKorblox.lAsHeadlessKorblox();
      return var0 == null ? null : var0.uAsLa();
   }

   static double jAsDouble(ChinaHat.Type_a var0, Wings.Type_n var1) {
      double var2 = var0 == null ? 0.0 : var0.rise() + 0.05;
      if (var1 != null) {
         var2 = Math.max(var2, (double)((var1.dragon() ? 0.15F : 0.15F) * var1.size()));
      }

      return var2;
   }

   static double DAsDouble(ChinaHat.Type_a var0, Wings.Type_n var1) {
      double var2 = var0 == null ? 0.0 : var0.radius() + 0.05;
      if (var1 != null) {
         var2 = Math.max(var2, (double)((var1.dragon() ? 1.4F : 1.85F) * var1.size()));
      }

      return var2;
   }

   static SkinTextures DAsSkinTextures(UUID var0, SkinTextures var1) {
      SkinTextures var2 = ml.HAsSkinTextures(var0, var1);
      SkinChanger var3 = SkinChanger.pAsSkinChanger();
      if (var3 != null) {
         var2 = var3.zAsSkinTextures(var2);
      }

      Capes var4 = Capes.gAsCapes();
      return var4 == null ? var2 : var4.VAsSkinTextures(var2);
   }

   static void DAsVoid(PlayerEntityRenderState var0, w9.Type_w var1) {
      float var2 = var1.walkSpeed() / 4.0F;
      var0.field_53537 = Math.clamp(var2 * 300.0F, 0.0F, 150.0F);
      var0.field_53538 = 0.0F;
      float var3 = var1.walkPosition() * 0.15F;
      float var4 = Math.min(0.1F, var2);
      var0.field_53536 = (float)Math.sin((double)(var3 * 6.0F)) * 32.0F * var4;
   }

   static Identifier TAsIdentifier(SkinTextures var0) {
      return var0.cape() != null && MinecraftClient.getInstance().options.isPlayerModelPartEnabled(PlayerModelPart.CAPE) ? var0.cape().texturePath() : null;
   }

   AccountCosmeticsRenderer.Type_b OAsB1_Type_b(Identifier var1, ChinaHat.Type_a var2, Wings.Type_n var3, w9.Type_w var4, PlayerCapeModel var5) {
      return this.OAsB1_Type_b(var1, var2, var3, false, null, var4, var5);
   }

   AccountCosmeticsRenderer.Type_b OAsB1_Type_b(
      Identifier var1, ChinaHat.Type_a var2, Wings.Type_n var3, boolean var4, la var5, w9.Type_w var6, PlayerCapeModel var7
   ) {
      if (var7 == null) {
         var1 = null;
      }

      float var8 = var6.ageInTicks();
      this.r.cAsVoid(var8, false, var6.walkSpeed(), false, false, Vec3d.ZERO);
      DragonWingRenderer.Type_n var9 = null;
      if (var3 != null && var3.dragon()) {
         if (this.J == null) {
            this.J = DragonWingRenderer.Type_q.XAsMe_Type_q();
         }

         this.r.OAsVoid(DragonWingRenderer.MAsFloat(this.r.aAsFloat(), 0.0F, 0.0F, 0.0F));
         var9 = DragonWingRenderer.NAsMe_Type_n(this.r.ZAsFloat(), this.r.aAsFloat(), 0.0F, 0.0F, 0.0F);
      }

      String var10 = (var1 == null ? "-" : var1)
         + "|"
         + (var2 == null ? "-" : var2.label())
         + "|"
         + (var3 == null ? "-" : (var3.dragon() ? "dragon" : "luminous"))
         + "|"
         + var4
         + "|"
         + (var5 == null ? "-" : var5.legs());
      if (!var10.equals(this.I)) {
         this.I = var10;
         ClientLog.GAsVoid(
            "Menu",
            "Account model cosmetics: cape={}, hat={}, wings={}, headless={}, korblox={}",
            var1 == null ? "none" : var1,
            var2 == null ? "none" : var2.label(),
            var3 == null ? "none" : (var3.dragon() ? "Ender Dragon" : "Luminous"),
            var4,
            var5 == null ? "none" : (var5.right() && var5.left() ? "both legs" : (var5.right() ? "right leg" : "left leg"))
         );
      }

      if (var1 == null && var2 == null && var3 == null && !var4 && var5 == null) {
         return null;
      } else {
         int var11 = var3 == null ? 0 : Wings.OAsInt(var3.first(), var3.second(), (double)var8 / 20.0);
         int var12 = var3 == null ? 0 : Wings.DAsInt(var3.first(), var3.second(), (double)var8 / 20.0);
         return new AccountCosmeticsRenderer.Type_b(var7, var1, var2, var3, var11, var12, this.J, var9, var8, this.r.aAsFloat(), var4, var5);
      }
   }

   public static void TAsVoid(PlayerSkinGuiElementRenderState var0, Matrix4fc var1, MatrixStack var2, Immediate var3) {
      if (var0.playerModel() instanceof CosmeticPlayerModel var4 && var4.HAsB1_Type_b() != null) {
         int var13 = BloomQuality.J;
         if (c != var13) {
            AccountCosmeticsRenderer.Type_b var6 = var4.HAsB1_Type_b();

            try {
               if (var6.cape() != null) {
                  var6.capeModel().setAngles(var4.QAsPlayerEntityRenderState());
                  var6.capeModel().render(var2, var3.getBuffer(RenderLayers.entitySolid(var6.cape())), 15728880, OverlayTexture.DEFAULT_UV);
                  var3.draw();
               }

               la var7 = var6.korblox();
               if (var7 != null) {
                  if (var7.right()) {
                     KorbloxLegRenderer.xAsVoid(var2, var3, var4.tAsModelPart(), var7, 15728880, OverlayTexture.DEFAULT_UV, false);
                  }

                  if (var7.left()) {
                     KorbloxLegRenderer.xAsVoid(var2, var3, var4.PAsModelPart(), var7, 15728880, OverlayTexture.DEFAULT_UV, true);
                  }
               }

               Wings.Type_n var8 = var6.wings();
               if (var8 != null && var8.dragon()) {
                  MatrixStack var9 = new MatrixStack();
                  var9.peek().copy(var2.peek());
                  var4.vAsModelPart().applyTransform(var9);
                  DragonWingRenderer.Type_q var10 = var6.dragon();
                  DragonWingRenderer.Type_n var11 = var6.angles();
                  var10.eAsVoid(var11);
                  var10.mAsVoid(var9, var3.getBuffer(DragonWingRenderer.mAsRenderLayer(var8.dragonColor())), var8.size(), false, 15728880, var8.dragonColor());
                  var3.draw();
               }

               AccountCosmeticsRenderer.Type_d var14 = new AccountCosmeticsRenderer.Type_d();
               ZAsVoid(var14, var4, new Matrix4f(var1).invert().mul(var2.peek().getPositionMatrix()), var6);
               MatrixStack var15 = new MatrixStack();
               var15.peek().getPositionMatrix().set(var1);
               var14.PAsVoid(var3, var15);
            } catch (RuntimeException var121) {
               c = var13;
               ClientLog.lAsVoid("Menu", "Account model cosmetics submission failed; body only until resource reload", var121);
            }
         }
      }
   }

   static void ZAsVoid(AccountCosmeticsRenderer.Type_d var0, CosmeticPlayerModel var1, Matrix4f var2, AccountCosmeticsRenderer.Type_b var3) {
      ChinaHat.Type_a var4 = var3.hat();
      if (var4 != null) {
         MatrixStack var5 = new MatrixStack();
         var5.peek().getPositionMatrix().set(var2);
         var1.XAsModelPart().applyTransform(var5);
         var0.blend(x4.Type_g.ALPHA);
         HornsMeshRenderer.Type_s var6 = var4.horns() != null ? HornsMeshRenderer.RAsM5_Type_s() : null;
         if (var6 != null) {
            float var7 = (float)var4.radius();
            Vector3f var8 = var2.transformDirection(0.0F, -1.0F, 0.0F, new Vector3f()).normalize();
            Vector3f var9 = var5.peek()
               .getPositionMatrix()
               .transformPosition(0.0F, -0.5F, var6.centerZ() * var7, new Vector3f())
               .add(new Vector3f(var8).mul(0.24F * var7));
            S.IAsVoid(
               (double)var3.age() / 20.0,
               (double)var9.x,
               (double)var9.y,
               (double)var9.z,
               (double)var8.x,
               (double)var8.y,
               (double)var8.z,
               (double)var7 * var4.horns().size(),
               var4.horns().heat()
            );
            var0.OAsVoid(var5.peek().getPositionMatrix(), var4.radius(), var4.horns(), S);
         } else if (var4.accessory() != null) {
            List var11 = HatMeshLoader.aAsList(var4.accessory());
            int var12 = !var11.isEmpty() && BitcoinPetMesh.nAsBoolean("Menu") ? 1 : 0;
            HatMeshLoader.gAsVoid(var0, var5.peek().getPositionMatrix(), var4.accessory(), var4.radius(), (double)(var4.top() >>> 24) / 255.0, var12 == 0);
            if (var12 != 0) {
               for (HatMeshLoader.Type_a var10 : (Iterable<HatMeshLoader.Type_a>) (var11)) {
                  var0.JAsVoid(var10.coin(), HatMeshLoader.kAsMatrix4f(var5.peek().getPositionMatrix(), var10, var4.radius(), new Matrix4f()));
               }
            }
         } else if (var4.kippah()) {
            xh.YAsVoid(var0, var5.peek().getPositionMatrix(), var4.radius(), var4.height(), var4.offset(), var4.top(), var4.rim(), var4.outline());
         } else {
            mo.JAsVoid(var0, var5.peek().getPositionMatrix(), var4.radius(), var4.height(), var4.offset(), var4.top(), var4.rim(), var4.outline());
         }
      }

      if (var3.wings() != null && !var3.wings().dragon()) {
         MAsVoid(var0, var1, var2, var3, var3.wings());
      }
   }

   private static void MAsVoid(
      AccountCosmeticsRenderer.Type_d var0, CosmeticPlayerModel var1, Matrix4f var2, AccountCosmeticsRenderer.Type_b var3, Wings.Type_n var4
   ) {
      MatrixStack var5 = new MatrixStack();
      var5.peek().getPositionMatrix().set(var2);
      var1.vAsModelPart().applyTransform(var5);
      ro.Type_h var6 = ro.rAsRo_Type_h(0.0F, 0.0F, 0.0F, 0.0F);
      ro.Type_v var7 = ro.vAsRo_Type_v(var3.age(), var3.walking(), 0.0F, 0.0F);
      ro.Type_r[] var8 = new ro.Type_r[2];
      int var9 = -1;

      for (int var10 = 0; var9 <= 1; var10++) {
         Matrix4f var11 = new Matrix4f(var5.peek().getPositionMatrix()).mul(ro.RAsMatrix4f(var6, var3.age(), var3.walking(), var4.size(), var9, false));
         var8[var10] = ro.gAsRo_Type_r(Vec3d.ZERO, var11, var9, var7, var3.primary(), var3.secondary(), var4.size());
         var9 += 2;
      }

      for (ro.Type_x var12 : ro.Type_x.values()) {
         for (ro.Type_r var16 : var8) {
            ro.vAsVoid(var0, var16, var12);
         }
      }
   }

   static record Type_b(
      PlayerCapeModel capeModel,
      Identifier cape,
      ChinaHat.Type_a hat,
      Wings.Type_n wings,
      int primary,
      int secondary,
      DragonWingRenderer.Type_q dragon,
      DragonWingRenderer.Type_n angles,
      float age,
      float walking,
      boolean headless,
      la korblox
   ) {
   }

   static final class Type_d implements o_ {
      private final List<AccountCosmeticsRenderer.Type_d.Type_x> E = new ArrayList<>();
      private final List<AccountCosmeticsRenderer.Type_d.Type_z> k = new ArrayList<>();
      private AccountCosmeticsRenderer.Type_d.Type_f K;
      private final Vector3f z = new Vector3f();
      private x4.Type_g a = x4.Type_g.ALPHA;
      private x4.Type_g s;
      private boolean L;
      private boolean b;
      private float[] l = new float[768];
      private int[] t = new int[256];
      private int c;

      Type_d() {
      }

      @Override
      public void blend(x4.Type_g var1) {
         this.a = var1;
      }

      @Override
      public void solid(boolean var1) {
         this.L = var1;
      }

      void JAsVoid(int var1, Matrix4f var2) {
         this.k.add(new AccountCosmeticsRenderer.Type_d.Type_z(var1, var2));
      }

      void OAsVoid(Matrix4f var1, double var2, ChinaHat.Type_y var4, ChinaHatFireRenderer var5) {
         this.K = new AccountCosmeticsRenderer.Type_d.Type_f(new Matrix4f(var1), var2, var4, var5);
      }

      @Override
      public void modelVertex(Matrix4f var1, float var2, float var3, float var4, int var5) {
         var1.transformPosition(var2, var3, var4, this.z);
         this.vertex((double)this.z.x, (double)this.z.y, (double)this.z.z, var5);
      }

      @Override
      public void vertex(double var1, double var3, double var5, int var7) {
         if (this.s != this.a || this.b != this.L) {
            this.RAsVoid();
            this.s = this.a;
            this.b = this.L;
         }

         if (this.c == this.t.length) {
            this.t = Arrays.copyOf(this.t, this.c * 2);
            this.l = Arrays.copyOf(this.l, this.c * 6);
         }

         this.l[this.c * 3] = (float)var1;
         this.l[this.c * 3 + 1] = (float)var3;
         this.l[this.c * 3 + 2] = (float)var5;
         this.t[this.c++] = var7;
      }

      @Override
      public void ribbon(Vec3d var1, Vec3d var2, double var3, int var5, int var6) {
         double var7 = var2.x - var1.x;
         double var9 = var2.y - var1.y;
         double var11 = var2.z - var1.z;
         if (!(var7 * var7 + var9 * var9 + var11 * var11 < 1.0E-10)) {
            double var13 = Math.hypot(var7, var9);
            double var15 = var13 < 1.0E-8 ? 0.5 : var9 / var13 / 2.0;
            double var17 = var13 < 1.0E-8 ? 0.0 : -var7 / var13 / 2.0;
            this.vertex(var1.x + var15 * var3, var1.y + var17 * var3, var1.z, var5);
            this.vertex(var2.x + var15 * var3, var2.y + var17 * var3, var2.z, var6);
            this.vertex(var2.x - var15 * var3, var2.y - var17 * var3, var2.z, var6);
            this.vertex(var1.x - var15 * var3, var1.y - var17 * var3, var1.z, var5);
         }
      }

      List<Vector3f> aAsList() {
         this.RAsVoid();
         ArrayList var1 = new ArrayList();

         for (AccountCosmeticsRenderer.Type_d.Type_x var3 : this.E) {
            for (int var4 = 0; var4 < var3.colors.length; var4++) {
               var1.add(new Vector3f(var3.points[var4 * 3], var3.points[var4 * 3 + 1], var3.points[var4 * 3 + 2]));
            }
         }

         return var1;
      }

      private void RAsVoid() {
         if (this.s != null && this.c > 0) {
            this.E.add(new AccountCosmeticsRenderer.Type_d.Type_x(this.s, this.b, Arrays.copyOf(this.l, this.c * 3), Arrays.copyOf(this.t, this.c)));
         }

         this.c = 0;
      }

      void PAsVoid(Immediate var1, MatrixStack var2) {
         this.RAsVoid();

         for (AccountCosmeticsRenderer.Type_d.Type_x var4 : this.E) {
            VertexConsumer var5 = var1.getBuffer(var4.solid ? EffectGeometryRenderer.pAsRenderLayer() : EffectGeometryRenderer.fAsRenderLayer(var4.blend));

            for (int var6 = 0; var6 < var4.colors.length; var6++) {
               var5.vertex(var2.peek(), var4.points[var6 * 3], var4.points[var6 * 3 + 1], var4.points[var6 * 3 + 2]).color(var4.colors[var6]);
            }

            var1.draw();
         }

         this.E.clear();
         BitcoinMeshReader.Type_g var12 = BitcoinPetMesh.TAsQ6_Type_g();
         if (var12 != null) {
            for (AccountCosmeticsRenderer.Type_d.Type_z var15 : this.k) {
               BitcoinPetMesh.Type_w var17 = BitcoinPetMesh.h.get(var15.coin());
               VertexConsumer var7 = var1.getBuffer(var17.type());
               Matrix4f var8 = new Matrix4f(var2.peek().getPositionMatrix()).mul(var15.transform());
               float[] var9 = var12.vertices();
               int[] var10 = var12.colors();

               for (int var11 = 0; var11 < var10.length; var11++) {
                  var7.vertex(var8, var9[var11 * 5], var9[var11 * 5 + 1], var9[var11 * 5 + 2])
                     .texture(var9[var11 * 5 + 3], var9[var11 * 5 + 4])
                     .color(var10[var11]);
               }

               var1.draw();
            }
         }

         this.k.clear();
         AccountCosmeticsRenderer.Type_d.Type_f var14 = this.K;
         this.K = null;
         HornsMeshRenderer.Type_s var16 = HornsMeshRenderer.RAsM5_Type_s();
         if (var14 != null && var16 != null && HornsMeshRenderer.XAsBoolean(var14.look().horn(), var14.look().fire(), var14.look().core())) {
            HornsMeshRenderer.sAsVoid(
               var1.getBuffer(HornsMeshRenderer.oAsRenderLayer()), new Matrix4f(var2.peek().getPositionMatrix()).mul(var14.head()), var16, var14.scale()
            );
            var1.draw();
            Entry var18 = var2.peek();
            VertexConsumer var19 = var1.getBuffer(ChinaHatFireRenderer.UAsRenderLayer());
            var14.fire()
               .RAsVoid(
                  (var2x, var4x, var6x, var8x, var10x, var12x, var14x) -> {
                     float var15x = (float)Math.cos(var12x);
                     float var16x = (float)Math.sin(var12x);
                     float var17x = (float)var8x;
                     float var18x = (float)(var8x * var10x);
                     float var19x = (float)var2x;
                     float var20 = (float)var4x;
                     float var21 = (float)var6x;

                     for (int var22 = 0; var22 < 4; var22++) {
                        float var23 = var22 != 0 && var22 != 3 ? 1.0F : -1.0F;
                        float var24 = var22 < 2 ? -1.0F : 1.0F;
                        var19.vertex(
                              var18,
                              var19x + var23 * var17x * var15x - var24 * var18x * var16x,
                              var20 + var23 * var17x * var16x + var24 * var18x * var15x,
                              var21
                           )
                           .texture((var23 + 1.0F) * 0.5F, (var24 + 1.0F) * 0.5F)
                           .color(var14x);
                     }
                  },
                  var14.look().fire(),
                  var14.look().core(),
                  1.0
               );
            var1.draw();
         }
      }

      private static record Type_f(Matrix4f head, double scale, ChinaHat.Type_y look, ChinaHatFireRenderer fire) {
      }

      private static record Type_x(x4.Type_g blend, boolean solid, float[] points, int[] colors) {
      }

      private static record Type_z(int coin, Matrix4f transform) {
      }
   }
}
