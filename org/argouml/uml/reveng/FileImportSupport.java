// $Id$
// Copyright (c) 1996-99 The Regents of the University of California. All
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

import org.argouml.kernel.*;
import org.argouml.application.api.*;
import org.argouml.util.osdep.OsUtil;
import org.argouml.uml.diagram.static_structure.layout.ClassdiagramLayouter;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.util.SuffixFilter;
import org.argouml.ui.ProjectBrowser;

import org.tigris.gef.base.Globals;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Vector;

/**
 * This is the base class for import from files.
 * It provides JFileChooser for file selection
 * and other methods common to file import.
 * It assumes that similar classes will be written
 * for other input sources.
 * 
 * @author Alexander Lepekhine
 * @version $Revision$
 */
public abstract class FileImportSupport implements PluggableImport {

    protected JPanel configPanel;

    protected JRadioButton attribute;

    protected JRadioButton datatype;

    protected static final String separator = "/";
    //System.getProperty("file.separator");
	
    /** Object(s) selected in chooser */
    protected Object theFile;
	
    /**
     * Get the panel that lets the user set reverse engineering
     * parameters.
     */
    public JComponent getConfigPanel() {

	if (configPanel == null) {
	    configPanel = new JPanel();
	    configPanel.setLayout(new GridBagLayout());

	    JLabel attributeLabel = new JLabel("Attributes modelled as");
	    configPanel.add(attributeLabel,
			    new GridBagConstraints(GridBagConstraints.RELATIVE,
						   GridBagConstraints.RELATIVE,
						   GridBagConstraints.REMAINDER,
						   1,
						   1.0, 0.0,
						   GridBagConstraints.NORTHWEST,
						   GridBagConstraints.NONE,
						   new Insets(5, 5, 0, 5),
						   0, 0));
	    ButtonGroup group1 = new ButtonGroup();
	    attribute =
		new JRadioButton("UML attributes.");
	    attribute.setSelected(true);
	    group1.add(attribute);
	    configPanel.add(attribute,
			    new GridBagConstraints(GridBagConstraints.RELATIVE,
						   GridBagConstraints.RELATIVE,
						   GridBagConstraints.REMAINDER,
						   1,
						   1.0, 0.0,
						   GridBagConstraints.NORTHWEST,
						   GridBagConstraints.NONE,
						   new Insets(0, 5, 0, 5),
						   0, 0));
	    JRadioButton association =
		new JRadioButton("UML associations.");
	    group1.add(association);
	    configPanel.add(association,
			    new GridBagConstraints(GridBagConstraints.RELATIVE,
						   GridBagConstraints.RELATIVE,
						   GridBagConstraints.REMAINDER,
						   1,
						   1.0, 0.0,
						   GridBagConstraints.NORTHWEST,
						   GridBagConstraints.NONE,
						   new Insets(0, 5, 5, 5),
						   0, 0));

	    ButtonGroup group2 = new ButtonGroup();
	    datatype =
		new JRadioButton("Arrays modelled as datatypes.");
	    datatype.setSelected(true);
	    group2.add(datatype);
	    configPanel.add(datatype,
			    new GridBagConstraints(GridBagConstraints.RELATIVE,
						   GridBagConstraints.RELATIVE,
						   GridBagConstraints.REMAINDER,
						   1,
						   1.0, 0.0,
						   GridBagConstraints.NORTHWEST,
						   GridBagConstraints.NONE,
						   new Insets(5, 5, 0, 5),
						   0, 0));
	    JRadioButton multi =
		new JRadioButton("Arrays modelled with multiplicity 1..n.");
	    group2.add(multi);
	    configPanel.add(multi,
			    new GridBagConstraints(GridBagConstraints.RELATIVE,
						   GridBagConstraints.RELATIVE,
						   GridBagConstraints.REMAINDER,
						   GridBagConstraints.REMAINDER,
						   1.0, 1.0,
						   GridBagConstraints.NORTHWEST,
						   GridBagConstraints.NONE,
						   new Insets(0, 5, 5, 5),
						   0, 0));
	}
	return configPanel;
    }

    /**
     * This method parses 1 file.
     * Default implementation does nothing.
     *
     * @param f The input file for the parser.
     * @exception Exception Parser exception.
     */
    public void parseFile( Project p, Object o, DiagramInterface diagram,
			   Import _import)
	throws Exception {
    }

