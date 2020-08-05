/**
 * This is event handler documentation.
 *
 * @param event the event.
 * @version 1.2.3
 */
[LegacyTreatNonObjectAsNull]
callback EventHandler = void ( Event event );

/**
 * Handle events of type SpeechSynthesisErrorEvent
 *
 * @param event the event
 */
callback SpeechSynthesisErrorEventHandler = void ( SpeechSynthesisErrorEvent event );

/**
 * Handle events of type SpeechSynthesisEvent
 *
 * @param event the event
 */
callback SpeechSynthesisEventHandler = void ( SpeechSynthesisEvent event );

interface Event {
};

interface SpeechSynthesisErrorEvent : SpeechSynthesisEvent {
};

interface SpeechSynthesisEvent : Event {
};
