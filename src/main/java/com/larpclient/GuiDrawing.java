package com.larpclient;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.joml.Vector2f;

public final class GuiDrawing {
   private static final Identifier Q = Identifier.of("larp-client", "textures/gui/glass/fill.png");
   private static final Identifier Y = Identifier.of("larp-client", "textures/gui/glass/rim.png");
   private static final int[] HexEdgeloop = new int[]{0, 48, 80};
   private static final int[] q = new int[]{48, 32, 48};
   static final double MenuPopupProvider = 6.0;

   private GuiDrawing() {
   }

   private static void AAsVoid(DrawContext var0, Identifier var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      var7 = qg.iAsInt(var7);
      if (var4 > 0 && var5 > 0 && var7 >>> 24 != 0) {
         int var8 = Math.min(var6, Math.min(var4, var5) / 2);

         for (int var9 = 0; var9 < 3; var9++) {
            int var10 = var9 == 0 ? var3 : (var9 == 1 ? var3 + var8 : var3 + var5 - var8);
            int var11 = var9 == 1 ? var5 - 2 * var8 : var8;
            if (var11 > 0) {
               for (int var12 = 0; var12 < 3; var12++) {
                  int var13 = var12 == 0 ? var2 : (var12 == 1 ? var2 + var8 : var2 + var4 - var8);
                  int var14 = var12 == 1 ? var4 - 2 * var8 : var8;
                  if (var14 > 0) {
                     var0.drawTexture(
                        RenderPipelines.GUI_TEXTURED,
                        var1,
                        var13,
                        var10,
                        (float)p[var12],
                        (float)p[var9],
                        var14,
                        var11,
                        q[var12],
                        q[var9],
                        128,
                        128,
                        var1.equals(Y) ? bg.pAsInt(var7, var9 == 0 ? 1.0 : (var9 == 1 ? 0.55 : 0.28)) : var7
                     );
                  }
               }
            }
         }
      }
   }

   public static void CAsVoid(DrawContext var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      RoundedShapePipeline.nAsVoid(var0, (float)var1, (float)var2, (float)var3, (float)var4, (float)var5, var6);
   }

   static void KAsVoid(DrawContext var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      AAsVoid(var0, Q, var1, var2, var3, var4, var5, var6);
   }

   static void WAsVoid(DrawContext var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      AAsVoid(var0, Y, var1, var2, var3, var4, var5, var6);
   }

   public static void OAsVoid(DrawContext var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, double var8) {
      CAsVoid(var0, var1, var2, var3, var4, var5, bg.xAsInt(var6, var7, 0.6));
      RoundedShapePipeline.SAsVoid(var0, (float)var1, (float)var2, (float)var3, (float)var4, (float)var5, bg.pAsInt(bd.I, var8), bg.pAsInt(bd.a, var8));
   }

   public static void OAsVoid(DrawContext var0, int var1, int var2, int var3, int var4, int var5, int var6, double var7) {
      OAsVoid(var0, var1, var2, var3, var4, var5, var6, var6, var7);
   }

   public static void gAsVoid(DrawContext var0, int var1, int var2, int var3, int var4) {
      JAsVoid(var0, var1, var2, var3, var4);
      OAsVoid(var0, var1, var2, var3, var4);
   }

