package com.larpclient;

import com.larpclient.module.AutoSell;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.KeyInput;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;

public final class AuctionItemSelectionPanel extends GuiPanel {
   private final Screen g;
   private final AutoSell R;
   private final boolean K;
   private final LinkedHashSet<String> n;
   private final List<AuctionItemSelectionPanel.Type_n> W = new ArrayList<>();
   private final List<GuiWidget> P = new ArrayList<>();
   private List<AuctionItemSelectionPanel.Type_n> AuraModule = List.of();
   private TextInputWidget j;
   private ActionButton h;
   private ActionButton q;
   private ActionButton X;
   private ActionButton u;
   private ActionButton Y;
   private ActionButton N;
   private ActionButton V;
   private String BoxUtils = "";
   private String GroundSample = "";
   private boolean S;
   private boolean d;
   private int f;
   private int I;
   private int E;
   private int M;
   private int C;
   private int z;
   private int a;
   private int e;
   private int J;
   private Set<String> G = Set.of();
   private Map<String, Integer> T = Map.of();
   private final w2 L = new w2(0.0);

   public AuctionItemSelectionPanel(Screen var1, AutoSell var2, boolean var3) {
      super(var3 ? "Never sell" : "Choose sell items");
      this.g = var1;
      this.R = var2;
      this.K = var3;
      this.n = new LinkedHashSet<>(var2.zAsSet(var3));

      for (Item var5 : Registries.ITEM) {
         ItemStack var6 = new ItemStack(var5);
         if (!var6.isEmpty()) {
            String var7 = Registries.ITEM.getId(var5).toString();
            String var8 = var6.getName().getString();
            this.W.add(new AuctionItemSelectionPanel.Type_n(var7, var8, var6, (var8 + " " + var7).toLowerCase(Locale.ROOT).replace('_', ' ')));
         }
      }

      for (String var10 : this.n) {
         if (this.W.stream().noneMatch(var1x -> var1x.id.equals(var10))) {
            this.W.add(new AuctionItemSelectionPanel.Type_n(var10, var10 + " (unavailable)", ItemStack.EMPTY, var10.replace('_', ' ')));
         }
      }

      this.W
         .sort(Comparator.comparing(AuctionItemSelectionPanel.Type_n::name, String.CASE_INSENSITIVE_ORDER).thenComparing(AuctionItemSelectionPanel.Type_n::id));
      this.zAsVoid();
      this.L.bAsDouble(1.0, 200);
      ClientLog.XAsVoid("AutoSell", "Opened {} item picker; draft size={}", var3 ? "excluded" : "selected", this.n.size());
   }

   private void zAsVoid() {
      HashSet var1 = new HashSet();
      if (this.client.player != null) {
         for (int var2 = 0; var2 < 36; var2++) {
            ItemStack var3 = this.client.player.getInventory().getStack(var2);
            if (!var3.isEmpty()) {
               var1.add(Registries.ITEM.getId(var3.getItem()).toString());
            }
         }
      }

      this.G = Set.copyOf(var1);
   }

