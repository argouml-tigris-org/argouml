/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2009 The Regents of the University of California. All
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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.uml.diagram.ui.PathItemPlacement;
import org.argouml.util.IItemUID;
import org.argouml.util.ItemUID;
import org.tigris.gef.base.PathItemPlacementStrategy;
import org.tigris.gef.persistence.pgml.Container;
import org.tigris.gef.persistence.pgml.FigEdgeHandler;
import org.tigris.gef.persistence.pgml.FigGroupHandler;
import org.tigris.gef.persistence.pgml.PGMLHandler;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Will set the ItemUID for objects represented by
 * PGML elements that contain private elements that have
 * ItemUID assignments in them.<p>
 *
 * Currently, there are three possibilities: ArgoDiagram,
 * FigNode, FigEdge
 */
class PrivateHandler
    extends org.tigris.gef.persistence.pgml.PrivateHandler {

    private Container container;

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(PrivateHandler.class.getName());

    /**
     * The constructor.
     *
     * @param parser
     * @param cont
     */
    public PrivateHandler(PGMLStackParser parser, Container cont) {
        super(parser, cont);
        container = cont;
    }

    /**
     * If the containing object is a type for which the private element
     * might contain an ItemUID, extract the ItemUID if it exists and assign it
     * to the object.
     *
     * @param contents
     * @exception SAXException
     */
    public void gotElement(String contents)
        throws SAXException {

        if (container instanceof PGMLHandler) {
            Object o = getPGMLStackParser().getDiagram();
            if (o instanceof IItemUID) {
                ItemUID uid = getItemUID(contents);
                if (uid != null) {
                    ((IItemUID) o).setItemUID(uid);
                }
            }
            // No other uses of string in PGMLHandler
            return;
        }

        if (container instanceof FigGroupHandler) {
            Object o = ((FigGroupHandler) container).getFigGroup();
            if (o instanceof IItemUID) {
                ItemUID uid = getItemUID(contents);
                if (uid != null) {
                    ((IItemUID) o).setItemUID(uid);
                }
            }
        }

        if (container instanceof FigEdgeHandler) {
            Object o = ((FigEdgeHandler) container).getFigEdge();
            if (o instanceof IItemUID) {
                ItemUID uid = getItemUID(contents);
                if (uid != null) {
                    ((IItemUID) o).setItemUID(uid);
                }
            }
        }

        // Handle other uses of <private> contents
        super.gotElement(contents);
    }

    /**
     * Process starting elements within the private tag.
     * This method handles all attributes within tags within private methods.
     * The only specific tags we handle here at the moment are pathitems.
     *
     * The strategy for handling pathitems is as follows:
     * <ul>
     *  <li>Data is saved for each path item using one <argouml:pathitem ... />
     *      tag per path item.
     *  <li>The code that defines what is stored is in
     *      org.argouml.persistence.PGML.tee
     *  <li>Each <argouml:pathitem> tag stores
     *  <ul>
     *    <li>The class name of the PathItemPlacementStrategy
     *    <li>The class name of the fig which it places.
     *    <li>The href of the model element which owns the fig being placed.
     *    <li>The angle of the placement vector (PathItemPlacement.angle)
     *    <li>The distance along the displacement vector to place the fig
     *        (PathItemPlacement.vectorOffset).
     *  </ul>
     *  </li>
     *  <li>No specific data is stored to match pathitem tags to the
     *      diagram figs which they control.
     *  <li>The matching during file load depends entirely on
     *      there being a unique figclassname and ownerhref combination
     *      for each pathitem on the diagram.  For example, For a
     *      FigAssociation, the main label is a FigTextGroup, and it's
     *      owner is assigned to the Association.  This combination is
     *      unique, and is used to match the parsed pathitem data back
     *      to the instantiated PathItemPlacement.
     *      Another example is the source multiplicity, which is a
     *      FigMultiplicity, and it's owner is assigned to the
     *      source model element.
     *      In each case, the combination is unique, so there is only
     *      one pathitem that matches when rebuilding the diagram.
     *  </ul>
     *
     * @param uri
     * @param localname
     * @param qname
     * @param attributes
     * @throws SAXException
     * @see org.tigris.gef.persistence.pgml.BaseHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(String uri, String localname, String qname,
            Attributes attributes) throws SAXException {
        if ("argouml:pathitem".equals(qname)
                && container instanceof FigEdgeHandler) {
            String classname = attributes.getValue("classname");
            String figclassname =
                attributes.getValue("figclassname");
            String ownerhref = attributes.getValue("ownerhref");
            String angle = attributes.getValue("angle");
            String offset = attributes.getValue("offset");
            if ( classname != null
                    && figclassname != null
                    && ownerhref != null
                    && angle != null
                    && offset != null ) {
                // Method 2: (assign data immediately, see end of file).
                // TODO: if we ever want to extend PathItemPlacement,
                // we should modify this, so that we also recognise any
                // subclass of PathItemPlacement.
                // Is the class name a PathItemPlacment?
                // TODO: Use class reference to make this dependency obvious
                if ("org.argouml.uml.diagram.ui.PathItemPlacement".equals(
                        classname)) {
                    PathItemPlacementStrategy pips
                        = getPips(figclassname, ownerhref);
                    // Sanity check - the returned path item placement
                    // strategy should match the one in the UML.
                    // If it doesn't, it could be that the UML was
                    // created with an older argo version, and the new
                    // argo version use a different placement strategy.
                    // If they don't match, just use the default.
                    if (pips != null
                            && classname.equals(pips.getClass().getName())) {
                        // Now we're into processing each specific path
                        // item strategy.
                        // At the moment, we only know PathItemPlacement
                        if (pips instanceof PathItemPlacement) {
                            PathItemPlacement pip =
                                (PathItemPlacement) pips;
                            pip.setDisplacementVector(
                                    Double.parseDouble(angle),
                                    Integer.parseInt(offset));
                        }
                        // Continue (future PathItemPlacementStrategy impl)
                        //else if (...) {
                        //}
                    }
                    // If the PathItemPlacement was unknown, leave the
                    // diagram with the default settings.
                    else {
                        LOG.log(Level.WARNING,
                                "PGML stored pathitem class name does "
                                + "not match the class name on the "
                                + "diagram. Label position will revert "
                                + "to defaults.");
                    }
                }
            }
            // If any of the values are null, ignore the element.
            else {
                LOG.log(Level.WARNING, "Could not find all attributes for <"
                        + qname + "> tag, ignoring.");
                //System.out.println("Error - one of these is null:"
                //        + "classname=" + classname
                //        + " figclassname=" + figclassname
                //        + " ownerhref=" + ownerhref
                //        + " angle=" + angle
                //        + " offset=" + offset);
            }
        }
        super.startElement(uri, localname, qname, attributes);
    }

    /**
     * Finds the path item placement strategy for a sub Fig, by its class name,
     * and it's owner href.
     * @param figclassname The class name of the fig being placed.
     * @param ownerhref The href of the owner of the fig being placed.
     * @return The path item placement strategy.
     */
    private PathItemPlacementStrategy getPips(String figclassname,
            String ownerhref) {
        if (container instanceof FigEdgeHandler) {
            FigEdge fe = ((FigEdgeHandler) container).getFigEdge();
            Object owner = getPGMLStackParser().findOwner(ownerhref);

            for (Object o : fe.getPathItemFigs()) {
                Fig f = (Fig) o;
                // For a match to be found, it has to have the same
                // owner, and the same long class name.
                if (owner.equals(f.getOwner())
                        && figclassname.equals(f.getClass().getName())) {
                    //System.out.println("MATCHED! " + figclassname);
                    return fe.getPathItemPlacementStrategy(f);
                }
            }
        }
        LOG.log(Level.WARNING,
                "Could not load path item for fig '" + figclassname
                + "', using default placement.");
        return null;
    }

    /**
     * Determine if the string contains an ItemUID.
     *
     * @return a newly created ItemUID (or <code>null</code>).
     */
    private ItemUID getItemUID(String privateContents) {
        StringTokenizer st = new StringTokenizer(privateContents, "\n");

        while (st.hasMoreElements()) {
            String str = st.nextToken();
            NameVal nval = splitNameVal(str);

            if (nval != null) {
                if (LOG.isLoggable(Level.FINE)) {
                    LOG.log(Level.FINE, "Private Element: \"" + nval.getName()
                              + "\" \"" + nval.getValue() + "\"");
                }
                if ("ItemUID".equals(nval.getName())
                    && nval.getValue().length() > 0) {
                    return new ItemUID(nval.getValue());
                }
            }
        }
        return null;
    }

    /**
     * Utility class to pair a name and a value String together.
     */
    static class NameVal {
        private String name;
        private String value;

        /**
         * The constructor.
         *
         * @param n the name
         * @param v the value
         */
        NameVal(String n, String v) {
            name = n.trim();
            value = v.trim();
        }

        /**
         * @return returns the name
         */
        String getName() {
            return name;
        }

        /**
         * @return returns the value
         */
        String getValue() {
            return value;
        }
    }

    /**
     * Splits a name value pair into a NameVal instance. A name value pair is
     * a String on the form &lt; name = ["] value ["] &gt;.
     *
     * @param str A String with a name value pair.
     * @return A NameVal, or null if they could not be split.
     */
    protected NameVal splitNameVal(String str) {
        NameVal rv = null;
        int lqpos, rqpos;
        int eqpos = str.indexOf('=');

        if (eqpos < 0) {
            return null;
        }

        lqpos = str.indexOf('"', eqpos);
        rqpos = str.lastIndexOf('"');

        if (lqpos < 0 || rqpos <= lqpos) {
            return null;
        }

        rv =
            new NameVal(str.substring(0, eqpos),
                str.substring(lqpos + 1, rqpos));

        return rv;
    }
}

