// File: UMLDeploymentDiagram.java
// Classes: UmlDeploymentDiagram
// Author: Clemens Eichler (5eichler@informatik.uni-hamburg.de)

package org.argouml.uml.diagram.deployment.ui;

import java.util.*;
import java.awt.*;
import java.beans.*;
import javax.swing.*;

import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.common_behavior.*;

import org.tigris.gef.base.CmdSetMode;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;
import org.tigris.gef.base.ModeCreatePolyEdge;
import org.tigris.gef.ui.*;

import org.argouml.uml.diagram.ui.*;
import org.argouml.ui.CmdCreateNode;
import org.argouml.uml.diagram.deployment.*;

public class UMLDeploymentDiagram extends UMLDiagram {

  ////////////////
  // actions for toolbar

  protected static Action _actionMNode =
    new CmdCreateNode(MNode.class, "Node");

  protected static Action _actionMNodeInstance =
    new CmdCreateNode(MNodeInstance.class, "NodeInstance");

  protected static Action _actionMComponent = 
    new CmdCreateNode(MComponent.class, "Component");

  protected static Action _actionMComponentInstance = 
    new CmdCreateNode(MComponentInstance.class, "ComponentInstance");

  protected static Action _actionMClass = 
    new CmdCreateNode(MClass.class, "Class");

  protected static Action _actionMInterface = 
    new CmdCreateNode(MInterface.class, "Interface");

  protected static Action _actionMObject =
    new CmdCreateNode(MObject.class, "Object");

  protected static Action _actionMDependency =			
    new CmdSetMode(ModeCreatePolyEdge.class,
		 "edgeClass", MDependency.class,					
		 "Dependency");							

  protected static Action _actionMAssociation =			
    new CmdSetMode(ModeCreatePolyEdge.class,
		 "edgeClass", MAssociation.class,					
 		 "Association");							

  protected static Action _actionMLink =
    new CmdSetMode(ModeCreatePolyEdge.class,
		 "edgeClass", MLink.class,
		 "Link");


  ////////////////////////////////////////////////////////////////
  // contructors
  protected static int _DeploymentDiagramSerial = 1;


  public UMLDeploymentDiagram() {
  
    try { setName(getNewDiagramName()); 
    }
    catch (PropertyVetoException pve) { }
  }

  public UMLDeploymentDiagram(MNamespace m) {
    this();
    setNamespace(m);
  }

    /** method to perform a number of important initializations of a <I>Deployment Diagram</I>. 
     * 
     * each diagram type has a similar <I>UMLxxxDiagram</I> class.
     *
     * @param m  MNamespace from the model in NSUML...
     * @modified changed <I>lay</I> from <I>LayerPerspective</I> to <I>LayerPerspectiveMutable</I>. 
     *           This class is a child of <I>LayerPerspective</I> and was implemented 
     *           to correct some difficulties in changing the model. <I>lay</I> is used 
     *           mainly in <I>LayerManager</I>(GEF) to control the adding, changing and 
     *           deleting layers on the diagram...
     *           psager@tigris.org   Jan. 24, 2oo2
     */        
  public void setNamespace(MNamespace m) {
    super.setNamespace(m);
    DeploymentDiagramGraphModel gm = new DeploymentDiagramGraphModel(); 
    gm.setNamespace(m);
    setGraphModel(gm);
    LayerPerspective lay = new LayerPerspectiveMutable(m.getName(), gm);   
    setLayer(lay);
    DeploymentDiagramRenderer rend = new DeploymentDiagramRenderer(); // singleton
    lay.setGraphNodeRenderer(rend);
    lay.setGraphEdgeRenderer(rend);
  }


  /** initialize the toolbar for this diagram type */
  protected void initToolBar() {
    //System.out.println("making deployment toolbar");
    _toolBar = new ToolBar();
    _toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
//     _toolBar.add(Actions.Cut);
//     _toolBar.add(Actions.Copy);
//     _toolBar.add(Actions.Paste);
//     _toolBar.addSeparator();

    _toolBar.add(_actionSelect);
    _toolBar.add(_actionBroom);
    _toolBar.addSeparator();

    _toolBar.add(_actionMNode);
    _toolBar.add(_actionMNodeInstance);
    _toolBar.add(_actionMComponent);
    _toolBar.add(_actionMComponentInstance);
    _toolBar.add(_actionMDependency);
    _toolBar.add(_actionMAssociation);
    _toolBar.add(_actionMObject); 
    _toolBar.add(_actionMLink);
// other actions
    _toolBar.addSeparator();

    _toolBar.add(_actionRectangle);
    _toolBar.add(_actionRRectangle);
    _toolBar.add(_actionCircle);
    _toolBar.add(_actionLine);
    _toolBar.add(_actionText);
    _toolBar.add(_actionPoly);
    _toolBar.add(_actionSpline);
    _toolBar.add(_actionInk);
    _toolBar.addSeparator();

    _toolBar.add(_diagramName);
  }

  static final long serialVersionUID = -375918274062198744L;
  
  protected static String getNewDiagramName() {
  	String name = null;
  	Object[] args = {name};
  	do {
        name = "deployment diagram " + _DeploymentDiagramSerial;
        _DeploymentDiagramSerial++;
        args[0] = name;
    }
    while (TheInstance.vetoCheck("name", args));
    return name;
  }

} /* end class UMLDeploymentDiagram */
