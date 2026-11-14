package com.larpclient;

import com.larpclient.module.Module;
import com.larpclient.module.WorldMenu;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import org.joml.Matrix3x2f;
import org.joml.Vector2f;

public final class ClickGuiScreen extends GuiPanel {
   private final Screen n;
   private final List<ActionButton> D = new ArrayList<>();
   private final List<ActionButton> GroundSample = new ArrayList<>();
   private final Map<ActionButton, Module> X = new IdentityHashMap<>();
   private final Map<ActionButton, FavoriteModuleWidget> W = new IdentityHashMap<>();
   private boolean q;
   private boolean O;
   private long H;
   private final ModuleSettingsPanel P = new ModuleSettingsPanel();
   private final InterfaceSettingsPanel r = new InterfaceSettingsPanel();
   private ProfileSettingsPanel R;
   private SettingsPanel B;
   private xm F;
   private ScreenScale h;
   private GuiCategory V;
   private Module z;
   private String E = "Modules";
   private String SahurDanceEngine = "";
   private String MenuPopupProvider = "";
   private boolean m;
   private boolean a;
   private boolean w;
   private static ClickGuiScreen Y;
   private Object p;
   private ColoredGeometryPipeline L;
   private int U;
   private int x;
   private double K;
   private double v = 1.0;
   private boolean T;
   private boolean J;
   private boolean e;
   private final w2 C = new w2(0.0);
   private final w2 N = new w2(1.0);
   private TextInputWidget l;
   private ActionButton A;
   private ActionButton I;
   private ActionButton u;
   private ActionButton S;
   private final AccountSkinPreview ChinaHatCodec = new AccountSkinPreview();
   private static final DiscordAvatarRenderer TextLayout = new DiscordAvatarRenderer();
   private final w2 ChatPayParser = new w2(0.0);
   private final w2 ScreenScale = new w2(0.0);
   private final w2 Z = new w2(0.0);
   private final w2 G = new w2(0.0);
   private boolean M;
   private wz s;
   private final Matrix3x2f AuctionNotifier = new Matrix3x2f();

   public ClickGuiScreen() {
      this(null);
   }

   public ClickGuiScreen(Screen var1) {
      super("Larp client");
      this.n = var1;
      this.V = LarpClient.CAsLarpClient().fAsBo().yAsN7().H ? GuiCategory.FAVORITES : GuiCategory.hAsAk(GuiWorkspace.I);
   }

   public void IAsVoid() {
      this.s = new wz();
   }

   public static ClickGuiScreen cAsPn(Screen var0, String var1, boolean var2) {
      ClickGuiScreen var3 = new ClickGuiScreen(var0);
      var3.T = true;
      var3.E = "Configs";
      var3.R = new ProfileSettingsPanel(var3);
      if (var1 != null) {
         var3.R.wI.S = var1;
      }

      var3.R.wf = var2;
      return var3;
   }

   public static ClickGuiScreen RAsPn(Screen var0) {
      ClickGuiScreen var1 = cAsPn(var0, null, false);
      var1.m = true;
      return var1;
   }

   public static ClickGuiScreen uAsPn(Screen var0, String var1) {
      ClickGuiScreen var2 = new ClickGuiScreen(var0);
      var2.T = true;
      var2.E = "Settings";
      var2.r.TAsVoid(var1);
      return var2;
   }

   public static ClickGuiScreen HAsPn(Screen var0, HudRenderer.Type_m var1) {
      ClickGuiScreen var2 = uAsPn(var0, "HUD");
      var2.r.EAsVoid(var1);
      return var2;
   }

   MinecraftClient client() {
      return this.client;
   }

   boolean cAsBoolean(ProfileSettingsPanel var1) {
      return this.B == var1;
   }

   public void close() {
      if (!this.J) {
         if (this.B != null) {
            this.B.suspend();
         }

         this.P.fAsVoid();
         GuiSounds.rAsVoid(GuiSounds.Type_o.CLOSE);
         this.J = true;
         this.g.bAsDouble(0.0, this.UAsInt());
         if (!WorldMenu.QAsBoolean(this)) {
            this.p = this.client.world;
            Y = this;
            this.L = this.CAsQ1();
            if (this.n instanceof ClientTitleScreen && !LarpClient.CAsLarpClient().fAsBo().yAsN7().l) {
               ClientLog.GAsVoid("GUI", "Custom main menu disabled; returning to the vanilla title screen");
               this.client.setScreen(new TitleScreen());
            } else {
               this.client.setScreen(this.n);
            }
         }
      }
   }

   private int UAsInt() {
      return LarpClient.CAsLarpClient().fAsBo().yAsN7().n;
   }

   private ColoredGeometryPipeline CAsQ1() {
      ClientPreferences var1 = LarpClient.CAsLarpClient().fAsBo().yAsN7();
      String var2 = var1.V;
      if (!var2.equals("Reverse") && var1.Z) {
         ArrayList var3 = new ArrayList();
         float var4 = this.o.m00();
         Vector2f var5 = this.o.transformPosition((float)this.F.left(), (float)this.F.top(), new Vector2f());
         var3.add(new ColoredGeometryPipeline.Type_i(var5.x, var5.y, (float)this.F.width() * var4, (float)this.F.height() * var4, (float)bd.q * var4));
         if (this.z != null && this.B == null && this.F.detachedInspector() && this.jAsDouble() > 0.5) {
            xm var6 = this.F.moduleInspector(this.K);
            Vector2f var7 = this.o.transformPosition((float)var6.inspectorX(), (float)var6.top(), new Vector2f());
            var3.add(
               new ColoredGeometryPipeline.Type_i(
                  var7.x, var7.y, (float)(var6.right() - var6.inspectorX()) * var4, (float)var6.height() * var4, (float)bd.q * var4
               )
            );
         }

         return ColoredGeometryPipeline.YAsQ1(var2, var3, this.UAsInt());
      } else {
         return null;
      }
   }

   public static boolean MAsBoolean() {
      return Y != null;
   }

   public static void kAsVoid(DrawContext var0, float var1) {
      ClickGuiScreen var2 = Y;
      if (var2 != null) {
         MinecraftClient var3 = MinecraftClient.getInstance();
         ColoredGeometryPipeline var4 = var2.L;
         if (var3.currentScreen == var2.n
            && var3.world == var2.p
            && var3.getOverlay() == null
            && var2.width == var0.getScaledWindowWidth()
            && var2.height == var0.getScaledWindowHeight()
            && var2.h.equals(uAsY(var3))
            && (var4 == null ? !(var2.g.yAsDouble() <= 0.0) : !var4.fAsBoolean())) {
            try {
               var0.createNewRootLayer();
               if (var4 != null) {
                  ClientPreferences var5 = LarpClient.CAsLarpClient().fAsBo().yAsN7();

                  try (bd.Type_e var6 = bd.VAsBd_Type_e(var5.M, var5.G, var5.b)) {
                     TAsVoid(var0, var0.getScaledWindowWidth(), var0.getScaledWindowHeight(), 1.0 - var4.BAsDouble(), var5);
                  }

                  var4.kAsVoid(var0);
               } else {
                  var2.render(var0, -10000, -10000, var1);
               }
            } catch (RuntimeException var111) {
               QAsVoid();
               ClientLog.lAsVoid("GUI", "Window exit rendering failed; input remains released", var111);
            }
         } else {
            if (var4 != null) {
               ClientLog.XAsVoid("GUI", "Close effect {}", var4.fAsBoolean() ? "finished" : "stopped by PetEffectRenderer screen, world or window size change");
            }

            QAsVoid();
         }
      }
   }

