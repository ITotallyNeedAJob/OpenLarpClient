package com.larpclient;

import java.util.Objects;
import net.minecraft.util.math.BlockPos;

public final class PixelUtil {
   private BlockPos d;
   private Object Z;
   private Object z;
   private int b;
   private int i;
   private int q;
   private boolean y;

   public PixelUtil() {
   }

   public BlockPos PAsBlockPos() {
      return this.d;
   }

   public void oAsVoid() {
      this.d = null;
      this.Z = this.z = null;
      this.y = false;
   }

   public void aAsVoid(BlockPos var1, Object var2, Object var3, int var4) {
      if (!Objects.equals(this.d, var1) || this.Z != var2 || this.z != var3 || var4 - this.i != 1) {
         this.d = var1 == null ? null : var1.toImmutable();
         this.Z = var2;
         this.z = var3;
         this.b = var4;
         this.y = false;
      }

      this.i = var4;
   }

   public boolean LAsBoolean(int var1, int var2, boolean var3) {
      if (this.d == null || var1 < this.b) {
         return false;
      } else if (!var3) {
         this.y = false;
         return false;
      } else {
         if (!this.y) {
            this.y = true;
            this.q = var1;
         }

         return var1 - this.b >= Math.max(2, var2) && var1 - this.q >= 1;
      }
   }
}
