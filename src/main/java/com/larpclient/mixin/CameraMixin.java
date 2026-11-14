package com.larpclient.mixin;

import com.larpclient.ParticleEmitter;
import com.larpclient.module.Freelook;
import com.larpclient.module.GUIAnimation;
import com.larpclient.module.Module3DUI;
import com.larpclient.module.Removals;
import com.larpclient.module.SmoothCamera;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Camera.class})
public abstract class CameraMixin {
   @Unique
   private float R = 4.0F;
   @Shadow
   private float field_18721;
   @Shadow
   private float field_18722;

   public CameraMixin() {
   }

   @Shadow
   protected abstract void method_19325(float var1, float var2);

   @Shadow
   protected abstract void method_19324(float var1, float var2, float var3);

   @Invoker("method_19318")
   protected abstract float invokeGetMaxZoom(float var1);

   @Shadow
   protected abstract void method_19327(double var1, double var3, double var5);

   @Inject(
      method = {"method_19321(Lnet/minecraft/class_1937;Lnet/minecraft/class_1297;ZZF)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_4184;method_19327(DDD)V",
         shift = Shift.AFTER
      ), @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_4184;method_19322(Lnet/minecraft/class_243;)V",
         shift = Shift.AFTER
      )}
   )
   private void larpclient$injectZZFV(CallbackInfo var1) {
      if (!ParticleEmitter.TAsBoolean()) {
         SmoothCamera var2 = SmoothCamera.XAsSmoothCamera();
         if (var2 != null) {
            Camera var3 = (Camera)(Object)this;
            Vec3d var4 = var2.JAsVec3d(var3, var3.getCameraPos());
            this.method_19327(var4.x, var4.y, var4.z);
         }
      }
   }

   @ModifyArg(
      method = {"method_19321(Lnet/minecraft/class_1937;Lnet/minecraft/class_1297;ZZF)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_4184;method_19318(F)F"
      )},
      index = 0
   )
   private float larpclient$modifyArgZZFV(float var1) {
      this.R = var1;
      GUIAnimation var2 = GUIAnimation.kAsGUIAnimation();
      return var2 != null && var2.xAsBoolean() ? var1 * var2.LAsFloat(true) : var1;
   }

   @Inject(
      method = {"method_19321(Lnet/minecraft/class_1937;Lnet/minecraft/class_1297;ZZF)V"},
      at = {@At("TAIL")}
   )
   private void larpclient$injectZZFV2(CallbackInfo var1) {
      MinecraftClient var2 = MinecraftClient.getInstance();
      GUIAnimation var3 = GUIAnimation.kAsGUIAnimation();
      if (var3 != null && var3.xAsBoolean() && var2.options.getPerspective().isFirstPerson() && var2.player != null && !var2.player.isSleeping()) {
         float var4 = var3.LAsFloat(false);
         if (var4 > 0.001F) {
            this.method_19324(-this.invokeGetMaxZoom(this.R * var4), 0.0F, 0.0F);
         }
      }
   }

   @Inject(
      method = {"method_19321(Lnet/minecraft/class_1937;Lnet/minecraft/class_1297;ZZF)V"},
      at = {@At("TAIL")}
   )
   private void larpclient$injectZZFV(World var1, Entity var2, boolean var3, boolean var4, float var5, CallbackInfo var6) {
      Entity var7 = ((Camera)(Object)this).getFocusedEntity();
      if (ParticleEmitter.oAsBoolean()) {
         Vec3d var17 = ParticleEmitter.dAsVec3d(((Camera)(Object)this).getLastTickProgress());
         this.method_19325(ParticleEmitter.JAsFloat(), ParticleEmitter.mAsFloat());
         this.method_19327(var17.x, var17.y, var17.z);
      } else if (ParticleEmitter.TAsBoolean() && var7 != null) {
         MinecraftClient var16 = MinecraftClient.getInstance();
         float var18 = ParticleEmitter.JAsFloat();
         float var19 = ParticleEmitter.mAsFloat();
         this.method_19325(var18, var19);
         Vec3d var20 = var7.getLerpedPos(var5).add(0.0, (double)var7.getStandingEyeHeight(), 0.0);
         SmoothCamera var21 = SmoothCamera.XAsSmoothCamera();
         if (var21 != null) {
            var20 = var21.JAsVec3d((Camera)(Object)this, var20);
         }

         this.method_19327(var20.x, var20.y, var20.z);
         if (!var16.options.getPerspective().isFirstPerson()) {
            float var22 = 4.0F;
            float var14 = 1.0F;
            if (var7 instanceof LivingEntity var15) {
               var22 = (float)var15.getAttributeValue(EntityAttributes.CAMERA_DISTANCE);
               var14 = var15.getScale();
            }

            this.method_19324(-this.invokeGetMaxZoom(var22 * var14), 0.0F, 0.0F);
         }
      } else if (Freelook.HAsBoolean() && var7 != null) {
         float var8 = Freelook.DAsFloat();
         float var9 = Freelook.LAsFloat();
         float var10 = Freelook.iAsFloat();
         float var11 = ((Camera)(Object)this).getLastTickProgress();
         Vec3d var12 = var7.getLerpedPos(var11);
         double var13 = var12.y + (double)var7.getStandingEyeHeight();
         this.method_19325(var8, var9);
         this.method_19327(var12.x, var13, var12.z);
         this.method_19324(-var10, 0.0F, 0.0F);
      }
   }

   @Inject(
      method = {"method_19321(Lnet/minecraft/class_1937;Lnet/minecraft/class_1297;ZZF)V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectZZFV3(CallbackInfo var1) {
      Module3DUI.xAsVoid();
   }

   @Inject(
      method = {"method_19321(Lnet/minecraft/class_1937;Lnet/minecraft/class_1297;ZZF)V"},
      at = {@At("TAIL")}
   )
   private void larpclient$injectZZFV4(CallbackInfo var1) {
      Module3DUI var2 = Module3DUI.VAsModule3DUI();
      if (var2 != null) {
         Camera var3 = (Camera)(Object)this;
         float var4 = var3.getLastTickProgress();
         Module3DUI.Type_u var5 = var2.GAsModule3DUI_Type_u(
            var3.getCameraPos(), var3.getYaw(), var3.getPitch(), var3.getFocusedEntity(), var4, MathHelper.lerp(var4, this.field_18722, this.field_18721)
         );
         if (var5 != null) {
            this.method_19325(var5.yaw(), var5.pitch());
            this.method_19327(var5.x(), var5.y(), var5.z());
         }
      }
   }

   @Inject(
      method = {"method_19318(F)F"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectClipToSpace(float var1, CallbackInfoReturnable<Float> var2) {
      if (Removals.xAsBoolean("clip")) {
         var2.setReturnValue(var1);
      }
   }
}
