package org.realityforge.webtack.model.tools.processors.merge_docs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.webtack.model.AttributeMember;
import org.realityforge.webtack.model.CallbackInterfaceDefinition;
import org.realityforge.webtack.model.ConstMember;
import org.realityforge.webtack.model.DictionaryDefinition;
import org.realityforge.webtack.model.DictionaryMember;
import org.realityforge.webtack.model.DocumentationBlockTag;
import org.realityforge.webtack.model.DocumentationElement;
import org.realityforge.webtack.model.EventMember;
import org.realityforge.webtack.model.ExtendedAttribute;
import org.realityforge.webtack.model.InterfaceDefinition;
import org.realityforge.webtack.model.MixinDefinition;
import org.realityforge.webtack.model.NamespaceDefinition;
import org.realityforge.webtack.model.OperationMember;
import org.realityforge.webtack.model.PartialDictionaryDefinition;
import org.realityforge.webtack.model.PartialInterfaceDefinition;
import org.realityforge.webtack.model.PartialMixinDefinition;
import org.realityforge.webtack.model.PartialNamespaceDefinition;
import org.realityforge.webtack.model.TypeReference;
import org.realityforge.webtack.model.WebIDLSchema;
import org.realityforge.webtack.model.tools.mdn_scanner.DocEntry;
import org.realityforge.webtack.model.tools.mdn_scanner.DocKind;
import org.realityforge.webtack.model.tools.mdn_scanner.DocRepositoryRuntime;
import org.realityforge.webtack.model.tools.mdn_scanner.config2.DocIndex;
import org.realityforge.webtack.model.tools.mdn_scanner.config2.EntryIndex;
import org.realityforge.webtack.model.tools.processors.AbstractProcessor;

/**
 * Lookup DocRepositoryRuntime for documentation for elements and add javadocs if documentation is present.
 */
