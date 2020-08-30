/**
 * This is event handler documentation.
 *
 * @param event the event.
 * @version 1.2.3
 */
[LegacyTreatNonObjectAsNull]
callback EventHandler = any ( Event event );

/**
 * This interface defines events which subtypes define event handlers for.
 */
interface Element {
  attribute NullableEventHandler oncancel;
  /**
   * blur has no handler property and thus no handler type but should have a listener as we will
   * create a helper method to subscribe.
   */
  event FocusEvent blur;
  /**
   * cancel is of type event so no new handler or listener will be created.
   */
  [NoCancel]
  event Event cancel;
  /**
   * click event handlers are present in subtypes so will have a listener and subtypes handler properties will be updated.
   */
  [NoBubble, NoCancel]
  event MouseEvent click;
};

interface Event {
};

interface EventSource {
  attribute NullableEventHandler onerror;
};

interface FocusEvent : Event {
};

interface HTMLElement : Element {
  attribute NullableEventHandler onclick;
};

interface MouseEvent : Event {
};

interface SVGElement : Element {
  attribute NullableEventHandler onclick;
};

interface SpeechSynthesisErrorEvent : SpeechSynthesisEvent {
};

interface SpeechSynthesisEvent : Event {
};

interface SpeechSynthesisUtterance {
  attribute NullableEventHandler onend;
  attribute NullableEventHandler onstart;
  event SpeechSynthesisEvent end;
  event SpeechSynthesisEvent start;
};

partial interface SpeechSynthesisUtterance {
  attribute NullableEventHandler onpause;
  attribute NullableEventHandler onresume;
  event SpeechSynthesisEvent resume;
};
