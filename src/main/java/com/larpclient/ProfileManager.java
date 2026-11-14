package com.larpclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.larpclient.module.Halloween;
import com.larpclient.module.Module;
import com.larpclient.module.Setting;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.stream.Stream;
import net.fabricmc.loader.api.FabricLoader;

public final class ProfileManager {
   private static final Gson u = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
   private final Path I;
   private final Path N;
   private final Path t;
   private final PreferenceSaveQueue sf = new PreferenceSaveQueue(ProfileManager::rAsVoid);
   private String n = "Default";
   private String o;
   private ClientPreferences x = new ClientPreferences();
   private final Map<String, JsonObject> H = new HashMap<>();
   private JsonObject A = new JsonObject();
   private JsonObject U = new JsonObject();
   public static int sm;
   public static int e;
   public static boolean m;
   public static int J;
   public static int w;
   public static int B;
   public static int W;
   public static boolean k;
   public static boolean p;
   public static boolean l;
   public static boolean z;
   public static int P;
   public static boolean V;
   public static int f;
   public static int s;
   public static boolean D;
   public static boolean Q;
   public static int sH;
   public static boolean h;
   public static int F;
   public static boolean r;
   public static boolean X;
   public static int G;
   public static boolean O;
   public static int d;
   public static int Y;
   public static boolean R;
   public static boolean S;
   public static boolean sI;
   public static boolean L;
   public static boolean C;
   public static int i;
   public static int T;
   public static boolean g;
   public static int Z;
   public static int K;
   public static int M;
   public static boolean E;
   public static int a;
   public static boolean y;
   public static boolean sB;
   public static int v;
   public static int j;
   public static boolean c;
   public static boolean sh;
   public static int q;
   public static int b;
   public static int sA;

   public ProfileManager() {
      this(FabricLoader.getInstance().getConfigDir().resolve("larp-client"));
   }

   public ProfileManager(Path var1) {
      this.I = var1;
      this.N = var1.resolve("profiles");
      this.t = var1.resolve("active-profile.txt");
   }

   public ClientPreferences yAsN7() {
      return this.x;
   }

   public boolean bAsBoolean(boolean var1) {
      boolean var2 = this.x.u;
      if (var2 == var1) {
         return true;
      } else {
         this.x.u = var1;
         if (!this.jAsBoolean()) {
            this.x.u = var2;
            return false;
         } else {
            LarpClient var3 = LarpClient.CAsLarpClient();
            if (var3 != null) {
               var3.tAsVoid(var1);
            }

            return true;
         }
      }
   }

   public boolean CAsBoolean(boolean var1, List<Module> var2) {
      return var1 && this.x.v.uAsBoolean() && !this.x.v.lAsBoolean(false, var2, this.n, this::jAsBoolean)
         ? false
         : this.x.E.WAsBoolean(var1, var2, this.n, this::jAsBoolean);
   }

   public boolean GAsBoolean(boolean var1, List<Module> var2) {
      return var1 && this.x.E.RAsBoolean() && !this.x.E.WAsBoolean(false, var2, this.n, this::jAsBoolean)
         ? false
         : this.x.v.lAsBoolean(var1, var2, this.n, this::jAsBoolean);
   }

   public boolean fAsBoolean(boolean var1) {
      if (this.x.O == var1) {
         return true;
      } else {
         boolean var2 = this.x.O;
         this.x.O = var1;
         if (!this.jAsBoolean()) {
            this.x.O = var2;
            return false;
         } else {
            SoftParticlePipeline.S = var1;
            return true;
         }
      }
   }

   public boolean OAsBoolean(BloomQuality.Type_w var1) {
      Objects.requireNonNull(var1);
      BloomQuality.Type_w var2 = this.x.a;
      if (var1 == var2) {
         return true;
      } else {
         this.x.a = var1;
         if (!this.jAsBoolean()) {
            this.x.a = var2;
            return false;
         } else {
            BloomQualityHelper.K = var1;
            return true;
         }
      }
   }

   public boolean BAsBoolean(Module var1, Setting<?> var2, int var3) {
      SettingShortcuts var4 = this.x.k.VAsXn();
      this.x.k.bAsVoid(var1, var2, var3);
      if (this.jAsBoolean()) {
         ClientLog.GAsVoid("Input", "Setting shortcut {}.{} set to code {}", var1.getName(), var2.getName(), var3);
         return true;
      } else {
         this.x.k = var4;
         return false;
      }
   }

