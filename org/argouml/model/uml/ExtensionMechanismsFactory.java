// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

import java.util.Collection;

import ru.novosoft.uml.MFactory;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;
import ru.novosoft.uml.foundation.extension_mechanisms.MTaggedValue;
import ru.novosoft.uml.model_management.MModel;

/**
 * Factory to create UML classes for the UML
 * Foundation::ExtensionMechanisms package.
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
 * @stereotype singleton
 */
public class ExtensionMechanismsFactory extends AbstractUmlModelFactory {

    /** Singleton instance.
     */
    private static final ExtensionMechanismsFactory SINGLETON =
	new ExtensionMechanismsFactory();

    /**
     * Singleton instance access method.
     *
     * @return the factory instance.
     */
    public static ExtensionMechanismsFactory getFactory() {
        return SINGLETON;
    }

    /** Don't allow instantiation
     */
    private ExtensionMechanismsFactory() {
    }

    /** Create an empty but initialized instance of a UML Stereotype.
     *
     *  @return an initialized UML Stereotype instance.
     */
    public Object/*MStereotype*/ createStereotype() {
        MStereotype modelElement =
	    MFactory.getDefaultFactory().createStereotype();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML TaggedValue.
     *
     *  @return an initialized UML TaggedValue instance.
     */
    public MTaggedValue createTaggedValue() {
        MTaggedValue modelElement =
	    MFactory.getDefaultFactory().createTaggedValue();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Builds a stereotype for some kind of modelelement.
     *
     * TODO: MVW: This needs rethinking/rework! I have the following questions:
     *       Why does it not search for a stereotype in the namespace using
     *       properties and only create a new stereotype if it will actually
     *       be used? Ie, why is there not a
     *       getStereotype(String name, String baseClass)? (edited by d00mst)
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
    public MStereotype buildStereotype(Object theModelElementObject,
                                       Object theName,
                                       Object theNamespaceObject) {
        if (theModelElementObject == null
                || theName == null
                || theNamespaceObject == null) {
            throw new IllegalArgumentException("one of the arguments is null");
        }
        MModelElement me = (MModelElement) theModelElementObject;
        String text = (String) theName;
        MNamespace ns = (MNamespace) theNamespaceObject;
    	MStereotype stereo = (MStereotype) createStereotype();
    	stereo.setName(text);
    	stereo.setBaseClass(ExtensionMechanismsHelper.getHelper()
			    .getMetaModelName(me));
    	MStereotype stereo2 =
	    ExtensionMechanismsHelper.getHelper().getStereotype(ns, stereo);
    	if (stereo2 != null) {
            stereo2.addExtendedElement(me);
            UmlFactory.getFactory().delete(stereo);
            return stereo2;
        }
        ns.addOwnedElement(stereo);
        stereo.addExtendedElement(me);
        return stereo;
    }

    /**
     * Builds an initialized stereotype.
     * 
     * @param theModelElementObject the baseclass for the new stereotype
     * @param theName               the name for the new stereotype
     * @param model the current model of interest
     * @param models all the models
     * @return                      the new stereotype
     */
    public MStereotype buildStereotype(
            Object theModelElementObject,
			String theName,
            Object model,
            Collection models) {
        MModelElement me = (MModelElement) theModelElementObject;
        MStereotype stereo = (MStereotype) createStereotype();
        stereo.setName(theName);
        stereo.setBaseClass(ExtensionMechanismsHelper.getHelper()
			    .getMetaModelName(me));
        MStereotype stereo2 =
	    ExtensionMechanismsHelper.getHelper().getStereotype(models, stereo);
        if (stereo2 != null) {
            stereo2.addExtendedElement(me);
            UmlFactory.getFactory().delete(stereo);
            return stereo2;
        }
        ((MModel) model).addOwnedElement(stereo);
        if (me != null)
            stereo.addExtendedElement(me);
        return stereo;
    }

    /**
     * Builds an initialized stereotype.
     *
     * @param text is the name of the stereotype
     * @param ns namespace where the stereotype lives (is known)
     * @return an initialized stereotype.
     */
    public Object/*MStereotype*/ buildStereotype(String text, Object ns) {
    	MStereotype stereo = (MStereotype) createStereotype();
    	stereo.setName(text);
    	if (ns != null && ns instanceof MNamespace)
    	    stereo.setNamespace((MNamespace) ns);
    	return stereo;
    }

    /**
     * Build an initialized instance of a UML TaggedValue.
     *
     * @param tag is the tag name (a String).
     * @param value is the value (a String).
     * @return an initialized UML TaggedValue instance.
     */
    public MTaggedValue buildTaggedValue(String tag, String value) {
        MTaggedValue tv = createTaggedValue();
        tv.setTag(tag);
        tv.setValue(value);
        return tv;
    }

    /**
     * @param elem the stereotype
     */
    public void deleteStereotype(MStereotype elem) { }

    /**
     * @param elem the taggedvalue
     */
    public void deleteTaggedValue(MTaggedValue elem) { }

    /**
     * Copies a stereotype.
     *
     * @param source is the stereotype to copy.
     * @param ns is the namespace to put the copy in.
     * @return a newly created stereotype
     */
    public MStereotype copyStereotype(MStereotype source, MNamespace ns) {
	MStereotype st = (MStereotype) createStereotype();
	ns.addOwnedElement(st);
	doCopyStereotype(source, st);
	return st;
    }

    /**
     * Used by the copy functions. Do not call this function directly.
     */
    private void doCopyStereotype(MStereotype source, MStereotype target) {
	CoreFactory.getFactory().doCopyGeneralizableElement(source, target);
	target.setBaseClass(source.getBaseClass());
	target.setIcon(source.getIcon());
	// TODO: constraints
	// TODO: required tags
    }
}

