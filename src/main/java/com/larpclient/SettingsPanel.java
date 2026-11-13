package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.input.KeyInput;

abstract class SettingsPanel extends GuiPanel {
   protected ClickGuiScreen i;
   protected xm P;
   protected final List<GuiWidget> l = new ArrayList<>();
   protected final List<ActionButton> E = new ArrayList<>();
   protected int s;
   protected int H;
   protected int k;
   protected int r;
   protected int S;
   protected int X;
   protected boolean O;
   protected String W;
   protected String C = "";
   protected String T = "";
   private final List<ActionButton> R = new ArrayList<>();
   private final List<ActionButton> N = new ArrayList<>();
   private final ActionButton n = new ActionButton("", this::dismissDialog);
   private List<String> J = List.of();
   private List<String> a = List.of();
   private int M;
   private int c;
   private int B;
   private int x;
   private final w2 d = new w2(1.0);
   private final w2 z = new w2(0.0);
   private final w2 q = new w2(0.0);
   private final w2 g = new w2(0.0);
   private boolean D;
   private boolean v;
   private boolean h;
   private static final int m = 300;
   private static final int V = 14;
   private static final int K = 20;

   protected final void sAsVoid() {
      this.d.aAsVoid(0.0);
      this.d.bAsDouble(1.0, 200);
   }

   protected double revealAmount() {
      return this.d.bAsDouble(1.0, 200);
   }

   SettingsPanel(String var1) {
      super(var1);
      this.W = var1;
      this.n.e = true;
      this.n.q = "x";
      this.n.F = 18;
      this.n.Y = false;
   }

   final void AAsVoid(ClickGuiScreen var1, xm var2) {
      this.i = var1;
      this.P = var2;
      this.init(var1.width, var1.height);
      this.sAsVoid();
   }

   void hAsVoid(xm var1) {
      if (this.P == null || this.P.width() != var1.width() || this.P.height() != var1.height()) {
         this.D = false;
      }

      this.P = var1;
      this.layout();
   }

   protected abstract void build();

   protected void init() {
      this.build();
   }

   protected void JAsVoid() {
      this.yAsVoid();
      this.l.clear();
      this.E.clear();
      this.R.clear();
      this.N.clear();
      this.C = this.T = "";
      this.AAsVoid(this.n);
   }

   protected void dismissDialog() {
   }

   protected ActionButton fAsQo(String var1, Runnable var2) {
      ActionButton var3 = new ActionButton(var1, var2);
      var3.l = true;
      var3.F = 20;
      this.R.add(var3);
      this.AAsVoid(var3);
      return var3;
   }

   protected ActionButton tAsQo(String var1, String var2, Runnable var3) {
      ActionButton var4 = this.MAsQo(var1, var3);
      var4.e = true;
      var4.p = false;
      var4.E = var2;
      var4.F = 30;
      this.N.add(var4);
      return var4;
   }

   protected void OAsVoid() {
      this.AAsVoid(new SettingsPanel.Type_q());
   }

   private boolean bAsBoolean(GuiWidget var1) {
      if (var1 == this.n) {
         return true;
      } else {
         if (var1 instanceof ActionButton var2 && this.R.contains(var2)) {
            return true;
         }

         return false;
      }
   }

   protected void AAsVoid(GuiWidget var1) {
      this.l.add(var1);
      this.Q.add(var1);
   }

   protected void kAsVoid(String var1) {
      this.AAsVoid(new SettingsPanel.Type_n(var1));
   }

   protected void QAsVoid(Supplier<String> var1) {
      this.AAsVoid(new SettingsPanel.Type_n(var1));
   }

   protected ActionButton MAsQo(String var1, Runnable var2) {
      ActionButton var3 = new ActionButton(var1, var2);
      var3.p = var1.equals("Load") || var1.startsWith("Save") || var1.equals("Fetch preview");
      var3.A = var1.startsWith("Delete");
      this.AAsVoid(var3);
      return var3;
   }

