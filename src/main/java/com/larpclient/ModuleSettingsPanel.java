package com.larpclient;

import com.larpclient.module.AutoSell;
import com.larpclient.module.AutoTrap;
import com.larpclient.module.CustomCrosshair;
import com.larpclient.module.FakePay;
import com.larpclient.module.FakeSpawner;
import com.larpclient.module.HUD;
import com.larpclient.module.HitFX;
import com.larpclient.module.HitSounds;
import com.larpclient.module.KeybindSetting;
import com.larpclient.module.KillFX;
import com.larpclient.module.LARP;
import com.larpclient.module.Module;
import com.larpclient.module.NumberSetting;
import com.larpclient.module.Setting;
import com.larpclient.module.SkinChanger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BooleanSupplier;
import net.minecraft.client.gui.DrawContext;

final class ModuleSettingsPanel extends SettingsPanel {
   private Module p;
   private String w = "";
   private String A = "";
   private String y = "";
   private String G = "";
   private ActionButton U;
   private int o;
   private int Y;
   private final Set<String> j = new HashSet<>();
   private final Map<String, ActionButton> e = new HashMap<>();
   private final Map<GuiWidget, BooleanSupplier> Z = new IdentityHashMap<>();
   private String L;
   private ModuleToggleWidget f;

   ModuleSettingsPanel() {
      super("Module");
   }

   void iAsVoid(Module var1) {
      if (this.p instanceof HitSounds var2) {
         var2.NAsVoid();
      }

      this.p = var1;
      this.j.clear();
      this.s = 0;
      if (var1 instanceof FakePay var4) {
         this.A = var4.TAsString();
      }

      this.y = "";
      this.G = "";
      this.build();
   }

   void uAsVoid(XModel var1, int var2, int var3) {
      this.o = var2;
      this.Y = var3;
      this.hAsVoid(var1);
   }

   @Override
   protected double revealAmount() {
      return 1.0;
   }

   @Override
   protected boolean renderPopupInline() {
      return false;
   }

   @Override
   protected boolean horizontalInput(double var1) {
      return var1 >= (double)this.o && var1 < (double)this.Y;
   }

   void RAsVoid() {
      int var1 = this.s;
      this.build();
      this.layout();
      this.s = var1;
      this.layout();
   }

