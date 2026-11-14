package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.ThemeSettings;
import com.larpclient.ColorUtil;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.BooleanSupplier;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.chunk.WorldChunk;

public class XRay extends Module {
   private static volatile XRay K;
   private static final Map<Block, Integer> N = new HashMap<>();
   private static final Map<Block, String> b = new LinkedHashMap<>();
   private static final String c = "Base Blocks";
   private static final int X = 32;
   private static volatile boolean T = false;
   private static volatile String t = "Both";
   private static volatile Set<Block> u = Set.of();
   private static volatile boolean p;
   private final ModeSetting mode = this.addSetting(new ModeSetting("Mode", "XRay rendering mode", "Both", "See Through", "ESP Outline", "Both"));
   private final NumberSetting range = this.addSetting(new NumberSetting("Range", "Scan radius", 32.0, 8.0, 64.0, 4.0));
   private final Map<String, BooleanSetting> V = new LinkedHashMap<>();
   private final ThemeSettings k = new ThemeSettings(var1x -> this.addSetting(var1x));
   private final BooleanSetting oreColors = this.addSetting(new BooleanSetting("Ore Colors", "Color each marker by what it is", true));
   private final BooleanSetting labels = this.addSetting(new BooleanSetting("Labels", "Name, vein size and distance above each vein", true));
   private final ModeSetting overlayStyle = this.addSetting(new ModeSetting("Overlay Style", "Ore marker shape", "Box", "Box", "Corners", "Filled"));
   private final NumberSetting maxOres = this.addSetting(new NumberSetting("Max Ores", "Nearest ore markers retained", 512.0, 64.0, 2048.0, 64.0));
   private final List<XRay.Type_p> D = new ArrayList<>();
   private final PriorityQueue<XRay.Type_p> A = new PriorityQueue<>(Comparator.comparingDouble(XRay.Type_p::distanceSquared).reversed());
   private Set<Block> e = Set.of();
   private int j = -1;
   private boolean n;
   private BlockPos w;
   private int P;
   private int d;
   private int M;
   private ClientWorld G;

   private static void VAsVoid(String var0, int var1, Block... var2) {
      for (Block var6 : var2) {
         N.put(var6, var1);
         b.put(var6, var0);
      }
   }

