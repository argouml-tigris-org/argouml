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

package org.argouml.cognitive.critics.ui;

import java.beans.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.text.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.argouml.kernel.*;
import org.argouml.ui.ProjectBrowser;
import org.argouml.cognitive.*;
import org.argouml.cognitive.critics.*;

import org.argouml.application.api.*;

/** Dialog box to list all critics and allow editing of some of their
 *  properties.  Needs-More-Work: knowledge type, supported goals,
 *  supported decisions, critic network. */
public class CriticBrowserDialog extends JDialog
implements ActionListener, ListSelectionListener, ItemListener, DocumentListener {
  public static int _numCriticBrowser = 0;

  ////////////////////////////////////////////////////////////////
  // constants
  private static final String BUNDLE = "Cognitive";

  static final String high = Argo.localize(BUNDLE, "level.high");
  static final String medium = Argo.localize(BUNDLE, "level.medium");
  static final String low = Argo.localize(BUNDLE, "level.low");

  public static final String PRIORITIES[] = { high, medium, low };
  public static final String USE_CLAR[] = { "Always", "If Only One", "Never" };


  ////////////////////////////////////////////////////////////////
  // instance variables

  protected JLabel _criticsLabel   = new JLabel("Critics");
  protected JLabel _clsNameLabel   = new JLabel("Critic Class: ");
  protected JLabel _headlineLabel  = new JLabel("Headline: ");
  protected JLabel _priorityLabel  = new JLabel("Priority: ");
  protected JLabel _moreInfoLabel  = new JLabel("MoreInfo: ");
  protected JLabel _descLabel      = new JLabel("Description: ");
  protected JLabel _clarifierLabel = new JLabel("Use Clarifier: ");

  TableModelCritics _tableModel  = new TableModelCritics();
  protected JTable _table        = new JTable(30, 3);
  protected JLabel _className    = new JLabel("");
  protected JTextField _headline = new JTextField("", 40);
  protected JComboBox _priority  = new JComboBox(PRIORITIES);
  protected JTextField _moreInfo = new JTextField("", 35);
  protected JTextArea _desc      = new JTextArea("", 6, 40);
  protected JComboBox _useClar   = new JComboBox(USE_CLAR);

  protected JButton _okButton      = new JButton("OK");
  protected JButton _wakeButton    = new JButton("Wake");
  protected JButton _configButton  = new JButton("Configure");
  protected JButton _networkButton = new JButton("Edit Network");
  protected JButton _goButton      = new JButton("Go");

  protected Critic _target;

  ////////////////////////////////////////////////////////////////
  // constructors

  public CriticBrowserDialog() {
    super(ProjectBrowser.TheInstance, "Critics");

    Container mainContent = getContentPane();
//     GridBagLayout gb = new GridBagLayout();
//     GridBagConstraints c = new GridBagConstraints();
//     c.fill = GridBagConstraints.BOTH;
//     c.weightx = 0.0;
//     c.ipadx = 3; c.ipady = 3;


    JPanel content = new JPanel();
    mainContent.add(content, BorderLayout.CENTER);
    //content.setLayout(gb);
    content.setLayout(null);

    _tableModel.setTarget(Agency.getCritics());
    _table.setModel(_tableModel);
    _table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    Font labelFont = MetalLookAndFeel.getSubTextFont();
    _table.setFont(labelFont);

//     _table.setMinimumSize(new Dimension(150, 80));
//     _table.setPreferredSize(new Dimension(200, 150));
//     _table.setSize(new Dimension(200, 150));

    //_table.setRowSelectionAllowed(false);
    _table.setIntercellSpacing(new Dimension(0, 1));
    _table.setShowVerticalLines(false);
    _table.getSelectionModel().addListSelectionListener(this);
    _table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

    TableColumn checkCol = _table.getColumnModel().getColumn(0);
    TableColumn descCol = _table.getColumnModel().getColumn(1);
    TableColumn actCol = _table.getColumnModel().getColumn(2);
    checkCol.setMinWidth(30);
    checkCol.setMaxWidth(30);
    checkCol.setWidth(30);
    descCol.setMinWidth(200);
    descCol.setWidth(200);
    actCol.setMinWidth(80);
    actCol.setMaxWidth(80);
    actCol.setWidth(80);

    _criticsLabel.setBounds(2, 2, 300, 25);
    content.add(_criticsLabel);
//     c.gridy = 1;
//     c.gridheight = 11; //GridBagConstraints.REMAINDER
    JScrollPane tableSP = new JScrollPane(_table);
    JPanel p = new JPanel();
    p.setLayout(new BorderLayout());
    p.setPreferredSize(new Dimension(310, 150));
    p.setSize(new Dimension(310, 150));
    p.setMaximumSize(new Dimension(310, 150));
    p.add(tableSP, BorderLayout.CENTER);
//     tableSP.setPreferredSize(new Dimension(310, 100));
//     tableSP.setSize(new Dimension(310, 100));
//     tableSP.setMaximumSize(new Dimension(310, 100));
//     gb.setConstraints(p, c);
    p.setBounds(2, 2 + 2 + 25, 340, 300);
    content.add(p);

//     c.gridx = 1;
//     c.gridy = 0;
//     c.gridwidth = 1;
//     c.gridheight = 1;
//     SpacerPanel spacer = new SpacerPanel();
//     gb.setConstraints(spacer, c);
//     content.add(spacer);

//     c.weightx = 0.0;
//     c.gridx = 2;
//     c.gridy = 1;
//     gb.setConstraints(_clsNameLabel, c);
    _clsNameLabel.setBounds(360, 2, 100, 25);
    content.add(_clsNameLabel);

//     c.gridy = 2;
//     gb.setConstraints(_headlineLabel, c);
    _headlineLabel.setBounds(360, 2+25+2, 100, 25);
    content.add(_headlineLabel);

//     c.gridy = 3;
//     gb.setConstraints(_priorityLabel, c);
    _priorityLabel.setBounds(360, 2*3+25*2, 100, 25);
    content.add(_priorityLabel);

//     c.gridy = 4;
//     gb.setConstraints(_moreInfoLabel, c);
    _moreInfoLabel.setBounds(360, 2*4 + 25*3, 100, 25);
    content.add(_moreInfoLabel);

//     c.gridy = 5;
//     gb.setConstraints(_descLabel, c);
    _descLabel.setBounds(360, 2*5 + 25*4, 100, 25);
    content.add(_descLabel);

//     c.gridy = 8;
//     gb.setConstraints(_clarifierLabel, c);
    _clarifierLabel.setBounds(360, 2 + (2+25)*5 + 85, 100, 25);
    content.add(_clarifierLabel);


//     c.weightx = 1.0;
//     c.gridx = 3;
//     c.gridy = 1;
//     c.gridwidth = 2;
//     gb.setConstraints(_className, c);
    _className.setBounds(465, 2, 320, 25);
    content.add(_className);

//     c.gridy = 2;
//     gb.setConstraints(_headline, c);
    _headline.setBounds(465, 2 + (2+25)*1, 320, 25);
    content.add(_headline);

//     c.gridy = 3;
//     gb.setConstraints(_priority, c);
    _priority.setBounds(465, 2 + (2+25)*2, 320, 25);
    content.add(_priority);

//     c.gridy = 4;
//     c.gridwidth = 1;
//     gb.setConstraints(_moreInfo, c);
    _moreInfo.setBounds(465, 2 + (2+25)*3, 320-60, 25);
    content.add(_moreInfo);

//     c.weightx = 0.0;
//     c.gridx = 4;
//     c.gridy = 4;
//     c.gridwidth = 1;
//     gb.setConstraints(_goButton, c);
    _goButton.setBounds(465+320-60, 2 + (2+25)*3, 60, 25);
    content.add(_goButton);

//     c.weightx = 1.0;
//     c.gridx = 3;
//     c.gridy = 5;
//     c.gridwidth = 2;
    JScrollPane descSP = new JScrollPane(_desc);
//     gb.setConstraints(descSP, c);
    descSP.setBounds(465, 2 + (2+25)*4, 320, 25+85);
    content.add(descSP);

//     c.gridy = 8;
//     gb.setConstraints(_useClar, c);
    _useClar.setBounds(465, 2 + (2+25)*5 + 85, 320, 25);
    content.add(_useClar);

//     c.gridy = 9;
    JPanel buttonPanel = new JPanel();
    //buttonPanel.setLayout(new GridLayout(1, 3));
    buttonPanel.add(_wakeButton);
    buttonPanel.add(_configButton);
    buttonPanel.add(_networkButton);
//     gb.setConstraints(buttonPanel, c);
    buttonPanel.setBounds(465, 2 + (2+25)*6+85, 320, 25+5+5);
    content.add(buttonPanel);

//     c.gridx = 2;
//     c.gridy = 10;
//     c.gridwidth = GridBagConstraints.REMAINDER;
    JPanel buttonPane = new JPanel();
    buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
    buttonPane.add(_okButton);
//     gb.setConstraints(buttonPane, c);
//     buttonPane.setBounds(465, 2 + (2+25)*7 + 5+85, 320, 25+5);
    buttonPane.setBounds(465, 360 - (25 + 5) - 35, 320, 25+5+5);
    content.add(buttonPane);

    _goButton.addActionListener(this);
    _okButton.addActionListener(this);
    _networkButton.addActionListener(this);
    _wakeButton.addActionListener(this);
    _configButton.addActionListener(this);
    _headline.getDocument().addDocumentListener(this);
    _moreInfo.getDocument().addDocumentListener(this);
    _desc.getDocument().addDocumentListener(this);
    _priority.addItemListener(this);
    _useClar.addItemListener(this);

    _wakeButton.setEnabled(false);
    _networkButton.setEnabled(false);
    _configButton.setEnabled(false);

    _desc.setLineWrap(true);
    _desc.setWrapStyleWord(true);

    setLocation(100, 150);
    setSize(465+320+10, 360);
    setResizable(false);
    _numCriticBrowser++;
  }

  public void setTarget(Object t) {
    _target = (Critic) t;
    _goButton.setEnabled(false);
    _networkButton.setEnabled(false);
    _wakeButton.setEnabled(_target != null &&
			   _target.snoozeOrder().getSnoozed());
    _configButton.setEnabled(false);
    _className.setText(_target.getClass().getName());
    _headline.setText(_target.getHeadline());

    int p = _target.getPriority();
    if (p == ToDoItem.HIGH_PRIORITY)
	_priority.setSelectedItem(high);
    else if (p == ToDoItem.MED_PRIORITY)
	_priority.setSelectedItem(medium);
    else
	_priority.setSelectedItem(low);
    _priority.repaint();

    _moreInfo.setText(_target.getMoreInfoURL());
    _desc.setText(_target.getDescriptionTemplate());
    _desc.setCaretPosition(0);
    _useClar.setSelectedItem("Always");
    _useClar.repaint();
  }

  public void setTargetHeadline() {
    if (_target == null) return;
    String h = _headline.getText();
    _target.setHeadline(h);
  }

  public void setTargetPriority() {
    if (_target == null) return;
    String p = (String) _priority.getSelectedItem();
    if (p == null) return;
    if (p.equals(PRIORITIES[0])) _target.setPriority(ToDoItem.HIGH_PRIORITY);
    if (p.equals(PRIORITIES[1])) _target.setPriority(ToDoItem.MED_PRIORITY);
    if (p.equals(PRIORITIES[2])) _target.setPriority(ToDoItem.LOW_PRIORITY);
  }

  public void setTargetMoreInfo() {
    if (_target == null) return;
    String mi = _moreInfo.getText();
    _target.setMoreInfoURL(mi);
  }

  public void setTargetDesc() {
    if (_target == null) return;
    String d = _desc.getText();
    _target.setDescription(d);
  }

  public void setTargetUseClarifiers() {
    System.out.println("setting clarifier usage rule");
  }

  ////////////////////////////////////////////////////////////////
  // event handlers


  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == _okButton) {
      setVisible(false);
      dispose();
      return;
    }
    if (e.getSource() == _goButton) {
      System.out.println("needs-more-work go!");
      return;
    }
    if (e.getSource() == _networkButton) {
      System.out.println("needs-more-work network!");
      return;
    }
    if (e.getSource() == _configButton) {
      System.out.println("needs-more-work config!");
      return;
    }
    if (e.getSource() == _wakeButton) {
      _target.unsnooze();
      return;
    }
    System.out.println("unknown src in CriticBrowserDialog: " + e.getSource());
  }

  public void valueChanged(ListSelectionEvent lse) {
    if (lse.getValueIsAdjusting()) return;
    Object src = lse.getSource();
    if (src != _table.getSelectionModel()) {
      System.out.println("src = " + src);
      return;
    }
    //System.out.println("got valueChanged from " + src);
    int row = _table.getSelectedRow();
    Vector critics = Agency.getCritics();
    setTarget(critics.elementAt(row));
  }

  public void insertUpdate(DocumentEvent e) {
    //System.out.println(getClass().getName() + " insert");
    Document hDoc = _headline.getDocument();
    Document miDoc = _moreInfo.getDocument();
    Document dDoc = _desc.getDocument();
    if (e.getDocument() == hDoc) setTargetHeadline();
    if (e.getDocument() == miDoc) setTargetMoreInfo();
    if (e.getDocument() == dDoc) setTargetDesc();
  }

  public void removeUpdate(DocumentEvent e) { insertUpdate(e); }

  public void changedUpdate(DocumentEvent e) {
    System.out.println(getClass().getName() + " changed");
    // Apparently, this method is never called.
  }

  public void itemStateChanged(ItemEvent e) {
    Object src = e.getSource();
    if (src == _priority) {
      //System.out.println("class keywords now is " +
      //_keywordsField.getSelectedItem());
      setTargetPriority();
    }
    else if (src == _useClar) {
      //System.out.println("class MVisibilityKind now is " +
      //_visField.getSelectedItem());
      setTargetUseClarifiers();
    }
    else System.out.println("unknown itemStateChanged src: "+ src);
  }

} /* end class CriticBrowserDialog */




