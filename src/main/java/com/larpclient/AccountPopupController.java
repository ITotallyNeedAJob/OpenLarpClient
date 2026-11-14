package com.larpclient;

import com.larpclient.account.AccountVault;
import com.larpclient.account.MicrosoftAccounts;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.session.Session;
import net.minecraft.text.OrderedText;

public final class AccountPopupController {
   static final UUID D = new UUID(0L, 0L);
   private static final int o = -2;
   private static final int L = 20;
   private final Screen O;
   private final MicrosoftAccounts g = MicrosoftAccounts.INSTANCE;
   private final WeatherRenderer S = new WeatherRenderer(0.0);
   private final TextInputWidget k;
   private final TextInputWidget j;
   private final TextInputWidget Z;
   private final TextInputWidget P;
   private MenuLayout.Type_c r = new MenuLayout.Type_c(8, 8, 120, 22);
   private AccountPopupController.Type_s W = AccountPopupController.Type_s.ACCOUNTS;
   private ProxyValidation.Type_j E = ProxyValidation.Type_j.SOCKS5;
   private boolean u;
   private boolean T;
   private boolean v;
   private int a;
   private int B;
   private int U;
   private int A;
   private int f;
   private int b = Integer.MIN_VALUE;
   private int c;
   private String N = "";
   private UUID q;
   private Session l;

   public AccountPopupController(Screen var1) {
      this.O = var1;
      this.k = new TextInputWidget("Paste access token", var1x -> this.N = "", 8192).OAsQq().MAsQq(this::AAsVoid);
      this.j = new TextInputWidget("Proxy host:port", this::lAsVoid, 300).MAsQq(this::TAsVoid);
      this.Z = new TextInputWidget("Username (optional)", var1x -> this.N = "", 255).MAsQq(this::TAsVoid);
      this.P = new TextInputWidget("Password (optional)", var1x -> this.N = "", 255).OAsQq().MAsQq(this::TAsVoid);
   }

   public boolean yAsBoolean() {
      return this.u;
   }

   public void fAsVoid(MenuLayout.Type_c var1) {
      this.r = var1;
   }

   public void jAsVoid() {
      if (this.u) {
         this.mAsVoid();
      } else {
         this.u = true;
         this.v = false;
         this.q = null;
         this.T = false;
         this.B = this.U = 0;
         this.W = AccountPopupController.Type_s.ACCOUNTS;
         this.lAsVoid();
         this.l = MinecraftClient.getInstance().getSession();
         this.S.aAsVoid(0.0);
         this.g.open(this.O);
      }
   }

   public void mAsVoid() {
      if (this.u) {
         this.g.cancel(this.O);
      }

      this.u = false;
      this.v = false;
      this.q = null;
      this.W = AccountPopupController.Type_s.ACCOUNTS;
      this.lAsVoid();
   }

   public static List<AccountPopupController.Type_y> VAsList(Session var0, Session var1, List<AccountVault.Entry> var2, boolean var3) {
      LinkedHashMap var4 = new LinkedHashMap();
      AccountVault.Entry var5 = var2.stream().filter(var1x -> var1x.id().equals(var0.getUuidOrNull())).findFirst().orElse(null);
      var4.put(
         var0.getUuidOrNull(),
         new AccountPopupController.Type_y(var0.getUuidOrNull(), var0.getUsername(), true, var5 != null, false, var5 == null ? "" : JAsString(var5))
      );

      for (AccountVault.Entry var7 : var2) {
         var4.putIfAbsent(var7.id(), new AccountPopupController.Type_y(var7.id(), var7.name(), false, true, false, JAsString(var7)));
      }

      if (var3 && var1 != null) {
         var4.putIfAbsent(var1.getUuidOrNull(), new AccountPopupController.Type_y(var1.getUuidOrNull(), var1.getUsername(), false, false, true, ""));
      }

      return List.copyOf(var4.values());
   }

   public static List<AccountPopupController.Type_y> OAsList(List<ProxyValidation> var0, UUID var1) {
      ArrayList var2 = new ArrayList();
      var2.add(new AccountPopupController.Type_y(D, "No proxy", var1 == null, false, false, "Direct"));

      for (ProxyValidation var4 : var0) {
         var2.add(new AccountPopupController.Type_y(var4.id(), var4.label(), var4.id().equals(var1), true, false, var4.authenticated() ? "Login" : ""));
      }

      return List.copyOf(var2);
   }

   private static String JAsString(AccountVault.Entry var0) {
      if (!var0.token()) {
         return "";
      } else {
         Instant var1 = var0.tokenExpiry();
         return var1 != null && var1.isBefore(Instant.now()) ? "Expired" : "Token";
      }
   }

   private List<AccountPopupController.Type_y> IAsList() {
      if (this.W == AccountPopupController.Type_s.PROXIES) {
         return OAsList(this.g.proxies(), this.g.activeProxyId());
      } else {
         MinecraftClient var1 = MinecraftClient.getInstance();
         return VAsList(var1.getSession(), this.g.launcher(), this.g.entries(), this.g.canReturn(var1));
      }
   }

   private AccountPopupController.Type_m BAsQ2_Type_m() {
      if (this.v) {
         return AccountPopupController.Type_m.RESET;
      } else if (this.g.busy()) {
         return this.g.code().isBlank() ? AccountPopupController.Type_m.BUSY : AccountPopupController.Type_m.CODE;
      } else if (!this.g.unlocked()) {
         return AccountPopupController.Type_m.UNAVAILABLE;
      } else {
         return switch (this.W) {
            case ACCOUNTS -> AccountPopupController.Type_m.LIST;
            case TOKEN -> AccountPopupController.Type_m.TOKEN;
            case PROXIES -> AccountPopupController.Type_m.PROXIES;
            case PROXY_FORM -> AccountPopupController.Type_m.PROXY_FORM;
         };
      }
   }

