// $Id$
// Copyright (c) 2005 The Regents of the University of California. All
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


/**
 * The interface for the factory for Uml.<p>
 *
 * Created from the old UmlFactory.
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
     */
    Object buildNode(Object elementType);

    /**
     * Checks if some type of UML model element is valid to
     * connect two other existing UML model elements.
     * This only works for UML elements. If a diagram contains
     * elements of another type then it is the responsibility
     * of the diagram to filter those out before calling this
     * method.
     *
     * @param connectionType  the UML object type of the connection
     * @param fromElement     the UML object type of the "from"
     * @param toElement       the UML object type of the "to"
     * @return true if valid
     */
    boolean isConnectionValid(Object connectionType, Object fromElement,
            Object toElement);

    /**
     * Deletes a model element. It calls the remove method of the
     * model element but also does 'cascading deletes' that are not
     * provided for in the remove method of the model element
     * itself. For example: this delete method also removes the binary
     * associations that a class has if the class is deleted. In this
     * way, it is not longer possible that illegal states exist in the
     * model.<p>
     *
     * The actual deletion is delegated to delete methods in the rest of the
     * factories. For example: a method deleteClass exists on CoreHelper.
     * Delete methods as deleteClass should only do those extra actions that are
     * necessary for the deletion of the modelelement itself. I.e. deleteClass
     * should only take care of things specific to class.<p>
     *
     * The implementation of this method uses a quite complicated if then else
     * tree. This is done to provide optimal performance and full compliance to
     * the UML 1.3 model. The last remark refers to the fact that the UML 1.3
     * model knows multiple inheritance in several places. This has to be taken
     * into account.<p>
     *
     * Extensions and its children are not taken into account
     * here. They do not require extra cleanup actions. Not in the
     * form of a call to the remove method as is normal for all
     * model elements and not in the form of other behaviour we
     * want to implement via this operation.
     * @param elem The element to be deleted
     */
    void delete(Object elem);

    /**
     * The Project may check if a certain model element has been removed.
     *
     * @param o the object to be checked
     * @return true if removed
     */
    boolean isRemoved(Object o);

    /**
     * Adds all interested (and centralized) listeners to the given
     * modelelement handle.
     *
     * @deprecated by Linus Tolke as of 0.19.3. This is not used and poor
     *       design so strive to remove it.
     * @param handle the modelelement the listeners are interested in
     */
    void addListenersToModelElement(Object handle);
}
