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

import ru.novosoft.uml.behavior.use_cases.MActor;
import ru.novosoft.uml.behavior.use_cases.MExtend;
import ru.novosoft.uml.behavior.use_cases.MExtensionPoint;
import ru.novosoft.uml.behavior.use_cases.MInclude;
import ru.novosoft.uml.behavior.use_cases.MUseCase;
import ru.novosoft.uml.behavior.use_cases.MUseCaseInstance;

/**
 * The interface for the factory for UseCases.<p>
 *
 * Created from the old UseCasesFactory.
 */
public interface UseCasesFactory {
    /**
     * Create an empty but initialized instance of a Extend.
     *
     * @return an initialized Extend instance.
     */
    MExtend createExtend();

    /**
     * Create an empty but initialized instance of a ExtensionPoint.
     *
     * @return an initialized ExtensionPoint instance.
     */
    MExtensionPoint createExtensionPoint();

    /**
     * Create an empty but initialized instance of a Actor.
     *
     * @return an initialized Actor instance.
     */
    MActor createActor();

    /**
     * Create an empty but initialized instance of a Include.
     *
     * @return an initialized Include instance.
     */
    MInclude createInclude();

    /**
     * Create an empty but initialized instance of a UseCase.
     *
     * @return an initialized UseCase instance.
     */
    MUseCase createUseCase();

    /**
     * Create an empty but initialized instance of a UseCaseInstance.
     *
     * @return an initialized UseCaseInstance instance.
     */
    MUseCaseInstance createUseCaseInstance();

    /**
     * Build an extend relationship.<p>
     *
     * Set the namespace to the base (preferred) or else extension's
     * namespace. We don't do any checking on base and extension. They
     * should be different, but that is someone else's problem.<p>
     *
     * @param abase       The base use case for the relationship
     *
     * @param anextension The extension use case for the relationship
     *
     * @return            The new extend relationship or <code>null</code>
     *                    if it can't be created.
     */
    MExtend buildExtend(Object abase, Object anextension);

    /**
     * Build an extend relationship.<p>
     *
     * @param abase       The base use case for the relationship
     * @param anextension The extension use case for the relationship
     * @param apoint      The insertion point for the extension
     * @return            The new extend relationship or <code>null</code>
     *                    if it can't be created.
     */
    MExtend buildExtend(Object abase, Object anextension, Object apoint);

    /**
     * Builds an extension point for a use case.
     *
     * @param modelElement The owning use case for the extension point.
     * @return The new extension point.
     * @throws IllegalArgumentException if modelElement isn't a use-case.
     */
    MExtensionPoint buildExtensionPoint(Object modelElement);

    /**
     * Build an include relationship.<p>
     *
     * Set the namespace to the base (preferred) or else extension's
     * namespace. We don't do any checking on base and extension. They
     * should be different, but that is someone else's problem.<p>
     *
     * <em>Note</em>. There is a bug in NSUML that gets the base and
     * addition associations back to front. We reverse the use of
     * their accessors in the code to correct this.<p>
     *
     * @param abase      The base use case for the relationship
     *
     * @param anaddition The extension use case for the relationship
     *
     * @return           The new include relationship or <code>null</code> if
     *                   it can't be created.
     */
    MInclude buildInclude(Object abase, Object anaddition);

    /**
     * Builds an actor in the same namespace of the given actor. If
     * object is no actor nothing is build. Did not give MActor as an
     * argument but object to seperate argouml better from NSUML.<p>
     *
     * @param model The namespace.
     * @param actor the given actor
     * @return MActor the newly build actor
     */
    MActor buildActor(Object actor, Object model);

    /**
     * @param elem the UML element to be deleted
     */
    void deleteActor(MActor elem);

    /**
     * @param elem the UML element to be deleted
     */
    void deleteExtend(MExtend elem);

    /**
     * @param elem the UML element to be deleted
     */
    void deleteExtensionPoint(MExtensionPoint elem);

    /**
     * @param elem the UML element to be deleted
     */
    void deleteInclude(MInclude elem);

    /**
     * @param elem the UML element to be deleted
     */
    void deleteUseCase(MUseCase elem);

    /**
     * @param elem the UML element to be deleted
     */
    void deleteUseCaseInstance(MUseCaseInstance elem);
}
