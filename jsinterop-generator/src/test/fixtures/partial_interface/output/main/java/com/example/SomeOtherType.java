package com.example;

import elemental3.lang.JsArray;
import elemental3.lang.JsIterator;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.Js;

/**
 * A test for a read-write maplike.
 * Also uses types that have a different boxed type and non-boxed type.
 */
@Generated("org.realityforge.webtack")
@JsType(
    isNative = true,
    namespace = JsPackage.GLOBAL,
    name = "SomeOtherType"
)
public final class SomeOtherType {
  private SomeOtherType() {
  }

  @JsOverlay
  @Nonnull
  public static SomeOtherType of(@Nonnull final Object object) {
    return Js.cast( object );
  }

  @JsProperty(
      name = "size"
  )
  public native int size();

  public native boolean has(int key);

  @Nullable
  public native Double get(int key);

  @Nonnull
  public native JsIterator<Double> keys();

  @Nonnull
  public native JsIterator<Double> values();

  @Nonnull
  public native JsIterator<Entry> entries();

  public native void forEach(@Nonnull ForEachCallback callback);

  public native void forEach(@Nonnull ForEachCallback2 callback);

  public native void forEach(@Nonnull ForEachCallback3 callback);

  public native void set(int key, int value);

  public native boolean delete(int key);

  public native void clear();

  @JsType(
      isNative = true,
      namespace = JsPackage.GLOBAL,
      name = "Array"
  )
  public static final class Entry extends JsArray<Object> {
    @JsOverlay
    public int key() {
      return getAtAsAny( 0 ).asInt();
    }

    @JsOverlay
    public int value() {
      return getAtAsAny( 1 ).asInt();
    }
  }

  @JsFunction
  @FunctionalInterface
  public interface ForEachCallback {
    void item(int value);
  }

  @JsFunction
  @FunctionalInterface
  public interface ForEachCallback2 {
    void item(int value, int key);
  }

  @JsFunction
  @FunctionalInterface
  public interface ForEachCallback3 {
    void item(int value, int key, @Nonnull SomeOtherType map);
  }
}
