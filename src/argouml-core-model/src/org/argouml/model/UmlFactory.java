/* $Id$
 *******************************************************************************
 * Copyright (c) 2009-2011 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Morris
 *    Bob Tarling
 *    Thomas Neustupny
 *******************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2005-2008 The Regents of the University of California. All
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
 * The interface for the UmlFactory.
 */
public interface UmlFactory {
    /**
     * Creates a UML model element of the given type and uses
     * this to connect two other existing UML model elements.
     * This only works for UML elements. If a diagram contains
     * elements of another type then it is the responsibility
     * of the diagram manage those items and not call this
     * method.
     *
     * @param connectionType the UML object type of the connection
     * @param fromElement    the UML object for the "from" element
     * @param fromStyle      the aggregationkind for the connection
     *                       in case of an association
     * @param toElement      the UML object for the "to" element
     * @param toStyle        the aggregationkind for the connection
     *                       in case of an association
     * @param unidirectional for association and associationrole
     * @param namespace      the namespace to use if it can't be determined
     * @return               the newly build connection (UML object)
     * @throws IllegalModelElementConnectionException if the connection is not
     *                                                a valid thing to do
     */
    Object buildConnection(Object connectionType, Object fromElement,
            Object fromStyle, Object toElement, Object toStyle,
            Object unidirectional, Object namespace)
        throws IllegalModelElementConnectionException;

    /**
     * Creates a UML model element of the given type.
     * This only works for UML elements. If a diagram contains
     * elements of another type then it is the responsibility
     * of the diagram manage those items and not call this
     * method. It also only works for UML model elements that
     * are represented in diagrams by a node.
     *
     * @param elementType the type of model element to build
     * @return the model element
     * TODO: This needs an extra parameter to specify the model/extent where
     * the new element should be created.
     */
    Object buildNode(Object elementType);
    
    /**
     * Creates a UML model element of the given type and adds 
     * it to the passed in container.
     * 
     * @param elementType the type of model element to build
     * @param container the model element that will contain the 
     * newly built model element
     * @return the model element
     */
    Object buildNode(Object elementType, Object container);

    /**
     * Creates a UML model element of the given type and adds 
     * it to the passed in container.
     * 
     * @param elementType the type of model element to build
     * @param container the model element that will contain the 
     * newly built model element
     * @param defaults the default values to apply to the new element
     * @return the model element
     */
    Object buildNode(Object elementType, Object container, String property, Defaults defaults);

    
    /**
     * Creates a UML model element of the given type and adds 
     * it to the passed in container.
     * 
     * @param elementType the type of model element to build
     * @param container the model element that will contain the 
     * newly built model element
     * @param property the property name of the containment
     * this is only required when a container has multiple
     * properties old containing the same type of object
     * @return the model element
     */
    Object buildNode(Object elementType, Object container, String property);

    /**
     * Checks if some type of UML model element is valid to
     * connect other UML model elements.<p>
     *
     * @param connectionType  the UML object type of the connection
     * @return true if valid
     */
    boolean isConnectionType(Object connectionType);
    

    /**
     * Checks if the given type of UML model element is valid to connect two
     * other existing UML model elements, optionally checking UML
     * well-formedness rules in addition to basic metamodel validity.
     * <p>
     * 
     * @param connectionType
     *            the UML object type of the connection
     * @param fromElement
     *            the UML object type of the "from"
     * @param toElement
     *            the UML object type of the "to"
     * @param checkWFR true to check UML Well Formedness Rules
     * @return true if valid
     */
    boolean isConnectionValid(Object connectionType, Object fromElement,
            Object toElement, boolean checkWFR);
    
    /**
     * Checks if the given type of UML model element is valid to be
     * contained within the passed in container model element. 
     * 
     * @param metaType the UML object type to be tested
     * @param container the UML object that is the container
     * @return true if valid
     */
    boolean isContainmentValid(Object metaType, Object container);
    
    /**
     * Delete a model element. This will do a a 'cascading deletes' 
     * which recursively deletes any model elements which would no
     * longer be valid after this element is deleted. For example, a binary
     * association which has one end deleted will also be deleted because
     * it no longer meets the minimum multiplicity constraint.
     * <p>
     * Callers who are interested in receiving notification of all elements
     * which were deleted should register an event listener to receive 
     * delete events.
     * 
     * @param elem The element to be deleted
     */
    void delete(Object elem);
    
    /**
     * Delete the entire extent which contains the given element.  Typically
     * this is the collection of elements which were read in from a single
     * XMI file.
     * 
     * @param element an element in extent to be deleted.  
     */
    void deleteExtent(Object element);

    /**
     * Check whether a model element has been deleted.<p>
     *
     * <em>NOTE:</em>You probably don't want to use this.  It's not thread-safe!
     * Without external synchronization there's no guarantee that the
     * element won't be deleted by thread after this method returns, but
     * before the caller can do anything with it.<p>
     *
     * If the calling code isn't protected by a lock or some other type of
     * synchronization, a much, <em>MUCH</em> better alternative to using this
     * call is to use a try/catch block which catches InvalidElementExceptions.
     *
     * @param o the object to be checked
     * @return true if removed
     */
    boolean isRemoved(Object o);

    /**
     * Get the top level elements (only packages in UML 1.x) for the given
     * extent if it is loaded. Returns null if the extent doesn't exist and an
     * empty collection if it exists, but contains no elements.
     * 
     * @param extentName the extent name (typically the public ID or URL of the
     *            file which is used for references)
     * @return a collection of elements if the extent exists, otherwise null
     */
    Collection getExtentElements(String extentName);

    /**
     * Get the top level packages (typically just a single Model) for the given
     * extent if it is loaded. Returns null if the extent doesn't exist and an
     * empty collection if it exists, but contains no packages.
     * 
     * @param extentName the extent name (typically the public ID or URL of the
     *            file which is used for references)
     * @return a collection of packages if the extent exists, otherwise null
     */
    Collection getExtentPackages(String extentName);
}
