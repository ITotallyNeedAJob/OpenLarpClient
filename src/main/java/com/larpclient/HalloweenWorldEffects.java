package com.larpclient;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.s2c.play.EntityDamageS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.dimension.DimensionType.Skybox;

public final class HalloweenWorldEffects {
   private final HalloweenCinders H = new HalloweenCinders();
   private final wg C = new wg();
   private Object U;
   private Object x;
   private Vec3d F;
   private int B = -1;
   private int u;
   private int f = -1;
   private boolean K;
   private boolean O;

   public HalloweenWorldEffects() {
   }

   private boolean fAsBoolean(MinecraftClient var1, boolean var2) {
      if (var1.world != null && var1.player != null) {
         Vec3d var3 = var1.gameRenderer.getCamera().getCameraPos();
         boolean var4 = var1.options.getPerspective().isFirstPerson();
         if (this.U != var1.world || this.x != var1.player || this.B != BloomQuality.J || this.F != null && this.F.squaredDistanceTo(var3) > 256.0) {
            this.bAsVoid();
            this.U = var1.world;
            this.x = var1.player;
            this.B = BloomQuality.J;
            if (var2) {
               WeaponTrailRenderer.LAsVoid();
            }

            ClientLog.XAsVoid("Halloween", "World effects initialized; reload and relocation clear transient geometry");
         }

         if (var2 && var4 != this.O) {
            WeaponTrailRenderer.LAsVoid();
         }

         this.O = var4;
         this.F = var3;
         return true;
      } else {
         this.bAsVoid();
         return false;
      }
   }

   public void aAsVoid(MinecraftClient var1, CinematicQuality var2, boolean var3, boolean var4) {
      if (this.fAsBoolean(var1, var4)) {
         if (var3 && var1.world.getDimension().skybox() == Skybox.OVERWORLD) {
            if (this.u++ % 5 == 0) {
               BlockPos var5 = BlockPos.ofFloored(this.F);
               this.K = var1.world.isChunkLoaded(var5) && var1.world.isSkyVisible(var5);
            }

            this.H.YAsVoid(this.F, var2, this.K);
         } else {
            this.H.jAsVoid();
         }

         if (var4) {
            this.C.EAsVoid();
            int var6 = var1.player.getInventory().getSelectedSlot();
            if (var6 != this.f || !this.O || !var1.player.isAlive()) {
               WeaponTrailRenderer.LAsVoid();
            }

            this.f = var6;
            WeaponTrailRenderer.PAsVoid(mb.dAsDouble(), 0.22);
         } else {
            this.C.yAsVoid();
         }
      }
   }

   public void fAsVoid(MinecraftClient var1, EntityDamageS2CPacket var2, CinematicQuality var3) {
      if (this.fAsBoolean(var1, true) && var2.sourceCauseId() == var1.player.getId()) {
         Entity var4 = var1.world.getEntityById(var2.entityId());
         if (var4 instanceof LivingEntity var5
            && var4 != var1.player
            && !var4.isRemoved()
            && !(var4.getEntityPos().squaredDistanceTo(var1.player.getEntityPos()) > 1600.0)) {
            Vec3d var6 = var5.getEntityPos().add(0.0, (double)var5.getHeight() * 0.6, 0.0);
            this.C.VAsVoid(var6, var6.subtract(var1.player.getEyePos()).normalize(), var3);
         }
      }
   }

   public void EAsVoid(float var1, CameraRenderState var2, boolean var3, boolean var4) {
      MinecraftClient var5 = MinecraftClient.getInstance();
      if (this.U == var5.world && this.x == var5.player && this.B == BloomQuality.J) {
         float var6 = var5.isPaused() ? 1.0F : var1;
         if (var3) {
            this.H.pAsVoid(var6, var2);
         }

         if (var4) {
            this.C.RAsVoid(var6, var2);
         }
      } else {
         this.bAsVoid();
      }
   }

   public void SAsVoid(EffectGeometryRenderer var1, boolean var2, boolean var3, boolean var4) {
      if (this.B == BloomQuality.J) {
         if (var3) {
            this.H.cAsVoid(var1, var2);
         }

         if (var4) {
            this.C.oAsVoid(var1, var2);
         }
      }
   }

   public boolean pAsBoolean(boolean var1, boolean var2) {
      return var1 && this.H.eAsBoolean() || var2 && this.C.SAsBoolean();
   }

   public boolean uAsBoolean(boolean var1, boolean var2) {
      return var1 && this.H.LAsBoolean() || var2 && this.C.gAsBoolean();
   }

   public void xAsVoid() {
      this.H.jAsVoid();
   }

   public void hAsVoid() {
      this.C.yAsVoid();
   }

   public void bAsVoid() {
      this.H.jAsVoid();
      this.C.yAsVoid();
      this.U = this.x = null;
      this.F = null;
      this.B = -1;
      this.u = 0;
      this.f = -1;
      this.K = false;
   }
}
