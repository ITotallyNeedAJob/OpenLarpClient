package com.larpclient;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.platform.DepthTestFunction;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextDrawable;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.font.TextDrawable.DrawnGlyphRect;
import net.minecraft.client.font.TextRenderer.GlyphDrawable;
import net.minecraft.client.font.TextRenderer.GlyphDrawer;
import net.minecraft.client.font.TextRenderer.TextLayerType;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;

public final class WorldTextRenderer implements AutoCloseable, GlyphDrawer {
   private static final double TextLayout = 0.55;
   private static final int V = 15728880;
   private static final int C = 256;
   private static final Comparator<WorldTextRenderer.Type_k> B = Comparator.<WorldTextRenderer.Type_k>comparingDouble(var0 -> var0.K).reversed();
   private final WorldTextRenderer.Type_k[] AuraModule = new WorldTextRenderer.Type_k[256];
   private final WorldTextRenderer.Type_k[] BoxUtils = new WorldTextRenderer.Type_k[256];
   private int R;
   private static final double PriceDetector = 0.01890625;
   private final LinkedHashMap<String, WorldTextRenderer.Type_o> W = new LinkedHashMap<>(64, 0.75F, true);
   private final Matrix4f X = new Matrix4f();
   private final Matrix4f SmoothInput = new Matrix4f();
   private WorldTextRenderer.Type_c K;
   private int p;
   private int v;
   private float H;
   private boolean d;
   private final Quaternionf z = new Quaternionf();
   private final Vector3f A = new Vector3f();
   private final TransformingVertexConsumer U = new TransformingVertexConsumer();
   private final dv[] n = new dv[96];
   private final double[] MenuPopupProvider = new double[96];
   private final RenderPipeline[][] P = new RenderPipeline[3][2];
   private EffectDrawBuffers L;
   private TextRenderer a;
   private int SkyChunkCache = -1;
   private int T = -1;
   private int e;
   private boolean i;
   private int l;

   public WorldTextRenderer() {
   }

   public void TAsVoid(r_ var1, CameraRenderState var2) {
      this.l = this.GAsInt(var1, var2);
   }

   public void nAsVoid(List<WorldTextRenderer.Type_d> var1, CameraRenderState var2) {
      this.AAsVoid();
      if (!this.i || this.T != BloomQuality.J) {
         try {
            this.UAsVoid();
            this.vAsVoid(var1, var2);
         } catch (RuntimeException var4) {
            this.ZAsVoid(var4);
         }
      }
   }

   private void vAsVoid(List<WorldTextRenderer.Type_d> var1, CameraRenderState var2) {
      for (WorldTextRenderer.Type_d var4 : var1) {
         if (this.R == 256) {
            break;
         }

         if (var4 != null
            && var4.text() != null
            && !var4.text().isBlank()
            && Double.isFinite(var4.x())
            && Double.isFinite(var4.y())
            && Double.isFinite(var4.z())
            && Double.isFinite(var4.scale())
            && !(var4.scale() <= 0.0)
            && var4.color() >>> 24 != 0
            && (
               !var4.worldAligned()
                  || Float.isFinite(var4.qx())
                     && Float.isFinite(var4.qy())
                     && Float.isFinite(var4.qz())
                     && Float.isFinite(var4.qw())
                     && !(var4.qx() * var4.qx() + var4.qy() * var4.qy() + var4.qz() * var4.qz() + var4.qw() * var4.qw() < 1.0E-4F)
            )) {
            WorldTextRenderer.Type_o var5 = this.LAsR9_Type_o(var4.text());
            WorldTextRenderer.Type_k var6 = this.x[this.R];
            if (var6 == null) {
               this.x[this.R] = var6 = new WorldTextRenderer.Type_k();
            }

            var6.T = var4;
            var6.t = var5;
            double var7 = var4.x() - var2.pos.x;
            double var9 = var4.y() - var2.pos.y;
            double var11 = var4.z() - var2.pos.z;
            var6.K = var7 * var7 + var9 * var9 + var11 * var11;
            this.c[this.R++] = var6;
         }
      }

      Arrays.sort(this.c, 0, this.R, B);
   }

   private void AAsVoid() {
      for (int var1 = 0; var1 < this.R; var1++) {
         this.c[var1].T = null;
         this.c[var1].t = null;
         this.c[var1] = null;
      }

      this.R = 0;
   }

