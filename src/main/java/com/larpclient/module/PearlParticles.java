package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.EffectDrawBuffers;
import com.larpclient.GlyphAtlas;
import com.larpclient.FadeCurve;
import com.larpclient.UiStyle;
import com.larpclient.ColorUtil;
import com.larpclient.MediaSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.util.math.Vec3d;

public final class PearlParticles extends Module {
   private static PearlParticles u;
   private final ColorSetting color = this.addSetting(new ColorSetting("Color", "Particle tint", -8866817));
   private final NumberSetting Z = this.xAsNumberSetting("Spawn Rate", "Particles per pearl each tick", 10.0, 1.0, 10.0, 1.0);
   private final NumberSetting V = this.xAsNumberSetting("Size", "Billboard width in blocks", 0.3, 0.05, 0.5, 0.01);
   private final NumberSetting K = this.xAsNumberSetting("Lifetime", "Seconds", 0.5, 0.5, 3.0, 0.1);
   private final NumberSetting P = this.xAsNumberSetting("Spread", "Initial velocity spread", 0.2, 0.01, 0.2, 0.01);
   private final NumberSetting b = this.xAsNumberSetting("Gravity", "Downward acceleration per tick", 0.008, 0.0, 0.03, 0.001);
   private final NumberSetting I = this.xAsNumberSetting("Fade Speed", "Opacity fade exponent", 2.0, 0.3, 2.0, 0.1);
   private final BooleanSetting colorAnimation = this.addSetting(new BooleanSetting("Color Animation", "Animate the theme gradient", false));
   private final HashMap<Integer, EnderPearlEntity> M = new HashMap<>();
   private final ArrayList<PearlParticles.Type_y> F = new ArrayList<>();
   private final Random N = new Random();
   private Object J;
   private Object x;
   private Vec3d a;
   private int o;

   public PearlParticles() {
      super("Pearl Particles", "Add glowing trails to ender pearls", Category.VISUALS, -1);
      u = this;
      this.color.visibleWhen(() -> !this.colorAnimation.getValue());
   }

   public List<Setting<?>> lAsList() {
      return List.of(this.Z, this.V, this.K);
   }

   public List<Setting<?>> PAsList() {
      return List.of(this.colorAnimation, this.color);
   }

   public List<Setting<?>> vAsList() {
      return List.of(this.P, this.b, this.I);
   }

