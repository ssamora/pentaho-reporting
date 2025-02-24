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


package org.pentaho.reporting.libraries.resourceloader.cache;

import org.pentaho.reporting.libraries.resourceloader.ResourceBundleData;
import org.pentaho.reporting.libraries.resourceloader.ResourceLoadingException;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;

/**
 * Creation-Date: 06.04.2006, 09:53:37
 *
 * @author Thomas Morgner
 */
public class DefaultResourceBundleDataCacheEntry implements ResourceBundleDataCacheEntry {
  private ResourceBundleData data;
  private long version;
  private static final long serialVersionUID = 980125445121523950L;

  public DefaultResourceBundleDataCacheEntry( final ResourceBundleData data,
                                              final ResourceManager manager )
    throws ResourceLoadingException {
    if ( data == null ) {
      throw new NullPointerException();
    }
    this.version = data.getVersion( manager );
    this.data = data;
  }

  public ResourceBundleData getData() {
    return data;
  }

  public long getStoredVersion() {
    return version;
  }
}
