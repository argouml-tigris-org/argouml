// $Id$
// Copyright (c) 2005 The Regents of the University of California. All
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

package org.argouml.gef;

import java.beans.PropertyVetoException;

import org.apache.log4j.Logger;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.presentation.Fig;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Handler for pgml elements, which represent entire diagram objects
 * in PGML files.
 *
 * @author Michael A. MacDonald
 */
public class PGMLHandler extends BaseHandler implements Container {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(PGMLHandler.class);

    /**
     * The constructor for this handler creates the diagram object according
     * to the attributes associated with the pgml element and associates
     * it with the PGMLStackParser object that is parsing the file.  Handlers
     * of sub-elements use this association to find the diagram object.
     *
     * @param parser The object that is parsing the PGML file
     * @param attrList The attributes identified by the SAX parser for the pgml
     * element.  The name, scale, description, and showSingleMultiplicity
     * attributes are used to construct the appropriate GED diagram object
     * and set its properties.
     * @throws SAXException if something goes wrong.
     */
    public PGMLHandler(PGMLStackParser parser, Attributes attrList)
    	throws SAXException {
        super(parser);
        String name = attrList.getValue("name");
        LOG.info("Got a diagram name of " + name);
        String scale = attrList.getValue("scale");
        String clsName = attrList.getValue("description");
        LOG.info("Got a description of " + clsName);
        String showSingleMultiplicity =
            attrList.getValue("showSingleMultiplicity");
        try {
            if (clsName != null && !clsName.equals("")) {
                initDiagram(clsName);
            }

            Diagram diagram = getPGMLStackParser().getDiagram();

            if (name != null && !name.equals("")) {
                diagram.setName(name);
            }

            if (scale != null && !"".equals(scale)) {
                diagram.setScale(Double.parseDouble(scale));
            }

            if (showSingleMultiplicity != null
                    && !"".equals(showSingleMultiplicity)) {
                diagram.setShowSingleMultiplicity(
                        Boolean.valueOf(showSingleMultiplicity).booleanValue());
            }
        } catch (PropertyVetoException ex) {
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
            getPGMLStackParser().setDiagram((Diagram) cls.newInstance());
            if (initStr != null && !initStr.equals("")) {
                getPGMLStackParser().getDiagram().initialize(
                    getPGMLStackParser().findOwner(initStr));
            }
        } catch (Exception ex) {
            throw new SAXException(ex);
        }
    }
    /**
     * Adds a Fig object to the diagram represented by the pgml element.
     * @param toAdd The Fig object to be added to the diagram.
     */
    public void addObject(Object toAdd) {
        Diagram diagram = getPGMLStackParser().getDiagram();
        diagram.add((Fig) toAdd);
    }
}
