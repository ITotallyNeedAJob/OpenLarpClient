package com.larpclient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

public final class EmotecraftJsonReader {
   public static final int r = 0;
   public static final int D = 1;
   public static final int q = 2;
   public static final int t = 3;
   public static final int z = 4;
   public static final int L = 5;
   public static final int P = 6;
   public static final int Y = 7;
   public static final int S = 8;
   public static final int C = 9;
   public static final int j = 10;
   public static final int O = EmotecraftJsonReader.Type_r.values().length * 10;
   private static final String[] T = new String[]{"x", "y", "z", "pitch", "yaw", "roll", "bend", "scale", "spread", "hold"};
   private static final float[][] x = new float[][]{
      {0.0F, 0.0F, 0.0F},
      {0.0F, 0.0F, 0.0F},
      {-5.0F, 2.0F, 0.0F},
      {5.0F, 2.0F, 0.0F},
      {-1.9F, 12.0F, 0.1F},
      {1.9F, 12.0F, 0.1F},
      {0.0F, 0.0F, 0.0F},
      {0.0F, 0.0F, 0.0F}
   };
   public final String d;
   public final String Q;
   public final int M;
   public final int y;
   public final int g;
   public final int i;
   public final boolean e;
   private final boolean s;
   private final EmotecraftJsonReader.Type_c[] I = new EmotecraftJsonReader.Type_c[O];
   private final Set<String> K = new TreeSet<>();

   private EmotecraftJsonReader(String var1, String var2, int var3, int var4, int var5, boolean var6, int var7, boolean var8) {
      this.d = var1;
      this.Q = var2;
      this.M = var3;
      this.y = var4;
      this.g = var5;
      this.e = var6;
      this.i = var7;
      this.s = var8;
   }

   public static EmotecraftJsonReader nAsNh(Reader var0, EmotecraftJsonReader.Type_a var1) {
      JsonObject var2 = JsonParser.parseReader(var0).getAsJsonObject();
      if (!var2.has("emote")) {
         throw new IllegalArgumentException("Only Emotecraft JSON emotes are supported");
      } else {
         int var3 = var2.has("version") ? var2.get("version").getAsInt() : 1;
         if (var3 > 3) {
            throw new IllegalArgumentException("Unsupported emote version " + var3);
         } else {
            JsonObject var4 = var2.getAsJsonObject("emote");
            int var5 = var4.has("beginTick") ? var4.get("beginTick").getAsInt() : 0;
            int var6 = var4.get("endTick").getAsInt();
            if (var6 <= 0) {
               throw new IllegalArgumentException("endTick must be positive");
            } else {
               int var7 = var4.has("isLoop") && var4.has("returnTick") && var4.get("isLoop").getAsBoolean() ? 1 : 0;
               int var8 = var7 != 0 ? var4.get("returnTick").getAsInt() : 0;
               int var9 = var4.has("stopTick") ? var4.get("stopTick").getAsInt() : var6;
               if (var1 != null) {
                  var7 = 1;
                  var8 = var1.returnTick();
                  var6 = var1.endTick();
                  var9 = var6 + 1;
               }

               if (var7 == 0 || var8 >= 0 && var8 <= var6) {
                  int var10 = var4.has("degrees") && !var4.get("degrees").getAsBoolean() ? 0 : 1;
                  int var11 = var4.has("easeBeforeKeyframe") && var4.get("easeBeforeKeyframe").getAsBoolean() ? 1 : 0;
                  EmotecraftJsonReader var12 = new EmotecraftJsonReader(
                     iAsString(var2, "name"), iAsString(var2, "author"), var5, var6, var9, var7 != 0, var8, var11 != 0
                  );

                  for (JsonElement var14 : var4.getAsJsonArray("moves")) {
                     JsonObject var15 = var14.getAsJsonObject();
                     int var16 = var15.get("tick").getAsInt();
                     EmotecraftJsonReader.Type_j var17 = EmotecraftJsonReader.Type_j.QAsNh_Type_j(
                        var15.has("easing") ? var15.get("easing").getAsString() : "linear"
                     );
                     if (var17 == null) {
                        var12.K.add("easing " + var15.get("easing").getAsString());
                        var17 = EmotecraftJsonReader.Type_j.LINEAR;
                     }

                     int var18 = var15.has("turn") ? var15.get("turn").getAsInt() : 0;

                     for (Entry var20 : var15.entrySet()) {
                        if (((JsonElement)var20.getValue()).isJsonObject()) {
                           EmotecraftJsonReader.Type_r var21 = PAsNh_Type_r((String)var20.getKey(), var3);
                           if (var21 == null) {
                              var12.K.add((String)var20.getKey());
                           } else {
                              for (Entry var23 : ((JsonElement)var20.getValue()).getAsJsonObject().entrySet()) {
                                 int var24 = Arrays.asList(T).indexOf(var23.getKey());
                                 if (var24 >= 0 && (var24 != 6 || DAsBoolean(var21)) && (var24 < 7 || var21 == EmotecraftJsonReader.Type_r.PROP)) {
                                    float var25 = ((JsonElement)var23.getValue()).getAsFloat();
                                    if (Float.isNaN(var25)) {
                                       throw new IllegalArgumentException("Keyframe value is NaN");
                                    }

                                    int var26 = (var24 < 3 || var24 > 6) && var24 != 8 ? 0 : 1;
                                    if (var26 != 0 && var10 != 0) {
                                       var25 *= (float) (Math.PI / 180.0);
                                    }

                                    float var27 = var26 != 0 && var18 != 0 ? var25 + (float)((Math.PI * 2) * (double)var18) : var25;
                                    var12.vAsNh_Type_c(var21, var24).aAsVoid(var16, var25, var27, var17);
                                 } else {
                                    var12.K.add((String)var20.getKey() + "." + (String)var23.getKey());
                                 }
                              }
                           }
                        }
                     }
                  }

                  return var12;
               } else {
                  throw new IllegalArgumentException("returnTick must be within 0..endTick");
               }
            }
         }
      }
   }

