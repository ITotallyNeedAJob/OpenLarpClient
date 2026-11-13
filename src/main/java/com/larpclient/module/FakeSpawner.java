package com.larpclient.module;

import com.google.gson.JsonObject;
import com.larpclient.ClientLog;
import com.larpclient.DonutAuctionApi;
import com.larpclient.FakeSpawnerController;
import com.larpclient.FakeSpawnerStateStore;
import com.larpclient.RealStackExemptions;
import com.larpclient.ShopPurchaseController;
import com.larpclient.SpawnerEntityCatalog;
import com.larpclient.VisualInventory;
import com.larpclient.al;
import com.larpclient.r2;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;

public final class FakeSpawner extends Module {
   private static final List<String> N = List.of(
      "None",
      "minecraft:cobblestone",
      "minecraft:stone",
      "minecraft:deepslate",
      "minecraft:netherrack",
      "minecraft:end_stone",
      "minecraft:iron_block",
      "minecraft:gold_block",
      "minecraft:glass"
   );
   private static FakeSpawner V;
   private final ModeSetting[] a = new ModeSetting[3];
   public final NumberSetting previewCount = this.addSetting(new NumberSetting("Preview Count", "Count for a standalone menu preview", 1.0, 1.0, 5000.0, 1.0));
   public final BooleanSetting enchantGlint = this.addSetting(new BooleanSetting("Enchant Glint", "Show an enchantment glint on spawner items", false));
   private final Map<String, NumberSetting> y = new LinkedHashMap<>();
   private boolean f;
   private String B = "§dSpawner";
   private String D = "";
   private final Path L;

   private Path DAsPath() {
      return this.L != null ? this.L : FabricLoader.getInstance().getConfigDir().resolve("larp-client/fake-spawner-local.json");
   }

   public FakeSpawner() {
      this(null);
   }

   public FakeSpawner(Path var1) {
      super("Fake Spawner", "Show blocks as spawners; loot buys real items via /shop", Category.PLAYER, -1);
      this.L = var1;
      V = this;
      String[] var2 = yAsStringArray();

      for (final SpawnerEntityCatalog var6 : SpawnerEntityCatalog.values()) {
         this.a[var6.ordinal()] = new ModeSetting(
            var6.g + " Block",
            "Use a different ordinary block for each mob type",
            var6 == SpawnerEntityCatalog.SKELETON ? "minecraft:cobblestone" : "None",
            var2
         ) {
            private final Map<String, ItemStack> F = new HashMap<>();

            @Override
            public String getDisplayName() {
               return switch (var6) {
                  case SKELETON -> "Skeleton";
                  case IRON_GOLEM -> "Iron";
                  case PIGLIN -> "Piglin";
               };
            }

            @Override
            public List<String> getVisibleModes() {
               if (FakeSpawner.N.contains(this.getValue())) {
                  return FakeSpawner.N;
               } else {
                  ArrayList var1x = new ArrayList<>(FakeSpawner.N);
                  var1x.add(this.getValue());
                  return List.copyOf(var1x);
               }
            }

            @Override
            public ItemStack getModeIcon(String var1) {
               if (var1.equals("None")) {
                  return ItemStack.EMPTY;
               } else {
                  Item iconItem = FakeSpawnerController.SAsItem(var1);
                  return this.F.computeIfAbsent(var1, key -> iconItem.getDefaultStack());
               }
            }

            @Override
            public boolean isSearchable() {
               return true;
            }

            @Override
            public String getModeLabel(String var1) {
               String var2x = var1.startsWith("minecraft:") ? var1.substring(10) : var1;
               return Arrays.stream(var2x.replace('_', ' ').split(" "))
                  .map(var0 -> (CharSequence)(var0.isEmpty() ? var0 : Character.toUpperCase(var0.charAt(0)) + var0.substring(1)))
                  .collect(Collectors.joining(" "));
            }

            @Override
            public void setValue(String var1) {
               if (!var1.equals("None")) {
                  for (ModeSetting var5 : FakeSpawner.this.a) {
                     if (var5 != null && var5 != this && var5.getValue().equals(var1)) {
                        throw new IllegalArgumentException("Each spawner type needs a different backing block");
                     }
                  }
               }

               super.setValue(var1);
            }
         };
         this.a[var6.ordinal()].onChanged(var1x -> this.IAsVoid());
      }

      for (String var8 : List.of("bone", "arrow", "iron_ingot", "rotten_flesh", "gold_nugget")) {
         NumberSetting var9 = new NumberSetting(
            var8.replace('_', ' ') + " Unit Price", "Cosmetic worth per item; -1 uses server order/API prices", -1.0, -1.0, 1.0E12, 0.01
         );
         var9.onChanged(var1x -> this.IAsVoid());
         this.y.put("minecraft:" + var8, var9);
      }

      if (var1 != null) {
         this.pAsVoid();
      }
   }

   private static String[] yAsStringArray() {
      ArrayList<String> var0 = new ArrayList<>();
      var0.add("None");
      Registries.ITEM
         .stream()
         .filter(
            var0x -> {
               if (var0x instanceof BlockItem var1
                  && !(var1.getBlock() instanceof BlockEntityProvider)
                  && var0x != Items.SPAWNER
                  && !var1.getBlock().getDefaultState().isAir()) {
                  return true;
               }

               return false;
            }
         )
         .map(var0x -> Registries.ITEM.getId(var0x).toString())
         .sorted()
         .forEach(var0::add);
      return var0.toArray(String[]::new);
   }

