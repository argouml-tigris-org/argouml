/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

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

package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.JFrame;

import org.argouml.application.api.CommandLineInterface;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.ui.ExceptionDialog;
import org.argouml.ui.UndoableAction;
import org.argouml.uml.reveng.Import;
import org.argouml.uml.reveng.ImportInterface;
import org.argouml.uml.reveng.ImporterManager;
import org.argouml.uml.reveng.ui.ImportStatusScreen;
import org.argouml.util.ArgoFrame;


/** Action to trigger importing from sources.
 * @stereotype singleton
 */
public class ActionImportFromSources extends UndoableAction
        implements CommandLineInterface {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(ActionImportFromSources.class.getName());

    /**
     * The singleton.
     */
    private static final ActionImportFromSources SINGLETON =
        new ActionImportFromSources();

    /**
     *  The constructor.
     */
    public ActionImportFromSources() {
        // this is never downlighted...
        super(Translator.localize("action.import-sources"),
                ResourceLoaderWrapper.lookupIcon("action.import-sources"));
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION,
                Translator.localize("action.import-sources"));
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent event) {
    	super.actionPerformed(event);
    	if (ImporterManager.getInstance().hasImporters()) {
            new Import(ArgoFrame.getFrame());
    	} else {
            LOG.log(Level.INFO,
                    "Import sources dialog not shown: no importers!");
            ExceptionDialog ed = new ExceptionDialog(ArgoFrame.getFrame(),
                Translator.localize("dialog.title.problem"),
                Translator.localize("dialog.import.no-importers.intro"),
                Translator.localize("dialog.import.no-importers.message"));
            ed.setModal(true);
            ed.setVisible(true);
    	}
    }

    /**
     * @return Returns the SINGLETON.
     */
    public static ActionImportFromSources getInstance() {
        return SINGLETON;
    }

    /**
     * Command line command for importing a directory or file.
     *
     * @param argument Formatted string (<importmodule>:<importpath>)
     * @return true if the command was performed successfully.
*/
    public boolean doCommand(String argument) {
        if (argument == null) {
            LOG.log(Level.SEVERE, "An argument has to be provided.");
            return false;
        }
        int index = argument.indexOf(':');
        if (index == -1 || argument.length() <= index) {
            LOG.log(Level.SEVERE,
                    "Argument must be <importmodule>:<importpath>");
            return false;
        }
        Import imp = new Import(null);
        Collection languages = imp.getLanguages();
        if (languages == null || languages.isEmpty()) {
            LOG.log(Level.SEVERE, "No importers available.");
            return false;
        }
        String importerName = argument.substring(0, index);
        ImportInterface importer = imp.getImporter(importerName);
        if (importer == null) {
            LOG.log(Level.SEVERE,
                    "No import support for language " + importerName);
            return false;
        }
        imp.setCurrentModule(importer);
        File file = new File(argument.substring(index + 1));
        if (!file.exists()) {
            LOG.log(Level.SEVERE,
                    "The specified file/directory doesn't exist.");
            return false;
        }
        imp.setFiles(new File[]{file});
        ImportStatusScreen iss =
            new ImportStatusScreen(new JFrame(), "Importing", "Splash");
        imp.doImport(iss);
        return true;
    }
}
/* end class ActionImportFromSources */
