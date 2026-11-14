package com.larpclient;

import com.larpclient.module.AstralWeaponTrails;
import com.larpclient.module.Halloween;
import com.larpclient.module.SwordModels;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderSetup;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Direction.Axis;
import org.joml.Vector3fc;

public final class WeaponTrailRenderer {
   private static final lb[] R = new lb[]{new lb(), new lb()};
   private static final ac[] ScreenScale = new ac[]{new ac(), new ac()};
   private static final LinkedHashMap<BakedQuad, LineSegment.Type_c> AuraModule = new LinkedHashMap<>();
   private static final IdentityHashMap<float[], LineSegment.Type_c> X = new IdentityHashMap<>();
   private static int F = -1;
   private static boolean d;
   private static boolean s;

   public static void LAsVoid() {
      for (lb var3 : R) {
         var3.XAsVoid();
      }

      for (ac var7 : y) {
         var7.FAsVoid();
      }

      AuraModule.clear();
      X.clear();
      SkyChunkCache = false;
      SahurDanceEngine = false;
   }

   public static void TAsVoid(Hand var0) {
      int var1 = var0 == Hand.MAIN_HAND ? 0 : 1;
      R[var1].XAsVoid();
      y[var1].FAsVoid();
   }

   public static int lAsInt() {
      return R[0].HAsInt() + R[1].HAsInt();
   }

   public static void PAsVoid(double var0, double var2) {
      for (lb var7 : R) {
         var7.HAsVoid(var0, var2);
      }
   }

   private static boolean lAsBoolean(ItemStack var0) {
      if (!Halloween.bAsBoolean()) {
         AstralWeaponTrails var2 = AstralWeaponTrails.JAsAstralWeaponTrails();
         return var2 != null && var2.JAsBoolean(var0);
      } else {
         MinecraftClient var1 = MinecraftClient.getInstance();
         return SwordModels.PAsBoolean(var0)
            && var1.world != null
            && var1.player != null
            && var1.player.isAlive()
            && !var1.player.isUsingSpyglass()
            && var1.options.getPerspective().isFirstPerson();
      }
   }

   public static dc.Type_c VAsDc_Type_c() {
      CinematicQuality var0 = Halloween.HAsHalloween().fAsM7();
      return new dc.Type_c("Astral", 0.22, 0.44, var0.n * 0.75, -7026494, -3046331, var0.dAsBoolean(), false, true, var0 == CinematicQuality.CINEMATIC ? 3 : 2);
   }

   public static void oAsVoid(
      ItemRenderState var0,
      MatrixStack var1,
      OrderedRenderCommandQueue var2,
      OrderedRenderCommandQueue var3,
      ItemStack var4,
      ItemDisplayContext var5,
      Hand var6,
      float var7,
      float var8,
      int var9,
      int var10,
      int var11
   ) {
      if (lAsBoolean(var4) && var5.isFirstPerson() && !d) {
         WeaponTrailRenderer.Type_r var12 = new WeaponTrailRenderer.Type_r();
         OrderedRenderCommandQueue var13 = (OrderedRenderCommandQueue)Proxy.newProxyInstance(
            OrderedRenderCommandQueue.class.getClassLoader(), new Class[]{OrderedRenderCommandQueue.class}, (var2xx, var3x, var4x) -> {
               if (var3x.getName().equals("submitItem")) {
                  try {
                     List var5x = (List)var4x[6];
                     var12.EAsVoid((MatrixStack)var4x[0], var5x);
                  } catch (RuntimeException var7x) {
                     SAsVoid(var7x);
                  }
               }

               try {
                  return var3x.invoke(var2, var4x);
               } catch (InvocationTargetException var6x) {
                  throw var6x.getCause();
               }
            }
         );
         var0.render(var1, var13, var9, var10, var11);
         if (var12.Z != null) {
            HAsVoid(var6, var4, var12.E, var12.Z, var7, var7 > 0.0F, var3);
         } else if (!s) {
            SkyChunkCache = true;
            ClientLog.XAsVoid("WeaponTrails", "No usable native sword blade quads; this model keeps ordinary rendering without PetEffectRenderer guessed trail attachment");
         }
      } else {
         var0.render(var1, var2, var9, var10, var11);
      }
   }

