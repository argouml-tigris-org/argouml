// $Id$
// Copyright (c) 1996-2008 The Regents of the University of California. All
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
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import java.util.StringTokenizer;

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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileSystemView;

import org.apache.log4j.Logger;
import org.argouml.application.api.Argo;
import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;
import org.argouml.moduleloader.ModuleInterface;
import org.argouml.taskmgmt.ProgressEvent;
import org.argouml.taskmgmt.ProgressMonitor;
import org.argouml.uml.reveng.java.JavaImport;
import org.argouml.uml.reveng.ui.ImportStatusScreen;
import org.argouml.util.SuffixFilter;
import org.argouml.util.UIUtils;
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
 * @author Andreas Rueckert a_rueckert@gmx.net
 */
public class Import extends ImportCommon implements ImportSettings {

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
//    private int importLevel;

    private JTextField inputSourceEncoding;

    private JDialog dialog;

    private ImportStatusScreen iss;
    
    private Frame myFrame;


    /**
     * The default extended configuration panel.
     * TODO: This used to be provided by the abstract class FileImportSupport
     * and it can be merged with our main configuration panel here.
     */
    private ConfigPanelExtension importConfigPanel;

    /**
     * Creates dialog window with chooser and configuration panel.
     *
     * @param frame the ui frame to display dialogs on
     */
    public Import(Frame frame) {
        super();
        myFrame = frame;

        JComponent chooser = getChooser();
        dialog =
            new JDialog(frame,
                    Translator.localize("action.import-sources"), true);

        dialog.getContentPane().add(chooser, BorderLayout.CENTER);
        dialog.getContentPane().add(getConfigPanel(this), BorderLayout.EAST);
        dialog.pack();
        int x =
            (frame.getSize().width
             - dialog.getSize().width)
            / 2;
        int y =
            (frame.getSize().height
             - dialog.getSize().height)
            / 2;
        dialog.setLocation(x > 0 ? x : 0, y > 0 ? y : 0);

        UIUtils.loadCommonKeyMap(dialog);

        dialog.setVisible(true);
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

    /**
     * Close dialog window.
     */
    private void disposeDialog() {
        StringBuffer flags = new StringBuffer(30);
        flags.append(isDescendSelected()).append(",");
        flags.append(isChangedOnlySelected()).append(",");
        flags.append(isCreateDiagramsSelected()).append(",");
        flags.append(isMinimizeFigsSelected()).append(",");
        flags.append(isDiagramLayoutSelected());
        Configuration.setString(Argo.KEY_IMPORT_GENERAL_SETTINGS_FLAGS,
                flags.toString());
        Configuration.setString(Argo.KEY_IMPORT_GENERAL_DETAIL_LEVEL,
                String.valueOf(getImportLevel()));
        Configuration.setString(Argo.KEY_INPUT_SOURCE_ENCODING,
            getInputSourceEncoding());
        if (importConfigPanel != null) {
            importConfigPanel.disposeDialog();
        }
        dialog.setVisible(false);
        dialog.dispose();
    }

    /**
     * Get the panel that lets the user set reverse engineering
     * parameters.
     *
     * @param importInstance the instance of the import
     * @return the panel
     * This is an internal method.  Use the accessors in
     * {@link ImportSettings} to determine the current settings.
     */
    private JComponent getConfigPanel(final Import importInstance) {

        final JTabbedPane tab = new JTabbedPane();

        // build the configPanel:
        if (configPanel == null) {
            JPanel general = new JPanel();
            general.setLayout(
                    new GridLayout2(13, 1, 0, 0, GridLayout2.NONE));

            general.add(new JLabel(
                    Translator.localize("action.import-select-lang")));

            JComboBox selectedLanguage = 
                new JComboBox(getModules().keySet().toArray());
            selectedLanguage.addActionListener(
                    new SelectedLanguageListener(importInstance, tab));
            general.add(selectedLanguage);

            boolean desc = true;
            boolean chan = true;
            boolean crea = true;
            boolean mini = true;
            boolean layo = true;
            String flags =
                    Configuration
                            .getString(Argo.KEY_IMPORT_GENERAL_SETTINGS_FLAGS);
            if (flags != null && flags.length() > 0) {
                StringTokenizer st = new StringTokenizer(flags, ",");
                if (st.hasMoreTokens() && st.nextToken().equals("false")) {
                    desc = false;
                }
                if (st.hasMoreTokens() && st.nextToken().equals("false")) {
                    chan = false;
                }
                if (st.hasMoreTokens() && st.nextToken().equals("false")) {
                    crea = false;
                }
                if (st.hasMoreTokens() && st.nextToken().equals("false")) {
                    mini = false;
                }
                if (st.hasMoreTokens() && st.nextToken().equals("false")) {
                    layo = false;
                }
            }
            
            descend =
                new JCheckBox(Translator.localize(
                        "action.import-option-descend-dir-recur"), desc);
            general.add(descend);

            changedOnly =
                new JCheckBox(Translator.localize(
                        "action.import-option-changed_new"), chan);
            general.add(changedOnly);

            createDiagrams =
                new JCheckBox(Translator.localize(
                        "action.import-option-create-diagram"), crea);
            general.add(createDiagrams);

            minimiseFigs =
                new JCheckBox(Translator.localize(
                        "action.import-option-min-class-icon"), mini);
            general.add(minimiseFigs);

            layoutDiagrams =
                new JCheckBox(Translator.localize(
                        "action.import-option-perform-auto-diagram-layout"),
                        layo);
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
                        "action.import-option-classifiers"));
            detailButtonGroup.add(classOnly);

            classAndFeatures =
                new JRadioButton(Translator.localize(
                        "action.import-option-classifiers-plus-specs"));
            detailButtonGroup.add(classAndFeatures);

            fullImport =
                new JRadioButton(Translator.localize(
                        "action.import-option-full-import"));
            String detaillevel =
                Configuration.getString(Argo.KEY_IMPORT_GENERAL_DETAIL_LEVEL);
            if ("0".equals(detaillevel)) {
                classOnly.setSelected(true);
            } else if ("1".equals(detaillevel)) {
                classAndFeatures.setSelected(true);
            } else {
                fullImport.setSelected(true);
            }
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

            // TODO: Encoding needs to be validated against set of 
            // available encodings using {@link Charset.isSupported(String)}
            // -- or use a menu with the contents of 
            // {@link Charset.availableCharsets()}            
//            JComboBox encoding =
//                    new JComboBox(Charset.availableCharsets().keySet()
//                            .toArray());
//            encoding.setSelectedItem(inputSourceEncoding.getText());
//            general.add(encoding);

            tab.add(general, Translator.localize("action.import-general"));
            tab.add(getConfigPanelExtension(), 
                    ((ModuleInterface) getCurrentModule()).getName());
            configPanel = tab;
        }
        return configPanel;

    }

    /*
     * Get the extension panel for the configuration settings.
     */
    private JComponent getConfigPanelExtension() {
        // New style importers don't provide a config panel
        // TODO: This needs review for the new style importers - tfm - 20070527
        if (importConfigPanel == null) {
            importConfigPanel = new ConfigPanelExtension();
        }
        return importConfigPanel;
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
            ImportInterface oldModule = getCurrentModule();
            setCurrentModule(getModules().get(selected));
            updateFilters(
                    (JFileChooser) dialog.getContentPane().getComponent(0),
                    oldModule.getSuffixFilters(),
                    getCurrentModule()
                    .getSuffixFilters());
            // TODO: Update configPanelExtension with extension settings
            // for new language
            
        }
    }

    /**
     * Parse all selected files. It calls the actual
     * parser methods depending on the type of the file.<p>
     */
    public void doFile() {
        iss = new ImportStatusScreen(myFrame, "Importing", "Splash");
        Thread t = new Thread(new Runnable() {
            public void run() {
                doImport(iss);
                // ExplorerEventAdaptor.getInstance().structureChanged();
                // ProjectBrowser.getInstance().getStatusBar().showProgress(0);
            }
        }, "Import Thread");
        t.start();
    }

    /*
     * @see org.argouml.uml.reveng.ImportCommon#getImportLevel()
     */
    public int getImportLevel() {
        if (classOnly != null && classOnly.isSelected()) {
            return ImportSettings.DETAIL_CLASSIFIER;
        } else if (classAndFeatures != null && classAndFeatures.isSelected()) {
            return ImportSettings.DETAIL_CLASSIFIER_FEATURE;
        } else if (fullImport != null && fullImport.isSelected()) {
            return ImportSettings.DETAIL_FULL;
        } else {
            return ImportSettings.DETAIL_CLASSIFIER;
        }
    }

    /*
     * @see org.argouml.uml.reveng.ImportCommon#isCreateDiagramsChecked()
     */
    public boolean isCreateDiagramsSelected() {
        if (createDiagrams != null) {
            return createDiagrams.isSelected();
        }
        return true;
    }

    /*
     * @see org.argouml.uml.reveng.ImportCommon#isMinimiseFigsChecked()
     */
    public boolean isMinimizeFigsSelected() {
        if (minimiseFigs != null) {
            return minimiseFigs.isSelected();
        }
        return false;
    }


    /*
     * @see org.argouml.uml.reveng.ImportCommon#isDiagramLayoutSelected()
     */
    public boolean isDiagramLayoutSelected() {
        if (layoutDiagrams != null) {
            return layoutDiagrams.isSelected();
        }
        return false;
    }


    /*
     * @see org.argouml.uml.reveng.ImportCommon#isDescendSelected()
     */
    public boolean isDescendSelected() {
        if (descend != null) {
            return descend.isSelected();
        }
        return true;
    }

    /*
     * @see org.argouml.uml.reveng.ImportCommon#isChangedOnlySelected()
     */
    public boolean isChangedOnlySelected() {
        if (changedOnly != null) {
            return changedOnly.isSelected();
        }
        return false;
    }


    /*
     * Create chooser for objects we are to import. Old style modules get to
     * provide their own (although I don't believe any of them do), while new
     * style modules get the a chooser provided by us (which matches what the
     * abstract class FileImportSupport used to provide).
     */
    private JComponent getChooser() {
        String directory = Globals.getLastDirectory();

        final JFileChooser chooser = new ImportFileChooser(this, directory);

        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        updateFilters(chooser, null, getCurrentModule().getSuffixFilters());

        return chooser;
    }

    private static void updateFilters(JFileChooser chooser,
            SuffixFilter[] oldFilters, SuffixFilter[] newFilters) {
        if (oldFilters != null) {
            for (int i = 0; i < oldFilters.length; i++) {
                chooser.removeChoosableFileFilter(oldFilters[i]);
            }
        }
        if (newFilters != null) {
            for (int i = 0; i < newFilters.length; i++) {
                chooser.addChoosableFileFilter(newFilters[i]);
            }
        }
    }


    private static class ImportFileChooser extends JFileChooser {

        private Import theImport;

        /**
         * Constructs a new ImportFileChooser opened to the given directory.
         * 
         * @param imp the import manager
         * @param currentDirectoryPath the directory path
         * @see javax.swing.JFileChooser#JFileChooser(String)
         */
        public ImportFileChooser(Import imp, String currentDirectoryPath) {
            super(currentDirectoryPath);
            theImport = imp;
            initChooser();
        }

        /**
         * Constructs a JFileChooser using the given current directory path and
         * FileSystemView.
         * 
         * @param imp the import manager
         * @param currentDirectoryPath the directory path
         * @param fsv the file system view
         * @see javax.swing.JFileChooser#JFileChooser(String, FileSystemView)
         */
        public ImportFileChooser(
                Import imp,
                String currentDirectoryPath,
                FileSystemView fsv) {
            super(currentDirectoryPath, fsv);
            theImport = imp;
            initChooser();
        }

        /**
         * Constructs a new default ImportFileChooser.
         * 
         * @param imp the import manager
         * @see javax.swing.JFileChooser#JFileChooser()
         */
        public ImportFileChooser(Import imp) {
            super();
            theImport = imp;
            initChooser();
        }

        /**
         * Constructs a JFileChooser using the given FileSystemView.
         * 
         * @param imp the import manager
         * @param fsv the file system view
         * @see javax.swing.JFileChooser#JFileChooser(FileSystemView)
         */
        public ImportFileChooser(
                Import imp,
                FileSystemView fsv) {
            super(fsv);
            theImport = imp;
            initChooser();
        }


        private void initChooser() {
            setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            setMultiSelectionEnabled(true);
            setSelectedFile(getCurrentDirectory());
        }
        
        /*
         * @see javax.swing.JFileChooser#approveSelection()
         */
        @Override
        public void approveSelection() {
            File[] files = getSelectedFiles();
            File dir = getCurrentDirectory();
            if (files.length == 0) {
                files = new File[] {dir};
            }
            if (files.length == 1) {
                File file = files[0];
                if (file != null && file.isDirectory()) {
                    dir = file;
                } else {
                    dir = file.getParentFile();
                }
            }
            theImport.setSelectedFiles(files);
            Globals.setLastDirectory(dir.getPath());
            theImport.disposeDialog();
            
            if (theImport.getCurrentModule() instanceof JavaImport) {
                // The OK button of this
                // dialog transfers to Import.doFile
                new ImportClasspathDialog(theImport);
            } else {
                theImport.doFile();
            } 
        }

        /*
         * @see javax.swing.JFileChooser#cancelSelection()
         */
        @Override
        public void cancelSelection() {
            theImport.disposeDialog();
        }

    }

    /**
     * @return Returns the Frame.
     */
    public Frame getFrame() {
        return myFrame;
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

    private JDialog importClasspathDialog;
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
        importClasspathDialog = this;
        setTitle(Translator.localize("dialog.import.classpath.title"));
        importProcess = importProcess1;

        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        getContentPane().setLayout(new BorderLayout(0, 0));

        // Explanatory text
        JTextArea ta =
                new JTextArea(Translator
                        .localize("dialog.import.classpath.text"));
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setFocusable(false);
        getContentPane().add(ta, BorderLayout.NORTH);

        // paths list
        pathsModel = new DefaultListModel();
        paths = new JList(pathsModel);
        paths.setVisibleRowCount(5);
        JScrollPane listScroller = new JScrollPane(paths);
        listScroller.setPreferredSize(new Dimension(300, 100));
        getContentPane().add(listScroller, BorderLayout.CENTER);

        initList();

        // controls
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new GridLayout(0, 3));
        addFile = new JButton(Translator.localize("button.add"));
        removeFile = new JButton(Translator.localize("button.remove"));
        ok = new JButton(Translator.localize("button.ok"));
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
        ok.requestFocusInWindow();
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


    class OkListener implements ActionListener {
        /*
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            try {
                URL[] urls = new URL[pathsModel.size()];
                for (int i = 0; i < urls.length; i++) {
                    try {
                        urls[i] = new File((String) pathsModel.get(i)).toURI()
                                .toURL();
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
                setVisible(false);
                setModal(false);
                dispose();
                importProcess.doFile();
            } finally {
                setVisible(false);
                setModal(false);
                dispose();
            }
        }
    }

    class RemoveListener implements ActionListener {
        /*
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
        /*
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
                public void actionPerformed(ActionEvent e1) {
                    if (e1.getActionCommand().equals(
                            JFileChooser.APPROVE_SELECTION)) {
                        File theFile = chooser.getSelectedFile();
                        if (theFile != null) {
                            pathsModel.addElement(theFile.toString());
                        }
                    } else if (e1.getActionCommand().equals(
                            JFileChooser.CANCEL_SELECTION)) {
                        // TODO: What shall we do here?
                    }
                    // bring the import classpath dialog to the front
                    importClasspathDialog.setVisible(true);
                }
            });

            chooser.showOpenDialog(importProcess.getFrame());
        }
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -8684620532717336574L;
}
