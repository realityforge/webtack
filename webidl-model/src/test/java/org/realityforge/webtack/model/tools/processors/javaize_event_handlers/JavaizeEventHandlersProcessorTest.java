package org.realityforge.webtack.model.tools.processors.javaize_event_handlers;

import javax.annotation.Nonnull;
import javax.json.Json;
import org.realityforge.webtack.model.tools.processors.AbstractProcessorTest;
import org.realityforge.webtack.model.tools.spi.Processor;
import org.realityforge.webtack.model.tools.spi.Registry;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class JavaizeEventHandlersProcessorTest
  extends AbstractProcessorTest
{
  @Test
  public void registry()
  {
    assertTrue( Registry.isProcessorPresent( "JavaizeEventHandlers" ) );
    assertNotNull( createProcessor( "basic" ) );
  }

  @Test
  public void basic()
    throws Exception
  {
    performStandardFixtureTest( "basic", () -> createProcessor( "basic" ) );
  }

  @SuppressWarnings( "SameParameterValue" )
  @Nonnull
  private Processor createProcessor( @Nonnull final String subDirectory )
  {
    return Registry.createProcessor( newPipelineContext( subDirectory ),
                                     "JavaizeEventHandlers",
                                     Json.createObjectBuilder().build() );
  }
}
