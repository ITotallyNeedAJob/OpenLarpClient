package com.larpclient.mixin;

import com.larpclient.module.Removals;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.client.sound.Channel.SourceManager;
import net.minecraft.client.sound.SoundSystem.PlayResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({SoundSystem.class})
public abstract class SoundSystemMixin {
   @Shadow
   @Final
   private Map<SoundInstance, SourceManager> field_18950;
   @Unique
   private final Set<SoundInstance> LineSegment = new HashSet<>();

   public SoundSystemMixin() {
   }

   @Shadow
   private float method_4853(SoundInstance var1) {
      throw new AssertionError("Mixin shadow");
   }

   @Inject(
      method = {"method_4857()V"},
      at = {@At("TAIL")}
   )
   private void larpclient$injectTick(CallbackInfo var1) {
      if (Removals.KAsRemovals() != null && (Removals.KAsRemovals().isRuntimeActive() || !this.e.isEmpty())) {
         this.e.retainAll(this.field_18950.keySet());

         for (Entry var3 : this.field_18950.entrySet()) {
            SoundInstance var4 = (SoundInstance)var3.getKey();
            if (Removals.uAsBoolean(var4.getId().toString())) {
               if (this.e.add(var4)) {
                  ((SourceManager)var3.getValue()).run(var0 -> var0.setVolume(0.0F));
               }
            } else if (this.e.remove(var4)) {
               float var5 = this.method_4853(var4);
               ((SourceManager)var3.getValue()).run(var1x -> var1x.setVolume(var5));
            }
         }
      }
   }

   @Inject(
      method = {"method_4854(Lnet/minecraft/class_1113;)Lnet/minecraft/class_1140$class_11518;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectHandler(SoundInstance var1, CallbackInfoReturnable<PlayResult> var2) {
      if (Removals.uAsBoolean(var1.getId().toString())) {
         var2.setReturnValue(PlayResult.NOT_STARTED);
      }
   }

   @Inject(
      method = {"method_4853(Lnet/minecraft/class_1113;)F"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void larpclient$injectF(SoundInstance var1, CallbackInfoReturnable<Float> var2) {
      if (Removals.uAsBoolean(var1.getId().toString())) {
         var2.setReturnValue(0.0F);
      }
   }
}
