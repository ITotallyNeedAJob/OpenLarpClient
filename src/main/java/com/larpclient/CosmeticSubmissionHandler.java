package com.larpclient;

import com.larpclient.module.SelfVisuals;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderSetup;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.RenderSetup.TextureSpec;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.command.RenderCommandQueue;
import net.minecraft.client.render.command.OrderedRenderCommandQueue.Custom;
import net.minecraft.client.render.entity.model.PlayerCapeModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.MatrixStack.Entry;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public final class CosmeticSubmissionHandler implements InvocationHandler {
   private static final Map<CosmeticSubmissionHandler.Type_t, RenderLayer> f = new LinkedHashMap<>();
   private static final Map<CosmeticSubmissionHandler.Type_p, RenderPipeline> b = new HashMap<>();
   private static final CosmeticSubmissionHandler x = new CosmeticSubmissionHandler();
   private final OrderedRenderCommandQueue O = (OrderedRenderCommandQueue)Proxy.newProxyInstance(
      OrderedRenderCommandQueue.class.getClassLoader(), new Class[]{OrderedRenderCommandQueue.class}, this
   );
   private final MatrixStack m = new MatrixStack();
   private final CameraRenderState v = new CameraRenderState();
   private EffectDrawBuffers H;
   private CosmeticSubmissionHandler.Type_n q;
   private int h;
   private double L;
   private double j;
   private double z;
   private boolean r;
   private boolean n;
   private boolean c;
   private SelfVisuals u;
   private int M = -1;
   private boolean t;
   private boolean l;
   private boolean P;
   private boolean R;
   private Entry p;
   private static final EnumSet<CosmeticSubmissionHandler.Type_n> F = EnumSet.range(
      CosmeticSubmissionHandler.Type_n.CHAMS_SOLID, CosmeticSubmissionHandler.Type_n.CHAMS_GHOST
   );

   public CosmeticSubmissionHandler() {
   }

   public static EntityRenderState oAsEntityRenderState(Entity var0, float var1) {
      return MinecraftClient.getInstance().getEntityRenderDispatcher().getAndUpdateRenderState(var0, var1);
   }

   public static boolean bAsBoolean() {
      return x.H != null || x.t;
   }

   public static Matrix4f dAsMatrix4f(EntityRenderState var0, MatrixStack var1) {
      Entry var2 = kAsMatrixStack_Entry(var0, var1, false);
      if (var2 == null && !x.P) {
         x.P = true;
         ClientLog.oAsVoid(
            "ChinaHat",
            "Native head attachment unavailable for this renderer; cone uses upright fallback and cap is skipped, retry after world/resource cleanup"
         );
      }

      return var2 == null ? null : new Matrix4f(var2.getPositionMatrix());
   }

   public static Entry JAsMatrixStack_Entry(EntityRenderState var0, MatrixStack var1) {
      Entry var2 = kAsMatrixStack_Entry(var0, var1, true);
      if (var2 == null && !x.R) {
         x.R = true;
         ClientLog.oAsVoid(
            "Wings",
            "Native body attachment unavailable for this renderer; Luminous uses its upright fallback and Ender Dragon is skipped, retry after world/resource cleanup"
         );
      }

      return var2;
   }

   private static Entry kAsMatrixStack_Entry(EntityRenderState var0, MatrixStack var1, boolean var2) {
      CosmeticSubmissionHandler var3 = x;
      MinecraftClient var4 = MinecraftClient.getInstance();
      Vec3d var5 = var4.gameRenderer.getCamera().getCameraPos();
      var3.v.pos = var5;
      var3.v.orientation.set(var4.gameRenderer.getCamera().getRotation());
      var3.m.peek().copy(var1.peek());
      var3.t = true;
      var3.l = var2;
      var3.p = null;

      Entry var6;
      try {
         var4.getEntityRenderDispatcher().render(var0, var3.v, var0.x - var5.x, var0.y - var5.y, var0.z - var5.z, var3.m, var3.O);
         var6 = var3.p;
      } finally {
         var3.t = false;
         var3.p = null;

         while (!var3.m.isEmpty()) {
            var3.m.pop();
         }
      }

      return var6;
   }

   public static void XAsVoid() {
      x.P = false;
      x.R = false;
      f.clear();
      b.clear();
   }

   public static void TAsVoid(ModelPart var0, MatrixStack var1, EffectDrawBuffers var2, Identifier var3, int var4, double var5, double var7, boolean var9) {
      if (x.M != BloomQuality.J) {
         XAsVoid();
         x.M = BloomQuality.J;
      }

      CosmeticSubmissionHandler.Type_n var10 = var7 > 0.0
         ? (var9 ? CosmeticSubmissionHandler.Type_n.DISSOLVE_GLOW : CosmeticSubmissionHandler.Type_n.DISSOLVE)
         : CosmeticSubmissionHandler.Type_n.ORIGINAL;
      if (!var9 || !(var7 <= 0.0)) {
         CosmeticSubmissionHandler.Type_r var11 = new CosmeticSubmissionHandler.Type_r(
            var2.XAsVertexConsumer(LAsRenderLayer(var3, var10, false)), var10, var4, var5, 0.0, 0.0, null
         );
         var11.s = (int)(Math.clamp(var7, 0.0, 1.0) * 4096.0);
         var0.render(var1, var11, 15728880, OverlayTexture.DEFAULT_UV);
      }
   }

   public static void AAsVoid(
      EntityRenderState var0, MatrixStack var1, EffectDrawBuffers var2, CosmeticSubmissionHandler.Type_n var3, int var4, double var5, boolean var7
   ) {
      xAsVoid(var0, var1, var2, var3, var4, var5, var7, true);
   }

   public static void xAsVoid(
      EntityRenderState var0,
      MatrixStack var1,
      EffectDrawBuffers var2,
      CosmeticSubmissionHandler.Type_n var3,
      int var4,
      double var5,
      boolean var7,
      boolean var8
   ) {
      x.fAsVoid(var0, var1, var2, var3, var4, var5, var7, var8);
   }

   private void fAsVoid(
      EntityRenderState var1,
      MatrixStack var2,
      EffectDrawBuffers var3,
      CosmeticSubmissionHandler.Type_n var4,
      int var5,
      double var6,
      boolean var8,
      boolean var9
   ) {
      if (this.M != BloomQuality.J) {
         XAsVoid();
         this.M = BloomQuality.J;
      }

      this.H = var3;
      this.q = var4;
      this.h = var5;
      this.L = var6;
      this.r = var8;
      this.c = var9;
      this.n = var4 == CosmeticSubmissionHandler.Type_n.MASK;
      this.u = var8 ? SelfVisuals.zAsSelfVisuals() : null;
      MinecraftClient var10 = MinecraftClient.getInstance();
      Vec3d var11 = var10.gameRenderer.getCamera().getCameraPos();
      this.j = var1.y - var11.y;
      this.z = var1.x - var11.x;
      this.v.pos = var11;
      this.v.orientation.set(var10.gameRenderer.getCamera().getRotation());
      this.m.peek().copy(var2.peek());

      try {
         var10.getEntityRenderDispatcher().render(var1, this.v, var1.x - var11.x, var1.y - var11.y, var1.z - var11.z, this.m, this.O);
      } catch (RuntimeException var16) {
         throw new IllegalStateException("ModelVisualRenderer stage=vanilla layer submission material=" + var4 + " entity=" + var1.entityType, var16);
      } finally {
         while (!this.m.isEmpty()) {
            this.m.pop();
         }

         this.H = null;
         this.u = null;
      }
   }

   @Override
   public Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
      if (var2.isDefault()) {
         return InvocationHandler.invokeDefault(var1, var2, var3);
      } else {
         String var4 = iAsString(var2);
         if (this.t && var2.getDeclaringClass() != Object.class && !var4.equals("submitModel") && !var4.equals("order")) {
            return null;
         } else {
            switch (var4) {
               case "order":
                  return this.O;
               case "submitModel":
                  Model var18 = (Model)var3[0];
                  Object var20 = var3[1];
                  MatrixStack var21 = (MatrixStack)var3[2];
                  if (this.t) {
                     if (this.p == null && var18 instanceof PlayerEntityModel var23) {
                        var18.setAngles(var20);
                        var21.push();

                        try {
                           (this.l ? var23.body : var23.head).applyTransform(var21);
                           this.p = var21.peek().copy();
                        } finally {
                           var21.pop();
                        }
                     }

                     return null;
                  } else {
                     RenderLayer var22 = (RenderLayer)var3[3];
                     boolean var24 = var18 instanceof PlayerEntityModel;
                     if (this.r && var18 instanceof PlayerCapeModel) {
                        if (EAsBoolean(this.q) || this.u == null || !this.u.KAsBoolean()) {
                           return null;
                        }
                     } else if (this.r
                        && !this.n
                        && !var24
                        && (this.u == null || (EAsBoolean(this.q) ? !EntityMaterialRenderer.kAsBoolean() : !this.u.affectArmor.getValue()))) {
                        return null;
                     }

                     VertexConsumer var25 = this.sAsVertexConsumer(var22);
                     if (var25 == null) {
                        return null;
                     }

                     if (var3[7] instanceof Sprite var26) {
                        var25 = var26.getTextureSpecificVertexConsumer(var25);
                     }

                     var18.setAngles(var20);
                     var18.render(var21, var25, (Integer)var3[4], (Integer)var3[5], (Integer)var3[6]);
                     return null;
                  }
               case "submitItem":
                  if (!this.r || this.n || this.u != null && !this.u.YAsBoolean() && this.u.affectHeldItems.getValue()) {
                     MatrixStack var17 = (MatrixStack)var3[0];
                     int[] var19 = (int[])var3[5];
                     RenderLayer var9 = (RenderLayer)var3[7];
                     TextureSpec var10 = (TextureSpec)var9.renderSetup.textures.get("Sampler0");
                     if (var10 == null) {
                        return null;
                     }

                     VertexConsumer var11 = this.KAsVertexConsumer(var10.location());

                     for (BakedQuad var13 : (Iterable<BakedQuad>) ((List)var3[6])) {
                        int var14 = var13.tintIndex();
                        iAsVoid(var11, var17.peek(), var13, var14 >= 0 && var14 < var19.length ? var19[var14] : -1, (Integer)var3[2], (Integer)var3[3]);
                     }

                     return null;
                  }

                  return null;
               case "submitCustomGeometry":
                  if (!this.r || this.n || this.u != null && !this.u.YAsBoolean() && this.u.affectHeldItems.getValue()) {
                     RenderLayer var7 = (RenderLayer)var3[1];
                     VertexConsumer var8 = this.sAsVertexConsumer(var7);
                     if (var8 != null) {
                        ((Custom)var3[2]).render(((MatrixStack)var3[0]).peek(), var8);
                     }

                     return null;
                  }

                  return null;
               case "toString":
                  return "Larp visual model collector";
               case "hashCode":
                  return System.identityHashCode(this);
               case "equals":
                  return var1 == var3[0];
               default:
                  return null;
            }
         }
      }
   }

   private static String iAsString(Method var0) {
      if (var0.getDeclaringClass() == Object.class) {
         return var0.getName();
      } else {
         Class[] var1 = var0.getParameterTypes();
         if (var1.length == 1 && var1[0] == int.class && var0.getReturnType() == RenderCommandQueue.class) {
            return "order";
         } else if (var1.length == 10 && var1[0] == Model.class) {
            return "submitModel";
         } else if (var1.length == 9 && var1[0] == MatrixStack.class && var1[1] == ItemDisplayContext.class) {
            return "submitItem";
         } else {
            return var1.length == 3 && var1[0] == MatrixStack.class && var1[1] == RenderLayer.class && var1[2] == Custom.class ? "submitCustomGeometry" : "";
         }
      }
   }

   private static void iAsVoid(VertexConsumer var0, Entry var1, BakedQuad var2, int var3, int var4, int var5) {
      var0.quad(
         var1,
         var2,
         (float)(var3 >> 16 & 0xFF) / 255.0F,
         (float)(var3 >> 8 & 0xFF) / 255.0F,
         (float)(var3 & 0xFF) / 255.0F,
         (float)(var3 >>> 24 & 0xFF) / 255.0F,
         var4,
         var5
      );
   }

   public static OrderedRenderCommandQueue sAsOrderedRenderCommandQueue(OrderedRenderCommandQueue var0) {
      SelfVisuals var1 = SelfVisuals.zAsSelfVisuals();
      if (var1 != null && var1.isOperational() && var1.VAsBoolean() && !var1.YAsBoolean() && var1.affectHeldItems.getValue()) {
         CosmeticSubmissionHandler.Type_n var2 = var1.rAsO0_Type_n();
         return var2 == null ? var0 : (OrderedRenderCommandQueue)oAsObject(var0, var1, var2, true);
      } else {
         return var0;
      }
   }

   private static Object oAsObject(RenderCommandQueue var0, SelfVisuals var1, CosmeticSubmissionHandler.Type_n var2, boolean var3) {
      Class<?> var4 = var3 ? OrderedRenderCommandQueue.class : RenderCommandQueue.class;
      return Proxy.newProxyInstance(var4.getClassLoader(), new Class[]{var4}, (var3x, var4x, var5) -> {
         if (var4x.isDefault()) {
            return InvocationHandler.invokeDefault(var3x, var4x, var5);
         } else {
            String var6 = iAsString(var4x);
            if (var6.equals("order")) {
               return oAsObject(((OrderedRenderCommandQueue)var0).getBatchingQueue((Integer)var5[0]), var1, var2, false);
            } else if (!var6.equals("submitItem")) {
               try {
                  return var4x.invoke(var0, var5);
               } catch (InvocationTargetException var22) {
                  throw var22.getCause();
               }
            } else {
               MatrixStack var7 = (MatrixStack)var5[0];
               int var8 = (Integer)var5[2];
               int var9 = (Integer)var5[3];
               int[] var10 = (int[])((int[])var5[5]).clone();
               List var11 = List.copyOf((List)var5[6]);
               LinkedHashMap<Object, ArrayList<Object>> var12 = new LinkedHashMap<>();

               for (BakedQuad var14 : (Iterable<BakedQuad>) (var11)) {
                  TextureSpec var15 = (TextureSpec)((RenderLayer)var5[7]).renderSetup.textures.get("Sampler0");
                  if (var15 == null) {
                     return var4x.invoke(var0, var5);
                  }

                  var12.computeIfAbsent(var15.location(), var0xx -> new ArrayList<>()).add(var14);
               }

               double var23 = (double)var7.peek().getPositionMatrix().m30();
               double var24 = (double)var7.peek().getPositionMatrix().m31();
               double var17 = var1.modelOpacity.getValue();

               for (java.util.Map.Entry var20 : var12.entrySet()) {
                  List var21 = List.copyOf((Collection)var20.getValue());
                  var0.submitCustom(var7, LAsRenderLayer((Identifier)var20.getKey(), var2, true), (var12x, var13) -> {
                     CosmeticSubmissionHandler.Type_r var14x = new CosmeticSubmissionHandler.Type_r(var13, var2, bd.w, var17, var23, var24, var1);

                     for (BakedQuad var16 : (Iterable<BakedQuad>) (var21)) {
                        int var17x = var16.tintIndex();
                        iAsVoid(var14x, var12x, var16, var17x >= 0 && var17x < var10.length ? var10[var17x] : -1, var8, var9);
                     }
                  });
               }

               return null;
            }
         }
      });
   }

   public static boolean TAsBoolean(ModelPart var0, MatrixStack var1, OrderedRenderCommandQueue var2, RenderLayer var3, int var4, int var5, Sprite var6) {
      SelfVisuals var7 = SelfVisuals.zAsSelfVisuals();
      if (var7 != null
         && var7.isOperational()
         && var7.VAsBoolean()
         && var7.firstPersonArms.getValue()
         && (!var7.YAsBoolean() || EntityMaterialRenderer.YAsBoolean())) {
         TextureSpec var8 = (TextureSpec)var3.renderSetup.textures.get("Sampler0");
         if (var8 == null) {
            return false;
         } else {
            CosmeticSubmissionHandler.Type_n var9 = var7.rAsO0_Type_n() == null ? CosmeticSubmissionHandler.Type_n.TINT : var7.rAsO0_Type_n();
            double var10 = (double)var1.peek().getPositionMatrix().m31();
            double var12 = (double)var1.peek().getPositionMatrix().m30();
            var2.submitCustom(var1, LAsRenderLayer(var8.location(), var9, true), (var10xx, var11) -> {
               long var12x = op.RAsLong();
               MatrixStack var14 = new MatrixStack();
               var14.peek().copy(var10xx);
               VertexConsumer var15 = new CosmeticSubmissionHandler.Type_r(var11, var9, bd.w, var7.hAsDouble(), var12, var10, var7);
               if (var6 != null) {
                  var15 = var6.getTextureSpecificVertexConsumer(var15);
               }

               try {
                  var0.render(var14, var15, var4, var5);
               } catch (RuntimeException var20) {
                  ClientLog.hAsVoid("SelfVisuals", "First-person arm stage=geometry shader=model/material failed; restoring vanilla on next frame", var20);
                  if (var7.YAsBoolean()) {
                     EntityMaterialRenderer.sAsVoid(var20);
                  } else {
                     var7.disable();
                  }
               } finally {
                  op.aAsVoid("SelfVisuals", var12x);
               }
            });
            return true;
         }
      } else {
         return false;
      }
   }

   public static void fAsVoid() {
      LAsRenderLayer(Identifier.of("minecraft", "textures/entity/player/wide/steve.png"), CosmeticSubmissionHandler.Type_n.GLASS, true);
   }

   public static boolean EAsBoolean(CosmeticSubmissionHandler.Type_n var0) {
      return var0 == CosmeticSubmissionHandler.Type_n.GLASS
         || var0 == CosmeticSubmissionHandler.Type_n.GLASS_MASK
         || var0 == CosmeticSubmissionHandler.Type_n.GLASS_GLOW;
   }

   public static boolean NAsBoolean(CosmeticSubmissionHandler.Type_n var0) {
      return F.contains(var0);
   }

   private VertexConsumer KAsVertexConsumer(Identifier var1) {
      return new CosmeticSubmissionHandler.Type_r(
         this.H
            .XAsVertexConsumer(
               LAsRenderLayer(
                  var1,
                  this.q,
                  this.q == CosmeticSubmissionHandler.Type_n.CHAMS_SURFACE
                     || this.r && !this.n && this.q != CosmeticSubmissionHandler.Type_n.GLASS_MASK && this.q != CosmeticSubmissionHandler.Type_n.GLASS_GLOW
               )
            ),
         this.q,
         this.h,
         this.L,
         this.z,
         this.j,
         this.u
      );
   }

   static boolean BAsBoolean(CosmeticSubmissionHandler.Type_n var0, boolean var1, boolean var2) {
      return var1
         && var2
         && (
            var0 == CosmeticSubmissionHandler.Type_n.RAINBOW
               || var0 == CosmeticSubmissionHandler.Type_n.HOLOGRAM
               || var0 == CosmeticSubmissionHandler.Type_n.TINT
               || var0 == CosmeticSubmissionHandler.Type_n.ORIGINAL
               || var0 == CosmeticSubmissionHandler.Type_n.GLASS
               || NAsBoolean(var0) && var0 != CosmeticSubmissionHandler.Type_n.CHAMS_GHOST
         );
   }

   private VertexConsumer sAsVertexConsumer(RenderLayer var1) {
      if (var1.getRenderPipeline() == RenderPipelines.GLINT) {
         return BAsBoolean(this.q, this.r, this.c) ? this.H.XAsVertexConsumer(var1) : null;
      } else {
         TextureSpec var2 = (TextureSpec)var1.renderSetup.textures.get("Sampler0");
         return var2 == null ? null : this.KAsVertexConsumer(var2.location());
      }
   }

   private static RenderLayer LAsRenderLayer(Identifier var0, CosmeticSubmissionHandler.Type_n var1, boolean var2) {
      CosmeticSubmissionHandler.Type_t var3 = new CosmeticSubmissionHandler.Type_t(var0, var1, var2);
      RenderLayer var4 = f.get(var3);
      if (var4 != null) {
         return var4;
      } else {
         if (f.size() >= 256) {
            f.clear();
         }

         CosmeticSubmissionHandler.Type_p var5 = new CosmeticSubmissionHandler.Type_p(var1, var2);
         RenderPipeline var6 = b.get(var5);
         if (var6 == null) {
            Builder var7 = RenderPipeline.builder(new Snippet[]{RenderPipelines.POSITION_TEX_COLOR_SNIPPET})
               .withLocation(Identifier.of("larp-client", "model_" + var1.name().toLowerCase(Locale.ROOT) + (var2 ? "_solid" : "_transparent")))
               .withFragmentShader(Identifier.of("larp-client", "model/material"))
               .withVertexShader(Identifier.of("larp-client", "model/material"))
               .withVertexFormat(VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, DrawMode.QUADS)
               .withDepthTestFunction(
                  var1 == CosmeticSubmissionHandler.Type_n.CHAMS_HIDDEN ? DepthTestFunction.GREATER_DEPTH_TEST : DepthTestFunction.LEQUAL_DEPTH_TEST
               )
               .withDepthWrite(var2)
               .withCull(var1 != CosmeticSubmissionHandler.Type_n.MASK && var1 != CosmeticSubmissionHandler.Type_n.CHAMS_SURFACE);
            if (var1 == CosmeticSubmissionHandler.Type_n.CHAMS_SURFACE) {
               var7.withoutBlend();
               var7.withShaderDefine("CHAMS_SURFACE");
            }

            if (EAsBoolean(var1)) {
               var7.withShaderDefine("LIQUID_GLASS");
            }

            if (var1 == CosmeticSubmissionHandler.Type_n.GLASS_MASK) {
               var7.withShaderDefine("GLASS_MASK");
            }

            if (var1 == CosmeticSubmissionHandler.Type_n.GLASS_GLOW) {
               var7.withShaderDefine("GLASS_GLOW");
            }

            if (var1 == CosmeticSubmissionHandler.Type_n.GLASS_MASK) {
               var7.withoutBlend();
            }

            if (var1 == CosmeticSubmissionHandler.Type_n.RAINBOW) {
               var7.withShaderDefine("RAINBOW");
            }

            if (var1 == CosmeticSubmissionHandler.Type_n.MASK
               || var1 == CosmeticSubmissionHandler.Type_n.SILHOUETTE
               || var1 == CosmeticSubmissionHandler.Type_n.CHAMS_VISIBLE
               || var1 == CosmeticSubmissionHandler.Type_n.CHAMS_HIDDEN) {
               var7.withShaderDefine("SILHOUETTE");
            }

            if (var1 == CosmeticSubmissionHandler.Type_n.HOLOGRAM) {
               var7.withShaderDefine("HOLOGRAM");
            }

            if (var1 == CosmeticSubmissionHandler.Type_n.MASK) {
               var7.withShaderDefine("DEPTH_BIAS");
            }

            if (NAsBoolean(var1)) {
               var7.withShaderDefine("CHAMS_STYLE");
               var7.withShaderDefine("STYLE_" + var1.name().substring(6));
            }

            if (var1 == CosmeticSubmissionHandler.Type_n.CHAMS_VISIBLE || var1 == CosmeticSubmissionHandler.Type_n.CHAMS_HIDDEN) {
               var7.withShaderDefine("CHAMS");
            }

            if (var1 == CosmeticSubmissionHandler.Type_n.DISSOLVE || var1 == CosmeticSubmissionHandler.Type_n.DISSOLVE_GLOW) {
               var7.withShaderDefine("DISSOLVE");
            }

            if (var1 == CosmeticSubmissionHandler.Type_n.DISSOLVE_GLOW) {
               var7.withShaderDefine("DISSOLVE_GLOW");
            }

            var6 = var7.build();
            WorldLabelLayout.DAsVoid(var6);
            b.put(var5, var6);
            ClientLog.GAsVoid("Shader", "Player material initialized: {} shader=model/material", var1);
         }

         RenderLayer var8 = RenderLayer.of("larp_model_" + var1, RenderSetup.builder(var6).texture("Sampler0", var0).translucent().build());
         f.put(var3, var8);
         return var8;
      }
   }

   public static enum Type_n {
      ORIGINAL,
      TINT,
      HOLOGRAM,
      SILHOUETTE,
      RAINBOW,
      MASK,
      CHAMS_VISIBLE,
      CHAMS_HIDDEN,
      DISSOLVE,
      DISSOLVE_GLOW,
      GLASS,
      GLASS_MASK,
      GLASS_GLOW,
      CHAMS_SURFACE,
      CHAMS_SOLID,
      CHAMS_GLOW,
      CHAMS_METALLIC,
      CHAMS_CAMO,
      CHAMS_MARBLE,
      CHAMS_GLITTER,
      CHAMS_GHOST;

      private Type_n() {
      }
   }

   private static record Type_p(CosmeticSubmissionHandler.Type_n material, boolean depthWrite) {
   }

   static final class Type_r implements VertexConsumer {
      private final VertexConsumer d;
      private final CosmeticSubmissionHandler.Type_n B;
      private final int H;
      private final double X;
      private final double D;
      private final double p;
      private final boolean m;
      private final boolean h;
      private final boolean o;
      private final boolean P;
      private final boolean W;
      private final int v;
      private final int A;
      private final int O;
      private final int l;
      private final int Q;
      private final double y;
      private final double x;
      private final double K;
      private final int c;
      private float T;
      private float M;
      private int s;
      private int k = -1;

      Type_r(VertexConsumer var1, CosmeticSubmissionHandler.Type_n var2, int var3, double var4, double var6, double var8, SelfVisuals var10) {
         this.d = var1;
         this.B = var2;
         this.H = var3;
         this.X = var4;
         this.D = var6;
         this.p = var8;
         this.P = CosmeticSubmissionHandler.EAsBoolean(var2) && var10 != null;
         this.v = EntityMaterialRenderer.IAsInt();
         this.A = EntityMaterialRenderer.YAsInt();
         this.y = this.P ? EntityMaterialRenderer.qAsDouble(var10) : 0.0;
         this.W = CosmeticSubmissionHandler.NAsBoolean(var2) && var10 != null;
         int var11 = this.W ? var10.gAsInt() : 0;
         this.O = var11 >> 8 & 65535;
         this.l = this.W ? var11 & 0xFF | (int)Math.round(var10.shading.getValue() * 255.0) << 8 : 0;
         this.Q = this.W ? (int)Math.round(var10.patternScale.getValue() * 256.0) : 0;
         this.m = var2 == CosmeticSubmissionHandler.Type_n.RAINBOW && var10 != null;
         this.h = this.m && var10.direction.pAsBoolean("Horizontal");
         this.x = this.m ? var10.gradientScale.getValue() : 0.0;
         this.K = this.W
            ? mb.RAsDouble() * var10.patternSpeed.getValue() % 1.0
            : (
               this.m && !var10.direction.pAsBoolean("Static")
                  ? mb.RAsDouble() * var10.rainbowSpeed.getValue() * (double)(var10.direction.pAsBoolean("Upward") ? -1 : 1) % 1.0
                  : 0.0
            );
         this.o = this.m
            || this.P
            || this.W
            || var2 == CosmeticSubmissionHandler.Type_n.MASK
            || var2 == CosmeticSubmissionHandler.Type_n.SILHOUETTE
            || var2 == CosmeticSubmissionHandler.Type_n.CHAMS_VISIBLE
            || var2 == CosmeticSubmissionHandler.Type_n.CHAMS_SURFACE
            || var2 == CosmeticSubmissionHandler.Type_n.CHAMS_HIDDEN;
         int var12 = this.m
            ? 0xFF000000 | (int)(var10.saturation.getValue() * 255.0) << 16 | (int)(var10.brightness.getValue() * 255.0) << 8
            : (this.P ? EntityMaterialRenderer.rAsInt(var10, 1.0) : (this.W ? var10.pAsInt() : var3));
         this.c = bg.pAsInt(var12, var4);
      }

      public VertexConsumer vertex(float x, float y, float z) {
         this.T = x;
         this.M = y;
         if (this.P) {
            this.k++;
         }

         this.d.vertex(x, y, z);
         return this;
      }

      public VertexConsumer color(int red, int green, int blue, int alpha) {
         return this.color(alpha << 24 | red << 16 | green << 8 | blue);
      }

      public VertexConsumer color(int argb) {
         int var2 = this.s;
         if (this.P) {
            var2 = (int)(this.y * 4096.0);
         } else if (this.W) {
            var2 = (int)(this.K * 4096.0);
         } else if (this.m) {
            double var3 = this.h ? (double)this.T - this.D : (double)this.M - this.p;
            double var5 = var3 * this.x + this.K;
            var2 = Math.clamp(Math.round(var5 * 4096.0), -32768, 32767);
         }

         int var7 = this.k & 3;
         this.d.light(var2, this.P ? (var7 == 2 ? 3 : (var7 == 3 ? 2 : var7)) : (this.W ? this.Q : 0));
         this.d.color(this.o ? this.c : bg.pAsInt(this.B == CosmeticSubmissionHandler.Type_n.ORIGINAL ? argb : bg.xAsInt(argb, this.H, 0.85), this.X));
         return this;
      }

      public VertexConsumer texture(float u, float v) {
         this.d.texture(u, v);
         return this;
      }

      public VertexConsumer overlay(int u, int v) {
         this.d.overlay(this.P ? this.v : (this.W ? this.O : u), this.P ? this.A : (this.W ? this.l : v));
         return this;
      }

      public VertexConsumer light(int u, int v) {
         return this;
      }

      public VertexConsumer normal(float x, float y, float z) {
         this.d.normal(x, y, z);
         return this;
      }

      public VertexConsumer lineWidth(float width) {
         return this;
      }
   }

   private static record Type_t(Identifier texture, CosmeticSubmissionHandler.Type_n material, boolean depthWrite) {
   }
}