   public boolean jAsBoolean() {
      try {
         this.sf.lAsVoid(this.I.resolve("menu.json"), u.toJson(this.x.tAsJsonObject()));
         return true;
      } catch (IOException var2) {
         ClientLog.lAsVoid("Menu", "Could not save local interface preferences", var2);
         return false;
      }
   }

   public void NAsVoid() {
      this.sf.PAsVoid(this.I.resolve("menu.json"), u.toJson(this.x.tAsJsonObject()));
   }

   public void QAsVoid() {
      if (this.o != null) {
         this.iAsVoid();
      } else {
         String var1;
         try {
            var1 = BAsString(this.MAsJsonObject());
         } catch (IllegalArgumentException var3) {
            ClientLog.lAsVoid("Config", "Profile " + this.n + " was not saved; its previous saved copy is unchanged", var3);
            return;
         }

         this.sf.PAsVoid(this.VAsPath(this.n), var1);
         this.sf.PAsVoid(this.t, this.n);
         this.NAsVoid();
      }
   }

   public String dAsString() {
      return this.sf.iAsString();
   }

   public void hAsVoid() {
      try {
         this.sf.close();
      } catch (IOException var2) {
         ClientLog.lAsVoid("Config", "Final preference flush failed", var2);
      }
   }

   private void AAsVoid() {
      Path var1 = this.I.resolve("menu.json");
      if (Files.exists(var1)) {
         try {
            if (Files.isSymbolicLink(var1) || Files.size(var1) > 131072L) {
               throw new IOException("Unsafe menu preferences");
            }

            this.x = ClientPreferences.LAsN7(this.wAsJsonObject(var1));
         } catch (Exception var3) {
            ClientLog.lAsVoid("Menu", "Local interface preferences unavailable; using safe defaults", var3);
         }
      }
   }

   public synchronized void pAsVoid(String var1, String var2) throws IOException {
      this.sf.tAsVoid();
      Path var3 = this.VAsPath(var1);
      Path var4 = this.VAsPath(var2);
      if (!var1.equals(var2)) {
         if (Files.isSymbolicLink(this.N) || Files.isSymbolicLink(var3) || Files.isSymbolicLink(var4)) {
            throw new IOException("Unsafe profile path");
         }

         if (Files.exists(var4)) {
            throw new FileAlreadyExistsException("That profile name already exists");
         }

         Files.createLink(var4, var3);

         try {
            if (this.n.equals(var1)) {
               rAsVoid(this.t, var2);
            }
         } catch (IOException var7) {
            Files.deleteIfExists(var4);
            throw var7;
         }

         try {
            Files.delete(var3);
         } catch (IOException var6) {
            if (this.n.equals(var1)) {
               rAsVoid(this.t, var1);
            }

            Files.deleteIfExists(var4);
            throw var6;
         }

         if (this.n.equals(var1)) {
            this.n = var2;
         }

         this.x.BAsVoid(var1, var2);
         this.jAsBoolean();
         ClientLog.GAsVoid("Config", "Renamed profile {} to {}", var1, var2);
      }
   }

   public synchronized void bAsVoid(String var1) throws IOException {
      this.sf.tAsVoid();
      if (this.n.equals(var1)) {
         throw new IOException("Load another profile before deleting the current one");
      } else {
         Path var2 = this.VAsPath(var1);
         if (!Files.isSymbolicLink(this.N) && !Files.isSymbolicLink(var2)) {
            Files.delete(var2);
            this.x.oAsVoid(var1);
            this.jAsBoolean();
            ClientLog.GAsVoid("Config", "Deleted profile {}", var1);
         } else {
            throw new IOException("Unsafe profile path");
         }
      }
   }

   public synchronized void uAsVoid(String var1, boolean var2) throws IOException {
      this.wAsVoid(var1, this.MAsJsonObject(), var2, true);
   }

   public synchronized void DAsVoid(String var1, String var2) throws IOException {
      this.wAsVoid(var2, this.aAsJsonObject(var1), false, true);
   }

   public String ZAsString() {
      return this.n;
   }

   private Path VAsPath(String var1) {
      ProfileCodec.IAsString(var1);
      return this.N.resolve(var1 + ".json");
   }

   public List<String> eAsList() {
      try {
         if (!Files.isDirectory(this.N)) {
            return List.of();
         } else {
            List var2;
            try (Stream<Path> var1 = Files.list(this.N)) {
               var2 = var1.filter(var0 -> var0.toString().endsWith(".json") && Files.isRegularFile(var0, LinkOption.NOFOLLOW_LINKS))
                  .map(var0 -> var0.getFileName().toString().replaceFirst("\\.json$", ""))
                  .filter(var0 -> {
                     try {
                        ProfileCodec.IAsString(var0);
                        return true;
                     } catch (IllegalArgumentException var2x) {
                        return false;
                     }
                  })
                  .sorted()
                  .toList();
            }

            return var2;
         }
      } catch (IOException var61) {
         ClientLog.lAsVoid("Config", "Cannot list profiles at " + this.N, var61);
         return List.of();
      }
   }

