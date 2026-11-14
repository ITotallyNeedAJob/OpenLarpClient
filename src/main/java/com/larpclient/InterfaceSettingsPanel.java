package com.larpclient;

import com.larpclient.module.BooleanSetting;
import com.larpclient.module.ColorSetting;
import com.larpclient.module.KeybindSetting;
import com.larpclient.module.ModeSetting;
import com.larpclient.module.NumberSetting;
import com.larpclient.module.Setting;
import com.larpclient.update.UpdateRuntime;
import com.larpclient.update.UpdateService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.input.KeyInput;

final class InterfaceSettingsPanel extends SettingsPanel {
   private String F = "Interface";
   private boolean y;
   private boolean p;
   private final List<InterfaceSettingsPanel.Type_x> A = new ArrayList<>();
   private final WeatherRenderer f = new WeatherRenderer(0.0);
   private HudRenderer.Type_m U;
   private final Map<ActionButton, HudRenderer.Type_m> G = new LinkedHashMap<>();
   private NumberSetting x;
   private NumberSetting y2;
   private NumberSetting scale;
   private BooleanSetting visible;
   private ModeSetting alignment;

   void EAsVoid(HudRenderer.Type_m var1) {
      this.U = var1;
   }

   InterfaceSettingsPanel() {
      super("Interface");
   }

   boolean xAsBoolean() {
      return this.F.equals("HUD");
   }

   void TAsVoid(String var1) {
      this.F = var1;
   }

   @Override
   protected void build() {
      this.JAsVoid();
      this.A.clear();
      this.G.clear();
      this.x = this.y2 = this.scale = null;
      this.visible = null;
      this.alignment = null;
      this.O = this.y;
      this.W = this.F.equals("Interface") ? "GUI Settings" : this.F;

      for (String var4 : new String[]{"Interface", "HUD", "Account", "Updates"}) {
         this.pAsVoid(var4.equals("Interface") ? "GUI Settings" : var4, this.F.equals(var4), () -> {
            if (!this.F.equals(var4)) {
               this.F = var4;
               this.s = 0;
               this.sAsVoid();
               ClientLog.GAsVoid("GUI", "Settings switched to {}", var4);
               this.build();
            }
         });
      }

      if (this.y) {
         this.C = "Larp Client";
         this.kAsVoid("Ready");
         this.fAsQo("Cancel", () -> {
            this.y = false;
            this.build();
         });
         this.fAsQo("Continue", () -> {
            this.y = false;
            SessionManager.TAsVoid();
            this.i.client().setScreen(new ActivationScreen(this.i));
         }).p = true;
      } else {
         String var71 = this.F;
         switch (var71) {
            case "Interface":
               this.IAsVoid();
               break;
            case "HUD":
               this.HAsVoid();
               break;
            case "Account":
               this.kAsVoid("Minecraft: " + this.i.client().getSession().getUsername());
               this.kAsVoid(SessionManager.pAsString());
               this.AAsVoid(new SectionHeaderWidget("Discord"));
               ProfileManager var8 = LarpClient.CAsLarpClient().fAsBo();
               BooleanSetting var10 = new BooleanSetting("Discord activity", "Show Playing with larpclient.com on Discord", var8.yAsN7().u);
               var10.onChanged(var2 -> {
                  if (!var8.bAsBoolean(var2)) {
                     var10.setValue(Boolean.valueOf(var8.yAsN7().u));
                  }
               });
               this.AAsVoid(new SettingWidget(var10));
               this.kAsVoid("Requires the Discord desktop app and activity sharing enabled.");
               break;
            case "Updates":
               this.AAsVoid(new SectionHeaderWidget("Client updates"));
               ClientPreferences var7x = LarpClient.CAsLarpClient().fAsBo().yAsN7();
               BooleanSetting var9 = new BooleanSetting("Automatic checks", "Check signed public release information after startup", var7x.S);
               var9.onChanged(var2 -> {
                  if (!UpdateRuntime.automatic(var2)) {
                     var9.setValue(Boolean.valueOf(var7x.S));
                  }
               });
               this.AAsVoid(new SettingWidget(var9));
               this.MAsQo("View updates / check now", () -> {
                  UpdateService var1 = UpdateRuntime.service();
                  if (var1 != null) {
                     var1.check();
                  }

                  UpdateRuntime.open(this.i);
               });
               this.kAsVoid("Press Update on a new release and it installs when you close Minecraft. No launcher setup is needed.");
               this.kAsVoid("On macOS, or when the client JAR is not directly in your mods folder, use the official downloads page.");
               break;
            default:
               throw new IllegalStateException("Unknown settings section");
         }
      }
   }