   private static String iAsString(JsonObject var0, String var1) {
      return var0.has(var1) && var0.get(var1).isJsonPrimitive() ? var0.get(var1).getAsString() : "";
   }

   private static EmotecraftJsonReader.Type_r PAsNh_Type_r(String var0, int var1) {
      return switch (var0) {
         case "head" -> EmotecraftJsonReader.Type_r.HEAD;
         case "torso" -> var1 < 3 ? EmotecraftJsonReader.Type_r.BODY : EmotecraftJsonReader.Type_r.TORSO;
         case "body" -> EmotecraftJsonReader.Type_r.BODY;
         case "rightArm" -> EmotecraftJsonReader.Type_r.RIGHT_ARM;
         case "leftArm" -> EmotecraftJsonReader.Type_r.LEFT_ARM;
         case "rightLeg" -> EmotecraftJsonReader.Type_r.RIGHT_LEG;
         case "leftLeg" -> EmotecraftJsonReader.Type_r.LEFT_LEG;
         case "prop" -> EmotecraftJsonReader.Type_r.PROP;
         default -> null;
      };
   }

   public static boolean DAsBoolean(EmotecraftJsonReader.Type_r var0) {
      return var0 == EmotecraftJsonReader.Type_r.RIGHT_ARM
         || var0 == EmotecraftJsonReader.Type_r.LEFT_ARM
         || var0 == EmotecraftJsonReader.Type_r.RIGHT_LEG
         || var0 == EmotecraftJsonReader.Type_r.LEFT_LEG;
   }

   private EmotecraftJsonReader.Type_c vAsNh_Type_c(EmotecraftJsonReader.Type_r var1, int var2) {
      int var3 = var1.ordinal() * 10 + var2;
      if (this.I[var3] == null) {
         this.I[var3] = new EmotecraftJsonReader.Type_c(var2 < 3 ? x[var1.ordinal()][var2] : (var2 == 7 ? 1.0F : 0.0F));
      }

      return this.I[var3];
   }

   public Set<String> EAsSet() {
      return Set.copyOf(this.K);
   }

   public boolean OAsBoolean(EmotecraftJsonReader.Type_r var1, int var2) {
      return this.I[var1.ordinal() * 10 + var2] != null;
   }

   public int UAsInt(int var1) {
      return this.e && var1 > this.y ? this.i + (var1 - this.y - 1) % (this.y - this.i + 1) : var1;
   }

