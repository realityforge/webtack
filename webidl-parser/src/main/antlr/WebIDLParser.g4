// This grammar is based on grammar in Web IDL Editor’s Draft, 13 March 2020.

// changes from the spec:
// - Added `webIDL` wrapper type to simplify parsing a single unit.
// - Renamed `enum` to `enumDefinition` to avoid collision with the java keyword when generating java code.
// - Renamed `const` to `constMember` to avoid collision with the java keyword when generating java code.
// - Renamed `null` to `nullModifier` to avoid collision with the java keyword when generating java code.
// - Renamed `default` to `defaultAssignment` to avoid collision with the java keyword when generating java code.
// - Redefined "extendedAttribute" as the small subset of extended attribute forms actually used by the specs that
//   are defined by ExtendedAttributeNoArgs, ExtendedAttributeArgList, ExtendedAttributeIdent,
//   ExtendedAttributeIdentList, ExtendedAttributeNamedArgList in the original spec. This change also meant that
//   unused rules such as "other", "extendedAttributeRest", "extendedAttributeInner" and "otherOrComma" could be removed
// - We have also added a JAVADOC comment parsing in a separate lexer island that contains documentation for the webidl
//   element in a javadoc-esque format. This is only allowed in specific places in the grammar which is NOT spec compliant
//   but as we are not using this as a general parser, this should not be an issue.
parser grammar WebIDLParser;

options { tokenVocab=WebIDLLexer; }

webIDL
	: definitions EOF
;

definitions
	: documentation extendedAttributeList definition definitions
	| /* empty */
;

definition
	: callbackOrInterfaceOrMixin
	| namespace
	| partial
	| dictionary
	| enumDefinition
	| typedef
	| includesStatement
;

argumentNameKeyword
	: ASYNC
	| ATTRIBUTE
	| EVENT
	| CALLBACK
	| CONST
	| CONSTRUCTOR
	| DELETER
	| DICTIONARY
	| ENUM
	| GETTER
	| INCLUDES
	| INHERIT
	| INTERFACE
	| ITERABLE
	| MAPLIKE
	| MIXIN
	| NAMESPACE
	| PARTIAL
	| READONLY
	| REQUIRED
	| SETLIKE
	| SETTER
	| STATIC
	| STRINGIFIER
	| TYPEDEF
	| UNRESTRICTED
;

callbackOrInterfaceOrMixin
	: CALLBACK callbackRestOrInterface
	| INTERFACE interfaceOrMixin
;

interfaceOrMixin
  : interfaceRest
  | mixinRest
;

interfaceRest
  : IDENTIFIER inheritance OPEN_BRACE interfaceMembers CLOSE_BRACE SEMI_COLON
;

partial
  : PARTIAL partialDefinition
;

partialDefinition
  : INTERFACE partialInterfaceOrPartialMixin
  | partialDictionary
  | namespace
;

partialInterfaceOrPartialMixin
  : partialInterfaceRest
  | mixinRest
;

partialInterfaceRest
  : IDENTIFIER OPEN_BRACE partialInterfaceMembers CLOSE_BRACE SEMI_COLON
;

interfaceMembers
  : documentation extendedAttributeList interfaceMember interfaceMembers
  | /* empty */
;

interfaceMember
  : partialInterfaceMember
  | constructor
;

partialInterfaceMembers
  : documentation extendedAttributeList partialInterfaceMember partialInterfaceMembers
  | /* empty */
;

partialInterfaceMember
  : constMember
  | operation
  | stringifier
  | event
  | staticMember
  | iterable
  | asyncIterable
  | readOnlyMember
  | readWriteAttribute
  | readWriteMaplike
  | readWriteSetlike
;

inheritance
  : COLON IDENTIFIER
  | /* empty */
;

mixinRest
  : MIXIN IDENTIFIER OPEN_BRACE mixinMembers CLOSE_BRACE SEMI_COLON
;

mixinMembers
  : documentation extendedAttributeList mixinMember mixinMembers
  | /* empty */
;

mixinMember
  : constMember
  | regularOperation
  | stringifier
  | readOnly attributeRest
;

includesStatement
  : IDENTIFIER INCLUDES IDENTIFIER SEMI_COLON
;

