package com.larpclient.module;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public abstract class Setting<T> {
   private final String name;
   private final String description;
   private T value;
   private final T defaultValue;
   private Consumer<T> changeListener;
   private BooleanSupplier visibilityPredicate = () -> true;
   private String displayNameOverride;

   public Setting<T> visibleWhen(BooleanSupplier var1) {
      this.visibilityPredicate = var1;
      return this;
   }

   public boolean isVisible() {
      return this.visibilityPredicate.getAsBoolean();
   }

   public Setting(String var1, String var2, T var3) {
      this.name = var1;
      this.description = var2;
      this.value = var3;
      this.defaultValue = var3;
   }

   public String getName() {
      return this.name;
   }

   public String getDisplayName() {
      return this.displayNameOverride != null ? this.displayNameOverride : this.name;
   }

   public Setting<T> displayName(String var1) {
      this.displayNameOverride = var1;
      return this;
   }

   public String getDescription() {
      return this.description;
   }

   public T getValue() {
      return this.value;
   }

   public void setValue(T var1) {
      this.value = var1;
      if (this.changeListener != null) {
         this.changeListener.accept(var1);
      }
   }

   public T getDefaultValue() {
      return this.defaultValue;
   }

   public void reset() {
      this.setValue(this.defaultValue);
   }

   public Setting<T> onChanged(Consumer<T> var1) {
      this.changeListener = var1;
      return this;
   }

   public abstract String getValueAsString();
}
