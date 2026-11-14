package com.larpclient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;

public class NotificationHud implements HudElement {
   private static final NotificationHud n = new NotificationHud();
   private final List<NotificationHud.Type_m> j = new ArrayList<>();
   private static final int v = 5;
   private static final int r = 7;
   private static final int T = 260;
   private static final int F = 10;
   public static final int K = 30;
   private static final int G = 10;
   private static final int L = 52;
   private static final int S = 14;
   private static final int i = 5;
   private static final int Z = 16;
   private static final int H = 12;
   private static final int D = 7;
   private static final int P = 20;
   private static final int M = 30;
   private static final int y = 5;
   private static final double g = 12.0;
   private static final double J = 9.0;
   private static final int B = 330;
   private static final int U = 280;
   private static final int w = 240;
   private static final int h = 8;
   private static final int X = 14;
   private static final int E = -603321844;
   private static final int t = -1;
   private static final int a = -6908266;
   private static final int b = -5592406;
   private static final double p = 0.58;
   public static final String O = "Enabled";
   public static final String W = "Disabled";
   private static final long A = 2000L;

   public NotificationHud() {
   }

   public static NotificationHud GAsW8() {
      return n;
   }

   public void nAsVoid() {
      HudElementRegistry.addLast(Identifier.of("larp-client", "notificationmanager"), this);
   }

   public void RAsVoid(String var1, String var2, NotificationHud.Type_u var3, long var4) {
      Pets var6 = Pets.VAsPets();
      if (var6 == null || !var6.sAsBoolean(var1, var2, var3, var4)) {
         this.oAsVoid(var1, var2, var3, var4);
      }
   }

   public void oAsVoid(String var1, String var2, NotificationHud.Type_u var3, long var4) {
      this.rAsVoid(new NotificationHud.Type_m(var1, var2, var3, Math.max(0L, var4)));
   }

   public void MAsVoid(String var1, String var2, NotificationHud.Type_u var3) {
      this.RAsVoid(var1, var2, var3, 4000L);
   }

   public void KAsVoid(String var1, boolean var2) {
      this.oAsVoid(var2 ? "Enabled" : "Disabled", var1, NotificationHud.Type_u.INFO, 2000L);
   }

   private void rAsVoid(NotificationHud.Type_m var1) {
      this.j.add(var1);
      int var2 = 0;

      for (int var3 = this.j.size() - 5; var2 < var3; var2++) {
         this.j.get(var2).OAsVoid();
      }

      while (this.j.size() > 10) {
         this.j.removeFirst();
      }
   }

   public void HAsVoid(String var1) {
      this.j.removeIf(var1xx -> var1xx.Z.equals(var1));
      Pets var2 = Pets.VAsPets();
      if (var2 != null) {
         var2.IAsVoid(var1);
      }
   }