   protected void pAsVoid(String var1, boolean var2, Runnable var3) {
      ActionButton var4 = new ActionButton(var1, var3);
      var4.e = true;
      var4.N = true;
      var4.i = var2;
      this.E.add(var4);
      this.Q.add(var4);
   }

   protected int bodyTop() {
      return this.P.bodyTop() + 28;
   }

   protected void layout() {
      int var1 = this.P.contentX();
      int var2 = Math.max(1, (this.P.right() - var1) / Math.max(1, this.E.size()));

      for (ActionButton var4 : this.E) {
         int var5 = Math.min(var2, Math.max(62, TextDrawing.UAsInt(var4.X) + 20));
         var4.bounds(var1, this.P.top() + 12, var5 - 4);
         var4.F = 24;
         var1 += var5;
      }

      this.n.Y = this.O;
      if (this.O) {
         this.lAsVoid();
      } else {
         this.H = this.P.contentX();
         this.r = this.P.right();
         this.k = this.bodyTop();
         this.S = this.P.bottom();
         this.iAsVoid();
      }
   }

   private void lAsVoid() {
      int var1 = this.P.bodyTop() + 8;
      int var2 = Math.max(1, this.P.bottom() - 8 - var1);
      int var3 = this.P.right() - this.P.contentX();
      this.B = Math.max(1, Math.min(300, var3 - 16));
      this.M = this.P.contentX() + (var3 - this.B) / 2;
      this.H = this.M + 14 - 8;
      this.r = this.M + this.B - 14 + 12;
      int var4 = Math.max(1, this.B - 28 - 22);
      this.J = this.C.isEmpty() ? List.of() : OAsList(this.C, var4, var0 -> TextDrawing.jAsInt(var0, TextDrawing.Type_s.TITLE));
      this.a = this.T.isEmpty() ? List.of() : OAsList(this.T, var4, var0 -> TextDrawing.jAsInt(var0, TextDrawing.Type_s.HELP));
      int var5 = 14
         + Math.max(
            14, this.J.size() * (TextDrawing.Type_s.TITLE.zAsInt() + 3) + (this.a.isEmpty() ? 0 : 1 + this.a.size() * (TextDrawing.Type_s.HELP.zAsInt() + 3))
         )
         + 8;
      this.BAsVoid();
      int var6 = Math.max(0, this.X - 4);
      int var7 = this.R.isEmpty() ? 10 : 46;
      this.x = Math.min(var2, var5 + var6 + var7);
      this.c = var1 + (var2 - this.x) * 2 / 5 + (int)Math.round((1.0 - this.revealAmount()) * 6.0);
      this.k = this.c + var5;
      this.S = Math.max(this.k, this.c + this.x - var7);
      this.nAsVoid(0);
      this.n.bounds(this.M + this.B - 14 - 18 + 4, this.c + 14 - 4, 18);
      int var8 = this.c + this.x - 14 - 20;
      List<Integer> var9 = this.R.stream().map(var0 -> Math.max(64, TextDrawing.jAsInt(var0.X, TextDrawing.Type_s.HELP) + 24)).toList();
      int var10 = Math.max(0, this.R.size() - 1) * 6;
      int var11 = var9.stream().mapToInt(Integer::intValue).sum();
      double var12 = Math.min(1.0, (double)Math.max(1, this.B - 28 - var10) / (double)Math.max(1, var11));
      int var14 = this.M + this.B - 14;

      for (int var15 = this.R.size() - 1; var15 >= 0; var15--) {
         int var16 = Math.max(1, (int)((double)var9.get(var15).intValue() * var12));
         var14 -= var16;
         this.R.get(var15).bounds(var14, var8, var16);
         var14 -= 6;
      }
   }

   protected final void iAsVoid() {
      if (!this.layoutCustomContent()) {
         this.BAsVoid();
         this.nAsVoid((int)Math.round((1.0 - this.revealAmount()) * 6.0));
      }
   }

