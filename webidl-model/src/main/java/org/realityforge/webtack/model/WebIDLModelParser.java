package org.realityforge.webtack.model;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.realityforge.webtack.webidl.parser.WebIDLParser;
import org.realityforge.webtack.webidl.parser.WebIDLParserTool;

@SuppressWarnings( "DuplicatedCode" )
public final class WebIDLModelParser
{
  @Nonnull
  private static final Map<String, Kind> STRING_KIND_MAP = Collections.unmodifiableMap( new HashMap<String, Kind>()
  {
    {
      put( "DOMString", Kind.DOMString );
      put( "ByteString", Kind.ByteString );
      put( "USVString", Kind.USVString );
    }
  } );
  @Nonnull
  private static final Map<String, Kind> BUFFER_KIND_MAP = Collections.unmodifiableMap( new HashMap<String, Kind>()
  {
    {
      put( "ArrayBuffer", Kind.ArrayBuffer );
      put( "DataView", Kind.DataView );
      put( "Int8Array", Kind.Int8Array );
      put( "Int16Array", Kind.Int16Array );
      put( "Int32Array", Kind.Int32Array );
      put( "Uint8Array", Kind.Uint8Array );
      put( "Uint16Array", Kind.Uint16Array );
      put( "Uint32Array", Kind.Uint32Array );
      put( "Uint8ClampedArray", Kind.Uint8ClampedArray );
      put( "Float32Array", Kind.Float32Array );
      put( "Float64Array", Kind.Float64Array );
    }
  } );

  private WebIDLModelParser()
  {
  }

  @Nonnull
  private static WebIDLSchema parse( @Nonnull final WebIDLParser.WebIDLContext ctx )
  {
    final List<Definition> definitions = parse( ctx.definitions() );

    final Map<String, CallbackDefinition> callbacks = new HashMap<>();
    final Map<String, CallbackInterfaceDefinition> callbackInterfaces = new HashMap<>();
    final Map<String, DictionaryDefinition> dictionaries = new HashMap<>();
    final Map<String, EnumerationDefinition> enumerations = new HashMap<>();
    final Map<String, InterfaceDefinition> interfaces = new HashMap<>();
    final Map<String, MixinDefinition> mixins = new HashMap<>();
    final Map<String, IncludesStatement> includes = new HashMap<>();
    final Map<String, NamespaceDefinition> namespaces = new HashMap<>();
    final Map<String, List<PartialDictionaryDefinition>> partialDictionaries = new HashMap<>();
    final Map<String, List<PartialInterfaceDefinition>> partialInterfaces = new HashMap<>();
    final Map<String, List<PartialMixinDefinition>> partialMixins = new HashMap<>();
    final Map<String, List<PartialNamespaceDefinition>> partialNamespaces = new HashMap<>();
    final Map<String, TypedefDefinition> typedefs = new HashMap<>();

    for ( final Definition definition : definitions )
    {
      if ( definition instanceof CallbackDefinition )
      {
        final CallbackDefinition value = (CallbackDefinition) definition;
        addToCollection( "callbacks", callbacks, value.getName(), value );
      }
      else if ( definition instanceof CallbackInterfaceDefinition )
      {
        final CallbackInterfaceDefinition value = (CallbackInterfaceDefinition) definition;
        addToCollection( "callback interfaces", callbackInterfaces, value.getName(), value );
      }
      else if ( definition instanceof DictionaryDefinition )
      {
        final DictionaryDefinition value = (DictionaryDefinition) definition;
        addToCollection( "dictionaries", dictionaries, value.getName(), value );
      }
      else if ( definition instanceof EnumerationDefinition )
      {
        final EnumerationDefinition value = (EnumerationDefinition) definition;
        addToCollection( "enumerations", enumerations, value.getName(), value );
      }
      else if ( definition instanceof InterfaceDefinition )
      {
        final InterfaceDefinition value = (InterfaceDefinition) definition;
        addToCollection( "interfaces", interfaces, value.getName(), value );
      }
      else if ( definition instanceof MixinDefinition )
      {
        final MixinDefinition value = (MixinDefinition) definition;
        addToCollection( "mixins", mixins, value.getName(), value );
      }
      else if ( definition instanceof IncludesStatement )
      {
        final IncludesStatement value = (IncludesStatement) definition;
        addToCollection( "includes", includes, value.getInterfaceName() + "+" + value.getMixinName(), value );
      }
      else if ( definition instanceof NamespaceDefinition )
      {
        final NamespaceDefinition value = (NamespaceDefinition) definition;
        addToCollection( "namespaces", namespaces, value.getName(), value );
      }
      else if ( definition instanceof TypedefDefinition )
      {
        final TypedefDefinition value = (TypedefDefinition) definition;
        addToCollection( "typedefs", typedefs, value.getName(), value );
      }
    }

    return new WebIDLSchema( callbacks,
                             callbackInterfaces,
                             dictionaries,
                             enumerations,
                             interfaces,
                             mixins,
                             includes,
                             namespaces,
                             partialDictionaries,
                             partialInterfaces,
                             partialMixins,
                             partialNamespaces,
                             typedefs );
  }

  private static <T extends Definition> void addToCollection( @Nonnull final String collectionName,
                                                              @Nonnull final Map<String, T> collection,
                                                              @Nonnull final String name,
                                                              @Nonnull final T value )
  {
    if ( collection.containsKey( name ) )
    {
      throw new IllegalModelException( "Multiple " + collectionName + " defined with the name '" + name + "'" );
    }
    else
    {
      collection.put( name, value );
    }
  }

  @Nonnull
  static List<Definition> parse( @Nonnull final WebIDLParser.DefinitionsContext ctx )
  {
    WebIDLParser.DefinitionsContext definitionsContext = ctx;
    WebIDLParser.DefinitionContext definitionContext;
    final List<Definition> definitions = new ArrayList<>();
    while ( null != ( definitionContext = definitionsContext.definition() ) )
    {
      final List<ExtendedAttribute> extendedAttributes = parse( definitionsContext.extendedAttributeList() );
      definitions.add( parse( definitionContext, extendedAttributes ) );
      definitionsContext = definitionsContext.definitions();
    }

    return Collections.unmodifiableList( definitions );
  }

