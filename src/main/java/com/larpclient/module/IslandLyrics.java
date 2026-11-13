package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.IslandLyricsPreview;
import com.larpclient.LyricsController;
import net.minecraft.client.gui.DrawContext;

public final class IslandLyrics extends Module {
   private static IslandLyrics N;
   private final IslandLyricsPreview D = new IslandLyricsPreview();
   private LyricsController s;

   public IslandLyrics() {
      super(
         "Island Lyrics",
         "The current Spotify lyric line in a movable pill. Song title, artist, album and duration are sent to LRCLIB to find lyrics.",
         Category.RENDER,
         -1
      );
      N = this;
   }

   public static IslandLyrics zAsIslandLyrics() {
      return N;
   }

   public static boolean cAsBoolean() {
      return N != null && N.isOperational();
   }

   public static boolean tAsBoolean() {
      return N != null && N.isEnabled() ? N != null && N.yAsBoolean() : false;
   }

   @Override
   public String getStatus() {
      return this.s == null ? "Play a song in Spotify desktop" : this.s.kAsWk_Type_r().status();
   }

   @Override
   public void onEnable() {
      this.eAsVoid();
      this.s = LyricsController.oAsWk(this);
      ClientLog.GAsVoid("IslandLyrics", "Island Lyrics enabled; whole cue lines in the HUD element, shared Spotify worker");
   }

   @Override
   public void onDisable() {
      this.eAsVoid();
      ClientLog.XAsVoid("IslandLyrics", "Released the shared Spotify worker and reset the pill");
   }

   private void eAsVoid() {
      if (this.s != null) {
         LyricsController.eAsVoid(this);
         this.s = null;
      }

      this.D.MAsVoid();
   }

   public boolean yAsBoolean() {
      return this.isEnabled() ? this.isRuntimeActive() && this.s != null && this.D.oAsBoolean(this.s.kAsWk_Type_r(), System.nanoTime()) : false;
   }

   public void WAsVoid(DrawContext var1, int var2, int var3, int var4, boolean var5) {
      long var6 = System.nanoTime();
      if (var5) {
         this.D.NAsVoid(var1, var2, var3, var4, var6);
      } else if (this.isRuntimeActive() && this.s != null) {
         this.D.TAsVoid(var1, var2, var3, var4, this.s.kAsWk_Type_r(), var6);
      }
   }
}
