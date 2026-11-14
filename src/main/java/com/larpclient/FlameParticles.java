package com.larpclient;

import com.larpclient.module.FlameHands;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.util.random.RandomGenerator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderSetup;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.MatrixStack.Entry;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class FlameParticles {
   public static final int e = 768;
   private static final Identifier f = Identifier.of("larp-client", "textures/particles/flame.png");
   private static final Identifier P = Identifier.of("larp-client", "textures/particles/flame_glow.png");
   private static final FlameParticles.Type_k[] t = new FlameParticles.Type_k[]{
      new FlameParticles.Type_k(new Random()), new FlameParticles.Type_k(new Random())
   };
   private static Object F;
   private static Object s;
   private static int h = -1;
   private static boolean v;
   private static Vec3d w;

   public static void UAsVoid() {
      for (FlameParticles.Type_k var3 : t) {
         var3.QAsVoid();
      }

      s = null;
      F = null;
      w = null;
      v = false;
   }

   public static void bAsVoid() {
      MinecraftClient var0 = MinecraftClient.getInstance();
      if (F != var0.world || s != var0.player || h != BloomQuality.J) {
         UAsVoid();
         F = var0.world;
         s = var0.player;
         h = BloomQuality.J;
      }

      if (var0.player != null) {
         Vec3d var1 = var0.player.getEntityPos();
         if (w != null && w.squaredDistanceTo(var1) > 64.0) {
            for (FlameParticles.Type_k var5 : t) {
               var5.QAsVoid();
            }

            ClientLog.XAsVoid("FlameHands", "Cleared histories after player relocation");
         }

         w = var1;
      }

      if (var0.player == null || var0.world == null || !var0.options.getPerspective().isFirstPerson()) {
         for (FlameParticles.Type_k var9 : t) {
            var9.QAsVoid();
         }
      }
   }

   public static void PAsVoid(Arm var0, MatrixStack var1, OrderedRenderCommandQueue var2, boolean var3) {
      FlameHands var4 = FlameHands.pAsFlameHands();
      MinecraftClient var5 = MinecraftClient.getInstance();
      if (var4 != null && var4.isRuntimeActive() && var5.player != null && var5.world != null && var5.options.getPerspective().isFirstPerson()) {
         bAsVoid();
         FlameParticles.Type_k var6 = t[var0 == Arm.RIGHT ? 0 : 1];
         if (!var4.hAsBoolean(var0 == var5.player.getMainArm(), var3)) {
            var6.QAsVoid();
         } else if (!v) {
            try {
               FlameParticles.Type_t.uAsVoid();
               Camera var7 = var5.gameRenderer.getCamera();
               Vec3d var13 = var5.player.getVelocity();
               Vector3f var14 = new Vector3f((float)var13.x, (float)var13.y, (float)var13.z).rotate(new Quaternionf(var7.getRotation()).conjugate());
               FlameParticles.Type_z var15 = var4.nAsLh_Type_z();
               var6.SAsVoid(MediaSession.RAsDouble(), var7.getYaw(), var7.getPitch(), var14.x, var14.y, var14.z, var3, var15);
               double[] var16 = var6.RAsDoubleArray2(var1.peek().getPositionMatrix(), var15);
               if (var16.length == 0) {
                  return;
               }

               var2.getBatchingQueue(2)
                  .submitCustom(
                     new MatrixStack(),
                     FlameParticles.Type_t.qAsRenderLayer(var4.renderMode.pAsBoolean("Glow")),
                     (var1x, var2x) -> nAsVoid(var16, var1x, var2x)
                  );
               OpenScreen.YAsVoid("Flame hand particles", (long)(var16.length / 6));
            } catch (RuntimeException var121) {
               for (FlameParticles.Type_k var11 : t) {
                  var11.QAsVoid();
               }

               v = true;
               ClientLog.lAsVoid("FlameHands", "Hand particle submission failed; suspended until disable or resource reload", var121);
            }
         }
      }
   }

   private static void nAsVoid(double[] var0, Entry var1, VertexConsumer var2) {
      for (int var3 = 0; var3 < var0.length; var3 += 6) {
         float var4 = (float)var0[var3];
         float var5 = (float)var0[var3 + 1];
         float var6 = (float)var0[var3 + 2];
         float var7 = (float)var0[var3 + 3] * 0.5F;
         float var8 = (float)var0[var3 + 4];
         float var9 = (float)Math.cos((double)var8) * var7;
         float var10 = (float)Math.sin((double)var8) * var7;
         int var11 = (int)((long)var0[var3 + 5]);
         DAsVoid(var2, var1, var4 - var9 - var10, var5 + var9 - var10, var6, var11, 0.0F, 1.0F);
         DAsVoid(var2, var1, var4 + var9 - var10, var5 + var9 + var10, var6, var11, 1.0F, 1.0F);
         DAsVoid(var2, var1, var4 + var9 + var10, var5 - var9 + var10, var6, var11, 1.0F, 0.0F);
         DAsVoid(var2, var1, var4 - var9 + var10, var5 - var9 - var10, var6, var11, 0.0F, 0.0F);
      }
   }

   private static void DAsVoid(VertexConsumer var0, Entry var1, float var2, float var3, float var4, int var5, float var6, float var7) {
      var0.vertex(var1, var2, var3, var4).color(var5).texture(var6, var7);
   }

   public static float LAsFloat(float var0) {
      float var1 = var0 % 360.0F;
      return var1 >= 180.0F ? var1 - 360.0F : (var1 < -180.0F ? var1 + 360.0F : var1);
   }

   public static int EAsInt(double var0, FlameParticles.Type_z var2) {
      String var3 = var2.colorMode;

      return switch (var3) {
         case "Gradient" -> AAsInt(var2.primary, var2.secondary, (float)(Math.sin(var0 * 3.0) * 0.5 + 0.5));
         case "Rainbow" -> Color.HSBtoRGB((float)(var0 % 4.0 / 4.0), 1.0F, 1.0F);
         default -> var2.primary;
      };
   }

   public static int AAsInt(int var0, int var1, float var2) {
      float var3 = Math.clamp(var2, 0.0F, 1.0F);
      int var4 = 0;

      for (int var5 = 0; var5 <= 24; var5 += 8) {
         var4 |= Math.round((float)(var0 >>> var5 & 0xFF) * (1.0F - var3) + (float)(var1 >>> var5 & 0xFF) * var3) << var5;
      }

      return var4;
   }

   private FlameParticles() {
   }

   public static final class Type_k {
      private final RandomGenerator v;
      private final ArrayList<FlameParticles.Type_n> u = new ArrayList<>();
      private double Q = Double.NaN;
      private double T;
      private float f;
      private float L;

      public Type_k(RandomGenerator var1) {
         this.v = var1;
      }

      public int fAsInt() {
         return this.u.size();
      }

      public void QAsVoid() {
         this.u.clear();
         this.Q = Double.NaN;
         this.T = 0.0;
      }

      public void SAsVoid(double var1, float var3, float var4, float var5, float var6, float var7, boolean var8, FlameParticles.Type_z var9) {
         if (!Double.isFinite(var1)) {
            this.QAsVoid();
         } else if (Double.isFinite(this.Q) && !(var1 < this.Q) && !(var1 - this.Q > 0.25)) {
            float var10 = (float)Math.clamp(var1 - this.Q, 0.0, 0.05);
            if (!(var10 <= 0.0F)) {
               this.Q = var1;
               float var11 = FlameParticles.LAsFloat(var3 - this.f);
               float var12 = var4 - this.L;
               this.f = var3;
               this.L = var4;
               float var13 = var9.inertia;
               float var14 = -var11 * var13 * 0.008F - var5 * var13 * 0.15F;
               float var15 = var12 * var13 * 0.008F - var6 * var13 * 0.15F + var9.buoyancy * 0.012F;
               float var16 = -var7 * var13 * 0.15F;
               float var17 = var10 * 60.0F;
               float var18 = (float)Math.pow(0.86, (double)var17);
               float var19 = var9.turbulence * 0.008F;

               for (int var20 = 0; var20 < this.u.size(); var20++) {
                  FlameParticles.Type_n var21 = this.u.get(var20);
                  if (var1 - var21.i >= (double)var21.h) {
                     this.u.remove(var20--);
                  } else {
                     var21.O = var21.O + var21.v * var17;
                     var21.c = var21.c + var21.r * var17;
                     var21.T = var21.T + var21.f * var17;
                     var21.v = (var21.v + (var14 + this.FAsFloat(-var19, var19)) * var17) * var18;
                     var21.r = (var21.r + (var15 + this.FAsFloat(-var19, var19) * 0.5F) * var17) * var18;
                     var21.f = (var21.f + (var16 + this.FAsFloat(-var19, var19)) * var17) * var18;
                     var21.d = var21.d + var21.t * var10;
                  }
               }

               this.T += (double)var10;
               double var26 = 1.0 / (double)var9.density;

               while (this.T + 1.0E-8 >= var26) {
                  this.T = Math.max(0.0, this.T - var26);
                  float var22 = this.v.nextFloat();
                  float var23 = var8 ? 0.45F : 0.15F;
                  float var24 = var8 ? 0.06F : 0.04F;
                  FlameParticles.Type_n var25 = new FlameParticles.Type_n();
                  var25.O = this.FAsFloat(-var24, var24);
                  var25.c = var22 * var23 + this.FAsFloat(-0.015F, 0.015F);
                  var25.T = -var22 * var23 * 0.35F + this.FAsFloat(-0.015F, 0.015F);
                  var25.v = this.FAsFloat(-0.001F, 0.001F);
                  var25.r = this.FAsFloat(0.003F, 0.008F);
                  var25.f = this.FAsFloat(-0.001F, 0.001F);
                  var25.D = var9.size * this.FAsFloat(0.7F, 1.3F) * (1.0F - var22 * 0.4F);
                  var25.h = Math.max(0.1F, var9.lifetime * this.FAsFloat(0.8F, 1.2F));
                  var25.i = var1;
                  var25.V = FlameParticles.EAsInt(var1, var9);
                  var25.d = this.FAsFloat(0.0F, 360.0F);
                  var25.t = this.FAsFloat(-90.0F, 90.0F);
                  if (this.u.size() >= 768) {
                     this.u.removeFirst();
                  }

                  this.u.add(var25);
               }
            }
         } else {
            this.QAsVoid();
            this.Q = var1;
            this.f = var3;
            this.L = var4;
         }
      }

      private float FAsFloat(float var1, float var2) {
         return var1 + (var2 - var1) * this.v.nextFloat();
      }

      public double[] RAsDoubleArray2(Matrix4f var1, FlameParticles.Type_z var2) {
         double[] var3 = new double[this.u.size() * 6];
         int var4 = 0;

         for (FlameParticles.Type_n var6 : this.u) {
            Vector3f var7 = new Vector3f(var6.O, var6.c, var6.T);
            var1.transformPosition(var7);
            float var8 = (float)Math.clamp((this.Q - var6.i) / (double)var6.h, 0.0, 1.0);
            int var9 = var2.colorMode.equals("Gradient") ? FlameParticles.AAsInt(var6.V, var2.secondary, var8) : var6.V;
            int var10 = Math.round((float)(var9 >>> 24) * (1.0F - var8));
            var3[var4++] = (double)var7.x;
            var3[var4++] = (double)var7.y;
            var3[var4++] = (double)var7.z;
            var3[var4++] = (double)var6.D;
            var3[var4++] = (double)((float)Math.toRadians((double)var6.d));
            var3[var4++] = (double)(var9 & 16777215 | var10 << 24);
         }

         return var3;
      }
   }

   private static final class Type_n {
      float O;
      float c;
      float T;
      float v;
      float r;
      float f;
      float D;
      float h;
      float d;
      float t;
      double i;
      int V;

      private Type_n() {
      }
   }

   private static final class Type_t {
      private static final RenderPipeline h = RenderPipeline.builder(new Snippet[]{RenderPipelines.POSITION_TEX_COLOR_SNIPPET})
         .withLocation(Identifier.of("larp-client", "flame_hands"))
         .withFragmentShader(Identifier.of("larp-client", "particle/sprite"))
         .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
         .withDepthWrite(false)
         .withBlend(BlendFunction.LIGHTNING)
         .withCull(false)
         .build();
      private static int k = -1;
      private static final RenderLayer H = GAsRenderLayer("bloom", FlameParticles.f);
      private static final RenderLayer y = GAsRenderLayer("glow", FlameParticles.P);

      private Type_t() {
      }

      private static RenderLayer GAsRenderLayer(String var0, Identifier var1) {
         return RenderLayer.of("larp_flame_hand_" + var0, RenderSetup.builder(h).texture("Sampler0", var1).build());
      }

      private static RenderLayer qAsRenderLayer(boolean var0) {
         return var0 ? y : H;
      }

      private static void uAsVoid() {
         if (k != BloomQuality.J) {
            WorldLabelLayout.DAsVoid(h);
            k = BloomQuality.J;
            ClientLog.XAsVoid("FlameHands", "Reference additive sprite material validated for resource generation {}", k);
         }
      }
   }

   public static record Type_z(
      float size, float density, float lifetime, float buoyancy, float turbulence, float inertia, int primary, int secondary, String colorMode
   ) {
   }
}
