package com.larpclient.module;

import com.google.gson.JsonObject;
import com.larpclient.ClientLog;
import com.larpclient.FakeBalanceFormatter;
import com.larpclient.ChatPacketReader;
import java.math.BigDecimal;
import java.util.List;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents.AllowChat;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents.AllowCommand;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.Packet;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardEntry;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.Team;
import net.minecraft.scoreboard.number.StyledNumberFormat;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public final class FakePay extends Module {
   private static volatile FakePay j;
   private volatile boolean g;
   private final ab c = new ab();
   private Object z;
   private boolean L;
   private boolean A;
   private boolean U;
   private boolean t;
   private long T;
   private boolean k;
   private final BooleanSetting spoofMoney = this.addSetting(
      new BooleanSetting("Spoof Money", "Replace sidebar, tab and /bal displays with your local fake balance", false)
   );
   private final NumberSetting fakeBalance = this.addSetting(
      new NumberSetting("Fake Balance", "Starting fake money; click the value to enter an amount", 1000000.0, 0.0, 1.0E9, 0.01) {
         @Override
         public String getValueAsString() {
            return BigDecimal.valueOf(this.getValue()).stripTrailingZeros().toPlainString();
         }
      }
   );
   private final NumberSetting balanceBillions = this.addSetting(
      new NumberSetting("Balance Billions", "Whole billions in the submitted balance", 0.0, 0.0, 10000.0, 1.0)
   );
   private final BooleanSetting deductPayments = this.addSetting(
      new BooleanSetting("Deduct Payments", "Subtract successful fake payments and reject amounts above the fake balance", true)
   );
   private final BooleanSetting actionBar = this.addSetting(new BooleanSetting("Action Bar", "Also show the local payment above the hotbar", true));
   private final BooleanSetting sound = this.addSetting(new BooleanSetting("Sound", "Play a local payment confirmation ding", true));
   private final BooleanSetting hideErrors = this.addSetting(
      new BooleanSetting("Hide Errors", "Hide local feedback for invalid /pay attempts and insufficient fake funds", false)
   );

   public FakePay() {
      super("FakePay", "Show fake local /pay messages", Category.PLAYER, -1);
      j = this;
      this.fakeBalance.visibleWhen(this.spoofMoney::getValue).onChanged(var1 -> this.DAsVoid());
      this.balanceBillions.visibleWhen(() -> false).onChanged(var1 -> this.DAsVoid());
      this.deductPayments.visibleWhen(this.spoofMoney::getValue);
      this.spoofMoney.onChanged(var1 -> this.DAsVoid());
      ClientSendMessageEvents.ALLOW_COMMAND.register((AllowCommand)var1 -> !this.PAsBoolean(var1));
      ClientSendMessageEvents.ALLOW_CHAT.register((AllowChat)var1 -> !var1.strip().startsWith("/") || !this.PAsBoolean(var1));
      ClientReceiveMessageEvents.MODIFY_GAME.register(FakePay::hAsText);
   }

   private boolean PAsBoolean(String var1) {
      return com.larpclient.g.ZAsBoolean(this.g, var1, this::mAsVoid, FakePay::VAsVoid);
   }

   @Override
   public void enable() {
      this.g = true;
      super.enable();
      this.g = this.isEnabled();
   }

   @Override
   public void disable() {
      this.g = false;
      this.DAsVoid();
      super.disable();
   }

   private static void VAsVoid(RuntimeException var0) {
      ClientLog.lAsVoid("FakePay", "Local simulation failed; payment remains blocked", var0);
   }

   private void mAsVoid(com.larpclient.g.Type_t var1) {
      MinecraftClient var2 = getMc();
      if (var2 != null && var2.world != null && var2.player != null) {
         if (!var1.valid()) {
            Text var5 = this.CAsText(var1.error());
            if (var5 != null) {
               var2.getMessageHandler().onGameMessage(var5, false);
            }

            ClientLog.XAsVoid("FakePay", "Invalid payment attempt consumed locally");
         } else {
            if (this.spoofMoney.getValue() && this.deductPayments.getValue()) {
               this.jAsVoid(var2.world);
               if (!this.c.MAsBoolean(var1.amount())) {
                  Text var4 = this.CAsText("Not enough fake money. Balance: $ " + com.larpclient.g.gAsString(this.c.gAsBigDecimal()));
                  if (var4 != null) {
                     var2.getMessageHandler().onGameMessage(var4, false);
                  }

                  ClientLog.XAsVoid("FakePay", "Payment rejected locally: insufficient fake balance");
                  return;
               }

               ClientLog.XAsVoid("FakePay", "Local fake balance after payment: {}", this.c.gAsBigDecimal());
            }

            Text var3 = JAsText(var1);
            var2.getMessageHandler().onGameMessage(var3, false);
            if (this.actionBar.getValue()) {
               var2.player.sendMessage(var3, true);
            }

            if (this.sound.getValue()) {
               var2.player.playSound(SoundEvents.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);
            }

            ClientLog.XAsVoid("FakePay", "Simulated payment shown locally; nothing sent");
         }
      }
   }

   public static Text JAsText(com.larpclient.g.Type_t var0) {
      if (!var0.valid()) {
         throw new IllegalArgumentException("Invalid simulated payment");
      } else {
         return Text.literal("You paid " + var0.player() + " ")
            .styled(var0x -> var0x.withColor(16777215))
            .append(Text.literal("$").styled(var0x -> var0x.withColor(64512)))
            .append(Text.literal(" " + com.larpclient.g.gAsString(var0.amount())).styled(var0x -> var0x.withColor(16777215)));
      }
   }

   private void DAsVoid() {
      this.z = null;
      this.L = false;
      this.A = false;
      this.U = false;
      this.t = false;
      this.T = 0L;
      this.k = false;
      this.c.SAsVoid(this.tAsBigDecimal());
   }

   private void jAsVoid(Object var1) {
      if (this.z != var1) {
         this.c.SAsVoid(this.tAsBigDecimal());
         this.z = var1;
         this.L = false;
         this.A = false;
         this.U = false;
         this.t = false;
         this.T = 0L;
         this.k = false;
      }
   }

   public NumberSetting fakeBalance() {
      return this.fakeBalance;
   }

   private BigDecimal tAsBigDecimal() {
      return BigDecimal.valueOf(this.balanceBillions.getValue()).scaleByPowerOfTen(9).add(BigDecimal.valueOf(this.fakeBalance.getValue())).min(ab.j);
   }

   public String TAsString() {
      return this.tAsBigDecimal().stripTrailingZeros().toPlainString();
   }

   public static void hAsVoid(JsonObject var0) {
      if (var0.has("Fake Balance") && !var0.has("Balance Billions")) {
         var0.addProperty("Balance Billions", 0);
      }
   }

   public String qAsString(String var1) {
      try {
         BigDecimal var2 = ab.jAsBigDecimal(var1);
         BigDecimal var3 = BigDecimal.valueOf(1000000000L);
         this.fakeBalance.setClampedValue(var2.remainder(var3).doubleValue());
         this.balanceBillions.setClampedValue(var2.divideToIntegralValue(var3).doubleValue());
         return "";
      } catch (IllegalArgumentException var41) {
         return var41.getMessage();
      }
   }

   public String mAsString() {
      MinecraftClient var1 = getMc();
      if (this.g && this.spoofMoney.getValue() && var1 != null && var1.world != null) {
         this.jAsVoid(var1.world);
      }

      return "Balance: $ " + com.larpclient.g.gAsString(this.z == null ? this.tAsBigDecimal() : this.c.gAsBigDecimal());
   }

   public Text CAsText(String var1) {
      return this.hideErrors.getValue() ? null : Text.literal("[Larp/FakePay] " + var1).styled(var0 -> var0.withColor(15834027));
   }

   public static MutableText NAsMutableText(MutableText var0, Text var1) {
      return j != null && j.isEnabled() ? VAsMutableText(var0, var1, "", "") : var0;
   }

   public static MutableText VAsMutableText(MutableText var0, Text var1, String var2, String var3) {
      if (j != null && j.isEnabled()) {
         FakePay var4 = j;
         MinecraftClient var5 = getMc();
         if (var4 != null && var4.g && var4.spoofMoney.getValue() && var5 != null && var5.world != null) {
            var4.jAsVoid(var5.world);
            MutableText var6 = var1 == null
               ? ab.vAsMutableText(var0, var4.c.gAsBigDecimal(), var2, var3)
               : (ab.sAsBoolean(var1, var2, var3) ? ab.KAsMutableText(var0, var4.c.gAsBigDecimal()) : var0);
            if (var6 != var0 && !var4.L) {
               var4.L = true;
               ClientLog.GAsVoid("FakePay", "Money sidebar row matched; local display override active");
            }

            return var6;
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   public static Text wAsText(Text var0) {
      if (j != null && j.isEnabled()) {
         FakePay var1 = j;
         MinecraftClient var2 = getMc();
         if (var0 != null && var1 != null && var1.g && var1.spoofMoney.getValue() && var2 != null && var2.world != null) {
            var1.jAsVoid(var2.world);
            Text var3 = ab.QAsText(var0, var1.c.gAsBigDecimal());
            if (var3 != var0 && !var1.A) {
               var1.A = true;
               ClientLog.GAsVoid("FakePay", "Tab footer money matched; shared local balance override active");
            }

            return var3;
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   public static Text hAsText(Text var0, boolean var1) {
      if (j != null && j.isEnabled()) {
         FakePay var2 = j;
         MinecraftClient var3 = getMc();
         if (var0 != null && var2 != null && var2.g && var2.spoofMoney.getValue() && var3 != null && var3.world != null) {
            var2.jAsVoid(var3.world);
            Text var4 = ab.JAsText(var0, var2.c.gAsBigDecimal());
            if (var4 != var0 && (var1 ? !var2.t : !var2.U)) {
               if (var1) {
                  var2.t = true;
               } else {
                  var2.U = true;
               }

               ClientLog.GAsVoid("FakePay", "Balance response matched in {}; shared local balance override active", var1 ? "action bar" : "chat");
            }

            return var4;
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   public static void aAsVoid(ScoreboardObjective var0) {
      FakePay var1 = j;
      MinecraftClient var2 = getMc();
      if (var1 != null && var1.g && var1.spoofMoney.getValue() && var2 != null && var2.world != null) {
         var1.jAsVoid(var2.world);
         if (!var1.L && !var1.k) {
            long var3 = System.nanoTime();
            if (var1.T == 0L) {
               var1.T = var3;
            } else if (var3 - var1.T >= 3000000000L) {
               Scoreboard var5 = var0.getScoreboard();
               List var6 = var5.getScoreboardEntries(var0).stream().filter(var0x -> !var0x.hidden()).limit(15L).toList();
               if (!var6.isEmpty()) {
                  var1.k = true;
                  ClientLog.oAsVoid("FakePay", "No money row matched after 3 seconds; received sidebar snapshot follows");

                  for (ScoreboardEntry var8 : (Iterable<ScoreboardEntry>) (var6)) {
                     Team var9 = var5.getScoreHolderTeam(var8.owner());
                     MutableText var10 = Team.decorateName(var9, var8.name());
                     MutableText var11 = var8.formatted(var0.getNumberFormatOr(StyledNumberFormat.RED));
                     ClientLog.GAsVoid(
                        "FakePay",
                        "Sidebar owner={} team={} label={} value={}",
                        eAsString(var8.owner()),
                        eAsString(var9 == null ? "" : var9.getName()),
                        eAsString(var10.getString()),
                        eAsString(var11.getString())
                     );
                  }
               }
            }
         }
      }
   }

   private static String eAsString(String var0) {
      StringBuilder var1 = new StringBuilder();
      var0.codePoints().limit(160L).forEach(var1xx -> {
         if (var1xx >= 32 && var1xx <= 126) {
            var1.appendCodePoint(var1xx);
         } else {
            var1.append(String.format("\\u{%04x}", var1xx));
         }
      });
      return var1.toString();
   }

   public static boolean tAsBoolean(Packet<?> var0) {
      FakePay var1 = j;
      if (var1 != null && var1.g) {
         String var2 = l8.iAsString(var0);
         return var2 == null ? false : com.larpclient.g.ZAsBoolean(true, var2, var1xx -> {
            MinecraftClient var2x = getMc();
            if (var2x != null) {
               ClientWorld var3 = var2x.world;
               ClientPlayerEntity var4 = var2x.player;
               var2x.execute(() -> {
                  if (var3 != null && var2x.world == var3 && var2x.player == var4 && var1.g) {
                     try {
                        var1.mAsVoid(var1xx);
                     } catch (RuntimeException var6) {
                        VAsVoid(var6);
                     }
                  }
               });
               ClientLog.XAsVoid("FakePay", "Final outgoing payment packet blocked");
            }
         }, FakePay::VAsVoid);
      } else {
         return false;
      }
   }

   @Override
   public void onEnable() {
      this.DAsVoid();
      ClientLog.GAsVoid("FakePay", "Local /pay simulation active; valid and invalid payment attempts are blocked");
   }

   @Override
   public void onDisable() {
      this.DAsVoid();
      if (!this.isEnabled()) {
         this.g = false;
      }

      ClientLog.XAsVoid("FakePay", "Runtime released; command guard follows enabled preference");
   }
}
