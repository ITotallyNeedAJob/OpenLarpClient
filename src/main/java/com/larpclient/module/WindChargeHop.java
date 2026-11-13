package com.larpclient.module;

import com.larpclient.BlockPlacementController;
import com.larpclient.ClientLog;
import com.larpclient.b6;
import com.larpclient.b_;
import com.larpclient.dq;
import com.larpclient.p5;
import com.larpclient.pg;
import com.larpclient.mixin.ClientPlayerInteractionManagerAccessor;
import com.larpclient.mixin.MinecraftClientAccessor;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.UseCooldownComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public final class WindChargeHop extends Module {
   private static final int M = 40;
   private static WindChargeHop u;
   private final KeybindSetting hopBind = this.addSetting(new KeybindSetting("Hop Bind", "Press for one wind charge hop; separate from the module toggle", -1));
   private final ModeSetting direction = this.addSetting(
      new ModeSetting(
         "Direction", "Up throws straight down for height; Forward throws behind you so the burst pushes you where you look", "Up", "Up", "Forward"
      )
   );
   private final ModeSetting jump = this.addSetting(
      new ModeSetting(
         "Jump",
         "With Throw jumps as you throw, so the push adds to the jump; Timed jumps just before the push arrives, from your ping and measured hops; Off never jumps",
         "With Throw",
         "With Throw",
         "Timed",
         "Off"
      )
   );
   private final BooleanSetting swapBack = this.addSetting(new BooleanSetting("Swap Back", "Return to your previous hotbar slot after the throw", true));
   private final pg i = new pg(this);
   private static final Map<String, Integer> n = new HashMap<>();
   private WindChargeHop.Type_y g = WindChargeHop.Type_y.IDLE;
   private boolean q;
   private ClientPlayerEntity J;
   private ClientWorld d;
   private b6.Type_y k;
   private long w;
   private long R = Long.MIN_VALUE;
   private int X;
   private int B;
   private double L;
   private double V;
   private double A;
   private boolean e;
   private String Y = "Set a Hop Bind";
   private int E;

   public WindChargeHop() {
      super("Wind Charge Hop", "Throw a wind charge at the ground for a timed boost, with a silent turn", Category.MOVEMENT, -1);
      u = this;
      this.direction.onChanged(var1 -> this.jAsVoid("Direction changed"));
      this.hopBind.onChanged(var1 -> this.jAsVoid("Bind changed"));
   }

   public static WindChargeHop OAsWindChargeHop() {
      return u;
   }

   @Override
   public Module.Type_s getTickPhase() {
      return Module.Type_s.INTENT;
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(com.larpclient.module.k.VANILLA_INPUT, com.larpclient.module.k.SERVER_STATE_MODIFYING);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.Y : "";
   }

   public boolean UAsBoolean() {
      return this.isEnabled()
         ? this.isRuntimeActive() && this.J != null && this.J == mc.player && this.d == mc.world && (this.g == WindChargeHop.Type_y.THROW || this.w == this.R)
         : false;
   }

   public static void qAsVoid(int var0) {
      if (u != null && u.isEnabled()) {
         WindChargeHop var1 = u;
         if (var1 != null && var1.isRuntimeActive() && var0 > 0 && var0 == var1.hopBind.getValue()) {
            var1.q = true;
         }
      }
   }

   @Override
   public void onTick() {
      if (!this.isInGame()) {
         this.jAsVoid("Waiting for a world");
      } else {
         if (this.J != mc.player || this.d != mc.world) {
            this.jAsVoid("World changed");
            this.J = mc.player;
            this.d = mc.world;
            this.B = 0;
         }

         this.w++;
         if (this.B > 0) {
            this.zAsVoid();
         }

         boolean var1 = this.q;
         this.q = false;
         if (this.g == WindChargeHop.Type_y.IDLE) {
            if (var1) {
               this.vAsVoid();
            } else if (this.hopBind.getValue() < 0) {
               this.Y = "Set a Hop Bind";
            }
         } else if (this.g == WindChargeHop.Type_y.JUMP) {
            this.lAsVoid();
         } else {
            String var2 = this.UAsString();
            if (var2 != null) {
               this.jAsVoid(var2);
            } else {
               this.AAsVoid();
               b_.UAsVoid(this, this::jAsVoid, () -> this.g == WindChargeHop.Type_y.THROW && this.UAsString() == null && this.DAsBoolean());
               if (++this.E > 10) {
                  this.jAsVoid("The throw could not go out");
               }
            }
         }
      }
   }

   private void vAsVoid() {
      String var1 = this.UAsString();
      if (var1 == null) {
         var1 = this.EAsString();
      }

      if (var1 != null) {
         this.Y = var1;
         ClientLog.GAsVoid("WindHop", "Hop refused: {}", var1);
      } else {
         ClientPlayerEntity var2 = this.J;
         float var3 = this.FAsFloatArray()[0];
         int var4 = !this.jump.pAsBoolean("Off") && var2.isOnGround() ? 1 : 0;
         int var5 = this.jump.pAsBoolean("With Throw") && var4 != 0 ? 1 : 0;
         this.k = b6.rAsB6_Type_y(
            var2.getEntityPos(),
            (double)var2.getStandingEyeHeight(),
            var2.getVelocity(),
            var2.isOnGround(),
            var5 != 0,
            var3,
            this.direction.pAsBoolean("Forward"),
            this::lAsB6_Type_k
         );
         if (this.k == null) {
            this.Y = "No ground close enough for a boost";
            ClientLog.GAsVoid("WindHop", "Hop refused: no burst within {} blocks of you", 2.4);
         } else {
            this.g = WindChargeHop.Type_y.THROW;
            this.E = 0;
            ClientLog.GAsVoid(
               "WindHop",
               "Hop: {}, throw yaw {} pitch {}, burst after {} ticks at {}, predicted push {} up {} along ({} blocks/tick), jump {}",
               this.direction.getValue(),
               eAsString((double)this.k.yaw()),
               eAsString((double)this.k.pitch()),
               this.k.ticks(),
               XAsString(this.k.burst()),
               eAsString(this.k.push().y),
               eAsString(Math.hypot(this.k.push().x, this.k.push().z)),
               eAsString(this.k.push().length()),
               var4 != 0 ? this.jump.getValue() : "none"
            );
            this.AAsVoid();
            b_.UAsVoid(this, this::jAsVoid, () -> this.g == WindChargeHop.Type_y.THROW && this.UAsString() == null && this.DAsBoolean());
         }
      }
   }

   private b6.Type_k lAsB6_Type_k(Vec3d var1, Vec3d var2) {
      BlockHitResult var3 = this.d.raycast(new RaycastContext(var1, var2, ShapeType.COLLIDER, FluidHandling.NONE, this.J));
      return var3.getType() == Type.MISS ? null : new b6.Type_k(var3.getPos(), Vec3d.of(var3.getSide().getVector()));
   }

   private void AAsVoid() {
      if (b_.nAsBoolean(this)) {
         p5.jAsVoid(p5.Type_u.WINDHOP);
         if (p5.NAsP5_Type_u() == p5.Type_u.WINDHOP) {
            dq.RAsVoid(this, dq.Type_s.WINDHOP, this.k.yaw(), this.k.pitch(), 1.0);
         }
      }
   }

   private void jAsVoid() {
      ClientPlayerEntity var1 = this.J;
      if (this.g == WindChargeHop.Type_y.THROW
         && !((double)Math.abs(MathHelper.wrapDegrees(var1.getYaw() - this.k.yaw())) > 0.05)
         && !((double)Math.abs(var1.getPitch() - this.k.pitch()) > 0.05)) {
         int var2 = !this.jump.pAsBoolean("Off") && var1.isOnGround() ? 1 : 0;
         if (this.jump.pAsBoolean("With Throw") && var2 != 0) {
            var1.jump();
         }

         boolean var3;
         try {
            var3 = this.IAsBoolean();
         } finally {
            this.BAsVoid();
         }

         if (!var3) {
            this.jAsVoid("The wind charge throw was rejected");
         } else {
            this.R = this.w;
            this.L = this.V = var1.getY();
            this.A = var1.getVelocity().y;
            this.e = false;
            this.B = 40;
            String var4 = this.HAsString();
            int var5 = n.getOrDefault(var4, 0);
            this.X = b6.DAsInt(this.k.ticks(), this.aAsInt(), var5);
            if (this.jump.pAsBoolean("Timed") && var2 != 0 && this.X > 0) {
               this.g = WindChargeHop.Type_y.JUMP;
               this.Y = "Wind charge thrown; jump in " + this.X + " ticks";
            } else {
               if (this.jump.pAsBoolean("Timed") && var2 != 0) {
                  var1.jump();
               }

               this.g = WindChargeHop.Type_y.IDLE;
               this.Y = "Wind charge thrown";
            }

            ClientLog.GAsVoid(
               "WindHop",
               "Thrown at yaw {} pitch {}; jump {} ({} ticks from {}), round trip {} ticks",
               eAsString((double)var1.getYaw()),
               eAsString((double)var1.getPitch()),
               var2 != 0 ? this.jump.getValue() : "none",
               this.X,
               var5 > 0 ? "measured hops" : "ping",
               this.aAsInt()
            );
         }
      }
   }

   private void lAsVoid() {
      if (this.w - this.R >= (long)this.X) {
         this.g = WindChargeHop.Type_y.IDLE;
         if (this.J.isOnGround() && !this.e) {
            this.J.jump();
            this.Y = "Jumped for the burst";
         } else {
            this.Y = this.e ? "The push came before the jump" : "Left the ground before the jump";
         }
      }
   }

   private void zAsVoid() {
      ClientPlayerEntity var1 = this.J;
      double var2 = var1.getVelocity().y;
      if (!this.e && var2 - this.A > 0.5) {
         this.e = true;
         int var4 = (int)(this.w - this.R);
         n.put(this.HAsString(), var4);
         ClientLog.GAsVoid("WindHop", "Push arrived {} ticks after the throw: vertical speed {} to {}", var4, eAsString(this.A), eAsString(var2));
      }

      this.A = var2;
      this.V = Math.max(this.V, var1.getY());
      if (--this.B <= 0) {
         ClientLog.GAsVoid("WindHop", "Hop result: rose {} blocks{}", eAsString(this.V - this.L), this.e ? "" : "; no push seen, so the burst missed you");
         if (this.g == WindChargeHop.Type_y.IDLE) {
            this.Y = String.format(Locale.ROOT, "Hopped %.1f blocks", this.V - this.L);
         }
      }
   }

   private String UAsString() {
      if (!b_.jAsBoolean() || mc.currentScreen != null) {
         return "Paused: a screen is open";
      } else if (!mc.isWindowFocused()) {
         return "Paused: the window lost focus";
      } else if (p5.NAsP5_Type_u() != p5.Type_u.WINDHOP && !p5.fAsBoolean(p5.Type_u.WINDHOP)) {
         return "Paused: Freecam, Freelook or a pearl catch holds the view";
      } else if (this.J.hasVehicle()) {
         return "Not while riding";
      } else if (!this.J.isTouchingWater() && !this.J.isSubmergedIn(FluidTags.WATER) && !this.J.isInLava()) {
         return this.J.isGliding() ? "Not while gliding" : null;
      } else {
         return "Not in water or lava";
      }
   }

   private String EAsString() {
      if (this.J.isUsingItem()) {
         return "Stop using your item first";
      } else if (Aura.WAsBoolean()) {
         return "Paused while Kill Aura lowers your shield";
      } else {
         int var1 = BlockPlacementController.qAsInt(Items.WIND_CHARGE, true);
         if (var1 == -1) {
            return "No wind charges in your hotbar or hands";
         } else {
            ItemStack var2 = this.KAsItemStack(var1);
            if (!var2.isItemEnabled(this.d.getEnabledFeatures())) {
               return "Wind charges are disabled here";
            } else {
               float var3 = this.J.getItemCooldownManager().getCooldownProgress(var2, 0.0F);
               if (var3 > 0.0F) {
                  UseCooldownComponent var4 = (UseCooldownComponent)var2.get(DataComponentTypes.USE_COOLDOWN);
                  return String.format(
                     Locale.ROOT, "Wind charge cooling down (%.2f s)", (double)(var3 * (float)(var4 == null ? 10 : var4.getCooldownTicks())) / 20.0
                  );
               } else {
                  return var1 >= 0 && var1 != this.J.getInventory().getSelectedSlot() && !pg.yAsBoolean(this) ? "Hotbar busy" : null;
               }
            }
         }
      }
   }

   private ItemStack KAsItemStack(int var1) {
      return var1 == -2 ? this.J.getOffHandStack() : this.J.getInventory().getStack(var1);
   }

   private boolean DAsBoolean() {
      int var1 = BlockPlacementController.qAsInt(Items.WIND_CHARGE, true);
      return var1 != -1
         && !this.J.getItemCooldownManager().isCoolingDown(this.KAsItemStack(var1))
         && !Aura.WAsBoolean()
         && (var1 == -2 || var1 == this.J.getInventory().getSelectedSlot() || pg.yAsBoolean(this));
   }

   private boolean IAsBoolean() {
      int var1 = BlockPlacementController.qAsInt(Items.WIND_CHARGE, true);
      if (var1 == -1) {
         return false;
      } else {
         Hand var2 = var1 == -2 ? Hand.OFF_HAND : Hand.MAIN_HAND;
         if (var1 >= 0 && var1 != this.J.getInventory().getSelectedSlot() && !this.i.uAsBoolean(this.J, var1)) {
            return false;
         } else {
            boolean var3 = mc.interactionManager.interactItem(this.J, var2).isAccepted();
            if (var3) {
               this.J.swingHand(var2);
               ((MinecraftClientAccessor)mc).larpUseDelay(4);
            }

            return var3;
         }
      }
   }

   private void BAsVoid() {
      if (this.i.FAsBoolean()) {
         int var1 = this.J == null ? -1 : this.J.getInventory().getSelectedSlot();
         this.i.vAsVoid(getMc(), this.swapBack.getValue());
         if (this.J != null && this.J == mc.player && mc.interactionManager != null && var1 != this.J.getInventory().getSelectedSlot()) {
            ((ClientPlayerInteractionManagerAccessor)mc.interactionManager).larpSyncCarriedItem();
         }
      }
   }

   private float[] FAsFloatArray() {
      return !p5.TAsBoolean()
         ? new float[]{this.J.getYaw(), this.J.getPitch()}
         : p5.sAsFloatArray(p5.JAsFloat(), p5.mAsFloat(), mc.options.getPerspective().isFrontView());
   }

   private int aAsInt() {
      ClientPlayNetworkHandler var1 = mc.getNetworkHandler();
      PlayerListEntry var2 = var1 != null && this.J != null ? var1.getPlayerListEntry(this.J.getUuid()) : null;
      return var2 == null ? 0 : Math.max(0, Math.round((float)var2.getLatency() / 50.0F));
   }

   private String HAsString() {
      ServerInfo var1 = mc.getCurrentServerEntry();
      return var1 != null ? var1.address : (mc.isIntegratedServerRunning() ? "singleplayer" : "this server");
   }

   private void jAsVoid(String var1) {
      int var2 = this.g != WindChargeHop.Type_y.IDLE ? 1 : 0;
      b_.KAsVoid(this);
      this.BAsVoid();
      this.g = WindChargeHop.Type_y.IDLE;
      p5.GAsVoid(p5.Type_u.WINDHOP);
      if (var2 != 0) {
         this.Y = var1;
         ClientLog.GAsVoid("WindHop", "Hop cancelled: {}", var1);
      }
   }

   private static String eAsString(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   private static String XAsString(Vec3d var0) {
      return String.format(Locale.ROOT, "(%.1f, %.1f, %.1f)", var0.x, var0.y, var0.z);
   }

   @Override
   public void onEnable() {
      this.J = null;
      this.d = null;
      this.g = WindChargeHop.Type_y.IDLE;
      this.R = Long.MIN_VALUE;
      this.B = 0;
      this.Y = this.hopBind.getValue() < 0 ? "Set a Hop Bind" : "Ready";
      ClientLog.GAsVoid("WindHop", "Enabled; direction={}, jump={}, swap back={}", this.direction.getValue(), this.jump.getValue(), this.swapBack.getValue());
   }

   @Override
   public void onDisable() {
      this.jAsVoid("Disabled");
      this.J = null;
      this.d = null;
      ClientLog.GAsVoid("WindHop", "Disabled; pending throw and view released");
   }

   private static enum Type_y {
      IDLE,
      THROW,
      JUMP;

      private Type_y() {
      }
   }
}
