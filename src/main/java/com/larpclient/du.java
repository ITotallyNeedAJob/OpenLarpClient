package com.larpclient;

import java.util.function.DoubleSupplier;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundInstance.AttenuationType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

public final class du extends MovingSoundInstance {
   private final DoubleSupplier W;
   private final int i;
   private int l = -1;

   public du(EmoteCatalog.Type_a var1, DoubleSupplier var2, int var3) {
      super(SoundEvent.of(var1.music()), SoundCategory.PLAYERS, SoundInstance.createRandom());
      this.W = var2;
      this.i = Math.max(1, var3);
      this.repeat = true;
      this.relative = true;
      this.attenuationType = AttenuationType.NONE;
      this.volume = (float)var2.getAsDouble();
   }

   public void fAsVoid() {
      if (this.l < 0) {
         this.l = this.i;
      }
   }

   public void KAsVoid() {
      this.setDone();
   }

   public void tick() {
      float var1 = (float)Math.clamp(this.W.getAsDouble(), 0.0, 1.0);
      if (this.l >= 0) {
         if (this.l-- <= 0) {
            this.setDone();
            return;
         }

         var1 *= (float)(this.l + 1) / (float)(this.i + 1);
      }

      this.volume = var1;
   }

   public boolean shouldAlwaysPlay() {
      return true;
   }
}
