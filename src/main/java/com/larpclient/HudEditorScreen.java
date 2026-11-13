package com.larpclient;

import com.larpclient.module.BooleanSetting;
import com.larpclient.module.ModeSetting;
import com.larpclient.module.ModuleManager;
import com.larpclient.module.NumberSetting;
import com.larpclient.module.Setting;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;

public final class HudEditorScreen extends GuiPanel {
   private static final List<String> j = List.of(
      "Free", "Top left", "Top center", "Top right", "Center left", "Center", "Center right", "Bottom left", "Bottom center", "Bottom right"
   );
   private final Screen D;
   private WorkspaceSaveStatus O;
   private WorkspaceSaveStatus.Type_f s;
   private WorkspaceSaveStatus.Type_d N = WorkspaceSaveStatus.Type_d.NONE;
   private final List<HudEditorScreen.Type_o> Y = new ArrayList<>();
   private final List<HudEditorScreen.Type_o> C = new ArrayList<>();
   private final List<GuiWidget> y = new ArrayList<>();
   private final Set<GuiWidget> a = Collections.newSetFromMap(new IdentityHashMap<>());
   private final w2 K = new w2(1.0);
   private final Map<Setting<?>, Object> l = new IdentityHashMap<>();
   private TextInputWidget z;
   private HudEditorScreen.Type_o o;
   private HudEditorScreen.Type_o T;
   private HudEditorScreen.Type_o e;
   private HudEditorScreen.Type_o F;
   private NumberSetting x2;
   private NumberSetting y2;
   private NumberSetting size;
   private ModeSetting align;
   private BooleanSetting showWidget;
   private BooleanSetting lockPosition;
   private boolean P = true;
   private boolean X;
   private boolean h;
   private boolean L;
   private boolean w = true;
   private boolean u;
   private boolean R;
   private boolean q = true;
   private double n;
   private double m;
   private int c;
   private int W;
   private int M = -1;
   private int I = -1;
   private static boolean r;
   private String p = "";
   private Object g;

   public HudEditorScreen(Screen var1) {
      super("HUD Editor");
      this.D = var1;
   }

   public boolean pAsBoolean() {
      return this.h;
   }

   private ProfileManager NAsBo() {
      return LarpClient.CAsLarpClient().fAsBo();
   }

   private boolean yAsBoolean(HudRenderer.Type_m var1) {
      return this.NAsBo().yAsN7().VAsBoolean(var1.a);
   }

   protected void init() {
      if (this.O == null) {
         ModuleManager var1 = LarpClient.CAsLarpClient().mAsF();
         List var2 = Stream.of(var1.TAsModule("HUD"), var1.TAsModule("TargetHUD"), var1.TAsModule("Staff List"), var1.TAsModule("CustomCrosshair"))
            .filter(Objects::nonNull)
            .toList();
         this.O = new WorkspaceSaveStatus(() -> this.NAsBo().kAsBoolean(this.NAsBo().ZAsString()), this::yAsBoolean, var2);
         this.g = this.client.world;
         this.u = r;
         ClientLog.GAsVoid("HUD editor", "Opened canvas with floating dock, {}x{}; profile {}", this.width, this.height, this.NAsBo().ZAsString());
      } else {
         this.O.kAsVoid();
      }

      HudRenderer.CAsVoid();
      this.MAsVoid();
   }

   private HudEditorScreen.Type_o jAsPs_Type_o(String var1, String var2, Runnable var3, BooleanSupplier var4) {
      HudEditorScreen.Type_o var5 = new HudEditorScreen.Type_o(var1, var2, var3, var4);
      this.Q.add(var5);
      return var5;
   }

   private void xAsVoid(Setting<?> var1, boolean var2) {
      SettingWidget var3 = new SettingWidget(var1, true);
      this.y.add(var3);
      this.Q.add(var3);
      if (var2) {
         this.a.add(var3);
      }
   }

   private void wAsVoid(String var1, Runnable var2, boolean var3, boolean var4) {
      ActionButton var5 = new ActionButton(var1, var2);
      var5.e = true;
      var5.A = var4;
      var5.F = 28;
      this.y.add(var5);
      this.Q.add(var5);
      if (var3) {
         this.a.add(var5);
      }
   }

   private void fAsVoid(String var1, String var2, boolean var3, Consumer<Boolean> var4) {
      BooleanSetting var5 = new BooleanSetting(var1, var2, var3);
      var5.onChanged(var4);
      this.xAsVoid(var5, false);
   }

