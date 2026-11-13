package com.larpclient.mixin;

import com.larpclient.ProxyRoute;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import java.util.UUID;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ClientLoginNetworkHandler.class})
public abstract class ClientLoginNetworkHandlerMixin {
   @Shadow
   @Final
   private ClientConnection field_3707;

   public ClientLoginNetworkHandlerMixin() {
   }

   @Redirect(
      method = {"method_2892(Ljava/lang/String;)Lnet/minecraft/class_2561;"},
      at = {@At(
         value = "INVOKE",
         target = "Lcom/mojang/authlib/minecraft/MinecraftSessionService;joinServer(Ljava/util/UUID;Ljava/lang/String;Ljava/lang/String;)V"
      )}
   )
   private void larpclient$redirectHandler(MinecraftSessionService var1, UUID var2, String var3, String var4) throws AuthenticationException {
      ProxyRoute.mAsVoid(var1, ((ClientConnectionAccessor)this.field_3707).larpProxyChannel(), var2, var3, var4);
   }
}
