package com.larpclient.module;

import com.larpclient.BlockPlacementController;
import com.larpclient.ClientLog;
import com.larpclient.PlacementPreview;
import com.larpclient.ModuleDetector;
import com.larpclient.RotationAccumulator;
import com.larpclient.ParticleEmitter;
import com.larpclient.PixelUtil;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.Direction.Type;

public final class AutoTrap extends BlockPlacementController.Type_k implements PlacementPreview.Type_e {
   private final PlacementPreview a = new PlacementPreview(this, var1 -> this.addSetting(var1), 0.75, 0.35);
   private static AutoTrap v;
   private final ModeSetting mode = this.addSetting(
      new ModeSetting("Mode", "Legit aims smoothly; Blatant aims directly every tick. Both keep your view free", "Legit", "Legit", "Blatant")
   );
   private final ModeSetting blocks = this.addSetting(
      new ModeSetting("Blocks", "Hotbar trap building material", "Obsidian", "Obsidian", "Crying Obsidian", "Ender Chest", "Stone", "Oak Planks", "Held Block")
   );
   private final BooleanSetting i = this.UAsBooleanSetting("Only Standing", "Pause self trapping while standing on the ground", true);
   private final ModeSetting trapMode = this.addSetting(
      new ModeSetting("Trap Mode", "Lower perimeter, upper perimeter or full enclosure", "Full", "Full", "Legs", "Head") {
         @Override
         public String getDisplayName() {
            return "Trap Pattern";
         }
      }
   );
   private final BooleanSetting l = this.UAsBooleanSetting("Trap Predict", "Predict opponent displacement before choosing the trap footprint", false);
   private final NumberSetting n = this.KAsNumberSetting("Predict Ticks", "Ticks of displacement prediction", 2.0, 0.0, 10.0, 0.5);
   private final BooleanSetting Y = this.UAsBooleanSetting("Predict Sort", "Build blocks closest to the predicted opponent first", true);
   private final NumberSetting L = this.KAsNumberSetting("Placement Preview", "Ticks to show the next block before placing", 6.0, 2.0, 40.0, 1.0);
   private final NumberSetting A = this.KAsNumberSetting("Turn Speed", "Maximum player aim turn speed in degrees per second", 180.0, 30.0, 360.0, 15.0);
   private final PixelUtil g = new PixelUtil();
   private final RotationAccumulator U = new RotationAccumulator();
   private PlayerEntity y;
   private Object x;
   private Object S;
   private BlockPos C;
   private int X;
   private Vec3d r;

   @Override
   public PlacementPreview placementHighlight() {
      return this.a;
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.QAsVoid();
      this.a.SAsVoid();
      ClientLog.GAsVoid(
         this.getName(),
         "Placement mode={}; Legit preview={} ticks, delay={} ticks, turn cap={} degrees/s; Blatant uses direct turns and one native action per tick; mouse look stays free and movement stays manual",
         this.mode.getValue(),
         this.L.getValue(),
         this.d.getValue(),
         this.A.getValue()
      );
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.QAsVoid();
      this.a.rAsVoid();
   }

   private void QAsVoid() {
      ParticleEmitter.GAsVoid(ParticleEmitter.Type_u.AUTOTRAP);
      this.g.oAsVoid();
      this.U.aAsVoid();
      this.y = null;
      this.C = null;
      this.x = this.S = null;
      this.r = null;
   }

   @Override
   protected void aim(float var1, float var2) {
      if (this.isEnabled() && ParticleEmitter.fAsBoolean(ParticleEmitter.Type_u.AUTOTRAP)) {
         if (this.qAsBoolean()) {
            super.aim(var1, var2);
         } else {
            float[] var3 = this.U.DAsFloatArray(mc.player.age, mc.player.getYaw(), mc.player.getPitch(), var1, var2, this.A.getValue());
            ModuleDetector.yAsVoid(ModuleDetector.Type_s.PLACEMENT, var3[0], var3[1], 1.0);
         }

         if (ModuleDetector.CAsBoolean(ModuleDetector.Type_s.PLACEMENT)) {
            ParticleEmitter.jAsVoid(ParticleEmitter.Type_u.AUTOTRAP);
         } else {
            ParticleEmitter.GAsVoid(ParticleEmitter.Type_u.AUTOTRAP);
         }
      }
   }

