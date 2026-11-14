package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.GuiTextureDrawing;
import com.larpclient.TextDrawing;
import com.larpclient.ThemeSettings;
import com.larpclient.ColorUtil;
import com.larpclient.ProjectionUtil;
import com.larpclient.OpenScreen;
import com.larpclient.WebRenderer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.DebugRender;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.block.entity.CrafterBlockEntity;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.block.entity.DropperBlockEntity;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.block.entity.SmokerBlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import net.minecraft.world.chunk.WorldChunk;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class StorageESP extends Module {
   private final BooleanSetting chests = this.addSetting(new BooleanSetting("Chests", "Highlight regular and trapped chests", true));
   private final BooleanSetting enderChests = this.addSetting(new BooleanSetting("Ender Chests", "Highlight ender chests", true));
   private final BooleanSetting shulkers = this.addSetting(new BooleanSetting("Shulkers", "Highlight shulker boxes", true));
   private final BooleanSetting barrels = this.addSetting(new BooleanSetting("Barrels", "Highlight barrels", false));
   private final BooleanSetting spawners = this.addSetting(new BooleanSetting("Spawners", "Highlight mob spawners", true));
   private final BooleanSetting hoppers = this.addSetting(new BooleanSetting("Hoppers", "Highlight hoppers", false));
   private final BooleanSetting furnaces = this.addSetting(new BooleanSetting("Furnaces", "Highlight furnaces", false));
   private final BooleanSetting blastFurnaces = this.addSetting(new BooleanSetting("Blast Furnaces", "Highlight blast furnaces", false));
   private final BooleanSetting smokers = this.addSetting(new BooleanSetting("Smokers", "Highlight smokers", false));
   private final BooleanSetting dispensers = this.addSetting(new BooleanSetting("Dispensers", "Highlight dispensers", false));
   private final BooleanSetting droppers = this.addSetting(new BooleanSetting("Droppers", "Highlight droppers", false));
   private final BooleanSetting crafters = this.addSetting(new BooleanSetting("Crafters", "Highlight crafters", false));
   private final BooleanSetting brewingStands = this.addSetting(new BooleanSetting("Brewing Stands", "Highlight brewing stands", false));
   private final BooleanSetting beehives = this.addSetting(new BooleanSetting("Beehives", "Highlight beehives", false));
   private final BooleanSetting decoratedPots = this.addSetting(new BooleanSetting("Decorated Pots", "Highlight decorated pots", false));
   private final BooleanSetting chiseledBookshelves = this.addSetting(new BooleanSetting("Chiseled Bookshelves", "Highlight chiseled bookshelves", false));
   private final BooleanSetting jukeboxes = this.addSetting(new BooleanSetting("Jukeboxes", "Highlight jukeboxes", false));
   private final NumberSetting range = this.addSetting(new NumberSetting("Range", "Loaded storage scan radius", 64.0, 16.0, 128.0, 8.0));
   private final NumberSetting maxBlocks = this.addSetting(new NumberSetting("Max Blocks", "Nearest storage blocks drawn", 256.0, 16.0, 512.0, 16.0));
   private final ModeSetting mode = this.addSetting(new ModeSetting("Mode", "Storage highlight style", "Filled", "Box", "Corners", "Filled"));
   private final BooleanSetting labels = this.addSetting(new BooleanSetting("Labels", "Show storage names above highlighted blocks", true));
   private final NumberSetting labelRange = this.addSetting(
      new NumberSetting("Label Range", "Maximum name-tag distance, within the storage scan range", 128.0, 16.0, 128.0, 8.0)
   );
   private final NumberSetting maxLabels = this.addSetting(new NumberSetting("Max Labels", "Nearest storage name tags drawn", 64.0, 1.0, 128.0, 1.0));
   private final NumberSetting labelScale = this.addSetting(
      new NumberSetting("Label Scale", "Name-tag size on screen, the same at every distance", 1.0, 0.5, 2.0, 0.1)
   );
   private final BooleanSetting labelIcons = this.addSetting(new BooleanSetting("Label Icons", "Show the block item beside each storage name", true));
   private final BooleanSetting hideOverlaps = this.addSetting(new BooleanSetting("Hide Overlaps", "Skip farther name tags that would cover nearer ones", true));
   private final BooleanSetting boxes = this.addSetting(new BooleanSetting("Boxes", "Draw storage highlight boxes", true));
   private final BooleanSetting tracers = this.addSetting(new BooleanSetting("Tracers", "Draw lines to selected storage blocks", false));
   private final ModeSetting tracerOrigin = this.addSetting(
      new ModeSetting("Tracer Origin", "Start at the camera crosshair or player feet", "Crosshair", "Crosshair", "Feet")
   );
   private final NumberSetting tracerRange = this.addSetting(
      new NumberSetting("Tracer Range", "Maximum line distance, within scan range", 64.0, 16.0, 128.0, 8.0)
   );
   private final NumberSetting maxTracers = this.addSetting(new NumberSetting("Max Tracers", "Nearest selected storage lines drawn", 64.0, 1.0, 128.0, 1.0));
   private final ThemeSettings z = new ThemeSettings(var1x -> this.addSetting(var1x));
   private final List<BlockEntity> c = new ArrayList<>();
   private final Map<Block, ItemStack> m = new HashMap<>();
   private ClientWorld X;
   private int T;
   private static StorageESP K;
   private static boolean l;
   private static final int Q = 16;
   private static final int k = 12;

   public StorageESP() {
      super("StorageESP", "Highlight storage blocks", Category.RENDER, -1);

      for (Setting var2 : List.of(this.labelRange, this.maxLabels, this.labelScale, this.labelIcons, this.hideOverlaps)) {
         var2.visibleWhen(this.labels::getValue);
      }

      for (Setting var6 : List.of(this.tracerOrigin, this.tracerRange, this.maxTracers)) {
         var6.visibleWhen(this.tracers::getValue);
      }

      this.mode.visibleWhen(this.boxes::getValue);
      this.z
         .DAsVoid(
            () -> this.boxes.getValue() || this.tracers.getValue() || this.labels.getValue(),
            () -> this.boxes.getValue() || this.tracers.getValue(),
            () -> this.boxes.getValue() || this.tracers.getValue() || this.labels.getValue(),
            () -> this.boxes.getValue() && this.mode.pAsBoolean("Filled")
         );

      for (Setting var7 : this.WAsList()) {
         ((BooleanSetting)var7).onChanged(var1x -> this.ZAsVoid());
      }

      this.range.onChanged(var1x -> this.ZAsVoid());
      this.maxBlocks.onChanged(var1x -> this.ZAsVoid());
      WorldRenderEvents.BEFORE_DEBUG_RENDER.register((DebugRender)var1x -> this.OAsVoid());
      K = this;
      if (!l) {
         l = true;
         HudElementRegistry.addLast(Identifier.of("larp-client", "storage_esp_labels"), (var0, var1x) -> K.TAsVoid(var0, var1x));
      }
   }

   @Override
   public void onEnable() {
      this.T = 0;
      this.c.clear();
      this.X = null;
      ClientLog.XAsVoid("ESP", "StorageESP enabled; received loaded storage only, tracers={}", this.tracers.getValue());
   }

   @Override
   public void onDisable() {
      ClientLog.XAsVoid("ESP", "StorageESP cleanup; discarded {} cached blocks", this.c.size());
      this.c.clear();
      this.m.clear();
      this.X = null;
   }

   public List<Setting<?>> WAsList() {
      return List.of(
         this.chests,
         this.enderChests,
         this.shulkers,
         this.barrels,
         this.spawners,
         this.hoppers,
         this.furnaces,
         this.blastFurnaces,
         this.smokers,
         this.dispensers,
         this.droppers,
         this.crafters,
         this.brewingStands,
         this.beehives,
         this.decoratedPots,
         this.chiseledBookshelves,
         this.jukeboxes
      );
   }

   public List<Setting<?>> bAsList() {
      return List.of(this.range, this.maxBlocks);
   }

   public List<Setting<?>> DAsList() {
      return List.of(this.range, this.boxes, this.mode, this.labels, this.tracers, this.tracerOrigin);
   }

   public List<Setting<?>> PAsList() {
      return List.of(this.maxBlocks, this.labelRange, this.maxLabels, this.tracerRange, this.maxTracers);
   }

   public List<Setting<?>> wAsList() {
      return List.of(this.tracers, this.tracerOrigin, this.tracerRange, this.maxTracers);
   }

   public List<Setting<?>> QAsList() {
      return List.of(this.boxes, this.mode);
   }

   public List<Setting<?>> AAsList() {
      ArrayList var1 = new ArrayList<>(this.z.TAsList());
      var1.addAll(this.z.OAsList());
      var1.addAll(this.z.qAsList());
      var1.addAll(List.of(this.labelScale, this.labelIcons, this.hideOverlaps));
      return List.copyOf(var1);
   }

   private void ZAsVoid() {
      this.T = 0;
      this.c.clear();
   }

   public static Vec3d HAsVec3d(Vec3d var0, Quaternionf var1, Vec3d var2, String var3) {
      if (var3.equals("Feet")) {
         return var2.add(0.0, 0.1, 0.0);
      } else {
         Vector3f var4 = new Vector3f(0.0F, 0.0F, -1.0F).rotate(var1);
         return var0.add((double)var4.x * 0.5, (double)var4.y * 0.5, (double)var4.z * 0.5);
      }
   }

   public List<Setting<?>> EAsList() {
      return List.of(this.labels, this.labelRange, this.maxLabels, this.labelScale, this.labelIcons, this.hideOverlaps);
   }

   public static String CAsString(BlockEntity var0) {
      return var0.getCachedState().getBlock().getName().getString();
   }

   public static BlockPos GAsBlockPos(BlockEntity var0) {
      BlockState var1 = var0.getCachedState();
      return var1.getBlock() instanceof ChestBlock && var1.contains(ChestBlock.CHEST_TYPE) && var1.get(ChestBlock.CHEST_TYPE) != ChestType.SINGLE
         ? ChestBlock.getPosInFrontOf(var0.getPos(), var1)
         : null;
   }

   public static String pAsString(BlockEntity var0, boolean var1) {
      return var1 ? CAsString(var0) + " x2" : CAsString(var0);
   }

   public static String wAsString(String var0, double var1) {
      String var3 = (int)var1 + "m";
      return var0 != null && !var0.isBlank() ? var0 + " · " + var3 : var3;
   }

   private boolean FAsBoolean(BlockEntity var1) {
      if (var1 instanceof DropperBlockEntity) {
         return this.droppers.getValue();
      } else {
         return var1 instanceof DispenserBlockEntity
            ? this.dispensers.getValue()
            : var1 instanceof ChestBlockEntity && this.chests.getValue()
               || var1 instanceof EnderChestBlockEntity && this.enderChests.getValue()
               || var1 instanceof ShulkerBoxBlockEntity && this.shulkers.getValue()
               || var1 instanceof BarrelBlockEntity && this.barrels.getValue()
               || var1 instanceof MobSpawnerBlockEntity && this.spawners.getValue()
               || var1 instanceof HopperBlockEntity && this.hoppers.getValue()
               || var1 instanceof FurnaceBlockEntity && this.furnaces.getValue()
               || var1 instanceof BlastFurnaceBlockEntity && this.blastFurnaces.getValue()
               || var1 instanceof SmokerBlockEntity && this.smokers.getValue()
               || var1 instanceof CrafterBlockEntity && this.crafters.getValue()
               || var1 instanceof BrewingStandBlockEntity && this.brewingStands.getValue()
               || var1 instanceof BeehiveBlockEntity && this.beehives.getValue()
               || var1 instanceof DecoratedPotBlockEntity && this.decoratedPots.getValue()
               || var1 instanceof ChiseledBookshelfBlockEntity && this.chiseledBookshelves.getValue()
               || var1 instanceof JukeboxBlockEntity && this.jukeboxes.getValue();
      }
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.X != mc.world) {
            this.c.clear();
            this.X = mc.world;
            this.T = 0;
         }

         int var10000 = this.T++;
         if (var10000 % 10 == 0) {
            this.c.clear();
            int var1 = (int)Math.ceil(this.range.getValue() / 16.0) + 1;
            ChunkPos var2 = mc.player.getChunkPos();

            for (int var3 = var2.x - var1; var3 <= var2.x + var1; var3++) {
               for (int var4 = var2.z - var1; var4 <= var2.z + var1; var4++) {
                  WorldChunk var5 = mc.world.getChunkManager().getWorldChunk(var3, var4);
                  if (var5 != null) {
                     for (BlockEntity var7 : var5.getBlockEntities().values()) {
                        if (!var7.isRemoved()
                           && this.FAsBoolean(var7)
                           && mc.player.squaredDistanceTo(Vec3d.ofCenter(var7.getPos())) <= this.range.getValue() * this.range.getValue()) {
                           this.c.add(var7);
                        }
                     }
                  }
               }
            }

            this.c.sort(Comparator.comparingDouble(var0 -> mc.player.squaredDistanceTo(Vec3d.ofCenter(var0.getPos()))));
            if (this.c.size() > this.maxBlocks.getValue().intValue()) {
               this.c.subList(this.maxBlocks.getValue().intValue(), this.c.size()).clear();
            }
         }
      }
   }

   private boolean ZAsBoolean(BlockEntity var1) {
      if (!var1.isRemoved() && this.FAsBoolean(var1)) {
         BlockPos var2 = var1.getPos();
         WorldChunk var3 = mc.world.getChunkManager().getWorldChunk(var2.getX() >> 4, var2.getZ() >> 4);
         return var3 != null && var3.getBlockEntities().get(var2) == var1;
      } else {
         return false;
      }
   }

   private Box JAsBox(BlockEntity var1) {
      BlockPos var2 = var1.getPos();
      VoxelShape var3 = var1.getCachedState().getOutlineShape(mc.world, var2);
      return (var3.isEmpty() ? new Box(0.0, 0.0, 0.0, 1.0, 1.0, 1.0) : var3.getBoundingBox()).offset(var2).expand(0.003);
   }

   private int wAsInt(BlockEntity var1, double var2) {
      int var4 = var1 instanceof MobSpawnerBlockEntity
         ? -943189
         : (var1 instanceof EnderChestBlockEntity ? -5071873 : (var1 instanceof ShulkerBoxBlockEntity ? -1400362 : -1521772));
      return this.z.iAsInt(var4, var2, this.range.getValue(), 1.0, var1.getPos().hashCode());
   }

   private void OAsVoid() {
      if (this.isEnabled() && this.isRuntimeActive() && this.isInGame() && this.X == mc.world) {
         try {
            int var1 = 0;
            int var2 = 0;
            Camera var3 = mc.gameRenderer.getCamera();
            Vec3d var4 = this.tracers.getValue()
               ? HAsVec3d(var3.getCameraPos(), var3.getRotation(), mc.player.getEntityPos(), this.tracerOrigin.getValue())
               : null;

            for (BlockEntity var6 : this.c) {
               if (this.ZAsBoolean(var6)) {
                  double var7 = mc.player.getEntityPos().distanceTo(Vec3d.ofCenter(var6.getPos()));
                  if (!(var7 >= this.range.getValue())) {
                     if (var1++ >= this.maxBlocks.getValue().intValue()) {
                        break;
                     }

                     Box var9 = this.JAsBox(var6);
                     int var10 = this.wAsInt(var6, var7);
                     if (this.boxes.getValue()) {
                        this.z.AAsVoid(var9, this.mode.getValue(), var10);
                     }

                     if (var4 != null && var7 <= this.tracerRange.getValue() && var2++ < this.maxTracers.getValue().intValue() && var10 >>> 24 != 0) {
                        this.z.IAsVoid(var4, var9.getCenter(), var10);
                     }
                  }
               }
            }
         } catch (RuntimeException var111) {
            ClientLog.hAsVoid("ESP", "Storage overlay failed", var111);
         }
      }
   }

   private void TAsVoid(DrawContext var1, RenderTickCounter var2) {
      if (this.labels.getValue() && this.isRuntimeActive() && this.isInGame() && this.X == mc.world && !mc.options.hudHidden) {
         long var3 = OpenScreen.RAsLong();

         try {
            Camera var5 = mc.gameRenderer.getCamera();
            Vec3d var6 = var5.getCameraPos();
            Matrix4f var7 = WebRenderer.pAsMatrix4f(new Matrix4f());
            Vector3f var8 = var5.getRotation().transform(new Vector3f(0.0F, 1.0F, 0.0F));
            int var9 = mc.getWindow().getScaledWidth();
            int var10 = mc.getWindow().getScaledHeight();
            float var12 = this.labelScale.getValue().floatValue();
            int var13 = this.maxLabels.getValue().intValue();
            HashSet var14 = new HashSet();
            ArrayList<float[]> var15 = new ArrayList<>();
            ArrayList var16 = new ArrayList();

            for (BlockEntity var18 : this.c) {
               if (var16.size() >= var13) {
                  break;
               }

               if (this.ZAsBoolean(var18)) {
                  BlockPos var19 = var18.getPos();
                  double var20 = mc.player.getEntityPos().distanceTo(Vec3d.ofCenter(var19));
                  if (!(var20 >= this.range.getValue()) && !(var20 > this.labelRange.getValue())) {
                     BlockPos var22;
                     Box var23;
                     var22 = GAsBlockPos(var18);
                     var23 = this.JAsBox(var18);
                     label152:
                     if (var22 != null) {
                        if (var14.contains(var22)) {
                           continue;
                        }

                        if (mc.world.getBlockEntity(var22) instanceof ChestBlockEntity var24 && this.ZAsBoolean(var24)) {
                           var23 = var23.union(this.JAsBox(var24));
                           break label152;
                        }

                        var22 = null;
                     }

                     var14.add(var19);
                     Vec3d var43 = new Vec3d((var23.minX + var23.maxX) / 2.0, var23.maxY + 0.25, (var23.minZ + var23.maxZ) / 2.0);
                     ProjectionUtil.Type_e var44 = ProjectionUtil.vAsD5_Type_e(var43, var6, var7, var8, var9, var10, false);
                     if (var44 != null) {
                        int var26 = this.wAsInt(var18, var20);
                        double var27 = (double)(var26 >>> 24) / 255.0;
                        if (!(var27 < 0.03) && (this.z.throughWalls() || !this.DAsBoolean(var6, var43, var19, var22))) {
                           String var29 = var18 instanceof MobSpawnerBlockEntity var30 ? this.JAsString(var30) : null;
                           String var45 = TextDrawing.sAsString(pAsString(var18, var22 != null), 160, TextDrawing.Type_x.MEDIUM);
                           String var31 = wAsString(var29, var20);
                           ItemStack var32 = this.labelIcons.getValue() ? this.LAsItemStack(var18) : ItemStack.EMPTY;
                           int var33 = 7
                              + (var32.isEmpty() ? 0 : 16)
                              + TextDrawing.qAsInt(var45, TextDrawing.Type_x.MEDIUM)
                              + 6
                              + TextDrawing.nAsInt(var31)
                              + 6;
                           float var34 = var44.x() - (float)var33 * var12 / 2.0F;
                           float var35 = var44.y() - 16.0F * var12;
                           float[] var36 = new float[]{var34, var35, var34 + (float)var33 * var12, var35 + 16.0F * var12};
                           if (!this.hideOverlaps.getValue()
                              || !var15.stream()
                                 .anyMatch(
                                    var1x -> var36[0] < var1x[2] + 1.0F
                                          && var36[2] + 1.0F > var1x[0]
                                          && var36[1] < var1x[3] + 1.0F
                                          && var36[3] + 1.0F > var1x[1]
                                 )) {
                              var15.add(var36);
                              var16.add(new StorageESP.Type_l(var44.x(), var44.y(), var33, var45, var31, var32, var26, var27));
                           }
                        }
                     }
                  }
               }
            }

            for (int var42 = var16.size() - 1; var42 >= 0; var42--) {
               this.FAsVoid(var1, (StorageESP.Type_l)var16.get(var42), var12);
            }
         } catch (RuntimeException var401) {
            ClientLog.hAsVoid("ESP", "Storage name tags failed", var401);
         } finally {
            OpenScreen.aAsVoid("StorageESP labels", var3);
         }
      }
   }

   private void FAsVoid(DrawContext var1, StorageESP.Type_l var2, float var3) {
      var1.getMatrices().pushMatrix();

      try {
         var1.getMatrices().translate(var2.x(), var2.y()).scale(var3).translate((float)(-var2.width()) / 2.0F, -16.0F);
         GuiTextureDrawing.CAsVoid(var1, 0, 0, var2.width(), 16, ColorUtil.pAsInt(-435154666, var2.alpha()));
         GuiTextureDrawing.CAsVoid(var1, 0, 0, 2, 16, var2.color());
         int var4 = 7;
         if (!var2.icon().isEmpty()) {
            if (var2.alpha() > 0.5) {
               var1.getMatrices().pushMatrix();
               var1.getMatrices().translate(7.0F, 2.0F).scale(0.75F);
               var1.drawItem(var2.icon(), 0, 0);
               var1.getMatrices().popMatrix();
            }

            var4 += 16;
         }

         TextDrawing.oAsVoid(var1, var2.name(), var4, 4, ColorUtil.pAsInt(-854792, var2.alpha()), TextDrawing.Type_x.MEDIUM);
         var4 += TextDrawing.qAsInt(var2.name(), TextDrawing.Type_x.MEDIUM) + 6;
         TextDrawing.CAsVoid(var1, var2.detail(), var4, 4, ColorUtil.pAsInt(-6511697, var2.alpha()));
      } finally {
         var1.getMatrices().popMatrix();
      }
   }

   private boolean DAsBoolean(Vec3d var1, Vec3d var2, BlockPos var3, BlockPos var4) {
      BlockHitResult var5 = mc.world.raycast(new RaycastContext(var1, var2, ShapeType.COLLIDER, FluidHandling.NONE, mc.player));
      return var5.getType() != Type.BLOCK ? false : !var5.getBlockPos().equals(var3) && !var5.getBlockPos().equals(var4);
   }

   private ItemStack LAsItemStack(BlockEntity var1) {
      return this.m.computeIfAbsent(var1.getCachedState().getBlock(), var0 -> new ItemStack(var0.asItem()));
   }

   private String JAsString(MobSpawnerBlockEntity var1) {
      try {
         Entity var2 = var1.getLogic().getRenderedEntity(mc.world, var1.getPos());
         return var2 == null ? null : var2.getType().getName().getString();
      } catch (RuntimeException var31) {
         ClientLog.hAsVoid("ESP", "Spawner mob name unavailable; tag shows the block name", var31);
         return null;
      }
   }

   private static record Type_l(float x, float y, int width, String name, String detail, ItemStack icon, int color, double alpha) {
   }
}
