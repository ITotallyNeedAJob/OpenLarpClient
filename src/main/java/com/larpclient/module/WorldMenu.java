package com.larpclient.module;

import com.larpclient.ClickGuiScreen;
import com.larpclient.ClientLog;
import com.larpclient.GuiIcons;
import com.larpclient.GuiTextureDrawing;
import com.larpclient.HudEditorScreen;
import com.larpclient.UiStyle;
import com.larpclient.ColorUtil;
import com.larpclient.OverlayVideo;
import com.larpclient.QueueGroup;
import com.larpclient.WeatherRenderer;
import com.larpclient.WebRenderer;
import com.larpclient.XModel;
import com.larpclient.XTexture;
import java.util.Locale;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.input.MouseInput;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.Window;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import org.joml.Matrix4f;

public final class WorldMenu extends Module {
   private static WorldMenu n;
   private final ModeSetting placement = this.addSetting(
      new ModeSetting("Placement", "Pinned stays where you leave it and closes once you walk away; Follow moves along with you", "Pinned", "Pinned", "Follow")
   );
   private final NumberSetting closeDistance = this.addSetting(
      new NumberSetting("Close Distance", "A pinned menu closes once you are this many blocks from it", 15.0, 4.0, 48.0, 1.0)
   );
   private final NumberSetting menuHeight = this.addSetting(
      new NumberSetting("Menu Height", "Share of your view the menu fills when it opens in front of you, in percent", 50.0, 30.0, 90.0, 1.0)
   );
   private final NumberSetting panelSize = this.addSetting(new NumberSetting("Panel Size", "Height of the menu panel in blocks", 1.5, 0.5, 4.0, 0.1));
   private static final double c = 0.5;
   private static final double z = 16.0;
   private static final long l = 50000000L;
   private static final long b = 250L;
   private static final int A = 76;
   private static final int T = 6;
   private static final int C = 9;
   private static final int u = 16;
   private static final int e = 9;
   private static final int R = 16;
   private static final int D = 22;
   private OverlayVideo.Type_v f;
   private Object N;
   private ClickGuiScreen X;
   private ClickGuiScreen p;
   private Vec3d F;
   private boolean q;
   private double V;
   private double L;
   private int O;
   private double w;
   private double x;
   private long j;
   private int Y = -1;
   private WorldMenu.Type_d I;
   private final WeatherRenderer t = new WeatherRenderer(0.0);
   private final WeatherRenderer s = new WeatherRenderer(0.0);
   private double K = 1.0;
   private long o;

   public WorldMenu() {
      super("World Menu", "Opens the ClickGUI on a panel in the world while you keep playing", Category.RENDER, -1);
      n = this;
   }

   public static WorldMenu lAsWorldMenu() {
      return n;
   }

   @Override
   public String getDisplayName() {
      return "3D Click UI";
   }

   private boolean rAsBoolean() {
      MinecraftClient var1 = getMc();
      return this.isRuntimeActive() && var1.player != null && var1.world != null && var1.gameRenderer.getCamera().isReady();
   }

   private boolean PAsBoolean() {
      MinecraftClient var1 = getMc();
      return var1.currentScreen == null && var1.getOverlay() == null && var1.mouse.isCursorLocked();
   }

   private double oAsDouble(ClickGuiScreen var1) {
      double[] var2 = var1.TAsDoubleArray2();
      return this.panelSize.getValue() / Math.max(1.0, var2[3] - var2[1]);
   }

   private XTexture.Type_f xAsXt_Type_f(ClickGuiScreen var1, OverlayVideo.Type_v var2) {
      Camera var3 = getMc().gameRenderer.getCamera();
      Vec3d var4 = var3.getCameraPos();
      return OverlayVideo.AAsXt_Type_f(
         var2,
         this.oAsDouble(var1),
         (double)var1.width / 2.0,
         (double)var1.height / 2.0,
         var4.x,
         var4.y,
         var4.z,
         WebRenderer.pAsMatrix4f(new Matrix4f()),
         (double)var1.width,
         (double)var1.height
      );
   }

   private static double CAsDouble(ClickGuiScreen var0, XTexture.Type_f var1) {
      double[] var2 = var0.TAsDoubleArray2();
      double[] var3 = var1.bounds(var2[0], var2[1], var2[2], var2[3]);
      if (var3[0] == -10000.0) {
         return 0.0;
      } else {
         double var4 = (double)WebRenderer.pAsFloat();
         double var6 = (double)((Integer)getMc().options.getFov().getValue()).intValue();
         double var8 = var4 > 1.0 && var4 < 179.0 && var6 > 1.0 && var6 < 179.0
            ? Math.tan(Math.toRadians(var4) / 2.0) / Math.tan(Math.toRadians(var6) / 2.0)
            : 1.0;
         return (var3[3] - var3[1]) / (double)Math.max(1, var0.height) * var8;
      }
   }