   private static void QAsVoid() {
      if (Y != null && Y.L != null) {
         Y.L = null;
         ColoredGeometryPipeline.IAsVoid();
      }

      Y = null;
   }

   private static ScreenScale uAsY(MinecraftClient var0) {
      return com.larpclient.y.paired(var0.getWindow(), LarpClient.CAsLarpClient().fAsBo().yAsN7().JD);
   }

   protected void init() {
      this.h = uAsY(this.client);
      this.o.set(this.h.transform());
      QAsVoid();
      if (!this.T) {
         GuiWorkspace.Type_e var1 = GuiWorkspace.y;
         this.E = var1.page();
         this.d = var1.query();
         this.U = var1.moduleScroll();
         this.z = LarpClient.CAsLarpClient().mAsF().TAsModule(var1.module());
         this.z = nk.YAsModule(this.z, LarpClient.CAsLarpClient().mAsF().NAsList(), LarpClient.CAsLarpClient().fAsBo().yAsN7());
         if (this.z == null && !var1.module().isEmpty()) {
            ClientLog.GAsVoid("GUI", "Restored unavailable module {} with no selection", var1.module());
         }

         if (this.z != null && this.d.isBlank() && this.V != GuiCategory.FAVORITES) {
            this.V = GuiCategory.SAsAk(this.z);
         }

         this.T = true;
      }

      this.F = this.E.equals("Modules")
         ? xm.calculateModules(this.h.width(), this.h.height(), this.z == null ? 0.0 : 1.0)
         : xm.calculate(this.h.width(), this.h.height());
      this.J = false;
      this.g.aAsVoid(this.s == null ? 0.0 : 1.0);
      this.g.bAsDouble(1.0, this.UAsInt());
      if (!this.a) {
         this.a = true;
         GuiSounds.rAsVoid(GuiSounds.Type_o.OPEN);
      }

      this.e = false;
      this.y.aAsVoid(this.z == null ? 0.0 : 1.0);
      this.C.aAsVoid((double)this.U);
      this.MAsVoid();
      this.C.aAsVoid((double)this.U);
      if (this.z != null) {
         int var2 = this.P.P == null ? GuiWorkspace.y.settingScroll() : this.P.s;
         this.P.AAsVoid(this, this.F);
         this.P.iAsVoid(this.z);
         this.P.s = var2;
         this.y.aAsVoid(1.0);
      }

      this.sAsVoid();
      ClientLog.XAsVoid(
         "GUI",
         "ClickGUI opened, native {}x{} at GUI scale {}, workspace {}x{} / factor {} / {}",
         this.width,
         this.height,
         this.client.getWindow().getScaleFactor(),
         this.h.width(),
         this.h.height(),
         this.h.scale(),
         this.E
      );
   }

   private void sAsVoid() {
      String var1 = this.E;

      this.B = (SettingsPanel)(switch (var1) {
         case "Configs" -> {
            if (this.R == null) {
               this.R = new ProfileSettingsPanel(this);
            }

            yield this.R;
         }
         case "Settings" -> this.r;
         default -> null;
      });
      if (this.B != null) {
         this.B.AAsVoid(this, this.F);
      }

      if (this.m && this.B == this.R) {
         this.m = false;
         this.R.EAsVoid("Discover");
      }
   }

   private void fAsVoid(String var1) {
      if (!this.E.equals(var1)) {
         GuiSounds.rAsVoid(GuiSounds.Type_o.CATEGORY);
      }

      if (this.e) {
         this.z = null;
         this.e = false;
         this.y.aAsVoid(0.0);
      }

      if (this.B != null) {
         this.B.suspend();
      }

      this.P.suspend();
      this.E = var1;
      this.MAsVoid();
      this.sAsVoid();
      if (this.B == null && this.z != null) {
         this.P.AAsVoid(this, this.F);
         this.P.iAsVoid(this.z);
      }

      this.HAsVoid();
      ClientLog.XAsVoid("GUI", "Destination switched to {}", var1);
   }

   void LAsVoid() {
      this.r.TAsVoid("HUD");
      this.r.EAsVoid(null);
      this.r.s = 0;
      this.fAsVoid("Settings");
   }

   void eAsVoid() {
      if (this.client.player != null && this.client.world != null) {
         this.HAsVoid();
         this.client.setScreen(new HudEditorScreen(this));
      } else {
         this.f = "Join PetEffectRenderer world to edit the HUD.";
      }
   }

   void lAsVoid() {
      if (this.z != null && !this.e) {
         GuiSounds.rAsVoid(GuiSounds.Type_o.SETTINGS_CLOSE);
         this.e = true;
         this.P.fAsVoid();
         this.y.bAsDouble(0.0, 270);
      }
   }

   private void NAsVoid() {
      if (this.e && this.y.yAsDouble() == 0.0) {
         this.z = null;
         this.e = false;
         this.P.suspend();
      }
   }

   private void kAsVoid(GuiCategory var1) {
      boolean var2 = var1.LAsBoolean(this.V, () -> {
         if (this.E.equals("Modules")) {
            GuiSounds.rAsVoid(GuiSounds.Type_o.CATEGORY);
         }

         this.N.PAsVoid(220);
         ClientPreferences var2x = LarpClient.CAsLarpClient().fAsBo().yAsN7();
         if (var2x.H != (var1 == GuiCategory.FAVORITES)) {
            var2x.H = var1 == GuiCategory.FAVORITES;
            this.O = true;
            this.qAsBoolean();
         }

         this.V = var1;
         this.U = 0;
         this.C.aAsVoid(0.0);
         this.z = null;
         this.e = false;
         this.d = "";
         this.fAsVoid("Modules");
      });
      if (!var2 && !this.E.equals("Modules")) {
         this.fAsVoid("Modules");
      }
   }

