package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.db;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundInstance.AttenuationType;
import net.minecraft.client.sound.SoundSystem.PlayResult;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;

public final class HitSounds extends Module {
   private static HitSounds k;
   public static final String[] N = new String[]{
      "Attack Crit",
      "Attack Strong",
      "Attack Weak",
      "Attack Sweep",
      "Attack Knockback",
      "Orb Pickup",
      "Level Up",
      "Totem Use",
      "Anvil Land",
      "Bell",
      "Anime",
      "Blade Ball",
      "Chink",
      "Click",
      "Copper Bell",
      "Crystal",
      "Glass 1",
      "Glass 2",
      "Metal Bat",
      "Neverlose",
      "Punch",
      "Slice",
      "Tap",
      "Water",
      "Rust Headshot"
   };
   private static final String X = "Impact";
   private final ModeSetting sound = this.addSetting(SAsModeSetting("Sound", "Choose the hit sound", "Attack Crit", CAsStringArray(false)));
   private final NumberSetting volume = this.addSetting(
      new NumberSetting(
         "Volume",
         "Custom hit sound loudness; 0% is silent, 100% is original volume, 200% boosts playback. Players and Master sliders also apply",
         1.0,
         0.0,
         2.0,
         0.05
      ) {
         @Override
         public String getEditValueAsString() {
            return Long.toString(Math.round(this.getValue() * 100.0));
         }

         @Override
         public String getValueAsString() {
            return this.getEditValueAsString() + "%";
         }

         @Override
         public void setFromEditString(String var1) {
            this.setClampedValue(Double.valueOf(Double.parseDouble(var1.trim().replace("%", "")) / 100.0));
         }
      }
   );
   private final NumberSetting pitch = this.addSetting(
      new NumberSetting("Pitch", "1 is the original sound; lower is deeper and higher is brighter", 1.0, 0.5, 2.0, 0.05)
   );
   private final BooleanSetting mobSounds = this.addSetting(
      new BooleanSetting("Mob Sounds", "Also play hit sounds for mobs and other non-player entities", true)
   );
   private final BooleanSetting muteNormalHitSounds = this.addSetting(
      new BooleanSetting("Mute normal hit sounds", "Mute vanilla attack impacts, critical hits and sweeps locally; other sounds stay audible", true)
   );
   private static final Set<Identifier> z = Set.of(
      Identifier.ofVanilla("entity.player.attack.strong"),
      Identifier.ofVanilla("entity.player.attack.weak"),
      Identifier.ofVanilla("entity.player.attack.crit"),
      Identifier.ofVanilla("entity.player.attack.sweep"),
      Identifier.ofVanilla("entity.player.attack.knockback"),
      Identifier.ofVanilla("entity.player.attack.nodamage")
   );
   private final NumberSetting pitchRandom = this.addSetting(
      new NumberSetting("Pitch Random", "Random pitch variation around the chosen pitch", 0.0, 0.0, 1.0, 0.05)
   );
   private final BooleanSetting onlyPlayers = this.addSetting(new BooleanSetting("Only Players", "Play feedback only for player targets", false));
   private final BooleanSetting onlyCrit = this.addSetting(new BooleanSetting("Only Crit", "Play feedback only for critical attacks", false));
   private final BooleanSetting randomSound = this.addSetting(new BooleanSetting("Random Sound", "Choose a random sound on each hit", false));
   private final ModeSetting secondSound = this.addSetting(SAsModeSetting("Second Sound", "Layer an additional hit sound", "None", CAsStringArray(true)));
   private PositionedSoundInstance t;
   private long r;
   private PositionedSoundInstance P;
   private ClientWorld B;
   private ClientPlayerEntity S;
   private int Q = Integer.MIN_VALUE;
   private boolean b;
   private boolean F;

   private static String[] CAsStringArray(boolean var0) {
      ArrayList<String> var1 = new ArrayList<>(List.of(N));
      if (var0) {
         var1.addFirst("None");
      } else {
         var1.addAll(List.of("Glass", "Round Hit", "Crisp Tick"));
      }

      return var1.toArray(String[]::new);
   }