   private void MAsVoid() {
      this.yAsVoid();
      this.Y.clear();
      this.C.clear();
      this.y.clear();
      this.a.clear();
      this.z = null;
      this.x2 = this.y2 = this.size = null;
      this.align = null;
      this.showWidget = this.lockPosition = null;
      this.Y
         .add(
            this.jAsPs_Type_o(
               "Widgets", "panels-top-left", () -> this.gAsVoid(WorkspaceSaveStatus.Type_d.WIDGETS), () -> this.N == WorkspaceSaveStatus.Type_d.WIDGETS
            )
         );
      this.o = this.jAsPs_Type_o("Undo (Ctrl+Z)", "rotate-ccw", () -> {
         this.sAsVoid();
         this.O.xAsBoolean();
      }, () -> false);
      this.o.f = true;
      this.Y.add(this.o);
      this.T = this.jAsPs_Type_o("Redo (Ctrl+Shift+Z)", "rotate-ccw", () -> {
         this.sAsVoid();
         this.O.sAsBoolean();
      }, () -> false);
      this.T.f = this.T.P = true;
      this.Y.add(this.T);
      HudEditorScreen.Type_o var1 = this.jAsPs_Type_o(
         "Editor options", "settings", () -> this.gAsVoid(WorkspaceSaveStatus.Type_d.OPTIONS), () -> this.N == WorkspaceSaveStatus.Type_d.OPTIONS
      );
      var1.f = true;
      this.Y.add(var1);
      this.e = this.jAsPs_Type_o("Edit", "paintbrush", () -> {
         if (this.O.aAsLp_Type_m() != null) {
            if (this.N != WorkspaceSaveStatus.Type_d.STYLE && this.N != WorkspaceSaveStatus.Type_d.POSITION) {
               this.cAsVoid(this.O.aAsLp_Type_m());
            } else {
               this.XAsVoid(WorkspaceSaveStatus.Type_d.NONE);
            }
         }
      }, () -> this.N == WorkspaceSaveStatus.Type_d.STYLE || this.N == WorkspaceSaveStatus.Type_d.POSITION);
      this.Y.add(this.e);
      HudEditorScreen.Type_o var2 = this.jAsPs_Type_o("Done", "", this::close, () -> false);
      var2.X = true;
      this.Y.add(var2);
      this.F = this.jAsPs_Type_o("Save failed. Retry", "rotate-ccw", () -> {
         this.O.EAsBoolean();
         this.PAsVoid();
      }, () -> false);
      if (this.N != WorkspaceSaveStatus.Type_d.NONE) {
         HudEditorScreen.Type_o var3 = this.jAsPs_Type_o("Close panel", "x", () -> this.XAsVoid(WorkspaceSaveStatus.Type_d.NONE), () -> false);
         var3.f = true;
         this.C.add(var3);
         if (this.N == WorkspaceSaveStatus.Type_d.WIDGETS) {
            this.z = new TextInputWidget("Search widgets", var1x -> {
               this.p = var1x;
               this.c = 0;
               this.qAsVoid();
            }, 80);
            this.z.W = this.p;
            this.Q.add(this.z);

            for (HudRenderer.Type_m var5 : HudRenderer.S) {
               HudEditorScreen.Type_p var6 = new HudEditorScreen.Type_p(var5);
               this.y.add(var6);
               this.Q.add(var6);
            }
         } else if (this.N == WorkspaceSaveStatus.Type_d.OPTIONS) {
            this.fAsVoid(
               "Snap to guides", "Align with screen edges and center", GuiWorkspace.c.snapping(), var1x -> this.O.JAsVoid(var1x, GuiWorkspace.c.grid())
            );
            this.fAsVoid("Show grid", "Show an eight-pixel placement grid", GuiWorkspace.c.grid(), var1x -> this.O.JAsVoid(GuiWorkspace.c.snapping(), var1x));
            BooleanSetting var7 = new BooleanSetting("Preview hidden widgets", "Preview without enabling widgets", this.X);
            var7.visibleWhen(() -> !this.h);
            var7.onChanged(var1x -> this.X = var1x);
            SettingWidget var9 = new SettingWidget(var7);
            this.y.add(var9);
            this.Q.add(var9);
            this.fAsVoid("Use live content", "Show current gameplay content instead of samples", this.h, var1x -> {
               this.O.kAsVoid();
               this.h = var1x;
               HudRenderer.CAsVoid();
            });
            this.wAsVoid("Reset layout", () -> {
               this.sAsVoid();
               this.O.SAsVoid();
            }, false, true);
         } else if (this.O.aAsLp_Type_m() != null) {
            this.C
               .add(this.jAsPs_Type_o("Appearance", "", () -> this.XAsVoid(WorkspaceSaveStatus.Type_d.STYLE), () -> this.N == WorkspaceSaveStatus.Type_d.STYLE));
            this.C
               .add(
                  this.jAsPs_Type_o(
                     "Position", "", () -> this.XAsVoid(WorkspaceSaveStatus.Type_d.POSITION), () -> this.N == WorkspaceSaveStatus.Type_d.POSITION
                  )
               );
            HudRenderer.Type_m var8 = this.O.aAsLp_Type_m();
            if (this.N == WorkspaceSaveStatus.Type_d.STYLE) {
               this.showWidget = new BooleanSetting("Show widget", "Keep this widget visible in gameplay", HudRenderer.HAsBoolean(var8));
               this.showWidget.onChanged(var2x -> {
                  if (!this.L) {
                     HudRenderer.IAsVoid(var8, var2x);
                     this.O.VAsVoid();
                  }
               });
               this.xAsVoid(this.showWidget, false);
               this.HAsVoid(var8);

               for (Setting var11 : HudRenderer.HAsList(var8)) {
                  this.xAsVoid(var11, false);
               }
            } else {
               this.lockPosition = new BooleanSetting("Lock position", "Prevent moving and resizing this widget", this.yAsBoolean(var8));
               this.lockPosition.onChanged(var2x -> {
                  if (!this.L) {
                     this.MAsVoid(var8, var2x);
                  }
               });
               this.xAsVoid(this.lockPosition, false);
               this.x2 = new NumberSetting("X", "Horizontal position in GUI pixels", (double)var8.PAsInt(this.width), 0.0, (double)this.width, 1.0);
               this.y2 = new NumberSetting("Y", "Vertical position in GUI pixels", (double)var8.ZAsInt(this.height), 0.0, (double)this.height, 1.0);
               this.x2.onChanged(var2x -> {
                  if (!this.L && !this.yAsBoolean(var8)) {
                     var8.SAsVoid(var2x.intValue(), var8.ZAsInt(this.height), this.width, this.height);
                  }
               });
               this.y2.onChanged(var2x -> {
                  if (!this.L && !this.yAsBoolean(var8)) {
                     var8.SAsVoid(var8.PAsInt(this.width), var2x.intValue(), this.width, this.height);
                  }
               });
               this.xAsVoid(this.x2, true);
               this.xAsVoid(this.y2, true);
               this.HAsVoid(var8);
               this.align = new ModeSetting("Align", "Align within the full game viewport", yAsString(var8), j.toArray(String[]::new));
               this.align.onChanged(var2x -> {
                  if (!this.L && !this.yAsBoolean(var8) && !var2x.equals("Free")) {
                     int var3x = j.indexOf(var2x) - 1;
                     GuiWorkspace.B.put(var8.a, new GuiWorkspace.Type_g((double)(var3x % 3) * 0.5, (double)(var3x / 3) * 0.5));
                  }
               });
               this.xAsVoid(this.align, true);
               this.wAsVoid("Reset placement", () -> {
                  this.sAsVoid();
                  if (!this.yAsBoolean(var8)) {
                     this.O.WAsVoid(var8::AAsVoid);
                  }
               }, true, false);
            }
         }
      }

      this.qAsVoid();
   }

