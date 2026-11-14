package com.larpclient;

public final class SkyModeState {
   private SkyModeState.Type_q O;
   private SkyModeState.Type_b a;
   private int H;
   private int U;
   private int g;
   private boolean n;

   public SkyModeState() {
   }

   public boolean iAsBoolean(SkyModeState.Type_q var1) {
      return this.O == var1;
   }

   public void qAsVoid(SkyModeState.Type_q var1, SkyModeState.Type_b var2, int var3) {
      if (this.O != null && (!this.a.current() || this.a.player() != var2.player())) {
         this.ZAsVoid();
      }

      if (var2.allowed() && var3 >= 0) {
         if (this.O != null && this.O != var1) {
            if (var1 != SkyModeState.Type_q.GOLDEN_APPLE) {
               return;
            }

            this.ZAsVoid();
         }

         if (this.O != var1) {
            if (!var2.usingItem() && !var2.manualUse() && var2.usable(var3)) {
               this.O = var1;
               this.a = var2;
               this.H = var2.selected();
               this.U = var3;
               this.g = var2.tick();
               this.n = false;
               var2.select(this.U);
               var2.holdUse(true);
            }
         } else if (!this.a.manualUse() && this.a.selected() == this.U && this.a.usable(this.U)) {
            if (this.a.usingItem()) {
               this.n = true;
            } else if (this.n || this.a.tick() - this.g >= 8) {
               this.ZAsVoid();
               return;
            }

            this.a.holdUse(true);
         } else {
            this.ZAsVoid();
         }
      } else {
         this.SAsVoid(var1);
      }
   }

   public void SAsVoid(SkyModeState.Type_q var1) {
      if (this.O == var1) {
         this.ZAsVoid();
      }
   }

   public void ZAsVoid() {
      SkyModeState.Type_b var1 = this.a;
      int var2 = this.H;
      int var3 = this.U;
      this.O = null;
      this.a = null;
      this.n = false;
      if (var1 != null) {
         try {
            var1.releaseUse();
         } finally {
            if (var1.current() && !var1.manualUse() && var1.selected() == var3) {
               var1.select(var2);
            }
         }
      }
   }

   public interface Type_b {
      Object player();

      int tick();

      boolean current();

      boolean allowed();

      int selected();

      void select(int var1);

      boolean usable(int var1);

      boolean usingItem();

      boolean manualUse();

      void holdUse(boolean var1);

      void releaseUse();
   }

   public static enum Type_q {
      FOOD,
      GOLDEN_APPLE;

      private Type_q() {
      }
   }
}
