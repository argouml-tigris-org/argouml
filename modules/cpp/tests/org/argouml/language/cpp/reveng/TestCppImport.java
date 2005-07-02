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
package org.argouml.language.cpp.reveng;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.io.CopyUtils;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.reveng.DiagramInterface;
import org.argouml.uml.reveng.Import;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test the CppImport class. FIXME: duplicate code from TestCppFileGeneration
 * and BaseTestGeneratorCpp.
 * 
 * @author Luis Sergio Oliveira (euluis)
 * @since 0.19.3
 */
public class TestCppImport extends TestCase {

    /** The Logger for this class */
    private static final Logger LOG = Logger.getLogger(TestCppImport.class);

    /**
     * Constructor.
     * 
     * @param testName
     */
    public TestCppImport(String testName) {
        super(testName);
    }

    /**
     * @return the test suite
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(TestCppImport.class);
        return suite;
    }

    /**
     * Enables debugging in IDEs that don't support debugging unit tests...
     * 
     * @param args the arguments given on the commandline
     */
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * System temporary directory property name.
     */
    static final String SYSPROPNAME_TMPDIR = "java.io.tmpdir";

    /**
     * Path of the temporary directory in the system.
     */
    private File tmpDir;

    /**
     * Directory to be deleted on tearDown if not null.
     */
    private File genDir;

    /**
     * The ArgoUML C++ reveng module.
     */
    private CppImport cppImp;

    /**
     * The ArgoUML project.
     */
    private Project proj;

    /**
     * @throws Exception if something goes wrong
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        tmpDir = new File(System.getProperty(SYSPROPNAME_TMPDIR));
        proj = ProjectManager.getManager().getCurrentProject();
        cppImp = new CppImport();
    }

    /**
     * @throws Exception
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        if (genDir != null && genDir.exists()) {
            FileUtils.deleteDirectory(genDir);
        }
        super.tearDown();
    }

    /**
     * Setup a directory with the given name for the caller test.
     * 
     * @param dirName the directory to be created in the system temporary dir
     * @return the created directory
     */
    private File setUpDirectory4Test(String dirName) {
        File generationDir = new File(tmpDir, dirName);
        generationDir.mkdirs();
        return generationDir;
    }

    /**
     * Simple test for the <code>CppImport.parseFile(xxx)</code> method, using
     * the SimpleClass.cpp source file.
     * 
     * @throws Exception something went wrong
     */
    public void testParseFileSimpleClass() throws Exception {
        genDir = setUpDirectory4Test("testParseFileSimpleClass");
        File srcFile = setupSrcFile4Reverse("SimpleClass.cpp");

        Editor ed = Globals.curEditor();
        DiagramInterface di = new DiagramInterface(ed);
        Import imp = null;
        // FIXME: The following fails because it can't find PluggableImport
        // modules. I must get ModuleLoader to load the CppImport module.
        // From a different perspective, I don't like being too tied to the
        // Import class, since it seams to be doing too much - i.e., it
        // contains things related to the view, to the model and to the
        // controller, while it should be only a controller. The view
        // (e.g., details pannel) and the model (e.g.,
        // CreateDiagramsChecked, DiscendDirectoriesRecursively and
        // Attribute properties).
        // Bottom line: for now I simply don't need it!
        //!imp = new Import();
        cppImp.parseFile(proj, srcFile, di, imp);

        Collection nss = Model.getModelManagementHelper().getAllNamespaces(
                proj.getModel());
        Object pack = findModelElementWithName(nss, "pack");
        assertNotNull("The pack namespace wasn't found in the model!", pack);

        Collection clss = Model.getCoreHelper().getAllClasses(pack);
        Object simpleClass = findModelElementWithName(clss, "SimpleClass");
        assertNotNull("The pack.SimpleClass class wasn't found in the model!",
                simpleClass);

        Collection opers = Model.getCoreHelper().getBehavioralFeatures(
                simpleClass);
        Object newOperation = findModelElementWithName(opers, "newOperation");
        assertNotNull("The pack.SimpleClass.newOperation() model element "
                + "doesn't exist!", newOperation);
    }

    /**
     * When a file must be reversed, it must be open by the CppImport,
     * therefore, a copy of the file name given, which is a resource in the
     * package of this class, must be prepared. The <code>File</code> object
     * for this copy is returned, with its absolute path set.
     * 
     * @param fn name of the source file which exists as a resource within the
     *            package of this class
     * @return the <code>File</code> object for the copy of the source file
     * @throws IOException
     */
    private File setupSrcFile4Reverse(String fn) throws IOException {
        InputStream in = getClass().getResourceAsStream(fn);
        File srcFile = new File(genDir, fn);
        OutputStream out = null;
        try {
            out = new FileOutputStream(srcFile);
        } catch (FileNotFoundException e) {
            in.close();
            throw e;
        }
        try {
            CopyUtils.copy(in, out);
        } finally {
            in.close();
            out.close();
        }
        return srcFile;
    }

    /**
     * Find in a <code>Collection</code> of model elements one with the
     * specified name.
     * 
     * @param mes the model elements in which to search
     * @param meName simple name of the ME
     * @return the ME if found or null
     */
    private Object findModelElementWithName(Collection mes, String meName) {
        Iterator it = mes.iterator();
        Object me = null;
        while (it.hasNext()) {
            Object possibleME = it.next();
            if (meName.equals(Model.getFacade().getName(possibleME))) {
                me = possibleME;
                break;
            }
        }
        return me;
    }

}