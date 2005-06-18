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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.argouml.model.ExtensionMechanismsHelper;

import ru.novosoft.uml.behavior.use_cases.MExtensionPoint;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;
import ru.novosoft.uml.foundation.extension_mechanisms.MTaggedValue;
import ru.novosoft.uml.model_management.MModel;

/**
 * Helper class for UML Foundation::ExtensionMechanisms Package.
 *
 * Current implementation is a placeholder.
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
 */
class ExtensionMechanismsHelperImpl implements ExtensionMechanismsHelper {

    /**
     * The model implementation.
     */
    private NSUMLModelImplementation nsmodel;

    /**
     * Don't allow instantiation.
     *
     * @param implementation To get other helpers and factories.
     */
    ExtensionMechanismsHelperImpl(NSUMLModelImplementation implementation) {
        nsmodel = implementation;
    }

    /**
     * Returns all stereotypes in a namespace, but not those in a subnamespace.
     *
     * @param ns is the namespace.
     * @return a Collection with the stereotypes.
     */
    public Collection getStereotypes(Object ns) {
        if (!(ns instanceof MNamespace)) {
            throw new IllegalArgumentException();
        }

        List l = new ArrayList();
        if (ns == null) {
            return l;
        }
        Iterator it = ((MNamespace) ns).getOwnedElements().iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof MStereotype) {
                l.add(o);
            }
        }
        return l;
    }

    /**
     * Finds a stereotype in some namespace, but not in its subnamespaces.
     * Returns null if no such stereotype is found.
     *
     * TODO: What if stereo.getName() or stereo.getBaseClass() is null?
     * Then you know immediately that none will be found, but is that the
     * correct answer?
     *
     * @return the stereotype found or null.
     * @param ns is the namespace.
     * @param stereo is the stereotype.
     */
    public Object getStereotype(Object ns, Object stereo) {
        if (!(ns instanceof MNamespace)) {
            throw new IllegalArgumentException("namespace");
        }

        if (!(stereo instanceof MStereotype)) {
            throw new IllegalArgumentException("stereotype");
        }

        String name = ((MModelElement) stereo).getName();
        String baseClass = ((MStereotype) stereo).getBaseClass();
        Iterator it = getStereotypes(ns).iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof MStereotype && ((MStereotype) o).getName() != null
                    && ((MStereotype) o).getName().equals(name)
                    && ((MStereotype) o).getBaseClass() != null
                    && ((MStereotype) o).getBaseClass().equals(baseClass)) {

                return (MStereotype) o;

            }
        }
        return null;
    }

    /**
     * Searches for a stereotype just like the given stereotype in all models
     * in the current project.
     * The given stereotype can not have its namespace set yet;
     * otherwise it will be returned itself!
     *
     * TODO: Should it only search for stereotypes owned by the Model object?
     *
     * @param models a collection of models
     * @param stereo is the given stereotype
     * @return Stereotype
     */
    public Object getStereotype(Collection models, Object stereo) {
        if (stereo == null) {
            return null;
        }
        if (!(stereo instanceof MStereotype)) {
            throw new IllegalArgumentException("stereotype");
        }

        String name = ((MModelElement) stereo).getName();
        String baseClass = ((MStereotype) stereo).getBaseClass();
        if (name == null || baseClass == null) {
            return null;
        }
        Iterator it2 = models.iterator();
        while (it2.hasNext()) {
            MModel model = (MModel) it2.next();
            Iterator it = getStereotypes(model).iterator();
            while (it.hasNext()) {
                Object o = it.next();
                if (o instanceof MStereotype
                        && ((MStereotype) o).getName().equals(name)
                        && ((MStereotype) o).getBaseClass().equals(baseClass)) {

                    return (MStereotype) o;

                }
            }
        }
        return null;
    }

    /**
     * @param m the ModelElement
     * @return the meta name of the ModelElement
     */
    public String getMetaModelName(Object m) {
        if (m == null) {
            return null;
        }
        if (!(m instanceof MModelElement)) {
            throw new IllegalArgumentException();
        }

        return getMetaModelName(m.getClass());
    }

    /**
     * @param clazz the UML class
     * @return the meta name of the UML class
     */
    protected String getMetaModelName(Class clazz) {
        if (clazz == null) {
            return null;
        }
        String name = clazz.getName();
        name = name.substring(name.lastIndexOf('.') + 2, name.length());
        if (name.endsWith("Impl")) {
            name = name.substring(0, name.lastIndexOf("Impl"));
        }
        return name;
    }

    /**
     * Returns all possible stereotypes for some
     * modelelement. Possible stereotypes are those stereotypes that
     * are owned by the same namespace the modelelement is owned by
     * and that have a baseclass that is the same as the
     * metamodelelement name of the modelelement.
     *
     * @param modelElement is the model element
     * @param models the models to search in
     * @return Collection
     */
    public Collection getAllPossibleStereotypes(Collection models,
            Object modelElement) {
        MModelElement m = (MModelElement) modelElement;
        List ret = new ArrayList();
        if (m == null) {
            return ret;
        }
        // euluis 2005-06-04 - It must check if the modelElement is contained
        // in a sub-package of one of the models. In this case this
        // sub-package and containing packages should be added to the models
        // to be searched for, before calling getStereotypes.
        Collection nameSpaces = new ArrayList(models);
        Iterator iModels = models.iterator();
        while (iModels.hasNext()) {
            if (iModels.next() == m.getModel()) {
                MModelElement me = m;
                while (m.getModel() != (me = me.getNamespace()) && me != null) {
                    nameSpaces.add(me);
                }
                break; // done - namespace hierarchy added
            }
        }

        Iterator itNs = nameSpaces.iterator();
        while (itNs.hasNext()) {
            Iterator it = getStereotypes(itNs.next()).iterator();
            while (it.hasNext()) {
                MStereotype stereo = (MStereotype) it.next();
                if (isValidStereoType(m.getClass(), stereo)) {
                    ret.add(stereo);
                }
            }
        }
        return ret;
    }

    /**
     * This function answers the question:
     * Can we apply the given stereotype to the given class?
     *
     * @param clazz the class we want to apply the stereotype to
     * @param stereo the given stereotype
     * @return true if the stereotype may be applied
     */
    private boolean isValidStereoType(Class clazz, Object stereo) {
        if (clazz == null || stereo == null) {
            return false;
        }
        if (getMetaModelName(clazz).equals(
                nsmodel.getFacade().getBaseClass(stereo))) {
            return true;
        }
        if (getMetaModelName(clazz).equals("ModelElement")) {
            return false;
        }
        return isValidStereoType(clazz.getSuperclass(), stereo);
    }

    /**
     * Returns true if the given stereotype has a baseclass that
     * equals the baseclass of the given modelelement or one of the
     * superclasses of the given modelelement.
     *
     * @param theModelElement is the model element
     * @param theStereotype   is the stereotype
     * @return boolean
     */
    public boolean isValidStereoType(Object theModelElement,
            Object theStereotype) {
        if (theModelElement == null) {
            return false;
        }
        return isValidStereoType(theModelElement.getClass(), theStereotype);
    }

    /**
     * Get all stereotypes from all Models in the list.
     *
     * Finds only stereotypes owned by the Model objects themselves.
     *
     * @return the collection of stereotypes in all models
     *         in the current project
     * @param models the models to search
     * @throws ClassCastException if an member in the models is not a Model.
     */
    public Collection getStereotypes(Collection models) {
        List ret = new ArrayList();
        Iterator it = models.iterator();
        while (it.hasNext()) {
            MModel model = (MModel) it.next();
            ret.addAll(getStereotypes(model));
        }
        return ret;
    }

    /**
     * Sets the stereotype of some modelelement. The method also
     * copies a stereotype that is not a part of the current model to
     * the current model.
     *
     * @param modelElement is the model element
     * @param stereotype is the stereotype
     */
    public void setStereoType(Object modelElement, Object stereotype) {
        if (stereotype != null) {
            stereotype = nsmodel.getModelManagementHelper()
                    .getCorrespondingElement(stereotype,
                            nsmodel.getFacade().getModel(modelElement), true);
        }
        nsmodel.getCoreHelper().setStereotype(modelElement, stereotype);
    }

    /**
     * Tests if a stereotype is a stereotype with some name and base class.
     *
     * @param object is the stereotype.
     * @param name is the name of the stereotype.
     * @param base is the base class of the stereotype.
     * @return true if object is a stereotype with the desired characteristics.
     */
    public boolean isStereotype(Object object, String name, String base) {
        if (object == null || !(object instanceof MStereotype)) {
            return false;
        }

        MStereotype st = (MStereotype) object;
        if (name == null && st.getName() != null) {
            return false;
        }
        if (base == null && st.getBaseClass() != null) {
            return false;
        }

        return name.equals(st.getName()) && base.equals(st.getBaseClass());
    }

    /**
     * Tests if a stereotype is or inherits from a stereotype with some
     * name and base class.
     *
     * @param object is the stereotype.
     * @param name is the name of the stereotype.
     * @param base is the base class of the stereotype.
     * @return true if object is a (descendant of a) stereotype with the
     *	desired characteristics.
     */
    public boolean isStereotypeInh(Object object, String name, String base) {
        if (object == null || !(object instanceof MStereotype)) {
            return false;
        }
        if (isStereotype(object, name, base)) {
            return true;
        }
        Iterator it = nsmodel.getCoreHelper().getSupertypes(object).iterator();
        while (it.hasNext()) {
            if (isStereotypeInh(it.next(), name, base)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Add an extended element to a stereotype.
     *
     * @param handle Stereotype
     * @param extendedElement ExtensionPoint
     */
    public void addExtendedElement(Object handle, Object extendedElement) {
        if (handle instanceof MStereotype
                && extendedElement instanceof MExtensionPoint) {
            ((MStereotype) handle)
                    .addExtendedElement((MModelElement) extendedElement);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or extendedElement: " + extendedElement);
    }

    /**
     * Set the baseclass of some stereotype.
     *
     * @param handle the stereotype
     * @param baseClass the baseclass
     */
    public void setBaseClass(Object handle, Object baseClass) {
        if (handle instanceof MStereotype && baseClass instanceof String) {
            ((MStereotype) handle).setBaseClass((String) baseClass);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or baseClass: " + baseClass);
    }

    /**
     * Set the icon for a stereotype.
     *
     * @param handle Stereotype
     * @param icon String
     */
    public void setIcon(Object handle, Object icon) {
        if (handle instanceof MStereotype
                && (icon == null || icon instanceof String)) {
            ((MStereotype) handle).setIcon((String) icon);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle + " or icon: "
                + icon);
    }

    /**
     * Set the Tag of a TaggedValue.
     *
     * @param handle TaggedValue
     * @param tag String
     */
    public void setTag(Object handle, Object tag) {
        if (handle instanceof MTaggedValue && tag instanceof String) {
            ((MTaggedValue) handle).setTag((String) tag);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle + " or tag: "
                + tag);
    }

    /**
     * Sets a value of some taggedValue.
     *
     * @param handle is the tagged value
     * @param value is the value
     */
    public void setValueOfTag(Object handle, String value) {
        if (handle instanceof MTaggedValue) {
            ((MTaggedValue) handle).setValue(value);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }
}

