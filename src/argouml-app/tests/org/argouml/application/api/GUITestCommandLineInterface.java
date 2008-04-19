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

package org.argouml.application.api;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import org.argouml.util.CheckMain;

/**
 * This is to test the CommandLineInterface functions.<p>
 *
 * Note that:<ul>
 * <li>We can't do the real ActionExit() here because that would exit
 *     the jvm running the test case.
 * <li>As a consequence, we can't use the -batch command line option
 *     since that would invoke ActionExit().
 * <li>After calling main we must reset the ArgoSecurityManager or else
 *     the test case will not be able to exit. For this reason use
 *     {@link CheckMain#doCommand(List)} instead of a direct call.
 * @author Linus Tolke
 */
public class GUITestCommandLineInterface extends TestCase {
    /**
     * Constructor.
     *
     * @param name the name of the test case
     */
    public GUITestCommandLineInterface(String name) {
        super(name);
    }

    /**
     * Test the simplest possible action.
     */
    public void testActionExit() {
        List<String> c = new ArrayList<String>();
        c.add("org.argouml.application.api.FalseActionExit");
        CheckMain.doCommand(c);

	assertTrue(FalseActionExit.getLast().isExited());
    }
}

