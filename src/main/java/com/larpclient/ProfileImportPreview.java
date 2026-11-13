package com.larpclient;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

final class ProfileImportPreview {
   private final ProfileSettingsPanel g;
   String S;
   String R = "";
   String d = "";
   List<JsonObject> D = List.of();
   JsonObject b;
   ProfileCodec.Type_t m;
   boolean W;

   ProfileImportPreview(ProfileSettingsPanel var1) {
      this.g = var1;
      this.S = null;
   }

   void XAsVoid() {
      this.b = null;
      this.m = null;
   }

   void JAsVoid() {
      this.b = null;
      this.D = List.of();
   }

   TextInputWidget cAsQq() {
      return this.g.tAsQq("LARP-XXXXXXXXXXXXXXXX", this.d, 32, var1 -> this.d = var1);
   }

   void DAsVoid() {
      this.g.QAsVoid("Import preview", this.m.name());
      this.g
         .text(
            "Larp "
               + this.m.larpVersion()
               + " · Minecraft "
               + this.m.minecraftVersion()
               + "\n"
               + this.m.modules()
               + " modules, "
               + this.m.enabled()
               + " enabled"
         );
      this.g.text("Supported settings load. Unavailable settings stay saved. Saving does not change your active config.");
      this.g.tAsQq("Local config name", this.R, 32, var1 -> this.R = var1);
      this.g.rAsQo("Cancel", () -> {
         this.m = null;
         this.g.wf = false;
         this.g.build();
      });
      this.g.rAsQo("Save locally", () -> this.oAsVoid(false)).p = true;
   }

   void yAsVoid() {
      String var1 = this.g.G.toLowerCase(Locale.ROOT);

      for (String var3 : this.g.wv.eAsList()) {
         if (var3.toLowerCase(Locale.ROOT).contains(var1)) {
            this.g.FAsVoid(var3, var3.equals(this.g.wv.ZAsString()) ? "Active" : "Local", this.b == null && var3.equals(this.S), () -> {
               this.b = null;
               this.S = var3;
               this.g.s = 0;
               this.g.build();
            });
            boolean var4 = var3.equals(this.g.wv.ZAsString());
            this.g.QAsVoid(var4 ? "Update" : "Load", () -> {
               if (var4) {
                  this.PAsVoid(var3);
               } else {
                  this.HAsVoid(var3);
               }
            });
            this.g.QAsVoid("Share", () -> this.iAsVoid(var3));
            if (!var4) {
               this.g.QAsVoid("Delete", () -> this.lAsVoid(var3));
            }
         }
      }

      for (JsonObject var6 : this.D) {
         String var7 = ProfileCodec.MAsString(var6, "name", 32);
         if (var7.toLowerCase(Locale.ROOT).contains(var1)) {
            this.g.FAsVoid(var7, this.W ? "Experimental storage" : "Shared · " + ProfileShareCodec.KAsString(var6), this.b == var6, () -> {
               this.b = var6;
               this.g.s = 0;
               this.g.build();
            });
         }
      }
   }

