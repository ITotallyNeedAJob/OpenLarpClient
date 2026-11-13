package com.larpclient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public final class xx {
   public static final int v = 512;
   public static final int x = 256;
   public static final double H = 2.5;
   private final HashMap<Integer, xx.Type_s> U = new HashMap<>();
   private final HashMap<Integer, xx.Type_q> G = new HashMap<>();
   private final ArrayList<xx.Type_e> i = new ArrayList<>();
   private final Random r;

   public xx(long var1) {
      this.r = new Random(var1);
   }

   public void aAsVoid() {
      this.U.clear();
      this.G.clear();
      this.i.clear();
   }

   public int qAsInt() {
      return this.i.size();
   }

   public int YAsInt() {
      return this.U.size();
   }

   public void dAsVoid(int var1) {
      this.G.remove(var1);
   }

   public void gAsVoid(int var1, Object var2, float var3, double var4) {
      if (var2 != null && Float.isFinite(var3) && Double.isFinite(var4) && (this.G.size() < 512 || this.G.containsKey(var1))) {
         this.G.put(var1, new xx.Type_q(var2, var4 + 2.5));
         xx.Type_s var6 = this.U.get(var1);
         if ((var6 == null || var6.identity != var2) && this.U.size() < 512) {
            this.U.put(var1, new xx.Type_s(var2, var3, var4));
         }
      }
   }

   public void zAsVoid(int var1, Object var2, float var3, double var4, double var6, double var8, double var10, boolean var12, double var13, double var15) {
      if (var2 != null
         && Float.isFinite(var3)
         && Double.isFinite(var13)
         && Double.isFinite(var4)
         && Double.isFinite(var6)
         && Double.isFinite(var8)
         && Double.isFinite(var10)
         && Double.isFinite(var15)
         && !(var15 <= 0.0)) {
         xx.Type_s var17 = this.U.get(var1);
         if (var17 != null || this.U.size() < 512) {
            this.U.put(var1, new xx.Type_s(var2, var3, var13));
            if (var17 != null && var17.identity == var2) {
               float var18 = var17.value - var3;
               xx.Type_q var19 = this.G.get(var1);
               int var20 = var19 != null && var19.identity == var2 && var13 <= var19.until ? 1 : 0;
               if (var18 > 0.0F && (var12 || var20 != 0)) {
                  if (this.i.size() >= 256) {
                     this.i.removeFirst();
                  }

                  this.i
                     .add(
                        new xx.Type_e(
                           var2,
                           EAsString(var18),
                           var18,
                           var4 + this.r.nextDouble(-0.25, 0.25),
                           var6 + var10 * 0.62 + this.r.nextDouble(-0.05, 0.12),
                           var8 + this.r.nextDouble(-0.25, 0.25),
                           var13,
                           Math.clamp(var15, 0.3, 2.5)
                        )
                     );
               }
            } else {
               this.G.remove(var1);
            }
         }
      }
   }

   public void BAsVoid(double var1, Predicate<Object> var3) {
      this.U.entrySet().removeIf(var3xx -> var1 - var3xx.getValue().seen > 2.5 || var3.test(var3xx.getValue().identity));
      this.G.entrySet().removeIf(var3xx -> var1 > var3xx.getValue().until || var3.test(var3xx.getValue().identity));
      this.i.removeIf(var3xx -> var1 - var3xx.born >= var3xx.lifetime || var3.test(var3xx.owner));
   }

   public List<xx.Type_b> vAsList(double var1) {
      ArrayList var3 = new ArrayList(this.i.size());

      for (xx.Type_e var5 : this.i) {
         double var6 = Math.clamp((var1 - var5.born) / var5.lifetime, 0.0, 1.0);
         if (!(var6 >= 1.0)) {
            var3.add(new xx.Type_b(var5.text, var5.damage, var5.x, var5.y + var6 * 0.55, var5.z, 1.0 - var6, 1.0 + 0.12 * (1.0 - var6)));
         }
      }

      return List.copyOf(var3);
   }

   public static String EAsString(float var0) {
      return BigDecimal.valueOf((double)var0).setScale(1, RoundingMode.HALF_UP).toPlainString();
   }

   public static int UAsInt(float var0) {
      return var0 >= 5.0F ? -48060 : (var0 >= 3.0F ? -22016 : (var0 >= 1.0F ? -171 : -11141291));
   }

   public static record Type_b(String text, float damage, double x, double y, double z, double opacity, double scale) {
   }

   private static record Type_e(Object owner, String text, float damage, double x, double y, double z, double born, double lifetime) {
   }

   private static record Type_q(Object identity, double until) {
   }

   private static record Type_s(Object identity, float value, double seen) {
   }
}
