package com.larpclient.mixin.feature.render;

import com.larpclient.TerrainMaterialClassifier;
import net.minecraft.client.render.BufferBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin({BufferBuilder.class})
public abstract class BufferBuilderMixin {
   public BufferBuilderMixin() {
   }

   @ModifyVariable(
      method = {"method_23919(FFFIFFIIFFF)V"},
      at = {@At("HEAD")},
      argsOnly = true,
      ordinal = 0
   )
   private int larpclient$modifyVariableVertex(int var1, float var2, float var3, float var4) {
      return TerrainMaterialClassifier.OAsInt(var1, var2, var3, var4);
   }
}