   public AutoTrap() {
      super("AutoTrap", "Trap with smooth Legit or fast Blatant aim while looking freely", 6);
      this.mode.onChanged(var1 -> {
         BlockPlacementController.WAsVoid(this);
         this.QAsVoid();
         this.a.rAsVoid();
      });
      this.rotation.visibleWhen(() -> !this.qAsBoolean());
      this.rotation.onChanged(var1 -> {
         BlockPlacementController.WAsVoid(this);
         this.QAsVoid();
      });
      this.d.visibleWhen(() -> !this.qAsBoolean());
      this.L.visibleWhen(() -> !this.qAsBoolean());
      this.A.visibleWhen(() -> !this.qAsBoolean() && this.rotation.pAsBoolean("Visible"));
      v = this;
      this.n.visibleWhen(this.l::getValue);
      this.Y.visibleWhen(this.l::getValue);
      this.i.visibleWhen(() -> false);
   }

   public boolean qAsBoolean() {
      return this.mode.pAsBoolean("Blatant");
   }

   private boolean RAsBoolean() {
      return this.qAsBoolean() || this.rotation.pAsBoolean("Visible");
   }

   public boolean VAsBoolean() {
      return this.isEnabled()
         ? this.isRuntimeActive()
            && this.RAsBoolean()
            && mc.player != null
            && this.x == mc.world
            && this.S == mc.player
            && this.oAsBoolean(this.y)
            && (this.g.PAsBlockPos() != null || this.C != null)
            && (!this.Q.getValue() || !mc.player.isUsingItem())
            && (!this.j.getValue() || !mc.options.attackKey.isPressed())
         : false;
   }

