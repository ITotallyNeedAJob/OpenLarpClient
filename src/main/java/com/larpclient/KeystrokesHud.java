package com.larpclient;

import com.larpclient.module.HUD;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;

public final class KeystrokesHud {
   public static final int M = 21;
   public static final int Q = 4;
   public static final int W = 13;
   public static final int T = 4;
   public static final int ChatPayParser = 10;
   public static final int TextLayout = 7;
   public static final double AuraModule = 34.0;
   public static final double q = 12.0;
   private static final Map<String, Float> SkyChunkCache = new HashMap<>();
   private static final KeystrokesHud.Type_z F = new KeystrokesHud.Type_z();
   private static final KeystrokesHud.Type_z J = new KeystrokesHud.Type_z();
   private static Object A;
   private static long S;

   private KeystrokesHud() {
   }

   public static boolean WAsBoolean() {
      HUD var0 = HUD.EAsHUD();
      return var0 != null && var0.fAsBoolean("keystrokes");
   }

   public static void AAsVoid(int var0) {
      if (WAsBoolean() && MinecraftClient.getInstance().currentScreen == null && var0 <= 1 && var0 >= 0) {
         (var0 == 0 ? F : J).yAsVoid(System.nanoTime());
      }
   }

   public static int cAsInt(boolean var0) {
      return (var0 ? F : J).MAsInt(System.nanoTime());
   }

   public static int uAsInt(HUD var0) {
      return keystrokesPadding(var0) * 2 + 63 + 8;
   }

   public static int QAsInt(HUD var0) {
      int var1 = keystrokesPadding(var0) * 2 + 42 + 4;
      if (var0.keystrokesMouse.getValue()) {
         var1 += 25;
      }

      if (var0.keystrokesSpace.getValue()) {
         var1 += 17;
      }

      return var1;
   }

   private static int keystrokesPadding(HUD var0) {
      return var0.keystrokesPadding.getValue().intValue();
   }

   public static float XAsFloat(float var0, boolean var1, double var2) {
      double var4 = 1.0 - Math.exp(-(var1 ? 34.0 : 12.0) * Math.clamp(var2, 0.0, 0.1));
      return (float)((double)var0 + (double)((float)(var1 ? 1 : 0) - var0) * var4);
   }

   public static void IAsVoid(DrawContext var0, HUD var1, int var2, int var3, boolean var4) {
      MinecraftClient var5 = MinecraftClient.getInstance();
      long var6 = System.nanoTime();
      double var8 = S == 0L ? 0.016666666666666666 : (double)(var6 - S) / 1.0E9;
      S = var6;
      if (A != var5.world) {
         SkyChunkCache.clear();
         F.dAsVoid();
         J.dAsVoid();
         A = var5.world;
      }

      int var10 = uAsInt(var1);
      int var11 = QAsInt(var1);
      GuiTextureDrawing.AAsVoid(
         var0, (float)var2, (float)var3, (float)var10, (float)var11, 8.0F, bg.XAsInt(-16119284, var1.keystrokesBGAlpha.getValue() / 255.0)
      );
      MenuLogoLayout.yAsVoid(var0, var2, var3, var10, var11, 8);
      GameOptions var12 = var5.options;
      boolean var13 = var1.keystrokesArrows.getValue();
      int var14 = var2 + keystrokesPadding(var1);
      int var15 = var3 + keystrokesPadding(var1);
      rAsVoid(var0, var13 ? "↑" : "W", null, PAsBoolean(var12.forwardKey, var4), var14 + 25, var15, 21, 21, var8);
      var15 += 25;
      rAsVoid(var0, var13 ? "←" : "A", null, PAsBoolean(var12.leftKey, var4), var14, var15, 21, 21, var8);
      rAsVoid(var0, var13 ? "↓" : "S", null, PAsBoolean(var12.backKey, var4), var14 + 25, var15, 21, 21, var8);
      rAsVoid(var0, var13 ? "→" : "D", null, PAsBoolean(var12.rightKey, var4), var14 + 50, var15, 21, 21, var8);
      var15 += 25;
      if (var1.keystrokesMouse.getValue()) {
         boolean var19 = var1.keystrokesCPS.getValue();
         rAsVoid(var0, "LMB", var19 ? cAsInt(true) + " CPS" : null, PAsBoolean(var12.attackKey, var4), var14, var15, 33, 21, var8);
         rAsVoid(var0, "RMB", var19 ? cAsInt(false) + " CPS" : null, PAsBoolean(var12.useKey, var4), var14 + 71 - 33, var15, 33, 21, var8);
         var15 += 25;
      }

      if (var1.keystrokesSpace.getValue()) {
         float var22 = oAsFloat("Space", PAsBoolean(var12.jumpKey, var4), var8);
         kAsVoid(var0, var14, var15, 71, 13, var22);
         int var23 = Math.round(21.300001F);
         GuiTextureDrawing.AAsVoid(
            var0, (float)(var14 + (71 - var23) / 2), (float)(var15 + 6 - 1), (float)var23, 2.0F, 1.0F, bg.xAsInt(-7697772, -1, (double)var22)
         );
      }
   }

