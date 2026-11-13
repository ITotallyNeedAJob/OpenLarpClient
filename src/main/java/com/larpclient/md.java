package com.larpclient;

import net.minecraft.util.math.Vec3d;

public final class md {
   private md.Type_a r;
   private boolean E;
   private boolean V;
   private Vec3d z;

   public md() {
   }

   public void xAsVoid() {
      this.r = null;
      this.E = this.V = false;
      this.z = null;
   }

   public md.Type_u IAsMd_Type_u(md.Type_a var1) {
      md.Type_u var2 = this.MAsMd_Type_u(var1);
      if (var1.eligible && var1.grounded && this.E) {
         this.z = var1.position;
      }

      return var2;
   }

   public md.Type_u MAsMd_Type_u(md.Type_a var1) {
      if (this.r != null
         && this.r.world == var1.world
         && this.r.player == var1.player
         && var1.eligible
         && this.r.eligible
         && !(this.r.position.squaredDistanceTo(var1.position) > 16.0)) {
         md.Type_u var2 = md.Type_u.NONE;
         if (var1.grounded) {
            if (this.E && this.V) {
               var2 = new md.Type_u(md.Type_p.LAND, var1.position);
            }

            this.E = true;
            this.V = false;
            this.z = null;
         } else if (this.E) {
            if (this.z != null && var1.position.y > this.z.y + 0.001) {
               var2 = new md.Type_u(md.Type_p.JUMP, this.z);
            }

            this.V = this.V | this.r.position.squaredDistanceTo(var1.position) > 1.0E-6;
            this.z = null;
         }

         this.r = var1;
         return var2;
      } else {
         this.xAsVoid();
         this.r = var1;
         return md.Type_u.NONE;
      }
   }

   public static boolean vAsBoolean(String var0, md.Type_p var1) {
      return var1 != md.Type_p.NONE && (var0.equals("Both") || var0.equals(var1 == md.Type_p.JUMP ? "Jump" : "Land"));
   }

   public static record Type_a(Object world, Object player, Vec3d position, boolean grounded, boolean eligible) {
   }

   public static enum Type_p {
      NONE,
      JUMP,
      LAND;

      private Type_p() {
      }
   }

   public static record Type_u(md.Type_p event, Vec3d source) {
      public static final md.Type_u NONE = new md.Type_u(md.Type_p.NONE, Vec3d.ZERO);
   }
}
