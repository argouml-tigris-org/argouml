// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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
 * An abstract Decorator for the {@link ExtensionMechanismsHelper}.
 *
 * @author Bob Tarling
 */
public abstract class AbstractExtensionMechanismsHelperDecorator
	implements ExtensionMechanismsHelper {

    /**
     * The component.
     */
    private ExtensionMechanismsHelper impl;

    /**
     * @param component The component to decorate.
     */
    AbstractExtensionMechanismsHelperDecorator(
            ExtensionMechanismsHelper component) {
        impl = component;
    }

    /**
     * @see org.argouml.model.ExtensionMechanismsHelper#getStereotypes(
     *         java.lang.Object)
     */
    public Collection getStereotypes(Object ns) {
        return impl.getStereotypes(ns);
    }

    /**
     * The component we are decorating.
     *
     * @return Returns the component.
     */
    protected ExtensionMechanismsHelper getComponent() {
        return impl;
    }

    /**
     * @see org.argouml.model.ExtensionMechanismsHelper#getStereotype(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public Object getStereotype(Object ns, Object stereo) {
        return impl.getStereotype(ns, stereo);
    }

    /**
     * @see org.argouml.model.ExtensionMechanismsHelper#getStereotype(
     *         java.util.Collection,
     *         java.lang.Object)
     */
    public Object getStereotype(Collection models, Object stereo) {
        return impl.getStereotype(models, stereo);
    }

    /**
     * @see org.argouml.model.ExtensionMechanismsHelper#getMetaModelName(
     *         java.lang.Object)
     */
    public String getMetaModelName(Object m) {
        return impl.getMetaModelName(m);
    }

    /**
     * @see org.argouml.model.ExtensionMechanismsHelper#getAllPossibleStereotypes(
     *         java.util.Collection,
     *         java.lang.Object)
     */
    public Collection getAllPossibleStereotypes(Collection models,
            Object modelElement) {
        return impl.getAllPossibleStereotypes(models, modelElement);
    }

    /**
     * @see org.argouml.model.ExtensionMechanismsHelper#isValidStereoType(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public boolean isValidStereoType(Object theModelElement,
            Object theStereotype) {
        return impl.isValidStereoType(theModelElement, theStereotype);
    }

    /**
     * @see org.argouml.model.ExtensionMechanismsHelper#getStereotypes(
     *         java.util.Collection)
     */
    public Collection getStereotypes(Collection models) {
        return impl.getStereotypes(models);
    }

    /**
     * @see org.argouml.model.ExtensionMechanismsHelper#addCopyStereotype(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void addCopyStereotype(Object modelElement, Object stereotype) {
        impl.addCopyStereotype(modelElement, stereotype);
    }

    /**
     * @see org.argouml.model.ExtensionMechanismsHelper#isStereotype(
     *         java.lang.Object,
     *         java.lang.String, java.lang.String)
     */
    public boolean isStereotype(Object object, String name, String base) {
        return impl.isStereotype(object, name, base);
    }

    /**
     * @see org.argouml.model.ExtensionMechanismsHelper#isStereotypeInh(
     *         java.lang.Object,
     *         java.lang.String, java.lang.String)
     */
    public boolean isStereotypeInh(Object object, String name, String base) {
        return impl.isStereotypeInh(object, name, base);
    }

    /**
     * @see org.argouml.model.ExtensionMechanismsHelper#addExtendedElement(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void addExtendedElement(Object handle, Object extendedElement) {
        impl.addExtendedElement(handle, extendedElement);
    }

    /**
     * @see org.argouml.model.ExtensionMechanismsHelper#addBaseClass(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void addBaseClass(Object handle, Object baseClass) {
        impl.addBaseClass(handle, baseClass);
    }

    /**
     * @see org.argouml.model.ExtensionMechanismsHelper#removeBaseClass(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void removeBaseClass(Object handle, Object baseClass) {
        impl.removeBaseClass(handle, baseClass);
    }


    /**
     * @see org.argouml.model.ExtensionMechanismsHelper#setIcon(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void setIcon(Object handle, Object icon) {
        impl.setIcon(handle, icon);
    }

    /**
     * @see org.argouml.model.ExtensionMechanismsHelper#setTag(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void setTag(Object handle, Object tag) {
        impl.setTag(handle, tag);
    }

    /**
     * @see org.argouml.model.ExtensionMechanismsHelper#setType(
     *          java.lang.Object, java.lang.Object)
     */
    public void setType(Object handle, Object type) {
        impl.setType(handle, type);
    }
    /**
     * @see org.argouml.model.ExtensionMechanismsHelper#setValueOfTag(
     *         java.lang.Object,
     *         java.lang.String)
     */
    public void setValueOfTag(Object handle, String value) {
        impl.setValueOfTag(handle, value);
    }

    /**
     * @see org.argouml.model.ExtensionMechanismsHelper#addTaggedValue(
     *         java.lang.Object, java.lang.Object)
     */
    public void addTaggedValue(Object handle, Object taggedValue) {
        impl.addTaggedValue(handle, taggedValue);
    }

    /**
     * @see org.argouml.model.ExtensionMechanismsHelper#removeTaggedValue(
     *         java.lang.Object, java.lang.Object)
     */
    public void removeTaggedValue(Object handle, Object taggedValue) {
        impl.removeTaggedValue(handle, taggedValue);
    }

    /**
     * @see org.argouml.model.ExtensionMechanismsHelper#setTaggedValue(
     *         java.lang.Object, java.util.Collection)
     */
    public void setTaggedValue(Object handle, Collection taggedValues) {
        impl.setTaggedValue(handle, taggedValues);
    }

    /**
     * @see org.argouml.model.ExtensionMechanismsHelper#hasStereoType(java.lang.Object, java.lang.String)
     */
    public boolean hasStereoType(Object handle, String name) {
       return impl.hasStereoType(handle, name);
    }



}
