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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.argouml.model.UseCasesHelper;

import ru.novosoft.uml.MBase;
import ru.novosoft.uml.behavior.use_cases.MActor;
import ru.novosoft.uml.behavior.use_cases.MExtend;
import ru.novosoft.uml.behavior.use_cases.MExtensionPoint;
import ru.novosoft.uml.behavior.use_cases.MInclude;
import ru.novosoft.uml.behavior.use_cases.MUseCase;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.data_types.MBooleanExpression;
import ru.novosoft.uml.model_management.MSubsystem;

/**
 * Helper class for UML BehavioralElements::UseCases Package.
 *
 * Current implementation is a placeholder.
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
 */
class UseCasesHelperImpl implements UseCasesHelper {

    /**
     * The model implementation.
     */
    private NSUMLModelImplementation nsmodel;

    /**
     * Don't allow instantiation.
     *
     * @param implementation To get other helpers and factories.
     */
    UseCasesHelperImpl(NSUMLModelImplementation implementation) {
        nsmodel = implementation;
    }

    /**
     * This method returns all extension points of a given use case.<p>
     *
     * Here for completeness, but actually just a wrapper for the NSUML
     * function.<p>
     *
     * @param useCase  The use case for which we want the extension points.
     *
     * @return         A collection of the extension points.
     */
    public Collection getExtensionPoints(Object/*MUseCase*/ useCase) {

        return ((MUseCase) useCase).getExtensionPoints();
    }

    /**
     * Returns all usecases in some namespace ns.
     *
     * @param ns is the namespace
     * @return Collection
     */
    public Collection getAllUseCases(Object ns) {
        if (!(ns instanceof MNamespace)) {
            throw new IllegalArgumentException();
        }

	Iterator it = ((MNamespace) ns).getOwnedElements().iterator();
	List list = new ArrayList();
	while (it.hasNext()) {
	    Object o = it.next();
	    if (o instanceof MNamespace) {
		list.addAll(getAllUseCases(o));
	    }
	    if (o instanceof MUseCase) {
		list.add(o);
	    }

	}
	return list;
    }

    /**
     * Returns all actors in some namespace ns.
     *
     * @param ns is the namespace
     * @return Collection
     */
    public Collection getAllActors(Object ns) {
        if (!(ns instanceof MNamespace)) {
            throw new IllegalArgumentException();
        }

	Iterator it = ((MNamespace) ns).getOwnedElements().iterator();
	List list = new ArrayList();
	while (it.hasNext()) {
	    Object o = it.next();
	    if (o instanceof MNamespace) {
		list.addAll(getAllActors(o));
	    }
	    if (o instanceof MActor) {
		list.add(o);
	    }

	}
	return list;
    }

    /**
     * Returns all usecases this given usecase extends.<p>
     *
     * @param ausecase the given usecase
     * @return Collection all usecases this given usecase extends
     */
    public Collection getExtendedUseCases(Object ausecase) {
	if (ausecase == null) {
	    return new ArrayList();
	}
        MUseCase usecase = (MUseCase) ausecase;
	Iterator it = usecase.getExtends().iterator();
	List list = new ArrayList();
	while (it.hasNext()) {
	    MExtend extend = (MExtend) it.next();
	    MUseCase base = extend.getBase();
	    list.add(base);
	}
	return list;
    }

    /**
     * @param usecase the given usecase
     * @return Collection all usecases that extend the given usecase
     */
    public Collection getExtendingUseCases(Object usecase) {
        if (usecase == null) {
            return new ArrayList();
        }
        Iterator it = ((MUseCase) usecase).getExtends2().iterator();
        List list = new ArrayList();
        while (it.hasNext()) {
            MExtend ext = (MExtend) it.next();
            MUseCase extension = ext.getExtension();
            list.add(extension);
        }
        return list;
    }

    /**
     * Returns the extend relation between two usecases base and
     * extension. If there is none null is returned.
     *
     * @param abase the given base usecase
     * @param anextension the given extension usecase
     * @return MExtend the extend relation
     */
    public Object getExtends(Object/*MUseCase*/ abase,
			     Object/*MUseCase*/ anextension) {
        MUseCase base = (MUseCase) abase;
        MUseCase extension = (MUseCase) anextension;
	if (base == null || extension == null) {
	    return null;
	}
	Iterator it = extension.getExtends().iterator();
	while (it.hasNext()) {
	    MExtend extend = (MExtend) it.next();
	    if (extend.getBase() == base) {
		return extend;
	    }
	}
	return null;
    }

    /**
     * Returns all usecases this usecase includes.
     *
     * @param ausecase the given usecase
     * @return Collection all usecases the given usecase includes
     */
    public Collection getIncludedUseCases(Object/*MUseCase*/ ausecase) {
	if (ausecase == null) {
	    return new ArrayList();
	}
        MUseCase usecase = (MUseCase) ausecase;
	Iterator it = usecase.getIncludes().iterator();
	List list = new ArrayList();
	while (it.hasNext()) {
	    MInclude include = (MInclude) it.next();
	    MUseCase addition = include.getBase();
	    list.add(addition);
	}
	return list;
    }

