/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2007-2008 The Regents of the University of California. All
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

package org.argouml.ui.explorer;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.argouml.application.helpers.ApplicationVersion;
import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.model.UmlException;
import org.argouml.model.XmiWriter;
import org.argouml.persistence.PersistenceManager;
import org.argouml.persistence.ProjectFileView;
import org.argouml.persistence.UmlFilePersister;
import org.argouml.profile.Profile;
import org.argouml.profile.ProfileException;
import org.argouml.util.ArgoFrame;

/**
 * Exports the model of a selected profile as XMI.
 *
 * @author Marcos Aurélio
 */
public class ActionExportProfileXMI extends AbstractAction {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(ActionExportProfileXMI.class.getName());

    private Profile selectedProfile;

    /**
     * Default Constructor
     *
     * @param profile the selected profile
     */
    public ActionExportProfileXMI(Profile profile) {
        super(Translator.localize("action.export-profile-as-xmi"));
        this.selectedProfile = profile;
    }


    public void actionPerformed(ActionEvent arg0) {
        try {
            final Collection profilePackages =
                selectedProfile.getProfilePackages();
            final Object model = profilePackages.iterator().next();

            if (model != null) {
                File destiny = getTargetFile();
                if (destiny != null) {
                    saveModel(destiny, model);
                }
            }
        } catch (ProfileException e) {
            // TODO: We should be giving the user more direct feedback
            LOG.log(Level.SEVERE, "Exception", e);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Exception", e);
        } catch (UmlException e) {
            LOG.log(Level.SEVERE, "Exception", e);
        }
    }


    private void saveModel(File destiny, Object model) throws IOException,
            UmlException {
        OutputStream stream = new FileOutputStream(destiny);
        XmiWriter xmiWriter =
            Model.getXmiWriter(model, stream,
                    ApplicationVersion.getVersion() + "("
                        + UmlFilePersister.PERSISTENCE_VERSION + ")");
        xmiWriter.write();
    }

    private File getTargetFile() {
        // show a chooser dialog for the file name, only xmi is allowed
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(Translator.localize(
                                       "action.export-profile-as-xmi"));
        chooser.setFileView(ProjectFileView.getInstance());
        chooser.setApproveButtonText(Translator.localize(
                                             "filechooser.export"));
        chooser.setAcceptAllFileFilterUsed(true);
        chooser.setFileFilter(new FileFilter() {

            public boolean accept(File file) {
                return file.isDirectory() || isXmiFile(file);
            }



            public String getDescription() {
                return "*.XMI";
            }

        });

        String fn =
            Configuration.getString(
                PersistenceManager.KEY_PROJECT_NAME_PATH);
        if (fn.length() > 0) {
            fn = PersistenceManager.getInstance().getBaseName(fn);
            chooser.setSelectedFile(new File(fn));
        }

        int result = chooser.showSaveDialog(ArgoFrame.getFrame());
        if (result == JFileChooser.APPROVE_OPTION) {
            File theFile = chooser.getSelectedFile();
            if (theFile != null) {
                if (!theFile.getName().toUpperCase().endsWith(".XMI")) {
                    theFile = new File(theFile.getAbsolutePath() + ".XMI");
                }
                return theFile;
            }
        }

        return null;
    }

    private static boolean isXmiFile(File file) {
        return file.isFile()
                && (file.getName().toLowerCase().endsWith(".xml")
                        || file.getName().toLowerCase().endsWith(".xmi"));
    }
}
