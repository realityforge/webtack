/**
 * Documentation for FullscreenNavigationUI.
 *
 * @version 23
 * @see http://example.com/#FullscreenNavigationUI
 */
enum FullscreenNavigationUI {
  /**
   * Documentation for "auto" value.
   *
   * @version 23
   * @see http://example.com/#FullscreenNavigationUI.auto
   */
  "auto",
  /**
   * Documentation for "hide" value.
   *
   * @version 23
   * @see http://example.com/#FullscreenNavigationUI.hide
   */
  "hide",
  /**
   * Documentation for "show" value.
   *
   * @version 23
   * @see http://example.com/#FullscreenNavigationUI.show
   */
  "show"
};

/**
 * The glorious WebAssembly namespace.
 *
 * @data 2019
 */
[Exposed=(Window,Worker,Worklet)]
namespace WebAssembly {
  /**
   * Compile the buffer.
   *
   * @param bytes the bytes to compile
   * @return the promise for the compiled module
   * @see http://example.com
   */
  Promise<Module> compile( BufferSource bytes );
};

/**
 * The stream WebAssembly extensions.
 *
 * @data 2020
 */
partial namespace WebAssembly {
  /**
   * Compile the data as it streams in.
   *
   * @param source the source to compile
   * @return the promise for the compiled module
   * @see http://example.com
   */
  Promise<Module> compileStreaming( Promise<Response> source );
};

/**
 * This is event handler documentation.
 *
 * @param event the event.
 * @version 1.2.3
 */
[LegacyTreatNonObjectAsNull]
callback EventHandler = undefined ( Event event );

/**
 * Documentation for FullscreenNavigationUI.
 *
 * @version 23
 * @see http://example.com/#FullscreenOptions
 */
dictionary FullscreenOptions {
  /**
   * Documentation for navigationUI.
   *
   * @version 23
   * @see http://example.com/#FullscreenOptions.navigationUI
   */
  FullscreenNavigationUI navigationUI = "auto";
};

/**
 * Documentation for WebGLContextAttributes.
 *
 * @version 42
 * @see http://example.com/#WebGLContextAttributes
 */
partial dictionary WebGLContextAttributes {
  /**
   * Documentation for xrCompatible.
   *
   * @version 23
   * @see http://example.com/#WebGLContextAttributes.xrCompatible
   */
  boolean xrCompatible = null;
};

/**
 * This is some basic documentation for DocumentOrShadowRoot
 * element.
 *
 * @see http://example.com/#DocumentOrShadowRoot
 */
interface mixin DocumentOrShadowRoot {
  /**
   * documentation ....
   *
   * @see http://example.com/#pointerLockElement
   */
  [LegacyLenientSetter]
  readonly attribute Element? pointerLockElement;
  /**
   * This is some basic documentation for DocumentOrShadowRoot
   * element.
   *
   * @see http://example.com/#DocumentOrShadowRoot
   */
  event DeviceMotionEvent devicemotion;
};

/**
 * This is from spec blah blah that decorates the mixin
 * by adding full screen.
 *
 * @see http://example.com/#DocumentOrShadowRoot
 * @version 2
 */
partial interface mixin DocumentOrShadowRoot {
  /**
   * Go fullscreen baby!
   * Experience the glory of full screen.
   * Yum!
   *
   * @see http://example.com/#fullscreenElement
   */
  [LegacyLenientSetter]
  readonly attribute Element? fullscreenElement;
  /**
   * This is from spec blah blah that decorates the mixin
   * by adding full screen.
   *
   * @see http://example.com/#DocumentOrShadowRoot
   * @version 2
   */
  event Event focus;
};

/**
 * Documentation for AudioParamMap.
 */
[Exposed=Window]
interface AudioParamMap {
  /**
   * Documentation for maplike.
   */
  readonly maplike<DOMString, AudioParam>;
};

/**
 * Documentation for CommPanel.
 */
interface CommPanel {
  /**
   * Documentation for async iterable.
   *
   * @see http://example.com/#CommPanel.details
   */
  async iterable<USVString, ArrayBuffer>;
};

/**
 * Documentation for Config.
 */
[Exposed=(Window,Worker)]
interface Config {
  /**
   * Documentation for setlike.
   *
   * @see http://example.com/#config
   */
  setlike<USVString>;
};

/**
 * The Document interface.
 *
 * @version 23
 */
[Exposed=Window]
interface Document {
  /**
   * This constant is documented and has an {@link #URL inline tag}.
   *
   * @see http://example.com/#NAMESPACE_RULE
   */
  const unsigned short NAMESPACE_RULE = 10;
  /**
   * The static supportedEntryTypes attribute is documented.
   */
  static readonly attribute FrozenArray<DOMString> supportedEntryTypes;
  /**
   * The readonly URL attribute is documented.
   *
   * @version 23
   */
  readonly attribute USVString URL;
  /**
   * The characterSet attribute is documented.
   */
  attribute DOMString characterSet;
  /**
   * The href attribute is documented.
   */
  stringifier attribute USVString href;
  /**
   * The scroll event fires when the document view or an element has been scrolled.
   *
   * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Document/scroll_event">MDN - scroll</a>
   */
  event Event scroll;
  /**
   * This constructor is documented.
   *
   * @param callback the callback.
   * @return the new instance.
   * @version 23
   */
  constructor( PerformanceObserverCallback callback );
  /**
   * This operation is documented.
   *
   * @return the new range.
   * @version 23
   */
  [NewObject]
  Range createRange();
  /**
   * This operation is documented.
   *
   * @param node this is the node we are importing
   * @param deep should it be a deep import?
   * @version 23
   */
  [CEReactions, NewObject]
  Node importNode( Node node, optional boolean deep = false );
};

/**
 * Documentation for FormData.
 */
[Exposed=(Window,Worker)]
interface FormData {
  /**
   * Documentation for iterable.
   *
   * @see http://example.com/#iterable
   */
  iterable<USVString, FormDataEntryValue>;
};

partial interface Document {
  /**
   * The wheel event fires when the user rotates a wheel button on a pointing device (typically a mouse)..
   *
   * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Document/wheel_event">MDN - wheel</a>
   */
  event WheelEvent wheel;
};

/**
 * Geolocation extensions for navigator.
 */
partial interface Navigator {
  /**
   * Commented attribute in partial interface.
   */
  readonly attribute Geolocation geolocation;
};

/**
 * Because why not.
 *
 * @version 23
 */
Document includes DocumentOrShadowRoot;
