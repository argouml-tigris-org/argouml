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
import org.argouml.kernel.Project;
import org.argouml.model.CoreHelper;
import org.argouml.model.DeleteInstanceEvent;
import org.argouml.model.Model;
import org.argouml.model.ModelManagementHelper;
import org.argouml.uml.diagram.activity.ui.FigPool;
import org.argouml.uml.diagram.static_structure.ui.FigComment;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.util.EnumerationIterator;
import org.argouml.util.IItemUID;
import org.argouml.util.ItemUID;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Editor;
import org.tigris.gef.graph.MutableGraphSupport;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.undo.UndoManager;

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
public abstract class ArgoDiagramImpl extends Diagram 
    implements PropertyChangeListener, ArgoDiagram, IItemUID {

    private ItemUID id;

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(ArgoDiagramImpl.class);

    /**
     * The constructor.
     */
    public ArgoDiagramImpl() {
        super();
        if (!(UndoManager.getInstance() instanceof DiagramUndoManager)) {
            UndoManager.setInstance(new DiagramUndoManager());
            LOG.info("Setting Diagram undo manager");
        } else {
            LOG.info("Diagram undo manager already set");
        }
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
    public ArgoDiagramImpl(String diagramName) {
        // next line patch to issue 596 (hopefully)
        super(diagramName);
        if (!(UndoManager.getInstance() instanceof DiagramUndoManager)) {
            UndoManager.setInstance(new DiagramUndoManager());
            LOG.info("Setting Diagram undo manager");
        } else {
            LOG.info("Diagram undo manager already set");
        }
        try {
            setName(diagramName);
        } catch (PropertyVetoException pve) { }
    }



    public void setName(String n) throws PropertyVetoException {
        super.setName(n);
        MutableGraphSupport.enableSaveAction();
    }


    public void setItemUID(ItemUID i) {
        id = i;
    }


    public ItemUID getItemUID() {
        return id;
    }


    /**
     * The UID.
     */
    static final long serialVersionUID = -401219134410459387L;


    public String getVetoMessage(String propertyName) {
    	if (propertyName.equals("name")) {
	    return "Name of diagram may not exist already";
    	}
        return null;
    }


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


    public void damage() {
        if (getLayer() != null && getLayer().getEditors() != null) {
            Iterator it = getLayer().getEditors().iterator();
            while (it.hasNext()) {
                ((Editor) it.next()).damageAll();
            }
        }
    }


    public List getEdges() {
        if (getGraphModel() != null) {
            return getGraphModel().getEdges();
        }
        return super.getEdges();
    }


    public List getNodes() {
        if (getGraphModel() != null) {
            return getGraphModel().getNodes();
        }
        return super.getNodes();
    }


    public String toString() {
        return "Diagram: " + getName();
    }


    public String repair() {
        StringBuffer report = new StringBuffer(500);

        boolean faultFixed;
        do {
            faultFixed = false;
            List<Fig> figs = new ArrayList<Fig>(getLayer().getContentsNoEdges());
            for (Fig f : figs) {
                if (repairFig(f, report)) {
                    faultFixed = true;
                }
            }
            figs = new ArrayList<Fig>(getLayer().getContentsEdgesOnly());
            for (Fig f : figs) {
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


    public List presentationsFor(Object obj) {
        List<Fig> presentations = new ArrayList<Fig>();
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
        List<Fig> contents = new ArrayList<Fig>(getLayer().getContents());
        int size = contents.size();
        for (int i = 0; i < size; ++i) {
            Fig f = contents.get(i);
            f.removeFromDiagram();
        }
        firePropertyChange("remove", null, null);
        super.remove();
    }
    

    public void setProject(Project p) {
	project = p;
    }
    
    protected Project getProject() {
	return project;
    }
    
    public abstract void encloserChanged(
            FigNode enclosed, FigNode oldEncloser, FigNode newEncloser); 
	// Do nothing, override in subclass.


    public Object getDependentElement() {
        return null;
    }


    public Object getNamespace() {
        return namespace;
    }


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


    public void propertyChange(PropertyChangeEvent evt) {
        if ((evt.getSource() == namespace)
                && (evt instanceof DeleteInstanceEvent)
                && "remove".equals(evt.getPropertyName())) {

            Model.getPump().removeModelEventListener(this, namespace, "remove");

            getProject().moveToTrash(this);
        }
    }


    public Object getOwner() {
        return getNamespace();
    }

    public Iterator<Fig> getFigIterator() {
        return new EnumerationIterator(elements());
    }
    
}
