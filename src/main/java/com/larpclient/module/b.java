package com.larpclient.module;

import com.larpclient.LarpClient;
import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public final class b {
   private LivingEntity K;
   private Integer Q;

   public b() {
   }

   public LivingEntity HAsLivingEntity(b.Type_p var1) {
      MinecraftClient var2 = MinecraftClient.getInstance();
      if (var2.player != null && var2.world != null) {
         double var3 = var2.player.getVelocity().length() * 2.0;
         double var5 = var1.searchRange() + Math.min(3.0, var3);
         if (var1.focus() && this.Q != null) {
            if (var2.world.getEntityById(this.Q) instanceof LivingEntity var7
               && var7.isAlive()
               && this.MAsBoolean(var7, var1)
               && pAsDouble(var7) <= var5
               && HAsBoolean(var7, var1.fov())
               && (var1.ignoreWalls() || var2.player.canSee(var7))) {
               this.K = var7;
               return this.K;
            }

            this.Q = null;
         }

         ArrayList var11 = new ArrayList();

         for (Entity var9 : var2.world.getEntities()) {
            if (var9 instanceof LivingEntity) {
               LivingEntity var10 = (LivingEntity)var9;
               if (this.MAsBoolean(var10, var1)
                  && pAsDouble(var10) <= var5
                  && HAsBoolean(var10, var1.fov())
                  && (var1.ignoreWalls() || var2.player.canSee(var10))) {
                  var11.add(var10);
               }
            }
         }

         if (var11.isEmpty()) {
            this.CAsVoid();
            return null;
         } else {
            var11.sort(CAsComparator(var1.sort()));
            this.K = (LivingEntity)var11.getFirst();
            if (var1.focus()) {
               this.Q = this.K.getId();
            }

            return this.K;
         }
      } else {
         this.CAsVoid();
         return null;
      }
   }

   public boolean MAsBoolean(LivingEntity var1, b.Type_p var2) {
      MinecraftClient var3 = MinecraftClient.getInstance();
      if (var1 == var3.player) {
         return false;
      } else if (!var1.isAlive() || var1.getHealth() <= 0.0F || var1.isRemoved()) {
         return false;
      } else if (var1 == var3.player.getControllingVehicle()) {
         return false;
      } else if (var1 instanceof PlayerEntity var4) {
         if (var4.isSpectator()) {
            return false;
         } else if (AAsBoolean(var4)) {
            return false;
         } else if (!var2.players()) {
            return false;
         } else {
            return !var2.invisibles() && var4.isInvisible() ? false : var2.naked() || !YAsBoolean(var4);
         }
      } else if (var1 instanceof AnimalEntity) {
         return var2.animals();
      } else {
         return var1 instanceof MobEntity && !(var1 instanceof ArmorStandEntity) ? var2.mobs() : false;
      }
   }

   private static boolean AAsBoolean(PlayerEntity var0) {
      MCFriend var1 = LarpClient.CAsLarpClient().mAsF().EAsModule(MCFriend.class);
      return var1 != null && var1.SAsBoolean(var0.getName().getString());
   }

   private static boolean YAsBoolean(PlayerEntity var0) {
      for (EquipmentSlot var4 : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
         if (!var0.getEquippedStack(var4).isEmpty()) {
            return false;
         }
      }

      return true;
   }

   private static double pAsDouble(LivingEntity var0) {
      return y.nAsVec3d(var0).subtract(MinecraftClient.getInstance().player.getEyePos()).length();
   }

   private static double zAsDouble(LivingEntity var0) {
      ClientPlayerEntity var1 = MinecraftClient.getInstance().player;
      Vec3d var2 = var1.getEyePos();
      Vec3d var3 = Aura.YAsVec3d();
      Vec3d var4 = y.nAsVec3d(var0).subtract(var2).normalize();
      return Math.acos(MathHelper.clamp(var3.dotProduct(var4), -1.0, 1.0)) * (180.0 / Math.PI);
   }

   private static boolean HAsBoolean(LivingEntity var0, double var1) {
      return var1 >= 360.0 || zAsDouble(var0) <= Math.max(0.0, var1) * 0.5;
   }

   private static Comparator<LivingEntity> CAsComparator(String var0) {
      ClientPlayerEntity var1 = MinecraftClient.getInstance().player;
      Comparator var2 = Comparator.comparingDouble(entity -> var1.squaredDistanceTo((Entity)entity));

      return switch (var0) {
         case "Health" -> Comparator.<LivingEntity>comparingDouble(var0x -> (double)(var0x.getHealth() + var0x.getAbsorptionAmount())).thenComparing(var2);
         case "FOV" -> Comparator.comparingDouble(b::zAsDouble).thenComparing(var2);
         case "Armor" -> Comparator.comparingDouble(LivingEntity::getArmor)
         .thenComparingDouble(var0x -> (double)(var0x.getHealth() + var0x.getAbsorptionAmount()));
         default -> var2;
      };
   }

   public LivingEntity kAsLivingEntity() {
      return this.K;
   }

   public void CAsVoid() {
      this.K = null;
      this.Q = null;
   }

   public static record Type_p(
      double searchRange,
      boolean players,
      boolean mobs,
      boolean animals,
      boolean invisibles,
      boolean naked,
      boolean focus,
      boolean ignoreWalls,
      String sort,
      double fov
   ) {
   }
}
