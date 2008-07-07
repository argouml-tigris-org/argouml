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
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import org.argouml.application.api.Argo;
import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;
import org.argouml.moduleloader.ModuleInterface;
import org.argouml.uml.reveng.SettingsTypes.BooleanSelection2;
import org.argouml.uml.reveng.SettingsTypes.PathListSelection;
import org.argouml.uml.reveng.SettingsTypes.PathSelection;
import org.argouml.uml.reveng.SettingsTypes.Setting;
import org.argouml.uml.reveng.SettingsTypes.UniqueSelection2;
import org.argouml.uml.reveng.SettingsTypes.UserString2;
import org.argouml.uml.reveng.ui.ImportClasspathDialog;
import org.argouml.uml.reveng.ui.ImportStatusScreen;
import org.argouml.util.SuffixFilter;
import org.argouml.util.UIUtils;
import org.tigris.gef.base.Globals;
import org.tigris.swidgets.GridLayout2;



/**
 * This is the main class for the Swing importer framework.  It extends 
 * ImportCommon which contains all the GUI independent pieces of the import
 * framework.  
 * <p>
 * The Service Providers Interface (SPI) to the individual language importers
 * is defined in such a way that they can be completely GUI independent as
 * well, receiving lists of source files and settings for the import and 
 * reporting progress via progress monitor API.
 * 
 * It provides JPanels for tailoring the import run in the FileChooser.
 * <p>
 * 
 * The Import run is started by calling doFile(Project, File)
 * <p>
 * 
 * Supports recursive search in folder for source files with matching 
 * extensions.
 * <p>
 * 
 * There are three levels of detail for import:
 * <p>
 * 
 * <ol>
 * <li> 0 - classifiers only
 * <li> 1 - classifiers plus feature specifications
 * <li> 2 - full import, feature detail (ie. operations with methods)
 * </ol>
 * 
 * @author Andreas Rueckert a_rueckert@gmx.net
 * @author Tom Morris <tfmorris@gmail.com>
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

    private JComboBox sourceEncoding;
    
    private JDialog dialog;

    private ImportStatusScreen iss;

    private Frame myFrame;

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
        dialog.getContentPane().add(getConfigPanel(), BorderLayout.EAST);
        dialog.pack();
        int x = (frame.getSize().width - dialog.getSize().width) / 2;
        int y = (frame.getSize().height - dialog.getSize().height) / 2;
        dialog.setLocation(x > 0 ? x : 0, y > 0 ? y : 0);

        UIUtils.loadCommonKeyMap(dialog);

        dialog.setVisible(true);
    }

    /*
     * @see org.argouml.uml.reveng.ImportSettings#getInputSourceEncoding()
     */
    public String getInputSourceEncoding() {
        return (String) sourceEncoding.getSelectedItem();
    }

    /*
     * @see org.argouml.uml.reveng.ImportSettings#isAttributeSelected()
     */
    @Deprecated
    public boolean isAttributeSelected() {
        return false;
    }

    /*
     * @see org.argouml.uml.reveng.ImportSettings#isDatatypeSelected()
     */
    @Deprecated
    public boolean isDatatypeSelected() {
        return false;
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
        Configuration.setString(Argo.KEY_IMPORT_GENERAL_SETTINGS_FLAGS, flags
                .toString());
        Configuration.setString(Argo.KEY_IMPORT_GENERAL_DETAIL_LEVEL, String
                .valueOf(getImportLevel()));
        Configuration.setString(Argo.KEY_INPUT_SOURCE_ENCODING,
                getInputSourceEncoding());
        dialog.setVisible(false);
        dialog.dispose();
    }

    /**
     * Get the panel that lets the user set reverse engineering parameters.
     * 
     * @param importInstance the instance of the import
     * @return the panel This is an internal method. Use the accessors in
     *         {@link ImportSettings} to determine the current settings.
     */
    private JComponent getConfigPanel() {

        final JTabbedPane tab = new JTabbedPane();

        // build the configPanel:
        if (configPanel == null) {
            JPanel general = new JPanel();
            general.setLayout(new GridLayout2(20, 1, 0, 0, GridLayout2.NONE));

            general.add(new JLabel(Translator
                    .localize("action.import-select-lang")));

            JComboBox selectedLanguage = new JComboBox(getModules().keySet()
                    .toArray());
            selectedLanguage
                    .addActionListener(new SelectedLanguageListener(tab));
            general.add(selectedLanguage);

            addConfigCheckboxes(general);

            addDetailLevelButtons(general);

            addSourceEncoding(general);

            tab.add(general, Translator.localize("action.import-general"));
            tab.add(getConfigPanelExtension(),
                    ((ModuleInterface) getCurrentModule()).getName());
            configPanel = tab;
        }
        return configPanel;

    }


    private void addConfigCheckboxes(JPanel panel) {
        boolean desc = true;
        boolean chan = true;
        boolean crea = true;
        boolean mini = true;
        boolean layo = true;
        String flags = Configuration
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

        descend = new JCheckBox(Translator
                .localize("action.import-option-descend-dir-recur"), desc);
        panel.add(descend);

        changedOnly = new JCheckBox(Translator
                .localize("action.import-option-changed_new"), chan);
        panel.add(changedOnly);

        createDiagrams = new JCheckBox(Translator
                .localize("action.import-option-create-diagram"), crea);
        panel.add(createDiagrams);

        minimiseFigs = new JCheckBox(Translator
                .localize("action.import-option-min-class-icon"), mini);
        panel.add(minimiseFigs);

        layoutDiagrams = new JCheckBox(Translator.localize(
                "action.import-option-perform-auto-diagram-layout"),
                layo);
        panel.add(layoutDiagrams);

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
    }


    private void addDetailLevelButtons(JPanel panel) {
        // select the level of import
        // 0 - classifiers only
        // 1 - classifiers plus feature specifications
        // 2 - full import, feature detail

        JLabel importDetailLabel = new JLabel(Translator
                .localize("action.import-level-of-import-detail"));
        ButtonGroup detailButtonGroup = new ButtonGroup();

        classOnly = new JRadioButton(Translator
                .localize("action.import-option-classifiers"));
        detailButtonGroup.add(classOnly);

        classAndFeatures = new JRadioButton(Translator
                .localize("action.import-option-classifiers-plus-specs"));
        detailButtonGroup.add(classAndFeatures);

        fullImport = new JRadioButton(Translator
                .localize("action.import-option-full-import"));
        String detaillevel = Configuration
                .getString(Argo.KEY_IMPORT_GENERAL_DETAIL_LEVEL);
        if ("0".equals(detaillevel)) {
            classOnly.setSelected(true);
        } else if ("1".equals(detaillevel)) {
            classAndFeatures.setSelected(true);
        } else {
            fullImport.setSelected(true);
        }
        detailButtonGroup.add(fullImport);

        panel.add(importDetailLabel);
        panel.add(classOnly);
        panel.add(classAndFeatures);
        panel.add(fullImport);
    }
    
    
    private void addSourceEncoding(JPanel panel) {
        panel.add(new JLabel(
                Translator.localize("action.import-file-encoding")));
        String enc =
            Configuration.getString(Argo.KEY_INPUT_SOURCE_ENCODING);
        if (enc == null || enc.trim().equals("")) {
            enc = System.getProperty("file.encoding");
        }
        // cp1252 is often the default, but windows-1252 is the name listed
        // by Charset.availableCharsets
        if (enc.startsWith("cp")) {
            enc = "windows-" + enc.substring(2);
        }

        sourceEncoding = new JComboBox(Charset
                .availableCharsets().keySet().toArray());
        sourceEncoding.setSelectedItem(enc);
        panel.add(sourceEncoding);
    }
    
    /*
     * Get the extension panel for the configuration settings.
     */
    private JComponent getConfigPanelExtension() {
        List<Setting> settings = getCurrentModule().getImportSettings();
        return  new ConfigPanelExtension(settings);
    }

    private class SelectedLanguageListener implements ActionListener {

        /**
         * The pane.
         */
        private JTabbedPane tab;

        /**
         * The constructor.
         * 
         * @param i The current import.
         * @param t The pane.
         */
        SelectedLanguageListener(JTabbedPane t) {
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
            updateFilters((JFileChooser) dialog.getContentPane()
                    .getComponent(0), oldModule.getSuffixFilters(),
                    getCurrentModule().getSuffixFilters());
            updateTabbedPane();
        }
        
        private void updateTabbedPane() {
            String name = ((ModuleInterface) getCurrentModule()).getName();
            if (tab.indexOfTab(name) < 0) {
                tab.add(getConfigPanelExtension(), name);
            }
        }
    }
    


    /**
     * Parse all selected files. It calls the actual parser methods depending on
     * the type of the file.
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
            if (newFilters.length > 0) {
                chooser.setFileFilter(newFilters[0]);
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
        public ImportFileChooser(Import imp, String currentDirectoryPath,
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
        public ImportFileChooser(Import imp, FileSystemView fsv) {
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

            theImport.doFile();
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
     * Extended configuration panel for file import.  Built based on settings
     * requested by the specific language importer.
     */
    class ConfigPanelExtension extends JPanel {


        /**
         * Construct the configuration extension panel.
         * @param settings A list of settings requested by the language importer
         */
        public ConfigPanelExtension(final List<Setting> settings) {

            setLayout(new GridBagLayout());
            
            if (settings == null || settings.size() == 0) {
                JLabel label = new JLabel("No settings for this importer");
                add(label, createGridBagConstraints(true, false, false));
                add(new JPanel(), createGridBagConstraintsFinal()); 
                return;
            }

            for (Setting setting : settings) {
                if (setting instanceof UniqueSelection2) {
                    JLabel label = new JLabel(setting.getLabel());
                    add(label, createGridBagConstraints(true, false, false));

                    final UniqueSelection2 us = (UniqueSelection2) setting;
                    ButtonGroup group = new ButtonGroup();
                    int count = 0;
                    for (String option : us.getOptions()) {
                        JRadioButton button = new JRadioButton(option);
                        final int index = count++;
                        if (us.getDefaultSelection() == index) {
                            button.setSelected(true);
                        }
                        group.add(button);
                        button.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                us.setSelection(index);
                            }
                        });
                        add(button, createGridBagConstraints(false, false,
                                false));
                    }
                } else if (setting instanceof UserString2) {
                    JLabel label = new JLabel(setting.getLabel());
                    add(label, createGridBagConstraints(true, false, false));
                    final UserString2 us = (UserString2) setting;
                    final JTextField text = 
                        new JTextField(us.getDefaultString());
                    text.addFocusListener(new FocusListener() {
                        public void focusGained(FocusEvent e) { } 
                        public void focusLost(FocusEvent e) {
                            us.setUserString(text.getText());           
                        }
                        
                    });
                    add(text, createGridBagConstraints(true, false, false));
                } else if (setting instanceof BooleanSelection2) {
                    final BooleanSelection2 bs = (BooleanSelection2) setting;
                    final JCheckBox button = new JCheckBox(setting.getLabel());
                    button.setEnabled(bs.isSelected());
                    button.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            bs.setSelected(button.isSelected());
                        }
                    });
                    add(button, createGridBagConstraints(true, false, false));
                } else if (setting instanceof PathSelection) {
                    JLabel label = new JLabel(setting.getLabel());
                    add(label, createGridBagConstraints(true, false, false));
                    PathSelection ps = (PathSelection) setting;
                    // TODO: Need to add FileChooser 
                    JTextField text = new JTextField(ps.getDefaultPath());
                    add(text, createGridBagConstraints(true, false, false));
                    // TODO: Update setting
                } else if (setting instanceof PathListSelection) {
                    PathListSelection pls = (PathListSelection) setting;
                    add(new ImportClasspathDialog(pls),
                            createGridBagConstraints(true, false, false));
                } else {
                    throw new RuntimeException("Unknown setting type requested "
                            + setting);
                }
            }
            add(new JPanel(), createGridBagConstraintsFinal()); 
        }


        /**
         * Create a GridBagConstraints object to use with the layout.
         * 
         * @param topInset true to use a top inset 
         * @param bottomInset true to use a bottom inset
         * @param fill true to fill (horizontally)
         * @return the grid bag constraints
         */
        private GridBagConstraints createGridBagConstraints(boolean topInset,
                boolean bottomInset, boolean fill) {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = GridBagConstraints.RELATIVE;
            gbc.gridy = GridBagConstraints.RELATIVE;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.gridheight = 1;
            gbc.weightx = 1.0;
            gbc.weighty = 0.0;
            gbc.anchor = GridBagConstraints.NORTHWEST;
            gbc.fill = fill ? GridBagConstraints.HORIZONTAL
                    : GridBagConstraints.NONE;
            gbc.insets = 
                new Insets(
                        topInset ? 5 : 0, 
                                5, 
                                bottomInset ? 5 : 0, 
                                        5);
            gbc.ipadx = 0;
            gbc.ipady = 0;
            return gbc;
        }

        /**
         * A GridBagConstraints for the last item to take up the rest of the
         * space.
         * 
         * @return the GridBagConstraints object
         */
        private GridBagConstraints createGridBagConstraintsFinal() {
            GridBagConstraints gbc = createGridBagConstraints(false, true,
                    false);
            gbc.gridheight = GridBagConstraints.REMAINDER;
            gbc.weighty = 1.0;
            return gbc;
        }

    }



}
