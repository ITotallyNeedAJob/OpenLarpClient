package com.larpclient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class ChunkScanBudget<C> {
   private C c;
   private int H;
   private int Z;
   private int U;
   private int f;
   private int n;
   private List<int[]> D = List.of();

   public ChunkScanBudget() {
   }

   public void lAsVoid() {
      this.c = null;
      this.U = this.f = 0;
   }

   public void WAsVoid(int var1, int var2) {
      if (this.c != null && this.H == var1 && this.Z == var2) {
         this.c = null;
      }
   }

   public void sAsVoid(ChunkScanBudget.Type_v<C> var1, int var2, int var3, int var4, int var5) {
      if (var4 >= 1 && var4 <= 12 && var5 >= 1 && var5 <= 8) {
         if (this.n != var4) {
            ArrayList<int[]> var6 = new ArrayList<>();

            for (int var7 = -var4; var7 <= var4; var7++) {
               for (int var8 = -var4; var8 <= var4; var8++) {
                  var6.add(new int[]{var7, var8});
               }
            }

            var6.sort(Comparator.comparingInt(var0 -> var0[0] * var0[0] + var0[1] * var0[1]));
            this.D = List.copyOf(var6);
            this.n = var4;
            this.f = 0;
         }

         int var12 = 0;
         int var13 = 0;
         int var14 = 0;
         int var9 = 2 * var4 + 1;
         int var10 = var9 * var9;
         if (this.c != null) {
            var12++;
            if (Math.abs((long)this.H - (long)var2) > (long)var4 || Math.abs((long)this.Z - (long)var3) > (long)var4 || var1.loaded(this.H, this.Z) != this.c) {
               this.c = null;
            }
         }

         while (var12 < 16 && var13 < 64 && var14 < var5) {
            if (this.c == null) {
               int var11 = this.f++ % var10;
               this.f %= var10;
               this.H = var2 + this.D.get(var11)[0];
               this.Z = var3 + this.D.get(var11)[1];
               var12++;
               if (!var1.due(this.H, this.Z)) {
                  continue;
               }

               this.c = var1.loaded(this.H, this.Z);
               if (this.c == null) {
                  continue;
               }

               this.U = 0;
               var1.begin(this.c);
            }

            if (this.U >= var1.sections(this.c)) {
               var1.finish(this.H, this.Z);
               this.c = null;
            } else {
               int var15 = this.U++;
               var13++;
               if (var1.candidate(this.c, var15)) {
                  var14++;
                  var1.scan(this.c, var15);
               }
            }
         }
      } else {
         throw new IllegalArgumentException("Invalid chunk scan budget");
      }
   }

   public interface Type_v<C> {
      C loaded(int var1, int var2);

      boolean due(int var1, int var2);

      int sections(C var1);

      boolean candidate(C var1, int var2);

      void begin();

      default void begin(C var1) {
         this.begin();
      }

      void scan(C var1, int var2);

      void finish(int var1, int var2);
   }
}
