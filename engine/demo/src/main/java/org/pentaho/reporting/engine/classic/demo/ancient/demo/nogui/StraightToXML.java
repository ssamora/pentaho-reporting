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


package org.pentaho.reporting.engine.classic.demo.ancient.demo.nogui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.net.URL;
import javax.swing.table.TableModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.TableDataFactory;
import org.pentaho.reporting.engine.classic.core.modules.output.xml.XMLProcessor;
import org.pentaho.reporting.engine.classic.demo.ancient.demo.opensource.OpenSourceProjects;
import org.pentaho.reporting.libraries.base.util.ObjectUtilities;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;
import org.pentaho.reporting.libraries.xmlns.parser.ParseException;

/**
 * A demonstration that shows how to generate a report and save it to XML without displaying any print preview first.
 *
 * @author Thomas Morgner
 */
public class StraightToXML
{
  private static final Log logger = LogFactory.getLog(StraightToXML.class);

  /**
   * Creates a new demo application.
   *
   * @param filename the output filename.
   * @throws ParseException if the report could not be parsed.
   */
  public StraightToXML(final String filename)
      throws ParseException
  {
    final URL in = ObjectUtilities.getResource
        ("org/pentaho/reporting/engine/classic/demo/opensource/opensource.xml", StraightToXML.class);
    final MasterReport report = parseReport(in);
    final TableModel data = new OpenSourceProjects();
    report.setDataFactory(new TableDataFactory
        ("default", data));
    saveXML(report, filename);
  }

  /**
   * Reads the report from the specified template file.
   *
   * @param templateURL the template location.
   * @return a report.
   * @throws ParseException if the report could not be parsed.
   */
  private MasterReport parseReport(final URL templateURL)
      throws ParseException
  {
    try
    {
      final ResourceManager mgr = new ResourceManager();
      final Resource resource = mgr.createDirectly(templateURL, MasterReport.class);
      return (MasterReport) resource.getResource();
    }
    catch (Exception e)
    {
      throw new ParseException("Failed to parse the report", e);
    }
  }

  /**
   * Saves a report to XML format.
   *
   * @param report   the report.
   * @param fileName target file name.
   * @return <code>true</code> if the export succeeded, and <code>false</code> otherwise.
   */
  public boolean saveXML(final MasterReport report, final String fileName)
  {
    Writer out = null;
    try
    {
      out = new BufferedWriter(new FileWriter(new File(fileName)));

      final XMLProcessor xprc = new XMLProcessor(report);
      xprc.setWriter(out);
      xprc.processReport();
      return true;
    }
    catch (Exception e)
    {
      System.err.println("Writing PDF failed.");
      System.err.println(e.toString());
      return false;
    }
    finally
    {
      try
      {
        if (out != null)
        {
          out.close();
        }
      }
      catch (Exception e)
      {
        System.err.println("Saving XML failed.");
        System.err.println(e.toString());
      }
    }
  }

  /**
   * Demo starting point.
   *
   * @param args ignored.
   */
  public static void main(final String[] args)
  {
    ClassicEngineBoot.getInstance().start();
    try
    {
      //final StraightToXML demo =
      new StraightToXML(System.getProperty("user.home") + "/OpenSource-Demo.xml");
      System.exit(0);
    }
    catch (Exception e)
    {
      logger.error("Failed to run demo", e);
      System.exit(1);
    }
  }

}
