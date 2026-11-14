package com.larpclient;

import com.larpclient.module.Module;
import com.larpclient.module.Setting;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

public final class WorkspaceSaveStatus {
   public static final int D = 64;
   private static long W = -1L;
   private final BooleanSupplier d;
   private final Predicate<HudRenderer.Type_m> p;
   private final List<Module> H;
   private final HudEditorController M;
   private final ArrayDeque<WorkspaceSaveStatus.Type_m> z = new ArrayDeque<>();
   private final ArrayDeque<WorkspaceSaveStatus.Type_m> N = new ArrayDeque<>();
   private WorkspaceSaveStatus.Type_m v;
   private long m;
   private long S;
   private boolean G;
   private boolean a;
   private String g;

   public WorkspaceSaveStatus(BooleanSupplier var1, Predicate<HudRenderer.Type_m> var2) {
      this(var1, var2, List.of());
   }

   public WorkspaceSaveStatus(BooleanSupplier var1, Predicate<HudRenderer.Type_m> var2, List<Module> var3) {
      this.m = GuiWorkspace.m;
      this.g = "Saved";
      this.H = List.copyOf(var3);
      this.d = var1;
      this.p = var2;
      this.G = W == GuiWorkspace.m;
      if (this.G) {
         this.g = "Save failed. Retry";
      }

      this.M = new HudEditorController(this::bAsBoolean, var0 -> {
      }, () -> {
      });
      this.M.NAsVoid(var2);
   }

   public HudEditorController gAsOz() {
      return this.M;
   }

   public HudRenderer.Type_m aAsLp_Type_m() {
      return this.M.nAsLp_Type_m();
   }

   public boolean CAsBoolean() {
      return !this.z.isEmpty();
   }

   public boolean yAsBoolean() {
      return !this.N.isEmpty();
   }

   public int cAsInt() {
      return this.z.size();
   }

   public boolean jAsBoolean() {
      return this.G;
   }

   public String EAsString() {
      return this.g;
   }

   public void eAsVoid(HudRenderer.Type_m var1) {
      this.M.pAsVoid(var1);
      this.bAsBoolean();
   }

   public void rAsVoid() {
      this.KAsBoolean();
      if (this.v == null) {
         this.v = WorkspaceSaveStatus.Type_m.capture(this.H);
      }
   }

   public void VAsVoid() {
      this.a = true;
   }

   public void WAsVoid(Runnable var1) {
      this.rAsVoid();
      var1.run();
      this.bAsBoolean();
   }

   public boolean bAsBoolean() {
      if (this.m != GuiWorkspace.m) {
         this.KAsBoolean();
         return true;
      } else {
         byte var1 = (byte)(this.a ? 1 : 0);
         if (this.v != null) {
            if (!this.v.equals(WorkspaceSaveStatus.Type_m.capture(this.H))) {
               AAsVoid(this.z, this.v);
               this.N.clear();
               var1 = 1;
            }

            this.v = null;
         }

         this.G |= var1 != 0;
         this.a = false;
         return var1 != 0 ? this.EAsBoolean() : !this.G;
      }
   }

   public boolean EAsBoolean() {
      if (!this.G) {
         return true;
      } else {
         this.S++;
         if (this.d.getAsBoolean()) {
            this.G = false;
            W = -1L;
            this.g = "Saved";
            ClientLog.XAsVoid("HUD editor", "Saved completed workspace edit");
            return true;
         } else {
            W = this.m;
            this.g = "Save failed. Retry";
            ClientLog.oAsVoid("HUD editor", "Workspace save failed; pending edits retained for retry");
            return false;
         }
      }
   }

   public boolean xAsBoolean() {
      return this.sAsBoolean(this.z, this.N);
   }

   public boolean sAsBoolean() {
      return this.sAsBoolean(this.N, this.z);
   }

   private boolean sAsBoolean(ArrayDeque<WorkspaceSaveStatus.Type_m> var1, ArrayDeque<WorkspaceSaveStatus.Type_m> var2) {
      this.M.NAsBoolean(0);
      this.bAsBoolean();
      if (var1.isEmpty()) {
         return false;
      } else {
         AAsVoid(var2, WorkspaceSaveStatus.Type_m.capture(this.H));
         var1.removeLast().restore();
         this.G = true;
         this.EAsBoolean();
         return true;
      }
   }

   private static void AAsVoid(ArrayDeque<WorkspaceSaveStatus.Type_m> var0, WorkspaceSaveStatus.Type_m var1) {
      if (var0.size() == 64) {
         var0.removeFirst();
      }

      var0.addLast(var1);
   }