final class MergeDocsProcessor
  extends AbstractProcessor
{
  @Nonnull
  private final DocRepositoryRuntime _runtime;
  private final boolean _createEvents;
  private String _type;
  private boolean _typeIsMixin;
  private WebIDLSchema _schema;

  MergeDocsProcessor( @Nonnull final DocRepositoryRuntime runtime, final boolean createEvents )
  {
    _runtime = Objects.requireNonNull( runtime );
    _createEvents = createEvents;
  }

  @Nullable
  @Override
  public WebIDLSchema process( @Nonnull final WebIDLSchema schema )
  {
    try
    {
      _schema = schema;
      return super.process( schema );
    }
    finally
    {
      _schema = null;
    }
  }

  @Nonnull
  @Override
  protected CallbackInterfaceDefinition transformCallbackInterface( @Nonnull final CallbackInterfaceDefinition input )
  {
    _type = input.getName();
    _typeIsMixin = false;
    final DocEntry docEntry = _runtime.findDocEntry( _type, null );
    final CallbackInterfaceDefinition definition =
      new CallbackInterfaceDefinition( input.getName(),
                                       transformOperationMember( input.getOperation() ),
                                       transformConstants( input.getConstants() ),
                                       null == docEntry ?
                                       transformDocumentation( input.getDocumentation() ) :
                                       createDocumentationElement( docEntry ),
                                       transformExtendedAttributes( input.getExtendedAttributes() ),
                                       transformSourceLocations( input.getSourceLocations() ) );
    _type = null;
    return definition;
  }

  @Nonnull
  @Override
  protected InterfaceDefinition transformInterface( @Nonnull final InterfaceDefinition input )
  {
    final String namespace = input.getNamespace();
    _type = ( null == namespace ? "" : namespace + "." ) + input.getName();
    _typeIsMixin = false;
    final DocEntry docEntry = _runtime.findDocEntry( _type, null );
    final InterfaceDefinition definition =
      new InterfaceDefinition( input.getName(),
                               input.getInherits(),
                               transformConstants( input.getConstants() ),
                               transformAttributeMembers( input.getAttributes() ),
                               transformOperationMembers( input.getOperations() ),
                               transformEventMembers( input.getEvents() ),
                               transformIterableMember( input.getIterable() ),
                               transformAsyncIterableMember( input.getAsyncIterable() ),
                               transformMapLikeMember( input.getMapLikeMember() ),
                               transformSetLikeMember( input.getSetLikeMember() ),
                               null == docEntry ?
                               transformDocumentation( input.getDocumentation() ) :
                               createDocumentationElement( docEntry ),
                               transformExtendedAttributes( input.getExtendedAttributes() ),
                               transformSourceLocations( input.getSourceLocations() ) );
    _type = null;
    return definition;
  }

  @Nonnull
  @Override
  protected List<EventMember> transformEventMembers( @Nonnull final List<EventMember> inputs )
  {
    final List<EventMember> events = super.transformEventMembers( inputs );
    if ( _createEvents )
    {
      final DocIndex index = _runtime.findIndexForType( _type );
      final List<EntryIndex> entries =
        null != index ?
        index.getEntries().stream().filter( e -> e.getName().endsWith( "_event" ) ).collect( Collectors.toList() ) :
        null;
      if ( null != entries )
      {
        for ( final EntryIndex eventIndex : entries )
        {
          final DocEntry eventDocEntry = _runtime.getDocEntry( eventIndex );
          final String eventName = eventDocEntry.getEventName();
          assert null != eventName;
          if ( events.stream().noneMatch( e -> e.getName().equals( eventName ) ) && !partialContainsEvent( eventName ) )
          {
            final String eventType = eventDocEntry.getEventType();
            assert null != eventType;
            // Some events are a union and we don't yet represent this scenario so this will fail
            // It will also skip adding events that are documented on MDN but are deprecated and no longer part of the spec
            if ( null != _schema.findInterfaceByName( eventType ) )
            {
              final List<ExtendedAttribute> attributes = new ArrayList<>();
              if ( Boolean.FALSE == eventDocEntry.getEventBubbles() )
              {
                attributes.add( ExtendedAttribute.createExtendedAttributeNoArgs( "NoBubble",
                                                                                 Collections.emptyList() ) );
              }
              if ( Boolean.FALSE == eventDocEntry.getEventCancelable() )
              {
                attributes.add( ExtendedAttribute.createExtendedAttributeNoArgs( "NoCancel",
                                                                                 Collections.emptyList() ) );
              }
              events.add( new EventMember( eventName,
                                           new TypeReference( eventType,
                                                              Collections.emptyList(),
                                                              false,
                                                              Collections.emptyList() ),
                                           createDocumentationElement( eventDocEntry ),
                                           attributes,
                                           Collections.emptyList() ) );
            }
          }
        }
      }
    }
    return events;
  }

  private boolean partialContainsEvent( @Nonnull final String eventName )
  {
    return _typeIsMixin ? anyPartialMixinContainEvent( eventName ) : anyPartialInterfaceContainEvent( eventName );
  }

  private boolean anyPartialMixinContainEvent( @Nonnull final String eventName )
  {
    for ( final PartialMixinDefinition definition : _schema.findPartialMixinsByName( _type ) )
    {
      if ( definition.getEvents().stream().anyMatch( e -> e.getName().equals( eventName ) ) )
      {
        return true;
      }
    }
    return false;
  }

  private boolean anyPartialInterfaceContainEvent( @Nonnull final String eventName )
  {
    for ( final PartialInterfaceDefinition definition : _schema.findPartialInterfacesByName( _type ) )
    {
      if ( definition.getEvents().stream().anyMatch( e -> e.getName().equals( eventName ) ) )
      {
        return true;
      }
    }
    return false;
  }

  @Nonnull
  @Override
  protected PartialInterfaceDefinition transformPartialInterface( @Nonnull final PartialInterfaceDefinition input )
  {
    _type = input.getName();
    _typeIsMixin = false;
    final DocEntry docEntry = _runtime.findDocEntry( _type, null );
    final PartialInterfaceDefinition definition =
      new PartialInterfaceDefinition( input.getName(),
                                      transformConstants( input.getConstants() ),
                                      transformAttributeMembers( input.getAttributes() ),
                                      transformOperationMembers( input.getOperations() ),
                                      // Deliberately call super here so that only interfaces
                                      // pick up events otherwise multiple partial interfaces
                                      // or a partial and an actual interface would both receive
                                      // events that are part of the docs which would cause errors
                                      super.transformEventMembers( input.getEvents() ),
                                      transformIterableMember( input.getIterable() ),
                                      transformAsyncIterableMember( input.getAsyncIterable() ),
                                      transformMapLikeMember( input.getMapLikeMember() ),
                                      transformSetLikeMember( input.getSetLikeMember() ),
                                      null == docEntry ?
                                      transformDocumentation( input.getDocumentation() ) :
                                      createDocumentationElement( docEntry ),
                                      transformExtendedAttributes( input.getExtendedAttributes() ),
                                      transformSourceLocations( input.getSourceLocations() ) );
    _type = null;
    return definition;
  }

  @Nonnull
  @Override
  protected MixinDefinition transformMixin( @Nonnull final MixinDefinition input )
  {
    _type = input.getName();
    _typeIsMixin = true;
    final DocEntry docEntry = _runtime.findDocEntry( _type, null );
    final MixinDefinition definition =
      new MixinDefinition( input.getName(),
                           transformConstants( input.getConstants() ),
                           transformAttributeMembers( input.getAttributes() ),
                           transformOperationMembers( input.getOperations() ),
                           transformEventMembers( input.getEvents() ),
                           null == docEntry ?
                           transformDocumentation( input.getDocumentation() ) :
                           createDocumentationElement( docEntry ),
                           transformExtendedAttributes( input.getExtendedAttributes() ),
                           transformSourceLocations( input.getSourceLocations() ) );
    _type = null;
    return definition;
  }

  @Nonnull
  @Override
  protected PartialMixinDefinition transformPartialMixin( @Nonnull final PartialMixinDefinition input )
  {
    _type = input.getName();
    _typeIsMixin = true;
    final DocEntry docEntry = _runtime.findDocEntry( _type, null );
    final PartialMixinDefinition definition =
      new PartialMixinDefinition( input.getName(),
                                  transformConstants( input.getConstants() ),
                                  transformAttributeMembers( input.getAttributes() ),
                                  transformOperationMembers( input.getOperations() ),
                                  transformEventMembers( input.getEvents() ),
                                  null == docEntry ?
                                  transformDocumentation( input.getDocumentation() ) :
                                  createDocumentationElement( docEntry ),
                                  transformExtendedAttributes( input.getExtendedAttributes() ),
                                  transformSourceLocations( input.getSourceLocations() ) );
    _type = null;
    return definition;
  }

  @Nonnull
  @Override
  protected DictionaryDefinition transformDictionary( @Nonnull final DictionaryDefinition input )
  {
    _type = input.getName();
    _typeIsMixin = false;
    final DocEntry docEntry = _runtime.findDocEntry( _type, null );
    final DictionaryDefinition definition =
      new DictionaryDefinition( input.getName(),
                                input.getInherits(),
                                transformDictionaryMembers( input.getMembers() ),
                                null == docEntry ?
                                transformDocumentation( input.getDocumentation() ) :
                                createDocumentationElement( docEntry ),
                                transformExtendedAttributes( input.getExtendedAttributes() ),
                                transformSourceLocations( input.getSourceLocations() ) );
    _type = null;
    return definition;
  }

  @Nonnull
  @Override
  protected PartialDictionaryDefinition transformPartialDictionary( @Nonnull final PartialDictionaryDefinition input )
  {
    _type = input.getName();
    _typeIsMixin = false;
    final DocEntry docEntry = _runtime.findDocEntry( _type, null );
    final PartialDictionaryDefinition definition =
      new PartialDictionaryDefinition( input.getName(),
                                       transformDictionaryMembers( input.getMembers() ),
                                       null == docEntry ?
                                       transformDocumentation( input.getDocumentation() ) :
                                       createDocumentationElement( docEntry ),
                                       transformExtendedAttributes( input.getExtendedAttributes() ),
                                       transformSourceLocations( input.getSourceLocations() ) );
    _type = null;
    return definition;
  }

  @Nonnull
  @Override
  protected NamespaceDefinition transformNamespace( @Nonnull final NamespaceDefinition input )
  {
    _type = input.getName();
    _typeIsMixin = false;
    final DocEntry docEntry = _runtime.findDocEntry( _type, null );
    final NamespaceDefinition definition =
      new NamespaceDefinition( input.getName(),
                               transformOperationMembers( input.getOperations() ),
                               transformAttributeMembers( input.getAttributes() ),
                               null == docEntry ?
                               transformDocumentation( input.getDocumentation() ) :
                               createDocumentationElement( docEntry ),
                               transformExtendedAttributes( input.getExtendedAttributes() ),
                               transformSourceLocations( input.getSourceLocations() ) );
    _type = null;
    return definition;
  }

  @Nonnull
  @Override
  protected PartialNamespaceDefinition transformPartialNamespace( @Nonnull final PartialNamespaceDefinition input )
  {
    _type = input.getName();
    _typeIsMixin = false;
    final DocEntry docEntry = _runtime.findDocEntry( _type, null );
    final PartialNamespaceDefinition definition =
      new PartialNamespaceDefinition( input.getName(),
                                      transformOperationMembers( input.getOperations() ),
                                      transformAttributeMembers( input.getAttributes() ),
                                      null == docEntry ?
                                      transformDocumentation( input.getDocumentation() ) :
                                      createDocumentationElement( docEntry ),
                                      transformExtendedAttributes( input.getExtendedAttributes() ),
                                      transformSourceLocations( input.getSourceLocations() ) );
    _type = null;
    return definition;
  }

  @Nonnull
  @Override
  protected ConstMember transformConstant( @Nonnull final ConstMember input )
  {
    final DocEntry docEntry = null != _type ? _runtime.findDocEntry( _type, input.getName() ) : null;
    return new ConstMember( input.getName(),
                            transformType( input.getType() ),
                            transformConstValue( input.getValue() ),
                            null == docEntry ?
                            transformDocumentation( input.getDocumentation() ) :
                            createDocumentationElement( docEntry ),
                            transformExtendedAttributes( input.getExtendedAttributes() ),
                            transformSourceLocations( input.getSourceLocations() ) );
  }

  @Nonnull
  @Override
  protected EventMember transformEventMember( @Nonnull final EventMember input )
  {
    final DocEntry docEntry = null != _type ? _runtime.findDocEntry( _type, input.getName() + "_event" ) : null;
    return new EventMember( input.getName(),
                            input.getEventType(),
                            null == docEntry ?
                            transformDocumentation( input.getDocumentation() ) :
                            createDocumentationElement( docEntry ),
                            transformExtendedAttributes( input.getExtendedAttributes() ),
                            transformSourceLocations( input.getSourceLocations() ) );
  }

  @Nonnull
  @Override
  protected DictionaryMember transformDictionaryMember( @Nonnull final DictionaryMember input )
  {
    final DocEntry docEntry = null != _type ? _runtime.findDocEntry( _type, input.getName() ) : null;
    return new DictionaryMember( input.getName(),
                                 transformType( input.getType() ),
                                 input.isOptional(),
                                 input.getDefaultValue(),
                                 null == docEntry ?
                                 transformDocumentation( input.getDocumentation() ) :
                                 createDocumentationElement( docEntry ),
                                 transformExtendedAttributes( input.getExtendedAttributes() ),
                                 transformSourceLocations( input.getSourceLocations() ) );
  }

  @Nonnull
  @Override
  protected AttributeMember transformAttributeMember( @Nonnull final AttributeMember input )
  {
    final DocEntry docEntry = null != _type ? _runtime.findDocEntry( _type, input.getName() ) : null;
    return new AttributeMember( input.getName(),
                                transformType( input.getType() ),
                                input.getModifiers(),
                                null == docEntry ?
                                transformDocumentation( input.getDocumentation() ) :
                                createDocumentationElement( docEntry ),
                                transformExtendedAttributes( input.getExtendedAttributes() ),
                                transformSourceLocations( input.getSourceLocations() ) );
  }

  @Nonnull
  @Override
  protected OperationMember transformOperationMember( @Nonnull final OperationMember input )
  {
    final DocEntry docEntry =
      null != _type ?
      _runtime.findDocEntry( _type, OperationMember.Kind.CONSTRUCTOR == input.getKind() ? _type : input.getName() ) :
      null;
    return new OperationMember( input.getKind(),
                                input.getName(),
                                transformArguments( input.getArguments() ),
                                transformType( input.getReturnType() ),
                                null == docEntry ?
                                transformDocumentation( input.getDocumentation() ) :
                                createDocumentationElement( docEntry ),
                                transformExtendedAttributes( input.getExtendedAttributes() ),
                                transformSourceLocations( input.getSourceLocations() ) );
  }

  @Nonnull
  @Override
  protected OperationMember transformOptionalOperationMember( @Nonnull final OperationMember input )
  {
    return transformOperationMember( input );
  }

  @Nonnull
  private DocumentationElement createDocumentationElement( @Nonnull final DocEntry docEntry )
  {
    return new DocumentationElement( docEntry.getDescription(),
                                     Collections.singletonList( seeTag( docEntry ) ),
                                     Collections.emptyList() );
  }

  @Nonnull
  private DocumentationBlockTag seeTag( @Nonnull final DocEntry docEntry )
  {
    final String label = docEntry.getKind() == DocKind.Event ? docEntry.getEventName() + " event" : docEntry.getName();
    final String seeLink = "<a href=\"" + docEntry.getHref() + "\">" + label + " - MDN</a>";
    return new DocumentationBlockTag( "see", seeLink );
  }
}