   private void BAsVoid() {
      this.X = 0;

      for (GuiWidget var2 : this.l) {
         if (!this.bAsBoolean(var2)) {
            var2.Y = this.componentShown(var2);
            if (!var2.Y) {
               var2.hAsVoid();
            } else {
               var2.bounds(this.H + 8, 0, Math.max(1, this.r - this.H - 20));
               if (this.O && var2 instanceof ActionButton) {
                  ActionButton var3 = (ActionButton)var2;
                  if (this.N.contains(var3)) {
                     var2.k -= 8;
                     var2.V += 16;
                  }
               }

               if (var2 instanceof SettingsPanel.Type_n) {
                  SettingsPanel.Type_n var10 = (SettingsPanel.Type_n)var2;
                  if (this.O) {
                     var10.k -= 4;
                  }

                  var10.AAsVoid();
               }
            }
         }
      }

      int var8 = 0;

      while (var8 < this.l.size()) {
         GuiWidget var9 = this.l.get(var8);
         if (!var9.Y || this.bAsBoolean(var9)) {
            var8++;
         } else {
            if (var9 instanceof SettingWidget var11 && var11.LAsBoolean()) {
               ArrayList<SettingWidget> var4;
               for (var4 = new ArrayList<>(); var8 < this.l.size(); var8++) {
                  if (!(this.l.get(var8) instanceof SettingWidget var5) || !var5.LAsBoolean()) {
                     break;
                  }

                  if (var5.Y) {
                     var4.add(var5);
                  }
               }

               j.Type_u var12 = j.LAsJ_Type_u(Math.max(1, this.r - this.H - 20), var4.stream().map(SettingWidget::GAsInt).toList());

               for (int var13 = 0; var13 < var4.size(); var13++) {
                  j.Type_m var7 = var12.cells().get(var13);
                  var4.get(var13).bounds(this.H + 8 + var7.x(), this.X + var7.y(), var7.width());
               }

               this.X = this.X + var12.height() + 4;
               continue;
            }

            var9.x = this.X;
            this.X = this.X + var9.F + 4;
            var8++;
         }
      }
   }

   private void nAsVoid(int var1) {
      int var2 = this.X - (this.O ? 4 : 0) - (this.S - this.k);
      this.s = Math.clamp((long)this.s, 0, Math.max(0, var2));
      if (!this.D) {
         this.z.aAsVoid((double)this.s);
         this.D = true;
      }

      int var3 = (int)Math.round(this.z.bAsDouble((double)this.s, 150));
      int var4 = this.k - var3 + var1;

      for (GuiWidget var6 : this.l) {
         if (var6.Y && !this.bAsBoolean(var6)) {
            var6.x += var4;
            if (var6 instanceof SettingWidget var7) {
               var7.fAsVoid(this.P.top() + 12, this.S);
            }
         }
      }
   }

   protected boolean componentShown(GuiWidget var1) {
      boolean var10000;
      if (var1 instanceof SettingWidget var3) {
         var10000 = var3.vAsBoolean();
      } else {
         if (var1 instanceof SectionHeaderWidget var2 && !var2.nAsBoolean()) {
            return false;
         }

         var10000 = true;
      }

      return var10000;
   }

   protected boolean layoutCustomContent() {
      return false;
   }

   protected boolean aAsBoolean() {
      return true;
   }

   protected boolean dAsBoolean() {
      return true;
   }

   protected int titleX() {
      return this.P.contentX() + 8;
   }

   protected int titleY() {
      return this.P.bodyTop() + 8;
   }

   protected void renderExtra(DrawContext var1, int var2, int var3) {
   }

   public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
      if (!this.horizontalInput((double)mouseX)) {
         mouseX = -1;
      }

      if (this.O != this.h) {
         this.sAsVoid();
         this.h = this.O;
      }

      this.layout();

