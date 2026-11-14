package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.KeybindNames;
import com.larpclient.SessionManager;
import com.larpclient.OuterEffect;
import com.larpclient.ParticleEmitter;
import com.larpclient.mixin.ClientPlayNetworkHandlerAccessor;
import com.larpclient.mixin.WorldRendererAccessor;
import java.util.Locale;
import java.util.Set;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec2f;

public class Freecam extends Module {
   private final NumberSetting speed = this.addSetting(new NumberSetting("Speed", "Detached camera speed", 1.0, 0.1, 50.0, 0.1));
   private final NumberSetting vertical = this.addSetting(new NumberSetting("Vertical", "Vertical speed multiplier", 1.0, 0.2, 5.0, 0.05) {
      @Override
      public String getValueAsString() {
         return String.format(Locale.ROOT, "%.2f", this.getValue());
      }
   });
   private final BooleanSetting smooth = this.addSetting(new BooleanSetting("Smooth", "Use smooth half-speed camera travel", true));
   private final BooleanSetting body = this.addSetting(new BooleanSetting("Body", "Show your body from the detached camera", true));
   private final BooleanSetting hands = this.addSetting(new BooleanSetting("Hands", "Show first-person hands while detached", true));
   private final BooleanSetting keysMoveBody = this.addSetting(new BooleanSetting("Keys Move Body", "Also steer your body with movement keys", false));
   private final BooleanSetting keepWalking = this.addSetting(new BooleanSetting("Keep Walking", "Carry movement held when Freecam starts", true));
   private final BooleanSetting scrollSpeed = this.addSetting(new BooleanSetting("Scroll Speed", "Adjust camera speed with the mouse wheel", true));
   private final BooleanSetting resetScrollSpeed = this.addSetting(
      new BooleanSetting("Reset Scroll Speed", "Keep wheel changes temporary until Freecam ends", true)
   );
   private final NumberSetting chunks = this.addSetting(
      new NumberSetting("Chunks", "Client chunk-view radius, within server-provided terrain", 12.0, 2.0, 32.0, 1.0)
   );
   private final ModeSetting activation = this.addSetting(
      new ModeSetting("Activation", "Toggle the module key or keep it held to use Freecam", "Toggle", "Toggle", "Hold")
   );
   private final ot M = new ot();
   private static Freecam E;
   private ClientPlayerEntity B;
   private ClientWorld A;
   private ot.Type_s K;
   private boolean g;
   private boolean Q;
   private boolean P;

   @Override
   public Module.Type_s getTickPhase() {
      return Module.Type_s.CAMERA;
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(k.VANILLA_INPUT);
   }

   public Freecam() {
      super("Freecam", "Explore with a detached camera", Category.PLAYER, -1);
      E = this;
      this.resetScrollSpeed.visibleWhen(this.scrollSpeed::getValue);
      this.speed.onChanged(var1 -> this.M.sAsVoid());
      this.keepWalking.onChanged(var1 -> {
         if (!var1) {
            this.M.CAsVoid();
         }
      });
      this.activation.onChanged(var1 -> {
         if (this.eAsBoolean() || this.P) {
            this.setEnabled(false);
         }
      });
   }

   public boolean eAsBoolean() {
      return this.activation.pAsBoolean("Hold");
   }

   private boolean OAsBoolean() {
      return KeybindNames.sAsBoolean(getMc().getWindow().getHandle(), this.getKeyBind());
   }

