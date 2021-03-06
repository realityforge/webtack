package com.example;

import elemental3.lang.JsArray;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

@Generated("org.realityforge.webtack")
@JsType(
    isNative = true,
    namespace = JsPackage.GLOBAL,
    name = "Object"
)
public interface Dictionary_requiredShortSequenceValue {
  @JsOverlay
  @Nonnull
  static Dictionary_requiredShortSequenceValue create(
      @Nonnull final JsArray<Double> requiredShortSequenceValue) {
    return Js.<Dictionary_requiredShortSequenceValue>uncheckedCast( JsPropertyMap.of() ).requiredShortSequenceValue( requiredShortSequenceValue );
  }

  @JsOverlay
  @Nonnull
  static Dictionary_requiredShortSequenceValue create(
      @Nonnull final double[] requiredShortSequenceValue) {
    return Js.<Dictionary_requiredShortSequenceValue>uncheckedCast( JsPropertyMap.of() ).requiredShortSequenceValue( requiredShortSequenceValue );
  }

  @JsProperty(
      name = "requiredShortSequenceValue"
  )
  @Nonnull
  JsArray<Double> requiredShortSequenceValue();

  @JsProperty
  void setRequiredShortSequenceValue(@Nonnull JsArray<Double> requiredShortSequenceValue);

  @JsOverlay
  @Nonnull
  default Dictionary_requiredShortSequenceValue requiredShortSequenceValue(
      @Nonnull final JsArray<Double> requiredShortSequenceValue) {
    setRequiredShortSequenceValue( requiredShortSequenceValue );
    return this;
  }

  @JsOverlay
  default void setRequiredShortSequenceValue(@Nonnull final double... requiredShortSequenceValue) {
    setRequiredShortSequenceValue( Js.<JsArray<Double>>uncheckedCast( requiredShortSequenceValue ) );
  }

  @JsOverlay
  @Nonnull
  default Dictionary_requiredShortSequenceValue requiredShortSequenceValue(
      @Nonnull final double... requiredShortSequenceValue) {
    setRequiredShortSequenceValue( requiredShortSequenceValue );
    return this;
  }
}
