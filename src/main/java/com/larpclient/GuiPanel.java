package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;

public abstract class GuiPanel extends Screen {
   protected final List<GuiWidget> Q = new ArrayList<>();
   protected GuiWidget b;
   protected GuiWidget t;

   protected GuiPanel(String var1) {
      super(Text.literal(var1));
   }

   protected void yAsVoid() {
      for (GuiWidget var2 : this.Q) {
         var2.hAsVoid();
      }

      this.Q.clear();
      this.b = this.t = null;
   }

   protected boolean accepts(GuiWidget var1, double var2, double var4) {
      return var1.contains(var2, var4);
   }

   public boolean mouseClicked(Click click, boolean doubled) {
      if (this.b != null && this.b.pAsBoolean(click.button())) {
         return true;
      } else {
         for (GuiWidget var4 : this.Q) {
            if (this.accepts(var4, click.x(), click.y())) {
               if (this.b != null && this.b != var4) {
                  this.b.hAsVoid();
               }

               this.b = var4;
               var4.y = true;
               var4.Q = false;
               if (var4.click(click.x(), click.y(), click.button())) {
                  this.t = var4;
                  return true;
               }
            }
         }

         if (this.b != null) {
            this.b.hAsVoid();
         }

         this.b = null;
         return false;
      }
   }

   public boolean mouseDragged(Click click, double offsetX, double offsetY) {
      if (this.t != null) {
         this.t.drag(click.x(), click.y());
         return true;
      } else {
         return false;
      }
   }

   public boolean mouseReleased(Click click) {
      if (this.t != null) {
         this.t.release();
      }

      this.t = null;
      return true;
   }

   public boolean keyPressed(KeyInput input) {
      if (input.key() != 258) {
         return this.b != null && this.b.key(input.key()) ? true : super.keyPressed(input);
      } else {
         int var2 = (input.modifiers() & 1) != 0 ? -1 : 1;
         int var3 = this.b == null ? (var2 > 0 ? -1 : 0) : this.Q.indexOf(this.b);
         if (this.b != null) {
            this.b.hAsVoid();
         }

         for (int var4 = 0; var4 < this.Q.size(); var4++) {
            var3 = Math.floorMod(var3 + var2, this.Q.size());
            GuiWidget var5 = this.Q.get(var3);
            if (var5.B && var5.Y) {
               this.b = var5;
               var5.y = true;
               var5.Q = true;
               this.focusChanged(var5);
               break;
            }
         }

         return true;
      }
   }

   protected void focusChanged(GuiWidget var1) {
   }

   public boolean charTyped(CharInput input) {
      return this.b != null && this.b.character(input.codepoint());
   }

   public boolean shouldPause() {
      return false;
   }
}