   void jAsVoid() {
      if (this.b != null) {
         this.wAsVoid();
      } else if (this.S != null) {
         String var1 = this.S;
         boolean var2 = var1.equals(this.g.wv.ZAsString());
         Optional var3 = this.D.stream().filter(var1xx -> ProfileCodec.MAsString(var1xx, "name", 32).equals(var1)).findFirst();
         String var4 = var2 ? "Active config" : "Saved config";
         if (var3.isPresent()) {
            var4 = var4 + (this.W ? " · Experimental copy" : " · Shared, " + ProfileShareCodec.KAsString((JsonObject)var3.get()).toLowerCase(Locale.ROOT));
         }

         this.g.QAsVoid(var1, var4);
         if (var3.isPresent() && !this.W && !ProfileShareCodec.VAsString((JsonObject)var3.get()).isEmpty()) {
            this.g.text(ProfileShareCodec.VAsString((JsonObject)var3.get()));
         }

         if (!this.g.Y.isEmpty()) {
            this.g.option("Copy share code", this.g.Y, () -> {
               this.g.JAsMinecraftClient().keyboard.setClipboard(this.g.Y);
               this.g.Z = "Share code copied to clipboard.";
            });
         }

         this.g.option("Load", "Replace your live settings with this config", () -> this.HAsVoid(var1));
         this.g.option("Save changes", "Overwrite it with your current settings", () -> this.PAsVoid(var1));
         if (this.g.wq.SAsBoolean() && this.W) {
            this.g
               .option(
                  "Upload experimental copy",
                  "Send the saved file to experimental storage",
                  () -> this.g
                        .fAsVoid("Upload an experimental copy?\nSaved “" + var1 + "” goes to experimental storage.", "Upload", () -> this.QAsVoid(var1, true))
               );
         }

         this.g.option("Share", "Upload it and copy a share code", () -> this.iAsVoid(var1));
         this.g.option("Duplicate", "Save a copy under a new name", () -> this.g.WAsVoid("Duplicate", var1 + " copy"));
         this.g.option("Rename", "Change its name", () -> this.g.WAsVoid("Rename", var1));
         if (!var2) {
            this.g.OAsVoid();
            this.g.option("Delete", "Remove it from this computer", () -> this.lAsVoid(var1));
         }
      }
   }

   private void HAsVoid(String var1) {
      this.g.fAsVoid("Load “" + var1 + "”?\nYour live settings are replaced with this config.", "Load", () -> {
         this.g.Z = this.g.wv.EAsBoolean(var1) ? "Loaded " + var1 : "Load failed. Existing profile retained.";
         this.g.build();
      });
   }

   private void PAsVoid(String var1) {
      this.g.fAsVoid("Save changes to “" + var1 + "”?\nThe saved config is replaced with your current settings.", "Save", () -> {
         this.g.i.HAsVoid();

         try {
            this.g.wv.uAsVoid(var1, true);
            this.g.Z = "Saved " + var1;
         } catch (IOException var3) {
            ClientLog.oAsVoid("Config", "Save failed: {}", var3.toString());
            this.g.Z = "Save failed: " + var3.getMessage();
         }

         this.g.build();
      });
   }

   private void lAsVoid(String var1) {
      this.g.KAsVoid("Delete “" + var1 + "”?\nThe saved file is removed from this computer. This cannot be undone.", "Delete", () -> {
         try {
            this.g.wv.bAsVoid(var1);
            this.S = null;
            this.g.Z = "Deleted";
         } catch (IOException var3) {
            ClientLog.oAsVoid("Config", "Delete failed: {}", var3.toString());
            this.g.Z = "Delete failed: " + var3.getMessage();
         }

         this.g.build();
      });
   }

   private void iAsVoid(String var1) {
      this.g.Y = "";
      this.g
         .hAsVoid(
            "Share “"
               + var1
               + "”\nIt gets uploaded to larpclient.com and the code is copied to your clipboard. Only people with the code can open it.\nPost to Discord and the bot also puts it in the Discord server, code included, within a minute.",
            "Share",
            () -> this.cAsVoid(var1, false, false),
            "Share and post to Discord",
            () -> this.cAsVoid(var1, false, true)
         );
   }

   private void QAsVoid(String var1, boolean var2) {
      this.cAsVoid(var1, var2, false);
   }

