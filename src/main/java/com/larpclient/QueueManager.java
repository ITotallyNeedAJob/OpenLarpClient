package com.larpclient;

import com.larpclient.module.Aura;
import com.larpclient.module.TargetStrafe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;

public final class QueueManager {
   public static Vec2f TAsVec2f(Vec2f var0, float var1, float var2) {
      double var3 = Math.toRadians((double)(var1 - var2));
      double var5 = Math.cos(var3);
      double var7 = Math.sin(var3);
      return new Vec2f((float)((double)var0.x * var5 - (double)var0.y * var7), (float)((double)var0.x * var7 + (double)var0.y * var5));
   }

   public static Vec2f hAsVec2f(Vec2f var0) {
      MinecraftClient var1 = MinecraftClient.getInstance();
      return var1.player != null && !TargetStrafe.yAsBoolean() && hAsBoolean() && !KAsBoolean() ? TAsVec2f(var0, ParticleEmitter.jAsFloat(), var1.player.getYaw()) : var0;
   }

   public static boolean hAsBoolean() {
      ParticleEmitter.Type_u var0 = ParticleEmitter.NAsP5_Type_u();
      if (var0 != ParticleEmitter.Type_u.AUTOTRAP
         && var0 != ParticleEmitter.Type_u.AUTOWEB
         && var0 != ParticleEmitter.Type_u.SCAFFOLD
         && var0 != ParticleEmitter.Type_u.AUTOANCHOR
         && var0 != ParticleEmitter.Type_u.MACE
         && var0 != ParticleEmitter.Type_u.AUTOCRYSTAL
         && var0 != ParticleEmitter.Type_u.PEARLCATCH
         && var0 != ParticleEmitter.Type_u.WINDHOP) {
         Aura var1 = Aura.fAsAura();
         return var1 != null && var1.DAsBoolean();
      } else {
         return true;
      }
   }

   public static boolean KAsBoolean() {
      ParticleEmitter.Type_u var0 = ParticleEmitter.NAsP5_Type_u();
      return (
            var0 == ParticleEmitter.Type_u.MACE
               || var0 == ParticleEmitter.Type_u.AUTOTRAP
               || var0 == ParticleEmitter.Type_u.AUTOWEB
               || var0 == ParticleEmitter.Type_u.SCAFFOLD
               || var0 == ParticleEmitter.Type_u.AUTOANCHOR
               || var0 == ParticleEmitter.Type_u.AUTOCRYSTAL
               || var0 == ParticleEmitter.Type_u.PEARLCATCH
               || var0 == ParticleEmitter.Type_u.WINDHOP
         )
         && !TargetStrafe.yAsBoolean();
   }

   public static PlayerInput eAsPlayerInput(ClientPlayerEntity var0, PlayerInput var1) {
      if (var0 != null && KAsBoolean()) {
         float var2 = var1.forward() == var1.backward() ? 0.0F : (var1.forward() ? 1.0F : -1.0F);
         float var3 = var1.left() == var1.right() ? 0.0F : (var1.left() ? 1.0F : -1.0F);
         if (var2 == 0.0F && var3 == 0.0F) {
            return var1;
         } else {
            double var4 = MathHelper.wrapDegrees(Math.toDegrees(Aura.rAsDouble(ParticleEmitter.jAsFloat(), var2, var3)));
            float[] var6 = Aura.sAsFloatArray(var4, var0.getYaw());
            var0.input.movementVector = new Vec2f(var6[1], var6[0]).normalize();
            return new PlayerInput(var6[0] > 0.0F, var6[0] < 0.0F, var6[1] > 0.0F, var6[1] < 0.0F, var1.jump(), var1.sneak(), var1.sprint());
         }
      } else {
         return var1;
      }
   }

   private QueueManager() {
   }
}
