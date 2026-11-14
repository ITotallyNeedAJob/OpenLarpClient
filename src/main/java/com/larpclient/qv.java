package com.larpclient;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.StainedGlassBlock;
import net.minecraft.block.StainedGlassPaneBlock;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction.Axis;

public final class qv {
   private static final Set<Block> S = Set.of(Blocks.BEACON, Blocks.RESPAWN_ANCHOR, Blocks.ENCHANTING_TABLE, Blocks.CRAFTER, Blocks.OBSERVER, Blocks.CONDUIT);
   private static final Set<Block> BloomUtility = Set.of(Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.BARREL, Blocks.HOPPER, Blocks.DISPENSER, Blocks.DROPPER);
   private static final Set<Block> T = Set.of(
      Blocks.CRAFTING_TABLE,
      Blocks.FURNACE,
      Blocks.BLAST_FURNACE,
      Blocks.SMOKER,
      Blocks.ANVIL,
      Blocks.CHIPPED_ANVIL,
      Blocks.DAMAGED_ANVIL,
      Blocks.BREWING_STAND,
      Blocks.SMITHING_TABLE,
      Blocks.LOOM,
      Blocks.STONECUTTER,
      Blocks.GRINDSTONE,
      Blocks.CARTOGRAPHY_TABLE,
      Blocks.FLETCHING_TABLE,
      Blocks.COMPOSTER
   );
   private static final Set<Block> SmoothInput = Set.of(
      Blocks.GLASS,
      Blocks.GLASS_PANE,
      Blocks.TINTED_GLASS,
      Blocks.IRON_BLOCK,
      Blocks.GOLD_BLOCK,
      Blocks.DIAMOND_BLOCK,
      Blocks.EMERALD_BLOCK,
      Blocks.NETHERITE_BLOCK,
      Blocks.REDSTONE_BLOCK,
      Blocks.POLISHED_DEEPSLATE,
      Blocks.DEEPSLATE_BRICKS,
      Blocks.DEEPSLATE_TILES
   );

   public qv() {
   }

   public static boolean GAsBoolean(BlockState var0) {
      return (var0.isOf(Blocks.BEEHIVE) || var0.isOf(Blocks.BEE_NEST)) && (Integer)var0.get(Properties.HONEY_LEVEL) == 5;
   }

   public static qv.Type_k xAsQv_Type_k(BlockState var0, int var1, boolean var2) {
      Block var3 = var0.getBlock();
      if (var3 instanceof ShulkerBoxBlock || S.contains(var3) || GAsBoolean(var0)) {
         return qv.Type_k.DEDICATED;
      } else if (var0.isOf(Blocks.ENDER_CHEST)) {
         return var2 ? qv.Type_k.STORAGE : qv.Type_k.DEDICATED;
      } else if (BloomUtility.contains(var3)) {
         return qv.Type_k.STORAGE;
      } else if (T.contains(var3)) {
         return qv.Type_k.UTILITY;
      } else if (!SmoothInput.contains(var3) && !(var3 instanceof StainedGlassBlock) && !(var3 instanceof StainedGlassPaneBlock)) {
         if (var0.isOf(Blocks.DEEPSLATE) && var0.get(Properties.AXIS) != Axis.Y) {
            return qv.Type_k.ALTERED;
         } else {
            return var0.isOf(Blocks.COBBLED_DEEPSLATE) || var1 >= 16 && var0.isOf(Blocks.DEEPSLATE) ? qv.Type_k.ALTERED : qv.Type_k.NONE;
         }
      } else {
         return qv.Type_k.CRAFTED;
      }
   }

   public static enum Type_k {
      NONE,
      STORAGE,
      UTILITY,
      CRAFTED,
      ALTERED,
      DEDICATED;

      private Type_k() {
      }
   }

   public static final class Type_s {
      private final Map<Integer, qv.Type_s.Type_z> C = new HashMap<>();

      public Type_s() {
      }

      public void AAsVoid() {
         this.C.clear();
      }

      public int UAsInt() {
         return this.C.size();
      }

      public void wAsVoid(BlockState var1, int var2, boolean var3, boolean var4) {
         this.vAsVoid(var1, 0, var2, 0, var3, var4);
      }

      public void vAsVoid(BlockState var1, int var2, int var3, int var4, boolean var5, boolean var6) {
         qv.Type_k var7 = qv.xAsQv_Type_k(var1, var3, var5);
         if (var7 != qv.Type_k.NONE && (var7 != qv.Type_k.ALTERED || var6)) {
            qv.Type_s.Type_z var8 = this.C.computeIfAbsent(var3 >> 4, var1x -> new qv.Type_s.Type_z(var3));
            if (var1.isOf(Blocks.ENDER_CHEST) && !var5) {
               var8.X++;
            }

            if (var7 != qv.Type_k.ALTERED) {
               var8.QAsVoid(var2, var3, var4);
            }

            switch (var7) {
               case STORAGE:
                  var8.I++;
                  break;
               case UTILITY:
                  var8.i++;
                  break;
               case CRAFTED:
                  var8.L++;
                  break;
               case ALTERED:
                  var8.r++;
                  break;
               case DEDICATED:
                  var8.J++;
                  if (var8.A.isEmpty()) {
                     var8.A = var1.getBlock().getName().getString();
                     var8.e = var3;
                  }
            }
         }
      }

      public ae zAsAe(String var1, int var2, int var3) {
         return this.mAsAe(var1, var2, var3, 1);
      }

      public ae mAsAe(String var1, int var2, int var3, int var4) {
         ae var5 = ae.EMPTY;

         for (qv.Type_s.Type_z var7 : this.C.values()) {
            ae var8 = var7.hAsAe();
            boolean var9 = var8.qualifies(var1, var2, var3, var4);
            boolean var10 = var5.qualifies(var1, var2, var3, var4);
            if (var9 && !var10 || var9 == var10 && (var8.weight() > var5.weight() || var8.weight() == var5.weight() && var8.sampleY() < var5.sampleY())) {
               var5 = var8;
            }
         }

         return var5;
      }

      private static final class Type_z {
         int I;
         int i;
         int L;
         int r;
         int J;
         int e;
         int X;
         String A = "";
         int U = 16;
         int F = Integer.MAX_VALUE;
         int BoxUtils = 16;
         int H;
         int R = Integer.MIN_VALUE;
         int Z;

         Type_z(int var1) {
            this.e = var1;
         }

         void QAsVoid(int var1, int var2, int var3) {
            this.U = Math.min(this.U, var1);
            this.F = Math.min(this.F, var2);
            this.c = Math.min(this.c, var3);
            this.H = Math.max(this.H, var1 + 1);
            this.R = Math.max(this.R, var2 + 1);
            this.Z = Math.max(this.Z, var3 + 1);
         }

         ae hAsAe() {
            Box var1 = this.F == Integer.MAX_VALUE
               ? null
               : new Box((double)this.U, (double)this.F, (double)this.c, (double)this.H, (double)this.R, (double)this.Z);
            return new ae(this.I, this.i, this.L, this.r, this.J, this.e, this.A, var1, this.X);
         }
      }
   }
}