   public boolean GAsBoolean(int var1) {
      return this.e && var1 > this.y;
   }

   public float bAsFloat(int var1, float var2) {
      return !this.e ? 0.0F : ((float)this.UAsInt(var1) + var2 - (float)this.i) / (float)(this.y - this.i + 1);
   }

   public boolean XAsBoolean(int var1) {
      return !this.e && var1 >= this.g;
   }

   public void aAsVoid(int var1, float var2, float[] var3) {
      int var4 = this.UAsInt(var1);
      boolean var5 = this.GAsBoolean(var1);

      for (int var6 = 0; var6 < O; var6++) {
         var3[var6] = this.I[var6] == null ? Float.NaN : this.YAsFloat(this.I[var6], var4, var2, var5);
      }
   }

   private float YAsFloat(EmotecraftJsonReader.Type_c var1, int var2, float var3, boolean var4) {
      List var5 = var1.K;
      int var6 = this.y - this.i + 1;
      EmotecraftJsonReader.Type_g var7 = null;
      EmotecraftJsonReader.Type_g var8 = null;
      int var9 = 0;
      int var10 = 0;

      for (int var11 = var5.size() - 1; var11 >= 0; var11--) {
         EmotecraftJsonReader.Type_g var12 = (EmotecraftJsonReader.Type_g)var5.get(var11);
         if (var12.tick <= var2 && (!this.e || var12.tick <= this.y) && (!var4 || var12.tick >= this.i)) {
            var7 = var12;
            var9 = var12.tick;
            break;
         }
      }

      for (EmotecraftJsonReader.Type_g var15 : (Iterable<EmotecraftJsonReader.Type_g>) (var5)) {
         if (var15.tick > var2 && (!this.e || var15.tick <= this.y)) {
            var8 = var15;
            var10 = var15.tick;
            break;
         }
      }

      if (this.e && var7 == null && var4) {
         var7 = this.xAsNh_Type_g(var5);
         if (var7 != null) {
            var9 = var7.tick - var6;
         }
      }

      if (this.e && var8 == null) {
         var8 = this.FAsNh_Type_g(var5);
         if (var8 != null) {
            var10 = var8.tick + var6;
         }
      }

      if (var7 == null) {
         var7 = new EmotecraftJsonReader.Type_g(this.M, var1.a, var1.a, EmotecraftJsonReader.Type_j.LINEAR);
         var9 = this.M;
      }

      if (var8 == null) {
         return var7.depart;
      } else if (var10 <= var9) {
         return var7.depart;
      } else {
         float var14 = Math.clamp(((float)var2 + var3 - (float)var9) / (float)(var10 - var9), 0.0F, 1.0F);
         float var16 = (this.s ? var8.ease : var7.ease).PAsFloat(var14);
         return var7.depart + (var8.arrive - var7.depart) * var16;
      }
   }

   private EmotecraftJsonReader.Type_g FAsNh_Type_g(List<EmotecraftJsonReader.Type_g> var1) {
      for (EmotecraftJsonReader.Type_g var3 : var1) {
         if (var3.tick >= this.i && var3.tick <= this.y) {
            return var3;
         }
      }

      return null;
   }

   private EmotecraftJsonReader.Type_g xAsNh_Type_g(List<EmotecraftJsonReader.Type_g> var1) {
      for (int var2 = var1.size() - 1; var2 >= 0; var2--) {
         EmotecraftJsonReader.Type_g var3 = var1.get(var2);
         if (var3.tick >= this.i && var3.tick <= this.y) {
            return var3;
         }
      }

      return null;
   }

   public static record Type_a(int returnTick, int endTick) {
   }

   private static final class Type_c {
      final float a;
      final List<EmotecraftJsonReader.Type_g> K = new ArrayList<>();

      Type_c(float var1) {
         this.a = var1;
      }

      void aAsVoid(int var1, float var2, float var3, EmotecraftJsonReader.Type_j var4) {
         int var5 = 0;

         while (var5 < this.K.size() && this.K.get(var5).tick < var1) {
            var5++;
         }

         if (var5 < this.K.size() && this.K.get(var5).tick == var1) {
            this.K.set(var5, new EmotecraftJsonReader.Type_g(var1, this.K.get(var5).arrive, var3, var4));
         } else {
            this.K.add(var5, new EmotecraftJsonReader.Type_g(var1, var2, var3, var4));
         }
      }
   }

