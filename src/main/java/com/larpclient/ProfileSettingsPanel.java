package com.larpclient;

import com.google.gson.JsonObject;
import com.larpclient.module.Module;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.input.KeyInput;

public final class ProfileSettingsPanel extends SettingsPanel {
   final PlatformService wq = PlatformService.x;
   final ProfileManager wv = LarpClient.CAsLarpClient().fAsBo();
   final ProfileImportPreview wI = new ProfileImportPreview(this);
   private final bv p = new bv(this);
   String Z = "";
   String Y = "";
   String G = "";
   boolean wg;
   boolean wf;
   private boolean ww;
   private boolean wU;
   private String wX = "";
   private String L = "";
   private Runnable o;
   private Runnable f;
   private String A = "";
   private String wo = "Confirm";
   private String wz = "";
   private String U = "Cancel";
   private boolean wO;
   private final List<ActionButton> w = new ArrayList<>();
   private final List<ActionButton> e = new ArrayList<>();
   private final Map<ActionButton, List<ActionButton>> wT = new LinkedHashMap<>();
   private TextInputWidget wB;
   private TextInputWidget wA;
   private TextInputWidget wY;
   private int u;
   private int j;
   private int y = 62;
   private final w2 F = new w2(0.0);
   private final dr<JsonObject> I = new dr<>(var1x -> this.JAsMinecraftClient().execute(var1x), this::fAsBoolean);

   ProfileSettingsPanel(ClickGuiScreen var1) {
      super("Configs");
      this.i = var1;
   }

   private boolean fAsBoolean() {
      return this.JAsMinecraftClient().currentScreen == this.i && this.i.cAsBoolean(this);
   }

   List<Module> oAsList() {
      return LarpClient.CAsLarpClient().mAsF().NAsList();
   }

   MinecraftClient JAsMinecraftClient() {
      return this.i.client();
   }

   void nAsVoid(GuiWidget var1) {
      this.Q.add(var1);
   }

   protected void text(String var1) {
      super.kAsVoid(var1);
   }

   ActionButton OAsQo(String var1, Runnable var2) {
      return this.MAsQo(var1, () -> {
         if (!this.wg) {
            this.NAsVoid(var2);
         }
      });
   }

   protected ActionButton option(String var1, String var2, Runnable var3) {
      return super.tAsQo(var1, var2, () -> {
         if (!this.wg) {
            this.NAsVoid(var3);
         }
      });
   }

   ActionButton rAsQo(String var1, Runnable var2) {
      return this.fAsQo(var1, () -> {
         if (!this.wg) {
            this.NAsVoid(var2);
         }
      });
   }

   void QAsVoid(String var1, String var2) {
      this.C = var1;
      this.T = var2;
   }

   TextInputWidget tAsQq(String var1, String var2, int var3, Consumer<String> var4) {
      TextInputWidget var5 = new TextInputWidget(var1, var4, var3);
      var5.W = var2;
      this.AAsVoid(var5);
      return var5;
   }

   void FAsVoid(String var1, String var2, boolean var3, Runnable var4) {
      ActionButton var5 = new ActionButton(var1, () -> {
         if (!this.wg) {
            this.Y = "";
            this.sAsVoid();
            this.NAsVoid(var4);
         }
      });
      var5.e = true;
      var5.K = true;
      var5.E = var2;
      var5.i = var2.equals("Active");
      this.w.add(var5);
      this.Q.add(var5);
   }

   void QAsVoid(String var1, Runnable var2) {
      if (!this.w.isEmpty()) {
         ActionButton var3 = this.w.getLast();
         ActionButton var4 = new ActionButton(var1, () -> {
            if (!this.wg) {
               this.NAsVoid(var2);
            }
         });
         var4.p = var1.equals("Load") || var1.equals("Update");
         var4.A = var1.equals("Delete");
         var4.l = true;
         this.wT.computeIfAbsent(var3, var0 -> new ArrayList<>()).add(var4);
         this.Q.add(var4);
      }
   }

   private void JAsVoid(String var1, Runnable var2) {
      ActionButton var3 = new ActionButton(var1, () -> {
         if (!this.wg) {
            this.NAsVoid(var2);
         }
      });
      var3.e = true;
      this.e.add(var3);
      this.Q.add(var3);
   }

