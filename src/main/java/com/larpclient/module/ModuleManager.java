package com.larpclient.module;

import com.larp.obf.AuthorizationChangedException;
import com.larp.obf.RuntimeAdmission;
import com.larpclient.AirPlace;
import com.larpclient.AuthorizationState;
import com.larpclient.AutoFarm;
import com.larpclient.AutoTunnel;
import com.larpclient.ClientLog;
import com.larpclient.CryptoPets;
import com.larpclient.CrystalChams;
import com.larpclient.GuiCategory;
import com.larpclient.Pets;
import com.larpclient.RealisticWater;
import com.larpclient.SnowGround;
import com.larpclient.WalkStyle;
import com.larpclient.WavingFoliage;
import com.larpclient.WetGround;
import com.larpclient.ModuleImportBroker;
import com.larpclient.ModuleDetector;
import com.larpclient.OutlineEffect;
import com.larpclient.ParticleEmitter;
import com.larpclient.ParticleKind;
import com.larpclient.QueueSort;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.MinecraftClient;

public class ModuleManager {
   private final List<Module> c = new ArrayList<>();
   private final qs n = new qs();

   public ModuleManager() {
   }

   public void aAsVoid() {
      this.HAsVoid(true);
   }

   public void HAsVoid(boolean var1) {
      this.hAsVoid(new Aura());
      this.hAsVoid(new AntiKnockback());
      this.hAsVoid(new Backtrack());
      this.hAsVoid(new Criticals());
      this.hAsVoid(new AutoTotem());
      this.hAsVoid(new HoverTotem());
      this.hAsVoid(new Mace());
      this.hAsVoid(new LungeSwap());
      this.hAsVoid(new TotemHit());
      this.hAsVoid(new ShieldBreaker());
      this.hAsVoid(new RodSwap());
      this.hAsVoid(new Reach());
      this.hAsVoid(new TriggerBot());
      this.hAsVoid(new AutoClicker());
      this.hAsVoid(new AutoCrystal());
      this.hAsVoid(new CrystalOptimizer());
      this.hAsVoid(new AnchorTap());
      this.hAsVoid(new AutoAnchor());
      this.hAsVoid(new AutoCart());
      this.hAsVoid(new AntiTrap());
      this.hAsVoid(new AutoWeb());
      this.hAsVoid(new AutoTrap());
      this.hAsVoid(new IgnoreEntity());
      this.hAsVoid(new AimAssist());
      this.hAsVoid(new WTap());
      this.hAsVoid(new Fly());
      this.hAsVoid(new Speed());
      this.hAsVoid(new GrimSpeed());
      this.hAsVoid(new TargetStrafe());
      this.hAsVoid(new Scaffold());
      this.hAsVoid(new Jesus());
      this.hAsVoid(new NoFall());
      this.hAsVoid(new NoSlowdown());
      this.hAsVoid(new NoJumpDelay());
      this.hAsVoid(new AutoSprint());
      this.hAsVoid(new ElytraFly());
      this.hAsVoid(new EntitySpeed());
      this.hAsVoid(new BoatFly());
      this.hAsVoid(new AirStuck());
      this.hAsVoid(new ElytraMotion());
      this.hAsVoid(new ElytraHelper());
      this.hAsVoid(new AutoPearlCatch());
      this.hAsVoid(new WindChargeHop());
      this.hAsVoid(new AutoMLG());
      this.hAsVoid(new Fullbright());
      this.hAsVoid(new Zoom());
      this.hAsVoid(new ESP());
      this.hAsVoid(new XRay());
      this.hAsVoid(new NoBlockRotation());
      this.hAsVoid(new RealisticWater());
      this.hAsVoid(new WavingFoliage());
      this.hAsVoid(new WetGround());
      this.hAsVoid(new SnowGround());
      this.hAsVoid(new Tracers());
      this.hAsVoid(new Nametags());
      this.hAsVoid(new NoWeather());
      this.hAsVoid(new Chams());
      this.hAsVoid(new HandChams());
      this.hAsVoid(new CrystalChams());
      this.hAsVoid(new WalkStyle());
      this.hAsVoid(new Emotes());
      this.hAsVoid(new Pets());
      this.hAsVoid(new CryptoPets());
      this.hAsVoid(new ItemESP());
      this.hAsVoid(new TimeChanger());
      this.hAsVoid(new Animations());
      this.hAsVoid(new ViewModel());
      this.hAsVoid(new Removals());
      this.hAsVoid(new GUIAnimation());
      this.hAsVoid(new Module3DUI());
      this.hAsVoid(new WorldMenu());
      this.hAsVoid(new LineGlyphs());
      this.hAsVoid(new PearlParticles());
      this.hAsVoid(new SkyShader());
      this.hAsVoid(new SwordModels());
      this.hAsVoid(new StorageESP());
      this.hAsVoid(new PlayerTrail());
      this.hAsVoid(new HitFX());
      this.hAsVoid(new KillFX());
      this.hAsVoid(new Halloween());
      this.hAsVoid(new ItemPhysic());
      this.hAsVoid(new DamageMarkers());
      this.hAsVoid(new WorldLyrics());
      this.hAsVoid(new IslandLyrics());
      this.hAsVoid(new EchoHand());
      this.hAsVoid(new FlameHands());
      this.hAsVoid(new HitSounds());
      this.hAsVoid(new PlayerModel());
      this.hAsVoid(new SkinChanger());
      this.hAsVoid(new Capes());
      this.hAsVoid(new JumpFX());
      this.hAsVoid(new BlockOverlay());
      this.hAsVoid(new TargetAura());
      this.hAsVoid(new SelfVisuals());
      this.hAsVoid(new WorldColor());
      this.hAsVoid(new MotionBlur());
      this.hAsVoid(new AmbientParticles());
      this.hAsVoid(new AstralWeaponTrails());
      this.hAsVoid(new CelestialWeather());
      this.hAsVoid(new TargetHUD());
      this.hAsVoid(new StaffList());
      this.hAsVoid(new CustomCrosshair());
      this.hAsVoid(new ClickPulse());
      this.hAsVoid(new Freelook());
      this.hAsVoid(new HUD());
      this.hAsVoid(new CustomSky());
      this.hAsVoid(new StretchedRes());
      this.hAsVoid(new ChinaHat());
      this.hAsVoid(new Wings());
      this.hAsVoid(new HeadlessKorblox());
      this.hAsVoid(new LARP());
      this.hAsVoid(new NameChanger());
      if (var1) {
         this.EAsModule(HUD.class).setEnabled(true);
      }

      this.hAsVoid(new Performance());
      this.hAsVoid(new AutoTool());
      this.hAsVoid(new PrimeChunkFinder());
      this.hAsVoid(new Freecam());
      this.hAsVoid(new SmoothCamera());
      this.hAsVoid(new FakeLag());
      this.hAsVoid(new AutoArmor());
      this.hAsVoid(new AutoEat());
      this.hAsVoid(new ChestStealer());
      this.hAsVoid(new AutoRespawn());
      this.hAsVoid(new AutoDisconnect());
      this.hAsVoid(new InvCleaner());
      this.hAsVoid(new MCFriend());
      this.hAsVoid(new AutoGap());
      this.hAsVoid(new NoBreakDelay());
      this.hAsVoid(new FastUse());
      this.hAsVoid(new AutoDrain());
      this.hAsVoid(new SusChunkFinder());
      this.hAsVoid(new SpawnerFinder());
      this.hAsVoid(new BaseFinder());
      this.hAsVoid(new AutoSell());
      this.hAsVoid(new HomeMacro());
      this.hAsVoid(new LightDebug());
      this.hAsVoid(new MotionScanner());
      this.hAsVoid(new HolesESP());
      this.hAsVoid(new AirPlace());
      this.hAsVoid(new AutoFarm());
      this.hAsVoid(new AutoTunnel());
      this.hAsVoid(new FakePay());
      this.hAsVoid(new FakeSpawner());
      this.hAsVoid(new SpinBot());
      if (var1) {
         this.EAsModule(FakeSpawner.class).pAsVoid();
      }
   }

