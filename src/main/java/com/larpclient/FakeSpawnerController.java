package com.larpclient;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.larpclient.module.FakeSpawner;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.LongSets;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen.CreativeScreenHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry.Reference;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;

public final class FakeSpawnerController {
   private static final FakeSpawnerController T = new FakeSpawnerController();
   private static final int U = 45;
   private static final String j = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀsᴛᴜᴠᴡxʏᴢ";
   private static final long r = 1800L;
   private static final long a = 650L;
   private static final int K = 64648;
   private static final int H = 16711680;
   private static final double t = 75.0;
   private final Map<Long, Long> f = new HashMap<>();
   private final Map<Long, Map<Item, Integer>> d = new HashMap<>();
   private final Map<Long, Integer> G = new ConcurrentHashMap<>();
   private final Set<Long> c = ConcurrentHashMap.newKeySet();
   private final Map<Long, FakeSpawnerController.Type_b> E = new HashMap<>();
   private final Map<Long, Deque<FakeSpawnerController.Type_l>> N = new HashMap<>();
   private final Map<Integer, ItemStack> C = new HashMap<>();
   private final Map<Long, FakeSpawnerController.Type_x> X = new HashMap<>();
   private volatile Map<Long, MobSpawnerBlockEntity> v = new ConcurrentHashMap<>();
   private final Long2ObjectMap<List<MobSpawnerBlockEntity>> n = new Long2ObjectOpenHashMap();
   private final Map<Long, Long> Q = new HashMap<>();
   private final Map<Long, Float> l = new HashMap<>();
   private final Map<Long, Integer> Do = new HashMap<>();
   private int w;
   private final Set<Integer> D = new HashSet<>();
   private final Map<Integer, FakeSpawnerController.Type_c> L = new HashMap<>();
   private BlockPos k;
   private int z = -1;
   private final Map<Long, Integer> b = new HashMap<>();
   private final Map<Long, Double> u = new HashMap<>();
   private final Map<Long, Integer> i = new ConcurrentHashMap<>();
   private final Set<Long> B = new HashSet<>();
   private final Map<Item, Double> s = new HashMap<>();
   private final Set<Long> Dx = ConcurrentHashMap.newKeySet();
   private volatile World S;
   private ClientPlayerEntity g;
   private Item x;
   private String D4;
   private volatile Map<Item, SpawnerEntityCatalog> Db = Map.of();
   private volatile LongSet I = LongSets.EMPTY_SET;
   private volatile LongSet M = LongSets.EMPTY_SET;
   private final Map<Long, Long> D6 = new ConcurrentHashMap<>();
   private final Map<Long, SpawnerEntityCatalog> q = new ConcurrentHashMap<>();
   private final Map<Long, FakeSpawnerController.Type_v> Y = new HashMap<>();
   private final List<JsonElement> p = new ArrayList<>();
   private String P;
   private boolean y = true;
   private long m;
   private long O;
   private long W;
   private long R;
   private long o;
   private long F;
   private final Set<ItemStack> DJ = Collections.newSetFromMap(new IdentityHashMap<>());
   private final Set<ItemStack> A = Collections.newSetFromMap(new WeakHashMap<>());
   private Hand Z;
   private int V = -1;
   private int J = -1;
   private final Set<Long> e = ConcurrentHashMap.newKeySet();
   private long h;

   private static Path zAsPath() {
      return FabricLoader.getInstance().getConfigDir().resolve("larp-client/fake-spawner-piles.json");
   }

   void AAsVoid(Inventory var1) {
      this.DJ.clear();
      this.A.clear();

      for (int var2 = 0; var2 < var1.size(); var2++) {
         ItemStack var3 = var1.getStack(var2);
         if (!var3.isEmpty()) {
            this.DJ.add(var3);
         }
      }
   }

   public ItemStack MAsItemStack(ItemStack var1, ItemStack var2) {
      if (!var2.isEmpty() && (this.DJ.contains(var1) || this.A.contains(var1))) {
         this.A.add(var2);
         if (Boolean.getBoolean("larp.debug")) {
            this.o++;
         }
      }

      return var2;
   }

   public void PAsVoid() {
      MinecraftClient var1 = MinecraftClient.getInstance();
      if (var1.isOnThread() && var1.player != null) {
         this.AAsVoid(var1.player.getInventory());
         if (!var1.player.currentScreenHandler.getCursorStack().isEmpty()) {
            this.DJ.add(var1.player.currentScreenHandler.getCursorStack());
         }

         VisualInventory.vAsVoid();
      }
   }

   public void gAsVoid() {
      this.OAsVoid();
      if (Boolean.getBoolean("larp.debug")) {
         this.O++;
      }
   }

   public void dAsVoid(int var1, int var2) {
      if (Boolean.getBoolean("larp.debug")) {
         this.W += (long)var1;
         this.R += (long)var2;
      }
   }

   public boolean YAsBoolean(ItemStack var1) {
      if (!this.XAsBoolean()) {
         return false;
      } else {
         ClientPlayerEntity var2 = MinecraftClient.getInstance().player;
         return var2 != null
            && (
               var1 == var2.getMainHandStack() && this.vAsBoolean(var2.getInventory().getSelectedSlot())
                  || var1 == var2.getOffHandStack() && this.vAsBoolean(40)
            );
      }
   }

   public void rAsVoid() {
      MinecraftClient var1 = MinecraftClient.getInstance();
      if (var1 != null && !var1.isOnThread()) {
         throw new IllegalStateException("Fake spawner reset must run on the client thread");
      } else {
         this.UAsVoid();
         FakeSpawnerTransaction.uAsVoid("controller cleanup");
         VisualInventory.XAsVoid();
         this.DJ.clear();
         this.A.clear();
         this.v = new ConcurrentHashMap<>();
         this.n.clear();
         this.I = LongSets.EMPTY_SET;
         this.M = LongSets.EMPTY_SET;
         this.S = null;
         this.z = -1;
         this.Z = null;
         this.J = -1;
         this.Do.clear();
         this.w = 0;
         this.e.clear();
         this.D.clear();
         this.L.clear();
         this.b.clear();
         this.u.clear();
         this.i.clear();
         this.B.clear();
         this.s.clear();
         this.Dx.clear();

         for (Long var3 : new ArrayList<>(this.l.keySet())) {
            this.RAsVoid(BlockPos.fromLong(var3));
         }

         int var6 = this.G.isEmpty() && this.c.isEmpty() ? 0 : 1;
         this.G.clear();
         this.q.clear();
         this.Y.clear();
         this.p.clear();
         this.P = null;
         this.c.clear();
         this.f.clear();
         this.d.clear();
         this.E.clear();
         this.N.clear();
         this.C.clear();

         for (long var4 : this.D6.keySet()) {
            this.rAsVoid(BlockPos.fromLong(var4));
         }

         this.D6.clear();
         this.M = LongSets.EMPTY_SET;
         this.X.clear();
         this.l.clear();
         this.Q.clear();
         this.k = null;
         this.g = null;
         this.x = null;
         if (var1 != null) {
            if (var1.currentScreen instanceof SpawnerLootScreen || var1.currentScreen instanceof AuctionConfirmScreen) {
               var1.setScreen(null);
            }

            if (var6 != 0 && var1.world != null) {
               var1.worldRenderer.reload();
            }
         }
      }
   }

   private void IAsVoid() {
      MinecraftClient var1 = MinecraftClient.getInstance();
      if (var1 != null && !var1.isOnThread()) {
         throw new IllegalStateException("Fake spawner state must be updated on the client thread");
      } else {
         ClientWorld var2 = var1 == null ? null : var1.world;
         ClientPlayerEntity var3 = var1 == null ? null : var1.player;
         Item var4 = this.lAsItem();
         if (var2 != this.S || var3 != this.g || !this.OAsString().equals(this.D4) || !this.XAsBoolean()) {
            this.rAsVoid();
            this.S = var2;
            this.g = var3;
            this.x = var4;
            this.D4 = this.OAsString();
            HashMap var5 = new HashMap();
            FakeSpawner var6 = FakeSpawner.kAsFakeSpawner();
            if (var6 != null) {
               for (SpawnerEntityCatalog var10 : SpawnerEntityCatalog.values()) {
                  String var11 = var6.zAsString(var10.ordinal());
                  if (var11 != null && !var11.isBlank()) {
                     var5.put(SAsItem(var11), var10);
                  }
               }
            }

            this.Db = Map.copyOf(var5);
            if (var2 != null && var3 != null && this.XAsBoolean()) {
               this.P = this.qAsString(var1);
               this.CAsVoid();
               this.y = true;
            }
         }
      }
   }

   private String qAsString(MinecraftClient var1) {
      String var2 = var1.getCurrentServerEntry() != null
         ? var1.getCurrentServerEntry().address
         : (var1.getServer() != null ? "singleplayer:" + var1.getServer().getSavePath(WorldSavePath.ROOT).toAbsolutePath().normalize() : null);
      return var2 == null ? null : var2 + "|" + var1.world.getRegistryKey().getValue() + "|" + var1.player.getUuid();
   }

   private void CAsVoid() {
      if (this.P != null) {
         this.WAsVoid(zAsPath());
      }
   }

   private void WAsVoid(Path var1) {
      if (this.P != null && Files.exists(var1)) {
         try {
            JsonObject var2 = FakeSpawnerStateStore.MAsJsonObject(var1);
            if (var2.has("quarantined") && var2.get("quarantined").isJsonObject() && var2.getAsJsonObject("quarantined").has(this.P)) {
               for (JsonElement var4 : var2.getAsJsonObject("quarantined").getAsJsonArray(this.P)) {
                  this.p.add(var4.deepCopy());
               }
            }

            if (!var2.has(this.P)) {
               return;
            }

            int var20 = 0;

            for (JsonElement var5 : var2.getAsJsonArray(this.P)) {
               try {
                  JsonObject var6 = var5.getAsJsonObject();
                  long var7 = var6.get("pos").getAsLong();
                  SpawnerEntityCatalog var9 = SpawnerEntityCatalog.valueOf(var6.get("kind").getAsString());
                  int var10 = var6.get("count").getAsInt();
                  long var11 = var6.get("placedAt").getAsLong();
                  ArrayList var13 = new ArrayList();

                  for (JsonElement var15 : var6.getAsJsonArray("backing")) {
                     JsonObject var16 = var15.getAsJsonObject();
                     var13.add(
                        new FakeSpawnerController.Type_u(
                           var16.get("slot").getAsInt(), var16.get("item").getAsString(), var16.get("amount").getAsInt(), var16.get("reserved").getAsInt()
                        )
                     );
                  }

                  HashMap var22 = new HashMap();
                  if (var6.has("purchased")) {
                     for (Entry var24 : var6.getAsJsonObject("purchased").entrySet()) {
                        int var17 = ((JsonElement)var24.getValue()).getAsInt();
                        if (var17 > 0) {
                           var22.put((String)var24.getKey(), var17);
                        }
                     }
                  }

                  if (var10 < 1 || var10 > 100000 || var10 > 1 && var13.isEmpty() || this.Y.containsKey(var7)) {
                     throw new IllegalArgumentException("Invalid or conflicting saved pile");
                  }

                  this.Y.put(var7, new FakeSpawnerController.Type_v(var7, var9, var10, var11, var13, var22));
               } catch (RuntimeException var181) {
                  var20++;
                  this.p.add(var5.deepCopy());
               }
            }

            if (var20 > 0) {
               ClientLog.oAsVoid("FakeSpawner", "Retaining {} unmatched pile records for recovery; no inventory was restored", var20);
            }
         } catch (Exception var191) {
            ClientLog.lAsVoid("FakeSpawner", "Pile load failed; retaining the file for recovery", var191);
         }
      }
   }

   private void UAsVoid() {
      if (this.P != null) {
         this.zAsVoid(zAsPath());
      }
   }

   private void zAsVoid(Path var1) {
      if (this.P != null) {
         try {
            JsonObject var2 = Files.exists(var1) ? FakeSpawnerStateStore.MAsJsonObject(var1) : new JsonObject();
            JsonArray var3 = new JsonArray();
            if (var2.has(this.P) && !var2.get(this.P).isJsonArray()) {
               throw new IOException("Unrecognized saved pile data; original file retained");
            }

            if (var2.has("quarantined") && !var2.get("quarantined").isJsonObject()) {
               throw new IOException("Unrecognized pile quarantine data; original file retained");
            }

            JsonObject var4 = var2.has("quarantined") ? var2.getAsJsonObject("quarantined") : new JsonObject();
            if (var4.has(this.P) && !var4.get(this.P).isJsonArray()) {
               throw new IOException("Unrecognized retained pile records; original file retained");
            }

            JsonArray var5 = new JsonArray();

            for (JsonElement var7 : this.p) {
               var5.add(var7.deepCopy());
            }

            if (!var5.isEmpty()) {
               var4.add(this.P, var5);
               var2.add("quarantined", var4);
            }

            for (FakeSpawnerController.Type_v var18 : this.Y.values()) {
               var3.add(var18.toJson());
            }

            for (Entry var19 : this.G.entrySet()) {
               long var8 = (Long)var19.getKey();
               if (!this.Y.containsKey(var8) && (Integer)var19.getValue() >= 1) {
                  Deque var10 = this.N.get(var8);
                  if (var10 == null) {
                     var10 = new ArrayDeque();
                  }

                  if ((Integer)var19.getValue() <= 1 || !var10.isEmpty()) {
                     ArrayList var11 = new ArrayList();

                     for (FakeSpawnerController.Type_l var13 : (Iterable<FakeSpawnerController.Type_l>) (var10)) {
                        ItemStack var14 = this.C.get(var13.preferredSlot);
                        if (var14 == null || var14.isEmpty()) {
                           var11.clear();
                           break;
                        }

                        var11.add(
                           new FakeSpawnerController.Type_u(
                              var13.preferredSlot, Registries.ITEM.getId(var13.stack.getItem()).toString(), var13.stack.getCount(), var14.getCount()
                           )
                        );
                     }

                     if (!var11.isEmpty() || (Integer)var19.getValue() == 1) {
                        HashMap var20 = new HashMap();
                        this.d.getOrDefault(var8, Map.of()).forEach((var1x, var2x) -> var20.put(Registries.ITEM.getId(var1x).toString(), var2x));
                        var3.add(
                           new FakeSpawnerController.Type_v(
                                 var8,
                                 this.QAsNa(BlockPos.fromLong(var8)),
                                 (Integer)var19.getValue(),
                                 this.f.getOrDefault(var8, System.currentTimeMillis()),
                                 var11,
                                 var20
                              )
                              .toJson()
                        );
                     }
                  }
               }
            }

            var2.add(this.P, var3);
            Files.createDirectories(var1.getParent());
            FakeSpawnerStateStore.VAsVoid(var1, var2.toString());
         } catch (Exception var151) {
            ClientLog.lAsVoid("FakeSpawner", "Pile save failed; previous file retained", var151);
         }
      }
   }

