package com.example;

import elemental3.lang.JsArray;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
public interface Dictionary_requiredNullableFloatSequenceValue {
  @JsOverlay
  @Nonnull
  static Dictionary_requiredNullableFloatSequenceValue create(
      @Nullable final JsArray<Double> requiredNullableFloatSequenceValue) {
    return Js.<Dictionary_requiredNullableFloatSequenceValue>uncheckedCast( JsPropertyMap.of() ).requiredNullableFloatSequenceValue( requiredNullableFloatSequenceValue );
  }

  @JsOverlay
  @Nonnull
  static Dictionary_requiredNullableFloatSequenceValue create(
      @Nullable final double[] requiredNullableFloatSequenceValue) {
    return Js.<Dictionary_requiredNullableFloatSequenceValue>uncheckedCast( JsPropertyMap.of() ).requiredNullableFloatSequenceValue( requiredNullableFloatSequenceValue );
  }

  @JsProperty(
      name = "requiredNullableFloatSequenceValue"
  )
  @Nullable
  JsArray<Double> requiredNullableFloatSequenceValue();

  @JsProperty
  void setRequiredNullableFloatSequenceValue(
      @Nullable JsArray<Double> requiredNullableFloatSequenceValue);

  @JsOverlay
  @Nonnull
  default Dictionary_requiredNullableFloatSequenceValue requiredNullableFloatSequenceValue(
      @Nullable final JsArray<Double> requiredNullableFloatSequenceValue) {
    setRequiredNullableFloatSequenceValue( requiredNullableFloatSequenceValue );
    return this;
  }

  @JsOverlay
  default void setRequiredNullableFloatSequenceValue(
      @Nullable final double... requiredNullableFloatSequenceValue) {
    setRequiredNullableFloatSequenceValue( Js.<JsArray<Double>>uncheckedCast( requiredNullableFloatSequenceValue ) );
  }

  @JsOverlay
  @Nonnull
  default Dictionary_requiredNullableFloatSequenceValue requiredNullableFloatSequenceValue(
      @Nullable final double... requiredNullableFloatSequenceValue) {
    setRequiredNullableFloatSequenceValue( requiredNullableFloatSequenceValue );
    return this;
  }
}
