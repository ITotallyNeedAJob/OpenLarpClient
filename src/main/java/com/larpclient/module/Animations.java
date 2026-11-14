package com.larpclient.module;

import com.google.gson.JsonObject;
import com.larpclient.ClientLog;
import com.larpclient.ProfileManager;
import com.larpclient.DepthEffect;
import com.larpclient.MediaSession;
import com.larpclient.PanelQuality;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.RotationAxis;

public class Animations extends Module {
   private final ModeSetting swing = this.addSetting(new ModeSetting("Swing", "Swing preset", "Module 6", dx.N) {
      @Override
      public String getDisplayName() {
         return "Mode";
      }

      @Override
      public void setValue(String var1) {
         super.setValue(Animations.KAsString(var1));
      }
   });
   private final BooleanSetting W = this.JAsBooleanSetting("Only with aura", "Use swing presets while Kill Aura has a target", false);
   private final NumberSetting U = this.aAsNumberSetting("Legacy Strength", "Classic preset strength", 20.0, 20.0, 75.0, 0.1);
   private final NumberSetting V = this.aAsNumberSetting("Swing Strength", "Preset movement strength", 8.0, 1.0, 10.0, 0.1);
   private final NumberSetting I = this.aAsNumberSetting("Angle", "Module 2 and Module 4 angle", 100.0, 0.0, 360.0, 1.0);
   private final NumberSetting B = this.aAsNumberSetting("Turn", "Shift and Break turn", 0.0, -90.0, 90.0, 0.5);
   private final NumberSetting p = this.aAsNumberSetting("Down Strength", "Additional downward swing movement", 0.0, 0.0, 1.0, 0.05);
   private final NumberSetting e = this.aAsNumberSetting("Distance", "Preset depth", 0.25, 0.0, 0.8, 0.01);
   private final BooleanSetting a = this.JAsBooleanSetting("Smooth Motion", "Ease swing progress", true);
   private final NumberSetting m = this.aAsNumberSetting("Smooth Amount", "Blend cubic easing", 0.65, 0.0, 1.0, 0.05);
   private final NumberSetting t = this.aAsNumberSetting("Motion Curve", "Progress curve exponent", 1.0, 0.45, 2.5, 0.05);
   private final BooleanSetting w = this.JAsBooleanSetting("Item 360", "Continuously rotate held items", false);
   private final ModeSetting spinAxis = this.addSetting(new ModeSetting("Spin Axis", "Continuous spin axis", "X", "X", "Y", "Z"));
   private final NumberSetting K = this.aAsNumberSetting("Spin Speed", "Continuous spin speed", 3.6, 0.2, 12.0, 0.1);
   private final BooleanSetting A = this.JAsBooleanSetting("Slow", "Override local hand swing duration", false);
   private final NumberSetting i = this.aAsNumberSetting("Speed", "Swing duration in ticks", 12.0, 1.0, 50.0, 1.0);
   private final dx.Type_r y = new dx.Type_r();
   private Object Z;
   private Object H;
   private final pq L = new pq();
   private static Animations R;
   public static int d;
   private static final String[] b = new String[]{
      "Mode 4", "Mode 2", "None", "Mode 1", "Swipe", "Twist", "Module 6", "Mode 5", "Mode 3", "Default", "Spin", "Smooth 2", "Smooth", "Old", "Normal"
   };