   public void OAsVoid() {
      this.y = true;
   }

   private void WAsVoid(MinecraftClient var1) {
      if (this.y && !this.Y.isEmpty()) {
         this.y = false;
         byte var2 = 0;

         for (FakeSpawnerController.Type_v var4 : new ArrayList<>(this.Y.values())) {
            BlockPos var5 = BlockPos.fromLong(var4.pos);
            if (var1.world.isChunkLoaded(var5)) {
               WorldChunk var6 = var1.world.getWorldChunk(var5);
               int var7 = var6.getSectionIndex(var5.getY());
               BlockState var8 = var7 >= 0 && var7 < var6.getSectionArray().length
                  ? var6.getSection(var7).getBlockState(var5.getX() & 15, var5.getY() & 15, var5.getZ() & 15)
                  : Blocks.AIR.getDefaultState();
               if (this.LAsNa(var8.getBlock().asItem()) == var4.kind) {
                  HashMap var9 = new HashMap();

                  for (FakeSpawnerController.Type_u var11 : var4.backing) {
                     if ((var11.slot < 0 || var11.slot >= 36) && var11.slot != 40
                        || var11.amount < 1
                        || var11.reserved < 1
                        || var11.amount > var11.reserved + 1
                        || var11.reserved > 64
                        || var9.containsKey(var11.slot)
                        || this.vAsBoolean(var11.slot)) {
                        break;
                     }

                     ItemStack var12 = var1.player.getInventory().getStack(var11.slot);
                     if (var12.isEmpty() || var12.getCount() != var11.reserved || !Registries.ITEM.getId(var12.getItem()).toString().equals(var11.item)) {
                        break;
                     }

                     var9.put(var11.slot, var12.copy());
                  }

                  if (var9.size() == var4.backing.size()) {
                     int var15 = var4.backing.stream().mapToInt(var0 -> Math.min(var0.amount, var0.reserved)).sum();
                     if (var15 == var4.count - 1 || var15 == var4.count) {
                        ArrayDeque var16 = new ArrayDeque();
                        VisualInventory.GAsVoid();

                        for (FakeSpawnerController.Type_u var13 : var4.backing) {
                           ItemStack var14 = (ItemStack)var9.get(var13.slot);
                           this.C.put(var13.slot, var14);
                           var16.addLast(new FakeSpawnerController.Type_l(var13.slot, var14.copyWithCount(Math.min(var13.amount, var13.reserved))));
                        }

                        this.N.put(var4.pos, var16);
                        this.G.put(var4.pos, var4.count);
                        this.q.put(var4.pos, var4.kind);
                        this.f.put(var4.pos, var4.placedAt);
                        HashMap var18 = new HashMap();
                        var4.purchased.forEach((var1x, var2x) -> {
                           Identifier var3 = Identifier.tryParse(var1x);
                           if (var3 != null && Registries.ITEM.get(var3) != Items.AIR) {
                              var18.put((Item)Registries.ITEM.get(var3), var2x);
                           }
                        });
                        if (!var18.isEmpty()) {
                           this.d.put(var4.pos, var18);
                        }

                        this.Y.remove(var4.pos);
                        this.nAsVoid(var4.pos);
                        this.KAsMobSpawnerBlockEntity(var1.world, var5);
                        this.rAsVoid(var5);
                        VisualInventory.GAsVoid();
                        var2 = 1;
                     }
                  }
               }
            }
         }

         if (var2 != 0) {
            this.UAsVoid();
         }
      }
   }

   private FakeSpawnerController() {
   }

   public static FakeSpawnerController ZAsMa() {
      return T;
   }

   public boolean XAsBoolean() {
      FakeSpawner var1 = FakeSpawner.kAsFakeSpawner();
      return var1 != null && var1.isRuntimeActive();
   }

   public boolean DAsBoolean(ItemStack var1) {
      return this.XAsBoolean() && this.CAsBoolean(var1);
   }

   public boolean CAsBoolean(ItemStack var1) {
      return this.HAsBoolean(var1) && (this.DJ.contains(var1) || this.A.contains(var1) || Sha256Digest.oAsBoolean(var1));
   }

   private boolean HAsBoolean(ItemStack var1) {
      return var1 != null && !var1.isEmpty() && !AuctionMenuText.nAsBoolean(var1) && this.LAsNa(var1.getItem()) != null;
   }

   private String OAsString() {
      FakeSpawner var1 = FakeSpawner.kAsFakeSpawner();
      return var1.zAsString(0) + "|" + var1.zAsString(1) + "|" + var1.zAsString(2);
   }

   public SpawnerEntityCatalog LAsNa(Item var1) {
      FakeSpawner var2 = FakeSpawner.kAsFakeSpawner();

      for (SpawnerEntityCatalog var6 : SpawnerEntityCatalog.values()) {
         String var7 = var2.zAsString(var6.ordinal());
         if (var7 != null && !var7.isBlank() && var1 == SAsItem(var7)) {
            return var6;
         }
      }

      return null;
   }

   public SpawnerEntityCatalog QAsNa(BlockPos var1) {
      return var1 == null ? SpawnerEntityCatalog.SKELETON : this.q.getOrDefault(var1.asLong(), SpawnerEntityCatalog.SKELETON);
   }

   private Item SAsItem(long var1) {
      return SAsItem(FakeSpawner.kAsFakeSpawner().zAsString(this.QAsNa(BlockPos.fromLong(var1)).ordinal()));
   }

   public Item lAsItem() {
      FakeSpawner var1 = FakeSpawner.kAsFakeSpawner();
      return SAsItem(var1 == null ? null : var1.lAsString());
   }

   public static Item SAsItem(String var0) {
      Identifier var1 = var0 == null ? null : Identifier.tryParse(var0);
      Item var2 = var1 == null ? Items.COBBLESTONE : (Item)Registries.ITEM.get(var1);
      if (var2 instanceof BlockItem var3
         && var2 != Items.AIR
         && !var3.getBlock().getDefaultState().isAir()
         && !(var3.getBlock() instanceof BlockEntityProvider)
         && var3.getBlock() != Blocks.SPAWNER) {
         return var2;
      }

      return Items.COBBLESTONE;
   }

   private Block DAsBlock() {
      return this.lAsItem() instanceof BlockItem var1 ? var1.getBlock() : Blocks.COBBLESTONE;
   }

   public boolean MAsBoolean(BlockState var1) {
      return this.XAsBoolean() && var1 != null ? this.LAsNa(var1.getBlock().asItem()) != null || var1.isOf(Blocks.SPAWNER) : false;
   }

   public void eAsVoid(BlockPos var1) {
      this.IAsVoid();
      this.Dx.add(var1.asLong());
      this.G.remove(var1.asLong());
      this.nAsVoid(var1.asLong());
      this.rAsVoid(var1);
   }

   public BlockState AAsBlockState(BlockPos var1, BlockState var2) {
      return var1 == null ? var2 : this.cAsBlockState(var1.asLong(), var2);
   }

   public BlockState cAsBlockState(long var1, BlockState var3) {
      if (!this.I.contains(var1) || !this.oAsBoolean() || var3 == null || this.e.contains(var1)) {
         return var3;
      } else if (this.i.containsKey(var1)) {
         return Blocks.AIR.getDefaultState();
      } else if (this.Dx.contains(var1)) {
         return var3;
      } else if (!this.c.contains(var1) || !var3.isOf(Blocks.SPAWNER) && !this.Db.containsKey(var3.getBlock().asItem())) {
         int var4 = this.G.getOrDefault(var1, 0);
         if (var4 <= 0) {
            return var3;
         } else {
            return !var3.isAir() && !var3.isOf(Blocks.SPAWNER) && !this.Db.containsKey(var3.getBlock().asItem()) ? var3 : Blocks.SPAWNER.getDefaultState();
         }
      } else {
         return Blocks.AIR.getDefaultState();
      }
   }

   public boolean oAsBoolean() {
      return (!this.G.isEmpty() || !this.c.isEmpty() || !this.i.isEmpty()) && this.XAsBoolean() && this.S == MinecraftClient.getInstance().world;
   }

   private void bAsVoid() {
      LongOpenHashSet var1 = new LongOpenHashSet();

      for (long var3 : this.G.keySet()) {
         var1.add(var3);
      }

      for (long var9 : this.c) {
         var1.add(var9);
      }

      for (long var10 : this.i.keySet()) {
         var1.add(var10);
      }

      this.I = LongSets.unmodifiable(var1);
      LongOpenHashSet var8 = new LongOpenHashSet();

      for (long var4 : this.D6.keySet()) {
         var8.add(var4);
      }

      this.M = LongSets.unmodifiable(var8);
   }

   public void zAsVoid(BlockPos var1, BlockState var2) {
      this.IAsVoid();
      if (var1 != null) {
         long var3 = var1.asLong();
         if (this.kAsBoolean(var3)) {
            this.w = 0;
            this.rAsVoid(var1);
            ClientLog.XAsVoid("FakeSpawner", "Final break {} at {}; server state={}", var2 != null && var2.isAir() ? "confirmed" : "corrected", var1, var2);
         }

         if (this.Dx.contains(var3)) {
            if (var2 != null && !var2.isAir()) {
               return;
            }

            ItemBlockCache.xAsVoid(var1);
            this.Dx.remove(var3);
         }

         if (this.c.contains(var3)) {
            if (var2 != null && this.MAsBoolean(var2)) {
               this.w = 0;
            }

            this.X.put(var3, new FakeSpawnerController.Type_x(var1.toImmutable(), System.currentTimeMillis() + 10000L));
            this.c.remove(var3);
            this.rAsVoid(var1);
         }

         int var5 = this.G.getOrDefault(var3, 0);
         if (var2 != null && !var2.isAir()) {
            if (this.XAsBoolean()) {
               if (this.MAsBoolean(var2)) {
                  if (var5 <= 0 && !this.E.containsKey(var3)) {
                     return;
                  }

                  SpawnerEntityCatalog var6 = this.LAsNa(var2.getBlock().asItem());
                  if (var6 != null && this.q.put(var3, var6) != var6) {
                     this.nAsVoid(var3);
                  }

                  this.f.putIfAbsent(var3, System.currentTimeMillis());
                  FakeSpawnerController.Type_b var7 = this.E.remove(var3);
                  int var8 = var7 == null ? this.G.getOrDefault(var3, 1) : Math.max(1, var7.stackCount);
                  this.G.put(var3, Math.max(1, var8));
                  this.pAsVoid(
                     "track:" + var3, 1500L, "trackWorldState fake pos={} state={} resolvedCount={} pending={}", var1, var2.getBlock(), var8, var7 != null
                  );
                  if (var7 != null && var7.hiddenStack != null) {
                     this.mAsBoolean(var3, var7.hiddenStack, true);
                  }

                  if (var7 != null) {
                     this.UAsVoid();
                  }

                  this.KAsMobSpawnerBlockEntity(MinecraftClient.getInstance() == null ? null : MinecraftClient.getInstance().world, var1);
                  if (var5 <= 0) {
                     this.rAsVoid(var1);
                  }
               } else {
                  this.zAsVoid(var1, null);
               }
            }
         } else {
            if (var5 > 0) {
               this.VAsVoid("trackWorldState removed pos={} previousCount={} state={}", var1, var5, var2);
            }

            if (var5 > 0 && !this.lAsBoolean(var1)) {
               this.gAsVoid(var1);
            } else if (this.lAsBoolean(var1)) {
               this.hAsMa_Type_l(var3, Integer.MAX_VALUE);
               VisualInventory.vAsVoid();
               this.VAsVoid("Placement rejected at {}; reservations released without inventory replay", var1);
            }

            this.f.remove(var3);
            this.d.remove(var3);
            this.G.remove(var3);
            this.E.remove(var3);
            this.N.remove(var3);
            this.nAsVoid(var3);
            if (var5 > 0) {
               this.rAsVoid(var1);
            }

            this.l.remove(var3);
            this.Q.remove(var3);
            this.u.remove(var3);
            this.RAsVoid(var1);
            if (var1.equals(this.k)) {
               this.k = null;
            }
         }
      }
   }

   public void gAsVoid(BlockPos var1, ItemStack var2, int var3, boolean var4) {
      this.IAsVoid();
      if (this.XAsBoolean() && var1 != null && this.CAsBoolean(var2)) {
         FakeSpawnerController.Type_v var5 = this.Y.remove(var1.asLong());
         if (var5 != null) {
            this.p.add(var5.toJson());
            this.VAsVoid("New placement at {} archives a superseded saved pile for recovery", var1);
         }

         this.q.put(var1.asLong(), this.LAsNa(var2.getItem()));
         ItemStack var6 = var4 ? var2.copy() : ItemStack.EMPTY;
         int var7 = var4 ? var2.getCount() : 1;
         this.E.put(var1.asLong(), new FakeSpawnerController.Type_b(var7, new FakeSpawnerController.Type_l(var3, var6), System.currentTimeMillis()));
      }
   }

