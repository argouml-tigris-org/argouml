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

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileSystemView;

import org.apache.log4j.Logger;
import org.argouml.application.api.Argo;
import org.argouml.application.api.Configuration;
import org.argouml.application.api.PluggableImport;
import org.argouml.cognitive.Designer;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.moduleloader.ModuleInterface;
import org.argouml.ui.ArgoFrame;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.explorer.ExplorerEventAdaptor;
import org.argouml.uml.diagram.static_structure.ClassDiagramGraphModel;
import org.argouml.uml.diagram.static_structure.layout.ClassdiagramLayouter;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.util.SuffixFilter;
import org.argouml.util.UIUtils;
import org.argouml.util.logging.SimpleTimer;
import org.tigris.gef.base.Globals;
import org.tigris.swidgets.GridLayout2;


/**
 * This is the main class for all import classes.<p>
 *
 * It provides JPanels for tailoring the import run in the FileChooser.<p>
 *
 * The Import run is started by calling doFile(Project, File)<p>
 *
 * Supports recursive search in folder for all .java classes.<p>
 *
 * There are now 3 levels of detail for import:<p>
 *
 * <ol>
 *   <li> 0 - classifiers only
 *   <li> 1 - classifiers plus feature specifications
 *   <li> 2 - full import, feature detail (ie. operations with methods)
 * </ol>
 *
 * TODO: Add registration methods for new languages.
 *
 * @author Andreas Rueckert a_rueckert@gmx.net
 */
public class Import implements ImportSettings {

    private static final String SEPARATOR = "/";
    
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(Import.class);

    /**
     * Imported directory.
     */
    private String srcPath;

    /**
     * Create a interface to the current diagram.
     */
    private DiagramInterface diagramInterface;

    /**
     * Current language module.
     */
    private Object module;

    /**
     * keys are module name, values are PluggableImport instance.
     */
    private Hashtable modules;

    private JComponent configPanel;

    private JCheckBox descend;

    private JCheckBox changedOnly;

    private JCheckBox createDiagrams;

    private JCheckBox minimiseFigs;

    private JCheckBox layoutDiagrams;

    // level 0 import detail
    private JRadioButton classOnly;

    // level 1 import detail
    private JRadioButton classAndFeatures;

    // level 2 import detail
    private JRadioButton fullImport;

    //import detail level var:
    private int importLevel;

    private JTextField inputSourceEncoding;

    private JDialog dialog;

    private ImportStatusScreen iss;

    private StringBuffer problems = new StringBuffer();
    
    // current level / pass
    private int level;
    
    private File selectedFile;

    /**
     * The default extended configuration panel.
     * TODO: This used to be provided by the abstract class FileImportSupport
     * and it can be merged with our main configuration panel here.
     */
    private ConfigPanelExtension importConfigPanel;
    
    /**
     * Creates dialog window with chooser and configuration panel.
     */
    public Import() {
        modules = new Hashtable();
        
        // Get all old style modules
        List arraylist = Argo.getPlugins(PluggableImport.class);
        ListIterator iterator = arraylist.listIterator();
        while (iterator.hasNext()) {
            PluggableImport pIModule = (PluggableImport) iterator.next();
            modules.put(pIModule.getModuleName(), pIModule);
        }
        if (modules.size() == 0) {
            LOG.warn("No old style import modules defined");
        }

        Set newPlugins = ImporterManager.getInstance().getImporters();
        for (Iterator it = newPlugins.iterator(); it.hasNext();) {
            ModuleInterface mod = (ModuleInterface) it.next();
            modules.put(mod.getName(), mod);
        }
        if (modules.size() == 0) {
            throw new RuntimeException("Internal error. "
                    + "No importer modules found.");
        }

        // "Java" is a default module
        module = modules.get("Java");
        if (module == null) {
            throw new RuntimeException("Internal error. "
                       + "Default import module not found");
        }
        JComponent chooser = getChooser();
        dialog =
            new JDialog(ArgoFrame.getInstance(),
                    Translator.localize("action.import-sources"), true);

        dialog.getContentPane().add(chooser, BorderLayout.CENTER);
        dialog.getContentPane().add(getConfigPanel(this), BorderLayout.EAST);
        dialog.pack();
        int x =
            (ArgoFrame.getInstance().getSize().width
             - dialog.getSize().width)
            / 2;
        int y =
            (ArgoFrame.getInstance().getSize().height
             - dialog.getSize().height)
            / 2;
        dialog.setLocation(x > 0 ? x : 0, y > 0 ? y : 0);
        
        UIUtils.loadCommonKeyMap(dialog);
        
        dialog.setVisible(true);
    }

