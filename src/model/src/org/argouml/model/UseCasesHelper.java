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
 * The interface for the helper for UseCases.<p>
 *
 * Created from the old UseCasesHelper.
 */
public interface UseCasesHelper {
    /**
     * This method returns all extension points of a given use case.<p>
     *
     * @param useCase  The use case for which we want the extension points.
     *
     * @return         A collection of the extension points.
     * @deprecated by tfmorris for 0.23.5 use {@link Facade#getExtensionPoints(Object)}
     */
    Collection getExtensionPoints(Object useCase);

    /**
     * Returns all usecases in some namespace ns.
     *
     * @param ns is the namespace
     * @return Collection
     */
    Collection getAllUseCases(Object ns);

    /**
     * Returns all actors in some namespace ns.
     *
     * @param ns is the namespace
     * @return Collection
     */
    Collection getAllActors(Object ns);

    /**
     * Returns all usecases this given usecase extends.<p>
     *
     * @param ausecase the given usecase
     * @return Collection all usecases this given usecase extends
     */
    Collection getExtendedUseCases(Object ausecase);

    /**
     * @param usecase the given usecase
     * @return Collection all usecases that extend the given usecase
     */
    Collection getExtendingUseCases(Object usecase);

    /**
     * Returns the extend relation between two usecases base and
     * extension. If there is none null is returned.
     *
     * @param abase the given base usecase
     * @param anextension the given extension usecase
     * @return MExtend the extend relation
     */
    Object getExtends(Object abase, Object anextension);

    /**
     * Returns all usecases this usecase includes.
     *
     * @param ausecase the given usecase
     * @return Collection all usecases the given usecase includes
     */
    Collection getIncludedUseCases(Object ausecase);

    /**
     * Returns the include relation between two usecases base and
     * inclusion. If there is none null is returned.
     *
     * @param abase the given base usecase
     * @param aninclusion the given inclusion usecase
     * @return The include relation.
     */
    Object getIncludes(Object abase, Object aninclusion);

    /**
     * Returns the specificationpath operation of some usecase. See
     * section 2.11.3.5 of the UML 1.3 spec for a definition.<p>
     *
     * @param ausecase the given usecase
     * @return Collection the specificationpath operation of the given usecase
     */
    Collection getSpecificationPath(Object ausecase);

    /**
     * Sets the base usecase of a given extend. Updates the
     * extensionpoints of the extend too.
     * @param extend the given extend
     * @param base the base usecase
     */
    void setBase(Object extend, Object base);

    /**
     * Remove an extend to a Use Case or Extension Point.
     *
     * @param elem The Use Case or Extension Point.
     * @param extend The Extend to add.
     */
    void removeExtend(Object elem, Object extend);

    /**
     * This method removes an Extension Point from a Use Case or an Extend.
     *
     * @param elem is The Use Case or Extend.
     * @param ep is the extension point
     */
    void removeExtensionPoint(Object elem, Object ep);

    /**
     * Remove an include from a Use Case.
     *
     * @param usecase The Use Case.
     * @param include The Include.
     */
    void removeInclude(Object usecase, Object include);

    /**
     * Add an extend to a Use Case or Extension Point.
     *
     * @param elem The Use Case or Extension Point.
     * @param extend The Extend to add.
     */
    void addExtend(Object elem, Object extend);

    /**
     * Adds an extension point to some model element.
     *
     * @param handle is the model element
     * @param extensionPoint is the extension point
     */
    void addExtensionPoint(Object handle, Object extensionPoint);

    /**
     * Add an include to a Use Case.
     *
     * @param usecase The Use Case.
     * @param include The Include.
     */
    void addInclude(Object usecase, Object include);

    /**
     * Sets the addition to an include.
     *
     * @param handle Include
     * @param useCase UseCase
     */
    void setAddition(Object handle, Object useCase);

    /**
     * Set the condition of an extend.
     *
     * @param handle is the extend
     * @param booleanExpression is the condition
     */
    void setCondition(Object handle, Object booleanExpression);

    /**
     * Set the extension of a usecase.
     *
     * @param handle Extend
     * @param ext UseCase or null
     */
    void setExtension(Object handle, Object ext);

    /**
     * Sets the extension points of some use cases.
     *
     * @param handle the use case
     * @param extensionPoints is the extension points
     */
    void setExtensionPoints(Object handle, Collection extensionPoints);

    /**
     * Set the collection of Include relationships for a usecase.
     *
     * @param handle UseCase
     * @param includes the collection of Include relationships
     */
    void setIncludes(Object handle, Collection includes);

    /**
     * Sets a location of some extension point.
     *
     * @param handle is the extension point
     * @param loc is the location
     */
    void setLocation(Object handle, String loc);

    /**
     * Set a Use Case for an Extension Point.
     *
     * @param elem The Extension Point.
     * @param usecase The Use Case.
     */
    void setUseCase(Object elem, Object usecase);
}
