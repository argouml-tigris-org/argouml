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
import org.argouml.model.Model;

import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.data_types.MExpression;
import ru.novosoft.uml.foundation.data_types.MExpressionEditor;
import ru.novosoft.uml.foundation.data_types.MMultiplicity;
import ru.novosoft.uml.foundation.data_types.MPseudostateKind;
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
     */
    public boolean equalsBRANCHKind(Object kind) {

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
     * Sets the language of an expression.
     *
     * TODO: This operation is fooling the user
     * in thinking that the body of the object is changed.
     * Instead, a new object is created and as a side-effect the body is lost.
     * There is no other way: a MExpression can not be altered,
     * once created!
     * So, this operation should return the created object instead!
     * Or should it simply copy the body?
     *
     * @param handle is the expression
     * @param language is the lang
     */
    public void setLanguage(Object handle, String language) {
        if (handle instanceof MExpression) {
            MExpressionEditor expressionEditor =
                (MExpressionEditor)
                	Model.getDataTypesFactory().
                		createExpressionEditor(handle);
            expressionEditor.setLanguage(language);
            handle = expressionEditor.toExpression();
            // TODO: Is something missing here?

            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or language: " + language);
    }
}

