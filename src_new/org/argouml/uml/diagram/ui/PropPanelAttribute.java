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



// File: PropPanelAttribute.java
// Classes: PropPanelAttribute
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.diagram.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.text.*;
import javax.swing.border.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.model_management.*;

import org.tigris.gef.util.*;

import org.argouml.kernel.*;
import org.argouml.ui.*;
import org.argouml.uml.ui.*;

/** User interface panel shown at the bottom of the screen that allows
 *  the user to edit the properties of the selected UML model
 *  element. */

public class PropPanelAttribute extends PropPanel
implements ItemListener {

  ////////////////////////////////////////////////////////////////
  // constants
  public static final String VISIBILITIES[] = {
    MVisibilityKind.PUBLIC.getName(), MVisibilityKind.PRIVATE.getName(),
    MVisibilityKind.PROTECTED.getName() };
	// what about PACKAGE in nsuml?

  public static final String ATTRKEYWORDS[] = {
    "none", "transient", "static", "final", "static final"};


  public static Vector OFFERED_TYPES = null;
  
  ////////////////////////////////////////////////////////////////
  // instance vars
  JLabel _visLabel = new JLabel("Visibility: ");
  JComboBox _visField = new JComboBox(VISIBILITIES);
  JLabel _keywordsLabel = new JLabel("Keywords: ");
  JComboBox _keywordsField = new JComboBox(ATTRKEYWORDS);
  JLabel _typeLabel = new JLabel("Type: ");
  JComboBox _typeField = new JComboBox();
  JLabel _initLabel = new JLabel("Initial Value: ");
  JTextArea _initText = new JTextArea();
  SpacerPanel _spacer = new SpacerPanel();

  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelAttribute() {
    super("Attribute Properties");
    GridBagLayout gb = (GridBagLayout) getLayout();
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.ipadx = 0; c.ipady = 0;


    //_visField.getEditor().getEditorComponent().setBackground(Color.white);
    //_keywordsField.getEditor().getEditorComponent().setBackground(Color.white);
    _typeField.setEditable(true);
    _typeField.getEditor().getEditorComponent().setBackground(Color.white);
    _typeField.setRenderer(new UMLListCellRenderer());

    c.gridx = 0;
    c.gridwidth = 1;
    c.gridy = 1;
    c.weightx = 0.0;
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
    gb.setConstraints(_initLabel, c);
    add(_initLabel);
    c.gridy = 1;
    c.gridheight = GridBagConstraints.REMAINDER;
    JScrollPane initScroll = new JScrollPane(_initText);
    gb.setConstraints(initScroll, c);
    add(initScroll);

    _initText.addKeyListener(this);
    _initText.addFocusListener(this);


    _visField.addItemListener(this);
    _keywordsField.addItemListener(this);
    _typeField.addItemListener(this);

    remove(_namespaceLabel);
    remove(_namespaceField);
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  protected void setTargetInternal(Object t) {
    super.setTargetInternal(t);
    MAttribute attr = (MAttribute) t;

    Vector offeredTypes = getOfferedTypes();
    if (offeredTypes != null)
      _typeField.setModel(new DefaultComboBoxModel(Converter.convert(offeredTypes)));

    MVisibilityKind vk = attr.getVisibility();
	if (vk != null)
		_visField.setSelectedItem(vk.getName());

    MScopeKind sk = attr.getOwnerScope();
    MChangeableKind ck = attr.getChangeability();

    if (MScopeKind.CLASSIFIER.equals(sk) && MChangeableKind.FROZEN.equals(ck))
      _keywordsField.setSelectedItem("static final");
    else if (MScopeKind.CLASSIFIER.equals(sk))
      _keywordsField.setSelectedItem("static");
    else if (MChangeableKind.FROZEN.equals(ck))
      _keywordsField.setSelectedItem("final");
    else
      _keywordsField.setSelectedItem("None");

    MClassifier type = attr.getType();
    if (type.getName() != null)
	_typeField.setSelectedItem(type.getName());

    MExpression expr = attr.getInitialValue();
    if (expr == null) _initText.setText("");
    else _initText.setText(expr.getBody());

  }



  public void setTargetVisibility() {
    if (_target == null) return;
    if (_inChange) return;
    MVisibilityKind vk = MVisibilityKind.forName((String)_visField.getSelectedItem());
    MAttribute attr = (MAttribute) _target;
	attr.setVisibility(vk);
  }

  public void setTargetKeywords() {
    if (_target == null) return;
    if (_inChange) return;
    String keys = (String) _keywordsField.getSelectedItem();
    if (keys == null) {
      //System.out.println("keywords are null");
      return;
    }
    MAttribute attr = (MAttribute) _target;
	if (keys.equals("None")) {
		attr.setOwnerScope(MScopeKind.INSTANCE);
		attr.setChangeability(null);
	}
	else if (keys.equals("transient")) {
		System.out.println("needs-more-work: setting to transient...");
		attr.setOwnerScope(MScopeKind.INSTANCE);
		attr.setChangeability(null);
	}
	else if (keys.equals("static")) {
		attr.setOwnerScope(MScopeKind.CLASSIFIER);
		attr.setChangeability(null);
	}
	else if (keys.equals("final")) {
		attr.setOwnerScope(MScopeKind.INSTANCE);
		attr.setChangeability(MChangeableKind.FROZEN);
	}
	else if (keys.equals("static final")) {
		attr.setOwnerScope(MScopeKind.CLASSIFIER);
		attr.setChangeability(MChangeableKind.FROZEN);
	}
  }

  public void setTargetType() {
    if (!(_target instanceof MAttribute)) return;
    if (_inChange) return;
    MAttribute attr = (MAttribute) _target;
    Object sel = _typeField.getSelectedItem();
    if (sel == null) return;
    // System.out.println("set target type: " + sel);
    Project p = ProjectBrowser.TheInstance.getProject();
    MClassifier type = p.findType((String)sel);

    if (type == null) {
	System.out.println("should not be here!");
	type = new MClassifierImpl();
	type.setName(sel.toString());
    }
    attr.setType(type);
  }


  public void setTargetInit() {
    if (_target == null) return;
    String initStr = _initText.getText();
    MAttribute attr = (MAttribute) _target;
	MExpression exp = new MExpression("", initStr);
	attr.setInitialValue(exp);
  }




  ////////////////////////////////////////////////////////////////
  // event handlers

    public void focusLost(FocusEvent e){
	super.focusLost(e);
	if (e.getComponent() == _initText)
	    setTargetInit();
    }

  public void itemStateChanged(ItemEvent e) {
	  if (e.getStateChange() == ItemEvent.SELECTED) {
		  Object src = e.getSource();
		  if (src == _keywordsField) {
			  //System.out.println("attr keywords now is " +
			  //_keywordsField.getSelectedItem());
			  setTargetKeywords();
		  }
		  else if (src == _visField) {
			  //System.out.println("attr MVisibilityKind now is " +
			  //_visField.getSelectedItem());
			  setTargetVisibility();
		  }
		  else if (src == _typeField) {
			  //System.out.println(getClass().getName() + " itemStateChanged " +e);
			  //       System.out.println("attr type now is " +
			  // 			 _typeField.getSelectedItem());
			  setTargetType();
		  }
		  else if (src == _initText) {
			  // needs-more-work: I might want to have an expression builder
			  // or a history of useful expressions 
			  //System.out.println("attr init now is " +
			  //_initText.getText());
			  setTargetInit();
		  }
	  }
  }


  ////////////////////////////////////////////////////////////////
  // static methods

  public static Vector getOfferedTypes() {
    // needs-more-work: should update when project changes
    Project p = ProjectBrowser.TheInstance.getProject();
    Vector types = p.getDefinedTypesVector();
    Vector res = new Vector();
    for (int i = 0; i<types.size();i++) {
	res.add(((MClassifier)types.get(i)).getName());
    }
    return res;
  }

} /* end class PropPanelAttribute */
