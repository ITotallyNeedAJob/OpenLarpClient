package com.larpclient;

import com.larpclient.update.UpdateLayout;
import com.larpclient.update.UpdateRuntime;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.cursor.StandardCursors;
import net.minecraft.client.gui.navigation.GuiNavigationType;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

public final class ClientTitleScreen extends Screen implements xs {
   private static final nc.Type_c W = new nc.Type_c();
   private static final w9.Type_f N = new w9.Type_f();
   private static final String Z = "Larp Client";
   private final nc D = W.cAsNc();
   private final List<AnimatedButton> k = new ArrayList<>();
   private final List<ClientTitleScreen.Type_c> r = new ArrayList<>();
   private RootMenuLayout b;
   private AnimatedButton o;
   private AnimatedButton Y;
   private AnimatedButton X;
   private AnimatedButton P;
   private AnimatedButton u;
   private UpdateLayout.Notice g;
   private boolean e;
   private boolean m;
   private nc.Type_j G;
   private final AccountSkinPreview f = new AccountSkinPreview();
   private final AccountWalkOnAnimation S = new AccountWalkOnAnimation(N.GAsW9());
   private final AccountPopupController R = new AccountPopupController(this);

   public ClientTitleScreen() {
      super(Text.literal("Larp Client"));
   }

   public static boolean QAsBoolean() {
      LarpClient var0 = LarpClient.CAsLarpClient();
      return var0 == null || var0.fAsBo() == null || var0.fAsBo().yAsN7().l;
   }

   protected void init() {
      this.b = RootMenuLayout.calculate(this.width, this.height, TextDrawing.qAsInt("Larp Client", TextDrawing.Type_x.SEMIBOLD));
      this.aAsVoid();
      this.k.clear();
      if (!this.dAsN7().N) {
         this.D.pAsVoid();
      }

      this.o = (AnimatedButton)this.addDrawableChild(
         new AnimatedButton(
            Text.translatable("menu.singleplayer"), AnimatedButton.Type_u.PRIMARY, () -> this.zAsVoid(new SelectWorldScreen(this)), this.b.navigation(0)
         )
      );
      this.addDrawableChild(
         new AnimatedButton(
            Text.translatable("menu.multiplayer"), AnimatedButton.Type_u.PRIMARY, () -> this.zAsVoid(new MultiplayerScreen(this)), this.b.navigation(1)
         )
      );
      this.addDrawableChild(
         new AnimatedButton(
            Text.literal("Minecraft Settings"),
            AnimatedButton.Type_u.SECONDARY,
            () -> this.zAsVoid(new OptionsScreen(this, this.client.options)),
            this.b.navigation(2)
         )
      );
      this.addDrawableChild(
         new AnimatedButton(
            Text.literal("Client"), AnimatedButton.Type_u.SECONDARY, () -> this.zAsVoid(ModuleSearchPanel.MAsScreen(this)), this.b.navigation(3)
         )
      );
      this.addDrawableChild(new AnimatedButton(Text.translatable("menu.quit"), AnimatedButton.Type_u.QUIET, this.client::scheduleStop, this.b.navigation(4)));
      w9.Type_i var1 = w9.UAsW9_Type_i(this.b);
      this.X = (AnimatedButton)this.addDrawableChild(
         new AnimatedButton(
            Text.literal("Switch Microsoft account: " + this.client.getSession().getUsername()),
            AnimatedButton.Type_u.SESSION,
            this.R::jAsVoid,
            var1.visible() ? var1.name() : this.b.account()
         )
      );
      this.R.fAsVoid(new MenuLayout.Type_c(this.X.getX(), this.X.getY(), this.X.getWidth(), this.X.getHeight()));
      this.X.C = var1x -> {
         String var2 = TextDrawing.sAsString(this.client.getSession().getUsername(), this.X.getWidth() - 32, TextDrawing.Type_x.SEMIBOLD);
         int var3x = TextDrawing.qAsInt(var2, TextDrawing.Type_x.SEMIBOLD);
         int var4x = this.X.getX() + (this.X.getWidth() - var3x - 14) / 2;
         TextDrawing.oAsVoid(var1x, var2, var4x, this.X.getY() + 4, bg.pAsInt(-1513240, this.X.eAsDouble()), TextDrawing.Type_x.SEMIBOLD);
         GuiIcons.aAsVoid(
            var1x,
            GuiIcons.PAsIdentifier(this.R.yAsBoolean() ? "chevron-up" : "chevron-down"),
            var4x + var3x + 5,
            this.X.getY() + 4,
            9,
            bg.pAsInt(-5592406, this.X.eAsDouble())
         );
         double var5 = this.X.HAsDouble();
         AnimatedButton.LAsVoid(
            var1x,
            (float)this.X.getX() + (float)this.X.getWidth() / 2.0F,
            this.X.getY() + 17,
            (float)(20.0 + (double)(var3x + 14 - 20) * var5),
            bg.pAsInt(bg.xAsInt(-11447983, -4342339, var5), this.X.eAsDouble())
         );
      };

      for (Element var3 : this.children()) {
         if (var3 instanceof AnimatedButton) {
            AnimatedButton var4 = (AnimatedButton)var3;
            if (var4 != this.X) {
               this.k.add(var4);
            }
         }
      }

      this.Y = (AnimatedButton)this.addDrawableChild(
         new AnimatedButton(Text.literal("Press to enter"), AnimatedButton.Type_u.PRIMARY, this::oAsVoid, nc.FAsD8_Type_c(this.width, this.height))
      );
      this.g = UpdateLayout.notice(this.width, this.height);
      this.P = (AnimatedButton)this.addDrawableChild(
         new AnimatedButton(Text.literal("View update"), AnimatedButton.Type_u.SECONDARY, () -> UpdateRuntime.open(this), this.g.open())
      );
      this.u = (AnimatedButton)this.addDrawableChild(
         new AnimatedButton(Text.literal("Later"), AnimatedButton.Type_u.QUIET, UpdateRuntime::later, this.g.later())
      );
      this.WAsVoid();
      ClientLog.XAsVoid(
         "Menu", "Text navigation initialized at {}x{}; centered labels, subtle selection markers, direct Minecraft options", this.width, this.height
      );
   }

