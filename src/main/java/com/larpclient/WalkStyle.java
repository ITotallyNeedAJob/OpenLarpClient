package com.larpclient;

import com.larpclient.module.Category;
import com.larpclient.module.Emotes;
import com.larpclient.module.ModeSetting;
import com.larpclient.module.Module;
import com.larpclient.module.NumberSetting;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;

public final class WalkStyle extends Module {
   private static WalkStyle D;
   private final ModeSetting style = this.addSetting(
      new ModeSetting(
         "Style",
         "Local walking choreography",
         "Natural",
         "Natural",
         "Swagger",
         "Robot",
         "Sneaky",
         "Hands in pockets",
         "Ninja run",
         "Zombie",
         "Bouncy",
         "Runway"
      )
   );
   private final NumberSetting strength = this.addSetting(new NumberSetting("Strength", "Animation strength in percent", 100.0, 30.0, 150.0, 5.0));
   private int N = Integer.MIN_VALUE;
   private boolean h;

   public WalkStyle() {
      super("Walk Style", "Walking animations with articulated knees and elbows", Category.RENDER, -1);
      D = this;
   }

   public static WalkStyle xAsWalkStyle() {
      return D;
   }

   @Override
   public void onTick() {
      this.N = this.isInGame() ? getMc().player.getId() : Integer.MIN_VALUE;
      this.h = this.isInGame() && !getMc().options.getPerspective().isFirstPerson();
   }

   @Override
   public void onEnable() {
      this.onTick();
      ClientLog.XAsVoid("WalkStyle", "Local articulated walking activated");
   }

   @Override
   public void onDisable() {
      this.N = Integer.MIN_VALUE;
   }

   public float[] yAsFloatArray(PlayerEntityModel var1, PlayerEntityRenderState var2) {
      return this.isRuntimeActive() && this.h && var2.id == this.N && !var2.hasVehicle && !var2.isGliding && !var2.isSwimming && !Emotes.LAsBoolean(var2)
         ? uAsFloatArray(this.style.getValue(), this.strength.getValue().floatValue(), var1, var2)
         : null;
   }

