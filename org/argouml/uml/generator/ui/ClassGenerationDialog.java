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

import java.io.*;
import java.util.*;
import java.util.StringTokenizer;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import ru.novosoft.uml.foundation.core.*;

import org.tigris.gef.util.*;

import org.argouml.kernel.*;
import org.argouml.ui.*;
import org.argouml.util.osdep.*;
import org.argouml.language.java.generator.*;
import org.apache.log4j.Category;
import org.argouml.application.api.*;
import org.argouml.application.notation.*;
import org.argouml.uml.generator.*;

public class ClassGenerationDialog extends JDialog implements ActionListener {
    protected static Category cat = Category.getInstance(ClassGenerationDialog.class);

  ////////////////////////////////////////////////////////////////
  // constants
  //private static final String BUNDLE = "Cognitive";

  //static final String high = Argo.localize(BUNDLE, "level.high");

  ////////////////////////////////////////////////////////////////
  // instance variables
  private TableModelClassChecks _classTableModel = new TableModelClassChecks();
  private boolean isPathInModel = false;

  protected JCheckBox _compileCheckBox;
  protected JLabel _classesLabel;
  protected JTable _classTable;
  protected JButton _cancelButton;
  protected JButton _browseButton;
  protected JComboBox _outputDirectoryComboBox;
  protected JLabel _outputDirectoryLabel;
  protected JScrollPane _tableScrollPane;
  protected JButton _generateButton;

    ArrayList _languages = null;

  ////////////////////////////////////////////////////////////////
  // constructors


  public ClassGenerationDialog(Vector nodes) {
	  this(nodes,false);
  }

  public ClassGenerationDialog(Vector nodes, boolean isPathInModel) {
    super(ProjectBrowser.TheInstance, "Generate Classes");
    this.isPathInModel = isPathInModel;

    GridBagConstraints gridBagConstraints;

    _classesLabel = new JLabel();
    _outputDirectoryLabel = new JLabel();
    _browseButton = new JButton();
    _cancelButton = new JButton();
    _generateButton = new JButton();
    _outputDirectoryComboBox = new JComboBox(Converter.convert(getClasspathEntries()));
    _tableScrollPane = new JScrollPane();
    _classTable = new JTable();
    _compileCheckBox = new JCheckBox();

    getContentPane().setLayout(new GridBagLayout());

    _classesLabel.setText("Generate Classes ...");
    _classesLabel.setToolTipText("null");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.insets = new Insets(5, 5, 5, 5);
    gridBagConstraints.anchor = GridBagConstraints.WEST;
    getContentPane().add(_classesLabel, gridBagConstraints);

    _outputDirectoryLabel.setText("Output Directory:");
    _outputDirectoryLabel.setToolTipText("null");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.insets = new Insets(5, 5, 5, 5);
    gridBagConstraints.anchor = GridBagConstraints.WEST;
    if (!isPathInModel)
      getContentPane().add(_outputDirectoryLabel, gridBagConstraints);

    _browseButton.setText("Browse...");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.insets = new Insets(5, 5, 5, 5);
    gridBagConstraints.anchor = GridBagConstraints.EAST;
    if (!isPathInModel)
      getContentPane().add(_browseButton, gridBagConstraints);

    _cancelButton.setText("Cancel");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.insets = new Insets(5, 5, 5, 5);
    gridBagConstraints.anchor = GridBagConstraints.EAST;
    getContentPane().add(_cancelButton, gridBagConstraints);

    _generateButton.setLabel("Generate");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.insets = new Insets(5, 5, 5, 5);
    gridBagConstraints.anchor = GridBagConstraints.EAST;
    getContentPane().add(_generateButton, gridBagConstraints);

    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    if (!isPathInModel) {
      _outputDirectoryComboBox.setEditable(true);
      getContentPane().add(_outputDirectoryComboBox, gridBagConstraints);
    }

    ArrayList ll = Notation.getAvailableNotations();
    _languages = new ArrayList();
    for (int l = 0; l < ll.size(); l++) {
	if (NotationProviderFactory.getInstance()
	    .getProvider((NotationName)ll.get(l)) instanceof FileGenerator) {
	    _languages.add(ll.get(l));
	}
    }
    ll = null;

    _classTableModel.setTarget(nodes, _languages);
    _classTable.setModel(_classTableModel);
    _classTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    _classTable.setShowVerticalLines(false);
    if (_languages.size() <= 1)
	_classTable.setTableHeader(null);
    _classTable.setIntercellSpacing(new Dimension(0, 1));
    TableColumn descCol = _classTable.getColumnModel().getColumn(0);
    descCol.setMinWidth(100);
    descCol.setMaxWidth(200);
    descCol = null;
    _tableScrollPane.setViewportView(_classTable);

    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    gridBagConstraints.fill = GridBagConstraints.BOTH;
    gridBagConstraints.insets = new Insets(5, 5, 5, 5);
    gridBagConstraints.weighty = 2.0;
    getContentPane().add(_tableScrollPane, gridBagConstraints);

    _compileCheckBox.setText("compile generated source");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    gridBagConstraints.anchor = GridBagConstraints.WEST;
    getContentPane().add(_compileCheckBox, gridBagConstraints);

    pack();

    // Center Dialog on Screen -- todo: this should be a support function
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Rectangle pbBox = pb.getBounds();
    setLocation(pbBox.x + (pbBox.width - this.getWidth())/2,
    		pbBox.y + (pbBox.height - this.getHeight())/2);

    Project p = ProjectManager.getManager().getCurrentProject();
    _outputDirectoryComboBox.getModel().setSelectedItem(p.getGenerationPrefs().getOutputDir());

    getRootPane().setDefaultButton(_generateButton);
    _generateButton.addActionListener(this);
    _cancelButton.addActionListener(this);
    _browseButton.addActionListener(this);
  }

