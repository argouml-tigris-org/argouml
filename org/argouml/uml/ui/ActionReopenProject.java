// $Id$
// Copyright (c) 2003-2004 The Regents of the University of California. All
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

import org.argouml.ui.*;
import org.argouml.i18n.Translator;
import org.argouml.kernel.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.JOptionPane;
import java.text.MessageFormat;

/**
 * Reopens a project with respect of the calling event handler - should be 
 * used with menu item
 *
 * @author  Frank Jelinek
 * @since 10. November 2003 (0.15.2)
 */

public class ActionReopenProject extends UMLAction {

    String _filename;
    
    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * Constructor.
     */
    public ActionReopenProject(String filename) {
	super("action.reopen-project");
	_filename = filename;
    }

    ////////////////////////////////////////////////////////////////
    // main methods

    /**
     * returns the filename for comparing during menu creation
     */
    public String getFilename() {
        return _filename;
    }
    
    /** 
     * Performs the save and reload of a project.
     *
     * @param e e should old the event and the eventsource. Event
     * source is the menu item, the text is used for opening the
     * project
     */
    public void actionPerformed(ActionEvent e) {

        
        // actually copy from ActionOpenProject, there should be a better way
        ProjectBrowser pb = ProjectBrowser.getInstance();
        Project p = ProjectManager.getManager().getCurrentProject();

        if (p != null && p.needsSave()) {
            String t =
                MessageFormat.format(
                        Translator.localize(
				"Actions",
				"optionpane.open-project-save-changes-to"),
			new Object[] {
			    p.getName()
			});

            int response =
                JOptionPane.showConfirmDialog(
					      pb,
					      t,
					      t,
					      JOptionPane.YES_NO_CANCEL_OPTION);

            if (response == JOptionPane.CANCEL_OPTION 
	        || response == JOptionPane.CLOSED_OPTION)
                return;
            if (response == JOptionPane.YES_OPTION) {
                boolean safe = false;

                if (ActionSaveProject.SINGLETON.shouldBeEnabled()) {
                    safe = ActionSaveProject.SINGLETON.trySave(true);
                }
                if (!safe) {
                    safe = ActionSaveProjectAs.SINGLETON.trySave(false);
                }
                if (!safe)
                    return;
            }
        }
        
        // load of the new project
        // just reuse of the ActionOpen object
        File toOpen = new File(_filename);;
        
        try {
            ActionOpenProject openProjectHandler =
		new ActionOpenProject();
            openProjectHandler.loadProject(toOpen.toURL());
        }
        catch ( java.net.MalformedURLException ex) {
            cat.error("got an URLException in ActionReopenProject", ex);
        }
    }
}
