package com.larpclient.mixin;

import net.minecraft.client.render.ChunkRenderingDataPreparer;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({WorldRenderer.class})
public interface WorldRendererAccessor {
   @Accessor("field_45615")
   ChunkRenderingDataPreparer larpOcclusionGraph();
}
