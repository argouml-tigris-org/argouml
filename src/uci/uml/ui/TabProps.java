// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products
// must be negotiated with University of California. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "as is",
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
import uci.util.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.tree.*;
//import com.sun.java.swing.border.*;

import uci.gef.Diagram;
import uci.uml.Foundation.Core.*;
import uci.uml.Behavioral_Elements.Common_Behavior.*;
import uci.uml.Behavioral_Elements.State_Machines.*;
import uci.uml.Behavioral_Elements.Use_Cases.*;
import uci.uml.Model_Management.*;
import uci.uml.ui.props.*;

public class TabProps extends TabSpawnable
implements TabModelTarget {
  ////////////////////////////////////////////////////////////////
  // instance variables
  protected Object    _target;
  protected boolean   _shouldBeEnabled    = false;
  protected JPanel    _blankPanel         = new JPanel();
  protected Hashtable _panels             = new Hashtable();
  protected JPanel    _lastPanel          = null;
  protected String    _panelClassBaseName = "";

  ////////////////////////////////////////////////////////////////
  // constructor
  public TabProps(String tabName, String panelClassBase) {
    super(tabName);
    _panelClassBaseName = panelClassBase;
    setLayout(new BorderLayout());
    //setFont(new Font("Dialog", Font.PLAIN, 10));
    initPanels();
  }

  public TabProps() {
    this("Properties", "props.PropPanel");
  }
  
  protected void initPanels() {
    //_panels.put(Diagram.class, new PropPanelDiagram());
    //_panels.put(Model.class, new PropPanelModel());
    //_panels.put(MMClass.class, new PropPanelClass());
    //_panels.put(Interface.class, new PropPanelInterface());
    //_panels.put(Attribute.class, new PropPanelAttr());
    //_panels.put(Operation.class, new PropPanelOper());
    //_panels.put(Association.class, new PropPanelAssoc());
    //_panels.put(State.class, new PropPanelState());
    //_panels.put(Transition.class, new PropPanelTransition());
    //_panels.put(Pseudostate.class, new PropPanelPseudostate());
    //_panels.put(UseCase.class, new PropPanelUseCase());
    //_panels.put(Actor.class, new PropPanelActor());
    //_panels.put(Instance.class, new PropPanelInstance());
    //_panels.put(Link.class, new PropPanelLink());
    //_panels.put(Generalization.class, new PropPanelGeneralization());
    //_panels.put(Realization.class, new PropPanelRealization());
  }
  
  ////////////////////////////////////////////////////////////////
  // accessors
  public void setTarget(Object t) {
    _target = t;
    if (_lastPanel != null) remove(_lastPanel);
    if (t == null) {
      add(_blankPanel, BorderLayout.NORTH);      
      _shouldBeEnabled = false;
      _lastPanel = _blankPanel;
      return;
    }
    _shouldBeEnabled = true;
    TabModelTarget newPanel = null;
    Class targetClass = t.getClass();
    while (targetClass != null && newPanel == null) {
      newPanel = findPanelFor(targetClass);
      targetClass = targetClass.getSuperclass();
    }
    if (newPanel instanceof JPanel) {
      newPanel.setTarget(_target);
      add((JPanel) newPanel, BorderLayout.NORTH);
      _shouldBeEnabled = true;
      _lastPanel = (JPanel) newPanel;
    }
    else {
      add(_blankPanel, BorderLayout.NORTH);      
      _shouldBeEnabled = false;
      _lastPanel = _blankPanel;
    }
    validate();
  }

  public void refresh() { setTarget(_target); }
  
  public TabModelTarget findPanelFor(Class targetClass) {
    TabModelTarget p = (TabModelTarget) _panels.get(targetClass);
    if (p == null) {
      Class panelClass = panelClassFor(targetClass);
      if (panelClass == null) return null;
      try { p = (TabModelTarget) panelClass.newInstance(); }
      catch (IllegalAccessException ignore) { return null; }
      catch (InstantiationException ignore) { return null; }
      _panels.put(targetClass, p);
    }
    return p;
  }

  
  public Class panelClassFor(Class targetClass) {
    String pack = "uci.uml.ui";
    String base = getClassBaseName();
    String targetClassName = targetClass.getName();
    int lastDot = targetClassName.lastIndexOf(".");
    if (lastDot > 0) targetClassName = targetClassName.substring(lastDot+1);
    if (targetClassName.startsWith("MM"))
      targetClassName = targetClassName.substring(2);
    try {
      String panelClassName = pack + "." + base + targetClassName;
      return Class.forName(panelClassName);
    }
    catch (ClassNotFoundException ignore) { }
    return null;
  }

  protected String getClassBaseName() { return _panelClassBaseName; }
  
  public Object getTarget() { return _target; }

  public boolean shouldBeEnabled() { return _shouldBeEnabled; }


} /* end class TabProps */
