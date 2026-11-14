package com.larp.obf;

import com.larpclient.ClientLog;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.StackWalker.Option;
import java.lang.StackWalker.StackFrame;
import java.lang.System.Logger.Level;
import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class MethodBootstrap {
   private static volatile Object[][] f0;
   private static final AtomicBoolean f1 = new AtomicBoolean();
   private static final StackWalker f2 = StackWalker.getInstance(Set.of(Option.RETAIN_CLASS_REFERENCE, Option.SHOW_HIDDEN_FRAMES), 16);

   private MethodBootstrap() {
   }

   static void m0(Lookup var0, boolean var1) {
      m0(var0, var1, null, null);
   }

   private static void m1(Lookup var0, int var1) throws Exception {
      Object[] var2 = m4()[var1];
      m0(var0, false, (String)var2[1], (String)var2[2]);
   }

   private static void m0(Lookup var0, boolean var1, String var2, String var3) {
      if (var0 != null && var0.hasFullPrivilegeAccess() && var0.lookupClass().getClassLoader() == MethodBootstrap.class.getClassLoader()) {
         Class var4 = var0.lookupClass();
         boolean var5 = f2.walk(
            var4xx -> {
               Iterator var5x = var4xx.limit(64L).iterator();
               boolean var6 = false;

               while (var5x.hasNext()) {
                  StackFrame var7 = (StackFrame)var5x.next();
                  Class var8 = var7.getDeclaringClass();
                  if (var8.getClassLoader() == null && var8.getName().startsWith("java.lang.invoke.")) {
                     if (var8.getName().equals("java.lang.invoke.MethodHandleNatives") && var7.getMethodName().startsWith("link")) {
                        var6 = true;
                     }
                  } else if (var6) {
                     if (var8 == var4) {
                        if (var2 == null) {
                           return true;
                        }

                        String var9 = var7.getDescriptor();
                        return var7.getMethodName().equals(var2)
                           && (var9.equals(var3) || ("(L" + var4.getName().replace('.', '/') + ";" + var9.substring(1)).equals(var3));
                     }

                     return var1
                        && var8.isHidden()
                        && var8.getClassLoader() == var4.getClassLoader()
                        && var8.getNestHost() == var4.getNestHost()
                        && var8.getName().startsWith(var4.getName() + "$b");
                  }
               }

               return false;
            }
         );
         if (!var5) {
            throw new SecurityException("Protected decoding requires the owning JVM bootstrap");
         }
      } else {
         throw new SecurityException("Protected bootstrap caller mismatch");
      }
   }

   static boolean m2() {
      return f2.walk(
         var0 -> {
            List var1 = var0.skip(1L).limit(2L).toList();
            return var1.size() == 2
               && ((StackFrame)var1.get(0)).getDeclaringClass() == ConstantBootstrap.class
               && ((StackFrame)var1.get(0)).getMethodName().equals("m3")
               && ((StackFrame)var1.get(1)).getDeclaringClass() == ConstantBootstrap.class
               && ((StackFrame)var1.get(1)).getMethodName().equals("m4");
         }
      );
   }

   private static MethodHandle m3(Lookup var0, MethodType var1, int var2, byte[] var3, byte[] var4) {
      throw new IllegalStateException("Unresolved method body: " + var2);
   }

   // $VF: Could not create synchronized statement, marking monitor enters and exits
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with PetEffectRenderer copy of the class file (if you have the rights to distribute it!)
   private static Object[][] m4() throws Exception {
      Object[][] var0 = f0;
      if (var0 != null) {
         return var0;
      } else {
         synchronized (MethodBootstrap.class) {
            if (f0 != null) {
               // $VF: monitorexit
               return f0;
            } else {
               try (JarFile var2 = ConstantBootstrap.m2()) {
                  JarEntry var3 = var2.getJarEntry("larp/bodies.bin");
                  if (var3 == null || var3.getSize() < 8L || var3.getSize() > 33554432L) {
                     throw new IOException("Protected implementation bundle missing or oversized");
                  }

                  try (DataInputStream var4 = new DataInputStream(var2.getInputStream(var3))) {
                     label122: {
                        if (var4.readInt() != 1279415090) {
                           throw new IOException("Invalid protected implementation bundle");
                        }

                        int var5 = var4.readInt();
                        if (var5 >= 1 && var5 <= 4096) {
                           var0 = new Object[var5][];

                           for (int var6 = 0; var6 < var5; var6++) {
                              String var7 = var4.readUTF();
                              String var8 = var4.readUTF();
                              String var9 = var4.readUTF();
                              if (var7.isEmpty()
                                 || var8.isEmpty()
                                 || var8.startsWith("<")
                                 || var9.isEmpty()
                                 || var7.length() + var8.length() + var9.length() > 4000) {
                                 throw new IOException("Invalid protected implementation identity");
                              }

                              int var10 = var4.readInt();
                              if (var10 < 28 || var10 > 1048576) {
                                 throw new IOException("Invalid protected implementation size");
                              }

                              byte[] var11 = var4.readNBytes(var10);
                              if (var11.length != var10) {
                                 throw new IOException("Truncated protected implementation");
                              }

                              var0[var6] = new Object[]{var7, var8, var9, var11};
                           }

                           if (var4.read() == -1) {
                              break label122;
                           }

                           throw new IOException("Trailing protected implementation data");
                        }

                        throw new IOException("Invalid protected implementation count");
                     }
                  }
               }

               f0 = var0;
               // $VF: monitorexit
               return var0;
            }
         }
      }
   }

   private static byte[] m5(String var0, String var1, String var2, int var3) {
      return String.join("\n", var0, var1, var2, Integer.toString(var3)).getBytes(StandardCharsets.UTF_8);
   }

   public static CallSite m6(Lookup var0, String var1, MethodType var2, int var3) throws Throwable {
      return m6(var0, var1, var2, var3, "");
   }

   public static CallSite m6(Lookup var0, String var1, MethodType var2, int var3, String var4) throws Throwable {
      try {
         if (var0.hasFullPrivilegeAccess() && var0.lookupClass().getClassLoader() == MethodBootstrap.class.getClassLoader()) {
            Object[][] var12 = m4();
            if (var3 >= 0 && var3 < var12.length) {
               Object[] var13 = var12[var3];
               String var7 = var0.lookupClass().getName().replace('.', '/');
               if (var7.equals(var13[0]) && var1.equals(var13[1]) && var2.descriptorString().equals(var13[2])) {
                  ConstantBootstrap.m1();
                  ConstantCallSite var8 = new ConstantCallSite(
                     m3(var0, var2, var3, m5(var7, var1, var2.descriptorString(), var3), (byte[])var13[3]).asFixedArity()
                  );
                  if (!var4.isEmpty()) {
                     if (!var4.equals("$larp$linked$" + var3)) {
                        throw new IOException("Protected linkage field mismatch");
                     }

                     Field var9 = var0.lookupClass().getDeclaredField(var4);
                     if (var9.getType() != boolean.class || !Modifier.isStatic(var9.getModifiers())) {
                        throw new IOException("Protected linkage field type mismatch");
                     }

                     var9.setAccessible(true);
                     var9.setBoolean(null, true);
                  }

                  return var8;
               } else {
                  throw new IOException("Protected implementation identity mismatch");
               }
            } else {
               throw new IOException("Invalid protected implementation index");
            }
         } else {
            throw new IllegalAccessException("Protected implementation caller mismatch");
         }
      } catch (Throwable var111) {
         Throwable var5 = var111;
         if (f1.compareAndSet(false, true)) {
            String var6 = "Protected implementation link failed for "
               + var0.lookupClass().getName()
               + "."
               + var1
               + " at slot "
               + var3
               + "; restart with an intact release archive and sign in again if activation is required";

            try {
               ClientLog.lAsVoid("Auth", var6, var5);
            } catch (Throwable var101) {
               System.getLogger("Larp/Protection").log(Level.WARNING, var6, var111);
            }
         }

         throw var111;
      }
   }

   private static Lookup m7(Lookup var0) throws IllegalAccessException {
      return MethodHandles.classData(var0, "_", Lookup.class);
   }

   public static CallSite m8(Lookup var0, String var1, MethodType var2, int var3, String var4, String var5, String var6) throws ReflectiveOperationException {
      var3 &= 255;
      Lookup var7 = m7(var0);
      ClassLoader var8 = var7.lookupClass().getClassLoader();
      Class var9 = Class.forName(var4.replace('/', '.'), false, var8);
      MethodHandle var10;
      if (var3 >= 178 && var3 <= 181) {
         Class var13 = MethodType.fromMethodDescriptorString("()" + var6, var8).returnType();

         var10 = switch (var3) {
            case 178 -> var7.findStaticGetter(var9, var5, var13);
            case 179 -> var7.findStaticSetter(var9, var5, var13);
            case 180 -> var7.findGetter(var9, var5, var13);
            default -> var7.findSetter(var9, var5, var13);
         };
      } else {
         MethodType var11 = MethodType.fromMethodDescriptorString(var6, var8);

         var10 = switch (var3) {
            case 182, 185 -> var7.findVirtual(var9, var5, var11);
            case 183 -> var7.findSpecial(var9, var5, var11, var7.lookupClass());
            case 184 -> var7.findStatic(var9, var5, var11);
            default -> throw new IllegalArgumentException("Invalid protected member operation");
         };
      }

      return new ConstantCallSite(var10.asFixedArity().asType(var2));
   }

   public static CallSite m9(Lookup var0, String var1, MethodType var2, MethodHandle var3, Object... var4) throws Throwable {
      ArrayList var5 = new ArrayList();
      var5.add(m7(var0));
      var5.add(var1);
      var5.add(var2);
      Collections.addAll(var5, var4);
      return (CallSite)var3.invokeWithArguments(var5);
   }

   public static Object ma(Lookup var0, String var1, Class<?> var2, MethodHandle var3, Object... var4) throws Throwable {
      ArrayList var5 = new ArrayList();
      var5.add(m7(var0));
      var5.add(var1);
      var5.add(var2);
      Collections.addAll(var5, var4);
      return var3.invokeWithArguments(var5);
   }
}
