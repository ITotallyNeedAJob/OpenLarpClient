package com.larpclient.module;

import com.larpclient.ChunkLightCapture;
import com.larpclient.FindingZoneBounds;
import com.larpclient.GrowthClueSummary;
import com.larpclient.ThemeSettings;
import com.larpclient.ae;
import com.larpclient.u;
import com.larpclient.wl;
import com.larpclient.xc;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.WorldChunk;

public final class SusChunkFinder extends sf {
   private final NumberSetting maxScanY = this.addSetting(
      new NumberSetting("Max Scan Y", "Highest growth/light height to inspect, capped at Y60", 48.0, -64.0, 60.0, 1.0)
   );
   private final NumberSetting minMatureClusters = this.addSetting(
      new NumberSetting("Min Mature Clusters", "Visible mature amethyst required per chunk; natural geodes can match", 8.0, 1.0, 128.0, 1.0)
   );
   private final NumberSetting minMaturePercent = this.addSetting(
      new NumberSetting("Min Mature Percent", "Mature clusters as a share of clusters plus buds", 80.0, 50.0, 100.0, 5.0)
   );
   private final NumberSetting sensitivity = this.addSetting(
      new NumberSetting("Sensitivity", "Higher requires more evidence: 5 points per step", 3.0, 1.0, 10.0, 1.0)
   );
   private final NumberSetting minScanY = this.addSetting(new NumberSetting("Min Scan Y", "Lowest growth/light height", -64.0, -64.0, 60.0, 1.0));
   private final BooleanSetting amethystLight = this.addSetting(
      new BooleanSetting("Amethyst Light", "Received amethyst light and low-light peaks, including hidden block clues", true)
   );
   private final BooleanSetting kelp = this.addSetting(new BooleanSetting("Kelp", "Tall or max-age kelp columns", true));
   private final BooleanSetting bamboo = this.addSetting(new BooleanSetting("Bamboo", "Bamboo columns at least 12 blocks tall", true));
   private final BooleanSetting berries = this.addSetting(new BooleanSetting("Berries", "Fully mature sweet berry bushes", true));
   private final BooleanSetting vines = this.addSetting(new BooleanSetting("Vines", "Hanging vines at least 7 blocks long", true));
   private final BooleanSetting dripstone = this.addSetting(new BooleanSetting("Dripstone", "Downward spikes at least 5 blocks long", true));
   private final BooleanSetting smartMode = this.addSetting(new BooleanSetting("Smart Mode", "Combine neighbouring clue chunks into scored zones", true));
   private final NumberSetting mergeRadius = this.addSetting(new NumberSetting("Merge Radius", "Chunk distance linking clues into zones", 1.0, 1.0, 4.0, 1.0));
   private final BooleanSetting centroidMarker = this.addSetting(new BooleanSetting("Centroid Marker", "Show the centre of qualifying zones", true));
   private GrowthClueSummary D = GrowthClueSummary.EMPTY;
   private List<FindingZoneBounds.Type_c> U = List.of();
   private final Set<Long> w = new HashSet<>();
   private com.larpclient.s P;
   private int Q;
   private WorldChunk f;

   public SusChunkFinder() {
      super("SusChunkFinder", "Mark possible player activity from growth and light clues");
      ChunkLightCapture.CAsVoid();

      for (Setting var2 : this.detectionSettings()) {
         var2.onChanged(var1 -> this.sAsVoid("detection changed"));
      }

      this.mergeRadius.visibleWhen(this.smartMode::getValue);
      this.centroidMarker.visibleWhen(this.smartMode::getValue);
   }

   @Override
   public List<Setting<?>> detectionSettings() {
      return List.of(
         this.maxScanY,
         this.minMatureClusters,
         this.minMaturePercent,
         this.sensitivity,
         this.minScanY,
         this.amethystLight,
         this.kelp,
         this.bamboo,
         this.berries,
         this.vines,
         this.dripstone,
         this.smartMode,
         this.mergeRadius
      );
   }

