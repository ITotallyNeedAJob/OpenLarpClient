package com.larpclient;

import com.larpclient.module.BooleanSetting;
import com.larpclient.module.ColorSetting;
import com.larpclient.module.ModeSetting;
import com.larpclient.module.NumberSetting;
import com.larpclient.module.Setting;
import java.util.Comparator;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;
import net.minecraft.client.render.DrawStyle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.debug.gizmo.GizmoDrawing;
import net.minecraft.world.debug.gizmo.VisibilityConfigurable;
import net.minecraft.world.debug.gizmo.TextGizmo.Style;

public final class ThemeSettings {
   private final ModeSetting color = new ModeSetting(
      "Color", "Theme follows your live accent colors", "Theme", "Theme", "Category", "Custom", "Distance", "Health", "Rainbow"
   );
   private final ColorSetting customColor = new ColorSetting("Custom Color", "Color used by Custom mode", -4291585);
   private final NumberSetting lineWidth = new NumberSetting("Line Width", "Outline width in pixels", 1.5, 0.5, 4.0, 0.25);
   private final NumberSetting fillOpacity = new NumberSetting("Fill Opacity", "Soft fill for Filled mode", 0.12, 0.0, 0.5, 0.02);
   private final BooleanSetting throughWalls = new BooleanSetting(
      "Through Walls", "Show boxes and lines behind solid blocks; silhouettes always show through", true
   );

   public ThemeSettings(Consumer<Setting<?>> var1) {
      this(var1, true);
   }

   public ThemeSettings(Consumer<Setting<?>> var1, boolean var2) {
      this.customColor.visibleWhen(() -> this.color.pAsBoolean("Custom"));

      for (Setting var4 : List.of(this.color, this.customColor, this.lineWidth, this.throughWalls)) {
         var1.accept(var4);
      }

      if (var2) {
         var1.accept(this.fillOpacity);
      }
   }

   public void DAsVoid(BooleanSupplier var1, BooleanSupplier var2, BooleanSupplier var3, BooleanSupplier var4) {
      this.color.visibleWhen(var1);
      this.customColor.visibleWhen(() -> var1.getAsBoolean() && this.color.pAsBoolean("Custom"));
      this.lineWidth.visibleWhen(var2);
      this.throughWalls.visibleWhen(var3);
      this.fillOpacity.visibleWhen(var4);
   }

   public List<Setting<?>> TAsList() {
      return List.of(this.color, this.customColor);
   }

   public List<Setting<?>> OAsList() {
      return List.of(this.lineWidth, this.throughWalls);
   }

   public List<Setting<?>> qAsList() {
      return List.of(this.fillOpacity);
   }

   public boolean throughWalls() {
      return this.throughWalls.getValue();
   }

   public double fillOpacity() {
      return this.fillOpacity.getValue();
   }

   public float lineWidth() {
      return this.lineWidth.getValue().floatValue();
   }

   public int iAsInt(int var1, double var2, double var4, double var6, int var8) {
      String var10 = this.color.getValue();

      int var9 = switch (var10) {
         case "Category" -> var1;
         case "Custom" -> this.customColor.getValue();
         case "Distance" -> bd.NAsInt(Math.clamp(var2 / var4, 0.0, 1.0));
         case "Health" -> bg.xAsInt(-1017711, -7347266, var6);
         case "Rainbow" -> bg.HSBtoRGB((float)(System.currentTimeMillis() % 6000L) / 6000.0F + (float)var8 * 0.037F, 0.45F, 1.0F);
         default -> bd.NAsInt(0.25);
      };
      return bg.pAsInt(var9, RAsDouble(var2, var4));
   }

   public static double RAsDouble(double var0, double var2) {
      if (!(var2 <= 0.0) && !(var0 >= var2)) {
         double var4 = Math.clamp((var0 / var2 - 0.65) / 0.35, 0.0, 1.0);
         return 1.0 - var4 * var4 * (3.0 - 2.0 * var4);
      } else {
         return 0.0;
      }
   }

   public static List<Entity> jAsList(Iterable<Entity> var0, Entity var1, double var2, int var4, Predicate<Entity> var5) {
      double var6 = var2 * var2;
      return StreamSupport.stream(var0.spliterator(), false)
         .filter(var4x -> var4x != var1 && var4x.isAlive() && var1.squaredDistanceTo(var4x) <= var6 && var5.test(var4x))
         .sorted(Comparator.comparingDouble(var1::squaredDistanceTo))
         .limit((long)var4)
         .toList();
   }

   public static Box GAsBox(Entity var0, float var1) {
      return var0.getBoundingBox().offset(var0.getLerpedPos(var1).subtract(var0.getEntityPos())).expand(0.025);
   }

   private void ZAsVoid(VisibilityConfigurable var1) {
      if (this.throughWalls.getValue()) {
         var1.ignoreOcclusion();
      }
   }

   public void IAsVoid(Vec3d var1, Vec3d var2, int var3) {
      this.ZAsVoid(GizmoDrawing.line(var1, var2, var3, this.lineWidth()));
   }

   public void AAsVoid(Box var1, String var2, int var3) {
      if (var3 >>> 24 != 0) {
         if (var2.equals("Corners")) {
            double var4 = var1.getLengthX() * 0.25;
            double var6 = var1.getLengthY() * 0.18;
            double var8 = var1.getLengthZ() * 0.25;

            for (int var10 = 0; var10 < 2; var10++) {
               for (int var11 = 0; var11 < 2; var11++) {
                  for (int var12 = 0; var12 < 2; var12++) {
                     Vec3d var13 = new Vec3d(var10 == 0 ? var1.minX : var1.maxX, var11 == 0 ? var1.minY : var1.maxY, var12 == 0 ? var1.minZ : var1.maxZ);
                     this.IAsVoid(var13, var13.add(var10 == 0 ? var4 : -var4, 0.0, 0.0), var3);
                     this.IAsVoid(var13, var13.add(0.0, var11 == 0 ? var6 : -var6, 0.0), var3);
                     this.IAsVoid(var13, var13.add(0.0, 0.0, var12 == 0 ? var8 : -var8), var3);
                  }
               }
            }
         } else {
            int var14 = var2.equals("Filled") ? bg.pAsInt(var3, this.fillOpacity.getValue()) : 0;
            this.ZAsVoid(GizmoDrawing.box(var1, DrawStyle.filledAndStroked(var3, this.lineWidth(), var14)));
         }
      }
   }

   public void uAsVoid(Box var1, double var2, int var4) {
      Vec3d var5 = new Vec3d(var1.minX - 0.08, var1.minY, var1.minZ);
      this.ZAsVoid(GizmoDrawing.line(var5, var5.add(0.0, var1.getLengthY(), 0.0), bg.pAsInt(bd.A, (double)(var4 >>> 24) / 255.0), this.lineWidth() + 1.0F));
      int var6 = bg.xAsInt(-1017711, -7347266, var2);
      this.IAsVoid(var5, var5.add(0.0, var1.getLengthY() * Math.clamp(var2, 0.0, 1.0), 0.0), bg.XAsInt(var6, (double)(var4 >>> 24) / 255.0));
   }

   public void NAsVoid(Vec3d var1, String var2, int var3) {
      this.ZAsVoid(GizmoDrawing.text(var2, var1, Style.left(var3).scaled(0.7F)));
   }

   public static double sAsDouble(LivingEntity var0) {
      return ScoreboardHealthResolver.jAsDouble(var0);
   }
}
