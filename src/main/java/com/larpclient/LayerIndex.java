package com.larpclient;

import com.larpclient.module.ViewModel;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Hand;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public final class LayerIndex {
   private static final long PriceDetector = 250000000L;
   private static final LayerIndex.Type_p[] AuraModule = new LayerIndex.Type_p[]{new LayerIndex.Type_p(), new LayerIndex.Type_p()};
   private static float s;
   private static float d;
   private static int X;
   private static int E;
   private static long Z;

   public static boolean nAsBoolean() {
      MinecraftClient var0 = MinecraftClient.getInstance();
      ViewModel var1 = ViewModel.qAsViewModel();
      return var1 != null && var1.isRuntimeActive() && var0.player != null && var0.currentScreen != null && var0.currentScreen.getClass() == ChatScreen.class;
   }

   public static void kAsVoid() {
      if (nAsBoolean()) {
         MinecraftClient var0 = MinecraftClient.getInstance();
         Window var1 = var0.getWindow();
         float var2 = w3.CAsFloat();
         if (!(var2 <= 0.0F) && var1.getFramebufferHeight() > 0) {
            float var3 = (float)Math.tan(Math.toRadians((double)var2) / 2.0);
            SahurDanceEngine = 1.0F / var3;
            SkyChunkCache = SahurDanceEngine * (float)var1.getFramebufferHeight() / (float)var1.getFramebufferWidth();
            X = var1.getScaledWidth();
            E = var1.getScaledHeight();
            Z++;
         }
      }
   }

   public static void AAsVoid(Hand var0, boolean var1, MatrixStack var2, ItemRenderState var3) {
      if (nAsBoolean() && SahurDanceEngine != 0.0F) {
         Matrix4f var4 = new Matrix4f(RenderSystem.getModelViewStack()).mul(var2.peek().getPositionMatrix());
         LayerIndex.Type_p var5 = wAsLi_Type_p(var0, var1);
         Vector3f var6 = new Vector3f();
         var3.load(var3x -> var5.PAsVoid(var4.transformPosition(var3x, var6)));
      }
   }

   public static void WAsVoid(Hand var0, boolean var1, MatrixStack var2, ModelPart var3) {
      if (nAsBoolean() && SahurDanceEngine != 0.0F) {
         Matrix4f var4 = new Matrix4f(RenderSystem.getModelViewStack());
         LayerIndex.Type_p var5 = wAsLi_Type_p(var0, var1);
         MatrixStack var6 = new MatrixStack();
         var6.multiplyPositionMatrix(var2.peek().getPositionMatrix());
         Vector3f var7 = new Vector3f();
         var3.collectVertices(var6, var3x -> var5.PAsVoid(var4.transformPosition(var3x, var7)));
      }
   }

   public static void dAsVoid(Hand var0, boolean var1, MatrixStack var2, float[] var3, int var4) {
      if (nAsBoolean() && SahurDanceEngine != 0.0F) {
         Matrix4f var5 = new Matrix4f(RenderSystem.getModelViewStack()).mul(var2.peek().getPositionMatrix());
         LayerIndex.Type_p var6 = wAsLi_Type_p(var0, var1);
         Vector3f var7 = new Vector3f();

         for (int var8 = 0; var8 < var3.length; var8 += 8) {
            var6.PAsVoid(var5.transformPosition((float)var4 * var3[var8], var3[var8 + 1], var3[var8 + 2], var7));
         }
      }
   }

   public static LayerIndex.Type_w YAsLi_Type_w(Hand var0) {
      LayerIndex.Type_p var1 = x[var0.ordinal()];
      if (var1.d != 0 && System.nanoTime() - var1.v <= 250000000L) {
         double var2 = var1.B / (double)var1.d;
         int var4 = (int)Math.floor(var1.I);
         int var5 = (int)Math.floor(var1.E);
         return new LayerIndex.Type_w(
            var0,
            var1.U,
            var4,
            var5,
            Math.max(1, (int)Math.ceil(var1.Y) - var4),
            Math.max(1, (int)Math.ceil(var1.s) - var5),
            2.0 * var2 / (double)(SkyChunkCache * (float)X),
            2.0 * var2 / (double)(SahurDanceEngine * (float)E)
         );
      } else {
         return null;
      }
   }

   private static LayerIndex.Type_p wAsLi_Type_p(Hand var0, boolean var1) {
      LayerIndex.Type_p var2 = x[var0.ordinal()];
      if (var2.o != Z) {
         var2.BAsVoid(Z, var1);
      }

      var2.v = System.nanoTime();
      return var2;
   }

   private LayerIndex() {
   }

   private static final class Type_p {
      long AuctionNotifier = -1L;
      long v;
      boolean U;
      int d;
      double I;
      double E;
      double Y;
      double s;
      double B;

      private Type_p() {
      }

      void BAsVoid(long var1, boolean var3) {
         this.o = var1;
         this.U = var3;
         this.d = 0;
         this.B = 0.0;
         this.I = this.E = Double.MAX_VALUE;
         this.Y = this.s = -Double.MAX_VALUE;
      }

      void PAsVoid(Vector3fc var1) {
         if (!(var1.z() > -0.01F)) {
            float var2 = -var1.z();
            double var3 = (double)((LayerIndex.s * var1.x() / var2 + 1.0F) / 2.0F * (float)LayerIndex.X);
            double var5 = (double)((1.0F - LayerIndex.d * var1.y() / var2) / 2.0F * (float)LayerIndex.E);
            this.I = Math.min(this.I, var3);
            this.Y = Math.max(this.Y, var3);
            this.E = Math.min(this.E, var5);
            this.s = Math.max(this.s, var5);
            this.B += (double)var2;
            this.d++;
         }
      }
   }

   public static record Type_w(Hand hand, boolean left, int x, int y, int width, int height, double unitsPerPixelX, double unitsPerPixelY) {
      public boolean contains(double var1, double var3) {
         return var1 >= (double)this.x && var3 >= (double)this.y && var1 < (double)(this.x + this.width) && var3 < (double)(this.y + this.height);
      }
   }
}
