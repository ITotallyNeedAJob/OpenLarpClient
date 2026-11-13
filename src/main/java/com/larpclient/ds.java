package com.larpclient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public final class ds {
   public static int vAsInt(double var0) {
      return MathHelper.floor(var0 + 1.0E-6) - 1;
   }

   public static List<BlockPos> oAsList(Vec3d var0, Box var1, Vec3d var2, int var3) {
      Vec3d var4 = new Vec3d(var2.x, 0.0, var2.z);
      if (var4.lengthSquared() > 0.36) {
         var4 = var4.normalize().multiply(0.6);
      }

      double var5 = Math.clamp(var1.getLengthX() * 0.5 - 0.001, 0.0, 0.49);
      double var7 = Math.clamp(var1.getLengthZ() * 0.5 - 0.001, 0.0, 0.49);
      LinkedHashSet var9 = new LinkedHashSet();
      BlockPos var10 = new BlockPos(MathHelper.floor(var0.x), var3, MathHelper.floor(var0.z));
      var9.add(var10);

      for (double var14 : new double[]{0.0, 0.5, 1.0}) {
         Vec3d var16 = var0.add(var4.multiply(var14));

         for (double var20 : new double[]{-var5, 0.0, var5}) {
            for (double var25 : new double[]{-var7, 0.0, var7}) {
               var9.add(new BlockPos(MathHelper.floor(var16.x + var20), var3, MathHelper.floor(var16.z + var25)));
            }
         }
      }

      ArrayList var27 = new ArrayList(var9);
      var27.remove(var10);
      var27.sort(Comparator.comparingDouble(var1x -> Vec3d.ofCenter((Vec3i)var1x).multiply(1.0, 0.0, 1.0).squaredDistanceTo(var0.multiply(1.0, 0.0, 1.0))));
      var27.addFirst(var10);
      return List.copyOf(var27.subList(0, Math.min(9, var27.size())));
   }

   private ds() {
   }
}