   public Animations() {
      super("Animations", "Customize swing presets and held-item spin", Category.VISUALS, -1);
      R = this;
      this.U.visibleWhen(() -> this.swing.pAsBoolean("Mode 2") || this.swing.pAsBoolean("Mode 3") || this.swing.pAsBoolean("Mode 4"));
      this.V.visibleWhen(() -> {
         String var1 = this.swing.getValue();

         return switch (var1) {
            case "Module 1", "Module 2", "Module 3", "Module 4", "Module 6", "Module 7", "Shift", "Break", "Lunge", "Spear", "Jelly", "Shake" -> true;
            default -> false;
         };
      });
      this.p.visibleWhen(() -> {
         String var1 = this.swing.getValue();

         return switch (var1) {
            case "Mode 1", "Mode 2", "Mode 3", "Mode 4", "Mode 5", "Module 8" -> false;
            default -> true;
         };
      });
      this.e.visibleWhen(() -> {
         String var1 = this.swing.getValue();

         return switch (var1) {
            case "Module 2", "Module 4", "Slant", "Triangle", "Shift", "Break", "Lunge", "Spear", "Down", "Jelly", "Shake" -> true;
            default -> false;
         };
      });
      this.I.visibleWhen(() -> this.swing.pAsBoolean("Module 2") || this.swing.pAsBoolean("Module 4"));
      this.B.visibleWhen(() -> this.swing.pAsBoolean("Shift") || this.swing.pAsBoolean("Break"));
      this.m.visibleWhen(this.a::getValue);
      this.spinAxis.visibleWhen(this.w::getValue);
      this.K.visibleWhen(this.w::getValue);
      this.i.visibleWhen(this.A::getValue);
      this.swing.onChanged(var1 -> this.y.EAsVoid());
      this.aAsNumberSetting("Duration", "Retained legacy value", 1.0, 0.25, 3.0, 0.05).visibleWhen(() -> false);
      this.aAsNumberSetting("Strength", "Retained legacy value", 1.0, 0.0, 3.0, 0.05).visibleWhen(() -> false);
      this.JAsBooleanSetting("Only During Swing", "Retained legacy value", false).visibleWhen(() -> false);
      this.JAsBooleanSetting("Equip Motion", "Retained legacy value", false).visibleWhen(() -> false);
      this.JAsBooleanSetting("Animate Offhand", "Retained legacy value", false).visibleWhen(() -> false);
   }

