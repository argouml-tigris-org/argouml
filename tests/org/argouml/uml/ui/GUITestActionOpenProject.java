// $Id$
// Copyright (c) 2004-2006 The Regents of the University of California. All
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

import junit.framework.TestCase;

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

    /**
     * Test an empty project.
     */
    public void testEmptyProject() {
        File file
            = CheckMain.getTestModel("testmodels/EmptyProject0161.zargo");

        CheckMain.callMain(new String[] {
            "-nosplash",
            "-command",
            "org.argouml.uml.ui.ActionOpenProject=" + file.getAbsolutePath(),
        });

        assertEquals("EmptyProject0161",
		ProjectManager.getManager().getCurrentProject().getBaseName());
    }

    /**
     * Test a project with contents.
     */
    public void testProjectWithContents() {
        File file =
            CheckMain.getTestModel("testmodels/GUITestPropertyPanels.zargo");

        CheckMain.callMain(new String[] {
            "-nosplash",
            "-command",
            "org.argouml.uml.ui.ActionOpenProject=" + file.getAbsolutePath(),
        });

        assertEquals("GUITestPropertyPanels",
		ProjectManager.getManager().getCurrentProject().getBaseName());
    }
}

