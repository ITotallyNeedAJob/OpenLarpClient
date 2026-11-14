package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.LightLayerParser;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents.Unload;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.DebugRender;
import net.minecraft.client.render.DrawStyle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.LightType;
import net.minecraft.world.debug.gizmo.GizmoDrawing;

public final class LightDebug extends Module {
   private final NumberSetting maxY = this.addSetting(new NumberSetting("Max Y", "Highest layer to inspect, starting at Y -64", -50.0, -64.0, 16.0, 1.0));
   private final NumberSetting chunkRadius = this.addSetting(
      new NumberSetting("Chunk Radius", "Loaded square radius around your current chunk", 8.0, 1.0, 12.0, 1.0)
   );
   private final NumberSetting alpha = this.addSetting(new NumberSetting("Alpha", "Opacity of light-strength colored block volumes", 160.0, 0.0, 255.0, 1.0));
   private final NumberSetting renderLimit = this.addSetting(
      new NumberSetting("Render Limit", "Nearest chunk light runs drawn per frame; all scan data is retained", 16384.0, 1024.0, 65536.0, 1024.0)
   );
   private final Map<Long, List<LightLayerParser.Type_p>> r = new HashMap<>();
   private List<LightDebug.Type_s> u = List.of();
   private ClientWorld q;
   private ChunkPos U;
   private int A;
   private int d;
   private int W;
   private List<ChunkPos> F = List.of();
   private byte[] Q;
   private int a;
   private int f;
   private boolean j;
   private final Mutable N = new Mutable();
   private static final int I = 32768;

