package com.larpclient.module;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Objects;

public class NumberSetting extends Setting<Double> {
   private final double minValue;
   private final double maxValue;
   private final double step;
   private NumberSetting y;
   private NumberSetting Z;
   private String b;

   public NumberSetting(String var1, String var2, double var3, double var5, double var7, double var9) {
      super(var1, var2, var3);
      this.minValue = var5;
      this.maxValue = var7;
      this.step = var9;
   }

   public void setClampedValue(Double var1) {
      if (var1 != null && Double.isFinite(var1)) {
         var1 = Math.max(this.minValue, Math.min(this.maxValue, var1));
         var1 = this.minValue + (double)Math.round((var1 - this.minValue) / this.step) * this.step;
         var1 = (double)Math.round(var1 * 1000.0) / 1000.0;
         super.setValue(Math.clamp(var1, this.minValue, this.maxValue));
      } else {
         throw new IllegalArgumentException("Non-finite number");
      }
   }

   public NumberSetting WAsNumberSetting(NumberSetting var1) {
      if (var1 != this && var1.minValue == this.minValue && var1.maxValue == this.maxValue && var1.step == this.step) {
         this.y = var1;
         var1.Z = this;
         return this;
      } else {
         throw new IllegalArgumentException("Range bounds must share limits and step");
      }
   }

   public NumberSetting OAsNumberSetting() {
      return this.y;
   }

   public boolean rAsBoolean() {
      return this.Z != null;
   }

   public NumberSetting dAsNumberSetting(String var1) {
      this.b = Objects.requireNonNull(var1);
      return this;
   }

   public boolean PAsBoolean() {
      return this.b != null;
   }

   public String SAsString() {
      return this.b;
   }

   public String XAsString() {
      if (this.y != null) {
         return String.format(Locale.US, "%.1f - %.1f", this.getValue(), this.y.getValue());
      } else {
         return this.b != null ? String.format(Locale.US, "%.1f%s", this.getValue(), this.b) : this.getValueAsString();
      }
   }

   public String JAsString() {
      if (this.y != null) {
         return String.format(Locale.US, "%.1f - %.1f", this.maxValue, this.maxValue);
      } else {
         return this.b != null ? String.format(Locale.US, "%.1f%s", this.maxValue, this.b) : this.getValueAsString();
      }
   }

   public double getMin() {
      return this.minValue;
   }

   public double getMax() {
      return this.maxValue;
   }

   public double getStep() {
      return this.step;
   }

   public double getNormalizedValue() {
      return (this.getValue() - this.minValue) / (this.maxValue - this.minValue);
   }

   public void vAsVoid(double var1) {
      this.setClampedValue(this.minValue + (this.maxValue - this.minValue) * Math.max(0.0, Math.min(1.0, var1)));
   }

   public String getEditValueAsString() {
      return BigDecimal.valueOf(this.getValue()).stripTrailingZeros().toPlainString();
   }

   public void setFromEditString(String var1) {
      double var2 = Double.parseDouble(var1);
      if (Double.compare(var2, this.getValue()) != 0) {
         this.setClampedValue(var2);
      }
   }

   @Override
   public String getValueAsString() {
      return this.step >= 1.0 ? String.valueOf((int)this.getValue().doubleValue()) : String.format(Locale.ROOT, "%.1f", this.getValue());
   }
}
