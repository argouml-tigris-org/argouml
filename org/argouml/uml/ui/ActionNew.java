// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import java.awt.Window;
import java.awt.event.ActionEvent;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.cognitive.Designer;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.FindDialog;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.targetmanager.TargetManager;

/**
 * Action to trigger creation of a new project.
 *
 * @stereotype singleton
 */
public class ActionNew extends ActionFileOperations {

    ////////////////////////////////////////////////////////////////
    // static variables

    /**
     * The singleton.
     */
    public static final ActionNew SINGLETON = new ActionNew(); 

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * The constructor.
     */
    protected ActionNew() { 
        super(Translator.localize("action.new"), ResourceLoaderWrapper
                .lookupIconResource(Translator.getImageBinding("New"),
                        Translator.localize("New")));
    }

    ////////////////////////////////////////////////////////////////
    // main methods

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        Project p = ProjectManager.getManager().getCurrentProject();
        
        if (!askConfirmationAndSave()) return;
        
        // TODO yuk! Why is this needed? In fact how can anyone select
        // the new action if there is a dialog?
	// We should remove all open dialogs. They have as parent the
	// ProjectBrowser
	Window[] windows = ProjectBrowser.getInstance().getOwnedWindows();
	for (int i = 0; i < windows.length; i++) {
	    windows[i].dispose();
	}

	Designer.disableCritiquing();
	Designer.clearCritiquing();
	// clean the history
	TargetManager.getInstance().cleanHistory();
        p.remove();
	p = ProjectManager.getManager().makeEmptyProject();
	FindDialog.getInstance().doClearTabs();
	FindDialog.getInstance().doResetFields();
	TargetManager.getInstance().setTarget(p.getDiagrams().toArray()[0]);
	Designer.enableCritiquing();
    }
} /* end class ActionNew */
