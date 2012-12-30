/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    thn
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2008 The Regents of the University of California. All
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.jmi.model.MofClass;
import javax.jmi.reflect.InvalidObjectException;

import org.argouml.model.ExtensionMechanismsHelper;
import org.argouml.model.InvalidElementException;
import org.argouml.model.NotImplementedException;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.foundation.core.Stereotype;
import org.omg.uml.foundation.core.TagDefinition;
import org.omg.uml.foundation.core.TaggedValue;
import org.omg.uml.modelmanagement.Model;
import org.omg.uml.modelmanagement.UmlPackage;

/**
 * Helper class for UML Foundation::ExtensionMechanisms Package.<p>
 *
 * @since ARGO0.19.5
 * @author Ludovic Ma&icirc;tre
 * @author Tom Morris
 * derived from NSUML implementation by:
 * @author Thierry Lach
 */
class ExtensionMechanismsHelperMDRImpl implements ExtensionMechanismsHelper {

    private static final Logger LOG =
        Logger.getLogger(ExtensionMechanismsHelperMDRImpl.class.getName());

    /**
     * The model implementation.
     */
    private MDRModelImplementation modelImpl;

    private static Map<String, String> packageMap =
        new HashMap<String, String>(16);

    /**
     * Don't allow instantiation.
     *
     * @param implementation
     *            To get other helpers and factories.
     */
    ExtensionMechanismsHelperMDRImpl(MDRModelImplementation implementation) {
        modelImpl = implementation;
        packageMap.put("core", "Core");
        packageMap.put("datatypes", "Data_Types");
        packageMap.put("commonbehavior", "Common_Behavior");
        packageMap.put("usecases", "Use_Cases");
        packageMap.put("statemachines", "State_Machines");
        packageMap.put("collaborations", "Collaborations");
        packageMap.put("activitygraphs", "Activity_Graphs");
        packageMap.put("modelmanagement", "Model_Management");
    }