   public void FAsVoid(BlockPos var1, BlockState var2) {
      this.IAsVoid();
      FakeSpawnerController.Type_b var3 = this.E.get(var1.asLong());
      if (var3 != null && this.MAsBoolean(var2)) {
         this.c.remove(var1.asLong());
         this.G.put(var1.asLong(), var3.stackCount);
         this.f.putIfAbsent(var1.asLong(), var3.createdAtMs);
         this.KAsMobSpawnerBlockEntity(this.S, var1);
         this.rAsVoid(var1);
      }
   }

   public void WAsVoid() {
      MinecraftClient var1 = MinecraftClient.getInstance();
      if (this.XAsBoolean() && var1.player != null) {
         for (Entry var3 : new ArrayList<>(this.E.entrySet())) {
            if (this.G.containsKey(var3.getKey())) {
               FakeSpawnerController.Type_b var4 = (FakeSpawnerController.Type_b)var3.getValue();
               if (var4.hiddenStack != null && !var4.hiddenStack.stack.isEmpty()) {
                  this.mAsBoolean((Long)var3.getKey(), var4.hiddenStack, true);
               }

               this.G.put((Long)var3.getKey(), var4.stackCount);
            }
         }
      }
   }

   public void sAsVoid() {
      MinecraftClient var1 = MinecraftClient.getInstance();
      if (var1.isOnThread()
         && var1.player != null
         && this.XAsBoolean()
         && var1.world == this.S
         && var1.player == this.g
         && !FakeSpawnerTransaction.aAsBoolean()) {
         this.zAsVoid(var1.player.getInventory());
         this.L
            .entrySet()
            .removeIf(
               var1xx -> {
                  ItemStack var2 = var1.player.getInventory().getStack(var1xx.getKey());
                  FakeSpawnerController.Type_c var3x = var1xx.getValue();
                  return var1.player.age >= var3x.expires
                     || !var2.isEmpty() && !var2.isOf(var3x.stack.getItem())
                     || var2.isOf(var3x.stack.getItem()) && var2.getCount() >= var3x.expected;
               }
            );

         for (Entry var3 : this.C.entrySet()) {
            if (!this.E.values().stream().anyMatch(var1x -> var1x.hiddenStack != null && var1x.hiddenStack.preferredSlot == (Integer)var3.getKey())) {
               ItemStack var4 = var1.player.getInventory().getStack((Integer)var3.getKey());
               if (var4.isOf(((ItemStack)var3.getValue()).getItem())
                  && var4.getCount() > ((ItemStack)var3.getValue()).getCount()
                  && this.BAsBoolean((Integer)var3.getKey(), var4.getCount() - ((ItemStack)var3.getValue()).getCount())) {
                  break;
               }
            }
         }
      }
   }

   void zAsVoid(Inventory var1) {
      Iterator var2 = this.C.entrySet().iterator();

      while (var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         int var4 = (Integer)var3.getKey();
         ItemStack var5 = var1.getStack(var4);
         ItemStack var6 = (ItemStack)var3.getValue();
         if (var5.isOf(var6.getItem()) && var5.getCount() >= var6.getCount()) {
            this.KAsVoid(var4, var5);
         } else {
            int var7 = var5.isOf(var6.getItem()) ? var5.getCount() : 0;
            int var8 = var6.getCount() - var7;
            if (var7 == 0) {
               var2.remove();
            } else {
               var3.setValue(var5.copyWithCount(var7));
            }

            for (Entry var10 : this.N.entrySet()) {
               ArrayDeque var11 = new ArrayDeque();

               for (FakeSpawnerController.Type_l var13 : (Iterable<FakeSpawnerController.Type_l>) ((Deque)var10.getValue())) {
                  int var14 = var13.preferredSlot == var4 ? Math.min(var8, var13.stack.getCount()) : 0;
                  var8 -= var14;
                  if (var14 > 0) {
                     this.G.computeIfPresent((Long)var10.getKey(), (var1x, var2x) -> Math.max(1, var2x - var14));
                  }

                  if (var13.stack.getCount() > var14) {
                     var11.addLast(new FakeSpawnerController.Type_l(var13.preferredSlot, var13.stack.copyWithCount(var13.stack.getCount() - var14)));
                  }
               }

               ((Deque)var10.getValue()).clear();
               ((Deque)var10.getValue()).addAll(var11);
            }

            ClientLog.XAsVoid(
               "FakeSpawner",
               "Reservation corrected physical={} item={} old={} available={}; real inventory retained",
               var4,
               Registries.ITEM.getId(var6.getItem()),
               var6.getCount(),
               var7
            );
            VisualInventory.vAsVoid();
         }
      }
   }

   private boolean BAsBoolean(int var1, int var2) {
      MinecraftClient var3 = MinecraftClient.getInstance();
      int var4 = this.TAsInt();
      return var4 >= 0 && var3.player != null
         ? FakeSpawnerTransaction.nAsBoolean(
            "separate reservation surplus",
            FakeSpawnerTransaction.VAsList(var1, var4, var2, var3.player.getInventory().getStack(var1).getCount()),
            VisualInventory::vAsVoid,
            VisualInventory::vAsVoid
         )
         : false;
   }

   public boolean lAsBoolean(BlockPos var1) {
      return var1 != null && this.E.containsKey(var1.asLong());
   }

   public boolean KAsBoolean(BlockPos var1) {
      return this.XAsBoolean() && this.S == MinecraftClient.getInstance().world && var1 != null && this.c.contains(var1.asLong());
   }

   public void WAsVoid(Hand var1) {
      ClientPlayerEntity var2 = MinecraftClient.getInstance().player;
      if (var2 != null) {
         this.Z = var1;
         this.V = var2.getInventory().getSelectedSlot();
         this.J = var2.age + 4;
      }
   }

   public boolean GAsBoolean(Hand var1) {
      ClientPlayerEntity var2 = MinecraftClient.getInstance().player;
      return this.XAsBoolean()
         && var2 == this.g
         && var1 == this.Z
         && var2.age <= this.J
         && var2.getInventory().getSelectedSlot() == this.V
         && this.CAsBoolean(var2.getStackInHand(var1));
   }

   public void mAsVoid(BlockPos var1) {
      MinecraftClient var2 = MinecraftClient.getInstance();
      if (this.XAsBoolean() && var2.world != null && var2.world.getBlockState(var1).isAir() && this.D6.put(var1.asLong(), var2.world.getTime() + 10L) == null) {
         this.rAsVoid(var1);
      }
   }

   public BlockState mAsBlockState(BlockPos var1, BlockState var2) {
      return this.YAsBlockState(var1.asLong(), var2);
   }

   public BlockState YAsBlockState(long var1, BlockState var3) {
      return this.M.contains(var1) && var3.isAir() && this.XAsBoolean() && this.S == MinecraftClient.getInstance().world
         ? Blocks.SPAWNER.getDefaultState()
         : var3;
   }

   public boolean lAsBoolean(BlockPos var1, ItemStack var2, int var3, boolean var4) {
      this.IAsVoid();
      if (this.XAsBoolean()
         && var1 != null
         && !this.lAsBoolean(var1)
         && this.yAsInt(var1) > 0
         && this.CAsBoolean(var2)
         && this.LAsNa(var2.getItem()) == this.QAsNa(var1)
         && !this.vAsBoolean(var3)
         && !FakeSpawnerTransaction.aAsBoolean()) {
         long var5 = var1.asLong();
         int var7 = var4 ? var2.getCount() : 1;
         if (var7 < var2.getCount()) {
            int var8 = this.TAsInt();
            if (var8 < 0) {
               return false;
            } else {
               Item var9 = var2.getItem();
               return FakeSpawnerTransaction.nAsBoolean("single pile addition", FakeSpawnerTransaction.VAsList(var3, var8, var7, var2.getCount()), () -> {
                  MinecraftClient var6 = MinecraftClient.getInstance();
                  ItemStack var7x = var6.player.getInventory().getStack(var8);
                  if (this.G.containsKey(var5) && var7x.isOf(var9) && var7x.getCount() == var7) {
                     this.RAsBoolean(var5, var8, var7x, var7);
                  }
               }, VisualInventory::vAsVoid);
            }
         } else {
            return this.RAsBoolean(var5, var3, var2, var7);
         }
      } else {
         return false;
      }
   }

   private boolean RAsBoolean(long var1, int var3, ItemStack var4, int var5) {
      if (!this.mAsBoolean(var1, new FakeSpawnerController.Type_l(var3, var4.copy()), false)) {
         return false;
      } else {
         int var6 = (int)Math.min(2147483647L, (long)this.G.getOrDefault(var1, 1).intValue() + (long)var5);
         this.G.put(var1, var6);
         this.f.putIfAbsent(var1, System.currentTimeMillis());
         this.sAsVoid(MinecraftClient.getInstance().world, BlockPos.fromLong(var1));
         this.eAsVoid("You just added another spawner! There are now " + var6 + " spawners in the stack.");
         this.UAsVoid();
         return true;
      }
   }