  @Nonnull
  static Definition parse( @Nonnull final WebIDLParser.DefinitionContext ctx,
                           @Nonnull final List<ExtendedAttribute> extendedAttributes )
  {
    final WebIDLParser.CallbackOrInterfaceOrMixinContext callbackOrInterfaceOrMixinContext =
      ctx.callbackOrInterfaceOrMixin();
    if ( null != callbackOrInterfaceOrMixinContext )
    {
      final WebIDLParser.CallbackRestOrInterfaceContext callbackRestOrInterfaceContext =
        callbackOrInterfaceOrMixinContext.callbackRestOrInterface();
      if ( null != callbackRestOrInterfaceContext )
      {
        final WebIDLParser.CallbackRestContext callbackRestContext = callbackRestOrInterfaceContext.callbackRest();
        if ( null != callbackRestContext )
        {
          final String name = callbackRestContext.IDENTIFIER().getText();
          final Type returnType = parse( callbackRestContext.returnType() );
          final List<Argument> arguments = parse( callbackRestContext.argumentList() );

          return new CallbackDefinition( name, returnType, arguments, extendedAttributes );
        }
        else
        {
          return parseCallbackInterface( callbackRestOrInterfaceContext, extendedAttributes );
        }
      }
      else
      {
        final WebIDLParser.InterfaceOrMixinContext interfaceOrMixinContext =
          callbackOrInterfaceOrMixinContext.interfaceOrMixin();
        assert null != interfaceOrMixinContext;
        final WebIDLParser.InterfaceRestContext interfaceRestContext = interfaceOrMixinContext.interfaceRest();
        if ( null != interfaceRestContext )
        {
          return parse( interfaceRestContext, extendedAttributes );
        }
        else
        {
          final WebIDLParser.MixinRestContext mixinRestContext = interfaceOrMixinContext.mixinRest();
          assert null != mixinRestContext;
          return parse( mixinRestContext, false, extendedAttributes );
        }
      }
    }
    final WebIDLParser.NamespaceContext namespaceContext = ctx.namespace();
    if ( null != namespaceContext )
    {
      return parse( namespaceContext, false, extendedAttributes );
    }
    final WebIDLParser.PartialContext partialContext = ctx.partial();
    if ( null != partialContext )
    {
      final WebIDLParser.PartialDefinitionContext partialDefinitionContext = partialContext.partialDefinition();
      final WebIDLParser.PartialInterfaceOrPartialMixinContext partialInterfaceOrPartialMixinContext =
        partialDefinitionContext.partialInterfaceOrPartialMixin();
      if ( null != partialInterfaceOrPartialMixinContext )
      {
        final WebIDLParser.PartialInterfaceRestContext partialInterfaceRestContext =
          partialInterfaceOrPartialMixinContext.partialInterfaceRest();
        if ( null != partialInterfaceRestContext )
        {
          return parse( partialInterfaceRestContext, extendedAttributes );
        }
        else
        {
          final WebIDLParser.MixinRestContext mixinRestContext = partialInterfaceOrPartialMixinContext.mixinRest();
          assert null != mixinRestContext;
          return parse( mixinRestContext, true, extendedAttributes );
        }
      }
      final WebIDLParser.PartialDictionaryContext partialDictionaryContext =
        partialDefinitionContext.partialDictionary();
      if ( null != partialDictionaryContext )
      {
        return parse( partialDictionaryContext, extendedAttributes );
      }
      else
      {
        final WebIDLParser.NamespaceContext partialNamespaceContext = partialDefinitionContext.namespace();
        assert null != partialNamespaceContext;
        return parse( partialNamespaceContext, true, extendedAttributes );
      }
    }
    final WebIDLParser.DictionaryContext dictionaryContext = ctx.dictionary();
    if ( null != dictionaryContext )
    {
      return parse( dictionaryContext, extendedAttributes );
    }
    final WebIDLParser.EnumDefinitionContext enumDefinitionContext = ctx.enumDefinition();
    if ( null != enumDefinitionContext )
    {
      return parse( enumDefinitionContext, extendedAttributes );
    }
    final WebIDLParser.TypedefContext typedefContext = ctx.typedef();
    if ( null != typedefContext )
    {
      return parse( typedefContext, extendedAttributes );
    }
    final WebIDLParser.IncludesStatementContext includesStatementContext = ctx.includesStatement();
    assert null != includesStatementContext;
    return parse( includesStatementContext, extendedAttributes );
  }

  @Nonnull
  private static CallbackInterfaceDefinition parseCallbackInterface( @Nonnull final WebIDLParser.CallbackRestOrInterfaceContext callbackRestOrInterfaceContext,
                                                                     @Nonnull final List<ExtendedAttribute> extendedAttributes )
  {
    OperationMember operation = null;
    final List<ConstMember> constMembers = new ArrayList<>();
    final String name = callbackRestOrInterfaceContext.IDENTIFIER().getText();
    WebIDLParser.CallbackInterfaceMembersContext callbackInterfaceMembersContext =
      callbackRestOrInterfaceContext.callbackInterfaceMembers();
    assert null != callbackInterfaceMembersContext;
    while ( callbackInterfaceMembersContext.getChildCount() > 0 )
    {
      final List<ExtendedAttribute> memberExtendedAttributes =
        parse( callbackInterfaceMembersContext.extendedAttributeList() );
      final WebIDLParser.CallbackInterfaceMemberContext callbackInterfaceMemberContext =
        callbackInterfaceMembersContext.callbackInterfaceMember();
      final WebIDLParser.ConstMemberContext constMemberContext = callbackInterfaceMemberContext.constMember();
      if ( null != constMemberContext )
      {
        constMembers.add( parse( constMemberContext, memberExtendedAttributes ) );
      }
      else
      {
        final WebIDLParser.RegularOperationContext regularOperationContext =
          callbackInterfaceMemberContext.regularOperation();
        assert null != regularOperationContext;
        final OperationMember candidate =
          parse( regularOperationContext, OperationMember.Kind.DEFAULT, memberExtendedAttributes );
        if ( null != operation )
        {
          throw new IllegalModelException( "IDL attempted to define duplicate operation " +
                                           "named '" + candidate.getName() + "' in callback interface named '" +
                                           name + "' when an existing operation exists " +
                                           "named '" + operation.getName() + "'" );
        }
        else
        {
          operation = candidate;
        }
      }

      callbackInterfaceMembersContext = callbackInterfaceMembersContext.callbackInterfaceMembers();
    }

    if ( null == operation )
    {
      throw new IllegalModelException( "IDL attempted to define callback interface named '" +
                                       name + "' without specifying an operation" );
    }
    return new CallbackInterfaceDefinition( name,
                                            operation,
                                            Collections.unmodifiableList( constMembers ),
                                            extendedAttributes );
  }

  @Nonnull
  private static Definition parse( @Nonnull final WebIDLParser.MixinRestContext ctx,
                                   final boolean partial,
                                   @Nonnull final List<ExtendedAttribute> extendedAttributes )
  {
    final List<ConstMember> constants = new ArrayList<>();
    final List<AttributeMember> attributes = new ArrayList<>();
    final List<OperationMember> operations = new ArrayList<>();

    final String name = ctx.IDENTIFIER().getText();
    WebIDLParser.MixinMembersContext mixinMembersContext = ctx.mixinMembers();
    while ( mixinMembersContext.getChildCount() > 0 )
    {
      final List<ExtendedAttribute> memberExtendedAttributes = parse( mixinMembersContext.extendedAttributeList() );
      final WebIDLParser.MixinMemberContext mixinMemberContext = mixinMembersContext.mixinMember();
      final WebIDLParser.ConstMemberContext constMemberContext = mixinMemberContext.constMember();
      if ( null != constMemberContext )
      {
        constants.add( parse( constMemberContext, memberExtendedAttributes ) );
      }
      else
      {
        final WebIDLParser.RegularOperationContext regularOperationContext = mixinMemberContext.regularOperation();
        if ( null != regularOperationContext )
        {
          operations.add( parse( regularOperationContext, OperationMember.Kind.DEFAULT, memberExtendedAttributes ) );
        }
        else
        {
          final WebIDLParser.StringifierContext stringifier = mixinMemberContext.stringifier();
          if ( null != stringifier )
          {
            final Member member = parse( stringifier, memberExtendedAttributes );
            if ( member instanceof OperationMember )
            {
              operations.add( (OperationMember) member );
            }
            else
            {
              attributes.add( (AttributeMember) member );
            }
          }
          else
          {
            final Set<AttributeMember.Modifier> modifiers = new HashSet<>();
            if ( mixinMemberContext.readOnly().getChildCount() > 0 )
            {
              modifiers.add( AttributeMember.Modifier.READ_ONLY );
            }
            attributes.add( parse( mixinMemberContext.attributeRest(), modifiers, memberExtendedAttributes ) );
          }
        }
      }
      mixinMembersContext = mixinMembersContext.mixinMembers();
    }

    return partial ?
           new PartialMixinDefinition( name,
                                       Collections.unmodifiableList( constants ),
                                       Collections.unmodifiableList( attributes ),
                                       Collections.unmodifiableList( operations ),
                                       extendedAttributes ) :
           new MixinDefinition( name,
                                Collections.unmodifiableList( constants ),
                                Collections.unmodifiableList( attributes ),
                                Collections.unmodifiableList( operations ),
                                extendedAttributes );

  }