callbackRestOrInterface
  : callbackRest
  | INTERFACE IDENTIFIER OPEN_BRACE callbackInterfaceMembers CLOSE_BRACE SEMI_COLON
;

callbackInterfaceMembers
  : documentation extendedAttributeList callbackInterfaceMember callbackInterfaceMembers
  | /* empty */
;

callbackInterfaceMember
  : constMember
  |  regularOperation
;

constMember
  : CONST constMemberType IDENTIFIER EQUALS constMemberValue SEMI_COLON
;

constMemberValue
  : booleanLiteral
  | floatLiteral
  | INTEGER
;

booleanLiteral
  : TRUE
  | FALSE
;

floatLiteral
  : DECIMAL
  | NEGATIVE_INFINITY
  | POSITIVE_INFINITY
  | NAN
;

constMemberType
  : primitiveType
  | IDENTIFIER
;

readOnlyMember
  : READONLY readOnlyMemberRest
;

readOnlyMemberRest
  : attributeRest
  | maplikeRest
  | setlikeRest
;

readWriteAttribute
  : INHERIT attributeRest
  | attributeRest
;

attributeRest
  : ATTRIBUTE typeWithExtendedAttributes attributeName SEMI_COLON
;

attributeName
  : attributeNameKeyword
  | IDENTIFIER
;

attributeNameKeyword
  : ASYNC
  | EVENT
  | REQUIRED
;

readOnly
  : READONLY
  | /* empty */
;

defaultValue
  : constMemberValue
  | STRING
  | OPEN_SQUARE_BRACKET CLOSE_SQUARE_BRACKET
  | OPEN_BRACE CLOSE_BRACE
  | NULL
;

operation
  : regularOperation
  | specialOperation
;

regularOperation
  : returnType operationRest
;

specialOperation
  : special regularOperation
;

special
  : GETTER
  | SETTER
  | DELETER
;

operationRest
  : optionalOperationName OPEN_BRACKET argumentList CLOSE_BRACKET SEMI_COLON
;

optionalOperationName
  : operationName
  | /* empty */
;

operationName
  : operationNameKeyword
  | IDENTIFIER
;

operationNameKeyword
  : INCLUDES
;

argumentList
  : argument arguments
  | /* empty */
;

arguments
  : COMMA argument arguments
  | /* empty */
;

argument
  : extendedAttributeList argumentRest
;

argumentRest
  : OPTIONAL typeWithExtendedAttributes argumentName defaultAssignment
  | type ellipsis argumentName
;

argumentName
  : argumentNameKeyword
  | IDENTIFIER
;

ellipsis
  : ELLIPSIS
  | /* empty */
;

returnType
  : type
  | VOID
;

constructor
  : CONSTRUCTOR OPEN_BRACKET argumentList CLOSE_BRACKET SEMI_COLON
;

event
  : EVENT IDENTIFIER IDENTIFIER SEMI_COLON
;

stringifier
  : STRINGIFIER stringifierRest
;

stringifierRest
  : readOnly attributeRest
  | regularOperation
  | SEMI_COLON
;

staticMember
  : STATIC staticMemberRest
;

staticMemberRest
  : readOnly attributeRest
  | regularOperation
;

iterable
  : ITERABLE OPEN_ANGLE_BRACKET typeWithExtendedAttributes optionalType CLOSE_ANGLE_BRACKET SEMI_COLON
;

optionalType
  : COMMA typeWithExtendedAttributes
  | /* empty */
;

asyncIterable
  : ASYNC ITERABLE OPEN_ANGLE_BRACKET typeWithExtendedAttributes COMMA typeWithExtendedAttributes CLOSE_ANGLE_BRACKET SEMI_COLON
;

readWriteMaplike
  : maplikeRest
;

maplikeRest
  : MAPLIKE OPEN_ANGLE_BRACKET typeWithExtendedAttributes COMMA typeWithExtendedAttributes CLOSE_ANGLE_BRACKET SEMI_COLON
;

readWriteSetlike
  : setlikeRest
;

setlikeRest
  : SETLIKE OPEN_ANGLE_BRACKET typeWithExtendedAttributes CLOSE_ANGLE_BRACKET SEMI_COLON
;

namespace
  : NAMESPACE IDENTIFIER OPEN_BRACE namespaceMembers CLOSE_BRACE SEMI_COLON
