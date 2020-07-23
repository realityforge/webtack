package com.example;

import elemental2.core.JsArray;
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
    name = "?"
)
public interface Dictionary_requiredNullableFloatFrozenArrayValue {
  @JsOverlay
  @Nonnull
  static Dictionary_requiredNullableFloatFrozenArrayValue create(
      @Nullable final JsArray<Double> requiredNullableFloatFrozenArrayValue) {
    return Js.<Dictionary_requiredNullableFloatFrozenArrayValue>uncheckedCast( JsPropertyMap.of() ).requiredNullableFloatFrozenArrayValue( requiredNullableFloatFrozenArrayValue );
  }

  @JsProperty
  @Nullable
  JsArray<Double> getRequiredNullableFloatFrozenArrayValue();

  @JsProperty
  void setRequiredNullableFloatFrozenArrayValue(
      @Nullable JsArray<Double> requiredNullableFloatFrozenArrayValue);

  @JsOverlay
  @Nonnull
  default Dictionary_requiredNullableFloatFrozenArrayValue requiredNullableFloatFrozenArrayValue(
      @Nullable final JsArray<Double> requiredNullableFloatFrozenArrayValue) {
    setRequiredNullableFloatFrozenArrayValue( requiredNullableFloatFrozenArrayValue );
    return this;
  }
}
