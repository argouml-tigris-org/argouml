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


// 27 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). Added MExtendImpl to
// the list of classes in run().

// 4 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Preloaded
// PropPanelDiagram for each of the standard diagrams in initPanels to
// eliminate messages when trying to find the panel.


package org.argouml.uml.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.behavior.activity_graphs.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;

import org.tigris.gef.base.*;
import org.tigris.gef.presentation.*;

import org.apache.log4j.Category;
import org.argouml.application.api.*;
import org.argouml.application.events.*;
import org.argouml.ui.*;
import org.argouml.uml.diagram.ui.*;
import org.argouml.uml.diagram.activity.ui.*;
import org.argouml.uml.diagram.static_structure.ui.*;
import org.argouml.uml.diagram.collaboration.ui.*;
import org.argouml.uml.diagram.deployment.ui.*;
import org.argouml.uml.diagram.sequence.ui.*;
import org.argouml.uml.diagram.state.ui.*;
import org.argouml.uml.diagram.use_case.ui.*;

import org.argouml.uml.ui.foundation.core.*;
import org.argouml.uml.ui.behavior.common_behavior.*;
import org.argouml.uml.ui.behavior.collaborations.*;
import org.argouml.uml.ui.behavior.activity_graphs.*;
import org.argouml.uml.ui.behavior.state_machines.*;
import org.argouml.uml.ui.behavior.use_cases.*;
import org.argouml.uml.ui.model_management.*;
import org.argouml.uml.ui.foundation.extension_mechanisms.*;
import org.argouml.util.ConfigLoader;

import org.argouml.swingext.*;

