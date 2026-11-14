package com.larpclient;

import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

final class BulletTracer {
   private final ProfileSettingsPanel E;
   String k = "";
   int y;
   int F;
   long c;
   boolean n;
   List<JsonObject> p = List.of();
   JsonObject s;

   BulletTracer(ProfileSettingsPanel var1) {
      this.E = var1;
   }

   void bAsVoid() {
      this.s = null;
   }

   void vAsVoid() {
      String var1 = this.k.toLowerCase(Locale.ROOT);

      for (JsonObject var3 : this.p) {
         String var4 = ProfileCodec.MAsString(var3, "name", 32);
         String var5 = ProfileCodec.MAsString(var3, "description", 4000);
         if ((var4 + " " + var5 + " " + ProfileCodec.MAsString(var3, "author", 160)).toLowerCase(Locale.ROOT).contains(var1)) {
            this.E.FAsVoid(var4, ProfileCodec.MAsString(var3, "author", 160), this.s == var3, () -> {
               this.s = var3;
               this.E.s = 0;
               this.E.build();
            });
         }
      }
   }

   void hAsVoid() {
      if (this.s != null) {
         String var1 = ProfileCodec.MAsString(this.s, "author", 160);
         this.E.QAsVoid(ProfileCodec.MAsString(this.s, "name", 32), var1.isBlank() ? "" : "By " + var1);
         String var2 = ProfileCodec.MAsString(this.s, "description", 4000);
         if (!var2.isBlank()) {
            this.E.text(var2);
         }

         ArrayList var3 = new ArrayList();
         String var4 = ProfileCodec.MAsString(this.s, "minecraftVersion", 160);
         String var5 = ProfileCodec.MAsString(this.s, "larpVersion", 160);
         if (!var4.isBlank()) {
            var3.add("Minecraft " + var4);
         }

         if (!var5.isBlank()) {
            var3.add("Larp " + var5);
         }

         if (!var3.isEmpty()) {
            this.E.text(String.join(" · ", var3));
         }

         this.E.rAsQo("Preview import", () -> this.E.wI.oAsVoid(ProfileShareCodec.nAsString(this.s))).p = true;
      }
   }

   void FAsVoid() {
      this.E.s = 0;
      this.s = null;
      this.n = false;
      this.p = List.of();
      this.E.iAsVoid("Loading marketplace...", this.E.wq.o.cAsCompletableFuture("/api/configs?limit=30&offset=" + this.y, false).thenApply(var0 -> {
         AuctionPageCodec.validate(var0);
         return (JsonObject)var0;
      }), var1 -> {
         this.p = AuctionPageCodec.validate(var1).items();
         this.F = var1.get("total").getAsInt();
         this.n = true;
         this.E.Z = "Search filters this loaded page (" + this.p.size() + " of " + this.F + ").";
      });
   }

   void CAsVoid() {
      if (this.c != 0L && System.currentTimeMillis() >= this.c && this.E.QAsBoolean()) {
         this.c = 0L;
         this.E.EAsVoid();
      }
   }
}