   private static boolean JAsBoolean(AccountPopupController.Type_m var0) {
      return var0 == AccountPopupController.Type_m.LIST || var0 == AccountPopupController.Type_m.PROXIES;
   }

   private static boolean TAsBoolean(AccountPopupController.Type_m var0) {
      return var0 == AccountPopupController.Type_m.TOKEN || var0 == AccountPopupController.Type_m.PROXY_FORM;
   }

   public static AccountPopupController.Type_w CAsQ2_Type_w(
      int var0, int var1, MenuLayout.Type_c var2, int var3, int var4, AccountPopupController.Type_m var5, boolean var6
   ) {
      int var7 = Math.max(1, Math.min(280, var0 - 16));
      int var8 = JAsBoolean(var5) && var6 ? 28 : 0;

      int var9 = switch (var5) {
         case LIST, PROXIES -> Math.min(6, Math.max(1, var3)) * 32 + 46 + var8;
         case BUSY -> 94;
         case CODE -> 166;
         case UNAVAILABLE -> 150;
         case RESET -> 124;
         case TOKEN -> 158;
         case PROXY_FORM -> 200;
      };
      int var10 = Math.clamp((long)(var2.x() + var2.width() / 2 - var7 / 2), 8, Math.max(8, var0 - var7 - 8));
      int var11 = var2.y() + var2.height() + 4;
      int var12 = JAsBoolean(var5) ? 78 + var8 : var9;
      int var13 = var1 - 8 - var11;
      int var14 = var2.y() - 12;
      int var15 = var13 < var12 && var14 > var13 ? 1 : 0;
      int var16 = var15 != 0 ? var14 : var13;
      int var17 = Math.max(1, Math.min(Math.min(var9, Math.max(var12, var16)), var1 - 16));
      int var18 = var15 != 0 ? var2.y() - var17 - 4 : var11;
      int var19 = Math.clamp((long)var18, 8, Math.max(8, var1 - var17 - 8));
      int var20 = Math.max(1, (var17 - 46 - var8) / 32);
      return new AccountPopupController.Type_w(
         new MenuLayout.Type_c(var10, var19, var7, var17),
         new MenuLayout.Type_c(var10 + 6, var19 + var17 - 34, var7 - 12, 28),
         var20,
         Math.clamp((long)var4, 0, Math.max(0, var3 - var20)),
         var8
      );
   }

   public static MenuLayout.Type_c gAsD8_Type_c(AccountPopupController.Type_w var0, AccountPopupController.Type_m var1, int var2) {
      MenuLayout.Type_c var3 = var0.panel();
      int var4 = var1 == AccountPopupController.Type_m.PROXY_FORM ? 4 : 1;
      int var5 = var3.y() + (var1 == AccountPopupController.Type_m.TOKEN ? 50 : 26);
      int var6 = var0.footer().y() - 4 - 20;
      int var7 = Math.max(12, Math.min(26, (var6 - var5) / var4));
      return new MenuLayout.Type_c(var3.x() + 10, var5 + var2 * var7, var3.width() - 20, Math.max(10, Math.min(20, var7 - 4)));
   }

   private AccountPopupController.Type_w hAsQ2_Type_w(AccountPopupController.Type_m var1, int var2) {
      return CAsQ2_Type_w(this.O.width, this.O.height, this.r, var2, this.a, var1, !this.eAsString(var1).isBlank());
   }

   private static boolean WAsBoolean(MenuLayout.Type_c var0, double var1, double var3) {
      return var1 >= (double)var0.x() && var3 >= (double)var0.y() && var1 < (double)(var0.x() + var0.width()) && var3 < (double)(var0.y() + var0.height());
   }

   private String eAsString(AccountPopupController.Type_m var1) {
      if (TAsBoolean(var1)) {
         return !this.N.isEmpty() ? this.N : (this.g.error() ? this.g.status() : "");
      } else {
         return var1 == AccountPopupController.Type_m.PROXIES && this.g.status().isBlank() && ProxyRoute.jAsString() != null
            ? ProxyRoute.jAsString()
            : this.g.status();
      }
   }

   private boolean pAsBoolean(AccountPopupController.Type_m var1) {
      if (TAsBoolean(var1)) {
         return true;
      } else {
         return var1 == AccountPopupController.Type_m.PROXIES && this.g.status().isBlank() ? ProxyRoute.jAsString() != null : this.g.error();
      }
   }

   private static boolean BAsBoolean() {
      return ProxyRoute.fAsPy_Type_y() != null || ProxyRoute.jAsString() != null;
   }

   private static String zAsString() {
      return ProxyRoute.fAsPy_Type_y() != null ? "Proxy: On" : (ProxyRoute.jAsString() != null ? "Proxy: Paused" : "Proxy: Off");
   }

   private int EAsInt(AccountPopupController.Type_w var1, int var2, int var3, int var4) {
      for (int var5 = var1.scroll(); var5 < Math.min(var2, var1.scroll() + var1.capacity()); var5++) {
         if (WAsBoolean(var1.row(var5), (double)var3, (double)var4)) {
            return var5;
         }
      }

      return -1;
   }

