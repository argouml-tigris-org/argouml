// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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
    protected AbstractExtensionMechanismsHelperDecorator(
            ExtensionMechanismsHelper component) {
        impl = component;
    }

    /**
     * The component we are decorating.
     *
     * @return Returns the component.
     */
    protected ExtensionMechanismsHelper getComponent() {
        return impl;
    }

    /*
     * @see org.argouml.model.ExtensionMechanismsHelper#getStereotypes(java.lang.Object)
     */
    public Collection getStereotypes(Object ns) {
        return impl.getStereotypes(ns);
    }

    public Object getStereotype(Object ns, Object stereo) {
        return impl.getStereotype(ns, stereo);
    }

    public Object getStereotype(Collection models, Object stereo) {
        return impl.getStereotype(models, stereo);
    }

    public String getMetaModelName(Object m) {
        return impl.getMetaModelName(m);
    }

    public Collection getAllPossibleStereotypes(Collection models,
            Object modelElement) {
        return impl.getAllPossibleStereotypes(models, modelElement);
    }

    /**
     * @deprecated Use {@link #isValidStereotype(Object,Object)} instead
     */
    public boolean isValidStereoType(Object theModelElement,
            Object theStereotype) {
        return isValidStereotype(theModelElement, theStereotype);
    }

    public boolean isValidStereotype(Object theModelElement,
            Object theStereotype) {
        return impl.isValidStereotype(theModelElement, theStereotype);
    }

    public Collection getStereotypes(Collection models) {
        return impl.getStereotypes(models);
    }

    public void addCopyStereotype(Object modelElement, Object stereotype) {
        impl.addCopyStereotype(modelElement, stereotype);
    }

    public boolean isStereotype(Object object, String name, String base) {
        return impl.isStereotype(object, name, base);
    }

    public boolean isStereotypeInh(Object object, String name, String base) {
        return impl.isStereotypeInh(object, name, base);
    }

    public void addExtendedElement(Object handle, Object extendedElement) {
        impl.addExtendedElement(handle, extendedElement);
    }

    public void addBaseClass(Object handle, Object baseClass) {
        impl.addBaseClass(handle, baseClass);
    }

    public void removeBaseClass(Object handle, Object baseClass) {
        impl.removeBaseClass(handle, baseClass);
    }

    public void setIcon(Object handle, Object icon) {
        impl.setIcon(handle, icon);
    }

    @SuppressWarnings("deprecation")
    public void setTag(Object handle, Object tag) {
        impl.setTag(handle, tag);
    }

    public void setTagType(Object handle, String tagType) {
        impl.setType(handle, tagType);
    }
    
    public void setType(Object handle, Object type) {
        impl.setType(handle, type);
    }

    @Deprecated
    public void setValueOfTag(Object handle, String value) {
        impl.setValueOfTag(handle, value);
    }

    public void setDataValues(Object handle, String[] values) {
        impl.setDataValues(handle, values);
    }
    
    public void addTaggedValue(Object handle, Object taggedValue) {
        impl.addTaggedValue(handle, taggedValue);
    }

    public void removeTaggedValue(Object handle, Object taggedValue) {
        impl.removeTaggedValue(handle, taggedValue);
    }

    public void setTaggedValue(Object handle, Collection taggedValues) {
        impl.setTaggedValue(handle, taggedValues);
    }

    /**
     * @deprecated Use {@link #hasStereotype(Object,String)} instead
     */
    public boolean hasStereoType(Object handle, String name) {
        return hasStereotype(handle, name);
    }

    public boolean hasStereotype(Object handle, String name) {
        return impl.hasStereotype(handle, name);
    }

}
