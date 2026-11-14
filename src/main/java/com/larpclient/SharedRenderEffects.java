package com.larpclient;

import com.larpclient.module.AmbientParticles;
import com.larpclient.module.AttackRaycast;
import com.larpclient.module.BlockOverlay;
import com.larpclient.module.Chams;
import com.larpclient.module.ChinaHat;
import com.larpclient.module.DamageMarkers;
import com.larpclient.module.HitFX;
import com.larpclient.module.JumpFX;
import com.larpclient.module.KillFX;
import com.larpclient.module.LineGlyphs;
import com.larpclient.module.PearlParticles;
import com.larpclient.module.PlayerModel;
import com.larpclient.module.SelfVisuals;
import com.larpclient.module.SkyShader;
import com.larpclient.module.TargetAura;
import com.larpclient.module.Wings;
import com.larpclient.module.WorldLyrics;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStopping;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.StartTick;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldExtractionContext;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.AfterBlockOutlineExtraction;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.BeforeBlockOutline;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.render.state.WorldRenderState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;

public final class SharedRenderEffects {
   public static final ParticleEmitterEngine B = new ParticleEmitterEngine();
   public static final RibbonTrailEngine p = new RibbonTrailEngine();
   public static final BodyTrailEngine Z = new BodyTrailEngine();
   public static final OfferManager H = new OfferManager();
   private static List<PlacementPreview> x = List.of();
   private static EffectDrawBuffers L;
   private static ClientWorld f;
   private static boolean q;
   private static int A = -1;
   private static LivingEntity Q;
   private static double d;
   private static WorldRenderState k;
   private static float V;
   private static EntityRenderState G;
   private static CosmeticSubmissionHandler.Type_n n;
   private static double v;
   private static double o;
   private static int K;
   private static boolean l;
   private static boolean X;
   private static Boolean s;
   private static final Set<String> R = new HashSet<>();
   private static int N = -1;

   public SharedRenderEffects() {
   }

   static void EAsVoid(String var0, Runnable var1) {
      if (N != BloomQuality.J) {
         R.clear();
         N = BloomQuality.J;
      }

      if (!R.contains(var0)) {
         try {
            var1.run();
         } catch (RuntimeException var4) {
            R.add(var0);
            RenderPerformanceStats.pAsVoid(var0);
            if (var0.equals("Chams")) {
               Chams var3 = Chams.mAsChams();
               if (var3 != null) {
                  var3.jAsVoid();
               }
            }

            if (var0.equals("CrystalChams")) {
               CrystalChams var5 = CrystalChams.hAsCrystalChams();
               if (var5 != null) {
                  var5.nAsVoid();
               }
            }

            if (var0.equals("Pets")) {
               Pets var6 = Pets.VAsPets();
               if (var6 != null) {
                  var6.DAsVoid();
               }
            }

            if (var0.equals("CryptoPets")) {
               CryptoPets var7 = CryptoPets.IAsCryptoPets();
               if (var7 != null) {
                  var7.sAsVoid();
               }
            }

            if (var0.equals("EmoteProps")) {
               EmotePropRenderer.lAsVoid();
            }

            if (var0.equals("PlayerModel")) {
               PlayerModel var8 = PlayerModel.hAsPlayerModel();
               if (var8 != null) {
                  var8.SAsVoid();
               }
            }

            if (var0.equals("SnowGround")) {
               SnowGround var9 = SnowGround.vAsSnowGround();
               if (var9 != null) {
                  var9.MAsVoid();
               }
            }

            if (var0.equals("KillFX")) {
               KillFX var10 = KillFX.aAsKillFX();
               if (var10 != null) {
                  var10.lAsVoid();
               }
            }

            if (L != null) {
               L.FAsVoid();
            }

            ClientLog.lAsVoid("Render", "Cosmetic stage=" + var0 + "; isolated until resource reload", var4);
         }
      }
   }

   static boolean OAsBoolean(String var0) {
      return !ZAsBoolean(var0);
   }

   public static boolean ZAsBoolean(String var0) {
      return N != BloomQuality.J || !R.contains(var0);
   }

   private static void bAsVoid(String var0, EffectGeometryRenderer var1, Runnable var2) {
      EAsVoid(var0, () -> {
         try {
            var2.run();
         } finally {
            var1.QAsVoid(false);
            var1.blend(EffectRenderParams.Type_g.ALPHA);
         }
      });
   }

