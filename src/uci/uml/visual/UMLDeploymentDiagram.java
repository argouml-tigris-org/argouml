
// File: UMLDeploymentDiagram.java
// Classes: UmlDeploymentDiagram
// Author: Clemens Eichler (5eichler@informatik.uni-hamburg.de)


package uci.uml.visual;

import com.sun.java.util.collections.*;
import java.awt.*;
import java.beans.*;
import javax.swing.*;

import uci.gef.*;
import uci.graph.*;
import uci.ui.*;
import uci.uml.ui.*;
import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.common_behavior.*;

public class UMLDeploymentDiagram extends UMLDiagram {

  ////////////////
  // actions for toolbar

  protected static Action _actionMNode =
    new CmdCreateNode(MNodeImpl.class, "Node");

  protected static Action _actionMNodeInstance =
    new CmdCreateNode(MNodeInstanceImpl.class, "NodeInstance");

  protected static Action _actionMComponent = 
    new CmdCreateNode(MComponentImpl.class, "Component");

  protected static Action _actionMComponentInstance = 
    new CmdCreateNode(MComponentInstanceImpl.class, "ComponentInstance");

  protected static Action _actionMClass = 
    new CmdCreateNode(MClassImpl.class, "Class");

  protected static Action _actionMInterface = 
    new CmdCreateNode(MInterfaceImpl.class, "Interface");

  protected static Action _actionMObject =
    new CmdCreateNode(MObjectImpl.class, "Object");

  protected static Action _actionMDependency =			
    new CmdSetMode(ModeCreatePolyEdge.class,
		 "edgeClass", MDependencyImpl.class,					
		 "Dependency");							

  protected static Action _actionMAssociation =			
    new CmdSetMode(ModeCreatePolyEdge.class,
		 "edgeClass", MAssociationImpl.class,					
 		 "Association");							

  protected static Action _actionMLink =
    new CmdSetMode(ModeCreatePolyEdge.class,
		 "edgeClass", MLinkImpl.class,
		 "Link");


  ////////////////////////////////////////////////////////////////
  // contructors
  protected static int _DeploymentDiagramSerial = 1;


  public UMLDeploymentDiagram() {
    try { setName("deployment diagram " + _DeploymentDiagramSerial++); 
    }
    catch (PropertyVetoException pve) { }
  }

  public UMLDeploymentDiagram(MNamespace m) {
    this();
    setNamespace(m);
  }

  public void setNamespace(MNamespace m) {
    super.setNamespace(m);
    DeploymentDiagramGraphModel gm = new DeploymentDiagramGraphModel(); 
    gm.setNamespace(m);
    setGraphModel(gm);
    LayerPerspective lay = new LayerPerspective(m.getName(), gm);   
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
    _toolBar.add(_actionMClass);
    _toolBar.add(_actionMInterface);
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

} /* end class UMLUseCaseDiagram */