    public Collection<Stereotype> getStereotypes(Object ns) {
        if (!(ns instanceof Namespace)) {
            throw new IllegalArgumentException("A namespace was expected we got " + ns);
        }

        List<Stereotype> l = new ArrayList<Stereotype>();
        // TODO: this could be a huge collection - find a more efficient way
        try {
            for (Object o : ((Namespace) ns).getOwnedElement()) {
                if (o instanceof Stereotype) {
                    l.add((Stereotype) o);
                } else if (o instanceof UmlPackage) {
                    l.addAll(getStereotypes(o));
                }
            }
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
        return l;
    }

    /* TODO: Make this work when the given stereotype
     * has more than one baseclass.
     * TODO: Currently only works for stereotypes where the baseclass is
     * equal to the given one - inheritance does not work.*/
    public Object getStereotype(Object ns, Object stereo) {
        if (!(ns instanceof Namespace)) {
            throw new IllegalArgumentException("namespace");
        }

        if (!(stereo instanceof Stereotype)) {
            throw new IllegalArgumentException("stereotype");
        }

        try {
            String name = ((ModelElement) stereo).getName();
            Collection<String> baseClasses =
                ((Stereotype) stereo).getBaseClass();
            if (name == null || baseClasses.size() != 1) {
                return null;
            }
            String baseClass = baseClasses.iterator().next();

            for (Stereotype o : getStereotypes(ns)) {
                if (name.equals(o.getName())
                        && o.getBaseClass().contains(baseClass)) {
                    return o;
                }
            }
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
        return null;
    }


    public Stereotype getStereotype(Collection models, Object stereo) {
        if (stereo == null) {
            throw new IllegalArgumentException("null argument");
        }
        if (!(stereo instanceof Stereotype)) {
            throw new IllegalArgumentException("stereotype");
        }

        try {
            String name = ((Stereotype) stereo).getName();
            Collection<String> baseClasses =
                ((Stereotype) stereo).getBaseClass();
            if (name == null || baseClasses.size() != 1) {
                return null;
            }
            String baseClass = baseClasses.iterator().next();

            for (Model model : ((Collection<Model>) models)) {
                // TODO: this should call the single namespace form
                // getStereotype(it2.next(); stereo);
                for (Stereotype o : getStereotypes(model)) {
                    if (name.equals(o.getName())
                            && o.getBaseClass().contains(baseClass)) {
                        return o;
                    }
                }
            }
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
        return null;
    }


    @Deprecated
    public String getMetaModelName(Object m) {
        return modelImpl.getMetaTypes().getName(m);
    }


    /*
     * @see org.argouml.model.ExtensionMechanismsHelper#getAllPossibleStereotypes(java.util.Collection, java.lang.Object)
     */
    public Collection<Stereotype> getAllPossibleStereotypes(Collection models,
            Object modelElement) {
        if (modelElement == null) {
            return Collections.emptySet();
        }
        List<Stereotype> ret = new ArrayList<Stereotype>();
        try {
            for (Stereotype stereo : getStereotypes(models)) {
                if (isApplicableStereo(modelElement, stereo)) {
                    ret.add(stereo);
                }
            }
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
        return ret;
    }

    /*
     * @see org.argouml.model.ExtensionMechanismsHelper#getCommonTaggedValueTypes()
     */
    public Collection getCommonTaggedValueTypes() {
        // TODO: still not used, because in ArgoUML String is "hardwired"
        return null;
    }


    /**
     * Can the given stereotype be applied the given class?
     *
     * @param element
     *            the UML element we want to apply the stereotype to
     * @param stereo
     *            the given stereotype
     * @return true if the stereotype may be applied
     */
    private boolean isApplicableStereo(Object element, Stereotype stereo) {
        String metatypeName = modelImpl.getMetaTypes().getName(element);
        MofClass metatype = ((FacadeMDRImpl) modelImpl.getFacade())
                .getMofClass(metatypeName);
        Collection<String> allTypes = getNames(metatype.allSupertypes());
        allTypes.add(metatypeName);

        for (String base : stereo.getBaseClass()) {
            if (allTypes.contains(base)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Convert a collection of elements to a collection of names.
     *
     * @param elements The elements.
     * @return A Collection with {@link String}s.
     */
    private Collection<String> getNames(Collection<MofClass> elements) {
        Collection<String> names = new HashSet<String>();
        for (MofClass element : elements) {
            names.add(element.getName());
        }
        return names;
    }


    public boolean isValidStereotype(Object theModelElement,
            Object theStereotype) {
        if (theModelElement == null) {
            return false;
        }
        return isApplicableStereo(theModelElement, (Stereotype) theStereotype);
    }


    public Collection<Stereotype> getStereotypes(Collection models) {
        List<Stereotype> ret = new ArrayList<Stereotype>();
        try {
            for (Object model : models) {
                if (!(model instanceof Model)) {
                    throw new IllegalArgumentException(
                            "Expected to receive a collection of Models. "
                            + "The collection contained a "
                            + model.getClass().getName());
                }
                ret.addAll(getStereotypes(model));
            }
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
        return ret;
    }


    public void addCopyStereotype(Object modelElement, Object stereotype) {
        modelImpl.getCoreHelper().addStereotype(modelElement, stereotype);
    }



    public boolean isStereotype(Object object, String name, String base) {
        if (!(object instanceof Stereotype)) {
            return false;
        }

        Stereotype st = (Stereotype) object;
        try {
            if (name == null && st.getName() != null) {
                return false;
            }
            if (base == null && !(st.getBaseClass().isEmpty())) {
                return false;
            }

            return name.equals(st.getName())
                && st.getBaseClass().contains(base);
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
    }


    public boolean isStereotypeInh(Object object, String name, String base) {
        if (!(object instanceof Stereotype)) {
            return false;
        }
        try {
            if (isStereotype(object, name, base)) {
                return true;
            }
            /* TODO: mvw: do we really look into super-types of the stereotype,
             * or should we be looking into super-types of the baseclass? */
            Iterator it =
                modelImpl.getCoreHelper().getSupertypes(object).iterator();
            while (it.hasNext()) {
                if (isStereotypeInh(it.next(), name, base)) {
                    return true;
                }
            }
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
        return false;
    }


    public void addExtendedElement(Object handle, Object extendedElement) {
        if (handle instanceof Stereotype
                && extendedElement instanceof ModelElement) {
            ((ModelElement) extendedElement).getStereotype().add(
                    (Stereotype) handle);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or extendedElement: " + extendedElement);
    }


    public void addBaseClass(Object handle, Object baseClass) {
        if (handle instanceof Stereotype) {
            if (baseClass instanceof String) {
                ((Stereotype) handle).getBaseClass().add((String) baseClass);
                return;
            }
            if (baseClass instanceof ModelElement) {
                ((Stereotype) handle).getBaseClass().add(
                        modelImpl.getMetaTypes().getName(baseClass));
                return;
            }
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or baseClass: " + baseClass);
    }

    public void applyProfile(Object handle, Object profile) {
        // do nothing
    }

    public void removeBaseClass(Object handle, Object baseClass) {
        try {
            if (handle instanceof Stereotype) {
                if (baseClass instanceof String) {
                    ((Stereotype) handle).getBaseClass().remove(baseClass);
                    return;
                }
                if (baseClass instanceof ModelElement) {
                    ((Stereotype) handle).getBaseClass().remove(
                            modelImpl.getMetaTypes().getName(baseClass));
                    return;
                }
            }
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or baseClass: " + baseClass);
    }


    public void setIcon(Object handle, Object icon) {
        if (handle instanceof Stereotype
                && (icon == null || icon instanceof String)) {
            ((Stereotype) handle).setIcon((String) icon);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle + " or icon: "
                + icon);
    }


    public void setValueOfTag(Object handle, String value) {
        setDataValues(handle, new String[] {value});
    }

    public void setDataValues(Object handle, String[] values) {
        if (handle instanceof TaggedValue) {
            TaggedValue tv = (TaggedValue) handle;
            Collection<String> dataValues = tv.getDataValue();
            dataValues.clear();
            Collections.addAll(dataValues, values);
        }
    }

    public void addTaggedValue(Object handle, Object taggedValue) {
        if (handle instanceof ModelElement
                && taggedValue instanceof TaggedValue) {
            ((ModelElement) handle).getTaggedValue().add(
                    (TaggedValue) taggedValue);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or taggedValue: " + taggedValue);
    }


    public void removeTaggedValue(Object handle, Object taggedValue) {
        if (handle instanceof ModelElement
                && taggedValue instanceof TaggedValue) {
            ((ModelElement) handle).getTaggedValue().remove(taggedValue);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or taggedValue: " + taggedValue);
    }


    public void setTaggedValue(Object handle, Collection taggedValues) {
        if (handle instanceof ModelElement) {
            Collection tv =
                modelImpl.getFacade().getTaggedValuesCollection(handle);
            if (!tv.isEmpty()) {
                Collection tvs = new ArrayList(tv);
                for (Object value : tvs) {
                    if (!taggedValues.contains(value)) {
                        tv.remove(value);
                    }
                }
            }
            for (Object value : taggedValues) {
                if (!tv.contains(value)) {
                    tv.add(value);
                }
            }
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or taggedValues: " + taggedValues);
    }

    public void setTaggedValue(Object handle, Object property, Object value) {
        throw new NotImplementedException();
    }

    public void setTagType(Object handle, String tagType) {
        if (handle instanceof TagDefinition) {
            // TODO: What type of validation can we do here on tagType?
            ((TagDefinition) handle).setTagType(tagType);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or tagType: " + tagType);
    }

    public void setType(Object handle, Object type) {
        if (type == null || type instanceof TagDefinition) {
            if (handle instanceof TaggedValue) {
                ((TaggedValue) handle).setType((TagDefinition) type);
                return;
            }
        }
        throw new IllegalArgumentException("handle: " + handle + " or type: "
                + type);
    }

    public void unapplyProfile(Object handle, Object profile) {
        // do nothing
    }

    public boolean hasStereotype(Object handle, String name) {
        if (name == null || !(handle instanceof ModelElement)) {
            throw new IllegalArgumentException();
        }
        try {
            ModelElement element = (ModelElement) handle;
            for (Stereotype stereotype : element.getStereotype()) {
                if (name.equals(stereotype.getName())) {
                    return true;
                }
            }
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
        return false;
    }

    public Object makeProfileApplicable(Object handle) {
        // there is nothing to do in UML1.4
        return handle;
    }
}
