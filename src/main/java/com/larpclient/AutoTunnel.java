package com.larpclient;

import com.larpclient.module.BooleanSetting;
import com.larpclient.module.ModeSetting;
import com.larpclient.module.NumberSetting;
import com.larpclient.module.Setting;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SplittableRandom;
import java.util.TreeMap;
import java.util.stream.Collectors;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.WorldChunk;

public final class AutoTunnel extends NodeRenderer {
   private static AutoTunnel L;
   private final BooleanSetting j = this.QAsBooleanSetting("Legit", "Select the best hotbar tool while smoothly aiming at each block", false);
   private final ModeSetting width = this.addSetting(
      new ModeSetting("Width", "Tunnel cross section; 1x1 is a crawl tunnel that needs a crawling start", "1x2", "1x1", "1x2", "3x3")
   );
   private final NumberSetting s = this.HAsNumberSetting("Reach", "How far ahead to mine", 4.0, 2.0, 6.0, 1.0);
   private final BooleanSetting J = this.QAsBooleanSetting("Base finder", "Walk a fixed heading, avoid hazards and stop at nearby storage clusters", false);
   private final NumberSetting W = this.HAsNumberSetting("Hazard scan", "Distance ahead to inspect lava, water and drops", 6.0, 3.0, 12.0, 1.0);
   private final BooleanSetting V = this.QAsBooleanSetting("Avoid lava", "Avoid lava beside, above and below the tunnel", true);
   private final BooleanSetting o = this.QAsBooleanSetting("Avoid water", "Avoid water that would flood the tunnel", true);
   private final BooleanSetting X = this.QAsBooleanSetting("Avoid drops", "Avoid drops of two or more blocks", true);
   private final BooleanSetting G = this.QAsBooleanSetting("Avoid gravel", "Avoid falling sand and gravel above the tunnel", false);
   private final NumberSetting i = this.HAsNumberSetting("Detour", "Maximum sideways distance for a safe return to the main heading", 4.0, 2.0, 12.0, 1.0);
   private final BooleanSetting S = this.KAsBooleanSetting("Chests", "Include chests and trapped chests", true);
   private final BooleanSetting O = this.KAsBooleanSetting("Barrels", "Include barrels", true);
   private final BooleanSetting d = this.KAsBooleanSetting("Ender Chests", "Include ender chests", true);
   private final BooleanSetting z = this.KAsBooleanSetting("Hoppers", "Include hoppers", true);
   private final BooleanSetting q = this.KAsBooleanSetting("Spawners", "Include mob spawners", true);
   private final BooleanSetting T = this.KAsBooleanSetting("Shulker Boxes", "Include uncolored and all colored shulker boxes", true);
   private final NumberSetting I = this.HAsNumberSetting(
      "Blocks to flag", "Selected storage blocks within 12 blocks of each other needed for a find", 5.0, 1.0, 64.0, 1.0
   );
   private final BooleanSetting a = this.QAsBooleanSetting("Skip dungeons", "Ignore spawners on cobblestone floors or near cobwebs", true);
   private final NumberSetting x = this.HAsNumberSetting("Ignore near start", "Ignore finds this close to the starting point", 48.0, 0.0, 256.0, 8.0);
   private final ModeSetting onFind = this.addSetting(
      new ModeSetting("On find", "Report and stop, or disconnect after reporting", "Notify and stop", "Notify and stop", "Leave") {
         @Override
         public String getModeLabel(String var1) {
            return var1.equals("Notify and stop") ? "Notify & stop" : var1;
         }

         @Override
         public String getValueAsString() {
            return this.getModeLabel(this.getValue());
         }
      }
   );
   private final BooleanSetting f = this.QAsBooleanSetting("Log finds", "Append time, server, dimension and coordinates to config/larp-client/bases.txt", true);
   private final NumberSetting b = this.HAsNumberSetting(
      "Save tool at", "Stop before the selected tool has fewer remaining uses; zero allows breaking", 30.0, 0.0, 200.0, 5.0
   );
   private final NumberSetting u = this.HAsNumberSetting("Stop at health", "Stop when health reaches this value; zero disables the limit", 8.0, 0.0, 20.0, 1.0);
   private final ChunkScanBudget<WorldChunk> g = new ChunkScanBudget<>();
   private final Map<Long, AutoTunnel.Type_x> r = new HashMap<>();
   private final List<BlockPos> w = new ArrayList<>();
   private final ArrayDeque<BlockPos> K = new ArrayDeque<>();
   private ClientWorld v;
   private BlockPos E;
   private Vec3d A;
   private Direction t;
   private Direction k;
   private BlockPos l;
   private BlockPos Y;
   private final AuraModule H = new AuraModule();
   private final SplittableRandom U = new SplittableRandom();
   private Vec3d Z = Vec3d.ZERO;
   private boolean m;
   private boolean h;
   private int F;
   private BlockPos N;
   private Vec3d c = Vec3d.ZERO;
   private double Q = 0.6;
   private final ChunkScanBudget.Type_v<WorldChunk> R = new ChunkScanBudget.Type_v<WorldChunk>() {
      public WorldChunk loaded(int var1, int var2) {
         return AutoTunnel.this.v.getChunkManager().getWorldChunk(var1, var2);
      }

      @Override
      public boolean due(int var1, int var2) {
         AutoTunnel.Type_x var3 = AutoTunnel.this.r.get(ChunkPos.toLong(var1, var2));
         return var3 == null || (long)AutoTunnel.mc.player.age - var3.tick >= 80L;
      }

      public int sections(WorldChunk var1) {
         return var1.getSectionArray().length;
      }

      public boolean candidate(WorldChunk var1, int var2) {
         ChunkSection var3 = var1.getSectionArray()[var2];
         return !var3.isEmpty() && var3.hasAny(AutoTunnel.this::jAsBoolean);
      }

      @Override
      public void begin() {
         AutoTunnel.this.w.clear();
      }

      public void scan(WorldChunk var1, int var2) {
         ChunkSection var3 = var1.getSectionArray()[var2];

         for (int var4 = 0; var4 < 16; var4++) {
            for (int var5 = 0; var5 < 16; var5++) {
               for (int var6 = 0; var6 < 16; var6++) {
                  BlockState var7 = var3.getBlockState(var6, var4, var5);
                  if (AutoTunnel.this.jAsBoolean(var7) && AutoTunnel.this.w.size() < 256) {
                     BlockPos var8 = new BlockPos(var1.getPos().getStartX() + var6, var1.getBottomY() + var2 * 16 + var4, var1.getPos().getStartZ() + var5);
                     if (!(AutoTunnel.this.E.getSquaredDistance(var8) < AutoTunnel.this.x.getValue() * AutoTunnel.this.x.getValue())
                        && (!AutoTunnel.this.a.getValue() || !var7.isOf(Blocks.SPAWNER) || !AutoTunnel.this.aAsBoolean(var8))) {
                        AutoTunnel.this.w.add(var8);
                     }
                  }
               }
            }
         }
      }

      @Override
      public void finish(int var1, int var2) {
         AutoTunnel.this.r.put(ChunkPos.toLong(var1, var2), new AutoTunnel.Type_x(List.copyOf(AutoTunnel.this.w), (long)AutoTunnel.mc.player.age));
      }
   };