   private void MAsVoid() {
      this.yAsVoid();
      this.D.clear();
      this.k.clear();
      this.X.clear();
      this.W.clear();

      for (GuiCategory var4 : GuiCategory.values()) {
         ActionButton var5 = new ActionButton(var4.J, () -> this.kAsVoid(var4));
         var5.a = null;
         var5.q = var4.n;
         var5.e = true;
         var5.W = true;
         this.D.add(var5);
         this.Q.add(var5);
      }

      for (String var9 : new String[]{"Editor", "Configs", "Settings"}) {
         ActionButton var10 = new ActionButton(var9, () -> {
            if (var9.equals("Editor")) {
               this.eAsVoid();
            } else {
               this.fAsVoid(var9);
            }
         });
         var10.a = null;
         var10.q = var9.equals("Editor") ? "panels-top-left" : (var9.equals("Configs") ? "folder" : "settings");
         var10.e = true;
         var10.W = true;
         this.D.add(var10);
         this.Q.add(var10);
      }

      this.S = new ActionButton("Account", () -> {
         this.r.TAsVoid("Account");
         this.fAsVoid("Settings");
      });
      this.Q.add(this.S);
      this.I = new ActionButton("Israel mode", this::UAsVoid);
      this.I.e = true;
      this.Q.add(this.I);
      this.u = new ActionButton("Larp Preset", this::GAsVoid);
      this.u.e = true;
      this.Q.add(this.u);
      this.l = new TextInputWidget("Search modules...", var1 -> {
         this.d = var1;
         this.U = 0;
         this.C.aAsVoid(0.0);
         this.PAsVoid();
      }, 256);
      this.l
         .AAsQq(var0 -> SkyShaderSettings.EAsList(nk.sAsList(LarpClient.CAsLarpClient().mAsF().NAsList(), LarpClient.CAsLarpClient().fAsBo().yAsN7()), var0));
      this.l.W = this.d;
      this.l.b = "search";
      this.Q.add(this.l);
      this.A = new ActionButton("", this::lAsVoid);
      this.A.q = "x";
      this.A.e = true;
      this.Q.add(this.A);
      this.A.a = null;
      this.PAsVoid();
   }

   private void PAsVoid() {
      this.q = false;
      int var1 = -1;
      int var2 = this.b != null && this.b.Q ? 1 : 0;
      byte var3 = 0;
      Module var4 = null;

      for (int var5 = 0; var5 < this.k.size(); var5++) {
         if (this.b == this.k.get(var5) || this.b == this.W.get(this.k.get(var5))) {
            var1 = var5;
            var4 = this.X.get(this.k.get(var5));
            var3 = (byte)(this.b == this.W.get(this.k.get(var5)) ? 1 : 0);
         }
      }

      if (this.k.contains(this.b) || this.W.containsValue(this.b)) {
         this.b.hAsVoid();
         this.b = null;
      }

      this.Q.removeAll(this.k);
      this.Q.removeAll(this.W.values());
      this.k.clear();
      this.X.clear();
      this.W.clear();
      ClientPreferences var10 = LarpClient.CAsLarpClient().fAsBo().yAsN7();

      for (Module var7 : this.V.nAsList(LarpClient.CAsLarpClient().mAsF().NAsList(), this.d, var10)) {
         ActionButton var8 = new ActionButton(var7.getDisplayName(), () -> {
            int var2x = this.z == null ? 1 : 0;
            int var3x = this.z != var7 ? 1 : 0;
            if (var3x != 0) {
               GuiSounds.rAsVoid(GuiSounds.Type_o.SETTINGS_OPEN);
            }

            this.e = false;
            this.z = var7;
            if (var2x != 0) {
               this.P.AAsVoid(this, this.F.moduleInspector(this.K));
            }

            if (var3x != 0) {
               this.P.iAsVoid(var7);
            }

            this.y.bAsDouble(1.0, 270);
            this.qAsVoid();
         });
         var8.a = null;
         var8.r = var8.t = true;
         var8.z = () -> {
            var7.toggle();
            GuiSounds.rAsVoid(var7.isEnabled() ? GuiSounds.Type_o.ON : GuiSounds.Type_o.OFF);
         };
         var8.g = var7.isEnabled();
         this.k.add(var8);
         this.X.put(var8, var7);
         FavoriteModuleWidget var9 = new FavoriteModuleWidget(() -> var10.yAsBoolean(var7.getName()), () -> {
            var10.sAsVoid(var7.getName(), !var10.yAsBoolean(var7.getName()));
            this.O = true;
            this.qAsBoolean();
            this.q = true;
            this.H = System.nanoTime() + 160000000L;
         });
         this.W.put(var8, var9);
         this.Q.add(var9);
         this.Q.add(var8);
      }

      this.qAsVoid();
      if (var1 >= 0 && var2 != 0 && !this.k.isEmpty()) {
         ActionButton var11 = this.k.get(Math.min(var1, this.k.size() - 1));

         for (ActionButton var13 : this.k) {
            if (this.X.get(var13) == var4) {
               var11 = var13;
               break;
            }
         }

         this.b = (GuiWidget)(var3 != 0 ? this.W.get(var11) : var11);
         this.b.y = this.b.Q = true;
         this.focusChanged(this.b);
      } else if (var1 >= 0 && var2 != 0) {
         this.b = this.l;
         this.b.y = this.b.Q = true;
      }
   }

   private boolean qAsBoolean() {
      LarpClient.CAsLarpClient().fAsBo().NAsVoid();
      this.O = false;
      return true;
   }

   private void UAsVoid() {
      LarpClient var1 = LarpClient.CAsLarpClient();
      ProfileManager var2 = var1.fAsBo();
      AppearanceRestoreJournal var3 = var2.yAsN7().v;
      boolean var4 = var2.GAsBoolean(!var3.uAsBoolean(), var1.mAsF().NAsList());
      if (!var4) {
         this.f = "Israel mode could not be changed. Click to retry; see the client log.";
         ClientLog.oAsVoid("GUI", "Israel mode toggle failed; local restore state retained for recovery");
      } else if (this.f.startsWith("Israel mode")) {
         this.f = "";
      }

      if (this.z != null && this.B == null) {
         this.P.RAsVoid();
      }
   }

   private void GAsVoid() {
      LarpClient var1 = LarpClient.CAsLarpClient();
      ProfileManager var2 = var1.fAsBo();
      if (!var2.CAsBoolean(!var2.yAsN7().E.RAsBoolean(), var1.mAsF().NAsList())) {
         this.f = "Larp Preset could not be changed. Click to retry; see the client log.";
         ClientLog.oAsVoid("GUI", "Larp Preset toggle failed; local restore state retained for recovery");
      } else if (this.f.startsWith("Larp Preset")) {
         this.f = "";
      }

      if (this.z != null && this.B == null) {
         this.P.RAsVoid();
      }
   }

   private Vector2f XAsVector2f(double var1, double var3) {
      return new Matrix3x2f(this.o).invert().transformPosition((float)var1, (float)var3, new Vector2f());
   }

   private Click WAsClick(Click var1) {
      Vector2f var2 = this.XAsVector2f(var1.x(), var1.y());
      return new Click((double)var2.x, (double)var2.y, var1.buttonInfo());
   }

