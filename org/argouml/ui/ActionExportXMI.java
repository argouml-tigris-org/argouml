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

package org.argouml.ui;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import org.argouml.application.api.Configuration;
import org.argouml.i18n.Translator;
import org.argouml.persistence.PersistenceManager;

/**
 * Exports the xmi of a project to a file choosen by the user.
 * @author jaap.branderhorst@xs4all.nl
 * Jun 7, 2003
 */
public final class ActionExportXMI extends AbstractAction {

    /**
     * The constructor.
     */
    public ActionExportXMI() {
        super(Translator.localize("action.export-project-as-xmi"));
    }

    /**
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        PersistenceManager pm = PersistenceManager.getInstance();
        // show a chooser dialog for the file name, only xmi is allowed
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(Translator.localize(
				       "action.export-project-as-xmi"));
        chooser.setApproveButtonText(Translator.localize(
					     "filechooser.export"));
        chooser.setAcceptAllFileFilterUsed(true);
        pm.setXmiFileChooserFilter(chooser);

        String fn =
            Configuration.getString(
                PersistenceManager.KEY_PROJECT_NAME_PATH);
        if (fn.length() > 0) {
            fn = PersistenceManager.getInstance().getBaseName(fn);
            chooser.setSelectedFile(new File(fn));
        }

        int result = chooser.showSaveDialog(ProjectBrowser.getInstance());
        if (result == JFileChooser.APPROVE_OPTION) {
            File theFile = chooser.getSelectedFile();
            if (theFile != null) {
                String name = theFile.getName();
                Configuration.setString(
                        PersistenceManager.KEY_PROJECT_NAME_PATH,
                        PersistenceManager.getInstance().getBaseName(
                                theFile.getPath()));
                name = pm.fixXmiExtension(name);
                theFile = new File(theFile.getParent(), name);
                ProjectBrowser.getInstance().trySaveWithProgressMonitor(
                        false, theFile);
            }
        }
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -3445739054369264482L;
}