   public static void JAsVoid(DrawContext var0, int var1, int var2, int var3, int var4) {
      int var5 = MinecraftClient.getInstance().world != null ? 1 : 0;
      int var6 = var5 != 0 && !xt.zAsBoolean() && BloomQuality.cAsBoolean() ? 1 : 0;
      if (var6 != 0) {
         Vector2f var7 = var0.getMatrices().transformPosition((float)var1, (float)var2, new Vector2f());
         float var8 = (float)var3 * var0.getMatrices().m00();
         float var9 = (float)var3 * var0.getMatrices().m01();
         float var10 = (float)var4 * var0.getMatrices().m10();
         float var11 = (float)var4 * var0.getMatrices().m11();
         BloomQuality.XAsVoid(
            Math.round(var7.x + Math.min(0.0F, var8) + Math.min(0.0F, var10)),
            Math.round(var7.y + Math.min(0.0F, var9) + Math.min(0.0F, var11)),
            Math.round(Math.abs(var8) + Math.abs(var10)),
            Math.round(Math.abs(var9) + Math.abs(var11))
         );
      }

      bAsVoid(var0, var1, var2, var3, var4);
      CAsVoid(var0, var1, var2, var3, var4, bd.q, var5 != 0 && var6 == 0 ? bd.MAsInt() : bd.d);
      if (bd.hAsBoolean()) {
         RoundedShapePipeline.tAsVoid(
            var0, (float)var1, (float)var2, (float)var3, (float)var4 * 0.5F, (float)bd.q, bg.XAsInt(bd.w, 0.07 * bd.ZAsDouble()), bg.XAsInt(bd.w, 0.0)
         );
      }
   }

   public static void fAsVoid(DrawContext var0, int var1, int var2, int var3, int var4) {
      LarpClient var5 = LarpClient.CAsLarpClient();
      int var6 = var5 != null && !var5.fAsBo().yAsN7().Z ? 0 : 1;
      double var7 = var6 != 0 ? 0.5 + 0.5 * Math.sin((double)System.nanoTime() / 1.0E9 * 1.6) : 0.5;
      HAsVoid(var0, (float)var1, (float)var2, (float)var3, (float)var4, (float)bd.q, 14.0F, bg.XAsInt(bd.w, (0.12 + 0.05 * var7) * bd.ZAsDouble()));
   }

   public static void OAsVoid(DrawContext var0, int var1, int var2, int var3, int var4) {
      if (bd.hAsBoolean()) {
         RoundedShapePipeline.SAsVoid(
            var0, (float)var1, (float)var2, (float)var3, (float)var4, (float)bd.q, bg.xAsInt(bd.I, bg.XAsInt(bd.w, 0.5), 0.65 * bd.ZAsDouble()), bd.a
         );
      } else {
         RoundedShapePipeline.SAsVoid(var0, (float)var1, (float)var2, (float)var3, (float)var4, (float)bd.q, bd.I, bd.a);
      }
   }

   public static void DAsVoid(DrawContext var0, int var1, int var2, int var3, int var4) {
      CAsVoid(var0, var1, var2, var3, var4, bd.J, bd.j);
      RoundedShapePipeline.SAsVoid(var0, (float)var1, (float)var2, (float)var3, (float)var4, (float)bd.J, bg.pAsInt(bd.I, 0.8), bg.pAsInt(bd.I, 0.8));
   }

   public static void PAsVoid(DrawContext var0, int var1, int var2, int var3, int var4, int var5, int var6, double var7) {
      if (bd.hAsBoolean() && var7 > 0.0) {
         RoundedShapePipeline.CAsVoid(
            var0,
            (float)var1,
            (float)var2,
            (float)var3,
            (float)var4,
            (float)var5,
            10.0F + 6.0F * (float)var7,
            bg.pAsInt(bd.k, 0.35 * var7),
            bg.pAsInt(bd.k, 0.7 * var7)
         );
      }

      CAsVoid(var0, var1, var2, var3, var4, var5, var6);
      RoundedShapePipeline.SAsVoid(var0, (float)var1, (float)var2, (float)var3, (float)var4, (float)var5, bd.I, bd.I);
   }

   public static void HAsVoid(DrawContext var0, float var1, float var2, float var3, float var4, float var5, float var6, int var7) {
      if (bd.hAsBoolean() && BloomQuality.U != BloomQuality.Type_w.Off) {
         RoundedShapePipeline.CAsVoid(var0, var1, var2, var3, var4, var5, var6, var7, var7);
      }
   }

   public static void aAsVoid(DrawContext var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      if (bd.hAsBoolean()) {
         LarpClient var7 = LarpClient.CAsLarpClient();
         double var8 = var7 != null && !var7.fAsBo().yAsN7().Z ? 0.0 : (double)System.nanoTime() / 1.0E9 / 6.0;
         RoundedShapePipeline.tAsVoid(
            var0, (float)var1, (float)var2, (float)var3, (float)var4, (float)var5, BloomQuality.U == BloomQuality.Type_w.Off ? 0.0F : 6.0F, var8, var6
         );
      }
   }

