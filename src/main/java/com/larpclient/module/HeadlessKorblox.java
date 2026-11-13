package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.la;
import com.larpclient.ms;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.PlayerLikeEntity;
import net.minecraft.entity.player.PlayerEntity;

public final class HeadlessKorblox extends Module {
   public static final int G = -14935272;
   public static final int n = -10178636;
   private static HeadlessKorblox q;
   public final BooleanSetting self = this.addSetting(new BooleanSetting("Self", "Wear it on your own player", true));
   public final BooleanSetting otherPlayers = this.addSetting(new BooleanSetting("Other Players", "Local preview on nearby players; only you see it", false));
   public final BooleanSetting headless = this.addSetting(new BooleanSetting("Headless", "Hide the head and hat layer like Roblox's Headless Horseman", true));
   public final BooleanSetting hideHelmets = this.addSetting(
      new BooleanSetting("Hide Helmets", "Also hide helmets, carved pumpkins and skulls so no head shows; off lets them float like Roblox hats", true)
   );
   public final BooleanSetting korblox = this.addSetting(new BooleanSetting("Korblox", "Swap a leg for the Korblox Deathspeaker bone spike", true));
   public final ModeSetting leg = this.addSetting(
      new ModeSetting("Leg", "Which leg becomes the bone spike; Roblox's is the right", "Right", "Right", "Left", "Both")
   );
   public final NumberSetting length = this.addSetting(
      new NumberSetting(
         "Length", "Spike length; 1 matches the Roblox leg, the longest reaches the foot", 1.0, 0.6, Math.floor(ms.fAsDouble() * 20.0) / 20.0, 0.05
      )
   );
   public final NumberSetting thickness = this.addSetting(new NumberSetting("Thickness", "Spike and hip knob width", 1.0, 0.6, 1.5, 0.05));
   public final ColorSetting hipColor = this.addSetting(new ColorSetting("Hip Color", "Color at the hip knob", -14935272));
   public final ColorSetting tipColor = this.addSetting(new ColorSetting("Tip Color", "Color the bone fades to at its point", -10178636));
   public final NumberSetting tipGlow = this.addSetting(new NumberSetting("Tip Glow", "How brightly the point glows in the dark", 0.3, 0.0, 1.0, 0.05));
   private la U;

   public HeadlessKorblox() {
      super("Headless Korblox", "Go headless and swap a leg for the Korblox bone spike, like the Roblox combo", Category.VISUALS, -1);
      q = this;
      this.hideHelmets.visibleWhen(this.headless::getValue);

      for (Setting var4 : new Setting[]{this.leg, this.length, this.thickness, this.hipColor, this.tipColor, this.tipGlow}) {
         var4.visibleWhen(this.korblox::getValue);
      }

      this.headless.onChanged(var0 -> ClientLog.GAsVoid("Headless", "Headless={}", var0));
      this.hideHelmets.onChanged(var0 -> ClientLog.GAsVoid("Headless", "Hide Helmets={}", var0));
      this.korblox.onChanged(var0 -> ClientLog.GAsVoid("Korblox", "Korblox={}", var0));
   }

   public static HeadlessKorblox lAsHeadlessKorblox() {
      return q;
   }

   @Override
   public void onEnable() {
      ClientLog.GAsVoid(
         "Headless",
         "Enabled; headless={}, hideHelmets={}, korblox={}, leg={}, self={}, others={}",
         this.headless.getValue(),
         this.hideHelmets.getValue(),
         this.korblox.getValue(),
         this.leg.getValue(),
         this.self.getValue(),
         this.otherPlayers.getValue()
      );
   }

   public int MAsInt(PlayerLikeEntity var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.headless.getValue() && this.uAsBoolean(var1)) {
            return this.hideHelmets.getValue() ? 2 : 1;
         } else {
            return 0;
         }
      } else {
         return 0;
      }
   }

   public la kAsLa(PlayerLikeEntity var1) {
      if (!this.isEnabled()) {
         return null;
      } else {
         return this.isRuntimeActive() && this.korblox.getValue() && this.uAsBoolean(var1) ? this.WAsLa() : null;
      }
   }

   public boolean AAsBoolean() {
      return this.isEnabled() ? this.isOperational() && this.self.getValue() && this.headless.getValue() : false;
   }

   public la uAsLa() {
      if (!this.isEnabled()) {
         return null;
      } else {
         return this.isOperational() && this.self.getValue() && this.korblox.getValue() ? this.WAsLa() : null;
      }
   }

   private boolean uAsBoolean(PlayerLikeEntity var1) {
      MinecraftClient var2 = getMc();
      boolean var10000;
      if (var2 != null && var1 == var2.player) {
         var10000 = this.self.getValue();
      } else {
         if (this.otherPlayers.getValue() && var1 instanceof PlayerEntity var3 && !var3.isSpectator()) {
            return true;
         }

         var10000 = false;
      }

      return var10000;
   }

   private la WAsLa() {
      int var1 = this.leg.pAsBoolean("Both") ? 3 : (this.leg.pAsBoolean("Left") ? 2 : 1);
      int var2 = this.hipColor.getValue();
      int var3 = this.tipColor.getValue();
      float var4 = this.tipGlow.getValue().floatValue();
      float var5 = this.length.getValue().floatValue();
      float var6 = this.thickness.getValue().floatValue();
      la var7 = this.U;
      if (var7 == null
         || var7.legs() != var1
         || var7.hip() != var2
         || var7.tip() != var3
         || var7.glow() != var4
         || var7.length() != var5
         || var7.thickness() != var6) {
         this.U = var7 = new la(var1, var2, var3, var4, var5, var6);
      }

      return var7;
   }
}