  @Nonnull
  static List<Argument> parse( @Nonnull final WebIDLParser.ArgumentListContext ctx )
  {
    final WebIDLParser.ArgumentContext argumentContext = ctx.argument();
    if ( null != argumentContext )
    {
      final List<Argument> arguments = new ArrayList<>();
      arguments.add( parse( argumentContext ) );
      WebIDLParser.ArgumentsContext argumentsContext = ctx.arguments();
      while ( argumentsContext.getChildCount() > 0 )
      {
        arguments.add( parse( argumentsContext.argument() ) );
        argumentsContext = argumentsContext.arguments();
      }
      return Collections.unmodifiableList( arguments );
    }
    else
    {
      return Collections.emptyList();
    }
  }

  @Nonnull
  static Argument parse( @Nonnull final WebIDLParser.ArgumentContext ctx )
  {
    final List<ExtendedAttribute> extendedAttributes = parse( ctx.extendedAttributeList() );
    final WebIDLParser.ArgumentRestContext argumentRestContext = ctx.argumentRest();
    final WebIDLParser.ArgumentNameContext argumentNameContext = argumentRestContext.argumentName();
    final TerminalNode identifier = argumentNameContext.IDENTIFIER();
    final String name = null != identifier ? identifier.getText() : argumentNameContext.argumentNameKeyword().getText();
    final WebIDLParser.TypeWithExtendedAttributesContext typeWithExtendedAttributesContext =
      argumentRestContext.typeWithExtendedAttributes();
    final Type type;
    final boolean optional;
    final boolean variadic;
    final DefaultValue defaultValue;
    if ( null != typeWithExtendedAttributesContext )
    {
      type = parse( typeWithExtendedAttributesContext );
      optional = true;
      variadic = false;
      final WebIDLParser.DefaultValueContext defaultValueContext =
        argumentRestContext.defaultAssignment().defaultValue();
      defaultValue = null != defaultValueContext ? parse( defaultValueContext ) : null;
    }
    else
    {
      type = parse( argumentRestContext.type() );
      optional = false;
      variadic = argumentRestContext.ellipsis().getChildCount() > 0;
      defaultValue = null;
    }
    return new Argument( name, type, optional, variadic, defaultValue, extendedAttributes );
  }

  @Nonnull
  static String extractString( @Nonnull final TerminalNode string )
  {
    final String text = string.getText();
    assert text.startsWith( "\"" );
    assert text.endsWith( "\"" );
    return text.substring( 1, text.length() - 1 );
  }

  @Nonnull
  static OperationMember parse( @Nonnull final WebIDLParser.OperationContext ctx,
                                @Nonnull final List<ExtendedAttribute> extendedAttributes )
  {
    final WebIDLParser.RegularOperationContext regularOperationContext = ctx.regularOperation();
    if ( null != regularOperationContext )
    {
      return parse( regularOperationContext, OperationMember.Kind.DEFAULT, extendedAttributes );
    }
    else
    {
      final WebIDLParser.SpecialOperationContext specialOperationContext = ctx.specialOperation();
      assert null != specialOperationContext;
      return parse( specialOperationContext, extendedAttributes );
    }
  }

  @Nonnull
  private static OperationMember parse( @Nonnull final WebIDLParser.SpecialOperationContext ctx,
                                        @Nonnull final List<ExtendedAttribute> extendedAttributes )
  {
    final OperationMember.Kind kind = OperationMember.Kind.valueOf( ctx.special().getText().toUpperCase() );
    return parse( ctx.regularOperation(), kind, extendedAttributes );
  }

  @Nonnull
  private static OperationMember parse( @Nonnull final WebIDLParser.RegularOperationContext ctx,
                                        @Nonnull final OperationMember.Kind kind,
                                        @Nonnull final List<ExtendedAttribute> extendedAttributes )
  {
    final Type returnType = parse( ctx.returnType() );
    final WebIDLParser.OperationNameContext operationNameContext =
      ctx.operationRest().optionalOperationName().operationName();
    final String name;
    if ( null != operationNameContext )
    {
      final WebIDLParser.OperationNameKeywordContext operationNameKeywordContext =
        operationNameContext.operationNameKeyword();
      if ( null != operationNameKeywordContext )
      {
        name = operationNameKeywordContext.getText();
      }
      else
      {
        name = operationNameContext.IDENTIFIER().getText();
      }
    }
    else
    {
      name = null;
    }
    final List<Argument> arguments = parse( ctx.operationRest().argumentList() );
    return new OperationMember( kind, name, arguments, returnType, extendedAttributes );
  }

  @Nonnull
  static AttributeMember parse( @Nonnull final WebIDLParser.ReadWriteAttributeContext ctx,
                                @Nonnull final Set<AttributeMember.Modifier> modifiers,
                                @Nonnull final List<ExtendedAttribute> extendedAttributes )
  {
    if ( ctx.getChildCount() > 1 )
    {
      modifiers.add( AttributeMember.Modifier.INHERIT );
    }
    return parse( ctx.attributeRest(), modifiers, extendedAttributes );
  }

  @Nonnull
  static AttributeMember parse( @Nonnull final WebIDLParser.AttributeRestContext ctx,
                                @Nonnull final Set<AttributeMember.Modifier> modifiers,
                                @Nonnull final List<ExtendedAttribute> extendedAttributes )
  {
    final WebIDLParser.AttributeNameContext attributeNameContext = ctx.attributeName();
    final WebIDLParser.AttributeNameKeywordContext attributeNameKeywordContext =
      attributeNameContext.attributeNameKeyword();
    final String name = null == attributeNameKeywordContext ?
                        attributeNameContext.IDENTIFIER().getText() :
                        attributeNameKeywordContext.getText();
    final Type type = parse( ctx.typeWithExtendedAttributes() );
    return new AttributeMember( name, type, Collections.unmodifiableSet( modifiers ), extendedAttributes );
  }

  @Nonnull
  static MapLikeMember parse( @Nonnull final WebIDLParser.MaplikeRestContext ctx,
                              final boolean readOnly,
                              @Nonnull final List<ExtendedAttribute> extendedAttributes )
  {
    final Type keyType = parse( ctx.typeWithExtendedAttributes( 0 ) );
    final Type valueType = parse( ctx.typeWithExtendedAttributes( 1 ) );
    return new MapLikeMember( keyType, valueType, readOnly, extendedAttributes );
  }

  @Nonnull
  static IterableMember parse( @Nonnull final WebIDLParser.IterableContext ctx,
                               @Nonnull final List<ExtendedAttribute> extendedAttributes )
  {
    final Type type1 = parse( ctx.typeWithExtendedAttributes() );
    final WebIDLParser.TypeWithExtendedAttributesContext optional =
      ctx.optionalType().typeWithExtendedAttributes();
    final Type type2 = null == optional ? null : parse( optional );
    return new IterableMember( null == type2 ? null : type1, null == type2 ? type1 : type2, extendedAttributes );
  }

  @Nonnull
  static AsyncIterableMember parse( @Nonnull final WebIDLParser.AsyncIterableContext ctx,
                                    @Nonnull final List<ExtendedAttribute> extendedAttributes )
  {
    final Type keyType = parse( ctx.typeWithExtendedAttributes( 0 ) );
    final Type valueType = parse( ctx.typeWithExtendedAttributes( 1 ) );
    return new AsyncIterableMember( keyType, valueType, extendedAttributes );
  }

