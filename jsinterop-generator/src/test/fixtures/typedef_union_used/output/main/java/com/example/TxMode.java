package com.example;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.intellij.lang.annotations.MagicConstant;

@Generated("org.realityforge.webtack")
@MagicConstant(
    valuesFromClass = TxMode.class
)
public @interface TxMode {
  @Nonnull
  String not_allowed = "not-allowed";

  @Nonnull
  String requires = "requires";

  @Nonnull
  String requires_new = "requires_new";
}