      for (ActionButton var6 : this.E) {
         if (var6.i) {
            if (!this.v) {
               this.q.aAsVoid((double)var6.k);
               this.g.aAsVoid((double)var6.V);
               this.v = true;
            }

            int var7 = (int)Math.round(this.q.bAsDouble((double)var6.k, 200));
            int var8 = (int)Math.round(this.g.bAsDouble((double)var6.V, 200));
            int var9 = bd.hAsBoolean() ? bd.WAsInt(var7, bd.m) : bg.XAsInt(bd.u, 0.75);
            GuiDrawing.HAsVoid(context, (float)(var7 + 7), (float)(var6.x + var6.F), (float)Math.max(1, var8 - 14), 2.0F, 1.0F, 4.0F, bg.XAsInt(var9, 0.45));
            GuiDrawing.CAsVoid(context, var7 + 7, var6.x + var6.F, Math.max(1, var8 - 14), 2, 1, var9);
         }
      }

      for (ActionButton var24 : this.E) {
         var24.render(context, this.O ? -1 : mouseX, mouseY);
      }

      if (this.dAsBoolean()) {
         TextDrawing.NAsVoid(
            context,
            TextDrawing.zAsString(this.W, Math.max(1, this.r - this.titleX() - 32), TextDrawing.Type_s.TITLE),
            this.titleX(),
            this.titleY(),
            bd.u,
            TextDrawing.Type_s.TITLE
         );
      }

      this.renderExtra(context, this.O ? -1 : mouseX, mouseY);
      if (this.O) {
         this.qAsVoid(context, mouseX, mouseY);
      }

      context.enableScissor(this.H, this.k, this.r, this.S);

      try (qg var22 = qg.nAsQg(this.revealAmount())) {
         for (GuiWidget var27 : this.l) {
            if (this.aAsBoolean() && var27.Y && !this.bAsBoolean(var27) && var27.x + var27.F > this.k && var27.x < this.S) {
               var27.render(context, mouseY >= this.k && mouseY < this.S ? mouseX : -1, mouseY);
            }
         }
      } finally {
         context.disableScissor();
      }

      int var23 = this.S - this.k;
      if (this.aAsBoolean()) {
         GuiDrawing.aAsVoid(context, this.r - 3, this.k, Math.max(1, var23), this.X - (this.O ? 4 : 0), (int)Math.round(this.z.yAsDouble()));
      }

      if (this.O) {
         try (qg var26 = qg.nAsQg(this.revealAmount())) {
            if (this.X - 4 > var23) {
               int var28 = bg.pAsInt(bd.I, 1.2);
               if (this.s > 0) {
                  GuiDrawing.CAsVoid(context, this.M + 14, this.k - 1, this.B - 28, 1, 0, var28);
               }

               GuiDrawing.CAsVoid(context, this.M + 14, this.S, this.B - 28, 1, 0, var28);
            }

            this.n.render(context, mouseX, mouseY);

            for (ActionButton var30 : this.R) {
               var30.render(context, mouseX, mouseY);
            }
         }
      }

