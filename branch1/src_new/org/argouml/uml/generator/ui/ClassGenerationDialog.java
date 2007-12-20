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

package org.argouml.uml.generator.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.notation.Notation;
import org.argouml.uml.generator.CodeGenerator;
import org.argouml.uml.generator.GeneratorManager;
import org.argouml.uml.generator.Language;
import org.argouml.util.ArgoDialog;
import org.tigris.swidgets.Dialog;

/**
 * The dialog that starts the generation of classes.
 */
public class ClassGenerationDialog
    extends ArgoDialog
    implements ActionListener {

    private static final String SOURCE_LANGUAGE_TAG = "src_lang";
    
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(ClassGenerationDialog.class);

    private TableModelClassChecks classTableModel;
    private boolean isPathInModel;
    private List<Language> languages;

    private JTable classTable;
    private JComboBox outputDirectoryComboBox;

    /**
     * Used to select the next language column in case
     * the "Select All" button is pressed.
     */
    private int languageHistory;


    /**
     * Constructor.
     *
     * @param nodes The nodes to generate.
     */
    public ClassGenerationDialog(List nodes) {
        this(nodes, false);
    }

    /**
     * Constructor.
     *
     * @param nodes The nodes to generate.
     * @param inModel <code>true</code> if the path is in the model.
     *        TODO: Correct?
     */
    public ClassGenerationDialog(List nodes, boolean inModel) {
        super(
            Translator.localize("dialog.title.generate-classes"),
            Dialog.OK_CANCEL_OPTION,
            true);
        isPathInModel = inModel;

        buildLanguages();

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));

        // Class Table

        classTableModel = new TableModelClassChecks();
        classTableModel.setTarget(nodes);
        classTable = new JTable(classTableModel);
        classTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        classTable.setShowVerticalLines(false);
        if (languages.size() <= 1) {
            classTable.setTableHeader(null);
        }
        setClassTableColumnWidths();
        classTable.setPreferredScrollableViewportSize(new Dimension(300, 300));

        // Select Buttons

        JButton selectAllButton = new JButton();
        nameButton(selectAllButton, "button.select-all");
        selectAllButton.addActionListener(new ActionListener() {
            /*
             * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
             */
            public void actionPerformed(ActionEvent e) {
                classTableModel.setAllChecks(true);
                classTable.repaint();
            }
        });
        JButton selectNoneButton = new JButton();
        nameButton(selectNoneButton, "button.select-none");
        selectNoneButton.addActionListener(new ActionListener() {
            /*
             * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
             */
            public void actionPerformed(ActionEvent e) {
                classTableModel.setAllChecks(false);
                classTable.repaint();
            }
        });

        JPanel selectPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        selectPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        JPanel selectButtons = new JPanel(new BorderLayout(5, 0));
        selectButtons.add(selectAllButton, BorderLayout.CENTER);
        selectButtons.add(selectNoneButton, BorderLayout.EAST);
        selectPanel.add(selectButtons);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 2));
        centerPanel.add(new JLabel(Translator.localize(
            "label.available-classes")), BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(classTable), BorderLayout.CENTER);
        centerPanel.add(selectPanel, BorderLayout.SOUTH);
        contentPanel.add(centerPanel, BorderLayout.CENTER);

        // Output Directory
        outputDirectoryComboBox = new JComboBox(getClasspathEntries().toArray());

        JButton browseButton = new JButton();
        nameButton(browseButton, "button.browse");
        browseButton.setText(browseButton.getText() + "...");
        browseButton.addActionListener(new ActionListener() {
            /*
             * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
             */
            public void actionPerformed(ActionEvent e) {
                doBrowse();
            }
        });

        JPanel southPanel = new JPanel(new BorderLayout(0, 2));

        if (!inModel) {
            outputDirectoryComboBox.setEditable(true);
            JPanel outputPanel = new JPanel(new BorderLayout(5, 0));
            outputPanel.setBorder(
                BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(
			Translator.localize("label.output-directory")),
                    BorderFactory.createEmptyBorder(2, 5, 5, 5)));
            outputPanel.add(outputDirectoryComboBox, BorderLayout.CENTER);
            outputPanel.add(browseButton, BorderLayout.EAST);
            southPanel.add(outputPanel, BorderLayout.NORTH);
        }

        // Compile Checkbox

        //_compileCheckBox = new JCheckBox();
        //nameButton(_compileCheckBox, "checkbox.compile-generated-source");
        // TODO: Implement the compile feature. For now, disable the checkbox.
        //_compileCheckBox.setEnabled(false);
        //southPanel.add(_compileCheckBox, BorderLayout.SOUTH);

        contentPanel.add(southPanel, BorderLayout.SOUTH);

        setContent(contentPanel);

        Project p = ProjectManager.getManager().getCurrentProject();
        outputDirectoryComboBox.getModel().setSelectedItem(
            p.getProjectSettings().getGenerationOutputDir());
    }

    /*
     * @see org.tigris.swidgets.Dialog#nameButtons()
     */
    @Override
    protected void nameButtons() {
        super.nameButtons();
        nameButton(getOkButton(), "button.generate");
    }

    private void setClassTableColumnWidths() {
        TableColumn column = null;
        Component c = null;
        int width = 0;

        for (int i = 0; i < classTable.getColumnCount() - 1; ++i) {
            column = classTable.getColumnModel().getColumn(i);
            width = 30;

            JTableHeader header = classTable.getTableHeader();
            if (header != null) {
                c =
		    header.getDefaultRenderer().getTableCellRendererComponent(
			    classTable,
			    column.getHeaderValue(),
			    false,
			    false,
			    0,
			    0);
                width = Math.max(c.getPreferredSize().width + 8, width);
            }

            column.setPreferredWidth(width);
            column.setWidth(width);
            column.setMinWidth(width);
            column.setMaxWidth(width);
        }
    }

    private void buildLanguages() {
        languages = new ArrayList<Language>(
                GeneratorManager.getInstance().getLanguages());
    }

    private static Collection getClasspathEntries() {
        String classpath = System.getProperty("java.class.path");
        Collection<String> entries = new TreeSet<String>();

        Project p = ProjectManager.getManager().getCurrentProject();
        entries.add(p.getProjectSettings().getGenerationOutputDir());

        final String pathSep = System.getProperty("path.separator");
        StringTokenizer allEntries = new StringTokenizer(classpath, pathSep);
        while (allEntries.hasMoreElements()) {
            String entry = allEntries.nextToken();
            if (!entry.toLowerCase().endsWith(".jar")
                && !entry.toLowerCase().endsWith(".zip")) {
                entries.add(entry);
            }
        }
        return entries;
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);

        // Generate Button --------------------------------------
        if (e.getSource() == getOkButton()) {
            String path =
                ((String) outputDirectoryComboBox.getModel()
                        .getSelectedItem()).trim();
            Project p = ProjectManager.getManager().getCurrentProject();
            p.getProjectSettings().setGenerationOutputDir(path);
            List<String>[] fileNames = new List[languages.size()];
            for (int i = 0; i < languages.size(); i++) {
                fileNames[i] = new ArrayList<String>();
                Language language = languages.get(i);
                GeneratorManager genMan = GeneratorManager.getInstance();
                CodeGenerator generator = genMan.getGenerator(language);
                Set nodes = classTableModel.getChecked(language);

                if (!isPathInModel) {
                    Collection files =
                        generator.generateFiles(nodes, path, false);
                    for (Iterator fit = files.iterator(); fit.hasNext();) {
                        fileNames[i].add(path + CodeGenerator.FILE_SEPARATOR
                                + fit.next());
                    }
                } else {
                    // classify nodes by base path
                    Map nodesPerPath = new HashMap();
                    for (Iterator iter = nodes.iterator(); iter.hasNext();) {
                        Object node = iter.next();
                        if (!Model.getFacade().isAClassifier(node)) {
                            continue;
                        }
                        path = GeneratorManager.getCodePath(node);
                        if (path == null) {
                            Object parent =
                                Model.getFacade().getNamespace(node);
                            while (parent != null) {
                                path = GeneratorManager.getCodePath(parent);
                                if (path != null) {
                                    break;
                                }
                                parent =
                                    Model.getFacade().getNamespace(parent);
                            }
                        }
                        if (path != null) {
                            final String fileSep = CodeGenerator.FILE_SEPARATOR;
                            if (path.endsWith(fileSep)) { // remove trailing /
                                path =
                                    path.substring(0, path.length()
                                        - fileSep.length());
                            }
                            Set np = (Set) nodesPerPath.get(path);
                            if (np == null) {
                                np = new HashSet();
                                nodesPerPath.put(path, np);
                            }
                            np.add(node);
                            saveLanguage(node, language);
                        }
                    } // end for (all nodes)

                    // generate the files
                    Iterator nit = nodesPerPath.entrySet().iterator();
                    while (nit.hasNext()) {
                        Map.Entry entry = (Map.Entry) nit.next();
                        String basepath = (String) entry.getKey();
                        Set nodeColl = (Set) entry.getValue();
                        // TODO: the last argument (recursive flag) should be a
                        // selectable option
                        Collection files =
                            generator.generateFiles(nodeColl, basepath, false);
                        for (Iterator fit = files.iterator(); fit.hasNext();) {
                            fileNames[i].add(basepath
                                    + CodeGenerator.FILE_SEPARATOR
                                    + fit.next());
                        }
                    }
                } // end if (!isPathInModel) .. else
            } // end for (all languages)
            // TODO: do something with the generated list fileNames,
            // for example, show it to the user in a dialog box.
        }
    }

    /**
     * Save the source language in the model.
     * 
     * TODO: Support multiple languages now that we have UML 1.4
     * tagged values.
     * @param node
     * @param language
     */
    private void saveLanguage(Object node, Language language) {
        Object taggedValue =
                Model.getFacade().getTaggedValue(node, SOURCE_LANGUAGE_TAG);
        if (taggedValue != null) {
            String savedLang = Model.getFacade().getValueOfTag(taggedValue);
            if (!language.getName().equals(savedLang)) {
                Model.getExtensionMechanismsHelper().setValueOfTag(
                        taggedValue, language.getName());
            }
        } else {
            taggedValue =
                    Model.getExtensionMechanismsFactory().buildTaggedValue(
                            SOURCE_LANGUAGE_TAG, language.getName());
            Model.getExtensionMechanismsHelper().addTaggedValue(
                    node, taggedValue);

        }
    }

    private void doBrowse() {
        try {
            // Show Filechooser to select OutputDirectory
            JFileChooser chooser =
                new JFileChooser(
                    (String) outputDirectoryComboBox
                        .getModel()
                        .getSelectedItem());

            if (chooser == null) {
                chooser = new JFileChooser();
            }

            chooser.setFileHidingEnabled(true);
            chooser.setMultiSelectionEnabled(false);
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setDialogTitle(Translator.localize(
                    "dialog.generation.chooser.choose-output-dir"));
            chooser.showDialog(this, Translator.localize(
                    "dialog.generation.chooser.approve-button-text"));

            if (!"".equals(chooser.getSelectedFile().getPath())) {
                String path = chooser.getSelectedFile().getPath();
                outputDirectoryComboBox.addItem(path);
                outputDirectoryComboBox.getModel().setSelectedItem(path);
            } // else ignore
        } catch (Exception userPressedCancel) {
            // TODO: How does the pressed cancel become a java.lang.Exception?
            LOG.info("user pressed cancel");
        }
    }

    class TableModelClassChecks extends AbstractTableModel {

        private List classes;
        private Set[] checked;

        /**
         * Constructor.
         */
        public TableModelClassChecks() {
        }

        ////////////////
        // accessors
        public void setTarget(List nodes) {
            classes = nodes;
            checked = new Set[getLanguagesCount()];
            for (int j = 0; j < getLanguagesCount(); j++) {
                // Doesn't really matter what set we use.
                checked[j] = new HashSet();
            }

            int size = classes.size();
            for (int i = 0; i < size; i++) {
                Object cls = classes.get(i);
                String name = Model.getFacade().getName(cls);
                // Jaap B. in older versions of argouml (before
                // 0.14alpha1) names were not initialized correctly.  this
                // is a patch for that.
                if (name == null || name.length() == 0) {
                    // TODO: This is modifying the user's model - bad! - tfm
                    Model.getCoreHelper().setName(cls, "");
                    // continue;
                }

                for (int j = 0; j < getLanguagesCount(); j++) {
                    if (isSupposedToBeGeneratedAsLanguage(
                            languages.get(j), cls)) {
                        checked[j].add(cls);
                    } else if ((languages.get(j)).getName().equals(
                            Notation.getConfiguredNotation()
                                    .getConfigurationValue())) {
                        checked[j].add(cls);
                    }
                }
            }
            fireTableStructureChanged();

            getOkButton().setEnabled(classes.size() > 0
				     && getChecked().size() > 0);
        }

        private boolean isSupposedToBeGeneratedAsLanguage(
            Language lang,
            Object cls) {
            if (lang == null || cls == null) {
                return false;
            }

            Object taggedValue =
                Model.getFacade().getTaggedValue(cls, SOURCE_LANGUAGE_TAG);
            if (taggedValue == null) {
                return false;
            }
            String savedLang = Model.getFacade().getValueOfTag(taggedValue);
            return (lang.getName().equals(savedLang));
        }

        private int getLanguagesCount() {
            if (languages == null) {
                return 0;
            }
            return languages.size();
        }

        public Set getChecked(Language lang) {
            int index = languages.indexOf(lang);
            if (index == -1) {
                return Collections.EMPTY_SET;
            }
            return checked[index];
        }

        /**
         * All checked classes.
         *
         * @return The union of all languages as a {@link Set}.
         */
        public Set getChecked() {
            Set union = new HashSet();
            for (int i = 0; i < getLanguagesCount(); i++) {
                union.addAll(checked[i]);
            }
            return union;
        }

        ////////////////
        // TableModel implementation
        
        /*
         * @see javax.swing.table.TableModel#getColumnCount()
         */
        public int getColumnCount() {
            return 1 + getLanguagesCount();
        }

        /*
         * @see javax.swing.table.TableModel#getColumnName(int)
         */
        @Override
        public String getColumnName(int c) {
            if (c >= 0 && c < getLanguagesCount()) {
                return languages.get(c).getName();
            } else if (c == getLanguagesCount()) {
                return "Class Name";
            }
            return "XXX";
        }

        /*
         * @see javax.swing.table.TableModel#getColumnClass(int)
         */
        public Class getColumnClass(int c) {
            if (c >= 0 && c < getLanguagesCount()) {
                return Boolean.class;
            } else if (c == getLanguagesCount()) {
                return String.class;
            }
            return String.class;
        }

        /*
         * @see javax.swing.table.TableModel#isCellEditable(int, int)
         */
        @Override
        public boolean isCellEditable(int row, int col) {
            Object cls = classes.get(row);
            if (col == getLanguagesCount()) {
                return false;
            }
            if (!(Model.getFacade().getName(cls).length() > 0)) {
                return false;
            }
            if (col >= 0 && col < getLanguagesCount()) {
                return true;
            }
            return false;
        }

        /*
         * @see javax.swing.table.TableModel#getRowCount()
         */
        public int getRowCount() {
            if (classes == null) {
                return 0;
            }
            return classes.size();
        }

        /*
         * @see javax.swing.table.TableModel#getValueAt(int, int)
         */
        public Object getValueAt(int row, int col) {
            Object cls = classes.get(row);
            if (col == getLanguagesCount()) {
                String name = Model.getFacade().getName(cls);
                if (name.length() > 0) {
                    return name;
                }
                return "(anon)";
            } else if (col >= 0 && col < getLanguagesCount()) {
                if (checked[col].contains(cls)) {
                    return Boolean.TRUE;
                }
                return Boolean.FALSE;
            } else {
                return "CC-r:" + row + " c:" + col;
            }
        }

        /*
         * @see javax.swing.table.TableModel#setValueAt(
         *          java.lang.Object, int, int)
         */
        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == getLanguagesCount()) {
                return;
	    }
            if (columnIndex >= getColumnCount()) {
                return;
	    }
            if (!(aValue instanceof Boolean)) {
                return;
	    }
            boolean val = ((Boolean) aValue).booleanValue();
            Object cls = classes.get(rowIndex);

            if (columnIndex >= 0 && columnIndex < getLanguagesCount()) {
                if (val) {
                    checked[columnIndex].add(cls);
                } else {
                    checked[columnIndex].remove(cls);
                }
            }

            if (val && !getOkButton().isEnabled()) {
                getOkButton().setEnabled(true);
            } else if (!val && getOkButton().isEnabled()
                && getChecked().size() == 0) {
                getOkButton().setEnabled(false);
            }
        }

        /**
         * Sets or clears all checkmarks for the (next) language for
         * all classes.
         *
         * @param value If false then all checkmarks are cleared for all
         * languages.
         * If true then all are cleared, except for one language column,
         * these are all set.
         */
        public void setAllChecks(boolean value) {
            int rows = getRowCount();
            int checks = getLanguagesCount();

            if (rows == 0) {
                return;
            }

            for (int i = 0; i < rows; ++i) {
                Object cls = classes.get(i);

                for (int j = 0; j < checks; ++j) {
                    if (value && (j == languageHistory)) {
                        checked[j].add(cls);
                    } else {
                        checked[j].remove(cls);
                    }
                }
            }
            if (value) {
		if (++languageHistory >= checks) {
		    languageHistory = 0;
		}
	    }
            getOkButton().setEnabled(value);
        }

        /**
         * The UID.
         */
        private static final long serialVersionUID = 6108214254680694765L;
    } /* end class TableModelClassChecks */

    /**
     * The UID.
     */
    private static final long serialVersionUID = -8897965616334156746L;
} /* end class ClassGenerationDialog */

