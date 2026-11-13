package com.larpclient;

import com.larpclient.module.Emotes;
import java.util.List;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;

public final class EmoteWheelScreen extends Screen {
   private static final int R = 38;
   private static final int S = 32;
   private static final int q = 5;
   private final Emotes C;
   private final w2 h = new w2(0.0);
   private final w2[] X = new w2[]{new w2(0.0), new w2(0.0), new w2(0.0), new w2(0.0)};
   private int r = -1;
   private double g = Double.NaN;
   private float Z = 1.0F;
   private boolean Y;

   public EmoteWheelScreen(Emotes var1) {
      super(Text.literal("Emotes"));
      this.C = var1;
      this.h.bAsDouble(1.0, 160);
   }

   public static int DAsInt(double var0, double var2, double var4) {
      if (var0 * var0 + var2 * var2 < var4 * var4) {
         return -1;
      } else if (Math.abs(var0) > Math.abs(var2)) {
         return var0 > 0.0 ? 1 : 3;
      } else {
         return var2 > 0.0 ? 2 : 0;
      }
   }

   private void tAsVoid(double var1, double var3) {
      double var5 = var1 - (double)this.width / 2.0;
      double var7 = var3 - (double)this.height / 2.0;
      this.r = DAsInt(var5, var7, (double)(38.0F * this.Z));
      this.g = this.r < 0 ? Double.NaN : Math.atan2(var7, var5);
   }

   private void DAsVoid() {
      if (!this.Y) {
         this.Y = true;
         List var1 = this.C.TAsList();
         if (this.r >= 0 && !"Empty".equals(var1.get(this.r))) {
            this.C.IAsVoid(this.r);
         } else if (this.r < 0 && this.C.qAsBoolean()) {
            this.C.kAsVoid("stopped from the wheel");
         } else {
            ClientLog.XAsVoid("Emotes", "Wheel closed without an emote (slot {})", this.r < 0 ? "hub" : this.r + 1);
         }

         this.close();
      }
   }

   public void tick() {
      int var1 = this.C.wheelKey();
      boolean var2 = KeybindNames.sAsBoolean(this.client.getWindow().getHandle(), var1);
      if (!this.C.isRuntimeActive() || this.client.player == null) {
         this.close();
      } else if (!var2) {
         this.DAsVoid();
      }
   }

   public boolean keyReleased(KeyInput input) {
      if (input.key() == this.C.wheelKey()) {
         this.DAsVoid();
         return true;
      } else {
         return super.keyReleased(input);
      }
   }

   public void mouseMoved(double mouseX, double mouseY) {
      this.tAsVoid(mouseX, mouseY);
   }

   public boolean mouseClicked(Click click, boolean doubled) {
      this.tAsVoid(click.x(), click.y());
      if (click.button() == 0) {
         this.DAsVoid();
      } else {
         this.close();
      }

      return true;
   }

   public boolean shouldPause() {
      return false;
   }

   public void renderBackground(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
      GuiTextureDrawing.CAsVoid(context, 0, 0, this.width, this.height, bg.XAsInt(-16777216, 0.25 * this.h.yAsDouble()));
   }

   public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
      super.render(context, mouseX, mouseY, deltaTicks);
      List var5 = this.C.TAsList();
      double var6 = this.h.bAsDouble(1.0, 160);
      int var8 = this.width / 2;
      int var9 = this.height / 2;
      float var10 = (float)Math.min(1.0, (double)(Math.min(this.width, this.height) - 24) / 236.0);
      this.Z = (float)((0.9 + 0.1 * var6) * (double)var10);
      this.tAsVoid((double)mouseX, (double)mouseY);

