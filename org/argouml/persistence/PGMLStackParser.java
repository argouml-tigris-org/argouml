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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.argouml.uml.diagram.ui.AttributesCompartmentContainer;
import org.argouml.uml.diagram.ui.ExtensionsCompartmentContainer;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.FigEdgePort;
import org.argouml.uml.diagram.ui.OperationsCompartmentContainer;
import org.argouml.uml.diagram.ui.PathContainer;
import org.argouml.uml.diagram.ui.StereotypeContainer;
import org.argouml.uml.diagram.ui.VisibilityContainer;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.persistence.pgml.Container;
import org.tigris.gef.persistence.pgml.FigEdgeHandler;
import org.tigris.gef.persistence.pgml.FigGroupHandler;
import org.tigris.gef.persistence.pgml.HandlerStack;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigNode;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The PGML Parser.
 */
public class PGMLStackParser
    extends org.tigris.gef.persistence.pgml.PGMLStackParser {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(PGMLStackParser.class);

    private List figEdges = new ArrayList(50);
    
    /**
     * Constructor.
     * @param modelElementsByUuid a map of model elements indexed
     *                            by a unique string identifier.
     */
    public PGMLStackParser(Map modelElementsByUuid) {
        super(modelElementsByUuid);
        // TODO: Use stylesheet to convert or wait till we use Fig
        // factories in diagram subsystem.
        // What is the last version that used FigNote?
        addTranslation("org.argouml.uml.diagram.static_structure.ui.FigNote",
            "org.argouml.uml.diagram.static_structure.ui.FigComment");
        addTranslation("org.argouml.uml.diagram.state.ui.FigState",
            "org.argouml.uml.diagram.state.ui.FigSimpleState");
        addTranslation("org.argouml.uml.diagram.ui.FigCommentPort",
            "org.argouml.uml.diagram.ui.FigEdgePort");
    }

    /**
     * @see org.tigris.gef.persistence.pgml.HandlerFactory#getHandler(
     *         HandlerStack, Object, String, String, String, Attributes)
     */
    public DefaultHandler getHandler(HandlerStack stack,
                                             Object container,
                                             String uri,
                                             String localname,
                                             String qname,
                                             Attributes attributes)
        throws SAXException {

        // Ignore non-private elements within FigNode groups
        if (container instanceof FigGroupHandler) {
            FigGroup group = ((FigGroupHandler) container).getFigGroup();
            if (group instanceof FigNode && !qname.equals("private")) {
                return null;
            }
        }

        // Handle ItemUID in container contents
        if (qname.equals("private") && (container instanceof Container)) {
            return new PrivateHandler(this, (Container) container);
        }

        DefaultHandler handler =
            super.getHandler(stack, container, uri, localname, qname,
                    attributes);

        if (handler instanceof FigEdgeHandler) {
            return new org.argouml.persistence.FigEdgeHandler(
                    this, ((FigEdgeHandler) handler).getFigEdge());
        }

        return handler;

    }

    /**
     * @see org.tigris.gef.persistence.pgml.PGMLStackParser#setAttrs(
     *         org.tigris.gef.presentation.Fig, org.xml.sax.Attributes)
     * TODO: Change to protected here and in GEF
     */
    public final void setAttrs(Fig f, Attributes attrList) throws SAXException {
        if (f instanceof FigGroup) {
            FigGroup group = (FigGroup) f;
            String clsNameBounds = attrList.getValue("description");
            if (clsNameBounds != null) {
                StringTokenizer st =
                    new StringTokenizer(clsNameBounds, ",;[] ");
                // Discard class name, x y w h
                if (st.hasMoreElements()) {
                    st.nextToken();
                }
                if (st.hasMoreElements()) {
                    st.nextToken();
                }
                if (st.hasMoreElements()) {
                    st.nextToken();
                }
                if (st.hasMoreElements()) {
                    st.nextToken();
                }
                if (st.hasMoreElements()) {
                    st.nextToken();
                }

                Map attributeMap = interpretStyle(st);
                setStyleAttributes(group, attributeMap);
            }
        }

        // TODO: Code within these comments should be removed and replaced with
        // the commented out line once issue
        // http://argouml.tigris.org/issues/show_bug.cgi?id=4020 and
        // http://argouml.tigris.org/issues/show_bug.cgi?id=4021 have been
	// resolved

        super.setAttrs(f, attrList);

        String name = attrList.getValue("name");
        if (name != null && !name.equals("")) {
            registerFig(f, name);
        }

        setCommonAttrs(f, attrList);

        String href = attrList.getValue("href");
        if (href != null && !href.equals("")) {
            Object modelElement = findOwner(href);
            if (modelElement == null) {
                LOG.error("Can't find href of " + href);
                throw new SAXException("Found href of " + href
				       + " with no matching element in model");
            }
            if (f.getOwner() != modelElement) {
                f.setOwner(modelElement);
            } else {
                LOG.debug("Ignoring href on " + f.getClass().getName()
			 + " as it's already set");
            }
        }
    }

    /**
     * The StringTokenizer is expected to be positioned at the start of a string
     * of style identifiers in the format name=value;name=value;name=value....
     * Each name value pair is interpreted and the Fig configured accordingly.
     * The value is optional and will default to a value applicable for its
     * name.
     * The current applicable names are operationsVisible and attributesVisible
     * and are used to show or hide the compartments within Class and Interface.
     * The default values are true.
     * @param st The StrinkTokenizer positioned at the first style identifier
     * @return a map of attributes
     */
    private Map interpretStyle(StringTokenizer st) {
        Map map = new HashMap();
        String name;
        String value;
        while (st.hasMoreElements()) {
            String namevaluepair = st.nextToken();
            int equalsPos = namevaluepair.indexOf('=');
            if (equalsPos < 0) {
                name = namevaluepair;
                value = "true";
            } else {
                name = namevaluepair.substring(0, equalsPos);
                value = namevaluepair.substring(equalsPos + 1);
            }

            map.put(name, value);
        }
        return map;
    }

    /**
     * Set the fig style attributes. This should move into
     * the render factories as described in issue 859.
     * @param fig the fig to style.
     * @param attributeMap a map of name value pairs
     */
    private void setStyleAttributes(Fig fig, Map attributeMap) {
        String name;
        String value;
        Iterator it = attributeMap.keySet().iterator();
        while (it.hasNext()) {
            name = (String) it.next();
            value = (String) attributeMap.get(name);

            if ("operationsVisible".equals(name)) {
                ((OperationsCompartmentContainer) fig)
                    .setOperationsVisible(value.equalsIgnoreCase("true"));
            } else if ("attributesVisible".equals(name)) {
                ((AttributesCompartmentContainer) fig)
                    .setAttributesVisible(value.equalsIgnoreCase("true"));
            } else if ("stereotypeVisible".equals(name)) {
                ((StereotypeContainer) fig)
                    .setStereotypeVisible(value.equalsIgnoreCase("true"));
            } else if ("visibilityVisible".equals(name)) {
                ((VisibilityContainer) fig)
                .setVisibilityVisible(value.equalsIgnoreCase("true"));
            } else if ("pathVisible".equals(name)) {
                ((PathContainer) fig)
                    .setPathVisible(value.equalsIgnoreCase("true"));
            } else if ("extensionPointVisible".equals(name)) {
                ((ExtensionsCompartmentContainer) fig)
                    .setExtensionPointVisible(value.equalsIgnoreCase("true"));
            }
        }
    }
    
    public Diagram readDiagram(InputStream is, boolean closeStream)
        throws SAXException {
        Diagram d = super.readDiagram(is, closeStream);
        
        // Loop through all edges read and attach them to nodes
        Iterator it = figEdges.iterator();
        while (it.hasNext()) {
            Fig spf = null;
            Fig dpf = null;
            FigNode sfn = null;
            FigNode dfn = null;
            
            EdgeData edgeData = (EdgeData) it.next();
            FigEdge edge = edgeData.getEdge();
            
            spf = findFig(edgeData.getSourcePortFig());
            dpf = findFig(edgeData.getDestPortFig());
            sfn = getFigNode(edgeData.getSourceFigNode());
            dfn = getFigNode(edgeData.getDestFigNode());
            
            if (spf == null && sfn != null) {
                spf = getPortFig(sfn);
            }

            if (dpf == null && dfn != null) {
                dpf = getPortFig(dfn);
            }

            if (spf == null || dpf == null || sfn == null || dfn == null) {
                throw new SAXException("Can't find nodes for FigEdge: "
                        + edge.getId() + ":"
                        + edge.toString());
            } else {
                edge.setSourcePortFig(spf);
                edge.setDestPortFig(dpf);
                edge.setSourceFigNode(sfn);
                edge.setDestFigNode(dfn);
            }
        }
        
        return d;
    }
    
    // TODO: Move to GEF
    public void addFigEdge(FigEdge figEdge, String sourcePortFig, 
            String destPortFig, String sourceFigNode, String destFigNode) {
        figEdges.add(new EdgeData(figEdge, sourcePortFig, destPortFig, 
                sourceFigNode, destFigNode));
    }
    
    // TODO: Move to GEF
    /**
     * Get the FigNode that the fig id represents.
     *
     * @param figId (In the form Figx.y.z)
     * @return the FigNode with the given id
     */
    private FigNode getFigNode(String figId) {
        if (figId.indexOf('.') < 0) {
            // If there is no dot then this must be a top level Fig and can be
            // assumed to be a FigNode.
            return (FigNode) findFig(figId);
        }
        // If the id does not look like a top-level Fig then we can assume that
        // this is an id of a FigEdgePort inside some FigEdge.
        // So extract the FigEdgePort from the FigEdge and return that as
        // the FigNode.
        figId = figId.substring(0, figId.indexOf('.'));
        FigEdgeModelElement edge = (FigEdgeModelElement) findFig(figId);
        if (edge == null) {
            throw new IllegalStateException(
                    "Can't find a FigNode with id " + figId);
        }
        edge.makeEdgePort();
        return edge.getEdgePort();
    }
    

    // TODO: Move to GEF
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
    
    // TODO: Move to GEF
    private class EdgeData {
        FigEdge edge;
        String sourcePortFig;
        String destPortFig;
        String sourceFigNode;
        String destFigNode;
        
        public EdgeData(FigEdge edge, String sourcePort, 
                String destPort, String sourceNode, String destNode) {
            this.edge = edge;
            this.sourcePortFig = sourcePort;
            this.destPortFig = destPort;
            this.sourceFigNode = sourceNode;
            this.destFigNode = destNode;
        }
        
        public String getDestFigNode() {
            return destFigNode;
        }
        public String getDestPortFig() {
            return destPortFig;
        }
        public FigEdge getEdge() {
            return edge;
        }
        public String getSourceFigNode() {
            return sourceFigNode;
        }
        public String getSourcePortFig() {
            return sourcePortFig;
        }
        
    }
}
