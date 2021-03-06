package com.example;

import javaemul.internal.annotations.DoNotAutobox;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.Any;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

@Generated("org.realityforge.webtack")
@JsType(
    isNative = true,
    namespace = JsPackage.GLOBAL,
    name = "Object"
)
public interface RequiredAnyDict {
  @JsOverlay
  @Nonnull
  static RequiredAnyDict create(@DoNotAutobox @Nullable final Object someValue) {
    return Js.<RequiredAnyDict>uncheckedCast( JsPropertyMap.of() ).someValue( someValue );
  }

  @JsProperty(
      name = "someValue"
  )
  @Nullable
  Any someValue();

  @JsProperty
  void setSomeValue(@DoNotAutobox @Nullable Object someValue);

  @JsOverlay
  @Nonnull
  default RequiredAnyDict someValue(@DoNotAutobox @Nullable final Object someValue) {
    setSomeValue( someValue );
    return this;
  }
}