      try (qg var11 = qg.nAsQg(var6)) {
         context.getMatrices().pushMatrix();

         try {
            context.getMatrices().translate((float)var8, (float)var9);
            context.getMatrices().scale(this.Z, this.Z);

            for (int var12 = 0; var12 < 4; var12++) {
               this.IAsVoid(context, var12, (String)var5.get(var12));
            }

            this.QAsVoid(context, var5);
         } finally {
            context.getMatrices().popMatrix();
         }

         this.qAsVoid(context, var8, var9 + Math.round(108.0F * this.Z));
      }
   }

   private void IAsVoid(DrawContext var1, int var2, String var3) {
      boolean var4 = "Empty".equals(var3);
      EmoteCatalog.Type_a var5 = EmoteCatalog.LAsA2_Type_a(var3);
      double var6 = this.X[var2].bAsDouble(var2 == this.r ? 1.0 : 0.0, 160);
      int var8 = var4 ? bg.XAsInt(bd.m, 0.5) : bg.XAsInt(bd.m, 0.84);
      int var9 = bg.xAsInt(var8, bg.XAsInt(bg.xAsInt(bd.m, bd.w, 0.2), 0.94), var4 ? var6 * 0.4 : var6);
      int var10 = bg.xAsInt(bg.XAsInt(-1381654, 0.09), var4 ? bg.XAsInt(-1381654, 0.3) : bd.w, var6);
      EmoteWheelRenderer.IAsVoid(var1, var2, var6 * 5.0, var9, var10);
      double var11 = (double)(var2 - 1) * Math.PI / 2.0;
      double var13 = 67.0 + var6 * 5.0;
      int var15 = (int)Math.round(Math.cos(var11) * var13);
      int var16 = (int)Math.round(Math.sin(var11) * var13);
      TextDrawing.Type_s var17 = TextDrawing.Type_s.BODY;
      if (!var4 && var5 != null) {
         int var20 = bg.xAsInt(bd.e, -1381654, 0.55 + 0.45 * var6);
         var1.drawTexture(RenderPipelines.GUI_TEXTURED, var5.icon(), var15 - 19, var16 - 19 - 5, 0.0F, 0.0F, 38, 38, 96, 96, 96, 96, qg.iAsInt(var20));
         int var19 = bg.xAsInt(bd.u, -1381654, var6);
         TextDrawing.NAsVoid(var1, var3, var15 - TextDrawing.jAsInt(var3, var17) / 2, var16 + 19 - 4, var19, var17);
      } else {
         int var18 = bg.xAsInt(bd.W, bd.e, var6);
         GuiTextureDrawing.CAsVoid(var1, var15 - 5, var16 - 9, 11, 1, var18);
         GuiTextureDrawing.CAsVoid(var1, var15, var16 - 14, 1, 11, var18);
         TextDrawing.NAsVoid(var1, "Empty", var15 - TextDrawing.jAsInt("Empty", var17) / 2, var16 + 4, var18, var17);
      }
   }

   private void QAsVoid(DrawContext var1, List<String> var2) {
      GuiTextureDrawing.RAsVoid(var1, 0, 0, 32, bg.XAsInt(bd.m, 0.9));
      GuiDrawing.wAsVoid(var1, -32, -32, 65, 65, 32, bg.XAsInt(-1381654, 0.12));
      int var5 = -1381654;
      String var3;
      String var4;
      if (this.r >= 0) {
         var3 = var2.get(this.r);
         boolean var6 = "Empty".equals(var3);
         var4 = var6 ? "No emote" : (this.C.qAsBoolean() && var3.equals(this.C.TAsString()) ? "Playing" : "Release");
         if (var6) {
            var5 = bd.W;
         }
      } else {
         var3 = this.C.qAsBoolean() ? "Stop" : "Cancel";
         var4 = this.C.qAsBoolean() ? this.C.TAsString() : "Emotes";
         var5 = bd.e;
      }

      TextDrawing.Type_s var12 = TextDrawing.Type_s.MODULE;
      var3 = RAsString(var3, var12, 54);
      TextDrawing.NAsVoid(var1, var3, -TextDrawing.jAsInt(var3, var12) / 2, -8, var5, var12);
      TextDrawing.Type_s var7 = TextDrawing.Type_s.EYEBROW;
      var4 = RAsString(var4, var7, 50);
      TextDrawing.NAsVoid(var1, var4, -TextDrawing.jAsInt(var4, var7) / 2, 5, bd.W, var7);
      if (!Double.isNaN(this.g)) {
         int var8 = (int)Math.round(Math.cos(this.g) * 35.0);
         int var9 = (int)Math.round(Math.sin(this.g) * 35.0);
         GuiTextureDrawing.RAsVoid(var1, var8, var9, 2, bd.w);
      }
   }

   private void qAsVoid(DrawContext var1, int var2, int var3) {
      String var4 = KeybindNames.eAsString(this.C.wheelKey());
      String var5 = "Release " + var4 + " to play  ·  Esc to cancel";
      TextDrawing.Type_s var6 = TextDrawing.Type_s.HELP;
      TextDrawing.NAsVoid(var1, var5, var2 - TextDrawing.jAsInt(var5, var6) / 2, var3, bd.e, var6);
   }

   private static String RAsString(String var0, TextDrawing.Type_s var1, int var2) {
      if (TextDrawing.jAsInt(var0, var1) <= var2) {
         return var0;
      } else {
         String var3 = var0;

         while (var3.length() > 1 && TextDrawing.jAsInt(var3 + "...", var1) > var2) {
            var3 = var3.substring(0, var3.length() - 1);
         }

         return var3.strip() + "...";
      }
   }
}
