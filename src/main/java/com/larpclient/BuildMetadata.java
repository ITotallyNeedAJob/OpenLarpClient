package com.larpclient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class BuildMetadata {
   private static final Properties A = new Properties();
   public static final String I;
   public static final String e;
   public static final String O;
   public static final String z;
   public static final String D;
   public static final String U;
   public static final String Z;
   public static final String g;

   private BuildMetadata() {
   }

   static {
      try (InputStream var0 = BuildMetadata.class.getResourceAsStream("/larp-version.properties")) {
         if (var0 == null) {
            throw new IllegalStateException("Missing build metadata");
         }

         A.load(var0);
      } catch (IOException var51) {
         throw new ExceptionInInitializerError(var51);
      }

      I = A.getProperty("larpVersion");
      e = A.getProperty("minecraftVersion");
      O = A.getProperty("product");
      z = A.getProperty("edition");
      D = A.getProperty("loader");
      U = A.getProperty("channel");
      Z = A.getProperty("flavor");
      g = A.getProperty("buildId");
   }
}
