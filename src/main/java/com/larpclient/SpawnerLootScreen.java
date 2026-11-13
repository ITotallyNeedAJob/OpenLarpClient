package com.larpclient;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.tooltip.TooltipSubmenuHandler;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

public class SpawnerLootScreen extends GenericContainerScreen {
   private final BlockPos c;
   private ItemStack I = ItemStack.EMPTY;
   private int P = -1;
   private long e;
   private long Z;
   private int n;
   private int H;
   private boolean w;
   private static final SimpleInventory h = new SimpleInventory(1);

   private void dAsVoid() {
      if (!this.I.isEmpty()) {
         if (this.P < 54) {
            ((GenericContainerScreenHandler)this.handler).getSlot(this.P).setStackNoCallbacks(this.I);
         }

         this.I = ItemStack.EMPTY;
         this.P = -1;
      }
   }

   private void LAsVoid(int var1) {
      this.P = var1;
      Slot var2 = ((GenericContainerScreenHandler)this.handler).getSlot(var1);
      this.I = (var1 < 54 ? var2 : VisualInventory.eAsSlot(this.handler, var2)).getStack().copy();
      if (var1 < 54) {
         var2.setStackNoCallbacks(ItemStack.EMPTY);
      }

      ((GenericContainerScreenHandler)this.handler).setCursorStack(this.I);
      this.e = System.currentTimeMillis() + 200L;
   }

   private int rAsInt(Item var1) {
      int var2 = 0;

      for (int var3 = 0; var3 < 45; var3++) {
         if (((GenericContainerScreenHandler)this.handler).getSlot(var3).getStack().isOf(var1)) {
            var2 += ((GenericContainerScreenHandler)this.handler).getSlot(var3).getStack().getCount();
         }
      }

      return var2;
   }

   public SpawnerLootScreen(GenericContainerScreenHandler var1, PlayerInventory var2, Text var3) {
      this(var1, var2, var3, null);
   }

   public SpawnerLootScreen(GenericContainerScreenHandler var1, PlayerInventory var2, Text var3, BlockPos var4) {
      super(var1, var2, var3);
      this.c = var4 == null ? null : var4.toImmutable();
   }

   protected void addTooltipSubmenuHandler(TooltipSubmenuHandler handler) {
   }

   public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
      if (!this.I.isEmpty() && System.currentTimeMillis() >= this.e) {
         this.dAsVoid();
      }

      if (this.w && System.currentTimeMillis() >= this.Z) {
         this.w = false;
         this.client.setScreen(new AuctionConfirmScreen(this.c, FakeSpawnerController.ZAsMa().QAsNa(this.c), this.n, this.H));
      } else {
         if (this.client.player != null) {
            ((GenericContainerScreenHandler)this.handler).setCursorStack(this.I.isEmpty() ? this.client.player.playerScreenHandler.getCursorStack() : this.I);
         }

         super.render(context, mouseX, mouseY, deltaTicks);
         this.drawMouseoverTooltip(context, mouseX, mouseY);
      }
   }

   protected void drawSlot(DrawContext context, Slot slot, int mouseX, int mouseY) {
      if (this.P >= 54 && slot.id == this.P && !this.I.isEmpty()) {
         Slot var5 = new Slot(h, 0, slot.x, slot.y);
         var5.id = slot.id;
         super.drawSlot(context, var5, mouseX, mouseY);
      } else {
         super.drawSlot(context, slot, mouseX, mouseY);
      }
   }

   public boolean mouseClicked(Click click, boolean doubled) {
      if (this.I.isEmpty() && !this.w) {
         if (click.button() != 0 && click.button() != 1) {
            return true;
         } else {
            for (int var3 = 0; var3 < ((GenericContainerScreenHandler)this.handler).slots.size(); var3++) {
               Slot var4 = (Slot)((GenericContainerScreenHandler)this.handler).slots.get(var3);
               int var5 = this.x + var4.x;
               int var6 = this.y + var4.y;
               if (!(click.x() < (double)var5) && !(click.x() >= (double)(var5 + 16)) && !(click.y() < (double)var6) && !(click.y() >= (double)(var6 + 16))) {
                  FakeSpawnerController var7 = FakeSpawnerController.ZAsMa();
                  if (var3 < 45 && SpawnerEntityCatalog.oAsBoolean(var4.getStack().getItem())) {
                     int var11 = !this.client.options.sneakKey.isPressed() && !this.client.player.isSneaking() ? 0 : 1;
                     int var12 = var11 != 0
                        ? var7.yAsInt(var4.getStack().getItem(), var4.getStack().getCount())
                        : var7.HAsInt(var4.getStack().getItem(), click.button() == 1 ? 1 : var4.getStack().getCount());
                     if (var12 > 0) {
                        var4.getStack().decrement(var12);
                     }
                  } else if ((var3 == 48 || var3 == 50) && this.client.player.playerScreenHandler.getCursorStack().isEmpty()) {
                     SpawnerEntityCatalog var8 = var7.QAsNa(this.c);
                     int var9 = this.rAsInt(var8.a);
                     int var10 = this.rAsInt(var8.o);
                     if (var9 + var10 == 0) {
                        this.LAsVoid(var3);
                        if (var3 == 48) {
                           ChatMessages.RAsVoid(Text.literal("Spawner has no loot").formatted(Formatting.RED), true);
                        }
                     } else if (var3 == 48) {
                        this.LAsVoid(var3);
                        this.n = var9;
                        this.H = var10;
                        this.Z = this.e + 50L;
                        this.w = true;
                     }
                  } else if (var3 == 49 && this.client.player.playerScreenHandler.getCursorStack().isEmpty()) {
                     this.LAsVoid(var3);
                  } else if (var3 < 54) {
                     var7.dAsBoolean(var3);
                  } else if (var3 >= 54
                     && !VisualInventory.eAsSlot(this.handler, var4).getStack().isEmpty()
                     && this.client.player.playerScreenHandler.getCursorStack().isEmpty()) {
                     this.LAsVoid(var3);
                  }

                  if (this.client.player != null) {
                     ((GenericContainerScreenHandler)this.handler)
                        .setCursorStack(this.I.isEmpty() ? this.client.player.playerScreenHandler.getCursorStack() : this.I);
                  }
                  break;
               }
            }

            return true;
         }
      } else {
         return true;
      }
   }

   protected void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType) {
   }

   public void close() {
      this.client.setScreen(null);
      ShopPurchaseController.wAsVoid(this.c);
   }

   public void removed() {
      this.dAsVoid();
   }
}