   public void dAsVoid(int var1, int var2, int var3, int var4) {
      HudRenderer.Type_m var5 = this.aAsLp_Type_m();
      if (var5 != null && !this.p.test(var5)) {
         this.WAsVoid(
            () -> var5.SAsVoid(WidgetLayoutUtil.LAsInt(var5.PAsInt(var3) + var1, var5.vAsInt(), var3), WidgetLayoutUtil.LAsInt(var5.ZAsInt(var4) + var2, var5.TAsInt(), var4), var3, var4)
         );
      }
   }

   public void SAsVoid() {
      this.WAsVoid(() -> HudRenderer.dAsVoid(var1 -> !this.p.test(var1)));
   }

   public void JAsVoid(boolean var1, boolean var2) {
      GuiWorkspace.Type_a var3 = GuiWorkspace.c;
      if (var3.snapping() != var1 || var3.grid() != var2) {
         GuiWorkspace.c = new GuiWorkspace.Type_a(var3.panel(), var3.collapsed(), var1, var2);
         this.G = true;
         this.EAsBoolean();
      }
   }

   public boolean KAsBoolean() {
      if (this.m == GuiWorkspace.m) {
         return false;
      } else {
         this.M.eAsVoid(true, true);
         this.z.clear();
         this.N.clear();
         this.v = null;
         this.G = this.a = false;
         this.g = "Saved";
         this.m = GuiWorkspace.m;
         return true;
      }
   }

   public void kAsVoid() {
      this.M.NAsBoolean(0);
      this.bAsBoolean();
   }

   public void LAsVoid() {
      long var1 = this.S;
      this.M.xAsVoid();
      this.bAsBoolean();
      if (this.S == var1) {
         this.EAsBoolean();
      }
   }

   public static enum Type_d {
      NONE,
      WIDGETS,
      STYLE,
      POSITION,
      OPTIONS;

      private Type_d() {
      }
   }

   public static record Type_f(WidgetLayoutUtil.Type_p dock, WidgetLayoutUtil.Type_p panel, WidgetLayoutUtil.Type_p body, WidgetLayoutUtil.Type_p retry, List<WidgetLayoutUtil.Type_p> tools, int width, int height) {
      public Type_f(WidgetLayoutUtil.Type_p dock, WidgetLayoutUtil.Type_p panel, WidgetLayoutUtil.Type_p body, WidgetLayoutUtil.Type_p retry, List<WidgetLayoutUtil.Type_p> tools, int width, int height) {
         tools = List.copyOf(tools);
         this.dock = dock;
         this.panel = panel;
         this.body = body;
         this.retry = retry;
         this.tools = tools;
         this.width = width;
         this.height = height;
      }

      public static WorkspaceSaveStatus.Type_f calculate(
         int var0, int var1, WorkspaceSaveStatus.Type_d var2, int var3, int var4, int var5, boolean var6, boolean var7
      ) {
         if (!var6) {
            return new WorkspaceSaveStatus.Type_f(null, null, null, null, List.of(), var0, var1);
         } else {
            int var8 = Math.min(12, Math.max(0, var0 / 4));
            int var9 = Math.max(1, Math.min(340, var0 - var8 * 2));
            WidgetLayoutUtil.Type_p var10 = new WidgetLayoutUtil.Type_p((var0 - var9) / 2, Math.max(0, var1 - var8 - 42), var9, Math.min(42, var1));
            ArrayList var11 = new ArrayList();
            int var12 = Math.max(2, var9 - 172);
            int[] var13 = new int[]{var12 / 2 + 4, 28, 28, 28, var12 - var12 / 2 - 4, 56};
            double var14 = Math.min(1.0, (double)Math.max(1, var9 - 32) / (double)Arrays.stream(var13).sum());
            int var16 = var10.x() + 6;

            for (int var20 : var13) {
               int var21 = Math.max(1, (int)Math.floor((double)var20 * var14));
               var11.add(new WidgetLayoutUtil.Type_p(var16, var10.y() + 6, var21, Math.min(30, var10.height())));
               var16 += var21 + 4;
            }

            WidgetLayoutUtil.Type_p var26 = var7 ? new WidgetLayoutUtil.Type_p((var0 - Math.min(208, var9)) / 2, Math.max(0, var10.y() - 29), Math.min(208, var9), 22) : null;
            if (var2 == WorkspaceSaveStatus.Type_d.NONE) {
               return new WorkspaceSaveStatus.Type_f(var10, null, null, var26, var11, var0, var1);
            } else {
               int var27 = Math.max(1, Math.min(var2 == WorkspaceSaveStatus.Type_d.WIDGETS ? 268 : 260, var0 - var8 * 2));
               int var28 = header(var2);
               int var29 = Math.max(1, var10.y() - (var7 ? 40 : 12) - var8);
               int var30 = Math.min(var28 + Math.min(Math.max(0, var3), 280) + 10, var29);
               int var22 = Math.clamp(var4 < 0 ? (long)var10.x() : (long)var4, var8, Math.max(var8, var0 - var8 - var27));
               int var23 = Math.clamp(var5 < 0 ? (long)(var10.y() - var30 - 12) : (long)var5, var8, Math.max(var8, var10.y() - (var7 ? 40 : 12) - var30));
               WidgetLayoutUtil.Type_p var24 = new WidgetLayoutUtil.Type_p(var22, var23, var27, var30);
               WidgetLayoutUtil.Type_p var25 = new WidgetLayoutUtil.Type_p(var22 + 10, var23 + Math.min(var28, var30), Math.max(1, var27 - 20), Math.max(0, var30 - var28 - 10));
               return new WorkspaceSaveStatus.Type_f(var10, var24, var25, var26, var11, var0, var1);
            }
         }
      }

