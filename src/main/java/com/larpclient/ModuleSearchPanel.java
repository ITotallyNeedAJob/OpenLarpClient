package com.larpclient;

import com.larpclient.module.FakeSpawner;
import com.larpclient.module.HUD;
import com.larpclient.module.KeybindSetting;
import com.larpclient.module.Module;
import com.larpclient.module.NumberSetting;
import com.larpclient.module.Setting;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import org.joml.Matrix3x2f;
import org.joml.Vector2f;

public final class ModuleSearchPanel extends GuiPanel {
   static final int P = 8;
   static final int DropletTrail = 6;
   static final int H = 20;
   static final int ScreenScale = 14;
   static final int X = 36;
   static final int v = 46;
   static final int Y = 20;
   static final int HexEdgeloop = 18;
   static final int SkyChunkCache = 90;
   static final int E = 122;
   static final int J = 160;
   private static final int Z = -401337324;
   private static final int z = -16119286;
   private final Screen I;
   private final List<ModuleSearchPanel.Type_z> AuctionNotifier = new ArrayList<>();
   private final w2 q = new w2(0.0);
   private final Matrix3x2f r = new Matrix3x2f();
   private ScreenScale S;
   private TextInputWidget A;
   private ModuleSearchPanel.Type_w U;
   private ModuleSearchPanel.Type_w k;
   private ModuleSearchPanel.Type_x w;
   private String PriceDetector = "";
   private String K = "";
   private int N;
   private int a;
   private long V;
   private ModuleSearchPanel.Type_z O;
   private int n;
   private int i;
   private boolean L;
   private boolean T;
   private boolean j;

   public static Screen MAsScreen(Screen var0) {
      return (Screen)(ZAsD2().R ? new ModuleSearchPanel(var0) : new ClickGuiScreen(var0));
   }

   public static boolean lAsBoolean(Screen var0) {
      return var0 instanceof ClickGuiScreen || var0 instanceof ModuleSearchPanel;
   }

   private static PanelLayoutCodec ZAsD2() {
      return LarpClient.CAsLarpClient().fAsBo().yAsN7().y;
   }

   public ModuleSearchPanel(Screen var1) {
      super("Larp client");
      this.I = var1;
   }

   static int GAsInt(int var0, int var1) {
      int var2 = Math.max(1, var1);
      return Math.clamp((long)((var0 - 16 - (var2 - 1) * 6) / var2), 90, 122);
   }

   static int[] lAsIntArray(int var0, int var1, int var2) {
      int var3 = Math.max(1, (var2 - 16 + 6) / (var1 + 6));
      return new int[]{8 + var0 % var3 * (var1 + 6), 36 + var0 / var3 * 160};
   }

   static int[] IAsIntArray(int var0, int var1, int var2, int var3, int var4) {
      return new int[]{Math.clamp((long)var0, 24 - var2, Math.max(24 - var2, var3 - 24)), Math.clamp((long)var1, 0, Math.max(0, var4 - 20))};
   }

   protected void init() {
      this.S = this.NAsY();
      this.r.set(this.S.transform());
      this.L = false;
      if (!this.T) {
         this.T = true;
         this.q.aAsVoid(0.0);
         GuiSounds.rAsVoid(GuiSounds.Type_o.OPEN);
      }

      this.q.bAsDouble(1.0, this.YAsInt());
      if (this.o.isEmpty()) {
         this.gAsVoid();
      }

      this.NAsVoid();
      String var1 = this.A == null ? null : this.A.W;
      this.A = new TextInputWidget("Search modules...", var1x -> {
         this.m = var1x;

         for (ModuleSearchPanel.Type_z var3 : this.o) {
            var3.dAsVoid();
            var3.w = 0;
         }
      }, 64);
      this.A.b = "search";
      this.A.W = var1 == null ? this.m : var1;
      this.U = new ModuleSearchPanel.Type_w(() -> "Settings", this::RAsVoid, true);
      this.k = new ModuleSearchPanel.Type_w(() -> "Reset layout", this::YAsVoid, true);
      this.w = new ModuleSearchPanel.Type_x(this::jAsVoid);
      this.WAsVoid();
      ClientLog.XAsVoid("GUI", "Legacy ClickGUI opened, {} panels, workspace {}x{}", this.o.size(), this.S.width(), this.S.height());
   }

   private ScreenScale NAsY() {
      return com.larpclient.y.from(this.client.getWindow(), LarpClient.CAsLarpClient().fAsBo().yAsN7().JD);
   }

   private int YAsInt() {
      return LarpClient.CAsLarpClient().fAsBo().yAsN7().n;
   }

   private void gAsVoid() {
      this.o.clear();

      for (GuiCategory var4 : GuiCategory.values()) {
         if (var4 != GuiCategory.FAVORITES) {
            this.o.add(new ModuleSearchPanel.Type_z(var4));
         }
      }

      this.o.add(new ModuleSearchPanel.Type_z(GuiCategory.FAVORITES));
   }

