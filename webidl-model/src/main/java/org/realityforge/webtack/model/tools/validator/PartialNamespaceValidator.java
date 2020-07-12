package org.realityforge.webtack.model.tools.validator;

import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.Nonnull;
import org.realityforge.webtack.model.PartialNamespaceDefinition;
import org.realityforge.webtack.model.WebIDLSchema;

final class PartialNamespaceValidator
  implements Validator
{
  @Nonnull
  @Override
  public Collection<ValidationError> validate( @Nonnull final WebIDLSchema schema )
  {
    final Collection<ValidationError> errors = new ArrayList<>();
    for ( final PartialNamespaceDefinition partial : schema.getPartialNamespaces() )
    {
      final String name = partial.getName();
      if ( null == schema.findNamespaceByName( name ) )
      {
        final String message =
          "Namespace named '" + name + "' does not exist but a partial for the namespace does exist";
        errors.add( new ValidationError( partial, message, true ) );
      }
    }
    return errors;
  }
}