   public boolean fAsBoolean() {
      ClientPlayerEntity var1 = MinecraftClient.getInstance().player;
      if (var1 != null && var1.getMainHandStack().isIn(ItemTags.PICKAXES)) {
         ItemEnchantmentsComponent var2 = (ItemEnchantmentsComponent)var1.getMainHandStack().get(DataComponentTypes.ENCHANTMENTS);
         Reference var3 = var1.getEntityWorld().getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH);
         return var2 != null && var2.getLevel(var3) > 0;
      } else {
         return false;
      }
   }

   public boolean BAsBoolean(BlockPos var1, Direction var2) {
      MinecraftClient var3 = MinecraftClient.getInstance();
      if (!this.vAsBoolean(var1)) {
         return false;
      } else {
         long var4 = var1.asLong();
         if (this.i.containsKey(var4)) {
            return true;
         } else if (this.fAsBoolean()) {
            this.b.remove(var4);
            this.u.remove(var4);
            this.B.remove(var4);
            return false;
         } else if (this.B.contains(var4)) {
            return true;
         } else {
            int var6 = var3.player.age;
            long var7 = var1.asLong();
            if (this.Do.getOrDefault(var7, -1) == var6) {
               return true;
            } else {
               this.Do.put(var7, var6);
               this.b.computeIfAbsent(var4, var1x -> var6);
               double var9 = var3.player.getAbilities().creativeMode
                  ? 1.0
                  : (double)Blocks.SPAWNER.getDefaultState().calcBlockBreakingDelta(var3.player, var3.world, var1);
               double var11 = Math.min(1.0, this.u.getOrDefault(var4, 0.0) + var9);
               this.u.put(var4, var11);
               this.HAsVoid(var3, var1, var2, var11);
               if (var11 >= 1.0) {
                  this.b.remove(var4);
                  this.u.remove(var4);
                  this.B.add(var4);
                  this.i.put(var4, var6 + 4);
                  this.RAsVoid(var1);
                  this.ZAsVoid(var1, var2);
                  this.mAsVoid(var1, true);
                  this.rAsVoid(var1);
               }

               return true;
            }
         }
      }
   }

   private void AAsVoid() {
      MinecraftClient var1 = MinecraftClient.getInstance();

      for (Entry var3 : new ArrayList<>(this.i.entrySet())) {
         if (var1.player.age >= (Integer)var3.getValue()) {
            this.i.remove(var3.getKey());
            this.rAsVoid(BlockPos.fromLong((Long)var3.getKey()));
            this.eAsVoid("You cannot break spawners without a silk touch!");
         }
      }

      if (!var1.options.attackKey.isPressed()) {
         for (long var6 : this.b.keySet()) {
            this.RAsVoid(BlockPos.fromLong(var6));
         }

         this.b.clear();
         this.u.clear();
         this.B.clear();
      } else {
         this.b.keySet().removeIf(var2 -> {
            byte var10000;
            label21: {
               if (var1.crosshairTarget instanceof BlockHitResult var4 && var4.getBlockPos().asLong() == var2) {
                  var10000 = 0;
                  break label21;
               }

               var10000 = 1;
            }

            int var3x = var10000;
            if (var3x != 0) {
               this.RAsVoid(BlockPos.fromLong(var2));
            }

            return var3x != 0;
         });
      }
   }

   public boolean sAsBoolean(BlockPos var1) {
      if (this.BAsBoolean(var1, null)) {
         return true;
      } else {
         MinecraftClient var2 = MinecraftClient.getInstance();
         if (this.XAsBoolean() && var2 != null && var2.world != null && var2.player != null && var1 != null) {
            if (this.MAsBoolean(var2.world.getBlockState(var1)) && this.yAsInt(var1) > this.KAsInt()) {
               long var3 = var1.asLong();
               this.l.putIfAbsent(var3, 0.0F);
               this.HAsVoid(var2, var1, null, 0.0);
               return true;
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   public boolean KAsBoolean(BlockPos var1, Direction var2) {
      if (this.BAsBoolean(var1, var2)) {
         return true;
      } else {
         MinecraftClient var3 = MinecraftClient.getInstance();
         if (this.XAsBoolean() && var3 != null && var3.world != null && var3.player != null && var1 != null) {
            if (this.vAsBoolean(var1) && var3.player.age < this.w) {
               return true;
            } else if (this.MAsBoolean(var3.world.getBlockState(var1)) && this.yAsInt(var1) > this.KAsInt()) {
               long var4 = var1.asLong();
               int var6 = var3.player.age;
               if (this.Do.getOrDefault(var4, -1) == var6) {
                  return true;
               } else {
                  this.Do.put(var4, var6);
                  float var7 = var3.player.getAbilities().creativeMode
                     ? 1.0F
                     : Blocks.SPAWNER.getDefaultState().calcBlockBreakingDelta(var3.player, var3.world, var1);
                  float var8 = Math.min(1.0F, this.l.getOrDefault(var4, 0.0F) + var7);
                  this.l.put(var4, var8);
                  this.HAsVoid(var3, var1, var2, (double)var8);
                  if (var8 >= 1.0F) {
                     if (this.NAsBoolean(var1, this.KAsInt())) {
                        this.w = var6 + 5;
                        this.mAsVoid(var1, true);
                     }

                     this.l.remove(var4);
                     this.Q.remove(var4);
                     this.RAsVoid(var1);
                  }

                  return true;
               }
            } else {
               if (var1 != null) {
                  this.l.remove(var1.asLong());
                  this.Q.remove(var1.asLong());
                  this.RAsVoid(var1);
               }

               return false;
            }
         } else {
            if (var1 != null) {
               this.l.remove(var1.asLong());
               this.Q.remove(var1.asLong());
               this.RAsVoid(var1);
            }

            return false;
         }
      }
   }

   public void gAsVoid(BlockPos var1) {
      if (var1 != null && this.Dx.contains(var1.asLong())) {
         ItemBlockCache.xAsVoid(var1);
      }

      if (var1 != null && this.c.contains(var1.asLong())) {
         this.X.put(var1.asLong(), new FakeSpawnerController.Type_x(var1.toImmutable(), System.currentTimeMillis() + 10000L));
      }

      if (var1 != null && this.yAsInt(var1) > 0) {
         this.X.put(var1.asLong(), new FakeSpawnerController.Type_x(var1.toImmutable(), System.currentTimeMillis() + 10000L));
         int var2 = this.G.getOrDefault(var1.asLong(), 0);
         this.hAsMa_Type_l(var1.asLong(), var2);
         this.NAsVoid(var1.asLong(), var2);
         this.MAsVoid(new ItemStack(this.SAsItem(var1.asLong()), 1), 1);
         VisualInventory.vAsVoid();
         this.l.remove(var1.asLong());
         this.Q.remove(var1.asLong());
         this.RAsVoid(var1);
      }
   }

   private int KAsInt() {
      MinecraftClient var1 = MinecraftClient.getInstance();
      return var1.player != null && (var1.player.isSneaking() || var1.options.sneakKey.isPressed()) ? 64 : 1;
   }

   public boolean cAsBoolean(BlockPos var1) {
      return this.NAsBoolean(var1, 64);
   }

   private boolean NAsBoolean(BlockPos var1, int var2) {
      if (this.lAsBoolean(var1)) {
         return false;
      } else if (this.vAsBoolean(var1)) {
         long var3 = var1.asLong();
         int var5 = this.G.getOrDefault(var3, 0);
         if (var5 <= 0) {
            return false;
         } else {
            int var6 = Math.min(var2, var5);
            Deque var7 = this.N.get(var3);
            if (var7 != null && !var7.isEmpty()) {
               ItemStack var8 = MinecraftClient.getInstance().player.getInventory().getStack(((FakeSpawnerController.Type_l)var7.getLast()).preferredSlot);
               int var9 = 0;

               for (FakeSpawnerController.Type_l var11 : (Iterable<FakeSpawnerController.Type_l>) (var7.reversed())) {
                  if (!ItemStack.areItemsAndComponentsEqual(var8, MinecraftClient.getInstance().player.getInventory().getStack(var11.preferredSlot))) {
                     break;
                  }

                  var9 += var11.stack.getCount();
               }

               if (var9 < var6 && var9 < var5 - 1) {
                  var6 = var9;
               }
            }

            if (!this.mAsBoolean(var3, var6)) {
               return false;
            } else {
               this.NAsVoid(var3, var6);
               return true;
            }
         }
      } else {
         return false;
      }
   }

   private void NAsVoid(long var1, int var3) {
      int var4 = Math.max(0, this.G.getOrDefault(var1, 0) - var3);
      BlockPos var5 = BlockPos.fromLong(var1);
      if (var4 > 0) {
         this.G.put(var1, var4);
         this.f.put(var1, System.currentTimeMillis());
      } else {
         this.c.add(var1);
         this.G.remove(var1);
         this.E.remove(var1);
         this.f.remove(var1);
         this.d.remove(var1);
         this.N.remove(var1);
         this.nAsVoid(var1);
         this.rAsVoid(var5);
         if (var5.equals(this.k)) {
            this.k = null;
         }
      }

      this.X.put(var1, new FakeSpawnerController.Type_x(var5.toImmutable(), System.currentTimeMillis() + 10000L));
      this.UAsVoid();
   }

   public boolean vAsBoolean(BlockPos var1) {
      if (var1 == null) {
         return false;
      } else {
         long var2 = var1.asLong();
         return this.G.getOrDefault(var2, 0) > 0 && !this.e.contains(var2) && this.S == MinecraftClient.getInstance().world && this.XAsBoolean();
      }
   }

   public FakeSpawnerController.Type_d PAsMa_Type_d(BlockPos var1, SoundEvent var2, float var3, float var4) {
      if (var1 != null && var2 != null && this.vAsBoolean(var1)) {
         BlockState var5 = ((BlockItem)this.SAsItem(var1.asLong())).getBlock().getDefaultState();
         if (var5 == null) {
            return null;
         } else {
            BlockSoundGroup var6 = var5.getSoundGroup();
            BlockSoundGroup var7 = Blocks.SPAWNER.getDefaultState().getSoundGroup();
            if (SAsBoolean(var2, var6.getPlaceSound())) {
               return new FakeSpawnerController.Type_d(var7.getPlaceSound(), (var7.getVolume() + 1.0F) / 2.0F, var7.getPitch() * 0.8F);
            } else if (SAsBoolean(var2, var6.getBreakSound())) {
               return new FakeSpawnerController.Type_d(var7.getBreakSound(), (var7.getVolume() + 1.0F) / 2.0F, var7.getPitch() * 0.8F);
            } else {
               return SAsBoolean(var2, var6.getHitSound())
                  ? new FakeSpawnerController.Type_d(var7.getHitSound(), (var7.getVolume() + 1.0F) / 8.0F, var7.getPitch() * 0.5F)
                  : null;
            }
         }
      } else {
         return null;
      }
   }

   private static boolean SAsBoolean(SoundEvent var0, SoundEvent var1) {
      return var0 == var1 || var0.id().equals(var1.id());
   }

   private void sAsVoid(World var1, BlockPos var2) {
      if (var1 != null && var2 != null && this.vAsBoolean(var2)) {
         BlockSoundGroup var3 = Blocks.SPAWNER.getDefaultState().getSoundGroup();
         var1.playSoundClient(
            (double)var2.getX() + 0.5,
            (double)var2.getY() + 0.5,
            (double)var2.getZ() + 0.5,
            var3.getPlaceSound(),
            SoundCategory.BLOCKS,
            (var3.getVolume() + 1.0F) / 2.0F,
            var3.getPitch() * 0.8F,
            false
         );
      }
   }

   private void mAsVoid(BlockPos var1, boolean var2) {
      MinecraftClient var3 = MinecraftClient.getInstance();
      if (var3 != null && var3.world != null && var1 != null && this.vAsBoolean(var1)) {
         BlockSoundGroup var4 = Blocks.SPAWNER.getDefaultState().getSoundGroup();
         var3.world
            .playSoundClient(
               (double)var1.getX() + 0.5,
               (double)var1.getY() + 0.5,
               (double)var1.getZ() + 0.5,
               var2 ? var4.getBreakSound() : var4.getHitSound(),
               SoundCategory.BLOCKS,
               (var4.getVolume() + 1.0F) / (var2 ? 2.0F : 8.0F),
               var4.getPitch() * (var2 ? 0.8F : 0.5F),
               false
            );
      }
   }

   public BlockState QAsBlockState(BlockPos var1, BlockState var2) {
      return this.AAsBlockState(var1, var2);
   }

   public MobSpawnerBlockEntity KAsMobSpawnerBlockEntity(World var1, BlockPos var2) {
      Map var3 = this.v;
      if (var1 != null && var1 == this.S && this.vAsBoolean(var2) && !this.i.containsKey(var2.asLong())) {
         long var4 = var2.asLong();
         MobSpawnerBlockEntity var6 = (MobSpawnerBlockEntity)var3.get(var4);
         if (var6 == null && MinecraftClient.getInstance().isOnThread()) {
            var6 = new MobSpawnerBlockEntity(var2.toImmutable(), Blocks.SPAWNER.getDefaultState());
            var6.setEntityType(this.QAsNa(var2).w, var1.getRandom());
            var6.setWorld(var1);
            var6.getLogic().getRenderedEntity(var1, var2);
            var3.put(var4, var6);
            ((List)this.n.computeIfAbsent(ChunkPos.toLong(var2.getX() >> 4, var2.getZ() >> 4), var0 -> new ArrayList())).add(var6);
         }

         return var3 == this.v && var1 == this.S && this.vAsBoolean(var2) ? var6 : null;
      } else {
         return null;
      }
   }

   private void rAsVoid(BlockPos var1) {
      this.bAsVoid();
      MinecraftClient var2 = MinecraftClient.getInstance();
      if (var2 != null && var2.world != null && var2.world == this.S) {
         this.m++;
         var2.worldRenderer.scheduleBlockRenders(var1.getX() - 1, var1.getY() - 1, var1.getZ() - 1, var1.getX() + 1, var1.getY() + 1, var1.getZ() + 1);
      }
   }

   public Collection<MobSpawnerBlockEntity> jAsCollection(World var1) {
      return (Collection<MobSpawnerBlockEntity>)(var1 == this.S && this.XAsBoolean() ? this.v.values() : List.of());
   }

   private void nAsVoid(long var1) {
      MobSpawnerBlockEntity var3 = this.v.remove(var1);
      if (var3 != null) {
         BlockPos var4 = var3.getPos();
         long var5 = ChunkPos.toLong(var4.getX() >> 4, var4.getZ() >> 4);
         List var7 = (List)this.n.get(var5);
         if (var7 != null) {
            var7.remove(var3);
            if (var7.isEmpty()) {
               this.n.remove(var5);
            }
         }
      }
   }

   public Collection<MobSpawnerBlockEntity> gAsCollection(World var1, Vec3d var2, int var3) {
      if (var1 == this.S && this.XAsBoolean() && !this.n.isEmpty()) {
         ArrayList var4 = null;
         int var5 = MathHelper.floor(var2.x - (double)var3) >> 4;
         int var6 = MathHelper.floor(var2.x + (double)var3) >> 4;
         int var7 = MathHelper.floor(var2.z - (double)var3) >> 4;
         int var8 = MathHelper.floor(var2.z + (double)var3) >> 4;

         for (int var9 = var5; var9 <= var6; var9++) {
            for (int var10 = var7; var10 <= var8; var10++) {
               List var11 = (List)this.n.get(ChunkPos.toLong(var9, var10));
               if (var11 != null) {
                  if (var4 == null) {
                     var4 = new ArrayList();
                  }

                  var4.addAll(var11);
               }
            }
         }

         return (Collection<MobSpawnerBlockEntity>)(Object)(var4 == null ? List.of() : var4);
      } else {
         return List.of();
      }
   }

   public boolean UAsBoolean(BlockEntity var1) {
      if (var1 instanceof MobSpawnerBlockEntity var2) {
         BlockPos var3 = var2.getPos();
         if (var3 == null) {
            return false;
         } else {
            MobSpawnerBlockEntity var4 = this.v.get(var3.asLong());
            return var4 == var1;
         }
      } else {
         return false;
      }
   }

   public void AAsVoid(String var1, BlockPos var2, BlockEntity var3) {
      if (Boolean.getBoolean("larp.debug") && var2 != null) {
         this.pAsVoid(
            "request:" + var1 + ":" + var2.asLong(),
            1500L,
            "{} requested fake block entity pos={} result={} count={}",
            var1,
            var2,
            var3 != null ? var3.getClass().getSimpleName() : "null",
            this.yAsInt(var2)
         );
      }
   }

   public void qAsVoid(String var1, BlockPos var2, String var3) {
      if (Boolean.getBoolean("larp.debug")) {
         if (var2 == null) {
            this.pAsVoid("render:" + var1, 1500L, "renderer {} {}", var1, var3);
         } else {
            this.pAsVoid("render:" + var1 + ":" + var2.asLong(), 1500L, "renderer {} pos={} count={} {}", var1, var2, this.yAsInt(var2), var3);
         }
      }
   }

   public void DAsVoid(WorldChunk var1, Collection<BlockPos> var2) {
      this.IAsVoid();
      this.OAsVoid();
      if (this.XAsBoolean() && var1 != null) {
         int var3 = var1.getPos().x;
         int var4 = var1.getPos().z;
         int var5 = var3 << 4;
         int var6 = var4 << 4;
         int var7 = var5 + 15;
         int var8 = var6 + 15;
         Mutable var9 = new Mutable();
         HashSet var10 = new HashSet<>(this.G.keySet());
         var10.addAll(this.c);

         for (Long var12 : (Iterable<Long>) (var10)) {
            BlockPos var13 = BlockPos.fromLong(var12);
            int var14 = var13.getX();
            int var15 = var13.getZ();
            if (var14 >= var5 && var14 <= var7 && var15 >= var6 && var15 <= var8) {
               var9.set(var14, var13.getY(), var15);
               if (!var1.getWorld().isOutOfHeightLimit(var9)) {
                  this.zAsVoid(var9, var1.getSection(var1.getSectionIndex(var9.getY())).getBlockState(var9.getX() & 15, var9.getY() & 15, var9.getZ() & 15));
               }
            }
         }

         if (var2 != null) {
            for (BlockPos var17 : new ArrayList<>(var2)) {
               if (var17 != null) {
                  this.zAsVoid(var17, var1.getBlockState(var17));
               }
            }
         }
      }
   }

   public void YAsVoid() {
      long var1 = Boolean.getBoolean("larp.debug") ? System.nanoTime() : 0L;
      this.IAsVoid();
      MinecraftClient var3 = MinecraftClient.getInstance();
      if (var3 != null && var3.world != null && var3.player != null && this.XAsBoolean()) {
         this.WAsVoid(var3);
         this.PAsVoid();

         for (Entry var5 : this.D6.entrySet()) {
            if (var3.world.getTime() >= (Long)var5.getValue()) {
               this.D6.remove(var5.getKey());
               this.rAsVoid(BlockPos.fromLong((Long)var5.getKey()));
            }
         }

         this.AAsVoid();
         if (!this.X.isEmpty() || !this.l.isEmpty() || !this.c.isEmpty()) {
            for (Entity var6 : var3.world.getEntities()) {
               if (var6 instanceof ItemEntity var8) {
                  this.FAsBoolean(var8);
               }
            }
         }

         this.vAsVoid();
         this.X.entrySet().removeIf(var0 -> var0.getValue().untilMs < System.currentTimeMillis());
         this.D.removeIf(var1x -> !(var3.world.getEntityById(var1x) instanceof ItemEntity));
         FakeSpawnerTransaction.LAsVoid();
         this.sAsVoid();

         for (Entry var17 : new ArrayList<>(this.E.entrySet())) {
            if (!this.G.containsKey(var17.getKey()) && System.currentTimeMillis() - ((FakeSpawnerController.Type_b)var17.getValue()).createdAtMs > 5000L) {
               this.zAsVoid(BlockPos.fromLong((Long)var17.getKey()), null);
            }
         }

         if (!var3.options.attackKey.isPressed()) {
            for (Long var18 : new ArrayList<>(this.l.keySet())) {
               this.RAsVoid(BlockPos.fromLong(var18));
            }

            this.l.clear();
            this.Q.clear();
         }

         for (Long var19 : new ArrayList<>(this.l.keySet())) {
            if (!(var3.crosshairTarget instanceof BlockHitResult var22) || !var22.getBlockPos().equals(BlockPos.fromLong(var19))) {
               this.RAsVoid(BlockPos.fromLong(var19));
               this.l.remove(var19);
               this.Q.remove(var19);
            }
         }

         if (var3 != null && var3.world != null && var3.player != null && this.XAsBoolean()) {
            for (BlockEntity var21 : this.gAsCollection(var3.world, var3.player.getEntityPos(), 32)) {
               if (var21 instanceof MobSpawnerBlockEntity) {
                  MobSpawnerBlockEntity var23 = (MobSpawnerBlockEntity)var21;
                  if (var23.getPos() != null
                     && var3.world.isChunkLoaded(var23.getPos())
                     && this.vAsBoolean(var23.getPos())
                     && var23.getPos().getSquaredDistance(var3.player.getEntityPos()) <= 1024.0) {
                     MobSpawnerBlockEntity.clientTick(var3.world, var23.getPos(), var23.getCachedState(), var23);
                  }
               }
            }

            long var14 = var3.world.getTime();

            for (BlockPos var26 : this.IAsList(var3.player.getBlockPos(), 16, 8)) {
               if ((var14 + Math.floorMod(var26.asLong(), 3L)) % 20L == 0L) {
                  this.FAsVoid(var3.world, var26);
               }
            }
         } else {
            for (Long var20 : new ArrayList<>(this.l.keySet())) {
               this.RAsVoid(BlockPos.fromLong(var20));
            }

            this.l.clear();
            this.Q.clear();
         }

         if (var1 != 0L) {
            long var15 = System.nanoTime() - var1;
            long var25 = System.currentTimeMillis();
            if (var25 >= this.F) {
               this.F = var25 + 5000L;
               this.VAsVoid(
                  "Work counters: piles={} reservations={} mappingRebuilds={} terrainInvalidations={} inventoryUpdates={} renderCandidates={} renderExtractions={} displayCopies={} tickMicros={}",
                  this.G.size(),
                  this.C.size(),
                  VisualInventory.jAsLong(),
                  this.m,
                  this.O,
                  this.W,
                  this.R,
                  this.o,
                  var15 / 1000L
               );
            }

            if (var15 > 5000000L) {
               this.pAsVoid("tick-spike", 2000L, "Client tick CPU spike micros={} piles={} reservations={}", var15 / 1000L, this.G.size(), this.C.size());
            }
         }
      } else {
         this.rAsVoid();
      }
   }

   public int yAsInt(BlockPos var1) {
      return !this.vAsBoolean(var1) ? 0 : this.G.getOrDefault(var1.asLong(), 0);
   }

   public List<BlockPos> IAsList(BlockPos var1, int var2, int var3) {
      ArrayList var4 = new ArrayList();
      if (this.XAsBoolean() && var1 != null && !this.G.isEmpty()) {
         int var5 = var1.getX();
         int var6 = var1.getY();
         int var7 = var1.getZ();

         for (MobSpawnerBlockEntity var9 : this.gAsCollection(this.S, Vec3d.ofCenter(var1), var2 + 1)) {
            BlockPos var10 = var9.getPos();
            if (this.vAsBoolean(var10)
               && Math.abs(var10.getX() - var5) <= var2
               && Math.abs(var10.getY() - var6) <= var3
               && Math.abs(var10.getZ() - var7) <= var2) {
               var4.add(var10);
            }
         }

         return var4;
      } else {
         return var4;
      }
   }

   public boolean FAsBoolean(ItemEntity var1) {
      if (this.XAsBoolean() && var1 != null && this.HAsBoolean(var1.getStack()) && !Sha256Digest.RAsBoolean(var1.getStack())) {
         long var2 = System.currentTimeMillis();
         if (this.D.contains(var1.getId())) {
            return true;
         } else {
            HashSet var4 = new HashSet<>(this.c);
            var4.addAll(this.l.keySet());

            for (long var6 : (Iterable<Long>) (var4)) {
               BlockPos var8 = BlockPos.fromLong(var6);
               if (var1.squaredDistanceTo((double)var8.getX() + 0.5, (double)var8.getY() + 0.5, (double)var8.getZ() + 0.5) <= 4.84) {
                  this.D.add(var1.getId());
                  return true;
               }
            }

            this.X.entrySet().removeIf(var2xx -> var2xx.getValue().untilMs < var2);

            for (FakeSpawnerController.Type_x var14 : this.X.values()) {
               double var7 = (double)var14.pos.getX() + 0.5 - var1.getX();
               double var9 = (double)var14.pos.getY() + 0.5 - var1.getY();
               double var11 = (double)var14.pos.getZ() + 0.5 - var1.getZ();
               if (var7 * var7 + var9 * var9 + var11 * var11 <= 4.840000000000001) {
                  this.D.add(var1.getId());
                  return true;
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public boolean hAsBoolean(ItemEntity var1) {
      return this.FAsBoolean(var1);
   }

   public ItemStack XAsItemStack(ItemStack var1) {
      if (!this.DAsBoolean(var1)) {
         return var1;
      } else {
         ItemStack var2 = new ItemStack(Items.SPAWNER, var1.getCount());
         var2.set(DataComponentTypes.CUSTOM_NAME, this.RAsText(var1));
         var2.set(DataComponentTypes.LORE, new LoreComponent(List.of(RAsMutableText(this.LAsNa(var1.getItem()).g, Formatting.YELLOW))));
         if (this.qAsBoolean()) {
            var2.set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);
         }

         return var2;
      }
   }

   public ItemStack gAsItemStack(ItemStack var1) {
      return var1;
   }

   public boolean lAsBoolean(ItemStack var1) {
      return this.XAsBoolean() && var1 != null && !var1.isEmpty() && !AuctionMenuText.nAsBoolean(var1) && this.MAsBoolean(var1) && this.mAsLong(var1) > 0L;
   }

   public Text EAsText(long var1) {
      return RAsMutableText("~", Formatting.DARK_GRAY).append(iAsMutableText("$ ", 65280)).append(RAsMutableText(this.GAsString(var1), Formatting.WHITE));
   }

   public Text sAsText(ItemStack var1) {
      return this.OAsText(this.QAsLong(var1));
   }

   public Text SAsText(ItemStack var1) {
      long var2 = this.mAsLong(var1);
      long var4 = Math.max(1L, (var2 + 63L) / 64L);
      return RAsMutableText("Contains: " + var4 + " stack(s)", Formatting.WHITE);
   }

   public Text SAsText() {
      return RAsMutableText("Left Shift", Formatting.GOLD)
         .append(RAsMutableText(": ", Formatting.GOLD))
         .append(RAsMutableText("view contents", Formatting.WHITE));
   }

   public Text nAsText() {
      return RAsMutableText("Left Alt + Left Shift", Formatting.GOLD)
         .append(RAsMutableText(": ", Formatting.GOLD))
         .append(RAsMutableText("view contents", Formatting.WHITE));
   }

   public Text kAsText() {
      return RAsMutableText("Left Control", Formatting.GOLD)
         .append(RAsMutableText(": ", Formatting.GOLD))
         .append(RAsMutableText("lock tooltip", Formatting.WHITE));
   }

   public List<Text> BAsList(ItemStack var1, boolean var2) {
      ArrayList var3 = new ArrayList();
      var3.add(var1.getName());
      var3.add(this.sAsText(var1));
      if (!var2) {
         return var3;
      } else {
         boolean var4 = this.lAsBoolean(341);
         boolean var5 = this.lAsBoolean(340);
         boolean var6 = this.lAsBoolean(342);
         if (var4) {
            return var3;
         } else if (var5 && var6) {
            var3.add(this.kAsText());
            return var3;
         } else if (var5) {
            var3.add(this.SAsText(var1));
            var3.add(this.nAsText());
            var3.add(this.kAsText());
            return var3;
         } else {
            var3.add(this.SAsText(var1));
            var3.add(this.SAsText());
            return var3;
         }
      }
   }

   public Text ZAsText() {
      FakeSpawner var1 = FakeSpawner.kAsFakeSpawner();
      String var2 = var1 != null ? var1.oAsString() : "§dSpawner";
      return iAsText(var2);
   }

   public Text RAsText(ItemStack var1) {
      Text var2 = (Text)var1.get(DataComponentTypes.CUSTOM_NAME);
      if (var2 != null) {
         return Text.literal(var2.getString()).formatted(new Formatting[]{Formatting.WHITE, Formatting.ITALIC});
      } else {
         this.LAsNa(var1.getItem());
         return RAsMutableText("Spawner", Formatting.LIGHT_PURPLE);
      }
   }

   public List<Text> QAsList(int var1, boolean var2, boolean var3) {
      return this.SAsList(new ItemStack(this.lAsItem(), var1), var2, var3);
   }

   public List<Text> SAsList(ItemStack var1, boolean var2, boolean var3) {
      int var4 = var1.getCount();
      ArrayList var5 = new ArrayList();
      var5.add(RAsMutableText(this.LAsNa(var1.getItem()).g, Formatting.YELLOW));
      if (var2) {
         var5.add(
            RAsMutableText("~", Formatting.DARK_GRAY).append(iAsMutableText("$ ", 65280)).append(RAsMutableText(this.CAsString((long)var4), Formatting.WHITE))
         );
      }

      var5.add(Text.empty());
      var5.add(RAsMutableText("Interact with Spawn Egg:", Formatting.GRAY));
      var5.add(RAsMutableText(" Sets Mob Type", Formatting.BLUE));
      if (var3) {
         var5.add(RAsMutableText("minecraft:spawner", Formatting.DARK_GRAY));
         var5.add(RAsMutableText("13 component(s)", Formatting.DARK_GRAY));
      }

      return var5;
   }

   public boolean NAsBoolean() {
      MinecraftClient var1 = MinecraftClient.getInstance();
      if (var1 == null) {
         return false;
      } else if (var1.currentScreen instanceof HandledScreen var2) {
         Text var4 = var2.getTitle();
         return var4 == null ? false : var4.getString().toLowerCase(Locale.ROOT).contains("shulker");
      } else {
         return false;
      }
   }

   public boolean qAsBoolean() {
      FakeSpawner var1 = FakeSpawner.kAsFakeSpawner();
      return var1 != null && var1.enchantGlint();
   }

   public void NAsVoid() {
      this.lAsVoid(this.k);
   }

   public void lAsVoid(BlockPos var1) {
      this.IAsVoid();
      MinecraftClient var2 = MinecraftClient.getInstance();
      if (this.XAsBoolean() && var2 != null && var2.world != null && var2.player != null) {
         this.k = var1 == null ? null : var1.toImmutable();
         long var3 = this.wAsLong(var1);
         int var5 = this.hAsInt(var1);
         SpawnerEntityCatalog var6 = this.QAsNa(var1);
         FakeSpawnerController.Type_m var7 = this.wAsMa_Type_m(var3, var5, var6);
         Map<Item, Integer> var8 = var1 == null ? Map.of() : this.d.getOrDefault(var1.asLong(), Map.of());
         var7 = new FakeSpawnerController.Type_m(
            Math.max(0, var7.l - var8.getOrDefault(var6.a, 0) - ShopPurchaseController.kAsInt(var6.a)),
            Math.max(0, var7.v - var8.getOrDefault(var6.o, 0) - ShopPurchaseController.kAsInt(var6.o)),
            var7.B,
            var7.r
         );
         String var9 = var5 + " " + tAsString(var6.g.toLowerCase(Locale.ROOT) + " spawner" + (var5 == 1 ? "" : "s"));
         SimpleInventory var10 = new SimpleInventory(54);
         int var11 = uAsInt(var10, 0, var6.a, var7.l);
         uAsInt(var10, var11, var6.o, var7.v);
         var10.setStack(
            48,
            TAsItemStack(
               Items.GOLD_INGOT.getDefaultStack(),
               RAsMutableText("Sell All", Formatting.WHITE),
               List.of(RAsMutableText("Click to sell all loot", Formatting.GRAY).formatted(Formatting.ITALIC))
            )
         );
         var10.setStack(
            49,
            TAsItemStack(
               var6.AAsItemStack(),
               mAsText(var5 + " " + var6.g.toLowerCase(Locale.ROOT) + " spawner" + (var5 == 1 ? "" : "s"), 64648),
               this.pAsList(var7, var6)
            )
         );
         var10.setStack(
            50,
            TAsItemStack(
               Items.DROPPER.getDefaultStack(),
               RAsMutableText("Drop Loot", Formatting.WHITE),
               List.of(RAsMutableText("Click to drop all loot on the page", Formatting.GRAY).formatted(Formatting.ITALIC))
            )
         );
         GenericContainerScreenHandler var12 = new GenericContainerScreenHandler(ScreenHandlerType.GENERIC_9X6, 0, var2.player.getInventory(), var10, 6);
         var2.setScreen(new SpawnerLootScreen(var12, var2.player.getInventory(), Text.literal(var9), var1));
      }
   }

   public void oAsVoid(BlockPos var1, Item var2, int var3) {
      if (var1 != null && var2 != null && var3 > 0) {
         this.d.computeIfAbsent(var1.asLong(), var0 -> new HashMap<>()).merge(var2, var3, Integer::sum);
      }
   }

   public long QAsLong(ItemStack var1) {
      if (!this.XAsBoolean() || var1.isEmpty() || AuctionMenuText.nAsBoolean(var1)) {
         return 0L;
      } else if (this.HAsBoolean(var1)) {
         return (long)var1.getCount() * 1000L;
      } else if (SpawnerEntityCatalog.oAsBoolean(var1.getItem())) {
         return this.WAsLong(var1.getItem(), var1.getCount());
      } else {
         ContainerComponent var2 = (ContainerComponent)var1.get(DataComponentTypes.CONTAINER);
         return var2 == null
            ? 0L
            : StreamSupport.<ItemStack>stream(var2.iterateNonEmptyCopy().spliterator(), false).mapToLong(this::QAsLong).sum() * (long)var1.getCount();
      }
   }

   public Text OAsText(long var1) {
      double var3 = var1 >= 1000000000L ? 1.0E9 : (var1 >= 1000000L ? 1000000.0 : (var1 >= 1000L ? 1000.0 : 1.0));
      String var5 = var3 == 1.0E9 ? "B" : (var3 == 1000000.0 ? "M" : (var3 == 1000.0 ? "K" : ""));
      return RAsMutableText("~", Formatting.DARK_GRAY)
         .append(iAsMutableText("$ ", 65280))
         .append(RAsMutableText(SAsString((double)var1 / var3, var3 == 1.0 ? 0 : 1) + var5, Formatting.WHITE));
   }

   public long LAsLong(int var1) {
      return this.WAsLong(Items.BONE, var1);
   }

   public long WAsLong(Item var1, int var2) {
      Double var3 = FakeSpawner.kAsFakeSpawner().bAsDouble2(Registries.ITEM.getId(var1).toString());
      if (var3 != null) {
         return Math.round(var3 * (double)Math.max(0, var2));
      } else {
         String var4 = Registries.ITEM.getId(var1).getPath().replace('_', ' ');
         DonutAuctionApi var5 = DonutAuctionApi.ZAsA6();
         var5.vAsVoid(var4, var0 -> {
         });
         double var6 = this.s.getOrDefault(var1, (double)var5.WAsLong(var4));
         return Math.round(var6 * (double)Math.max(0, var2));
      }
   }

   public void vAsVoid() {
      MinecraftClient var1 = MinecraftClient.getInstance();
      if (var1.player != null && var1.currentScreen != null && var1.currentScreen.getTitle().getString().toLowerCase(Locale.ROOT).contains("order")) {
         ScreenHandler var2 = var1.player.currentScreenHandler;
         HashMap<Item, Double> var3 = new HashMap<>();

         for (Slot var5 : var2.slots) {
            if (var5.inventory != var1.player.getInventory()) {
               ItemStack var6 = var5.getStack();
               if (SpawnerEntityCatalog.oAsBoolean(var6.getItem())) {
                  LoreComponent var7 = (LoreComponent)var6.get(DataComponentTypes.LORE);
                  if (var7 != null) {
                     for (Text var9 : var7.lines()) {
                        String var10 = var9.getString().toLowerCase(Locale.ROOT);
                        if (var10.contains("each") || var10.contains("per item") || var10.contains("unit price")) {
                           Matcher var11 = Pattern.compile("\\$\\s*([0-9][0-9,]*(?:\\.[0-9]+)?)\\s*([kmb]?)").matcher(var10);
                           if (var11.find()) {
                              double var12 = Double.parseDouble(var11.group(1).replace(",", ""));
                              String var14 = var11.group(2);

                              var12 *= (double)(switch (var14) {
                                 case "k" -> 1000;
                                 case "m" -> 1000000;
                                 case "b" -> 1000000000;
                                 default -> 1;
                              });
                              if (Double.isFinite(var12) && var12 > 0.0) {
                                 var3.merge(var6.getItem(), var12, Math::max);
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

         var3.forEach((var1x, var2x) -> this.s.merge(var1x, var2x, Math::max));
      }
   }

   public int eAsInt() {
      return this.uAsInt(Items.BONE);
   }

   private boolean BAsBoolean(ItemStack var1) {
      if (var1.contains(DataComponentTypes.CUSTOM_NAME)) {
         return false;
      } else {
         FakeSpawner var2 = FakeSpawner.kAsFakeSpawner();

         for (SpawnerEntityCatalog var6 : SpawnerEntityCatalog.values()) {
            String var7 = var2.zAsString(var6.ordinal());
            if (var7 != null && !var7.isBlank() && var1.isOf(var6.a)) {
               return true;
            }
         }

         return false;
      }
   }

   private int uAsInt(Item var1) {
      MinecraftClient var2 = MinecraftClient.getInstance();
      if (this.XAsBoolean() && var2.player != null) {
         int var3 = -1;
         int var4 = 0;

         for (int var5 = 0; var5 < 36; var5++) {
            ItemStack var6 = var2.player.getInventory().getStack(var5);
            if (var6.isOf(var1) && !var6.contains(DataComponentTypes.CUSTOM_NAME) && !WaterOverlay.dAsBoolean(var6) && var6.getCount() > var4) {
               var3 = var5;
               var4 = var6.getCount();
            }
         }

         return this.z = var3;
      } else {
         return -1;
      }
   }

   public void nAsVoid(int var1) {
   }

   public void jAsVoid(ScreenHandler var1, Slot var2) {
   }

   public void iAsVoid(Slot var1) {
   }

   public void hAsVoid(BlockPos var1) {
      if (var1 != null && this.lAsBoolean(var1.asLong())) {
         this.RAsVoid(var1);
         this.rAsVoid(var1);
         ClientLog.XAsVoid("FakeSpawner", "Final break predicted at {}; retaining pile and reservations until server acknowledgment", var1);
      }
   }

   boolean lAsBoolean(long var1) {
      return this.G.getOrDefault(var1, 0) > 0 && this.e.add(var1);
   }

   boolean kAsBoolean(long var1) {
      return this.e.remove(var1);
   }

   public BlockState ZAsBlockState(World var1, BlockPos var2, BlockState var3) {
      if (this.XAsBoolean() && var1 == this.S && this.G.getOrDefault(var2.asLong(), 0) > 0 && var1 instanceof ClientWorld var4) {
         if (var1.isOutOfHeightLimit(var2)) {
            return Blocks.AIR.getDefaultState();
         } else {
            WorldChunk var5 = var4.getWorldChunk(var2);
            return var5.getSection(var5.getSectionIndex(var2.getY())).getBlockState(var2.getX() & 15, var2.getY() & 15, var2.getZ() & 15);
         }
      } else {
         return var3;
      }
   }

   public void MAsVoid(Runnable var1) {
      MinecraftClient var2 = MinecraftClient.getInstance();
      if (this.XAsBoolean() && var2.player != null && var2.isOnThread()) {
         try {
            this.OAsVoid(var2.player.getInventory(), var1);
         } finally {
            this.PAsVoid();
         }
      } else {
         var1.run();
      }
   }

   void OAsVoid(Inventory var1, Runnable var2) {
      try {
         var2.run();
      } finally {
         VisualInventory.vAsVoid();
      }
   }

   private void KAsVoid(int var1, ItemStack var2) {
      ItemStack var3 = this.C.get(var1);
      if (var3 != null && var2.isOf(var3.getItem()) && var2.getCount() >= var3.getCount() && !ItemStack.areItemsAndComponentsEqual(var2, var3)) {
         this.C.put(var1, var2.copyWithCount(var3.getCount()));

         for (Deque var5 : this.N.values()) {
            ArrayDeque var6 = new ArrayDeque();

            for (FakeSpawnerController.Type_l var8 : (Iterable<FakeSpawnerController.Type_l>) (var5)) {
               var6.addLast(
                  var8.preferredSlot == var1 && var8.stack.isOf(var2.getItem())
                     ? new FakeSpawnerController.Type_l(var1, var2.copyWithCount(var8.stack.getCount()))
                     : var8
               );
            }

            var5.clear();
            var5.addAll(var6);
         }
      }
   }

   public boolean hAsBoolean(ItemStack var1) {
      return this.XAsBoolean() && !var1.isEmpty() && this.C.values().stream().anyMatch(var1xx -> ItemStack.areItemsAndComponentsEqual(var1, var1xx));
   }

   public boolean AAsBoolean(int var1, int var2, int var3, SlotActionType var4) {
      if (!this.XAsBoolean() || FakeSpawnerTransaction.SAsBoolean()) {
         return true;
      } else if (FakeSpawnerTransaction.aAsBoolean()) {
         return false;
      } else {
         ClientPlayerEntity var5 = MinecraftClient.getInstance().player;
         return var5 != null && var5.currentScreenHandler.syncId == var1 ? this.eAsBoolean(var5.currentScreenHandler, var2, var3, var4) : true;
      }
   }

   public boolean eAsBoolean(ScreenHandler var1, int var2, int var3, SlotActionType var4) {
      if (!this.XAsBoolean() || FakeSpawnerTransaction.SAsBoolean()) {
         return true;
      } else if (FakeSpawnerTransaction.aAsBoolean()) {
         return false;
      } else {
         ClientPlayerEntity var5 = MinecraftClient.getInstance().player;
         if (var5 == null) {
            return true;
         } else if (var1 != var5.currentScreenHandler && var1 != var5.playerScreenHandler) {
            return false;
         } else {
            if (var2 >= 0 && var2 < var1.slots.size()) {
               Slot var6 = var1.getSlot(var2);
               if (var6.inventory == var5.getInventory()
                  && (this.vAsBoolean(var6.getIndex()) || ShopPurchaseController.UAsBoolean(var6.getIndex()) || this.qAsBoolean(var6.getIndex()))) {
                  return false;
               }
            }

            return var4 != SlotActionType.SWAP || !this.vAsBoolean(var3) && !ShopPurchaseController.UAsBoolean(var3) && !this.qAsBoolean(var3)
               ? var4 != SlotActionType.PICKUP_ALL || !this.hAsBoolean(var1.getCursorStack())
               : false;
         }
      }
   }

   public void pAsVoid(int var1, Runnable var2) {
      this.MAsVoid(var2);
   }

   public boolean WAsBoolean(int var1, int var2) {
      MinecraftClient var3 = MinecraftClient.getInstance();
      ItemStack var4 = this.C.get(var2);
      if (var4 == null || var4.isEmpty()) {
         return false;
      } else if (var3.player != null
         && var3.interactionManager != null
         && var1 >= 9
         && var1 < 36
         && !this.vAsBoolean(var1)
         && var3.player.playerScreenHandler.getCursorStack().isEmpty()
         && var3.player.currentScreenHandler == var3.player.playerScreenHandler) {
         if (this.BAsBoolean(var3.player.getInventory().getStack(var1))) {
            this.nAsVoid(var2);
         }

         FakeSpawnerTransaction.QAsVoid(() -> var3.interactionManager.clickSlot(0, var1, var2, SlotActionType.SWAP, var3.player));
         if (var4 != null && !var4.isEmpty()) {
            this.C.remove(var2);
            this.C.put(var1, var4);

            for (Deque var6 : this.N.values()) {
               ArrayDeque var7 = new ArrayDeque();

               for (FakeSpawnerController.Type_l var9 : (Iterable<FakeSpawnerController.Type_l>) (var6)) {
                  var7.addLast(var9.preferredSlot == var2 ? new FakeSpawnerController.Type_l(var1, var9.stack) : var9);
               }

               var6.clear();
               var6.addAll(var7);
            }

            VisualInventory.vAsVoid();
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean vAsBoolean(int var1) {
      return this.XAsBoolean() && this.C.containsKey(var1) && !this.C.get(var1).isEmpty();
   }

   public int hAsInt(int var1, ItemStack var2) {
      ItemStack var3 = this.C.get(var1);
      return this.XAsBoolean() && var3 != null && var2.isOf(var3.getItem()) ? Math.max(0, var2.getCount() - var3.getCount()) : 0;
   }

   public ItemStack sAsItemStack(int var1, ItemStack var2) {
      if (!this.vAsBoolean(var1)) {
         return var2;
      } else {
         int var3 = this.hAsInt(var1, var2);
         return var3 == 0 ? ItemStack.EMPTY : Sha256Digest.fAsItemStack(var2.copyWithCount(var3));
      }
   }

   public boolean XAsBoolean(ScreenHandler var1, List<FakeSpawnerTransaction.Type_z> var2) {
      MinecraftClient var3 = MinecraftClient.getInstance();
      if (var3.player != null
         && var1 == var3.player.playerScreenHandler
         && var3.player.isInCreativeMode()
         && var3.player.currentScreenHandler instanceof CreativeScreenHandler) {
         this.MAsVoid(() -> {
            for (FakeSpawnerTransaction.Type_z var4 : var2) {
               var1.onSlotClick(var4.slot(), var4.button(), var4.type(), var3.player);
            }

            var1.sendContentUpdates();
         });
         return true;
      } else {
         return false;
      }
   }

   public boolean jAsBoolean(ScreenHandler var1, Slot var2, int var3) {
      MinecraftClient var4 = MinecraftClient.getInstance();
      if (var4.player != null && var2 != null && var2.inventory == var4.player.getInventory() && var3 >= 0 && var3 <= 1) {
         int var5 = var2.getIndex();
         ItemStack var6 = var4.player.getInventory().getStack(var5);
         int var7 = this.hAsInt(var5, var6);
         if (var7 != 0 && var1.getCursorStack().isEmpty()) {
            int var8 = var3 == 0 ? var7 : (var7 + 1) / 2;
            ArrayList var9 = new ArrayList();
            var9.add(new FakeSpawnerTransaction.Type_z(var2.id, 0, SlotActionType.PICKUP));

            for (int var10 = 0; var10 < var6.getCount() - var8; var10++) {
               var9.add(new FakeSpawnerTransaction.Type_z(var2.id, 1, SlotActionType.PICKUP));
            }

            int var11 = var6.getCount() - var7;
            if (this.XAsBoolean(var1, var9)) {
               ClientLog.XAsVoid("FakeSpawner", "Creative surplus picked physical={} amount={} reserved={}", var5, var8, var11);
               return true;
            } else {
               FakeSpawnerTransaction.QAsBoolean(
                  var1, "pick unreserved surplus", var9, var6.copyWithCount(var8), VisualInventory::vAsVoid, VisualInventory::vAsVoid
               );
               return true;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private int TAsInt() {
      PlayerInventory var1 = MinecraftClient.getInstance().player.getInventory();

      for (int var2 = 0; var2 < 36; var2++) {
         if (!this.vAsBoolean(var2) && !this.qAsBoolean(var2) && var1.getStack(var2).isEmpty()) {
            return var2;
         }
      }

      return -1;
   }

   public int KAsInt(int var1) {
      return this.yAsInt(Items.BONE, var1);
   }

   public int yAsInt(Item var1, int var2) {
      return ShopPurchaseController.GAsInt(var1, var2);
   }

   public boolean zAsBoolean(ItemStack var1) {
      return false;
   }

   public boolean xAsBoolean() {
      return this.VAsInt(1) == 1;
   }

   public int VAsInt(int var1) {
      return this.HAsInt(Items.BONE, var1);
   }

   public int HAsInt(Item var1, int var2) {
      return this.yAsInt(var1, var2);
   }

   public boolean dAsBoolean(int var1) {
      return var1 == 48 || var1 == 50;
   }

   public boolean hAsBoolean(Text var1, ScreenHandler var2) {
      if (var1 != null && var2 != null) {
         String var3 = var1.getString().toLowerCase(Locale.ROOT);
         return var3.contains("sell") && var2.slots.size() >= 72;
      } else {
         return false;
      }
   }

   public Text uAsText(Text var1) {
      return var1 == null ? Text.literal(tAsString("place items in here to sell")) : Text.literal(tAsString(var1.getString()));
   }

   public void wAsVoid(Text var1, ScreenHandler var2) {
      if (this.XAsBoolean() && this.hAsBoolean(var1, var2)) {
         long var3 = 0L;

         for (int var5 = 0; var5 < Math.max(0, var2.slots.size() - 36); var5++) {
            var3 += this.QAsLong(var2.getSlot(var5).getStack());
         }

         if (var3 > 0L) {
            com.larpclient.AuctionNotifier.qAsVoid(var3);
         }
      }
   }

   private long wAsLong(BlockPos var1) {
      long var2 = System.currentTimeMillis();
      return var1 == null ? var2 : this.f.computeIfAbsent(var1.asLong(), var2xx -> var2);
   }

   private int hAsInt(BlockPos var1) {
      return var1 == null ? Math.max(1, FakeSpawner.kAsFakeSpawner().previewCount()) : Math.max(1, this.G.getOrDefault(var1.asLong(), 1));
   }

   private FakeSpawnerController.Type_m pAsMa_Type_m(long var1, int var3) {
      return this.wAsMa_Type_m(var1, var3, SpawnerEntityCatalog.SKELETON);
   }

   private FakeSpawnerController.Type_m wAsMa_Type_m(long var1, int var3, SpawnerEntityCatalog var4) {
      double var5 = this.tAsDouble(var3);
      double var7 = var4.o == Items.AIR ? 0.0 : Math.max(0.15, var5 * 0.33);
      long var9 = Math.max(0L, System.currentTimeMillis() - var1);
      long var11 = var9 / 60000L;
      return new FakeSpawnerController.Type_m(Math.max(0, (int)((double)var11 * var5)), Math.max(0, (int)((double)var11 * var7)), var5, var7);
   }

   private double tAsDouble(int var1) {
      int var2 = Math.max(1, var1);
      if (var2 == 1) {
         return 1.9;
      } else {
         return var2 <= 64 ? (double)var2 * 1.9453125 : 124.5 + (double)(var2 - 64) * 0.88671875;
      }
   }

   private static int uAsInt(SimpleInventory var0, int var1, Item var2, int var3) {
      int var4 = Math.max(0, var3);

      while (var4 > 0 && var1 < 45) {
         int var5 = Math.min(64, var4);
         var0.setStack(var1++, new ItemStack(var2, var5));
         var4 -= var5;
      }

      return var1;
   }

   private List<Text> pAsList(FakeSpawnerController.Type_m var1, SpawnerEntityCatalog var2) {
      int var3 = var1 == null ? 0 : Math.max(0, var1.v);
      int var4 = var1 == null ? 0 : Math.max(0, var1.l);
      if (var3 <= 0 && var4 <= 0) {
         return List.of(iAsMutableText("(0% filled)", 64648));
      } else {
         ArrayList var5 = new ArrayList();
         if (var3 > 0) {
            var5.add(iAsMutableText(var3 + " ", 64648).append(var2.o.getName(new ItemStack(var2.o)).copy().formatted(Formatting.WHITE)));
         }

         if (var4 > 0) {
            var5.add(iAsMutableText(var4 + " ", 64648).append(var2.a.getName(new ItemStack(var2.a)).copy().formatted(Formatting.WHITE)));
         }

         var5.add(iAsMutableText("(0% filled)", 64648));
         return var5;
      }
   }

   private boolean mAsBoolean(long var1, int var3) {
      MinecraftClient var4 = MinecraftClient.getInstance();
      if (var4.player != null
         && !FakeSpawnerTransaction.aAsBoolean()
         && var4.player.currentScreenHandler == var4.player.playerScreenHandler
         && var4.player.playerScreenHandler.getCursorStack().isEmpty()) {
         Deque var5 = this.N.get(var1);
         if (var5 != null && !var5.isEmpty()) {
            LinkedHashMap<Integer, Integer> var6 = new LinkedHashMap<>();
            int var7 = var3;

            for (FakeSpawnerController.Type_l var9 : (Iterable<FakeSpawnerController.Type_l>) (var5.reversed())) {
               ItemStack var10 = var4.player.getInventory().getStack(var9.preferredSlot);
               if (!var10.isOf(var9.stack.getItem()) || var10.getCount() < var9.stack.getCount()) {
                  return false;
               }

               int var11 = Math.min(var7, var9.stack.getCount());
               var6.merge(Integer.valueOf(var9.preferredSlot), Integer.valueOf(var11), Integer::sum);
               var7 -= var11;
               if (var7 <= 0) {
                  break;
               }
            }

            int var13 = var3 - var7;
            int var14 = -1;

            for (Entry var17 : var6.entrySet()) {
               ItemStack var12 = this.C.get(var17.getKey());
               if (var12 != null
                  && var12.getCount() == (Integer)var17.getValue()
                  && var4.player.getInventory().getStack((Integer)var17.getKey()).getCount() == (Integer)var17.getValue()) {
                  var14 = (Integer)var17.getKey();
                  break;
               }
            }

            if (var14 < 0) {
               var14 = this.TAsInt();
            }

            if (var14 < 0) {
               return false;
            } else {
               ArrayList var16 = new ArrayList();

               for (Entry var20 : var6.entrySet()) {
                  if ((Integer)var20.getKey() != var14) {
                     var16.addAll(
                        FakeSpawnerTransaction.VAsList(
                           (Integer)var20.getKey(), var14, (Integer)var20.getValue(), var4.player.getInventory().getStack((Integer)var20.getKey()).getCount()
                        )
                     );
                  }
               }

               if (var16.isEmpty()) {
                  this.hAsMa_Type_l(var1, var3);
                  if (var7 > 0) {
                     this.MAsVoid(new ItemStack(this.SAsItem(var1), var7), var7);
                  }

                  VisualInventory.vAsVoid();
                  return true;
               } else {
                  int var19 = var14;
                  int var21 = var7;
                  FakeSpawnerTransaction.nAsBoolean("mining return", var16, () -> {
                     this.hAsMa_Type_l(var1, var3);
                     this.NAsVoid(var1, var3);
                     if (var21 > 0) {
                        this.MAsVoid(new ItemStack(this.SAsItem(var1), var21), var21);
                     }

                     this.mAsVoid(BlockPos.fromLong(var1), true);
                     VisualInventory.vAsVoid();
                     this.VAsVoid("Mining return predicted pile={} physical={} amount={} worldPending={}", var1, var19, var13, var21);
                  }, () -> this.sAsVoid());
                  return false;
               }
            }
         } else {
            this.MAsVoid(new ItemStack(this.SAsItem(var1), var3), var3);
            return true;
         }
      } else {
         return false;
      }
   }

   private void MAsVoid(ItemStack var1, int var2) {
      if (var2 > 0 && !var1.isEmpty()) {
         PlayerInventory var3 = MinecraftClient.getInstance().player.getInventory();
         int var4 = -1;

         for (int var5 = 0; var5 < 36; var5++) {
            if (!this.vAsBoolean(var5)) {
               ItemStack var6 = var3.getStack(var5);
               FakeSpawnerController.Type_c var7 = this.L.get(var5);
               if (var7 != null && ItemStack.areItemsAndComponentsEqual(var7.stack, var1) && var7.expected + var2 <= var1.getMaxCount()) {
                  var4 = var5;
                  break;
               }

               if (var7 == null && !var6.isEmpty() && ItemStack.areItemsAndComponentsEqual(var6, var1) && var6.getCount() + var2 <= var6.getMaxCount()) {
                  var4 = var5;
                  break;
               }
            }
         }

         if (var4 < 0) {
            var4 = this.TAsInt();
         }

         if (var4 >= 0) {
            int var8 = var3.getStack(var4).isEmpty() && !this.L.containsKey(var4) ? VisualInventory.JAsInt() : -1;
            this.UAsVoid(var4, var1, 0, var2);
            if (var8 >= 0) {
               VisualInventory.YAsVoid(var8, var4);
            }
         }
      }
   }

   private void UAsVoid(int var1, ItemStack var2, int var3, int var4) {
      if (var4 > 0) {
         FakeSpawnerController.Type_c var5 = this.L.get(var1);
         int var6 = MinecraftClient.getInstance().player.getInventory().getStack(var1).getCount();
         int var7 = var5 != null && ItemStack.areItemsAndComponentsEqual(var5.stack, var2) ? Math.max(var5.expected + var3, var6) : var6;
         this.L.put(var1, new FakeSpawnerController.Type_c(var2.copyWithCount(1), var7 + var4, MinecraftClient.getInstance().player.age + 100));
         VisualInventory.GAsVoid();
      }
   }

   public boolean qAsBoolean(int var1) {
      return this.L.containsKey(var1);
   }

   public ItemStack MAsItemStack(int var1, ItemStack var2) {
      FakeSpawnerController.Type_c var3 = this.L.get(var1);
      return var3 != null && var2.isEmpty() ? Sha256Digest.fAsItemStack(var3.stack.copyWithCount(var3.expected)) : var2;
   }

   private FakeSpawnerController.Type_l hAsMa_Type_l(long var1, int var3) {
      Deque var4 = this.N.get(var1);
      if (var4 != null && !var4.isEmpty() && var3 > 0) {
         int var5 = var3;
         int var6 = -1;
         ItemStack var7 = ItemStack.EMPTY;

         while (var5 > 0 && !var4.isEmpty()) {
            FakeSpawnerController.Type_l var8 = (FakeSpawnerController.Type_l)var4.removeLast();
            if (var8 != null && var8.stack != null && !var8.stack.isEmpty()) {
               if (var6 < 0) {
                  var6 = var8.preferredSlot;
               }

               if (var7.isEmpty()) {
                  var7 = var8.stack.copy();
               }

               int var9 = Math.min(var5, var8.stack.getCount());
               ItemStack var10 = this.C.get(var8.preferredSlot);
               if (var10 != null && ItemStack.areItemsAndComponentsEqual(var10, var8.stack)) {
                  if (var10.getCount() <= var9) {
                     this.C.remove(var8.preferredSlot);
                  } else {
                     this.C.put(var8.preferredSlot, var10.copyWithCount(var10.getCount() - var9));
                  }
               }

               var5 -= var9;
               int var11 = var8.stack.getCount() - var9;
               if (var11 > 0) {
                  var4.addLast(new FakeSpawnerController.Type_l(var8.preferredSlot, var8.stack.copyWithCount(var11)));
               }
            }
         }

         if (var4.isEmpty()) {
            this.N.remove(var1);
         }

         return var7.isEmpty() ? null : new FakeSpawnerController.Type_l(var6, var7.copyWithCount(var3));
      } else {
         return null;
      }
   }

   private boolean mAsBoolean(long var1, FakeSpawnerController.Type_l var3, boolean var4) {
      if (var3 != null && !var3.stack.isEmpty()) {
         MinecraftClient var5 = MinecraftClient.getInstance();
         if (var5.player == null) {
            return false;
         } else {
            int var6 = var3.preferredSlot;
            if (var6 >= 0 && var6 < var5.player.getInventory().size() && !this.C.containsKey(var6)) {
               ItemStack var7 = var5.player.getInventory().getStack(var6);
               int var8 = var3.stack.getCount();
               int var9 = var4 && !var5.player.isInCreativeMode() ? var8 - 1 : var8;
               if (var9 <= 0) {
                  return true;
               } else if (var7.isOf(var3.stack.getItem()) && var7.getCount() >= var9) {
                  this.C.put(var6, var7.copyWithCount(var9));
                  this.N.computeIfAbsent(var1, var0 -> new ArrayDeque<>()).addFirst(new FakeSpawnerController.Type_l(var6, var7.copyWithCount(var9)));
                  VisualInventory.vAsVoid();
                  ClientLog.XAsVoid(
                     "FakeSpawner",
                     "Reservation created pile={} physical={} item={} reserved={} real={}",
                     var1,
                     var6,
                     Registries.ITEM.getId(var7.getItem()),
                     var9,
                     var7.getCount()
                  );
                  return true;
               } else {
                  return false;
               }
            } else {
               return false;
            }
         }
      } else {
         return false;
      }
   }

   private void ZAsVoid(BlockPos var1, Direction var2) {
      MinecraftClient var3 = MinecraftClient.getInstance();
      if (var3 != null && var3.particleManager != null && var3.world != null && var1 != null) {
         double var4 = (double)var1.getX() + 0.5;
         double var6 = (double)var1.getY() + 0.55;
         double var8 = (double)var1.getZ() + 0.5;
         if (var2 != null) {
            var4 += (double)var2.getOffsetX() * 0.42;
            var6 += (double)var2.getOffsetY() * 0.42;
            var8 += (double)var2.getOffsetZ() * 0.42;
         }

         BlockStateParticleEffect var10 = new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.SPAWNER.getDefaultState());

         for (int var11 = 0; var11 < 1; var11++) {
            var3.particleManager
               .addParticle(
                  var10,
                  var4,
                  var6,
                  var8,
                  (var3.world.getRandom().nextDouble() - 0.5) * 0.14,
                  var3.world.getRandom().nextDouble() * 0.12 + 0.02,
                  (var3.world.getRandom().nextDouble() - 0.5) * 0.14
               );
         }
      }
   }

   private void HAsVoid(MinecraftClient var1, BlockPos var2, Direction var3, double var4) {
      if (var1 != null && var1.player != null && var1.worldRenderer != null && var2 != null) {
         int var6 = MathHelper.clamp((int)(var4 * 10.0), 0, 9);
         var1.world.setBlockBreakingInfo(var1.player.getId(), var2, var6);
         if (var4 <= 0.0) {
            return;
         }

         long var7 = var2.asLong();
         long var9 = (long)var1.player.age;
         long var11 = this.Q.getOrDefault(var7, 0L);
         if (var9 - var11 >= 4L) {
            this.Q.put(var7, var9);
            this.ZAsVoid(var2, var3);
            this.mAsVoid(var2, false);
         }
      }
   }

   private void RAsVoid(BlockPos var1) {
      MinecraftClient var2 = MinecraftClient.getInstance();
      if (var2 != null && var2.player != null && var2.worldRenderer != null && var1 != null) {
         var2.world.setBlockBreakingInfo(var2.player.getId(), var1, -1);
      }
   }

   private void FAsVoid(World var1, BlockPos var2) {
      MinecraftClient var3 = MinecraftClient.getInstance();
      if (var3 != null && var3.particleManager != null && var2 != null) {
         double var4 = (double)var2.getX() + 0.5 + (var1.getRandom().nextDouble() - 0.5) * 0.45;
         double var6 = (double)var2.getY() + 0.35 + var1.getRandom().nextDouble() * 0.45;
         double var8 = (double)var2.getZ() + 0.5 + (var1.getRandom().nextDouble() - 0.5) * 0.45;
         var3.particleManager.addParticle(ParticleTypes.FLAME, var4, var6, var8, 0.0, 0.006, 0.0);
         if (var1.getRandom().nextInt(4) == 0) {
            var3.particleManager.addParticle(ParticleTypes.SMOKE, var4, var6 + 0.02, var8, 0.0, 0.004, 0.0);
         }
      }
   }

   private void eAsVoid(String var1) {
      MinecraftClient var2 = MinecraftClient.getInstance();
      if (var2 != null && var2.player != null && var1 != null && !var1.isBlank()) {
         ChatMessages.RAsVoid(iAsMutableText(var1, 16711680), false);
         ChatMessages.RAsVoid(iAsMutableText(var1, 16711680), true);
      }
   }

   private void VAsVoid(String var1, Object... var2) {
      if (Boolean.getBoolean("larp.debug")) {
         ClientLog.XAsVoid("FakeSpawner", var1, var2);
      }
   }

   private void pAsVoid(String var1, long var2, String var4, Object... var5) {
      if (Boolean.getBoolean("larp.debug")) {
         long var6 = System.currentTimeMillis();
         if (var6 >= this.h) {
            this.h = var6 + var2;
            this.VAsVoid(var4, var5);
         }
      }
   }

   private static ItemStack TAsItemStack(ItemStack var0, Text var1, List<Text> var2) {
      var0.set(DataComponentTypes.CUSTOM_NAME, var1);
      var0.set(DataComponentTypes.RARITY, Rarity.COMMON);
      if (var2 != null && !var2.isEmpty()) {
         var0.set(DataComponentTypes.LORE, new LoreComponent(var2));
      }

      return var0;
   }

   private static Text UAsText(String var0, Formatting var1) {
      return RAsMutableText(tAsString(var0), var1);
   }

   private static MutableText RAsMutableText(String var0, Formatting var1) {
      return Text.literal(var0).setStyle(Style.EMPTY.withFormatting(var1).withItalic(false));
   }

   private static Text mAsText(String var0, int var1) {
      return iAsMutableText(tAsString(var0), var1);
   }

   private static MutableText iAsMutableText(String var0, int var1) {
      return Text.literal(var0).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(var1)).withItalic(false));
   }

   private static String tAsString(String var0) {
      StringBuilder var1 = new StringBuilder();

      for (int var2 = 0; var2 < var0.length(); var2++) {
         char var3 = var0.charAt(var2);
         if (var3 >= 'A' && var3 <= 'Z') {
            var1.append("ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀsᴛᴜᴠᴡxʏᴢ".charAt(var3 - 'A'));
         } else if (var3 >= 'a' && var3 <= 'z') {
            var1.append("ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀsᴛᴜᴠᴡxʏᴢ".charAt(var3 - 'a'));
         } else {
            var1.append(var3);
         }
      }

      return var1.toString();
   }

   private static String SAsString(double var0, int var2) {
      String var3 = String.format(Locale.US, "%." + var2 + "f", var0);

      while (var3.contains(".") && var3.endsWith("0")) {
         var3 = var3.substring(0, var3.length() - 1);
      }

      if (var3.endsWith(".")) {
         var3 = var3.substring(0, var3.length() - 1);
      }

      return var3;
   }

   private static Text iAsText(String var0) {
      if (var0 != null && !var0.isEmpty()) {
         MutableText var1 = Text.empty();
         Style var2 = Style.EMPTY.withItalic(false);
         StringBuilder var3 = new StringBuilder();

         for (int var4 = 0; var4 < var0.length(); var4++) {
            char var5 = var0.charAt(var4);
            if ((var5 == 167 || var5 == '&') && var4 + 1 < var0.length()) {
               if (var5 == '&' && var4 + 7 < var0.length() && var0.charAt(var4 + 1) == '#') {
                  Integer var6 = bAsInteger(var0.substring(var4 + 2, var4 + 8));
                  if (var6 != null) {
                     gAsVoid(var1, var3, var2);
                     var2 = var2.withColor(TextColor.fromRgb(var6)).withItalic(false);
                     var4 += 7;
                     continue;
                  }
               }

               Formatting var7 = Formatting.byCode(var0.charAt(var4 + 1));
               if (var7 != null) {
                  gAsVoid(var1, var3, var2);
                  if (var7 == Formatting.RESET) {
                     var2 = Style.EMPTY.withItalic(false);
                  } else if ("0123456789abcdef".indexOf(Character.toLowerCase(var0.charAt(var4 + 1))) >= 0) {
                     var2 = Style.EMPTY.withFormatting(var7).withItalic(false);
                  } else {
                     var2 = var2.withFormatting(var7).withItalic(false);
                  }

                  var4++;
                  continue;
               }
            }

            var3.append(var5);
         }

         gAsVoid(var1, var3, var2);
         return var1;
      } else {
         return Text.empty();
      }
   }

   private static void gAsVoid(MutableText var0, StringBuilder var1, Style var2) {
      if (var1.length() != 0) {
         var0.append(Text.literal(var1.toString()).setStyle(var2));
         var1.setLength(0);
      }
   }

   private static Integer bAsInteger(String var0) {
      try {
         return Integer.parseInt(var0, 16);
      } catch (NumberFormatException var2) {
         return null;
      }
   }

   private String CAsString(long var1) {
      long var3 = Math.max(1L, var1);
      double var5 = (double)var3 * 1000.0;
      if (var5 >= 1.0E9) {
         return SAsString(var5 / 1.0E9, 1) + "B";
      } else if (var5 >= 1000000.0) {
         return SAsString(var5 / 1000000.0, 1) + "M";
      } else {
         return var5 >= 1000.0 ? SAsString(var5 / 1000.0, 1) + "K" : SAsString(var5, 0);
      }
   }

   private String GAsString(long var1) {
      long var3 = Math.max(1L, var1);
      double var5 = (double)var3 * 1000.0 + 75.0;
      if (var5 >= 1.0E9) {
         return SAsString(var5 / 1.0E9, 1) + "B";
      } else if (var5 >= 1000000.0) {
         return SAsString(var5 / 1000000.0, 1) + "M";
      } else {
         return var5 >= 1000.0 ? SAsString(var5 / 1000.0, 1) + "K" : SAsString(var5, 0);
      }
   }

   private boolean MAsBoolean(ItemStack var1) {
      boolean var10000;
      if (var1 != null && !var1.isEmpty()) {
         if (var1.getItem() instanceof BlockItem var2 && var2.getBlock() instanceof ShulkerBoxBlock) {
            return true;
         }

         var10000 = false;
      } else {
         var10000 = false;
      }

      return var10000;
   }

   private long mAsLong(ItemStack var1) {
      if (var1 != null && !var1.isEmpty()) {
         long var2 = 0L;
         ContainerComponent var4 = (ContainerComponent)var1.get(DataComponentTypes.CONTAINER);
         if (var4 != null) {
            for (ItemStack var6 : var4.iterateNonEmpty()) {
               if (var6 != null && !var6.isEmpty() && this.HAsBoolean(var6)) {
                  var2 += (long)var6.getCount();
               }
            }
         }

         return var2;
      } else {
         return 0L;
      }
   }

   private int BAsInt(NbtCompound var1) {
      if (var1 == null) {
         return 0;
      } else if (var1.contains("count")) {
         return Math.max(0, var1.getInt("count", 0));
      } else {
         return var1.contains("Count") ? Math.max(0, var1.getInt("Count", 0)) : 0;
      }
   }

   private boolean lAsBoolean(int var1) {
      MinecraftClient var2 = MinecraftClient.getInstance();
      return var2 != null && var2.getWindow() != null ? InputUtil.isKeyPressed(var2.getWindow(), var1) : false;
   }

   private static record Type_b(int stackCount, FakeSpawnerController.Type_l hiddenStack, long createdAtMs) {
   }

   private static record Type_c(ItemStack stack, int expected, int expires) {
   }

   public static record Type_d(SoundEvent event, float volume, float pitch) {
   }

   private static record Type_l(int preferredSlot, ItemStack stack) {
   }

   public static final class Type_m {
      public final int l;
      public final int v;
      public final double B;
      public final double r;

      public Type_m(int var1, int var2, double var3, double var5) {
         this.l = Math.max(0, var1);
         this.v = Math.max(0, var2);
         this.B = Math.max(0.0, var3);
         this.r = Math.max(0.0, var5);
      }
   }

   private static record Type_u(int slot, String item, int amount, int reserved) {
   }

   private static record Type_v(
      long pos, SpawnerEntityCatalog kind, int count, long placedAt, List<FakeSpawnerController.Type_u> backing, Map<String, Integer> purchased
   ) {
      JsonObject toJson() {
         JsonObject var1 = new JsonObject();
         var1.addProperty("pos", this.pos);
         var1.addProperty("kind", this.kind.name());
         var1.addProperty("count", this.count);
         var1.addProperty("placedAt", this.placedAt);
         JsonArray var2 = new JsonArray();

         for (FakeSpawnerController.Type_u var4 : this.backing) {
            JsonObject var5 = new JsonObject();
            var5.addProperty("slot", var4.slot);
            var5.addProperty("item", var4.item);
            var5.addProperty("amount", var4.amount);
            var5.addProperty("reserved", var4.reserved);
            var2.add(var5);
         }

         var1.add("backing", var2);
         JsonObject var6 = new JsonObject();
         this.purchased.forEach(var6::addProperty);
         var1.add("purchased", var6);
         return var1;
      }
   }

   private static record Type_x(BlockPos pos, long untilMs) {
   }
}
