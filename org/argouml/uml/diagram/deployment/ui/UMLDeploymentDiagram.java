// File: UMLDeploymentDiagram.java
// Classes: UmlDeploymentDiagram
// Author: Clemens Eichler (5eichler@informatik.uni-hamburg.de)

package org.argouml.uml.diagram.deployment.ui;

import java.beans.PropertyVetoException;

import javax.swing.Action;
import javax.swing.JToolBar;

import org.apache.log4j.Category;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.CmdCreateNode;
import org.argouml.uml.diagram.deployment.DeploymentDiagramGraphModel;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.ui.ActionAddNote;
import org.tigris.gef.base.CmdSetMode;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;
import org.tigris.gef.base.ModeCreatePolyEdge;
import org.tigris.gef.ui.ToolBar;
import ru.novosoft.uml.behavior.common_behavior.MComponentInstance;
import ru.novosoft.uml.behavior.common_behavior.MLink;
import ru.novosoft.uml.behavior.common_behavior.MNodeInstance;
import ru.novosoft.uml.behavior.common_behavior.MObject;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MComponent;
import ru.novosoft.uml.foundation.core.MDependency;
import ru.novosoft.uml.foundation.core.MInterface;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.core.MNode;

public class UMLDeploymentDiagram extends UMLDiagram {
    protected static Category cat = Category.getInstance(UMLDeploymentDiagram.class);

    ////////////////
    // actions for toolbar

    protected static Action _actionMNode = new CmdCreateNode(MNode.class, "Node");

    protected static Action _actionMNodeInstance = new CmdCreateNode(MNodeInstance.class, "NodeInstance");

    protected static Action _actionMComponent = new CmdCreateNode(MComponent.class, "Component");

    protected static Action _actionMComponentInstance = new CmdCreateNode(MComponentInstance.class, "ComponentInstance");

    protected static Action _actionMClass = new CmdCreateNode(MClass.class, "Class");

    protected static Action _actionMInterface = new CmdCreateNode(MInterface.class, "Interface");

    protected static Action _actionMObject = new CmdCreateNode(MObject.class, "Object");

    protected static Action _actionMDependency = new CmdSetMode(ModeCreatePolyEdge.class, "edgeClass", MDependency.class, "Dependency");

    protected static Action _actionMAssociation = new CmdSetMode(ModeCreatePolyEdge.class, "edgeClass", MAssociation.class, "Association");

    protected static Action _actionMLink = new CmdSetMode(ModeCreatePolyEdge.class, "edgeClass", MLink.class, "Link");

    ////////////////////////////////////////////////////////////////
    // contructors
    protected static int _DeploymentDiagramSerial = 1;

    public UMLDeploymentDiagram() {

        try {
            setName(getNewDiagramName());
        } catch (PropertyVetoException pve) {
        }
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

    /**
     * <p>Initialize the toolbar with buttons required for a use case diagram.</p>
     * @param toolBar The toolbar to which to add the buttons.
     */
    protected void initToolBar(JToolBar toolBar) {
        toolBar.add(_actionMNode);
        toolBar.add(_actionMNodeInstance);
        toolBar.add(_actionMComponent);
        toolBar.add(_actionMComponentInstance);
        toolBar.add(_actionMDependency);
        toolBar.add(_actionMAssociation);
        toolBar.add(_actionMObject);
        toolBar.add(_actionMLink);
        // other actions
        toolBar.addSeparator();
        toolBar.add(ActionAddNote.SINGLETON);
    }

    static final long serialVersionUID = -375918274062198744L;

    /**
         * Creates a new diagramname.
         * @return String
         */
    protected static String getNewDiagramName() {
        String name = null;
        name = "Deployment Diagram " + _DeploymentDiagramSerial;
        _DeploymentDiagramSerial++;
        if (!ProjectManager.getManager().getCurrentProject().isValidDiagramName(name)) {
            name = getNewDiagramName();
        }
        return name;
    }
} /* end class UMLDeploymentDiagram */
