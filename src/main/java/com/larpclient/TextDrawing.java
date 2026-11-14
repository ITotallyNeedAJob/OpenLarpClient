package com.larpclient;

import java.util.Arrays;
import java.util.Locale;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.StyleSpriteSource.Font;
import net.minecraft.util.Identifier;

public final class TextDrawing {
   private static int r;
   private static final FontCache K = new FontCache();
   private static final Style[] u = Arrays.stream(TextDrawing.Type_x.values())
      .map(
         var0 -> var0 == TextDrawing.Type_x.MINECRAFT
               ? Style.EMPTY
               : Style.EMPTY.withFont(new Font(Identifier.of("larp-client", var0.name().toLowerCase(Locale.ROOT))))
      )
      .toArray(Style[]::new);
   private static final Style F = Style.EMPTY.withFont(new Font(Identifier.of("larp-client", "world_semibold")));
   private static int D;

   public TextDrawing() {
   }

   public static int lAsInt() {
      return r;
   }

   public static void XAsVoid() {
      K.DAsVoid(null, -1);
      r++;
   }

   public static int jAsInt(String var0, TextDrawing.Type_s var1) {
      return qAsBoolean() ? qAsInt(var0, var1.G) : (int)Math.ceil((double)((float)mAsInt(var0, var1.q) * var1.C));
   }

   public static String zAsString(String var0, int var1, TextDrawing.Type_s var2) {
      return qAsBoolean() ? sAsString(var0, var1, var2.G) : mAsString(var0, Math.max(0, (int)Math.floor((double)((float)var1 / var2.C))), var2.q);
   }

   public static void NAsVoid(DrawContext var0, String var1, int var2, int var3, int var4, TextDrawing.Type_s var5) {
      if (qAsBoolean()) {
         oAsVoid(var0, var1, var2, var3, var4, var5.G);
      } else if (var5.C == 1.0F) {
         TAsVoid(var0, var1, var2, var3, var4, var5.q);
      } else {
         var0.getMatrices().pushMatrix();

         try {
            var0.getMatrices().translate((float)var2, (float)var3);
            var0.getMatrices().scale(var5.C, var5.C);
            TAsVoid(var0, var1, 0, 0, var4, var5.q);
         } finally {
            var0.getMatrices().popMatrix();
         }
      }
   }

   public static Text UAsText(String var0) {
      return Text.literal(var0).setStyle(F);
   }

   public static TextDrawing.Type_l XAsV_Type_l() {
      D++;
      return () -> D--;
   }

   private static boolean qAsBoolean() {
      if (D > 0) {
         return true;
      } else {
         MinecraftClient var0 = MinecraftClient.getInstance();
         return var0 != null && var0.inGameHud != null && var0.currentScreen instanceof ClickGuiScreen;
      }
   }

   private static TextDrawing.Type_x NAsV_Type_x(TextDrawing.Type_x var0) {
      if (!qAsBoolean()) {
         return var0;
      } else {
         return switch (var0) {
            case REGULAR -> TextDrawing.Type_x.CLICK_MEDIUM;
            case MEDIUM -> TextDrawing.Type_x.CLICK_SEMIBOLD;
            case SEMIBOLD -> TextDrawing.Type_x.CLICK_SEMIBOLD;
            default -> var0;
         };
      }
   }

   public static int UAsInt(String var0) {
      return mAsInt(var0, TextDrawing.Type_x.REGULAR);
   }

   public static int mAsInt(String var0, TextDrawing.Type_x var1) {
      return qAsInt(var0, NAsV_Type_x(var1));
   }

   public static void UAsVoid(DrawContext var0, String var1, int var2, int var3, int var4) {
      TAsVoid(var0, var1, var2, var3, var4, TextDrawing.Type_x.REGULAR);
   }

   public static void TAsVoid(DrawContext var0, String var1, int var2, int var3, int var4, TextDrawing.Type_x var5) {
      oAsVoid(var0, var1, var2, var3, var4, NAsV_Type_x(var5));
   }

   public static String HAsString(String var0, int var1) {
      return mAsString(var0, var1, TextDrawing.Type_x.REGULAR);
   }

   public static String mAsString(String var0, int var1, TextDrawing.Type_x var2) {
      return sAsString(var0, var1, NAsV_Type_x(var2));
   }

   public static Text ZAsText(String var0, TextDrawing.Type_x var1) {
      return Text.literal(var0).setStyle(u[var1.ordinal()]);
   }

   public static int nAsInt(String var0) {
      return qAsInt(var0, TextDrawing.Type_x.REGULAR);
   }

