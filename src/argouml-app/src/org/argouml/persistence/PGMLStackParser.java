// $Id$
// Copyright (c) 2005-2009 The Regents of the University of California. All
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

import java.awt.Rectangle;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.AttributesCompartmentContainer;
import org.argouml.uml.diagram.DiagramEdgeSettings;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ExtensionsCompartmentContainer;
import org.argouml.uml.diagram.OperationsCompartmentContainer;
import org.argouml.uml.diagram.PathContainer;
import org.argouml.uml.diagram.StereotypeContainer;
import org.argouml.uml.diagram.VisibilityContainer;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.FigEdgePort;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.persistence.pgml.Container;
import org.tigris.gef.persistence.pgml.FigEdgeHandler;
import org.tigris.gef.persistence.pgml.FigGroupHandler;
import org.tigris.gef.persistence.pgml.FigLineHandler;
import org.tigris.gef.persistence.pgml.FigPolyHandler;
import org.tigris.gef.persistence.pgml.FigTextHandler;
import org.tigris.gef.persistence.pgml.HandlerFactory;
import org.tigris.gef.persistence.pgml.HandlerStack;
import org.tigris.gef.persistence.pgml.PrivateHandler;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigCircle;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigLine;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigPoly;
import org.tigris.gef.presentation.FigRRect;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;
import org.tigris.gef.util.ColorFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

// TODO: Move to Diagram subsystem?

/**
 * The PGML Parser. <p>
 * 
 * This replaces much of the identically named class from GEF.
 */
