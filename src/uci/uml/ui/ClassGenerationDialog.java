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




package uci.uml.ui;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import uci.util.*;
import uci.argo.kernel.*;
import uci.uml.visual.*;
import uci.uml.Foundation.Core.*;
import uci.uml.generate.*;

public class ClassGenerationDialog extends JFrame implements ActionListener {

  ////////////////////////////////////////////////////////////////
  // constants
  public static final String PRIORITIES[] = { "High", "Medium", "Low" };
  public static final int WIDTH = 300;
  public static final int HEIGHT = 350;

  ////////////////////////////////////////////////////////////////
  // instance variables
  TableModelClassChecks _tableModel = new TableModelClassChecks();
  protected JTable _table = new JTable(15, 2);
  protected JTextField _dir = new JTextField();
  protected JButton _generateButton = new JButton("Generate");
  protected JButton _cancelButton = new JButton("Cancel");

  ////////////////////////////////////////////////////////////////
  // constructors

  public ClassGenerationDialog(Vector nodes) {
    super("Generate Classes");
    JLabel promptLabel = new JLabel("Generate Classes:");
    JLabel dirLabel = new JLabel("Output Directory:");

    _tableModel.setTarget(nodes);
    _table.setModel(_tableModel);

    Font labelFont = MetalLookAndFeel.getSubTextFont();
    _table.setFont(labelFont);

    //_table.setRowSelectionAllowed(false);
    _table.setIntercellSpacing(new Dimension(0, 1));
    _table.setShowVerticalLines(false);
//     _table.getSelectionModel().addListSelectionListener(this);
    _table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

    TableColumn checkCol = _table.getColumnModel().getColumn(0);
    TableColumn descCol = _table.getColumnModel().getColumn(1);
    checkCol.setMinWidth(20);
    checkCol.setWidth(30);
    descCol.setMinWidth(200);
    descCol.setWidth(200);
    _table.setTableHeader(null);

// Vector nodes = _diagram.getGraphModel().getNodes();
// _table.setModel();
// _table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

    setSize(new Dimension(WIDTH, HEIGHT));
    getContentPane().setLayout(new BorderLayout());
    JPanel top = new JPanel();
    GridBagLayout gb = new GridBagLayout();
    top.setLayout(gb);
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 0.0;
    c.ipadx = 3; c.ipady = 3;


    c.gridx = 0;
    c.gridwidth = 1;
    c.gridy = 0;
    gb.setConstraints(promptLabel, c);
    top.add(promptLabel);

    c.gridy = 1;
    c.weighty = 1.0;
    JScrollPane classesSP = new JScrollPane(_table);
    JPanel hack = new JPanel();
    hack.setLayout(new BorderLayout());
    hack.add(classesSP, BorderLayout.CENTER);
    hack.setPreferredSize(new Dimension(250, 200));
    hack.setSize(new Dimension(250, 200));
    gb.setConstraints(hack, c);
    top.add(hack);

    c.weighty = 0.0;
    c.gridy = 2;
    gb.setConstraints(dirLabel, c);
    top.add(dirLabel);

    c.weightx = 1.0;
    c.gridx = 0;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridy = 3;
    gb.setConstraints(_dir, c);
    top.add(_dir);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    JPanel buttonInner = new JPanel(new GridLayout(1, 2));
    buttonInner.add(_generateButton);
    buttonInner.add(_cancelButton);
    buttonPanel.add(buttonInner);

    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Project p = pb.getProject();
    _dir.setText(p.getGenerationPrefs().getOutputDir());

    Rectangle pbBox = pb.getBounds();
    setLocation(pbBox.x + (pbBox.width - WIDTH)/2,
		pbBox.y + (pbBox.height - HEIGHT)/2);
    getContentPane().add(top, BorderLayout.NORTH);
    getContentPane().add(buttonPanel, BorderLayout.SOUTH);

    getRootPane().setDefaultButton(_generateButton);
    _generateButton.addActionListener(this);
    _cancelButton.addActionListener(this);
  }

  public Dimension getMaximumSize() { return new Dimension(WIDTH, HEIGHT); }


  ////////////////////////////////////////////////////////////////
  // event handlers
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == _generateButton) {
      String path = _dir.getText().trim();
      String newPath = "";
      // convert back slashes to forward slashes
      StringTokenizer st = new StringTokenizer(path, "\\", true);
      while (st.hasMoreTokens()) {
	String t = st.nextToken();
	if ("\\".equals(t)) newPath += "/";
	else newPath += t;
      }
      path = newPath;

      ProjectBrowser pb = ProjectBrowser.TheInstance;
      Project p = pb.getProject();
      p.getGenerationPrefs().setOutputDir(path);
      Vector nodes = _tableModel.getChecked();
      int size = nodes.size();
      for (int i = 0; i <size; i++) {
	Object node = nodes.elementAt(i);
	if (node instanceof Classifier)
	  GeneratorJava.GenerateFile((Classifier) node, path);
      }
      setVisible(false);
      dispose();
    }
    if (e.getSource() == _cancelButton) {
      //System.out.println("cancel");
      setVisible(false);
      dispose();
    }
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
      Classifier cls = (Classifier) _classes.elementAt(i);
      String name = cls.getName().getBody();
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
    Classifier cls = (Classifier) _classes.elementAt(row);
    return col == 0 && cls.getName().getBody().length() > 0;
  }

  public int getRowCount() {
    if (_classes == null) return 0;
    return _classes.size();
  }

  public Object getValueAt(int row, int col) {
    Classifier cls = (Classifier) _classes.elementAt(row);
    if (col == 0) {
      return (_checked.contains(cls)) ? Boolean.TRUE : Boolean.FALSE;
    }
    else if (col == 1) {
      String name = cls.getName().getBody();
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
