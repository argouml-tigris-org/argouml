// $Id:PrintManager.java 12546 2007-05-05 16:54:40Z linus $
// Copyright (c) 2004-2007 The Regents of the University of California. All
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

package org.argouml.ui.cmd;

import org.argouml.kernel.ProjectManager;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.PrintAction;

/**
 * print the current active diagram.
 */
public class PrintManager {

    private final PrintAction printCmd = new PrintAction();

    private static final PrintManager INSTANCE = new PrintManager();

    /**
     * @return the instance of the printmanager
     */
    public static PrintManager getInstance() {
        return INSTANCE;
    }

    /**
     * The constructor.
     */
    private PrintManager() {
        // instantiation not allowed
    }

    /**
     * Print the active diagram
     */
    public void print() {
        Object target = ProjectManager.getManager().getCurrentProject()
                .getActiveDiagram();
        if (target instanceof Diagram) {
            printCmd.actionPerformed(null);
        }
    }

    /**
     * Show the page setup dialog.
     */
    public void showPageSetupDialog() {
        printCmd.doPageSetup();
    }
} /* end class ActionPrint */
