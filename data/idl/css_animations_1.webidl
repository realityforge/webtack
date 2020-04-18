[Exposed=Window,
 Constructor(CSSOMString type, optional AnimationEventInit animationEventInitDict)]
interface AnimationEvent : Event {
  readonly attribute CSSOMString animationName;
  readonly attribute double elapsedTime;
  readonly attribute CSSOMString pseudoElement;
};
dictionary AnimationEventInit : EventInit {
  CSSOMString animationName = "";
  double elapsedTime = 0.0;
  CSSOMString pseudoElement = "";
};

partial interface CSSRule {
    const unsigned short KEYFRAMES_RULE = 7;
    const unsigned short KEYFRAME_RULE = 8;
};

[Exposed=Window]
interface CSSKeyframeRule : CSSRule {
  attribute CSSOMString keyText;
  [SameObject, PutForwards=cssText] readonly attribute CSSStyleDeclaration style;
};

[Exposed=Window]
interface CSSKeyframesRule : CSSRule {
           attribute CSSOMString name;
  readonly attribute CSSRuleList cssRules;

  void             appendRule(CSSOMString rule);
  void             deleteRule(CSSOMString select);
  CSSKeyframeRule? findRule(CSSOMString select);
};

partial interface mixin GlobalEventHandlers {
  attribute EventHandler onanimationstart;
  attribute EventHandler onanimationiteration;
  attribute EventHandler onanimationend;
  attribute EventHandler onanimationcancel;
};