/* $Id$
 *******************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *******************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

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


/**
 * The interface for the factory for UseCases.
 */
public interface UseCasesFactory extends Factory {
    /**
     * Create an empty but initialized instance of a Extend.
     *
     * @return an initialized Extend instance.
     */
    Object createExtend();

    /**
     * Create an empty but initialized instance of a ExtensionPoint.
     *
     * @return an initialized ExtensionPoint instance.
     */
    Object createExtensionPoint();

    /**
     * Create an empty but initialized instance of a Actor.
     *
     * @return an initialized Actor instance.
     */
    Object createActor();

    /**
     * Create an empty but initialized instance of a Include.
     *
     * @return an initialized Include instance.
     */
    Object createInclude();

    /**
     * Create an empty but initialized instance of a UseCase.
     *
     * @return an initialized UseCase instance.
     */
    Object createUseCase();


    /**
     * Build an extend relationship with a newly created extension point.
     * Equivalent to calling 3 argument form with null as 3rd argument.<p>
     * 
     * @param abase       The base use case for the relationship
     * @param anextension The extension use case for the relationship
     * @return            The new extend relationship or <code>null</code>
     *                    if it can't be created.
     */
    Object buildExtend(Object abase, Object anextension);

    /**
     * Build an extend relationship.<p>
     * 
     * @param abase
     *            The base use case for the relationship
     * @param anextension
     *            The extension use case for the relationship
     * @param apoint
     *            The extension point in the base for the extension. If null,
     *            one is created.
     * @return The new extend relationship or <code>null</code> if it can't be
     *         created.
     */
    Object buildExtend(Object abase, Object anextension, Object apoint);

    /**
     * Builds an extension point for a use case.
     *
     * @param modelElement The owning use case for the extension point.
     * @return The new extension point.
     * @throws IllegalArgumentException if modelElement isn't a use-case.
     */
    Object buildExtensionPoint(Object modelElement);

    /**
     * Build an include relationship.<p>
     *
     * Set the namespace to the base (preferred) or else extension's
     * namespace. We don't do any checking on base and extension. They
     * should be different, but that is someone else's problem.<p>
     *
     * @param abase      The base use case for the relationship
     *
     * @param anaddition The extension use case for the relationship
     *
     * @return           The new include relationship or <code>null</code> if
     *                   it can't be created.
     */
    Object buildInclude(Object abase, Object anaddition);

    /**
     * Builds an actor in the same namespace as the given actor. If
     * the object is not and actor nothing is built.<p>
     * 
     * TODO: This shouldn't just silently fail if it is passed a bad
     * argument.  This contract will change. - tfm 20070607
     *
     * @param model The namespace.
     * @param actor the given actor
     * @return Actor the newly built actor
     */
    Object buildActor(Object actor, Object model);
}
