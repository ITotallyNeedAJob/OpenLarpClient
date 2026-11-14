package com.larpclient;

import java.util.List;
import java.util.random.RandomGenerator;

public final class NetworkBridge implements RandomStringGenerator {
   private List<String> k = List.of();
   private String b = "";
   private String i = "";
   private ModuleRegistry p;
   private MediaSessionMetadata Q;
   private String A = "";
   private int j = -1;
   private double t;
   private double s;
   private double v = 4.0;
   private boolean g;

   public NetworkBridge() {
   }

   public boolean CAsBoolean(LyricsController.Type_r var1, long var2) {
      MediaSessionMetadata var4 = var1.playback();
      ModuleRegistry var5 = var1.lyrics();
      int var6 = var4 != null && var2 - var4.observedAt() > 4000000000L ? 1 : 0;
      if (var4 != this.Q) {
         this.Q = var4;
         this.A = var4 == null ? "" : var4.key();
      }

      String var7 = var4 != null && var6 == 0 ? this.A : "";
      int var8 = -1;
      String var9 = "";
      double var10 = var4 == null ? 0.0 : var4.positionAt(var2);
      this.s = 0.0;
      this.v = 4.0;
      if (!var7.isEmpty() && var5 != null && var5.timed() != null) {
         TimedLyricsParser var12 = var5.timed();
         var8 = var12.hAsInt(var10);
         var9 = var12.yAsString(var8, var10);
         if (var8 >= 0) {
            double var13 = var12.wAsList().get(var8).seconds();
            this.s = Math.max(0.0, var10 - var13);
            double var15 = var8 + 1 < var12.wAsList().size() ? var12.wAsList().get(var8 + 1).seconds() : var4.duration();
            this.v = Math.clamp(var15 - var13, 0.2, 18.0);
         }
      }

      this.g = !this.b.equals(var7) || this.p != var5 || var8 < this.j || var8 > this.j + 1 || Math.abs(var10 - this.t) > 1.25;
      this.t = var10;
      if (this.b.equals(var7) && this.p == var5 && this.j == var8 && this.i.equals(var9)) {
         return false;
      } else {
         this.b = var7;
         this.p = var5;
         this.j = var8;
         this.i = var9;
         if (var7.isEmpty() || var5 == null) {
            this.k = List.of();
         } else if (var5.timed() == null) {
            this.k = var5.plain().stream().filter(var0 -> !var0.isBlank()).limit(1L).toList();
         } else {
            this.k = var9.isBlank() ? List.of() : List.of(var9);
         }

         return true;
      }
   }

   public double aAsDouble() {
      return this.s;
   }

   public int AAsInt() {
      return this.j;
   }

   public double xAsDouble() {
      return this.v;
   }

   public boolean fAsBoolean() {
      return this.g;
   }

   public boolean SAsBoolean() {
      return this.p != null && this.p.timed() != null && !this.i.isBlank();
   }

   @Override
   public String next(double var1, RandomGenerator var3) {
      return this.k.isEmpty() ? null : this.k.get(var3.nextInt(this.k.size()));
   }

   public List<String> GAsList() {
      return this.k;
   }

   public void iAsVoid() {
      this.k = List.of();
      this.b = this.i = "";
      this.p = null;
      this.j = -1;
      this.Q = null;
      this.A = "";
      this.t = this.s = 0.0;
      this.v = 4.0;
      this.g = false;
   }
}
