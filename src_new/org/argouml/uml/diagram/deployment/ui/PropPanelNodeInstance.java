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

// File: PropPanelNodeInstance.java
// Classes: PropPanelNodeInstance
// Original Author: 5eichler@informatik.uni-hamburg.de
// $Id$

package org.argouml.uml.diagram.deployment.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.text.*;
import javax.swing.border.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.common_behavior.*; 
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.model_management.*;

import org.tigris.gef.base.*;
import org.tigris.gef.graph.*;

import org.argouml.ui.*;
import org.argouml.uml.ui.*;
import org.argouml.uml.generator.*;
import org.argouml.uml.diagram.deployment.*;

/** User interface panel shown at the bottom of the screen that allows
 *  the user to edit the properties of the selected UML model
 *  element. */

public class PropPanelNodeInstance extends PropPanel {

  ////////////////////////////////////////////////////////////////
  // constants

  
  ////////////////////////////////////////////////////////////////
  // instance vars
  JLabel _baseLabel = new JLabel("Base : "); 
  JTextField _baseField = new JTextField(); 

  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelNodeInstance() {
    super("NodeInstance Properties");
    GridBagLayout gb = (GridBagLayout) getLayout();    
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.ipadx = 0; c.ipady = 0;

    // add all widgets and labels 
 
    c.gridx = 0; 
    c.gridwidth = 1; 
    c.gridy = 1; 
    c.weightx = 0.0; 
    gb.setConstraints(_baseLabel, c); 
    add(_baseLabel); 
 
    c.weightx = 1.0; 
    c.gridx = 1; 
    c.gridy = 1; 
    _baseField.setMinimumSize(new Dimension(120, 20)); 
    gb.setConstraints(_baseField, c); 
    add(_baseField); 
    _baseField.addKeyListener(this); 
    _baseField.addFocusListener(this); 
    _baseField.setFont(_stereoField.getFont()); 

  }

  ////////////////////////////////////////////////////////////////
  // accessors


  /** Set the values to be shown in all widgets based on model */ 
  protected void setTargetInternal(Object t) { 
    super.setTargetInternal(t); 
    MNodeInstance noi = (MNodeInstance) t; 
    // construct bases string (comma separated)
    String baseStr = "";
    Collection col = noi.getClassifiers(); 
    if (col != null && col.size() > 0){
	Iterator it = col.iterator();
	baseStr = ((MClassifier)it.next()).getName(); 
	while (it.hasNext()) { 
	    baseStr += ", "+((MClassifier)it.next()).getName(); 
	} 
	_baseField.setText(baseStr);
    } 
      
    else { 
	_baseField.setText(null); 
    } 
    
    // set the values to be shown in all widgets based on model 
      validate(); 
  } 

  public void setTargetBase() {  
    if (_target == null) return;  
    if (_inChange) return; 
  
    MNodeInstance noi = (MNodeInstance) _target; 
    // use ParserDisplay instead of reimplementiong
    ParserDisplay.SINGLETON.parseNodeInstance(noi, noi.getName() + ":" + _baseField.getText());

  }  

  ////////////////////////////////////////////////////////////////
  // event handling

    public void focusLost(FocusEvent e){
	super.focusLost(e);
	if (e.getComponent() == _baseField)
	    setTargetBase();
    }

  static final long serialVersionUID = 5574833923466612432L;
  
} /* end class PropPanelNode */
