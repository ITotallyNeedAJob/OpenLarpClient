package com.larpclient.mixin;

import com.larpclient.ParticleEmitter;
import com.larpclient.ParticleJoint;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReferenceArray;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BuiltChunkStorage;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.ChunkRenderingDataPreparer;
import net.minecraft.client.render.ChunkRenderingDataPreparer.ChunkInfo;
import net.minecraft.client.render.chunk.ChunkBuilder.BuiltChunk;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ChunkRenderingDataPreparer.class})
public abstract class ChunkRenderingDataPreparerMixin {
   @Shadow
   private BuiltChunkStorage field_45623;
   @Unique
   private boolean Q;
   @Unique
   private volatile LongOpenHashSet E;
   @Unique
   private volatile BlockPos I;

   public ChunkRenderingDataPreparerMixin() {
   }

   @Shadow
   public abstract void method_52817();

   @Inject(
      method = {"method_52834(ZLnet/minecraft/class_4184;Lnet/minecraft/class_4604;Ljava/util/List;Lit/unimi/dsi/fastutil/longs/LongOpenHashSet;)V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectV(CallbackInfo var1) {
      boolean var2 = p5.oAsBoolean();
      if (var2 != this.Q) {
         this.method_52817();
      }

      this.Q = var2;
   }

   @Inject(
      method = {"method_52833(ZLnet/minecraft/class_4184;Lnet/minecraft/class_243;Lit/unimi/dsi/fastutil/longs/LongOpenHashSet;)V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectV(boolean var1, Camera var2, Vec3d var3, LongOpenHashSet var4, CallbackInfo var5) {
      LongOpenHashSet var6 = null;
      ClientWorld var7 = MinecraftClient.getInstance().world;
      if (this.Q && var7 != null) {
         var6 = new LongOpenHashSet();
         AtomicReferenceArray var8 = var7.getChunkManager().chunks.chunks;

         for (int var9 = 0; var9 < var8.length(); var9++) {
            WorldChunk var10 = (WorldChunk)var8.get(var9);
            if (var10 != null) {
               ChunkPos var11 = var10.getPos();
               if (var7.getChunkManager().getChunk(var11.x, var11.z, ChunkStatus.FULL, false) == var10) {
                  var6.add(var11.toLong());
               }
            }
         }
      }

      this.I = var2.getBlockPos().toImmutable();
      this.E = var6;
   }

   @Inject(
      method = {"method_52821(Lnet/minecraft/class_4184;Ljava/util/Queue;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectV(Camera var1, Queue<ChunkInfo> var2, CallbackInfo var3) {
      LongOpenHashSet var4 = this.E;
      if (var4 != null && this.field_45623 != null && this.I != null) {
         HeightLimitView var5 = this.field_45623.getWorld();
         pj.MAsVoid(var4, this.I, this.field_45623.getViewDistance(), var5.getBottomSectionCoord(), var5.getTopSectionCoord(), var2xx -> {
            BuiltChunk var4x = this.field_45623.getRenderedChunk(ChunkSectionPos.from(var2xx).getMinPos());
            if (var4x != null) {
               var2.add(ChunkInfoInvoker.larpCreateNode(var4x, null, 0));
            }
         });
         var3.cancel();
      }
   }

   @Inject(
      method = {"method_52819(Lnet/minecraft/class_1923;)V"},
      at = {@At("TAIL")}
   )
   private void larpclient$injectV2(CallbackInfo var1) {
      if (this.Q) {
         this.method_52817();
      }
   }

   @Inject(
      method = {"method_52826(Lnet/minecraft/class_769;)V"},
      at = {@At("TAIL")}
   )
   private void larpclient$injectV(BuiltChunkStorage var1, CallbackInfo var2) {
      this.E = null;
      this.I = null;
      this.Q = false;
   }
}
