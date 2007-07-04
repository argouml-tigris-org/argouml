// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.uml.diagram;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.argouml.cognitive.ItemUID;
import org.argouml.kernel.Project;
import org.argouml.model.CoreHelper;
import org.argouml.model.DeleteInstanceEvent;
import org.argouml.model.Model;
import org.argouml.model.ModelManagementHelper;
import org.argouml.uml.diagram.activity.ui.FigPool;
import org.argouml.uml.diagram.static_structure.ui.FigComment;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Editor;
import org.tigris.gef.graph.MutableGraphSupport;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigNode;

/**
 * This class represents all Diagrams within ArgoUML.
 * It is based upon the GEF Diagram.<p>
 * 
 * It adds a namespace, and the capability 
 * to delete itself when its namespace is deleted. <p>
 * 
 * TODO: MVW: I am not sure of the following:<p>
 * The "namespace" of the diagram is e.g. used when creating new elements
 * that are shown on the diagram; they will have their namespace set
 * according this. It is NOT necessarily equal to the "owner". <p>
 *
 * MVW: I doubt all following:
 * The "namespace" of the diagram is e.g. used to register a listener
 * to the UML model, to be notified if this element is removed;
 * which will imply that this diagram has to be deleted, too. <p>
 * 
 * Hence the namespace of e.g. a collaboration diagram should be the
 * represented classifier or, in case of a represented operation, the
 * classifier that owns this operation.
 * And the namespace of the statechart diagram should be 
 * the namespace of its statemachine.
 */
