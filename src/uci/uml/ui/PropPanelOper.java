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
import com.sun.java.swing.border.*;

import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Model_Management.*;

public class PropPanelOper extends PropPanel
implements ItemListener {

  ////////////////////////////////////////////////////////////////
  // constants
  public static final VisibilityKind VISIBILITIES[] = {
    VisibilityKind.PUBLIC, VisibilityKind.PRIVATE,
    VisibilityKind.PROTECTED, VisibilityKind.PACKAGE };
  public static final String ATTRKEYWORDS[] = {
    "None", "static", "final", "static final", "synchronized",
    "static synchronized", "final synchronized", "static final synchronized"
  };

  
  ////////////////////////////////////////////////////////////////
  // instance vars
  JLabel _visLabel = new JLabel("Visibility: ");
  JComboBox _visField = new JComboBox(VISIBILITIES);
  JLabel _keywordsLabel = new JLabel("Keywords: ");
  JComboBox _keywordsField = new JComboBox(ATTRKEYWORDS);
  JLabel _typeLabel = new JLabel("Return Type: ");
  JComboBox _typeField = new JComboBox();
  JLabel _argsLabel = new JLabel("Arguments: ");
  JTable _argsTable = new JTable(4, 2);
  SpacerPanel _spacer = new SpacerPanel();

  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelOper() {
    super("Operation Properties");
    GridBagLayout gb = (GridBagLayout) getLayout();
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 0.0;
    c.ipadx = 0; c.ipady = 0;


    //_visField.getEditor().getEditorComponent().setBackground(Color.white);
    //_keywordsField.getEditor().getEditorComponent().setBackground(Color.white);
    _typeField.setEditable(true);
    _typeField.getEditor().getEditorComponent().setBackground(Color.white);    

    c.gridx = 0;
    c.gridwidth = 1;
    c.gridy = 1;
    gb.setConstraints(_visLabel, c);
    add(_visLabel);
    c.gridy = 2;
    gb.setConstraints(_keywordsLabel, c);
    add(_keywordsLabel);
    c.gridy = 3;
    gb.setConstraints(_typeLabel, c);
    add(_typeLabel);

    
    c.weightx = 1.0;
    c.gridx = 1;
    //c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridy = 1;
    gb.setConstraints(_visField, c);
    add(_visField);
    c.gridy = 2;
    gb.setConstraints(_keywordsField, c);
    add(_keywordsField);
    c.gridy = 3;
    gb.setConstraints(_typeField, c);
    add(_typeField);

    c.weightx = 0.0;
    c.gridx = 2;
    c.gridy = 0;
    gb.setConstraints(_spacer, c);
    add(_spacer);
    

    c.weightx = 1.0;
    c.gridwidth = 3;
    c.gridx = 3;
    //c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridy = 0;
    gb.setConstraints(_argsLabel, c);
    add(_argsLabel);
    c.gridy = 1;
    c.gridheight = GridBagConstraints.REMAINDER;
    gb.setConstraints(_argsTable, c);
    add(_argsTable);

    _visField.addItemListener(this);
    _keywordsField.addItemListener(this);
    _typeField.addItemListener(this);

  }


  
  ////////////////////////////////////////////////////////////////
  // accessors

  public void setTarget(Object t) {
    super.setTarget(t);
    Operation oper = (Operation) _target;

    VisibilityKind vk = oper.getVisibility();
    _visField.setSelectedItem(vk);

    ScopeKind sk = oper.getOwnerScope();
    // needs-more-work: concurrency
    // needs-more-work: final methods?
    if (ScopeKind.CLASSIFIER.equals(sk))
      _keywordsField.setSelectedItem("static");
    else
      _keywordsField.setSelectedItem("None");

    Vector params = oper.getParameter();
    if (params != null && params.size() > 0) {
      Parameter p = null;
      java.util.Enumeration enum = params.elements();
      while (enum.hasMoreElements()) {
	p = (Parameter) enum.nextElement();
	if (p.getName().equals(Parameter.RETURN_NAME)) break;
      }
      if (p != null) _typeField.setSelectedItem(p.getType());
      else _typeField.setSelectedItem(null);
    }
  }



  public void setTargetVisibility() {
    if (_target == null) return;
    VisibilityKind vk = (VisibilityKind) _visField.getSelectedItem();
    Operation oper = (Operation) _target;
    try {
        oper.setVisibility(vk);
    }
    catch (PropertyVetoException ignore) { }
  }

  // needs-more-work: how to model abstract methods?
  // needs-more-work: how to model final methods?
  
  public void setTargetKeywords() {
    if (_target == null) return;
    String keys = (String) _keywordsField.getSelectedItem();
    if (keys == null) {
      System.out.println("keywords are null");
      return;
    }
    Operation oper = (Operation) _target;
    try {
      if (keys.equals("None")) {
	oper.setOwnerScope(ScopeKind.INSTANCE);
	oper.setConcurrency(CallConcurrencyKind.CONCURRENT);
      }
      else if (keys.equals("static")) {
	oper.setOwnerScope(ScopeKind.CLASSIFIER);
	oper.setConcurrency(CallConcurrencyKind.CONCURRENT);
      }
      else if (keys.equals("final")) {
	oper.setOwnerScope(ScopeKind.INSTANCE);
	oper.setConcurrency(CallConcurrencyKind.CONCURRENT);
      }
      else if (keys.equals("static final")) {
	oper.setOwnerScope(ScopeKind.INSTANCE);
	oper.setConcurrency(CallConcurrencyKind.CONCURRENT);
      }
      else if (keys.equals("synchronized")) {
	oper.setOwnerScope(ScopeKind.INSTANCE);
	oper.setConcurrency(CallConcurrencyKind.GUARDED);
      }
      else if (keys.equals("static synchronized")) {
	oper.setOwnerScope(ScopeKind.CLASSIFIER);
	oper.setConcurrency(CallConcurrencyKind.GUARDED);
      }
      else if (keys.equals("final synchronized")) {
	oper.setOwnerScope(ScopeKind.INSTANCE);
	oper.setConcurrency(CallConcurrencyKind.GUARDED);
      }
      else if (keys.equals("static final synchronized")) {
	oper.setOwnerScope(ScopeKind.CLASSIFIER);
	oper.setConcurrency(CallConcurrencyKind.GUARDED);
      }
    }
    catch (PropertyVetoException pve) {
      System.out.println("could not set keywords!");
    }
  }

  public void setTargetType() {
    if (_target == null) return;
    System.out.println("needs-more-work: set target type");
  }


  ////////////////////////////////////////////////////////////////
  // event handlers


  public void insertUpdate(DocumentEvent e) {
    System.out.println(getClass().getName() + " insert");
    Component ed = _typeField.getEditor().getEditorComponent();
    Document typeDoc = ((JTextField)ed).getDocument();

    if (e.getDocument() == typeDoc) setTargetType();
    super.insertUpdate(e);
  }

  public void removeUpdate(DocumentEvent e) { insertUpdate(e); }

  public void changedUpdate(DocumentEvent e) {
    System.out.println(getClass().getName() + " changed");
    // Apparently, this method is never called.
  }
  
  public void itemStateChanged(ItemEvent e) {
    Object src = e.getSource();
    if (src == _keywordsField) {
      System.out.println("attr keywords now is " +
			 _keywordsField.getSelectedItem());
      setTargetKeywords();
    }
    else if (src == _visField) {
      System.out.println("attr VisibilityKind now is " +
			 _visField.getSelectedItem());
      setTargetVisibility();
    }
    else if (src == _typeField) {
      System.out.println("attr type now is " +
			 _typeField.getSelectedItem());
      setTargetType();
    }
  }


  
} /* end class PropPanelOper */