   private SettingsPanel CAsP2() {
      return (SettingsPanel)(this.B != null ? this.B : (this.z != null ? this.P : null));
   }

   private boolean lAsBoolean() {
      return this.B == null && this.z != null && !this.e && !this.F.columns();
   }

   private int tAsInt() {
      return this.F.moduleListRight(this.K);
   }

   private double jAsDouble() {
      return this.F.detachedInspector() ? Math.clamp(this.K * 2.0 - 1.0, 0.0, 1.0) : this.K;
   }

   private int ZAsInt() {
      return this.F.detachedInspector() ? this.F.top() : this.F.bodyTop();
   }

   private int kAsInt() {
      return this.F.detachedInspector() ? this.F.top() + this.F.height() : this.F.bottom();
   }

   private void qAsVoid() {
      ClientPreferences var1 = LarpClient.CAsLarpClient().fAsBo().yAsN7();
      if (!this.w) {
         ScreenScale var2 = uAsY(this.client);
         if (!var2.equals(this.h)) {
            this.h = var2;
            ClientLog.XAsVoid(
               "GUI", "ClickGUI size {}%: workspace {}x{} / factor {}", Math.round(var1.JD * 100.0), this.h.width(), this.h.height(), this.h.scale()
            );
         }
      }

      double var9 = this.g.bAsDouble(this.J ? 0.0 : 1.0, var1.n);
      this.K = this.y.bAsDouble(this.E.equals("Modules") && this.z != null && !this.e ? 1.0 : 0.0, 270);
      this.F = (this.E.equals("Modules") ? xm.calculateModules(this.h.width(), this.h.height(), this.K) : xm.calculate(this.h.width(), this.h.height()))
         .offset(0, var1.A.equals("Slide") ? (int)Math.round((1.0 - var9) * 8.0) : 0);
      int var4 = this.F.navigationPitch(this.D.size());
      this.x = Math.clamp((long)this.x, 0, this.F.navigationScrollLimit(this.D.size()));
      int var5 = this.F.navigationTop() - this.x;

      for (int var6 = 0; var6 < this.D.size(); var6++) {
         if (var6 == GuiCategory.values().length) {
            var5 += this.F.navigationGap();
         }

         ActionButton var7 = this.D.get(var6);
         var7.bounds(this.F.left() + 4, var5, this.F.navigationRight() - this.F.left() - 4);
         var7.H = 1.0;
         var7.F = var4 - 1;
         var5 += var4;
         var7.i = this.E.equals("Modules") ? var7.X.equals(this.V.J) : var7.X.equals(this.E);
      }

      this.S.bounds(this.F.left() + 4, this.F.accountY(), this.F.navigationRight() - this.F.left() - 4);
      this.S.F = 30;
      this.I.bounds(this.F.presetLeft(), this.F.themeSwitchY(), this.F.presetWidth());
      this.I.F = this.F.themeSwitchHeight();
      this.I.g = LarpClient.CAsLarpClient().fAsBo().yAsN7().v.uAsBoolean();
      this.u.bounds(this.F.larpPresetLeft(), this.F.themeSwitchY(), this.F.presetWidth());
      this.u.F = this.F.themeSwitchHeight();
      this.u.g = LarpClient.CAsLarpClient().fAsBo().yAsN7().E.RAsBoolean();
      this.l.Y = this.E.equals("Modules");
      this.l.bounds(this.F.contentX(), this.F.top() + 12, this.F.right() - this.F.contentX());
      this.l.F = 26;
      this.v = this.N.yAsDouble();
      int var11 = Math.max(1, this.F.bottom() - this.F.bodyTop());
      this.U = Math.clamp((long)this.U, 0, Math.max(0, this.F.moduleContentHeight(this.k.size()) - var11));
      var5 = this.F.bodyTop() - (int)Math.round(this.C.bAsDouble((double)this.U, 150));

      for (int var12 = 0; var12 < this.k.size(); var12++) {
         ActionButton var8 = this.k.get(var12);
         var8.Y = this.E.equals("Modules") && !this.lAsBoolean();
         var8.bounds(this.F.moduleCardX(var12, this.v), var5 + var12 / this.F.moduleGridColumns() * 38, this.F.moduleCardWidth());
         var8.F = 34;
         this.W.get(var8).cAsVoid(var8);
      }

      this.A.Y = this.E.equals("Modules") && this.z != null;
      xm var13 = this.F.moduleInspector(this.K);
      this.A.bounds(var13.right() - 30, var13.bodyTop() + 1, 22);
      this.A.F = 22;
      if (this.B != null) {
         this.B.hAsVoid(this.F);
      } else if (this.z != null) {
         xm var14 = this.F.moduleInspector(this.K);
         this.P.uAsVoid(var14, this.F.moduleInspectorClipLeft(this.K), var14.right());
      }
   }

   public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
      long var5 = op.RAsLong();
      this.NAsVoid();
      this.qAsVoid();
      double var7 = this.g.yAsDouble();
      ClientPreferences var9 = LarpClient.CAsLarpClient().fAsBo().yAsN7();
      boolean var10 = WorldMenu.IAsBoolean(this);
      BloomQuality.iAsVoid(var9.o);
      if (!var10 && this.client.world != null && var9.s.equals("Everywhere")) {
         BloomQuality.nAsVoid();
      }

      if (!var10) {
         TAsVoid(context, this.width, this.height, var7, var9);
      }

      this.o
         .set(this.h.transform())
         .mul(xb.mAsMatrix3x2f(var9.A, var7, (double)this.F.left() + (double)this.F.width() / 2.0, (double)this.F.top() + (double)this.F.height() / 2.0));
      if (this.s != null) {
         aq.Type_x var11 = this.J
            ? this.s.AAsAq_Type_x()
            : this.s.XAsAq_Type_x(System.nanoTime(), this.client.getOverlay() == null && this.client.isWindowFocused(), com.larpclient.f.CAsBoolean());
         this.o
            .set(this.h.transform())
            .mul(var11.matrix((double)this.h.width() / 2.0, (double)this.h.height() / 2.0))
            .mul(xb.mAsMatrix3x2f(var9.A, var7, (double)this.F.left() + (double)this.F.width() / 2.0, (double)this.F.top() + (double)this.F.height() / 2.0));
      }

      Vector2f var117 = this.XAsVector2f((double)mouseX, (double)mouseY);
      mouseX = Math.round(var117.x);
      mouseY = Math.round(var117.y);
      context.getMatrices().pushMatrix();
      context.getMatrices().mul(this.o);

