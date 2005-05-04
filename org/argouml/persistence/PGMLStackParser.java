package org.argouml.persistence;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import java.util.StringTokenizer;

import org.argouml.uml.diagram.ui.AttributesCompartmentContainer;
import org.argouml.uml.diagram.ui.OperationsCompartmentContainer;

import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigNode;

import org.argouml.gef.HandlerStack;
import org.argouml.gef.FigGroupHandler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

public class PGMLStackParser extends org.argouml.gef.PGMLStackParser {

    private static final Logger LOG = Logger.getLogger(PGMLStackParser.class);

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
    }

    /**
     * @see org.argouml.gef.HandlerFactory#getHandler
     */
    public DefaultHandler getHandler( HandlerStack stack,
                                             Object container,
                                             String uri,
                                             String localname,
                                             String qname,
                                             Attributes attributes)
            throws SAXException {
/*
        // Ignore things within nodes
        if ( qname.equals( "group") && container instanceof
             FigGroupHandler)
        {
            FigGroup group=((FigGroupHandler)container).getFigGroup();
            if ( group instanceof AttributesCompartmentContainer ||
                group instanceof OperationsCompartmentContainer)
            {
                String description=attributes.getValue( "description");
                if ( description.indexOf( "Compartment")!= -1)
                    return null;
            }
        }
        // Don't load text within node
        if ( qname.equals( "text") && container instanceof
             FigGroupHandler)
        {
            FigGroup group=((FigGroupHandler)container).getFigGroup();
            if ( group instanceof FigNode)
                return null;
        }
 */
        if (container instanceof FigGroupHandler) {
            FigGroup group=((FigGroupHandler)container).getFigGroup();
            if (group instanceof FigNode && !qname.equals( "private")) {
                return null;
            }
        }
        DefaultHandler result=super.getHandler(
            stack, container, uri, localname, qname, attributes);
        
        return result;
    }
    
    public void setAttrs(Fig f, Attributes attrList) throws SAXException {
        if (f instanceof FigGroup) {
            FigGroup group=(FigGroup)f;
            String clsNameBounds = attrList.getValue("description");
            if ( clsNameBounds!=null) {
                StringTokenizer st = new StringTokenizer(clsNameBounds,
                    ",;[] ");
                // Discard class name, x y w h
                if ( st.hasMoreElements()) st.nextToken();
                if ( st.hasMoreElements()) st.nextToken();
                if ( st.hasMoreElements()) st.nextToken();
                if ( st.hasMoreElements()) st.nextToken();
                if ( st.hasMoreElements()) st.nextToken();
                
                Map attributeMap = interpretStyle(st);
                setStyleAttributes( group, attributeMap);
            }
        }

        super.setAttrs(f, attrList);
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
            }
        }
    }
}
