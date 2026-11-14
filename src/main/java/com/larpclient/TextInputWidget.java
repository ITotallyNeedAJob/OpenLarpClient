package com.larpclient;

import com.larpclient.module.GUIAnimation;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

public final class TextInputWidget extends GuiWidget {
   public String W = "";
   private final String D;
   private final int f;
   private boolean X;
   private final w2 AuctionNotifier = new w2(0.0);
   private final dn n = new dn();
   private final Consumer<String> w;
   private Runnable I;
   private Function<String, List<String>> d;
   private List<String> R = List.of();
   private String l;
   private int E;
   private boolean H;
   private boolean S;
   public String BloomUtility = "";
   public int P = -1;

   public TextInputWidget OAsQq() {
      this.S = true;
      return this;
   }

   public String pAsString() {
      return this.S ? "*".repeat(this.W.codePointCount(0, this.W.length())) : this.W;
   }

   public TextInputWidget AAsQq(Function<String, List<String>> var1) {
      this.d = var1;
      this.l = null;
      return this;
   }

   public String dAsString() {
      if (!this.S && this.d != null && !this.W.isBlank()) {
         if (!this.W.equals(this.l)) {
            this.l = this.W;
            this.E = 0;
            this.H = false;
            this.R = this.d.apply(this.W).stream().filter(var1 -> var1.length() <= this.f).filter(var1 -> !var1.equalsIgnoreCase(this.W)).limit(6L).toList();
         }

         return !this.H && !this.R.isEmpty() ? this.R.get(this.E) : "";
      } else {
         return "";
      }
   }

   public boolean yAsBoolean() {
      if (!this.y) {
         return false;
      } else {
         String var1 = this.dAsString();
         if (var1.isEmpty()) {
            return false;
         } else {
            this.W = var1;
            this.X = false;
            this.vAsVoid();
            this.H = true;
            this.l = this.W;
            return true;
         }
      }
   }

   private void vAsVoid() {
      this.l = null;
      this.H = false;
      this.w.accept(this.W);
   }

   private boolean lAsBoolean(int var1) {
      if (var1 == 262) {
         return this.yAsBoolean();
      } else if ((var1 == 264 || var1 == 265 || var1 == 256) && !this.dAsString().isEmpty()) {
         if (var1 == 256) {
            this.H = true;
         } else {
            this.E = Math.floorMod(this.E + (var1 == 265 ? -1 : 1), this.R.size());
         }

         return true;
      } else {
         return false;
      }
   }

   public void UAsVoid() {
      this.X = true;
   }

   public TextInputWidget MAsQq(Runnable var1) {
      this.I = var1;
      return this;
   }

   public TextInputWidget(String var1, Consumer<String> var2) {
      this(var1, var2, 64);
   }

   public TextInputWidget(String var1, Consumer<String> var2, int var3) {
      this.f = var3;
      this.D = var1;
      this.w = var2;
   }

