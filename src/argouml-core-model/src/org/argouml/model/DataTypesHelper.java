/* $Id$
 *******************************************************************************
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
 *******************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2005-2007 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.model;

import java.util.Collection;


/**
 * The interface for the helper for DataTypes.<p>
 *
 * Created from the old DataTypesHelper.
 */
public interface DataTypesHelper {

    /**
     * @param kind the pseudostate kind
     * @return true if this is a initial kind
     */
    boolean equalsINITIALKind(Object kind);

    /**
     * @param kind the pseudostate kind
     * @return if this is a history kind
     */
    boolean equalsDeepHistoryKind(Object kind);

    /**
     * @param kind the pseudostate kind
     * @return if this is a shallow history kind
     */
    boolean equalsShallowHistoryKind(Object kind);

    /**
     * @param kind the pseudostate kind
     * @return if this is a fork kind
     */
    boolean equalsFORKKind(Object kind);

    /**
     * @param kind the pseudostate kind
     * @return if this is a join kind
     */
    boolean equalsJOINKind(Object kind);

    /**
     * @param kind the pseudostate kind (Choice)
     * @return if this is a branch-choice kind
     */
    boolean equalsCHOICEKind(Object kind);

    /**
     * @param kind the pseudostate kind
     * @return if this is a junction kind
     */
    boolean equalsJUNCTIONKind(Object kind);

    /**
     * Converts a Multiplicity to a String.
     *
     * @param multiplicity The Multiplicity to convert.
     * @return The String representation of multiplicity.
     * @throws IllegalArgumentException if multiplicity is not a Multiplicity.
     */
    String multiplicityToString(Object multiplicity);

    /**
     * Sets the body of an expression.
     *
     * TODO: This operation is fooling the user
     * in thinking that the body of the object is changed.
     * Instead, a new object is created.
     * There is no other way: a MExpression can not be altered,
     * once created!
     * So, this operation returns a newly created object instead.
     *
     * @param handle The expression to modify.
     * @param body The body to set.
     * @return The newly created expression.
     */
    Object setBody(Object handle, String body);

    /**
     * Gets the body of an expression.
     *
     * @param handle The expression to get.
     * @return The body (a String).
     */
    String getBody(Object handle);

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
     * @param language The new language.
     * @return The newly created Object.
     */
    Object setLanguage(Object handle, String language);

    /**
     * Gets the language of an expression.
     *
     * @param handle The expression to get.
     * @return The language (a String).
     */
    String getLanguage(Object handle);

    /**
     * Return the list of implemented ValueSpecification
     * @return All the type of ValueSpecification  
     */
    Collection<String> getValueSpecifications();
    
    /**
     * Creates a value specification object of a given type.
     * TODO move to DataTypesFactory?
     * 
     * @param handle The element that will own the value specification.
     * @param type "OpaqueExpression", "LiteralBoolean",...
     * @return The ValueSpecification created for handle.
     * @since 20110407
     */
    Object createValueSpecification(Object handle, String type);

    /**
     * Modifies a value specification object.
     * 
     * @param handle The instance to modify.
     * @param tabValues One ore more object in order to get the values.
     * @since 20110407
     */
    void modifyValueSpecification(Object handle, Object[] tabValues);
    
    /**
     * Gets the value specification values as an array.
     * 
     * @param handle
     * @return The existing ValueSpecification for handle
     * @since 20110412
     */
    Object[] getValueSpecificationValues(Object handle); 
}