   public static void TAsVoid(DrawContext var0, int var1, int var2, int var3, int var4, double var5) {
      BAsVoid(var0, var1, var2, var3, var4, bd.hAsBoolean() ? Math.min(8, var4 / 2) : bd.T, var5);
   }

   public static void BAsVoid(DrawContext var0, int var1, int var2, int var3, int var4, int var5, double var6) {
      if (!bd.hAsBoolean()) {
         OAsVoid(var0, var1, var2, var3, var4, var5, bd._Z, 0.5 + 0.6 * var6);
         if (var6 > 0.001) {
            RoundedShapePipeline.SAsVoid(
               var0, (float)var1, (float)var2, (float)var3, (float)var4, (float)var5, bg.XAsInt(bd.u, 0.45 * var6), bg.XAsInt(bd.u, 0.25 * var6)
            );
         }
      } else {
         int var8 = bd.WAsInt(var1, bd._Z);
         if (var6 > 0.001) {
            RoundedShapePipeline.CAsVoid(
               var0, (float)var1, (float)var2, (float)var3, (float)var4, (float)var5, 7.0F, bg.XAsInt(var8, 0.24 * var6), bg.XAsInt(var8, 0.24 * var6)
            );
         }

         CAsVoid(var0, var1, var2, var3, var4, var5, bg.xAsInt(bd._Z, bd.h, 0.5 * var6));
         RoundedShapePipeline.SAsVoid(
            var0,
            (float)var1,
            (float)var2,
            (float)var3,
            (float)var4,
            (float)var5,
            bg.xAsInt(bd.I, bg.XAsInt(var8, 0.9), var6),
            bg.xAsInt(bd.a, bg.XAsInt(var8, 0.9), var6)
         );
      }
   }

   public static void iAsVoid(DrawContext var0, int var1, int var2, int var3, int var4) {
      if (bd.hAsBoolean()) {
         PAsVoid(var0, var1, var2, var3, var4, bd.E, bg.xAsInt(bd.M, bd.j | 0xFF000000, 0.35), 1.0);
      } else {
         CAsVoid(var0, var1, var2, var3, var4, bd.l, bd._C);
         GuiTextureDrawing.oAsVoid(var0, var1, var2 + 4, Math.max(0, var4 - 8), true, bd.I);
      }
   }

   public static void dAsVoid(DrawContext var0, int var1, int var2, int var3, int var4, int var5) {
      if (bd.hAsBoolean()) {
         RoundedShapePipeline.cAsVoid(
            var0, (float)(var1 - 1), (float)(var2 - 1), (float)(var3 + 2), (float)(var4 + 2), (float)(var5 + 1), bg.XAsInt(bd.WAsInt(var1, bd.M), 0.85)
         );
      } else {
         RoundedShapePipeline.SAsVoid(
            var0, (float)(var1 - 1), (float)(var2 - 1), (float)(var3 + 2), (float)(var4 + 2), (float)(var5 + 1), bg.pAsInt(bd.u, 0.55), bg.pAsInt(bd.u, 0.3)
         );
      }
   }

   public static void TAsVoid(DrawContext var0, int var1, int var2, int var3, int var4, double var5, boolean var7) {
      OAsVoid(var0, var1, var2, var3, var4, bd.T, bg.xAsInt(bd.M, bd.h, var7 ? 1.0 : var5), 0.6 + var5 * 0.4);
   }

   public static void qAsVoid(DrawContext var0, int var1, int var2, boolean var3) {
      uAsVoid(var0, var1, var2, var3 ? 1.0 : 0.0);
   }

   public static void uAsVoid(DrawContext var0, int var1, int var2, double var3) {
      FAsVoid(var0, var1, var2, var3, bd.tAsInt(var1), mi.controlInk(bd.m) == -15132391);
   }

