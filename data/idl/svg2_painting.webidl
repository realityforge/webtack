[Exposed=Window]
interface SVGMarkerElement : SVGElement {

  // Marker Unit Types
  const unsigned short SVG_MARKERUNITS_UNKNOWN = 0;
  const unsigned short SVG_MARKERUNITS_USERSPACEONUSE = 1;
  const unsigned short SVG_MARKERUNITS_STROKEWIDTH = 2;

  // Marker Orientation Types
  const unsigned short SVG_MARKER_ORIENT_UNKNOWN = 0;
  const unsigned short SVG_MARKER_ORIENT_AUTO = 1;
  const unsigned short SVG_MARKER_ORIENT_ANGLE = 2;

  [SameObject] readonly attribute SVGAnimatedLength refX;
  [SameObject] readonly attribute SVGAnimatedLength refY;
  [SameObject] readonly attribute SVGAnimatedEnumeration markerUnits;
  [SameObject] readonly attribute SVGAnimatedLength markerWidth;
  [SameObject] readonly attribute SVGAnimatedLength markerHeight;
  [SameObject] readonly attribute SVGAnimatedEnumeration orientType;
  [SameObject] readonly attribute SVGAnimatedAngle orientAngle;
  attribute DOMString orient;

  void setOrientToAuto();
  void setOrientToAngle(SVGAngle angle);
};

SVGMarkerElement includes SVGFitToViewBox;