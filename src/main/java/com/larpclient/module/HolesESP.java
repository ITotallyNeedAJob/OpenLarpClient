package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.BlockPosChecker;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents.Load;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents.Unload;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.DebugRender;
import net.minecraft.client.render.DrawStyle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.debug.gizmo.GizmoDrawing;

public final class HolesESP extends Module {
   public static final int Q = 8192;
   public static final int y = 2048;
   public static final int o = 65536;
   private static final int O = 8192;
   private final NumberSetting opacity = this.addSetting(new NumberSetting("Opacity", "Fill alpha of passage boxes, from 0 to 255", 60.0, 0.0, 255.0, 1.0));
   private final ColorSetting G = this.addSetting(new ColorSetting("1x1 Color", "One-block-wide enclosed passages", -6283024));
   private final ColorSetting R = this.addSetting(new ColorSetting("3x1 Color", "Three-block-wide enclosed passages", -12058494));
   private final NumberSetting markerLimit = this.addSetting(
      new NumberSetting("Marker Limit", "Nearest boxes submitted in dense terrain", 4096.0, 256.0, 16384.0, 256.0)
   );
   private final Map<Long, Map<BlockPos, Integer>> Z = new HashMap<>();
   private final Set<Long> U = new HashSet<>();
   private final ArrayDeque<Long> d = new ArrayDeque<>();
   private final Map<Long, BlockPosChecker.Type_m> z = new HashMap<>();
   private final Set<Long> K = new HashSet<>();
   private Map<BlockPos, Integer> X;
   private List<HolesESP.Type_t> s = List.of();
   private ClientWorld l;
   private WorldChunk D;
   private long N;
   private int M;
   private int t;
   private int e;
   private int L;
   private boolean i;
   private static HolesESP u;

