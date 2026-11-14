package com.larpclient;

import com.mojang.blaze3d.shaders.ShaderType;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL31C;

public final class SodiumTerrainMaterialBridge {
   static final Identifier B = Identifier.of("larp-client", "shaders/temple/sodium_terrain.vsh");
   static final Identifier J = Identifier.of("larp-client", "shaders/temple/sodium_terrain.fsh");
   static final String ChinaHatCodec = "blocks/block_layer_opaque";
   static final int PetEffectRenderer = 30;
   private static final Pattern MenuPopupProvider = Pattern.compile("\\bvoid\\s+main\\s*\\(\\s*(?:void\\s*)?\\)");
   private static final Pattern P = Pattern.compile("fragColor\\s*=\\s*_linearFog\\(\\s*color\\s*,");
   private static final Pattern W = Pattern.compile("(?m)^\\s*#moj_import\\s*<larp-client:([^>]+)>\\s*$");
   private static final List<String> BloomUtility = List.of(
      "_vert_position",
      "u_RegionOffset",
      "_get_draw_translation",
      "_draw_id",
      "_vert_tex_light_coord",
      "_vert_color",
      "u_LightTex",
      "v_Color",
      "u_ProjectionMatrix",
      "u_ModelViewMatrix"
   );
   private static final List<String> E = List.of("v_Color", "u_FogColor", "u_RenderFog", "fragColor");
   private static final int PriceDetector = 600;
   private static final IdentityHashMap<Object, Map<Object, Object>> S = new IdentityHashMap<>();
   private static final Object AuraModule = new Object();
   private static Method D;
   private static Method w;
   private static String j;
   private static String e;
   private static boolean c;
   private static boolean s;
   private static boolean O;
   private static int r;
   private static int K;
   private static volatile boolean A;
   private static volatile boolean M;

   static boolean uAsBoolean() {
      return !FabricLoader.getInstance().isModLoaded("iris") && uAsString().startsWith("0.8.");
   }

   static String uAsString() {
      return FabricLoader.getInstance().getModContainer("sodium").map(var0 -> var0.getMetadata().getVersion().getFriendlyString()).orElse("");
   }

   static void zAsVoid() {
      SkyChunkCache = true;
   }

   static boolean iAsBoolean() {
      return !SkyChunkCache && r >= 600;
   }

   static boolean qAsBoolean() {
      return O;
   }

   static boolean jAsBoolean() {
      return M;
   }

   static boolean gAsBoolean() {
      return !A && K >= 600;
   }

   static void EAsVoid(boolean var0) {
      if (!SkyChunkCache && ++r == 600) {
         ClientLog.oAsVoid(
            "TerrainMaterials", "Sodium {} drew no terrain through the terrain material hook in {} frames; keeping Sodium output", uAsString(), 600
         );
      }

      if (var0 && !A && ++K == 600) {
         ClientLog.oAsVoid(
            "TerrainMaterials", "Sodium {} built no chunk mesh through the Snow Ground hook in {} frames; Snow Ground keeps Sodium output", uAsString(), 600
         );
      }
   }

   public static Object JAsObject(Object var0, Object var1) {
      zAsVoid();
      if (!O && WetReflectionEffect.iAsBoolean()) {
         try {
            Map var2 = S.computeIfAbsent(var0, var0x -> new HashMap<>());
            Object var3 = var2.get(var1);
            if (var3 == x) {
               return null;
            } else {
               if (var3 == null) {
                  if (TAsBoolean(var1)) {
                     var2.put(var1, x);
                     return null;
                  }

                  var3 = OAsObject(var0, var1);
                  var2.put(var1, var3);
                  ClientLog.GAsVoid("TerrainMaterials", "Wet Ground and Snow Ground draw Sodium {} pass {} through their variant program", uAsString(), var1);
               }

               GL31C.glBindBufferBase(35345, 30, WetReflectionEffect.gAsInt());
               return var3;
            }
         } catch (IOException | RuntimeException | ReflectiveOperationException var41) {
            O = true;
            ClientLog.lAsVoid("TerrainMaterials", "stage=sodium-variant; Wet Ground and Snow Ground keep Sodium output until resource reload", var41);
            WetReflectionEffect.TAsVoid();
            return null;
         }
      } else {
         return null;
      }
   }