;

namespaceMembers
  : documentation extendedAttributeList namespaceMember namespaceMembers
  | /* empty */
;

namespaceMember
  : regularOperation
  | READONLY attributeRest
;

dictionary
  : DICTIONARY IDENTIFIER inheritance OPEN_BRACE dictionaryMembers CLOSE_BRACE SEMI_COLON
;

dictionaryMembers
  : documentation dictionaryMember dictionaryMembers
  | /* empty */
;

dictionaryMember
  : extendedAttributeList dictionaryMemberRest
;

dictionaryMemberRest
  : REQUIRED typeWithExtendedAttributes IDENTIFIER SEMI_COLON
  | type IDENTIFIER defaultAssignment SEMI_COLON
;

partialDictionary
  : DICTIONARY IDENTIFIER OPEN_BRACE dictionaryMembers CLOSE_BRACE SEMI_COLON
;

defaultAssignment
  : EQUALS defaultValue
  | /* empty */
;

enumDefinition
  : ENUM IDENTIFIER OPEN_BRACE enumValueList CLOSE_BRACE SEMI_COLON
;

enumValueList
  : documentation STRING enumValueListComma
;

enumValueListComma
  : COMMA enumValueListString
  | /* empty */
;

enumValueListString
  : documentation STRING enumValueListComma
  | /* empty */
;

callbackRest
  : IDENTIFIER EQUALS returnType OPEN_BRACKET argumentList CLOSE_BRACKET SEMI_COLON
;

typedef
  : TYPEDEF typeWithExtendedAttributes IDENTIFIER SEMI_COLON
;

type
  : singleType
  | unionType nullModifier
;

typeWithExtendedAttributes
  : extendedAttributeList type
;

singleType
  : distinguishableType
  | ANY
  | promiseType
;

unionType
  : OPEN_BRACKET unionMemberType OR unionMemberType unionMemberTypes CLOSE_BRACKET
;

unionMemberType
  : extendedAttributeList distinguishableType
  | unionType nullModifier
;

unionMemberTypes
  : OR unionMemberType unionMemberTypes
  | /* empty */
;

distinguishableType
  : primitiveType nullModifier
  | stringType nullModifier
  | IDENTIFIER nullModifier
  | SEQUENCE OPEN_ANGLE_BRACKET typeWithExtendedAttributes CLOSE_ANGLE_BRACKET nullModifier
  | OBJECT nullModifier
  | SYMBOL nullModifier
  | bufferRelatedType nullModifier
  | FROZEN_ARRAY OPEN_ANGLE_BRACKET typeWithExtendedAttributes CLOSE_ANGLE_BRACKET nullModifier
  | recordType nullModifier
;

primitiveType
  : unsignedIntegerType
  | unrestrictedFloatType
  | BOOLEAN
  | BYTE
  | OCTET
;

unrestrictedFloatType
  : UNRESTRICTED floatType
  | floatType
;

floatType
  : FLOAT
  | DOUBLE
;

unsignedIntegerType
  : UNSIGNED integerType
  | integerType
;

integerType
  : SHORT
  | LONG optionalLong
;

optionalLong
  : LONG
  | /* empty */
;

stringType
  : BYTE_STRING
  | DOM_STRING
  | USV_STRING
;

promiseType
  : PROMISE OPEN_ANGLE_BRACKET returnType CLOSE_ANGLE_BRACKET
;

recordType
  : RECORD OPEN_ANGLE_BRACKET stringType COMMA typeWithExtendedAttributes CLOSE_ANGLE_BRACKET
;

nullModifier
  : QUESTION_MARK
  | /* empty */
;

bufferRelatedType
  : ARRAY_BUFFER
  | DATA_VIEW
  | INT8_ARRAY
  | INT16_ARRAY
  | INT32_ARRAY
  | UINT8_ARRAY
  | UINT16_ARRAY
  | UINT32_ARRAY
  | UINT8_CLAMPED_ARRAY
  | FLOAT32_ARRAY
  | FLOAT64_ARRAY
;

extendedAttributeList
  : OPEN_SQUARE_BRACKET extendedAttribute extendedAttributes CLOSE_SQUARE_BRACKET
  | /* empty */
;

extendedAttributes
  : COMMA extendedAttribute extendedAttributes
  | /* empty */
