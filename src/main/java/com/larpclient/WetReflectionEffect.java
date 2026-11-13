package com.larpclient;

import com.larpclient.module.Module;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import java.nio.ByteBuffer;
import java.util.IdentityHashMap;
import java.util.OptionalInt;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStopping;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.Disconnect;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.AfterEntities;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.BeforeTranslucent;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.EndMain;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.StartMain;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.GlGpuBuffer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gl.UniformType;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.render.state.WorldRenderState;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

public final class WetReflectionEffect {
   private static final IdentityHashMap<RenderPipeline, RenderPipeline> c = new IdentityHashMap<>();
   private static final IdentityHashMap<RenderPipeline, RenderPipeline> L = new IdentityHashMap<>();
   private static final RenderPipeline k = ZAsRenderPipeline("wet_reflection");
   private static final RenderPipeline N = ZAsRenderPipeline("underwater");
   public static final int T = 1088;
   private static final WetReflectionEffect.Type_r w = new WetReflectionEffect.Type_r();
   private static GpuBuffer J;
   private static boolean p;
   private static boolean D;
   private static boolean t;
   private static boolean n;
   private static boolean r;
   private static boolean y;
   private static int H = -1;
   private static Object U;
   private static WorldRenderState Z;
   private static double O;
   private static double o;
   private static boolean V;