   private BooleanSetting KAsBooleanSetting(String var1, String var2, boolean var3) {
      return this.addSetting(new BooleanSetting("Storage " + var1, var2, var3) {
         @Override
         public String getDisplayName() {
            return "Storage: " + var1;
         }
      });
   }

   public AutoTunnel() {
      super("Auto Tunnel", "Mine and walk a centered 1x1, 1x2 or 3x3 tunnel, including while menus are open");
      L = this;

      for (Setting var2 : List.of(this.W, this.i, this.S, this.O, this.d, this.z, this.q, this.T, this.I, this.x, this.onFind, this.f)) {
         var2.visibleWhen(this.J::getValue);
      }

      this.a.visibleWhen(() -> this.J.getValue() && this.q.getValue());
      this.J.onChanged(var1 -> this.dAsVoid());
      this.width.onChanged(var1 -> this.dAsVoid());

      for (BooleanSetting var4 : List.of(this.S, this.O, this.d, this.z, this.q, this.T, this.a)) {
         var4.onChanged(var1 -> {
            this.g.lAsVoid();
            this.r.clear();
            this.w.clear();
         });
      }
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.dAsVoid();
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.dAsVoid();
   }

   @Override
   public boolean allowsScreenInteractions() {
      return true;
   }

   public static boolean IAsBoolean() {
      if (L != null && L.isEnabled()) {
         AutoTunnel var0 = L;
         MinecraftClient var1 = MinecraftClient.getInstance();
         return var0 != null && var0.isRuntimeActive() && ModuleImportBroker.tAsBoolean(var0) && var1.getOverlay() == null && var1.player.networkHandler.isLoaded();
      } else {
         return false;
      }
   }

