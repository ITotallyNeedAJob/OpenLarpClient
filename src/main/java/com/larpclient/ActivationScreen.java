package com.larpclient;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ActivationScreen extends Screen {
   private static final Identifier CropHelper = Identifier.of("larp-client", "textures/gui/logo.png");
   private final Screen b;
   private final ClientWorld j;
   private String T = "";
   private String X = "";
   private boolean m;
   private TextFieldWidget n;
   private ButtonWidget h;
   private ButtonWidget v;
   private ButtonWidget P;
   private final AccountPopupController AuctionNotifier = new AccountPopupController(this);
   private int t;
   private int z;
   private int s;
   private int Z;
   private int p;
   private int I;

   public ActivationScreen() {
      this(null);
   }

   public ActivationScreen(Screen var1) {
      super(Text.literal("Larp Client"));
      this.b = var1;
      this.j = MinecraftClient.getInstance().world;
      this.X = SessionManager.pAsString();
      String var2 = SessionManager.fAsString();
      if (var2 != null) {
         this.T = var2;
      }
   }

   protected void init() {
      this.SAsVoid();
   }

   public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
   }

   public boolean keyPressed(KeyInput input) {
      return false;
   }

   private void dAsVoid() {
   }

   public void tick() {
      this.SAsVoid();
   }

   static ActivationScreen.Type_k LAsPu_Type_k(Object var0, Object var1, boolean var2) {
      if (var2 && var0 == var1) {
         return ActivationScreen.Type_k.WORKSPACE;
      } else {
         return var1 != null ? ActivationScreen.Type_k.GAMEPLAY : ActivationScreen.Type_k.TITLE;
      }
   }

   private void SAsVoid() {
      MinecraftClient var1 = MinecraftClient.getInstance();
      if (SessionManager.HAsBoolean() && var1.currentScreen == this) {
         ActivationScreen.Type_k var2 = LAsPu_Type_k(this.j, var1.world, this.b != null);
         ClientLog.GAsVoid("Auth", "Authentication complete; returning to {}", var2);

         var1.setScreen((Screen)(switch (var2) {
            case WORKSPACE -> this.b;
            case GAMEPLAY -> null;
            case TITLE -> new TitleScreen();
         }));
      }
   }

   public boolean shouldPause() {
      return false;
   }

   public boolean shouldCloseOnEsc() {
      return false;
   }

   public boolean mouseClicked(Click click, boolean doubled) {
      return this.o.zAsBoolean(click) || super.mouseClicked(click, doubled);
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      return this.o.dAsBoolean(mouseX, mouseY, verticalAmount) || super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
   }

   public boolean mouseDragged(Click click, double offsetX, double offsetY) {
      return this.o.yAsBoolean() || super.mouseDragged(click, offsetX, offsetY);
   }

   public boolean mouseReleased(Click click) {
      return this.o.yAsBoolean() || super.mouseReleased(click);
   }

   public boolean charTyped(CharInput input) {
      return this.o.KAsBoolean(input) || super.charTyped(input);
   }

   public void removed() {
      this.o.mAsVoid();
      super.removed();
   }

   public static enum Type_k {
      WORKSPACE,
      GAMEPLAY,
      TITLE;

      private Type_k() {
      }
   }
}
