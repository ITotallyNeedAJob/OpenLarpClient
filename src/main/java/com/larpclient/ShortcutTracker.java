package com.larpclient;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public final class ShortcutTracker<T> {
   private final boolean[] B;
   private final boolean[] C;
   private final boolean[] n;
   private final LinkedHashSet<T> HexEdgeloop = new LinkedHashSet<>();

   public ShortcutTracker(int var1) {
      this.B = new boolean[var1];
      this.C = new boolean[var1];
      this.n = new boolean[var1];
   }

   public void CAsVoid(int var1) {
      if (var1 > 0 && var1 < this.n.length) {
         this.n[var1] = true;
      }
   }

   public void qAsVoid(int var1) {
      if (var1 > 0 && var1 < this.n.length) {
         this.B[var1] = true;
         this.n[var1] = false;
      }
   }

   public void gAsVoid() {
      Arrays.fill(this.n, false);
   }

   public void xAsVoid() {
      this.p.clear();
      Arrays.fill(this.C, false);
   }

   public void DAsVoid(T var1) {
      if (var1 != null) {
         this.p.add(var1);
      }
   }

   public void AAsVoid(T var1, int var2, boolean var3) {
      if (var2 > 0 && var2 < this.C.length) {
         this.C[var2] = var3;
         if (this.n[var2] || var3 && !this.B[var2]) {
            this.DAsVoid(var1);
         }
      }
   }

   public List<T> CAsList() {
      System.arraycopy(this.C, 0, this.B, 0, this.C.length);
      this.gAsVoid();
      return List.copyOf(this.p);
   }
}
