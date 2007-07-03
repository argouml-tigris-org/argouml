// $Id: PredicateFind.java 12777 2007-06-08 07:46:22Z tfmorris $
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

import org.argouml.model.Model;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.util.Predicate;
import org.tigris.gef.util.PredicateTrue;

/**
 * Class to find out if a given object fulfills certain given predicates.
 *
 */
public class PredicateFind implements Predicate {

    ////////////////////////////////////////////////////////////////
    // instance variables
    private Predicate elementName;
    private Predicate packageName;
    private Predicate diagramName;
    private Predicate theType;

    private Predicate specific = PredicateTrue.theInstance();

    /**
     * The constructor.
     *
     * @param e Predicate for the element name
     * @param p Predicate for the package name
     * @param d Predicate for the diagram name
     * @param t Predicate for the type
     */
    public PredicateFind(Predicate e, Predicate p, Predicate d,
			 Predicate t) {
	elementName = e;
	packageName = p;
	diagramName = d;
	theType = t;
    }


    /**
     * @param d the given diagram
     * @return true if the name of the given diagram equals
     */
    public boolean matchDiagram(Diagram d) {
        return matchDiagram(d.getName());
    }

    /**
     * @param name the name to match
     * @return true if the name of the given diagram equals
     */
    public boolean matchDiagram(String name) {
        return diagramName.predicate(name);
    }

    /**
     * @param m the given package
     * @return true if the name of the given package is equal
     */
    public boolean matchPackage(Object m) {
	boolean res = packageName.predicate(Model.getFacade().getName(m));
	return res;
    }

    /*
     * @see org.tigris.gef.util.Predicate#predicate(java.lang.Object)
     */
    public boolean predicate(Object o) {
	if (!(Model.getFacade().isAUMLElement(o))) {
            return false;
        }
	Object me = o;
	return theType.predicate(me) && specific.predicate(me)
	    && elementName.predicate(Model.getFacade().getName(me));
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 9149816242647422438L;
} /* end class PredicateFind */