  @Nonnull
  static SetLikeMember parse( @Nonnull final WebIDLParser.SetlikeRestContext ctx,
                              final boolean readOnly,
                              @Nonnull final List<ExtendedAttribute> extendedAttributes )
  {
    final Type type = parse( ctx.typeWithExtendedAttributes() );
    return new SetLikeMember( type, readOnly, extendedAttributes );
  }

  @Nonnull
  static OperationMember parse( @Nonnull final WebIDLParser.ConstructorContext ctx,
                                @Nonnull final List<ExtendedAttribute> extendedAttributes )
  {
    final List<Argument> arguments = parse( ctx.argumentList() );
    return new OperationMember( OperationMember.Kind.CONSTRUCTOR,
                                null,
                                arguments,
                                new Type( Kind.Void, Collections.emptyList(), false ),
                                extendedAttributes );
  }

  @Nonnull
  static Member parse( @Nonnull final WebIDLParser.StringifierContext ctx,
                       @Nonnull final List<ExtendedAttribute> extendedAttributes )
  {
    final WebIDLParser.StringifierRestContext stringifierRestContext = ctx.stringifierRest();
    final WebIDLParser.AttributeRestContext attributeRestContext = stringifierRestContext.attributeRest();
    if ( null != attributeRestContext )
    {
      final Set<AttributeMember.Modifier> modifiers = new HashSet<>();
      if ( stringifierRestContext.readOnly().getChildCount() > 0 )
      {
        modifiers.add( AttributeMember.Modifier.READ_ONLY );
      }
      modifiers.add( AttributeMember.Modifier.STRINGIFIER );
      return parse( attributeRestContext, modifiers, extendedAttributes );
    }
    else
    {
      final WebIDLParser.RegularOperationContext regularOperationContext = stringifierRestContext.regularOperation();
      if ( null != regularOperationContext )
      {
        return parse( regularOperationContext, OperationMember.Kind.STRINGIFIER, extendedAttributes );
      }
      else
      {
        return new OperationMember( OperationMember.Kind.STRINGIFIER,
                                    null,
                                    Collections.emptyList(),
                                    new Type( Kind.DOMString, Collections.emptyList(), false ),
                                    extendedAttributes );
      }
    }
  }

  @Nonnull
  static Member parse( @Nonnull final WebIDLParser.StaticMemberContext ctx,
                       @Nonnull final List<ExtendedAttribute> extendedAttributes )
  {
    final WebIDLParser.StaticMemberRestContext staticMemberRestContext = ctx.staticMemberRest();
    final WebIDLParser.AttributeRestContext attributeRestContext = staticMemberRestContext.attributeRest();
    if ( null != attributeRestContext )
    {
      final Set<AttributeMember.Modifier> modifiers = new HashSet<>();
      if ( staticMemberRestContext.readOnly().getChildCount() > 0 )
      {
        modifiers.add( AttributeMember.Modifier.READ_ONLY );
      }
      modifiers.add( AttributeMember.Modifier.STATIC );
      return parse( attributeRestContext, modifiers, extendedAttributes );
    }
    else
    {
      final WebIDLParser.RegularOperationContext regularOperationContext = staticMemberRestContext.regularOperation();
      assert null != regularOperationContext;
      return parse( regularOperationContext, OperationMember.Kind.STATIC, extendedAttributes );
    }
  }

  @Nonnull
  static Member parse( @Nonnull final WebIDLParser.ReadOnlyMemberContext ctx,
                       @Nonnull final List<ExtendedAttribute> extendedAttributes )
  {
    final WebIDLParser.ReadOnlyMemberRestContext readOnlyMemberRestContext = ctx.readOnlyMemberRest();
    final WebIDLParser.AttributeRestContext attributeRestContext = readOnlyMemberRestContext.attributeRest();
    if ( null != attributeRestContext )
    {
      final Set<AttributeMember.Modifier> modifiers = new HashSet<>();
      modifiers.add( AttributeMember.Modifier.READ_ONLY );
      return parse( attributeRestContext, modifiers, extendedAttributes );
    }
    final WebIDLParser.MaplikeRestContext maplikeRestContext = readOnlyMemberRestContext.maplikeRest();
    if ( null != maplikeRestContext )
    {
      return parse( maplikeRestContext, true, extendedAttributes );
    }
    final WebIDLParser.SetlikeRestContext setlikeRestContext = readOnlyMemberRestContext.setlikeRest();
    assert null != setlikeRestContext;
    return parse( setlikeRestContext, true, extendedAttributes );
  }

  @Nonnull
  static ConstMember parse( @Nonnull final WebIDLParser.ConstMemberContext ctx,
                            @Nonnull final List<ExtendedAttribute> extendedAttributes )
  {
    final String name = ctx.IDENTIFIER().getText();
    final WebIDLParser.ConstMemberTypeContext constMemberTypeContext = ctx.constMemberType();
    final TerminalNode identifier = constMemberTypeContext.IDENTIFIER();
    final Type type;
    if ( null != identifier )
    {
      type = new TypeReference( identifier.getText(), Collections.emptyList(), false );
    }
    else
    {
      type = parse( constMemberTypeContext.primitiveType(), Collections.emptyList(), false );
    }
    final ConstValue value = parse( ctx.constMemberValue() );
    return new ConstMember( name, type, value, extendedAttributes );
  }

  @Nonnull
  static ConstValue parse( @Nonnull final WebIDLParser.ConstMemberValueContext ctx )
  {
    final WebIDLParser.BooleanLiteralContext booleanLiteralContext = ctx.booleanLiteral();
    if ( null != booleanLiteralContext )
    {
      return new ConstValue( "true".equals( booleanLiteralContext.getText() ) ?
                             ConstValue.Kind.True :
                             ConstValue.Kind.False, null );
    }
    final WebIDLParser.FloatLiteralContext floatLiteralContext = ctx.floatLiteral();
    if ( null != floatLiteralContext )
    {
      final TerminalNode decimal = floatLiteralContext.DECIMAL();
      if ( null != decimal )
      {
        return new ConstValue( ConstValue.Kind.Decimal, decimal.getText() );
      }
      else
      {
        final String text = floatLiteralContext.getText();
        if ( "-Infinity".equals( text ) )
        {
          return new ConstValue( ConstValue.Kind.NegativeInfinity, null );
        }
        else if ( "Infinity".equals( text ) )
        {
          return new ConstValue( ConstValue.Kind.PositiveInfinity, null );
        }
        else
        {
          assert "NaN".equals( text );
          return new ConstValue( ConstValue.Kind.NaN, null );
        }
      }
    }
    return new ConstValue( ConstValue.Kind.Integer, ctx.INTEGER().getText() );
  }

  @Nonnull
  static DefaultValue parse( @Nonnull final WebIDLParser.DefaultValueContext ctx )
  {
    final WebIDLParser.ConstMemberValueContext constMemberValueContext = ctx.constMemberValue();
    if ( null != constMemberValueContext )
    {
      return new DefaultValue( DefaultValue.Kind.Const, parse( constMemberValueContext ), null );
    }
    final TerminalNode string = ctx.STRING();
    if ( null != string )
    {
      return new DefaultValue( DefaultValue.Kind.String, null, extractString( string ) );
    }
    final String child = ctx.getChild( 0 ).getText();
    if ( "[".equals( child ) )
    {
      return new DefaultValue( DefaultValue.Kind.EmptySequence, null, null );
    }
    else if ( "{".equals( child ) )
    {
      return new DefaultValue( DefaultValue.Kind.EmptyDictionary, null, null );
    }
    else
    {
      assert "null".equals( child );
      return new DefaultValue( DefaultValue.Kind.Null, null, null );
    }
  }

