/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michiel Van Der Wulp
 *    Bob Tarling
 *    Thomas Neustupny
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */
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
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.model.Model;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramEdgeSettings;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.PathContainer;
import org.argouml.uml.diagram.StereotypeContainer;
import org.argouml.uml.diagram.VisibilityContainer;
import org.argouml.uml.diagram.ui.FigCompartmentBox;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.FigEdgePort;
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
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

// TODO: Move to Diagram subsystem?

/**
 * The PGML Parser.
 * <p>
 *
 * This replaces much of the identically named class from GEF.
 */
class PGMLStackParser extends org.tigris.gef.persistence.pgml.PGMLStackParser {

    private static final Logger LOG =
        Logger.getLogger(PGMLStackParser.class.getName());

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
     * HandlerStack, Object, String, String, String, Attributes)
     */
    @Override
    public DefaultHandler getHandler(HandlerStack stack, Object container,
            String uri, String localname, String qname, Attributes attributes)
        throws SAXException {

        String href = attributes.getValue("href");
        Object owner = null;

        if (href != null) {
            owner = findOwner(href);
            if (owner == null) {
                LOG.log(Level.WARNING, "Found href of " + href
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

        DefaultHandler handler = super.getHandler(stack, container, uri,
                localname, qname, attributes);

        if (handler instanceof FigEdgeHandler) {
            return new org.argouml.persistence.FigEdgeHandler(this,
                    ((FigEdgeHandler) handler).getFigEdge());
        }

        return handler;

    }

    /*
     * @see org.tigris.gef.persistence.pgml.PGMLStackParser#setAttrs(
     * org.tigris.gef.presentation.Fig, org.xml.sax.Attributes)
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
                LOG.log(Level.SEVERE, "Can't find href of " + href);
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
                LOG.log(Level.FINE,
                        "Ignoring href on {0} as it's already set",
                        f.getClass().getName());
            }
        }
    }

    /**
     * The StringTokenizer is expected to be positioned at the start of a string
     * of style identifiers in the format name=value;name=value;name=value....
     * Each name value pair is interpreted and the Fig configured accordingly.
     * The value is optional and will default to a value applicable for its
     * name. The current applicable names are operationsVisible and
     * attributesVisible and are used to show or hide the compartments within
     * Class and Interface. The default values are true.
     *
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
     * Set the fig style attributes.
     * <p>
     *
     * TODO: This should move into the render factories as described in issue
     * 859.
     *
     * @param fig the fig to style.
     * @param attributeMap a map of name value pairs
     */
    private void setStyleAttributes(Fig fig, Map<String, String> attributeMap) {

        for (Map.Entry<String, String> entry : attributeMap.entrySet()) {
            final String name = entry.getKey();
            final String value = entry.getValue();

            if (fig instanceof FigCompartmentBox) {
                FigCompartmentBox fcb = (FigCompartmentBox) fig;
                if ("operationsVisible".equals(name)) {
                    fcb.showCompartment(Model.getMetaTypes().getOperation(),
                            value.equalsIgnoreCase("true"));
                } else if ("attributesVisible".equals(name)) {
                    fcb.showCompartment(Model.getMetaTypes().getAttribute(),
                            value.equalsIgnoreCase("true"));
                } else if ("enumerationLiteralsVisible".equals(name)) {
                    fcb.showCompartment(Model.getMetaTypes()
                            .getEnumerationLiteral(), value
                            .equalsIgnoreCase("true"));
                } else if ("extensionPointVisible".equals(name)) {
                    fcb.showCompartment(Model.getMetaTypes()
                            .getExtensionPoint(), value
                            .equalsIgnoreCase("true"));
                }
            }
            if ("stereotypeVisible".equals(name)) {
                ((StereotypeContainer) fig).setStereotypeVisible(value
                        .equalsIgnoreCase("true"));
            } else if ("visibilityVisible".equals(name)) {
                ((VisibilityContainer) fig).setVisibilityVisible(value
                        .equalsIgnoreCase("true"));
            } else if ("pathVisible".equals(name)) {
                ((PathContainer) fig).setPathVisible(value
                        .equalsIgnoreCase("true"));
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

        InputStream stream = is.getByteStream();
        if (stream == null) {
            try {
                // happens when 'is' comes from a zip file
                URL url = new URL(is.getSystemId());
                stream = url.openStream();
                closeStream = true;
            } catch (Exception e) {
                // continue with null stream, readDiagram(...) will take care of
                // it
            }
        }
        return (ArgoDiagram) readDiagram(stream, closeStream);
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
     * This is called when all nodes and edges have been read and placed on the
     * diagram. This method then attaches the edges to the correct node,
     * including the nodes contained within edges allowing edge to edge
     * connections for comment edges, association classes and dependencies.
     *
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
            final FigNode sourceFigNode = getFigNode(edgeData
                    .getSourceFigNodeId());
            final FigNode destFigNode = getFigNode(edgeData.getDestFigNodeId());

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

            if (sourcePortFig == null || destPortFig == null
                    || sourceFigNode == null || destFigNode == null) {
                LOG.log(Level.SEVERE,
                        "Can't find nodes for FigEdge: " + edge.getId() + ":"
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
     *
     * @param figEdge The FigEdge
     * @param sourcePortFigId The id of the source port
     * @param destPortFigId The id of the destination port
     * @param sourceFigNodeId The id of the source node
     * @param destFigNodeId The id of the destination node
     */
    public void addFigEdge(final FigEdge figEdge, final String sourcePortFigId,
            final String destPortFigId, final String sourceFigNodeId,
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
     * @throws IllegalStateException if the figId supplied is not of a FigNode
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
                throw new IllegalStateException("Can't find a FigNode with id "
                        + figId);
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
                LOG.log(Level.SEVERE,
                        "FigID " + figId + " is not a node, edge ignored");
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
     * The data from an edge extracted from the PGML before we can guarantee all
     * the nodes have been constructed. This stores the FigEdge and the id's of
     * the nodes to connect to later. If the nodes are not known then the ports
     * are returned instead.
     */
    private class EdgeData {
        private final FigEdge figEdge;

        private final String sourcePortFigId;

        private final String destPortFigId;

        private final String sourceFigNodeId;

        private final String destFigNodeId;

        /**
         * Constructor
         *
         * @param edge The FigEdge
         * @param sourcePortId The id of the source port
         * @param destPortId The id of the destination port
         * @param sourceNodeId The id of the source node
         * @param destNodeId The id of the destination node
         */
        public EdgeData(FigEdge edge, String sourcePortId, String destPortId,
                String sourceNodeId, String destNodeId) {
            if (sourcePortId == null || destPortId == null) {
                throw new IllegalArgumentException(
                        "source port and dest port must not be null"
                                + " source = " + sourcePortId + " dest = "
                                + destPortId + " figEdge = " + edge);
            }
            this.figEdge = edge;
            this.sourcePortFigId = sourcePortId;
            this.destPortFigId = destPortId;
            this.sourceFigNodeId = sourceNodeId != null ? sourceNodeId
                    : sourcePortId;
            this.destFigNodeId = destNodeId != null ? destNodeId : destPortId;
        }

        /**
         * Get the id of the destination FigNode
         *
         * @return the id
         */
        public String getDestFigNodeId() {
            return destFigNodeId;
        }

        /**
         * Get the id of the destination port
         *
         * @return the id
         */
        public String getDestPortFigId() {
            return destPortFigId;
        }

        /**
         * Get the FigEdge
         *
         * @return the FigEdge
         */
        public FigEdge getFigEdge() {
            return figEdge;
        }

        /**
         * Get the id of the source FigNode
         *
         * @return the id
         */
        public String getSourceFigNodeId() {
            return sourceFigNodeId;
        }

        /**
         * Get the id of the source port
         *
         * @return the id
         */
        public String getSourcePortFigId() {
            return sourcePortFigId;
        }
    }

    /**
     * Construct a new instance of the named Fig with the owner represented by
     * the given href and the bounds parsed from the PGML file. We look for
     * constructors of the form Fig(Object owner, Rectangle bounds,
     * DiagramSettings settings) which is typically used for subclasses of
     * FigNodeModelElement, then Fig(Object owner, DiagramSettings settings)
     * which is used for subclasses of FigEdgeModelElement.
     * <p>
     * If we fail to find any of the constructors that we know about, we'll call
     * GEF's version of this method to see if it can find a constructor.
     *
     * @param className fully qualified name of class to instantiate
     * @param href string representing UUID of owning element
     * @param bounds position and size of figure
     * @return
     * @throws SAXException
     * @see org.tigris.gef.persistence.pgml.PGMLStackParser#constructFig(java.lang.String,
     *      java.lang.String, java.awt.Rectangle)
     */
    @Override
    protected Fig constructFig(final String className, final String href,
            final Rectangle bounds, final Attributes attributes)
        throws SAXException {

        final DiagramSettings oldSettings =
            ((ArgoDiagram) getDiagram()).getDiagramSettings();

        Fig f = null;
        try {
            Class figClass = Class.forName(className);

            final Constructor[] constructors = figClass.getConstructors();

            // We are looking first to match with 3 different constructor
            // types. We would not expect a Fig to have any mix of these.
            // Any constructor other than these should be deprecated so we
            // look for these first.
            // Fig(DiagramEdgeSettings, DiagramSettings)
            // Fig(Object, Rectangle, DiagramSettings)
            // Fig(Rectangle, DiagramSettings)
            for (Constructor constructor : constructors) {
                Class[] parameterTypes = constructor.getParameterTypes();
                if (parameterTypes.length == 3
                        && parameterTypes[0].equals(Object.class)
                        && parameterTypes[1].equals(Rectangle.class)
                        && parameterTypes[2].equals(DiagramSettings.class)) {
                    // FigNodeModelElements should match here
                    final Object parameters[] = new Object[3];
                    final Object owner = getOwner(className, href);
                    if (owner == null) {
                        return null;
                    }
                    parameters[0] = owner;
                    parameters[1] = bounds;
                    parameters[2] = oldSettings;

                    constructor.setAccessible(true);
                    f = (Fig) constructor.newInstance(parameters);
                } else if (parameterTypes.length == 2
                        && parameterTypes[0].equals(DiagramEdgeSettings.class)
                        && parameterTypes[1].equals(DiagramSettings.class)) {
                    // FigEdgeModelElements should match here (they have no
                    // bounds)
                    final Object parameters[] = new Object[2];
                    final Object owner = getOwner(className, href);
                    if (owner == null) {
                        return null;
                    }

                    String sourceUuid = attributes.getValue("sourceConnector");
                    String destinationUuid = attributes
                            .getValue("destConnector");

                    final Object source;
                    final Object destination;
                    if (sourceUuid != null && destinationUuid != null) {
                        source = findOwner(sourceUuid);
                        destination = findOwner(destinationUuid);
                    } else {
                        source = null;
                        destination = null;
                    }

                    DiagramEdgeSettings newSettings = new DiagramEdgeSettings(
                            owner, source, destination);
                    parameters[0] = newSettings;
                    parameters[1] = oldSettings;

                    constructor.setAccessible(true);
                    f = (Fig) constructor.newInstance(parameters);
                } else if (parameterTypes.length == 2
                        && parameterTypes[0].equals(Rectangle.class)
                        && parameterTypes[1].equals(DiagramSettings.class)) {
                    // A FigNodeModelElement with no owner should match here
                    // TODO: This is a temporary solution due to FigPool
                    // extending
                    // FigNodeModelElement when in fact it should not do so.
                    Object parameters[] = new Object[2];
                    parameters[0] = bounds;
                    parameters[1] = oldSettings;

                    constructor.setAccessible(true);
                    f = (Fig) constructor.newInstance(parameters);
                }
            }
            if (f == null) {
                // If no Fig was created by the code above then we must go
                // look for the old style constructor that should have fallen
                // into disuse by now.
                // Fig(Object, Rectangle, DiagramSettings)
                // All of these constructors should have been deprecated
                // at least and replaced with the new signature. This is
                // here for paranoia only until all Figs have been reviewed.
                for (Constructor constructor : constructors) {
                    Class[] parameterTypes = constructor.getParameterTypes();
                    if (parameterTypes.length == 2
                            && parameterTypes[0].equals(Object.class)
                            && parameterTypes[1].equals(DiagramSettings.class)) {
                        Object parameters[] = new Object[2];

                        final Object owner = getOwner(className, href);
                        // currently FigEdgeNote can be passed null
//                        if (owner == null) {
//                            return null;
//                        }
                        parameters[0] = owner;
                        parameters[1] = oldSettings;

                        constructor.setAccessible(true);
                        f = (Fig) constructor.newInstance(parameters);
                        LOG.log(Level.WARNING,
                                "Fig created by old style constructor "
                                + f.getClass().getName());
                        break;
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
            LOG.log(Level.WARNING,
                    "No ArgoUML constructor found for " + className
                    + " falling back to GEF's default constructors");
            f = super.constructFig(className, href, bounds, attributes);
        }

        return f;
    }

    /**
     * Given the href extracted from the PGML return the model element with that
     * uuid.
     *
     * @param className Used only for logging should the href not be found
     * @param href The href
     * @return
     */
    private Object getOwner(String className, String id) {
        if (id == null) {
            LOG.log(Level.WARNING,
                    "There is no href attribute provided for a " + className
                    + " so the diagram element is ignored on load");
            return null;
        }
        final Object owner = findOwner(id);
        if (owner == null) {
            LOG.log(Level.WARNING,
                    "The href " + id + " is not found for a " + className
                    + " so the diagram element is ignored on load");
            return null;
        }
        return owner;
    }

    /**
     * Save the newly created Diagram for use by the parser. We take the
     * opportunity to attach our default diagram settings to it so we'll have
     * them if needed when constructing Figs.
     * <p>
     * Diagrams are created in GEF's PGMLHandler.initDiagram() which is private
     * and can't be overridden. Initialization sequence is:
     * <ul>
     * <li>load diagram class using name in PGML file
     * <li>instantiate using 0-arg constructor
     * <li>invoke this method (setDiagram(<newDiagramInstance))
     * <li>invoke diagram's initialize(Object owner) method
     * <li>diagram.setName()
     * <li>diagram.setScale()
     * <li>diagram.setShowSingleMultiplicity() (?!why does GEF care about
     * multiplicity?!)
     * </ul>
     *
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
