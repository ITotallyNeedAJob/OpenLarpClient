package com.larpclient;

import com.larpclient.module.AmbientParticles;
import com.larpclient.module.Animations;
import com.larpclient.module.AstralWeaponTrails;
import com.larpclient.module.Aura;
import com.larpclient.module.AutoAnchor;
import com.larpclient.module.AutoArmor;
import com.larpclient.module.AutoCart;
import com.larpclient.module.AutoClicker;
import com.larpclient.module.AutoCrystal;
import com.larpclient.module.AutoPearlCatch;
import com.larpclient.module.AutoSell;
import com.larpclient.module.AutoTrap;
import com.larpclient.module.BaseFinder;
import com.larpclient.module.BlockOverlay;
import com.larpclient.module.CelestialWeather;
import com.larpclient.module.Chams;
import com.larpclient.module.ChinaHat;
import com.larpclient.module.CustomCrosshair;
import com.larpclient.module.CustomSky;
import com.larpclient.module.DamageMarkers;
import com.larpclient.module.ESP;
import com.larpclient.module.EchoHand;
import com.larpclient.module.FakeSpawner;
import com.larpclient.module.FlameHands;
import com.larpclient.module.GUIAnimation;
import com.larpclient.module.HUD;
import com.larpclient.module.Halloween;
import com.larpclient.module.HandChams;
import com.larpclient.module.HeadlessKorblox;
import com.larpclient.module.HitFX;
import com.larpclient.module.HitSounds;
import com.larpclient.module.HolesESP;
import com.larpclient.module.HomeMacro;
import com.larpclient.module.ItemESP;
import com.larpclient.module.JumpFX;
import com.larpclient.module.KillFX;
import com.larpclient.module.LightDebug;
import com.larpclient.module.Module;
import com.larpclient.module.MotionBlur;
import com.larpclient.module.MotionScanner;
import com.larpclient.module.Nametags;
import com.larpclient.module.PearlParticles;
import com.larpclient.module.Performance;
import com.larpclient.module.PlayerModel;
import com.larpclient.module.PlayerTrail;
import com.larpclient.module.Removals;
import com.larpclient.module.RodSwap;
import com.larpclient.module.Scaffold;
import com.larpclient.module.SelfVisuals;
import com.larpclient.module.Setting;
import com.larpclient.module.SkyShader;
import com.larpclient.module.StorageESP;
import com.larpclient.module.SwordModels;
import com.larpclient.module.TargetAura;
import com.larpclient.module.Tracers;
import com.larpclient.module.ViewModel;
import com.larpclient.module.Wings;
import com.larpclient.module.WorldColor;
import com.larpclient.module.XRay;
import com.larpclient.module.Zoom;
import com.larpclient.module.sf;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

public final class ControlSettings {
   public ControlSettings() {
   }

