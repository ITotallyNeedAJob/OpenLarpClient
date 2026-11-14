package com.larpclient.module;

import com.larpclient.BlockPlacementController;
import com.larpclient.ClientLog;
import com.larpclient.KeybindNames;
import com.larpclient.LarpClient;
import com.larpclient.ModuleImportBroker;
import com.larpclient.FallDamage;
import com.larpclient.ModuleDetector;
import com.larpclient.ParticleEmitter;
import com.larpclient.ParticleKind;
import com.larpclient.ModuleKeybindChecker;
import com.larpclient.mixin.ClientPlayerEntityAccessor;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.MouseInput;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ShieldItem;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public final class Aura extends Module {
   private static Aura A;
   public static volatile boolean Y;
   private final ModeSetting activation = (ModeSetting)this.addSetting(
         new ModeSetting("Activation", "Toggle acts until you switch it off; Hold only acts while the module shortcut is held", "Toggle", "Toggle", "Hold")
      )
      .displayName("Keybind Mode");
   private final ModeSetting rotation = this.addSetting(
      new ModeSetting("Rotation", "Rotation engine that turns the player toward the aim point", "Sloth", "Sloth", "FunTime", "Polar", "Matrix", "Legit") {
         @Override
         public void setValue(String var1) {
            super.setValue(Aura.kAsString(var1));
         }

         @Override
         public boolean acceptsValue(String var1) {
            return super.acceptsValue(Aura.kAsString(var1));
         }
      }
   );
   private final ModeSetting auraMode = this.addSetting(
      new ModeSetting("Aura Mode", "Blatant aims and swings on every chance; Normal waits until the attack is about to land", "Normal", "Blatant", "Normal") {
         @Override
         public void setValue(String var1) {
            super.setValue(Aura.nAsString(var1));
         }

         @Override
         public boolean acceptsValue(String var1) {
            return super.acceptsValue(Aura.nAsString(var1));
         }
      }
   );
   private final ModeSetting combatVersion = this.addSetting(
      new ModeSetting("Combat Version", "1.9+ waits for the attack charge; 1.8 clicks at the CPS range", "1.9", "1.9", "1.8") {
         @Override
         public void setValue(String var1) {
            super.setValue("1.9+".equals(var1) ? "1.9" : var1);
         }

         @Override
         public boolean acceptsValue(String var1) {
            return "1.9+".equals(var1) || super.acceptsValue(var1);
         }

         @Override
         public String getModeLabel(String var1) {
            return "1.9".equals(var1) ? "1.9+" : var1;
         }

         @Override
         public String getValueAsString() {
            return this.getModeLabel(this.getValue());
         }
      }
   );
   private final NumberSetting attackRange = this.addSetting(
      new NumberSetting("Attack Range", "Maximum distance to the target box for an attack", 3.0, 2.0, 6.0, 0.1)
   );
   private final NumberSetting aimRange = this.addSetting(
      new NumberSetting("Aim Range", "Extra distance beyond Attack Range where targets are tracked", 1.5, 0.0, 9.0, 0.1)
   );
   private final NumberSetting fov = this.addSetting(new NumberSetting("FOV", "Field of view around your camera for new targets", 360.0, 30.0, 360.0, 5.0));
   private final BooleanSetting players = this.addSetting(new BooleanSetting("Players", "Target players", true));
   private final BooleanSetting mobs = this.addSetting(new BooleanSetting("Mobs", "Target hostile and neutral mobs", true));
   private final BooleanSetting animals = this.addSetting(new BooleanSetting("Animals", "Target animals", false));
   private final BooleanSetting invisibles = this.addSetting(new BooleanSetting("Invisibles", "Target invisible players", true));
   private final BooleanSetting nakedPlayers = this.addSetting(new BooleanSetting("Naked Players", "Target players without armor", true));
   private final ModeSetting criticals = this.addSetting(
      new ModeSetting(
         "Criticals", "Adaptive lands criticals when available; Always waits for a falling hit; None ignores them", "Adaptive", "Adaptive", "Always", "None"
      )
   );
   private final ModeSetting aimPoint = this.addSetting(
      new ModeSetting("Aim Point", "Where on the target box to aim", "Nearest", "Nearest", "Center", "Multipoint", "Head", "Body")
   );
   private final BooleanSetting predict = this.addSetting(new BooleanSetting("Predict", "Lead moving targets", false));
   private final NumberSetting predictValue = this.addSetting(new NumberSetting("Predict Value", "Ticks of target motion to lead", 1.0, 0.0, 4.0, 0.1));
   private final NumberSetting reachBuffer = this.addSetting(
      new NumberSetting("Reach Buffer", "Distance subtracted from Attack Range as a safety margin", 0.2, 0.0, 1.0, 0.05)
   );
   private final BooleanSetting rotationETA = this.addSetting(
      new BooleanSetting("Rotation ETA", "Retained from the reference, which never reads it; it has no effect", true)
   );
   private final BooleanSetting entityRaycast = this.addSetting(
      new BooleanSetting("Entity Raycast", "Attack whichever valid entity the final aim ray actually hits", true)
   );
   private final BooleanSetting dynamicSafeReach = this.addSetting(
      new BooleanSetting("Dynamic Safe Reach", "Shorten reach while the gap to the target is changing quickly, scaled by ping", true)
   );
   private final BooleanSetting predictCriticals = this.addSetting(
      new BooleanSetting("Predict Criticals", "Start the sprint reset a tick or two before the jump arc falls", true)
   );
   private final BooleanSetting randomizeTiming = this.addSetting(
      new BooleanSetting("Randomize Timing", "Attack at a randomized charge between 97 and 100 percent", true)
   );
   private final ModeSetting sprintMode = this.addSetting(
      new ModeSetting(
         "Sprint Mode",
         "Legit drops sprint a tick before criticals; Keep Sprint keeps it held; None leaves sprint alone",
         "Legit",
         "Legit",
         "Keep Sprint",
         "None"
      )
   );
   private final BooleanSetting throughWalls = this.addSetting(new BooleanSetting("Through Walls", "Select and attack targets behind blocks", false));
   private final BooleanSetting wallBypass = this.addSetting(
      new BooleanSetting("Wall Bypass", "Through-wall attacks that first send break start/abort actions for blocks along the line of sight", false)
   );
   private final BooleanSetting shieldBreaker = this.addSetting(
      new BooleanSetting("Shield Breaker", "Swap to your best hotbar axe for hits on a raised shield, then back next tick", true)
   );
   private final BooleanSetting shieldUnpress = this.addSetting(new BooleanSetting("Shield Unpress", "Lower your own shield right before attacking", false));
   private final BooleanSetting raycast = this.addSetting(new BooleanSetting("Raycast", "Require a block-free line to the target for each attack", true));
   private final BooleanSetting tPSSync = this.addSetting(new BooleanSetting("TPS Sync", "Scale timing and prediction to the measured server TPS", true));
   private final BooleanSetting eating = this.addSetting(new BooleanSetting("Eating", "Do not attack while eating or drinking", true));
   private final BooleanSetting breaking = this.addSetting(new BooleanSetting("Breaking", "Do not attack while mining", false));
   private final BooleanSetting placing = this.addSetting(new BooleanSetting("Placing", "Do not attack while placing blocks", false));
   private final BooleanSetting inInventory = this.addSetting(new BooleanSetting("In Inventory", "Do not attack with a screen open", false));
   private final BooleanSetting inLava = this.addSetting(new BooleanSetting("In Lava", "Do not attack while in lava", false));
   private final BooleanSetting inWeb = this.addSetting(new BooleanSetting("In Web", "Do not attack while in cobweb", false));
   private final BooleanSetting inWater = this.addSetting(new BooleanSetting("In Water", "Do not attack while in water", false));
   private final ModeSetting movementReference = this.addSetting(
      new ModeSetting("Movement Reference", "Target follows combat facing; Camera uses Move Fix to follow your view", "Target", "Target", "Camera")
   );
   private final ModeSetting moveFix = this.addSetting(
      new ModeSetting(
         "Move Fix",
         "Silent and Free steer your keys toward the camera direction; Targeted walks at the target on W; None moves exactly with the camera",
         "Silent",
         "Silent",
         "Targeted",
         "Free",
         "None"
      )
   );
   private final NumberSetting minCPS = this.addSetting(new NumberSetting("Min CPS", "Lowest 1.8 clicks per second", 8.0, 4.0, 16.0, 0.5));
   private final NumberSetting maxCPS = this.addSetting(new NumberSetting("Max CPS", "Highest 1.8 clicks per second", 11.0, 4.0, 16.0, 0.5));
   private final BooleanSetting autoJump = this.addSetting(new BooleanSetting("Auto Jump", "Jump to set up criticals", false));
   private final BooleanSetting smartAimVisibility = this.addSetting(
      new BooleanSetting("Smart Aim Visibility", "Move the aim point to the nearest visible part of the target", true) {
         @Override
         public String getDisplayName() {
            return "Smart Aim (Visibility)";
         }
      }
   );
   private final BooleanSetting focusTarget = this.addSetting(new BooleanSetting("Focus Target", "Keep the current target while it stays valid", true));
   private final ModeSetting sortMode = this.addSetting(
      new ModeSetting("Sort Mode", "Order for choosing a new target", "Distance", "Distance", "Health", "FOV", "Armor")
   );
   private final BooleanSetting deliberateMiss = this.addSetting(new BooleanSetting("Deliberate Miss", "Occasionally swing just past the target", false));
   private final NumberSetting missThreshold = this.addSetting(
      new NumberSetting("Miss Threshold", "Average hits between deliberate misses", 28.0, 10.0, 80.0, 1.0)
   );
   private final BooleanSetting fOVCone = this.addSetting(new BooleanSetting("FOV Cone", "Show the attack range arc on the ground", true));
   private final NumberSetting indicatorWidth = this.addSetting(new NumberSetting("Indicator Width", "Thickness of FOV range lines", 3.5, 1.0, 6.0, 0.5));
   private final BooleanSetting visibleLines = this.addSetting(new BooleanSetting("Visible Lines", "Keep FOV lines visible over terrain and entities", false));
   private final BooleanSetting hitZone = this.addSetting(new BooleanSetting("Hit Zone", "Terrain positioning guide around the current target", true));
   private final NumberSetting zoneWidth = this.addSetting(new NumberSetting("Zone Width", "Ground boundary stroke width", 2.0, 1.0, 6.0, 0.5));
   private final NumberSetting zoneOpacity = this.addSetting(new NumberSetting("Zone Opacity", "Ground guide opacity", 0.65, 0.05, 1.0, 0.05));
   private final BooleanSetting zoneFill = this.addSetting(new BooleanSetting("Zone Fill", "Faint fill on supported collision surfaces", false));
   private final BooleanSetting zoneBloom = this.addSetting(new BooleanSetting("Zone Bloom", "Soft glow through the shared visual effects", true));
   private final BooleanSetting zoneMotion = this.addSetting(new BooleanSetting("Zone Motion", "Traveling highlights along the terrain boundary", true));
   private final b g = new b();
   private final AttackController ZC = new AttackController(this);
   private final v D = new v();
   private final o H = new o();
   private final r ZU = new r();
   private final c Zb = new c();
   private final t d = new t();
   private final x q = new x();
   private final p z = new p();
   private h Z;
   private LivingEntity K;
   private LivingEntity ZO;
   private float Z6;
   private float l;
   private long Z5;
   private int u = Integer.MIN_VALUE;
   private double j = 1.0;
   private boolean ZL;
   private int p;
   private boolean t;
   private String b = "Idle";
   private int B;
   private int y = Integer.MIN_VALUE;

   public Aura() {
      super("Aura", "Automatically attacks nearby entities", Category.COMBAT, -1);
      A = this;
      this.predictValue.visibleWhen(this.predict::getValue);
      this.moveFix.visibleWhen(() -> this.movementReference.pAsBoolean("Camera"));
      this.predictCriticals.visibleWhen(() -> !this.criticals.pAsBoolean("None"));
      this.randomizeTiming.visibleWhen(() -> !this.fAsBoolean());
      this.minCPS.visibleWhen(this::fAsBoolean);
      this.maxCPS.visibleWhen(this::fAsBoolean);
      this.missThreshold.visibleWhen(this.deliberateMiss::getValue);
      this.indicatorWidth.visibleWhen(this.fOVCone::getValue);
      this.visibleLines.visibleWhen(this.fOVCone::getValue);
      this.zoneWidth.visibleWhen(this.hitZone::getValue);
      this.zoneOpacity.visibleWhen(this.hitZone::getValue);
      this.zoneFill.visibleWhen(this.hitZone::getValue);
      this.zoneBloom.visibleWhen(this.hitZone::getValue);
      this.zoneMotion.visibleWhen(this.hitZone::getValue);
      this.rotation.onChanged(var1 -> this.xAsVoid("Rotation engine: " + var1));
   }

   public static Aura fAsAura() {
      return A;
   }

   @Override
   public String getDisplayName() {
      return "Kill Aura";
   }

   public boolean SAsBoolean() {
      return this.activation.pAsBoolean("Hold");
   }

   @Override
   public ModeSetting keybindMode() {
      return this.activation;
   }

   @Override
   public void toggle() {
      if (this.SAsBoolean() && mc != null && mc.inGameHud != null && mc.currentScreen == null) {
         this.setEnabled(true);
      } else {
         super.toggle();
      }
   }

   private boolean mAsBoolean() {
      if (!this.SAsBoolean()) {
         return true;
      } else {
         int var1 = this.getKeyBind();
         if (KeybindNames.hAsBoolean(var1) && mc.getWindow() != null) {
            return mc.currentScreen == null && mc.isWindowFocused() ? KeybindNames.sAsBoolean(mc.getWindow().getHandle(), var1) : false;
         } else {
            return false;
         }
      }
   }

   private static String nAsString(String var0) {
      if (!"Balanced".equalsIgnoreCase(var0) && !"Balant".equalsIgnoreCase(var0)) {
         return "Legit".equalsIgnoreCase(var0) ? "Normal" : var0;
      } else {
         return "Blatant";
      }
   }

   private static String kAsString(String var0) {
      if ("TEST".equalsIgnoreCase(var0)) {
         return "Legit";
      } else {
         return !"HolyWorld".equals(var0) && !"Neuro".equals(var0) && !"Smooth".equals(var0) ? var0 : "Sloth";
      }
   }

   @Override
   public boolean allowsScreenInteractions() {
      return !this.inInventory.getValue();
   }

   @Override
   public Module.Type_s getTickPhase() {
      return Module.Type_s.INTENT;
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(k.VANILLA_INPUT, k.SERVER_STATE_MODIFYING);
   }

   public List<Setting<?>> cAsList() {
      return List.of(
         this.rotation,
         this.auraMode,
         this.movementReference,
         this.moveFix,
         this.aimPoint,
         this.predict,
         this.predictValue,
         this.smartAimVisibility,
         this.rotationETA
      );
   }

   public List<Setting<?>> zAsList() {
      return List.of(
         this.combatVersion,
         this.attackRange,
         this.aimRange,
         this.reachBuffer,
         this.dynamicSafeReach,
         this.criticals,
         this.predictCriticals,
         this.sprintMode,
         this.autoJump,
         this.randomizeTiming,
         this.minCPS,
         this.maxCPS,
         this.entityRaycast,
         this.deliberateMiss,
         this.missThreshold
      );
   }

   public List<Setting<?>> GAsList() {
      return List.of(this.players, this.mobs, this.animals, this.invisibles, this.nakedPlayers);
   }

   public List<Setting<?>> MAsList() {
      return List.of(this.fov, this.sortMode, this.focusTarget);
   }

   public List<Setting<?>> PAsList() {
      return List.of(this.throughWalls, this.wallBypass, this.shieldBreaker, this.shieldUnpress, this.raycast, this.tPSSync);
   }

   public List<Setting<?>> mAsList() {
      return List.of(this.eating, this.breaking, this.placing, this.inInventory, this.inLava, this.inWeb, this.inWater);
   }

   public List<Setting<?>> ZAsList() {
      return List.of(
         this.fOVCone, this.indicatorWidth, this.visibleLines, this.hitZone, this.zoneWidth, this.zoneOpacity, this.zoneFill, this.zoneBloom, this.zoneMotion
      );
   }

   @Override
   public void onEnable() {
      this.B = 3;
      this.y = Integer.MIN_VALUE;
      this.ZC.LAsVoid();
      this.g.CAsVoid();
      this.D.oAsVoid();
      s.QAsVoid();
      this.K = this.ZO = null;
      Y = false;
      this.Z6 = this.l = 0.0F;
      this.Z5 = 0L;
      this.u = Integer.MIN_VALUE;
      this.j = 1.0;
      this.MAsVoid();
      this.b = "Idle";
      ClientLog.GAsVoid(
         "Aura",
         "Movement: Reference="
            + this.movementReference.getValue()
            + "; Move Fix="
            + this.moveFix.getValue()
            + "; vanilla sprint eligibility; attacks wait for matching sprint reports"
      );
   }

   @Override
   public void onDisable() {
      this.K = this.ZO = null;
      Y = false;
      this.Z6 = this.l = 0.0F;
      this.Z5 = 0L;
      this.u = Integer.MIN_VALUE;
      this.j = 1.0;
      this.MAsVoid();
      this.VAsVoid();
      if (this.Z != null) {
         this.Z.reset();
      }

      this.Z = null;
      this.ZC.VAsVoid(this.getPlayer());
      this.ZC.GAsVoid(mc);
      this.ZC.LAsVoid();
      this.g.CAsVoid();
      this.D.oAsVoid();
      s.QAsVoid();
      this.b = "Idle";
   }

   @Override
   public void onTick() {
      this.ZC.wAsVoid(mc);
      if (this.isInGame()) {
         ClientPlayerEntity var1 = mc.player;
         if (!this.mAsBoolean()) {
            this.IAsVoid("Hold released");
         } else if (ModuleImportBroker.tAsBoolean(this) && !var1.isSpectator() && var1.isAlive()) {
            if (this.p > 0) {
               this.p--;
            }

            boolean var2 = this.auraMode.pAsBoolean("Blatant");
            boolean var3 = this.ZAsBoolean();
            int var4 = !var3 && this.ZC.PAsBoolean(var1, this.fAsBoolean(), this.tPSSync.getValue(), this.randomizeTiming.getValue(), var2) ? 1 : 0;
            float var5 = this.ZC.wAsFloat(var1, this.fAsBoolean(), this.tPSSync.getValue(), this.randomizeTiming.getValue(), var2);
            int var6 = !this.throughWalls.getValue() && !this.wallBypass.getValue() ? 0 : 1;
            b.Type_p var7 = new b.Type_p(
               this.attackRange.getValue() + this.aimRange.getValue(),
               this.players.getValue(),
               this.mobs.getValue(),
               this.animals.getValue(),
               this.invisibles.getValue(),
               this.nakedPlayers.getValue(),
               this.focusTarget.getValue(),
               var6 != 0,
               this.sortMode.getValue(),
               this.fov.getValue()
            );
            this.K = this.g.HAsLivingEntity(var7);
            RotationAngles var8 = new RotationAngles(var1.getYaw(), var1.getPitch());
            if (this.K != this.ZO) {
               if (this.Z != null) {
                  this.Z.targetChanged(this.K);
               }

               s.QAsVoid();
               if (this.K != null) {
                  this.Z5 = System.currentTimeMillis();
                  this.ZC.CAsVoid();
                  this.xAsVoid("Target acquired: entity=" + this.K.getId());
               } else {
                  this.VAsVoid();
                  this.xAsVoid("Target lost");
               }

               this.ZO = this.K;
            }

            if (this.K == null) {
               this.MAsVoid();
               Y = false;
               this.ZC.VAsVoid(var1);
               this.Z5 = 0L;
               this.VAsVoid();
               this.b = "No target";
            } else {
               h var9 = this.TAsH();
               if (var9 != this.Z) {
                  if (this.Z != null) {
                     this.Z.reset();
                  }

                  this.Z = var9;
               }

               Box var10 = this.K.getBoundingBox();
               double var11 = this.mAsDouble(this.K);
               double var52;
               int var13 = ((var52 = com.larpclient.module.y.sAsDouble(var1, var10) - var11) == 0.0 ? 0 : (var52 < 0.0 ? -1 : 1)) <= 0 ? 1 : 0;
               float var14 = 1.25F;
               if (var13 != 0) {
                  RotationAngles var15 = lk.XAsL6(s.kAsVec3d(var10));
                  var14 = Math.max(1.25F, (float)this.Z.ticksToReach(var8, var15) + 0.35F);
                  var14 = Math.max(var14, var2 ? 1.0F : 4.5F);
               }

               int var39 = !var3 && var13 != 0 && (var4 != 0 || var5 <= var14) ? 1 : 0;
               int var16 = var13 != 0 && this.Z5 > 0L && System.currentTimeMillis() - this.Z5 < (var2 ? 150L : 500L) ? 1 : 0;
               int var17 = var13 != 0 && (var2 || var39 != 0 || var16 != 0) ? 1 : 0;
               Vec3d var18 = this.QAsVec3d(this.K, var17 != 0);
               RotationAngles var19 = lk.XAsL6(var18);
               boolean var421 = com.larpclient.module.y.sAsDouble(var1, var10) <= var11 && (var2 || var39 != 0);
               int var20 = var421 && (var4 != 0 || var5 <= (var2 ? 0.5F : 1.15F)) ? 1 : 0;
               boolean var21 = this.PAsBoolean(var20 != 0);
               int var22 = this.Z == this.z ? 1 : 0;
               if (this.Z5 > 0L && !var421 && var22 == 0) {
                  long var23 = System.currentTimeMillis() - this.Z5;
                  long var25 = var2 ? 0L : 120L;
                  float var27 = MathHelper.clamp((float)var23 / (float)var25, 0.0F, 1.0F);
                  if (var27 < 1.0F) {
                     var19 = new RotationAngles(var8.n + lk.WAsFloat(var8.n, var19.n) * var27, var8.m + (var19.m - var8.m) * var27);
                  }
               }

               RotationAngles var41 = xAsL6(var1);
               boolean var24 = com.larpclient.module.y.BAsBoolean(
                  var1, var10.expand((double)this.K.getTargetingMargin()), var41.n, var41.m, this.attackRange.getValue() + 1.0, var6 != 0
               );
               RotationAngles var42x = this.Z.rotate(var8, var19, this.K, var421, var24);
               if (var2 && var22 == 0) {
                  this.Z6 += 0.015F;
                  double var26 = this.H.dAsDouble((double)this.Z6, 0.0, 0.0);
                  double var28 = this.H.dAsDouble(0.0, (double)this.Z6, 0.0);
                  float var30 = var421 ? 0.15F : 1.0F;
                  var42x.n += (float)(var26 * 0.12F * (double)var30);
                  var42x.m = MathHelper.clamp(var42x.m + (float)(var28 * 0.08F * (double)var30), -90.0F, 90.0F);
               }

               if (this.deliberateMiss.getValue()) {
                  this.D.GAsVoid(this.missThreshold.getValue().intValue());
                  this.D.yAsVoid();
                  if (this.D.UAsBoolean()) {
                     var42x = this.D.SAsL6(var42x);
                  }
               } else {
                  this.D.oAsVoid();
               }

               if (this.Z == this.z) {
                  var42x = this.z.vAsL6(var8, var42x);
               } else {
                  var42x = lk.UAsL6(var8, var42x);
               }

               this.qAsVoid(var42x);
               if (!var21 && !var3 && (!this.deliberateMiss.getValue() || !this.D.UAsBoolean())) {
                  boolean var44 = com.larpclient.module.y.BAsBoolean(
                     var1, var10.expand((double)this.K.getTargetingMargin()), var42x.n, var42x.m, var11, var6 != 0
                  );
                  RotationAngles var45 = !var2 && !var44 ? var41 : var42x;
                  LivingEntity var46 = this.K;
                  Box var29 = var10;
                  double var47 = var11;
                  byte var32 = 1;
                  if (this.entityRaycast.getValue()) {
                     EntityHitResult var33 = com.larpclient.module.y.hAsEntityHitResult(var1, var45.n, var45.m, var11, var6 != 0);
                     var32 = 0;
                     if (var33 != null && var33.getEntity() instanceof LivingEntity var34 && this.g.MAsBoolean(var34, var7)) {
                        double var50 = this.mAsDouble(var34);
                        Box var37 = var34.getBoundingBox();
                        if (com.larpclient.module.y.sAsDouble(var1, var37) <= var50) {
                           var46 = var34;
                           var29 = var37;
                           var47 = var50;
                           var32 = 1;
                        }
                     }
                  }

                  LivingEntity var48 = var46;
                  Runnable var49 = this.wallBypass.getValue() ? () -> zAsVoid(var48) : null;
                  boolean var51 = this.ZC
                     .tAsBoolean(
                        var1,
                        new AttackController.Type_b(
                           var46,
                           var29,
                           var45.n,
                           var45.m,
                           var47,
                           var6 != 0,
                           this.LAsN_Type_s(),
                           this.zAsN_Type_h(),
                           this.shieldBreaker.getValue(),
                           this.eating.getValue(),
                           this.raycast.getValue() && var6 == 0,
                           var32 != 0,
                           this.autoJump.getValue(),
                           this.predictCriticals.getValue(),
                           var49,
                           this.fAsBoolean(),
                           this.tPSSync.getValue(),
                           this.randomizeTiming.getValue(),
                           var2,
                           this.minCPS.getValue().floatValue(),
                           this.maxCPS.getValue().floatValue()
                        )
                     );
                  this.vAsVoid();
                  if (var51) {
                     this.xAsVoid("Attack executed: entity=" + var46.getId());
                     if (this.deliberateMiss.getValue()) {
                        this.D.AAsVoid();
                     }
                  }
               } else if (var3) {
                  this.ZC.VAsVoid(var1);
               }

               this.b = var13 != 0 ? (var4 != 0 ? "Attacking" : "Charging") : "Tracking";
            }
         } else {
            this.IAsVoid("Interaction unavailable");
         }
      }
   }

   private void IAsVoid(String var1) {
      if (this.K != null || this.ZO != null) {
         this.xAsVoid("Paused: " + var1);
      }

      this.K = this.ZO = null;
      Y = false;
      this.ZC.VAsVoid(this.getPlayer());
      this.MAsVoid();
      this.VAsVoid();
      this.b = var1;
   }

   private double mAsDouble(LivingEntity var1) {
      double var2 = Math.max(0.0, this.attackRange.getValue() - this.reachBuffer.getValue());
      if (this.dynamicSafeReach.getValue() && mc.player != null && var1 != null) {
         Box var4 = var1.getBoundingBox();
         double var5 = (var4.minX + var4.maxX) * 0.5 - mc.player.getX();
         double var7 = (var4.minZ + var4.maxZ) * 0.5 - mc.player.getZ();
         double var9 = var5 * var5 + var7 * var7;
         if (var9 <= 1.0E-6) {
            return var2;
         } else {
            double var11 = 1.0 / Math.sqrt(var9);
            var5 *= var11;
            var7 *= var11;
            Vec3d var13 = mc.player.getVelocity();
            Vec3d var14 = var1.getVelocity();
            double var15 = Math.abs((var14.x - var13.x) * var5 + (var14.z - var13.z) * var7);
            boolean var17 = this.auraMode.pAsBoolean("Blatant");
            double var18 = var15 * this.OAsDouble() * (var17 ? 0.15 : 0.4);
            double var20 = MathHelper.clamp(var18, 0.0, var17 ? 0.08 : 0.25);
            return Math.max(0.0, var2 - var20);
         }
      } else {
         return var2;
      }
   }

   private double OAsDouble() {
      int var1 = mc.player.age;
      if (this.u == Integer.MIN_VALUE || var1 < this.u || var1 - this.u >= 20) {
         double var2 = 1.0;
         if (mc.getNetworkHandler() != null) {
            PlayerListEntry var4 = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
            if (var4 != null) {
               var2 = (double)((float)var4.getLatency() / 50.0F);
            }
         }

         this.j = MathHelper.clamp(var2, 0.5, 3.0);
         this.u = var1;
      }

      return this.j;
   }

   private Vec3d QAsVec3d(LivingEntity var1, boolean var2) {
      double var3 = this.attackRange.getValue() + this.aimRange.getValue();
      int var5 = this.Z == this.z ? 1 : 0;
      int var7 = this.Z == this.z && this.predict.getValue() && !var2 ? 1 : 0;
      Vec3d var8 = var7 != 0
         ? com.larpclient.module.l.AAsVec3d(var1, this.JAsDouble())
         : (this.predict.getValue() && !var2 && var5 == 0 ? com.larpclient.module.l.AAsVec3d(var1, this.fAsDouble()) : var1.getEntityPos());
      Box var9 = var1.getBoundingBox().offset(var8.x - var1.getX(), var8.y - var1.getY(), var8.z - var1.getZ());
      Vec3d var10;
      if (var5 == 0
         && !this.aimPoint.pAsBoolean("Center")
         && (
            !this.auraMode.pAsBoolean("Normal")
               || this.aimPoint.pAsBoolean("Head")
               || this.aimPoint.pAsBoolean("Body")
               || this.aimPoint.pAsBoolean("Multipoint")
         )) {
         if (this.aimPoint.pAsBoolean("Multipoint")) {
            var10 = s.dAsVec3d(var1, var9, var3);
         } else if (this.aimPoint.pAsBoolean("Head")) {
            var10 = new Vec3d(var9.getCenter().x, var9.minY + var9.getLengthY() * 0.78F, var9.getCenter().z);
         } else if (this.aimPoint.pAsBoolean("Body")) {
            var10 = new Vec3d(var9.getCenter().x, var9.minY + var9.getLengthY() * 0.55F, var9.getCenter().z);
         } else {
            var10 = s.kAsVec3d(var9);
         }
      } else {
         var10 = new Vec3d(var9.getCenter().x, var9.minY + var9.getLengthY() * 0.6F, var9.getCenter().z);
      }

      if (var2 && var5 != 0) {
         var10 = HAsVec3d(var1, var9, var10);
      }

      int var11 = var2 || !this.auraMode.pAsBoolean("Normal") && var5 == 0 || var5 != 0 && var5 == 0 || var5 != 0 && pAsBoolean(var1) ? 0 : 1;
      if (var11 != 0) {
         float var12 = var5 != 0 ? 0.032F : 0.012F;
         double var13 = (double)(var1.getWidth() * (var5 != 0 ? 0.24F : 0.15F));
         double var15 = (double)(var1.getHeight() * (var5 != 0 ? 0.16F : 0.12F));
         this.l += var12;
         double var17 = this.H.dAsDouble((double)this.l, 120.0, 0.0) * var13;
         double var19 = this.H.dAsDouble(0.0, (double)this.l, 240.0) * var15;
         double var21 = this.H.dAsDouble(0.0, 120.0, (double)this.l) * var13;
         var10 = pAsVec3d(var10.add(var17, var19, var21), var9);
      }

      if (this.smartAimVisibility.getValue() && !this.throughWalls.getValue() && !this.wallBypass.getValue()) {
         var10 = s.aAsVec3d(var9, var10, var3);
      }

      return var10;
   }

   private double fAsDouble() {
      double var1 = 1.0;
      if (this.tPSSync.getValue()) {
         float var3 = com.larpclient.module.q.HAsFloat();
         if (Float.isFinite(var3) && var3 > 0.0F) {
            var1 = (double)(20.0F / MathHelper.clamp(var3, 10.0F, 20.0F));
         }
      }

      return MathHelper.clamp(this.predictValue.getValue() * var1, 0.0, 4.0);
   }

   private double JAsDouble() {
      return MathHelper.clamp(Math.max(1.0, this.fAsDouble()) + 1.0, 1.0, 3.0);
   }

   private static Vec3d HAsVec3d(LivingEntity var0, Box var1, Vec3d var2) {
      ClientPlayerEntity var3 = MinecraftClient.getInstance().player;
      if (var3 != null && var0 != null && var1 != null) {
         Vec3d var4 = var0.getVelocity();
         Vec3d var5 = var3.getVelocity();
         double var6 = (var0.getX() - var0.lastX) * 0.7F + var4.x * 0.3F - var5.x;
         double var8 = (var0.getY() - var0.lastY) * 0.7F + var4.y * 0.3F - var5.y;
         double var10 = (var0.getZ() - var0.lastZ) * 0.7F + var4.z * 0.3F - var5.z;
         double var12 = Math.min(0.08F, var1.getLengthX() * 0.25);
         double var14 = Math.min(0.08F, var1.getLengthZ() * 0.25);
         double var16 = Math.min(0.12F, var1.getLengthY() * 0.12F);
         double var18 = var1.getLengthX() * 0.22F;
         double var20 = var1.getLengthZ() * 0.22F;
         double var22 = var1.getLengthY() * 0.1F;
         Vec3d var24 = var1.getCenter();
         return new Vec3d(
            MathHelper.clamp(var24.x + MathHelper.clamp(var6 * 0.65F, -var18, var18), var1.minX + var12, var1.maxX - var12),
            MathHelper.clamp(var24.y + MathHelper.clamp(var8 * 0.45F, -var22, var22), var1.minY + var16, var1.maxY - var16),
            MathHelper.clamp(var24.z + MathHelper.clamp(var10 * 0.65F, -var20, var20), var1.minZ + var14, var1.maxZ - var14)
         );
      } else {
         return var2;
      }
   }

   private static boolean pAsBoolean(LivingEntity var0) {
      ClientPlayerEntity var1 = MinecraftClient.getInstance().player;
      if (var0 != null && var1 != null) {
         Vec3d var2 = var0.getVelocity();
         Vec3d var3 = var1.getVelocity();
         double var4 = (var0.getX() - var0.lastX) * 0.7F + var2.x * 0.3F - var3.x;
         double var6 = (var0.getZ() - var0.lastZ) * 0.7F + var2.z * 0.3F - var3.z;
         return var4 * var4 + var6 * var6 >= 0.0025F;
      } else {
         return false;
      }
   }

   private static Vec3d pAsVec3d(Vec3d var0, Box var1) {
      return new Vec3d(
         MathHelper.clamp(var0.x, var1.minX + 0.001F, var1.maxX - 0.001F),
         MathHelper.clamp(var0.y, var1.minY + 0.001F, var1.maxY - 0.001F),
         MathHelper.clamp(var0.z, var1.minZ + 0.001F, var1.maxZ - 0.001F)
      );
   }

   private h TAsH() {
      String var1 = this.rotation.getValue();

      return (h)(switch (var1) {
         case "FunTime" -> this.Zb;
         case "Polar" -> this.d;
         case "Matrix" -> this.q;
         case "Legit" -> this.z;
         default -> this.ZU;
      });
   }

   private AttackController.Type_s LAsN_Type_s() {
      String var1 = this.criticals.getValue();

      return switch (var1) {
         case "Always" -> AttackController.Type_s.ALWAYS;
         case "None" -> AttackController.Type_s.NONE;
         default -> AttackController.Type_s.ADAPTIVE;
      };
   }

   private AttackController.Type_h zAsN_Type_h() {
      String var1 = this.sprintMode.getValue();

      return switch (var1) {
         case "Keep Sprint" -> AttackController.Type_h.KEEP;
         case "None" -> AttackController.Type_h.NONE;
         default -> AttackController.Type_h.LEGIT;
      };
   }

   private boolean fAsBoolean() {
      return this.combatVersion.pAsBoolean("1.8");
   }

   private boolean ZAsBoolean() {
      if (mc.player != null && mc.world != null) {
         AutoTrap var1 = AutoTrap.oAsAutoTrap();
         AutoWeb var2 = LarpClient.CAsLarpClient().mAsF().EAsModule(AutoWeb.class);
         if (var1 != null && var1.isRuntimeActive() && BlockPlacementController.tAsBoolean(var1)) {
            return true;
         } else if (var2 != null && var2.isRuntimeActive() && BlockPlacementController.tAsBoolean(var2)) {
            return true;
         } else {
            AutoAnchor var3 = AutoAnchor.SAsAutoAnchor();
            if (var3 != null && var3.isRuntimeActive() && BlockPlacementController.tAsBoolean(var3)) {
               return true;
            } else if (this.eating.getValue() && com.larpclient.module.y.WAsBoolean()) {
               return true;
            } else if (this.breaking.getValue() && this.dAsBoolean()) {
               return true;
            } else if (this.placing.getValue() && this.AAsBoolean()) {
               return true;
            } else if (this.inInventory.getValue() && mc.currentScreen != null) {
               return true;
            } else if (this.inLava.getValue() && mc.player.isInLava()) {
               return true;
            } else {
               return this.inWeb.getValue() && com.larpclient.module.y.lAsBoolean()
                  ? true
                  : this.inWater.getValue() && (mc.player.isTouchingWater() || mc.player.isSubmergedInWater());
            }
         }
      } else {
         return true;
      }
   }

   private boolean dAsBoolean() {
      return mc.interactionManager != null && mc.interactionManager.isBreakingBlock()
         ? true
         : mc.options != null && mc.options.attackKey.isPressed() && mc.crosshairTarget != null && mc.crosshairTarget.getType() == Type.BLOCK;
   }

   private boolean AAsBoolean() {
      return mc.options != null && mc.options.useKey.isPressed()
         ? mc.player.getMainHandStack().getItem() instanceof BlockItem || mc.player.getOffHandStack().getItem() instanceof BlockItem
         : false;
   }

   private boolean PAsBoolean(boolean var1) {
      if (!this.shieldUnpress.getValue() || !var1) {
         this.MAsVoid();
         return false;
      } else if (this.ZL) {
         return false;
      } else if (!mc.player.isUsingItem() || !(mc.player.getActiveItem().getItem() instanceof ShieldItem)) {
         return false;
      } else if (mc.interactionManager == null) {
         return false;
      } else {
         if (mc.options != null) {
            mc.options.useKey.setPressed(false);
            this.t = true;
         }

         mc.interactionManager.stopUsingItem(mc.player);
         this.ZL = true;
         return true;
      }
   }

   private void vAsVoid() {
      if (this.ZL) {
         this.p = 1;
      }

      this.ZL = false;
      this.YAsVoid();
   }

   private void MAsVoid() {
      this.ZL = false;
      this.p = 0;
      this.YAsVoid();
   }

   private void YAsVoid() {
      if (this.t && mc.options != null) {
         int var1 = this.uAsInt();
         int var2;
         if (var1 >= 0 && mc.getWindow() != null) {
            var2 = GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), var1) == 1 ? 1 : 0;
         } else {
            var2 = mc.options.useKey.isPressed() ? 1 : 0;
         }

         mc.options.useKey.setPressed(var2 != 0);
         this.t = false;
      } else {
         this.t = false;
      }
   }

   private int uAsInt() {
      if (mc.options == null) {
         return -1;
      } else {
         for (int var1 = 0; var1 <= 7; var1++) {
            if (mc.options.useKey.matchesMouse(new Click(0.0, 0.0, new MouseInput(var1, 0)))) {
               return var1;
            }
         }

         return -1;
      }
   }

   private static void zAsVoid(LivingEntity var0) {
      MinecraftClient var1 = MinecraftClient.getInstance();
      if (var1.player != null && var1.world != null && var0 != null) {
         Vec3d var2 = var1.player.getEyePos();
         Vec3d var3 = var0.getEyePos().subtract(var2);
         double var4 = var3.length();
         if (!(var4 < 0.01)) {
            Vec3d var6 = var3.normalize();

            for (double var7 = 0.5; var7 < var4; var7 += 0.5) {
               Vec3d var9 = var2.add(var6.multiply(var7));
               BlockPos var10 = new BlockPos((int)Math.floor(var9.x), (int)Math.floor(var9.y), (int)Math.floor(var9.z));
               if (!var1.world.isAir(var10)) {
                  var1.player.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.START_DESTROY_BLOCK, var10, Direction.UP));
                  var1.player.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.ABORT_DESTROY_BLOCK, var10, Direction.UP));
               }
            }
         }
      }
   }

   private void qAsVoid(RotationAngles var1) {
      if (mc.currentScreen == null && !ModuleKeybindChecker.NAsBoolean("Freelook") && !ModuleKeybindChecker.NAsBoolean("Freecam")) {
         ParticleEmitter.jAsVoid(ParticleEmitter.Type_u.AURA);
      }

      ModuleDetector.RAsVoid(this, ModuleDetector.Type_s.AURA, var1.n, var1.m, 1.0);
   }

   private void VAsVoid() {
      ParticleEmitter.GAsVoid(ParticleEmitter.Type_u.AURA);
   }

   private static RotationAngles xAsL6(ClientPlayerEntity var0) {
      ClientPlayerEntityAccessor var1 = (ClientPlayerEntityAccessor)var0;
      return new RotationAngles(var1.larpYRotLast(), var1.larpXRotLast());
   }

   public boolean wAsBoolean() {
      return this.isEnabled() ? this.isRuntimeActive() && this.K != null && ModuleImportBroker.fAsBoolean(this) : false;
   }

   public boolean DAsBoolean() {
      return this.isEnabled()
         ? this.wAsBoolean() && this.movementReference.pAsBoolean("Camera") && this.moveFix.pAsBoolean("None") && ParticleEmitter.NAsP5_Type_u() == ParticleEmitter.Type_u.AURA
         : false;
   }

   public static PlayerInput qAsPlayerInput(ClientPlayerEntity var0, PlayerInput var1) {
      if (A != null && A.isEnabled()) {
         Aura var2 = A;
         if (var2 != null && var2.isRuntimeActive() && var2.K != null && var0 != null) {
            float var3 = var1.forward() == var1.backward() ? 0.0F : (var1.forward() ? 1.0F : -1.0F);
            byte var4 = (byte)(var1.sprint() ? 1 : 0);
            if (var2.ZC.KAsBoolean(var0)) {
               var4 = 0;
            } else if (var2.sprintMode.pAsBoolean("Keep Sprint")
               && var3 > 0.0F
               && !var1.sneak()
               && !var0.horizontalCollision
               && !var0.isTouchingWater()
               && !var0.isSubmergedInWater()) {
               var4 = 1;
            } else if (var2.sprintMode.pAsBoolean("Legit")
               && (var1.sprint() || getMc().options != null && getMc().options.sprintKey.isPressed())
               && var3 > 0.0F
               && !var1.sneak()
               && !var0.horizontalCollision) {
               var4 = 1;
            }

            PlayerInput var5 = new PlayerInput(var1.forward(), var1.backward(), var1.left(), var1.right(), var1.jump(), var1.sneak(), var4 != 0);
            if (ParticleEmitter.NAsP5_Type_u() == ParticleEmitter.Type_u.AURA) {
               double var6 = var2.K.getX() - var0.getX();
               double var8 = var2.K.getZ() - var0.getZ();
               double var10 = MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(var8, var6)) - 90.0);
               var5 = var2.mAsPlayerInput(var0.input, var5, var0.getYaw(), var2.movementReference.pAsBoolean("Camera") ? FAsFloat() : var0.getYaw(), var10);
            }

            return var5;
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public PlayerInput mAsPlayerInput(Input var1, PlayerInput var2, float var3, float var4, double var5) {
      if (!this.isEnabled()) {
         return var2;
      } else if (!this.movementReference.pAsBoolean("Target") && !this.moveFix.pAsBoolean("None")) {
         float var7 = var2.forward() == var2.backward() ? 0.0F : (var2.forward() ? 1.0F : -1.0F);
         float var8 = var2.left() == var2.right() ? 0.0F : (var2.left() ? 1.0F : -1.0F);
         if (var7 == 0.0F && var8 == 0.0F) {
            return var2;
         } else {
            double var9 = this.moveFix.pAsBoolean("Targeted") && var7 > 0.0F && var8 == 0.0F
               ? var5
               : MathHelper.wrapDegrees(Math.toDegrees(rAsDouble(var4, var7, var8)));
            float[] var11 = sAsFloatArray(var9, var3);
            var1.movementVector = new Vec2f(var11[1], var11[0]).normalize();
            return new PlayerInput(var11[0] > 0.0F, var11[0] < 0.0F, var11[1] > 0.0F, var11[1] < 0.0F, var2.jump(), var2.sneak(), var2.sprint());
         }
      } else {
         return var2;
      }
   }

   public static double rAsDouble(float var0, float var1, float var2) {
      if (var1 < 0.0F) {
         var0 += 180.0F;
      }

      float var3 = 1.0F;
      if (var1 < 0.0F) {
         var3 = -0.5F;
      }

      if (var1 > 0.0F) {
         var3 = 0.5F;
      }

      if (var2 > 0.0F) {
         var0 -= 90.0F * var3;
      }

      if (var2 < 0.0F) {
         var0 += 90.0F * var3;
      }

      return Math.toRadians((double)var0);
   }

   public static float[] sAsFloatArray(double var0, float var2) {
      double var3 = MathHelper.wrapDegrees(var0 - (double)var2);
      if (var3 >= -22.5 && var3 <= 22.5) {
         return new float[]{1.0F, 0.0F};
      } else if (var3 > 22.5 && var3 <= 67.5) {
         return new float[]{1.0F, -1.0F};
      } else if (var3 > 67.5 && var3 <= 112.5) {
         return new float[]{0.0F, -1.0F};
      } else if (var3 > 112.5 && var3 <= 157.5) {
         return new float[]{-1.0F, -1.0F};
      } else if (var3 > 157.5 || var3 <= -157.5) {
         return new float[]{-1.0F, 0.0F};
      } else if (var3 < -22.5 && var3 >= -67.5) {
         return new float[]{1.0F, 1.0F};
      } else {
         return var3 < -67.5 && var3 >= -112.5 ? new float[]{0.0F, 1.0F} : new float[]{-1.0F, 1.0F};
      }
   }

   public static boolean WAsBoolean() {
      if (A != null && A.isEnabled()) {
         Aura var0 = A;
         return var0 != null && var0.isRuntimeActive() && var0.shieldUnpress.getValue() && rAsClientPlayerEntity() != null && (var0.ZL || var0.p > 0);
      } else {
         return false;
      }
   }

   public float BAsFloat(float var1, float var2) {
      if (this.isEnabled()) {
         if (!this.wAsBoolean()) {
            return var1;
         } else if (this.HAsBoolean()) {
            float var4 = MathHelper.wrapDegrees(FAsFloat() - var1);
            return Math.abs(var4) > 0.5F ? var1 + var4 * 0.45F : FAsFloat();
         } else {
            boolean var3 = this.auraMode.pAsBoolean("Normal");
            return WAsFloat(var1, var2, var3 ? 35.0F : 50.0F, var3 ? 0.25F : 0.3F);
         }
      } else {
         return var1;
      }
   }

   public static float WAsFloat(float var0, float var1, float var2, float var3) {
      float var4 = MathHelper.wrapDegrees(var1 - var0);
      if (Math.abs(var4) <= var2) {
         return var0;
      } else {
         float var5 = var4 < 0.0F ? var1 + var2 : var1 - var2;
         return var0 + MathHelper.wrapDegrees(var5 - var0) * var3;
      }
   }

   private boolean HAsBoolean() {
      ClientPlayerEntity var1 = rAsClientPlayerEntity();
      if (this.movementReference.pAsBoolean("Camera") && this.moveFix.pAsBoolean("Targeted") && var1 != null && var1.input != null && this.K != null) {
         Vec2f var2 = var1.input.getMovementInput();
         if (!(var2.y <= 0.0F) && !(Math.abs(var2.x) > 0.05F)) {
            float var3 = MathHelper.wrapDegrees((float)Math.toDegrees(Math.atan2(this.K.getZ() - var1.getZ(), this.K.getX() - var1.getX())) - 90.0F);
            return Math.abs(MathHelper.wrapDegrees(var3 - FAsFloat())) <= 15.0F;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private static ClientPlayerEntity rAsClientPlayerEntity() {
      return getMc().player;
   }

   public static boolean rAsBoolean() {
      if (A != null && A.isEnabled()) {
         Aura var0 = A;
         return var0 != null && var0.isRuntimeActive() && Y;
      } else {
         return false;
      }
   }

   public static float FAsFloat() {
      MinecraftClient var0 = MinecraftClient.getInstance();
      return gAsBoolean() ? ParticleEmitter.sAsFloatArray(ParticleEmitter.JAsFloat(), ParticleEmitter.mAsFloat(), var0.options.getPerspective().isFrontView())[0] : var0.player.getYaw();
   }

   private static boolean gAsBoolean() {
      ParticleEmitter.Type_u var0 = ParticleEmitter.NAsP5_Type_u();
      return var0 == ParticleEmitter.Type_u.AURA || var0 == ParticleEmitter.Type_u.MACE;
   }

   public static float KAsFloat() {
      MinecraftClient var0 = MinecraftClient.getInstance();
      return gAsBoolean() ? ParticleEmitter.sAsFloatArray(ParticleEmitter.JAsFloat(), ParticleEmitter.mAsFloat(), var0.options.getPerspective().isFrontView())[1] : var0.player.getPitch();
   }

   public static Vec3d YAsVec3d() {
      return lk.MAsVec3d(FAsFloat(), KAsFloat());
   }

   public LivingEntity hAsLivingEntity() {
      if (this.isEnabled()) {
         return this.isRuntimeActive() ? this.K : null;
      } else {
         return null;
      }
   }

   public double fOV() {
      return this.fov.getValue();
   }

   public double uAsDouble() {
      return ModuleImportBroker.MAsDouble(this.vAsDouble());
   }

   private double vAsDouble() {
      return Math.max(0.0, this.attackRange.getValue() - this.reachBuffer.getValue());
   }

   public boolean XAsBoolean() {
      return this.isEnabled() ? this.isOperational() && this.isInGame() && mc.currentScreen == null && this.fOVCone.getValue() && this.mAsBoolean() : false;
   }

   public float indicatorWidth() {
      return this.indicatorWidth.getValue().floatValue();
   }

   public boolean visibleLines() {
      return this.visibleLines.getValue();
   }

   public boolean qAsBoolean() {
      return this.isEnabled() ? this.isRuntimeActive() && this.K != null && this.hitZone.getValue() && ModuleImportBroker.jAsBoolean() && this.mAsBoolean() : false;
   }

   public float zoneWidth() {
      return this.zoneWidth.getValue().floatValue();
   }

   public double zoneOpacity() {
      return this.zoneOpacity.getValue();
   }

   public boolean zoneFill() {
      return this.zoneFill.getValue();
   }

   public boolean zoneBloom() {
      return this.zoneBloom.getValue();
   }

   public boolean zoneMotion() {
      return this.zoneMotion.getValue();
   }

   public String SAsString() {
      if (this.K != null && mc.player != null) {
         String var1 = ModuleImportBroker.MAsString(this.K, this.vAsDouble(), mc.player.getRotationVector());
         return "range".equals(var1) ? "Outside reach" : (var1 == null ? "Valid attack ray" : "Inside reach: " + var1);
      } else {
         return "No lock";
      }
   }

   @Override
   public String getStatus() {
      return this.b;
   }

   private void xAsVoid(String var1) {
      ClientLog.XAsVoid("Aura", var1);
   }

   public static void bAsVoid(ClientPlayerEntity var0, String var1) {
      Aura var2 = A;
      if (var2 != null && var2.isRuntimeActive() && var2.K != null && var0 != null) {
         if (var1.equals("jump-before")) {
            if (var2.B <= 0) {
               return;
            }

            var2.B--;
            var2.y = var0.age + 16;
         }

         if (var0.age <= var2.y) {
            PlayerInput var3 = var0.input.playerInput;
            Vec2f var4 = var0.input.getMovementInput();
            Vec3d var5 = var0.getVelocity();
            RotationAngles var6 = xAsL6(var0);
            GameOptions var7 = getMc().options;
            PlayerInput var8 = new PlayerInput(
               var7.forwardKey.isPressed(),
               var7.backKey.isPressed(),
               var7.leftKey.isPressed(),
               var7.rightKey.isPressed(),
               var7.jumpKey.isPressed(),
               var7.sneakKey.isPressed(),
               var7.sprintKey.isPressed()
            );
            ClientLog.GAsVoid(
               "AuraMovement",
               String.format(
                  Locale.ROOT,
                  "%s tick=%d mode=%s/%s/%s reference=%s target=%d ground=%s sprint=%s sprintReportMatches=%s reset=%s yaw=%.4f sentYaw=%.4f keys=%s physical=%s input=(%.5f,%.5f) pos=(%.6f,%.6f,%.6f) velocity=(%.6f,%.6f,%.6f) fall=%.5f orbit=%s",
                  var1,
                  var0.age,
                  var2.rotation.getValue(),
                  var2.moveFix.getValue(),
                  var2.sprintMode.getValue(),
                  var2.movementReference.getValue(),
                  var2.K.getId(),
                  var0.isOnGround(),
                  var0.isSprinting(),
                  FallDamage.mAsBoolean(var0.isSprinting()),
                  Y,
                  var0.getYaw(),
                  var6.n,
                  var3,
                  var8,
                  var4.x,
                  var4.y,
                  var0.getX(),
                  var0.getY(),
                  var0.getZ(),
                  var5.x,
                  var5.y,
                  var5.z,
                  var0.fallDistance,
                  TargetStrafe.yAsBoolean()
               )
            );
         }
      }
   }

   public static PlayerInput DAsPlayerInput(PlayerInput var0) {
      return ParticleKind.cAsPlayerInput(var0);
   }
}