   private void hAsVoid(Module var1) {
      if (GuiCategory.SAsAk(var1) == GuiCategory.COMBAT) {
         var1.offerHoldKeybind();
      }

      this.c.add(var1);
   }

   public void OAsVoid() {
      MinecraftClient var1 = MinecraftClient.getInstance();
      if (!AuthorizationState.ZAsBoolean() || !RuntimeAdmission.m1()) {
         for (Module var3 : this.c) {
            var3.suspendForDisconnect();
         }

         o9.yAsVoid();
         p5.gAsVoid();
         dq.bAsVoid();
         b_.AAsVoid();
         pk.kAsVoid();
      }

      if (this.n.qAsBoolean(var1.player, var1.world, var1.player != null && var1.player.isAlive())) {
         for (Module var5 : this.c) {
            var5.suspendForDisconnect();
         }

         FakeLag.AAsVoid();
         o9.yAsVoid();
         p5.gAsVoid();
         dq.bAsVoid();
         b_.AAsVoid();
         pk.kAsVoid();
      }
   }

   public void rAsVoid() {
      this.OAsVoid();
      dq.bAsVoid();
      b_.KAsVoid();

      for (Module var2 : this.c) {
         var2.activatePending();
      }

      for (Module.Type_s var4 : Module.Type_s.values()) {
         for (Module var6 : this.c) {
            if (var6.isRuntimeActive() && var6.getTickPhase() == var4) {
               try {
                  var6.onTick();
               } catch (AuthorizationChangedException var8) {
                  var6.suspendForDisconnect();
               } catch (RuntimeException var9) {
                  ClientLog.lAsVoid("Module", "Tick failed; disabling " + var6.getName(), var9);
                  var6.disable();
               }
            }
         }

         if (var4 == Module.Type_s.INTENT) {
            dq.aAsVoid();
         }
      }
   }

   public List<Module> NAsList() {
      return this.c;
   }

   public List<Module> ZAsList(Category var1) {
      ArrayList var2 = new ArrayList();

      for (Module var4 : this.c) {
         if (var4.getCategory() == var1) {
            var2.add(var4);
         }
      }

      return var2;
   }

   public List<Module> FAsList() {
      ArrayList var1 = new ArrayList();

      for (Module var3 : this.c) {
         if (var3.isEnabled()) {
            var1.add(var3);
         }
      }

      return var1;
   }

   public Module TAsModule(String var1) {
      for (Module var3 : this.c) {
         if (var3.getName().equalsIgnoreCase(var1)) {
            return var3;
         }
      }

      return null;
   }

   public <T extends Module> T EAsModule(Class<T> var1) {
      for (Module var3 : this.c) {
         if (var1.isInstance(var3)) {
            return (T)var3;
         }
      }

      return null;
   }
}
