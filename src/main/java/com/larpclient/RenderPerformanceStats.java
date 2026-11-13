package com.larpclient;

import com.larpclient.module.Module;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.state.CameraRenderState;

final class RenderPerformanceStats {
   private static List<qn> P = List.of();

   static void BAsVoid(List<Module> var0) {
      P = var0.stream().filter(var0x -> var0x instanceof qn).map(var0x -> (qn)var0x).toList();
   }

   static void PAsVoid(MinecraftClient var0) {
      for (qn var2 : P) {
         if (dAsBoolean(var2)) {
            SharedRenderEffects.EAsVoid(var2.effectName(), () -> var2.tick(var0));
         }
      }
   }

   static void qAsVoid(float var0, CameraRenderState var1) {
      for (qn var3 : P) {
         if (dAsBoolean(var3)) {
            SharedRenderEffects.EAsVoid(var3.effectName(), () -> {
               long var3x = op.RAsLong();

               try {
                  var3.prepare(var0, var1);
               } finally {
                  op.aAsVoid(var3.effectName() + " preparation CPU", var3x);
               }
            });
         }
      }
   }

   static void fAsVoid(EffectGeometryRenderer var0, boolean var1) {
      if (!var1 || x9.K != BloomQuality.Type_w.Off) {
         for (qn var3 : P) {
            if (dAsBoolean(var3)) {
               SharedRenderEffects.EAsVoid(var3.effectName(), () -> {
                  long var3x = op.RAsLong();

                  try {
                     var3.render(var0, var1);
                  } finally {
                     var0.QAsVoid(false);
                     var0.blend(x4.Type_g.ALPHA);
                     op.aAsVoid(var3.effectName() + (var1 ? " glow CPU" : " base CPU"), var3x);
                  }
               });
            }
         }
      }
   }

   private static boolean dAsBoolean(qn var0) {
      if (var0 instanceof Module var1 && !var1.isRuntimeActive()) {
         return false;
      }

      return true;
   }

   static boolean ZAsBoolean() {
      return x9.K != BloomQuality.Type_w.Off
         && P.stream().anyMatch(var0 -> dAsBoolean(var0) && !SharedRenderEffects.OAsBoolean(var0.effectName()) && var0.glowing());
   }

   static boolean AAsBoolean() {
      return x9.K != BloomQuality.Type_w.Off
         && P.stream().anyMatch(var0 -> dAsBoolean(var0) && !SharedRenderEffects.OAsBoolean(var0.effectName()) && var0.soft());
   }

   static void nAsVoid() {
      for (qn var1 : P) {
         pAsVoid(var1.effectName());
      }
   }

   static void pAsVoid(String var0) {
      for (qn var2 : P) {
         if (var2.effectName().equals(var0)) {
            try {
               var2.clear();
            } catch (RuntimeException var4) {
               ClientLog.hAsVoid("Render", "Effect cleanup failed: " + var0, var4);
            }
         }
      }
   }

   private RenderPerformanceStats() {
   }
}