;

extendedAttribute
  : extendedAttributeNoArgs
  | extendedAttributeArgList
  | extendedAttributeIdent
  | extendedAttributeIdentList
  | extendedAttributeNamedArgList
;

identifierList
  : IDENTIFIER identifiers
;

identifiers
  : COMMA IDENTIFIER identifiers
  | /* empty */
;

extendedAttributeNoArgs
  : IDENTIFIER
;

extendedAttributeArgList
  : IDENTIFIER OPEN_BRACKET argumentList CLOSE_BRACKET
;

extendedAttributeIdent
  : IDENTIFIER EQUALS IDENTIFIER
;

extendedAttributeIdentList
  : IDENTIFIER EQUALS OPEN_BRACKET identifierList CLOSE_BRACKET
;

extendedAttributeNamedArgList
  : IDENTIFIER EQUALS IDENTIFIER OPEN_BRACKET argumentList CLOSE_BRACKET
;

/*

The following is the grammar from the spec:

Definitions ::
    ExtendedAttributeList Definition Definitions
    ε

Definition ::
    CallbackOrInterfaceOrMixin
    Namespace
    Partial
    Dictionary
    Enum
    Typedef
    IncludesStatement

ArgumentNameKeyword ::
    async
    attribute
    callback
    const
    constructor
    deleter
    dictionary
    enum
    getter
    includes
    inherit
    interface
    iterable
    maplike
    mixin
    namespace
    partial
    readonly
    required
    setlike
    setter
    static
    stringifier
    typedef
    unrestricted

CallbackOrInterfaceOrMixin ::
    callback CallbackRestOrInterface
    interface InterfaceOrMixin

InterfaceOrMixin ::
    InterfaceRest
    MixinRest

InterfaceRest ::
    identifier Inheritance { InterfaceMembers } ;

Partial ::
    partial PartialDefinition

PartialDefinition ::
    interface PartialInterfaceOrPartialMixin
    PartialDictionary
    Namespace

PartialInterfaceOrPartialMixin ::
    PartialInterfaceRest
    MixinRest

PartialInterfaceRest ::
    identifier { PartialInterfaceMembers } ;

InterfaceMembers ::
    ExtendedAttributeList InterfaceMember InterfaceMembers
    ε

InterfaceMember ::
    PartialInterfaceMember
    Constructor

PartialInterfaceMembers ::
    ExtendedAttributeList PartialInterfaceMember PartialInterfaceMembers
    ε

PartialInterfaceMember ::
    Const
    Operation
    Stringifier
    StaticMember
    Iterable
    AsyncIterable
    ReadOnlyMember
    ReadWriteAttribute
    ReadWriteMaplike
    ReadWriteSetlike

Inheritance ::
    : identifier
    ε

MixinRest ::
    mixin identifier { MixinMembers } ;

MixinMembers ::
    ExtendedAttributeList MixinMember MixinMembers
    ε

MixinMember ::
    Const
    RegularOperation
    Stringifier
    ReadOnly AttributeRest

IncludesStatement ::
    identifier includes identifier ;

CallbackRestOrInterface ::
    CallbackRest
    interface identifier { CallbackInterfaceMembers } ;

CallbackInterfaceMembers ::
    ExtendedAttributeList CallbackInterfaceMember CallbackInterfaceMembers
    ε

CallbackInterfaceMember ::
    Const
    RegularOperation

Const ::
    const ConstType identifier = ConstValue ;

ConstValue ::
    BooleanLiteral
    FloatLiteral
    integer

BooleanLiteral ::
    true
    false

FloatLiteral ::
    decimal
    -Infinity
    Infinity
    NaN

ConstType ::
    PrimitiveType
    identifier

ReadOnlyMember ::
    readonly ReadOnlyMemberRest

ReadOnlyMemberRest ::
    AttributeRest
    MaplikeRest
    SetlikeRest

ReadWriteAttribute ::
    inherit AttributeRest
    AttributeRest

AttributeRest ::
    attribute TypeWithExtendedAttributes AttributeName ;

AttributeName ::
    AttributeNameKeyword
    identifier

AttributeNameKeyword ::
    async
    required

ReadOnly ::
    readonly
    ε

DefaultValue ::
    ConstValue
    string
    [ ]
    { }
    null

Operation ::
    RegularOperation
    SpecialOperation

RegularOperation ::
    ReturnType OperationRest

SpecialOperation ::
    Special RegularOperation

Special ::
    getter
    setter
    deleter

OperationRest ::
    OptionalOperationName ( ArgumentList ) ;

OptionalOperationName ::
    OperationName
    ε

OperationName ::
    OperationNameKeyword
    identifier

OperationNameKeyword ::
    includes

ArgumentList ::
    Argument Arguments
    ε

Arguments ::
    , Argument Arguments
    ε

Argument ::
    ExtendedAttributeList ArgumentRest

ArgumentRest ::
    optional TypeWithExtendedAttributes ArgumentName Default
    Type Ellipsis ArgumentName

ArgumentName ::
    ArgumentNameKeyword
    identifier

Ellipsis ::
    ...
    ε

ReturnType ::
    Type
    void

Constructor ::
    constructor ( ArgumentList ) ;

Stringifier ::
    stringifier StringifierRest

StringifierRest ::
    ReadOnly AttributeRest
    RegularOperation
    ;

StaticMember ::
    static StaticMemberRest

StaticMemberRest ::
    ReadOnly AttributeRest
    RegularOperation

Iterable ::
    iterable < TypeWithExtendedAttributes OptionalType > ;

OptionalType ::
    , TypeWithExtendedAttributes
    ε

AsyncIterable ::
    async iterable < TypeWithExtendedAttributes , TypeWithExtendedAttributes > ;

ReadWriteMaplike ::
    MaplikeRest

MaplikeRest ::
    maplike < TypeWithExtendedAttributes , TypeWithExtendedAttributes > ;

ReadWriteSetlike ::
    SetlikeRest

SetlikeRest ::
    setlike < TypeWithExtendedAttributes > ;

Namespace ::
    namespace identifier { NamespaceMembers } ;

NamespaceMembers ::
    ExtendedAttributeList NamespaceMember NamespaceMembers
    ε

NamespaceMember ::
    RegularOperation
    readonly AttributeRest

Dictionary ::
    dictionary identifier Inheritance { DictionaryMembers } ;

DictionaryMembers ::
    DictionaryMember DictionaryMembers
    ε

DictionaryMember ::
    ExtendedAttributeList DictionaryMemberRest

DictionaryMemberRest ::
    required TypeWithExtendedAttributes identifier ;
    Type identifier Default ;

PartialDictionary ::
    dictionary identifier { DictionaryMembers } ;

Default ::
    = DefaultValue
    ε

Enum ::
    enum identifier { EnumValueList } ;

EnumValueList ::
    string EnumValueListComma

EnumValueListComma ::
    , EnumValueListString
    ε

EnumValueListString ::
    string EnumValueListComma
    ε

CallbackRest ::
    identifier = ReturnType ( ArgumentList ) ;

Typedef ::
    typedef TypeWithExtendedAttributes identifier ;

Type ::
    SingleType
    UnionType Null

TypeWithExtendedAttributes ::
    ExtendedAttributeList Type

SingleType ::
    DistinguishableType
    any
    PromiseType

UnionType ::
    ( UnionMemberType or UnionMemberType UnionMemberTypes )

UnionMemberType ::
    ExtendedAttributeList DistinguishableType
    UnionType Null

UnionMemberTypes ::
    or UnionMemberType UnionMemberTypes
    ε

DistinguishableType ::
    PrimitiveType Null
    StringType Null
    identifier Null
    sequence < TypeWithExtendedAttributes > Null
    object Null
    symbol Null
    BufferRelatedType Null
    FrozenArray < TypeWithExtendedAttributes > Null
    RecordType Null

PrimitiveType ::
    UnsignedIntegerType
    UnrestrictedFloatType
    boolean
    byte
    octet

UnrestrictedFloatType ::
    unrestricted FloatType
    FloatType

FloatType ::
    float
    double

UnsignedIntegerType ::
    unsigned IntegerType
    IntegerType

IntegerType ::
    short
    long OptionalLong

OptionalLong ::
    long
    ε

StringType ::
    ByteString
    DOMString
    USVString

PromiseType ::
    Promise < ReturnType >

RecordType ::
    record < StringType , TypeWithExtendedAttributes >

Null ::
    ?
    ε

BufferRelatedType ::
    ArrayBuffer
    DataView
    Int8Array
    Int16Array
    Int32Array
    Uint8Array
    Uint16Array
    Uint32Array
    Uint8ClampedArray
    Float32Array
    Float64Array

ExtendedAttributeList ::
    [ ExtendedAttribute ExtendedAttributes ]
    ε

ExtendedAttributes ::
    , ExtendedAttribute ExtendedAttributes
    ε

ExtendedAttribute ::
    ( ExtendedAttributeInner ) ExtendedAttributeRest
    [ ExtendedAttributeInner ] ExtendedAttributeRest
    { ExtendedAttributeInner } ExtendedAttributeRest
    Other ExtendedAttributeRest

ExtendedAttributeRest ::
    ExtendedAttribute
    ε

ExtendedAttributeInner ::
    ( ExtendedAttributeInner ) ExtendedAttributeInner
    [ ExtendedAttributeInner ] ExtendedAttributeInner
    { ExtendedAttributeInner } ExtendedAttributeInner
    OtherOrComma ExtendedAttributeInner
    ε

Other ::
    integer
    decimal
    identifier
    string
    other
    -
    -Infinity
    .
    ...
    :
    ;
    <
    =
    >
    ?
    ByteString
    DOMString
    FrozenArray
    Infinity
    NaN
    Promise
    USVString
    any
    boolean
    byte
    double
    false
    float
    long
    null
    object
    octet
    or
    optional
    record
    sequence
    short
    symbol
    true
    unsigned
    void
    ArgumentNameKeyword
    BufferRelatedType

OtherOrComma ::
    Other
    ,

IdentifierList ::
    identifier Identifiers

Identifiers ::
    , identifier Identifiers
    ε

ExtendedAttributeNoArgs ::
    identifier

ExtendedAttributeArgList ::
    identifier ( ArgumentList )

ExtendedAttributeIdent ::
    identifier = identifier

ExtendedAttributeIdentList ::
    identifier = ( IdentifierList )

ExtendedAttributeNamedArgList ::
    identifier = identifier ( ArgumentList )
*/

