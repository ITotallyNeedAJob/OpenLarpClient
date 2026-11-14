package com.larpclient;

import com.larpclient.module.BooleanSetting;
import com.larpclient.module.Category;
import com.larpclient.module.ColorSetting;
import com.larpclient.module.ModeSetting;
import com.larpclient.module.Module;
import com.larpclient.module.NumberSetting;
import com.larpclient.module.Setting;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class Pets extends Module {
   private static Pets y;
   private final ModeSetting pet = this.addSetting(
      new ModeSetting("Pet", "Companion style", "Tung Tung Tung Sahur", "Dragon", "Jellyfish", "Rune Orb", "Ghost", "Tung Tung Tung Sahur")
   );
   private final BooleanSetting realism = this.addSetting(new BooleanSetting("Realism", "Detailed anatomy, natural finishes and reactive gaze", false));
   private final ModeSetting realisticPet = this.addSetting(
      new ModeSetting(
         "Realistic Pet", "Detailed companion style", "Tung Tung Tung Sahur", "Dragon", "Jellyfish", "Rune Orb", "Bee", "Ghost", "Tung Tung Tung Sahur"
      )
   );
   private final NumberSetting realisticSize = this.addSetting(new NumberSetting("Realistic Size", "Detailed companion size", 1.0, 0.5, 2.5, 0.05));
   private final NumberSetting size = this.addSetting(new NumberSetting("Size", "Pet size", 1.3, 0.5, 2.5, 0.05));
   private final ModeSetting position = this.addSetting(new ModeSetting("Position", "Where the companion follows", "Shoulder", "Shoulder", "Orbit", "Behind"));
   private final BooleanSetting sparkleTrail = this.addSetting(new BooleanSetting("Sparkle trail", "Sparks follow the companion", true));
   private final BooleanSetting inFirstPerson = this.addSetting(new BooleanSetting("In first person", "Show the companion in first person", true));
   private final BooleanSetting speechBubbles = this.addSetting(new BooleanSetting("Speech bubbles", "Show notifications above an on-screen companion", true));
   private final ModeSetting colorMode = this.addSetting(new ModeSetting("Color mode", "Realistic companion palette", "Natural", "Natural", "Theme", "Custom"));
   private final ColorSetting customColor = this.addSetting(new ColorSetting("Custom color", "Realistic companion glow", -1459895));
   private final Map<String, Pets.Type_t> Y = new LinkedHashMap<>();
   private Object U;
   private Object p;
   private Vec3d J;
   private Vec3d T = Vec3d.ZERO;
   private float TextLayout = Float.NaN;
   private float K;
   private float Z;
   private double F;
   private double b;
   private double M;
   private double c;
   private double D;
   private double n;
   private double LineSegment = 6.5;
   private double W;
   private double x;
   private double S;
   private int h;
   private boolean B;
   private String v;
   private final ArrayDeque<Pets.Type_v> MenuPopupProvider = new ArrayDeque<>();
   private final ArrayDeque<Pets.Type_g> V = new ArrayDeque<>();
   private Pets.Type_g s;
   private long C;
   private long g;
   private Pets.Type_w z;
   private final Random A = new Random(1279349328L);
   private double r;

   public Pets() {
      super("Pets", "A local companion with reactive motion and speech bubbles", Category.RENDER, -1);
      ScreenScale = this;
      this.pet.visibleWhen(() -> !this.realism.getValue());
      this.size.visibleWhen(() -> !this.realism.getValue());
      this.realisticPet.visibleWhen(() -> this.realism.getValue());
      this.realisticSize.visibleWhen(() -> this.realism.getValue());
      this.Y.put("Dragon", new Pets.Type_t("Dragon", 50, "Scales", "Obsidian", "Gold"));
      this.Y.put("Jellyfish", new Pets.Type_t("Jellyfish", 70, "Glass", "Energy", "Crystal"));
      this.Y.put("Rune Orb", new Pets.Type_t("Rune Orb", 70, "Glass", "Crystal", "Obsidian"));
      this.Y.put("Ghost", new Pets.Type_t("Ghost", 50, "Wisp", "Silk", "Glass"));
      this.speechBubbles.onChanged(var1 -> {
         if (!var1) {
            this.EAsVoid();
            this.V.clear();
            this.s = null;
         }
      });
      this.colorMode.visibleWhen(() -> this.realism.getValue() && !uAsBoolean(this.realisticPet.getValue()));
      this.customColor.visibleWhen(() -> this.colorMode.isVisible() && this.colorMode.pAsBoolean("Custom"));
   }

   public static Pets VAsPets() {
      return y;
   }

   public List<Setting<?>> lAsList() {
      return List.of(this.realism, this.pet, this.realisticPet, this.size, this.realisticSize);
   }

   public List<Setting<?>> WAsList() {
      return List.of(this.position, this.inFirstPerson, this.sparkleTrail, this.speechBubbles);
   }

   public List<Setting<?>> FAsList() {
      ArrayList var1 = new ArrayList<>(List.of(this.colorMode, this.customColor));

      for (Pets.Type_t var3 : this.Y.values()) {
         var1.addAll(List.of(var3.companionSurfaceFinish, var3.useTheFinishPalette, var3.mainCompanionColor));
      }

      return List.copyOf(var1);
   }

   public List<Setting<?>> LAsList() {
      ArrayList var1 = new ArrayList();

      for (Pets.Type_t var3 : this.Y.values()) {
         var1.addAll(List.of(var3.magicGlowStrengthInPercent, var3.companionGlowColor));
      }

      return List.copyOf(var1);
   }

   @Override
   public void onEnable() {
      this.DAsVoid();
      ClientLog.XAsVoid("Pets", "Local companion activated: {}", this.pet.getValue());
   }

   @Override
   public void onDisable() {
      this.DAsVoid();
   }

   public void DAsVoid() {
      this.z = null;
      this.g = 0L;
      this.EAsVoid();
      this.U = this.p = null;
      this.J = null;
      this.T = Vec3d.ZERO;
      this.j = Float.NaN;
      this.z = null;
      this.f.clear();
      this.V.clear();
      this.s = null;
      this.g = 0L;
      this.S = mb.RAsDouble();
      this.v = null;
      this.F = this.b = this.M = this.c = this.n = this.W = this.x = 0.0;
      this.D = 1.0;
      this.e = 6.5;
      this.h = 0;
      this.B = false;
      this.r = 0.0;
      this.A.setSeed(1279349328L);
   }

   private void EAsVoid() {
      Pets.Type_g var1 = this.s;
      long var2 = this.C;
      List var4 = List.copyOf(this.V);
      this.s = null;
      this.V.clear();
      NotificationHud var5 = NotificationHud.GAsW8();
      if (var1 != null) {
         long var6 = var1.duration - (System.nanoTime() - var2) / 1000000L;
         if (var6 > 0L) {
            var5.oAsVoid(var1.title, var1.text, var1.type, var6);
         }
      }

      for (Pets.Type_g var7 : (Iterable<Pets.Type_g>) (var4)) {
         var5.oAsVoid(var7.title, var7.text, var7.type, var7.duration);
      }
   }

   public void UAsVoid() {
      if (this.isEnabled() && this.isRuntimeActive()) {
         this.c = Math.max(this.c, 1.5);
         this.D = 0.0;
      }
   }

   public void pAsVoid(float var1, CameraRenderState var2) {
      if (this.isEnabled()) {
         this.z = null;
         if (this.isRuntimeActive() && this.isInGame()) {
            MinecraftClient var3 = getMc();
            boolean var4 = this.realism.getValue();
            String var5 = var4 ? this.realisticPet.getValue() : this.pet.getValue();
            boolean var6 = uAsBoolean(var5);
            if (this.U != var3.world || this.p != var3.player || !Objects.equals(this.v, var5 + ":" + var4)) {
               this.DAsVoid();
               this.U = var3.world;
               this.p = var3.player;
               this.v = var5 + ":" + var4;
               this.S = mb.RAsDouble();
            }

            double var7 = Math.clamp(mb.RAsDouble() - this.S, 0.0, 0.1);
            this.S = mb.RAsDouble();
            this.x += var7;
            double var9 = qAsDouble(var5, var4 ? this.realisticSize.getValue() : this.size.getValue());
            Vec3d var11 = var3.player.getCameraPosVec(var1);
            Vec3d var12 = var3.player.getVelocity();
            float var13 = (float)Math.toRadians((double)var3.player.getYaw(var1));
            if (Float.isNaN(this.j)) {
               this.j = var13;
            }

            if (var12.horizontalLength() * 20.0 > 1.0) {
               this.j = this.j + MAsFloat((float)Math.atan2(-var12.x, var12.z) - this.j) * (float)Math.min(1.0, var7 * 3.0);
            } else if ((var3.options.useKey.isPressed() || var3.options.attackKey.isPressed()) && this.J != null) {
               Vec3d var14 = this.J.subtract(var11);
               if (var14.length() < 6.0
                  && var14.length() > 0.001
                  && var14.normalize().dotProduct(var3.player.getRotationVec(var1)) > Math.cos(Math.toRadians(28.0))) {
                  this.j = this.j + MAsFloat(var13 - this.j) * (float)Math.min(1.0, var7 * 6.0);
               }
            }

            Vec3d var47 = new Vec3d(-Math.sin((double)this.j), 0.0, Math.cos((double)this.j));
            Vec3d var15 = new Vec3d(-var47.z, 0.0, var47.x);
            String var48 = this.position.getValue();

            Vec3d var16 = switch (var48) {
               case "Orbit" -> var11.add(Math.cos(this.x * 0.9) * 1.3 * var9, -0.15 + Math.sin(this.x * 1.7) * 0.15, Math.sin(this.x * 0.9) * 1.3 * var9);
               case "Behind" -> var11.add(var47.multiply(-1.4 * var9)).add(var15.multiply(0.35)).add(0.0, -0.95, 0.0);
               default -> var11.add(var15.multiply(0.72 * var9)).add(var47.multiply(-0.35)).add(0.0, 0.28 * var9, 0.0);
            };
            BlockHitResult var48_r2 = var3.world.raycast(new RaycastContext(var11, var16, ShapeType.COLLIDER, FluidHandling.NONE, var3.player));
            if (var48_r2.getType() == Type.BLOCK) {
               var16 = var48_r2.getPos().subtract(var16.subtract(var11).normalize().multiply(0.3 * var9));
            }

            if (this.J == null || this.J.squaredDistanceTo(var16) > 400.0) {
               this.J = var16;
               this.T = Vec3d.ZERO;
               this.f.clear();
            }

            int var49 = var3.player.hurtTime;
            if (!var6 && var49 > this.h && var49 >= 6) {
               this.M = 1.0;
               this.T = this.T.add(this.J.subtract(var11).normalize().multiply(2.5));
            }

            this.h = var49;
            this.M = Math.max(0.0, this.M - var7 * 1.6);
            this.c = Math.max(0.0, this.c - var7);
            if (var3.player.handSwinging && !this.B) {
               this.c = Math.max(this.c, 1.1);
               this.D = 0.0;
            }

            this.B = var3.player.handSwinging;
            this.D = Math.min(1.0, this.D + var7 / 0.65);
            this.T = this.T.add(var16.subtract(this.J).multiply(26.0 * var7)).multiply(Math.pow(0.004, var7));
            this.J = this.J.add(this.T.multiply(var7));
            double var19 = this.T.length();
            float var21 = var19 > 0.6 ? (float)Math.atan2(-this.T.x, this.T.z) : this.j;
            this.K = this.K + MAsFloat(var21 - this.K) * (float)Math.min(1.0, var7 * 6.0);
            this.Z = this.Z + ((float)Math.clamp(-this.T.y * 0.35, -0.6, 0.6) - this.Z) * (float)Math.min(1.0, var7 * 5.0);
            Vec3d var22 = this.J.add(0.0, Math.sin(this.x * 2.1) * 0.05 * var9 + (!var6 && this.D < 1.0 ? Math.sin(this.D * Math.PI) * 0.12 * var9 : 0.0), 0.0);
            Vec3d var23 = (var3.crosshairTarget != null && var3.crosshairTarget.getType() != Type.MISS
                  ? var3.crosshairTarget.getPos()
                  : var11.add(var3.player.getRotationVec(var1).multiply(6.0)))
               .subtract(var22);
            double var24 = (double)MAsFloat((float)(Math.atan2(-var23.x, var23.z) - (double)this.K));
            double var26 = -Math.atan2(var23.y, var23.horizontalLength());
            int var28 = Math.abs(var24) < 1.6 ? 1 : 0;
            this.F = this.F + ((var28 != 0 ? Math.clamp(var24, -1.1, 1.1) : Math.sin(this.x * 0.6) * 0.35) - this.F) * (1.0 - Math.exp(-4.0 * var7));
            this.b = this.b + ((var28 != 0 ? Math.clamp(var26, -0.6, 0.6) : 0.0) - this.b) * (1.0 - Math.exp(-4.0 * var7));
            this.W = this.W + (Math.clamp((var19 - 2.5) / 3.0, 0.0, 1.0) - this.W) * Math.min(1.0, var7 * 3.0);
            this.e = this.e + (6.5 + Math.min(var19, 2.5) * 0.6 - this.W * 3.0 - this.e) * Math.min(1.0, var7 * 2.0);
            this.n = (this.n + var7 * this.e) % (Math.PI * 2);
            double var29 = Math.sin(this.n) * (1.0 - this.W * 0.75) + this.W * 0.15;
            int var31 = var3.options.getPerspective().isFirstPerson() && !this.inFirstPerson.getValue() ? 0 : 1;
            if (var31 != 0) {
               if (var6) {
                  SahurPetRenderer.bAsVoid();
               }

               Pets.Type_t var36 = this.Y.getOrDefault(var5, this.Y.get("Dragon"));
               int var32;
               int var33;
               double var34;
               if (var6) {
                  var32 = -1;
                  var33 = bd.w;
                  var34 = this.sparkleTrail.getValue() ? 0.5 : 0.0;
               } else if (var4) {
                  int var37 = switch (var5) {
                     case "Dragon" -> -10976403;
                     case "Ghost" -> -1840922;
                     case "Jellyfish" -> -7288099;
                     case "Rune Orb" -> -4025601;
                     default -> -2116013;
                  };
                  var32 = var37;
                  String var53 = this.colorMode.getValue();

                  var33 = switch (var53) {
                     case "Theme" -> bd.w;
                     case "Custom" -> this.customColor.getValue();
                     default -> var37;
                  };
                  var34 = 0.6;
               } else {
                  var32 = var36.YAsInt();
                  var33 = var36.eAsInt();
                  var34 = var36.magicGlowStrengthInPercent.getValue() / 100.0;
               }

               this.f.removeIf(var1x -> this.x - var1x.birth > var1x.lifetime);
               if (this.sparkleTrail.getValue() && var7 > 0.0) {
                  this.r = this.r + var7 * (15.0 + Math.min(36.0, var19 * 18.0));

                  while (this.r >= 1.0) {
                     this.r--;
                     Vec3d var50 = var22.add(this.A.nextGaussian() * 0.06 * var9, this.A.nextGaussian() * 0.06 * var9, this.A.nextGaussian() * 0.06 * var9);
                     this.f.addLast(new Pets.Type_v(var50, this.x, 0.8 + this.A.nextDouble() * 0.8, 0.8 + this.A.nextDouble() * 0.8));

                     while (this.f.size() > 96) {
                        this.f.removeFirst();
                     }
                  }
               }

               if (!this.sparkleTrail.getValue()) {
                  this.f.clear();
                  this.r = 0.0;
               }

               BlockHitResult var51 = var3.world
                  .raycast(new RaycastContext(var22, var22.add(0.0, -4.5, 0.0), ShapeType.COLLIDER, FluidHandling.ANY, var3.player));
               Vec3d var53 = var51.getType() == Type.BLOCK ? var51.getPos().add(0.0, 0.012, 0.0) : null;
               double var55 = var53 == null ? 0.0 : Math.pow(Math.max(0.0, 1.0 - (var22.y - var53.y) / 4.5), 2.0) * 0.4;
               Matrix4f var41 = new Matrix4f(w3.CAsMatrix4fc()).mul(w3.VAsMatrix4fc());
               Vector3f var42 = new Quaternionf(var2.orientation).transform(new Vector3f(0.0F, 1.0F, 0.0F));
               d5.Type_e var43 = d5.vAsD5_Type_e(
                  var22.add(0.0, 0.32 * var9, 0.0), var2.pos, var41, var42, var3.getWindow().getScaledWidth(), var3.getWindow().getScaledHeight(), false
               );
               if (var43 != null) {
                  this.g = System.nanoTime();
               }

               double var44 = this.D < 1.0 ? (1.0 - Math.pow(1.0 - this.D, 3.0)) * Math.PI * 2.0 : 0.0;
               Quaternionf var46 = var6
                  ? new Quaternionf().rotationY(-this.K)
                  : new Quaternionf().rotationY((float)((double)(-this.K) + var44)).rotateX(this.Z);
               this.z = new Pets.Type_w(
                  var5,
                  var36.companionSurfaceFinish.getValue(),
                  var4,
                  var22,
                  var2.pos.subtract(var22).normalize(),
                  var46,
                  var9,
                  this.x,
                  var19,
                  var29,
                  this.F,
                  this.b,
                  Math.max(this.c, this.M),
                  var32,
                  var33,
                  var34,
                  var53,
                  var55,
                  List.copyOf(this.f),
                  var43
               );
            }
         }
      }
   }

   private static float MAsFloat(float var0) {
      return (float)Math.atan2(Math.sin((double)var0), Math.cos((double)var0));
   }

   public static double qAsDouble(String var0, double var1) {
      return var1 * (var0.equals("Tung Tung Tung Sahur") ? 1.2 : 1.0);
   }

   static boolean uAsBoolean(String var0) {
      return var0.equals("Tung Tung Tung Sahur");
   }

   public boolean TAsBoolean() {
      return this.isEnabled() ? this.z != null && this.z.glowStrength > 0.0 : false;
   }

   public void JAsVoid(EffectGeometryRenderer var1, boolean var2) {
      if (this.isEnabled()) {
         Pets.Type_w var3 = this.z;
         if (var3 != null && (!var2 || !(var3.glowStrength <= 0.0))) {
            if (!var2 && var3.shadow != null) {
               var1.oAsVoid(var3.shadow, 0.18 * var3.scale, 0.16 * var3.scale, bg.XAsInt(-16777216, var3.shadowAlpha));
            }

            PetEffectRenderer.jAsVoid(var1, var3, var2);

            for (Pets.Type_v var5 : var3.sparks) {
               double var6 = Math.clamp(1.0 - (var3.time - var5.birth) / var5.lifetime, 0.0, 1.0);
               var1.BAsVoid(
                  var5.point.x,
                  var5.point.y + 0.08 * (1.0 - var6),
                  var5.point.z,
                  0.035 * var3.scale * var6 * var5.scale,
                  var3.time,
                  bg.XAsInt(var3.glow, var6 * var6 * var3.glowStrength)
               );
            }
         }
      }
   }

   public boolean sAsBoolean(String var1, String var2, NotificationHud.Type_u var3, long var4) {
      if (this.isEnabled()) {
         int var6 = switch (var3) {
            case WARNING -> -996986;
            case DANGER -> -1415819;
            default -> bd.w;
         };
         if (this.isRuntimeActive()
            && SharedRenderEffects.RAsBoolean()
            && !getMc().options.hudHidden
            && !(getMc().currentScreen instanceof ClickGuiScreen)
            && this.speechBubbles.getValue()
            && this.z != null
            && this.z.label != null
            && System.nanoTime() - this.g <= 250000000L) {
            this.V.removeIf(var1xx -> var1xx.title.equals(var1));
            if (this.s != null && this.s.title.equals(var1)) {
               this.s = null;
            }

            this.V.addLast(new Pets.Type_g(var1, var2 == null ? "" : var2, var6, Math.max(0L, var4), var3));

            while (this.V.size() > 4) {
               this.V.removeFirst();
            }

            this.c = Math.max(this.c, 0.6);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public void IAsVoid(String var1) {
      this.V.removeIf(var1xx -> var1xx.title.equals(var1));
      if (this.s != null && this.s.title.equals(var1)) {
         this.s = null;
      }
   }

   public void XAsVoid(DrawContext var1) {
      if (this.isEnabled()) {
         Pets.Type_w var2 = this.z;
         if (this.isRuntimeActive() && this.speechBubbles.getValue() && var2 != null && var2.label != null) {
            long var3 = System.nanoTime();
            if (this.s != null && (var3 - this.C) / 1000000L > this.s.duration + 220L) {
               this.s = null;
            }

            if (this.s == null && !this.V.isEmpty()) {
               this.s = this.V.removeFirst();
               this.C = var3;
            }

            if (this.s != null) {
               double var5 = (double)(var3 - this.C) / 1000000.0;
               double var7 = Math.clamp(var5 / 120.0, 0.0, 1.0) * (1.0 - Math.clamp((var5 - (double)this.s.duration) / 220.0, 0.0, 1.0));
               double var9 = Math.clamp(var5 / 220.0, 0.0, 1.0);
               double var11 = 1.0 + 2.70158 * Math.pow(var9 - 1.0, 3.0) + 1.70158 * Math.pow(var9 - 1.0, 2.0);
               String var13 = TextDrawing.sAsString(this.s.title, 220, TextDrawing.Type_x.MEDIUM);
               String var14 = TextDrawing.sAsString(this.s.text, 220, TextDrawing.Type_x.REGULAR);
               int var15 = Math.max(90, Math.max(TextDrawing.qAsInt(var13, TextDrawing.Type_x.MEDIUM), TextDrawing.nAsInt(var14)) + 24);
               int var16 = Math.clamp((long)Math.round(var2.label.x() - (float)var15 / 2.0F), 4, Math.max(4, getMc().getWindow().getScaledWidth() - var15 - 4));
               int var17 = Math.max(4, Math.round(var2.label.y()) - 42);
               var1.getMatrices().pushMatrix();

               try {
                  var1.getMatrices().translate(var2.label.x(), var2.label.y());
                  var1.getMatrices().scale((float)var11, (float)var11);
                  var1.getMatrices().translate(-var2.label.x(), -var2.label.y());
                  GuiTextureDrawing.AAsVoid(var1, (float)var16, (float)var17, (float)var15, 34.0F, 7.0F, bg.pAsInt(-185207568, var7));
                  int var18 = Math.clamp((long)Math.round(var2.label.x()), var16 + 10, var16 + var15 - 10);

                  for (int var19 = 0; var19 < 6; var19++) {
                     var1.fill(var18 - 5 + var19, var17 + 34 + var19, var18 + 5 - var19, var17 + 35 + var19, bg.pAsInt(-185207568, var7));
                  }

                  GuiTextureDrawing.AAsVoid(var1, (float)(var16 + 8), (float)(var17 + 9), 3.0F, 3.0F, 1.0F, bg.pAsInt(this.s.color, var7));
                  TextDrawing.oAsVoid(var1, var13, var16 + 16, var17 + 5, bg.pAsInt(-14539990, var7), TextDrawing.Type_x.MEDIUM);
                  TextDrawing.CAsVoid(var1, var14, var16 + 8, var17 + 19, bg.pAsInt(-11381667, var7));
               } finally {
                  var1.getMatrices().popMatrix();
               }
            }
         }
      }
   }

   private static record Type_g(String title, String text, int color, long duration, NotificationHud.Type_u type) {
   }

   private final class Type_t {
      final ModeSetting companionSurfaceFinish;
      final BooleanSetting useTheFinishPalette;
      final ColorSetting mainCompanionColor;
      final NumberSetting magicGlowStrengthInPercent;
      final ColorSetting companionGlowColor;

      Type_t(String var2, int var3, String... var4) {
         this.companionSurfaceFinish = Pets.this.addSetting(new ModeSetting(var2 + " finish", "Companion surface finish", var4[0], var4));
         this.useTheFinishPalette = Pets.this.addSetting(new BooleanSetting(var2 + " natural colours", "Use the finish palette", false));
         this.mainCompanionColor = Pets.this.addSetting(new ColorSetting(var2 + " colour", "Main companion color", -1459895));
         this.magicGlowStrengthInPercent = Pets.this.addSetting(
            new NumberSetting(var2 + " glow", "Magic glow strength in percent", (double)var3, 0.0, 100.0, 5.0)
         );
         this.companionGlowColor = Pets.this.addSetting(new ColorSetting(var2 + " glow colour", "Companion glow color", -1459895));
         this.companionSurfaceFinish.visibleWhen(() -> Pets.this.pet.pAsBoolean(var2) && !Pets.this.realism.getValue());
         this.useTheFinishPalette.visibleWhen(() -> this.companionSurfaceFinish.isVisible());
         this.mainCompanionColor.visibleWhen(() -> this.companionSurfaceFinish.isVisible() && !this.useTheFinishPalette.getValue());
         this.magicGlowStrengthInPercent.visibleWhen(() -> this.companionSurfaceFinish.isVisible());
         this.companionGlowColor.visibleWhen(() -> this.companionSurfaceFinish.isVisible() && this.magicGlowStrengthInPercent.getValue() > 0.0);
      }

      int YAsInt() {
         int var1;
         if (this.useTheFinishPalette.getValue()) {
            String var2 = this.companionSurfaceFinish.getValue();

            var1 = switch (var2) {
               case "Scales" -> -10252932;
               case "Obsidian" -> -13356471;
               case "Gold" -> -2247330;
               case "Energy" -> -5341185;
               case "Crystal" -> -6496791;
               case "Wisp" -> -2037525;
               case "Silk" -> -4412714;
               default -> -5054483;
            };
         } else {
            var1 = this.mainCompanionColor.getValue().equals(this.mainCompanionColor.getDefaultValue()) ? bd.w : this.mainCompanionColor.getValue();
         }

         String var4 = this.companionSurfaceFinish.getValue();

         return switch (var4) {
            case "Gold" -> bg.xAsInt(var1, -12168, 0.4);
            case "Obsidian" -> bg.xAsInt(var1, -15724259, 0.65);
            case "Crystal" -> bg.xAsInt(var1, -1639937, 0.25);
            case "Silk" -> bg.xAsInt(var1, -1122056, 0.18);
            default -> var1;
         };
      }

      int eAsInt() {
         return this.companionGlowColor.getValue().equals(this.companionGlowColor.getDefaultValue()) ? bd.w : this.companionGlowColor.getValue();
      }
   }

   private static record Type_v(Vec3d point, double birth, double lifetime, double scale) {
   }

   public static record Type_w(
      String style,
      String finish,
      boolean realism,
      Vec3d center,
      Vec3d view,
      Quaternionf rotation,
      double scale,
      double time,
      double speed,
      double wings,
      double gazeYaw,
      double gazePitch,
      double reaction,
      int body,
      int glow,
      double glowStrength,
      Vec3d shadow,
      double shadowAlpha,
      List<Pets.Type_v> sparks,
      d5.Type_e label
   ) {
   }
}
