package com.larpclient.mixin;

import com.larpclient.LarpClient;
import com.larpclient.ModuleImportBroker;
import com.larpclient.ParticleKind;
import com.larpclient.QueueManager;
import com.larpclient.module.Aura;
import com.larpclient.module.AutoSprint;
import com.larpclient.module.GrimSpeed;
import com.larpclient.module.Mace;
import com.larpclient.module.TargetStrafe;
import com.larpclient.module.WTap;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec2f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ClientPlayerEntity.class})
public class ClientPlayerEntityTickMovementMixin {
   public ClientPlayerEntityTickMovementMixin() {
   }

   @Inject(
      method = {"method_6007()V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_744;method_3129()V",
         shift = Shift.AFTER
      )}
   )
   private void larpclient$injectTickMovement(CallbackInfo var1) {
      ClientPlayerEntity var2 = (ClientPlayerEntity)(Object)this;
      var2.input.playerInput = AutoSprint.qAsPlayerInput(var2, var2.input.playerInput);
      var2.input.playerInput = Aura.qAsPlayerInput(var2, var2.input.playerInput);
      var2.input.playerInput = TargetStrafe.HAsPlayerInput(var2, var2.input.playerInput);
      var2.input.playerInput = GrimSpeed.xAsPlayerInput(var2, var2.input.playerInput);
      var2.input.playerInput = WTap.JAsPlayerInput(var2, var2.input.playerInput);
      var2.input.playerInput = Mace.TAsPlayerInput(var2, var2.input.playerInput);
      var2.input.playerInput = qm.eAsPlayerInput(var2, var2.input.playerInput);
      if (Mace.uAsBoolean() || Aura.rAsBoolean()) {
         var2.input.playerInput = pk.cAsPlayerInput(var2.input.playerInput);
      }

      Aura.bAsVoid(var2, "input-final");
   }

   @Inject(
      method = {"method_48300()Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectCanStartSprinting(CallbackInfoReturnable<Boolean> var1) {
      if (WTap.MAsBoolean() || Mace.uAsBoolean() || Aura.rAsBoolean()) {
         var1.setReturnValue(false);
      }
   }

   @Inject(
      method = {"method_5773()V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectTick(CallbackInfo var1) {
      if (LarpClient.CAsLarpClient() != null && LarpClient.CAsLarpClient().mAsF() != null) {
         LarpClient.CAsLarpClient().mAsF().rAsVoid();
      }
   }

   @Inject(
      method = {"method_5773()V"},
      at = {@At("RETURN")}
   )
   private void larpclient$injectTick2(CallbackInfo var1) {
      Aura.bAsVoid((ClientPlayerEntity)(Object)this, "tick-end");
   }

   @Inject(
      method = {"method_67270(Lnet/minecraft/class_241;)Lnet/minecraft/class_241;"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void larpclient$injectHandler(Vec2f var1, CallbackInfoReturnable<Vec2f> var2) {
      var2.setReturnValue(qm.hAsVec2f((Vec2f)var2.getReturnValue()));
   }

   @Inject(
      method = {"method_5773()V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_742;method_5773()V"
      )}
   )
   private void larpclient$injectTick3(CallbackInfo var1) {
      b_.wAsVoid();
      b_.aAsVoid();
   }
}