   public void IAsVoid(MatrixStack var1, CameraRenderState var2, boolean var3) {
      this.CAsVoid(var1, var2, var3, true, 1.0);
   }

   public void CAsVoid(MatrixStack var1, CameraRenderState var2, boolean var3, boolean var4, double var5) {
      if (this.R != 0 && !this.i && this.L != null) {
         try {
            this.e = var3 ? 1 : 0;
            this.A.set(0.0F, 0.0F, -1.0F).rotate(var2.orientation);

            for (int var7 = 0; var7 < this.R; var7++) {
               WorldTextRenderer.Type_k var8 = this.c[var7];
               WorldTextRenderer.Type_d var9 = var8.T;
               WorldTextRenderer.Type_o var10 = var8.t;
               double var11 = var9.x() - var2.pos.x;
               double var13 = var9.y() - var2.pos.y;
               double var15 = var9.z() - var2.pos.z;
               double var17 = Math.hypot((double)(var10.width + 12), (double)(var10.height + 8)) * 0.01890625 * var9.scale();
               if (!(var11 * (double)this.A.x + var13 * (double)this.A.y + var15 * (double)this.A.z < -var17)) {
                  this.X.set(var1.peek().getPositionMatrix()).translate((float)var11, (float)var13, (float)var15);
                  this.mAsVoid(var9, var2.orientation);
                  float var19 = (float)(0.01890625 * Math.min(32.0, var9.scale()));
                  this.X.scale(var19, -var19, var19).translate(0.0F, -var10.centerY, 0.0F);
                  this.K = var9.motion();
                  this.v = var10.width;
                  this.H = (float)var10.height / 9.0F;
                  if (var4) {
                     this.X.translate(1.0F, 1.0F, -0.03F);
                     this.U.o = this.p = bg.XAsInt(-15724520, (double)(var9.color() >>> 24) / 255.0 * 0.65 * var5);
                     this.d = true;
                     var10.text.draw(this);
                     this.X.translate(-1.0F, -1.0F, 0.03F);
                  }

                  this.U.o = this.p = bg.XAsInt(var9.color(), (double)(var9.color() >>> 24) / 255.0 * var5);
                  this.d = false;
                  var10.text.draw(this);
               }
            }

            this.L.NAsVoid(this::jAsPw);
         } catch (RuntimeException var231) {
            this.ZAsVoid(var231);
         } finally {
            this.U.U = null;
            this.K = null;
         }
      }
   }

   private boolean xAsBoolean(TextDrawable var1) {
      WorldTextRenderer.Type_c var2 = this.K;
      float var3 = (var1.getEffectiveMinX() + var1.getEffectiveMaxX()) * 0.5F;
      float var4 = (var1.getEffectiveMinY() + var1.getEffectiveMaxY()) * 0.5F;
      double var5 = this.v <= 0 ? 0.0 : Math.clamp(((double)var3 + (double)this.v * 0.5) / (double)this.v, 0.0, 1.0);
      double var7 = 1.0;
      double var9 = 0.0;
      double var11 = 0.0;
      double var13 = 0.0;
      double var15 = 0.0;
      double var17 = 1.0;
      double var19 = (double)this.H;
      if (var2.kind() != 0) {
         var7 = Math.clamp((var2.reveal() - var5 * 0.55) / 0.44999999999999996, 0.0, 1.0);
         var9 = Math.clamp((var2.exit() - var5 * 0.4) / 0.6, 0.0, 1.0);
         double var21 = 1.0 - Math.pow(1.0 - var7, 3.0);
         if (var2.kind() == 1) {
            double var23 = 1.0 + 2.2 * Math.pow(var7 - 1.0, 3.0) + 1.2 * Math.pow(var7 - 1.0, 2.0);
            var13 = var19 * (7.0 * (1.0 - var21) + 0.7 * Math.sin(var2.time() * 2.4 - var5 * 6.0) * var21 - 10.0 * var9 * var9);
            var11 = var19 * (var5 - 0.5) * 8.0 * var9 * var9;
            var15 = (var5 - 0.5) * 0.6 * var9;
            var17 = (0.4 + 0.6 * var23) * (1.0 - 0.3 * var9);
         } else {
            var11 = var19 * -6.0 * (1.0 - var21);
            var13 = var19 * (1.4 * Math.sin(var2.time() * 3.0 - var5 * 7.0) * var21 + 7.0 * var9 * var9);
         }
      }

      double var26 = a1.OAsDouble(var7) * (1.0 - a1.OAsDouble(var9));
      if (var26 < 0.00392156862745098) {
         return false;
      } else {
         int var27 = this.p;
         if (!this.d && var2.accent() >>> 24 != 0) {
            double var24 = 0.5 - 0.5 * Math.cos(var5 * 2.4 - var2.time() * 1.2);
            var27 = bg.xAsInt(var27, var2.accent() & 16777215 | var27 & 0xFF000000, var24 * 0.85);
         }

         this.U.o = bg.pAsInt(var27, var26);
         this.t.set(this.X).translate(var3 + (float)var11, var4 + (float)var13, 0.0F).rotateZ((float)var15).scale((float)var17).translate(-var3, -var4, 0.0F);
         return true;
      }
   }

