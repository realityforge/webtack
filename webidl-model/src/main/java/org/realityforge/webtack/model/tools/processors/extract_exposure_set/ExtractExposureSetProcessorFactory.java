package org.realityforge.webtack.model.tools.processors.extract_exposure_set;

import javax.annotation.Nonnull;
import org.realityforge.webtack.model.tools.processors.AbstractProcessorFactory;
import org.realityforge.webtack.model.tools.spi.Name;
import org.realityforge.webtack.model.tools.spi.PipelineContext;
import org.realityforge.webtack.model.tools.spi.Processor;

@Name( "ExtractExposureSet" )
public final class ExtractExposureSetProcessorFactory
  extends AbstractProcessorFactory
{
  public String globalInterface;

  @Nonnull
  @Override
  public Processor create( @Nonnull final PipelineContext context )
  {
    return new ExtractExposureSetProcessor( context, requireNonNull( "globalInterface", globalInterface ) );
  }
}
