package com.larpclient;

import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector4f;
import org.joml.Vector4fc;

public final class WebRenderer {
   private static final Matrix4f H = new Matrix4f();
   private static final Matrix4f v = new Matrix4f();
   private static final Vector4f w = new Vector4f();
   private static CameraSubmersionType x = CameraSubmersionType.NONE;
   private static boolean W;
   private static float z;
   private static float E;
   private static Frustum y;

   public static void XAsVoid(float var0) {
      z = var0;
   }

   public static void HAsVoid(float var0) {
      E = var0;
   }

   public static float CAsFloat() {
      return E;
   }

   public static void bAsVoid(Frustum var0) {
      y = var0;
   }

   public static Frustum BAsFrustum() {
      return y;
   }

   public static void wAsVoid(Camera var0, Matrix4fc var1, Matrix4fc var2, Vector4fc var3) {
      H.set(var1);
      v.set(var2);
      w.set(var3);
      x = var0.getSubmersionType();
      W = true;
   }

   public static boolean QAsBoolean() {
      return W;
   }

   public static Matrix4fc VAsMatrix4fc() {
      return H;
   }

   public static Matrix4fc CAsMatrix4fc() {
      return v;
   }

   public static Matrix4f pAsMatrix4f(Matrix4f var0) {
      return var0.set(v).mul(H);
   }

   public static Vector4fc VAsVector4fc() {
      return w;
   }

   public static CameraSubmersionType bAsCameraSubmersionType() {
      return x;
   }

   public static float pAsFloat() {
      return z;
   }

   private WebRenderer() {
   }
}