   protected void init() {
      this.yAsVoid();
      this.P.clear();
      this.E = Math.max(1, Math.min(700, this.width - 24));
      this.M = Math.max(1, Math.min(500, this.height - 20));
      this.f = (this.width - this.E) / 2;
      this.I = (this.height - this.M) / 2;
      this.a = this.E >= 440 ? 2 : 1;
      this.C = this.I + 111;
      this.z = this.I + this.M - 66;
      this.e = Math.max(1, (this.z - this.C) / 34);
      this.j = new TextInputWidget("Search items or namespace:item...", var1x -> {
         this.c = var1x;
         this.J = 0;
         this.TAsVoid();
      }, 96);
      this.j.W = this.c;
      this.j.bounds(this.f + 12, this.I + 40, this.E - 24);
      this.j.F = 24;
      this.Q.add(this.j);
      this.h = this.XAsQo("In inventory", () -> {
         this.zAsVoid();
         this.S = !this.S;
         this.J = 0;
         this.TAsVoid();
      });
      this.q = this.XAsQo("Selected", () -> {
         this.d = !this.d;
         this.J = 0;
         this.TAsVoid();
      });
      this.X = this.XAsQo("Add held", () -> {
         if (this.client.player != null && !this.client.player.getMainHandStack().isEmpty()) {
            String var1x = Registries.ITEM.getId(this.client.player.getMainHandStack().getItem()).toString();
            this.GAsVoid(() -> {
               LinkedHashSet var2x = new LinkedHashSet<>(this.n);
               var2x.add(var1x);
               this.MAsVoid(var2x);
            });
         } else {
            this.k = "Hold an item first.";
         }
      });
      this.u = this.XAsQo("Select results", () -> this.GAsVoid(() -> {
            LinkedHashSet var1x = new LinkedHashSet<>(this.n);
            this.x.forEach(var1xxx -> var1x.add(var1xxx.id));
            this.MAsVoid(var1x);
         }));
      this.Y = this.XAsQo("Clear", () -> this.GAsVoid(this.n::clear));
      this.N = this.XAsQo("Cancel", this::close);
      this.V = this.XAsQo("Done", () -> {
         try {
            this.R.KAsVoid(this.K, this.n);
            this.R.TAsVoid();
            ClientLog.XAsVoid("AutoSell", "Saved {} item selection; size={}", this.K ? "excluded" : "selected", this.n.size());
            this.client.setScreen(this.g);
         } catch (IllegalArgumentException var2x) {
            this.k = var2x.getMessage();
         }
      });
      this.V.p = true;
      int var1 = (this.E - 32) / 3;
      this.h.bounds(this.f + 12, this.I + 73, var1);
      this.q.bounds(this.f + 16 + var1, this.I + 73, var1);
      this.X.bounds(this.f + 20 + var1 * 2, this.I + 73, var1);
      int var2 = this.I + this.M - 31;
      int var3 = (this.E - 36) / 4;
      this.u.bounds(this.f + 12, var2, var3);
      this.Y.bounds(this.f + 16 + var3, var2, var3);
      this.N.bounds(this.f + 20 + var3 * 2, var2, var3);
      this.V.bounds(this.f + 24 + var3 * 3, var2, var3);
      this.TAsVoid();
      this.b = this.j;
      this.j.y = true;
   }

   private ActionButton XAsQo(String var1, Runnable var2) {
      ActionButton var3 = new ActionButton(var1, var2);
      this.Q.add(var3);
      return var3;
   }

   private void MAsVoid(Collection<String> var1) {
      List var2 = SelectionSettings.cAsList(var1);
      this.n.clear();
      this.n.addAll(var2);
   }

   private void GAsVoid(Runnable var1) {
      try {
         var1.run();
         this.k = "";
         this.TAsVoid();
      } catch (IllegalArgumentException var3) {
         this.k = var3.getMessage();
      }
   }

   private void TAsVoid() {
      String[] var1 = this.c.strip().toLowerCase(Locale.ROOT).replace('_', ' ').split("\\s+");
      this.x = this.W
         .stream()
         .filter(var1x -> !this.S || this.G.contains(var1x.id))
         .filter(var1x -> !this.d || this.n.contains(var1x.id))
         .filter(var1xx -> Arrays.stream(var1).allMatch(var1xx.search::contains))
         .sorted(Comparator.comparing(var1x -> !this.n.contains(var1x.id)))
         .toList();
      this.J = Math.clamp((long)this.J, 0, Math.max(0, (this.x.size() + this.a - 1) / this.a - this.e));
      this.h.i = this.S;
      this.q.i = this.d;
      this.KAsVoid();
      this.FAsVoid();
   }

   private void KAsVoid() {
      SelectionSettings.Type_r var1 = this.R.iAsP7_Type_r();
      int var2 = !var1.error().isEmpty() ? 1 : 0;
      SelectionSettings.Type_r var3 = new SelectionSettings.Type_r(
         var2 != 0 ? SelectionSettings.F.getFirst() : var1.mode(),
         (Set<String>)(this.K ? var1.selected() : this.n),
         (Set<String>)(this.K ? this.n : var1.excluded()),
         var2 != 0 || var1.hotbar(),
         var2 != 0 || var1.named(),
         var2 != 0 || var1.enchanted(),
         var2 != 0 || var1.filled(),
         ""
      );
      HashMap<String, Integer> var4 = new HashMap<>();
      if (this.client.player != null) {
         for (int var5 = 0; var5 < 36; var5++) {
            SelectionSettings.Type_o var6 = AutoSell.aAsP7_Type_o(this.client.player.getInventory().getStack(var5), var5);
            if (var3.eligible(var6)) {
               var4.merge(var6.id(), 1, Integer::sum);
            }
         }
      }

      this.T = Map.copyOf(var4);
   }

