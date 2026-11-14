package com.larpclient.mixin.compat;

import java.util.List;
import java.util.Set;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public final class CompatibilityMixinPlugin implements IMixinConfigPlugin {
   private static final String DropletTrail = "com.larpclient.mixin.compat.BasePreviewRendererMixin";
   private static final String J = "com.larpclient.mixin.compat.sodium.LevelSliceMixin";

   public CompatibilityMixinPlugin() {
   }

   public void onLoad(String var1) {
   }

   public String getRefMapperConfig() {
      return null;
   }

   public boolean shouldApplyMixin(String var1, String var2) {
      if ("com.larpclient.mixin.compat.BasePreviewRendererMixin".equals(var2)) {
         return FabricLoader.getInstance().isModLoaded("shulkerboxtooltip");
      } else {
         return "com.larpclient.mixin.compat.sodium.LevelSliceMixin".equals(var2)
            ? FabricLoader.getInstance()
               .getModContainer("sodium")
               .map(var0 -> var0.getMetadata().getVersion().getFriendlyString().startsWith("0.8."))
               .orElse(false)
            : true;
      }
   }

   public void acceptTargets(Set<String> var1, Set<String> var2) {
   }

   public List<String> getMixins() {
      return null;
   }

   public void preApply(String var1, ClassNode var2, String var3, IMixinInfo var4) {
   }

   public void postApply(String var1, ClassNode var2, String var3, IMixinInfo var4) {
   }
}
