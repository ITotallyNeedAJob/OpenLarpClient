package com.larpclient.mixin;

import com.larpclient.DisplayAliasManager;
import java.util.List;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine.Visible;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ChatHud.class})
public abstract class ChatHudMixin {
   @Shadow
   private int field_2066;
   @Shadow
   private boolean field_2067;
   @Shadow
   @Final
   private List<Visible> field_2064;
   @Unique
   private long o = -1L;

   public ChatHudMixin() {
   }

   @Shadow
   private void method_44813() {
      throw new AssertionError();
   }

   @Shadow
   public abstract int method_1813();

   @Inject(
      method = {"method_45584()V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectTickRemovalQueueIfExists(CallbackInfo var1) {
      if (this.o != DisplayAliasManager.CAsLong()) {
         this.o = DisplayAliasManager.CAsLong();
         int var2 = this.field_2066;
         boolean var3 = this.field_2067;
         this.method_44813();
         this.field_2066 = Math.max(0, Math.min(var2, this.field_2064.size() - this.method_1813()));
         this.field_2067 = var3;
      }
   }
}
