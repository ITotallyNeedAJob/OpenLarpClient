package com.larpclient.module;

import com.larpclient.AuctionItemSelectionPanel;
import com.larpclient.AuctionSaleConfirmation;
import com.larpclient.ClientLog;
import com.larpclient.LarpClient;
import com.larpclient.SelectionSettings;
import com.larpclient.oe;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public final class AutoSell extends Module {
   private static final int m = 100;
   private static AutoSell k;
   private final TextSetting menuTitle = this.addSetting(new TextSetting("Menu Title", "Exact sell-menu title, ignoring case and surrounding spaces", "Sell"));
   private final NumberSetting V = this.JAsNumberSetting("Min Cycle Delay", "Minimum ticks between inventory cycles", 1);
   private final NumberSetting x = this.JAsNumberSetting("Max Cycle Delay", "Maximum ticks between inventory cycles", 3);
   private final NumberSetting f = this.JAsNumberSetting("Min Click Delay", "Archived reference preference; transfers wait for server acknowledgment", 1);
   private final NumberSetting L = this.JAsNumberSetting("Max Click Delay", "Archived reference preference; transfers wait for server acknowledgment", 3);
   private final NumberSetting S = this.JAsNumberSetting("Menu Open Wait", "Ticks before the additional 20-tick server-response grace period", 3);
   private final BooleanSetting G = this.NAsBooleanSetting("Randomize Delays", "Random cycle range and source pause jitter", true);
   private final BooleanSetting a = this.NAsBooleanSetting("Show Messages", "Report menu opening, transfers, sell clicks and recovery", true);
   private final BooleanSetting N = this.NAsBooleanSetting("Show Stats", "Report attempted confirmations and acknowledged transfers when stopped", true);
   private final BooleanSetting R = this.NAsBooleanSetting("Background Mode", "Archived source preference", true);
   private final ModeSetting itemMode = this.addSetting(
      new ModeSetting(
         "Item Mode",
         "Choose selected items or explicitly allow all inventory items; exclusions always win",
         SelectionSettings.F.getFirst(),
         SelectionSettings.F.toArray(String[]::new)
      )
   );
   private final BooleanSetting j = this.NAsBooleanSetting("Protect Hotbar", "Keep items in the nine hotbar slots", true);
   private final BooleanSetting K = this.NAsBooleanSetting("Protect Named Items", "Keep items with a custom name", true);
   private final BooleanSetting s = this.NAsBooleanSetting("Protect Enchanted Items", "Keep enchanted items and enchanted books", true);
   private final BooleanSetting M = this.NAsBooleanSetting("Protect Filled Containers", "Keep containers and bundles that contain items", true);
   private final TextSetting C = this.oAsE("Selection Version", "1");
   private final TextSetting P = this.oAsE("Selection Error", "");
   private final List<TextSetting> l = new ArrayList<>();
   private final List<TextSetting> p = new ArrayList<>();
   private final oe h = new oe();
   private Object w;
   private Object z;
   private Object n;
   private GenericContainerScreenHandler U;
   private final Map<Integer, ItemStack> v = new LinkedHashMap<>();
   private Map<AuctionSaleConfirmation.Type_e, Integer> o = Map.of();
   private Map<AuctionSaleConfirmation.Type_e, Integer> A = Map.of();
   private AuctionSaleConfirmation.Type_g O;
   private ItemStack r = ItemStack.EMPTY;
   private int b;
   private int F = -1;
   private int Q;
   private int q;
   private int Z;
   private int X;
   private int u;
   private int I;
   private long E;
   private long H;
   private long g;
   private boolean d;
   private boolean B;
   private boolean T;
   private SelectionSettings.Type_r e;

   public AutoSell() {
      super("Auto Sell", "Sell chosen inventory items through a validated server sell menu", Category.PLAYER, -1);
      k = this;
      this.f.visibleWhen(() -> false);
      this.L.visibleWhen(() -> false);
      this.R.visibleWhen(() -> false);

      for (int var1 = 0; var1 < 32; var1++) {
         this.l.add(this.oAsE(SelectionSettings.CAsString(var1), ""));
         this.p.add(this.oAsE(SelectionSettings.RAsString(var1), ""));
      }

      for (Setting var2 : this.getSettings()) {
         if (SelectionSettings.eAsSet().contains(var2.getName())) {
            this.lAsVoid(var2);
         }
      }
   }

   private void lAsVoid(Setting var1) {
      var1.onChanged(var1x -> {
         this.e = null;
         this.H++;
      });
   }

   private TextSetting oAsE(String var1, String var2) {
      TextSetting var3 = this.addSetting(new TextSetting(var1, "Auto Sell item selection data", var2));
      var3.visibleWhen(() -> false);
      return var3;
   }

   private NumberSetting JAsNumberSetting(String var1, String var2, int var3) {
      return this.addSetting(new NumberSetting(var1, var2, (double)var3, 0.0, 20.0, 1.0));
   }

   private BooleanSetting NAsBooleanSetting(String var1, String var2, boolean var3) {
      return this.addSetting(new BooleanSetting(var1, var2, var3));
   }

   public List<Setting<?>> QAsList() {
      return List.of(this.menuTitle, this.S);
   }

   public List<Setting<?>> nAsList() {
      return List.of(this.V, this.x, this.f, this.L, this.G);
   }

   public List<Setting<?>> OAsList() {
      return List.of(this.a, this.N, this.R);
   }

   public List<Setting<?>> TAsList() {
      return List.of(this.itemMode, this.j, this.K, this.s, this.M);
   }

   public SelectionSettings.Type_r iAsP7_Type_r() {
      if (this.e == null) {
         LinkedHashMap var1 = new LinkedHashMap();

         for (Setting var3 : this.getSettings()) {
            if (SelectionSettings.eAsSet().contains(var3.getName())) {
               var1.put(var3.getName(), var3.getValue());
            }
         }

         this.e = SelectionSettings.gAsP7_Type_r(var1);
      }

      return this.e;
   }

   public String qAsString() {
      SelectionSettings.Type_r var1 = this.iAsP7_Type_r();
      if (!var1.error().isEmpty()) {
         return var1.error();
      } else {
         long var2 = var1.selected().stream().filter(var0 -> !Registries.ITEM.containsId(Identifier.of(var0))).count();
         return var1.mode()
            + ": "
            + var1.selected().size()
            + " selected, "
            + var1.excluded().size()
            + " excluded"
            + (var2 == 0L ? "" : ", " + var2 + " unavailable")
            + (var1.needsSelection() ? ". Choose items to begin." : "");
      }
   }

   public Set<String> zAsSet(boolean var1) {
      return var1 ? this.iAsP7_Type_r().excluded() : this.iAsP7_Type_r().selected();
   }

   public void KAsVoid(boolean var1, Collection<String> var2) {
      List var3 = SelectionSettings.cAsList(var2);
      List var4 = var1 ? this.p : this.l;
      if (!this.iAsP7_Type_r().error().isEmpty()) {
         this.itemMode.setValue(SelectionSettings.F.getFirst());

         for (TextSetting var6 : this.l) {
            var6.dAsVoid("");
         }

         for (TextSetting var9 : this.p) {
            var9.dAsVoid("");
         }

         this.j.setValue(Boolean.valueOf(true));
         this.K.setValue(Boolean.valueOf(true));
         this.s.setValue(Boolean.valueOf(true));
         this.M.setValue(Boolean.valueOf(true));
      }

      for (int var8 = 0; var8 < var4.size(); var8++) {
         ((TextSetting)var4.get(var8)).dAsVoid(var8 < var3.size() ? (String)var3.get(var8) : "");
      }

      this.C.dAsVoid("1");
      this.P.dAsVoid("");
   }

   public void uAsVoid(Screen var1, boolean var2) {
      getMc().setScreen(new AuctionItemSelectionPanel(var1, this, var2));
   }

   public void BAsVoid(boolean var1) {
      if (this.isInGame() && !mc.player.getMainHandStack().isEmpty()) {
         LinkedHashSet var2 = new LinkedHashSet<>(this.zAsSet(var1));
         var2.add(JAsString(mc.player.getMainHandStack()));
         this.KAsVoid(var1, var2);
         this.TAsVoid();
      } else {
         throw new IllegalArgumentException("Hold an item first.");
      }
   }

   public void mAsVoid(boolean var1) {
      this.KAsVoid(var1, Set.of());
      this.TAsVoid();
   }

   public void TAsVoid() {
      LarpClient var1 = LarpClient.CAsLarpClient();
      if (var1 != null && var1.fAsBo() != null) {
         var1.fAsBo().QAsVoid();
      }
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(com.larpclient.module.k.SERVER_STATE_MODIFYING);
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Idle";
      } else {
         SelectionSettings.Type_r var1 = this.iAsP7_Type_r();
         if (!var1.error().isEmpty()) {
            return "Repair item selection";
         } else if (var1.needsSelection()) {
            return "Choose items to begin";
         } else if (this.O != null) {
            return "Waiting for item transfer";
         } else if (this.B) {
            return "Waiting for sale response";
         } else {
            return switch (this.h.NAsOe_Type_r()) {
               case WAIT_MENU_OPEN -> "Waiting for Sell menu";
               case FILLING_ITEMS, POST_FILL_WAIT -> "Filling Sell menu";
               case CLICK_SELL, POST_SELL_WAIT -> "Confirming selected items";
               default -> "Waiting for eligible items";
            };
         }
      }
   }

   @Override
   public void onEnable() {
      this.BAsVoid();
      if (this.isInGame() && mc.player.networkHandler != null) {
         this.w = mc.player;
         this.z = mc.world;
         this.n = mc.player.networkHandler;
         this.E = System.nanoTime();
         this.g = this.H;
         this.kAsVoid(this.iAsP7_Type_r().needsSelection() ? "Choose items to begin; no /sell command will be sent." : "Auto Sell activated.");
         ClientLog.XAsVoid(
            "AutoSell", "Activated with policy={}, selected={}, excluded={}", this.itemMode.getValue(), this.zAsSet(false).size(), this.zAsSet(true).size()
         );
      }
   }

   @Override
   public void onDisable() {
      if (this.E != 0L && this.N.getValue() && this.isInGame()) {
         this.gAsVoid(
            "Auto Sell stopped: " + this.Z + " confirmation attempts, " + this.X + " empty-menu acknowledgments, " + this.q + " acknowledged stack transfers."
         );
      }

      if (this.o.isEmpty() && this.O == null && !this.B && (this.U == null || this.rAsMap(this.U).isEmpty())) {
         this.nAsVoid();
      }

      this.BAsVoid();
   }

   private void BAsVoid() {
      this.h.CAsVoid();
      this.w = this.z = this.n = null;
      this.U = null;
      this.v.clear();
      this.o = this.A = Map.of();
      this.O = null;
      this.r = ItemStack.EMPTY;
      this.F = -1;
      this.Q = this.q = this.Z = this.X = this.u = this.I = 0;
      this.d = this.B = this.T = false;
      this.E = 0L;
   }

   @Override
   public void onTick() {
      if (this.isInGame() && mc.player.networkHandler != null) {
         if (mc.player != this.w || mc.world != this.z || mc.player.networkHandler != this.n) {
            this.BAsVoid();
            this.onEnable();
         } else if (!mc.player.isAlive()) {
            this.RAsVoid("Player is no longer alive; sale cancelled.");
         } else {
            SelectionSettings.Type_r var1 = this.iAsP7_Type_r();
            if (!var1.error().isEmpty()) {
               this.RAsVoid(var1.error());
            } else {
               if (this.H != this.g) {
                  if (this.U != null || this.d) {
                     this.RAsVoid("Item selection changed during a sale. Review the menu before restarting.");
                     return;
                  }

                  this.g = this.H;
               }

               GenericContainerScreenHandler var2 = this.JAsGenericContainerScreenHandler();
               if (this.U != null && var2 != this.U) {
                  if (this.B && this.T && var2 == null && mc.player.currentScreenHandler == mc.player.playerScreenHandler) {
                     AuctionSaleConfirmation.Type_j var4 = AuctionSaleConfirmation.dAsMp_Type_j(
                        true, ++this.I, mc.player.currentScreenHandler.getCursorStack().isEmpty(), this.A, this.PAsMap()
                     );
                     if (var4 != AuctionSaleConfirmation.Type_j.WAIT) {
                        if (var4 == AuctionSaleConfirmation.Type_j.CONTINUE) {
                           this.o = Map.of();
                           this.B = this.T = false;
                           this.I = 0;
                           this.U = null;
                           this.v.clear();
                           this.F = -1;
                           this.kAsVoid("Server closed the menu after confirmation. Checking remaining eligible items.");
                           ClientLog.XAsVoid("AutoSell", "Native close settled after confirmation; sale revenue/outcome not measured");
                           this.OAsVoid(oe.Type_r.IDLE, this.XAsInt());
                        } else {
                           this.RAsVoid("Sell menu closed with returned or changed inventory. Review the result before restarting.");
                        }
                     }
                  } else {
                     this.RAsVoid(
                        this.B
                           ? "Sell menu closed or changed after confirmation; check the sale result before restarting."
                           : "Sell menu closed or changed; pending sale cancelled."
                     );
                  }
               } else if (!mc.player.currentScreenHandler.getCursorStack().isEmpty()) {
                  if (this.O != null || this.B) {
                     this.h.mAsVoid();
                     if (this.h.eAsInt() >= 100) {
                        this.RAsVoid("Cursor did not clear after the server response; no further click was sent.");
                     }
                  } else if (this.U != null || this.d) {
                     this.RAsVoid("Cursor holds an item; sale cancelled without another click.");
                  }
               } else if (mc.currentScreen == null || var2 != null || this.d) {
                  this.h.mAsVoid();
                  if (this.U != null && !this.B && !this.IAsBoolean(var2)) {
                     this.RAsVoid("Sell menu controls changed; review the menu before restarting.");
                  } else {
                     switch (this.h.NAsOe_Type_r()) {
                        case WAIT_MENU_OPEN:
                           if (var2 != null) {
                              this.d = false;
                              this.u = 0;
                              if (this.YAsBoolean(var2)) {
                                 this.OAsVoid(oe.Type_r.FILLING_ITEMS, 1);
                              }
                           } else if (this.h.wAsBoolean()) {
                              this.d = false;
                              if (++this.u >= 3) {
                                 this.RAsVoid("Sell menu did not open after three requests.");
                                 return;
                              }

                              this.OAsVoid(oe.Type_r.IDLE, this.BAsInt(3));
                           }
                           break;
                        case FILLING_ITEMS:
                           this.RAsVoid(var2);
                           break;
                        case POST_FILL_WAIT:
                           if (this.h.YAsBoolean()) {
                              this.OAsVoid(oe.Type_r.CLICK_SELL, 0);
                           }
                           break;
                        case CLICK_SELL:
                           this.eAsVoid(var2);
                           break;
                        case POST_SELL_WAIT:
                           if (!this.h.YAsBoolean()) {
                              return;
                           }

                           Map var3 = this.rAsMap(var2);
                           if (AuctionSaleConfirmation.WAsBoolean(this.Q, var2.getRevision(), var3, this.A, this.PAsMap())) {
                              if (!this.IAsBoolean(var2)) {
                                 this.RAsVoid("Sell response changed the menu controls; inspect this server layout before restarting.");
                                 return;
                              }

                              this.B = false;
                              this.X++;
                              this.o = Map.of();
                              this.kAsVoid("Server cleared the selected sell items.");
                              if (this.UAsBoolean()) {
                                 this.OAsVoid(oe.Type_r.FILLING_ITEMS, this.BAsInt(1));
                              } else {
                                 this.nAsVoid();
                                 this.OAsVoid(oe.Type_r.IDLE, this.XAsInt());
                              }
                           } else if (this.h.eAsInt() >= 100) {
                              this.RAsVoid("Sale was rejected, returned items, or did not finish. No duplicate confirmation was sent.");
                           }
                           break;
                        case IDLE:
                           if (this.h.YAsBoolean() && !var1.needsSelection()) {
                              if (var2 != null && this.UAsBoolean()) {
                                 if (this.YAsBoolean(var2)) {
                                    this.OAsVoid(oe.Type_r.FILLING_ITEMS, 1);
                                 }
                              } else if (var2 == null && this.UAsBoolean()) {
                                 this.OAsVoid(oe.Type_r.SEND_COMMAND, 0);
                              } else {
                                 this.OAsVoid(oe.Type_r.IDLE, this.XAsInt());
                              }
                              break;
                           }

                           return;
                        case SEND_COMMAND:
                           if (!this.UAsBoolean()) {
                              this.OAsVoid(oe.Type_r.IDLE, this.XAsInt());
                              return;
                           }

                           if (mc.currentScreen == null && mc.player.currentScreenHandler == mc.player.playerScreenHandler) {
                              this.d = true;
                              mc.player.networkHandler.sendChatCommand("sell");
                              this.kAsVoid("Sent /sell command.");
                              this.OAsVoid(oe.Type_r.WAIT_MENU_OPEN, this.BAsInt(this.S.getValue().intValue()));
                              break;
                           }

                           return;
                        default:
                           this.OAsVoid(oe.Type_r.IDLE, this.XAsInt());
                     }
                  }
               }
            }
         }
      }
   }

   private boolean YAsBoolean(GenericContainerScreenHandler var1) {
      this.U = var1;
      this.v.clear();
      this.F = -1;
      int var2 = var1.getRows() * 9;

      for (int var3 = var2 - 9; var3 < var2; var3++) {
         ItemStack var4 = var1.getSlot(var3).getStack();
         if (!var4.isEmpty() && var4.contains(DataComponentTypes.CUSTOM_NAME) && var4.getCount() == 1) {
            if (AuctionSaleConfirmation.RAsBoolean(JAsString(var4), var4.getName().getString(), var3, var2)) {
               if (this.F != -1) {
                  this.RAsVoid("Multiple sell controls found. This menu layout needs review.");
                  return false;
               }

               this.F = var3;
               this.v.put(var3, var4.copy());
            } else if (AuctionSaleConfirmation.ZAsBoolean(JAsString(var4), var4.getName().getString(), var3, var2)) {
               this.v.put(var3, var4.copy());
            }
         }
      }

      if (this.F < 0) {
         this.RAsVoid("Unsupported Sell menu. Expected one named Sell or Confirm pane in its bottom row.");
         return false;
      } else if (!this.rAsMap(var1).isEmpty()) {
         this.RAsVoid("Sell menu already contains items. Empty it manually before starting Auto Sell.");
         return false;
      } else {
         this.o = Map.of();
         this.g = this.H;
         ClientLog.XAsVoid("AutoSell", "Validated {}-row menu, confirm slot={}, controls={}", var1.getRows(), this.F + 1, this.v.size());
         return true;
      }
   }

   private void RAsVoid(GenericContainerScreenHandler var1) {
      if (var1 != null && mc.interactionManager != null) {
         if (this.O != null) {
            ItemStack var5 = var1.getSlot(this.b).getStack();
            int var6 = var5.isEmpty() ? 0 : (ItemStack.areItemsAndComponentsEqual(var5, this.r) ? var5.getCount() : -1);
            int var4 = this.O.accepted(var1.getRevision(), var6, this.rAsMap(var1), this.PAsMap());
            if (var4 > 0) {
               this.o = AuctionSaleConfirmation.OAsMap(this.o, this.O.key(), var4);
               this.q++;
               this.O = null;
               this.r = ItemStack.EMPTY;
               if (!this.HAsBoolean(var1)) {
                  return;
               }

               this.OAsVoid(oe.Type_r.FILLING_ITEMS, this.BAsInt(1));
            } else if (this.h.eAsInt() >= 100) {
               this.RAsVoid("Item transfer was rejected, the menu is full, or the server did not respond. No repeat click was sent.");
            }
         } else if (this.h.YAsBoolean() && this.HAsBoolean(var1)) {
            for (int var2 = var1.getRows() * 9; var2 < var1.slots.size(); var2++) {
               Slot var3 = var1.getSlot(var2);
               if (var3.inventory == mc.player.getInventory() && this.fAsBoolean(var3.getStack(), var3.getIndex()) && this.pAsBoolean(var1, var3.getStack())) {
                  if (this.JAsGenericContainerScreenHandler() == var1
                     && mc.player.currentScreenHandler == var1
                     && var1.getCursorStack().isEmpty()
                     && this.H == this.g
                     && this.IAsBoolean(var1)) {
                     this.b = var2;
                     this.r = var3.getStack().copy();
                     this.O = new AuctionSaleConfirmation.Type_g(var1.getRevision(), QAsMp_Type_e(this.r), this.r.getCount(), this.rAsMap(var1), this.PAsMap());
                     mc.interactionManager.clickSlot(var1.syncId, var2, 0, SlotActionType.QUICK_MOVE, mc.player);
                     this.OAsVoid(oe.Type_r.FILLING_ITEMS, 1);
                     return;
                  }

                  return;
               }
            }

            if (this.o.isEmpty()) {
               this.nAsVoid();
               this.OAsVoid(oe.Type_r.IDLE, this.XAsInt());
            } else {
               this.OAsVoid(oe.Type_r.POST_FILL_WAIT, this.BAsInt(1));
            }
         }
      }
   }

   private boolean HAsBoolean(GenericContainerScreenHandler var1) {
      if (!this.rAsMap(var1).equals(this.o)) {
         this.RAsVoid("Unexplained or returned items in the sell menu. Review them manually; no confirmation was sent.");
         return false;
      } else {
         for (int var2 = 0; var2 < var1.getRows() * 9; var2++) {
            if (!this.v.containsKey(var2)) {
               ItemStack var3 = var1.getSlot(var2).getStack();
               if (!var3.isEmpty() && !this.fAsBoolean(var3, -1)) {
                  this.RAsVoid("Protected or excluded item in sell slot " + (var2 + 1) + ". No confirmation was sent.");
                  return false;
               }
            }
         }

         return true;
      }
   }

   private boolean pAsBoolean(GenericContainerScreenHandler var1, ItemStack var2) {
      for (int var3 = 0; var3 < var1.getRows() * 9; var3++) {
         if (!this.v.containsKey(var3)) {
            Slot var4 = var1.getSlot(var3);
            ItemStack var5 = var4.getStack();
            if (AuctionSaleConfirmation.aAsBoolean(
               var5.isEmpty(), ItemStack.areItemsAndComponentsEqual(var2, var5), var4.canInsert(var2), var5.getCount(), var4.getMaxItemCount(var2)
            )) {
               return true;
            }
         }
      }

      return false;
   }

   private void eAsVoid(GenericContainerScreenHandler var1) {
      if (var1 != null
         && mc.interactionManager != null
         && !this.B
         && !this.o.isEmpty()
         && this.HAsBoolean(var1)
         && this.IAsBoolean(var1)
         && this.JAsGenericContainerScreenHandler() == var1
         && var1.getCursorStack().isEmpty()
         && mc.player.currentScreenHandler == var1
         && this.H == this.g) {
         this.Q = var1.getRevision();
         this.A = this.PAsMap();
         this.B = true;
         mc.interactionManager.clickSlot(var1.syncId, this.F, 0, SlotActionType.PICKUP, mc.player);
         this.Z++;
         this.kAsVoid("Sent one confirmation for the selected items.");
         this.OAsVoid(oe.Type_r.POST_SELL_WAIT, this.BAsInt(1));
      }
   }

   private boolean IAsBoolean(GenericContainerScreenHandler var1) {
      if (var1 != null && var1 == this.U && this.F >= 0) {
         for (Entry var3 : this.v.entrySet()) {
            if (!ItemStack.areEqual((ItemStack)var3.getValue(), var1.getSlot((Integer)var3.getKey()).getStack())) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private boolean UAsBoolean() {
      for (int var1 = 0; var1 < 36; var1++) {
         if (this.fAsBoolean(mc.player.getInventory().getStack(var1), var1)) {
            return true;
         }
      }

      return false;
   }

   private boolean fAsBoolean(ItemStack var1, int var2) {
      return this.iAsP7_Type_r().eligible(aAsP7_Type_o(var1, var2));
   }

   public static SelectionSettings.Type_o aAsP7_Type_o(ItemStack var0, int var1) {
      ContainerComponent var2 = (ContainerComponent)var0.get(DataComponentTypes.CONTAINER);
      BundleContentsComponent var3 = (BundleContentsComponent)var0.get(DataComponentTypes.BUNDLE_CONTENTS);
      ItemEnchantmentsComponent var4 = (ItemEnchantmentsComponent)var0.get(DataComponentTypes.STORED_ENCHANTMENTS);
      int var5 = var2 != null && var2.iterateNonEmpty().iterator().hasNext() || var3 != null && !var3.isEmpty() ? 1 : 0;
      return new SelectionSettings.Type_o(
         JAsString(var0),
         var0.getCount(),
         var1,
         var0.contains(DataComponentTypes.CUSTOM_NAME),
         var0.hasEnchantments() || var4 != null && !var4.isEmpty(),
         var5 != 0
      );
   }

   private Map<AuctionSaleConfirmation.Type_e, Integer> rAsMap(GenericContainerScreenHandler var1) {
      HashMap var2 = new HashMap();

      for (int var3 = 0; var3 < var1.getRows() * 9; var3++) {
         if (!this.v.containsKey(var3)) {
            tAsVoid(var2, var1.getSlot(var3).getStack());
         }
      }

      return Map.copyOf(var2);
   }

   private Map<AuctionSaleConfirmation.Type_e, Integer> PAsMap() {
      HashMap var1 = new HashMap();

      for (int var2 = 0; var2 < 36; var2++) {
         tAsVoid(var1, mc.player.getInventory().getStack(var2));
      }

      return Map.copyOf(var1);
   }

   private static void tAsVoid(Map<AuctionSaleConfirmation.Type_e, Integer> var0, ItemStack var1) {
      if (!var1.isEmpty()) {
         var0.merge(QAsMp_Type_e(var1), var1.getCount(), Integer::sum);
      }
   }

   private static AuctionSaleConfirmation.Type_e QAsMp_Type_e(ItemStack var0) {
      return new AuctionSaleConfirmation.Type_e(JAsString(var0), var0.copy().getComponents());
   }

   private static String JAsString(ItemStack var0) {
      return Registries.ITEM.getId(var0.getItem()).toString();
   }

   private GenericContainerScreenHandler JAsGenericContainerScreenHandler() {
      if (mc.currentScreen instanceof GenericContainerScreen var1
         && mc.player.currentScreenHandler instanceof GenericContainerScreenHandler var2
         && var1.getScreenHandler() == var2
         && oe.dAsBoolean(var1.getTitle().getString(), this.menuTitle.getValue())) {
         return var2;
      }

      return null;
   }

   public static boolean vAsBoolean(GenericContainerScreenHandler var0) {
      return k != null && k.isEnabled() ? k != null && k.isRuntimeActive() && k.isInGame() && k.JAsGenericContainerScreenHandler() == var0 : false;
   }

   public static void FAsVoid(int var0) {
      AutoSell var1 = k;
      if (var1 != null && var1.isRuntimeActive() && var1.U != null && var1.B && var1.U.syncId == var0) {
         var1.T = true;
         var1.I = 0;
      }
   }

   private void OAsVoid(oe.Type_r var1, int var2) {
      this.h.QAsVoid(var1, var2);
   }

   private int BAsInt(int var1) {
      return oe.SAsInt(var1, this.G.getValue(), ThreadLocalRandom.current());
   }

   private int XAsInt() {
      return oe.lAsInt(this.V.getValue().intValue(), this.x.getValue().intValue(), this.G.getValue(), ThreadLocalRandom.current());
   }

   private void nAsVoid() {
      if (this.U != null && this.isInGame() && mc.player == this.w && mc.world == this.z && mc.player.currentScreenHandler == this.U) {
         mc.player.closeHandledScreen();
      }

      this.U = null;
      this.v.clear();
      this.F = -1;
      this.d = this.B = false;
   }

   private void RAsVoid(String var1) {
      ClientLog.oAsVoid("AutoSell", "{} Stopped; enable again to retry.", var1);
      this.gAsVoid(var1);
      this.disable();
   }

   private void kAsVoid(String var1) {
      if (this.a.getValue()) {
         this.gAsVoid(var1);
      }
   }

   private void gAsVoid(String var1) {
      if (this.isInGame()) {
         mc.getMessageHandler().onGameMessage(Text.literal("[Auto Sell] " + var1), false);
      }
   }
}
