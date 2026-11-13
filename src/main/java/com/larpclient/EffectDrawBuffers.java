package com.larpclient;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.function.UnaryOperator;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;

public final class EffectDrawBuffers implements AutoCloseable {
   private final bk M = new bk(() -> "Larp effects", 524288);
   private final LinkedHashMap<RenderLayer, bk.Type_v> V = new LinkedHashMap<>();
   private RenderLayer X;
   private VertexConsumer J;

   public EffectDrawBuffers() {
   }

   public VertexConsumer XAsVertexConsumer(RenderLayer var1) {
      if (var1 == this.X && this.J != null) {
         return this.J;
      } else {
         this.X = var1;
         return this.J = this.M
            .dAsVertexConsumer(
               this.V
                  .computeIfAbsent(
                     var1,
                     var1x -> this.M
                           .LAsBk_Type_v(
                              var1x.getVertexFormat(), var1x.getDrawMode(), var1x.isTranslucent() ? RenderSystem.getProjectionType().getVertexSorter() : null
                           )
                  )
            );
      }
   }

   public void CAsVoid() {
      this.NAsVoid(UnaryOperator.identity());
   }

   public void NAsVoid(UnaryOperator<ImmediateMaterialState> var1) {
      if (!this.V.isEmpty()) {
         this.X = null;
         this.J = null;

         try {
            this.M.OAsVoid();

            for (Entry var3 : this.V.entrySet()) {
               if (!NAsBoolean((RenderLayer)var3.getKey())) {
                  bk.Type_c var4 = this.M.UAsBk_Type_c((bk.Type_v)var3.getValue());
                  if (var4 != null) {
                     ImmediateMaterialState var5 = var1.apply(ImmediateMaterialState.prepare((RenderLayer)var3.getKey()));
                     if (!JumpRingRenderer.QAsBoolean(var5, var4) && !SoftParticlePipeline.FAsBoolean(var5, var4)) {
                        var5.drawFromBuffer(var4);
                     }

                     op.FAsVoid("Effect draw groups", 1L);
                  }
               }
            }

            for (Entry var10 : this.V.entrySet()) {
               if (NAsBoolean((RenderLayer)var10.getKey())) {
                  bk.Type_c var11 = this.M.UAsBk_Type_c((bk.Type_v)var10.getValue());
                  if (var11 != null) {
                     ImmediateMaterialState var12 = var1.apply(ImmediateMaterialState.prepare((RenderLayer)var10.getKey()));
                     if (!JumpRingRenderer.QAsBoolean(var12, var11) && !SoftParticlePipeline.FAsBoolean(var12, var11)) {
                        var12.drawFromBuffer(var11);
                     }

                     op.FAsVoid("Effect draw groups", 1L);
                  }
               }
            }
         } finally {
            this.V.clear();
            this.M.bAsVoid();
         }
      }
   }

   static boolean NAsBoolean(RenderLayer var0) {
      return var0.getRenderPipeline() == RenderPipelines.GLINT;
   }

   public void FAsVoid() {
      this.X = null;
      this.J = null;
      this.V.clear();
      this.M.bAsVoid();
   }

   public void hAsVoid() {
      this.V.clear();
      this.X = null;
      this.J = null;
      this.M.vAsVoid();
   }

   @Override
   public void close() {
      this.M.close();
   }
}
