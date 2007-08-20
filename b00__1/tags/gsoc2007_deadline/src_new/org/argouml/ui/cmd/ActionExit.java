// $Id:ActionExit.java 11510 2006-11-24 07:37:59Z tfmorris $
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

package org.argouml.ui.cmd;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.argouml.application.api.CommandLineInterface;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.ui.ProjectBrowser;

/**
 * Action to exit ArgoUML.
 * If the project is dirty, then ask the user if he wants to save first.
 */
public class ActionExit extends AbstractAction
    implements CommandLineInterface {

    /**
     * The constructor.
     */
    public ActionExit() {
        super(Translator.localize("action.exit"),
                ResourceLoaderWrapper.lookupIcon("action.exit"));
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
        ProjectBrowser.getInstance().tryExit();
    }


    /**
     * Execute this action from the command line.
     *
     * @param argument is not used.
     * @return true if it is OK.
     *
     * @see org.argouml.application.api.CommandLineInterface#doCommand(java.lang.String)
     */
    public boolean doCommand(String argument) {
        System.exit(0);
        return true;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -6264722939329644183L;
} /* end class ActionExit */