   public static void FAsVoid(DrawContext var0, int var1, int var2, double var3, int var5, boolean var6) {
      double var7 = Math.clamp(var3, 0.0, 1.0);
      int var9 = mi.readableAccent(var5, bd.M | 0xFF000000);
      int var10 = mi.controlInk(var9);
      if (bd.hAsBoolean()) {
         var10 = mi.contrast(-1, var9) >= 2.2 ? -1 : -15132388;
         int var17 = var6 ? -2894888 : bg.xAsInt(bd.X | 0xFF000000, bd.u, 0.12);
         int var18 = var6 ? -1 : -3552816;
         String var13 = bd.yAsBd_Type_p().switches();
         switch (var13) {
            case "Square":
               NAsVoid(var0, var1, var2, var7, var9, var17, bg.xAsInt(var18, var10, var7), var6);
               return;
            case "Minimal":
               cAsVoid(var0, var1, var2, var7, var9, var17, var6);
               return;
            default:
               if (var7 > 0.01) {
                  HAsVoid(var0, (float)var1, (float)var2, 24.0F, 13.0F, 6.5F, 5.0F, bg.XAsInt(var9, 0.35 * var7));
               }

               RoundedShapePipeline.nAsVoid(var0, (float)var1, (float)var2, 24.0F, 13.0F, 6.5F, bg.xAsInt(var17, var9, var7));
               if (var7 > 0.01) {
                  RoundedShapePipeline.gAsVoid(var0, (float)var1, (float)var2, 24.0F, 13.0F, 6.5F, bg.XAsInt(bd.Q, 0.0), bg.XAsInt(bd.Q, 0.55 * var7));
               }

               if (var7 < 0.999) {
                  RoundedShapePipeline.SAsVoid(var0, (float)var1, (float)var2, 24.0F, 13.0F, 6.5F, bg.pAsInt(bd.I, 1.0 - var7), bg.pAsInt(bd.a, 1.0 - var7));
               }

               float var19 = (float)(3.15 * (1.0 - Math.abs(var7 * 2.0 - 1.0)));
               float var20 = 9.0F + var19;
               float var15 = (float)(var1 + 2) + (float)(11.0 * var7) - var19 * (float)var7;
               RoundedShapePipeline.CAsVoid(var0, var15, (float)(var2 + 2), var20, 9.0F, 4.5F, 3.0F, 1207959552, 1509949440);
               RoundedShapePipeline.nAsVoid(var0, var15, (float)(var2 + 2), var20, 9.0F, 4.5F, bg.xAsInt(var18, var10, var7));
               if (var6) {
                  RoundedShapePipeline.SAsVoid(var0, var15, (float)(var2 + 2), var20, 9.0F, 4.5F, 469762048, 704643072);
               }
         }
      } else {
         int var11 = bg.xAsInt(var6 ? -9143678 : -12961217, var9, var7);
         OAsVoid(var0, var1 + 1, var2 + 1, 22, 12, 6, var11, 0.35);
         int var12 = var1 + 3 + (int)Math.round(var7 * 10.0);
         CAsVoid(var0, var12, var2 + 3, 8, 8, 4, 939524096);
         CAsVoid(var0, var12, var2 + 3, 8, 8, 4, bg.xAsInt(-1776408, var10, var7));
      }
   }

   private static void NAsVoid(DrawContext var0, int var1, int var2, double var3, int var5, int var6, int var7, boolean var8) {
      if (var3 > 0.01) {
         HAsVoid(var0, (float)var1, (float)var2, 24.0F, 13.0F, 2.5F, 5.0F, bg.XAsInt(var5, 0.3 * var3));
      }

      RoundedShapePipeline.nAsVoid(var0, (float)var1, (float)var2, 24.0F, 13.0F, 2.5F, bg.xAsInt(var6, var5, var3));
      if (var3 < 0.999) {
         RoundedShapePipeline.SAsVoid(var0, (float)var1, (float)var2, 24.0F, 13.0F, 2.5F, bg.pAsInt(bd.I, 1.0 - var3), bg.pAsInt(bd.a, 1.0 - var3));
      }

      float var9 = (float)(var1 + 2) + (float)(11.0 * var3);
      RoundedShapePipeline.CAsVoid(var0, var9, (float)(var2 + 2), 9.0F, 9.0F, 1.5F, 2.5F, 1073741824, 1342177280);
      RoundedShapePipeline.nAsVoid(var0, var9, (float)(var2 + 2), 9.0F, 9.0F, 1.5F, var7);
      if (var8) {
         RoundedShapePipeline.SAsVoid(var0, var9, (float)(var2 + 2), 9.0F, 9.0F, 1.5F, 469762048, 704643072);
      }
   }

