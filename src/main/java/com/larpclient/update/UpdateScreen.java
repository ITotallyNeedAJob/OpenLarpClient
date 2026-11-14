package com.larpclient.update;

import com.larp.updater.UpdateManifest;
import com.larpclient.ClientPreferences;
import com.larpclient.GuiDrawing;
import com.larpclient.LarpClient;
import com.larpclient.RoundedShapePipeline;
import com.larpclient.TextDrawing;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

public final class UpdateScreen extends Screen {
   private final Screen parent;
   private UpdateLayout.Dialog layout;
   private ButtonWidget primary;
   private ButtonWidget secondary;
   private ButtonWidget skip;
   private ButtonWidget automatic;
   private int scroll;
   private List<OrderedText> lines = List.of();
   private String renderedNotes = "";
   private final UpdateConsentKeys consentKeys = new UpdateConsentKeys();

   public UpdateScreen(Screen var1) {
      super(Text.literal("Client updates"));
      this.parent = var1;
   }

   protected void init() {
      this.consentKeys.begin(var1x -> InputUtil.isKeyPressed(this.client.getWindow(), var1x));
      this.layout = UpdateLayout.dialog(this.width, this.height);
      int var1 = this.layout.x() + 12;
      int var2 = this.layout.width() - 24;
      int var3 = this.layout.actionsY();
      int var5 = Math.max(1, (var2 - 6) / 2);
      this.primary = this.button("Check for updates", var1, var3, var5, 22, this::primary);
      this.secondary = this.button("Official downloads", var1 + var5 + 6, var3, Math.max(1, var2 - var5 - 6), 22, this::secondary);
      this.skip = this.button("Skip this release", var1, var3 + 24, var5, 22, () -> {
         if (UpdateRuntime.skip()) {
            this.close();
         }
      });
      this.button("Later", var1 + var5 + 6, var3 + 24, Math.max(1, var2 - var5 - 6), 22, this::close);
      this.automatic = this.button("Automatic checks", var1, var3 + 48, Math.max(1, var2), 20, () -> {
         ClientPreferences var0 = LarpClient.CAsLarpClient().fAsBo().yAsN7();
         UpdateRuntime.automatic(!var0.S);
      });
      this.sync();
   }

   private ButtonWidget button(String var1, int var2, int var3, int var4, int var5, Runnable var6) {
      return (ButtonWidget)this.addDrawableChild(
         new ButtonWidget(var2, var3, var4, var5, Text.literal(var1), var1x -> var6.run(), var0 -> (MutableText)var0.get()) {
            public void onClick(Click click, boolean doubled) {
               if (!doubled) {
                  super.onClick(click, false);
               }
            }

            protected void drawIcon(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
               boolean var5x = this.isSelected();
               int var6x = this.active ? (var5x ? -13224394 : -14474461) : -15132391;
               RoundedShapePipeline.nAsVoid(context, (float)this.getX(), (float)this.getY(), (float)this.getWidth(), (float)this.getHeight(), 3.0F, var6x);
               if (this.isFocused()) {
                  GuiDrawing.dAsVoid(context, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 3);
               }

               TextDrawing.uAsVoid(
                  context,
                  TextDrawing.LAsString(this.getMessage().getString(), this.getWidth() - 12),
                  this.getX() + this.getWidth() / 2,
                  this.getY() + (this.getHeight() - 9) / 2,
                  this.active ? -1842205 : -8947849
               );
            }
         }
      );
   }

   private void primary() {
      UpdateService var1 = UpdateRuntime.service();
      if (var1 == null) {
         UpdateRuntime.officialDownloads();
      } else {
         switch (var1.snapshot().state()) {
            case AVAILABLE:
               if (var1.snapshot().installable()) {
                  var1.download();
               } else {
                  UpdateRuntime.officialDownloads();
               }
               break;
            case READY:
               var1.queue();
               break;
            case DOWNLOADING:
            case QUEUED:
               var1.cancel();
               break;
            default:
               var1.check();
         }
      }
   }

