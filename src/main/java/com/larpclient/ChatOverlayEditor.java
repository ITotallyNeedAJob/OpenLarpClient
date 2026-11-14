package com.larpclient;

import com.larpclient.module.ViewModel;
import java.util.Locale;
import java.util.function.BooleanSupplier;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Hand;

public final class ChatOverlayEditor {
   private static final long ChatPayParser = 750000000L;
   private final BooleanSupplier A;
   private Hand O;
   private Hand K;
   private li.Type_w E;
   private double Z;
   private double Q;
   private double o;
   private double N;
   private double M;
   private boolean k;
   private long e;

   public ChatOverlayEditor() {
      this(() -> {
         ProfileManager var0 = LarpClient.CAsLarpClient().fAsBo();
         return var0.kAsBoolean(var0.ZAsString());
      });
   }

   public ChatOverlayEditor(BooleanSupplier var1) {
      this.A = var1;
   }

   private static ViewModel MAsViewModel() {
      ViewModel var0 = ViewModel.qAsViewModel();
      return var0 != null && var0.isRuntimeActive() ? var0 : null;
   }

   public boolean SAsBoolean() {
      return this.K != null;
   }

   public Hand BAsHand() {
      return this.O;
   }

   private Hand QAsHand(double var1, double var3) {
      if (this.O != null && GAsBoolean(this.O, var1, var3)) {
         return this.O;
      } else {
         for (Hand var8 : Hand.values()) {
            if (GAsBoolean(var8, var1, var3)) {
               return var8;
            }
         }

         return null;
      }
   }

   private static boolean GAsBoolean(Hand var0, double var1, double var3) {
      li.Type_w var5 = li.YAsLi_Type_w(var0);
      return var5 != null && var5.contains(var1, var3);
   }

   public boolean uAsBoolean(double var1, double var3, int var5) {
      ViewModel var6 = MAsViewModel();
      if (this.SAsBoolean()) {
         return true;
      } else {
         Hand var7 = var6 == null ? null : this.QAsHand(var1, var3);
         if (var7 == null) {
            this.vAsVoid();
            return false;
         } else if (var5 == 0) {
            this.E = li.YAsLi_Type_w(var7);
            this.O = this.K = var7;
            this.Z = var1;
            this.Q = var3;
            this.o = var6.HAsDouble(var7);
            this.N = var6.NAsDouble(var7);
            ClientLog.XAsVoid("ViewModel", "Dragging {} in chat", BAsString(var7));
            return true;
         } else if (var5 == 1) {
            this.O = var7;
            var6.UAsVoid(var7);
            this.EAsVoid();
            this.WAsVoid();
            ClientLog.XAsVoid("ViewModel", "Reset {} size and position from chat", BAsString(var7));
            return true;
         } else {
            return false;
         }
      }
   }

   public boolean OAsBoolean(double var1, double var3, int var5) {
      if (this.SAsBoolean() && var5 == 0) {
         ViewModel var6 = MAsViewModel();
         if (var6 == null) {
            this.OAsVoid();
            return true;
         } else if (this.E == null) {
            return true;
         } else {
            double var7 = (var1 - this.Z) * this.E.unitsPerPixelX() * (double)(this.E.left() ? -1 : 1);
            double var9 = -(var3 - this.Q) * this.E.unitsPerPixelY();
            var6.IAsVoid(this.K, this.o + var7, this.N + var9);
            this.EAsVoid();
            return true;
         }
      } else {
         return false;
      }
   }

   public boolean sAsBoolean(int var1) {
      if (this.SAsBoolean() && var1 == 0) {
         this.OAsVoid();
         return true;
      } else {
         return false;
      }
   }

   public boolean rAsBoolean(double var1, double var3, double var5) {
      ViewModel var7 = MAsViewModel();
      if (var7 != null && this.O != null && GAsBoolean(this.O, var1, var3)) {
         this.M += var5;
         int var8 = (int)this.M;
         if (var8 != 0) {
            this.M -= (double)var8;
            var7.WAsVoid(this.O, (double)var8);
            this.EAsVoid();
         }

         return true;
      } else {
         return false;
      }
   }

   public void vAsVoid() {
      if (this.SAsBoolean()) {
         this.OAsVoid();
      }

      this.O = null;
      this.M = 0.0;
   }

   public void rAsVoid(boolean var1) {
      if (!var1 && this.SAsBoolean()) {
         this.OAsVoid();
      }
   }

   public void NAsVoid() {
      this.vAsVoid();
      this.WAsVoid();
   }

   private void OAsVoid() {
      this.K = null;
      this.E = null;
      this.WAsVoid();
   }

   private void EAsVoid() {
      this.k = true;
      this.e = System.nanoTime();
   }

   private void WAsVoid() {
      if (this.k) {
         if (this.A.getAsBoolean()) {
            this.k = false;
            ClientLog.XAsVoid("ViewModel", "Saved chat hand placement to the active profile");
         } else {
            ClientLog.oAsVoid("ViewModel", "Hand placement save failed; retrying on the next edit or when chat closes");
         }
      }
   }

   public void sAsVoid(DrawContext var1, int var2, int var3, boolean var4) {
      ViewModel var5 = MAsViewModel();
      if (var5 == null) {
         if (this.O != null || this.SAsBoolean()) {
            this.vAsVoid();
         }
      } else {
         if (this.k && !this.SAsBoolean() && System.nanoTime() - this.e > 750000000L) {
            this.WAsVoid();
         }

         Hand var6 = !var4 && !this.SAsBoolean() ? this.QAsHand((double)var2, (double)var3) : null;
         ow.gAsVoid(this.O, var6);

         for (Hand var10 : Hand.values()) {
            li.Type_w var11 = li.YAsLi_Type_w(var10);
            if (var11 != null && (var10 == this.O || var10 == var6)) {
               String var12 = BAsString(var10) + " " + String.format(Locale.ROOT, "%.2fx", var5.RAsDouble(var10));
               int var13 = var11.y() > 16 ? var11.y() - 13 : var11.y() + var11.height() + 3;
               ww.fAsVoid(var1, var12, (double)Math.max(2, var11.x()), (double)var13, 10.0, var10 == this.O ? -1 : -1325400065);
               if (var10 == this.O && !this.SAsBoolean()) {
                  ww.fAsVoid(
                     var1,
                     "Drag to move, scroll to resize, right-click to reset",
                     (double)Math.max(2, var11.x()),
                     var11.y() > 16 ? (double)(var13 - 11) : (double)(var13 + 11),
                     9.0,
                     -1694498817
                  );
               }
            }
         }
      }
   }

   private static String BAsString(Hand var0) {
      return var0 == Hand.OFF_HAND ? "Offhand" : "Main hand";
   }
}
