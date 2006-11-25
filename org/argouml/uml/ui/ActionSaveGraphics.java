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

package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.argouml.application.api.CommandLineInterface;
import org.argouml.application.api.Configuration;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.ArgoFrame;
import org.argouml.ui.ExceptionDialog;
import org.argouml.ui.ProjectBrowser;
import org.argouml.util.SuffixFilter;
import org.tigris.gef.base.CmdSaveGraphics;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.util.Util;


/**
 * Wraps a CmdSaveGIF or CmdSave(E)PS to allow selection of an output file.
 */
public class ActionSaveGraphics extends AbstractAction
        implements CommandLineInterface {

    private static final long serialVersionUID = 3062674953320109889L;
    
    private static final Logger LOG =
        Logger.getLogger(ActionSaveGraphics.class);

    /**
     * Constructor for this action.
     */
    public ActionSaveGraphics() {
        super(Translator.localize("action.save-graphics"),
                ResourceLoaderWrapper.lookupIcon("action.save-graphics"));
    }

    /*
     * @see AbstractAction#actionPerformed(ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
        trySave(); //TODO: what to do with the return value?
    }

    /**
     * Method that does almost everything in this class.<p>
     *
     * @return true if all went well.
     */
    private boolean trySave() {
        Object target =
            ProjectManager.getManager().getCurrentProject().getActiveDiagram();

        if (!(target instanceof Diagram)) {
            return false;
        }

        String defaultName = ((Diagram) target).getName();
        defaultName = Util.stripJunk(defaultName);

        Project p =  ProjectManager.getManager().getCurrentProject();
        SaveGraphicsManager sgm = SaveGraphicsManager.getInstance();
        try {
            JFileChooser chooser = null;

            if (p != null
            	&& p.getURI() != null
            	&& p.getURI().toURL().getFile().length() > 0) {

                chooser = new JFileChooser(p.getURI().toURL().getFile());
            }

            if (chooser == null) {
                chooser = new JFileChooser();
            }

            Object[] s = {defaultName };
            chooser.setDialogTitle(
                    Translator.messageFormat("filechooser.save-graphics", s));
            // Only specified format are allowed.
            chooser.setAcceptAllFileFilterUsed(false);
            sgm.setFileChooserFilters(chooser, defaultName);

            String fn = Configuration.getString(
                    SaveGraphicsManager.KEY_SAVE_GRAPHICS_PATH);
            if (fn.length() > 0) {
                chooser.setSelectedFile(new File(fn));
            }

            int retval = chooser.showSaveDialog(ArgoFrame.getInstance());
            if (retval == JFileChooser.APPROVE_OPTION) {
                File theFile = chooser.getSelectedFile();
                if (theFile != null) {
                    String path = theFile.getPath();
                    Configuration.setString(
                            SaveGraphicsManager.KEY_SAVE_GRAPHICS_PATH,
                            path);

                    theFile = new File(theFile.getParentFile(),
                            sgm.fixExtension(theFile.getName()));
                    String suffix = sgm.getFilterFromFileName(theFile.getName())
                        .getSuffix();
                    return doSave(theFile, suffix, true);
                }
            }
        } catch (OutOfMemoryError e) {
            new ExceptionDialog(ArgoFrame.getInstance(),
                "You have run out of memory. "
                + "Close down ArgoUML and restart with a larger heap size.", e);
        } catch (Exception e) {
            new ExceptionDialog(ArgoFrame.getInstance(), e);
            LOG.error("Got some exception", e);
        }

        return false;
    }

    /**
     * Actually do the saving of the graphics file.
     *
     * @return true if it was successful.
     * @param theFile is the file that we are writing to
     * @param suffix is the suffix. Used for deciding what format the file
     * shall have.
     * @param useUI is true if we are supposed to use the UI e.g. to warn
     *              the user that we are replacing an old file.
     */
    private boolean doSave(File theFile,
			   String suffix, boolean useUI)
	throws FileNotFoundException, IOException {

        SaveGraphicsManager sgm = SaveGraphicsManager.getInstance();
        CmdSaveGraphics cmd = null;

        cmd = sgm.getSaveCommandBySuffix(suffix);
        if (cmd == null) {
            return false;
        }

        if (useUI) {
            ProjectBrowser.getInstance().showStatus(
                            "Writing " + theFile + "...");
        }
	if (theFile.exists() && useUI) {
	    int response = JOptionPane.showConfirmDialog(
                ArgoFrame.getInstance(),
                Translator.messageFormat("optionpane.confirm-overwrite",
                        new Object[] {theFile}),
                Translator.localize("optionpane.confirm-overwrite-title"),
                JOptionPane.YES_NO_OPTION);
	    if (response != JOptionPane.YES_OPTION) {
		return false;
	    }
	}
	FileOutputStream fo = new FileOutputStream(theFile);
	cmd.setStream(fo);
        cmd.setScale(Configuration.getInteger(
                SaveGraphicsManager.KEY_GRAPHICS_RESOLUTION, 1));
	cmd.doIt();
	fo.close();
        if (useUI) {
            ProjectBrowser.getInstance().showStatus("Wrote " + theFile);
        }
	return true;
    }


    /**
     * Execute this action from the command line.
     *
     * TODO: The underlying GEF library relies on Acme that doesn't allow
     * us to create these files unless there is a window showing. For this
     * reason I have had to split the performing of commands in
     * {@link org.argouml.application.Main#main(String[])} so that we can,
     * by not supplying the -batch option, run these commands
     * with the window showing. Hopefully this can eventually be fixed.
     *
     * @see org.argouml.application.api.CommandLineInterface#doCommand(String)
     * @param argument is the file name that we save to.
     * @return true if it is OK.
     */
    public boolean doCommand(String argument) {
	File file = new File(argument);
	String suffix = SuffixFilter.getExtension(file);
	if (suffix == null) {
	    return false;
	}

	try {
	    return doSave(file, suffix, false);
	} catch (FileNotFoundException e) {
	    LOG.error("File not found error when writing.", e);
	} catch (IOException e) {
	    LOG.error("IO error when writing.", e);
	}
	return false;
    }
} /* end class ActionSaveGraphics */