   public static float[] uAsFloatArray(String var0, float var1, PlayerEntityModel var2, PlayerEntityRenderState var3) {
      float var4 = Math.min(1.0F, var3.limbSwingAmplitude) * var1 / 100.0F;
      if (var4 < 0.02F) {
         return null;
      } else {
         float var5 = var3.limbSwingAnimationProgress * 0.6662F;
         float var6 = (float)Math.sin((double)var5);
         float var7 = (float)Math.cos((double)var5);
         float var8 = Math.max(0.0F, var6);
         float var9 = Math.max(0.0F, -var6);
         float var10 = 0.3F + Math.max(0.0F, -var7) * 0.4F;
         float var11 = 0.3F + Math.max(0.0F, var7) * 0.4F;
         float var12 = var8 * 1.1F;
         float var13 = var9 * 1.1F;
         switch (var0) {
            case "Swagger":
               var2.body.yaw += var6 * 0.28F * var4;
               var2.rightArm.pitch = var7 * 1.25F * var4;
               var2.leftArm.pitch = -var7 * 1.25F * var4;
               var2.rightArm.roll += 0.18F * var4;
               var2.leftArm.roll -= 0.18F * var4;
               var2.head.roll += var6 * 0.08F * var4;
               var10 = 0.45F + Math.max(0.0F, -var7) * 0.4F;
               var11 = 0.45F + Math.max(0.0F, var7) * 0.4F;
               var12 = var8 * 0.9F;
               var13 = var9 * 0.9F;
               break;
            case "Robot":
               float var16 = (float)Math.round(var7 * 2.0F) / 2.0F;
               var2.rightArm.pitch = var16 * 0.8F * var4;
               var2.leftArm.pitch = -var16 * 0.8F * var4;
               var2.rightArm.roll = var2.leftArm.roll = 0.0F;
               var2.rightLeg.pitch = -var16 * 0.7F * var4;
               var2.leftLeg.pitch = var16 * 0.7F * var4;
               var2.head.yaw = var2.head.pitch = 0.0F;
               var11 = 1.57F;
               var10 = 1.57F;
               var12 = Math.max(0.0F, var16) * 0.6F;
               var13 = Math.max(0.0F, -var16) * 0.6F;
               break;
            case "Sneaky":
               var2.rightArm.pitch = (-0.7F + var7 * 0.25F) * var4;
               var2.leftArm.pitch = (-0.7F - var7 * 0.25F) * var4;
               var2.rightArm.roll -= 0.1F * var4;
               var2.leftArm.roll += 0.1F * var4;
               var2.body.pitch += 0.2F * var4;
               var2.head.pitch += 0.15F * var4;
               var2.rightLeg.pitch = var7 * 0.7F * var4;
               var2.leftLeg.pitch = -var7 * 0.7F * var4;
               var11 = 1.5F;
               var10 = 1.5F;
               var12 = 0.7F + var8 * 0.5F;
               var13 = 0.7F + var9 * 0.5F;
               break;
            case "Hands in pockets":
               var2.rightArm.pitch = var2.leftArm.pitch = 0.2F * var4;
               var2.rightArm.roll = 0.05F;
               var2.leftArm.roll = -0.05F;
               var2.rightLeg.pitch *= 0.75F;
               var2.leftLeg.pitch *= 0.75F;
               var2.head.roll += var6 * 0.05F * var4;
               var11 = 0.9F;
               var10 = 0.9F;
               var12 = var8 * 0.6F;
               var13 = var9 * 0.6F;
               break;
            case "Ninja run":
               var2.rightArm.pitch = var2.leftArm.pitch = 1.25F * var4;
               var2.rightArm.roll = 0.2F * var4;
               var2.leftArm.roll = -0.2F * var4;
               var2.body.pitch += 0.35F * var4;
               var2.head.pitch -= 0.2F * var4;
               var2.rightLeg.pitch = var7 * 1.6F * var4;
               var2.leftLeg.pitch = -var7 * 1.6F * var4;
               var11 = 0.1F;
               var10 = 0.1F;
               var12 = var8 * 1.5F;
               var13 = var9 * 1.5F;
               break;
            case "Zombie":
               var2.rightArm.pitch = -1.45F + var6 * 0.06F;
               var2.leftArm.pitch = -1.45F - var6 * 0.06F;
               var2.rightLeg.pitch *= 0.45F;
               var2.leftLeg.pitch *= 0.45F;
               var2.head.roll += var6 * 0.12F * var4;
               var2.body.roll += var6 * 0.05F * var4;
               var11 = 0.15F;
               var10 = 0.15F;
               var12 = var8 * 0.3F;
               var13 = var9 * 0.3F;
               break;
            case "Bouncy":
               var2.rightArm.pitch = var7 * 1.3F * var4;
               var2.leftArm.pitch = -var7 * 1.3F * var4;
               var2.rightLeg.pitch = var7 * 1.5F * var4;
               var2.leftLeg.pitch = -var7 * 1.5F * var4;
               var2.head.pitch = var2.head.pitch + Math.abs(var6) * 0.12F * var4;
               var11 = 0.8F;
               var10 = 0.8F;
               var12 = var8 * 1.6F;
               var13 = var9 * 1.6F;
               break;
            case "Runway":
               var2.body.roll += var6 * 0.1F * var4;
               var2.body.yaw += var6 * 0.12F * var4;
               var2.rightArm.pitch = var7 * 0.45F * var4;
               var2.leftArm.pitch = -var7 * 0.45F * var4;
               var2.rightLeg.yaw = -0.18F * var4;
               var2.leftLeg.yaw = 0.18F * var4;
               var2.head.roll -= var6 * 0.04F * var4;
               var11 = 0.25F;
               var10 = 0.25F;
               var12 = var8 * 0.7F;
               var13 = var9 * 0.7F;
         }

         return new float[]{-var10 * var4, -var11 * var4, var12 * var4, var13 * var4};
      }
   }
}
