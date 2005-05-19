// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;
import org.argouml.application.api.CommandLineInterface;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.ExceptionDialog;
import org.argouml.ui.ProjectBrowser;
import org.argouml.util.FileFilters;
import org.argouml.util.SuffixFilter;
import org.tigris.gef.base.CmdSaveEPS;
import org.tigris.gef.base.CmdSaveGIF;
import org.tigris.gef.base.CmdSaveGraphics;
import org.tigris.gef.base.CmdSavePNG;
import org.tigris.gef.base.CmdSavePS;
import org.tigris.gef.base.CmdSaveSVG;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Editor;
import org.tigris.gef.persistence.PostscriptWriter;
import org.tigris.gef.util.Util;


/**
 * Wraps a CmdSaveGIF or CmdSave(E)PS to allow selection of an output file.
 */
public class ActionSaveGraphics
        extends UMLAction
        implements CommandLineInterface {

    private static final Logger LOG =
        Logger.getLogger(ActionSaveGraphics.class);
    
    private static final String PS_SFX = FileFilters.PS_FILTER.getSuffix();
    private static final String EPS_SFX = FileFilters.EPS_FILTER.getSuffix();
    private static final String GIF_SFX = FileFilters.GIF_FILTER.getSuffix();
    private static final String PNG_SFX = FileFilters.PNG_FILTER.getSuffix();
    private static final String SVG_SFX = FileFilters.SVG_FILTER.getSuffix();

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * Constructor for this action.
     */
    public ActionSaveGraphics() {
        super("action.save-graphics", NO_ICON);
    }


    ////////////////////////////////////////////////////////////////
    // main methods

    /**
     * @see UMLAction#actionPerformed(ActionEvent)
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
        
        ProjectBrowser pb = ProjectBrowser.getInstance();
        Project p =  ProjectManager.getManager().getCurrentProject();
        try {
            JFileChooser chooser = null;
        
            if (p != null
                	&& p.getURL() != null
                	&& p.getURL().getFile().length() > 0) {
        
            	String filename = p.getURL().getFile();
                // TODO: Someone please explain this.
            	if (!filename.startsWith("/FILE1/+/")) {
            	    chooser = new JFileChooser(p.getURL().getFile());
            	}
            }
        
            if (chooser == null) {
                chooser = new JFileChooser();
            }
        
            //TODO: i18n
            chooser.setDialogTitle("Save Diagram as Graphics: " + defaultName);
            // Only specified format are allowed.
            chooser.removeChoosableFileFilter(chooser.
        				      getAcceptAllFileFilter());
            chooser.addChoosableFileFilter(FileFilters.PNG_FILTER);
            chooser.addChoosableFileFilter(FileFilters.GIF_FILTER);
            chooser.addChoosableFileFilter(FileFilters.PS_FILTER);
            chooser.addChoosableFileFilter(FileFilters.EPS_FILTER);
            chooser.addChoosableFileFilter(FileFilters.SVG_FILTER);
            // concerning the following lines: is .GIF preferred?
            chooser.setFileFilter(FileFilters.PNG_FILTER);
            String fileName = defaultName + "."
        	+ FileFilters.PNG_FILTER.getSuffix();
            chooser.setSelectedFile(new File(fileName));
        
            int retval = chooser.showSaveDialog(pb);
            if (retval == 0) {
                File theFile = chooser.getSelectedFile();
                if (theFile != null) {
                    String suffix = SuffixFilter.getExtension(theFile);
                    if (suffix == null
                            || !(suffix.equals(PS_SFX)
                            || suffix.equals(EPS_SFX)
                            || suffix.equals(GIF_SFX)
                            || suffix.equals(PNG_SFX)
                            || suffix.equals(SVG_SFX))) {
                		// add the selected filter suffix
        		FileFilter filter = chooser.getFileFilter();
              		suffix = FileFilters.getSuffix(filter);
               		theFile = new File(theFile.getParentFile(),
       			     theFile.getName() + "." + suffix);
                    }
                    // end new code
                
                    return doSave(theFile, suffix, true);
                }
            }
        } catch (OutOfMemoryError e) {
            new ExceptionDialog(ProjectBrowser.getInstance(),
                "You have run out of memory. " 
                + "Close down ArgoUML and restart with a larger heap size.", e);
        } catch (Exception e) {
            new ExceptionDialog(ProjectBrowser.getInstance(), e);
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

	CmdSaveGraphics cmd = null;
	if (FileFilters.PS_FILTER.getSuffix().equals(suffix)) {
	    cmd = new CmdSavePS();
	} else if (FileFilters.EPS_FILTER.getSuffix().equals(suffix)) {
	    cmd = new ActionSaveGraphicsCmdSaveEPS();
	} else if (FileFilters.PNG_FILTER.getSuffix().equals(suffix)) {
	    cmd = new CmdSavePNG();
	} else if (FileFilters.GIF_FILTER.getSuffix().equals(suffix)) {
	    cmd = new CmdSaveGIF();
	} else if (FileFilters.SVG_FILTER.getSuffix().equals(suffix)) {
	    cmd = new CmdSaveSVG();
	} else {
            if (useUI) { 
                ProjectBrowser.getInstance().showStatus(
                          "Unknown graphics file type with suffix "
			  + suffix);
            }
	    return false;
	}

        if (useUI) {
            ProjectBrowser.getInstance().showStatus(
                            "Writing " + theFile + "...");
        }
	if (theFile.exists() && useUI) {
	    String t = "Overwrite " + theFile;
	    int response = JOptionPane.showConfirmDialog(
                ProjectBrowser.getInstance(), t, t, JOptionPane.YES_NO_OPTION);
	    if (response != JOptionPane.YES_OPTION) {
		return false;
	    }
	}
	FileOutputStream fo = new FileOutputStream(theFile);
	cmd.setStream(fo);
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


/**
 * Class to adjust {@link org.tigris.gef.base.CmdSaveEPS} for our purpuses.<p>
 *
 * While doing this refactoring (February 2004) it is unclear to me (Linus
 * Tolke) why this modification in the {@link org.tigris.gef.base.CmdSaveEPS}
 * behavior is needed. Is it a bug in GEF? Is it an added feature?
 * The old comment was: override gef default to cope with scaling.
 */
class ActionSaveGraphicsCmdSaveEPS extends CmdSaveEPS {
    protected void saveGraphics(OutputStream s, Editor ce,
				Rectangle drawingArea)
	throws IOException {

	double scale = ce.getScale();
	int x = (int) (drawingArea.x * scale);
	int y = (int) (drawingArea.y * scale);
	int h = (int) (drawingArea.height * scale);
	int w = (int) (drawingArea.width * scale);
	drawingArea = new Rectangle(x, y, w, h);

	PostscriptWriter ps = new PostscriptWriter(s, drawingArea);

	ps.scale(scale, scale);

	ce.print(ps);
	ps.dispose();
    }
}
