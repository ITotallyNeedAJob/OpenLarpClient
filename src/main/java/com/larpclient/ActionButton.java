package com.larpclient;

import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public final class ActionButton extends GuiWidget {
   public String X;
   public boolean i;
   public boolean o;
   public boolean e;
   public boolean p;
   public boolean A;
   public boolean N;
   public Boolean g;
   public Runnable z;
   public GuiSounds.Type_o PetEffectRenderer = GuiSounds.Type_o.CLICK;
   public String E = "";
   public String q = "";
   public String D = "";
   public boolean r;
   public boolean t;
   public boolean M;
   public boolean W;
   public boolean K;
   public boolean l;
   public boolean u;
   public double h = 1.0;
   public double H = 1.0;
   public boolean v;
   private final Runnable G;
   private final w2 BloomUtility = new w2(0.0);
   private final w2 MenuPopupProvider = new w2(0.0);
   private final w2 J = new w2(0.0);
   private final w2 I = new w2(0.0);
   private boolean U;
   private mi R = mi.atAsMi(0.0, 0.0, bd.tAsInt(this.k));
   private long j;
   private long c;
   private float s;
   private float n;
   private float S;

   public mi IAsMi() {
      return this.R;
   }

   public float JAsFloat() {
      return this.S;
   }

   public double bAsDouble(int var1, int var2) {
      return this.b.bAsDouble(this.contains((double)var1, (double)var2) || this.y && this.Q ? 1.0 : 0.0, 160);
   }

   public ActionButton(String var1, Runnable var2) {
      this.X = var1;
      this.G = var2;
   }

   public static int rAsInt(int var0) {
      return (var0 < 144 ? 18 : 34) + 14;
   }

   public int eAsInt() {
      return Math.max(1, this.V - (this.t ? 82 : 54));
   }

   public int lAsInt() {
      return this.x + (this.F - 13) / 2;
   }

   @Override
   public void render(DrawContext var1, int var2, int var3) {
      if (this.Y) {
         double var4 = this.bAsDouble(var2, var3);
         double var6 = this.f.bAsDouble(this.i && !this.N ? 1.0 : 0.0, 180);
         double var8 = this.I.bAsDouble(System.nanoTime() < this.j ? 1.0 : 0.0, 100);
         if (this.l) {
            int var35 = this.p ? (this.A ? -24915 : mi.readableAccent(bd.tAsInt(this.k), bd.m)) : bd._Z;
            if (!this.B) {
               var4 = 0.0;
            }

            int var36 = bd.T;
            if (this.p && bd.hAsBoolean()) {
               GuiDrawing.HAsVoid(var1, (float)this.k, (float)this.x, (float)this.V, (float)this.F, (float)var36, 5.0F, bg.XAsInt(var35, 0.18 + 0.14 * var4));
            }

            if (!this.p && bd.hAsBoolean()) {
               if (var4 > 0.001) {
                  GuiDrawing.CAsVoid(var1, this.k, this.x, this.V, this.F, var36, bg.XAsInt(bd.u, var4 * 0.045));
               }

               RoundedShapePipeline.SAsVoid(
                  var1,
                  (float)this.k,
                  (float)this.x,
                  (float)this.V,
                  (float)this.F,
                  (float)var36,
                  bg.pAsInt(bd.I, 1.3 + var4 * 0.7),
                  bg.pAsInt(bd.a, 1.6 + var4 * 0.9)
               );
            } else {
               GuiDrawing.CAsVoid(var1, this.k, this.x, this.V, this.F, var36, bg.xAsInt(var35, bd.h, var4 * 0.15));
            }

            int var42 = !this.B ? bd.W : (this.p ? mi.controlInk(var35) : (this.A ? -24915 : bg.xAsInt(bd.e, bd.u, var4)));
            if (!this.q.isEmpty()) {
               GuiIcons.aAsVoid(var1, GuiIcons.PAsIdentifier(this.q), this.k + (this.V - 10) / 2, this.x + (this.F - 10) / 2, 10, var42);
            } else {
               String var44 = TextDrawing.zAsString(this.X, this.V - 8, TextDrawing.Type_s.HELP);
               int var46 = bd.hAsBoolean() ? this.k + (this.V - TextDrawing.jAsInt(var44, TextDrawing.Type_s.HELP)) / 2 : this.k + 4;
               TextDrawing.NAsVoid(var1, var44, var46, this.x + (this.F - TextDrawing.Type_s.HELP.zAsInt()) / 2, var42, TextDrawing.Type_s.HELP);
            }

            if (this.y && this.Q) {
               GuiDrawing.dAsVoid(var1, this.k, this.x, this.V, this.F, var36);
            }
         } else if (this.K) {
            int var34 = bd.hAsBoolean() ? bd.E : 6;
            if (bd.hAsBoolean()) {
               GuiDrawing.PAsVoid(var1, this.k, this.x, this.V, this.F, var34, bg.xAsInt(bd.M, bd.h, var4), 0.4 + 0.6 * var4);
            } else {
               GuiDrawing.CAsVoid(var1, this.k, this.x, this.V, this.F, var34, bg.xAsInt(bd.m, bd.h, var4 * 0.35));
            }

            int var11 = rAsInt(this.V);
            int var41 = var11 - 14;
            GuiDrawing.CAsVoid(var1, this.k + 7, this.x + 7, var41, var41, 6, bd.V);
            var1.drawTexture(
               RenderPipelines.GUI_TEXTURED,
               Identifier.of("minecraft", "textures/misc/unknown_server.png"),
               this.k + 7,
               this.x + 7,
               0.0F,
               0.0F,
               var41,
               var41,
               64,
               64,
               64,
               64,
               qg.iAsInt(-1)
            );
            TextDrawing.NAsVoid(
               var1, TextDrawing.zAsString(this.X, this.V - var11 - 7, TextDrawing.Type_s.MODULE), this.k + var11, this.x + 6, bd.u, TextDrawing.Type_s.MODULE
            );
            TextDrawing.NAsVoid(
               var1, TextDrawing.zAsString(this.E, this.V - var11 - 7, TextDrawing.Type_s.HELP), this.k + var11, this.x + 19, bd.e, TextDrawing.Type_s.HELP
            );
            if (this.y && this.Q) {
               GuiDrawing.dAsVoid(var1, this.k, this.x, this.V, this.F, var34);
            }
         } else if (this.M) {
            this.TAsVoid(var1, var4, var8);
         } else if (this.W) {
            this.BAsVoid(var1, var4, var6, var8);
         } else {
            if (!this.U && this.g != null) {
               this.J.aAsVoid(this.g ? 1.0 : 0.0);
               this.U = true;
            }

            double var10 = this.J.bAsDouble(Boolean.TRUE.equals(this.g) ? 1.0 : 0.0, 180);
            var1.getMatrices().pushMatrix();

            try {
               if (this.r && bd.hAsBoolean()) {
                  this.R = mi.atAsMi(var10, var4, bd.tAsInt(this.k));
                  bd.Type_p var39 = bd.yAsBd_Type_p();
                  int var43 = this.R.accent();
                  boolean var45 = var39.enabled().equals("Glow");
                  int var47 = !var45 && !var39.enabled().equals("Tint") ? 0 : 1;
                  int var16 = bd.E;
                  this.S = var39.hoverLift() ? (float)var4 : 0.0F;
                  if (this.S > 0.01F && !var39.card().equals("Flat")) {
                     RoundedShapePipeline.CAsVoid(
                        var1,
                        (float)this.k,
                        (float)this.x + 1.5F,
                        (float)this.V,
                        (float)this.F,
                        (float)var16,
                        8.0F,
                        bg.pAsInt(bd.k, 0.4 * (double)this.S),
                        bg.pAsInt(bd.k, 0.6 * (double)this.S)
                     );
                  }

                  var1.getMatrices().translate(0.0F, -this.S);
                  if (var45 && var10 > 0.01) {
                     GuiDrawing.HAsVoid(var1, (float)this.k, (float)this.x, (float)this.V, (float)this.F, (float)var16, 9.0F, bg.XAsInt(var43, 0.14 * var10));
                  }

                  String var49 = var39.card();
                  switch (var49) {
                     case "Outline":
                        if (var4 > 0.001) {
                           GuiDrawing.CAsVoid(var1, this.k, this.x, this.V, this.F, var16, bg.XAsInt(bd.u, 0.035 * var4));
                        }
                        break;
                     case "Flat":
                        GuiDrawing.CAsVoid(var1, this.k, this.x, this.V, this.F, var16, bg.pAsInt(this.R.background(), 0.75));
                        break;
                     default:
                        GuiDrawing.CAsVoid(var1, this.k, this.x, this.V, this.F, var16, this.R.background());
                  }

                  if (var47 != 0 && var10 > 0.01) {
                     RoundedShapePipeline.gAsVoid(
                        var1,
                        (float)this.k,
                        (float)this.x,
                        (float)this.V * 0.7F,
                        (float)this.F,
                        (float)var16,
                        bg.XAsInt(var43, (var45 ? 0.15 : 0.22) * var10),
                        bg.XAsInt(var43, 0.0)
                     );
                  }

                  if (!var39.card().equals("Flat")) {
                     double var48 = (var39.card().equals("Outline") ? 0.85 : 0.55) + 0.25 * var4;
                     int var19 = bg.xAsInt(bg.XAsInt(bd.A, var48), bg.XAsInt(var43, 0.55), var47 != 0 ? 0.8 * var10 : 0.0);
                     RoundedShapePipeline.SAsVoid(var1, (float)this.k, (float)this.x, (float)this.V, (float)this.F, (float)var16, var19, var19);
                  }

                  double var49_r2 = (double)(System.nanoTime() - this.c) / 6.5E8;
                  if (var39.ripple() && var49_r2 < 1.0) {
                     float var50 = 10.0F + (float)w2.Type_e.CUBIC_OUT.zAsDouble(var49_r2) * (float)this.V * 0.9F;
                     RoundedShapePipeline.pAsVoid(
                        var1,
                        (float)this.k,
                        (float)this.x,
                        (float)this.V,
                        (float)this.F,
                        (float)var16,
                        this.s,
                        this.n,
                        var50,
                        bg.XAsInt(var43, 0.22 * (1.0 - var49_r2))
                     );
                  }

                  if (var6 > 0.001) {
                     GuiDrawing.CAsVoid(var1, this.k, this.x, this.V, this.F, bd.E, bg.XAsInt(bd.u, 0.045 * var6));
                     RoundedShapePipeline.SAsVoid(
                        var1,
                        (float)this.k,
                        (float)this.x,
                        (float)this.V,
                        (float)this.F,
                        (float)bd.E,
                        bg.XAsInt(this.R.accent(), 0.28 * var6),
                        bg.XAsInt(this.R.accent(), 0.18 * var6)
                     );
                     GuiDrawing.aAsVoid(var1, this.k, this.x, this.V, this.F, bd.E, bg.XAsInt(this.R.accent(), var6));
                  }

                  int var51 = bg.xAsInt(bg.xAsInt(this.R.subtitle(), this.R.title(), 0.75 + 0.25 * var4), this.R.title(), var10);
                  if (var39.enabled().equals("Text")) {
                     var51 = bg.xAsInt(var51, var43, var10);
                  }

                  TextDrawing.NAsVoid(
                     var1,
                     TextDrawing.zAsString(this.X, this.eAsInt(), TextDrawing.Type_s.MODULE),
                     this.k + 12,
                     this.x + this.F / 2 - 12,
                     var51,
                     TextDrawing.Type_s.MODULE
                  );
                  TextDrawing.NAsVoid(
                     var1,
                     TextDrawing.zAsString(this.E, this.eAsInt(), TextDrawing.Type_s.BODY),
                     this.k + 12,
                     this.x + this.F / 2 + 2,
                     this.R.subtitle(),
                     TextDrawing.Type_s.BODY
                  );
                  GuiDrawing.FAsVoid(var1, this.k + this.V - 32, this.lAsInt(), var10, this.R.accent(), this.R.light());
               } else if (this.r) {
                  this.R = mi.atAsMi(var10, var4, bd.tAsInt(this.k));
                  GuiDrawing.OAsVoid(var1, this.k, this.x, this.V, this.F, bd.E, bg.xAsInt(this.R.background(), bd.y, var6), 0.9 + var4 * 0.6);
                  if (var6 > 0.001) {
                     RoundedShapePipeline.SAsVoid(
                        var1,
                        (float)this.k,
                        (float)this.x,
                        (float)this.V,
                        (float)this.F,
                        (float)bd.E,
                        bg.XAsInt(bd.u, 0.34 * var6),
                        bg.XAsInt(bd.u, 0.26 * var6)
                     );
                  }

                  TextDrawing.NAsVoid(
                     var1,
                     TextDrawing.zAsString(this.X, this.eAsInt(), TextDrawing.Type_s.MODULE),
                     this.k + 12,
                     this.x + this.F / 2 - 12,
                     this.R.title(),
                     TextDrawing.Type_s.MODULE
                  );
                  TextDrawing.NAsVoid(
                     var1,
                     TextDrawing.zAsString(this.E, this.eAsInt(), TextDrawing.Type_s.BODY),
                     this.k + 12,
                     this.x + this.F / 2 + 2,
                     this.R.subtitle(),
                     TextDrawing.Type_s.BODY
                  );
                  if (this.o) {
                     GuiIcons.aAsVoid(var1, GuiIcons.PAsIdentifier("star"), this.k + this.V - 27, this.x + (this.F - 11) / 2, 11, this.R.accent());
                  } else {
                     GuiDrawing.FAsVoid(var1, this.k + this.V - 32, this.lAsInt(), var10, this.R.accent(), this.R.light());
                  }
               } else if (this.u) {
                  if (var4 > 0.001) {
                     GuiDrawing.CAsVoid(var1, this.k, this.x, this.V, this.F, bd.T, bg.XAsInt(bd.u, var4 * 0.045));
                  }

                  RoundedShapePipeline.SAsVoid(
                     var1,
                     (float)this.k,
                     (float)this.x,
                     (float)this.V,
                     (float)this.F,
                     (float)bd.T,
                     bg.pAsInt(bd.I, 1.3 + var4 * 0.7),
                     bg.pAsInt(bd.a, 1.6 + var4 * 0.9)
                  );
                  String var38 = TextDrawing.HAsString(this.X, Math.max(1, this.V - 12));
                  TextDrawing.UAsVoid(
                     var1, var38, this.k + (this.V - TextDrawing.UAsInt(var38)) / 2, this.x + (this.F - 9) / 2, this.B ? bg.xAsInt(bd.e, bd.u, var4) : bd.W
                  );
               } else {
                  if (this.e) {
                     double var12 = Math.max(var6, var4 * 0.6);
                     if (var12 > 0.001) {
                        GuiDrawing.CAsVoid(var1, this.k, this.x, this.V, this.F, bd.T, bg.XAsInt(bd.u, 0.065 * var12));
                     }
                  } else {
                     GuiDrawing.OAsVoid(var1, this.k, this.x, this.V, this.F, bd.p, bg.xAsInt(bd.M, bd.h, Math.max(var6 * 0.65, var4)), 0.9 + var4 * 0.6);
                  }

                  int var37 = this.q.isEmpty() ? 8 : 27;
                  int var13 = Math.min(13, Math.max(1, this.F - 2));
                  if (!this.q.isEmpty()) {
                     GuiIcons.aAsVoid(
                        var1,
                        GuiIcons.PAsIdentifier(this.q),
                        this.k + (this.X.isEmpty() ? (this.V - var13) / 2 : 8),
                        this.x + (this.F - var13) / 2,
                        var13,
                        this.i ? bd.u : bg.xAsInt(bd.e, bd.u, var4)
                     );
                  }

                  int var14 = this.V - var37 - (this.g == null ? 8 : 34);
                  if (var14 > 4 && this.H > 0.001) {
                     if (this.v) {
                        var1.enableScissor(this.k + var37, this.x, this.k + this.V - 8, this.x + this.F);
                     }

                     try (qg var15 = qg.nAsQg(this.H)) {
                        TextDrawing.UAsVoid(
                           var1,
                           this.v ? this.X : TextDrawing.HAsString(this.X, var14),
                           this.k + var37,
                           this.x + (this.E.isEmpty() ? (this.F - 9) / 2 : 4),
                           this.B ? (this.A ? -24915 : (this.p ? bd.tAsInt(this.k) : (this.N && !this.i ? bg.xAsInt(bd.e, bd.u, var4) : bd.u))) : bd.e
                        );
                     } finally {
                        if (this.v) {
                           var1.disableScissor();
                        }
                     }
                  }

                  if (!this.E.isEmpty()) {
                     TextDrawing.UAsVoid(var1, TextDrawing.HAsString(this.E, this.V - 42), this.k + 8, this.x + 16, bd.e);
                  }

                  if (this.g != null) {
                     GuiDrawing.uAsVoid(var1, this.k + this.V - 32, this.x + (this.F - 12) / 2, var10);
                  }
               }

               int var40 = this.r ? bd.E : bd.T;
               if (var8 > 0.001) {
                  GuiDrawing.CAsVoid(var1, this.k, this.x, this.V, this.F, var40, bg.XAsInt(bd.u, var8 * 0.07));
               }

               if (this.y && this.Q) {
                  GuiDrawing.dAsVoid(var1, this.k, this.x, this.V, this.F, var40);
               }
            } finally {
               var1.getMatrices().popMatrix();
            }
         }
      }
   }

   private void BAsVoid(DrawContext var1, double var2, double var4, double var6) {
      double var8 = Math.max(var2, var6) * (1.0 - var4);
      if (var8 > 0.001) {
         GuiTextureDrawing.AAsVoid(var1, (float)this.k, (float)this.x, (float)this.V, (float)this.F, (float)bd.l, bg.XAsInt(bd.u, 0.05 * var8));
      }

      int var10 = bg.xAsInt(bd.e, bd.u, 0.45);
      int var11 = bg.xAsInt(bg.xAsInt(var10, bd.u, var2 * 0.85), bd.u, var4);
      int var12 = Math.min(12, this.F - 2);
      int var13 = bd.WAsInt(this.k, bd.m);
      if (mi.controlInk(bd.m) == -1) {
         var13 = bg.xAsInt(var13, -1, 0.12);
      }

      if (!this.q.isEmpty()) {
         GuiIcons.aAsVoid(
            var1, GuiIcons.PAsIdentifier(this.q), this.k + 8, this.x + (this.F - var12) / 2, var12, bg.xAsInt(bg.xAsInt(bd.e, bd.u, var2 * 0.7), var13, var4)
         );
      }

      if (this.H > 0.001) {
         var1.enableScissor(this.k + 27, this.x, this.k + this.V - 4, this.x + this.F);

         try (qg var15 = qg.nAsQg(this.H)) {
            TextDrawing.Type_s var16 = TextDrawing.Type_s.NAVIGATION;
            TextDrawing.NAsVoid(
               var1, TextDrawing.zAsString(this.X, Math.max(1, this.V - 27 - 4), var16), this.k + 27, this.x + (this.F - var16.zAsInt()) / 2, var11, var16
            );
         } finally {
            var1.disableScissor();
         }
      }

      if (this.y && this.Q) {
         GuiDrawing.dAsVoid(var1, this.k, this.x, this.V, this.F, bd.l);
      }
   }

   private void TAsVoid(DrawContext var1, double var2, double var4) {
      var1.getMatrices().pushMatrix();

      try {
         var1.getMatrices().translate((float)this.k + (float)this.V / 2.0F, (float)this.x + (float)this.F / 2.0F);
         float var6 = (float)(1.0 + 0.018 * var2 - 0.025 * var4);
         var1.getMatrices().scale(var6, var6);
         var1.getMatrices().translate((float)(-this.k) - (float)this.V / 2.0F, (float)(-this.x) - (float)this.F / 2.0F);
         GuiDrawing.CAsVoid(var1, this.k, this.x, this.V, this.F, 2, bg.pAsInt(bg.xAsInt(-2144326101, -1337177788, var2), this.h));
         int var7 = bg.pAsInt(this.B ? -1711656 : -6711922, this.h);
         int var8 = this.q.isEmpty() ? 7 : 22;
         if (!this.q.isEmpty()) {
            GuiIcons.aAsVoid(var1, GuiIcons.PAsIdentifier(this.q), this.k + 6, this.x + (this.F - 10) / 2, 10, var7);
         }

         TextDrawing.UAsVoid(
            var1, TextDrawing.HAsString(this.X, this.V - var8 - 6), this.k + var8 + (int)Math.round(var2 * 2.0), this.x + (this.F - 9) / 2, var7
         );
         if (this.y && this.Q) {
            GuiDrawing.vAsVoid(var1, this.k, this.x, this.V, this.F, bg.pAsInt(-1711656, this.h));
         }
      } finally {
         var1.getMatrices().popMatrix();
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (this.B && var5 == 0 && this.contains(var1, var3)) {
         this.j = System.nanoTime() + 110000000L;
         if (this.r) {
            this.c = System.nanoTime();
            this.s = (float)var1;
            this.n = (float)var3;
         }

         GuiSounds.rAsVoid(this.a);
         if (this.z != null && var1 >= (double)(this.k + this.V - 38)) {
            this.z.run();
         } else {
            this.G.run();
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (this.Y && this.y && this.B && (var1 == 257 || var1 == 32)) {
         this.j = System.nanoTime() + 110000000L;
         GuiSounds.rAsVoid(this.a);
         if (this.r && var1 == 32 && this.z != null) {
            this.z.run();
         } else {
            this.G.run();
         }

         return true;
      } else {
         return false;
      }
   }
}