   private double GAsDouble() {
      return this.placement.pAsBoolean("Pinned") ? Math.clamp(this.closeDistance.getValue() - 1.0, 0.5, 16.0) : 16.0;
   }

   public static boolean gAsBoolean(ClickGuiScreen var0) {
      if (n != null && n.isEnabled()) {
         WorldMenu var1 = n;
         return var1 != null && var1.mAsBoolean(var0);
      } else {
         return false;
      }
   }

   private boolean mAsBoolean(ClickGuiScreen var1) {
      if (!this.rAsBoolean()) {
         return false;
      } else if (var1 == this.X) {
         return true;
      } else if (var1.QAsScreen() != null) {
         return false;
      } else {
         if (this.X != null) {
            this.iAsVoid("a new window replaced it");
         }

         this.p = null;
         this.X = var1;
         this.KAsVoid();
         Window var2 = getMc().getWindow();
         var1.init(var2.getScaledWidth(), var2.getScaledHeight());
         this.mAsVoid(var1);
         return true;
      }
   }

   private void mAsVoid(ClickGuiScreen var1) {
      Camera var2 = getMc().gameRenderer.getCamera();
      Vec3d var3 = var2.getCameraPos();
      OverlayVideo.Type_v var4 = OverlayVideo.LAsOv_Type_v(var3.x, var3.y, var3.z, (double)var2.getYaw(), (double)var2.getPitch(), 1.0);
      double var5 = CAsDouble(var1, this.xAsXt_Type_f(var1, var4));
      double var7 = var5 > 0.0 ? Math.clamp(var5 / (this.menuHeight.getValue() / 100.0), 0.5, 16.0) : 2.0;
      this.f = OverlayVideo.LAsOv_Type_v(var3.x, var3.y, var3.z, (double)var2.getYaw(), (double)var2.getPitch(), var7);
      this.N = getMc().world;
      this.F = var3;
      this.t.aAsVoid(1.0);
      this.K = 1.0;
      this.o = 0L;
      ClientLog.XAsVoid(
         "GUI",
         "World Menu opened in the world, {} blocks ahead, yaw {}, pitch {}, {}",
         OAsString(var7),
         Math.round(this.f.yaw()),
         Math.round(this.f.pitch()),
         this.placement.pAsBoolean("Pinned") ? "closes beyond " + this.closeDistance.getValue().intValue() + " blocks" : "following"
      );
   }

   private double DAsDouble() {
      if (this.f == null) {
         return 0.0;
      } else {
         Vec3d var1 = getMc().gameRenderer.getCamera().getCameraPos();
         return this.f.distance(var1.x, var1.y, var1.z);
      }
   }

