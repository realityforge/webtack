package com.example;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import jsinterop.base.Js;

/**
 * Accessor for the global <b>globalThis</b> property also know as the global object.
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/globalThis">globalThis - MDN</a>
 */
@Generated("org.realityforge.webtack")
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
  @Nonnull
  public static GlobalWindow globalThis() {
    return Js.uncheckedCast( Js.global() );
  }

  public static boolean closed() {
    return globalThis().closed();
  }

  @Nonnull
  public static ConsoleNamespace console() {
    return globalThis().console();
  }

  @Nonnull
  public static CSSNamespace css() {
    return globalThis().css();
  }

  @Nonnull
  public static WebAssemblyNamespace webAssembly() {
    return globalThis().webAssembly();
  }

  @Nonnull
  public static JsMathNamespace math() {
    return globalThis().math();
  }
}
