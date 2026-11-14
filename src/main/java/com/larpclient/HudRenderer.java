package com.larpclient;

import com.larpclient.module.BooleanSetting;
import com.larpclient.module.CustomCrosshair;
import com.larpclient.module.HUD;
import com.larpclient.module.IslandLyrics;
import com.larpclient.module.Module;
import com.larpclient.module.ModuleManager;
import com.larpclient.module.Setting;
import com.larpclient.module.StaffList;
import com.larpclient.module.TargetHUD;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Predicate;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

public final class HudRenderer {
   public static final String BUILD_WATERMARK = "Larp Cracked By Cig";
   private static final Map<HudRenderer.Type_m, List<d7.Type_p>> X = new LinkedHashMap<>();
   private static Object M;
   private static Object z;
   private static int d;
   private static int a;
   private static long y;
   private static List<HudRenderer.Type_p> L = List.of();
   private static long u;
   private static boolean R;
   private static final int q = 224;
   private static final int J = 72;
   public static final List<HudRenderer.Type_m> S = List.of(
      new HudRenderer.Type_m("watermark", 170, 22, 0.015, 0.02),
      new HudRenderer.Type_m("info", 178, 27, 0.015, 0.85),
      new HudRenderer.Type_m("modules", 130, 100, 0.98, 0.06),
      new HudRenderer.Type_m("target", 224, 72, 0.55, 0.62),
      new HudRenderer.Type_m("effects", 150, 58, 0.015, 0.7),
      new HudRenderer.Type_m("keybinds", 120, 58, 0.015, 0.35),
      new HudRenderer.Type_m("armor", 84, 22, 0.5, 0.9),
      new HudRenderer.Type_m("notifications", 260, 178, 0.98, 0.08),
      new HudRenderer.Type_m("crosshair", 84, 84, 0.5, 0.5),
      new HudRenderer.Type_m("speed", 172, 72, 0.015, 0.55),
      new HudRenderer.Type_m("cooldowns", 168, 48, 0.78, 0.08),
      new HudRenderer.Type_m("staffs", 168, 48, 0.35, 0.65),
      new HudRenderer.Type_m("inventory", 250, 120, 0.04, 0.48),
      new HudRenderer.Type_m("radar", 84, 84, 0.34, 0.45),
      new HudRenderer.Type_m("keystrokes", 81, 98, 0.985, 0.93),
      new HudRenderer.Type_m("island", 300, 20, 0.5, 0.015)
   );
   private static final w2 TextLayout = new w2(1.0);
   private static final w2 T = new w2(0.0);
   private static final w2 ChinaHatCodec = new w2(1.0);
   private static double W = 1.0;
   private static long U;
   private static final Identifier I = Identifier.ofVanilla("hud/heart/container");
   private static final Identifier GroundSample = Identifier.ofVanilla("hud/heart/full");
   private static LivingEntity N;
   private static final n1 SkyChunkCache = new n1();
   private static float b;
   private static long m;
   private static final GuiWorkspace.Type_z v = new GuiWorkspace.Type_z(1.0, false);
   private static final EquipmentSlot[] BoxUtils = new EquipmentSlot[]{
      EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND
   };
   private static final rb DropletTrail = new rb();
   private static final oj Q = new oj();
   private static List<ItemStack> G;

   public HudRenderer() {
   }

   public static void drawBuildWatermark(DrawContext context) {
      MinecraftClient client = MinecraftClient.getInstance();
      if (client != null && client.textRenderer != null) {
         int textWidth = client.textRenderer.getWidth("Larp Cracked By Cig");
         int AuraModule = Math.max(4, client.getWindow().getScaledWidth() - textWidth - 6);
         int ScreenScale = Math.max(4, client.getWindow().getScaledHeight() - 9 - 5);
         context.fill(AuraModule - 3, ScreenScale - 2, AuraModule + textWidth + 3, ScreenScale + 9 + 2, -1441787883);
         context.drawTextWithShadow(client.textRenderer, "Larp Cracked By Cig", x, y, -1);
      }
   }

   public static boolean TAsBoolean() {
      MinecraftClient var0 = MinecraftClient.getInstance();
      return var0.player != null
         && var0.world != null
         && var0.isWindowFocused()
         && var0.currentScreen != null
         && var0.currentScreen.getClass() == ChatScreen.class;
   }

   public static void QAsVoid() {
      L = List.of();
      X.clear();
      M = null;
      z = null;
      ww.KAsVoid();
   }

   public static void wAsVoid(DrawContext var0) {
      if (M != var0) {
         X.clear();
         M = var0;
         MinecraftClient var1 = MinecraftClient.getInstance();
         z = var1.world;
         SahurDanceEngine = var0.getScaledWindowWidth();
         PetEffectRenderer = var0.getScaledWindowHeight();
         ScreenScale = GuiWorkspace.m;
      }
   }

   public static void xAsVoid(HudRenderer.Type_m var0, d7.Type_p var1) {
      if (var1.width() > 0 && var1.height() > 0) {
         X.computeIfAbsent(var0, var0x -> new ArrayList<>()).add(var1);
      }
   }

   public static List<HudRenderer.Type_p> pAsList() {
      MinecraftClient var0 = MinecraftClient.getInstance();
      if (z == var0.world
         && ScreenScale == GuiWorkspace.m
         && var0.player != null
         && SahurDanceEngine == var0.getWindow().getScaledWidth()
         && PetEffectRenderer == var0.getWindow().getScaledHeight()
         && !var0.options.hudHidden) {
         ArrayList var1 = new ArrayList();
         X.forEach((var1xx, var2) -> {
            if (HAsBoolean(var1xx)) {
               for (d7.Type_p var4 : var2) {
                  var1.add(new HudRenderer.Type_p(var1xx, var4));
               }
            }
         });
         return List.copyOf(var1);
      } else {
         return List.of();
      }
   }

   public static Optional<d7.Type_p> qAsOptional(List<HudRenderer.Type_p> var0, HudRenderer.Type_m var1) {
      return var0.stream()
         .filter(var1xx -> var1xx.element() == var1)
         .map(HudRenderer.Type_p::bounds)
         .reduce(
            (var0x, var1x) -> new d7.Type_p(
                  Math.min(var0x.x(), var1x.x()),
                  Math.min(var0x.y(), var1x.y()),
                  Math.max(var0x.x() + var0x.width(), var1x.x() + var1x.width()) - Math.min(var0x.x(), var1x.x()),
                  Math.max(var0x.y() + var0x.height(), var1x.y() + var1x.height()) - Math.min(var0x.y(), var1x.y())
               )
         );
   }

