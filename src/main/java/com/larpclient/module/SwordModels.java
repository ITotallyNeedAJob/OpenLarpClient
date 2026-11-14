package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.KnifeAssets;
import com.larpclient.KnifeType;
import com.larpclient.WatchInspectAnimation;
import com.larpclient.KnifeState;
import com.larpclient.MediaSession;
import com.larpclient.KnifeMeshRendererState;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Hand;

public final class SwordModels extends Module {
   public final ModeSetting model = this.addSetting(
      new ModeSetting("Model", "First-person sword replacement", "Bayonet", "Bayonet", "Stiletto", "Butterfly", "Luxury Watch", "Ledger Nano S")
   );
   public final ModeSetting finish = this.addSetting(
      new ModeSetting("Finish", "Moving galaxy colors on the blade", "Galaxy", "Galaxy", "Violet", "Rose", "Ice", "Steel")
   );
   public final NumberSetting modelScale = this.addSetting(new NumberSetting("Model Scale", "Knife size or watch case size", 1.0, 0.7, 1.3, 0.05));
   public final NumberSetting handX = this.addSetting(new NumberSetting("Hand X", "Horizontal hand offset", 0.0, -0.2, 0.2, 0.01));
   public final NumberSetting handY = this.addSetting(new NumberSetting("Hand Y", "Vertical hand offset", 0.0, -0.2, 0.2, 0.01));
   public final NumberSetting handZ = this.addSetting(new NumberSetting("Hand Z", "Depth of the hand", 0.0, -0.2, 0.15, 0.01));
   public final KeybindSetting inspectKey = this.addSetting(
      new KeybindSetting("Inspect Key", "Inspect main-hand knife or watch, or offhand if main hand is not a sword", 71)
   );
   public final NumberSetting inspectSpeed = this.addSetting(new NumberSetting("Inspect Speed", "2 matches the reference showcase tempo", 2.0, 0.5, 2.0, 0.05));
   public final BooleanSetting idleMotion = this.addSetting(new BooleanSetting("Idle Motion", "Subtle breathing motion", true));
   public final BooleanSetting enchantmentGlint = this.addSetting(
      new BooleanSetting("Enchantment Glint", "Show enchantment shimmer on the model geometry", true)
   );
   private final BooleanSetting resetDefaults = this.addSetting(new BooleanSetting("Reset Defaults", "Restore all Sword Skins settings", false));
   private static SwordModels y;
   private final bj[] b = new bj[]{new bj(), new bj()};
   private final r8[] B = new r8[]{new r8(), new r8()};
   private final WatchInspectAnimation[] Q = new WatchInspectAnimation[]{new WatchInspectAnimation(), new WatchInspectAnimation()};
   private final ItemStack[] g = new ItemStack[]{ItemStack.EMPTY, ItemStack.EMPTY};
   private int O = -1;

   public SwordModels() {
      super("Sword Models", "Replace held swords with CS:GO knives, a Ledger or a wristwatch", Category.VISUALS, -1);
      y = this;
      this.finish.visibleWhen(() -> !this.fAsBc().XAsBoolean());
      this.resetDefaults.visibleWhen(() -> false);
      this.inspectKey.visibleWhen(() -> this.fAsBc().VAsBoolean());
      this.inspectSpeed.visibleWhen(() -> this.fAsBc().VAsBoolean());
      this.idleMotion.visibleWhen(() -> !this.fAsBc().XAsBoolean());
      this.model.onChanged(var1 -> this.AAsVoid());
      this.resetDefaults.onChanged(var1 -> {
         if (var1) {
            for (Setting var3 : this.getSettings()) {
               var3.reset();
            }

            this.AAsVoid();
         }
      });
   }

   @Override
   public String getDisplayName() {
      return "Sword Skins";
   }

   @Override
   public List<String> getSearchAliases() {
      return List.of("CS:GO Swords", "Knives");
   }

   public static SwordModels vAsSwordModels() {
      return y;
   }

   public static boolean PAsBoolean(ItemStack var0) {
      return var0 != null && !var0.isEmpty() && var0.isIn(ItemTags.SWORDS);
   }

   public KnifeType fAsBc() {
      return KnifeType.bAsBc(this.model.getValue());
   }

   public bj WAsBj(Hand var1) {
      return this.b[var1 == Hand.MAIN_HAND ? 0 : 1];
   }

   public r8 NAsR8(Hand var1) {
      return this.B[var1 == Hand.MAIN_HAND ? 0 : 1];
   }

   public WatchInspectAnimation NAsQt(Hand var1) {
      return this.Q[var1 == Hand.MAIN_HAND ? 0 : 1];
   }

   private void HAsVoid(int var1) {
      this.b[var1].QAsVoid();
      this.B[var1].aAsVoid();
      this.Q[var1].KAsVoid();
   }

   public boolean rAsBoolean(ItemStack var1) {
      return this.isEnabled() ? this.isOperational() && PAsBoolean(var1) && KnifeAssets.lAsDe(this.fAsBc()) != null : false;
   }

   public int ZAsInt() {
      String var1 = this.finish.getValue();

      return switch (var1) {
         case "Violet" -> 1;
         case "Rose" -> 2;
         case "Ice" -> 3;
         case "Steel" -> 4;
         default -> 0;
      };
   }

   private void AAsVoid() {
      for (int var1 = 0; var1 < this.b.length; var1++) {
         this.HAsVoid(var1);
      }

      this.g[0] = this.g[1] = ItemStack.EMPTY;
      this.O = -1;
   }

   @Override
   public void onEnable() {
      this.AAsVoid();
   }

   @Override
   public void onDisable() {
      this.AAsVoid();
   }

   @Override
   public void onTick() {
      ClientPlayerEntity var1 = this.getPlayer();
      if (var1 == null) {
         this.AAsVoid();
      } else {
         int var2 = var1.getInventory().getSelectedSlot();
         if (var2 != this.O) {
            this.HAsVoid(0);
            this.O = var2;
         }

         for (Hand var6 : Hand.values()) {
            int var7 = var6 == Hand.MAIN_HAND ? 0 : 1;
            ItemStack var8 = var1.getStackInHand(var6);
            if (var8 != this.g[var7] || !PAsBoolean(var8)) {
               this.HAsVoid(var7);
            }

            this.g[var7] = var8;
            if (var1.handSwinging && var1.preferredHand == var6) {
               this.b[var7].sAsVoid();
            }
         }
      }
   }

   public void oAsVoid(int var1) {
      if (this.isEnabled()) {
         MinecraftClient var2 = getMc();
         int var3 = var1 == this.inspectKey.getValue()
               && this.isOperational()
               && var2.player != null
               && var2.currentScreen == null
               && var2.isWindowFocused()
               && !var2.isPaused()
            ? 1
            : 0;
         if (var3 != 0) {
            ClientPlayerEntity var4 = var2.player;
            Hand var5 = PAsBoolean(var4.getMainHandStack()) ? Hand.MAIN_HAND : Hand.OFF_HAND;
            if (this.fAsBc().VAsBoolean()
               && this.rAsBoolean(var4.getStackInHand(var5))
               && !var4.handSwinging
               && !var4.isUsingItem()
               && this.WAsBj(var5).oAsBoolean(mb.RAsDouble(), this.inspectSpeed.getValue())
               && this.fAsBc() == KnifeType.WATCH) {
               ClientLog.XAsVoid("SwordModels", "Watch inspect started in {} at speed {}", var5, this.inspectSpeed.getValue());
            }
         }
      }
   }
}