   protected void setInitialFocus() {
      if (!this.D.wAsBoolean()) {
         if (this.D.LAsBoolean()) {
            this.setInitialFocus(this.Y);
         } else {
            this.blur();
         }
      } else if (this.client.getNavigationType().isKeyboard()) {
         this.setInitialFocus(this.o);
      } else {
         this.blur();
      }
   }

   public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
      this.ZAsNc();
      nc.Type_a var5 = this.D.TAsNc_Type_a(this.b, this.VAsBoolean());
      if (this.dAsN7().O && this.VAsBoolean()) {
         this.RAsVoid(context);
      }

      context.createNewRootLayer();
      nc.Type_h var6 = this.D.WAsNc_Type_h(this.b, this.VAsBoolean());
      if (var6.alpha() > 0.0) {
         this.fAsVoid(context, var6);
         context.createNewRootLayer();
      }

      context.getMatrices().pushMatrix();

      try {
         context.getMatrices().translate((float)var5.x(), (float)var5.y());
         context.getMatrices().rotate((float)var5.rotation());
         context.getMatrices().scale((float)(var5.size() / 64.0), (float)(var5.size() / 64.0 * var5.stretch()));
         context.drawTexture(RenderPipelines.GUI_TEXTURED, MenuLogoAssets.oAsIdentifier(), -32, -32, 0.0F, 0.0F, 64, 64, 64, 64, 64, 64, -855310);
      } finally {
         context.getMatrices().popMatrix();
      }

      nc.Type_q var7 = this.D.BAsNc_Type_q(this.VAsBoolean());
      if (!this.D.wAsBoolean() && var7.alpha() > 0.0) {
         this.DAsVoid(
            context,
            (float)this.width / 2.0F,
            (float)((double)this.height * 0.48 + Math.clamp((double)this.height * 0.16, 30.0, 86.0) / 2.0 + 14.0),
            Math.min(1.65F, this.b.brandScale() + 0.15F),
            var7.alpha()
         );
      }

      this.S.lAsVoid(context, this.b, this.D, this.f, mouseX, mouseY, this.VAsBoolean(), this.dAsN7().N);
      this.RAsVoid();
      context.createNewRootLayer();
      this.VAsVoid(context);
      if (!this.R.yAsBoolean() && this.hAsBoolean() && this.ZAsBoolean((double)mouseX, (double)mouseY)) {
         context.setCursor(StandardCursors.POINTING_HAND);
      }

