package com.larpclient.module;

import com.larpclient.AutoTunnel;
import com.larpclient.ClientLog;
import com.larpclient.LarpClient;
import com.larpclient.ModuleImportBroker;
import com.larpclient.ParticleEmitter;
import com.larpclient.ModuleKeybindChecker;
import com.larpclient.WidgetQueue;
import java.util.Set;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.hit.EntityHitResult;

public final class WTap extends Module {
   private static WTap r;
   private final BooleanSetting playersOnly = this.addSetting(new BooleanSetting("Players Only", "Only release forward after attacking a player", true));
   private final BooleanSetting weaponsOnly = this.addSetting(new BooleanSetting("Weapons Only", "Require a held melee weapon", true));
   private final NumberSetting releaseDuration = this.addSetting(
      new NumberSetting("Release Duration", "Forward release in native input ticks", 1.0, 1.0, 3.0, 1.0)
   );
   private final wq.Type_d d = new wq.Type_d();
   private final wq.Type_s k = new wq.Type_s();
   private ClientPlayerEntity S;
   private Object M;
   private String c = "Waiting for a sprint attack";

   public WTap() {
      super("W Tap", "Briefly release forward after a charged sprint attack", Category.COMBAT, -1);
      r = this;
   }

   public static void jAsVoid(PlayerEntity var0, Entity var1) {
      if (r != null && r.isEnabled()) {
         WTap var2 = r;
         if (var2 != null && var0 == getMc().player) {
            var2.k.uAsVoid();
            if (var2.isRuntimeActive() && var0 == getMc().player && var0 instanceof ClientPlayerEntity var3) {
               int var4 = !b_.pAsBoolean()
                     && mc.options.attackKey.isPressed()
                     && mc.crosshairTarget instanceof EntityHitResult var5
                     && var5.getEntity() == var1
                  ? 1
                  : 0;
               boolean var7 = var2.LAsBoolean(var1);
               boolean var8 = wq.LAsBoolean(
                  var4 != 0,
                  var2.gAsBoolean(var3),
                  var3.isSprinting(),
                  mc.options.forwardKey.isPressed(),
                  mc.options.backKey.isPressed(),
                  mc.options.sneakKey.isPressed(),
                  var3.getAttackCooldownProgress(0.5F),
                  var7
               );
               var2.k.SAsVoid(var0, var1, var8);
               if (var8) {
                  var2.S = var3;
                  var2.M = mc.world;
               }
            }
         }
      }
   }

   public static void cAsVoid(PlayerEntity var0, Entity var1) {
      if (r != null && r.isEnabled()) {
         WTap var2 = r;
         if (var2 != null) {
            var2.k.ZAsVoid(var0, var1);
         }
      }
   }

   public static void EAsVoid(PlayerEntity var0, Entity var1) {
      if (r != null && r.isEnabled()) {
         WTap var2 = r;
         if (var2 != null && var0 == getMc().player && var2.k.vAsBoolean(var0, var1) && var2.isRuntimeActive() && var0 == var2.S && var2.gAsBoolean(var2.S)) {
            var2.d.cAsVoid(var2.releaseDuration.getValue().intValue());
            var2.c = "Forward release queued";
            ClientLog.XAsVoid("WTap", "Executed manual sprint attack; release queued for {} input ticks", var2.releaseDuration.getValue().intValue());
         }
      }
   }

