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




package uci.uml.ui.props;

//import jargo.kernel.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.text.*;
//import javax.swing.border.*;

import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Model_Management.*;
import uci.uml.ui.*;

/** This is an abstract superclass for panels that go in the
 *  "Properties" tab in the Argo/UML user interface.
 *
 * @see TabProps */

public class PropPanel extends TabSpawnable
implements TabModelTarget, DocumentListener {
  ////////////////////////////////////////////////////////////////
  // instance vars
  Object _target;
  boolean _inChange = false;
  JLabel _nameLabel = new JLabel("Name: ");
  JTextField _nameField = new JTextField();
  JLabel _stereoLabel = new JLabel("Stereotype: ");
  JComboBox _stereoField = new JComboBox();
  JLabel _namespaceLabel = new JLabel("Namespace: ");
  JComboBox _namespaceField = new JComboBox();

  ////////////////////////////////////////////////////////////////
  // constructors

  public PropPanel(String title) {
    super(title);
    _stereoField.setEditable(true);
    _stereoField.getEditor().getEditorComponent().setBackground(Color.white);
    _namespaceField.setEditable(true);
    _namespaceField.getEditor().getEditorComponent().setBackground(Color.white);

    GridBagLayout gb = new GridBagLayout();
    setLayout(gb);
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 0.0;
    c.weighty = 0.0;
    c.ipadx = 3; c.ipady = 3;

    c.gridx = 0;
    c.gridwidth = 1;
    c.gridy = 0;
    gb.setConstraints(_nameLabel, c);
    add(_nameLabel);
    c.gridy = 9;
    gb.setConstraints(_stereoLabel, c);
    add(_stereoLabel);
    c.gridy = 10;
    gb.setConstraints(_namespaceLabel, c);
    add(_namespaceLabel);


    _nameField.setMinimumSize(new Dimension(120, 20));
    c.weightx = 1.0;
    c.gridx = 1;
    //c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridy = 0;
    gb.setConstraints(_nameField, c);
    add(_nameField);
    c.gridy = 9;
    gb.setConstraints(_stereoField, c);
    add(_stereoField);
    c.gridy = 10;
    gb.setConstraints(_namespaceField, c);
    add(_namespaceField);

    _nameField.getDocument().addDocumentListener(this);
    _nameField.setFont(_stereoField.getFont());

    Component ed = _stereoField.getEditor().getEditorComponent();
    Document stereoDoc = ((JTextField)ed).getDocument();
    stereoDoc.addDocumentListener(this);

    ed = _namespaceField.getEditor().getEditorComponent();
    Document nsDoc = ((JTextField)ed).getDocument();
    nsDoc.addDocumentListener(this);
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public void setTarget(Object t) {
    try {
      _inChange = true;
      setTargetInternal(t);
    }
    finally {
      _inChange = false;
    }
  }

  protected void setTargetInternal(Object t) {
    _target = t;
    ModelElement me = (ModelElement) _target;
    String n = me.getName().getBody();
    // this is a small hack to get the text to update for an empty string
    if (n.equals("")) _nameField.setText(" ");
    _nameField.setText(n);
    _nameField.setCaretPosition(0);
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
    Namespace ns = me.getNamespace();
    ed = (JTextField) _namespaceField.getEditor().getEditorComponent();
    if (ns != null) {
      String namespaceName = ns.getName().getBody();
      while (ns.getNamespace() != null) {
	namespaceName = ns.getNamespace().getName().getBody() +
	  "." + namespaceName;
	ns = ns.getNamespace();
      }
      
      // needs-more-work: select from list
      _namespaceField.setSelectedItem(ns);
      ed.setText(namespaceName);
    }
    else {
      _namespaceField.setSelectedItem(null);
      ed.setText("");
    }
  }

  public Object getTarget() { return _target; }

  public void refresh() { setTarget(_target); }

  public boolean shouldBeEnabled() { return _target instanceof ModelElement; }


  protected void setTargetName() {
    if (_target == null) return;
    if (_inChange) return;
    try {
      String newName = _nameField.getText();
      ((ModelElement)_target).setName(new Name(newName));
    }
    catch (PropertyVetoException pve) { }
  }

  protected void setTargetStereotype() {
    if (_target == null) return;
    if (_inChange) return;
    try {
      ModelElement me = (ModelElement) _target;
      // needs-more-work: find predefined stereotype
      Component ed = _stereoField.getEditor().getEditorComponent();
      String stereoName = ((JTextField)ed).getText();
      Namespace m = ProjectBrowser.TheInstance.getProject().getCurrentNamespace();
      Stereotype s = null;
      ElementOwnership eo = m.findElementNamed(stereoName);
      if (eo != null && eo.getModelElement() instanceof Stereotype)
	s = (Stereotype) eo.getModelElement();
      else {
	s = new Stereotype(stereoName);
	m.addPublicOwnedElement(s);
      }
      Vector stereos = new Vector();
      stereos.addElement(s);
      //System.out.println("setting stereotype");
      me.setStereotype(stereos);
    }
    catch (PropertyVetoException pve) { }
  }

  ////////////////////////////////////////////////////////////////
  // DocumentListener implementation

  public void insertUpdate(DocumentEvent e) {
    //System.out.println(getClass().getName() + " insert");
    if (e.getDocument() == _nameField.getDocument()) setTargetName();
    Component ed = _stereoField.getEditor().getEditorComponent();
    Document stereoDoc = ((JTextField)ed).getDocument();
    if (e.getDocument() == stereoDoc) setTargetStereotype();
  }

  public void removeUpdate(DocumentEvent e) { insertUpdate(e); }

  public void changedUpdate(DocumentEvent e) {
    //System.out.println(getClass().getName() + " changed");
    // Apparently, this method is never called.
  }

} /* end class PropPanel */
