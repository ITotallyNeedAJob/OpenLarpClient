package com.larpclient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import net.minecraft.client.resource.metadata.TextureResourceMetadata;
import net.minecraft.client.texture.MipmapStrategy;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.ReloadableTexture;
import net.minecraft.client.texture.TextureContents;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.slf4j.LoggerFactory;

public final class StartupPresentation extends ReloadableTexture {
   public static final Identifier SmoothInput = Identifier.of("larp-client", "textures/gui/startup-brand.png");
   public static final Identifier PriceDetector = Identifier.of("larp-client", "startup/typography");
   private final String R;

   public static StartupPresentation.Type_a gAsW_Type_a(Path var0) {
      byte var1 = 1;
      if (!Files.exists(var0)) {
         return new StartupPresentation.Type_a(true);
      } else {
         try {
            if (Files.isSymbolicLink(var0) || Files.size(var0) > 131072L) {
               throw new IOException("Unsafe startup appearance preferences");
            }

            JsonObject var2 = JsonParser.parseString(Files.readString(var0)).getAsJsonObject();
            JsonElement var3 = var2.get("motion");
            if (var3 != null && var3.isJsonPrimitive() && var3.getAsJsonPrimitive().isBoolean()) {
               var1 = (byte)(var3.getAsBoolean() ? 1 : 0);
            }
         } catch (RuntimeException | IOException var41) {
            LoggerFactory.getLogger("Larp/Startup").warn("Startup motion preferences unavailable; using default motion", var41);
         }

         return new StartupPresentation.Type_a(var1 != 0);
      }
   }

   public StartupPresentation() {
      this(t, "textures/gui/logo.png");
   }

   public StartupPresentation(Identifier var1, String var2) {
      super(var1);
      this.R = "/assets/larp-client/" + var2;
   }

   public TextureContents loadContents(ResourceManager resourceManager) throws IOException {
      TextureContents var3;
      try (InputStream var2 = StartupPresentation.class.getResourceAsStream(this.R)) {
         if (var2 == null) {
            throw new IOException("Missing startup asset: " + this.R);
         }

         var3 = new TextureContents(NativeImage.read(var2), new TextureResourceMetadata(true, true, MipmapStrategy.MEAN, 0.0F));
      }

      return var3;
   }

   public static record Type_a(boolean motion) {
   }
}
