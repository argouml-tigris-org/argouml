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
import java.lang.*;

import org.argouml.ui.*;
import org.argouml.application.api.*;
import org.argouml.util.logging.*;

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
     * The method that for all parsing actions. It calls the
     * actual parser methods depending on the type of the
     * file.
     *
     * @param p The current Argo project.
     * @param f The file or directory, we want to parse.
     */
    public static void doFile(Project p, File f) {
	Vector files = listDirectoryRecursively(f);

	ImportStatusScreen iss = new ImportStatusScreen("Importing",
							"Splash");

	SwingUtilities.invokeLater(new ImportRun(iss, p, _diagram, files));

	iss.show();
    }

    /**
     * This method does all the actual importing. Normally it runs in another
     * thread.
     *
     * @param iss is the status screen that is called for updates.
     * @param p is the project
     * @param files is a Vector of the files to be imported.
     */
    public static void realDoFile(ImportStatusScreen iss, 
				  Project p, Vector files) {

	ProjectBrowser pb = ProjectBrowser.TheInstance;

	int countFiles = files.size();

	SimpleTimer st = new SimpleTimer("realDoFile");
	st.mark("start");

	while (files.size() > 0) { // Passes
	    int countFilesThisPass = files.size();

	    Vector nextPassFiles = new Vector();

	    for (int i = 0; i < files.size(); i++) {
		File curFile = (File)files.elementAt(i);

		try {
		    st.mark(curFile.getName());
		    pb.showStatus("Importing " + curFile.getName() 
				  + " (in " + curFile.getParent());
		    parseFile(p, curFile);       // Try to parse this file.

		    int tot;
		    iss.setMaximum(tot = countFiles
				   + _diagram.getModifiedDiagrams().size()/10);
		    int act;
		    iss.setValue(act = countFiles - countFilesThisPass
				 + i + 1 - nextPassFiles.size());
		    pb.getStatusBar().showProgress(100 * act/tot);
		}
		catch(Exception e1) {
		    Argo.log.debug(e1);
		    nextPassFiles.addElement(curFile);
		}
	    }

	    // Now one sweep is done, set up everything for the next one.
	    if (files.size() == nextPassFiles.size())
		break;
	    files = nextPassFiles;
	}

	if (files.size() > 0) {
	    Argo.log.info("There are unparseable files:");
	    for (int i = 0; i < files.size(); i++) {
		Argo.log.info("Unparseable file: " 
			      + ((File)files.elementAt(i)).getName());
	    }
	}

	st.mark("layout");
	// Layout the modified diagrams.
	for(int i=0; i < _diagram.getModifiedDiagrams().size(); i++) {
	    ClassdiagramLayouter layouter =
		new ClassdiagramLayouter((UMLDiagram)
					 (_diagram.getModifiedDiagrams()
					  .elementAt(i)));
	    layouter.layout();

	    // Resize the diagram???
	    iss.setValue(countFiles + (i + 1)/10);
	}

	iss.done();
	
	Argo.log.info(st);
	pb.getStatusBar().showProgress(0);
    }

    /**
     * This method returns a Vector with files to import.
     *
     * The result depends on the descend settings.
     *
     * @param f The directory or a file.
     */
    private static Vector listDirectoryRecursively(File f) {
	Vector res = new Vector();

	Vector toDoDirectories = new Vector();
	Vector doneDirectories = new Vector();

	toDoDirectories.add(f);

	while (toDoDirectories.size() > 0) {
	    File curDir = (File)toDoDirectories.elementAt(0);
	    toDoDirectories.removeElementAt(0);
	    doneDirectories.add(curDir);

	    if (!curDir.isDirectory()) {
		// For some reason, this eledged directory is a single file
		// This could be that there is some confusion or just
		// the normal, that a single file was selected and is 
		// supposed to be imported.
		res.add(curDir);
		continue;
	    }

	    // Get the contents of the directory
	    String [] files = curDir.list();

	    for( int i = 0; i < files.length; i++) {
		File curFile = new File(curDir, files[i]);

		// The following test can cause trouble with links,
		// because links are accepted as directories, even if
		// they link files.
		// Links could also result in infinite loops. For this reason
		// we don't do this traversing recursively.
		if (curFile.isDirectory()) {   // If this file is a directory
		    if(descend.isSelected()) {
			if (doneDirectories.indexOf(curFile) >= 0
			    || toDoDirectories.indexOf(curFile) >= 0) {
			    // This one is already seen or to be seen.
			}
			else {
			    toDoDirectories.add(curFile);
			}
		    }
		}
		else {
		    if (isParseable(curFile))
			res.add(curFile);
		}
	    } 
	}
	return res;
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
     * Tells if the file is parseable or not.
     * Must match with files that are actually parseable.
     *
     * @see #parseFile
     * @param f file to be tested.
     * @return true if parseable, false if not.
     */
    private static boolean isParseable(File f) {
	if (f.getName().endsWith(".java")) {
	    return true;
	}

	return false;
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


class ImportRun implements Runnable {
    ImportStatusScreen _iss;
    Project _project;
    DiagramInterface _diagram;
    Vector _filesLeft;
    int _countFiles;
    int _countFilesThisPass;
    Vector _nextPassFiles;
    SimpleTimer _st;

    public ImportRun(ImportStatusScreen iss, Project p, DiagramInterface d,
		     Vector f) {
	_iss = iss;
	_project = p;
	_diagram = d;
	_filesLeft = f;
	_countFiles = _filesLeft.size();
	_countFilesThisPass = _countFiles;
	_nextPassFiles = new Vector();
	_st = new SimpleTimer("ImportRun");
	_st.mark("start");
    }

    public void run() {
	ProjectBrowser pb = ProjectBrowser.TheInstance;

	if (_filesLeft.size() > 0) {
	    File curFile = (File)_filesLeft.elementAt(0);
	    _filesLeft.removeElementAt(0);
	    
	    try {
		_st.mark(curFile.getName());
		pb.showStatus("Importing " + curFile.getName() 
			      + " (in " + curFile.getParent());
		Import.parseFile(_project, curFile); // Try to parse this file.

		int tot;
		_iss.setMaximum(tot = _countFiles
				+ _diagram.getModifiedDiagrams().size()/10);
		int act;
		_iss.setValue(act = _countFiles
			      - _filesLeft.size() - _nextPassFiles.size());
		pb.getStatusBar().showProgress(100 * act/tot);
	    }
	    catch(Exception e1) {
		Argo.log.debug(e1);
		_nextPassFiles.addElement(curFile);
	    }

	    SwingUtilities.invokeLater(this);
	    return;
	}

	if (_nextPassFiles.size() > 0
	    && _nextPassFiles.size() < _countFilesThisPass) {
	    _filesLeft = _nextPassFiles;
	    _nextPassFiles = new Vector();
	    _countFilesThisPass = _filesLeft.size();

	    SwingUtilities.invokeLater(this);
	    return;
	}
	    
	// Do post load processings.
	_st.mark("postprocessings");

	// Check if any diagrams where modified and the project
	// should be saved before exiting.
	if(Import.needsSave()) {
	    _project.setNeedsSave(true);
	}

	ProjectManager.getManager().setCurrentProject(_project);
	pb.showStatus("Import done");

	// Layout the modified diagrams.
	_st.mark("layout");
	for(int i=0; i < _diagram.getModifiedDiagrams().size(); i++) {
	    ClassdiagramLayouter layouter =
		new ClassdiagramLayouter((UMLDiagram)
					 (_diagram.getModifiedDiagrams()
					  .elementAt(i)));
	    layouter.layout();

	    // Resize the diagram???
	    _iss.setValue(_countFiles + (i + 1)/10);
	}

	_iss.done();
	
	Argo.log.info(_st);
	pb.getStatusBar().showProgress(0);
    }
}

class ImportStatusBar extends StatusBar {
    public void setMaximum(int i) { _progress.setMaximum(i); }
    public void setValue(int i) { _progress.setValue(i); }
}

class ImportStatusScreen extends JDialog {
    protected ImportStatusBar _statusBar = new ImportStatusBar();

    public ImportStatusScreen(String title, String iconName) {
	super();
	if (title != null) setTitle(title);
	Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
	setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	getContentPane().setLayout(new BorderLayout(0, 0));
	getContentPane().add(_statusBar, BorderLayout.SOUTH);
	Dimension contentPaneSize = getContentPane().getPreferredSize();
	setSize(contentPaneSize.width, contentPaneSize.height);
	setLocation(scrSize.width/2 - contentPaneSize.width/2,
		    scrSize.height/2 - contentPaneSize.height/2);
	pack();
    }

    public void setMaximum(int i) { _statusBar.setMaximum(i); }
    public void setValue(int i) { 
	_statusBar.setValue(i);
	repaint();
    }

    public void done() { hide(); dispose(); }
}
