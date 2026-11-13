package com.larpclient.mixin.feature.entity;

import java.util.List;
import java.util.Map;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPart.Cuboid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ModelPart.class})
public interface ModelPartAccessor {
   @Accessor("field_3661")
   Map<String, ModelPart> larpWalkChildren();

   @Accessor("field_3663")
   List<Cuboid> larpWalkCubes();
}