   private static boolean PAsBoolean(KeyBinding var0, boolean var1) {
      return !var1 && var0.isPressed();
   }

   private static float oAsFloat(String var0, boolean var1, double var2) {
      float var4 = XAsFloat(SkyChunkCache.getOrDefault(var0, 0.0F), var1, var2);
      SkyChunkCache.put(var0, var4);
      return var4;
   }

   private static void kAsVoid(DrawContext var0, int var1, int var2, int var3, int var4, float var5) {
      GuiTextureDrawing.AAsVoid(var0, (float)var1, (float)var2, (float)var3, (float)var4, 4.0F, bg.XAsInt(-1, 0.055 * (double)(1.0F - var5)));
      if (!(var5 <= 0.01F)) {
         RoundedShapePipeline.CAsVoid(
            var0,
            (float)var1,
            (float)var2,
            (float)var3,
            (float)var4,
            4.0F,
            6.0F * var5,
            bg.pAsInt(bd.w, 0.45 * (double)var5),
            bg.pAsInt(bd.w, 0.45 * (double)var5)
         );
         GuiTextureDrawing.AAsVoid(var0, (float)var1, (float)var2, (float)var3, (float)var4, 4.0F, bg.pAsInt(bd.w, 0.9 * (double)var5));
      }
   }

   private static void rAsVoid(DrawContext var0, String var1, String var2, boolean var3, int var4, int var5, int var6, int var7, double var8) {
      float var10 = oAsFloat(var1, var3, var8);
      kAsVoid(var0, var4, var5, var6, var7, var10);
      int var11 = bg.xAsInt(bd.e, -1, (double)var10);
      double var12 = (double)var4 + (double)var6 / 2.0;
      if (var2 == null) {
         ww.fAsVoid(var0, var1, var12 - (double)ww.WAsInt(var1, 10.0) / 2.0, (double)var5 + (double)(var7 - 10) / 2.0, 10.0, var11);
      } else {
         double var14 = (double)var5 + (double)(var7 - 10 - 7) / 2.0;
         ww.fAsVoid(var0, var1, var12 - (double)ww.WAsInt(var1, 10.0) / 2.0, var14, 10.0, var11);
         ww.fAsVoid(var0, var2, var12 - (double)ww.WAsInt(var2, 7.0) / 2.0, var14 + 10.0, 7.0, bg.xAsInt(-7697772, -1, (double)var10));
      }
   }

   public static final class Type_z {
      public static final long W = 1000000000L;
      private final ArrayDeque<Long> PetEffectRenderer = new ArrayDeque<>();

      public Type_z() {
      }

      public void yAsVoid(long var1) {
         this.a.addLast(var1);
         this.gAsVoid(var1);
      }

      public int MAsInt(long var1) {
         this.gAsVoid(var1);
         return this.a.size();
      }

      public void dAsVoid() {
         this.a.clear();
      }

      private void gAsVoid(long var1) {
         while (!this.a.isEmpty() && var1 - this.a.peekFirst() > 1000000000L) {
            this.a.removeFirst();
         }
      }
   }
}
