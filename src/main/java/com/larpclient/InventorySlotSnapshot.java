package com.larpclient;

public final class InventorySlotSnapshot {
   private final int[] p = new int[36];
   private final boolean[] D = new boolean[36];
   private long X = -1L;
   private long d = -1L;
   private long n;
   private int[] I;

   public InventorySlotSnapshot() {
      int var1 = 0;

      while (var1 < 36) {
         this.p[var1] = var1++;
      }
   }

   public int[] zAsIntArray(long var1, long var3) {
      if (this.I != null && var1 == this.X && var3 == this.d) {
         return this.I;
      } else {
         boolean[] var5 = new boolean[36];
         boolean[] var6 = new boolean[36];

         for (int var7 = 0; var7 < 36; var7++) {
            var5[var7] = (var1 & 1L << var7) != 0L;
            var6[var7] = (var3 & 1L << var7) != 0L;
         }

         this.kAsVoid(var5, var6);
         this.X = var1;
         this.d = var3;
         this.I = this.yAsIntArray(var6);
         this.n++;
         return this.I;
      }
   }

   public long NAsLong() {
      return this.n;
   }

   public void RAsVoid(int[] var1) {
      if (var1.length != 36) {
         throw new IllegalArgumentException("Expected 36 slots");
      } else {
         System.arraycopy(var1, 0, this.p, 0, 36);
         this.I = null;
      }
   }

   public void kAsVoid(boolean[] var1, boolean[] var2) {
      this.I = null;
      if (var1.length == 36 && var2.length == 36) {
         boolean[] var3 = new boolean[36];

         for (int var4 = 0; var4 < 36; var4++) {
            int var5 = this.p[var4];
            if (var5 >= 0 && !var2[var5] && !var3[var5]) {
               var3[var5] = true;
            } else {
               this.p[var4] = -1;
               this.D[var4] = false;
            }
         }

         for (int var7 = 0; var7 < 9; var7++) {
            if (this.p[var7] < 0) {
               for (int var9 = 9; var9 < 36; var9++) {
                  int var6 = this.p[var9];
                  if (var6 >= 0 && var1[var6] && !this.D[var9]) {
                     this.p[var7] = var6;
                     this.p[var9] = -1;
                     break;
                  }
               }
            }
         }

         for (int var8 = 0; var8 < 36; var8++) {
            if (!var2[var8] && !var3[var8]) {
               int var10 = this.p[var8] < 0 ? var8 : this.HAsInt();
               if (var10 < 0) {
                  break;
               }

               this.p[var10] = var8;
               var3[var8] = true;
            }
         }
      } else {
         throw new IllegalArgumentException("Expected 36 slots");
      }
   }

   private int HAsInt() {
      for (int var1 = 0; var1 < 36; var1++) {
         if (this.p[var1] < 0) {
            return var1;
         }
      }

      return -1;
   }

   public int[] yAsIntArray(boolean[] var1) {
      int[] var2 = (int[])this.p.clone();

      for (int var3 = 0; var3 < 36; var3++) {
         if (var2[var3] >= 0 && var1[var2[var3]]) {
            var2[var3] = -1;
         }
      }

      return var2;
   }

   public void KAsVoid(int var1, int var2) {
      if (var1 >= 0 && var1 < 36 && var2 >= 0 && var2 < 36) {
         this.I = null;
         int var3 = this.p[var1];
         byte var4 = 0;

         for (int var5 = 0; var5 < 36; var5++) {
            if (var5 != var1 && this.p[var5] == var2) {
               this.p[var5] = var3;
               this.D[var5] = false;
               var4 = 1;
               break;
            }
         }

         this.p[var1] = var2;
         this.D[var1] = true;
         if (var4 == 0 && var3 >= 0 && var3 != var2) {
            int var6 = this.HAsInt();
            if (var6 >= 0) {
               this.p[var6] = var3;
               this.D[var6] = false;
            }
         }
      }
   }

   public void NAsVoid(int var1, int var2) {
      this.I = null;

      for (int var3 = 0; var3 < 36; var3++) {
         if (this.p[var3] == var1) {
            this.p[var3] = var2;
         } else if (this.p[var3] == var2) {
            this.p[var3] = var1;
         }
      }
   }
}
