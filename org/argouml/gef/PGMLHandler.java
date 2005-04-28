package org.argouml.gef;

import java.beans.PropertyVetoException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.tigris.gef.base.Diagram;

import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigEdge;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Handler for pgml elements, which represent entire diagram objects
 * in PGML files
 * @author Michael A. MacDonald
 */
public class PGMLHandler extends BaseHandler implements Container {
    private static final Log LOG = LogFactory.getLog(PGMLHandler.class);

    /**
     * The constructor for this handler creates the diagram object according
     * to the attributes associated with the pgml element and associates
     * it with the PGMLStackParser object that is parsing the file.  Handlers
     * of sub-elements use this association to find the diagram object.
     * @param parser The object that is parsing the PGML file
     * @param attrList The attributes identified by the SAX parser for the pgml
     * element.  The name, scale, description, and showSingleMultiplicity
     * attributes are used to construct the appropriate GED diagram object
     * and set its properties.
     */
    public PGMLHandler( PGMLStackParser parser, Attributes attrList)
    throws SAXException
    {
        super( parser);
        String name = attrList.getValue("name");
        LOG.info("Got a diagram name of " + name);
        String scale = attrList.getValue("scale");
        String clsName = attrList.getValue("description");
        LOG.info("Got a description of " + clsName);
        String showSingleMultiplicity = attrList.getValue("showSingleMultiplicity");
        try {
            if(clsName != null && !clsName.equals("")) {
                initDiagram(clsName);
            }

            Diagram diagram=getPGMLStackParser().getDiagram();

            if(name != null && !name.equals("")) {
                diagram.setName(name);
            }

            if(scale != null && !"".equals(scale)) {
                diagram.setScale(Double.parseDouble(scale));
            }

            if(showSingleMultiplicity != null && !"".equals(showSingleMultiplicity)) {
                diagram.setShowSingleMultiplicity(Boolean.valueOf(showSingleMultiplicity).booleanValue());
            }
        }
        catch(PropertyVetoException ex) {
            throw new SAXException(ex);
        }
    }
    private void initDiagram(String diagDescr) throws SAXException {
        String clsName = diagDescr;
        String initStr = null;
        int bar = diagDescr.indexOf("|");
        if (bar != -1) {
            clsName = diagDescr.substring(0, bar);
            initStr = diagDescr.substring(bar + 1);
        }

        try {
            Class cls = Class.forName(clsName);
            getPGMLStackParser().setDiagram( (Diagram)cls.newInstance());
            if(initStr != null && !initStr.equals("")) {
                getPGMLStackParser().getDiagram().initialize(
                    getPGMLStackParser().findOwner(initStr));
            }
        } catch(Exception ex) {
            throw new SAXException(ex);
        }
    }
    /**
     * Adds a Fig object to the diagram represented by the pgml element.
     * @param toAdd The Fig object to be added to the diagram.
     */
    public void addObject( Object toAdd)
    {
        Diagram diagram=getPGMLStackParser().getDiagram();
        diagram.add( (Fig)toAdd);
    }
}
