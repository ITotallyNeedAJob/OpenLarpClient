package com.larpclient.mixin;

import com.larpclient.ActivityReporter;
import com.larpclient.CometRenderer;
import com.larpclient.FallDamage;
import com.larpclient.KnockbackAdjuster;
import com.larpclient.module.AimAssist;
import com.larpclient.module.AutoCart;
import com.larpclient.module.AutoCrystal;
import com.larpclient.module.AutoSell;
import com.larpclient.module.BoatFly;
import com.larpclient.module.DamageMarkers;
import com.larpclient.module.FakeLag;
import com.larpclient.module.Fly;
import com.larpclient.module.Freecam;
import com.larpclient.module.GrimSpeed;
import com.larpclient.module.Halloween;
import com.larpclient.module.JumpFX;
import com.larpclient.module.KillFX;
import com.larpclient.module.Reach;
import com.larpclient.module.Speed;
import com.larpclient.module.TargetStrafe;
import com.larpclient.module.WTap;
import com.larpclient.module.q;
import java.util.Optional;
import java.util.Set;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPosition;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.network.packet.s2c.play.CloseScreenS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityAnimationS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityDamageS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerAbilitiesS2CPacket;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.network.packet.s2c.play.VehicleMoveS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ClientPlayNetworkHandler.class})
public class ClientPlayNetworkHandlerOnCloseScreenMixin {
   public ClientPlayNetworkHandlerOnCloseScreenMixin() {
   }

   @Inject(
      method = {"method_11102(Lnet/minecraft/class_2645;)V"},
      at = {@At("RETURN")}
   )
   private void larpclient$injectV(CloseScreenS2CPacket var1, CallbackInfo var2) {
      AutoSell.FAsVoid(var1.getSyncId());
   }

   @Inject(
      method = {"method_11079(Lnet/minecraft/class_2761;)V"},
      at = {@At("TAIL")}
   )
   private void larpclient$injectV(WorldTimeUpdateS2CPacket var1, CallbackInfo var2) {
      q.MAsVoid();
   }

   @Inject(
      method = {"method_49034(Lnet/minecraft/class_8143;)V"},
      at = {@At("TAIL")}
   )
   private void larpclient$injectV(EntityDamageS2CPacket var1, CallbackInfo var2) {
      ActivityReporter.pAsVoid(var1);
      DamageMarkers var3 = DamageMarkers.DAsDamageMarkers();
      if (var3 != null) {
         var3.qAsVoid(var1);
      }

      KillFX var4 = KillFX.aAsKillFX();
      if (var4 != null) {
         var4.aAsVoid(var1);
      }

      Halloween var5 = Halloween.HAsHalloween();
      if (var5 != null) {
         var5.kAsVoid(var1);
      }
   }

   @Inject(
      method = {"method_11148(Lnet/minecraft/class_2663;)V"},
      at = {@At("TAIL")}
   )
   private void larpclient$injectV(EntityStatusS2CPacket var1, CallbackInfo var2) {
      ActivityReporter.qAsVoid(var1);
      KillFX var3 = KillFX.aAsKillFX();
      if (var3 != null) {
         var3.lAsVoid(var1);
      }

      AutoCrystal var4 = AutoCrystal.vAsAutoCrystal();
      if (var4 != null) {
         var4.AAsVoid(var1);
      }
   }

   @Inject(
      method = {"method_11134(Lnet/minecraft/class_2692;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_1297;method_5641(DDDFF)V",
         shift = Shift.AFTER
      )}
   )
   private void larpclient$injectV(VehicleMoveS2CPacket var1, CallbackInfo var2) {
      BoatFly.gAsVoid();
   }

   @Inject(
      method = {"method_11160(Lnet/minecraft/class_2616;)V"},
      at = {@At("TAIL")}
   )
   private void larpclient$injectV(EntityAnimationS2CPacket var1, CallbackInfo var2) {
      DamageMarkers var3 = DamageMarkers.DAsDamageMarkers();
      if (var3 != null) {
         var3.AAsVoid(var1);
      }
   }

   @Inject(
      method = {"method_64897(Lnet/minecraft/class_10182;Ljava/util/Set;Lnet/minecraft/class_1297;Z)Z"},
      at = {@At("RETURN")}
   )
   private static void larpclient$injectZZ(EntityPosition var0, Set<PositionFlag> var1, Entity var2, boolean var3, CallbackInfoReturnable<Boolean> var4) {
      if (var2 instanceof ClientPlayerEntity var5) {
         db.tAsVoid();
         FakeLag.QAsVoid(var5);
         Speed.dAsVoid(var5);
         TargetStrafe.LAsVoid(var5);
         GrimSpeed.PAsVoid(var5);
         AimAssist.fAsVoid(var5);
         WTap.RAsVoid(var5);
         ai.uAsVoid();
         JumpFX var6 = JumpFX.JAsJumpFX();
         if (var6 != null) {
            var6.SAsVoid();
         }
      }
   }

   @Redirect(
      method = {"method_11124(Lnet/minecraft/class_2664;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_2664;comp_2884()Ljava/util/Optional;"
      )}
   )
   private Optional<Vec3d> larpclient$redirectV(ExplosionS2CPacket var1) {
      return var1.playerKnockback().map(var0 -> n5.eAsVec3d(var0, true));
   }

   @Inject(
      method = {"method_11124(Lnet/minecraft/class_2664;)V"},
      at = {@At("RETURN")}
   )
   private void larpclient$injectV(ExplosionS2CPacket var1, CallbackInfo var2) {
      AutoCart.KAsVoid(var1.center(), var1.radius());
   }

   @Redirect(
      method = {"method_11149(Lnet/minecraft/class_2781;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_1324;method_6192(D)V"
      )}
   )
   private void larpclient$redirectV(EntityAttributeInstance var1, double var2) {
      Reach var4 = Reach.nAsReach();
      if (var4 != null) {
         var4.bAsVoid(var1);
      }

      var1.setBaseValue(var2);
   }

   @Inject(
      method = {"method_11154(Lnet/minecraft/class_2696;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_1657;method_31549()Lnet/minecraft/class_1656;",
         ordinal = 0
      )}
   )
   private void larpclient$injectV(PlayerAbilitiesS2CPacket var1, CallbackInfo var2) {
      Fly var3 = Fly.bAsFly();
      if (var3 != null) {
         var3.jAsVoid();
      }

      Freecam.BAsVoid();
   }
}