   private void KAsVoid(Runnable var1) {
      var1.run();
      LarpClient.CAsLarpClient().fAsBo().NAsVoid();
   }

   private void GAsVoid(String var1, String var2, GuiWidget... var3) {
      WidgetLabel var4 = new WidgetLabel(var1, var2);
      this.AAsVoid(var4);

      for (GuiWidget var8 : var3) {
         this.AAsVoid(var8);
      }

      this.A.add(new InterfaceSettingsPanel.Type_x(var4, List.of(var3)));
   }

   private void IAsVoid() {
      ClientPreferences var1 = LarpClient.CAsLarpClient().fAsBo().yAsN7();
      this.GAsVoid("Main color", "Choose the main GUI theme.", new ThemePreviewWidget(() -> var1.M, var2x -> this.KAsVoid(() -> var1.M = var2x)));
      ColorSetting var2 = new ColorSetting("Primary", "Primary theme color", UiStyle.w);
      var2.onChanged(var0 -> UiStyle.w = var0);
      ColorSetting var3 = new ColorSetting("Secondary", "Secondary theme color", UiStyle.Q);
      var3.onChanged(var0 -> UiStyle.Q = var0);
      ActionButton var4 = new ActionButton("Reset palette", () -> {
         UiStyle.w = -1381654;
         UiStyle.Q = -6250336;
         this.build();
      });
      var4.u = true;
      BooleanSetting var5 = new BooleanSetting("Animation gradient", "Animate the GUI accent between primary and secondary", var1.b);
      var5.onChanged(var2x -> this.KAsVoid(() -> var1.b = var2x));
      this.GAsVoid("Accent color", "Accent colors of the GUI.", new QueueJob(var1x -> {
         UiStyle.w = var1x;
         var2.setValue(Integer.valueOf(var1x));
      }), new SettingWidget(var2), new SettingWidget(var3), new SettingWidget(var5), var4);
      ModeSetting var6 = new ModeSetting("Open", "How the GUI appears", var1.A, XBlurEffect.L.toArray(String[]::new));
      var6.onChanged(var2x -> this.KAsVoid(() -> var1.A = var2x));
      ModeSetting var7 = new ModeSetting(
         "Close",
         "How the GUI leaves. Reverse plays the opening backwards; the others break the window apart. Reduced motion always uses Reverse",
         var1.V,
         ClientPreferences.Y.toArray(String[]::new)
      );
      var7.onChanged(var2x -> this.KAsVoid(() -> var1.V = var2x));
      NumberSetting var8 = new NumberSetting(
         "Speed", "Opening and closing duration in milliseconds. Breaking effects take about twice as long", (double)var1.n, 100.0, 1000.0, 10.0
      );
      var8.onChanged(var2x -> this.KAsVoid(() -> var1.n = var2x.intValue()));
      this.GAsVoid("Animation", "GUI open/close animations.", new SettingWidget(var6), new SettingWidget(var7), new SettingWidget(var8));
      NumberSetting var9 = new NumberSetting("Opacity", "Window and navigation opacity", var1.G * 100.0, 0.0, 100.0, 1.0);
      var9.onChanged(var2x -> this.KAsVoid(() -> var1.G = var2x / 100.0));
      this.GAsVoid("Transparent", "Make your GUI transparent.", new SettingWidget(var9));
      ModeSetting var10 = new ModeSetting(
         "Style", "What the GUI draws behind its window. Dim darkens the world without light", var1.D, ClientPreferences.Q.toArray(String[]::new)
      );
      var10.onChanged(var2x -> this.KAsVoid(() -> var1.D = var2x));
      NumberSetting var11 = new NumberSetting("Dim", "How much the backdrop darkens the world, in percent. Not used with None", var1.JE * 100.0, 0.0, 90.0, 1.0);
      var11.onChanged(var2x -> this.KAsVoid(() -> var1.JE = var2x / 100.0));
      NumberSetting var12 = new NumberSetting(
         "Blur",
         "Blur behind the glass while in a world: 0 is none, 2 the usual strength, 4 the heaviest. Needs Blur quality on in the Performance module",
         (double)var1.o,
         0.0,
         4.0,
         1.0
      );
      var12.onChanged(var2x -> this.KAsVoid(() -> var1.o = var2x.intValue()));
      ModeSetting var13 = new ModeSetting(
         "Blur area", "Blur only behind the window, or the whole world while the GUI is open", var1.s, ClientPreferences.P.toArray(String[]::new)
      );
      var13.onChanged(var2x -> this.KAsVoid(() -> var1.s = var2x));
      this.GAsVoid("Backdrop", "Shown behind the GUI.", new SettingWidget(var10), new SettingWidget(var11), new SettingWidget(var12), new ModeSettingWidget(var13));
      ModeSetting var14 = new ModeSetting(
         "Cards", "Module cards: Filled, an Outline only, or Flat without a contour", var1.r, ClientPreferences.K.toArray(String[]::new)
      );
      var14.onChanged(var2x -> this.KAsVoid(() -> var1.r = var2x));
      ModeSetting var15 = new ModeSetting(
         "Enabled cards",
         "How an enabled module stands out: a Glow with an accent wash, only the Tint, an accent Text title, or None",
         var1.T,
         ClientPreferences.h.toArray(String[]::new)
      );
      var15.onChanged(var2x -> this.KAsVoid(() -> var1.T = var2x));
      ModeSetting var16 = new ModeSetting("Switches", "Pill, Square or Minimal switches", var1.d, ClientPreferences.C.toArray(String[]::new));
      var16.onChanged(var2x -> this.KAsVoid(() -> var1.d = var2x));
      ModeSetting var17 = new ModeSetting("Sliders", "Pill, Minimal or Classic sliders", var1.R, ClientPreferences.F.toArray(String[]::new));
      var17.onChanged(var2x -> this.KAsVoid(() -> var1.R = var2x));
      NumberSetting var18 = new NumberSetting(
         "Rounding", "Corner rounding of the window, cards and controls, in percent. 0 gives square corners", var1.X * 100.0, 0.0, 200.0, 5.0
      );
      var18.onChanged(var2x -> this.KAsVoid(() -> var1.X = var2x / 100.0));
      BooleanSetting var19 = new BooleanSetting("Accent glow", "A soft accent glow breathing around the window", var1.B);
      var19.onChanged(var2x -> this.KAsVoid(() -> var1.B = var2x));
      BooleanSetting var20 = new BooleanSetting("Ripple", "A ripple spreads from the pointer when you click a module card", var1.z);
      var20.onChanged(var2x -> this.KAsVoid(() -> var1.z = var2x));
      BooleanSetting var21 = new BooleanSetting("Hover lift", "Module cards rise a little under the pointer", var1.I);
      var21.onChanged(var2x -> this.KAsVoid(() -> var1.I = var2x));
      this.GAsVoid(
         "Style",
         "Look of cards, controls and effects.",
         new SettingWidget(var14),
         new SettingWidget(var15),
         new SettingWidget(var16),
         new SettingWidget(var17),
         new SettingWidget(var18),
         new SettingWidget(var19),
         new SettingWidget(var20),
         new SettingWidget(var21)
      );
      NumberSetting var22 = new NumberSetting("Intensity", "Glow strength", (double)BloomQuality.h, 0.0, 2.0, 0.05);
      var22.onChanged(var0 -> BloomQuality.h = var0.floatValue());
      NumberSetting var23 = new NumberSetting("Radius", "Halo spread", (double)BloomQuality.X, 0.5, 3.0, 0.1);
      var23.onChanged(var0 -> BloomQuality.X = var0.floatValue());
      this.GAsVoid("Bloom", "Shared world glow.", new SettingWidget(var22), new SettingWidget(var23));
      LarpClient var24 = LarpClient.CAsLarpClient();
      KeybindSetting var25 = new KeybindSetting(
         "ClickUI keybind",
         "Open or close ClickUI with a key or a middle or side mouse button. Escape/Delete clears the binding; right click restores Right Shift.",
         344
      );
      var25.lAsVoid(var24.kAsInt());
      var25.onChanged(var24::sAsVoid);
      NumberSetting var26 = new NumberSetting(
         "Scale", "ClickUI size in percent. Larger sizes are limited by the window size.", var1.JD * 100.0, 50.0, 200.0, 5.0
      );
      var26.onChanged(var2x -> this.KAsVoid(() -> var1.JD = var2x / 100.0));
      ActionButton var27 = new ActionButton("Reset scale", () -> var26.setClampedValue(100.0));
      var27.u = true;
      this.GAsVoid("ClickUI", "Shortcut and size of the client interface.", new SettingWidget(var25), new SettingWidget(var26), var27);
      BooleanSetting var28 = new BooleanSetting("Custom main menu", "Use the client title screen and menu backgrounds instead of vanilla", var1.l);
      var28.onChanged(var2x -> {
         this.KAsVoid(() -> var1.l = var2x);
         ClientLog.GAsVoid("GUI", "Custom main menu {}", var2x ? "enabled" : "disabled");
      });
      this.GAsVoid("Main menu", "Client title screen outside worlds.", new SettingWidget(var28));
      BooleanSetting var29 = new BooleanSetting(
         "Legacy layout",
         "Floating category panels: left click toggles a module, right click drops down its settings. Applies the next time you open the GUI",
         var1.y.R
      );
      var29.onChanged(var2x -> {
         this.KAsVoid(() -> var1.y.R = var2x);
         ClientLog.GAsVoid("GUI", "Legacy ClickGUI {}", var2x ? "enabled" : "disabled");
      });
      ColorSetting var30 = new ColorSetting("Panel color", "Enabled module rows and the menu key outline in the legacy layout", var1.y.b);
      var30.onChanged(var2x -> this.KAsVoid(() -> var1.y.b = var2x));
      ActionButton var31 = new ActionButton("Reset panel layout", () -> this.KAsVoid(var1.y::SAsVoid));
      var31.u = true;
      this.GAsVoid("Legacy ClickGUI", "Classic panels with drop-down settings.", new SettingWidget(var29), new SettingWidget(var30), var31);
   }