   public static void lAsVoid(Hand var0, ItemStack var1, float[] var2, int var3, MatrixStack var4, float var5, boolean var6, OrderedRenderCommandQueue var7) {
      if (lAsBoolean(var1) && !d) {
         try {
            LineSegment.Type_c var8 = X.get(var2);
            if (var8 == null) {
               var8 = LineSegment.dAsE_Type_c(var2);
               if (var8 == null) {
                  return;
               }

               if (X.size() >= 12) {
                  X.clear();
               }

               X.put(var2, var8);
            }

            HAsVoid(var0, var1, var8, var8.transform(var4.peek().getPositionMatrix(), var3), var5, var6, var7);
         } catch (RuntimeException var91) {
            SAsVoid(var91);
         }
      }
   }

   private static void HAsVoid(Hand var0, ItemStack var1, LineSegment.Type_c var2, LineSegment.Type_c var3, float var4, boolean var5, OrderedRenderCommandQueue var6) {
      AstralWeaponTrails var7 = AstralWeaponTrails.JAsAstralWeaponTrails();
      if (lAsBoolean(var1) && !d) {
         long var8 = op.RAsLong();

         try {
            if (F != BloomQuality.J) {
               LAsVoid();
               F = BloomQuality.J;
            }

            int var10 = var0 == Hand.MAIN_HAND ? 0 : 1;
            lb var11 = R[var10];
            dc.Type_c var12 = Halloween.bAsBoolean() ? VAsDc_Type_c() : var7.vAsDc_Type_c();
            op.FAsVoid("Weapon trail blade captures", 1L);
            op.FAsVoid("Weapon trail active captures", var5 && var4 > 0.0F ? 1L : 0L);
            op.YAsVoid(var10 == 0 ? "Weapon trail main swing (milli)" : "Weapon trail offhand swing (milli)", (long)Math.round(var4 * 1000.0F));
            var11.MAsBoolean(
               y[var10].ZAsObject(var1, var2),
               var3.root(),
               var3.tip(),
               mb.RAsDouble(),
               var4,
               var5,
               var12.duration(),
               Halloween.bAsBoolean() ? x9.jAsInt(48) : var7.lAsInt()
            );
            dc.Type_g var13 = dc.iAsDc_Type_g(var11.eAsList(), mb.RAsDouble(), var12);
            op.YAsVoid("Weapon trail history", (long)lAsInt());
            op.FAsVoid("Weapon trail vertices", (long)var13.vertices());
            if (var13.vertices() == 0) {
               return;
            }

            WeaponTrailRenderer.Type_x.lAsVoid();
            if (!var13.body().AAsBoolean()) {
               gAsVoid(var6, var13.body(), false);
            }

            if (!var13.light().AAsBoolean()) {
               gAsVoid(var6, var13.light(), true);
            }

            return;
         } catch (RuntimeException var171) {
            SAsVoid(var171);
         } finally {
            op.aAsVoid("Weapon trails", var8);
         }
      }
   }

   private static void gAsVoid(OrderedRenderCommandQueue var0, dc.Type_l var1, boolean var2) {
      var0.getBatchingQueue(var2 ? 3 : 2).submitCustom(new MatrixStack(), WeaponTrailRenderer.Type_x.oAsRenderLayer(var2), (var1xx, var2x) -> {
         try {
            for (int var3 = 0; var3 < var1.DAsInt(); var3++) {
               var2x.vertex(var1xx, var1.NAsFloat(var3), var1.ZAsFloat(var3), var1.JAsFloat(var3)).color(var1.BAsInt(var3));
            }
         } catch (RuntimeException var41) {
            SAsVoid(var41);
         }
      });
   }

