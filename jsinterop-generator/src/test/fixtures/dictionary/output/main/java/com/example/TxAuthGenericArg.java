package com.example;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

/**
 * This tests that lowercase name converted to uppercase when converted into java.
 */
@Generated("org.realityforge.webtack")
@JsType(
    isNative = true,
    namespace = JsPackage.GLOBAL,
    name = "Object"
)
public interface TxAuthGenericArg {
  @JsOverlay
  @Nonnull
  static TxAuthGenericArg create(@Nonnull final String contentType) {
    return Js.<TxAuthGenericArg>uncheckedCast( JsPropertyMap.of() ).contentType( contentType );
  }

  @JsProperty(
      name = "contentType"
  )
  @Nonnull
  String contentType();

  @JsProperty
  void setContentType(@Nonnull String contentType);

  @JsOverlay
  @Nonnull
  default TxAuthGenericArg contentType(@Nonnull final String contentType) {
    setContentType( contentType );
    return this;
  }
}
