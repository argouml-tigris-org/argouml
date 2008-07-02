// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.persistence;

import java.io.File;
import java.net.URL;

import junit.framework.TestCase;
import org.argouml.model.InitializeModel;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.profile.init.InitProfileSubsystem;

/**
 * Testclass for the XMIReader. Placeholder for all saving/loading tests
 * concerning XMIReader (like the dreaded ClassCastException issues).
 *
 * @author jaap.branderhorst@xs4all.nl
 * @since Jan 17, 2003
 */
public class TestXmiFilePersister extends TestCase {

    /**
     * Constructor for TestXMIReader.
     * @param arg0 is the name of the test case.
     */
    public TestXmiFilePersister(String arg0) {
        super(arg0);
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeMDR();
        new InitProfileSubsystem().init();
    }

    /**
     * This is a regression test for issue 1504.
     * Test basic serialization to XMI file.
     * 
     * @throws Exception if saving fails.
     */
    public void testSave() throws Exception {
        Project p = ProjectManager.getManager().makeEmptyProject();
        Object clazz = Model.getCoreFactory().buildClass(
                Model.getModelManagementFactory().getRootModel());
        Object returnType = ProjectManager.getManager()
            	.getCurrentProject().getDefaultReturnType();
        Object oper = Model.getCoreFactory().buildOperation(clazz, returnType);
        Model.getCoreHelper().setType(
                Model.getFacade().getParameter(oper, 0),
                p.findType("String"));
        File file = new File("test.xmi");
        XmiFilePersister persister = new XmiFilePersister();
        p.preSave();
        persister.save(p, file);
        p.postSave();
    }
    
    /**
     * This is more like a functional test, exercising several sub-systems 
     * of ArgoUML, including persistence, kernel and model.
     * It is composed of the following steps:
     * <ol>
     * <li>create a model with a class in it, then assert that the class is 
     * found in the project;</li>
     * <li>save the model as an XMI file;</li>
     * <li>load the model and create a project around it, then assert that 
     * the class is found again.</li>
     * </ol>
     * 
     * @throws Exception when any of the activities fails
     */
    public void testCreateSaveAndLoadYieldsCorrectModel() throws Exception {
        Project project = ProjectManager.getManager().makeEmptyProject();
        Object model = Model.getModelManagementFactory().getRootModel();
        assertNotNull(model);
        Object classifier = Model.getCoreFactory().buildClass("Foo", model);
        assertNotNull(project.findType("Foo", false));
        // TODO: We should really set up our own profile instead of depending
        // on the default.
        // This depends on the default profile configuration containing the
        // type Integer to test properly.  Otherwise it will get created in
        // the main project, defeating the purpose
        Object intType = project.findType("Integer", false);
        assertNotNull(intType);
        Object attribute = 
            Model.getCoreFactory().buildAttribute2(classifier, intType);
        Model.getCoreHelper().setName(attribute, "profileTypedAttribute");
        checkFoo(project.findType("Foo", false));

        File file = File.createTempFile("ArgoTestCreateSaveAndLoad", ".xmi");
        XmiFilePersister persister = new XmiFilePersister();
        project.preSave();
        persister.save(project, file);
        project.postSave();
        
        Model.getUmlFactory().delete(classifier);

        ProjectManager.getManager().makeEmptyProject();
        
        persister = new XmiFilePersister();
        project = persister.doLoad(file);
        Object attType = checkFoo(project.findType("Foo", false));

        assertEquals("Integer", Model.getFacade().getName(attType));

        file.delete();
    }

    private Object checkFoo(Object theClass) {
        assertNotNull(theClass);
        Object att = Model.getFacade().getAttributes(theClass).get(0);
        assertNotNull(att);
        Object attType = Model.getFacade().getType(att);
        assertNotNull(attType);
        return attType;
    }

    /**
     * This is a regression test for issue 1504.
     * Test loading from minimal XMI file.
     * 
     * @throws Exception if loading project fails
     */
    public void testLoadProject() throws Exception {
        File file = new File("test.xmi");

        XmiFilePersister persister = new XmiFilePersister();

        ProjectManager.getManager().makeEmptyProject();

        persister.doLoad(file);
    }

    /**
     * Test loading a UML1.3 XMI file.
     * 
     * @throws Exception if loading project fails
     */
    public void testLoadProject13() throws Exception {
        String filename = "/testmodels/uml13/Alittlebitofeverything.xmi";
        URL url = TestZargoFilePersister.class.getResource(filename);
        assertTrue("Unintended failure: resource to be tested is not found: "
                + filename + ", converted to URL: " + url, url != null);
        String name = url.getFile();

        XmiFilePersister persister = new XmiFilePersister();

        ProjectManager.getManager().makeEmptyProject();

        persister.doLoad(new File(name));
    }
}
