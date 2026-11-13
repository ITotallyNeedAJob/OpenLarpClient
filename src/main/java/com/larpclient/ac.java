package com.larpclient;

import java.util.Objects;
import net.minecraft.item.ItemStack;

public final class ac {
   private ItemStack x;
   private e.Type_c A;
   private Object W;

   public ac() {
   }

   public void FAsVoid() {
      this.x = null;
      this.A = null;
      this.W = null;
   }

   public Object ZAsObject(ItemStack var1, e.Type_c var2) {
      if (this.x == null || !ItemStack.areItemsAndComponentsEqual(this.x, var1) || !Objects.equals(this.A, var2)) {
         this.x = var1.copy();
         this.A = var2;
         this.W = new Object();
      }

      return this.W;
   }
}