   public static void xAsVoid() {
      if (!p) {
         p = true;
         r = FabricLoader.getInstance().isModLoaded("sodium") || FabricLoader.getInstance().isModLoaded("iris");
         y = r && SodiumTerrainMaterialBridge.uAsBoolean();
         if (y) {
            ClientLog.GAsVoid(
               "TerrainMaterials",
               "Sodium {} detected; Wet Ground and Snow Ground draw through a variant of Sodium's chunk program, other terrain materials keep Sodium output",
               SodiumTerrainMaterialBridge.uAsString()
            );
         } else if (r) {
            ClientLog.oAsVoid(
               "TerrainMaterials", "Sodium/Iris detected; native terrain material hooks are unavailable, preserving renderer output and saved module choices"
            );
         }

         WorldRenderEvents.START_MAIN.register((StartMain)var0 -> mAsVoid(var0.worldState()));
         WorldRenderEvents.AFTER_ENTITIES.register((AfterEntities)var0 -> PAsVoid());
         WorldRenderEvents.BEFORE_TRANSLUCENT.register((BeforeTranslucent)var0 -> XAsVoid());
         WorldRenderEvents.END_MAIN.register((EndMain)var0 -> {
            if (gAsBoolean()) {
               try {
                  DropletSurfaceRenderer.tAsVoid();
               } catch (RuntimeException var2) {
                  RAsVoid("volumetric-surface", var2);
               }
            }

            tAsVoid();
         });
         ClientTickEvents.END_CLIENT_TICK.register(VolumetricWaterEngine::WAsVoid);
         ClientPlayConnectionEvents.DISCONNECT.register((Disconnect)(var0, var1) -> jAsVoid());
         ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)var0 -> jAsVoid());
      }
   }

   public static boolean NAsBoolean() {
      return D || r;
   }

   public static boolean gAsBoolean() {
      return t && !NAsBoolean();
   }

   public static boolean zAsBoolean() {
      return D || r && (!y || SodiumTerrainMaterialBridge.iAsBoolean() || SodiumTerrainMaterialBridge.qAsBoolean());
   }

   public static boolean fAsBoolean() {
      return zAsBoolean() || r && (SodiumTerrainMaterialBridge.jAsBoolean() || SodiumTerrainMaterialBridge.gAsBoolean());
   }

   static boolean RAsBoolean() {
      return !D && (!r || y && !SodiumTerrainMaterialBridge.qAsBoolean() && !SodiumTerrainMaterialBridge.jAsBoolean());
   }

   private static boolean DAsBoolean() {
      return t && !zAsBoolean();
   }

   private static boolean VAsBoolean() {
      return r ? !zAsBoolean() && (WAsBoolean(WetGround.KAsWetGround()) || WAsBoolean(SnowGround.vAsSnowGround())) : !D;
   }

   static boolean iAsBoolean() {
      return y && J != null && DAsBoolean() && (WAsBoolean(WetGround.KAsWetGround()) || WAsBoolean(SnowGround.vAsSnowGround()));
   }

   static int gAsInt() {
      return ((GlGpuBuffer)J).id;
   }

   private static boolean WAsBoolean(Module var0) {
      return var0 != null && var0.isRuntimeActive();
   }

   private static boolean bAsBoolean() {
      return WAsBoolean(WavingFoliage.DAsWavingFoliage())
         || WAsBoolean(WetGround.KAsWetGround())
         || WAsBoolean(SnowGround.vAsSnowGround())
         || WAsBoolean(RealisticWater.tAsRealisticWater());
   }

   public static void gAsVoid() {
      ClientLog.XAsVoid("TerrainMaterials", "Native terrain material activated; generations follow shader reloads");
   }

   static void FAsVoid(int var0) {
      if (!r) {
         ClientLog.GAsVoid("SnowGround", "Subdivided drift surfaces; cells per top={}, maximum relief={} blocks", var0 >= 2 ? 9 : 4, 0.55F);
      } else if (y) {
         ClientLog.GAsVoid(
            "SnowGround",
            "Sodium {} chunk meshes carry snow tags; drift relief follows block corners without subdivision, maximum relief={} blocks",
            SodiumTerrainMaterialBridge.uAsString(),
            0.55F
         );
      } else {
         ClientLog.oAsVoid("SnowGround", "Sodium/Iris without a supported terrain hook; snow terrain keeps renderer output, snowfall still draws");
      }
   }

   public static void TAsVoid() {
      TerrainMaterialClassifier.hAsVoid();
      MinecraftClient var0 = MinecraftClient.getInstance();
      int var1 = !WAsBoolean(WavingFoliage.DAsWavingFoliage()) && !WAsBoolean(RealisticWater.tAsRealisticWater()) && !WAsBoolean(SnowGround.vAsSnowGround())
         ? 0
         : 1;
      if (var0 != null && (var1 != 0 || V)) {
         V = var1 != 0;
         var0.execute(() -> {
            if (var0.world != null) {
               var0.worldRenderer.reload();
            }
         });
      }
   }

   public static void OAsVoid() {
      if (!bAsBoolean()) {
         QAsVoid();
      }
   }

   private static void mAsVoid(WorldRenderState var0) {
      Z = var0;
      n = false;
      MinecraftClient var1 = MinecraftClient.getInstance();
      if (U != var1.world) {
         QAsVoid();
         U = var1.world;
         O = mb.RAsDouble();
         o = 0.0;
         TerrainMaterialClassifier.hAsVoid();
      }

      if (H != BloomQuality.J) {
         QAsVoid();
         D = false;
         H = BloomQuality.J;
         c.clear();
         L.clear();
         SodiumTerrainMaterialBridge.hAsVoid();
         TAsVoid();
      }

      if (bAsBoolean() && VAsBoolean() && var0.cameraRenderState.pos != null && w3.QAsBoolean()) {
         long var2 = op.RAsLong();

         try {
            if (J == null) {
               J = RenderSystem.getDevice().createBuffer(() -> "Larp terrain material parameters", 136, 1088L);
               if (y) {
                  WorldLabelLayout.DAsVoid(k);
                  ClientLog.GAsVoid("TerrainMaterials", "Sodium Wet Ground and Snow Ground material parameters and wet reflection shader ready");
               } else {
                  kAsRenderPipeline(RenderPipelines.SOLID_TERRAIN);
                  kAsRenderPipeline(RenderPipelines.CUTOUT_TERRAIN);
                  kAsRenderPipeline(RenderPipelines.TRANSLUCENT);
                  tAsRenderPipeline(RenderPipelines.SOLID_TERRAIN);
                  tAsRenderPipeline(RenderPipelines.CUTOUT_TERRAIN);
                  tAsRenderPipeline(RenderPipelines.TRANSLUCENT);
                  WorldLabelLayout.DAsVoid(k);
                  WorldLabelLayout.DAsVoid(N);
                  ClientLog.GAsVoid("TerrainMaterials", "Terrain, water, foliage, wet reflection, snow and underwater shaders ready");
               }
            }

            jAsVoid(var0);
            if (y) {
               SodiumTerrainMaterialBridge.EAsVoid(TerrainMaterialClassifier.gAsBoolean());
            }

            t = true;
         } catch (RuntimeException var8) {
            RAsVoid(y ? "sodium-prepare" : "prepare", var8);
         } finally {
            op.aAsVoid("TerrainMaterials", var2);
         }
      }
   }

   public static RenderPipeline tAsRenderPipeline(RenderPipeline var0) {
      if (r) {
         return var0;
      } else if (!D && bAsBoolean() && (var0 != RenderPipelines.TRANSLUCENT || WAsBoolean(RealisticWater.tAsRealisticWater()))) {
         return var0 != RenderPipelines.SOLID_TERRAIN && var0 != RenderPipelines.CUTOUT_TERRAIN && var0 != RenderPipelines.TRANSLUCENT
            ? var0
            : c.computeIfAbsent(
               var0,
               var0x -> {
                  int var1 = var0x == RenderPipelines.TRANSLUCENT ? 1 : 0;
                  Builder var2 = WorldLabelLayout.GAsRenderPipeline_Builder(var0x)
                     .withLocation(WAsIdentifier("pipeline/temple/" + var0x.getLocation().getPath()))
                     .withVertexShader(WAsIdentifier("temple/terrain"))
                     .withFragmentShader(WAsIdentifier("temple/terrain"))
                     .withUniform("LarpMaterial", UniformType.UNIFORM_BUFFER);
                  if (var1 != 0) {
                     var2.withSampler("SceneColor").withSampler("SceneDepth");
                  }

                  if (var1 != 0) {
                     var2.withShaderDefine("LARP_WATER");
                  }

                  RenderPipeline var3 = var2.build();
                  WorldLabelLayout.DAsVoid(var3);
                  return var3;
               }
            );
      } else {
         return L.getOrDefault(var0, var0);
      }
   }

   private static RenderPipeline kAsRenderPipeline(RenderPipeline var0) {
      return L.computeIfAbsent(
         var0,
         var0x -> {
            Builder var1 = WorldLabelLayout.GAsRenderPipeline_Builder(var0x)
               .withLocation(WAsIdentifier("pipeline/temple/decode/" + var0x.getLocation().getPath()))
               .withVertexShader(WAsIdentifier("temple/terrain_decode"));
            RenderPipeline var2 = var1.build();
            WorldLabelLayout.DAsVoid(var2);
            return var2;
         }
      );
   }

   public static void EAsVoid(RenderPass var0, RenderPipeline var1) {
      if (J != null && !D && c.containsValue(var1)) {
         var0.setUniform("LarpMaterial", J.slice());
         if (var1.getShaderDefines().flags().contains("LARP_WATER") && w.Y != null) {
            var0.bindTexture("SceneColor", w.Y, RenderSystem.getSamplerCache().get(FilterMode.LINEAR));
            var0.bindTexture("SceneDepth", w.s, RenderSystem.getSamplerCache().get(FilterMode.NEAREST));
         }
      }
   }

   private static void jAsVoid(WorldRenderState var0) {
      WavingFoliage var1 = WavingFoliage.DAsWavingFoliage();
      RealisticWater var2 = RealisticWater.tAsRealisticWater();
      WetGround var3 = WetGround.KAsWetGround();
      SnowGround var4 = SnowGround.vAsSnowGround();
      boolean var5 = WAsBoolean(var4);
      boolean var6 = WAsBoolean(var1);
      boolean var7 = WAsBoolean(var2);
      boolean var8 = WAsBoolean(var3);
      double var9 = mb.RAsDouble();
      o = o + Math.clamp(var9 - O, 0.0, 0.1) * (var6 ? var1.speed.getValue() : 1.0);
      O = var9;
      float var11 = Math.clamp(var0.weatherRenderState.intensity, 0.0F, 1.0F);
      float var12 = var8 ? var3.puddles.getValue().floatValue() / 100.0F : 0.0F;
      float var13 = var8 ? var3.shine.getValue().floatValue() / 100.0F : 0.0F;
      float var14 = var8 ? var3.wetness.getValue().floatValue() / 100.0F : 0.0F;
      if (var8 && var3.wetterInRain.getValue()) {
         var12 = Math.min(1.0F, var12 + var11 * 0.35F);
         var13 = Math.min(1.0F, var13 + var11 * 0.25F);
         var14 = Math.min(1.0F, var14 + var11 * 0.25F);
      }

      CameraRenderState var15 = var0.cameraRenderState;
      Matrix4f var16 = new Matrix4f(w3.CAsMatrix4fc()).mul(w3.VAsMatrix4fc());
      Matrix4f var17 = new Matrix4f(var16).invert();
      float var18 = var0.skyRenderState.sunAngle;
      float var19 = -((float)Math.sin((double)var18));
      float var20 = (float)Math.cos((double)var18);
      float var21 = Math.clamp(var20 * 1.8F + 0.15F, 0.0F, 1.0F) * (1.0F - var11 * 0.9F);
      int var22 = var0.skyRenderState.skyColor;
      MemoryStack var23 = MemoryStack.stackPush();

      try {
         ByteBuffer var24 = var23.malloc(1088);
         yAsVoid(var24, (float)o, var6 ? var1.strength.getValue().floatValue() : 0.0F, var6 ? var1.gusts.getValue().floatValue() / 100.0F : 0.0F, (float)var9);
         yAsVoid(
            var24,
            var7 ? var2.intensity.getValue().floatValue() / 100.0F : 0.0F,
            var7 ? var2.waves.getValue().floatValue() / 100.0F : 0.0F,
            var7 && var2.worldReflections.getValue() ? 1.0F : 0.0F,
            var7 ? 1.0F : 0.0F
         );
         yAsVoid(
            var24,
            var7 && var2.foam.getValue() ? 1.0F : 0.0F,
            var7 && var2.caustics.getValue() ? 1.0F : 0.0F,
            var7 && var2.underwater.getValue() ? 1.0F : 0.0F,
            0.0F
         );
         yAsVoid(var24, var12, var13, var8 && var3.raindrops.getValue() ? 1.0F + var11 : 0.0F, var8 && var3.style.pAsBoolean("Flooded") ? 1.0F : 0.0F);
         yAsVoid(
            var24,
            var8 ? 1.0F : 0.0F,
            var8 && var3.worldReflections.getValue() ? 1.0F : 0.0F,
            n ? 1.0F : 0.0F,
            w3.bAsCameraSubmersionType() == CameraSubmersionType.WATER ? 1.0F : 0.0F
         );
         yAsVoid(var24, var19, var20, 0.0F, var21);
         yAsVoid(var24, (float)(var22 >> 16 & 0xFF) / 255.0F, (float)(var22 >> 8 & 0xFF) / 255.0F, (float)(var22 & 0xFF) / 255.0F, var11);
         yAsVoid(var24, (float)(var15.pos.x % 8192.0), (float)var15.pos.y, (float)(var15.pos.z % 8192.0), var14);
         yAsVoid(
            var24,
            var5 ? var4.I.getValue().floatValue() / 100.0F : 0.0F,
            var5 ? var4.j.getValue().floatValue() / 100.0F : 0.0F,
            var5 ? var4.G.getValue().floatValue() / 100.0F : 0.0F,
            var5 ? (float)var4.pAsInt() : 0.0F
         );
         var16.get(var24);
         var24.position(var24.position() + 64);
         var17.get(var24);
         var24.position(var24.position() + 64);
         if (var4 != null) {
            var4.DAsVoid(var24, var15.pos, var9, var5);
         } else {
            for (int var25 = 0; var25 < 100; var25++) {
               var24.putLong(0L);
            }
         }

         yAsVoid(var24, (float)var15.pos.x, (float)var15.pos.y, (float)var15.pos.z, 0.0F);
         var24.flip();
         RenderSystem.getDevice().createCommandEncoder().writeToBuffer(J.slice(), var24);
      } catch (Throwable var271) {
         if (var23 != null) {
            try {
               var23.close();
            } catch (Throwable var261) {
               var271.addSuppressed(var261);
            }
         }

         throw var271;
      }

      if (var23 != null) {
         var23.close();
      }
   }

   private static void XAsVoid() {
      if (gAsBoolean() && WAsBoolean(RealisticWater.tAsRealisticWater())) {
         try {
            w.OAsVoid(MinecraftClient.getInstance().getFramebuffer());
            n = true;
            jAsVoid(Z);
         } catch (RuntimeException var1) {
            RAsVoid("capture-water-scene", var1);
         }
      }
   }

   private static void PAsVoid() {
      WetGround var0 = WetGround.KAsWetGround();
      if (DAsBoolean() && WAsBoolean(var0) && var0.worldReflections.getValue()) {
         try {
            Framebuffer var1 = MinecraftClient.getInstance().getFramebuffer();
            w.OAsVoid(var1);
            n = true;
            jAsVoid(Z);
            lAsVoid(k, var1);
         } catch (RuntimeException var21) {
            RAsVoid("wet-reflection", var21);
         }
      }
   }

   private static void tAsVoid() {
      RealisticWater var0 = RealisticWater.tAsRealisticWater();
      if (gAsBoolean() && WAsBoolean(var0) && var0.underwater.getValue() && w3.bAsCameraSubmersionType() == CameraSubmersionType.WATER) {
         try {
            Framebuffer var1 = MinecraftClient.getInstance().getFramebuffer();
            w.OAsVoid(var1);
            n = true;
            jAsVoid(Z);
            lAsVoid(N, var1);
         } catch (RuntimeException var21) {
            RAsVoid("underwater", var21);
         }
      }
   }

   private static void lAsVoid(RenderPipeline var0, Framebuffer var1) {
      CommandEncoder var2 = RenderSystem.getDevice().createCommandEncoder();
      RenderPass var3 = var2.createRenderPass(() -> "Larp " + var0.getLocation(), var1.getColorAttachmentView(), OptionalInt.empty());

      try {
         var3.setPipeline(var0);
         var3.setUniform("LarpMaterial", J.slice());
         var3.bindTexture("SceneColor", w.Y, RenderSystem.getSamplerCache().get(FilterMode.LINEAR));
         var3.bindTexture("SceneDepth", w.s, RenderSystem.getSamplerCache().get(FilterMode.NEAREST));
         var3.draw(0, 3);
      } catch (Throwable var7) {
         if (var3 != null) {
            try {
               var3.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (var3 != null) {
         var3.close();
      }
   }

   static GpuBuffer MAsGpuBuffer() {
      return J;
   }

   static GpuTextureView SAsGpuTextureView() {
      return w.Y;
   }

   static GpuTextureView RAsGpuTextureView() {
      return w.s;
   }

   static WorldRenderState iAsWorldRenderState() {
      return Z;
   }

   static void FAsVoid() {
      w.OAsVoid(MinecraftClient.getInstance().getFramebuffer());
      n = true;
      jAsVoid(Z);
   }

   private static RenderPipeline ZAsRenderPipeline(String var0) {
      return RenderPipeline.builder(new Snippet[0])
         .withLocation(WAsIdentifier("pipeline/temple/" + var0))
         .withVertexShader(WAsIdentifier("post/fullscreen"))
         .withFragmentShader(WAsIdentifier("temple/" + var0))
         .withUniform("LarpMaterial", UniformType.UNIFORM_BUFFER)
         .withSampler("SceneColor")
         .withSampler("SceneDepth")
         .withVertexFormat(VertexFormats.EMPTY, DrawMode.TRIANGLES)
         .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
         .withDepthWrite(false)
         .withCull(false)
         .build();
   }

   private static void yAsVoid(ByteBuffer var0, float var1, float var2, float var3, float var4) {
      var0.putFloat(var1).putFloat(var2).putFloat(var3).putFloat(var4);
   }

   private static Identifier WAsIdentifier(String var0) {
      return Identifier.of("larp-client", var0);
   }

   private static void RAsVoid(String var0, RuntimeException var1) {
      D = true;
      QAsVoid();
      TAsVoid();
      ClientLog.lAsVoid("TerrainMaterials", "stage=" + var0 + "; restored vanilla terrain; retry after resource reload", var1);
   }

   public static void QAsVoid() {
      n = false;
      t = false;
      if (J != null) {
         J.close();
         J = null;
      }

      w.YAsVoid();
      DropletSurfaceRenderer.EAsVoid();
   }

   public static void jAsVoid() {
      VolumetricWaterEngine.YAsVoid();
      QAsVoid();
      Z = null;
      U = null;
      o = 0.0;
      O = mb.RAsDouble();
   }

   private WetReflectionEffect() {
   }

   private static final class Type_r {
      private GpuTexture O;
      private GpuTexture N;
      private GpuTextureView Y;
      private GpuTextureView s;

      private Type_r() {
      }

      private void OAsVoid(Framebuffer var1) {
         if (var1.getDepthAttachment() == null) {
            throw new IllegalStateException("Terrain target has no depth texture");
         } else {
            if (this.O == null
               || this.O.getWidth(0) != var1.textureWidth
               || this.O.getHeight(0) != var1.textureHeight
               || this.O.getFormat() != var1.getColorAttachment().getFormat()
               || this.N.getFormat() != var1.getDepthAttachment().getFormat()) {
               this.YAsVoid();
               GpuDevice var2 = RenderSystem.getDevice();
               this.O = var2.createTexture("Larp material scene", 5, var1.getColorAttachment().getFormat(), var1.textureWidth, var1.textureHeight, 1, 1);
               this.N = var2.createTexture("Larp material depth", 5, var1.getDepthAttachment().getFormat(), var1.textureWidth, var1.textureHeight, 1, 1);
               this.Y = var2.createTextureView(this.O);
               this.s = var2.createTextureView(this.N);
            }

            CommandEncoder var4 = RenderSystem.getDevice().createCommandEncoder();
            var4.copyTextureToTexture(var1.getColorAttachment(), this.O, 0, 0, 0, 0, 0, var1.textureWidth, var1.textureHeight);
            var4.copyTextureToTexture(var1.getDepthAttachment(), this.N, 0, 0, 0, 0, 0, var1.textureWidth, var1.textureHeight);
         }
      }

      private void YAsVoid() {
         if (this.Y != null) {
            this.Y.close();
            this.Y = null;
         }

         if (this.s != null) {
            this.s.close();
            this.s = null;
         }

         if (this.O != null) {
            this.O.close();
            this.O = null;
         }

         if (this.N != null) {
            this.N.close();
            this.N = null;
         }
      }
   }
}