   private void mAsVoid(WorldTextRenderer.Type_d var1, Quaternionfc var2) {
      this.X.rotate((Quaternionfc)(var1.worldAligned() ? this.z.set(var1.qx(), var1.qy(), var1.qz(), var1.qw()).normalize() : var2));
   }

   private void ZAsVoid(RuntimeException var1) {
      this.i = true;
      this.AAsVoid();
      this.W.clear();
      if (this.L != null) {
         this.L.close();
         this.L = null;
      }

      ClientLog.lAsVoid("WorldText", "World label stage failed; isolated until resource reload or re-enable", var1);
   }

   private void UAsVoid() {
      TextRenderer var1 = MinecraftClient.getInstance().textRenderer;
      if (this.a != var1 || this.s != TextDrawing.lAsInt()) {
         this.W.clear();
         this.a = var1;
         this.s = TextDrawing.lAsInt();
      }

      if (this.T != BloomQuality.J) {
         this.W.clear();
         this.i = false;
         this.T = BloomQuality.J;

         for (int var2 = 0; var2 < 3; var2++) {
            for (int var3 = 0; var3 < 2; var3++) {
               Builder var4 = WorldLabelLayout.GAsRenderPipeline_Builder(
                     var3 == 1 ? RenderPipelines.RENDERTYPE_TEXT_INTENSITY : RenderPipelines.RENDERTYPE_TEXT
                  )
                  .withLocation(Identifier.of("larp-client", "world_text_" + var2 + "_" + var3))
                  .withVertexShader(RenderPipelines.RENDERTYPE_TEXT.getVertexShader())
                  .withFragmentShader(Identifier.of("larp-client", "world_text"))
                  .withCull(false)
                  .withDepthBias(0.0F, 0.0F)
                  .withDepthTestFunction(
                     var2 == 1 ? DepthTestFunction.NO_DEPTH_TEST : (var2 == 2 ? DepthTestFunction.GREATER_DEPTH_TEST : DepthTestFunction.LEQUAL_DEPTH_TEST)
                  )
                  .withDepthWrite(false);
               if (var3 == 1) {
                  var4.withShaderDefine("IS_GRAYSCALE");
               }

               this.P[var2][var3] = var4.build();
               WorldLabelLayout.DAsVoid(this.P[var2][var3]);
            }
         }

         ClientLog.GAsVoid("WorldText", "World glyph pipelines ready: normal, through walls, soft occlusion");
      }

      if (this.L == null) {
         this.L = new EffectDrawBuffers();
      }
   }

