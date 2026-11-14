package com.larpclient;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public final class IslandLyricsPreview {
   public static final int K = 300;
   public static final int C = 20;
   static final int F = 44;
   static final int U = 10;
   static final int p = 13;
   static final long n = 380000000L;
   static final long X = 520000000L;
   static final double S = 1.6;
   private static final int e = -1250068;
   private static final int b = -4671294;
   private static final int w = 805306368;
   private static final Identifier z = Identifier.of("larp-client", "textures/gui/logo.png");
   private static final IslandLyricsPreview.Type_a T = new IslandLyricsPreview.Type_a(
      IslandLyricsPreview.Type_m.LYRIC, "Island Lyrics preview line", "sample", true, 0.0
   );
   private final IslandLyricsPreview.Type_i d = new IslandLyricsPreview.Type_i(44.0F, 170.0F, 19.0F);
   private final IslandLyricsPreview.Type_i I = new IslandLyricsPreview.Type_i(14.0F, 170.0F, 19.0F);
   private final IslandLyricsPreview.Type_i t = new IslandLyricsPreview.Type_i(0.0F, 90.0F, 18.0F);
   private final IslandLyricsPreview.Type_i Y = new IslandLyricsPreview.Type_i(0.0F, 120.0F, 20.0F);
   private IslandLyricsPreview.Type_a L = IslandLyricsPreview.Type_a.IDLE;
   private IslandLyricsPreview.Type_a E = IslandLyricsPreview.Type_a.IDLE;
   private long O;
   private long s;
   private double j;

   public IslandLyricsPreview() {
   }

   static IslandLyricsPreview.Type_a zAsOb_Type_a(LyricsController.Type_r var0, long var1) {
      MediaSessionMetadata var3 = var0 == null ? null : var0.playback();
      if (var3 != null && var1 - var3.observedAt() <= 4000000000L) {
         String var4 = var3.key();
         TimedLyricsParser var5 = var0.lyrics() == null ? null : var0.lyrics().timed();
         double var6 = var3.positionAt(var1);
         if (var5 != null && var3.playing()) {
            int var8 = var5.hAsInt(var6);
            String var9 = var5.yAsString(var8, var6);
            if (!var9.isBlank()) {
               double var10 = var5.wAsList().get(var8).seconds();
               double var12 = var8 + 1 < var5.wAsList().size() ? var5.wAsList().get(var8 + 1).seconds() : var3.duration();
               double var14 = Math.clamp((var6 - var10) / Math.clamp(var12 - var10, 0.2, 18.0), 0.0, 1.0);
               return new IslandLyricsPreview.Type_a(IslandLyricsPreview.Type_m.LYRIC, var9, "l" + var4 + "\u001f" + var8, true, var14);
            }
         }

         String var16 = var3.artist().isBlank() ? var3.title() : var3.title() + "  ·  " + var3.artist();
         return new IslandLyricsPreview.Type_a(IslandLyricsPreview.Type_m.SONG, var16, "s" + var4, var3.playing(), 0.0);
      } else {
         return IslandLyricsPreview.Type_a.IDLE;
      }
   }

   public boolean oAsBoolean(LyricsController.Type_r var1, long var2) {
      return zAsOb_Type_a(var1, var2).kind != IslandLyricsPreview.Type_m.IDLE || this.Y.h >= 0.02F;
   }

   public void MAsVoid() {
      this.L = this.E = IslandLyricsPreview.Type_a.IDLE;
      this.d.CAsVoid(44.0F);
      this.I.CAsVoid(14.0F);
      this.t.CAsVoid(0.0F);
      this.Y.CAsVoid(0.0F);
      this.O = this.s = 0L;
      this.j = 0.0;
   }

   public void TAsVoid(DrawContext var1, int var2, int var3, int var4, LyricsController.Type_r var5, long var6) {
      float var8 = this.s == 0L ? 0.0F : Math.min(0.05F, (float)(var6 - this.s) / 1.0E9F);
      this.s = var6;
      IslandLyricsPreview.Type_a var9 = zAsOb_Type_a(var5, var6);
      if (!var9.key.equals(this.L.key)) {
         this.E = this.L;
         this.L = var9;
         this.O = var6;
         if (var9.kind == IslandLyricsPreview.Type_m.LYRIC) {
            this.t.CAsVoid(0.0F);
         }
      } else {
         this.L = var9;
      }

      if (this.L.playing) {
         this.j = (this.j + (double)var8 * 1.6 * Math.PI * 2.0) % (Math.PI * 2);
      }

      float var10 = this.L.kind == IslandLyricsPreview.Type_m.IDLE ? 44.0F : eAsFloat(this.L, var4);
      float var11 = this.Y.kAsFloat(this.L.kind == IslandLyricsPreview.Type_m.IDLE ? 0.0F : 1.0F, var8);
      float var12 = this.d.kAsFloat(var10, var8);
      float var13 = this.I.kAsFloat(this.L.kind == IslandLyricsPreview.Type_m.IDLE ? 14.0F : 20.0F, var8);
      if (!(var11 < 0.02F)) {
         float var14 = GAsFloat(Math.clamp((float)(var6 - this.O) / 3.8E8F, 0.0F, 1.0F));
         float var15 = 0.0F;
         if (this.L.kind == IslandLyricsPreview.Type_m.LYRIC) {
            float var16 = HudSdfAtlas.jAsFloat(this.L.text, 10.0) - (float)(Math.round(var12) - 50);
            var15 = this.t.kAsFloat(var16 <= 0.0F ? 0.0F : (float)Math.clamp(this.L.progress * (double)(var16 + 30.0F) - 15.0, 0.0, (double)var16), var8);
         }

         float var17 = Math.clamp((float)(var6 - this.O) / 5.2E8F, 0.0F, 1.0F);
         this.IAsVoid(var1, var2, var3, var4, Math.max(12, Math.round(var12)), Math.max(8, Math.round(var13)), var11, var14, var17, var15, var6);
      }
   }

   public void NAsVoid(DrawContext var1, int var2, int var3, int var4, long var5) {
      IslandLyricsPreview.Type_a var7 = this.L;
      this.L = T;

      try {
         this.IAsVoid(var1, var2, var3, var4, Math.round(eAsFloat(T, var4)), 20, 1.0F, 1.0F, 1.0F, 0.0F, var5);
      } finally {
         this.L = var7;
      }
   }

   private static float eAsFloat(IslandLyricsPreview.Type_a var0, int var1) {
      return Math.clamp(HudSdfAtlas.jAsFloat(var0.text, 10.0) + 62.0F, 110.0F, (float)Math.max(110, var1));
   }

   private void IAsVoid(DrawContext var1, int var2, int var3, int var4, int var5, int var6, float var7, float var8, float var9, float var10, long var11) {
      int var13 = var2 + (var4 - var5) / 2;
      int var14 = var3 + (20 - var6) / 2;
      int var15 = var6 / 2;

      try (QueueGroup var16 = QueueGroup.nAsQg((double)Math.clamp(var7, 0.0F, 1.0F))) {
         RoundedShapePipeline.CAsVoid(var1, (float)var13, (float)var14, (float)var5, (float)var6, (float)var15, 3.0F, 805306368, 805306368);
         RoundedShapePipeline.nAsVoid(var1, (float)var13, (float)var14, (float)var5, (float)var6, (float)var15, MenuLogoLayout.zAsInt());
         var1.enableScissor(var13 + 2, var14, var13 + var5 - 2, var14 + var6);

         try {
            if (var8 < 1.0F && this.E.kind != IslandLyricsPreview.Type_m.IDLE) {
               try (QueueGroup var17 = QueueGroup.nAsQg((double)(1.0F - var8) * 0.5)) {
                  int var18 = Math.round(var8 * 3.0F);
                  this.kAsVoid(var1, this.E, var13 - var18, var14, var5, var6, var11, 0.0F, 1.0F);
                  this.kAsVoid(var1, this.E, var13 + var18, var14, var5, var6, var11, 0.0F, 1.0F);
               }
            }

            try (QueueGroup var35 = QueueGroup.nAsQg((double)var8)) {
               if (this.L.kind == IslandLyricsPreview.Type_m.IDLE) {
                  double var36 = 0.5 + 0.5 * Math.sin((double)var11 / 9.0E8);
                  PAsVoid(var1, (float)var13 + (float)var5 / 2.0F, (float)var14 + (float)var6 / 2.0F, 2.2F, ColorUtil.pAsInt(UiStyle.w, 0.55 + 0.35 * var36));
               } else {
                  this.kAsVoid(var1, this.L, var13, var14, var5, var6, var11, var10, var9);
               }
            }
         } finally {
            var1.disableScissor();
         }
      }
   }

   private void kAsVoid(DrawContext var1, IslandLyricsPreview.Type_a var2, int var3, int var4, int var5, int var6, long var7, float var9, float var10) {
      float var11 = (float)var4 + (float)var6 / 2.0F;
      this.tAsVoid(var1, (float)(var3 + 12), var11);
      fAsVoid(var1, var3 + var5 - 22, var11, var2.playing, var7);
      int var12 = var3 + 23;
      int var13 = var5 - 50;
      double var14 = (double)var4 + (double)(var6 - 10) / 2.0;
      if (var2.kind == IslandLyricsPreview.Type_m.LYRIC) {
         var1.enableScissor(var12 - 1, var4, var12 + var13 + 1, var4 + var6);

         try {
            if (var10 < 1.0F) {
               EAsVoid(var1, var2.text, (double)((float)var12 - var9), var14, var10);
            } else {
               WorldWidget.fAsVoid(var1, var2.text, (double)((float)var12 - var9), var14, 10.0, -1250068);
            }
         } finally {
            var1.disableScissor();
         }
      } else {
         WorldWidget.fAsVoid(var1, HudSdfAtlas.bAsString(var2.text, (double)var13, 10.0), (double)var12, var14, 10.0, -4671294);
      }
   }

   private static void EAsVoid(DrawContext var0, String var1, double var2, double var4, float var6) {
      float var7 = Math.max(1.0F, HudSdfAtlas.jAsFloat(var1, 10.0));
      float var8 = 0.0F;
      int var9 = 0;

      while (var9 < var1.length()) {
         int var10 = var1.codePointAt(var9);
         String var11 = new String(Character.toChars(var10));
         float var12 = HudSdfAtlas.jAsFloat(var11, 10.0);
         float var13 = Math.clamp((var6 - (var8 + var12 / 2.0F) / var7 * 0.5F) / 0.5F, 0.0F, 1.0F);
         if (var13 > 0.0F && !Character.isWhitespace(var10)) {
            try (QueueGroup var14 = QueueGroup.nAsQg((double)var13)) {
               WorldWidget.fAsVoid(var0, var11, var2 + (double)var8, var4 + (double)(4.0F * (1.0F - GAsFloat(var13))), 10.0, -1250068);
            }
         }

         var8 += var12;
         var9 += Character.charCount(var10);
      }
   }

   private void tAsVoid(DrawContext var1, float var2, float var3) {
      var1.getMatrices().pushMatrix();

      try {
         var1.getMatrices().translate(var2, var3).rotate((float)this.j);
         if (!HudSdfAtlas.NAsBoolean(var1, "logo", -6.5, -6.5, 13.0, -1)) {
            GuiTextureDrawing.AAsVoid(var1, z, -6, -6, 13, 13, 64, 64);
         }
      } finally {
         var1.getMatrices().popMatrix();
      }
   }

   private static void fAsVoid(DrawContext var0, int var1, float var2, boolean var3, long var4) {
      double var6 = (double)var4 / 1.0E9;

      for (int var8 = 0; var8 < 4; var8++) {
         float var9 = var3 ? (float)(3.0 + 5.0 * Math.abs(Math.sin(var6 * (2.6 + (double)var8 * 1.1) + (double)var8 * 1.7))) : 2.5F;
         var0.getMatrices().pushMatrix();

         try {
            var0.getMatrices().translate((float)var1 + (float)var8 * 3.5F, var2 - var9 / 2.0F);
            RoundedShapePipeline.nAsVoid(var0, 0.0F, 0.0F, 2.0F, (float)Math.max(2, Math.round(var9)), 1.0F, ColorUtil.pAsInt(UiStyle.w, 0.95));
         } finally {
            var0.getMatrices().popMatrix();
         }
      }
   }

   private static void PAsVoid(DrawContext var0, float var1, float var2, float var3, int var4) {
      var0.getMatrices().pushMatrix();

      try {
         var0.getMatrices().translate(var1, var2).scale(var3 / 8.0F, var3 / 8.0F);
         RoundedShapePipeline.nAsVoid(var0, -8.0F, -8.0F, 16.0F, 16.0F, 8.0F, var4);
      } finally {
         var0.getMatrices().popMatrix();
      }
   }

   static float GAsFloat(float var0) {
      float var1 = 1.0F - var0;
      return 1.0F - var1 * var1 * var1;
   }

   static record Type_a(IslandLyricsPreview.Type_m kind, String text, String key, boolean playing, double progress) {
      static final IslandLyricsPreview.Type_a IDLE = new IslandLyricsPreview.Type_a(IslandLyricsPreview.Type_m.IDLE, "", "i", false, 0.0);
   }

   static final class Type_i {
      float h;
      float G;
      final float D;
      final float L;

      Type_i(float var1, float var2, float var3) {
         this.h = var1;
         this.D = var2;
         this.L = var3;
      }

      float kAsFloat(float var1, float var2) {
         int var3 = Math.max(1, (int)Math.ceil((double)(var2 / 0.008F)));
         float var4 = var2 / (float)var3;

         for (int var5 = 0; var5 < var3; var5++) {
            float var6 = this.D * (var1 - this.h) - this.L * this.G;
            this.G += var6 * var4;
            this.h = this.h + this.G * var4;
         }

         return this.h;
      }

      void CAsVoid(float var1) {
         this.h = var1;
         this.G = 0.0F;
      }
   }

   static enum Type_m {
      IDLE,
      SONG,
      LYRIC;

      private Type_m() {
      }
   }
}