   private void HAsVoid(HudRenderer.Type_m var1) {
      this.size = new NumberSetting("Size", "Uniform widget scale", var1.BAsDouble(), 0.5, 2.5, 0.01);
      this.size.onChanged(var2 -> {
         if (!this.L && !this.yAsBoolean(var1)) {
            var1.iAsVoid(var2);
         }
      });
      this.xAsVoid(this.size, true);
   }

   private void gAsVoid(WorkspaceSaveStatus.Type_d var1) {
      this.XAsVoid(this.N == var1 ? WorkspaceSaveStatus.Type_d.NONE : var1);
   }

   private void XAsVoid(WorkspaceSaveStatus.Type_d var1) {
      this.sAsVoid();
      this.O.kAsVoid();
      this.R = false;
      this.P = true;
      if (this.N == WorkspaceSaveStatus.Type_d.NONE || var1 == WorkspaceSaveStatus.Type_d.WIDGETS || var1 == WorkspaceSaveStatus.Type_d.OPTIONS) {
         this.M = this.I = -1;
      }

      this.N = var1;
      this.c = 0;
      this.K.aAsVoid(this.q ? 0.0 : 1.0);
      this.K.bAsDouble(1.0, 160);
      this.MAsVoid();
   }

   private void cAsVoid(HudRenderer.Type_m var1) {
      this.sAsVoid();
      this.O.eAsVoid(var1);
      this.UAsVoid(var1);
      d7.Type_p var2 = HudRenderer.qAsOptional(HudRenderer.NAsList(), var1).orElse(var1.mAsD7_Type_p(this.width, this.height));
      this.M = var2.x() + var2.width() + 14;
      this.I = var2.y();
      if (this.M + 260 > this.width - 12) {
         this.M = Math.max(12, var2.x() - 274);
      }

      this.N = WorkspaceSaveStatus.Type_d.STYLE;
      this.P = true;
      this.c = 0;
      this.K.aAsVoid(this.q ? 0.0 : 1.0);
      this.K.bAsDouble(1.0, 160);
      this.MAsVoid();
   }

   private void TAsVoid(HudRenderer.Type_m var1) {
      this.sAsVoid();
      this.O.eAsVoid(var1);
      this.UAsVoid(var1);
      this.N = WorkspaceSaveStatus.Type_d.NONE;
      this.c = 0;
      this.MAsVoid();
   }

   private void UAsVoid(HudRenderer.Type_m var1) {
      if (this.h && HudRenderer.qAsOptional(HudRenderer.NAsList(), var1).isEmpty()) {
         this.h = false;
      }
   }

   private void MAsVoid(HudRenderer.Type_m var1, boolean var2) {
      this.O.kAsVoid();
      this.NAsBo().yAsN7().dAsVoid(var1.a, var2);
      this.PAsVoid();
   }

   private void PAsVoid() {
      this.u = r = !this.NAsBo().jAsBoolean();
   }

   private boolean XAsBoolean(HudRenderer.Type_m var1) {
      return this.p.isBlank() || (HudRenderer.XAsString(var1.a) + " " + var1.a).toLowerCase(Locale.ROOT).contains(this.p.toLowerCase(Locale.ROOT));
   }

   private boolean rAsBoolean(GuiWidget var1) {
      if (var1 instanceof HudEditorScreen.Type_p var2 && !this.XAsBoolean(var2.T)) {
         return false;
      }

      return true;
   }

   private boolean JAsBoolean(GuiWidget var1) {
      if (var1 instanceof SettingWidget var2 && !var2.vAsBoolean()) {
         return false;
      }

      return true;
   }

   private void qAsVoid() {
      this.q = this.NAsBo().yAsN7().Z;
      this.W = this.y.stream().filter(this::rAsBoolean).filter(this::JAsBoolean).mapToInt(var0 -> var0.F + 4).sum();
      this.s = WorkspaceSaveStatus.Type_f.calculate(this.width, this.height, this.N, this.W, this.M, this.I, this.P, this.O.jAsBoolean() || this.u);

      for (int var1 = 0; var1 < this.Y.size(); var1++) {
         HudEditorScreen.Type_o var2 = this.Y.get(var1);
         var2.Y = this.s.dock() != null;
         if (var2.Y) {
            lAsVoid(var2, this.s.tools().get(var1));
         }
      }

      this.o.B = this.O.CAsBoolean();
      this.T.B = this.O.yAsBoolean();
      this.e.B = this.O.aAsLp_Type_m() != null;
      this.F.Y = this.s.retry() != null;
      if (this.F.Y) {
         lAsVoid(this.F, this.s.retry());
      }

      d7.Type_p var9 = this.s.panel();
      d7.Type_p var10 = this.s.body();

      for (HudEditorScreen.Type_o var4 : this.C) {
         var4.Y = var9 != null;
      }

      if (var9 != null && !this.C.isEmpty()) {
         this.C.getFirst().bounds(var9.x() + var9.width() - 36, var9.y() + 9, 26);
         this.C.getFirst().F = 26;
         if (this.C.size() == 3) {
            int var11 = (var9.width() - 24) / 2;

            for (int var13 = 1; var13 < 3; var13++) {
               this.C.get(var13).bounds(var9.x() + 10 + (var13 - 1) * (var11 + 4), var9.y() + 43, var11);
               this.C.get(var13).F = 26;
            }
         }
      }

      if (this.z != null) {
         this.z.Y = var9 != null;
         if (var9 != null) {
            this.z.bounds(var9.x() + 10, var9.y() + 43, var9.width() - 20);
            this.z.F = 25;
         }
      }

      int var12 = var10 == null ? 0 : var10.height();
      this.c = Math.clamp((long)this.c, 0, Math.max(0, this.W - var12));
      int var14 = var10 == null ? 0 : var10.y() - this.c;

      for (GuiWidget var6 : this.y) {
         int var7 = this.rAsBoolean(var6) && this.JAsBoolean(var6) ? 1 : 0;
         var6.Y = var10 != null && var7 != 0 && var14 + var6.F > var10.y() && var14 < var10.y() + var10.height();
         var6.B = !this.a.contains(var6) || this.O.aAsLp_Type_m() == null || !this.yAsBoolean(this.O.aAsLp_Type_m());
         if (var10 != null && var7 != 0) {
            var6.bounds(var10.x(), var14, var10.width());
            if (var6 instanceof SettingWidget var8) {
               var8.fAsVoid(var10.y(), var10.y() + var10.height());
            }

            var14 += var6.F + 4;
         }
      }

      if (this.b != null && (!this.b.Y || !this.b.B)) {
         this.b.hAsVoid();
         this.b = this.t = null;
         this.QAsVoid();
      }
   }

