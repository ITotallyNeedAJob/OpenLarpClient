package com.larpclient;

import com.larpclient.module.BooleanSetting;
import com.larpclient.module.Category;
import com.larpclient.module.ModeSetting;
import com.larpclient.module.Module;
import com.larpclient.module.NoWeather;
import com.larpclient.module.NumberSetting;
import com.larpclient.module.Removals;
import java.nio.ByteBuffer;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.Heightmap.Type;

public final class SnowGround extends Module {
   private static SnowGround l;
   public final NumberSetting I = this.fAsNumberSetting("Coverage", "Snow coverage on exposed ground and roofs, percent", 100.0, 0.0, 100.0, 5.0);
   public final NumberSetting j = this.fAsNumberSetting("Depth", "Soft drift relief and snow along upper edges, percent", 75.0, 0.0, 100.0, 5.0);
   public final NumberSetting G = this.fAsNumberSetting("Sparkle", "Small sunlit ice crystals, percent", 30.0, 0.0, 100.0, 5.0);
   public final BooleanSetting snowfall = this.addSetting(new BooleanSetting("Snowfall", "Falling snow even in clear server weather", true));
   public final NumberSetting R = this.fAsNumberSetting("Density", "Falling snow density, percent", 85.0, 0.0, 100.0, 5.0);
   public final NumberSetting S = this.fAsNumberSetting("Wind", "Sideways drift and rolling gusts, percent", 35.0, 0.0, 100.0, 5.0);
   public final NumberSetting o = this.fAsNumberSetting("Radius", "Snowfall radius around the camera in blocks", 24.0, 12.0, 40.0, 2.0);
   public final ModeSetting quality = this.addSetting(new ModeSetting("Quality", "Snow detail and bounded flake count", "High", "Low", "Medium", "High"));
   public final BooleanSetting footsteps = this.addSetting(new BooleanSetting("Footsteps", "Clear powder beneath you and leave a soft walking trail", true));
   public final NumberSetting Y = this.fAsNumberSetting("Trail Time", "Seconds before walking trails fill back in", 12.0, 2.0, 30.0, 1.0);
   private final wd X = new wd();
   private Vec3d i = Vec3d.ZERO;
   private final SnowOptions T = new SnowOptions();
   private Object z;
   private Object g;
   private int t = -1;
   private int F = -1;

