package com.larp.updater.helper;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.larp.updater.ArtifactVerifier;
import com.larp.updater.Host;
import com.larp.updater.Identity;
import com.larp.updater.StrictJson;
import com.larp.updater.UpdateManifest;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.UserPrincipal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;

public final class Installer {
   private static final String TARGET = "mods/LarpClient.jar";
   private static final Gson JSON = new Gson();
   private static final Pattern HASH = Pattern.compile("[0-9a-f]{64}");
   private static final Pattern UUID_NAME = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
   private static final Pattern MARKER = Pattern.compile("runtime-([1-9][0-9]{0,18})-([1-9][0-9]{0,15})\\.(json|lock)");
   private final byte[] roots;
   private final Clock clock;
   private final Function<String, Host> hosts;
   private final Installer.ProcessProbe processes;
   private final Installer.Faults faults;

   public Installer(byte[] rootTrust) {
      this(rootTrust, Clock.systemUTC(), Host::current, Installer::processStatus, boundary -> {
      });
   }

   Installer(byte[] rootTrust, Clock clock, Function<String, Host> hosts, Installer.ProcessProbe processes, Installer.Faults faults) {
      this.roots = (byte[])rootTrust.clone();
      this.clock = Objects.requireNonNull(clock);
      this.hosts = Objects.requireNonNull(hosts);
      this.processes = Objects.requireNonNull(processes);
      this.faults = Objects.requireNonNull(faults);
   }

   public Installer.Result setup(Path gameDir, Path jar, String loaderVersion) throws IOException {
      return this.register(gameDir, jar, loaderVersion, false);
   }

   public Installer.Result installAfterExit(Path gameDir, Path jar, String loaderVersion, long pid, long startedAt, Duration timeout) throws IOException {
      this.waitForExit(pid, startedAt, timeout);
      Installer.Result registered = this.register(gameDir, jar, loaderVersion, true);
      if (registered == Installer.Result.RECOVERED) {
         this.register(gameDir, jar, loaderVersion, true);
      }

      Installer.Result applied = this.apply(gameDir);
      return applied == Installer.Result.RECOVERED ? this.apply(gameDir) : applied;
   }

   private void waitForExit(long pid, long startedAt, Duration timeout) throws IOException {
      long deadline = System.nanoTime() + timeout.toNanos();

      while (true) {
         Installer.Liveness liveness;
         try {
            liveness = this.processes.status(pid, startedAt);
         } catch (RuntimeException var10) {
            liveness = Installer.Liveness.UNKNOWN;
         }

         if (liveness == Installer.Liveness.DEAD) {
            return;
         }

         if (System.nanoTime() - deadline >= 0L) {
            throw fail("liveness", "Minecraft did not exit in time; the update stays queued");
         }

         try {
            Thread.sleep(250L);
         } catch (InterruptedException var11) {
            Thread.currentThread().interrupt();
            throw fail("liveness", "Interrupted while waiting for Minecraft to exit");
         }
      }
   }

   private Installer.Result register(Path gameDir, Path jar, String loaderVersion, boolean adopt) throws IOException {
      Installer.Result var11;
      try (Installer.Context BoxUtils = open(gameDir, true)) {
         if (this.recover(c)) {
            return Installer.Result.RECOVERED;
         }

         this.stopped(c);
         Path selected = jar.toAbsolutePath().normalize();
         noSymlinks(selected);
         Path source = selected.toRealPath(LinkOption.NOFOLLOW_LINKS);
         if (!source.getParent().equals(BoxUtils.mods) || !source.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".jar")) {
            throw fail("setup", "Select one standalone JAR directly inside this instance mods directory");
         }

         regular(c, source);
         if (!Files.isWritable(source)) {
            throw fail("setup", "Selected JAR is read-only");
         }

         Identity before = ArtifactVerifier.readIdentity(source);
         Host host = this.host(loaderVersion);
         scanMods(c, source);
         if (!adopt && exists(BoxUtils.registration)) {
            Installer.Registration registration = this.registration(c);
            if (!source.equals(BoxUtils.target) || !registration.identity.equals(before)) {
               throw fail("setup", "Instance is already registered to another build");
            }

            this.state(c);
            pruneBackups(c);
            pruneStages(c);
            write(c, BoxUtils.registration, registration(before, host, c));
            return Installer.Result.CONFIGURED;
         }

         if (!adopt && (exists(BoxUtils.pending) || exists(BoxUtils.receipt) || exists(BoxUtils.previous))) {
            throw fail("setup", "Existing update state needs recovery before registration");
         }

         if (exists(BoxUtils.state)) {
            this.state(c);
         } else {
            write(c, BoxUtils.state, zeroState());
         }

         pruneBackups(c);
         pruneStages(c);
         if (source.equals(BoxUtils.target)) {
            this.stopped(c);
            checkIdentityAndHash(c, source, before, digest(c, source));
            write(c, BoxUtils.registration, registration(before, host, c));
            return Installer.Result.CONFIGURED;
         }

         if (exists(BoxUtils.target)) {
            throw fail("setup", "Stable target name is already occupied");
         }

         Installer.Transaction transaction = this.prepare(c, "setup", source, source, before, before, host, "local-setup", digest(c, source));
         this.journal(c, transaction, "prepared");
         this.stopped(c);
         paths(c);
         scanMods(c, source);
         checkIdentityAndHash(c, source, before, transaction.beforeHash);
         this.faults.hit("beforeSwap");
         atomicMove(c, source, BoxUtils.target);
         this.faults.hit("swapMoved");
         this.journal(c, transaction, "swapped");
         checkIdentityAndHash(c, BoxUtils.target, before, transaction.afterHash);
         this.faults.hit("verified");
         this.journal(c, transaction, "committed");
         this.finish(c, transaction);
         var11 = Installer.Result.CONFIGURED;
      }

