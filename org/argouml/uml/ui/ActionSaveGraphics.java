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

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;
import org.argouml.application.api.CommandLineInterface;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.ProjectBrowser;
import org.argouml.util.FileFilters;
import org.argouml.util.SuffixFilter;
import org.argouml.util.osdep.OsUtil;
import org.tigris.gef.base.CmdSaveEPS;
import org.tigris.gef.base.CmdSaveGIF;
import org.tigris.gef.base.CmdSaveGraphics;
import org.tigris.gef.base.CmdSavePS;
import org.tigris.gef.base.CmdSaveSVG;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.util.Util;
import java.io.*;
import java.awt.Rectangle;
import org.tigris.gef.base.*;
import org.tigris.gef.persistence.*;


/** Wraps a CmdSaveGIF or CmdSave(E)PS to allow selection of an output file. 
 *  @stereotype singleton
 */

public class ActionSaveGraphics
    extends UMLAction
    implements CommandLineInterface {

    /**
     * @deprecated as of 0.15.4. Will be made private. Use your own logger.
     */
    protected static Logger cat =
	Logger.getLogger(ActionSaveGraphics.class);

    ////////////////////////////////////////////////////////////////
    // static variables

    /**
     * @deprecated by Linus Tolke as of 0.15.4 this should not be
     * used. We are changing this action so that it will no longer be
     * a singleton. Use the constructor to create yourself a new
     * instance of this object instead.
     */
    public static ActionSaveGraphics SINGLETON = new ActionSaveGraphics(); 

    /**
     * @deprecated by Linus Tolke as of 0.15.4. Get this information from
     * some java.io class.
     *
     * @see java.io
     */
    public static final String separator = "/";


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
    public void actionPerformed( ActionEvent ae ) {
	trySave( false );
    }

    /**
     * Method that does almost everything in this class.<p>
     *
     * @param overwrite True if we shouldn't care that we erase an old copy.
     * @return true if all went well.
     */
    public boolean trySave( boolean overwrite ) {
	Object target =
	    ProjectManager.getManager().getCurrentProject().getActiveDiagram();

	if (!(target instanceof Diagram)) {
	    return false;
	}

	String defaultName = ((Diagram) target).getName();
	defaultName = Util.stripJunk(defaultName);

	// FIX - It's probably worthwhile to abstract and factor
	// this chooser and directory stuff. More file handling is
	// coming, I'm sure.

	ProjectBrowser pb = ProjectBrowser.getInstance();
	Project p =  ProjectManager.getManager().getCurrentProject();
	try {
	    JFileChooser chooser = null;

	    if (p != null
		&& p.getURL() != null
		&& p.getURL().getFile().length() > 0 ) {

		String filename = p.getURL().getFile();
		if (!filename.startsWith("/FILE1/+/")) {
		    chooser = OsUtil.getFileChooser(p.getURL().getFile());
		}
	    }

	    if (chooser == null) {
		chooser = OsUtil.getFileChooser();
	    }

	    chooser.setDialogTitle("Save Diagram as Graphics: "
				   + defaultName );
	    // Only specified format are allowed.
	    chooser.removeChoosableFileFilter(chooser.
					      getAcceptAllFileFilter());
	    chooser.addChoosableFileFilter(FileFilters.GIFFilter);
	    chooser.addChoosableFileFilter(FileFilters.PSFilter);
	    chooser.addChoosableFileFilter(FileFilters.EPSFilter);
	    chooser.addChoosableFileFilter(FileFilters.SVGFilter);
	    // concerning the following lines: is .GIF preferred?
	    chooser.setFileFilter(FileFilters.GIFFilter);
	    File def = new File(defaultName + "."
				+ FileFilters.GIFFilter._suffix);
	    chooser.setSelectedFile(def);

	    int retval = chooser.showSaveDialog(pb);
	    if (retval == 0) {
		File theFile = chooser.getSelectedFile();
		if (theFile != null) {
		    String suffix = SuffixFilter.getExtension(theFile);
		    // 2002-07-16 Jaap Branderhorst patch to issue
		    // 517 issue is: a file should be saved with
		    // the suffix from the selected filter and
		    // according to the format of the selected
		    // filter.  start new code
			
		    if (suffix == null
			|| !(suffix.equals(FileFilters.PSFilter._suffix)
			     || suffix.equals(FileFilters.EPSFilter._suffix)
			     || suffix.equals(FileFilters.GIFFilter._suffix)
			     || suffix.equals(FileFilters.SVGFilter._suffix))) {
			// add the selected filter suffix
			FileFilter filter = chooser.getFileFilter();
			suffix = FileFilters.getSuffix(filter);  
			theFile =
			    new File(theFile.getParentFile(),
				     theFile.getName() + "." + suffix);
		    }
		    // end new code
				
		    return doSave(theFile, suffix, overwrite);
		}
	    }
	}
	catch (FileNotFoundException ignore) {
	    cat.error("got a FileNotFoundException", ignore);
	}
	catch (IOException ignore) {
	    cat.error("got an IOException", ignore);
	}

	return false;
    }

    /**
     * Actually do the saving.
     *
     * @return true if it was successful.
     * @param theFile is the file that we are writing to
     * @param suffix is the suffix. Used for deciding what format the file 
     * shall have.
     * @param overwrite is true if we are not supposed to warn that we are
     * replacing an old file.
     */
    private boolean doSave(File theFile,
			   String suffix, boolean overwrite)
	throws FileNotFoundException, IOException {

	ProjectBrowser pb = ProjectBrowser.getInstance();

	CmdSaveGraphics cmd = null;
	if (FileFilters.PSFilter._suffix.equals(suffix)) {
	    cmd = new CmdSavePS();
	} else if (FileFilters.EPSFilter._suffix.equals(suffix)) {
	    cmd = new ActionSaveGraphicsCmdSaveEPS();
	} else if (FileFilters.GIFFilter._suffix.equals(suffix)) {
	    cmd = new CmdSaveGIF();
	} else if (FileFilters.SVGFilter._suffix.equals(suffix)) {
	    cmd = new CmdSaveSVG();
	} else {
	    pb.showStatus("Unknown graphics file type with suffix "
			  + suffix);
	    return false;
	}

	pb.showStatus("Writing " + theFile + "...");
	if (theFile.exists() && !overwrite) {
	    String t = "Overwrite " + theFile;
	    int response =
		JOptionPane.showConfirmDialog(pb, t, t,
					      JOptionPane.YES_NO_OPTION);
	    if (response != JOptionPane.YES_OPTION) {
		return false;
	    }
	}
	FileOutputStream fo = new FileOutputStream(theFile);
	cmd.setStream(fo);
	cmd.doIt();
	fo.close();
	pb.showStatus("Wrote " + theFile);
	return true;
    }


    /**
     * Execute this action from the command line.
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
	    return doSave(file, suffix, true);
	} catch (FileNotFoundException e) {
	    cat.error("File not found error when writing.", e);
	} catch (IOException e) {
	    cat.error("IO error when writing.", e);
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