   private static record Type_g(int tick, float arrive, float depart, EmotecraftJsonReader.Type_j ease) {
   }

   public static enum Type_j {
      LINEAR,
      CONSTANT,
      EASEINSINE,
      EASEOUTSINE,
      EASEINOUTSINE,
      EASEINQUAD,
      EASEOUTQUAD,
      EASEINOUTQUAD,
      EASEINCUBIC,
      EASEOUTCUBIC,
      EASEINOUTCUBIC,
      EASEINQUART,
      EASEOUTQUART,
      EASEINOUTQUART,
      EASEINQUINT,
      EASEOUTQUINT,
      EASEINOUTQUINT,
      EASEINEXPO,
      EASEOUTEXPO,
      EASEINOUTEXPO,
      EASEINCIRC,
      EASEOUTCIRC,
      EASEINOUTCIRC,
      EASEINBACK,
      EASEOUTBACK,
      EASEINOUTBACK,
      EASEINELASTIC,
      EASEOUTELASTIC,
      EASEINOUTELASTIC,
      EASEINBOUNCE,
      EASEOUTBOUNCE,
      EASEINOUTBOUNCE;

      private Type_j() {
      }

      static EmotecraftJsonReader.Type_j QAsNh_Type_j(String var0) {
         String var1 = var0.toUpperCase(Locale.ROOT).replace("_", "");
         if (var1.equals("STEP")) {
            return CONSTANT;
         } else {
            for (EmotecraftJsonReader.Type_j var5 : values()) {
               if (var5.name().equals(var1)) {
                  return var5;
               }
            }

            return null;
         }
      }

