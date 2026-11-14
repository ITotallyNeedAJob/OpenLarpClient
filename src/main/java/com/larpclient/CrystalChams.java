package com.larpclient;

import com.larpclient.module.BooleanSetting;
import com.larpclient.module.Category;
import com.larpclient.module.ColorSetting;
import com.larpclient.module.ModeSetting;
import com.larpclient.module.Module;
import com.larpclient.module.NumberSetting;
import com.larpclient.module.Setting;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;

public final class CrystalChams extends Module {
   private static CrystalChams N;
   private final ModeSetting model = this.addSetting(new ModeSetting("Model", "Crystal appearance", "Vanilla", "Vanilla", "Gem", "Orb", "Prism"));
   private final ModeSetting colorMode = this.addSetting(new ModeSetting("Color mode", "Crystal palette", "Theme", "Theme", "Custom", "Rainbow", "Original"));
   private final ColorSetting color = this.addSetting(new ColorSetting("Color", "Custom crystal tint", -1459895));
   private final NumberSetting opacity = this.addSetting(new NumberSetting("Opacity", "Crystal opacity in percent", 100.0, 10.0, 100.0, 5.0));
   private final BooleanSetting glow = this.addSetting(new BooleanSetting("Glow", "Full brightness and luminous core", true));
   private final BooleanSetting rings = this.addSetting(new BooleanSetting("Rings", "Thin orbiting rings", true));
   private final NumberSetting size = this.addSetting(new NumberSetting("Size", "Visual scale in percent", 100.0, 20.0, 300.0, 5.0));
   private final NumberSetting spinRate = this.addSetting(new NumberSetting("Spin rate", "Rotation speed in percent", 100.0, 0.0, 600.0, 10.0));
   private final NumberSetting bounce = this.addSetting(new NumberSetting("Bounce", "Bob height in percent", 100.0, 0.0, 300.0, 10.0));
   private final ModeSetting base = this.addSetting(new ModeSetting("Base", "Base plate visibility", "Default", "Default", "Always", "Never"));
   private CrystalChams.Type_m W;

   public CrystalChams() {
      super("Crystal Chams", "Crystal colors, motion and Gem, Orb or Prism models", Category.RENDER, -1);
      N = this;
      this.color.visibleWhen(() -> this.colorMode.pAsBoolean("Custom"));
      this.rings.visibleWhen(() -> !this.model.pAsBoolean("Vanilla"));
   }

   public static CrystalChams hAsCrystalChams() {
      return N;
   }

   public List<Setting<?>> HAsList() {
      return List.of(this.model, this.size, this.rings, this.base);
   }

   public List<Setting<?>> KAsList() {
      return List.of(this.colorMode, this.color, this.opacity, this.glow);
   }

   public List<Setting<?>> SAsList() {
      return List.of(this.spinRate, this.bounce);
   }

   public boolean yAsBoolean() {
      return this.isEnabled() ? this.isRuntimeActive() && !this.model.pAsBoolean("Vanilla") : false;
   }

   public float SAsFloat() {
      return this.isRuntimeActive() ? this.size.getValue().floatValue() / 100.0F : 1.0F;
   }

   public float xAsFloat() {
      return this.isRuntimeActive() ? this.spinRate.getValue().floatValue() / 100.0F : 1.0F;
   }

   public float kAsFloat() {
      return this.isRuntimeActive() ? this.bounce.getValue().floatValue() / 100.0F : 1.0F;
   }

   public boolean VAsBoolean() {
      return this.isEnabled() ? this.isRuntimeActive() && this.glow.getValue() : false;
   }

   public boolean JAsBoolean() {
      return this.isEnabled() ? this.opacity.getValue() < 100.0 : false;
   }

   public boolean AAsBoolean() {
      return this.isEnabled() ? this.isRuntimeActive() && (!this.colorMode.pAsBoolean("Original") || this.JAsBoolean() || this.glow.getValue()) : false;
   }

   public Boolean xAsBoolean2() {
      return this.isRuntimeActive() && !this.base.pAsBoolean("Default") ? this.base.pAsBoolean("Always") : null;
   }

