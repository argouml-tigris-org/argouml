/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    linus
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2004-2008 The Regents of the University of California. All
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

import javax.swing.JPanel;

import junit.framework.TestCase;

import org.argouml.application.InitSubSystemForTest;
import org.argouml.model.InitializeModel;
import org.argouml.notation.providers.uml.InitNotationUml;
import org.argouml.profile.init.InitProfileSubsystem;
import org.argouml.ui.ProjectBrowser;
import org.argouml.util.CheckMain;

/**
 * @author Linus Tolke
 */
public class GUITestActionSaveGraphics extends TestCase {

    private static final String OUTPUT_FILE = "test-out.gif";

    /**
     * Constructor for GUITestActionSaveGraphics.
     *
     * @param arg0 The name of the test case.
     */
    public GUITestActionSaveGraphics(String arg0) {
        super(arg0);
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeDefault();
        new InitProfileSubsystem().init();
        ProjectBrowser.makeInstance(null, true, new JPanel());
	InitSubSystemForTest.initSubsystem(new InitUmlUI());
        InitSubSystemForTest.initSubsystem(new InitNotationUml());
    }

    /**
     * Test dumping a diagram from a project with contents.
     */
    public void testProjectWithContents() {
        File file =
            CheckMain.getTestModel(
		    "testmodels/uml14/GUITestPropertyPanels.zargo");

        new File(OUTPUT_FILE).delete();

        List<String> c = new ArrayList<String>();
        c.add("org.argouml.uml.ui.ActionOpenProject=" + file.getAbsolutePath());
        c.add("org.argouml.uml.ui.ActionSaveGraphics=" + OUTPUT_FILE);
        CheckMain.doCommand(c);

        assertTrue(new File(OUTPUT_FILE).exists());

        new File(OUTPUT_FILE).delete();
    }
}