   private static void SAsVoid(RuntimeException var0) {
      for (lb var4 : R) {
         var4.XAsVoid();
      }

      if (!d) {
         ClientLog.lAsVoid("WeaponTrails", "Native blade capture/submission failed; trails suspended until toggle or resource reload", var0);
      }

      SahurDanceEngine = true;
   }

   private WeaponTrailRenderer() {
   }

   private static final class Type_r {
      LineSegment.Type_c Z;
      LineSegment.Type_c E;

      private Type_r() {
      }

      void EAsVoid(MatrixStack var1, List<BakedQuad> var2) {
         if (!var2.isEmpty()) {
            BakedQuad var3 = var2.getFirst();
            LineSegment.Type_c var4 = WeaponTrailRenderer.x.get(var3);
            if (var4 == null) {
               ArrayList var5 = new ArrayList(Math.min(4096, var2.size() * 4));

               for (BakedQuad var7 : var2) {
                  if (var7.face().getAxis() != Axis.Z) {
                     for (int var8 = 0; var8 < 4 && var5.size() < 4096; var8++) {
                        Vector3fc var9 = var7.getPosition(var8);
                        var5.add(new Vec3d((double)var9.x(), (double)var9.y(), (double)var9.z()));
                     }
                  }
               }

               if (var5.size() < 4) {
                  for (BakedQuad var12 : var2) {
                     for (int var13 = 0; var13 < 4 && var5.size() < 4096; var13++) {
                        Vector3fc var14 = var12.getPosition(var13);
                        var5.add(new Vec3d((double)var14.x(), (double)var14.y(), (double)var14.z()));
                     }
                  }
               }

               var4 = LineSegment.LAsE_Type_c(var5);
               if (var4 == null) {
                  return;
               }

               if (WeaponTrailRenderer.x.size() >= 32) {
                  WeaponTrailRenderer.x.remove(WeaponTrailRenderer.x.keySet().iterator().next());
               }

               WeaponTrailRenderer.x.put(var3, var4);
            }

            LineSegment.Type_c var10 = var4.transform(var1.peek().getPositionMatrix(), 1);
            if (this.Z == null || var10.length() > this.Z.length()) {
               this.Z = var10;
               this.E = var4;
            }
         }
      }
   }

   private static final class Type_x {
      private static final RenderLayer F = FAsRenderLayer(false);
      private static final RenderLayer CropHelper = FAsRenderLayer(true);
      private static int MenuPopupProvider = -1;

      private Type_x() {
      }

      private static RenderLayer FAsRenderLayer(boolean var0) {
         RenderPipeline var1 = RenderPipeline.builder(new Snippet[]{RenderPipelines.POSITION_COLOR_SNIPPET})
            .withLocation(Identifier.of("larp-client", var0 ? "weapon_trail_light" : "weapon_trail_body"))
            .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
            .withDepthWrite(false)
            .withBlend(var0 ? BlendFunction.LIGHTNING : BlendFunction.TRANSLUCENT)
            .withCull(false)
            .build();
         return RenderLayer.of(var0 ? "larp_weapon_trail_light" : "larp_weapon_trail_body", RenderSetup.builder(var1).translucent().build());
      }

      static RenderLayer oAsRenderLayer(boolean var0) {
         return var0 ? CropHelper : F;
      }

      static void lAsVoid() {
         if (MenuPopupProvider != BloomQuality.J) {
            WorldLabelLayout.DAsVoid(F.getRenderPipeline());
            WorldLabelLayout.DAsVoid(CropHelper.getRenderPipeline());
            MenuPopupProvider = BloomQuality.J;
            ClientLog.XAsVoid("WeaponTrails", "Native depth-tested hand ribbon materials validated for generation {}", f);
         }
      }
   }
}