   public void lAsVoid() {
      if (this.o != null) {
         this.iAsVoid();
      } else {
         this.kAsBoolean(this.n);
      }
   }

   public boolean kAsBoolean(String var1) {
      try {
         this.sf.tAsVoid();
         Path var2 = this.VAsPath(var1);
         rAsVoid(var2, BAsString(this.MAsJsonObject()));
         rAsVoid(this.t, var1);
         this.x.v.HAsVoid(this.n, var1);
         this.x.E.JAsVoid(this.n, var1);
         this.n = var1;
         this.o = null;
         if (this.x.v.uAsBoolean() || this.x.E.RAsBoolean()) {
            this.jAsBoolean();
         }

         ClientLog.GAsVoid("Config", "Saved {}", var2);
         return true;
      } catch (Exception var31) {
         ClientLog.lAsVoid("Config", "Failed to save profile " + var1, var31);
         return false;
      }
   }

   public boolean EAsBoolean(String var1) {
      try {
         this.sf.tAsVoid();
         Path var2 = this.VAsPath(var1);
         if (!Files.exists(var2)) {
            ClientLog.oAsVoid("Config", "Profile not found: {}", var2);
            return false;
         } else {
            JsonObject var3 = this.aAsJsonObject(var1);
            String var4 = this.n;
            rAsVoid(this.t, var1);

            try {
               this.CAsVoid(var3);
            } catch (RuntimeException var6) {
               rAsVoid(this.t, var4);
               throw var6;
            }

            this.n = var1;
            this.o = null;
            this.x.oAsVoid(var1, System.currentTimeMillis());
            this.jAsBoolean();
            ClientLog.GAsVoid("Config", "Loaded {}", var2);
            return true;
         }
      } catch (Exception var71) {
         ClientLog.lAsVoid("Config", "Failed to load profile " + var1 + "; existing values retained where possible", var71);
         return false;
      }
   }

   public void SAsVoid(String var1) {
      try {
         this.bAsVoid(var1);
      } catch (Exception var3) {
         ClientLog.lAsVoid("Config", "Failed to delete profile " + var1, var3);
      }
   }

   public void IAsVoid() {
      ClientLog.GAsVoid("Config", "Configuration directory: {}", this.I);
      this.AAsVoid();

      try {
         Files.createDirectories(this.N);
         if (Files.exists(this.t)) {
            String var1 = Files.readString(this.t).trim();

            try {
               this.VAsPath(var1);
               this.n = var1;
            } catch (IllegalArgumentException var3) {
               ClientLog.oAsVoid("Config", "Invalid active profile; using Default");
            }
         }

         Path var5 = this.VAsPath(this.n);
         if (Files.exists(var5, LinkOption.NOFOLLOW_LINKS)) {
            if (!this.EAsBoolean(this.n)) {
               this.o = this.n;
               this.iAsVoid();
            }
         } else {
            Path var2 = this.I.resolve("modules.json");
            if (Files.exists(var2)) {
               this.CAsVoid(this.wAsJsonObject(var2));
               ClientLog.GAsVoid("Config", "Migrated legacy {} to schema 2", var2);
               this.lAsVoid();
            }
         }
      } catch (Exception var41) {
         this.o = this.n;
         ClientLog.lAsVoid("Config", "Configuration load failed; protecting original profile and retaining defaults", var41);
         this.iAsVoid();
      }

      SoftParticlePipeline.S = this.x.O;
      BloomQualityHelper.K = this.x.a;
      this.IAsVoid(xAsList());
   }

   private synchronized void iAsVoid() {
      String var1 = this.o;
      if (var1 != null) {
         try {
            if (Files.isSymbolicLink(this.N)) {
               throw new IOException("Unsafe profile path");
            }

            Files.createDirectories(this.N);
            Path var2 = Files.createTempFile(this.N, "larp-recovery-", ".tmp");

            try {
               Files.writeString(var2, BAsString(this.MAsJsonObject()));
               long var3 = 1L;

               String var5;
               while (true) {
                  var5 = "Recovery " + var3;

                  try {
                     Files.createLink(this.VAsPath(var5), var2);
                     break;
                  } catch (FileAlreadyExistsException var11) {
                     var3++;
                  }
               }

               rAsVoid(this.t, var5);
               this.x.v.HAsVoid(this.n, var5);
               this.x.E.JAsVoid(this.n, var5);
               this.n = var5;
               this.o = null;
               this.jAsBoolean();
               ClientLog.oAsVoid("Config", "Rejected startup profile {} preserved; fallback saved as {}", var1, var5);
            } finally {
               Files.deleteIfExists(var2);
            }
         } catch (Exception var131) {
            ClientLog.lAsVoid("Config", "Recovery save failed; automatic saves will preserve rejected profile " + var1, var131);
         }
      }
   }