  public final static String pathSep = System.getProperty("path.separator");

  private static Vector getClasspathEntries() {
      String classpath=System.getProperty("java.class.path");
      Vector entries=new Vector();
      StringTokenizer allEntries=new StringTokenizer(classpath,pathSep);
      while (allEntries.hasMoreElements()) {
	  String entry=allEntries.nextToken();
	  if (!entry.toLowerCase().endsWith(".jar")
	      && !entry.toLowerCase().endsWith(".zip")) {
	      entries.addElement(entry);
	  }
      }
      // if (entries.size() == 0) {
// 	  JOptionPane.showMessageDialog(null, "In order to generate Java files, you need to have\nat least one directory in your CLASSPATH environment variable,\nwhere ArgoUML can store and compile the files.", "Code generation", JOptionPane.ERROR_MESSAGE);
// 	  return null;
//       }
      return entries;
  }


  ////////////////////////////////////////////////////////////////
  // event handlers
  /** Either the Generate or the Cancel buttons is pressed.
   */
  public void actionPerformed(ActionEvent e) {

    // Generate Button --------------------------------------
    if (e.getSource() == _generateButton) {
      String path = ((String)_outputDirectoryComboBox.getModel().getSelectedItem()).trim();
      Project p = ProjectManager.getManager().getCurrentProject();
      p.getGenerationPrefs().setOutputDir(path);
      Vector[] fileNames = new Vector[_languages.size()];
      for (int i = 0; i < _languages.size(); i++) {
        fileNames[i] = new Vector();
        NotationName language = (NotationName)_languages.get(i);
        FileGenerator generator = (FileGenerator)Generator.getGenerator(language);
        Set nodes = _classTableModel.getChecked(language);
        for (Iterator iter = nodes.iterator(); iter.hasNext(); ) {
          Object node = iter.next();
          if (node instanceof MClassifier) {
            if (isPathInModel) {
              path = Generator.getCodePath((MClassifier)node);
              if (path == null) {
                MNamespace parent = ((MClassifier)node).getNamespace();
                while (parent != null) {
                  path = Generator.getCodePath(parent);
                  if (path != null)
                    break;
                  parent = parent.getNamespace();
                }
              }
            }
            // TODO:
            // This will only work for languages that have each node
            // in a separate files (one or more).
            if (path != null) {
              String fn = generator.GenerateFile((MClassifier) node, path);
              fileNames[i].add(fn);
            }
          }
        }
      }
      setVisible(false);
      dispose();
    }

    // Cancel Button ------------------------------------------
    if (e.getSource() == _cancelButton) {
      cat.debug("cancel");
      setVisible(false);
      dispose();
    }

    // Browse Button ------------------------------------------
      if (e.getSource() == _browseButton) {
          try {
              // Show Filechooser to select OuputDirectory
              JFileChooser chooser = OsUtil.getFileChooser((String)_outputDirectoryComboBox.getModel().getSelectedItem());

              if (chooser == null) chooser = OsUtil.getFileChooser();

              chooser.setFileHidingEnabled(true);
              chooser.setMultiSelectionEnabled(false);
              chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
              chooser.setDialogTitle("Choose Output Directory");
              chooser.showDialog(this,"Choose");

              if ("" != chooser.getSelectedFile().getPath()) {
                  _outputDirectoryComboBox.getModel().setSelectedItem(chooser.getSelectedFile().getPath());
              } // else ignore
          }
          catch (Exception userPressedCancel) {
          }
      }
  }
} /* end class ClassGenerationDialog */




