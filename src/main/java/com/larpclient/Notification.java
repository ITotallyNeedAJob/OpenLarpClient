package com.larpclient;

import com.larpclient.module.CustomSky;
import com.larpclient.module.KeybindSetting;
import com.larpclient.module.ModeSetting;
import com.larpclient.module.Module;
import com.larpclient.module.Setting;
import com.larpclient.module.SkyShader;
import java.util.ArrayList;
import java.util.List;

public final class Notification extends Module {
   private final CustomSky j;
   private final SkyShader O;
   private final ClientPreferences r;
   private final ModeSetting style;
   private static Notification i;
   private static List<Module> L = List.of();
   private static List<Module> Y = List.of();

   public Notification(CustomSky var1, SkyShader var2, ClientPreferences var3) {
      super(var1.getName(), "Choose a sky effect and adjust its appearance", var1.getCategory(), -1);
      this.j = var1;
      this.O = var2;
      this.r = var3;
      ArrayList<String> var4 = new ArrayList<>();
      var1.skyStyle().getModes().forEach(var1x -> var4.add("World: " + var1x));
      var2.mode().getModes().forEach(var1x -> var4.add("Shader: " + var1x));
      this.style = new ModeSetting(
         "Style", "Choose a sky. Classic and shader skies retain their own appearance settings", "World: Nebula", var4.toArray(String[]::new)
      ) {
         public String getValue() {
            return Notification.this.BAsString() + ": " + (Notification.this.BAsString().equals("World") ? var1.skyStyle() : var2.mode()).getValue();
         }

         @Override
         public String getModeLabel(String var1x) {
            String var2x = var1x.substring(var1x.indexOf(": ") + 2);
            return var1x.startsWith("World:") ? var2x + " (Classic)" : var2x;
         }

         @Override
         public boolean isSearchable() {
            return true;
         }

         @Override
         public void setValue(String var1x) {
            if (!this.acceptsValue(var1x)) {
               throw new IllegalArgumentException("Unknown sky: " + var1x);
            } else {
               boolean var2x = Notification.this.isEnabled();
               boolean var3x = var1x.startsWith("World:");
               (var3x ? var1.skyStyle() : var2.mode()).setValue(var1x.substring(var1x.indexOf(": ") + 2));
               var3.i = var3x ? "World" : "Shader";
               if (var2x) {
                  Notification.this.QAsVoid(var3x);
               }

               ClientLog.GAsVoid("Sky", "Selected {}; enabled={}", var1x, var2x);
            }
         }
      };
   }

   private static KeybindSetting xAsU(String var0, Module var1) {
      KeybindSetting var2 = new KeybindSetting(
         var0, "Retains the original sky shortcut. Takes a key or a middle or side mouse button; Escape/Delete clears it", var1.getKeyBind()
      );
      var2.onChanged(var1::setKeyBind);
      return var2;
   }

   private String BAsString() {
      if (this.j.isEnabled()) {
         this.r.i = "World";
      } else if (this.O.isEnabled()) {
         this.r.i = "Shader";
      }

      return this.r.i;
   }

   private void QAsVoid(boolean var1) {
      if (var1) {
         this.O.disable();
         this.j.enable();
      } else {
         this.j.disable();
         this.O.enable();
      }
   }

   public ModeSetting style() {
      return this.style;
   }

   public List<Setting<?>> CAsList() {
      return List.of(xAsU("Classic sky keybind", this.j), xAsU("Larp sky keybind", this.O));
   }

   public Module IAsModule() {
      return (Module)(this.BAsString().equals("World") ? this.j : this.O);
   }

   public List<Setting<?>> BAsList() {
      Module var1 = (Module)(this.BAsString().equals("World") ? this.j : this.O);
      ModeSetting var2 = var1 == this.j ? this.j.skyStyle() : this.O.mode();
      return var1.getSettings().stream().filter(var1x -> var1x != var2).toList();
   }

   @Override
   public String getDisplayName() {
      return "Sky";
   }

   @Override
   public boolean hasSettings() {
      return true;
   }

   @Override
   public List<Setting<?>> getSettings() {
      ArrayList var1 = new ArrayList<>(this.j.getSettings());
      var1.addAll(this.O.getSettings());
      return var1;
   }

   @Override
   public boolean isEnabled() {
      return this.j.isEnabled() || this.O.isEnabled();
   }

   @Override
   public boolean isOperational() {
      return this.isEnabled() ? this.j.isOperational() || this.O.isOperational() : false;
   }

   @Override
   public boolean isRuntimeActive() {
      return this.isEnabled() ? this.j.isRuntimeActive() || this.O.isRuntimeActive() : false;
   }

   @Override
   public void toggle() {
      this.setEnabled(!this.isEnabled());
   }

   @Override
   public void setEnabled(boolean var1) {
      if (var1) {
         this.enable();
      } else {
         this.disable();
      }
   }

   @Override
   public void enable() {
      this.QAsVoid(this.BAsString().equals("World"));
   }

   @Override
   public void disable() {
      this.BAsString();
      this.j.disable();
      this.O.disable();
   }

   public static List<Module> sAsList(List<Module> var0, ClientPreferences var1) {
      if (i != null && i.r == var1 && L.equals(var0)) {
         return Y;
      } else {
         CustomSky var2 = null;
         SkyShader var3 = null;

         for (Module var5 : var0) {
            if (var5 instanceof CustomSky var6) {
               var2 = var6;
            }

            if (var5 instanceof SkyShader var9) {
               var3 = var9;
            }
         }

         if (var2 != null && var3 != null) {
            if (i == null || i.j != var2 || i.O != var3 || i.r != var1) {
               i = new Notification(var2, var3, var1);
            }

            ArrayList var7 = new ArrayList(var0.size() - 1);

            for (Module var10 : var0) {
               if (var10 == var2) {
                  var7.add(i);
               } else if (var10 != var3) {
                  var7.add(var10);
               }
            }

            L = List.copyOf(var0);
            Y = List.copyOf(var7);
            return Y;
         } else {
            return var0;
         }
      }
   }

   public static Module YAsModule(Module var0, List<Module> var1, ClientPreferences var2) {
      return !(var0 instanceof CustomSky) && !(var0 instanceof SkyShader)
         ? var0
         : sAsList(var1, var2).stream().filter(var0x -> var0x instanceof Notification).findFirst().orElse(var0);
   }
}