   private static List<MenuLayout.Type_c> eAsList(MenuLayout.Type_c var0, int var1) {
      int var2 = (var0.width() - 4 * (var1 - 1)) / var1;
      ArrayList var3 = new ArrayList();

      for (int var4 = 0; var4 < var1; var4++) {
         var3.add(
            new MenuLayout.Type_c(
               var0.x() + var4 * (var2 + 4), var0.y(), var4 == var1 - 1 ? var0.x() + var0.width() - (var0.x() + var4 * (var2 + 4)) : var2, var0.height()
            )
         );
      }

      return var3;
   }

   private List<AccountPopupController.Type_e> CAsList(
      AccountPopupController.Type_w var1, AccountPopupController.Type_m var2, List<AccountPopupController.Type_y> var3, int var4
   ) {
      ArrayList var5 = new ArrayList();
      MenuLayout.Type_c var6 = var1.footer();
      MenuLayout.Type_c var7 = new MenuLayout.Type_c(var6.x(), var6.y() - 30, var6.width(), 24);
      switch (var2) {
         case LIST:
         case PROXIES:
            int var17 = var2 == AccountPopupController.Type_m.PROXIES ? 1 : 0;
            if (var4 >= var1.scroll() && var4 < Math.min(var3.size(), var1.scroll() + var1.capacity())) {
               AccountPopupController.Type_y var20 = var3.get(var4);
               MenuLayout.Type_c var23 = var1.row(var4);
               int var24 = var23.x() + var23.width() - 4;
               if (var20.id().equals(this.q)) {
                  var5.add(new AccountPopupController.Type_e(new MenuLayout.Type_c(var24 - 124, var23.y() + 4, 64, 24), "Remove?", () -> {
                     this.q = null;
                     if (var17 != 0) {
                        this.g.removeProxy(this.O, var20.id());
                     } else {
                        this.g.remove(this.O, var20.id());
                     }
                  }, true, false, var4));
                  var5.add(
                     new AccountPopupController.Type_e(
                        new MenuLayout.Type_c(var24 - 56, var23.y() + 4, 56, 24), "Cancel", () -> this.q = null, false, false, var4
                     )
                  );
               } else {
                  if (!var20.current()) {
                     var5.add(
                        new AccountPopupController.Type_e(
                           new MenuLayout.Type_c(var24 - (var20.saved() ? 124 : 56), var23.y() + 4, 56, 24), var17 != 0 ? "Use" : "Switch", () -> {
                              if (var17 != 0) {
                                 if (var20.id().equals(D)) {
                                    this.g.proxyOff(this.O);
                                 } else {
                                    this.g.useProxy(this.O, var20.id());
                                 }
                              } else if (var20.launcher()) {
                                 this.g.returnToLauncher(this.O);
                              } else {
                                 this.g.switchTo(this.O, var20.id());
                              }
                           }, false, false, var4
                        )
                     );
                  }

                  if (var20.saved()) {
                     var5.add(
                        new AccountPopupController.Type_e(
                           new MenuLayout.Type_c(var24 - 64, var23.y() + 4, 64, 24), "Remove", () -> this.q = var20.id(), true, false, var4
                        )
                     );
                  }
               }
            }

            if (var17 != 0) {
               List var21 = eAsList(var6, 2);
               var5.add(
                  new AccountPopupController.Type_e(
                     (MenuLayout.Type_c)var21.get(0), "+ Add proxy", () -> this.bAsVoid(AccountPopupController.Type_s.PROXY_FORM), false, false, var3.size()
                  )
               );
               var5.add(
                  new AccountPopupController.Type_e(
                     (MenuLayout.Type_c)var21.get(1), "Back", () -> this.bAsVoid(AccountPopupController.Type_s.ACCOUNTS), false, false, var3.size()
                  )
               );
            } else {
               List var22 = eAsList(var6, 3);
               var5.add(new AccountPopupController.Type_e((MenuLayout.Type_c)var22.get(0), "+ Microsoft", () -> {
                  this.q = null;
                  this.g.add(this.O);
               }, false, false, var3.size()));
               var5.add(
                  new AccountPopupController.Type_e(
                     (MenuLayout.Type_c)var22.get(1), "+ Token", () -> this.bAsVoid(AccountPopupController.Type_s.TOKEN), false, false, var3.size()
                  )
               );
               var5.add(
                  new AccountPopupController.Type_e(
                     (MenuLayout.Type_c)var22.get(2), zAsString(), () -> this.bAsVoid(AccountPopupController.Type_s.PROXIES), false, BAsBoolean(), var3.size()
                  )
               );
            }
            break;
         case BUSY:
            var5.add(new AccountPopupController.Type_e(var6, "Cancel", () -> this.g.cancel(this.O), false, false, 0));
            break;
         case CODE:
            List var16 = eAsList(var7, 2);
            var5.add(new AccountPopupController.Type_e((MenuLayout.Type_c)var16.get(0), "Open Microsoft", this.g::openBrowser, false, false, 0));
            var5.add(new AccountPopupController.Type_e((MenuLayout.Type_c)var16.get(1), "Copy code", this.g::copyCode, false, false, 0));
            var5.add(new AccountPopupController.Type_e(var6, "Cancel", () -> this.g.cancel(this.O), false, false, 0));
            break;
         case UNAVAILABLE:
            List var15 = eAsList(var7, 2);
            var5.add(new AccountPopupController.Type_e((MenuLayout.Type_c)var15.get(0), "Retry", () -> this.g.open(this.O), false, false, 0));
            if (this.g.exists()) {
               var5.add(new AccountPopupController.Type_e((MenuLayout.Type_c)var15.get(1), "Start new list", () -> this.v = true, false, false, 0));
            }

            if (BAsBoolean()) {
               List var19 = eAsList(var6, 2);
               var5.add(new AccountPopupController.Type_e((MenuLayout.Type_c)var19.get(0), "Proxy off", () -> this.g.proxyOff(this.O), false, false, 0));
               var5.add(new AccountPopupController.Type_e((MenuLayout.Type_c)var19.get(1), "Close", this::mAsVoid, false, false, 0));
            } else {
               var5.add(new AccountPopupController.Type_e(var6, "Close", this::mAsVoid, false, false, 0));
            }
            break;
         case RESET:
            List var14 = eAsList(var6, 2);
            var5.add(new AccountPopupController.Type_e((MenuLayout.Type_c)var14.get(0), "Start new list", () -> {
               this.v = false;
               this.g.archive(this.O);
            }, true, false, 0));
            var5.add(new AccountPopupController.Type_e((MenuLayout.Type_c)var14.get(1), "Cancel", () -> this.v = false, false, false, 0));
            break;
         case TOKEN:
            List var13 = eAsList(var6, 3);
            var5.add(new AccountPopupController.Type_e((MenuLayout.Type_c)var13.get(0), "Paste", this::XAsVoid, false, false, 0));
            var5.add(new AccountPopupController.Type_e((MenuLayout.Type_c)var13.get(1), "Sign in", this::AAsVoid, false, false, 0));
            var5.add(
               new AccountPopupController.Type_e(
                  (MenuLayout.Type_c)var13.get(2), "Cancel", () -> this.bAsVoid(AccountPopupController.Type_s.ACCOUNTS), false, false, 0
               )
            );
            break;
         case PROXY_FORM:
            List var8 = eAsList(gAsD8_Type_c(var1, var2, 0), 3);

            for (ProxyValidation.Type_j var12 : ProxyValidation.Type_j.values()) {
               var5.add(new AccountPopupController.Type_e((MenuLayout.Type_c)var8.get(var12.ordinal()), var12.I, () -> {
                  this.E = var12;
                  this.N = "";
               }, false, var12 == this.E, -2));
            }

            List var18 = eAsList(var6, 2);
            var5.add(new AccountPopupController.Type_e((MenuLayout.Type_c)var18.get(0), "Save and use", this::TAsVoid, false, false, 0));
            var5.add(
               new AccountPopupController.Type_e(
                  (MenuLayout.Type_c)var18.get(1), "Cancel", () -> this.bAsVoid(AccountPopupController.Type_s.PROXIES), false, false, 0
               )
            );
      }

      return var5;
   }

