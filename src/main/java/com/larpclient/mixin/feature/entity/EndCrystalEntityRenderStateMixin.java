package com.larpclient.mixin.feature.entity;

import com.larpclient.wj;
import net.minecraft.client.render.entity.state.EndCrystalEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({EndCrystalEntityRenderState.class})
public class EndCrystalEntityRenderStateMixin implements wj {
   @Unique
   private int v = Integer.MIN_VALUE;

   public EndCrystalEntityRenderStateMixin() {
   }

   @Override
   public int larpCrystalId() {
      return this.v;
   }

   @Override
   public void larpCrystalId(int var1) {
      this.v = var1;
   }
}
