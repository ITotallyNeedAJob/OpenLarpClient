package com.larpclient;

import com.larpclient.module.BooleanSetting;
import com.larpclient.module.ColorSetting;
import com.larpclient.module.ModeSetting;
import com.larpclient.module.Module;
import com.larpclient.module.NumberSetting;
import com.larpclient.module.Setting;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldExtractionContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.state.WorldRenderState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShapes;

public final class PlacementPreview {
   private static final SkyZone j = SkyZone.build(VoxelShapes.fullCube());
   private final Module x;
   private final BooleanSetting highlight = new BooleanSetting("Highlight", "Show the next placement with a soft block outline", true);
   private final ModeSetting highlightColorMode = new ModeSetting(
      "Highlight Color Mode", "Follow your theme or choose a custom color", "Theme", "Theme", "Custom"
   );
   private final ColorSetting highlightColor;
   private final NumberSetting C;
   private final NumberSetting highlightThickness;
   private final BooleanSetting highlightFill;
   private final NumberSetting g;
   private final NumberSetting d;
   private final NumberSetting s;
   private final NumberSetting b;
   private final List<Setting<?>> I;
   private final WeaponTrail B;
   private final RenderStateDataKey<PlacementPreview.Type_f> A;
   private BlockPos l;
   private Object a;
   private Object O;
   private Object r;
   private Object H;
   private int L;
   private int w;
   private Vec3d M;

   public PlacementPreview(Module var1, Consumer<Setting<?>> var2) {
      this(var1, var2, -1.0, -1.0);
   }