      try (
         bd.Type_e var12 = bd.jAsBd_Type_e(var9.M, var10 ? 1.0 : var9.G, var9.b, var9.pAsBd_Type_p());
         TextDrawing.Type_l var13 = TextDrawing.XAsV_Type_l();
         qg var14 = qg.nAsQg(xb.NAsDouble(var9.A, var7));
      ) {
         if (!var10 && bd.yAsBd_Type_p().accentGlow()) {
            GuiDrawing.fAsVoid(context, this.F.left(), this.F.top(), this.F.width(), this.F.height());
         }

         GuiDrawing.JAsVoid(context, this.F.left(), this.F.top(), this.F.width(), this.F.height());
         GuiDrawing.DAsVoid(context, this.F.sunkenX(), this.F.sunkenY(), this.F.sunkenWidth(), this.F.sunkenHeight());
         this.gAsVoid(context, this.CAsP2() != null && this.CAsP2().TAsBoolean() ? -1 : mouseX, mouseY);
         GuiDrawing.OAsVoid(context, this.F.left(), this.F.top(), this.F.width(), this.F.height());
         this.I.render(context, this.CAsP2() != null && this.CAsP2().TAsBoolean() ? -1 : mouseX, mouseY);
         this.u.render(context, this.CAsP2() != null && this.CAsP2().TAsBoolean() ? -1 : mouseX, mouseY);
         if (this.B != null) {
            this.B.render(context, mouseX, mouseY, deltaTicks);
         } else {
            this.l.render(context, mouseX, mouseY);
            context.enableScissor(this.F.contentX(), this.F.bodyTop(), this.tAsInt(), this.F.bottom());

            try (qg var15 = qg.nAsQg(0.35 + 0.65 * this.v)) {
               for (ActionButton var17 : this.k) {
                  Module var18 = this.X.get(var17);
                  var17.i = var18 == this.z;
                  var17.g = var18.isEnabled();
                  var17.E = this.d.isBlank() ? var18.getDescription() : GuiCategory.SAsAk(var18).J + " · " + var18.getDescription();
                  if (var17.x + var17.F > this.F.bodyTop() && var17.x < this.F.bottom()) {
                     int var19 = mouseY >= this.F.bodyTop() && mouseY < this.F.bottom() && mouseX >= this.F.contentX() && mouseX < this.tAsInt() ? mouseX : -1;
                     var17.render(context, var19, mouseY);
                     this.W.get(var17).render(context, var19, mouseY);
                  }
               }

               if (this.k.isEmpty() && !this.lAsBoolean()) {
                  String var122 = this.V == GuiCategory.FAVORITES && this.d.isBlank() ? "Star PetEffectRenderer module to add it here." : "No matches";
                  TextDrawing.UAsVoid(
                     context,
                     TextDrawing.HAsString(var122, this.tAsInt() - this.F.contentX() - 16),
                     this.F.moduleListLeft(this.v) + 8,
                     this.F.bodyTop() + 12,
                     bd.e
                  );
               }
            } finally {
               context.disableScissor();
            }

            if (!this.lAsBoolean()) {
               GuiDrawing.aAsVoid(
                  context,
                  this.F.moduleScrollbarX(this.K),
                  this.F.bodyTop(),
                  Math.max(1, this.F.bottom() - this.F.bodyTop()),
                  this.F.moduleContentHeight(this.k.size()),
                  (int)Math.round(this.C.yAsDouble())
               );
            }

            if (this.z != null) {
               xm var118 = this.F.moduleInspector(this.K);
               int var123 = this.F.columns() ? var118.inspectorX() : var118.contentX();
               double var126 = this.jAsDouble();
               if (this.F.detachedInspector()) {
                  try (qg var131 = qg.nAsQg(var126)) {
                     GuiDrawing.gAsVoid(context, var123, var118.top(), var118.right() - var123, var118.height());
                  }
               }

               context.enableScissor(this.F.moduleInspectorClipLeft(this.K), this.ZAsInt(), var118.right(), this.kAsInt());

               try (qg var132 = qg.nAsQg(var126)) {
                  if (!this.F.detachedInspector()) {
                     GuiDrawing.iAsVoid(context, var123, this.F.bodyTop(), var118.right() - var123, this.F.bottom() - this.F.bodyTop());
                  }

                  this.P.render(context, mouseX, mouseY, deltaTicks);
                  this.A.render(context, mouseX, mouseY);
               } finally {
                  context.disableScissor();
               }

               context.enableScissor(this.F.moduleInspectorClipLeft(this.K), var118.top() + 12, var118.right(), this.kAsInt());

               try (qg var133 = qg.nAsQg(var126)) {
                  this.P.MAsVoid(context, mouseX, mouseY);
               } finally {
                  context.disableScissor();
               }
            }

            for (ActionButton var124 : this.k) {
               FavoriteModuleWidget var127 = this.W.get(var124);
               boolean var129 = this.accepts(var127, (double)mouseX, (double)mouseY);
               if ((this.CAsP2() == null || !this.CAsP2().TAsBoolean())
                  && var127.Y
                  && (var129 || var127.y && var127.Q)
                  && var127.x >= this.F.bodyTop()
                  && var127.x + var127.F <= this.F.bottom()) {
                  var127.cAsVoid(context, this.h.width(), this.h.height());
                  break;
               }
            }
         }

         SettingsPanel var120 = this.CAsP2();
         String var125 = UAsString(var120, var120 == null ? "" : var120.VAsString(mouseX, mouseY));
         String var128 = LarpClient.CAsLarpClient().fAsBo().dAsString();
         if (!var128.isBlank()) {
            var125 = var128;
         }

         if (!this.f.isBlank()) {
            var125 = this.f;
         }

         if (var125.isBlank() && (this.I.contains((double)mouseX, (double)mouseY) || this.I.y && this.I.Q)) {
            var125 = "Temporary cosmetic preset. Off restores your previous choices.";
         }

         if (var125.isBlank() && (this.u.contains((double)mouseX, (double)mouseY) || this.u.y && this.u.Q)) {
            var125 = "Purple cosmetic preset. Off restores your previous choices; your theme stays saved.";
         }

         if (var125.isBlank()) {
            var125 = "Enter opens settings · Space toggles · Tab navigates · Right click resets PetEffectRenderer setting";
         }

         int var130 = this.F.profileLeft();
         String var134 = "Profile: " + LarpClient.CAsLarpClient().fAsBo().ZAsString();
         TextDrawing.NAsVoid(
            context, TextDrawing.zAsString(var134, this.F.profileWidth(), TextDrawing.Type_s.HELP), var130, this.F.profileY(), bd.e, TextDrawing.Type_s.HELP
         );
         TextDrawing.NAsVoid(
            context,
            TextDrawing.zAsString(var125, this.F.helpWidth(), TextDrawing.Type_s.HELP),
            this.F.helpLeft(),
            this.F.helpY(),
            this.f.isBlank() ? bd.e : -24915,
            TextDrawing.Type_s.HELP
         );
         double var121 = xb.cAsDouble(var9.A, var7);
         if (var121 > 0.01) {
            RoundedShapePipeline.nAsVoid(
               context, (float)this.F.left(), (float)this.F.top(), (float)this.F.width(), (float)this.F.height(), (float)bd.q, bg.XAsInt(-1, var121)
            );
         }

         if (var10) {
            WorldMenu.eAsVoid(context, this, this.F, mouseX, mouseY);
         }
      } finally {
         context.getMatrices().popMatrix();
      }

