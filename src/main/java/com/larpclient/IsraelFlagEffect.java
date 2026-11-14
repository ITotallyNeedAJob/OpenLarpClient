package com.larpclient;

import java.nio.ByteBuffer;

public final class IsraelFlagEffect extends SkyShaderEffect {
   public IsraelFlagEffect() {
      super("israel_flag");
   }

   @Override
   protected void write(bl var1, ByteBuffer var2) {
      YAsVoid(var2, (float)this.T, var1.U.tAsFloat(), var1.U.horizonBlend() ? 1.0F : 0.0F, 0.0F);
   }
}
