package com.larpclient.module;

import com.google.gson.JsonObject;
import com.larpclient.ClientLog;
import com.larpclient.ModuleImportBroker;
import com.larpclient.ParticleEmitter;
import com.larpclient.ModuleKeybindChecker;
import com.larpclient.TimestampTracker;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;

public final class AutoClicker extends Module {
   private static AutoClicker O;
   private final ModeSetting activation = this.addSetting(
      new ModeSetting("Activation", "Hold each attack/use bind, or click while the module is enabled", "Hold", "Hold", "Automatic")
   );
   private final BooleanSetting leftClick = this.addSetting(new BooleanSetting("Left Click", "Repeat attacks", true));
   private final BooleanSetting rightClick = this.addSetting(new BooleanSetting("Right Click", "Repeat item use and placement", false));
   private final NumberSetting cps = this.addSetting(new NumberSetting("CPS", "Maximum left clicks per second", 10.0, 1.0, 20.0, 1.0));
   private final NumberSetting rightCPS = this.addSetting(new NumberSetting("Right CPS", "Maximum right clicks per second", 10.0, 1.0, 20.0, 1.0));
   private final BooleanSetting randomizeCPS = this.addSetting(
      new BooleanSetting("Randomize CPS", "Choose a rate within each button's range after a click", false)
   );
   private final NumberSetting minCPS = this.addSetting(new NumberSetting("Min CPS", "Minimum left rate, capped by CPS", 8.0, 1.0, 20.0, 1.0));
   private final NumberSetting minRightCPS = this.addSetting(new NumberSetting("Min Right CPS", "Minimum right rate, capped by Right CPS", 8.0, 1.0, 20.0, 1.0));
   private final BooleanSetting onlyWeapon = this.addSetting(new BooleanSetting("Only Weapon", "Only click while holding a weapon", false));
   private final BooleanSetting waitCooldown = this.addSetting(new BooleanSetting("Wait Cooldown", "Wait for a fully charged attack", false));
   private final BooleanSetting onlyEntities = this.addSetting(
      new BooleanSetting("Only Entities", "Left clicks require an entity under your actual aim", false)
   );
   private final BooleanSetting breakBlocks = this.addSetting(new BooleanSetting("Break Blocks", "Keep native continuous mining while aiming at blocks", true));
   private final x7 y = new x7();
   private final x7 T = new x7();
   private boolean L;
   private boolean j;
   private String H = "Waiting for input";
   private long z;
   private int g;
   private int U;

   public AutoClicker() {
      super("AutoClicker", "Customizable attack and use clicking", Category.COMBAT, -1);
      O = this;
      this.cps.visibleWhen(this.leftClick::getValue);
      this.rightCPS.visibleWhen(this.rightClick::getValue);
      this.minCPS.visibleWhen(() -> this.leftClick.getValue() && this.randomizeCPS.getValue());
      this.minRightCPS.visibleWhen(() -> this.rightClick.getValue() && this.randomizeCPS.getValue());
      this.waitCooldown.visibleWhen(this.leftClick::getValue);
      this.onlyEntities.visibleWhen(this.leftClick::getValue);
      this.breakBlocks.visibleWhen(() -> this.leftClick.getValue() && !this.onlyEntities.getValue());

      for (Setting var2 : this.getSettings()) {
         var2.onChanged(var1 -> this.cAsVoid());
      }
   }

   public static AutoClicker GAsAutoClicker() {
      return O;
   }

   public static void DAsVoid(JsonObject var0) {
      if (!var0.has("Right CPS") && var0.has("CPS")) {
         var0.add("Right CPS", var0.get("CPS").deepCopy());
      }
   }

   @Override
   public Module.Type_s getTickPhase() {
      return Module.Type_s.INTENT;
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(k.VANILLA_INPUT);
   }

   public List<Setting<?>> nAsList() {
      return List.of(this.activation, this.leftClick, this.rightClick, this.onlyWeapon);
   }

   public List<Setting<?>> LAsList() {
      return List.of(this.cps, this.rightCPS, this.randomizeCPS, this.minCPS, this.minRightCPS, this.waitCooldown);
   }

   public List<Setting<?>> FAsList() {
      return List.of(this.onlyEntities, this.breakBlocks);
   }

   private boolean tAsBoolean() {
      return this.isRuntimeActive()
         && this.isInGame()
         && mc.interactionManager != null
         && mc.player.isAlive()
         && !mc.player.isSpectator()
         && mc.isWindowFocused()
         && mc.currentScreen == null
         && mc.getOverlay() == null
         && (!this.onlyWeapon.getValue() || Reach.dAsBoolean());
   }

   public boolean NAsBoolean() {
      return this.isEnabled()
         ? this.tAsBoolean() && this.leftClick.getValue() && !mc.player.getMainHandStack().contains(DataComponentTypes.PIERCING_WEAPON)
         : false;
   }

   public boolean jAsBoolean() {
      return this.isEnabled() ? this.tAsBoolean() && this.rightClick.getValue() : false;
   }

   public boolean gAsBoolean() {
      return this.L;
   }

   private boolean AAsBoolean(boolean var1) {
      return this.activation.pAsBoolean("Automatic") || (var1 ? mc.options.useKey : mc.options.attackKey).isPressed();
   }

   private boolean qAsBoolean() {
      return r6.NAsBoolean("Aura") || p5.NAsP5_Type_u() != p5.Type_u.NONE;
   }

