package org.realityforge.webtack.model;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnull;

public final class CallbackInterfaceDefinition
  extends Definition
{
  @Nonnull
  private final String _name;
  @Nonnull
  private final OperationMember _operation;
  @Nonnull
  private final List<ConstMember> _constants;

  public CallbackInterfaceDefinition( @Nonnull final String name,
                                      @Nonnull final OperationMember operation,
                                      @Nonnull final List<ConstMember> constants,
                                      @Nonnull final List<ExtendedAttribute> extendedAttributes,
                                      @Nonnull final List<SourceInterval> sourceLocations )
  {
    super( extendedAttributes, sourceLocations );
    _name = Objects.requireNonNull( name );
    _operation = Objects.requireNonNull( operation );
    _constants = Objects.requireNonNull( constants );
  }

  @Nonnull
  public String getName()
  {
    return _name;
  }

  @Nonnull
  public OperationMember getOperation()
  {
    return _operation;
  }

  @Nonnull
  public List<ConstMember> getConstants()
  {
    return _constants;
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
      final CallbackInterfaceDefinition other = (CallbackInterfaceDefinition) o;
      return _name.equals( other._name ) &&
             _operation.equals( other._operation ) &&
             _constants.equals( other._constants );
    }
  }

  @Override
  public int hashCode()
  {
    return Objects.hash( super.hashCode(), _name, _operation, _constants );
  }

  public boolean equiv( @Nonnull final CallbackInterfaceDefinition other )
  {
    if ( super.equiv( other ) &&
         _name.equals( other._name ) &&
         _constants.size() == other._constants.size() &&
         _operation.equiv( other._operation ) )
    {
      final Set<ConstMember> otherConstants = new HashSet<>( other._constants );
      for ( final ConstMember member : _constants )
      {
        if ( !otherConstants.remove( member ) )
        {
          return false;
        }
      }
      return true;
    }
    else
    {
      return false;
    }
  }
}