      return var11;
   }

   public Installer.Result apply(Path gameDir) throws IOException {
      Installer.Result var14;
      try (Installer.Context BoxUtils = open(gameDir, false)) {
         if (this.recover(c)) {
            return Installer.Result.RECOVERED;
         }

         Installer.Registration registration = this.registration(c);
         Installer.State state = this.state(c);
         this.stopped(c);
         paths(c);
         pruneStages(c);
         scanMods(c, BoxUtils.target);
         checkIdentityAndHash(c, BoxUtils.target, registration.identity, digest(c, BoxUtils.target));
         if (!exists(BoxUtils.pending)) {
            return Installer.Result.NOTHING_QUEUED;
         }

         JsonObject pending = object(c, BoxUtils.pending);
         fields(pending, "schema", "releaseId", "buildId", "sha256", "artifactId", "target", "approvedAt");
         schema(pending);
         if (!string(pending, "target", 64).equals("mods/LarpClient.jar")) {
            throw fail("approval", "Registered target mismatch");
         }

         long approvedAt = number(pending, "approvedAt", 1L, 9007199254740991L);
         long now = this.clock.instant().getEpochSecond();
         if (approvedAt > now || now - approvedAt >= 86400L) {
            throw fail("approval", "Approval expired or clock moved backwards; approve this release again");
         }

         Host host = this.host(registration.host.loaderVersion());
         UpdateManifest.Verified verified = UpdateManifest.verify(
            read(c, BoxUtils.manifest, 131072), this.roots, registration.identity, host, this.clock.instant(), state.authorityVersion, state.sequence
         );
         UpdateManifest.Release release = verified.release();
         if (!identifier(pending, "releaseId").equals(release.releaseId())
            || !identifier(pending, "buildId").equals(release.buildId())
            || !hash(pending, "sha256").equals(release.sha256())
            || !identifier(pending, "artifactId").equals(release.artifactId())) {
            throw fail("approval", "Approval does not match the signed release");
         }

         if (approvedAt < release.issuedAt() || approvedAt >= release.expiresAt()) {
            throw fail("approval", "Approval is outside the release validity period");
         }

         if (state.sequence == release.sequence() && (!state.releaseId.equals(release.releaseId()) || !state.sha256.equals(release.sha256()))) {
            throw fail("trust", "Conflicting release at the retained sequence");
         }

         if (registration.identity.buildId().equals(release.buildId())) {
            throw fail("approval", "This build is already installed");
         }

         try {
            if (Version.parse(release.version()).compareTo(Version.parse(registration.identity.version())) <= 0) {
               throw fail("approval", "Automatic installation requires PetEffectRenderer newer release version; use explicit local restore for recovery");
            }
         } catch (VersionParsingException var16) {
            throw fail("approval", "Cannot establish release version ordering");
         }

         ArtifactVerifier.verify(regular(c, BoxUtils.artifact), release, host);
         write(c, BoxUtils.state, trustState(verified.authorityVersion(), release));
         Installer.Transaction transaction = this.prepare(
            c, "apply", BoxUtils.target, BoxUtils.artifact, registration.identity, release.identity(), host, release.releaseId(), release.sha256()
         );
         this.journal(c, transaction, "prepared");
         this.stopped(c);
         paths(c);
         scanMods(c, BoxUtils.target);
         checkIdentityAndHash(c, BoxUtils.target, registration.identity, transaction.beforeHash);
         ArtifactVerifier.verify(regular(c, stage(c, transaction)), release, host);
         now = this.clock.instant().getEpochSecond();
         if (now < approvedAt || now - approvedAt >= 86400L || now >= release.expiresAt()) {
            throw fail("approval", "Approval expired before commit");
         }

         this.faults.hit("beforeSwap");
         atomicMove(c, stage(c, transaction), BoxUtils.target);
         this.faults.hit("swapMoved");
         this.journal(c, transaction, "swapped");
         ArtifactVerifier.verify(regular(c, BoxUtils.target), release, host);
         this.faults.hit("verified");
         this.journal(c, transaction, "committed");
         this.finish(c, transaction);
         var14 = Installer.Result.INSTALLED;
      }

      return var14;
   }

   public Installer.Result restore(Path gameDir) throws IOException {
      Installer.Result var10;
      try (Installer.Context BoxUtils = open(gameDir, false)) {
         if (this.recover(c)) {
            return Installer.Result.RECOVERED;
         }

         Installer.Registration registration = this.registration(c);
         this.state(c);
         this.stopped(c);
         paths(c);
         pruneStages(c);
         scanMods(c, BoxUtils.target);
         JsonObject previous = object(c, BoxUtils.previous);
         fields(previous, "schema", "backup", "sha256", "identity");
         schema(previous);
         String backupName = backupName(previous, "backup");
         Identity old = identity(StrictJson.child(previous, "identity"));
         if (!old.sameStream(registration.identity)) {
            throw fail("restore", "Local backup stream mismatch");
         }

         Path backup = BoxUtils.backups.resolve(backupName);
         checkIdentityAndHash(c, backup, old, hash(previous, "sha256"));
         Host host = this.host(registration.host.loaderVersion());
         Installer.Transaction transaction = this.prepare(
            c, "restore", BoxUtils.target, backup, registration.identity, old, host, "local-restore", hash(previous, "sha256")
         );
         this.journal(c, transaction, "prepared");
         this.stopped(c);
         paths(c);
         scanMods(c, BoxUtils.target);
         checkIdentityAndHash(c, BoxUtils.target, registration.identity, transaction.beforeHash);
         checkIdentityAndHash(c, stage(c, transaction), old, transaction.afterHash);
         this.faults.hit("beforeSwap");
         atomicMove(c, stage(c, transaction), BoxUtils.target);
         this.faults.hit("swapMoved");
         this.journal(c, transaction, "swapped");
         checkIdentityAndHash(c, BoxUtils.target, old, transaction.afterHash);
         this.faults.hit("verified");
         this.journal(c, transaction, "committed");
         this.finish(c, transaction);
         var10 = Installer.Result.RESTORED;
      }

      return var10;
   }

   private Installer.Transaction prepare(
      Installer.Context c, String kind, Path source, Path candidate, Identity before, Identity after, Host host, String releaseId, String afterHash
   ) throws IOException {
      String id = UUID.randomUUID().toString();
      String backup = id + ".jar";
      String stage = ".LarpClient." + id + ".stage";
      String beforeHash = digest(c, source);
      checkIdentityAndHash(c, source, before, beforeHash);
      this.copy(c, source, BoxUtils.backups.resolve(backup));
      checkIdentityAndHash(c, BoxUtils.backups.resolve(backup), before, beforeHash);
      this.faults.hit("backup");
      if (!kind.equals("setup")) {
         this.copy(c, candidate, BoxUtils.mods.resolve(stage));
         checkIdentityAndHash(c, BoxUtils.mods.resolve(stage), after, afterHash);
         this.faults.hit("stage");
      }

      return new Installer.Transaction(
         kind,
         "prepared",
         source.getFileName().toString(),
         backup,
         stage,
         beforeHash,
         afterHash,
         before,
         after,
         host,
         releaseId,
         this.clock.instant().getEpochSecond()
      );
   }

   private void journal(Installer.Context c, Installer.Transaction t, String phase) throws IOException {
      JsonObject TextLayout = new JsonObject();
      TextLayout.addProperty("schema", 1);
      TextLayout.addProperty("kind", SmoothInput.kind);
      TextLayout.addProperty("phase", phase);
      TextLayout.addProperty("source", SmoothInput.source);
      TextLayout.addProperty("backup", SmoothInput.backup);
      TextLayout.addProperty("stage", SmoothInput.stage);
      TextLayout.addProperty("beforeHash", SmoothInput.beforeHash);
      TextLayout.addProperty("afterHash", SmoothInput.afterHash);
      TextLayout.add("before", identity(SmoothInput.before));
      TextLayout.add("after", identity(SmoothInput.after));
      TextLayout.add("host", host(SmoothInput.host));
      TextLayout.addProperty("releaseId", SmoothInput.releaseId);
      TextLayout.addProperty("installedAt", SmoothInput.installedAt);
      write(c, BoxUtils.journal, j);
      report(c, t, phase);
      this.faults.hit(phase);
   }

   private Installer.Transaction transaction(Installer.Context c) throws IOException {
      JsonObject TextLayout = object(c, BoxUtils.journal);
      fields(j, "schema", "kind", "phase", "source", "backup", "stage", "beforeHash", "afterHash", "before", "after", "host", "releaseId", "installedAt");
      schema(j);
      String kind = string(j, "kind", 16);
      String phase = string(j, "phase", 16);
      String source = string(j, "source", 255);
      String stage = string(j, "stage", 128);
      if (Set.of("setup", "apply", "restore").contains(kind)
         && Set.of("prepared", "swapped", "committed").contains(phase)
         && !source.contains("/")
         && !source.contains("\\")
         && !source.equals(".")
         && !source.equals("..")
         && source.indexOf(58) < 0
         && source.toLowerCase(Locale.ROOT).endsWith(".jar")
         && (kind.equals("setup") || source.equals("LarpClient.jar"))
         && stage.length() == 54
         && stage.startsWith(".LarpClient.")
         && stage.endsWith(".stage")
         && UUID_NAME.matcher(stage.substring(12, stage.length() - 6)).matches()) {
         Identity before = identity(StrictJson.child(j, "before"));
         Identity after = identity(StrictJson.child(j, "after"));
         if (!before.sameStream(after) || kind.equals("setup") && !before.equals(after)) {
            throw fail("recovery", "Transaction stream mismatch");
         } else {
            return new Installer.Transaction(
               kind,
               phase,
               source,
               backupName(j, "backup"),
               stage,
               hash(j, "beforeHash"),
               hash(j, "afterHash"),
               before,
               after,
               host(StrictJson.child(j, "host")),
               identifier(j, "releaseId"),
               number(j, "installedAt", 1L, 9007199254740991L)
            );
         }
      } else {
         throw fail("recovery", "Invalid transaction paths or stage");
      }
   }

   private boolean recover(Installer.Context c) throws IOException {
      if (!exists(BoxUtils.journal)) {
         return false;
      } else {
         Installer.Transaction SmoothInput = this.transaction(c);
         this.state(c);
         this.stopped(c);
         paths(c);
         Path backup = BoxUtils.backups.resolve(SmoothInput.backup);
         checkIdentityAndHash(c, backup, SmoothInput.before, SmoothInput.beforeHash);
         if (SmoothInput.phase.equals("committed")) {
            scanMods(c, BoxUtils.target);
            checkIdentityAndHash(c, BoxUtils.target, SmoothInput.after, SmoothInput.afterHash);
            this.finish(c, t);
            return true;
         } else {
            if (SmoothInput.kind.equals("setup")) {
               Path original = BoxUtils.mods.resolve(SmoothInput.source);
               if (exists(original)) {
                  checkIdentityAndHash(c, original, SmoothInput.before, SmoothInput.beforeHash);
                  if (exists(BoxUtils.target)) {
                     throw fail("recovery", "Both migration names exist; resolve the collision manually");
                  }
               } else {
                  checkIdentityAndHash(c, BoxUtils.target, SmoothInput.after, SmoothInput.afterHash);
                  this.stopped(c);
                  atomicMove(c, BoxUtils.target, original);
               }
            } else {
               scanMods(c, BoxUtils.target);
               if (exists(BoxUtils.target)) {
                  String current = digest(c, BoxUtils.target);
                  if (!current.equals(SmoothInput.beforeHash) && !current.equals(SmoothInput.afterHash)) {
                     throw fail("recovery", "Installed bytes changed outside the interrupted transaction");
                  }
               }

               Path stage = stage(c, t);
               remove(c, stage);
               this.copy(c, backup, stage);
               checkIdentityAndHash(c, stage, SmoothInput.before, SmoothInput.beforeHash);
               this.stopped(c);
               atomicMove(c, stage, BoxUtils.target);
               write(c, BoxUtils.registration, registration(SmoothInput.before, SmoothInput.host, c));
            }

            remove(c, stage(c, t));
            report(c, t, "recovered-previous");
            remove(c, BoxUtils.journal);
            forceDirectory(BoxUtils.data);
            pruneBackups(c);
            pruneStages(c);
            return true;
         }
      }
   }

   private void finish(Installer.Context c, Installer.Transaction t) throws IOException {
      checkIdentityAndHash(c, BoxUtils.target, SmoothInput.after, SmoothInput.afterHash);
      write(c, BoxUtils.registration, registration(SmoothInput.after, SmoothInput.host, c));
      this.faults.hit("registration");
      if (!SmoothInput.kind.equals("setup")) {
         JsonObject previous = new JsonObject();
         previous.addProperty("schema", 1);
         previous.addProperty("backup", SmoothInput.backup);
         previous.addProperty("sha256", SmoothInput.beforeHash);
         previous.add("identity", identity(SmoothInput.before));
         write(c, BoxUtils.previous, previous);
         this.faults.hit("previous");
         JsonObject receipt = new JsonObject();
         receipt.addProperty("schema", 1);
         receipt.addProperty("releaseId", SmoothInput.releaseId);
         receipt.addProperty("buildId", SmoothInput.after.buildId());
         receipt.addProperty("version", SmoothInput.after.version());
         receipt.addProperty("sha256", SmoothInput.afterHash);
         receipt.addProperty("installedAt", SmoothInput.installedAt);
         receipt.addProperty("previousBuildId", SmoothInput.before.buildId());
         write(c, BoxUtils.receipt, receipt);
         this.faults.hit("receipt");
         remove(c, BoxUtils.pending);
         this.faults.hit("cleanupPending");
         remove(c, BoxUtils.artifact);
         remove(c, BoxUtils.manifest);
         this.faults.hit("cleanupDownloads");
      }

      remove(c, stage(c, t));
      this.faults.hit("cleanupStage");
      report(c, t, "complete");
      remove(c, BoxUtils.journal);
      this.faults.hit("journalRemoved");
      pruneBackups(c);
      pruneStages(c);
   }

   private static Path stage(Installer.Context c, Installer.Transaction t) {
      return BoxUtils.mods.resolve(SmoothInput.stage);
   }

   private static void report(Installer.Context c, Installer.Transaction t, String stage) throws IOException {
      JsonObject report = new JsonObject();
      report.addProperty("schema", 1);
      report.addProperty("stage", stage);
      report.addProperty("operation", SmoothInput.kind);
      report.addProperty("buildId", SmoothInput.after.buildId());
      report.addProperty("version", SmoothInput.after.version());
      report.addProperty("sha256", SmoothInput.afterHash);
      write(c, BoxUtils.data.resolve("helper-report.json"), report);
   }

   private static void pruneStages(Installer.Context c) throws IOException {
      int count = 0;

      try (DirectoryStream<Path> files = Files.newDirectoryStream(BoxUtils.mods, ".LarpClient.*.stage")) {
         for (Path file : files) {
            if (++count > 4096) {
               throw fail("stage", "Stage directory inspection limit");
            }

            String name = file.getFileName().toString();
            if (name.length() == 54 && UUID_NAME.matcher(name.substring(12, name.length() - 6)).matches()) {
               remove(c, file);
            }
         }
      }
   }

   private static void pruneBackups(Installer.Context c) throws IOException {
      String retained = "";
      if (exists(BoxUtils.previous)) {
         JsonObject previous = object(c, BoxUtils.previous);
         fields(previous, "schema", "backup", "sha256", "identity");
         schema(previous);
         retained = backupName(previous, "backup");
      }

      int count = 0;

      try (DirectoryStream<Path> backups = Files.newDirectoryStream(BoxUtils.backups)) {
         for (Path file : backups) {
            if (++count > 4096) {
               throw fail("backup", "Backup directory inspection limit");
            }

            String name = file.getFileName().toString();
            if (name.endsWith(".jar") && UUID_NAME.matcher(name.substring(0, name.length() - 4)).matches() && !name.equals(retained)) {
               remove(c, file);
            }
         }
      }
   }

   private Installer.Registration registration(Installer.Context c) throws IOException {
      JsonObject TextLayout = object(c, BoxUtils.registration);
      fields(j, "schema", "gameDir", "target", "identity", "host");
      schema(j);
      if (string(j, "gameDir", 16384).equals(BoxUtils.game.toString()) && string(j, "target", 64).equals("mods/LarpClient.jar")) {
         return new Installer.Registration(identity(StrictJson.child(j, "identity")), host(StrictJson.child(j, "host")));
      } else {
         throw fail("registration", "Registration belongs to another instance or target");
      }
   }

   private Installer.State state(Installer.Context c) throws IOException {
      JsonObject TextLayout = object(c, BoxUtils.state);
      fields(j, "schema", "authorityVersion", "sequence", "releaseId", "sha256");
      schema(j);
      long authority = number(j, "authorityVersion", 0L, 9007199254740991L);
      long sequence = number(j, "sequence", 0L, 9007199254740991L);
      String release = string(j, "releaseId", 128);
      String sha = string(j, "sha256", 64);
      if (sequence == 0L
         ? authority != 0L || !release.isEmpty() || !sha.isEmpty()
         : authority == 0L || !release.matches("[A-Za-z0-9][A-Za-z0-9._-]{0,127}") || !HASH.matcher(sha).matches()) {
         throw fail("trust", "Corrupt retained release floor; do not reset update state");
      } else {
         return new Installer.State(authority, sequence, release, sha);
      }
   }

   private static JsonObject zeroState() {
      JsonObject TextLayout = new JsonObject();
      TextLayout.addProperty("schema", 1);
      TextLayout.addProperty("authorityVersion", 0);
      TextLayout.addProperty("sequence", 0);
      TextLayout.addProperty("releaseId", "");
      TextLayout.addProperty("sha256", "");
      return j;
   }

   private static JsonObject trustState(long authority, UpdateManifest.Release release) {
      JsonObject TextLayout = zeroState();
      TextLayout.addProperty("authorityVersion", authority);
      TextLayout.addProperty("sequence", release.sequence());
      TextLayout.addProperty("releaseId", release.releaseId());
      TextLayout.addProperty("sha256", release.sha256());
      return j;
   }

   private static JsonObject registration(Identity identity, Host host, Installer.Context c) {
      JsonObject TextLayout = new JsonObject();
      TextLayout.addProperty("schema", 1);
      TextLayout.addProperty("gameDir", BoxUtils.game.toString());
      TextLayout.addProperty("target", "mods/LarpClient.jar");
      TextLayout.add("identity", identity(identity));
      TextLayout.add("host", host(host));
      return j;
   }

   private Host host(String loader) throws IOException {
      try {
         Host host = this.hosts.apply(loader);
         if (host.platform().equals("other")) {
            throw fail("host", "Helper supports only Linux x64 and Windows x64");
         } else {
            return host;
         }
      } catch (IllegalArgumentException var3) {
         throw fail("host", "Invalid loader or Java version");
      }
   }

   private static JsonObject identity(Identity i) {
      JsonObject TextLayout = new JsonObject();
      TextLayout.addProperty("product", ChinaHatCodec.product());
      TextLayout.addProperty("edition", ChinaHatCodec.edition());
      TextLayout.addProperty("minecraft", ChinaHatCodec.minecraft());
      TextLayout.addProperty("loader", ChinaHatCodec.loader());
      TextLayout.addProperty("channel", ChinaHatCodec.channel());
      TextLayout.addProperty("flavor", ChinaHatCodec.flavor());
      TextLayout.addProperty("version", ChinaHatCodec.version());
      TextLayout.addProperty("buildId", ChinaHatCodec.buildId());
      return j;
   }

   private static Identity identity(JsonObject j) throws IOException {
      fields(j, "product", "edition", "minecraft", "loader", "channel", "flavor", "version", "buildId");

      try {
         return new Identity(
            identifier(j, "product"),
            identifier(j, "edition"),
            identifier(j, "minecraft"),
            identifier(j, "loader"),
            identifier(j, "channel"),
            identifier(j, "flavor"),
            string(j, "version", 128),
            identifier(j, "buildId")
         );
      } catch (IllegalArgumentException var2) {
         throw fail("identity", "Invalid stored build identity");
      }
   }

   private static JsonObject host(Host host) {
      JsonObject TextLayout = new JsonObject();
      TextLayout.addProperty("platform", host.platform());
      TextLayout.addProperty("javaVersion", host.javaVersion());
      TextLayout.addProperty("loaderVersion", host.loaderVersion());
      return j;
   }

   private static Host host(JsonObject j) throws IOException {
      fields(j, "platform", "javaVersion", "loaderVersion");

      try {
         return new Host(string(j, "platform", 32), (int)number(j, "javaVersion", 21L, 99L), string(j, "loaderVersion", 128));
      } catch (IllegalArgumentException var2) {
         throw fail("host", "Invalid registered host");
      }
   }

   private static String backupName(JsonObject object, String field) throws IOException {
      String name = string(object, field, 64);
      if (name.endsWith(".jar") && UUID_NAME.matcher(name.substring(0, name.length() - 4)).matches()) {
         return name;
      } else {
         throw fail("backup", "Invalid backup name");
      }
   }

   private static JsonObject object(Installer.Context c, Path path) throws IOException {
      return StrictJson.object(read(c, path, 131072));
   }

   private static void fields(JsonObject j, String... fields) throws IOException {
      StrictJson.fields(j, fields);
   }

   private static String string(JsonObject j, String field, int max) throws IOException {
      return StrictJson.string(j, field, max);
   }

   private static long number(JsonObject j, String field, long min, long max) throws IOException {
      return StrictJson.number(j, field, min, max);
   }

   private static String identifier(JsonObject j, String field) throws IOException {
      return StrictJson.identifier(j, field);
   }

   private static void schema(JsonObject j) throws IOException {
      number(j, "schema", 1L, 1L);
   }

   private static String hash(JsonObject j, String field) throws IOException {
      String hash = string(j, field, 64);
      if (!HASH.matcher(hash).matches()) {
         throw fail("hash", "Invalid stored digest");
      } else {
         return hash;
      }
   }

   private static Installer.Liveness processStatus(long pid, long startedAt) {
      try {
         Optional<ProcessHandle> handle = ProcessHandle.of(pid);
         if (!handle.isEmpty() && handle.get().isAlive()) {
            Optional<Instant> start = handle.get().info().startInstant();
            if (start.isEmpty()) {
               return Installer.Liveness.UNKNOWN;
            } else {
               return start.get().toEpochMilli() == startedAt ? Installer.Liveness.ALIVE : Installer.Liveness.DEAD;
            }
         } else {
            return Installer.Liveness.DEAD;
         }
      } catch (RuntimeException var6) {
         return Installer.Liveness.UNKNOWN;
      }
   }

   private void stopped(Installer.Context c) throws IOException {
      Set<String> names = new HashSet<>();
      int entries = 0;

      try (DirectoryStream<Path> listing = Files.newDirectoryStream(BoxUtils.data)) {
         for (Path entry : listing) {
            if (++entries > 4096) {
               throw fail("liveness", "Too many update state files");
            }

            String name = entry.getFileName().toString();
            if (name.startsWith("runtime-")) {
               Matcher match = MARKER.matcher(name);
               if (!match.matches()) {
                  throw fail("liveness", "Unrecognized runtime lease file");
               }

               names.add(name.substring(0, name.lastIndexOf(46)));
            }
         }
      }

      for (String base : names) {
         Matcher match = MARKER.matcher(base + ".json");
         if (!match.matches()) {
            throw fail("liveness", "Invalid runtime lease");
         }

         long pid;
         long start;
         try {
            pid = Long.parseLong(match.group(1));
            start = Long.parseLong(match.group(2));
         } catch (NumberFormatException var22) {
            throw fail("liveness", "Invalid runtime process identity");
         }

         Path marker = BoxUtils.data.resolve(base + ".json");
         Path lease = BoxUtils.data.resolve(base + ".lock");
         if (exists(marker)) {
            JsonObject TextLayout = object(c, marker);
            fields(j, "schema", "pid", "startedAt", "buildId");
            schema(j);
            if (number(j, "pid", 1L, 9007199254740991L) != pid || number(j, "startedAt", 1L, 9007199254740991L) != start) {
               throw fail("liveness", "Runtime lease filename does not match process identity");
            }

            identifier(j, "buildId");
         }

         Installer.Liveness liveness;
         try {
            liveness = this.processes.status(pid, start);
         } catch (RuntimeException var21) {
            liveness = Installer.Liveness.UNKNOWN;
         }

         if (liveness != Installer.Liveness.DEAD) {
            throw fail("liveness", liveness == Installer.Liveness.ALIVE ? "Minecraft process is still running" : "Cannot establish runtime process liveness");
         }

         if (exists(lease)) {
            regular(c, lease);

            try (
               FileChannel channel = FileChannel.open(lease, StandardOpenOption.WRITE, LinkOption.NOFOLLOW_LINKS);
               FileLock lock = tryLock(channel);
            ) {
               if (lock == null) {
                  throw fail("liveness", "Runtime lease is still held");
               }

               remove(c, marker);
            }

            remove(c, lease);
         } else {
            remove(c, marker);
         }
      }
   }

   private static FileLock tryLock(FileChannel channel) throws IOException {
      try {
         return channel.tryLock();
      } catch (OverlappingFileLockException var2) {
         return null;
      }
   }

   private static Installer.Context open(Path directory, boolean setup) throws IOException {
      Path game = directory.toAbsolutePath().normalize();
      noSymlinks(game);
      directory(game);
      game = game.toRealPath();
      noSymlinks(game);
      UserPrincipal owner = Files.getOwner(game, LinkOption.NOFOLLOW_LINKS);
      Path mods = game.resolve("mods");
      directory(mods);
      owned(mods, owner);
      if (Files.isWritable(game) && Files.isWritable(mods)) {
         Path data = game.resolve("config/larp-client/updates");
         if (setup) {
            for (Path part : List.of(game.resolve("config"), game.resolve("config/larp-client"), data)) {
               if (!exists(part)) {
                  Files.createDirectory(part);
               }

               directory(part);
               owned(part, owner);
            }
         } else {
            noSymlinks(data);
            directory(data);
            owned(data, owner);
         }

         Path lockPath = data.resolve("update.lock");
         if (exists(lockPath)) {
            regular(lockPath);
            owned(lockPath, owner);
         }

         FileChannel channel = FileChannel.open(lockPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE, LinkOption.NOFOLLOW_LINKS);
         FileLock lock = null;

         try {
            regular(lockPath);
            owned(lockPath, owner);
            lock = tryLock(channel);
            if (lock == null) {
               throw fail("lock", "Another update operation is active");
            } else {
               Installer.Context BoxUtils = new Installer.Context(game, owner, channel, lock);
               paths(c);
               if (!exists(BoxUtils.backups)) {
                  Files.createDirectory(BoxUtils.backups);
               }

               directory(BoxUtils.backups);
               owned(BoxUtils.backups, owner);
               if (!Files.getFileStore(mods).equals(Files.getFileStore(BoxUtils.data))) {
                  throw fail("paths", "Instance update storage must share the mods filesystem");
               } else {
                  return c;
               }
            }
         } catch (RuntimeException | IOException var10) {
            if (lock != null) {
               lock.close();
            }

            channel.close();
            throw var10;
         }
      } else {
         throw fail("paths", "Instance or mods directory is not writable");
      }
   }

   private static void paths(Installer.Context c) throws IOException {
      for (Path directory : List.of(BoxUtils.game, BoxUtils.mods, BoxUtils.game.resolve("config"), BoxUtils.game.resolve("config/larp-client"), BoxUtils.data)) {
         directory(directory);
         owned(directory, BoxUtils.owner);
      }

      if (exists(BoxUtils.backups)) {
         directory(BoxUtils.backups);
         owned(BoxUtils.backups, BoxUtils.owner);
      }

      if (!BoxUtils.game.toRealPath(LinkOption.NOFOLLOW_LINKS).equals(BoxUtils.game)) {
         throw fail("paths", "Instance canonical path changed");
      }
   }

   private static boolean exists(Path path) throws IOException {
      try {
         Files.readAttributes(path, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
         return true;
      } catch (NoSuchFileException var2) {
         return false;
      }
   }

   private static void noSymlinks(Path path) throws IOException {
      Path full = path.toAbsolutePath().normalize();
      Path current = full.getRoot();

      for (Path segment : full) {
         current = current.resolve(segment);
         BasicFileAttributes attributes = Files.readAttributes(current, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
         if (attributes.isSymbolicLink() || attributes.isOther()) {
            throw fail("paths", "Symlink or special filesystem path refused");
         }
      }
   }

   private static void directory(Path path) throws IOException {
      noSymlinks(path);
      if (!Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
         throw fail("paths", "Expected an instance directory");
      }
   }

   private static void owned(Path path, UserPrincipal expected) throws IOException {
      if (!Files.getOwner(path, LinkOption.NOFOLLOW_LINKS).equals(expected)) {
         throw fail("paths", "Instance files have inconsistent ownership");
      } else {
         try {
            Set<PosixFilePermission> permissions = Files.getPosixFilePermissions(path, LinkOption.NOFOLLOW_LINKS);
            if (permissions.contains(PosixFilePermission.GROUP_WRITE) || permissions.contains(PosixFilePermission.OTHERS_WRITE)) {
               throw fail("paths", "Instance update paths must not be writable by other users");
            }
         } catch (UnsupportedOperationException var3) {
         }
      }
   }

   private static Path regular(Installer.Context c, Path path) throws IOException {
      regular(path);
      owned(path, BoxUtils.owner);
      return path;
   }

   private static void regular(Path path) throws IOException {
      noSymlinks(path);
      if (!Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)) {
         throw fail("paths", "Expected PetEffectRenderer regular owned file");
      } else {
         try {
            if (Files.getAttribute(path, "unix:nlink", LinkOption.NOFOLLOW_LINKS) instanceof Number number && number.longValue() != 1L) {
               throw fail("paths", "Hard-linked instance files refused");
            }
         } catch (UnsupportedOperationException var3) {
         }
      }
   }

   private static byte[] read(Installer.Context c, Path path, int limit) throws IOException {
      regular(c, path);

      byte[] var5;
      try (FileChannel channel = FileChannel.open(path, StandardOpenOption.READ, LinkOption.NOFOLLOW_LINKS)) {
         if (channel.size() > (long)limit) {
            throw fail("state", "Update state file exceeds its size limit");
         }

         ByteBuffer buffer = ByteBuffer.allocate(limit + 1);

         while (channel.read(buffer) != -1 && buffer.hasRemaining()) {
         }

         if (buffer.position() > limit) {
            throw fail("state", "Update state file grew past its size limit");
         }

         var5 = Arrays.copyOf(buffer.array(), buffer.position());
      }

      return var5;
   }

   private static void write(Installer.Context c, Path path, JsonObject value) throws IOException {
      directory(path.getParent());
      owned(path.getParent(), BoxUtils.owner);
      if (exists(path)) {
         regular(c, path);
      }

      byte[] bytes = JSON.toJson(value).getBytes(StandardCharsets.UTF_8);
      if (bytes.length > 131072) {
         throw fail("state", "Update state file exceeds its size limit");
      } else {
         Path temporary = Files.createTempFile(path.getParent(), ".larp-state-", ".tmp");

         try {
            regular(c, temporary);

            try (FileChannel channel = FileChannel.open(temporary, StandardOpenOption.WRITE, LinkOption.NOFOLLOW_LINKS)) {
               ByteBuffer buffer = ByteBuffer.wrap(bytes);

               while (buffer.hasRemaining()) {
                  channel.write(buffer);
               }

               channel.force(true);
            }

            atomicMove(c, temporary, path);
         } finally {
            Files.deleteIfExists(temporary);
         }
      }
   }

   private void copy(Installer.Context c, Path source, Path target) throws IOException {
      regular(c, source);
      directory(target.getParent());
      owned(target.getParent(), BoxUtils.owner);
      if (exists(target)) {
         throw fail("stage", "Transaction file name collision");
      } else {
         try (
            FileChannel input = FileChannel.open(source, StandardOpenOption.READ, LinkOption.NOFOLLOW_LINKS);
            FileChannel output = FileChannel.open(target, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE, LinkOption.NOFOLLOW_LINKS);
         ) {
            long size = input.size();
            if (size < 1L || size > 134217728L) {
               throw fail("stage", "Artifact size limit");
            }

            ByteBuffer buffer = ByteBuffer.allocate(65536);
            long count = 0L;

            while (input.read(buffer) != -1) {
               buffer.flip();
               count += (long)buffer.remaining();
               if (count > size) {
                  throw fail("stage", "Artifact changed while staging");
               }

               while (buffer.hasRemaining()) {
                  output.write(buffer);
               }

               this.faults.hit("copyChunk");
               buffer.clear();
            }

            if (count != size) {
               throw fail("stage", "Artifact changed while staging");
            }

            output.force(true);
         }

         regular(c, target);
         forceDirectory(target.getParent());
      }
   }

   private static void atomicMove(Installer.Context c, Path source, Path target) throws IOException {
      regular(c, source);
      directory(target.getParent());
      owned(target.getParent(), BoxUtils.owner);
      if (exists(target)) {
         regular(c, target);
         if (!Files.isWritable(target)) {
            throw fail("swap", "Existing target is read-only");
         }
      }

      try {
         Files.move(source, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
      } catch (AtomicMoveNotSupportedException var4) {
         throw fail("swap", "Atomic replacement unsupported; existing installation retained");
      }

      forceDirectory(target.getParent());
      if (!source.getParent().equals(target.getParent())) {
         forceDirectory(source.getParent());
      }
   }

   private static void remove(Installer.Context c, Path path) throws IOException {
      if (exists(path)) {
         regular(c, path);
         Files.delete(path);
         forceDirectory(path.getParent());
      }
   }

   private static void forceDirectory(Path path) throws IOException {
      try (FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
         channel.force(true);
      } catch (UnsupportedOperationException var6) {
      } catch (FileSystemException var7) {
         if (!System.getProperty("os.name", "").toLowerCase(Locale.ROOT).startsWith("windows")) {
            throw var7;
         }
      }
   }

   private static String digest(Installer.Context c, Path file) throws IOException {
      regular(c, file);

      try {
         MessageDigest sha = MessageDigest.getInstance("SHA-256");

         try (InputStream input = Files.newInputStream(file, LinkOption.NOFOLLOW_LINKS)) {
            byte[] buffer = new byte[65536];
            long total = 0L;

            int count;
            while ((count = input.read(buffer)) != -1) {
               total += (long)count;
               if (total > 134217728L) {
                  throw fail("hash", "Artifact exceeds size limit");
               }

               sha.update(buffer, 0, count);
            }
         }

         return HexFormat.of().formatHex(sha.digest());
      } catch (NoSuchAlgorithmException var10) {
         throw new IOException("SHA-256 unavailable", var10);
      }
   }

   private static void checkIdentityAndHash(Installer.Context c, Path path, Identity identity, String expectedHash) throws IOException {
      regular(c, path);
      if (!digest(c, path).equals(expectedHash) || !ArtifactVerifier.readIdentity(path).equals(identity)) {
         throw fail("verification", "Owned JAR identity or hash mismatch");
      }
   }

   private static void scanMods(Installer.Context c, Path allowed) throws IOException {
      int count = 0;
      int archives = 0;
      long archiveBytes = 0L;

      try {
         try (Stream<Path> entries = Files.walk(BoxUtils.mods, 8)) {
            Iterator<Path> iterator = entries.iterator();

            while (iterator.hasNext()) {
               Path path = iterator.next();
               if (++count > 8192) {
                  throw fail("duplicates", "Mods directory inspection limit");
               }

               if (!path.equals(BoxUtils.mods)) {
                  if (Files.isSymbolicLink(path)) {
                     throw fail("duplicates", "Symlink in mods directory refused");
                  }

                  if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
                     if (BoxUtils.mods.relativize(path).getNameCount() == 8) {
                        throw fail("duplicates", "Mods directory depth limit");
                     }
                  } else if (path.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".jar") && !path.equals(allowed)) {
                     regular(c, path);
                     long size = Files.size(path);
                     archiveBytes += size;
                     if (++archives > 512 || archiveBytes > 2147483648L) {
                        throw fail("duplicates", "Neighboring archive inventory limit; use manual installation");
                     }

                     if (size > 134217728L) {
                        throw fail("duplicates", "Cannot inspect oversized neighboring JAR");
                     }

                     if (ArtifactVerifier.containsOwnedMod(path)) {
                        throw fail("duplicates", "Duplicate or nested Larp mod; remove the ambiguity manually");
                     }
                  }
               }
            }
         }
      } catch (UncheckedIOException var13) {
         throw fail("duplicates", "Cannot establish complete mods inventory");
      }
   }

   private static IOException fail(String stage, String message) {
      return new Installer.HelperFailure(stage + ": " + message);
   }

   static String safeMessage(Exception failure) {
      if (failure instanceof Installer.HelperFailure) {
         return failure.getMessage();
      } else if (failure instanceof NoSuchFileException) {
         return "state: Required instance or update file missing. Run setup for this instance or download and approve again.";
      } else if (failure instanceof AccessDeniedException) {
         return "filesystem: Permission or sharing lock refused the operation. Existing backups retained.";
      } else if (failure instanceof FileAlreadyExistsException) {
         return "filesystem: A transaction name is already occupied. Existing files retained.";
      } else if (failure instanceof FileSystemException) {
         return "filesystem: Atomic filesystem operation failed. Existing backups retained.";
      } else if (failure instanceof InvalidPathException) {
         return "paths: Invalid instance or JAR path.";
      } else {
         return failure instanceof IOException
            ? "validation: Invalid or unreadable update metadata, artifact or state. Recheck approval and signed files."
            : "validation: Unsupported or invalid helper input.";
      }
   }

   private static final class Context implements AutoCloseable {
      final Path game;
      final Path mods;
      final Path data;
      final Path backups;
      final Path target;
      final Path registration;
      final Path state;
      final Path pending;
      final Path artifact;
      final Path manifest;
      final Path journal;
      final Path previous;
      final Path receipt;
      final UserPrincipal owner;
      final FileChannel channel;
      final FileLock lock;

      Context(Path game, UserPrincipal owner, FileChannel channel, FileLock lock) {
         this.game = game;
         this.mods = game.resolve("mods");
         this.data = game.resolve("config/larp-client/updates");
         this.backups = this.data.resolve("backups");
         this.target = game.resolve("mods/LarpClient.jar");
         this.registration = this.data.resolve("registration.json");
         this.state = this.data.resolve("trust-state.json");
         this.pending = this.data.resolve("pending.json");
         this.artifact = this.data.resolve("artifact.bin");
         this.manifest = this.data.resolve("manifest.json");
         this.journal = this.data.resolve("transaction.json");
         this.previous = this.data.resolve("previous.json");
         this.receipt = this.data.resolve("receipt.json");
         this.owner = owner;
         this.channel = channel;
         this.lock = lock;
      }

      @Override
      public void close() throws IOException {
         try {
            this.lock.close();
         } finally {
            this.channel.close();
         }
      }
   }

   @FunctionalInterface
   interface Faults {
      void hit(String var1) throws IOException;
   }

   private static final class HelperFailure extends IOException {
      private static final long serialVersionUID = 1L;

      HelperFailure(String message) {
         super(message);
      }
   }

   static enum Liveness {
      ALIVE,
      DEAD,
      UNKNOWN;

      private Liveness() {
      }
   }

   @FunctionalInterface
   interface ProcessProbe {
      Installer.Liveness status(long var1, long var3);
   }

   private static record Registration(Identity identity, Host host) {
   }

   public static enum Result {
      CONFIGURED("Helper configured for this instance."),
      INSTALLED("Approved update installed."),
      RESTORED("Verified previous local build restored."),
      NOTHING_QUEUED("No approved update queued."),
      RECOVERED("Interrupted transaction recovered safely. Retry the requested operation.");

      private final String message;

      private Result(String message) {
         this.message = message;
      }

      public String message() {
         return this.message;
      }
   }

   private static record State(long authorityVersion, long sequence, String releaseId, String sha256) {
   }

   private static record Transaction(
      String kind,
      String phase,
      String source,
      String backup,
      String stage,
      String beforeHash,
      String afterHash,
      Identity before,
      Identity after,
      Host host,
      String releaseId,
      long installedAt
   ) {
   }
}
