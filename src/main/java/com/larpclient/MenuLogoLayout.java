package com.larpclient;

import com.larpclient.module.HUD;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.util.Identifier;

public final class MenuLogoLayout {
   public static final int U = 17;
   public static final int L = 172;
   public static final int P = 72;
   public static final int o = 42;
   public static final int w = 6;
   public static final int r = 30;
   private static final int a = 9;
   private static final int k = 12;
   private static final int e = 14;
   private static final int R = 16;
   private static final int m = 11;
   private static final double H = 22.0;
   private static final double M = 4.0;
   private static final double A = 6.0;

   private MenuLogoLayout() {
   }

   public static int xAsInt(int var0) {
      return var0 + 12;
   }

   public static int zAsInt() {
      return -267645934;
   }

   public static int mAsInt() {
      return bd.w;
   }

   public static void aAsVoid(DrawContext var0, String var1, int var2, int var3, int var4, boolean var5) {
      int var6 = xAsInt(var2);
      int var7 = var3 - var6;
      GuiTextureDrawing.CAsVoid(var0, var7, var4, var6, 17, zAsInt());
      yAsVoid(var0, var7, var4, var6, 17, 0);
      GuiTextureDrawing.mAsVoid(var0, var5 ? var3 - 2 : var7, var4, 2, 17, bd.w, bg.xAsInt(bd.w, -1, 0.45));
      ww.aAsVoid(var0, var1, (double)(var7 + (var5 ? 5 : 7)), (double)(var4 + 3), bd.w);
   }

   private static List<MenuLogoLayout.Type_z> OAsList(String var0, String var1, String var2, String var3) {
      HUD var4 = HUD.EAsHUD();
      ArrayList var5 = new ArrayList();
      if (var4 == null || var4.watermarkUser.getValue()) {
         var5.add(new MenuLogoLayout.Type_z(var0, MenuLogoLayout.Type_b.USER));
      }

      if (var4 == null || var4.watermarkIP.getValue()) {
         var5.add(new MenuLogoLayout.Type_z(var1, MenuLogoLayout.Type_b.SERVER));
      }

      if (var4 == null || var4.watermarkFPS.getValue()) {
         var5.add(new MenuLogoLayout.Type_z(var2, MenuLogoLayout.Type_b.FPS));
      }

      if (!var3.isEmpty() && (var4 == null || var4.watermarkTime.getValue())) {
         var5.add(new MenuLogoLayout.Type_z(var3, MenuLogoLayout.Type_b.TIME));
      }

      return var5;
   }

   public static int cAsInt(int... var0) {
      return var0.length == 0 ? 42 : 72 + Arrays.stream(var0).sum() + (var0.length - 1) * 14;
   }

   public static int QAsInt(String var0, String var1, String var2, String var3) {
      return cAsInt(OAsList(var0, var1, var2, var3).stream().mapToInt(MenuLogoLayout.Type_z::width).toArray());
   }

   public static void iAsVoid(DrawContext var0, int var1, int var2, int var3, String var4, String var5, String var6, String var7) {
      HUD var8 = HUD.EAsHUD();
      int var9 = var8 != null && var8.watermarkLogoSide.pAsBoolean("Right") ? 1 : 0;
      double var10 = (double)System.nanoTime() / 1.0E9;
      int var12 = var9 != 0 ? var1 + var3 - 42 : var1;
      GuiTextureDrawing.AAsVoid(var0, (float)var12, (float)var2, 42.0F, 42.0F, 9.0F, zAsInt());
      yAsVoid(var0, var12, var2, 42, 42, 9);
      if (!HudSdfAtlas.NAsBoolean(var0, "logo", (double)(var12 + 5), (double)(var2 + 5), 32.0, -1)) {
         GuiTextureDrawing.AAsVoid(var0, Identifier.of("larp-client", "textures/gui/logo.png"), var12 + 5, var2 + 5, 32, 32, 64, 64);
      }

      List var13 = OAsList(var4, var5, var6, var7);
      if (!var13.isEmpty()) {
         int var14 = var9 != 0 ? var1 : var1 + 42 + 6;
         int var15 = var3 - 42 - 6;
         int var16 = var2 + 6;
         GuiTextureDrawing.AAsVoid(var0, (float)var14, (float)var16, (float)var15, 30.0F, 6.0F, zAsInt());
         yAsVoid(var0, var14, var16, var15, 30, 6);
         int var17 = var14 + 12;
         int var18 = var16 + 8;

         for (MenuLogoLayout.Type_z var20 : (Iterable<MenuLogoLayout.Type_z>) (var13)) {
            int var21 = var17;
            switch (var20.kind()) {
               case USER:
                  if (MinecraftClient.getInstance().player != null) {
                     PlayerSkinDrawer.draw(var0, MinecraftClient.getInstance().player.getSkin(), var17, var16 + 7, 16);
                  }

                  var17 += 22;
                  break;
               case FPS:
                  QAsVoid(var0, var17, var16 + 8, var10);
                  var17 += 17;
            }

            if (var20.kind() == MenuLogoLayout.Type_b.TIME) {
               YAsVoid(var0, var20.label(), var17, var18, var10);
            } else {
               ww.fAsVoid(var0, var20.label(), (double)var17, (double)var18, 13.0, -1);
            }

            var17 = var21 + var20.width() + 14;
         }
      }
   }

