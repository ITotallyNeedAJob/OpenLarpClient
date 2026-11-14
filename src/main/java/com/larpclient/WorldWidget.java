package com.larpclient;

import com.larpclient.module.HUD;
import com.larpclient.module.Module;
import com.larpclient.module.ModuleManager;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public final class WorldWidget {
   public static final int w = 32;
   public static final int u = 12;
   public static final int j = 13;
   public static final int f = -267645934;
   private static final Map<Module, Float> s = new IdentityHashMap<>();
   private static final Map<Identifier, Float> S = new HashMap<>();
   private static Object V;
   private static Object F;
   private static long B;
   private static long b;
   private static final Map<Identifier, WorldWidget.Type_x> Z = new HashMap<>();

   private WorldWidget() {
   }

   public static void KAsVoid() {
      s.clear();
      S.clear();
      Z.clear();
      F = null;
      V = null;
      b = 0L;
      B = 0L;
   }

   private static void tAsVoid() {
      MinecraftClient var0 = MinecraftClient.getInstance();
      if (V != var0.world || F != var0.player) {
         KAsVoid();
         V = var0.world;
         F = var0.player;
      }
   }

   public static int VAsInt(String var0) {
      return WAsInt(var0, 13.0);
   }

   public static int WAsInt(String var0, double var1) {
      return (int)Math.ceil((double)HudSdfAtlas.jAsFloat(var0, var1));
   }

   public static String xAsString(String var0, double var1, double var3) {
      return HudSdfAtlas.bAsString(var0, var1, var3);
   }

   public static int gAsInt(int var0, int var1) {
      return var0 + var1 + (int)Math.ceil(54.0);
   }

   public static int yAsInt(int var0, int var1, int var2) {
      return 26 + (int)Math.ceil(16.799999999999997) + var2 + var0 + 48 + var1;
   }

   public static float oAsFloat(float var0, boolean var1) {
      return vAsFloat(var0, var1, 0.016666666666666666);
   }

   public static float vAsFloat(float var0, boolean var1, double var2) {
      double var4 = 1.0 - Math.pow(0.85, Math.clamp(var2, 0.0, 0.25) * 60.0);
      return (float)((double)var0 + (double)((float)(var1 ? 1 : 0) - var0) * var4);
   }

   private static double YAsDouble(long var0, long var2) {
      return var0 == 0L ? 0.016666666666666666 : (double)Math.max(0L, var2 - var0) / 1.0E9;
   }

   public static String VAsString(int var0, boolean var1) {
      int var2 = Math.max(0, var0 / 20);
      return var1 ? "∞" : String.format(Locale.ROOT, "%d:%02d", var2 / 60, var2 % 60);
   }

   private static WorldWidget.Type_j wAsWw_Type_j(String var0, List<WorldWidget.Type_x> var1, boolean var2) {
      int var3 = Math.max(168, VAsInt(var0) + 28 + 24);
      double var4 = 48.0;

      for (WorldWidget.Type_x var7 : var1) {
         var3 = Math.max(var3, var7.width());
         var4 += (double)((float)(var2 ? 22 : 23) * var7.alpha());
      }

      return new WorldWidget.Type_j(var0, List.copyOf(var1), var3, (int)Math.ceil(var4), var2);
   }

   public static WorldWidget.Type_j lAsWw_Type_j(boolean var0) {
      tAsVoid();
      long var1 = System.nanoTime();
      double var3 = YAsDouble(B, var1);
      if (!var0) {
         B = var1;
      }

      ArrayList var5 = new ArrayList();

      for (Module var7 : LarpClient.CAsLarpClient().mAsF().NAsList()) {
         HUD var8 = HUD.EAsHUD();
         int var9 = var7.getKeyBind() >= 0 && (var7.isEnabled() || var8 != null && var8.keybindDisplay.pAsBoolean("Always")) ? 1 : 0;
         float var10 = var0 ? (float)(var9 != 0 ? 1 : 0) : vAsFloat(s.getOrDefault(var7, 0.0F), var9 != 0, var3);
         if (var10 < 0.05F && var9 == 0) {
            if (!var0) {
               s.remove(var7);
            }
         } else {
            if (!var0) {
               s.put(var7, var10);
            }

            if (!(var10 <= 0.05F)) {
               String var11 = var7.getDisplayName();
               String var12 = var7.getKeyBind() < 0 ? "None" : KeybindNames.eAsString(var7.getKeyBind()).toUpperCase(Locale.ROOT);
               var5.add(new WorldWidget.Type_x(var11, var12, null, gAsInt(VAsInt(var11), VAsInt(var12)), var10));
            }
         }
      }

      if (var0 && var5.isEmpty()) {
         var5.add(new WorldWidget.Type_x("Backtrack", "R", null, gAsInt(VAsInt("Backtrack"), VAsInt("R")), 1.0F));
         var5.add(new WorldWidget.Type_x("Speed", "V", null, gAsInt(VAsInt("Speed"), VAsInt("V")), 1.0F));
      }

      return wAsWw_Type_j("KeyBinds", var5, true);
   }

   public static WorldWidget.Type_j DAsWw_Type_j(boolean var0) {
      tAsVoid();
      long var1 = System.nanoTime();
      double var3 = YAsDouble(b, var1);
      if (!var0) {
         b = var1;
      }

      MinecraftClient var5 = MinecraftClient.getInstance();
      ArrayList var6 = new ArrayList();
      HashSet var7 = new HashSet();
      if (var5.player != null) {
         for (StatusEffectInstance var9 : var5.player.getStatusEffects()) {
            Identifier var10 = Registries.STATUS_EFFECT.getId((StatusEffect)var9.getEffectType().value());
            var7.add(var10);
            float var11 = var0 ? 1.0F : vAsFloat(S.getOrDefault(var10, 0.0F), true, var3);
            if (!var0) {
               S.put(var10, var11);
            }

            String var12 = ((StatusEffect)var9.getEffectType().value()).getName().getString()
               + (var9.getAmplifier() > 0 ? " " + (var9.getAmplifier() + 1) : "");
            String var13 = VAsString(var9.getDuration(), var9.isInfinite());
            Identifier var14 = Identifier.of(var10.getNamespace(), "mob_effect/" + var10.getPath());
            WorldWidget.Type_x var15 = new WorldWidget.Type_x(var12, var13, var14, yAsInt(VAsInt(var12), VAsInt(var13), VAsInt(">")), var11);
            var6.add(var15);
            if (!var0) {
               Z.put(var10, var15);
            }
         }
      }

      if (!var0) {
         Iterator var16 = S.entrySet().iterator();

         while (var16.hasNext()) {
            Entry var18 = (Entry)var16.next();
            if (!var7.contains(var18.getKey())) {
               float var20 = vAsFloat((Float)var18.getValue(), false, var3);
               if (var20 <= 0.05F) {
                  var16.remove();
                  Z.remove(var18.getKey());
               } else {
                  var18.setValue(var20);
                  WorldWidget.Type_x var21 = Z.get(var18.getKey());
                  if (var21 != null) {
                     var6.add(new WorldWidget.Type_x(var21.label(), var21.value(), var21.icon(), var21.width(), var20));
                  }
               }
            }
         }
      }

      if (var0 && var6.isEmpty()) {
         for (String[] var19 : List.of(new String[]{"Speed 2", "0:45", "speed"}, new String[]{"Strength", "0:30", "strength"})) {
            var6.add(
               new WorldWidget.Type_x(var19[0], var19[1], Identifier.ofVanilla("mob_effect/" + var19[2]), yAsInt(VAsInt(var19[0]), VAsInt(var19[1]), VAsInt(">")), 1.0F)
            );
         }
      }

      var6.sort(Comparator.comparingInt(WorldWidget.Type_x::width).reversed());
      return wAsWw_Type_j("Potions", var6, false);
   }

   public static void aAsVoid(DrawContext var0, String var1, double var2, double var4, int var6) {
      fAsVoid(var0, var1, var2, var4, 13.0, var6);
   }

   public static void fAsVoid(DrawContext var0, String var1, double var2, double var4, double var6, int var8) {
      HudSdfAtlas.DAsVoid(var0, var1, var2, var4, var6, var8);
   }

   public static void sAsVoid(DrawContext var0, String var1, int var2, int var3, int var4, int var5) {
      Identifier var6 = Identifier.of("larp-client", "textures/gui/larp/" + var1 + ".png");
      var0.drawTexture(RenderPipelines.GUI_TEXTURED, var6, var2, var3, 0.0F, 0.0F, var4, var4, 16, 16, 16, 16, QueueGroup.iAsInt(var5));
   }

   public static void aAsVoid(DrawContext var0, String var1, int var2, int var3, int var4, int var5) {
      if (!HudSdfAtlas.NAsBoolean(var0, var1, (double)var2, (double)var3, (double)var4, var5)) {
         GuiIcons.aAsVoid(var0, GuiIcons.PAsIdentifier(var1), var2, var3, var4, var5);
      }
   }

   public static List<WidgetLayoutUtil.Type_p> KAsList(WorldWidget.Type_j var0, int var1, int var2, boolean var3) {
      ArrayList var4 = new ArrayList();
      var4.add(new WidgetLayoutUtil.Type_p(var1, var2, var0.width(), var0.height()));
      double var5 = (double)(var2 + 39);

      for (WorldWidget.Type_x var8 : var0.rows()) {
         int var9 = var0.keys() ? 22 : 23;
         var4.add(new WidgetLayoutUtil.Type_p(var1, (int)Math.floor(var5), var0.width(), (int)Math.ceil((double)((float)var9 * var8.alpha()))));
         var5 += (double)((float)var9 * var8.alpha());
      }

      return var4;
   }

   public static void XAsVoid(DrawContext var0, WorldWidget.Type_j var1, int var2, int var3, boolean var4) {
      LayerJob.CAsVoid(var0, var2, var3, var1.width(), var1.height(), var1.title(), var1.keys() ? "keyboard" : "flask-conical", true);
      List var5 = KAsList(var1, var2, var3, var4);

      for (int var6 = 0; var6 < var1.rows().size(); var6++) {
         WorldWidget.Type_x var7 = var1.rows().get(var6);
         WidgetLayoutUtil.Type_p var8 = (WidgetLayoutUtil.Type_p)var5.get(var6 + 1);
         int var9 = var8.y();
         int var10 = var8.height();
         double var11 = (double)var7.alpha();
         var0.enableScissor(var2, var9, var2 + var1.width(), var9 + var10);

         try {
            double var13 = (double)var9 + (double)(var10 - 13) / 2.0;
            int var15 = var2 + 12;
            if (!var1.keys()) {
               var0.drawGuiTexture(RenderPipelines.GUI_TEXTURED, var7.icon(), var15, var9 + (var10 - 15) / 2, 15, 15, ColorUtil.pAsInt(-1, var11));
               var15 += 22;
            }

            int var16 = VAsInt(var7.value());
            int var17 = var16 + 12;
            int var18 = var2 + var1.width() - 12 - var16;
            int var19 = -1250068;
            if (var1.keys()) {
               ModuleManager var20 = LarpClient.CAsLarpClient().mAsF();
               boolean var21 = var20.NAsList().stream().anyMatch(var1x -> var1x.isEnabled() && var1x.getDisplayName().equals(var7.label()));
               var19 = var21 ? UiStyle.w : -1494422292;
               GuiTextureDrawing.AAsVoid(var0, (float)(var18 - 6), (float)(var9 + (var10 - 17) / 2), (float)var17, 17.0F, 4.0F, ColorUtil.pAsInt(-1275068416, var11));
            }

            String var25 = xAsString(var7.label(), (double)(var18 - var15 - 14), 13.0);
            aAsVoid(var0, var25, (double)var15, var13, ColorUtil.pAsInt(var19, var11));
            aAsVoid(var0, var7.value(), (double)var18, var13, ColorUtil.pAsInt(-4671294, var11));
         } finally {
            var0.disableScissor();
         }
      }
   }

   public static record Type_j(String title, List<WorldWidget.Type_x> rows, int width, int height, boolean keys) {
   }

   public static record Type_x(String label, String value, Identifier icon, int width, float alpha) {
   }
}