    /**
     * @param key the key of the attribute
     * @return the value of the attribute
     * @deprecated by tfmorris for 0.23.3, use getLevel()
     */
    public Object getAttribute(String key) {
        if ("level".equals(key)) {
            return new Integer(level);
        } else {
            throw new IllegalArgumentException("Unknown attribute name");
        }
    }

    /**
     * @param key the key of the attribute
     * @param value the value of the attribute
     * 
     * @deprecated by tfmorris for 0.23.3, use setLevel()
     */
    public void setAttribute(String key, Object value) {
        if ("level".equals(key) && value instanceof Integer) {
            level = ((Integer) value).intValue();
        } else {
            throw new IllegalArgumentException("Unknown attribute name" 
                    + " or invalid value");
        }
    }
    
    /*
     * @see org.argouml.uml.reveng.ImportSettings#getImportLevel()
     */
    public int getImportLevel() {
        return level;
    }
    
    /*
     * @param newLevel the import detail for the current pass
     */
    private void setImportLevel(int newLevel) {
        level = newLevel;
    }

    /*
     * @see org.argouml.uml.reveng.ImportSettings#getInputSourceEncoding()
     */
    public String getInputSourceEncoding() {
        return inputSourceEncoding.getText();
    }

    /*
     * @see org.argouml.uml.reveng.ImportSettings#isAttributeSelected()
     */
    public boolean isAttributeSelected() {
        // This is only valid for new style importers, but they're also
        // the only ones invoking this method
        return importConfigPanel.getAttribute().isSelected();
    }
    
    /*
     * @see org.argouml.uml.reveng.ImportSettings#isDatatypeSelected()
     */
    public boolean isDatatypeSelected() {
        // This is only valid for new style importers, but they're also
        // the only ones invoking this method
        return importConfigPanel.getDatatype().isSelected();
    }

    /*
     * @see org.argouml.uml.reveng.ImportSettings#getImportSession()
     */
    public Import getImportSession() {
        return this;
    }
    
    /**
     * Close dialog window.
     * 
     * @deprecated by tfmorris for 0.23.2 - UI management is solely an internal
     *             matter. Visibility of this will be further reduced when no
     *             importers use FileImportSupport.
     */
    void disposeDialog() {
        Configuration.setString(Argo.KEY_INPUT_SOURCE_ENCODING,
            getInputSourceEncoding());
        dialog.setVisible(false);
        dialog.dispose();
    }

