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
 * The interface for the factory for ExstensionMechanisms.
 */
public interface ExtensionMechanismsFactory extends Factory {
    /**
     * Create an empty but initialized instance of a UML Stereotype.
     *
     * @return an initialized UML Stereotype instance.
     */
    //Object createStereotype();

    /**
     * Create an empty but initialized instance of a UML TaggedValue.
     *
     * @return an initialized UML TaggedValue instance.
     */
    Object createTaggedValue();

    /**
     * Builds a stereotype for some kind of modelelement.
     *
     * @param theModelElementObject    a Model Element that the stereotype
     *                                 will be applied to. The stereotype will
     *                                 have its BaseClass set to an appropriate
     *                                 value for this kind of Model Elements.
     * @param theName                  the name for the stereotype
     * @param theNamespaceObject       the namespace the stereotype will be
     *                                 created within.
     * @return                         the resulting stereotype object
     * @throws IllegalArgumentException if either argument is null.
     */
    /*
     * TODO: MVW: This needs rethinking/rework! I have the following questions:
     * Why does it not search for a stereotype in the namespace using 
     * properties and only create a new stereotype if it will actually be used?
     * Ie, why is there not a getStereotype(String name, String baseClass)? 
     * (edited by d00mst)
     */
    Object buildStereotype(Object theModelElementObject,
            Object theName,
            Object theNamespaceObject);

    /**
     * Builds an initialized stereotype.
     *
     * @param theModelElementObject the baseclass for the new stereotype
     * @param theName               the name for the new stereotype
     * @param model the current model of interest
     * @param models all the models
     * @return                      the new stereotype
     */
    Object buildStereotype(Object theModelElementObject, String theName,
            Object model, Collection models);

    /**
     * Builds an initialized stereotype.
     *
     * @param text is the name of the stereotype
     * @param ns namespace where the stereotype lives (is known)
     * @return an initialized stereotype.
     */
    Object buildStereotype(String text, Object ns);

    /**
     * Build an initialized instance of a UML TaggedValue.
     *
     * @param tag is the tag name (a String).
     * @param value is the value (a String).
     * @return an initialized UML TaggedValue instance.
     */
    Object buildTaggedValue(String tag, String value);

    /**
     * Copies a stereotype.
     *
     * @param source is the stereotype to copy.
     * @param ns is the namespace to put the copy in.
     * @return a newly created stereotype
     */
    Object copyStereotype(Object source, Object ns);

    /**
     * Build an initialized instance of a TagDefinition.
     * @param name is the name of the TagDefinition
     * @param stereotype is the optional stereotype owning the TagDefinition
     * @param ns is the namespace to put the TagDefinition.
     * @return a newly created TagDefinition.
     */
    Object buildTagDefinition(String name, Object stereotype, Object ns);

    /**
     * Create a TagDefinition.
     *
     * @return a TagDefinition
     */
    Object createTagDefinition();

    /**
     * @return a Stereotype
     */
    Object createStereotype();

    /**
     * Copy a TagDefinition into the given namespace or stereotype.
     *
     * @param aTd The TagDefinition to copy
     * @param aNs A Namespace or a stereotype into which place the copy
     * @return A copy of the tagdefinition
     */
    Object copyTagDefinition(Object aTd, Object aNs);

}