   private void secondary() {
      UpdateService var1 = UpdateRuntime.service();
      if (var1 != null && var1.snapshot().state() == UpdateService.State.AVAILABLE) {
         var1.check();
      } else if (var1 != null && (var1.snapshot().state() == UpdateService.State.READY || var1.snapshot().state() == UpdateService.State.ERROR)) {
         var1.cancel();
      } else {
         UpdateRuntime.officialDownloads();
      }
   }

   private void sync() {
      UpdateService var1 = UpdateRuntime.service();
      UpdateService.Snapshot var2 = var1 == null ? null : var1.snapshot();
      if (var2 == null) {
         this.primary.setMessage(Text.literal("Official downloads"));
         this.primary.active = true;
      } else {
         String var3 = switch (var2.state()) {
            case AVAILABLE -> var2.installable() ? "Update" : "Official download";
            case READY -> "Update";
            case DOWNLOADING -> "Cancel download";
            case QUEUED -> "Cancel update";
            case STARTING -> "Preparing updates";
            case CHECKING -> "Checking updates";
            case CANCELLING -> "Cancelling update";
            default -> "Check for updates";
         };
         this.primary.setMessage(Text.literal(var3));
         this.primary.active = !var2.busy() || var2.state() == UpdateService.State.DOWNLOADING;
      }

      this.secondary
         .setMessage(
            Text.literal(
               var2 != null && var2.state() == UpdateService.State.AVAILABLE
                  ? "Check again"
                  : (
                     var2 != null && var2.state() == UpdateService.State.READY
                        ? "Cancel download"
                        : (var2 != null && var2.state() == UpdateService.State.ERROR ? "Clear staged update" : "Official downloads")
                  )
            )
         );
      this.secondary.active = var2 == null || var2.state() != UpdateService.State.CANCELLING;
      this.skip.visible = var2 != null && var2.state() == UpdateService.State.AVAILABLE;
      this.skip.active = this.skip.visible;
      boolean var4 = LarpClient.CAsLarpClient().fAsBo().yAsN7().S;
      this.automatic.setMessage(Text.literal("Automatic checks: " + (var4 ? "On" : "Off")));
   }

   public void tick() {
      this.consentKeys.reconcile(var1 -> InputUtil.isKeyPressed(this.client.getWindow(), var1));
      this.sync();
   }

   public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
      this.sync();
      int var5 = this.layout.x();
      int var6 = this.layout.y();
      int var7 = this.layout.width();
      RoundedShapePipeline.nAsVoid(context, (float)var5, (float)var6, (float)var7, (float)this.layout.height(), 7.0F, -182904551);
      TextDrawing.oAsVoid(context, "Client updates", var5 + 14, var6 + 13, -1447447, TextDrawing.Type_x.SEMIBOLD);
      UpdateService var8 = UpdateRuntime.service();
      UpdateService.Snapshot var9 = var8 == null ? null : var8.snapshot();
      String var10 = var8 == null
         ? "Larp Client"
         : var8.identity().edition() + " / Minecraft " + var8.identity().minecraft() + " / " + var8.identity().channel();
      TextDrawing.CAsVoid(context, TextDrawing.LAsString(var10, var7 - 28), var5 + 14, var6 + 29, -5592406);
      String var11 = var8 == null ? "" : "Installed " + var8.identity().version() + (var9.release() == null ? "" : "  |  Release " + var9.release().version());
      TextDrawing.CAsVoid(context, TextDrawing.LAsString(var11, var7 - 28), var5 + 14, var6 + 45, -3487030);
      String var12 = this.notes(var8, var9);
      if (!var12.equals(this.renderedNotes)) {
         this.renderedNotes = var12;
         this.lines = this.client.textRenderer.wrapLines(TextDrawing.ZAsText(var12, TextDrawing.Type_x.REGULAR), Math.max(1, var7 - 36));
         this.scroll = 0;
      }

      int var13 = Math.max(0, this.lines.size() * 11 - this.layout.textHeight());
      this.scroll = Math.clamp((long)this.scroll, 0, var13);
      context.enableScissor(var5 + 12, this.layout.textTop(), var5 + var7 - 12, this.layout.textBottom());

      try {
         for (int var14 = Math.max(0, this.scroll / 11);
            var14 < this.lines.size() && this.layout.textTop() + var14 * 11 - this.scroll < this.layout.textBottom();
            var14++
         ) {
            context.drawText(this.client.textRenderer, this.lines.get(var14), var5 + 16, this.layout.textTop() + var14 * 11 - this.scroll, -4473925, false);
         }
      } finally {
         context.disableScissor();
      }

