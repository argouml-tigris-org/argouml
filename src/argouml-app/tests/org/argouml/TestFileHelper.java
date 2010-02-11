/* $Id$
 *****************************************************************************
 * Copyright (c) 2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    euluis
 *****************************************************************************
 */

package org.argouml;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

/**
 * Integration tests for the FileHelper.
 * @author Luis Sergio Oliveira (euluis)
 */
public class TestFileHelper extends TestCase {
    
    /**
     * Test {@link FileHelper#createTempDirectory()} with the directory not
     * existing before.
     * @throws IOException if the creation of files or directories throws.
     */
    public void testCreateTempDirectory() throws IOException {
        File tmpDir = FileHelper.createTempDirectory();
        tmpDir.deleteOnExit();
        assertTrue("The directory should exist.", tmpDir.exists());
        assertTrue("The directory isn't a directory.", tmpDir.isDirectory());
        assertTrue("The directory is writable.", tmpDir.canWrite());
    }
}