   private static void CAsVoid(WorldExtractionContext var0) {
      WebRenderer.bAsVoid(var0.frustum());
      k = var0.worldState();
      X = false;

      for (PlacementPreview var2 : x) {
         EAsVoid(var2.tAsString(), () -> var2.AAsVoid(var0));
      }

      V = var0.tickCounter().getTickProgress(false);
      CameraRenderState var17 = k.cameraRenderState;
      EntityMaterialRenderer.jAsVoid(var17);
      RenderPerformanceStats.qAsVoid(V, var17);
      PlayerModel var18 = PlayerModel.hAsPlayerModel();
      if (var18 != null) {
         EAsVoid("PlayerModel", () -> var18.qAsVoid(V, var17.pos));
      }

      JumpFX var3 = JumpFX.JAsJumpFX();
      if (var3 != null) {
         EAsVoid("JumpFX", var3::QAsVoid);
      }

      KillFX var4 = KillFX.aAsKillFX();
      if (var4 != null) {
         EAsVoid("KillFX", () -> var4.pAsVoid(var17));
      }

      CrystalChams var5 = CrystalChams.hAsCrystalChams();
      if (var5 != null) {
         EAsVoid("CrystalChams", () -> var5.hAsVoid(V));
      }

      Pets var6 = Pets.VAsPets();
      if (var6 != null) {
         EAsVoid("Pets", () -> var6.pAsVoid(V, var17));
      }

      CryptoPets var7 = CryptoPets.IAsCryptoPets();
      if (var7 != null) {
         EAsVoid("CryptoPets", () -> var7.WAsVoid(V, var17));
      }

      EAsVoid("EmoteProps", () -> EmotePropRenderer.iAsVoid(V));
      SnowGround var8 = SnowGround.vAsSnowGround();
      if (var8 != null) {
         EAsVoid("SnowGround", () -> var8.EAsVoid(V, var17));
      }

      EAsVoid("Trails", () -> p.TAsVoid(var17.pos, V));
      EAsVoid("Body Trails", () -> Z.ZAsVoid(var17.pos, V));
      EAsVoid("GhostEcho", () -> H.KAsVoid(var17.pos));
      EAsVoid("Comets", () -> CometRenderer.AAsVoid(V));
      EAsVoid("HitZone", AttackRaycast::ZAsVoid);
      TargetAura var9 = TargetAura.mAsTargetAura();
      if (var9 != null) {
         EAsVoid("TargetAura", () -> var9.LAsVoid(V));
      }

      EAsVoid("Particles", () -> B.fAsVoid(var17.pos, V, var0.frustum()));
      AmbientParticles var10 = AmbientParticles.CAsAmbientParticles();
      if (var10 != null && var10.isOperational()) {
         EAsVoid("Ambient Particles", () -> var10.OAsB9().BAsVoid(var17.pos, V, var0.frustum()));
      }

      Chams var11 = Chams.mAsChams();
      if (var11 != null) {
         EAsVoid("Chams", () -> var11.WAsVoid(V));
      }

      ChinaHat var12 = ChinaHat.uAsChinaHat();
      if (var12 != null) {
         EAsVoid("ChinaHat", () -> var12.OAsVoid(V));
      }

      Wings var13 = Wings.tAsWings();
      if (var13 != null) {
         EAsVoid("Wings", () -> var13.JAsVoid(V));
      }

      DamageMarkers var14 = DamageMarkers.DAsDamageMarkers();
      if (var14 != null) {
         EAsVoid("DamageMarkers", () -> var14.vAsVoid(var17));
      }

      WorldLyrics var15 = WorldLyrics.TAsWorldLyrics();
      if (var15 != null) {
         EAsVoid("WorldLyrics", () -> var15.gAsVoid(var17));
      }

      G = null;
      SelfVisuals var16 = SelfVisuals.zAsSelfVisuals();
      n = null;
      l = false;
      o = 0.0;
      if (var16 != null && var16.tAsBoolean() && var16.MAsBoolean()) {
         EAsVoid("SelfVisuals", () -> {
            G = CosmeticSubmissionHandler.oAsEntityRenderState(MinecraftClient.getInstance().player, V);
            n = !var16.VAsBoolean() ? null : (var16.rAsO0_Type_n() == null ? CosmeticSubmissionHandler.Type_n.TINT : var16.rAsO0_Type_n());
            v = var16.hAsDouble();
            K = UiStyle.w;
            o = var16.YAsBoolean() && var16.enhancedRefraction.getValue() ? var16.refractionStrength.getValue() : 0.0;
            l = var16.VAsBoolean() && var16.modelBloom.getValue() && (!var16.YAsBoolean() || BloomQualityHelper.K != BloomQuality.Type_w.Off);
         });
      }
   }

