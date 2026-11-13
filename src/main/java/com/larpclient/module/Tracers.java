package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.ThemeSettings;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.DebugRender;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public class Tracers extends Module {
   private final BooleanSetting players = this.addSetting(new BooleanSetting("Players", "Trace players", true));
   private final BooleanSetting mobs = this.addSetting(new BooleanSetting("Mobs", "Trace hostile mobs", false));
   private final BooleanSetting animals = this.addSetting(new BooleanSetting("Animals", "Trace animals", false));
   private final NumberSetting range = this.addSetting(new NumberSetting("Range", "Max tracer range", 96.0, 16.0, 256.0, 8.0));
   private final NumberSetting maxEntities = this.addSetting(new NumberSetting("Max Entities", "Nearest targets drawn", 64.0, 8.0, 256.0, 8.0));
   private final ThemeSettings T = new ThemeSettings(var1 -> this.addSetting(var1), false);

   public Tracers() {
      super("Tracers", "Draw lines to entities", Category.RENDER, -1);
      WorldRenderEvents.BEFORE_DEBUG_RENDER.register((DebugRender)var1 -> this.lAsVoid());
   }

   private void lAsVoid() {
      if (this.isEnabled() && this.isOperational() && this.isInGame()) {
         Camera var1 = mc.gameRenderer.getCamera();
         Vector3f var2 = new Vector3f(0.0F, 0.0F, -1.0F).rotate(var1.getRotation());
         Vec3d var3 = var1.getCameraPos().add((double)var2.x * 0.5, (double)var2.y * 0.5, (double)var2.z * 0.5);
         float var4 = mc.getRenderTickCounter().getTickProgress(false);

         try {
            for (Entity var6 : ThemeSettings.jAsList(
               mc.world.getEntities(),
               mc.player,
               this.range.getValue(),
               this.maxEntities.getValue().intValue(),
               var1x -> var1x instanceof LivingEntity
                     && (
                        var1x instanceof PlayerEntity
                           ? this.players.getValue()
                           : (var1x instanceof Monster ? this.mobs.getValue() : var1x instanceof AnimalEntity && this.animals.getValue())
                     )
            )) {
               int var7 = var6 instanceof PlayerEntity ? -4221441 : (var6 instanceof Monster ? -943189 : -7677757);
               int var8 = this.T
                  .iAsInt(var7, (double)mc.player.distanceTo(var6), this.range.getValue(), ThemeSettings.sAsDouble((LivingEntity)var6), var6.getId());
               this.T.IAsVoid(var3, var6.getLerpedPos(var4).add(0.0, (double)var6.getHeight() * 0.5, 0.0), var8);
            }
         } catch (RuntimeException var9) {
            ClientLog.hAsVoid("ESP", "Tracer overlay failed", var9);
         }
      }
   }

   public List<Setting<?>> oAsList() {
      return List.of(this.players, this.mobs, this.animals);
   }

   public List<Setting<?>> JAsList() {
      ArrayList var1 = new ArrayList<>(List.of(this.range));
      var1.addAll(this.T.TAsList());
      var1.addAll(this.T.OAsList());
      return List.copyOf(var1);
   }

   public List<Setting<?>> yAsList() {
      return List.of(this.maxEntities);
   }
}
