package uci.uml.ui;

//import jargo.kernel.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.tree.*;
import com.sun.java.swing.text.*;
//import com.sun.java.swing.border.*;

import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Model_Management.*;

public class PropPanel extends TabSpawnable
implements TabModelTarget, DocumentListener {
  ////////////////////////////////////////////////////////////////
  // instance vars
  Object _target;
  JLabel _nameLabel = new JLabel("Name: ");
  JTextField _nameField = new JTextField();
  JLabel _stereoLabel = new JLabel("Stereotype: ");
  JComboBox _stereoField = new JComboBox();

  ////////////////////////////////////////////////////////////////
  // constructors

  public PropPanel(String title) {
    super(title);
    _stereoField.setEditable(true);
    _stereoField.getEditor().getEditorComponent().setBackground(Color.white);

    GridBagLayout gb = new GridBagLayout();
    setLayout(gb);
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 0.0;
    c.ipadx = 3; c.ipady = 3;

    c.gridx = 0;
    c.gridwidth = 1;
    c.gridy = 0;
    gb.setConstraints(_nameLabel, c);
    add(_nameLabel);
    c.gridy = 4;
    gb.setConstraints(_stereoLabel, c);
    add(_stereoLabel);

    c.weightx = 1.0;
    c.gridx = 1;
    //c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridy = 0;
    gb.setConstraints(_nameField, c);
    add(_nameField);
    c.gridy = 4;
    gb.setConstraints(_stereoField, c);
    add(_stereoField);

    _nameField.getDocument().addDocumentListener(this);
    _nameField.setFont(_stereoField.getFont());

    Component ed = _stereoField.getEditor().getEditorComponent();
    Document stereoDoc = ((JTextField)ed).getDocument();
    stereoDoc.addDocumentListener(this);
    
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public void setTarget(Object t) {
    _target = t;
    ModelElement me = (ModelElement) _target;
    _nameField.setText(me.getName().getBody());
    Vector stereos = me.getStereotype();
    JTextField ed = (JTextField) _stereoField.getEditor().getEditorComponent();
    if (stereos != null && stereos.size() == 1) {
      Stereotype s = (Stereotype) stereos.firstElement();
      String stereoName = s.getName().getBody();
      // needs-more-work: select from list
      _stereoField.setSelectedItem(s);
      ed.setText(stereoName);
    }
    else {
      _stereoField.setSelectedItem(null);
      ed.setText("");
    }
  }

  public Object getTarget() { return _target; }

  public boolean shouldBeEnabled() { return _target instanceof ModelElement; }


  protected void setTargetName() {
    if (_target == null) return;
    try {
      ((ModelElement)_target).setName(new Name(_nameField.getText()));
    }
    catch (PropertyVetoException pve) { } 
  }

  protected void setTargetStereotype() {
    if (_target == null) return;
    try {
    ModelElement me = (ModelElement) _target;
    // needs-more-work: find predefined stereotype
    Component ed = _stereoField.getEditor().getEditorComponent();
    String stereoName = ((JTextField)ed).getText();
    Stereotype s = new Stereotype(stereoName);
    Vector stereos = new Vector();
    stereos.addElement(s);
    System.out.println("setting stereotype");
    me.setStereotype(stereos);
    }
    catch (PropertyVetoException pve) { } 
  }

  ////////////////////////////////////////////////////////////////
  // event handling

  public void insertUpdate(DocumentEvent e) {
    System.out.println(getClass().getName() + " insert");
    if (e.getDocument() == _nameField.getDocument()) setTargetName();
    Component ed = _stereoField.getEditor().getEditorComponent();
    Document stereoDoc = ((JTextField)ed).getDocument();
    if (e.getDocument() == stereoDoc) setTargetStereotype();
  }

  public void removeUpdate(DocumentEvent e) { insertUpdate(e); }

  public void changedUpdate(DocumentEvent e) {
    System.out.println(getClass().getName() + " changed");
    // Apparently, this method is never called.
  }
  
} /* end class PropPanel */
