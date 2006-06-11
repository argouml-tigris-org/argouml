// $Id$
// Copyright (c) 2005-2006 The Regents of the University of California. All
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
 * The interface for the Helper for Uml.<p>
 *
 * Created from the old UmlHelper.
 */
public interface UmlHelper {
    /**
     * Ensures that all of the elements in a model are registered
     * to the UmlModelListener.  This is useful when the MModel is
     * not created by the UmlFactory.
     *
     * @param model the UML model
     */
    void addListenersToModel(Object model);

    /**
     * Returns the owner of some modelelement object. In most cases this will be
     * the owning namespace but in some cases it will be null (the root model)
     * or for instance the owning class with an attribute.
     * 
     * @param handle
     *            the modelelement
     * @return Object the owner
     * @deprecated for 0.21 by tfmorris - use getModelElementContainer - this
     *             was never implemented for MDR, so any use would throw a run
     *             time exception. Will be removed quickly because of this.
     * @see Facade#getModelElementContainer(Object)
     */
    Object getOwner(Object handle);

    /**
     * Utility method to quickly delete a collection of modelelements. This
     * method should only be called from within the model component. The only
     * reason it is public is that the other helpers/factories are in other
     * packages and therefore cannot see this method if it is not public.
     *
     * @param col a collection of modelelements
     */
    void deleteCollection(Collection col);

    /**
     * Returns the source of some relationship.
     * This is the element in binary relations from which a relation 'departs'.
     *
     * @param relationShip the relationship to be tested
     * @return the source of the relationship
     */
    Object getSource(Object relationShip);

    /**
     * Returns the destination of some relationship.
     * This is the element in binary relations at which a relation 'arrives'.
     *
     * @param relationShip  the relationship to be tested
     * @return the destination of the relationship
     */
    Object getDestination(Object relationShip);
}
