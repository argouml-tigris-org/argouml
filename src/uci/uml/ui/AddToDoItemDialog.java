package uci.uml.ui;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;

import uci.util.*;
import uci.argo.kernel.*;

public class AddToDoItemDialog extends JFrame implements ActionListener {

  ////////////////////////////////////////////////////////////////
  // constants
  public static final String PRIORITIES[] = { "High", "Medium", "Low" };
  
  ////////////////////////////////////////////////////////////////
  // instance variables
  protected JTextField _headline = new JTextField();
  protected JComboBox  _priority = new JComboBox(PRIORITIES);
  protected JTextField _moreinfo = new JTextField();
  protected JTextArea  _description = new JTextArea();
  protected JButton _addButton = new JButton("Add");
  protected JButton _cancelButton = new JButton("Cancel");
  

  ////////////////////////////////////////////////////////////////
  // constructors
  
  public AddToDoItemDialog() {
    super("Add a ToDoItem");
    JLabel headlineLabel = new JLabel("Headline:");
    JLabel priorityLabel = new JLabel("Priority:");
    JLabel moreInfoLabel = new JLabel("MoreInfoURL:");

    _priority.setSelectedItem(PRIORITIES[0]);

    setSize(new Dimension(400, 350));
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
    gb.setConstraints(headlineLabel, c);
    top.add(headlineLabel);
    c.gridy = 1;
    gb.setConstraints(priorityLabel, c);
    top.add(priorityLabel);
    c.gridy = 2;
    gb.setConstraints(moreInfoLabel, c);
    top.add(moreInfoLabel);

    c.weightx = 1.0;
    c.gridx = 1;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridy = 0;
    gb.setConstraints(_headline, c);
    top.add(_headline);
    c.gridy = 1;
    gb.setConstraints(_priority, c);
    top.add(_priority);
    c.gridy = 2;
    gb.setConstraints(_moreinfo, c);
    top.add(_moreinfo);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    JPanel buttonInner = new JPanel(new GridLayout(1, 2));
    buttonInner.add(_addButton);
    buttonInner.add(_cancelButton);
    buttonPanel.add(buttonInner);
    
    getContentPane().add(top, BorderLayout.NORTH);
    getContentPane().add(new JScrollPane(_description), BorderLayout.CENTER);
    getContentPane().add(buttonPanel, BorderLayout.SOUTH);

    getRootPane().setDefaultButton(_addButton);
    _addButton.addActionListener(this);
    _cancelButton.addActionListener(this);
  }


  ////////////////////////////////////////////////////////////////
  // event handlers
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == _addButton) {
      Designer dsgr = Designer.TheDesigner;
      String head = _headline.getText();
      int pri = ToDoItem.HIGH_PRIORITY;
      switch (_priority.getSelectedIndex()) {
      case 0: pri = ToDoItem.HIGH_PRIORITY; break;
      case 1: pri = ToDoItem.MED_PRIORITY; break;
      case 2: pri = ToDoItem.LOW_PRIORITY; break;
      }
      String desc = _description.getText();
      String more = _moreinfo.getText();
      Set offs = new Set();  //? null
      ToDoItem item = new ToDoItem(dsgr, head, pri, desc, more, offs);
      dsgr.getToDoList().addElement(item); //? inform()
      System.out.println("add an item");
      hide();
      dispose();
    }
    if (e.getSource() == _cancelButton) {
      System.out.println("cancel");
      hide();
      dispose();
    }
  }

} /* end class AddToDoItemDialog */
