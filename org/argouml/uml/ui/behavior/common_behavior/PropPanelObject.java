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

// File: PropPanelObject.java
// Classes: PropPanelObject
// Original Author: 5eichler@informatik.uni-hamburg.de
// $Id$

package org.argouml.uml.ui.behavior.common_behavior;

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
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.model_management.*;

import org.tigris.gef.base.*;
import org.tigris.gef.graph.*;

import org.argouml.ui.*;
import org.argouml.uml.ui.*;
import org.argouml.uml.diagram.static_structure.*;
import org.argouml.uml.diagram.deployment.*;

/** User interface panel shown at the bottom of the screen that allows
 *  the user to edit the properties of the selected UML model
 *  element. */

public class PropPanelObject extends PropPanel {

  ////////////////////////////////////////////////////////////////
  // constants
  // needs-more-work 

  ////////////////////////////////////////////////////////////////
  // instance vars

  JLabel _baseLabel = new JLabel("Base : ");
  JTextField _baseField = new JTextField();
  JLabel _componentInstanceLabel = new JLabel("ComponentInstance: ");
  public static JTextField _componentInstanceField = new JTextField();
  JLabel _componentLabel = new JLabel("ImplementationLocation: ");
  public static JTextField _componentField = new JTextField();

  // declare and initialize all widgets
  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelObject() {
    super("Object Properties");
    //_componentField.setEditable(false);
    //_componentInstanceField.setEditable(false);

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
    c.gridy = 2;
    gb.setConstraints(_componentLabel, c);
    add(_componentLabel);
    c.gridy = 3;
    gb.setConstraints(_componentInstanceLabel, c);
    add(_componentInstanceLabel);

    c.weightx = 1.0;
    c.gridx = 1;
    c.gridy = 1;
    _baseField.setMinimumSize(new Dimension(120, 20));
    gb.setConstraints(_baseField, c);
    add(_baseField);
    _baseField.addKeyListener(this);
    _baseField.addFocusListener(this);
    _baseField.setFont(_stereoField.getFont());
    c.gridy = 2;
    _componentField.setMinimumSize(new Dimension(120, 20));
    gb.setConstraints(_componentField, c);
    add(_componentField);
    _componentField.setFont(_stereoField.getFont());
    c.gridy = 3;
    _componentField.setMinimumSize(new Dimension(120, 20));
    gb.setConstraints(_componentInstanceField, c);
    add(_componentInstanceField);
    _componentInstanceField.setFont(_stereoField.getFont());
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  /** Set the values to be shown in all widgets based on model */
  protected void setTargetInternal(Object t) {
    super.setTargetInternal(t);
    if (t instanceof MObject) {
      MObject tt = (MObject) t;
      Collection residences = tt.getElementResidences();
      if (tt.getComponentInstance() != null) {
        MComponentInstance comp_inst = tt.getComponentInstance();
        _componentInstanceField.setText(comp_inst.getName());
      }
      else {
        _componentInstanceField.setText(null);
      }
      if (residences != null && (residences.size() > 0)) {
        Iterator it = residences.iterator();
        while (it.hasNext()) {
          MElementResidence residence = (MElementResidence) it.next();
          if (residence != null) {
            MComponent comp = (MComponent) residence.getImplementationLocation();
            if (comp != null) {
            _componentField.setText(comp.getName());
            }
          }
        }
      }
      else {
        _componentField.setText(null);
      }
      if (tt.getClassifiers() != null) {
        String base = "";
        Collection classifiers = tt.getClassifiers();
        Iterator it = classifiers.iterator();
        while (it.hasNext()) {
          Object o = it.next();
          if (o != null && (o instanceof MClassifier)) {
            MClassifier cls = (MClassifier) o;
            if (cls != null) {
              base = cls.getName();
            }
          }
        }
        _baseField.setText(base);        
      } 
      else {
        _baseField.setText(null);
      }
    }
   
    // set the values to be shown in all widgets based on model
      validate();
  }

  public void setTargetBase() { 
    if (_target == null) return; 
    if (_inChange) return;
 
    MObject tt = (MObject) _target;
    MClass classifier = new MClassImpl(); 
    String base = _baseField.getText();
    Collection col = tt.getClassifiers();
    if ((col != null) && (col.size()>0)) { 
      Iterator itcol = col.iterator(); 
      while (itcol.hasNext()) { 
        MClassifier cls = (MClassifier) itcol.next(); 
        tt.removeClassifier(cls); 
      } 
    } 

    Vector diagrams = ProjectBrowser.TheInstance.getProject().getDiagrams();
    GraphModel model = null;
    Vector v = new Vector();
    int size = diagrams.size();
    for (int i=0; i<size; i++) {
      Object o = diagrams.elementAt(i);
      if (!(o instanceof Diagram)) continue;
      if (o instanceof MModel) continue;
      Diagram d = (Diagram) o;
      model = d.getGraphModel(); 

      if (!(model instanceof ClassDiagramGraphModel || model instanceof DeploymentDiagramGraphModel)) continue;
       
      Vector nodes = model.getNodes();
      int s = nodes.size();
      for (int j=0; j<s; j++) {
        MModelElement node = (MModelElement) nodes.elementAt(j);
        if (node != null && (node instanceof MClassImpl)) {
          MClass mclass = (MClass) node;
          if (mclass.getNamespace() != tt.getNamespace()) continue;
          String class_name = mclass.getName();
          if (class_name != null && (class_name.equals(base))) {
            v.addElement(mclass);
            tt.setClassifiers(v);
            return; 
          }      
        }
      }
    }

    classifier.setName(base);
    v.addElement(classifier);
    tt.setClassifiers(v);

    //System.out.println("needs-more-work: baseClass = " + base); 
    // needs-more-work: this could involve changes to the graph model 
  } 

 
  ////////////////////////////////////////////////////////////////
  // event handlers


    public void focusLost(FocusEvent e){
	super.focusLost(e);
	if (e.getComponent() == _baseField)
	    setTargetBase();
    }


} /* end class PropPanelObject */
