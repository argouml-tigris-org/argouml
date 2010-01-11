/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2006 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.persistence;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import junit.framework.TestCase;

/**
 * Test class for PersistenceManager. Actually, the only tested method is 
 * getPersisterFromFileName().
 *
 * @since Jun 2, 2006
 * @author andrea.nironi@gmail.com
 */
public class TestPersistenceManager extends TestCase {

    /**
     * Constructor for TestPersistenceManager.
     * @param arg0 is the name of the test case.
     */
    public TestPersistenceManager(String arg0) {
        super(arg0);
    }
    
    /**
     * Test if the getPersisterFromFileName returns a correct implementation.
     * 
     */
    public void testGetPersisterFromFileName() {
        PersistenceManager persistence = PersistenceManager.getInstance();
        AbstractFilePersister testPersister;
        
        // test an unknown file
        testPersister = persistence.getPersisterFromFileName(
                "unknown.foo");
        assertNull(testPersister);
        
        // test project readable files
        testPersister = persistence.getPersisterFromFileName(
                "unknown." + new ZargoFilePersister().getExtension());
        assertNotNull(testPersister);
        
        testPersister = persistence.getPersisterFromFileName(
                "unknown." + new XmiFilePersister().getExtension());
        assertNotNull(testPersister);
        
        testPersister = persistence.getPersisterFromFileName(
                "unknown." + new XmlFilePersister().getExtension());
        assertNotNull(testPersister);
        
        testPersister = persistence.getPersisterFromFileName(
                "unknown." + new UmlFilePersister().getExtension());
        assertNotNull(testPersister);
        
        testPersister = persistence.getPersisterFromFileName(
                "unknown." + new ZipFilePersister().getExtension());
        assertNotNull(testPersister);
    }
    
    public void testSetSaveFileChooserFilters() {
        JFileChooser chooser = new JFileChooser();
        PersistenceManager persistence = PersistenceManager.getInstance();
        persistence.setSaveFileChooserFilters(chooser, null);
        
        FileFilter[] fileFilters = chooser.getChoosableFileFilters();
        assertNotNull(fileFilters);
        assertEquals(4, fileFilters.length);
        FileFilter defaultFileFilter = chooser.getFileFilter();
        assertNotNull(defaultFileFilter);
        assertTrue(defaultFileFilter.accept(new File(
                "foo."
                + new ZargoFilePersister().getExtension())));
        
    }
}
