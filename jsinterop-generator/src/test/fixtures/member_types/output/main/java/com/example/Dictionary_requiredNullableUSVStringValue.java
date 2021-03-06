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
public interface Dictionary_requiredNullableUSVStringValue {
  @JsOverlay
  @Nonnull
  static Dictionary_requiredNullableUSVStringValue create(
      @Nullable final String requiredNullableUSVStringValue) {
    return Js.<Dictionary_requiredNullableUSVStringValue>uncheckedCast( JsPropertyMap.of() ).requiredNullableUSVStringValue( requiredNullableUSVStringValue );
  }

  @JsProperty(
      name = "requiredNullableUSVStringValue"
  )
  @Nullable
  String requiredNullableUSVStringValue();

  @JsProperty
  void setRequiredNullableUSVStringValue(@Nullable String requiredNullableUSVStringValue);

  @JsOverlay
  @Nonnull
  default Dictionary_requiredNullableUSVStringValue requiredNullableUSVStringValue(
      @Nullable final String requiredNullableUSVStringValue) {
    setRequiredNullableUSVStringValue( requiredNullableUSVStringValue );
    return this;
  }
}
