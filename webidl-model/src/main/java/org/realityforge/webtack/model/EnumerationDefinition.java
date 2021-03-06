package org.realityforge.webtack.model;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class EnumerationDefinition
  extends NamedDefinition
{
  @Nonnull
  private final List<EnumerationValue> _values;

  public EnumerationDefinition( @Nonnull final String name,
                                @Nonnull final List<EnumerationValue> values,
                                @Nullable final DocumentationElement documentation,
                                @Nonnull final List<ExtendedAttribute> extendedAttributes,
                                @Nonnull final List<SourceInterval> sourceLocations )
  {
    super( name, documentation, extendedAttributes, sourceLocations );
    _values = Objects.requireNonNull( values
                                        .stream()
                                        .sorted( Comparator.comparing( EnumerationValue::getValue ) )
                                        .collect( Collectors.toList() ) );
  }

  @Nonnull
  public List<EnumerationValue> getValues()
  {
    return _values;
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
      final EnumerationDefinition other = (EnumerationDefinition) o;
      return getName().equals( other.getName() ) && _values.equals( other._values );
    }
  }

  @Override
  public int hashCode()
  {
    return Objects.hash( super.hashCode(), _values );
  }

  public boolean equiv( @Nonnull final EnumerationDefinition other )
  {
    if ( super.equiv( other ) && _values.size() == other._values.size() )
    {
      int index = 0;
      for ( final EnumerationValue value : _values )
      {
        if ( !value.equiv( other.getValues().get( index++ ) ) )
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
