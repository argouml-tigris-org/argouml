// $Id$
/*******************************************************************************
 * Copyright (c) 2007,2010 Tom Morris and other contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Morris - initial implementation 
 *******************************************************************************/

package org.argouml.model.euml;

import org.argouml.model.PseudostateKind;

/**
 * The Eclipse UML2 implementation of PseudoStateKind.  An enumeration
 * of all the values that a PseudoState can have.  EntryPoint, ExitPoint,
 * and Terminate are new for UML 2.x.
 * 
 * @author Tom Morris
 */
class PseudostateKindEUMLImpl implements PseudostateKind {

    public Object getChoice() {
        return org.eclipse.uml2.uml.PseudostateKind.CHOICE_LITERAL;
    }

    public Object getDeepHistory() {
        return org.eclipse.uml2.uml.PseudostateKind.DEEP_HISTORY_LITERAL;
    }

    public Object getEntryPoint() {
        return org.eclipse.uml2.uml.PseudostateKind.ENTRY_POINT_LITERAL;        
    }
 
    public Object getExitPoint() {
        return org.eclipse.uml2.uml.PseudostateKind.EXIT_POINT_LITERAL;        
    }
   
    public Object getFork() {
        return org.eclipse.uml2.uml.PseudostateKind.FORK_LITERAL;
    }

    public Object getInitial() {
        return org.eclipse.uml2.uml.PseudostateKind.INITIAL_LITERAL;
    }

    public Object getJoin() {
        return org.eclipse.uml2.uml.PseudostateKind.JOIN_LITERAL;
    }

    public Object getJunction() {
        return org.eclipse.uml2.uml.PseudostateKind.JUNCTION_LITERAL;
    }

    public Object getShallowHistory() {
        return org.eclipse.uml2.uml.PseudostateKind.SHALLOW_HISTORY_LITERAL;
    }
 
    public Object getTerminate() {
        return org.eclipse.uml2.uml.PseudostateKind.TERMINATE_LITERAL;        
    }

}
