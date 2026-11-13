package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.LyricsController;
import com.larpclient.WorldTextRenderer;
import com.larpclient.bd;
import com.larpclient.bg;
import com.larpclient.lq;
import com.larpclient.nb;
import com.larpclient.rv;
import com.larpclient.w3;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaterniond;
import org.joml.Vector3d;

public final class WorldLyrics extends Module {
   private static final double x = 0.75;
   private static WorldLyrics O;
   private final ModeSetting style = this.addSetting(new ModeSetting("Style", "Fixed plane tilt for new lyric lines", "Calm", "Calm", "Floating", "Dreamy"));
   private final ModeSetting motion = this.addSetting(new ModeSetting("Motion", "How letters enter, move while sung and leave", "Pop", "Pop", "Wave", "Fade"));
   private final NumberSetting density = this.addSetting(new NumberSetting("Density", "Length and amount of fading lyric trail", 16.0, 4.0, 32.0, 1.0));
   private final NumberSetting size = this.addSetting(new NumberSetting("Size", "World text size", 1.0, 0.5, 2.0, 0.1));
   private final ColorSetting color = this.addSetting(new ColorSetting("Color", "Lyric tint", -724993));
   private final BooleanSetting gradient = this.addSetting(new BooleanSetting("Gradient", "Flow the lyric tint into the client accent color", true));
   private final BooleanSetting glow = this.addSetting(new BooleanSetting("Glow", "Soft text bloom", true));
   private final BooleanSetting throughWalls = this.addSetting(new BooleanSetting("Through Walls", "Keep lyrics visible behind blocks and entities", true));
   private final nb w = new nb();
   private final rv f = new rv();
   private final Quaterniond m = new Quaterniond();
   private LyricsController r;
   private WorldTextRenderer o;
   private Object g;
   private Object n;
   private boolean Z;
   private boolean I;
   private long t;
   private double c;

   public WorldLyrics() {
      super(
         "WorldLyrics",
         "Whole Spotify lyric lines spawn near your crosshair, travel with you and animate letter by letter. Song title, artist, album and duration are sent to LRCLIB to find lyrics.",
         Category.VISUALS,
         -1
      );
      O = this;
   }

   public static WorldLyrics TAsWorldLyrics() {
      return O;
   }

   @Override
   public String getDisplayName() {
      return "Spotify World Lyrics";
   }

   @Override
   public String getStatus() {
      return this.r == null ? "Play a song in Spotify desktop" : this.r.kAsWk_Type_r().status();
   }

   @Override
   public void onEnable() {
      this.wAsVoid();
      this.r = LyricsController.oAsWk(this);
      ClientLog.GAsVoid(
         "WorldLyrics",
         "Spotify World Lyrics enabled; motion="
            + this.motion.getValue()
            + ", throughWalls="
            + this.throughWalls.getValue()
            + ", lines follow the player with fixed birth orientation"
      );
   }

   @Override
   public void onDisable() {
      this.wAsVoid();
   }

   public void wAsVoid() {
      if (this.r != null) {
         LyricsController.eAsVoid(this);
         this.r = null;
      }

      this.WAsVoid();
      ClientLog.XAsVoid("WorldLyrics", "Stopped playback worker and released world text resources");
   }

   public void WAsVoid() {
      this.w.iAsVoid();
      this.f.BAsVoid();
      if (this.o != null) {
         this.o.close();
         this.o = null;
      }

      this.g = this.n = null;
      this.I = this.Z = false;
      this.t = 0L;
      this.c = 0.0;
   }

