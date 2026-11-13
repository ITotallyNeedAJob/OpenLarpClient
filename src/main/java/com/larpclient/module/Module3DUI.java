package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.p5;
import com.larpclient.w2;
import com.larpclient.xt;
import com.larpclient.mixin.HandledScreenHooksAccessor;
import java.util.Locale;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public final class Module3DUI extends Module {
   private static Module3DUI l;
   private final ModeSetting screens = this.addSetting(new ModeSetting("Screens", "Which menus open in 3D", "Inventory", "Inventory", "All Containers"));
   private final ModeSetting side = this.addSetting(
      new ModeSetting("Side", "Screen side for the menu; the camera moves over the opposite shoulder", "Right", "Right", "Left")
   );
   private final NumberSetting menuAngle = this.addSetting(
      new NumberSetting("Menu Angle", "Degrees the menu turns toward the center of the view", 14.0, 0.0, 45.0, 1.0)
   );
   private final NumberSetting menuScreenHeight = this.addSetting(
      new NumberSetting("Menu Screen Height", "On-screen menu height, tabs included, as a percent of the screen", 76.0, 30.0, 90.0, 1.0)
   );
   private final NumberSetting cameraDistance = this.addSetting(
      new NumberSetting("Camera Distance", "Camera distance behind the player in blocks", 1.1, 0.6, 4.0, 0.1)
   );
   private final NumberSetting cameraOffset = this.addSetting(new NumberSetting("Camera Offset", "Sideways camera offset in blocks", 0.5, 0.0, 1.5, 0.05));
   private final NumberSetting cameraHeight = this.addSetting(
      new NumberSetting("Camera Height", "Camera height relative to the torso in blocks", 0.1, -0.6, 0.6, 0.05)
   );
   private final NumberSetting cameraPitch = this.addSetting(
      new NumberSetting("Camera Pitch", "Camera pitch in degrees; positive looks down", 3.0, -15.0, 40.0, 1.0)
   );
   private final NumberSetting transition = this.addSetting(
      new NumberSetting("Transition", "Camera and menu transition in milliseconds", 320.0, 0.0, 800.0, 10.0)
   );
   private final BooleanSetting dimBackground = this.addSetting(
      new BooleanSetting("Dim Background", "Keep the vanilla darkened backdrop behind the menu", false)
   );
   private final BooleanSetting showHUD = this.addSetting(new BooleanSetting("Show HUD", "Keep the hotbar and HUD visible while a 3D menu is open", true));
   private final ModeSetting playerFacing = this.addSetting(
      new ModeSetting(
         "Player Facing",
         "Where your player turns while a 3D menu is open: its own way, around to look at you, or by Player Turn. Only you see it",
         "Forward",
         "Forward",
         "Camera",
         "Custom"
      )
   );
   private final NumberSetting playerTurn = this.addSetting(
      new NumberSetting("Player Turn", "Degrees your player turns from its own facing; 180 faces the camera", 180.0, -180.0, 180.0, 5.0)
   );
   private final w2 R = new w2(0.0);
   private Perspective I;
   private boolean C;
   private static final double x = 3.5;

   public Module3DUI() {
      super("3D UI", "Third person shoulder view with your inventory in 3D beside you", Category.RENDER, -1);
      l = this;
      this.playerTurn.visibleWhen(() -> this.playerFacing.pAsBoolean("Custom"));
   }

   public static Module3DUI VAsModule3DUI() {
      return l;
   }

   private boolean KAsBoolean() {
      MinecraftClient var1 = getMc();
      ClientPlayerEntity var2 = var1.player;
      return this.isRuntimeActive()
         && var2 != null
         && var1.world != null
         && var2.isAlive()
         && !var2.isSleeping()
         && !var2.isSpectator()
         && !p5.oAsBoolean()
         && !Freelook.HAsBoolean();
   }

   public boolean rAsBoolean(Screen var1) {
      if (!this.isEnabled()) {
         return false;
      } else {
         return var1 instanceof HandledScreen && this.KAsBoolean()
            ? this.screens.pAsBoolean("All Containers") || var1 instanceof InventoryScreen || var1 instanceof CreativeInventoryScreen
            : false;
      }
   }

   private double qAsDouble() {
      boolean var1 = this.rAsBoolean(getMc().currentScreen);
      double var2 = this.R.YAsDouble(var1 ? 1.0 : 0.0, this.transition.getValue().intValue(), w2.Type_e.CUBIC_OUT);
      return Math.clamp(var2, 0.0, 1.0);
   }

   public static void xAsVoid() {
      Module3DUI var0 = l;
      if (var0 != null) {
         var0.BAsVoid();
      }
   }

   private void BAsVoid() {
      if (!this.KAsBoolean()) {
         this.VAsVoid("view unavailable");
         this.R.aAsVoid(0.0);
      } else {
         double var1 = this.qAsDouble();
         if (var1 <= 0.001 && !this.rAsBoolean(getMc().currentScreen)) {
            this.VAsVoid("closed");
         } else {
            GameOptions var3 = getMc().options;
            if (this.I == null) {
               this.I = var3.getPerspective();
               ClientLog.XAsVoid("Camera", "3D UI view engaged from {}", this.I);
            } else if (var3.getPerspective() != Perspective.THIRD_PERSON_BACK) {
               ClientLog.XAsVoid("Camera", "3D UI view yielded to perspective {}", var3.getPerspective());
               this.I = null;
               this.R.aAsVoid(0.0);
               return;
            }

            if (var3.getPerspective() != Perspective.THIRD_PERSON_BACK) {
               var3.setPerspective(Perspective.THIRD_PERSON_BACK);
            }
         }
      }
   }

   private void VAsVoid(String var1) {
      if (this.I != null) {
         GameOptions var2 = getMc().options;
         if (var2.getPerspective() == Perspective.THIRD_PERSON_BACK) {
            var2.setPerspective(this.I);
         }

         ClientLog.XAsVoid("Camera", "3D UI view released ({}), perspective {}", var1, var2.getPerspective());
         this.I = null;
      }
   }

   public Module3DUI.Type_u GAsModule3DUI_Type_u(Vec3d var1, float var2, float var3, Entity var4, float var5, float var6) {
      if (this.isEnabled()) {
         if (this.I != null && var4 != null) {
            double var7 = this.qAsDouble();
            if (var7 <= 0.001) {
               return null;
            } else {
               Vec3d var9 = var4.getLerpedPos(var5);
               Vec3d var10 = this.I.isFirstPerson() ? var9.add(0.0, (double)var6, 0.0) : var1;
               float var11 = var4 instanceof LivingEntity var12 ? var12.getScale() : 1.0F;
               float var28 = var4.getYaw(var5);
               float var13 = this.cameraPitch.getValue().floatValue();
               double var14 = Math.toRadians((double)var28);
               double var16 = Math.toRadians((double)var13);
               Vec3d var18 = new Vec3d(-Math.sin(var14) * Math.cos(var16), -Math.sin(var16), Math.cos(var14) * Math.cos(var16));
               Vec3d var19 = new Vec3d(-Math.cos(var14), 0.0, -Math.sin(var14));
               Vec3d var20 = var9.add(0.0, (double)var4.getHeight() * 0.6 + this.cameraHeight.getValue() * (double)var11, 0.0);
               double var21 = (double)(this.side.pAsBoolean("Right") ? 1 : -1) * this.cameraOffset.getValue() * (double)var11;
               Vec3d var23 = var20.subtract(var18.multiply(this.cameraDistance.getValue() * (double)var11)).add(var19.multiply(var21));
               BlockHitResult var24 = var4.getEntityWorld().raycast(new RaycastContext(var20, var23, ShapeType.VISUAL, FluidHandling.NONE, var4));
               if (var24.getType() != Type.MISS) {
                  Vec3d var25 = var24.getPos().subtract(var20);
                  double var26 = var25.length();
                  var23 = var26 < 1.0E-4 ? var20 : var20.add(var25.multiply(Math.max(0.0, var26 - 0.2) / var26));
               }

               float var29 = (float)var7;
               Vec3d var30 = var10.lerp(var23, (double)var29);
               return new Module3DUI.Type_u(var30.x, var30.y, var30.z, var2 + MathHelper.wrapDegrees(var28 - var2) * var29, MathHelper.lerp(var29, var3, var13));
            }
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private Module3DUI.Type_t nAsModule3DUI_Type_t(Entity var1, float var2) {
      if (this.I != null && !this.playerFacing.pAsBoolean("Forward") && var1 != null && var1 == getMc().player) {
         float var3 = (float)this.qAsDouble();
         if ((double)var3 <= 0.001) {
            return null;
         } else if (this.playerFacing.pAsBoolean("Custom")) {
            return new Module3DUI.Type_t(var1.getYaw(var2) + this.playerTurn.getValue().floatValue(), Float.NaN, var3);
         } else {
            Vec3d var4 = getMc().gameRenderer.getCamera().getCameraPos();
            Vec3d var5 = var1.getCameraPosVec(var2);
            double var6 = var4.x - var5.x;
            double var8 = var4.y - var5.y;
            double var10 = var4.z - var5.z;
            return new Module3DUI.Type_t(
               (float)Math.toDegrees(Math.atan2(-var6, var10)), (float)(-Math.toDegrees(Math.atan2(var8, Math.hypot(var6, var10)))), var3
            );
         }
      } else {
         return null;
      }
   }

   public static void nAsVoid(Entity var0, LivingEntityRenderState var1, float var2) {
      if (l != null && l.isEnabled()) {
         Module3DUI var3 = l;
         Module3DUI.Type_t var4 = var3 == null ? null : var3.nAsModule3DUI_Type_t(var0, var2);
         if (var4 != null) {
            float var5 = var1.bodyYaw;
            float var6 = var5 + var1.relativeHeadYaw;
            float var7 = var4.weight();
            float var8 = var5 + MathHelper.wrapDegrees(var4.yaw() - var5) * var7;
            float var9 = var6 + MathHelper.wrapDegrees(var4.yaw() - var6) * var7;
            var1.bodyYaw = var8;
            var1.relativeHeadYaw = MathHelper.wrapDegrees(var9 - var8);
            if (!Float.isNaN(var4.pitch())) {
               var1.pitch = MathHelper.lerp(var7, var1.pitch, var4.pitch());
            }
         }
      }
   }

   public static float PAsFloat(Entity var0, float var1, float var2) {
      if (l != null && l.isEnabled()) {
         Module3DUI var3 = l;
         Module3DUI.Type_t var4 = var3 == null ? null : var3.nAsModule3DUI_Type_t(var0, var2);
         return var4 == null ? var1 : var1 + MathHelper.wrapDegrees(var4.yaw() - var1) * var4.weight();
      } else {
         return var1;
      }
   }

   public static xt.Type_n jAsXt_Type_n(Screen var0) {
      Module3DUI var1 = l;
      xt.Type_n var2 = var1 == null ? null : var1.OAsXt_Type_n(var0);
      xt.LAsVoid(var0, var2);
      return var2;
   }

   private xt.Type_n OAsXt_Type_n(Screen var1) {
      if (this.I != null && this.rAsBoolean(var1)) {
         double var2 = this.qAsDouble();
         if (var2 <= 0.001) {
            return null;
         } else {
            double[] var4 = xt.vAsDoubleArray2(var1);
            if (var4 == null) {
               HandledScreenHooksAccessor var5 = (HandledScreenHooksAccessor)var1;
               var4 = new double[]{
                  (double)var5.larpLeftPos(),
                  (double)var5.larpTopPos(),
                  (double)(var5.larpLeftPos() + var5.larpImageWidth()),
                  (double)(var5.larpTopPos() + var5.larpImageHeight())
               };
            }

            double var41 = var4[0];
            double var7 = var4[1];
            double var9 = var4[2];
            double var11 = var4[3];
            double var13 = (var41 + var9) / 2.0;
            double var15 = (var7 + var11) / 2.0;
            int var17 = this.side.pAsBoolean("Right") ? 1 : -1;
            double var18 = (double)(-var17) * Math.toRadians(this.menuAngle.getValue());
            double var20 = 3.5 * Math.max(1.0, (var9 - var41) / 2.0);
            double[] var22 = xt.Type_n.ofAsXt_Type_n(var13, var15, 0.0, 0.0, 1.0, var18, var20).bounds(var41, var7, var9, var11);
            double var25 = (double)var1.width;
            double var27 = (double)var1.height;
            double var29 = Math.min(
               Math.min(this.menuScreenHeight.getValue() / 100.0 * var27, var27 - 16.0) / (var22[3] - var22[1]), (var25 * 0.5 - 8.0) / (var22[2] - var22[0])
            );
            double var31 = var17 > 0 ? var25 * 0.71 : var25 * 0.29;
            double var33 = var31 - var29 * (var22[0] + var22[2]) / 2.0;
            var33 = var17 > 0 ? Math.min(var33, var25 - 8.0 - var29 * var22[2]) : Math.max(var33, 8.0 - var29 * var22[0]);
            double var35 = var27 / 2.0 - var29 * (var22[1] + var22[3]) / 2.0;
            if (var2 >= 0.999 && !this.C) {
               this.C = true;
               ClientLog.XAsVoid(
                  "GUI",
                  "3D UI menu settled: {} content {}..{} x {}..{} (measured={}), scale {}, target {} {} in {}x{}",
                  var1.getClass().getSimpleName(),
                  Math.round(var41),
                  Math.round(var9),
                  Math.round(var7),
                  Math.round(var11),
                  xt.vAsDoubleArray2(var1) != null,
                  String.format(Locale.ROOT, "%.2f", var29),
                  Math.round(var33),
                  Math.round(var35),
                  var1.width,
                  var1.height
               );
            } else if (var2 < 0.5) {
               this.C = false;
            }

            double var39 = MathHelper.lerp(var2, 1.0, var29);
            return xt.Type_n.ofAsXt_Type_n(
               var13, var15, MathHelper.lerp(var2, var13, var33), MathHelper.lerp(var2, var15, var35), var39, var18 * var2, var20 * var39
            );
         }
      } else {
         return null;
      }
   }

   public static void BAsVoid(DrawContext var0, int var1, int var2) {
      Module3DUI var3 = l;
      if (var3 != null) {
         float var4 = var3.dimBackground.getValue() ? 1.0F : (float)(1.0 - var3.qAsDouble());
         if (!(var4 <= 0.01F)) {
            var0.fillGradient(0, 0, var1, var2, OAsInt(-1072689136, var4), OAsInt(-804253680, var4));
         }
      }
   }

   private static int OAsInt(int var0, float var1) {
      return Math.round((float)(var0 >>> 24) * var1) << 24 | var0 & 16777215;
   }

   public static boolean WAsBoolean() {
      if (l != null && l.isEnabled()) {
         Module3DUI var0 = l;
         return var0 != null && !var0.showHUD.getValue() && var0.I != null && var0.rAsBoolean(getMc().currentScreen);
      } else {
         return false;
      }
   }

   @Override
   public void onEnable() {
      ClientLog.XAsVoid(
         "Camera",
         "3D UI active: screens={}, side={}, player facing={}{}",
         this.screens.getValue(),
         this.side.getValue(),
         this.playerFacing.getValue(),
         this.playerFacing.pAsBoolean("Custom") ? " " + this.playerTurn.getValue().intValue() + " degrees" : ""
      );
   }

   @Override
   public void onDisable() {
      this.VAsVoid("module inactive");
      this.R.aAsVoid(0.0);
      xt.LAsVoid(null, null);
   }

   private static record Type_t(float yaw, float pitch, float weight) {
   }

   public static record Type_u(double x, double y, double z, float yaw, float pitch) {
   }
}
