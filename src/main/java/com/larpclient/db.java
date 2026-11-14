package com.larpclient;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.util.math.Vec3d;

public final class db {
   private static double O;
   private static double x;
   private static boolean i;
   private static boolean B = true;
   private static boolean D;
   private static float a;
   private static float v;
   private static boolean z;

   public static synchronized void uAsVoid(Packet<?> var0) {
      if (var0 instanceof PlayerMoveC2SPacket var1) {
         if (var1.changesLook()) {
            PetEffectRenderer = var1.getYaw(a);
            v = var1.getPitch(v);
            z = true;
         }

         double var3 = var1.getY(O);
         if (var1.isOnGround()) {
            AuraModule = 0.0;
         } else if (var1.changesPosition() && i) {
            double var5 = O - var3;
            AuraModule = var5 < 0.0 ? 0.0 : AuraModule + var5;
         }

         if (var1.changesPosition()) {
            O = var3;
            ChinaHatCodec = true;
         }

         B = var1.isOnGround();
      } else if (var0 instanceof ClientCommandC2SPacket var2) {
         if (var2.getMode() == Mode.START_SPRINTING) {
            D = true;
         } else if (var2.getMode() == Mode.STOP_SPRINTING) {
            D = false;
         }
      }
   }

   public static synchronized void tAsVoid() {
      ChinaHatCodec = false;
      AuraModule = 0.0;
      O = 0.0;
      B = true;
      D = false;
      z = false;
   }

   public static synchronized boolean mAsBoolean(boolean var0) {
      return D == var0;
   }

   public static synchronized Vec3d JAsVec3d(Vec3d var0) {
      return z ? Vec3d.fromPolar(v, a) : var0;
   }

   public static boolean RAsBoolean(PlayerEntity var0, Entity var1) {
      if (!sAsBoolean(var0, var1)) {
         return false;
      } else {
         MinecraftClient var2 = MinecraftClient.getInstance();
         return var2.getServer() != null && r6.NAsBoolean("NoFall")
            ? false
            : QAsBoolean(var0.getY(), var0.getVelocity().y, var0.getAttackCooldownProgress(0.0F));
      }
   }

   public static boolean jAsBoolean(PlayerEntity var0, Entity var1) {
      return var0 != null
         && fAsBoolean(
            var0.getVelocity().y,
            var0.getAttackCooldownProgress(1.0F),
            var0.isOnGround(),
            var0.isClimbing(),
            var0.isTouchingWater() || var0.isInLava(),
            var0.hasBlindnessEffect(),
            var0.hasVehicle(),
            var0.getAbilities().flying || var0.isGliding(),
            var1 instanceof LivingEntity
         );
   }

   public static boolean fAsBoolean(double var0, float var2, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7, boolean var8, boolean var9) {
      return var0 < 0.0 && var2 >= 1.0F && !var3 && !var4 && !var5 && !var6 && !var7 && !var8 && var9;
   }

   public static synchronized boolean QAsBoolean(double var0, double var2, float var4) {
      return IAsBoolean(i, O, x, B, D, var0, var2, var4);
   }

   public static boolean IAsBoolean(boolean var0, double var1, double var3, boolean var5, boolean var6, double var7, double var9, float var11) {
      return var0 && !var5 && !var6 && var3 > 0.0 && Math.abs(var7 - var1) <= 2.0E-4 && var9 < 0.0 && var11 >= 1.0F;
   }

   public static boolean sAsBoolean(PlayerEntity var0, Entity var1) {
      return var0 != null
         && ZAsBoolean(
            var0.fallDistance,
            var0.getAttackCooldownProgress(0.5F),
            var0.isOnGround(),
            var0.isClimbing(),
            var0.isTouchingWater(),
            var0.hasBlindnessEffect(),
            var0.hasVehicle(),
            var0.isSprinting(),
            var1 instanceof LivingEntity
         );
   }

   public static boolean ZAsBoolean(double var0, float var2, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7, boolean var8, boolean var9) {
      return var2 > 0.9F && var0 > 0.0 && !var3 && !var4 && !var5 && !var6 && !var7 && !var8 && var9;
   }

   private db() {
   }
}
