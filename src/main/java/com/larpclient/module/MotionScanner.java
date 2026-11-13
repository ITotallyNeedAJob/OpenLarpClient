package com.larpclient.module;

import com.larpclient.CandidateGroup;
import com.larpclient.ClientLog;
import com.larpclient.NotificationHud;
import java.util.List;
import java.util.Set;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents.Unload;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.DebugRender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.DrawStyle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.LightData;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.debug.gizmo.GizmoDrawing;
import org.joml.Vector3f;

public final class MotionScanner extends Module {
   private final NumberSetting opacity = this.addSetting(new NumberSetting("Opacity", "Opacity of detected chunk volumes", 140.0, 10.0, 255.0, 1.0));
   private final NumberSetting sensitivity = this.addSetting(
      new NumberSetting("Sensitivity", "Minimum nearby anomalous chunks required for a discovery", 1.0, 1.0, 5.0, 1.0)
   );
   private final BooleanSetting hUDNotification = this.addSetting(new BooleanSetting("HUD Notification", "Show a local discovery notification", true));
   private final BooleanSetting soundNotification = this.addSetting(
      new BooleanSetting("Sound Notification", "Play the source note-block chime on a discovery", true)
   );
   private final BooleanSetting chatNotifications = this.addSetting(new BooleanSetting("Chat Notifications", "Write discoveries to local chat", true));
   private final BooleanSetting showCoords = this.addSetting(new BooleanSetting("Show Coords", "Include representative chunk-center coordinates in chat", true));
   private final BooleanSetting showTracers = this.addSetting(new BooleanSetting("Show Tracers", "Draw camera-to-discovery lines", false));
   private final NumberSetting tracersAlpha = this.addSetting(new NumberSetting("Tracers Alpha", "Opacity of camera-to-discovery lines", 70.0, 0.0, 255.0, 1.0));
   private final ColorSetting highlightColor = this.addSetting(
      new ColorSetting("Highlight Color", "Detected chunk and tracer color; source blue by default", -14122241)
   );
   private final CandidateGroup a = new CandidateGroup();
   private Set<Long> d = Set.of();
   private ClientWorld Z;
   private boolean J;
   private static MotionScanner O;

