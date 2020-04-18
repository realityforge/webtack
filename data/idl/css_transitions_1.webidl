[Exposed=Window,
 Constructor(CSSOMString type, optional TransitionEventInit transitionEventInitDict)]
interface TransitionEvent : Event {
  readonly attribute CSSOMString propertyName;
  readonly attribute double elapsedTime;
  readonly attribute CSSOMString pseudoElement;
};

dictionary TransitionEventInit : EventInit {
  CSSOMString propertyName = "";
  double elapsedTime = 0.0;
  CSSOMString pseudoElement = "";
};

partial interface GlobalEventHandlers {
  attribute EventHandler ontransitionrun;
  attribute EventHandler ontransitionstart;
  attribute EventHandler ontransitionend;
  attribute EventHandler ontransitioncancel;
};