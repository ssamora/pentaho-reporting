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


package org.pentaho.reporting.designer.core.splash;

import javax.swing.*;
import javax.swing.plaf.PanelUI;
import java.awt.*;

/**
 * @author wseyler
 */
public class BackgroundUI extends PanelUI {
  private Image background;

  public BackgroundUI( ImageIcon imageIcon ) {
    background = imageIcon.getImage();
  }

  public void paint( Graphics g, JComponent c ) {
    g.drawImage( background, 0, 0, null );
  }
}


