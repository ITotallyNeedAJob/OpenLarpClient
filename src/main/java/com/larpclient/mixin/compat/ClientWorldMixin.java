package com.larpclient.mixin.compat;

import com.larpclient.FakeSpawnerController;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ClientWorld.class})
public abstract class ClientWorldMixin {
   public ClientWorldMixin() {
   }

   @Inject(
      method = {"method_30092(Lnet/minecraft/class_2338;Lnet/minecraft/class_2680;II)Z"},
      at = {@At("RETURN")}
   )
   private void larpclient$injectIIZ(BlockPos var1, BlockState var2, int var3, int var4, CallbackInfoReturnable<Boolean> var5) {
      FakeSpawnerController var6 = FakeSpawnerController.ZAsMa();
      if ((Boolean)var5.getReturnValue() && var6.XAsBoolean()) {
         var6.FAsVoid(var1, var2);
      }
   }

   @Inject(
      method = {"method_41926(Lnet/minecraft/class_2338;Lnet/minecraft/class_2680;Lnet/minecraft/class_243;)V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectV(BlockPos var1, BlockState var2, Vec3d var3, CallbackInfo var4) {
      FakeSpawnerController var5 = FakeSpawnerController.ZAsMa();
      if (var5.XAsBoolean()) {
         var5.zAsVoid(var1, var2);
      }
   }

   @Redirect(
      method = {"method_30092(Lnet/minecraft/class_2338;Lnet/minecraft/class_2680;II)Z", "method_41926(Lnet/minecraft/class_2338;Lnet/minecraft/class_2680;Lnet/minecraft/class_243;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_638;method_8320(Lnet/minecraft/class_2338;)Lnet/minecraft/class_2680;"
      )}
   )
   private BlockState larpclient$redirectIIZ(ClientWorld var1, BlockPos var2) {
      if (!FakeSpawnerController.ZAsMa().XAsBoolean()) {
         return var1.getBlockState(var2);
      } else if (var1.isOutOfHeightLimit(var2)) {
         return Blocks.AIR.getDefaultState();
      } else {
         WorldChunk var3 = var1.getWorldChunk(var2);
         return var3.getSection(var3.getSectionIndex(var2.getY())).getBlockState(var2.getX() & 15, var2.getY() & 15, var2.getZ() & 15);
      }
   }

   @Inject(
      method = {"method_41928(Lnet/minecraft/class_2338;Lnet/minecraft/class_2680;I)V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectIV(BlockPos var1, BlockState var2, int var3, CallbackInfo var4) {
      FakeSpawnerController var5 = FakeSpawnerController.ZAsMa();
      if (var5.XAsBoolean()) {
         var5.zAsVoid(var1, var2);
      }
   }

   @ModifyVariable(
      method = {"method_31595(Lnet/minecraft/class_2338;Lnet/minecraft/class_2680;)V"},
      at = {@At("HEAD")},
      argsOnly = true,
      ordinal = 0
   )
   private BlockState larpclient$modifyVariableV(BlockState var1, BlockPos var2, BlockState var3) {
      return FakeSpawnerController.ZAsMa().AAsBlockState(var2, var1);
   }

   @Redirect(
      method = {"method_74254(Lnet/minecraft/class_2338;Lnet/minecraft/class_2350;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_638;method_8320(Lnet/minecraft/class_2338;)Lnet/minecraft/class_2680;"
      )}
   )
   private BlockState larpclient$redirectV(ClientWorld var1, BlockPos var2) {
      return FakeSpawnerController.ZAsMa().AAsBlockState(var2, var1.getBlockState(var2));
   }

   @Inject(
      method = {"method_8465(Lnet/minecraft/class_1297;DDDLnet/minecraft/class_6880;Lnet/minecraft/class_3419;FFJ)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void larpclient$injectFFJV(
      Entity var1,
      double var2,
      double var4,
      double var6,
      RegistryEntry<SoundEvent> var8,
      SoundCategory var9,
      float var10,
      float var11,
      long var12,
      CallbackInfo var14
   ) {
      MinecraftClient var15 = MinecraftClient.getInstance();
      if (var15.isOnThread() && var9 == SoundCategory.BLOCKS) {
         FakeSpawnerController var16 = FakeSpawnerController.ZAsMa();
         FakeSpawnerController.Type_d var17 = var16.PAsMa_Type_d(BlockPos.ofFloored(var2, var4, var6), (SoundEvent)var8.value(), var10, var11);
         if (var17 != null) {
            var14.cancel();
            ((ClientWorld)(Object)this).playSound(var1, var2, var4, var6, Registries.SOUND_EVENT.getEntry(var17.event()), var9, var17.volume(), var17.pitch(), var12);
         }
      }
   }
}