   public static List<Setting<?>> HAsList(HudRenderer.Type_m var0) {
      if (var0.a.equals("crosshair")) {
         ModuleManager var5 = LarpClient.CAsLarpClient().mAsF();
         Module var7 = var5.TAsModule("CustomCrosshair");
         return var7 == null ? List.of() : var7.getSettings();
      } else {
         HUD var1 = HUD.EAsHUD();
         if (var0.a.equals("island")) {
            IslandLyrics var6 = IslandLyrics.zAsIslandLyrics();
            return var6 == null ? List.of() : var6.getSettings();
         } else if (!var0.a.equals("target") && !var0.a.equals("staffs")) {
            return var1 == null ? List.of() : var1.LAsList(var0.a);
         } else {
            ModuleManager var2 = LarpClient.CAsLarpClient().mAsF();
            Module var3 = (Module)(var0.a.equals("target") ? var2.EAsModule(TargetHUD.class) : var2.EAsModule(StaffList.class));
            ArrayList var4 = new ArrayList();
            if (var3 != null) {
               var4.addAll(var3.getSettings());
            }

            if (var1 != null) {
               var4.addAll(var1.LAsList(var0.a));
            }

            return List.copyOf(var4);
         }
      }
   }

   public static boolean XAsBoolean(List<ItemStack> var0) {
      return var0.stream().anyMatch(var0x -> !var0x.isEmpty());
   }

   public static String hAsString(HudRenderer.Type_m var0) {
      if (var0.EAsBoolean()) {
         return "Hidden";
      } else if (!HAsBoolean(var0)) {
         return "Disabled";
      } else {
         MinecraftClient var1 = MinecraftClient.getInstance();
         if (var1.player == null) {
            return "Waiting for world";
         } else {
            String var3 = var0.a;

            int var2 = switch (var3) {
               case "notifications" -> NotificationHud.GAsW8().XAsBoolean() ? 1 : 0;
               case "target" -> SharedRenderEffects.HAsLivingEntity() != null || N != null && T.yAsDouble() > 0.01 ? 1 : 0;
               case "effects" -> !var1.player.getStatusEffects().isEmpty() ? 1 : 0;
               case "keybinds" -> 1;
               case "modules" -> LarpClient.CAsLarpClient().mAsF().NAsList().stream().anyMatch(var0x -> var0x.isOperational()) ? 1 : 0;
               case "armor" -> {
                  HUD var5 = HUD.EAsHUD();
                  yield var5 != null
                        && Arrays.stream(c)
                           .filter(var1x -> var5.armorHeldItems.getValue() || var1x != EquipmentSlot.MAINHAND && var1x != EquipmentSlot.OFFHAND)
                           .anyMatch(var1xx -> !var1.player.getEquippedStack(var1xx).isEmpty())
                     ? 1
                     : 0;
               }
               case "cooldowns", "staffs" -> lj.AAsBoolean(var0.a, lj.cAsLj_Type_y(var0.a, false)) ? 1 : 0;
               case "crosshair" -> var1.options.getPerspective().isFirstPerson() ? 1 : 0;
               case "island" -> IslandLyrics.tAsBoolean() ? 1 : 0;
               default -> 1;
            };
            return var2 != 0 ? "Visible" : "Waiting for content";
         }
      }
   }

   public static HudRenderer.Type_m QAsLp_Type_m(String var0) {
      return S.stream().filter(var1 -> var1.a.equals(var0)).findFirst().orElseThrow();
   }

   public static String XAsString(String var0) {
      return switch (var0) {
         case "watermark" -> "Watermark / server / FPS";
         case "speed" -> "Speed graph";
         case "info" -> "Coordinates";
         case "modules" -> "Active modules";
         case "target" -> "TargetHUD";
         case "effects" -> "Potions";
         case "keybinds" -> "Keybinds";
         case "armor" -> "Armor";
         case "notifications" -> "Notifications";
         case "crosshair" -> "Custom crosshair";
         case "cooldowns" -> "Cooldowns";
         case "staffs" -> "Staff List";
         case "inventory" -> "Inventory";
         case "radar" -> "Radar";
         case "keystrokes" -> "Keystrokes";
         case "island" -> "Island Lyrics";
         default -> var0;
      };
   }

   public static boolean HAsBoolean(HudRenderer.Type_m var0) {
      if (var0.EAsBoolean()) {
         return false;
      } else {
         HUD var1 = HUD.EAsHUD();
         String var2 = var0.a;

         return switch (var2) {
            case "crosshair" -> CustomCrosshair.qAsBoolean();
            case "notifications" -> true;
            case "target" -> {
               TargetHUD var4 = LarpClient.CAsLarpClient().mAsF().EAsModule(TargetHUD.class);
               yield var4 != null && var4.isOperational();
            }
            case "watermark" -> var1 != null && var1.RAsBoolean();
            case "info" -> var1 != null && var1.zAsBoolean();
            case "modules" -> var1 != null && var1.UAsBoolean();
            case "armor" -> var1 != null && var1.BAsBoolean();
            case "keybinds" -> var1 != null && var1.cAsBoolean();
            case "effects" -> var1 != null && var1.rAsBoolean();
            case "speed" -> var1 != null && var1.VAsBoolean();
            case "staffs" -> StaffList.yAsBoolean();
            case "island" -> IslandLyrics.cAsBoolean();
            case "cooldowns", "inventory", "radar", "keystrokes" -> var1 != null && var1.fAsBoolean(var0.a);
            default -> var1 != null && var1.isOperational();
         };
      }
   }

   public static void DAsVoid() {
      dAsVoid(var0 -> true);
   }

   public static void dAsVoid(Predicate<HudRenderer.Type_m> var0) {
      for (HudRenderer.Type_m var2 : S) {
         if (var0.test(var2)) {
            GuiWorkspace.B.remove(var2.a);
            GuiWorkspace.Type_z var3 = GuiWorkspace.v.get(var2.a);
            if (var3 != null) {
               GuiWorkspace.v.put(var2.a, new GuiWorkspace.Type_z(var2.lAsDouble(), var3.hidden()));
            }
         }
      }
   }

   public static void IAsVoid(HudRenderer.Type_m var0, boolean var1) {
      mAsVoid(var0, var1, var0x -> LarpClient.CAsLarpClient().mAsF().TAsModule(var0x));
   }

   public static void mAsVoid(HudRenderer.Type_m var0, boolean var1, Function<String, Module> var2) {
      var0.xAsVoid(!var1);
      if (var1 && !var0.a.equals("notifications")) {
         Module var3 = var2.apply(
            var0.a.equals("crosshair")
               ? "CustomCrosshair"
               : (var0.a.equals("target") ? "TargetHUD" : (var0.a.equals("staffs") ? "Staff List" : (var0.a.equals("island") ? "Island Lyrics" : "HUD")))
         );
         if (var3 != null) {
            String var5 = var0.a;

            String var4 = switch (var5) {
               case "watermark" -> "Watermark";
               case "info" -> "Info Panel";
               case "modules" -> "Module List";
               case "armor" -> "Armor";
               case "keybinds" -> "Keybinds";
               case "effects" -> "Potions";
               case "speed" -> "Speed";
               case "cooldowns" -> "Cooldowns";
               case "inventory" -> "Inventory";
               case "radar" -> "Radar";
               case "keystrokes" -> "Keystrokes";
               default -> "";
            };

            for (Setting var9 : var3.getSettings()) {
               if (var9.getName().equals(var4) && var9 instanceof BooleanSetting var7) {
                  var7.setValue(Boolean.valueOf(true));
               }
            }

            var3.setEnabled(true);
         }
      }
   }

