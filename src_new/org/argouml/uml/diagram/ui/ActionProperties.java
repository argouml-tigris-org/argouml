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

package org.argouml.uml.diagram.ui;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.ui.ProjectBrowser;
import org.tigris.gef.undo.UndoableAction;

/** Action to select the properties tab.
 * @stereotype singleton
 */
public class ActionProperties extends UndoableAction {

    ////////////////////////////////////////////////////////////////
    // static variables

    private static ActionProperties singleton = new ActionProperties();


    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * The constructor.
     */
    protected ActionProperties() {
        super(Translator.localize("action.properties"),
                ResourceLoaderWrapper.lookupIcon("action.properties"));
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION, 
                Translator.localize("action.properties"));
        String localMnemonic =
            Translator.localize("action.properties.mnemonic");
        if (localMnemonic != null && localMnemonic.length() == 1) {
            putValue(Action.MNEMONIC_KEY, new Integer(localMnemonic.charAt(0)));
        }
    }


    ////////////////////////////////////////////////////////////////
    // main methods

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
    	super.actionPerformed(ae);
	ProjectBrowser pb = ProjectBrowser.getInstance();
	if (pb == null) return;
	pb.selectTabNamed("action.properties");
    }

    /**
     * @return always true (the action is always enabled)
     * @see org.tigris.gef.undo.UndoableAction#isEnabled()
     */
    public boolean isEnabled() {
	return true;
    }


    /**
     * @return Returns the singleton.
     */
    public static ActionProperties getSingleton() {
        return singleton;
    }
} /* end class ActionProperties */