   private static ModeSetting SAsModeSetting(String var0, String var1, String var2, String[] var3) {
      return new ModeSetting(var0, var1, var2, var3) {
         @Override
         public boolean acceptsValue(String var1) {
            return "Impact".equals(var1) || super.acceptsValue(var1);
         }

         @Override
         public void setValue(String var1) {
            super.setValue("Impact".equals(var1) ? "Rust Headshot" : var1);
         }
      };
   }

   public HitSounds() {
      super("Hit Sounds", "Play custom sounds when you hit targets", Category.VISUALS, -1);
      k = this;
      this.mobSounds.visibleWhen(() -> !this.onlyPlayers.getValue());
      this.sound.visibleWhen(() -> !this.randomSound.getValue());
      this.muteNormalHitSounds.onChanged(var0 -> ClientLog.GAsVoid("HitSounds", "Vanilla attack sounds muted: {}", var0));
      this.sound.onChanged(var0 -> ClientLog.GAsVoid("HitSounds", "Sound selected: {}", var0));
   }

   public static HitSounds hAsHitSounds() {
      return k;
   }

   public List<Setting<?>> fAsList() {
      return List.of(this.volume, this.sound, this.pitch, this.pitchRandom, this.randomSound, this.secondSound);
   }

   public List<Setting<?>> GAsList() {
      return List.of(this.onlyPlayers, this.onlyCrit, this.muteNormalHitSounds, this.mobSounds);
   }

   public boolean bAsBoolean(SoundInstance var1) {
      return this.isEnabled()
         ? this.isRuntimeActive()
            && this.muteNormalHitSounds.getValue()
            && var1 != null
            && var1 != this.P
            && var1 != this.t
            && (this.F || System.nanoTime() <= this.r)
            && z.contains(var1.getId())
         : false;
   }

   public Identifier dAsIdentifier() {
      return MAsIdentifier(this.sound.getValue());
   }

   public static Identifier MAsIdentifier(String var0) {
      String var1 = switch (var0) {
         case "Attack Crit" -> "entity.player.attack.crit";
         case "Attack Strong" -> "entity.player.attack.strong";
         case "Attack Weak" -> "entity.player.attack.weak";
         case "Attack Sweep" -> "entity.player.attack.sweep";
         case "Attack Knockback" -> "entity.player.attack.knockback";
         case "Orb Pickup" -> "entity.experience_orb.pickup";
         case "Level Up" -> "entity.player.levelup";
         case "Totem Use" -> "item.totem.use";
         case "Anvil Land" -> "block.anvil.land";
         case "Bell" -> "block.bell.use";
         default -> null;
      };
      if (var1 != null) {
         return Identifier.ofVanilla(var1);
      } else {
         String var5 = switch (var0) {
            case "Glass" -> "glass";
            case "Round Hit" -> "round_hit";
            case "Crisp Tick" -> "crisp_tick";
            case "Rust Headshot" -> "impact";
            default -> "clarity_" + var0.toLowerCase(Locale.ROOT).replace(" ", "");
         };
         return Identifier.of("larp-client", "hit." + var5);
      }
   }

   public PositionedSoundInstance dAsPositionedSoundInstance() {
      return this.hAsPositionedSoundInstance(this.sound.getValue());
   }

   private PositionedSoundInstance hAsPositionedSoundInstance(String var1) {
      float var2 = (float)Math.clamp(this.pitch.getValue() + ThreadLocalRandom.current().nextDouble(-1.0, 1.0) * this.pitchRandom.getValue(), 0.5, 2.0);
      return new PositionedSoundInstance(
         MAsIdentifier(var1),
         SoundCategory.PLAYERS,
         this.volume.getValue().floatValue(),
         var2,
         SoundInstance.createRandom(),
         false,
         0,
         AttenuationType.NONE,
         0.0,
         0.0,
         0.0,
         true
      );
   }

