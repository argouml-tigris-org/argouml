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

// File: TabStyle.java
// Classes: TabStyle
// Original Author:
// $Id$

// 12 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Extended to support
// use case style panel that handles optional display of extension points.


package org.argouml.uml.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.model_management.*;

import org.tigris.gef.base.Diagram;
import org.tigris.gef.presentation.Fig;

import org.argouml.kernel.*;
import org.argouml.ui.*;
import org.argouml.uml.diagram.ui.*;
import org.argouml.uml.diagram.state.ui.*;
import org.argouml.uml.diagram.static_structure.ui.*;
import org.argouml.uml.diagram.use_case.ui.*;

public class TabStyle extends TabSpawnable
implements TabFigTarget, PropertyChangeListener, DelayedVChangeListener {
  ////////////////////////////////////////////////////////////////
  // instance variables
  protected Fig           _target;
  protected boolean       _shouldBeEnabled    = false;
  protected JPanel        _blankPanel         = new JPanel();
  protected Hashtable     _panels             = new Hashtable();
  protected JPanel        _lastPanel          = null;
  protected TabFigTarget  _stylePanel         = null;
  protected String        _panelClassBaseName = "";
  protected String        _alternativeBase    = "";

  ////////////////////////////////////////////////////////////////
  // constructor
  public TabStyle(String tabName, String panelClassBase, String altBase) {
    super(tabName);
    _panelClassBaseName = panelClassBase;
    _alternativeBase = altBase;
    setLayout(new BorderLayout());
    //setFont(new Font("Dialog", Font.PLAIN, 10));
    initPanels();
  }

  public TabStyle() {
    this("Style", "style.StylePanel", "style.SP");
  }

  protected void initPanels() {
    StylePanelFigClass spfc      = new StylePanelFigClass();
    StylePanelFigInterface spfi  = new StylePanelFigInterface();
    StylePanelFigUseCase spfuc   = new StylePanelFigUseCase();
    SPFigEdgeModelElement spfeme = new SPFigEdgeModelElement();
    StylePanelFig spf            = new StylePanelFig();

    _panels.put(FigClass.class, spfc);
    _panels.put(FigUseCase.class, spfuc);
    _panels.put(FigNodeModelElement.class, spf);
    _panels.put(FigEdgeModelElement.class, spfeme);
    _panels.put(FigInterface.class, spfi);
    _panels.put(FigAssociation.class, spfeme);
    _panels.put(FigSimpleState.class, spf);
    _panels.put(FigTransition.class, spfeme);
    _panels.put(FigActor.class, spf);
    _panels.put(FigInstance.class, spf);
    _panels.put(FigLink.class, spfeme);
    _panels.put(FigGeneralization.class, spfeme);
    _panels.put(FigRealization.class, spfeme);
  }

   /** Adds a style panel to the internal list. This allows a plugin to
   *  add and register a new style panel at run-time. This property style will
   *  then be displayed in the detatils pane whenever an element of the given 
   *  metaclass is selected.
   *
   * @param c the metaclass whose details show be displayed in the property panel p
   * @param s an instance of the style panel for the metaclass m
   *
   */
  public void addPanel(Class c, StylePanel s) {
    _panels.put(c,s);
  }


  ////////////////////////////////////////////////////////////////
  // accessors
  public void setTarget(Object t) {
    if (_target != null) _target.removePropertyChangeListener(this);
    
    if( !(t instanceof Fig))
        return;
    
    _target = (Fig)t;
    if (_target != null) _target.addPropertyChangeListener(this);
    if (_lastPanel != null) remove(_lastPanel);
    if (t == null) {
      add(_blankPanel, BorderLayout.NORTH);
      _shouldBeEnabled = false;
      _lastPanel = _blankPanel;
      return;
    }
    _shouldBeEnabled = true;
    _stylePanel = null;
    Class targetClass = t.getClass();
    while (targetClass != null && _stylePanel == null) {
      _stylePanel = findPanelFor(targetClass);
      targetClass = targetClass.getSuperclass();
    }
    if (_stylePanel != null) {
      _stylePanel.setTarget(_target);
      add((JPanel)_stylePanel, BorderLayout.NORTH);
      _shouldBeEnabled = true;
      _lastPanel = (JPanel) _stylePanel;
    }
    else {
      add(_blankPanel, BorderLayout.NORTH);
      _shouldBeEnabled = false;
      _lastPanel = _blankPanel;
    }
    validate();
    repaint();
  }

  public void refresh() { setTarget(_target); }

  public TabFigTarget findPanelFor(Class targetClass) {
    TabFigTarget p = (TabFigTarget) _panels.get(targetClass);
    if (p == null) {
      Class panelClass = panelClassFor(targetClass);
      if (panelClass == null) return null;
      try { p = (TabFigTarget) panelClass.newInstance(); }
      catch (IllegalAccessException ignore) { return null; }
      catch (InstantiationException ignore) { return null; }
      _panels.put(targetClass, p);
    }
    else cat.debug("found style for " + targetClass.getName());
    return p;
  }

  public Class panelClassFor(Class targetClass) {
    String pack = "org.argouml.ui";
    String base = getClassBaseName();
    String alt = getAlternativeClassBaseName();

    String targetClassName = targetClass.getName();
    int lastDot = targetClassName.lastIndexOf(".");
    if (lastDot > 0) targetClassName = targetClassName.substring(lastDot+1);
    try {
      String panelClassName = pack + "." + base + targetClassName;
      Class cls = Class.forName(panelClassName);
      return cls;
    }
    catch (ClassNotFoundException ignore) { }
    try {
      String panelClassName = pack + "." + alt + targetClassName;
      Class cls = Class.forName(panelClassName);
      return cls;
    }
    catch (ClassNotFoundException ignore) { }
    return null;
  }

  protected String getClassBaseName() { return _panelClassBaseName; }

  protected String getAlternativeClassBaseName() {
    return _alternativeBase; }

  public Object getTarget() { return _target; }

  public boolean shouldBeEnabled(Object target) {
  
    if (target == null) {
      _shouldBeEnabled = false;
      return _shouldBeEnabled;
    }
    
    _shouldBeEnabled = true;
    _stylePanel = null;
    Class targetClass = target.getClass();
    while (targetClass != null && _stylePanel == null) {
      _stylePanel = findPanelFor(targetClass);
      targetClass = targetClass.getSuperclass();
    }
    if (_stylePanel != null) {
      _shouldBeEnabled = true;
    }
    else {
      _shouldBeEnabled = false;
    }
    
    return _shouldBeEnabled;
  }


  ////////////////////////////////////////////////////////////////
  // PropertyChangeListener implementation

  public void propertyChange(PropertyChangeEvent pce) {
    DelayedChangeNotify delayedNotify = new DelayedChangeNotify(this, pce);
    SwingUtilities.invokeLater(delayedNotify);
  }

  public void delayedVetoableChange(PropertyChangeEvent pce) {
    if (_stylePanel != null) _stylePanel.refresh();
  }

} /* end class TabStyle */