   public static int qAsInt(String var0, TextDrawing.Type_x var1) {
      TextRenderer var2 = MinecraftClient.getInstance().textRenderer;
      K.DAsVoid(var2, BloomQuality.J);
      return K.aAsInt(
         var0, var1.ordinal(), () -> ClickGuiTextRenderer.QAsBoolean(var1, var0) ? ClickGuiTextRenderer.TAsInt(var0, var1) : var2.getWidth(ZAsText(var0, var1))
      );
   }

   public static void CAsVoid(DrawContext var0, String var1, int var2, int var3, int var4) {
      oAsVoid(var0, var1, var2, var3, var4, TextDrawing.Type_x.REGULAR);
   }

   public static void oAsVoid(DrawContext var0, String var1, int var2, int var3, int var4, TextDrawing.Type_x var5) {
      var4 = QueueGroup.iAsInt(var4);
      if (var4 >>> 24 >= 4 && !ClickGuiTextRenderer.kAsBoolean(var0, var1, var2, var3, var4, var5)) {
         var0.drawText(MinecraftClient.getInstance().textRenderer, ZAsText(var1, var5), var2, var3, var4, false);
      }
   }

   public static void uAsVoid(DrawContext var0, String var1, int var2, int var3, int var4) {
      CAsVoid(var0, var1, var2 - nAsInt(var1) / 2, var3, var4);
   }

   public static String LAsString(String var0, int var1) {
      return sAsString(var0, var1, TextDrawing.Type_x.REGULAR);
   }

   public static String sAsString(String var0, int var1, TextDrawing.Type_x var2) {
      K.DAsVoid(MinecraftClient.getInstance().textRenderer, BloomQuality.J);
      return K.nAsString(var0, var2.ordinal(), var1, () -> {
         if (qAsInt(var0, var2) <= var1) {
            return var0;
         } else {
            int var3 = var0.length();

            while (var3 > 0 && qAsInt(var0.substring(0, var3) + "…", var2) > var1) {
               var3 = var0.offsetByCodePoints(var3, -1);
            }

            return var3 == 0 ? "" : var0.substring(0, var3) + "…";
         }
      });
   }

   static {
      ClientLog.GAsVoid(
         "Font",
         "Inter UI/HUD and ClickUI faces registered at native pixel sizes per role; ClickUI text is rasterized at the on-screen pixel size, other text by Minecraft"
      );
   }

   public interface Type_l extends AutoCloseable {
      @Override
      void close();
   }

   public static enum Type_s {
      BRAND(TextDrawing.Type_x.SEMIBOLD, 1.2F, TextDrawing.Type_x.CLICK_BRAND),
      TITLE(TextDrawing.Type_x.SEMIBOLD, 1.15F, TextDrawing.Type_x.CLICK_TITLE),
      MODULE(TextDrawing.Type_x.MEDIUM, 1.0F, TextDrawing.Type_x.CLICK_MODULE),
      BODY(TextDrawing.Type_x.REGULAR, 1.0F, TextDrawing.Type_x.CLICK_BODY),
      NAVIGATION(TextDrawing.Type_x.MEDIUM, 0.9F, TextDrawing.Type_x.CLICK_NAVIGATION),
      SECTION(TextDrawing.Type_x.MEDIUM, 0.9F, TextDrawing.Type_x.CLICK_SECTION),
      HELP(TextDrawing.Type_x.REGULAR, 0.85F, TextDrawing.Type_x.CLICK_HELP),
      EYEBROW(TextDrawing.Type_x.SEMIBOLD, 0.8F, TextDrawing.Type_x.CLICK_EYEBROW);

      public final TextDrawing.Type_x q;
      public final float C;
      final TextDrawing.Type_x G;

      private Type_s(TextDrawing.Type_x var3, float var4, TextDrawing.Type_x var5) {
         this.q = var3;
         this.C = var4;
         this.G = var5;
      }

      public int zAsInt() {
         return (int)Math.ceil((double)(9.0F * this.C));
      }
   }

   public static enum Type_x {
      REGULAR,
      MEDIUM,
      SEMIBOLD,
      CLOCK,
      HUD,
      HUD_SF_PRO,
      CLICK_REGULAR,
      CLICK_MEDIUM,
      CLICK_SEMIBOLD,
      CLICK_BRAND,
      CLICK_TITLE,
      CLICK_MODULE,
      CLICK_BODY,
      CLICK_NAVIGATION,
      CLICK_SECTION,
      CLICK_HELP,
      CLICK_EYEBROW,
      MINECRAFT;

      private Type_x() {
      }
   }
}
