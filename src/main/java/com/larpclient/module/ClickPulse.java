package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.HudRenderer;
import com.larpclient.RoundedShapePipeline;
import com.larpclient.UiStyle;
import java.util.ArrayDeque;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public final class ClickPulse extends Module {
   private static final int z = 12;
   private static ClickPulse q;
   private final ModeSetting trigger = this.addSetting(new ModeSetting("Trigger", "Which clicks start a pulse", "Both", "Both", "Left", "Right", "On Hit"));
   private final NumberSetting size = this.addSetting(new NumberSetting("Size", "Final ring radius", 14.0, 6.0, 40.0, 1.0));
   private final NumberSetting duration = this.addSetting(new NumberSetting("Duration", "Milliseconds until the ring fades out", 350.0, 120.0, 1000.0, 10.0));
   private final NumberSetting thickness = this.addSetting(new NumberSetting("Thickness", "Ring width in screen pixels", 2.0, 2.0, 8.0, 2.0));
   private final BooleanSetting themeColor = this.addSetting(new BooleanSetting("Theme Color", "Follow the interface accent color", true));
   private final ColorSetting color = this.addSetting(new ColorSetting("Color", "Ring color and opacity", -1));
   private final ArrayDeque<Long> H = new ArrayDeque<>();

   public ClickPulse() {
      super("Click Pulse", "A ring pulses out of the crosshair when you click", Category.RENDER, -1);
      q = this;
      this.color.visibleWhen(() -> !this.themeColor.getValue());
      this.trigger.onChanged(var1 -> {
         this.H.clear();
         ClientLog.GAsVoid("ClickPulse", "Trigger selected: {}", var1);
      });
      HudElementRegistry.addLast(
         Identifier.of("larp-client", "clickpulse"),
         (var1, var2) -> {
            if (!this.H.isEmpty()) {
               MinecraftClient var3 = getMc();
               HudRenderer.wAsVoid(var1);
               if (this.isOperational()
                  && this.isInGame()
                  && !var3.options.hudHidden
                  && var3.options.getPerspective().isFirstPerson()
                  && (var3.currentScreen == null || var3.currentScreen.getClass() == ChatScreen.class)) {
                  int var4 = var3.getWindow().getScaledWidth();
                  int var5 = var3.getWindow().getScaledHeight();
                  HudRenderer.Type_m var6 = HudRenderer.QAsLp_Type_m("crosshair");
                  if (CustomCrosshair.qAsBoolean() && !var6.EAsBoolean()) {
                     var6.PAsVoid(var4, var5);
                     var6.uAsVoid(var1, var6.PAsInt(var4), var6.ZAsInt(var5));

                     try {
                        this.fAsVoid(var1, (double)(var6.PAsInt(var4) + var6.v / 2), (double)(var6.ZAsInt(var5) + var6.y / 2));
                     } finally {
                        var6.IAsVoid(var1);
                     }
                  } else {
                     this.fAsVoid(var1, (double)((var4 - 15) / 2) + 7.5, (double)((var5 - 15) / 2) + 7.5);
                  }
               } else {
                  this.H.clear();
               }
            }
         }
      );
   }

   public static ClickPulse zAsClickPulse() {
      return q;
   }

   @Override
   public void onEnable() {
      this.H.clear();
   }

   @Override
   public void onDisable() {
      this.H.clear();
   }

   public static void HAsVoid(int var0) {
      if (q != null && q.isEnabled()) {
         ClickPulse var1 = q;
         if (var1 != null
            && var0 <= 1
            && !var1.trigger.pAsBoolean("On Hit")
            && (!var1.trigger.pAsBoolean("Left") || var0 == 0)
            && (!var1.trigger.pAsBoolean("Right") || var0 == 1)) {
            var1.SAsVoid();
         }
      }
   }

   public void hAsVoid(Entity var1) {
      if (this.isEnabled() && var1 != null && this.trigger.pAsBoolean("On Hit")) {
         this.SAsVoid();
      }
   }

   private void SAsVoid() {
      if (this.isOperational() && getMc().player != null && getMc().currentScreen == null) {
         if (this.H.size() >= 12) {
            this.H.removeFirst();
         }

         this.H.addLast(System.nanoTime());
      }
   }

   private int tAsInt() {
      return this.themeColor.getValue() ? bd.w | 0xFF000000 : this.color.getValue();
   }

   private void fAsVoid(DrawContext var1, double var2, double var4) {
      long var6 = System.nanoTime();
      double var8 = this.duration.getValue() * 1000000.0;

      while (!this.H.isEmpty() && (double)(var6 - this.H.peekFirst()) >= var8) {
         this.H.removeFirst();
      }

      int var10 = this.tAsInt();
      int var11 = var10 >>> 24;
      if (!this.H.isEmpty() && var11 != 0) {
         double var12 = Math.hypot((double)var1.getMatrices().m00(), (double)var1.getMatrices().m01()) * (double)getMc().getWindow().getScaleFactor();
         int var14 = Math.max(1, this.thickness.getValue().intValue() / 2);
         double var15 = 3.0 * var12;
         double var17 = this.size.getValue() * var12;
         var1.getMatrices().pushMatrix();

         try {
            var1.getMatrices().translate((float)var2, (float)var4);
            var1.getMatrices().scale((float)(1.0 / var12));

            for (long var20 : this.H) {
               double var22 = Math.clamp((double)(var6 - var20) / var8, 0.0, 1.0);
               double var24 = 1.0 - Math.pow(1.0 - var22, 3.0);
               int var26 = (int)Math.round((double)var11 * (1.0 - var22) * (1.0 - var22));
               if (var26 > 0) {
                  int var27 = var26 << 24 | var10 & 16777215;
                  int var28 = (int)Math.round(var15 + (var17 - var15) * var24);

                  for (int var29 = 0; var29 < var14; var29++) {
                     int var30 = var28 - var29 * 2;
                     if (var30 < 2) {
                        break;
                     }

                     RoundedShapePipeline.cAsVoid(var1, (float)(-var30), (float)(-var30), (float)(2 * var30), (float)(2 * var30), (float)var30, var27);
                  }
               }
            }
         } finally {
            var1.getMatrices().popMatrix();
         }
      }
   }
}
