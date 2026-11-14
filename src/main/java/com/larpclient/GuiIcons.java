package com.larpclient;

import com.larpclient.module.Category;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public final class GuiIcons {
   public static final Identifier w = yAsIdentifier("toggle-left");
   public static final Identifier X = yAsIdentifier("toggle-right");
   public static final Identifier C = yAsIdentifier("chevron-down");
   public static final Identifier S = yAsIdentifier("chevron-up");
   private static final Map<String, Identifier> i = new HashMap<>();

   public static Identifier PAsIdentifier(String var0) {
      return i.computeIfAbsent(var0, GuiIcons::yAsIdentifier);
   }

   public static String NAsString(Category var0) {
      return switch (var0) {
         case COMBAT -> "swords";
         case MOVEMENT -> "footprints";
         case RENDER -> "eye";
         case VISUALS -> "sparkles";
         case PLAYER -> "user";
      };
   }

   public static String XAsString(String var0) {
      return switch (var0) {
         case "watermark" -> "gauge";
         case "info" -> "map-pin";
         case "speed", "cooldowns" -> "gauge";
         case "staffs" -> "user";
         case "inventory" -> "panels-top-left";
         case "radar" -> "crosshair";
         case "music", "island" -> "bell";
         case "modules" -> "list";
         case "target", "crosshair" -> "crosshair";
         case "keybinds", "keystrokes" -> "keyboard";
         case "effects" -> "flask-conical";
         case "armor" -> "shield";
         default -> "bell";
      };
   }

   private GuiIcons() {
   }

   public static Identifier yAsIdentifier(String var0) {
      return Identifier.of("larp-client", "textures/gui/icons/" + var0 + ".png");
   }

   public static void aAsVoid(DrawContext var0, Identifier var1, int var2, int var3, int var4, int var5) {
      var0.drawTexture(RenderPipelines.GUI_TEXTURED, var1, var2, var3, 0.0F, 0.0F, var4, var4, 96, 96, 96, 96, QueueGroup.iAsInt(var5));
   }
}