  @Nonnull
  static EnumerationDefinition parse( @Nonnull final WebIDLParser.EnumDefinitionContext ctx,
                                      @Nonnull final List<ExtendedAttribute> extendedAttributes )
  {
    final String name = ctx.IDENTIFIER().getText();
    final Set<String> values = parse( ctx.enumValueList() );
    return new EnumerationDefinition( name, values, extendedAttributes );
  }

  @Nonnull
  private static Set<String> parse( @Nonnull final WebIDLParser.EnumValueListContext enumValueListContext )
  {
    final Set<String> values = new LinkedHashSet<>();
    values.add( extractString( enumValueListContext.STRING() ) );

    WebIDLParser.EnumValueListStringContext enumValueListStringContext =
      enumValueListContext.enumValueListComma().enumValueListString();
    while ( null != enumValueListStringContext )
    {
      final TerminalNode string = enumValueListStringContext.STRING();
      if ( null != string )
      {
        values.add( extractString( string ) );
      }
      final WebIDLParser.EnumValueListCommaContext enumValueListCommaContext =
        enumValueListStringContext.enumValueListComma();
      enumValueListStringContext =
        null != enumValueListCommaContext ? enumValueListCommaContext.enumValueListString() : null;
    }

    return Collections.unmodifiableSet( values );
  }

  @Nonnull
  static List<ExtendedAttribute> parse( @Nonnull final WebIDLParser.ExtendedAttributeListContext ctx )
  {
    final WebIDLParser.ExtendedAttributeContext extendedAttributeContext = ctx.extendedAttribute();
    if ( null == extendedAttributeContext )
    {
      return Collections.emptyList();
    }
    else
    {
      final List<ExtendedAttribute> attributes = new ArrayList<>();
      attributes.add( parse( extendedAttributeContext ) );
      collectAttributes( attributes, ctx.extendedAttributes() );
      return Collections.unmodifiableList( attributes );
    }
  }

  private static void collectAttributes( @Nonnull final List<ExtendedAttribute> attributes,
                                         @Nonnull final WebIDLParser.ExtendedAttributesContext extendedAttributesContext )
  {
    final WebIDLParser.ExtendedAttributeContext attr = extendedAttributesContext.extendedAttribute();
    if ( null != attr )
    {
      attributes.add( parse( attr ) );
      collectAttributes( attributes, extendedAttributesContext.extendedAttributes() );
    }
  }

  @Nonnull
  static ExtendedAttribute parse( @Nonnull final WebIDLParser.ExtendedAttributeContext ctx )
  {
    final WebIDLParser.ExtendedAttributeNoArgsContext noArgsContext = ctx.extendedAttributeNoArgs();
    if ( null != noArgsContext )
    {
      return ExtendedAttribute.createExtendedAttributeNoArgs( noArgsContext.IDENTIFIER().getText() );
    }
    final WebIDLParser.ExtendedAttributeIdentContext identContext = ctx.extendedAttributeIdent();
    if ( null != identContext )
    {
      return ExtendedAttribute.createExtendedAttributeIdent( identContext.IDENTIFIER( 0 ).getText(),
                                                             identContext.IDENTIFIER( 1 ).getText() );
    }
    final WebIDLParser.ExtendedAttributeIdentListContext identListContext = ctx.extendedAttributeIdentList();
    if ( null != identListContext )
    {
      final List<String> identifiers = new ArrayList<>();
      collectIdentifiers( identifiers, identListContext.identifierList() );
      return ExtendedAttribute.createExtendedAttributeIdentList( identListContext.IDENTIFIER().getText(),
                                                                 Collections.unmodifiableList( identifiers ) );
    }
    final WebIDLParser.ExtendedAttributeArgListContext argListContext = ctx.extendedAttributeArgList();
    if ( null != argListContext )
    {
      final String argName = argListContext.IDENTIFIER().getText();
      final List<Argument> arguments = parse( argListContext.argumentList() );
      return ExtendedAttribute.createExtendedAttributeArgList( argName, arguments );
    }
    final WebIDLParser.ExtendedAttributeNamedArgListContext namedArgListContext = ctx.extendedAttributeNamedArgList();
    assert null != namedArgListContext;

    final String name = namedArgListContext.IDENTIFIER( 0 ).getText();
    final String argName = namedArgListContext.IDENTIFIER( 1 ).getText();
    final List<Argument> arguments = parse( namedArgListContext.argumentList() );
    return ExtendedAttribute.createExtendedAttributeNamedArgList( name, argName, arguments );
  }

  private static void collectIdentifiers( @Nonnull final List<String> identifiers,
                                          @Nonnull final WebIDLParser.IdentifierListContext identifierListContext )
  {
    identifiers.add( identifierListContext.IDENTIFIER().getText() );
    collectIdentifiers( identifiers, identifierListContext.identifiers() );
  }

  private static void collectIdentifiers( @Nonnull final List<String> identifiers,
                                          @Nonnull final WebIDLParser.IdentifiersContext identifiersContext )
  {
    final TerminalNode identifier = identifiersContext.IDENTIFIER();
    if ( null != identifier )
    {
      identifiers.add( identifier.getText() );
      collectIdentifiers( identifiers, identifiersContext.identifiers() );
    }
  }

  @Nonnull
  static InterfaceDefinition parse( @Nonnull final WebIDLParser.InterfaceRestContext ctx,
                                    @Nonnull final List<ExtendedAttribute> extendedAttributes )
  {
    final String name = ctx.IDENTIFIER().getText();

    final ArrayList<ConstMember> constants = new ArrayList<>();
    final ArrayList<AttributeMember> attributes = new ArrayList<>();
    final ArrayList<OperationMember> operations = new ArrayList<>();
    IterableMember iterable = null;
    AsyncIterableMember asyncIterable = null;
    MapLikeMember mapLike = null;
    SetLikeMember setLike = null;

    WebIDLParser.InterfaceMembersContext interfaceMembersContext = ctx.interfaceMembers();
    while ( interfaceMembersContext.getChildCount() > 0 )
    {
      final List<ExtendedAttribute> memberExtendedAttributes = parse( interfaceMembersContext.extendedAttributeList() );

      final WebIDLParser.InterfaceMemberContext interfaceMemberContext = interfaceMembersContext.interfaceMember();
      final WebIDLParser.PartialInterfaceMemberContext partialInterfaceMemberContext =
        interfaceMemberContext.partialInterfaceMember();
      if ( null != partialInterfaceMemberContext )
      {
        final Member member = parse( partialInterfaceMemberContext, memberExtendedAttributes );
        if ( member instanceof AttributeMember )
        {
          attributes.add( (AttributeMember) member );
        }
        else if ( member instanceof ConstMember )
        {
          constants.add( (ConstMember) member );
        }
        else if ( member instanceof OperationMember )
        {
          operations.add( (OperationMember) member );
        }
        else if ( member instanceof MapLikeMember )
        {
          if ( null != mapLike )
          {
            throw new IllegalModelException( "IDL attempted to define multiple mapLike members for " +
                                             "interface named '" + name + "'" );
          }
          mapLike = (MapLikeMember) member;
        }
        else if ( member instanceof SetLikeMember )
        {
          if ( null != setLike )
          {
            throw new IllegalModelException( "IDL attempted to define multiple setLike members for " +
                                             "interface named '" + name + "'" );
          }
          setLike = (SetLikeMember) member;
        }
        else if ( member instanceof IterableMember )
        {
          if ( null != iterable )
          {
            throw new IllegalModelException( "IDL attempted to define multiple iterable members for " +
                                             "interface named '" + name + "'" );
          }
          iterable = (IterableMember) member;
        }
        else
        {
          assert member instanceof AsyncIterableMember;
          if ( null != asyncIterable )
          {
            throw new IllegalModelException( "IDL attempted to define multiple async iterable members for " +
                                             "interface named '" + name + "'" );
          }
          asyncIterable = (AsyncIterableMember) member;
        }
      }
      else
      {
        final WebIDLParser.ConstructorContext constructorContext = interfaceMemberContext.constructor();
        assert null != constructorContext;
        operations.add( parse( constructorContext, memberExtendedAttributes ) );
      }

      interfaceMembersContext = interfaceMembersContext.interfaceMembers();
    }

    return new InterfaceDefinition( name,
                                    extractInherits( ctx.inheritance() ),
                                    Collections.unmodifiableList( constants ),
                                    Collections.unmodifiableList( attributes ),
                                    Collections.unmodifiableList( operations ),
                                    iterable,
                                    asyncIterable,
                                    mapLike,
                                    setLike,
                                    extendedAttributes );
  }