   public int CAsInt(int var1) {
      double var2 = MediaSession.RAsDouble();
      String var5 = this.colorMode.getValue();

      int var4 = switch (var5) {
         case "Custom" -> this.color.getValue();
         case "Original" -> -1;
         case "Rainbow" -> ColorUtil.HSBtoRGB((float)((var2 * 0.12 + (double)var1 * 0.13) % 1.0), 0.7F, 1.0F);
         default -> ColorUtil.xAsInt(UiStyle.w, UiStyle.Q, 0.5 + 0.5 * Math.sin(var2 + (double)var1 * 0.1));
      };
      return ColorUtil.XAsInt(var4, this.opacity.getValue() / 100.0);
   }

   @Override
   public void onEnable() {
      ClientLog.XAsVoid("CrystalChams", "Native crystal appearance activated: {}", this.model.getValue());
   }

   @Override
   public void onDisable() {
      this.nAsVoid();
   }

   public void nAsVoid() {
      this.W = null;
   }

   public boolean YAsBoolean(Object var1) {
      if (!this.isEnabled()) {
         return false;
      } else {
         CrystalChams.Type_m var2 = this.W;
         return SharedRenderEffects.RAsBoolean() && this.yAsBoolean() && var2 != null && var1 instanceof WidgetJob var3
            ? var2.crystals.stream().anyMatch(var1x -> var1x.id == var3.larpCrystalId())
            : false;
      }
   }

   public void hAsVoid(float var1) {
      if (this.isEnabled()) {
         this.W = null;
         if (this.yAsBoolean() && this.isInGame()) {
            ArrayList var2 = new ArrayList();

            for (Entity var4 : getMc().world.getEntities()) {
               if (var4 instanceof EndCrystalEntity) {
                  EndCrystalEntity var5 = (EndCrystalEntity)var4;
                  if (!(var4.squaredDistanceTo(getMc().player) > 9216.0)) {
                     if (var2.size() >= 128) {
                        break;
                     }

                     float var6 = (float)var5.endCrystalAge + var1;
                     double var7 = Math.sin((double)var6 * 0.2) / 2.0 + 0.5;
                     double var9 = (var7 * var7 + var7) * 0.4 * (double)this.kAsFloat();
                     Vec3d var11 = var5.getLerpedPos(var1);
                     Vec3d var12 = var11.add(0.0, 1.2 + var9, 0.0);
                     int var13 = this.colorMode.pAsBoolean("Original")
                        ? ColorUtil.XAsInt(-3707905, this.opacity.getValue() / 100.0)
                        : this.CAsInt((int)(var11.x * 7.0 + var11.z * 13.0));
                     var2.add(
                        new CrystalChams.Type_j(
                           var5.getId(),
                           var12,
                           var11,
                           var6,
                           Math.toRadians((double)(var6 * 3.0F * this.xAsFloat())),
                           var13,
                           this.xAsBoolean2() == null ? var5.shouldShowBottom() : this.xAsBoolean2()
                        )
                     );
                  }
               }
            }

            this.W = new CrystalChams.Type_m(this.model.getValue(), (double)this.SAsFloat(), this.rings.getValue(), this.glow.getValue(), List.copyOf(var2));
         }
      }
   }

   public boolean nAsBoolean() {
      return this.isEnabled() ? this.W != null && this.W.glow && !this.W.crystals.isEmpty() : false;
   }

