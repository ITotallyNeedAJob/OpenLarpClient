package com.larpclient;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public final class HudEditorController {
   private final BooleanSupplier l;
   private final Consumer<HudRenderer.Type_m> M;
   private final Runnable p;
   private HudRenderer.Type_m s;
   private HudRenderer.Type_m w;
   private d7.Type_z D;
   private d7.Type_q O;
   private GuiWorkspace.Type_g m;
   private GuiWorkspace.Type_z e;
   private d7.Type_p V;
   private int y = -1;
   private int v = -1;
   private long R = GuiWorkspace.m;
   private boolean c;
   private boolean k;
   private double L;
   private double z;
   private String W = "";
   private boolean u;
   private final HudWidgetSettings X = new HudWidgetSettings(() -> this.c = true, this::DAsVoid);
   private final d_ b = new d_();
   private double Z;
   private double d;
   private boolean P;
   private long q;
   private boolean r;
   private Predicate<HudRenderer.Type_m> B = var0 -> false;
   private int F;
   private int S;
   private int C = -1;

   public HudEditorController() {
      this(
         () -> {
            ProfileManager var0 = LarpClient.CAsLarpClient().fAsBo();
            return var0.kAsBoolean(var0.ZAsString());
         },
         var0 -> {
            MinecraftClient var1 = MinecraftClient.getInstance();
            var1.setScreen(ClickGuiScreen.HAsPn(var1.currentScreen, var0));
         },
         () -> MinecraftClient.getInstance()
               .inGameHud
               .getChatHud()
               .addMessage(Text.literal("HUD save failed. Right-click an overlay and choose Retry save. See the client log."))
      );
      this.u = true;
      this.B = var0 -> LarpClient.CAsLarpClient().fAsBo().yAsN7().VAsBoolean(var0.a);
   }

   public HudEditorController(BooleanSupplier var1, Consumer<HudRenderer.Type_m> var2, Runnable var3) {
      this.l = var1;
      this.M = var2;
      this.p = var3;
   }

   public void NAsVoid(Predicate<HudRenderer.Type_m> var1) {
      this.r = true;
      this.B = Objects.requireNonNull(var1);
   }

   public void pAsVoid(HudRenderer.Type_m var1) {
      if (this.eAsBoolean()) {
         this.EAsVoid();
      }

      this.s = var1;
      this.V = null;
   }

   public int JAsInt() {
      return this.y;
   }

   public int fAsInt() {
      return this.v;
   }

   public HudRenderer.Type_m nAsLp_Type_m() {
      return this.s;
   }

   public void WAsVoid() {
      this.V = null;
      this.X.YAsVoid();
   }

   public boolean eAsBoolean() {
      return this.w != null;
   }

   public boolean SAsBoolean(double var1, double var3, int var5, int var6, List<HudRenderer.Type_p> var7) {
      if (!this.eAsBoolean() && (this.V == null || !this.V.contains(var1, var3, 0))) {
         if (this.u && this.X.rAsBoolean() && this.X.lAsD7_Type_p().contains(var1, var3, 0)) {
            return true;
         } else {
            List<HudRenderer.Type_p> var8 = var7.stream().filter(var1x -> this.r || !var1x.element().EAsBoolean()).toList();
            return var8.stream().anyMatch(var4 -> var4.bounds().contains(var1, var3, 0))
               ? true
               : this.u
                  && HudRenderer.S
                     .stream()
                     .filter(var1x -> !this.B.test(var1x))
                     .anyMatch(
                        var7x -> HudRenderer.qAsOptional(var8, var7x).map(var6xx -> d7.XAsD7_Type_p(var6xx, var5, var6).contains(var1, var3, 0)).orElse(false)
                     );
         }
      } else {
         return true;
      }
   }

   public boolean OAsBoolean(double var1, double var3, int var5, int var6, int var7, List<HudRenderer.Type_p> var8, boolean var9) {
      if (this.eAsBoolean()) {
         return true;
      } else if (var9) {
         this.V = null;
         return false;
      } else if (this.u && this.X.iAsBoolean(var1, var3, var5, var6, var7)) {
         return true;
      } else if (this.V != null && this.V.contains(var1, var3, 0) && var5 == 0) {
         int var15 = (int)(var3 - (double)this.V.y()) / 20;
         this.V = null;
         switch (var15) {
            case 0:
               this.s.xAsVoid(true);
               this.c = true;
               this.DAsVoid();
               this.s = null;
               break;
            case 1:
               this.DAsVoid();
               this.M.accept(this.s);
               break;
            case 2:
               this.s.AAsVoid();
               this.c = true;
               this.DAsVoid();
               break;
            case 3:
               this.DAsVoid();
         }

         return true;
      } else {
         this.V = null;
         if (var5 != 0 && var5 != 1) {
            return false;
         } else {
            List<HudRenderer.Type_p> var10 = var8.stream().filter(var1x -> this.r || !var1x.element().EAsBoolean()).toList();
            Optional<d7.Type_p> var11 = HudRenderer.qAsOptional(var10, this.s);
            HudRenderer.Type_m var12 = this.u && var5 == 0
               ? HudRenderer.S
                  .stream()
                  .filter(var1x -> !this.B.test(var1x))
                  .filter(
                     var7xx -> HudRenderer.qAsOptional(var10, var7xx)
                           .map(var6xxx -> d7.XAsD7_Type_p(var6xxx, var6, var7).contains(var1, var3, 0))
                           .orElse(false)
                  )
                  .reduce((var0, var1x) -> var1x)
                  .orElse(null)
               : null;
            if (var12 != null) {
               this.s = var12;
               var11 = HudRenderer.qAsOptional(var10, this.s);
            }

            if (var5 == 0 && this.s != null && !this.B.test(this.s) && var11.isPresent() && d7.XAsD7_Type_p(var11.get(), var6, var7).contains(var1, var3, 0)) {
               this.NAsVoid(this.s);
               this.F = this.s.PAsInt(var6);
               this.S = this.s.ZAsInt(var7);
               d7.Type_p var16 = var11.get();
               this.O = new d7.Type_q(var16, var1, var3, this.s.vAsDouble());
               this.L = (double)(var16.x() - this.s.PAsInt(var6)) / this.s.vAsDouble();
               this.z = (double)(var16.y() - this.s.ZAsInt(var7)) / this.s.vAsDouble();
               return true;
            } else {
               HudRenderer.Type_p var13 = d7.lAsObject(var10, var0 -> true, HudRenderer.Type_p::bounds, var1, var3);
               if (var13 == null) {
                  this.s = null;
                  if (this.u && var5 == 1) {
                     this.X.EAsVoid((int)var1, (int)var3, var6, var7);
                     return true;
                  } else {
                     return false;
                  }
               } else {
                  this.s = var13.element();
                  ClientLog.XAsVoid("HUD editor", "Selected {} in {}", this.s.a, this.r ? "workspace" : "chat");
                  if (var5 == 1) {
                     if (this.u) {
                        this.X.zAsVoid(this.s, (int)var1, (int)var3, var6, var7);
                        return true;
                     }

                     int var14 = this.W.isEmpty() ? 60 : 80;
                     this.V = new d7.Type_p(d7.LAsInt((int)var1, 142, var6), d7.LAsInt((int)var3, var14, var7), Math.min(142, var6), Math.min(var14, var7));
                  } else {
                     if (this.B.test(this.s)) {
                        return true;
                     }

                     this.NAsVoid(this.s);
                     this.F = this.s.PAsInt(var6);
                     this.S = this.s.ZAsInt(var7);
                     this.D = new d7.Type_z(var1 - (double)this.s.PAsInt(var6), var3 - (double)this.s.ZAsInt(var7));
                     this.b.oAsVoid((double)this.s.PAsInt(var6), (double)this.s.ZAsInt(var7));
                     this.Z = var1;
                     this.d = var3;
                     this.P = false;
                     this.q = System.nanoTime();
                  }

                  return true;
               }
            }
         }
      }
   }

   private void NAsVoid(HudRenderer.Type_m var1) {
      this.w = var1;
      this.C = -1;
      this.m = GuiWorkspace.B.get(var1.a);
      this.e = GuiWorkspace.v.get(var1.a);
   }

   public boolean QAsBoolean(double var1, double var3, int var5, int var6, int var7, boolean var8) {
      return this.UAsBoolean(var1, var3, var5, var6, var7, var8, false);
   }

   public boolean UAsBoolean(double var1, double var3, int var5, int var6, int var7, boolean var8, boolean var9) {
      if (this.u && var5 == 0 && this.X.CAsBoolean(var1, var3, var6, var7)) {
         return true;
      } else if (this.eAsBoolean() && var5 == 0) {
         if (this.O != null) {
            this.w.iAsVoid(this.u ? d_.YAsDouble(this.O, var1, var3) : this.O.scale(var1, var3));
            this.w
               .SAsVoid(
                  (int)Math.round((double)this.O.bounds().x() - this.L * this.w.vAsDouble()),
                  (int)Math.round((double)this.O.bounds().y() - this.z * this.w.vAsDouble()),
                  var6,
                  var7
               );
         } else {
            if (this.u) {
               this.Z = var1;
               this.d = var3;
               this.P = var8;
               return true;
            }

            d7.Type_d var10 = this.D
               .move(var1, var3, this.w.hAsD7_Type_p(var6, var7), var6, var7, GuiWorkspace.c.snapping(), GuiWorkspace.c.grid(), var8, this.y, this.v);
            int var11 = var10.x();
            int var12 = var10.y();
            if (this.r && var9) {
               int var13 = var11 - this.F;
               int var14 = var12 - this.S;
               if (this.C < 0 && Math.max(Math.abs(var13), Math.abs(var14)) >= 3) {
                  this.C = Math.abs(var13) >= Math.abs(var14) ? 0 : 1;
               }

               if (this.C == 0) {
                  var12 = this.S;
               } else if (this.C == 1) {
                  var11 = this.F;
               }
            } else {
               this.C = -1;
            }

            this.w.SAsVoid(var11, var12, var6, var7);
            this.y = var9 && this.C == 1 ? -1 : var10.guideX();
            this.v = var9 && this.C == 0 ? -1 : var10.guideY();
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean NAsBoolean(int var1) {
      if (this.u && var1 == 0 && this.X.cAsBoolean()) {
         return true;
      } else if (this.eAsBoolean() && var1 == 0) {
         this.EAsVoid();
         return true;
      } else {
         return false;
      }
   }

   private void EAsVoid() {
      if (this.w != null) {
         this.c = this.c | (!Objects.equals(this.m, GuiWorkspace.B.get(this.w.a)) || !Objects.equals(this.e, GuiWorkspace.v.get(this.w.a)));
         ClientLog.XAsVoid("HUD editor", "Released {} gesture for {}", this.O == null ? "move" : "scale", this.w.a);
      }

      this.w = null;
      this.D = null;
      this.O = null;
      this.m = null;
      this.e = null;
      this.y = this.v = -1;
      this.DAsVoid();
   }

   private void DAsVoid() {
      if (this.c) {
         if (this.l.getAsBoolean()) {
            this.c = false;
            this.W = "";
            ClientLog.XAsVoid("HUD editor", "Committed {} edit to active profile", this.r ? "workspace" : "chat");
         } else {
            if (this.W.isEmpty()) {
               this.p.run();
            }

            this.W = "HUD save failed. Retry save in the menu.";
            ClientLog.oAsVoid("HUD editor", "HUD save failed; pending changes retained for retry/close");
         }
      }
   }

   public void eAsVoid(boolean var1, boolean var2) {
      if (this.R != GuiWorkspace.m) {
         this.X.KAsVoid();
         this.w = null;
         this.D = null;
         this.O = null;
         this.c = false;
         this.W = "";
         this.y = this.v = -1;
         this.s = null;
         this.V = null;
         this.R = GuiWorkspace.m;
         ClientLog.XAsVoid("HUD editor", "Released chat editing after profile replacement");
      }

      if (var1 && var2) {
         this.k = false;
      } else {
         if (!this.k) {
            this.xAsVoid();
         }

         this.k = true;
      }
   }

   public void xAsVoid() {
      this.X.YAsVoid();
      this.EAsVoid();
      this.s = null;
      this.V = null;
   }

   public boolean GAsBoolean(int var1) {
      return this.u && this.X.YAsBoolean(var1);
   }

   public boolean lAsBoolean(int var1) {
      return this.u && this.X.DAsBoolean(var1);
   }

   public boolean GAsBoolean(double var1, double var3, double var5, boolean var7) {
      return this.u && !var7 && this.X.fAsBoolean(var1, var3, var5);
   }

   public void mAsVoid(DrawContext var1, int var2, int var3, int var4, int var5, boolean var6) {
      if (this.u && this.w != null && this.D != null) {
         long var7 = System.nanoTime();
         d7.Type_d var9 = this.D
            .move(this.Z, this.d, this.w.hAsD7_Type_p(var4, var5), var4, var5, GuiWorkspace.c.snapping(), GuiWorkspace.c.grid(), this.P, this.y, this.v);
         d7.Type_d var10 = this.b
            .LAsD7_Type_d((double)var9.x(), (double)var9.y(), MinecraftClient.getInstance().isShiftPressed(), (double)(var7 - this.q) / 1.0E9);
         this.q = var7;
         this.w.SAsVoid(var10.x(), var10.y(), var4, var5);
         this.y = var9.guideX();
         this.v = var9.guideY();
      }

      List<HudRenderer.Type_p> var12 = HudRenderer.pAsList();
      Optional<d7.Type_p> var8 = HudRenderer.qAsOptional(var12, this.s);
      if (this.s != null && var8.isEmpty() && !this.eAsBoolean()) {
         this.s = null;
         this.V = null;
      }

      HudRenderer.Type_p var13 = var6 ? null : d7.lAsObject(var12, var0 -> true, HudRenderer.Type_p::bounds, (double)var2, (double)var3);
      if (var13 != null && var13.element() != this.s) {
         gAsVoid(var1, var13.bounds(), 1620481696);
      }

      if (var8.isPresent()) {
         d7.Type_p var14 = var8.get();
         gAsVoid(var1, var14, bg.XAsInt(bd.w, 0.8));
         if (!this.B.test(this.s)) {
            d7.Type_p var11 = d7.XAsD7_Type_p(var14, var4, var5);
            GuiTextureDrawing.AAsVoid(var1, (float)var11.x(), (float)var11.y(), (float)var11.width(), (float)var11.height(), 2.0F, bd.w);
         }
      }

      if (this.eAsBoolean()) {
         if (this.u) {
            for (int var15 = 0; var15 < var4; var15 += 40) {
               GuiTextureDrawing.CAsVoid(var1, var15, 0, 1, var5, var15 % 200 == 0 ? 721420287 : 385875967);
            }

            for (int var16 = 0; var16 < var5 - 14; var16 += 40) {
               GuiTextureDrawing.CAsVoid(var1, 0, var16, var4, 1, var16 % 200 == 0 ? 721420287 : 385875967);
            }

            Optional<d7.Type_p> var17 = HudRenderer.qAsOptional(var12, this.w);
            if (var17.isPresent()) {
               GuiTextureDrawing.CAsVoid(var1, var17.get().x() + var17.get().width() / 2, 0, 1, var5, bg.XAsInt(bd.w, 0.6));
               GuiTextureDrawing.CAsVoid(var1, 0, var17.get().y() + var17.get().height() / 2, var4, 1, bg.XAsInt(bd.w, 0.6));
            }
         }

         if (this.y >= 0) {
            GuiTextureDrawing.CAsVoid(var1, Math.min(var4 - 1, this.y), 0, 1, var5, bg.XAsInt(bd.w, 0.3));
         }

         if (this.v >= 0) {
            GuiTextureDrawing.CAsVoid(var1, 0, Math.min(var5 - 1, this.v), var4, 1, bg.XAsInt(bd.w, 0.3));
         }
      }

      if (this.u) {
         for (HudRenderer.Type_m var20 : HudRenderer.S) {
            if (!this.B.test(var20)) {
               HudRenderer.qAsOptional(var12, var20)
                  .ifPresent(
                     var5xx -> {
                        d7.Type_p var6x = d7.XAsD7_Type_p(var5xx, var4, var5);
                        GuiTextureDrawing.AAsVoid(
                           var1,
                           (float)var6x.x(),
                           (float)var6x.y(),
                           (float)var6x.width(),
                           (float)var6x.height(),
                           2.0F,
                           bg.XAsInt(bd.w, var6x.contains((double)var2, (double)var3, 0) ? 0.6 : 0.35)
                        );
                        GuiTextureDrawing.CAsVoid(var1, var6x.x() + 2, var6x.y() + var6x.height() - 2, var6x.width() - 4, 1, -1073741825);
                        GuiTextureDrawing.CAsVoid(var1, var6x.x() + var6x.width() - 2, var6x.y() + 2, 1, var6x.height() - 4, -1073741825);
                     }
                  );
            }
         }

         this.X.rAsVoid(var1, var2, var3);
      }

      if (this.V != null) {
         GuiTextureDrawing.AAsVoid(var1, (float)this.V.x(), (float)this.V.y(), (float)this.V.width(), (float)this.V.height(), 6.0F, -267645934);
         String[] var19 = new String[]{"Hide", "Settings", "Reset placement", "Retry save"};

         for (int var21 = 0; var21 < this.V.height() / 20; var21++) {
            if (!var6 && this.V.contains((double)var2, (double)var3, 0) && (var3 - this.V.y()) / 20 == var21) {
               GuiTextureDrawing.AAsVoid(var1, (float)(this.V.x() + 2), (float)(this.V.y() + var21 * 20), (float)(this.V.width() - 4), 20.0F, 4.0F, 905969663);
            }

            GuiIcons.aAsVoid(
               var1,
               GuiIcons.PAsIdentifier(new String[]{"eye", "settings", "rotate-ccw", "badge-check"}[var21]),
               this.V.x() + 7,
               this.V.y() + var21 * 20 + 4,
               12,
               bd.w
            );
            ww.fAsVoid(var1, var19[var21], (double)(this.V.x() + 25), (double)(this.V.y() + var21 * 20 + 6), 11.0, -1);
         }
      }
   }

   private static void gAsVoid(DrawContext var0, d7.Type_p var1, int var2) {
      GuiTextureDrawing.pAsVoid(var0, var1.x() - 1, var1.y() - 1, var1.width() + 2, var1.height() + 2, var2);
   }
}
