package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.EmoteCatalog;
import com.larpclient.EmoteWheelScreen;
import com.larpclient.EmotecraftJsonReader;
import com.larpclient.KeybindNames;
import com.larpclient.DurationTracker;
import com.larpclient.OptionPanel;
import com.larpclient.ParticleEmitter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.sound.SoundSystem.PlayResult;
import net.minecraft.entity.PlayerLikeEntity;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.Vec3d;

public final class Emotes extends Module {
   private static final int G = 3;
   private static final int c = 4;
   private static Emotes T;
   public final KeybindSetting wheelKey = this.addSetting(new KeybindSetting("Wheel Key", "Hold to open the emote wheel, release over an emote to play it", 66));
   private final ModeSetting[] slot4 = new ModeSetting[]{
      this.addSetting(new ModeSetting("Slot 1", "Top emote on the wheel", "Take the L", EmoteCatalog.wAsStringArray())),
      this.addSetting(new ModeSetting("Slot 2", "Right emote on the wheel", "Floss", EmoteCatalog.wAsStringArray())),
      this.addSetting(new ModeSetting("Slot 3", "Bottom emote on the wheel", "Maybach Bounce", EmoteCatalog.wAsStringArray())),
      this.addSetting(new ModeSetting("Slot 4", "Left emote on the wheel", "Money Spread", EmoteCatalog.wAsStringArray()))
   };
   public final BooleanSetting thirdPerson = this.addSetting(
      new BooleanSetting("Third Person", "Switch to third person while an emote plays, then switch back", true)
   );
   public final NumberSetting x = this.addSetting(
      new NumberSetting(
         "Music Volume",
         "Loudness of your emote songs from the emote music resource pack; 0% is silent. Players and Master sliders also apply",
         0.3,
         0.0,
         1.0,
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
   private EmotecraftJsonReader I;
   private EmoteCatalog.Type_a q;
   private String K = "";
   private int Q;
   private int U;
   private boolean s;
   private boolean n;
   private boolean f;
   private Perspective N = Perspective.FIRST_PERSON;
   private Perspective L = Perspective.THIRD_PERSON_BACK;
   private final float[] w;
   private float y;
   private int O;
   private Object z;
   private Object v;
   private DurationTracker m;
   public static boolean B;
   private static final String[] a = new String[]{"none", "none", "Emotes", "Playing {} from slot {} (camera switch={}, prop={})"};

   public Emotes() {
      super("Emotes", "Hold a key to pick an emote from a wheel, like Fortnite", Category.RENDER, -1);
      this.w = new float[EmotecraftJsonReader.O];
      T = this;
   }

   public static Emotes gAsEmotes() {
      return T;
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(k.RENDER_ONLY);
   }

   public List<String> TAsList() {
      return Arrays.stream(this.slot4).map(Setting::getValue).toList();
   }

   public int wheelKey() {
      return this.wheelKey.getValue();
   }

   public boolean qAsBoolean() {
      return this.isEnabled() ? this.I != null && !this.s : false;
   }

   public String TAsString() {
      return this.qAsBoolean() ? this.K : "";
   }

   @Override
   public void onEnable() {
      this.n = this.QAsBoolean();
      ClientLog.XAsVoid("Emotes", "Ready; wheel key={}, slots={}", this.wheelKey.getValue(), this.TAsList());
   }

   @Override
   public void onDisable() {
      if (getMc().currentScreen instanceof EmoteWheelScreen var1) {
         var1.close();
      }

      this.mAsVoid("module disabled");
   }

   @Override
   public void onTick() {
      MinecraftClient var1 = getMc();
      byte var2 = (byte)(this.QAsBoolean() ? 1 : 0);
      if (var2 != 0
         && !this.n
         && var1.currentScreen == null
         && var1.getOverlay() == null
         && var1.isWindowFocused()
         && var1.player != null
         && var1.player.isAlive()) {
         var1.setScreen(new EmoteWheelScreen(this));
         ClientLog.XAsVoid("Emotes", "Wheel opened");
      }

      this.n = var2 != 0;
      this.RAsVoid();
   }

   private boolean QAsBoolean() {
      MinecraftClient var1 = getMc();
      return var1.getWindow() != null && KeybindNames.sAsBoolean(var1.getWindow().getHandle(), this.wheelKey.getValue());
   }

   public void IAsVoid(int var1) {
      if (this.isEnabled() && var1 >= 0 && var1 < this.slot4.length && this.isRuntimeActive() && this.isInGame()) {
         EmoteCatalog.Type_a var2 = EmoteCatalog.LAsA2_Type_a(this.slot4[var1].getValue());
         if (var2 != null) {
            EmotecraftJsonReader var3 = EmoteCatalog.pAsNh(var2);
            if (var3 != null) {
               MinecraftClient var4 = getMc();
               ClientPlayerEntity var5 = var4.player;
               this.I = var3;
               this.q = var2;
               this.K = var2.label();
               this.Q = this.U = 0;
               this.s = false;
               this.z = var5;
               this.v = var4.world;
               this.O = var5.hurtTime;
               this.y = var5.getYaw();
               this.yAsVoid(var2);
               this.kAsVoid(var2);
               String[] var6 = a;
               String var10000 = var6[2];
               String var10001 = var6[3];
               Object[] var10002 = new Object[]{this.K, (var1 | 1) + (var1 & 1), null, null};
               Object var10005;
               if (this.f) {
                  var10005 = this.L;
               } else {
                  var6 = a;
                  var10005 = var6[0];
               }

               var10002[2] = var10005;
               var10002[3] = var2.prop() == null ? a[1] : var2.prop();
               ClientLog.GAsVoid(var10000, var10001, var10002);
            }
         }
      }
   }

   private void kAsVoid(EmoteCatalog.Type_a var1) {
      GameOptions var2 = getMc().options;
      Perspective var3 = var2.getPerspective();
      Perspective var4 = var1.prop() != null ? Perspective.THIRD_PERSON_FRONT : Perspective.THIRD_PERSON_BACK;
      int var5 = this.f && var3 == this.L ? 1 : 0;
      int var6 = var3.isFirstPerson() || var5 != 0 || var1.prop() != null && var3 == Perspective.THIRD_PERSON_BACK ? 1 : 0;
      if (this.thirdPerson.getValue() && ParticleEmitter.NAsP5_Type_u() == ParticleEmitter.Type_u.NONE && var6 != 0 && var3 != var4) {
         if (var5 == 0) {
            this.N = var3;
         }

         var2.setPerspective(var4);
         this.f = true;
         this.L = var4;
      }
   }

   public void kAsVoid(String var1) {
      if (this.I != null && !this.s) {
         this.s = true;
         this.U = 0;
         if (this.m != null) {
            this.m.fAsVoid();
         }

         this.NAsVoid();
         ClientLog.GAsVoid("Emotes", "Stopped {} after {} ticks: {}", this.K, this.Q, var1);
      }
   }

   private void mAsVoid(String var1) {
      if (this.I != null && !this.s) {
         ClientLog.GAsVoid("Emotes", "Stopped {} after {} ticks: {}", this.K, this.Q, var1);
      }

      this.NAsVoid();
      this.iAsVoid();
      this.I = null;
      this.q = null;
      this.K = "";
      this.s = false;
      this.z = this.v = null;
   }

   private void yAsVoid(EmoteCatalog.Type_a var1) {
      this.iAsVoid();
      if (!(this.x.getValue() <= 0.0)) {
         if (getMc().getSoundManager().get(var1.music()) == null) {
            ClientLog.XAsVoid("Emotes", "No music for {}: add {} with the emote music resource pack", var1.label(), var1.music());
         } else {
            DurationTracker var2 = new DurationTracker(var1, this.x::getValue, 4);

            try {
               PlayResult var3 = getMc().getSoundManager().play(var2);
               this.m = var2;
               ClientLog.XAsVoid("Emotes", "Music {} started at {}% volume: {}", var1.music(), Math.round(this.x.getValue() * 100.0), var3);
            } catch (RuntimeException var41) {
               ClientLog.hAsVoid("Emotes", "Emote music failed to start; the dance plays silently, reload resources to retry", var41);
            }
         }
      }
   }

   private void iAsVoid() {
      if (this.m != null) {
         DurationTracker var1 = this.m;
         this.m = null;

         try {
            getMc().getSoundManager().stop(var1);
         } catch (RuntimeException var3) {
            ClientLog.hAsVoid("Emotes", "Emote music stop failed; it ends with the sound engine", var3);
         }
      }
   }

   private void NAsVoid() {
      GameOptions var1 = getMc().options;
      if (this.f && var1.getPerspective() == this.L) {
         var1.setPerspective(this.N);
      }

      this.f = false;
   }

   private void RAsVoid() {
      if (this.I != null) {
         MinecraftClient var1 = getMc();
         ClientPlayerEntity var2 = var1.player;
         if (var2 == null || var2 != this.z || var1.world != this.v || !var2.isAlive()) {
            this.mAsVoid("world or player changed");
         } else if (this.s) {
            if (++this.U >= 4) {
               this.mAsVoid("blend finished");
            }
         } else {
            this.Q++;
            String var3 = this.MAsString(var2);
            if (var3 != null) {
               this.kAsVoid(var3);
            } else if (this.I.XAsBoolean(this.Q)) {
               this.kAsVoid("emote finished");
            }
         }
      }
   }

   private String MAsString(ClientPlayerEntity var1) {
      PlayerInput var2 = var1.input.playerInput;
      if (var2.forward() || var2.backward() || var2.left() || var2.right()) {
         return "movement input";
      } else if (var2.jump()) {
         return "jump";
      } else if (var2.sneak()) {
         return "sneak";
      } else {
         int var3 = var1.hurtTime;
         int var4 = var3 > this.O ? 1 : 0;
         this.O = var3;
         if (var4 != 0) {
            return "took damage";
         } else if (!var1.handSwinging && !var1.isUsingItem()) {
            return !var1.hasVehicle() && !var1.isGliding() && !var1.isSwimming() && !var1.isSleeping() && !var1.getAbilities().flying ? null : "pose changed";
         } else {
            return "attack or item use";
         }
      }
   }

   public static void gAsVoid(PlayerLikeEntity var0, PlayerEntityRenderState var1, float var2) {
      if (T != null && T.isEnabled()) {
         OptionPanel var3 = (OptionPanel)var1;
         Emotes var4 = T;
         if (var4 != null && var4.I != null && var4.isRuntimeActive() && var0 == getMc().player) {
            float var5 = var4.ZAsFloat(var2);
            var3.larp$setEmoteWeight(var5);
            if (!(var5 <= 0.0F)) {
               int var6 = var4.Q;
               float var7 = var4.s ? 0.0F : var2;
               var4.I.aAsVoid(var6, var7, var3.larp$emoteSample());
               var3.larp$setEmoteProp(var4.q.prop(), var4.I.bAsFloat(var6, var7));
               Screen var8 = getMc().currentScreen;
               if (var8 == null || var8 instanceof ChatScreen || var8 instanceof EmoteWheelScreen) {
                  var1.bodyYaw = var4.y;
                  var1.relativeHeadYaw = 0.0F;
                  var1.pitch = 0.0F;
               }
            }
         } else {
            var3.larp$setEmoteWeight(0.0F);
            var3.larp$setEmoteProp(null, 0.0F);
         }
      }
   }

   private float ZAsFloat(float var1) {
      float var2 = Math.min(1.0F, ((float)this.Q + (this.s ? 0.0F : var1)) / 3.0F);
      float var3 = this.s ? 1.0F - Math.min(1.0F, ((float)this.U + var1) / 4.0F) : 1.0F;
      float var4 = Math.min(var2, var3);
      return var4 * var4 * (3.0F - 2.0F * var4);
   }

   public static Emotes.Type_d rAsEmotes_Type_d(float var0) {
      Emotes var1 = T;
      if (var1 != null && var1.I != null && var1.q != null && var1.q.prop() != null && !var1.q.handProp() && var1.isRuntimeActive()) {
         ClientPlayerEntity var2 = getMc().player;
         if (var2 != null && var2 == var1.z) {
            var1.I.aAsVoid(var1.Q, var1.s ? 0.0F : var0, var1.w);
            int var3 = EmotecraftJsonReader.Type_r.PROP.ordinal() * 10;
            float[] var4 = new float[7];

            for (int var5 = 0; var5 < 7; var5++) {
               float var6 = var1.w[var3 + (var5 == 6 ? 7 : var5)];
               var4[var5] = Float.isNaN(var6) ? (float)(var5 == 6 ? 1 : 0) : var6;
            }

            float var7 = var1.s ? Math.min(1.0F, ((float)var1.U + var0) / 4.0F) : 0.0F;
            float var8 = 1.0F - var7 * var7 * (3.0F - 2.0F * var7);
            return new Emotes.Type_d(
               var1.q.prop(),
               var2.getLerpedPos(var0),
               var1.y,
               var2.getScale(),
               var4,
               var8,
               (float)var1.Q + (var1.s ? 0.0F : var0),
               var1.s ? (float)var1.U + var0 : 0.0F,
               var1.s
            );
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static boolean LAsBoolean(PlayerEntityRenderState var0) {
      boolean var10000;
      if (T != null && T.isEnabled()) {
         if (var0 instanceof OptionPanel var1 && var1.larp$emoteWeight() > 0.0F) {
            return true;
         }

         var10000 = false;
      } else {
         var10000 = false;
      }

      return var10000;
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Idle";
      } else {
         int var1 = this.wheelKey.getValue();
         if (this.qAsBoolean()) {
            return this.K;
         } else {
            return var1 < 0 ? "Wheel key unbound" : "Hold " + KeybindNames.eAsString(var1);
         }
      }
   }

   public static record Type_d(String prop, Vec3d feet, float facing, float avatarScale, float[] track, float fade, float age, float stopAge, boolean stopping) {
   }
}