   public static List<ControlSettings.Type_h> FAsList(Module var0) {
      if (var0 instanceof nk var6) {
         ArrayList var7 = new ArrayList();
         var7.add(new ControlSettings.Type_h("Sky", List.of(var6.style())));
         if (var6.IAsModule() instanceof CustomSky var75) {
            var7.addAll(rAsList(var75));
         } else if (var6.IAsModule() instanceof SkyShader var78) {
            var7.addAll(vAsList(var78));
         }

         var7.add(ControlSettings.Type_h.advanced("Existing keybinds", var6.CAsList()));
         return List.copyOf(var7);
      } else {
         ArrayList var1 = new ArrayList();
         Set var2 = Collections.newSetFromMap(new IdentityHashMap());
         if (var0.keybindMode() != null) {
            var2.add(var0.keybindMode());
         }

         if (var0 instanceof AutoClicker var3) {
            var1.add(new ControlSettings.Type_h("Click controls", var3.nAsList()));
            var1.add(new ControlSettings.Type_h("Timing", var3.LAsList()));
            var1.add(new ControlSettings.Type_h("Targets and mining", var3.FAsList()));
         }

         if (var0 instanceof RodSwap var8) {
            var1.add(new ControlSettings.Type_h("Rod controls", var8.qAsList()));
            var1.add(new ControlSettings.Type_h("Range and timing", var8.jAsList()));
         }

         if (var0 instanceof Halloween var9) {
            var1.add(new ControlSettings.Type_h("After Dark", var9.MAsList()));
            var1.add(ControlSettings.Type_h.advanced("Customize Effects", var9.VAsList()));
         }

         if (var0 instanceof Zoom var10) {
            var1.add(new ControlSettings.Type_h("Zoom controls", var10.jAsList()));
            var1.add(new ControlSettings.Type_h("View", var10.FAsList()));
         }

         if (var0 instanceof Removals var11) {
            var1.add(new ControlSettings.Type_h("Effects", var11.QAsList(), true));
            var1.add(new ControlSettings.Type_h("Sounds", var11.dAsList(), true));
         }

         if (var0 instanceof AutoCrystal var12) {
            var1.add(new ControlSettings.Type_h("Actions", var12.HAsList()));
            var1.add(new ControlSettings.Type_h("Obsidian bases", var12.NAsList()));
            var1.add(new ControlSettings.Type_h("Targets", var12.zAsList()));
            var1.add(new ControlSettings.Type_h("Aim", var12.kAsList()));
            var1.add(new ControlSettings.Type_h("Damage and pauses", var12.VAsList()));
         }

         if (var0 instanceof AutoAnchor var13) {
            var1.add(new ControlSettings.Type_h("Actions", var13.CAsList()));
            var1.add(new ControlSettings.Type_h("Targets", var13.sAsList()));
            var1.add(new ControlSettings.Type_h("Aim", var13.MAsList()));
            var1.add(new ControlSettings.Type_h("Damage and pauses", var13.wAsList()));
         }

         if (var0 instanceof AutoCart var14) {
            var1.add(new ControlSettings.Type_h("Aim", var14.QAsList()));
            var1.add(new ControlSettings.Type_h("Shot", var14.YAsList()));
            var1.add(new ControlSettings.Type_h("Targets", var14.dAsList()));
            var1.add(new ControlSettings.Type_h("Damage", var14.qAsList()));
         }

         if (var0 instanceof AutoPearlCatch var15) {
            var1.add(new ControlSettings.Type_h("Catch", var15.LAsList()));
            var1.add(new ControlSettings.Type_h("Aim", var15.zAsList()));
         }

         if (var0 instanceof Performance var16) {
            var1.add(new ControlSettings.Type_h("Render quality", var16.CAsList()));
            var1.add(new ControlSettings.Type_h("Chunks and frame rate", var16.RAsList()));
         }

         if (var0 instanceof AutoArmor var17) {
            var1.add(new ControlSettings.Type_h("Armor materials", var17.mAsList()));
         }

         if (var0 instanceof Aura var18) {
            var1.add(new ControlSettings.Type_h("Rotation", var18.cAsList()));
            var1.add(new ControlSettings.Type_h("Attack", var18.zAsList()));
            var1.add(new ControlSettings.Type_h("Targets", var18.GAsList(), true));
            var1.add(new ControlSettings.Type_h("Selection", var18.MAsList()));
            var1.add(new ControlSettings.Type_h("Utils", var18.PAsList(), true));
            var1.add(new ControlSettings.Type_h("No Attack When", var18.mAsList(), true));
            var1.add(new ControlSettings.Type_h("Indicators", var18.ZAsList()));
         }

         if (var0 instanceof HitSounds var19) {
            var1.add(new ControlSettings.Type_h("Custom sound", var19.fAsList()));
            var1.add(new ControlSettings.Type_h("Behavior", var19.GAsList()));
         }

         if (var0 instanceof HitFX var20) {
            var1.add(new ControlSettings.Type_h("Impact style", var20.AAsList()));
            var1.add(new ControlSettings.Type_h("Hit rings", var20.RAsList()));
            var1.add(new ControlSettings.Type_h("Colors", var20.tAsList()));
            var1.add(ControlSettings.Type_h.advanced("Advanced", var20.eAsList()));
         }

         if (var0 instanceof KillFX var21) {
            var1.add(new ControlSettings.Type_h("Finisher", var21.lAsList()));
            var1.add(new ControlSettings.Type_h("Feedback", var21.XAsList()));
            var1.add(ControlSettings.Type_h.advanced("Distance", var21.cAsList()));
         }

         if (var0 instanceof CustomSky var22) {
            var1.add(new ControlSettings.Type_h("Sky", List.of(var22.skyStyle())));
            var1.addAll(rAsList(var22));
         }

         if (var0 instanceof SkyShader var23) {
            var1.add(new ControlSettings.Type_h("Sky", List.of(var23.mode())));
            var1.addAll(vAsList(var23));
         }

         if (var0 instanceof WorldColor var24) {
            var1.add(new ControlSettings.Type_h("World ambience", List.of(var24.ambientStrength, var24.ambientColor)));
            var1.add(new ControlSettings.Type_h("Color grading", List.of(var24.saturation, var24.exposure, var24.tint)));
            var1.add(
               ControlSettings.Type_h.advanced("Fine grading", List.of(var24.contrast, var24.gamma, var24.shadowTint, var24.highlightTint, var24.vignette))
            );
         }

         if (var0 instanceof MotionBlur var25) {
            var1.add(new ControlSettings.Type_h("Motion blur", List.of(var25.mode, var25.strength)));
            var1.add(ControlSettings.Type_h.advanced("Streaks", List.of(var25.quality, var25.maxLength)));
         }

         if (var0 instanceof AmbientParticles var26) {
            var1.add(new ControlSettings.Type_h("World particles", List.of(var26.style, var26.H, var26.x, var26.h, var26.W, var26.v)));
            var1.add(new ControlSettings.Type_h("Floating motion", List.of(var26.G, var26.E, var26.N, var26.z)));
            var1.add(new ControlSettings.Type_h("Flying comets", List.of(var26.Q, var26.O, var26.T)));
            var1.add(new ControlSettings.Type_h("Appearance", List.of(var26.themeColors, var26.color, var26.tailColor, var26.J, var26.bloom)));
            var1.add(ControlSettings.Type_h.advanced("Particle limit", List.of(var26.C)));
         }

         if (var0 instanceof Wings var27) {
            var1.add(new ControlSettings.Type_h("Wearers", var27.tAsList(), true));
            var1.add(new ControlSettings.Type_h("Appearance", var27.XAsList()));
         }

         if (var0 instanceof HeadlessKorblox var28) {
            var1.add(new ControlSettings.Type_h("Wearers", List.of(var28.self, var28.otherPlayers), true));
            var1.add(new ControlSettings.Type_h("Headless head", List.of(var28.headless, var28.hideHelmets)));
            var1.add(new ControlSettings.Type_h("Korblox leg", List.of(var28.korblox, var28.leg, var28.length, var28.thickness)));
            var1.add(new ControlSettings.Type_h("Spike colors", List.of(var28.hipColor, var28.tipColor, var28.tipGlow)));
         }

         if (var0 instanceof ChinaHat var29) {
            var1.add(new ControlSettings.Type_h("Wearers", var29.aAsList(), true));
            var1.add(new ControlSettings.Type_h("Appearance", var29.FAsList()));
            var1.add(ControlSettings.Type_h.advanced("Shape", var29.QAsList()));
         }

         if (var0 instanceof PlayerTrail var30) {
            var1.add(new ControlSettings.Type_h("Trail", var30.qAsList()));
            var1.add(new ControlSettings.Type_h("Appearance", var30.fAsList()));
            var1.add(new ControlSettings.Type_h("Motion", var30.jAsList()));
            var1.add(ControlSettings.Type_h.advanced("History and limits", var30.hAsList()));
         }

         if (var0 instanceof AstralWeaponTrails var31) {
            var1.add(new ControlSettings.Type_h("Effect", var31.rAsList()));
            var1.add(new ControlSettings.Type_h("Appearance", var31.JAsList()));
            var1.add(ControlSettings.Type_h.advanced("Advanced", var31.CAsList()));
         }

         if (var0 instanceof CelestialWeather var32) {
            var1.add(new ControlSettings.Type_h("Effect", var32.XAsList()));
            var1.add(new ControlSettings.Type_h("Appearance", var32.BAsList()));
            var1.add(ControlSettings.Type_h.advanced("Advanced", var32.NAsList()));
         }

         if (var0 instanceof SelfVisuals var33) {
            var1.add(new ControlSettings.Type_h("Body effect", var33.CAsList()));
            var1.add(new ControlSettings.Type_h("Appearance", var33.LAsList()));
            var1.add(new ControlSettings.Type_h("Motion", var33.ZAsList()));
            var1.add(ControlSettings.Type_h.advanced("Refraction", var33.aAsList()));
         }

         if (var0 instanceof JumpFX var34) {
            var1.add(new ControlSettings.Type_h("Jump effect", var34.gAsList()));
            var1.add(new ControlSettings.Type_h("Shape and timing", var34.RAsList()));
            var1.add(new ControlSettings.Type_h("Appearance", var34.zAsList()));
         }

         if (var0 instanceof TargetAura var35) {
            var1.add(new ControlSettings.Type_h("Target effect", var35.fAsList()));
            var1.add(new ControlSettings.Type_h("Colors", var35.aAsList()));
         }

         if (var0 instanceof CustomCrosshair var36) {
            var1.add(new ControlSettings.Type_h("Shape", var36.yAsList()));
            var1.add(new ControlSettings.Type_h("Appearance", var36.CAsList()));
         }

         if (var0 instanceof EchoHand var37) {
            var1.add(new ControlSettings.Type_h("Hand effect", var37.QAsList()));
            var1.add(new ControlSettings.Type_h("Appearance", var37.pAsList()));
            var1.add(ControlSettings.Type_h.advanced("History and motion", var37.RAsList()));
         }

         if (var0 instanceof FlameHands var38) {
            var1.add(new ControlSettings.Type_h("Hand flames", var38.MAsList()));
            var1.add(new ControlSettings.Type_h("Colors", var38.iAsList()));
            var1.add(new ControlSettings.Type_h("Motion", var38.UAsList()));
         }

         if (var0 instanceof PearlParticles var39) {
            var1.add(new ControlSettings.Type_h("Pearl trail", var39.lAsList()));
            var1.add(new ControlSettings.Type_h("Colors", var39.PAsList()));
            var1.add(ControlSettings.Type_h.advanced("Drift and fade", var39.vAsList()));
         }

         if (var0 instanceof DamageMarkers var40) {
            var1.add(new ControlSettings.Type_h("Damage sources", var40.rAsList(), true));
            var1.add(new ControlSettings.Type_h("Labels", var40.dAsList()));
         }

         if (var0 instanceof PlayerModel var41) {
            var1.add(new ControlSettings.Type_h("Character", var41.eAsList()));
            var1.add(ControlSettings.Type_h.advanced("Diagnostics", var41.QAsList()));
         }

         if (var0 instanceof GUIAnimation var42) {
            var1.add(new ControlSettings.Type_h("Interface", var42.vAsList(), true));
            var1.add(new ControlSettings.Type_h("Camera", var42.XAsList(), true));
            var1.add(new ControlSettings.Type_h("World and timing", var42.qAsList()));
         }

         if (var0 instanceof Chams var43) {
            var1.add(new ControlSettings.Type_h("Entities", var43.tAsList(), true));
            var1.add(new ControlSettings.Type_h("Colors", var43.cAsList()));
            var1.add(new ControlSettings.Type_h("Outline and fill", var43.h.xAsList()));
            var1.add(new ControlSettings.Type_h("Glow", var43.h.sAsList()));
            var1.add(new ControlSettings.Type_h("Gradient motion", var43.h.bAsList()));
            var1.add(new ControlSettings.Type_h("Occlusion", var43.OAsList()));
            var1.add(ControlSettings.Type_h.advanced("Distance and limits", var43.kAsList()));
         }

         if (var0 instanceof HandChams var44) {
            var1.add(new ControlSettings.Type_h("Colors", var44.t.PAsList()));
            var1.add(new ControlSettings.Type_h("Outline and fill", var44.t.xAsList()));
            var1.add(new ControlSettings.Type_h("Glow", var44.t.sAsList()));
            var1.add(new ControlSettings.Type_h("Gradient motion", var44.t.bAsList()));
         }

         if (var0 instanceof ESP var45) {
            var1.add(new ControlSettings.Type_h("Entities", var45.OAsList(), true));
            var1.add(new ControlSettings.Type_h("Highlight", var45.EAsList()));
            var1.add(new ControlSettings.Type_h("Colors", var45.rAsList()));
            var1.add(new ControlSettings.Type_h("Stroke", var45.wAsList()));
            var1.add(new ControlSettings.Type_h("Health", var45.IAsList()));
            var1.add(ControlSettings.Type_h.advanced("Limits and alerts", var45.dAsList()));
         }

         if (var0 instanceof ItemESP var46) {
            var1.add(new ControlSettings.Type_h("Drops", var46.GAsList()));
            var1.add(new ControlSettings.Type_h("Appearance", var46.rAsList()));
            var1.add(ControlSettings.Type_h.advanced("Item limit", var46.CAsList()));
         }

         if (var0 instanceof Tracers var47) {
            var1.add(new ControlSettings.Type_h("Entities", var47.oAsList(), true));
            var1.add(new ControlSettings.Type_h("Lines", var47.JAsList()));
            var1.add(ControlSettings.Type_h.advanced("Entity limit", var47.yAsList()));
         }

         if (var0 instanceof Nametags var48) {
            var1.add(new ControlSettings.Type_h("Entities", var48.KAsList(), true));
            var1.add(new ControlSettings.Type_h("Label content", var48.sAsList(), true));
            var1.add(new ControlSettings.Type_h("Placement", var48.EAsList()));
         }

         if (var0 instanceof XRay var49) {
            var1.add(new ControlSettings.Type_h("Ore view", var49.OAsList()));
            var1.add(new ControlSettings.Type_h("Show", var49.yAsList(), true));
            var1.add(new ControlSettings.Type_h("Markers", var49.YAsList()));
            var1.add(ControlSettings.Type_h.advanced("Marker limit", var49.zAsList()));
         }

         if (var0 instanceof CrystalChams var50) {
            var1.add(new ControlSettings.Type_h("Crystal", var50.HAsList()));
            var1.add(new ControlSettings.Type_h("Appearance", var50.KAsList()));
            var1.add(new ControlSettings.Type_h("Motion", var50.SAsList()));
         }

         if (var0 instanceof Pets var51) {
            var1.add(new ControlSettings.Type_h("Companion", var51.lAsList()));
            var1.add(new ControlSettings.Type_h("Follow", var51.WAsList()));
            var1.add(new ControlSettings.Type_h("Finish", var51.FAsList()));
            var1.add(new ControlSettings.Type_h("Glow", var51.LAsList()));
         }

         if (var0 instanceof CryptoPets var52) {
            var1.add(new ControlSettings.Type_h("Coins", var52.mAsList(), true));
            var1.add(new ControlSettings.Type_h("Ring", var52.DAsList()));
         }

         if (var0 instanceof RealisticWater var53) {
            var1.add(new ControlSettings.Type_h("Surface", List.of(var53.intensity, var53.waves, var53.worldReflections, var53.foam, var53.caustics)));
            var1.add(new ControlSettings.Type_h("Water view", List.of(var53.underwater, var53.volumetric)));
         }

         if (var0 instanceof WavingFoliage var54) {
            var1.add(new ControlSettings.Type_h("Foliage", List.of(var54.leaves, var54.trees, var54.plants), true));
            var1.add(new ControlSettings.Type_h("Wind", List.of(var54.strength, var54.speed, var54.gusts)));
         }

         if (var0 instanceof WetGround var55) {
            var1.add(new ControlSettings.Type_h("Puddles", List.of(var55.style, var55.puddles, var55.wetness, var55.shine, var55.worldReflections)));
            var1.add(new ControlSettings.Type_h("Rain", List.of(var55.raindrops, var55.wetterInRain), true));
         }

         if (var0 instanceof SnowGround var56) {
            var1.add(new ControlSettings.Type_h("Ground", List.of(var56.I, var56.j, var56.G, var56.quality)));
            var1.add(new ControlSettings.Type_h("Snowfall", List.of(var56.snowfall, var56.R, var56.S, var56.o)));
            var1.add(new ControlSettings.Type_h("Footsteps", List.of(var56.footsteps, var56.Y)));
         }

         if (var0 instanceof BlockOverlay var57) {
            var1.add(new ControlSettings.Type_h("Appearance", var57.MAsList()));
            var1.add(new ControlSettings.Type_h("Animation", var57.vAsList()));
         }

         if (var0 instanceof sf var58) {
            var1.add(new ControlSettings.Type_h("Detection", var58.detectionSettings()));
            if (var58 instanceof BaseFinder var4) {
               var1.add(new ControlSettings.Type_h("Diagnostics", var4.WAsList()));
            }

            var1.add(new ControlSettings.Type_h("Scanning", var58.AAsList()));
            var1.add(new ControlSettings.Type_h("Markers", var58.markerSettings()));
            var1.add(new ControlSettings.Type_h("Alerts", var58.tAsList()));
         }

         if (var0 instanceof AutoSell var59) {
            var1.add(new ControlSettings.Type_h("Items", var59.TAsList()));
            var1.add(new ControlSettings.Type_h("Sell menu", var59.QAsList()));
            var1.add(new ControlSettings.Type_h("Timing", var59.nAsList()));
            var1.add(new ControlSettings.Type_h("Feedback", var59.OAsList()));
         }

         if (var0 instanceof MotionScanner var60) {
            var1.add(new ControlSettings.Type_h("Detection", var60.LAsList()));
            var1.add(new ControlSettings.Type_h("Alerts", var60.fAsList()));
            var1.add(new ControlSettings.Type_h("Appearance", var60.SAsList()));
         }

         if (var0 instanceof HomeMacro var61) {
            var1.add(new ControlSettings.Type_h("Home replacement", var61.zAsList()));
         }

         if (var0 instanceof LightDebug var62) {
            var1.add(new ControlSettings.Type_h("Scanning", var62.pAsList()));
            var1.add(new ControlSettings.Type_h("Appearance", var62.NAsList()));
         }

         if (var0 instanceof HolesESP var63) {
            var1.add(new ControlSettings.Type_h("Passage colors", var63.BAsList()));
            var1.add(new ControlSettings.Type_h("Rendering", var63.gAsList()));
         }

         if (var0 instanceof StorageESP var64) {
            var1.add(new ControlSettings.Type_h("Storage types", var64.WAsList(), true));
            var1.add(new ControlSettings.Type_h("Highlights", var64.DAsList()));
            var1.add(new ControlSettings.Type_h("Appearance", var64.AAsList()));
            var1.add(ControlSettings.Type_h.advanced("Distance and limits", var64.PAsList()));
         }

         if (var0 instanceof HUD var74) {
            return List.of(new ControlSettings.Type_h("Widgets", var74.FAsList(), true), new ControlSettings.Type_h("Panels", var74.qAsList()));
         } else {
            if (var0 instanceof Animations var65) {
               var1.add(new ControlSettings.Type_h("Swing", var65.JAsList()));
               var1.add(ControlSettings.Type_h.advanced("Fine tuning", var65.SAsList()));
            }

            if (var0 instanceof ViewModel var66) {
               var1.add(new ControlSettings.Type_h("Main hand", var66.uAsList()));
               var1.add(new ControlSettings.Type_h("Offhand", var66.hAsList()));
            }

            if (var0 instanceof SwordModels var67) {
               var1.add(new ControlSettings.Type_h("Appearance", List.of(var67.model, var67.finish, var67.enchantmentGlint)));
               var1.add(new ControlSettings.Type_h("Inspect", List.of(var67.inspectKey, var67.inspectSpeed, var67.idleMotion)));
               var1.add(ControlSettings.Type_h.advanced("Hand placement", List.of(var67.modelScale, var67.handX, var67.handY, var67.handZ)));
            }

            if (var0 instanceof FakeSpawner var68) {
               var1.add(new ControlSettings.Type_h("Backing blocks", var68.RAsList()));
               var1.add(new ControlSettings.Type_h("Appearance", var68.wAsList()));
               var1.add(new ControlSettings.Type_h("Loot worth", var68.xAsList()));
            }

            if (var0 instanceof AutoTrap var69) {
               var1.add(new ControlSettings.Type_h("Trap", var69.PAsList()));
               var1.add(ControlSettings.Type_h.advanced("Advanced", var69.UAsList()));
            }

            if (var0 instanceof Scaffold var70) {
               var1.add(new ControlSettings.Type_h("Bridge", var70.sAsList()));
               var1.add(ControlSettings.Type_h.advanced("Advanced", var70.pAsList()));
            }

            if (var0 instanceof PlacementPreview.Type_e var71) {
               var1.add(
                  !(var0 instanceof AutoTrap) && !(var0 instanceof Scaffold)
                     ? new ControlSettings.Type_h("Highlight", var71.placementHighlight().BAsList())
                     : ControlSettings.Type_h.advanced("Highlight", var71.placementHighlight().BAsList())
               );
            }

            for (ControlSettings.Type_h var76 : (Iterable<ControlSettings.Type_h>) (var1)) {
               var2.addAll(var76.settings());
            }

            List var73 = var0.getSettings().stream().filter(var1x -> !var2.contains(var1x)).toList();
            if (!var73.isEmpty()) {
               String var77 = var1.isEmpty() ? "Settings" : (var0 instanceof CustomSky ? "General" : "Defaults");
               if (var0 instanceof BaseFinder || var0 instanceof StorageESP) {
                  var77 = "Appearance";
               }

               var1.add(new ControlSettings.Type_h(var77, var73));
            }

            return List.copyOf(var1);
         }
      }
   }

   private static List<ControlSettings.Type_h> rAsList(CustomSky var0) {
      return List.of(
         new ControlSettings.Type_h("Appearance", var0.EAsList()),
         new ControlSettings.Type_h("Motion", var0.yAsList()),
         new ControlSettings.Type_h("Atmosphere", var0.TAsList()),
         ControlSettings.Type_h.advanced("Detail and placement", var0.PAsList())
      );
   }

   private static List<ControlSettings.Type_h> vAsList(SkyShader var0) {
      return List.of(new ControlSettings.Type_h("Appearance", var0.tAsList()), new ControlSettings.Type_h("Motion and pattern", var0.lAsList()));
   }

   public static record Type_h(String title, List<Setting<?>> settings, boolean chips, boolean collapsed) {
      public Type_h(String var1, List<Setting<?>> var2) {
         this(var1, var2, false, false);
      }

      public Type_h(String var1, List<Setting<?>> var2, boolean var3) {
         this(var1, var2, var3, false);
      }

      public static ControlSettings.Type_h advanced(String var0, List<Setting<?>> var1) {
         return new ControlSettings.Type_h(var0, var1, false, true);
      }
   }
}
