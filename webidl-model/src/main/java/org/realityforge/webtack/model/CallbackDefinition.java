package org.realityforge.webtack.model;

import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;

public final class CallbackDefinition
  extends Definition
{
  @Nonnull
  private final String _name;
  @Nonnull
  private final Type _returnType;
  @Nonnull
  private final List<Argument> _arguments;

  public CallbackDefinition( @Nonnull final String name,
                             @Nonnull final Type returnType,
                             @Nonnull final List<Argument> arguments,
                             @Nonnull final List<ExtendedAttribute> extendedAttributes,
                             @Nonnull final List<SourceInterval> sourceLocations )
  {
    super( extendedAttributes, sourceLocations );
    _name = Objects.requireNonNull( name );
    _returnType = Objects.requireNonNull( returnType );
    _arguments = Objects.requireNonNull( arguments );
  }

  @Nonnull
  public String getName()
  {
    return _name;
  }

  @Nonnull
  public Type getReturnType()
  {
    return _returnType;
  }

  @Nonnull
  public List<Argument> getArguments()
  {
    return _arguments;
  }

  @Override
  public boolean equals( final Object o )
  {
    if ( this == o )
    {
      return true;
    }
    else if ( o == null || getClass() != o.getClass() || !super.equals( o ) )
    {
      return false;
    }
    else
    {
      final CallbackDefinition other = (CallbackDefinition) o;
      return _name.equals( other._name ) &&
             _returnType.equals( other._returnType ) &&
             _arguments.equals( other._arguments );
    }
  }

  @Override
  public int hashCode()
  {
    return Objects.hash( super.hashCode(), _name, _returnType, _arguments );
  }

  public boolean equiv( @Nonnull final CallbackDefinition other )
  {
    return super.equiv( other ) &&
           _name.equals( other._name ) &&
           _returnType.equiv( other._returnType ) &&
           Argument.argumentListEquiv( _arguments, other._arguments );
  }
}