   private void NAsVoid() {
      ArrayList<ModuleSearchPanel.Type_z> var1 = new ArrayList<>(this.o);
      var1.sort(Comparator.comparingInt(var0 -> var0.I));
      int var2 = this.NAsY().width();
      int var3 = GAsInt(var2, var1.size());

      for (int var4 = 0; var4 < var1.size(); var4++) {
         ModuleSearchPanel.Type_z var5 = var1.get(var4);
         PanelLayoutCodec.Type_e var6 = ZAsD2().TAsD2_Type_e(var5.e.name());
         int[] var7 = var6 == null ? lAsIntArray(var4, var3, var2) : new int[]{var6.x(), var6.y()};
         var5.a = var7[0];
         var5.X = var7[1];
         var5.j = var6 == null || var6.open();
         var5.dAsVoid();
      }
   }

   private void YAsVoid() {
      ZAsD2().SAsVoid();
      this.NAsVoid();
      this.j = true;
      this.pAsVoid();
      GuiSounds.rAsVoid(GuiSounds.Type_o.CLICK);
      ClientLog.GAsVoid("GUI", "Legacy ClickGUI panel layout reset to the default row");
   }

   private void jAsVoid() {
      if (ZAsD2().R) {
         ZAsD2().R = false;
         this.V = System.nanoTime();
         this.j = true;
         this.pAsVoid();
         GuiSounds.rAsVoid(GuiSounds.Type_o.OFF);
         ClientLog.GAsVoid("GUI", "Legacy ClickGUI disabled from the footer switch");
      }
   }

   private void RAsVoid() {
      this.sAsVoid();
      this.client.setScreen(ClickGuiScreen.uAsPn(this, "Interface"));
   }

   private void SAsVoid(Module var1) {
      this.sAsVoid();
      GuiWorkspace.I = GuiCategory.SAsAk(var1).name();
      GuiWorkspace.y = new GuiWorkspace.Type_e("Modules", var1.getName(), "", 0, 0);
      this.client.setScreen(new ClickGuiScreen(this));
   }

   private void yAsVoid(ModuleSearchPanel.Type_z var1) {
      try {
         ZAsD2().VAsVoid(var1.e.name(), new PanelLayoutCodec.Type_e(var1.a, var1.X, var1.j));
      } catch (IllegalArgumentException var3) {
         ClientLog.oAsVoid("GUI", "Legacy panel {} place not saved: {}", var1.e.name(), var3.getMessage());
      }

      this.j = true;
   }

   private void pAsVoid() {
      if (this.j) {
         LarpClient.CAsLarpClient().fAsBo().NAsVoid();
         this.j = false;
      }
   }

   public void close() {
      if (!this.L) {
         this.L = true;
         this.sAsVoid();
         GuiSounds.rAsVoid(GuiSounds.Type_o.CLOSE);
         this.client.setScreen(this.I);
      }
   }

   public void removed() {
      this.sAsVoid();
      this.pAsVoid();
      LarpClient.CAsLarpClient().fAsBo().QAsVoid();
      ClientLog.XAsVoid("GUI", "Legacy ClickGUI closed; preference and profile saves queued");
   }

   public void tick() {
      int var1 = this.V != 0L && System.nanoTime() - this.V < 100000000L ? 1 : 0;
      if (!this.L && var1 == 0 && !ZAsD2().R) {
         this.L = true;
         ClientLog.GAsVoid("GUI", "Legacy ClickGUI turned off; opening the workspace ClickGUI");
         this.client.setScreen(new ClickGuiScreen(this.I));
      } else {
         for (ModuleSearchPanel.Type_z var3 : this.o) {
            for (ModuleSearchPanel.Type_j var5 : var3.Q) {
               var5.EAsVoid();
            }
         }
      }
   }

   private void sAsVoid() {
      if (this.b != null) {
         this.b.hAsVoid();
      }

      this.b = this.t = null;
   }

   private void WAsVoid() {
      ScreenScale var1 = this.NAsY();
      if (!var1.equals(this.S) && this.O == null && this.t == null) {
         this.S = var1;
      }

      int var2 = GAsInt(this.S.width(), this.o.size());
      this.A.bounds(this.S.width() / 2 - 95, 8, 190);
      this.A.F = 20;
      this.U.bounds(this.A.k + this.A.V + 6, 8, 58);
      this.U.F = 20;
      this.k.bounds(this.U.k + this.U.V + 4, 8, 72);
      this.k.F = 20;
      this.K = KeybindNames.eAsString(LarpClient.CAsLarpClient().kAsInt());
      this.a = TextDrawing.mAsInt(this.K, TextDrawing.Type_x.SEMIBOLD) + TextDrawing.mAsInt(" = menu", TextDrawing.Type_x.SEMIBOLD) + 22;
      int var3 = this.w.dAsInt();
      this.N = (this.S.width() - this.a - 6 - var3) / 2;
      this.w.bounds(this.N + this.a + 6, this.VAsInt(), var3);
      this.w.F = 18;
      this.Q.clear();
      this.Q.add(this.A);
      this.Q.add(this.U);
      this.Q.add(this.k);
      this.Q.add(this.w);

      for (ModuleSearchPanel.Type_z var5 : this.o) {
         int[] var6 = IAsIntArray(var5.a, var5.X, var5.y, this.S.width(), this.S.height());
         if (var5 != this.O) {
            var5.a = var6[0];
            var5.X = var6[1];
         }

         var5.YAsVoid(var2, this.S.height());
         if (var5.QAsBoolean()) {
            for (ModuleSearchPanel.Type_j var8 : var5.Q) {
               if (var8.M) {
                  for (GuiWidget var10 : var8.y) {
                     if (var10.Y) {
                        this.Q.add(var10);
                     }
                  }
               }
            }
         }
      }
   }

