package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.EffectDrawBuffers;
import com.larpclient.GlyphAtlas;
import com.larpclient.UiStyle;
import com.larpclient.ColorUtil;
import com.larpclient.MediaSession;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

public final class LineGlyphs extends Module {
   private static LineGlyphs P;
   private final NumberSetting count = this.addSetting(new NumberSetting("Count", "Active line glyphs", 70.0, 10.0, 200.0, 5.0));
   private final BooleanSetting slowSpeed = this.addSetting(new BooleanSetting("Slow Speed", "Slow the segment growth", false));
   private final ModeSetting color = this.addSetting(new ModeSetting("Color", "Glyph palette", "Theme", "Theme", "Rainbow", "Single"));
   private final ArrayList<LineGlyphs.Type_x> x = new ArrayList<>();
   private final Random Q = new Random(40506L);
   private Object c;
   private Object G;
   private Vec3d I;

   public LineGlyphs() {
      super("Line Glyphs", "Show animated angular lines around you", Category.VISUALS, -1);
      P = this;
   }

   public static LineGlyphs LAsLineGlyphs() {
      return P;
   }

   public void LAsVoid() {
      this.x.clear();
      this.c = this.G = null;
      this.I = null;
   }

   @Override
   public void onEnable() {
      this.LAsVoid();
      ClientLog.GAsVoid("LineGlyphs", "Enabled; bounded world-fixed glyphs, count={}", this.count.getValue());
   }

   @Override
   public void onDisable() {
      this.LAsVoid();
   }

   @Override
   public void onTick() {
      if (this.isRuntimeActive() && this.isInGame()) {
         Vec3d var1 = this.getPlayer().getEntityPos();
         double var14;
         if (this.c != getMc().world
            || this.G != this.getPlayer()
            || this.I != null && ((var14 = var1.squaredDistanceTo(this.I) - 256.0) == 0.0 ? 0 : (var14 < 0.0 ? -1 : 1)) > 0) {
            this.LAsVoid();
         }

         this.c = getMc().world;
         this.G = this.getPlayer();
         this.I = var1;
         double var2 = mb.dAsDouble();
         this.x.removeIf(var2xx -> var2xx.TAsBoolean(var2));
         int var4 = this.count.getValue().intValue();

         while (this.x.size() > var4) {
            this.x.removeFirst();
         }

         for (int var5 = 0; var5 < 8 && this.x.size() < var4; var5++) {
            double var6 = (double)((Integer)getMc().options.getFov().getValue()).intValue();
            double var8 = Math.toRadians((double)this.getPlayer().getYaw() + (this.Q.nextDouble() - 0.5) * var6 * 1.5);
            int var10 = this.Q.nextInt(6, 24);
            Vec3d var11 = new Vec3d(
               (double)((int)var1.x - (int)(Math.sin(var8) * (double)var10)),
               (double)((int)var1.y + this.Q.nextInt(12)),
               (double)((int)var1.z + (int)(Math.cos(var8) * (double)var10))
            );
            this.x.add(new LineGlyphs.Type_x(var11, this.Q.nextInt(7, 13), var2, this.Q));
         }

         for (LineGlyphs.Type_x var13 : this.x) {
            var13.NAsVoid(var2, this.slowSpeed.getValue(), this.Q);
         }
      } else {
         this.LAsVoid();
      }
   }

   public boolean xAsBoolean(double var1) {
      if (!this.isEnabled()) {
         return false;
      } else if (this.isRuntimeActive() && this.isInGame() && this.c == getMc().world) {
         int var3 = 0;

         for (LineGlyphs.Type_x var5 : this.x) {
            double var6 = var5.uAsDouble(var1);

            for (int var8 = 1; var8 < var5.L.size() && var6 > 0.0; var8++) {
               int var9 = this.vAsInt(var3 + var8 * 180, var1, var6 * (0.25 + ((double)var8 - 1.0) / (double)var5.L.size() / 1.75));
               int var10 = this.vAsInt(var3 + (var8 + 1) * 180, var1, var6 * (0.25 + (double)var8 / (double)var5.L.size() / 1.75));
               if ((var9 | var10) >>> 24 != 0 && var5.L.get(var8 - 1).squaredDistanceTo(this.FAsVec3d(var5, var8)) >= 1.0E-10) {
                  return true;
               }
            }

            var3++;
         }

         return false;
      } else {
         return false;
      }
   }

   private Vec3d FAsVec3d(LineGlyphs.Type_x var1, int var2) {
      Vec3d var3 = var1.L.get(var2);
      return var2 == var1.L.size() - 1
         ? var1.L
            .get(var2 - 1)
            .lerp(
               var3,
               Math.clamp(1.0 - (double)((float)var1.Y - mb.LAsFloat() * (float)(this.slowSpeed.getValue() ? 1 : 2)) / Math.max(1.0, (double)var1.b), 0.0, 1.0)
            )
         : var3;
   }

