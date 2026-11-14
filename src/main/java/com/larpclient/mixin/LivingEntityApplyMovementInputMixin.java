package com.larpclient.mixin;

import com.larpclient.KnockbackAdjuster;
import com.larpclient.ParticleEmitter;
import com.larpclient.module.Aura;
import com.larpclient.module.AutoAnchor;
import com.larpclient.module.AutoCrystal;
import com.larpclient.module.AutoTrap;
import com.larpclient.module.AutoWeb;
import com.larpclient.module.GrimSpeed;
import com.larpclient.module.JumpFX;
import com.larpclient.module.Scaffold;
import com.larpclient.module.TargetStrafe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin({LivingEntity.class})
public class LivingEntityApplyMovementInputMixin {
   public LivingEntityApplyMovementInputMixin() {
   }

   @ModifyArg(
      method = {"method_26318(Lnet/minecraft/class_243;F)Lnet/minecraft/class_243;"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_1309;method_5784(Lnet/minecraft/class_1313;Lnet/minecraft/class_243;)V"
      )},
      index = 1
   )
   private Vec3d larpclient$modifyArgHandler(Vec3d var1) {
      return (Object)this instanceof ClientPlayerEntity var2 ? TargetStrafe.qAsVec3d(var2, var1) : var1;
   }

   @Inject(
      method = {"method_6007()V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_1309;method_6070()V"
      )}
   )
   private void larpclient$injectTickMovement(CallbackInfo var1) {
      if ((Object)this instanceof ClientPlayerEntity var2) {
         GrimSpeed.yAsVoid(var2);
      }
   }

   @ModifyVariable(
      method = {"method_6031(F)V"},
      at = {@At("HEAD")},
      argsOnly = true
   )
   private float larpclient$modifyVariableTurnHead(float var1) {
      if ((Object)this != MinecraftClient.getInstance().player) {
         return var1;
      } else {
         Scaffold var2 = Scaffold.FAsScaffold();
         if (var2 != null) {
            var1 = var2.UAsFloat(var1);
         }

         AutoTrap var3 = AutoTrap.oAsAutoTrap();
         if (var3 != null) {
            var1 = var3.KAsFloat(var1);
         }

         AutoWeb var4 = AutoWeb.PAsAutoWeb();
         if (var4 != null) {
            var1 = var4.zAsFloat(var1);
         }

         AutoAnchor var5 = AutoAnchor.SAsAutoAnchor();
         if (var5 != null) {
            var1 = var5.IAsFloat(var1);
         }

         AutoCrystal var6 = AutoCrystal.vAsAutoCrystal();
         return var6 == null ? var1 : var6.DAsFloat(var1);
      }
   }

   @Inject(
      method = {"method_6043()V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectJump(CallbackInfo var1) {
      if ((Object)this == MinecraftClient.getInstance().player) {
         Aura.bAsVoid((ClientPlayerEntity)(Object)this, "jump-before");
         JumpFX var2 = JumpFX.JAsJumpFX();
         if (var2 != null) {
            var2.OAsVoid();
         }
      }
   }

   @Inject(
      method = {"method_6043()V"},
      at = {@At("RETURN")}
   )
   private void larpclient$injectJump2(CallbackInfo var1) {
      if ((Object)this == MinecraftClient.getInstance().player) {
         Aura.bAsVoid((ClientPlayerEntity)(Object)this, "jump-after");
      }
   }

   @Redirect(
      method = {"method_6043()V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_1309;method_36454()F"
      )}
   )
   private float larpclient$redirectJump(LivingEntity var1) {
      return var1 == MinecraftClient.getInstance().player && p5.NAsP5_Type_u() == p5.Type_u.SCAFFOLD ? p5.jAsFloat() : var1.getYaw();
   }

   @ModifyArgs(
      method = {"method_6005(DDD)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_1309;method_18800(DDD)V"
      )}
   )
   private void larpclient$modifyArgsTakeKnockback(Args var1, double var2, double var4, double var6) {
      if ((Object)this == MinecraftClient.getInstance().player) {
         Vec3d var8 = ((LivingEntity)(Object)this).getVelocity();
         Vec3d var9 = new Vec3d((Double)var1.get(0), (Double)var1.get(1), (Double)var1.get(2));
         Vec3d var10 = var8.add(n5.eAsVec3d(var9.subtract(var8), false));
         var1.set(0, var10.x);
         var1.set(1, var10.y);
         var1.set(2, var10.z);
      }
   }
}
