package com.larpclient;

import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public final class ot {
   private PlayerInput SahurDanceEngine = PlayerInput.DEFAULT;
   private int C;
   private double S = 1.0;

   public ot() {
   }

   public static Vec3d fAsVec3d(float var0, PlayerInput var1, double var2, double var4, boolean var6) {
      double var7 = (double)aAsFloat(var1.forward(), var1.backward());
      double var9 = (double)aAsFloat(var1.left(), var1.right());
      double var11 = (double)aAsFloat(var1.jump(), var1.sneak());
      double var13 = Math.toRadians((double)var0);
      double var15 = var2 * (var6 ? 0.5 : 1.0);
      return new Vec3d(
         (-Math.sin(var13) * var7 + Math.cos(var13) * var9) * var15, var11 * var15 * var4, (Math.cos(var13) * var7 + Math.sin(var13) * var9) * var15
      );
   }

   public void OAsVoid(PlayerInput var1, boolean var2, boolean var3) {
      this.DAsVoid();
      this.d = var3 ? var1 : PlayerInput.DEFAULT;
      if (var2) {
         this.d = tAsPlayerInput(this.d, new PlayerInput(false, false, false, false, false, true, false));
      }

      this.C = 40;
   }

   public void jAsVoid(PlayerInput var1, boolean var2, boolean var3, boolean var4, Vec2f var5, Vec3d var6, float var7, boolean var8) {
      if (!var8) {
         if (this.C <= 0) {
            return;
         }

         this.C--;
      }

      if (var2 && var3) {
         this.d = tAsPlayerInput(this.d, var1);
      }

      if (var4) {
         this.d = tAsPlayerInput(this.d, new PlayerInput(false, false, false, false, false, true, false));
      }

      if (var2) {
         this.d = tAsPlayerInput(
            this.d, new PlayerInput((double)var5.y > 0.02, (double)var5.y < -0.02, (double)var5.x > 0.02, (double)var5.x < -0.02, false, false, false)
         );
         if ((!var8 || !this.d.forward() && !this.d.backward() && !this.d.left() && !this.d.right()) && !(var6.horizontalLengthSquared() < 1.0E-8)) {
            double var9 = Math.toRadians((double)var7);
            double var11 = -var6.x * Math.sin(var9) + var6.z * Math.cos(var9);
            double var13 = -var6.z * Math.sin(var9) - var6.x * Math.cos(var9);
            int var15 = Math.abs(var11) >= Math.abs(var13) ? 1 : 0;
            this.d = tAsPlayerInput(
               this.d,
               new PlayerInput(
                  var15 != 0 && var11 > 0.01, var15 != 0 && var11 < -0.01, var15 == 0 && var13 > 0.01, var15 == 0 && var13 < -0.01, false, false, false
               )
            );
         }
      }
   }

   public PlayerInput pAsPlayerInput(PlayerInput var1, boolean var2) {
      PlayerInput var3 = var2
         ? tAsPlayerInput(this.d, new PlayerInput(var1.forward(), var1.backward(), var1.left(), var1.right(), false, var1.sneak(), var1.sprint()))
         : this.d;
      if (var2) {
         this.d = new PlayerInput(
            this.d.forward() && !var1.backward(),
            this.d.backward() && !var1.forward(),
            this.d.left() && !var1.right(),
            this.d.right() && !var1.left(),
            this.d.jump(),
            this.d.sneak(),
            this.d.sprint()
         );
      }

      return var3;
   }

   public static Vec2f UAsVec2f(PlayerInput var0) {
      return new Vec2f(aAsFloat(var0.left(), var0.right()), aAsFloat(var0.forward(), var0.backward())).normalize();
   }

   public boolean uAsBoolean(double var1, double var3, boolean var5) {
      if (Double.isFinite(var1) && var1 != 0.0) {
         if (var5) {
            this.S = Math.clamp(this.S * (var1 > 0.0 ? 1.2 : 0.8333333333333334), 0.1 / var3, 50.0 / var3);
         }

         return true;
      } else {
         return false;
      }
   }

   public double bAsDouble(double var1, boolean var3) {
      return var3 ? Math.clamp(var1 * this.S, 0.1, 50.0) : var1;
   }

   public void sAsVoid() {
      this.S = 1.0;
   }

   public void CAsVoid() {
      this.d = PlayerInput.DEFAULT;
      this.C = 0;
   }

   public void DAsVoid() {
      this.CAsVoid();
      this.sAsVoid();
   }

   public static ot.Type_s yAsOt_Type_s(Vec3d var0, int var1, int var2, int var3, int var4) {
      int var5 = MathHelper.floor(var0.x) >> 4;
      int var6 = MathHelper.floor(var0.z) >> 4;
      int var7 = Math.max(Math.abs(var5 - var1), Math.abs(var6 - var2));
      int var8 = Math.clamp((long)var3, 2, 32);
      int var9 = Math.max(2, var4);
      int var10 = Math.max(var9, Math.clamp((long)Math.max(var8, var7 / 2 + 3), 2, 32));
      int var11 = var7 > var8 ? var1 + (var5 - var1) / 2 : var5;
      int var12 = var7 > var8 ? var2 + (var6 - var2) / 2 : var6;
      int var13 = var10 - var9;
      return new ot.Type_s(Math.clamp((long)var11, var1 - var13, var1 + var13), Math.clamp((long)var12, var2 - var13, var2 + var13), var10);
   }

   private static float aAsFloat(boolean var0, boolean var1) {
      return (float)((var0 ? 1 : 0) - (var1 ? 1 : 0));
   }

   public static PlayerInput tAsPlayerInput(PlayerInput var0, PlayerInput var1) {
      return new PlayerInput(
         var0.forward() || var1.forward(),
         var0.backward() || var1.backward(),
         var0.left() || var1.left(),
         var0.right() || var1.right(),
         var0.jump() || var1.jump(),
         var0.sneak() || var1.sneak(),
         var0.sprint() || var1.sprint()
      );
   }

   public static record Type_s(int x, int z, int radius) {
   }
}
