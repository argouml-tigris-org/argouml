// Copyright (c) 1996-01 The Regents of the University of California. All
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

package org.argouml.uml.reveng; 

import java.io.*;
import org.argouml.kernel.*;
import org.argouml.uml.reveng.java.*;
import org.argouml.uml.diagram.ui.*;
import org.argouml.uml.diagram.static_structure.layout.*;
import org.tigris.gef.base.*;
import ru.novosoft.uml.model_management.*;

import javax.swing.*;
import java.awt.*;

/**
 * This is the main class for all import classes.
 *
 * $Revision$
 * $Date$
 *
 * @author Andreas Rueckert <a_rueckert@gmx.net>
 */
public class Import {

    // Create a interface to the current diagram
    static DiagramInterface _diagram =
	new DiagramInterface(Globals.curEditor());

    private static JComponent configPanel = null;
    private static JCheckBox descend;

    /**
     * Get the panel that lets the user set reverse engineering
     * parameters.
     */
    public static JComponent getConfigPanel() {

	if(configPanel == null) {
	    JPanel general = new JPanel();
	    general.setLayout(new GridBagLayout());

	    descend = new JCheckBox("Descend directories recursively.");
	    descend.setSelected(true);
	    general.add(descend, 
			new GridBagConstraints(GridBagConstraints.RELATIVE,
					       GridBagConstraints.RELATIVE,
					       GridBagConstraints.REMAINDER,
					       GridBagConstraints.REMAINDER,
					       1.0, 1.0,
					       GridBagConstraints.NORTHWEST,
					       GridBagConstraints.NONE,
					       new Insets(5, 5, 5, 5),
					       0, 0));

	    JTabbedPane tab = new JTabbedPane();
	    tab.add(general, "General");
	    tab.add(JavaImport.getConfigPanel(), "Java");
	    configPanel = tab;
	}
	return configPanel;
    }
	

    /**
     * The main method for all parsing actions. It calls the
     * actual parser methods depending on the type of the
     * file.
     *
     * @param p The current Argo project.
     * @param f The file or directory, we want to parse.
     * @exception Parser exceptions.  */
    public static void doFile(Project p, File f) throws Exception {

	if ( f.isDirectory())       // If f is a directory, 
	    doDirectory( p, f);     // import all the files in this directory
	else
	    parseFile(p, f);       // Try to parse this file.

	// Layout the modified diagrams.
	for(int i=0; i < _diagram.getModifiedDiagrams().size(); i++) {
	    ClassdiagramLayouter layouter =
		new ClassdiagramLayouter((UMLDiagram)
					 (_diagram.getModifiedDiagrams()
					  .elementAt(i)));
	    layouter.layout();

	    // Resize the diagram???
	}
    }

    /**
     * This method imports an entire directory. It calls the parser for
     * files and creates packages for the directories.
     *
     * @param p The current project.
     * @param f The directory.
     * @exception Parser exceptions.
     */
    public static void doDirectory(Project p, File f) throws Exception {
	String [] files = f.list();  // Get the content of the directory

	for( int i = 0; i < files.length; i++) {
	    File curFile = new File( f, files[i]);

	    // The following test can cause trouble with links,
	    // because links are accepted as directories, even if
	    // they link files.
	    if ( curFile.isDirectory()) {   // If this file is a directory
		// MMFactory factory = new MMFactory((MModel)p.getModel());
		// factory.createPackage( curFile);  // create a package for it.
		if(descend.isSelected()) {
		    doDirectory(p, curFile);
		}
	    } else
		parseFile(p, curFile);      // Parse the file.
	} 
    }


    /**
     * Parse 1 file.
     *
     * @param f The file to parse.
     * @exception Parser exception.
     */
    public static void parseFile( Project p, File f) throws Exception {

	// Is this file a Java source file?
	if ( f.getName().endsWith(".java")) {
	    JavaImport.parseFile( p, f, _diagram);
	}
    }
}
