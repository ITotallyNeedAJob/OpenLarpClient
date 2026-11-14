package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.LarpClient;
import com.larpclient.WorldTextRenderer;
import com.larpclient.ColorUtil;
import com.larpclient.MediaSession;
import com.larpclient.WebRenderer;
import com.larpclient.XRayRenderer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityAnimationS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityDamageS2CPacket;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public final class DamageMarkers extends Module {
   private static DamageMarkers q;
   private final NumberSetting size = this.addSetting(new NumberSetting("Size", "Damage text size", 1.0, 0.5, 2.5, 0.1));
   private final NumberSetting lifeTime = this.addSetting(new NumberSetting("Life Time", "Marker lifetime in milliseconds", 1000.0, 300.0, 2500.0, 100.0));
   private final BooleanSetting selfDamage = this.addSetting(new BooleanSetting("Self Damage", "Show your own health loss", true));
   private final BooleanSetting friendHits = this.addSetting(new BooleanSetting("Friend Hits", "Show damage attributed to friends", true));
   private final ModeSetting color = this.addSetting(new ModeSetting("Color", "Color by damage or a chosen tint", "Damage", "Damage", "Custom"));
   private final ColorSetting customColor = this.addSetting(new ColorSetting("Custom Color", "Chosen damage marker tint", -48060));
   private final XRayRenderer x = new XRayRenderer(21463920019784013L);
   private final HashMap<LivingEntity, Double> X = new HashMap<>();
   private WorldTextRenderer l;
   private Object d;
   private Object w;
   private Vec3d H;

   public DamageMarkers() {
      super("Damage Markers", "Show dealt damage and your own health loss as floating numbers", Category.VISUALS, -1);
      q = this;
      this.customColor.visibleWhen(() -> this.color.pAsBoolean("Custom"));
   }

   public static DamageMarkers DAsDamageMarkers() {
      return q;
   }

   public List<Setting<?>> rAsList() {
      return List.of(this.selfDamage, this.friendHits);
   }

   public List<Setting<?>> dAsList() {
      return List.of(this.size, this.lifeTime, this.color, this.customColor);
   }

   @Override
   public void onEnable() {
      this.gAsVoid();
      ClientLog.GAsVoid("DamageMarkers", "Enabled; observed health loss, 8-block trigger range, 256 marker budget");
   }

   @Override
   public void onDisable() {
      this.gAsVoid();
   }

   public void gAsVoid() {
      this.x.aAsVoid();
      this.X.clear();
      this.d = this.w = null;
      this.H = null;
      if (this.l != null) {
         this.l.close();
         this.l = null;
      }
   }

   private boolean DAsBoolean(Entity var1) {
      LarpClient var2 = LarpClient.CAsLarpClient();
      if (var1 instanceof PlayerEntity && var2 != null) {
         MCFriend var3 = var2.mAsF().EAsModule(MCFriend.class);
         return var3 != null && var3.SAsBoolean(var1.getName().getString());
      } else {
         return false;
      }
   }

   private boolean CAsBoolean() {
      if (this.isRuntimeActive() && this.isInGame()) {
         if (this.d != getMc().world || this.w != this.getPlayer()) {
            this.gAsVoid();
            this.d = getMc().world;
            this.w = this.getPlayer();
         }

         return true;
      } else {
         return false;
      }
   }

   public void SAsVoid(Entity var1) {
      if (this.isEnabled() && this.CAsBoolean() && var1 instanceof LivingEntity var2 && var1 != this.getPlayer()) {
         this.x.gAsVoid(var1.getId(), var1, var2.getHealth(), MediaSession.dAsDouble());
      }
   }

   public void qAsVoid(EntityDamageS2CPacket var1) {
      if (this.isEnabled() && this.CAsBoolean()) {
         Entity var2 = getMc().world.getEntityById(var1.entityId());
         if (var2 instanceof LivingEntity var3 && (var2 != this.getPlayer() || this.selfDamage.getValue())) {
            Entity var4 = var1.createDamageSource(getMc().world).getAttacker();
            if (var2 == this.getPlayer() || var4 == this.getPlayer() || this.friendHits.getValue() && this.DAsBoolean(var4)) {
               this.x.gAsVoid(var2.getId(), var2, var3.getHealth(), MediaSession.dAsDouble());
            }
         }
      }
   }

   public void AAsVoid(EntityAnimationS2CPacket var1) {
      if (this.isEnabled()
         && this.CAsBoolean()
         && this.friendHits.getValue()
         && (var1.getAnimationId() == 0 || var1.getAnimationId() == 3)
         && getMc().world.getEntityById(var1.getEntityId()) instanceof LivingEntity var3
         && this.DAsBoolean(var3)
         && (this.X.size() < 128 || this.X.containsKey(var3))) {
         this.X.put(var3, MediaSession.dAsDouble() + 2.5);
      }
   }

   @Override
   public void onTick() {
      if (!this.CAsBoolean()) {
         this.gAsVoid();
      } else {
         Vec3d var1 = this.getPlayer().getEntityPos();
         double var12;
         if (this.H != null && ((var12 = var1.squaredDistanceTo(this.H) - 256.0) == 0.0 ? 0 : (var12 < 0.0 ? -1 : 1)) > 0) {
            this.gAsVoid();
            this.d = getMc().world;
            this.w = this.getPlayer();
         }

         this.H = var1;
         double var2 = MediaSession.dAsDouble();
         this.X.entrySet().removeIf(var2xx -> var2 > var2xx.getValue() || var2xx.getKey().isRemoved());
         this.x.BAsVoid(var2, var0 -> ((Entity)var0).isRemoved());

         for (Entity var5 : getMc().world.getEntities()) {
            if (var5 instanceof LivingEntity) {
               LivingEntity var6 = (LivingEntity)var5;
               if (!var5.isRemoved() && !(var5.squaredDistanceTo(this.getPlayer()) > 64.0)) {
                  Object var7 = var6.getAttacker();
                  if (var7 == null) {
                     var7 = var6.getAttackingPlayer();
                  }

                  int var8 = var5 == this.getPlayer() ? (this.selfDamage.getValue() ? 1 : 0) : (var7 == this.getPlayer() ? 1 : 0);
                  if (var5 != this.getPlayer() && this.friendHits.getValue()) {
                     var8 |= this.DAsBoolean((Entity)var7) ? 1 : 0;
                     if (var8 == 0) {
                        for (LivingEntity var10 : this.X.keySet()) {
                           if (var5.squaredDistanceTo(var10) <= 16.0) {
                              var8 = 1;
                              break;
                           }
                        }
                     }
                  }

                  if (var5 == this.getPlayer() && !this.selfDamage.getValue()) {
                     this.x.dAsVoid(var5.getId());
                  }

                  Vec3d var11 = var5.getEntityPos();
                  this.x
                     .zAsVoid(
                        var5.getId(),
                        var5,
                        var6.getHealth(),
                        var11.x,
                        var11.y,
                        var11.z,
                        (double)var6.getHeight(),
                        var8 != 0,
                        var2,
                        this.lifeTime.getValue() / 1000.0
                     );
               }
            }
         }
      }
   }

   public void vAsVoid(CameraRenderState var1) {
      if (this.isEnabled() && this.CAsBoolean()) {
         if (this.l == null) {
            this.l = new WorldTextRenderer();
         }

         ArrayList var2 = new ArrayList();
         double var3 = MediaSession.RAsDouble();
         Vector3f var5 = new Vector3f(0.0F, 0.0F, -1.0F).rotate(var1.orientation);
         double var6 = Math.max(0.01, (double)Math.abs(WebRenderer.CAsMatrix4fc().m11()));
         double var8 = (double)Math.max(1, getMc().getWindow().getScaledHeight());

         for (XRayRenderer.Type_b var11 : this.x.vAsList(var3)) {
            double var12 = var11.x() - var1.pos.x;
            double var14 = var11.y() - var1.pos.y;
            double var16 = var11.z() - var1.pos.z;
            double var18 = var12 * (double)var5.x + var14 * (double)var5.y + var16 * (double)var5.z;
            if (!(var18 <= 0.05)) {
               int var20 = this.color.pAsBoolean("Custom") ? this.customColor.getValue() : XRayRenderer.UAsInt(var11.damage());
               double var21 = 0.5 * var18 / (var8 * var6 * 0.01890625);
               var2.add(
                  new WorldTextRenderer.Type_d(
                     var11.text(), var11.x(), var11.y(), var11.z(), var21 * this.size.getValue() * var11.scale(), ColorUtil.XAsInt(var20, var11.opacity())
                  )
               );
            }
         }

         this.l.nAsVoid(var2, var1);
      }
   }

   public void HAsVoid(MatrixStack var1, CameraRenderState var2) {
      if (this.isEnabled() && this.isRuntimeActive() && this.l != null && this.d == getMc().world) {
         this.l.IAsVoid(var1, var2, true);
      }
   }

   public void MAsVoid() {
      if (this.l != null) {
         this.l.GAsVoid();
      }
   }
}
