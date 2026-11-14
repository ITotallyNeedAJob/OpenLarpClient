package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.ParticleEmitter;
import com.larpclient.WeatherRenderer;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.client.option.Perspective;
import net.minecraft.screen.slot.Slot;

public final class GUIAnimation extends Module {
   private static GUIAnimation H;
   private final BooleanSetting f = this.BAsBooleanSetting("Player List", "Slide the player list in and out", true);
   private final BooleanSetting E = this.BAsBooleanSetting("Inventory", "Animate container entrances with aligned input", true);
   private final BooleanSetting c = this.BAsBooleanSetting("Camera Zoom", "Smooth changes in camera field of view", false);
   private final BooleanSetting chunkUpdate = this.addSetting(
      new BooleanSetting("Chunk Update", "Keep vanilla chunk appearance; turn off to animate newly visible chunks", true) {
         @Override
         public String getDisplayName() {
            return "Native Chunks";
         }
      }
   );
   private final BooleanSetting O = this.BAsBooleanSetting("Perspective Change", "Ease camera distance when switching perspective", false);
   private final BooleanSetting v = this.BAsBooleanSetting("Chat Messages", "Ease received chat, typed chat and client search text into view", true);
   private final BooleanSetting F = this.BAsBooleanSetting("Items", "Animate hovered inventory items", true);
   private final ModeSetting chunkAnimation = this.addSetting(
      new ModeSetting("Chunk Animation", "Curve for chunk appearance", "Quart", "Quart", "Circ", "Sine", "Cubic")
   );
   private final NumberSetting speed = this.addSetting(
      new NumberSetting("Speed", "Inventory and chunk entrance duration in tenths of a second", 6.0, 2.0, 10.0, 1.0) {
         @Override
         public String getDisplayName() {
            return "Duration";
         }

         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, "%.1f s", this.getValue() / 10.0);
         }

         @Override
         public String getEditValueAsString() {
            return BigDecimal.valueOf(this.getValue() / 10.0).stripTrailingZeros().toPlainString();
         }

