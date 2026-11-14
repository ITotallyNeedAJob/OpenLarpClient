package com.larpclient;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import net.minecraft.util.math.BlockPos;

public final class CooldownTracker {
   private final Map<Integer, Long> R = new HashMap<>();
   private BlockPos i;
   private long Y;
   private long I;
   private long P;

   public CooldownTracker() {
   }

   public void AAsVoid(long var1, IntPredicate var3, Predicate<BlockPos> var4) {
      this.R.entrySet().removeIf(var3xx -> var1 >= var3xx.getValue() || !var3.test(var3xx.getKey()));
      if (this.i != null && (var1 >= this.Y || var4.test(this.i))) {
         this.i = null;
      }
   }

   public boolean cAsBoolean(long var1, int var3) {
      return var1 >= this.I && !this.R.containsKey(var3);
   }

   public boolean IAsBoolean(long var1) {
      return var1 >= this.P && this.i == null && this.R.isEmpty();
   }

   public boolean UAsBoolean() {
      return !this.R.isEmpty();
   }

   public boolean JAsBoolean() {
      return this.i != null;
   }

   public void wAsVoid(long var1, int var3, int var4) {
      this.gAsVoid(var1, var3, var4, 10);
   }

   public void UAsVoid(long var1, BlockPos var3, int var4, boolean var5) {
      this.xAsVoid(var1, var3, var4, var5, 10);
   }

   public void gAsVoid(long var1, int var3, int var4, int var5) {
      this.I = var1 + (long)Math.max(1, var4);
      this.R.put(var3, var1 + (long)Math.clamp((long)var5, 2, 40));
   }

   public void xAsVoid(long var1, BlockPos var3, int var4, boolean var5, int var6) {
      this.P = var1 + (long)Math.max(1, var4);
      if (var5) {
         this.i = var3.toImmutable();
         this.Y = var1 + (long)Math.clamp((long)var6, 2, 40);
      }
   }

   public void nAsVoid(long var1, int var3) {
      this.P = var1 + (long)Math.max(1, var3);
   }

   public void zAsVoid() {
      this.R.clear();
      this.i = null;
      this.I = this.P = this.Y = 0L;
   }
}