public class TabProps extends TabSpawnable
implements TabModelTarget, NavigationListener, ArgoModuleEventListener {
    protected static Category cat = 
        Category.getInstance(TabProps.class);
  ////////////////////////////////////////////////////////////////
  // instance variables
  protected Object    _target;
  protected boolean   _shouldBeEnabled    = false;
  protected JPanel    _blankPanel         = new JPanel();
  protected Hashtable _panels             = new Hashtable();
  protected JPanel    _lastPanel          = null;
  protected String    _panelClassBaseName = "";
  private LinkedList _navListeners = new LinkedList();

  ////////////////////////////////////////////////////////////////
  // constructor
  public TabProps() {
    this("tab.properties", "ui.PropPanel");
  }

  public TabProps(String tabName, String panelClassBase) {
    super(tabName);
    setOrientation(ConfigLoader.getTabPropsOrientation());
    _panelClassBaseName = panelClassBase;
    setLayout(new BorderLayout());
    //setFont(new Font("Dialog", Font.PLAIN, 10));

    ArrayList list = Argo.getPlugins(PluggablePropertyPanel.class);
    ListIterator iterator = list.listIterator();
    while (iterator.hasNext()) {
	Object o = iterator.next();
	PluggablePropertyPanel ppp = (PluggablePropertyPanel)o;
        _panels.put(ppp.getClassForPanel(), ppp.getPropertyPanel());
    }

    ArgoEventPump.addListener(ArgoEventTypes.ANY_MODULE_EVENT, this);

    initPanels();
  }

  public void finalize() {
    ArgoEventPump.removeListener(ArgoEventTypes.ANY_MODULE_EVENT, this);
  }

    /*
     * Set the orientation of the property panel
     * @param orientation the new orientation for this preoprty panel
     */
    public void setOrientation(Orientation orientation) {
        super.setOrientation(orientation);
        Enumeration enum = _panels.elements();
        while (enum.hasMoreElements()) {
            Object o = enum.nextElement();
            if (o instanceof Orientable) {
                Orientable orientable = (Orientable)o;
                orientable.setOrientation(orientation);
            }
        }
    }

  /** Preload property panels that are commonly used within the first
   *  few seconds after the tool is launched. */
  protected void initPanels() {

    _panels.put(MClassImpl.class, new PropPanelClass());
    _panels.put(ArgoDiagram.class, new PropPanelDiagram());

    // Put all the diagram PropPanels here explicitly. They would eventually
    // pick up their superclass ArgoDiagram, but in the meantime
    // panelClassFor() would moan that they can't be found.

    // Note that state digrams do actually have a diagram property panel!

    _panels.put(UMLActivityDiagram.class, new PropPanelUMLActivityDiagram());
    _panels.put(UMLClassDiagram.class, new PropPanelUMLClassDiagram());
    _panels.put(UMLCollaborationDiagram.class, new PropPanelUMLCollaborationDiagram());
    _panels.put(UMLDeploymentDiagram.class, new PropPanelUMLDeploymentDiagram());
    _panels.put(UMLSequenceDiagram.class, new PropPanelUMLSequenceDiagram());
    _panels.put(UMLStateDiagram.class, new PropPanelUMLStateDiagram());
    _panels.put(UMLUseCaseDiagram.class, new PropPanelUMLUseCaseDiagram());
    

    // FigText has no owner, so we do it directly
    _panels.put(FigText.class, new PropPanelString());
    // now a plugin
    // _panels.put(MModelImpl.class, new PropPanelModel());
    _panels.put(MUseCaseImpl.class, new PropPanelUseCase());
    //important: MStateImpl corresponds to PropPanelSimpleState not to PropPanelState!!
    //otherwise, spawing will not ne successful!!
    _panels.put(MStateImpl.class, new PropPanelSimpleState());

    org.argouml.application.Main.addPostLoadAction(new InitPanelsLater(_panels,this,orientation));
  }

  /** Adds a property panel to the internal list. This allows a plugin to
   *  add a register a new property panel an run-time. This property panel will then
   *  be displayed in the detatils pane whenever an element of the given metaclass is
   *  selected.
   *
   * @param c the metaclass whose details show be displayed in the property panel p
   * @param p an instance of the property panel for the metaclass m
   *
   */
  public void addPanel(Class c, PropPanel p) {
    _panels.put(c,p);
  }

    public void addNavigationListener(NavigationListener navListener) {
        _navListeners.add(navListener);
    }

    public void removeNavigationListener(NavigationListener navListener) {
        _navListeners.remove(navListener);
    }

    /**    Called by a user interface element when a request to
     *    navigate to a model element has been received.
     */
    public void navigateTo(Object element) {
        Iterator iter = _navListeners.iterator();
	    while(iter.hasNext()) {
	        ((NavigationListener) iter.next()).navigateTo(element);
	    }
    }

    /**   Called by a user interface element when a request to
     *   open a model element in a new window has been recieved.
     */
    public void open(Object element) {
        Iterator iter = _navListeners.iterator();
        while(iter.hasNext()) {
            ((NavigationListener) iter.next()).open(element);
        }
    }

    public boolean navigateBack(boolean attempt) {
        boolean navigated = false;
        Iterator iter = _navListeners.iterator();
	    while(iter.hasNext()) {
	        navigated = ((NavigationListener) iter.next()).navigateBack(attempt);
            if(navigated) attempt = false;
	    }
        return navigated;
    }

    public boolean navigateForward(boolean attempt) {
        boolean navigated = false;
        Iterator iter = _navListeners.iterator();
	    while(iter.hasNext()) {
	        navigated = ((NavigationListener) iter.next()).navigateForward(attempt);
            if(navigated) attempt = false;
	    }
        return navigated;
    }

    public boolean isNavigateForwardEnabled() {
        boolean enabled = false;
        Iterator iter = _navListeners.iterator();
	    while(iter.hasNext() && !enabled) {
	        enabled = ((NavigationListener) iter.next()).isNavigateForwardEnabled();
	    }
        return enabled;
    }

    public boolean isNavigateBackEnabled() {
        boolean enabled = false;
        Iterator iter = _navListeners.iterator();
	    while(iter.hasNext() && !enabled) {
	        enabled = ((NavigationListener) iter.next()).isNavigateBackEnabled();
	    }
        return enabled;
    }


  ////////////////////////////////////////////////////////////////
  // accessors
	public void setTarget(Object t) {
            
		// don't need to change the target if it is the same as the
                // existing one!
                if(_target == t)
                    return;
                
                _target = t;
                
		if (_lastPanel != null) remove(_lastPanel);
		if (t == null) {
			add(_blankPanel, BorderLayout.CENTER);
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
			if (targetClass == java.lang.Object.class) break;
		}
		if (newPanel instanceof JPanel) {
			newPanel.setTarget(_target);
			add((JPanel) newPanel, BorderLayout.CENTER);
			_shouldBeEnabled = true;
			_lastPanel = (JPanel) newPanel;
		}
		else {
			add(_blankPanel, BorderLayout.CENTER);
			_shouldBeEnabled = false;
			_lastPanel = _blankPanel;
		}
		validate();
		repaint();
	}

  public void refresh() { setTarget(_target); }

  public TabModelTarget findPanelFor(Class targetClass) {
    TabModelTarget p = (TabModelTarget) _panels.get(targetClass);
    cat.debug("Getting prop panel for:" + targetClass+", found"+p);
    if (p == null) {
      Class panelClass = panelClassFor(targetClass);
      if (panelClass == null) return null;
      try { p = (TabModelTarget) panelClass.newInstance(); }
      catch (IllegalAccessException ignore) { return null; }
      catch (InstantiationException ignore) { return null; }
      catch (Exception ignore) { return null; }
      _panels.put(targetClass, p);
    }
    else cat.debug("found props for " + targetClass.getName());
    return p;
  }

  public Class panelClassFor(Class targetClass) {
    String panelClassName="";
    String pack = "org.argouml.uml";
    String base = "";

    String targetClassName = targetClass.getName();
    int lastDot = targetClassName.lastIndexOf(".");

    //remove "ru.novosoft.uml"
    if (lastDot>0)
        base=targetClassName.substring(16, lastDot+1);
    else
        base=targetClassName.substring(16);

    if (lastDot > 0) targetClassName = targetClassName.substring(lastDot+1);

    if (targetClassName.startsWith("M"))
      targetClassName = targetClassName.substring(1); //remove M
    if (targetClassName.endsWith("Impl"))
      targetClassName = targetClassName.substring(0,targetClassName.length()-4); //remove Impl

    // This doesn't work for panel property tabs - they are being put in the
    // wrong place. Really we should have defined these are preloaded them
    // along with ArgoDiagram in initPanels above.

    try {
      panelClassName = pack + ".ui." + base + "PropPanel" + targetClassName;
      return Class.forName(panelClassName);
    }
    catch (ClassNotFoundException ignore) {
        cat.error("Class "+panelClassName+" for Panel not found!", ignore);
    }
    return null;
  }

  protected String getClassBaseName() { return _panelClassBaseName; }

  public Object getTarget() { return _target; }

  public boolean shouldBeEnabled(Object target) {

		if (target == null) {
			_shouldBeEnabled = false;
			return _shouldBeEnabled;
		}
                
		_shouldBeEnabled = true;
		TabModelTarget newPanel = null;
		Class targetClass = target.getClass();
		while (targetClass != null && newPanel == null) {
			newPanel = findPanelFor(targetClass);
			targetClass = targetClass.getSuperclass();
			if (targetClass == java.lang.Object.class) break;
		}
		if (newPanel instanceof JPanel) {
			_shouldBeEnabled = true;
		}
		else {
			_shouldBeEnabled = false;
		}
                
                return _shouldBeEnabled;
  }

  public void moduleLoaded (ArgoModuleEvent event) {
      if (event.getSource() instanceof PluggablePropertyPanel) {
          PluggablePropertyPanel p = (PluggablePropertyPanel)event.getSource();
          _panels.put(p.getClassForPanel(), p.getPropertyPanel());
	  if(p.getPropertyPanel() instanceof UMLUserInterfaceContainer) {
            ((UMLUserInterfaceContainer)p.getPropertyPanel()).addNavigationListener(this);
	}

      }
  }
  public void moduleUnloaded (ArgoModuleEvent event) { }
  public void moduleEnabled (ArgoModuleEvent event) { }
  public void moduleDisabled (ArgoModuleEvent event) { }

} /* end class TabProps */