   public LightDebug() {
      super("Light Debug", "Show underground block light brighter than the sky", Category.RENDER, -1);
      this.maxY.onChanged(var1 -> this.IAsVoid());
      this.chunkRadius.onChanged(var1 -> this.IAsVoid());
      WorldRenderEvents.BEFORE_DEBUG_RENDER.register((DebugRender)var1 -> this.iAsVoid());
      ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var1, var2) -> {
         if (var1 == this.q) {
            this.r.remove(var2.getPos().toLong());
            this.SAsVoid();
         }
      });
   }

   public List<Setting<?>> pAsList() {
      return List.of(this.maxY, this.chunkRadius);
   }

   public List<Setting<?>> NAsList() {
      return List.of(this.alpha, this.renderLimit);
   }

   private void IAsVoid() {
      this.r.clear();
      this.u = List.of();
      this.F = List.of();
      this.Q = null;
      this.U = null;
      this.d = this.W = this.A = 0;
      this.j = true;
   }

   @Override
   public void onEnable() {
      this.IAsVoid();
      this.q = getMc().world;
      ClientLog.GAsVoid("BaseFinding", "Light Debug active: block light above sky, Y -64..{}, radius {}", this.maxY.getValue(), this.chunkRadius.getValue());
   }

   @Override
   public void onDisable() {
      ClientLog.XAsVoid("BaseFinding", "Light Debug cleanup: {} lit chunks", this.r.size());
      this.IAsVoid();
      this.q = null;
   }

   private void ZAsVoid(ChunkPos var1) {
      this.U = var1;
      this.a = Math.max(-64, this.q.getBottomY());
      this.f = Math.min(this.maxY.getValue().intValue(), this.q.getTopYInclusive());
      int var2 = this.chunkRadius.getValue().intValue();
      ArrayList<ChunkPos> var3 = new ArrayList<>();
      this.r
         .keySet()
         .removeIf(
            var2xx -> {
               ChunkPos var3x = new ChunkPos(var2xx);
               return Math.abs((long)var3x.x - (long)this.U.x) > (long)var2
                  || Math.abs((long)var3x.z - (long)this.U.z) > (long)var2
                  || this.q.getChunkManager().getWorldChunk(var3x.x, var3x.z) == null;
            }
         );

      for (int var4 = -var2; var4 <= var2; var4++) {
         for (int var5 = -var2; var5 <= var2; var5++) {
            ChunkPos var6 = new ChunkPos(var1.x + var4, var1.z + var5);
            if (this.q.getChunkManager().getWorldChunk(var6.x, var6.z) != null) {
               var3.add(var6);
            }
         }
      }

      var3.sort(
         Comparator.comparingLong(var1xx -> (long)(var1xx.x - var1.x) * (long)(var1xx.x - var1.x) + (long)(var1xx.z - var1.z) * (long)(var1xx.z - var1.z))
      );
      this.F = List.copyOf(var3);
      this.d = this.W = 0;
      this.Q = null;
      this.j = false;
      this.SAsVoid();
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.q != mc.world) {
            this.IAsVoid();
            this.q = mc.world;
         }

         ChunkPos var1 = mc.player.getChunkPos();
         if (++this.A % 16 == 0) {
            this.j = true;
         }

         if (!var1.equals(this.U)) {
            this.F = List.of();
            this.Q = null;
            this.ZAsVoid(var1);
         } else if (this.d >= this.F.size() && this.j) {
            this.ZAsVoid(var1);
         }

         byte var2 = 0;
         int var3 = 32768;

         while (var3 > 0 && this.d < this.F.size()) {
            ChunkPos var4 = this.F.get(this.d);
            if (this.f >= this.a && this.q.getChunkManager().getWorldChunk(var4.x, var4.z) != null) {
               if (this.Q == null) {
                  this.Q = new byte[(this.f - this.a + 1) * 256];
               }

               int var5 = Math.min(this.Q.length, this.W + var3);

               for (var3 -= var5 - this.W; this.W < var5; this.W++) {
                  this.N.set(var4.getStartX() + (this.W & 15), this.a + (this.W >> 8), var4.getStartZ() + (this.W >> 4 & 15));
                  int var6 = this.q.getLightLevel(LightType.BLOCK, this.N);
                  if (var6 > 0 && LightLayerParser.tAsBoolean(var6, this.q.getLightLevel(LightType.SKY, this.N))) {
                     this.Q[this.W] = (byte)var6;
                  }
               }

               if (this.W == this.Q.length) {
                  List var7 = LightLayerParser.lAsList(this.Q, var4.x, var4.z, this.a);
                  if (var7.isEmpty()) {
                     this.r.remove(var4.toLong());
                  } else {
                     this.r.put(var4.toLong(), var7);
                  }

                  this.d++;
                  this.W = 0;
                  this.Q = null;
                  var2 = 1;
               }
            } else {
               this.r.remove(var4.toLong());
               this.d++;
               this.W = 0;
               this.Q = null;
               var2 = 1;
            }
         }

         if (var2 != 0) {
            this.SAsVoid();
         }
      }
   }

   private void SAsVoid() {
      if (this.U == null) {
         this.u = List.of();
      } else {
         this.u = this.r.entrySet().stream().sorted(Comparator.<Entry<Long, List<LightLayerParser.Type_p>>>comparingDouble(var1 -> {
            ChunkPos var2 = new ChunkPos(var1.getKey());
            double var3 = (double)var2.x - (double)this.U.x;
            double var5 = (double)var2.z - (double)this.U.z;
            return var3 * var3 + var5 * var5;
         }).thenComparingLong(Entry::getKey)).map(var0 -> new LightDebug.Type_s(var0.getKey(), var0.getValue())).toList();
      }
   }

   private void iAsVoid() {
      if (this.isRuntimeActive() && this.isInGame() && this.q == mc.world && !(this.alpha.getValue() <= 0.0)) {
         try {
            int var1 = 0;
            int var2 = this.renderLimit.getValue().intValue();

            for (LightDebug.Type_s var4 : this.u) {
               ChunkPos var5 = new ChunkPos(var4.key());
               if (this.q.getChunkManager().getWorldChunk(var5.x, var5.z) != null) {
                  for (LightLayerParser.Type_p var7 : var4.runs()) {
                     if (var1++ >= var2) {
                        return;
                     }

                     int var8 = LightLayerParser.bAsInt(var7.light(), this.alpha.getValue().intValue());
                     GizmoDrawing.box(
                           new Box(
                              (double)var7.x(),
                              (double)var7.y(),
                              (double)var7.z(),
                              (double)(var7.x() + var7.length()),
                              (double)(var7.y() + 1),
                              (double)(var7.z() + 1)
                           ),
                           DrawStyle.filled(var8)
                        )
                        .ignoreOcclusion();
                  }
               }
            }
         } catch (RuntimeException var91) {
            ClientLog.hAsVoid("BaseFinding", "Light Debug render failed; scan continues", var91);
         }
      }
   }

   @Override
   public String getStatus() {
      return this.u.size() + " lit chunks";
   }

   private static record Type_s(long key, List<LightLayerParser.Type_p> runs) {
   }
}
