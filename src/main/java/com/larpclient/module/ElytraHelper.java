package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.KeybindNames;
import com.larpclient.ModuleImportBroker;
import com.larpclient.ModuleKeybindChecker;
import java.util.Set;
import java.util.function.BooleanSupplier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public final class ElytraHelper extends Module {
   private final KeybindSetting fireworkKey = this.addSetting(new KeybindSetting("Firework Key", "Hold this key to use hotbar fireworks while gliding", -1));
   private final BooleanSetting autoStart = this.addSetting(new BooleanSetting("Auto Start", "Start gliding when falling with an elytra equipped", true));
   private final BooleanSetting fastStart = this.addSetting(new BooleanSetting("Fast Start", "Use a firework on the first automatic takeoff", true));
   private final BooleanSetting combatRelaunch = this.addSetting(
      new BooleanSetting("Combat Relaunch", "Jump and relaunch when landing with an elytra equipped", true)
   );
   private final NumberSetting relaunchDelay = this.addSetting(
      new NumberSetting("Relaunch Delay", "Delay after the relaunch jump in milliseconds", 175.0, 0.0, 1000.0, 25.0)
   );
   private final BooleanSetting relaunchFirework = this.addSetting(new BooleanSetting("Relaunch Firework", "Use a firework after the relaunch", true));
   private final BooleanSetting autoFirework = this.addSetting(new BooleanSetting("Auto Firework", "Use hotbar fireworks when flight speed drops", false));
   private final NumberSetting delay = this.addSetting(new NumberSetting("Delay", "Minimum time between fireworks in milliseconds", 650.0, 100.0, 2000.0, 25.0));
   private final BooleanSetting onlyWithTarget = this.addSetting(
      new BooleanSetting("Only With Target", "Automatic fireworks require the current Kill Aura target", false)
   );
   private final BooleanSetting onlyWhileGliding = this.addSetting(new BooleanSetting("Only While Gliding", "Automatic fireworks require active gliding", true));
   private ClientPlayerEntity f;
   private ClientWorld p;
   private KeyBinding c;
   private boolean J;
   private long h;
   private long g;
   private static boolean Q;
   private int e;
   private int u;
   private boolean l;
   private boolean F;
   private boolean E;
   private boolean m;

   public ElytraHelper() {
      super("ElytraHelper", "Automate elytra takeoff and fireworks", Category.MOVEMENT, -1);
      this.fastStart.visibleWhen(this.autoStart::getValue);
      this.relaunchDelay.visibleWhen(this.combatRelaunch::getValue);
      this.relaunchFirework.visibleWhen(this.combatRelaunch::getValue);
      this.delay.visibleWhen(this.autoFirework::getValue);
      this.onlyWithTarget.visibleWhen(this.autoFirework::getValue);
      this.onlyWhileGliding.visibleWhen(this.autoFirework::getValue);
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(k.SERVER_STATE_MODIFYING);
   }

   @Override
   public void onEnable() {
      this.DAsVoid();
      this.f = mc.player;
      this.p = mc.world;
      this.h = System.nanoTime();
   }

   @Override
   public void onDisable() {
      ModuleImportBroker.KAsVoid(this);
      this.DAsVoid();
      this.f = null;
      this.p = null;
   }

   private void DAsVoid() {
      this.aAsVoid();
      this.e = 0;
      this.l = false;
      this.F = false;
      this.E = false;
   }

   private void aAsVoid() {
      if (this.m && this.c != null && this.c.isPressed() == this.J) {
         this.c.setPressed(ModuleKeybindChecker.pAsBoolean(this.c));
      }

      this.m = false;
      this.c = null;
   }

   private boolean nAsBoolean() {
      return this.isInGame()
         && mc.player == this.f
         && mc.world == this.p
         && ModuleKeybindChecker.VAsBoolean()
         && ModuleImportBroker.jAsBoolean()
         && !mc.player.getAbilities().flying
         && !mc.player.hasVehicle()
         && !mc.player.isTouchingWater()
         && !mc.player.isInLava()
         && mc.player.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA)
         && !this.FAsBoolean();
   }

   private boolean FAsBoolean() {
      AirStuck var1 = AirStuck.zAsAirStuck();
      ElytraMotion var2 = ElytraMotion.vAsElytraMotion();
      return var1 != null && var1.wAsBoolean() || var2 != null && var2.ZAsBoolean();
   }

   @Override
   public void onTick() {
      if (!this.nAsBoolean()) {
         ModuleImportBroker.KAsVoid(this);
         this.DAsVoid();
      } else {
         long var1 = System.nanoTime();
         if (this.e != 0) {
            if (mc.player.age > this.u) {
               ModuleImportBroker.KAsVoid(this);
               this.DAsVoid();
               ClientLog.XAsVoid("ElytraHelper", "Takeoff timed out; released jump input");
            } else if (this.e == 1) {
               if (mc.player.isOnGround()) {
                  this.DAsVoid();
                  return;
               }

               ModuleImportBroker.UAsVoid(this, () -> {
                  if (this.e == 1 && this.nAsBoolean() && !mc.player.isOnGround()) {
                     mAsVoid();
                     this.mAsVoid(true);
                     this.e = 2;
                     if (this.E) {
                        DAsVoid(() -> this.h = System.nanoTime());
                     }

                     ClientLog.XAsVoid("ElytraHelper", "Takeoff requested; firework={}", this.E);
                  }
               }, () -> this.e == 1 && this.nAsBoolean() && !mc.player.isOnGround());
            } else {
               this.aAsVoid();
               this.e = 0;
               this.E = false;
            }
         } else if (mc.player.isOnGround()) {
            this.l = false;
            if (this.combatRelaunch.getValue() && !this.F) {
               mc.player.jump();
               this.g = var1;
               this.F = true;
               ClientLog.XAsVoid("ElytraHelper", "Relaunch jump; waiting {} ms", this.relaunchDelay.getValue());
            }
         } else {
            if (this.F && !this.combatRelaunch.getValue()) {
               this.F = false;
            }

            if (this.F) {
               if (var1 - this.g >= (long)(this.relaunchDelay.getValue() * 1000000.0)) {
                  this.iAsVoid(this.relaunchFirework.getValue());
                  this.F = false;
                  this.l = true;
               }
            } else if (this.autoStart.getValue() && !mc.player.isGliding() && mc.player.fallDistance > 0.0) {
               this.iAsVoid(this.fastStart.getValue() && !this.l);
               this.l = true;
            } else {
               int var3 = mc.player.isGliding() && KeybindNames.sAsBoolean(mc.getWindow().getHandle(), this.fireworkKey.getValue()) ? 1 : 0;
               if (var3 != 0 && var1 - this.h >= 180000000L) {
                  RAsVoid(
                     this,
                     () -> this.nAsBoolean() && mc.player.isGliding() && KeybindNames.sAsBoolean(mc.getWindow().getHandle(), this.fireworkKey.getValue()),
                     () -> this.h = System.nanoTime()
                  );
               } else if (this.autoFirework.getValue() && this.PAsBoolean() && var1 - this.h >= (long)(this.delay.getValue() * 1000000.0)) {
                  RAsVoid(this, () -> this.nAsBoolean() && this.PAsBoolean(), () -> this.h = System.nanoTime());
               }
            }
         }
      }
   }

   private void mAsVoid(boolean var1) {
      this.c = mc.options.jumpKey;
      this.c.setPressed(var1);
      this.J = var1;
      this.m = true;
   }

   private void iAsVoid(boolean var1) {
      this.mAsVoid(false);
      this.E = var1;
      this.e = 1;
      this.u = mc.player.age + 3;
   }

   private boolean PAsBoolean() {
      if (this.onlyWhileGliding.getValue() && !mc.player.isGliding()) {
         return false;
      } else {
         LivingEntity var1 = pAsLivingEntity();
         if (this.onlyWithTarget.getValue() && var1 == null) {
            return false;
         } else {
            double var2 = mc.player.getVelocity().horizontalLength() * 20.0;
            return var1 == null ? var2 <= 22.0 : !(mc.player.distanceTo(var1) < 5.0F) || !(var2 > var1.getVelocity().horizontalLength() * 20.0 * 0.9);
         }
      }
   }

   static LivingEntity pAsLivingEntity() {
      Aura var0 = Aura.fAsAura();
      LivingEntity var1 = var0 == null ? null : var0.hAsLivingEntity();
      return var1 != null && var1.isAlive() && !var1.isRemoved() && var1.getEntityWorld() == getMc().world ? var1 : null;
   }

   static void mAsVoid() {
      getMc().player.startGliding();
      getMc().player.networkHandler.sendPacket(new ClientCommandC2SPacket(getMc().player, Mode.START_FALL_FLYING));
   }

   static void RAsVoid(Module var0, BooleanSupplier var1, Runnable var2) {
      ModuleImportBroker.UAsVoid(var0, () -> DAsVoid(var2), var1);
   }

   static void DAsVoid(Runnable var0) {
      MinecraftClient var1 = getMc();
      if (ModuleImportBroker.jAsBoolean() && !var1.player.isUsingItem()) {
         PlayerInventory var2 = var1.player.getInventory();
         int var3 = -1;

         for (int var4 = 0; var4 < 9; var4++) {
            if (var2.getStack(var4).isOf(Items.FIREWORK_ROCKET)) {
               var3 = var4;
               break;
            }
         }

         if (var3 < 0) {
            if (!Q) {
               ClientLog.XAsVoid("ElytraHelper", "Firework unavailable in hotbar");
            }

            Q = true;
         } else {
            Q = false;
            int var9 = var2.getSelectedSlot();
            var2.setSelectedSlot(var3);

            try {
               ActionResult var5 = var1.interactionManager.interactItem(var1.player, Hand.MAIN_HAND);
               if (var5.isAccepted()) {
                  var1.player.swingHand(Hand.MAIN_HAND);
                  var0.run();
               }
            } finally {
               var2.setSelectedSlot(var9);
            }
         }
      }
   }

   @Override
   public String getStatus() {
      return this.e != 0 ? "Takeoff" : (this.F ? "Relaunch" : "");
   }
}
