// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.log4j.Category;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.ArgoDiagram;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.util.FileFilters;
import org.tigris.gef.base.CmdSaveEPS;
import org.tigris.gef.base.CmdSaveGIF;
import org.tigris.gef.base.CmdSaveGraphics;
import org.tigris.gef.base.CmdSavePS;
import org.tigris.gef.base.CmdSaveSVG;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.util.Util;


/** 
 * Wraps a CmdSaveGIF or CmdSave(E)PS to allow selection of an output file. 
 * Introduced thanks to issue 2126. Saves diagrams only as GIFs. <p>
 * 
 * TODO: Add a user choice for other formats (PNG, SVG,...)
 *  
 * @author Leonardo Souza Mario Bueno (lsbueno@tigris.org)
 */

public class ActionSaveAllGraphics extends UMLAction {
    protected static Category cat =
	Category.getInstance(ActionSaveAllGraphics.class);
    
    ////////////////////////////////////////////////////////////////
    // static variables
    
    public static final String separator = "/";
    
    
    ////////////////////////////////////////////////////////////////
    // constructors
    
    public ActionSaveAllGraphics() {
	super( "action.save-all-graphics", NO_ICON);
    }
    
    
    ////////////////////////////////////////////////////////////////
    // main methods
    
    public void actionPerformed( ActionEvent ae ) {
	trySave( false );
    }
    
    public boolean trySave(boolean overwrite) {
	Project p =  ProjectManager.getManager().getCurrentProject();
	TargetManager tm = TargetManager.getInstance();
	Vector  targets = p.getDiagrams();
	Iterator it = targets.iterator();
	File saveDir = getSaveDir(p);
	boolean okSoFar = true;
	ArgoDiagram activeDiagram = p.getActiveDiagram();
	while (it.hasNext() && okSoFar) {
	    ArgoDiagram d = (ArgoDiagram) it.next();
	    tm.setTarget(d);
	    okSoFar = trySaveDiagram(overwrite, d, saveDir);
	}
	tm.setTarget(activeDiagram);
	return okSoFar;
    }	

    protected boolean trySaveDiagram(boolean overwrite, Object target, File saveDir) {
	ProjectBrowser pb = ProjectBrowser.getInstance();
	if ( target instanceof Diagram ) {
	    String defaultName = ((Diagram) target).getName();
	    defaultName = Util.stripJunk(defaultName);
	    // FIX - It's probably worthwhile to abstract and factor
	    // this chooser and directory stuff. More file handling is
	    // coming, I'm sure.
	    try {
		File theFile = new File(saveDir, defaultName + "."
					+ FileFilters.GIF_FILTER.getSuffix() );
		String name = theFile.getName();
		String path = theFile.getParent();
		CmdSaveGraphics cmd = getCmdSaveGraphics(FileFilters.GIF_FILTER.getSuffix());
		if (cmd == null) {
		    pb.showStatus("Unknown graphics file type with extension "
				  + FileFilters.GIF_FILTER.getSuffix());
		    return false;
		}
		pb.showStatus( "Writing " + path + name + "..." );
		saveGraphicsToFile(theFile, cmd,overwrite);
		pb.showStatus( "Wrote " + path + name );
		return true;
	    }
	    catch ( FileNotFoundException ignore )
		{
		    cat.error("got a FileNotFoundException", ignore);
		}
	    catch ( IOException ignore )
		{
		    cat.error("got an IOException", ignore);
		}
	}
	return false;
    }
    

    protected File getSaveDir(Project p) {
	JFileChooser chooser = getFileChooser(p);
	ProjectBrowser pb = ProjectBrowser.getInstance();
	int retval = chooser.showSaveDialog( pb );
	if ( retval == JFileChooser.APPROVE_OPTION ) {
	    return chooser.getSelectedFile();
	}
        return null;
    }

    private boolean saveGraphicsToFile(File theFile, CmdSaveGraphics cmd, boolean overwrite) throws IOException {
	ProjectBrowser pb = ProjectBrowser.getInstance();
	if ( theFile.exists() && !overwrite ) {
	    String t = "Overwrite " + theFile.getPath();
	    int response =
		JOptionPane.showConfirmDialog(pb, t, t,
					      JOptionPane.YES_NO_OPTION);
	    if (response == JOptionPane.NO_OPTION) return false;
	}
	FileOutputStream fo = null;
	try {
	    fo = new FileOutputStream( theFile );
	    cmd.setStream(fo);
	    cmd.doIt();
	} finally {
	    if (fo != null) {
		fo.close();
	    }
	}
	return true;
    }
    
    private CmdSaveGraphics getCmdSaveGraphics(String extension) {
	if (FileFilters.PS_FILTER.getSuffix().equals(extension))
	    return  new CmdSavePS();
	else if (FileFilters.EPS_FILTER.getSuffix().equals(extension))
	    return new CmdSaveEPS();
	else if (FileFilters.GIF_FILTER.getSuffix().equals(extension))
	    return new CmdSaveGIF();
	else if (FileFilters.SVG_FILTER.getSuffix().equals(extension)) {
	    return new CmdSaveSVG(); 
	} else {
	    return null;
	}
    }
    
    private JFileChooser getFileChooser(Project p) {
	JFileChooser chooser = null;
	try {
	    if ( p != null && p.getURL() != null &&
		 p.getURL().getFile().length() > 0 ) {
		String filename = p.getURL().getFile();
		if ( !filename.startsWith( "/FILE1/+/" ) )
		    chooser  =
			new JFileChooser( p.getURL().getFile() );
	    }
	}
	catch ( Exception ex ) {
	    cat.error("exception in opening JFileChooser", ex);
	}
	
	if ( chooser == null ) chooser = new JFileChooser();
	chooser.setDialogTitle( "Save All Diagrams as Graphics" ); //TODO: i18n
	chooser.setDialogType(JFileChooser.OPEN_DIALOG);
	chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	chooser.setMultiSelectionEnabled(false);
	return chooser;
    }

} /* end class ActionSaveAllGraphics */ 