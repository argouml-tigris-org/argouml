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

// Copyright (c) 1996-2008 The Regents of the University of California. All
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.argouml.application.api.CommandLineInterface;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoStatusEvent;
import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramUtils;
import org.argouml.util.ArgoFrame;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.SaveGraphicsAction;
import org.tigris.gef.util.Util;

/**
 * Wraps a SaveGraphicsAction allow selection of an output directory to which
 * all diagrams will be written. Introduced thanks to issue 2126. Saves diagrams
 * only using the default format.
 * <p>
 *
 * TODO: Add a user choice for other formats (PNG, SVG,...) <p>
 *
 * @author Leonardo Souza Mario Bueno (lsbueno@tigris.org)
 */

public class ActionSaveAllGraphics extends AbstractAction
    implements CommandLineInterface {

    private static final Logger LOG =
        Logger.getLogger(ActionSaveAllGraphics.class.getName());

    private boolean overwrite;

    /**
     * The constructor.
     *
     */
    public ActionSaveAllGraphics() {
        super(Translator.localize("action.save-all-graphics"),
                null);
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION,
                Translator.localize("action.save-all-graphics"));
    }

    public void actionPerformed( ActionEvent ae ) {
        trySave( false );
    }

    /**
     * @param canOverwrite true if we can overwrite without asking
     * @return success
     */
    public boolean trySave(boolean canOverwrite) {
        return trySave(canOverwrite, null);
    }

    /**
     * @param canOverwrite
     *            true if we can overwrite without asking
     * @param directory
     *            directory to save to. If null, user will be prompted to
     *            choose.
     * @return success save status
     */
    public boolean trySave(boolean canOverwrite, File directory) {
        overwrite = canOverwrite;
        Project p =  ProjectManager.getManager().getCurrentProject();
        TargetManager tm = TargetManager.getInstance();
        File saveDir = (directory != null) ? directory : getSaveDir(p);
        if (saveDir == null) {
            /* The user cancelled! */
            return false;
        }
        boolean okSoFar = true;
        ArgoDiagram activeDiagram = DiagramUtils.getActiveDiagram();
        for (ArgoDiagram d : p.getDiagramList()) {
            tm.setTarget(d);
            okSoFar = trySaveDiagram(d, saveDir);
            if (!okSoFar) {
                break;
            }
        }
        tm.setTarget(activeDiagram);
        return okSoFar;
    }

    /**
     * @param target the diagram
     * @param saveDir the directory to save to
     * @return continue exporting diagrams if true
     */
    protected boolean trySaveDiagram(Object target,
            File saveDir) {
        if ( target instanceof Diagram ) {
            String defaultName = ((Diagram) target).getName();
            defaultName = Util.stripJunk(defaultName);
            // TODO: It's probably worthwhile to abstract and factor
            // this chooser and directory stuff. More file handling is
            // coming, I'm sure.
            try {
                File theFile = new File(saveDir, defaultName + "."
                    + SaveGraphicsManager.getInstance().getDefaultSuffix());
                String name = theFile.getName();
                String path = theFile.getParent();
                SaveGraphicsAction cmd = SaveGraphicsManager.getInstance()
                    .getSaveActionBySuffix(
                        SaveGraphicsManager.getInstance().getDefaultSuffix());
                if (cmd == null) {
                    showStatus("Unknown graphics file type with extension "
                            + SaveGraphicsManager.getInstance()
                                    .getDefaultSuffix());
                    return false;
                }
                showStatus( "Writing " + path + name + "..." );
                boolean result = saveGraphicsToFile(theFile, cmd);
                showStatus( "Wrote " + path + name );
                return result;
            }
            catch ( FileNotFoundException ignore ) {
                LOG.log(Level.SEVERE, "got a FileNotFoundException", ignore);
            }
            catch ( IOException ignore ) {
                LOG.log(Level.SEVERE, "got an IOException", ignore);
            }
        }
        return false;
    }


    /**
     * @param p the current project
     * @return returns null if the user did not approve his choice
     */
    protected File getSaveDir(Project p) {
        JFileChooser chooser = getFileChooser(p);

        String fn = Configuration.getString(
                SaveGraphicsManager.KEY_SAVEALL_GRAPHICS_PATH);
        if (fn.length() > 0) {
            chooser.setSelectedFile(new File(fn));
        }

        int retval = chooser.showSaveDialog(ArgoFrame.getFrame());

        if ( retval == JFileChooser.APPROVE_OPTION ) {
            File theFile = chooser.getSelectedFile();
            String path = theFile.getPath();
            Configuration.setString(
                    SaveGraphicsManager.KEY_SAVEALL_GRAPHICS_PATH,
                    path);
            return theFile;
        }
        return null;
    }

    /**
     * @param theFile the file to write
     * @param cmd the action to execute to save the graphics
     * @return continue exporting diagrams if true
     * @throws IOException
     */
    private boolean saveGraphicsToFile(File theFile, SaveGraphicsAction cmd)
        throws IOException {
        if ( theFile.exists() && !overwrite ) {
            String message = Translator.messageFormat(
                    "optionpane.confirm-overwrite",
                    new Object[] {theFile});
            String title = Translator.localize(
                    "optionpane.confirm-overwrite-title");
            //Custom button text:
            Object[] options =
            {Translator.localize(
                    "optionpane.confirm-overwrite.overwrite"), // 0
             Translator.localize(
                    "optionpane.confirm-overwrite.overwrite-all"), // 1
             Translator.localize(
                    "optionpane.confirm-overwrite.skip-this-one"), // 2
             Translator.localize(
                    "optionpane.confirm-overwrite.cancel")}; // 3

            int response =
		JOptionPane.showOptionDialog(ArgoFrame.getFrame(),
                    message,
                    title,
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,     //do not use a custom Icon
                    options,  //the titles of buttons
                    options[0]); //default button title

            if (response == 1) {
                overwrite = true;
            }
            if (response == 2) {
                return true;
            }
            if (response == 3) {
                return false;
            }
            if (response == JOptionPane.CLOSED_OPTION) {
                return false;
            }
        }
        FileOutputStream fo = null;
        try {
            fo = new FileOutputStream( theFile );
            cmd.setStream(fo);
            cmd.setScale(Configuration.getInteger(
                    SaveGraphicsManager.KEY_GRAPHICS_RESOLUTION, 1));
            cmd.actionPerformed(null);
        } finally {
            if (fo != null) {
                fo.close();
            }
        }
        return true;
    }

    private JFileChooser getFileChooser(Project p) {
        JFileChooser chooser = null;
        try {
	    if ( p != null
                && p.getURI() != null
                && p.getURI().toURL().getFile().length() > 0 ) {
	        chooser = new JFileChooser(p.getURI().toURL().getFile());
            }
        }
        catch ( MalformedURLException ex ) {
            LOG.log(Level.SEVERE, "exception in opening JFileChooser", ex);
        }

        if ( chooser == null ) {
            chooser = new JFileChooser();
        }
        chooser.setDialogTitle(
                Translator.localize("filechooser.save-all-graphics"));
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        return chooser;
    }

    private void showStatus(String text) {
        ArgoEventPump.fireEvent(new ArgoStatusEvent(
                ArgoEventTypes.STATUS_TEXT, this, text));
    }

    /**
     * Execute this action from the command line.
     *
     * @see org.argouml.application.api.CommandLineInterface#doCommand(String)
     * @param argument is the directory name that we save to.
     * @return true if it is OK.
     */
    public boolean doCommand(String argument) {
        File dir = new File(argument);
        if (!dir.exists() || !dir.isDirectory()) {
            LOG.log(Level.SEVERE, "The argument must be a path to an existing directory.");
            return false;
        }
        boolean result = true;
        for (Project p : ProjectManager.getManager().getOpenProjects()) {
            TargetManager tm = TargetManager.getInstance();
            for (ArgoDiagram d : p.getDiagramList()) {
                tm.setTarget(d);
                if (!trySaveDiagram(d, dir)) {
                    result = false;
                }
            }
        }
        return result;
    }
}