   @Override
   protected void build() {
      this.JAsVoid();
      this.e.clear();
      this.Z.clear();
      this.U = null;
      this.f = null;
      if (this.p != null) {
         Module var1 = this.p;
         this.L = var1 instanceof Notification var2 ? var2.style().getValue() : null;
         this.W = var1.getDisplayName();
         this.f = new ModuleToggleWidget("", var1);
         this.Q.add(this.f);
         if (!var1.getDescription().isBlank()) {
            this.kAsVoid(var1.getDescription());
         }

         if (var1 instanceof AutoTrap var9) {
            this.QAsVoid(var9::getStatus);
         }

         if (var1 instanceof CustomCrosshair var10) {
            this.AAsVoid(new ModuleSettingsPanel.Type_v(var10));
            ActionButton var3 = this.MAsQo("Clear drawing", var10::TAsVoid);
            var3.u = true;
            this.Z.put(var3, var10::YAsBoolean);
         }

         if (var1 instanceof AutoSell var11) {
            this.QAsVoid(var11::qAsString);
            this.MAsQo("Choose items to sell", () -> var11.uAsVoid(this.i, false));
            this.MAsQo("Choose excluded items", () -> var11.uAsVoid(this.i, true));
            this.MAsQo("Add held item to selection", () -> {
               try {
                  var11.BAsVoid(false);
                  this.G = "Held item added.";
               } catch (IllegalArgumentException var3x) {
                  this.G = var3x.getMessage();
               }
            });
            this.QAsVoid(() -> this.G);
         }

         Iterator var12 = ControlSettings.FAsList(var1).iterator();

         while (true) {
            ControlSettings.Type_h var18;
            do {
               if (!var12.hasNext()) {
                  if (var1 instanceof HUD) {
                     this.MAsQo("Open HUD editor", () -> this.i.eAsVoid());
                  }

                  if (var1 instanceof SkinChanger var13) {
                     this.MAsQo("Apply skin", var13::KAsVoid);
                     this.MAsQo("Refresh skins", () -> {
                        var13.fAsVoid();
                        this.build();
                        this.layout();
                     });
                     this.MAsQo("Open skins folder", var13::YAsVoid);
                     this.QAsVoid(() -> "Add a 64x64 or 64x32 PNG to the skins folder, then refresh.");
                  }

                  if (var1 instanceof HitSounds var14) {
                     this.MAsQo("Preview sound", var14::IAsVoid);
                  }

                  if (var1 instanceof LARP var15) {
                     TextInputWidget var19 = new TextInputWidget("Minecraft username", var1x -> this.w = var1x, 16);
                     var19.W = this.w;
                     this.AAsVoid(var19);
                     this.U = new ActionButton("Start larping", () -> {
                        if (!this.w.trim().matches("[A-Za-z0-9_]{1,16}")) {
                           AppearanceAssets.O = "Enter a Minecraft username";
                        } else {
                           var15.setEnabled(true);
                           AppearanceAssets.eAsVoid(this.w.trim());
                        }
                     });
                     this.AAsVoid(this.U);
                  }

                  if (!(var1 instanceof Notification)) {
                     this.AAsVoid(new SectionHeaderWidget("Keybind"));
                     KeybindSetting var16 = new KeybindSetting(
                        var1 instanceof HUD ? "Toggle HUD" : "Keybind",
                        "Press a key, or a middle or side mouse button. Escape/Delete clears the binding",
                        var1.getKeyBind()
                     );
                     var16.onChanged(var1::setKeyBind);
                     this.AAsVoid(new SettingWidget(var16));
                     if (var1.keybindMode() != null) {
                        this.AAsVoid(new SettingWidget(var1.keybindMode()));
                     }
                  }

                  ActionButton var17 = this.MAsQo("Reset module settings", () -> {
                     for (Setting var3x : var1.getSettings()) {
                        var3x.reset();
                     }

                     if (var1 instanceof FakeSpawner var4x) {
                        var4x.YAsVoid();
                     }

                     if (var1 instanceof FakePay var5x) {
                        this.A = var5x.TAsString();
                     }

                     this.y = "";
                     this.build();
                  });
                  var17.u = true;
                  return;
               }

               final ControlSettings.Type_h var18f = (ControlSettings.Type_h)var12.next();
               var18 = var18f;
               if (!var18.collapsed()) {
                  break;
               }

               String var4 = (this.j.contains(var18.title()) ? "Hide " : "Show ") + var18.title().toLowerCase(Locale.ROOT);
               ActionButton var5 = this.MAsQo(var4, () -> {
                  boolean var2x = this.e.get(var18f.title()).Q;
                  if (!this.j.remove(var18f.title())) {
                     this.j.add(var18f.title());
                  }

                  this.build();
                  this.layout();
                  if (var2x) {
                     this.FAsVoid(this.e.get(var18f.title()));
                  }
               });
               this.e.put(var18.title(), var5);
               this.Z.put(var5, () -> var18f.settings().stream().anyMatch(Setting::isVisible));
            } while (!this.j.contains(var18.title()));

            final ControlSettings.Type_h var18g = var18;
            this.AAsVoid(new SectionHeaderWidget(var18.title(), () -> var18g.settings().stream().anyMatch(Setting::isVisible), var18.chips()));

            for (Setting var21 : var18.settings()) {
               if (var1 instanceof FakePay var6 && var21 == var6.fakeBalance()) {
                  this.AAsVoid(new SectionHeaderWidget("Fake balance"));
                  TextInputWidget var7 = new TextInputWidget("Amount, e.g. 10T, 1B or 61.4", var1x -> {
                     this.A = var1x;
                     this.y = "";
                  }, 40);
                  var7.W = this.A;
                  Runnable var8 = () -> {
                     String var2x = var6.qAsString(this.A);
                     this.y = var2x.isEmpty() ? "Balance submitted" : var2x;
                  };
                  var7.MAsQq(var8);
                  this.AAsVoid(var7);
                  this.MAsQo("Submit", var8);
                  this.QAsVoid(() -> var6.mAsString() + (this.y.isEmpty() ? "" : "\n" + this.y));
                  continue;
               }

               if (var21 instanceof NumberSetting var22 && var22.rAsBoolean()) {
                  continue;
               }

               if (var18.chips()) {
                  this.AAsVoid(SettingWidget.LAsQf(var21));
               } else {
                  this.AAsVoid(new SettingWidget(var21));
               }

               if (var1 instanceof HitFX var23 && var21 == var23.rAsSetting()) {
                  this.QAsVoid(var23::xAsString);
               }

               if (var1 instanceof KillFX var24 && var21 == var24.gAsSetting()) {
                  this.QAsVoid(var24::FAsString);
               }
            }
         }
      }
   }

