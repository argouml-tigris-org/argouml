// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
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

package org.argouml.language.cpp.generator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.argouml.model.Model;
import org.argouml.model.ModelFacade;
import org.argouml.model.UUIDManager;

/**
 * Tests for GeneratorCpp file generation functionalities, i.e., generateFile2 
 * method.
 * @see GeneratorCpp
 * @author euluis
 * @since 0.17.3
 */
public class TestCppFileGeneration extends BaseTestGeneratorCpp {

    /** The Logger for this class */
    private static final Logger LOG = Logger.getLogger(
        TestCppFileGeneration.class);

    /**
     * Creates a new instance of TestCppFileGeneration.
     * @param testName name of the test
     */
    public TestCppFileGeneration(String testName) {
        super(testName);
    }
    
    /**
     * @return the test suite
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(TestCppFileGeneration.class);
        return suite;
    }

    /**
     * Enables debugging in IDEs that don't support debugging unit tests...
     * @param args the arguments given on the commandline
     */
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    /** system temporary directory property name */
    static final String SYSPROPNAME_TMPDIR = "java.io.tmpdir";
    
    /** path of the temporary directory in the system */
    private File tmpDir;

    /** directory to be deleted on tearDown if not null */
    private File genDir;
    
    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() {
        super.setUp();
        String packageName = "pack";
        Object aPackage = Model.getModelManagementFactory().
            buildPackage(packageName, UUIDManager.getInstance().getNewUUID());
        ModelFacade.setNamespace(getAClass(), aPackage);

        tmpDir = new File(System.getProperty(SYSPROPNAME_TMPDIR));
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws IOException {
        if (genDir != null && genDir.exists()) {
            FileUtils.deleteDirectory(genDir);
        } 
    }

    /**
     * Setup a directory with the given name for the caller test.
     * @param dirName the directory to be created in the system temporary dir 
     * @return the created directory
     */
    private File setUpDirectory4Test(String dirName) {
        File generationDir = new File(tmpDir, dirName);
        generationDir.mkdirs();
        return generationDir;
    }

    /**
     * Test for file generation of a classifier, checking if in the second time 
     * the empty method implementation is generated correctly (no extra curly 
     * braces each time the class is generated - issue 2828) and the added 
     * code is preserved.
     * @throws IOException some unexpected file access problem occurred
     */
    public void testGenerateAfterModifyAndIssue2828() throws IOException {
        genDir = setUpDirectory4Test("testIssue2828");
        // generate the classifier for the first time in temp dir
        String filePath = getGenerator().generateFile2(
                getAClass(), genDir.getPath());
        assertNotNull(filePath);
        File genFile = new File(filePath);
        
        // create some content in the foo method implementation
        String encoding = getEncoding(genFile);
        String originalGenerated = FileUtils.readFileToString(
            genFile, encoding);
        StringBuffer modified = new StringBuffer();
        // look for the implementation
        int fooImplIndex = originalGenerated.indexOf("::foo(");
        assertTrue(fooImplIndex != -1);
        // now look for the opening brace...
        fooImplIndex = originalGenerated.indexOf('{', fooImplIndex);
        assertTrue(fooImplIndex != -1);
        // insert some dummy content
        modified.append(originalGenerated.substring(0, ++fooImplIndex));
        String dummyContent = "\n\tint i = 0; // dummy content";
        modified.append(dummyContent);
        modified.append(originalGenerated.substring(fooImplIndex));
        genFile.delete();
        FileUtils.writeStringToFile(genFile, modified.toString(), encoding);
        
        // generate the classifier for the second time
        assertEquals(filePath, 
            getGenerator().generateFile2(getAClass(), genDir.getPath()));
        // check if the file generated in the second time is equal to the 
        // modified file
        String secondGenerated = FileUtils.readFileToString(genFile, encoding);
        assertEquals(modified.toString(), secondGenerated);
    }
    
    /**
     * Test that the model isn't used as a namespace - issue #2963.
     * @throws IOException
     */
    public void testModelIsNotNamespace() throws IOException {
        final String testName = "testModelIsNotNamespace";
        genDir = setUpDirectory4Test(testName);
        ModelFacade.setName(getModel(), testName);
        ModelFacade.setNamespace(getPack(), getModel());
        
        String filePath = getGenerator().generateFile2(
                getAClass(), genDir.getPath());
        assertNotNull(filePath);
        File genFile = new File(filePath);
        
        String encoding = getEncoding(genFile);
        String generated = FileUtils.readFileToString(
            genFile, encoding);
        String modelNs = "namespace " + ModelFacade.getName(getModel()) + " {";
        assertTrue(generated.indexOf(modelNs) == -1);
        String packNs = "namespace " + ModelFacade.getName(getPack()) + " {";
        assertTrue(generated.indexOf(packNs) != -1);
    }

    /**
     * @return the package pack
     */
    private Object getPack() {
        return ModelFacade.getModelElementContainer(getAClass());
    }

    /**
     * Retrieve the encoding for the given file.
     * @param f the file for which the encoding is wanted
     * @return the file encoding as a string
     * @throws IOException if something goes wrong in file access
     */
    private static String getEncoding(File f) throws IOException {
        FileReader fin = new FileReader(f);
        String encoding = fin.getEncoding();
        fin.close();
        return encoding;
    }
}