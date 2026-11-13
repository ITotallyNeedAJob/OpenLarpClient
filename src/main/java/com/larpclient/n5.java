package com.larpclient;

import com.larpclient.module.AntiKnockback;
import com.larpclient.module.ModuleManager;
import net.minecraft.util.math.Vec3d;

public final class n5 {
   public static Vec3d bAsVec3d(Vec3d var0, double var1, double var3) {
      return new Vec3d(var0.x * var1, var0.y * var3, var0.z * var1);
   }

   public static Vec3d eAsVec3d(Vec3d var0, boolean var1) {
      LarpClient var2 = LarpClient.CAsLarpClient();
      if (var2 != null && var2.mAsF() != null) {
         ModuleManager var3 = var2.mAsF();
         AntiKnockback var4 = var3.EAsModule(AntiKnockback.class);
         return var4 != null && var4.isRuntimeActive() ? bAsVec3d(var0, var4.CAsDouble(), var4.nAsDouble()) : var0;
      } else {
         return var0;
      }
   }

   private n5() {
   }
}
