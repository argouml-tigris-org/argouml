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




package uci.uml.ui;

//import jargo.kernel.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import uci.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
//import javax.swing.border.*;

import uci.gef.*;
import uci.uml.visual.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.behavior.activity_graphs.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.model_management.*;
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

  /** Preload property panels that are commonly used within the first
   *  few seconds after the tool is launched. */
  protected void initPanels() {

    _panels.put(MClassImpl.class, new PropPanelClass());
    _panels.put(Diagram.class, new PropPanelDiagram());
    // FigText has no owner, so we do it directly
    _panels.put(FigText.class, new PropPanelString());
    _panels.put(MModelImpl.class, new PropPanelModel());
    _panels.put(MUseCaseImpl.class, new PropPanelUseCase());


	uci.uml.Main.addPostLoadAction(new InitPanelsLater(_panels));
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
		repaint();
	}

  public void refresh() { setTarget(_target); }

  public TabModelTarget findPanelFor(Class targetClass) {
    TabModelTarget p = (TabModelTarget) _panels.get(targetClass);
    System.out.println("Getting prop panel for:" + targetClass+", found"+p);
    if (p == null) {
      Class panelClass = panelClassFor(targetClass);
      if (panelClass == null) return null;
      try { p = (TabModelTarget) panelClass.newInstance(); }
      catch (IllegalAccessException ignore) { return null; }
      catch (InstantiationException ignore) { return null; }
      _panels.put(targetClass, p);
    }
    //else System.out.println("found props for " + targetClass.getName());
    return p;
  }

  public Class panelClassFor(Class targetClass) {
    String pack = "uci.uml.ui";
    String base = getClassBaseName();
    String targetClassName = targetClass.getName();
    int lastDot = targetClassName.lastIndexOf(".");
    if (lastDot > 0) targetClassName = targetClassName.substring(lastDot+1);
    if (targetClassName.startsWith("M"))
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


class InitPanelsLater implements Runnable {
  Hashtable _panels = null;
  public InitPanelsLater(Hashtable p) { _panels = p; }

  /** Load commonly used property panels, but not those that are
   *  commonly used within a few seconds of the tool being launched. */ 
  public void run() {
  //   // preload commonly used property panels

	  //fill the Hashtable. alphabetical order please... ;-)
    _panels.put(MActionStateImpl.class, new PropPanelActionState());
    _panels.put(MActorImpl.class, new PropPanelActor());
    _panels.put(MAssociationImpl.class, new PropPanelAssociation());
    _panels.put(MAssociationRoleImpl.class, new PropPanelAssociationRole());
    _panels.put(MAttributeImpl.class, new PropPanelAttribute());
    // _panels.put(MClassImpl.class, new PropPanelClass());
    _panels.put(MClassifierRoleImpl.class, new PropPanelClassifierRole());
    _panels.put(MDependencyImpl.class, new PropPanelDependency());
    //_panels.put(Diagram.class, new PropPanelDiagram());
    _panels.put(MGeneralizationImpl.class, new PropPanelGeneralization());
    _panels.put(MInstanceImpl.class, new PropPanelInstance());
    _panels.put(MComponentInstanceImpl.class, new PropPanelComponentInstance());
    _panels.put(MComponentImpl.class, new PropPanelComponent());
    _panels.put(MNodeInstanceImpl.class, new PropPanelNodeInstance());
    _panels.put(MNodeImpl.class, new PropPanelNode());
    _panels.put(MObjectImpl.class, new PropPanelObject());
    _panels.put(MInstanceImpl.class, new PropPanelInstance());
    _panels.put(MInterfaceImpl.class, new PropPanelInterface());
    _panels.put(MLinkImpl.class, new PropPanelLink());
    _panels.put(MMessageImpl.class, new PropPanelMessage());
    //_panels.put(MModelImpl.class, new PropPanelModel());

	// how are Notes handled? Toby, nsuml
    //_panels.put(MNoteImpl.class, new PropPanelNote());
    _panels.put(MOperationImpl.class, new PropPanelOperation());
    _panels.put(MPseudostateImpl.class, new PropPanelPseudostate());
	//    _panels.put(Realization.class, new PropPanelRealization());
	// Realization in nsuml!!!
	_panels.put(UMLStateDiagram.class, new PropPanelUMLStateDiagram());
    _panels.put(MStateImpl.class, new PropPanelState());
    _panels.put(String.class, new PropPanelString());
    _panels.put(MTransitionImpl.class, new PropPanelTransition());
    //_panels.put(MUseCaseImpl.class, new PropPanelUseCase());

 	System.out.println("done preloading Property Panels");
  }
} /* end class InitPanelsLater */
