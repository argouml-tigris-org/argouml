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
     * Here for completeness, but actually just a wrapper for the NSUML
     * function.<p>
     *
     * @param useCase  The use case for which we want the extension points.
     *
     * @return         A collection of the extension points.
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
}