   private NumberSetting fAsNumberSetting(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new NumberSetting(var1, var2, var3, var5, var7, var9));
   }

   public SnowGround() {
      super("Snow Ground", "Deep powder, wind-shaped snow drifts and dense falling snow", Category.RENDER, -1);
      l = this;
      this.R.visibleWhen(this.snowfall::getValue);
      this.S.visibleWhen(this.snowfall::getValue);
      this.o.visibleWhen(this.snowfall::getValue);
      this.Y.visibleWhen(this.footsteps::getValue);
      this.footsteps.onChanged(var1 -> this.X.TAsVoid());
      this.snowfall.onChanged(var1 -> this.T.yAsVoid());
      this.quality.onChanged(var1 -> {
         this.T.yAsVoid();
         WetReflectionEffect.TAsVoid();
      });
      this.o.onChanged(var1 -> this.T.yAsVoid());
   }

   public static boolean hAsBoolean() {
      return l != null && l.isEnabled() ? l != null && l.isRuntimeActive() : false;
   }

   public static SnowGround vAsSnowGround() {
      return l;
   }

   public int pAsInt() {
      return this.quality.pAsBoolean("High") ? 2 : (this.quality.pAsBoolean("Medium") ? 1 : 0);
   }

   public static int xAsInt(int var0) {
      return var0 >= 2 ? 3200 : (var0 == 1 ? 1600 : 800);
   }

   public static boolean rAsBoolean() {
      if (l != null && l.isEnabled()) {
         SnowGround var0 = l;
         return var0 != null
            && hAsBoolean()
            && var0.t != BloomQuality.J
            && var0.snowfall.getValue()
            && var0.R.getValue() > 0.0
            && !NoWeather.iAsBoolean()
            && !Removals.xAsBoolean("weather");
      } else {
         return false;
      }
   }

   public SnowOptions kAsQp() {
      return this.T;
   }

   public void wAsVoid() {
      this.T.yAsVoid();
      this.X.TAsVoid();
      this.i = Vec3d.ZERO;
      this.z = this.g = null;
   }

   public wd WAsWd() {
      return this.X;
   }

   public void DAsVoid(ByteBuffer var1, Vec3d var2, double var3, boolean var5) {
      if (this.isEnabled()) {
         this.X
            .DAsVoid(
               var1, var2, this.i, var3, this.Y.getValue(), var5 && hAsBoolean() && this.footsteps.getValue() && this.z == mc.world && this.g == mc.player
            );
      }
   }

   public void MAsVoid() {
      this.wAsVoid();
      this.t = BloomQuality.J;
   }

   public static boolean FAsBoolean() {
      return l != null && l.isEnabled() ? rAsBoolean() && l.T.cAsInt() > 0 : false;
   }

   @Override
   public void onEnable() {
      this.wAsVoid();
      WetReflectionEffect.gAsVoid();
      WetReflectionEffect.TAsVoid();
      ClientLog.GAsVoid(
         "SnowGround",
         "Enabled powder and snowfall; quality={}, flake cap={}, radius={} blocks, footsteps={}, trail={} seconds",
         this.quality.getValue(),
         xAsInt(this.pAsInt()),
         this.o.getValue(),
         this.footsteps.getValue(),
         this.Y.getValue()
      );
      WetReflectionEffect.FAsVoid(this.pAsInt());
      boolean var1 = NoWeather.iAsBoolean();
      if (this.snowfall.getValue() && (var1 || Removals.xAsBoolean("weather"))) {
         ClientLog.GAsVoid("SnowGround", "Snowfall hidden while {} removes weather; powder and footsteps still draw", var1 ? "NoWeather" : "Removals");
      }
   }

   @Override
   public void onDisable() {
      this.wAsVoid();
      WetReflectionEffect.TAsVoid();
      WetReflectionEffect.OAsVoid();
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() && WetReflectionEffect.fAsBoolean() ? "Material fallback" : "";
   }

   @Override
   public void onTick() {
      if (this.F != BloomQuality.J) {
         this.wAsVoid();
         this.F = BloomQuality.J;
      }

      if (!this.isInGame()) {
         this.wAsVoid();
      } else {
         if (this.z != mc.world || this.g != mc.player) {
            this.wAsVoid();
            this.z = mc.world;
            this.g = mc.player;
         }

         if (!mc.isPaused()) {
            Vec3d var1 = new Vec3d(mc.player.getX(), mc.player.getBoundingBox().minY, mc.player.getZ());
            this.i = var1;
            if (!this.footsteps.getValue()) {
               this.X.TAsVoid();
            } else {
               this.X
                  .vAsVoid(
                     var1,
                     mc.player.isOnGround()
                        && !mc.player.hasVehicle()
                        && !mc.player.isSpectator()
                        && !mc.player.isTouchingWater()
                        && !mc.player.getAbilities().flying,
                     mb.dAsDouble(),
                     this.Y.getValue()
                  );
            }

            if (!rAsBoolean()) {
               this.T.yAsVoid();
            } else {
               this.T
                  .ZAsVoid(
                     mc.gameRenderer.getCamera().getCameraPos(),
                     new SnowOptions.Type_k(this.o.getValue(), this.R.getValue() / 100.0, this.S.getValue() / 100.0, xAsInt(this.pAsInt())),
                     (var0, var1x) -> {
                        if (!mc.world.isChunkLoaded(var0 >> 4, var1x >> 4)) {
                           return SnowOptions.Type_t.UNAVAILABLE;
                        } else {
                           int var2 = mc.world.getTopY(Type.MOTION_BLOCKING, var0, var1x) - 1;
                           BlockPos var3 = new BlockPos(var0, var2, var1x);
                           BlockState var4 = mc.world.getBlockState(var3);
                           VoxelShape var5 = var4.getCollisionShape(mc.world, var3);
                           double var6 = (double)var2 + (!var5.isEmpty() && var4.getFluidState().isEmpty() ? var5.getMax(Axis.Y) : 1.0);
                           int var8 = mc.world.getLightLevel(var3.up());
                           return new SnowOptions.Type_t(var6, (double)var8 / 15.0);
                        }
                     }
                  );
            }
         }
      }
   }

   public void EAsVoid(float var1, CameraRenderState var2) {
      if (this.isEnabled()) {
         if (hAsBoolean() && this.z == mc.world && this.g == mc.player) {
            this.i = mc.player.getCameraPosVec(var1).subtract(0.0, (double)mc.player.getStandingEyeHeight(), 0.0);
            if (!rAsBoolean()) {
               this.T.yAsVoid();
            } else {
               this.T.WAsVoid(var2.pos, var1);
            }
         } else {
            this.wAsVoid();
         }
      }
   }

   public void NAsVoid(EffectGeometryRenderer var1) {
      if (this.isEnabled() && hAsBoolean() && rAsBoolean() && this.z == mc.world && this.g == mc.player) {
         this.T.lAsVoid(var1);
      }
   }
}