   private static void QAsVoid(DrawContext var0, int var1, int var2, double var3) {
      double[] var5 = new double[]{5.2, 7.1, 4.3};
      double[] var6 = new double[]{0.0, 1.7, 3.1};

      for (int var7 = 0; var7 < 3; var7++) {
         int var8 = 4 + (int)Math.round(10.0 * (0.5 + 0.5 * Math.sin(var3 * var5[var7] + var6[var7])));
         RoundedShapePipeline.nAsVoid(var0, (float)(var1 + var7 * 4), (float)(var2 + 14 - var8), 3.0F, (float)var8, 1.0F, bd.w);
      }
   }

   private static void YAsVoid(DrawContext var0, String var1, int var2, int var3, double var4) {
      int var6 = var1.indexOf(58);
      if (var6 < 0) {
         ww.fAsVoid(var0, var1, (double)var2, (double)var3, 13.0, -1);
      } else {
         String var7 = var1.substring(0, var6 + 1);
         double var8 = 0.55 + 0.45 * Math.cos(var4 * Math.PI * 2.0);
         ww.fAsVoid(var0, var1.substring(0, var6), (double)var2, (double)var3, 13.0, -1);
         ww.fAsVoid(var0, ":", (double)(var2 + ww.WAsInt(var1.substring(0, var6), 13.0)), (double)var3, 13.0, bg.pAsInt(-1, var8));
         ww.fAsVoid(var0, var1.substring(var6 + 1), (double)(var2 + ww.WAsInt(var7, 13.0)), (double)var3, 13.0, -1);
      }
   }

   public static void yAsVoid(DrawContext var0, int var1, int var2, int var3, int var4, int var5) {
      kAsVoid(var0, var1, var2, var3, var4, var5, 1.0);
   }

   public static void kAsVoid(DrawContext var0, int var1, int var2, int var3, int var4, int var5, double var6) {
      HUD var8 = HUD.EAsHUD();
      if ((var8 == null || var8.lightSweep()) && !(var6 <= 0.01)) {
         double var9 = (double)System.nanoTime() / 1.0E9 % 6.0 / 4.0;
         float var11 = var0.getMatrices().m00();
         if (!(var9 >= 1.0) && !(var11 <= 0.0F)) {
            double var12 = -22.0 + var9 * ((double)var0.getScaledWindowWidth() + 44.0);
            double var14 = (var12 - (double)var0.getMatrices().m20()) / (double)var11;
            double var16 = 11.0 / (double)var11;
            int var18 = var1 + var5;
            int var19 = var1 + var3 - var5;
            if (!(var14 + var16 <= (double)var18) && !(var14 - var16 >= (double)var19)) {
               int var20 = (int)Math.round(var14 - var16);
               int var21 = Math.max(1, (int)Math.round(var16));
               int var22 = bg.pAsInt(251658239, var6);
               var0.enableScissor(var18, var2, var19, var2 + var4);

               try {
                  RoundedShapePipeline.gAsVoid(var0, (float)var20, (float)var2, (float)var21, (float)var4, 0.0F, var22 & 16777215, var22);
                  RoundedShapePipeline.gAsVoid(var0, (float)(var20 + var21), (float)var2, (float)var21, (float)var4, 0.0F, var22, var22 & 16777215);
               } finally {
                  var0.disableScissor();
               }
            }
         }
      }
   }

   public static int OAsInt(String var0) {
      return 12 + ww.WAsInt("XYZ " + var0, 15.0);
   }

   public static void YAsVoid(DrawContext var0, int var1, int var2, int var3, String var4) {
      GuiTextureDrawing.AAsVoid(var0, (float)var1, (float)var2, (float)var3, 27.0F, 6.0F, zAsInt());
      yAsVoid(var0, var1, var2, var3, 27, 6);
      ww.fAsVoid(var0, "XYZ", (double)(var1 + 6), (double)(var2 + 6), 15.0, bd.w);
      ww.fAsVoid(var0, " " + var4, (double)(var1 + 6 + ww.WAsInt("XYZ", 15.0)), (double)(var2 + 6), 15.0, -1);
   }

   public static String FAsString(double var0) {
      return String.format(Locale.ROOT, "%.2f bps", var0);
   }

