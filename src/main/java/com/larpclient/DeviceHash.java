package com.larpclient;

public final class DeviceHash {
   private final b4 N;
   private int w;

   public DeviceHash(b4 var1) {
      this.N = var1;
      this.w = var1.ordinal();
   }

   public b4 VAsB4() {
      return b4.values()[this.w];
   }

   public b4 wAsB4() {
      return this.N;
   }

   public void lAsVoid(int var1) {
      this.w = Math.clamp((long)var1, 0, b4.values().length - 1);
   }

   public void fAsVoid(int var1) {
      this.lAsVoid(this.w + Integer.signum(var1));
   }

   public b4 AAsB4() {
      return this.N;
   }

   public b4 tAsB4() {
      return this.VAsB4();
   }
}
