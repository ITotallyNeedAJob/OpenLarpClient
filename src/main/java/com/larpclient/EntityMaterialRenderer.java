package com.larpclient;

import com.larpclient.module.SelfVisuals;
import net.minecraft.client.render.state.CameraRenderState;
import org.joml.Vector4fc;

public final class EntityMaterialRenderer {
   private static EntityMaterialRenderer.Type_l P = new EntityMaterialRenderer.Type_l(0.65, 0.78, 0.8, 0.18, false);
   private static double x;
   private static int K = -7891281;
   private static int n = -1;

   public static void jAsVoid(CameraRenderState var0) {
      SelfVisuals var1 = SelfVisuals.zAsSelfVisuals();
      if (var1 != null) {
         P = new EntityMaterialRenderer.Type_l(
            var1.surfaceFlow.getValue(), var1.gloss.getValue(), var1.edgeHighlight.getValue(), var1.iridescence.getValue(), var1.affectArmor.getValue()
         );
      }

      x = MediaSession.RAsDouble();
      if (WebRenderer.QAsBoolean()) {
         Vector4fc var2 = WebRenderer.VAsVector4fc();
         K = 0xFF000000
            | Math.clamp((long)Math.round(var2.x() * 255.0F), 0, 255) << 16
            | Math.clamp((long)Math.round(var2.y() * 255.0F), 0, 255) << 8
            | Math.clamp((long)Math.round(var2.z() * 255.0F), 0, 255);
      }
   }

   public static boolean YAsBoolean() {
      if (n == BloomQuality.J) {
         return false;
      } else {
         try {
            CosmeticSubmissionHandler.fAsVoid();
            return true;
         } catch (RuntimeException var1) {
            n = BloomQuality.J;
            ClientLog.lAsVoid("Liquid Glass", "Standard material unavailable; native skin retained until resource reload", var1);
            return false;
         }
      }
   }

   public static boolean kAsBoolean() {
      return P.armor;
   }

   public static void sAsVoid(RuntimeException var0) {
      n = BloomQuality.J;
      ClientLog.lAsVoid("Liquid Glass", "Material submission failed; native geometry retained until resource reload", var0);
   }

   public static int rAsInt(SelfVisuals var0, double var1) {
      return (int)(Math.clamp(var1, 0.0, 1.0) * 255.0) << 24 | (int)(P.gloss * 255.0) << 16 | (int)(P.edge * 255.0) << 8 | (int)(P.iridescence * 255.0);
   }

   public static double qAsDouble(SelfVisuals var0) {
      return x * P.flow % (Math.PI * 2);
   }

   public static int IAsInt() {
      return K >>> 8 & 65535;
   }

   public static int YAsInt() {
      return K & 0xFF;
   }

   private EntityMaterialRenderer() {
   }

   private static record Type_l(double flow, double gloss, double edge, double iridescence, boolean armor) {
   }
}
