package com.larpclient.module;

import com.larpclient.ChunkScanBudget;
import com.larpclient.ClientLog;
import com.larpclient.FindingZoneBounds;
import com.larpclient.NotificationHud;
import com.larpclient.ThemeSettings;
import com.larpclient.WidgetLayer;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents.Unload;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.DebugRender;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.WorldChunk;

public abstract class sf extends Module {
   private final NumberSetting scanRadius = this.addSetting(new NumberSetting("Scan Radius", "Loaded chunks around the player", 4.0, 1.0, 12.0, 1.0));
   private final NumberSetting sectionsPerTick = this.addSetting(
      new NumberSetting("Sections Per Tick", "At most 4096 block reads per matching section", 2.0, 1.0, 8.0, 1.0)
   );
   private final NumberSetting rescanSeconds = this.addSetting(
      new NumberSetting("Rescan Seconds", "Minimum interval between complete scans of a chunk", 2.0, 1.0, 30.0, 1.0)
   );
   private final NumberSetting markerY = this.addSetting(new NumberSetting("Marker Y", "Height of the thin chunk markers", 64.0, -64.0, 320.0, 1.0));
   private final NumberSetting maxMarkers = this.addSetting(new NumberSetting("Max Markers", "Nearest qualifying chunks drawn", 64.0, 1.0, 256.0, 1.0));
   private final ModeSetting markerStyle = this.addSetting(
      new ModeSetting("Marker Style", "Chunk outline or translucent slab", "Filled", "Box", "Corners", "Filled")
   );
   private final BooleanSetting labels = this.addSetting(new BooleanSetting("Labels", "Evidence labels on the nearest 24 chunk markers", true));
   private final BooleanSetting tracers = this.addSetting(new BooleanSetting("Tracers", "Lines from the camera to chunk markers", false));
   private final BooleanSetting notifications = this.addSetting(
      new BooleanSetting("Notifications", "Deduplicated discoveries, at most one alert per second", true)
   );
   private final BooleanSetting chatAlerts = this.addSetting(new BooleanSetting("Chat Alerts", "Include coordinates and observed evidence in local chat", true));
   private final BooleanSetting alertSound = this.addSetting(new BooleanSetting("Alert Sound", "Play a local chime for each delivered discovery alert", false));
   private final ThemeSettings b = new ThemeSettings(var1x -> this.addSetting(var1x));
   private final ChunkScanBudget<WorldChunk> p = new ChunkScanBudget<>();
   private final wl q = new wl();
   private final ArrayDeque<sf.Type_t> z = new ArrayDeque<>();
   private List<Long> t = List.of();
   private final Map<Long, Long> S = new HashMap<>();
   private ClientWorld a;
   private long n;
   private int v;
   private int y;
   private int O;
   private int h;
   private int x;
   private final ChunkScanBudget.Type_v<WorldChunk> j = new ChunkScanBudget.Type_v<WorldChunk>() {
      public WorldChunk loaded(int var1, int var2) {
         return sf.this.a.getChunkManager().getWorldChunk(var1, var2);
      }

      @Override
      public boolean due(int var1, int var2) {
         return sf.this.q.iAsBoolean(ChunkPos.toLong(var1, var2), sf.this.n, sf.this.rescanSeconds.getValue().longValue() * 20L);
      }

      public int sections(WorldChunk var1) {
         return var1.getSectionArray().length;
      }

      @Override
      public void begin() {
         sf.this.v = sf.this.y = sf.this.O = 0;
         sf.this.h = Integer.MIN_VALUE;
         sf.this.beginScan();
      }

      public void begin(WorldChunk var1) {
         this.begin();
         sf.this.beginChunk(var1);
      }

      public boolean candidate(WorldChunk var1, int var2) {
         int var3 = var1.getBottomY() + var2 * 16;
         return (long)var3 + 15L >= (long)sf.this.minimumY() && var3 <= sf.this.maximumY() && sf.this.sectionCandidate(var1, var2);
      }

      public void scan(WorldChunk var1, int var2) {
         int var3 = var1.getBottomY() + var2 * 16;
         ChunkSection var4 = var1.getSectionArray()[var2];
         int var5 = (int)Math.min(15L, (long)sf.this.maximumY() - (long)var3);
         int var6 = (int)Math.max(0L, (long)sf.this.minimumY() - (long)var3);

         for (int var7 = var6; var7 <= var5; var7++) {
            for (int var8 = 0; var8 < 16; var8++) {
               for (int var9 = 0; var9 < 16; var9++) {
                  sf.this.scanBlock(var1, var4.getBlockState(var8, var7, var9), var8, var3 + var7, var9);
               }
            }
         }
      }

      @Override
      public void finish(int var1, int var2) {
         wl.Type_v var3 = sf.this.evidence();
         long var4 = ChunkPos.toLong(var1, var2);
         if (sf.this.q.PAsBoolean(var4, var3, sf.this.qualifies(var3), sf.this.n)) {
            sf.this.QAsVoid(var4, var3);
         }
      }
   };