      super.render(
         context,
         !this.R.yAsBoolean() && (this.D.wAsBoolean() || this.D.LAsBoolean()) ? mouseX : -1,
         !this.R.yAsBoolean() && (this.D.wAsBoolean() || this.D.LAsBoolean()) ? mouseY : -1,
         deltaTicks
      );
      this.R.WAsVoid(context, mouseX, mouseY);
   }

   private void VAsVoid(DrawContext var1) {
      if (this.P != null && this.P.visible) {
         UpdateRuntime.noticeShown();
         if (!this.g.compact()) {
            MenuLayout.Type_c var2 = this.g.surface();
            RoundedShapePipeline.nAsVoid(var1, (float)var2.x(), (float)var2.y(), (float)var2.width(), (float)var2.height(), 5.0F, -451208421);
            TextDrawing.oAsVoid(
               var1, TextDrawing.LAsString(UpdateRuntime.noticeTitle(), var2.width() - 16), var2.x() + 8, var2.y() + 9, -2171170, TextDrawing.Type_x.MEDIUM
            );
            String var3 = UpdateRuntime.noticeDetail();
            TextDrawing.CAsVoid(var1, TextDrawing.LAsString(var3, var2.width() - 16), var2.x() + 8, var2.y() + 25, -6513508);
         }
      }
   }

   private void DAsVoid(DrawContext var1, float var2, float var3, float var4, double var5) {
      var1.getMatrices().pushMatrix();

      try {
         var1.getMatrices().translate(var2, var3);
         var1.getMatrices().scale(var4, var4);
         TextDrawing.oAsVoid(
            var1, "Larp Client", -TextDrawing.qAsInt("Larp Client", TextDrawing.Type_x.SEMIBOLD) / 2, 0, bg.pAsInt(-1644826, var5), TextDrawing.Type_x.SEMIBOLD
         );
      } finally {
         var1.getMatrices().popMatrix();
      }
   }

   private void fAsVoid(DrawContext var1, nc.Type_h var2) {
      MenuLayout.Type_c var3 = this.b.title();
      float var4 = this.b.brandScale();
      float var5 = (float)((double)((float)var3.y() + ((float)var3.height() - 9.0F * var4) / 2.0F) + var2.lift());
      int var6 = bg.pAsInt(-1644826, var2.alpha());
      if (!(var2.clip() <= var2.x()) && !this.r.isEmpty()) {
         for (ClientTitleScreen.Type_c var8 : this.r) {
            double var9 = var2.x() + (double)(var8.x() * var4);
            double var11 = (double)Math.max(1.0F, var8.width() * var4);
            double var13 = Math.clamp((var9 + var11 - var2.clip()) / var11, 0.0, 1.0);
            int var15 = qg.iAsInt(bg.pAsInt(var6, var13 * var13 * (3.0 - 2.0 * var13)));
            if (var15 >>> 24 >= 4) {
               var1.getMatrices().pushMatrix();

               try {
                  var1.getMatrices().translate((float)var9, var5);
                  var1.getMatrices().scale(var4, var4);
                  var1.drawText(this.client.textRenderer, var8.text(), 0, 0, var15, false);
               } finally {
                  var1.getMatrices().popMatrix();
               }
            }
         }
      } else {
         var1.getMatrices().pushMatrix();

         try {
            var1.getMatrices().translate((float)var2.x(), var5);
            var1.getMatrices().scale(var4, var4);
            TextDrawing.oAsVoid(var1, "Larp Client", 0, 0, var6, TextDrawing.Type_x.SEMIBOLD);
         } finally {
            var1.getMatrices().popMatrix();
         }
      }
   }

   private void aAsVoid() {
      this.r.clear();
      TextRenderer var1 = this.client.textRenderer;
      float[] var2 = new float[]{0.0F};
      TextDrawing.ZAsText("Larp Client", TextDrawing.Type_x.SEMIBOLD).asOrderedText().accept((var3, var4, var5) -> {
         OrderedText var6 = OrderedText.styled(var5, var4);
         float var7 = var1.getTextHandler().getWidth(var6);
         this.r.add(new ClientTitleScreen.Type_c(var6, var2[0], var7));
         var2[0] += var7;
         return true;
      });
   }

   private void RAsVoid(DrawContext var1) {
      for (int var2 = 0; var2 < 12; var2++) {
         nc.Type_l var3 = this.D.oAsNc_Type_l(var2, this.b);
         if (!(var3.alpha() <= 0.0)) {
            var1.getMatrices().pushMatrix();

            try {
               var1.getMatrices().translate((float)var3.x(), (float)var3.y());
               var1.getMatrices().rotate((float)var3.rotation());
               float var4 = (float)(var3.size() / 64.0);
               var1.getMatrices().scale(var4, var4);
               var1.drawTexture(
                  RenderPipelines.GUI_TEXTURED, MenuLogoAssets.oAsIdentifier(), -32, -32, 0.0F, 0.0F, 64, 64, 64, 64, 64, 64, bg.XAsInt(-1513240, var3.alpha())
               );
            } finally {
               var1.getMatrices().popMatrix();
            }
         }
      }
   }

   private ClientPreferences dAsN7() {
      return LarpClient.CAsLarpClient().fAsBo().yAsN7();
   }

   private void zAsVoid(Screen var1) {
      if (var1 instanceof ClickGuiScreen var2) {
         var2.IAsVoid();
      } else if (var1 instanceof f var3) {
         var3.larpOpenMenuPopup();
      } else {
         ClientLog.oAsVoid("Menu", "Popup hook unavailable for {}; opening destination directly", var1.getClass().getSimpleName());
      }

      ClientLog.XAsVoid("Menu", "Opening {} with the shared {} ms popup", var1.getClass().getSimpleName(), 240);
      this.client.setScreen(var1);
   }

   private boolean VAsBoolean() {
      return this.dAsN7().Z && this.dAsN7().L != WorldTimePreset.Type_b.STATIC && (Double)this.client.options.getPanoramaSpeed().getValue() > 0.0;
   }

   public nc ZAsNc() {
      this.D.oAsVoid(var1x -> InputUtil.isKeyPressed(this.client.getWindow(), var1x));
      long var1 = System.nanoTime();
      int var3 = this.client.getOverlay() == null && this.client.isWindowFocused() ? 1 : 0;
      this.D.xAsVoid(var1, var3 != 0, this.VAsBoolean());
      this.WAsVoid();
      if (!this.m && this.client.getOverlay() == null) {
         this.m = true;
         ClientLog.GAsVoid("Menu", "Launch presentation: {}; Enter, Space or click to enter; another press skips motion", this.D.aAsNc_Type_j());
      }

      return this.D;
   }

   private void WAsVoid() {
      if (this.Y != null) {
         this.Y.visible = !this.D.wAsBoolean() && this.D.RAsNc_Type_q(this.VAsBoolean()).alpha() > 0.0;
         this.Y.active = this.D.LAsBoolean();
         this.Y.rAsVoid(this.D.RAsNc_Type_q(this.VAsBoolean()));

         for (int var1 = 0; var1 < this.k.size(); var1++) {
            AnimatedButton var2 = this.k.get(var1);
            nc.Type_q var3 = this.D.mAsNc_Type_q(var1);
            var2.rAsVoid(var3);
            var2.visible = var3.alpha() > 0.0;
            var2.active = this.D.wAsBoolean();
         }

         this.RAsVoid();
         if (this.P != null) {
            int var4 = this.D.wAsBoolean() && UpdateRuntime.notice() ? 1 : 0;
            this.P.visible = this.P.active = var4 != 0;
            this.u.visible = this.u.active = var4 != 0 && !this.g.compact();
            this.P.setMessage(Text.literal(this.g.compact() ? UpdateRuntime.noticeTitle() : UpdateRuntime.noticeAction()));
         }

         if (this.G != this.D.aAsNc_Type_j()) {
            nc.Type_j var5 = this.G;
            this.G = this.D.aAsNc_Type_j();
            this.setInitialFocus();
            if (this.D.wAsBoolean()) {
               ClientLog.GAsVoid("Menu", "Launch reveal settled; native navigation active");
            } else if (this.D.LAsBoolean() && var5 == nc.Type_j.RETURN) {
               ClientLog.GAsVoid("Menu", "Entrance replay reached the welcome pose; waiting for entry");
            }
         }
      }
   }

   private void RAsVoid() {
      if (this.X != null) {
         int var1 = this.D.wAsBoolean() && (!w9.UAsW9_Type_i(this.b).visible() || this.S.AAsDouble() == 1.0) ? 1 : 0;
         this.X.visible = this.X.active = var1 != 0;
         this.X.rAsVoid(nc.Type_q.REST);
         this.X.setMessage(Text.literal("Switch Microsoft account: " + this.client.getSession().getUsername()));
      }
   }

   private void oAsVoid() {
      this.ZAsNc();
      if (this.D.RAsBoolean(this.VAsBoolean())) {
         this.blur();
         this.WAsVoid();
         ClientLog.GAsVoid(
            "Menu", "Entry accepted; logo anticipation and side arc into the lockup, then the wordmark slides out from behind it while the menu staggers in"
         );
      }
   }

   private boolean ZAsBoolean(double var1, double var3) {
      MenuLayout.Type_c var5 = this.b.logo();
      MenuLayout.Type_c var6 = this.b.title();
      int var7 = Math.min(var5.x(), var6.x());
      int var8 = Math.min(var5.y(), var6.y());
      int var9 = Math.max(var5.x() + var5.width(), var6.x() + var6.width());
      int var10 = Math.max(var5.y() + var5.height(), var6.y() + var6.height());
      return var1 >= (double)var7 && var1 < (double)var9 && var3 >= (double)var8 && var3 < (double)var10;
   }

   private boolean hAsBoolean() {
      return this.D.wAsBoolean() && this.dAsN7().N;
   }

   private void zAsVoid() {
      this.ZAsNc();
      if (this.D.BAsBoolean(this.VAsBoolean())) {
         this.R.mAsVoid();
         this.blur();
         this.S.KAsVoid();
         this.WAsVoid();
         ClientLog.GAsVoid("Menu", "Logo clicked; rewinding the entrance to the welcome pose ({})", this.VAsBoolean() ? "animated" : "reduced motion");
      }
   }

   public boolean charTyped(CharInput input) {
      return this.R.KAsBoolean(input) || super.charTyped(input);
   }

   public boolean keyPressed(KeyInput input) {
      if (this.R.BAsBoolean(input)) {
         return true;
      } else {
         int var2 = input.key();
         if (this.D.ZAsBoolean(var2)) {
            return true;
         } else {
            if (!this.D.wAsBoolean()) {
               if (var2 == 257 || var2 == 335 || var2 == 32) {
                  this.client.setNavigationType(GuiNavigationType.KEYBOARD_TAB);
                  this.D.LAsBoolean(var2);
                  if (this.D.LAsBoolean()) {
                     this.Y.playDownSound(this.client.getSoundManager());
                     this.oAsVoid();
                  } else {
                     this.D.HAsVoid();
                     this.WAsVoid();
                  }

                  return true;
               }

               if (!this.D.LAsBoolean()) {
                  this.D.HAsVoid();
                  this.WAsVoid();
               }

               if (this.D.LAsBoolean()) {
                  return true;
               }
            }

            return super.keyPressed(input);
         }
      }
   }

   public boolean keyReleased(KeyInput input) {
      return this.D.WAsBoolean(input.key()) || super.keyReleased(input);
   }

   public boolean mouseClicked(Click click, boolean doubled) {
      if (this.R.zAsBoolean(click)) {
         return true;
      } else if (!this.D.wAsBoolean()) {
         if (click.button() == 0) {
            this.e = true;
            if (this.D.LAsBoolean()) {
               this.Y.playDownSound(this.client.getSoundManager());
               this.oAsVoid();
            } else {
               this.D.HAsVoid();
               this.WAsVoid();
            }
         }

         return true;
      } else if (super.mouseClicked(click, doubled)) {
         return true;
      } else if (click.button() == 0 && this.hAsBoolean() && this.ZAsBoolean(click.x(), click.y())) {
         this.e = true;
         this.Y.playDownSound(this.client.getSoundManager());
         this.zAsVoid();
         return true;
      } else {
         return false;
      }
   }

   public boolean mouseReleased(Click click) {
      if (this.R.yAsBoolean()) {
         return true;
      } else if (this.e && click.button() == 0) {
         this.e = false;
         return true;
      } else {
         return !this.D.wAsBoolean() || super.mouseReleased(click);
      }
   }

   public void onDisplayed() {
      super.onDisplayed();
      this.D.oAsVoid(var1 -> InputUtil.isKeyPressed(this.client.getWindow(), var1));
      if (this.D.LAsBoolean()) {
         for (int var4 : new int[]{257, 335, 32}) {
            if (InputUtil.isKeyPressed(this.client.getWindow(), var4)) {
               this.D.LAsBoolean(var4);
            }
         }
      }
   }

   public void removed() {
      this.R.mAsVoid();
      if (!this.D.LAsBoolean()) {
         this.D.HAsVoid();
      }

      this.S.hAsVoid(!this.D.LAsBoolean());
      super.removed();
   }

   public boolean shouldCloseOnEsc() {
      return false;
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      return this.R.dAsBoolean(mouseX, mouseY, verticalAmount) || super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
   }

   public boolean shouldPause() {
      return false;
   }

   private static record Type_c(OrderedText text, float x, float width) {
   }
}
