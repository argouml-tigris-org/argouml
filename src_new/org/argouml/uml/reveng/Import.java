// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

    private static final String SEPARATOR = "/";

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

    private StringBuffer problems = new StringBuffer();
    
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
                        "action.import-option-classfiers"));
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
        iss = new ImportStatusScreen("Importing", "Splash");
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

        /*
         * @see javax.swing.JFileChooser#JFileChooser(String)
         */
        public ImportFileChooser(Import imp, String currentDirectoryPath) {
            super(currentDirectoryPath);
            theImport = imp;
        }

        /*
         * @see javax.swing.JFileChooser#JFileChooser(String, FileSystemView)
         */
        public ImportFileChooser(
                Import imp,
                String currentDirectoryPath,
                FileSystemView fsv) {
            super(currentDirectoryPath, fsv);
            theImport = imp;
        }

        /*
         * @see javax.swing.JFileChooser#JFileChooser()
         */
        public ImportFileChooser(Import imp) {
            super();
            theImport = imp;
        }

        /*
         * @see javax.swing.JFileChooser#JFileChooser(FileSystemView)
         */
        public ImportFileChooser(
                Import imp,
                FileSystemView fsv) {
            super(fsv);
            theImport = imp;
        }

        /*
         * @see javax.swing.JFileChooser#approveSelection()
         */
        public void approveSelection() {
            theImport.setSelectedFile(getSelectedFile());
            if (getSelectedFile() != null) {
                String path = getSelectedFile().getParent();
                String filename =
                        path + SEPARATOR + getSelectedFile().getName();
                Globals.setLastDirectory(path);
                if (filename != null) {
                    theImport.disposeDialog();
                    // TODO: This is only relevant for Java import
                    // move out of normal control flow.  The OK button of this
                    // dialog transfers to Import.doFile
                    new ImportClasspathDialog(theImport);
                    return;
                }
            }
        }

        /*
         * @see javax.swing.JFileChooser#cancelSelection()
         */
        public void cancelSelection() {
            theImport.disposeDialog();
        }

    }


    /**
     * A window that shows the progress bar and a cancel button.
     * As a convenience to callers which may be executing on a thread other
     * than the Swing event thread, all methods use SwingUtilities.invokeLater()
     * to make sure that Swing calls happen on the appropriate thread.
     *
     * TODO: React on the close button as if the Cancel button was pressed.
     * 
     * TODO: Refactor to use a common progress dialog.  There's really no reason
     * to have our own specific implementation - tfm - 20070201
     */
    class ImportStatusScreen extends JDialog implements ProgressMonitor {

        private JButton cancelButton;

        private JLabel progressLabel;

        private JProgressBar progress;

        private boolean cancelled = false;

        /**
         * The constructor.
         *
         * @param title
         * @param iconName
         */
        public ImportStatusScreen(String title, String iconName) {
            super(myFrame, true);
            if (title != null) {
                setTitle(title);
            }
            Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
            getContentPane().setLayout(new BorderLayout(4, 4));

            // Parsing file x of z.
            JPanel topPanel = new JPanel();
            progressLabel = new JLabel();
            progressLabel.setPreferredSize(new Dimension(400, 20));
            progressLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            topPanel.add(progressLabel);
            getContentPane().add(topPanel, BorderLayout.NORTH);

            // progress bar
            progress = new JProgressBar();
            progress.setPreferredSize(new Dimension(350, 20));
            getContentPane().add(progress, BorderLayout.CENTER);

            // stop button
            cancelButton = new JButton(Translator.localize("button.cancel"));
            JPanel bottomPanel = new JPanel();
            bottomPanel.add(cancelButton);
            getContentPane().add(bottomPanel, BorderLayout.SOUTH);
            cancelButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    cancelled = true;

                }

            });

            pack();
            Dimension contentPaneSize = getContentPane().getPreferredSize();
            setLocation(scrSize.width / 2 - contentPaneSize.width / 2,
                    scrSize.height / 2 - contentPaneSize.height / 2);
            setResizable(false);
        }

        public void setMaximumProgress(final int i) {
            SwingUtilities.invokeLater(new Runnable () {
                public void run() {
                    progress.setMaximum(i);
                    setVisible(true);
                }
            });
        }

        public void updateProgress(final int i) {
            SwingUtilities.invokeLater(new Runnable () {
                public void run() {
                    progress.setValue(i);
                }
            });
        }

        /**
         * The UID.
         */
        private static final long serialVersionUID = -1336242911879462274L;

        /*
         * @see org.argouml.application.api.ProgressMonitor#close()
         */
        public void close() {
            SwingUtilities.invokeLater(new Runnable () {
                public void run() {
                    setVisible(false);
                    dispose();
                }
            });
        }

        /*
         * @see org.argouml.application.api.ProgressMonitor#isCanceled()
         */
        public boolean isCanceled() {
            return cancelled;
        }

        /*
         * @see org.argouml.application.api.ProgressMonitor#notifyMessage(java.lang.String, java.lang.String, java.lang.String)
         */
        public void notifyMessage(String title, String introduction,
                String message) {
            // TODO: Create an error dialog or panel in our progress dialog
            // for now we just use our old style separate error dialog
            JDialog problemsDialog = new ProblemsDialog(message);
            problemsDialog.setTitle(title);
            problemsDialog.setVisible(true);
            // TODO: Only needed while we have a separate problem dialog
            // (see above)
            setVisible(false);
            dispose();
        }

        /*
         * @see org.argouml.application.api.ProgressMonitor#notifyNullAction()
         */
        public void notifyNullAction() {
            String msg = Translator.localize("label.import.empty");
            notifyMessage(msg, msg, msg);
        }

        /*
         * @see org.argouml.application.api.ProgressMonitor#updateMainTask(java.lang.String)
         */
        public void updateMainTask(final String name) {
            SwingUtilities.invokeLater(new Runnable () {
                public void run() {
                    setTitle(name);
                }
            });
        }

        /*
         * @see org.argouml.application.api.ProgressMonitor#updateSubTask(java.lang.String)
         */
        public void updateSubTask(final String action) {
            SwingUtilities.invokeLater(new Runnable () {
                public void run() {
                    progressLabel.setText(action);
                }
            });
        }

        /*
         * @see org.argouml.persistence.ProgressListener#progress(org.argouml.persistence.ProgressEvent)
         */
        public void progress(ProgressEvent event) throws InterruptedException {
            // ignored
        }

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
            this(problems.toString());
        }

        /**
         * The constructor.
         */
        public ProblemsDialog(String errors) {
            super();
            setResizable(true);
            setModal(true);
            setTitle(Translator.localize("dialog.title.import-problems"));

            Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
            getContentPane().setLayout(new BorderLayout(0, 0));

            // the introducing label
            northLabel =
                new JLabel(Translator.localize("label.import-problems"));
            getContentPane().add(northLabel, BorderLayout.NORTH);

            // the text box containing the problem messages
            JEditorPane textArea = new JEditorPane();
            textArea.setText(errors);
            JPanel centerPanel = new JPanel(new BorderLayout());
            centerPanel.add(new JScrollPane(textArea));
            centerPanel.setPreferredSize(new Dimension(600, 200));
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

            pack();
            Dimension contentPaneSize = getContentPane().getSize();
            setLocation(scrSize.width / 2 - contentPaneSize.width / 2,
                    scrSize.height / 2 - contentPaneSize.height / 2);
        }

        /*
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