   private NumberSetting aAsNumberSetting(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new NumberSetting(var1, var2, var3, var5, var7, var9));
   }

   private BooleanSetting JAsBooleanSetting(String var1, String var2, boolean var3) {
      return this.addSetting(new BooleanSetting(var1, var2, var3));
   }

   public static Animations oAsAnimations() {
      return R;
   }

   public List<Setting<?>> JAsList() {
      return List.of(this.swing, this.W, this.U, this.V, this.I, this.B, this.p, this.e, this.a, this.A, this.i);
   }

   public List<Setting<?>> SAsList() {
      return List.of(this.m, this.t, this.w, this.spinAxis, this.K);
   }

   @Override
   public String getDisplayName() {
      return "SwingAnimation";
   }

   public String swing() {
      return this.swing.getValue();
   }

   private boolean BAsBoolean() {
      Aura var1 = Aura.fAsAura();
      return var1 != null && var1.hAsLivingEntity() != null;
   }

   public boolean UAsBoolean(Arm var1) {
      if (!this.isEnabled()) {
         return false;
      } else {
         ClientPlayerEntity var2 = getMc().player;
         return this.isRuntimeActive() && var2 != null && var2.getMainArm() == var1;
      }
   }

   public boolean KAsBoolean(Arm var1) {
      return this.isEnabled() ? this.UAsBoolean(var1) && (!this.W.getValue() || this.BAsBoolean()) : false;
   }

   public boolean qAsBoolean() {
      return false;
   }

   public boolean CAsBoolean() {
      return this.isEnabled() ? this.A.getValue() : false;
   }

   public int vAsInt(int var1) {
      if (this.isEnabled()) {
         return this.A.getValue() ? this.i.getValue().intValue() : var1;
      } else {
         return var1;
      }
   }

   private void BAsVoid() {
      MinecraftClient var1 = getMc();
      if (this.Z != var1.player || this.H != var1.world) {
         this.y.EAsVoid();
         this.L.nAsVoid();
         this.Z = var1.player;
         this.H = var1.world;
      }
   }

   public void sAsVoid(MatrixStack var1, Arm var2, float var3) {
      if (this.isEnabled()) {
         this.BAsVoid();
         dx.kAsVoid(
            var1,
            this.swing.getValue(),
            var2 == Arm.LEFT,
            var3,
            new dx.Type_j(
               this.U.getValue().floatValue(),
               this.V.getValue().floatValue(),
               this.I.getValue().floatValue(),
               this.B.getValue().floatValue(),
               this.p.getValue().floatValue(),
               this.e.getValue().floatValue(),
               this.a.getValue(),
               this.m.getValue().floatValue(),
               this.t.getValue().floatValue()
            ),
            this.y
         );
      }
   }

   public void xAsVoid(MatrixStack var1) {
      if (this.isEnabled() && this.isRuntimeActive() && this.w.getValue()) {
         this.BAsVoid();
         float var2 = (float)Math.toDegrees(this.L.uAsDouble(mb.RAsDouble(), this.K.getValue() / 4.0) % (Math.PI * 2));
         String var3 = this.spinAxis.getValue();

         var1.multiply(switch (var3) {
            case "Y" -> RotationAxis.POSITIVE_Y.rotationDegrees(var2);
            case "Z" -> RotationAxis.POSITIVE_Z.rotationDegrees(var2);
            default -> RotationAxis.POSITIVE_X.rotationDegrees(var2);
         });
      }
   }

   @Override
   public void onEnable() {
      this.oAsVoid();
      ClientLog.GAsVoid("SwingAnimation", "Activated preset {}", this.swing.getValue());
   }

   @Override
   public void onDisable() {
      this.oAsVoid();
      ClientLog.GAsVoid("SwingAnimation", "Cleared swing cycle and item spin state");
   }

   private void oAsVoid() {
      this.y.EAsVoid();
      this.Z = this.H = null;
      this.L.nAsVoid();
   }

   public static String KAsString(String var0) {
      byte var4 = -1;
      boolean var1 = Emotes.B;
      switch (var0.hashCode()) {
         case -1955878649:
            String[] var5 = b;
            if (!var0.equals(var5[14])) {
               break;
            }

            var4 = 0;
            if (!var1) {
               break;
            }

            int var2 = ProfileManager.sm;
            ProfileManager.sm = ++var2;
         case -1085510111:
            String[] var7 = b;
            if (!var0.equals(var7[9])) {
               break;
            }

            var4 = 1;
            if (!var1) {
               break;
            }
         case 2433880:
            String[] var8 = b;
            if (!var0.equals(var8[2])) {
               break;
            }

            var4 = 2;
            if (!var1) {
               break;
            }
         case 80301850:
            String[] var9 = b;
            if (!var0.equals(var9[4])) {
               break;
            }

            var4 = 3;
            if (!var1) {
               break;
            }
         case -1814666802:
            String[] var10 = b;
            if (!var0.equals(var10[12])) {
               break;
            }

            var4 = 4;
            if (!var1) {
               break;
            }
         case 79367:
            String[] var11 = b;
            if (!var0.equals(var11[13])) {
               break;
            }

            var4 = 5;
            if (!var1) {
               break;
            }
         case -138073504:
            String[] var12 = b;
            if (!var0.equals(var12[11])) {
               break;
            }

            var4 = 6;
            if (!var1) {
               break;
            }
         case 81225479:
            String[] var13 = b;
            if (!var0.equals(var13[5])) {
               break;
            }

            var4 = 7;
            if (!var1) {
               break;
            }
         case 2583650:
            String[] var14 = b;
            if (var0.equals(var14[10])) {
               var4 = 8;
            }
      }
      return switch (var4) {
         case 0, 1, 2 -> {
            String[] var19 = b;
            yield var19[3];
         }
         case 3 -> {
            String[] var18 = b;
            yield var18[1];
         }
         case 4, 5 -> {
            String[] var17 = b;
            yield var17[6];
         }
         case 6 -> {
            String[] var16 = b;
            yield var16[8];
         }
         case 7 -> {
            String[] var15 = b;
            yield var15[0];
         }
         case 8 -> b[7];
         default -> var0;
      };
   }

   public static void QAsVoid(JsonObject var0) {
      if (var0.has("Swing") && var0.get("Swing").isJsonPrimitive() && var0.getAsJsonPrimitive("Swing").isString()) {
         var0.addProperty("Swing", KAsString(var0.get("Swing").getAsString()));
      }

      if (var0.has("Swing Speed")) {
         if (!var0.has("Duration")) {
            try {
               double var1 = var0.get("Swing Speed").getAsDouble();
               if (Double.isFinite(var1)) {
                  var0.addProperty("Duration", Math.clamp(var1 / 6.0, 0.25, 3.0));
               }
            } catch (RuntimeException var3) {
            }
         }

         var0.remove("Swing Speed");
      }
   }
}