   private static void cAsVoid(DrawContext var0, int var1, int var2, double var3, int var5, int var6, boolean var7) {
      RoundedShapePipeline.nAsVoid(var0, (float)(var1 + 1), (float)(var2 + 4), 22.0F, 5.0F, 2.5F, bg.xAsInt(var6, bg.XAsInt(var5, 0.55), var3));
      float var9 = (float)(var1 + 1) + 5.5F + (float)(11.0 * var3);
      RoundedShapePipeline.CAsVoid(var0, var9 - 5.5F, (float)(var2 + 1), 11.0F, 11.0F, 5.5F, 2.5F, 1073741824, 1342177280);
      int var10 = bg.xAsInt(var7 ? -1 : -1776408, var5 | 0xFF000000, var3);
      RoundedShapePipeline.nAsVoid(var0, var9 - 5.5F, (float)(var2 + 1), 11.0F, 11.0F, 5.5F, var10);
      RoundedShapePipeline.nAsVoid(var0, var9 - 2.31F, (float)(var2 + 1) + 5.5F - 2.31F, 4.62F, 4.62F, 2.31F, bg.xAsInt(bg.XAsInt(bd.m, 0.6), -1, var3));
   }

   public static void aAsVoid(DrawContext var0, int var1, int var2, int var3, int var4, int var5) {
      if (var4 > var3) {
         int var6 = Math.min(var3, Math.max(18, var3 * var3 / var4));
         int var7 = (int)((double)(var3 - var6) * (double)var5 / (double)(var4 - var3));
         CAsVoid(var0, var1, var2 + var7, 2, var6, 1, bd.hAsBoolean() ? bg.XAsInt(bd.e, 0.4) : -2139654281);
      }
   }

   public static void vAsVoid(DrawContext var0, int var1, int var2, int var3, int var4, double var5) {
      OAsVoid(var0, var1, var2, var3, var4, bd.p, bg.pAsInt(bd.d, var5), var5 * 0.7);
   }

   public static void vAsVoid(DrawContext var0, int var1, int var2, int var3, int var4, int var5) {
      wAsVoid(var0, var1, var2, var3, var4, bd.l, var5);
   }

   public static void wAsVoid(DrawContext var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      RoundedShapePipeline.SAsVoid(var0, (float)var1, (float)var2, (float)var3, (float)var4, (float)var5, var6, bg.pAsInt(var6, 0.28));
   }

   public static void bAsVoid(DrawContext var0, int var1, int var2, int var3, int var4) {
      uAsVoid(var0, var1, var2, var3, var4, bd.q);
   }

   public static void uAsVoid(DrawContext var0, int var1, int var2, int var3, int var4, int var5) {
      if (bd.hAsBoolean()) {
         RoundedShapePipeline.CAsVoid(var0, (float)var1, (float)var2, (float)var3, (float)var4, (float)var5, 30.0F, bg.pAsInt(bd.k, 0.25), bd.k);
         RoundedShapePipeline.CAsVoid(var0, (float)var1, (float)var2, (float)var3, (float)var4, (float)var5, 6.0F, bg.pAsInt(bd.k, 0.45), bg.pAsInt(bd.k, 0.7));
      } else {
         for (int var6 = 8; var6 >= 2; var6 -= 2) {
            CAsVoid(var0, var1 - var6, var2 - var6 + 5, var3 + var6 * 2, var4 + var6 * 2, bd.q + var6, 150994944);
         }
      }
   }
}