   public HolesESP() {
      super("Holes ESP", "Find enclosed vertical passages in received terrain", Category.RENDER, -1);
      u = this;
      this.markerLimit.onChanged(var1 -> this.GAsVoid());
      WorldRenderEvents.BEFORE_DEBUG_RENDER.register((DebugRender)var1 -> this.NAsVoid());
      ClientChunkEvents.CHUNK_LOAD.register((Load)(var1, var2) -> {
         if (this.isRuntimeActive() && this.l == var1) {
            this.gAsVoid(var2.getPos());
         }
      });
      ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var1, var2) -> {
         if (this.l == var1) {
            if (!this.isInGame()) {
               this.FAsVoid();
               this.l = null;
            } else {
               long var3 = var2.getPos().toLong();
               this.AAsVoid(var3);
               this.U.remove(var3);
               this.d.remove(var3);
               if (this.D != null && this.N == var3) {
                  this.ZAsVoid();
               }

               this.gAsVoid(var2.getPos());
               this.GAsVoid();
            }
         }
      });
   }

   public List<Setting<?>> BAsList() {
      return List.of(this.opacity, this.G, this.R);
   }

   public List<Setting<?>> gAsList() {
      return List.of(this.markerLimit);
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(k.RENDER_ONLY);
   }

   @Override
   public void onEnable() {
      this.FAsVoid();
      this.l = getMc().world;
      ClientLog.GAsVoid("BaseFinding", "Holes ESP active: four-level collision passages, {} reads/tick", 8192);
   }

   @Override
   public void onDisable() {
      this.FAsVoid();
      this.l = null;
      ClientLog.XAsVoid("BaseFinding", "Holes ESP released scan and marker state");
   }

   private void FAsVoid() {
      this.Z.clear();
      this.U.clear();
      this.d.clear();
      this.z.clear();
      this.K.clear();
      this.s = List.of();
      this.L = this.e = 0;
      this.i = false;
      this.ZAsVoid();
   }

   private void ZAsVoid() {
      this.D = null;
      this.X = null;
      this.M = 0;
   }

   private void AAsVoid(long var1) {
      Map var3 = this.Z.remove(var1);
      if (var3 != null) {
         this.L = this.L - var3.size();
      }
   }

   private int xAsInt() {
      return Math.clamp((long)((Integer)getMc().options.getViewDistance().getValue()).intValue(), 1, 32);
   }

   private boolean AAsBoolean(ChunkPos var1) {
      ChunkPos var2 = getMc().player.getChunkPos();
      int var3 = this.xAsInt();
      return Math.abs((long)var1.x - (long)var2.x) <= (long)var3 && Math.abs((long)var1.z - (long)var2.z) <= (long)var3;
   }

   private void JAsVoid(long var1) {
      ChunkPos var3 = new ChunkPos(Long.valueOf(var1));
      if (this.AAsBoolean(var3) && this.l.getChunkManager().getWorldChunk(var3.x, var3.z) != null && this.U.add(var1)) {
         this.d.addLast(var1);
      }
   }

   private void gAsVoid(ChunkPos var1) {
      for (int var2 = -1; var2 <= 1; var2++) {
         for (int var3 = -1; var3 <= 1; var3++) {
            long var4 = ChunkPos.toLong(var1.x + var2, var1.z + var3);
            if (this.D != null && this.N == var4) {
               this.ZAsVoid();
            }

            this.AAsVoid(var4);
            this.JAsVoid(var4);
         }
      }
   }

   public static void NAsVoid(ClientWorld var0, BlockPos var1) {
      if (u != null && u.isEnabled()) {
         HolesESP var2 = u;
         if (var2 != null && var2.isRuntimeActive() && var2.l == var0 && getMc().isOnThread()) {
            if (var2.K.size() < 4096) {
               var2.K.add(ChunkPos.toLong(var1.getX() >> 4, var1.getZ() >> 4));
            } else {
               var2.Z.clear();
               var2.L = 0;
               var2.e = 0;
            }
         }
      }
   }

   private BlockPosChecker.Type_m QAsX__Type_m(BlockPos var1) {
      if (this.l.isOutOfHeightLimit(var1)) {
         return BlockPosChecker.Type_m.UNKNOWN;
      } else {
         BlockPosChecker.Type_m var2 = this.z.get(var1.asLong());
         if (var2 != null) {
            return var2;
         } else {
            WorldChunk var3 = this.l.getChunkManager().getWorldChunk(var1.getX() >> 4, var1.getZ() >> 4);
            if (var3 == null) {
               return BlockPosChecker.Type_m.UNKNOWN;
            } else {
               this.t++;
               VoxelShape var4 = var3.getBlockState(var1).getCollisionShape(this.l, var1);
               BlockPosChecker.Type_m var5 = var4.equals(VoxelShapes.fullCube()) ? BlockPosChecker.Type_m.FULL : BlockPosChecker.Type_m.OPEN;
               this.z.put(var1.asLong(), var5);
               return var5;
            }
         }
      }
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.l != mc.world) {
            this.FAsVoid();
            this.l = mc.world;
         }

         int var10000 = this.e++;
         if (var10000 % 20 == 0) {
            this.aAsVoid();
         }

         if (!this.K.isEmpty()) {
            for (long var2 : this.K) {
               this.gAsVoid(new ChunkPos(var2));
            }

            this.K.clear();
            this.GAsVoid();
         }

         this.z.clear();
         this.t = 0;
         byte var10 = 0;

         for (int var11 = 0; var11 < 2048 && this.t < 8064; var11++) {
            if (this.D == null) {
               Long var3 = this.d.pollFirst();
               if (var3 == null) {
                  break;
               }

               this.U.remove(var3);
               ChunkPos var4 = new ChunkPos(var3);
               this.D = this.l.getChunkManager().getWorldChunk(var4.x, var4.z);
               if (this.D == null || !this.AAsBoolean(var4)) {
                  this.D = null;
                  continue;
               }

               this.N = var3;
               this.X = new HashMap<>();
               this.M = 0;
            }

            int var12 = this.l.getHeight();
            if (this.M >= 256 * var12) {
               this.AAsVoid(this.N);
               this.Z.put(this.N, this.X);
               this.L = this.L + this.X.size();
               this.ZAsVoid();
               var10 = 1;
            } else {
               int var13 = this.M;
               int var5 = this.l.getBottomY() + var13 % var12;
               int var6 = this.D.getSectionIndex(var5);
               if (this.D.getSectionArray()[var6].isEmpty()) {
                  this.M = this.M + Math.min(16 - (var5 & 15), var12 - var13 % var12);
               } else {
                  this.M++;
                  int var7 = var13 / var12;
                  BlockPos var8 = new BlockPos(this.D.getPos().getStartX() + var7 / 16, var5, this.D.getPos().getStartZ() + var7 % 16);
                  int var9 = BlockPosChecker.LAsInt(var8, this.l.getTopYInclusive(), this::QAsX__Type_m);
                  if (var9 >= 0) {
                     if (this.X.size() < 8192 && this.L + this.X.size() < 65536) {
                        this.X.put(var8, var9);
                     } else if (!this.i) {
                        this.i = true;
                        ClientLog.GAsVoid("BaseFinding", "Holes ESP dense-result ceiling reached; nearest retained markers remain visible");
                     }
                  }
               }
            }
         }

         this.z.clear();
         if (var10 != 0 || this.e % 20 == 0) {
            this.GAsVoid();
         }
      }
   }

   private void aAsVoid() {
      for (long var2 : new ArrayList<>(this.Z.keySet())) {
         ChunkPos var4 = new ChunkPos(var2);
         if (!this.AAsBoolean(var4) || this.l.getChunkManager().getWorldChunk(var4.x, var4.z) == null) {
            this.AAsVoid(var2);
         }
      }

      if (this.D != null && (!this.AAsBoolean(this.D.getPos()) || this.l.getChunkManager().getWorldChunk(this.D.getPos().x, this.D.getPos().z) != this.D)) {
         this.ZAsVoid();
      }

      this.d.removeIf(var1 -> {
         ChunkPos var2x = new ChunkPos(var1);
         int var3x = this.AAsBoolean(var2x) && this.l.getChunkManager().getWorldChunk(var2x.x, var2x.z) != null ? 0 : 1;
         if (var3x != 0) {
            this.U.remove(var1);
         }

         return var3x != 0;
      });
      ChunkPos var8 = mc.player.getChunkPos();
      int var9 = this.xAsInt();
      ArrayList var3 = new ArrayList();

      for (int var10 = var8.x - var9; var10 <= var8.x + var9; var10++) {
         for (int var5 = var8.z - var9; var5 <= var8.z + var9; var5++) {
            long var6 = ChunkPos.toLong(var10, var5);
            if (!this.Z.containsKey(var6)
               && (this.D == null || this.N != var6)
               && this.l.getChunkManager().getWorldChunk(var10, var5) != null
               && !this.U.contains(var6)) {
               var3.add(var6);
            }
         }
      }

      var3.sort(Comparator.comparingDouble(var1 -> {
         ChunkPos var2x = new ChunkPos((Long)var1);
         return Math.pow((double)(var2x.x - var8.x), 2.0) + Math.pow((double)(var2x.z - var8.z), 2.0);
      }));

      for (long var12 : (Iterable<Long>) (var3)) {
         this.JAsVoid(var12);
      }
   }

   private void GAsVoid() {
      if (this.l != null && getMc().player != null) {
         Vec3d var1 = mc.player.getEntityPos();
         this.s = this.Z
            .values()
            .stream()
            .flatMap(var0 -> var0.entrySet().stream())
            .map(var0 -> new HolesESP.Type_t(var0.getKey(), var0.getValue()))
            .sorted(Comparator.comparingDouble(var1xx -> var1xx.pos().getSquaredDistance(var1)))
            .limit(this.markerLimit.getValue().longValue())
            .toList();
      } else {
         this.s = List.of();
      }
   }

   private void NAsVoid() {
      if (this.isEnabled() && this.isRuntimeActive() && this.isInGame() && this.l == mc.world) {
         int var1 = Math.clamp((long)this.opacity.getValue().intValue(), 0, 255);
         if (var1 != 0) {
            ChunkPos var2 = mc.player.getChunkPos();
            int var3 = this.xAsInt();

            for (HolesESP.Type_t var5 : this.s) {
               if (Math.abs((long)(var5.pos().getX() >> 4) - (long)var2.x) <= (long)var3
                  && Math.abs((long)(var5.pos().getZ() >> 4) - (long)var2.z) <= (long)var3) {
                  int var6 = var5.kind() == 0 ? this.G.getValue() : this.R.getValue();
                  GizmoDrawing.box(BlockPosChecker.BAsBox(var5.pos(), var5.kind()), DrawStyle.filled(var1 << 24 | var6 & 16777215)).ignoreOcclusion();
               }
            }
         }
      }
   }

   @Override
   public String getStatus() {
      return this.L + " passages";
   }

   private static record Type_t(BlockPos pos, int kind) {
   }
}
