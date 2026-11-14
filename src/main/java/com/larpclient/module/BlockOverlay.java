package com.larpclient.module;

import com.larpclient.BloomQuality;
import com.larpclient.ClientLog;
import com.larpclient.EffectGeometryRenderer;
import com.larpclient.SharedRenderEffects;
import com.larpclient.UiStyle;
import com.larpclient.SkyZone;
import com.larpclient.MediaSession;
import com.larpclient.OpenScreen;
import com.larpclient.RibbonHelper;
import com.larpclient.XZombieEffect;
import java.util.List;
import java.util.Locale;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldExtractionContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.state.OutlineRenderState;
import net.minecraft.client.render.state.WorldRenderState;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;

public final class BlockOverlay extends Module {
   private static BlockOverlay m;
   private final NumberSetting transition = this.addSetting(
      new NumberSetting("Transition", "Time to glide to another block, in milliseconds; 0 is instant", 240.0, 0.0, 300.0, 10.0)
   );
   private final ModeSetting colorMode = this.addSetting(
      new ModeSetting("Color Mode", "Use your interface accent or a custom color", "Theme", "Theme", "Custom")
   );
   private final ColorSetting color = this.addSetting(new ColorSetting("Color", "Custom highlight color", -1381654));
   private final NumberSetting outlineOpacity = this.addSetting(vAsNumberSetting("Outline Opacity", "Outline visibility", 0.85, 0.1, 1.0, 0.05));
   private final NumberSetting outlineThickness = this.addSetting(new NumberSetting("Outline Thickness", "Stroke width in blocks", 0.015, 0.005, 0.05, 0.005) {
      @Override
      public String getValueAsString() {
         return String.format(Locale.ROOT, "%.3f", this.getValue());
      }
   });
   private final BooleanSetting fill = this.addSetting(new BooleanSetting("Fill", "Subtle tint over the selected shape", true));
   private final NumberSetting fillOpacity = this.addSetting(vAsNumberSetting("Fill Opacity", "Keep the block texture visible", 0.15, 0.01, 0.3, 0.01));
   private final NumberSetting glow = this.addSetting(vAsNumberSetting("Glow", "Soft edge halo and shared quality-controlled bloom", 1.5, 0.0, 1.5, 0.05));
   private final XZombieEffect u = new XZombieEffect();
   private Object E;
   private Object g;
   private Vec3d d;
   private VoxelShape V;
   private SkyZone D;
   private BlockPos K;
   private int v = -1;
   private boolean y;
   private boolean e;
   private static final RenderStateDataKey<BlockOverlay.Type_d> c = RenderStateDataKey.create(() -> "Larp block overlay");

   public BlockOverlay() {
      super("Block Overlay", "Highlight the block you aim at", Category.VISUALS, -1);
      m = this;
      this.color.visibleWhen(() -> this.colorMode.pAsBoolean("Custom"));
      this.fillOpacity.visibleWhen(this.fill::getValue);
   }

   private static NumberSetting vAsNumberSetting(String var0, String var1, double var2, double var4, double var6, double var8) {
      return new NumberSetting(var0, var1, var2, var4, var6, var8) {
         @Override
         public String getValueAsString() {
            return this.getEditValueAsString() + "%";
         }

         @Override
         public String getEditValueAsString() {
            return Long.toString(Math.round(this.getValue() * 100.0));
         }

         @Override
         public void setFromEditString(String var1) {
            this.setClampedValue(Double.valueOf(Double.parseDouble(var1) / 100.0));
         }
      };
   }

   public static BlockOverlay jAsBlockOverlay() {
      return m;
   }

   public List<Setting<?>> MAsList() {
      return List.of(this.colorMode, this.color, this.outlineOpacity, this.outlineThickness, this.fill, this.fillOpacity, this.glow);
   }

   public List<Setting<?>> vAsList() {
      return List.of(this.transition);
   }

