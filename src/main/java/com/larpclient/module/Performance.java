package com.larpclient.module;

import com.larpclient.BloomQuality;
import com.larpclient.ClientLog;
import com.larpclient.LarpClient;
import com.larpclient.ProfileManager;
import com.larpclient.SoftParticlePipeline;
import com.larpclient.BloomQualityHelper;
import java.util.List;
import net.minecraft.client.render.ChunkBuilderMode;

public final class Performance extends Module {
   private static Performance L;
   public final ModeSetting chunkUpdates = this.addSetting(
      new ModeSetting(
         "Chunk Updates",
         "Balanced threads terrain updates but keeps player edits immediate; Threaded may delay visible block updates",
         "Balanced",
         "Balanced",
         "Threaded",
         "Unchanged"
      )
   );
   public final BooleanSetting streamingFPSLimit = this.addSetting(
      new BooleanSetting("Streaming FPS Limit", "Cap gameplay FPS to leave GPU headroom for recording; never raises Minecraft's existing limit", false)
   );
   public final NumberSetting streamingFPS = this.addSetting(
      new NumberSetting("Streaming FPS", "Gameplay frame limit while recording or streaming", 120.0, 30.0, 240.0, 1.0)
   );

   public Performance() {
      super(
         "Performance",
         "Render quality for client effects always applies. Turn the module on for smoother chunk updates and an optional recording FPS cap",
         Category.PLAYER,
         -1
      );
      L = this;
      this.streamingFPS.visibleWhen(this.streamingFPSLimit::getValue);
      this.chunkUpdates.onChanged(var1 -> this.rAsVoid());
      this.streamingFPSLimit.onChanged(var1 -> this.rAsVoid());
      this.streamingFPS.onChanged(var1 -> this.rAsVoid());
   }

   public List<Setting<?>> RAsList() {
      return List.of(this.chunkUpdates, this.streamingFPSLimit, this.streamingFPS);
   }

   public List<Setting<?>> CAsList() {
      ModeSetting var1 = new ModeSetting("Bloom", "Glow quality for effects that request bloom", BloomQuality.U.name(), "Off", "Low", "Medium", "High", "Ultra");
      var1.onChanged(var0 -> BloomQuality.U = BloomQuality.Type_w.valueOf(var0));
      ModeSetting var2 = new ModeSetting(
         "Blur", "Blur quality behind the ClickGUI window and HUD panels", BloomQuality.v.name(), "Off", "Low", "Medium", "High", "Ultra"
      );
      var2.onChanged(var0 -> BloomQuality.v = BloomQuality.Type_w.valueOf(var0));
      BloomQuality.Type_w var3 = x9.K;
      ModeSetting var4 = new ModeSetting(
         "Effects",
         "Particle detail, glow and refraction for trails, weather and After Dark",
         var3 == BloomQuality.Type_w.Medium ? "Balanced" : var3.name(),
         "Off",
         "Low",
         "Balanced",
         "High",
         "Ultra"
      );
      var4.onChanged(var1x -> {
         ProfileManager var2x = LarpClient.CAsLarpClient().fAsBo();
         BloomQuality.Type_w var3x = var1x.equals("Balanced") ? BloomQuality.Type_w.Medium : BloomQuality.Type_w.valueOf(var1x);
         if (!var2x.OAsBoolean(var3x)) {
            BloomQuality.Type_w var4x = var2x.yAsN7().a;
            var4.setValue(var4x == BloomQuality.Type_w.Medium ? "Balanced" : var4x.name());
         }
      });
      BooleanSetting var5 = new BooleanSetting("Soft particles", "Fade glow and smoke where they meet solid surfaces", SoftParticlePipeline.S);
      var5.onChanged(var1x -> {
         ProfileManager var2x = LarpClient.CAsLarpClient().fAsBo();
         if (!var2x.fAsBoolean(var1x)) {
            var5.setValue(Boolean.valueOf(var2x.yAsN7().O));
         }
      });
      return List.of(var1, var2, var4, var5);
   }

   public static ChunkBuilderMode tAsChunkBuilderMode(ChunkBuilderMode var0) {
      if (L != null && L.isEnabled()) {
         Performance var1 = L;
         if (var1 == null || !var1.isRuntimeActive()) {
            return var0;
         } else if (var1.chunkUpdates.pAsBoolean("Threaded")) {
            return ChunkBuilderMode.NONE;
         } else {
            return var1.chunkUpdates.pAsBoolean("Balanced") && var0 == ChunkBuilderMode.NEARBY ? ChunkBuilderMode.PLAYER_AFFECTED : var0;
         }
      } else {
         return var0;
      }
   }

   public static int QAsInt(int var0) {
      if (L != null && L.isEnabled()) {
         Performance var1 = L;
         return var1 != null && var1.isRuntimeActive() && var1.streamingFPSLimit.getValue() ? Math.min(var0, var1.streamingFPS.getValue().intValue()) : var0;
      } else {
         return var0;
      }
   }

   private void rAsVoid() {
      if (this.isRuntimeActive()) {
         this.tAsVoid();
      }
   }

   private void tAsVoid() {
      ClientLog.GAsVoid(
         "Performance",
         "Chunk updates={}; recording FPS limit={}",
         this.chunkUpdates.getValue(),
         this.streamingFPSLimit.getValue() ? this.streamingFPS.getValue().intValue() : "off"
      );
   }

   @Override
   public void onEnable() {
      this.tAsVoid();
   }

   @Override
   public void onDisable() {
      ClientLog.GAsVoid("Performance", "Native render policy released; video preferences retained");
   }
}
