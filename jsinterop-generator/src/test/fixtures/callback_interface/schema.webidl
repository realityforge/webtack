callback interface EventListener {
  undefined handleEvent( Event event );
};

/**
 * This tests that lowercase name converted to uppercase when converted into java.
 */
callback interface txCallback {
  undefined handleEvent( Event event );
};

[Exposed=(Window,Worker,AudioWorklet)]
interface Event {
  constructor();
};
