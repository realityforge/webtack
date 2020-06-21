package org.realityforge.webtack.jsinterop;

import javax.annotation.Nonnull;
import org.realityforge.webtack.model.CallbackInterfaceDefinition;
import org.realityforge.webtack.model.WebIDLSchema;
import org.testng.annotations.Test;

public final class CallbackInterfaceGeneratorTest
  extends AbstractTest
{
  @Test
  public void generate()
    throws Exception
  {
    final String content =
      "interface Event {\n" +
      "  constructor();\n" +
      "};\n" +
      "\n" +
      "callback interface EventListener {\n" +
      "  void handleEvent( Event event );\n" +
      "};\n";
    final WebIDLSchema schema = loadSchema( content );
    final CallbackInterfaceDefinition definition = schema.findCallbackInterfaceByName( "EventListener" );
    assert null != definition;

    generateCallbackCode( schema, definition );
  }

  @Test
  public void generate_callback_returnsValue()
    throws Exception
  {
    // Example returns nullable value
    final String content =
      "callback interface XPathNSResolver {\n" +
      "  DOMString? lookupNamespaceURI( DOMString? prefix );\n" +
      "};\n";
    final WebIDLSchema schema = loadSchema( content );
    final CallbackInterfaceDefinition definition = schema.findCallbackInterfaceByName( "XPathNSResolver" );
    assert null != definition;
    generateCallbackCode( schema, definition );
  }

  @Test
  public void generate_exposed_and_contains_constants()
    throws Exception
  {
    // Example returns nullable value
    final String content =
      "interface Node {\n" +
      "  constructor();\n" +
      "};\n" +
      "[Exposed=Window]\n" +
      "callback interface NodeFilter {\n" +
      "  const unsigned short FILTER_ACCEPT = 1;\n" +
      "  const unsigned short FILTER_REJECT = 2;\n" +
      "  const unsigned short FILTER_SKIP = 3;\n" +
      "  const unsigned long SHOW_ALL = 0xFFFFFFFF;\n" +
      "  const unsigned long SHOW_ATTRIBUTE = 0x2;\n" +
      "  const unsigned long SHOW_CDATA_SECTION = 0x8;\n" +
      "  const unsigned long SHOW_COMMENT = 0x80;\n" +
      "  const unsigned long SHOW_DOCUMENT = 0x100;\n" +
      "  const unsigned long SHOW_DOCUMENT_FRAGMENT = 0x400;\n" +
      "  const unsigned long SHOW_DOCUMENT_TYPE = 0x200;\n" +
      "  const unsigned long SHOW_ELEMENT = 0x1;\n" +
      "  const unsigned long SHOW_ENTITY = 0x20;\n" +
      "  const unsigned long SHOW_ENTITY_REFERENCE = 0x10;\n" +
      "  const unsigned long SHOW_NOTATION = 0x800;\n" +
      "  const unsigned long SHOW_PROCESSING_INSTRUCTION = 0x40;\n" +
      "  const unsigned long SHOW_TEXT = 0x4;\n" +
      "  unsigned short acceptNode( Node node );\n" +
      "};\n";
    final WebIDLSchema schema = loadSchema( content );
    final CallbackInterfaceDefinition definition = schema.findCallbackInterfaceByName( "NodeFilter" );
    assert null != definition;
    generateCallbackCode( schema, definition );
  }

  private void generateCallbackCode( @Nonnull final WebIDLSchema schema,
                                     @Nonnull final CallbackInterfaceDefinition definition )
    throws Exception
  {
    //TODO: We should generate the java code for above and compile java code to ensure it is valid
    final CallbackInterfaceGenerator generator = new CallbackInterfaceGenerator();
    generator.generate( newContext( schema ), definition );
    assertFileMatchesFixture( javaFile( definition.getName() ), javaFixtureFile( definition.getName() ) );
  }
}
