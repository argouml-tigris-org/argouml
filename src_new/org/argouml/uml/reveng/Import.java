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

package org.argouml.uml.reveng; 

import java.io.*;
import org.argouml.kernel.*;
import org.argouml.uml.reveng.java.*;
import org.argouml.uml.diagram.ui.*;
import org.argouml.uml.diagram.static_structure.layout.*;
import org.tigris.gef.base.*;
import ru.novosoft.uml.*;
import ru.novosoft.uml.model_management.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * This is the main class for all import classes.
 *
 * Supports recursive search in folder for all .java classes.
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

    /** The files that needs a second RE pass. */
    private static Vector secondPassFiles;

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

	secondPassFiles = new Vector();

	if ( f.isDirectory()) {     // If f is a directory, 
	    doDirectory( p, f);     // import all the files in this directory
	}
	else {
	    try {
		parseFile(p, f);       // Try to parse this file.
	    }
	    catch(Exception e1) {
		secondPassFiles.addElement(f);
	    }
	}

	for(Iterator i = secondPassFiles.iterator(); i.hasNext();) {
	    try {
		parseFile(p, (File)i.next());
	    }
	    catch(Exception e2) {
		System.out.println("ERROR: " + e2.getMessage());
		e2.printStackTrace();
	    }
	}

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
	    }
	    else {
		try {
		    parseFile(p, curFile);       // Try to parse this file.
		}
		catch(Exception e) {
		    secondPassFiles.add(curFile);
		}
	    }
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

    /**
     * If we have modified any diagrams, the project was modified and
     * should be saved. I don't consider a import, that only modifies
     * the metamodel, at this point (Andreas Rueckert <a_rueckert@gmx.net> ).
     * Calling Project.setNeedsSave(true) doesn't work here, because
     * Project.postLoad() is called after the import and it sets the
     * _needsSave flag to false.
     *
     * @return true, if any diagrams where modified and the project should be saved before exit.
     */
    public static boolean needsSave() {
	return (_diagram.getModifiedDiagrams().size() > 0);
    }
}
