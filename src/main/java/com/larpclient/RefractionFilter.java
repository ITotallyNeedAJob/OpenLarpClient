package com.larpclient;

import net.minecraft.entity.attribute.EntityAttributeInstance;

public final class RefractionFilter {
   private EntityAttributeInstance i;
   private double C;
   private double b;

   public RefractionFilter() {
   }

   public void dAsVoid(EntityAttributeInstance var1, double var2) {
      if (var1 != this.i) {
         this.TAsVoid();
         this.i = var1;
         if (var1 != null) {
            this.C = var1.getBaseValue();
         }
      }

      if (this.i != null) {
         if (this.i.getBaseValue() != this.b) {
            this.C = this.i.getBaseValue();
         }

         this.i.setBaseValue(var2);
         this.b = var2;
      }
   }

   public void TAsVoid() {
      if (this.i != null && this.i.getBaseValue() == this.b) {
         this.i.setBaseValue(this.C);
      }

      this.i = null;
   }

   public void jAsVoid(EntityAttributeInstance var1) {
      if (this.i == var1) {
         this.i = null;
      }
   }
}
