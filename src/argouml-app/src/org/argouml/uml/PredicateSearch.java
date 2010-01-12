/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
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

// Copyright (c) 2000-2008 The Regents of the University of California. All
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
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.util.Predicate;
import org.argouml.util.PredicateTrue;

/**
 * Class to find out if a given object fulfills certain given predicates. This
 * replaces {@link PredicateFind} by 1sturm with an implementation using the
 * new GEF-free interfaces.
 * 
 * @author 1sturm
 * @author Tom Morris
 */
public class PredicateSearch implements Predicate {

    private Predicate elementName;
    private Predicate packageName;
    private Predicate diagramName;
    private Predicate theType;

    private Predicate specific = PredicateTrue.getInstance();

    /**
     * The constructor.
     *
     * @param elementNamePredicate Predicate for the element name
     * @param packageNamePredicate Predicate for the package name
     * @param diagramNamePredicate Predicate for the diagram name
     * @param typePredicate Predicate for the type
     */
    public PredicateSearch(Predicate elementNamePredicate,
            Predicate packageNamePredicate, Predicate diagramNamePredicate,
            Predicate typePredicate) {
        elementName = elementNamePredicate;
        packageName = packageNamePredicate;
        diagramName = diagramNamePredicate;
        theType = typePredicate;
    }

    /**
     * @param diagram the given diagram
     * @return true if the name of the given diagram equals
     */
    public boolean matchDiagram(ArgoDiagram diagram) {
        return matchDiagram(diagram.getName());
    }

    
    /**
     * @param name the name to match
     * @return true if the name of the given diagram equals
     */
    public boolean matchDiagram(String name) {
        return diagramName.evaluate(name);
    }

    /**
     * @param pkg the given package
     * @return true if the name of the given package is equal
     */
    public boolean matchPackage(Object pkg) {
	boolean res = packageName.evaluate(Model.getFacade().getName(pkg));
	return res;
    }

    public boolean evaluate(Object element) {
	if (!(Model.getFacade().isAUMLElement(element))) {
            return false;
        }
	Object me = element;
	return theType.evaluate(me) && specific.evaluate(me)
	    && elementName.evaluate(Model.getFacade().getName(me));
    }
}
