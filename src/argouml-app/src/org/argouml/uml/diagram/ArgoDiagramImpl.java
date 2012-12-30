/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2008 The Regents of the University of California. All
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
import java.beans.VetoableChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.application.events.ArgoDiagramAppearanceEvent;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoNotationEvent;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.CoreHelper;
import org.argouml.model.DeleteInstanceEvent;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.argouml.model.ModelManagementHelper;
import org.argouml.uml.diagram.activity.ui.FigPool;
import org.argouml.uml.diagram.static_structure.ui.FigComment;
import org.argouml.uml.diagram.ui.ArgoFig;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.util.EnumerationIterator;
import org.argouml.util.IItemUID;
import org.argouml.util.ItemUID;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.graph.GraphModel;
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
    implements PropertyChangeListener, VetoableChangeListener, ArgoDiagram,
    IItemUID {

    private ItemUID id;

    /**
     * The project this diagram is contained in.
     */
    private Project project;

    protected Object namespace;

    private DiagramSettings settings;

    private static final Logger LOG =
        Logger.getLogger(ArgoDiagramImpl.class.getName());

    /**
     * Default constructor.  Used by PGML parser when diagram is first created.
     * @deprecated for 0.27.2 by tfmorris.  The 0-arg constructor of our sub
     * classes will get called by the PGML parser, but this should not get
     * propagated up the hierarchy.  The GEF Diagram constructor implementation
     * is going to provide defaults for all missing args anyway, so we should
     * always use the fully specified 3-arg constructor.
     */
    @Deprecated
    public ArgoDiagramImpl() {
        super();

        // TODO: What is this trying to do? It's never going to get called - tfm
        // really dirty hack to remove unwanted listeners
        getLayer().getGraphModel().removeGraphEventListener(getLayer());

        constructorInit();
    }


    /**
     * The constructor.
     *
     * @param diagramName the name of the diagram
     * @deprecated for 0.27.2 by tfmorris. Use
     * {@link #ArgoDiagramImpl(String, GraphModel, LayerPerspective)}.
     */
    @Deprecated
    public ArgoDiagramImpl(String diagramName) {
        // next line patch to issue 596 (hopefully)
        super(diagramName);
        try {
            setName(diagramName);
        } catch (PropertyVetoException pve) { }
        constructorInit();
    }

    /**
     * Construct a new ArgoUML diagram.  This is the preferred form of the
     * constructor.  If you don't know the name yet, make one up (because that's
     * what the super classes constructors are going to do anyway).
     *
     * @param name the name of the new diagram
     * @param graphModel graph model to associate with diagram
     * @param layer layer to associate with diagram
     * (use new LayerPerspective(name, graphModel)) if you need a default
     */
    public ArgoDiagramImpl(String name, GraphModel graphModel,
            LayerPerspective layer) {
        super(name, graphModel, layer);
        // TODO: Do we really need to do this? Carried over from old behavior
        try {
            setName(name);
        } catch (PropertyVetoException pve) {
        }
        constructorInit();
    }

    /**
     * Finish initialization which is common to multiple constructors which
     * don't invoke each other.
     */
    private void constructorInit() {
        // TODO: These should get replaced immediately by the creating
        // initialization code, but make sure we've got a default just in case.
        Project project = ProjectManager.getManager().getCurrentProject();
        if (project != null) {
            settings = project.getProjectSettings().getDefaultDiagramSettings();
        }
        // TODO: we should be given an Undo manager to use rather than looking
        // for a global one
        if (!(UndoManager.getInstance() instanceof DiagramUndoManager)) {
            UndoManager.setInstance(new DiagramUndoManager());
            LOG.log(Level.INFO, "Setting Diagram undo manager");
        } else {
            LOG.log(Level.INFO, "Diagram undo manager already set");
        }

        // Register for notification of any global changes that would affect
        // our rendering
        ArgoEventPump.addListener(ArgoEventTypes.ANY_NOTATION_EVENT, this);
        ArgoEventPump.addListener(
                ArgoEventTypes.ANY_DIAGRAM_APPEARANCE_EVENT, this);

        // Listen for name changes so we can veto them if we don't like them
        addVetoableChangeListener(this);
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
        if (LOG.isLoggable(Level.INFO)) {
            LOG.log(Level.INFO, "Checking " + figDescription(f) + f.getOwner());
        }
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

    public Project getProject() {
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
            LOG.log(Level.SEVERE, "Not a namespace {0}", ns);
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

            if (getProject() != null) {
                getProject().moveToTrash(this);
            }
        }
    }


    public Object getOwner() {
        return getNamespace();
    }

    public Iterator<Fig> getFigIterator() {
        return new EnumerationIterator(elements());
    }

    public void setDiagramSettings(DiagramSettings newSettings) {
        settings = newSettings;
    }

    public DiagramSettings getDiagramSettings() {
        return settings;
    }

    /**
     * Handles a global change to the diagram font.
     * @param e the event
     * @see org.argouml.application.events.ArgoDiagramAppearanceEventListener#diagramFontChanged(org.argouml.application.events.ArgoDiagramAppearanceEvent)
     */
    public void diagramFontChanged(ArgoDiagramAppearanceEvent e) {
        renderingChanged();
    }

    /**
     * Rerender the entire diagram based on new global rendering settings.
     * <p>
     * NOTE: Figs which define their own presentation listeners will get
     * re-rendered twice
     */
    public void renderingChanged() {
        for (Object fig : getLayer().getContents()) {
            try {
                // This should always be true, but just in case...
                if (fig instanceof ArgoFig) {
                    ((ArgoFig) fig).renderingChanged();
                } else {
                    LOG.log(Level.WARNING, "Diagram " + getName() + " contains non-ArgoFig "
                            + fig);
                }
            } catch (InvalidElementException e) {
                LOG.log(Level.SEVERE, "Tried to refresh deleted element ", e);
            }
        }
        damage();
    }

    public void notationChanged(ArgoNotationEvent e) {
        renderingChanged();
    }

    public void notationAdded(ArgoNotationEvent e) {
        // Do nothing
    }

    public void notationProviderAdded(ArgoNotationEvent e) {
        // Do nothing
    }

    public void notationProviderRemoved(ArgoNotationEvent e) {
        // Do nothing
    }


    public void notationRemoved(ArgoNotationEvent e) {
        // Do nothing
    }


    /**
     * Receive vetoable change event. GEF will call this method with the 'name'
     * property when it attempts to set the name. If this will be a duplicate
     * for the project, we can veto the requested change.
     *
     * @param evt the change event
     * @throws PropertyVetoException if the name is illegal. Usuallly this means
     *             a duplicate in the project.
     * @see java.beans.VetoableChangeListener#vetoableChange(java.beans.PropertyChangeEvent)
     */
    public void vetoableChange(PropertyChangeEvent evt)
        throws PropertyVetoException {

        if ("name".equals(evt.getPropertyName())) {
            if (project != null) {
                if (!project.isValidDiagramName((String) evt.getNewValue())) {
                    throw new PropertyVetoException("Invalid name", evt);
                }
            }
        }
    }

}
