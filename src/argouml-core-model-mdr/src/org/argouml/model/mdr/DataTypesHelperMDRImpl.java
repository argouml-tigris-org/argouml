/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Morris
 *    Thomas Neustupny
 *    Laurent Braud
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.model.mdr;

import java.util.Collection;
import java.util.Iterator;

import javax.jmi.reflect.InvalidObjectException;

import org.argouml.model.DataTypesHelper;
import org.argouml.model.InvalidElementException;
import org.omg.uml.foundation.datatypes.Expression;
import org.omg.uml.foundation.datatypes.Multiplicity;
import org.omg.uml.foundation.datatypes.MultiplicityRange;
import org.omg.uml.foundation.datatypes.PseudostateKind;
import org.omg.uml.foundation.datatypes.PseudostateKindEnum;

/**
 * DataTypesHelper for the MDR ModelImplementation
 * <p>
 * @since ARGO0.19.5
 * @author Ludovic Ma&icirc;tre
 * @author Tom Morris
 */
class DataTypesHelperMDRImpl implements DataTypesHelper {

    private MDRModelImplementation modelImpl;
	
    /**
     * Default constructor
     * @param implementation the ModelImplementatin
     */
    public DataTypesHelperMDRImpl(MDRModelImplementation implementation) {
        modelImpl = implementation;
    }

    
    public boolean equalsINITIALKind(Object kind) {
        if (!(kind instanceof PseudostateKind)) {
            throw new IllegalArgumentException();
        }
        try {
            return PseudostateKindEnum.PK_INITIAL.equals(kind);
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
    }


    public boolean equalsDeepHistoryKind(Object kind) {
        if (!(kind instanceof PseudostateKind)) {
            throw new IllegalArgumentException();
        }
        try {
            return PseudostateKindEnum.PK_DEEP_HISTORY.equals(kind);
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
    }


    public boolean equalsShallowHistoryKind(Object kind) {
        if (!(kind instanceof PseudostateKind)) {
            throw new IllegalArgumentException();
        }
        try {
            return PseudostateKindEnum.PK_SHALLOW_HISTORY.equals(kind);
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
    }


    public boolean equalsFORKKind(Object kind) {
        if (!(kind instanceof PseudostateKind)) {
            throw new IllegalArgumentException();
        }
        try {
            return PseudostateKindEnum.PK_FORK.equals(kind);
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
    }


    public boolean equalsJOINKind(Object kind) {
        if (!(kind instanceof PseudostateKind)) {
            throw new IllegalArgumentException();
        }
        try {
            return PseudostateKindEnum.PK_JOIN.equals(kind);
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
    }


    public boolean equalsCHOICEKind(Object kind) {
        if (!(kind instanceof PseudostateKind)) {
            throw new IllegalArgumentException();
        }
        try {
            return PseudostateKindEnum.PK_CHOICE.equals(kind);
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
    }


    public boolean equalsJUNCTIONKind(Object kind) {
        if (!(kind instanceof PseudostateKind)) {
            throw new IllegalArgumentException();
        }
        try {
            return PseudostateKindEnum.PK_JUNCTION.equals(kind);
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
    }


    public String multiplicityToString(Object multiplicity) {
        if (!(multiplicity instanceof Multiplicity)) {
            throw new IllegalArgumentException("Unrecognized object: "
                    + multiplicity);
        }
        try {
            String rc = "";
            Iterator i = ((Multiplicity) multiplicity).getRange().iterator();
            boolean first = true;
            while (i.hasNext()) {
                if (first) {
                    first = false;
                } else {
                    rc += ",";
                }
                rc += multiplicityRangeToString((MultiplicityRange) i.next());
            }
            return rc;
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
    }

    /**
     * @param range multiplicity range to convert
     * @return string representing multiplicity range
     */
    private String multiplicityRangeToString(MultiplicityRange range) {
        if (range.getLower() == range.getUpper()) {
            return DataTypesFactoryMDRImpl.boundToString(range.getLower());
        } else {
            return DataTypesFactoryMDRImpl.boundToString(
                    range.getLower())
                    + ".."
                    + DataTypesFactoryMDRImpl.boundToString(range.getUpper());
        }
    }


    public Object setBody(Object handle, String body) {
        if (handle instanceof Expression) {
            ((Expression) handle).setBody(body);
            return handle;
        }
        throw new IllegalArgumentException("handle: " + handle + " body:"
                + body);
    }


    public String getBody(Object handle) {
        try {
            if (handle instanceof Expression) {
                return ((Expression) handle).getBody();
            }
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
        throw new IllegalArgumentException("handle: " + handle);
    }


    public Object setLanguage(Object handle, String language) {
        if (handle instanceof Expression) {
            ((Expression) handle).setLanguage(language);
            return handle;
        }
        throw new IllegalArgumentException("handle: " + handle + " language: "
                + language);
    }


    public String getLanguage(Object handle) {
        try {
            if (handle instanceof Expression) {
                return ((Expression) handle).getLanguage();
            }
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
        throw new IllegalArgumentException("handle: " + handle);
    }
    
    /**
     * @see org.argouml.model.DataTypesHelper#getValueSpecifications()
     */
    public Collection<String> getValueSpecifications() {
        return null;
    }

    /**
     * @see org.argouml.model.DataTypesHelper#createValueSpecification(java.lang.Object,java.lang.String)
     */
    public Object createValueSpecification(Object handle,
            String type) {
        return null;
    }

    /**
     * @see org.argouml.model.DataTypesHelper#modifyValueSpecification(java.lang.Object, java.lang.Object[])
     */
    public void modifyValueSpecification(Object handle, Object[] tabValues) {
        // Not implemented in MDR 
    }
    
    /**
     * @see org.argouml.model.DataTypesHelper#getValueSpecificationValues(java.lang.Object)
     */
    public Object[] getValueSpecificationValues(Object handle) {
        return null;
    }
    
}