   private void NAsVoid(Runnable var1) {
      try {
         var1.run();
      } catch (IllegalArgumentException var3) {
         this.Z = var3.getMessage();
         ClientLog.oAsVoid("Config", "Config action rejected: {}", this.Z);
         this.build();
      } catch (RuntimeException var4) {
         this.Z = "The operation could not be completed. Try again.";
         ClientLog.lAsVoid("Config", "Config action failed", var4);
         this.build();
      }
   }

   @Override
   protected void build() {
      this.JAsVoid();
      this.w.clear();
      this.e.clear();
      this.wT.clear();
      this.wA = this.wY = null;
      this.O = this.ww || this.wf || this.wI.m != null || this.o != null || !this.wX.isEmpty() || this.WAsBoolean();
      this.pAsVoid("My Configs", !this.wU, () -> this.EAsVoid("My Configs"));
      this.pAsVoid("Discover", this.wU, () -> this.EAsVoid("Discover"));
      this.wB = new TextInputWidget(this.wU ? "Search this page of configs..." : "Search configs...", var1x -> {
         if (this.wU) {
            this.p.k = var1x;
            this.p.c = System.currentTimeMillis() + 180L;
         } else {
            this.G = var1x;
            this.VAsVoid();
         }
      }, 80);
      this.wB.W = this.wU ? this.p.k : this.G;
      this.Q.add(this.wB);
      if (!this.wU) {
         this.JAsVoid("Save as new config", () -> this.WAsVoid("New", ""));
         this.JAsVoid("Import config", () -> {
            this.wf = true;
            this.s = 0;
            this.build();
            if (this.wY != null) {
               this.TAsVoid(this.wY);
            }
         });
         if (this.P != null && !this.P.columns()) {
            this.JAsVoid("More", () -> {
               this.ww = true;
               this.s = 0;
               this.build();
            });
         } else {
            this.JAsVoid("Refresh shared", () -> this.wI.TAsVoid(false));
            if (this.wq.SAsBoolean()) {
               this.JAsVoid(this.wI.W ? "Regular storage" : "Experimental storage", this::WAsVoid);
            }
         }

         if (!this.Y.isEmpty()) {
            this.JAsVoid("Copy share code", () -> {
               this.JAsMinecraftClient().keyboard.setClipboard(this.Y);
               this.Z = "Share code copied to clipboard.";
            });
         }
      } else {
         this.JAsVoid("Refresh", this.p::FAsVoid);
         this.JAsVoid("Previous", () -> {
            if (this.p.y > 0) {
               this.p.y = Math.max(0, this.p.y - 30);
               this.p.FAsVoid();
            }
         });
         this.JAsVoid("Next", () -> {
            if (this.p.y + 30 < this.p.F) {
               this.p.y += 30;
               this.p.FAsVoid();
            }
         });
         this.e.get(1).B = this.p.y > 0;
         this.e.get(2).B = this.p.y + 30 < this.p.F;
      }

      if (this.wU) {
         this.p.vAsVoid();
      } else {
         this.wI.yAsVoid();
      }

      if (this.ww) {
         this.QAsVoid("Shared configs", "");
         this.option("Refresh shared", "Add your uploaded configs to this list", () -> {
            this.ww = false;
            this.wI.TAsVoid(false);
         });
         if (this.wq.SAsBoolean()) {
            this.option(
               this.wI.W ? "Regular storage" : "Experimental storage",
               this.wI.W ? "Show your regular shared configs" : "Show configs in experimental storage",
               () -> {
                  this.ww = false;
                  this.WAsVoid();
               }
            );
         }
      } else if (this.o != null) {
         int var1 = this.A.indexOf(10);
         this.QAsVoid(var1 < 0 ? this.A : this.A.substring(0, var1), "");
         if (var1 >= 0) {
            this.text(this.A.substring(var1 + 1));
         }

         this.rAsQo(this.U, () -> {
            this.o = this.f = null;
            this.build();
         });
         if (this.f != null) {
            this.rAsQo(this.wz, () -> this.BAsVoid(this.f));
         }

         ActionButton var2 = this.rAsQo(this.wo, () -> this.BAsVoid(this.o));
         var2.p = true;
         var2.A = this.wO;
      } else if (this.wI.m != null) {
         this.wI.DAsVoid();
      } else if (this.wf) {
         this.QAsVoid("Import config", "Paste a share code. Nothing loads until you confirm.");
         this.wY = this.wI.cAsQq();
         this.rAsQo("Cancel", () -> {
            this.wf = false;
            this.build();
         });
         this.rAsQo("Fetch preview", () -> this.wI.oAsVoid(ProfileShareCodec.lAsString(this.wI.d))).p = true;
      } else if (!this.wX.isEmpty()) {
         String var3 = this.wX;
         switch (var3) {
            case "New":
               this.QAsVoid("Save as new config", "Saves your current settings. Nothing is loaded.");
               break;
            case "Duplicate":
               this.QAsVoid("Duplicate “" + this.wI.S + "”", "Name the copy.");
               break;
            default:
               this.QAsVoid(this.wX + " “" + this.wI.S + "”", "");
         }

         this.wA = this.tAsQq("Config name", this.L, 32, var1x -> this.L = var1x);
         this.rAsQo("Cancel", () -> {
            this.wX = "";
            this.build();
         });
         this.rAsQo(this.wX.equals("Rename") ? "Rename" : "Save", this::QAsVoid).p = true;
      } else if (this.wU) {
         this.p.hAsVoid();
      } else {
         this.wI.jAsVoid();
      }

      this.W = this.wU ? "Discover" : "My Configs";

      for (GuiWidget var6 : this.Q) {
         if (!(var6 instanceof SectionHeaderWidget)) {
            var6.B = var6.B & !this.wg;
         }
      }

      if (this.P != null) {
         this.layout();
      }
   }