   public void render(DrawContext var1, RenderTickCounter var2) {
      MinecraftClient var3 = MinecraftClient.getInstance();
      HudRenderer.wAsVoid(var1);
      if (var3.player != null && !var3.options.hudHidden && !ModuleSearchPanel.lAsBoolean(var3.currentScreen)) {
         if (var3.currentScreen instanceof HudEditorScreen var4 && !var4.pAsBoolean()) {
            return;
         }

         SharedRenderEffects.HAsVoid(var1);
         int var16 = var3.getWindow().getScaledWidth();
         int var17 = var3.getWindow().getScaledHeight();
         HudRenderer.Type_m var6 = HudRenderer.QAsLp_Type_m("notifications");
         var6.v = 260;
         var6.y = 178;
         var6.PAsVoid(var16, var17);
         Iterator var7 = this.j.iterator();
         int var8 = 0;

         while (var7.hasNext()) {
            NotificationHud.Type_m var9 = (NotificationHud.Type_m)var7.next();
            var9.F.YAsDouble(1.0, 330, WeatherRenderer.Type_e.BACK_OUT);
            if (var9.sAsLong() > var9.a) {
               var9.z.YAsDouble(1.0, 280, WeatherRenderer.Type_e.SINE_OUT);
            }

            if (var9.z.yAsDouble() >= 0.95) {
               var7.remove();
            } else {
               double var10 = Math.clamp(1.0 - var9.z.yAsDouble(), 0.0, 1.0);
               if (!var6.EAsBoolean() && !(var10 <= 0.01)) {
                  int var12 = fAsInt(var9.Z, var9.p);
                  int var13 = mAsInt(var6.PAsInt(var16), var16, var12);
                  int var14 = var8 * 37;
                  if (!var9.I) {
                     var9.I = true;
                     var9.A.aAsVoid((double)(var14 - 14));
                  }

                  int var15 = var6.ZAsInt(var17) + (int)Math.round(var9.A.YAsDouble((double)var14, 240, WeatherRenderer.Type_e.QUAD_OUT) - (1.0 - var9.F.yAsDouble()) * 8.0);
                  pAsVoid(var1, var6, var16, var17, var13, var15, var12, var9.Z, var9.p, var9.v, var10);
                  var8++;
               }
            }
         }

         if (var8 == 0 && !var6.EAsBoolean() && HudRenderer.TAsBoolean()) {
            int var18 = fAsInt("Enabled", "Kill Aura");
            pAsVoid(
               var1,
               var6,
               var16,
               var17,
               mAsInt(var6.PAsInt(var16), var16, var18),
               var6.ZAsInt(var17),
               var18,
               "Enabled",
               "Kill Aura",
               NotificationHud.Type_u.INFO,
               1.0
            );
         }
      }
   }

   private static void pAsVoid(
      DrawContext var0,
      HudRenderer.Type_m var1,
      int var2,
      int var3,
      int var4,
      int var5,
      int var6,
      String var7,
      String var8,
      NotificationHud.Type_u var9,
      double var10
   ) {
      var1.uAsVoid(var0, var1.PAsInt(var2), var1.ZAsInt(var3));

      try {
         AAsVoid(var0, var4, var5, var6, var7, var8, var9, var10);
      } finally {
         var1.IAsVoid(var0);
      }

      HudRenderer.xAsVoid(
         var1,
         new WidgetLayoutUtil.Type_p(
            var1.PAsInt(var2) + (int)Math.floor((double)(var4 - var1.PAsInt(var2)) * var1.vAsDouble()),
            var1.ZAsInt(var3) + (int)Math.floor((double)(var5 - var1.ZAsInt(var3)) * var1.vAsDouble()),
            (int)Math.ceil((double)var6 * var1.vAsDouble()),
            (int)Math.ceil(30.0 * var1.vAsDouble())
         )
      );
   }

   public boolean XAsBoolean() {
      return this.j.stream().anyMatch(var0 -> var0.sAsLong() < var0.a + 280L);
   }

   private static int mAsInt(int var0, int var1, int var2) {
      return var0 + (var0 + 130 > var1 / 2 ? 260 - var2 : 0);
   }

   private static int eAsInt(String var0, String var1) {
      if (var0.isBlank()) {
         return WorldWidget.WAsInt(var1, 12.0);
      } else {
         int var2 = WorldWidget.WAsInt(var0, 12.0);
         return var1.isBlank() ? var2 : var2 + 5 + WorldWidget.WAsInt(var1, 12.0);
      }
   }

   private static int fAsInt(String var0, String var1) {
      return Math.min(260, 52 + eAsInt(var0, var1) + 14);
   }

   private static NotificationHud.Type_h VAsW8_Type_h(String var0) {
      return "Enabled".equals(var0) ? NotificationHud.Type_h.ON : ("Disabled".equals(var0) ? NotificationHud.Type_h.OFF : NotificationHud.Type_h.NONE);
   }

