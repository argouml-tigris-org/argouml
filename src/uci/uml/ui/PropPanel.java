// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products may
// be obtained by contacting the University of California. David F. Redmiles
// Department of Information and Computer Science (ICS) University of
// California Irvine, California 92697-3425 Phone: 714-824-3823. This software
// program and documentation are copyrighted by The Regents of the University
// of California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.


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
