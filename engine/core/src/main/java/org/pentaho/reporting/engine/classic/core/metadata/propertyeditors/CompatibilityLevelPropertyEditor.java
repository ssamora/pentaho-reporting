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


package org.pentaho.reporting.engine.classic.core.metadata.propertyeditors;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditor;
import java.util.TreeSet;

import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;

public class CompatibilityLevelPropertyEditor implements PropertyEditor {
  private String[] TAGS = { null, "3.8.0", "3.8.3", "3.9.0", "3.9.1", "4.0.0" };
  private int[] TAG_MAP = { -1, ClassicEngineBoot.computeVersionId( 3, 8, 0 ),
    ClassicEngineBoot.computeVersionId( 3, 8, 3 ), ClassicEngineBoot.computeVersionId( 3, 9, 0 ),
    ClassicEngineBoot.computeVersionId( 3, 9, 1 ), ClassicEngineBoot.computeVersionId( 4, 0, 0 ) };

  private PropertyChangeSupport propertyChangeSupport;
  private Integer value;

  public CompatibilityLevelPropertyEditor() {
    propertyChangeSupport = new PropertyChangeSupport( this );
  }

  public void setValue( final Object value ) {
    if ( value == null || value instanceof Integer ) {
      final Object oldValue = this.value;
      this.value = (Integer) value;
      propertyChangeSupport.firePropertyChange( null, oldValue, this.value );
    }
  }

  public Object getValue() {
    return value;
  }

  public String getAsText() {
    if ( value == null ) {
      return null;
    }

    final int ix = value.intValue();
    for ( int i = 0; i < TAG_MAP.length; i++ ) {
      if ( TAG_MAP[i] == ix ) {
        return TAGS[i];
      }
    }
    if ( ix < 0 ) {
      return null;
    }

    return ClassicEngineBoot.printVersion( value );
  }

  public void setAsText( final String text ) throws IllegalArgumentException {
    if ( text == null ) {
      setValue( null );
      return;
    }

    for ( int i = 0; i < TAGS.length; i++ ) {
      if ( text.equals( TAGS[i] ) ) {
        setValue( TAG_MAP[i] );
        return;
      }
    }

    final int i = ClassicEngineBoot.parseVersionId( text );
    if ( i != -1 ) {
      setValue( i );
    } else {
      setValue( null );
    }
  }

  public String[] getTags() {
    final String asText = getAsText();
    if ( asText == null ) {
      return TAGS.clone();
    }

    final TreeSet<String> tags = new TreeSet<String>();
    for ( final String s : TAGS ) {
      if ( s != null ) {
        tags.add( s );
      }
    }
    tags.add( asText );
    final String[] retval = new String[tags.size() + 1];
    System.arraycopy( tags.toArray( new String[tags.size()] ), 0, retval, 1, tags.size() );
    return retval;
  }

  public boolean isPaintable() {
    return false;
  }

  public void paintValue( final Graphics gfx, final Rectangle box ) {

  }

  public Component getCustomEditor() {
    return null;
  }

  public boolean supportsCustomEditor() {
    return false;
  }

  /**
   * Register a listener for the PropertyChange event. When a PropertyEditor changes its value it should fire a
   * PropertyChange event on all registered PropertyChangeListeners, specifying the null value for the property name and
   * itself as the source.
   *
   * @param listener
   *          An object to be invoked when a PropertyChange event is fired.
   */
  public void addPropertyChangeListener( final PropertyChangeListener listener ) {
    propertyChangeSupport.addPropertyChangeListener( listener );
  }

  /**
   * Remove a listener for the PropertyChange event.
   *
   * @param listener
   *          The PropertyChange listener to be removed.
   */
  public void removePropertyChangeListener( final PropertyChangeListener listener ) {
    propertyChangeSupport.removePropertyChangeListener( listener );
  }

  public String getJavaInitializationString() {
    return String.valueOf( value );
  }

}
