package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.oe;
import java.util.List;
import java.util.Set;
import net.minecraft.text.Text;

public final class HomeMacro extends Module {
   private final NumberSetting homeSlot = this.addSetting(new NumberSetting("Home Slot", "Numbered home to delete and set again", 1.0, 1.0, 5.0, 1.0));
   private final BooleanSetting chatFeedback = this.addSetting(new BooleanSetting("Chat Feedback", "Confirm when the replacement command is sent", true));
   private Object y;
   private Object W;
   private Object Q;
   private final oe.Type_z H = new oe.Type_z();
   private int A;

   public HomeMacro() {
      super("Home Macro", "Delete a numbered home, then set it again after one second", Category.PLAYER, -1);
   }

   @Override
   public String getDisplayName() {
      return "Home Macro";
   }

   public List<Setting<?>> zAsList() {
      return List.of(this.homeSlot, this.chatFeedback);
   }

   @Override
   public Set<k> getNetworkImpacts() {
      return Set.of(k.SERVER_STATE_MODIFYING);
   }

   @Override
   public String getStatus() {
      return this.H.TAsBoolean() ? "Replacing Home " + this.A : "";
   }

   @Override
   public void onEnable() {
      this.onDisable();
      if (this.isInGame() && mc.player.networkHandler != null) {
         this.y = mc.player;
         this.W = mc.world;
         this.Q = mc.player.networkHandler;
         this.A = this.homeSlot.getValue().intValue();
         this.H.qAsVoid(this.A, System.nanoTime());
         mc.player.networkHandler.sendChatCommand("delhome " + this.A);
         ClientLog.XAsVoid("HomeMacro", "Delete sent for home {}; replacement waits one second", this.A);
      } else {
         this.disable();
      }
   }

   @Override
   public void onTick() {
      if (this.H.TAsBoolean()) {
         if (this.isInGame() && mc.player == this.y && mc.world == this.W && mc.player.networkHandler == this.Q && mc.player.isAlive()) {
            int var1 = this.H.BAsInt(System.nanoTime());
            if (var1 != 0) {
               mc.player.networkHandler.sendChatCommand("sethome " + this.A);
               if (this.chatFeedback.getValue()) {
                  mc.getMessageHandler().onGameMessage(Text.literal("[LARP] Home " + this.A + " replacement command sent."), false);
               }

               ClientLog.XAsVoid("HomeMacro", "Replacement command sent for home {}", this.A);
               this.disable();
            }
         } else {
            ClientLog.XAsVoid("HomeMacro", "Replacement cancelled because the active session changed");
            this.disable();
         }
      }
   }

   @Override
   public void onDisable() {
      this.H.AAsVoid();
      this.A = 0;
      this.y = this.W = this.Q = null;
   }
}
