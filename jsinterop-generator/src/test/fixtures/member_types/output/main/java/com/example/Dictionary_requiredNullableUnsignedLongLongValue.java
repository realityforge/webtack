package com.example;

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
public interface Dictionary_requiredNullableUnsignedLongLongValue {
  @JsOverlay
  @Nonnull
  static Dictionary_requiredNullableUnsignedLongLongValue create(
      @Nullable final Double requiredNullableUnsignedLongLongValue) {
    return Js.<Dictionary_requiredNullableUnsignedLongLongValue>uncheckedCast( JsPropertyMap.of() ).requiredNullableUnsignedLongLongValue( requiredNullableUnsignedLongLongValue );
  }

  @JsProperty(
      name = "requiredNullableUnsignedLongLongValue"
  )
  @Nullable
  Double requiredNullableUnsignedLongLongValue();

  @JsProperty
  void setRequiredNullableUnsignedLongLongValue(
      @Nullable Double requiredNullableUnsignedLongLongValue);

  @JsOverlay
  @Nonnull
  default Dictionary_requiredNullableUnsignedLongLongValue requiredNullableUnsignedLongLongValue(
      @Nullable final Double requiredNullableUnsignedLongLongValue) {
    setRequiredNullableUnsignedLongLongValue( requiredNullableUnsignedLongLongValue );
    return this;
  }
}
