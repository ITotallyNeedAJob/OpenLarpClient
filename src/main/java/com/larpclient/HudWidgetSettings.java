package com.larpclient;

import com.larpclient.module.NumberSetting;
import com.larpclient.module.Setting;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.DrawContext;

public final class HudWidgetSettings {
   private final List<GuiWidget> V = new ArrayList<>();
   private final Runnable y;
   private final Runnable b;
   private d7.Type_p x;
   private HudRenderer.Type_m Z;
   private GuiWidget l;
   private boolean B;
   private double w;
   private double P;
   private int t;
   private final w2 h = new w2(0.0);

   public HudWidgetSettings(Runnable var1, Runnable var2) {
      this.y = var1;
      this.b = var2;
   }

   public boolean rAsBoolean() {
      return this.x != null;
   }

   public d7.Type_p lAsD7_Type_p() {
      return this.x;
   }

   public void KAsVoid() {
      this.V.clear();
      this.x = null;
      this.l = null;
      this.B = false;
      this.t = 0;
   }

   public void YAsVoid() {
      if (this.rAsBoolean()) {
         for (GuiWidget var2 : this.V) {
            var2.hAsVoid();
         }

         this.V.clear();
         this.x = null;
         this.l = null;
         this.B = false;
         this.t = 0;
         this.b.run();
      }
   }

   public void EAsVoid(int var1, int var2, int var3, int var4) {
      this.YAsVoid();
      this.Z = null;

      for (HudRenderer.Type_m var6 : HudRenderer.S) {
         ActionButton var7 = new ActionButton(HudRenderer.XAsString(var6.a), () -> {
            int var2x = !HudRenderer.HAsBoolean(var6) ? 1 : 0;
            HudRenderer.IAsVoid(var6, var2x != 0);
            this.y.run();
            this.b.run();
            GuiSounds.rAsVoid(var2x != 0 ? GuiSounds.Type_o.ON : GuiSounds.Type_o.OFF);
         });
         var7.a = null;
         var7.F = 22;
         this.V.add(var7);
      }

      this.KAsVoid(var1, var2, 220, var3, var4);
   }

   public void zAsVoid(HudRenderer.Type_m var1, int var2, int var3, int var4, int var5) {
      this.YAsVoid();
      this.Z = var1;
      NumberSetting var6 = new NumberSetting("Widget Size", "Scale this HUD widget", var1.BAsDouble(), 0.5, 2.5, 0.02);
      var6.onChanged(var2x -> {
         var1.iAsVoid(var2x);
         this.y.run();
      });
      this.V.add(new SettingWidget(var6, true));

      for (Setting var8 : HudRenderer.HAsList(var1)) {
         this.V.add(new SettingWidget(var8, true));
      }

      ActionButton var9 = new ActionButton("Hide widget", () -> {
         var1.xAsVoid(true);
         this.y.run();
         this.YAsVoid();
      });
      ActionButton var10 = new ActionButton("Reset placement", () -> {
         var1.AAsVoid();
         this.y.run();
         this.b.run();
      });
      this.V.add(var9);
      this.V.add(var10);
      this.KAsVoid(var2, var3, 252, var4, var5);
      GuiSounds.rAsVoid(GuiSounds.Type_o.SETTINGS_OPEN);
   }

   private void KAsVoid(int var1, int var2, int var3, int var4, int var5) {
      int var6 = Math.min(var5 - 10, 46 + this.V.stream().mapToInt(var0 -> var0.F + 2).sum());
      this.x = new d7.Type_p(
         d7.LAsInt(var1, Math.min(var3, var4 - 10), var4), d7.LAsInt(var2, var6, var5), Math.max(1, Math.min(var3, var4 - 10)), Math.max(1, var6)
      );
      this.t = 0;
      this.h.aAsVoid(0.0);
      this.h.bAsDouble(1.0, 300);
      this.BAsVoid();
   }

   private void BAsVoid() {
      int var1 = this.x.y() + 39 - this.t;

      for (GuiWidget var3 : this.V) {
         var3.bounds(this.x.x() + 10, var1, Math.max(1, this.x.width() - 20));
         if (var3 instanceof ActionButton && this.Z == null) {
            var3.F = 22;
         }

         if (var3 instanceof SettingWidget var4) {
            var4.fAsVoid(this.x.y() + 39, this.x.y() + this.x.height() - 7);
         }

         var1 += var3.F + 2;
      }
   }

   private boolean pAsBoolean(double var1, double var3) {
      return this.x.contains(var1, var3, 0) && var3 >= (double)(this.x.y() + 39) && var3 < (double)(this.x.y() + this.x.height() - 7);
   }

