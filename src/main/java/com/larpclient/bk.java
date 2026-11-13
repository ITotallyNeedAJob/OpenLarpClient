package com.larpclient;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.VertexSorter;
import com.mojang.blaze3d.systems.RenderSystem.ShapeIndexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import com.mojang.blaze3d.vertex.VertexFormat.IndexType;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.function.Supplier;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.BufferAllocator;

public final class bk implements AutoCloseable {
   private final Supplier<String> f;
   private final int n;
   private final ArrayList<bk.Type_v> m = new ArrayList<>();
   private int v;

   public bk(Supplier<String> var1, int var2) {
      this.f = var1;
      this.n = var2;
   }

   public bk.Type_v LAsBk_Type_v(VertexFormat var1, DrawMode var2, VertexSorter var3) {
      if (this.v == this.m.size()) {
         this.m.add(new bk.Type_v(Math.min(this.n, 65536)));
      }

      bk.Type_v var4 = this.m.get(this.v++);
      var4.j = new BufferBuilder(var4.e, var2, var1);
      var4.F = var3;
      var4.O = null;
      return var4;
   }

   public VertexConsumer dAsVertexConsumer(bk.Type_v var1) {
      return var1.j;
   }

   public void OAsVoid() {
      GpuDevice var1 = RenderSystem.getDevice();
      CommandEncoder var2 = var1.createCommandEncoder();

      for (int var3 = 0; var3 < this.v; var3++) {
         bk.Type_v var4 = this.m.get(var3);
         BuiltBuffer var5 = var4.j.endNullable();
         var4.j = null;
         if (var5 != null) {
            BuiltBuffer var6 = var5;

            try {
               if (var4.F != null && var5.getDrawParameters().mode() == DrawMode.QUADS) {
                  var5.sortQuads(var4.e, var4.F);
               }

               ByteBuffer var7 = var5.getBuffer();
               if (var4.E == null || var4.E.size() < (long)var7.remaining()) {
                  if (var4.E != null) {
                     var4.E.close();
                  }

                  var4.E = var1.createBuffer(this.f, 40, (long)var7.remaining());
               }

               var2.writeToBuffer(var4.E.slice(0L, (long)var7.remaining()), var7);
               ByteBuffer var10 = var5.getSortedBuffer();
               GpuBuffer var8;
               IndexType var9;
               if (var10 == null) {
                  ShapeIndexBuffer var11 = RenderSystem.getSequentialBuffer(var5.getDrawParameters().mode());
                  var8 = var11.getIndexBuffer(var5.getDrawParameters().indexCount());
                  var9 = var11.getIndexType();
               } else {
                  if (var4.Q == null || var4.Q.size() < (long)var10.remaining()) {
                     if (var4.Q != null) {
                        var4.Q.close();
                     }

                     var4.Q = var1.createBuffer(this.f, 72, (long)var10.remaining());
                  }

                  var2.writeToBuffer(var4.Q.slice(0L, (long)var10.remaining()), var10);
                  var8 = var4.Q;
                  var9 = var5.getDrawParameters().indexType();
               }

               var4.O = new bk.Type_c(var4.E, var8, var9, var5.getDrawParameters().indexCount(), 0, 0);
            } catch (Throwable var131) {
               if (var5 != null) {
                  try {
                     var6.close();
                  } catch (Throwable var121) {
                     var131.addSuppressed(var121);
                  }
               }

               throw var131;
            }

            if (var5 != null) {
               var5.close();
            }
         }
      }
   }

   public bk.Type_c UAsBk_Type_c(bk.Type_v var1) {
      return var1.O;
   }

   public void bAsVoid() {
      for (int var1 = 0; var1 < this.v; var1++) {
         bk.Type_v var2 = this.m.get(var1);
         var2.e.clear();
         var2.j = null;
         var2.O = null;
      }

      this.v = 0;
   }

   public void vAsVoid() {
      this.bAsVoid();
   }

   @Override
   public void close() {
      for (bk.Type_v var2 : this.m) {
         var2.e.close();
         if (var2.E != null) {
            var2.E.close();
         }

         if (var2.Q != null) {
            var2.Q.close();
         }
      }

      this.m.clear();
      this.v = 0;
   }

   public static record Type_c(GpuBuffer vertexBuffer, GpuBuffer indexBuffer, IndexType indexType, int indexCount, int firstIndex, int baseVertex) {
   }

   public static final class Type_v {
      final BufferAllocator e;
      BufferBuilder j;
      VertexSorter F;
      GpuBuffer E;
      GpuBuffer Q;
      bk.Type_c O;

      Type_v(int var1) {
         this.e = new BufferAllocator(var1);
      }
   }
}