    /**
     * Get the panel that lets the user set reverse engineering
     * parameters.
     * 
     * @param importInstance the instance of the import
     * @return the panel
     * @deprecated by tfmorris for 0.23.3 - this is an internal method
     * and the visibility will be reduced.  Use the accessors in
     * {@link ImportSettings} to determine the current settings.
     */
    public JComponent getConfigPanel(final Import importInstance) {

        final JTabbedPane tab = new JTabbedPane();

        // build the configPanel:
        if (configPanel == null) {
            JPanel general = new JPanel();
            general.setLayout(
                    new GridLayout2(13, 1, 0, 0, GridLayout2.NONE));

            general.add(new JLabel(
                    Translator.localize("action.import-select-lang")));

            Vector languages = new Vector();

            for (Enumeration keys = modules.keys(); keys.hasMoreElements();) {
                languages.add(keys.nextElement());
            }
            JComboBox selectedLanguage = new JComboBox(languages);
            selectedLanguage.addActionListener(
                    new SelectedLanguageListener(importInstance, tab));
            general.add(selectedLanguage);

            descend =
                new JCheckBox(Translator.localize(
                        "action.import-option-descend-dir-recur"));
            descend.setSelected(true);
            general.add(descend);

            changedOnly =
                new JCheckBox(Translator.localize(
                        "action.import-option-changed_new"), true);
            general.add(changedOnly);

            createDiagrams =
                new JCheckBox(Translator.localize(
                        "action.import-option-create-diagram"), true);
            general.add(createDiagrams);

            minimiseFigs =
                new JCheckBox(Translator.localize(
                        "action.import-option-min-class-icon"), true);
            general.add(minimiseFigs);

            layoutDiagrams =
                new JCheckBox(Translator.localize(
                        "action.import-option-perform-auto-diagram-layout"),
                        true);
            general.add(layoutDiagrams);

            // de-selects the fig minimising & layout
            // if we are not creating diagrams
            createDiagrams.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent actionEvent) {
                    if (!createDiagrams.isSelected()) {
                        minimiseFigs.setSelected(false);
                        layoutDiagrams.setSelected(false);
                    }
                }
            });

            // select the level of import
            // 0 - classifiers only
            // 1 - classifiers plus feature specifications
            // 2 - full import, feature detail

            JLabel importDetailLabel =
                new JLabel(Translator.localize(
                        "action.import-level-of-import-detail"));
            ButtonGroup detailButtonGroup = new ButtonGroup();

            classOnly =
                new JRadioButton(Translator.localize(
                        "action.import-option-classfiers"));
            detailButtonGroup.add(classOnly);

            classAndFeatures =
                new JRadioButton(Translator.localize(
                        "action.import-option-classifiers-plus-specs"));
            detailButtonGroup.add(classAndFeatures);

            fullImport =
                new JRadioButton(Translator.localize(
                        "action.import-option-full-import"));
            fullImport.setSelected(true);
            detailButtonGroup.add(fullImport);

            general.add(importDetailLabel);
            general.add(classOnly);
            general.add(classAndFeatures);
            general.add(fullImport);

            general.add(new JLabel(
                    Translator.localize("action.import-file-encoding")));
            String enc =
                Configuration.getString(Argo.KEY_INPUT_SOURCE_ENCODING);
            if (enc == null || enc.trim().equals("")) {
                inputSourceEncoding =
                    new JTextField(System.getProperty("file.encoding"));
            } else {
                inputSourceEncoding = new JTextField(enc);
            }
            general.add(inputSourceEncoding);

            tab.add(general, Translator.localize("action.import-general"));
            String moduleName = "";
            if (module instanceof PluggableImport) {
                moduleName = ((PluggableImport) module).getModuleName();
            } else if (module instanceof ModuleInterface) {
                moduleName = ((ModuleInterface) module).getName();
            }
            tab.add(getConfigPanelExtension(), moduleName);
            configPanel = tab;
        }
        return configPanel;
        
    }
    
    /*
     * Get the extension panel for the configuration settings.
     */
    private JComponent getConfigPanelExtension() {
        JComponent result;
        if (module instanceof PluggableImport) {
            // Old style importer
            PluggableImport pi = (PluggableImport) module;
            result = ((PluggableImport) pi).getConfigPanel();
        } else if (module instanceof ImportInterface) {
            // New style importers don't provide a config panel
            if (importConfigPanel == null) {
                importConfigPanel = new ConfigPanelExtension();
            }
            result = importConfigPanel; 
        } else {
            throw new RuntimeException("Unrecognized module type");
        }
        if (result == null) {
            result = new JPanel();
        }
        return result;
    }

    private class SelectedLanguageListener implements ActionListener {

        /**
         * The current import.
         */
        private Import importInstance;

        /**
         * The pane.
         */
        private JTabbedPane tab;

        /**
         * The constructor.
         * @param i The current import.
         * @param t The pane.
         */
        SelectedLanguageListener(Import i, JTabbedPane t) {
            importInstance = i;
            tab = t;
        }

        /*
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            JComboBox cb = (JComboBox) e.getSource();
            String selected = (String) cb.getSelectedItem();
            Object oldModule = module;
            module = modules.get(selected);
            if (oldModule instanceof PluggableImport
                    || module instanceof PluggableImport) {
                dialog.getContentPane().remove(0);
                JComponent chooser = getChooser();
                if (chooser == null) {
                    chooser = new JPanel();
                }
                dialog.getContentPane().add(chooser, 0);

                JComponent config = getConfigPanelExtension();
                tab.remove(1);
                tab.add(config, selected, 1);
                tab.validate();

                dialog.validate();
            }
            
            // TODO: If/when we implement support for new style
            // importers to specify additional configuration settings,
            // those will need to be refreshed here - tfm
        }
    }


    /**
     * This method is called by ActionImportFromSources to start the
     * import run.<p>
     *
     * The method that for all parsing actions. It calls the actual
     * parser methods depending on the type of the file.<p>
     */
    public void doFile() {
        // determine how many files to process
        List files = getFileList();

        if (changedOnly.isSelected()) {
            // filter out all unchanged files
            Object model =
                ProjectManager.getManager().getCurrentProject().getModel();
            for (int i = files.size() - 1; i >= 0; i--) {
                File f = (File) files.get(i);
                String fn = f.getAbsolutePath();
                String lm = String.valueOf(f.lastModified());
                if (lm.equals(
                        Model.getFacade().getTaggedValueValue(model, fn))) {
                    files.remove(i);
                }
            }
        }

        if (!classOnly.isSelected()) {
            // 2 passes needed
            files.addAll(files); // for the second pass

            if (classAndFeatures.isSelected()) {
                importLevel = 1;
            } else {
                importLevel = 2;
            }
        } else {
            importLevel = 0;
        }

        // we always start with a level 0 import
        setImportLevel(0);

        diagramInterface = getCurrentDiagram();

        ArgoFrame.getInstance().setCursor(
                Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        // now start importing (with an empty problem list)
        problems = new StringBuffer();
        iss = new ImportStatusScreen("Importing", "Splash");
        SwingUtilities.invokeLater(
                   new ImportRun(files, layoutDiagrams.isSelected()));
        iss.setVisible(true);
    }

    /**
     * Get the files. For old style modules, this asks the module for the list.
     * For new style modules we generate it ourselves based on their specified
     * file suffixes.
     * 
     * @return the list of files to be imported
     */
    private List getFileList() {
        List files;
        if (module instanceof PluggableImport) {
            // Old style importer
            PluggableImport pi = (PluggableImport) module;
            files = pi.getList(this);
        } else if (module instanceof ImportInterface) {
            // Old style importer - we did the file selection
            if (selectedFile.isDirectory()) {
                setSrcPath(selectedFile.getAbsolutePath());
            } else {
                setSrcPath(null);
            }
            files = FileImportUtils.getList(selectedFile, isDescendSelected(),
                    ((ImportInterface) module).getSuffixFilters());
        } else {
            throw new RuntimeException("Unrecognized module type");
        }
        return files;
    }

    /**
     * Set path for processed directory.
     *
     * @param path the given path
     */
    public void setSrcPath(String path) {
        srcPath = path;
    }

    /**
     * @return path for processed directory.
     */
    public String getSrcPath() {
        return srcPath;
    }

    /**
     * Parse one source file.
     *
     * @param project the project
     * @param f The file to parse.
     * @exception Exception ??? 
     * TODO: Couldn't we throw a narrower exception? - tfm
     * TODO: Does this need to be public? - tfm
     */
    public void parseFile(Project project, Object f) throws Exception {
        File file = (File) f;
        if (module instanceof PluggableImport) {
            // Old style importer
            PluggableImport pi = (PluggableImport) module;
            // Is this file a compatible source file?
            if (pi.isParseable(file)) {
                ProjectBrowser.getInstance()
                    .showStatus("Parsing " + file.toString() + "...");
                pi.parseFile(project, file, diagramInterface, this);
                setLastModified(project, file);
            }
        } else if (module instanceof ImportInterface) {
            // New style importer
            ImportInterface im = (ImportInterface) module;
            if (im.isParseable(file)) {
                ProjectBrowser.getInstance()
                    .showStatus("Parsing " + file.toString() + "...");
                im.parseFile(project, file, this);
                setLastModified(project, file);
            }

        } else {
            throw new RuntimeException("Unrecognized module type");
        }
        

    }

    /*
     * Create a TaggedValue with a tag/type matching our source module
     * filename and a value of the file's last modified timestamp.
     * @param project
     * @param f
     */
    private void setLastModified(Project project, File file) {
        // set the lastModified value
        String fn = file.getAbsolutePath();
        String lm = String.valueOf(file.lastModified());
        if (lm != null) {
            Model.getCoreHelper()
                .setTaggedValue(project.getModel(), fn, lm);
        }
    }

    /**
     * Check, if "Create diagrams from imported code" is selected.<p>
     *
     * @return true, if "Create diagrams from imported code" is selected
     */
    public boolean isCreateDiagramsChecked() {
        if (createDiagrams != null) {
            return createDiagrams.isSelected();
        }
        return true;
    }

    /**
     * Check, if "Discend directories recursively" is selected.<p>
     *
     * @deprecated by tfmorris for 0.23.3, use 
     * {@link ImportSettings#isDescendSelected()}
     * @return true, if "Discend directories recursively" is selected
     */
    public boolean isDiscendDirectoriesRecursively() {
        return isDescendSelected();
    }

    /*
     * @see org.argouml.uml.reveng.ImportSettings#isDescendSelected()
     */
    public boolean isDescendSelected() {
        if (descend != null) {
            return descend.isSelected();
        }
        return true;
    }
    
    /**
     * Check, if "Minimise Class icons in diagrams" is selected.<p>
     *
     * @deprecated by tfmorris for 0.23.2.  Adding figures to diagrams is
     * no longer handled by pluggable importers.
     * @return true, if "Minimise Class icons in diagrams" is selected
     */
    public boolean isMinimiseFigsChecked() {
        if (minimiseFigs != null) {
            return minimiseFigs.isSelected();
        }
        return false;
    }

    /*
     * @see org.argouml.uml.reveng.ImportSettings#isChangedOnlySelected()
     */
    public boolean isChangedOnlySelected() {
        if (changedOnly != null) {
            return changedOnly.isSelected();
        }
        return false;
    }


    /**
     * If we have modified any diagrams, the project was modified and
     * should be saved. I don't consider a import, that only modifies
     * the metamodel, at this point (Andreas Rueckert).
     * Calling Project.setNeedsSave(true) doesn't work here, because
     * Project.postLoad() is called after the import and it sets the
     * needsSave flag to false.<p>
     *
     * @return true, if any diagrams where modified and the project
     * should be saved before exit.
     * @deprecated by tfmorris for 0.23.2 - use standard project
     * "save needed" mechanisms
     */
    public boolean needsSave() {
        return (diagramInterface.getModifiedDiagrams().size() > 0);
    }


    /*
     * @see org.argouml.uml.reveng.ImportSettings#getDiagramInterface()
     */
    public DiagramInterface getDiagramInterface() {
        return diagramInterface;
    }

    /**
     * Set target diagram.<p>
     *
     * @return selected diagram, if it is class diagram,
     * else return null.
     */
    private DiagramInterface getCurrentDiagram() {
        DiagramInterface result = null;
        if (Globals.curEditor().getGraphModel()
                instanceof ClassDiagramGraphModel) {

            result =  new DiagramInterface(Globals.curEditor());

        }
        return result;
    }

    /*
     * Create chooser for objects we are to import. Old style modules get to
     * provide their own (although I don't believe any of them do), while new
     * style modules get the a chooser provided by us (which matches what the
     * abstract class FileImportSupport used to provide).
     */
    private JComponent getChooser() {
        if (module instanceof PluggableImport) {
            return ((PluggableImport)module).getChooser(this);
        } else {
            String directory = Globals.getLastDirectory();
            
            final JFileChooser chooser = new ImportFileChooser(this, directory);

            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            SuffixFilter[] filters = 
                ((ImportInterface) module).getSuffixFilters();
            if (filters != null) {
                for (int i = 0; i < filters.length; i++) {
                    chooser.addChoosableFileFilter(filters[i]);
                }
            }
            return chooser;
        } 
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
            selectedFile = getSelectedFile();
            if (selectedFile != null) {
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

    /**
     * This class parses each file in turn and allows the GUI to refresh
     * itself by performing the run() once for each file.<p>
     *
     * This class also listens for a "Stop" message from the
     * ImportStatusScreen, in order to cancel long import runs.<p>
     */
    class ImportRun implements Runnable {
        private List filesLeft;

        private int countFiles;

        private int countFilesThisPass;

        private Vector nextPassFiles;

        private SimpleTimer st;

        private boolean cancelled;

        private boolean criticThreadWasOn;

        private boolean doLayout;

        /**
         * The constructor.
         *
         * @param f the files left to parse/import
         * @param layout do a autolayout afterwards
         */
        public ImportRun(List f,  boolean layout) {

            iss.addCancelButtonListener(new ActionListener() {
                public void actionPerformed(ActionEvent actionEvent) {
                    cancel();
                }
            });

            filesLeft = f;
            countFiles = filesLeft.size();
            countFilesThisPass = countFiles;
            nextPassFiles = new Vector();
            st = new SimpleTimer();
            st.mark("start");
            cancelled = false;
            criticThreadWasOn = Designer.theDesigner().getAutoCritique();
            if (criticThreadWasOn) {
                Designer.theDesigner().setAutoCritique(false);
            }
            this.doLayout = layout;
        }

        /**
         * Called once for each file to be parsed.<p>
         *
         * To refresh the GUI it calls itself again using the
         * {@link SwingUtilities#invokeLater(Runnable)} method.<p>
         */
        public void run() {
            // We shouldn't really turn off events, but as long
            // as we are, wrap in try block to make sure they get
            // turned back on.
            Model.getPump().stopPumpingEvents();
            try {
                if (filesLeft.size() > 0) {

                    // if there ae 2 passes:
                    if (importLevel > 0) {
                        if (filesLeft.size() <= countFiles / 2) {
                            if (importLevel == 1) {
                                setImportLevel(1);
                            } else {
                                setImportLevel(2);
                            }
                        }
                    }

                    Object curFile = filesLeft.get(0);
                    filesLeft.remove(0);

                    try {
                        st.mark(curFile.toString());
                        ProjectBrowser.getInstance().showStatus(
                                "Importing " + curFile.toString());
                        parseFile(
                                ProjectManager.getManager()
                                    .getCurrentProject(),
                                curFile); // Try to parse this file.

                        int tot = countFiles;
                        if (diagramInterface != null) {
                            tot +=
                                diagramInterface.getModifiedDiagrams().size()
                                / 10;
                        }
                        iss.setMaximum(tot);
                        int act =
                            countFiles
                            - filesLeft.size()
                            - nextPassFiles.size();
                        iss.setValue(act);
                        ProjectBrowser.getInstance().getStatusBar()
                                .showProgress(100 * act / tot);
                    } catch (Exception e1) {

                        nextPassFiles.addElement(curFile);
                        StringBuffer sb = new StringBuffer(80);
                        // RuntimeExceptions should be reported here!
                        if (e1 instanceof RuntimeException) {
                            sb.append("Program bug encountered while parsing ");
                            sb.append(curFile.toString());
                            sb.append(", so some elements are not"
                                    + " created in the model\n");
                            StringWriter sw = new StringWriter();
                            PrintWriter pw = new java.io.PrintWriter(sw);
                            e1.printStackTrace(pw);
                            sb.append(sw.getBuffer());
                            LOG.error(sb.toString(), e1);
                        } else {
                            sb.append("Uncaught exception encountered"
                                    + " while parsing ");
                            sb.append(curFile.toString());
                            sb.append(", so some elements are not "
                                    + "created in the model\n");
                            StringWriter sw = new StringWriter();
                            PrintWriter pw = new java.io.PrintWriter(sw);
                            e1.printStackTrace(pw);
                            sb.append(sw.getBuffer());
                            LOG.warn(sb.toString(), e1);
                        }
                        problems.append(sb);
                    }

                    if (!isCancelled()) {
                        SwingUtilities.invokeLater(this);
                        return;
                    }
                }

                if (nextPassFiles.size() > 0
                        && nextPassFiles.size() < countFilesThisPass) {
                    filesLeft = nextPassFiles;
                    nextPassFiles = new Vector();
                    countFilesThisPass = filesLeft.size();
                    SwingUtilities.invokeLater(this);
                    return;
                }

                // Do post load processings.
                st.mark("postprocessings");

                ProjectBrowser.getInstance().showStatus("Import done");

                // Layout the modified diagrams.
                if (!isCancelled() && doLayout) {
                    st.mark("layout");
                    if (diagramInterface != null) {
                        for (int i = 0; i < diagramInterface
                                .getModifiedDiagrams().size(); i++) {
                            UMLDiagram diagram =
                                (UMLDiagram) diagramInterface
                                    .getModifiedDiagrams().elementAt(i);
                            ClassdiagramLayouter layouter;
                            if (module instanceof PluggableImport) {
                                // There are no known modules which implement this,
                                // but just in case ...
                                layouter = ((PluggableImport) module)
                                        .getLayout(diagram);
                            } else {
                                layouter = new ClassdiagramLayouter(diagram);
                            }

                            layouter.layout();

                            // Resize the diagram???
                            iss.setValue(countFiles + (i + 1) / 10);
                        }
                    }
                }

                iss.done();
                ArgoFrame.getInstance().setCursor(
                        Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                // if errors occured, display the collected messages here
                if (problems != null && problems.length() > 0) {
                    ProblemsDialog pd = new ProblemsDialog();
                    pd.setVisible(true);
                }
                
                // TODO: Inform user if no files were imported

                // turn critiquing on again
                if (criticThreadWasOn) {
                    Designer.theDesigner().setAutoCritique(true);
                }

                ExplorerEventAdaptor.getInstance().structureChanged();

                LOG.info(st);
                ProjectBrowser.getInstance().getStatusBar().showProgress(0);
            } finally {
                // Be sure event pump gets turned back on again.
                // Note: this doesn't deal with the critics
                Model.getPump().startPumpingEvents();
            }
        }

        private void cancel() { cancelled = true; }

        private boolean isCancelled() { return cancelled; }

    }

    /**
     * A window that shows the progress bar and a cancel button.
     * TODO: React on the close button as if the Cancel button was pressed.
     */
    class ImportStatusScreen extends JDialog {

        private JButton cancelButton;

        private JLabel progressLabel;

        private int numberOfFiles;

        private JProgressBar progress;

        /**
         * The constructor.
         *
         * @param title
         * @param iconName
         */
        public ImportStatusScreen(String title, String iconName) {

            super();
            if (title != null) {
                setTitle(title);
            }
            Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
            getContentPane().setLayout(new BorderLayout(0, 0));

            // Parsing file x of z.
            JPanel topPanel = new JPanel();
            progressLabel = new JLabel();
            topPanel.add(progressLabel);
            getContentPane().add(topPanel, BorderLayout.NORTH);

            // progress bar
            progress = new JProgressBar();
            getContentPane().add(progress, BorderLayout.CENTER);

            // stop button
            cancelButton = new JButton("Stop");
            JPanel bottomPanel = new JPanel();
            bottomPanel.add(cancelButton);
            getContentPane().add(bottomPanel, BorderLayout.SOUTH);

            Dimension contentPaneSize = getContentPane().getPreferredSize();
            setLocation(scrSize.width / 2 - contentPaneSize.width / 2,
                    scrSize.height / 2 - contentPaneSize.height / 2);
            pack();
            this.setResizable(false);
            this.setModal(true);        //MVW - Issue 2539.
        }

        public void setMaximum(int i) {
            progress.setMaximum(i);
            numberOfFiles = i;
        }

        public void setValue(int i) {

            progress.setValue(i);

            String pass = "1-st pass";
            // if there are 2 passes:
            if (importLevel > 0 && i >= numberOfFiles / 2) {
                pass = "2-nd pass";
            }

            int fileNumber =
                (i != 1 && numberOfFiles != 0)
                    ? ((i - 1) % (numberOfFiles / 2) + 1)
                        : 1;

            progressLabel.setText("Parsing file "
                  + fileNumber + " of " + numberOfFiles / 2
                  + ", " + pass + ". ");
            pack(); // MVW: Is this not time-consuming?
                    // Better make the window big enough at the start,
                    // and only refresh the label.
        }

        public void addCancelButtonListener(ActionListener al) {
            cancelButton.addActionListener(al);
        }

        public void done() {
            setVisible(false);
            dispose();
        }

        /**
         * The UID.
         */
        private static final long serialVersionUID = -1336242911879462274L;
    }


    /**
     * A window that shows the problems occured during import.
     */
    class ProblemsDialog extends JDialog implements ActionListener {

        private JButton closeButton;
        private JLabel northLabel;

        /**
         * The constructor.
         */
        public ProblemsDialog() {
            super();
            setResizable(true);
            setModal(false);
            setTitle(Translator.localize("dialog.title.import-problems"));

            Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
            getContentPane().setLayout(new BorderLayout(0, 0));

            // the introducing label
            northLabel =
                new JLabel(Translator.localize("label.import-problems"));
            getContentPane().add(northLabel, BorderLayout.NORTH);

            // the text box containing the problem messages
            JEditorPane textArea = new JEditorPane();
            textArea.setText(problems.toString());
            JPanel centerPanel = new JPanel(new BorderLayout());
            centerPanel.add(new JScrollPane(textArea));
            centerPanel.setPreferredSize(new Dimension(300, 200));
            getContentPane().add(centerPanel);

            // close button
            closeButton = new JButton(Translator.localize("button.close"));
            JPanel bottomPanel = new JPanel();
            bottomPanel.add(closeButton);
            getContentPane().add(bottomPanel, BorderLayout.SOUTH);

            // listeners
            closeButton.addActionListener(this);
            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent evt) {
                    disposeDialog();
                }
            });

            Dimension contentPaneSize = getContentPane().getPreferredSize();
            setLocation(scrSize.width / 2 - contentPaneSize.width / 2,
                    scrSize.height / 2 - contentPaneSize.height / 2);
            pack();
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            disposeDialog();
        }

        public void disposeDialog() {
            setVisible(false); 
            dispose();
        }

        /**
         * The UID.
         */
        private static final long serialVersionUID = -9221358976863603143L;
    }


}

/**
 * dialog to setup the import classpath.
 */
class ImportClasspathDialog extends JDialog {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(ImportClasspathDialog.class);

    private JList paths;
    private DefaultListModel pathsModel;

    private JButton addFile;

    private JButton removeFile;

    private JButton ok;

    private Import importProcess;

    /**
     * Construct a dialog to allow the user to set up the classpath for the
     * import.
     * 
     * @param importProcess1
     */
    public ImportClasspathDialog(Import importProcess1) {

        super();
        setTitle("Set up the import classpath"); //MVW - Issue 2539.
        importProcess = importProcess1;

        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        getContentPane().setLayout(new BorderLayout(0, 0));

        // paths list
        pathsModel = new DefaultListModel();
        paths = new JList(pathsModel);
        paths.setVisibleRowCount(5);
        JScrollPane listScroller = new JScrollPane(paths);
        listScroller.setPreferredSize(new Dimension(250, 80));
        getContentPane().add(listScroller, BorderLayout.CENTER);

        initList();

        // controls
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new GridLayout(0, 3));
        addFile = new JButton("Add");
        removeFile = new JButton("Remove");
        ok = new JButton("Ok");
        controlsPanel.add(addFile);
        controlsPanel.add(removeFile);
        controlsPanel.add(ok);
        getContentPane().add(controlsPanel, BorderLayout.SOUTH);

        addFile.addActionListener(new AddListener());
        removeFile.addActionListener(new RemoveListener());
        ok.addActionListener(new OkListener());

        //Display the window.
        Dimension contentPaneSize = getContentPane().getPreferredSize();
        setLocation(scrSize.width / 2 - contentPaneSize.width / 2,
            scrSize.height / 2 - contentPaneSize.height / 2);
        pack();
        setVisible(true);
        this.setModal(true);        //MVW   Issue 2539.
    }

    private void initList() {

        URL[] urls =
            ImportClassLoader.getURLs(Configuration.getString(
                Argo.KEY_USER_IMPORT_CLASSPATH, ""));

        for (int i = 0; i < urls.length; i++) {
            pathsModel.addElement(urls[i].getFile());
        }

        paths.setSelectedIndex(0);
    }


    private void doFiles() {
        importProcess.doFile();
    }

    class OkListener implements ActionListener {
        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {

            URL[] urls = new URL[pathsModel.size()];
            for (int i = 0; i < urls.length; i++) {
                try {
                    urls[i] = new File((String) pathsModel.get(i)).toURL();
                } catch (Exception e1) {
                    LOG.warn("could not do ok: could not make"
                            + "url " + pathsModel.get(i) + ", " + e1,
                            e1);
                }
            }

            try {
                ImportClassLoader.getInstance(urls);
                ImportClassLoader.getInstance().saveUserPath();
            } catch (Exception e1) {
                LOG.warn("could not do ok", e1);
            }
            doFiles();
            dispose();
        }
    }

    class RemoveListener implements ActionListener {
        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            //This method can be called only if
            //there's a valid selection
            //so go ahead and remove whatever's selected.
            int index = paths.getSelectedIndex();
            pathsModel.remove(index);

            int size = pathsModel.getSize();

            if (size == 0) { //nothings left, disable firing.
                removeFile.setEnabled(false);

            } else { //Select an index.
                if (index == pathsModel.getSize()) {
                    //removed item in last position
                    index--;
                }

                paths.setSelectedIndex(index);
                paths.ensureIndexIsVisible(index);
            }
        }
    }


    class AddListener implements ActionListener {
        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {

            String directory = Globals.getLastDirectory();
            JFileChooser ch = new JFileChooser(directory);
            if (ch == null) {
                ch = new JFileChooser();
            }

            final JFileChooser chooser = ch;

            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

            chooser.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (e.getActionCommand().equals(
                            JFileChooser.APPROVE_SELECTION)) {
                        File theFile = chooser.getSelectedFile();
                        if (theFile != null) {
                            pathsModel.addElement(theFile.toString());
                        }
                    } else if (e.getActionCommand().equals(
                            JFileChooser.CANCEL_SELECTION)) {
                        // TODO: What shall we do here?
                    }
                }
            });

            chooser.showOpenDialog(ArgoFrame.getInstance());
        }
    }


    /**
     * The UID.
     */
    private static final long serialVersionUID = -8684620532717336574L;
}