   private boolean qAsBoolean(boolean var1) {
      return (var1 ? this.jAsBoolean() : this.NAsBoolean())
         && this.AAsBoolean(var1)
         && b_.jAsBoolean()
         && !this.qAsBoolean()
         && !mc.player.isUsingItem()
         && !mc.player.isRiding()
         && (var1 || !mc.options.useKey.isPressed());
   }

   public boolean RAsBoolean() {
      return this.isEnabled()
         ? this.jAsBoolean() && this.activation.pAsBoolean("Automatic") && !this.qAsBoolean() && b_.jAsBoolean() && mc.player.isUsingItem()
         : false;
   }

   @Override
   public void onTick() {
      if (this.tAsBoolean() && b_.jAsBoolean() && !this.qAsBoolean()) {
         long var1 = System.nanoTime();
         this.H = mc.player.isUsingItem() ? "Using held item" : "Waiting for input";
         if (!this.qAsBoolean(false)) {
            this.y.YAsVoid();
         }

         if (!this.qAsBoolean(true)) {
            this.T.YAsVoid();
         }

         int var3 = this.qAsBoolean(true) && this.T.dAsBoolean(var1) ? 1 : 0;
         if (var3 != 0 && this.j) {
            this.vAsVoid();
         } else {
            byte var4 = 0;
            if (this.qAsBoolean(false) && this.y.dAsBoolean(var1)) {
               HitResult var5 = this.vAsHitResult();
               if (var5 instanceof EntityHitResult var6) {
                  if (this.waitCooldown.getValue() && !b_.VAsBoolean()) {
                     this.H = "Waiting for attack cooldown";
                  } else {
                     Entity var7 = var6.getEntity();
                     b_.tAsVoid(
                        this,
                        var7,
                        mc.player.getEntityInteractionRange(),
                        this.waitCooldown.getValue(),
                        () -> this.qAsBoolean(false) && this.vAsHitResult() instanceof EntityHitResult var2 && var2.getEntity() == var7,
                        1,
                        () -> this.RAsVoid(false),
                        var1x -> this.H = "Waiting: " + var1x
                     );
                     var4 = 1;
                  }
               } else if (var5.getType() == Type.MISS && !this.onlyEntities.getValue()) {
                  b_.UAsVoid(
                     this,
                     () -> this.cAsVoid(() -> {
                           mc.doAttack();
                           this.RAsVoid(false);
                        }),
                     () -> this.qAsBoolean(false)
                           && !this.onlyEntities.getValue()
                           && this.vAsHitResult().getType() == Type.MISS
                           && (!this.waitCooldown.getValue() || b_.VAsBoolean())
                  );
                  var4 = 1;
               }
            }

            if (var3 != 0 && var4 == 0) {
               this.vAsVoid();
            }
         }
      } else {
         this.cAsVoid();
         this.H = "Paused: camera or input unavailable";
      }
   }

   private void vAsVoid() {
      b_.UAsVoid(this, () -> this.cAsVoid(() -> {
            mc.doItemUse();
            this.RAsVoid(true);
         }), () -> this.qAsBoolean(true));
   }

   public void IAsVoid(Consumer<Boolean> var1, boolean var2) {
      if (!this.NAsBoolean()) {
         var1.accept(var2);
      } else {
         this.cAsVoid(
            () -> {
               int var2x = this.qAsBoolean(false) && this.breakBlocks.getValue() && !this.onlyEntities.getValue() && mc.crosshairTarget.getType() == Type.BLOCK
                  ? 1
                  : 0;
               var1.accept(var2x != 0);
               if (var2x != 0) {
                  this.H = "Mining";
               }
            }
         );
      }
   }

   private void cAsVoid(Runnable var1) {
      HitResult var2 = mc.crosshairTarget;
      boolean var3 = this.L;
      mc.crosshairTarget = this.vAsHitResult();
      this.L = true;

      try {
         var1.run();
      } finally {
         mc.crosshairTarget = var2;
         this.L = var3;
      }
   }

   private HitResult vAsHitResult() {
      return mc.player.getCrosshairTarget(1.0F, mc.player);
   }

   private void RAsVoid(boolean var1) {
      long var2 = System.nanoTime();
      double var4 = (var1 ? this.rightCPS : this.cps).getValue();
      double var6 = this.randomizeCPS.getValue() ? (var1 ? this.minRightCPS : this.minCPS).getValue() : var4;
      (var1 ? this.T : this.y).CAsVoid(var2, var6, var4, ThreadLocalRandom.current().nextDouble());
      if (var1) {
         this.U++;
      } else {
         this.g++;
      }

      this.j = !var1;
      this.H = var1 ? "Using" : "Clicking";
      if (var2 - this.z >= 1000000000L) {
         this.z = var2;
         ClientLog.XAsVoid("AutoClicker", "Native clicks: left={}, right={}; activation={}", this.g, this.U, this.activation.getValue());
      }
   }

   private void cAsVoid() {
      this.y.YAsVoid();
      this.T.YAsVoid();
      b_.KAsVoid(this);
   }

   @Override
   public void onEnable() {
      this.cAsVoid();
      this.g = this.U = 0;
      this.z = 0L;
      this.j = false;
      this.H = "Waiting for input";
      ClientLog.XAsVoid(
         "AutoClicker",
         "Enabled: activation={}, left={} CPS, right={} CPS, cooldown={}",
         this.activation.getValue(),
         this.cps.getValue(),
         this.rightCPS.getValue(),
         this.waitCooldown.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.cAsVoid();
      this.H = "Waiting for input";
      ClientLog.XAsVoid("AutoClicker", "Released pending clicks and button timers");
   }

   @Override
   public String getStatus() {
      return this.H;
   }
}