   public PlacementPreview(Module var1, Consumer<Setting<?>> var2, double var3, double var5) {
      this.highlightColor = new ColorSetting("Highlight Color", "Custom placement highlight color", UiStyle.w);
      this.C = hAsNumberSetting("Highlight Opacity", "Outline visibility", 0.85, 0.1, 1.0, 0.05);
      this.highlightThickness = new NumberSetting("Highlight Thickness", "Outline width in blocks", 0.015, 0.005, 0.04, 0.005) {
         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, "%.3f", this.getValue());
         }
      };
      this.highlightFill = new BooleanSetting("Highlight Fill", "Subtle tint inside the placement box", true);
      this.g = hAsNumberSetting("Highlight Fill Opacity", "Keep the scene visible through the fill", 0.08, 0.01, 0.3, 0.01);
      this.d = hAsNumberSetting("Highlight Glow", "Soft edge halo and shared quality-controlled bloom", 0.65, 0.0, 1.5, 0.05);
      this.B = new WeaponTrail();
      this.w = -1;
      this.x = var1;
      this.s = var3 < 0.0 ? null : kAsNumberSetting("Highlight Hold", "Seconds to retain the previous placement box", var3, 0.0, 3.0);
      this.b = var5 < 0.0 ? null : kAsNumberSetting("Highlight Fade", "Seconds for the previous box to fade out", var5, 0.1, 2.0);
      ArrayList var7 = new ArrayList<>(
         List.of(this.highlight, this.highlightColorMode, this.highlightColor, this.C, this.highlightThickness, this.highlightFill, this.g, this.d)
      );
      if (this.s != null) {
         var7.add(this.s);
         var7.add(this.b);
      }

      this.I = List.copyOf(var7);
      this.A = RenderStateDataKey.create(() -> var1.getName() + " placement highlight");
      this.I.forEach(var2);

      for (Setting var9 : this.I) {
         if (var9 != this.highlight) {
            var9.visibleWhen(this.highlight::getValue);
         }
      }

      this.highlightColor.visibleWhen(() -> this.highlight.getValue() && this.highlightColorMode.pAsBoolean("Custom"));
      this.g.visibleWhen(() -> this.highlight.getValue() && this.highlightFill.getValue());
      this.highlight.onChanged(var1x -> this.rAsVoid());
   }

   public boolean highlight() {
      return this.highlight.getValue();
   }

   public String tAsString() {
      return this.x.getName() + " highlight";
   }

   public List<Setting<?>> BAsList() {
      return this.I;
   }

   public void mAsVoid(BlockPos var1) {
      MinecraftClient var2 = MinecraftClient.getInstance();
      this.l = this.highlight.getValue() && var1 != null ? var1.toImmutable() : null;
      this.a = var2.world;
      this.O = var2.player;
      this.L = var2.player == null ? 0 : var2.player.age;
   }

   public void rAsVoid() {
      this.l = null;
      this.a = this.O = this.r = this.H = null;
      this.M = null;
      this.B.gAsVoid();
   }

   public void SAsVoid() {
      this.rAsVoid();
      ClientLog.GAsVoid(
         this.x.getName(),
         "Placement highlight {}; theme/custom color, world-fixed fades, shared depth-tested glow",
         this.highlight.getValue() ? "enabled" : "disabled"
      );
   }

   public void AAsVoid(WorldExtractionContext var1) {
      WorldRenderState var2 = var1.worldState();
      var2.setData(this.A, null);
      MinecraftClient var3 = MinecraftClient.getInstance();
      if (this.x.isRuntimeActive()
         && this.highlight.getValue()
         && var3.player != null
         && var3.player.isAlive()
         && var3.currentScreen == null
         && !var3.options.hudHidden
         && var3.isWindowFocused()) {
         Vec3d var4 = var1.camera().getCameraPos();
         int var5 = this.M != null && this.M.distanceTo(var4) > 8.0 ? 1 : 0;
         if (this.r != var1.world() || this.H != var3.player || this.w != BloomQuality.J || var5 != 0) {
            this.B.gAsVoid();
            if (var5 != 0) {
               this.l = null;
            }

            this.r = var1.world();
            this.H = var3.player;
            this.w = BloomQuality.J;
         }

         this.M = var4;
         BlockPos var6 = BlockPlacementController.rAsBoolean(this.a, var3.world, this.O, var3.player, this.L, var3.player.age) ? this.l : null;
         List var7 = this.B.ZAsList(var6, MediaSession.RAsDouble(), this.s == null ? 0.06 : this.s.getValue(), this.b == null ? 0.18 : this.b.getValue());
         if (!var7.isEmpty()) {
            var2.setData(
               this.A,
               new PlacementPreview.Type_f(
                  var7,
                  this.highlightColorMode.pAsBoolean("Theme") ? UiStyle.w : this.highlightColor.getValue(),
                  this.C.getValue(),
                  this.highlightThickness.getValue(),
                  this.highlightFill.getValue() ? this.g.getValue() : 0.0,
                  this.d.getValue()
               )
            );
         }
      } else {
         this.rAsVoid();
      }
   }

   private PlacementPreview.Type_f aAsA3_Type_f(WorldRenderState var1) {
      return (PlacementPreview.Type_f)var1.getData(this.A);
   }

   public boolean EAsBoolean(WorldRenderState var1) {
      PlacementPreview.Type_f var2 = this.aAsA3_Type_f(var1);
      return this.x.isRuntimeActive() && this.highlight.getValue() && var2 != null && var2.glow > 0.0;
   }

   public void gAsVoid(EffectGeometryRenderer var1, boolean var2, WorldRenderState var3) {
      PlacementPreview.Type_f var4 = this.aAsA3_Type_f(var3);
      if (this.x.isRuntimeActive() && this.highlight.getValue() && var4 != null && (!var2 || !(var4.glow <= 0.0))) {
         long var5 = OpenScreen.RAsLong();

         for (WeaponTrail.Type_s var8 : var4.boxes) {
            RibbonHelper.lAsVoid(var1, var2, j, new Box(var8.pos()).expand(0.002), var8.alpha(), var4.color, var4.opacity, var4.width, var4.fillOpacity, var4.glow);
         }

         OpenScreen.aAsVoid(this.x.getName() + (var2 ? " highlight glow" : " highlight base"), var5);
      }
   }

   private static NumberSetting kAsNumberSetting(String var0, String var1, double var2, double var4, double var6) {
      return new NumberSetting(var0, var1, var2, var4, var6, 0.05) {
         @Override
         public String getValueAsString() {
            return this.getEditValueAsString() + " s";
         }
      };
   }

   private static NumberSetting hAsNumberSetting(String var0, String var1, double var2, double var4, double var6, double var8) {
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

   public interface Type_e {
      PlacementPreview placementHighlight();
   }

   private static record Type_f(List<WeaponTrail.Type_s> boxes, int color, double opacity, double width, double fillOpacity, double glow) {
   }
}