   @Override
   public List<Setting<?>> markerSettings() {
      ArrayList var1 = new ArrayList<>(super.markerSettings());
      var1.add(this.centroidMarker);
      return List.copyOf(var1);
   }

   public static boolean pAsBoolean(BlockState var0) {
      return var0.isOf(Blocks.AMETHYST_CLUSTER)
         || var0.isOf(Blocks.SMALL_AMETHYST_BUD)
         || var0.isOf(Blocks.MEDIUM_AMETHYST_BUD)
         || var0.isOf(Blocks.LARGE_AMETHYST_BUD);
   }

   private boolean TAsBoolean(u.Type_o var1) {
      return switch (var1) {
         case KELP -> this.kelp.getValue();
         case BAMBOO -> this.bamboo.getValue();
         case BERRIES -> this.berries.getValue();
         case VINES -> this.vines.getValue();
         case DRIPSTONE -> this.dripstone.getValue();
      };
   }

   @Override
   protected boolean candidate(BlockState var1) {
      return pAsBoolean(var1) || u.gAsBoolean(var1);
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
   protected void beginScan() {
      this.D = GrowthClueSummary.EMPTY;
      this.Q = Integer.MIN_VALUE;
   }

   @Override
   protected void clearEvidence() {
      this.beginScan();
      this.U = List.of();
      this.w.clear();
      this.P = null;
      this.f = null;
   }

   @Override
   protected void beforeScan() {
      this.P = ChunkLightCapture.HAsS(getMc().world);

      for (long var2 : this.P.qAsSet()) {
         this.qAsVoid(var2);
      }
   }

   @Override
   protected boolean sectionCandidate(WorldChunk var1, int var2) {
      ChunkPos var3 = var1.getPos();
      return super.sectionCandidate(var1, var2)
         || this.amethystLight.getValue() && this.P != null && this.P.GAsBoolean(var3.x, (var1.getBottomY() >> 4) + var2, var3.z);
   }

   @Override
   protected void beginChunk(WorldChunk var1) {
      this.f = var1;
   }

   @Override
   protected void count(BlockState var1, int var2) {
      if (var2 >= this.minimumY() && var2 <= this.maximumY()) {
         if (var1.isOf(Blocks.AMETHYST_CLUSTER)) {
            this.OAsVoid(var2);
         } else if (pAsBoolean(var1)) {
            this.gAsVoid();
         }
      }
   }

   private BlockState nAsBlockState(BlockPos var1) {
      if (var1.getY() < this.minimumY() || var1.getY() > this.maximumY()) {
         return Blocks.AIR.getDefaultState();
      } else if (this.f != null && var1.getX() >> 4 == this.f.getPos().x && var1.getZ() >> 4 == this.f.getPos().z) {
         return this.f.getBlockState(var1);
      } else {
         WorldChunk var2 = getMc().world.getChunkManager().getWorldChunk(var1.getX() >> 4, var1.getZ() >> 4);
         return var2 == null ? Blocks.AIR.getDefaultState() : var2.getBlockState(var1);
      }
   }

   @Override
   protected void scanBlock(WorldChunk var1, BlockState var2, int var3, int var4, int var5) {
      if (var4 >= this.minimumY() && var4 <= this.maximumY()) {
         this.count(var2, var4);
         if (u.gAsBoolean(var2) || this.amethystLight.getValue() && this.D.light() < 16) {
            BlockPos var6 = new BlockPos(var1.getPos().getStartX() + var3, var4, var1.getPos().getStartZ() + var5);
            if (this.amethystLight.getValue()
               && this.D.light() < 16
               && this.P != null
               && (
                  u.DAsBoolean(var2, this.P.OAsInt(var6.getX(), var4, var6.getZ()), var6, this::nAsBlockState)
                     || xc.qAsBoolean(var6, var1x -> this.P.OAsInt(var1x.getX(), var1x.getY(), var1x.getZ()), this::nAsBlockState)
               )) {
               this.D = this.D.plus(new GrowthClueSummary(1, 0, 0, 0, 0, 0));
               this.Q = var4;
            }

            if (u.gAsBoolean(var2)) {
               u.Type_o var7 = u.IAsU_Type_o(var2, var6, this::nAsBlockState);
               if (var7 != null && this.TAsBoolean(var7)) {
                  this.D = this.D.plus(switch (var7) {
                     case KELP -> new GrowthClueSummary(0, 1, 0, 0, 0, 0);
                     case BAMBOO -> new GrowthClueSummary(0, 0, 1, 0, 0, 0);
                     case BERRIES -> new GrowthClueSummary(0, 0, 0, 1, 0, 0);
                     case VINES -> new GrowthClueSummary(0, 0, 0, 0, 1, 0);
                     case DRIPSTONE -> new GrowthClueSummary(0, 0, 0, 0, 0, 1);
                  });
                  this.Q = var4;
               }
            }
         }
      }
   }

   @Override
   protected wl.Type_v evidence() {
      wl.Type_v var1 = super.evidence();
      return new wl.Type_v(var1.mature(), var1.buds(), 0, this.Q == Integer.MIN_VALUE ? var1.sampleY() : this.Q, ae.EMPTY, this.D, List.of());
   }

   @Override
   protected boolean qualifies(wl.Type_v var1) {
      return var1.matureAmethyst(this.minMatureClusters.getValue().intValue(), this.minMaturePercent.getValue().intValue())
         || var1.sus().score() >= this.sensitivity();
   }

   private int sensitivity() {
      return this.sensitivity.getValue().intValue() * 5;
   }

   @Override
   protected int zoneRadius() {
      return this.smartMode.getValue() ? this.mergeRadius.getValue().intValue() : 0;
   }

   @Override
   protected void afterScan() {
      Map<Long, wl.Type_v> var1 = this.FAsWl().EAsMap();
      HashSet var2 = new HashSet();
      var1.forEach((var2xx, var3x) -> {
         if (var3x.sus().score() > 0 || this.qualifies(var3x)) {
            var2.add(var2xx);
         }
      });
      ArrayList var3 = new ArrayList();

      for (FindingZoneBounds.Type_c var5 : (Iterable<FindingZoneBounds.Type_c>) (FindingZoneBounds.HAsList(var2, this.zoneRadius()))) {
         GrowthClueSummary var6 = GrowthClueSummary.EMPTY;
         byte var7 = 0;

         for (long var9 : var5.members()) {
            wl.Type_v var11 = var1.get(var9);
            var6 = var6.plus(var11.sus());
            var7 |= (byte)(this.qualifies(var11) ? 1 : 0);
         }

         int var12 = var7 == 0 && var6.score() < this.sensitivity() ? 0 : 1;

         for (long var10 : var5.members()) {
            this.jAsVoid(var10, var1.get(var10), var12 != 0);
         }

         if (var12 != 0) {
            var3.add(var5);
         }
      }

      this.U = List.copyOf(var3);
   }

   @Override
   protected void beginDraw() {
      this.w.clear();
   }

   @Override
   protected void drawEvidence(ChunkPos var1, wl.Type_v var2, ThemeSettings var3, int var4) {
      if (this.smartMode.getValue() && this.centroidMarker.getValue()) {
         for (FindingZoneBounds.Type_c var6 : this.U) {
            if (var6.members().contains(var1.toLong())) {
               long var7 = var6.members().stream().min(Long::compare).orElseThrow();
               if (this.w.size() < 24 && this.w.add(var7)) {
                  var3.NAsVoid(new Vec3d(var6.x(), (double)(var2.sampleY() + 2), var6.z()), "Possible activity · " + var6.members().size() + " chunks", var4);
               }
               break;
            }
         }
      }
   }

   @Override
   protected String describe(wl.Type_v var1) {
      return var1.sus().score() > 0 ? var1.sus().describe() : var1.mature() + " mature, " + var1.buds() + " buds (" + var1.maturePercent() + "% mature)";
   }

   @Override
   protected int tint() {
      return -3498764;
   }
}
