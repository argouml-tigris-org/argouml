// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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
 * package.<p>
 * 
 * @since ARGO0.19.5
 * @author Ludovic Ma&icirc;tre
 * @author Tom Morris
 * <p>
 * derived from NSUML implementation by:
 * @author Thierry Lach
 */
class ExtensionMechanismsFactoryMDRImpl extends
        AbstractUmlModelFactoryMDR implements ExtensionMechanismsFactory {

    /**
     * The model implementation.
     */
    private MDRModelImplementation modelImpl;

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
        modelImpl = implementation;
        extensionHelper = implementation.getExtensionMechanismsHelper();
    }

    /*
     * @see org.argouml.model.ExtensionMechanismsFactory#createTaggedValue()
     */
    public TaggedValue createTaggedValue() {
        TaggedValue tv = modelImpl.getUmlPackage().getCore().getTaggedValue().
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
    TagDefinition getTagDefinition(String tagName) {
        if (tagName == null) {
            throw new IllegalArgumentException("Argument may not be null");
        }
        
        // Look for a TagDefinition matching the given name
        for (Iterator i = modelImpl.getUmlPackage().getCore().getTagDefinition()
                .refAllOfClass().iterator(); i.hasNext();) {
            TagDefinition td = (TagDefinition) i.next();
            if (tagName.equals(td.getName())) {
                return td;
            }
        }
        
        // Create a new TagDefinition if none found
        Object rootModel = modelImpl.getModelManagementFactory().getRootModel();
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
    public Stereotype buildStereotype(
            Object theModelElementObject,
            Object theName,
            Object theNamespaceObject) {
        
        if (theModelElementObject == null || theName == null
                || theNamespaceObject == null) {
            throw new IllegalArgumentException(
                    "one of the arguments is null: modelElement="
                    + theModelElementObject
                    + " name=" + theName
                    + " namespace=" + theNamespaceObject);
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
            modelImpl.getUmlFactory().delete(stereo);
            return stereo2;
        }
        stereo.setNamespace(ns);
        me.getStereotype().add(stereo);
        return stereo;
    }


    public Stereotype buildStereotype(
            Object theModelElementObject,
            String theName,
            Object model,
            Collection models) {
        
        ModelElement me = (ModelElement) theModelElementObject;

        Stereotype stereo = buildStereotype(theName);
        stereo.getBaseClass().add(
                modelImpl.getExtensionMechanismsHelper().getMetaModelName(me));
        Stereotype stereo2 = (Stereotype) extensionHelper.getStereotype(models,
                stereo);
        if (stereo2 != null) {
            me.getStereotype().add(stereo2);
            modelImpl.getUmlFactory().delete(stereo);
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
        Stereotype stereotype = modelImpl.getUmlPackage().getCore().
                getStereotype().createStereotype();
        super.initialize(stereotype);
        stereotype.setName(text);
        return stereotype;
    }


    public Stereotype buildStereotype(String text, Object ns) {
        if (!(ns instanceof Namespace)) {
            throw new IllegalArgumentException(
                    "Namespace is wrong type - text:" + text + ",ns:"
                            + ns);
        }
        Stereotype stereo = buildStereotype(text);
        stereo.setNamespace((Namespace) ns);
        return stereo;
    }


    @Deprecated
    public TaggedValue buildTaggedValue(String tag, String value) {
        TaggedValue tv = buildTaggedValue(getTagDefinition(tag));
        tv.getDataValue().add(value);
        return tv;
    }

    private TaggedValue buildTaggedValue(TagDefinition type) {
        TaggedValue tv = createTaggedValue();
        tv.setType(type);
        return tv;
    }
    
    public TaggedValue buildTaggedValue(Object type, String[] values) {
        if (!(type instanceof TagDefinition)) {
            throw new IllegalArgumentException(
                    "TagDefinition required, received - " + type);
        }
        TaggedValue tv = buildTaggedValue((TagDefinition) type);
        for (String value : values) {
            tv.getDataValue().add(value);
        }
        return tv;
    }


    public void copyTaggedValues(Object source, Object target) {    
        if (!(source instanceof ModelElement)
                || !(target instanceof ModelElement)) {
            throw new IllegalArgumentException();
        }

        Iterator it = ((ModelElement) source).getTaggedValue().iterator();
        Collection taggedValues = ((ModelElement) target).getTaggedValue();
        // Clear target so that multiple copies have no effect 
        // (other than inefficiency)
        taggedValues.clear();
        while (it.hasNext()) {
            taggedValues.add(copyTaggedValue((TaggedValue) it.next()));
        }
    }
    
    /**
     * Copy a single TaggedValue and return the copy.
     * 
     * @param source the TaggedValue to copy
     * @return the newly cloned copy
     */
    private Object copyTaggedValue(TaggedValue source) {
        TaggedValue tv = createTaggedValue();
        tv.setType(source.getType());
        tv.getDataValue().addAll(source.getDataValue());
        tv.getReferenceValue().addAll(source.getReferenceValue());
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
        modelImpl.getUmlHelper().deleteCollection(
                ((Stereotype) elem).getDefinedTag());
        modelImpl.getUmlHelper().deleteCollection(
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
        modelImpl.getUmlHelper().deleteCollection(
                modelImpl.getUmlPackage().getCore().getATypeTypedValue()
                        .getTypedValue((TagDefinition) elem));
    }


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
        ((CoreFactoryMDRImpl) modelImpl.getCoreFactory())
                .doCopyGeneralizableElement(source, target);
        target.getBaseClass().clear();
        target.getBaseClass().addAll(source.getBaseClass());
        target.setIcon(source.getIcon());
        // TODO: constraints
        // TODO: required tags
    }

    public TagDefinition buildTagDefinition(String name, Object owner, 
            Object namespace) {
        return buildTagDefinition(name, owner, namespace, null); // "Element");
    }

    public TagDefinition buildTagDefinition(String name, Object owner, 
            Object ns, String tagType) {
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
        coreHelper.setName(td, name);
        coreHelper.setMultiplicity(td, org.argouml.model.Model
                .getDataTypesFactory().createMultiplicity(0, 1));
        td.setTagType(tagType);
        return td;
    }


    public Object createTagDefinition() {
        TagDefinition td = modelImpl.getUmlPackage().getCore()
                .getTagDefinition().createTagDefinition();
        super.initialize(td);
        return td;
    }
    

    public Object createStereotype() {
        Stereotype st = modelImpl.getUmlPackage().getCore().getStereotype()
            .createStereotype();
        super.initialize(st);
        return st;
    }


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
        ((CoreFactoryMDRImpl) modelImpl.getCoreFactory())
                .doCopyModelElement(source, target);
        target.setTagType(source.getTagType());
        String srcMult = org.argouml.model.Model.getFacade().toString(
                source.getMultiplicity());
        target.setMultiplicity((Multiplicity) org.argouml.model.Model
                .getDataTypesFactory().createMultiplicity(srcMult));
    }    
}
