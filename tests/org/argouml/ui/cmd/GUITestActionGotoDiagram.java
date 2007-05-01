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

package org.argouml.ui.cmd;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.argouml.util.CheckMain;

/**
 * Tests the change of diagram from the command line.
 */
public class GUITestActionGotoDiagram extends TestCase {
    private static final String OUTPUT_FILE1 = "test-out1.gif";
    private static final String OUTPUT_FILE2 = "test-out2.gif";

    /**
     * Constructor for GUITestActionGotoDiagram.
     * @param name The name of the test case.
     */
    public GUITestActionGotoDiagram(String name) {
        super(name);
    }

    /**
     * Test dumping a named diagram from a project with contents.
     */
    public void testProjectWithContents() {
        File file =
	    CheckMain.getTestModel(
                    "testmodels/uml14/GUITestPropertyPanels.zargo");

        List<String> c = new ArrayList<String>();
        c.add("org.argouml.uml.ui.ActionOpenProject=" + file.toString());
        c.add("org.argouml.uml.ui.ActionSaveGraphics=" + OUTPUT_FILE1);
        c.add("org.argouml.ui.cmd.ActionGotoDiagram=Deployment Diagram 1");
        c.add("org.argouml.uml.ui.ActionSaveGraphics=" + OUTPUT_FILE2);
        CheckMain.doCommand(c);

        assertTrue(new File(OUTPUT_FILE1).exists());
        assertTrue(new File(OUTPUT_FILE2).exists());

        // TODO: We could check that the contents of the files differ.

        new File(OUTPUT_FILE1).delete();
        new File(OUTPUT_FILE2).delete();
    }
}