   private static int RAsInt(int var0, int var1) {
      if (var1 != 3) {
         return var0 % var1;
      } else {
         return switch (var0) {
            case 0, 5, 8 -> 0;
            case 1, 4, 6, 9 -> 1;
            default -> 2;
         };
      }
   }

   private static int fAsInt(int[] var0) {
      int var1 = 0;

      for (int var2 = 1; var2 < var0.length; var2++) {
         if (var0[var2] < var0[var1]) {
            var1 = var2;
         }
      }

      return var1;
   }

   @Override
   protected boolean layoutCustomContent() {
      if (!this.O && this.F.equals("Interface") && !this.A.isEmpty()) {
         int var1 = this.r - this.H - 16;
         int var2 = var1 >= 470 ? 3 : (var1 >= 320 ? 2 : 1);
         int[] var3 = new int[var2];
         int[] var4 = new int[var2];
         if (var2 == 3) {
            var4[0] = (var1 - 16) * 31 / 100;
            var4[1] = (var1 - 16) * 38 / 100;
            var4[2] = var1 - 16 - var4[0] - var4[1];
         } else {
            Arrays.fill(var4, Math.max(1, (var1 - 8 * (var2 - 1)) / var2));
         }

         this.X = 0;
         int[] var5 = new int[this.A.size()];

         for (int var6 = 0; var6 < this.A.size(); var6++) {
            InterfaceSettingsPanel.Type_x var7 = this.A.get(var6);
            int var8 = var2 == 3 ? RAsInt(var6, var2) : fAsInt(var3);
            var5[var6] = var8;
            var7.panel.F = 40 + var7.controls.stream().mapToInt(var0 -> var0.F + 4).sum();
            var3[var8] += var7.panel.F + 8;
            this.X = Math.max(this.X, var3[var8] - 8);
         }

         this.s = Math.clamp((long)this.s, 0, Math.max(0, this.X - (this.S - this.k)));
         int var16 = (int)Math.round(this.f.bAsDouble((double)this.s, 150));
         Arrays.fill(var3, 0);

         for (int var17 = 0; var17 < this.A.size(); var17++) {
            InterfaceSettingsPanel.Type_x var18 = this.A.get(var17);
            int var9 = var5[var17];
            int var10 = this.H + 8;

            for (int var11 = 0; var11 < var9; var11++) {
               var10 += var4[var11] + 8;
            }

            int var19 = this.k + var3[var9] - var16;
            var18.panel.bounds(var10, var19, var4[var9]);
            int var12 = var19 + 36;

            for (GuiWidget var14 : var18.controls) {
               var14.bounds(var10 + 6, var12, Math.max(1, var4[var9] - 12));
               if (var14 instanceof SettingWidget var15) {
                  var15.fAsVoid(this.P.top() + 12, this.S);
               }

               var12 += var14.F + 4;
            }

            var3[var9] += var18.panel.F + 8;
         }

         return true;
      } else {
         return false;
      }
   }

