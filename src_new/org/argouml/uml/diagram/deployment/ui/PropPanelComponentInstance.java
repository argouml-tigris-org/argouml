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

// File: PropPanelComponentInstance.java
// Classes: PropPanelComponentInstance
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
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.model_management.*;

import org.tigris.gef.base.*;
import org.tigris.gef.graph.*;

import org.argouml.ui.*;
import org.argouml.uml.ui.*;
import org.argouml.uml.diagram.deployment.*;

/** User interface panel shown at the bottom of the screen that allows
 *  the user to edit the properties of the selected UML model
 *  element. */

public class PropPanelComponentInstance extends PropPanel 
implements ItemListener, DocumentListener {

  ////////////////////////////////////////////////////////////////
  // constants
  // needs-more-work 

  ////////////////////////////////////////////////////////////////
  // instance vars
  JLabel _baseLabel = new JLabel("Base : "); 
  JTextField _baseField = new JTextField(); 
  public JLabel _deploymentLocationLabel = new JLabel("Node-Instance: ");
  public static JTextField _deploymentLocationField = new JTextField();

  // declare and initialize all widgets
  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelComponentInstance() {
    super("ComponentInstance Properties");
    _deploymentLocationField.setEditable(false);
    GridBagLayout gb = (GridBagLayout) getLayout();
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 0.0;
    c.ipadx = 0; c.ipady = 0;


    c.gridx = 0;
    c.gridwidth = 1;
    c.gridy = 1; 
    c.weightx = 0.0; 
    gb.setConstraints(_baseLabel, c); 
    add(_baseLabel); 
    c.gridy = 2;
    gb.setConstraints(_deploymentLocationLabel, c);
    add(_deploymentLocationLabel);
        
    c.weightx = 1.0; 
    c.gridx = 1; 
    c.gridy = 1; 
    _baseField.setMinimumSize(new Dimension(120, 20)); 
    gb.setConstraints(_baseField, c); 
    add(_baseField); 
    _baseField.getDocument().addDocumentListener(this); 
    _baseField.setFont(_stereoField.getFont()); 
    c.gridy = 2;
    _deploymentLocationField.setMinimumSize(new Dimension(120, 20));
    gb.setConstraints(_deploymentLocationField, c);
    add(_deploymentLocationField);
    _deploymentLocationField.getDocument().addDocumentListener(this);
    _deploymentLocationField.setFont(_stereoField.getFont());

  }

  ////////////////////////////////////////////////////////////////
  // accessors

  /** Set the values to be shown in all widgets based on model */
  protected void setTargetInternal(Object t) {
    super.setTargetInternal(t);
    MComponentInstance coi = (MComponentInstance) t;
    if (coi.getNodeInstance() != null) {
      MNodeInstance node = (MNodeInstance) coi.getNodeInstance();
      _deploymentLocationField.setText(node.getName());
    }
    else {
      _deploymentLocationField.setText(null);
    }

    if (coi.getClassifiers() != null) { 
      String base = ""; 
      Collection classifiers = coi.getClassifiers(); 
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

    validate();
  }
  
  public void setTargetBase() {  
    if (_target == null) return;  
    if (_inChange) return; 
  
    MComponentInstance coi = (MComponentInstance) _target; 
    MComponent classifier = new MComponentImpl();  
    String base = _baseField.getText(); 
    Collection col = coi.getClassifiers(); 
    if ((col != null) && (col.size()>0)) {  
      Iterator itcol = col.iterator();  
      while (itcol.hasNext()) {  
        MClassifier cls = (MClassifier) itcol.next();  
        coi.removeClassifier(cls);  
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
 
      if (!(model instanceof DeploymentDiagramGraphModel)) continue; 
        
      Vector nodes = model.getNodes(); 
      int s = nodes.size(); 
      for (int j=0; j<s; j++) { 
        MModelElement node = (MModelElement) nodes.elementAt(j); 
        if (node != null && (node instanceof MComponentImpl)) { 
          MComponent mcomp = (MComponent) node; 
          if (mcomp.getNamespace() != coi.getNamespace()) continue;
          String comp_name = mcomp.getName(); 
          if (comp_name != null && (comp_name.equals(base))) { 
            v.addElement(mcomp); 
            coi.setClassifiers(v); 
            return;  
          }       
        } 
      } 
    } 
 
    classifier.setName(base); 
    v.addElement(classifier); 
    coi.setClassifiers(v); 
 
    //System.out.println("needs-more-work: baseClass = " + base);  
    // needs-more-work: this could involve changes to the graph model  
  }  
 

  ////////////////////////////////////////////////////////////////
  // event handlers


  /** The user typed some text */
  public void insertUpdate(DocumentEvent e) {
    //System.out.println(getClass().getName() + " insert");
    // check if it was one of my text fields
    super.insertUpdate(e);
    if (e.getDocument() == _baseField.getDocument()) { 
      setTargetBase(); 
    } 
 }

  public void removeUpdate(DocumentEvent e) { insertUpdate(e); }

  public void changedUpdate(DocumentEvent e) {
    System.out.println(getClass().getName() + " changed");
    // Apparently, this method is never called.
  }

  /** The user modified one of the widgets */
  public void itemStateChanged(ItemEvent e) {
    Object src = e.getSource(); 
    // check for each widget, and update the model with new value 
    if (src == _baseField) { 
      setTargetBase(); 
    }
  }

  static final long serialVersionUID = 4536645723645617622L;
  
} /* end class PropPanelComponentInstance */