   public void CAsVoid(MatrixStack var1, EffectDrawBuffers var2, CameraRenderState var3, double var4, boolean var6) {
      if (this.isEnabled() && this.isRuntimeActive() && this.c == getMc().world) {
         GlyphAtlas var7 = new GlyphAtlas(var1, var2, var3);
         int var8 = 0;

         for (LineGlyphs.Type_x var10 : this.x) {
            double var11 = var10.uAsDouble(var4);
            double var13 = (0.15 + 2.85 * Math.clamp(1.0 - var10.o.distanceTo(var3.pos) / 20.0, 0.0, 1.0)) * 0.008;

            for (int var15 = 1; var15 < var10.L.size(); var15++) {
               Vec3d var16 = var10.L.get(var15 - 1);
               Vec3d var17 = this.FAsVec3d(var10, var15);
               int var18 = this.vAsInt(var8 + var15 * 180, var4, var11 * (0.25 + ((double)var15 - 1.0) / (double)var10.L.size() / 1.75));
               int var19 = this.vAsInt(var8 + (var15 + 1) * 180, var4, var11 * (0.25 + (double)var15 / (double)var10.L.size() / 1.75));
               if (!var6) {
                  var7.aAsVoid(var16, var17, var13 * 3.0, bg.pAsInt(var18, 0.35), bg.pAsInt(var19, 0.35));
               }

               var7.aAsVoid(var16, var17, var13, var18, var19);
               int var20 = bg.xAsInt(var18, var18 & 0xFF000000 | 16777215, 0.28);
               int var21 = bg.xAsInt(var19, var19 & 0xFF000000 | 16777215, var15 == var10.L.size() - 1 ? 0.6 : 0.35);
               var7.aAsVoid(var16, var17, var13 * 0.42, var20, var21);
            }

            var8++;
         }
      }
   }

   private int vAsInt(int var1, double var2, double var4) {
      int var6 = this.color.pAsBoolean("Rainbow")
         ? bg.HSBtoRGB((float)((var2 * 0.1 + (double)var1 / 360.0) % 1.0), 1.0F, 1.0F)
         : (this.color.pAsBoolean("Single") ? bd.w : bg.xAsInt(bd.w, bd.Q, 0.5 + 0.5 * Math.sin(var2 * 2.0 + (double)var1 * 0.02)));
      return bg.XAsInt(var6, var4);
   }

   private static final class Type_x {
      final Vec3d o;
      final ArrayList<Vec3d> L = new ArrayList<>();
      final double l;
      int z;
      int Y;
      int b;
      int e;
      int V;
      double n = Double.NaN;

      Type_x(Vec3d var1, int var2, double var3, Random var5) {
         this.o = var1;
         this.z = var2;
         this.l = var3;
         this.L.add(var1);
         this.e = var5.nextInt(4) * 90;
         this.V = var5.nextInt(-1, 2) * 90;
      }

      void NAsVoid(double var1, boolean var3, Random var4) {
         if (this.z == 0) {
            if (Double.isNaN(this.n)) {
               this.n = var1;
            }
         } else if (this.Y > 0) {
            this.Y = Math.max(0, this.Y - (var3 ? 1 : 2));
         } else {
            int var5 = this.e;
            int var6 = this.V;

            for (int var7 = 0; var7 < 150 && Math.abs(this.V - var6) != 90; var7++) {
               this.V = var4.nextInt(-2, 2) * 90;
            }

            for (int var9 = 0; var9 < 5 && Math.abs(this.e - var5) != 90; var9++) {
               this.e = var4.nextInt(4) * 90;
            }

            this.Y = this.b = var4.nextInt(3);
            int var10 = Math.max(1, this.Y);
            Vec3d var8 = this.L.getLast();
            this.L
               .add(
                  var8.add(
                     this.V == 0 ? (double)Math.round(-Math.sin(Math.toRadians((double)this.e)) * (double)var10) : 0.0,
                     (double)Math.round(Math.sin(Math.toRadians((double)this.V)) * (double)var10),
                     this.V == 0 ? (double)Math.round(Math.cos(Math.toRadians((double)this.e)) * (double)var10) : 0.0
                  )
               );
            this.z--;
         }
      }

      double uAsDouble(double var1) {
         return Double.isNaN(this.n) ? Math.clamp((var1 - this.l) / 0.6, 0.0, 1.0) : Math.clamp(1.0 - (var1 - this.n) / 0.4, 0.0, 1.0);
      }

      boolean TAsBoolean(double var1) {
         return !Double.isNaN(this.n) && var1 - this.n >= 0.4 || var1 - this.l > 5.0;
      }
   }
}