   private static void lAsVoid(GuiWidget var0, d7.Type_p var1) {
      var0.bounds(var1.x(), var1.y(), var1.width());
      var0.F = var1.height();
   }

   @Override
   protected boolean accepts(GuiWidget var1, double var2, double var4) {
      return this.y.contains(var1)
         ? var1.contains(var2, var4) && this.s.controlContains(new d7.Type_p(var1.k, var1.x, var1.V, var1.F), var2, var4)
         : var1.contains(var2, var4);
   }

   @Override
   protected void focusChanged(GuiWidget var1) {
      if (this.y.contains(var1) && this.s.body() != null) {
         d7.Type_p var2 = this.s.body();
         if (var1.x < var2.y()) {
            this.c = this.c - (var2.y() - var1.x);
         }

         if (var1.x + var1.F > var2.y() + var2.height()) {
            this.c = this.c + (var1.x + var1.F - var2.y() - var2.height());
         }

         this.qAsVoid();
      }
   }

   private static String yAsString(HudRenderer.Type_m var0) {
      GuiWorkspace.Type_g var1 = GuiWorkspace.B.get(var0.a);
      if (var1 == null) {
         return var0.a.equals("crosshair") ? "Center" : "Free";
      } else {
         int var2 = (int)Math.round(var1.x() * 2.0);
         int var3 = (int)Math.round(var1.y() * 2.0);
         return var2 >= 0
               && var2 <= 2
               && var3 >= 0
               && var3 <= 2
               && Math.abs(var1.x() - (double)var2 * 0.5) < 0.001
               && Math.abs(var1.y() - (double)var3 * 0.5) < 0.001
            ? j.get(var3 * 3 + var2 + 1)
            : "Free";
      }
   }

   private void uAsVoid() {
      HudRenderer.Type_m var1 = this.O.aAsLp_Type_m();
      if (var1 != null) {
         this.L = true;

         try {
            if (this.x2 != null) {
               this.x2.setClampedValue((double)var1.PAsInt(this.width));
            }

            if (this.y2 != null) {
               this.y2.setClampedValue((double)var1.ZAsInt(this.height));
            }

            if (this.size != null) {
               this.size.setClampedValue(var1.BAsDouble());
            }

            if (this.align != null) {
               this.align.setValue(yAsString(var1));
            }

            if (this.showWidget != null) {
               this.showWidget.setValue(Boolean.valueOf(HudRenderer.HAsBoolean(var1)));
            }

            if (this.lockPosition != null) {
               this.lockPosition.setValue(Boolean.valueOf(this.yAsBoolean(var1)));
            }
         } finally {
            this.L = false;
         }
      }
   }

   private void GAsVoid() {
      this.O.rAsVoid();
      if (this.l.isEmpty() && this.O.aAsLp_Type_m() != null) {
         for (Setting var2 : HudRenderer.HAsList(this.O.aAsLp_Type_m())) {
            this.l.put(var2, var2.getValue());
         }
      }
   }

   private void QAsVoid() {
      if (this.l.entrySet().stream().anyMatch(var0 -> !Objects.equals(var0.getValue(), var0.getKey().getValue()))) {
         this.O.VAsVoid();
      }

      this.l.clear();
      this.O.bAsBoolean();
   }

   private void JAsVoid() {
      this.b = this.t = null;
      this.Q.clear();
      this.l.clear();
      HudRenderer.CAsVoid();
      this.N = WorkspaceSaveStatus.Type_d.NONE;
      this.R = false;
      this.c = 0;
   }

   private void sAsVoid() {
      if (this.O.KAsBoolean()) {
         this.JAsVoid();
      } else {
         if (this.b != null) {
            this.b.hAsVoid();
         }

         this.b = this.t = null;
         this.QAsVoid();
      }
   }

   private boolean SAsBoolean() {
      if (this.b instanceof TextInputWidget) {
         return true;
      } else {
         if (this.b instanceof SettingWidget var1 && var1.xAsBoolean()) {
            return true;
         }

         return false;
      }
   }

