// $Id$
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

import java.util.Iterator;

import javax.jmi.reflect.InvalidObjectException;

import org.argouml.model.DataTypesHelper;
import org.argouml.model.InvalidElementException;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.TaggedValue;
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
public class DataTypesHelperMDRImpl implements DataTypesHelper {

    private MDRModelImplementation modelImpl;
	
    /**
     * Default constructor
     * @param implementation the ModelImplementatin
     */
    public DataTypesHelperMDRImpl(MDRModelImplementation implementation) {
        modelImpl = implementation;
    }

    /*
     * @see org.argouml.model.DataTypesHelper#copyTaggedValues(
     *      java.lang.Object, java.lang.Object)
     */
    public void copyTaggedValues(Object from, Object to) {
        if (!(from instanceof ModelElement)) {
            throw new IllegalArgumentException();
        }
        if (!(to instanceof ModelElement)) {
            throw new IllegalArgumentException();
        }

        Iterator it = ((ModelElement) from).getTaggedValue().iterator();
        TaggedValue tv;
        TaggedValue newTv;
        String value;
        while (it.hasNext()) {
            tv = (TaggedValue) it.next();
            if (!tv.getDataValue().isEmpty())
                value = tv.getDataValue().iterator().next().toString();
            else
                value = "";
            modelImpl.getCoreHelper().setTaggedValue(to,
                    tv.getType().getName(), value);
            newTv = (TaggedValue) modelImpl.getFacade().getTaggedValue(to,
                    tv.getType().getName());
            newTv.getType().setTagType(tv.getType().getTagType());
            // TODO: Should we also copy the ReferenceValue collection ?
        }
    }

    /*
     * @see org.argouml.model.DataTypesHelper#equalsINITIALKind(java.lang.Object)
     */
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

    /*
     * @see org.argouml.model.DataTypesHelper#equalsDeepHistoryKind(java.lang.Object)
     */
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

    /*
     * @see org.argouml.model.DataTypesHelper#equalsShallowHistoryKind(java.lang.Object)
     */
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

    /*
     * @see org.argouml.model.DataTypesHelper#equalsFORKKind(java.lang.Object)
     */
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

    /*
     * @see org.argouml.model.DataTypesHelper#equalsJOINKind(java.lang.Object)
     */
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

    /*
     * @see org.argouml.model.DataTypesHelper#equalsCHOICEKind(java.lang.Object)
     */
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

    /*
     * @see org.argouml.model.DataTypesHelper#equalsJUNCTIONKind(java.lang.Object)
     */
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

    /*
     * @see org.argouml.model.DataTypesHelper#multiplicityToString(java.lang.Object)
     */
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

    /*
     * @see org.argouml.model.DataTypesHelper#setBody(java.lang.Object,
     *      java.lang.String)
     */
    public Object setBody(Object handle, String body) {
        if (handle instanceof Expression) {
            ((Expression) handle).setBody(body);
            return handle;
        }
        throw new IllegalArgumentException("handle: " + handle + " body:"
                + body);
    }

    /*
     * @see org.argouml.model.DataTypesHelper#getBody(java.lang.Object)
     */
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

    /*
     * @see org.argouml.model.DataTypesHelper#setLanguage(java.lang.Object,
     *      java.lang.String)
     */
    public Object setLanguage(Object handle, String language) {
        if (handle instanceof Expression) {
            ((Expression) handle).setLanguage(language);
            return handle;
        }
        throw new IllegalArgumentException("handle: " + handle + " language: "
                + language);
    }

    /*
     * @see org.argouml.model.DataTypesHelper#getLanguage(java.lang.Object)
     */
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
	
}