   private void cAsVoid(String var1, boolean var2, boolean var3) {
      try {
         JsonObject var4 = ProfileCodec.rAsJsonObject(var1, this.g.wv.aAsJsonObject(var1), this.g.oAsList());
         if (var3) {
            var4.addProperty("discordSharing", true);
         }

         this.g.Y = "";
         this.g
            .iAsVoid(
               "Uploading to larpclient.com...",
               var2
                  ? this.g.wq.oAsCompletableFuture("POST", "/api/profiles", var4, null)
                  : this.g.wq.o.FAsCompletableFuture("POST", "/api/configs", var4, true, null),
               var3xx -> {
                  ProfileShareCodec.uAsString(var3xx, "id");
                  ProfileShareCodec.uAsString(var3xx, "revision");
                  if (var2) {
                     this.g.Z = "Experimental profile uploaded";
                  } else {
                     this.g.Y = ProfileShareCodec.nAsString(var3xx);
                     byte var4x = 1;

                     try {
                        this.g.JAsMinecraftClient().keyboard.setClipboard(this.g.Y);
                        this.g.Z = "Uploaded successfully. Share code copied to clipboard.";
                        ClientLog.GAsVoid("Config", "Saved config uploaded; share code copied to clipboard");
                     } catch (RuntimeException var6) {
                        var4x = 0;
                        this.g.Z = "Uploaded successfully. Clipboard copy failed. Use Copy share code to retry.";
                        ClientLog.oAsVoid("Config", "Upload succeeded but clipboard copy failed: {}", var6.toString());
                     }

                     if (var3) {
                        this.g.Z = (var4x != 0 ? "Code copied. " : "Clipboard copy failed, use Copy share code. ")
                           + "The bot posts it in Discord within a minute.";
                        ClientLog.GAsVoid("Config", "Shared config opted in to Discord posting");
                        this.TAsVoid(true);
                     }
                  }
               }
            );
      } catch (IOException var51) {
         ClientLog.oAsVoid("Config", "Config operation failed: {}", var51.toString());
         this.g.Z = "Could not read the saved profile. Save it locally first.";
         this.g.build();
      }
   }

   void wAsVoid() {
      if (this.W && !this.g.wq.SAsBoolean()) {
         this.g.QAsVoid("Experimental storage", "");
         this.g.text("Experimental access is unavailable for this session.");
      } else if (this.b != null) {
         String var1 = ProfileCodec.MAsString(this.b, "name", 32);
         String var2 = "Updated " + ProfileCodec.MAsString(this.b, "updatedAt", 40);
         if (!this.W) {
            this.g.QAsVoid(var1, ProfileShareCodec.KAsString(this.b) + " · " + var2);
            String var3 = ProfileShareCodec.VAsString(this.b);
            if (!var3.isEmpty()) {
               this.g.text(var3);
            }

            String var4 = ProfileCodec.MAsString(this.b, "rejectionReason", 500);
            if (!var4.isBlank()) {
               this.g.text("Review reason: " + var4);
            }

            this.g.option("Copy share code", ProfileShareCodec.nAsString(this.b), () -> {
               this.g.JAsMinecraftClient().keyboard.setClipboard(ProfileShareCodec.nAsString(this.b));
               this.g.Z = "Copied";
            });
            this.g.option("Import / download", "Preview it before saving locally", () -> this.oAsVoid(ProfileShareCodec.nAsString(this.b)));
            this.g
               .option(
                  "Regenerate share code",
                  "Replace the code; the old one stops working",
                  () -> this.HAsVoid("regenerate", "Regenerate the share code?\nThe old code stops working immediately.", "Regenerate")
               );
            String var5 = this.b.get("marketplaceState").getAsString();
            boolean var6 = ProfileShareCodec.XAsBoolean(this.b);
            if (!var6 && !var5.equals("removed")) {
               this.g.option("Post to Discord", "The bot posts it with its share code", this::IAsVoid);
            }

            if (var6) {
               this.g
                  .option(
                     "Stop posting to Discord",
                     "The bot deletes its post; the code keeps working",
                     () -> this.HAsVoid(
                           "discord-sharing", "Take “" + var1 + "” off Discord?\nThe bot deletes its post. Your share code keeps working.", "Take off Discord"
                        )
                  );
            }

            if (Set.of("private", "rejected").contains(var5)) {
               this.g
                  .option(
                     "Submit to Discover",
                     "Send this revision for public review",
                     () -> this.HAsVoid("submit", "Submit to Discover?\nThis revision goes to public Marketplace review.", "Submit")
                  );
            }

            if (Set.of("submitted", "approved", "rejected").contains(var5)) {
               this.g
                  .option(
                     "Withdraw Discover submission",
                     "Take it out of the public Marketplace",
                     () -> this.HAsVoid(
                           "withdraw", "Withdraw this Discover submission?\nIt leaves the public Marketplace. Your share code keeps working.", "Withdraw"
                        )
                  );
            }

            this.g.OAsVoid();
            this.g
               .option(
                  "Delete cloud config",
                  "Remove it and revoke its share code",
                  () -> this.HAsVoid("delete", "Delete “" + var1 + "” from the cloud?\nIts share code is revoked. This cannot be undone.", "Delete")
               );
         } else {
            this.g.QAsVoid(var1, "Experimental storage · " + var2);
            this.g
               .option(
                  "Download preview",
                  "Review the server copy before saving it",
                  () -> this.g.iAsVoid("Downloading...", this.g.wq.oAsCompletableFuture("GET", this.hAsString(), null, null), this::aAsVoid)
               );
            this.g.option("Check revision", "Compare with the server's latest revision", () -> {
               String var1x = ProfileShareCodec.uAsString(this.b, "revision");
               this.g.iAsVoid("Checking revision...", this.g.wq.oAsCompletableFuture("GET", this.hAsString() + "/revision", null, null), var2x -> {
                  if (!var1x.equals(ProfileShareCodec.uAsString(var2x, "revision"))) {
                     this.g.Z = "Server revision changed. Review the refreshed item.";
                     this.TAsVoid(true);
                  } else {
                     this.g.Z = "Server revision is unchanged.";
                  }
               });
            });
            if (this.S == null) {
               this.S = this.g.wv.ZAsString();
            }

            this.g
               .option(
                  "Replace server copy",
                  "From saved “" + this.S + "” (choose it in My Configs)",
                  () -> this.g
                        .fAsVoid(
                           "Replace the server copy?\nThe experimental copy is replaced with saved local config “" + this.S + "”.", "Replace", this::RAsVoid
                        )
               );
            this.g.OAsVoid();
            this.g
               .option(
                  "Delete experimental profile",
                  "Remove the server copy",
                  () -> this.g
                        .KAsVoid(
                           "Delete this experimental profile?\nThe server copy is removed. This cannot be undone.",
                           "Delete",
                           () -> this.g
                                 .iAsVoid(
                                    "Deleting...",
                                    this.g.wq.oAsCompletableFuture("DELETE", this.hAsString(), null, ProfileShareCodec.uAsString(this.b, "revision")),
                                    var1x -> {
                                       MAsVoid(var1x);
                                       this.TAsVoid(false);
                                    }
                                 )
                        )
               );
         }
      }
   }

