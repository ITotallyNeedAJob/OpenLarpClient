package com.larpclient.module;

import com.larpclient.ClientLog;
import com.larpclient.LarpClient;
import com.larpclient.NotificationHud;
import com.larpclient.LayerJob;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;

public final class StaffList extends Module {
   public final BooleanSetting playerHeads = this.addSetting(new BooleanSetting("Player Heads", "Show staff player heads", true));
   public final BooleanSetting alerts = this.addSetting(new BooleanSetting("Alerts", "Notify when staff join, leave or go into Vanish", true));
   public final TextSetting staffNames = this.addSetting(
      new TextSetting(
         "Staff Names", "Players who always count as staff, separated by commas or spaces. Needed for Vanish and for servers that hide staff ranks", ""
      )
   );
   private static final Pattern r = Pattern.compile("[.*]?[A-Za-z0-9_]{1,16}");
   private static volatile StaffList.Type_o f = new StaffList.Type_o("", Set.of());
   private final StaffList.Type_a Z = new StaffList.Type_a();
   private int k;

   public StaffList() {
      super("Staff List", "Show staff from server rank labels and your Staff Names list", Category.RENDER, -1);
      this.staffNames.dAsE("Name1, Name2");
   }

   private static StaffList DAsStaffList() {
      LarpClient var0 = LarpClient.CAsLarpClient();
      return var0 == null ? null : var0.mAsF().EAsModule(StaffList.class);
   }

   public static boolean yAsBoolean() {
      StaffList var0 = DAsStaffList();
      return var0 != null && var0.isOperational();
   }

   public static boolean FAsBoolean() {
      StaffList var0 = DAsStaffList();
      return var0 == null || var0.playerHeads.getValue();
   }

   public static Set<String> zAsSet() {
      StaffList var0 = DAsStaffList();
      String var1 = var0 == null ? "" : var0.staffNames.getValue();
      StaffList.Type_o var2 = f;
      if (!var2.raw().equals(var1)) {
         f = var2 = new StaffList.Type_o(var1, IAsSet(var1));
      }

      return var2.lowerCase();
   }

   public static Set<String> IAsSet(String var0) {
      LinkedHashSet var1 = new LinkedHashSet();

      for (String var5 : var0.split("[\\s,;]+")) {
         if (!var5.isEmpty()) {
            var1.add(var5.toLowerCase(Locale.ROOT));
         }
      }

      return Collections.unmodifiableSet(var1);
   }

   public static List<StaffList.Type_u> kAsList() {
      MinecraftClient var0 = MinecraftClient.getInstance();
      ArrayList var1 = new ArrayList();
      ClientPlayNetworkHandler var2 = var0.getNetworkHandler();
      if (var2 != null && var0.world != null) {
         Set var3 = zAsSet();
         HashSet var4 = new HashSet();

         for (PlayerListEntry var6 : var2.getPlayerList()) {
            String var7 = var6.getProfile().name();
            var4.add(var7.toLowerCase(Locale.ROOT));
            if (r.matcher(var7).matches() && dAsBoolean(var6, var3)) {
               StaffList.Type_j var8 = var6.getGameMode() == GameMode.SPECTATOR
                  ? StaffList.Type_j.SPECTATOR
                  : (var0.world.getPlayerByUuid(var6.getProfile().id()) != null ? StaffList.Type_j.NEAR : StaffList.Type_j.ONLINE);
               var1.add(new StaffList.Type_u(var7, var6, var8));
            }
         }

         if (!var3.isEmpty()) {
            for (Team var11 : var0.world.getScoreboard().getTeams()) {
               for (String var13 : var11.getPlayerList()) {
                  String var9 = var13.toLowerCase(Locale.ROOT);
                  if (var3.contains(var9) && var4.add(var9)) {
                     var1.add(new StaffList.Type_u(var13, null, StaffList.Type_j.VANISH));
                  }
               }
            }
         }

         var1.sort(Comparator.comparing(StaffList.Type_u::name, String.CASE_INSENSITIVE_ORDER));
         return var1;
      } else {
         return var1;
      }
   }

   private static boolean dAsBoolean(PlayerListEntry var0, Set<String> var1) {
      if (var1.contains(var0.getProfile().name().toLowerCase(Locale.ROOT))) {
         return true;
      } else {
         Team var2 = var0.getScoreboardTeam();
         return var2 != null && LayerJob.cAsBoolean(var2.getPrefix().getString()) || LayerJob.cAsBoolean(aAsString(var0));
      }
   }

   private static String aAsString(PlayerListEntry var0) {
      Text var1 = var0.getDisplayName();
      if (var1 == null) {
         return "";
      } else {
         String var2 = var1.getString();
         int var3 = var2.toLowerCase(Locale.ROOT).indexOf(var0.getProfile().name().toLowerCase(Locale.ROOT));
         return var3 < 0 ? "" : var2.substring(0, var3);
      }
   }

   @Override
   public void onEnable() {
      this.Z.BAsVoid();
      this.k = 0;
   }

   @Override
   public void onDisable() {
      this.Z.BAsVoid();
      this.k = 0;
   }