      if (this.renderPopupInline()) {
         this.MAsVoid(context, mouseX, mouseY);
      }
   }

   private void qAsVoid(DrawContext var1, int var2, int var3) {
      double var4 = this.revealAmount();
      GuiDrawing.CAsVoid(
         var1, this.P.contentX(), this.P.bodyTop(), this.P.right() - this.P.contentX(), this.P.bottom() - this.P.bodyTop(), bd.l, bg.pAsInt(-1728053248, var4)
      );
      RoundedShapePipeline.CAsVoid(
         var1, (float)this.M, (float)(this.c + 3), (float)this.B, (float)this.x, (float)bd.l, 16.0F, bg.pAsInt(805306368, var4), bg.pAsInt(1342177280, var4)
      );
      GuiDrawing.PAsVoid(var1, this.M, this.c, this.B, this.x, bd.l, bg.pAsInt(bd.V, var4), 0.0);

      try (qg var6 = qg.nAsQg(var4)) {
         int var7 = this.c + 14;

         for (String var9 : this.J) {
            TextDrawing.NAsVoid(var1, var9, this.M + 14, var7, bd.u, TextDrawing.Type_s.TITLE);
            var7 += TextDrawing.Type_s.TITLE.zAsInt() + 3;
         }

         var7++;

         for (String var14 : this.a) {
            TextDrawing.NAsVoid(var1, var14, this.M + 14, var7, bd.e, TextDrawing.Type_s.HELP);
            var7 += TextDrawing.Type_s.HELP.zAsInt() + 3;
         }
      }
   }

   private boolean hAsBoolean(double var1, double var3) {
      return this.O
         && !GuiTextureDrawing.ZAsBoolean(var1, var3, this.M, this.c, this.B, this.x)
         && var1 >= (double)this.P.contentX()
         && var1 < (double)this.P.right()
         && var3 >= (double)this.P.top()
         && var3 < (double)this.P.bottom();
   }

   protected boolean renderPopupInline() {
      return true;
   }

   final void MAsVoid(DrawContext var1, int var2, int var3) {
      SettingWidget var4 = this.AAsQf();
      if (var4 != null) {
         var4.oAsVoid(var1, this.horizontalInput((double)var2) ? var2 : -1, var3);
      }
   }

   private SettingWidget AAsQf() {
      for (GuiWidget var2 : this.l) {
         if (var2 instanceof SettingWidget var3 && var3.yAsBoolean()) {
            return var3;
         }
      }

      return null;
   }

   @Override
   protected boolean accepts(GuiWidget var1, double var2, double var4) {
      if (!this.horizontalInput(var2)) {
         return false;
      } else if (!super.accepts(var1, var2, var4)) {
         return false;
      } else if (this.O && !this.l.contains(var1)) {
         return false;
      } else {
         return this.bAsBoolean(var1)
            ? this.O
            : !this.l.contains(var1) || this.aAsBoolean() && var4 >= (double)this.k && var4 < (double)this.S && var2 >= (double)this.H && var2 < (double)this.r;
      }
   }

   @Override
   public boolean mouseClicked(Click click, boolean doubled) {
      this.layout();
      SettingWidget var3 = this.AAsQf();
      if (var3 == null) {
         if (click.button() == 0
            && this.hAsBoolean(click.x(), click.y())
            && this.horizontalInput(click.x())
            && this.l.stream().noneMatch(var0 -> var0 instanceof TextInputWidget)) {
            this.dismissDialog();
            return true;
         } else {
            return super.mouseClicked(click, doubled);
         }
      } else {
         if (this.horizontalInput(click.x()) && var3.uAsBoolean(click.x(), click.y())) {
            var3.oAsBoolean(click.x(), click.y(), click.button());
            this.t = var3;
         } else {
            var3.BAsVoid();
         }

         return true;
      }
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      SettingWidget var9 = this.AAsQf();
      if (var9 != null) {
         if (this.horizontalInput(mouseX) && var9.uAsBoolean(mouseX, mouseY)) {
            var9.OAsVoid(verticalAmount);
         }

         return true;
      } else if (this.horizontalInput(mouseX)
         && this.aAsBoolean()
         && !(mouseX < (double)this.H)
         && !(mouseX >= (double)this.r)
         && !(mouseY < (double)this.k)
         && !(mouseY >= (double)this.S)) {
         this.s -= (int)(verticalAmount * 27.0);
         this.layout();
         return true;
      } else {
         return false;
      }
   }

   @Override
   protected void focusChanged(GuiWidget var1) {
      if (this.l.contains(var1)) {
         if (var1.x < this.k) {
            this.s = this.s - (this.k - var1.x);
         } else if (var1.x + var1.F > this.S) {
            this.s = this.s + (var1.x + var1.F - this.S);
         }

         this.z.aAsVoid((double)this.s);
         this.layout();
      }
   }

   protected void TAsVoid(GuiWidget var1) {
      this.b = var1;
      var1.y = var1.Q = true;
   }

   protected boolean horizontalInput(double var1) {
      return true;
   }

   List<GuiWidget> qAsList() {
      return this.Q.stream().filter(var1 -> var1.Y && var1.B && (!this.l.contains(var1) || this.aAsBoolean()) && (!this.O || this.l.contains(var1))).toList();
   }

   GuiWidget YAsQb() {
      return this.b;
   }

   void fAsVoid() {
      if (this.b != null) {
         this.b.hAsVoid();
      }

      this.b = this.t = null;
   }

   void FAsVoid(GuiWidget var1) {
      this.TAsVoid(var1);
      this.focusChanged(var1);
   }

   void suspend() {
      this.yAsVoid();
   }

   String VAsString(int var1, int var2) {
      if (this.O) {
         return "";
      } else {
         if (this.b instanceof SettingWidget var3 && this.b.Q) {
            return var3.SAsString();
         }

         if (this.horizontalInput((double)var1) && var2 >= this.k && var2 < this.S) {
            for (GuiWidget var7 : this.l) {
               if (var7 instanceof SettingWidget var5 && var5.contains((double)var1, (double)var2)) {
                  return var5.SAsString();
               }
            }

            return "";
         } else {
            return "";
         }
      }
   }

   boolean TAsBoolean() {
      return this.O || this.AAsQf() != null;
   }

   @Override
   public boolean keyPressed(KeyInput input) {
      SettingWidget var2 = this.AAsQf();
      if (input.key() == 256 && var2 != null) {
         var2.BAsVoid();
         return true;
      } else if (input.key() == 258) {
         List var3 = this.qAsList();
         int var4 = (input.modifiers() & 1) != 0 ? -1 : 1;
         int var5 = this.b == null ? (var4 > 0 ? -1 : 0) : var3.indexOf(this.b);
         this.fAsVoid();
         if (!var3.isEmpty()) {
            this.FAsVoid((GuiWidget)var3.get(Math.floorMod(var5 + var4, var3.size())));
         }

         return true;
      } else {
         return super.keyPressed(input);
      }
   }

   public void close() {
      this.i.close();
   }

   static List<String> OAsList(String var0, int var1, ToIntFunction<String> var2) {
      ArrayList var3 = new ArrayList();

      for (String var7 : var0.split("\n", -1)) {
         String var8 = var7;

         while (!var8.isEmpty() && var2.applyAsInt(var8) > var1) {
            int var9 = var8.length();

            while (var9 > Character.charCount(var8.codePointAt(0)) && var2.applyAsInt(var8.substring(0, var9)) > var1) {
               var9 = var8.offsetByCodePoints(var9, -1);
            }

            int var10 = var8.lastIndexOf(32, var9 - 1);
            if (var10 > 0) {
               var9 = var10 + 1;
            }

            var3.add(var8.substring(0, var9));
            var8 = var8.substring(var9);
         }

         var3.add(var8);
      }

      return var3;
   }

   private static final class Type_n extends GuiWidget {
      private final Supplier<String> S;
      private List<String> c = List.of();
      private int D = -1;
      private String X;

      Type_n(String var1) {
         this(() -> var1);
      }

      Type_n(Supplier<String> var1) {
         this.S = var1;
         this.B = false;
      }

      void AAsVoid() {
         String var1 = this.S.get();
         if (this.D != this.V || !var1.equals(this.X)) {
            this.D = this.V;
            this.X = var1;
            this.c = SettingsPanel.OAsList(var1, this.V - 8, TextDrawing::UAsInt);
            this.F = Math.max(22, this.c.size() * 12 + 6);
         }
      }

      @Override
      public void render(DrawContext var1, int var2, int var3) {
         for (int var4 = 0; var4 < this.c.size(); var4++) {
            TextDrawing.UAsVoid(var1, this.c.get(var4), this.k + 4, this.x + 4 + var4 * 12, bd.e);
         }
      }
   }

   private static final class Type_q extends GuiWidget {
      Type_q() {
         this.B = false;
         this.F = 5;
      }

      @Override
      public void render(DrawContext var1, int var2, int var3) {
         GuiDrawing.CAsVoid(var1, this.k, this.x + 2, Math.max(1, this.V), 1, 0, bg.pAsInt(bd.I, 1.2));
      }
   }
}
