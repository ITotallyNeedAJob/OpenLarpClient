package com.larpclient;

public final class DeviceHash {
   private final BlockErase N;
   private int w;

   public DeviceHash(BlockErase var1) {
      this.N = var1;
      this.w = var1.ordinal();
   }

   public BlockErase VAsB4() {
      return BlockErase.values()[this.w];
   }

   public BlockErase wAsB4() {
      return this.N;
   }

   public void lAsVoid(int var1) {
      this.w = Math.clamp((long)var1, 0, BlockErase.values().length - 1);
   }

   public void fAsVoid(int var1) {
      this.lAsVoid(this.w + Integer.signum(var1));
   }

   public BlockErase AAsB4() {
      return this.N;
   }

   public BlockErase tAsB4() {
      return this.VAsB4();
   }
}
