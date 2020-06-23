package org.realityforge.webtack.jsinterop;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;
import org.realityforge.webtack.model.Argument;
import org.realityforge.webtack.model.CallbackInterfaceDefinition;
import org.realityforge.webtack.model.ConstMember;
import org.realityforge.webtack.model.ConstValue;
import org.realityforge.webtack.model.EnumerationDefinition;
import org.realityforge.webtack.model.Kind;
import org.realityforge.webtack.model.OperationMember;
import org.realityforge.webtack.model.Type;
import org.realityforge.webtack.model.TypeReference;

final class CallbackInterfaceGenerator
{
  void generate( @Nonnull final CodeGenContext context, @Nonnull final CallbackInterfaceDefinition definition )
    throws IOException
  {
    final boolean exposedOnGlobal = ElementUtil.isExposedOnGlobal( definition );
    final TypeSpec.Builder type =
      TypeSpec
        .interfaceBuilder( definition.getName() )
        .addModifiers( Modifier.PUBLIC );
    CodeGenUtil.writeGeneratedAnnotation( type );
    type.addAnnotation( AnnotationSpec.builder( Types.JS_TYPE )
                          .addMember( "isNative", "true" )
                          .addMember( "namespace", "$T.GLOBAL", Types.JS_PACKAGE )
                          .addMember( "name", exposedOnGlobal ? "\"" + definition.getName() + "\"" : "\"?\"" )
                          .build() )
      .addAnnotation( FunctionalInterface.class );

    for ( final ConstMember constant : definition.getConstants() )
    {
      generateConstant( context, constant, type );
    }

    generateOperation( context, definition.getOperation(), type );

    CodeGenUtil.writeTopLevelType( context, type );
  }

  private void generateConstant( @Nonnull final CodeGenContext context,
                                 @Nonnull final ConstMember constant,
                                 @Nonnull final TypeSpec.Builder type )
  {
    final Type constantType = constant.getType();
    final Type actualType = CodeGenUtil.resolveTypeDefs( context, constantType );
    final FieldSpec.Builder field =
      FieldSpec.builder( CodeGenUtil.toTypeName( context, constantType, actualType ),
                         constant.getName(),
                         Modifier.PUBLIC,
                         Modifier.STATIC,
                         Modifier.FINAL );
    final ConstValue value = constant.getValue();
    final ConstValue.Kind kind = value.getKind();
    if ( ConstValue.Kind.True == kind )
    {
      field.initializer( "true" );
    }
    else if ( ConstValue.Kind.False == kind )
    {
      field.initializer( "false" );
    }
    else if ( ConstValue.Kind.NaN == kind &&
              ( Kind.Float == actualType.getKind() || Kind.UnrestrictedFloat == actualType.getKind() ) )
    {
      field.initializer( "$T.NaN", Float.class );
    }
    else if ( ConstValue.Kind.NaN == kind )
    {
      assert Kind.Double == actualType.getKind() || Kind.UnrestrictedDouble == actualType.getKind();
      field.initializer( "$T.NaN", Double.class );
    }
    else if ( ConstValue.Kind.PositiveInfinity == kind &&
              ( Kind.Float == actualType.getKind() || Kind.UnrestrictedFloat == actualType.getKind() ) )
    {
      field.initializer( "$T.POSITIVE_INFINITY", Float.class );
    }
    else if ( ConstValue.Kind.PositiveInfinity == kind )
    {
      assert Kind.Double == actualType.getKind() || Kind.UnrestrictedDouble == actualType.getKind();
      field.initializer( "$T.POSITIVE_INFINITY", Double.class );
    }
    else if ( ConstValue.Kind.NegativeInfinity == kind &&
              ( Kind.Float == actualType.getKind() || Kind.UnrestrictedFloat == actualType.getKind() ) )
    {
      field.initializer( "$T.NEGATIVE_INFINITY", Float.class );
    }
    else if ( ConstValue.Kind.NegativeInfinity == kind )
    {
      assert Kind.Double == actualType.getKind() || Kind.UnrestrictedDouble == actualType.getKind();
      field.initializer( "$T.NEGATIVE_INFINITY", Double.class );
    }
    else if ( ConstValue.Kind.Decimal == kind )
    {
      field.initializer( Objects.requireNonNull( value.getValue() ) );
    }
    else
    {
      assert ConstValue.Kind.Integer == kind;
      field.initializer( Objects.requireNonNull( value.getValue() ) );
    }
    type.addField( field.build() );
  }

  private void generateOperation( @Nonnull final CodeGenContext context,
                                  @Nonnull final OperationMember operation,
                                  @Nonnull final TypeSpec.Builder type )
  {
    final String name = operation.getName();
    assert null != name;
    final MethodSpec.Builder method =
      MethodSpec.methodBuilder( name ).addModifiers( Modifier.PUBLIC, Modifier.ABSTRACT );
    final Type returnType = operation.getReturnType();
    if ( Kind.Void != returnType.getKind() )
    {
      final Type actualType = CodeGenUtil.resolveTypeDefs( context, returnType );
      if ( CodeGenUtil.isNullable( context, returnType ) )
      {
        method.addAnnotation( Types.NULLABLE );
      }
      else if ( !actualType.getKind().isPrimitive() )
      {
        method.addAnnotation( Types.NONNULL );
      }
      method.returns( CodeGenUtil.toTypeName( context, returnType, actualType ) );
    }
    final List<Argument> arguments = operation.getArguments();
    for ( final Argument argument : arguments )
    {
      final Type argumentType = argument.getType();
      final Type actualType = CodeGenUtil.resolveTypeDefs( context, argumentType );
      final ParameterSpec.Builder parameter =
        ParameterSpec.builder( CodeGenUtil.toTypeName( context, argumentType, actualType ), argument.getName() );

      if ( addMagicConstantsAnnotation() && Kind.TypeReference == actualType.getKind() )
      {
        final EnumerationDefinition enumeration =
          context.getSchema().findEnumerationByName( ( (TypeReference) actualType ).getName() );
        if ( null != enumeration )
        {
          final AnnotationSpec.Builder annotation = AnnotationSpec.builder( Types.MAGIC_CONSTANT );
          for ( final String value : enumeration.getValues() )
          {
            annotation.addMember( "stringValues", "$S", value );
          }
          parameter.addAnnotation( annotation.build() );

        }
      }
      if ( CodeGenUtil.isNullable( context, argumentType ) )
      {
        parameter.addAnnotation( Types.NULLABLE );
      }
      else if ( !argumentType.getKind().isPrimitive() )
      {
        parameter.addAnnotation( Types.NONNULL );
      }
      method.addParameter( parameter.build() );
    }
    final MethodSpec build = method.build();
    type.addMethod( build );
  }

  boolean addMagicConstantsAnnotation()
  {
    return true;
  }
}