   private static String OAsString(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   public static boolean dAsBoolean() {
      if (n != null && n.isEnabled()) {
         WorldMenu var0 = n;
         return var0 != null && var0.X != null && var0.rAsBoolean();
      } else {
         return false;
      }
   }

   public static boolean nAsBoolean() {
      if (n != null && n.isEnabled()) {
         WorldMenu var0 = n;
         if (var0 != null && var0.X != null && var0.rAsBoolean()) {
            ClientLog.XAsVoid("GUI", "World Menu closed with the menu key");
            var0.X.close();
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean QAsBoolean(ClickGuiScreen var0) {
      if (n != null && n.isEnabled()) {
         WorldMenu var1 = n;
         return var1 != null && var1.hAsBoolean(var0);
      } else {
         return false;
      }
   }

   private boolean hAsBoolean(ClickGuiScreen var1) {
      if (var1 != null && var1 == this.X) {
         this.X = null;
         this.p = var1;
         this.KAsVoid();
         var1.removed();
         ClientLog.XAsVoid("GUI", "World Menu closing {} blocks away", OAsString(this.DAsDouble()));
         return true;
      } else {
         return false;
      }
   }

   private void iAsVoid(String var1) {
      ClickGuiScreen var2 = this.X;
      this.X = this.p = null;
      this.KAsVoid();
      this.f = null;
      this.N = null;
      this.F = null;
      if (var2 != null) {
         ClientLog.XAsVoid("GUI", "World Menu removed its panel ({})", var1);

         try {
            var2.removed();
         } catch (RuntimeException var4) {
            ClientLog.oAsVoid("GUI", "World Menu window cleanup failed after {}: {}", var1, var4.toString());
         }
      }
   }

   private void KAsVoid() {
      this.O = 0;
      this.I = null;
      this.q = false;
   }

   @Override
   public void onTick() {
      ClickGuiScreen var1 = this.X;
      if (var1 != null) {
         if (this.rAsBoolean() && this.N == getMc().world) {
            var1.tick();
            if (this.X == var1 && this.placement.pAsBoolean("Pinned") && this.I == null) {
               double var2 = this.DAsDouble();
               double var4;
               if (((var4 = var2 - this.closeDistance.getValue()) == 0.0 ? 0 : (var4 < 0.0 ? -1 : 1)) > 0) {
                  ClientLog.XAsVoid(
                     "GUI", "World Menu closed: you are {} blocks from it, past Close Distance {}", OAsString(var2), this.closeDistance.getValue().intValue()
                  );
                  var1.close();
               }
            }
         } else {
            this.iAsVoid("left the level");
         }
      }
   }

   public static void WAsVoid(DrawContext var0, float var1) {
      if (n != null && n.isEnabled()) {
         WorldMenu var2 = n;
         if (var2 != null) {
            var2.iAsVoid(var0, var1);
         }
      }
   }

   private void iAsVoid(DrawContext var1, float var2) {
      this.q = false;
      ClickGuiScreen var3 = this.X != null ? this.X : this.p;
      if (var3 != null) {
         MinecraftClient var4 = getMc();
         if (!this.rAsBoolean() || this.f == null || this.N != var4.world) {
            this.iAsVoid(this.rAsBoolean() ? "left the level" : "unavailable");
         } else if (var3 == this.p && var3.pAsBoolean()) {
            this.p = null;
         } else {
            if (var3.width != var1.getScaledWindowWidth() || var3.height != var1.getScaledWindowHeight()) {
               if (var3 != this.X) {
                  this.p = null;
                  return;
               }

               var3.init(var1.getScaledWindowWidth(), var1.getScaledWindowHeight());
               ClientLog.XAsVoid("GUI", "World Menu window laid out again at {}x{}", var3.width, var3.height);
            }

            if (!var4.options.hudHidden && !(var4.currentScreen instanceof HudEditorScreen)) {
               Vec3d var5 = var4.gameRenderer.getCamera().getCameraPos();
               if (this.placement.pAsBoolean("Follow") && this.F != null && this.I == null) {
                  this.f = new OverlayVideo.Type_v(
                     this.f.x() + var5.x - this.F.x, this.f.y() + var5.y - this.F.y, this.f.z() + var5.z - this.F.z, this.f.yaw(), this.f.pitch()
                  );
               }

               this.F = var5;
               if (this.I != null) {
                  this.gAsVoid(var3);
               }

               if (!this.f.faces(var5.x, var5.y, var5.z)) {
                  this.t.aAsVoid(0.0);
               } else {
                  XTexture.Type_f var6 = this.xAsXt_Type_f(var3, this.f);
                  double[] var7 = var3.TAsDoubleArray2();
                  double[] var8 = var6.bounds(0.0, 0.0, (double)var3.width, Math.max((double)var3.height, var7[3] + 24.0));
                  if (!(var8[2] < 0.0) && !(var8[3] < 0.0) && !(var8[0] > (double)var3.width) && !(var8[1] > (double)var3.height)) {
                     long var9 = System.nanoTime();
                     if (var9 - this.o >= 50000000L) {
                        this.o = var9;
                        this.K = this.EAsDouble(var3, var5);
                     }

                     double var11 = this.t.bAsDouble(this.K, 160);
                     if (!(var11 < 0.01)) {
                        double[] var13 = var6.inverse((double)var3.width / 2.0, (double)var3.height / 2.0);
                        int var14 = var3 == this.X && var13[0] != -10000.0 ? 1 : 0;
                        this.q = var14 != 0 && var11 > 0.5 && this.PAsBoolean() && var3.pAsBoolean(var13[0], var13[1]) && !this.RAsBoolean(var3, var13, var5);
                        if (this.q) {
                           this.V = var13[0];
                           this.L = var13[1];
                        }

                        if (var14 != 0 && this.O != 0 && this.I == null && (var13[0] != this.w || var13[1] != this.x)) {
                           MouseInput var15 = new MouseInput(Integer.numberOfTrailingZeros(this.O), 0);
                           var3.mouseDragged(new Click(var13[0], var13[1], var15), var13[0] - this.w, var13[1] - this.x);
                           this.w = var13[0];
                           this.x = var13[1];
                        }

                        int var18 = this.q || var14 != 0 && this.O != 0 ? 1 : 0;
                        int var16 = var18 != 0 ? (int)Math.floor(var13[0]) : -10000;
                        int var17 = var18 != 0 ? (int)Math.floor(var13[1]) : -10000;
                        var1.createNewRootLayer();
                        XTexture.oAsVoid(var1, var6, false, () -> {
                           var1.createNewRootLayer();

                           try (QueueGroup var7x = QueueGroup.nAsQg(var11)) {
                              var3.render(var1, var16, var17, var2);
                           }
                        });
                        var1.createNewRootLayer();
                     }
                  } else {
                     this.t.aAsVoid(0.0);
                  }
               }
            }
         }
      }
   }

   private boolean RAsBoolean(ClickGuiScreen var1, double[] var2, Vec3d var3) {
      HitResult var4 = getMc().crosshairTarget;
      if (var4 != null && var4.getType() != Type.MISS) {
         double var5 = this.oAsDouble(var1);
         double[] var7 = this.f.point((var2[0] - (double)var1.width / 2.0) * var5, ((double)var1.height / 2.0 - var2[1]) * var5);
         return var4.getPos().distanceTo(var3) < new Vec3d(var7[0], var7[1], var7[2]).distanceTo(var3);
      } else {
         return false;
      }
   }

   private void gAsVoid(ClickGuiScreen var1) {
      Camera var2 = getMc().gameRenderer.getCamera();
      double[] var3 = OverlayVideo.rAsDoubleArray2(
         WebRenderer.pAsMatrix4f(new Matrix4f()), (double)var1.width / 2.0, (double)var1.height / 2.0, (double)var1.width, (double)var1.height
      );
      if (var3 != null) {
         Vec3d var4 = var2.getCameraPos();
         this.f = OverlayVideo.UAsOv_Type_v(var4.x, var4.y, var4.z, var3, this.I.distance(), this.I.right(), this.I.up());
      }
   }

   private double EAsDouble(ClickGuiScreen var1, Vec3d var2) {
      MinecraftClient var3 = getMc();
      double[] var4 = var1.TAsDoubleArray2();
      double var5 = this.oAsDouble(var1);
      double var7 = (double)var1.width / 2.0;
      double var9 = (double)var1.height / 2.0;
      double var11 = (var4[0] + var4[2]) / 2.0;
      double var13 = (var4[1] + var4[3]) / 2.0;
      double var15 = (var4[2] - var4[0]) * 0.4;
      double var17 = (var4[3] - var4[1]) * 0.4;
      double[][] var19 = new double[][]{
         {var11, var13}, {var11 - var15, var13 - var17}, {var11 + var15, var13 - var17}, {var11 - var15, var13 + var17}, {var11 + var15, var13 + var17}
      };
      int var20 = 0;

      for (double[] var24 : var19) {
         double[] var25 = this.f.point((var24[0] - var7) * var5, (var9 - var24[1]) * var5);
         Vec3d var26 = new Vec3d(var25[0], var25[1], var25[2]);
         BlockHitResult var27 = var3.world.raycast(new RaycastContext(var2, var26, ShapeType.VISUAL, FluidHandling.NONE, var3.player));
         if (var27.getType() == Type.MISS || var27.getPos().distanceTo(var2) >= var26.distanceTo(var2) - 0.05) {
            var20++;
         }
      }

      return (double)var20 / (double)var19.length;
   }

   public static boolean zAsBoolean(MouseInput var0, int var1) {
      if (n != null && n.isEnabled()) {
         WorldMenu var2 = n;
         return var2 != null && var2.AAsBoolean(var0, var1);
      } else {
         return false;
      }
   }

   private boolean AAsBoolean(MouseInput var1, int var2) {
      ClickGuiScreen var3 = this.X;
      int var4 = 1 << Math.clamp((long)var1.button(), 0, 30);
      if (var2 == 0) {
         if ((this.O & var4) == 0) {
            return false;
         } else {
            this.O &= ~var4;
            if (var1.button() == 0) {
               this.kAsVoid();
            }

            if (var3 != null) {
               var3.mouseReleased(new Click(this.w, this.x, var1));
            }

            return getMc().currentScreen == null;
         }
      } else if (var2 != 1 || var3 == null || !this.rAsBoolean() || !this.PAsBoolean()) {
         return false;
      } else if (!this.q) {
         if (var3.rAsBoolean()) {
            var3.bAsVoid();
            ClientLog.XAsVoid("GUI", "World Menu typing ended by a click in the world");
         }

         return false;
      } else {
         long var5 = Util.getMeasuringTimeMs();
         int var7 = var1.button() == this.Y && var5 - this.j < 250L ? 1 : 0;
         this.j = var5;
         this.Y = var1.button();
         this.O |= var4;
         this.w = this.V;
         this.x = this.L;
         var3.mouseClicked(new Click(this.V, this.L, var1), var7 != 0);
         return true;
      }
   }

   public static boolean FAsBoolean() {
      if (n != null && n.isEnabled()) {
         WorldMenu var0 = n;
         return var0 != null && var0.X != null && (var0.q || var0.O != 0) && var0.PAsBoolean();
      } else {
         return false;
      }
   }

   public static boolean lAsBoolean(double var0) {
      if (n != null && n.isEnabled()) {
         WorldMenu var2 = n;
         return var2 != null && var2.VAsBoolean(var0);
      } else {
         return false;
      }
   }

   private boolean VAsBoolean(double var1) {
      ClickGuiScreen var3 = this.X;
      if (var3 == null || !this.rAsBoolean() || !this.PAsBoolean()) {
         return false;
      } else if (this.I != null) {
         this.I = new WorldMenu.Type_d(Math.clamp(this.I.distance() * Math.pow(1.12, var1), 0.5, this.GAsDouble()), this.I.right(), this.I.up());
         return true;
      } else if (!this.q) {
         return false;
      } else {
         var3.mouseScrolled(this.V, this.L, 0.0, var1);
         return true;
      }
   }

   private boolean jAsBoolean(ClickGuiScreen var1) {
      return var1 != null && var1 == this.X && this.rAsBoolean() && this.PAsBoolean() && var1.rAsBoolean();
   }

   public static boolean IAsBoolean() {
      if (n != null && n.isEnabled()) {
         WorldMenu var0 = n;
         return var0 != null && var0.jAsBoolean(var0.X);
      } else {
         return false;
      }
   }

   public static boolean JAsBoolean(int var0, KeyInput var1) {
      if (n != null && n.isEnabled()) {
         WorldMenu var2 = n;
         if (var2 != null && var0 != 0 && var2.jAsBoolean(var2.X)) {
            if (var1.key() == 256) {
               var2.X.iAsVoid();
               ClientLog.XAsVoid("GUI", "World Menu typing ended with Escape");
            } else {
               var2.X.keyPressed(var1);
            }

            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean AAsBoolean(CharInput var0) {
      if (n != null && n.isEnabled()) {
         WorldMenu var1 = n;
         if (var1 != null && var1.jAsBoolean(var1.X)) {
            var1.X.charTyped(var0);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean IAsBoolean(ClickGuiScreen var0) {
      if (n != null && n.isEnabled()) {
         WorldMenu var1 = n;
         return var1 != null && var0 != null && (var0 == var1.X || var0 == var1.p) && var1.rAsBoolean();
      } else {
         return false;
      }
   }

   private static int[] lAsIntArray(XModel var0) {
      return new int[]{var0.left() + (var0.width() - 76) / 2, var0.top() + var0.height() + 9, 76, 6};
   }

   private static boolean TAsBoolean(XModel var0, double var1, double var3) {
      int[] var5 = lAsIntArray(var0);
      return var1 >= (double)(var5[0] - 16)
         && var1 < (double)(var5[0] + var5[2] + 16)
         && var3 >= (double)(var5[1] - 9)
         && var3 < (double)(var5[1] + var5[3] + 9);
   }

   private static int[] dAsIntArray(XModel var0) {
      int[] var1 = lAsIntArray(var0);
      return new int[]{var1[0] + var1[2] + 22, var1[1] + var1[3] / 2 - 8, 16, 16};
   }

   private static boolean jAsBoolean(XModel var0, double var1, double var3) {
      int[] var5 = dAsIntArray(var0);
      return var1 >= (double)(var5[0] - 4) && var1 < (double)(var5[0] + var5[2] + 4) && var3 >= (double)(var5[1] - 4) && var3 < (double)(var5[1] + var5[3] + 4);
   }

   public static boolean IAsBoolean(XModel var0, double var1, double var3) {
      return TAsBoolean(var0, var1, var3) || jAsBoolean(var0, var1, var3);
   }

   public static void eAsVoid(DrawContext var0, ClickGuiScreen var1, XModel var2, int var3, int var4) {
      if (n != null && n.isEnabled()) {
         WorldMenu var5 = n;
         if (var5 != null && IAsBoolean(var1)) {
            var5.nAsVoid(var0, var1, var2, var3, var4);
         }
      }
   }

   private void nAsVoid(DrawContext var1, ClickGuiScreen var2, XModel var3, int var4, int var5) {
      int var6 = var2 == this.X ? 1 : 0;
      int var7 = var6 != 0 && this.I != null ? 1 : 0;
      int var8 = var7 != 0 || var6 != 0 && TAsBoolean(var3, (double)var4, (double)var5) ? 1 : 0;
      double var9 = this.s.bAsDouble(var8 != 0 ? 1.0 : 0.0, 180);
      int[] var11 = lAsIntArray(var3);
      int var12 = var11[2] + (int)Math.round(18.0 * var9);
      int var13 = var11[0] - (var12 - var11[2]) / 2;
      int var14 = var7 != 0 ? UiStyle.WAsInt(var13, UiStyle.m) : ColorUtil.XAsInt(UiStyle.u, 0.34 + 0.42 * var9);
      GuiTextureDrawing.AAsVoid(var1, (float)var13, (float)var11[1], (float)var12, (float)var11[3], (float)(var11[3] / 2), var14);
      int[] var15 = dAsIntArray(var3);
      int var16 = var6 != 0 && this.I == null && jAsBoolean(var3, (double)var4, (double)var5) ? 1 : 0;
      GuiTextureDrawing.AAsVoid(
         var1, (float)var15[0], (float)var15[1], (float)var15[2], (float)var15[3], (float)(var15[2] / 2), var16 != 0 ? -1750963 : ColorUtil.XAsInt(UiStyle.u, 0.16)
      );
      GuiIcons.aAsVoid(var1, GuiIcons.PAsIdentifier("x"), var15[0] + 3, var15[1] + 3, var15[2] - 6, var16 != 0 ? -1 : ColorUtil.XAsInt(UiStyle.u, 0.8));
   }

   public static boolean jAsBoolean(ClickGuiScreen var0, double var1, double var3, int var5, XModel var6) {
      if (n != null && n.isEnabled()) {
         WorldMenu var7 = n;
         return var7 != null && var7.ZAsBoolean(var0, var1, var3, var5, var6);
      } else {
         return false;
      }
   }

   private boolean ZAsBoolean(ClickGuiScreen var1, double var2, double var4, int var6, XModel var7) {
      if (var6 != 0 || var1 != this.X || !this.rAsBoolean()) {
         return false;
      } else if (jAsBoolean(var7, var2, var4)) {
         ClientLog.XAsVoid("GUI", "World Menu closed with its X");
         var1.close();
         return true;
      } else if (TAsBoolean(var7, var2, var4) && this.q) {
         double var8 = this.oAsDouble(var1);
         double var10 = (this.V - (double)var1.width / 2.0) * var8;
         double var12 = ((double)var1.height / 2.0 - this.L) * var8;
         double[] var14 = this.f.point(var10, var12);
         Vec3d var15 = getMc().gameRenderer.getCamera().getCameraPos();
         double var16 = new Vec3d(var14[0], var14[1], var14[2]).distanceTo(var15);
         this.I = new WorldMenu.Type_d(Math.clamp(var16, 0.5, this.GAsDouble()), var10, var12);
         ClientLog.XAsVoid("GUI", "World Menu grabbed {} blocks away", OAsString(var16));
         return true;
      } else {
         return false;
      }
   }

   private void kAsVoid() {
      if (this.I != null) {
         this.I = null;
         ClientLog.XAsVoid(
            "GUI",
            "World Menu set down {} blocks away, yaw {}, pitch {}",
            OAsString(this.DAsDouble()),
            this.f == null ? 0L : Math.round(this.f.yaw()),
            this.f == null ? 0L : Math.round(this.f.pitch())
         );
      }
   }

   @Override
   public void onEnable() {
      ClientLog.XAsVoid(
         "GUI",
         "World Menu active: placement={}, close distance={}, height={}%, size={}",
         this.placement.getValue(),
         this.closeDistance.getValue().intValue(),
         this.menuHeight.getValue().intValue(),
         this.panelSize.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.iAsVoid("turned off");
      this.t.aAsVoid(0.0);
   }

   private static record Type_d(double distance, double right, double up) {
   }
}