   void IAsVoid(List<Module> var1) {
      if (this.x.v.kAsJsonObject().get("active").getAsBoolean() && this.x.E.XAsJsonObject().get("active").getAsBoolean()) {
         this.x.v = new AppearanceRestoreJournal();
         this.x.E = new ActiveProfileJournal();
         this.jAsBoolean();
         ClientLog.oAsVoid("Menu", "Conflicting cosmetic preset journals ignored; loaded profile retained");
      }

      this.x.v.yAsBoolean(var1, this.n, this::jAsBoolean);
      this.x.E.DAsBoolean(var1, this.n, this::jAsBoolean);
   }

   private JsonObject wAsJsonObject(Path var1) throws IOException {
      try {
         return JsonParser.parseString(Files.readString(var1)).getAsJsonObject();
      } catch (RuntimeException var4) {
         Path var3 = var1.resolveSibling(var1.getFileName() + ".invalid-" + System.currentTimeMillis());
         Files.copy(var1, var3);
         ClientLog.lAsVoid("Config", "Malformed JSON preserved at " + var3, var4);
         throw var4;
      }
   }

   public JsonObject aAsJsonObject(String var1) throws IOException {
      this.sf.tAsVoid();
      Path var2 = this.VAsPath(var1);
      if (!Files.isSymbolicLink(var2) && !Files.isSymbolicLink(this.N) && Files.size(var2) <= 131072L) {
         return this.wAsJsonObject(var2);
      } else {
         throw new IOException("Unsafe or oversized profile");
      }
   }

   public boolean WAsBoolean(String var1) {
      return Files.exists(this.VAsPath(var1), LinkOption.NOFOLLOW_LINKS);
   }

   public synchronized void WAsVoid(String var1, JsonObject var2, boolean var3, List<Module> var4) throws IOException {
      this.wAsVoid(var1, var2, var3, false);
   }

   private synchronized void wAsVoid(String var1, JsonObject var2, boolean var3, boolean var4) throws IOException {
      this.sf.tAsVoid();
      var2 = var4 ? ProfileCodec.FAsJsonObject(var2) : ProfileCodec.aAsJsonObject(var2);
      Path var5 = this.VAsPath(var1);
      if (!Files.isSymbolicLink(this.N) && !Files.isSymbolicLink(var5)) {
         if (Files.exists(var5, LinkOption.NOFOLLOW_LINKS) && !var3) {
            throw new FileAlreadyExistsException("Choose a new profile name or explicitly confirm replacement.");
         } else {
            String var6 = BAsString(var2);
            if (var3) {
               rAsVoid(var5, var6);
            } else {
               Files.createDirectories(this.N);
               Path var7 = Files.createTempFile(this.N, "larp-import-", ".tmp");

               try {
                  Files.writeString(var7, var6);
                  Files.createLink(var5, var7);
               } finally {
                  Files.deleteIfExists(var7);
               }
            }

            ClientLog.XAsVoid("Config", "Imported profile saved locally");
         }
      } else {
         throw new IOException("Unsafe profile path");
      }
   }

   private JsonObject MAsJsonObject() {
      return this.vAsJsonObject(xAsList());
   }

   private static String BAsString(JsonObject var0) {
      ProfileCodec.FAsJsonObject(var0);
      String var1 = u.toJson(var0);
      return var1.getBytes(StandardCharsets.UTF_8).length <= 131072 ? var1 : var0.toString();
   }

   JsonObject vAsJsonObject(List<Module> var1) {
      return QAsJsonElement(this.A, this.QAsJsonObject(var1), this.U).getAsJsonObject();
   }

   private static JsonElement QAsJsonElement(JsonElement var0, JsonElement var1, JsonElement var2) {
      if (var0 instanceof JsonObject var3 && var1 instanceof JsonObject var4) {
         JsonObject var5 = var3.deepCopy();

         for (Entry var7 : var4.entrySet()) {
            JsonElement var8 = var2 instanceof JsonObject var9 ? var9.get((String)var7.getKey()) : null;
            var5.add((String)var7.getKey(), QAsJsonElement(var3.get((String)var7.getKey()), (JsonElement)var7.getValue(), var8));
         }

         return var5;
      }

      return var0 != null && var1.equals(var2) ? var0.deepCopy() : var1.deepCopy();
   }

