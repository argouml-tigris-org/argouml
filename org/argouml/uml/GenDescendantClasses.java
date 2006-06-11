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

import java.util.Collection;
import java.util.Enumeration;
import java.util.Vector;

import org.argouml.model.Model;
import org.tigris.gef.util.ChildGenerator;
/** Utility class to generate the subclasses of a class.  It
 *  recursively moves down the class hierarchy.  But it does that in a
 *  safe way that will nothang in case of cyclic inheritance.
 *  @stereotype singleton
 */

public class GenDescendantClasses implements ChildGenerator {
    private static final GenDescendantClasses SINGLETON =
        new GenDescendantClasses();

    /**
     * @return Returns the sINGLETON.
     */
    public static GenDescendantClasses getSINGLETON() {
        return SINGLETON;
    }

    /**
     * @see org.tigris.gef.util.ChildGenerator#gen(java.lang.Object)
     */
    public Enumeration gen(Object o) {
	Vector res = new Vector();
	if (!(Model.getFacade().isAGeneralizableElement(o))) {
                return res.elements();
        }

	Object cls = /*(MGeneralizableElement)*/ o;
	Collection gens = Model.getFacade().getSpecializations(cls);
	if (gens == null) return res.elements();
	accumulateDescendants(cls, res);
	return res.elements();
    }


    /**
     * @param cls the starting class (in fact GeneralizableElement)
     * @param accum the accumulated list of descendents
     */
    public void accumulateDescendants(Object/*MGeneralizableElement*/ cls,
            Vector accum) {
	Vector gens = new Vector(Model.getFacade().getSpecializations(cls));
	if (gens == null) return;
	int size = gens.size();
	for (int i = 0; i < size; i++) {
	    Object g = /*(MGeneralization)*/ (gens.elementAt(i));
	    Object ge = Model.getFacade().getChild(g);
	    if (!accum.contains(ge)) {
		accum.add(ge);
		accumulateDescendants(cls, accum);
	    }
	}
    }
} /* end class GenDescendantClasses */