         @Override
         public void setFromEditString(String var1) {
            super.setFromEditString(Double.toString(Double.parseDouble(var1.strip().replace("s", "")) * 10.0));
         }
      }
   );
   private final w2 P = new w2(0.0);
   private final w2 U = new w2(0.0);
   private final Map<Slot, GUIAnimation.Type_v> A = new WeakHashMap<>();
   private Object I;
   private float Y = Float.NaN;
   private long e;

   public GUIAnimation() {
      super("GUI Animation", "Animate menus, chat, camera changes and chunks", Category.RENDER, -1);
      H = this;
      this.chunkAnimation.visibleWhen(() -> !this.chunkUpdate.getValue());
      this.speed.visibleWhen(() -> !this.chunkUpdate.getValue() || this.E.getValue());
   }

   private BooleanSetting BAsBooleanSetting(String var1, String var2, boolean var3) {
      return this.addSetting(new BooleanSetting(var1, var2, var3));
   }

   public static GUIAnimation kAsGUIAnimation() {
      return H;
   }

   public List<Setting<?>> vAsList() {
      return List.of(this.f, this.E, this.v, this.F);
   }

   public List<Setting<?>> XAsList() {
      return List.of(this.c, this.O);
   }

   public List<Setting<?>> qAsList() {
      return List.of(this.chunkUpdate, this.chunkAnimation, this.speed);
   }

   public boolean XAsBoolean() {
      return this.isEnabled() ? this.isRuntimeActive() && this.E.getValue() : false;
   }

   public boolean WAsBoolean() {
      return this.isEnabled() ? this.isRuntimeActive() && this.F.getValue() : false;
   }

   public boolean HAsBoolean() {
      return this.isEnabled() ? this.isRuntimeActive() && this.v.getValue() : false;
   }

   public boolean ZAsBoolean() {
      return this.isEnabled() ? this.isRuntimeActive() && !this.chunkUpdate.getValue() : false;
   }

   public boolean xAsBoolean() {
      if (!this.isEnabled()) {
         return false;
      } else {
         SmoothCamera var1 = SmoothCamera.XAsSmoothCamera();
         return this.isRuntimeActive() && this.O.getValue() && p5.NAsP5_Type_u() == p5.Type_u.NONE && (var1 == null || !var1.isRuntimeActive());
      }
   }

   public float aAsFloat() {
      if (this.isRuntimeActive() && this.f.getValue()) {
         boolean var1 = getMc().options.playerListKey.isPressed();
         return (float)Math.clamp(this.P.bAsDouble(var1 ? 1.0 : 0.0, var1 ? 240 : 190), 0.0, 1.0);
      } else {
         return getMc().options.playerListKey.isPressed() ? 1.0F : 0.0F;
      }
   }

   public boolean pAsBoolean() {
      return this.isEnabled() ? this.isRuntimeActive() && this.f.getValue() : false;
   }

   public long LAsLong() {
      return Math.round(this.speed.getValue() * 100.0);
   }

   public float wAsFloat(long var1) {
      if (this.isEnabled()) {
         if (!this.XAsBoolean()) {
            return 1.0F;
         } else {
            double var3 = Math.clamp((double)(System.nanoTime() - var1) / ((double)this.LAsLong() * 1000000.0), 0.0, 1.0);
            return (float)(0.86 + 0.14 * w2.Type_e.BACK_OUT.zAsDouble(var3));
         }
      } else {
         return 1.0F;
      }
   }

   public double uAsDouble(double var1, double var3, float var5) {
      return var3 + (var1 - var3) / (double)Math.max(0.01F, var5);
   }

   public float MAsFloat(Slot var1, boolean var2) {
      if (this.isEnabled()) {
         long var3 = System.nanoTime();
         GUIAnimation.Type_v var5 = this.A.get(var1);
         float var6 = var5 == null ? 1.0F : var5.scale;
         double var7 = var5 == null ? 0.0 : Math.clamp((double)(var3 - var5.time) * 1.0E-9, 0.0, 0.1);
         float var9 = (float)((double)var6 + ((var2 ? 1.12 : 1.0) - (double)var6) * (1.0 - Math.exp(-(var2 ? 6.5 : 5.0) * var7)));
         this.A.put(var1, new GUIAnimation.Type_v(var9, var3));
         return var9;
      } else {
         return 1.0F;
      }
   }

   public double yAsDouble(double var1) {
      var1 = Math.clamp(var1, 0.0, 1.0);
      String var3 = this.chunkAnimation.getValue();

      return switch (var3) {
         case "Circ" -> Math.sqrt(1.0 - (var1 - 1.0) * (var1 - 1.0));
         case "Sine" -> Math.sin(var1 * Math.PI / 2.0);
         case "Cubic" -> 1.0 - Math.pow(1.0 - var1, 3.0);
         default -> 1.0 - Math.pow(1.0 - var1, 4.0);
      };
   }

   public float LAsFloat(boolean var1) {
      Perspective var2 = getMc().options.getPerspective();
      if (!this.xAsBoolean()) {
         this.I = var2;
         this.U.aAsVoid(var1 ? 1.0 : 0.0);
         return var1 ? 1.0F : 0.0F;
      } else {
         if (this.I == null) {
            this.U.aAsVoid(var1 ? 1.0 : 0.0);
         } else if (this.I != var2 && var1 && this.I != Perspective.FIRST_PERSON) {
            this.U.aAsVoid(0.0);
         }

         this.I = var2;
         return (float)this.U.bAsDouble(var1 ? 1.0 : 0.0, 320);
      }
   }

   public float qAsFloat(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.c.getValue() && p5.NAsP5_Type_u() == p5.Type_u.NONE) {
            long var2 = System.nanoTime();
            if (Float.isNaN(this.Y)) {
               this.Y = var1;
            } else {
               this.Y = this.Y + (var1 - this.Y) * (float)(1.0 - Math.exp(-12.0 * Math.clamp((double)(var2 - this.e) * 1.0E-9, 0.0, 0.1)));
            }

            this.e = var2;
            return this.Y;
         } else {
            this.Y = Float.NaN;
            return var1;
         }
      } else {
         return var1;
      }
   }

   @Override
   public void onEnable() {
      ClientLog.XAsVoid("GUI", "GUI Animation active: chat and typed input={}, native editing remains immediate", this.v.getValue());
   }

   @Override
   public void onDisable() {
      ClientLog.XAsVoid("GUI", "GUI Animation inactive; text motion settles on the next extraction");
      this.P.aAsVoid(0.0);
      this.U.aAsVoid(0.0);
      this.A.clear();
      this.I = null;
      this.Y = Float.NaN;
      this.e = 0L;
   }

   private static record Type_v(float scale, long time) {
   }
}
