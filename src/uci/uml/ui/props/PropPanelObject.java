
package uci.uml.ui.props;

//import jargo.kernel.*;
import java.awt.*;
import java.awt.event.*;
import com.sun.java.util.collections.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.text.*;
import javax.swing.border.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import uci.util.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.model_management.*;
import uci.uml.ui.*;

/** User interface panel shown at the bottom of the screen that allows
 *  the user to edit the properties of the selected UML model
 *  element. */

public class PropPanelObject extends PropPanel
implements DocumentListener, ItemListener {

  ////////////////////////////////////////////////////////////////
  // constants
  // needs-more-work 

  ////////////////////////////////////////////////////////////////
  // instance vars
  JLabel _baseLabel = new JLabel("Base: ");
  JTextField _baseField = new JTextField();
  
  JLabel _componentLabel = new JLabel("ComponentInstance :");
  public static JTextField _componentField = new JTextField();

  // declare and initialize all widgets

  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelObject() {
    super("Object Properties");
    GridBagLayout gb = (GridBagLayout) getLayout();
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 0.0;
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
    _baseField.getDocument().addDocumentListener(this);
    _baseField.setFont(_stereoField.getFont());

    c.gridx = 0;
    c.gridwidth = 1;
    c.gridy = 2;
    c.weightx = 0.0;
    gb.setConstraints(_componentLabel, c);
    add(_componentLabel);

    c.weightx = 1.0;
    c.gridx = 1;
    c.gridy = 2;
    _componentField.setMinimumSize(new Dimension(120, 20));
    gb.setConstraints(_componentField, c);
    add(_componentField);
    _componentField.getDocument().addDocumentListener(this);
    _componentField.setFont(_stereoField.getFont());


    // register interest in change events from all widgets
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  /** Set the values to be shown in all widgets based on model */
  protected void setTargetInternal(Object t) {
    super.setTargetInternal(t);
    MObject tt = (MObject) t;
    if (tt.getUMLClassName() != null)
      _baseField.setText(tt.getUMLClassName().trim());
    if (tt.getComponentInstance() != null) {
      MComponentInstance comp_inst = tt.getComponentInstance();
      _componentField.setText(comp_inst.getName());
    }
    else {
      _componentField.setText(null);
    }
   
    // set the values to be shown in all widgets based on model
    validate();
  }


  ////////////////////////////////////////////////////////////////
  // event handlers


  /** The user typed some text */
  public void insertUpdate(DocumentEvent e) {
    //System.out.println(getClass().getName() + " insert");
    // check if it was one of my text fields
    super.insertUpdate(e);

    if (e.getDocument() == _baseField.getDocument()) {
      setTargetBaseString(_baseField.getText().trim());
    }
    else if (e.getDocument() == _componentField.getDocument()) {
      setTargetComponentString(_componentField.getText());   
    }

  }

  public void removeUpdate(DocumentEvent e) { insertUpdate(e); }

  public void changedUpdate(DocumentEvent e) {
    // Apparently, this method is never called.
  }

  /** The user modified one of the widgets */
  public void itemStateChanged(ItemEvent e) {
    Object src = e.getSource();
    // check for each widget, and update the model with new value
  }


  protected void setTargetBaseString(String s) {
/*
    if (_target == null) return;
    if (_inChange) return;
    try {
      ((MMObject)_target).setBaseString(s);
    }
    catch (PropertyVetoException pve) { } 
*/
  }

  protected void setTargetComponentString(String s) {
/*
    if (_target == null) return;
    if (_inChange) return;
    try {
      ((MMObject)_target).setComponentString(s);
    }
    catch (PropertyVetoException pve) { } 
*/
  }


} /* end class PropPanelObject */
