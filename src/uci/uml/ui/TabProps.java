package uci.uml.ui;

//import jargo.kernel.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import uci.util.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.tree.*;
//import com.sun.java.swing.border.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Model_Management.*;

public class TabProps extends TabSpawnable
implements TabModelTarget {
  ////////////////////////////////////////////////////////////////
  // instance variables
  Object _target;
  boolean _shouldBeEnabled = false;
  JPanel blankPane = new JPanel();
  PropPanelModel modelPane = new PropPanelModel();
  PropPanelClass classPane = new PropPanelClass();
  PropPanelInterface interfacePane = new PropPanelInterface();
  PropPanelAttr attrPane = new PropPanelAttr();
  PropPanelOper operPane = new PropPanelOper();
  PropPanelAssoc assocPane = new PropPanelAssoc();
  //PropPanelAssoc assocPane = new PropPanelAssoc();
  JPanel _lastPanel = null;
  
  ////////////////////////////////////////////////////////////////
  // constructor
  public TabProps() {
    super("Properties");
    setLayout(new BorderLayout());
    //setFont(new Font("Dialog", Font.PLAIN, 10));
  }

  ////////////////////////////////////////////////////////////////
  // accessors
  public void setTarget(Object t) {
    _target = t;
    if (_lastPanel != null) remove(_lastPanel);
    if (_target instanceof Model) {
      _shouldBeEnabled = true;
      modelPane.setTarget(_target);
      add(modelPane, BorderLayout.NORTH);
      _lastPanel = modelPane;
    }
    else if (_target instanceof uci.uml.Foundation.Core.Class) {
      _shouldBeEnabled = true;
      classPane.setTarget(_target);
      add(classPane, BorderLayout.NORTH);
      _lastPanel = classPane;
    }
    else if (_target instanceof uci.uml.Foundation.Core.Interface) {
      _shouldBeEnabled = true;
      interfacePane.setTarget(_target);
      add(interfacePane, BorderLayout.NORTH);
      _lastPanel = interfacePane;
    }
    else if (_target instanceof uci.uml.Foundation.Core.Attribute) {
      _shouldBeEnabled = true;
      attrPane.setTarget(_target);
      add(attrPane, BorderLayout.NORTH);
      _lastPanel = attrPane;
    }
    else if (_target instanceof uci.uml.Foundation.Core.Operation) {
      _shouldBeEnabled = true;
      operPane.setTarget(_target);
      add(operPane, BorderLayout.NORTH);
      _lastPanel = operPane;
    }
    else if (_target instanceof uci.uml.Foundation.Core.Association) {
      _shouldBeEnabled = true;
      assocPane.setTarget(_target);
      add(assocPane, BorderLayout.NORTH);
      _lastPanel = assocPane;
    }
    //else if ...
    else {
      _shouldBeEnabled = false;
      add(blankPane, BorderLayout.NORTH);
      _lastPanel = blankPane;
    }
    invalidate();
  }
  public Object getTarget() { return _target; }

  public boolean shouldBeEnabled() { return _shouldBeEnabled; }


} /* end class TabProps */