   private void HAsVoid() {
      this.W = this.U == null ? "HUD overlays" : "HUD / " + HudRenderer.XAsString(this.U.a);
      if (this.U == null) {
         this.MAsQo("Open HUD editor", () -> this.i.eAsVoid());
         this.kAsVoid("Use Editor for placement and previews. Chat dragging remains available.");

         for (HudRenderer.Type_m var8 : HudRenderer.S) {
            ActionButton var11 = this.MAsQo(HudRenderer.XAsString(var8.a), () -> {
               this.U = var8;
               this.s = 0;
               this.sAsVoid();
               this.build();
            });
            var11.E = HudRenderer.hAsString(var8);
            var11.r = true;
            var11.F = 34;
            var11.g = HudRenderer.HAsBoolean(var8);
            var11.z = () -> {
               HudRenderer.IAsVoid(var8, !HudRenderer.HAsBoolean(var8));
               ClientLog.XAsVoid("HUD editor", "{} visibility={}", var8.a, HudRenderer.HAsBoolean(var8));
            };
            this.G.put(var11, var8);
         }

         this.AAsVoid(new SectionHeaderWidget("Placement guides"));
         BooleanSetting var6 = new BooleanSetting("Snap", "Snap to viewport edges and center while dragging", GuiWorkspace.c.snapping());
         var6.onChanged(var0 -> GuiWorkspace.c = new GuiWorkspace.Type_a(GuiWorkspace.c.panel(), GuiWorkspace.c.collapsed(), var0, GuiWorkspace.c.grid()));
         this.AAsVoid(new SettingWidget(var6));
         BooleanSetting var9 = new BooleanSetting("8px Grid", "Optional grid; Alt bypasses grid and snapping", GuiWorkspace.c.grid());
         var9.onChanged(var0 -> GuiWorkspace.c = new GuiWorkspace.Type_a(GuiWorkspace.c.panel(), GuiWorkspace.c.collapsed(), GuiWorkspace.c.snapping(), var0));
         this.AAsVoid(new SettingWidget(var9));
      } else {
         HudRenderer.Type_m var1 = this.U;
         var1.PAsVoid(this.width, this.height);
         this.MAsQo("All HUD overlays", () -> {
            this.U = null;
            this.s = 0;
            this.sAsVoid();
            this.build();
         });
         this.kAsVoid(HudRenderer.hAsString(var1) + ". Visibility restores the required module and content toggle.");
         this.visible = new BooleanSetting("Visible", "Hide only this overlay; restoring enables its content", HudRenderer.HAsBoolean(var1));
         this.visible.onChanged(var2x -> {
            if (!this.p) {
               HudRenderer.IAsVoid(var1, var2x);
            }
         });
         this.x = new NumberSetting("X", "Layout origin in GUI pixels", (double)var1.PAsInt(this.width), 0.0, (double)this.width, 1.0);
         this.y2 = new NumberSetting("Y", "Layout origin in GUI pixels", (double)var1.ZAsInt(this.height), 0.0, (double)this.height, 1.0);
         this.x.onChanged(var2x -> {
            if (!this.p) {
               var1.SAsVoid(var2x.intValue(), var1.ZAsInt(this.height), this.width, this.height);
            }
         });
         this.y2.onChanged(var2x -> {
            if (!this.p) {
               var1.SAsVoid(var1.PAsInt(this.width), var2x.intValue(), this.width, this.height);
            }
         });
         this.scale = new NumberSetting("Scale", "Uniform content scale", var1.BAsDouble(), 0.5, 2.5, 0.05);
         this.scale.onChanged(var2x -> {
            if (!this.p) {
               var1.iAsVoid(var2x);
            }
         });
         this.alignment = new ModeSetting(
            "Alignment",
            "Normalized screen anchor",
            GAsString(var1),
            "Free",
            "Top left",
            "Top center",
            "Top right",
            "Center left",
            "Center",
            "Center right",
            "Bottom left",
            "Bottom center",
            "Bottom right"
         );
         this.alignment.onChanged(var2x -> {
            if (!this.p && !var2x.equals("Free")) {
               int var3x = this.alignment.getModes().indexOf(var2x) - 1;
               GuiWorkspace.B.put(var1.a, new GuiWorkspace.Type_g((double)(var3x % 3) * 0.5, (double)(var3x / 3) * 0.5));
            }
         });
         this.AAsVoid(new SectionHeaderWidget("Placement"));

         for (Setting var3 : List.of(this.visible, this.x, this.y2, this.scale, this.alignment)) {
            this.AAsVoid(new SettingWidget(var3));
         }

         List var7 = HudRenderer.HAsList(var1);
         if (!var7.isEmpty()) {
            this.AAsVoid(new SectionHeaderWidget("Appearance"));
         }

         for (Setting var4 : (Iterable<Setting>) (var7)) {
            this.AAsVoid(new SettingWidget(var4, true));
         }

         if (var1.a.equals("crosshair")) {
            this.MAsQo("Center crosshair", () -> {
               var1.DAsVoid();
               this.build();
            });
         }

         this.MAsQo("Reset placement", () -> {
            var1.AAsVoid();
            this.build();
         });
      }
   }

