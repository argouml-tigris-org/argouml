// $Id$
// Copyright (c) 2005-2006 The Regents of the University of California. All
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

package org.argouml.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.argouml.model.Model;
import org.argouml.uml.diagram.static_structure.ui.FigEdgeNote;
import org.argouml.uml.diagram.static_structure.ui.FigPackage;
import org.argouml.uml.diagram.ui.FigEdgeAssociationClass;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Layer;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigGroup;

/**
 * Utility class for use by pgml.tee.
 *
 * @author Bob Tarling
 */
public final class PgmlUtility {

    /**
     * Constructor.
     */
    private PgmlUtility() {
    }
    
    /**
     * Translate the visibility flag of a Fig to the PGML "visibility" attribute
     * value.
     * The PGML values are 0=hidden and 1=shown.
     * If not specified then 1 is the default so we return null for this to
     * prevent redundent data being written to PGML.
     * TODO: Remove on GEF release after 0.11.9 as it will be provided there.
     * 
     * @param f The Fig
     * @return "0"=hidden, null=shown
     */
    public static String getVisibility(Fig f) {
        if (f.isVisible()) return null;
        return "0";
    }
    
    /**
     * Return just the comment edges for a specific layer.
     * TODO: Document: Diagram / layer?
     *
     * @param diagram The diagram.
     * @return a {@link List} with the edges.
     */
    public static List getEdges(Diagram diagram) {
        Layer lay = diagram.getLayer();
        Collection edges = lay.getContentsEdgesOnly();
        List returnEdges = new ArrayList(edges.size());
        getEdges(diagram, edges, returnEdges);
        return returnEdges;
    }

    /**
     * Return the diagram contents in the order to save to PGML
     * Nodes first, then edges connecting nodes and lastly
     * edges that connect edges to other edges.
     *
     * @param diagram The {@link Diagram}.
     * @return a {@link List} with the contents.
     */
    public static List getContents(Diagram diagram) {
        Layer lay = diagram.getLayer();
        List contents = lay.getContents();
        int size = contents.size();
        List list = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            Object o = contents.get(i);
            if (!(o instanceof FigEdge)) {
                list.add(o);
            }
        }
        getEdges(diagram, lay.getContentsEdgesOnly(), list);
        return list;
    }
    
    private static void getEdges(Diagram diagram, 
            Collection edges, List returnEdges) {
        Iterator it = edges.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (!(o instanceof FigEdgeNote) 
                    && !(o instanceof FigEdgeAssociationClass)) {
                returnEdges.add(o);
            }
        }
        it = edges.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof FigEdgeAssociationClass) {
                returnEdges.add(o);
            }
        }
        it = edges.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof FigEdgeNote) {
                returnEdges.add(o);
            }
        }
    }

    /**
     * Return the identifier for this Fig which is the encloser 
     * of the given Fig
     * @param f the Fig to generate the id for
     * @return a unique string
     */
    public static String getEnclosingId(Fig f) {
        
        Fig encloser = f.getEnclosingFig();
        
        if (encloser instanceof FigPackage) {
            // I hack that works around issue 4729 and 4674
            // If we can determine why FigPackage.modelChanged
            // receive rogue events we can remove this block.
            Object namespace = Model.getFacade().getNamespace(f.getOwner());
            if (namespace != encloser.getOwner()) {
                return null;
            }
        }
        
        if (encloser == null) {
            return null;
        }
        
        return getId(encloser);
    }


    /**
     * Generate an identifier for this Fig which is unique within the 
     * diagram.
     * @param f the Fig to generate the id for
     * @return a unique string
     */
    public static String getId(Fig f) {
        if (f == null) {
            throw new IllegalArgumentException("A fig must be supplied");
        }
        if (f.getGroup() != null) {
            String groupId = f.getGroup().getId();
            if (f.getGroup() instanceof FigGroup) {
                FigGroup group = (FigGroup) f.getGroup();
                return groupId + "." + (group.getFigs()).indexOf(f);
            } else if (f.getGroup() instanceof FigEdge) {
                FigEdge edge = (FigEdge) f.getGroup();
                return groupId + "."
                        + (((List) edge.getPathItemFigs()).indexOf(f) + 1);
            } else {
                return groupId + ".0";
            }
        }

        Layer layer = f.getLayer();
        if (layer == null) {
            return "LAYER_NULL";
        }

        List c = layer.getContents();
        int index = c.indexOf(f);
        return "Fig" + index;
    }

}