   private List<TextInputWidget> JAsList(AccountPopupController.Type_m var1) {
      return var1 == AccountPopupController.Type_m.TOKEN
         ? List.of(this.k)
         : (var1 == AccountPopupController.Type_m.PROXY_FORM ? List.of(this.j, this.Z, this.P) : List.of());
   }

   private List<Object> lAsList(AccountPopupController.Type_m var1, List<AccountPopupController.Type_e> var2) {
      ArrayList var3 = new ArrayList();

      for (AccountPopupController.Type_e var5 : var2) {
         if (var5.line() == -2) {
            var3.add(var5);
         }
      }

      var3.addAll(this.JAsList(var1));

      for (AccountPopupController.Type_e var7 : var2) {
         if (var7.line() != -2) {
            var3.add(var7);
         }
      }

      return var3;
   }

   private void lAsVoid(AccountPopupController.Type_w var1, AccountPopupController.Type_m var2) {
      List var3 = this.JAsList(var2);

      for (int var4 = 0; var4 < var3.size(); var4++) {
         MenuLayout.Type_c var5 = gAsD8_Type_c(var1, var2, var2 == AccountPopupController.Type_m.PROXY_FORM ? var4 + 1 : var4);
         ((TextInputWidget)var3.get(var4)).bounds(var5.x(), var5.y(), var5.width());
         ((TextInputWidget)var3.get(var4)).F = var5.height();
      }
   }

   private void zAsVoid(List<Object> var1) {
      for (TextInputWidget var3 : List.of(this.k, this.j, this.Z, this.P)) {
         var3.y = this.A >= 0 && this.A < var1.size() && var1.get(this.A) == var3;
      }
   }

   private void bAsVoid(AccountPopupController.Type_s var1) {
      this.W = var1;
      this.q = null;
      this.a = 0;
      this.B = 0;
      this.U = 0;
      this.N = "";
      this.g.clearMessage();
      if (var1 == AccountPopupController.Type_s.ACCOUNTS || var1 == AccountPopupController.Type_s.PROXIES) {
         this.lAsVoid();
      }

      AccountPopupController.Type_m var2 = this.BAsQ2_Type_m();
      if (TAsBoolean(var2)) {
         List var3 = this.lAsList(var2, this.CAsList(this.hAsQ2_Type_w(var2, 0), var2, List.of(), -1));
         this.A = var3.indexOf(this.JAsList(var2).getFirst());
         this.zAsVoid(var3);
      }
   }

   private void lAsVoid() {
      for (TextInputWidget var2 : List.of(this.k, this.j, this.Z, this.P)) {
         var2.W = "";
         var2.hAsVoid();
      }

      this.f = 0;
      this.A = 0;
      this.N = "";
      this.E = ProxyValidation.Type_j.SOCKS5;
   }

   private void AAsVoid() {
      if (this.BAsQ2_Type_m() == AccountPopupController.Type_m.TOKEN) {
         this.N = "";
         this.g.addToken(this.O, this.k.W, () -> {
            this.k.W = "";
            this.bAsVoid(AccountPopupController.Type_s.ACCOUNTS);
         });
      }
   }

