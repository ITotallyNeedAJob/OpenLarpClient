package com.larpclient.mixin.compat;

import com.larpclient.FakeSpawnerController;
import com.larpclient.WebRenderer;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.util.Collection;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.block.entity.state.MobSpawnerBlockEntityRenderState;
import net.minecraft.client.render.state.WorldRenderState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({WorldRenderer.class})
public abstract class WorldRendererMixin {
   @Shadow
   private ClientWorld field_4085;
   @Shadow
   @Final
   private BlockEntityRenderManager field_27741;

   public WorldRendererMixin() {
   }

   @WrapMethod(
      method = {"method_74314(Lnet/minecraft/class_4184;FLnet/minecraft/class_11658;)V"}
   )
   private void q(Camera var1, float var2, WorldRenderState var3, Operation<Void> var4) {
      var4.call(new Object[]{var1, var2, var3});
      FakeSpawnerController var5 = FakeSpawnerController.ZAsMa();
      if (this.field_4085 != null && var5.XAsBoolean()) {
         Collection var6 = var5.jAsCollection(this.field_4085);
         if (!var6.isEmpty()) {
            BlockEntityRenderer var7 = this.field_27741.get((MobSpawnerBlockEntity)var6.iterator().next());
            if (var7 != null) {
               Collection var8 = var5.gAsCollection(this.field_4085, var1.getCameraPos(), var7.getRenderDistance());
               LongOpenHashSet var9 = new LongOpenHashSet();
               var3.blockEntityRenderStates
                  .removeIf(var2x -> var5.vAsBoolean(var2x.pos) && !var7.rendersOutsideBoundingBox() && !WebRenderer.BAsFrustum().isVisible(new Box(var2x.pos)));

               for (BlockEntityRenderState var11 : var3.blockEntityRenderStates) {
                  if (var5.vAsBoolean(var11.pos)) {
                     var9.add(var11.pos.asLong());
                  }
               }

               int var22 = 0;

               for (BlockEntity var12 : (Iterable<BlockEntity>) (var8)) {
                  BlockPos var13 = var12.getPos();
                  if (!var12.isRemoved() && this.field_4085.isChunkLoaded(var13) && var5.vAsBoolean(var13)) {
                     BlockEntityRenderer var14 = this.field_27741.get(var12);
                     if (var14 != null
                        && var14.isInRenderDistance(var12, var1.getCameraPos())
                        && (var14.rendersOutsideBoundingBox() || WebRenderer.BAsFrustum().isVisible(new Box(var13)))) {
                        if (var9.contains(var13.asLong())) {
                           var22++;
                        } else {
                           BlockEntityRenderState var15 = this.field_27741.getRenderState(var12, var2, null);
                           if (var15 != null) {
                              var3.blockEntityRenderStates.add(var15);
                              var22++;
                           }
                        }
                     }
                  }
               }

               var5.dAsVoid(var8.size(), var22);

               for (BlockEntityRenderState var25 : var3.blockEntityRenderStates) {
                  if (var25 instanceof MobSpawnerBlockEntityRenderState var26 && var26.displayEntityRenderState != null && var5.vAsBoolean(var25.pos)) {
                     int var27 = LightmapTextureManager.getBlockLightCoordinates(var25.lightmapCoordinates);
                     int var28 = LightmapTextureManager.getSkyLightCoordinates(var25.lightmapCoordinates);

                     for (Direction var19 : Direction.values()) {
                        BlockPos var20 = var25.pos.offset(var19);
                        if (this.field_4085.isChunkLoaded(var20)) {
                           int var21 = WorldRenderer.getLightmapCoordinates(this.field_4085, var20);
                           var27 = Math.max(var27, LightmapTextureManager.getBlockLightCoordinates(var21));
                           var28 = Math.max(var28, LightmapTextureManager.getSkyLightCoordinates(var21));
                        }
                     }

                     var25.lightmapCoordinates = LightmapTextureManager.pack(var27, var28);
                     var26.displayEntityRenderState.light = var25.lightmapCoordinates;
                  }
               }
            }
         }
      }
   }
}