   protected sf(String var1, String var2) {
      super(var1, var2, Category.RENDER, -1);
      this.b.DAsVoid(() -> true, () -> true, () -> true, () -> this.markerStyle.pAsBoolean("Filled"));
      this.scanRadius.onChanged(var1x -> this.sAsVoid("radius changed"));
      this.rescanSeconds.onChanged(var1x -> this.sAsVoid("rescan interval changed"));
      this.notifications.onChanged(var1x -> {
         this.z.clear();
         this.x = 0;
      });
      this.chatAlerts.visibleWhen(this.notifications::getValue);
      this.alertSound.visibleWhen(this.notifications::getValue);
      WorldRenderEvents.BEFORE_DEBUG_RENDER.register((DebugRender)var1x -> this.jAsVoid());
      ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var1x, var2x) -> {
         if (var1x == this.a) {
            ChunkPos var3 = var2x.getPos();
            this.q.rAsVoid(var3.toLong());
            this.p.WAsVoid(var3.x, var3.z);
            this.z.removeIf(var1xx -> var1xx.key() == var3.toLong());
         }
      });
   }

   protected abstract boolean candidate(BlockState var1);

   protected abstract boolean qualifies(wl.Type_v var1);

   protected abstract String describe(wl.Type_v var1);

   protected abstract int tint();

   protected int minimumY() {
      return Integer.MIN_VALUE;
   }

   protected void beginScan() {
   }

   protected void beginChunk(WorldChunk var1) {
   }

   protected void beforeScan() {
   }

   protected void afterScan() {
   }

   protected void beginDraw() {
   }

   protected int zoneRadius() {
      return 0;
   }

   protected final wl FAsWl() {
      return this.q;
   }

   protected final long UAsLong() {
      return this.n;
   }

   protected final void qAsVoid(long var1) {
      ChunkPos var3 = new ChunkPos(var1);
      this.p.WAsVoid(var3.x, var3.z);
      this.q.GAsVoid(var1);
   }

   protected final void jAsVoid(long var1, wl.Type_v var3, boolean var4) {
      if (this.q.jAsBoolean(var1, var3, var4, this.n)) {
         this.QAsVoid(var1, var3);
      }
   }

   protected boolean sectionCandidate(WorldChunk var1, int var2) {
      ChunkSection var3 = var1.getSectionArray()[var2];
      return !var3.isEmpty() && var3.hasAny(this::candidate);
   }

   protected void scanBlock(WorldChunk var1, BlockState var2, int var3, int var4, int var5) {
      this.count(var2, var3, var4, var5);
   }

   protected void clearEvidence() {
   }

   protected wl.Type_v evidence() {
      return new wl.Type_v(this.v, this.y, this.O, this.h);
   }

   public List<Setting<?>> detectionSettings() {
      return List.of();
   }

   protected final int scanRadius() {
      return this.scanRadius.getValue().intValue();
   }

   public List<Setting<?>> AAsList() {
      return List.of(this.scanRadius, this.sectionsPerTick, this.rescanSeconds);
   }

   public List<Setting<?>> markerSettings() {
      return List.of(this.markerY, this.maxMarkers, this.markerStyle, this.labels, this.tracers);
   }

   public List<Setting<?>> tAsList() {
      return List.of(this.notifications, this.chatAlerts, this.alertSound);
   }

   protected int maximumY() {
      return Integer.MAX_VALUE;
   }

   protected abstract void count(BlockState var1, int var2);

   protected void count(BlockState var1, int var2, int var3, int var4) {
      this.count(var1, var3);
   }

   protected void drawEvidence(ChunkPos var1, wl.Type_v var2, ThemeSettings var3, int var4) {
   }

   protected final void OAsVoid(int var1) {
      this.v++;
      if (this.h == Integer.MIN_VALUE) {
         this.h = var1;
      }
   }

   protected final void gAsVoid() {
      this.y++;
   }

   protected final void AAsVoid(int var1) {
      this.O++;
      if (this.h == Integer.MIN_VALUE) {
         this.h = var1;
      }
   }

   protected final void sAsVoid(String var1) {
      if (this.q.wAsInt() > 0 || !this.z.isEmpty()) {
         ClientLog.XAsVoid("BaseFinding", "{} cleanup: {} ({} flags)", this.getName(), var1, this.q.lAsInt());
      }

      this.p.lAsVoid();
      this.clearEvidence();
      this.q.EAsVoid();
      this.z.clear();
      this.S.clear();
      NotificationHud.GAsW8().HAsVoid(this.getName());
      this.t = List.of();
      this.x = 0;
      this.n = 0L;
   }

   @Override
   public void onEnable() {
      this.sAsVoid("activation");
      this.a = getMc().world;
      ClientLog.GAsVoid("BaseFinding", "{} active: received-data scan, radius {}", this.getName(), this.scanRadius.getValue().intValue());
   }

   @Override
   public void onDisable() {
      this.sAsVoid("disable or world suspension");
      this.a = null;
      ClientLog.XAsVoid("BaseFinding", "{} released world scan state", this.getName());
   }

   private void QAsVoid(long var1, wl.Type_v var3) {
      ChunkPos var4 = new ChunkPos(var1);
      ClientLog.XAsVoid("BaseFinding", "{} found chunk [{}, {}]: {}", this.getName(), var4.x, var4.z, this.describe(var3));
      if (this.notifications.getValue()) {
         if (this.z.size() < 64) {
            this.z.addLast(new sf.Type_t(var1, this.describe(var3)));
         } else {
            this.x++;
         }
      }
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.a != mc.world) {
            this.sAsVoid("world changed");
            this.a = mc.world;
         }

         this.n++;
         this.S.values().removeIf(var1x -> this.n - var1x >= 1200L);
         ChunkPos var1 = mc.player.getChunkPos();
         int var2 = this.scanRadius.getValue().intValue();
         this.q
            .JAsVoid(
               var3x -> {
                  ChunkPos var5 = new ChunkPos(var3x);
                  return Math.abs((long)var5.x - (long)var1.x) <= (long)var2
                     && Math.abs((long)var5.z - (long)var1.z) <= (long)var2
                     && this.a.getChunkManager().getWorldChunk(var5.x, var5.z) != null;
               },
               this.n
            );
         this.beforeScan();
         this.p.sAsVoid(this.j, var1.x, var1.z, var2, this.sectionsPerTick.getValue().intValue());
         this.afterScan();
         this.t = this.q
            .CAsMap()
            .keySet()
            .stream()
            .sorted(Comparator.comparingDouble(this::sAsDouble))
            .limit((long)this.maxMarkers.getValue().intValue())
            .toList();
         long var3;
         if (((var3 = this.n % 20L - 0L) == 0L ? 0 : (var3 < 0L ? -1 : 1)) == 0) {
            this.rAsVoid();
         }
      }
   }

   private double sAsDouble(long var1) {
      ChunkPos var3 = new ChunkPos(var1);
      double var4 = (double)var3.getStartX() + 8.0 - mc.player.getX();
      double var6 = (double)var3.getStartZ() + 8.0 - mc.player.getZ();
      return var4 * var4 + var6 * var6;
   }

   private void rAsVoid() {
      if (this.notifications.getValue()) {
         Map<Long, wl.Type_v> var1 = this.q.CAsMap();

         while (!this.z.isEmpty()) {
            sf.Type_t var2 = this.z.removeFirst();
            if (var1.containsKey(var2.key())) {
               ChunkPos var3 = new ChunkPos(var2.key());
               FindingZoneBounds.Type_c var4 = FindingZoneBounds.HAsList(var1.keySet(), this.zoneRadius())
                  .stream()
                  .filter(var1x -> var1x.members().contains(var2.key()))
                  .findFirst()
                  .orElseThrow();
               this.z.removeIf(var1x -> var4.members().contains(var1x.key()));
               boolean var5 = var4.members().stream().anyMatch(this.S::containsKey);
               if (this.S.size() + var4.members().size() > 1024) {
                  this.S.entrySet().stream().min(Entry.comparingByValue()).ifPresent(var1x -> this.S.remove(var1x.getKey()));
               }

               for (long var7 : var4.members()) {
                  if (this.S.size() < 1024) {
                     this.S.put(var7, this.n);
                  }
               }

               if (!var5) {
                  String var9 = var4.members().size() > 1
                     ? "Zone ("
                        + var4.members().size()
                        + " chunks) at X:"
                        + Math.round(var4.x())
                        + " Z:"
                        + Math.round(var4.z())
                        + " · "
                        + Math.round(Math.hypot(var4.x() - mc.player.getX(), var4.z() - mc.player.getZ()))
                        + "m"
                     : "Chunk [" + var3.x + ", " + var3.z + "] at X:" + var3.getStartX() + " Z:" + var3.getStartZ();
                  NotificationHud.GAsW8().MAsVoid(this.getName(), var9, NotificationHud.Type_u.INFO);
                  if (this.chatAlerts.getValue()) {
                     mc.getMessageHandler().onGameMessage(Text.literal("[Larp/" + this.getName() + "] " + var9 + ": " + var2.evidence()), false);
                  }

                  if (this.alertSound.getValue()) {
                     mc.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                  }

                  return;
               }
            }
         }

         if (this.x > 0) {
            NotificationHud.GAsW8().MAsVoid(this.getName(), this.x + " additional discoveries; see chunk markers", NotificationHud.Type_u.INFO);
            this.x = 0;
         }
      }
   }

   private void jAsVoid() {
      if (this.isRuntimeActive() && this.isInGame() && this.a == mc.world) {
         try {
            Map<Long, wl.Type_v> var1 = this.q.CAsMap();
            double var2 = Math.clamp(this.markerY.getValue(), (double)this.a.getBottomY(), (double)(this.a.getTopYInclusive() - 1));
            double var4 = Math.sqrt(2.0) * (this.scanRadius.getValue() + 1.0) * 16.0;
            Vec3d var6 = mc.gameRenderer.getCamera().getCameraPos();
            int var7 = 0;
            this.beginDraw();

            for (long var9 : this.t) {
               wl.Type_v var11 = var1.get(var9);
               ChunkPos var12 = new ChunkPos(var9);
               if (var11 != null && this.a.getChunkManager().getWorldChunk(var12.x, var12.z) != null) {
                  double var13 = Math.sqrt(this.sAsDouble(var9));
                  int var15 = this.b.iAsInt(this.tint(), var13, var4, 1.0, var12.hashCode());
                  Box var16 = new Box(
                     (double)var12.getStartX(),
                     var2,
                     (double)var12.getStartZ(),
                     (double)(var12.getStartX() + 16),
                     var2 + 0.35,
                     (double)(var12.getStartZ() + 16)
                  );
                  this.drawEvidence(var12, var11, this.b, var15);
                  this.b.AAsVoid(var16, this.markerStyle.getValue(), var15);
                  Vec3d var17 = new Vec3d((double)(var12.getStartX() + 8), var2 + 0.4, (double)(var12.getStartZ() + 8));
                  if (this.tracers.getValue()) {
                     this.b.IAsVoid(var6, var17, var15);
                  }

                  if (this.labels.getValue() && var7++ < 24) {
                     this.b.NAsVoid(var17.add(0.0, 0.8, 0.0), this.describe(var11), var15);
                  }
               }
            }
         } catch (RuntimeException var181) {
            ClientLog.hAsVoid("BaseFinding", this.getName() + " marker rendering failed; scan remains active", var181);
         }
      }
   }

   @Override
   public String getStatus() {
      return this.q.lAsInt() + " chunks";
   }

   private static record Type_t(long key, String evidence) {
   }
}