   public void lAsVoid() {
      HudElementRegistry.addLast(
         Identifier.of("larp-client", "hudoverlay"),
         (var0, var1) -> {
            MinecraftClient var2 = MinecraftClient.getInstance();
            wAsVoid(var0);
            if (var2.player != null && !var2.options.hudHidden && !(var2.currentScreen instanceof ClickGuiScreen)) {
               drawBuildWatermark(var0);
            }

            if (var2.player != null
               && !var2.options.hudHidden
               && !ModuleSearchPanel.lAsBoolean(var2.currentScreen)
               && !(var2.currentScreen instanceof HudEditorScreen)) {
               wAsVoid(var0, false);
            }
         }
      );
      ClientLog.GAsVoid("HUD", "Registered {} Larp-styled movable elements; native fonts, saved placement and chat controls retained", S.size());
   }

   public static void CAsVoid(MinecraftClient var0) {
      HUD var1 = HUD.EAsHUD();
      if (var0.world == null || var0.player == null || var1 == null || !var1.isOperational()) {
         DropletTrail.fAsVoid();
      } else if (!var0.isPaused() && var0.world.getTickManager().shouldTick()) {
         DropletTrail.cAsVoid(var0.world, var0.player, var0.player.getX(), var0.player.getZ(), (double)var0.world.getTickManager().getTickRate());
      } else {
         DropletTrail.SAsVoid();
      }
   }

   private static List<ItemStack> xAsList() {
      if (G == null) {
         G = List.of(
            new ItemStack(Items.DIAMOND_HELMET),
            new ItemStack(Items.DIAMOND_CHESTPLATE),
            new ItemStack(Items.DIAMOND_LEGGINGS),
            new ItemStack(Items.DIAMOND_BOOTS)
         );
      }

      return G;
   }

   public static void wAsVoid(DrawContext var0, boolean var1) {
      fAsVoid(var0, var1, null, false);
   }

   public static void tAsVoid(DrawContext var0, HudRenderer.Type_m var1, boolean var2) {
      fAsVoid(var0, true, var1, var2);
   }

   public static List<HudRenderer.Type_p> NAsList() {
      return CropHelper == GuiWorkspace.m ? L : List.of();
   }

   public static void CAsVoid() {
      L = List.of();
   }

   public static void MAsVoid(DrawContext var0, HudRenderer.Type_m var1, boolean var2, boolean var3) {
      if (var3) {
         wAsVoid(var0);
         wAsVoid(var0, false);
         L = pAsList();
         CropHelper = GuiWorkspace.m;
      } else {
         tAsVoid(var0, var1, var2);
      }
   }

