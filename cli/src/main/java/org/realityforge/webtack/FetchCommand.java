package org.realityforge.webtack;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.realityforge.getopt4j.CLOption;
import org.realityforge.getopt4j.CLOptionDescriptor;
import org.realityforge.webtack.model.WebIDLModelParser;
import org.realityforge.webtack.model.WebIDLSchema;
import org.realityforge.webtack.model.WebIDLWriter;
import org.realityforge.webtack.model.tools.fetch.FetchException;
import org.realityforge.webtack.model.tools.fetch.FetchResult;
import org.realityforge.webtack.model.tools.fetch.FetchUtil;
import org.realityforge.webtack.model.tools.repository.config.RepositoryConfig;
import org.realityforge.webtack.model.tools.repository.config.SourceConfig;

final class FetchCommand
  extends ConfigurableCommand
{
  @Nonnull
  static final String COMMAND = "fetch";
  // This was yoinked from https://github.com/microsoft/TSJS-lib-generator/blob/b19840bed0543fadb5361d5071a172d568f00272/src/idlfetcher.ts#L16-L21
  @Nonnull
  private static final String CSS_SELECTOR =
    // bikeshed and ReSpec
    "pre.idl:not(.extract):not(.example)," +
    // Web Cryptography
    "pre.code code.idl-code," +
    // HTML
    "pre:not(.extract) code.idl," +
    // Permissions
    "#permission-registry + pre.highlight";
  private static final int FORCE_OPT = 'f';
  private static final int NO_VERIFY_OPT = 2;
  private static final int NO_REMOVE_SOURCE_OPT = 3;
  private static final int NO_ADD_DOCS_OPT = 4;
  private static final CLOptionDescriptor[] OPTIONS = new CLOptionDescriptor[]
    {
      new CLOptionDescriptor( "force",
                              CLOptionDescriptor.ARGUMENT_DISALLOWED,
                              FORCE_OPT,
                              "Force the downloading of the source even if the last modified at time indicates it is up to date." ),
      new CLOptionDescriptor( "no-verify",
                              CLOptionDescriptor.ARGUMENT_DISALLOWED,
                              NO_VERIFY_OPT,
                              "Skip running verify command after fetching WebIDL source." ),
      new CLOptionDescriptor( "no-remove-source",
                              CLOptionDescriptor.ARGUMENT_DISALLOWED,
                              NO_REMOVE_SOURCE_OPT,
                              "Do not remove source after fetching and extracting WebIDL." ),
      new CLOptionDescriptor( "no-add-docs",
                              CLOptionDescriptor.ARGUMENT_DISALLOWED,
                              NO_ADD_DOCS_OPT,
                              "Do not add doc source for elements within WebIDL after fetching WebIDL." )
    };
  @Nonnull
  private final Set<String> _sourceNames = new LinkedHashSet<>();
  private boolean _force;
  private boolean _noVerify;
  private boolean _noRemoveSource;
  private boolean _noAddDocs;

  FetchCommand()
  {
    super( COMMAND, "Download the WebIDL source from one or more sources", OPTIONS );
  }

  @Override
  boolean processArguments( @Nonnull final Environment environment, @Nonnull final List<CLOption> arguments )
  {
    for ( final CLOption option : arguments )
    {
      final int optionId = option.getId();
      if ( FORCE_OPT == optionId )
      {
        _force = true;
      }
      else if ( NO_VERIFY_OPT == optionId )
      {
        _noVerify = true;
      }
      else if ( NO_REMOVE_SOURCE_OPT == optionId )
      {
        _noRemoveSource = true;
      }
      else if ( NO_ADD_DOCS_OPT == optionId )
      {
        _noAddDocs = true;
      }
      else
      {
        assert CLOption.TEXT_ARGUMENT == optionId;
        _sourceNames.add( option.getArgument() );
      }
    }

    return true;
  }

  @Override
  int run( @Nonnull final Context context )
  {
    final Logger logger = context.environment().logger();

    final RepositoryConfig config = context.config();

    for ( final String sourceName : getSourceNames( config ) )
    {
      if ( logger.isLoggable( Level.FINE ) )
      {
        logger.log( Level.FINE, "Fetching source named '" + sourceName + "'" );
      }
      final SourceConfig source = config.findSourceByName( sourceName );
      if ( null == source )
      {
        final String message =
          "Error: Attempting to fetch source with the name '" + sourceName + "' but no such source exists.";
        throw new TerminalStateException( message, ExitCodes.ERROR_SOURCE_DOES_NOT_EXIST_CODE );
      }
      final String url = source.getUrl();
      if ( null == url )
      {
        if ( logger.isLoggable( Level.INFO ) )
        {
          logger.log( Level.INFO, "Source named '" + sourceName + "' has no url, no need to fetch" );
        }
        continue;
      }
      final FetchResult result;
      try
      {
        result = FetchUtil.downloadURL( url, _force ? 0 : source.getLastModifiedTime() );
      }
      catch ( final FetchException e )
      {
        throw new TerminalStateException( "Failed during fetch of source at " + e.getUrl(),
                                          e.getCause(),
                                          ExitCodes.ERROR_SOURCE_FETCH_FAILED_CODE );
      }
      if ( null == result )
      {
        if ( logger.isLoggable( Level.INFO ) )
        {
          logger.log( Level.INFO, "Source named '" + sourceName + "' up to date, no need to fetch" );
        }
      }
      else
      {
        final Path file = result.getPath();
        source.setLastModifiedTime( result.getLastModifiedAt() );
        if ( logger.isLoggable( Level.FINE ) )
        {
          logger.log( Level.FINE, "Fetched source named '" + sourceName + "' to " + file );
        }
        final Path target = context.environment().getPathForSource( source );

        if ( !Files.exists( target.getParent() ) )
        {
          try
          {
            Files.createDirectories( target.getParent() );
          }
          catch ( final IOException ioe )
          {
            final String message =
              "Error: Failed to create directory to contain IDL for the source with the name '" +
              sourceName + "' due to " + ioe;
            throw new TerminalStateException( message, ExitCodes.ERROR_EXTRACT_IDL_FAILED_CODE );
          }
        }

        final Path tmpTarget = target.getParent().resolve( target.getName( target.getNameCount() - 1 ) + ".tmp" );
        try
        {
          Files.deleteIfExists( target );
          Files.deleteIfExists( tmpTarget );
        }
        catch ( final IOException ioe )
        {
          final String message =
            "Error: Failed to removed existing files for source named '" + sourceName + "' with the error: " + ioe;
          throw new TerminalStateException( message, ExitCodes.ERROR_REMOVING_EXISTING_IDL_CODE );
        }

        final List<String> sourceTags = source.getTags();
        final Set<String> tags =
          null == sourceTags ? Collections.emptySet() : Collections.unmodifiableSet( new HashSet<>( sourceTags ) );
        extractWebIDL( logger, source, file, tmpTarget );
        try
        {
          RepositoryConfig.save( config );
        }
        catch ( final Exception e )
        {
          final String message =
            "Error: Failed to update config file after fetching IDL for the source with the name '" +
            sourceName + "' due to " + e;
          throw new TerminalStateException( message, ExitCodes.ERROR_SAVING_CONFIG_CODE );
        }
        if ( _noVerify )
        {
          try
          {
            Files.move( tmpTarget, target );
          }
          catch ( final IOException e )
          {
            final String message =
              "Error: Failed to save IDL file for the source named '" + sourceName + "' due to " + e;
            throw new TerminalStateException( message, ExitCodes.ERROR_SAVING_IDL_CODE );
          }
        }
        else
        {
          final WebIDLSchema schema;
          try ( final FileReader reader = new FileReader( tmpTarget.toFile() ) )
          {
            final CountingConsoleErrorListener errorListener = new CountingConsoleErrorListener( tmpTarget.toString() );
            schema = WebIDLModelParser.parse( tmpTarget.toString(), tags, reader, errorListener );

            final int errorCount = errorListener.getErrorCount();
            if ( 0 == errorCount )
            {
              if ( logger.isLoggable( Level.FINE ) )
              {
                logger.log( Level.FINE, "Source named '" + sourceName + "' parsed without errors." );
              }
            }
            else
            {
              final String message =
                "Error: Attempting to parse source named '" + sourceName + "' but there was " + errorCount +
                " errors detected in the WebIDL";
              throw new TerminalStateException( message, ExitCodes.ERROR_IDL_NOT_VALID_CODE );
            }
          }
          catch ( final IOException ioe )
          {
            final String message =
              "Error: Attempting to parse source with the name '" + sourceName + "' but there was an error " +
              "reading the WebIDL for source. Error: " + ioe;
            throw new TerminalStateException( message, ExitCodes.ERROR_SOURCE_NOT_FETCHED_CODE );
          }
          catch ( final Throwable t )
          {
            final String message =
              "Error: Attempting to parse source with the name '" + sourceName +
              "' but there was an unexpected error verifying source. Error: " + t;
            throw new TerminalStateException( message, ExitCodes.ERROR_SOURCE_NOT_FETCHED_CODE );
          }
          if ( schema.isEmpty() )
          {
            final String message =
              "Error: Schema from source named '" + sourceName + "' fetched from " + url +
              " contains no elements. Either the URL contains no WebIDL or the matching rules failed to " +
              "match WebIDL sections.";
            throw new TerminalStateException( message, ExitCodes.ERROR_IDL_NOT_VALID_CODE );
          }

          try ( final FileWriter writer = new FileWriter( target.toFile() ) )
          {
            WebIDLWriter.writeSchema( writer, schema );
          }
          catch ( final IOException ioe )
          {
            final String message =
              "Error: Failed while writing source named '" + sourceName + "' with the error: " + ioe;
            throw new TerminalStateException( message, ExitCodes.ERROR_SAVING_IDL_CODE );
          }
          final CountingConsoleErrorListener errorListener = new CountingConsoleErrorListener( tmpTarget.toString() );
          try ( final FileReader reader = new FileReader( target.toFile() ) )
          {
            final WebIDLSchema reloadedSchema =
              WebIDLModelParser.parse( target.toString(), tags, reader, errorListener );
            if ( !reloadedSchema.equiv( schema ) )
            {
              final String message =
                "Error: Normalized WebIDL for source named '" + sourceName + "' saved to " + target +
                " is not equivalent to WebIDL extracted to " + tmpTarget;
              throw new TerminalStateException( message, ExitCodes.ERROR_SAVING_IDL_CODE );
            }
          }
          catch ( final IOException ioe )
          {
            final String message =
              "Error: Failed reloading schema for source named '" + sourceName + "' with the error: " + ioe;
            throw new TerminalStateException( message, ExitCodes.ERROR_SAVING_IDL_CODE );
          }
          try
          {
            Files.deleteIfExists( tmpTarget );
          }
          catch ( final IOException ioe )
          {
            final String message =
              "Error: Failed to remove temporary IDL file for source named '" +
              sourceName + "' with the error: " + ioe;
            throw new TerminalStateException( message, ExitCodes.ERROR_REMOVING_EXISTING_IDL_CODE );
          }
          if ( logger.isLoggable( Level.INFO ) )
          {
            logger.log( Level.INFO, "Source named '" + sourceName + "' extracted from url " + url +
                                    " exported WebIDL to file " + target );
          }
        }
        if ( !_noRemoveSource )
        {
          try
          {
            Files.deleteIfExists( target.getParent().resolve( sourceName + ".html" ) );
          }
          catch ( final IOException ioe )
          {
            final String message =
              "Error: Failed to remove source file that the IDL file was extracted from for source named '" +
              sourceName + "' with the error: " + ioe;
            throw new TerminalStateException( message, ExitCodes.ERROR_REMOVING_SOURCE_CODE );
          }
        }
        if ( !_noAddDocs )
        {
          final AddDocsCommand command = new AddDocsCommand();
          command.processOptions( context.environment(), "--idl-source-name", sourceName );
          final int addResult = command.run( context );
          if ( ExitCodes.SUCCESS_EXIT_CODE != addResult )
          {
            return addResult;
          }
        }
      }
    }

    return ExitCodes.SUCCESS_EXIT_CODE;
  }

  @Nonnull
  private Set<String> getSourceNames( final RepositoryConfig config )
  {
    return _sourceNames.isEmpty() ?
           config.getSources()
             .stream()
             .map( SourceConfig::getName )
             .collect( Collectors.toCollection( LinkedHashSet::new ) ) :
           _sourceNames;
  }

  private void extractWebIDL( @Nonnull final Logger logger,
                              @Nonnull final SourceConfig source,
                              @Nonnull final Path input,
                              @Nonnull final Path output )
  {
    final String sourceName = source.getName();
    final String url = source.getUrl();
    assert null != url;
    try
    {
      final String lowerUrl = url.toLowerCase();
      if ( lowerUrl.endsWith( ".idl" ) || lowerUrl.endsWith( WebIDLSchema.EXTENSION ) )
      {
        if ( logger.isLoggable( Level.INFO ) )
        {
          logger.log( Level.INFO, "Source named '" + sourceName + "' is already an IDL file. No processing required." );
        }
        Files.copy( input, output, StandardCopyOption.REPLACE_EXISTING );
      }
      else
      {
        if ( logger.isLoggable( Level.FINE ) )
        {
          logger.log( Level.FINE, "Source named '" + sourceName + "' needs to have IDL extracted." );
        }
        // Cache a copy of downloaded html from which WebIDL was extracted
        Files.copy( input, output.getParent().resolve( sourceName + ".html" ), StandardCopyOption.REPLACE_EXISTING );

        final Document document = Jsoup.parse( input.toFile(), StandardCharsets.UTF_8.name(), url );
        final String selector = source.getSelector();
        final Elements elements = document.select( null == selector ? CSS_SELECTOR : selector );

        // Some spec docs (gamepad, push_api, screen_orientation, selection_api) include an anchor in block so strip it out
        elements.forEach( e -> e.getElementsByClass( "idlHeader" ).forEach( Node::remove ) );

        final String idl = elements
          .stream()
          // Not part of an example block
          .filter( e -> !( e.hasParent() && e.parent().classNames().contains( "example" ) ) )

          // Not part of the index
          .filter( e -> null == e.previousElementSibling() ||
                        (
                          !e.previousElementSibling().classNames().contains( "atrisk" ) &&
                          !e.previousElementSibling().text().contains( "IDL Index" )
                        ) )
          .map( Element::text )
          .collect( Collectors.joining( "\n\n" ) );
        Files.write( output,
                     idl.getBytes( StandardCharsets.UTF_8 ),
                     StandardOpenOption.CREATE,
                     StandardOpenOption.TRUNCATE_EXISTING );
      }
      if ( logger.isLoggable( Level.FINE ) )
      {
        logger.log( Level.FINE, "Source named '" + sourceName + "' processed and WebIDL extracted to file " + output );
      }
    }
    catch ( final IOException ioe )
    {
      final String message =
        "Error: Failed to extract WebIDL from source named '" + sourceName + "' downloaded to file " +
        input + " due to " + ioe;
      throw new TerminalStateException( message, ExitCodes.ERROR_EXTRACT_IDL_FAILED_CODE );
    }
  }
}
