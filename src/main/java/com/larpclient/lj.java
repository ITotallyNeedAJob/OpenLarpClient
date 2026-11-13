package com.larpclient;

import com.larpclient.module.HUD;
import com.larpclient.module.MCFriend;
import com.larpclient.module.StaffList;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public final class lj {
   private static final List<String> e = List.of(
      "helper", "admin", "owner", "founder", "manager", "curator", "developer", "staff", "куратор", "админ", "хелпер", "помощн", "стаж", "владел", "разраб"
   );
   private static final String N = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀꜱᴛᴜᴠᴡʏᴢ";
   private static final String m = "abcdefghijklmnopqrstuvwyz";
   private static final Pattern p = Pattern.compile("§.?");
   private static final Pattern Z = Pattern.compile("[^\\p{L}]+");
   private static final Map<String, Boolean> I = new ConcurrentHashMap<>();

   private lj() {
   }

   public static boolean cAsBoolean(String var0) {
      if (var0.isBlank()) {
         return false;
      } else {
         if (I.size() > 1024) {
            I.clear();
         }

         return I.computeIfAbsent(var0, lj::SAsBoolean);
      }
   }

   private static boolean SAsBoolean(String var0) {
      String var1 = Normalizer.normalize(p.matcher(var0).replaceAll(""), Form.NFKC).toLowerCase(Locale.ROOT);
      StringBuilder var2 = new StringBuilder(var1.length());

      for (int var3 = 0; var3 < var1.length(); var3++) {
         int var4 = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀꜱᴛᴜᴠᴡʏᴢ".indexOf(var1.charAt(var3));
         var2.append(var4 < 0 ? var1.charAt(var3) : "abcdefghijklmnopqrstuvwyz".charAt(var4));
      }

      String var8 = var2.toString();
      if (e.stream().anyMatch(var8::contains)) {
         return true;
      } else {
         for (String var7 : Z.split(var8)) {
            if (var7.endsWith("mod")
               || var7.endsWith("мод")
               || var7.equals("mods")
               || var7.equals("dev")
               || var7.contains("moder") && !var7.contains("modern")
               || var7.contains("модер") && !var7.contains("модерн")) {
               return true;
            }
         }

         return false;
      }
   }

   public static double[] nAsDoubleArray2(double var0, double var2, double var4, double var6) {
      double var8 = Math.hypot(var0, var2);
      if (var8 > var6) {
         return null;
      } else {
         double var10 = Math.atan2(var2, var0) - Math.toRadians(var4 + 180.0);
         return new double[]{42.0 + var8 / var6 * 24.0 * Math.cos(var10), 42.0 + var8 / var6 * 24.0 * Math.sin(var10)};
      }
   }

   public static lj.Type_y cAsLj_Type_y(String var0, boolean var1) {
      MinecraftClient var2 = MinecraftClient.getInstance();
      HUD var3 = HUD.EAsHUD();
      ArrayList var4 = new ArrayList();
      switch (var0) {
         case "cooldowns":
            if (var2.player != null) {
               HashSet var11 = new HashSet();
               ItemCooldownManager var12 = var2.player.getItemCooldownManager();

               for (int var9 = 0; var9 < var2.player.getInventory().size(); var9++) {
                  ItemStack var10 = var2.player.getInventory().getStack(var9);
                  if (!var10.isEmpty()
                     && (var3 == null || !var3.cooldownItems.pAsBoolean("Combat") || lj.Type_o.j.contains(var10.getItem()))
                     && var12.isCoolingDown(var10)
                     && var11.add(var12.getGroup(var10))) {
                     var4.add(
                        new lj.Type_r(
                           var10.getName().getString(),
                           String.format(Locale.ROOT, "%.1fs", var12.getCooldownProgress(var10, 0.0F) * 3.0F),
                           var10.copy(),
                           null,
                           -1250068
                        )
                     );
                  }
               }
            }

            if (var1 && var4.isEmpty()) {
               var4.add(new lj.Type_r("Ender Pearl", "1.8s", new ItemStack(Items.ENDER_PEARL), null, -1250068));
            }

            return YAsLj_Type_y("Cooldowns", "gauge", var4, 23);
         case "staffs":
            for (StaffList.Type_u var8 : StaffList.kAsList()) {
               var4.add(new lj.Type_r(var8.name(), var8.status().x, ItemStack.EMPTY, var8.info(), var8.status().i));
            }

            if (var1 && var4.isEmpty()) {
               var4.add(new lj.Type_r("Staff", "Online", ItemStack.EMPTY, null, -10551483));
            }

            if (var4.isEmpty()) {
               var4.add(new lj.Type_r("No staff online", "", ItemStack.EMPTY, null, -7697772));
            }

            return YAsLj_Type_y("StaffList", "user", var4, 19);
         case "inventory":
            return new lj.Type_y("Inventory", "panels-top-left", 250, var3 != null && !var3.inventoryHeader.getValue() ? 90 : 120, List.of());
         case "radar":
            return new lj.Type_y("Radar", "crosshair", 84, 84, List.of());
         default:
            throw new IllegalArgumentException(var0);
      }
   }

   private static lj.Type_y YAsLj_Type_y(String var0, String var1, List<lj.Type_r> var2, int var3) {
      int var4 = 168;

      for (lj.Type_r var6 : var2) {
         var4 = Math.max(var4, Math.min(300, ww.WAsInt(var6.label(), 13.0) + ww.WAsInt(var6.value(), 13.0) + 64));
      }

      return new lj.Type_y(var0, var1, var4, 48 + var2.size() * var3, List.copyOf(var2));
   }

   public static boolean AAsBoolean(String var0, lj.Type_y var1) {
      return switch (var0) {
         case "cooldowns" -> var1 != null && !var1.rows().isEmpty();
         default -> true;
      };
   }

   public static void CAsVoid(DrawContext var0, int var1, int var2, int var3, int var4, String var5, String var6, boolean var7) {
      GuiTextureDrawing.AAsVoid(var0, (float)var1, (float)var2, (float)var3, (float)var4, 12.0F, -603321844);
      if (var7) {
         var0.enableScissor(var1, var2, var1 + var3, var2 + 32);

         try {
            GuiTextureDrawing.AAsVoid(var0, (float)var1, (float)var2, (float)var3, (float)var4, 12.0F, -267645934);
         } finally {
            var0.disableScissor();
         }
      }

      MenuLogoLayout.yAsVoid(var0, var1, var2, var3, var4, 12);
      if (var7) {
         RoundedShapePipeline.nAsVoid(var0, (float)(var1 + 2), (float)(var2 + 32), (float)(var3 - 4), 1.0F, 0.5F, 675036230);
         ww.fAsVoid(var0, var5, (double)(var1 + 12), (double)(var2 + 10), 13.0, -1250068);
         ww.aAsVoid(var0, var6, var1 + var3 - 26, var2 + 9, 14, bd.w);
      }
   }

   public static void XAsVoid(DrawContext var0, String var1, lj.Type_y var2, int var3, int var4, boolean var5) {
      MinecraftClient var6 = MinecraftClient.getInstance();
      HUD var7 = HUD.EAsHUD();
      switch (var1) {
         case "inventory":
            int var19 = var7 != null && !var7.inventoryHeader.getValue() ? 0 : 1;
            CAsVoid(var0, var3, var4, var2.width(), var2.height(), var2.title(), var2.icon(), var19 != 0);
            int var20 = var4 + (var19 != 0 ? 37 : 8);

            for (int var22 = 0; var22 < 27; var22++) {
               int var24 = var3 + 8 + var22 % 9 * 26;
               int var26 = var20 + var22 / 9 * 26;
               GuiTextureDrawing.AAsVoid(var0, (float)var24, (float)var26, 25.0F, 25.0F, 2.0F, 676352089);
               ItemStack var28 = var6.player == null ? ItemStack.EMPTY : var6.player.getInventory().getStack(var22 + 9);
               if (var5 && var28.isEmpty() && var22 < 3) {
                  var28 = new ItemStack(var22 == 0 ? Items.ENDER_PEARL : (var22 == 1 ? Items.DIAMOND_SWORD : Items.GOLDEN_APPLE));
               }

               if (!var28.isEmpty()) {
                  var0.drawItem(var28, var24 + 5, var26 + 5);
                  var0.drawStackOverlay(var6.textRenderer, var28, var24 + 5, var26 + 5);
               }
            }
            break;
         case "radar":
            CAsVoid(var0, var3, var4, 84, 84, "", "", false);
            GuiTextureDrawing.CAsVoid(var0, var3 + 42, var4 + 14, 1, 56, bg.XAsInt(bd.w, 0.23));
            GuiTextureDrawing.CAsVoid(var0, var3 + 14, var4 + 42, 56, 1, bg.XAsInt(bd.w, 0.23));
            GuiTextureDrawing.RAsVoid(var0, var3 + 42, var4 + 42, 2, bd.w);
            double var18 = var7 == null ? 47.0 : var7.radarRange.getValue();
            if (var6.player != null && var6.world != null) {
               MCFriend var21 = LarpClient.CAsLarpClient().mAsF().EAsModule(MCFriend.class);

               for (Entity var25 : var6.world.getEntities()) {
                  boolean var27 = var25 instanceof PlayerEntity;
                  if (var25 != var6.player && var25.isAlive() && (var27 || var7 != null && var7.radarMobs.getValue() && var25 instanceof MobEntity)) {
                     double[] var16 = nAsDoubleArray2(var25.getX() - var6.player.getX(), var25.getZ() - var6.player.getZ(), (double)var6.player.getYaw(), var18);
                     if (var16 != null) {
                        int var17 = !var27
                           ? -922768896
                           : (
                              var25 == SharedRenderEffects.HAsLivingEntity()
                                 ? -44462
                                 : (var21 != null && var21.SAsBoolean(var25.getName().getString()) ? -10551483 : -335544321)
                           );
                        GuiTextureDrawing.RAsVoid(var0, var3 + (int)Math.round(var16[0]), var4 + (int)Math.round(var16[1]), 2, var17);
                     }
                  }
               }
            }

            if (var5) {
               GuiTextureDrawing.RAsVoid(var0, var3 + 54, var4 + 29, 2, -335544321);
            }
            break;
         default:
            CAsVoid(var0, var3, var4, var2.width(), var2.height(), var2.title(), var2.icon(), true);
            int var10 = var1.equals("staffs") ? 19 : 23;
            int var11 = var4 + 39;

            for (lj.Type_r var13 : var2.rows()) {
               int var14 = var3 + 12;
               if (!var13.item().isEmpty()) {
                  var0.drawItem(var13.item(), var14, var11 + 2);
                  var14 += 22;
               } else if (var13.player() != null && StaffList.FAsBoolean()) {
                  PlayerSkinDrawer.draw(var0, var13.player().getSkinTextures(), var14, var11 + 2, 15);
                  var14 += 21;
               }

               int var15 = var13.value().isEmpty() ? 0 : ww.WAsInt(var13.value(), 13.0) + 8;
               ww.fAsVoid(
                  var0,
                  ww.xAsString(var13.label(), (double)(var3 + var2.width() - 12 - var15 - var14), 13.0),
                  (double)var14,
                  FontPreset.vAsDouble((double)var11, (double)var10, 13.0),
                  13.0,
                  var13.value().isEmpty() ? var13.tint() : -1250068
               );
               ww.fAsVoid(
                  var0,
                  var13.value(),
                  (double)(var3 + var2.width() - 12 - ww.WAsInt(var13.value(), 13.0)),
                  FontPreset.vAsDouble((double)var11, (double)var10, 13.0),
                  13.0,
                  var13.tint()
               );
               var11 += var10;
            }
      }
   }

   private static final class Type_o {
      static final Set<Item> j = Set.of(Items.ENDER_PEARL, Items.CHORUS_FRUIT, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE, Items.SHIELD);

      private Type_o() {
      }
   }

   public static record Type_r(String label, String value, ItemStack item, PlayerListEntry player, int tint) {
   }

   public static record Type_y(String title, String icon, int width, int height, List<lj.Type_r> rows) {
   }
}
