package org.realityforge.webtack.model;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class PartialDictionaryDefinition
  extends NamedDefinition
{
  @Nonnull
  private final List<DictionaryMember> _members;

  public PartialDictionaryDefinition( @Nonnull final String name,
                                      @Nonnull final List<DictionaryMember> members,
                                      @Nullable final DocumentationElement documentation,
                                      @Nonnull final List<ExtendedAttribute> extendedAttributes,
                                      @Nonnull final List<SourceInterval> sourceLocations )
  {
    super( name, documentation, extendedAttributes, sourceLocations );
    _members = Objects.requireNonNull( members );
  }

  @Nonnull
  public List<DictionaryMember> getMembers()
  {
    return _members;
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
      final PartialDictionaryDefinition other = (PartialDictionaryDefinition) o;
      return getName().equals( other.getName() ) &&
             _members.equals( other._members );
    }
  }

  @Override
  public int hashCode()
  {
    return Objects.hash( super.hashCode(), _members );
  }

  public boolean equiv( @Nonnull final PartialDictionaryDefinition other )
  {
    if ( super.equiv( other ) &&
         getName().equals( other.getName() ) &&
         _members.size() == other._members.size() )
    {
      final Set<DictionaryMember> otherMembers = new HashSet<>( other._members );
      for ( final DictionaryMember member : _members )
      {
        if ( !otherMembers.removeIf( member::equiv ) )
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