   public static void uAsVoid(Object var0) {
      if (!M && TerrainMaterialClassifier.jAsBoolean()) {
         try {
            for (Object var4 : (Object[])SodiumTerrainMaterialBridge.Type_u.N.get(var0)) {
               SodiumTerrainMaterialBridge.Type_u.R
                  .setInt(
                     var4,
                     TerrainMaterialClassifier.OAsInt(
                        SodiumTerrainMaterialBridge.Type_u.R.getInt(var4),
                        SodiumTerrainMaterialBridge.Type_u.Z.getFloat(var4),
                        SodiumTerrainMaterialBridge.Type_u.C.getFloat(var4),
                        SodiumTerrainMaterialBridge.Type_u.Q.getFloat(var4)
                     )
                  );
            }

            if (!A) {
               MAsVoid();
            }
         } catch (RuntimeException | LinkageError | ReflectiveOperationException var5) {
            sAsVoid(var5);
         }
      }
   }

   private static synchronized void MAsVoid() {
      if (!A) {
         A = true;
         ClientLog.GAsVoid("TerrainMaterials", "Snow Ground tags Sodium {} chunk meshes; relief follows block corners without subdivision", uAsString());
      }
   }

   private static synchronized void sAsVoid(Throwable var0) {
      if (!M) {
         M = true;
         ClientLog.lAsVoid("TerrainMaterials", "stage=sodium-snow-mesh; Snow Ground keeps Sodium output until resource reload", var0);
         MinecraftClient var1 = MinecraftClient.getInstance();
         if (var1 != null) {
            var1.execute(WetReflectionEffect::TAsVoid);
         }
      }
   }

   public static String mAsString(Identifier var0, String var1) {
      if (!BoxUtils || var1 == null || !var0.getNamespace().equals("sodium")) {
         return var1;
      } else if (var0.getPath().equals("blocks/block_layer_opaque.vsh")) {
         return EAsString(var1, j, ShaderType.VERTEX);
      } else {
         return var0.getPath().equals("blocks/block_layer_opaque.fsh") ? EAsString(var1, e, ShaderType.FRAGMENT) : var1;
      }
   }

   public static void fAsVoid(Object var0) {
      Map var1 = S.remove(var0);
      if (var1 != null) {
         EAsVoid(var1);
      }
   }

   static void hAsVoid() {
      for (Map var1 : S.values()) {
         EAsVoid(var1);
      }

      S.clear();
      LineSegment = null;
      TextLayout = null;
      M = false;
      O = false;
   }

   public static String EAsString(String var0, String var1, ShaderType var2) {
      for (String var4 : var2 == ShaderType.VERTEX ? BloomUtility : E) {
         if (!var0.contains(var4)) {
            throw new IllegalStateException("Sodium " + var2.getName() + " chunk shader no longer provides " + var4);
         }
      }

      Matcher var8 = MenuPopupProvider.matcher(var0);
      if (!var8.find()) {
         throw new IllegalStateException("Sodium " + var2.getName() + " chunk shader has no main()");
      } else {
         int var9 = var8.start();
         int var5 = var8.end();
         if (var8.find()) {
            throw new IllegalStateException("Sodium " + var2.getName() + " chunk shader has more than one main()");
         } else {
            String var6 = var0.substring(0, var9) + var1 + "\nvoid larp_sodium_main()" + var0.substring(var5);
            if (var2 == ShaderType.VERTEX) {
               return var6;
            } else {
               Matcher var7 = P.matcher(var6);
               if (!var7.find()) {
                  throw new IllegalStateException("Sodium fragment chunk shader no longer fogs color through _linearFog");
               } else {
                  var9 = var7.start();
                  var5 = var7.end();
                  if (var7.find()) {
                     throw new IllegalStateException("Sodium fragment chunk shader fogs color more than once");
                  } else {
                     return var6.substring(0, var9) + "fragColor = _linearFog(larp_terrain(color)," + var6.substring(var5);
                  }
               }
            }
         }
      }
   }

   public static String dAsString(String var0, Function<String, String> var1) {
      Matcher var2 = W.matcher(var0);
      StringBuilder var3 = new StringBuilder();

      while (var2.find()) {
         String var4 = var1.apply(var2.group(1));
         if (var4 == null) {
            throw new IllegalStateException("Missing Larp shader include " + var2.group(1));
         }

         if (W.matcher(var4).find()) {
            throw new IllegalStateException("Nested Larp shader include in " + var2.group(1));
         }

         var2.appendReplacement(var3, Matcher.quoteReplacement(var4));
      }

      var2.appendTail(var3);
      return var3.toString();
   }