   public static FakeSpawner kAsFakeSpawner() {
      return V;
   }

   public String zAsString(int var1) {
      String var2 = this.a[var1].getValue();
      return var2.equals("None") ? "" : var2;
   }

   public String lAsString() {
      return this.zAsString(0);
   }

   public int previewCount() {
      return this.previewCount.getValue().intValue();
   }

   public String oAsString() {
      return this.B;
   }

   public boolean enchantGlint() {
      return this.enchantGlint.getValue();
   }

   public Double bAsDouble2(String var1) {
      NumberSetting var2 = this.y.get(var1);
      return var2 != null && !(var2.getValue() < 0.0) ? var2.getValue() : null;
   }

   public String PAsString() {
      return this.D;
   }

   public void OAsVoid(String var1) {
      this.D = var1 == null ? "" : var1.trim();
   }

   public String uAsString() {
      return this.B;
   }

   public void QAsVoid(String var1) {
      this.B = var1 == null ? "§dSpawner" : var1;
      this.IAsVoid();
   }

   public void IAsVoid() {
      if (!this.f) {
         JsonObject var1 = new JsonObject();
         var1.addProperty("itemName", this.B);
         var1.addProperty("apiKey", this.D);

         for (SpawnerEntityCatalog var5 : SpawnerEntityCatalog.values()) {
            var1.addProperty(var5.name() + "Block", this.a[var5.ordinal()].getValue());
         }

         JsonObject var7 = new JsonObject();
         this.y.forEach((var1x, var2) -> var7.addProperty(var1x, var2.getValue()));
         var1.add("prices", var7);

         try {
            FakeSpawnerStateStore.VAsVoid(this.DAsPath(), var1.toString());
         } catch (IOException var6) {
            ClientLog.lAsVoid("FakeSpawner", "Local preference save failed", var6);
         }
      }
   }

   public void pAsVoid() {
      if (Files.exists(this.DAsPath())) {
         this.f = true;

         try {
            JsonObject var1 = FakeSpawnerStateStore.MAsJsonObject(this.DAsPath());

            for (ModeSetting var5 : this.a) {
               var5.setValue("None");
            }

            for (SpawnerEntityCatalog var15 : SpawnerEntityCatalog.values()) {
               String var6 = var15.name() + "Block";
               if (var1.has(var6)) {
                  this.a[var15.ordinal()].setValue(var1.get(var6).getAsString());
               } else {
                  this.a[var15.ordinal()].reset();
               }
            }

            if (var1.has("prices")) {
               this.y.forEach((var1xx, var2) -> {
                  if (var1.getAsJsonObject("prices").has(var1xx)) {
                     var2.setClampedValue(var1.getAsJsonObject("prices").get(var1xx).getAsDouble());
                  }
               });
            }

            if (var1.has("itemName")) {
               this.B = var1.get("itemName").getAsString();
            }

            if (var1.has("apiKey")) {
               this.D = var1.get("apiKey").getAsString();
            }
         } catch (Exception var101) {
            ClientLog.lAsVoid("FakeSpawner", "Local preferences unreadable; valid values retained and file preserved", var101);
         } finally {
            this.f = false;
         }
      }
   }

   public void YAsVoid() {
      this.f = true;

      try {
         for (ModeSetting var4 : this.a) {
            var4.setValue("None");
         }

         for (ModeSetting var13 : this.a) {
            var13.reset();
         }

         for (NumberSetting var11 : this.y.values()) {
            var11.reset();
         }
      } finally {
         this.f = false;
      }

      this.IAsVoid();
   }

   public List<Setting<?>> RAsList() {
      return List.of(this.a);
   }

   public List<Setting<?>> wAsList() {
      return List.of(this.previewCount, this.enchantGlint);
   }

   public List<Setting<?>> xAsList() {
      return List.copyOf(this.y.values());
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(k.RENDER_ONLY, k.SERVER_STATE_MODIFYING);
   }

   @Override
   public void onEnable() {
      this.pAsVoid();
      FakeSpawnerController.ZAsMa().YAsVoid();
      ClientLog.GAsVoid(
         "FakeSpawner", "Active: {} / {} / {}; withdrawal purchases validate raw server lore", this.zAsString(0), this.zAsString(1), this.zAsString(2)
      );
      ClientLog.XAsVoid(
         "FakeSpawner",
         "Inventory clicks predict against real backing reservations; compact block choices show native icons and retain saved legacy selections"
      );
   }

   @Override
   public void onTick() {
      MinecraftClient var1 = getMc();
      RealStackExemptions.yAsVoid();
      FakeSpawnerController.ZAsMa().YAsVoid();
      ShopPurchaseController.AAsVoid(var1);
      r2.RAsVoid();
      VisualInventory.GAsVoid();
      com.larpclient.o.eAsVoid();
      al.tAsVoid();
      DonutAuctionApi.ZAsA6().AAsVoid();
   }

   @Override
   public void onDisable() {
      ShopPurchaseController.WAsVoid();
      FakeSpawnerController.ZAsMa().rAsVoid();
      VisualInventory.XAsVoid();
      al.eAsVoid();
      com.larpclient.o.IAsVoid();
      ClientLog.GAsVoid("FakeSpawner", "Runtime released; saved piles retained for matching world/player restoration");
   }
}
