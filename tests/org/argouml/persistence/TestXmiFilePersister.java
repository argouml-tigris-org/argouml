// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

import junit.framework.TestCase;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;

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

    /**
     * This is a regression test for issue 1504.
     * Test basic serialization to XMI file.
     */
    public void testSave() {

        try {
            Project p = ProjectManager.getManager().makeEmptyProject();
            Object clazz = Model.getCoreFactory().buildClass(p.getModel());
            Object model =
                ProjectManager.getManager()
                	.getCurrentProject().getModel();
            Object voidType =
                ProjectManager.getManager()
                	.getCurrentProject().findType("void");
            Object oper =
                Model.getCoreFactory().buildOperation(clazz, model, voidType);
            Model.getCoreHelper().setType(
                    Model.getFacade().getParameter(oper, 0),
                    p.findType("String"));
            File file = new File("test.xmi");
            XmiFilePersister persister = new XmiFilePersister();
            p.preSave();
            persister.save(p, file);
            p.postSave();
        } catch (SaveException e) {
            fail("Save resulted in an exception");
        }
    }

    /**
     * This is a regression test for issue 1504.
     * Test loading from minimal XMI file.
     */
    public void testLoadProject() {

        try {
            File file = new File("test.xmi");

            XmiFilePersister persister = new XmiFilePersister();

            ProjectManager.getManager().makeEmptyProject();

            persister.doLoad(file);
        } catch (OpenException e) {
            fail("Load resulted in an exception");
        }
    }
}
