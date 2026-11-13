package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.GrowthClueSummary;
import com.larpclient.ThemeSettings;
import com.larpclient.ae;
import com.larpclient.wl;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.block.entity.TrialSpawnerBlockEntity;
import net.minecraft.block.spawner.TrialSpawnerLogic;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.WorldChunk;

public final class SpawnerFinder extends sf {
   public static final int F = 64;
   public static final int T = 256;
   public static final int s = 24;
   public static final int l = 16;
   private final BooleanSetting trialSpawners = this.addSetting(new BooleanSetting("Trial Spawners", "Also find received trial spawners", false));
   private final BooleanSetting spawnerBoxes = this.addSetting(new BooleanSetting("Spawner Boxes", "Outline exact received spawner positions", true));
   private final BooleanSetting spawnerLabels = this.addSetting(new BooleanSetting("Spawner Labels", "Received mob type, coordinates and distance", true));
   private final BooleanSetting rangeRings = this.addSetting(
      new BooleanSetting("Range Rings", "16-block reference ring; server activation ranges can differ", true)
   );
   private final BooleanSetting spawnerTracers = this.addSetting(
      new BooleanSetting("Spawner Tracers", "Lines from the camera to exact spawner positions", false)
   );
   private final List<wl.Type_g> Z = new ArrayList<>();
   private int m;
   private int L;
   private int W;

   public SpawnerFinder() {
      super("SpawnerFinder", "Highlight loaded spawners and their mob types");
      this.trialSpawners.onChanged(var1 -> this.sAsVoid("spawner types changed"));
   }

   @Override
   public List<Setting<?>> detectionSettings() {
      return List.of(this.trialSpawners);
   }

   @Override
   public List<Setting<?>> markerSettings() {
      ArrayList var1 = new ArrayList<>(super.markerSettings());
      var1.addAll(List.of(this.spawnerBoxes, this.spawnerLabels, this.rangeRings, this.spawnerTracers));
      return List.copyOf(var1);
   }

   public static boolean FAsBoolean(BlockState var0) {
      return var0.isOf(Blocks.SPAWNER);
   }

   @Override
   protected boolean candidate(BlockState var1) {
      return FAsBoolean(var1) || this.trialSpawners.getValue() && var1.isOf(Blocks.TRIAL_SPAWNER);
   }

   @Override
   protected void beginScan() {
      this.Z.clear();
   }

   @Override
   protected void clearEvidence() {
      this.Z.clear();
   }

   @Override
   protected void count(BlockState var1, int var2) {
      if (this.candidate(var1)) {
         this.AAsVoid(var2);
      }
   }

   @Override
   protected void scanBlock(WorldChunk var1, BlockState var2, int var3, int var4, int var5) {
      if (this.candidate(var2)) {
         this.AAsVoid(var4);
         if (this.Z.size() < 64) {
            BlockPos var6 = new BlockPos(var1.getPos().getStartX() + var3, var4, var1.getPos().getStartZ() + var5);
            boolean var7 = var2.isOf(Blocks.TRIAL_SPAWNER);
            String var8 = var7 ? "Trial Spawner" : "Spawner";

            try {
               BlockEntity var9 = var1.getBlockEntity(var6);
               Entity var10 = null;
               if (var9 instanceof MobSpawnerBlockEntity var11) {
                  var10 = var11.getLogic().getRenderedEntity(getMc().world, var6);
               } else if (var9 instanceof TrialSpawnerBlockEntity var12) {
                  TrialSpawnerLogic var13 = var12.getSpawner();
                  var10 = var13.getData().setDisplayEntity(var13, getMc().world, var12.getSpawnerState());
               }

               if (var10 != null) {
                  var8 = var10.getType().getName().getString() + (var7 ? " Trial Spawner" : " Spawner");
               }
            } catch (RuntimeException var141) {
               ClientLog.hAsVoid("BaseFinding", "Spawner mob label unavailable; retaining generic name and exact position", var141);
            }

            this.Z.add(new wl.Type_g(var3, var4, var5, var7, var8));
         }
      }
   }

   @Override
   protected wl.Type_v evidence() {
      wl.Type_v var1 = super.evidence();
      return new wl.Type_v(0, 0, var1.spawners(), var1.sampleY(), ae.EMPTY, GrowthClueSummary.EMPTY, this.Z);
   }

   @Override
   protected boolean qualifies(wl.Type_v var1) {
      return var1.spawners() > 0;
   }

   @Override
   protected String describe(wl.Type_v var1) {
      if (!var1.hits().isEmpty()) {
         wl.Type_g var2 = var1.hits().getFirst();
         return var2.name() + " · " + var1.spawners() + " blocks · Y:" + var2.y();
      } else {
         return var1.spawners() + " spawner blocks; first at Y:" + var1.sampleY();
      }
   }

   @Override
   protected void beginDraw() {
      this.m = this.L = this.W = 0;
   }

   @Override
   protected void drawEvidence(ChunkPos var1, wl.Type_v var2, ThemeSettings var3, int var4) {
      for (wl.Type_g var6 : var2.hits()) {
         if (this.m >= 256) {
            break;
         }

         BlockPos var7 = new BlockPos(var1.getStartX() + var6.x(), var6.y(), var1.getStartZ() + var6.z());
         BlockState var8 = getMc().world.getBlockState(var7);
         if (var6.trial() ? this.trialSpawners.getValue() && var8.isOf(Blocks.TRIAL_SPAWNER) : FAsBoolean(var8)) {
            this.m++;
            Vec3d var9 = Vec3d.ofCenter(var7);
            if (this.spawnerBoxes.getValue()) {
               var3.AAsVoid(new Box(var7).expand(0.03), "Box", var4);
            }

            if (this.spawnerLabels.getValue() && this.L++ < 24) {
               var3.NAsVoid(
                  var9.add(0.0, 1.0, 0.0),
                  var6.name()
                     + " · "
                     + Math.round(var9.distanceTo(getMc().player.getEntityPos()))
                     + "m · "
                     + var7.getX()
                     + ", "
                     + var7.getY()
                     + ", "
                     + var7.getZ(),
                  var4
               );
            }

            if (this.spawnerTracers.getValue()) {
               var3.IAsVoid(getMc().gameRenderer.getCamera().getCameraPos(), var9, var4);
            }

            if (this.rangeRings.getValue() && this.W++ < 16) {
               for (int var10 = 0; var10 < 48; var10++) {
                  double var11 = (double)var10 * Math.PI * 2.0 / 48.0;
                  double var13 = (double)(var10 + 1) * Math.PI * 2.0 / 48.0;
                  var3.IAsVoid(
                     var9.add(Math.cos(var11) * 16.0, 0.0, Math.sin(var11) * 16.0), var9.add(Math.cos(var13) * 16.0, 0.0, Math.sin(var13) * 16.0), var4
                  );
               }
            }
         }
      }
   }

   @Override
   protected int tint() {
      return -943189;
   }
}
