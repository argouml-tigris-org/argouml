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
import org.argouml.uml.reveng.*;
import org.argouml.util.osdep.*;
import org.argouml.util.FileFilters;
import org.argouml.util.SuffixFilter;

import org.tigris.gef.base.*;

import java.awt.event.*;
import java.io.*;

import javax.swing.*;


/** Action to trigger importing from sources.
 * @stereotype singleton
 */
public class ActionImportFromSources extends UMLAction {
    
    protected static Category cat = Category.getInstance(org.argouml.uml.ui.ActionImportFromSources.class);

    ////////////////////////////////////////////////////////////////
    // static variables

    public static ActionImportFromSources SINGLETON = new ActionImportFromSources(); 

    public static final String separator = "/"; //System.getProperty("file.separator");


    ////////////////////////////////////////////////////////////////
    // constructors

    protected ActionImportFromSources() {
        super("Import sources...");
    }


    ////////////////////////////////////////////////////////////////
    // main methods

    public void actionPerformed(ActionEvent event) {
        ProjectBrowser pb = ProjectBrowser.TheInstance;
        Project p = Project.getCurrentProject();

        try {
            String directory = Globals.getLastDirectory();
            JFileChooser chooser = OsUtil.getFileChooser(directory);

            if (chooser == null) chooser = OsUtil.getFileChooser();

            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.setDialogTitle("Import sources");
            SuffixFilter filter = FileFilters.JavaFilter;
            chooser.addChoosableFileFilter(filter);
            // chooser.setFileFilter(filter);
	    
	    chooser.setAccessory(Import.getConfigPanel());

            int retval = chooser.showOpenDialog(pb);

            if (retval == 0) {
                File theFile = chooser.getSelectedFile();
                if (theFile != null) {
                    String path = chooser.getSelectedFile().getParent();
                    String filename = chooser.getSelectedFile().getName();
                    filename = path + separator + filename;
                    //    if (!filename.endsWith(Project.FILE_EXT)) {
                    //  filename += Project.FILE_EXT;
                    //  theFile = new File(filename);
                    //}
                    Globals.setLastDirectory(path);
                    if (filename != null) {
                        pb.showStatus("Parsing " + path + filename + "...");
                        //p = ArgoParser.SINGLETON.getProject();
                        Import.doFile(p, theFile);
                        p.postLoad();

			// Check if any diagrams where modified and the project
			// should be saved before exiting.
			if(Import.needsSave()) {
			    p.setNeedsSave(true);
			}

                        pb.setProject(p);
                        pb.showStatus("Parsed " + filename);
                        return;
                    }
                }
            }
        } catch (Exception exception) {
            cat.error("got an Exception in ActionImportFromSources", exception);
        }
    }
}
/* end class ActionImportFromSources */   