   private void WAsVoid() {
      this.wI.W = !this.wI.W;
      this.wI.XAsVoid();
      this.wI.D = List.of();
      this.wI.TAsVoid(false);
   }

   void EAsVoid() {
      this.VAsVoid();
   }

   private void VAsVoid() {
      int var1 = this.wB != null && this.wB.y ? 1 : 0;
      this.u = 0;
      this.build();
      if (var1 != 0) {
         this.TAsVoid(this.wB);
      }
   }

   void WAsVoid(String var1, String var2) {
      this.wX = var1;
      this.L = var2;
      this.s = 0;
      this.build();
      if (this.wA != null) {
         this.TAsVoid(this.wA);
      }
   }

   private void QAsVoid() {
      this.i.HAsVoid();
      String var1 = ProfileCodec.IAsString(this.L);

      try {
         if (this.wX.equals("Rename")) {
            this.wv.pAsVoid(this.wI.S, var1);
         } else if (this.wX.equals("Duplicate")) {
            this.wv.DAsVoid(this.wI.S, var1);
         } else {
            this.wv.uAsVoid(var1, false);
         }

         this.wI.S = var1;
         this.wX = "";
         this.Z = "Saved " + var1;
         this.build();
      } catch (IOException var3) {
         this.Z = var3.getMessage();
         ClientLog.oAsVoid("Config", "Config {} failed: {}", this.wX, var3.toString());
         this.build();
      }
   }

   void EAsVoid(String var1) {
      if (!this.wg) {
         this.sAsVoid();
         this.wU = var1.equals("Discover");
         this.wI.m = null;
         this.wf = false;
         this.ww = false;
         this.wX = "";
         this.o = null;
         this.p.bAsVoid();
         this.s = this.u = 0;
         this.Y = "";
         this.Z = "";
         ClientLog.GAsVoid("GUI", "Configs switched to {}", this.wU ? "Discover" : "My Configs");
         if (this.wU && !this.p.n) {
            this.p.FAsVoid();
         } else {
            this.build();
         }
      }
   }

   boolean QAsBoolean() {
      return this.wU && !this.O;
   }

   void fAsVoid(String var1, String var2, Runnable var3) {
      this.hAsVoid(var1, var2, var3, "", null);
   }

   void KAsVoid(String var1, String var2, Runnable var3) {
      this.hAsVoid(var1, var2, var3, "", null);
      this.wO = true;
      this.build();
   }

   void hAsVoid(String var1, String var2, Runnable var3, String var4, Runnable var5) {
      this.A = var1;
      this.wo = var2;
      this.o = var3;
      this.wO = false;
      this.U = "Cancel";
      this.wz = var4;
      this.f = var5;
      this.s = 0;
      this.build();
   }

   void wAsVoid(String var1, String var2, Runnable var3, String var4) {
      this.hAsVoid(var1, var2, var3, "", null);
      this.U = var4;
      this.build();
   }

   private void BAsVoid(Runnable var1) {
      this.o = this.f = null;
      this.wf = false;
      var1.run();
   }