   private void XAsVoid() {
      String var1 = MinecraftClient.getInstance().keyboard.getClipboard();
      this.k.W = "";
      this.k.y = true;
      this.k.HAsVoid(var1);
      AccountPopupController.Type_m var2 = this.BAsQ2_Type_m();
      List var3 = this.lAsList(var2, this.CAsList(this.hAsQ2_Type_w(var2, 0), var2, List.of(), -1));
      this.A = var3.indexOf(this.k);
      this.zAsVoid(var3);
   }

   private void TAsVoid() {
      if (this.BAsQ2_Type_m() == AccountPopupController.Type_m.PROXY_FORM) {
         ProxyValidation var1;
         try {
            var1 = ProxyValidation.parse(this.E, this.j.W, this.Z.W, this.P.W);
         } catch (IllegalArgumentException var3) {
            this.N = var3.getMessage();
            return;
         }

         this.N = "";
         this.g.saveProxy(this.O, var1, () -> this.bAsVoid(AccountPopupController.Type_s.PROXIES));
      }
   }

   private void lAsVoid(String var1) {
      this.N = "";
      int var2 = this.f;
      this.f = var1.length();
      if (var1.length() - var2 > 1) {
         ProxyValidation.Type_i var3 = ProxyValidation.split(var1, this.E);
         if (var3 != null) {
            this.E = var3.type();
            this.j.W = var3.address();
            this.f = this.j.W.length();
            if (!var3.username().isEmpty() || !var3.password().isEmpty()) {
               this.Z.W = var3.username();
               this.P.W = var3.password();
            }
         }
      }
   }

