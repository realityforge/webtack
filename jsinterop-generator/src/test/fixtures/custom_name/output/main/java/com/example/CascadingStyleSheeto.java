package com.example;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@Generated("org.realityforge.webtack")
@JsType(
    isNative = true,
    namespace = JsPackage.GLOBAL,
    name = "CSS"
)
public final class CascadingStyleSheeto {
  private CascadingStyleSheeto() {
  }

  @Nonnull
  public native String escape(@Nonnull String ident);
}
