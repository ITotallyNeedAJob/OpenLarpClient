package com.larpclient.module;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class Fullbright extends Module {
   private final ModeSetting mode = this.addSetting(new ModeSetting("Mode", "Brightness method", "Gamma", "Gamma", "NightVision"));
   private static boolean a = false;
   private double G = 1.0;
   private String i = null;

   public Fullbright() {
      super("Fullbright", "Brighten dark areas", Category.RENDER, -1);
   }

   @Override
   public void onEnable() {
      a = true;
      this.i = null;
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         String var1 = this.mode.getValue();
         if (!var1.equals(this.i)) {
            this.oAsVoid(this.i);
            this.pAsVoid(var1);
            this.i = var1;
         }

         if ("NightVision".equals(this.i)) {
            mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 400, 0, false, false, false));
         } else if ("Gamma".equals(this.i)) {
            this.DAsVoid(16.0);
         }
      }
   }

   @Override
   public void onDisable() {
      a = false;
      if ("Gamma".equals(this.i)) {
         this.DAsVoid(this.G);
      } else if (this.isInGame()) {
         this.oAsVoid(this.i);
      }

      this.i = null;
   }

   private void pAsVoid(String var1) {
      if ("Gamma".equals(var1)) {
         this.G = (Double)mc.options.getGamma().getValue();
         this.DAsVoid(16.0);
      }
   }

   private void oAsVoid(String var1) {
      if (var1 != null) {
         if ("Gamma".equals(var1)) {
            this.DAsVoid(this.G);
         } else if ("NightVision".equals(var1)) {
            mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
         }
      }
   }

   private void DAsVoid(double var1) {
      mc.options.getGamma().value = var1;
   }

   public static boolean UAsBoolean() {
      return a;
   }
}