   @Override
   public boolean mouseClicked(Click click, boolean doubled) {
      if (this.client.player != null && this.client.world != null) {
         if (this.O.KAsBoolean()) {
            this.JAsVoid();
            this.MAsVoid();
         }

         if (this.O.gAsOz().eAsBoolean()) {
            return true;
         } else {
            this.qAsVoid();

            for (GuiWidget var4 : this.Q) {
               if (var4 instanceof SettingWidget) {
                  SettingWidget var5 = (SettingWidget)var4;
                  if (var5.yAsBoolean()) {
                     if (var5.uAsBoolean(click.x(), click.y())) {
                        this.GAsVoid();
                        var5.oAsBoolean(click.x(), click.y(), click.button());
                        this.t = var5;
                        return true;
                     }

                     var5.BAsVoid();
                  }
               }
            }

            GuiWidget var6 = this.Q.stream().filter(var2x -> this.accepts(var2x, click.x(), click.y())).findFirst().orElse(null);
            if (var6 != this.b) {
               this.sAsVoid();
            }

            if (var6 != null) {
               this.GAsVoid();
               this.b = var6;
               var6.y = true;
               var6.Q = false;
               if (var6.click(click.x(), click.y(), click.button())) {
                  if (this.Q.contains(var6)) {
                     this.t = var6;
                  } else {
                     this.b = this.t = null;
                  }
               }

               if (!(this.t instanceof SettingWidget) && !this.SAsBoolean()) {
                  this.QAsVoid();
               }

               return true;
            } else if (this.s.panelContains(click.x(), click.y())) {
               if (click.button() == 0 && click.y() < (double)(this.s.panel().y() + 39)) {
                  this.R = true;
                  this.n = click.x() - (double)this.s.panel().x();
                  this.m = click.y() - (double)this.s.panel().y();
               }

               return true;
            } else if (this.s.dock() != null && this.s.dock().contains(click.x(), click.y(), 0)) {
               return true;
            } else {
               if (this.N != WorkspaceSaveStatus.Type_d.NONE) {
                  this.XAsVoid(WorkspaceSaveStatus.Type_d.NONE);
               }

               if (this.s.canvasContains(click.x(), click.y())) {
                  HudRenderer.Type_p var7 = d7.lAsObject(HudRenderer.NAsList(), var0 -> true, HudRenderer.Type_p::bounds, click.x(), click.y());
                  if (click.button() == 1) {
                     if (var7 == null) {
                        this.XAsVoid(WorkspaceSaveStatus.Type_d.WIDGETS);
                     } else {
                        this.cAsVoid(var7.element());
                     }
                  } else if (click.button() == 0) {
                     this.GAsVoid();
                     this.O.gAsOz().OAsBoolean(click.x(), click.y(), 0, this.width, this.height, HudRenderer.NAsList(), false);
                  }
               }

               if (!this.O.gAsOz().eAsBoolean()) {
                  this.QAsVoid();
               }

               return true;
            }
         }
      } else {
         return true;
      }
   }

   @Override
   public boolean mouseDragged(Click click, double offsetX, double offsetY) {
      if (this.O.KAsBoolean()) {
         this.JAsVoid();
         this.MAsVoid();
         return true;
      } else if (!this.client.isWindowFocused()) {
         this.IAsVoid();
         return true;
      } else if (this.R && this.s.panel() != null) {
         this.M = Math.max(0, (int)Math.round(click.x() - this.n));
         this.I = Math.max(0, (int)Math.round(click.y() - this.m));
         this.qAsVoid();
         return true;
      } else {
         return this.O.gAsOz().eAsBoolean()
            ? this.O
               .gAsOz()
               .UAsBoolean(click.x(), click.y(), click.button(), this.width, this.height, this.client.isAltPressed(), this.client.isShiftPressed())
            : super.mouseDragged(click, offsetX, offsetY);
      }
   }

   @Override
   public boolean mouseReleased(Click click) {
      if (this.O.KAsBoolean()) {
         this.JAsVoid();
         this.MAsVoid();
         return true;
      } else {
         this.R = false;
         this.O.gAsOz().NAsBoolean(click.button());
         super.mouseReleased(click);
         if (!this.SAsBoolean()) {
            this.QAsVoid();
         }

         return true;
      }
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      if (this.O.KAsBoolean()) {
         this.JAsVoid();
         this.MAsVoid();
         return true;
      } else {
         for (GuiWidget var10 : this.Q) {
            if (var10 instanceof SettingWidget var11 && var11.uAsBoolean(mouseX, mouseY)) {
               var11.OAsVoid(verticalAmount);
               return true;
            }
         }

         if (this.s.panelContains(mouseX, mouseY)) {
            this.sAsVoid();
            this.c = this.c - (int)Math.round(verticalAmount * 28.0);
            this.qAsVoid();
         }

         return true;
      }
   }

   @Override
   public boolean charTyped(CharInput input) {
      if (this.O.KAsBoolean()) {
         this.JAsVoid();
         this.MAsVoid();
         return true;
      } else {
         return super.charTyped(input);
      }
   }

   @Override
   public boolean keyPressed(KeyInput input) {
      if (this.O.KAsBoolean()) {
         this.JAsVoid();
         this.MAsVoid();
      }

      int var2 = input.key();
      int var3 = (input.modifiers() & 2) != 0 ? 1 : 0;
      int var4 = (input.modifiers() & 1) != 0 ? 1 : 0;
      if (this.SAsBoolean()) {
         if (var2 == 258) {
            this.sAsVoid();
            return super.keyPressed(input);
         } else {
            this.GAsVoid();
            boolean var9 = super.keyPressed(input);
            if (!this.SAsBoolean()) {
               this.QAsVoid();
            }

            return var9;
         }
      } else if (var2 == 256) {
         for (GuiWidget var11 : this.y) {
            if (var11 instanceof SettingWidget var7 && var7.yAsBoolean()) {
               var7.BAsVoid();
               return true;
            }
         }

         if (this.N != WorkspaceSaveStatus.Type_d.NONE) {
            this.XAsVoid(WorkspaceSaveStatus.Type_d.NONE);
         } else {
            this.close();
         }

         return true;
      } else if (var2 == 290) {
         this.sAsVoid();
         this.O.kAsVoid();
         this.N = WorkspaceSaveStatus.Type_d.NONE;
         this.R = false;
         this.P = !this.P;
         this.MAsVoid();
         return true;
      } else if (var3 == 0 || var2 != 90 && var2 != 89) {
         if (var2 == 87 && var3 == 0) {
            this.gAsVoid(WorkspaceSaveStatus.Type_d.WIDGETS);
            return true;
         } else {
            HudRenderer.Type_m var5 = this.O.aAsLp_Type_m();
            if (var2 == 69 && var3 == 0 && var5 != null) {
               this.cAsVoid(var5);
               return true;
            } else if (this.b == null && var5 != null && !this.yAsBoolean(var5) && var2 >= 262 && var2 <= 265) {
               int var10 = var4 != 0 ? 10 : 1;
               this.O.dAsVoid(var2 == 262 ? var10 : (var2 == 263 ? -var10 : 0), var2 == 264 ? var10 : (var2 == 265 ? -var10 : 0), this.width, this.height);
               return true;
            } else if (this.b == null && (var2 == 261 || var2 == 259) && var5 != null) {
               this.sAsVoid();
               this.O.WAsVoid(() -> var5.xAsVoid(true));
               return true;
            } else {
               this.GAsVoid();
               boolean var6 = super.keyPressed(input);
               if (!this.SAsBoolean()) {
                  this.QAsVoid();
               }

               return var6;
            }
         }
      } else {
         this.sAsVoid();
         if (var2 != 89 && var4 == 0) {
            this.O.xAsBoolean();
         } else {
            this.O.sAsBoolean();
         }

         return true;
      }
   }