   public XRay() {
      super("XRay", "Highlight ores through blocks", Category.RENDER, -1);
      Set var4 = Set.of("Coal", "Copper", "Quartz");

      for (String var3 : new LinkedHashSet<>(b.values())) {
         this.V.put(var3, this.addSetting(new BooleanSetting(var3, "Show " + var3.toLowerCase(Locale.ROOT), !var4.contains(var3))));
      }

      BooleanSupplier var5 = () -> !this.mode.pAsBoolean("See Through");
      this.range.visibleWhen(var5);
      this.maxOres.visibleWhen(var5);
      this.overlayStyle.visibleWhen(var5);
      this.oreColors.visibleWhen(var5);
      this.labels.visibleWhen(var5);
      this.k.DAsVoid(() -> var5.getAsBoolean() && !this.oreColors.getValue(), var5, var5, () -> var5.getAsBoolean() && this.overlayStyle.pAsBoolean("Filled"));
      this.OAsBoolean();
      WorldRenderEvents.BEFORE_DEBUG_RENDER.register(this::iAsVoid);
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)var0 -> pAsVoid());
      K = this;
   }

   @Override
   public void onEnable() {
      t = this.mode.getValue();
      this.OAsBoolean();
      T = true;
      pAsVoid();
      this.TAsVoid();
      if (AAsBoolean()) {
         this.iAsVoid();
      }

      String var1 = FabricLoader.getInstance()
         .getModContainer("sodium")
         .map(var0 -> "Sodium " + var0.getMetadata().getVersion().getFriendlyString())
         .orElse("vanilla");
      ClientLog.GAsVoid("XRay", "Enabled in {} mode, {} block types shown, {} terrain", t, u.size(), var1);
   }

   public List<Setting<?>> OAsList() {
      return List.of(this.mode, this.overlayStyle, this.range);
   }

   public List<Setting<?>> yAsList() {
      return List.copyOf(this.V.values());
   }

   public List<Setting<?>> YAsList() {
      ArrayList var1 = new ArrayList<>(List.of(this.oreColors, this.labels));
      var1.addAll(this.k.TAsList());
      var1.addAll(this.k.OAsList());
      var1.addAll(this.k.qAsList());
      return List.copyOf(var1);
   }

   public List<Setting<?>> zAsList() {
      return List.of(this.maxOres);
   }

   @Override
   public void onDisable() {
      T = false;
      pAsVoid();
      this.TAsVoid();
      this.iAsVoid();
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         String var1 = this.mode.getValue();
         byte var2 = (byte)(AAsBoolean() ? 1 : 0);
         boolean var3 = this.OAsBoolean();
         if (!var1.equals(t)) {
            t = var1;
            this.TAsVoid();
         }

         byte var4 = (byte)(AAsBoolean() ? 1 : 0);
         bAsVoid(var4 != 0);
         if (var2 != var4 || var3 && var4 != 0) {
            ClientLog.XAsVoid("XRay", "Rebuilding terrain: mode {}, filters changed {}", t, var3);
            this.iAsVoid();
         }

         if (bAsBoolean()) {
            this.QAsVoid();
         } else if (!this.D.isEmpty() || this.w != null) {
            this.TAsVoid();
         }
      }
   }

   private boolean OAsBoolean() {
      int var1 = 0;
      int var2 = 0;

      for (BooleanSetting var4 : this.V.values()) {
         if (var4.getValue()) {
            var1 |= 1 << var2;
         }

         var2++;
      }

      if (var1 == this.j) {
         return false;
      } else {
         this.j = var1;
         HashSet var8 = new HashSet();
         HashSet var9 = new HashSet();

         for (Block var6 : N.keySet()) {
            String var7 = b.get(var6);
            if (var7 == null) {
               var8.add(var6);
            } else if (this.V.get(var7).getValue()) {
               var8.add(var6);
               var9.add(var6);
            }
         }

         u = Set.copyOf(var8);
         this.e = Set.copyOf(var9);
         this.TAsVoid();
         return true;
      }
   }

   private void TAsVoid() {
      this.D.clear();
      this.A.clear();
      this.w = null;
      this.G = null;
      this.d = this.M = 0;
      this.n = false;
   }

   private void QAsVoid() {
      int var1 = this.range.getValue().intValue();
      BlockPos var2 = mc.player.getBlockPos();
      if (this.G != mc.world || this.w == null || this.P != var1 || this.w.getSquaredDistance(var2) > 64.0) {
         this.TAsVoid();
         this.G = mc.world;
         this.w = var2;
         this.P = var1;
      }

      if (this.M > 0) {
         this.M--;
      } else {
         int var3 = var1 * 2 + 1;
         int var4 = var3 * var3 * var3;
         Mutable var5 = new Mutable();

         for (int var6 = 0; var6 < 8192 && this.d < var4; this.d++) {
            int var7 = this.d % var3;
            int var8 = this.d / var3 % var3;
            int var9 = this.d / (var3 * var3);
            int var10 = XAsInt(var9);
            int var11 = XAsInt(var7);
            int var12 = XAsInt(var8);
            if (var10 * var10 + var11 * var11 + var12 * var12 <= var1 * var1) {
               var5.set(this.w.getX() + var10, this.w.getY() + var11, this.w.getZ() + var12);
               WorldChunk var13 = mc.world.getChunkManager().getWorldChunk(var5.getX() >> 4, var5.getZ() >> 4);
               if (var13 != null && var5.getY() >= mc.world.getBottomY() && var5.getY() <= mc.world.getTopYInclusive()) {
                  Block var14 = var13.getBlockState(var5).getBlock();
                  if (this.e.contains(var14)) {
                     int var15 = 0xFF000000 | N.get(var14);
                     this.A.add(new XRay.Type_p(var5.toImmutable(), var14, var15, var5.getSquaredDistance(this.w), 0));

                     while (this.A.size() > this.maxOres.getValue().intValue()) {
                        this.A.poll();
                     }
                  }
               }
            }

            var6++;
         }

         if (!this.n || this.d >= var4) {
            this.D.clear();
            this.D.addAll(this.A);
            this.D.sort(Comparator.comparingDouble(XRay.Type_p::distanceSquared));
            this.BAsVoid();
         }

         if (this.d >= var4) {
            this.n = true;
            this.d = 0;
            this.A.clear();
            this.M = 10;
         }
      }
   }

   private void BAsVoid() {
      HashMap var1 = new HashMap(this.D.size() * 2);

      for (int var2 = 0; var2 < this.D.size(); var2++) {
         var1.put(this.D.get(var2).pos(), var2);
      }

      boolean[] var13 = new boolean[this.D.size()];
      ArrayDeque var3 = new ArrayDeque();

      for (int var4 = 0; var4 < this.D.size(); var4++) {
         if (!var13[var4]) {
            var13[var4] = true;
            XRay.Type_p var5 = this.D.get(var4);
            String var6 = cAsString(var5.block());
            int var7 = 0;
            var3.add(var4);

            while (!var3.isEmpty()) {
               BlockPos var8 = this.D.get((Integer)var3.poll()).pos();
               var7++;

               for (int var9 = -1; var9 <= 1; var9++) {
                  for (int var10 = -1; var10 <= 1; var10++) {
                     for (int var11 = -1; var11 <= 1; var11++) {
                        Integer var12 = (Integer)var1.get(var8.add(var9, var10, var11));
                        if (var12 != null && !var13[var12] && cAsString(this.D.get(var12).block()).equals(var6)) {
                           var13[var12] = true;
                           var3.add(var12);
                        }
                     }
                  }
               }
            }

            this.D.set(var4, var5.withVein(var7));
         }
      }
   }

   private static String cAsString(Block var0) {
      String var1 = b.get(var0);
      return "Base Blocks".equals(var1) ? var0.getName().getString() : var1;
   }

   private static int XAsInt(int var0) {
      return var0 == 0 ? 0 : (var0 % 2 == 1 ? (var0 + 1) / 2 : -var0 / 2);
   }

   private void iAsVoid(WorldRenderContext var1) {
      if (this.isOperational() && this.isInGame() && bAsBoolean() && this.G == mc.world) {
         try {
            int var2 = 0;
            int var3 = 0;
            double var4 = this.range.getValue();
            Vec3d var6 = mc.player.getEntityPos();

            for (XRay.Type_p var8 : this.D) {
               BlockPos var9 = var8.pos();
               WorldChunk var10 = mc.world.getChunkManager().getWorldChunk(var9.getX() >> 4, var9.getZ() >> 4);
               if (var10 != null && var10.getBlockState(var9).getBlock() == var8.block()) {
                  Vec3d var11 = Vec3d.ofCenter(var9);
                  double var12 = var6.distanceTo(var11);
                  if (!(var12 >= var4)) {
                     if (var2++ >= this.maxOres.getValue().intValue()) {
                        break;
                     }

                     int var14 = this.oreColors.getValue()
                        ? ColorUtil.pAsInt(var8.color(), ThemeSettings.RAsDouble(var12, var4))
                        : this.k.iAsInt(var8.color(), var12, var4, 1.0, var9.hashCode());
                     this.k.AAsVoid(new Box(var9).expand(0.003), this.overlayStyle.getValue(), var14);
                     if (this.labels.getValue() && var8.vein() > 0 && var3 < 32 && var14 >>> 24 != 0) {
                        var3++;
                        String var15 = cAsString(var8.block()) + (var8.vein() > 1 ? " x" + var8.vein() : "") + " · " + Math.round(var12) + "m";
                        this.k.NAsVoid(var11.add(0.0, 0.85, 0.0), var15, var14);
                     }
                  }
               }
            }
         } catch (RuntimeException var161) {
            ClientLog.hAsVoid("ESP", "Ore overlay failed", var161);
         }
      }
   }

   private void iAsVoid() {
      if (mc.world != null && mc.worldRenderer != null) {
         mc.worldRenderer.reload();
      }
   }

   private static void pAsVoid() {
      bAsVoid(AAsBoolean());
   }

   private static void bAsVoid(boolean var0) {
      if (var0 != p) {
         p = var0;
         ClientLog.XAsVoid("XRay", "Terrain see-through {} for chunk rebuilds", var0 ? "admitted" : "withdrawn");
      }
   }

   public static boolean kAsBoolean() {
      return K != null && K.isEnabled() ? T : false;
   }

   public static boolean AAsBoolean() {
      return K != null && K.isEnabled() ? T && (t.equals("See Through") || t.equals("Both")) : false;
   }

   public static boolean aAsBoolean() {
      return p;
   }

   public static boolean bAsBoolean() {
      return K != null && K.isEnabled() ? T && (t.equals("ESP Outline") || t.equals("Both")) : false;
   }

   public static boolean ZAsBoolean(Block var0) {
      return u.contains(var0);
   }

   static {
      VAsVoid("Diamond", 4910553, Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE);
      VAsVoid("Emerald", 1564002, Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE);
      VAsVoid("Gold", 16570170, Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE, Blocks.NETHER_GOLD_ORE);
      VAsVoid("Iron", 14857359, Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE);
      VAsVoid("Redstone", 16724016, Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_REDSTONE_ORE);
      VAsVoid("Lapis", 4025343, Blocks.LAPIS_ORE, Blocks.DEEPSLATE_LAPIS_ORE);
      VAsVoid("Copper", 15764043, Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE);
      VAsVoid("Coal", 10790052, Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE);
      VAsVoid("Ancient Debris", 12607548, Blocks.ANCIENT_DEBRIS);
      VAsVoid("Quartz", 16052198, Blocks.NETHER_QUARTZ_ORE);
      VAsVoid("Base Blocks", 4910553, Blocks.DIAMOND_BLOCK);
      VAsVoid("Base Blocks", 1564002, Blocks.EMERALD_BLOCK);
      VAsVoid("Base Blocks", 16570170, Blocks.GOLD_BLOCK);
      VAsVoid("Base Blocks", 15132390, Blocks.IRON_BLOCK);
      VAsVoid("Base Blocks", 10259596, Blocks.NETHERITE_BLOCK);
      VAsVoid("Base Blocks", 16757820, Blocks.CHEST);
      VAsVoid("Base Blocks", 11554047, Blocks.ENDER_CHEST);
      VAsVoid("Base Blocks", 16727160, Blocks.SPAWNER);
      VAsVoid("Base Blocks", 16734810, Blocks.TNT);

      for (Block var3 : new Block[]{Blocks.LAVA, Blocks.WATER, Blocks.OBSIDIAN, Blocks.BEDROCK}) {
         N.put(var3, 16777215);
      }
   }

   private static record Type_p(BlockPos pos, Block block, int color, double distanceSquared, int vein) {
      XRay.Type_p withVein(int var1) {
         return new XRay.Type_p(this.pos, this.block, this.color, this.distanceSquared, var1);
      }
   }
}
