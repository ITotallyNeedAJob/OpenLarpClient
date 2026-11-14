package com.larpclient;

import com.larpclient.module.BooleanSetting;
import com.larpclient.module.Category;
import com.larpclient.module.Module;
import com.larpclient.module.NumberSetting;
import com.larpclient.module.Setting;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import org.joml.Quaternionf;

public final class CryptoPets extends Module {
   public static final int PetEffectRenderer = 12;
   private static CryptoPets Z;
   private final Map<BitcoinPetMesh.Type_w, BooleanSetting> I = new LinkedHashMap<>();
   private final NumberSetting count = this.addSetting(new NumberSetting("Count", "How many coins circle you", 4.0, 1.0, 12.0, 1.0));
   private final NumberSetting size = this.addSetting(new NumberSetting("Size", "Coin size", 1.3, 0.5, 2.5, 0.05));
   private final NumberSetting radius = this.addSetting(new NumberSetting("Radius", "Distance from you in blocks", 1.2, 0.6, 3.0, 0.05));
   private final NumberSetting orbitSpeed = this.addSetting(new NumberSetting("Orbit speed", "How fast the ring turns", 1.0, 0.0, 3.0, 0.05));
   private final BooleanSetting sparkleTrail = this.addSetting(new BooleanSetting("Sparkle trail", "Sparks follow each coin", true));
   private final BooleanSetting inFirstPerson = this.addSetting(new BooleanSetting("In first person", "Show the coins in first person", true));
   private Object L;
   private Object B;
   private final double[] DropletTrail = new double[12];
   private final double[] J = new double[12];
   private double U;
   private double HexEdgeloop = 1.0;
   private double n;
   private double w;
   private double s;
   private boolean r;
   private boolean A;
   private final ArrayDeque<CryptoPets.Type_b> ChinaHatCodec = new ArrayDeque<>();
   private final Random h = new Random(1129470288L);
   private CryptoPets.Type_f m;

   public CryptoPets() {
      super("Crypto Pets", "Spinning crypto coins that circle you", Category.RENDER, -1);
      Z = this;

      for (BitcoinPetMesh.Type_w var2 : BitcoinPetMesh.h) {
         this.I.put(var2, this.addSetting(new BooleanSetting(var2.style(), "Include " + var2.style() + " coins", true)));
      }
   }

   public static CryptoPets IAsCryptoPets() {
      return Z;
   }

   public List<Setting<?>> mAsList() {
      return List.copyOf(this.I.values());
   }

   public List<Setting<?>> DAsList() {
      return List.of(this.count, this.size, this.radius, this.orbitSpeed, this.sparkleTrail, this.inFirstPerson);
   }

   @Override
   public void onEnable() {
      this.sAsVoid();
      ClientLog.XAsVoid("Pets", "Crypto Pets activated: {} coins", this.count.getValue().intValue());
   }

   @Override
   public void onDisable() {
      this.sAsVoid();
   }

   public void sAsVoid() {
      this.m = null;
      this.L = this.B = null;
      this.i.clear();
      this.s = 0.0;
      this.n = this.U = 0.0;
      this.p = 1.0;
      this.r = this.A = false;
      Arrays.fill(this.l, 0.0);
      Arrays.fill(this.J, 0.0);
      this.w = mb.RAsDouble();
      this.h.setSeed(1129470288L);
   }

   private List<BitcoinPetMesh.Type_w> zAsList() {
      ArrayList var1 = new ArrayList();
      this.I.forEach((var1xx, var2) -> {
         if (var2.getValue()) {
            var1.add(var1xx);
         }
      });
      return var1;
   }

