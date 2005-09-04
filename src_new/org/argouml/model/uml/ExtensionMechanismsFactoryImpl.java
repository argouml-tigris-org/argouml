// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

import org.apache.log4j.Logger;
import org.argouml.model.ExtensionMechanismsFactory;

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
 * TODO: Change visibility to package after reflection problem solved.
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
 */
public class ExtensionMechanismsFactoryImpl
	extends AbstractUmlModelFactory
	implements ExtensionMechanismsFactory {
    /**
     * Logger.
     */
    private static final Logger LOG =
            Logger.getLogger(ExtensionMechanismsFactoryImpl.class);

    /**
     * The model implementation.
     */
    private NSUMLModelImplementation nsmodel;

    /**
     * Don't allow instantiation.
     *
     * @param implementation To get other helpers and factories.
     */
    ExtensionMechanismsFactoryImpl(NSUMLModelImplementation implementation) {
        nsmodel = implementation;
    }

    /**
     * Create an empty but initialized instance of a UML TaggedValue.
     *
     * @return an initialized UML TaggedValue instance.
     */
    public Object createTaggedValue() {
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
    public Object buildStereotype(Object theModelElementObject,
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
    	MStereotype stereo = (MStereotype) buildStereotype(text);
    	stereo.setBaseClass(nsmodel.getExtensionMechanismsHelper()
			    .getMetaModelName(me));
    	MStereotype stereo2 =
    	    (MStereotype)
    	    	nsmodel.getExtensionMechanismsHelper()
    	    		.getStereotype(ns, stereo);
    	if (stereo2 != null) {
            stereo2.addExtendedElement(me);
            nsmodel.getUmlFactory().delete(stereo);
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
    public Object buildStereotype(
            Object theModelElementObject,
            String theName,
            Object model,
            Collection models) {
        MModelElement me = (MModelElement) theModelElementObject;
        MStereotype stereo = (MStereotype) buildStereotype(theName);
        stereo.setBaseClass(nsmodel.getExtensionMechanismsHelper()
			    .getMetaModelName(me));
        MStereotype stereo2 =
            (MStereotype)
            	nsmodel.getExtensionMechanismsHelper()
            		.getStereotype(models, stereo);
        if (stereo2 != null) {
            stereo2.addExtendedElement(me);
            nsmodel.getUmlFactory().delete(stereo);
            return stereo2;
        }
        ((MModel) model).addOwnedElement(stereo);
        if (me != null) {
            stereo.addExtendedElement(me);
        }
        return stereo;
    }

    /**
     * Builds an initialized stereotype with no namespace.
     * A stereotype
     * must have a namespace so this method is unsafe. Use
     * buildStereotype(String, Object). 
     *
     * @param text is the name of the stereotype
     * @return an initialized stereotype.
     */
    private Object buildStereotype(String text) {
        MStereotype stereotype =
            MFactory.getDefaultFactory().createStereotype();
        super.initialize(stereotype);
        stereotype.setName(text);
        LOG.info("Created a new stereotype of <<" + text + ">>");
        return stereotype;
    }


    /**
     * Builds an initialized stereotype.
     *
     * @param text is the name of the stereotype
     * @param ns namespace where the stereotype lives (is known)
     * @return an initialized stereotype.
     */
    public Object/*MStereotype*/ buildStereotype(String text, Object ns) {
        // TODO: Should throw IllegalArgumentException if ns not
        // instanceof MNamespace or text is not null and no empty.
        // Not willing to do this close to 0.18 release.
        MStereotype stereo = (MStereotype) buildStereotype(text);
    	if (ns != null && ns instanceof MNamespace) {
    	    stereo.setNamespace((MNamespace) ns);
    	}
    	return stereo;
    }

    /**
     * Build an initialized instance of a UML TaggedValue.
     *
     * @param tag is the tag name (a String).
     * @param value is the value (a String).
     * @return an initialized UML TaggedValue instance.
     */
    public Object buildTaggedValue(String tag, String value) {
        MTaggedValue tv = (MTaggedValue) createTaggedValue();
        tv.setTag(tag);
        tv.setValue(value);
        return tv;
    }

    /**
     * @param elem the stereotype
     */
    void deleteStereotype(Object elem) {
        if (!(elem instanceof MStereotype)) {
            throw new IllegalArgumentException();
        }
        // TODO: implement
    }

    /**
     * @param elem the taggedvalue
     */
    void deleteTaggedValue(Object elem) {
        if (!(elem instanceof MTaggedValue)) {
            throw new IllegalArgumentException();
        }
        // TODO: implement
    }

    /**
     * Copies a stereotype.
     *
     * @param source is the stereotype to copy.
     * @param ns is the namespace to put the copy in.
     * @return a newly created stereotype
     */
    public Object copyStereotype(Object source, Object ns) {
        if (!(source instanceof MStereotype)) {
            throw new IllegalArgumentException("source");
        }
        if (!(ns instanceof MNamespace)) {
            throw new IllegalArgumentException("namespace");
        }

        MStereotype st = (MStereotype) buildStereotype(null);
        ((MNamespace) ns).addOwnedElement(st);
        doCopyStereotype((MStereotype) source, st);
	return st;
    }

    /**
     * Used by the copy functions. Do not call this function directly.
     *
     * @param source The stereotype to copy from.
     * @param target The object becoming a copy.
     */
    private void doCopyStereotype(MStereotype source, MStereotype target) {
	((CoreFactoryImpl) nsmodel.getCoreFactory())
		.doCopyGeneralizableElement(source, target);
	target.setBaseClass(source.getBaseClass());
	target.setIcon(source.getIcon());
	// TODO: constraints
	// TODO: required tags
    }
}

