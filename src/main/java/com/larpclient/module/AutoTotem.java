package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.ClickState;
import com.larpclient.ParticleEmitter;
import com.larpclient.ParticleKind;
import com.larpclient.mixin.ClientPlayerEntityAccessor;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public final class AutoTotem extends Module {
   private static AutoTotem J;
   private Object m;
   private Object G;
   private long e = Long.MIN_VALUE;
   private final ModeSetting mode = this.addSetting(new ModeSetting("Mode", "When to protect your offhand", "Health", "Health", "Always"));
   private final NumberSetting health = this.addSetting(
      new NumberSetting("Health", "Equip at or below this health; an empty offhand is always protected", 10.0, 1.0, 20.0, 0.5)
   );
   private final BooleanSetting includeAbsorption = this.addSetting(
      new BooleanSetting("Include Absorption", "Count absorption hearts when comparing health", false)
   );
   private final ModeSetting swapMode = this.addSetting(new ModeSetting("Swap Mode", "Route from inventory to offhand", "Direct", "Direct", "Hotbar"));
   private final ModeSetting swapStyle = this.addSetting(
      new ModeSetting(
         "Swap Style", "Legit stops and clicks like a player in the inventory; Instant clicks at once and gets flagged", "Legit", "Legit", "Instant"
      )
   );
   private final NumberSetting clickDelay = this.addSetting(
      new NumberSetting("Click Delay", "Shortest pause in milliseconds before each legit inventory step", 100.0, 0.0, 1000.0, 10.0)
   );
   private final NumberSetting clickDelayMax = this.addSetting(
      new NumberSetting("Click Delay Max", "Longest pause in milliseconds before each legit inventory step", 200.0, 0.0, 1000.0, 10.0)
   );
   private final BooleanSetting offhandKey = this.addSetting(
      new BooleanSetting("Offhand Key", "Equip hotbar totems with the swap hands key, which works while moving", true)
   );
   private final BooleanSetting openInventory = this.addSetting(
      new BooleanSetting("Open Inventory", "Show the inventory during legit clicks, which also stops camera turns", false)
   );
   private final NumberSetting delay = this.addSetting(new NumberSetting("Delay", "Milliseconds before equipping", 0.0, 0.0, 1000.0, 10.0));
   private final NumberSetting randomDelay = this.addSetting(
      new NumberSetting("Random Delay", "Maximum additional milliseconds, sampled once per equip", 0.0, 0.0, 1000.0, 10.0)
   );
   private final BooleanSetting hotbarRefill = this.addSetting(
      new BooleanSetting("Hotbar Refill", "Keep spare totems in empty hotbar slots without replacing items", false)
   );
   private final NumberSetting hotbarReserves = this.addSetting(
      new NumberSetting("Hotbar Reserves", "Desired number of hotbar slots holding spare totems", 1.0, 1.0, 9.0, 1.0)
   );
   private final BooleanSetting inInventory = this.addSetting(
      new BooleanSetting("In Inventory", "Allow swaps in the survival inventory when the cursor is empty", true)
   );
   private final at.Type_s K = new at.Type_s();
   private final at.Type_a q = new at.Type_a();
   private final at.Type_r v = new at.Type_r();
   private ClientPlayerEntity k;
   private Object A;
   private Screen r;
   private boolean d;

   public AutoTotem() {
      super("AutoTotem", "Automatically equip offhand totems", Category.COMBAT, -1);
      J = this;
      this.health.visibleWhen(() -> this.mode.pAsBoolean("Health"));
      this.includeAbsorption.visibleWhen(() -> this.mode.pAsBoolean("Health"));
      this.hotbarReserves.visibleWhen(this.hotbarRefill::getValue);
      this.clickDelay.WAsNumberSetting(this.clickDelayMax).displayName("Click Delay");
      this.clickDelay.visibleWhen(() -> this.swapStyle.pAsBoolean("Legit"));
      this.clickDelayMax.visibleWhen(() -> this.swapStyle.pAsBoolean("Legit"));
      this.offhandKey.visibleWhen(() -> this.swapStyle.pAsBoolean("Legit"));
      this.openInventory.visibleWhen(() -> this.swapStyle.pAsBoolean("Legit"));
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(com.larpclient.module.k.SERVER_STATE_MODIFYING);
   }

   @Override
   public void onEnable() {
      this.K.MAsVoid();
      this.m = this.G = null;
      this.lAsVoid();
   }

   @Override
   public void onDisable() {
      this.OAsVoid("module disabled", true);
      this.K.MAsVoid();
      this.m = this.G = null;
   }

   public static boolean TAsBoolean(ClientPlayerEntity var0) {
      if (J != null && J.isEnabled()) {
         AutoTotem var1 = J;
         return var1 != null && var0 != null && var1.q.pAsBoolean() && var1.k == var0 && var1.isRuntimeActive();
      } else {
         return false;
      }
   }

   public static boolean NAsBoolean() {
      return sAsBoolean(false);
   }

   public static boolean IAsBoolean() {
      return sAsBoolean(true);
   }

   private static boolean sAsBoolean(boolean var0) {
      AutoTotem var1 = J;
      if (var1 != null && var1.isRuntimeActive()) {
         MinecraftClient var2 = getMc();
         ClientPlayerEntity var3 = var2.player;
         if (var3 == null || var1.k != var3 || !var1.v.iAsBoolean() && (!var1.q.pAsBoolean() || var0)) {
            if (var3 != null
               && var2.world != null
               && var3.isAlive()
               && !var3.isSpectator()
               && !p5.oAsBoolean()
               && var3.currentScreenHandler == var3.playerScreenHandler
               && var3.playerScreenHandler.getCursorStack().isEmpty()
               && (var2.currentScreen == null || var1.inInventory.getValue() && var2.currentScreen instanceof InventoryScreen)) {
               if (var0 && !var1.swapMode.pAsBoolean("Hotbar")) {
                  return false;
               } else if (var1.m == var3 && var1.G == var2.world && Util.getMeasuringTimeMs() - var1.e < 100L) {
                  return true;
               } else {
                  boolean var4 = at.oAsBoolean(
                     var3.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING),
                     var3.getOffHandStack().isEmpty(),
                     var1.mode.pAsBoolean("Always"),
                     (double)var3.getHealth(),
                     (double)var3.getAbsorptionAmount(),
                     var1.includeAbsorption.getValue(),
                     var1.health.getValue()
                  );
                  long var5 = 0L;
                  int var7 = 0;

                  for (int var8 = 0; var8 < 36; var8++) {
                     ItemStack var9 = var3.getInventory().getStack(var8);
                     if (var9.isOf(Items.TOTEM_OF_UNDYING)) {
                        var5 |= 1L << var8;
                     } else if (var8 < 9 && var9.isEmpty()) {
                        var7 |= 1 << var8;
                     }
                  }

                  return at.rAsAt_Type_e(
                        var5, var7, var4, var1.swapMode.pAsBoolean("Hotbar"), var1.hotbarRefill.getValue(), var1.hotbarReserves.getValue().intValue()
                     )
                     != null;
               }
            } else {
               return false;
            }
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   @Override
   public void onTick() {
      if (this.k != null && (this.k != mc.player || this.A != mc.world)) {
         ClientLog.XAsVoid("AutoTotem", "Player or world changed during a swap; state dropped without packets");
         this.lAsVoid();
      }

      if (this.isInGame()
         && mc.interactionManager != null
         && mc.player.isAlive()
         && !mc.player.isSpectator()
         && !p5.oAsBoolean()
         && mc.player.currentScreenHandler == mc.player.playerScreenHandler
         && mc.player.playerScreenHandler.getCursorStack().isEmpty()
         && this.EAsBoolean(mc.currentScreen)) {
         long var1 = Util.getMeasuringTimeMs();
         boolean var3 = this.d;
         this.d = mc.player.isUsingItem();
         if (this.v.iAsBoolean()) {
            this.cAsVoid(var1, var3);
         } else if (this.q.pAsBoolean()) {
            this.VAsVoid(var1);
         } else {
            boolean var4 = this.CAsBoolean();
            if (!var4) {
               this.K.SAsVoid();
            }

            at.Type_e var5 = this.rAsAt_Type_e(var4);
            if (var5 == null) {
               this.K.SAsVoid();
            } else {
               if (var4) {
                  double var8;
                  int var6 = !this.K.lAsBoolean() && ((var8 = this.randomDelay.getValue() - 0.0) == 0.0 ? 0 : (var8 < 0.0 ? -1 : 1)) > 0
                     ? ThreadLocalRandom.current().nextInt(this.randomDelay.getValue().intValue() + 1)
                     : 0;
                  if (!this.K.eAsBoolean(var1, this.delay.getValue().intValue() + var6)) {
                     return;
                  }
               } else if (!this.K.nAsBoolean(var1)) {
                  return;
               }

               if (!this.eAsBoolean(var5)) {
                  this.K.SAsVoid();
               } else if (this.swapStyle.pAsBoolean("Legit")) {
                  this.dAsVoid(var1, var5, var4);
               } else {
                  PlayerScreenHandler var7 = mc.player.playerScreenHandler;
                  mc.interactionManager.clickSlot(var7.syncId, var5.menuSlot(), var5.button(), SlotActionType.SWAP, mc.player);
                  this.m = mc.player;
                  this.G = mc.world;
                  this.e = var1;
                  this.K.cAsVoid(var1, var5.equips());
               }
            }
         }
      } else {
         this.OAsVoid("context changed", true);
         this.K.SAsVoid();
      }
   }

   private void dAsVoid(long var1, at.Type_e var3, boolean var4) {
      Screen var5 = mc.currentScreen;
      if (var3.equips() && var3.source() < 9 && this.offhandKey.getValue() && var5 == null) {
         if (!mc.player.isUsingItem()) {
            this.k = mc.player;
            this.A = mc.world;
            int var7 = mc.player.getInventory().getSelectedSlot();
            this.v.QAsVoid(var1, var7, var3.source(), this.hAsInt());
            ClientLog.XAsVoid("AutoTotem", "Swap hands route: hotbar {} to offhand, selected slot {}", var3.source() + 1, var7 + 1);
            this.cAsVoid(var1, mc.player.isUsingItem());
         }
      } else if (var4 || var5 != null || this.EAsBoolean()) {
         this.k = mc.player;
         this.A = mc.world;
         int var6 = var5 == null ? 1 : 0;
         this.q.GAsVoid(var1, var6 != 0);
         if (var6 != 0 && this.openInventory.getValue()) {
            this.r = new InventoryScreen(mc.player);
            mc.setScreen(this.r);
         }

         ClientLog.XAsVoid(
            "AutoTotem",
            "Inventory visit for {} from slot {}: {}, waiting for idle input and no sprint",
            var4 ? "equip" : "refill",
            var3.source(),
            var6 != 0 ? (this.openInventory.getValue() ? "opened the inventory" : "silent visit") : "player inventory already open"
         );
         this.VAsVoid(var1);
      }
   }

   private void VAsVoid(long var1) {
      if (this.r != null && mc.currentScreen != this.r) {
         ClientLog.XAsVoid("AutoTotem", "Opened inventory was closed after {} clicks; visit ended", this.q.PAsInt());
         this.r = null;
         this.q.XAsVoid();
         this.bAsVoid();
      } else if (this.q.LAsBoolean(var1)) {
         ClientLog.oAsVoid("AutoTotem", "Inventory visit timed out in stage {}; closing and releasing input", this.q.iAsAt_Type_a_Type_u());
         this.OAsVoid("timed out", true);
      } else {
         boolean var3 = this.EAsBoolean();
         switch (this.q.iAsAt_Type_a_Type_u()) {
            case STOPPING:
               if (this.q.PAsBoolean(var1, var3, this.hAsInt())) {
                  ClientLog.XAsVoid("AutoTotem", "Server has idle input and no sprint; first click after a human pause");
               }
               break;
            case CLICKING:
               if (!this.q.mAsBoolean(var1, var3)) {
                  return;
               }

               at.Type_e var4 = this.rAsAt_Type_e(this.CAsBoolean());
               if (var4 != null && this.eAsBoolean(var4)) {
                  PlayerScreenHandler var5 = mc.player.playerScreenHandler;
                  mc.interactionManager.clickSlot(var5.syncId, var4.menuSlot(), var4.button(), SlotActionType.SWAP, mc.player);
                  this.m = mc.player;
                  this.G = mc.world;
                  this.e = var1;
                  this.K.cAsVoid(var1, var4.equips());
                  this.q.xAsVoid(var1, this.hAsInt());
                  ClientLog.XAsVoid(
                     "AutoTotem",
                     "Clicked slot {} to {} (state {})",
                     var4.menuSlot(),
                     var4.button() == 40 ? "offhand" : "hotbar " + (var4.button() + 1),
                     var5.getRevision()
                  );
                  break;
               }

               if (this.q.dAsBoolean(var1, this.hAsInt())) {
                  ClientLog.XAsVoid("AutoTotem", "Clicks done ({}); closing the inventory after a pause", this.q.PAsInt());
               } else {
                  ClientLog.XAsVoid("AutoTotem", "Visit ended after {} clicks", this.q.PAsInt());
                  this.eAsVoid();
               }

               return;
            case CLOSING:
               if (!this.q.gAsBoolean(var1, var3)) {
                  return;
               }

               this.pAsVoid();
               ClientLog.XAsVoid("AutoTotem", "Closed the inventory while still; input released");
               this.eAsVoid();
               break;
            default:
               this.eAsVoid();
         }
      }
   }

   private void cAsVoid(long var1, boolean var3) {
      ClientPlayerEntity var4 = mc.player;
      PlayerInventory var5 = var4.getInventory();
      if (this.v.kAsBoolean(var1)) {
         ClientLog.oAsVoid("AutoTotem", "Swap hands route timed out in stage {}", this.v.aAsAt_Type_r_Type_v());
         this.OAsVoid("timed out", true);
      } else if (this.v.fAsBoolean(var1)) {
         int var6 = this.v.PAsInt();
         switch (this.v.aAsAt_Type_r_Type_v()) {
            case SELECT:
               if (!var5.getStack(var6).isOf(Items.TOTEM_OF_UNDYING) || var4.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING)) {
                  ClientLog.XAsVoid("AutoTotem", "Hotbar totem moved before selection; route cancelled");
                  this.v.oAsVoid();
                  this.bAsVoid();
                  return;
               }

               if (var3 || var4.isUsingItem()) {
                  return;
               }

               var5.setSelectedSlot(var6);
               this.v.mAsVoid(var1, this.hAsInt());
               ClientLog.XAsVoid("AutoTotem", "Selected hotbar {}; swap hands follows on a later tick", var6 + 1);
               break;
            case SWAP:
               if (var5.getSelectedSlot() != var6) {
                  ClientLog.XAsVoid("AutoTotem", "Player changed the selected slot; swap hands route cancelled");
                  this.v.oAsVoid();
                  this.bAsVoid();
                  return;
               }

               if (var4.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING) || !var5.getStack(var6).isOf(Items.TOTEM_OF_UNDYING)) {
                  ClientLog.XAsVoid("AutoTotem", "Offhand or hotbar changed before swap hands; restoring the slot");
                  this.v.mAsVoid(var1, this.hAsInt());
                  return;
               }

               if (var3 || !pk.hAsBoolean(mc, var4) || var4.handSwinging && var4.handSwingTicks < 0) {
                  return;
               }

               var4.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ORIGIN, Direction.DOWN));
               this.m = var4;
               this.G = mc.world;
               this.e = var1;
               this.K.cAsVoid(var1, true);
               this.v.mAsVoid(var1, this.hAsInt());
               ClientLog.XAsVoid("AutoTotem", "Pressed swap hands for hotbar {}", var6 + 1);
               break;
            case RESTORE:
               if (this.v.GAsBoolean(var5.getSelectedSlot())) {
                  var5.setSelectedSlot(this.v.sAsInt());
                  ClientLog.XAsVoid("AutoTotem", "Restored hotbar {}", this.v.sAsInt() + 1);
               } else {
                  ClientLog.XAsVoid("AutoTotem", "Player chose another slot; restore skipped");
               }

               this.v.oAsVoid();
               this.bAsVoid();
               break;
            default:
               this.v.oAsVoid();
               this.bAsVoid();
         }
      }
   }

   private void OAsVoid(String var1, boolean var2) {
      int var3 = this.k != null && this.k == mc.player && this.A == mc.world ? 1 : 0;
      if (this.q.pAsBoolean()) {
         if (var2 && var3 != 0 && this.q.nAsBoolean() && (this.q.PAsInt() > 0 || this.r != null)) {
            this.pAsVoid();
         }

         ClientLog.XAsVoid("AutoTotem", "Inventory visit stopped ({}) after {} clicks", var1, this.q.PAsInt());
      }

      if (this.v.iAsBoolean()
         && var3 != 0
         && this.v.aAsAt_Type_r_Type_v() != at.Type_r.Type_v.SELECT
         && this.v.GAsBoolean(this.k.getInventory().getSelectedSlot())) {
         this.k.getInventory().setSelectedSlot(this.v.sAsInt());
         ClientLog.XAsVoid("AutoTotem", "Swap hands route stopped ({}); restored hotbar {}", var1, this.v.sAsInt() + 1);
      }

      this.lAsVoid();
   }

   private void pAsVoid() {
      ClientPlayerEntity var1 = mc.player;
      if (var1 != null && var1 == this.k && var1.currentScreenHandler == var1.playerScreenHandler) {
         if (this.r != null && mc.currentScreen == this.r) {
            var1.closeHandledScreen();
         } else if (mc.currentScreen == null) {
            var1.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(var1.playerScreenHandler.syncId));
         }

         this.r = null;
      }
   }

   private void eAsVoid() {
      this.q.XAsVoid();
      this.bAsVoid();
   }

   private void bAsVoid() {
      if (!this.q.pAsBoolean() && !this.v.iAsBoolean()) {
         this.lAsVoid();
      }
   }

   private void lAsVoid() {
      this.q.XAsVoid();
      this.v.oAsVoid();
      this.k = null;
      this.A = null;
      this.r = null;
      this.d = false;
   }

   private boolean EAsBoolean(Screen var1) {
      return var1 == null || this.r != null && var1 == this.r || this.inInventory.getValue() && var1 instanceof InventoryScreen;
   }

   private boolean EAsBoolean() {
      ClientPlayerEntityAccessor var1 = (ClientPlayerEntityAccessor)mc.player;
      PlayerInput var2 = var1.larpLastSentInput();
      return var2 != null && at.cAsBoolean(var1.larpWasSprinting(), var2.forward(), var2.backward(), var2.left(), var2.right(), var2.jump(), var2.sneak());
   }

   private int hAsInt() {
      return at.yAsInt(this.clickDelay.getValue().intValue(), this.clickDelayMax.getValue().intValue(), ThreadLocalRandom.current().nextDouble());
   }

   private boolean CAsBoolean() {
      ItemStack var1 = mc.player.getOffHandStack();
      return at.oAsBoolean(
         var1.isOf(Items.TOTEM_OF_UNDYING),
         var1.isEmpty(),
         this.mode.pAsBoolean("Always"),
         (double)mc.player.getHealth(),
         (double)mc.player.getAbsorptionAmount(),
         this.includeAbsorption.getValue(),
         this.health.getValue()
      );
   }

   private at.Type_e rAsAt_Type_e(boolean var1) {
      long var2 = 0L;
      int var4 = 0;

      for (int var5 = 0; var5 < 36; var5++) {
         ItemStack var6 = mc.player.getInventory().getStack(var5);
         if (var6.isOf(Items.TOTEM_OF_UNDYING)) {
            var2 |= 1L << var5;
         } else if (var5 < 9 && var6.isEmpty()) {
            var4 |= 1 << var5;
         }
      }

      return at.rAsAt_Type_e(var2, var4, var1, this.swapMode.pAsBoolean("Hotbar"), this.hotbarRefill.getValue(), this.hotbarReserves.getValue().intValue());
   }

   private boolean eAsBoolean(at.Type_e var1) {
      Slot var2 = mc.player.playerScreenHandler.getSlot(var1.menuSlot());
      return var2.getStack().isOf(Items.TOTEM_OF_UNDYING)
         && var2.canTakeItems(mc.player)
         && var2.canInsert(var1.equips() ? mc.player.getOffHandStack() : ItemStack.EMPTY)
         && (var1.equips() || mc.player.getInventory().getStack(var1.button()).isEmpty());
   }
}
