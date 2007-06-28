// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.uml;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import org.argouml.model.Model;
import org.tigris.gef.util.ChildGenerator;

/**
 * Utility class to generate a list of the children of a class.  In this case
 * the "children" of a class are the other classes that are
 * associated with the parent class, and that MAssociation has a
 * COMPOSITE end at the parent.  This is used in one of the critics.
 *
 * @see org.argouml.uml.cognitive.critics.CrCircularComposition
 * @stereotype singleton
 */
public class GenCompositeClasses implements ChildGenerator {
    /**
     * This SINGLETON is used in CrCircularComposition.
     *
     */
    private static final GenCompositeClasses SINGLETON =
        new GenCompositeClasses();

    /**
     * @return Returns the sINGLETON.
     */
    public static GenCompositeClasses getSINGLETON() {
        return SINGLETON;
    }
    
    /*
     * @see org.tigris.gef.util.ChildGenerator#gen(java.lang.Object)
     */
    public Enumeration gen(Object o) {
	Vector res = new Vector();
	if (!(Model.getFacade().isAClassifier(o))) {
	    return res.elements();
	}
	Object cls = o;
	Vector ends = new Vector(Model.getFacade().getAssociationEnds(cls));
	if (ends == null) {
	    return res.elements();
	}
	Iterator assocEnds = ends.iterator();
	while (assocEnds.hasNext()) {
	    Object ae = assocEnds.next();
	    if (Model.getAggregationKind().getComposite().equals(
	            Model.getFacade().getAggregation(ae))) {
		Object asc = Model.getFacade().getAssociation(ae);
		ArrayList conn =
		    new ArrayList(Model.getFacade().getConnections(asc));
		if (conn == null || conn.size() != 2) {
		    continue;
		}
		Object otherEnd =
		    (ae == conn.get(0)) ? conn.get(1) : conn.get(0);
		if (Model.getFacade().getType(ae)
		        != Model.getFacade().getType(otherEnd)) {
		    res.add(Model.getFacade().getType(otherEnd));
		}
	    }
	}
	return res.elements();
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -6027679124153204193L;
} /* end class GenCompositeClasses */

