package com.larpclient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public final class RealStackExemptions {
   private static Map<Integer, ItemProvenanceQuantity.Type_h> N = new HashMap<>();
   private static String t;
   private static long M;
   private static Path f;
   private static final Map<String, RealStackExemptions.Type_z> E = new HashMap<>();
   private static Map<Integer, ItemStack> SkyChunkCache = Map.of();
   private static Object U;
   private static boolean y;

   public RealStackExemptions() {
   }

   public static void SAsVoid(ItemStack var0, int var1) {
      String var2 = Sha256Digest.DAsString(var0);
      RealStackExemptions.Type_z var3 = E.get(var2);
      E.put(var2, new RealStackExemptions.Type_z(Math.max(0, var1) + (var3 == null ? 0 : var3.count()), System.currentTimeMillis() + 10000L));
   }

   public static void yAsVoid() {
      MinecraftClient var0 = MinecraftClient.getInstance();
      if (var0.isOnThread() && !FakeSpawnerTransaction.aAsBoolean()) {
         if (var0.player != null && var0.world != null) {
            String var1 = var0.getCurrentServerEntry() == null ? "local" : var0.getCurrentServerEntry().address;
            int var2 = !var1.equals(t) ? 1 : 0;
            if (var2 != 0) {
               SmoothInput = var1;
               M = 0L;
               N.clear();
               MenuPopupProvider = FabricLoader.getInstance().getConfigDir().resolve("larp-client/fake-spawner-real-stacks-v2-" + Sha256Digest.EAsString(t) + ".txt");

               try {
                  if (Files.exists(f)) {
                     for (String var4 : Files.readAllLines(f)) {
                        String[] var5 = var4.split("\t");
                        if (var5.length == 4) {
                           N.put(Integer.parseInt(var5[0]), new ItemProvenanceQuantity.Type_h(var5[1], Integer.parseInt(var5[2]), Integer.parseInt(var5[3])));
                        }
                     }
                  }
               } catch (NumberFormatException | IOException var18) {
                  ClientLog.oAsVoid("FakeSpawner", "Could not load auction stack exemptions", var18);
               }
            }

            long var19 = System.currentTimeMillis();
            boolean var20 = AuctionMenuText.BAsBoolean();
            if (var20) {
               M = var19 + 10000L;
            } else if (var0.player.currentScreenHandler != var0.player.playerScreenHandler) {
               M = 0L;
            }

            HashMap<Integer, ItemStack> var6 = new HashMap<>();
            PlayerInventory var7 = var0.player.getInventory();

            for (int var8 = 0; var8 < var7.size(); var8++) {
               WAsVoid(var6, var8, var7.getStack(var8));
            }

            WAsVoid(var6, -1, var0.player.currentScreenHandler.getCursorStack());
            if (!var20) {
               for (Slot var9 : var0.player.currentScreenHandler.slots) {
                  if (var9.inventory != var7) {
                     WAsVoid(var6, 1000 + var9.id, var9.getStack());
                  }
               }
            }

            E.values().removeIf(var2x -> var2x.until() < var19);
            int var22 = var2 == 0
                  && U == var0.player.currentScreenHandler
                  && ScreenScale == var20
                  && var6.size() == SkyChunkCache.size()
                  && var6.entrySet().stream().allMatch(var0x -> ItemStack.areEqual(var0x.getValue(), SkyChunkCache.getOrDefault(var0x.getKey(), ItemStack.EMPTY)))
               ? 1
               : 0;
            if (var22 == 0) {
               SkyChunkCache = new HashMap<>();
               var6.forEach((var0x, var1x) -> SkyChunkCache.put(var0x, var1x.copy()));
               U = var0.player.currentScreenHandler;
               ScreenScale = var20;
               HashMap var23 = new HashMap();
               var6.forEach((var1x, var2x) -> {
                  String var3 = Sha256Digest.DAsString(var2x);
                  ItemProvenanceQuantity.Type_h var4x = N.get(var1x);
                  int var5x = Sha256Digest.RAsBoolean(var2x) ? var2x.getCount() : 0;
                  if (var5x > 0 && var4x != null && var4x.item().equals(var3)) {
                     var5x = Math.min(var5x, var4x.purchased());
                  }

                  var23.put(var1x, new ItemProvenanceQuantity.Type_h(var3, var2x.getCount(), var5x));
               });
               if (var2 != 0) {
                  N.entrySet()
                     .removeIf(
                        var1x -> !var23.containsKey(var1x.getKey())
                              || !((ItemProvenanceQuantity.Type_h)var23.get(var1x.getKey())).item().equals(var1x.getValue().item())
                              || ((ItemProvenanceQuantity.Type_h)var23.get(var1x.getKey())).count() != var1x.getValue().count()
                     );
               }

               Map var10 = ItemProvenanceQuantity.CAsMap(N, var23, var2 == 0 && var19 < M);
               E.values().removeIf(var2x -> var2x.until() < var19);

               for (Entry var12 : (Iterable<Entry>) (new TreeMap(var10).entrySet())) {
                  ItemProvenanceQuantity.Type_h var13 = (ItemProvenanceQuantity.Type_h)var12.getValue();
                  RealStackExemptions.Type_z var14 = E.get(var13.item());
                  if (var14 != null) {
                     ItemProvenanceQuantity.Type_h var15 = N.get(var12.getKey());
                     int var16 = var13.count() - (var15 != null && var15.item().equals(var13.item()) ? var15.count() : 0);
                     int var17 = Math.min(Math.max(0, var16), Math.min(var14.count(), var13.count() - var13.purchased()));
                     if (var17 > 0) {
                        var10.put((Integer)var12.getKey(), new ItemProvenanceQuantity.Type_h(var13.item(), var13.count(), var13.purchased() + var17));
                        if (var17 == var14.count()) {
                           E.remove(var13.item());
                        } else {
                           E.put(var13.item(), new RealStackExemptions.Type_z(var14.count() - var17, var14.until()));
                        }
                     }
                  }
               }

               var6.forEach((var1x, var2x) -> Sha256Digest.RAsVoid(var2x, ((ItemProvenanceQuantity.Type_h)var10.get(var1x)).purchased() == var2x.getCount()));
               int var24 = !var10.equals(N) ? 1 : 0;
               N = var10;
               boolean var25 = dAsBoolean(var0);
               if (var24 != 0 || var25) {
                  IAsVoid();
               }
            }
         } else {
            SmoothInput = null;
            N.clear();
            E.clear();
            M = 0L;
            SkyChunkCache = Map.of();
            U = null;
         }
      }
   }

   private static void IAsVoid() {
      try {
         Files.createDirectories(MenuPopupProvider.getParent());
         List var0 = N.entrySet()
            .stream()
            .filter(var0x -> var0x.getKey() < 1000 && var0x.getValue().purchased() > 0)
            .map(var0x -> var0x.getKey() + "\t" + var0x.getValue().item() + "\t" + var0x.getValue().count() + "\t" + var0x.getValue().purchased())
            .sorted()
            .toList();
         FakeSpawnerStateStore.VAsVoid(f, String.join("\n", var0));
      } catch (IOException var11) {
         ClientLog.oAsVoid("FakeSpawner", "Could not save auction stack exemptions", var11);
      }
   }

   private static boolean dAsBoolean(MinecraftClient var0) {
      if (var0.interactionManager != null
         && var0.player.currentScreenHandler == var0.player.playerScreenHandler
         && var0.player.playerScreenHandler.getCursorStack().isEmpty()) {
         for (Entry var2 : new TreeMap<>(N).entrySet()) {
            int var3 = (Integer)var2.getKey();
            ItemProvenanceQuantity.Type_h var4 = (ItemProvenanceQuantity.Type_h)var2.getValue();
            if (var3 >= 0 && var3 < 36 && var4.purchased() > 0 && var4.purchased() < var4.count()) {
               int var5 = -1;

               for (int var6 = 0; var6 < 36; var6++) {
                  if (var0.player.getInventory().getStack(var6).isEmpty()) {
                     var5 = var6;
                     break;
                  }
               }

               if (var5 < 0) {
                  return false;
               }

               if (var3 < 9) {
                  int var10000 = var3 + 36;
               }

               if (var5 < 9) {
                  int var81 = var5 + 36;
               }

               if (!FakeSpawnerController.ZAsMa().vAsBoolean(var3)) {
                  int var8 = var5;
                  return FakeSpawnerTransaction.nAsBoolean(
                     "separate purchased backing", FakeSpawnerTransaction.VAsList(var3, var8, var4.purchased(), var4.count()), () -> {
                        Sha256Digest.RAsVoid(var0.player.getInventory().getStack(var8), true);
                        N.put(var3, new ItemProvenanceQuantity.Type_h(var4.item(), var4.count() - var4.purchased(), 0));
                        N.put(var8, new ItemProvenanceQuantity.Type_h(var4.item(), var4.purchased(), var4.purchased()));
                        SkyChunkCache = Map.of();
                        IAsVoid();
                     }, () -> {
                        SkyChunkCache = Map.of();
                        M = 0L;
                     }
                  );
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private static void WAsVoid(Map<Integer, ItemStack> var0, int var1, ItemStack var2) {
      if (!var2.isEmpty()) {
         var2.getItem().getRegistryEntry().registryKey().getValue().toString();
         if (FakeSpawnerController.ZAsMa().LAsNa(var2.getItem()) != null) {
            var0.put(var1, var2);
         }
      }
   }

   private static record Type_z(int count, long until) {
   }
}