      if (var13 > 0) {
         int var19 = this.layout.textHeight();
         int var15 = Math.max(12, var19 * var19 / (this.lines.size() * 11));
         int var16 = this.layout.textTop() + (var19 - var15) * this.scroll / var13;
         context.fill(var5 + var7 - 10, var16, var5 + var7 - 8, var16 + var15, -8947849);
      }

      if (var9 != null && var9.state() == UpdateService.State.DOWNLOADING && var9.release() != null) {
         int var20 = (int)((double)(var7 - 28) * Math.min(1.0, (double)var9.downloaded() / (double)var9.release().size()));
         context.fill(var5 + 14, this.layout.actionsY() - 8, var5 + var7 - 14, this.layout.actionsY() - 5, -13092808);
         context.fill(var5 + 14, this.layout.actionsY() - 8, var5 + 14 + var20, this.layout.actionsY() - 5, -3552823);
      }

      super.render(context, mouseX, mouseY, deltaTicks);
   }

   private String notes(UpdateService var1, UpdateService.Snapshot var2) {
      if (var1 == null) {
         return UpdateRuntime.unavailable();
      } else {
         StringBuilder var3 = new StringBuilder(var2.message());
         if (!var2.installedNotice().isEmpty()) {
            var3.append("\n\nSuccessfully loaded this installed build: ").append(var1.identity().version()).append('.');
         }

         if (var2.release() != null) {
            UpdateManifest.Release var4 = var2.release();
            var3.append("\n\n").append(var4.title()).append("\n").append(var4.summary());

            for (String var6 : var4.notes()) {
               var3.append("\n\n• ").append(var6);
            }

            var3.append("\n\n")
               .append(String.format(Locale.ROOT, "%.1f MiB", (double)var4.size() / 1048576.0))
               .append(" / Java ")
               .append(var4.minimumJava())
               .append("+ / Fabric ")
               .append(var4.minimumLoader())
               .append("+");
         }

         if (!var2.installable()) {
            var3.append(
               "\n\nThis client can update itself on Windows and Linux when its JAR is directly in the instance'SkyChunkCache mods folder. Otherwise, use the official download page and replace the mod after Minecraft closes."
            );
         } else {
            var3.append("\n\nUpdate downloads and verifies this exact signed release. It installs when you close Minecraft, and the approval lasts 24 hours.");
         }

         if (var2.checkedAt() > 0L) {
            var3.append("\n\nLast successful check: ").append(Instant.ofEpochSecond(var2.checkedAt())).append('.');
         }

         if (var2.cached()) {
            var3.append("\n\nCached release details. An expired manifest cannot authorize installation.");
         }

         return var3.toString();
      }
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      if (mouseX >= (double)(this.layout.x() + 12)
         && mouseX < (double)(this.layout.x() + this.layout.width() - 12)
         && mouseY >= (double)this.layout.textTop()
         && mouseY < (double)this.layout.textBottom()) {
         this.scroll = Math.clamp((long)(this.scroll - (int)(verticalAmount * 22.0)), 0, Math.max(0, this.lines.size() * 11 - this.layout.textHeight()));
         return true;
      } else {
         return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
      }
   }

   public boolean keyPressed(KeyInput input) {
      int var2 = input.key();
      if (var2 == 256) {
         this.close();
         return true;
      } else if (var2 != 267 && var2 != 266) {
         return !this.consentKeys.press(var2) ? true : super.keyPressed(input);
      } else {
         this.scroll = Math.clamp(
            (long)(this.scroll + (var2 == 267 ? 1 : -1) * this.layout.textHeight()), 0, Math.max(0, this.lines.size() * 11 - this.layout.textHeight())
         );
         return true;
      }
   }

   public boolean keyReleased(KeyInput input) {
      this.consentKeys.release(input.key());
      return super.keyReleased(input);
   }

   public void close() {
      UpdateRuntime.later();
      this.client.setScreen(this.parent);
   }

   public boolean shouldPause() {
      return false;
   }
}