   public float KAsFloat(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && ParticleEmitter.NAsP5_Type_u() == ParticleEmitter.Type_u.AUTOTRAP && ModuleDetector.CAsBoolean(ModuleDetector.Type_s.PLACEMENT)) {
            mc.player.setHeadYaw(mc.player.getYaw());
            return mc.player.getYaw();
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public boolean SAsBoolean(int var1, boolean var2, boolean var3) {
      return this.isEnabled() ? this.qAsBoolean() || var2 && this.g.LAsBoolean(var1, this.L.getValue().intValue(), var3) : false;
   }

   public List<Setting<?>> PAsList() {
      return List.of(this.mode, this.blocks, this.h, this.trapMode);
   }

   public List<Setting<?>> UAsList() {
      return List.of(this.p, this.rotation, this.J, this.Q, this.j, this.d, this.L, this.A, this.l, this.n, this.Y, this.i);
   }

   private boolean SAsBoolean(BlockPos var1, Item var2) {
      return BlockPlacementController.uAsBlockHitResult(var1, var2, this.h.getValue(), this.RAsBoolean()) != null;
   }

   public static AutoTrap oAsAutoTrap() {
      return v;
   }

   public Item HAsItem() {
      String var1 = this.blocks.getValue();

      return switch (var1) {
         case "Crying Obsidian" -> Items.CRYING_OBSIDIAN;
         case "Ender Chest" -> Items.ENDER_CHEST;
         case "Stone" -> Items.STONE;
         case "Oak Planks" -> Items.OAK_PLANKS;
         case "Held Block" -> mc.player != null && mc.player.getMainHandStack().getItem() instanceof BlockItem
         ? mc.player.getMainHandStack().getItem()
         : Items.AIR;
         default -> Items.OBSIDIAN;
      };
   }

   public List<BlockPos> eAsList(PlayerEntity var1) {
      if (var1 == mc.player && this.i.getValue() && var1.isOnGround()) {
         return List.of();
      } else {
         Vec3d var2 = cAsVec3d(var1, this.l.getValue() ? this.n.getValue() : 0.0);
         ArrayList var3 = new ArrayList<>(nAsList(rAsList(var1, var2), this.trapMode.getValue()));
         var3.sort(
            Comparator.<BlockPos>comparingInt(Vec3i::getY)
               .thenComparing(Comparator.<BlockPos>comparingDouble(var0 -> Vec3d.ofCenter(var0).squaredDistanceTo(mc.player.getEyePos())).reversed())
         );
         if (this.l.getValue() && this.Y.getValue()) {
            var3.sort(Comparator.comparingInt(Vec3i::getY).thenComparingDouble(var1x -> Vec3d.ofCenter(var1x).squaredDistanceTo(var2)));
         }

         return var3;
      }
   }

   public static List<BlockPos> nAsList(List<BlockPos> var0, String var1) {
      LinkedHashSet var2 = new LinkedHashSet();
      if (!var1.equals("Head")) {
         for (BlockPos var4 : var0) {
            var2.add(var4.down());
         }
      }

      for (BlockPos var10 : var0) {
         for (Direction var6 : Type.HORIZONTAL) {
            BlockPos var7 = var10.offset(var6);
            if (!var0.contains(var7)) {
               var2.add(var7);
               if (!var1.equals("Legs")) {
                  var2.add(var7.up());
               }
            }
         }
      }

      if (!var1.equals("Legs")) {
         for (BlockPos var11 : var0) {
            var2.add(var11.up(2));
         }
      }

      return List.copyOf(var2);
   }

   public static List<BlockPos> MAsList(List<BlockPos> var0) {
      if (var0.isEmpty()) {
         return List.of();
      } else {
         int var1 = var0.stream().mapToInt(Vec3i::getY).max().orElse(0);
         LinkedHashSet var2 = new LinkedHashSet();

         for (BlockPos var4 : var0) {
            if (var4.getY() == var1) {
               for (Direction var6 : Type.HORIZONTAL) {
                  BlockPos var7 = var4.offset(var6);
                  if (!var0.contains(var7) && var0.contains(var7.down())) {
                     var2.add(var7);
                  }
               }
            }
         }

         return List.copyOf(var2);
      }
   }

   public static List<BlockPos> sAsList(List<BlockPos> var0, Vec3d var1) {
      return MAsList(var0).stream().sorted(Comparator.<BlockPos>comparingDouble(var1xx -> Vec3d.ofCenter(var1xx).squaredDistanceTo(var1)).reversed()).toList();
   }

   static Vec3d cAsVec3d(PlayerEntity var0, double var1) {
      Vec3d var3 = var0.getVelocity();
      if (var3.horizontalLengthSquared() < 1.0E-8) {
         var3 = new Vec3d(var0.getX() - var0.lastX, var0.getY() - var0.lastY, var0.getZ() - var0.lastZ);
      }

      return var0.getEntityPos().add(var3.multiply(var1));
   }

   static List<BlockPos> rAsList(PlayerEntity var0, Vec3d var1) {
      Box var2 = var0.getBoundingBox().offset(var1.subtract(var0.getEntityPos()));
      int var3 = (int)Math.floor(var1.y + 0.01);
      ArrayList var4 = new ArrayList(4);

      for (int var5 = (int)Math.floor(var2.minX + 0.001); var5 <= (int)Math.floor(var2.maxX - 0.001); var5++) {
         for (int var6 = (int)Math.floor(var2.minZ + 0.001); var6 <= (int)Math.floor(var2.maxZ - 0.001); var6++) {
            var4.add(new BlockPos(var5, var3, var6));
         }
      }

      return var4;
   }

   private boolean vAsBoolean(List<BlockPos> var1) {
      int var2 = var1.stream().mapToInt(Vec3i::getY).max().orElse(0);
      return !this.trapMode.pAsBoolean("Legs") && var1.stream().anyMatch(var1x -> var1x.getY() == var2 && BlockPlacementController.jAsBoolean(var1x));
   }

   private boolean WAsBoolean(PlayerEntity var1, BlockPos var2) {
      List var3 = this.eAsList(var1);
      return var3.contains(var2) || this.vAsBoolean(var3) && MAsList(var3).contains(var2);
   }

   @Override
   public void onTick() {
      this.a.mAsVoid(null);
      BlockPlacementController.WAsVoid(this);
      if (!this.cAsBoolean()) {
         this.g.oAsVoid();
         this.U.aAsVoid();
      } else if (this.RAsBoolean() && ParticleEmitter.NAsP5_Type_u() != ParticleEmitter.Type_u.NONE && ParticleEmitter.NAsP5_Type_u() != ParticleEmitter.Type_u.AUTOTRAP) {
         this.g.oAsVoid();
         this.U.aAsVoid();
         this.KAsVoid("Waiting for camera control");
      } else {
         if (this.x != mc.world || this.S != mc.player || this.r != null && this.r.squaredDistanceTo(mc.player.getEntityPos()) > 64.0) {
            this.QAsVoid();
         }

         this.x = mc.world;
         this.S = mc.player;
         this.r = mc.player.getEntityPos();
         if (!this.oAsBoolean(this.y)) {
            this.y = this.qAsPlayerEntity();
            this.g.oAsVoid();
            this.U.aAsVoid();
         }

         if (this.y == null) {
            this.KAsVoid("Waiting for a target");
         } else {
            Item var1 = this.HAsItem();
            if (var1 != Items.AIR && BlockPlacementController.qAsInt(var1, this.J.getValue()) != -1) {
               if (this.C != null) {
                  if (!BlockPlacementController.jAsBoolean(this.C)) {
                     this.C = null;
                  } else {
                     if (mc.player.age - this.X < 10) {
                        this.a.mAsVoid(this.C);
                        this.KAsVoid("Waiting for world update");
                        return;
                     }

                     this.C = null;
                     this.g.oAsVoid();
                     this.KAsVoid("Block update missing; previewing a retry");
                  }
               }

               List<BlockPos> var2 = this.eAsList(this.y);
               ArrayList var3 = new ArrayList<>(var2);
               if (this.vAsBoolean(var2)) {
                  var3.addAll(sAsList(var2, mc.player.getEyePos()));
               }

               BlockPos var4 = this.g.PAsBlockPos();
               if (var4 == null || !var3.contains(var4) || !this.SAsBoolean(var4, var1)) {
                  var4 = null;

                  for (BlockPos var6 : (Iterable<BlockPos>) (var3)) {
                     if (this.SAsBoolean(var6, var1)) {
                        var4 = var6;
                        break;
                     }
                  }
               }

               if (var4 != null) {
                  BlockPos var10 = var4;
                  PlayerEntity var12 = this.y;
                  this.g.aAsVoid(var10, var12, var1, mc.player.age);
                  this.a.mAsVoid(var10);
                  BlockHitResult var7 = BlockPlacementController.uAsBlockHitResult(var10, var1, this.h.getValue(), this.RAsBoolean());
                  if (var7 == null) {
                     this.g.oAsVoid();
                  } else {
                     boolean var8 = BlockPlacementController.JAsBoolean(
                        this,
                        var10,
                        var1,
                        this.h.getValue(),
                        this.RAsBoolean(),
                        this.J.getValue(),
                        () -> this.cAsBoolean() && this.y == var12 && this.oAsBoolean(var12) && this.HAsItem() == var1 && this.WAsBoolean(var12, var10),
                        () -> {
                           float[] var2x = ModuleDetector.QAsFloatArray(mc.player.getEyePos(), var7.getPos());
                           int var3x = Math.abs(MathHelper.wrapDegrees(var2x[0] - mc.player.getYaw())) < 2.0F
                                 && Math.abs(var2x[1] - mc.player.getPitch()) < 2.0F
                              ? 1
                              : 0;
                           return (!this.RAsBoolean() || ParticleEmitter.NAsP5_Type_u() == ParticleEmitter.Type_u.AUTOTRAP)
                              && this.SAsBoolean(mc.player.age, this.rAsBoolean(), var3x != 0);
                        },
                        var2x -> {
                           this.WAsVoid(var2x);
                           if (var2x) {
                              this.C = var10;
                              this.X = mc.player.age;
                           } else {
                              this.g.oAsVoid();
                           }
                        }
                     );
                     if (var8) {
                        this.KAsVoid("Preparing trap placement");
                     }
                  }
               } else {
                  this.g.oAsVoid();
                  this.U.aAsVoid();
                  boolean var9 = var2.stream().noneMatch(BlockPlacementController::jAsBoolean);
                  int var11 = !this.trapMode.pAsBoolean("Legs")
                        && var2.stream()
                           .filter(BlockPlacementController::jAsBoolean)
                           .allMatch(var1x -> var1x.getY() == var2.stream().mapToInt(Vec3i::getY).max().orElse(0))
                     ? 1
                     : 0;
                  this.KAsVoid(
                     var9
                        ? "Trap enclosure filled"
                        : (
                           var11 != 0
                              ? "Roof needs a higher view; jump or move to expose a supported face"
                              : "Move closer or around the target to reach the remaining trap faces"
                        )
                  );
               }
            } else {
               this.g.oAsVoid();
               this.U.aAsVoid();
               this.KAsVoid("Trap blocks missing from hotbar");
            }
         }
      }
   }
}
