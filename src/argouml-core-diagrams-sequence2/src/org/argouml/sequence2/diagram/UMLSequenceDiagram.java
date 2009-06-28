// $Id$
// Copyright (c) 2007-2009 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.sequence2.diagram;

import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.static_structure.ui.FigComment;
import org.argouml.uml.diagram.ui.ActionSetMode;
import org.argouml.uml.diagram.ui.RadioAction;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;
import org.tigris.gef.base.ModePlace;
import org.tigris.gef.graph.GraphFactory;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;

/**
 * The diagram for sequence diagrams.
 *
 * @author penyaskito
 */
public class UMLSequenceDiagram extends UMLDiagram {
    
    private Object[] actions;

    private static final Logger LOG = Logger
        .getLogger(UMLSequenceDiagram.class);
    
    /**
     * TODO: Document!
     * 
     * @deprecated for 0.28 by tfmorris.  Use 
     * {@link #UMLActivityDiagram(String, Object, GraphModel)}.
     */
    @Deprecated
    public UMLSequenceDiagram() {
        super();
        // Create the graph model
        MutableGraphModel gm = new SequenceDiagramGraphModel(); 
        setGraphModel(gm);
        
        // Create the layer
        LayerPerspective lay = new
            LayerPerspectiveMutable(this.getName(), gm);
        setLayer(lay);
        
        // Create the renderer
        SequenceDiagramRenderer renderer = new SequenceDiagramRenderer();
        lay.setGraphNodeRenderer(renderer);
        lay.setGraphEdgeRenderer(renderer);
        
        LOG.debug("Created sequence diagram");
    }
    
    /**
     * Creates a new UmlSequenceDiagram with a collaboration.
     * @param collaboration The collaboration
     * 
     */
    public UMLSequenceDiagram(Object collaboration) {
        this();
        try {
            this.setName(getNewDiagramName());
        } catch (PropertyVetoException e) {
            LOG.error("Exception", e);
        }
        ((SequenceDiagramGraphModel) getGraphModel()).
            setCollaboration(collaboration);
        setNamespace(collaboration);
    }
    
    
    /**
     * Method called by PGML parser during diagram load to initialize a diagram.
     * We are passed the owner of that diagram which is the collaboration.
     * @param owner UML model element representing the collaboration
     * @see org.tigris.gef.base.Diagram#initialize(java.lang.Object)
     */
    @Override
    public void initialize(Object owner) {
        super.initialize(owner);
        SequenceDiagramGraphModel gm =
            (SequenceDiagramGraphModel) getGraphModel();
        gm.setCollaboration(owner);
    }

    
    
    
    /**
     * Get the Uml actions that can be performed in the diagram 
     * @return An array with the Uml actions 
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getUmlActions()
     */
    @Override
    protected Object[] getUmlActions() {
        if (actions == null) {
            actions = new Object[8];
            int i = 0;
            actions[i++] = new RadioAction(new ActionAddClassifierRole());
            actions[i++] = new RadioAction(new ActionSetAddMessageMode(
                    Model.getMetaTypes().getCallAction(),
                    "button.new-callaction"));
            actions[i++] = new RadioAction(new ActionSetAddMessageMode(
                    Model.getMetaTypes().getSendAction(),
                    "button.new-sendaction"));
            actions[i++] = new RadioAction(new ActionSetAddMessageMode(
                    Model.getMetaTypes().getReturnAction(),
                    "button.new-returnaction"));
            actions[i++] = new RadioAction(new ActionSetAddMessageMode(
                    Model.getMetaTypes().getCreateAction(),
                    "button.new-createaction"));
            actions[i++] = new RadioAction(new ActionSetAddMessageMode(
                    Model.getMetaTypes().getDestroyAction(),
                    "button.new-destroyaction"));
            actions[i++] = new RadioAction(new ActionSetMode(
                    ModeBroomMessages.class,
                    "button.broom-messages"));
        }
        return actions;
    }
    
    /**
     * Get the localized label name for the diagram
     * @return The localized label name for the diagram
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getLabelName()
     */
    @Override
    public String getLabelName() {
        return Translator.localize("label.sequence-diagram");
    }
    
    @Override
    public void encloserChanged(FigNode enclosed, FigNode oldEncloser,
            FigNode newEncloser) {
    	// Do nothing.        
    }
    
    @Override
    public boolean isRelocationAllowed(Object base)  {
    	return Model.getFacade().isACollaboration(base);
    }

