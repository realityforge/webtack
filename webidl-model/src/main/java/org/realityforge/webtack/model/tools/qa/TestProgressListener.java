package org.realityforge.webtack.model.tools.qa;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.realityforge.webtack.model.WebIDLSchema;
import org.realityforge.webtack.model.tools.pipeline.ProgressListener;
import org.realityforge.webtack.model.tools.pipeline.config.PipelineConfig;
import org.realityforge.webtack.model.tools.pipeline.config.StageConfig;
import org.realityforge.webtack.model.tools.repository.config.SourceConfig;

public final class TestProgressListener
  implements ProgressListener
{
  @Nonnull
  private final List<String> _trace = new ArrayList<>();

  @Override
  public void onSourcesFiltered( @Nonnull final PipelineConfig pipeline, @Nonnull final List<SourceConfig> sources )
  {
    _trace.add( "onSourcesFiltered(" + pipeline.getName() + "," +
                sources.stream().map( SourceConfig::getName ).collect( Collectors.toList() ) +
                ")" );
  }

  @Override
  public void onSourceParsed( @Nonnull final PipelineConfig pipeline,
                              @Nonnull final SourceConfig source,
                              @Nonnull final WebIDLSchema schema )
  {
    _trace.add( "onSourceParsed(" + pipeline.getName() + "," + source.getName() + ")" );
  }

  @Override
  public void beforeStage( @Nonnull final PipelineConfig pipeline,
                           @Nonnull final StageConfig stage,
                           @Nonnull final List<WebIDLSchema> schemas )
  {
    _trace.add( "beforeStage(" + pipeline.getName() + "," + stage.getName() + "), schemaCount=" + schemas.size() );
  }

  @Override
  public void stageDebug( @Nonnull final PipelineConfig pipeline,
                          @Nonnull final StageConfig stage,
                          @Nonnull final String message )
  {
    _trace.add( "stageDebug(" + pipeline.getName() + "," + stage.getName() + "): DEBUG: " + message );
  }

  @Override
  public void stageInfo( @Nonnull final PipelineConfig pipeline,
                         @Nonnull final StageConfig stage,
                         @Nonnull final String message )
  {
    _trace.add( "stageInfo(" + pipeline.getName() + "," + stage.getName() + "): INFO: " + message );
  }

  @Override
  public void stageError( @Nonnull final PipelineConfig pipeline,
                          @Nonnull final StageConfig stage,
                          @Nonnull final String message )
  {
    _trace.add( "stageError(" + pipeline.getName() + "," + stage.getName() + "): ERROR: " + message );
  }

  @Override
  public void afterStage( @Nonnull final PipelineConfig pipeline,
                          @Nonnull final StageConfig stage,
                          @Nonnull final List<WebIDLSchema> schemas )
  {
    _trace.add( "afterStage(" + pipeline.getName() + "," + stage.getName() + "), schemaCount=" + schemas.size() );
  }

  @Nonnull
  public List<String> getTrace()
  {
    return _trace;
  }

  public void assertContains( @Nonnull final String line )
  {
    if ( !_trace.contains( line ) )
    {
      throw new AssertionError( "Expected trace to contain line:\n\n" + line +
                                "\n\nbut trace consisted of:\n\n" + String.join( "\n", _trace ) + "\n" );
    }
  }
}