   private void FAsVoid(List<StaffList.Type_u> var1) {
      MinecraftClient var2 = MinecraftClient.getInstance();
      if (var2.getNetworkHandler() != null && var2.world != null) {
         LinkedHashSet<String> var3 = new LinkedHashSet<>();

         for (Team var5 : var2.world.getScoreboard().getTeams()) {
            if (!var5.getPlayerList().isEmpty()) {
               var3.add(var5.getPrefix().getString().strip());
            }
         }

         for (PlayerListEntry var8 : var2.getNetworkHandler().getPlayerList()) {
            var3.add(aAsString(var8).strip());
         }

         var3.removeIf(String::isEmpty);
         List var7 = var3.stream().limit(16L).map(var0 -> (LayerJob.cAsBoolean(var0) ? "+" : "-") + "'" + var0 + "'").toList();
         ClientLog.GAsVoid(
            "StaffList",
            "Detected {} staff ({} in Vanish) among {} players with {} Staff Names; rank labels seen ({} total, + counts as staff): {}",
            var1.size(),
            var1.stream().filter(StaffList.Type_u::vanished).count(),
            var2.getNetworkHandler().getPlayerList().size(),
            zAsSet().size(),
            var3.size(),
            var7
         );
      }
   }

   @Override
   public void onTick() {
      List var1 = kAsList();
      if (++this.k == 40) {
         this.FAsVoid(var1);
      }

      LinkedHashMap var2 = new LinkedHashMap();

      for (StaffList.Type_u var4 : (Iterable<StaffList.Type_u>) (var1)) {
         var2.put(var4.name(), Boolean.valueOf(var4.vanished()));
      }

      StaffList.Type_a.Type_b var5 = this.Z.jAsStaffList_Type_a_Type_b(var2);
      if (this.alerts.getValue()) {
         uAsVoid("Staff online", var5.online(), NotificationHud.Type_u.WARNING, 5000L);
         uAsVoid("Staff joined", var5.joined(), NotificationHud.Type_u.WARNING, 5000L);
         uAsVoid("Staff left", var5.left(), NotificationHud.Type_u.INFO, 4000L);
         uAsVoid("Staff in Vanish", var5.vanished(), NotificationHud.Type_u.DANGER, 6000L);
      }
   }

   private static void uAsVoid(String var0, List<String> var1, NotificationHud.Type_u var2, long var3) {
      if (!var1.isEmpty()) {
         String var5 = var1.size() <= 3 ? String.join(", ", var1) : String.join(", ", var1.subList(0, 3)) + " +" + (var1.size() - 3);
         ClientLog.GAsVoid("StaffList", "{}: {}", var0, String.join(", ", var1));
         NotificationHud.GAsW8().RAsVoid(var0, var5, var2, var3);
      }
   }

   public static final class Type_a {
      public static final int M = 40;
      public static final int E = 40;
      private final Map<String, Boolean> t = new LinkedHashMap<>();
      private final Map<String, Integer> R = new HashMap<>();
      private int P;

      public Type_a() {
      }

      public void BAsVoid() {
         this.t.clear();
         this.R.clear();
         this.P = 0;
      }

      public StaffList.Type_a.Type_b jAsStaffList_Type_a_Type_b(Map<String, Boolean> var1) {
         ArrayList var2 = new ArrayList();
         ArrayList var3 = new ArrayList();
         ArrayList var4 = new ArrayList();
         ArrayList var5 = new ArrayList();
         if (this.P < 40) {
            this.t.clear();
            this.t.putAll(var1);
            this.R.clear();
            if (++this.P == 40) {
               var1.forEach((var2xx, var3x) -> (var3x ? var5 : var2).add(var2xx));
            }

            return new StaffList.Type_a.Type_b(var2, var3, var4, var5);
         } else {
            for (String var7 : List.copyOf(this.t.keySet())) {
               if (!var1.containsKey(var7)) {
                  this.t.remove(var7);
                  var4.add(var7);
               }
            }

            this.R.keySet().retainAll(var1.keySet());

            for (Entry var12 : var1.entrySet()) {
               String var8 = (String)var12.getKey();
               boolean var9 = (Boolean)var12.getValue();
               Boolean var10 = this.t.get(var8);
               if (var10 == null && !var9) {
                  this.R.remove(var8);
                  this.t.put(var8, false);
                  var3.add(var8);
               } else if (var10 != null && var10 == var9) {
                  this.R.remove(var8);
               } else if (this.R.merge(var8, 1, Integer::sum) >= 40) {
                  this.R.remove(var8);
                  this.t.put(var8, var9);
                  if (var9) {
                     var5.add(var8);
                  }
               }
            }

            return new StaffList.Type_a.Type_b(var2, var3, var4, var5);
         }
      }

      public static record Type_b(List<String> online, List<String> joined, List<String> left, List<String> vanished) {
      }
   }

   public static enum Type_j {
      ONLINE("Online", -10551483),
      NEAR("Near", -15821),
      SPECTATOR("Spectator", -44462),
      VANISH("Vanish", -44462);

      public final String x;
      public final int i;

      private Type_j(String var3, int var4) {
         this.x = var3;
         this.i = var4;
      }
   }

   private static record Type_o(String raw, Set<String> lowerCase) {
   }

   public static record Type_u(String name, PlayerListEntry info, StaffList.Type_j status) {
      public boolean vanished() {
         return this.status == StaffList.Type_j.VANISH;
      }
   }
}
