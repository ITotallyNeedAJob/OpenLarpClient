package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.ThemeSettings;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.DebugRender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;

public class ItemESP extends Module {
   private final NumberSetting range = this.addSetting(new NumberSetting("Range", "Max render range", 48.0, 8.0, 128.0, 8.0));
   private final NumberSetting maxItems = this.addSetting(new NumberSetting("Max Items", "Nearest drops drawn", 128.0, 16.0, 512.0, 16.0));
   private final ModeSetting mode = this.addSetting(new ModeSetting("Mode", "Drop highlight style", "Corners", "Box", "Corners", "Filled"));
   private final BooleanSetting xPOrbs = this.addSetting(new BooleanSetting("XP Orbs", "Include experience orbs", false));
   private final BooleanSetting labels = this.addSetting(new BooleanSetting("Labels", "Item name, count and distance within 24 blocks", true));
   private final ThemeSettings d = new ThemeSettings(var1 -> this.addSetting(var1));

   public ItemESP() {
      super("ItemESP", "Highlight dropped items and experience orbs", Category.RENDER, -1);
      this.d.DAsVoid(() -> true, () -> true, () -> true, () -> this.mode.pAsBoolean("Filled"));
      WorldRenderEvents.BEFORE_DEBUG_RENDER.register((DebugRender)var1 -> this.RAsVoid());
   }

   private void RAsVoid() {
      if (this.isEnabled() && this.isOperational() && this.isInGame()) {
         float var1 = mc.getRenderTickCounter().getTickProgress(false);

         try {
            int var2 = 0;

            for (Entity var4 : ThemeSettings.jAsList(
               mc.world.getEntities(),
               mc.player,
               this.range.getValue(),
               this.maxItems.getValue().intValue(),
               var1x -> var1x instanceof ItemEntity || this.xPOrbs.getValue() && var1x instanceof ExperienceOrbEntity
            )) {
               double var5 = (double)mc.player.distanceTo(var4);
               int var7 = this.d.iAsInt(var4 instanceof ItemEntity ? -3627265 : -7677757, var5, this.range.getValue(), 1.0, var4.getId());
               Box var8 = ThemeSettings.GAsBox(var4, var1);
               this.d.AAsVoid(var8, this.mode.getValue(), var7);
               if (this.labels.getValue() && var5 <= 24.0 && var4 instanceof ItemEntity) {
                  ItemEntity var9 = (ItemEntity)var4;
                  if (var2++ < 24) {
                     ItemStack var10 = var9.getStack();
                     this.d
                        .NAsVoid(
                           var8.getCenter().add(0.0, var8.getLengthY() * 0.5 + 0.18, 0.0),
                           var10.getName().getString() + " ×" + var10.getCount() + " · " + (int)var5 + "m",
                           var7
                        );
                  }
               }
            }
         } catch (RuntimeException var111) {
            ClientLog.hAsVoid("ESP", "Item overlay failed", var111);
         }
      }
   }

   public List<Setting<?>> GAsList() {
      return List.of(this.mode, this.range, this.xPOrbs, this.labels);
   }

   public List<Setting<?>> rAsList() {
      ArrayList var1 = new ArrayList<>(this.d.TAsList());
      var1.addAll(this.d.OAsList());
      var1.addAll(this.d.qAsList());
      return List.copyOf(var1);
   }

   public List<Setting<?>> CAsList() {
      return List.of(this.maxItems);
   }
}