   public boolean iAsBoolean(double var1, double var3, int var5, int var6, int var7) {
      if (!this.rAsBoolean()) {
         return false;
      } else {
         this.BAsVoid();
         if (!this.x.contains(var1, var3, 0)) {
            this.YAsVoid();
            return false;
         } else if (var3 < (double)(this.x.y() + 34)) {
            if (var1 >= (double)(this.x.x() + this.x.width() - 24)) {
               GuiSounds.rAsVoid(GuiSounds.Type_o.SETTINGS_CLOSE);
               this.YAsVoid();
            } else if (var5 == 0) {
               this.B = true;
               this.w = var1 - (double)this.x.x();
               this.P = var3 - (double)this.x.y();
            }

            return true;
         } else {
            for (GuiWidget var9 : this.V) {
               if (var9 instanceof SettingWidget) {
                  SettingWidget var10 = (SettingWidget)var9;
                  if (var10.yAsBoolean()) {
                     if (var10.uAsBoolean(var1, var3)) {
                        var10.oAsBoolean(var1, var3, var5);
                        this.l = var9;
                        this.y.run();
                        return true;
                     }

                     var10.BAsVoid();
                  }
               }
            }

            if (!this.pAsBoolean(var1, var3)) {
               return true;
            } else {
               for (int var12 = 0; var12 < this.V.size(); var12++) {
                  GuiWidget var13 = this.V.get(var12);
                  if (var13.x >= this.x.y() + 39 && var13.x + var13.F <= this.x.y() + this.x.height() - 7 && var13.contains(var1, var3)) {
                     if (this.Z == null && var5 == 1) {
                        this.zAsVoid(HudRenderer.S.get(var12), (int)var1, (int)var3, var6, var7);
                        return true;
                     }

                     for (GuiWidget var11 : this.V) {
                        if (var11 != var13) {
                           var11.hAsVoid();
                        }
                     }

                     if (var13.click(var1, var3, var5)) {
                        this.l = var13;
                        this.y.run();
                     }

                     return true;
                  }
               }

               return true;
            }
         }
      }
   }

   public boolean CAsBoolean(double var1, double var3, int var5, int var6) {
      if (!this.rAsBoolean()) {
         return false;
      } else if (this.B) {
         this.x = new d7.Type_p(
            d7.LAsInt((int)Math.round(var1 - this.w), this.x.width(), var5),
            d7.LAsInt((int)Math.round(var3 - this.P), this.x.height(), var6),
            this.x.width(),
            this.x.height()
         );
         this.BAsVoid();
         return true;
      } else if (this.l != null) {
         this.l.drag(var1, var3);
         this.y.run();
         return true;
      } else {
         return false;
      }
   }

   public boolean cAsBoolean() {
      int var1 = !this.B && this.l == null ? 0 : 1;
      this.B = false;
      if (this.l != null) {
         this.l.release();
         this.l = null;
         this.b.run();
      }

      return var1 != 0;
   }

   public boolean YAsBoolean(int var1) {
      for (GuiWidget var3 : this.V) {
         if (var3 instanceof SettingWidget var4 && var4.xAsBoolean()) {
            boolean var5 = var4.key(var1);
            this.y.run();
            return var5;
         }
      }

      return false;
   }

   public boolean DAsBoolean(int var1) {
      for (GuiWidget var3 : this.V) {
         if (var3 instanceof SettingWidget var4 && var4.xAsBoolean()) {
            var4.character(var1);
            return true;
         }
      }

      return false;
   }

   public boolean fAsBoolean(double var1, double var3, double var5) {
      if (this.rAsBoolean() && this.x.contains(var1, var3, 0)) {
         for (GuiWidget var8 : this.V) {
            if (var8 instanceof SettingWidget var9 && var9.uAsBoolean(var1, var3)) {
               var9.OAsVoid(var5);
               return true;
            }
         }

         int var10 = this.V.stream().mapToInt(var0 -> var0.F + 2).sum();
         this.t = Math.clamp((long)(this.t - (int)Math.round(var5 * 22.0)), 0, Math.max(0, var10 - this.x.height() + 46));
         this.BAsVoid();
         return true;
      } else {
         return false;
      }
   }

   public void rAsVoid(DrawContext var1, int var2, int var3) {
      if (this.rAsBoolean()) {
         this.BAsVoid();

         try (qg var4 = qg.nAsQg(this.h.yAsDouble())) {
            GuiTextureDrawing.AAsVoid(
               var1, (float)(this.x.x() - 3), (float)(this.x.y() - 3), (float)(this.x.width() + 6), (float)(this.x.height() + 6), 13.0F, 1593835520
            );
            GuiTextureDrawing.AAsVoid(var1, (float)this.x.x(), (float)this.x.y(), (float)this.x.width(), (float)this.x.height(), 10.0F, -183957239);
            ww.fAsVoid(
               var1, this.Z == null ? "HUD Elements" : HudRenderer.XAsString(this.Z.a), (double)(this.x.x() + 12), (double)(this.x.y() + 11), 13.0, -1250068
            );
            GuiIcons.aAsVoid(var1, GuiIcons.PAsIdentifier("x"), this.x.x() + this.x.width() - 20, this.x.y() + 12, 12, -5592406);
            GuiTextureDrawing.CAsVoid(var1, this.x.x() + 2, this.x.y() + 32, this.x.width() - 4, 1, 675036230);
            var1.enableScissor(this.x.x(), this.x.y() + 39, this.x.x() + this.x.width(), this.x.y() + this.x.height() - 7);

            try {
               for (int var5 = 0; var5 < this.V.size(); var5++) {
                  GuiWidget var6 = this.V.get(var5);
                  if (var6.x + var6.F >= this.x.y() + 39 && var6.x < this.x.y() + this.x.height() - 7) {
                     if (this.Z == null && var6 instanceof ActionButton var7) {
                        var7.e = true;
                        var7.i = HudRenderer.HAsBoolean(HudRenderer.S.get(var5));
                        var7.g = var7.i;
                     }

                     var6.render(var1, this.pAsBoolean((double)var2, (double)var3) ? var2 : -1, var3);
                  }
               }
            } finally {
               var1.disableScissor();
            }

            for (GuiWidget var16 : this.V) {
               if (var16 instanceof SettingWidget) {
                  SettingWidget var17 = (SettingWidget)var16;
                  if (var17.yAsBoolean()) {
                     var17.oAsVoid(var1, var2, var3);
                  }
               }
            }
         }
      }
   }
}