    /**
     * Returns the include relation between two usecases base and
     * inclusion. If there is none null is returned.
     *
     * @param abase the given base usecase
     * @param aninclusion the given inclusion usecase
     * @return MExtend the include relation
     */
    public Object getIncludes(Object abase, Object aninclusion) {
        MUseCase base = (MUseCase) abase;
        MUseCase inclusion = (MUseCase) aninclusion;
	if (base == null || inclusion == null) {
	    return null;
	}
	Iterator it = inclusion.getIncludes().iterator();
	while (it.hasNext()) {
	    MInclude include = (MInclude) it.next();
	    if (include.getBase() == base) {
		return include;
	    }
	}
	return null;
    }

    /**
     * Returns the specificationpath operation of some usecase. See
     * section 2.11.3.5 of the UML 1.3 spec for a definition.<p>
     *
     * @param ausecase the given usecase
     * @return Collection the specificationpath operation of the given usecase
     */
    public Collection getSpecificationPath(Object ausecase) {
        MUseCase uc = (MUseCase) ausecase;
	Set set = new HashSet();
	set.addAll(nsmodel.getModelManagementHelper()
		   .getAllSurroundingNamespaces(uc));
	Set set2 = new HashSet();
	Iterator it = set.iterator();
	while (it.hasNext()) {
	    Object o = it.next();
	    if (o instanceof MSubsystem || o instanceof MClass) {
		set2.add(o);
	    }
	}
	return set2;
    }

