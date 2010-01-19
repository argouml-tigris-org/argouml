/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *    euluis
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2005-2006 The Regents of the University of California. All
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

package org.argouml.model.mdr;

import java.io.File;
import java.io.FileInputStream;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;

/**
 * Test read models. 
 * TODO: Move this test into argouml base when we will want to read also zip 
 * file.
 * 
 * @author lmaitre
 * 
 */
public class TestReadCompressedFilesAndHref extends
        AbstractMDRModelImplementationTestCase {

    private Logger LOG = Logger.getLogger(TestReadCompressedFilesAndHref.class);

    /**
     * TODO: either the AndroMDA profiles are added to the repository or this
     * should be deleted [euluis on 2010-01-18].
     */
    public void testReadCompressedFileAndHref() {
        String testModel = "tests/testmodels/MDASampleModel.xmi";
        //notice that i must replace and hardcode user.home on my Mac OS X computer
        //[since this always give "/tmp" (at least under Eclipe)]
        final String ANDROMDA_HOME = System.getProperty("user.home")
            + "/andromda-bin-3.1-RC1";
        File mdaIsHere = new File(ANDROMDA_HOME);
        if (mdaIsHere.exists()) {
            LOG.info("Begin testReadCompressedFileAndHref()");        
            XmiReaderImpl reader = new XmiReaderImpl(modelImplementation);
            reader.addSearchPath(ANDROMDA_HOME + "/andromda/xml.zips");
            try {
                reader.parse(
                        new InputSource(new FileInputStream(testModel)), false);
            } catch (Exception e) {
                e.printStackTrace();
                fail("Exception while loading model");
            }
            assertTrue("model is loaded", true);
        }
    }
    
    private final String ISSUE5946_BASE_DIR = "/testmodels/AndroMDA-3.3/";

    public void testReadAndroMDAProfileIssue5946() {
        assertLoadModel(ISSUE5946_BASE_DIR + "unzipped-uml14"
            + "/andromda-profile-datatype-3.3.xml", null,
            "testReadAndroMDAProfileIssue5946");
    }

    public void testReadCompressedAndroMDAProfileIssue5946() {
        // TODO: uncomment the following to get the failure.
//        assertLoadModel(ISSUE5946_BASE_DIR + "zipped-uml14"
//            + "/andromda-profile-datatype/3.3/andromda-profile-datatype-3.3.xml.zip",
//            null, "testReadCompressedAndroMDAProfileFileIssue5946");
    }

    public void testReadFileAndHrefIssue5946() {
        assertLoadModel(ISSUE5946_BASE_DIR + "timetracker2.xmi",
            ISSUE5946_BASE_DIR + "unzipped-uml14/",
            "testReadFileAndHrefIssue5946");
    }

    /**
     * This test reproduces the problem as reported in issue 5946.
     * We have two parts for which there is no support:
     * <ul>
     * <li>reading compressed profiles;</li>
     * <li>reading profiles within the directory tree recursively.</li>
     * </ul>
     */
    public void testReadCompressedFileAndHrefIssue5946() {
        // TODO: uncomment the following to get the failure.
//        assertLoadModel(ISSUE5946_BASE_DIR + "timetracker.xmi",
//            ISSUE5946_BASE_DIR + "zipped-uml14/",
//            "testReadFileAndHrefIssue5946");
    }
    
    void assertLoadModel(String modelPath, String profilesPath,
            String testName) {
        LOG.info("Begin " + testName + "()");
        XmiReaderImpl reader = new XmiReaderImpl(modelImplementation);
        if (profilesPath != null) {
            reader.addSearchPath(
                getClass().getResource(profilesPath).getFile());
        }
        try {
            reader.parse(new InputSource(getClass().getResourceAsStream(
                modelPath)), false);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception while loading model");
        }
        assertTrue(modelPath + " model is loaded", true);
        
    }
}
