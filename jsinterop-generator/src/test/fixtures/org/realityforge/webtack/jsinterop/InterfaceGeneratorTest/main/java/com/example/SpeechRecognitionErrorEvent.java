package com.example;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@Generated("org.realityforge.webtack")
@JsType(
    isNative = true,
    namespace = JsPackage.GLOBAL,
    name = "SpeechRecognitionErrorEvent"
)
public final class SpeechRecognitionErrorEvent extends Event {
  public SpeechRecognitionErrorEvent(@Nonnull final String type,
      @Nonnull final SpeechRecognitionErrorEventInit eventInitDict) {
    super( type, eventInitDict );
  }
}