   private String hAsString() {
      return "/api/profiles/" + ProfileShareCodec.uAsString(this.b, "id");
   }

   private void RAsVoid() {
      try {
         JsonObject var1 = ProfileCodec.rAsJsonObject(this.S, this.g.wv.aAsJsonObject(this.S), this.g.oAsList());
         var1.addProperty("revision", ProfileShareCodec.uAsString(this.b, "revision"));
         this.g.iAsVoid("Replacing server copy...", this.g.wq.oAsCompletableFuture("PUT", this.hAsString(), var1, null), var1x -> {
            ProfileShareCodec.uAsString(var1x, "revision");
            this.TAsVoid(false);
         });
      } catch (IOException var21) {
         ClientLog.oAsVoid("Config", "Config operation failed: {}", var21.toString());
         this.g.Z = "Could not read local profile.";
         this.g.build();
      }
   }

   private CompletableFuture<JsonObject> HAsCompletableFuture(JsonObject var1, String var2, Boolean var3) {
      JsonObject var4 = new JsonObject();
      var4.addProperty("action", var2);
      var4.addProperty("revision", ProfileShareCodec.uAsString(var1, "revision"));
      if (var3 != null) {
         var4.addProperty("discordSharing", var3);
      }

      return this.g.wq.o.FAsCompletableFuture("POST", "/api/configs/" + ProfileShareCodec.uAsString(var1, "id") + "/actions", var4, true, null);
   }

   private void IAsVoid() {
      this.g
         .fAsVoid(
            "Post “"
               + ProfileCodec.MAsString(this.b, "name", 32)
               + "” in the Discord server?\nThe bot posts it with its share code within a minute, so anyone in the server can import it.",
            "Post",
            () -> this.g.iAsVoid("Turning on Discord posting...", this.HAsCompletableFuture(this.b, "discord-sharing", true), var1 -> {
                  ProfileShareCodec.uAsString(var1, "revision");
                  this.g.Z = "Done. The bot posts it in Discord within a minute.";
                  ClientLog.GAsVoid("Config", "Discord posting enabled for shared config");
                  this.TAsVoid(true);
               })
         );
   }