   @Override
   public void onEnable() {
      this.pAsVoid();
      ClientLog.GAsVoid(
         "BlockOverlay", "Selection overlay enabled; cached native shapes, interruptible {} ms transition, shared glow", this.transition.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.pAsVoid();
   }

   public void pAsVoid() {
      this.u.kAsVoid();
      this.D = null;
      this.V = null;
      this.K = null;
      this.E = this.g = null;
      this.d = null;
      this.y = this.e = false;
   }

   public void EAsVoid(WorldExtractionContext var1, HitResult var2) {
      if (this.isEnabled()) {
         WorldRenderState var3 = var1.worldState();
         var3.setData(c, null);
         if (!this.isRuntimeActive()) {
            this.pAsVoid();
         } else {
            MinecraftClient var4 = getMc();
            if (var4.currentScreen == null && !var4.options.hudHidden && var4.isWindowFocused() && var4.player != null && var4.player.isAlive()) {
               Vec3d var5 = var1.camera().getCameraPos();
               if (this.E != var1.world() || this.g != var4.player || this.v != BloomQuality.J || this.d != null && this.d.distanceTo(var5) > 8.0) {
                  this.pAsVoid();
                  this.E = var1.world();
                  this.g = var4.player;
                  this.v = BloomQuality.J;
               }

               this.d = var5;
               if (!this.y) {
                  try {
                     OutlineRenderState var6 = var1.worldState().outlineRenderState;
                     Box var7 = null;
                     this.K = null;
                     if (var6 != null && var2 != null && var2.getType() == Type.BLOCK) {
                        VoxelShape var8 = var6.shape();
                        this.D = this.SAsDz(var8);
                        if (this.D != null) {
                           this.K = var6.pos();
                           var7 = this.D.bounds().offset(this.K);
                        } else if (!this.e) {
                           this.e = true;
                           ClientLog.GAsVoid("BlockOverlay", "Empty or complex selection shape; keeping native outline (limits: {} edges, {} boxes)", 128, 32);
                        }
                     }

                     XZombieEffect.Type_n var11 = this.u.wAsXz_Type_n(var7, MediaSession.RAsDouble(), this.transition.getValue() / 1000.0);
                     BlockOverlay.Type_d var9 = this.D != null && var11.bounds() != null
                        ? new BlockOverlay.Type_d(
                           this.K,
                           this.D,
                           var11.bounds().expand(0.002),
                           var11.alpha(),
                           this.colorMode.pAsBoolean("Theme") ? UiStyle.w : this.color.getValue(),
                           this.outlineOpacity.getValue(),
                           this.outlineThickness.getValue(),
                           this.fill.getValue() ? this.fillOpacity.getValue() : 0.0,
                           this.glow.getValue()
                        )
                        : null;
                     var3.setData(c, var9);
                  } catch (RuntimeException var101) {
                     this.y = true;
                     this.K = null;
                     ClientLog.lAsVoid("BlockOverlay", "Selection extraction failed; native outline retained, retry on resource reload", var101);
                  }
               }
            } else {
               this.pAsVoid();
            }
         }
      }
   }

   private SkyZone SAsDz(VoxelShape var1) {
      if (var1 != this.V) {
         this.D = SkyZone.build(var1);
         this.V = var1;
      }

      return this.D;
   }

   private static BlockOverlay.Type_d VAsBlockOverlay_Type_d(WorldRenderState var0) {
      return (BlockOverlay.Type_d)var0.getData(c);
   }

   public boolean BAsBoolean(BlockPos var1, WorldRenderState var2) {
      if (!this.isEnabled()) {
         return false;
      } else {
         BlockOverlay.Type_d var3 = VAsBlockOverlay_Type_d(var2);
         return this.isRuntimeActive() && !this.y && SharedRenderEffects.RAsBoolean() && var3 != null && var1.equals(var3.selected);
      }
   }

   public boolean QAsBoolean(WorldRenderState var1) {
      if (!this.isEnabled()) {
         return false;
      } else {
         BlockOverlay.Type_d var2 = VAsBlockOverlay_Type_d(var1);
         return this.isRuntimeActive() && var2 != null && var2.alpha > 0.001 && var2.glow > 0.0;
      }
   }

   public void zAsVoid(EffectGeometryRenderer var1, boolean var2, WorldRenderState var3) {
      if (this.isEnabled()) {
         BlockOverlay.Type_d var4 = VAsBlockOverlay_Type_d(var3);
         if (this.isRuntimeActive() && var4 != null && !(var4.alpha < 0.001) && (!var2 || !(var4.glow <= 0.0))) {
            long var5 = OpenScreen.RAsLong();
            RibbonHelper.lAsVoid(var1, var2, var4.geometry, var4.bounds, var4.alpha, var4.color, var4.opacity, var4.width, var4.fillOpacity, var4.glow);
            OpenScreen.aAsVoid(var2 ? "BlockOverlay glow" : "BlockOverlay base", var5);
         }
      }
   }

   private static record Type_d(
      BlockPos selected, SkyZone geometry, Box bounds, double alpha, int color, double opacity, double width, double fillOpacity, double glow
   ) {
   }
}
