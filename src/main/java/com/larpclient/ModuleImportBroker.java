package com.larpclient;

import com.larpclient.module.AnchorTap;
import com.larpclient.module.AutoCrystal;
import com.larpclient.module.Criticals;
import com.larpclient.module.Module;
import com.larpclient.module.RodSwap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.LookAndOnGround;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public final class ModuleImportBroker {
   private static final List<ModuleImportBroker.Type_p> s = new ArrayList<>();
   private static boolean F;
   private static boolean c;
   private static final List<ModuleImportBroker.Type_c> L = new ArrayList<>();
   private static ModuleImportBroker.Type_c K;
   private static int j;
   private static long M = Long.MIN_VALUE;
   private static final Set<Integer> y = new HashSet<>();
   private static String R;
   private static boolean f;

   public static void UAsVoid(Module var0, Runnable var1, BooleanSupplier var2) {
      s.add(new ModuleImportBroker.Type_p(var0, var1, var2));
   }

   public static boolean nAsBoolean(Module var0) {
      XAsVoid();
      return tAsBoolean(var0) && (R == null || R.equals(var0.getName()));
   }

   public static boolean VAsBoolean(Module var0, Runnable var1, BooleanSupplier var2) {
      XAsVoid();
      if (var0 != null
         && var0.isRuntimeActive()
         && jAsBoolean()
         && var2.getAsBoolean()
         && K == null
         && !F
         && y.isEmpty()
         && (R == null || R.equals(var0.getName()))) {
         R = var0.getName();
         F = true;
         c = true;

         try {
            var1.run();
         } finally {
            c = false;
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean ZAsBoolean(Module var0, Entity var1, double var2) {
      XAsVoid();
      if (var0 != null && var1 != null && var0.isRuntimeActive() && K == null && !F && LAsBoolean(var0.getName(), var1) && nAsBoolean(var1, var2)) {
         MinecraftClient var4 = MinecraftClient.getInstance();
         ModuleImportBroker.Type_c var5 = new ModuleImportBroker.Type_c(var0, var1, var2, false, () -> true, 1);
         GAsVoid(var5);
         return ParticleKind.BAsBoolean(var4.player);
      } else {
         return false;
      }
   }

   public static boolean WAsBoolean(Module var0, Entity var1, Runnable var2) {
      XAsVoid();
      if (var0 != null && var1 != null && var0.isRuntimeActive() && tAsBoolean(var0) && K == null && !F && YAsBoolean(var0.getName(), var1)) {
         f = true;

         try {
            var2.run();
         } finally {
            f = false;
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean DAsBoolean(Module var0, Entity var1, Runnable var2) {
      XAsVoid();
      if (var0 != null && var1 != null && var0.isRuntimeActive() && tAsBoolean(var0) && y.contains(var1.getId())) {
         f = true;

         try {
            var2.run();
         } finally {
            f = false;
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean EAsBoolean() {
      return f || c;
   }

   public static boolean JAsBoolean() {
      if (!jAsBoolean()) {
         return false;
      } else {
         XAsVoid();
         if (c) {
            return true;
         } else if (AnchorTap.YAsBoolean()) {
            return false;
         } else if (K != null || R != null && !R.equals("Manual")) {
            return false;
         } else {
            F = true;
            R = "Manual";
            return true;
         }
      }
   }

   public static boolean PAsBoolean() {
      return SAsBoolean() && MinecraftClient.getInstance().currentScreen == null;
   }

   private static boolean SAsBoolean() {
      MinecraftClient var0 = MinecraftClient.getInstance();
      return var0.player != null && var0.world != null && var0.interactionManager != null && var0.player.isAlive() && !var0.player.isSpectator();
   }

   public static boolean jAsBoolean() {
      return PAsBoolean() && !ParticleEmitter.oAsBoolean();
   }

   public static boolean tAsBoolean(Module var0) {
      return fAsBoolean(var0) && !ParticleEmitter.oAsBoolean();
   }

   public static boolean fAsBoolean(Module var0) {
      return SAsBoolean() && AAsBoolean(MinecraftClient.getInstance().currentScreen != null, var0);
   }

   public static boolean AAsBoolean(boolean var0, Module var1) {
      return !var0 || var1 != null && var1.isRuntimeActive() && var1.allowsScreenInteractions();
   }

   public static boolean NAsBoolean() {
      return !L.isEmpty() || !s.isEmpty() || K != null;
   }

   public static void KAsVoid() {
      L.clear();
      s.clear();
      XAsVoid();
   }

   private static void XAsVoid() {
      ClientWorld var0 = MinecraftClient.getInstance().world;
      if (var0 != null) {
         uAsVoid(var0.getTime());
      }
   }

   private static void uAsVoid(long var0) {
      if (M != var0) {
         M = var0;
         y.clear();
         R = null;
         F = false;
      }
   }

   public static void AAsVoid() {
      FallDamage.tAsVoid();
      BlockPlacementController.dAsVoid();
      L.clear();
      s.clear();
      F = false;
      c = false;
      K = null;
      y.clear();
      R = null;
      M = Long.MIN_VALUE;
      f = false;
   }

   public static void KAsVoid(Module var0) {
      L.removeIf(var1 -> var1.source == var0);
      s.removeIf(var1 -> var1.source == var0);
      if (K != null && K.source == var0) {
         K = null;
      }
   }

   public static void zAsVoid(Module var0, Entity var1, double var2, boolean var4, BooleanSupplier var5) {
      BAsVoid(var0, var1, var2, var4, var5, 1);
   }

   public static void BAsVoid(Module var0, Entity var1, double var2, boolean var4, BooleanSupplier var5, int var6) {
      L.add(new ModuleImportBroker.Type_c(var0, var1, var2, var4, var5, var6));
   }

   public static void zAsVoid(Module var0, Entity var1, double var2, BooleanSupplier var4, Supplier<float[]> var5, Runnable var6) {
      L.add(new ModuleImportBroker.Type_c(var0, var1, var2, false, var4, 1, var5, var6, var0x -> {
      }));
   }

   public static double HAsDouble(Entity var0) {
      return OptifineBridge.lAsDouble(MinecraftClient.getInstance().player.getEyePos(), var0.getBoundingBox());
   }

   public static boolean nAsBoolean(Entity var0, double var1) {
      ClientPlayerEntity var3 = MinecraftClient.getInstance().player;
      return var3 != null && eAsBoolean(var0, var1, var3.getRotationVector());
   }

   public static void tAsVoid(Module var0, Entity var1, double var2, boolean var4, BooleanSupplier var5, int var6, Runnable var7, Consumer<String> var8) {
      L.add(new ModuleImportBroker.Type_c(var0, var1, var2, var4, var5, var6, null, var7, var8));
   }

   public static boolean VAsBoolean() {
      return MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().player.getAttackCooldownProgress(0.0F) >= 1.0F;
   }

   public static double MAsDouble(double var0) {
      ClientPlayerEntity var2 = MinecraftClient.getInstance().player;
      return var2 == null ? 0.0 : Math.max(0.0, Math.min(var0, var2.getEntityInteractionRange()));
   }

   public static boolean eAsBoolean(Entity var0, double var1, Vec3d var3) {
      return MAsString(var0, var1, var3) == null;
   }

   public static String MAsString(Entity var0, double var1, Vec3d var3) {
      if (!jAsBoolean() || var0 == null) {
         return "interaction ownership";
      } else if (RodSwap.DAsBoolean()) {
         return "temporary rod selection";
      } else {
         MinecraftClient var4 = MinecraftClient.getInstance();
         if (var0 != var4.player && var0.getEntityWorld() == var4.world && var0.isAlive() && !var0.isRemoved() && !var0.isSpectator() && var0.canHit()) {
            double var5 = MAsDouble(var1);
            Vec3d var7 = var4.player.getEyePos();
            if (HAsDouble(var0) > var5 + 1.0E-7) {
               return "range";
            } else {
               Optional var8 = OptifineBridge.iAsOptional(var7, var3, var0.getBoundingBox(), var5);
               if (var8.isEmpty()) {
                  return "alignment";
               } else {
                  return QAsBoolean(var0, var7, (Vec3d)var8.get(), CAsList(var0, var7)) ? null : "obstruction";
               }
            }
         } else {
            return "target invalid";
         }
      }
   }

   private static List<Entity> CAsList(Entity var0, Vec3d var1) {
      MinecraftClient var2 = MinecraftClient.getInstance();
      return var2.world.getOtherEntities(var2.player, var0.getBoundingBox().union(new Box(var1, var1)).expand(1.0));
   }

   private static boolean QAsBoolean(Entity var0, Vec3d var1, Vec3d var2, List<Entity> var3) {
      MinecraftClient var4 = MinecraftClient.getInstance();
      BlockHitResult var5 = var4.world.raycast(new RaycastContext(var1, var2, ShapeType.COLLIDER, FluidHandling.NONE, var4.player));
      if (var5.getType() != Type.MISS && var1.squaredDistanceTo(var5.getPos()) + 1.0E-7 < var1.squaredDistanceTo(var2)) {
         return false;
      } else {
         for (Entity var7 : var3) {
            if (var7 != var0 && var7.canHit() && !var7.isSpectator() && OptifineBridge.EAsBoolean(var1, var2, var7.getBoundingBox())) {
               return false;
            }
         }

         return true;
      }
   }

   public static BlockHitResult TAsBlockHitResult(BlockPos var0, double var1) {
      return !jAsBoolean() ? null : eAsBlockHitResult(var0, var1, MinecraftClient.getInstance().player.getRotationVector());
   }

   public static BlockHitResult eAsBlockHitResult(BlockPos var0, double var1, Vec3d var3) {
      if (!jAsBoolean()) {
         return null;
      } else {
         MinecraftClient var4 = MinecraftClient.getInstance();
         double var5 = Math.max(0.0, Math.min(var1, var4.player.getBlockInteractionRange()));
         Vec3d var7 = var4.player.getEyePos();
         Vec3d var8 = var7.add(var3.normalize().multiply(var5));
         BlockHitResult var9 = var4.world.raycast(new RaycastContext(var7, var8, ShapeType.OUTLINE, FluidHandling.NONE, var4.player));
         if (var9.getType() == Type.BLOCK && var9.getBlockPos().equals(var0)) {
            for (Entity var11 : var4.world.getOtherEntities(var4.player, new Box(var7, var8).expand(1.0))) {
               if (var11.canHit() && !var11.isSpectator() && OptifineBridge.EAsBoolean(var7, var9.getPos(), var11.getBoundingBox())) {
                  return null;
               }
            }

            return var9;
         } else {
            return null;
         }
      }
   }

   public static Optional<Vec3d> nAsOptional(Entity var0, double var1, Vec3d var3, Vec3d var4) {
      if (jAsBoolean() && var0 != null && !(HAsDouble(var0) > var1 + 1.0E-7)) {
         Vec3d var5 = MinecraftClient.getInstance().player.getEyePos();
         List var6 = CAsList(var0, var5);
         return com.larpclient.BoxUtils.bAsOptional(var0.getBoundingBox(), var5, var3, var4, var5xx -> {
            Optional var6x = OptifineBridge.iAsOptional(var5, var5xx.subtract(var5), var0.getBoundingBox(), var1);
            return var6x.isPresent() && QAsBoolean(var0, var5, (Vec3d)var6x.get(), var6);
         });
      } else {
         return Optional.empty();
      }
   }

   public static int bAsInt(String var0) {
      return switch (var0) {
         case "Aura" -> 4;
         case "AutoCart" -> 5;
         case "AutoCrystal" -> 6;
         case "Auto MLG" -> 7;
         case "Auto Pearl Catch" -> 8;
         case "Wind Charge Hop" -> 8;
         case "TriggerBot" -> 2;
         case "AutoClicker" -> 1;
         case "Scaffold" -> 1;
         case "Auto Drain" -> 1;
         case "Rod Swap" -> 0;
         default -> 5;
      };
   }

   private static Vec3d HAsVec3d(ModuleImportBroker.Type_c var0) {
      if (var0.aim != null) {
         float[] var1 = var0.aim.get();
         return Vec3d.fromPolar(var1[1], var1[0]);
      } else {
         return MinecraftClient.getInstance().player.getRotationVector();
      }
   }

   public static void wAsVoid() {
      XAsVoid();
      if (jAsBoolean()) {
         AutoCrystal var0 = AutoCrystal.vAsAutoCrystal();
         if (var0 != null) {
            var0.LAsVoid();
         }

         BlockPlacementController.wAsVoid();
      }
   }

   private static boolean xAsBoolean(ModuleImportBroker.Type_c var0) {
      String var1;
      if ((var0.source == null || var0.source.isRuntimeActive()) && var0.valid.getAsBoolean()) {
         if (var0.cooldown && !VAsBoolean()) {
            var1 = "cooldown";
         } else {
            var1 = MAsString(var0.target, var0.range, HAsVec3d(var0));
         }
      } else {
         var1 = "intent expired / item use";
      }

      if (var1 != null) {
         var0.rejected.accept(var1);
      }

      return var1 == null;
   }

   public static void aAsVoid() {
      XAsVoid();
      if (SAsBoolean() && !ParticleEmitter.oAsBoolean()) {
         if (!PAsBoolean()) {
            L.clear();
            K = null;
            BlockPlacementController.dAsVoid();
         }

         MinecraftClient var0 = MinecraftClient.getInstance();
         L.sort(Comparator.<ModuleImportBroker.Type_c>comparingInt(var0x -> bAsInt(var0x.source.getName())).reversed());
         if (K != null) {
            int var1 = !s.stream()
                     .anyMatch(var0x -> var0x.source.isRuntimeActive() && var0x.valid.getAsBoolean() && bAsInt(var0x.source.getName()) > bAsInt(iAsString(K)))
                  && xAsBoolean(K)
                  && Criticals.dAsBoolean()
                  && (var0.player.age == j || !var0.player.isOnGround())
                  && L.stream().noneMatch(var0x -> xAsBoolean(var0x) && bAsInt(iAsString(var0x)) > bAsInt(iAsString(K)))
               ? 1
               : 0;
            switch (ModuleKeybinds.UAsMk_Type_g(var0.player.age - j, var1 != 0, Criticals.yAsBoolean())) {
               case CANCEL:
                  K.rejected.accept("critical delay cancelled");
                  K = null;
                  break;
               case READY:
                  GAsVoid(K);
                  K = null;
                  break;
               case WAIT:
                  L.clear();
                  s.clear();
                  return;
            }
         }

         s.sort(Comparator.<ModuleImportBroker.Type_p>comparingInt(var0x -> bAsInt(var0x.source.getName())).reversed());

         for (ModuleImportBroker.Type_c var2 : List.copyOf(L)) {
            if (s.stream()
               .anyMatch(var1x -> var1x.source.isRuntimeActive() && var1x.valid.getAsBoolean() && bAsInt(var1x.source.getName()) > bAsInt(iAsString(var2)))) {
               var2.rejected.accept("interaction ownership");
            } else if (!LAsBoolean(iAsString(var2), var2.target) || y.size() >= var2.limit) {
               var2.rejected.accept("interaction ownership");
            } else if (xAsBoolean(var2)) {
               if (JAsBoolean(var2)) {
                  break;
               }

               GAsVoid(var2);
            }
         }

         L.clear();
         if (K == null && !F) {
            for (ModuleImportBroker.Type_p var8 : List.copyOf(s)) {
               if (tAsBoolean(var8.source) && var8.source.isRuntimeActive() && var8.valid.getAsBoolean() && (R == null || R.equals(var8.source.getName()))) {
                  R = var8.source.getName();
                  F = true;
                  c = true;

                  try {
                     var8.action.run();
                     break;
                  } finally {
                     c = false;
                  }
               }
            }
         }

         s.clear();
      } else {
         AAsVoid();
      }
   }

   private static String iAsString(ModuleImportBroker.Type_c var0) {
      return var0.source == null ? "Manual" : var0.source.getName();
   }

   private static boolean JAsBoolean(ModuleImportBroker.Type_c var0) {
      if (var0.target instanceof LivingEntity && Criticals.qAsBoolean()) {
         var0.rejected.accept("critical delay");
         K = var0;
         j = MinecraftClient.getInstance().player.age;
         return true;
      } else {
         return false;
      }
   }

   private static void GAsVoid(ModuleImportBroker.Type_c var0) {
      if (xAsBoolean(var0) && y.size() < var0.limit && YAsBoolean(iAsString(var0), var0.target)) {
         MinecraftClient var1 = MinecraftClient.getInstance();
         int var2 = var0.aim != null ? 1 : 0;
         f = true;

         try {
            if (var2 != 0) {
               float[] var3 = var0.aim.get();
               var1.player.networkHandler.sendPacket(new LookAndOnGround(var3[0], var3[1], var1.player.isOnGround(), var1.player.horizontalCollision));
            }

            var1.interactionManager.attackEntity(var1.player, var0.target);
            var1.player.swingHand(Hand.MAIN_HAND);
            var0.completed.run();
         } finally {
            f = false;
            if (var2 != 0) {
               var1.player
                  .networkHandler
                  .sendPacket(new LookAndOnGround(var1.player.getYaw(), var1.player.getPitch(), var1.player.isOnGround(), var1.player.horizontalCollision));
            }
         }
      }
   }

   private static boolean LAsBoolean(String var0, Entity var1) {
      return (R == null || R.equals(var0)) && !y.contains(var1.getId());
   }

   private static boolean YAsBoolean(String var0, Entity var1) {
      if (!LAsBoolean(var0, var1)) {
         return false;
      } else {
         R = var0;
         y.add(var1.getId());
         return true;
      }
   }

   public static boolean YAsBoolean(Entity var0) {
      if (!f) {
         RodSwap.xAsVoid();
      }

      if (f) {
         return true;
      } else if (!nAsBoolean(var0, Double.MAX_VALUE)) {
         return false;
      } else {
         XAsVoid();
         if (K != null) {
            return false;
         } else {
            MinecraftClient var1 = MinecraftClient.getInstance();
            ModuleImportBroker.Type_c var2 = new ModuleImportBroker.Type_c(
               null,
               var0,
               var1.player.getEntityInteractionRange(),
               true,
               () -> var1.options.attackKey.isPressed() && var1.crosshairTarget instanceof EntityHitResult var2x && var2x.getEntity() == var0,
               1
            );
            return LAsBoolean("Manual", var0) && !JAsBoolean(var2) ? YAsBoolean("Manual", var0) : false;
         }
      }
   }

   public static boolean MAsBoolean() {
      return K != null;
   }

   public static boolean pAsBoolean() {
      return f;
   }

   private ModuleImportBroker() {
   }

   private static record Type_c(
      Module source,
      Entity target,
      double range,
      boolean cooldown,
      BooleanSupplier valid,
      int limit,
      Supplier<float[]> aim,
      Runnable completed,
      Consumer<String> rejected
   ) {
      Type_c(Module var1, Entity var2, double var3, boolean var5, BooleanSupplier var6, int var7) {
         this(var1, var2, var3, var5, var6, var7, null, () -> {
         }, var0 -> {
         });
      }
   }

   private static record Type_p(Module source, Runnable action, BooleanSupplier valid) {
   }
}
