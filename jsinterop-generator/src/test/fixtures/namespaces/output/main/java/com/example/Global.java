package com.example;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * Accessor for the global <b>globalThis</b> property also know as the global object.
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/globalThis">globalThis - MDN</a>
 */
@Generated("org.realityforge.webtack")
@JsType(
    isNative = true,
    namespace = JsPackage.GLOBAL,
    name = "goog.global"
)
public final class Global {
  private static GlobalWindow globalThis;

  private Global() {
  }

  /**
   * Accessor for the global <b>globalThis</b> property contains the global <i>this</i> value, which is akin to the global object.
   *
   * @return the global object
   * @see <a href="https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/globalThis">globalThis - MDN</a>
   */
  @JsOverlay
  @Nonnull
  public static GlobalWindow globalThis() {
    return globalThis;
  }

  public static boolean closed() {
    return globalThis().closed();
  }

  @Nonnull
  public static Console console() {
    return globalThis().console();
  }

  @Nonnull
  public static CSS css() {
    return globalThis().css();
  }

  @Nonnull
  public static WebAssembly webAssembly() {
    return globalThis().webAssembly();
  }
}
