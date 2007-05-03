// $Id$
// Copyright (c) 2004-2007 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import org.argouml.model.InitializeModel;

import org.argouml.kernel.ProjectManager;
import org.argouml.util.CheckMain;

/**
 * Testing opening a project from the command line.
 *
 * @author Linus Tolke
 */
public class GUITestActionOpenProject extends TestCase {
    /**
     * @param name The name of the test case.
     */
    public GUITestActionOpenProject(String name) {
        super(name);
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
	super.setUp();
        InitializeModel.initializeDefault();
    }

    /**
     * Test an empty 1.3 project.
     */
    public void testEmptyProject13() {
	doTestActionOpenProject("testmodels/uml13/EmptyProject0161.zargo",
				"EmptyProject0161");
    }

    /**
     * Test a 1.3 project with contents.
     */
    public void testProjectWithContents13() {
        doTestActionOpenProject("testmodels/uml13/GUITestPropertyPanels.zargo",
				"GUITestPropertyPanels");
    }

    /**
     * Test an empty 1.4 project.
     */
    public void testEmptyProject14() {
	doTestActionOpenProject("testmodels/uml14/EmptyProject024.zargo",
				"EmptyProject024");
    }

    /**
     * Test a 1.4 project with contents.
     */
    public void testProjectWithContents14() {
        doTestActionOpenProject("testmodels/uml14/GUITestPropertyPanels.zargo",
				"GUITestPropertyPanels");
    }

    /**
     * Do the actual testing.
     */
    private void doTestActionOpenProject(String fileName, String projectName) {
        File file
            = CheckMain.getTestModel(fileName);

        List<String> c = new ArrayList<String>();
        c.add("org.argouml.uml.ui.ActionOpenProject=" + file.getAbsolutePath());
        CheckMain.doCommand(c);

        assertEquals(projectName,
		ProjectManager.getManager().getCurrentProject().getBaseName());
    }
}