      HudRenderer.drawBuildWatermark(context);
      op.aAsVoid("ClickGUI", var5);
   }

   public double[] TAsDoubleArray2() {
      float var1 = this.h.scale();
      return new double[]{
         (double)((float)this.F.left() * var1),
         (double)((float)this.F.top() * var1),
         (double)((float)(this.F.left() + this.F.width()) * var1),
         (double)((float)(this.F.top() + this.F.height()) * var1)
      };
   }

   public Screen QAsScreen() {
      return this.n;
   }

   public boolean pAsBoolean() {
      return this.J && this.g.yAsDouble() <= 0.0;
   }

   public boolean pAsBoolean(double var1, double var3) {
      Vector2f var5 = this.XAsVector2f(var1, var3);
      if (var5.x >= (float)this.F.left()
         && var5.x < (float)(this.F.left() + this.F.width())
         && var5.y >= (float)this.F.top()
         && var5.y < (float)(this.F.top() + this.F.height())) {
         return true;
      } else {
         if (this.z != null && this.F.detachedInspector() && this.jAsDouble() > 0.2) {
            xm var6 = this.F.moduleInspector(this.K);
            if (var5.x >= (float)var6.inspectorX()
               && var5.x < (float)var6.right()
               && var5.y >= (float)var6.top()
               && var5.y < (float)(var6.top() + var6.height())) {
               return true;
            }
         }

         return WorldMenu.IAsBoolean(this.F, (double)var5.x, (double)var5.y);
      }
   }

   public boolean rAsBoolean() {
      SettingsPanel var1 = this.CAsP2();
      return PAsBoolean(this.b) || var1 != null && PAsBoolean(var1.YAsQb());
   }

   private static boolean PAsBoolean(GuiWidget var0) {
      if (var0 instanceof TextInputWidget var2 && var2.y || var0 instanceof SettingWidget var1 && var1.xAsBoolean()) {
         return true;
      }

      return false;
   }

   public void iAsVoid() {
      SettingsPanel var1 = this.CAsP2();
      GuiWidget var2 = var1 == null ? null : var1.YAsQb();
      if (var2 != null) {
         var2.key(256);
      }

      if (this.b != null && this.b != var2) {
         this.b.key(256);
      }

      this.bAsVoid();
   }

   public void bAsVoid() {
      SettingsPanel var1 = this.CAsP2();
      if (var1 != null) {
         var1.fAsVoid();
      }

      if (this.b != null) {
         this.b.hAsVoid();
      }

      this.b = this.t = null;
   }

   private static void TAsVoid(DrawContext var0, int var1, int var2, double var3, ClientPreferences var5) {
      int var6 = bg.XAsInt(-16777216, var5.JE);
      String var7 = var5.D;
      switch (var7) {
         case "None":
            break;
         case "Gradient":
            int var11 = bg.pAsInt(var6, var3);
            int var10 = bg.pAsInt(bg.xAsInt(var6, bg.XAsInt(bd.w, 0.5), 0.35), var3);
            GuiTextureDrawing.mAsVoid(var0, 0, 0, var1, var2, var11, var10);
            break;
         case "Aurora":
         case "Nebula":
         case "Waves":
            GuiAuroraRenderer.Type_d var9 = GuiAuroraRenderer.Type_d.valueOf(var5.D.toUpperCase(Locale.ROOT));
            if (!GuiAuroraRenderer.AAsBoolean(var0, var1, var2, var3, bd.w, bd.Q, var5.M, var5.Z, var9, var5.JE)) {
               GuiTextureDrawing.CAsVoid(var0, 0, 0, var1, var2, bg.pAsInt(var6, var3));
            }
            break;
         default:
            GuiTextureDrawing.CAsVoid(var0, 0, 0, var1, var2, bg.pAsInt(var6, var3));
      }
   }

   static String UAsString(SettingsPanel var0, String var1) {
      if (var0 instanceof ProfileSettingsPanel var2 && !var2.Z.isBlank()) {
         return var2.Z;
      }

      return var1;
   }

   private void gAsVoid(DrawContext var1, int var2, int var3) {
      int var4 = this.F.contentX() - this.F.left() - 10;
      var1.enableScissor(this.F.left(), this.F.top(), this.F.contentX() - 10, this.F.navigationTop());

      try {
         var1.drawTexture(
            RenderPipelines.GUI_TEXTURED,
            MenuLogoAssets.oAsIdentifier(),
            this.F.left() + 7,
            this.F.top() + 10,
            0.0F,
            0.0F,
            22,
            22,
            64,
            64,
            64,
            64,
            qg.iAsInt(-1)
         );

         try (qg var5 = qg.nAsQg(1.0)) {
            int var6 = this.F.left() + 35;
            int var7 = var4 - 39;
            TextDrawing.NAsVoid(
               var1, TextDrawing.zAsString("Larp Client", var7, TextDrawing.Type_s.BRAND), var6, this.F.top() + 10, bd.u, TextDrawing.Type_s.BRAND
            );
            int var9 = var6 + TextDrawing.jAsInt("Version ", TextDrawing.Type_s.HELP);
            TextDrawing.NAsVoid(var1, "Version ", var6, this.F.top() + 24, bd.W, TextDrawing.Type_s.HELP);
            TextDrawing.NAsVoid(
               var1,
               TextDrawing.zAsString(LarpClient.C, Math.max(1, var6 + var7 - var9), TextDrawing.Type_s.HELP),
               var9,
               this.F.top() + 24,
               bd.WAsInt(var9, bd.m),
               TextDrawing.Type_s.HELP
            );
            WAsVoid(var1, "FEATURES", this.F.left() + 12, this.F.navigationTop() - 12, this.F.navigationRight());
         }
      } finally {
         var1.disableScissor();
      }

      var1.enableScissor(this.F.left(), this.F.navigationTop(), this.F.contentX() - 10, this.F.navigationBottom());

      try {
         try (qg var49 = qg.nAsQg(1.0)) {
            WAsVoid(var1, "OTHERS", this.F.left() + 12, this.D.get(GuiCategory.values().length).x - 12, this.F.navigationRight());
         }

         this.JAsVoid(var1);

         for (ActionButton var52 : this.D) {
            var52.render(var1, var3 >= this.F.navigationTop() && var3 < this.F.navigationBottom() ? var2 : -1, var3);
         }
      } finally {
         var1.disableScissor();
      }

      if (this.F.navigationScrollLimit(this.D.size()) > 0) {
         GuiDrawing.aAsVoid(
            var1,
            this.F.contentX() - 12,
            this.F.navigationTop(),
            this.F.navigationBottom() - this.F.navigationTop(),
            this.D.size() * this.F.navigationPitch(this.D.size()) + this.F.navigationGap(),
            this.x
         );
      }

      var1.enableScissor(this.F.left(), this.F.accountY(), this.F.contentX() - 10, this.F.accountY() + this.S.F);

      try {
         double var51 = this.S.bAsDouble(var2, var3);
         if (var51 > 0.001) {
            GuiTextureDrawing.AAsVoid(var1, (float)this.S.k, (float)this.S.x, (float)this.S.V, (float)this.S.F, (float)bd.l, bg.XAsInt(bd.u, var51 * 0.045));
         }

         DiscordPresenceStatus var53 = DiscordPresenceStatus.current();
         if (!TextLayout.CAsBoolean(var1, this.F.left() + 8, this.S.x + 5, 20)) {
            this.i.fAsVoid(var1, this.F.left() + 8, this.S.x + 5, 20);
         }

         GuiTextureDrawing.RAsVoid(var1, this.F.left() + 25, this.S.x + 24, 4, bd.m);
         GuiTextureDrawing.RAsVoid(var1, this.F.left() + 25, this.S.x + 24, 3, var53.state().WAsInt());

         try (qg var54 = qg.nAsQg(1.0)) {
            int var55 = Math.max(1, var4 - 40);
            String var10 = var53.account() != null ? var53.account().name() : this.client.getSession().getUsername();
            TextDrawing.TAsVoid(
               var1, TextDrawing.mAsString(var10, var55, TextDrawing.Type_x.MEDIUM), this.F.left() + 34, this.S.x + 6, bd.u, TextDrawing.Type_x.MEDIUM
            );
            TextDrawing.NAsVoid(
               var1,
               TextDrawing.zAsString(var53.state().TAsString(), var55, TextDrawing.Type_s.HELP),
               this.F.left() + 34,
               this.S.x + 18,
               bd.e,
               TextDrawing.Type_s.HELP
            );
         }

         if (this.S.y && this.S.Q) {
            GuiDrawing.dAsVoid(var1, this.S.k, this.S.x, this.S.V, this.S.F, bd.l);
         }
      } finally {
         var1.disableScissor();
      }
   }

   private static void WAsVoid(DrawContext var0, String var1, int var2, int var3, int var4) {
      TextDrawing.NAsVoid(var0, var1, var2, var3, bg.XAsInt(bd.e, 0.8), TextDrawing.Type_s.EYEBROW);
   }

   private void JAsVoid(DrawContext var1) {
      ActionButton var2 = null;

      for (ActionButton var4 : this.D) {
         if (var4.i) {
            var2 = var4;
            break;
         }
      }

      double var9 = this.G.bAsDouble(var2 != null ? 1.0 : 0.0, 180);
      if (var2 != null) {
         int var5 = var2.x - this.F.navigationTop() + this.x;
         if (!this.M) {
            this.Z.aAsVoid((double)var5);
            this.M = true;
         }

         this.Z.bAsDouble((double)var5, 200);
      }

      if (this.M && !(var9 < 0.01)) {
         ActionButton var10 = this.D.getFirst();
         float var6 = (float)(this.F.navigationTop() - this.x) + (float)this.Z.yAsDouble();
         int var7 = bd.WAsInt(var10.k, bd.m);
         int var8 = bd.l;
         GuiDrawing.HAsVoid(var1, (float)var10.k, var6, (float)var10.V, (float)var10.F, (float)var8, 9.0F, bg.XAsInt(var7, 0.22 * var9));
         RoundedShapePipeline.gAsVoid(
            var1, (float)var10.k, var6, (float)var10.V, (float)var10.F, (float)var8, bg.XAsInt(var7, 0.26 * var9), bg.XAsInt(bd.Q, 0.08 * var9)
         );
         RoundedShapePipeline.SAsVoid(
            var1, (float)var10.k, var6, (float)var10.V, (float)var10.F, (float)var8, bg.XAsInt(var7, 0.35 * var9), bg.XAsInt(var7, 0.35 * var9)
         );
      }
   }

   @Override
   protected boolean accepts(GuiWidget var1, double var2, double var4) {
      if (!super.accepts(var1, var2, var4)) {
         return false;
      } else if (this.D.contains(var1)) {
         return var4 >= (double)this.F.navigationTop() && var4 < (double)this.F.navigationBottom();
      } else {
         return !this.k.contains(var1) && !this.W.containsValue(var1)
            ? true
            : !this.lAsBoolean()
               && var4 >= (double)this.F.bodyTop()
               && var4 < (double)this.F.bottom()
               && var2 >= (double)this.F.contentX()
               && var2 < (double)this.tAsInt();
      }
   }

   @Override
   public boolean mouseClicked(Click click, boolean doubled) {
      if (this.J) {
         return true;
      } else {
         this.w = true;
         click = this.WAsClick(click);
         this.qAsVoid();
         SettingsPanel var3 = this.CAsP2();
         GuiWidget var4 = var3 == null ? null : var3.YAsQb();
         if (var4 != null && var4.pAsBoolean(click.button())) {
            return true;
         } else if (click.button() > 1 && LarpClient.CAsLarpClient().kAsInt() == KeybindNames.ZAsInt(click.button())) {
            this.close();
            return true;
         } else if (var3 != null && var3.TAsBoolean()) {
            return var3.mouseClicked(click, doubled);
         } else if (WorldMenu.jAsBoolean(this, click.x(), click.y(), click.button(), this.F)) {
            if (var3 != null) {
               var3.fAsVoid();
            }

            return true;
         } else {
            for (ActionButton var6 : this.D) {
               if (this.accepts(var6, click.x(), click.y())) {
                  if (var6.i && this.E.equals("Modules")) {
                     return var6.click(click.x(), click.y(), click.button());
                  }

                  return super.mouseClicked(click, doubled);
               }
            }

            if (this.jAsDouble() > 0.2 && this.A.contains(click.x(), click.y()) && click.x() < (double)this.F.moduleInspector(this.K).right()) {
               return super.mouseClicked(click, doubled);
            } else {
               int var8 = this.B != null
                  ? (this.F.containsWorkspace(click.x(), click.y()) ? 1 : 0)
                  : (
                     this.jAsDouble() > 0.2
                           && (!this.e || this.F.columns())
                           && click.x() >= (double)this.F.moduleInspectorClipLeft(this.K)
                           && click.x() < (double)this.F.moduleInspector(this.K).right()
                           && click.y() >= (double)this.ZAsInt()
                           && click.y() < (double)this.kAsInt()
                        ? 1
                        : 0
                  );
               if (var3 != null && var8 != 0) {
                  if (this.b != null) {
                     this.b.hAsVoid();
                  }

                  this.b = this.t = null;
                  return this.e && this.B == null ? true : var3.mouseClicked(click, doubled);
               } else {
                  if (var3 != null) {
                     var3.fAsVoid();
                  }

                  return super.mouseClicked(click, doubled);
               }
            }
         }
      }
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      if (this.J) {
         return true;
      } else {
         Vector2f var9 = this.XAsVector2f(mouseX, mouseY);
         mouseX = (double)var9.x;
         mouseY = (double)var9.y;
         this.qAsVoid();
         SettingsPanel var10 = this.CAsP2();
         if ((var10 == null || !var10.TAsBoolean())
            && this.F.containsNavigation(mouseX, mouseY)
            && mouseY >= (double)this.F.navigationTop()
            && mouseY < (double)this.F.navigationBottom()) {
            this.x -= (int)(verticalAmount * 28.0);
            this.qAsVoid();
            return true;
         } else if (var10 != null
            && !this.e
            && (!this.F.detachedInspector() || this.jAsDouble() > 0.2)
            && var10.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)) {
            return true;
         } else if (this.E.equals("Modules")
            && !this.lAsBoolean()
            && mouseX >= (double)this.F.contentX()
            && mouseX < (double)this.tAsInt()
            && mouseY >= (double)this.F.bodyTop()
            && mouseY < (double)this.F.bottom()) {
            this.U -= (int)(verticalAmount * 27.0);
            this.qAsVoid();
            return true;
         } else {
            return false;
         }
      }
   }

   @Override
   protected void focusChanged(GuiWidget var1) {
      if (this.D.contains(var1)) {
         if (var1.x < this.F.navigationTop()) {
            this.x = this.x - (this.F.navigationTop() - var1.x);
         } else if (var1.x + var1.F > this.F.navigationBottom()) {
            this.x = this.x + (var1.x + var1.F - this.F.navigationBottom());
         }

         this.qAsVoid();
      }

      if (this.k.contains(var1) || this.W.containsValue(var1)) {
         for (Entry var3 : this.W.entrySet()) {
            if (var3.getValue() == var1) {
               var1 = (GuiWidget)var3.getKey();
               break;
            }
         }

         if (var1.x < this.F.bodyTop()) {
            this.U = this.U - (this.F.bodyTop() - var1.x);
         } else if (var1.x + var1.F > this.F.bottom()) {
            this.U = this.U + (var1.x + var1.F - this.F.bottom());
         }

         this.C.aAsVoid((double)this.U);
         this.qAsVoid();
      }
   }

   static boolean JAsBoolean(int var0, GuiWidget var1, GuiWidget var2, Runnable var3) {
      if (var0 != 256) {
         return false;
      } else {
         if (var1 != null) {
            var1.key(var0);
         }

         if (var2 != null && var2 != var1) {
            var2.key(var0);
         }

         var3.run();
         return true;
      }
   }

   @Override
   public boolean keyPressed(KeyInput input) {
      if (this.J) {
         return true;
      } else {
         SettingsPanel var2 = this.CAsP2();
         if (JAsBoolean(input.key(), var2 == null ? null : var2.YAsQb(), this.b, this::close)) {
            return true;
         } else if (this.l != null
            && this.E.equals("Modules")
            && this.b == this.l
            && (var2 == null || !var2.TAsBoolean())
            && input.key() == 258
            && (input.modifiers() & 1) == 0
            && this.l.yAsBoolean()) {
            return true;
         } else if (input.key() != 258 || var2 != null && var2.TAsBoolean()) {
            return var2 != null && (var2.YAsQb() != null || var2.TAsBoolean()) ? var2.keyPressed(input) : super.keyPressed(input);
         } else {
            ArrayList var3 = new ArrayList();
            this.Q.stream().filter(var0 -> var0.Y && var0.B).forEach(var3::add);
            if (var2 != null && !this.e && (!this.F.detachedInspector() || this.jAsDouble() > 0.2)) {
               var3.addAll(var2.qAsList());
            }

            GuiWidget var4 = var2 != null && var2.YAsQb() != null ? var2.YAsQb() : this.b;
            int var5 = (input.modifiers() & 1) != 0 ? -1 : 1;
            int var6 = var4 == null ? (var5 > 0 ? -1 : 0) : var3.indexOf(var4);
            if (this.b != null) {
               this.b.hAsVoid();
            }

            this.b = null;
            if (var2 != null) {
               var2.fAsVoid();
            }

            if (!var3.isEmpty()) {
               GuiWidget var7 = (GuiWidget)var3.get(Math.floorMod(var6 + var5, var3.size()));
               if (this.Q.contains(var7)) {
                  this.b = var7;
                  var7.y = var7.Q = true;
                  this.focusChanged(var7);
               } else {
                  var2.FAsVoid(var7);
               }
            }

            return true;
         }
      }
   }

   @Override
   public boolean charTyped(CharInput input) {
      if (this.J) {
         return true;
      } else {
         SettingsPanel var2 = this.CAsP2();
         return var2 != null && var2.YAsQb() != null ? var2.charTyped(input) : super.charTyped(input);
      }
   }

   @Override
   public boolean mouseDragged(Click click, double offsetX, double offsetY) {
      if (!this.J && !this.e) {
         Vector2f var6 = this.XAsVector2f(click.x(), click.y());
         Vector2f var7 = this.XAsVector2f(click.x() - offsetX, click.y() - offsetY);
         click = this.WAsClick(click);
         offsetX = (double)(var6.x - var7.x);
         offsetY = (double)(var6.y - var7.y);
         SettingsPanel var8 = this.CAsP2();
         return var8 != null && var8.mouseDragged(click, offsetX, offsetY) || super.mouseDragged(click, offsetX, offsetY);
      } else {
         return true;
      }
   }

   @Override
   public boolean mouseReleased(Click click) {
      this.w = false;
      click = this.WAsClick(click);
      if (this.CAsP2() != null) {
         this.CAsP2().mouseReleased(click);
      }

      return super.mouseReleased(click);
   }

   public void tick() {
      this.NAsVoid();
      if (this.q && System.nanoTime() >= this.H) {
         this.q = false;
         this.PAsVoid();
      }

      if (!this.J && this.CAsP2() != null) {
         this.CAsP2().tick();
      }
   }

   void HAsVoid() {
      if (this.V != GuiCategory.FAVORITES) {
         GuiWorkspace.I = this.V.name();
      }

      GuiWorkspace.y = new GuiWorkspace.Type_e(this.E, this.z == null ? "" : this.z.getName(), this.d, this.U, this.P.s);
   }

   public void removed() {
      if (this.s != null && !this.J) {
         this.s.OAsVoid();
      }

      if (this.B != null) {
         this.B.suspend();
      }

      this.P.suspend();
      this.HAsVoid();
      ProfileManager var1 = LarpClient.CAsLarpClient().fAsBo();
      if (this.O) {
         this.qAsBoolean();
      }

      var1.QAsVoid();
      ClientLog.XAsVoid("GUI", "ClickGUI closed; atomic preference save queued without blocking input");
   }
}
