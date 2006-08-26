// $Id$
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

import javax.jmi.reflect.RefPackage;

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
    
    //notice that i must replace and hardcode user.home on my Mac OS X computer
    //[since this always give "/tmp" (at least under Eclipe)]
    private static final String ANDROMDA_HOME = System.getProperty("user.home")
            + "/andromda-bin-3.1-RC1";

    private String testModel = "tests/testmodels/MDASampleModel.xmi";

    //Uncomment if you test the zipfilepersister
    //private String testModelZip = "tests/testmodels/CarRentalSystem.xml.zip";

    public void testReadCompressedFileAndHref() {
        File mdaIsHere = new File (ANDROMDA_HOME);
        if (mdaIsHere.exists()) {
            LOG.info("Begin testReadCompressedFileAndHref()");        
            //Remove the dependency to argouml base for the moment
            // org.argouml.persistence.ProjectFilePersister persister =
            //     org.argouml.persistence.PersistenceManager
            //         .getInstance().getPersisterFromFileName(testModel);
            XmiReaderImpl reader = new XmiReaderImpl(modelImplementation,
                    (RefPackage) modelImplementation.getMofPackage());
            try {
                //persister.doLoad(new File(testModel));
                reader.parse(new InputSource(new FileInputStream(
                        testModel)));
            } catch (Exception e) {
                e.printStackTrace();
                fail("Exception while loading model");
            }
            assertTrue("model is loaded", true);
        }
    }

    /* Commented out until the ZipFilePersister have been committed - notice that it works!
     TODO: also adapt for Win32/generic, i use /tmp to store the saved file
    public void testReadAndWriteCompressedFileAndHrefWithZargoFilePersisterAndZip() {
        LOG.info("Begin testReadCompressedFileAndHrefWithZipFilePersisterAndZip()");
        org.argouml.persistence.ProjectFilePersister persister = 
            org.argouml.persistence.PersistenceManager.getInstance().
                    getPersisterFromFileName(testModelZip);
        try {
            File file = new File(testModelZip);
            LOG.info("File is "+file+", persister is "+persister);
            Project p = persister.doLoad(file);
            File saveFile = new File("/tmp/"+file.getName());
            LOG.info("Saving file " + saveFile.getCanonicalPath() 
                + ", persister is "+persister);
            persister.save(p,saveFile);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception while loading model");
        }
        assertTrue("model is loaded",true);
    }*/

    /*
     * @see org.argouml.model.mdr.AbstractMDRModelImplementationTestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        XmiReferenceResolverImpl.addModuleSearchPath(ANDROMDA_HOME
                + "/andromda/xml.zips");
    }
}
