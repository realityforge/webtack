package org.realityforge.webtack.model;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class PartialMixinDefinitionTest
  extends AbstractTest
{
  @Test
  public void parse_constant()
    throws IOException
  {
    final PartialMixinDefinition mixin =
      ensurePartialMixinDefinition( "partial interface mixin WebGL2RenderingContextBase\n" +
                                    "{\n" +
                                    "  const GLenum READ_BUFFER = 0x0C02;\n" +
                                    "};\n",
                                    "WebGL2RenderingContextBase",
                                    1,
                                    0,
                                    0 );
    final List<ConstMember> constants = mixin.getConstants();
    final ConstMember constant = constants.get( 0 );
    assertEquals( constant.getName(), "READ_BUFFER" );
    assertEquals( constant.getType().getKind(), Kind.TypeReference );
    assertEquals( ( (TypeReference) constant.getType() ).getName(), "GLenum" );
    assertEquals( constant.getValue().getValue(), "0x0C02" );
  }

  @Test
  public void parse_attribute()
    throws IOException
  {
    final PartialMixinDefinition mixin =
      ensurePartialMixinDefinition( "partial interface mixin NavigatorAutomationInformation {\n" +
                                    "    readonly attribute boolean webdriver;\n" +
                                    "};\n",
                                    "NavigatorAutomationInformation",
                                    0,
                                    1,
                                    0 );
    final List<AttributeMember> attributes = mixin.getAttributes();
    final AttributeMember attribute = attributes.get( 0 );
    assertEquals( attribute.getName(), "webdriver" );
    assertEquals( attribute.getType().getKind(), Kind.Boolean );
    assertTrue( attribute.getModifiers().contains( AttributeMember.Modifier.READ_ONLY ) );
  }

  @Test
  public void parse_stringifier_attribute()
    throws IOException
  {
    final PartialMixinDefinition mixin =
      ensurePartialMixinDefinition( "partial interface mixin HTMLHyperlinkElementUtils {\n" +
                                    "  [CEReactions] stringifier attribute USVString href;\n" +
                                    "};\n",
                                    "HTMLHyperlinkElementUtils",
                                    0,
                                    1,
                                    0 );
    final List<AttributeMember> attributes = mixin.getAttributes();
    final AttributeMember attribute = attributes.get( 0 );
    assertTrue( attribute.getExtendedAttributes().stream().anyMatch( a -> "CEReactions".equals( a.getName() ) ) );
    assertEquals( attribute.getName(), "href" );
    assertEquals( attribute.getType().getKind(), Kind.USVString );
    final Set<AttributeMember.Modifier> modifiers = attribute.getModifiers();
    assertEquals( modifiers.size(), 1 );
    assertTrue( modifiers.contains( AttributeMember.Modifier.STRINGIFIER ) );
  }

  @Test
  public void parse_operation()
    throws IOException
  {
    final PartialMixinDefinition mixin =
      ensurePartialMixinDefinition( "partial interface mixin CanvasUserInterface {\n" +
                                    "  void scrollPathIntoView();\n" +
                                    "  void scrollPathIntoView(Path2D path);\n" +
                                    "};\n",
                                    "CanvasUserInterface",
                                    0,
                                    0,
                                    2 );
    final List<OperationMember> operations = mixin.getOperations();
    {
      final OperationMember operation = operations.get( 0 );
      assertEquals( operation.getName(), "scrollPathIntoView" );
      assertEquals( operation.getReturnType().getKind(), Kind.Void );
      assertEquals( operation.getArguments().size(), 0 );
    }

    {
      final OperationMember operation = operations.get( 1 );
      assertEquals( operation.getName(), "scrollPathIntoView" );
      assertEquals( operation.getReturnType().getKind(), Kind.Void );
      final List<Argument> arguments = operation.getArguments();
      assertEquals( arguments.size(), 1 );
      final Argument argument1 = arguments.get( 0 );
      assertEquals( argument1.getName(), "path" );
      final Type argument1Type = argument1.getType();
      assertEquals( argument1Type.getKind(), Kind.TypeReference );
      assertEquals( ( (TypeReference) argument1Type ).getName(), "Path2D" );
    }
  }

  @Test
  public void parse_stringifier_operation()
    throws IOException
  {
    final PartialMixinDefinition mixin =
      ensurePartialMixinDefinition( "partial interface mixin CanvasUserInterface {\n" +
                                    "  stringifier;\n" +
                                    "};\n",
                                    "CanvasUserInterface",
                                    0,
                                    0,
                                    1 );
    final List<OperationMember> operations = mixin.getOperations();
    final OperationMember operation = operations.get( 0 );
    assertNull( operation.getName() );
    assertEquals( operation.getReturnType().getKind(), Kind.DOMString );
    assertEquals( operation.getArguments().size(), 0 );
    assertEquals( operation.getKind(), OperationMember.Kind.STRINGIFIER );
  }

  @Nonnull
  private PartialMixinDefinition ensurePartialMixinDefinition( @Nonnull final String webIDL,
                                                               @Nonnull final String name,
                                                               final int constantCount,
                                                               final int attributeCount,
                                                               final int operationCount )
    throws IOException
  {
    final Definition definition =
      WebIDLModelParser.parse( createParser( webIDL ).definition(), Collections.emptyList() );
    assertTrue( definition instanceof PartialMixinDefinition );
    final PartialMixinDefinition mixin = (PartialMixinDefinition) definition;
    assertEquals( mixin.getName(), name );
    assertEquals( mixin.getConstants().size(), constantCount );
    assertEquals( mixin.getAttributes().size(), attributeCount );
    assertEquals( mixin.getOperations().size(), operationCount );
    return mixin;
  }
}
