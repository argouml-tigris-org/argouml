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

import java.io.File;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.filechooser.FileSystemView;

import org.argouml.application.api.PluggableImport;
import org.argouml.kernel.Project;
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
 * @deprecated by tfmorris for 0.23.2 - implement ImportInterface and ModuleInterface
 * directly, using the utility methods in FileImportUtils to replace some of the previous
 * functionality of this class.
 *
 * @author Alexander Lepekhine
 */
public abstract class FileImportSupport implements PluggableImport {
    
    private static final String SEPARATOR = "/";

    private JPanel configPanel;


    /**
     * Object(s) selected in chooser.
     */
    private Object theFile;

    /*
     * @see org.argouml.application.api.PluggableImport#getConfigPanel()
     */
    public JComponent getConfigPanel() {
	if (configPanel == null) {
	    configPanel = new ConfigPanelExtension();
        }
	return configPanel;
    }

    /*
     * @see org.argouml.application.api.PluggableImport#parseFile(org.argouml.kernel.Project, java.lang.Object, org.argouml.uml.reveng.DiagramInterface, org.argouml.uml.reveng.Import)
     */
    public void parseFile(Project p, Object o, DiagramInterface diagram,
			  Import theImport)
	throws Exception {
        
        // Default implementation does nothing
        
    }

    /*
     * @see org.argouml.application.api.PluggableImport#getChooser(org.argouml.uml.reveng.Import)
     * 
     * Default chooser is a JFileChooser
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

    /*
     * @see org.argouml.application.api.PluggableImport#getList(org.argouml.uml.reveng.Import)
     */
    public Vector getList(Import theImport) {
        if (theFile != null && theFile instanceof File) {
            File f = (File) theFile;
            if (f.isDirectory()) {
                theImport.setSrcPath(f.getAbsolutePath());
            } else {
                theImport.setSrcPath(null);
            }
            return new Vector(FileImportUtils.getList(f, 
                    theImport.isDiscendDirectoriesRecursively(), getSuffixFilters()));
        } else {
            return new Vector();
        }

    }

    /*
     * @see org.argouml.application.api.PluggableImport#isParseable(java.lang.Object)
     */
    public boolean isParseable(Object f) {
        return FileImportUtils.matchesSuffix(f, getSuffixFilters());
    }

    /*
     * @see org.argouml.application.api.PluggableImport#getLayout(org.argouml.uml.diagram.ui.UMLDiagram)
     */
    public ClassdiagramLayouter getLayout(UMLDiagram diagram) {
	return	new ClassdiagramLayouter(diagram);
    }

    /*
     * @see org.argouml.application.api.Pluggable#inContext(java.lang.Object[])
     */
    public boolean inContext(Object[] context) {
        return true;
    }

    /*
     * @see org.argouml.application.api.ArgoModule#initializeModule()
     */
    public boolean initializeModule() {
        // called when loading module
        return true;
    }

    /*
     * @see org.argouml.application.api.ArgoModule#shutdownModule()
     */
    public boolean shutdownModule() {
        // called when the module is shutdown
        return true;
    }

    /*
     * @see org.argouml.application.api.ArgoModule#setModuleEnabled(boolean)
     */
    public void setModuleEnabled(boolean tf) {
        // called to enable-disable
    }

    /*
     * @see org.argouml.application.api.ArgoModule#isModuleEnabled()
     */
    public boolean isModuleEnabled() {
        // determines if enabled-disabled
        return true;
    }

    /*
     * @see org.argouml.application.api.ArgoModule#getModuleVersion()
     */
    public String getModuleVersion() {
        return "0.1";
    }

    /*
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
     * @return Returns the attribute radio button.
     * @deprecated by tfmorris for 0.23.4,
     *  use {@link ImportSettings#isAttributeSelected()}
     */
    protected JRadioButton getAttribute() {
        return ((ConfigPanelExtension) getConfigPanel()).getAttribute();
    }

    /**
     * @return Returns the datatype radio button.
     * @deprecated by tfmorris for 0.23.4,
     *  use {@link ImportSettings#isDatatypeSelected()}
     */
    protected JRadioButton getDatatype() {
        return ((ConfigPanelExtension) getConfigPanel()).getDatatype();
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

        /*
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

    }


}
