// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.model.mdr;

import java.util.Collection;
import java.util.Iterator;

import org.argouml.model.CoreHelper;
import org.argouml.model.ExtensionMechanismsFactory;
import org.argouml.model.ExtensionMechanismsHelper;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.foundation.core.Stereotype;
import org.omg.uml.foundation.core.TagDefinition;
import org.omg.uml.foundation.core.TaggedValue;
import org.omg.uml.foundation.datatypes.Multiplicity;

/**
 * Factory to create UML classes for the UML ExtensionMechanisms
 * package.
 * 
 * TODO: Change visibility to package after reflection problem solved.
 * <p>
 * @since ARGO0.19.5
 * @author Ludovic Ma&icirc;tre
 * @author Tom Morris
 * <p>
 * derived from NSUML implementation by:
 * @author Thierry Lach
 */
public class ExtensionMechanismsFactoryMDRImpl extends
        AbstractUmlModelFactoryMDR implements ExtensionMechanismsFactory {

    /**
     * The model implementation.
     */
    private MDRModelImplementation nsmodel;

    /**
     * The extension mechanism helper.
     */
    private ExtensionMechanismsHelper extensionHelper;

    /**
     * Don't allow instantiation.
     * 
     * @param implementation
     *            To get other helpers and factories.
     */
    ExtensionMechanismsFactoryMDRImpl(MDRModelImplementation implementation) {
        nsmodel = implementation;
        extensionHelper = implementation.getExtensionMechanismsHelper();
    }

    /*
     * @see org.argouml.model.ExtensionMechanismsFactory#createTaggedValue()
     */
    public Object createTaggedValue() {
        TaggedValue tv = nsmodel.getUmlPackage().getCore().getTaggedValue().
                createTaggedValue();
        super.initialize(tv);
        return tv;
    }

    /**
     * Get an instance of a UML TagDefinition.
     * 
     * @param tagName The name of the TagDefinition to create/retrieve
     * @return an initialized UML TaggedValue instance.
     */
    Object getTagDefinition(String tagName) {
        if (tagName == null) {
            throw new IllegalArgumentException("Argument may not be null");
        }
        
        // Look for a TagDefinition matching the given name
        for (Iterator i = nsmodel.getUmlPackage().getCore().getTagDefinition()
                .refAllOfClass().iterator(); i.hasNext();) {
            TagDefinition td = (TagDefinition) i.next();
            if (tagName.equals(td.getName())) {
                return td;
            }
        }
        
        // Create a new TagDefinition if none found
        Object rootModel = nsmodel.getModelManagementFactory().getRootModel();
        TagDefinition td = (TagDefinition) buildTagDefinition(tagName, null,
                rootModel);

        super.initialize(td);
        return td;
    }

    /*
     * TODO: MVW: This needs rethinking/rework! I have the following questions:
     * Why does it not search for a stereotype in the namespace using properties
     * and only create a new stereotype if it will actually be used? Ie, why is
     * there not a getStereotype(String name, String baseClass)? (edited by
     * d00mst)  <these comments imported from NSUML implementation - tfm>
     * 
     * @see org.argouml.model.ExtensionMechanismsFactory#buildStereotype(java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public Object buildStereotype(
            Object theModelElementObject,
            Object theName,
            Object theNamespaceObject) {
        
        if (theModelElementObject == null || theName == null
                || theNamespaceObject == null) {
            throw new IllegalArgumentException("one of the arguments is null");
        }
        
        ModelElement me = (ModelElement) theModelElementObject;
        
        String text = (String) theName;
        Namespace ns = (Namespace) theNamespaceObject;
        Stereotype stereo = buildStereotype(text);
        stereo.getBaseClass().add(extensionHelper.getMetaModelName(me));
        // TODO: this doesn't look right - review - tfm
        Stereotype stereo2 = (Stereotype) extensionHelper.getStereotype(ns,
                stereo);
        if (stereo2 != null) {
            me.getStereotype().add(stereo2);
            nsmodel.getUmlFactory().delete(stereo);
            return stereo2;
        }
        stereo.setNamespace(ns);
        me.getStereotype().add(stereo);
        return stereo;
    }

    /*
     * @see org.argouml.model.ExtensionMechanismsFactory#buildStereotype(java.lang.Object, java.lang.String, java.lang.Object, java.util.Collection)
     */
    public Object buildStereotype(
            Object theModelElementObject,
            String theName,
            Object model,
            Collection models) {
        
        ModelElement me = (ModelElement) theModelElementObject;

        Stereotype stereo = buildStereotype(theName);
        stereo.getBaseClass().add(
                nsmodel.getExtensionMechanismsHelper().getMetaModelName(me));
        Stereotype stereo2 = (Stereotype) extensionHelper.getStereotype(models,
                stereo);
        if (stereo2 != null) {
            me.getStereotype().add(stereo2);
            nsmodel.getUmlFactory().delete(stereo);
            return stereo2;
        }
        stereo.setNamespace((org.omg.uml.modelmanagement.Model) model);
        if (me != null) {
            me.getStereotype().add(stereo);
        }
        return stereo;
    }

    /*
     * Builds an initialized stereotype with no namespace. A stereotype must
     * have a namespace so this method is unsafe. Use buildStereotype(String,
     * Object).
     * 
     * @param text
     *            is the name of the stereotype
     * @return an initialized stereotype.
     */
    private Stereotype buildStereotype(String text) {
        Stereotype stereotype = nsmodel.getUmlPackage().getCore().
                getStereotype().createStereotype();
        super.initialize(stereotype);
        stereotype.setName(text);
        return stereotype;
    }

    /*
     * @see org.argouml.model.ExtensionMechanismsFactory#buildStereotype(java.lang.String, java.lang.Object)
     */
    public Object buildStereotype(String text, Object ns) {
        if (!(ns instanceof Namespace)) {
            throw new IllegalArgumentException(
                    "Namespace is not of the good type! text:" + text + ",ns:"
                            + ns);
        }
        Stereotype stereo = buildStereotype(text);
        if (ns != null && ns instanceof Namespace) {
            stereo.setNamespace((Namespace) ns);
        }
        return stereo;
    }

    /*
     * @see org.argouml.model.ExtensionMechanismsFactory#buildTaggedValue(java.lang.String, java.lang.String)
     */
    public Object buildTaggedValue(String tag, String value) {
        TaggedValue tv = (TaggedValue) createTaggedValue();
        TagDefinition td = (TagDefinition) getTagDefinition(tag);
        td = 
            (TagDefinition) 
            nsmodel.getModelManagementHelper().getCorrespondingElement(
                td, nsmodel.getModelManagementFactory().getRootModel(), true);
        tv.setType(td);
        // TODO: Some CASE tools appear to manage only one
        // dataValue. This is an array of String according to the
        // UML 1.4 specs.
        tv.getDataValue().add(value);
        return tv;
    }


    /**
     * @param elem
     *            the stereotype
     */
    void deleteStereotype(Object elem) {
        if (!(elem instanceof Stereotype)) {
            throw new IllegalArgumentException();
        }
        nsmodel.getUmlHelper().deleteCollection(
                ((Stereotype) elem).getDefinedTag());
        nsmodel.getUmlHelper().deleteCollection(
                ((Stereotype) elem).getStereotypeConstraint());
    }

    /**
     * @param elem
     *            the taggedvalue
     */
    void deleteTaggedValue(Object elem) {
        if (!(elem instanceof TaggedValue)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Delete a TagDefinition.
     * 
     * @param elem the element to be deleted
     */
    void deleteTagDefinition(Object elem) {
        if (!(elem instanceof TagDefinition)) {
            throw new IllegalArgumentException();
        }
        // Delete all TaggedValues with this type
        nsmodel.getUmlHelper().deleteCollection(
                nsmodel.getUmlPackage().getCore().getATypeTypedValue()
                        .getTypedValue((TagDefinition) elem));
    }

    /*
     * @see org.argouml.model.ExtensionMechanismsFactory#copyStereotype(java.lang.Object, java.lang.Object)
     */
    public Object copyStereotype(Object source, Object ns) {
        if (!(source instanceof Stereotype)) {
            throw new IllegalArgumentException("source");
        }
        if (!(ns instanceof Namespace)) {
            throw new IllegalArgumentException("namespace");
        }

        Stereotype st = buildStereotype(null);
        ((Namespace) ns).getOwnedElement().add(st);
        doCopyStereotype((Stereotype) source, st);
        return st;
    }

    /*
     * Used by the copy functions. Do not call this function directly.
     * 
     * @param source
     *            The stereotype to copy from.
     * @param target
     *            The object becoming a copy.
     */
    private void doCopyStereotype(Stereotype source, Stereotype target) {
        ((CoreFactoryMDRImpl) nsmodel.getCoreFactory())
                .doCopyGeneralizableElement(source, target);
        target.getBaseClass().clear();
        target.getBaseClass().addAll(source.getBaseClass());
        target.setIcon(source.getIcon());
        // TODO: constraints
        // TODO: required tags
    }

    /*
     * @see org.argouml.model.ExtensionMechanismsFactory#buildTagDefinition(
     *          java.lang.String, java.lang.Object, java.lang.Object)
     */
    public Object buildTagDefinition(String text, Object owner, Object ns) {
        if (owner != null) {
            if (!(owner instanceof Stereotype)) {
                throw new IllegalArgumentException("owner: " + owner);
            }
            if (ns != null) {
                throw new IllegalArgumentException(
                        "only one of owner & namespace may be specified");
            }
        } else if (!(ns instanceof Namespace)) {
            throw new IllegalArgumentException("namespace: " + ns);
        }
        TagDefinition td = (TagDefinition) createTagDefinition();
        CoreHelper coreHelper = org.argouml.model.Model.getCoreHelper();
        if (owner != null) {
            coreHelper.setOwner(td, owner);
        } else {
            coreHelper.setNamespace(td, ns);
        }
        coreHelper.setName(td, text);
        coreHelper.setMultiplicity(td, org.argouml.model.Model
                .getDataTypesFactory().createMultiplicity(0, 1));
        td.setTagType("String");
        return td;
    }

    /*
     * @see org.argouml.model.ExtensionMechanismsFactory#createTagDefinition()
     */
    public Object createTagDefinition() {
        TagDefinition td = nsmodel.getUmlPackage().getCore().getTagDefinition()
                .createTagDefinition();
        super.initialize(td);
        return td;
    }
    
    /*
     * @see org.argouml.model.ExtensionMechanismsFactory#createStereotype()
     */
    public Object createStereotype() {
        Stereotype st = nsmodel.getUmlPackage().getCore().getStereotype()
            .createStereotype();
        super.initialize(st);
        return st;
    }

    /*
     * @see org.argouml.model.ExtensionMechanismsFactory#copyTagDefinition(java.lang.Object, java.lang.Object)
     */
    public Object copyTagDefinition(Object anElement, Object aNs) {
        if (!(anElement instanceof TagDefinition)) {
            throw new IllegalArgumentException("source: " + anElement);
        }
        if (!(aNs instanceof Namespace || aNs instanceof Stereotype)) {
            throw new IllegalArgumentException("namespace: " + aNs);
        }
        TagDefinition source = (TagDefinition) anElement;
        TagDefinition td = (TagDefinition) createTagDefinition();
        if (aNs instanceof Namespace) {
            td.setNamespace((Namespace) aNs);
        } else {
            td.setOwner((Stereotype) aNs);
        }
        doCopyTagDefinition(source, td);
        return td;
    }
    
    /*
     * Used by the copy functions. Do not call this function directly.
     * 
     * @param source
     *            The stereotype to copy from.
     * @param target
     *            The object becoming a copy.
     */
    private void doCopyTagDefinition(TagDefinition source, 
            TagDefinition target) {
        ((CoreFactoryMDRImpl) nsmodel.getCoreFactory())
                .doCopyModelElement(source, target);
        target.setTagType(source.getTagType());
        String srcMult = org.argouml.model.Model.getFacade().toString(
                source.getMultiplicity());
        target.setMultiplicity((Multiplicity) org.argouml.model.Model
                .getDataTypesFactory().createMultiplicity(srcMult));
    }    
}