  @Nonnull
  static PartialInterfaceDefinition parse( @Nonnull final WebIDLParser.PartialInterfaceRestContext ctx,
                                           @Nonnull final List<ExtendedAttribute> extendedAttributes )
  {
    final String name = ctx.IDENTIFIER().getText();

    final ArrayList<ConstMember> constants = new ArrayList<>();
    final ArrayList<AttributeMember> attributes = new ArrayList<>();
    final ArrayList<OperationMember> operations = new ArrayList<>();
    IterableMember iterable = null;
    AsyncIterableMember asyncIterable = null;
    MapLikeMember mapLike = null;
    SetLikeMember setLike = null;

    WebIDLParser.PartialInterfaceMembersContext partialInterfaceMembersContext = ctx.partialInterfaceMembers();
    while ( partialInterfaceMembersContext.getChildCount() > 0 )
    {
      final List<ExtendedAttribute> memberExtendedAttributes =
        parse( partialInterfaceMembersContext.extendedAttributeList() );

      final WebIDLParser.PartialInterfaceMemberContext partialInterfaceMemberContext =
        partialInterfaceMembersContext.partialInterfaceMember();
      assert null != partialInterfaceMemberContext;
      final Member member = parse( partialInterfaceMemberContext, memberExtendedAttributes );
      if ( member instanceof AttributeMember )
      {
        attributes.add( (AttributeMember) member );
      }
      else if ( member instanceof ConstMember )
      {
        constants.add( (ConstMember) member );
      }
      else if ( member instanceof OperationMember )
      {
        operations.add( (OperationMember) member );
      }
      else if ( member instanceof MapLikeMember )
      {
        if ( null != mapLike )
        {
          throw new IllegalModelException( "IDL attempted to define multiple mapLike members for " +
                                           "partial interface named '" + name + "'" );
        }
        mapLike = (MapLikeMember) member;
      }
      else if ( member instanceof SetLikeMember )
      {
        if ( null != setLike )
        {
          throw new IllegalModelException( "IDL attempted to define multiple setLike members for " +
                                           "partial interface named '" + name + "'" );
        }
        setLike = (SetLikeMember) member;
      }
      else if ( member instanceof IterableMember )
      {
        if ( null != iterable )
        {
          throw new IllegalModelException( "IDL attempted to define multiple iterable members for " +
                                           "partial interface named '" + name + "'" );
        }
        iterable = (IterableMember) member;
      }
      else
      {
        assert member instanceof AsyncIterableMember;
        if ( null != asyncIterable )
        {
          throw new IllegalModelException( "IDL attempted to define multiple async iterable members for " +
                                           "partial interface named '" + name + "'" );
        }
        asyncIterable = (AsyncIterableMember) member;
      }

      partialInterfaceMembersContext = partialInterfaceMembersContext.partialInterfaceMembers();
    }

    return new PartialInterfaceDefinition( name,
                                           Collections.unmodifiableList( constants ),
                                           Collections.unmodifiableList( attributes ),
                                           Collections.unmodifiableList( operations ),
                                           iterable,
                                           asyncIterable,
                                           mapLike,
                                           setLike,
                                           extendedAttributes );
  }

  @Nonnull
  static Member parse( @Nonnull final WebIDLParser.PartialInterfaceMemberContext ctx,
                       @Nonnull final List<ExtendedAttribute> extendedAttributes )
  {
    final WebIDLParser.ConstMemberContext constMemberContext = ctx.constMember();
    if ( null != constMemberContext )
    {
      return parse( constMemberContext, extendedAttributes );
    }
    final WebIDLParser.OperationContext operationContext = ctx.operation();
    if ( null != operationContext )
    {
      return parse( operationContext, extendedAttributes );
    }
    final WebIDLParser.StringifierContext stringifierContext = ctx.stringifier();
    if ( null != stringifierContext )
    {
      return parse( stringifierContext, extendedAttributes );
    }
    final WebIDLParser.StaticMemberContext staticMemberContext = ctx.staticMember();
    if ( null != staticMemberContext )
    {
      return parse( staticMemberContext, extendedAttributes );
    }
    final WebIDLParser.IterableContext iterableContext = ctx.iterable();
    if ( null != iterableContext )
    {
      return parse( iterableContext, extendedAttributes );
    }
    final WebIDLParser.AsyncIterableContext asyncIterableContext = ctx.asyncIterable();
    if ( null != asyncIterableContext )
    {
      return parse( asyncIterableContext, extendedAttributes );
    }
    final WebIDLParser.ReadOnlyMemberContext readOnlyMemberContext = ctx.readOnlyMember();
    if ( null != readOnlyMemberContext )
    {
      return parse( readOnlyMemberContext, extendedAttributes );
    }
    final WebIDLParser.ReadWriteAttributeContext readWriteAttributeContext = ctx.readWriteAttribute();
    if ( null != readWriteAttributeContext )
    {
      return parse( readWriteAttributeContext, new HashSet<>(), extendedAttributes );
    }
    final WebIDLParser.ReadWriteMaplikeContext readWriteMaplikeContext = ctx.readWriteMaplike();
    if ( null != readWriteMaplikeContext )
    {
      return parse( readWriteMaplikeContext.maplikeRest(), false, extendedAttributes );
    }
    final WebIDLParser.ReadWriteSetlikeContext readWriteSetlikeContext = ctx.readWriteSetlike();
    assert null != readWriteSetlikeContext;
    return parse( readWriteSetlikeContext.setlikeRest(), false, extendedAttributes );
  }

  @Nullable
  private static String extractInherits( @Nonnull final WebIDLParser.InheritanceContext inheritance )
  {
    final TerminalNode identifier = inheritance.IDENTIFIER();
    return null != identifier ? identifier.getSymbol().getText() : null;
  }

  @Nonnull
  static TypedefDefinition parse( @Nonnull final WebIDLParser.TypedefContext ctx,
                                  @Nonnull final List<ExtendedAttribute> extendedAttributes )
  {
    final String name = ctx.IDENTIFIER().getText();
    final Type type = parse( ctx.typeWithExtendedAttributes() );
    return new TypedefDefinition( name, type, extendedAttributes );
  }

  @Nonnull
  static Type parse( @Nonnull final WebIDLParser.TypeWithExtendedAttributesContext ctx )
  {
    final WebIDLParser.ExtendedAttributeListContext extendedAttributeListContext = ctx.extendedAttributeList();
    return parse( ctx.type(), parse( extendedAttributeListContext ) );
  }

  @Nonnull
  static Type parse( @Nonnull final WebIDLParser.TypeContext ctx )
  {
    return parse( ctx, Collections.emptyList() );
  }

  @Nonnull
  private static Type parse( @Nonnull final WebIDLParser.TypeContext ctx,
                             @Nonnull final List<ExtendedAttribute> extendedAttributes )
  {
    final WebIDLParser.SingleTypeContext singleTypeContext = ctx.singleType();
    if ( null != singleTypeContext )
    {
      return parse( singleTypeContext, extendedAttributes );
    }
    else
    {
      final boolean nullable = 1 == ctx.nullModifier().getChildCount();
      final WebIDLParser.UnionTypeContext unionTypeContext = ctx.unionType();
      assert null != unionTypeContext;
      return parse( unionTypeContext, extendedAttributes, nullable );
    }
  }