   private void FAsVoid() {
      if (this.b != null && this.P.contains(this.b)) {
         this.b.hAsVoid();
         this.b = null;
      }

      this.Q.removeAll(this.P);
      this.P.clear();
      int var1 = (this.E - 28) / this.a;

      for (int var2 = this.J * this.a; var2 < Math.min(this.x.size(), (this.J + this.e) * this.a); var2++) {
         AuctionItemSelectionPanel.Type_u var3 = new AuctionItemSelectionPanel.Type_u(this.x.get(var2));
         int var4 = var2 - this.J * this.a;
         var3.bounds(this.f + 12 + var4 % this.a * var1, this.C + var4 / this.a * 34, var1 - 4);
         var3.F = 30;
         this.P.add(var3);
         this.Q.add(var3);
      }
   }

   public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
      double var5 = this.L.bAsDouble(1.0, 200);
      GuiTextureDrawing.CAsVoid(context, 0, 0, this.width, this.height, -1609560044);

      try (qg var7 = qg.nAsQg(var5)) {
         GuiDrawing.OAsVoid(context, this.f, this.I, this.E, this.M, bd.l, bd.m, 0.6);
         TextDrawing.NAsVoid(context, this.K ? "Never sell" : "Choose sell items", this.f + 12, this.I + 12, bd.u, TextDrawing.Type_s.TITLE);
         String var8 = this.n.size() + " / 32";
         TextDrawing.UAsVoid(context, var8, this.f + this.E - 12 - TextDrawing.UAsInt(var8), this.I + 16, bd.e);

         for (GuiWidget var10 : this.Q) {
            if (!this.P.contains(var10)) {
               var10.render(context, mouseX, mouseY);
            }
         }

         context.enableScissor(this.f + 8, this.C, this.f + this.E - 8, this.z);

         try {
            for (GuiWidget var20 : this.P) {
               var20.render(context, mouseX, mouseY);
            }
         } finally {
            context.disableScissor();
         }

         if (this.x.isEmpty()) {
            TextDrawing.UAsVoid(context, "No matching items", this.f + 16, this.C + 12, bd.e);
         }

         int var19 = (this.x.size() + this.a - 1) / this.a * 34;
         GuiDrawing.aAsVoid(context, this.f + this.E - 7, this.C, Math.max(1, this.z - this.C), var19, this.J * 34);
         String var21 = !this.k.isEmpty()
            ? this.k
            : this.x.size() + " results, " + this.T.values().stream().mapToInt(Integer::intValue).sum() + " eligible inventory stacks. Escape cancels.";
         TextDrawing.NAsVoid(
            context,
            TextDrawing.zAsString(var21, this.E - 24, TextDrawing.Type_s.HELP),
            this.f + 12,
            this.I + this.M - 50,
            this.k.isEmpty() ? bd.e : -24915,
            TextDrawing.Type_s.HELP
         );
      }
   }

   @Override
   protected boolean accepts(GuiWidget var1, double var2, double var4) {
      return super.accepts(var1, var2, var4) && (!this.P.contains(var1) || var4 >= (double)this.C && var4 < (double)this.z);
   }

   @Override
   public boolean mouseClicked(Click click, boolean doubled) {
      return super.mouseClicked(click, doubled);
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      if (!(mouseX < (double)this.f) && !(mouseX >= (double)(this.f + this.E)) && !(mouseY < (double)this.C) && !(mouseY >= (double)this.z)) {
         this.J = this.J - (int)Math.signum(verticalAmount) * 2;
         this.TAsVoid();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean keyPressed(KeyInput input) {
      if (input.key() == 256) {
         this.close();
         return true;
      } else if (this.b != this.j && (input.key() == 266 || input.key() == 267 || input.key() == 264 || input.key() == 265)) {
         this.J = this.J + (input.key() == 266 ? -this.e : (input.key() == 267 ? this.e : (input.key() == 264 ? 1 : -1)));
         this.TAsVoid();
         return true;
      } else {
         return super.keyPressed(input);
      }
   }

   public void close() {
      ClientLog.XAsVoid("AutoSell", "Item picker draft discarded");
      this.client.setScreen(this.g);
   }

   private static record Type_n(String id, String name, ItemStack icon, String search) {
   }

   private final class Type_u extends GuiWidget {
      private final AuctionItemSelectionPanel.Type_n L;
      private final w2 X = new w2(0.0);

      Type_u(AuctionItemSelectionPanel.Type_n var2) {
         this.L = var2;
      }

      @Override
      public void render(DrawContext var1, int var2, int var3) {
         boolean var4 = AuctionItemSelectionPanel.this.n.contains(this.L.id);
         double var5 = this.X.bAsDouble(!this.contains((double)var2, (double)var3) && !this.y ? 0.0 : 1.0, 160);
         GuiDrawing.CAsVoid(var1, this.k, this.x, this.V, this.F, bd.T, bg.xAsInt(var4 ? bd.y : bd._Z, bd.h, var5 * 0.6));
         if (!this.L.icon.isEmpty()) {
            var1.drawItem(this.L.icon, this.k + 7, this.x + 7);
         }

         TextDrawing.UAsVoid(var1, TextDrawing.HAsString(this.L.name, this.V - 54), this.k + 29, this.x + 5, bd.u);
         TextDrawing.NAsVoid(
            var1, TextDrawing.zAsString(this.L.id, this.V - 54, TextDrawing.Type_s.HELP), this.k + 29, this.x + 18, bd.e, TextDrawing.Type_s.HELP
         );
         if (var4) {
            TextDrawing.UAsVoid(var1, "+", this.k + this.V - 15, this.x + 10, bd.tAsInt(this.k));
         }

         if (this.y && this.Q) {
            GuiDrawing.dAsVoid(var1, this.k, this.x, this.V, this.F, bd.T);
         }

         if (this.contains((double)var2, (double)var3)) {
            String var7 = this.L.id
               + (
                  AuctionItemSelectionPanel.this.G.contains(this.L.id)
                     ? " | " + AuctionItemSelectionPanel.this.T.getOrDefault(this.L.id, 0) + " eligible inventory stacks"
                     : ""
               )
               + (
                  AuctionItemSelectionPanel.this.R.zAsSet(!AuctionItemSelectionPanel.this.K).contains(this.L.id)
                     ? (AuctionItemSelectionPanel.this.K ? " | Also selected for selling; exclusion wins" : " | Excluded from selling")
                     : ""
               );
            var1.drawTooltip(Text.literal(var7), var2, var3);
         }
      }

      private void dAsVoid() {
         boolean var1 = this.Q;
         AuctionItemSelectionPanel.this.GAsVoid(() -> {
            LinkedHashSet var1x = new LinkedHashSet<>(AuctionItemSelectionPanel.this.n);
            if (!var1x.remove(this.L.id)) {
               var1x.add(this.L.id);
            }

            AuctionItemSelectionPanel.this.MAsVoid(var1x);
         });

         for (GuiWidget var3 : AuctionItemSelectionPanel.this.P) {
            if (var3 instanceof AuctionItemSelectionPanel.Type_u var4 && var4.L.id.equals(this.L.id)) {
               AuctionItemSelectionPanel.this.b = var4;
               var4.y = true;
               var4.Q = var1;
               break;
            }
         }

         GuiSounds.rAsVoid(GuiSounds.Type_o.CLICK);
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (var5 == 0 && this.contains(var1, var3)) {
            this.dAsVoid();
            return true;
         } else {
            return false;
         }
      }

      @Override
      public boolean key(int var1) {
         if (!this.y || var1 != 257 && var1 != 32) {
            return false;
         } else {
            this.dAsVoid();
            return true;
         }
      }
   }
}
