// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

package org.argouml.uml.diagram.collaboration.ui;

import java.beans.PropertyVetoException;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.Action;

import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.CmdCreateNode;
import org.argouml.ui.CmdSetMode;
import org.argouml.uml.diagram.collaboration.CollabDiagramGraphModel;
import org.argouml.uml.diagram.ui.ActionAddAssociationRole;
import org.argouml.uml.diagram.ui.ActionAddMessage;
import org.argouml.uml.diagram.ui.FigMessage;
import org.argouml.uml.diagram.ui.RadioAction;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;
import org.tigris.gef.base.ModeCreatePolyEdge;
import org.tigris.gef.presentation.Fig;

/**
 * The base class of the collaboration diagram.<p>
 *
 * Defines the toolbar, provides for its initialization and provides
 * constructors for a top level diagram and one within a defined
 * namespace.<p>
 *
 * @author agauthie@ics.uci.edu
 */
public class UMLCollaborationDiagram extends UMLDiagram {

    private Object collaboration;
    /**
     * Logging.
     */
    private static final Logger LOG =
        Logger.getLogger(UMLCollaborationDiagram.class);

    ////////////////////////
    // actions for toolbar

    private Action actionClassifierRole;
    private Action actionGeneralize;

    private Action actionAssociation;
    private Action actionAggregation;
    private Action actionComposition;
    private Action actionUniAssociation;
    private Action actionUniAggregation;
    private Action actionUniComposition;

    private Action actionDepend;
    private Action actionMessage;

    ////////////////////////////////////////////////////////////////
    // contructors

    /**
     * This constructor is used to build a dummy collaboration diagram so
     * that a project will load properly.
     */
    public UMLCollaborationDiagram() {
        try {
            setName(getNewDiagramName());
        } catch (PropertyVetoException pve) { }
    }

    /**
     * The constructor.
     * 
     * @param namespace the namespace for the diagram
     */
    private UMLCollaborationDiagram(Object namespace) {
        this();
        setNamespace(namespace);
    }

    /**
     * The constructor.
     *
     * @param namespace the namespace for the diagram
     * @param collab the collaboration represented by this diagram
     */
    public UMLCollaborationDiagram(Object namespace, Object collab) {
        this();
        collaboration = collab;
        setNamespace(namespace);
    }

    /**
     * The owner of a collaboration diagram is the collaboration
     * it's showing.
     * 
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getOwner()
     */
    public Object getOwner() {
        CollabDiagramGraphModel gm = (CollabDiagramGraphModel) getGraphModel();
        return gm.getCollaboration();
    }
    
    /**
     * @return the number of UML messages in the diagram
     */
    public int getNumMessages() {
        Layer lay = getLayer();
        Collection figs = lay.getContents(null);
        int res = 0;
        Iterator it = figs.iterator();
        while (it.hasNext()) {
            Fig f = (Fig) it.next();
            if (Model.getFacade().isAMessage(f.getOwner())) {
                res++;
            }
        }
        return res;
    }

    /**
     * Method to perform a number of important initializations of a
     * <I>CollaborationDiagram</I>.<p>
     *
     * Each diagram type has a similar <I>UMLxxxDiagram</I> class.<p>
     *
     * Changed <I>lay</I> from <I>LayerPerspective</I> to
     * <I>LayerPerspectiveMutable</I>.  This class is a child of
     * <I>LayerPerspective</I> and was implemented to correct some
     * difficulties in changing the model.  <I>Lay</I> is used mainly
     * in <I>LayerManager</I>(GEF) to control the adding, changing and
     * deleting layers on the diagram...
     *
     * @param handle  MNamespace from the model in NSUML...
     * @author psager@tigris.org Jan. 24, 2002
     */
    public void setNamespace(Object handle) {
        if (!Model.getFacade().isANamespace(handle)) {
            LOG.error(
                "Illegal argument. Object " + handle + " is not a namespace");
            throw new IllegalArgumentException(
                "Illegal argument. Object " + handle + " is not a namespace");
        }
        super.setNamespace(handle);
        CollabDiagramGraphModel gm = new CollabDiagramGraphModel();
        gm.setCollaboration(collaboration);
        LayerPerspective lay =
            new LayerPerspectiveMutable(Model.getFacade().getName(handle), gm);
        CollabDiagramRenderer rend = new CollabDiagramRenderer(); // singleton
        lay.setGraphNodeRenderer(rend);
        lay.setGraphEdgeRenderer(rend);
        setLayer(lay);

    }

    /**
     * Called by the PGML parser to initialize the 
     * diagram. First the parser creates a diagram via the
     * default constructor. Then this method is called.
     *
     * @see org.tigris.gef.base.Diagram#initialize(Object)
     */
    public void initialize(Object owner) {
        collaboration = owner;
        Object ns = 
            Model.getFacade().getRepresentedClassifier(collaboration);
        if (ns == null)
            ns = Model.getFacade().getRepresentedOperation(collaboration);
        setNamespace(ns);
        
        super.initialize(owner);
    }

    
    /**
     * Get the actions from which to create a toolbar or equivalent
     * graphic triggers.
     *
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getUmlActions()
     */
    protected Object[] getUmlActions() {
        Object[] actions = {
	    getActionClassifierRole(),
	    null,
	    getAssociationActions(),
	    getActionGeneralize(),
	    getActionDepend(),
            null,
            getActionMessage(), //this one behaves differently, hence seperated!
        };
        return actions;
    }

