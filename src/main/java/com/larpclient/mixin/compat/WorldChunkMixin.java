package com.larpclient.mixin.compat;

import com.larpclient.FakeSpawnerController;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({WorldChunk.class})
public abstract class WorldChunkMixin {
   @Shadow
   @Final
   private World field_12858;

   public WorldChunkMixin() {
   }

   @Redirect(
      method = {"method_12208(Lnet/minecraft/class_2338;)Lnet/minecraft/class_2586;", "method_12204(Lnet/minecraft/class_2338;Lnet/minecraft/class_2487;)Lnet/minecraft/class_2586;", "method_12007(Lnet/minecraft/class_2586;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_2818;method_8320(Lnet/minecraft/class_2338;)Lnet/minecraft/class_2680;"
      )}
   )
   private BlockState larpclient$redirectHandler(WorldChunk var1, BlockPos var2) {
      if (this.field_12858 instanceof ClientWorld && FakeSpawnerController.ZAsMa().vAsBoolean(var2)) {
         int var3 = var1.getSectionIndex(var2.getY());
         return var3 >= 0 && var3 < var1.getSectionArray().length
            ? var1.getSection(var3).getBlockState(var2.getX() & 15, var2.getY() & 15, var2.getZ() & 15)
            : Blocks.AIR.getDefaultState();
      } else {
         return var1.getBlockState(var2);
      }
   }
}
