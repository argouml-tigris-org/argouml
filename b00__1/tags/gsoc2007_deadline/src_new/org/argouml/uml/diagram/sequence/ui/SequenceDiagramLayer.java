// $Id:SequenceDiagramLayer.java 12546 2007-05-05 16:54:40Z linus $
// Copyright (c) 2003-2007 The Regents of the University of California. All
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

package org.argouml.uml.diagram.sequence.ui;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;
import org.tigris.gef.base.LayerPerspectiveMutable;
import org.tigris.gef.graph.GraphEvent;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;

/**
 * The layer on which the figs in a sequence diagram are placed. Also
 * responsible for distributing figs on the diagram if a fig is added
 * or removed.
 *
 * @author : jaap.branderhorst@xs4all.nl
 */
public class SequenceDiagramLayer extends LayerPerspectiveMutable {
    
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(SequenceDiagramLayer.class);

    /**
     * The distance between two objects on the sequence diagram.
     */
    public static final int OBJECT_DISTANCE = 30;

    /**
     * The distance between the left side of the diagram and the first
     * {@link FigClassifierRole}.
     */
    public static final int DIAGRAM_LEFT_MARGIN = 50;

    /**
     * The distance between the top side of the diagram and the top of
     * the highest {@link FigClassifierRole}.
     */
    public static final int DIAGRAM_TOP_MARGIN = 50;

    /**
     * The vertical distance between two links.
     */
    public static final int LINK_DISTANCE = 32;

    /**
     * Linked list with all fig objects sorted by x coordinate in it.
     */
    private List figObjectsX = new LinkedList();

    /**
     * The constructor.
     *
     * @param name the name
     * @param gm the graph model
     */
    public SequenceDiagramLayer(String name, MutableGraphModel gm) {
        super(name, gm);

    }

    /*
     * @see org.tigris.gef.base.LayerPerspective#putInPosition(Fig)
     */
    public void putInPosition(Fig f) {
        if (f instanceof FigClassifierRole) {
            distributeFigClassifierRoles((FigClassifierRole) f);
        } else {
            super.putInPosition(f);
	}
    }

    /**
     * Distributes the fig objects contained in figObjectsX over the
     * sequencediagram.
     *
     * @param f
     */
    private void distributeFigClassifierRoles(FigClassifierRole f) {
        reshuffleFigClassifierRolesX(f);
        int listPosition = figObjectsX.indexOf(f);
        Iterator it =
            figObjectsX.subList(listPosition, figObjectsX.size()).iterator();
        int positionX =
            listPosition == 0
                ? DIAGRAM_LEFT_MARGIN
                : (((Fig) figObjectsX.get(listPosition - 1)).getX()
                    + ((Fig) figObjectsX.get(listPosition - 1)).getWidth()
                    + OBJECT_DISTANCE);
        while (it.hasNext()) {
            FigClassifierRole fig = (FigClassifierRole) it.next();
            Rectangle r = fig.getBounds();
            if (r.x < positionX) {
                r.x = positionX;
            }
            r.y = DIAGRAM_TOP_MARGIN;
            fig.setBounds(r);
            fig.updateEdges();
            positionX = (fig.getX() + fig.getWidth() + OBJECT_DISTANCE);
        }
    }

    /*
     * @see org.tigris.gef.graph.GraphListener#nodeAdded(GraphEvent)
     */
    public void nodeAdded(GraphEvent ge) {
        super.nodeAdded(ge);
        Fig fig = presentationFor(ge.getArg());
        if (fig instanceof FigClassifierRole) {
            ((FigClassifierRole) fig).renderingChanged();
        }
    }

    /*
     * @see org.tigris.gef.base.Layer#add(org.tigris.gef.presentation.Fig)
     */
    public void add(Fig f) {
        super.add(f);
        if (f instanceof FigClassifierRole) {
            if (!figObjectsX.isEmpty()) {
                ListIterator it = figObjectsX.listIterator(0);
                while (it.hasNext()) {
                    Fig fig = (Fig) it.next();
                    if (fig.getX() >= f.getX()) {
                        it.previous();
                        it.add(f);
                        break;
                    }
                }
                if (!it.hasNext()) {
                    it.add(f);
                }
            } else {
                figObjectsX.add(f);
            }
            distributeFigClassifierRoles((FigClassifierRole) f);
        }
    }

    /**
     * Return the node index at a certain y point.
     *
     * @param y The point.
     * @return The node index.
     */
    public static int getNodeIndex(int y) {
        y -= DIAGRAM_TOP_MARGIN + FigClassifierRole.MIN_HEAD_HEIGHT;
        if (y < 0) {
            y = 0;
	}
        return y / LINK_DISTANCE;
    }

    /**
     * Makes all the figclassifier roles have uniform size.
     *
     * @return The number of nodes that the FigClassifierRole
     * objects have
     */
    int makeUniformNodeCount() {
        int maxNodes = -1;
        for (Iterator it = figObjectsX.iterator(); it.hasNext();) {
            Object o = it.next();
            if (o instanceof FigClassifierRole) {
                int nodeCount = ((FigClassifierRole) o).getNodeCount();
                if (nodeCount > maxNodes) {
                    maxNodes = nodeCount;
		}
            }
        }
        for (Iterator it = figObjectsX.iterator(); it.hasNext();) {
            Object o = it.next();
            if (o instanceof FigClassifierRole) {
                ((FigClassifierRole) o).growToSize(maxNodes);
            }
        }

        return maxNodes;
    }

