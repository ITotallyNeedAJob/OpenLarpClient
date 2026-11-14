package com.larpclient;

import com.larpclient.mixin.ClientPlayerEntityAccessor;
import com.larpclient.mixin.KeyBindingAccessor;
import com.larpclient.mixin.MinecraftClientAccessor;
import com.larpclient.module.MCFriend;
import com.larpclient.module.Mace;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlocksAttacksComponent;
import net.minecraft.component.type.WeaponComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.vehicle.AbstractBoatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public final class pk {
   private static PlayerEntity y;
   private static Object i;
   private static int PriceDetector = -1;
   private static LivingEntity Z;
   private static int Q;
   private static final double w = Math.toRadians(10.0);

   public static void OAsVoid(PlayerEntity var0) {
      MinecraftClient var1 = MinecraftClient.getInstance();
      if (var0 == var1.player) {
         ScreenScale = var0;
         ChinaHatCodec = var1.world;
         PriceDetector = var0.age;
      }
   }

   public static boolean BAsBoolean(ClientPlayerEntity var0) {
      return var0 != null && ScreenScale == var0 && ChinaHatCodec == var0.getEntityWorld() && PriceDetector == var0.age;
   }

   public static void kAsVoid() {
      ScreenScale = null;
      ChinaHatCodec = null;
      PriceDetector = -1;
      Z = null;
   }

   public static boolean eAsBoolean(MinecraftClient var0) {
      return FAsBoolean(var0, p5.Type_u.NONE);
   }

   public static boolean FAsBoolean(MinecraftClient var0, p5.Type_u var1) {
      p5.Type_u var2 = p5.NAsP5_Type_u();
      return b_.jAsBoolean()
         && var0.getOverlay() == null
         && var0.isWindowFocused()
         && var0.getCameraEntity() == var0.player
         && (var2 == p5.Type_u.NONE || var2 == var1);
   }

   public static boolean hAsBoolean(MinecraftClient var0, ClientPlayerEntity var1) {
      if (!var1.isUsingItem()
         && !var1.isRiding()
         && !var0.interactionManager.isBreakingBlock()
         && !fAsBoolean(var0.options.swapHandsKey)
         && !fAsBoolean(var0.options.dropKey)) {
         PlayerInput var2 = var1.getLastPlayerInput();
         return !(var1.getVehicle() instanceof AbstractBoatEntity) || var2.forward() == var2.backward() && var2.left() == var2.right();
      } else {
         return false;
      }
   }

   public static boolean fAsBoolean(KeyBinding var0) {
      return ((KeyBindingAccessor)var0).larpClickCount() > 0;
   }

   public static boolean BAsBoolean(MinecraftClient var0) {
      return fAsBoolean(var0.options.attackKey);
   }

   public static boolean IAsBoolean(MinecraftClient var0) {
      return var0.options.useKey.isPressed() || fAsBoolean(var0.options.useKey);
   }

   public static PlayerInput cAsPlayerInput(PlayerInput var0) {
      return !var0.sprint() ? var0 : new PlayerInput(var0.forward(), var0.backward(), var0.left(), var0.right(), var0.jump(), var0.sneak(), false);
   }

   public static int kAsInt(MinecraftClient var0) {
      return ((MinecraftClientAccessor)var0).larpAttackDelay();
   }

   public static void uAsVoid(MinecraftClient var0, int var1) {
      ((MinecraftClientAccessor)var0).larpAttackDelay(var1);
   }

   public static void DAsVoid(MinecraftClient var0) {
      while (var0.options.attackKey.wasPressed()) {
      }

      while (var0.options.useKey.wasPressed()) {
      }

      MinecraftClientAccessor var1 = (MinecraftClientAccessor)var0;
      var1.larpAttackDelay(Math.max(var1.larpAttackDelay(), 1));
      var1.larpUseDelay(Math.max(var1.larpUseDelay(), 2));
   }

   public static boolean FAsBoolean(ClientPlayerEntity var0, Entity var1) {
      if (var1 instanceof LivingEntity var2
         && var1 != var0
         && !(var1 instanceof ArmorStandEntity)
         && var1 != var0.getVehicle()
         && var2.isAlive()
         && var1.isAttackable()
         && !var1.isRemoved()
         && var1.getEntityWorld() == var0.getEntityWorld()) {
         if (var1 instanceof PlayerEntity var3) {
            if (var3.isSpectator() || var3.isCreative() || !var0.shouldDamagePlayer(var3) || var0.networkHandler.getPlayerListEntry(var3.getUuid()) == null) {
               return false;
            }

            LarpClient var4 = LarpClient.CAsLarpClient();
            MCFriend var5 = var4 != null && var4.mAsF() != null ? var4.mAsF().EAsModule(MCFriend.class) : null;
            if (var5 != null && var5.SAsBoolean(var3.getName().getString())) {
               return false;
            }
         }

         return true;
      }

      return false;
   }

   public static boolean WAsBoolean(ClientPlayerEntity var0, Vec3d var1, Vec3d var2) {
      BlockHitResult var3 = var0.getEntityWorld().raycast(new RaycastContext(var1, var2, ShapeType.COLLIDER, FluidHandling.NONE, var0));
      return var3.getType() == Type.MISS || var1.squaredDistanceTo(var3.getPos()) >= var1.squaredDistanceTo(var2) - 0.001;
   }

   public static EntityHitResult CAsEntityHitResult(MinecraftClient var0) {
      if (var0.player != null && var0.world != null) {
         HitResult var1 = var0.player.getCrosshairTarget(1.0F, var0.player);
         if (var1 instanceof EntityHitResult var2 && WAsBoolean(var0.player, var0.player.getEyePos(), var1.getPos())) {
            return var2;
         }

         return null;
      } else {
         return null;
      }
   }

   public static float jAsFloat(ClientPlayerEntity var0) {
      return var0.getAttackCooldownProgress(0.5F);
   }

   public static double CAsDouble(ItemStack var0) {
      double[] var1 = new double[]{0.0};
      var0.applyAttributeModifiers(EquipmentSlot.MAINHAND, (var1xx, var2) -> {
         if (var1xx.equals(EntityAttributes.ATTACK_DAMAGE) && var2.operation() == Operation.ADD_VALUE) {
            var1[0] += var2.value();
         }
      });
      return var1[0];
   }

   public static boolean CAsBoolean(ClientPlayerEntity var0, LivingEntity var1) {
      return jAsFloat(var0) > 0.9F
         && var0.fallDistance > 0.0
         && !var0.isOnGround()
         && !var0.isClimbing()
         && !var0.isTouchingWater()
         && !var0.hasBlindnessEffect()
         && !var0.hasVehicle()
         && !var0.isSprinting();
   }

   public static boolean PAsBoolean(LivingEntity var0, ClientPlayerEntity var1) {
      if (!var0.isUsingItem()) {
         return false;
      } else {
         ItemStack var2 = var0.getActiveItem();
         BlocksAttacksComponent var3 = (BlocksAttacksComponent)var2.get(DataComponentTypes.BLOCKS_ATTACKS);
         if (var3 != null && var0.getItemUseTime() + WAsInt(var1) >= var3.getBlockDelayTicks()) {
            DamageSource var4 = var1.getDamageSources().playerAttack(var1);
            if (var3.bypassedBy().map(var1x -> var4.isIn(var1x)).orElse(false)) {
               return false;
            } else {
               Vec3d var5 = var1.getEntityPos().subtract(var0.getEntityPos()).multiply(1.0, 0.0, 1.0).normalize();
               double var6 = Math.acos(Math.clamp(var5.dotProduct(Vec3d.fromPolar(0.0F, var0.getHeadYaw())), -1.0, 1.0));
               return var3.getDamageReductionAmount(var4, 1.0F, Math.max(0.0, var6 - w)) > 0.0F;
            }
         } else {
            return false;
         }
      }
   }

   public static int WAsInt(ClientPlayerEntity var0) {
      PlayerListEntry var1 = var0.networkHandler.getPlayerListEntry(var0.getUuid());
      return var1 == null ? 1 : Math.max(0, (var1.getLatency() + 49) / 50) + 1;
   }

   public static void wAsVoid(LivingEntity var0) {
      ClientPlayerEntity var1 = MinecraftClient.getInstance().player;
      Z = var0;
      Q = var0.age + Math.clamp(var1 == null ? 3L : (long)(WAsInt(var1) + 2), 3, 20);
   }

   public static boolean NAsBoolean(LivingEntity var0) {
      return var0 == Z && var0.age < Q;
   }

   public static int JAsInt(ClientPlayerEntity var0) {
      int var1 = -1;
      double var2 = Double.NEGATIVE_INFINITY;

      for (int var4 = 0; var4 < PlayerInventory.getHotbarSize(); var4++) {
         ItemStack var5 = var0.getInventory().getStack(var4);
         WeaponComponent var6 = (WeaponComponent)var5.get(DataComponentTypes.WEAPON);
         if (!var5.willBreakNextUse() && var6 != null && !(var6.disableBlockingForSeconds() <= 0.0F)) {
            double var7 = CAsDouble(var5);
            if (var7 > var2) {
               var1 = var4;
               var2 = var7;
            }
         }
      }

      return var1;
   }

   public static boolean MAsBoolean(MinecraftClient var0, EntityHitResult var1) {
      ClientPlayerEntity var2 = var0.player;
      if (var2 != null
         && kAsInt(var0) <= 0
         && !BAsBoolean(var2)
         && hAsBoolean(var0, var2)
         && !var2.isSpectator()
         && var2.isSprinting() == ((ClientPlayerEntityAccessor)var2).larpWasSprinting()) {
         ItemStack var3 = var2.getMainHandStack();
         return !var3.contains(DataComponentTypes.PIERCING_WEAPON)
               && var3.isItemEnabled(var0.world.getEnabledFeatures())
               && !var2.isBelowMinimumAttackCharge(var3, 0)
            ? b_.ZAsBoolean(Mace.yAsMace(), var1.getEntity(), var2.getEntityInteractionRange())
            : false;
      } else {
         return false;
      }
   }

   private pk() {
   }
}
