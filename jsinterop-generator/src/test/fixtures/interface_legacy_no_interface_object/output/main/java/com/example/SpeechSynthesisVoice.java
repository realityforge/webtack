package com.example;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

@Generated("org.realityforge.webtack")
@JsType(
    isNative = true,
    namespace = JsPackage.GLOBAL,
    name = "Object"
)
public class SpeechSynthesisVoice {
  protected SpeechSynthesisVoice() {
  }

  @JsProperty(
      name = "default"
  )
  public native boolean default_();

  @JsProperty(
      name = "lang"
  )
  @Nonnull
  public native String lang();

  @JsProperty(
      name = "localService"
  )
  public native boolean localService();

  @JsProperty(
      name = "name"
  )
  @Nonnull
  public native String name();

  @JsProperty(
      name = "voiceURI"
  )
  @Nonnull
  public native String voiceURI();
}