   public void WAsVoid(DrawContext var1, int var2, int var3) {
      if (this.u) {
         if (!this.g.busy() && MinecraftClient.getInstance().getSession() != this.l) {
            this.mAsVoid();
         } else {
            AccountPopupController.Type_m var4 = this.BAsQ2_Type_m();
            List var5 = this.IAsList();
            AccountPopupController.Type_w var6 = this.hAsQ2_Type_w(var4, var5.size());
            this.a = var6.scroll();
            if (var2 != this.b || var3 != this.c) {
               this.T = false;
               this.b = var2;
               this.c = var3;
            }

            boolean var7 = JAsBoolean(var4);
            int var8 = !var7 ? -1 : (this.T ? this.B : this.EAsInt(var6, var5.size(), var2, var3));
            if (var7 && this.q != null) {
               for (int var9 = 0; var9 < var5.size(); var9++) {
                  if (((AccountPopupController.Type_y)var5.get(var9)).id().equals(this.q)) {
                     var8 = var9;
                  }
               }
            }

            List<AccountPopupController.Type_e> var34 = this.CAsList(var6, var4, var5, var8);
            List var10 = TAsBoolean(var4) ? this.lAsList(var4, var34) : List.of();
            boolean var11 = LarpClient.CAsLarpClient().fAsBo().yAsN7().Z;
            double var12 = var11 ? this.S.bAsDouble(1.0, 140) : 1.0;
            MenuLayout.Type_c var14 = var6.panel();
            String var15 = this.eAsString(var4);
            var1.createNewRootLayer();
            GuiDrawing.CAsVoid(var1, var14.x(), var14.y(), var14.width(), var14.height(), 5, ColorUtil.pAsInt(-99347436, var12));
            GuiDrawing.wAsVoid(var1, var14.x(), var14.y(), var14.width(), var14.height(), 5, ColorUtil.pAsInt(-13290187, var12));
            var1.enableScissor(var14.x(), var14.y(), var14.x() + var14.width(), var14.y() + var14.height());

            try {
               if (var7) {
                  for (int var35 = this.a; var35 < Math.min(var5.size(), this.a + var6.capacity()); var35++) {
                     AccountPopupController.Type_y var38 = (AccountPopupController.Type_y)var5.get(var35);
                     MenuLayout.Type_c var41 = var6.row(var35);
                     int var19 = var35 == var8 ? 1 : 0;
                     int var20 = var35;
                     int var21 = var19 != 0 && var34.stream().anyMatch(var1x -> var1x.line() == var20) ? 1 : 0;
                     if (var19 != 0) {
                        GuiDrawing.CAsVoid(var1, var41.x(), var41.y(), var41.width(), var41.height(), 3, ColorUtil.pAsInt(-14342875, var12));
                     }

                     String var22 = var38.current() ? (var4 == AccountPopupController.Type_m.PROXIES ? "Active" : "Current") : var38.note();
                     int var23 = !var22.isEmpty() && var21 == 0 ? TextDrawing.nAsInt(var22) + 18 : 14;
                     if (var21 != 0) {
                        for (AccountPopupController.Type_e var25 : var34) {
                           if (var25.line() == var35) {
                              var23 = Math.max(var23, var41.x() + var41.width() - var25.box().x() + 8);
                           }
                        }
                     }

                     TextDrawing.oAsVoid(
                        var1,
                        TextDrawing.LAsString(var38.name(), var41.width() - var23),
                        var41.x() + 8,
                        var41.y() + 11,
                        ColorUtil.pAsInt(var38.current() ? UiStyle.u : UiStyle.e, var12),
                        var38.current() ? TextDrawing.Type_x.MEDIUM : TextDrawing.Type_x.REGULAR
                     );
                     if (!var22.isEmpty() && var21 == 0) {
                        TextDrawing.CAsVoid(
                           var1,
                           var22,
                           var41.x() + var41.width() - 8 - TextDrawing.nAsInt(var22),
                           var41.y() + 11,
                           ColorUtil.pAsInt("Expired".equals(var22) ? -24915 : UiStyle.W, var12)
                        );
                     }
                  }

                  if (var5.size() > var6.capacity()) {
                     int var36 = var6.capacity() * 32;
                     int var39 = Math.max(8, var36 * var6.capacity() / var5.size());
                     int var42 = (var36 - var39) * this.a / (var5.size() - var6.capacity());
                     GuiTextureDrawing.CAsVoid(var1, var14.x() + var14.width() - 3, var14.y() + 6 + var42, 1, var39, ColorUtil.pAsInt(UiStyle.e, var12));
                  }

                  if (var6.statusHeight() > 0) {
                     ZAsVoid(
                        var1,
                        var15,
                        var14.x() + 10,
                        var6.footer().y() - var6.statusHeight() + 1,
                        var14.width() - 20,
                        2,
                        this.pAsBoolean(var4) ? -24915 : UiStyle.W,
                        var12
                     );
                  }

                  GuiTextureDrawing.CAsVoid(var1, var14.x() + 6, var6.footer().y() - 3, var14.width() - 12, 1, ColorUtil.pAsInt(-13619152, var12));
               } else if (!TAsBoolean(var4)) {
                  if (var4 == AccountPopupController.Type_m.CODE) {
                     ZAsVoid(var1, "Microsoft sign-in", var14.x() + 10, var14.y() + 12, var14.width() - 20, 1, UiStyle.u, var12);
                     ZAsVoid(var1, "microsoft.com/link", var14.x() + 10, var14.y() + 30, var14.width() - 20, 1, UiStyle.e, var12);
                     TextDrawing.uAsVoid(var1, this.g.code(), var14.x() + var14.width() / 2, var14.y() + 49, ColorUtil.pAsInt(UiStyle.u, var12));
                     ZAsVoid(var1, "Approve only the code shown here.", var14.x() + 10, var14.y() + 69, var14.width() - 20, 2, UiStyle.e, var12);
                  } else if (var4 == AccountPopupController.Type_m.RESET) {
                     ZAsVoid(var1, "Start a new saved list?", var14.x() + 10, var14.y() + 12, var14.width() - 20, 1, UiStyle.u, var12);
                     ZAsVoid(
                        var1,
                        "The old encrypted file will be kept for recovery. Add your accounts again.",
                        var14.x() + 10,
                        var14.y() + 34,
                        var14.width() - 20,
                        3,
                        UiStyle.e,
                        var12
                     );
                  } else {
                     ZAsVoid(
                        var1,
                        this.g.status().isBlank() ? "Loading saved accounts..." : this.g.status(),
                        var14.x() + 10,
                        var14.y() + 12,
                        var14.width() - 20,
                        Math.max(1, (var6.footer().y() - var14.y() - (var4 == AccountPopupController.Type_m.UNAVAILABLE ? 46 : 18)) / 10),
                        this.g.error() ? -24915 : UiStyle.e,
                        var12
                     );
                  }
               } else {
                  this.lAsVoid(var6, var4);
                  int var16 = var4 == AccountPopupController.Type_m.TOKEN ? 1 : 0;
                  ZAsVoid(
                     var1,
                     var16 != 0 ? "Sign in with a token" : "Add a proxy for server traffic",
                     var14.x() + 10,
                     var14.y() + 10,
                     var14.width() - 20,
                     1,
                     UiStyle.u,
                     var12
                  );
                  if (var16 != 0) {
                     ZAsVoid(
                        var1,
                        "Paste a Minecraft access token. It is checked with Minecraft and saved encrypted until it expires.",
                        var14.x() + 10,
                        var14.y() + 24,
                        var14.width() - 20,
                        2,
                        UiStyle.e,
                        var12
                     );
                  }

                  this.zAsVoid(var10);

                  for (TextInputWidget var18 : this.JAsList(var4)) {
                     var18.render(var1, var2, var3);
                  }

                  if (!var15.isBlank()) {
                     ZAsVoid(var1, var15, var14.x() + 10, var6.footer().y() - 4 - 20, var14.width() - 20, 2, this.pAsBoolean(var4) ? -24915 : UiStyle.W, var12);
                  }

                  GuiTextureDrawing.CAsVoid(var1, var14.x() + 6, var6.footer().y() - 3, var14.width() - 12, 1, ColorUtil.pAsInt(-13619152, var12));
               }

               for (int var37 = 0; var37 < var34.size(); var37++) {
                  AccountPopupController.Type_e var40 = var34.get(var37);
                  MenuLayout.Type_c var43 = var40.box();
                  int var44 = this.T && this.FAsBoolean(var4, var34, var10, var37) ? 1 : 0;
                  int var45 = !this.T && WAsBoolean(var43, (double)var2, (double)var3) ? 1 : 0;
                  if (var40.selected() || var44 != 0 || var45 != 0) {
                     GuiDrawing.CAsVoid(
                        var1, var43.x(), var43.y(), var43.width(), var43.height(), 3, ColorUtil.pAsInt(var40.selected() ? -13882324 : -13421773, var12)
                     );
                  }

                  if (var44 != 0) {
                     GuiDrawing.wAsVoid(var1, var43.x(), var43.y(), var43.width(), var43.height(), 3, ColorUtil.pAsInt(UiStyle.e, var12));
                  }

                  int var46 = TextDrawing.nAsInt(var40.label());
                  float var47 = lAsFloat(var46, var43.width() - 12);
                  var1.getMatrices().pushMatrix();

                  try {
                     var1.getMatrices()
                        .translate(
                           (float)var43.x() + ((float)var43.width() - (float)var46 * var47) / 2.0F,
                           (float)var43.y() + ((float)var43.height() - 9.0F * var47) / 2.0F
                        );
                     var1.getMatrices().scale(var47, var47);
                     TextDrawing.CAsVoid(
                        var1,
                        var40.label(),
                        0,
                        0,
                        ColorUtil.pAsInt(var40.destructive() ? -24915 : (!var40.selected() && var45 == 0 && var44 == 0 ? UiStyle.e : UiStyle.u), var12)
                     );
                  } finally {
                     var1.getMatrices().popMatrix();
                  }
               }
            } finally {
               var1.disableScissor();
            }
         }
      }
   }