  @Nonnull
  private static Type parse( @Nonnull final WebIDLParser.UnionTypeContext ctx,
                             @Nonnull final List<ExtendedAttribute> extendedAttributes,
                             final boolean nullable )
  {
    final List<Type> memberTypes = new ArrayList<>();
    collectUnionMemberType( ctx.unionMemberType( 0 ), memberTypes );
    collectUnionMemberType( ctx.unionMemberType( 1 ), memberTypes );
    WebIDLParser.UnionMemberTypesContext unionMemberTypesContext = ctx.unionMemberTypes();
    while ( null != unionMemberTypesContext.unionMemberType() )
    {
      collectUnionMemberType( unionMemberTypesContext.unionMemberType(), memberTypes );
      unionMemberTypesContext = unionMemberTypesContext.unionMemberTypes();
    }
    return new UnionType( Collections.unmodifiableList( memberTypes ), extendedAttributes, nullable );
  }

  private static void collectUnionMemberType( @Nonnull final WebIDLParser.UnionMemberTypeContext ctx,
                                              @Nonnull final List<Type> memberTypes )
  {
    final WebIDLParser.ExtendedAttributeListContext extendedAttributeListContext = ctx.extendedAttributeList();
    if ( null != extendedAttributeListContext )
    {
      final WebIDLParser.DistinguishableTypeContext distinguishableTypeContext = ctx.distinguishableType();
      assert null != distinguishableTypeContext;
      memberTypes.add( parse( distinguishableTypeContext, parse( extendedAttributeListContext ) ) );
    }
    else
    {
      final boolean nullable = 1 == ctx.nullModifier().getChildCount();
      final WebIDLParser.UnionTypeContext unionTypeContext = ctx.unionType();
      assert null != unionTypeContext;
      memberTypes.add( parse( unionTypeContext, Collections.emptyList(), nullable ) );
    }
  }

  @Nonnull
  private static Type parse( @Nonnull final WebIDLParser.SingleTypeContext ctx,
                             @Nonnull final List<ExtendedAttribute> extendedAttributes )
  {
    final WebIDLParser.DistinguishableTypeContext distinguishableTypeContext = ctx.distinguishableType();
    if ( null != distinguishableTypeContext )
    {
      return parse( distinguishableTypeContext, extendedAttributes );
    }
    else if ( ctx.getChild( 0 ) instanceof TerminalNode )
    {
      assert ctx.getChild( 0 ).getText().equals( "any" );
      return new Type( Kind.Any, extendedAttributes, false );
    }
    else
    {
      final WebIDLParser.PromiseTypeContext promiseTypeContext = ctx.promiseType();
      assert null != promiseTypeContext;
      return parse( promiseTypeContext, extendedAttributes );
    }
  }

  @Nonnull
  private static Type parse( @Nonnull final WebIDLParser.PromiseTypeContext ctx,
                             @Nonnull final List<ExtendedAttribute> extendedAttributes )
  {
    return new PromiseType( parse( ctx.returnType() ), extendedAttributes );
  }

  @Nonnull
  static Type parse( @Nonnull final WebIDLParser.ReturnTypeContext returnTypeContext )
  {
    final WebIDLParser.TypeContext type = returnTypeContext.type();
    return null != type ? parse( type ) : new Type( Kind.Void, Collections.emptyList(), false );
  }

  @Nonnull
  private static Type parse( @Nonnull final WebIDLParser.DistinguishableTypeContext ctx,
                             @Nonnull final List<ExtendedAttribute> extendedAttributes )
  {
    final boolean nullable = 1 == ctx.nullModifier().getChildCount();

    final WebIDLParser.PrimitiveTypeContext primitiveTypeContext = ctx.primitiveType();
    if ( null != primitiveTypeContext )
    {
      return parse( primitiveTypeContext, extendedAttributes, nullable );
    }
    final WebIDLParser.StringTypeContext stringTypeContext = ctx.stringType();
    if ( null != stringTypeContext )
    {
      return parse( stringTypeContext, extendedAttributes, nullable );
    }
    final TerminalNode identifier = ctx.IDENTIFIER();
    if ( null != identifier )
    {
      return new TypeReference( identifier.getText(), extendedAttributes, nullable );
    }
    final ParseTree child1 = ctx.getChild( 0 );
    if ( child1 instanceof TerminalNode )
    {
      final String text = child1.getText();
      if ( "sequence".equals( text ) )
      {
        return new SequenceType( parse( ctx.typeWithExtendedAttributes() ), extendedAttributes, nullable );
      }
      else if ( "object".equals( text ) )
      {
        return new Type( Kind.Object, extendedAttributes, nullable );
      }
      else if ( "symbol".equals( text ) )
      {
        return new Type( Kind.Symbol, extendedAttributes, nullable );
      }
      else
      {
        assert "FrozenArray".equals( text );
        return new FrozenArrayType( parse( ctx.typeWithExtendedAttributes() ), extendedAttributes, nullable );
      }
    }
    final WebIDLParser.BufferRelatedTypeContext bufferRelatedTypeContext = ctx.bufferRelatedType();
    if ( null != bufferRelatedTypeContext )
    {
      return parse( bufferRelatedTypeContext, extendedAttributes, nullable );
    }
    final WebIDLParser.RecordTypeContext recordTypeContext = ctx.recordType();
    assert null != recordTypeContext;
    return parse( recordTypeContext, extendedAttributes, nullable );
  }

  @Nonnull
  private static Type parse( @Nonnull final WebIDLParser.RecordTypeContext ctx,
                             @Nonnull final List<ExtendedAttribute> extendedAttributes,
                             final boolean nullable )
  {
    final Type keyType = parse( ctx.stringType(), Collections.emptyList(), false );
    final Type valueType = parse( ctx.typeWithExtendedAttributes() );
    return new RecordType( keyType, valueType, extendedAttributes, nullable );
  }

  @Nonnull
  private static Type parse( @Nonnull final WebIDLParser.BufferRelatedTypeContext ctx,
                             @Nonnull final List<ExtendedAttribute> extendedAttributes,
                             final boolean nullable )
  {
    final TerminalNode child = (TerminalNode) ctx.getChild( 0 );
    final String literalName = child.getText();
    assert null != literalName;
    final Kind kind = BUFFER_KIND_MAP.get( literalName );
    assert null != kind;
    return new Type( kind, extendedAttributes, nullable );
  }

