package com.larpclient;

import com.mojang.blaze3d.textures.GpuTexture;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

public final class xa {
   private static final Set<GpuTexture> J = Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<>()));
   private static final Set<String> G = Arrays.stream(TextDrawing.Type_x.values())
      .filter(var0 -> var0 != TextDrawing.Type_x.HUD)
      .map(var0 -> var0.name().toLowerCase(Locale.ROOT))
      .collect(Collectors.toUnmodifiableSet());

   private xa() {
   }

   public static boolean AAsBoolean(GpuTexture var0, String var1, boolean var2) {
      if (!tAsBoolean(var1, var2)) {
         return false;
      } else {
         J.add(var0);
         return true;
      }
   }

   public static boolean XAsBoolean(GpuTexture var0) {
      return J.contains(var0);
   }

   public static boolean tAsBoolean(String var0, boolean var1) {
      if (!var1 && var0 != null && var0.startsWith("larp-client:")) {
         int var2 = var0.indexOf(47);
         return var2 > 0 && G.contains(var0.substring(12, var2));
      } else {
         return false;
      }
   }
}