   private static String RAsString(String var0, NotificationHud.Type_u var1) {
      if (VAsW8_Type_h(var0) != NotificationHud.Type_h.NONE) {
         return "power";
      } else {
         return switch (var1) {
            case INFO, WARNING -> "bell";
            case DANGER -> "x";
            case SUCCESS -> "badge-check";
         };
      }
   }

   private static int NAsInt(String var0, NotificationHud.Type_u var1) {
      NotificationHud.Type_h var2 = VAsW8_Type_h(var0);
      if (var2 != NotificationHud.Type_h.NONE) {
         return var2 == NotificationHud.Type_h.ON ? UiStyle.w : -6908266;
      } else {
         return switch (var1) {
            case INFO, SUCCESS -> UiStyle.w;
            case WARNING -> -996986;
            case DANGER -> -6908266;
         };
      }
   }

   public static void xAsVoid(DrawContext var0, int var1, int var2) {
      MinecraftClient var3 = MinecraftClient.getInstance();
      int var4 = fAsInt("Enabled", "Kill Aura");
      AAsVoid(var0, mAsInt(var1, var3.getWindow().getScaledWidth(), var4), var2, var4, "Enabled", "Kill Aura", NotificationHud.Type_u.INFO, 1.0);
   }

   private static void AAsVoid(DrawContext var0, int var1, int var2, int var3, String var4, String var5, NotificationHud.Type_u var6, double var7) {
      GuiTextureDrawing.AAsVoid(var0, (float)var1, (float)var2, (float)var3, 30.0F, 10.0F, ColorUtil.pAsInt(-603321844, var7));
      WorldWidget.aAsVoid(var0, RAsString(var4, var6), var1 + 12, var2 + 7, 16, ColorUtil.pAsInt(NAsInt(var4, var6), var7));
      WorldWidget.aAsVoid(var0, "chevron-right", var1 + 30, var2 + 5, 20, ColorUtil.pAsInt(-5592406, var7 * 0.58));
      int var9 = var3 - 52 - 14;
      if (var4.isBlank()) {
         WorldWidget.fAsVoid(var0, WorldWidget.xAsString(var5, (double)var9, 12.0), (double)(var1 + 52), (double)var2 + 9.0, 12.0, ColorUtil.pAsInt(-1, var7));
      } else {
         String var10 = WorldWidget.xAsString(var4, (double)var9, 12.0);
         WorldWidget.fAsVoid(var0, var10, (double)(var1 + 52), (double)var2 + 9.0, 12.0, ColorUtil.pAsInt(-1, var7));
         if (!var5.isBlank()) {
            int var11 = WorldWidget.WAsInt(var10, 12.0) + 5;
            WorldWidget.fAsVoid(var0, WorldWidget.xAsString(var5, (double)(var9 - var11), 12.0), (double)(var1 + 52 + var11), (double)var2 + 9.0, 12.0, ColorUtil.pAsInt(UiStyle.w, var7));
         }
      }
   }

   private static enum Type_h {
      NONE,
      ON,
      OFF;

      private Type_h() {
      }
   }

   private static final class Type_m {
      final String Z;
      final String p;
      final NotificationHud.Type_u v;
      final long a;
      long X = System.nanoTime();
      boolean I;
      final WeatherRenderer F = new WeatherRenderer(0.0);
      final WeatherRenderer z = new WeatherRenderer(0.0);
      final WeatherRenderer A = new WeatherRenderer(0.0);

      Type_m(String var1, String var2, NotificationHud.Type_u var3, long var4) {
         this.Z = var1 == null ? "" : var1;
         this.p = var2 == null ? "" : var2;
         this.v = var3;
         this.a = var4;
      }

      long sAsLong() {
         return (System.nanoTime() - this.X) / 1000000L;
      }

      void OAsVoid() {
         this.X = Math.min(this.X, System.nanoTime() - this.a * 1000000L);
      }
   }

   public static enum Type_u {
      INFO,
      WARNING,
      DANGER,
      SUCCESS;

      private Type_u() {
      }
   }
}
