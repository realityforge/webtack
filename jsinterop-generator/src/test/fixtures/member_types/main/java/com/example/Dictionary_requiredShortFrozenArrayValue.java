package com.example;

import elemental2.core.JsArray;
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
    name = "?"
)
public interface Dictionary_requiredShortFrozenArrayValue {
  @JsOverlay
  @Nonnull
  static Dictionary_requiredShortFrozenArrayValue create(
      @Nonnull final JsArray<Double> requiredShortFrozenArrayValue) {
    return Js.<Dictionary_requiredShortFrozenArrayValue>uncheckedCast( JsPropertyMap.of() ).requiredShortFrozenArrayValue( requiredShortFrozenArrayValue );
  }

  @JsProperty
  @Nonnull
  JsArray<Double> getRequiredShortFrozenArrayValue();

  @JsProperty
  void setRequiredShortFrozenArrayValue(@Nonnull JsArray<Double> requiredShortFrozenArrayValue);

  @JsOverlay
  @Nonnull
  default Dictionary_requiredShortFrozenArrayValue requiredShortFrozenArrayValue(
      @Nonnull final JsArray<Double> requiredShortFrozenArrayValue) {
    setRequiredShortFrozenArrayValue( requiredShortFrozenArrayValue );
    return this;
  }
}