   private void IAsVoid() {
      this.R = false;
      this.sAsVoid();
      this.O.kAsVoid();
   }

   public void tick() {
      if (this.O.KAsBoolean()) {
         this.JAsVoid();
         this.MAsVoid();
      }

      if (this.client.world == this.g && this.client.player != null) {
         boolean var1 = this.client.isWindowFocused();
         if (!var1 && this.w) {
            this.IAsVoid();
         }

         this.w = var1;
      } else {
         this.close();
      }
   }

   public void resize(int width, int height) {
      if (this.O != null) {
         this.IAsVoid();
      }

      super.resize(width, height);
   }

   public void close() {
      if (this.O != null) {
         this.sAsVoid();
         this.O.LAsVoid();
      }

      if (this.u) {
         this.PAsVoid();
      }

      if (this.O != null && this.O.jAsBoolean() || this.u) {
         NotificationHud.GAsW8().oAsVoid("HUD save failed", "Edits remain in memory. Reopen the editor to retry.", NotificationHud.Type_u.WARNING, 6000L);
      }

      this.client.setScreen(this.client.world == this.g ? this.D : null);
   }

   public void removed() {
      if (this.O != null) {
         this.sAsVoid();
         this.O.LAsVoid();
      }

      ClientLog.XAsVoid("HUD editor", "Released canvas input");
   }

   public void renderBackground(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
   }

