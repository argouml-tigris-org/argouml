// $Id$
// Copyright (c) 1996-99 The Regents of the University of California. All
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



// File: CrUselessAbstract.java
// Classes: CrUselessAbstract
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.cognitive.critics;

import java.util.Enumeration;
import java.util.Vector;

import org.argouml.cognitive.Designer;
import org.argouml.cognitive.Goal;
import org.argouml.model.ModelFacade;

import org.tigris.gef.util.ChildGenerator;
import org.tigris.gef.util.EnumerationEmpty;
import org.tigris.gef.util.VectorSet;

/** A critic to detect when a class can never have instances (of
 *  itself of any subclasses). */
public class CrUselessAbstract extends CrUML {

    public CrUselessAbstract() {
	setHeadline("Define Concrete (Sub)Class");
	addSupportedDecision(CrUML.decINHERITANCE);
	addSupportedGoal(Goal.UNSPEC);
	addTrigger("specialization");
	addTrigger("isAbstract");
    }

    public boolean predicate2(Object dm, Designer dsgr) {
	if (!(ModelFacade.isAClass(dm))) return false;
	Object cls = /*(MClass)*/ dm;
	if (!ModelFacade.isAbstract(cls))
	    return false;  // original class was not abstract
	VectorSet derived =
	    (new VectorSet(cls)).reachable(new ChildGenDerivedClasses());
	Enumeration enum = derived.elements();
	Object c;
	while (enum.hasMoreElements()) {
	    c = /*(MClass)*/ enum.nextElement();
	    if (!ModelFacade.isAbstract(c))
		return false;  // found a concrete subclass
	}
	return true; // no concrete subclasses defined, this class is "useless"
    }

} /* end class CrUselessAbstract */



class ChildGenDerivedClasses implements ChildGenerator {
    public Enumeration gen(Object o) {
	Object c = /*(MClass)*/ o;
	Vector specs = new Vector(ModelFacade.getSpecializations(c));
	if (specs == null) {
	    return EnumerationEmpty.theInstance();
	}
	// TODO: it would be nice to have a EnumerationXform
	// and a Functor object in uci.util
	Vector specClasses = new Vector(specs.size());
	Enumeration enum = specs.elements();
	while (enum.hasMoreElements()) {
	    Object g = /*(MGeneralization)*/ enum.nextElement();
	    Object ge = ModelFacade.getChild(g);
	    // assert: ge != null
	    if (ge != null) specClasses.addElement(ge);
	}
	return specClasses.elements();
    }
} /* end class derivedClasses */