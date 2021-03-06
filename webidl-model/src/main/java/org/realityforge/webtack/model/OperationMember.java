package org.realityforge.webtack.model;

import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class OperationMember
  extends Element
  implements Member, Comparable<OperationMember>
{
  @Nonnull
  private final Kind _kind;
  @Nullable
  private final String _name;
  @Nonnull
  private final List<Argument> _arguments;
  @Nonnull
  private final Type _returnType;

  public OperationMember( @Nonnull final Kind kind,
                          @Nullable final String name,
                          @Nonnull final List<Argument> arguments,
                          @Nonnull final Type returnType,
                          @Nullable final DocumentationElement documentation,
                          @Nonnull final List<ExtendedAttribute> extendedAttributes,
                          @Nonnull final List<SourceInterval> sourceLocations )
  {
    super( documentation, extendedAttributes, sourceLocations );
    _kind = Objects.requireNonNull( kind );
    _name = name;
    _arguments = Objects.requireNonNull( arguments );
    _returnType = Objects.requireNonNull( returnType );
  }

  @Nonnull
  public Kind getKind()
  {
    return _kind;
  }

  @Nullable
  public String getName()
  {
    return _name;
  }

  @Nonnull
  public List<Argument> getArguments()
  {
    return _arguments;
  }

  @Nonnull
  public Type getReturnType()
  {
    return _returnType;
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
      final OperationMember other = (OperationMember) o;
      return _kind == other._kind &&
             Objects.equals( _name, other._name ) &&
             _arguments.equals( other._arguments ) &&
             _returnType.equals( other._returnType );
    }
  }

  @Override
  public int hashCode()
  {
    return Objects.hash( super.hashCode(), _kind, _name, _arguments, _returnType );
  }

  public boolean equiv( @Nonnull final OperationMember other )
  {
    return super.equiv( other ) &&
           _kind == other._kind &&
           Objects.equals( _name, other._name ) &&
           Argument.argumentListEquiv( _arguments, other._arguments ) &&
           _returnType.equiv( other._returnType );
  }

  @Override
  public int compareTo( @Nonnull final OperationMember other )
  {
    String name = _name;
    if ( null == name )
    {
      name = "";
    }
    String otherName = other.getName();
    if ( null == otherName )
    {
      otherName = "";
    }

    int result = name.compareTo( otherName );
    if ( 0 != result )
    {
      return result;
    }
    result = _arguments.size() - other.getArguments().size();
    if ( 0 != result )
    {
      return result;
    }
    return hashCode() - other.hashCode();
  }

  public enum Kind
  {
    STATIC,
    CONSTRUCTOR,
    DEFAULT,
    GETTER,
    SETTER,
    DELETER,
    STRINGIFIER
  }
}
