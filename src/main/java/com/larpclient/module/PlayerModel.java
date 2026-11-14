package com.larpclient.module;

import com.larpclient.BloomQuality;
import com.larpclient.ClientLog;
import com.larpclient.EffectDrawBuffers;
import com.larpclient.SahurPetRenderer;
import com.larpclient.WaterBubble;
import com.larpclient.ParticleEmitter;
import com.larpclient.QueueYield;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public final class PlayerModel extends Module {
   private static PlayerModel g;
   private final NumberSetting scale = this.addSetting(new NumberSetting("Scale", "Visual character size", 1.0, 0.5, 2.0, 0.05));
   private final BooleanSetting showTiming = this.addSetting(new BooleanSetting("Show Timing", "Show CPU model submission timing", false));
   private Object k;
   private Object c;
   private long x;
   private long r;
   private long H;
   private boolean b;
   private LivingEntityRenderState P;
   private int h = -1;
   private final Matrix4f u = new Matrix4f();
   private float Z;
   private boolean O;
   private bw d;

   public PlayerModel() {
      super("Player Model", "Replace your rendered player with Tung Tung Tung Sahur", Category.VISUALS, -1);
      g = this;
   }

   public static PlayerModel hAsPlayerModel() {
      return g;
   }

   public List<Setting<?>> eAsList() {
      return List.of(this.scale);
   }

   public List<Setting<?>> QAsList() {
      return List.of(this.showTiming);
   }

   public boolean zAsBoolean() {
      return this.isEnabled() ? this.isRuntimeActive() && this.h != BloomQuality.J && SahurPetRenderer.SAsBoolean() : false;
   }

   public boolean FAsBoolean(EntityRenderState var1) {
      return this.isEnabled() ? this.zAsBoolean() && this.P == var1 : false;
   }

   public void LAsVoid(LivingEntityRenderState var1) {
      if (this.isEnabled()) {
         this.P = var1;
      }
   }

   @Override
   public void onEnable() {
      this.LAsVoid();

      try {
         this.FAsVoid();
      } catch (Exception var2) {
         ClientLog.lAsVoid("PlayerModel", "Mesh stage failed; retaining native body", var2);
         this.setEnabled(false);
      }
   }

   private void FAsVoid() {
      SahurPetRenderer.bAsVoid();
      if (this.d == null || this.d.pAsAo_Type_u() != SahurPetRenderer.TAsAo_Type_u()) {
         this.d = new bw(SahurPetRenderer.TAsAo_Type_u());
      }

      ClientLog.XAsVoid("PlayerModel", "Sahur ready: walk rig, {} triangles, scale={}", this.d.aAsAo_Type_u().triangles(), this.scale.getValue());
   }

   @Override
   public void onDisable() {
      this.LAsVoid();
   }

   public void LAsVoid() {
      this.b = false;
      this.P = null;
      this.k = this.c = null;
      this.H = this.r = 0L;
   }

   public void SAsVoid() {
      this.LAsVoid();
      this.h = BloomQuality.J;
   }

   public void qAsVoid(float var1, Vec3d var2) {
      if (this.isEnabled()) {
         this.b = false;
         if (this.isRuntimeActive() && this.isInGame() && this.h != BloomQuality.J) {
            if (!SahurPetRenderer.SAsBoolean() || this.d == null || this.d.pAsAo_Type_u() != SahurPetRenderer.TAsAo_Type_u()) {
               this.FAsVoid();
            }

            if (this.zAsBoolean() && this.isInGame()) {
               ClientPlayerEntity var3 = mc.player;
               if (this.k != var3 || this.c != mc.world) {
                  LivingEntityRenderState var4 = this.P;
                  this.LAsVoid();
                  this.P = var4;
                  this.k = var3;
                  this.c = mc.world;
               }

               if ((!mc.options.getPerspective().isFirstPerson() || p5.oAsBoolean()) && this.P != null) {
                  this.Z = this.scale.getValue().floatValue();
                  this.O = var3.isInSneakingPose();
                  this.d.xAsFloatArray(this.P.limbSwingAnimationProgress, this.P.limbSwingAmplitude);
                  qy.cAsMatrix4f(
                     this.u,
                     SahurPetRenderer.TAsAo_Type_u(),
                     (float)(MathHelper.lerp((double)var1, var3.lastRenderX, var3.getX()) - var2.x),
                     (float)(MathHelper.lerp((double)var1, var3.lastRenderY, var3.getY()) - var2.y),
                     (float)(MathHelper.lerp((double)var1, var3.lastRenderZ, var3.getZ()) - var2.z),
                     (float)Math.toRadians((double)Module3DUI.PAsFloat(var3, MathHelper.lerpAngleDegrees(var1, var3.lastBodyYaw, var3.bodyYaw), var1)),
                     this.Z,
                     this.O
                  );
                  this.b = true;
               }
            }
         }
      }
   }

   public void MAsVoid(MatrixStack var1, EffectDrawBuffers var2) {
      if (this.isEnabled() && this.b && this.zAsBoolean() && this.k == mc.player && this.c == mc.world) {
         long var3 = System.nanoTime();

         try {
            SahurPetRenderer.qAsVoid(var1, var2, this.u, this.d.vAsFloatArray(), this.d.aAsAo_Type_u().colors());
         } catch (RuntimeException var6) {
            ClientLog.lAsVoid("PlayerModel", "Submission failed; restoring native body", var6);
            this.setEnabled(false);
         }

         if (this.showTiming.getValue()) {
            this.r = this.r + (System.nanoTime() - var3);
            this.H++;
            if (System.nanoTime() - this.x > 1000000000L) {
               mc.player
                  .sendMessage(
                     Text.literal(
                        String.format(Locale.ROOT, "%s: %.3f ms CPU/frame", "Tung Tung Tung Sahur", (double)this.r / 1000000.0 / (double)Math.max(1L, this.H))
                     ),
                     true
                  );
               this.x = System.nanoTime();
               this.r = this.H = 0L;
            }
         }
      }
   }

   public boolean vAsBoolean(EntityRenderState var1, Arm var2, MatrixStack var3) {
      if (this.isEnabled()) {
         if (this.FAsBoolean(var1) && this.b) {
            int var4 = var2 == Arm.RIGHT ? 1 : 0;
            qy.JAsVoid(
               var3, SahurPetRenderer.TAsAo_Type_u(), var4 != 0, this.d.NAsVector3f(var4 != 0, qy.gAsVector3f(var4 != 0)), this.Z, this.O, this.P.baseScale
            );
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }
}
