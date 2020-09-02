package org.realityforge.webtack.jsinterop;

import java.nio.file.Paths;
import javax.annotation.Nonnull;
import org.realityforge.webtack.model.tools.spi.Action;
import org.realityforge.webtack.model.tools.spi.ActionFactory;
import org.realityforge.webtack.model.tools.spi.Name;

@Name( "Jsinterop" )
public final class JsinteropActionFactory
  implements ActionFactory
{
  public String outputDirectory;
  public String packageName;
  public String globalInterface;
  public boolean generateGwtModule = true;
  public boolean enableMagicConstants = true;

  @Nonnull
  @Override
  public Action create()
  {
    if ( null == outputDirectory )
    {
      throw new IllegalArgumentException( "Jsinterop missing required outputDirectory configuration value" );
    }
    if ( null == packageName )
    {
      throw new IllegalArgumentException( "Jsinterop missing required packageName configuration value" );
    }
    return new JsinteropAction( Paths.get( outputDirectory ), packageName, globalInterface, generateGwtModule,enableMagicConstants );
  }
}