      public float PAsFloat(float var1) {
         double var2 = (double)var1;

         return switch (this) {
            case LINEAR -> (float)var2;
            case CONSTANT -> 0.0F;
            case EASEINSINE -> (float)(1.0 - Math.cos(var2 * Math.PI / 2.0));
            case EASEOUTSINE -> (float)Math.sin(var2 * Math.PI / 2.0);
            case EASEINOUTSINE -> (float)(-(Math.cos(Math.PI * var2) - 1.0) / 2.0);
            case EASEINQUAD -> (float)(var2 * var2);
            case EASEOUTQUAD -> (float)(1.0 - (1.0 - var2) * (1.0 - var2));
            case EASEINOUTQUAD -> var2 < 0.5 ? (float)(2.0 * var2 * var2) : (float)(1.0 - Math.pow(-2.0 * var2 + 2.0, 2.0) / 2.0);
            case EASEINCUBIC -> (float)(var2 * var2 * var2);
            case EASEOUTCUBIC -> (float)(1.0 - Math.pow(1.0 - var2, 3.0));
            case EASEINOUTCUBIC -> var2 < 0.5 ? (float)(4.0 * var2 * var2 * var2) : (float)(1.0 - Math.pow(-2.0 * var2 + 2.0, 3.0) / 2.0);
            case EASEINQUART -> (float)Math.pow(var2, 4.0);
            case EASEOUTQUART -> (float)(1.0 - Math.pow(1.0 - var2, 4.0));
            case EASEINOUTQUART -> var2 < 0.5 ? (float)(8.0 * Math.pow(var2, 4.0)) : (float)(1.0 - Math.pow(-2.0 * var2 + 2.0, 4.0) / 2.0);
            case EASEINQUINT -> (float)Math.pow(var2, 5.0);
            case EASEOUTQUINT -> (float)(1.0 - Math.pow(1.0 - var2, 5.0));
            case EASEINOUTQUINT -> var2 < 0.5 ? (float)(16.0 * Math.pow(var2, 5.0)) : (float)(1.0 - Math.pow(-2.0 * var2 + 2.0, 5.0) / 2.0);
            case EASEINEXPO -> var2 == 0.0 ? 0.0F : (float)Math.pow(2.0, 10.0 * var2 - 10.0);
            case EASEOUTEXPO -> var2 == 1.0 ? 1.0F : (float)(1.0 - Math.pow(2.0, -10.0 * var2));
            case EASEINOUTEXPO -> var2 == 0.0 || var2 == 1.0
            ? (float)var2
            : (var2 < 0.5 ? (float)(Math.pow(2.0, 20.0 * var2 - 10.0) / 2.0) : (float)((2.0 - Math.pow(2.0, -20.0 * var2 + 10.0)) / 2.0));
            case EASEINCIRC -> (float)(1.0 - Math.sqrt(1.0 - var2 * var2));
            case EASEOUTCIRC -> (float)Math.sqrt(1.0 - Math.pow(var2 - 1.0, 2.0));
            case EASEINOUTCIRC -> var2 < 0.5
            ? (float)((1.0 - Math.sqrt(1.0 - Math.pow(2.0 * var2, 2.0))) / 2.0)
            : (float)((Math.sqrt(1.0 - Math.pow(-2.0 * var2 + 2.0, 2.0)) + 1.0) / 2.0);
            case EASEINBACK -> (float)(2.70158 * var2 * var2 * var2 - 1.70158 * var2 * var2);
            case EASEOUTBACK -> (float)(1.0 + 2.70158 * Math.pow(var2 - 1.0, 3.0) + 1.70158 * Math.pow(var2 - 1.0, 2.0));
            case EASEINOUTBACK -> var2 < 0.5
            ? (float)(Math.pow(2.0 * var2, 2.0) * (7.189819 * var2 - 2.5949095) / 2.0)
            : (float)((Math.pow(2.0 * var2 - 2.0, 2.0) * (3.5949095 * (var2 * 2.0 - 2.0) + 2.5949095) + 2.0) / 2.0);
            case EASEINELASTIC -> var2 != 0.0 && var2 != 1.0
            ? (float)(-Math.pow(2.0, 10.0 * var2 - 10.0) * Math.sin((var2 * 10.0 - 10.75) * (Math.PI * 2.0 / 3.0)))
            : (float)var2;
            case EASEOUTELASTIC -> var2 != 0.0 && var2 != 1.0
            ? (float)(Math.pow(2.0, -10.0 * var2) * Math.sin((var2 * 10.0 - 0.75) * (Math.PI * 2.0 / 3.0)) + 1.0)
            : (float)var2;
            case EASEINOUTELASTIC -> var2 == 0.0 || var2 == 1.0
            ? (float)var2
            : (
               var2 < 0.5
                  ? (float)(-(Math.pow(2.0, 20.0 * var2 - 10.0) * Math.sin((20.0 * var2 - 11.125) * (Math.PI * 4.0 / 9.0))) / 2.0)
                  : (float)(Math.pow(2.0, -20.0 * var2 + 10.0) * Math.sin((20.0 * var2 - 11.125) * (Math.PI * 4.0 / 9.0)) / 2.0 + 1.0)
            );
            case EASEINBOUNCE -> (float)(1.0 - fAsDouble(1.0 - var2));
            case EASEOUTBOUNCE -> (float)fAsDouble(var2);
            case EASEINOUTBOUNCE -> var2 < 0.5 ? (float)((1.0 - fAsDouble(1.0 - 2.0 * var2)) / 2.0) : (float)((1.0 + fAsDouble(2.0 * var2 - 1.0)) / 2.0);
         };
      }

      private static double fAsDouble(double var0) {
         if (var0 < 0.36363636363636365) {
            return 7.5625 * var0 * var0;
         } else if (var0 < 0.7272727272727273) {
            double var8;
            return 7.5625 * (var8 = var0 - 0.5454545454545454) * var8 + 0.75;
         } else {
            double var6;
            double var7;
            return var0 < 0.9090909090909091
               ? 7.5625 * (var6 = var0 - 0.8181818181818182) * var6 + 0.9375
               : 7.5625 * (var7 = var0 - 0.9545454545454546) * var7 + 0.984375;
         }
      }
   }

   public static enum Type_r {
      HEAD,
      TORSO,
      RIGHT_ARM,
      LEFT_ARM,
      RIGHT_LEG,
      LEFT_LEG,
      BODY,
      PROP;

      private Type_r() {
      }
   }
}