      public static int header(WorkspaceSaveStatus.Type_d var0) {
         return var0 == WorkspaceSaveStatus.Type_d.WIDGETS
            ? 78
            : (var0 != WorkspaceSaveStatus.Type_d.STYLE && var0 != WorkspaceSaveStatus.Type_d.POSITION ? 45 : 76);
      }

      public boolean panelContains(double var1, double var3) {
         return this.panel != null && this.panel.contains(var1, var3, 0);
      }

      public boolean canvasContains(double var1, double var3) {
         return var1 >= 0.0
            && var1 < (double)this.width
            && var3 >= 0.0
            && var3 < (double)this.height
            && !this.panelContains(var1, var3)
            && (this.dock == null || !this.dock.contains(var1, var3, 0))
            && (this.retry == null || !this.retry.contains(var1, var3, 0));
      }

      public boolean controlContains(WidgetLayoutUtil.Type_p var1, double var2, double var4) {
         return this.body != null && this.body.contains(var2, var4, 0) && var1.contains(var2, var4, 0);
      }
   }

   public static record Type_m(
      Map<String, GuiWorkspace.Type_g> positions, Map<String, GuiWorkspace.Type_z> styles, Map<Setting<?>, Object> settings, Map<Module, Boolean> owners
   ) {
      public Type_m(
         Map<String, GuiWorkspace.Type_g> positions, Map<String, GuiWorkspace.Type_z> styles, Map<Setting<?>, Object> settings, Map<Module, Boolean> owners
      ) {
         positions = Map.copyOf(positions);
         styles = Map.copyOf(styles);
         settings = Map.copyOf(settings);
         owners = Map.copyOf(owners);
         this.positions = positions;
         this.styles = styles;
         this.settings = settings;
         this.owners = owners;
      }

      public static WorkspaceSaveStatus.Type_m capture() {
         return capture(List.of());
      }

      public static WorkspaceSaveStatus.Type_m capture(List<Module> var0) {
         LinkedHashMap var1 = new LinkedHashMap();
         LinkedHashMap var2 = new LinkedHashMap();

         for (Module var4 : var0) {
            var2.put(var4, Boolean.valueOf(var4.isEnabled()));

            for (Setting var6 : var4.getSettings()) {
               var1.put(var6, var6.getValue());
            }
         }

         return new WorkspaceSaveStatus.Type_m(GuiWorkspace.B, GuiWorkspace.v, var1, var2);
      }

      private static void restoreSetting(Setting<?> var0, Object var1) {
         if (!Objects.equals(var0.getValue(), var1)) {
            ((Setting<Object>)var0).setValue(var1);
         }
      }

      public void restore() {
         GuiWorkspace.B.clear();
         GuiWorkspace.B.putAll(this.positions);
         GuiWorkspace.v.clear();
         GuiWorkspace.v.putAll(this.styles);
         this.settings.forEach(WorkspaceSaveStatus.Type_m::restoreSetting);
         this.owners.forEach((var0, var1) -> {
            if (var0.isEnabled() != var1) {
               var0.setEnabled(var1);
            }
         });
      }
   }
}