   public void gAsVoid(CameraRenderState var1) {
      if (this.isEnabled()) {
         this.I = this.Z = false;
         MinecraftClient var2 = getMc();
         if (this.isRuntimeActive() && this.isInGame() && this.getPlayer().isAlive()) {
            if (this.g != var2.world || this.n != var2.player) {
               this.WAsVoid();
               this.g = var2.world;
               this.n = var2.player;
               ClientLog.GAsVoid(
                  "WorldLyrics",
                  "Lyric planes initialized; player-following placement, per-letter motion, "
                     + (this.throughWalls.getValue() ? "drawn through blocks" : "native scene depth with surface-aware birth")
               );
            }

            long var3 = System.nanoTime();
            double var5 = this.t == 0L ? 0.0 : Math.clamp((double)(var3 - this.t) / 1.0E9, 0.0, 0.1);
            this.t = var3;
            if (var2.isPaused()) {
               var5 = 0.0;
            }

            this.c += var5;
            this.f.nAsVoid(this.density.getValue().intValue());
            if (!var2.isPaused() && this.r != null) {
               boolean var7 = this.w.CAsBoolean(this.r.kAsWk_Type_r(), var3);
               if (this.w.fAsBoolean()) {
                  this.f.BAsVoid();
               }

               if (var7 || this.w.fAsBoolean()) {
                  if (!this.w.GAsList().isEmpty()) {
                     this.f.XAsVoid(this.throughWalls.getValue() ? 8.0 : this.gAsDouble(var1));
                  }

                  this.f.TAsVoid(this.w.GAsList(), this.w.AAsInt());
               }
            }

            if (this.o == null) {
               this.o = new WorldTextRenderer();
            }

            double var13 = this.style.pAsBoolean("Floating") ? 1.5 : (this.style.pAsBoolean("Dreamy") ? 2.2 : 0.65);
            List<rv.Type_x> var9 = this.f
               .KAsList(
                  var1.pos.x,
                  var1.pos.y,
                  var1.pos.z,
                  this.m.set(var1.orientation),
                  (double)(1.0F / w3.CAsMatrix4fc().m00()),
                  (double)(1.0F / w3.CAsMatrix4fc().m11()),
                  this.size.getValue(),
                  var13,
                  var5,
                  this.o::mAsInt
               );
            int var10 = this.motion.pAsBoolean("Pop") ? 1 : (this.motion.pAsBoolean("Wave") ? 2 : 0);
            int var11 = this.gradient.getValue() ? bd.w | 0xFF000000 : 0;
            List var12 = var9.stream()
               .map(
                  var3x -> new WorldTextRenderer.Type_d(
                        var3x.text(),
                        var3x.x(),
                        var3x.y(),
                        var3x.z(),
                        var3x.scale(),
                        bg.pAsInt(this.color.getValue(), var10 == 0 ? var3x.opacity() : 1.0),
                        (float)var3x.qx(),
                        (float)var3x.qy(),
                        (float)var3x.qz(),
                        (float)var3x.qw(),
                        true,
                        var10 == 0 && var11 == 0 ? null : new WorldTextRenderer.Type_c(var10, Math.min(1.0, var3x.age() / 0.75), var3x.exit(), this.c, var11)
                     )
               )
               .toList();
            this.o.nAsVoid(var12, var1);
            this.I = !var12.isEmpty();
            this.Z = this.I && this.glow.getValue();
         }
      }
   }

   private double gAsDouble(CameraRenderState var1) {
      Vector3d var2 = new Vector3d(0.0, 0.0, -1.0).rotate(this.m.set(var1.orientation)).normalize();
      Vec3d var3 = var1.pos.add(var2.x * 7.99, var2.y * 7.99, var2.z * 7.99);
      lq.Type_l var4 = lq.AAsLq_Type_l(getMc().world, var1.pos, var3);
      return var4 == null ? 8.0 : Math.max(0.6, var1.pos.distanceTo(var4.point()) - 0.35);
   }

   public boolean jAsBoolean() {
      return this.isEnabled() ? this.isRuntimeActive() && this.Z && this.o != null && this.o.TAsBoolean() : false;
   }

   public void pAsVoid(MatrixStack var1, CameraRenderState var2, boolean var3) {
      if (this.isEnabled() && this.isRuntimeActive() && this.I && this.o != null && this.g == getMc().world && (!var3 || this.Z)) {
         this.o.CAsVoid(var1, var2, this.throughWalls.getValue(), !var3, var3 ? 0.3 : 1.0);
      }
   }

   public void OAsVoid() {
      if (this.o != null) {
         this.o.GAsVoid();
      }
   }
}
