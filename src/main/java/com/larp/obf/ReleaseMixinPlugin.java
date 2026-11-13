package com.larp.obf;

import java.lang.System.Logger.Level;
import java.util.List;
import java.util.Set;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public final class ReleaseMixinPlugin implements IMixinConfigPlugin {
   private boolean f0;

   public ReleaseMixinPlugin() {
   }

   private static String m0() {
      return "MCowBQYDK2VwAyEArWjHusXwRacOd8MTDBrSZ7jyqPezfhCqgR/px+YbGH0=";
   }

   public void onLoad(String var1) {
      this.f0 = false;
   }

   public void acceptTargets(Set<String> var1, Set<String> var2) {
      if (this.f0) {
         try {
            ReleaseArchiveVerifier.m2(var2);
         } catch (IllegalStateException var4) {
            throw m1("Mixin target admission", var4);
         }
      }
   }

   private static LinkageError m1(String var0, Exception var1) {
      String var2 = "[Larp/Auth] Protected release rejected during " + var0 + "; remove modifying mods, restore the intact release archive and restart";
      System.getLogger("Larp/Auth").log(Level.ERROR, var2, var1);
      return new LinkageError(var2, var1);
   }

   public String getRefMapperConfig() {
      return null;
   }

   public boolean shouldApplyMixin(String var1, String var2) {
      return true;
   }

   public List<String> getMixins() {
      return null;
   }

   public void preApply(String var1, ClassNode var2, String var3, IMixinInfo var4) {
   }

   public void postApply(String var1, ClassNode var2, String var3, IMixinInfo var4) {
   }
}
