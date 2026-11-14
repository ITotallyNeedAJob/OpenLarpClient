package com.larpclient;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.state.CameraRenderState;

public interface QueueNode {
   String effectName();

   void tick(MinecraftClient var1);

   void prepare(float var1, CameraRenderState var2);

   void render(EffectGeometryRenderer var1, boolean var2);

   boolean glowing();

   default boolean soft() {
      return false;
   }

   void clear();

   default void SAsVoid() {
      this.clear();
   }
}