    /**
     * Sets the base usecase of a given extend. Updates the
     * extensionpoints of the extend too.
     *
     * @param extend the given extend
     * @param base the base usecase
     */
    public void setBase(Object extend, Object base) {
        if (base == null) {
            throw new IllegalArgumentException(
                    "The base cannot be null");
        }
        
        if (!(base instanceof MUseCase)) {
            throw new IllegalArgumentException(
                    "The base cannot be a " + base.getClass().getName());
        }


        if (extend == null) {
            throw new IllegalArgumentException("extend");
        }

        if (extend instanceof MExtend) {
            MExtend theExtend = ((MExtend) extend);
            if (base == theExtend.getBase()) {
                return;
            }
            Iterator it = theExtend.getExtensionPoints().iterator();
            while (it.hasNext()) {
                MExtensionPoint point = (MExtensionPoint) it.next();
                point.removeExtend(theExtend);
            }
            MExtensionPoint point =
                (MExtensionPoint)
                nsmodel.getUseCasesFactory().buildExtensionPoint(base);
            theExtend.setBase((MUseCase) base);
            theExtend.addExtensionPoint(point);
        } else  if (extend instanceof MInclude) {
            MInclude theInclude = ((MInclude) extend);
            if (base == theInclude.getBase()) {
                return;
            }
            theInclude.setAddition((MUseCase) base);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Remove an extend to a Use Case or Extension Point.
     *
     * @param elem The Use Case or Extension Point.
     * @param extend The Extend to add.
     */
    public void removeExtend(Object elem, Object extend) {
        if (elem instanceof MUseCase
                && extend instanceof MExtend) {
            ((MUseCase) elem).removeExtend((MExtend) extend);
            return;
        }
        if (elem instanceof MExtensionPoint
                && extend instanceof MExtend) {
            ((MExtensionPoint) elem).removeExtend((MExtend) extend);
            return;
        }

        throw new IllegalArgumentException("elem: " + elem
                + " or extend: " + extend);
    }

    /**
     * This method removes an Extension Point from a Use Case or an Extend.
     *
     * @param elem is The Use Case or Extend.
     * @param ep is the extension point
     */
    public void removeExtensionPoint(Object elem, Object ep) {
        if (elem instanceof MUseCase
                && ep instanceof MExtensionPoint) {
            ((MUseCase) elem).removeExtensionPoint((MExtensionPoint) ep);
            return;
        }
        if (elem instanceof MExtend
                && ep instanceof MExtensionPoint) {
            ((MExtend) elem).removeExtensionPoint((MExtensionPoint) ep);
            return;
        }

        throw new IllegalArgumentException("elem: " + elem + " or ep: " + ep);
    }

    /**
     * Remove an include from a Use Case.
     *
     * @param usecase The Use Case.
     * @param include The Include.
     */
    public void removeInclude(Object usecase, Object include) {
        if (usecase instanceof MUseCase
                && include instanceof MInclude) {
            ((MUseCase) usecase).removeInclude((MInclude) include);
            return;
        }

        throw new IllegalArgumentException("usecase: " + usecase
                + " or include: " + include);
    }

    /**
     * Add an extend to a Use Case or Extension Point.
     *
     * @param elem The Use Case or Extension Point.
     * @param extend The Extend to add.
     */
    public void addExtend(Object elem, Object extend) {
        if (elem instanceof MUseCase
                && extend instanceof MExtend) {
            ((MUseCase) elem).addExtend((MExtend) extend);
            return;
        }
        if (elem instanceof MExtensionPoint
                && extend instanceof MExtend) {
            ((MExtensionPoint) elem).addExtend((MExtend) extend);
            return;
        }

        throw new IllegalArgumentException("elem: " + elem
                + " or extend: " + extend);
    }

    /**
     * Adds an extension point to some model element.
     *
     * @param handle is the model element
     * @param extensionPoint is the extension point
     */
    public void addExtensionPoint(
            Object handle,
            Object extensionPoint) {
        if (extensionPoint instanceof MExtensionPoint) {
            if (handle instanceof MUseCase) {
                ((MUseCase) handle).addExtensionPoint(
                        (MExtensionPoint) extensionPoint);
                return;
            }
            if (handle instanceof MExtend) {
                ((MExtend) handle).addExtensionPoint(
                        (MExtensionPoint) extensionPoint);
                return;
            }
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or extensionPoint: " + extensionPoint);
    }

    /**
     * Add an include to a Use Case.
     *
     * @param usecase The Use Case.
     * @param include The Include.
     */
    public void addInclude(Object usecase, Object include) {
        if (usecase instanceof MUseCase
                && include instanceof MInclude) {
            ((MUseCase) usecase).addInclude((MInclude) include);
            return;
        }

        throw new IllegalArgumentException("usecase: " + usecase
                + " or include: " + include);
    }

    /**
     * Sets the addition to an include.
     * There is a bug in NSUML that reverses additions and bases for includes.
     *
     * @param handle Include
     * @param useCase UseCase
     */
    public void setAddition(Object handle, Object useCase) {
        if ((handle instanceof MBase) && ((MBase) handle).isRemoved()) {
            throw new IllegalArgumentException("Operation on a removed object ["
                    + handle + "]");
        }
        if ((useCase instanceof MBase) && ((MBase) useCase).isRemoved()) {
            throw new IllegalArgumentException("Operation on a removed object ["
                    + useCase + "]");
        }

        if (handle instanceof MInclude && useCase instanceof MUseCase) {
            ((MInclude) handle).setBase((MUseCase) useCase);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * Set the condition of an extend.
     *
     * @param handle is the extend
     * @param booleanExpression is the condition
     */
    public void setCondition(Object handle, Object booleanExpression) {
        if (handle instanceof MExtend
                && booleanExpression instanceof MBooleanExpression) {
            ((MExtend) handle).setCondition(
                    (MBooleanExpression) booleanExpression);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or booleanExpression: " + booleanExpression);
    }

    /**
     * Set the extension of a usecase.
     *
     * @param extend Extend
     * @param useCase UseCase
     */
    public void setExtension(Object extend, Object useCase) {
        if (!(useCase instanceof MUseCase)) {
            throw new IllegalArgumentException("A use case must be supplied");
        }
        
        if ((extend instanceof MBase) && ((MBase) extend).isRemoved()) {
            throw new IllegalStateException("Operation on a removed object ["
                    + extend + "]");
        }
        if ((useCase instanceof MBase) && ((MBase) useCase).isRemoved()) {
            throw new IllegalStateException("Operation on a removed object ["
                    + useCase + "]");
        }

        if (extend instanceof MExtend
                && (useCase instanceof MUseCase)) {
            ((MExtend) extend).setExtension((MUseCase) useCase);
            return;
        }
        throw new IllegalArgumentException("handle: " + extend
                + " or ext: " + useCase);
    }

    /**
     * Sets the extension points of some use cases or extend relationships.
     *
     * @param handle the use case or extend relationship
     * @param extensionPoints is the extension points
     */
    public void setExtensionPoints(
            Object handle,
            Collection extensionPoints) {
        if (handle instanceof MUseCase && extensionPoints instanceof List) {
            ((MUseCase) handle).setExtensionPoints(extensionPoints);
            return;
        }
        if (handle instanceof MExtend && extensionPoints instanceof Collection) {
            ((MExtend) handle).setExtensionPoints(new ArrayList(extensionPoints));
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or extensionPoints: " + extensionPoints.getClass());
    }

    /**
     * Set the collection of Include relationships for a usecase.
     *
     * @param handle UseCase
     * @param includes the collection of Include relationships
     */
    public void setIncludes(Object handle, Collection includes) {
        if (handle instanceof MUseCase) {
            ((MUseCase) handle).setIncludes(includes);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or includes: " + includes);
    }

    /**
     * Sets a location of some extension point.
     *
     * @param handle is the extension point
     * @param loc is the location
     */
    public void setLocation(Object handle, String loc) {
        if (handle instanceof MExtensionPoint) {
            ((MExtensionPoint) handle).setLocation(loc);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * Set a Use Case for an Extension Point.
     *
     * @param elem The Extension Point.
     * @param usecase The Use Case.
     */
    public void setUseCase(Object elem, Object usecase) {
        if (elem instanceof MExtensionPoint
                && (usecase instanceof MUseCase
                        || usecase == null)) {
            ((MExtensionPoint) elem).setUseCase((MUseCase) usecase);
            return;
        }

        throw new IllegalArgumentException("elem: " + elem
                + " or usecase: " + usecase);
    }
}