   void iAsVoid(String var1, CompletableFuture<JsonObject> var2, Consumer<JsonObject> var3) {
      if (this.wg) {
         var2.cancel(true);
      } else {
         this.wg = true;
         this.Z = var1;
         this.build();
         NotificationCategory.CAsVoid("API", NotificationCategory.Type_o.REQUEST_STARTED);
         this.I.BAsBoolean(var2, (var3xx, var4) -> {
            this.wg = false;
            if (var4 != null) {
               NotificationCategory.CAsVoid("API", NotificationCategory.Type_o.REQUEST_FAILED);
               this.Z = this.wq.SAsString(var4);
               ClientLog.oAsVoid("API", "Config request failed: {}", this.Z);
               AuthException var5 = PlatformService.BAsAh(var4);
               if (var5.GAsString().equals("REVISION_CONFLICT") && !var1.equals("Loading cloud items...")) {
                  this.wI.TAsVoid(true);
                  return;
               }

               if (var5.vAsInt() == 403 && this.wI.W) {
                  this.wI.JAsVoid();
               }

               if (var5.vAsInt() == 401 || var5.vAsInt() == 503 || var5.GAsString().equals("NETWORK")) {
                  this.wq.YAsCompletableFuture();
               }
            } else {
               try {
                  var3.accept(var3xx);
                  NotificationCategory.CAsVoid("API", NotificationCategory.Type_o.REQUEST_SUCCEEDED);
               } catch (RuntimeException var61) {
                  this.Z = "Invalid server response. Nothing was loaded locally.";
                  ClientLog.oAsVoid("API", "Invalid config response: {}", var61.toString());
               }
            }

            this.build();
         });
      }
   }

   protected boolean showTitle() {
      return false;
   }

   protected boolean contentShown() {
      return this.O;
   }

   @Override
   protected int bodyTop() {
      return this.P.bodyTop() + this.y;
   }

   private int UAsInt() {
      return this.P.right();
   }

   private boolean WAsBoolean() {
      return this.wU ? this.p.s != null : this.wI.b != null || this.wI.S != null;
   }

   @Override
   protected void layout() {
      int var1 = this.P.contentX();
      int var2 = this.P.right() - var1;
      int var3 = Math.min(68, Math.max(1, var2 - 60));
      int var4 = Math.min(180, Math.max(1, var2 - var3));
      this.wB.bounds(var1 + var3, this.P.bodyTop(), var4);
      this.wB.F = 20;
      j.Type_u var5 = com.larpclient.j.LAsJ_Type_u(var2, this.e.stream().map(var0 -> TextDrawing.jAsInt(var0.X, TextDrawing.Type_s.HELP) + 12).toList());
      this.y = 27 + var5.height() + 8;

      for (int var6 = 0; var6 < this.e.size(); var6++) {
         ActionButton var7 = this.e.get(var6);
         j.Type_m var8 = var5.cells().get(var6);
         var7.l = true;
         var7.bounds(var1 + var8.x(), this.P.bodyTop() + 27 + var8.y(), var8.width());
         var7.F = 24;
      }

      super.layout();
      o1 var12 = o1.calculate(var2, this.w.size());
      int var13 = Math.max(1, this.P.bottom() - this.bodyTop());
      this.j = var12.height();
      this.u = Math.clamp((long)this.u, 0, Math.max(0, this.j - var13));
      int var14 = (int)Math.round(this.F.bAsDouble((double)this.u, 150));

      for (int var9 = 0; var9 < this.w.size(); var9++) {
         ActionButton var10 = this.w.get(var9);
         var10.bounds(var1 + var12.xAsInt(var9), this.bodyTop() + var12.yAsInt(var9) - var14, var12.cardWidth());
         var10.F = 48;
         List var11 = this.wT.getOrDefault(var10, List.of());
         rAsVoid(var10, var11);
      }
   }

   static void rAsVoid(ActionButton var0, List<ActionButton> var1) {
      int var2 = var1.stream().mapToInt(var0x -> var0x.A ? 36 : 46).sum();
      int var3 = Math.max(0, var1.size() - 1) * 4;
      int var4 = var0.V - ActionButton.rAsInt(var0.V) - 6 - var3;
      double var5 = Math.min(1.0, (double)Math.max(1, var4) / (double)Math.max(1, var2));
      int var7 = var1.stream().mapToInt(var2x -> Math.max(1, (int)((double)(var2x.A ? 36 : 46) * var5))).sum() + var3;
      int var8 = var0.k + var0.V - 6 - var7;

      for (ActionButton var10 : var1) {
         int var11 = Math.max(1, (int)((double)(var10.A ? 36 : 46) * var5));
         var10.bounds(var8, var0.x + 31, var11);
         var10.F = 14;
         var10.Y = var0.Y;
         var8 += var11 + 4;
      }
   }

