package org.realityforge.webtack.jsinterop;

import javax.annotation.Nonnull;

final class NamingUtil
{
  private NamingUtil()
  {
  }

  @Nonnull
  static String lowercaseFirstCharacter( @Nonnull final String name )
  {
    return Character.toLowerCase( name.charAt( 0 ) ) + ( name.length() > 1 ? name.substring( 1 ) : "" );
  }

  @Nonnull
  static String uppercaseFirstCharacter( @Nonnull final String name )
  {
    return Character.toUpperCase( name.charAt( 0 ) ) + ( name.length() > 1 ? name.substring( 1 ) : "" );
  }
}