   private static String GAsString(HudRenderer.Type_m var0) {
      GuiWorkspace.Type_g var1 = GuiWorkspace.B.get(var0.a);
      if (var1 == null) {
         return var0.a.equals("crosshair") ? "Center" : "Free";
      } else {
         int var2 = (int)Math.round(var1.x() * 2.0);
         int var3 = (int)Math.round(var1.y() * 2.0);
         return Math.abs(var1.x() - (double)var2 * 0.5) < 0.001 && Math.abs(var1.y() - (double)var3 * 0.5) < 0.001
            ? List.of("Top left", "Top center", "Top right", "Center left", "Center", "Center right", "Bottom left", "Bottom center", "Bottom right")
               .get(var3 * 3 + var2)
            : "Free";
      }
   }

   @Override
   protected void renderExtra(DrawContext var1, int var2, int var3) {
      for (Entry var5 : this.G.entrySet()) {
         ((ActionButton)var5.getKey()).g = HudRenderer.HAsBoolean((HudRenderer.Type_m)var5.getValue());
         ((ActionButton)var5.getKey()).E = HudRenderer.hAsString((HudRenderer.Type_m)var5.getValue());
      }

      if (this.U != null && this.x != null) {
         this.p = true;

         try {
            this.x.setClampedValue((double)this.U.PAsInt(this.width));
            this.y2.setClampedValue((double)this.U.ZAsInt(this.height));
            this.scale.setClampedValue(this.U.BAsDouble());
            this.visible.setValue(Boolean.valueOf(HudRenderer.HAsBoolean(this.U)));
            this.alignment.setValue(GAsString(this.U));
         } finally {
            this.p = false;
         }
      }
   }

   @Override
   protected void layout() {
      super.layout();
      if (!this.O) {
         this.r = this.F.equals("Interface") ? this.P.right() : Math.min(this.P.right(), this.H + 440);
         this.iAsVoid();
      }
   }

   @Override
   protected void dismissDialog() {
      if (this.y) {
         this.y = false;
         this.build();
      }
   }

   @Override
   public boolean keyPressed(KeyInput input) {
      if (this.y && input.key() == 256) {
         this.dismissDialog();
         return true;
      } else {
         return super.keyPressed(input);
      }
   }

   private static record Type_x(WidgetLabel panel, List<GuiWidget> controls) {
   }
}