    public void contractDiagram(int startNodeIndex, int numberOfNodes) {
        if (makeUniformNodeCount() <= startNodeIndex) {
            return;
	}
        boolean[] emptyArray = new boolean[numberOfNodes];
        java.util.Arrays.fill(emptyArray, true);
        for (Iterator it = figObjectsX.iterator(); it.hasNext();) {
            ((FigClassifierRole) it.next())
		.updateEmptyNodeArray(startNodeIndex, emptyArray);
        }
        for (Iterator it = figObjectsX.iterator(); it.hasNext();) {
            ((FigClassifierRole) it.next())
		.contractNodes(startNodeIndex, emptyArray);
        }
        updateActivations();
    }

    public void expandDiagram(int startNodeIndex, int numberOfNodes) {
        if (makeUniformNodeCount() <= startNodeIndex) {
            return;
	}
        for (Iterator it = figObjectsX.iterator(); it.hasNext();) {
            ((FigClassifierRole) it.next())
		.grow(startNodeIndex, numberOfNodes);
        }
        updateActivations();
    }

    /**
     * Add nodes to.
     *
     * TODO: Linus doesn't understand this comment. Please elaborate!
     * Bob doesn't either. What hope do we have.
     */
    private void reshuffleFigClassifierRolesX(Fig f) {
        figObjectsX.remove(f);
        int x = f.getX();
        int i;
        for (i = 0; i < figObjectsX.size(); i++) {
            Fig fig = (Fig) figObjectsX.get(i);
            if (fig.getX() > x) {
		break;
	    }
	}
        figObjectsX.add(i, f);
    }

    /*
     * @see org.tigris.gef.base.Layer#deleted(org.tigris.gef.presentation.Fig)
     */
    public void deleted(Fig f) {
        super.deleted(f);
        figObjectsX.remove(f);
        if (!figObjectsX.isEmpty()) {
            putInPosition((Fig) figObjectsX.get(0));
        }
    }

    /**
     * Update Activations.
     * First removes all current activation boxes, then adds new ones
     * to all figobject depending on the state of the nodes.
     */
    public void updateActivations() {
        makeUniformNodeCount();
        for (Iterator it = figObjectsX.iterator(); it.hasNext();) {
            Object fig = it.next();
            if (fig instanceof FigClassifierRole) {
                ((FigClassifierRole) fig).updateActivations();
                ((FigClassifierRole) fig).damage();
            }
        }
    }

    /*
     * @see org.tigris.gef.base.Layer#remove(org.tigris.gef.presentation.Fig)
     */
    public void remove(Fig f) {
        if (f instanceof FigMessage) {
            LOG.info("Removing a FigMessage");
            FigMessage fm = (FigMessage) f;
            FigMessagePort source = (FigMessagePort) fm.getSourcePortFig();
            FigMessagePort dest = (FigMessagePort) fm.getDestPortFig();
            
            if (source != null) {
        	removeFigMessagePort(source);
            }
            if (dest != null) {
        	removeFigMessagePort(dest);
            }
            if (source != null) {
                FigLifeLine sourceLifeLine = (FigLifeLine) source.getGroup();
            	updateNodeStates(source, sourceLifeLine);
            }
            if (dest != null && fm.getSourceFigNode() != fm.getDestFigNode()) {
                FigLifeLine destLifeLine = (FigLifeLine) dest.getGroup();
                updateNodeStates(dest, destLifeLine);
            }
        }
        super.remove(f);
        
        LOG.info("A Fig has been removed, updating activations");
        updateActivations();
    }
    
    private void removeFigMessagePort(FigMessagePort fmp) {
        Fig parent = fmp.getGroup();
        if (parent instanceof FigLifeLine) {
            ((FigClassifierRole) parent.getGroup()).removeFigMessagePort(fmp);
        }
    }
    
    // TODO: Get rid of first argument.
    private void updateNodeStates(FigMessagePort fmp, FigLifeLine lifeLine) {
    	if (lifeLine != null) {
            ((FigClassifierRole) lifeLine.getGroup()).updateNodeStates();
    	}
    }
    

    /**
     * Returns a list with all {@link FigMessage}s that intersect with
     * the given y coordinate.<p>
     *
     * @param y is the given y coordinate.
     * @return the list with {@link FigMessage}s.
     */
    public List getFigMessages(int y) {
        if (getContents().isEmpty()
	    || getContentsEdgesOnly().isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List retList = new ArrayList();
        Iterator it = getContentsEdgesOnly().iterator();
        while (it.hasNext()) {
            FigEdge fig = (FigEdge) it.next();
            if (fig instanceof FigMessage
                && fig.hit(new Rectangle(fig.getX(), y, 8, 8))) {
                retList.add(fig);
            }

        }
        return retList;

    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 4291295642883664670L;
}