   @Override
   void aimAt(Vec3d var1, float var2) {
      if (this.isEnabled() && !this.h) {
         double var3 = Math.min(this.s.getValue(), mc.player.getBlockInteractionRange());
         Vec3d var5 = var1;
         if (this.Y != null) {
            if (!this.Y.equals(this.N)) {
               this.N = this.Y;
               this.c = new Vec3d(this.U.nextDouble(-0.22, 0.22), this.U.nextDouble(-0.22, 0.22), this.U.nextDouble(-0.22, 0.22));
            }

            Vec3d var6 = var1.add(this.c);
            if (MiningProgress.NAsBoolean(this.Y, var6, var3)) {
               var5 = var6;
            }
         }

         float[] var9 = ModuleDetector.QAsFloatArray(mc.player.getEyePos(), var5);
         int var7 = this.Y != null && MiningProgress.NAsBlockHitResult(this.Y, var3) == null ? 0 : 1;
         float[] var8 = this.H.eAsFloatArray(mc.player.getYaw(), mc.player.getPitch(), var9[0], var9[1], var7 != 0);
         ModuleDetector.RAsVoid(this, ModuleDetector.Type_s.TUNNEL, var8[0], var8[1], 1.0);
      }
   }

   private void dAsVoid() {
      ModuleImportBroker.KAsVoid(this);
      MiningProgress.cAsVoid(this);
      this.m = this.h = false;
      this.k = this.t = null;
      this.E = null;
      this.A = null;
      this.l = this.Y = this.N = null;
      this.Z = Vec3d.ZERO;
      this.H.eAsVoid();
      this.Q = this.U.nextDouble(0.35, 1.1);
      this.K.clear();
      this.g.lAsVoid();
      this.r.clear();
      this.w.clear();
      this.v = null;
      this.F = 0;
   }

   @Override
   public void onTick() {
      this.m = false;
      this.Z = Vec3d.ZERO;
      if (this.fAsBoolean() && !this.wAsBoolean()) {
         byte var1 = (byte)(this.KAsBoolean() ? 1 : 0);
         if ((var1 != 0) != this.h) {
            this.h = var1 != 0;
            ClientLog.GAsVoid(
               this.getName(),
               var1 != 0
                  ? "Inventory menu open; holding position and facing, mining only the block under the crosshair"
                  : "Inventory menu closed; resuming walking and aiming"
            );
            if (var1 == 0) {
               this.H.eAsVoid();
            }
         }

         double var11;
         if (this.v != mc.world
            || this.A != null && ((var11 = mc.player.getEntityPos().squaredDistanceTo(this.A) - 64.0) == 0.0 ? 0 : (var11 < 0.0 ? -1 : 1)) > 0) {
            this.dAsVoid();
            this.v = mc.world;
            this.E = mc.player.getBlockPos();
            this.t = Direction.fromHorizontalDegrees((double)mc.player.getYaw());
         }

         Direction var2 = this.t;
         double var12;
         if (((var12 = this.u.getValue() - 0.0) == 0.0 ? 0 : (var12 < 0.0 ? -1 : 1)) > 0 && (double)mc.player.getHealth() <= this.u.getValue()) {
            this.ZAsVoid("Low health");
         } else if (this.width.pAsBoolean("1x1") && !mc.player.isCrawling()) {
            MiningProgress.cAsVoid(this);
            this.H.eAsVoid();
            this.Y = null;
            this.SAsVoid("Waiting: 1x1 needs a crawling start, such as under a trapdoor");
         } else {
            if (this.J.getValue()) {
               this.DAsVoid();
               if (!this.isRuntimeActive()) {
                  return;
               }

               var2 = this.wAsDirection();
               if (var2 == null) {
                  return;
               }
            }

            if (this.k != var2 || this.l == null) {
               this.k = var2;
               this.l = mc.player.getBlockPos().offset(var2);
               this.Y = null;
               this.H.eAsVoid();
               ClientLog.GAsVoid(
                  this.getName(), "Following {} through lane {}, {} at Y {} ({})", var2, this.l.getX(), this.l.getZ(), this.l.getY(), this.width.getValue()
               );
            }

            this.l = pAsBlockPos(this.l, mc.player.getEntityPos(), var2, this.zAsBoolean(this.l, var2));
            String var3 = this.hAsString(this.l, var2);
            if (var3 != null) {
               MiningProgress.cAsVoid(this);
               this.H.eAsVoid();
               this.Y = null;
               this.SAsVoid("Waiting: " + var3);
            } else {
               double var4 = Math.min(this.s.getValue(), mc.player.getBlockInteractionRange());
               BlockPos var6 = this.qAsBlockPos(var2, var4);
               if (var6 != null) {
                  if (this.gAsBoolean(var6)) {
                     this.ZAsVoid("Tool nearly broken");
                     return;
                  }

                  Direction var8 = var2;
                  BlockPos var9 = this.l;
                  boolean var10 = MiningProgress.UAsBoolean(
                     this,
                     var6,
                     var4,
                     true,
                     12.0F,
                     this.j.getValue() || this.J.getValue(),
                     () -> !this.wAsBoolean() && this.hAsString(var9, var8) == null && (!this.J.getValue() || !mc.world.getBlockState(var6).hasBlockEntity())
                  );
                  if (!var10) {
                     this.SAsVoid("Waiting for a reachable tunnel face");
                  }
               } else {
                  MiningProgress.cAsVoid(this);
               }

               this.BAsVoid(var2, var6);
            }
         }
      } else {
         MiningProgress.cAsVoid(this);
         this.H.eAsVoid();
         this.Y = null;
         this.SAsVoid(this.wAsBoolean() ? "Paused for manual movement" : "Waiting for world or interaction control");
      }
   }