   @Override
   protected void renderExtra(DrawContext var1, int var2, int var3) {
      TextDrawing.NAsVoid(
         var1,
         TextDrawing.zAsString("Configs", Math.max(1, this.wB.k - this.P.contentX() - 6), TextDrawing.Type_s.TITLE),
         this.P.contentX(),
         this.P.bodyTop() + 3,
         bd.u,
         TextDrawing.Type_s.TITLE
      );
      this.wB.render(var1, var2, var3);

      for (ActionButton var5 : this.e) {
         var5.render(var1, var2, var3);
      }

      var1.enableScissor(this.P.contentX(), this.bodyTop(), this.UAsInt(), this.P.bottom());

      try {
         int var12 = var3 >= this.bodyTop() && var3 < this.P.bottom() ? var2 : -1;

         for (ActionButton var6 : this.w) {
            if (var6.x + var6.F > this.bodyTop() && var6.x < this.P.bottom()) {
               var6.render(var1, var12, var3);

               for (ActionButton var8 : this.wT.getOrDefault(var6, List.of())) {
                  var8.render(var1, var12, var3);
               }
            }
         }
      } finally {
         var1.disableScissor();
      }

      GuiDrawing.aAsVoid(var1, this.UAsInt() - 3, this.bodyTop(), Math.max(1, this.P.bottom() - this.bodyTop()), this.j, (int)Math.round(this.F.yAsDouble()));
      if (this.w.isEmpty()) {
         TextDrawing.UAsVoid(var1, this.wg ? "Loading..." : "No configs found", this.P.contentX() + 8, this.bodyTop() + 8, bd.e);
      }
   }

   @Override
   protected boolean accepts(GuiWidget var1, double var2, double var4) {
      if (this.w.contains(var1) && this.wT.getOrDefault(var1, List.of()).stream().anyMatch(var4xx -> var4xx.contains(var2, var4))) {
         return false;
      } else {
         return !this.w.contains(var1) && !this.wT.values().stream().anyMatch(var1xx -> var1xx.contains(var1))
            ? super.accepts(var1, var2, var4)
            : !this.O && var1.contains(var2, var4) && var4 >= (double)this.bodyTop() && var4 < (double)this.P.bottom() && var2 < (double)this.UAsInt();
      }
   }

   @Override
   protected void focusChanged(GuiWidget var1) {
      if (!this.w.contains(var1) && !this.wT.values().stream().anyMatch(var1xx -> var1xx.contains(var1))) {
         super.focusChanged(var1);
      } else {
         if (var1.x < this.bodyTop()) {
            this.u = this.u - (this.bodyTop() - var1.x);
         } else if (var1.x + var1.F > this.P.bottom()) {
            this.u = this.u + (var1.x + var1.F - this.P.bottom());
         }

         this.F.aAsVoid((double)this.u);
         this.layout();
      }
   }

   @Override
   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      if (!this.TAsBoolean()
         && (this.P.columns() || !this.WAsBoolean())
         && mouseX >= (double)this.P.contentX()
         && mouseX < (double)this.UAsInt()
         && mouseY >= (double)this.bodyTop()
         && mouseY < (double)this.P.bottom()) {
         this.u -= (int)(verticalAmount * 27.0);
         this.layout();
         return true;
      } else {
         return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
      }
   }

   @Override
   protected void dismissDialog() {
      if (this.O && !this.wg) {
         if (this.WAsBoolean() && !this.ww && this.o == null && this.wI.m == null && !this.wf && this.wX.isEmpty()) {
            this.wI.b = null;
            this.wI.S = null;
            this.p.bAsVoid();
         } else if (this.ww) {
            this.ww = false;
         } else if (this.o != null) {
            this.o = this.f = null;
         } else if (this.wI.m != null) {
            this.wI.m = null;
         } else {
            this.wf = false;
            this.wX = "";
         }

         this.build();
      }
   }

   @Override
   public boolean keyPressed(KeyInput input) {
      if (input.key() == 256 && this.O && !this.wg) {
         this.dismissDialog();
         return true;
      } else {
         return super.keyPressed(input);
      }
   }

   public void tick() {
      this.p.CAsVoid();
   }

   @Override
   void suspend() {
      this.wg = false;
      this.I.dAsVoid();
      super.suspend();
   }
}
