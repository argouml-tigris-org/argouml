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

import ru.novosoft.uml.behavior.use_cases.MActor;
import ru.novosoft.uml.behavior.use_cases.MExtend;
import ru.novosoft.uml.behavior.use_cases.MExtensionPoint;
import ru.novosoft.uml.behavior.use_cases.MInclude;
import ru.novosoft.uml.behavior.use_cases.MUseCase;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MNamespace;
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
     * @param extend the given extend
     * @param base the base usecase
     */
    public void setBase(Object extend, Object base) {
        if (extend == null || !(extend instanceof MExtend)) {
            throw new IllegalArgumentException("extend");
        }
        if (base == null || !(base instanceof MUseCase)) {
            throw new IllegalArgumentException("base");
        }

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
    }
}
