package com.larpclient;

import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.Vec2f;

public final class WidgetQueue {
   public static boolean LAsBoolean(boolean var0, boolean var1, boolean var2, boolean var3, boolean var4, boolean var5, float var6, boolean var7) {
      return var0 && var1 && var2 && var3 && !var4 && !var5 && Float.isFinite(var6) && var6 > 0.9F && var7;
   }

   public static PlayerInput ZAsPlayerInput(PlayerInput var0) {
      return new PlayerInput(false, var0.backward(), var0.left(), var0.right(), var0.jump(), var0.sneak(), false);
   }

   public static Vec2f eAsVec2f(PlayerInput var0) {
      return new Vec2f((float)((var0.left() ? 1 : 0) - (var0.right() ? 1 : 0)), (float)((var0.forward() ? 1 : 0) - (var0.backward() ? 1 : 0))).normalize();
   }

   private WidgetQueue() {
   }

   public static final class Type_d {
      private int e;
      private int P = Integer.MIN_VALUE;
      private boolean H;

      public Type_d() {
      }

      public void cAsVoid(int var1) {
         if (this.e == 0 && !this.H) {
            this.e = Math.clamp((long)var1, 1, 3);
         }
      }

      public boolean uAsBoolean(int var1, boolean var2) {
         if (!var2) {
            this.gAsVoid();
            return false;
         } else {
            if (var1 != this.P) {
               this.P = var1;
               this.H = this.e > 0;
               if (this.H) {
                  this.e--;
               }
            }

            return this.H;
         }
      }

      public boolean IAsBoolean(int var1) {
         return this.H && this.P == var1;
      }

      public boolean GAsBoolean() {
         return this.e > 0 || this.H;
      }

      public void gAsVoid() {
         this.e = 0;
         this.H = false;
         this.P = Integer.MIN_VALUE;
      }
   }

   public static final class Type_s {
      private Object p;
      private Object I;
      private boolean t;
      private boolean f;

      public Type_s() {
      }

      public void SAsVoid(Object var1, Object var2, boolean var3) {
         this.p = var1;
         this.I = var2;
         this.t = var3;
         this.f = false;
      }

      public void ZAsVoid(Object var1, Object var2) {
         if (this.p == var1 && this.I == var2) {
            this.f = true;
         }
      }

      public boolean vAsBoolean(Object var1, Object var2) {
         int var3 = this.t && this.f && this.p == var1 && this.I == var2 ? 1 : 0;
         this.uAsVoid();
         return var3 != 0;
      }

      public void uAsVoid() {
         this.p = this.I = null;
         this.t = this.f = false;
      }
   }
}
