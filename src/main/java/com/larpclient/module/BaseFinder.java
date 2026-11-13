package com.larpclient.module;

import com.larpclient.BlockChangeEvidence;
import com.larpclient.ClientLog;
import com.larpclient.ThemeSettings;
import com.larpclient.ae;
import com.larpclient.qv;
import com.larpclient.wl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents.Load;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents.Unload;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;

public final class BaseFinder extends sf {
   private final ModeSetting detection = this.addSetting(
      new ModeSetting(
         "Detection",
         "Conservative: dedicated blocks. Balanced: storage plus utilities. Broad: more possible structures",
         "Balanced",
         "Conservative",
         "Balanced",
         "Broad"
      )
   );
   private final NumberSetting minStorage = this.addSetting(
      new NumberSetting("Min Storage", "Ordinary storage required in one 16-block height band; dedicated clues bypass this", 4.0, 1.0, 64.0, 1.0)
   );
   private final NumberSetting minUtilities = this.addSetting(
      new NumberSetting("Min Utilities", "Crafting/workstation blocks supporting ordinary storage in Balanced detection", 2.0, 1.0, 32.0, 1.0)
   );
   private final NumberSetting minEnderChests = this.addSetting(
      new NumberSetting("Min Ender Chests", "Ender chests needed in one 16-block height band outside the End", 1.0, 1.0, 64.0, 1.0)
   );
   private final NumberSetting minScanY = this.addSetting(new NumberSetting("Min Scan Y", "Lowest received block height to inspect", -64.0, -64.0, 60.0, 1.0));
   private final NumberSetting maxScanY = this.addSetting(
      new NumberSetting("Max Scan Y", "Highest block height to inspect, capped at Y60", 60.0, -64.0, 60.0, 1.0)
   );
   private final BooleanSetting terrainClues = this.addSetting(
      new BooleanSetting("Terrain Clues", "Include deepslate placement evidence in Broad detection; never proof on its own", true)
   );
   private final BooleanSetting baseBoxes = this.addSetting(
      new BooleanSetting("Base Boxes", "Outline the detected building evidence as well as its chunk", true)
   );
   private final ModeSetting baseStyle = this.addSetting(
      new ModeSetting("Base Style", "Box style for the detected building evidence", "Box", "Box", "Corners", "Filled")
   );
   private final qv.Type_s G = new qv.Type_s();
   private final BooleanSetting visibilityDiagnostic = this.addSetting(
      new BooleanSetting("Visibility Diagnostic", "Experimental received-data observations in latest.log; does not recover hidden blocks", false)
   );
   private final BlockChangeEvidence e = new BlockChangeEvidence();
   private ClientWorld E;
   private long f;
   private boolean L;
   private static BaseFinder u;
   private boolean V;
   private final BooleanSetting honeyClues = this.addSetting(
      new BooleanSetting("Honey Clues", "Full hives or nests after 10 seconds loaded; natural bee activity can qualify", true)
   );
   private final Map<Long, Long> W = new HashMap<>();
   private boolean H;