   private WorldTextRenderer.Type_o LAsR9_Type_o(String var1) {
      WorldTextRenderer.Type_o var2 = this.W.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         Text var3 = TextDrawing.UAsText(var1);
         int var4 = this.a.getWidth(var3);
         GlyphDrawable var5 = this.a.prepare(var3.asOrderedText(), (float)(-var4) / 2.0F, 0.0F, -1, false, false, 0);
         ScreenRect var6 = var5.getScreenRect();
         WorldTextRenderer.Type_o var7 = new WorldTextRenderer.Type_o(
            var5, var4, var6 == null ? 32 : var6.height(), var6 == null ? 16.0F : (float)var6.getTop() + (float)var6.height() / 2.0F
         );
         if (this.W.size() >= 256) {
            this.W.remove(this.W.keySet().iterator().next());
         }

         this.W.put(var1, var7);
         return var7;
      }
   }

   public boolean TAsBoolean() {
      return !this.i;
   }

   public int mAsInt(String var1) {
      if (var1.isEmpty()) {
         return 0;
      } else {
         this.UAsVoid();
         return this.LAsR9_Type_o(var1).width;
      }
   }

   public void jAsVoid(MatrixStack var1, CameraRenderState var2, r_ var3, d1 var4, boolean var5) {
      if ((!this.i || this.T != BloomQuality.J) && (!var5 || var4.H && !(var4.A <= 0.0))) {
         long var6 = op.RAsLong();

         try {
            this.UAsVoid();
            int var8 = this.l;
            if (var4.a == d1.Type_f.SOFT_OCCLUSION) {
               this.e = 2;
               this.oAsVoid(var1, var2, var3, var4, var5, var8, 0.18);
            }

            this.e = var4.a == d1.Type_f.THROUGH_WALLS ? 1 : 0;
            this.oAsVoid(var1, var2, var3, var4, var5, var8, 1.0);
         } catch (RuntimeException var121) {
            this.i = true;
            if (this.L != null) {
               this.L.close();
               this.L = null;
            }

            this.W.clear();
            ClientLog.lAsVoid("WorldText", "World text render failed; isolated until resource reload or re-enable", var121);
         } finally {
            this.U.U = null;
            op.aAsVoid("WorldText", var6);
         }
      }
   }

   private int GAsInt(r_ var1, CameraRenderState var2) {
      int var3 = 0;
      double var4 = Math.sin(var1.O);
      double var6 = Math.cos(var1.O);
      Vec3d var8 = var2.pos;

      for (dv var12 : var1.cAsDvArray()) {
         if (var12.T && !(var12.m < 0.00392156862745098)) {
            double var13 = var12.M + var12.B;
            double var15 = var12.N + var12.U;
            double var17 = var1.P + var13 * var6 + var15 * var4 - var8.x;
            double var19 = var1.g + var12.A + var12.f - var8.y;
            double var21 = var1.V + var15 * var6 - var13 * var4 - var8.z;
            double var23 = var17 * var17 + var19 * var19 + var21 * var21;

            int var25;
            for (var25 = var3++; var25 > 0 && this.f[var25 - 1] < var23; var25--) {
               this.f[var25] = this.f[var25 - 1];
               this.n[var25] = this.n[var25 - 1];
            }

            this.f[var25] = var23;
            this.n[var25] = var12;
         }
      }

      return var3;
   }

   private void oAsVoid(MatrixStack var1, CameraRenderState var2, r_ var3, d1 var4, boolean var5, int var6, double var7) {
      double var9 = Math.sin(var3.O);
      double var11 = Math.cos(var3.O);
      Vec3d var13 = var2.pos;
      this.A.set(0.0F, 0.0F, -1.0F).rotate(var2.orientation);

      for (int var14 = 0; var14 < var6; var14++) {
         dv var15 = this.n[var14];
         double var16 = Math.sqrt(this.f[var14]);
         if (!(var16 >= var4.b)) {
            double var18 = var15.m * var4.Y * var7;
            if (var4.N) {
               double var20 = Math.min(var4.I, var4.b - 0.1);
               var18 *= 1.0 - a1.OAsDouble((var16 - var20) / (var4.b - var20));
            }

            var18 *= a1.OAsDouble(var16 / 0.8);
            if (var5) {
               var18 *= var4.A;
            }
            int var38 = switch (var4.M) {
               case STATIC -> var4.x;
               case GRADIENT -> bg.xAsInt(var4.x, var4.O, (Math.sin(var15.j) + 1.0) * 0.5);
               case RAINBOW -> bg.HSBtoRGB((float)((var3.HAsDouble() * 0.035 + var15.j / (Math.PI * 2)) % 1.0), 0.45F, 1.0F);
            };
            var38 = bg.pAsInt(var38, var18);
            if (var38 >>> 24 >= 1) {
               WorldTextRenderer.Type_o var21 = this.LAsR9_Type_o(var15.V);
               double var22 = var15.M + var15.B;
               double var24 = var15.N + var15.U;
               double var26 = var3.P + var22 * var11 + var24 * var9 - var13.x;
               double var28 = var3.g + var15.A + var15.f - var13.y;
               double var30 = var3.V + var24 * var11 - var22 * var9 - var13.z;
               double var32 = Math.hypot((double)(var21.width + 12), (double)(var21.height + 8))
                  * 0.01890625
                  * var4.C
                  * (1.0 + var15.E * var4.P)
                  * var15.F
                  * 0.5;
               if (!(var26 * (double)this.A.x + var28 * (double)this.A.y + var30 * (double)this.A.z < -var32)) {
                  this.X.set(var1.peek().getPositionMatrix()).translate((float)var26, (float)var28, (float)var30);
                  switch (var4.E) {
                     case BILLBOARD:
                        this.X.rotate(var2.orientation);
                        break;
                     case Y_AXIS_BILLBOARD:
                        this.X.rotateY((float)Math.atan2(-var26, -var30));
                        break;
                     case FIXED:
                        this.X.rotateY((float)var15.h).rotateX((float)var15.Y);
                  }

                  double var34 = var4.p == d1.Type_q.STATIC ? 0.0 : var15.x;
                  this.X.rotateZ((float)(var15.L + var34));
                  float var36 = (float)(0.01890625 * var4.C * (1.0 + var15.E * var4.P) * var15.F);
                  this.X.scale(var36, -var36, var36).translate(0.0F, -var21.centerY, 0.0F);
                  if (var4.h && !var5) {
                     this.X.translate(2.0F, 2.0F, -0.03F);
                     this.U.o = bg.XAsInt(-15724520, (double)(var38 >>> 24) / 255.0 * 0.5);
                     var21.text.draw(this);
                     this.X.translate(-2.0F, -2.0F, 0.03F);
                  }

                  this.U.o = var38;
                  var21.text.draw(this);
               }
            }
         }
      }

      this.L.NAsVoid(this::jAsPw);
   }

   private ImmediateMaterialState jAsPw(ImmediateMaterialState var1) {
      int var2 = var1.pipeline() == RenderPipelines.RENDERTYPE_TEXT_INTENSITY ? 1 : 0;
      return new ImmediateMaterialState(this.P[this.e][var2], var1.outputTarget(), var1.dynamicTransforms(), var1.scissorState(), var1.textures());
   }

   public void drawGlyph(DrawnGlyphRect glyph) {
      this.yAsVoid(glyph);
   }

   public void drawRectangle(TextDrawable rect) {
      this.yAsVoid(rect);
   }

   private void yAsVoid(TextDrawable var1) {
      this.U.U = this.L.XAsVertexConsumer(var1.getRenderLayer(TextLayerType.NORMAL));
      if (this.K == null) {
         var1.render(this.X, this.U, 15728880, false);
      } else if (this.xAsBoolean(var1)) {
         var1.render(this.t, this.U, 15728880, false);
      }
   }

   public void GAsVoid() {
      if (this.L != null) {
         this.L.hAsVoid();
      }
   }

   @Override
   public void close() {
      this.W.clear();
      this.AAsVoid();
      Arrays.fill(this.n, null);
      if (this.L != null) {
         this.L.close();
         this.L = null;
      }

      this.i = false;
      this.T = -1;
   }

   public static record Type_c(int kind, double reveal, double exit, double time, int accent) {
      public static final int FADE = 0;
      public static final int POP = 1;
      public static final int WAVE = 2;
   }

   public static record Type_d(
      String text,
      double x,
      double y,
      double z,
      double scale,
      int color,
      float qx,
      float qy,
      float qz,
      float qw,
      boolean worldAligned,
      WorldTextRenderer.Type_c motion
   ) {
      public Type_d(String var1, double var2, double var4, double var6, double var8, int var10) {
         this(var1, var2, var4, var6, var8, var10, 0.0F, 0.0F, 0.0F, 1.0F, false);
      }

      public Type_d(
         String var1, double var2, double var4, double var6, double var8, int var10, float var11, float var12, float var13, float var14, boolean var15
      ) {
         this(var1, var2, var4, var6, var8, var10, var11, var12, var13, var14, var15, null);
      }
   }

   private static final class Type_k {
      WorldTextRenderer.Type_d T;
      WorldTextRenderer.Type_o t;
      double K;

      private Type_k() {
      }
   }

   private static record Type_o(GlyphDrawable text, int width, int height, float centerY) {
   }
}
