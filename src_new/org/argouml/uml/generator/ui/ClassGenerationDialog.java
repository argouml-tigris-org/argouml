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

public class ClassGenerationDialog extends JFrame implements ActionListener {

  ////////////////////////////////////////////////////////////////
  // constants
  //private static final String BUNDLE = "Cognitive";

  //static final String high = Localizer.localize(BUNDLE, "level.high");
  
  ////////////////////////////////////////////////////////////////
  // instance variables
  private TableModelClassChecks _classTableModel = new TableModelClassChecks();
  
  protected JCheckBox _compileCheckBox;
  protected JLabel _classesLabel;
  protected JTable _classTable;
  protected JButton _cancelButton;
  protected JButton _browseButton;
  protected JComboBox _outputDirectoryComboBox;
  protected JLabel _outputDirectoryLabel;
  protected JScrollPane _tableScrollPane;
  protected JButton _generateButton;

  ////////////////////////////////////////////////////////////////
  // constructors

  public ClassGenerationDialog(Vector nodes) {
    super("Generate Classes");
    
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
    getContentPane().add(_outputDirectoryLabel, gridBagConstraints);

    _browseButton.setText("Browse...");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.insets = new Insets(5, 5, 5, 5);
    gridBagConstraints.anchor = GridBagConstraints.EAST;
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
    _outputDirectoryComboBox.setEditable(true);
    getContentPane().add(_outputDirectoryComboBox, gridBagConstraints);

    _classTableModel.setTarget(nodes);
    _classTable.setModel(_classTableModel);
    _classTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    _classTable.setShowVerticalLines(false);
    _classTable.setTableHeader(null);
    _classTable.setIntercellSpacing(new Dimension(0, 1));
    TableColumn checkCol = _classTable.getColumnModel().getColumn(0);
    checkCol.setMinWidth(20);
    checkCol.setMaxWidth(30);
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

    Project p = pb.getProject();
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
  public void actionPerformed(ActionEvent e) {
      
      // Generate Button --------------------------------------
      if (e.getSource() == _generateButton) {
      String path = ((String)_outputDirectoryComboBox.getModel().getSelectedItem()).trim();

      ProjectBrowser pb = ProjectBrowser.TheInstance;
      Project p = pb.getProject();
      p.getGenerationPrefs().setOutputDir(path);
      Vector nodes = _classTableModel.getChecked();
      int size = nodes.size();
      String[] compileCmd=new String[size+1];

      for (int i = 0; i <size; i++) {
	Object node = nodes.elementAt(i);
	if (node instanceof MClassifier)
	  compileCmd[i+1] = GeneratorJava.GenerateFile((MClassifier) node, path);
      }
      if (_compileCheckBox.isSelected()) {
	  String compiler=System.getProperty("argo.compiler");
	  if (compiler==null || compiler.length()==0)
	      compiler="javac";
	  compileCmd[0]=compiler;
	  //compileCmd[0] += " -d "+path+" -classpath "+System.getProperty("java.class.path");
	  
	  String compilerOutput=compile(compileCmd);
	  if (compilerOutput==null) {
	      System.out.println("Compilation done.");
	      JOptionPane.showMessageDialog(this, "Compilation done.","Code Generation", JOptionPane.INFORMATION_MESSAGE);
	  } else {
	      // todo: should display errors in a window!
	      System.out.println("Compiler errors -> System.err");
	      System.err.println(compilerOutput);
	      JOptionPane.showMessageDialog(this, "Compiler errors -> System.err\n"+compilerOutput, "Code Generation", JOptionPane.ERROR_MESSAGE);
	      
	  }
      }

      setVisible(false);
      dispose();
    }
    
    // Cancel Button ------------------------------------------
    if (e.getSource() == _cancelButton) {
      //System.out.println("cancel");
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

  private String compile(String[] compileCmd) {
      for (int i=0; i<compileCmd.length; ++i)
	  System.out.print(compileCmd[i]+" ");
      System.out.println();
      StringBuffer allOut=new StringBuffer();
      int exitState=-1;
      boolean goon=true;
      try {
	  Process compileProc=Runtime.getRuntime().exec(compileCmd);
	  BufferedReader coutRB=new BufferedReader(new InputStreamReader(compileProc.getInputStream()));;
	  BufferedReader cerrRB=new BufferedReader(new InputStreamReader(compileProc.getErrorStream()));
	  int co,ce;
  	  do {
	      co=coutRB.read();
  	      if (co != -1) {
  		  allOut.append((char)co);
	      }
	      ce=cerrRB.read();
  	      if (ce != -1) {
  		  allOut.append((char)ce);
	      }
	      if (co==-1 && ce==-1){
		  try {
		      exitState=compileProc.exitValue();
		      goon=false;
		  } catch (IllegalThreadStateException e1) {
		      // wait until next polling:
		      try {
			  Thread.yield();
			  Thread.sleep(500);
		      } catch (InterruptedException irr) { }
		  }
	      }
  	  } while (goon || co!=-1 || ce!=-1);
      } catch (IOException e2) {
	  System.out.println("Exception while reading compiler output:");
	  e2.printStackTrace();
      }
      String outStr=null;
      if (exitState!=0) {
	  // Compiler reported errors, messages are suppressed:
	  outStr=allOut.toString();
      }
      return outStr;
  }

} /* end class ClassGenerationDialog */




class TableModelClassChecks extends AbstractTableModel {
  ////////////////
  // instance varables
  Vector _classes;
  VectorSet _checked = new VectorSet();

  ////////////////
  // constructor
  public TableModelClassChecks() {
  }

  ////////////////
  // accessors
  public void setTarget(Vector classes) {
    _classes = classes;
    _checked.removeAllElements();
    int size = _classes.size();
    for (int i = 0; i < size; i++) {
      MClassifier cls = (MClassifier) _classes.elementAt(i);
      String name = cls.getName();
      if (name.length() > 0) _checked.addElement(cls);
    }
    fireTableStructureChanged();
  }

  public Vector getChecked() { return _checked.asVector(); }

  ////////////////
  // TableModel implemetation
  public int getColumnCount() { return 2; }

  public String  getColumnName(int c) {
    if (c == 0) return "X";
    if (c == 1) return "Class Name";
    return "XXX";
  }

  public Class getColumnClass(int c) {
    if (c == 0) return Boolean.class;
    if (c == 1) return String.class;
    return String.class;
  }

  public boolean isCellEditable(int row, int col) {
    MClassifier cls = (MClassifier) _classes.elementAt(row);
    return col == 0 && cls.getName().length() > 0;
  }

  public int getRowCount() {
    if (_classes == null) return 0;
    return _classes.size();
  }

  public Object getValueAt(int row, int col) {
    MClassifier cls = (MClassifier) _classes.elementAt(row);
    if (col == 0) {
      return (_checked.contains(cls)) ? Boolean.TRUE : Boolean.FALSE;
    }
    else if (col == 1) {
      String name = cls.getName();
      return (name.length() > 0) ? name : "(anon)";
    }
    else
      return "CC-" + row*2+col;
  }

  public void setValueAt(Object aValue, int rowIndex, int columnIndex)  {
    //System.out.println("setting table value " + rowIndex + ", " + columnIndex);
    if (columnIndex != 0) return;
    if (!(aValue instanceof Boolean)) return;
    boolean val = ((Boolean)aValue).booleanValue();
    Object cls = _classes.elementAt(rowIndex);
    if (val) _checked.addElement(cls);
    else _checked.removeElement(cls);
  }

} /* end class TableModelClassChecks */
