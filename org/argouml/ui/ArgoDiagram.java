// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.ui;

import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.argouml.cognitive.ItemUID;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.persistence.PgmlUtility;
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
 * It is based upon the GEF Diagram.
 */
public class ArgoDiagram extends Diagram {

    private ItemUID id;

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(ArgoDiagram.class);

    static {
        /**
         * Hack to use vetocheck in constructing names.
         *
         * TODO: Is this needed?
         */
        new ArgoDiagram();
    }

    /**
     * The constructor.
     */
    public ArgoDiagram() {
        super();
        // really dirty hack to remove unwanted listeners
        getLayer().getGraphModel().removeGraphEventListener(getLayer());
    }

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

    /**
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
        if (fig == null && Model.getFacade().isAModelElement(obj)) {
	    // maybe we have a modelelement that is part of some other
            // fig
            if (Model.getFacade().isAOperation(obj)
		|| Model.getFacade().isAAttribute(obj)) {

                // get all the classes from the diagram
                return presentationFor(Model.getFacade().getOwner(obj));
            }
        }
        return fig;
    }

    /**
     * @see org.tigris.gef.base.Diagram#initialize(Object)
     */
    public void initialize(Object owner) {
        super.initialize(owner);
        ProjectManager.getManager().getCurrentProject().setActiveDiagram(this);
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

    /**
     * Get all the model elements in this diagram that are represented
     * by a FigEdge.
     * @see Diagram#getEdges()
     */
    public List getEdges() {
        if (getGraphModel() != null) {
            return getGraphModel().getEdges();
        }
        return super.getEdges();
    }

    /**
     * @see Diagram#getEdges(Collection)
     * TODO: This method can be deleted after GEF 0.11.3M6
     */
    public Collection getEdges(Collection c) {
        if (getGraphModel() != null) {
            return getGraphModel().getEdges();
        }
        return getEdges();
    }

    /**
     * Get all the model elements in this diagram that are represented
     * by a FigNode.
     * @see Diagram#getNodes()
     */
    public List getNodes() {
        if (getGraphModel() != null) {
            return getGraphModel().getNodes();
        }
        return super.getNodes();
    }

    /**
     * @see Diagram#getEdges(Collection)
     * TODO: This method can be deleted after GEF 0.11.3M6
     */
    public Collection getNodes(Collection c) {
        if (getGraphModel() != null) {
            return getGraphModel().getNodes();
        }
        return getNodes();
    }

    /**
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
     */
    public String repair() {
        String report = "";

        List figs = new ArrayList(PgmlUtility.getContents(this));
        for (Iterator i = figs.iterator(); i.hasNext();) {
            String figDescription = null;

            Fig f = (Fig) i.next();
            LOG.info("Checking " + figDescription(f) + f.getOwner());

            // 1. Make sure all Figs in the Diagrams layer refer back to
            // that layer.
            if (!getLayer().equals(f.getLayer())) {
                if (figDescription == null) {
                    figDescription = figDescription(f);
                    report += figDescription;
                }

                // The report
                if (f.getLayer() == null) {
                    report += "-- Fixed: layer was null\n";
                } else {
                    report += "-- Fixed: refered to wrong layer\n";
                }
                // The fix
                f.setLayer(getLayer());
            }

            // 2. Make sure that all Figs are visible
            if (!f.isVisible()) {
                if (figDescription == null) {
                    figDescription = figDescription(f);
                    report += figDescription;
                }
                // The report
                report += "-- Fixed: a Fig must be visible\n";
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
                        report += figDescription;
                    }
                    report +=
                        "-- Removed: as it has no dest Fig\n";
                    f.removeFromDiagram();
                } else if (sourceFig == null) {
                    if (figDescription == null) {
                        figDescription = figDescription(f);
                        report += figDescription;
                    }
                    report +=
                        "-- Removed: as it has no source Fig\n";
                    f.removeFromDiagram();
                } else if (sourceFig.getOwner() == null) {
                    if (figDescription == null) {
                        figDescription = figDescription(f);
                        report += figDescription;
                    }
                    report +=
                        "-- Removed: as its source Fig has no owner\n";
                    f.removeFromDiagram();
                } else if (destFig.getOwner() == null) {
                    if (figDescription == null) {
                        figDescription = figDescription(f);
                        report += figDescription;
                    }
                    report +=
                        "-- Removed: as its destination Fig has no owner\n";
                    f.removeFromDiagram();
                } else if (Model.getUmlFactory().isRemoved(
                        sourceFig.getOwner())) {
                    if (figDescription == null) {
                        figDescription = figDescription(f);
                        report += figDescription;
                    }
                    report +=
                        "-- Removed: as its source Figs owner is no "
                        + "longer in the repository\n";
                    f.removeFromDiagram();
                } else if (Model.getUmlFactory().isRemoved(
                        destFig.getOwner())) {
                    if (figDescription == null) {
                        figDescription = figDescription(f);
                        report += figDescription;
                    }
                    report +=
                        "-- Removed: as its destination Figs owner "
                        + "is no longer in the repository\n";
                    f.removeFromDiagram();
                }
            } else if ((f instanceof FigNode || f instanceof FigEdge)
                    && f.getOwner() == null) {
                if (figDescription == null) {
                    figDescription = figDescription(f);
                    report += figDescription;
                }
                // 4. Make sure all FigNodes and FigEdges have an owner
                // The report
                report +=
                    "-- Removed: owner was null\n";
                // The fix
                f.removeFromDiagram();
            } else if ((f instanceof FigNode || f instanceof FigEdge)
                    &&  Model.getFacade().isAModelElement(f.getOwner())
                    &&  Model.getUmlFactory().isRemoved(f.getOwner())) {
                if (figDescription == null) {
                    figDescription = figDescription(f);
                    report += figDescription;
                }
                // 5. Make sure all FigNodes and FigEdges have a valid owner
                // The report
                report +=
                    "-- Removed: model element no longer in the repository\n";
                // The fix
                f.removeFromDiagram();
            } else if (f instanceof FigGroup && !(f instanceof FigNode)) {
                if (figDescription == null) {
                    figDescription = figDescription(f);
                    report += figDescription;
                }
                // 4. Make sure the only FigGroups on a diagram are also
                //    FigNodes
                // The report
                report +=
                    "-- Removed: a FigGroup should not be on the diagram\n";
                // The fix
                f.removeFromDiagram();
            }
        }

        return report;
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

    /**
     * Find the all Figs that visualise the given model element in
     * this layer, or null if there is none.
     * TODO: once GEF includes this same method in Diagram then the can go
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
} /* end class ArgoDiagram */
