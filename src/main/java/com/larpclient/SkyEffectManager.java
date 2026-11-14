package com.larpclient;

import com.larpclient.module.CustomSky;
import com.larpclient.module.Halloween;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;

public final class SkyEffectManager implements AutoCloseable {
   private static SkyEffectManager B;
   private static final Set<String> Q = new HashSet<>();
   private static int T = -1;
   private static boolean I;
   private final bl F = new bl();
   private final Map<String, ad> BloomUtility = new HashMap<>();
   private ad k;
   private String G;
   private int M = -1;
   private double SmoothInput = Double.NaN;
   private boolean d;

   public SkyEffectManager() {
      B = this;
   }

   public static boolean gAsBoolean(String var0) {
      return I || T != BloomQuality.J || !Q.contains(var0);
   }

   public static boolean WAsBoolean() {
      return gAsBoolean("Nebula");
   }

   public static void KAsVoid() {
      I = true;
   }

   public static void vAsVoid() {
      if (B != null) {
         B.d = false;
      }

      if (I || T != BloomQuality.J) {
         Q.clear();
         T = BloomQuality.J;
         I = false;
         if (B != null) {
            B.GAsVoid();
         }
      }

      CustomSky var0 = CustomSky.NAsCustomSky();
      if (B != null && (!Halloween.OAsBoolean() && (var0 == null || !var0.isOperational()) || MinecraftClient.getInstance().world == null) && B.k != null) {
         B.close();
         ClientLog.GAsVoid("Sky", "Sky renderer disabled; GPU resources released");
      }
   }

   public static boolean AAsBoolean() {
      return B != null && B.d && Halloween.OAsBoolean() && gAsBoolean("Harvest Moon");
   }

   public boolean QAsBoolean(Framebuffer var1) {
      this.d = false;
      if (Halloween.OAsBoolean() && gAsBoolean("Harvest Moon")) {
         long var2 = op.RAsLong();

         try {
            if (this.M != BloomQuality.J) {
               this.GAsVoid();
            }

            if (!"Harvest Moon".equals(this.G)) {
               if (this.k != null) {
                  this.k.close();
               }

               this.G = "Harvest Moon";
               this.t = Double.NaN;
               this.k = this.b.computeIfAbsent(this.G, var0 -> new dy());
               ClientLog.GAsVoid("Halloween", "Harvest Moon owns the sky; existing selections retained");
            }

            double var4 = mb.RAsDouble();
            this.F.a = Double.isNaN(this.t) ? 0.0 : Math.clamp(var4 - this.t, 0.0, 0.1);
            this.t = var4;
            this.F.L = var1;
            this.k.render(this.F);
            this.d = true;
            return true;
         } catch (IOException | RuntimeException var101) {
            Q.add("Harvest Moon");
            T = BloomQuality.J;
            this.close();
            ClientLog.lAsVoid("Halloween", "Harvest Moon shader failed; existing or native sky restored until F3+T or toggle", var101);
         } finally {
            this.F.L = null;
            op.aAsVoid("Harvest Moon CPU", var2);
         }

         return false;
      } else {
         return false;
      }
   }

   public boolean SAsBoolean(Framebuffer var1, CustomSky var2) {
      if (!gAsBoolean(var2.ZAsString())) {
         return false;
      } else {
         long var3 = op.RAsLong();

         try {
            if (this.M != BloomQuality.J) {
               this.GAsVoid();
            }

            if (!var2.ZAsString().equals(this.G)) {
               if (this.k != null) {
                  this.k.close();
               }

               this.t = Double.NaN;
               this.G = var2.ZAsString();
               this.k = this.b.computeIfAbsent(this.G, var0 -> {
                  return (ad)(switch (var0) {
                     case "Ocean" -> new dw();
                     case "Israel Flag" -> new dm();
                     default -> new NebulaSkyRenderer();
                  });
               });
               ClientLog.GAsVoid("Sky", "Sky mode switched: {}; resource generation={}", this.G, this.M);
            }

            double var5 = mb.RAsDouble();
            this.F.a = Double.isNaN(this.t) ? 0.0 : Math.clamp(var5 - this.t, 0.0, 0.1);
            this.t = var5;
            this.F.L = var1;
            this.F.U = var2;
            this.k.render(this.F);
            return true;
         } catch (IOException | RuntimeException var111) {
            Q.add(var2.ZAsString());
            T = BloomQuality.J;
            this.close();
            ClientLog.lAsVoid("Sky", var2.ZAsString() + " failed; using vanilla sky until F3+T or disable/enable; generation=" + this.M, var111);
         } finally {
            this.F.L = null;
            this.F.U = null;
            op.aAsVoid("Sky", var3);
         }

         return false;
      }
   }

   private void GAsVoid() {
      for (ad var2 : this.b.values()) {
         var2.reload();
      }

      this.F.I.close();
      this.M = BloomQuality.J;
      this.t = Double.NaN;
   }

   @Override
   public void close() {
      this.d = false;

      for (ad var2 : this.b.values()) {
         var2.close();
      }

      this.k = null;
      this.F.I.close();
      this.G = null;
      this.t = Double.NaN;
   }
}
