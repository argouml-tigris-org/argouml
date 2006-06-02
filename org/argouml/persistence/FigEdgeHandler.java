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
            String sourcePortFig = null;
            String destPortFig = null;
            String sourceFigNode = null;
            String destFigNode = null;
            while (st2.hasMoreElements()) {
                String attribute = st2.nextToken();
                String value = st2.nextToken();
                
                if (attribute.equals("sourcePortFig")) {
                    sourcePortFig = value;
                }

                if (attribute.equals("destPortFig")) {
                    destPortFig = value;
                }

                if (attribute.equals("sourceFigNode")) {
                    sourceFigNode = value;
                }

                if (attribute.equals("destFigNode")) {
                    destFigNode = value;
                }
            }
            
            ((org.argouml.persistence.PGMLStackParser)parser).addFigEdge(
                    edge, 
                    sourcePortFig, 
                    destPortFig, 
                    sourceFigNode, 
                    destFigNode);
        }
    }
}