class TableModelCritics extends AbstractTableModel
implements VetoableChangeListener, DelayedVChangeListener {
  ////////////////
  // instance varables
  Vector _target;

  ////////////////
  // constructor
  public TableModelCritics() { }

  ////////////////
  // accessors
  public void setTarget(Vector critics) {
    _target = critics;
    //fireTableStructureChanged();
  }

  ////////////////
  // TableModel implemetation
  public int getColumnCount() { return 3; }

  public String  getColumnName(int c) {
    if (c == 0) return "X";
    if (c == 1) return "Headline";
    if (c == 2) return "Active";
    return "XXX";
  }

  public Class getColumnClass(int c) {
    if (c == 0) return Boolean.class;
    if (c == 1) return String.class;
    if (c == 2) return String.class;
    return String.class;
  }

  public boolean isCellEditable(int row, int col) {
    return col == 0;
  }

  public int getRowCount() {
    if (_target == null) return 0;
    return _target.size();
  }

  public Object getValueAt(int row, int col) {
    Critic cr = (Critic) _target.elementAt(row);
    if (col == 0) return cr.isEnabled() ? Boolean.TRUE : Boolean.FALSE;
    if (col == 1) return cr.getHeadline();
    if (col == 2) return cr.isActive() ? "Active" : "Inactive";
    return "CR-" + row*2+col; // for debugging
  }

  public void setValueAt(Object aValue, int rowIndex, int columnIndex)  {
    //System.out.println("setting table value " + rowIndex + ", " + columnIndex);
    if (columnIndex != 0) return;
    if (!(aValue instanceof Boolean)) return;
    Boolean enable = (Boolean) aValue;
    Critic cr = (Critic) _target.elementAt(rowIndex);
    cr.setEnabled(enable.booleanValue());
    fireTableRowsUpdated(rowIndex, rowIndex); //needs-more-work
  }

  ////////////////
  // event handlers

  public void vetoableChange(PropertyChangeEvent pce) {
    DelayedChangeNotify delayedNotify = new DelayedChangeNotify(this, pce);
    SwingUtilities.invokeLater(delayedNotify);
  }

  public void delayedVetoableChange(PropertyChangeEvent pce) {
    fireTableStructureChanged();
  }


} /* end class TableModelCritics */
