package com.larp.obf;

import java.lang.invoke.MethodHandles.Lookup;
import java.util.jar.JarFile;

public final class ConstantBootstrap {
   private ConstantBootstrap() {
   }

   private static String m0() {
      return "MCowBQYDK2VwAyEArWjHusXwRacOd8MTDBrSZ7jyqPezfhCqgR/px+YbGH0=";
   }

   public static void m1() {
   }

   static JarFile m2() throws Exception {
      return new JarFile(ReleaseArchiveVerifier.m0(ConstantBootstrap.class).toFile());
   }

   private static long m3(int var0) {
      throw new IllegalStateException("Unresolved constant: " + var0);
   }

   public static Object m4(Lookup var0, String var1, Class<?> var2, int var3, int var4, String var5) {
      if (var0.hasFullPrivilegeAccess() && var0.lookupClass().getName().equals(var5)) {
         MethodBootstrap.m0(var0, true);
         if (var2 != String.class) {
            long var11 = m3(var3);
            if (var2 == int.class) {
               return (int)var11;
            } else if (var2 == long.class) {
               return var11;
            } else if (var2 == float.class) {
               return Float.intBitsToFloat((int)var11);
            } else if (var2 == double.class) {
               return Double.longBitsToDouble(var11);
            } else {
               throw new BootstrapMethodError("Unsupported protected constant type: " + var2);
            }
         } else {
            char[] var6 = new char[var4];

            for (int var7 = 0; var7 < var4; var7 += 4) {
               long var8 = m3(var3++);

               for (int var10 = 0; var10 < 4 && var7 + var10 < var4; var10++) {
                  var6[var7 + var10] = (char)((int)(var8 >>> var10 * 16));
               }
            }

            return new String(var6).intern();
         }
      } else {
         throw new BootstrapMethodError("Protected constant caller mismatch");
      }
   }
}
