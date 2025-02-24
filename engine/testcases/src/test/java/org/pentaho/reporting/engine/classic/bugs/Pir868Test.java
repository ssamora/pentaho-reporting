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


package org.pentaho.reporting.engine.classic.bugs;

import junit.framework.TestCase;
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.ReportProcessingException;
import org.pentaho.reporting.engine.classic.core.layout.ModelPrinter;
import org.pentaho.reporting.engine.classic.core.layout.model.LayoutNodeTypes;
import org.pentaho.reporting.engine.classic.core.layout.model.LogicalPageBox;
import org.pentaho.reporting.engine.classic.core.layout.model.RenderNode;
import org.pentaho.reporting.engine.classic.core.layout.output.ContentProcessingException;
import org.pentaho.reporting.engine.classic.core.testsupport.DebugReportRunner;
import org.pentaho.reporting.engine.classic.core.testsupport.selector.MatchFactory;
import org.pentaho.reporting.libraries.resourceloader.ResourceException;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;

import java.net.URL;

public class Pir868Test extends TestCase {
  public Pir868Test() {
  }

  public void setUp() throws Exception {
    ClassicEngineBoot.getInstance().start();
  }

  public void testSample()
    throws ResourceException, ReportProcessingException, ContentProcessingException {
    final URL reportURL = getClass().getResource( "Pir-868.prpt" );
    final ResourceManager mgr = new ResourceManager();
    final MasterReport report = (MasterReport) mgr.createDirectly( reportURL, MasterReport.class ).getResource();

    final LogicalPageBox pageDH = DebugReportRunner.layoutSingleBand( report, report.getDetailsHeader(), false, false );
    final LogicalPageBox pageIB = DebugReportRunner.layoutSingleBand( report, report.getItemBand(), false, false );

    final RenderNode[] dhRow = MatchFactory.findElementsByNodeType( pageDH, LayoutNodeTypes.TYPE_BOX_ROWBOX );
    final RenderNode[] ibRow = MatchFactory.findElementsByNodeType( pageIB, LayoutNodeTypes.TYPE_BOX_ROWBOX );
    ModelPrinter.INSTANCE.print( pageDH );
    ModelPrinter.INSTANCE.print( pageIB );

    assertEquals( 1, dhRow.length );
    assertEquals( 1, ibRow.length );
    assertEquals( dhRow[ 0 ].getWidth(), ibRow[ 0 ].getWidth() );
    assertEquals( dhRow[ 0 ].getX(), ibRow[ 0 ].getX() );
  }
}
