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

import org.apache.log4j.Category;
import org.argouml.kernel.*;
import org.argouml.ui.*;
import org.argouml.util.*;
import org.argouml.util.osdep.*;
import org.tigris.gef.base.*;
import org.tigris.gef.util.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.*;
import javax.swing.filechooser.*;


/** Wraps a CmdSaveGIF to allow selection of an output file. */

public class ActionSaveGIF extends UMLAction {
    protected static Category cat = Category.getInstance(ActionSaveGIF.class);

    ////////////////////////////////////////////////////////////////
    // static variables

    public static ActionSaveGIF SINGLETON = new ActionSaveGIF(); 

    public static final String separator = "/";


    ////////////////////////////////////////////////////////////////
    // constructors

    protected ActionSaveGIF() {
	super( "Save GIF...", NO_ICON);
    }


    ////////////////////////////////////////////////////////////////
    // main methods

    public void actionPerformed( ActionEvent ae ) {
	trySave( false );
    }

    public boolean trySave( boolean overwrite ) {
	CmdSaveGIF cmd = new CmdSaveGIF();
	Object target = ProjectBrowser.TheInstance.getActiveDiagram();
	if( target instanceof Diagram ) {
	    String defaultName = ((Diagram)target).getName();
	    defaultName = Util.stripJunk(defaultName);

	    // FIX - It's probably worthwhile to abstract and factor this chooser
	    // and directory stuff. More file handling is coming, I'm sure.

	    ProjectBrowser pb = ProjectBrowser.TheInstance;
	    Project p =  pb.getProject();
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

		chooser.setDialogTitle( "Save Diagram as GIF: " + defaultName );
		FileFilter filter = FileFilters.GIFFilter;
		chooser.addChoosableFileFilter( filter );
		chooser.setFileFilter( filter );
		File def = new File(  defaultName + ".gif" ); // is .GIF preferred?
		chooser.setSelectedFile( def );

		int retval = chooser.showSaveDialog( pb );
		if( retval == 0 ) {
		    File theFile = chooser.getSelectedFile();
		    if( theFile != null ) {
			String path = theFile.getParent();
			String name = theFile.getName();
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
			cmd.setStream( fo );
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
} /* end class ActionSaveGIF */
