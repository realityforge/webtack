[Exposed=(Window,Worker)]
interface PerformanceResourceTiming : PerformanceEntry {
    readonly        attribute DOMString           initiatorType;
    readonly        attribute DOMString           nextHopProtocol;
    readonly        attribute DOMHighResTimeStamp workerStart;
    readonly        attribute DOMHighResTimeStamp redirectStart;
    readonly        attribute DOMHighResTimeStamp redirectEnd;
    readonly        attribute DOMHighResTimeStamp fetchStart;
    readonly        attribute DOMHighResTimeStamp domainLookupStart;
    readonly        attribute DOMHighResTimeStamp domainLookupEnd;
    readonly        attribute DOMHighResTimeStamp connectStart;
    readonly        attribute DOMHighResTimeStamp connectEnd;
    readonly        attribute DOMHighResTimeStamp secureConnectionStart;
    readonly        attribute DOMHighResTimeStamp requestStart;
    readonly        attribute DOMHighResTimeStamp responseStart;
    readonly        attribute DOMHighResTimeStamp responseEnd;
    readonly        attribute unsigned long long  transferSize;
    readonly        attribute unsigned long long  encodedBodySize;
    readonly        attribute unsigned long long  decodedBodySize;
    [Default] object toJSON();
};

partial interface Performance {
  void clearResourceTimings ();
  void setResourceTimingBufferSize (unsigned long maxSize);
              attribute EventHandler onresourcetimingbufferfull;
};