class PGMLStackParser
    extends org.tigris.gef.persistence.pgml.PGMLStackParser {

    private static final Logger LOG = Logger.getLogger(PGMLStackParser.class);

    private List<EdgeData> figEdges = new ArrayList<EdgeData>(50);
    
    private LinkedHashMap<FigEdge, Object> modelElementsByFigEdge =
        new LinkedHashMap<FigEdge, Object>(50);

    private DiagramSettings diagramSettings;

    // TODO: Use stylesheet to convert or wait till we use Fig
    // factories in diagram subsystem.
    // What is the last version that used FigNote?
    private void addTranslations() {
        addTranslation("org.argouml.uml.diagram.ui.FigNote",
        	"org.argouml.uml.diagram.static_structure.ui.FigComment");
        addTranslation("org.argouml.uml.diagram.static_structure.ui.FigNote",
            "org.argouml.uml.diagram.static_structure.ui.FigComment");
        addTranslation("org.argouml.uml.diagram.state.ui.FigState",
            "org.argouml.uml.diagram.state.ui.FigSimpleState");
        addTranslation("org.argouml.uml.diagram.ui.FigCommentPort",
            "org.argouml.uml.diagram.ui.FigEdgePort");
        addTranslation("org.tigris.gef.presentation.FigText",
                "org.argouml.uml.diagram.ui.ArgoFigText");
        addTranslation("org.tigris.gef.presentation.FigLine",
                "org.argouml.gefext.ArgoFigLine");
        addTranslation("org.tigris.gef.presentation.FigPoly",
                "org.argouml.gefext.ArgoFigPoly");
        addTranslation("org.tigris.gef.presentation.FigCircle",
                "org.argouml.gefext.ArgoFigCircle");
        addTranslation("org.tigris.gef.presentation.FigRect",
                "org.argouml.gefext.ArgoFigRect");
        addTranslation("org.tigris.gef.presentation.FigRRect",
                "org.argouml.gefext.ArgoFigRRect");
        addTranslation(
                "org.argouml.uml.diagram.deployment.ui.FigMNodeInstance",
                "org.argouml.uml.diagram.deployment.ui.FigNodeInstance");
        addTranslation("org.argouml.uml.diagram.ui.FigRealization",
                "org.argouml.uml.diagram.ui.FigAbstraction");
    }
    
    /**
     * Construct a PGML parser with the given HREF/Object map and default
     * diagram settings.
     * 
     * @param modelElementsByUuid map of HREF ids to objects used to associate
     *            Figs with their owning model elements
     * @param defaultSettings default diagram settings to use for newly created
     *            diagram and its contained Figs
     */
    public PGMLStackParser(Map<String, Object> modelElementsByUuid, 
            DiagramSettings defaultSettings) {
        super(modelElementsByUuid);
        addTranslations();
        // Create a new diagram wide settings block which is backed by 
        // the project-wide defaults that we were passed
        diagramSettings = new DiagramSettings(defaultSettings);
    }

    /*
     * @see org.tigris.gef.persistence.pgml.HandlerFactory#getHandler(
     *         HandlerStack, Object, String, String, String, Attributes)
     */
    @Override
    public DefaultHandler getHandler(HandlerStack stack,
                                             Object container,
                                             String uri,
                                             String localname,
                                             String qname,
                                             Attributes attributes)
        throws SAXException {

        String href = attributes.getValue("href");
        Object owner = null;
        
        if (href != null) {
            owner = findOwner(href);
            if (owner == null) {
                LOG.warn("Found href of " 
                	+ href
                	+ " with no matching element in model");
                return null;
            }
        }
	
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
            getHandlerSuper(stack, container, uri, localname, qname,
                    attributes);

        if (handler instanceof FigEdgeHandler) {
            return new org.argouml.persistence.FigEdgeHandler(
                    this, ((FigEdgeHandler) handler).getFigEdge());
        }

        return handler;

    }

    /*
     * @see org.tigris.gef.persistence.pgml.PGMLStackParser#setAttrs(
     *         org.tigris.gef.presentation.Fig, org.xml.sax.Attributes)
     */
    @Override
    protected final void setAttrs(Fig f, Attributes attrList)
        throws SAXException {
        
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

                Map<String, String> attributeMap = interpretStyle(st);
                setStyleAttributes(group, attributeMap);
            }
        }

        // TODO: Attempt to move the following code to GEF

        String name = attrList.getValue("name");
        if (name != null && !name.equals("")) {
            registerFig(f, name);
        }

        setCommonAttrs(f, attrList);

        final String href = attrList.getValue("href");
        if (href != null && !href.equals("")) {
            Object modelElement = findOwner(href);
            if (modelElement == null) {
                LOG.error("Can't find href of " + href);
                throw new SAXException("Found href of " + href
				       + " with no matching element in model");
            }
            // The owner should always have already been set in the constructor
            if (f.getOwner() != modelElement) {
                // Assign nodes immediately but edges later. See issue 4310.
                if (f instanceof FigEdge) {
                    modelElementsByFigEdge.put((FigEdge) f, modelElement);
                } else {
                    f.setOwner(modelElement);
                }
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
    private Map<String, String> interpretStyle(StringTokenizer st) {
        Map<String, String> map = new HashMap<String, String>();
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
    private void setStyleAttributes(Fig fig, Map<String, String> attributeMap) {
        
        for (Map.Entry<String, String> entry : attributeMap.entrySet()) {
            final String name = entry.getKey();
            final String value = entry.getValue();

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

    /**
     * Read and parse the input stream to create a new diagram and return it.
     * 
     * @param is the input stream
     * @param closeStream true to close the stream when parsing is complete
     * @return the diagram created as a result of the parse
     * @throws SAXException
     */
    public ArgoDiagram readArgoDiagram(InputSource is, boolean closeStream)
        throws SAXException {

        return (ArgoDiagram) readDiagram(is.getByteStream(), closeStream);
    }
    
    /**
     * Read and parse the input stream to create a new diagram and return it.
     * 
     * @param is the input stream
     * @param closeStream true to close the stream when parsing is complete
     * @return the diagram created as a result of the parse
     * @throws SAXException
     */
    public ArgoDiagram readArgoDiagram(InputStream is, boolean closeStream)
        throws SAXException {

        return (ArgoDiagram) readDiagram(is, closeStream);
    }
    
    @Override
    public Diagram readDiagram(InputStream is, boolean closeStream)
        throws SAXException {
        
        // TODO: we really want to be able replace the initial content handler
        // which is passed to SAX, but we can't do this without cloning a
        // whole bunch of code because it's private in the super class.
        
        Diagram d = super.readDiagram(is, closeStream);
        
        attachEdges(d);
        
        return d;
    }
    
    /**
     * This is called when all nodes and edges have been read and placed on
     * the diagram. This method then attaches the edges to the correct node,
     * including the nodes contained within edges allowing edge to edge
     * connections for comment edges, association classes and dependencies.
     * @param d the Diagram
     */
    private void attachEdges(Diagram d) {
        for (EdgeData edgeData : figEdges) {
            final FigEdge edge = edgeData.getFigEdge();
            
            Object modelElement = modelElementsByFigEdge.get(edge);
            if (modelElement != null) {
                if (edge.getOwner() == null) {
                    edge.setOwner(modelElement);
                }
            }
        }
        
        for (EdgeData edgeData : figEdges) {
            final FigEdge edge = edgeData.getFigEdge();
            
            Fig sourcePortFig = findFig(edgeData.getSourcePortFigId());
            Fig destPortFig = findFig(edgeData.getDestPortFigId());
            final FigNode sourceFigNode =
                getFigNode(edgeData.getSourceFigNodeId());
            final FigNode destFigNode =
                getFigNode(edgeData.getDestFigNodeId());
            
            if (sourceFigNode instanceof FigEdgePort) {
                sourcePortFig = sourceFigNode;
            }
            
            if (destFigNode instanceof FigEdgePort) {
                destPortFig = destFigNode;
            }
            
            if (sourcePortFig == null && sourceFigNode != null) {
                sourcePortFig = getPortFig(sourceFigNode);
            }

            if (destPortFig == null && destFigNode != null) {
                destPortFig = getPortFig(destFigNode);
            }

            if (sourcePortFig == null
            	|| destPortFig == null 
            	|| sourceFigNode == null 
            	|| destFigNode == null) {
                LOG.error("Can't find nodes for FigEdge: "
                        + edge.getId() + ":"
                        + edge.toString());
                edge.removeFromDiagram();
            } else {
                edge.setSourcePortFig(sourcePortFig);
                edge.setDestPortFig(destPortFig);
                edge.setSourceFigNode(sourceFigNode);
                edge.setDestFigNode(destFigNode);
            }
        }
        
        // Once all edges are connected do a compute route on each to make
        // sure that annotations and the edge port is positioned correctly
        // Only do this after all edges are connected as compute route
        // requires all edges to be connected to nodes.
        // TODO: It would be nice not to have to do this and restore annotation
        // positions instead.
        for (Object edge : d.getLayer().getContentsEdgesOnly()) {
            FigEdge figEdge = (FigEdge) edge;
            figEdge.computeRouteImpl();
        }
    }
    
    // TODO: Move to GEF
    /**
     * Store data of a FigEdge together with the id's of nodes to connect to
     * @param figEdge The FigEdge
     * @param sourcePortFigId The id of the source port
     * @param destPortFigId The id of the destination port
     * @param sourceFigNodeId The id of the source node
     * @param destFigNodeId The id of the destination node
     */
    public void addFigEdge(
            final FigEdge figEdge,
            final String sourcePortFigId, 
            final String destPortFigId, 
            final String sourceFigNodeId, 
            final String destFigNodeId) {
        figEdges.add(new EdgeData(figEdge, sourcePortFigId, destPortFigId, 
                sourceFigNodeId, destFigNodeId));
    }
    
    // TODO: Move to GEF
    /**
     * Get the FigNode that the fig id represents.
     *
     * @param figId (In the form Figx.y.z)
     * @return the FigNode with the given id
     * @throws IllegalStateException
     *              if the figId supplied is not of a FigNode
     */
    private FigNode getFigNode(String figId) throws IllegalStateException {
        if (figId.contains(".")) {
            // If the id does not look like a top-level Fig then we can assume
            // that this is an id of a FigEdgePort inside some FigEdge.
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
        } else {
            // If there is no dot then this must be a top level Fig and can be
            // assumed to be a FigNode.
            Fig f = findFig(figId);
            if (f instanceof FigNode) {
                return (FigNode) f;
            } else {
                LOG.error("FigID " + figId + " is not a node, edge ignored");
                return null;
            }
        }
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
    
    /**
     * The data from an edge extracted from the PGML before we can guarantee
     * all the nodes have been constructed. This stores the FigEdge and the
     * id's of the nodes to connect to later.
     * If the nodes are not known then the ports are returned instead.
     */
    private class EdgeData {
        private final FigEdge figEdge;
        private final String sourcePortFigId;
        private final String destPortFigId;
        private final String sourceFigNodeId;
        private final String destFigNodeId;
        
        /**
         * Constructor
         * @param edge The FigEdge
         * @param sourcePortId The id of the source port
         * @param destPortId The id of the destination port
         * @param sourceNodeId The id of the source node
         * @param destNodeId The id of the destination node
         */
        public EdgeData(FigEdge edge, String sourcePortId, 
                String destPortId, String sourceNodeId, String destNodeId) {
            if (sourcePortId == null || destPortId == null) {
                throw new IllegalArgumentException(
                        "source port and dest port must not be null"
                        + " source = " + sourcePortId
                        + " dest = " + destPortId
                        + " figEdge = " + edge);
            }
            this.figEdge = edge;
            this.sourcePortFigId = sourcePortId;
            this.destPortFigId = destPortId;
            this.sourceFigNodeId =
                sourceNodeId != null ? sourceNodeId : sourcePortId;
            this.destFigNodeId = 
                destNodeId != null ? destNodeId : destPortId;
        }
        
        /**
         * Get the id of the destination FigNode
         * @return the id
         */
        public String getDestFigNodeId() {
            return destFigNodeId;
        }
        
        /**
         * Get the id of the destination port
         * @return the id
         */
        public String getDestPortFigId() {
            return destPortFigId;
        }
        /**
         * Get the FigEdge
         * @return the FigEdge
         */
        public FigEdge getFigEdge() {
            return figEdge;
        }
        /**
         * Get the id of the source FigNode
         * @return the id
         */
        public String getSourceFigNodeId() {
            return sourceFigNodeId;
        }
        /**
         * Get the id of the source port
         * @return the id
         */
        public String getSourcePortFigId() {
            return sourcePortFigId;
        }
    }

    /**
     * Returns ContentHandler objects appropriate for the standard set of
     * elements that can appear within a PGML file.
     * <p>
     * 
     * First, the <em>description</em> attribute is checked for a PGML-style
     * class name specifier; if one is found, it is passed through the
     * {@link #translateType translateClassName} method and the resulting class
     * name is instantiated. If the resulting object is itself an instance of
     * {@link HandlerFactory}, the method returns the result of calling
     * {@link HandlerFactory#getHandler getHandler} on that object with the same
     * arguments. This allows a Fig object to take complete control of its own
     * parsing without imposing any change on the global parsing framework.
     * <p>
     * 
     * If the element doesn't incorporate a class name, or if the instanced
     * object does not implement {@link HandlerFactory}, the element name is
     * compared with one of PGML's special element names. If a known element
     * name is found, either the element is processed immediately and null
     * returned, or the appropriate handler for that element is returned. If the
     * element name is unknown, null is returned.
     * <p>
     * 
     * @param stack
     *                Implementation of the stack of content handlers
     * @param container
     *                An object that provides context for the element (most
     *                often by providing an implementation of the
     *                {@link Container} interface.
     * @param uri
     *                SAX uri argument
     * @param localname
     *                SAX local element name
     * @param qname
     *                SAX qualified element name
     * @param attributes
     *                The attributes that the SAX parser have identified for the
     *                element.
     * @return ContentHandler object appropriate for the element, or null if the
     *         element can be skipped
     * 
     * @see org.tigris.gef.persistence.pgml.HandlerFactory#getHandler(
     *      org.argouml.gef.HandlerStack, java.lang.Object, java.lang.String,
     *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    public DefaultHandler getHandlerSuper(HandlerStack stack, Object container,
            String uri, String localname, String qname, Attributes attributes)
            throws SAXException {

        String href = attributes.getValue("href");

        String clsNameBounds = attributes.getValue("description");
        Object elementInstance = null;
        if (clsNameBounds != null) {

            StringTokenizer st = new StringTokenizer(clsNameBounds, ",;[] ");
            String clsName = translateType(st.nextToken());
            elementInstance = constructFig(clsName, href,
                    getBounds(clsNameBounds), attributes);
            if (elementInstance instanceof HandlerFactory) {
                return ((HandlerFactory) elementInstance).getHandler(stack,
                        container, uri, localname, qname, attributes);
            }
        }

        // If we got here, one of the built-in handlers will apply
        if (qname.equals("group")) {
            if (elementInstance instanceof FigGroup) {
                return getGroupHandler(container, (FigGroup) elementInstance,
                        attributes);
            }
            if (elementInstance instanceof FigEdge) {
                setAttrs((FigEdge) elementInstance, attributes);
                if (container instanceof Container) {
                    ((Container) container).addObject(elementInstance);
                }
                return new FigEdgeHandler(this, (FigEdge) elementInstance);
            }
        }

        if (qname.equals("text")) {
            if (elementInstance == null) {
                elementInstance = new FigText(0, 0, 100, 100);
            }
            if (elementInstance instanceof FigText) {
                FigText text = (FigText) elementInstance;
                setAttrs(text, attributes);
                if (container instanceof Container) {
                    ((Container) container).addObject(text);
                }
                String font = attributes.getValue("font");
                if (font != null && !font.equals("")) {
                    text.setFontFamily(font);
                }

                String textsize = attributes.getValue("textsize");
                if (textsize != null && !textsize.equals("")) {
                    int textsizeInt = Integer.parseInt(textsize);
                    text.setFontSize(textsizeInt);
                }

                String justification = attributes.getValue("justification");
                if (justification != null && !justification.equals("")) {
                    text.setJustificationByName(justification);
                }
                String italic = attributes.getValue("italic");
                if (italic != null && !italic.equals("")) {
                    text.setItalic(Boolean.valueOf(italic).booleanValue());
                }
                String bold = attributes.getValue("bold");
                if (bold != null && !bold.equals("")) {
                    text.setBold(Boolean.valueOf(bold).booleanValue());
                }

                String textColor = attributes.getValue("textcolor");
                if (textColor != null && !textColor.equals("")) {
                    text.setTextColor(ColorFactory.getColor(textColor));
                }

                return new FigTextHandler(this, text);
            }
        }

        if (qname.equals("path") || qname.equals("line")) {
            if (elementInstance == null) {
                elementInstance = new FigPoly();
            }
            if (elementInstance instanceof FigLine) {
                setAttrs((Fig) elementInstance, attributes);
                if (container instanceof Container) {
                    ((Container) container).addObject(elementInstance);
                }
                return new FigLineHandler(this, (FigLine) elementInstance);
            }
            if (elementInstance instanceof FigPoly) {
                setAttrs((Fig) elementInstance, attributes);
                if (container instanceof Container) {
                    ((Container) container).addObject(elementInstance);
                }
                return new FigPolyHandler(this, (FigPoly) elementInstance);
            }
        }

        if (qname.equals("private")) {
            if (elementInstance != null) {
                LOG.warn("private element unexpectedly generated instance: "
                        + elementInstance.toString());
            }
            if (container instanceof Container) {
                return new PrivateHandler(this, (Container) container);
            } else {
                LOG.warn("private element with inappropriate container: "
                        + container.toString());
            }
        }

        if (qname.equals("rectangle")) {
            String cornerRadius = attributes.getValue("rounding");
            int rInt = -1;
            if (cornerRadius != null && cornerRadius.length() > 0) {
                rInt = Integer.parseInt(cornerRadius);
            }
            if (elementInstance == null) {
                if (rInt >= 0) {
                    elementInstance = new FigRRect(0, 0, 80, 80);
                } else {
                    elementInstance = new FigRect(0, 0, 80, 80);
                }
            }
            if (elementInstance instanceof FigRRect && rInt >= 0) {
                ((FigRRect) elementInstance).setCornerRadius(rInt);
            }
            if (elementInstance instanceof Fig) {
                setAttrs((Fig) elementInstance, attributes);
                if (container instanceof Container) {
                    ((Container) container).addObject(elementInstance);
                }
                return null;
            }
        }

        if (qname.equals("ellipse")) {
            System.out.println("Found an ellipse");
            if (elementInstance == null) {
                System.out.println("Created a FigCircle");
                elementInstance = new FigCircle(0, 0, 50, 50);
            }
            if (elementInstance instanceof FigCircle) {
                FigCircle f = (FigCircle) elementInstance;
                setAttrs(f, attributes);
                String rx = attributes.getValue("rx");
                String ry = attributes.getValue("ry");
                int rxInt = (rx == null || rx.equals("")) ? 10 : Integer
                        .parseInt(rx);
                int ryInt = (ry == null || ry.equals("")) ? 10 : Integer
                        .parseInt(ry);
                f.setBounds(f.getX() - rxInt, f.getY() - ryInt, rxInt * 2,
                        ryInt * 2);
                if (container instanceof Container) {
                    ((Container) container).addObject(elementInstance);
                }

                return null;
            }
        }

        // Don't know what to do; throw up our hands--usually this
        // will mean that sub-elements are ignored
        LOG.info("Unrecognized element " + qname);
        if (elementInstance != null) {
            // Implement reasonable default behavior
            if (elementInstance instanceof Fig) {
                setAttrs((Fig) elementInstance, attributes);
            }
            if (container instanceof Container) {
                ((Container) container).addObject(elementInstance);
            }
        }
        return null;
    }
    
    /**
     * Construct a new instance of the named Fig with the owner represented
     * by the given href and the bounds parsed from the PGML file.  We look
     * for constructors of the form Fig(Object owner, Rectangle
     * bounds, DiagramSettings settings) which is typically used for subclasses
     * of FigNodeModelElement, then Fig(Object owner, DiagramSettings settings)
     * which is used for subclasses of FigEdgeModelElement.
     * <p>
     * If we fail to find any of the constructors that we know about, we'll
     * call GEF's version of this method to see if it can find a constructor.
     * 
     * @param className fully qualified name of class to instantiate
     * @param href string representing UUID of owning element
     * @param bounds position and size of figure
     * @param attributes the XML attributes that made up the top level element
     * in PGML
     * @return
     * @throws SAXException
     */
    private Fig constructFig(String className, String href, Rectangle bounds, Attributes attributes)
        throws SAXException {
	
	Fig f = null;

        try {
            Class figClass = Class.forName(className);
            for (Constructor constructor : figClass.getConstructors()) {
                Class[] parameterTypes = constructor.getParameterTypes();
                // FigNodeModelElements should match here
                if (parameterTypes.length == 3
                        && parameterTypes[0].equals(Object.class)
                        && parameterTypes[1].equals(Rectangle.class)
                        && parameterTypes[2].equals(DiagramSettings.class)
                ) {
                    Object parameters[] = new Object[3];
                    Object owner = null;
                    if (href != null) {
                        owner = findOwner(href);
                    }
                    parameters[0] = owner;
                    parameters[1] = bounds;
                    parameters[2] = 
                        ((ArgoDiagram) getDiagram()).getDiagramSettings();
                    
                    constructor.setAccessible(true);
                    f =  (Fig) constructor.newInstance(parameters);
                }
                // FigEdgeModelElements should match here (they have no bounds)
                if (parameterTypes.length == 2
                        && parameterTypes[0].equals(DiagramEdgeSettings.class)
                        && parameterTypes[1].equals(DiagramSettings.class)
                ) {
                    Object parameters[] = new Object[2];
                    Object owner = null;
                    if (href != null) {
                        owner = findOwner(href);
                    }
                    
                    String sourceUuid =
                        attributes.getValue("sourceConnector");
                    String destinationUuid =
                        attributes.getValue("destConnector");
                    
                    LOG.info("The source connector uuid is "
                            + sourceUuid);
                    LOG.info("The destination connector uuid is "
                            + destinationUuid);
                    
                    final Object source;
                    final Object destination;
                    if (sourceUuid != null && destinationUuid != null) {
                        source = findOwner(sourceUuid);
                        destination = findOwner(destinationUuid);
                    } else {
                        source = null;
                        destination = null;
                    }
                    
                    DiagramEdgeSettings settings =
                        new DiagramEdgeSettings(owner, source, destination);
                    parameters[0] = settings;
                    parameters[1] = 
                        ((ArgoDiagram) getDiagram()).getDiagramSettings();

                    constructor.setAccessible(true);
                    f =  (Fig) constructor.newInstance(parameters);
                }
            }
            if (f == null) {
                // FigEdgeModelElements with the old style constructor should
                // match here (they have no bounds)
                for (Constructor constructor : figClass.getConstructors()) {
                    Class[] parameterTypes = constructor.getParameterTypes();
                    if (parameterTypes.length == 2
                            && parameterTypes[0].equals(Object.class)
                            && parameterTypes[1].equals(DiagramSettings.class)
                    ) {
                        Object parameters[] = new Object[2];
                        Object owner = null;
                        if (href != null) {
                            owner = findOwner(href);
                        }
                        parameters[0] = owner;
                        parameters[1] = 
                            ((ArgoDiagram) getDiagram()).getDiagramSettings();

                        constructor.setAccessible(true);
                        f =  (Fig) constructor.newInstance(parameters);
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            throw new SAXException(e);
        } catch (IllegalAccessException e) {
            throw new SAXException(e);
        } catch (InstantiationException e) {
            throw new SAXException(e);
        } catch (InvocationTargetException e) {
            throw new SAXException(e);
        }

        // Fall back to GEF's handling if we couldn't find an appropriate
        // constructor
        if (f == null) {
            // TODO: Convert this to a warning or error when all the Figs
            // have been upgraded.
            LOG.debug("No ArgoUML constructor found for " + className
                    + " falling back to GEF's default constructors");
            f = constructFig(className, href, bounds);
        }
        
	return f;
    }
    
    /**
     * @param container
     * @param group
     * @param attributes
     * @return
     * @throws SAXException
     */
    private DefaultHandler getGroupHandler(Object container, FigGroup group,
            Attributes attributes) throws SAXException {
        if (container instanceof Container) {
            ((Container) container).addObject(group);
        }
        StringTokenizer st = new StringTokenizer(attributes
                .getValue("description"), ",;[] ");
        setAttrs(group, attributes);
        if (st.hasMoreElements()) {
            st.nextToken();
        }
        String xStr = null;
        String yStr = null;
        String wStr = null;
        String hStr = null;
        if (st.hasMoreElements()) {
            xStr = st.nextToken();
            yStr = st.nextToken();
            wStr = st.nextToken();
            hStr = st.nextToken();
        }
        if (xStr != null && !xStr.equals("")) {
            int x = Integer.parseInt(xStr);
            int y = Integer.parseInt(yStr);
            int w = Integer.parseInt(wStr);
            int h = Integer.parseInt(hStr);
            group.setBounds(x, y, w, h);
        }
        return new FigGroupHandler(this, group);
    }

    /**
     * Retrieve a bounds Rectangle from its description where the description is
     * in the form "anything[x,y,w,h]anything"
     * 
     * @param boundsDescription
     * @return a bounds Rectangle or null is invalid
     */
    Rectangle getBounds(String boundsDescription) {
        try {
            int bracketPosn = boundsDescription.indexOf('[');
            if (bracketPosn < 0)
                return null;
            boundsDescription = boundsDescription.substring(bracketPosn + 1);
            StringTokenizer st = new StringTokenizer(boundsDescription, ", ]");
            String xStr = null;
            String yStr = null;
            String wStr = null;
            String hStr = null;
            if (st.hasMoreElements()) {
                xStr = st.nextToken();
                yStr = st.nextToken();
                wStr = st.nextToken();
                hStr = st.nextToken();
            }
            if (xStr != null && !xStr.equals("")) {
                int x = Integer.parseInt(xStr);
                int y = Integer.parseInt(yStr);
                int w = Integer.parseInt(wStr);
                int h = Integer.parseInt(hStr);
                return new Rectangle(x, y, w, h);
            }
        } catch (Exception e) {
            LOG.warn("Exception extracting bounds from description", e);
        }
        return null;
    }
    
    
    /**
     * Save the newly created Diagram for use by the parser.  We take the 
     * opportunity to attach our default diagram settings to it so we'll have
     * them if needed when constructing Figs.
     * <p>
     * Diagrams are created in GEF's PGMLHandler.initDiagram() which is private
     * and can't be overridden.  Initialization sequence is:<ul> 
     * <li>load diagram class using name in PGML file
     * <li>instantiate using 0-arg constructor
     * <li>invoke this method (setDiagram(<newDiagramInstance))
     * <li>invoke diagram's initialize(Object owner) method
     * <li>diagram.setName()
     * <li>diagram.setScale()
     * <li>diagram.setShowSingleMultiplicity() 
     *     (?!why does GEF care about multiplicity?!)
     * </ul>
     * @param diagram the new diagram
     * @see org.tigris.gef.persistence.pgml.PGMLStackParser#setDiagram(org.tigris.gef.base.Diagram)
     */
    @Override
    public void setDiagram(Diagram diagram) {
        // TODO: We could generalize this to initialize more stuff if needed
        ((ArgoDiagram) diagram).setDiagramSettings(getDiagramSettings());
        super.setDiagram(diagram);
    }
    
    public DiagramSettings getDiagramSettings() {
        return diagramSettings;
    }
}
