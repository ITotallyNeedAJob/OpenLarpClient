package com.larpclient.module;

import com.larpclient.CapeCatalog;
import com.larpclient.CapeTextureCache;
import com.larpclient.ClientLog;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.SkinTextures;
import net.minecraft.util.AssetInfo.TextureAsset;

public final class Capes extends Module {
   private static Capes Q;
   private final ModeSetting cape = this.addSetting(
      new ModeSetting("Cape", "Official cape to wear. Only your client shows it", "MineCon 2011", CapeCatalog.AAsStringArray()) {
         @Override
         public boolean isSearchable() {
            return true;
         }

         @Override
         public String getModeLabel(String var1) {
            return CapeCatalog.FAsString(var1);
         }

         @Override
         public int drawModeIcon(DrawContext var1, String var2, int var3, int var4) {
            CapeCatalog.Type_r var5 = CapeCatalog.TAsA__Type_r(var2);
            if (var5 == null) {
               return 0;
            } else {
               TextureAsset var6 = CapeTextureCache.KAsAssetInfo_TextureAsset(var5);
               if (var6 != null) {
                  var1.drawTexture(RenderPipelines.GUI_TEXTURED, var6.texturePath(), var3 + 4, var4, 1.0F, 1.0F, 10, 16, 10, 16, 64, 32);
               } else {
                  var1.fill(var3 + 4, var4, var3 + 14, var4 + 16, 822083583);
               }

               return 18;
            }
         }
      }
   );
   private volatile TextureAsset R;
   private long i;
   private long f;
   private volatile String s = "Choose a cape";

   public Capes() {
      super("Capes", "Wear any official cape, like a MineCon cape. Only you see it", Category.VISUALS, -1);
      Q = this;
      this.cape.onChanged(var1 -> {
         if (this.isRuntimeActive()) {
            this.bAsVoid();
         }
      });
   }

   public static Capes gAsCapes() {
      return Q;
   }

   @Override
   public String getStatus() {
      return this.s;
   }

   @Override
   public void onEnable() {
      this.bAsVoid();
   }

   @Override
   public void onTick() {
      long var1;
      long var2;
      if (((var1 = this.f - 0L) == 0L ? 0 : (var1 < 0L ? -1 : 1)) != 0 && ((var2 = System.nanoTime() - this.f) == 0L ? 0 : (var2 < 0L ? -1 : 1)) >= 0) {
         this.bAsVoid();
      }
   }

   @Override
   public void onDisable() {
      this.i++;
      this.R = null;
      this.f = 0L;
      this.s = "Original cape restored";
      ClientLog.GAsVoid("Capes", "Disabled; original cape restored");
   }

   private void bAsVoid() {
      this.f = 0L;
      long var1 = ++this.i;
      CapeCatalog.Type_r var3 = CapeCatalog.TAsA__Type_r(this.cape.getValue());
      if (var3 == null) {
         this.R = null;
         this.s = "Choose a cape";
      } else {
         CompletableFuture var4 = CapeTextureCache.xAsCompletableFuture(var3);
         if (var4.isDone() && !var4.isCompletedExceptionally()) {
            this.R = (TextureAsset)var4.join();
            this.s = var3.label();
         } else {
            this.s = "Loading " + var3.label() + "...";
            var4.whenCompleteAsync((var4x, var5) -> {
               if (var1 == this.i && this.isRuntimeActive()) {
                  if (var5 != null) {
                     this.s = "Could not load " + var3.label() + ". Retrying in 30 seconds";
                     this.f = System.nanoTime() + 30000000000L;
                  } else {
                     this.R = (TextureAsset)var4x;
                     this.s = var3.label();
                     ClientLog.GAsVoid("Capes", "Wearing {}", var3.label());
                  }
               }
            }, getMc());
         }
      }
   }

   public SkinTextures VAsSkinTextures(SkinTextures var1) {
      if (this.isEnabled()) {
         if (var1 != null && this.isOperational()) {
            CapeCatalog.Type_r var2 = CapeCatalog.TAsA__Type_r(this.cape.getValue());
            TextureAsset var3 = var2 == null ? null : CapeTextureCache.KAsAssetInfo_TextureAsset(var2);
            return var3 == null ? var1 : new SkinTextures(var1.body(), var3, null, var1.model(), var1.secure());
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public SkinTextures LAsSkinTextures(UUID var1, SkinTextures var2) {
      if (!this.isEnabled()) {
         return var2;
      } else {
         TextureAsset var3 = this.R;
         return var3 != null && var2 != null && this.isRuntimeActive() && mc.player != null && var1.equals(mc.player.getUuid())
            ? new SkinTextures(var2.body(), var3, null, var2.model(), var2.secure())
            : var2;
      }
   }
}
