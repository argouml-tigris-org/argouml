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



// File: PropPanelTransition.java
// Classes: PropPanelTransition
// Original Author: jrobbins@ics.uci.edu
// $Id$

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
import javax.swing.border.*;

import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Model_Management.*;
import uci.uml.Behavioral_Elements.State_Machines.*;
import uci.uml.Behavioral_Elements.Common_Behavior.*;
import uci.uml.generate.*;
import uci.uml.ui.*;

/** User interface panel shown at the bottom of the screen that allows
 *  the user to edit the properties of the selected UML model
 *  element. */

public class PropPanelTransition extends PropPanelTwoEnds {

  ////////////////////////////////////////////////////////////////
  // constants
  // needs-more-work 

  ////////////////////////////////////////////////////////////////
  // instance vars
  JLabel _triggerLabel = new JLabel("Trigger: ");
  JTextField _triggerField = new JTextField();
  JLabel _guardLabel = new JLabel("Guard: ");
  JTextField _guardField = new JTextField();
  JLabel _effectLabel = new JLabel("Effect: ");
  JTextField _effectField = new JTextField();

  // declare and initialize all widgets

  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelTransition() {
    super("Transition Properties");
    GridBagLayout gb = (GridBagLayout) getLayout();
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 0.0;
    c.ipadx = 0; c.ipady = 0;


    c.gridx = 0;
    c.gridwidth = 1;
    c.gridy = 1;
    gb.setConstraints(_triggerLabel, c);
    add(_triggerLabel);

    c.gridy = 2;
    gb.setConstraints(_guardLabel, c);
    add(_guardLabel);

    c.gridy = 3;
    gb.setConstraints(_effectLabel, c);
    add(_effectLabel);

    _triggerField.setMinimumSize(new Dimension(120, 20));
    c.weightx = 1.0;
    c.gridx = 1;
    c.gridy = 1;
    gb.setConstraints(_triggerField, c);
    add(_triggerField);

    c.gridy = 2;
    gb.setConstraints(_guardField, c);
    add(_guardField);

    c.gridy = 3;
    gb.setConstraints(_effectField, c);
    add(_effectField);

    _triggerField.getDocument().addDocumentListener(this);
    _triggerField.setFont(_stereoField.getFont());
    _guardField.getDocument().addDocumentListener(this);
    _guardField.setFont(_stereoField.getFont());
    _effectField.getDocument().addDocumentListener(this);
    _effectField.setFont(_stereoField.getFont());

  }

  ////////////////////////////////////////////////////////////////
  // accessors

  /** Set the values to be shown in all widgets based on model */
  protected void setTargetInternal(Object t) {
    super.setTargetInternal(t);
    Transition tt = (Transition) t;
    _triggerField.setText(GeneratorDisplay.Generate(tt.getTrigger()));
    _guardField.setText(GeneratorDisplay.Generate(tt.getGuard()));
    _effectField.setText(GeneratorDisplay.Generate(tt.getEffect()));
  }

  public String getSourceLabel() {
    if (!(_target instanceof Transition)) return "non Transition";
    return "Source:";
  }
  public String getSourceValue() {
    if (!(_target instanceof Transition)) return "non Transition";
    Transition g = (Transition) _target;
    StateVertex src = g.getSource();
    if (src == null) return "null";
    return src.getName().getBody();
  }
  public String getDestLabel() {
    if (!(_target instanceof Transition)) return "non Transition";
    return "Target:";
  }
  public String getDestValue() {
    if (!(_target instanceof Transition)) return "non Transition";
    Transition g = (Transition) _target;
    StateVertex tar = g.getTarget();
    if (tar == null) return "null";
    return tar.getName().getBody();
  }
  

  public void setTargetTrigger() {
    if (_inChange) return;
    Transition t = (Transition) _target;
    String newText = _triggerField.getText();
    //System.out.println("setTargetTrigger: " + newText);
    try {
      t.setTrigger(ParserDisplay.SINGLETON.parseEvent(newText));
    }
    catch (PropertyVetoException pve) { }
  }

  public void setTargetGuard() {
    if (_inChange) return;
    Transition t = (Transition) _target;
    String newText = _guardField.getText();
    //System.out.println("setTargetGuard: " + newText);
    try {
      t.setGuard(ParserDisplay.SINGLETON.parseGuard(newText));
    }
    catch (PropertyVetoException pve) { }
  }

  public void setTargetEffect() {
    if (_inChange) return;
    Transition t = (Transition) _target;
    String newText = _effectField.getText();
    //System.out.println("setTargetEffect: " + newText);
    try {
      t.setEffect(ParserDisplay.SINGLETON.parseActions(newText));
    }
    catch (PropertyVetoException pve) { }
  }

  ////////////////////////////////////////////////////////////////
  // event handlers


  /** The user typed some text */
  public void insertUpdate(DocumentEvent e) {
    //System.out.println(getClass().getName() + " insert");
    // check if it was one of my text fields
    if (e.getDocument() == _triggerField.getDocument()) setTargetTrigger();
    if (e.getDocument() == _guardField.getDocument()) setTargetGuard();
    if (e.getDocument() == _effectField.getDocument()) setTargetEffect();
    super.insertUpdate(e);
  }

  public void removeUpdate(DocumentEvent e) { insertUpdate(e); }

  public void changedUpdate(DocumentEvent e) {
    //System.out.println(getClass().getName() + " changed");
    // Apparently, this method is never called.
  }

  /** The user modified one of the widgets */
  public void itemStateChanged(ItemEvent e) {
    Object src = e.getSource();
    // check for each widget, and update the model with new value
  }


} /* end class PropPanelState */