   private JsonObject QAsJsonObject(List<Module> var1) {
      JsonObject var2 = new JsonObject();
      var2.addProperty("schema", 2);
      var2.add("ui", GuiWorkspace.bAsJsonObject());
      JsonObject var3 = new JsonObject();

      for (Module var5 : var1) {
         JsonObject var6 = new JsonObject();
         var6.addProperty("enabled", var5.isEnabledPreference());
         var6.addProperty("keybind", var5.getKeyBind());
         JsonObject var7 = new JsonObject();

         for (Setting var9 : var5.getSettings()) {
            var7.add(var9.getName(), u.toJsonTree(var9.getValue()));
         }

         var6.add("settings", var7);
         this.x.v.HAsVoid(var5, var6);
         this.x.E.aAsVoid(var5, var6);
         if (this.H.containsKey(var5.getName())) {
            var6.add("legacySettings", this.H.get(var5.getName()).deepCopy());
         }

         var3.add(var5.getName(), var6);
      }

      var2.add("modules", var3);
      return var2;
   }

   private void CAsVoid(JsonObject var1) {
      this.MAsVoid(var1, xAsList());
   }

   private static List<Module> xAsList() {
      LarpClient var0 = LarpClient.CAsLarpClient();
      return var0 == null ? List.of() : var0.mAsF().NAsList();
   }

   void MAsVoid(JsonObject var1, List<Module> var2) {
      JsonObject var3 = ProfileCodec.FAsJsonObject(var1);
      ModuleMigrationNames.qAsVoid(var1, var3);
      JsonObject var4 = var3.has("modules") ? var3.getAsJsonObject("modules") : var3;
      this.x.v.yAsVoid(var2, this::jAsBoolean);
      this.x.E.uAsVoid(var2, this::jAsBoolean);
      if (var3.has("ui")) {
         GuiWorkspace.TAsVoid(var3.getAsJsonObject("ui"));
      } else {
         GuiWorkspace.m++;
      }

      if (!var3.has("schema")) {
         ClientLog.GAsVoid("Config", "Reading legacy module configuration");
      }

      CustomSkySettings.wAsVoid(var4);
      JsonObject var5 = new JsonObject();

      for (Entry var7 : var4.entrySet()) {
         String var8 = var2.stream().map(Module::getName).filter(((String)var7.getKey())::equalsIgnoreCase).findFirst().orElse((String)var7.getKey());
         var5.add(var8, (JsonElement)var7.getValue());
      }

      var4 = var5;
      var3.add("modules", var5);
      this.H.clear();

      for (Module var17 : var2) {
         if (var17 instanceof Halloween var19) {
            var19.wAsVoid(var4.keySet().stream().anyMatch("Halloween"::equalsIgnoreCase));
         }
      }

      for (Entry var18 : var4.entrySet()) {
         String var20 = (String)var18.getKey();
         Module var9 = var2.stream().filter(var1x -> var1x.getName().equalsIgnoreCase(var20)).findFirst().orElse(null);
         if (var9 == null) {
            ClientLog.oAsVoid("Config", "Unknown module {} ignored", var20);
         } else {
            try {
               JsonObject var10 = ((JsonElement)var18.getValue()).getAsJsonObject();
               ModuleConfigCodec var11 = ModuleConfigCodec.decode(var9, var10);
               var11.apply(var9);
               if (var11.archive() != null) {
                  this.H.put(var9.getName(), var11.archive());
               }
            } catch (RuntimeException var121) {
               ClientLog.lAsVoid("Config", "Invalid module entry " + var20, var121);
            }
         }
      }

      this.A = var3.deepCopy();
      this.U = this.QAsJsonObject(var2);
      ClientLog.GAsVoid("Config", "Applied supported profile values; unfamiliar values retained for future saves");
   }

   private static void rAsVoid(Path var0, String var1) throws IOException {
      Files.createDirectories(var0.getParent());
      Path var2 = Files.createTempFile(var0.getParent(), "larp-", ".tmp");

      try {
         Files.writeString(var2, var1);

         try {
            Files.move(var2, var0, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
         } catch (AtomicMoveNotSupportedException var7) {
            Files.move(var2, var0, StandardCopyOption.REPLACE_EXISTING);
         }
      } finally {
         Files.deleteIfExists(var2);
      }
   }
}
