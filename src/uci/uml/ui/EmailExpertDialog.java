package uci.uml.ui;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;

import uci.util.*;
import uci.argo.kernel.*;

public class EmailExpertDialog extends JFrame implements ActionListener {

  ////////////////////////////////////////////////////////////////
  // constants
  
  ////////////////////////////////////////////////////////////////
  // instance variables
  protected JTextField _to = new JTextField();
  protected JTextField _cc = new JTextField();
  protected JTextField _subject = new JTextField();
  protected JTextArea  _body = new JTextArea();
  protected JButton _sendButton = new JButton("Send");
  protected JButton _cancelButton = new JButton("Cancel");
  protected ToDoItem _target;

  ////////////////////////////////////////////////////////////////
  // constructors
  
  public EmailExpertDialog() {
    super("Send Email to an Expert");
    JLabel toLabel = new JLabel("To:");
    JLabel ccLabel = new JLabel("Cc:");
    JLabel subjectLabel = new JLabel("Subject:");

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
    gb.setConstraints(toLabel, c);
    top.add(toLabel);
    c.gridy = 1;
    gb.setConstraints(ccLabel, c);
    top.add(ccLabel);
    c.gridy = 2;
    gb.setConstraints(subjectLabel, c);
    top.add(subjectLabel);

    c.weightx = 1.0;
    c.gridx = 1;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridy = 0;
    gb.setConstraints(_to, c);
    top.add(_to);
    c.gridy = 1;
    gb.setConstraints(_cc, c);
    top.add(_cc);
    c.gridy = 2;
    gb.setConstraints(_subject, c);
    top.add(_subject);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    JPanel buttonInner = new JPanel(new GridLayout(1, 2));
    buttonInner.add(_sendButton);
    buttonInner.add(_cancelButton);
    buttonPanel.add(buttonInner);
    
    getContentPane().add(top, BorderLayout.NORTH);
    getContentPane().add(new JScrollPane(_body), BorderLayout.CENTER);
    getContentPane().add(buttonPanel, BorderLayout.SOUTH);

    getRootPane().setDefaultButton(_sendButton);
    _sendButton.addActionListener(this);
    _cancelButton.addActionListener(this);
  }

  public void setTarget(Object t) { _target = (ToDoItem) t; }

  ////////////////////////////////////////////////////////////////
  // event handlers
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == _sendButton) {
      Designer dsgr = Designer.TheDesigner;
      String to = _to.getText();
      String cc = _cc.getText();
      String subject = _subject.getText();
      System.out.println("sending email!");
      hide();
      dispose();
    }
    if (e.getSource() == _cancelButton) {
      System.out.println("cancel");
      hide();
      dispose();
    }
  }

} /* end class EmailExpertDialog */
