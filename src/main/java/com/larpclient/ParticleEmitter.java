package com.larpclient;

import com.larpclient.module.Aura;
import com.larpclient.module.AutoAnchor;
import com.larpclient.module.AutoCrystal;
import com.larpclient.module.AutoPearlCatch;
import com.larpclient.module.AutoTrap;
import com.larpclient.module.AutoWeb;
import com.larpclient.module.Freecam;
import com.larpclient.module.Mace;
import com.larpclient.module.Scaffold;
import com.larpclient.module.WindChargeHop;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.entity.EntityPose;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public final class ParticleEmitter {
   private static final ParticleEmitter.Type_x T = new ParticleEmitter.Type_x();
   private static Perspective r;
   private static Perspective o;
   private static float G;
   private static float y;
   private static Vec3d v = Vec3d.ZERO;
   private static Vec3d R = Vec3d.ZERO;

   public static boolean fAsBoolean(ParticleEmitter.Type_u var0) {
      NAsP5_Type_u();
      return T.CAsBoolean(var0);
   }

   public static void jAsVoid(ParticleEmitter.Type_u var0) {
      MinecraftClient var1 = MinecraftClient.getInstance();
      NAsP5_Type_u();
      if (T.CAsBoolean(var0)) {
         if (T.s == var0) {
            if (!DAsBoolean(var0)) {
               MAsVoid(var0);
            }
         } else {
            if (DAsBoolean(T.s)) {
               GAsVoid(T.s);
            }

            if (T.s == ParticleEmitter.Type_u.NONE && !DAsBoolean(var0)) {
               r = var1.options.getPerspective();
            }

            if (T.cAsBoolean(var0, var1.player, var1.world)) {
               G = var1.player.getYaw();
               y = var1.player.getPitch();
               o = var1.options.getPerspective();
               if (DAsBoolean(var0) && o.isFrontView()) {
                  G += 180.0F;
                  y = -y;
               }

               R = v = var0 == ParticleEmitter.Type_u.FREECAM
                  ? var1.player.getEntityPos().add(0.0, (double)var1.player.getEyeHeight(EntityPose.STANDING), 0.0)
                  : var1.player.getEyePos();
               if (!DAsBoolean(var0)) {
                  MAsVoid(var0);
               } else {
                  sAsVoid("acquired");
               }
            }
         }
      }
   }

   public static void GAsVoid(ParticleEmitter.Type_u var0) {
      if (T.s == var0) {
         MinecraftClient var1 = MinecraftClient.getInstance();
         if (DAsBoolean(var0) && T.EAsBoolean(var1.player, var1.world, var1.player != null && var1.player.isAlive())) {
            BAsVoid();
            float[] var2 = sAsFloatArray(G, y, var1.options.getPerspective().isFrontView());
            var1.player.setYaw(var2[0]);
            var1.player.setPitch(var2[1]);
            if (var0 == ParticleEmitter.Type_u.SCAFFOLD) {
               var1.player.setHeadYaw(var2[0]);
            }

            var1.player.lastYaw = var2[0];
            var1.player.lastPitch = var2[1];
         }

         gAsVoid();
      }
   }

   private static boolean jAsBoolean() {
      MinecraftClient var0 = MinecraftClient.getInstance();
      return T.EAsBoolean(var0.player, var0.world, var0.player != null && var0.player.isAlive())
         && var0.currentScreen == null
         && var0.isWindowFocused()
         && !var0.player.isSpectator();
   }

   public static void gAsVoid() {
      if (DAsBoolean(T.s)) {
         sAsVoid("released");
      }

      if (r != null) {
         MinecraftClient.getInstance().options.setPerspective(r);
      }

      r = null;
      o = null;
      T.XAsBoolean(T.s);
      R = v = Vec3d.ZERO;
      y = 0.0F;
      G = 0.0F;
   }

   public static ParticleEmitter.Type_u NAsP5_Type_u() {
      if (T.s == ParticleEmitter.Type_u.SCAFFOLD) {
         Scaffold var0 = Scaffold.FAsScaffold();
         if (!jAsBoolean() || var0 == null || !var0.bAsBoolean()) {
            GAsVoid(ParticleEmitter.Type_u.SCAFFOLD);
         }
      }

      if (T.s == ParticleEmitter.Type_u.AUTOTRAP) {
         AutoTrap var1 = AutoTrap.oAsAutoTrap();
         if (!jAsBoolean() || var1 == null || !var1.VAsBoolean()) {
            GAsVoid(ParticleEmitter.Type_u.AUTOTRAP);
         }
      }

      if (T.s == ParticleEmitter.Type_u.AUTOWEB) {
         AutoWeb var2 = AutoWeb.PAsAutoWeb();
         if (!jAsBoolean() || var2 == null || !var2.kAsBoolean()) {
            GAsVoid(ParticleEmitter.Type_u.AUTOWEB);
         }
      }

      if (T.s == ParticleEmitter.Type_u.AUTOANCHOR) {
         AutoAnchor var3 = AutoAnchor.SAsAutoAnchor();
         if (!jAsBoolean() || var3 == null || !var3.LAsBoolean()) {
            GAsVoid(ParticleEmitter.Type_u.AUTOANCHOR);
         }
      }

      if (T.s == ParticleEmitter.Type_u.AURA) {
         Aura var4 = Aura.fAsAura();
         if (!jAsBoolean() || var4 == null || !var4.wAsBoolean()) {
            GAsVoid(ParticleEmitter.Type_u.AURA);
         }
      }

      if (T.s == ParticleEmitter.Type_u.MACE) {
         Mace var5 = Mace.yAsMace();
         if (!jAsBoolean() || var5 == null || !var5.rAsBoolean()) {
            GAsVoid(ParticleEmitter.Type_u.MACE);
         }
      }

      if (T.s == ParticleEmitter.Type_u.AUTOCRYSTAL) {
         AutoCrystal var6 = AutoCrystal.vAsAutoCrystal();
         if (!jAsBoolean() || var6 == null || !var6.zAsBoolean()) {
            GAsVoid(ParticleEmitter.Type_u.AUTOCRYSTAL);
         }
      }

      if (T.s == ParticleEmitter.Type_u.PEARLCATCH) {
         AutoPearlCatch var7 = AutoPearlCatch.cAsAutoPearlCatch();
         if (!jAsBoolean() || var7 == null || !var7.NAsBoolean()) {
            GAsVoid(ParticleEmitter.Type_u.PEARLCATCH);
         }
      }

      if (T.s == ParticleEmitter.Type_u.WINDHOP) {
         WindChargeHop var8 = WindChargeHop.OAsWindChargeHop();
         if (!jAsBoolean() || var8 == null || !var8.UAsBoolean()) {
            GAsVoid(ParticleEmitter.Type_u.WINDHOP);
         }
      }

      MinecraftClient var9 = MinecraftClient.getInstance();
      if (T.s != ParticleEmitter.Type_u.NONE && !T.EAsBoolean(var9.player, var9.world, var9.player != null && var9.player.isAlive())) {
         gAsVoid();
      }

      if (DAsBoolean(T.s)) {
         BAsVoid();
      }

      return T.s;
   }

   private static boolean DAsBoolean(ParticleEmitter.Type_u var0) {
      return var0 == ParticleEmitter.Type_u.AURA
         || var0 == ParticleEmitter.Type_u.MACE
         || var0 == ParticleEmitter.Type_u.AUTOTRAP
         || var0 == ParticleEmitter.Type_u.AUTOWEB
         || var0 == ParticleEmitter.Type_u.AUTOANCHOR
         || var0 == ParticleEmitter.Type_u.AUTOCRYSTAL
         || var0 == ParticleEmitter.Type_u.SCAFFOLD
         || var0 == ParticleEmitter.Type_u.PEARLCATCH
         || var0 == ParticleEmitter.Type_u.WINDHOP;
   }

   public static boolean TAsBoolean() {
      return DAsBoolean(NAsP5_Type_u());
   }

   private static void BAsVoid() {
      Perspective var0 = MinecraftClient.getInstance().options.getPerspective();
      if (o != null && o.isFrontView() != var0.isFrontView()) {
         G = G + (var0.isFrontView() ? 180.0F : -180.0F);
         y = -y;
      }

      o = var0;
   }

   public static boolean oAsBoolean() {
      return NAsP5_Type_u() == ParticleEmitter.Type_u.FREECAM;
   }

   public static void kAsVoid(double var0, double var2) {
      G += (float)var0 * 0.15F;
      float var4 = oAsBoolean() ? 90.0F : 89.0F;
      y = MathHelper.clamp(y + (float)var2 * 0.15F, -var4, var4);
   }

   public static float JAsFloat() {
      return G;
   }

   public static float jAsFloat() {
      return MinecraftClient.getInstance().options.getPerspective().isFrontView() ? G - 180.0F : G;
   }

   public static float mAsFloat() {
      return y;
   }

   public static Vec3d dAsVec3d(float var0) {
      return R.lerp(v, (double)var0);
   }

   public static void DAsVoid(double var0, double var2, boolean var4) {
      MinecraftClient var5 = MinecraftClient.getInstance();
      R = v;
      if (oAsBoolean() && var5.currentScreen == null && var5.isWindowFocused()) {
         v = v.add(OuterEffect.fAsVec3d(G, Freecam.xAsPlayerInput(), var0, var2, var4));
      }
   }

   private static void MAsVoid(ParticleEmitter.Type_u var0) {
      MinecraftClient var1 = MinecraftClient.getInstance();
      Perspective var2 = r == null ? var1.options.getPerspective() : r;
      Perspective var3 = var0 == ParticleEmitter.Type_u.FREECAM
         ? (Freecam.bAsBoolean() && var2.isFirstPerson() ? Perspective.THIRD_PERSON_BACK : var2)
         : Perspective.THIRD_PERSON_BACK;
      if (var1.options.getPerspective() != var3) {
         var1.options.setPerspective(var3);
      }
   }

   public static float[] sAsFloatArray(float var0, float var1, boolean var2) {
      return new float[]{var2 ? var0 - 180.0F : var0, var2 ? -var1 : var1};
   }

   private static void sAsVoid(String var0) {
      if (T.s == ParticleEmitter.Type_u.SCAFFOLD) {
         ClientLog.XAsVoid("Scaffold", "Independent view {}", var0);
      } else if (T.s == ParticleEmitter.Type_u.AUTOTRAP) {
         ClientLog.GAsVoid("AutoTrap", "Independent view {}", var0);
      } else if (T.s == ParticleEmitter.Type_u.AUTOWEB) {
         ClientLog.GAsVoid("AutoWeb", "Independent view {}", var0);
      } else if (T.s == ParticleEmitter.Type_u.AUTOANCHOR) {
         ClientLog.GAsVoid("AutoAnchor", "Independent view {}", var0);
      } else if (T.s == ParticleEmitter.Type_u.AUTOCRYSTAL) {
         ClientLog.XAsVoid("AutoCrystal", "Independent view {}", var0);
      } else if (T.s == ParticleEmitter.Type_u.PEARLCATCH) {
         ClientLog.GAsVoid("PearlCatch", "Silent view {}", var0);
      } else if (T.s == ParticleEmitter.Type_u.WINDHOP) {
         ClientLog.GAsVoid("WindHop", "Silent view {}", var0);
      } else {
         if (T.s == ParticleEmitter.Type_u.AURA) {
            ClientLog.XAsVoid("Aura", "Independent view {}", var0);
         }

         if (T.s == ParticleEmitter.Type_u.MACE) {
            ClientLog.XAsVoid("Mace", "Independent view {}", var0);
         }
      }
   }

   private ParticleEmitter() {
   }

   public static enum Type_u {
      NONE,
      SCAFFOLD,
      AUTOTRAP,
      AUTOWEB,
      AUTOANCHOR,
      AURA,
      MACE,
      AUTOCRYSTAL,
      WINDHOP,
      PEARLCATCH,
      FREELOOK,
      FREECAM;

      private Type_u() {
      }
   }

   public static final class Type_x {
      private ParticleEmitter.Type_u s = ParticleEmitter.Type_u.NONE;
      private Object u;
      private Object T;

      public Type_x() {
      }

      public ParticleEmitter.Type_u RAsP5_Type_u() {
         return this.s;
      }

      public boolean CAsBoolean(ParticleEmitter.Type_u var1) {
         if (var1 == ParticleEmitter.Type_u.SCAFFOLD) {
            return this.s == ParticleEmitter.Type_u.NONE || this.s == var1;
         } else if (OAsBoolean(var1)) {
            return this.s == ParticleEmitter.Type_u.NONE || this.s == ParticleEmitter.Type_u.SCAFFOLD || OAsBoolean(this.s);
         } else if (var1 == ParticleEmitter.Type_u.AURA) {
            return this.s == ParticleEmitter.Type_u.NONE || this.s == ParticleEmitter.Type_u.SCAFFOLD || OAsBoolean(this.s) || this.s == var1;
         } else if (var1 == ParticleEmitter.Type_u.MACE) {
            return this.s == ParticleEmitter.Type_u.NONE || this.s == ParticleEmitter.Type_u.SCAFFOLD || OAsBoolean(this.s) || this.s == ParticleEmitter.Type_u.AURA || this.s == var1;
         } else if (var1 == ParticleEmitter.Type_u.AUTOCRYSTAL) {
            return this.s == ParticleEmitter.Type_u.NONE || this.s == ParticleEmitter.Type_u.SCAFFOLD || OAsBoolean(this.s) || this.s == ParticleEmitter.Type_u.AURA || this.s == var1;
         } else if (var1 == ParticleEmitter.Type_u.WINDHOP) {
            return this.s != ParticleEmitter.Type_u.FREELOOK && this.s != ParticleEmitter.Type_u.FREECAM && this.s != ParticleEmitter.Type_u.PEARLCATCH;
         } else if (var1 != ParticleEmitter.Type_u.PEARLCATCH) {
            return var1 == ParticleEmitter.Type_u.FREELOOK ? this.s != ParticleEmitter.Type_u.FREECAM : true;
         } else {
            return this.s != ParticleEmitter.Type_u.FREELOOK && this.s != ParticleEmitter.Type_u.FREECAM;
         }
      }

      private static boolean OAsBoolean(ParticleEmitter.Type_u var0) {
         return var0 == ParticleEmitter.Type_u.AUTOTRAP || var0 == ParticleEmitter.Type_u.AUTOWEB || var0 == ParticleEmitter.Type_u.AUTOANCHOR;
      }

      public boolean cAsBoolean(ParticleEmitter.Type_u var1, Object var2, Object var3) {
         if (!this.CAsBoolean(var1)) {
            return false;
         } else {
            this.s = var1;
            this.u = var2;
            this.T = var3;
            return true;
         }
      }

      public boolean EAsBoolean(Object var1, Object var2, boolean var3) {
         return var1 != null && var3 && this.u == var1 && this.T == var2;
      }

      public boolean XAsBoolean(ParticleEmitter.Type_u var1) {
         if (this.s != var1) {
            return false;
         } else {
            this.s = ParticleEmitter.Type_u.NONE;
            this.u = this.T = null;
            return true;
         }
      }
   }
}
