package com.larpclient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;

public final class ParticleEffectPreset {
   public static final List<String> PetEffectRenderer = List.of(
      "Ripple", "Shockwave", "Rune", "Bloom", "Corona", "Vortex", "Spectrum", "Cloud", "End Rod", "Crit", "Flame", "Soul Flame", "Happy Villager"
   );

   public static int JAsInt(String var0) {
      int var1 = PetEffectRenderer.indexOf(var0);
      return var1 >= 0 && var1 < 7 ? var1 : -1;
   }

   public static boolean hAsBoolean(String var0) {
      return PetEffectRenderer.indexOf(var0) >= 7;
   }

   public static boolean MAsBoolean(String var0) {
      return PetEffectRenderer.contains(var0);
   }

   public static ParticleEffect iAsParticleEffect(String var0) {
      return switch (var0) {
         case "Cloud" -> ParticleTypes.CLOUD;
         case "End Rod" -> ParticleTypes.END_ROD;
         case "Crit" -> ParticleTypes.CRIT;
         case "Flame" -> ParticleTypes.FLAME;
         case "Soul Flame" -> ParticleTypes.SOUL_FIRE_FLAME;
         case "Happy Villager" -> ParticleTypes.HAPPY_VILLAGER;
         default -> null;
      };
   }

   public static List<Vec3d> RAsList(Vec3d var0, double var1, int var3) {
      int var4 = Math.clamp((long)var3, 4, 64);
      ArrayList var5 = new ArrayList(var4);

      for (int var6 = 0; var6 < var4; var6++) {
         double var7 = (Math.PI * 2) * (double)var6 / (double)var4;
         var5.add(var0.add(Math.cos(var7) * var1, 0.05, Math.sin(var7) * var1));
      }

      return List.copyOf(var5);
   }

   private ParticleEffectPreset() {
   }

   public static final class Type_o {
      private final ArrayList<ParticleEffectPreset.Type_z> r = new ArrayList<>();

      public Type_o() {
      }

      public void AAsVoid(ParticleEffectPreset.Type_z var1) {
         if (this.r.size() == 6) {
            this.r.removeFirst();
         }

         this.r.add(var1);
      }

      public void nAsVoid(double var1, double var3) {
         this.r.removeIf(var4 -> var1 - var4.born() >= var3);
      }

      public List<ParticleEffectPreset.Type_z> lAsList() {
         return Collections.unmodifiableList(this.r);
      }

      public void pAsVoid() {
         this.r.clear();
      }
   }

   public static record Type_z(Vec3d source, double born, float seed) {
   }
}