   private static void fAsVoid(DrawContext var0, boolean var1, HudRenderer.Type_m var2, boolean var3) {
      MinecraftClient var4 = MinecraftClient.getInstance();
      if (var4.player != null || var1) {
         ArrayList var5 = var1 ? new ArrayList() : null;
         if (var1) {
            L = List.of();
         }

         long var6 = op.RAsLong();
         HUD var8 = HUD.EAsHUD();
         int var9 = var4.getWindow().getScaledWidth();
         int var10 = var4.getWindow().getScaledHeight();
         int var11 = !var1 && TAsBoolean() ? 1 : 0;
         if (!var1 && var11 != (R ? 1 : 0)) {
            R = var11 != 0;
            ClientLog.XAsVoid("HUD", var11 != 0 ? "Chat open; enabled overlays without content show samples" : "Chat closed; overlay samples removed");
         }

         List<oj.Type_x> var12 = Q.KAsList(
            nk.sAsList(LarpClient.CAsLarpClient().mAsF().NAsList(), LarpClient.CAsLarpClient().fAsBo().yAsN7()),
            var4.textRenderer,
            TextDrawing.lAsInt(),
            BloomQuality.J,
            ww::VAsInt
         );
         if ((var1 || var11 != 0) && var12.isEmpty()) {
            var12 = List.of(new oj.Type_x("Sprint", ww.VAsInt("Sprint")), new oj.Type_x("Fullbright", ww.VAsInt("Fullbright")));
         }

         HudRenderer.Type_m var13 = QAsLp_Type_m("modules");
         double var14 = var13.BAsDouble();
         int var16 = Math.max(1, (int)((double)(var10 - 24) / var14 / 17.0));
         int var17 = Math.max(1, (var12.size() + var16 - 1) / var16);
         int var18 = Math.max(20, var12.stream().mapToInt(var0x -> MenuLogoLayout.xAsInt(var0x.width())).max().orElse(0));
         var13.v = var17 * var18 + (var17 - 1) * 8;
         var13.y = Math.max(20, Math.min(var16, var12.size()) * 17);
         var13.d = Math.max(0.1, Math.min(2.5, Math.min(((double)var9 - 16.0) / (double)var13.v, ((double)var10 - 16.0) / (double)var13.y)));
         ArrayList<ItemStack> var19 = new ArrayList<>();
         if (var4.player != null && var8 != null) {
            for (EquipmentSlot var23 : c) {
               if (var23 != EquipmentSlot.MAINHAND && var23 != EquipmentSlot.OFFHAND || var8.armorHeldItems.getValue()) {
                  ItemStack var24 = var4.player.getEquippedStack(var23);
                  if (!var24.isEmpty() || var8.armorEmptySlots.getValue()) {
                     var19.add(var24);
                  }
               }
            }

            if (var8.armorReverse.getValue()) {
               Collections.reverse(var19);
            }
         }

         if ((var1 || var11 != 0) && var19.stream().allMatch(ItemStack::isEmpty)) {
            var19.clear();
            var19.addAll(xAsList());
            if (var8 != null && var8.armorReverse.getValue()) {
               Collections.reverse(var19);
            }
         }

         int var58 = var8 != null && var8.armorDurability.pAsBoolean("Remaining") ? 46 : 26;
         int var59 = var8 != null && !var8.armorDurability.pAsBoolean("Off") ? 37 : 27;
         int var60 = var8 == null ? 5 : var8.armorSpacing.getValue().intValue();
         int var61 = var8 != null && var8.armorLayout.pAsBoolean("Vertical") ? 1 : 0;
         if (var61 != 0 && var8 != null && !var8.armorDurability.pAsBoolean("Off")) {
            var58 += 38;
            var59 = 26;
         }

         int var62 = Math.max(1, var19.size());
         QAsLp_Type_m("armor").v = 6 + (var61 != 0 ? var58 : var62 * (var58 + var60) - var60);
         QAsLp_Type_m("armor").y = 6 + (var61 != 0 ? var62 * (var59 + var60) - var60 : var59);
         String var25 = var4.getCurrentFps() + " fps";
         String var26 = var4.player == null ? "Player" : ml.cAsString(var4.player.getUuid(), var4.player.getName().getString());
         var26 = ww.xAsString(var26, 130.0, 13.0);
         String var27 = !var4.isIntegratedServerRunning() && var4.getCurrentServerEntry() != null ? var4.getCurrentServerEntry().address : "singleplayer";
         var27 = ww.xAsString(var27, 154.0, 13.0);
         String var28 = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
         QAsLp_Type_m("watermark").v = MenuLogoLayout.QAsInt(var26, var27, var25, var28);
         QAsLp_Type_m("watermark").y = 42;
         String var29 = var4.player == null
            ? "128.0, 64.0, -32.0"
            : String.format(Locale.ROOT, "%.1f, %.1f, %.1f", var4.player.getX(), var4.player.getY(), var4.player.getZ());
         QAsLp_Type_m("info").v = MenuLogoLayout.OAsInt(var29);
         int var30 = var11 != 0 && SharedRenderEffects.HAsLivingEntity() == null && (N == null || T.yAsDouble() < 0.01) ? 1 : 0;
         HashMap var31 = new HashMap();

         for (String var33 : List.of("cooldowns", "staffs", "inventory", "radar")) {
            lj.Type_y var34 = lj.cAsLj_Type_y(var33, var1);
            var31.put(var33, var34);
            QAsLp_Type_m(var33).v = var34.width();
            QAsLp_Type_m(var33).y = var34.height();
         }

         if (var8 != null) {
            QAsLp_Type_m("keystrokes").v = KeystrokesHud.uAsInt(var8);
            QAsLp_Type_m("keystrokes").y = KeystrokesHud.QAsInt(var8);
         }

         ww.Type_j var65 = ww.lAsWw_Type_j(var1);
         ww.Type_j var66 = ww.DAsWw_Type_j(var1);
         int var67 = var11 != 0 && (var4.player == null || var4.player.getStatusEffects().isEmpty()) ? 1 : 0;
         if (var67 != 0) {
            var66 = ww.DAsWw_Type_j(true);
         }

         QAsLp_Type_m("keybinds").v = var65.width();
         QAsLp_Type_m("keybinds").y = var65.height();
         QAsLp_Type_m("effects").v = var66.width();
         QAsLp_Type_m("effects").y = var66.height();
         Iterator var35 = S.iterator();

         while (true) {
            HudRenderer.Type_m var36;
            boolean var39;
            label1025:
            while (true) {
               if (!var35.hasNext()) {
                  if (var1) {
                     L = List.copyOf(var5);
                     CropHelper = GuiWorkspace.m;
                  }

                  if (!var1 && Boolean.getBoolean("larp.debug")) {
                     int var68 = var10 - 15;

                     for (Entry var71 : op.gAsMap().entrySet()) {
                        String var73 = (String)var71.getKey() + " " + var71.getValue();
                        TextDrawing.CAsVoid(var0, var73, var9 - TextDrawing.nAsInt(var73) - 5, var68, bd.e);
                        var68 -= 11;
                     }

                     for (Entry var72 : op.pAsMap().entrySet()) {
                        String var74 = String.format(Locale.ROOT, "%SkyChunkCache %.2f ms CPU", var72.getKey(), var72.getValue());
                        TextDrawing.CAsVoid(var0, var74, var9 - TextDrawing.nAsInt(var74) - 5, var68, bd.e);
                        var68 -= 11;
                     }
                  }

                  op.aAsVoid("HUD", var6);
                  return;
               }

               var36 = (HudRenderer.Type_m)var35.next();
               var36.PAsVoid(var9, var10);
               int var37 = var36.PAsInt(var9);
               int var38 = var36.ZAsInt(var10);
               var39 = HAsBoolean(var36);
               if ((var1 ? d7.oAsBoolean(var39, var3, var2 == var36) : var39)
                  && (var1 || (!var36.a.equals("modules") || !var12.isEmpty()) && (!var36.a.equals("armor") || XAsBoolean(var19)))
                  && (var1 || var11 != 0 || !var31.containsKey(var36.a) || lj.AAsBoolean(var36.a, (lj.Type_y)var31.get(var36.a)))
                  && (var1 || var67 != 0 || !var36.a.equals("effects") || var4.player != null && !var4.player.getStatusEffects().isEmpty())) {
                  int var40 = var1 || var11 != 0 && !IslandLyrics.tAsBoolean() ? 1 : 0;
                  if (var1 || !var36.a.equals("island") || var40 != 0 || IslandLyrics.tAsBoolean()) {
                     int var41 = !var36.a.equals("notifications") && !var36.a.equals("crosshair") ? 0 : 1;
                     if (var41 == 0 || var1) {
                        var36.uAsVoid(var0, var37, var38);

                        try {
                           String var42 = var36.a;
                           switch (var42) {
                              case "cooldowns":
                              case "staffs":
                              case "inventory":
                              case "radar":
                                 lj.XAsVoid(var0, var36.a, (lj.Type_y)var31.get(var36.a), var37, var38, var1);
                                 break label1025;
                              case "island":
                                 IslandLyrics var78 = IslandLyrics.zAsIslandLyrics();
                                 if (var78 == null) {
                                    break;
                                 }

                                 var78.WAsVoid(var0, var37, var38, var36.v, var40 != 0);
                                 break label1025;
                              case "keystrokes":
                                 if (var8 == null) {
                                    break;
                                 }

                                 if (var8.wAsBoolean() && !(var4.currentScreen instanceof ClickGuiScreen)) {
                                    hAsVoid(var0, var37, var38, var36.v, var36.y);
                                 }

                                 KeystrokesHud.IAsVoid(var0, var8, var37, var38, var1);
                                 break label1025;
                              case "notifications":
                                 NotificationHud.xAsVoid(var0, var37, var38);
                                 break label1025;
                              case "crosshair":
                                 CustomCrosshair.yAsVoid(var0, var37 + var36.v / 2, var38 + var36.y / 2);
                                 break label1025;
                              case "watermark":
                                 if (!var1 && !var8.RAsBoolean()) {
                                    break;
                                 }

                                 if (var8 != null && var8.wAsBoolean() && !(var4.currentScreen instanceof ClickGuiScreen)) {
                                    hAsVoid(var0, var37, var38, var36.v, var36.y);
                                 }

                                 MenuLogoLayout.iAsVoid(var0, var37, var38, var36.v, var26, var27, var25, var28);
                                 break label1025;
                              case "speed":
                                 MenuLogoLayout.zAsVoid(var0, var37, var38, l, var1);
                                 break label1025;
                              case "info":
                                 if (!var1 && !var8.zAsBoolean()) {
                                    break;
                                 }

                                 if (var8 != null && var8.wAsBoolean() && !(var4.currentScreen instanceof ClickGuiScreen)) {
                                    hAsVoid(var0, var37, var38, var36.v, var36.y);
                                 }

                                 MenuLogoLayout.YAsVoid(var0, var37, var38, var36.v, var29);
                                 break label1025;
                              case "modules":
                                 if (!var1 && !var8.UAsBoolean()) {
                                    break;
                                 }

                                 int var77 = 0;

                                 for (; var77 < var12.size(); var77++) {
                                    String var80 = var12.get(var77).name();
                                    int var83 = var77 / var16;
                                    int var86 = var77 % var16;
                                    int var87 = var37 + var36.vAsInt() / 2 > var9 / 2 ? 1 : 0;
                                    int var88 = var87 != 0
                                       ? var37 + var36.v - var83 * (var18 + 8)
                                       : var37 + var83 * (var18 + 8) + MenuLogoLayout.xAsInt(var12.get(var77).width());
                                    int var89 = var12.get(var77).width();
                                    int var51 = MenuLogoLayout.xAsInt(var89);
                                    int var90 = var88 - var51;
                                    int var91 = var38 + var86 * 17;
                                    MenuLogoLayout.aAsVoid(var0, var80, var89, var88, var91, var87 != 0);
                                    if (!var1) {
                                       xAsVoid(
                                          var36,
                                          new d7.Type_p(
                                             var37 + (int)Math.floor((double)(var90 - var37) * var36.vAsDouble()),
                                             var38 + (int)Math.floor((double)(var91 - var38) * var36.vAsDouble()),
                                             (int)Math.ceil((double)var51 * var36.vAsDouble()),
                                             (int)Math.ceil(17.0 * var36.vAsDouble())
                                          )
                                       );
                                    }
                                 }

                                 if (var1 && var12.isEmpty()) {
                                    TextDrawing.CAsVoid(var0, "No active modules", var37 + 4, var38 + 5, bd.e);
                                 }
                                 break label1025;
                              case "target":
                                 if (!var1 && var30 == 0) {
                                    iAsVoid(var0, var37, var38);
                                    break label1025;
                                 }

                                 mAsVoid(var0, var37, var38);
                                 break label1025;
                              case "keybinds":
                              case "effects":
                                 ww.Type_j var76 = var36.a.equals("keybinds") ? var65 : var66;
                                 int var79 = var37 + var36.vAsInt() / 2 > var9 / 2 ? 1 : 0;
                                 if (var8 != null && var8.wAsBoolean() && !(var4.currentScreen instanceof ClickGuiScreen)) {
                                    for (d7.Type_p var84 : ww.KAsList(var76, var37, var38, var79 != 0)) {
                                       hAsVoid(var0, var84.x(), var84.y(), var84.width(), var84.height());
                                    }
                                 }

                                 ww.XAsVoid(var0, var76, var37, var38, var79 != 0);
                                 if (var1) {
                                    break label1025;
                                 }

                                 Iterator var82 = ww.KAsList(var76, var37, var38, var79 != 0).iterator();

                                 while (true) {
                                    if (!var82.hasNext()) {
                                       break label1025;
                                    }

                                    d7.Type_p var85 = (d7.Type_p)var82.next();
                                    xAsVoid(
                                       var36,
                                       new d7.Type_p(
                                          var37 + (int)Math.floor((double)(var85.x() - var37) * var36.vAsDouble()),
                                          var38 + (int)Math.floor((double)(var85.y() - var38) * var36.vAsDouble()),
                                          (int)Math.ceil((double)var85.width() * var36.vAsDouble()),
                                          (int)Math.ceil((double)var85.height() * var36.vAsDouble())
                                       )
                                    );
                                 }
                              case "armor":
                                 if (!var1 && !var8.BAsBoolean()) {
                                    break;
                                 }

                                 if (var8.armorBackground.getValue() && XAsBoolean(var19)) {
                                    GuiTextureDrawing.AAsVoid(var0, (float)var37, (float)var38, (float)var36.v, (float)var36.y, 6.0F, MenuLogoLayout.zAsInt());
                                    MenuLogoLayout.yAsVoid(var0, var37, var38, var36.v, var36.y, 6);
                                 }

                                 for (int var44 = 0; var44 < var19.size(); var44++) {
                                    ItemStack var45 = var19.get(var44);
                                    int var46 = var37 + 3 + (var61 != 0 ? 0 : var44 * (var58 + var60));
                                    int var47 = var38 + 3 + (var61 != 0 ? var44 * (var59 + var60) : 0);
                                    if (!var45.isEmpty()) {
                                       int var48 = var61 != 0 ? var46 : var46 + (var58 - 21) / 2;
                                       var0.getMatrices().pushMatrix();
                                       var0.getMatrices().translate((float)var48, (float)var47).scale(1.3125F);
                                       var0.drawItem(var45, 0, 0);
                                       var0.getMatrices().popMatrix();
                                       if (var45.isDamageable()) {
                                          int var49 = Math.max(0, var45.getMaxDamage() - var45.getDamage());
                                          double var50 = (double)var49 / (double)Math.max(1, var45.getMaxDamage());
                                          int var52 = bg.xAsInt(-43434, -8790368, var50);
                                          if (var8.armorBars.getValue()) {
                                             GuiTextureDrawing.CAsVoid(var0, var46 + 2, var47 + 23, var58 - 4, 2, 1879048192);
                                             GuiTextureDrawing.CAsVoid(var0, var46 + 2, var47 + 23, (int)Math.round((double)(var58 - 4) * var50), 2, var52);
                                          }

                                          if (!var8.armorDurability.pAsBoolean("Off")) {
                                             String var53 = var8.armorDurability.pAsBoolean("Percent")
                                                ? (int)Math.ceil(var50 * 100.0) + "%"
                                                : Integer.toString(var49);
                                             int var54 = var61 != 0 ? var46 + 28 : var46 + (var58 - ww.WAsInt(var53, 10.4)) / 2;
                                             ww.fAsVoid(var0, var53, (double)var54, (double)(var47 + (var61 != 0 ? 7 : 28)), 10.4, var52);
                                          }
                                       }
                                    }
                                 }
                              default:
                                 break label1025;
                           }
                        } finally {
                           var36.IAsVoid(var0);
                        }
                     }
                  }
               }
            }

            if (!var1 && (var36.a.equals("target") ? var30 != 0 : !var36.a.equals("modules") && !var36.a.equals("keybinds") && !var36.a.equals("effects"))) {
               xAsVoid(var36, var36.mAsD7_Type_p(var9, var10));
            }

            if (var1) {
               var5.add(new HudRenderer.Type_p(var36, var36.mAsD7_Type_p(var9, var10)));
            }

            if (var1 && !var39) {
               d7.Type_p var75 = var36.mAsD7_Type_p(var9, var10);
               GuiTextureDrawing.CAsVoid(var0, var75.x(), var75.y(), var75.width(), var75.height(), 1882207290);
            }
         }
      }
   }

