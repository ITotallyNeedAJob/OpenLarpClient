package com.larpclient.module;

import com.larpclient.CinematicQuality;
import com.larpclient.ClientLog;
import com.larpclient.EffectGeometryRenderer;
import com.larpclient.HalloweenWorldEffects;
import com.larpclient.SharedRenderEffects;
import com.larpclient.SkyEffectManager;
import com.larpclient.WeaponTrailRenderer;
import com.larpclient.qn;
import java.util.List;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.network.packet.s2c.play.EntityDamageS2CPacket;
import net.minecraft.world.dimension.DimensionType.Skybox;

public final class Halloween extends Module implements qn {
   private static Halloween C;
   private final ModeSetting intensity = this.addSetting(
      new ModeSetting("Intensity", "A coordinated After Dark preset", "Balanced", "Subtle", "Balanced", "Cinematic")
   );
   private final BooleanSetting A = this.cAsBooleanSetting("Harvest Moon", "Amber moon and midnight atmosphere");
   private final BooleanSetting k = this.cAsBooleanSetting("Cinderfall", "Sparse drifting ash and embers outdoors");
   private final BooleanSetting R = this.cAsBooleanSetting("Haunted Combat", "Spectral hit accents and native weapon trails");
   private final BooleanSetting f = this.cAsBooleanSetting("Soul Harvest", "An intact pumpkin flattens the defeated player and sends out a ground shockwave");
   private final BooleanSetting afterDarkMenu = this.addSetting(new BooleanSetting("After Dark Menu", "Use the Halloween logo in menus and ClickGUI", true) {
      @Override
      public String getDisplayName() {
         return "Menu Logo";
      }
   });
   private HalloweenWorldEffects v;

   public Halloween() {
      super("Halloween", "After Dark: harvest moon, drifting cinders and spectral combat", Category.VISUALS, -1);
      C = this;
      this.intensity.onChanged(var1 -> {
         this.clear();
         if (this.isRuntimeActive() && this.R.getValue()) {
            cAsVoid();
         }

         ClientLog.GAsVoid("Halloween", "Intensity changed: {}", var1);
      });
      this.k.onChanged(var1 -> {
         if (this.v != null) {
            this.v.xAsVoid();
         }
      });
      this.R.onChanged(var1 -> {
         if (this.v != null) {
            this.v.hAsVoid();
         }

         if (this.isRuntimeActive()) {
            cAsVoid();
         }
      });
      this.f.onChanged(var1 -> {
         if (this.isRuntimeActive()) {
            TAsVoid();
         }
      });
      this.A.onChanged(var1 -> {
         if (this.isRuntimeActive()) {
            SkyEffectManager.KAsVoid();
         }
      });
   }

   private BooleanSetting cAsBooleanSetting(String var1, String var2) {
      return this.addSetting(new BooleanSetting(var1, var2, true));
   }

   public static Halloween HAsHalloween() {
      return C;
   }

   public CinematicQuality fAsM7() {
      return CinematicQuality.xAsM7(this.intensity.getValue());
   }

   public List<Setting<?>> MAsList() {
      return List.of(this.intensity);
   }

   public List<Setting<?>> VAsList() {
      return List.of(this.A, this.k, this.R, this.f, this.afterDarkMenu);
   }

   public static boolean bAsBoolean() {
      return C != null && C.isEnabled() ? C != null && C.isRuntimeActive() && C.R.getValue() && SharedRenderEffects.ZAsBoolean("Halloween") : false;
   }

   public static boolean RAsBoolean() {
      return C != null && C.isEnabled() ? C != null && C.isRuntimeActive() && C.f.getValue() : false;
   }

   public static boolean vAsBoolean() {
      return C != null && C.isEnabled() ? C != null && C.isOperational() && C.afterDarkMenu.getValue() : false;
   }

   public static boolean OAsBoolean() {
      if (C == null || !C.isEnabled()) {
         return false;
      } else if (C != null && C.isRuntimeActive() && C.A.getValue()) {
         MinecraftClient var0 = getMc();
         return var0.world != null
            && var0.player != null
            && var0.world.getDimension().skybox() == Skybox.OVERWORLD
            && var0.gameRenderer.getCamera().getSubmersionType() == CameraSubmersionType.NONE;
      } else {
         return false;
      }
   }

   public void kAsVoid(EntityDamageS2CPacket var1) {
      if (this.isEnabled() && bAsBoolean()) {
         this.ZAsR7().fAsVoid(getMc(), var1, this.fAsM7());
      }
   }

   private HalloweenWorldEffects ZAsR7() {
      if (this.v == null) {
         this.v = new HalloweenWorldEffects();
      }

      return this.v;
   }

   @Override
   public String effectName() {
      return "Halloween";
   }

   @Override
   public void tick(MinecraftClient var1) {
      if (this.isEnabled()) {
         this.ZAsR7().aAsVoid(var1, this.fAsM7(), this.k.getValue(), bAsBoolean());
      }
   }

   @Override
   public void prepare(float var1, CameraRenderState var2) {
      if (this.isEnabled() && this.v != null) {
         this.v.EAsVoid(var1, var2, this.k.getValue(), bAsBoolean());
      }
   }

   @Override
   public void render(EffectGeometryRenderer var1, boolean var2) {
      if (this.isEnabled() && this.v != null) {
         this.v.SAsVoid(var1, var2, this.k.getValue(), bAsBoolean());
      }
   }

   @Override
   public boolean glowing() {
      return this.isEnabled() ? this.v != null && this.v.pAsBoolean(this.k.getValue(), bAsBoolean()) : false;
   }

   @Override
   public boolean soft() {
      return this.v != null && this.v.uAsBoolean(this.k.getValue(), bAsBoolean());
   }

   private static void cAsVoid() {
      WeaponTrailRenderer.LAsVoid();
   }

   private static void TAsVoid() {
      KillFX var0 = KillFX.aAsKillFX();
      if (var0 != null) {
         var0.lAsVoid();
      }
   }

   @Override
   public void clear() {
      if (this.v != null) {
         this.v.bAsVoid();
      }
   }

   public void wAsVoid(boolean var1) {
      this.clear();
      if (this.isRuntimeActive()) {
         this.PAsVoid();
      }

      if (!var1) {
         this.disable();

         for (Setting var3 : this.getSettings()) {
            var3.reset();
         }

         this.setKeyBind(-1);
      }
   }

   @Override
   public void onEnable() {
      this.clear();
      this.PAsVoid();
      ClientLog.GAsVoid("Halloween", "After Dark enabled: {}; sky, hit and finisher ownership is temporary", this.intensity.getValue());
   }

   @Override
   public void onDisable() {
      this.clear();
      this.PAsVoid();
      ClientLog.GAsVoid("Halloween", "After Dark disabled; seasonal state cleared, saved cosmetics retained");
   }

   private void PAsVoid() {
      if (this.f.getValue()) {
         TAsVoid();
      }

      if (this.R.getValue()) {
         cAsVoid();
      }

      if (this.A.getValue()) {
         SkyEffectManager.KAsVoid();
      }
   }
}
