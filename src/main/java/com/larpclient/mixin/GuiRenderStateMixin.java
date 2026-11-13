package com.larpclient.mixin;

import com.larpclient.ClientLog;
import com.larpclient.xt;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.gui.render.state.GuiRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({GuiRenderState.class})
public abstract class GuiRenderStateMixin implements xt.Type_e {
   @Unique
   private static final int R = 4;
   @Shadow
   @Final
   private List<?> field_60043;
   @Unique
   private final int[] V = new int[4];
   @Unique
   private final int[] C = new int[4];
   @Unique
   private final xt.Type_s[] D = new xt.Type_s[4];
   @Unique
   private final boolean[] y = new boolean[4];
   @Unique
   private int S;
   @Unique
   private int K;
   @Unique
   private static boolean r;

   public GuiRenderStateMixin() {
   }

   @Override
   public void larpWarpBegin(xt.Type_s var1, boolean var2) {
      if (this.S == 4) {
         this.K++;
         if (!r) {
            r = true;
            ClientLog.oAsVoid("GUI", "More than {} projected GUI ranges in one frame; extra content stays flat", 4);
         }
      } else {
         int var3 = this.S++;
         this.V[var3] = this.field_60043.size();
         this.C[var3] = Integer.MAX_VALUE;
         this.D[var3] = var1;
         this.y[var3] = var2;
      }
   }

   @Override
   public void larpWarpEnd() {
      if (this.K > 0) {
         this.K--;
      } else if (this.S > 0) {
         this.C[this.S - 1] = this.field_60043.size();
      }
   }

   @Inject(
      method = {"method_70926()V"},
      at = {@At("HEAD")}
   )
   private void larpclient$injectClear(CallbackInfo var1) {
      Arrays.fill(this.D, null);
      this.S = this.K = 0;
   }

   @WrapOperation(
      method = {"method_71074(Ljava/util/function/Consumer;Lnet/minecraft/class_11246$class_11325;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Ljava/util/List;get(I)Ljava/lang/Object;"
      )}
   )
   private Object larpclient$wrapOperationV(List<?> var1, int var2, Operation<Object> var3) {
      int var4 = -1;

      for (int var5 = 0; var5 < this.S; var5++) {
         if (var2 >= this.V[var5] && var2 < this.C[var5]) {
            var4 = var5;
            break;
         }
      }

      if (var4 < 0) {
         xt.ZAsVoid(-1, null, false);
      } else {
         xt.ZAsVoid(var2 - this.V[var4], this.D[var4], this.y[var4]);
      }

      return var3.call(new Object[]{var1, var2});
   }

   @Inject(
      method = {"method_71074(Ljava/util/function/Consumer;Lnet/minecraft/class_11246$class_11325;)V"},
      at = {@At("RETURN")}
   )
   private void larpclient$injectV(CallbackInfo var1) {
      xt.ZAsVoid(-1, null, false);
   }
}