   private static Object OAsObject(Object var0, Object var1) throws ReflectiveOperationException, IOException {
      if (TextLayout == null || LineSegment == null) {
         TextLayout = dAsString(gAsString(B), SodiumTerrainMaterialBridge::AAsString);
         LineSegment = dAsString(gAsString(J), SodiumTerrainMaterialBridge::AAsString);
      }

      Method var2 = dAsMethod(var0.getClass(), "createShader", 2);
      BoxUtils = true;

      Object var3;
      try {
         var3 = var2.invoke(var0, "blocks/block_layer_opaque", var1);
      } catch (InvocationTargetException var10) {
         throw var10.getCause() instanceof RuntimeException var5 ? var5 : new IllegalStateException(var10.getCause());
      } finally {
         BoxUtils = false;
      }

      Object var4 = var3.getClass().getMethod("bindUniformBlockOptional", String.class, int.class).invoke(var3, "LarpMaterial", 30);
      if (var4 == null) {
         UAsVoid(var3);
         throw new IllegalStateException("Sodium terrain material variant lost its LarpMaterial block");
      } else {
         return var3;
      }
   }

   private static boolean TAsBoolean(Object var0) throws ReflectiveOperationException {
      if (D == null) {
         D = var0.getClass().getMethod("pass");
      }

      Object var1 = D.invoke(var0);
      if (w == null) {
         w = var1.getClass().getMethod("isTranslucent");
      }

      return (Boolean)w.invoke(var1);
   }

   private static void EAsVoid(Map<Object, Object> var0) {
      for (Object var2 : var0.values()) {
         if (var2 != x) {
            try {
               UAsVoid(var2);
            } catch (RuntimeException | ReflectiveOperationException var4) {
               ClientLog.oAsVoid("TerrainMaterials", "Sodium terrain material variant release failed: {}", var4.toString());
            }
         }
      }

      var0.clear();
   }

   private static void UAsVoid(Object var0) throws ReflectiveOperationException {
      var0.getClass().getMethod("delete").invoke(var0);
   }

   private static Method dAsMethod(Class<?> var0, String var1, int var2) throws NoSuchMethodException {
      for (Class var3 = var0; var3 != null; var3 = var3.getSuperclass()) {
         for (Method var7 : var3.getDeclaredMethods()) {
            if (var7.getName().equals(var1) && var7.getParameterCount() == var2) {
               var7.setAccessible(true);
               return var7;
            }
         }
      }

      throw new NoSuchMethodException(var0.getName() + "." + var1);
   }

   private static String AAsString(String var0) {
      try {
         return gAsString(Identifier.of("larp-client", "shaders/include/" + var0));
      } catch (IOException var2) {
         return null;
      }
   }

   private static String gAsString(Identifier var0) throws IOException {
      String var2;
      try (InputStream var1 = MinecraftClient.getInstance().getResourceManager().open(var0)) {
         var2 = new String(var1.readAllBytes(), StandardCharsets.UTF_8);
      }

      return var2;
   }

   private SodiumTerrainMaterialBridge() {
   }

   private static final class Type_u {
      static final String X = "net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer";
      static final String I = "net.caffeinemc.mods.sodium.client.render.chunk.vertex.format.ChunkVertexEncoder$Vertex";
      static final Field N;
      static final Field Z;
      static final Field C;
      static final Field Q;
      static final Field R;

      private Type_u() {
      }

      private static Field QAsField(Class<?> var0, String var1, Class<?> var2) throws NoSuchFieldException {
         Field var3 = var0.getField(var1);
         if (var3.getType() != var2) {
            throw new NoSuchFieldException(var0.getName() + "." + var1 + " is no longer " + var2);
         } else {
            return var3;
         }
      }

      static {
         try {
            ClassLoader var0 = SodiumTerrainMaterialBridge.class.getClassLoader();
            Class var1 = Class.forName("net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer", false, var0);
            Class var2 = Class.forName("net.caffeinemc.mods.sodium.client.render.chunk.vertex.format.ChunkVertexEncoder$Vertex", false, var0);
            N = var1.getDeclaredField("vertices");
            if (N.getType() != var2.arrayType()) {
               throw new NoSuchFieldException(
                  "net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer.vertices is no longer PetEffectRenderer vertex array"
               );
            } else {
               N.setAccessible(true);
               Z = QAsField(var2, "x", float.class);
               C = QAsField(var2, "y", float.class);
               Q = QAsField(var2, "z", float.class);
               R = QAsField(var2, "color", int.class);
            }
         } catch (ReflectiveOperationException var31) {
            throw new ExceptionInInitializerError(var31);
         }
      }
   }
}