   public void WAsVoid(float var1, CameraRenderState var2) {
      if (this.isEnabled()) {
         this.m = null;
         if (this.isRuntimeActive() && this.isInGame()) {
            MinecraftClient var3 = getMc();
            if (this.L != var3.world || this.B != var3.player) {
               this.sAsVoid();
               this.L = var3.world;
               this.B = var3.player;
            }

            double var4 = Math.clamp(mb.RAsDouble() - this.w, 0.0, 0.1);
            this.w = mb.RAsDouble();
            this.n += var4;
            List var6 = this.zAsList();
            int var7 = var6.isEmpty() ? 0 : this.count.getValue().intValue();
            double var8 = this.A ? 1.0 - Math.exp(-6.0 * var4) : 1.0;

            for (int var10 = 0; var10 < 12; var10++) {
               if (var10 < var7) {
                  this.l[var10] = this.l[var10] + ((Math.PI * 2) * (double)var10 / (double)var7 - this.l[var10]) * var8;
               }

               this.J[var10] = this.J[var10] + ((double)(var10 < var7 ? 1 : 0) - this.J[var10]) * var8;
            }

            this.A = true;
            this.U = (this.U + var4 * this.orbitSpeed.getValue() * 0.9) % (Math.PI * 2);
            if (var3.player.handSwinging && !this.r) {
               this.p = 0.0;
            }

            this.r = var3.player.handSwinging;
            this.p = Math.min(1.0, this.p + var4 / 0.65);
            this.i.removeIf(var1x -> this.n - var1x.birth > var1x.lifetime);
            if (!this.sparkleTrail.getValue()) {
               this.i.clear();
               this.s = 0.0;
            }

            int var29 = var3.options.getPerspective().isFirstPerson() && !this.inFirstPerson.getValue() ? 0 : 1;
            if (var29 != 0 && !var6.isEmpty()) {
               BitcoinPetMesh.hAsVoid();
               Vec3d var11 = var3.player.getCameraPosVec(var1);
               double var12 = this.p < 1.0 ? (1.0 - Math.pow(1.0 - this.p, 3.0)) * Math.PI * 2.0 : 0.0;
               ArrayList var14 = new ArrayList();

               for (int var15 = 0; var15 < 12; var15++) {
                  if (!(this.J[var15] < 0.01)) {
                     BitcoinPetMesh.Type_w var16 = (BitcoinPetMesh.Type_w)var6.get(var15 % var6.size());
                     double var17 = scale(var16, this.size.getValue()) * this.J[var15];
                     double var19 = this.U + this.l[var15];
                     Vec3d var21 = var11.add(
                        Math.cos(var19) * this.radius.getValue(),
                        -0.35 + Math.sin(this.n * 2.1 + (double)var15 * 1.7) * 0.06,
                        Math.sin(var19) * this.radius.getValue()
                     );
                     BlockHitResult var22 = var3.world.raycast(new RaycastContext(var11, var21, ShapeType.COLLIDER, FluidHandling.NONE, var3.player));
                     if (var22.getType() == Type.BLOCK) {
                        var21 = var22.getPos().subtract(var21.subtract(var11).normalize().multiply(0.3 * var17));
                     }

                     BlockHitResult var23 = var3.world
                        .raycast(new RaycastContext(var21, var21.add(0.0, -4.5, 0.0), ShapeType.COLLIDER, FluidHandling.ANY, var3.player));
                     Vec3d var24 = var23.getType() == Type.BLOCK ? var23.getPos().add(0.0, 0.012, 0.0) : null;
                     double var25 = var24 == null ? 0.0 : Math.pow(Math.max(0.0, 1.0 - (var21.y - var24.y) / 4.5), 2.0) * 0.4 * this.J[var15];
                     Quaternionf var27 = new Quaternionf().rotationY((float)((this.n * 1.6 + (double)var15 * 0.9 + var12) % (Math.PI * 2)));
                     var14.add(new CryptoPets.Type_e(var16, var21, var27, var17, var24, var25));
                     if (this.sparkleTrail.getValue() && var4 > 0.0 && this.h.nextDouble() < var4 * 15.0) {
                        Vec3d var28 = var21.add(
                           this.h.nextGaussian() * 0.06 * var17, this.h.nextGaussian() * 0.06 * var17, this.h.nextGaussian() * 0.06 * var17
                        );
                        this.i.addLast(new CryptoPets.Type_b(var28, this.n, 0.8 + this.h.nextDouble() * 0.8, 0.8 + this.h.nextDouble() * 0.8, var16.spark()));

                        while (this.i.size() > 96) {
                           this.i.removeFirst();
                        }
                     }
                  }
               }

               this.m = new CryptoPets.Type_f(List.copyOf(var14), this.n, this.sparkleTrail.getValue() ? 0.5 : 0.0, List.copyOf(this.i));
            }
         }
      }
   }

   public static double scale(BitcoinPetMesh.Type_w var0, double var1) {
      return var1 * var0.scale();
   }

   public boolean tAsBoolean() {
      return this.isEnabled() ? this.m != null && this.m.glowStrength > 0.0 && !this.m.coins.isEmpty() : false;
   }

   public void rAsVoid(EffectGeometryRenderer var1, boolean var2) {
      if (this.isEnabled()) {
         CryptoPets.Type_f var3 = this.m;
         if (var3 != null && (!var2 || !(var3.glowStrength <= 0.0))) {
            for (CryptoPets.Type_e var5 : var3.coins) {
               if (!var2) {
                  if (var5.shadow != null) {
                     var1.oAsVoid(var5.shadow, 0.18 * var5.scale, 0.16 * var5.scale, bg.XAsInt(-16777216, var5.shadowAlpha));
                  }

                  BitcoinPetMesh.uAsVoid(var1, var5.kind, var5.center, var5.rotation, (float)var5.scale);
               }
            }

            for (CryptoPets.Type_b var9 : var3.sparks) {
               double var6 = Math.clamp(1.0 - (var3.time - var9.birth) / var9.lifetime, 0.0, 1.0);
               var1.BAsVoid(
                  var9.point.x,
                  var9.point.y + 0.08 * (1.0 - var6),
                  var9.point.z,
                  0.035 * this.size.getValue() * var6 * var9.scale,
                  var3.time,
                  bg.XAsInt(var9.color, var6 * var6 * var3.glowStrength)
               );
            }
         }
      }
   }

   private static record Type_b(Vec3d point, double birth, double lifetime, double scale, int color) {
   }

   public static record Type_e(BitcoinPetMesh.Type_w kind, Vec3d center, Quaternionf rotation, double scale, Vec3d shadow, double shadowAlpha) {
   }

   public static record Type_f(List<CryptoPets.Type_e> coins, double time, double glowStrength, List<CryptoPets.Type_b> sparks) {
   }
}
