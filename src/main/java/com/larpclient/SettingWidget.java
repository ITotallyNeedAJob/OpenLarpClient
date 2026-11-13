package com.larpclient;

import com.larpclient.module.BooleanSetting;
import com.larpclient.module.ColorSetting;
import com.larpclient.module.FakeSpawner;
import com.larpclient.module.KeybindSetting;
import com.larpclient.module.ModeSetting;
import com.larpclient.module.Module;
import com.larpclient.module.NumberSetting;
import com.larpclient.module.Setting;
import com.larpclient.module.TextSetting;
import java.awt.Color;
import java.math.BigDecimal;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public final class SettingWidget extends GuiWidget {
   private final Setting<?> t;
   private final boolean e;
   private boolean O;
   private boolean z;
   private boolean p;
   private boolean w;
   private boolean P;
   private boolean E;
   private boolean a;
   private Module m;
   private boolean I;
   private String s;
   private long h;
   private long K;
   private String q;
   private final w2 d;
   private final w2 A;
   private final w2 N;
   private final w2 b;
   private final w2 D;
   private int R;
   private int u;
   private int X;
   private int M;
   private int C;
   private int L;
   private int o;
   private int n;
   private int S;
   private int c;
   private final TextInputWidget l;
   private final TextInputWidget g;
   private boolean r;
   private List<String> Z;
   private int G;

   public SettingWidget(Setting<?> var1) {
      this(var1, false);
   }

   public SettingWidget(Setting<?> var1, boolean var2) {
      super();
      TextInputWidget var10001;
      label45: {
         this.s = "";
         this.d = new w2(0.0);
         this.A = new w2(0.0);
         this.R = 1;
         this.u = -1;
         this.Z = List.of();
         this.e = var2;
         this.t = var1;
         if (var1 instanceof ModeSetting var3 && var3.isSearchable()) {
            var10001 = new TextInputWidget("Search options...", var1x -> this.SAsVoid(), 96);
            break label45;
         }

         var10001 = null;
      }

      this.l = var10001;
      this.g = var1 instanceof TextSetting var4 ? new TextInputWidget(var4.NAsString(), var0 -> {
      }, 128).MAsQq(this::OAsVoid) : null;
      if (var1 instanceof ModeSetting var5) {
         this.Z = var5.getVisibleModes();
      }

      byte var10003;
      label39: {
         if (var1 instanceof BooleanSetting var6 && var6.getValue()) {
            var10003 = 1;
            break label39;
         }

         var10003 = 0;
      }

      // Vineflower could not resugar this constructor call (see $VF marker).
      // Bytecode: new w2, dup, iload var10003, i2d, invokespecial w2.<init>(D)V
      w2 var9 = new w2((double)var10003);
      this.N = var9;
      this.b = new w2(var1 instanceof NumberSetting var7 ? var7.getNormalizedValue() : 0.0);
      this.D = new w2(this.qAsNumberSetting() != null ? this.qAsNumberSetting().getNormalizedValue() : 0.0);
      this.F = this.eAsBoolean() ? 38 : 26;
   }

   public boolean vAsBoolean() {
      return this.e || this.t.isVisible();
   }

   public static SettingWidget LAsQf(Setting<?> var0) {
      if (!(var0 instanceof BooleanSetting)) {
         throw new IllegalArgumentException("Toggle chips require boolean settings");
      } else {
         SettingWidget var1 = new SettingWidget(var0);
         var1.O = true;
         var1.F = 24;
         return var1;
      }
   }

   public boolean LAsBoolean() {
      return this.O;
   }

   public int GAsInt() {
      String var1 = this.LAsString();
      return TextDrawing.UAsInt(this.t.getDisplayName()) + 20 + (var1 == null ? 0 : jAsInt(var1) + 4);
   }

   public String SAsString() {
      if (this.cAsModule() == null) {
         return this.t.getDescription();
      } else {
         int var1 = this.tAsInt();
         String var2 = this.t instanceof BooleanSetting ? "toggle" : "cycle";
         String var3 = var1 == -1
            ? "Middle click to bind a key or mouse button that " + var2 + "s this."
            : "Shortcut: " + KeybindNames.eAsString(var1) + ". Middle click to change, then Escape/Delete to clear.";
         return this.t.getDescription() + " " + var3;
      }
   }

   private Module cAsModule() {
      if (!this.I) {
         this.I = true;
         LarpClient var1 = LarpClient.CAsLarpClient();
         if (var1 != null && var1.mAsF() != null && SettingShortcuts.lAsBoolean(this.t)) {
            this.m = SettingShortcuts.aAsModule(var1.mAsF().NAsList(), this.t);
         }
      }

      return this.m;
   }

   private int tAsInt() {
      Module var1 = this.cAsModule();
      return var1 == null ? -1 : LarpClient.CAsLarpClient().fAsBo().yAsN7().k.KAsInt(var1, this.t);
   }

   private void DAsVoid(int var1) {
      Module var2 = this.cAsModule();
      if (var2 != null && LarpClient.CAsLarpClient().fAsBo().BAsBoolean(var2, this.t, var1)) {
         GuiSounds.rAsVoid(GuiSounds.Type_o.CLICK);
      }
   }

   private String LAsString() {
      if (this.P) {
         return "Press key…";
      } else {
         int var1 = this.tAsInt();
         return var1 == -1 ? null : KeybindNames.eAsString(var1);
      }
   }

   private static int jAsInt(String var0) {
      return TextDrawing.jAsInt(var0, TextDrawing.Type_s.HELP) + 8;
   }

   private void ZAsVoid(DrawContext var1, int var2, int var3, int var4, int var5, int var6) {
      String var7 = this.LAsString();
      if (var7 == null) {
         TextDrawing.UAsVoid(var1, TextDrawing.HAsString(this.t.getDisplayName(), var2), var3, var4, var5);
      } else {
         int var8 = Math.min(jAsInt(var7), Math.max(1, var2 / 2));
         String var9 = TextDrawing.HAsString(this.t.getDisplayName(), Math.max(1, var2 - var8 - 4));
         TextDrawing.UAsVoid(var1, var9, var3, var4, var5);
         int var10 = var3 + TextDrawing.UAsInt(var9) + 4;
         GuiDrawing.CAsVoid(var1, var10, var4 - 2, var8, 13, 3, bg.XAsInt(var5, 0.08));
         TextDrawing.NAsVoid(
            var1,
            TextDrawing.zAsString(var7, var8 - 8, TextDrawing.Type_s.HELP),
            var10 + 4,
            var4,
            this.P ? var6 : bg.XAsInt(var5, 0.7),
            TextDrawing.Type_s.HELP
         );
      }
   }

   public boolean yAsBoolean() {
      return this.z && this.Y;
   }

   public void BAsVoid() {
      this.z = false;
      this.p = false;
      this.A.aAsVoid(0.0);
      if (this.l != null) {
         this.l.hAsVoid();
      }
   }

   private int SAsInt() {
      return this.l == null ? 0 : Math.min(24, this.o);
   }

   private int KAsInt() {
      return Math.max(0, (this.o - this.SAsInt()) / 20);
   }

   private void SAsVoid() {
      this.Z = ((ModeSetting)this.t).EAsList(this.l.W);
      this.n = this.G = 0;
      this.fAsVoid(this.S, this.c);
   }

   private int xAsInt() {
      return Math.min(148, this.V / 2 - 4);
   }

   private int VAsInt() {
      return this.k + this.V - this.xAsInt() - 4;
   }

   private boolean eAsBoolean() {
      if (this.t instanceof NumberSetting var1 && (var1.PAsBoolean() || var1.OAsNumberSetting() != null)) {
         return true;
      }

      return false;
   }

   private int zAsInt() {
      return this.eAsBoolean() ? this.k + 6 : this.VAsInt();
   }

   private int JAsInt() {
      return this.eAsBoolean() ? this.x + 29 : this.x + 11;
   }

   private int QAsInt() {
      return Math.max(1, this.eAsBoolean() ? this.V - 12 : this.xAsInt() - this.TAsInt() - 14);
   }

   private NumberSetting qAsNumberSetting() {
      return this.t instanceof NumberSetting var1 ? var1.OAsNumberSetting() : null;
   }

   private int TAsInt() {
      if (!this.eAsBoolean()) {
         return 44;
      } else if (MinecraftClient.getInstance() == null) {
         return 44;
      } else {
         if (this.u < 0) {
            this.u = Math.clamp((long)(TextDrawing.UAsInt(((NumberSetting)this.t).JAsString()) + 10), 44, 96);
         }

         return this.u;
      }
   }

   private int wAsInt() {
      return this.k + this.V - 4 - this.TAsInt();
   }

   private String MAsString() {
      if (this.t instanceof NumberSetting var8) {
         NumberSetting var11 = var8.OAsNumberSetting();
         long var3 = Double.doubleToLongBits(var8.getValue());
         long var5 = var11 == null ? 0L : Double.doubleToLongBits(var11.getValue());
         if (this.q == null || this.h != var3 || this.K != var5) {
            this.q = !var8.PAsBoolean() && var11 == null ? BigDecimal.valueOf(var8.getValue()).stripTrailingZeros().toPlainString() : var8.XAsString();
            this.h = var3;
            this.K = var5;
         }

         return this.q;
      } else if (this.t instanceof KeybindSetting var7) {
         return KeybindNames.eAsString(var7.getValue());
      } else {
         return this.t instanceof ModeSetting var1 ? var1.getModeLabel(var1.getValue()) : this.t.getValueAsString();
      }
   }

   public void fAsVoid(int var1, int var2) {
      if (this.l == null && this.t instanceof ModeSetting var3) {
         this.Z = var3.getVisibleModes();
      }

      this.S = var1;
      this.c = var2;
      this.M = this.t instanceof ModeSetting ? this.VAsInt() : this.k;
      this.L = this.t instanceof ModeSetting ? this.xAsInt() : this.V;
      int var5 = this.t instanceof ModeSetting ? (this.l == null ? 0 : 24) + Math.max(1, Math.min(8, this.Z.size())) * 20 : 80;
      this.o = Math.min(var5, Math.max(0, var2 - var1));
      int var6 = (int)Math.round((1.0 - this.A.bAsDouble(this.z ? 1.0 : 0.0, 160)) * 4.0);
      this.C = Math.clamp((long)(this.x + this.F + var6), var1, Math.max(var1, var2 - this.o));
      if (this.t instanceof ModeSetting) {
         this.n = Math.clamp((long)this.n, 0, Math.max(0, this.Z.size() - Math.max(1, this.KAsInt())));
      }

      if (this.l != null) {
         this.l.k = this.M + 3;
         this.l.x = this.C + 2;
         this.l.V = Math.max(1, this.L - 6);
         this.l.F = Math.max(0, this.SAsInt() - 4);
      }
   }

   public boolean uAsBoolean(double var1, double var3) {
      return this.yAsBoolean() && GuiTextureDrawing.ZAsBoolean(var1, var3, this.M, this.C, this.L, this.o);
   }

   @Override
   public void render(DrawContext var1, int var2, int var3) {
      if (this.Y) {
         boolean var4 = this.contains((double)var2, (double)var3);
         double var5 = this.d.bAsDouble(!var4 && !this.y ? 0.0 : 1.0, 150);
         int var7 = mi.readableAccent(bd.tAsInt(this.k), bd.m);
         if (this.O && this.t instanceof BooleanSetting var19) {
            double var21 = this.N.bAsDouble(var19.getValue() ? 1.0 : 0.0, 150);
            int var25 = mi.readableAccent(bd.tAsInt(this.k), bd.M | 0xFF000000);
            int var27 = bg.xAsInt(bd._Z | 0xFF000000, bd.h | 0xFF000000, var5);
            int var28 = bg.xAsInt(var27, var25, var21);
            int var31 = bd.hAsBoolean() ? this.F / 2 : 7;
            if (var21 > 0.01) {
               GuiDrawing.HAsVoid(var1, (float)this.k, (float)this.x, (float)this.V, (float)this.F, (float)var31, 5.0F, bg.XAsInt(var25, 0.22 * var21));
            }

            GuiDrawing.CAsVoid(var1, this.k, this.x, this.V, this.F, var31, var28);
            GuiDrawing.wAsVoid(var1, this.k, this.x, this.V, this.F, var31, bg.xAsInt(bd.A, var25, var21));
            if (this.y && this.Q) {
               GuiDrawing.dAsVoid(var1, this.k, this.x, this.V, this.F, var31);
            }

            this.ZAsVoid(var1, this.V - 16, this.k + 8, this.x + (this.F - 9) / 2, mi.controlInk(var28), var7);
         } else {
            if (var5 > 0.001 && bd.hAsBoolean()) {
               GuiDrawing.CAsVoid(var1, this.k, this.x, this.V, this.F, bd.T + 1, bg.XAsInt(bd.u, 0.028 * var5));
            }

            this.ZAsVoid(
               var1,
               this.t instanceof BooleanSetting ? this.V - 42 : (this.eAsBoolean() ? this.V - this.TAsInt() - 16 : this.V / 2 - 12),
               this.k + 4,
               this.x + 7,
               bd.u,
               var7
            );
            int var8 = this.y && this.Q ? 1 : 0;
            if (this.t instanceof BooleanSetting var20) {
               if (var8 != 0) {
                  GuiDrawing.dAsVoid(var1, this.k + this.V - 28, this.x + 6, 24, 13, 6);
               }

               GuiDrawing.uAsVoid(var1, this.k + this.V - 28, this.x + 6, this.N.bAsDouble(var20.getValue() ? 1.0 : 0.0, 180));
            } else if (this.t instanceof NumberSetting var10) {
               GuiDrawing.TAsVoid(var1, this.wAsInt(), this.x + 3, this.TAsInt(), 20, !this.E && var8 == 0 ? var5 * 0.3 : 1.0);
               String var23 = TextDrawing.HAsString(this.E ? this.s + "_" : this.MAsString(), this.TAsInt() - 10);
               TextDrawing.UAsVoid(
                  var1, var23, this.wAsInt() + this.TAsInt() - 6 - TextDrawing.UAsInt(var23), this.x + 7, this.E ? bd.u : bg.xAsInt(bd.e, bd.u, var5)
               );
               if (this.p) {
                  this.b.aAsVoid(var10.getNormalizedValue());
               }

               double var12 = this.b.bAsDouble(var10.getNormalizedValue(), 160);
               NumberSetting var14 = this.qAsNumberSetting();
               if (bd.hAsBoolean()) {
                  double var32 = -1.0;
                  if (var14 != null) {
                     if (this.p) {
                        this.D.aAsVoid(var14.getNormalizedValue());
                     }

                     var32 = this.D.bAsDouble(var14.getNormalizedValue(), 160);
                  }

                  this.NAsVoid(var1, var12, var32, Math.max(var5, this.p ? 1.0 : 0.0), var7);
                  return;
               }

               GuiDrawing.CAsVoid(var1, this.zAsInt(), this.JAsInt() + 1, this.QAsInt(), 2, 1, bd.A);
               if (var14 == null) {
                  GuiDrawing.CAsVoid(var1, this.zAsInt(), this.JAsInt() + 1, (int)((double)this.QAsInt() * var12), 2, 1, var7);
                  bAsVoid(var1, this.zAsInt() + (int)((double)this.QAsInt() * var12), this.JAsInt() + 2, var5);
               } else {
                  if (this.p) {
                     this.D.aAsVoid(var14.getNormalizedValue());
                  }

                  double var15 = this.D.bAsDouble(var14.getNormalizedValue(), 160);
                  int var17 = (int)((double)this.QAsInt() * var12);
                  int var18 = (int)((double)this.QAsInt() * var15);
                  GuiDrawing.CAsVoid(var1, this.zAsInt() + var17, this.JAsInt() + 1, Math.max(0, var18 - var17), 2, 1, var7);
                  bAsVoid(var1, this.zAsInt() + var17, this.JAsInt() + 2, var5);
                  bAsVoid(var1, this.zAsInt() + var18, this.JAsInt() + 2, var5);
               }
            } else {
               int var24 = this.VAsInt();
               int var26 = this.xAsInt();
               GuiDrawing.TAsVoid(var1, var24, this.x + 2, var26, 20, !this.z && !this.w && var8 == 0 ? var5 * 0.3 : 1.0);
               if (this.t instanceof TextSetting) {
                  this.g.bounds(var24, this.x + 2, var26);
                  this.g.F = 20;
                  if (!this.r) {
                     this.g.W = this.MAsString();
                  }

                  this.g.render(var1, var2, var3);
               } else if (this.t instanceof ColorSetting var13) {
                  TextDrawing.UAsVoid(var1, TextDrawing.HAsString(String.format("#%08X", var13.getValue()), var26 - 28), var24 + 6, this.x + 6, bd.e);
                  GuiDrawing.CAsVoid(var1, this.k + this.V - 23, this.x + 6, 14, 12, 3, var13.getValue());
                  RoundedShapePipeline.SAsVoid(
                     var1, (float)(this.k + this.V - 23), (float)(this.x + 6), 14.0F, 12.0F, 3.0F, bg.pAsInt(bd.I, 2.0), bg.pAsInt(bd.a, 2.5)
                  );
               } else {
                  int var30;
                  byte var37;
                  label154: {
                     var30 = this.t instanceof ModeSetting var33 ? VAsInt(var1, var33, var33.getValue(), var24 + 6, this.x + 4) : 0;
                     if (this.t instanceof KeybindSetting var35 && var35.getValue() < 0) {
                        var37 = 1;
                        break label154;
                     }

                     var37 = 0;
                  }

                  int var34 = var37;
                  TextDrawing.UAsVoid(
                     var1,
                     TextDrawing.HAsString(this.w ? "Press key…" : this.MAsString(), var26 - 22 - var30),
                     var24 + 6 + var30,
                     this.x + 6,
                     this.w ? var7 : (var34 != 0 ? bd.e : bd.u)
                  );
                  if (this.t instanceof ModeSetting) {
                     GuiIcons.aAsVoid(var1, this.z ? GuiIcons.S : GuiIcons.C, this.k + this.V - 20, this.x + 5, 14, bd.e);
                  }
               }
            }
         }
      }
   }

   private void NAsVoid(DrawContext var1, double var2, double var4, double var6, int var8) {
      String var9 = bd.yAsBd_Type_p().slider();
      switch (var9) {
         case "Minimal":
         case "Classic":
            this.KAsVoid(var1, var2, var4, var6, var8, bd.yAsBd_Type_p().slider().equals("Classic"));
            return;
         default:
            float var23 = (float)(this.JAsInt() + 2);
            float var24 = (float)this.QAsInt();
            float var11 = (float)this.zAsInt();
            float var12 = 6.0F + (float)var6;
            float var13 = var12 / 2.0F;
            float var14 = var23 - var13;
            int var15 = var4 >= 0.0 ? 1 : 0;
            RoundedShapePipeline.nAsVoid(var1, var11, var14, var24, var12, var13, bg.XAsInt(bd.u, 0.1));
            RoundedShapePipeline.SAsVoid(var1, var11, var14, var24, var12, var13, bg.XAsInt(bd.A, 0.5), bg.XAsInt(bd.A, 0.5));
            float var16 = var15 != 0 ? (float)((double)var24 * Math.min(var2, var4)) : 0.0F;
            float var17 = (float)((double)var24 * (var15 != 0 ? Math.max(var2, var4) : var2));
            float var18 = var11 + Math.max(0.0F, Math.min(var16 - var13, var24 - var12));
            float var19 = var11 + Math.min(var24, Math.max(var17 + var13, var16 + var12));
            float var20 = Math.max(var12, var19 - var18);
            int var21 = bg.xAsInt(var8, bd.Q | 0xFF000000, 0.6);
            if (var6 > 0.01) {
               GuiDrawing.HAsVoid(var1, var18, var14, var20, var12, var13, 5.0F, bg.XAsInt(var8, 0.35 * var6));
            }

            RoundedShapePipeline.gAsVoid(var1, var18, var14, var20, var12, var13, var21, var8);
            RoundedShapePipeline.tAsVoid(var1, var18, var14, var20, var12 / 2.0F, var13, 956301311, 16777215);
            float var22 = var12 - 2.5F;
            if (var15 != 0) {
               NAsVoid(var1, var18 + var13, var23, var22, this.p && this.R == 1 ? 1.0 : 0.0, var8);
               NAsVoid(var1, var18 + var20 - var13, var23, var22, this.p && this.R == 2 ? 1.0 : 0.0, var8);
            } else {
               NAsVoid(var1, var18 + var20 - var13, var23, var22, this.p ? 1.0 : 0.0, var8);
            }
      }
   }

   private void KAsVoid(DrawContext var1, double var2, double var4, double var6, int var8, boolean var9) {
      float var10 = (float)(this.JAsInt() + 2);
      float var11 = (float)this.QAsInt();
      float var12 = (float)this.zAsInt();
      float var13 = var9 ? 4.0F : 2.5F + (float)var6;
      float var14 = var10 - var13 / 2.0F;
      int var15 = var4 >= 0.0 ? 1 : 0;
      RoundedShapePipeline.nAsVoid(var1, var12, var14, var11, var13, var13 / 2.0F, bg.XAsInt(bd.u, var9 ? 0.14 : 0.16));
      float var16 = var15 != 0 ? (float)((double)var11 * Math.min(var2, var4)) : 0.0F;
      float var17 = (float)((double)var11 * (var15 != 0 ? Math.max(var2, var4) : var2));
      float var18 = Math.max(var13, var17 - var16);
      if (var9) {
         if (var6 > 0.01) {
            GuiDrawing.HAsVoid(var1, var12 + var16, var14, var18, var13, var13 / 2.0F, 5.0F, bg.XAsInt(var8, 0.4 * var6));
         }

         RoundedShapePipeline.gAsVoid(var1, var12 + var16, var14, var18, var13, var13 / 2.0F, bg.xAsInt(var8, bd.Q | 0xFF000000, 0.6), var8);
      } else {
         RoundedShapePipeline.nAsVoid(var1, var12 + var16, var14, var18, var13, var13 / 2.0F, var8);
      }

      float var19 = (float)(var9 ? 7 : 6) + 2.0F * (float)var6;
      if (var15 != 0) {
         GAsVoid(var1, var12 + var16, var10, var19, var8, var9);
      }

      GAsVoid(var1, var12 + var17, var10, var19, var8, var9);
   }

   private static void GAsVoid(DrawContext var0, float var1, float var2, float var3, int var4, boolean var5) {
      float var6 = var1 - var3 / 2.0F;
      float var7 = var2 - var3 / 2.0F;
      RoundedShapePipeline.CAsVoid(var0, var6, var7, var3, var3, var3 / 2.0F, 1.5F, 1073741824, 1342177280);
      RoundedShapePipeline.nAsVoid(var0, var6, var7, var3, var3, var3 / 2.0F, var5 ? -1 : bd.m | 0xFF000000);
      RoundedShapePipeline.cAsVoid(var0, var6, var7, var3, var3, var3 / 2.0F, var4);
   }

   private static void NAsVoid(DrawContext var0, float var1, float var2, float var3, double var4, int var6) {
      float var7 = var1 - var3 / 2.0F;
      float var8 = var2 - var3 / 2.0F;
      RoundedShapePipeline.CAsVoid(var0, var7, var8, var3, var3, var3 / 2.0F, 1.5F, 1174405120, 1375731712);
      if (var4 > 0.01) {
         GuiDrawing.HAsVoid(var0, var7, var8, var3, var3, var3 / 2.0F, 5.0F, bg.XAsInt(var6, 0.45 * var4));
      }

      RoundedShapePipeline.nAsVoid(var0, var7, var8, var3, var3, var3 / 2.0F, -1);
   }

   private static void bAsVoid(DrawContext var0, int var1, int var2, double var3) {
      int var5 = 8 + (int)Math.round(var3);
      RoundedShapePipeline.nAsVoid(
         var0, (float)(var1 - var5 / 2 - 1), (float)(var2 - var5 / 2 - 1), (float)(var5 + 2), (float)(var5 + 2), (float)var5 / 2.0F + 1.0F, 855638016
      );
      RoundedShapePipeline.nAsVoid(var0, (float)(var1 - var5 / 2), (float)(var2 - var5 / 2), (float)var5, (float)var5, (float)var5 / 2.0F, -723722);
   }

   public void oAsVoid(DrawContext var1, int var2, int var3) {
      if (this.yAsBoolean()) {
         try (qg var4 = qg.nAsQg(this.A.bAsDouble(1.0, 160))) {
            boolean var5 = bd.hAsBoolean();
            if (var5) {
               GuiDrawing.PAsVoid(var1, this.M, this.C, this.L, this.o, bd.p, bd.V, 1.0);
            } else {
               GuiDrawing.bAsVoid(var1, this.M, this.C, this.L, this.o);
               GuiDrawing.OAsVoid(var1, this.M, this.C, this.L, this.o, bd.T, bd.V, 1.0);
            }

            var1.enableScissor(this.M, this.C, this.M + this.L, this.C + this.o);

            try {
               if (this.t instanceof ModeSetting var6) {
                  if (this.l != null) {
                     this.l.render(var1, var2, var3);
                  }

                  int var26 = this.C + this.SAsInt();
                  int var27 = Math.min(this.Z.size() - this.n, this.KAsInt());
                  if (this.Z.isEmpty() && this.KAsInt() > 0) {
                     TextDrawing.UAsVoid(var1, "No matching blocks", this.M + 8, var26 + 6, bd.e);
                  }

                  for (int var28 = 0; var28 < var27; var28++) {
                     String var29 = this.Z.get(var28 + this.n);
                     int var30 = var26 + var28 * 20;
                     boolean var32 = GuiTextureDrawing.ZAsBoolean((double)var2, (double)var3, this.M, var30, this.L, 20);
                     boolean var33 = var6.pAsBoolean(var29);
                     int var34 = var32 || this.l != null && this.G == var28 + this.n ? 1 : 0;
                     if (var5 && var33) {
                        GuiDrawing.CAsVoid(var1, this.M + 2, var30 + 1, this.L - 4, 18, bd.T, bd.RAsInt(this.M, var34 != 0 ? 1.3 : 1.0));
                     } else if (var34 != 0 || var33) {
                        GuiDrawing.CAsVoid(var1, this.M + 2, var30 + 1, this.L - 4, 18, bd.T, var5 ? bg.XAsInt(bd.u, 0.08) : bd.h);
                     }

                     int var16 = VAsInt(var1, var6, var29, this.M + 8, var30 + 2);
                     TextDrawing.UAsVoid(
                        var1,
                        TextDrawing.HAsString(var6.getModeLabel(var29), this.L - 16 - var16),
                        this.M + 8 + var16,
                        var30 + 6,
                        !var33 && !var32 ? bg.xAsInt(bd.e, bd.u, 0.5) : bd.u
                     );
                  }

                  GuiDrawing.aAsVoid(var1, this.M + this.L - 3, var26, this.KAsInt() * 20, this.Z.size() * 20, this.n * 20);
               } else if (this.t instanceof ColorSetting var7) {
                  float[] var25 = Color.RGBtoHSB(var7.getValue() >> 16 & 0xFF, var7.getValue() >> 8 & 0xFF, var7.getValue() & 0xFF, null);
                  String[] var9 = new String[]{"H", "S", "V", "A"};
                  int var10 = Math.max(1, this.L - 28);

                  for (int var11 = 0; var11 < 4; var11++) {
                     int var12 = this.C + 6 + var11 * 18;
                     TextDrawing.UAsVoid(var1, var9[var11], this.M + 5, var12, bd.e);

                     for (int var13 = 0; var13 < var10; var13 += 3) {
                        float var14 = (float)var13 / (float)var10;

                        int var15 = switch (var11) {
                           case 0 -> bg.HSBtoRGB(var14, 1.0F, 1.0F);
                           case 1 -> bg.HSBtoRGB(var25[0], var14, var25[2]);
                           case 2 -> bg.HSBtoRGB(var25[0], var25[1], var14);
                           default -> bg.xAsInt(bd.d, var7.getValue() | 0xFF000000, (double)var14);
                        };
                        GuiTextureDrawing.CAsVoid(var1, this.M + 20 + var13, var12, Math.min(3, var10 - var13), 10, var15);
                     }

                     float var31 = var11 < 3 ? var25[var11] : (float)(var7.getValue() >>> 24) / 255.0F;
                     GuiTextureDrawing.pAsVoid(var1, this.M + 19 + (int)(var31 * (float)var10), var12 - 1, 3, 12, bd.u);
                  }
               }
            } finally {
               var1.disableScissor();
            }

            if (!var5) {
               RoundedShapePipeline.SAsVoid(
                  var1, (float)this.M, (float)this.C, (float)this.L, (float)this.o, (float)bd.T, bg.pAsInt(bd.I, 1.5), bg.pAsInt(bd.a, 1.5)
               );
            }
         }
      }
   }

   private static int VAsInt(DrawContext var0, ModeSetting var1, String var2, int var3, int var4) {
      int var5 = var1.drawModeIcon(var0, var2, var3, var4);
      if (var5 > 0) {
         return var5;
      } else {
         ItemStack var6 = var1.getModeIcon(var2);
         if (var6.isEmpty()) {
            return 0;
         } else {
            var0.drawItem(var6, var3, var4);
            return 18;
         }
      }
   }

   public boolean oAsBoolean(double var1, double var3, int var5) {
      if (!this.uAsBoolean(var1, var3)) {
         return false;
      } else if (var5 != 0) {
         return true;
      } else {
         if (this.t instanceof ModeSetting var6) {
            if (this.l != null && var3 < (double)(this.C + this.SAsInt())) {
               this.l.click(var1, var3, var5);
               return true;
            }

            int var8 = (int)(var3 - (double)this.C - (double)this.SAsInt()) / 20;
            if (var8 < 0 || var8 >= this.KAsInt() || var8 + this.n >= this.Z.size()) {
               return true;
            }

            this.TAsVoid(var6, this.Z.get(var8 + this.n));
            this.BAsVoid();
            GuiSounds.rAsVoid(GuiSounds.Type_o.CLICK);
         } else if (this.t instanceof ColorSetting) {
            this.X = Math.clamp((long)((int)(var3 - (double)this.C - 6.0) / 18), 0, 3);
            this.p = true;
            this.drag(var1, var3);
         }

         return true;
      }
   }

   private void TAsVoid(ModeSetting var1, String var2) {
      try {
         var1.setValue(var2);
      } catch (IllegalArgumentException var5) {
         FakeSpawner var4 = FakeSpawner.kAsFakeSpawner();
         if (var4 == null || !var4.RAsList().contains(var1)) {
            throw var5;
         }

         ChatMessages.RAsVoid(Text.literal(var5.getMessage()), true);
      }
   }

   public void OAsVoid(double var1) {
      if (this.t instanceof ModeSetting) {
         this.n -= (int)var1;
         this.fAsVoid(this.S, this.c);
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (!this.contains(var1, var3)) {
         return false;
      } else {
         this.y = true;
         if (var5 == 2 && this.cAsModule() != null && !this.P) {
            this.E = false;
            this.r = false;
            this.w = false;
            this.BAsVoid();
            if (this.g != null) {
               this.g.hAsVoid();
            }

            this.P = true;
            GuiSounds.rAsVoid(GuiSounds.Type_o.CLICK);
            return true;
         } else if (var5 == 1) {
            this.E = false;
            this.r = false;
            this.w = false;
            this.P = false;
            if (this.g != null) {
               this.g.hAsVoid();
            }

            if (this.t instanceof ModeSetting var11) {
               this.TAsVoid(var11, var11.getDefaultValue());
            } else {
               this.t.reset();
            }

            if (this.qAsNumberSetting() != null) {
               this.qAsNumberSetting().reset();
            }

            GuiSounds.rAsVoid(GuiSounds.Type_o.CLICK);
            return true;
         } else if (var5 != 0) {
            return false;
         } else if (this.P) {
            this.P = false;
            return true;
         } else {
            if (this.t instanceof BooleanSetting var6) {
               var6.toggle();
               GuiSounds.rAsVoid(var6.getValue() ? GuiSounds.Type_o.ON : GuiSounds.Type_o.OFF);
            } else if (this.t instanceof NumberSetting var7) {
               if (this.qAsNumberSetting() != null) {
                  if (var3 >= (double)(this.x + 22)) {
                     double var14 = this.xAsDouble(var1);
                     this.R = Math.abs(var14 - var7.getValue()) > Math.abs(var14 - this.qAsNumberSetting().getValue()) ? 2 : 1;
                     this.p = true;
                     this.drag(var1, var3);
                  }
               } else if (this.eAsBoolean()
                  ? !(var3 < (double)(this.x + 24)) || !(var1 >= (double)(this.wAsInt() - 4))
                  : !(var1 >= (double)(this.wAsInt() - 4))) {
                  if (this.eAsBoolean() ? var3 >= (double)(this.x + 22) : var1 >= (double)(this.VAsInt() - 4)) {
                     this.mAsVoid();
                     this.p = true;
                     this.drag(var1, var3);
                  }
               } else {
                  this.s = var7.getEditValueAsString();
                  this.E = true;
                  this.a = true;
               }
            } else if (this.t instanceof ModeSetting var8) {
               this.z = !this.z;
               GuiSounds.rAsVoid(this.z ? GuiSounds.Type_o.SETTINGS_OPEN : GuiSounds.Type_o.SETTINGS_CLOSE);
               if (this.l != null) {
                  this.l.W = "";
                  this.l.y = this.z;
               }

               this.Z = var8.getVisibleModes();
               this.G = Math.max(0, this.Z.indexOf(var8.getValue()));
               this.n = Math.max(0, this.G - 2);
               this.fAsVoid(this.S, this.c);
            } else if (this.t instanceof ColorSetting) {
               this.z = !this.z;
               GuiSounds.rAsVoid(this.z ? GuiSounds.Type_o.SETTINGS_OPEN : GuiSounds.Type_o.SETTINGS_CLOSE);
            } else if (this.t instanceof TextSetting) {
               this.g.bounds(this.VAsInt(), this.x + 2, this.xAsInt());
               this.g.F = 20;
               this.g.W = this.MAsString();
               this.r = true;
               this.g.click((double)(this.VAsInt() + 2), (double)(this.x + 4), 0);
               this.g.UAsVoid();
            } else if (this.t instanceof KeybindSetting) {
               this.w = true;
               GuiSounds.rAsVoid(GuiSounds.Type_o.CLICK);
            }

            return true;
         }
      }
   }

   @Override
   public void drag(double var1, double var3) {
      if (this.p) {
         if (this.qAsNumberSetting() != null) {
            if (this.uAsBoolean(this.xAsDouble(var1))) {
               GuiSounds.rAsVoid(GuiSounds.Type_o.SLIDER);
            }
         } else if (this.t instanceof NumberSetting var5) {
            double var12 = var5.getValue();
            var5.vAsVoid((var1 - (double)this.zAsInt()) / (double)this.QAsInt());
            if (var12 != var5.getValue()) {
               GuiSounds.rAsVoid(GuiSounds.Type_o.SLIDER);
            }
         }

         if (this.t instanceof ColorSetting var11) {
            double var14 = Math.clamp((var1 - (double)this.M - 20.0) / (double)Math.max(1, this.L - 28), 0.0, 1.0);
            int var8 = var11.getValue();
            float[] var9 = Color.RGBtoHSB(var8 >> 16 & 0xFF, var8 >> 8 & 0xFF, var8 & 0xFF, null);
            if (this.X < 3) {
               var9[this.X] = (float)var14;
            }

            int var10 = this.X == 3 ? (int)(var14 * 255.0) : var8 >>> 24;
            var11.setValue(Integer.valueOf(bg.HSBtoRGB(var9[0], var9[1], var9[2]) & 16777215 | var10 << 24));
            if (var8 != var11.getValue()) {
               GuiSounds.rAsVoid(GuiSounds.Type_o.SLIDER);
            }
         }
      }
   }

   private double xAsDouble(double var1) {
      NumberSetting var3 = (NumberSetting)this.t;
      return var3.getMin() + Math.clamp((var1 - (double)this.zAsInt()) / (double)this.QAsInt(), 0.0, 1.0) * (var3.getMax() - var3.getMin());
   }

   private boolean uAsBoolean(double var1) {
      NumberSetting var3 = (NumberSetting)this.t;
      NumberSetting var4 = this.qAsNumberSetting();
      double var5 = var3.getValue();
      double var7 = var4.getValue();
      if (this.R == 1) {
         var3.setClampedValue(Math.min(var1, var4.getValue()));
      } else {
         var4.setClampedValue(Math.max(var1, var3.getValue()));
      }

      return var5 != var3.getValue() || var7 != var4.getValue();
   }

   private void mAsVoid() {
      if (this.E) {
         try {
            ((NumberSetting)this.t).setFromEditString(this.s);
         } catch (IllegalArgumentException var1) {
         }

         this.E = false;
      }
   }

   private void OAsVoid() {
      if (this.r && this.t instanceof TextSetting var1) {
         try {
            var1.dAsVoid(this.g.W);
         } catch (IllegalArgumentException var3) {
         }

         this.r = false;
         this.g.hAsVoid();
      }
   }

   @Override
   public void release() {
      this.p = false;
   }

   public void blur() {
      super.hAsVoid();
      this.w = false;
      this.P = false;
      this.BAsVoid();
      this.mAsVoid();
      this.OAsVoid();
   }

   public boolean xAsBoolean() {
      return this.y && (this.E || this.r || this.w || this.P || this.z && this.l != null);
   }

   public boolean captureMouse(int var1) {
      if (this.y && KeybindNames.RAsBoolean(var1)) {
         int var2 = KeybindNames.ZAsInt(var1);
         if (this.w && this.t instanceof KeybindSetting var3) {
            var3.lAsVoid(var2);
            this.w = false;
            GuiSounds.rAsVoid(GuiSounds.Type_o.CLICK);
            return true;
         } else if (this.P) {
            this.P = false;
            this.DAsVoid(var2);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   @Override
   public boolean character(int var1) {
      if (this.y && this.r) {
         return this.g.character(var1);
      } else if (this.y && this.z && this.l != null) {
         return this.l.character(var1);
      } else if (this.y && this.E && "0123456789.-+eE".indexOf(var1) >= 0) {
         if (this.a) {
            this.s = "";
            this.a = false;
         }

         if (this.s.length() < 24) {
            this.s = this.s + (char)var1;
            GuiSounds.rAsVoid(GuiSounds.Type_o.TYPING);
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (!this.y) {
         return false;
      } else if (this.r) {
         if (var1 == 256) {
            this.r = false;
            this.g.hAsVoid();
            return true;
         } else if (var1 != 257 && var1 != 335) {
            return this.g.key(var1);
         } else {
            this.OAsVoid();
            return true;
         }
      } else if (this.w && this.t instanceof KeybindSetting var8) {
         var8.lAsVoid(var1 != 256 && var1 != 261 ? var1 : -1);
         this.w = false;
         return true;
      } else if (this.P) {
         this.P = false;
         this.DAsVoid(var1 != 256 && var1 != 261 ? var1 : -1);
         return true;
      } else if (this.E) {
         if (var1 == 256) {
            this.E = false;
         } else if (var1 == 257) {
            this.mAsVoid();
         } else if (var1 == 259) {
            this.s = !this.a && !this.s.isEmpty() ? this.s.substring(0, this.s.length() - 1) : "";
            this.a = false;
         }

         return true;
      } else if (this.z && var1 == 256) {
         this.BAsVoid();
         return true;
      } else if (this.z && this.l != null && this.t instanceof ModeSetting var7) {
         if (var1 == 264 || var1 == 265) {
            this.G = Math.clamp((long)(this.G + (var1 == 264 ? 1 : -1)), 0, Math.max(0, this.Z.size() - 1));
            if (this.G < this.n) {
               this.n = this.G;
            } else if (this.G >= this.n + Math.max(1, this.KAsInt())) {
               this.n = this.G - Math.max(1, this.KAsInt()) + 1;
            }

            this.fAsVoid(this.S, this.c);
            return true;
         } else if (var1 != 257 && var1 != 335) {
            return this.l.key(var1);
         } else {
            if (!this.Z.isEmpty()) {
               this.TAsVoid(var7, this.Z.get(this.G));
               this.BAsVoid();
               GuiSounds.rAsVoid(GuiSounds.Type_o.CLICK);
            }

            return true;
         }
      } else {
         if (this.t instanceof ModeSetting var2 && this.z && (var1 == 264 || var1 == 265)) {
            List var15 = var2.getVisibleModes();
            int var4 = Math.clamp((long)(var15.indexOf(var2.getValue()) + (var1 == 264 ? 1 : -1)), 0, var15.size() - 1);
            this.TAsVoid(var2, (String)var15.get(var4));
            this.n = Math.max(0, var4 - 2);
            this.fAsVoid(this.S, this.c);
            return true;
         }

         if (this.t instanceof NumberSetting var5 && this.qAsNumberSetting() != null && (var1 == 262 || var1 == 263)) {
            double var14 = this.R == 1 ? var5.getValue() : this.qAsNumberSetting().getValue();
            if (this.uAsBoolean(var14 + (double)(var1 == 262 ? 1 : -1) * var5.getStep())) {
               GuiSounds.rAsVoid(GuiSounds.Type_o.SLIDER);
            }

            return true;
         }

         if (this.t instanceof NumberSetting var6 && (var1 == 262 || var1 == 263)) {
            double var13 = var6.getValue();
            var6.setClampedValue(var13 + (double)(var1 == 262 ? 1 : -1) * var6.getStep());
            if (var13 != var6.getValue()) {
               GuiSounds.rAsVoid(GuiSounds.Type_o.SLIDER);
            }

            return true;
         }

         return var1 != 257 && var1 != 32 ? false : this.click(this.O ? (double)this.k + (double)this.V / 2.0 : (double)(this.k + 2), (double)(this.x + 2), 0);
      }
   }
}
