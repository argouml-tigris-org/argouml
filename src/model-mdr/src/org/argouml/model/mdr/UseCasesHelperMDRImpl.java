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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.jmi.reflect.InvalidObjectException;

import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.argouml.model.UseCasesHelper;
import org.omg.uml.behavioralelements.usecases.Actor;
import org.omg.uml.behavioralelements.usecases.Extend;
import org.omg.uml.behavioralelements.usecases.ExtensionPoint;
import org.omg.uml.behavioralelements.usecases.Include;
import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.foundation.core.UmlClass;
import org.omg.uml.foundation.datatypes.BooleanExpression;
import org.omg.uml.modelmanagement.Subsystem;

/**
 * UseCase Helper for MDR ModelImplementation.<p>
 *
 * @since ARGO0.19.5
 * @author Ludovic Ma&icirc;tre
 * @author Tom Morris

 */
class UseCasesHelperMDRImpl implements UseCasesHelper {

    /**
     * The model implementation.
     */
    private MDRModelImplementation modelImpl;

    /**
     * Don't allow instantiation.
     *
     * @param implementation
     *            To get other helpers and factories.
     */
    UseCasesHelperMDRImpl(MDRModelImplementation implementation) {
        modelImpl = implementation;
    }

    /*
     * @see org.argouml.model.UseCasesHelper#getAllUseCases(java.lang.Object)
     */
    public Collection getAllUseCases(Object ns) {
        if (!(ns instanceof Namespace)) {
            throw new IllegalArgumentException();
        }

        List list = new ArrayList();
        try {
            Iterator it = ((Namespace) ns).getOwnedElement().iterator();
            while (it.hasNext()) {
                Object o = it.next();
                if (o instanceof Namespace) {
                    list.addAll(getAllUseCases(o));
                }
                if (o instanceof UseCase) {
                    list.add(o);
                }
                
            }
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
        return list;
    }

    /*
     * @see org.argouml.model.UseCasesHelper#getAllActors(java.lang.Object)
     */
    public Collection getAllActors(Object ns) {
        if (!(ns instanceof Namespace)) {
            throw new IllegalArgumentException();
        }

        List list = new ArrayList();
        try {
            Iterator it = ((Namespace) ns).getOwnedElement().iterator();
            while (it.hasNext()) {
                Object o = it.next();
                if (o instanceof Namespace) {
                    list.addAll(getAllActors(o));
                }
                if (o instanceof Actor) {
                    list.add(o);
                }
            }
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
        return list;
    }

    /*
     * @see org.argouml.model.UseCasesHelper#getExtendedUseCases(java.lang.Object)
     */
    public Collection getExtendedUseCases(Object ausecase) {
        if (ausecase == null) {
            return new ArrayList();
        }
        List list = new ArrayList();
        UseCase usecase = (UseCase) ausecase;
        try {
            Iterator it = usecase.getExtend().iterator();
            while (it.hasNext()) {
                Extend extend = (Extend) it.next();
                UseCase base = extend.getBase();
                list.add(base);
            }
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
        return list;
    }

    /*
     * @see org.argouml.model.UseCasesHelper#getExtendingUseCases(java.lang.Object)
     */
    public Collection getExtendingUseCases(Object usecase) {
        if (usecase == null) {
            return new ArrayList();
        }
        List list = new ArrayList();
        try {
            Iterator it = Model.getFacade().getExtenders(usecase).iterator();
            while (it.hasNext()) {
                Extend ext = (Extend) it.next();
                UseCase extension = ext.getExtension();
                list.add(extension);
            }
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
        return list;
    }

    /*
     * @see org.argouml.model.UseCasesHelper#getExtends(java.lang.Object, java.lang.Object)
     */
    public Object getExtends(Object abase, Object anextension) {
        if (!(abase instanceof UseCase)
                || !(anextension instanceof UseCase)) {
            throw new IllegalArgumentException();
        }
        UseCase base = (UseCase) abase;
        UseCase extension = (UseCase) anextension;
        try {
            Iterator it = extension.getExtend().iterator();
            while (it.hasNext()) {
                Extend extend = (Extend) it.next();
                if (extend.getBase() == base) {
                    return extend;
                }
            }
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
        return null;
    }

    /*
     * @see org.argouml.model.UseCasesHelper#getIncludedUseCases(java.lang.Object)
     */
    public Collection getIncludedUseCases(Object ausecase) {
        if (!(ausecase instanceof UseCase)) {
            throw new IllegalArgumentException();
        }
        List list = new ArrayList();
        UseCase usecase = (UseCase) ausecase;
        try {
            Iterator it = usecase.getInclude().iterator();
            while (it.hasNext()) {
                Include include = (Include) it.next();
                UseCase addition = include.getBase();
                list.add(addition);
            }
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
        return list;
    }

    /*
     * @see org.argouml.model.UseCasesHelper#getIncludes(java.lang.Object, java.lang.Object)
     */
    public Object getIncludes(Object abase, Object aninclusion) {
        if (!(abase instanceof UseCase)
                || !(aninclusion instanceof UseCase)) {
            throw new IllegalArgumentException();
        }
        UseCase base = (UseCase) abase;
        UseCase inclusion = (UseCase) aninclusion;
        try {
            Iterator it = inclusion.getInclude().iterator();
            while (it.hasNext()) {
                Include include = (Include) it.next();
                if (include.getBase() == base) {
                    return include;
                }
            }
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
        return null;
    }

    /*
     * @see org.argouml.model.UseCasesHelper#getSpecificationPath(java.lang.Object)
     */
    public Collection getSpecificationPath(Object ausecase) {
        UseCase uc = (UseCase) ausecase;
        Set set = new HashSet();
        try {
            set.addAll(modelImpl.getModelManagementHelper().
                    getAllSurroundingNamespaces(uc));
            Set set2 = new HashSet();
            Iterator it = set.iterator();
            while (it.hasNext()) {
                Object o = it.next();
                if (o instanceof Subsystem || o instanceof UmlClass) {
                    set2.add(o);
                }
            }
            return set2;
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
    }

    /*
     * @see org.argouml.model.UseCasesHelper#setBase(java.lang.Object, java.lang.Object)
     */
    public void setBase(Object extend, Object base) {
        if (base == null) {
            throw new IllegalArgumentException(
                    "The base cannot be null");
        }

        if (!(base instanceof UseCase)) {
            throw new IllegalArgumentException(
                    "The base cannot be a " + base.getClass().getName());
        }

        if (extend == null) {
            throw new IllegalArgumentException("extend");
        }

        if (extend instanceof Extend) {
            Extend theExtend = ((Extend) extend);
            if (base == theExtend.getBase()) {
                return;
            }
            Iterator it = theExtend.getExtensionPoint().iterator();
            while (it.hasNext()) {
                ExtensionPoint point = (ExtensionPoint) it.next();
                removeExtend(point, theExtend);
            }
            ExtensionPoint point =
                (ExtensionPoint) modelImpl.
                    getUseCasesFactory().buildExtensionPoint(base);
            theExtend.setBase((UseCase) base);
            addExtensionPoint(theExtend, point);
        } else if (extend instanceof Include) {
            // TODO: this can be simplified to just
            //((Include) extend).setBase((UseCase) base);
            Include theInclude = ((Include) extend);
            if (base == theInclude.getBase()) {
                return;
            }
            // TODO: This looks backwards. Left over from issue 2034?
            theInclude.setAddition((UseCase) base);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /*
     * @see org.argouml.model.UseCasesHelper#removeExtend(java.lang.Object, java.lang.Object)
     */
    public void removeExtend(Object elem, Object extend) {
        try {
            if (elem instanceof UseCase && extend instanceof Extend) {
                ((UseCase) elem).getExtend().remove(extend);
                return;
            }
            if (elem instanceof ExtensionPoint && extend instanceof Extend) {
                ((Extend) extend).getExtensionPoint().remove(elem);
                return;
            }
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
        throw new IllegalArgumentException("elem: " + elem + " or extend: "
                + extend);
    }

    /*
     * @see org.argouml.model.UseCasesHelper#removeExtensionPoint(java.lang.Object, java.lang.Object)
     */
    public void removeExtensionPoint(Object elem, Object ep) {
        try {
            if (ep instanceof ExtensionPoint) {
                if (elem instanceof UseCase) {
                    ((UseCase) elem).getExtensionPoint().remove(ep);
                    return;
                }
                if (elem instanceof Extend) {
                    ((Extend) elem).getExtensionPoint().remove(ep);
                    return;
                }
            }
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
        throw new IllegalArgumentException("elem: " + elem + " or ep: " + ep);
    }

    /*
     * @see org.argouml.model.UseCasesHelper#removeInclude(java.lang.Object, java.lang.Object)
     */
    public void removeInclude(Object usecase, Object include) {
        try {
            if (usecase instanceof UseCase && include instanceof Include) {
                ((UseCase) usecase).getInclude().remove(include);
                return;
            }
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
        throw new IllegalArgumentException("usecase: " + usecase
                + " or include: " + include);
    }

    /*
     * @see org.argouml.model.UseCasesHelper#addExtend(java.lang.Object, java.lang.Object)
     */
    public void addExtend(Object elem, Object extend) {
        if (elem instanceof UseCase && extend instanceof Extend) {
            ((UseCase) elem).getExtend().add(extend);
            return;
        }
        if (elem instanceof ExtensionPoint && extend instanceof Extend) {
            ((Extend) extend).getExtensionPoint().add(elem);
            return;
        }

        throw new IllegalArgumentException("elem: " + elem + " or extend: "
                + extend);
    }

    /*
     * @see org.argouml.model.UseCasesHelper#addExtensionPoint(java.lang.Object, java.lang.Object)
     */
    public void addExtensionPoint(Object handle, Object extensionPoint) {
        if (extensionPoint instanceof ExtensionPoint) {
            if (handle instanceof UseCase) {
                ((UseCase) handle).getExtensionPoint().add(extensionPoint);
                return;
            }
            if (handle instanceof Extend) {
                ((Extend) handle).getExtensionPoint().add(extensionPoint);
                return;
            }
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or extensionPoint: " + extensionPoint);
    }

    /*
     * @see org.argouml.model.UseCasesHelper#addExtensionPoint(java.lang.Object, int, java.lang.Object)
     */
    public void addExtensionPoint(Object handle, int position, 
            Object extensionPoint) {
        if (extensionPoint instanceof ExtensionPoint) {
            if (handle instanceof Extend) {
                ((Extend) handle).getExtensionPoint().add(position, 
                        extensionPoint);
                return;
            }
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or extensionPoint: " + extensionPoint);
    }

    /*
     * @see org.argouml.model.UseCasesHelper#addInclude(java.lang.Object, java.lang.Object)
     */
    public void addInclude(Object usecase, Object include) {
        if (usecase instanceof UseCase && include instanceof Include) {
            ((UseCase) usecase).getInclude().add(include);
            return;
        }

        throw new IllegalArgumentException("usecase: " + usecase
                + " or include: " + include);
    }

    /*
     * @see org.argouml.model.UseCasesHelper#setAddition(java.lang.Object, java.lang.Object)
     */
    public void setAddition(Object handle, Object useCase) {
        if (!(useCase instanceof UseCase)) {
            throw new IllegalArgumentException("A UseCase was expected ["
                    + useCase + "]");
        }

        if (handle instanceof Include) {
            try {
                ((Include) handle).setAddition((UseCase) useCase);
                return;
            } catch (InvalidObjectException e) {
                throw new IllegalArgumentException(
                        "Operation on a removed object [" + handle + " or "
                                + useCase + "]");
            }
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /*
     * @see org.argouml.model.UseCasesHelper#setCondition(java.lang.Object, java.lang.Object)
     */
    public void setCondition(Object handle, Object booleanExpression) {
        if (handle instanceof Extend
                && booleanExpression instanceof BooleanExpression) {
            ((Extend) handle)
                    .setCondition((BooleanExpression) booleanExpression);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or booleanExpression: " + booleanExpression);
    }

    /*
     * @see org.argouml.model.UseCasesHelper#setExtension(java.lang.Object, java.lang.Object)
     */
    public void setExtension(Object handle, Object useCase) {
        if (!(useCase instanceof UseCase)) {
            throw new IllegalArgumentException("A use case must be supplied");
        }

        if (handle instanceof Extend
                && (useCase instanceof UseCase)) {
            try {
                ((Extend) handle).setExtension((UseCase) useCase);
                return;
            } catch (InvalidObjectException e) {
                throw new IllegalStateException(
                        "Operation on a removed object [" + handle + " or "
                                + useCase + "]");
            }
        }
        throw new IllegalArgumentException(
                "handle: " + handle + " or ext: "
                + useCase);
    }

    /*
     * @see org.argouml.model.UseCasesHelper#setExtensionPoints(java.lang.Object, java.util.Collection)
     */
    public void setExtensionPoints(Object handle, Collection extensionPoints) {
        if (handle instanceof UseCase || handle instanceof Extend) {
            Collection eps = Model.getFacade().getExtensionPoints(handle);
            if (!eps.isEmpty()) {
                Vector extPts = new Vector();
                extPts.addAll(eps);
                Iterator toRemove = extPts.iterator();
                while (toRemove.hasNext()) {
                    removeExtensionPoint(handle, toRemove.next());
                }
            }
            if (!extensionPoints.isEmpty()) {
                Iterator toAdd = extensionPoints.iterator();
                while (toAdd.hasNext()) {
                    addExtensionPoint(handle, toAdd.next());
                }
            }
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or extensionPoints: " + extensionPoints);
    }

    /*
     * @see org.argouml.model.UseCasesHelper#setIncludes(java.lang.Object, java.util.Collection)
     */
    public void setIncludes(Object handle, Collection includes) {
        if (handle instanceof UseCase) {
            Collection inc = Model.getFacade().getIncludes(handle);
            if (!inc.isEmpty()) {
                Vector in = new Vector();
                in.addAll(inc);
                Iterator toRemove = in.iterator();
                while (toRemove.hasNext()) {
                    removeInclude(handle, toRemove.next());
                }
            }
            if (!includes.isEmpty()) {
                Iterator toAdd = includes.iterator();
                while (toAdd.hasNext()) {
                    addInclude(handle, toAdd.next());
                }
            }
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or includes: " + includes);
    }

    /*
     * @see org.argouml.model.UseCasesHelper#setLocation(java.lang.Object, java.lang.String)
     */
    public void setLocation(Object handle, String loc) {
        if (handle instanceof ExtensionPoint) {
            ((ExtensionPoint) handle).setLocation(loc);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /*
     * @see org.argouml.model.UseCasesHelper#setUseCase(java.lang.Object, java.lang.Object)
     */
    public void setUseCase(Object elem, Object usecase) {
        if (elem instanceof ExtensionPoint
                && (usecase instanceof UseCase || usecase == null)) {
            ((ExtensionPoint) elem).setUseCase((UseCase) usecase);
            return;
        }

        throw new IllegalArgumentException("elem: " + elem + " or usecase: "
                + usecase);
    }
}
