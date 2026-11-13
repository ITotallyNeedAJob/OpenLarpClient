package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.NotificationHud;
import com.larpclient.ThemeSettings;
import com.larpclient.we;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map.Entry;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents.Unload;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.DebugRender;
import net.minecraft.block.AbstractPressurePlateBlock;
import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ButtonBlock;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.LeverBlock;
import net.minecraft.block.ObserverBlock;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.LightData;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import net.minecraft.world.chunk.ChunkNibbleArray;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.chunk.light.LightingProvider;

public final class PrimeChunkFinder extends Module {
   private static PrimeChunkFinder t;
   private final NumberSetting scanRadius = this.addSetting(new NumberSetting("Scan Radius", "Loaded chunk radius", 8.0, 1.0, 12.0, 1.0));
   private final BooleanSetting tracers = this.addSetting(new BooleanSetting("Tracers", "Draw lines to detected chunks", true));
   private final BooleanSetting labels = this.addSetting(new BooleanSetting("Labels", "Show the received activity signal", true));
   private final BooleanSetting notifications = this.addSetting(new BooleanSetting("Notifications", "Notify once per discovered chunk", true));
   private final BooleanSetting chatAlerts = this.addSetting(new BooleanSetting("Chat Alerts", "Report discovery coordinates in local chat", true));
   private final ThemeSettings F = new ThemeSettings(var1 -> this.addSetting(var1));
   private final we a = new we();
   private final LinkedHashMap<Long, String> r = new LinkedHashMap<>();
   private final Set<Long> U = new HashSet<>();
   private final LinkedHashMap<Long, Long> f = new LinkedHashMap<>();
   private final ArrayDeque<ChunkPos> D = new ArrayDeque<>();
   private ClientWorld M;
   private long b;
   private long P = -20L;
   private final ArrayDeque<Long> A = new ArrayDeque<>();

