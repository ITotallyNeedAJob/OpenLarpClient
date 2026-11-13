package com.larpclient;

import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import net.minecraft.world.explosion.ExplosionImpl;

public final class nf {
   public static final double c = 6.0;

   public static float vAsFloat(double var0, double var2) {
      return sAsFloat(var0, var2, 6.0);
   }

   public static float sAsFloat(double var0, double var2, double var4) {
      if (Double.isFinite(var0) && Double.isFinite(var2) && Double.isFinite(var4) && !(var4 <= 0.0)) {
         double var6 = var4 * 2.0;
         if (var0 > var6) {
            return 0.0F;
         } else {
            double var8 = (1.0 - Math.clamp(var0 / var6, 0.0, 1.0)) * Math.clamp(var2, 0.0, 1.0);
            return (float)((var8 * var8 + var8) / 2.0 * 7.0 * var6 + 1.0);
         }
      } else {
         return 0.0F;
      }
   }

   public static boolean uAsBoolean(double var0, double var2, double var4) {
      return Double.isFinite(var0) && var0 <= var2 && var0 < var4;
   }

   public static float IAsFloat(LivingEntity var0, Vec3d var1) {
      return TAsFloat(var0, var1, 6.0);
   }

   public static float TAsFloat(LivingEntity var0, Vec3d var1, double var2) {
      return wAsFloatArray(var0, var1, var2)[0];
   }

   public static float aAsFloat(LivingEntity var0, Vec3d var1, BlockPos var2) {
      return DAsFloat(var0, var1, var2, Vec3d.ZERO);
   }

   public static float DAsFloat(LivingEntity var0, Vec3d var1, BlockPos var2, Vec3d var3) {
      return OAsFloatArray(var0, var1, var2, var3, 6.0)[0];
   }

   public static float[] wAsFloatArray(LivingEntity var0, Vec3d var1, double... var2) {
      return OAsFloatArray(var0, var1, null, Vec3d.ZERO, var2);
   }

   private static float[] OAsFloatArray(LivingEntity var0, Vec3d var1, BlockPos var2, Vec3d var3, double... var4) {
      float[] var5 = new float[var4.length];
      if (var0 instanceof PlayerEntity var6 && var6.getAbilities().invulnerable) {
         return var5;
      }

      double var15 = var0.getEntityPos().add(var3).distanceTo(var1);
      double var8 = 0.0;

      for (double var13 : var4) {
         if (Double.isFinite(var13)) {
            var8 = Math.max(var8, var13 * 2.0);
         }
      }

      if (var15 > var8) {
         return var5;
      } else {
         float var16 = zAsFloat(var1, var0, var2, var3);

         for (int var17 = 0; var17 < var4.length; var17++) {
            var5[var17] = DAsFloat(var0, sAsFloat(var15, (double)var16, var4[var17]));
         }

         return var5;
      }
   }

   public static float zAsFloat(Vec3d var0, Entity var1, BlockPos var2) {
      return zAsFloat(var0, var1, var2, Vec3d.ZERO);
   }

   public static float zAsFloat(Vec3d var0, Entity var1, BlockPos var2, Vec3d var3) {
      if (var2 == null && var3.equals(Vec3d.ZERO)) {
         return ExplosionImpl.calculateReceivedDamage(var0, var1);
      } else {
         Box var4 = var1.getBoundingBox().offset(var3);
         Box var5 = var2 == null ? null : new Box(var2);
         double var6 = 1.0 / ((var4.maxX - var4.minX) * 2.0 + 1.0);
         double var8 = 1.0 / ((var4.maxY - var4.minY) * 2.0 + 1.0);
         double var10 = 1.0 / ((var4.maxZ - var4.minZ) * 2.0 + 1.0);
         double var12 = (1.0 - Math.floor(1.0 / var6) * var6) / 2.0;
         double var14 = (1.0 - Math.floor(1.0 / var10) * var10) / 2.0;
         if (!(var6 < 0.0) && !(var8 < 0.0) && !(var10 < 0.0)) {
            int var16 = 0;
            int var17 = 0;

            for (double var18 = 0.0; var18 <= 1.0; var18 += var6) {
               for (double var20 = 0.0; var20 <= 1.0; var20 += var8) {
                  for (double var22 = 0.0; var22 <= 1.0; var22 += var10) {
                     Vec3d var24 = new Vec3d(
                        MathHelper.lerp(var18, var4.minX, var4.maxX) + var12,
                        MathHelper.lerp(var20, var4.minY, var4.maxY),
                        MathHelper.lerp(var22, var4.minZ, var4.maxZ) + var14
                     );
                     if ((var5 == null || var5.raycast(var24, var0).isEmpty())
                        && var1.getEntityWorld().raycast(new RaycastContext(var24, var0, ShapeType.COLLIDER, FluidHandling.NONE, var1)).getType() == Type.MISS) {
                        var16++;
                     }

                     var17++;
                  }
               }
            }

            return (float)var16 / (float)var17;
         } else {
            return 0.0F;
         }
      }
   }

   public static float DAsFloat(LivingEntity var0, float var1) {
      if (var1 <= 0.0F) {
         return 0.0F;
      } else {
         if (var0 instanceof PlayerEntity) {
            var1 = switch (var0.getEntityWorld().getDifficulty()) {
               case PEACEFUL -> 0.0F;
               case EASY -> Math.min(var1 / 2.0F + 1.0F, var1);
               case NORMAL -> var1;
               case HARD -> var1 * 1.5F;
               default -> throw new MatchException(null, null);
            };
         }

         DamageSource var2 = var0.getEntityWorld().getDamageSources().explosion(null);
         var1 = DamageUtil.getDamageLeft(var0, var1, var2, (float)var0.getArmor(), (float)var0.getAttributeValue(EntityAttributes.ARMOR_TOUGHNESS));
         StatusEffectInstance var3 = var0.getStatusEffect(StatusEffects.RESISTANCE);
         if (var3 != null) {
            var1 *= Math.max(0.0F, 1.0F - 0.2F * (float)(var3.getAmplifier() + 1));
         }

         int var4 = 0;

         for (EquipmentSlot var8 : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
            ItemEnchantmentsComponent var9 = (ItemEnchantmentsComponent)var0.getEquippedStack(var8).get(DataComponentTypes.ENCHANTMENTS);
            if (var9 != null) {
               for (Entry var11 : var9.getEnchantmentEntries()) {
                  if (((RegistryEntry)var11.getKey()).matchesKey(Enchantments.PROTECTION)) {
                     var4 += var11.getIntValue();
                  }

                  if (((RegistryEntry)var11.getKey()).matchesKey(Enchantments.BLAST_PROTECTION)) {
                     var4 += 2 * var11.getIntValue();
                  }
               }
            }
         }

         return DamageUtil.getInflictedDamage(var1, (float)Math.min(20, var4));
      }
   }

   private nf() {
   }
}
