package com.larpclient;

import java.nio.ByteBuffer;

public final class WaterDistortion extends SkyShaderEffect {
   public WaterDistortion() {
      super("caustics");
   }

   @Override
   protected void write(bl var1, ByteBuffer var2) {
      this.AAsVoid(var1, var2);
      YAsVoid(var2, var1.U.EAsFloat(), var1.U.zAsFloat(), var1.U.SAsFloat(), 0.0F);
   }
}