   public static boolean RAsBoolean() {
      return !q;
   }

   public static boolean BAsBoolean(EntityRenderState var0) {
      return G != null && n != null && k != null && k.entityRenderStates.contains(var0);
   }

   public static void sAsVoid() {
      RenderPerformanceStats.BAsVoid(LarpClient.CAsLarpClient().mAsF().NAsList());
      x = LarpClient.CAsLarpClient()
         .mAsF()
         .NAsList()
         .stream()
         .filter(var0 -> var0 instanceof PlacementPreview.Type_e)
         .map(var0 -> ((PlacementPreview.Type_e)var0).placementHighlight())
         .toList();
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)var0 -> {
         JumpFX var1 = JumpFX.JAsJumpFX();
         if (var1 != null && !var0.isPaused()) {
            var1.FAsVoid();
         }

         if (!var0.isPaused()) {
            RenderPerformanceStats.PAsVoid(var0);
         }
      });
      ClientTickEvents.START_CLIENT_TICK.register((StartTick)var0 -> {
         FlameParticles.bAsVoid();
         if (var0.world != f) {
            RenderPerformanceStats.nAsVoid();
            EAsVoid("Scene resource cleanup", SceneRenderResources::BAsVoid);
            EAsVoid("Refraction cleanup", RefractionPostEffect::EAsVoid);
            HandEffectRenderer.KAsVoid();
            ButtonWidget.lAsVoid();
            FlameParticles.UAsVoid();
            CrystalChams var1 = CrystalChams.hAsCrystalChams();
            if (var1 != null) {
               var1.nAsVoid();
            }

            Pets var2 = Pets.VAsPets();
            if (var2 != null) {
               var2.DAsVoid();
            }

            CryptoPets var3 = CryptoPets.IAsCryptoPets();
            if (var3 != null) {
               var3.sAsVoid();
            }

            DamageMarkers var4 = DamageMarkers.DAsDamageMarkers();
            if (var4 != null) {
               var4.gAsVoid();
            }

            WorldLyrics var5 = WorldLyrics.TAsWorldLyrics();
            if (var5 != null) {
               var5.WAsVoid();
            }

            KillFX var6 = KillFX.aAsKillFX();
            if (var6 != null) {
               var6.lAsVoid();
            }

            x.forEach(PlacementPreview::rAsVoid);
            f = var0.world;
            k = null;
            G = null;
            B.JAsVoid();
            p.FAsVoid();
            Z.WAsVoid();
            H.rAsVoid();
            CometRenderer.uAsVoid();
            AmbientParticles var7 = AmbientParticles.CAsAmbientParticles();
            if (var7 != null) {
               var7.pAsVoid();
            }

            JumpFX var8 = JumpFX.JAsJumpFX();
            if (var8 != null) {
               var8.SAsVoid();
            }

            BlockOverlay var9 = BlockOverlay.jAsBlockOverlay();
            if (var9 != null) {
               var9.pAsVoid();
            }

            WorldColorPostEffect.fAsVoid();
            MotionBlurEffect.QAsVoid();
            EntityHaloEffect.aAsVoid();
            LightmapTintEffect.jAsVoid();
            SoftParticlePipeline.yAsVoid();
            CosmeticSubmissionHandler.XAsVoid();
            Chams var10 = Chams.mAsChams();
            if (var10 != null) {
               var10.jAsVoid();
            }

            PlayerModel var11 = PlayerModel.hAsPlayerModel();
            if (var11 != null) {
               var11.LAsVoid();
            }

            Q = null;
            HudRenderer.UAsVoid();
            HudRenderer.QAsVoid();
            ClientLog.XAsVoid("Render", "World changed; transient effects cleared");
         }

         if (!var0.isPaused()) {
            MediaSession.JAsVoid();
            H.xAsVoid();
            B.eAsVoid(1.0, (var1x, var2x) -> LandQuery.AAsLq_Type_l(var0.world, var1x, var2x));
            p.DAsVoid();
            Z.bAsVoid();
            KillFX var12 = KillFX.aAsKillFX();
            if (var12 != null) {
               EAsVoid("KillFX", () -> var12.kAsVoid());
            }
         }
      });
      WorldRenderEvents.AFTER_BLOCK_OUTLINE_EXTRACTION.register((AfterBlockOutlineExtraction)(var0, var1) -> {
         BlockOverlay var2 = BlockOverlay.jAsBlockOverlay();
         if (var2 != null) {
            var2.EAsVoid(var0, var1);
         }
      });
      WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register((BeforeBlockOutline)(var0, var1) -> {
         BlockOverlay var2 = BlockOverlay.jAsBlockOverlay();
         return var2 == null || !var2.BAsBoolean(var1.pos(), var0.worldState());
      });
      WorldRenderEvents.END_EXTRACTION.register(SharedRenderEffects::CAsVoid);
      WorldRenderEvents.BEFORE_TRANSLUCENT.register(SharedRenderEffects::HAsVoid);
      WorldRenderEvents.END_MAIN.register(SharedRenderEffects::dAsVoid);
      ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)var0 -> {
         RenderPerformanceStats.nAsVoid();
         EAsVoid("Scene resource cleanup", SceneRenderResources::BAsVoid);
         EAsVoid("Refraction cleanup", RefractionPostEffect::EAsVoid);
         HandEffectRenderer.KAsVoid();
         FlameParticles.UAsVoid();
         DamageMarkers var1 = DamageMarkers.DAsDamageMarkers();
         if (var1 != null) {
            var1.gAsVoid();
         }

         WorldLyrics var2 = WorldLyrics.TAsWorldLyrics();
         if (var2 != null) {
            var2.wAsVoid();
         }

         KillFX var3 = KillFX.aAsKillFX();
         if (var3 != null) {
            var3.lAsVoid();
         }

         x.forEach(PlacementPreview::rAsVoid);
         BloomQuality.rAsVoid();
         SoftParticlePipeline.yAsVoid();
         EntityHaloEffect.aAsVoid();
         LightmapTintEffect.jAsVoid();
         Chams var4 = Chams.mAsChams();
         if (var4 != null) {
            var4.jAsVoid();
         }

         H.rAsVoid();
         CometRenderer.uAsVoid();
         AmbientParticles var5 = AmbientParticles.CAsAmbientParticles();
         if (var5 != null) {
            var5.pAsVoid();
         }

         JumpFX var6 = JumpFX.JAsJumpFX();
         if (var6 != null) {
            var6.SAsVoid();
         }

         BlockOverlay var7 = BlockOverlay.jAsBlockOverlay();
         if (var7 != null) {
            var7.pAsVoid();
         }

         LineGlyphs var8 = LineGlyphs.LAsLineGlyphs();
         if (var8 != null) {
            var8.LAsVoid();
         }

         PearlParticles var9 = PearlParticles.iAsPearlParticles();
         if (var9 != null) {
            var9.XAsVoid();
         }

         SkyShader var10 = SkyShader.EAsSkyShader();
         if (var10 != null) {
            var10.YAsVoid();
         }

         SnowGround var11 = SnowGround.vAsSnowGround();
         if (var11 != null) {
            var11.wAsVoid();
         }

         MenuNebulaRenderer.yAsVoid();
         GuiAuroraRenderer.gAsVoid();
         ClickGuiTextRenderer.uAsVoid();
         ColoredGeometryPipeline.IAsVoid();
         if (L != null) {
            L.close();
         }
      });
      ClientLog.GAsVoid("Render", "Shared effects initialized; depth-tested ribbons, particle pool, bloom and blur");
   }

   public static void dAsVoid(LivingEntity var0) {
      UAsVoid(var0);
      HitFX var1 = HitFX.tAsHitFX();
      if (var1 != null && var1.isOperational()) {
         var1.dAsVoid(var0);
      }
   }

   public static void UAsVoid(LivingEntity var0) {
      Q = var0;
      d = MediaSession.dAsDouble();
   }

   public static LivingEntity HAsLivingEntity() {
      return Q != null && Q.isAlive() && Q.getEntityWorld() == MinecraftClient.getInstance().world && MediaSession.dAsDouble() - d < 3.0 ? Q : null;
   }

   private static void vAsVoid() {
      SceneRenderResources.gAsVoid();
      RefractionPostEffect.HAsVoid();
      if (A != BloomQuality.J) {
         A = BloomQuality.J;
         WorldLabelLayout.DAsVoid(WorldLabelLayout.g);
         EffectGeometryRenderer.YAsVoid();
      }
   }

   private static void HAsVoid(WorldRenderContext var0) {
      if (A != BloomQuality.J) {
         q = false;
      }

      if (!q && var0.matrices() != null && var0.worldState() == k && MinecraftClient.getInstance().world != null) {
         if (L == null) {
            L = new EffectDrawBuffers();
         }

         GpuTextureView var1 = RenderSystem.outputColorTextureOverride;
         GpuTextureView var2 = RenderSystem.outputDepthTextureOverride;
         String var3 = "shader initialization before translucent terrain";

         try {
            vAsVoid();
            X = true;
            EntityRenderState var4 = G;
            var3 = "SelfVisuals outline mask / player material before translucent terrain";
            WAsVoid(var0, var4);
            var3 = "Comets before translucent terrain";
            EffectGeometryRenderer var5 = new EffectGeometryRenderer(var0.matrices(), L, var0.worldState().cameraRenderState);
            bAsVoid("Comets", var5, () -> {
               CometRenderer.iAsVoid(var5, false);
               L.CAsVoid();
            });
            SceneRenderResources.cAsVoid();
            SceneRenderResources.YAsVoid();
            if (var4 != null && l || CometRenderer.zAsBoolean()) {
               SceneRenderResources.xAsVoid();
            }
         } catch (RuntimeException var91) {
            q = true;
            ClientLog.lAsVoid("Render", "VisualRuntime stage=" + var3 + "; output attachments restored; retry on resource reload", var91);
         } finally {
            RenderSystem.outputColorTextureOverride = var1;
            RenderSystem.outputDepthTextureOverride = var2;
         }
      }
   }

   private static void WAsVoid(WorldRenderContext var0, EntityRenderState var1) {
      if (var1 != null && !Boolean.valueOf(X).equals(s)) {
         s = X;
         if (X) {
            ClientLog.GAsVoid("SelfVisuals", "Drawing before translucent terrain; glass, water and ice blend over the player");
         } else {
            ClientLog.oAsVoid("SelfVisuals", "Hook before translucent terrain did not run; drawing at END_MAIN, where glass hides the player");
         }
      }

      EAsVoid("SelfVisuals outline", () -> EntityHaloEffect.tAsVoid(var0.matrices(), var1));
      if (var1 != null && n == CosmeticSubmissionHandler.Type_n.GLASS && o > 0.0 && EntityMaterialRenderer.YAsBoolean()) {
         EAsVoid("Liquid Glass refraction", () -> RefractionPostEffect.HAsBoolean("Liquid Glass", () -> {
               try {
                  CosmeticSubmissionHandler.xAsVoid(var1, var0.matrices(), L, CosmeticSubmissionHandler.Type_n.GLASS_MASK, -1, v, true, false);
                  L.CAsVoid();
               } finally {
                  L.FAsVoid();
               }
            }, o));
      }

      if (var1 != null && n != null && (n != CosmeticSubmissionHandler.Type_n.GLASS || EntityMaterialRenderer.YAsBoolean())) {
         EAsVoid("SelfVisuals", () -> {
            long var2 = OpenScreen.RAsLong();
            CosmeticSubmissionHandler.AAsVoid(var1, var0.matrices(), L, n, K, v, true);
            L.CAsVoid();
            SceneRenderResources.cAsVoid();
            SceneRenderResources.YAsVoid();
            OpenScreen.aAsVoid("SelfVisuals body material", var2);
         });
      }
   }

   private static void dAsVoid(WorldRenderContext var0) {
      if (A != BloomQuality.J) {
         q = false;
      }

      if (!q && var0.matrices() != null && var0.worldState() == k) {
         MinecraftClient var1 = MinecraftClient.getInstance();
         if (var1.world != null) {
            if (L == null) {
               L = new EffectDrawBuffers();
            }

            GpuTextureView var2 = RenderSystem.outputColorTextureOverride;
            GpuTextureView var3 = RenderSystem.outputDepthTextureOverride;
            String var4 = "shader initialization";

            try {
               if (X) {
                  SceneRenderResources.cAsVoid();
                  SceneRenderResources.YAsVoid();
               } else {
                  vAsVoid();
               }

               float var5 = V;
               EffectGeometryRenderer var6 = new EffectGeometryRenderer(var0.matrices(), L, var0.worldState().cameraRenderState);
               var4 = "Wings Ender Dragon opaque pass";
               Wings var7 = Wings.tAsWings();
               if (var7 != null && var7.isOperational() && var7.eAsBoolean()) {
                  EAsVoid("Wings", () -> {
                     if (var7.sAsBoolean(var0.matrices(), L)) {
                        L.CAsVoid();
                        SceneRenderResources.cAsVoid();
                        SceneRenderResources.YAsVoid();
                     }
                  });
               }

               AmbientParticles var8 = AmbientParticles.CAsAmbientParticles();
               int var9 = var8 != null && var8.isOperational() && !R.contains("Ambient Particles") ? 1 : 0;
               SoftParticlePipeline.TAsVoid(
                  !R.contains("Particles") && B.oAsBoolean() || var9 != 0 && var8.OAsB9().vAsBoolean() || RenderPerformanceStats.AAsBoolean()
               );
               var4 = "Particles/Trails/Body Trails/TargetAura/GhostEcho/Comets/HitZone/BlockOverlay base geometry";
               oAsVoid(var0, var6, var5, false);
               PlayerModel var10 = PlayerModel.hAsPlayerModel();
               if (var10 != null) {
                  EAsVoid("PlayerModel", () -> var10.MAsVoid(var0.matrices(), L));
               }

               L.CAsVoid();
               SceneRenderResources.cAsVoid();
               SceneRenderResources.YAsVoid();
               var4 = "Chams nearest-surface gradient silhouette";
               Chams var11 = Chams.mAsChams();
               if (var11 != null) {
                  EAsVoid("Chams", () -> var11.FAsVoid(var0.matrices(), L, var5));
               }

               EntityRenderState var12 = G;
               var4 = "SelfVisuals outline mask / player material";
               if (!X) {
                  WAsVoid(var0, var12);
               }

               TargetAura var13 = TargetAura.mAsTargetAura();
               DamageMarkers var14 = DamageMarkers.DAsDamageMarkers();
               if (var14 != null) {
                  EAsVoid("DamageMarkers", () -> var14.HAsVoid(var0.matrices(), var0.worldState().cameraRenderState));
               }

               WorldLyrics var15 = WorldLyrics.TAsWorldLyrics();
               if (var15 != null) {
                  EAsVoid("WorldLyrics", () -> var15.pAsVoid(var0.matrices(), var0.worldState().cameraRenderState, false));
               }

               JumpFX var16 = JumpFX.JAsJumpFX();
               BlockOverlay var17 = BlockOverlay.jAsBlockOverlay();
               KillFX var18 = KillFX.aAsKillFX();
               var4 = "ChinaHat after player materials";
               ChinaHat var19 = ChinaHat.uAsChinaHat();
               if (var19 != null) {
                  EAsVoid("ChinaHat", () -> {
                     var19.LAsVoid(var6, var5);
                     L.CAsVoid();
                  });
               }

               var4 = "Wings after player materials";
               if (var7 != null && var7.isOperational() && !var7.eAsBoolean()) {
                  bAsVoid("Wings", var6, () -> var7.dAsVoid(var6, L));
               }

               Framebuffer var20 = RenderPerformanceStats.ZAsBoolean()
                     || LAsBoolean(MediaSession.RAsDouble())
                     || !R.contains("Particles") && B.yAsBoolean()
                     || var9 != 0 && var8.OAsB9().qAsBoolean()
                     || !R.contains("Trails") && p.lAsBoolean()
                     || !R.contains("Body Trails") && Z.QAsBoolean()
                     || !R.contains("BlockOverlay") && var17 != null && var17.QAsBoolean(var0.worldState())
                     || x.stream().anyMatch(var1x -> !R.contains(var1x.tAsString()) && var1x.EAsBoolean(var0.worldState()))
                     || !R.contains("Comets") && CometRenderer.zAsBoolean()
                     || !R.contains("JumpFX") && var16 != null && var16.NAsBoolean()
                     || !R.contains("KillFX") && var18 != null && var18.oAsBoolean()
                     || !R.contains("HitZone") && AttackRaycast.YAsBoolean()
                     || !R.contains("GhostEcho") && H.sAsBoolean()
                     || !R.contains("TargetAura") && var13 != null && var13.VAsBoolean()
                     || !R.contains("SelfVisuals") && var12 != null && l && ZAsBoolean("SelfVisuals bloom")
                     || !R.contains("Wings") && var7 != null && var7.kAsBoolean()
                     || !R.contains("ChinaHat") && var19 != null && var19.jAsBoolean()
                     || !R.contains("WorldLyrics") && var15 != null && var15.jAsBoolean()
                  ? BloomQuality.rAsFramebuffer()
                  : null;
               var4 = "explicit bloom mask / composite";
               if (var20 != null) {
                  RenderSystem.outputColorTextureOverride = var20.getColorAttachmentView();
                  RenderSystem.outputDepthTextureOverride = var20.getDepthAttachmentView();
                  oAsVoid(var0, var6, var5, true);
                  if (var15 != null && var15.jAsBoolean()) {
                     EAsVoid("WorldLyrics", () -> var15.pAsVoid(var0.matrices(), var0.worldState().cameraRenderState, true));
                  }

                  if (var7 != null && var7.kAsBoolean()) {
                     bAsVoid("Wings", var6, () -> var7.GAsVoid(var6, L));
                  }

                  if (var19 != null && !R.contains("ChinaHat") && var19.jAsBoolean()) {
                     EAsVoid("ChinaHat", () -> var19.QAsVoid(var6, L));
                  }

                  GpuTextureView var21 = X ? SceneRenderResources.oAsGpuTextureView() : null;
                  if (var21 != null) {
                     RenderSystem.outputDepthTextureOverride = var21;
                  }

                  if (X) {
                     bAsVoid("Comets", var6, () -> {
                        CometRenderer.iAsVoid(var6, true);
                        L.CAsVoid();
                     });
                  }

                  if (var12 != null && l) {
                     EAsVoid(
                        "SelfVisuals bloom",
                        () -> {
                           CosmeticSubmissionHandler.xAsVoid(
                              var12,
                              var0.matrices(),
                              L,
                              n == CosmeticSubmissionHandler.Type_n.GLASS ? CosmeticSubmissionHandler.Type_n.GLASS_GLOW : n,
                              K,
                              n == CosmeticSubmissionHandler.Type_n.GLASS ? v * 0.25 : 0.25,
                              true,
                              false
                           );
                           L.CAsVoid();
                        }
                     );
                  }

                  RenderSystem.outputColorTextureOverride = var2;
                  RenderSystem.outputDepthTextureOverride = var3;
                  BloomQuality.QAsVoid();
               }
            } catch (RuntimeException var27) {
               q = true;
               ClientLog.lAsVoid("Render", "VisualRuntime stage=" + var4 + "; output attachments restored; retry on resource reload", var27);
            } finally {
               RenderSystem.outputColorTextureOverride = var2;
               RenderSystem.outputDepthTextureOverride = var3;
               DamageMarkers var23 = DamageMarkers.DAsDamageMarkers();
               if (var23 != null) {
                  var23.MAsVoid();
               }

               WorldLyrics var24 = WorldLyrics.TAsWorldLyrics();
               if (var24 != null) {
                  var24.OAsVoid();
               }

               if (L != null) {
                  L.hAsVoid();
               }

               EAsVoid("Refraction cleanup", RefractionPostEffect::aAsVoid);
               EAsVoid("Scene resource cleanup", SceneRenderResources::UAsVoid);
            }
         }
      }
   }

   public static void HAsVoid(DrawContext var0) {
      if (RAsBoolean()) {
         Pets var1 = Pets.VAsPets();
         if (var1 != null) {
            EAsVoid("Pets", () -> var1.XAsVoid(var0));
         }
      }
   }

   private static boolean LAsBoolean(double var0) {
      LineGlyphs var2 = LineGlyphs.LAsLineGlyphs();
      PearlParticles var3 = PearlParticles.iAsPearlParticles();
      CrystalChams var4 = CrystalChams.hAsCrystalChams();
      Pets var5 = Pets.VAsPets();
      CryptoPets var6 = CryptoPets.IAsCryptoPets();
      return !R.contains("Pets") && var5 != null && var5.TAsBoolean()
         || !R.contains("CryptoPets") && var6 != null && var6.tAsBoolean()
         || !R.contains("CrystalChams") && var4 != null && var4.nAsBoolean()
         || !R.contains("LineGlyphs") && var2 != null && var2.xAsBoolean(var0)
         || !R.contains("PearlParticles") && var3 != null && var3.LAsBoolean(var0);
   }

   private static void oAsVoid(WorldRenderContext var0, EffectGeometryRenderer var1, float var2, boolean var3) {
      EffectDrawBuffers var4 = L;
      RenderPerformanceStats.fAsVoid(var1, var3);

      for (PlacementPreview var6 : x) {
         bAsVoid(var6.tAsString(), var1, () -> var6.gAsVoid(var1, var3, var0.worldState()));
      }

      SnowGround var16 = SnowGround.vAsSnowGround();
      if (!var3 && var16 != null) {
         bAsVoid("SnowGround", var1, () -> var16.NAsVoid(var1));
      }

      CrystalChams var17 = CrystalChams.hAsCrystalChams();
      if (var17 != null) {
         bAsVoid("CrystalChams", var1, () -> var17.TAsVoid(var1, var3));
      }

      Pets var7 = Pets.VAsPets();
      if (var7 != null) {
         bAsVoid("Pets", var1, () -> var7.JAsVoid(var1, var3));
      }

      CryptoPets var8 = CryptoPets.IAsCryptoPets();
      if (var8 != null) {
         bAsVoid("CryptoPets", var1, () -> var8.rAsVoid(var1, var3));
      }

      bAsVoid("EmoteProps", var1, () -> EmotePropRenderer.AAsVoid(var1, var3));
      bAsVoid("Particles", var1, () -> B.MAsVoid(var1, var0.worldState().cameraRenderState.pos, var2, var3));
      bAsVoid("Trails", var1, () -> p.RAsVoid(var1, var2, var3));
      bAsVoid("Body Trails", var1, () -> Z.EAsVoid(var1, var3));
      if (!X) {
         bAsVoid("Comets", var1, () -> CometRenderer.iAsVoid(var1, var3));
      }

      AmbientParticles var9 = AmbientParticles.CAsAmbientParticles();
      if (var9 != null && var9.isOperational()) {
         bAsVoid("Ambient Particles", var1, () -> var9.OAsB9().AAsVoid(var1, var0.worldState().cameraRenderState.pos, var2, var3));
      }

      JumpFX var10 = JumpFX.JAsJumpFX();
      if (var10 != null) {
         bAsVoid("JumpFX", var1, () -> var10.EAsVoid(var1, var3));
      }

      KillFX var11 = KillFX.aAsKillFX();
      if (var11 != null) {
         bAsVoid("KillFX", var1, () -> var11.CAsVoid(var1, var0.matrices(), var4, var3));
      }

      BlockOverlay var12 = BlockOverlay.jAsBlockOverlay();
      if (var12 != null) {
         bAsVoid("BlockOverlay", var1, () -> var12.zAsVoid(var1, var3, var0.worldState()));
      }

      bAsVoid("HitZone", var1, () -> AttackRaycast.yAsVoid(var1, var3));
      TargetAura var13 = TargetAura.mAsTargetAura();
      if (var13 != null) {
         bAsVoid("TargetAura", var1, () -> var13.XAsVoid(var1, var2, var3));
      }

      bAsVoid("GhostEcho", var1, () -> H.BAsVoid(var0.matrices(), var4, var3));
      LineGlyphs var14 = LineGlyphs.LAsLineGlyphs();
      if (var14 != null) {
         EAsVoid("LineGlyphs", () -> var14.CAsVoid(var0.matrices(), var4, var0.worldState().cameraRenderState, MediaSession.RAsDouble(), var3));
      }

      PearlParticles var15 = PearlParticles.iAsPearlParticles();
      if (var15 != null) {
         EAsVoid("PearlParticles", () -> var15.aAsVoid(var0.matrices(), var4, var0.worldState().cameraRenderState, MediaSession.RAsDouble(), var3));
      }

      EAsVoid("Shared geometry", var4::CAsVoid);
   }
}