    @SuppressWarnings("unchecked")
    public Collection getRelocationCandidates(Object root) {
        return 
        Model.getModelManagementHelper().getAllModelElementsOfKindWithModel(
            root, Model.getMetaTypes().getCollaboration());
    }

    @Override
    public boolean relocate(Object base) {
        ((SequenceDiagramGraphModel) getGraphModel())
	    	.setCollaboration(base);
        setNamespace(base);
        damage();
        return true;
    }
    
    /**
     * A sequence diagram can accept all classifiers. It will add them as a new 
     * Classifier Role with that classifier as a base.
     * @param objectToAccept element to test for acceptability
     * @return true if the element is acceptable
     * @see org.argouml.uml.diagram.ui.UMLDiagram#doesAccept(java.lang.Object)
     */
    @Override
    public boolean doesAccept(Object objectToAccept) {
        if (Model.getFacade().isAClassifier(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isAComment(objectToAccept)) {
            return true;
        }
        return false;
    }
    
    /**
     * Creates a new Classifier Role with a specified base.
     * @param base
     * @return The new CR
     */
    private Object makeNewCR(Object base) {
        Object node = null;
        Editor ce = Globals.curEditor();
        GraphModel gm = ce.getGraphModel();
        if (gm instanceof SequenceDiagramGraphModel) {
            Object collaboration =
                ((SequenceDiagramGraphModel) gm).getCollaboration();
            node =
                Model.getCollaborationsFactory().buildClassifierRole(
                        collaboration);
        }
        Model.getCollaborationsHelper().addBase(node, base);
        
        return node;
    }
    
    /**
     * Creates the Fig for the CR. Y position will be adjusted to match other 
     * the other CRs.
     * @param classifierRole
     * @param location The position where to put the new fig.
     * @return
     */
    private FigClassifierRole makeNewFigCR(Object classifierRole, 
            Point location) {
        if (classifierRole != null) {
            Rectangle bounds = new Rectangle();
            
            // Y position of the new CR should match existing CRs Y position
            for (Fig fig : (List<Fig>) getLayer().getContentsNoEdges()) {
                if (fig instanceof FigClassifierRole) {
                    bounds.y = fig.getY();
                    bounds.height = fig.getHeight();
                    break;
                }
            }
            if (location != null) {
                if (bounds.y == 0) {
                    bounds.y = location.y;
                }
                bounds.x = location.x;
            }

            FigClassifierRole newCR = new FigClassifierRole(classifierRole,
                    bounds, getDiagramSettings());
            getGraphModel().getNodes().add(newCR.getOwner());
            
            return newCR;
        }
        return null;
    }
    
    @Override
    public FigNode drop(Object droppedObject, Point location) {
        FigNode figNode = null;
        

        // If location is non-null, convert to a rectangle that we can use
        Rectangle bounds = null;
        if (location != null) {
            bounds = new Rectangle(location.x, location.y, 0, 0);
        }
        DiagramSettings settings = getDiagramSettings();
        
        if (Model.getFacade().isAComment(droppedObject)) {
            figNode = new FigComment(droppedObject, bounds, settings);
        } else if (Model.getFacade().isAClassifierRole(droppedObject)) {
            if (!getGraphModel().getNodes().contains(droppedObject)) {
                figNode = makeNewFigCR(droppedObject, location);  
            }
        } else if (Model.getFacade().isAClassifier(droppedObject)) {
            figNode = makeNewFigCR(makeNewCR(droppedObject), location);
        }
        
        if (figNode != null) {
            LOG.debug("Dropped object " + droppedObject + " converted to " 
                    + figNode);
        } else {
            LOG.debug("Dropped object NOT added " + droppedObject);
        }
        return figNode;
    }
    
    @Override
    public String getInstructions(Object droppedObject) {
    	if (Model.getFacade().isAClassifierRole(droppedObject)) {
    	    return super.getInstructions(droppedObject);
    	} else if (Model.getFacade().isAClassifier(droppedObject)) {
            return Translator.localize(
                    "misc.message.click-on-diagram-to-add-as-cr",
                    new Object[] {Model.getFacade().toString(droppedObject)});
        }
        return super.getInstructions(droppedObject);
    }
    
    @Override
    public ModePlace getModePlace(GraphFactory gf, String instructions) {
        return new ModePlaceClassifierRole(gf, instructions);
    }
}
