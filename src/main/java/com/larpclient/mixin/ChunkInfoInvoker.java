package com.larpclient.mixin;

import net.minecraft.client.render.ChunkRenderingDataPreparer.ChunkInfo;
import net.minecraft.client.render.chunk.ChunkBuilder.BuiltChunk;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({ChunkInfo.class})
public interface ChunkInfoInvoker {
   @Invoker("<init>")
   static ChunkInfo larpCreateNode(BuiltChunk var0, Direction var1, int var2) {
      throw new AssertionError("Freecam graph constructor invoker was not applied");
   }
}
