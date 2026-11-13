package com.larpclient.module;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import org.lwjgl.glfw.GLFW;

public class MCFriend extends Module {
   private final Set<String> Q = new HashSet<>();
   private boolean x = false;

   public MCFriend() {
      super("MCFriend", "Add friends with middle click", Category.PLAYER, -1);
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         long var1 = mc.getWindow().getHandle();
         int var3 = GLFW.glfwGetMouseButton(var1, 2) == 1 ? 1 : 0;
         if (var3 != 0
            && !this.x
            && mc.currentScreen == null
            && mc.crosshairTarget instanceof EntityHitResult var4
            && var4.getEntity() instanceof PlayerEntity var7) {
            String var8 = var7.getName().getString();
            if (this.Q.contains(var8)) {
               this.Q.remove(var8);
               mc.player.sendMessage(Text.literal("§8[§d§lLarp§8] §cRemoved friend: §f" + var8), true);
            } else {
               this.Q.add(var8);
               mc.player.sendMessage(Text.literal("§8[§d§lLarp§8] §aAdded friend: §f" + var8), true);
            }
         }

         this.x = var3 != 0;
      }
   }

   public boolean SAsBoolean(String var1) {
      return this.Q.contains(var1);
   }

   public Set<String> DAsSet() {
      return this.Q;
   }
}