   @Override
   protected boolean componentShown(GuiWidget var1) {
      BooleanSupplier var2 = this.Z.get(var1);
      return super.componentShown(var1) && (var2 == null || var2.getAsBoolean());
   }

   @Override
   protected int titleX() {
      return this.H + 12;
   }

   @Override
   protected int titleY() {
      return this.P.bodyTop() + 6;
   }

   @Override
   protected int bodyTop() {
      return this.P.bodyTop() + 44;
   }

   @Override
   protected void renderExtra(DrawContext var1, int var2, int var3) {
      if (this.f != null) {
         this.f.render(var1, var2, var3);
      }

      GuiTextureDrawing.oAsVoid(var1, this.H + 12, this.k - 5, this.r - this.H - 24, false, UiStyle.I);
   }

   @Override
   protected void layout() {
      if (this.P != null) {
         this.H = this.P.columns() ? this.P.inspectorX() : this.P.contentX();
         this.r = this.P.right();
         this.k = this.bodyTop();
         this.S = this.P.bottom();
         if (this.f != null) {
            this.f.bounds(this.H + 8, this.P.bodyTop() + 21, Math.max(1, this.r - this.H - 20));
            this.f.F = 16;
         }

         this.iAsVoid();
      }
   }

   @Override
   void suspend() {
      if (this.p instanceof HitSounds var1) {
         var1.NAsVoid();
      }

      super.suspend();
   }

   @Override
   public void close() {
      if (this.p instanceof HitSounds var1) {
         var1.NAsVoid();
      }

      this.i.lAsVoid();
   }

   public void tick() {
      if (this.p instanceof Notification var1 && !Objects.equals(this.L, var1.style().getValue())) {
         int var3 = this.b != null && this.b.Q ? 1 : 0;
         this.RAsVoid();
         if (var3 != 0) {
            this.l.stream().filter(var0 -> var0 instanceof SettingWidget).findFirst().ifPresent(this::FAsVoid);
         }
      }

      if (this.U != null) {
         this.U.B = !AppearanceAssets.lAsBoolean();
         this.U.X = this.U.B ? "Start larping" : "Starting...";
      }
   }

   private static final class Type_v extends GuiWidget {
      private static final int h = 88;
      private static final int o = 6;
      private static final int e = 14;
      private static final int T = 15;
      private static final int U = -14999514;
      private static final int l = -14275533;
      private static final int t = -13880517;
      private static final int O = -12958899;
      private final CustomCrosshair L;
      private boolean q;
      private boolean N;
      private int D;
      private int Z;

      Type_v(CustomCrosshair var1) {
         this.L = var1;
         this.F = 88;
      }

      @Override
      public void bounds(int var1, int var2, int var3) {
         super.bounds(var1, var2, var3);
         this.F = this.L.YAsBoolean() ? 12 + 15 * this.qAsInt() + 14 : 88;
         if (!this.L.YAsBoolean()) {
            this.release();
         }
      }

      private int aAsInt() {
         return Math.clamp((long)(this.V / 3), 56, 96);
      }

      private int dAsInt() {
         return this.V - this.aAsInt() - 4;
      }

      private int qAsInt() {
         return Math.clamp((long)((this.dAsInt() - 12) / 15), 3, 10);
      }

      private int MAsInt() {
         return this.k + (this.dAsInt() - 15 * this.qAsInt()) / 2;
      }

      private int KAsInt() {
         return this.x + 6;
      }

      private int KAsInt(double var1) {
         return (int)Math.floor((var1 - (double)this.MAsInt()) / (double)this.qAsInt());
      }

      private int HAsInt(double var1) {
         return (int)Math.floor((var1 - (double)this.KAsInt()) / (double)this.qAsInt());
      }

      private static boolean UAsBoolean(int var0, int var1) {
         return var0 >= 0 && var1 >= 0 && var0 < 15 && var1 < 15;
      }