   public PrimeChunkFinder() {
      super("Prime Chunk Finder", "Detect underground light and redstone activity", Category.RENDER, -1);
      t = this;
      this.scanRadius.onChanged(var1 -> {
         if (this.M != null) {
            this.xAsVoid(getMc().world);
         }
      });
      this.chatAlerts.visibleWhen(this.notifications::getValue);
      this.notifications.onChanged(var1 -> this.A.clear());
      ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var1, var2) -> {
         if (var1 == this.M) {
            long var3 = var2.getPos().toLong();
            this.a.SAsVoid(var3);
            this.r.remove(Long.valueOf(var3));
            this.U.remove(var3);
            this.A.remove(var3);
         }
      });
      WorldRenderEvents.BEFORE_DEBUG_RENDER.register((DebugRender)var1 -> this.FAsVoid());
   }

   private void xAsVoid(ClientWorld var1) {
      this.a.aAsVoid();
      this.r.clear();
      this.U.clear();
      this.f.clear();
      this.D.clear();
      this.A.clear();
      this.M = var1;
      this.b = 0L;
      this.P = -20L;
      NotificationHud.GAsW8().HAsVoid(this.getName());
      if (var1 != null && getMc().player != null) {
         ChunkPos var2 = mc.player.getChunkPos();
         int var3 = this.scanRadius.getValue().intValue();
         ArrayList<ChunkPos> var4 = new ArrayList<>();

         for (int var5 = -var3; var5 <= var3; var5++) {
            for (int var6 = -var3; var6 <= var3; var6++) {
               var4.add(new ChunkPos(var2.x + var5, var2.z + var6));
            }
         }

         var4.sort(Comparator.comparingLong(var1x -> (long)(var1x.x - var2.x) * (long)(var1x.x - var2.x) + (long)(var1x.z - var2.z) * (long)(var1x.z - var2.z)));
         this.D.addAll(var4);
      }
   }

   @Override
   public void onEnable() {
      this.xAsVoid(getMc().world);
      ClientLog.GAsVoid(
         "BaseFinding",
         "Prime Chunk Finder active: received light/redstone changes below Y0, loaded radius {}, initial light is baseline",
         this.scanRadius.getValue()
      );
   }

   @Override
   public void onDisable() {
      ClientLog.XAsVoid("BaseFinding", "Prime Chunk Finder cleanup: {} light histories, {} findings", this.a.TAsInt(), this.r.size());
      this.xAsVoid(null);
   }

   private boolean OAsBoolean(int var1, int var2) {
      if (this.M != null && mc.player != null) {
         ChunkPos var3 = mc.player.getChunkPos();
         int var4 = this.scanRadius.getValue().intValue();
         return Math.abs((long)var1 - (long)var3.x) <= (long)var4
            && Math.abs((long)var2 - (long)var3.z) <= (long)var4
            && this.M.getChunkManager().getWorldChunk(var1, var2) != null;
      } else {
         return false;
      }
   }

   public static void mAsVoid(ClientWorld var0, int var1, int var2, LightData var3, boolean var4) {
      if (t != null && t.isEnabled()) {
         PrimeChunkFinder var5 = t;
         if (var5 != null && var5.isRuntimeActive() && var0 != null && var0 == getMc().world && mc.isOnThread() && mc.player != null) {
            if (var5.M != var0) {
               var5.xAsVoid(var0);
            }

            if (var5.OAsBoolean(var1, var2)) {
               try {
                  var5.a
                     .MAsVoid(
                        var1,
                        var2,
                        var0.getChunkManager().getLightingProvider().getBottomY(),
                        var3.getInitedBlock(),
                        var3.getUninitedBlock(),
                        var3.getBlockNibbles(),
                        var4
                     );
               } catch (RuntimeException var7) {
                  ClientLog.hAsVoid("BaseFinding", "Prime light capture failed; later received packets can recover", var7);
               }
            }
         }
      }
   }

   public static boolean aAsBoolean(BlockState var0) {
      return var0.getBlock() instanceof RedstoneWireBlock
         || var0.getBlock() instanceof AbstractRedstoneGateBlock
         || var0.getBlock() instanceof PistonBlock
         || var0.getBlock() instanceof ObserverBlock
         || var0.getBlock() instanceof LeverBlock
         || var0.getBlock() instanceof ButtonBlock
         || var0.getBlock() instanceof AbstractPressurePlateBlock
         || var0.getBlock() instanceof HopperBlock
         || var0.getBlock() instanceof DispenserBlock
         || var0.getBlock() instanceof RedstoneTorchBlock
         || var0.isOf(Blocks.REDSTONE_LAMP)
         || var0.isOf(Blocks.TRIPWIRE)
         || var0.isOf(Blocks.TRIPWIRE_HOOK);
   }

   public static void eAsVoid(ClientWorld var0, BlockPos var1, BlockState var2) {
      if (t != null && t.isEnabled()) {
         PrimeChunkFinder var3 = t;
         if (var3 != null
            && var3.isRuntimeActive()
            && var3.M == var0
            && var0 == getMc().world
            && mc.isOnThread()
            && var1.getY() < 0
            && var3.OAsBoolean(var1.getX() >> 4, var1.getZ() >> 4)) {
            BlockState var4 = var0.getBlockState(var1);
            if (!var4.equals(var2)) {
               if (var4.getLuminance() > var2.getLuminance()) {
                  var3.f.put(Long.valueOf(var1.asLong()), Long.valueOf(var3.b + 100L));

                  while (var3.f.size() > 256) {
                     var3.f.remove(var3.f.keySet().iterator().next());
                  }
               }

               if (aAsBoolean(var4) || aAsBoolean(var2)) {
                  var3.MAsVoid(var1, "Redstone activity");
               }
            }
         }
      }
   }

   private boolean vAsBoolean(long var1) {
      return this.U.contains(var1) || mc.player != null && mc.player.getChunkPos().toLong() == var1;
   }

   private void MAsVoid(BlockPos var1, String var2) {
      long var3 = ChunkPos.toLong(var1.getX() >> 4, var1.getZ() >> 4);
      if (!this.vAsBoolean(var3) && !this.r.containsKey(Long.valueOf(var3))) {
         this.r.put(Long.valueOf(var3), var2);

         while (this.r.size() > 256) {
            long var5 = this.r.keySet().iterator().next();
            this.r.remove(Long.valueOf(var5));
            this.A.remove(var5);
         }

         ClientLog.XAsVoid("BaseFinding", "Prime discovery: chunk [{}, {}], {}", var1.getX() >> 4, var1.getZ() >> 4, var2);
         if (this.notifications.getValue()) {
            this.A.add(var3);
         }
      }
   }

   private BlockState jAsBlockState(BlockPos var1) {
      WorldChunk var2 = this.M.getChunkManager().getWorldChunk(var1.getX() >> 4, var1.getZ() >> 4);
      return var2 == null ? Blocks.AIR.getDefaultState() : var2.getBlockState(var1);
   }

   private boolean UAsBoolean(we.Type_i var1) {
      int var2 = Math.max(var1.before(), var1.after());
      BlockPos var3 = var1.pos();
      int var4 = Math.max(5, 15 - var2);

      for (int var5 = -var4; var5 <= var4; var5++) {
         for (int var6 = -var4 + Math.abs(var5); var6 <= var4 - Math.abs(var5); var6++) {
            int var7 = var4 - Math.abs(var5) - Math.abs(var6);

            for (int var8 = -var7; var8 <= var7; var8++) {
               int var9 = Math.abs(var5) + Math.abs(var6) + Math.abs(var8);
               BlockState var10 = this.jAsBlockState(var3.add(var5, var6, var8));
               if (var10.getLuminance() >= var2 + var9) {
                  return true;
               }

               if (var2 <= 5
                  && var9 <= 5
                  && (
                     var10.isOf(Blocks.AMETHYST_BLOCK)
                        || var10.isOf(Blocks.BUDDING_AMETHYST)
                        || var10.isOf(Blocks.AMETHYST_CLUSTER)
                        || var10.isOf(Blocks.SMALL_AMETHYST_BUD)
                        || var10.isOf(Blocks.MEDIUM_AMETHYST_BUD)
                        || var10.isOf(Blocks.LARGE_AMETHYST_BUD)
                  )) {
                  return true;
               }

               if (var2 <= 9 && var9 <= 9 - var2 && (var10.isOf(Blocks.REDSTONE_ORE) || var10.isOf(Blocks.DEEPSLATE_REDSTONE_ORE))) {
                  return true;
               }
            }
         }
      }

      for (Entry var12 : this.f.entrySet()) {
         if (var3.getManhattanDistance(BlockPos.fromLong((Long)var12.getKey())) <= 15 - var2) {
            return true;
         }
      }

      return false;
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.M != mc.world) {
            this.xAsVoid(mc.world);
         }

         this.b++;
         ChunkPos var1 = mc.player.getChunkPos();
         double var17;
         if (((var17 = mc.player.getY() - 0.0) == 0.0 ? 0 : (var17 < 0.0 ? -1 : 1)) < 0) {
            this.U.add(var1.toLong());
            this.r.remove(Long.valueOf(var1.toLong()));
            this.A.remove(var1.toLong());
         }

         if (this.b % 20L == 0L) {
            this.a.PAsVoid(var1x -> {
               ChunkPos var3x = new ChunkPos(var1x);
               return this.OAsBoolean(var3x.x, var3x.z);
            });
            this.r.keySet().removeIf(var1x -> {
               ChunkPos var2x = new ChunkPos(var1x);
               return !this.OAsBoolean(var2x.x, var2x.z);
            });
            this.U.removeIf(var1x -> {
               ChunkPos var2x = new ChunkPos(var1x);
               return !this.OAsBoolean(var2x.x, var2x.z);
            });
            this.f.values().removeIf(var1x -> var1x < this.b);
         }

         for (int var2 = 0; var2 < 8 && !this.D.isEmpty(); var2++) {
            ChunkPos var3 = this.D.remove();
            if (this.OAsBoolean(var3.x, var3.z) && !this.a.hAsBoolean(var3.toLong())) {
               LightingProvider var4 = this.M.getChunkManager().getLightingProvider();
               BitSet var5 = new BitSet();
               ArrayList var6 = new ArrayList();

               for (int var7 = Math.max(-4, this.M.getBottomSectionCoord()); var7 < 0; var7++) {
                  ChunkNibbleArray var8 = var4.get(LightType.BLOCK).getLightSection(ChunkSectionPos.from(var3.x, var7, var3.z));
                  if (var8 != null) {
                     var5.set(var7 - var4.getBottomY());
                     var6.add(var8.asByteArray());
                  }
               }

               this.a.MAsVoid(var3.x, var3.z, var4.getBottomY(), var5, new BitSet(), var6, true);
            }
         }

         for (we.Type_i var11 : this.a.cAsList(8)) {
            BlockPos var12 = var11.pos();
            long var14 = ChunkPos.toLong(var12.getX() >> 4, var12.getZ() >> 4);
            if (!this.vAsBoolean(var14)
               && !this.r.containsKey(Long.valueOf(var14))
               && this.OAsBoolean(var12.getX() >> 4, var12.getZ() >> 4)
               && !this.UAsBoolean(var11)) {
               this.MAsVoid(var12, "Underground light changed");
            }
         }

         long var18;
         if (((var18 = this.b - this.P - 20L) == 0L ? 0 : (var18 < 0L ? -1 : 1)) >= 0 && !this.A.isEmpty()) {
            long var10 = this.A.remove();
            String var13 = this.r.get(var10);
            if (var13 != null && this.notifications.getValue()) {
               ChunkPos var15 = new ChunkPos(var10);
               String var16 = var13 + ": " + (var15.getStartX() + 8) + ", " + (var15.getStartZ() + 8);
               NotificationHud.GAsW8().MAsVoid(this.getName(), var16, NotificationHud.Type_u.INFO);
               if (this.chatAlerts.getValue()) {
                  mc.getMessageHandler().onGameMessage(Text.literal("(Prime Chunk Finder) " + var16), false);
               }

               this.P = this.b;
            }
         }
      }
   }

   private void FAsVoid() {
      if (this.isRuntimeActive() && this.isInGame() && this.M == mc.world) {
         int var1 = 0;

         for (Entry var3 : this.r.entrySet()) {
            ChunkPos var4 = new ChunkPos((Long)var3.getKey());
            if (this.OAsBoolean(var4.x, var4.z)) {
               Vec3d var5 = new Vec3d((double)(var4.getStartX() + 8), 63.0, (double)(var4.getStartZ() + 8));
               int var6 = this.F.iAsInt(-10101600, var5.distanceTo(mc.player.getEntityPos()), (this.scanRadius.getValue() + 1.0) * 16.0, 1.0, var4.hashCode());
               this.F
                  .AAsVoid(
                     new Box((double)var4.getStartX(), 62.9, (double)var4.getStartZ(), (double)(var4.getStartX() + 16), 63.1, (double)(var4.getStartZ() + 16)),
                     "Filled",
                     var6
                  );
               if (this.tracers.getValue()) {
                  this.F.IAsVoid(mc.gameRenderer.getCamera().getCameraPos(), var5, var6);
               }

               if (this.labels.getValue() && var1++ < 24) {
                  this.F.NAsVoid(var5.add(0.0, 1.0, 0.0), (String)var3.getValue(), var6);
               }
            }
         }
      }
   }

   @Override
   public String getStatus() {
      return this.r.size() + " activity chunks";
   }
}