   private static void hAsVoid(DrawContext var0, int var1, int var2, int var3, int var4) {
      Vector2f var5 = var0.getMatrices().transformPosition((float)var1, (float)var2, new Vector2f());
      BloomQuality.XAsVoid(
         Math.round(var5.x), Math.round(var5.y), Math.round((float)var3 * var0.getMatrices().m00()), Math.round((float)var4 * var0.getMatrices().m11())
      );
   }

   public static void UAsVoid() {
      N = null;
      TextLayout.aAsVoid(1.0);
      ChinaHatCodec.aAsVoid(1.0);
      W = 1.0;
      U = 0L;
      T.aAsVoid(0.0);
      SkyChunkCache.eAsVoid();
      PriceDetector = 0L;
      BloomUtility = 0.0F;
   }

   public static void mAsVoid(DrawContext var0, int var1, int var2) {
      sAsVoid(var0, var1, var2, MinecraftClient.getInstance().player, "Sample opponent", 14.0F, 20.0F, 2.0F, 6.0, 1.0, 0.7, 0.7, true);
   }

   private static void iAsVoid(DrawContext var0, int var1, int var2) {
      long var3 = op.RAsLong();

      try {
         MinecraftClient var5 = MinecraftClient.getInstance();
         if (N != null && N.getEntityWorld() != var5.world) {
            UAsVoid();
         }

         LivingEntity var6 = SharedRenderEffects.HAsLivingEntity();
         if (var6 != null && var6 != N) {
            N = var6;
            ClientLog.XAsVoid(
               "TargetHUD",
               "Target {}: {} AuraModule {} blocks, portrait scale {} before HUD transform",
               var6.getType(),
               var6.getWidth(),
               var6.getHeight(),
               gAsFloat(var6.getWidth(), var6.getHeight())
            );
            SkyChunkCache.eAsVoid();
            BloomUtility = ScoreboardHealthResolver.UAsFloat(var6);
            PriceDetector = System.nanoTime();
            double var7 = ScoreboardHealthResolver.jAsDouble(var6);
            TextLayout.aAsVoid(var7);
            ChinaHatCodec.aAsVoid(var7);
            W = var7;
            U = 0L;
         }

         long var22 = System.nanoTime();
         if (PriceDetector != 0L) {
            SkyChunkCache.sAsVoid((double)(var22 - m) / 1.0E9);
         }

         PriceDetector = var22;
         float var9 = N == null ? 0.0F : ScoreboardHealthResolver.UAsFloat(N);
         if (N != null) {
            SkyChunkCache.TAsVoid((double)(BloomUtility - var9), ThreadLocalRandom.current());
            BloomUtility = var9;
         }

         double var10 = T.bAsDouble(var6 == null ? 0.0 : 1.0, 200);
         if (N == null || var10 < 0.01) {
            if (var6 == null) {
               N = null;
               SkyChunkCache.eAsVoid();
            }

            return;
         }

         HudRenderer.Type_m var12 = QAsLp_Type_m("target");
         double var13 = 0.92 + 0.08 * var10;
         xAsVoid(var12, vAsD7_Type_p(var1, var2, var12.vAsDouble(), var13, new d7.Type_p(0, 0, 224, 72)));
         double var15 = (double)Math.clamp(var9 / Math.max(1.0F, N.getMaxHealth()), 0.0F, 1.0F);
         double var17 = TextLayout.bAsDouble(var15, 220);
         sAsVoid(
            var0,
            var1,
            var2,
            N,
            ml.cAsString(N.getUuid(), N.getName().getString()),
            Math.max(0.0F, var9),
            Math.max(1.0F, N.getMaxHealth()),
            Math.max(0.0F, N.getAbsorptionAmount()),
            var5.player == null ? 0.0 : (double)var5.player.distanceTo(N),
            var10,
            var17,
            rAsDouble(var15, var17, var22),
            false
         );
      } finally {
         op.aAsVoid("TargetHUD", var3);
      }
   }

