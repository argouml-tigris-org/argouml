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


package org.argouml.uml.ui;
import javax.swing.*;

import org.apache.log4j.Category;

import java.awt.event.*;
import java.lang.reflect.*;
import ru.novosoft.uml.*;

public class UMLMetaclassComboBox extends JComboBox implements UMLUserInterfaceComponent, ItemListener {
   protected static Category cat = Category.getInstance(UMLMetaclassComboBox.class);

  private String[] _metaclasses =
    { "ModelElement", "Classifier", "Class", "Interface", "DataType", "Exception", "Signal",
        "Association", "AssociationEnd", "Attribute", "Operation", "Generalization", "Flow", "Usage", "BehavioralFeature",
        "CallEvent", "Abstraction", "Component", "Package", "Constraint", "Comment","ObjectFlowState",
        "Model", "Subsystem", "Collaboration", "Permission", "Actor", "Node", "NodeInstance", "Link" };

  private Method _getMethod;
  private Method _setMethod;
  private String _property;
  private UMLUserInterfaceContainer _container;
  private Object[] _noArgs = {};

  public UMLMetaclassComboBox(UMLUserInterfaceContainer container,String property,String getMethod,String setMethod) {
    setModel(new DefaultComboBoxModel(_metaclasses));
    _container = container;
    _property = property;
    addItemListener(this);
    try {
      _getMethod = container.getClass().getMethod(getMethod,new Class[] { });
    }
    catch(Exception e) {
        cat.error("Error in UMLMetaclassComboBox:" + getMethod, e);
    }
    try {
      _setMethod = container.getClass().getMethod(setMethod,new Class[] { String.class });
    }
    catch(Exception e) {
        cat.error("Error in UMLMetaclassComboBox:" + setMethod, e);
    }
  }

  public void propertySet(MElementEvent e) {
    String eventName = e.getName();
    if(eventName == null || _property == null || eventName.equals(_property)) {
      update();
    }
  }

  public void roleAdded(MElementEvent e) {
  }

  public void roleRemoved(MElementEvent e) {
  }

  public void listRoleItemSet(MElementEvent e) {
  }

  public void removed(MElementEvent e) {
  }

  public void recovered(MElementEvent e) {
  }

  public void targetChanged() {
    update();
  }

  public void targetReasserted() {
  }

  private void update() {
    String meta = "ModelElement";
    try {
      meta = (String) _getMethod.invoke(_container,_noArgs);
    }
    catch(Exception e) {
    }
    ComboBoxModel model = getModel();
    Object element;
    int size =  model.getSize();
    int i = 0;
    for(i = 0; i < size; i++) {
      element = model.getElementAt(i);
      if(element.equals(meta)) {
        model.setSelectedItem(element);
        break;
      }
    }
    if(i == size) {
      model.setSelectedItem(model.getElementAt(0));
    }
  }

  public void itemStateChanged(ItemEvent event) {
    if(event.getStateChange() == ItemEvent.SELECTED) {
      Object selectedItem = event.getItem();
      try {
        _setMethod.invoke(_container,new Object[] { selectedItem.toString() });
      }
      catch(Exception e) {
      }
    }
  }
}