  @Nonnull
  private static Type parse( @Nonnull final WebIDLParser.PrimitiveTypeContext ctx,
                             @Nonnull final List<ExtendedAttribute> extendedAttributes,
                             final boolean nullable )
  {
    final WebIDLParser.UnsignedIntegerTypeContext unsignedIntegerType = ctx.unsignedIntegerType();
    if ( null != unsignedIntegerType )
    {
      final ParseTree first = unsignedIntegerType.getChild( 0 );
      final boolean unsigned = first instanceof TerminalNode;
      assert !unsigned || first.getText().equals( "unsigned" );
      final WebIDLParser.IntegerTypeContext integerTypeContext = unsignedIntegerType.integerType();
      if ( integerTypeContext.getChild( 0 ).getText().equals( "long" ) )
      {
        final WebIDLParser.OptionalLongContext optionalLongContext = integerTypeContext.optionalLong();
        if ( 0 == optionalLongContext.getChildCount() )
        {
          return new Type( unsigned ? Kind.UnsignedLong : Kind.Long, extendedAttributes, nullable );
        }
        else
        {
          assert optionalLongContext.getChild( 0 ).getText().equals( "long" );
          return new Type( unsigned ? Kind.UnsignedLongLong : Kind.LongLong, extendedAttributes, nullable );
        }
      }
      else
      {
        assert integerTypeContext.getChild( 0 ).getText().equals( "short" );
        return new Type( unsigned ? Kind.UnsignedShort : Kind.Short, extendedAttributes, nullable );
      }
    }
    final WebIDLParser.UnrestrictedFloatTypeContext unrestrictedFloatType = ctx.unrestrictedFloatType();
    if ( null != unrestrictedFloatType )
    {
      final ParseTree first = unrestrictedFloatType.getChild( 0 );
      final boolean unrestricted = first instanceof TerminalNode;
      assert !unrestricted || first.getText().equals( "unrestricted" );

      if ( unrestrictedFloatType.floatType().getChild( 0 ).getText().equals( "float" ) )
      {
        return new Type( unrestricted ? Kind.UnrestrictedFloat : Kind.Float, extendedAttributes, nullable );
      }
      else
      {
        assert unrestrictedFloatType.floatType().getChild( 0 ).getText().equals( "double" );
        return new Type( unrestricted ? Kind.UnrestrictedDouble : Kind.Double, extendedAttributes, nullable );
      }
    }

    final TerminalNode child = (TerminalNode) ctx.getChild( 0 );
    final String literalName = child.getText();
    if ( "boolean".equals( literalName ) )
    {
      return new Type( Kind.Boolean, extendedAttributes, nullable );
    }
    else if ( "byte".equals( literalName ) )
    {
      return new Type( Kind.Byte, extendedAttributes, nullable );
    }
    else
    {
      assert "octet".equals( literalName );
      return new Type( Kind.Octet, extendedAttributes, nullable );
    }
  }

  @Nonnull
  private static Type parse( @Nonnull final WebIDLParser.StringTypeContext ctx,
                             @Nonnull final List<ExtendedAttribute> extendedAttributes,
                             final boolean nullable )
  {
    final TerminalNode child = (TerminalNode) ctx.getChild( 0 );
    final String literalName = child.getText();
    assert null != literalName;
    final Kind kind = STRING_KIND_MAP.get( literalName );
    assert null != kind;
    return new Type( kind, extendedAttributes, nullable );
  }

  @Nonnull
  static Definition parse( @Nonnull final WebIDLParser.NamespaceContext ctx,
                           final boolean partial,
                           @Nonnull final List<ExtendedAttribute> extendedAttributes )
  {
    final String name = ctx.IDENTIFIER().getText();

    final List<OperationMember> operations = new ArrayList<>();
    final List<AttributeMember> attributes = new ArrayList<>();

    WebIDLParser.NamespaceMembersContext namespaceMembersContext = ctx.namespaceMembers();
    WebIDLParser.NamespaceMemberContext namespaceMemberContext;
    while ( null != ( namespaceMemberContext = namespaceMembersContext.namespaceMember() ) )
    {
      final List<ExtendedAttribute> memberExtendedAttributes = parse( namespaceMembersContext.extendedAttributeList() );
      final WebIDLParser.RegularOperationContext regularOperationContext = namespaceMemberContext.regularOperation();
      if ( null != regularOperationContext )
      {
        operations.add( parse( regularOperationContext, OperationMember.Kind.DEFAULT, memberExtendedAttributes ) );
      }
      else
      {
        final WebIDLParser.AttributeRestContext attributeRestContext = namespaceMemberContext.attributeRest();
        final Set<AttributeMember.Modifier> modifiers = new HashSet<>();
        modifiers.add( AttributeMember.Modifier.READ_ONLY );
        attributes.add( parse( attributeRestContext, modifiers, memberExtendedAttributes ) );
      }
      namespaceMembersContext = namespaceMembersContext.namespaceMembers();
    }
    return
      partial ?
      new PartialNamespaceDefinition( name,
                                      Collections.unmodifiableList( operations ),
                                      Collections.unmodifiableList( attributes ),
                                      extendedAttributes ) :
      new NamespaceDefinition( name,
                               Collections.unmodifiableList( operations ),
                               Collections.unmodifiableList( attributes ),
                               extendedAttributes );
  }

  @Nonnull
  static IncludesStatement parse( @Nonnull final WebIDLParser.IncludesStatementContext ctx,
                                  @Nonnull final List<ExtendedAttribute> extendedAttributes )
  {
    final String interfaceName = ctx.IDENTIFIER( 0 ).getText();
    final String mixinName = ctx.IDENTIFIER( 1 ).getText();
    return new IncludesStatement( interfaceName, mixinName, extendedAttributes );
  }

  @Nonnull
  static DictionaryDefinition parse( @Nonnull final WebIDLParser.DictionaryContext ctx,
                                     @Nonnull final List<ExtendedAttribute> extendedAttributes )
  {
    final String name = ctx.IDENTIFIER().getText();
    final String inherits = extractInherits( ctx.inheritance() );
    final List<DictionaryMember> members = parse( ctx.dictionaryMembers() );
    return new DictionaryDefinition( name, inherits, members, extendedAttributes );
  }

  @Nonnull
  static PartialDictionaryDefinition parse( @Nonnull final WebIDLParser.PartialDictionaryContext ctx,
                                            @Nonnull final List<ExtendedAttribute> extendedAttributes )
  {
    final String name = ctx.IDENTIFIER().getText();
    final List<DictionaryMember> members = parse( ctx.dictionaryMembers() );
    return new PartialDictionaryDefinition( name, members, extendedAttributes );
  }

  @Nonnull
  static DictionaryMember parse( @Nonnull final WebIDLParser.DictionaryMemberContext ctx )
  {
    final List<ExtendedAttribute> extendedAttributes = parse( ctx.extendedAttributeList() );
    final WebIDLParser.DictionaryMemberRestContext restContext = ctx.dictionaryMemberRest();
    final String name = restContext.IDENTIFIER().getText();
    final WebIDLParser.TypeContext typeContext = restContext.type();
    final Type type;
    final boolean optional;
    final DefaultValue defaultValue;
    if ( null != typeContext )
    {
      type = parse( typeContext );
      optional = true;
      final WebIDLParser.DefaultValueContext defaultValueContext = restContext.defaultAssignment().defaultValue();
      defaultValue = null != defaultValueContext ? parse( defaultValueContext ) : null;
    }
    else
    {
      type = parse( restContext.typeWithExtendedAttributes() );
      optional = false;
      defaultValue = null;
    }
    return new DictionaryMember( name, type, optional, defaultValue, extendedAttributes );
  }

  @Nonnull
  static List<DictionaryMember> parse( @Nonnull final WebIDLParser.DictionaryMembersContext ctx )
  {
    WebIDLParser.DictionaryMembersContext members = ctx;
    WebIDLParser.DictionaryMemberContext memberContext;
    final List<DictionaryMember> results = new ArrayList<>();
    while ( null != ( memberContext = members.dictionaryMember() ) )
    {
      results.add( parse( memberContext ) );
      members = members.dictionaryMembers();
    }
    return Collections.unmodifiableList( results );
  }

  @Nonnull
  static WebIDLParser createParser( @Nonnull final Reader reader )
    throws IOException
  {
    final WebIDLParser parser = WebIDLParserTool.createParser( reader );
    parser.setBuildParseTree( true );
    return parser;
  }

  @Nonnull
  public static WebIDLSchema parse( @Nonnull final FileReader reader,
                                    @Nonnull final ANTLRErrorListener errorListener )
    throws IOException
  {
    final WebIDLParser parser = createParser( reader );
    parser.addErrorListener( errorListener );
    return parse( parser.webIDL() );
  }
}