   private void BAsVoid(Direction var1, BlockPos var2) {
      Vec3d var3 = Vec3d.ofBottomCenter(this.l);
      boolean var4 = this.zAsBoolean(this.l, var1);
      double var5 = com.larpclient.AuraModule.vAsDouble(mc.player.getEntityPos(), var3, var1) - (var4 ? -0.5 : 0.85);
      Vec3d var7 = mc.player.getVelocity();
      double var8 = var7.x * (double)var1.getOffsetX() + var7.z * (double)var1.getOffsetZ();
      Vec3d var10 = com.larpclient.AuraModule.WAsVec3d(mc.player.getEntityPos(), var3, var1);
      boolean var11 = this.H.UAsBoolean();
      int var12 = ModuleImportBroker.nAsBoolean(this) && !this.h && !QueueManager.hAsBoolean() && this.eAsBoolean(OAsVec3d(var10)) ? 1 : 0;
      this.m = this.H.fAsBoolean(var5, var8, var12 != 0);
      this.Z = this.m ? var10 : Vec3d.ZERO;
      if (this.m != var11) {
         ClientLog.XAsVoid(
            this.getName(), this.m ? "Walk keys pressed; {} clear ahead" : "Walk keys released; {} clear ahead", String.format(Locale.ROOT, "%.2f", var5)
         );
      }

      if (var2 == null && ModuleImportBroker.nAsBoolean(this)) {
         Vec3d var13 = var3.add((double)(var1.getOffsetX() * 6), 0.0, (double)(var1.getOffsetZ() * 6));
         if (mc.player.age % 60 == 0 && this.U.nextInt(4) == 0) {
            this.Q = this.U.nextDouble(0.35, 1.1);
         }

         this.aimAt(new Vec3d(var13.x, mc.player.getEyeY() - this.Q, var13.z), 12.0F);
      }

      if (this.A != null && mc.player.getEntityPos().squaredDistanceTo(this.A) < 1.0E-4 && this.m && var2 == null) {
         this.F++;
      } else {
         this.F = 0;
      }

      this.A = mc.player.getEntityPos();
      if (var2 == null) {
         this.SAsVoid(
            this.h
               ? "Holding still while an inventory menu is open"
               : (this.m ? (this.J.getValue() ? "Searching for bases" : "Following the cleared tunnel") : "Waiting at a protected or unreachable obstruction")
         );
      }

      if (this.F > 160) {
         this.ZAsVoid("Movement blocked; clear the route or choose a new heading");
      }
   }

   public static BlockPos pAsBlockPos(BlockPos var0, Vec3d var1, Direction var2, boolean var3) {
      return var3 && com.larpclient.AuraModule.vAsDouble(var1, Vec3d.ofBottomCenter(var0), var2) < 0.12 ? var0.offset(var2) : var0;
   }

   private boolean KAsBoolean() {
      return mc.currentScreen instanceof HandledScreen;
   }

   private boolean wAsBoolean() {
      return mc != null
         && mc.player != null
         && mc.isWindowFocused()
         && mc.currentScreen == null
         && (
            ModuleKeybindChecker.pAsBoolean(mc.options.forwardKey)
               || ModuleKeybindChecker.pAsBoolean(mc.options.backKey)
               || ModuleKeybindChecker.pAsBoolean(mc.options.leftKey)
               || ModuleKeybindChecker.pAsBoolean(mc.options.rightKey)
               || ModuleKeybindChecker.pAsBoolean(mc.options.jumpKey)
               || ModuleKeybindChecker.pAsBoolean(mc.options.sneakKey)
         );
   }

