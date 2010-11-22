/* $Id: $
 *****************************************************************************
 * Copyright (c) 2010 Contributors - see below
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

import java.awt.Rectangle;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.model.ActivityDiagram;
import org.argouml.model.Model;
import org.argouml.uml.diagram.DiagramElement;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import org.argouml.uml.diagram.UmlDiagramRenderer;
import org.argouml.uml.diagram.static_structure.ui.FigComment;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Diagram class for UML2 Activity Diagram
 * @author Bob Tarling
 */
public class UMLActivityDiagram extends BaseDiagram implements ActivityDiagram {
    
    private static final Logger LOG = Logger
        .getLogger(UMLActivityDiagram.class);
    
    private Map<String, Class<?>> metaTypeByName;
    private Map<Class<?>, String> nameByMetaType;
    
    UMLActivityDiagram(Object activity) {
        super(activity);
    }
    
    @Override
    UmlDiagramRenderer createDiagramRenderer() {
        return new ActivityDiagramRenderer();
    }

    @Override
    UMLMutableGraphSupport createGraphModel() {
        return new ActivityDiagramGraphModel(); 
    }

    @Override
    Object[] getNewEdgeTypes() {
        return new Object[] {
            Model.getMetaTypes().getControlFlow(),
            Model.getMetaTypes().getObjectFlow()
        };
    }

    @Override
    Object[] getNewNodeTypes() {
        return new Object[] {
            new Object[] {
                Model.getMetaTypes().getCallBehaviorAction(),
                Model.getMetaTypes().getCreateObjectAction(),
                Model.getMetaTypes().getDestroyObjectAction(),
            },
            Model.getMetaTypes().getAcceptEventAction(),
            Model.getMetaTypes().getSendSignalAction(),
            new Object[] {
                Model.getMetaTypes().getActivityParameterNode(),
                Model.getMetaTypes().getCentralBufferNode(),
                Model.getMetaTypes().getDataStoreNode(),
            },
            Model.getMetaTypes().getInputPin(),
            Model.getMetaTypes().getOutputPin(),
        };
    }
    
    private List<Object> getToolbarActions(Element toolbarNode) {
        List<Object> toolbarActions = new ArrayList<Object>();
        final NodeList nl = toolbarNode.getElementsByTagName("*");
        for (int i = 0; i < nl.getLength(); ++i) {
            final Element itemNode = (Element) nl.item(i);
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
            toolbarActions.add(o);
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
            Node classNode = nl.item(i);
            String className = classNode.getTextContent();
            try {
                final String name = 
                    classNode.getAttributes().getNamedItem("name").getNodeValue();
                Class<?> clazz = Class.forName(className);
                metaTypeByName.put(name, clazz);
                nameByMetaType.put(clazz, name);
            } catch (ClassNotFoundException e) {
                    LOG.error("Class not found " + className, e);
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
        final String filename;
        filename = "org/argouml/activity2/diagram/diagram.xml";
        InputStream inputStream = 
            this.getClass().getClassLoader().getResourceAsStream(filename);
        InputSource inputSource = new InputSource(inputStream);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.parse(inputSource);
    }
    
    @Override
    protected Object[] getUmlActions() {
        try {
            final Document doc = getDocument();
            final NodeList nl = doc.getDocumentElement().getChildNodes();
            for (int i = 0; i < nl.getLength(); ++i) {
                final Node n = nl.item(i);
                if (n.getNodeName().equals("classes")) {
                    final int size = n.getChildNodes().getLength();
                    nameByMetaType = new HashMap<Class<?>, String>(size);
                    metaTypeByName = new HashMap<String, Class<?>>(size);
                    populateClassMaps((Element) n, nameByMetaType, metaTypeByName);
                } else if (n.getNodeName().equals("toolbar")) {
                    List<Object> actions = getToolbarActions((Element) n);
                    return actions.toArray();
                }
            }
        } catch (DOMException e) {
            LOG.error("", e);
        } catch (IOException e) {
            LOG.error("", e);
        } catch (ParserConfigurationException e) {
            LOG.error("", e);
        } catch (SAXException e) {
            LOG.error("", e);
        }
        return null;
    }
    
    


    @Override
    public void initialize(Object owner) {
        super.initialize(owner);
        ActivityDiagramGraphModel gm =
            (ActivityDiagramGraphModel) getGraphModel();
    }

    @Override
    public String getLabelName() {
        return Translator.localize("label.activity-diagram");
    }
    
    @Override
    public boolean doesAccept(Object objectToAccept) {
        if (Model.getFacade().isAComment(objectToAccept)
                || Model.getFacade().isAActivityEdge(objectToAccept) 
                || Model.getFacade().isAActivityNode(objectToAccept) ) {
            return true;
        }
        return false;
    }
    
    public DiagramElement createDiagramElement(
            final Object modelElement,
            final Rectangle bounds) {
        
        FigNodeModelElement figNode = null;
        
        DiagramSettings settings = getDiagramSettings();
        
        if (Model.getFacade().isAActivityNode(modelElement)) {
            figNode = new FigBaseNode(modelElement, bounds, settings);
            final String style;
            if (Model.getFacade().isAObjectNode(modelElement)) {
                style="rect";
            } else if (Model.getFacade().isASendSignalAction(modelElement)) {
                style="pentagon";
            } else if (Model.getFacade().isAAcceptEventAction(modelElement)) {
                style="concave-pentagon";
            } else {
                style="rrect";
            }
            DiagramElementBuilder.buildDiagramElement((FigBaseNode) figNode, style, modelElement, settings);
        } else if (Model.getFacade().isAComment(modelElement)) {
            figNode = new FigComment(modelElement, bounds, settings);
        }
        
        return figNode;
    }
}
