package com.example;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

@Generated("org.realityforge.webtack")
@JsType(
    isNative = true,
    namespace = JsPackage.GLOBAL,
    name = "ClipboardItemData"
)
public class ClipboardItemData {
  protected ClipboardItemData() {
  }

  @JsProperty(
      name = "data"
  )
  @Nonnull
  public native String data();
}
