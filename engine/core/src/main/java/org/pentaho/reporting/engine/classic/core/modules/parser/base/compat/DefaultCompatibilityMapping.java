/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2029-07-20
 ******************************************************************************/


package org.pentaho.reporting.engine.classic.core.modules.parser.base.compat;

import org.pentaho.reporting.libraries.base.util.FloatDimension;
import org.pentaho.reporting.libraries.xmlns.common.ParserUtil;

import java.util.HashMap;

public class DefaultCompatibilityMapping implements CompatiblityMapping {
  private static final String OLD_DEMO = "org.jfree.report.demo.";
  private static final String NEW_DEMO = "org.pentaho.reporting.engine.classic.demo.";
  private static final String OLD_ANCIENT = "org.jfree.report.ancient.";
  private static final String NEW_ANCIENT = "org.pentaho.reporting.engine.classic.demo.ancient.";
  private static final String OLD_EXT = "org.jfree.report.ext.";
  private static final String NEW_EXT = "org.pentaho.reporting.engine.classic.extensions.";
  private static final String OLD_CORE = "org.jfree.report.";
  private static final String NEW_CORE = "org.pentaho.reporting.engine.classic.core.";
  private static final String JREFINERY_CORE = "com.jrefinery.report.";

  private HashMap classMappings;
  private static final String JREFINERY_PDFOUTPUT_CONFIG =
      "com.jrefinery.report.targets.pageable.output.PDFOutputTarget.default.";

  public DefaultCompatibilityMapping() {
    classMappings = new HashMap();
    classMappings.put( "org.jfree.ui.FloatDimension", FloatDimension.class.getName() );
    classMappings.put( "com.jrefinery.report.function.BSHExpression",
        "org.pentaho.reporting.engine.classic.core.modules.misc.beanshell.BSHExpression" );
  }

  public String mapClassName( final String className ) {
    if ( className == null ) {
      return null;
    }

    return performMapping( className );
  }

  private String performMapping( final String className ) {
    final String value = (String) classMappings.get( className );
    if ( value != null ) {
      return value;
    }
    if ( className.startsWith( OLD_ANCIENT ) ) {
      return NEW_ANCIENT + className.substring( OLD_ANCIENT.length() );
    }
    if ( className.startsWith( OLD_DEMO ) ) {
      return NEW_DEMO + className.substring( OLD_DEMO.length() );
    }
    if ( className.startsWith( OLD_EXT ) ) {
      return NEW_EXT + className.substring( OLD_EXT.length() );
    }
    if ( className.startsWith( OLD_CORE ) ) {
      return NEW_CORE + className.substring( OLD_CORE.length() );
    }
    if ( className.startsWith( JREFINERY_CORE ) ) {
      return NEW_CORE + className.substring( JREFINERY_CORE.length() );
    }
    return className;
  }

  public String mapConfigurationKey( final String key ) {
    if ( key == null ) {
      return null;
    }
    if ( key.startsWith( JREFINERY_PDFOUTPUT_CONFIG ) ) {
      return "org.pentaho.reporting.engine.classic.core.modules.output.pageable.pdf."
          + key.substring( JREFINERY_CORE.length() );
    }

    return performMapping( key );
  }

  public String mapConfigurationValue( final String originalKey, final String mappedKey, final String value ) {
    if ( mappedKey == null ) {
      return null;
    }
    if ( value == null ) {
      return null;
    }

    return performMapping( value );
  }

  public String mapExpressionProperty( final String expressionName, final String mappedExpression,
      final String propertyName ) {
    if ( "com.jrefinery.report.function.TextFormatExpression".equals( expressionName ) ) {
      if ( ParserUtil.parseInt( propertyName, -1 ) >= 0 ) {
        return "field[" + propertyName + "]";
      }
    }

    return propertyName;
  }
}