   @Override
   public void render(DrawContext var1, int var2, int var3) {
      double var4 = this.o.bAsDouble(this.y ? 1.0 : 0.0, 180);
      if (this.P < 0) {
         GuiDrawing.TAsVoid(var1, this.k, this.x, this.V, this.F, var4);
      } else {
         GuiDrawing.BAsVoid(var1, this.k, this.x, this.V, this.F, this.P, var4);
      }

      GUIAnimation var6 = GUIAnimation.kAsGUIAnimation();
      String var7 = this.pAsString();
      this.n.zAsVoid(var7, System.nanoTime(), !this.S && this.y && var6 != null && var6.HAsBoolean());
      int var8 = this.x + (this.F - 9) / 2;
      int var9 = this.b.isEmpty() ? 6 : 26;
      if (!this.b.isEmpty()) {
         GuiIcons.aAsVoid(var1, GuiIcons.PAsIdentifier(this.b), this.k + 9, this.x + (this.F - 12) / 2, 12, bg.xAsInt(bd.W, bd.u, var4 * 0.6));
      }

      var1.enableScissor(this.k + var9, this.x + 2, this.k + this.V - 6, this.x + this.F - 2);

      try {
         if (this.W.isEmpty()) {
            TextDrawing.UAsVoid(var1, TextDrawing.HAsString(this.D, this.V - var9 - 6), this.k + var9, var8, bd.W);
         } else {
            int var10 = TextDrawing.UAsInt(var7);
            int var11 = this.k + var9 - Math.max(0, var10 + (this.y ? TextDrawing.UAsInt("_") : 0) - (this.V - var9 - 6));
            if (!this.n.BAsBoolean()) {
               TextDrawing.UAsVoid(var1, var7, var11, var8, bd.u);
            } else {
               int var12 = 0;

               while (var12 < var7.length()) {
                  int var13 = var7.codePointAt(var12);
                  String var14 = new String(Character.toChars(var13));
                  int var15 = var11 + TextDrawing.UAsInt(var7.substring(0, var12));
                  if (var15 + TextDrawing.UAsInt(var14) >= this.k + var9 && var15 < this.k + this.V - 6) {
                     var1.getMatrices().pushMatrix();
                     var1.getMatrices().translate(0.0F, (float)this.n.WAsDouble(var12));

                     try {
                        TextDrawing.UAsVoid(var1, var14, var15, var8, this.n.gAsInt(bd.u, var12));
                     } finally {
                        var1.getMatrices().popMatrix();
                     }
                  }

                  var12 += Character.charCount(var13);
               }
            }

            if (this.y) {
               String var24 = this.dAsString();
               if (!var24.isEmpty()) {
                  if (var24.regionMatches(true, 0, this.W, 0, this.W.length())) {
                     TextDrawing.UAsVoid(var1, var24.substring(this.W.length()), var11 + var10, var8, bg.pAsInt(bd.e, 0.65));
                  } else {
                     String var25 = "Tab: " + var24;
                     int var26 = TextDrawing.jAsInt(var25, TextDrawing.Type_s.HELP);
                     if (var10 + var26 + 18 <= this.V) {
                        TextDrawing.NAsVoid(var1, var25, this.k + this.V - var26 - 6, var8, bd.e, TextDrawing.Type_s.HELP);
                     }
                  }
               }

               GuiTextureDrawing.CAsVoid(var1, var11 + var10, var8, 1, 9, bd.u);
            }
         }
      } finally {
         var1.disableScissor();
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      this.X = false;
      this.y = var5 == 0 && this.contains(var1, var3);
      return this.y;
   }

   @Override
   public boolean character(int var1) {
      if (this.y && !Character.isISOControl(var1)) {
         if (this.X) {
            this.W = "";
            this.X = false;
         }

         if (this.W.length() + Character.charCount(var1) > this.f) {
            return false;
         } else {
            this.W = this.W + new String(Character.toChars(var1));
            this.vAsVoid();
            GuiSounds.rAsVoid(GuiSounds.Type_o.TYPING);
            return true;
         }
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (!this.y) {
         return false;
      } else if (this.lAsBoolean(var1)) {
         return true;
      } else if ((var1 == 257 || var1 == 335) && this.I != null) {
         this.I.run();
         return true;
      } else {
         MinecraftClient var2 = MinecraftClient.getInstance();
         long var3 = var2.getWindow().getHandle();
         int var5 = GLFW.glfwGetKey(var3, 341) != 1 && GLFW.glfwGetKey(var3, 345) != 1 ? 0 : 1;
         if (var5 != 0 && var1 == 86) {
            this.HAsVoid(var2.keyboard.getClipboard());
         } else if (var5 != 0 && var1 == 65) {
            this.X = true;
         } else if (var1 == 259 && !this.W.isEmpty()) {
            GuiSounds.rAsVoid(GuiSounds.Type_o.TYPING);
            if (this.X) {
               this.W = "";
               this.X = false;
               this.vAsVoid();
               return true;
            }

            this.W = this.W.substring(0, this.W.offsetByCodePoints(this.W.length(), -1));
            this.vAsVoid();
         } else if (var1 == 256) {
            this.y = false;
         }

         return true;
      }
   }

   public void HAsVoid(String var1) {
      if (this.y && var1 != null) {
         String var2 = var1.replaceAll("[\\p{Cntrl}]", " ");
         if (this.X) {
            this.W = "";
            this.X = false;
         }

         int var3 = Math.min(var2.length(), Math.max(0, this.f - this.W.length()));
         if (var3 > 0 && var3 < var2.length() && Character.isHighSurrogate(var2.charAt(var3 - 1)) && Character.isLowSurrogate(var2.charAt(var3))) {
            var3--;
         }

         this.W = this.W + var2.substring(0, var3);
         this.vAsVoid();
         GuiSounds.rAsVoid(GuiSounds.Type_o.TYPING);
      }
   }
}
