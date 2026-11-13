package com.larpclient.mixin;

import com.larpclient.DisplayAliasManager;
import com.larpclient.LarpClient;
import com.larpclient.module.Nametags;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({EntityRenderer.class})
public class EntityRendererMixin {
   public EntityRendererMixin() {
   }

   @Inject(
      method = {"method_62354(Lnet/minecraft/class_1297;Lnet/minecraft/class_10017;F)V"},
      at = {@At("TAIL")}
   )
   private void larpclient$injectFV(Entity var1, EntityRenderState var2, float var3, CallbackInfo var4) {
      if (var1 instanceof PlayerEntity && var2.displayName != null) {
         var2.displayName = DisplayAliasManager.pAsText(var1.getUuid(), ((PlayerEntity)var1).getGameProfile().name(), var2.displayName);
      }

      LarpClient var5 = LarpClient.CAsLarpClient();
      if (var5 != null) {
         Nametags var6 = var5.mAsF().EAsModule(Nametags.class);
         if (var6 != null && var6.CAsBoolean(var1)) {
            var2.displayName = null;
            if (var2 instanceof PlayerEntityRenderState var7) {
               var7.playerName = null;
            }
         }
      }
   }
}
