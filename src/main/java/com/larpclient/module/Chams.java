package com.larpclient.module;

import com.larpclient.ChamsOutlineEffect;
import com.larpclient.ClientLog;
import com.larpclient.CosmeticSubmissionHandler;
import com.larpclient.EffectDrawBuffers;
import com.larpclient.ThemeSettings;
import com.larpclient.ColorUtil;
import com.larpclient.BridgeRenderer;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class Chams extends Module {
   private final BooleanSetting players = this.addSetting(new BooleanSetting("Players", "Apply to players", true));
   private final BooleanSetting mobs = this.addSetting(new BooleanSetting("Mobs", "Apply to mobs", false));
   private final NumberSetting red = this.addSetting(new NumberSetting("Red", "Red color component", 1.0, 0.0, 1.0, 0.05));
   private final NumberSetting green = this.addSetting(new NumberSetting("Green", "Green color component", 0.3, 0.0, 1.0, 0.05));
   private final NumberSetting blue = this.addSetting(new NumberSetting("Blue", "Blue color component", 0.3, 0.0, 1.0, 0.05));
   private final BooleanSetting themeColors = this.addSetting(new BooleanSetting("Theme Colors", "Use the live Larp accent instead of RGB", true));
   private final NumberSetting range = this.addSetting(new NumberSetting("Range", "Maximum material distance", 128.0, 16.0, 512.0, 16.0));
   private final NumberSetting visibleOpacity = this.addSetting(
      new NumberSetting("Visible Opacity", "Material strength on visible surfaces", 0.3, 0.0, 1.0, 0.05)
   );
   private final NumberSetting hiddenOpacity = this.addSetting(new NumberSetting("Hidden Opacity", "Material strength behind walls", 0.55, 0.0, 1.0, 0.05));
   private final ColorSetting hiddenColor = this.addSetting(new ColorSetting("Hidden Color", "Color of occluded surfaces", -1077331));
   private final BooleanSetting throughWalls = this.addSetting(new BooleanSetting("Through Walls", "Draw the occluded material pass", true));
   private final BooleanSetting outline = this.addSetting(new BooleanSetting("Outline", "Add a vanilla silhouette outline", false));
   private final NumberSetting maxEntities = this.addSetting(new NumberSetting("Max Entities", "Nearest models drawn", 32.0, 8.0, 128.0, 8.0));
   public final bp h = new bp(var1x -> this.addSetting(var1x));
   private final BooleanSetting renderSelf = this.addSetting(new BooleanSetting("Render Self", "Include your model in third person", false));
   private final BooleanSetting teamColors = this.addSetting(new BooleanSetting("Team Colors", "Use entity team colors instead of the gradient", false));
   private final ChamsOutlineEffect H = new ChamsOutlineEffect("Chams");
   private static Chams v;
   private List<ChamsOutlineEffect.Type_a> E = List.of();
   private bp.Type_e R;
   private Object V;
   private Object g;
   private boolean z;

   public Chams() {
      super("Chams", "Animated gradient outlines, fill and glow on entities", Category.RENDER, -1);
      v = this;

      for (Setting var2 : List.of(this.red, this.green, this.blue, this.themeColors, this.visibleOpacity, this.hiddenOpacity, this.hiddenColor, this.outline)) {
         var2.visibleWhen(() -> false);
      }

      this.h.color1.visibleWhen(() -> !this.teamColors.getValue());
      this.h.color2.visibleWhen(() -> !this.teamColors.getValue());
   }

   public static Chams mAsChams() {
      return v;
   }

   @Override
   public void onEnable() {
      ClientLog.GAsVoid("Chams", "Echo gradient enabled; nearest model surfaces, stable wall visibility and native skin retained");
   }

   public List<Setting<?>> tAsList() {
      return List.of(this.players, this.mobs, this.renderSelf);
   }

   public List<Setting<?>> cAsList() {
      return List.of(this.teamColors, this.h.color1, this.h.color2);
   }

   public List<Setting<?>> OAsList() {
      return List.of(this.throughWalls);
   }

   public List<Setting<?>> kAsList() {
      return List.of(this.range, this.maxEntities);
   }

   public boolean VAsBoolean() {
      return this.isEnabled() ? this.isOperational() && this.players.getValue() : false;
   }

   public boolean NAsBoolean() {
      return this.isEnabled() ? this.isOperational() && this.mobs.getValue() : false;
   }

   public float red() {
      return this.red.getValue().floatValue();
   }

   public float green() {
      return this.green.getValue().floatValue();
   }

   public float blue() {
      return this.blue.getValue().floatValue();
   }

   public int uAsInt(LivingEntity var1) {
      return this.isEnabled() ? 0 : 0;
   }

   public void WAsVoid(float var1) {
      if (this.isEnabled()) {
         this.E = List.of();
         this.R = null;
         if (this.isRuntimeActive() && this.isInGame()) {
            this.V = mc.world;
            this.g = mc.player;
            this.z = this.throughWalls.getValue();
            this.R = this.h.NAsBp_Type_e(this.teamColors.getValue());
            ArrayList var2 = new ArrayList();
            if (this.renderSelf.getValue() && !mc.options.getPerspective().isFirstPerson() && mc.player.isAlive()) {
               var2.add(new ChamsOutlineEffect.Type_a(CosmeticSubmissionHandler.oAsEntityRenderState(mc.player, var1), this.iAsInt(mc.player)));
            }

            for (Entity var4 : ThemeSettings.jAsList(
               mc.world.getEntities(), mc.player, this.range.getValue(), this.maxEntities.getValue().intValue() - var2.size(), var1x -> {
                  if (var1x instanceof LivingEntity var2x && var2x.isAlive() && var1x instanceof PlayerEntity) {
                     return this.players.getValue();
                  }

                  return this.mobs.getValue();
               }
            )) {
               var2.add(
                  new ChamsOutlineEffect.Type_a(
                     CosmeticSubmissionHandler.oAsEntityRenderState(var4, var1),
                     bg.pAsInt(this.iAsInt(var4), ThemeSettings.RAsDouble((double)mc.player.distanceTo(var4), this.range.getValue()))
                  )
               );
            }

            this.E = List.copyOf(var2);
         }
      }
   }

   public void FAsVoid(MatrixStack var1, EffectDrawBuffers var2, float var3) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame() && this.V == mc.world && this.g == mc.player && this.R != null) {
            var2.CAsVoid();
            this.H.GAsVoid(this.E, var1, this.R, this.z);
         } else {
            this.jAsVoid();
         }
      }
   }

   private int iAsInt(Entity var1) {
      return this.teamColors.getValue() ? 0xFF000000 | var1.getTeamColorValue() : -1;
   }

   public void jAsVoid() {
      this.E = List.of();
      this.R = null;
      this.V = this.g = null;
      this.H.close();
      this.h.EAsVoid();
   }

   @Override
   public void onDisable() {
      this.jAsVoid();
   }

   @Override
   public void onTick() {
      if (!this.isInGame() || this.V != null && (this.V != mc.world || this.g != mc.player)) {
         this.jAsVoid();
      }
   }
}