    private Object[] getAssociationActions() {
        Object[][] actions = {
	    {getActionAssociation(), getActionUniAssociation() },
	    {getActionAggregation(), getActionUniAggregation() },
	    {getActionComposition(), getActionUniComposition() },
        };

        return actions;
    }

    /**
     * After loading the diagram it is necessary to connect
     * every FigMessage to its FigAssociationRole.
     * This is done by adding the FigMessage
     * to the PathItems of its FigAssociationRole.
     */
    public void postLoad() {

        super.postLoad();

        Collection messages;
        Iterator msgIterator;
        if (getNamespace() == null) {
            LOG.error("Collaboration Diagram does not belong to a namespace");
            return;
        }
        Collection ownedElements = 
            Model.getFacade().getOwnedElements(getNamespace());
        Iterator oeIterator = ownedElements.iterator();
        Layer lay = getLayer();
        while (oeIterator.hasNext()) {
            Object me = /*(MModelElement)*/
		oeIterator.next();
            if (Model.getFacade().isAAssociationRole(me)) {
                messages = Model.getFacade().getMessages(me);
                msgIterator = messages.iterator();
                while (msgIterator.hasNext()) {
                    Object message = /*(MMessage)*/
			msgIterator.next();
                    FigMessage figMessage =
                        (FigMessage) lay.presentationFor(message);
                    if (figMessage != null) {
                        figMessage.addPathItemToFigAssociationRole(lay);
                    }
                }
            }
        }
    }

    /**
     * Creates a new diagramname.
     * @return String
     */
    protected String getNewDiagramName() {
        String name = "Collaboration Diagram " + getNextDiagramSerial();
        if (!ProjectManager.getManager().getCurrentProject()
	        .isValidDiagramName(name)) {
            name = getNewDiagramName();
        }
        return name;
    }

    /**
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getLabelName()
     */
    public String getLabelName() {
        return Translator.localize("label.collaboration-diagram");
    }

    /**
     * @return Returns the actionClassifierRole.
     */
    private Action getActionClassifierRole() {
        if (actionClassifierRole == null) {
            actionClassifierRole =
                new RadioAction(new CmdCreateNode(
                        Model.getMetaTypes().getClassifierRole(),
                        "ClassifierRole"));
        }
        return actionClassifierRole;
    }

    /**
     * @return Returns the actionAssociation.
     */
    protected Action getActionAssociation() {
        if (actionAssociation == null) {
            actionAssociation = new RadioAction(
                    new ActionAddAssociationRole(
                        Model.getAggregationKind().getNone(),
                        false,
                        "Association"));
        }
        return actionAssociation;
    }
    /**
     * @return Returns the actionComposition.
     */
    protected Action getActionComposition() {
        if (actionComposition == null) {
            actionComposition = new RadioAction(
                    new ActionAddAssociationRole(
                        Model.getAggregationKind().getComposite(),
                        false,
                        "Composition"));
        }
        return actionComposition;
    }
    /**
     * @return Returns the actionDepend.
     */
    protected Action getActionDepend() {
        if (actionDepend == null) {
            actionDepend = new RadioAction(
                    new CmdSetMode(
                        ModeCreatePolyEdge.class,
                        "edgeClass",
                        Model.getMetaTypes().getDependency(),
                        "Dependency"));
        }
        return actionDepend;
    }
    /**
     * @return Returns the actionGeneralize.
     */
    protected Action getActionGeneralize() {
        if (actionGeneralize == null) {
            actionGeneralize = new RadioAction(
                    new CmdSetMode(
                        ModeCreatePolyEdge.class,
                        "edgeClass",
                        Model.getMetaTypes().getGeneralization(),
                        "Generalization"));
        }
        return actionGeneralize;
    }

    /**
     * @return Returns the actionUniAggregation.
     */
    protected Action getActionUniAggregation() {
        if (actionUniAggregation == null) {
            actionUniAggregation = new RadioAction(
                    new ActionAddAssociationRole(
                        Model.getAggregationKind().getAggregate(),
                        true,
                        "UniAggregation"));
        }
        return actionUniAggregation;
    }
    /**
     * @return Returns the actionUniAssociation.
     */
    protected Action getActionUniAssociation() {
        if (actionUniAssociation  == null) {
            actionUniAssociation = new RadioAction(
                    new ActionAddAssociationRole(
                        Model.getAggregationKind().getNone(),
                        true,
                        "UniAssociation"));
        }
        return actionUniAssociation;
    }
    /**
     * @return Returns the actionUniComposition.
     */
    protected Action getActionUniComposition() {
        if (actionUniComposition == null) {
            actionUniComposition = new RadioAction(
                    new ActionAddAssociationRole(
                        Model.getAggregationKind().getComposite(),
                        true,
                        "UniComposition"));
        }
        return actionUniComposition;
    }

    /**
     * @return Returns the actionAggregation.
     */
    private Action getActionAggregation() {
        if (actionAggregation == null) {
            actionAggregation = new RadioAction(
                    new ActionAddAssociationRole(
                        Model.getAggregationKind().getAggregate(),
                        false,
                        "Aggregation"));
        }
        return actionAggregation;
    }

    /**
     * @return Returns the actionMessage.
     */
    private Action getActionMessage() {
        if (actionMessage == null) {
            actionMessage = ActionAddMessage.getSingleton();
        }
        return actionMessage;
    }
} /* end class UMLCollaborationDiagram */