documentation
	: JAVADOC_START skipWhitespace* documentationContent JAVADOC_END
	| /* empty */
	;

documentationContent
	: description skipWhitespace*
	| skipWhitespace* tagSection
	| description NEWLINE+ skipWhitespace* tagSection
	;

skipWhitespace
	: SPACE
	| NEWLINE
	;

description
	: descriptionLine (descriptionNewline+ descriptionLine)*
	;

descriptionLine
	: descriptionLineStart descriptionLineElement*
	| inlineTag descriptionLineElement*
	;

descriptionLineStart
	: SPACE? descriptionLineNoSpaceNoAt+ (descriptionLineNoSpaceNoAt | SPACE | AT)*
	;

descriptionLineNoSpaceNoAt
	: TEXT_CONTENT
	| NAME
	| STAR
	| SLASH
	| BRACE_OPEN
	| BRACE_CLOSE
	;

descriptionLineElement
	: inlineTag
	| descriptionLineText
	;

descriptionLineText
	: (descriptionLineNoSpaceNoAt | SPACE | AT)+
	;

descriptionNewline
	: NEWLINE
	;

tagSection
	: blockTag+
;

blockTag
	: SPACE? AT NAME SPACE? blockTagContent*
;

blockTagContent
	: blockTagText
	| inlineTag
	| NEWLINE
;

blockTagText
	: blockTagTextElement+
;

blockTagTextElement
	: TEXT_CONTENT
	| NAME
	| SPACE
	| STAR
	| SLASH
	| BRACE_OPEN
	| BRACE_CLOSE
;

inlineTag
	: INLINE_TAG_START inlineTagName SPACE* inlineTagContent? BRACE_CLOSE
;

inlineTagName
	: NAME
;

inlineTagContent
	: braceContent+
;

braceExpression
	: BRACE_OPEN braceContent* BRACE_CLOSE
;

braceContent
	: braceExpression
	| braceText (NEWLINE* braceText)*
;

braceText
	: TEXT_CONTENT
	| NAME
	| SPACE
	| STAR
	| SLASH
	| NEWLINE
;