   private NumberSetting xAsNumberSetting(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new NumberSetting(var1, var2, var3, var5, var7, var9));
   }

   public static PearlParticles iAsPearlParticles() {
      return u;
   }

   public void XAsVoid() {
      this.M.clear();
      this.F.clear();
      this.J = this.x = null;
      this.a = null;
      this.o = 0;
   }

   @Override
   public void onEnable() {
      this.XAsVoid();
      ClientLog.GAsVoid("PearlParticles", "Enabled; max 32 observed pearls, 4096 particles; source bloom sprite");
   }

   @Override
   public void onDisable() {
      this.XAsVoid();
   }

   @Override
   public void onTick() {
      if (this.isRuntimeActive() && this.isInGame()) {
         Vec3d var1 = this.getPlayer().getEntityPos();
         double var12;
         if (this.J != getMc().world
            || this.x != this.getPlayer()
            || this.a != null && ((var12 = var1.squaredDistanceTo(this.a) - 256.0) == 0.0 ? 0 : (var12 < 0.0 ? -1 : 1)) > 0) {
            this.XAsVoid();
         }

         this.J = getMc().world;
         this.x = this.getPlayer();
         this.a = var1;
         double var2 = mb.dAsDouble();
         this.M.entrySet().removeIf(var0 -> getMc().world.getEntityById(var0.getKey()) != var0.getValue() || var0.getValue().isRemoved());
         HashSet var4 = new HashSet();
         int var5 = 0;

         for (Entity var7 : getMc().world.getEntities()) {
            if (var7 instanceof EnderPearlEntity) {
               EnderPearlEntity var8 = (EnderPearlEntity)var7;
               if (!var8.isRemoved() && !(var8.squaredDistanceTo(this.getPlayer()) > 16384.0)) {
                  if (++var5 > 32) {
                     break;
                  }

                  var4.add(var8.getId());
                  if (this.M.get(var8.getId()) == var8) {
                     double var9 = this.P.getValue();

                     for (int var11 = 0; var11 < this.Z.getValue().intValue() && this.F.size() < 4096; var11++) {
                        this.F
                           .add(
                              new PearlParticles.Type_y(
                                 var8.getEntityPos(),
                                 new Vec3d(
                                    (this.N.nextDouble() * 2.0 - 1.0) * var9,
                                    (this.N.nextDouble() * 2.0 - 1.0) * var9 * 0.25,
                                    (this.N.nextDouble() * 2.0 - 1.0) * var9
                                 ),
                                 this.b.getValue(),
                                 var2,
                                 this.o++
                              )
                           );
                     }
                  }

                  if (this.M.size() < 32 || this.M.containsKey(var8.getId())) {
                     this.M.put(var8.getId(), var8);
                  }
               }
            }
         }

         this.M.keySet().retainAll(var4);
         this.F.removeIf(var3 -> var3.hAsBoolean(var2, this.K.getValue()));
      } else {
         this.XAsVoid();
      }
   }

   public boolean LAsBoolean(double var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame() && this.J == getMc().world) {
            for (PearlParticles.Type_y var4 : this.F) {
               double var5 = this.hAsDouble(var4, var1);
               if (var5 >= 0.001 && bg.pAsInt(this.iAsInt(var4, var1), var5) >>> 24 != 0) {
                  return true;
               }
            }

            return false;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private double hAsDouble(PearlParticles.Type_y var1, double var2) {
      double var4 = Math.clamp((var2 - var1.G) / this.K.getValue(), 0.0, 1.0);
      return Math.pow(1.0 - var4, this.I.getValue());
   }

   private int iAsInt(PearlParticles.Type_y var1, double var2) {
      return this.colorAnimation.getValue() ? bg.xAsInt(bd.w, bd.Q, 0.5 + 0.5 * Math.sin(var2 * 2.0 + (double)var1.O * 0.04)) : this.color.getValue();
   }

   public void aAsVoid(MatrixStack var1, EffectDrawBuffers var2, CameraRenderState var3, double var4, boolean var6) {
      if (this.isEnabled() && this.isRuntimeActive() && this.J == getMc().world) {
         GlyphAtlas var7 = new GlyphAtlas(var1, var2, var3);

         for (PearlParticles.Type_y var9 : this.F) {
            double var10 = this.hAsDouble(var9, var4);
            if (!(var10 < 0.001)) {
               Vec3d var12 = var9.D.lerp(var9.P, (double)mb.LAsFloat());
               int var13 = this.iAsInt(var9, var4);
               double var14 = Math.clamp((var4 - var9.G) / this.K.getValue(), 0.0, 1.0);
               var13 = bg.xAsInt(var13, var13 & 0xFF000000 | 16777215, 0.22 * Math.pow(1.0 - var14, 4.0));
               var7.aAsVoid(var12, xAsDouble(this.V.getValue(), var14, var9.O), bg.pAsInt(var13, var10));
            }
         }
      }
   }

   public static double xAsDouble(double var0, double var2, int var4) {
      int var5 = var4 * -1640531527;
      var5 ^= var5 >>> 16;
      double var6 = 0.74 + 0.26 * (double)(var5 >>> 8 & 65535) / 65535.0;
      return var0 * var6 * (1.0 - 0.55 * ay.RAsDouble(var2));
   }

   private static final class Type_y {
      Vec3d P;
      Vec3d D;
      Vec3d v;
      final double N;
      final double G;
      final int O;

      Type_y(Vec3d var1, Vec3d var2, double var3, double var5, int var7) {
         this.P = this.D = var1;
         this.v = var2;
         this.N = var3;
         this.G = var5;
         this.O = var7;
      }

      boolean hAsBoolean(double var1, double var3) {
         this.D = this.P;
         this.P = this.P.add(this.v);
         this.v = this.v.add(0.0, -this.N, 0.0).multiply(0.96);
         return var1 - this.G >= var3;
      }
   }
}