   private List<BlockPos> mAsList(BlockPos var1, Direction var2) {
      return dAsList(var1, var2, this.width.getValue());
   }

   public static int cAsInt(String var0) {
      return switch (var0) {
         case "1x1" -> 1;
         case "3x3" -> 3;
         default -> 2;
      };
   }

   public static List<BlockPos> dAsList(BlockPos var0, Direction var1, String var2) {
      boolean var3 = var2.equals("3x3");
      int var4 = cAsInt(var2);
      ArrayList var5 = new ArrayList(var3 ? 9 : var4);

      for (int var6 = 0; var6 < var4; var6++) {
         var5.add(var0.up(var6));
         if (var3) {
            var5.add(var0.offset(var1.rotateYClockwise()).up(var6));
            var5.add(var0.offset(var1.rotateYCounterclockwise()).up(var6));
         }
      }

      return var5;
   }

   private BlockPos qAsBlockPos(Direction var1, double var2) {
      for (int var4 = 0; (double)var4 < Math.ceil(var2); var4++) {
         BlockPos var5 = this.l.offset(var1, var4);
         if (!this.K.isEmpty() && com.larpclient.AuraModule.vAsDouble(Vec3d.ofBottomCenter(var5), Vec3d.ofBottomCenter((Vec3i)this.K.peekFirst()), var1) < -0.01
            || this.hAsString(var5, var1) != null) {
            break;
         }

         List<BlockPos> var6 = this.mAsList(var5, var1).stream().filter(var3 -> this.TAsBoolean(var3, var2)).toList();
         if (!var6.isEmpty()) {
            if (this.Y != null && var6.contains(this.Y)) {
               return this.Y;
            }

            this.Y = var6.stream().min(Comparator.comparingDouble(this::aAsDouble)).orElseThrow();
            return this.Y;
         }

         if (!this.zAsBoolean(var5, var1)) {
            break;
         }
      }

      this.Y = null;
      return null;
   }

   private boolean TAsBoolean(BlockPos var1, double var2) {
      return MiningProgress.VAsBoolean(var1)
         && mc.player.getEyePos().squaredDistanceTo(Vec3d.ofCenter(var1)) <= var2 * var2
         && (!this.J.getValue() || !mc.world.getBlockState(var1).hasBlockEntity())
         && MiningProgress.BAsBlockHitResult(this, var1, var2) != null;
   }

   private double aAsDouble(BlockPos var1) {
      float[] var2 = ModuleDetector.QAsFloatArray(mc.player.getEyePos(), Vec3d.ofCenter(var1));
      return Math.hypot((double)MathHelper.wrapDegrees(var2[0] - mc.player.getYaw()), (double)(var2[1] - mc.player.getPitch()));
   }

   private boolean zAsBoolean(BlockPos var1, Direction var2) {
      for (BlockPos var4 : this.mAsList(var1, var2)) {
         if (!mc.world.isChunkLoaded(var4) || !mc.world.getBlockState(var4).getCollisionShape(mc.world, var4).isEmpty()) {
            return false;
         }
      }

      return true;
   }

   private static Vec3d OAsVec3d(Vec3d var0) {
      return var0.normalize().multiply(0.55);
   }

   private boolean eAsBoolean(Vec3d var1) {
      Vec3d var2 = var1.multiply(0.65);
      Box var3 = mc.player.getBoundingBox().stretch(var2);
      if (!mc.world.isSpaceEmpty(mc.player, var3)) {
         return false;
      } else {
         BlockPos var4 = BlockPos.ofFloored(mc.player.getEntityPos().add(var2));
         return this.cAsBoolean(var4) && this.hAsString(var4, this.k) == null;
      }
   }

   private boolean gAsBoolean(BlockPos var1) {
      if (this.b.getValue() <= 0.0) {
         return false;
      } else {
         BlockState var2 = mc.world.getBlockState(var1);
         ItemStack var3 = mc.player.getMainHandStack();
         float var4 = var3.getMiningSpeedMultiplier(var2) + (float)(var3.isSuitableFor(var2) ? 1000 : 0);

         for (int var5 = 0; (this.j.getValue() || this.J.getValue()) && var5 < 9; var5++) {
            ItemStack var6 = mc.player.getInventory().getStack(var5);
            float var7 = var6.getMiningSpeedMultiplier(var2) + (float)(var6.isSuitableFor(var2) ? 1000 : 0);
            if (var7 > var4) {
               var3 = var6;
               var4 = var7;
            }
         }

         return var3.isDamageable() && (double)(var3.getMaxDamage() - var3.getDamage()) <= this.b.getValue();
      }
   }

