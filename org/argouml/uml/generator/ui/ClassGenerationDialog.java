// $Id$
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

package org.argouml.uml.generator.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import org.argouml.application.api.Notation;
import org.argouml.application.api.NotationName;
import org.argouml.application.notation.NotationProviderFactory;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.ui.ArgoDialog;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.generator.FileGenerator;
import org.argouml.uml.generator.Generator;
import org.argouml.util.osdep.OsUtil;
import org.tigris.gef.util.Converter;

public class ClassGenerationDialog extends ArgoDialog implements ActionListener {
    private static final Logger cat =
        Logger.getLogger(ClassGenerationDialog.class);

    ////////////////////////////////////////////////////////////////
    // instance variables

    private TableModelClassChecks _classTableModel = null;
    private boolean isPathInModel = false;
    private ArrayList _languages = null;

    private JCheckBox _compileCheckBox;
    private JTable _classTable;
    private JComboBox _outputDirectoryComboBox;

    /** Used to select the next language column in case 
     * the "Select All" button is pressed. 
     */
    private int languageHistory = 0;

    ////////////////////////////////////////////////////////////////
    // constructors

    public ClassGenerationDialog(Vector nodes) {
        this(nodes, false);
    }

    public ClassGenerationDialog(Vector nodes, boolean isPathInModel) {
        super(
            ProjectBrowser.getInstance(),
            Translator.localize(BUNDLE, "dialog.title.generate-classes"),
            ArgoDialog.OK_CANCEL_OPTION,
            true);
        this.isPathInModel = isPathInModel;

        buildLanguages();

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));

        // Class Table
        
        _classTableModel = new TableModelClassChecks();
        _classTableModel.setTarget(nodes, _languages);
        _classTable = new JTable(_classTableModel);
        _classTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        _classTable.setShowVerticalLines(false);
        if (_languages.size() <= 1) {
            _classTable.setTableHeader(null);
        }
        setClassTableColumnWidths();
        _classTable.setPreferredScrollableViewportSize(new Dimension(300, 300));

        // Select Buttons
        
        JButton selectAllButton = new JButton();
        nameButton(selectAllButton, "button.select-all");
        selectAllButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _classTableModel.setAllChecks(true);
                _classTable.repaint();
            }
        });
        JButton selectNoneButton = new JButton();
        nameButton(selectNoneButton, "button.select-none");
        selectNoneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _classTableModel.setAllChecks(false);
                _classTable.repaint();
            }
        });

        JPanel selectPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        selectPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        JPanel selectButtons = new JPanel(new BorderLayout(5, 0));
        selectButtons.add(selectAllButton, BorderLayout.CENTER);
        selectButtons.add(selectNoneButton, BorderLayout.EAST);
        selectPanel.add(selectButtons);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 2));
        centerPanel.add(new JLabel(Translator.localize(BUNDLE, 
            "label.available-classes")), BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(_classTable), BorderLayout.CENTER);
        centerPanel.add(selectPanel, BorderLayout.SOUTH);
        contentPanel.add(centerPanel, BorderLayout.CENTER);

        // Output Directory
        
        _outputDirectoryComboBox =
            new JComboBox(Converter.convert(new Vector(getClasspathEntries())));

        JButton browseButton = new JButton();
        nameButton(browseButton, "button.browse");
        browseButton.setText(browseButton.getText() + "...");
        browseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doBrowse();
            }
        });

        JPanel southPanel = new JPanel(new BorderLayout(0, 2));

        if (!isPathInModel) {
            _outputDirectoryComboBox.setEditable(true);
            JPanel outputPanel = new JPanel(new BorderLayout(5, 0));
            outputPanel.setBorder(
                BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(
                    Translator.localize(BUNDLE, "label.output-directory")),
                    BorderFactory.createEmptyBorder(2, 5, 5, 5)));
            outputPanel.add(_outputDirectoryComboBox, BorderLayout.CENTER);
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
        _outputDirectoryComboBox.getModel().setSelectedItem(
            p.getGenerationPrefs().getOutputDir());
    }

    protected void nameButtons() {
        super.nameButtons();
        nameButton(getOkButton(), "button.generate");
    }
    
    private void setClassTableColumnWidths() {
        TableColumn column = null;
        Component c = null;
        int width = 0;

        for (int i = 0; i < _classTable.getColumnCount() - 1; ++i) {
            column = _classTable.getColumnModel().getColumn(i);
            width = 30;

            JTableHeader header = _classTable.getTableHeader();
            if (header != null) {
                c = header.getDefaultRenderer().getTableCellRendererComponent(
                    _classTable,
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
        ArrayList ll = Notation.getAvailableNotations();
        _languages = new ArrayList();
        for (int l = 0; l < ll.size(); l++) {
            if (NotationProviderFactory
                .getInstance()
                .getProvider((NotationName) ll.get(l))
                instanceof FileGenerator) {
                _languages.add(ll.get(l));
            }
        }
    }

    private static Collection getClasspathEntries() {
        String classpath = System.getProperty("java.class.path");
        Collection entries = new TreeSet();

        Project p = ProjectManager.getManager().getCurrentProject();
        entries.add(p.getGenerationPrefs().getOutputDir());

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

    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);

        // Generate Button --------------------------------------
        if (e.getSource() == getOkButton()) {
            String path =
                ((String) _outputDirectoryComboBox
                    .getModel()
                    .getSelectedItem())
                    .trim();
            Project p = ProjectManager.getManager().getCurrentProject();
            p.getGenerationPrefs().setOutputDir(path);
            Vector[] fileNames = new Vector[_languages.size()];
            for (int i = 0; i < _languages.size(); i++) {
                fileNames[i] = new Vector();
                NotationName language = (NotationName) _languages.get(i);
                FileGenerator generator =
                    (FileGenerator) Generator.getGenerator(language);
                Set nodes = _classTableModel.getChecked(language);
                for (Iterator iter = nodes.iterator(); iter.hasNext();) {
                    Object node = iter.next();
                    if (ModelFacade.isAClassifier(node)) {
                        if (isPathInModel) {
                            path = Generator.getCodePath(node);
                            if (path == null) {
                                Object parent = ModelFacade.getNamespace(node);
                                while (parent != null) {
                                    path = Generator.getCodePath(parent);
                                    if (path != null)
                                        break;
                                    parent = ModelFacade.getNamespace(parent);
                                }
                            }
                        }
                        // TODO:
                        // This will only work for languages that have each node
                        // in a separate files (one or more).
                        if (path != null) {
                            String fn = generator.GenerateFile(node, path);
                            fileNames[i].add(fn);
                            // save the selected language in the model
                            // TODO 1: no support of multiple checked
                            // languages 
                            //
                            // TODO 2: it's a change in the model ->
                            // save needed!
                            Object taggedValue = ModelFacade.getTaggedValue(
                                node, "src_lang");
                            String savedLang = null;
                            if (taggedValue != null) {
                                savedLang = ModelFacade.getValueOfTag(
                                    taggedValue);
                            }
                            if (taggedValue == null || !language
                                .getConfigurationValue()
                                .equals(savedLang)) {
                                ModelFacade.setTaggedValue(
                                    node,
                                    "src_lang",
                                    language.getConfigurationValue());
                            }
                        }
                    }
                }
            }
        }
    }

    private void doBrowse() {
        try {
            // Show Filechooser to select OuputDirectory
            JFileChooser chooser =
                OsUtil.getFileChooser(
                    (String) _outputDirectoryComboBox
                        .getModel()
                        .getSelectedItem());

            if (chooser == null)
                chooser = OsUtil.getFileChooser();

            chooser.setFileHidingEnabled(true);
            chooser.setMultiSelectionEnabled(false);
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setDialogTitle("Choose Output Directory");
            chooser.showDialog(this, "Choose");

            if ("" != chooser.getSelectedFile().getPath()) {
                String path = chooser.getSelectedFile().getPath();
                _outputDirectoryComboBox.addItem(path);
                _outputDirectoryComboBox.getModel().setSelectedItem(path);
            } // else ignore
        }
        catch (Exception userPressedCancel) {
            cat.info("user pressed cancel");
        }
    }
    
    class TableModelClassChecks extends AbstractTableModel {
        ////////////////
        // instance varables
        Vector _classes;
        ArrayList _languages;
        Set[] _checked;

        ////////////////
        // constructor
        public TableModelClassChecks() {
        }

        ////////////////
        // accessors
        public void setTarget(Vector classes, ArrayList languages) {
            _classes = classes;
            _languages = languages;
            _checked = new Set[getLanguagesCount()];
            for (int j = 0; j < getLanguagesCount(); j++) {
                // Doesn't really matter what set we use.
                _checked[j] = new HashSet();
            }

            int size = _classes.size();
            for (int i = 0; i < size; i++) {
                Object cls = _classes.elementAt(i);
                String name = ModelFacade.getName(cls);
                // Jaap B. in older versions of argouml (before
                // 0.14alpha1) names were not initialized correctly.  this
                // is a patch for that.
                if (name == null || name.length() == 0) {
                    ModelFacade.setName(cls, "");
                    // continue;
                }

                for (int j = 0; j < getLanguagesCount(); j++) {
                    if (isSupposedToBeGeneratedAsLanguage((NotationName) _languages
                        .get(j),
                        cls))
                        _checked[j].add(cls);
                    else if (
                        ((NotationName) _languages.get(j)).equals(
                            Notation.getDefaultNotation())) {
                        _checked[j].add(cls);
                    }
                }
            }
            fireTableStructureChanged();
            
            getOkButton().setEnabled(_classes.size() > 0 && getChecked().size() > 0);
        }

        private boolean isSupposedToBeGeneratedAsLanguage(
            NotationName lang,
            Object cls) {
            if (lang == null) {
                return false;                
            }
            if (cls == null) {
                return false;
            } 
            
            Object taggedValue = ModelFacade.getTaggedValue(cls, "src_lang");
            if (taggedValue == null) {
                return false;
            } 
            String savedLang = ModelFacade.getValueOfTag(taggedValue);
            return (lang.getConfigurationValue().equals(savedLang));
        }

        private int getLanguagesCount() {
            if (_languages == null)
                return 0;
            return _languages.size();
        }

        public Set getChecked(NotationName nn) {
            int index = _languages.indexOf(nn);
            if (index == -1)
                return new HashSet();
            return _checked[index];
        }

        /** All checked classes. Union of all languages.
         */
        public Set getChecked() {
            Set union = new HashSet();
            for (int i = 0; i < getLanguagesCount(); i++)
                union.addAll(_checked[i]);
            return union;
        }

        ////////////////
        // TableModel implemetation
        public int getColumnCount() {
            return 1 + getLanguagesCount();
        }

        public String getColumnName(int c) {
            if (c >= 0 && c < getLanguagesCount()) {
                return ((NotationName) _languages.get(c)).getConfigurationValue();
            }
            else if (c == getLanguagesCount()) {
                return "Class Name";
            }
            return "XXX";
        }

        public Class getColumnClass(int c) {
            if (c >= 0 && c < getLanguagesCount()) {
                return Boolean.class;
            }
            else if (c == getLanguagesCount()) {
                return String.class;
            }
            return String.class;
        }

        public boolean isCellEditable(int row, int col) {
            Object cls = _classes.elementAt(row);
            if (col == getLanguagesCount())
                return false;
            if (!(ModelFacade.getName(cls).length() > 0))
                return false;
            if (col >= 0 && col < getLanguagesCount())
                return true;
            return false;
        }

        public int getRowCount() {
            if (_classes == null)
                return 0;
            return _classes.size();
        }

        public Object getValueAt(int row, int col) {
            Object cls = _classes.elementAt(row);
            if (col == getLanguagesCount()) {
                String name = ModelFacade.getName(cls);
                return (name.length() > 0) ? name : "(anon)";
            }
            else if (col >= 0 && col < getLanguagesCount()) {
                return _checked[col].contains(cls) ? Boolean.TRUE : Boolean.FALSE;
            }
            else
                return "CC-r:" + row + " c:" + col;
        }

        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            //  cat.debug("setting table value " + rowIndex + ", " + columnIndex);
            if (columnIndex == getLanguagesCount())
                return;
            if (columnIndex >= getColumnCount())
                return;
            if (!(aValue instanceof Boolean))
                return;
            boolean val = ((Boolean) aValue).booleanValue();
            Object cls = _classes.elementAt(rowIndex);

            if (columnIndex >= 0 && columnIndex < getLanguagesCount()) {
                if (val)
                    _checked[columnIndex].add(cls);
                else
                    _checked[columnIndex].remove(cls);
            }
            
            if (val && !getOkButton().isEnabled()) {
                getOkButton().setEnabled(true);
            }
            else if (!val && getOkButton().isEnabled()
                && getChecked().size() == 0) {
                getOkButton().setEnabled(false);
            }
        }

        /** Sets or clears all checkmarks for the (next) language for all classes
         *  @param value If false then all checkmarks are cleared for all 
         *  languages. 
         *  If true then all are cleared, except for one language column, 
         *  these are all set. 
         */
        public void setAllChecks(boolean value) {
            int rows = getRowCount();
            int checks = getLanguagesCount();
            
            if (rows == 0) {
                return;
            }

            for (int i = 0; i < rows; ++i) {
                Object cls = _classes.elementAt(i);

                for (int j = 0; j < checks; ++j) {
                    if (value && (j == languageHistory)) {
                        _checked[j].add(cls);
                    }
                    else {
                        _checked[j].remove(cls);
                    }
                }
            }
            if (value) if (++languageHistory >= checks) languageHistory = 0;
            getOkButton().setEnabled(value);
        }
    } /* end class TableModelClassChecks */    
} /* end class ClassGenerationDialog */