   private boolean FAsBoolean(AccountPopupController.Type_m var1, List<AccountPopupController.Type_e> var2, List<Object> var3, int var4) {
      if (!TAsBoolean(var1)) {
         if (!JAsBoolean(var1)) {
            return this.U == var4;
         } else {
            int var5 = var2.get(var4).line();
            int var6 = 0;
            if (var5 != this.B) {
               return false;
            } else {
               for (int var7 = 0; var7 < var4; var7++) {
                  if (var2.get(var7).line() == var5) {
                     var6++;
                  }
               }

               return var6 == this.U;
            }
         }
      } else {
         return this.A >= 0 && this.A < var3.size() && var3.get(this.A) == var2.get(var4);
      }
   }

   public static float lAsFloat(int var0, int var1) {
      return Math.min(1.0F, (float)Math.max(1, var1) / (float)Math.max(1, var0));
   }

   private static void ZAsVoid(DrawContext var0, String var1, int var2, int var3, int var4, int var5, int var6, double var7) {
      MinecraftClient var9 = MinecraftClient.getInstance();
      List var10 = var9.textRenderer.wrapLines(TextDrawing.ZAsText(var1, TextDrawing.Type_x.REGULAR), var4);

      for (int var11 = 0; var11 < Math.min(var5, var10.size()); var11++) {
         var0.drawText(var9.textRenderer, (OrderedText)var10.get(var11), var2, var3 + var11 * 10, ColorUtil.pAsInt(var6, var7), false);
      }
   }

   public boolean zAsBoolean(Click var1) {
      if (!this.u) {
         return false;
      } else {
         AccountPopupController.Type_m var2 = this.BAsQ2_Type_m();
         List var3 = this.IAsList();
         AccountPopupController.Type_w var4 = this.hAsQ2_Type_w(var2, var3.size());
         if (!WAsBoolean(var4.panel(), var1.x(), var1.y())) {
            this.mAsVoid();
            return true;
         } else if (var1.button() != 0) {
            return true;
         } else {
            this.T = false;
            boolean var5 = JAsBoolean(var2);
            int var6 = var5 ? this.EAsInt(var4, var3.size(), (int)var1.x(), (int)var1.y()) : -1;
            if (var5 && this.q != null) {
               for (int var7 = 0; var7 < var3.size(); var7++) {
                  if (((AccountPopupController.Type_y)var3.get(var7)).id().equals(this.q)) {
                     var6 = var7;
                  }
               }
            }

            List var11 = this.CAsList(var4, var2, var3, var6);

            for (AccountPopupController.Type_e var9 : (Iterable<AccountPopupController.Type_e>) (var11)) {
               if (WAsBoolean(var9.box(), var1.x(), var1.y())) {
                  var9.action().run();
                  return true;
               }
            }

            if (TAsBoolean(var2)) {
               this.lAsVoid(var4, var2);
               List var12 = this.lAsList(var2, var11);

               for (TextInputWidget var10 : this.JAsList(var2)) {
                  if (var10.contains(var1.x(), var1.y())) {
                     this.A = var12.indexOf(var10);
                  }
               }

               this.zAsVoid(var12);
            }

            if (var6 >= 0) {
               this.B = var6;
               this.U = 0;
            }

            return true;
         }
      }
   }

   public boolean KAsBoolean(CharInput var1) {
      if (!this.u) {
         return false;
      } else {
         for (TextInputWidget var3 : this.JAsList(this.BAsQ2_Type_m())) {
            if (var3.y) {
               var3.character(var1.codepoint());
            }
         }

         return true;
      }
   }

   public boolean dAsBoolean(double var1, double var3, double var5) {
      if (!this.u) {
         return false;
      } else {
         AccountPopupController.Type_m var7 = this.BAsQ2_Type_m();
         List var8 = this.IAsList();
         AccountPopupController.Type_w var9 = this.hAsQ2_Type_w(var7, var8.size());
         if (JAsBoolean(var7) && WAsBoolean(var9.panel(), var1, var3) && var5 != 0.0) {
            this.a = Math.clamp((long)(var9.scroll() + (var5 > 0.0 ? -1 : 1)), 0, Math.max(0, var8.size() - var9.capacity()));
            this.q = null;
         }

         return true;
      }
   }

