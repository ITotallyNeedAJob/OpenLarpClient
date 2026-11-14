package com.larpclient.module;

import com.larpclient.BloomQuality;
import com.larpclient.ClientLog;
import com.larpclient.CosmeticSubmissionHandler;
import com.larpclient.EffectDrawBuffers;
import com.larpclient.EffectGeometryRenderer;
import com.larpclient.HalloweenShockwaveGeometry;
import com.larpclient.PumpkinRenderer;
import com.larpclient.SahurPetRenderer;
import com.larpclient.SharedRenderEffects;
import com.larpclient.SahurPoseUtils;
import com.larpclient.WalkPose;
import com.larpclient.MovementInput;
import com.larpclient.UiStyle;
import com.larpclient.d;
import com.larpclient.DiscordRPC;
import com.larpclient.LandQuery;
import com.larpclient.MediaSession;
import com.larpclient.OpenScreen;
import com.larpclient.AccountWalkAnim;
import com.larpclient.WebRenderer;
import com.larpclient.WidgetManager;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerSkinType;
import net.minecraft.network.packet.s2c.play.EntityDamageS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public final class KillFX extends Module {
   public static final int D = 4;
   private static KillFX x;
   private final ModeSetting style = this.addSetting(
      new ModeSetting(
         "Style",
         "Choose a finisher or vary each kill",
         "Random",
         "Random",
         "Execution",
         "Singularity",
         "Soul Pyre",
         "Star of David",
         "Tung Tung",
         "Tung Tung Floss"
      )
   );
   private final ModeSetting colors = this.addSetting(
      new ModeSetting("Colors", "Authored palettes, your theme or custom colors", "Style", "Style", "Theme", "Custom")
   );
   private final ColorSetting primaryColor = this.addSetting(new ColorSetting("Primary Color", "Main energy color", -10101505));
   private final ColorSetting secondaryColor = this.addSetting(new ColorSetting("Secondary Color", "Trail and impact color", -32547));
   private final BooleanSetting bloom = this.addSetting(new BooleanSetting("Bloom", "Use the shared world bloom pass", true));
   private final BooleanSetting sound = this.addSetting(new BooleanSetting("Sound", "Local windup and finishing cues", true));
   private final NumberSetting volume = this.addSetting(new NumberSetting("Volume", "Local sound volume", 0.55, 0.0, 1.0, 0.05));
   private final NumberSetting renderDistance = this.addSetting(new NumberSetting("Render Distance", "Maximum distance in blocks", 48.0, 8.0, 64.0, 4.0));
   private final wm B = new wm();
   private final r0 z = new r0();
   private final ArrayList<KillFX.Type_g> L = new ArrayList<>();
   private List<KillFX.Type_e> g = List.of();
   private Object t;
   private Object k;
   private int e = -1;
   private int J = -1;
   private long N;
   private Vec3d h;
   private static final RenderStateDataKey<KillFX.Type_a> X = RenderStateDataKey.create(() -> "Larp KillFX confirmed victim");

   public KillFX() {
      super("KillFX", "Play cinematic finishers when a player you damaged dies", Category.VISUALS, -1);
      x = this;
      this.primaryColor.visibleWhen(() -> this.colors.pAsBoolean("Custom"));
      this.secondaryColor.visibleWhen(() -> this.colors.pAsBoolean("Custom"));
      this.volume.visibleWhen(() -> this.sound.getValue());
      this.style.onChanged(var1 -> {
         this.lAsVoid();
         ClientLog.GAsVoid("KillFX", "Style changed: {}; active finishers cleared", var1);
      });
   }

   public static KillFX aAsKillFX() {
      return x;
   }

   public List<Setting<?>> lAsList() {
      return List.of(this.style, this.colors, this.primaryColor, this.secondaryColor);
   }

   public Setting<?> gAsSetting() {
      return this.style;
   }

   public List<Setting<?>> XAsList() {
      return List.of(this.bloom, this.sound, this.volume);
   }

   public List<Setting<?>> cAsList() {
      return List.of(this.renderDistance);
   }

   public String FAsString() {
      String var1 = this.style.getValue();

      return switch (var1) {
         case "Execution" -> "Lifts the victim into a double helix, slices the body and drops the skinned pieces onto terrain.";
         case "Singularity" -> "Twists the body into a collapsing star vortex, then releases a sharp ring of light.";
         case "Soul Pyre" -> "Burns the skin away in glowing fragments while spiralling soul embers rise from the body.";
         case "Star of David" -> "Lifts the body and turns its six skinned parts into two interlocking triangles facing your view.";
         case "Tung Tung" -> "Summons a towering Tung Tung Sahur, who raises his wooden bat and slams the victim to the ground.";
         case "Tung Tung Floss" -> "Sahur crouches, jumps onto the fallen player and dances the Floss with swinging arms and counter-swaying hips.";
         default -> "Varies between all finishers without repeating the last animation.";
      };
   }

   @Override
   public void onEnable() {
      this.lAsVoid();
      ClientLog.GAsVoid("KillFX", "Enabled: {}; server death events only, four detached finishers maximum", this.style.getValue());
   }

   @Override
   public void onDisable() {
      this.lAsVoid();
   }

   public void lAsVoid() {
      this.B.vAsVoid();
      this.L.clear();
      this.g = List.of();
      this.z.SAsVoid();
      this.t = this.k = null;
      this.h = null;
      this.e = -1;
   }

   private boolean XAsBoolean() {
      MinecraftClient var1 = getMc();
      if (this.TAsBoolean() && var1.world != null && var1.player != null) {
         if (this.t != var1.world
            || this.k != var1.player
            || this.e != BloomQuality.J
            || this.h != null && this.h.squaredDistanceTo(var1.player.getEntityPos()) > 256.0) {
            this.lAsVoid();
            this.t = var1.world;
            this.k = var1.player;
            this.e = BloomQuality.J;
         }

         this.h = var1.player.getEntityPos();
         return true;
      } else {
         this.lAsVoid();
         return false;
      }
   }

   public void aAsVoid(EntityDamageS2CPacket var1) {
      if (this.isEnabled() && this.XAsBoolean()) {
         MinecraftClient var2 = getMc();
         Entity var3 = var2.world.getEntityById(var1.entityId());
         if (var3 instanceof PlayerEntity && var3 != var2.player) {
            this.B.SAsVoid(var3, var1.sourceCauseId() == var2.player.getId(), mb.dAsDouble());
         }
      }
   }

   public void lAsVoid(EntityStatusS2CPacket var1) {
      if (this.isEnabled() && this.XAsBoolean()) {
         MinecraftClient var2 = getMc();
         if (var1.getEntity(var2.world) instanceof AbstractClientPlayerEntity var4 && var4 != var2.player) {
            if (var1.getStatus() == 35) {
               this.B.UAsVoid(var4);
               this.L.removeIf(var1x -> var1x.E == var4);
               this.g = this.g.stream().filter(var1x -> var1x.owner.E != var4).toList();
            } else if (var1.getStatus() == 3
               && this.B.VAsBoolean(var4, mb.dAsDouble())
               && !(var4.getEntityPos().squaredDistanceTo(var2.player.getEntityPos()) > this.renderDistance.getValue() * this.renderDistance.getValue())) {
               az.Type_f var5 = Halloween.RAsBoolean() ? az.Type_f.SOUL_PYRE : this.QAsAz_Type_f();
               int var6 = var5 == az.Type_f.SOUL_PYRE ? -19633 : (var5 == az.Type_f.SINGULARITY ? -5144321 : -10101505);
               int var7 = var5 == az.Type_f.SOUL_PYRE ? -8915729 : (var5 == az.Type_f.SINGULARITY ? -8328705 : -32547);
               if (var5 == az.Type_f.STAR_OF_DAVID) {
                  var6 = -8474625;
                  var7 = -1444097;
               }

               if (var5 == az.Type_f.TUNG_TUNG || var5 == az.Type_f.TUNG_TUNG_FLOSS) {
                  var6 = -18594;
                  var7 = -5188;
               }

               if (this.colors.pAsBoolean("Custom")) {
                  var6 = this.primaryColor.getValue();
                  var7 = this.secondaryColor.getValue();
               } else if (this.colors.pAsBoolean("Theme")) {
                  var6 = bd.w;
                  var7 = bd.Q;
               }

               long var8 = var4.getUuid().getMostSignificantBits() ^ var4.getUuid().getLeastSignificantBits() ^ ++this.N;
               double var10 = Halloween.RAsBoolean()
                  ? Math.atan2(var2.player.getX() - var4.getX(), var2.player.getZ() - var4.getZ())
                  : Math.toRadians((double)var4.bodyYaw);
               az var12 = new az(var5, var4.getEntityPos(), var10, mb.dAsDouble(), var8, var4.getSkin().model() == PlayerSkinType.SLIM);
               if (this.L.size() >= 4) {
                  this.L.removeFirst();
               }

               KillFX.Type_g var13 = new KillFX.Type_g(var4, var12, var6, var7, this.bloom.getValue(), this.sound.getValue(), this.volume.getValue());
               var13.o = Halloween.RAsBoolean();
               this.L.add(var13);
               if (!var13.o) {
                  this.EAsVoid(
                     var13,
                     var5 == az.Type_f.SOUL_PYRE
                        ? SoundEvents.ITEM_FIRECHARGE_USE
                        : (
                           var5 == az.Type_f.TUNG_TUNG_FLOSS
                              ? SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP
                              : (var5 == az.Type_f.TUNG_TUNG ? SoundEvents.ENTITY_IRON_GOLEM_ATTACK : SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE)
                        ),
                     0.8F
                  );
               }

               ClientLog.XAsVoid(
                  "KillFX", "Confirmed player death: id={}, style={}, source=recent observed server damage", var4.getId(), var13.o ? "Soul Harvest" : var5
               );
            }
         }
      }
   }

   private az.Type_f QAsAz_Type_f() {
      if (!this.style.pAsBoolean("Random")) {
         String var3 = this.style.getValue();

         return switch (var3) {
            case "Execution" -> az.Type_f.EXECUTION;
            case "Singularity" -> az.Type_f.SINGULARITY;
            case "Star of David" -> az.Type_f.STAR_OF_DAVID;
            case "Tung Tung" -> az.Type_f.TUNG_TUNG;
            case "Tung Tung Floss" -> az.Type_f.TUNG_TUNG_FLOSS;
            default -> az.Type_f.SOUL_PYRE;
         };
      } else {
         int var1 = az.Type_f.values().length;
         int var2 = Math.floorMod(this.N * 31L + Double.doubleToLongBits(mb.dAsDouble()), var1);
         if (var2 == this.J) {
            var2 = (var2 + 1) % var1;
         }

         this.J = var2;
         return az.Type_f.values()[var2];
      }
   }

   public void kAsVoid() {
      if (this.isEnabled() && this.XAsBoolean()) {
         MinecraftClient var1 = getMc();
         double var2 = mb.dAsDouble();
         this.B.JAsVoid(var2, var1xx -> {
            if (var1xx instanceof Entity var2x && !var2x.isRemoved() && var2x.getEntityWorld() == var1.world) {
               return true;
            }

            return false;
         });
         this.L
            .removeIf(
               var3 -> (var3.o ? var2 - var3.a.U >= 2.2 : var3.a.uAsBoolean(var2))
                     || var3.o && !Halloween.RAsBoolean()
                     || !var3.o && !this.isRuntimeActive()
                     || var3.v && !var3.E.isRemoved() && var3.E.isAlive()
            );

         for (KillFX.Type_g var5 : this.L) {
            var5.v = var5.v | !var5.E.isAlive();
            if (var5.o) {
               if (!var5.Z && var2 - var5.a.U >= 0.78) {
                  var5.Z = true;
                  ClientLog.XAsVoid("Halloween", "Pumpkin smash impact: victim={}, age={}s", var5.E.getId(), var2 - var5.a.U);
               }
            } else {
               var5.a.bAsVoid(var2, (var1xx, var2x) -> lq.AAsLq_Type_l(var1.world, var1xx, var2x));
               double var6 = var5.a.C == az.Type_f.EXECUTION
                  ? 0.85
                  : (
                     var5.a.C == az.Type_f.SINGULARITY
                        ? 1.9
                        : (
                           var5.a.C == az.Type_f.TUNG_TUNG_FLOSS
                              ? 1.62
                              : (var5.a.C == az.Type_f.TUNG_TUNG ? 1.56 : (var5.a.C == az.Type_f.STAR_OF_DAVID ? 1.85 : 1.25))
                        )
                  );
               if (!var5.Z && var2 - var5.a.U >= var6) {
                  var5.Z = true;
                  this.EAsVoid(
                     var5,
                     var5.a.C == az.Type_f.EXECUTION
                        ? (SoundEvent)SoundEvents.ITEM_TRIDENT_THUNDER.value()
                        : (
                           var5.a.C == az.Type_f.SINGULARITY
                              ? (SoundEvent)SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE.value()
                              : (
                                 var5.a.C == az.Type_f.TUNG_TUNG_FLOSS
                                    ? SoundEvents.ENTITY_PLAYER_BIG_FALL
                                    : (
                                       var5.a.C == az.Type_f.TUNG_TUNG
                                          ? SoundEvents.BLOCK_ANVIL_LAND
                                          : (
                                             var5.a.C == az.Type_f.STAR_OF_DAVID
                                                ? SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE
                                                : SoundEvents.BLOCK_BEACON_DEACTIVATE
                                          )
                                    )
                              )
                        ),
                     1.25F
                  );
               }
            }
         }
      }
   }

   private void EAsVoid(KillFX.Type_g var1, SoundEvent var2, float var3) {
      if (var1.C && !(var1.u <= 0.0) && getMc().world != null) {
         Vec3d var4 = var1.a.X;
         float var10006 = (float)var1.u;
         getMc().world.playSoundClient(var4.x, var4.y, var4.z, var2, SoundCategory.PLAYERS, var10006, var3, false);
      }
   }

   public void pAsVoid(CameraRenderState var1) {
      if (this.isEnabled()) {
         Vec3d var2 = var1.pos;
         this.g = List.of();
         if (this.XAsBoolean() && !this.L.isEmpty()) {
            Vector3f var3 = var1.orientation.transform(new Vector3f(1.0F, 0.0F, 0.0F));
            Vector3f var4 = var1.orientation.transform(new Vector3f(0.0F, 1.0F, 0.0F));
            Vec3d var5 = new Vec3d((double)var3.x, (double)var3.y, (double)var3.z);
            Vec3d var6 = new Vec3d((double)var4.x, (double)var4.y, (double)var4.z);
            this.z.sAsVoid();
            ArrayList var7 = new ArrayList();

            for (KillFX.Type_g var9 : this.L) {
               if (!(var9.a.X.squaredDistanceTo(var2) > this.renderDistance.getValue() * this.renderDistance.getValue())) {
                  if (var9.o) {
                     if (Halloween.RAsBoolean()) {
                        double var17 = mb.RAsDouble() - var9.a.U;
                        Vec3d var18 = var9.a.X;
                        if (w3.BAsFrustum() == null
                           || w3.BAsFrustum().isVisible(new Box(var18.x - 3.5, var18.y, var18.z - 3.5, var18.x + 3.5, var18.y + 5.6, var18.z + 3.5))) {
                           PumpkinRenderer.GAsVoid();
                           if (var9.T == null) {
                              if (CosmeticSubmissionHandler.oAsEntityRenderState(var9.E, 1.0F) instanceof PlayerEntityRenderState var22) {
                                 var9.T = var22;
                              } else {
                                 ClientLog.hAsVoid("Halloween", "Victim skin unavailable; keeping native corpse while the intact pumpkin and wave render", null);
                              }
                           }

                           az.Type_x[] var20 = var9.T == null ? null : HalloweenShockwaveGeometry.wAsAz_xArray(var9.a, var17);
                           var7.add(
                              new KillFX.Type_e(
                                 var9,
                                 var9.T,
                                 var20,
                                 var17,
                                 var2,
                                 null,
                                 null,
                                 null,
                                 HalloweenShockwaveGeometry.uAsWn_Type_k(var18, var9.a.M, var17, Halloween.HAsHalloween().fAsM7()),
                                 this.e
                              )
                           );
                        }
                     }
                  } else {
                     if (var9.T == null) {
                        if (!(CosmeticSubmissionHandler.oAsEntityRenderState(var9.E, 1.0F) instanceof PlayerEntityRenderState var11)) {
                           ClientLog.hAsVoid("KillFX", "Native avatar snapshot unavailable; leaving original corpse visible", null);
                           continue;
                        }

                        var9.T = var11;
                     }

                     double var16 = Math.max(0.0, mb.RAsDouble() - var9.a.U);
                     aa.Type_z var12 = var9.a.C == az.Type_f.STAR_OF_DAVID ? aa.iAsAa_Type_z(aa.IAsVec3d(var9.a, var16), var2, var5, var6) : aa.a;
                     az.Type_x[] var13 = new az.Type_x[6];

                     for (int var14 = 0; var14 < var13.length; var14++) {
                        var13[var14] = var9.a.JAsAz_Type_x(var14, var16, (double)mb.LAsFloat(), var12);
                     }

                     dj.Type_z var21 = null;
                     d.Type_g var15 = null;
                     if (var9.a.C == az.Type_f.TUNG_TUNG) {
                        SahurPetRenderer.OAsVoid();
                        if (var9.d == null) {
                           var9.d = new dj(SahurPetRenderer.TAsAo_Type_u());
                           ClientLog.XAsVoid("KillFX", "Tung Tung ready: shared Sahur mesh, height={} blocks, articulated bat strike at {}s", 3.6, 1.56);
                        }

                        var21 = var9.d.oAsDj_Type_z(var9.a, var16, var2);
                     }

                     if (var9.a.C == az.Type_f.TUNG_TUNG_FLOSS) {
                        SahurPetRenderer.OAsVoid();
                        if (var9.c == null) {
                           var9.c = new d(SahurPetRenderer.TAsAo_Type_u());
                           ClientLog.XAsVoid("KillFX", "Tung Tung Floss ready: articulated jump, landing at {}s, four Floss cycles, expiry={}s", 1.62, 7.5);
                        }

                        var15 = var9.c.NAsD_Type_g(var9.a, var16, var2);
                     }

                     var7.add(new KillFX.Type_e(var9, var9.T, var13, var16, var2, var12, var21, var15, null, this.e));
                  }
               }
            }

            this.g = List.copyOf(var7);
         }
      }
   }

   public void LAsVoid(Entity var1, EntityRenderState var2) {
      if (this.isEnabled() && this.TAsBoolean() && var1.getEntityWorld() == this.t) {
         for (KillFX.Type_g var4 : this.L) {
            if (var4.E == var1) {
               var2.setData(X, new KillFX.Type_a(var1, this.t, this.e));
               return;
            }
         }
      }
   }

   public boolean uAsBoolean(EntityRenderState var1) {
      if (!this.isEnabled()) {
         return false;
      } else if (this.TAsBoolean() && SharedRenderEffects.RAsBoolean() && SharedRenderEffects.ZAsBoolean("KillFX") && !CosmeticSubmissionHandler.bAsBoolean()) {
         KillFX.Type_a var2 = (KillFX.Type_a)var1.getData(X);
         if (var2 != null && var2.world == getMc().world && var2.generation == BloomQuality.J) {
            for (KillFX.Type_e var4 : this.g) {
               if (var4.skin != null
                  && var4.parts != null
                  && var4.owner.E == var2.victim
                  && this.L.contains(var4.owner)
                  && var4.generation == var2.generation
                  && (var4.age < 0.25 || var1 instanceof LivingEntityRenderState var5 && var5.deathTime > 0.0F)) {
                  return true;
               }
            }

            return false;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private boolean TAsBoolean() {
      return this.isRuntimeActive() || Halloween.RAsBoolean();
   }

   public boolean oAsBoolean() {
      return this.isEnabled()
         ? this.TAsBoolean()
            && this.g
               .stream()
               .anyMatch(var0 -> var0.soul != null ? var0.soul.pumpkin() != null && Halloween.HAsHalloween().fAsM7().dAsBoolean() : var0.owner.B)
         : false;
   }

   public void CAsVoid(EffectGeometryRenderer var1, MatrixStack var2, EffectDrawBuffers var3, boolean var4) {
      if (this.isEnabled() && this.TAsBoolean()) {
         long var5 = op.RAsLong();

         try {
            for (KillFX.Type_e var8 : this.g) {
               if (var8.soul != null) {
                  if (Halloween.RAsBoolean() && var8.generation == BloomQuality.J && (!var4 || Halloween.HAsHalloween().fAsM7().dAsBoolean())) {
                     if (!var4 && var8.skin != null && var8.parts != null) {
                        this.z.MAsVoid(var8.skin, var8.owner.a, var8.parts, var8.camera, var2, var3, -1, false);
                     }

                     HalloweenShockwaveGeometry.NAsVoid(var1, var2, var3, var8.soul, var8.camera, var4);
                  }
               } else if (var8.generation == BloomQuality.J && (!var4 || var8.owner.B)) {
                  this.z.MAsVoid(var8.skin, var8.owner.a, var8.parts, var8.camera, var2, var3, var8.owner.J, var4);
                  if (!var4 && var8.strike != null) {
                     SahurPetRenderer.QAsVoid(var2, var3, var8.strike.transform(), var8.strike.vertices(), var8.strike.colors(), var8.strike.opacity());
                  }

                  if (!var4 && var8.floss != null) {
                     SahurPetRenderer.QAsVoid(var2, var3, var8.floss.transform(), var8.floss.vertices(), var8.floss.colors(), var8.floss.opacity());
                  }

                  b0.yAsVoid(var1, var8.owner.a, var8.parts, var8.age, var8.owner.J, var8.owner.I, var4, var8.starPlane, var8.strike, var8.floss);
               }
            }
         } finally {
            op.aAsVoid("KillFX", var5);
         }
      }
   }

   private static record Type_a(Object victim, Object world, int generation) {
   }

   private static record Type_e(
      KillFX.Type_g owner,
      PlayerEntityRenderState skin,
      az.Type_x[] parts,
      double age,
      Vec3d camera,
      aa.Type_z starPlane,
      dj.Type_z strike,
      d.Type_g floss,
      HalloweenShockwaveGeometry.Type_k soul,
      int generation
   ) {
   }

   private static final class Type_g {
      final AbstractClientPlayerEntity E;
      final az a;
      final int J;
      final int I;
      final boolean B;
      final boolean C;
      final double u;
      PlayerEntityRenderState T;
      dj d;
      d c;
      boolean Z;
      boolean v;
      boolean o;

      Type_g(AbstractClientPlayerEntity var1, az var2, int var3, int var4, boolean var5, boolean var6, double var7) {
         this.E = var1;
         this.a = var2;
         this.J = var3;
         this.I = var4;
         this.B = var5;
         this.C = var6;
         this.u = var7;
      }
   }
}
