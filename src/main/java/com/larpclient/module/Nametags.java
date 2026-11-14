package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.ScoreboardHealthResolver;
import com.larpclient.TextDrawing;
import com.larpclient.UiStyle;
import com.larpclient.ColorUtil;
import com.larpclient.ProjectionUtil;
import com.larpclient.ModuleList;
import com.larpclient.OpenScreen;
import com.larpclient.WebRenderer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAttachmentType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Nametags extends Module {
   private final BooleanSetting players = this.addSetting(new BooleanSetting("Players", "Show for players", true));
   private final BooleanSetting selfNametag = this.addSetting(
      new BooleanSetting("Self Nametag", "Show your own tag in third person or a detached camera", false)
   );
   private final BooleanSetting mobs = this.addSetting(new BooleanSetting("Mobs", "Show for hostile mobs", false));
   private final BooleanSetting animals = this.addSetting(new BooleanSetting("Animals", "Show for animals", false));
   private final BooleanSetting health = this.addSetting(new BooleanSetting("Health", "Show current health", true));
   private final BooleanSetting distance = this.addSetting(new BooleanSetting("Distance", "Show distance", true));
   private final BooleanSetting items = this.addSetting(new BooleanSetting("Items", "Show armor and held-item icons above the name", true));
   private final BooleanSetting ping = this.addSetting(new BooleanSetting("Ping", "Show player ping", true));
   private final NumberSetting range = this.addSetting(new NumberSetting("Range", "Max render range", 128.0, 8.0, 512.0, 8.0));
   private final NumberSetting scale = this.addSetting(new NumberSetting("Scale", "Nametag scale", 1.0, 0.5, 2.0, 0.1));
   private final NumberSetting height = this.addSetting(new NumberSetting("Height", "Gap above the entity", 0.25, 0.1, 1.5, 0.05));
   private final NumberSetting opacity = this.addSetting(new NumberSetting("Opacity", "Background opacity", 0.65, 0.0, 1.0, 0.05));
   private final BooleanSetting background = this.addSetting(new BooleanSetting("Background", "Draw label background", true));
   private final Set<Integer> f = new HashSet<>();

   public Nametags() {
      super("Nametags", "Show names, health and gear above entities", Category.RENDER, -1);
      this.opacity.visibleWhen(this.background::getValue);
      this.ping.visibleWhen(() -> this.players.getValue() || this.selfNametag.getValue());
      HudElementRegistry.addLast(Identifier.of("larp-client", "nametags"), this::kAsVoid);
      this.selfNametag.onChanged(var0 -> ClientLog.XAsVoid("Nametags", "Self nametag {} in third person or a detached camera", var0 ? "enabled" : "disabled"));
   }

   public boolean CAsBoolean(Entity var1) {
      return this.isEnabled() ? this.isOperational() && this.f.contains(var1.getId()) : false;
   }

   public List<Setting<?>> KAsList() {
      return List.of(this.players, this.selfNametag, this.mobs, this.animals);
   }

   public List<Setting<?>> sAsList() {
      return List.of(this.health, this.distance, this.items, this.ping);
   }

   public List<Setting<?>> EAsList() {
      return List.of(this.range, this.scale, this.height, this.background, this.opacity);
   }

   @Override
   public void onEnable() {
      ClientLog.XAsVoid("Nametags", "Replacement labels suppress native names and below-name scoreboard text");
   }

   @Override
   public void onDisable() {
      this.f.clear();
   }

   private boolean qAsBoolean(boolean var1, boolean var2) {
      return var1 ? this.selfNametag.getValue() && var2 : this.players.getValue();
   }

   private void kAsVoid(DrawContext var1, RenderTickCounter var2) {
      this.f.clear();
      if (this.isOperational() && this.isInGame() && !mc.options.hudHidden) {
         long var3 = OpenScreen.RAsLong();
         Camera var5 = mc.gameRenderer.getCamera();
         Vec3d var6 = var5.getCameraPos();
         Matrix4f var7 = WebRenderer.pAsMatrix4f(new Matrix4f());
         Vector3f var8 = var5.getRotation().transform(new Vector3f(0.0F, 0.025F * this.scale.getValue().floatValue(), 0.0F));
         int var9 = mc.getWindow().getScaledWidth();
         int var10 = mc.getWindow().getScaledHeight();
         float var12 = var2.getTickProgress(false);
         int var13 = 0;

         for (Entity var15 : mc.world.getEntities()) {
            if (var15 instanceof LivingEntity) {
               LivingEntity var16 = (LivingEntity)var15;
               if (var16.isAlive()
                  && (
                     var15 instanceof PlayerEntity
                        ? this.qAsBoolean(var15 == mc.player, var5.isThirdPerson())
                        : (var15 instanceof Monster ? this.mobs.getValue() : var15 instanceof AnimalEntity && this.animals.getValue())
                  )) {
                  double var17 = (double)mc.player.distanceTo(var15);
                  if (!(var17 > this.range.getValue())) {
                     Vec3d var19 = var15.getLerpedPos(var12);
                     Vec3d var20 = var15.getAttachments().getPointNullable(EntityAttachmentType.NAME_TAG, 0, var15.getLerpedYaw(var12));
                     double var21 = Math.max((double)var15.getHeight(), var20 == null ? 0.0 : var20.y);
                     ProjectionUtil.Type_e var23 = ProjectionUtil.vAsD5_Type_e(
                        var19.add(var20 == null ? 0.0 : var20.x, var21 + this.height.getValue(), var20 == null ? 0.0 : var20.z),
                        var6,
                        var7,
                        var8,
                        var9,
                        var10,
                        false
                     );
                     if (var23 != null) {
                        if (++var13 > 128) {
                           break;
                        }

                        ArrayList var24 = new ArrayList();
                        if (this.health.getValue()) {
                           var24.add((int)Math.ceil((double)ScoreboardHealthResolver.UAsFloat(var16)) + " HP");
                        }

                        if (this.distance.getValue()) {
                           var24.add((int)var17 + "m");
                        }

                        if (this.ping.getValue() && var15 instanceof PlayerEntity var25 && mc.getNetworkHandler() != null) {
                           PlayerListEntry var26 = mc.getNetworkHandler().getPlayerListEntry(var25.getUuid());
                           if (var26 != null) {
                              var24.add(var26.getLatency() + "ms");
                           }
                        }

                        String var41 = String.join(" · ", var24);
                        var1.getMatrices().pushMatrix();

                        try {
                           var1.getMatrices().translate(var23.x(), var23.y()).scale(var23.scale());
                           Text var42 = TextDrawing.ZAsText(
                              TextDrawing.sAsString(ModuleList.cAsString(var15.getUuid(), var15.getName().getString()), 160, TextDrawing.Type_x.MEDIUM),
                              TextDrawing.Type_x.MEDIUM
                           );
                           Text var27 = TextDrawing.ZAsText(TextDrawing.LAsString(var41.trim(), 180), TextDrawing.Type_x.REGULAR);
                           int var28 = this.background.getValue() ? ColorUtil.XAsInt(UiStyle.M, this.opacity.getValue()) : 0;
                           int var29 = var41.isEmpty() ? -16 : -30;
                           this.IAsVoid(var1, var42, var29, UiStyle.u, var28);
                           if (!var41.isEmpty()) {
                              this.IAsVoid(var1, var27, -16, -3090461, var28);
                           }

                           if (this.items.getValue()) {
                              ArrayList var30 = new ArrayList(6);

                              for (EquipmentSlot var34 : new EquipmentSlot[]{
                                 EquipmentSlot.MAINHAND, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.OFFHAND
                              }) {
                                 ItemStack var35 = var16.getEquippedStack(var34);
                                 if (!var35.isEmpty()) {
                                    var30.add(var35);
                                 }
                              }

                              int var43 = var29 - 20;

                              for (int var44 = 0; var44 < var30.size(); var44++) {
                                 var1.drawItem(
                                    var16,
                                    (ItemStack)var30.get(var44),
                                    Math.round(((float)var44 - (float)(var30.size() - 1) / 2.0F) * 18.0F) - 8,
                                    var43,
                                    var15.getId()
                                 );
                              }
                           }

                           this.f.add(var15.getId());
                        } catch (RuntimeException var391) {
                           ClientLog.hAsVoid("Render", "Nametag submission failed", var391);
                        } finally {
                           var1.getMatrices().popMatrix();
                        }
                     }
                  }
               }
            }
         }

         OpenScreen.aAsVoid("Nametags", var3);
      }
   }

   private void IAsVoid(DrawContext var1, Text var2, int var3, int var4, int var5) {
      int var6 = mc.textRenderer.getWidth(var2);
      int var7 = -var6 / 2;
      if (var5 != 0) {
         var1.fill(var7 - 1, var3 - 1, var7 + var6 + 1, var3 + 9, var5);
      }

      var1.createNewRootLayer();
      var1.drawText(mc.textRenderer, var2, var7, var3, var4, true);
   }
}