    /**
     * Create chooser for objects we are to import.
     * Default implemented chooser is JFileChooser.
     */
    public JComponent getChooser(Import  imp) {
	String directory = Globals.getLastDirectory();
	JFileChooser ch = OsUtil.getFileChooser(directory);
	if (ch == null) ch = OsUtil.getFileChooser();

	final JFileChooser chooser = ch; 
	final Import _import = imp;
		
	chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	SuffixFilter[] filters = getSuffixFilters();
	if (filters != null) {
	    for (int i = 0; i < filters.length; i++)
		chooser.addChoosableFileFilter(filters[i]);
	}
	chooser.addActionListener(new ActionListener() 
	    {
		public void actionPerformed(ActionEvent e) {
		    if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
			theFile = chooser.getSelectedFile();
			if (theFile != null) {
			    String path = chooser.getSelectedFile().getParent();
			    String filename =
				chooser.getSelectedFile().getName();
			    filename = path + separator + filename;
			    Globals.setLastDirectory(path);
			    if (filename != null) {
				_import.disposeDialog();
				_import.doFile();
				return;
			    }
			}
		    } else if (e.getActionCommand().equals(JFileChooser.CANCEL_SELECTION)) {
			_import.disposeDialog();
                        ProjectBrowser.getInstance().setEnabled(true);
		    }
		}
	    });
	return chooser;
    }
	
    /**
     * <p>This method returns a Vector with objects to import.
     *
     * <p>Processing each file in turn is equivalent to a breadth first
     * search through the directory structure.
     *
     * @param Import object called this method..
     */
    public Vector getList(Import _import) {
	Vector res = new Vector();

	Vector toDoDirectories = new Vector();
	Vector doneDirectories = new Vector();

	if (theFile != null && theFile instanceof File) {
	    File f = (File) theFile;
	    if (f.isDirectory()) _import.setSrcPath(f.getAbsolutePath());
	    else _import.setSrcPath(null);

	    toDoDirectories.add(f);

	    while (toDoDirectories.size() > 0) {
		File curDir = (File) toDoDirectories.elementAt(0);
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

		for ( int i = 0; i < files.length; i++) {
		    File curFile = new File(curDir, files[i]);

		    // The following test can cause trouble with
		    // links, because links are accepted as
		    // directories, even if they link files.  Links
		    // could also result in infinite loops. For this
		    // reason we don't do this traversing recursively.
		    if (curFile.isDirectory()) {
			// If this file is a directory
			if (_import.isDiscendDirectoriesRecursively()) {
			    if (doneDirectories.indexOf(curFile) >= 0
				|| toDoDirectories.indexOf(curFile) >= 0) {
				// This one is already seen or to be seen.
			    } else {
				toDoDirectories.add(curFile);
			    }
			}
		    } else {
			if (isParseable(curFile))	res.add(curFile);
		    }
		}
	    }
	}
	return res;
    }

    /**
     * Tells if the file is parseable or not.
     * Must match with files that are actually parseable.
     *
     * @param f file to be tested.
     * @return true if parseable, false if not.
     */
    public boolean isParseable(Object f) {
	SuffixFilter[] filters = getSuffixFilters();
	if (filters != null) {
	    for (int i = 0; i < filters.length; i++) {
		String fileName =
		    (f != null && f instanceof File
		     ? ((File) f).getName()
		     : "");
		if (fileName.endsWith(filters[i]._suffix)) return true;
	    }
	}
	return false;
    }


    /**
     * Provide layout for modified class diagram.
     */
    public ClassdiagramLayouter getLayout(UMLDiagram diagram) {
	return	new ClassdiagramLayouter(diagram);
    }

    public boolean inContext(Object[] context) {
	return true;
    }
	
    public boolean initializeModule() {
	// called when loading module
	return true;
    }

    public boolean shutdownModule() {
	// called when the module is shutdown
	return true;
    }

    public void setModuleEnabled(boolean tf) {
	// called to enable-disable
    }

    public boolean isModuleEnabled() {
	// determines if enabled-disabled
	return true;
    }

    public String getModuleVersion() {
	return "0.1";
    }

    public String getModuleAuthor() {
	return "";
    }

    // calls all modules to let them add to a popup menu
    public Vector getModulePopUpActions(Vector popUpActions, Object context) {
	return null;
    }
	
    /** 
     * Provides an array of suffixe filters for the module.
     * Must be implemented in child class.
     * @return SuffixFilter[] suffixes for processing
     */
    public abstract SuffixFilter[] getSuffixFilters();

}