   public static void zAsVoid(DrawContext var0, int var1, int var2, rb var3, boolean var4) {
      HUD var5 = HUD.EAsHUD();
      int var6 = var5 == null ? zAsInt() : var5.speedHeader.getValue();
      int var7 = var5 == null ? -1374744817 : var5.speedBackground.getValue();
      int var8 = var5 == null ? bd.w : var5.speedLine.getValue();
      var0.enableScissor(var1, var2 + 32, var1 + 172, var2 + 72);
      GuiTextureDrawing.AAsVoid(var0, (float)var1, (float)var2, 172.0F, 72.0F, 12.0F, var7);
      var0.disableScissor();
      var0.enableScissor(var1, var2, var1 + 172, var2 + 32);
      GuiTextureDrawing.AAsVoid(var0, (float)var1, (float)var2, 172.0F, 72.0F, 12.0F, var6);
      var0.disableScissor();
      yAsVoid(var0, var1, var2, 172, 72, 12);
      RoundedShapePipeline.nAsVoid(var0, (float)(var1 + 2), (float)(var2 + 32), 168.0F, 1.0F, 0.5F, 184549375);
      ww.fAsVoid(var0, "Speed", (double)(var1 + 12), FontPreset.vAsDouble((double)var2, 32.0, 13.0), 13.0, SAsInt(var6));
      ww.aAsVoid(var0, "gauge", var1 + 172 - 26, var2 + 9, 14, bd.w);
      ww.fAsVoid(var0, FAsString(var4 ? 4.3 : var3.IAsDouble()), (double)(var1 + 12), (double)(var2 + 38), 12.8, SAsInt(var7));
      GuiTextureDrawing.CAsVoid(var0, var1 + 10, var2 + 50, 152, 1, bg.pAsInt(var8, 0.15));
      GuiTextureDrawing.CAsVoid(var0, var1 + 86, var2 + 36, 1, 28, bg.pAsInt(var8, 0.15));
      int var9 = var4 ? 40 : Math.min(40, var3.xAsInt());
      float var10 = (float)(var1 + 10);
      float var11 = yAsFloat(var2, KAsDouble(var3, var4, 0, var9));

      for (int var12 = 0; var12 + 1 < var9; var12++) {
         double var13 = KAsDouble(var3, var4, Math.max(0, var12 - 1), var9);
         double var15 = KAsDouble(var3, var4, var12, var9);
         double var17 = KAsDouble(var3, var4, var12 + 1, var9);
         double var19 = KAsDouble(var3, var4, Math.min(var9 - 1, var12 + 2), var9);

         for (int var21 = 1; var21 <= 4; var21++) {
            double var22 = (double)var21 / 4.0;
            float var24 = (float)(var1 + 10) + (float)(((double)var12 + var22) * 152.0 / (double)(var9 - 1));
            float var25 = yAsFloat(var2, o8.SAsDouble(var13, var15, var17, var19, var22));
            var0.getMatrices().pushMatrix();
            var0.getMatrices().translate(var10, var11).rotate((float)Math.atan2((double)(var25 - var11), (double)(var24 - var10)));
            RoundedShapePipeline.nAsVoid(
               var0, 0.0F, 0.0F, (float)((int)Math.ceil(Math.hypot((double)(var24 - var10), (double)(var25 - var11)))), 1.0F, 0.5F, var8
            );
            var0.getMatrices().popMatrix();
            var10 = var24;
            var11 = var25;
         }
      }

      if (var9 < 2) {
         GuiTextureDrawing.CAsVoid(var0, var1 + 10, var2 + 62, 152, 1, var8);
      }
   }

   private static double KAsDouble(rb var0, boolean var1, int var2, int var3) {
      return var1 ? (Math.sin((double)var2 * 0.18) + 1.0) * 2.2 : (var3 == 0 ? 0.0 : var0.mAsDouble(var0.xAsInt() - var3 + var2));
   }

   public static float yAsFloat(int var0, double var1) {
      return (float)(var0 + 62) - (float)(Math.clamp(var1, 0.0, 10.0) / 10.0 * 24.0);
   }

   public static int SAsInt(int var0) {
      double var1 = (double)(var0 >> 16 & 0xFF) * 0.2126 + (double)(var0 >> 8 & 0xFF) * 0.7152 + (double)(var0 & 0xFF) * 0.0722;
      return var1 > 150.0 ? -15197408 : -986637;
   }

   private static enum Type_b {
      USER,
      SERVER,
      FPS,
      TIME;

      private Type_b() {
      }
   }

   private static record Type_z(String label, MenuLogoLayout.Type_b kind) {
      int width() {
         int var1 = ww.WAsInt(this.label, 13.0);

         return switch (this.kind) {
            case USER -> 22 + var1;
            case FPS -> 17 + var1;
            default -> var1;
         };
      }
   }
}