   public void IAsVoid(Entity var1) {
      if (this.isEnabled()
         && this.isRuntimeActive()
         && this.isInGame()
         && var1 != null
         && var1 != this.getPlayer()
         && var1.isAlive()
         && !var1.isRemoved()
         && (!this.onlyPlayers.getValue() && this.mobSounds.getValue() || var1 instanceof PlayerEntity)
         && (!this.onlyCrit.getValue() || db.sAsBoolean(this.getPlayer(), var1))) {
         MinecraftClient var2 = getMc();
         this.wAsVoid();
         int var3 = var2.player.age;
         if (this.Q != var3) {
            this.Q = var3;
            this.r = System.nanoTime() + 800000000L;
            if (this.volume.getValue() <= 0.0) {
               this.kAsVoid("Muted by Hit Sounds Volume=0");
            } else {
               try {
                  this.QAsVoid(false);
               } catch (RuntimeException var5) {
                  ClientLog.lAsVoid("HitSounds", "Local sound playback failed; disabling Hit Sounds", var5);
                  this.setEnabled(false);
               }
            }
         }
      }
   }

   public void IAsVoid() {
      MinecraftClient var1 = getMc();
      if (var1 != null) {
         try {
            this.wAsVoid();
            this.QAsVoid(true);
         } catch (RuntimeException var3) {
            ClientLog.lAsVoid("HitSounds", "Sound preview failed; reload resources and retry", var3);
         }
      }
   }

   public void NAsVoid() {
      if (this.F) {
         this.BAsVoid();
      }
   }

   private void QAsVoid(boolean var1) {
      MinecraftClient var2 = getMc();
      if (this.P != null) {
         var2.getSoundManager().stop(this.P);
      }

      if (this.t != null) {
         var2.getSoundManager().stop(this.t);
      }

      String var3 = this.randomSound.getValue() ? N[ThreadLocalRandom.current().nextInt(N.length)] : this.sound.getValue();
      this.P = this.hAsPositionedSoundInstance(var3);
      this.F = var1;
      PlayResult var4 = var2.getSoundManager().play(this.P);
      this.t = this.secondSound.pAsBoolean("None") ? null : this.hAsPositionedSoundInstance(this.secondSound.getValue());
      if (this.t != null) {
         var2.getSoundManager().play(this.t);
      }

      Sound var5 = this.P.getSound();
      String var6 = (var1 ? "Preview" : "Attack received")
         + "; sound="
         + this.dAsIdentifier()
         + ", playback="
         + var4
         + ", moduleVolume="
         + this.volume.getValue()
         + ", Players/Master gain="
         + var2.options.getSoundVolume(SoundCategory.PLAYERS)
         + ", resolved sample="
         + (var5 == null ? "unresolved" : var5.getIdentifier())
         + ", resolved volume="
         + (var5 == null ? 0.0F : this.P.getVolume())
         + ", resolved pitch="
         + (var5 == null ? 0.0F : this.P.getPitch());
      if (var1) {
         ClientLog.GAsVoid("HitSounds", var6);
      } else {
         this.kAsVoid(var6);
      }
   }

   private void kAsVoid(String var1) {
      if (!this.b) {
         this.b = true;
         ClientLog.GAsVoid("HitSounds", var1);
      }
   }

   @Override
   public void onEnable() {
      this.BAsVoid();
      this.b = false;
      ClientLog.GAsVoid(
         "HitSounds", "Local attack feedback enabled: sound={}, reference sample gain, paired local voices, Minecraft Players volume", this.sound.getValue()
      );
   }

   @Override
   public void onTick() {
      this.wAsVoid();
   }

   @Override
   public void onDisable() {
      this.BAsVoid();
   }

   private void wAsVoid() {
      MinecraftClient var1 = getMc();
      if (this.B != var1.world || this.S != var1.player) {
         this.BAsVoid();
         this.B = var1.world;
         this.S = var1.player;
      }
   }

   private void BAsVoid() {
      MinecraftClient var1 = getMc();
      PositionedSoundInstance var2 = this.P;
      if (this.t != null && var1 != null) {
         var1.getSoundManager().stop(this.t);
      }

      this.t = null;
      this.r = 0L;
      this.P = null;
      this.F = false;
      this.B = null;
      this.S = null;
      this.Q = Integer.MIN_VALUE;
      if (var2 != null && var1 != null) {
         var1.getSoundManager().stop(var2);
      }
   }
}
