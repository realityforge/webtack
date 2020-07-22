package org.realityforge.webtack.jsinterop;

import com.squareup.javapoet.TypeName;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.realityforge.webtack.model.Type;

final class TypedValue
{
  enum Nullability
  {
    NULLABLE, NONNULL, NA
  }

  // The type as it is declared in schema
  @Nonnull
  private final Type _declaredType;
  // The underlying type. Typedefs have been resolved and may be an alternate in the union
  @Nonnull
  private final Type _type;
  // The java type that will represent this value
  @Nonnull
  private final TypeName _javaType;
  // An enum indicating whether the Nullability annotation that should appear in java if any
  @Nonnull
  private final Nullability _nullability;

  TypedValue( @Nonnull final Type declaredType,
              @Nonnull final Type type,
              @Nonnull final TypeName javaType,
              @Nonnull final Nullability nullability )
  {
    _declaredType = Objects.requireNonNull( declaredType );
    _type = Objects.requireNonNull( type );
    _javaType = Objects.requireNonNull( javaType );
    _nullability = Objects.requireNonNull( nullability );
  }

  @Nonnull
  Type getDeclaredType()
  {
    return _declaredType;
  }

  @Nonnull
  Type getType()
  {
    return _type;
  }

  @Nonnull
  TypeName getJavaType()
  {
    return _javaType;
  }

  @Nonnull
  Nullability getNullability()
  {
    return _nullability;
  }
}