   public MotionScanner() {
      super("Motion Scanner", "Detect unusual received skylight patterns below sea level", Category.RENDER, -1);
      O = this;
      this.tracersAlpha.visibleWhen(this.showTracers::getValue);
      this.showCoords.visibleWhen(this.chatNotifications::getValue);
      this.sensitivity.onChanged(var1 -> this.J = true);
      ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var1, var2) -> {
         if (var1 == this.Z) {
            this.a.CAsVoid(var2.getPos().toLong());
            this.J = true;
            this.d = this.a.jAsSet();
         }
      });
      WorldRenderEvents.BEFORE_DEBUG_RENDER.register((DebugRender)var1 -> this.JAsVoid());
   }

   public List<Setting<?>> LAsList() {
      return List.of(this.sensitivity);
   }

   public List<Setting<?>> fAsList() {
      return List.of(this.hUDNotification, this.soundNotification, this.chatNotifications, this.showCoords);
   }

   public List<Setting<?>> SAsList() {
      return List.of(this.opacity, this.highlightColor, this.showTracers, this.tracersAlpha);
   }

   private void IAsVoid(ClientWorld var1) {
      this.a.VAsVoid();
      this.d = Set.of();
      this.J = false;
      this.Z = var1;
      NotificationHud.GAsW8().HAsVoid(this.getName());
   }

   @Override
   public void onEnable() {
      this.IAsVoid(getMc().world);
      ClientLog.GAsVoid(
         "BaseFinding", "Motion Scanner active: explicit zeroed sky-light sections below Y 62, cluster threshold {}", this.sensitivity.getValue()
      );
   }

   @Override
   public void onDisable() {
      ClientLog.XAsVoid("BaseFinding", "Motion Scanner cleanup: {} tracked chunks", this.a.mAsInt());
      this.IAsVoid(null);
   }

   public static void oAsVoid(ClientWorld var0, int var1, int var2, LightData var3) {
      if (O != null && O.isEnabled()) {
         MotionScanner var4 = O;
         if (var4 != null && var4.isRuntimeActive() && var0 != null && var3 != null) {
            MinecraftClient var5 = getMc();
            if (var5.isOnThread() && var5.world == var0 && var5.player != null && !(var5.player.getY() < 0.0)) {
               if (var4.Z != var0) {
                  var4.IAsVoid(var0);
               }

               if (var0.getChunkManager().getWorldChunk(var1, var2) != null) {
                  try {
                     List var6 = var3.getSkyNibbles().stream().map(CandidateGroup::eAsX0_Type_n).toList();
                     boolean var7 = var4.a
                        .oAsBoolean(var1, var2, var0.countVerticalSections(), var0.getBottomSectionCoord(), var3.getInitedSky(), var3.getUninitedSky(), var6);
                     var4.J |= var7;
                  } catch (RuntimeException var81) {
                     ClientLog.hAsVoid("BaseFinding", "Motion Scanner packet classification failed; later light updates can recover", var81);
                  }
               }
            }
         }
      }
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.Z != mc.world) {
            this.IAsVoid(mc.world);
         }

         if (this.a.TAsBoolean(var1x -> {
            ChunkPos var3x = new ChunkPos(var1x);
            return this.Z.getChunkManager().getWorldChunk(var3x.x, var3x.z) != null;
         })) {
            this.d = this.a.jAsSet();
         }

         double var5;
         if (((var5 = mc.player.getY() - 0.0) == 0.0 ? 0 : (var5 < 0.0 ? -1 : 1)) >= 0 && this.J) {
            ChunkPos var1 = mc.player.getChunkPos();

            for (long var3 : this.a.RAsList(this.sensitivity.getValue().intValue(), var1.x, var1.z)) {
               this.zAsVoid(var3);
            }

            this.d = this.a.jAsSet();
            this.J = false;
         }
      }
   }

   private void zAsVoid(long var1) {
      ChunkPos var3 = new ChunkPos(var1);
      int var4 = var3.getStartX() + 8;
      int var5 = var3.getStartZ() + 8;
      ClientLog.XAsVoid("BaseFinding", "Motion Scanner discovery: chunk [{}, {}], center {}, {}", var3.x, var3.z, var4, var5);
      if (this.soundNotification.getValue()) {
         mc.player.playSound((SoundEvent)SoundEvents.BLOCK_NOTE_BLOCK_CHIME.value(), 1.2F, 1.2F);
      }

      if (this.chatNotifications.getValue()) {
         mc.getMessageHandler()
            .onGameMessage(Text.literal("(Motion Scanner) Base detected" + (this.showCoords.getValue() ? ": " + var4 + ", " + var5 : "")), false);
      }

      if (this.hUDNotification.getValue()) {
         NotificationHud.GAsW8().MAsVoid(this.getName(), "Base detected", NotificationHud.Type_u.INFO);
      }
   }

   private void JAsVoid() {
      if (this.isRuntimeActive() && this.isInGame() && this.Z == mc.world) {
         ChunkPos var1 = mc.player.getChunkPos();
         int var2 = this.highlightColor.getValue() & 16777215 | this.opacity.getValue().intValue() << 24;
         Camera var3 = mc.gameRenderer.getCamera();
         Vector3f var4 = new Vector3f(0.0F, 0.0F, -1.0F).rotate(var3.getRotation());
         Vec3d var5 = var3.getCameraPos().add((double)var4.x * 0.1, (double)var4.y * 0.1, (double)var4.z * 0.1);

         try {
            for (long var7 : this.d) {
               ChunkPos var9 = new ChunkPos(var7);
               if (Math.abs((long)var9.x - (long)var1.x) <= 17L
                  && Math.abs((long)var9.z - (long)var1.z) <= 17L
                  && this.Z.getChunkManager().getWorldChunk(var9.x, var9.z) != null) {
                  Box var10 = new Box(
                     (double)var9.getStartX(), -64.0, (double)var9.getStartZ(), (double)(var9.getStartX() + 16), 320.0, (double)(var9.getStartZ() + 16)
                  );
                  GizmoDrawing.box(var10, DrawStyle.filled(var2)).ignoreOcclusion();
                  if (this.showTracers.getValue() && this.tracersAlpha.getValue() > 0.0) {
                     GizmoDrawing.line(var5, var10.getCenter(), var2 & 16777215 | this.tracersAlpha.getValue().intValue() << 24, 1.0F).ignoreOcclusion();
                  }
               }
            }
         } catch (RuntimeException var11) {
            ClientLog.hAsVoid("BaseFinding", "Motion Scanner render failed; evidence retained", var11);
         }
      }
   }

   @Override
   public String getStatus() {
      return this.d.size() + " discoveries";
   }
}
