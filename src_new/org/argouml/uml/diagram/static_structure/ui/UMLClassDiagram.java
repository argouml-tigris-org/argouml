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

// File: UMLClassDiagram.java
// Classes: UMLClassDiagram
// Original Author: jrobbins@ics.uci.edy
// $Id$

package org.argouml.uml.diagram.static_structure.ui;

import java.awt.Component;

import javax.swing.Action;
import javax.swing.JToolBar;

import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.swingext.PopupToolBoxButton;
import org.argouml.ui.CmdCreateNode;
import org.argouml.uml.diagram.static_structure.ClassDiagramGraphModel;
import org.argouml.uml.diagram.ui.ActionAddAssociation;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.ui.ActionAddAttribute;
import org.argouml.uml.ui.ActionAddNote;
import org.argouml.uml.ui.ActionAddOperation;
import org.tigris.gef.base.CmdSetMode;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;
import org.tigris.gef.base.ModeCreatePolyEdge;

import ru.novosoft.uml.behavior.common_behavior.MInstance;
import ru.novosoft.uml.behavior.common_behavior.MLink;
import ru.novosoft.uml.foundation.core.MAbstraction;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MDependency;
import ru.novosoft.uml.foundation.core.MGeneralization;
import ru.novosoft.uml.foundation.core.MInterface;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.core.MPermission;
import ru.novosoft.uml.foundation.core.MUsage;
import ru.novosoft.uml.foundation.data_types.MAggregationKind;
import ru.novosoft.uml.model_management.MPackage;
import ru.novosoft.uml.model_management.MModel;
import ru.novosoft.uml.model_management.MSubsystem;

public class UMLClassDiagram extends UMLDiagram {

    ////////////////
    // actions for toolbar
    // TODO: should these be static?

    protected static Action _actionClass = new CmdCreateNode(MClass.class, "Class");

    protected static Action _actionObject = new CmdCreateNode(MInstance.class, "Instance");

    protected static Action _actionInterface = new CmdCreateNode(MInterface.class, "Interface");

    protected static Action _actionDepend = new CmdSetMode(ModeCreatePolyEdge.class, "edgeClass", MDependency.class, "Dependency");

    protected static Action _actionPermission = new CmdSetMode(ModeCreatePolyEdge.class, "edgeClass", MPermission.class, "Permission");

    protected static Action _actionUsage = new CmdSetMode(ModeCreatePolyEdge.class, "edgeClass", MUsage.class, "Usage");

    protected static Action _actionLink = new CmdSetMode(ModeCreatePolyEdge.class, "edgeClass", MLink.class, "Link");

    protected static Action _actionGeneralize = new CmdSetMode(ModeCreatePolyEdge.class, "edgeClass", MGeneralization.class, "Generalization");

    protected static Action _actionRealize = new CmdSetMode(ModeCreatePolyEdge.class, "edgeClass", MAbstraction.class, "Realization");

    protected static Action _actionPackage = new CmdCreateNode(MPackage.class, "Package");

    protected static Action _actionModel = new CmdCreateNode(MModel.class, "Model");

    protected static Action _actionSubsystem = new CmdCreateNode(MSubsystem.class, "Subsystem");

    protected static Action _actionAssociation = new ActionAddAssociation(MAggregationKind.NONE, false, "Association");
    protected static Action _actionAggregation = new ActionAddAssociation(MAggregationKind.AGGREGATE, false, "Aggregation");
    protected static Action _actionComposition = new ActionAddAssociation(MAggregationKind.COMPOSITE, false, "Composition");
    protected static Action _actionUniAssociation = new ActionAddAssociation(MAggregationKind.NONE, true, "UniAssociation");
    protected static Action _actionUniAggregation = new ActionAddAssociation(MAggregationKind.AGGREGATE, true, "UniAggregation");
    protected static Action _actionUniComposition = new ActionAddAssociation(MAggregationKind.COMPOSITE, true, "UniComposition");

    ////////////////////////////////////////////////////////////////
    // contructors
    protected static int _ClassDiagramSerial = 1;

    public UMLClassDiagram() {
        super();
    }

    public UMLClassDiagram(String name, Object m) {
        super(name, (MNamespace)m);
    }

    public UMLClassDiagram(Object m) {
        this(getNewDiagramName(), m);
    }

    public void setNamespace(Object handle) {
        if (!ModelFacade.getInstance().isANamespace(handle)) {
          cat.error("Illegal argument. Object " + handle + " is not a namespace");
          throw new IllegalArgumentException("Illegal argument. Object " + handle + " is not a namespace");
        }
        MNamespace m = (MNamespace)handle;
        super.setNamespace(m);
        ClassDiagramGraphModel gm = new ClassDiagramGraphModel();
        gm.setNamespace(m);
        setGraphModel(gm);
        LayerPerspective lay = new LayerPerspectiveMutable(m.getName(), gm);
        setLayer(lay);
        ClassDiagramRenderer rend = new ClassDiagramRenderer(); // singleton
        lay.setGraphNodeRenderer(rend);
        lay.setGraphEdgeRenderer(rend);
    }

    /**
     * Get the actions from which to create a toolbar or equivilent graphic trigger
     */
    protected Object[] getUmlActions() {
        Object actions[] = {
            getPackageActions(),
            _actionClass,
            getAssociationActions(),
            _actionDepend,
            _actionPermission,
            _actionUsage,
            _actionGeneralize, null,
            _actionInterface,
            _actionRealize, null,
            ActionAddAttribute.SINGLETON,
            ActionAddOperation.SINGLETON, null,
            ActionAddNote.SINGLETON
        };

        return actions;
    }
    
    // remove the comment marks to enable models and subsystems.
    private Object[] getPackageActions() {
        /* subsystem, model enabled 
        Object actions[] = 
            { _actionPackage , 
              _actionModel,
              _actionSubsystem };
        */
        /* subsystem. model disabled */
           Object actions[] = 
            { _actionPackage };
       
        return actions;
    }

    private Object[] getAssociationActions() {
        Object actions[][] = {
            {_actionAssociation,_actionUniAssociation},
            {_actionAggregation,_actionUniAggregation},
            {_actionComposition,_actionUniComposition}
        };

        return actions;
    }
    
    
    /**
     * Creates a new diagramname.
     * @return String
     */
    protected static String getNewDiagramName() {
        String name = null;
        name = "Class Diagram " + _ClassDiagramSerial;
        _ClassDiagramSerial++;
        if (!ProjectManager.getManager().getCurrentProject().isValidDiagramName(name)) {
            name = getNewDiagramName();
        }
        return name;
    }
} /* end class UMLClassDiagram */
