package com.larpclient;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

public final class OfferManager {
   private final ArrayDeque<OfferManager.Type_o> U = new ArrayDeque<>();
   private double i = -100.0;
   private Vec3d W;
   private boolean d;
   private List<OfferManager.Type_r> l = List.of();
   private boolean X;
   public double n = 1.2;
   public double R = 2.4;
   public double m = 0.45;
   public int t = 6;
   public int Y = -8798209;
   public boolean E = true;
   public String p = "Original";
   public String h = "Smooth";

   public OfferManager() {
   }

   public boolean sAsBoolean() {
      return this.X;
   }

   public void KAsVoid(Vec3d var1) {
      if (this.d) {
         this.d = false;
         this.aAsVoid();
      }

      ArrayList var2 = new ArrayList();
      this.X = false;

      for (OfferManager.Type_o var4 : this.U) {
         EntityRenderState var5 = var4.state;
         double var6 = var5.x - var1.x;
         double var8 = var5.y - var1.y;
         double var10 = var5.z - var1.z;
         if (!(var6 * var6 + var8 * var8 + var10 * var10 > 2304.0)) {
            double var12 = Math.clamp((MediaSession.RAsDouble() - var4.born) / var4.lifetime, 0.0, 1.0);
            double var14 = this.m * (this.h.equals("Linear") ? 1.0 - var12 : FadeCurve.RAsDouble(1.0 - var12));
            if (!(var14 <= 0.0)) {
               String var17 = this.p;

               CosmeticSubmissionHandler.Type_n var16 = switch (var17) {
                  case "Original" -> CosmeticSubmissionHandler.Type_n.ORIGINAL;
                  case "Silhouette" -> CosmeticSubmissionHandler.Type_n.SILHOUETTE;
                  case "Hologram" -> CosmeticSubmissionHandler.Type_n.HOLOGRAM;
                  default -> CosmeticSubmissionHandler.Type_n.TINT;
               };
               var2.add(new OfferManager.Type_r(var5, var16, this.p.equals("Theme Tint") ? UiStyle.w : this.Y, var14, this.E));
               this.X = this.X | this.E;
            }
         }
      }

      this.l = List.copyOf(var2);
   }

   public void xAsVoid() {
      while (this.U.size() > Math.clamp((long)this.t, 1, 12)) {
         this.U.removeFirst();
      }

      this.U.removeIf(var0 -> MediaSession.dAsDouble() - var0.born >= var0.lifetime);
      MinecraftClient var1 = MinecraftClient.getInstance();
      if (var1.player == null || !var1.player.isAlive()) {
         this.rAsVoid();
      }
   }

   public void pAsVoid() {
      this.d = true;
   }

   private void aAsVoid() {
      MinecraftClient var1 = MinecraftClient.getInstance();
      if (var1.player != null && var1.player.isAlive()) {
         double var2 = MediaSession.dAsDouble();
         Vec3d var4 = var1.player.getEntityPos();
         if (!(var2 - this.i < this.n) && (this.W == null || !(this.W.squaredDistanceTo(var4) < 0.04))) {
            if (this.W == null) {
               this.W = var4;
               this.i = var2;
            } else if (this.W.squaredDistanceTo(var4) > 64.0) {
               this.rAsVoid();
               this.W = var4;
               this.i = var2;
            } else {
               EntityRenderState var5 = CosmeticSubmissionHandler.oAsEntityRenderState(var1.player, 1.0F);
               var5.displayName = null;
               if (var5 instanceof PlayerEntityRenderState var6) {
                  var6.playerName = null;
               }

               var5.shadowPieces.clear();
               var5.outlineColor = 0;

               while (this.U.size() >= Math.clamp((long)this.t, 1, 12)) {
                  this.U.removeFirst();
               }

               this.U.addLast(new OfferManager.Type_o(var5, var2, this.R));
               this.W = var4;
               this.i = var2;
            }
         }
      }
   }

   public boolean GAsBoolean() {
      return this.U.isEmpty();
   }

   public void rAsVoid() {
      this.U.clear();
      this.l = List.of();
      this.X = this.d = false;
      this.W = null;
      this.i = -100.0;
   }

   public void BAsVoid(MatrixStack var1, EffectDrawBuffers var2, boolean var3) {
      long var4 = OpenScreen.RAsLong();

      for (OfferManager.Type_r var7 : this.l) {
         if (!var3 || var7.bloom) {
            CosmeticSubmissionHandler.AAsVoid(var7.state, var1, var2, var7.material, var7.color, var7.alpha * (var3 ? 0.5 : 1.0), false);
         }
      }

      OpenScreen.aAsVoid("GhostEcho", var4);
   }

   private static record Type_o(EntityRenderState state, double born, double lifetime) {
   }

   private static record Type_r(EntityRenderState state, CosmeticSubmissionHandler.Type_n material, int color, double alpha, boolean bloom) {
   }
}