      @Override
      public void render(DrawContext var1, int var2, int var3) {
         if (!this.L.YAsBoolean()) {
            int var18 = this.V / 2;
            this.fAsVoid(var1, this.k, this.x, var18 - 4, this.F, true);
            this.fAsVoid(var1, this.k + var18, this.x, var18 - 4, this.F, false);
         } else {
            int var4 = this.dAsInt();
            int var5 = this.qAsInt();
            int var6 = this.MAsInt();
            int var7 = this.KAsInt();
            int var8 = var5 >= 5 ? 1 : 0;
            GuiDrawing.CAsVoid(var1, this.k, this.x, var4, this.F, 5, -14999514);
            CrosshairDrawingCodec var9 = this.L.mAsXp();
            int var10 = this.L.yAsInt();

            for (int var12 = 0; var12 < 15; var12++) {
               for (int var13 = 0; var13 < 15; var13++) {
                  int var14 = var6 + var13 * var5;
                  int var15 = var7 + var12 * var5;
                  if (var9.jAsBoolean(var13, var12)) {
                     GuiTextureDrawing.CAsVoid(var1, var14, var15, var5, var5, var10);
                  } else {
                     GuiTextureDrawing.CAsVoid(
                        var1, var14, var15, var5 - var8, var5 - var8, var13 == 7 && var12 == 7 ? -12958899 : (var13 != 7 && var12 != 7 ? -14275533 : -13880517)
                     );
                  }
               }
            }

            int var19 = this.KAsInt((double)var2);
            int var20 = this.HAsInt((double)var3);
            if (var2 >= 0 && UAsBoolean(var19, var20)) {
               GuiTextureDrawing.pAsVoid(var1, var6 + var19 * var5 - 1, var7 + var20 * var5 - 1, var5 + 1, var5 + 1, -1056964609);
            }

            String var21 = TextDrawing.zAsString("Left click draws, right click erases", Math.max(1, var4 - 12), TextDrawing.Type_s.HELP);
            TextDrawing.NAsVoid(var1, var21, this.k + 6, var7 + 15 * var5 + 4, -6643287, TextDrawing.Type_s.HELP);
            int var22 = this.k + var4 + 4;
            int var16 = this.aAsInt() - 4;
            int var17 = (this.F - 4) / 2;
            this.fAsVoid(var1, var22, this.x, var16, var17, true);
            this.fAsVoid(var1, var22, this.x + var17 + 4, var16, this.F - var17 - 4, false);
         }
      }

      private void fAsVoid(DrawContext var1, int var2, int var3, int var4, int var5, boolean var6) {
         double var7 = Math.min((double)(var4 - 8) / 2.0, (double)var5 / 2.0 - 15.0);
         double var9 = Math.min(1.0, Math.max(1.0, var7) / (double)CustomCrosshair.JAsInt());
         GuiDrawing.CAsVoid(var1, var2, var3, var4, var5, 5, var6 ? -14999514 : -2762790);
         TextDrawing.UAsVoid(var1, var6 ? "Dark" : "Light", var2 + 7, var3 + 6, var6 ? -4472376 : -12433328);
         var1.getMatrices().pushMatrix();

         try {
            var1.getMatrices().translate((float)var2 + (float)var4 / 2.0F, (float)var3 + (float)var5 / 2.0F + 7.0F);
            var1.getMatrices().scale((float)var9, (float)var9);
            CustomCrosshair.yAsVoid(var1, 0, 0);
         } finally {
            var1.getMatrices().popMatrix();
         }
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (this.L.YAsBoolean() && var5 <= 1) {
            int var6 = this.KAsInt(var1);
            int var7 = this.HAsInt(var3);
            if (!UAsBoolean(var6, var7)) {
               return false;
            } else {
               this.N = var5 == 0 && !this.L.mAsXp().jAsBoolean(var6, var7);
               this.q = true;
               this.D = var6;
               this.Z = var7;
               this.L.bAsVoid(var6, var7, this.N);
               return true;
            }
         } else {
            return false;
         }
      }

      @Override
      public void drag(double var1, double var3) {
         if (this.q && this.L.YAsBoolean()) {
            int var5 = this.KAsInt(var1);
            int var6 = this.HAsInt(var3);
            int var7 = var5 - this.D;
            int var8 = var6 - this.Z;
            int var9 = Math.max(Math.abs(var7), Math.abs(var8));

            for (int var10 = 1; var10 <= var9; var10++) {
               int var11 = this.D + (int)Math.round((double)var7 * (double)var10 / (double)var9);
               int var12 = this.Z + (int)Math.round((double)var8 * (double)var10 / (double)var9);
               if (UAsBoolean(var11, var12)) {
                  this.L.bAsVoid(var11, var12, this.N);
               }
            }

            this.D = var5;
            this.Z = var6;
         }
      }

      @Override
      public void release() {
         if (this.q) {
            this.q = false;
            ClientLog.GAsVoid("Crosshair", "Drawing edited: {} cells filled", this.L.mAsXp().jAsInt());
         }
      }
   }
}
