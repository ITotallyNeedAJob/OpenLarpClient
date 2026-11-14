package com.larp.updater.helper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public final class UpdaterHelper {
   static final Duration EXIT_WAIT = Duration.ofMinutes(5L);

   private UpdaterHelper() {
   }

   private static long positive(String value) throws UpdaterHelper.UsageFailure {
      if (!value.matches("[1-9][0-9]{0,17}")) {
         throw new UpdaterHelper.UsageFailure("Invalid process identity");
      } else {
         return Long.parseLong(value);
      }
   }

   public static void main(String[] arguments) {
      int result = run(arguments);
      if (result != 0) {
         System.exit(result);
      }
   }

   static int run(String[] arguments) {
      return run(arguments, null);
   }

   static int run(String[] arguments, Installer fixtureInstaller) {
      try {
         if (arguments.length < 1) {
            throw new UpdaterHelper.UsageFailure("Expected setup, apply, apply-after-exit or restore");
         } else {
            String command = arguments[0];
            if (!Set.of("setup", "apply", "apply-after-exit", "restore").contains(command)) {
               throw new UpdaterHelper.UsageFailure("Expected setup, apply, apply-after-exit or restore");
            } else {
               Map<String, String> flags = new LinkedHashMap<>();

               for (int i = 1; i < arguments.length; i += 2) {
                  if (i + 1 >= arguments.length
                     || !arguments[i].startsWith("--")
                     || arguments[i + 1].isBlank()
                     || flags.putIfAbsent(arguments[i], arguments[i + 1]) != null) {
                     throw new UpdaterHelper.UsageFailure("Invalid or repeated option");
                  }
               }
               Set<String> expected = switch (command) {
                  case "setup" -> Set.of("--game-dir", "--jar", "--loader-version");
                  case "apply-after-exit" -> Set.of("--game-dir", "--jar", "--loader-version", "--pid", "--started-at");
                  default -> Set.of("--game-dir");
               };
               if (!flags.keySet().equals(expected)) {
                  throw new UpdaterHelper.UsageFailure(
                     "setup requires --game-dir, --jar and --loader-version; apply-after-exit also requires --pid and --started-at; apply/restore require only --game-dir"
                  );
               } else {
                  Installer installer = fixtureInstaller;
                  if (fixtureInstaller == null) {
                     byte[] trust;
                     try (InputStream resource = UpdaterHelper.class.getResourceAsStream("/assets/larp-client/updates/root.json")) {
                        if (resource == null) {
                           throw new IOException("Packaged update trust is missing");
                        }

                        trust = resource.readNBytes(131073);
                        if (trust.length > 131072) {
                           throw new IOException("Packaged update trust is oversized");
                        }
                     }

                     installer = new Installer(trust);
                  }

                  Path game = Path.of(flags.get("--game-dir"));

                  Installer.Result result = switch (command) {
                     case "setup" -> installer.setup(game, Path.of(flags.get("--jar")), flags.get("--loader-version"));
                     case "apply" -> installer.apply(game);
                     case "apply-after-exit" -> installer.installAfterExit(
                     game,
                     Path.of(flags.get("--jar")),
                     flags.get("--loader-version"),
                     positive(flags.get("--pid")),
                     positive(flags.get("--started-at")),
                     EXIT_WAIT
                  );
                     default -> installer.restore(game);
                  };
                  System.out.println("[Larp/Updates] " + result.message());
                  if (result == Installer.Result.RECOVERED) {
                     System.err.println("[Larp/Updates] Stop this launch and rerun the requested helper operation before starting Minecraft.");
                     return 2;
                  } else {
                     if (command.equals("setup")) {
                        System.out.println("[Larp/Updates] Configure a waiting pre-launch command explicitly. Unverified launchers use manual updates.");
                     }

                     if (command.equals("restore")) {
                        System.out.println("[Larp/Updates] Restored only the local JAR. Check configuration downgrade compatibility before launch.");
                     }

                     return 0;
                  }
               }
            }
         }
      } catch (Exception var12) {
         System.err.println("[Larp/Updates] Refused: " + (var12 instanceof UpdaterHelper.UsageFailure ? var12.getMessage() : Installer.safeMessage(var12)));
         System.err.println("[Larp/Updates] Close Minecraft, keep the staged files and backup, then resolve the reported stage and retry.");
         return 1;
      }
   }

   private static final class UsageFailure extends IOException {
      private static final long serialVersionUID = 1L;

      UsageFailure(String message) {
         super(message);
      }
   }
}