   private void HAsVoid(String var1, String var2, String var3) {
      Runnable var4 = () -> this.g
            .iAsVoid("Updating cloud config...", this.HAsCompletableFuture(this.b, var1, var1.equals("discord-sharing") ? false : null), var2x -> {
               if (var1.equals("delete")) {
                  MAsVoid(var2x);
               } else {
                  ProfileShareCodec.nAsString(var2x);
                  ProfileShareCodec.KAsString(var2x);
                  ProfileShareCodec.uAsString(var2x, "revision");
               }

               if (var1.equals("regenerate")) {
                  this.g.Y = ProfileShareCodec.nAsString(var2x);
               }

               if (var1.equals("discord-sharing")) {
                  this.g.Z = "Taken off Discord. The bot removes its post within a minute.";
                  ClientLog.GAsVoid("Config", "Discord posting turned off for shared config");
               }

               this.TAsVoid(var1.equals("discord-sharing"));
            });
      if (var1.equals("delete")) {
         this.g.KAsVoid(var2, var3, var4);
      } else {
         this.g.fAsVoid(var2, var3, var4);
      }
   }

   private static void MAsVoid(JsonObject var0) {
      if (!ProfileCodec.SAsBoolean(var0.get("deleted"))) {
         throw AuthException.rAsAh();
      }
   }

   void TAsVoid(boolean var1) {
      String var2 = this.g.Z;
      String var3 = this.b == null ? "" : ProfileShareCodec.uAsString(this.b, "id");
      this.g
         .iAsVoid(
            "Loading cloud items...",
            this.W ? this.g.wq.JAsCompletableFuture() : this.g.wq.o.cAsCompletableFuture("/api/configs/mine?limit=100", true),
            var4 -> {
               this.D = ProfileShareCodec.dAsList(var4, this.W);
               this.b = this.D.stream().filter(var1xx -> ProfileShareCodec.uAsString(var1xx, "id").equals(var3)).findFirst().orElse(null);
               this.g.Z = var1 ? var2 : "Cloud items refreshed";
            }
         );
   }

   void oAsVoid(String var1) {
      this.g.iAsVoid("Importing...", this.g.wq.o.cAsCompletableFuture("/api/configs/import/" + var1, false), this::aAsVoid);
   }

   private void aAsVoid(JsonObject var1) {
      try {
         this.m = ProfileCodec.VAsAg_Type_t(var1, this.g.oAsList());
         this.R = this.m.name();
         this.g.s = 0;
         NotificationCategory.CAsVoid("Config", NotificationCategory.Type_o.IMPORT_VALIDATED);
         this.g.Z = "Review before saving locally";
      } catch (IllegalArgumentException var3) {
         NotificationCategory.CAsVoid("Config", NotificationCategory.Type_o.IMPORT_REJECTED);
         ClientLog.oAsVoid("Config", "Import rejected: {}", var3.getMessage());
         this.g.Z = var3.getMessage();
      }
   }

   void oAsVoid(boolean var1) {
      ProfileCodec.IAsString(this.R);
      if (this.g.wv.WAsBoolean(this.R) && !var1) {
         this.g.fAsVoid("Replace local “" + this.R + "”?\nYour live settings stay unchanged until you load it.", "Replace", () -> this.oAsVoid(true));
      } else {
         try {
            this.g.wv.WAsVoid(this.R, this.m.config(), var1, this.g.oAsList());
            String var2 = this.R;
            this.m = null;
            this.g.wf = false;
            this.S = var2;
            this.g.wAsVoid("Saved as “" + var2 + "”\nLoad it now and replace your live settings?", "Load", () -> {
               this.g.Z = this.g.wv.EAsBoolean(var2) ? "Loaded local profile" : "Load failed. Config remains saved locally.";
               this.g.build();
            }, "Not now");
         } catch (IOException var31) {
            ClientLog.oAsVoid("Config", "Config operation failed: {}", var31.toString());
            this.g.Z = "Could not save locally. Choose a different profile name and retry.";
            this.g.build();
         }
      }
   }
}