   public boolean BAsBoolean(KeyInput var1) {
      if (!this.u) {
         return false;
      } else {
         int var2 = var1.key();
         AccountPopupController.Type_m var3 = this.BAsQ2_Type_m();
         if (var2 == 256) {
            if (this.v) {
               this.v = false;
            } else if (this.q != null) {
               this.q = null;
            } else if (var3 == AccountPopupController.Type_m.TOKEN || var3 == AccountPopupController.Type_m.PROXIES) {
               this.bAsVoid(AccountPopupController.Type_s.ACCOUNTS);
            } else if (var3 == AccountPopupController.Type_m.PROXY_FORM) {
               this.bAsVoid(AccountPopupController.Type_s.PROXIES);
            } else {
               this.mAsVoid();
            }

            return true;
         } else {
            this.T = true;
            List var4 = this.IAsList();
            AccountPopupController.Type_w var5 = this.hAsQ2_Type_w(var3, var4.size());
            int var6 = (var1.modifiers() & 1) != 0 ? 1 : 0;
            int var7 = var2 != 257 && var2 != 335 && var2 != 32 ? 0 : 1;
            if (JAsBoolean(var3)) {
               this.B = Math.clamp((long)this.B, 0, var4.size());
               int var14 = var4.size() + 1;
               int var15 = NAsInt(this.CAsList(var5, var3, var4, this.B), this.B);
               if (var2 == 264 || var2 == 265) {
                  this.B = Math.floorMod(this.B + (var2 == 265 ? -1 : 1), var14);
                  this.U = 0;
                  this.q = null;
               } else if (var2 == 258 && var6 == 0) {
                  if (this.U < var15 - 1) {
                     this.U++;
                  } else {
                     this.B = Math.floorMod(this.B + 1, var14);
                     this.U = 0;
                     this.q = null;
                  }
               } else if (var2 == 258) {
                  if (this.U > 0) {
                     this.U--;
                  } else {
                     this.B = Math.floorMod(this.B - 1, var14);
                     this.q = null;
                     this.U = Math.max(0, NAsInt(this.CAsList(var5, var3, var4, this.B), this.B) - 1);
                  }
               } else if (var2 == 268) {
                  this.B = 0;
                  this.U = 0;
               } else if (var2 == 269) {
                  this.B = var4.size();
                  this.U = 0;
               } else if (var2 == 263 || var2 == 262) {
                  this.U = Math.clamp((long)(this.U + (var2 == 262 ? 1 : -1)), 0, Math.max(0, var15 - 1));
               }

               if (this.B < var4.size()) {
                  this.a = Math.clamp(
                     this.B < this.a ? (long)this.B : (long)(this.B >= this.a + var5.capacity() ? this.B - var5.capacity() + 1 : this.a),
                     0,
                     Math.max(0, var4.size() - var5.capacity())
                  );
               }

               if (var7 != 0) {
                  var5 = this.hAsQ2_Type_w(var3, var4.size());
                  int var16 = 0;

                  for (AccountPopupController.Type_e var12 : (Iterable<AccountPopupController.Type_e>) (this.CAsList(var5, var3, var4, this.B))) {
                     if (var12.line() == this.B && var16++ == this.U) {
                        var12.action().run();
                        break;
                     }
                  }
               }

               return true;
            } else {
               List var8 = this.CAsList(var5, var3, var4, -1);
               if (TAsBoolean(var3)) {
                  List var9 = this.lAsList(var3, var8);
                  this.A = Math.clamp((long)this.A, 0, var9.size() - 1);
                  Object var10 = var9.get(this.A);
                  if (var2 == 258) {
                     this.A = Math.floorMod(this.A + (var6 != 0 ? -1 : 1), var9.size());
                     this.zAsVoid(var9);
                     return true;
                  } else if (var10 instanceof TextInputWidget var17) {
                     var17.y = true;
                     var17.key(var2);
                     return true;
                  } else {
                     if (var2 != 264 && var2 != 262 && var2 != 265 && var2 != 263) {
                        if (var7 != 0 && var10 instanceof AccountPopupController.Type_e var11) {
                           var11.action().run();
                        }
                     } else {
                        this.A = Math.floorMod(this.A + (var2 != 265 && var2 != 263 ? 1 : -1), var9.size());
                        this.zAsVoid(var9);
                     }

                     return true;
                  }
               } else {
                  if (var2 == 258 || var2 == 264 || var2 == 265) {
                     this.U = Math.floorMod(this.U + (var2 != 265 && var6 == 0 ? 1 : -1), Math.max(1, var8.size()));
                  }

                  if (var7 != 0 && this.U >= 0 && this.U < var8.size()) {
                     ((AccountPopupController.Type_e)var8.get(this.U)).action().run();
                  }

                  return true;
               }
            }
         }
      }
   }

   private static int NAsInt(List<AccountPopupController.Type_e> var0, int var1) {
      int var2 = 0;

      for (AccountPopupController.Type_e var4 : var0) {
         if (var4.line() == var1) {
            var2++;
         }
      }

      return var2;
   }

   private static record Type_e(MenuLayout.Type_c box, String label, Runnable action, boolean destructive, boolean selected, int line) {
   }

   public static enum Type_m {
      LIST,
      BUSY,
      CODE,
      UNAVAILABLE,
      RESET,
      TOKEN,
      PROXIES,
      PROXY_FORM;

      private Type_m() {
      }
   }

   private static enum Type_s {
      ACCOUNTS,
      TOKEN,
      PROXIES,
      PROXY_FORM;

      private Type_s() {
      }
   }

   public static record Type_w(MenuLayout.Type_c panel, MenuLayout.Type_c footer, int capacity, int scroll, int statusHeight) {
      public MenuLayout.Type_c row(int var1) {
         return new MenuLayout.Type_c(this.panel.x() + 6, this.panel.y() + 6 + (var1 - this.scroll) * 32, this.panel.width() - 12, 32);
      }
   }

   public static record Type_y(UUID id, String name, boolean current, boolean saved, boolean launcher, String note) {
   }
}