   private Vector2f PAsVector2f(double var1, double var3) {
      return new Matrix3x2f(this.r).invert().transformPosition((float)var1, (float)var3, new Vector2f());
   }

   public void renderBackground(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
      if (this.client.world == null) {
         super.renderBackground(context, mouseX, mouseY, deltaTicks);
      }
   }

   public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
      long var5 = op.RAsLong();
      this.WAsVoid();
      ClientPreferences var7 = LarpClient.CAsLarpClient().fAsBo().yAsN7();
      double var8 = this.q.bAsDouble(this.L ? 0.0 : 1.0, var7.n);
      this.r.set(this.S.transform()).mul(xb.mAsMatrix3x2f(var7.A, var8, (double)this.S.width() / 2.0, (double)this.S.height() / 2.0));
      if (var7.A.equals("Slide")) {
         this.r.translate(0.0F, (float)((1.0 - var8) * 8.0));
      }

      Vector2f var10 = this.PAsVector2f((double)mouseX, (double)mouseY);
      mouseX = Math.round(var10.x);
      mouseY = Math.round(var10.y);
      context.getMatrices().pushMatrix();
      context.getMatrices().mul(this.r);

      try (qg var11 = qg.nAsQg(var8)) {
         int var12 = ZAsD2().b;
         this.A.render(context, mouseX, mouseY);
         this.U.render(context, mouseX, mouseY);
         this.k.render(context, mouseX, mouseY);
         ModuleSearchPanel.Type_z var13 = this.SAsP4_Type_z((double)mouseX, (double)mouseY);
         this.w.render(context, var13 == null ? mouseX : -1, mouseY);

         for (ModuleSearchPanel.Type_z var15 : this.o) {
            if (var15.QAsBoolean()) {
               var15.OAsVoid(context, var15 == var13 ? mouseX : -1, mouseY, var12);
            }
         }

         if (this.b instanceof SettingWidget var25 && var25.yAsBoolean()) {
            var25.oAsVoid(context, mouseX, mouseY);
         }

         String var26 = var13 != null
            ? var13.aAsString(mouseX, mouseY)
            : (this.w.contains((double)mouseX, (double)mouseY) ? "Turn off to switch back to the workspace ClickGUI" : null);
         this.bAsVoid(context, var26, var12);
      } finally {
         context.getMatrices().popMatrix();
      }