public abstract class ArgoDiagram extends Diagram 
    implements PropertyChangeListener {

    private ItemUID id;

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(ArgoDiagram.class);

    /**
     * The constructor.
     */
    public ArgoDiagram() {
        super();
        // really dirty hack to remove unwanted listeners
        getLayer().getGraphModel().removeGraphEventListener(getLayer());
    }
    
    /**
     * The project this diagram is contained in.
     */
    private Project project;

    protected Object namespace;

    /**
     * The constructor.
     *
     * @param diagramName the name of the diagram
     */
    public ArgoDiagram(String diagramName) {
        // next line patch to issue 596 (hopefully)
        super(diagramName);
        try {
            setName(diagramName);
        } catch (PropertyVetoException pve) { }
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /*
     * @see org.tigris.gef.base.Diagram#setName(java.lang.String)
     */
    public void setName(String n) throws PropertyVetoException {
        super.setName(n);
        MutableGraphSupport.enableSaveAction();
    }

    /**
     * @param i the new id
     */
    public void setItemUID(ItemUID i) {
        id = i;
    }

    /**
     * USED BY pgml.tee!!
     * @return the item UID
     */
    public ItemUID getItemUID() {
        return id;
    }

    /** The bean property name denoting the diagram's namespace. 
     * Value is a String. */
    public static final String NAMESPACE_KEY = "namespace";

    ////////////////////////////////////////////////////////////////
    // event management
    /**
     * The UID.
     */
    static final long serialVersionUID = -401219134410459387L;

    /**
     * TODO: The reference to the method
     * org.argouml.uml.ui.VetoablePropertyChange#getVetoMessage(String)
     * was here but the class does exist anymore. Where is it?
     * This method is never used!
     *
     * @param propertyName is the name of the property
     * @return a message or null if not applicable.
     */
    public String getVetoMessage(String propertyName) {
    	if (propertyName.equals("name")) {
	    return "Name of diagram may not exist already";
    	}
        return null;
    }

    /**
     * Finds the presentation (the Fig) for some object. If the object
     * is a modelelement that is contained in some other modelelement
     * that has its own fig, that fig is returned. It extends
     * presentationFor that only gets the fig belonging to the node
     * obj.<p>
     *
     * @author jaap.branderhorst@xs4all.nl
     * @return the Fig for the object
     * @param obj is th object
     */
    public Fig getContainingFig(Object obj) {
        Fig fig = super.presentationFor(obj);
        if (fig == null && Model.getFacade().isAUMLElement(obj)) {
	    // maybe we have a modelelement that is part of some other
            // fig
            if (Model.getFacade().isAOperation(obj)
                    || Model.getFacade().isAReception(obj)
                    || Model.getFacade().isAAttribute(obj)) {

                // get all the classes from the diagram
                return presentationFor(Model.getFacade().getOwner(obj));
            }
        }
        return fig;
    }

    /**
     * This will mark the entire visible area of all Editors to be repaired
     *  from any damage - i.e. repainted.
     */
    public void damage() {
        if (getLayer() != null && getLayer().getEditors() != null) {
            Iterator it = getLayer().getEditors().iterator();
            while (it.hasNext()) {
                ((Editor) it.next()).damageAll();
            }
        }
    }

    /*
     * Get all the model elements in this diagram that are represented
     * by a FigEdge.
     * @see org.tigris.gef.base.Diagram#getEdges()
     */
    public List getEdges() {
        if (getGraphModel() != null) {
            return getGraphModel().getEdges();
        }
        return super.getEdges();
    }


    /*
     * Get all the model elements in this diagram that are represented
     * by a FigNode.
     * @see org.tigris.gef.base.Diagram#getNodes()
     */
    public List getNodes() {
        if (getGraphModel() != null) {
            return getGraphModel().getNodes();
        }
        return super.getNodes();
    }


    /*
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "Diagram: " + getName();
    }

    /**
     * We hang our heads in shame. There are still bugs in ArgoUML
     * and/or GEF that cause corruptions in the model.
     * Before a save takes place we repair the model in order to
     * be as certain as possible that the saved file will reload.
     * TODO: Split into small inner classes for each fix.
     *
     * @return A text that explains what is repaired.
     */
    public String repair() {
        StringBuffer report = new StringBuffer(500);

        boolean faultFixed;
        do {
            faultFixed = false;
            List figs = new ArrayList(getLayer().getContentsNoEdges());
            for (Iterator i = figs.iterator(); i.hasNext();) {
                Fig f = (Fig) i.next();
                if (repairFig(f, report)) {
                    faultFixed = true;
                }
            }
            figs = new ArrayList(getLayer().getContentsEdgesOnly());
            for (Iterator i = figs.iterator(); i.hasNext();) {
                Fig f = (Fig) i.next();
                if (repairFig(f, report)) {
                    faultFixed = true;
                }
            }
        } while (faultFixed); // Repeat until no faults are fixed

        return report.toString();
    }
    
    private boolean repairFig(Fig f, StringBuffer report) {
        LOG.info("Checking " + figDescription(f) + f.getOwner());
        boolean faultFixed = false;
        String figDescription = null;
        
        // 1. Make sure all Figs in the Diagrams layer refer back to
        // that layer.
        if (!getLayer().equals(f.getLayer())) {
            if (figDescription == null) {
                figDescription = figDescription(f);
                report.append(figDescription);
            }

            // The report
            if (f.getLayer() == null) {
                report.append("-- Fixed: layer was null\n");
            } else {
                report.append("-- Fixed: refered to wrong layer\n");
            }
            faultFixed = true;
            // The fix
            f.setLayer(getLayer());
        }

        // 2. Make sure that all Figs are visible
        if (!f.isVisible()) {
            if (figDescription == null) {
                figDescription = figDescription(f);
                report.append(figDescription);
            }
            // The report
            report.append("-- Fixed: a Fig must be visible\n");
            faultFixed = true;
            // The fix
            f.setVisible(true);
        }

        if (f instanceof FigEdge) {
            // 3. Make sure all FigEdges are attached to a valid FigNode
            // The report
            FigEdge fe = (FigEdge) f;
            FigNode destFig = fe.getDestFigNode();
            FigNode sourceFig = fe.getSourceFigNode();

            if (destFig == null) {
                if (figDescription == null) {
                    figDescription = figDescription(f);
                    report.append(figDescription);
                }
                faultFixed = true;
                report.append("-- Removed: as it has no dest Fig\n");
                f.removeFromDiagram();
            } else if (sourceFig == null) {
                if (figDescription == null) {
                    figDescription = figDescription(f);
                    report.append(figDescription);
                }
                faultFixed = true;
                report.append("-- Removed: as it has no source Fig\n");
                f.removeFromDiagram();
            } else if (sourceFig.getOwner() == null) {
                if (figDescription == null) {
                    figDescription = figDescription(f);
                    report.append(figDescription);
                }
                faultFixed = true;
                report.append("-- Removed: as its source Fig has no owner\n");
                f.removeFromDiagram();
            } else if (destFig.getOwner() == null) {
                if (figDescription == null) {
                    figDescription = figDescription(f);
                    report.append(figDescription);
                }
                faultFixed = true;
                report.append(
                        "-- Removed: as its destination Fig has no owner\n");
                f.removeFromDiagram();
            } else if (Model.getUmlFactory().isRemoved(
                    sourceFig.getOwner())) {
                if (figDescription == null) {
                    figDescription = figDescription(f);
                    report.append(figDescription);
                }
                faultFixed = true;
                report.append("-- Removed: as its source Figs owner is no "
                    + "longer in the repository\n");
                f.removeFromDiagram();
            } else if (Model.getUmlFactory().isRemoved(
                    destFig.getOwner())) {
                if (figDescription == null) {
                    figDescription = figDescription(f);
                    report.append(figDescription);
                }
                faultFixed = true;
                report.append("-- Removed: as its destination Figs owner "
                    + "is no longer in the repository\n");
                f.removeFromDiagram();
            }
        } else if ((f instanceof FigNode || f instanceof FigEdge)
                && f.getOwner() == null && !(f instanceof FigPool)) {
            if (figDescription == null) {
                figDescription = figDescription(f);
                report.append(figDescription);
            }
            // 4. Make sure all FigNodes and FigEdges have an owner
            // The report
            faultFixed = true;
            report.append("-- Removed: owner was null\n");
            // The fix
            f.removeFromDiagram();
        } else if ((f instanceof FigNode || f instanceof FigEdge)
                &&  Model.getFacade().isAUMLElement(f.getOwner())
                &&  Model.getUmlFactory().isRemoved(f.getOwner())) {
            if (figDescription == null) {
                figDescription = figDescription(f);
                report.append(figDescription);
            }
            // 5. Make sure all FigNodes and FigEdges have a valid owner
            // The report
            faultFixed = true;
            report.append(
                    "-- Removed: model element no longer in the repository\n");
            // The fix
            f.removeFromDiagram();
        } else if (f instanceof FigGroup && !(f instanceof FigNode)) {
            if (figDescription == null) {
                figDescription = figDescription(f);
                report.append(figDescription);
            }
            // 4. Make sure the only FigGroups on a diagram are also
            //    FigNodes
            // The report
            faultFixed = true;
            report.append(
                    "-- Removed: a FigGroup should not be on the diagram\n");
            // The fix
            f.removeFromDiagram();
        }
        
        return faultFixed;
    }

    /**
     * Generate a description of a Fig that would be most meaningful to a
     * developer and the user.
     * This is used by the repair routines to describe the Fig that was repaired
     * <ul>
     * <li>FigComment - the text within body compartment of the Fig
     * <li>FigNodeModelElement -
     *        the text within the name compartment of the FigNode
     * <li>FigEdgeModelElement -
     *        the text within name compartment of the FigEdge and the
     *        descriptions of the adjoining FigNodes
     * </ul>
     * @param f the Fig to describe
     * @return The description as a String.
     */
    private String figDescription(Fig f) {
        String description = "\n" + f.getClass().getName();
        if (f instanceof FigComment) {
            description += " \"" + ((FigComment) f).getBody() + "\"";
        } else if (f instanceof FigNodeModelElement) {
            description += " \"" + ((FigNodeModelElement) f).getName() + "\"";
        } else if (f instanceof FigEdgeModelElement) {
            FigEdgeModelElement fe = (FigEdgeModelElement) f;
            description += " \"" + fe.getName() + "\"";
            String source;
            if (fe.getSourceFigNode() == null) {
                source = "(null)";
            } else {
                source =
                    ((FigNodeModelElement) fe.getSourceFigNode()).getName();
            }
            String dest;
            if (fe.getDestFigNode() == null) {
                dest = "(null)";
            } else {
                dest = ((FigNodeModelElement) fe.getDestFigNode()).getName();
            }
            description += " [" + source + "=>" + dest + "]";
        }
        return description + "\n";
    }

    /*
     * Find the all Figs that visualise the given model element in
     * this layer, or null if there is none.
     * 
     * TODO: once GEF includes this same method in Diagram then the can go
     * 
     * @see org.tigris.gef.base.Diagram#presentationsFor(java.lang.Object)
     */
    public List presentationsFor(Object obj) {
        List presentations = new ArrayList();
        int figCount = getLayer().getContents().size();
        for (int figIndex = 0; figIndex < figCount; ++figIndex) {
            Fig fig = (Fig) getLayer().getContents().get(figIndex);
            if (fig.getOwner() == obj) {
                presentations.add(fig);
            }
        }

        return presentations;
    }

    // TODO: Move to GEF
    public void remove() {
        List contents = new ArrayList(getLayer().getContents());
        int size = contents.size();
        for (int i = 0; i < size; ++i) {
            Fig f = (Fig) contents.get(i);
            f.removeFromDiagram();
        }
        firePropertyChange("remove", null, null);
        super.remove();
    }
    
    public void setProject(Project p) {
	this.project = p;
    }
    
    protected Project getProject() {
	return project;
    }
    
    /**
     * Called when the user releases a dragged a FigNode.
     * 
     * @param enclosed the enclosed FigNode that was dragged into the encloser
     * @param oldEncloser the previous encloser
     * @param newEncloser the FigNode that encloses the dragged FigNode
     */
    public abstract void encloserChanged(
            FigNode enclosed, FigNode oldEncloser, FigNode newEncloser); 
	// Do nothing, override in subclass.

    /**
     * This method shall return any UML modelelements
     * that should be deleted when the diagram gets deleted,
     * or null if there are none. The default implementation returns null;
     * e.g. a statechart diagram should return its statemachine.
     *
     * @author mvw@tigris.org
     *
     * @return the dependent element - in the general case there aren't, so null
     */
    public Object getDependentElement() {
        return null;
    }

    /**
     * @return the namespace for the diagram
     */
    public Object getNamespace() {
        return namespace;
    }

    /**
     * Sets the namespace of the Diagram, and
     * adds the diagram as a listener of its namespace in the UML model
     * (so that it can delete itself when the model element is deleted).
     *
     * @param ns the namespace for the diagram
     */
    public void setNamespace(Object ns) {
        if (!Model.getFacade().isANamespace(ns)) {
            LOG.error("Not a namespace");
            LOG.error(ns);
            throw new IllegalArgumentException("Given object not a namespace");
        }
        if ((namespace != null) && (namespace != ns)) {
            Model.getPump().removeModelEventListener(this, namespace);
        }
        Object oldNs = namespace;
        namespace = ns;
        firePropertyChange(NAMESPACE_KEY, oldNs, ns);
    
        // Add the diagram as a listener to the namespace so
        // that when the namespace is removed the diagram is deleted also.
        /* Listening only to "remove" events does not work... 
         * TODO: Check if this works now with new event pump - tfm 
         */
        Model.getPump().addModelEventListener(this, namespace, "remove");
    }

    /**
     * Set the namespace of a model element to the owner of
     * the given namespace. If the namespace is null
     * the namespace of the diagram is used instead.
     * If the modelElement is not valid in the given namespace
     * this method takes no action.
     * @param modelElement the model element
     * @param ns the namespace
     */
    public void setModelElementNamespace(Object modelElement, Object ns) {
        if (modelElement == null) {
            return;
        }
        
        // If we're not provided a namespace then get it from the diagram or
        // the root
        if (ns == null) {
            if (getNamespace() != null) {
        	ns = getNamespace();
            } else {
        	ns = getProject().getRoot();
            }
        }
        
        // If we haven't succeeded in getting a namespace then abort
        if (ns == null) {
            return;
        }
        
        // If we're trying to set the namespace to the existing value
        // then don't do any more work.
        if (Model.getFacade().getNamespace(modelElement) == ns) {
            return;
        }
        
        CoreHelper coreHelper = Model.getCoreHelper();
        ModelManagementHelper modelHelper = Model.getModelManagementHelper();
        
        if (!modelHelper.isCyclicOwnership(ns, modelElement)
                && coreHelper.isValidNamespace(modelElement, ns)) {
            
            coreHelper.setModelElementContainer(modelElement, ns);
            /* TODO: move the associations to the correct owner (namespace)
             * i.e. issue 2151
             */
        }
    }

    /**
     * This diagram listens to events from its namespace ModelElement;
     * when the modelelement is removed, we also want to delete this
     * diagram.  <p>
     *
     * There is also a risk that if this diagram was the one shown in
     * the diagram panel, then it will remain after it has been
     * deleted. So we need to deselect this diagram. 
     * There are other things to take care of, so all this is delegated to 
     * {@link org.argouml.kernel.Project#moveToTrash(Object)}.
     *
     * {@inheritDoc}
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if ((evt.getSource() == namespace)
                && (evt instanceof DeleteInstanceEvent)
                && "remove".equals(evt.getPropertyName())) {

            Model.getPump().removeModelEventListener(this, namespace, "remove");

            getProject().moveToTrash(this);
        }
    }

    /**
     * The default implementation for diagrams that
     * have the namespace as their owner.
     *
     * @return the namespace
     */
    public Object getOwner() {
        return getNamespace();
    }

} /* end class ArgoDiagram */