class TableModelClassChecks extends AbstractTableModel {
    protected static Category cat = Category.getInstance(TableModelClassChecks.class);
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
    for (int j = 0; j < getLanguagesCount(); j++)
	_checked[j] = new HashSet(); // Doesn't really matter what set we use.

    int size = _classes.size();
    for (int i = 0; i < size; i++) {
      MClassifier cls = (MClassifier) _classes.elementAt(i);
      String name = cls.getName();
      if (!(name.length() > 0))
	  continue;

      for (int j = 0; j < getLanguagesCount(); j++) {
	  // TODO:
	  // if (cls.isSupposedToBeGeneratedAsLanguage(_languages.index(j)))
	  //     _checked[j].add(cls);
	  // else
	  if (((NotationName)_languages.get(j))
	      .equals(Notation.getDefaultNotation())) {
	      _checked[j].add(cls);
	  }
      }
    }
    fireTableStructureChanged();
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
    public int getColumnCount() { return 1 + getLanguagesCount(); }

    public String  getColumnName(int c) {
	if (c == 0) return "Class Name";
	int langindex = c - 1;
	if (langindex >= 0 && langindex < getLanguagesCount())
	    return ((NotationName)_languages.get(langindex)).getConfigurationValue();
	return "XXX";
    }

    public Class getColumnClass(int c) {
	if (c == 0) return String.class;
	int langindex = c - 1;
	if (langindex >= 0 && langindex < getLanguagesCount())
	    return Boolean.class;
	return String.class;
    }

    public boolean isCellEditable(int row, int col) {
	MClassifier cls = (MClassifier) _classes.elementAt(row);
	if (col == 0)
	    return false;
	if (!(cls.getName().length() > 0))
	    return false;
	int langindex = col - 1;
	if (langindex >= 0 && langindex < getLanguagesCount())
	    return true;
	return false;
    }

    public int getRowCount() {
	if (_classes == null) return 0;
	return _classes.size();
    }

    public Object getValueAt(int row, int col) {
	MClassifier cls = (MClassifier) _classes.elementAt(row);
	int langindex = col - 1;
	if (col == 0) {
	    String name = cls.getName();
	    return (name.length() > 0) ? name : "(anon)";
	}
	else if (langindex >= 0 && langindex < getLanguagesCount()) {
	    return _checked[langindex].contains(cls)
		? Boolean.TRUE
		: Boolean.FALSE;
	}
	else
	    return "CC-r:" + row + " c:" + col;
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex)  {
	cat.debug("setting table value " + rowIndex + ", " + columnIndex);
	if (columnIndex == 0) return;
	if (columnIndex >= getColumnCount()) return;
	if (!(aValue instanceof Boolean)) return;
	boolean val = ((Boolean)aValue).booleanValue();
	Object cls = _classes.elementAt(rowIndex);

	int langindex = columnIndex - 1;
	if (langindex >= 0 && langindex < getLanguagesCount()) {
	    if (val) _checked[langindex].add(cls);
	    else _checked[langindex].remove(cls);
	}
    }
} /* end class TableModelClassChecks */
