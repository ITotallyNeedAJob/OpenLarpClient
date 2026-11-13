package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.GuiTextureDrawing;
import com.larpclient.NotificationHud;
import com.larpclient.NotificationSound;
import com.larpclient.ScoreboardHealthResolver;
import com.larpclient.TextDrawing;
import com.larpclient.ThemeSettings;
import com.larpclient.bg;
import com.larpclient.d5;
import com.larpclient.op;
import com.larpclient.w3;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class ESP extends Module {
   private final ModeSetting mode = this.addSetting(
      new ModeSetting(
         "Mode", "Box/corners/fill, schematic skeleton, or entity silhouette", "Corners", "Box", "Corners", "Filled", "Skeleton", "Outline", "3D Box"
      )
   );
   private final BooleanSetting players = this.addSetting(new BooleanSetting("Players", "Show players", true));
   private final BooleanSetting mobs = this.addSetting(new BooleanSetting("Mobs", "Show hostile mobs", true));
   private final BooleanSetting animals = this.addSetting(new BooleanSetting("Animals", "Show animals", false));
   private final NumberSetting range = this.addSetting(new NumberSetting("Range", "Max render distance", 128.0, 8.0, 512.0, 8.0));
   private final ThemeSettings u = new ThemeSettings(var1 -> this.addSetting(var1));
   private final NumberSetting maxEntities = this.addSetting(new NumberSetting("Max Entities", "Nearest entities drawn", 128.0, 16.0, 512.0, 16.0));
   private final BooleanSetting healthBars = this.addSetting(new BooleanSetting("Health Bars", "High-contrast screen-space health bar", true));
   private final NumberSetting healthWidth = this.addSetting(new NumberSetting("Health Width", "Health bar width in GUI pixels", 3.0, 2.0, 6.0, 1.0));
   private final BooleanSetting healthText = this.addSetting(new BooleanSetting("Health Text", "Show numeric HP beneath the health bar", true));
   private static ESP c;
   private final Set<Integer> o = new HashSet<>();
   private final BooleanSetting notifications = this.addSetting(new BooleanSetting("Notifications", "Notify when new player enters range", false));
   private final Set<Integer> w = new HashSet<>();
   private final Set<Integer> Y = new HashSet<>();
   private boolean T = true;

   public ESP() {
      super("ESP", "See entities through walls", Category.RENDER, -1);
      c = this;
      this.healthWidth.visibleWhen(this.healthBars::getValue);
      this.healthText.visibleWhen(this.healthBars::getValue);
      this.u
         .DAsVoid(
            () -> true,
            () -> !this.mode.pAsBoolean("Outline"),
            () -> !this.mode.pAsBoolean("Outline") || this.healthBars.getValue(),
            () -> this.mode.pAsBoolean("Filled")
         );
      WorldRenderEvents.BEFORE_DEBUG_RENDER.register(this::zAsVoid);
      HudElementRegistry.addLast(Identifier.of("larp-client", "entity_esp"), this::FAsVoid);
   }

   @Override
   public void onEnable() {
      this.o.clear();
      this.w.clear();
      this.Y.clear();
      this.T = true;
   }

   public List<Setting<?>> OAsList() {
      return List.of(this.players, this.mobs, this.animals);
   }

   public List<Setting<?>> EAsList() {
      return List.of(this.mode, this.range);
   }

   public List<Setting<?>> rAsList() {
      return this.u.TAsList();
   }

   public List<Setting<?>> wAsList() {
      ArrayList var1 = new ArrayList<>(this.u.OAsList());
      var1.addAll(this.u.qAsList());
      return List.copyOf(var1);
   }

   public List<Setting<?>> IAsList() {
      return List.of(this.healthBars, this.healthWidth, this.healthText);
   }

   public List<Setting<?>> dAsList() {
      return List.of(this.maxEntities, this.notifications);
   }

   @Override
   public void onDisable() {
      this.o.clear();
      this.w.clear();
      this.Y.clear();
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         this.o.clear();
         if (this.mode.pAsBoolean("Outline")) {
            for (Entity var2 : ThemeSettings.jAsList(
               mc.world.getEntities(), mc.player, this.range.getValue(), this.maxEntities.getValue().intValue(), this::kAsBoolean
            )) {
               this.o.add(var2.getId());
            }
         }

         if (this.notifications.getValue()) {
            HashSet var12 = new HashSet();

            for (Entity var3 : mc.world.getEntities()) {
               if (var3 != mc.player && var3 instanceof PlayerEntity) {
                  PlayerEntity var4 = (PlayerEntity)var3;
                  if (var4.isAlive()) {
                     double var5 = (double)mc.player.distanceTo(var3);
                     if (!(var5 > this.range.getValue())) {
                        var12.add(var3.getId());
                        int var7 = !this.w.contains(var3.getId()) ? 1 : 0;
                        int var8 = !this.Y.contains(var3.getId()) ? 1 : 0;
                        if ((var7 != 0 || var8 != 0) && !this.T) {
                           this.w.add(var3.getId());
                           String var9 = var4.getName().getString();
                           int var10 = (int)ScoreboardHealthResolver.UAsFloat(var4);
                           String var11 = String.format("%.0fm", var5);
                           NotificationHud.GAsW8().RAsVoid("Player Detected", var9 + " - " + var10 + "HP - " + var11, NotificationHud.Type_u.WARNING, 10000L);
                           NotificationSound.XAsVoid();
                        } else {
                           this.w.add(var3.getId());
                        }
                     }
                  }
               }
            }

            this.w.retainAll(var12);
            this.Y.clear();
            this.Y.addAll(var12);
            this.T = false;
         }
      }
   }

   private boolean kAsBoolean(Entity var1) {
      return var1 instanceof LivingEntity
         && (
            var1 instanceof PlayerEntity
               ? this.players.getValue()
               : (var1 instanceof Monster ? this.mobs.getValue() : var1 instanceof AnimalEntity && this.animals.getValue())
         );
   }

   public static int HAsInt(LivingEntity var0) {
      if (c != null && c.isEnabled()) {
         if (c != null
            && c.isOperational()
            && c.mode.pAsBoolean("Outline")
            && c.isInGame()
            && var0 != mc.player
            && var0.isAlive()
            && c.kAsBoolean(var0)
            && c.o.contains(var0.getId())) {
            double var1 = (double)mc.player.distanceTo(var0);
            return var1 >= c.range.getValue() ? 0 : c.WAsInt(var0, var1);
         } else {
            return 0;
         }
      } else {
         return 0;
      }
   }

   private int WAsInt(LivingEntity var1, double var2) {
      int var4 = var1 instanceof PlayerEntity ? -4221441 : (var1 instanceof Monster ? -943189 : -7677757);
      return this.u.iAsInt(var4, var2, this.range.getValue(), ThemeSettings.sAsDouble(var1), var1.getId());
   }

   private void zAsVoid(WorldRenderContext var1) {
      if (this.isEnabled() && this.isOperational() && this.isInGame() && this.mode.pAsBoolean("3D Box")) {
         float var2 = mc.getRenderTickCounter().getTickProgress(false);
         long var3 = op.RAsLong();

         try {
            for (Entity var6 : ThemeSettings.jAsList(
               mc.world.getEntities(), mc.player, this.range.getValue(), this.maxEntities.getValue().intValue(), this::kAsBoolean
            )) {
               int var7 = this.WAsInt((LivingEntity)var6, (double)mc.player.distanceTo(var6));
               Box var8 = ThemeSettings.GAsBox(var6, var2);
               this.u.AAsVoid(var8, "Box", var7);
            }
         } catch (RuntimeException var12) {
            ClientLog.hAsVoid("ESP", "Entity overlay failed", var12);
         } finally {
            op.aAsVoid("ESP", var3);
         }
      }
   }

   private void FAsVoid(DrawContext var1, RenderTickCounter var2) {
      if (this.isOperational() && this.isInGame() && !mc.options.hudHidden) {
         Camera var3 = mc.gameRenderer.getCamera();
         Matrix4f var4 = w3.pAsMatrix4f(new Matrix4f());
         int var5 = mc.getWindow().getScaledWidth();
         int var6 = mc.getWindow().getScaledHeight();
         float var7 = var2.getTickProgress(false);

         for (Entity var10 : ThemeSettings.jAsList(
            mc.world.getEntities(), mc.player, this.range.getValue(), this.maxEntities.getValue().intValue(), this::kAsBoolean
         )) {
            if (this.u.throughWalls() || mc.player.canSee(var10)) {
               d5.Type_s var11 = d5.dAsD5_Type_s(ThemeSettings.GAsBox(var10, var7), var3.getCameraPos(), var4, var5, var6, false);
               if (var11 != null) {
                  int var12 = var11.left();
                  int var13 = var11.top();
                  int var14 = var11.right() - var12;
                  int var15 = var11.bottom() - var13;
                  LivingEntity var16 = (LivingEntity)var10;
                  int var17 = this.WAsInt(var16, (double)mc.player.distanceTo(var10));
                  if (var17 >>> 24 != 0) {
                     if (this.mode.pAsBoolean("Box") || this.mode.pAsBoolean("Corners") || this.mode.pAsBoolean("Filled")) {
                        if (this.mode.pAsBoolean("Filled")) {
                           GuiTextureDrawing.CAsVoid(var1, var12, var13, var14, var15, bg.pAsInt(var17, this.u.fillOpacity()));
                        }

                        int var18 = Math.max(1, Math.round(this.u.lineWidth()));
                        int var19 = this.mode.pAsBoolean("Corners") ? Math.max(3, var14 / 4) : var14;
                        int var20 = this.mode.pAsBoolean("Corners") ? Math.max(3, var15 / 5) : var15;
                        ArrayList var21 = new ArrayList();
                        if (!this.mode.pAsBoolean("Corners")) {
                           var21.add(new int[]{var12, var13, var14, var18});
                           var21.add(new int[]{var12, var13 + var15 - var18, var14, var18});
                           var21.add(new int[]{var12, var13, var18, var15});
                           var21.add(new int[]{var12 + var14 - var18, var13, var18, var15});
                        } else {
                           for (int var22 = 0; var22 < 2; var22++) {
                              for (int var23 = 0; var23 < 2; var23++) {
                                 var21.add(new int[]{var22 == 0 ? var12 : var12 + var14 - var19, var23 == 0 ? var13 : var13 + var15 - var18, var19, var18});
                                 var21.add(new int[]{var22 == 0 ? var12 : var12 + var14 - var18, var23 == 0 ? var13 : var13 + var15 - var20, var18, var20});
                              }
                           }
                        }

                        for (int[] var35 : (Iterable<int[]>) (var21)) {
                           GuiTextureDrawing.CAsVoid(var1, var35[0] - 1, var35[1] - 1, var35[2] + 2, var35[3] + 2, -401600234);
                        }

                        for (int[] var36 : (Iterable<int[]>) (var21)) {
                           GuiTextureDrawing.CAsVoid(var1, var36[0], var36[1], var36[2], var36[3], var17);
                        }
                     }

                     if (this.mode.pAsBoolean("Skeleton")) {
                        try {
                           this.pAsVoid(var1, var16, var7, var3, var4, var5, var6, false, var17);
                        } catch (RuntimeException var28) {
                           ClientLog.hAsVoid("ESP", "Skeleton overlay failed", var28);
                        }
                     }

                     if (this.healthBars.getValue()) {
                        int var29 = this.healthWidth.getValue().intValue();
                        int var30 = var12 - var29 - 5;
                        if (var30 < 4) {
                           var30 = Math.min(var5 - var29 - 2, var12 + var14 + 5);
                        }

                        double var31 = ThemeSettings.sAsDouble(var16);
                        int var34 = (int)Math.round((double)var15 * var31);
                        GuiTextureDrawing.CAsVoid(var1, var30 - 1, var13 - 1, var29 + 2, var15 + 2, -267382506);
                        GuiTextureDrawing.CAsVoid(var1, var30, var13, var29, var15, -13420994);
                        int var37 = var31 < 0.5 ? bg.xAsInt(-44442, -10660, var31 * 2.0) : bg.xAsInt(-10660, -10951269, (var31 - 0.5) * 2.0);
                        GuiTextureDrawing.CAsVoid(var1, var30, var13 + var15 - var34, var29, var34, var37);
                        if (var16.getAbsorptionAmount() > 0.0F) {
                           int var24 = Math.round((float)var15 * Math.min(1.0F, var16.getAbsorptionAmount() / Math.max(1.0F, var16.getMaxHealth())));
                           GuiTextureDrawing.CAsVoid(var1, var30 - 3, var13 + var15 - var24, 2, var24, -10660);
                        }

                        if (this.healthText.getValue()) {
                           String var38 = (int)Math.ceil((double)ScoreboardHealthResolver.UAsFloat(var16)) + " HP";
                           int var25 = TextDrawing.nAsInt(var38);
                           int var26 = Math.max(1, Math.min(var5 - var25 - 3, var30));
                           int var27 = Math.min(var6 - 12, var13 + var15 + 3);
                           GuiTextureDrawing.CAsVoid(var1, var26 - 2, var27 - 1, var25 + 4, 11, -401600234);
                           TextDrawing.CAsVoid(var1, var38, var26, var27, -1);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void pAsVoid(DrawContext var1, LivingEntity var2, float var3, Camera var4, Matrix4f var5, int var6, int var7, boolean var8, int var9) {
      Vec3d var10 = var2.getLerpedPos(var3);
      double var11 = (double)var2.getHeight();
      double var13 = (double)var2.getWidth();
      double var15 = Math.toRadians((double)MathHelper.lerpAngleDegrees(var3, var2.lastBodyYaw, var2.bodyYaw));
      Vec3d var17 = new Vec3d(-Math.sin(var15), 0.0, Math.cos(var15));
      Vec3d var18 = new Vec3d(Math.cos(var15), 0.0, Math.sin(var15));
      double var19 = (double)var2.limbAnimator.getAnimationProgress(var3) * 0.6662;
      double var21 = (double)Math.min(1.0F, var2.limbAnimator.getAmplitude(var3));
      double var23 = Math.cos(var19) * 1.4 * var21;
      double var25 = Math.cos(var19) * var21;
      Vec3d var27 = var10.add(0.0, var11 * 0.375, 0.0);
      Vec3d var28 = var10.add(0.0, var11 * 0.75, 0.0);
      Vec3d var29 = var10.add(0.0, var11 * 0.6875, 0.0);
      Vec3d var30 = var29.add(var18.multiply(var13 * 0.55));
      Vec3d var31 = var29.add(var18.multiply(-var13 * 0.55));
      Vec3d var32 = var27.add(var18.multiply(var13 * 0.18));
      Vec3d var33 = var27.add(var18.multiply(-var13 * 0.18));
      Vec3d[][] var34 = new Vec3d[][]{
         {var27, var28},
         {var30, var31},
         {var32, var33},
         {var30, NAsVec3d(var30, var17, var11 * 0.3125, var25)},
         {var31, NAsVec3d(var31, var17, var11 * 0.3125, -var25)},
         {var32, NAsVec3d(var32, var17, var11 * 0.375, -var23)},
         {var33, NAsVec3d(var33, var17, var11 * 0.375, var23)}
      };
      Vec3d var35 = var4.getCameraPos();
      ArrayList var36 = new ArrayList(var34.length);

      for (Vec3d[] var40 : var34) {
         d5.Type_w var41 = d5.CAsD5_Type_w(var40[0], var40[1], var35, var5, var6, var7, var8);
         if (var41 != null) {
            var36.add(var41);
         }
      }

      d5.Type_e var46 = d5.vAsD5_Type_e(var10.add(0.0, var11 * 0.875, 0.0), var35, var5, new Vector3f(var4.getVerticalPlane()), var6, var7, var8);
      int var47 = var46 == null ? 0 : Math.max(1, Math.round(var46.scale() * (float)var11 * 0.125F));
      int var48 = Math.max(1, Math.round(this.u.lineWidth()));
      int var49 = bg.pAsInt(-401600234, (double)(var9 >>> 24) / 255.0);

      for (int var50 = 0; var50 < 2; var50++) {
         int var42 = var50 == 0 ? var49 : var9;
         float var43 = var50 == 0 ? (float)(var48 + 2) : (float)var48;

         for (d5.Type_w var45 : (Iterable<d5.Type_w>) (var36)) {
            GuiTextureDrawing.dAsVoid(var1, var45.x1(), var45.y1(), var45.x2(), var45.y2(), var43, var42);
         }

         if (var46 != null) {
            GuiTextureDrawing.RAsVoid(var1, Math.round(var46.x()), Math.round(var46.y()), var47 + (var50 == 0 ? 1 : 0), var42);
         }
      }
   }

   private static Vec3d NAsVec3d(Vec3d var0, Vec3d var1, double var2, double var4) {
      return var0.add(var1.multiply(-Math.sin(var4) * var2)).add(0.0, -Math.cos(var4) * var2, 0.0);
   }
}
