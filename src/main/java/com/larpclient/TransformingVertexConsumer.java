package com.larpclient;

import net.minecraft.client.render.VertexConsumer;
import org.joml.Matrix4fc;

final class TransformingVertexConsumer implements VertexConsumer {
   VertexConsumer U;
   int o;

   TransformingVertexConsumer() {
   }

   public VertexConsumer vertex(float x, float y, float z) {
      this.U.vertex(x, y, z);
      return this;
   }

   public VertexConsumer vertex(Matrix4fc matrix, float x, float y, float z) {
      return this.vertex(
         matrix.m00() * x + matrix.m10() * y + matrix.m20() * z + matrix.m30(),
         matrix.m01() * x + matrix.m11() * y + matrix.m21() * z + matrix.m31(),
         matrix.m02() * x + matrix.m12() * y + matrix.m22() * z + matrix.m32()
      );
   }

   public VertexConsumer color(int red, int green, int blue, int alpha) {
      this.U.color(red * (this.o >>> 16 & 0xFF) / 255, green * (this.o >>> 8 & 0xFF) / 255, blue * (this.o & 0xFF) / 255, alpha * (this.o >>> 24) / 255);
      return this;
   }

   public VertexConsumer color(int argb) {
      return this.color(argb >>> 16 & 0xFF, argb >>> 8 & 0xFF, argb & 0xFF, argb >>> 24);
   }

   public VertexConsumer texture(float u, float v) {
      this.U.texture(u, v);
      return this;
   }

   public VertexConsumer overlay(int u, int v) {
      this.U.overlay(u, v);
      return this;
   }

   public VertexConsumer light(int u, int v) {
      this.U.light(u, v);
      return this;
   }

   public VertexConsumer normal(float x, float y, float z) {
      this.U.normal(x, y, z);
      return this;
   }

   public VertexConsumer lineWidth(float width) {
      this.U.lineWidth(width);
      return this;
   }
}
