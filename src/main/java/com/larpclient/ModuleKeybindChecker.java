package com.larpclient;

import com.larpclient.module.Module;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil.Key;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public final class ModuleKeybindChecker {
   public static boolean NAsBoolean(String var0) {
      LarpClient var1 = LarpClient.CAsLarpClient();
      if (var1 != null && var1.mAsF() != null) {
         Module var2 = var1.mAsF().TAsModule(var0);
         return var2 != null && var2.isRuntimeActive();
      } else {
         return false;
      }
   }

   public static boolean VAsBoolean() {
      MinecraftClient var0 = MinecraftClient.getInstance();
      return var0.world != null && var0.player != null && var0.player.isAlive() && !ParticleEmitter.oAsBoolean();
   }

   public static boolean hAsBoolean() {
      return VAsBoolean();
   }

   public static boolean pAsBoolean(KeyBinding var0) {
      MinecraftClient var1 = MinecraftClient.getInstance();
      Key var2 = KeyBindingHelper.getBoundKeyOf(var0);
      if (var2.getCode() < 0) {
         return false;
      } else {
         return switch (var2.getCategory()) {
            case KEYSYM -> GLFW.glfwGetKey(var1.getWindow().getHandle(), var2.getCode()) == 1;
            case MOUSE -> GLFW.glfwGetMouseButton(var1.getWindow().getHandle(), var2.getCode()) == 1;
            default -> false;
         };
      }
   }

   public static Vec3d JAsVec3d() {
      MinecraftClient var0 = MinecraftClient.getInstance();
      double var1 = (double)((var0.options.forwardKey.isPressed() ? 1 : 0) - (var0.options.backKey.isPressed() ? 1 : 0));
      double var3 = (double)((var0.options.leftKey.isPressed() ? 1 : 0) - (var0.options.rightKey.isPressed() ? 1 : 0));
      float var5 = QueueManager.hAsBoolean() ? ParticleEmitter.jAsFloat() : var0.player.getYaw();
      double var6 = Math.toRadians((double)var5);
      return new Vec3d(-Math.sin(var6) * var1 + Math.cos(var6) * var3, 0.0, Math.cos(var6) * var1 + Math.sin(var6) * var3).normalize();
   }

   public static Vec3d PAsVec3d(Vec3d var0, Vec3d var1, double var2) {
      Vec3d var4 = var1.subtract(var0);
      return var0.add(var4.length() > var2 ? var4.normalize().multiply(var2) : var4);
   }

   private ModuleKeybindChecker() {
   }
}