class InitPanelsLater implements Runnable {
    protected static Category cat = 
        Category.getInstance(InitPanelsLater.class);
    private Hashtable _panels = null;
    private TabProps _tabProps;
    private Orientation _orientation;
    public InitPanelsLater(Hashtable p,TabProps tabProps, Orientation orientation) {
        _panels = p;
        _tabProps = tabProps;
        _orientation = orientation;
    }

  /** Load commonly used property panels, but not those that are
   *  commonly used within a few seconds of the tool being launched. */
 public void run() {
  //   // preload commonly used property panels

	  //fill the Hashtable. alphabetical order please... ;-)
    try {
        _panels.put(MActionStateImpl.class, new PropPanelActionState());
        _panels.put(MActorImpl.class, new PropPanelActor());
        _panels.put(MAssociationImpl.class, new PropPanelAssociation());
        _panels.put(MAssociationRoleImpl.class, new PropPanelAssociationRole());
        _panels.put(MAttributeImpl.class, new PropPanelAttribute());
        // _panels.put(MClassImpl.class, new PropPanelClass());
        _panels.put(MCollaborationImpl.class, new PropPanelCollaboration());
        _panels.put(MClassifierRoleImpl.class, new PropPanelClassifierRole());
        _panels.put(MDependencyImpl.class, new PropPanelDependency());
        _panels.put(MExtendImpl.class, new PropPanelExtend());
        _panels.put(MExtensionPointImpl.class, new PropPanelExtensionPoint());
        //_panels.put(ArgoDiagram.class, new PropPanelDiagram());
        _panels.put(MGeneralizationImpl.class, new PropPanelGeneralization());
        _panels.put(MIncludeImpl.class, new PropPanelInclude());
        _panels.put(MInstanceImpl.class, new PropPanelInstance());
        _panels.put(MComponentInstanceImpl.class, new PropPanelComponentInstance());
        _panels.put(MComponentImpl.class, new PropPanelComponent());
        _panels.put(MNodeInstanceImpl.class, new PropPanelNodeInstance());
        _panels.put(MNodeImpl.class, new PropPanelNode());
        _panels.put(MObjectImpl.class, new PropPanelObject());
        _panels.put(MInstanceImpl.class, new PropPanelInstance());
        _panels.put(MInterfaceImpl.class, new PropPanelInterface());
        _panels.put(MLinkImpl.class, new PropPanelLink());
        _panels.put(MStimulusImpl.class, new PropPanelStimulus());
        _panels.put(MMessageImpl.class, new PropPanelMessage());
        //_panels.put(MModelImpl.class, new PropPanelModel());

            // how are Notes handled? Toby, nsuml
        //_panels.put(MNoteImpl.class, new PropPanelNote());
        _panels.put(MOperationImpl.class, new PropPanelOperation());
        _panels.put(MPseudostateImpl.class, new PropPanelPseudostate());
            //    _panels.put(Realization.class, new PropPanelRealization());
            // Realization in nsuml!!!
            _panels.put(UMLStateDiagram.class, new PropPanelUMLStateDiagram());
        _panels.put(MStateImpl.class, new PropPanelSimpleState());
        _panels.put(MCompositeStateImpl.class, new PropPanelCompositeState());
        _panels.put(MFinalStateImpl.class, new PropPanelFinalState());
        _panels.put(String.class, new PropPanelString());
        _panels.put(MTransitionImpl.class, new PropPanelTransition());
        //_panels.put(MUseCaseImpl.class, new PropPanelUseCase());
        _panels.put(MAssociationEndImpl.class, new PropPanelAssociationEnd());
        _panels.put(MAssociationEndRoleImpl.class, new PropPanelAssociationEndRole());
        _panels.put(MParameterImpl.class,new PropPanelParameter());
        _panels.put(MSignalImpl.class, new PropPanelSignal());
        _panels.put(MStereotypeImpl.class, new PropPanelStereotype());
        _panels.put(MDataTypeImpl.class, new PropPanelDataType());
	// now a plugin
        // _panels.put(MPackageImpl.class, new PropPanelPackage());
        _panels.put(MAbstractionImpl.class, new PropPanelAbstraction());
        _panels.put(MGuardImpl.class,new PropPanelGuard());
        _panels.put(MCallEventImpl.class,new PropPanelCallEvent());
	_panels.put(MCallActionImpl.class,new PropPanelCallAction());
        _panels.put(MInteraction.class, new PropPanelInteraction());
        _panels.put(MStateMachine.class, new PropPanelStateMachine());
    }
    catch(Exception e) {
        cat.error("Exception in InitPanelsLater.run()", e);
     
    }

    Iterator iter = _panels.values().iterator();
    Object panel;
    while(iter.hasNext()) {
        panel = iter.next();
	if(panel instanceof UMLUserInterfaceContainer) {
            ((UMLUserInterfaceContainer) panel).addNavigationListener(_tabProps);
	}
    }

    Argo.log.info("done preloading Property Panels");
  }
} /* end class InitPanelsLater */
