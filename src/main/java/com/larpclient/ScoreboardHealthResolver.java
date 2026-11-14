package com.larpclient;

import java.util.Locale;
import java.util.regex.Pattern;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.ReadableScoreboardScore;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardCriterion.RenderType;
import net.minecraft.text.Text;

public final class ScoreboardHealthResolver {
   private static final Pattern AuraModule = Pattern.compile("\\bhp\\b");
   private static final ScoreboardHealthResolver.Type_c K = new ScoreboardHealthResolver.Type_c();
   private static final ScoreboardHealthResolver.Type_c ScreenScale = new ScoreboardHealthResolver.Type_c();
   private static boolean W;

   private ScoreboardHealthResolver() {
   }

   public static float UAsFloat(LivingEntity var0) {
      float var1 = var0.getHealth();
      if (var0 instanceof PlayerEntity var2 && var2 != MinecraftClient.getInstance().player) {
         int var3 = XAsInt(var2);
         if (var3 <= 0) {
            return var1;
         }

         float var4 = Math.max(0.0F, var0.getAbsorptionAmount());
         if (!(Math.abs((double)var3 - Math.ceil((double)var1)) <= 1.0) && !(Math.abs((double)var3 - Math.ceil((double)(var1 + var4))) <= 1.0)) {
            if (!W) {
               W = true;
               ClientLog.GAsVoid("ESP", "Synced health {} disagrees with scoreboard health {}; using scoreboard", var1, var3);
            }

            return Math.min((float)var3, Math.max(1.0F, var0.getMaxHealth()));
         }

         return var1;
      }

      return var1;
   }

   public static double jAsDouble(LivingEntity var0) {
      return (double)Math.clamp(UAsFloat(var0) / Math.max(1.0F, var0.getMaxHealth()), 0.0F, 1.0F);
   }

   private static int XAsInt(PlayerEntity var0) {
      ClientWorld var1 = MinecraftClient.getInstance().world;
      if (var1 == null) {
         return -1;
      } else {
         Scoreboard var2 = var1.getScoreboard();
         int var3 = fAsInt(var2, var2.getObjectiveForSlot(ScoreboardDisplaySlot.BELOW_NAME), K, var0);
         return var3 > 0 ? var3 : fAsInt(var2, var2.getObjectiveForSlot(ScoreboardDisplaySlot.LIST), y, var0);
      }
   }

   private static int fAsInt(Scoreboard var0, ScoreboardObjective var1, ScoreboardHealthResolver.Type_c var2, PlayerEntity var3) {
      if (var1 != null && var2.DAsBoolean(var1)) {
         ReadableScoreboardScore var4 = var0.getScore(var3, var1);
         return var4 == null ? -1 : var4.getScore();
      } else {
         return -1;
      }
   }

   private static final class Type_c {
      private ScoreboardObjective M;
      private Text o;
      private boolean T;

      private Type_c() {
      }

      boolean DAsBoolean(ScoreboardObjective var1) {
         if (var1.getRenderType() == RenderType.HEARTS) {
            return true;
         } else {
            Text var2 = var1.getDisplayName();
            if (var1 == this.M && var2 == this.o) {
               return this.T;
            } else {
               String var3 = var2.getString().toLowerCase(Locale.ROOT);
               this.M = var1;
               this.o = var2;
               this.T = var3.contains("❤") || var3.contains("♥") || var3.contains("health") || ScoreboardHealthResolver.x.matcher(var3).find();
               return this.T;
            }
         }
      }
   }
}