   public boolean IAsBoolean() {
      if (this.isEnabled()) {
         if (this.eAsBoolean() && this.OAsBoolean()) {
            this.P = true;
            this.enable();
            return this.isEnabled();
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   @Override
   public void enable() {
      if (!this.eAsBoolean() || this.P) {
         super.enable();
      }
   }

   @Override
   public void disable() {
      this.P = false;
      super.disable();
   }

   @Override
   public void suspendForDisconnect() {
      if (this.eAsBoolean()) {
         this.disable();
      } else {
         super.suspendForDisconnect();
      }
   }

   public static void XAsVoid() {
      Freecam var0 = E;
      if (var0 != null && var0.eAsBoolean() && var0.isEnabled()) {
         MinecraftClient var1 = getMc();
         if (var1 == null
            || var1.player == null
            || var1.world == null
            || !var1.player.isAlive()
            || var1.currentScreen != null
            || var1.getOverlay() != null
            || !var1.isWindowFocused()
            || !SessionManager.HAsBoolean()
            || var0.B != null && (var0.B != var1.player || var0.A != var1.world)
            || !var0.OAsBoolean()) {
            var0.setEnabled(false);
         }
      }
   }

   @Override
   public void setKeyBind(int var1) {
      if (this.eAsBoolean() && var1 != this.getKeyBind()) {
         this.setEnabled(false);
      }

      super.setKeyBind(var1);
   }

   @Override
   public void onEnable() {
      MinecraftClient var1 = getMc();
      this.B = var1.player;
      this.A = var1.world;
      p5.jAsVoid(p5.Type_u.FREECAM);
      this.M.OAsVoid(ot.tAsPlayerInput(xAsPlayerInput(), this.B.input.playerInput), this.B.isSneaking(), this.keepWalking.getValue());
      this.SAsVoid(true);
      this.VAsVoid();
      this.mAsVoid();
      ClientLog.GAsVoid(
         "Freecam",
         "Detached camera acquired; activation={}, speed={}, smooth={}, carry={}, body keys={}",
         this.activation.getValue(),
         this.speed.getValue(),
         this.smooth.getValue(),
         this.keepWalking.getValue(),
         this.keysMoveBody.getValue()
      );
      ClientLog.XAsVoid("Freecam", "Terrain visibility starts from loaded columns in view, including across unloaded camera chunks");
   }

   @Override
   public void onDisable() {
      MinecraftClient var1 = getMc();

      try {
         if (this.A != null && this.B != null) {
            ChunkPos var2 = this.B.getChunkPos();
            this.A.getChunkManager().setChunkMapCenter(var2.x, var2.z);
            this.A.getChunkManager().updateLoadDistance(this.GAsInt());
         }

         if (this.B == var1.player && this.A == var1.world && this.B != null && this.B.isAlive()) {
            if (this.g && this.B.getAbilities().allowFlying && !this.B.getAbilities().flying) {
               this.B.getAbilities().flying = this.Q;
            }

            this.B.input.playerInput = PlayerInput.DEFAULT;
         }
      } finally {
         this.P = false;
         this.g = false;
         this.B = null;
         this.A = null;
         this.K = null;
         this.M.DAsVoid();
         p5.GAsVoid(p5.Type_u.FREECAM);
         if (var1.world != null) {
            ((WorldRendererAccessor)var1.worldRenderer).larpOcclusionGraph().scheduleTerrainUpdate();
         }

         ClientLog.GAsVoid("Freecam", "Released camera, body input and chunk view");
      }
   }

   @Override
   public void onTick() {
      XAsVoid();
      if (this.isRuntimeActive()) {
         p5.jAsVoid(p5.Type_u.FREECAM);
         this.SAsVoid(false);
         this.VAsVoid();
         p5.DAsVoid(this.M.bAsDouble(this.speed.getValue(), this.resetScrollSpeed.getValue()), this.vertical.getValue(), this.smooth.getValue());
         this.mAsVoid();
      }
   }

   public static boolean yAsBoolean() {
      return E != null && E.isEnabled() ? p5.oAsBoolean() : false;
   }

   public static boolean bAsBoolean() {
      return E != null && E.isEnabled() ? E != null && E.body.getValue() : false;
   }

   public static boolean oAsBoolean() {
      return E != null && E.isEnabled() ? E != null && E.hands.getValue() : false;
   }

   public static PlayerInput yAsPlayerInput(PlayerInput var0) {
      if (E != null && E.isEnabled()) {
         Freecam var1 = E;
         MinecraftClient var2 = MinecraftClient.getInstance();
         return var1 != null && var2.currentScreen == null && var2.isWindowFocused()
            ? var1.M.pAsPlayerInput(var0, var1.keysMoveBody.getValue())
            : PlayerInput.DEFAULT;
      } else {
         return var0;
      }
   }

   public static PlayerInput xAsPlayerInput() {
      GameOptions var0 = MinecraftClient.getInstance().options;
      return new PlayerInput(
         var0.forwardKey.isPressed(),
         var0.backKey.isPressed(),
         var0.leftKey.isPressed(),
         var0.rightKey.isPressed(),
         var0.jumpKey.isPressed(),
         var0.sneakKey.isPressed(),
         var0.sprintKey.isPressed()
      );
   }

   private void SAsVoid(boolean var1) {
      if (this.B != null) {
         this.M
            .jAsVoid(
               ot.tAsPlayerInput(xAsPlayerInput(), this.B.input.playerInput),
               this.keepWalking.getValue(),
               this.keysMoveBody.getValue(),
               this.B.isSneaking(),
               new Vec2f(this.B.sidewaysSpeed, this.B.forwardSpeed),
               this.B.getVelocity(),
               this.B.getYaw(),
               var1
            );
      }
   }

   private void VAsVoid() {
      if (this.B != null && this.B.getAbilities().allowFlying) {
         if (!this.g) {
            this.Q = this.B.getAbilities().flying;
            this.g = true;
         }

         this.B.getAbilities().flying = false;
      }
   }

   public static void BAsVoid() {
      if (E != null) {
         E.g = false;
      }
   }

   private void mAsVoid() {
      if (this.A != null && this.B != null) {
         ChunkPos var1 = this.B.getChunkPos();
         ot.Type_s var2 = ot.yAsOt_Type_s(p5.dAsVec3d(1.0F), var1.x, var1.z, this.chunks.getValue().intValue(), this.GAsInt());
         ClientChunkManager var3 = this.A.getChunkManager();
         var3.setChunkMapCenter(var2.x(), var2.z());
         var3.updateLoadDistance(var2.radius());
         if (!var2.equals(this.K)) {
            ((WorldRendererAccessor)getMc().worldRenderer).larpOcclusionGraph().scheduleTerrainUpdate();
            this.K = var2;
         }
      }
   }

   private int GAsInt() {
      return this.B == null ? getMc().options.getClampedViewDistance() : ((ClientPlayNetworkHandlerAccessor)this.B.networkHandler).larpServerChunkRadius();
   }

   public static boolean QAsBoolean(double var0) {
      if (E != null && E.isEnabled()) {
         Freecam var2 = E;
         MinecraftClient var3 = MinecraftClient.getInstance();
         if (var2 != null
            && p5.oAsBoolean()
            && var2.scrollSpeed.getValue()
            && var3.currentScreen == null
            && var3.getOverlay() == null
            && var3.isWindowFocused()) {
            if (!var2.M.uAsBoolean(var0, var2.speed.getValue(), var2.resetScrollSpeed.getValue())) {
               return false;
            } else {
               if (!var2.resetScrollSpeed.getValue()) {
                  var2.speed.setClampedValue(var2.speed.getValue() * (var0 > 0.0 ? 1.2 : 0.8333333333333334));
               }

               ClientLog.XAsVoid("Freecam", "Wheel camera speed={}", var2.M.bAsDouble(var2.speed.getValue(), var2.resetScrollSpeed.getValue()));
               return true;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }
}