   public static PlayerInput JAsPlayerInput(ClientPlayerEntity var0, PlayerInput var1) {
      if (r != null && r.isEnabled()) {
         WTap var2 = r;
         if (var2 != null && var2.isRuntimeActive()) {
            if (var2.S != var0 || var2.M != getMc().world || !var2.gAsBoolean(var0) || !var1.forward() || var1.backward() || var1.sneak()) {
               var2.FAsVoid("Input or movement context changed");
               return var1;
            } else if (!var2.d.uAsBoolean(var0.age, true)) {
               var2.c = "Waiting for a sprint attack";
               return var1;
            } else {
               var2.c = "Releasing forward";
               var0.setSprinting(false);
               PlayerInput var3 = wq.ZAsPlayerInput(var1);
               var0.input.movementVector = wq.eAsVec2f(var3);
               return var3;
            }
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public static boolean MAsBoolean() {
      if (r != null && r.isEnabled()) {
         WTap var0 = r;
         MinecraftClient var1 = getMc();
         if (var0 == null || !var0.isRuntimeActive() || var1.player == null) {
            return false;
         } else if (var0.S == var1.player && var0.M == var1.world && var0.gAsBoolean(var1.player)) {
            return var0.d.IAsBoolean(var1.player.age);
         } else {
            var0.FAsVoid("Sprint ownership changed");
            return false;
         }
      } else {
         return false;
      }
   }

   private boolean gAsBoolean(ClientPlayerEntity var1) {
      if (var1 != null
         && var1 == getMc().player
         && r6.VAsBoolean()
         && b_.PAsBoolean()
         && mc.isWindowFocused()
         && p5.NAsP5_Type_u() == p5.Type_u.NONE
         && !var1.isUsingItem()
         && !var1.isTouchingWater()
         && !var1.isInLava()
         && !var1.isClimbing()
         && !var1.hasVehicle()
         && !var1.getAbilities().flying
         && !var1.isGliding()
         && !var1.isSleeping()
         && !var1.hasBlindnessEffect()
         && !var1.noClip
         && !var1.isInSneakingPose()
         && !var1.isSneaking()
         && AutoTunnel.SAsVec2f() == null) {
         AirStuck var2 = AirStuck.zAsAirStuck();
         ElytraMotion var3 = ElytraMotion.vAsElytraMotion();
         Fly var4 = Fly.bAsFly();
         return (var2 == null || !var2.wAsBoolean()) && (var3 == null || !var3.ZAsBoolean()) && (var4 == null || !var4.vAsBoolean()) && !Aura.rAsBoolean();
      } else {
         return false;
      }
   }

   private boolean LAsBoolean(Entity var1) {
      if (!(var1 instanceof LivingEntity var2)
         || var1 == mc.player
         || !var2.isAlive()
         || var1.isRemoved()
         || var1.isSpectator()
         || var1.getEntityWorld() != mc.world
         || var1.isInvisible()
         || mc.player.isTeammate(var1)
         || this.playersOnly.getValue() && !(var1 instanceof PlayerEntity)
         || !mc.player.canSee(var1)
         || b_.HAsDouble(var1) > mc.player.getEntityInteractionRange() + 1.0E-7) {
         return false;
      }

      if (this.weaponsOnly.getValue() && !iAsBoolean(mc.player)) {
         return false;
      } else {
         LarpClient var3 = LarpClient.CAsLarpClient();
         MCFriend var4 = var3 != null && var3.mAsF() != null ? var3.mAsF().EAsModule(MCFriend.class) : null;
         return !(var1 instanceof PlayerEntity) || var4 == null || !var4.SAsBoolean(var1.getName().getString());
      }
   }

   public static boolean iAsBoolean(PlayerEntity var0) {
      ItemStack var1 = var0.getMainHandStack();
      return !var1.isEmpty() && (var1.isIn(ItemTags.SWORDS) || var1.isIn(ItemTags.AXES) || var1.isIn(ItemTags.MELEE_WEAPON_ENCHANTABLE));
   }

   private void FAsVoid(String var1) {
      if (this.d.GAsBoolean()) {
         ClientLog.XAsVoid("WTap", "Release cancelled: {}; native input resumes", var1);
      }

      this.d.gAsVoid();
      this.k.uAsVoid();
      this.c = "Waiting for a sprint attack";
   }

   public static void RAsVoid(ClientPlayerEntity var0) {
      WTap var1 = r;
      if (var1 != null && var1.S == var0) {
         var1.FAsVoid("Position correction");
      }
   }

   @Override
   public void onEnable() {
      this.FAsVoid("Activated");
   }

   @Override
   public void onDisable() {
      this.FAsVoid("Disabled or disconnected");
      this.S = null;
      this.M = null;
   }

   public static void EAsVoid() {
      WTap var0 = r;
      if (var0 != null) {
         if (!var0.isRuntimeActive()) {
            var0.FAsVoid("Runtime unavailable");
         } else if (var0.S != null && (var0.S != getMc().player || var0.M != mc.world || !var0.gAsBoolean(mc.player))) {
            var0.FAsVoid("Context unavailable");
         }
      }
   }

   @Override
   public void onTick() {
      EAsVoid();
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(com.larpclient.module.k.VANILLA_INPUT);
   }

   @Override
   public String getStatus() {
      return this.c;
   }
}
