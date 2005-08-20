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

package org.argouml.model.uml;

import java.util.Iterator;

import org.argouml.model.DataTypesHelper;

import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.data_types.MActionExpression;
import ru.novosoft.uml.foundation.data_types.MArgListsExpression;
import ru.novosoft.uml.foundation.data_types.MBooleanExpression;
import ru.novosoft.uml.foundation.data_types.MExpression;
import ru.novosoft.uml.foundation.data_types.MIterationExpression;
import ru.novosoft.uml.foundation.data_types.MMappingExpression;
import ru.novosoft.uml.foundation.data_types.MMultiplicity;
import ru.novosoft.uml.foundation.data_types.MObjectSetExpression;
import ru.novosoft.uml.foundation.data_types.MProcedureExpression;
import ru.novosoft.uml.foundation.data_types.MPseudostateKind;
import ru.novosoft.uml.foundation.data_types.MTimeExpression;
import ru.novosoft.uml.foundation.data_types.MTypeExpression;
import ru.novosoft.uml.foundation.extension_mechanisms.MTaggedValue;

/**
 * Helper class for UML Foundation::DataTypes Package.<p>
 *
 * Current implementation is a placeholder.<p>
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
 */
class DataTypesHelperImpl implements DataTypesHelper {

    /**
     * The model implementation.
     */
    private NSUMLModelImplementation nsmodel;

    /**
     * Don't allow instantiation.
     *
     * @param implementation To get other helpers and factories.
     */
    DataTypesHelperImpl(NSUMLModelImplementation implementation) {
        nsmodel = implementation;
    }

     /**
     * @param from source
     * @param to destination
     */
    public void copyTaggedValues(Object from, Object to) {
        if (!(from instanceof MModelElement)) {
            throw new IllegalArgumentException();
        }
        if (!(to instanceof MModelElement)) {
            throw new IllegalArgumentException();
        }

	Iterator it = ((MModelElement) from).getTaggedValues().iterator();
	while (it.hasNext()) {
	    MTaggedValue tv = (MTaggedValue) it.next();
	    ((MModelElement) to).setTaggedValue(tv.getTag(), tv.getValue());
	}
    }

    /**
     * @param kind the pseudostate kind
     * @return true if this is a initial kind
     */
    public boolean equalsINITIALKind(Object kind) {

        if (!(kind instanceof MPseudostateKind)) {
            throw new IllegalArgumentException();
        }

        return MPseudostateKind.INITIAL.equals(kind);
    }

    /**
     * @param kind the pseudostate kind
     * @return if this is a history kind
     */
    public boolean equalsDeepHistoryKind(Object kind) {

        if (!(kind instanceof MPseudostateKind)) {
            throw new IllegalArgumentException();
        }

        return MPseudostateKind.DEEP_HISTORY.equals(kind);
    }

    /**
     * @param kind the pseudostate kind
     * @return if this is a shallow history kind
     */
    public boolean equalsShallowHistoryKind(Object kind) {

        if (!(kind instanceof MPseudostateKind)) {
            throw new IllegalArgumentException();
        }

        return MPseudostateKind.SHALLOW_HISTORY.equals(kind);
    }

    /**
     * @param kind the pseudostate kind
     * @return if this is a fork kind
     */
    public boolean equalsFORKKind(Object kind) {

        if (!(kind instanceof MPseudostateKind)) {
            throw new IllegalArgumentException();
        }

        return MPseudostateKind.FORK.equals(kind);
    }

    /**
     * @param kind the pseudostate kind
     * @return if this is a join kind
     */
    public boolean equalsJOINKind(Object kind) {

        if (!(kind instanceof MPseudostateKind)) {
            throw new IllegalArgumentException();
        }

        return MPseudostateKind.JOIN.equals(kind);
    }

    /**
     * @param kind the pseudostate kind (Choice)
     * @return if this is a branch-choice kind
     * @deprecated UML 1.3 only - use equalsCHOICEKind
     */
    public boolean equalsBRANCHKind(Object kind) {
        return equalsCHOICEKind(kind);
    }

