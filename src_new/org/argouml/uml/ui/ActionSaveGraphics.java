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

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Category;
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


/** Wraps a CmdSaveGIF or CmdSave(E)PS to allow selection of an output file. 
 *  @stereotype singleton
 */

public class ActionSaveGraphics extends UMLAction {
    protected static Category cat = Category.getInstance(ActionSaveGraphics.class);

    ////////////////////////////////////////////////////////////////
    // static variables

    public static ActionSaveGraphics SINGLETON = new ActionSaveGraphics(); 

    public static final String separator = "/";


    ////////////////////////////////////////////////////////////////
    // constructors

    protected ActionSaveGraphics() {
	super( "action.save-graphics", NO_ICON);
    }


    ////////////////////////////////////////////////////////////////
    // main methods

    public void actionPerformed( ActionEvent ae ) {
	trySave( false );
    }

    public boolean trySave( boolean overwrite ) {
	Object target = ProjectManager.getManager().getCurrentProject().getActiveDiagram();
	if( target instanceof Diagram ) {
	    String defaultName = ((Diagram)target).getName();
	    defaultName = Util.stripJunk(defaultName);

	    // FIX - It's probably worthwhile to abstract and factor this chooser
	    // and directory stuff. More file handling is coming, I'm sure.

	    ProjectBrowser pb = ProjectBrowser.getInstance();
	    Project p =  ProjectManager.getManager().getCurrentProject();
	    try {
		JFileChooser chooser = null;
		try {
		    if ( p != null && p.getURL() != null &&
			 p.getURL().getFile().length() > 0 ) {
			String filename = p.getURL().getFile();
			if( !filename.startsWith( "/FILE1/+/" ) )
			    chooser  = OsUtil.getFileChooser( p.getURL().getFile() );
		    }
		}
		catch( Exception ex ) {
                    cat.error("exception in opening JFileChooser", ex);
		}

		if( chooser == null ) chooser = OsUtil.getFileChooser();

		chooser.setDialogTitle( "Save Diagram as Graphics: " + defaultName );
		// Only specified format are allowed.
		chooser.removeChoosableFileFilter(chooser.
						  getAcceptAllFileFilter());
		chooser.addChoosableFileFilter( FileFilters.GIFFilter );
		chooser.addChoosableFileFilter( FileFilters.PSFilter );
		chooser.addChoosableFileFilter( FileFilters.EPSFilter );
		chooser.addChoosableFileFilter( FileFilters.SVGFilter );
		// concerning the following lines: is .GIF preferred?
		chooser.setFileFilter( FileFilters.GIFFilter );
		File def = new File(  defaultName + "."
				      + FileFilters.GIFFilter._suffix );
		chooser.setSelectedFile( def );

		int retval = chooser.showSaveDialog( pb );
		if( retval == 0 ) {
		    File theFile = chooser.getSelectedFile();
		    if( theFile != null ) {
			String path = theFile.getParent();
			String name = theFile.getName();
			String extension = SuffixFilter.getExtension(theFile);
			// 2002-07-16
			// Jaap Branderhorst
			// patch to issue 517
			// issue is:
			// a file should be saved with the extension from the selected filter and according to the format 
			// of the selected filter.
			// start new code
			
			if (extension == null || 
				!((extension.equals(FileFilters.PSFilter._suffix)) || 
					(extension.equals(FileFilters.EPSFilter._suffix)) ||
					(extension.equals(FileFilters.GIFFilter._suffix)) ||
					(extension.equals(FileFilters.SVGFilter._suffix))
				)) {
				// add the selected filter extension
				FileFilter filter = (FileFilter) chooser.getFileFilter();
				extension = FileFilters.getSuffix(filter);  
				theFile = new File(theFile.getParentFile(), theFile.getName() + "." + extension);
			}
			// end new code
				
			CmdSaveGraphics cmd=null;
			if (FileFilters.PSFilter._suffix.equals(extension))
			    cmd = new CmdSavePS();
			else if (FileFilters.EPSFilter._suffix.equals(extension))
			    cmd = new CmdSaveEPS();
			else if (FileFilters.GIFFilter._suffix.equals(extension))
			    cmd = new CmdSaveGIF();
			else if (FileFilters.SVGFilter._suffix.equals(extension))
			    cmd = new CmdSaveSVG();
			else {
			    pb.showStatus("Unknown graphics file type withextension "
					  +extension);
			    return false;
			}

			if( !path.endsWith( separator ) ) path += separator;
			pb.showStatus( "Writing " + path + name + "..." );
			if( theFile.exists() && !overwrite ) {
			    String t = "Overwrite " + path + name;
			    int response =
				JOptionPane.showConfirmDialog(pb, t, t,
							      JOptionPane.YES_NO_OPTION);
			    if (response == JOptionPane.NO_OPTION) return false;
			}
			FileOutputStream fo = new FileOutputStream( theFile );
			cmd.setStream(fo);
			cmd.doIt();
			fo.close();
			pb.showStatus( "Wrote " + path + name );
			return true;
		    }
		}
	    }
	    catch( FileNotFoundException ignore )
		{
                cat.error("got a FileNotFoundException", ignore);
		}
	    catch( IOException ignore )
		{
                cat.error("got an IOException", ignore);
		}
	}

	return false;
    }
} /* end class ActionSaveGraphics */
