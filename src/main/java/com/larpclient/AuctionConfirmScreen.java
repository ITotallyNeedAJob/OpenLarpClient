package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.tooltip.TooltipSubmenuHandler;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

public final class AuctionConfirmScreen extends GenericContainerScreen {
   private final BlockPos L;
   private final SpawnerEntityCatalog w;
   private final int h;
   private final int a;
   private final long X;
   private ItemStack E = ItemStack.EMPTY;
   private int H = -1;
   private long v;
   private boolean T;

   public AuctionConfirmScreen(BlockPos var1, SpawnerEntityCatalog var2, int var3, int var4) {
      super(iAsGenericContainerScreenHandler(var2, var3, var4), MinecraftClient.getInstance().player.getInventory(), Text.literal("Confirm Sell"));
      this.L = var1 == null ? null : var1.toImmutable();
      this.w = var2;
      this.h = var3;
      this.a = var4;
      FakeSpawnerController var5 = FakeSpawnerController.ZAsMa();
      this.X = var5.WAsLong(var2.a, var3) + var5.WAsLong(var2.o, var4);
   }

   private static GenericContainerScreenHandler iAsGenericContainerScreenHandler(SpawnerEntityCatalog var0, int var1, int var2) {
      SimpleInventory var3 = new SimpleInventory(27);
      var3.setStack(
         11,
         NAsItemStack(
            new ItemStack(Items.RED_STAINED_GLASS_PANE),
            Text.literal("Cancel").formatted(Formatting.WHITE),
            List.of(Text.literal("Click to cancel").formatted(new Formatting[]{Formatting.GRAY, Formatting.ITALIC}))
         )
      );
      ArrayList var4 = new ArrayList();
      if (var2 > 0) {
         var4.add(XAsText(var0.o, var2));
      }

      if (var1 > 0) {
         var4.add(XAsText(var0.a, var1));
      }

      var3.setStack(13, NAsItemStack(var0.AAsItemStack(), Text.literal("Loot").formatted(Formatting.WHITE), var4));
      long var5 = FakeSpawnerController.ZAsMa().WAsLong(var0.a, var1) + FakeSpawnerController.ZAsMa().WAsLong(var0.o, var2);
      var3.setStack(
         15,
         NAsItemStack(
            new ItemStack(Items.LIME_STAINED_GLASS_PANE),
            FakeSpawnerController.ZAsMa().OAsText(var5),
            List.of(Text.literal("Click to sell items").formatted(new Formatting[]{Formatting.GRAY, Formatting.ITALIC}))
         )
      );
      return new GenericContainerScreenHandler(ScreenHandlerType.GENERIC_9X3, 0, MinecraftClient.getInstance().player.getInventory(), var3, 3);
   }

   private static ItemStack NAsItemStack(ItemStack var0, Text var1, List<Text> var2) {
      var0.set(DataComponentTypes.CUSTOM_NAME, var1.copy().styled(var0x -> var0x.withItalic(false)));
      if (!var2.isEmpty()) {
         var0.set(DataComponentTypes.LORE, new LoreComponent(var2));
      }

      return var0;
   }

   private static Text XAsText(Item var0, int var1) {
      String var2 = var0 == Items.ROTTEN_FLESH ? "Rotten Flesh" : var0.getName(new ItemStack(var0)).getString() + "s";
      return Text.literal(var1 + " ").styled(var0x -> var0x.withColor(64648).withItalic(false)).append(Text.literal(var2).formatted(Formatting.WHITE));
   }

   protected void addTooltipSubmenuHandler(TooltipSubmenuHandler handler) {
   }

   private void TAsVoid() {
      ((GenericContainerScreenHandler)this.handler).setCursorStack(ItemStack.EMPTY);
      this.client.setScreen(null);
      FakeSpawnerController.ZAsMa().lAsVoid(this.L);
   }

   public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
      if (!this.E.isEmpty() && System.currentTimeMillis() >= this.v) {
         if (this.T) {
            this.TAsVoid();
            return;
         }

         ((GenericContainerScreenHandler)this.handler).getSlot(this.H).setStackNoCallbacks(this.E);
         this.E = ItemStack.EMPTY;
      }

      ((GenericContainerScreenHandler)this.handler).setCursorStack(this.E.isEmpty() ? this.client.player.playerScreenHandler.getCursorStack() : this.E);
      super.render(context, mouseX, mouseY, deltaTicks);
      this.drawMouseoverTooltip(context, mouseX, mouseY);
   }

   public boolean mouseClicked(Click click, boolean doubled) {
      if (this.E.isEmpty() && (click.button() == 0 || click.button() == 1)) {
         for (int var6 : new int[]{11, 13, 15}) {
            Slot var7 = ((GenericContainerScreenHandler)this.handler).getSlot(var6);
            int var8 = this.x + var7.x;
            int var9 = this.y + var7.y;
            if (!(click.x() < (double)var8) && !(click.x() >= (double)(var8 + 16)) && !(click.y() < (double)var9) && !(click.y() >= (double)(var9 + 16))) {
               if (var6 == 11) {
                  this.TAsVoid();
                  return true;
               }

               if (var6 == 15) {
                  if (this.X <= 0L) {
                     ChatMessages.RAsVoid(Text.literal("Spawner loot price unavailable").formatted(Formatting.RED), true);
                     return true;
                  }

                  FakeSpawnerController.ZAsMa().oAsVoid(this.L, this.w.a, this.h);
                  FakeSpawnerController.ZAsMa().oAsVoid(this.L, this.w.o, this.a);
                  AuctionNotifier.qAsVoid(this.X);
                  this.T = true;
               }

               this.H = var6;
               this.E = var7.getStack().copy();
               var7.setStackNoCallbacks(ItemStack.EMPTY);
               ((GenericContainerScreenHandler)this.handler).setCursorStack(this.E);
               this.v = System.currentTimeMillis() + 200L;
               return true;
            }
         }

         return true;
      } else {
         return true;
      }
   }

   protected void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType) {
   }

   public void close() {
      this.TAsVoid();
   }

   public void removed() {
      ((GenericContainerScreenHandler)this.handler).setCursorStack(ItemStack.EMPTY);
   }
}