    /**
     * @param kind the pseudostate kind (Choice)
     * @return if this is a branch-choice kind
     */
    public boolean equalsCHOICEKind(Object kind) {

        if (!(kind instanceof MPseudostateKind)) {
            throw new IllegalArgumentException();
        }

        return MPseudostateKind.BRANCH.equals(kind);
    }

    /**
     * @param kind the pseudostate kind
     * @return if this is a junction kind
     */
    public boolean equalsJUNCTIONKind(Object kind) {

        if (!(kind instanceof MPseudostateKind)) {
            throw new IllegalArgumentException();
        }
        return MPseudostateKind.JUNCTION.equals(kind);
    }

    /**
     * Converts a Multiplicity to a String.
     *
     * @param multiplicity The Multiplicity to convert.
     * @return The String representation of multiplicity.
     * @throws IllegalArgumentException if multiplicity is not a Multiplicity.
     */
    public String multiplicityToString(Object multiplicity) {
	if (!(multiplicity instanceof MMultiplicity)) {
	    throw new IllegalArgumentException("Unrecognized object: "
                + multiplicity);
	}

	return ((MMultiplicity) multiplicity).toString();
    }

    /**
     * @see org.argouml.model.DataTypesHelper#getBody(java.lang.Object)
     */
    public String getBody(Object handle) {
        if (handle instanceof MExpression) {
            return ((MExpression) handle).getBody();
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * @see org.argouml.model.DataTypesHelper#getLanguage(java.lang.Object)
     */
    public String getLanguage(Object handle) {
        if (handle instanceof MExpression) {
            return ((MExpression) handle).getLanguage();
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * TODO: This operation is fooling the user
     * in thinking that the body of the object is changed.
     * Instead, a new object is created.
     * There is no other way: a MExpression can not be altered,
     * once created!
     * So, this operation returns a newly created object instead.
     *
     * @see org.argouml.model.DataTypesHelper#setBody(Object, String)
     */
    public Object setBody(Object handle, String body) {
        String language;
        if (!(handle instanceof MExpression)) {
            throw new IllegalArgumentException("handle: " + handle);
        }

        language = ((MExpression) handle).getLanguage();
        return newExpression(handle, body, language);
    }

    /**
     * Sets the language of an expression.
     *
     * TODO: This operation is fooling the user
     * in thinking that the body of the object is changed.
     * Instead, a new object is created.
     * There is no other way: a MExpression can not be altered,
     * once created!
     * So, this operation returns a newly created object instead.
     *
     * @param handle The expression.
     * @param language The language.
     * @return The newly created expression.
     */
    public Object setLanguage(Object handle, String language) {
        String body;
        if (!(handle instanceof MExpression)) {
            throw new IllegalArgumentException("handle: " + handle);
        }

        body = ((MExpression) handle).getBody();
        return newExpression(handle, body, language);
    }

    /**
     * Create a new expression of the same type as the one given but with
     * the new body and language.
     *
     * @param handle The expression with the type that we want.
     * @param body The body.
     * @param language The language.
     * @return A newly created expression.
     */
    private Object newExpression(Object handle, String body, String language) {
        if (handle instanceof MActionExpression) {
            return new MActionExpression(language, body);
        }
        if (handle instanceof MArgListsExpression) {
            return new MArgListsExpression(language, body);
        }
        if (handle instanceof MBooleanExpression) {
            return new MBooleanExpression(language, body);
        }
        if (handle instanceof MIterationExpression) {
            return new MIterationExpression(language, body);
        }
        if (handle instanceof MMappingExpression) {
            return new MMappingExpression(language, body);
        }
        if (handle instanceof MObjectSetExpression) {
            return new MObjectSetExpression(language, body);
        }
        if (handle instanceof MProcedureExpression) {
            return new MProcedureExpression(language, body);
        }
        if (handle instanceof MTimeExpression) {
            return new MTimeExpression(language, body);
        }
        if (handle instanceof MTypeExpression) {
            return new MTypeExpression(language, body);
        }

        if (handle instanceof MExpression) {
            return new MExpression(language, body);
        }

        throw new IllegalArgumentException("Not an expression: " + handle);
    }
}