   private boolean cAsBoolean(BlockPos var1) {
      if (!mc.world.isChunkLoaded(var1)) {
         return false;
      } else {
         int var2 = Math.min(cAsInt(this.width.getValue()), 2);

         for (int var3 = 0; var3 < var2; var3++) {
            if (!mc.world.getBlockState(var1.up(var3)).getCollisionShape(mc.world, var1.up(var3)).isEmpty()) {
               return false;
            }
         }

         return true;
      }
   }

   private String hAsString(BlockPos var1, Direction var2) {
      if (!mc.world.isChunkLoaded(var1)) {
         return "Waiting for loaded terrain";
      } else {
         for (BlockPos var4 : this.mAsList(var1, var2)) {
            for (Direction var8 : Direction.values()) {
               BlockPos var9 = var4.offset(var8);
               if (!mc.world.isChunkLoaded(var9)) {
                  return "Waiting for loaded terrain";
               }

               FluidState var10 = mc.world.getFluidState(var9);
               if (this.V.getValue() && var10.isIn(FluidTags.LAVA)) {
                  return "Lava";
               }

               if (this.o.getValue() && var10.isIn(FluidTags.WATER)) {
                  return "Water";
               }
            }

            FluidState var13 = mc.world.getFluidState(var4);
            if (this.V.getValue() && var13.isIn(FluidTags.LAVA)) {
               return "Lava";
            }

            if (this.o.getValue() && var13.isIn(FluidTags.WATER)) {
               return "Water";
            }
         }

         if (this.X.getValue()
            && tAsBoolean(
               mc.world.getBlockState(var1.down()).getCollisionShape(mc.world, var1.down()).isEmpty(),
               mc.world.getBlockState(var1.down(2)).getCollisionShape(mc.world, var1.down(2)).isEmpty()
            )) {
            return "Deep drop";
         } else {
            if (this.G.getValue()) {
               for (BlockPos var12 : this.mAsList(var1, var2)) {
                  if (mc.world.getBlockState(var12.up()).getBlock() instanceof FallingBlock) {
                     return "Falling sand or gravel";
                  }
               }
            }

            return null;
         }
      }
   }

   private Direction wAsDirection() {
      BlockPos var1 = mc.player.getBlockPos();

      while (
         !this.K.isEmpty()
            && mc.player.getEntityPos().multiply(1.0, 0.0, 1.0).squaredDistanceTo(Vec3d.ofBottomCenter((Vec3i)this.K.peekFirst()).multiply(1.0, 0.0, 1.0))
               < 0.08
      ) {
         this.K.removeFirst();
      }

      if (!this.K.isEmpty()) {
         Direction var9 = var1.equals(this.K.peekFirst()) ? this.k : tAsDirection(var1, this.K.peekFirst());
         String var11 = this.hAsString(var1.offset(var9), var9);
         if (var11 != null) {
            this.ZAsVoid("Detour blocked by " + var11.toLowerCase(Locale.ROOT));
            return null;
         } else {
            return var9;
         }
      } else {
         String var2 = null;

         for (int var3 = 1; var3 <= this.W.getValue().intValue(); var3++) {
            var2 = this.hAsString(var1.offset(this.t, var3), this.t);
            if (var2 != null) {
               break;
            }
         }

         if (var2 == null) {
            return this.t;
         } else if (var2.equals("Waiting for loaded terrain")) {
            MiningProgress.cAsVoid(this);
            this.SAsVoid(var2);
            return null;
         } else {
            for (int var10 = 1; var10 <= this.i.getValue().intValue(); var10++) {
               for (Direction var5 : List.of(this.t.rotateYClockwise(), this.t.rotateYCounterclockwise())) {
                  int var6 = this.W.getValue().intValue() + 2;
                  BlockPos var7 = var1.offset(var5, var10);
                  BlockPos var8 = var7.offset(this.t, var6);
                  if (this.TAsBoolean(var1, var5, var10) && this.TAsBoolean(var7, this.t, var6) && this.TAsBoolean(var8, var5.getOpposite(), var10)) {
                     this.K.add(var7);
                     this.K.add(var8);
                     this.K.add(var1.offset(this.t, var6));
                     ClientLog.GAsVoid(this.getName(), "Routing {} blocks around {} using {}", var10, var2, var5);
                     this.SAsVoid("Detouring around " + var2.toLowerCase(Locale.ROOT));
                     return var5;
                  }
               }
            }

            this.ZAsVoid("No safe detour around " + var2.toLowerCase(Locale.ROOT));
            return null;
         }
      }
   }