   private static double rAsDouble(double var0, double var2, long var4) {
      if (var0 < W) {
         ChinaHatCodec.aAsVoid(Math.max(ChinaHatCodec.yAsDouble(), var2));
         U = var4 + 400000000L;
      }

      W = var0;
      double var6 = var4 < U ? ChinaHatCodec.yAsDouble() : ChinaHatCodec.YAsDouble(var0, 450, w2.Type_e.QUAD_OUT);
      return Math.max(var6, var2);
   }

   public static d7.Type_p vAsD7_Type_p(int var0, int var1, double var2, double var4, d7.Type_p var6) {
      double var11 = (double)var0 + (112.0 + ((double)var6.x() - 112.0) * var4) * var2;
      double var13 = (double)var1 + (36.0 + ((double)var6.y() - 36.0) * var4) * var2;
      int var15 = (int)Math.floor(var11);
      int var16 = (int)Math.floor(var13);
      return new d7.Type_p(
         var15, var16, (int)Math.ceil(var11 + (double)var6.width() * var4 * var2) - var15, (int)Math.ceil(var13 + (double)var6.height() * var4 * var2) - var16
      );
   }

   public static float gAsFloat(float var0, float var1) {
      return Float.isFinite(var0) && Float.isFinite(var1) && !(var0 <= 0.0F) && !(var1 <= 0.0F) ? Math.min(32.0F, 42.0F / Math.max(var0, var1)) : 0.0F;
   }

   private static void CAsVoid(DrawContext var0, int var1, int var2, LivingEntity var3) {
      EntityRenderer var4 = MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(var3);
      EntityRenderState var5 = var4.getAndUpdateRenderState(var3, 1.0F);
      float var6 = gAsFloat(var5.width, var5.height);
      if (var6 <= 0.0F) {
         ClientLog.hAsVoid("TargetHUD", "Invalid portrait dimensions; using an icon until target state recovers", null);
         ww.aAsVoid(var0, "user", var1 + 10, var2 + 10, 30, -5592406);
      } else {
         var5.shadowPieces.clear();
         var5.outlineColor = 0;
         if (var5 instanceof LivingEntityRenderState var7) {
            var7.bodyYaw = 180.0F;
            var7.relativeHeadYaw = 0.0F;
            var7.pitch = 0.0F;
         }

         ScreenRect var11 = new ScreenRect(var1, var2, 50, 50).transform(var0.getMatrices());
         var0.enableScissor(var1, var2, var1 + 50, var2 + 50);

         try {
            var0.addEntity(
               var5,
               var6 * var0.getMatrices().m00(),
               new Vector3f(0.0F, var5.height / 2.0F, 0.0F),
               new Quaternionf().rotateZ((float) Math.PI),
               new Quaternionf(),
               var11.getLeft(),
               var11.getTop(),
               var11.getRight(),
               var11.getBottom()
            );
         } finally {
            var0.disableScissor();
         }
      }
   }

