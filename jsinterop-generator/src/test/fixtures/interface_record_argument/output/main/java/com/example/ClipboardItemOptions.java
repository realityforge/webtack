package com.example;

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
public interface ClipboardItemOptions {
  @JsOverlay
  @Nonnull
  static ClipboardItemOptions create() {
    return Js.uncheckedCast( JsPropertyMap.of() );
  }

  @JsProperty(
      name = "presentationStyle"
  )
  String presentationStyle();

  @JsProperty
  void setPresentationStyle(@Nonnull String presentationStyle);

  @JsOverlay
  @Nonnull
  default ClipboardItemOptions presentationStyle(@Nonnull final String presentationStyle) {
    setPresentationStyle( presentationStyle );
    return this;
  }
}
