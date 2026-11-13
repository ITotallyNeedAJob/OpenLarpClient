package com.larpclient;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.math.Vec3d;

public final class PumpkinMeshReader {
   public static final int d = 12;
   public static final int N = 4000;
   public static final int V = -9416661;

   public static PumpkinMeshReader.Type_u XAsA7_Type_u(InputStream var0) throws IOException {
      if (var0 == null) {
         throw new IOException("Pumpkin mesh missing");
      } else {
         DataInputStream var1 = new DataInputStream(var0);
         if (var1.readInt() == 1347244081 && var1.readInt() == 12) {
            ArrayList var2 = new ArrayList(12);
            int var3 = 0;

            for (int var4 = 0; var4 < 12; var4++) {
               int var5 = var1.readInt();
               if (var5 < 3 || var5 % 3 != 0 || var5 > 12000 - var3) {
                  throw new IOException("Invalid pumpkin geometry budget");
               }

               var3 += var5;
               Vec3d var6 = new Vec3d((double)lAsFloat(var1), (double)lAsFloat(var1), (double)lAsFloat(var1));
               float[] var7 = new float[var5 * 5];
               int[] var8 = new int[var5];

               for (int var9 = 0; var9 < var5; var9++) {
                  for (int var10 = 0; var10 < 5; var10++) {
                     float var11 = lAsFloat(var1);
                     if (var10 >= 3 && (var11 < 0.0F || var11 > 1.0F)) {
                        throw new IOException("Invalid pumpkin UV");
                     }

                     var7[var9 * 5 + var10] = var11;
                  }

                  var8[var9] = var1.readInt();
                  if (var8[var9] >>> 24 != 255) {
                     throw new IOException("Pumpkin vertex must be opaque");
                  }
               }

               var2.add(new PumpkinMeshReader.Type_l(var6, var7, var8));
            }

            if (var1.read() != -1) {
               throw new IOException("Unexpected pumpkin mesh trailer");
            } else {
               return new PumpkinMeshReader.Type_u(List.copyOf(var2), var3 / 3);
            }
         } else {
            throw new IOException("Invalid pumpkin mesh header");
         }
      }
   }

   private static float lAsFloat(DataInputStream var0) throws IOException {
      float var1 = var0.readFloat();
      if (Float.isFinite(var1) && !(Math.abs(var1) > 1.0F)) {
         return var1;
      } else {
         throw new IOException("Invalid pumpkin coordinate");
      }
   }

   private PumpkinMeshReader() {
   }

   public static record Type_l(Vec3d pivot, float[] vertices, int[] colors) {
      public int triangles() {
         return this.colors.length / 3;
      }
   }

   public static record Type_u(List<PumpkinMeshReader.Type_l> chunks, int triangles) {
   }
}