   public BaseFinder() {
      super("Base Finder", "Find base clues in loaded blocks");

      for (Setting var2 : this.detectionSettings()) {
         var2.onChanged(var1 -> this.sAsVoid("detection changed"));
      }

      this.minStorage.visibleWhen(() -> !this.detection.getValue().equals("Conservative"));
      this.minUtilities.visibleWhen(() -> this.detection.getValue().equals("Balanced"));
      this.terrainClues.visibleWhen(() -> this.detection.getValue().equals("Broad"));
      this.baseStyle.visibleWhen(this.baseBoxes::getValue);
      u = this;
      this.visibilityDiagnostic.onChanged(var1 -> {
         if (this.E != null) {
            this.CAsVoid("setting changed");
         }

         this.DAsVoid();
      });
      ClientChunkEvents.CHUNK_LOAD.register((Load)(var1, var2x) -> this.IAsVoid(var1, var2x));
      ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var1, var2x) -> {
         if (var1 == this.E) {
            this.e.NAsVoid(var2x.getPos().toLong());
         }

         if (getMc() != null && var1 == getMc().world) {
            this.W.remove(var2x.getPos().toLong());
         }
      });
   }

   @Override
   public List<Setting<?>> detectionSettings() {
      return List.of(this.detection, this.minStorage, this.minUtilities, this.minEnderChests, this.minScanY, this.maxScanY, this.terrainClues, this.honeyClues);
   }

   @Override
   public List<Setting<?>> markerSettings() {
      ArrayList var1 = new ArrayList<>(super.markerSettings());
      var1.add(this.baseBoxes);
      var1.add(this.baseStyle);
      return List.copyOf(var1);
   }

   public static Box FAsBox(ChunkPos var0, wl.Type_v var1) {
      Box var2 = var1.base().bounds();
      return var2 == null ? null : var2.offset((double)var0.getStartX(), 0.0, (double)var0.getStartZ()).expand(0.08);
   }

   @Override
   protected void drawEvidence(ChunkPos var1, wl.Type_v var2, ThemeSettings var3, int var4) {
      if (this.baseBoxes.getValue()) {
         Box var5 = FAsBox(var1, var2);
         if (var5 != null) {
            var3.AAsVoid(var5, this.baseStyle.getValue(), var4);
         }
      }
   }

   @Override
   protected boolean candidate(BlockState var1) {
      if (qv.GAsBoolean(var1)) {
         return this.honeyClues.getValue();
      } else {
         qv.Type_k var2 = qv.xAsQv_Type_k(var1, 16, this.V);
         if (var2 == qv.Type_k.DEDICATED) {
            return true;
         } else {
            return this.detection.getValue().equals("Conservative")
               ? false
               : var2 == qv.Type_k.STORAGE
                  || var2 == qv.Type_k.UTILITY
                  || this.detection.getValue().equals("Broad") && (var2 == qv.Type_k.CRAFTED || var2 == qv.Type_k.ALTERED && this.terrainClues.getValue());
         }
      }
   }

   @Override
   protected int minimumY() {
      return Math.min(this.minScanY.getValue().intValue(), this.maxScanY.getValue().intValue());
   }

   @Override
   protected int maximumY() {
      return Math.min(60, Math.max(this.minScanY.getValue().intValue(), this.maxScanY.getValue().intValue()));
   }

   @Override
   protected void beforeScan() {
      ChunkPos var1 = getMc().player.getChunkPos();
      this.W
         .keySet()
         .removeIf(
            var2 -> {
               ChunkPos var3 = new ChunkPos(var2);
               return Math.abs((long)var3.x - (long)var1.x) > (long)this.scanRadius()
                  || Math.abs((long)var3.z - (long)var1.z) > (long)this.scanRadius()
                  || getMc().world.getChunkManager().getWorldChunk(var3.x, var3.z) == null;
            }
         );
   }

   @Override
   protected void beginChunk(WorldChunk var1) {
      long var2 = this.W.computeIfAbsent(var1.getPos().toLong(), var1x -> this.UAsLong());
      this.H = this.UAsLong() - var2 >= 200L;
   }

   @Override
   protected void beginScan() {
      this.G.AAsVoid();
      this.H = false;
      this.V = getMc() != null && getMc().world != null && getMc().world.getRegistryKey().equals(World.END);
   }

   @Override
   protected void count(BlockState var1, int var2) {
      this.count(var1, 0, var2, 0);
   }

   @Override
   protected void count(BlockState var1, int var2, int var3, int var4) {
      if (var3 >= this.minimumY() && var3 <= this.maximumY() && (!qv.GAsBoolean(var1) || this.honeyClues.getValue() && this.H)) {
         this.G.vAsVoid(var1, var2, var3, var4, this.V, this.detection.getValue().equals("Broad") && this.terrainClues.getValue());
      }
   }

   @Override
   protected wl.Type_v evidence() {
      ae var1 = this.G
         .mAsAe(
            this.detection.getValue(),
            this.minStorage.getValue().intValue(),
            this.minUtilities.getValue().intValue(),
            this.minEnderChests.getValue().intValue()
         );
      return new wl.Type_v(0, 0, 0, var1.sampleY(), var1);
   }

   @Override
   protected boolean qualifies(wl.Type_v var1) {
      return var1.base()
         .qualifies(
            this.detection.getValue(),
            this.minStorage.getValue().intValue(),
            this.minUtilities.getValue().intValue(),
            this.minEnderChests.getValue().intValue()
         );
   }

   @Override
   protected String describe(wl.Type_v var1) {
      return var1.base().describe();
   }

   @Override
   protected int tint() {
      return -6629451;
   }

   @Override
   protected void clearEvidence() {
      this.G.AAsVoid();
      this.W.clear();
      this.H = false;
      if (this.E != null) {
         this.CAsVoid("scan reset or lifecycle cleanup");
      }

      this.DAsVoid();
   }

   public List<Setting<?>> WAsList() {
      return List.of(this.visibilityDiagnostic);
   }

   private void DAsVoid() {
      this.e.EAsVoid();
      this.E = null;
      this.f = 0L;
      this.L = false;
   }

   private boolean dAsBoolean(ClientWorld var1) {
      if (this.visibilityDiagnostic.getValue() && !this.L && this.isRuntimeActive() && this.isInGame() && getMc().world == var1 && getMc().isOnThread()) {
         if (this.E != var1) {
            if (this.E != null) {
               this.CAsVoid("world changed");
            }

            this.DAsVoid();
            this.E = var1;
            ClientLog.GAsVoid(
               "BaseFinding", "Visibility diagnostic active: received block-entity chunk snapshots and server-verified updates only; no bypass assumed"
            );
         }

         return true;
      } else {
         return false;
      }
   }

   private boolean tAsBoolean(BlockPos var1) {
      if (var1.getY() >= this.minimumY() && var1.getY() <= this.maximumY()) {
         ChunkPos var2 = getMc().player.getChunkPos();
         return Math.abs((long)(var1.getX() >> 4) - (long)var2.x) <= (long)this.scanRadius()
            && Math.abs((long)(var1.getZ() >> 4) - (long)var2.z) <= (long)this.scanRadius();
      } else {
         return false;
      }
   }

   public static boolean hAsBoolean(BlockState var0, int var1, boolean var2) {
      qv.Type_k var3 = qv.xAsQv_Type_k(var0, var1, var2);
      return var3 != qv.Type_k.NONE && var3 != qv.Type_k.ALTERED || var0.isOf(Blocks.SPAWNER);
   }

   public static boolean UAsBoolean(BlockState var0) {
      return var0.isOf(Blocks.DEEPSLATE) || var0.isOf(Blocks.STONE) || var0.isOf(Blocks.NETHERRACK) || var0.isOf(Blocks.END_STONE);
   }

   private void wAsVoid(BlockPos var1, BlockState var2, BlockChangeEvidence.Type_k var3) {
      if (this.tAsBoolean(var1)) {
         if (!this.e.jAsBoolean(this.f)) {
            this.e.qAsVoid(var3, 1L);
         } else {
            this.e
               .ZAsVoid(
                  var1.asLong(),
                  ChunkPos.toLong(var1.getX() >> 4, var1.getZ() >> 4),
                  var2.toString(),
                  hAsBoolean(var2, var1.getY(), this.E.getRegistryKey().equals(World.END)),
                  UAsBoolean(var2),
                  var3,
                  this.f
               );
         }
      }
   }

   private void IAsVoid(ClientWorld var1, WorldChunk var2) {
      try {
         if (!this.dAsBoolean(var1)) {
            return;
         }

         ChunkPos var3 = getMc().player.getChunkPos();
         ChunkPos var4 = var2.getPos();
         if (Math.abs((long)var4.x - (long)var3.x) > (long)this.scanRadius() || Math.abs((long)var4.z - (long)var3.z) > (long)this.scanRadius()) {
            return;
         }

         this.e.TAsVoid();

         for (long var6 : this.e.gAsList(var4.toLong())) {
            BlockPos var8 = BlockPos.fromLong(var6);
            this.wAsVoid(var8, var2.getBlockState(var8), BlockChangeEvidence.Type_k.CHUNK);
         }

         int var10 = 0;

         for (BlockEntity var7 : var2.getBlockEntities().values()) {
            if (var10 == 256) {
               this.e.qAsVoid(BlockChangeEvidence.Type_k.CHUNK, (long)(var2.getBlockEntities().size() - var10));
               break;
            }

            var10++;
            if (!var7.isRemoved()) {
               this.wAsVoid(var7.getPos(), var7.getCachedState(), BlockChangeEvidence.Type_k.CHUNK);
            }
         }
      } catch (RuntimeException var91) {
         this.xAsVoid(var91);
      }
   }

   public static void AAsVoid(ClientWorld var0, BlockPos var1, BlockState var2) {
      if (u != null && u.isEnabled()) {
         BaseFinder var3 = u;
         if (var3 != null) {
            try {
               if (!var3.dAsBoolean(var0)) {
                  return;
               }

               if (var0.getChunkManager().getWorldChunk(var1.getX() >> 4, var1.getZ() >> 4) == null) {
                  return;
               }

               var3.wAsVoid(var1, var2, BlockChangeEvidence.Type_k.UPDATE);
            } catch (RuntimeException var5) {
               var3.xAsVoid(var5);
            }
         }
      }
   }

   private void xAsVoid(RuntimeException var1) {
      this.L = true;
      ClientLog.hAsVoid("BaseFinding", "Visibility diagnostic failed; observations paused until reset, native updates and Base Finder remain active", var1);
   }

   private void CAsVoid(String var1) {
      ClientLog.GAsVoid("BaseFinding", "Visibility diagnostic [{}]: {}", var1, this.e.hAsXj_Type_y());

      for (BlockChangeEvidence.Type_i var3 : this.e.jAsList(8)) {
         BlockPos var4 = BlockPos.fromLong(var3.position());
         ClientLog.GAsVoid(
            "BaseFinding",
            "Visibility observation [{}, {}, {}] {} tick {}: {} -> {} ({})",
            var4.getX(),
            var4.getY(),
            var4.getZ(),
            var3.source(),
            var3.tick(),
            var3.before(),
            var3.after(),
            var3.transition()
         );
      }
   }

   @Override
   public void onTick() {
      super.onTick();
      if (this.dAsBoolean(getMc().world)) {
         this.f++;
         long var1;
         if (((var1 = this.f % 20L - 0L) == 0L ? 0 : (var1 < 0L ? -1 : 1)) == 0) {
            this.e.kAsVoid(this.f);
         }

         long var2;
         if (((var2 = this.f % 200L - 0L) == 0L ? 0 : (var2 < 0L ? -1 : 1)) == 0) {
            this.CAsVoid("10 seconds");
         }
      }
   }

   @Override
   public String getStatus() {
      return super.getStatus()
         + (
            !this.visibilityDiagnostic.getValue()
               ? ""
               : (this.L ? " · diagnostic paused" : " · diagnostic: " + this.e.hAsXj_Type_y().tracked() + " observations")
         );
   }
}