   private static void sAsVoid(
      DrawContext var0,
      int var1,
      int var2,
      LivingEntity var3,
      String var4,
      float var5,
      float var6,
      float var7,
      double var8,
      double var10,
      double var12,
      double var14,
      boolean var16
   ) {
      MinecraftClient var17 = MinecraftClient.getInstance();
      TargetHUD var18 = LarpClient.CAsLarpClient().mAsF().EAsModule(TargetHUD.class);
      double var21 = 0.92 + 0.08 * var10;
      var0.getMatrices().pushMatrix();
      var0.getMatrices().translate((float)var1 + 112.0F, (float)var2 + 36.0F).scale((float)var21).translate((float)(-var1) - 112.0F, (float)(-var2) - 36.0F);

      try {
         HUD var23 = HUD.EAsHUD();
         if (var23 != null && var23.wAsBoolean() && !(var17.currentScreen instanceof ClickGuiScreen)) {
            hAsVoid(var0, var1, var2, 224, 72);
         }

         GuiTextureDrawing.AAsVoid(var0, (float)var1, (float)var2, 224.0F, 72.0F, 12.0F, bg.pAsInt(-603321844, var10));
         if (!var16 && !SkyChunkCache.tAsList().isEmpty()) {
            var0.enableScissor(var1, var2, var1 + 224, var2 + 72);

            try {
               for (n1.Type_v var25 : SkyChunkCache.tAsList()) {
                  double var26 = Math.clamp(var25.T / var25.a, 0.0, 1.0);
                  double var28 = var10 * (1.0 - var26) * (var25.p ? 1.0 : 0.72);
                  int var30 = Math.max(1, (int)Math.round(var25.z * (1.0 - var26 * 0.35)));
                  int var31 = var1 + 36 + (int)Math.round(var25.U);
                  int var32 = var2 + 36 + (int)Math.round(var25.R);
                  int var33 = var25.p ? bd.w : -419430401;
                  GuiTextureDrawing.RAsVoid(var0, var31, var32, (int)Math.ceil((double)var30 * 1.125), bg.pAsInt(var33, var28 * 0.22));
                  GuiTextureDrawing.RAsVoid(var0, var31, var32, Math.max(1, var30 / 2), bg.pAsInt(var33, var28));
               }
            } finally {
               var0.disableScissor();
            }
         }

         if (var3 instanceof AbstractClientPlayerEntity var56) {
            int var58 = Math.min(140, var56.hurtTime * 16);
            var0.getMatrices().pushMatrix();
            float var60 = (float)Math.sin((double)var56.hurtTime / 10.0 * Math.PI * 2.0) * 0.07F;
            var0.getMatrices().translate((float)(var1 + 36), (float)(var2 + 36)).rotate(var60).translate((float)(-var1 - 36), (float)(-var2 - 36));
            PlayerSkinDrawer.draw(var0, var56.getSkin(), var1 + 11, var2 + 11, 50, bg.pAsInt(-65536 | 255 - var58 << 8 | 255 - var58, var10));
            var0.getMatrices().popMatrix();
         } else if (var3 != null) {
            CAsVoid(var0, var1 + 11, var2 + 11, var3);
         } else {
            ww.aAsVoid(var0, "user", var1 + 21, var2 + 21, 30, bg.pAsInt(-5592406, var10));
         }

         int var57 = var18 != null && !var18.equipmentPos.pAsBoolean("Above") ? 0 : 1;
         int var59 = var18 != null && var18.healthBar.pAsBoolean("Above") ? 1 : 0;
         int var61 = var1 + 72;
         int var27 = var1 + 224 - 11;
         int var62 = var27 - var61;
         int var29 = var2 + 10;
         int var63 = var57 != 0 ? var29 : var29 + 17 + 6 + 5 + 6;
         int var64 = var57 != 0 ? var29 + 19 + 6 : var29;
         int var65 = var59 != 0 ? var64 + 5 + 6 : var64;
         int var66 = var59 != 0 ? var64 : var64 + 17 + 6;
         String var34 = String.format(Locale.ROOT, "%.1f", var5);
         int var35 = var27 - ww.WAsInt(var34, 16.0);
         ww.fAsVoid(var0, var34, (double)var35, (double)(var65 - 1), 16.0, bg.pAsInt(-1, var10));
         int var36 = var35 - 13;
         int var37 = var65 + 3;
         var0.drawGuiTexture(RenderPipelines.GUI_TEXTURED, I, var36, var37, 9, 9, bg.pAsInt(-1, var10));
         var0.drawGuiTexture(RenderPipelines.GUI_TEXTURED, k, var36, var37, 9, 9, bg.pAsInt(-1, var10));
         int var38 = var36 - 6;
         if (var7 > 0.0F) {
            String var39 = String.format(Locale.ROOT, "+%.1f", var7);
            int var40 = var36 - 4 - ww.WAsInt(var39, 11.0);
            ww.fAsVoid(var0, var39, (double)var40, (double)(var65 + 3), 11.0, bg.pAsInt(-11443, var10));
            var38 = var40 - 6;
         }

         String var67 = ww.xAsString(var4, (double)Math.max(0, var38 - var61), 13.0);
         ww.fAsVoid(var0, var67, (double)var61, (double)(var65 + 1), 13.0, bg.pAsInt(-1, var10));
         double var68 = (double)(var6 / (var6 + Math.max(0.0F, var7)));
         GuiTextureDrawing.AAsVoid(var0, (float)var61, (float)var66, (float)var62, 5.0F, 2.0F, bg.pAsInt(788529151, var10));
         int var42 = (int)Math.round((double)var62 * Math.clamp(var12, 0.0, 1.0) * var68);
         int var43 = (int)Math.round((double)var62 * Math.clamp(var14, 0.0, 1.0) * var68);
         if (var43 > var42) {
            GuiTextureDrawing.AAsVoid(var0, (float)var61, (float)var66, (float)var43, 5.0F, 2.0F, bg.pAsInt(-922746881, var10));
         }

         if (var42 > 0) {
            RoundedShapePipeline.gAsVoid(
               var0,
               (float)var61,
               (float)var66,
               (float)var42,
               5.0F,
               2.0F,
               bg.pAsInt(-3790808, var10),
               bg.pAsInt(bg.xAsInt(-3790808, -38050, (double)var42 / (double)var62), var10)
            );
         }

         if (var7 > 0.0F) {
            int var44 = Math.min(var62 - var42, Math.round((float)var62 * var7 / (var6 + var7)));
            if (var44 > 0) {
               GuiTextureDrawing.AAsVoid(var0, (float)(var61 + var42), (float)var66, (float)var44, 5.0F, 2.0F, bg.pAsInt(-11443, var10));
            }
         }

         String var69 = String.format(Locale.ROOT, var8 >= 100.0 ? "%.0fm" : "%.1fm", var8);
         int var45 = bg.pAsInt(-5592406, var10);
         if (var3 instanceof AbstractClientPlayerEntity) {
            int var46 = var18 != null && var18.handsItems.pAsBoolean("Left") ? 1 : 0;
            int var47 = var46 != 0 ? var61 + 36 + 6 : var61;
            int var48 = var46 != 0 ? var61 : var61 + 72 + 6;
            RAsVoid(var0, var3, var47, var63, Arrays.copyOfRange(c, 0, 4), var10);
            RAsVoid(var0, var3, var48, var63, Arrays.copyOfRange(c, 4, 6), var10);
            ww.fAsVoid(var0, var69, (double)(var27 - ww.WAsInt(var69, 11.0)), (double)(var63 + 4), 11.0, var45);
         } else {
            ww.fAsVoid(var0, var69, (double)var61, (double)(var63 + 4), 11.0, var45);
         }
      } finally {
         var0.getMatrices().popMatrix();
      }
   }

