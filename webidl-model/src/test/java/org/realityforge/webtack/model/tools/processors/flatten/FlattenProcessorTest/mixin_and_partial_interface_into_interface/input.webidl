interface mixin DocumentOrShadowRoot {
};

partial interface mixin DocumentOrShadowRoot {
  [LegacyLenientSetter]
  readonly attribute Element? fullscreenElement;
};

partial interface mixin DocumentOrShadowRoot {
  readonly attribute Element? activeElement;
};

partial interface mixin DocumentOrShadowRoot {
  [SameObject]
  readonly attribute StyleSheetList styleSheets;
};

[Exposed=Window]
interface Document : Node {
  readonly attribute USVString URL;
  readonly attribute DOMString characterSet;
  [NewObject]
  Range createRange();
  [NewObject]
  HTMLCollection getElementsByTagNameNS( DOMString? namespace, DOMString localName );
  [CEReactions, NewObject]
  Node importNode( Node node, optional boolean deep = false );
};

partial interface Document {
  readonly attribute DocumentTimeline timeline;
  sequence<Animation> getAnimations();
};

partial interface Document {
  readonly attribute SVGSVGElement? rootElement;
};

Document includes DocumentOrShadowRoot;
