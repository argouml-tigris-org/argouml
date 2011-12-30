/* $Id$
 *******************************************************************************
 * Copyright (c) 2011 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Luis Sergio Oliveira (euluis)
 *******************************************************************************
 */

package org.argouml.model.mdr;

import junit.framework.TestCase;

/**
 * Unit tests for {@link XmiReaderImpl}.
 *
 * @author Luis Sergio Oliveira (euluis)
 */
public class TestXmiReaderImpl extends TestCase {

    /**
     * Test {@link XmiReaderImpl#getTempXMIFileURIPrefix()}.
     */
    public void testGetTempXMIFileURIPrefix() {
        assertTrue(XmiReaderImpl.getTempXMIFileURIPrefix().contains(
                XmiReaderImpl.TEMP_XMI_FILE_PREFIX));
        assertTrue(XmiReaderImpl.getTempXMIFileURIPrefix().matches(
                ".*"
                + System.getProperty("java.io.tmpdir").replaceAll("[/\\\\]", "[/\\]+")
                + ".*"));
    }
}