   public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
      if (this.O != null && this.client.player != null && this.client.world != null) {
         if (this.O.KAsBoolean()) {
            this.JAsVoid();
            this.MAsVoid();
         }

         this.qAsVoid();
         if (GuiWorkspace.c.grid() && !this.h) {
            for (int var5 = 0; var5 < this.width; var5 += 8) {
               GuiTextureDrawing.CAsVoid(context, var5, 0, 1, this.height, var5 % 40 == 0 ? 318767103 : 117440511);
            }

            for (int var16 = 0; var16 < this.height; var16 += 8) {
               GuiTextureDrawing.CAsVoid(context, 0, var16, this.width, 1, var16 % 40 == 0 ? 318767103 : 117440511);
            }
         }

         HudRenderer.MAsVoid(context, this.O.aAsLp_Type_m(), this.X, this.h);
         this.XAsVoid(context, mouseX, mouseY);
         this.uAsVoid();
         ClientPreferences var17 = this.NAsBo().yAsN7();

         try (bd.Type_e var6 = bd.jAsBd_Type_e(var17.M, var17.G, var17.b, var17.pAsBd_Type_p())) {
            context.createNewRootLayer();
            if (this.s.dock() != null) {
               dAsVoid(context, this.s.dock());

               for (HudEditorScreen.Type_o var8 : this.Y) {
                  var8.render(context, mouseX, mouseY);
               }

               if (this.F.Y) {
                  this.F.render(context, mouseX, mouseY);
               }

               if (this.s.panel() == null && !this.O.gAsOz().eAsBoolean() && !this.F.Y) {
                  String var18 = this.O.aAsLp_Type_m() == null ? "HUD editor" : HudRenderer.XAsString(this.O.aAsLp_Type_m().a);
                  String var21 = this.O.aAsLp_Type_m() == null ? "Drag to move  /  Right-click to edit" : "Right-click to edit  /  Arrows to nudge";
                  int var9 = TextDrawing.jAsInt(var18, TextDrawing.Type_s.MODULE);
                  int var10 = TextDrawing.jAsInt(var21, TextDrawing.Type_s.HELP);
                  int var11 = Math.min(this.width - 24, var9 + var10 + 28);
                  int var12 = (this.width - var11) / 2;
                  int var13 = this.s.dock().y() - 28;
                  GuiDrawing.CAsVoid(context, var12, var13, var11, 21, 8, bd.MAsInt());
                  TextDrawing.NAsVoid(context, var18, var12 + 9, var13 + 6, bd.u, TextDrawing.Type_s.MODULE);
                  if (var9 + var10 + 28 <= var11) {
                     TextDrawing.NAsVoid(context, var21, var12 + var9 + 19, var13 + 7, bd.e, TextDrawing.Type_s.HELP);
                  }
               }
            }

            this.hAsVoid(context, mouseX, mouseY);

            for (GuiWidget var22 : this.y) {
               if (var22 instanceof SettingWidget) {
                  SettingWidget var24 = (SettingWidget)var22;
                  if (var24.yAsBoolean() && var22.Y) {
                     var24.oAsVoid(context, mouseX, mouseY);
                  }
               }
            }

            if (this.Q.stream().noneMatch(var0 -> {
               if (var0 instanceof SettingWidget var1x && var1x.yAsBoolean()) {
                  return true;
               }

               return false;
            })) {
               for (HudEditorScreen.Type_o var23 : this.Y) {
                  if (var23.Y && (var23.contains((double)mouseX, (double)mouseY) || var23.y && var23.Q)) {
                     var23.TAsVoid(context);
                  }
               }
            }
         }
      }
   }

   private static void dAsVoid(DrawContext var0, d7.Type_p var1) {
      GuiDrawing.uAsVoid(var0, var1.x(), var1.y(), var1.width(), var1.height(), 10);
      GuiDrawing.OAsVoid(var0, var1.x(), var1.y(), var1.width(), var1.height(), 10, bd.MAsInt(), 0.75);
   }

   private void hAsVoid(DrawContext var1, int var2, int var3) {
      d7.Type_p var4 = this.s.panel();
      d7.Type_p var5 = this.s.body();
      if (var4 != null) {
         if (!this.q) {
            this.K.aAsVoid(1.0);
         }

         try (qg var6 = qg.nAsQg(this.K.yAsDouble())) {
            dAsVoid(var1, var4);
            String var7 = this.N == WorkspaceSaveStatus.Type_d.WIDGETS
               ? "Widgets"
               : (this.N == WorkspaceSaveStatus.Type_d.OPTIONS ? "Editor options" : HudRenderer.XAsString(this.O.aAsLp_Type_m().a));
            if (this.N == WorkspaceSaveStatus.Type_d.STYLE || this.N == WorkspaceSaveStatus.Type_d.POSITION) {
               GuiIcons.aAsVoid(var1, GuiIcons.PAsIdentifier(GuiIcons.XAsString(this.O.aAsLp_Type_m().a)), var4.x() + 13, var4.y() + 15, 13, bd.e);
            }

            int var8 = var4.x() + (this.N != WorkspaceSaveStatus.Type_d.STYLE && this.N != WorkspaceSaveStatus.Type_d.POSITION ? 14 : 34);
            TextDrawing.NAsVoid(var1, TextDrawing.HAsString(var7, var4.x() + var4.width() - var8 - 42), var8, var4.y() + 16, bd.u, TextDrawing.Type_s.MODULE);

            for (HudEditorScreen.Type_o var10 : this.C) {
               var10.render(var1, var2, var3);
            }

            if (this.z != null) {
               this.z.render(var1, var2, var3);
            }

            var1.enableScissor(var5.x(), var5.y(), var5.x() + var5.width(), var5.y() + var5.height());

            try {
               for (GuiWidget var19 : this.y) {
                  if (var19.Y) {
                     var19.render(var1, var5.contains((double)var2, (double)var3, 0) ? var2 : -100, var3);
                  }
               }

               if (this.N == WorkspaceSaveStatus.Type_d.WIDGETS && this.y.stream().noneMatch(this::rAsBoolean)) {
                  TextDrawing.NAsVoid(var1, "No matching widgets", var5.x() + 6, var5.y() + 10, bd.e, TextDrawing.Type_s.BODY);
               }
            } finally {
               var1.disableScissor();
            }

            GuiDrawing.aAsVoid(var1, var4.x() + var4.width() - 5, var5.y(), var5.height(), this.W, this.c);
         }
      }
   }

   private void XAsVoid(DrawContext var1, int var2, int var3) {
      List<HudRenderer.Type_p> var4 = HudRenderer.NAsList();
      HudRenderer.Type_p var5 = this.s.canvasContains((double)var2, (double)var3)
         ? d7.lAsObject(var4, var0 -> true, HudRenderer.Type_p::bounds, (double)var2, (double)var3)
         : null;
      if (var5 != null && var5.element() != this.O.aAsLp_Type_m()) {
         hAsVoid(var1, var5.bounds(), -2137614688);
      }

      HudRenderer.Type_m var6 = this.O.aAsLp_Type_m();
      HudRenderer.qAsOptional(var4, var6).ifPresent(var3x -> {
         hAsVoid(var1, var3x, bg.XAsInt(bd.w, 0.85));
         if (!this.yAsBoolean(var6)) {
            d7.Type_p var4x = d7.XAsD7_Type_p(var3x, this.width, this.height);
            GuiDrawing.CAsVoid(var1, var4x.x() - 1, var4x.y() - 1, var4x.width() + 2, var4x.height() + 2, 3, -1072096999);
            GuiDrawing.CAsVoid(var1, var4x.x() + 1, var4x.y() + 1, var4x.width() - 2, var4x.height() - 2, 2, bd.w);
         }

         if (this.O.gAsOz().eAsBoolean()) {
            String var8x = var6.PAsInt(this.width) + ", " + var6.ZAsInt(this.height);
            int var5x = TextDrawing.jAsInt(var8x, TextDrawing.Type_s.HELP) + 12;
            int var6x = d7.LAsInt(var3x.x(), var5x, this.width);
            int var7x = Math.max(2, var3x.y() - 20);
            GuiDrawing.CAsVoid(var1, var6x, var7x, var5x, 16, 5, -552003303);
            TextDrawing.NAsVoid(var1, var8x, var6x + 6, var7x + 4, -1381654, TextDrawing.Type_s.HELP);
         }
      });
      if (this.O.gAsOz().eAsBoolean()) {
         int var7 = this.O.gAsOz().JAsInt();
         int var8 = this.O.gAsOz().fAsInt();
         if (var7 >= 0) {
            GuiTextureDrawing.CAsVoid(var1, Math.min(this.width - 1, var7), 0, 1, this.height, bg.XAsInt(bd.w, 0.45));
         }

         if (var8 >= 0) {
            GuiTextureDrawing.CAsVoid(var1, 0, Math.min(this.height - 1, var8), this.width, 1, bg.XAsInt(bd.w, 0.45));
         }
      }
   }

   private static void hAsVoid(DrawContext var0, d7.Type_p var1, int var2) {
      GuiTextureDrawing.pAsVoid(var0, var1.x() - 1, var1.y() - 1, var1.width() + 2, var1.height() + 2, var2);
   }

   private final class Type_o extends GuiWidget {
      final String R;
      final String L;
      final Runnable r;
      final BooleanSupplier l;
      final w2 J = new w2(0.0);
      boolean f;
      boolean P;
      boolean X;

      Type_o(String var2, String var3, Runnable var4, BooleanSupplier var5) {
         this.R = var2;
         this.L = var3;
         this.r = var4;
         this.l = var5;
      }

      @Override
      public void render(DrawContext var1, int var2, int var3) {
         if (this.Y) {
            boolean var4 = this.l.getAsBoolean();
            int var5 = this.contains((double)var2, (double)var3) || this.y && this.Q ? 1 : 0;
            if (!HudEditorScreen.this.q) {
               this.J.aAsVoid(var5 != 0 ? 1.0 : 0.0);
            }

            double var6 = this.J.bAsDouble(var5 != 0 ? 1.0 : 0.0, 160);
            int var8 = this.X ? bd.m : (this.B ? bd.u : bd.W);
            if (this.X || var4 || var6 > 0.001) {
               GuiDrawing.CAsVoid(
                  var1, this.k, this.x, this.V, this.F, 7, this.X ? bg.xAsInt(bd.u, bd.e, var6 * 0.12) : bg.xAsInt(bd.m, bd.y, var4 ? 1.0 : var6)
               );
            }

            int var10 = this.f ? 0 : TextDrawing.UAsInt(this.R);
            int var11 = var10 + (this.L.isEmpty() ? 0 : 13 + (var10 == 0 ? 0 : 6));
            int var12 = this.k + Math.max(5, (this.V - var11) / 2);
            if (!this.L.isEmpty()) {
               var1.getMatrices().pushMatrix();

               try {
                  if (this.P) {
                     var1.getMatrices().translate((float)(var12 * 2 + 13), 0.0F);
                     var1.getMatrices().scale(-1.0F, 1.0F);
                  }

                  GuiIcons.aAsVoid(var1, GuiIcons.PAsIdentifier(this.L), this.f ? this.k + (this.V - 13) / 2 : var12, this.x + (this.F - 13) / 2, 13, var8);
               } finally {
                  var1.getMatrices().popMatrix();
               }
            }

            if (!this.f) {
               TextDrawing.UAsVoid(
                  var1,
                  TextDrawing.HAsString(this.R, this.V - (this.L.isEmpty() ? 12 : 27)),
                  var12 + (this.L.isEmpty() ? 0 : 19),
                  this.x + (this.F - 9) / 2,
                  var8
               );
            }

            if (this.y && this.Q) {
               GuiDrawing.dAsVoid(var1, this.k, this.x, this.V, this.F, 7);
            }
         }
      }

      void TAsVoid(DrawContext var1) {
         if (this.f) {
            int var2 = TextDrawing.jAsInt(this.R, TextDrawing.Type_s.HELP) + 16;
            int var3 = d7.LAsInt(this.k + (this.V - var2) / 2, var2, HudEditorScreen.this.width);
            int var4 = Math.max(0, this.x - 27);
            GuiDrawing.CAsVoid(var1, var3, var4, var2, 20, 6, bd.V);
            TextDrawing.NAsVoid(var1, this.R, var3 + 8, var4 + 6, bd.u, TextDrawing.Type_s.HELP);
         }
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (var5 == 0 && this.contains(var1, var3)) {
            GuiSounds.rAsVoid(GuiSounds.Type_o.CLICK);
            this.r.run();
            return true;
         } else {
            return false;
         }
      }

      @Override
      public boolean key(int var1) {
         if (!this.B || var1 != 257 && var1 != 32) {
            return false;
         } else {
            GuiSounds.rAsVoid(GuiSounds.Type_o.CLICK);
            this.r.run();
            return true;
         }
      }
   }

   private final class Type_p extends GuiWidget {
      final HudRenderer.Type_m T;
      private final w2 f = new w2(0.0);
      private final w2 E = new w2(0.0);

      Type_p(HudRenderer.Type_m var2) {
         this.T = var2;
         this.F = 36;
         this.E.aAsVoid(HudRenderer.HAsBoolean(var2) ? 1.0 : 0.0);
      }

      @Override
      public void render(DrawContext var1, int var2, int var3) {
         boolean var4 = HudRenderer.HAsBoolean(this.T);
         int var5 = HudEditorScreen.this.O.aAsLp_Type_m() == this.T ? 1 : 0;
         if (!HudEditorScreen.this.q) {
            this.f.aAsVoid(this.contains((double)var2, (double)var3) ? 1.0 : 0.0);
            this.E.aAsVoid(var4 ? 1.0 : 0.0);
         }

         double var6 = this.f.bAsDouble(!this.contains((double)var2, (double)var3) && !this.y ? 0.0 : 1.0, 160);
         if (var5 != 0 || var6 > 0.001) {
            GuiDrawing.CAsVoid(var1, this.k, this.x, this.V, this.F, 7, bg.xAsInt(bd.m, bd.y, var5 != 0 ? 0.8 : var6 * 0.65));
         }

         GuiDrawing.CAsVoid(var1, this.k + 4, this.x + 6, 24, 24, 6, bd._Z);
         GuiIcons.aAsVoid(var1, GuiIcons.PAsIdentifier(GuiIcons.XAsString(this.T.a)), this.k + 10, this.x + 12, 12, var4 ? bd.u : bd.e);
         TextDrawing.NAsVoid(
            var1, TextDrawing.HAsString(HudRenderer.XAsString(this.T.a), this.V - 74), this.k + 36, this.x + 7, bd.u, TextDrawing.Type_s.MODULE
         );
         TextDrawing.NAsVoid(var1, var4 ? "Visible" : "Hidden", this.k + 36, this.x + 21, bd.e, TextDrawing.Type_s.HELP);
         GuiDrawing.uAsVoid(var1, this.k + this.V - 30, this.x + 12, this.E.bAsDouble(var4 ? 1.0 : 0.0, 180));
         if (this.y && this.Q) {
            GuiDrawing.dAsVoid(var1, this.k, this.x, this.V, this.F, 7);
         }
      }

      private void LAsVoid() {
         HudEditorScreen.this.O.WAsVoid(() -> {
            HudRenderer.IAsVoid(this.T, !HudRenderer.HAsBoolean(this.T));
            HudEditorScreen.this.O.VAsVoid();
         });
         GuiSounds.rAsVoid(HudRenderer.HAsBoolean(this.T) ? GuiSounds.Type_o.ON : GuiSounds.Type_o.OFF);
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (var5 == 1) {
            HudEditorScreen.this.cAsVoid(this.T);
            return true;
         } else if (var5 != 0) {
            return false;
         } else {
            if (var1 >= (double)(this.k + this.V - 38)) {
               this.LAsVoid();
            } else {
               HudEditorScreen.this.TAsVoid(this.T);
            }

            return true;
         }
      }

      @Override
      public boolean key(int var1) {
         if (var1 == 32) {
            this.LAsVoid();
            return true;
         } else if (var1 == 257) {
            HudEditorScreen.this.TAsVoid(this.T);
            return true;
         } else {
            return false;
         }
      }
   }
}
