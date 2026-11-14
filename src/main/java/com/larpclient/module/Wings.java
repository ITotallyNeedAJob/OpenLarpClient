package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.CosmeticSubmissionHandler;
import com.larpclient.DragonWingRenderer;
import com.larpclient.EffectDrawBuffers;
import com.larpclient.EffectGeometryRenderer;
import com.larpclient.LarpClient;
import com.larpclient.UiStyle;
import com.larpclient.ColorUtil;
import com.larpclient.MediaSession;
import com.larpclient.OpenScreen;
import com.larpclient.RodAttempt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.MatrixStack.Entry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public final class Wings extends Module {
   public static final String j = "Luminous";
   public static final String h = "Ender Dragon";
   private static Wings l;
   private final BooleanSetting self = this.addSetting(new BooleanSetting("Self", "Show your wings in third person", true));
   private final BooleanSetting friends = this.addSetting(new BooleanSetting("Friends", "Show wings on friends", true));
   private final BooleanSetting others = this.addSetting(new BooleanSetting("Others", "Show wings on other players", false));
   private final NumberSetting size = this.addSetting(new NumberSetting("Size", "Wing size", 1.0, 0.75, 1.35, 0.05));
   private final BooleanSetting customColors = this.addSetting(new BooleanSetting("Custom Colors", "Cycle between your two colors", true));
   private final ColorSetting color1 = this.addSetting(new ColorSetting("Color 1", "First wing color", -52586));
   private final ColorSetting color2 = this.addSetting(new ColorSetting("Color 2", "Second wing color", -13461761));
   private final ModeSetting style = this.addSetting(new ModeSetting("Style", "Wing design", "Luminous", "Luminous", "Ender Dragon"));
   private final ColorSetting dragonColor = this.addSetting(
      new ColorSetting("Dragon Color", "Tints the Ender Dragon texture; white keeps it vanilla and alpha sets opacity", -1)
   );
   public static final int p = 32;
   private List<Wings.Type_h> G = List.of();
   private List<Wings.Type_v> P = List.of();
   private final IdentityHashMap<Object, ro.Type_j> L = new IdentityHashMap<>();
   private ClientWorld I;
   private ClientWorld T;
   private DragonWingRenderer.Type_q u;
   private final MatrixStack b = new MatrixStack();
   private final MatrixStack C = new MatrixStack();

   public Wings() {
      super("Wings", "Wear luminous membrane wings or Ender Dragon wings", Category.VISUALS, -1);
      l = this;
      this.customColors.visibleWhen(() -> this.style.pAsBoolean("Luminous"));
      this.color1.visibleWhen(() -> this.style.pAsBoolean("Luminous") && this.customColors.getValue());
      this.color2.visibleWhen(() -> this.style.pAsBoolean("Luminous") && this.customColors.getValue());
      this.dragonColor.visibleWhen(() -> this.style.pAsBoolean("Ender Dragon"));
      this.style.onChanged(var0 -> ClientLog.GAsVoid("Wings", "Style={}", var0));
   }

   public static Wings tAsWings() {
      return l;
   }

   public List<Setting<?>> tAsList() {
      return List.of(this.self, this.friends, this.others);
   }

   public List<Setting<?>> XAsList() {
      return List.of(this.style, this.size, this.customColors, this.color1, this.color2, this.dragonColor);
   }

   public boolean eAsBoolean() {
      return this.isEnabled() ? this.style.pAsBoolean("Ender Dragon") : false;
   }

   public Wings.Type_n PAsWings_Type_n() {
      if (this.isEnabled()) {
         if (!this.isOperational() || !this.self.getValue()) {
            return null;
         } else if (this.eAsBoolean()) {
            return this.dragonColor.getValue() >>> 24 == 0
               ? null
               : new Wings.Type_n(true, this.size.getValue().floatValue(), 0, 0, this.dragonColor.getValue());
         } else {
            return new Wings.Type_n(
               false,
               this.size.getValue().floatValue(),
               this.customColors.getValue() ? this.color1.getValue() : bd.w,
               this.customColors.getValue() ? this.color2.getValue() : bd.Q,
               0
            );
         }
      } else {
         return null;
      }
   }

   public static int OAsInt(int var0, int var1, double var2) {
      return bg.xAsInt(var0, var1, (Math.sin(var2 * 2.0) + 1.0) / 2.0);
   }

   public static int DAsInt(int var0, int var1, double var2) {
      return bg.xAsInt(var0, var1, (Math.sin(var2 * 2.0 + 2.1) + 1.0) / 2.0);
   }

   @Override
   public void onEnable() {
      ClientLog.GAsVoid(
         "Wings",
         this.eAsBoolean()
            ? "Ender Dragon wings enabled; vanilla dragon wing parts on the native torso, 48 quads per wearer, 32 nearby previews"
            : "Articulated membrane wings enabled on the native torso; shared bloom, 146 base quads per wearer, 32 nearby previews"
      );
   }

   @Override
   public void onDisable() {
      this.G = List.of();
      this.P = List.of();
      this.I = null;
      this.L.clear();
      this.T = null;
   }

   public void JAsVoid(float var1) {
      if (this.isEnabled()) {
         this.G = List.of();
         this.P = List.of();
         this.I = null;
         if (this.isOperational() && this.isInGame()) {
            if (this.T != mc.world) {
               this.L.clear();
               this.T = mc.world;
            }

            MCFriend var2 = LarpClient.CAsLarpClient().mAsF().EAsModule(MCFriend.class);
            ArrayList var3 = new ArrayList();
            ArrayList var4 = new ArrayList();
            Set var5 = Collections.newSetFromMap(new IdentityHashMap());
            int var6 = 0;
            float var7 = this.size.getValue().floatValue();
            boolean var8 = this.eAsBoolean();
            int var9 = this.dragonColor.getValue();
            int var10 = this.customColors.getValue() ? this.color1.getValue() : bd.w;
            int var11 = this.customColors.getValue() ? this.color2.getValue() : bd.Q;
            int var12 = OAsInt(var10, var11, mb.RAsDouble());
            int var13 = DAsInt(var10, var11, mb.RAsDouble());

            for (AbstractClientPlayerEntity var15 : mc.world.getPlayers()) {
               if (var15 == mc.player) {
                  if (!this.self.getValue() || mc.options.getPerspective().isFirstPerson()) {
                     continue;
                  }
               } else {
                  int var16 = var2 != null && var2.SAsBoolean(var15.getName().getString()) ? 1 : 0;
                  if (var16 == 0 ? this.friends.getValue() : this.others.getValue() || var6 >= 32) {
                     continue;
                  }
               }

               if (var15.isAlive()
                  && !var15.isInvisible()
                  && !var15.isSpectator()
                  && !var15.isSleeping()
                  && !var15.isTouchingWater()
                  && !var15.isCrawling()
                  && !var15.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA)
                  && !(var15.squaredDistanceTo(mc.player) > 9216.0)) {
                  if (var15 != mc.player) {
                     var6++;
                  }

                  float var30 = Module3DUI.PAsFloat(var15, MathHelper.lerpAngleDegrees(var1, var15.lastBodyYaw, var15.bodyYaw), var1);
                  float var17 = (float)var15.age + var1;
                  float var18 = var15.limbAnimator.getAmplitude(var1);
                  Vec3d var19 = var15.getLerpedPos(var1);
                  ro.Type_j var20 = this.L.computeIfAbsent(var15, var0 -> new ro.Type_j());
                  var20.cAsVoid(var17, var15.isInSneakingPose(), var18, var15.isGliding(), FAsBoolean(var15), var19);
                  var5.add(var15);
                  if (var8) {
                     float var31 = DragonWingRenderer.IAsFloat(var20.YAsFloat(), var15.isGliding(), (float)var15.getGlidingTicks() + var1);
                     var20.OAsVoid(DragonWingRenderer.MAsFloat(var20.aAsFloat(), var20.eAsFloat(), var20.vAsFloat(), var31));
                     if (var9 >>> 24 != 0) {
                        DragonWingRenderer.Type_n var32 = DragonWingRenderer.NAsMe_Type_n(
                           var20.ZAsFloat() + DragonWingRenderer.XAsFloat(var15.getId()), var20.aAsFloat(), var20.eAsFloat(), var20.vAsFloat(), var31
                        );
                        var4.add(new Wings.Type_v(CosmeticSubmissionHandler.oAsEntityRenderState(var15, var1), var32, var7, var9));
                     }
                  } else {
                     Entry var22;
                     byte var10000;
                     label124: {
                        EntityRenderState var21 = CosmeticSubmissionHandler.oAsEntityRenderState(var15, var1);
                        var22 = CosmeticSubmissionHandler.JAsMatrixStack_Entry(var21, this.C);
                        if (var21 instanceof BipedEntityRenderState var24 && var24.isInSneakingPose) {
                           var10000 = 1;
                           break label124;
                        }

                        var10000 = 0;
                     }

                     int var23 = var10000;
                     float var33 = var22 == null
                        ? var20.YAsFloat()
                        : DragonWingRenderer.IAsFloat(var20.YAsFloat(), var15.isGliding(), (float)var15.getGlidingTicks() + var1);
                     ro.Type_h var25 = ro.rAsRo_Type_h(var20.eAsFloat(), var33, (float)var15.getGlidingTicks() + var1, var15.getPitch(var1));
                     ro.Type_v var26 = ro.vAsRo_Type_v(var17, var20.aAsFloat(), var20.eAsFloat(), var33);
                     Vec3d var27 = var22 == null ? var19 : mc.gameRenderer.getCamera().getCameraPos();
                     Matrix4f var28 = var22 == null
                        ? ro.lAsMatrix4f(var25, var30, var17, var20.aAsFloat(), var7, -1)
                        : new Matrix4f(var22.getPositionMatrix()).mul(ro.RAsMatrix4f(var25, var17, var20.aAsFloat(), var7, -1, var23 != 0));
                     Matrix4f var29 = var22 == null
                        ? ro.lAsMatrix4f(var25, var30, var17, var20.aAsFloat(), var7, 1)
                        : new Matrix4f(var22.getPositionMatrix()).mul(ro.RAsMatrix4f(var25, var17, var20.aAsFloat(), var7, 1, var23 != 0));
                     var3.add(
                        new Wings.Type_h(
                           ro.gAsRo_Type_r(var27, var28, -1, var26, var12, var13, var7), ro.gAsRo_Type_r(var27, var29, 1, var26, var12, var13, var7)
                        )
                     );
                  }
               }
            }

            this.L.keySet().removeIf(var1x -> !var5.contains(var1x));
            this.I = mc.world;
            this.G = List.copyOf(var3);
            this.P = List.copyOf(var4);
         } else {
            this.L.clear();
            this.T = null;
         }
      }
   }

   private static boolean FAsBoolean(PlayerEntity var0) {
      return !var0.isOnGround() && !var0.hasVehicle() && !var0.isClimbing() && !var0.isGliding();
   }

   public boolean sAsBoolean(MatrixStack var1, EffectDrawBuffers var2) {
      if (!this.isEnabled()) {
         return false;
      } else if (this.isOperational() && this.I == mc.world && !this.P.isEmpty()) {
         if (this.u == null) {
            this.u = DragonWingRenderer.Type_q.XAsMe_Type_q();
            ClientLog.GAsVoid("Wings", "Ender Dragon wing parts ready from the vanilla layer definition; texture={}", DragonWingRenderer.C);
         }

         long var3 = op.RAsLong();
         byte var5 = 0;

         for (Wings.Type_v var7 : this.P) {
            Entry var8 = CosmeticSubmissionHandler.JAsMatrixStack_Entry(var7.state, var1);
            if (var8 != null) {
               byte var10000;
               label40: {
                  this.b.peek().copy(var8);
                  this.u.eAsVoid(var7.angles);
                  if (var7.state instanceof BipedEntityRenderState var10 && var10.isInSneakingPose) {
                     var10000 = 1;
                     break label40;
                  }

                  var10000 = 0;
               }

               int var9 = var10000;
               this.u
                  .mAsVoid(this.b, var2.XAsVertexConsumer(DragonWingRenderer.mAsRenderLayer(var7.color)), var7.size, var9 != 0, var7.state.light, var7.color);
               var5 = 1;
            }
         }

         op.aAsVoid("Wings Ender Dragon attachment and submission", var3);
         return var5 != 0;
      } else {
         return false;
      }
   }

   public void dAsVoid(EffectGeometryRenderer var1, EffectDrawBuffers var2) {
      if (this.isEnabled() && this.isOperational() && this.I == mc.world) {
         for (ro.Type_x var6 : ro.Type_x.values()) {
            for (Wings.Type_h var8 : this.G) {
               ro.vAsVoid(var1, var8.left, var6);
               ro.vAsVoid(var1, var8.right, var6);
            }

            var2.CAsVoid();
         }
      }
   }

   public boolean kAsBoolean() {
      return this.isEnabled() ? this.isOperational() && this.I == mc.world && this.G.stream().anyMatch(var0 -> ro.UAsBoolean(var0.left)) : false;
   }

   public void GAsVoid(EffectGeometryRenderer var1, EffectDrawBuffers var2) {
      if (this.isEnabled() && this.kAsBoolean()) {
         for (Wings.Type_h var4 : this.G) {
            ro.MAsVoid(var1, var4.left);
            ro.MAsVoid(var1, var4.right);
         }

         var2.CAsVoid();
      }
   }

   private static record Type_h(ro.Type_r left, ro.Type_r right) {
   }

   public static record Type_n(boolean dragon, float size, int first, int second, int dragonColor) {
   }

   private static record Type_v(EntityRenderState state, DragonWingRenderer.Type_n angles, float size, int color) {
   }
}