      op.aAsVoid("Legacy ClickGUI", var5);
   }

   private int VAsInt() {
      return this.S.height() - 46 + 20;
   }

   private void bAsVoid(DrawContext var1, String var2, int var3) {
      String var4 = var2 != null && !var2.isBlank() ? var2 : "Left click toggles · Right click opens settings · Middle click stars · Drag PetEffectRenderer title to move it";
      var4 = TextDrawing.HAsString(var4, this.S.width() - 40);
      int var5 = TextDrawing.UAsInt(var4);
      int var6 = this.VAsInt();
      TextDrawing.UAsVoid(var1, var4, (this.S.width() - var5) / 2, var6 - 13, bd.e);
      TextDrawing.Type_x var7 = TextDrawing.Type_x.SEMIBOLD;
      int var8 = this.N;
      int var9 = this.a;
      GuiTextureDrawing.AAsVoid(var1, (float)(var8 - 1), (float)(var6 - 1), (float)(var9 + 2), 20.0F, 10.0F, var3 | 0xFF000000);
      GuiTextureDrawing.AAsVoid(var1, (float)var8, (float)var6, (float)var9, 18.0F, 9.0F, -268106488);
      TextDrawing.TAsVoid(var1, this.K, var8 + 11, var6 + 5, mi.readableAccent(var3, -16448248), var7);
      TextDrawing.TAsVoid(var1, " = menu", var8 + 11 + TextDrawing.mAsInt(this.K, var7), var6 + 5, -1, var7);
   }

   private ModuleSearchPanel.Type_z SAsP4_Type_z(double var1, double var3) {
      for (int var5 = this.o.size() - 1; var5 >= 0; var5--) {
         if (this.o.get(var5).QAsBoolean() && this.o.get(var5).nAsBoolean(var1, var3)) {
            return this.o.get(var5);
         }
      }

      return null;
   }

   private void DAsVoid(ModuleSearchPanel.Type_z var1) {
      this.o.remove(var1);
      this.o.add(var1);
   }

   @Override
   public boolean mouseClicked(Click click, boolean doubled) {
      if (this.L) {
         return true;
      } else {
         Vector2f var3 = this.PAsVector2f(click.x(), click.y());
         double var4 = (double)var3.x;
         double var6 = (double)var3.y;
         int var8 = click.button();
         this.WAsVoid();
         if (this.b != null && this.b.pAsBoolean(var8)) {
            return true;
         } else if (var8 > 1 && LarpClient.CAsLarpClient().kAsInt() == KeybindNames.ZAsInt(var8)) {
            this.close();
            return true;
         } else {
            if (this.b instanceof SettingWidget var9 && var9.uAsBoolean(var4, var6)) {
               if (var9.oAsBoolean(var4, var6, var8)) {
                  this.t = var9;
               }

               return true;
            }

            ModuleSearchPanel.Type_z var12 = this.SAsP4_Type_z(var4, var6);
            if (var12 != null) {
               this.DAsVoid(var12);
               return var12.tAsBoolean(var4, var6, var8);
            } else {
               for (GuiWidget var11 : List.of(this.A, this.U, this.k, this.w)) {
                  if (var11.contains(var4, var6)) {
                     this.mAsVoid(var11);
                     var11.click(var4, var6, var8);
                     return true;
                  }
               }

               this.sAsVoid();
               return false;
            }
         }
      }
   }

   private void mAsVoid(GuiWidget var1) {
      if (this.b != null && this.b != var1) {
         this.b.hAsVoid();
      }

      this.b = var1;
      var1.y = true;
      var1.Q = false;
   }

   @Override
   public boolean mouseDragged(Click click, double offsetX, double offsetY) {
      if (this.L) {
         return true;
      } else {
         Vector2f var6 = this.PAsVector2f(click.x(), click.y());
         if (this.O != null) {
            int[] var7 = IAsIntArray(Math.round(var6.x) - this.n, Math.round(var6.y) - this.i, this.O.y, this.S.width(), this.S.height());
            this.O.a = var7[0];
            this.O.X = var7[1];
            return true;
         } else if (this.t != null) {
            this.t.drag((double)var6.x, (double)var6.y);
            return true;
         } else {
            return false;
         }
      }
   }

   @Override
   public boolean mouseReleased(Click click) {
      if (this.O != null) {
         this.yAsVoid(this.O);
         this.pAsVoid();
         ClientLog.XAsVoid("GUI", "Legacy panel {} moved to {}, {}", this.O.e.J, this.O.a, this.O.X);
         this.O = null;
      }

      if (this.t != null) {
         this.t.release();
      }

      this.t = null;
      return true;
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      if (this.L) {
         return true;
      } else {
         Vector2f var9 = this.PAsVector2f(mouseX, mouseY);
         if (this.b instanceof SettingWidget var10 && var10.uAsBoolean((double)var9.x, (double)var9.y)) {
            var10.OAsVoid(verticalAmount);
            return true;
         }

         ModuleSearchPanel.Type_z var12 = this.SAsP4_Type_z((double)var9.x, (double)var9.y);
         if (var12 == null) {
            return false;
         } else {
            var12.w = var12.w - (int)Math.round(verticalAmount * 20.0);
            this.WAsVoid();
            return true;
         }
      }
   }

   @Override
   public boolean keyPressed(KeyInput input) {
      if (this.L) {
         return true;
      } else {
         return ClickGuiScreen.JAsBoolean(input.key(), this.b, null, this::close) ? true : super.keyPressed(input);
      }
   }

   @Override
   public boolean charTyped(CharInput input) {
      if (this.L) {
         return true;
      } else if (super.charTyped(input)) {
         return true;
      } else if ((this.b == null || this.b == this.A) && Character.isLetterOrDigit(input.codepoint())) {
         this.mAsVoid(this.A);
         return this.A.character(input.codepoint());
      } else {
         return false;
      }
   }

   @Override
   protected void focusChanged(GuiWidget var1) {
      for (ModuleSearchPanel.Type_z var3 : this.o) {
         for (ModuleSearchPanel.Type_j var5 : var3.Q) {
            if (var5.M && var5.y.contains(var1)) {
               if (var1.x < var3.MAsInt()) {
                  var3.w = var3.w - (var3.MAsInt() - var1.x);
               } else if (var1.x + var1.F > var3.S) {
                  var3.w = var3.w + (var1.x + var1.F - var3.S);
               }

               this.WAsVoid();
               return;
            }
         }
      }
   }

   private final class Type_j {
      final Module C;
      final List<GuiWidget> ScreenScale = new ArrayList<>();
      final Map<GuiWidget, BooleanSupplier> Y = new IdentityHashMap<>();
      final Set<String> V = new HashSet<>();
      boolean M;
      int r;
      String A;

      Type_j(Module var2) {
         this.C = var2;
      }

      boolean PAsBoolean(GuiWidget var1) {
         BooleanSupplier var2 = this.Y.get(var1);
         return var2 == null || var2.getAsBoolean();
      }

      int rAsInt() {
         int var1 = 5;

         for (GuiWidget var3 : this.y) {
            if (var3.Y) {
               var1 += var3.F + 2;
            }
         }

         return var1;
      }

      void qAsVoid(int var1) {
         switch (var1) {
            case 0:
               this.C.toggle();
               GuiSounds.rAsVoid(this.C.isEnabled() ? GuiSounds.Type_o.ON : GuiSounds.Type_o.OFF);
               break;
            case 1:
               this.M = !this.M;
               if (this.M) {
                  this.rAsVoid();
               } else if (this.y.contains(ModuleSearchPanel.this.b)) {
                  ModuleSearchPanel.this.sAsVoid();
               }

               GuiSounds.rAsVoid(this.M ? GuiSounds.Type_o.SETTINGS_OPEN : GuiSounds.Type_o.SETTINGS_CLOSE);
               break;
            case 2:
               ClientPreferences var2 = LarpClient.CAsLarpClient().fAsBo().yAsN7();
               var2.sAsVoid(this.C.getName(), !var2.yAsBoolean(this.C.getName()));
               LarpClient.CAsLarpClient().fAsBo().NAsVoid();

               for (ModuleSearchPanel.Type_z var4 : ModuleSearchPanel.this.o) {
                  var4.dAsVoid();
               }

               GuiSounds.rAsVoid(GuiSounds.Type_o.CLICK);
         }
      }

      void EAsVoid() {
         if (this.M && this.C instanceof nk var1 && !Objects.equals(this.A, var1.style().getValue())) {
            this.rAsVoid();
         }
      }

      void rAsVoid() {
         if (this.y.contains(ModuleSearchPanel.this.b)) {
            ModuleSearchPanel.this.sAsVoid();
         }

         this.y.clear();
         this.Y.clear();
         this.A = this.C instanceof nk var1 ? var1.style().getValue() : null;
         List var8 = ControlSettings.FAsList(this.C);
         Iterator var9 = var8.iterator();

         while (true) {
            ControlSettings.Type_h var3;
            BooleanSupplier var4;
            do {
               if (!var9.hasNext()) {
                  if (!(this.C instanceof nk)) {
                     KeybindSetting var10 = new KeybindSetting(
                        this.C instanceof HUD ? "Toggle HUD" : "Bind",
                        "Press PetEffectRenderer key, or PetEffectRenderer middle or side mouse button. Escape/Delete clears the binding",
                        this.C.getKeyBind()
                     );
                     var10.onChanged(this.C::setKeyBind);
                     this.lAsVoid(new SettingWidget(var10), null);
                     if (this.C.keybindMode() != null) {
                        this.lAsVoid(new SettingWidget(this.C.keybindMode()), null);
                     }
                  }

                  this.lAsVoid(new ModuleSearchPanel.Type_w(() -> "Reset settings", () -> {
                     for (Setting var2 : this.C.getSettings()) {
                        var2.reset();
                     }

                     if (this.C instanceof FakeSpawner var3x) {
                        var3x.YAsVoid();
                     }

                     this.rAsVoid();
                     GuiSounds.rAsVoid(GuiSounds.Type_o.CLICK);
                  }, false), null);
                  this.lAsVoid(new ModuleSearchPanel.Type_w(() -> "Open full editor", () -> ModuleSearchPanel.this.SAsVoid(this.C), false), null);
                  return;
               }

               final ControlSettings.Type_h var3f = (ControlSettings.Type_h)var9.next();
               var3 = var3f;
               var4 = () -> var3f.settings().stream().anyMatch(Setting::isVisible);
               if (!var3.collapsed()) {
                  break;
               }

               String var5 = var3f.title().toLowerCase(Locale.ROOT);
               ModuleSearchPanel.Type_w var6 = new ModuleSearchPanel.Type_w(() -> (this.V.contains(var3f.title()) ? "Hide " : "Show ") + var5, () -> {
                  if (!this.V.remove(var3f.title())) {
                     this.V.add(var3f.title());
                  }

                  this.rAsVoid();
               }, false);
               this.lAsVoid(var6, var4);
            } while (!this.V.contains(var3.title()));

            if (var8.size() > 1) {
               this.lAsVoid(new ModuleSearchPanel.Type_u(var3.title()), var4);
            }

            for (Setting var12 : var3.settings()) {
               if (!(var12 instanceof NumberSetting var7) || !var7.rAsBoolean()) {
                  SettingWidget var13 = new SettingWidget(var12);
                  this.lAsVoid(var13, var13::vAsBoolean);
               }
            }
         }
      }

      private void lAsVoid(GuiWidget var1, BooleanSupplier var2) {
         this.y.add(var1);
         if (var2 != null) {
            this.Y.put(var1, var2);
         }
      }
   }

   private static final class Type_u extends GuiWidget {
      private final String t;

      Type_u(String var1) {
         this.t = var1;
         this.F = 12;
      }

      @Override
      public boolean contains(double var1, double var3) {
         return false;
      }

      @Override
      public void render(DrawContext var1, int var2, int var3) {
         if (this.Y) {
            TextDrawing.NAsVoid(
               var1,
               TextDrawing.zAsString(this.t.toUpperCase(Locale.ROOT), this.V - 4, TextDrawing.Type_s.EYEBROW),
               this.k + 2,
               this.x + 2,
               bd.W,
               TextDrawing.Type_s.EYEBROW
            );
         }
      }
   }

   private static final class Type_w extends GuiWidget {
      private final Supplier<String> W;
      private final Runnable H;
      private final boolean a;

      Type_w(Supplier<String> var1, Runnable var2, boolean var3) {
         this.W = var1;
         this.H = var2;
         this.a = var3;
         this.F = 14;
      }

      @Override
      public void render(DrawContext var1, int var2, int var3) {
         if (this.Y) {
            boolean var4 = this.contains((double)var2, (double)var3);
            int var5 = this.y && this.Q ? 1 : 0;
            if (this.a) {
               GuiTextureDrawing.CAsVoid(var1, this.k - 1, this.x - 1, this.V + 2, this.F + 2, -16119286);
               GuiTextureDrawing.CAsVoid(var1, this.k, this.x, this.V, this.F, var4 ? -266330076 : -401337324);
            } else if (var4) {
               GuiTextureDrawing.CAsVoid(var1, this.k, this.x, this.V, this.F, 419430399);
            }

            if (var5 != 0) {
               GuiTextureDrawing.pAsVoid(var1, this.k, this.x, this.V, this.F, bd.u);
            }

            String var6 = TextDrawing.HAsString(this.W.get(), this.V - 8);
            TextDrawing.UAsVoid(var1, var6, this.k + (this.V - TextDrawing.UAsInt(var6)) / 2, this.x + (this.F - 8) / 2, !var4 && !this.a ? bd.e : -1);
         }
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (var5 == 0 && this.contains(var1, var3)) {
            GuiSounds.rAsVoid(GuiSounds.Type_o.CLICK);
            this.H.run();
            return false;
         } else {
            return false;
         }
      }

      @Override
      public boolean key(int var1) {
         if (var1 != 257 && var1 != 335 && var1 != 32) {
            return false;
         } else {
            this.H.run();
            return true;
         }
      }
   }

   private static final class Type_x extends GuiWidget {
      private static final String C = "Legacy";
      private static final int SkyChunkCache = 18;
      private static final int q = 10;
      private final Runnable G;
      private final w2 HexEdgeloop = new w2(1.0);

      Type_x(Runnable var1) {
         this.G = var1;
         this.F = 18;
      }

      int dAsInt() {
         return 11 + TextDrawing.mAsInt("Legacy", TextDrawing.Type_x.SEMIBOLD) + 6 + 18 + 8;
      }

      @Override
      public void render(DrawContext var1, int var2, int var3) {
         boolean var4 = this.contains((double)var2, (double)var3);
         double var5 = this.p.bAsDouble(ModuleSearchPanel.ZAsD2().R ? 1.0 : 0.0, 100);
         GuiTextureDrawing.AAsVoid(
            var1, (float)(this.k - 1), (float)(this.x - 1), (float)(this.V + 2), (float)(this.F + 2), (float)(this.F / 2 + 1), var4 ? -12961216 : -14803422
         );
         GuiTextureDrawing.AAsVoid(var1, (float)this.k, (float)this.x, (float)this.V, (float)this.F, (float)(this.F / 2), -268106488);
         if (this.y && this.Q) {
            GuiTextureDrawing.pAsVoid(var1, this.k, this.x, this.V, this.F, bd.u);
         }

         TextDrawing.TAsVoid(var1, "Legacy", this.k + 11, this.x + 5, var4 ? -1 : bd.e, TextDrawing.Type_x.SEMIBOLD);
         int var7 = this.k + this.V - 8 - 18;
         int var8 = this.x + (this.F - 10) / 2;
         int var9 = ModuleSearchPanel.ZAsD2().b | 0xFF000000;
         GuiTextureDrawing.AAsVoid(var1, (float)var7, (float)var8, 18.0F, 10.0F, 5.0F, bg.xAsInt(-13420994, var9, var5));
         int var10 = var7 + 5 + (int)Math.round(8.0 * var5);
         GuiTextureDrawing.RAsVoid(var1, var10, var8 + 5, 3, -1);
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (var5 == 0 && this.contains(var1, var3)) {
            this.G.run();
         }

         return false;
      }

      @Override
      public boolean key(int var1) {
         if (var1 != 257 && var1 != 335 && var1 != 32) {
            return false;
         } else {
            this.G.run();
            return true;
         }
      }
   }

   private final class Type_z {
      final GuiCategory e;
      final Map<Module, ModuleSearchPanel.Type_j> n = new IdentityHashMap<>();
      final List<ModuleSearchPanel.Type_j> Q = new ArrayList<>();
      final w2 z = new w2(-1.0);
      final w2 F = new w2(0.0);
      final w2 MenuPopupProvider = new w2(0.0);
      final int I;
      int a;
      int X;
      int ScreenScale = 90;
      int w;
      int S;
      int Y;
      boolean TextLayout = true;

      Type_z(GuiCategory var2) {
         this.e = var2;
         this.I = ModuleSearchPanel.this.o.size();
      }

      void dAsVoid() {
         this.Q.clear();
         List var1 = LarpClient.CAsLarpClient().mAsF().NAsList();
         ClientPreferences var2 = LarpClient.CAsLarpClient().fAsBo().yAsN7();

         for (Module var4 : (Iterable<Module>) (this.e.nAsList(var1, "", var2))) {
            if (ModuleSearchPanel.this.m.isBlank() || SkyShaderSettings.GAsInt(var4, ModuleSearchPanel.this.m) != Integer.MAX_VALUE) {
               this.Q.add(this.n.computeIfAbsent(var4, var1x -> ModuleSearchPanel.this.new Type_j(var1x)));
            }
         }
      }

      boolean QAsBoolean() {
         return !this.Q.isEmpty();
      }

      int MAsInt() {
         return this.X + 20;
      }

      boolean nAsBoolean(double var1, double var3) {
         return GuiTextureDrawing.ZAsBoolean(var1, var3, this.a, this.X, this.y, 20 + (this.j ? this.S - this.MAsInt() : 0));
      }

      void YAsVoid(int var1, int var2) {
         boolean var3 = this.Q.stream().anyMatch(var0 -> var0.M);
         int var4 = var3 ? Math.max(var1, 160) : var1;
         if (this.z.yAsDouble() < 0.0) {
            this.z.aAsVoid((double)var4);
         }

         this.y = (int)Math.round(this.z.bAsDouble((double)var4, 200));
         this.Y = 0;

         for (ModuleSearchPanel.Type_j var6 : this.Q) {
            for (GuiWidget var8 : var6.y) {
               var8.Y = this.j && var6.M && var6.PAsBoolean(var8);
            }

            this.Y = this.Y + 14 + (var6.M ? var6.rAsInt() : 0);
         }

         int var12 = Math.max(14, var2 - 46 - this.MAsInt());
         this.S = this.MAsInt() + (this.j ? Math.min(this.Y, var12) : 0);
         this.w = Math.clamp((long)this.w, 0, Math.max(0, this.Y - (this.S - this.MAsInt())));
         int var13 = this.MAsInt() - this.w;

         for (ModuleSearchPanel.Type_j var16 : this.Q) {
            var16.r = var13;
            var13 += 14;
            if (var16.M) {
               var13 += 2;

               for (GuiWidget var10 : var16.y) {
                  if (var10.Y) {
                     var10.bounds(this.a + 4, var13, this.y - 8);
                     if (var10 instanceof SettingWidget var11) {
                        var11.fAsVoid(6, ModuleSearchPanel.this.S.height() - 40);
                     }

                     var13 += var10.F + 2;
                  }
               }

               var13 += 3;
            }
         }
      }

      void OAsVoid(DrawContext var1, int var2, int var3, int var4) {
         int var5 = 20 + this.S - this.MAsInt();
         GuiTextureDrawing.CAsVoid(var1, this.a - 1, this.X - 1, this.y + 2, var5 + 2, -16119286);
         GuiTextureDrawing.CAsVoid(var1, this.a, this.X, this.y, var5, -401337324);
         String var6 = TextDrawing.mAsString(this.e.J, (int)((float)(this.y - 10) / 1.2F), TextDrawing.Type_x.MEDIUM);
         var1.getMatrices().pushMatrix();

         try {
            var1.getMatrices().translate((float)this.a + (float)this.y / 2.0F, (float)(this.X + 5));
            var1.getMatrices().scale(1.2F, 1.2F);
            TextDrawing.TAsVoid(var1, var6, -TextDrawing.mAsInt(var6, TextDrawing.Type_x.MEDIUM) / 2, 0, -1, TextDrawing.Type_x.MEDIUM);
         } finally {
            var1.getMatrices().popMatrix();
         }

         if (this.j) {
            int var7 = var3 >= this.MAsInt() && var3 < this.S ? 1 : 0;
            ModuleSearchPanel.Type_j var8 = null;
            if (var7 != 0) {
               for (ModuleSearchPanel.Type_j var10 : this.Q) {
                  if (GuiTextureDrawing.ZAsBoolean((double)var2, (double)var3, this.a, var10.r, this.y, 14)) {
                     var8 = var10;
                     break;
                  }
               }
            }

            int var29 = this.MAsInt() - this.w;
            if (var8 != null) {
               if (this.f.yAsDouble() < 0.05) {
                  this.F.aAsVoid((double)(var8.r - var29));
               }

               this.F.bAsDouble((double)(var8.r - var29), 160);
            }

            double var30 = this.f.bAsDouble(var8 == null ? 0.0 : 1.0, 160);
            var1.enableScissor(this.a, this.MAsInt(), this.a + this.y, this.S);

            try {
               ClientPreferences var12 = LarpClient.CAsLarpClient().fAsBo().yAsN7();

               for (ModuleSearchPanel.Type_j var14 : this.Q) {
                  int var15 = var14.r + 14 + (var14.M ? var14.rAsInt() : 0);
                  if (var15 >= this.MAsInt() && var14.r <= this.S && var14.C.isEnabled()) {
                     GuiTextureDrawing.CAsVoid(var1, this.a, var14.r, this.y, 14, var4 | 0xFF000000);
                  }
               }

               if (var30 > 0.01) {
                  GuiTextureDrawing.CAsVoid(var1, this.a, var29 + (int)Math.round(this.F.yAsDouble()), this.y, 14, bg.pAsInt(419430399, var30));
               }

               for (ModuleSearchPanel.Type_j var34 : this.Q) {
                  int var36 = var34.r + 14 + (var34.M ? var34.rAsInt() : 0);
                  if (var36 >= this.MAsInt() && var34.r <= this.S) {
                     boolean var16 = var34.C.isEnabled();
                     int var17 = var16 ? mi.controlInk(var4 | 0xFF000000) : -1;
                     String var18 = TextDrawing.HAsString(var34.C.getDisplayName(), this.y - 14);
                     TextDrawing.UAsVoid(var1, var18, this.a + (this.y - TextDrawing.UAsInt(var18)) / 2, var34.r + 3, var17);
                     if (var12.yAsBoolean(var34.C.getName()) && this.e != GuiCategory.FAVORITES) {
                        GuiTextureDrawing.CAsVoid(var1, this.a + 3, var34.r + 5, 3, 3, var16 ? var17 : bg.XAsInt(var4, 0.9));
                     }

                     if (var34.M) {
                        int var19 = var34.rAsInt();
                        GuiTextureDrawing.CAsVoid(var1, this.a, var34.r + 14, this.y, var19, 1342177280);
                        GuiTextureDrawing.CAsVoid(var1, this.a, var34.r + 14, 1, var19, var4 | 0xFF000000);

                        for (GuiWidget var21 : var34.y) {
                           if (var21.Y) {
                              var21.render(var1, var7 != 0 ? var2 : -1, var3);
                           }
                        }
                     }
                  }
               }
            } finally {
               var1.disableScissor();
            }

            int var311 = this.S - this.MAsInt();
            if (this.Y > var311 && var311 > 0) {
               int var33 = Math.max(12, var311 * var311 / this.Y);
               int var35 = var311 - var33;
               int var37 = this.MAsInt() + (int)Math.round((double)var35 * ((double)this.w / (double)(this.Y - var311)));
               GuiTextureDrawing.CAsVoid(var1, this.a + this.y - 2, var37, 2, var33, bg.XAsInt(var4, 0.8));
            }
         }
      }

      String aAsString(int var1, int var2) {
         if (var2 < this.MAsInt()) {
            return "Drag to move · Right click folds " + this.e.J;
         } else {
            for (ModuleSearchPanel.Type_j var4 : this.Q) {
               if (GuiTextureDrawing.ZAsBoolean((double)var1, (double)var2, this.a, var4.r, this.y, 14)) {
                  String var8 = var4.C.getDescription();
                  return var8.isBlank() ? var4.C.getDisplayName() : var8;
               }

               if (var4.M) {
                  for (GuiWidget var7 : var4.y) {
                     if (var7.Y && var7.contains((double)var1, (double)var2)) {
                        return var7 instanceof SettingWidget var5 ? var5.SAsString() : "";
                     }
                  }
               }
            }

            return "";
         }
      }

      boolean tAsBoolean(double var1, double var3, int var5) {
         if (var3 < (double)this.MAsInt()) {
            if (var5 == 0) {
               ModuleSearchPanel.this.sAsVoid();
               ModuleSearchPanel.this.O = this;
               ModuleSearchPanel.this.n = (int)Math.round(var1) - this.a;
               ModuleSearchPanel.this.i = (int)Math.round(var3) - this.X;
            } else if (var5 == 1) {
               this.j = !this.j;
               ModuleSearchPanel.this.yAsVoid(this);
               ModuleSearchPanel.this.pAsVoid();
               GuiSounds.rAsVoid(this.j ? GuiSounds.Type_o.SETTINGS_OPEN : GuiSounds.Type_o.SETTINGS_CLOSE);
            }

            return true;
         } else if (this.j && !(var3 >= (double)this.S)) {
            for (ModuleSearchPanel.Type_j var7 : this.Q) {
               if (GuiTextureDrawing.ZAsBoolean(var1, var3, this.a, var7.r, this.y, 14)) {
                  var7.qAsVoid(var5);
                  return true;
               }

               if (var7.M) {
                  for (GuiWidget var9 : var7.y) {
                     if (var9.Y && var9.contains(var1, var3)) {
                        ModuleSearchPanel.this.mAsVoid(var9);
                        if (var9.click(var1, var3, var5)) {
                           ModuleSearchPanel.this.t = var9;
                        }

                        return true;
                     }
                  }
               }
            }

            ModuleSearchPanel.this.sAsVoid();
            return true;
         } else {
            return true;
         }
      }
   }
}
