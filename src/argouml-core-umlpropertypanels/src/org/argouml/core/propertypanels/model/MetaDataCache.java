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

package org.argouml.core.propertypanels.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.argouml.model.Model;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * The cache of property panel metadata.
 *
 * @author Bob Tarling
 */
public class MetaDataCache {

    private static final Logger LOG =
        Logger.getLogger(MetaDataCache.class.getName());

    private Map<Class<?>, PanelData> cache =
        new HashMap<Class<?>, PanelData>();

    private Map<String, Class<?>> metaTypeByName;
    private Map<Class<?>, String> nameByMetaType;

    public MetaDataCache() throws Exception {
        Document doc = getDocument();
        NodeList nl = doc.getDocumentElement().getChildNodes();
        for (int i = 0; i < nl.getLength(); ++i) {
            Node n = nl.item(i);
            if (n.getNodeName().equals("classes")) {
        	final int size = n.getChildNodes().getLength();
        	nameByMetaType = new HashMap<Class<?>, String>(size);
        	metaTypeByName = new HashMap<String, Class<?>>(size);
        	populateClassMaps((Element) n, nameByMetaType, metaTypeByName);
            } else if (n.getNodeName().equals("panels")) {
        	cache = getPanels((Element) n);
            }
        }
    }

    public PanelData get(Class<?> clazz) {
	Class<?>[] interfaces = clazz.getInterfaces();

	for (Class interfaze : interfaces) {
	    PanelData pd = cache.get(interfaze);
	    if (pd != null) {
		return pd;
	    }
	}
        return null;
    }

    private Document getDocument() throws IOException, DOMException, ParserConfigurationException, SAXException {
        final String filename;
        if (Model.getFacade().getUmlVersion().charAt(0) == '2') {
            filename = "org/argouml/core/propertypanels/model/metamodel2.xml";
        } else {
            filename = "org/argouml/core/propertypanels/model/metamodel.xml";
        }
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(filename);
        InputSource inputSource = new InputSource(inputStream);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.parse(inputSource);
    }

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
                LOG.log(Level.SEVERE, "Class not found " + className, e);
            }
        }
    }

    private Map<Class<?>, PanelData> getPanels(Element panelsNode) {

        final Map<Class<?>, PanelData> map =
            new HashMap<Class<?>, PanelData>();

        final NodeList panelNodes = panelsNode.getElementsByTagName("panel");
        for (int i = 0; i < panelNodes.getLength(); ++i) {

            Element panelNode = (Element) panelNodes.item(i);
            final String name = panelNode.getAttribute("name");

            Class<?> clazz = metaTypeByName.get(name);

            if (clazz == null) {
                LOG.log(Level.WARNING,
                        "No class name translation found for panel: " + name);
            } else {
                final List<Class<?>> newChildTypes =
                    stringToMetaTypes(panelNode.getAttribute("new-child"));
                final List<Class<?>> newSiblingTypes =
                    stringToMetaTypes(panelNode.getAttribute("new-sibling"));

                final boolean siblingNavigation =
                    "true".equals(panelNode.getAttribute("navigate-sibling"));

                final PanelData pm =
                    new PanelData(clazz, name, newChildTypes, newSiblingTypes, siblingNavigation);
                map.put(clazz, pm);

                final NodeList controlNodes =
                    panelNode.getElementsByTagName("*");
                for (int j = 0; j < controlNodes.getLength(); ++j) {
                    Element controlNode = (Element) controlNodes.item(j);

                    final String propertyName =
                	controlNode.getAttribute("name");
                    final String label = controlNode.getAttribute("label");

                    final ControlData controlData =
                        new ControlData(controlNode.getTagName(), propertyName, label);

                    final List<Class<?>> types =
                	stringToMetaTypes(controlNode.getAttribute("type"));
                    for (Class<?> metaType : types) {
                        controlData.addType(metaType);
                    }

                    if (controlNode.getTagName().equals("checkgroup")) {
                        addCheckboxes(controlData, controlNode);
                    }
                    pm.addControlData(controlData);
                }
            }
        }

        return map;
    }

    /**
     * Takes as input a string of comma separated metatypes (e.g.
     * "Class,Interface,Attribute") and converts it to a list of classes of
     * the appropriate type.
     * @param typesString
     * @return
     */
    private List<Class<?>> stringToMetaTypes(String typesString) {
	List<Class<?>> classes = new ArrayList<Class<?>>();
        StringTokenizer st = new StringTokenizer(typesString, ",");
        while (st.hasMoreTokens()) {
            classes.add(metaTypeByName.get(st.nextToken()));
        }
        return classes;
    }

    private void addCheckboxes(ControlData controlData, Element controlElement) {
        final NodeList checkBoxElements =
            controlElement.getElementsByTagName("checkbox");
        for (int i = 0; i < checkBoxElements.getLength(); ++i) {
            Element cbNode = (Element) checkBoxElements.item(i);

            final String checkBoxType =
        	cbNode.getAttributes().getNamedItem("type").getNodeValue();
            final String checkBoxName =
    	        cbNode.getAttributes().getNamedItem("name").getNodeValue();

            CheckBoxData cbd =
        	new CheckBoxData(metaTypeByName.get(checkBoxType), checkBoxName);
            controlData.addCheckbox(cbd);
        }
    }
}
