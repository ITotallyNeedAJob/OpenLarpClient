package com.larpclient;

import java.util.BitSet;

public final class CrosshairDrawingCodec {
   public static final int Z = 15;
   public static final int A = 7;
   private static final int G = 225;
   private static final int J = 57;
   public static final CrosshairDrawingCodec g = new CrosshairDrawingCodec(new BitSet(225));
   public static final CrosshairDrawingCodec O;
   private final BitSet W;

   private CrosshairDrawingCodec(BitSet var1) {
      this.W = var1;
   }

   private static int pAsInt(int var0, int var1) {
      if (var0 >= 0 && var1 >= 0 && var0 < 15 && var1 < 15) {
         return var1 * 15 + var0;
      } else {
         throw new IndexOutOfBoundsException("Cell outside the drawing");
      }
   }

   public boolean jAsBoolean(int var1, int var2) {
      return this.W.get(pAsInt(var1, var2));
   }

   public boolean QAsBoolean() {
      return this.W.isEmpty();
   }

   public int jAsInt() {
      return this.W.cardinality();
   }

   public CrosshairDrawingCodec qAsXp(int var1, int var2, boolean var3) {
      int var4 = pAsInt(var1, var2);
      if (this.W.get(var4) == var3) {
         return this;
      } else {
         BitSet var5 = (BitSet)this.W.clone();
         var5.set(var4, var3);
         return new CrosshairDrawingCodec(var5);
      }
   }

   public String eAsString() {
      StringBuilder var1 = new StringBuilder(57);

      for (int var2 = 0; var2 < 57; var2++) {
         int var3 = 0;

         for (int var4 = 0; var4 < 4; var4++) {
            if (this.W.get(var2 * 4 + var4)) {
               var3 |= 8 >> var4;
            }
         }

         var1.append(Character.forDigit(var3, 16));
      }

      return var1.toString();
   }

   public static CrosshairDrawingCodec dAsXp(String var0) {
      if (var0 != null && var0.length() == 57) {
         BitSet var1 = new BitSet(225);

         for (int var2 = 0; var2 < 57; var2++) {
            int var3 = Character.digit(var0.charAt(var2), 16);
            if (var3 < 0) {
               throw new IllegalArgumentException("Expected hex digits");
            }

            for (int var4 = 0; var4 < 4; var4++) {
               if ((var3 & 8 >> var4) != 0) {
                  if (var2 * 4 + var4 >= 225) {
                     throw new IllegalArgumentException("Cell outside the drawing");
                  }

                  var1.set(var2 * 4 + var4);
               }
            }
         }

         return new CrosshairDrawingCodec(var1);
      } else {
         throw new IllegalArgumentException("Expected 57 hex digits");
      }
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 instanceof CrosshairDrawingCodec var2 && this.W.equals(var2.W)) {
         return true;
      }

      return false;
   }

   @Override
   public int hashCode() {
      return this.W.hashCode();
   }

   static {
      BitSet var0 = new BitSet(225);
      var0.set(pAsInt(7, 7));

      for (int var1 = 2; var1 <= 4; var1++) {
         var0.set(pAsInt(7 - var1, 7));
         var0.set(pAsInt(7 + var1, 7));
         var0.set(pAsInt(7, 7 - var1));
         var0.set(pAsInt(7, 7 + var1));
      }

      O = new CrosshairDrawingCodec(var0);
   }
}