// An alternative implementation of the parsing of pathitems is to collect
// everything at the start, then iterate through it all at the end.
// The code below does this - it works, but it is currently not used,
// since it is a unnecessarily complicated.
// There are probably better ways to implement this than using an
// ArrayList of Hashtables.
// see option 1 in
// http://argouml.tigris.org/issues/show_bug.cgi?id=1048#desc66
//

///**
// * A list of the path item attributes for this container.
// * The list is populated during parsing, them processed at endElement()
// */
//private List<Hashtable<String, String>> pathItemAttrs =
//    new ArrayList<Hashtable<String, String>>();

// This code has to go within the startElement block after the strings
// have been matched.

//// Method 1:
//// (collect data and assign later in endElement() method).
//Hashtable<String, String> ht =
//    new Hashtable<String, String>();
//ht.put("classname", classname);
//ht.put("figclassname", figclassname);
//ht.put("ownerhref", ownerhref);
//ht.put("angle", angle);
//ht.put("offset", offset);
//pathItemAttrs.add(ht);

//public void endElement(String uri, String localname, String qname)
//throws SAXException {
////System.out.print("Got endElement: "
////        + "uri='" + uri + "'\n"
////        + "localname='" + localname + "'\n"
////        + "qname='" + qname + "'\n"
////);
//// If we collected any path items for a FigEdgeModelElement,
//// process them now, and assign their values to real Figs on the diag.
//if (!(pathItemAttrs.isEmpty())) {
//    for (Hashtable<String, String> attrs : pathItemAttrs) {
//        // Is the class name a PathItemPlacment?
//        // TODO: if we ever want to extend PathItemPlacement,
//        // we should modify this, so that we also recognise any
//        // subclass of PathItemPlacement.
//        if ("org.argouml.uml.diagram.ui.PathItemPlacement".
//                equals(attrs.get("classname"))) {
//            //System.out.println("figclassname=" + attrs.get("figclassname"));
//
//            PathItemPlacementStrategy pips
//                = getPips(attrs.get("figclassname"),
//                        attrs.get("ownerhref"));
//            // Sanity check - the returned path item placement straty
//            // should match the one in the uml.
//            if (pips.getClass().getName().equals(attrs.get("classname"))) {
//                // Now we're into processing each specific path item
//                // strategy.
//                // At the moment, we only know about PathItemPlacement
//                if (pips instanceof PathItemPlacement) {
//                    PathItemPlacement pip = (PathItemPlacement) pips;
//                    pip.setDisplacementVector(
//                            Double.parseDouble(attrs.get("angle")),
//                            Integer.parseInt(attrs.get("offset")));
//                }
//                // Continue (future PathItemPlacementStrategy impl)
//                //else if (...) {
//                //
//                //}
//
//            }
//            else {
//                LOG.log(Level.WARNING, "PGML stored pathitem class name does not "
//                        + "match the class name on the diagram."
//                        + "Label position will revert to defaults.");
//            }
//        }
//    }
//}
//
//super.endElement(uri, localname, qname);
//}
