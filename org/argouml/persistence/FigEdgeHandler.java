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
package org.argouml.persistence;

import java.util.StringTokenizer;

import org.argouml.uml.diagram.ui.FigEdgePort;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.tigris.gef.persistence.pgml.PGMLStackParser;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigEdgePoly;
import org.tigris.gef.presentation.FigLine;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigPoly;
import org.xml.sax.SAXException;

/**
 * The handler for elements that represent FigEdge objects.
 * This extends the base GEF class to allow comment edges to connect
 * to nodes inside other edges.
 * @author Bob Tarling
 */
public class FigEdgeHandler
    extends org.tigris.gef.persistence.pgml.FigEdgeHandler {

    /**
     * @param parser
     * @param theEdge
     */
    public FigEdgeHandler(PGMLStackParser parser, FigEdge theEdge) {
        super(parser, theEdge);
    }
    /**
     * Incorporates a contained element into this FigEdge object.<p>
     *
     * Three types of contained elements are supported: FigLine or FigPoly
     * become the Fig associated with this FigEdge; String valued elements
     * (i.e., <em>private</em> elements) are themselves parsed to determin the
     * source and destination PortFig's for this FigEdge.
     *
     * @see org.tigris.gef.persistence.pgml.Container#addObject(java.lang.Object)
     */
    public void addObject(Object o) throws SAXException {
        FigEdge edge = getFigEdge();
        if (o instanceof FigLine || o instanceof FigPoly) {
            edge.setFig((Fig) o);
            if (o instanceof FigPoly) {
                ((FigPoly) o).setComplete(true);
            }
            edge.calcBounds();
            if (edge instanceof FigEdgePoly) {
                ((FigEdgePoly) edge).setInitiallyLaidOut(true);
            }
            edge.updateAnnotationPositions();
        }

        if (o instanceof String) {
            PGMLStackParser parser = getPGMLStackParser();
            Fig spf = null;
            Fig dpf = null;
            FigNode sfn = null;
            FigNode dfn = null;
            String body = (String) o;
            StringTokenizer st2 = new StringTokenizer(body, "=\"' \t\n");
            while (st2.hasMoreElements()) {
                String attribute = st2.nextToken();
                String value = st2.nextToken();
                if (attribute.equals("sourcePortFig")) {
                    spf = parser.findFig(value);
                }

                if (attribute.equals("destPortFig")) {
                    dpf = parser.findFig(value);
                }

                if (attribute.equals("sourceFigNode")) {
                    sfn = getFigNode(parser, value);
                }

                if (attribute.equals("destFigNode")) {
                    dfn = getFigNode(parser, value);
                }
            }

            if (spf == null && sfn != null) {
                spf = getPortFig(sfn);
            }

            if (dpf == null && dfn != null) {
                dpf = getPortFig(dfn);
            }

            if (spf == null || dpf == null || sfn == null || dfn == null) {
                throw new SAXException("Can't find nodes for FigEdge: "
                        + (String) o
                        + ":" + edge.getId() + ":"
                        + edge.toString());
            } else {
                edge.setSourcePortFig(spf);
                edge.setDestPortFig(dpf);
                edge.setSourceFigNode(sfn);
                edge.setDestFigNode(dfn);
            }
        }
    }

    /**
     * Get the FigNode that the fig id represents.
     *
     * @param parser The parser to use.
     * @param figId (In the form Figx.y.z)
     * @return the FigNode with the given id
     */
    private FigNode getFigNode(PGMLStackParser parser, String figId) {
        if (figId.indexOf('.') < 0) {
            // If there is no dot then this must be a top level Fig and can be
            // assumed to be a FigNode.
            return (FigNode) parser.findFig(figId);
        }
        // If the id does not look like a top-level Fig then we can assume that
        // this is an id of a FigCommentPort inside some FigEdge.
        // So extract the FigCommentPort from the FigEdge and return that as
        // the FigNode.
        figId = figId.substring(0, figId.indexOf('.'));
        FigEdgeModelElement edge = (FigEdgeModelElement) parser.findFig(figId);
        if (edge == null) {
            throw new IllegalStateException("Can't find a FigNode with id " + figId);
        }
        edge.makeEdgePort();
        return edge.getEdgePort();
    }


    /**
     * Get the Fig from the FigNode that is the port.
     *
     * @param figNode the FigNode
     * @return the Fig that is the port on the given FigNode
     */
    private Fig getPortFig(FigNode figNode) {
        if (figNode instanceof FigEdgePort) {
            // TODO: Can we just do this every time, no need for else - Bob
            return figNode;
        } else {
            return (Fig) figNode.getPortFigs().get(0);
        }
    }
}
