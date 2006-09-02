// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.filechooser.FileSystemView;

import org.argouml.application.api.PluggableImport;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.swingext.JXButtonGroupPanel;
import org.argouml.uml.diagram.static_structure.layout.ClassdiagramLayouter;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.util.SuffixFilter;
import org.tigris.gef.base.Globals;

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

    private JXButtonGroupPanel configPanel;

    private JRadioButton attribute;

    private JRadioButton datatype;

    private static final String SEPARATOR = "/";
    //System.getProperty("file.separator");

    /**
     * Object(s) selected in chooser.
     */
    private Object theFile;

    /**
     * Get the panel that lets the user set reverse engineering
     * parameters.
     *
     * @see org.argouml.application.api.PluggableImport#getConfigPanel()
     */
    public JComponent getConfigPanel() {

	if (configPanel == null) {
	    configPanel = new JXButtonGroupPanel(2);
	    configPanel.setLayout(new GridBagLayout());

	    JLabel attributeLabel1 =
                new JLabel(
                        Translator.localize("action.import-java-attr-model"));
	    configPanel.add(attributeLabel1,
			    new GridBagConstraints(GridBagConstraints.RELATIVE,
						   GridBagConstraints.RELATIVE,
						   GridBagConstraints.REMAINDER,
						   1,
						   1.0, 0.0,
						   GridBagConstraints.NORTHWEST,
						   GridBagConstraints.NONE,
						   new Insets(5, 5, 0, 5),
						   0, 0));
	    attribute =
		new JRadioButton(
                        Translator.localize("action.import-java-UML-attr"));
	    attribute.setSelected(true);
	    configPanel.add(attribute,
			    new GridBagConstraints(GridBagConstraints.RELATIVE,
						   GridBagConstraints.RELATIVE,
						   GridBagConstraints.REMAINDER,
						   1,
						   1.0, 0.0,
						   GridBagConstraints.NORTHWEST,
						   GridBagConstraints.NONE,
						   new Insets(0, 5, 0, 5),
						   0, 0), -1, 0);
	    JRadioButton association =
		new JRadioButton(
                        Translator.localize("action.import-java-UML-assoc"));
	    configPanel.add(association,
			    new GridBagConstraints(GridBagConstraints.RELATIVE,
						   GridBagConstraints.RELATIVE,
						   GridBagConstraints.REMAINDER,
						   1,
						   1.0, 0.0,
						   GridBagConstraints.NORTHWEST,
						   GridBagConstraints.NONE,
						   new Insets(0, 5, 5, 5),
						   0, 0), -1, 0);
	    JLabel attributeLabel2 =
	        new JLabel(
                    Translator.localize("action.import-java-array-model"));
	    configPanel.add(attributeLabel2,
                        new GridBagConstraints(GridBagConstraints.RELATIVE,
                                               GridBagConstraints.RELATIVE,
                                               GridBagConstraints.REMAINDER,
                                               1,
                                               1.0, 0.0,
                                               GridBagConstraints.NORTHWEST,
                                               GridBagConstraints.NONE,
                                               new Insets(5, 5, 0, 5),
                                               0, 0));

	    datatype =
		new JRadioButton(
                        Translator.localize(
                                "action.import-java-array-model-datatype"));
	    datatype.setSelected(true);
	    configPanel.add(datatype,
			    new GridBagConstraints(GridBagConstraints.RELATIVE,
						   GridBagConstraints.RELATIVE,
						   GridBagConstraints.REMAINDER,
						   1,
						   1.0, 0.0,
						   GridBagConstraints.NORTHWEST,
						   GridBagConstraints.NONE,
						   new Insets(5, 5, 0, 5),
						   0, 0), -1, 1);
	    JRadioButton multi =
		new JRadioButton(
                        Translator.localize(
                                "action.import-java-array-model-multi"));
	    configPanel.add(multi,
			    new GridBagConstraints(GridBagConstraints.RELATIVE,
						   GridBagConstraints.RELATIVE,
						   GridBagConstraints.REMAINDER,
						   GridBagConstraints.REMAINDER,
						   1.0, 1.0,
						   GridBagConstraints.NORTHWEST,
						   GridBagConstraints.NONE,
						   new Insets(0, 5, 5, 5),
						   0, 0), -1, 1);
	}
	return configPanel;
    }

    /**
     * This method parses 1 file.
     * Default implementation does nothing.
     *
     * @see org.argouml.application.api.PluggableImport#parseFile(
     *         org.argouml.kernel.Project, java.lang.Object,
     *         org.argouml.uml.reveng.DiagramInterface,
     *         org.argouml.uml.reveng.Import)
     * @param p the project
     * @param o the object
     * @param diagram the diagram interface
     * @param theImport the import
     * @exception Exception Parser exception.
     */
    public void parseFile(Project p, Object o, DiagramInterface diagram,
			  Import theImport)
	throws Exception {
    }

    /**
     * Create chooser for objects we are to import.
     * Default implemented chooser is JFileChooser.
     *
     * @see org.argouml.application.api.PluggableImport#getChooser(org.argouml.uml.reveng.Import)
     */
    public JComponent getChooser(Import imp) {
        String directory = Globals.getLastDirectory();

        final JFileChooser chooser = new ImportFileChooser(imp, directory);

        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        SuffixFilter[] filters = getSuffixFilters();
        if (filters != null) {
            for (int i = 0; i < filters.length; i++) {
                chooser.addChoosableFileFilter(filters[i]);
            }
        }
        return chooser;
    }

    /**
     * This method returns a Vector with objects to import.<p>
     *
     * Processing each file in turn is equivalent to a breadth first
     * search through the directory structure.
     *
     * @param theImport object called by this method.
     * @return the list
     *
     * @see org.argouml.application.api.PluggableImport#getList(org.argouml.uml.reveng.Import)
     */
    public Vector getList(Import theImport) {
	Vector res = new Vector();

	Vector toDoDirectories = new Vector();
	Vector doneDirectories = new Vector();

	if (theFile != null && theFile instanceof File) {
	    File f = (File) theFile;
	    if (f.isDirectory()) {
	        theImport.setSrcPath(f.getAbsolutePath());
	    } else {
	        theImport.setSrcPath(null);
	    }

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

		for (int i = 0; i < files.length; i++) {
		    File curFile = new File(curDir, files[i]);

		    // The following test can cause trouble with
		    // links, because links are accepted as
		    // directories, even if they link files.  Links
		    // could also result in infinite loops. For this
		    // reason we don't do this traversing recursively.
		    if (curFile.isDirectory()) {
			// If this file is a directory
			if (theImport.isDiscendDirectoriesRecursively()) {
			    if (doneDirectories.indexOf(curFile) >= 0
				|| toDoDirectories.indexOf(curFile) >= 0) {
				; // This one is already seen or to be seen.
			    } else {
				toDoDirectories.add(curFile);
			    }
			}
		    } else {
			if (isParseable(curFile)) {
			    res.add(curFile);
			}
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
		if (fileName.endsWith(filters[i].getSuffix())) {
		    return true;
		}
	    }
	}
	return false;
    }


    /**
     * Provide layout for modified class diagram.
     *
     * @see org.argouml.application.api.PluggableImport#getLayout(org.argouml.uml.diagram.ui.UMLDiagram)
     */
    public ClassdiagramLayouter getLayout(UMLDiagram diagram) {
	return	new ClassdiagramLayouter(diagram);
    }

    /**
     * @see org.argouml.application.api.Pluggable#inContext(java.lang.Object[])
     */
    public boolean inContext(Object[] context) {
	return true;
    }

    /**
     * @see org.argouml.application.api.ArgoModule#initializeModule()
     */
    public boolean initializeModule() {
	// called when loading module
	return true;
    }

    /**
     * @see org.argouml.application.api.ArgoModule#shutdownModule()
     */
    public boolean shutdownModule() {
	// called when the module is shutdown
	return true;
    }

    /**
     * @see org.argouml.application.api.ArgoModule#setModuleEnabled(boolean)
     */
    public void setModuleEnabled(boolean tf) {
	// called to enable-disable
    }

    /**
     * @see org.argouml.application.api.ArgoModule#isModuleEnabled()
     */
    public boolean isModuleEnabled() {
	// determines if enabled-disabled
	return true;
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleVersion()
     */
    public String getModuleVersion() {
	return "0.1";
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleAuthor()
     */
    public String getModuleAuthor() {
	return "";
    }

    /**
     * Calls all modules to let them add to a popup menu.
     *
     * @see org.argouml.application.api.ArgoModule#getModulePopUpActions(
     * java.util.Vector, java.lang.Object)
     */
    public Vector getModulePopUpActions(Vector popUpActions, Object context) {
	return null;
    }

    /**
     * Provides an array of suffixe filters for the module.
     * Must be implemented in child class.
     * @return SuffixFilter[] suffixes for processing
     */
    public abstract SuffixFilter[] getSuffixFilters();

    /**
     * @return Returns the attribute.
     */
    protected JRadioButton getAttribute() {
        return attribute;
    }

    /**
     * @return Returns the datatype.
     */
    protected JRadioButton getDatatype() {
        return datatype;
    }

    private class ImportFileChooser extends JFileChooser {

        private Import theImport;

        /**
         * @see javax.swing.JFileChooser#JFileChooser(String)
         */
        public ImportFileChooser(Import imp, String currentDirectoryPath) {
            super(currentDirectoryPath);
            theImport = imp;
        }

        /**
         * @see javax.swing.JFileChooser#JFileChooser(String, FileSystemView)
         */
        public ImportFileChooser(
                Import imp,
                String currentDirectoryPath,
                FileSystemView fsv) {
            super(currentDirectoryPath, fsv);
            theImport = imp;
        }

        /**
         * @see javax.swing.JFileChooser#JFileChooser()
         */
        public ImportFileChooser(Import imp) {
            super();
            this.theImport = imp;
        }

        /**
         * @see javax.swing.JFileChooser#JFileChooser(FileSystemView)
         */
        public ImportFileChooser(
                Import imp,
                FileSystemView fsv) {
            super(fsv);
            this.theImport = imp;
        }

	/**
	 * @see javax.swing.JFileChooser#approveSelection()
	 */
	public void approveSelection() {
            theFile = getSelectedFile();
            if (theFile != null) {
                String path = getSelectedFile().getParent();
                String filename =
                    getSelectedFile().getName();
                filename = path + SEPARATOR + filename;
                Globals.setLastDirectory(path);
                if (filename != null) {
                    theImport.disposeDialog();
                    new ImportClasspathDialog(theImport);
                    return;
                }
            }
	}

	/**
	 * @see javax.swing.JFileChooser#cancelSelection()
	 */
	public void cancelSelection() {
            theImport.disposeDialog();
	}

        /**
         * The UID.
         */
        private static final long serialVersionUID = 3298461148934583094L;
    }
}