   private static void RAsVoid(DrawContext var0, LivingEntity var1, int var2, int var3, EquipmentSlot[] var4, double var5) {
      for (int var7 = 0; var7 < var4.length; var7++) {
         ItemStack var8 = var1.getEquippedStack(var4[var7]);
         int var9 = var2 + var7 * 18;
         if (var8.isEmpty()) {
            GuiTextureDrawing.AAsVoid(var0, (float)(var9 + 2), (float)(var3 + 2), 12.0F, 12.0F, 3.0F, bg.pAsInt(352321535, var5));
         } else {
            if (var5 > 0.5) {
               var0.drawItem(var8, var9, var3);
            }

            if (var8.isDamageable()) {
               double var10 = Math.clamp((double)(var8.getMaxDamage() - var8.getDamage()) / (double)Math.max(1, var8.getMaxDamage()), 0.0, 1.0);
               GuiTextureDrawing.AAsVoid(var0, (float)var9, (float)(var3 + 17), 16.0F, 2.0F, 1.0F, bg.pAsInt(-938208236, var5));
               GuiTextureDrawing.AAsVoid(
                  var0,
                  (float)var9,
                  (float)(var3 + 17),
                  (float)((int)Math.round(16.0 * var10)),
                  2.0F,
                  1.0F,
                  bg.pAsInt(bg.xAsInt(-43434, -13710223, var10), var5)
               );
            }
         }
      }
   }

   public static final class Type_m {
      public final String a;
      public int v;
      public int y;
      private double SahurDanceEngine = 2.5;
      private final GuiWorkspace.Type_g z;

      Type_m(String var1, int var2, int var3, double var4, double var6) {
         this.a = var1;
         this.v = var2;
         this.y = var3;
         this.z = new GuiWorkspace.Type_g(var4, var6);
      }

      public double lAsDouble() {
         String var1 = this.a;

         return switch (var1) {
            case "watermark" -> 0.55;
            case "keybinds", "modules", "info" -> 0.7;
            case "inventory" -> 0.7;
            default -> 1.0;
         };
      }

      public double BAsDouble() {
         GuiWorkspace.Type_z var1 = GuiWorkspace.v.get(this.a);
         return var1 == null ? this.lAsDouble() : var1.scale();
      }

      public double vAsDouble() {
         return Math.min(this.d, this.BAsDouble());
      }

      public void PAsVoid(int var1, int var2) {
         this.d = Math.max(0.1, Math.min(2.5, Math.min((double)Math.max(1, var1) / (double)this.v, (double)Math.max(1, var2) / (double)this.y)));
      }

      public d7.Type_p mAsD7_Type_p(int var1, int var2) {
         if (this.a.equals("crosshair")) {
            int var3 = CustomCrosshair.JAsInt();
            return new d7.Type_p(
               this.PAsInt(var1) + (int)Math.round(((double)this.v / 2.0 - (double)var3) * this.vAsDouble()),
               this.ZAsInt(var2) + (int)Math.round(((double)this.y / 2.0 - (double)var3) * this.vAsDouble()),
               (int)Math.ceil((double)(var3 * 2) * this.vAsDouble()),
               (int)Math.ceil((double)(var3 * 2) * this.vAsDouble())
            );
         } else {
            return new d7.Type_p(
               this.PAsInt(var1), this.ZAsInt(var2), this.vAsInt(), this.a.equals("notifications") ? (int)Math.round(30.0 * this.vAsDouble()) : this.TAsInt()
            );
         }
      }

      public d7.Type_p hAsD7_Type_p(int var1, int var2) {
         return new d7.Type_p(this.PAsInt(var1), this.ZAsInt(var2), this.vAsInt(), this.TAsInt());
      }

      public void AAsVoid() {
         GuiWorkspace.B.remove(this.a);
         this.iAsVoid(this.lAsDouble());
      }

      public void ZAsVoid() {
         GuiWorkspace.B.remove(this.a);
         GuiWorkspace.v.remove(this.a);
      }

      public void DAsVoid() {
         GuiWorkspace.B.put(this.a, new GuiWorkspace.Type_g(0.5, 0.5));
      }

      public boolean EAsBoolean() {
         return GuiWorkspace.v.getOrDefault(this.a, HudRenderer.v).hidden();
      }

      public int vAsInt() {
         return (int)Math.round((double)this.v * this.vAsDouble());
      }

      public int TAsInt() {
         return (int)Math.round((double)this.y * this.vAsDouble());
      }

      public void xAsVoid(boolean var1) {
         GuiWorkspace.v.put(this.a, new GuiWorkspace.Type_z(this.BAsDouble(), var1));
      }

      public void iAsVoid(double var1) {
         GuiWorkspace.v.put(this.a, new GuiWorkspace.Type_z(var1, this.EAsBoolean()));
      }

      public void uAsVoid(DrawContext var1, int var2, int var3) {
         var1.getMatrices().pushMatrix();
         var1.getMatrices().translate((float)var2, (float)var3).scale((float)this.vAsDouble()).translate((float)(-var2), (float)(-var3));
      }

      public void IAsVoid(DrawContext var1) {
         var1.getMatrices().popMatrix();
      }

      public int PAsInt(int var1) {
         return this.a.equals("crosshair") && !GuiWorkspace.B.containsKey(this.a)
            ? Math.max(0, var1 / 2 - this.vAsInt() / 2)
            : (int)Math.round(GuiWorkspace.B.getOrDefault(this.a, this.z).x() * (double)Math.max(0, var1 - this.vAsInt()));
      }

      public int ZAsInt(int var1) {
         return this.a.equals("crosshair") && !GuiWorkspace.B.containsKey(this.a)
            ? Math.max(0, var1 / 2 - this.TAsInt() / 2)
            : (int)Math.round(GuiWorkspace.B.getOrDefault(this.a, this.z).y() * (double)Math.max(0, var1 - this.TAsInt()));
      }

      public void SAsVoid(int var1, int var2, int var3, int var4) {
         GuiWorkspace.B
            .put(
               this.a,
               new GuiWorkspace.Type_g((double)var1 / (double)Math.max(1, var3 - this.vAsInt()), (double)var2 / (double)Math.max(1, var4 - this.TAsInt()))
            );
      }
   }

   public static record Type_p(HudRenderer.Type_m element, d7.Type_p bounds) {
   }
}
