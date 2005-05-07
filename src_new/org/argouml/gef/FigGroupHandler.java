// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

import java.util.Collection;
import java.util.StringTokenizer;

import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigGroup;

import org.xml.sax.SAXException;

/**
 * The handler for elements that represent FigGroup-derived objects,
 * including FigNodes.
 * @author Michael A. MacDonald
 */
public class FigGroupHandler
	extends BaseHandler
	implements Container {
    /**
     * The Fig with the group.
     */
    private FigGroup group;

    /**
     * @param parser The PGMLStackParser for the diagram that contains this
     * FigGroup
     * @param theGroup The object corresponding to the element being parsed
     */
    public FigGroupHandler(PGMLStackParser parser,
                            FigGroup theGroup) {
        super(parser);
        group = theGroup;
    }

    /**
     * @return The object corresponding to the element being parsed.
     */
    public FigGroup getFigGroup() {
        return group;
    }

    /**
     * If the object being parsed is a FigNode, add the owner of the object
     * to the collection of node owners in the diagram associated with
     * the PGMLStackParser object.
     *
     * @see org.xml.sax.ContentHandler#endElement(
     *         java.lang.String, java.lang.String, java.lang.String)
     */
    public void endElement(String uri, String localname, String qname)
        throws SAXException {
        if (group instanceof FigNode) {
            Object owner = group.getOwner();
            Collection nodes = getPGMLStackParser().getDiagram().getNodes(null);
            if (!nodes.contains(owner)) {
                nodes.add(owner);
            }
        }
        super.endElement(uri, localname, qname);
    }

    /**
     * Add the object represented by a sub-element to this group.
     * If a sub-element represents a Fig, the Fig is added to this group's
     * Fig collection.  If a sub-element represents a String, it is
     * a <em>private</em> element that identifies the enclosing Fig of
     * this group, so set the enclosing Fig.
     *
     * @see org.argouml.gef.Container#addObject(java.lang.Object)
     */
    public void addObject(Object toAdd) {
        if (toAdd instanceof Fig) {
            group.addFig((Fig) toAdd);
        }
        // Handle private string
        if (toAdd instanceof String) {
            StringTokenizer st2 =
                new StringTokenizer((String) toAdd, "=\"' \t\n");
            while (st2.hasMoreElements()) {
                String t = st2.nextToken();
                String v = "no such fig";
                if (st2.hasMoreElements()) {
                    v = st2.nextToken();
                }

                if (t.equals("enclosingFig")) {
                    group.setEnclosingFig(getPGMLStackParser().findFig(v));
                }
            }
        }
    }
}
