// $Id$
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

package org.argouml.kernel;

import org.argouml.application.api.Configuration;
import org.argouml.notation.Notation;

import junit.framework.TestCase;

/**
 * Tests for the ProjectSettings.
 *
 * @author michiel
 */
public class TestProjectSettings extends TestCase {

    /**
     * Constructor.
     * 
     * @param arg0 is the name of the test case.
     */
    public TestProjectSettings(String arg0) {
        super(arg0);
    }

    /**
     * Test if the settings are copied correctly 
     * from the default into the project. <p>
     * 
     * For this test, only the shadow-width is used.
     */
    public void testProjectSettingsCreation() {
        Configuration.setInteger(
                Notation.KEY_DEFAULT_SHADOW_WIDTH, 2);
        Project p1 = ProjectManager.getManager().getCurrentProject();
        assertTrue("Default Setting is not copied", 
                p1.getProjectSettings().getDefaultShadowWidthValue() == 2);
        Configuration.setInteger(
                Notation.KEY_DEFAULT_SHADOW_WIDTH, 3);
        assertTrue("Project Setting is altered", 
                p1.getProjectSettings().getDefaultShadowWidthValue() == 2);
        ProjectManager.getManager().removeProject(p1);

        /* In the next line, replacing makeEmptyProject 
         * by getCurrentProject fails the test, 
         * except when run in Eclipse. 
         * MVW: I have no idea why.*/
        Project p2 = ProjectManager.getManager().makeEmptyProject();
        assertTrue("New project does not get Default Setting", 
                p2.getProjectSettings().getDefaultShadowWidthValue() == 3);
        p2.getProjectSettings().setDefaultShadowWidth(4);
        assertTrue("Default is altered by project-setting", 
                Configuration.getInteger(
                        Notation.KEY_DEFAULT_SHADOW_WIDTH) == 3);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        ProjectManager.getManager().setCurrentProject(null);
    }
}