   private boolean TAsBoolean(BlockPos var1, Direction var2, int var3) {
      for (int var4 = 1; var4 <= var3; var4++) {
         if (this.hAsString(var1.offset(var2, var4), var2) != null) {
            return false;
         }
      }

      return true;
   }

   private static Direction tAsDirection(BlockPos var0, BlockPos var1) {
      int var2 = var1.getX() - var0.getX();
      int var3 = var1.getZ() - var0.getZ();
      return Math.abs(var2) > Math.abs(var3) ? (var2 > 0 ? Direction.EAST : Direction.WEST) : (var3 > 0 ? Direction.SOUTH : Direction.NORTH);
   }

   public static PlayerInput LAsPlayerInput() {
      AutoTunnel var0 = L;
      if (var0 != null && var0.fAsBoolean() && var0.m && !var0.h && var0.k != null && !ParticleEmitter.oAsBoolean() && !QueueManager.hAsBoolean() && !var0.wAsBoolean()) {
         MinecraftClient var1 = MinecraftClient.getInstance();
         if (var0.v == var1.world
            && !(var0.Z.lengthSquared() < 1.0E-6)
            && var0.eAsBoolean(OAsVec3d(var0.Z))
            && (!ModuleDetector.YAsBoolean() || ModuleDetector.CAsBoolean(ModuleDetector.Type_s.TUNNEL))) {
            PlayerInput var2 = var0.H.jAsPlayerInput(var1.player.getYaw(), var0.Z);
            return var2.backward() ? null : var2;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static Vec2f SAsVec2f() {
      PlayerInput var0 = LAsPlayerInput();
      return var0 == null ? null : com.larpclient.AuraModule.sAsVec2f(var0);
   }

   public static boolean tAsBoolean(boolean var0, boolean var1) {
      return var0 && var1;
   }

   public static boolean AAsBoolean(BlockPos var0, BlockPos var1) {
      return Math.abs((long)var1.getX() - (long)var0.getX()) <= 12L
         && Math.abs((long)var1.getY() - (long)var0.getY()) <= 12L
         && Math.abs((long)var1.getZ() - (long)var0.getZ()) <= 12L;
   }

   public static Vec2f vAsVec2f(float var0, Direction var1) {
      double var2 = Math.toRadians((double)(var0 - var1.getPositiveHorizontalDegrees()));
      return new Vec2f((float)Math.sin(var2), (float)Math.cos(var2));
   }

   private boolean jAsBoolean(BlockState var1) {
      Block var2 = var1.getBlock();
      return this.S.getValue() && (var2 == Blocks.CHEST || var2 == Blocks.TRAPPED_CHEST)
         || this.O.getValue() && var2 == Blocks.BARREL
         || this.d.getValue() && var2 == Blocks.ENDER_CHEST
         || this.z.getValue() && var2 == Blocks.HOPPER
         || this.q.getValue() && var2 == Blocks.SPAWNER
         || this.T.getValue() && var2 instanceof ShulkerBoxBlock;
   }

   private boolean aAsBoolean(BlockPos var1) {
      BlockState var2 = mc.world.getBlockState(var1.down());
      if (!var2.isOf(Blocks.COBBLESTONE) && !var2.isOf(Blocks.MOSSY_COBBLESTONE)) {
         for (BlockPos var4 : BlockPos.iterate(var1.add(-2, -1, -2), var1.add(2, 1, 2))) {
            if (mc.world.isChunkLoaded(var4) && mc.world.getBlockState(var4).isOf(Blocks.COBWEB)) {
               return true;
            }
         }

         return false;
      } else {
         return true;
      }
   }

   private void DAsVoid() {
      ChunkPos var1 = mc.player.getChunkPos();
      int var2 = Math.clamp((long)mc.options.getClampedViewDistance(), 1, 12);
      this.r.entrySet().removeIf(var3x -> {
         ChunkPos var4 = new ChunkPos(var3x.getKey());
         return Math.abs(var4.x - var1.x) > var2 || Math.abs(var4.z - var1.z) > var2 || this.v.getChunkManager().getWorldChunk(var4.x, var4.z) == null;
      });
      this.g.sAsVoid(this.R, var1.x, var1.z, var2, 2);
      if (mc.player.age % 10 == 0) {
         ArrayList var3 = new ArrayList();

         for (AutoTunnel.Type_x var5 : this.r.values()) {
            for (BlockPos var7 : var5.positions) {
               if (var3.size() >= 4096) {
                  break;
               }

               if (mc.world.isChunkLoaded(var7)
                  && this.jAsBoolean(mc.world.getBlockState(var7))
                  && this.E.getSquaredDistance(var7) >= this.x.getValue() * this.x.getValue()
                  && (!this.a.getValue() || !mc.world.getBlockState(var7).isOf(Blocks.SPAWNER) || !this.aAsBoolean(var7))) {
                  var3.add(var7);
               }
            }
         }

         HashMap<String, List<BlockPos>> var16 = new HashMap<>();

         for (BlockPos var19 : (Iterable<BlockPos>) (var3)) {
            var16.computeIfAbsent(pAsString(var19.getX(), var19.getY(), var19.getZ()), var0 -> new ArrayList<>()).add(var19);
         }

         for (BlockPos var20 : (Iterable<BlockPos>) (var3)) {
            ArrayList var21 = new ArrayList();
            int var8 = Math.floorDiv(var20.getX(), 12);
            int var9 = Math.floorDiv(var20.getY(), 12);
            int var10 = Math.floorDiv(var20.getZ(), 12);

            for (int var11 = var8 - 1; var11 <= var8 + 1; var11++) {
               for (int var12 = var9 - 1; var12 <= var9 + 1; var12++) {
                  for (int var13 = var10 - 1; var13 <= var10 + 1; var13++) {
                     for (BlockPos var15 : var16.getOrDefault(var11 + ":" + var12 + ":" + var13, List.of())) {
                        if (AAsBoolean(var20, var15)) {
                           var21.add(var15);
                        }
                     }
                  }
               }
            }

            if ((double)var21.size() >= this.I.getValue()) {
               this.FAsVoid(var20, var21);
               return;
            }
         }
      }
   }

   private static String pAsString(int var0, int var1, int var2) {
      return Math.floorDiv(var0, 12) + ":" + Math.floorDiv(var1, 12) + ":" + Math.floorDiv(var2, 12);
   }

   private void FAsVoid(BlockPos var1, List<BlockPos> var2) {
      TreeMap<String, Integer> var3 = new TreeMap<>();

      for (BlockPos var5 : var2) {
         var3.merge(Registries.BLOCK.getId(mc.world.getBlockState(var5).getBlock()).getPath(), 1, Integer::sum);
      }

      String var11 = var1.getX() + ", " + var1.getY() + ", " + var1.getZ();
      String var12 = var11 + " (" + var3.entrySet().stream().map(var0 -> var0.getValue() + " " + var0.getKey()).collect(Collectors.joining(", ")) + ")";
      mc.player.sendMessage(Text.literal("Larp Auto Tunnel: base found at " + var12), false);
      NotificationHud.GAsW8().RAsVoid("Base found", var12, NotificationHud.Type_u.SUCCESS, 10000L);
      mc.getSoundManager().play(PositionedSoundInstance.ui(SoundEvents.ENTITY_PLAYER_LEVELUP, 1.4F));
      ClientLog.GAsVoid(this.getName(), "Base found at {}", var12);
      if (this.f.getValue()) {
         Path var6 = FabricLoader.getInstance().getConfigDir().resolve("larp-client/bases.txt");
         ServerInfo var7 = mc.getCurrentServerEntry();
         String var8 = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            + "  "
            + (var7 == null ? "singleplayer" : var7.address)
            + "  "
            + mc.world.getRegistryKey().getValue()
            + "  "
            + var12
            + System.lineSeparator();

         try {
            Files.createDirectories(var6.getParent());
            Files.writeString(var6, var8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
         } catch (IOException var10) {
            ClientLog.lAsVoid(this.getName(), "Could not append base find to " + var6, var10);
         }
      }

      boolean var13 = this.onFind.pAsBoolean("Leave");
      this.disable();
      if (var13) {
         mc.disconnect(Text.literal("Larp Auto Tunnel found a base at " + var12));
      }
   }

   private void ZAsVoid(String var1) {
      ClientLog.GAsVoid(this.getName(), "Stopped: {}", var1);
      mc.player.sendMessage(Text.literal("Larp Auto Tunnel stopped: " + var1), false);
      NotificationHud.GAsW8().MAsVoid("Auto Tunnel stopped", var1, NotificationHud.Type_u.WARNING);
      this.disable();
      this.SAsVoid(var1);
   }

   private static record Type_x(List<BlockPos> positions, long tick) {
   }
}