   public void TAsVoid(EffectGeometryRenderer var1, boolean var2) {
      if (this.isEnabled()) {
         CrystalChams.Type_m var3 = this.W;
         if (var3 != null && (!var2 || var3.glow)) {
            for (CrystalChams.Type_j var5 : var3.crystals) {
               int var7;
               BrushStroke var8;
               label106: {
                  int var6 = var5.tint;
                  var7 = ColorUtil.xAsInt(var6, ColorUtil.XAsInt(-1, (double)(var6 >>> 24) / 255.0), 0.35);
                  var8 = new BrushStroke(var1, var5.center, new Quaternionf(), var3.scale, var2);
                  if (!var2 && var5.base) {
                     BrushStroke var9 = new BrushStroke(var1, var5.foot, new Quaternionf(), 1.0, false);
                     var9.SAsVoid(0.0, 0.125, 0.0, 0.375, 0.125, 0.375, -14278098);
                     var9.KAsVoid(0.27, 0.012, new Quaternionf(), var6);
                  }

                  String var18 = var3.model;
                  int var16;
                  int var17;
                  BrushStroke var15;
                  switch (var18) {
                     case "Gem":
                        var8.rAsVoid(0.13, 0.24, 0.07, -var5.angle * 1.7, var7);
                        if (!var2) {
                           var8.rAsVoid(0.36, 0.6, 0.22, var5.angle, ColorUtil.pAsInt(var6, 0.85));
                        }
                        break label106;
                     case "Orb":
                        double var24 = 0.85 + 0.15 * Math.sin((double)var5.age * 0.25);
                        var8.LAsVoid(0.0, 0.0, 0.0, 0.15 * var24, 0.15 * var24, 0.15 * var24, var7);
                        if (!var2) {
                           var8.LAsVoid(0.0, 0.0, 0.0, 0.42, 0.42, 0.42, ColorUtil.pAsInt(var6, 0.7));
                           var8.KAsVoid(0.48, 0.03, new Quaternionf().rotationY((float)var5.angle).rotateX(0.35F), ColorUtil.xAsInt(var6, -1459895, 0.4));
                        }
                        break label106;
                     default:
                        float var11 = (float)Math.sqrt(0.5);
                        Quaternionf var12x = new Quaternionf().rotationY((float)var5.angle).rotateAxis((float) (Math.PI / 3), var11, 0.0F, var11);
                        Quaternionf var13 = new Quaternionf(var12x)
                           .mul(new Quaternionf().setAngleAxis((float) (Math.PI / 3), var11, 0.0F, var11).rotateY((float)var5.angle * 1.4F));
                        BrushStroke var14 = new BrushStroke(var1, var5.center, var13, var3.scale, var2);
                        var14.SAsVoid(0.0, 0.0, 0.0, 0.126, 0.126, 0.126, var7);
                        if (var2) {
                           break label106;
                        }

                        var14.SAsVoid(0.0, 0.0, 0.0, 0.225, 0.225, 0.225, ColorUtil.pAsInt(var6, 0.55));
                        var15 = new BrushStroke(var1, var5.center, var12x, var3.scale, false);
                        var15.SAsVoid(0.0, 0.0, 0.0, 0.3, 0.3, 0.3, ColorUtil.pAsInt(var6, 0.5));
                        var16 = ColorUtil.xAsInt(var6, -1459895, 0.55);
                        var17 = 0;
                  }

                  while (var17 < 3) {
                     for (int var18_r2 = -1; var18_r2 <= 1; var18_r2 += 2) {
                        for (int var19 = -1; var19 <= 1; var19 += 2) {
                           double[] var20 = new double[3];
                           double[] var21 = new double[]{0.022, 0.022, 0.022};
                           var20[(var17 + 1) % 3] = (double)var18_r2 * 0.3;
                           var20[(var17 + 2) % 3] = (double)var19 * 0.3;
                           var21[var17] = 0.322;
                           var15.SAsVoid(var20[0], var20[1], var20[2], var21[0], var21[1], var21[2], var16);
                        }
                     }

                     var17++;
                  }
               }

               if (var3.rings) {
                  for (int var23 = 0; var23 < 2; var23++) {
                     var8.KAsVoid(
                        0.62 + (double)var23 * 0.1,
                        0.008,
                        new Quaternionf().rotationY((float)(var5.angle * (var23 == 0 ? 1.6 : -1.2) + (double)var23)).rotateX(0.5F + (float)var23 * 0.6F),
                        ColorUtil.pAsInt(var7, 0.9)
                     );
                  }
               }
            }
         }
      }
   }

   private static record Type_j(int id, Vec3d center, Vec3d foot, float age, double angle, int tint, boolean base) {
   }

   private static record Type_m(String model, double scale, boolean rings, boolean glow, List<CrystalChams.Type_j> crystals) {
   }
}
