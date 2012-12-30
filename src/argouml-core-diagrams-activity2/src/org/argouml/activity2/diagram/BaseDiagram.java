/* $Id$
 *****************************************************************************
 * Copyright (c) 2010-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *****************************************************************************
 */

package org.argouml.activity2.diagram;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.argouml.kernel.Owned;
import org.argouml.model.Model;
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import org.argouml.uml.diagram.UmlDiagramRenderer;
import org.argouml.uml.diagram.ui.ActionSetMode;
import org.argouml.uml.diagram.ui.RadioAction;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;
import org.tigris.gef.base.ModeCreatePolyEdge;
import org.tigris.gef.graph.MutableGraphModel;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

abstract class BaseDiagram extends UMLDiagram implements Owned {

    private static final Logger LOG =
        Logger.getLogger(BaseDiagram.class.getName());

    final Object owner;

    BaseDiagram(Object owner) {
        super();
        this.owner = owner;
        MutableGraphModel gm = createGraphModel();
        setGraphModel(gm);

        // Create the layer
        LayerPerspective lay = new
            LayerPerspectiveMutable(this.getName(), gm);
        setLayer(lay);

        // Create the renderer
        UmlDiagramRenderer renderer = createDiagramRenderer();
        lay.setGraphNodeRenderer(renderer);
        lay.setGraphEdgeRenderer(renderer);
        
        LOG.log(Level.FINE, "Constructing diagram for {0}", owner);
    }

    public Object getOwner() {
        return owner;
    }

    abstract UmlDiagramRenderer createDiagramRenderer();
    abstract UMLMutableGraphSupport createGraphModel();

    private Map<String, Class<?>> metaTypeByName;
    private Map<Class<?>, String> nameByMetaType;

    protected Object[] getUmlActions() {
        try {
            final Document doc = getDocument();
            final Element element =
                getElement(doc.getDocumentElement(), "classes");
            final int size = element.getChildNodes().getLength();
            nameByMetaType = new HashMap<Class<?>, String>(size);
            metaTypeByName = new HashMap<String, Class<?>>(size);
            populateClassMaps(element, nameByMetaType, metaTypeByName);

            final Element toolbarElement =
                getElement(doc.getDocumentElement(), "toolbar");
            return getToolbarActions(toolbarElement);
        } catch (DOMException e) {
            LOG.log(Level.SEVERE, "", e);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "", e);
        } catch (ParserConfigurationException e) {
            LOG.log(Level.SEVERE, "", e);
        } catch (SAXException e) {
            LOG.log(Level.SEVERE, "", e);
        }
        return null;
    }

    /**
     * Get the single (or first) child Element of the given element
     * that has the given tag name.
     * @param element
     * @param tagName
     * @return the child element
     */
    private Element getElement(Element element, String tagName) {
        final NodeList nl = element.getElementsByTagName(tagName);
        if (nl.getLength() == 0) {
            return null;
        }
        return (Element) nl.item(0);
    }

    private Object[] getToolbarActions(Element toolbarNode) {

        final NodeList nl = toolbarNode.getChildNodes();

        List<Element> elements = new ArrayList<Element>();
        for (int i = 0; i < nl.getLength(); ++i) {
            final Node n = nl.item(i);
            if (n instanceof Element) {
                elements.add((Element) n);
            }
        }

        final Object[] toolbarActions = new Object[elements.size()];

        for (int i = 0; i < elements.size(); ++i) {
            final Element itemNode = elements.get(i);
            Object o;
            String style = itemNode.getNodeName();
            if (style.equals("dropdown")) {
                o = getToolbarActions(itemNode);
            } else if (style.equals("poly-edge")) {
                final String type = itemNode.getAttribute("type");
                final Class<?> metaType = metaTypeByName.get(type);
                o = getCreateEdgeAction(metaType);
            } else {
                final String type = itemNode.getAttribute("type");
                final Class<?> metaType = metaTypeByName.get(type);
                o = new CreateDiagramElementAction(
                        metaType,
                        style,
                        Model.getMetaTypes().getName(metaType),
                        this);
            }
            toolbarActions[i] = o;
        }
        return toolbarActions;
    }

    // TODO: This is currently duplicated from MetaDataCache - must find a
    // common place in model facade
    private void populateClassMaps(
            final Element classesNode,
            final Map<Class<?>, String> nameByMetaType,
            final Map<String, Class<?>> metaTypeByName) {
        final NodeList nl = classesNode.getElementsByTagName("class");
        for (int i = 0; i < nl.getLength(); ++i) {
            Element classNode = (Element) nl.item(i);
            String className = classNode.getTextContent();
            try {
                final String name =
                    classNode.getAttribute("name");
                Class<?> clazz = Class.forName(className);
                metaTypeByName.put(name, clazz);
                nameByMetaType.put(clazz, name);
            } catch (ClassNotFoundException e) {
                LOG.log(Level.SEVERE, "Class not found " + className, e);
            }
        }
    }

    /**
     * Get the diagram definition XML document
     * @return
     * @throws IOException
     * @throws DOMException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    private Document getDocument()
        throws IOException, DOMException,
            ParserConfigurationException, SAXException {
        final String filename = getDiagramXmlFile();
        InputStream inputStream =
            this.getClass().getClassLoader().getResourceAsStream(filename);
        InputSource inputSource = new InputSource(inputStream);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.parse(inputSource);
    }

    protected abstract String getDiagramXmlFile();

    protected Action getCreateEdgeAction(Object metaType) {
        String label = Model.getMetaTypes().getName(metaType);
        return new RadioAction(
                new ActionSetMode(
                        ModeCreatePolyEdge.class,
                        "edgeClass",
                        metaType,
                        label));
    }

    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#isRelocationAllowed(java.lang.Object)
     */
    public boolean isRelocationAllowed(Object base)  {
        return false;
    }

    public Collection getRelocationCandidates(Object root) {
        return Collections.EMPTY_LIST;
    }

    public boolean relocate(Object base) {
        return false;
    }
}
