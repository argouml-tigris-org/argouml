// $Id$
// Copyright (c) 2003-2005 The Regents of the University of California. All
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
import java.util.SortedMap;
import java.util.TreeMap;

import org.argouml.uml.diagram.sequence.Node;
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
public class SequenceDiagramLayout extends LayerPerspectiveMutable {
    /**
     * The distance between two objects on the sequence diagram.
     */
    public static final int OBJECT_DISTANCE = 30;

    /**
     * The distance between the left side of the diagram and the first
     * {@link FigObject}.
     */
    public static final int DIAGRAM_LEFT_MARGE = 50;

    /**
     * The distance between the top side of the diagram and the top of
     * the highest {@link FigObject}
     */
    public static final int DIAGRAM_TOP_MARGE = 50;

    /**
     * The vertical distance between two links
     */
    public static final int LINK_DISTANCE = 60;

    /**
     * Linked list with all fig objects sorted by x coordinate in it
     */
    private LinkedList figObjectsX = new LinkedList();

    /**
     * The heighest height of the outer box of a figobject.
     */
    private int heighestObjectHeight = 0;

    /**
     * The constructor.
     *
     * @param name the name
     * @param gm the graph model
     */
    public SequenceDiagramLayout(String name, MutableGraphModel gm) {
        super(name, gm);

    }

    /**
     * @see org.tigris.gef.base.LayerPerspective#putInPosition(Fig)
     */
    public void putInPosition(Fig f) {
        if (f instanceof FigObject) {
            distributeFigObjects(f);
            ((FigObject) f).updateEdges();
        } else
            super.putInPosition(f);
    }

    /**
     * Distributes the fig objects contained in _figObjectsX over the
     * sequencediagram.
     *
     * @param f
     */
    private void distributeFigObjects(Fig f) {
        int listPosition = figObjectsX.indexOf(f);
        if (listPosition < 0)
            return;
        if (listPosition < figObjectsX.size() - 1) {
            Fig next = (Fig) figObjectsX.get(listPosition + 1);
            if (next.getX() < f.getX()) {
                reshuffelFigObjectsX(f);
                listPosition = figObjectsX.indexOf(f);
            }
        } else if (listPosition > 0) {
            Fig previous = (Fig) figObjectsX.get(listPosition - 1);
            if (previous.getX() > f.getX()) {
                reshuffelFigObjectsX(f);
                listPosition = figObjectsX.indexOf(f);
            }
        }
        Iterator it =
            figObjectsX.subList(listPosition, figObjectsX.size()).iterator();
        int positionX =
            listPosition == 0
                ? DIAGRAM_LEFT_MARGE
                : (((Fig) figObjectsX.get(listPosition - 1)).getX()
                    + ((Fig) figObjectsX.get(listPosition - 1)).getWidth()
                    + OBJECT_DISTANCE);
        while (it.hasNext()) {
            Fig fig = (Fig) it.next();
            if (fig.getX() == positionX) {
                break;
            }
            fig.setX(positionX);
            ((FigObject) fig).updateEdges();
            positionX += (fig.getWidth() + OBJECT_DISTANCE);
        }
        if (heighestObjectHeight < f.getHeight()) {
            heighestObjectHeight = f.getHeight();
            it = figObjectsX.iterator();
            while (it.hasNext()) {
                Fig fig = (Fig) it.next();
                fig.setY(
                    DIAGRAM_TOP_MARGE + heighestObjectHeight - f.getHeight());
                fig.damage();
            }
        } else {
            f.setY(DIAGRAM_TOP_MARGE + heighestObjectHeight - f.getHeight());
            f.damage();
        }
    }

    /**
     * @see org.tigris.gef.graph.GraphListener#nodeAdded(GraphEvent)
     */
    public void nodeAdded(GraphEvent ge) {
        super.nodeAdded(ge);
        Fig fig = presentationFor(ge.getArg());
        if (fig instanceof FigObject) {
            ((FigObject) fig).renderingChanged();
        }
    }

    /**
     * @see org.tigris.gef.base.Layer#add(org.tigris.gef.presentation.Fig)
     */
    public void add(Fig f) {
        super.add(f);
        if (f instanceof FigObject) {
            SortedMap x = new TreeMap();
            if (!figObjectsX.isEmpty()) {
                Iterator it = figObjectsX.iterator();
                while (it.hasNext()) {
                    Fig fig = (Fig) it.next();
                    x.put(new Integer(fig.getX()), fig);
                }
                Object o = x.get(x.headMap(new Integer(f.getX())).lastKey());
                figObjectsX.add(figObjectsX.indexOf(o) + 1, f);
            } else
                figObjectsX.add(f);
            heighestObjectHeight =
                Math.max(heighestObjectHeight, f.getHeight());
        }
    }

    private void reshuffelFigObjectsX(Fig f) {
        figObjectsX.remove(f);
        int x = f.getX();
        int newPosition = 0;
        for (int i = 0; i < figObjectsX.size(); i++) {
            Fig fig = (Fig) figObjectsX.get(i);
            if (fig.getX() < x) {
                if (i != (figObjectsX.size() - 1)
                    && ((Fig) figObjectsX.get(i + 1)).getX() > x) {
                    newPosition = i + 1;
                    break;
                }
                if (i == (figObjectsX.size() - 1)) {
                    newPosition = i;
                }
            }
        }
        figObjectsX.add(newPosition, f);

    }

    /**
     * @see org.tigris.gef.base.Layer#deleted(org.tigris.gef.presentation.Fig)
     */
    public void deleted(Fig f) {
        super.deleted(f);
        figObjectsX.remove(f);
        if (f.getHeight() == heighestObjectHeight) {
            Iterator it = figObjectsX.iterator();
            while (it.hasNext()) {
                heighestObjectHeight =
                    Math.max(
                        heighestObjectHeight,
                        ((Fig) it.next()).getHeight());
            }
        }
        if (!figObjectsX.isEmpty()) {
            putInPosition((Fig) figObjectsX.get(0));
        }
    }

    public void updateActivations() {
        Iterator it = getContentsNoEdges().iterator();
        while (it.hasNext()) {
            Fig fig = (Fig) it.next();
            if (fig instanceof FigObject) {
                ((FigObject) fig).updateActivations();
            }
        }
    }

    /**
     * Returns a list with all {@link FigLink}s that intersect with
     * the given y coordinate.<p>
     *
     * @param y is the given y coordinate.
     * @return the list with {@link FigLink}s.
     */
    public List getFigLinks(int y) {
        if (getContents(null).isEmpty()
	    || getContentsEdgesOnly().isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List retList = new ArrayList();
        Iterator it = getContentsEdgesOnly().iterator();
        while (it.hasNext()) {
            FigEdge fig = (FigEdge) it.next();
            if (fig instanceof FigLink
                && fig.hit(new Rectangle(fig.getX(), y, 8, 8))) {
                retList.add(fig);
            }

        }
        return retList;

    }

    /**
     * @param position the position for the node
     * @param node the node to be added
     */
    public void addNode(int position, Node node) {
        Iterator it = getContentsNoEdges().iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof FigObject) {
                ((FigObject) o).addNode(position, node);
                ((FigObject) o).updateActivations();
            }
        }
    }

}
