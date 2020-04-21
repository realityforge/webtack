package org.realityforge.webtack.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

public final class WebIDLSchema
  extends Node
{
  @Nonnull
  private final Map<String, CallbackDefinition> _callbacks;
  @Nonnull
  private final Map<String, CallbackInterfaceDefinition> _callbackInterfaces;
  @Nonnull
  private final Map<String, DictionaryDefinition> _dictionaries;
  @Nonnull
  private final Map<String, EnumerationDefinition> _enumerations;
  @Nonnull
  private final Map<String, InterfaceDefinition> _interfaces;
  @Nonnull
  private final Map<String, MixinDefinition> _mixins;
  @Nonnull
  private final Map<String, IncludesStatement> _includes;
  @Nonnull
  private final Map<String, NamespaceDefinition> _namespaces;
  @Nonnull
  private final Map<String, List<PartialDictionaryDefinition>> _partialDictionaries;
  @Nonnull
  private final Map<String, List<PartialInterfaceDefinition>> _partialInterfaces;
  @Nonnull
  private final Map<String, List<PartialMixinDefinition>> _partialMixins;
  @Nonnull
  private final Map<String, List<PartialNamespaceDefinition>> _partialNamespaces;
  @Nonnull
  private final Map<String, TypedefDefinition> _typedefs;

  public WebIDLSchema( @Nonnull final Map<String, CallbackDefinition> callbacks,
                       @Nonnull final Map<String, CallbackInterfaceDefinition> callbackInterfaces,
                       @Nonnull final Map<String, DictionaryDefinition> dictionaries,
                       @Nonnull final Map<String, EnumerationDefinition> enumerations,
                       @Nonnull final Map<String, InterfaceDefinition> interfaces,
                       @Nonnull final Map<String, MixinDefinition> mixins,
                       @Nonnull final Map<String, IncludesStatement> includes,
                       @Nonnull final Map<String, NamespaceDefinition> namespaces,
                       @Nonnull final Map<String, List<PartialDictionaryDefinition>> partialDictionaries,
                       @Nonnull final Map<String, List<PartialInterfaceDefinition>> partialInterfaces,
                       @Nonnull final Map<String, List<PartialMixinDefinition>> partialMixins,
                       @Nonnull final Map<String, List<PartialNamespaceDefinition>> partialNamespaces,
                       @Nonnull final Map<String, TypedefDefinition> typedefs,
                       @Nonnull final List<SourceInterval> sourceLocations )
  {
    super( sourceLocations );
    _callbacks = Objects.requireNonNull( callbacks );
    _callbackInterfaces = Objects.requireNonNull( callbackInterfaces );
    _dictionaries = Objects.requireNonNull( dictionaries );
    _enumerations = Objects.requireNonNull( enumerations );
    _interfaces = Objects.requireNonNull( interfaces );
    _mixins = Objects.requireNonNull( mixins );
    _includes = Objects.requireNonNull( includes );
    _namespaces = Objects.requireNonNull( namespaces );
    _partialDictionaries = Objects.requireNonNull( partialDictionaries );
    _partialInterfaces = Objects.requireNonNull( partialInterfaces );
    _partialMixins = Objects.requireNonNull( partialMixins );
    _partialNamespaces = Objects.requireNonNull( partialNamespaces );
    _typedefs = Objects.requireNonNull( typedefs );
  }

  @Nonnull
  public Collection<CallbackDefinition> getCallbacks()
  {
    return _callbacks.values();
  }

  @Nonnull
  public Collection<CallbackInterfaceDefinition> getCallbackInterfaces()
  {
    return _callbackInterfaces.values();
  }

  @Nonnull
  public Collection<DictionaryDefinition> getDictionaries()
  {
    return _dictionaries.values();
  }

  @Nonnull
  public Collection<EnumerationDefinition> getEnumerations()
  {
    return _enumerations.values();
  }

  @Nonnull
  public Collection<InterfaceDefinition> getInterfaces()
  {
    return _interfaces.values();
  }

  @Nonnull
  public Collection<MixinDefinition> getMixins()
  {
    return _mixins.values();
  }

  @Nonnull
  public Collection<IncludesStatement> getIncludes()
  {
    return _includes.values();
  }

  @Nonnull
  public Collection<NamespaceDefinition> getNamespaces()
  {
    return _namespaces.values();
  }

  @Nonnull
  public Collection<PartialDictionaryDefinition> getPartialDictionaries()
  {
    return _partialDictionaries.values().stream().flatMap( Collection::stream ).collect( Collectors.toList() );
  }

  @Nonnull
  public Collection<PartialInterfaceDefinition> getPartialInterfaces()
  {
    return _partialInterfaces.values().stream().flatMap( Collection::stream ).collect( Collectors.toList() );
  }

  @Nonnull
  public Collection<PartialMixinDefinition> getPartialMixins()
  {
    return _partialMixins.values().stream().flatMap( Collection::stream ).collect( Collectors.toList() );
  }

  @Nonnull
  public Collection<PartialNamespaceDefinition> getPartialNamespaces()
  {
    return _partialNamespaces.values().stream().flatMap( Collection::stream ).collect( Collectors.toList() );
  }

  @Nonnull
  public Collection<TypedefDefinition> getTypedefs()
  {
    return _typedefs.values();
  }

  @Override
  public boolean equals( final Object o )
  {
    if ( this == o )
    {
      return true;
    }
    else if ( o == null || getClass() != o.getClass() )
    {
      return false;
    }
    else
    {
      final WebIDLSchema other = (WebIDLSchema) o;
      return _callbacks.equals( other._callbacks ) &&
             _callbackInterfaces.equals( other._callbackInterfaces ) &&
             _dictionaries.equals( other._dictionaries ) &&
             _enumerations.equals( other._enumerations ) &&
             _interfaces.equals( other._interfaces ) &&
             _mixins.equals( other._mixins ) &&
             _includes.equals( other._includes ) &&
             _namespaces.equals( other._namespaces ) &&
             _partialDictionaries.equals( other._partialDictionaries ) &&
             _partialInterfaces.equals( other._partialInterfaces ) &&
             _partialMixins.equals( other._partialMixins ) &&
             _partialNamespaces.equals( other._partialNamespaces ) &&
             _typedefs.equals( other._typedefs );
    }
  }

  @Override
  public int hashCode()
  {
    return Objects.hash( _callbacks,
                         _callbackInterfaces,
                         _dictionaries,
                         _enumerations,
                         _interfaces,
                         _mixins,
                         _includes,
                         _namespaces,
                         _partialDictionaries,
                         _partialInterfaces,
                         _partialMixins,
                         _partialNamespaces,
                         _typedefs );
  }

  public boolean equiv( @Nonnull final WebIDLSchema other )
  {
    if ( _callbacks.size() == other._callbacks.size() &&
         _callbackInterfaces.size() == other._callbackInterfaces.size() &&
         _dictionaries.size() == other._dictionaries.size() &&
         _enumerations.size() == other._enumerations.size() &&
         _interfaces.size() == other._interfaces.size() &&
         _mixins.size() == other._mixins.size() &&
         _includes.size() == other._includes.size() &&
         _namespaces.size() == other._namespaces.size() &&
         _partialDictionaries.size() == other._partialDictionaries.size() &&
         _partialInterfaces.size() == other._partialInterfaces.size() &&
         _partialMixins.size() == other._partialMixins.size() &&
         _partialNamespaces.size() == other._partialNamespaces.size() &&
         _typedefs.size() == other._typedefs.size()
    )
    {
      final Set<CallbackDefinition> otherCallbacks = new HashSet<>( _callbacks.values() );
      for ( final CallbackDefinition definition : _callbacks.values() )
      {
        if ( !otherCallbacks.remove( definition ) )
        {
          return false;
        }
      }
      final Set<CallbackInterfaceDefinition> otherCallbackInterfaces = new HashSet<>( _callbackInterfaces.values() );
      for ( final CallbackInterfaceDefinition definition : _callbackInterfaces.values() )
      {
        if ( !otherCallbackInterfaces.remove( definition ) )
        {
          return false;
        }
      }
      final Set<DictionaryDefinition> otherDictionaries = new HashSet<>( _dictionaries.values() );
      for ( final DictionaryDefinition definition : _dictionaries.values() )
      {
        if ( !otherDictionaries.remove( definition ) )
        {
          return false;
        }
      }
      final Set<EnumerationDefinition> otherEnumerations = new HashSet<>( _enumerations.values() );
      for ( final EnumerationDefinition definition : _enumerations.values() )
      {
        if ( !otherEnumerations.remove( definition ) )
        {
          return false;
        }
      }
      final Set<InterfaceDefinition> otherInterfaces = new HashSet<>( _interfaces.values() );
      for ( final InterfaceDefinition definition : _interfaces.values() )
      {
        if ( !otherInterfaces.remove( definition ) )
        {
          return false;
        }
      }
      final Set<MixinDefinition> otherMixins = new HashSet<>( _mixins.values() );
      for ( final MixinDefinition definition : _mixins.values() )
      {
        if ( !otherMixins.remove( definition ) )
        {
          return false;
        }
      }
      final Set<IncludesStatement> otherIncludes = new HashSet<>( _includes.values() );
      for ( final IncludesStatement definition : _includes.values() )
      {
        if ( !otherIncludes.remove( definition ) )
        {
          return false;
        }
      }
      final Set<NamespaceDefinition> otherNamespaces = new HashSet<>( _namespaces.values() );
      for ( final NamespaceDefinition definition : _namespaces.values() )
      {
        if ( !otherNamespaces.remove( definition ) )
        {
          return false;
        }
      }
      final Set<TypedefDefinition> otherTypedefs = new HashSet<>( _typedefs.values() );
      for ( final TypedefDefinition definition : _typedefs.values() )
      {
        if ( !otherTypedefs.remove( definition ) )
        {
          return false;
        }
      }
      final Set<PartialDictionaryDefinition> otherPartialDictionaries =
        other._partialDictionaries.values().stream().flatMap( Collection::stream ).collect( Collectors.toSet() );
      final List<PartialDictionaryDefinition> partialDictionaries =
        _partialDictionaries.values().stream().flatMap( Collection::stream ).collect( Collectors.toList() );
      for ( final PartialDictionaryDefinition definition : partialDictionaries )
      {
        if ( !otherPartialDictionaries.remove( definition ) )
        {
          return false;
        }
      }
      final Set<PartialInterfaceDefinition> otherPartialInterfaces =
        other._partialInterfaces.values().stream().flatMap( Collection::stream ).collect( Collectors.toSet() );
      final List<PartialInterfaceDefinition> partialInterfaces =
        _partialInterfaces.values().stream().flatMap( Collection::stream ).collect( Collectors.toList() );
      for ( final PartialInterfaceDefinition definition : partialInterfaces )
      {
        if ( !otherPartialInterfaces.remove( definition ) )
        {
          return false;
        }
      }
      final Set<PartialMixinDefinition> otherPartialMixins =
        other._partialMixins.values().stream().flatMap( Collection::stream ).collect( Collectors.toSet() );
      final List<PartialMixinDefinition> partialMixins =
        _partialMixins.values().stream().flatMap( Collection::stream ).collect( Collectors.toList() );
      for ( final PartialMixinDefinition definition : partialMixins )
      {
        if ( !otherPartialMixins.remove( definition ) )
        {
          return false;
        }
      }
      final Set<PartialNamespaceDefinition> otherPartialNamespaces =
        other._partialNamespaces.values().stream().flatMap( Collection::stream ).collect( Collectors.toSet() );
      final List<PartialNamespaceDefinition> partialNamespaces =
        _partialNamespaces.values().stream().flatMap( Collection::stream ).collect( Collectors.toList() );
      for ( final PartialNamespaceDefinition definition : partialNamespaces )
      {
        if ( !otherPartialNamespaces.remove( definition ) )
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
