// Copyright (c) 1996-2002 The Regents of the University of California. All
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

package org.argouml.model.uml.behavioralelements.usecases;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.argouml.model.uml.modelmanagement.ModelManagementHelper;
import org.argouml.ui.ProjectBrowser;

import ru.novosoft.uml.behavior.use_cases.MActor;
import ru.novosoft.uml.behavior.use_cases.MExtend;
import ru.novosoft.uml.behavior.use_cases.MInclude;
import ru.novosoft.uml.behavior.use_cases.MUseCase;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MModelElement;
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
public class UseCasesHelper {

    /** Don't allow instantiation.
     */
    private UseCasesHelper() {
    }
    
     /** Singleton instance.
     */
    private static UseCasesHelper SINGLETON =
                   new UseCasesHelper();

    
    /** Singleton instance access method.
     */
    public static UseCasesHelper getHelper() {
        return SINGLETON;
    }
    
    /** 
     * <p>This method returns all extension points of a given use case.
     *
     * <p>Here for completeness, but actually just a wrapper for the NSUML
     *   function.</p>
     *
     * @param useCase  The use case for which we want the extension points.
     *
     * @return         A collection of the extension points.
     */

    public Collection getExtensionPoints(MUseCase useCase) {

        return useCase.getExtensionPoints();
    }
    
	/**
	 * Returns all usecases in the current project model.
	 * @return Collection
	 */
    public Collection getAllUseCases() {
    	MNamespace model = ProjectBrowser.TheInstance.getProject().getModel();
		return getAllUseCases(model);
	}
	
	/**
	 * Returns all usecases in some namespace ns.
	 * @return Collection
	 */
	public Collection getAllUseCases(MNamespace ns) {
		Iterator it = ns.getOwnedElements().iterator();
		List list = new ArrayList();
		while (it.hasNext()) {
			Object o = it.next();
			if (o instanceof MNamespace) {
				list.addAll(getAllUseCases((MNamespace)o));
			} 
			if (o instanceof MUseCase) {
				list.add(o);
			}
			
		}
		return list;
	}
	
	/**
	 * Returns all actors in the current project model.
	 * @return Collection
	 */
	public Collection getAllActors() {
    	MNamespace model = ProjectBrowser.TheInstance.getProject().getModel();
		return getAllActors(model);
	}
	
	/**
	 * Returns all actors in some namespace ns.
	 * @return Collection
	 */
	public Collection getAllActors(MNamespace ns) {
		Iterator it = ns.getOwnedElements().iterator();
		List list = new ArrayList();
		while (it.hasNext()) {
			Object o = it.next();
			if (o instanceof MNamespace) {
				list.addAll(getAllActors((MNamespace)o));
			} 
			if (o instanceof MActor) {
				list.add(o);
			}
			
		}
		return list;
	}
	
	/**
	 * Returns all usecases this usecase extends
	 * @param clazz
	 * @return Collection
	 */
	public Collection getExtendedUseCases(MUseCase usecase) {
		if (usecase == null) return new ArrayList();
		Iterator it = usecase.getExtends().iterator();
		List list = new ArrayList();
		while (it.hasNext()) {
			MExtend extend = (MExtend)it.next();
			MUseCase base = extend.getBase();
			list.add(base);
		}
		return list;
	}
	
	/**
	 * Returns the extend relation between two usecases base and extension. If there is none
	 * null is returned.
	 * @param base
	 * @param extension
	 * @return MExtend
	 */
	public MExtend getExtends(MUseCase base, MUseCase extension) {
		if (base == null || extension == null) return null;
		Iterator it = extension.getExtends().iterator();
		while (it.hasNext()) {
			MExtend extend = (MExtend)it.next();
			if (extend.getBase() == base) {
				return extend;
			}
		}
		return null;
	}
	
	/**
	 * Returns all usecases this usecase includes
	 * @param clazz
	 * @return Collection
	 */
	public Collection getIncludedUseCases(MUseCase usecase) {
		if (usecase == null) return new ArrayList();
		Iterator it = usecase.getIncludes().iterator();
		List list = new ArrayList();
		while (it.hasNext()) {
			MInclude include = (MInclude)it.next();
			MUseCase addition = include.getBase();
			list.add(addition);
		}
		return list;
	}
	
	/**
	 * Returns the include relation between two usecases base and inclusion. If there is none
	 * null is returned.
	 * @param base
	 * @param extension
	 * @return MExtend
	 */
	public MInclude getIncludes(MUseCase base, MUseCase inclusion) {
		if (base == null || inclusion == null) return null;
		Iterator it = inclusion.getIncludes().iterator();
		while (it.hasNext()) {
			MInclude include = (MInclude)it.next();
			if (include.getBase() == base) {
				return include;
			}
		}
		return null;
	}
	
	/**
	 * Returns the specificationpath operation of some usecase. See 
	 * section 2.11.3.5 of the UML 1.3 spec for a definition
	 * @param uc
	 * @return Collection
	 */
	public Collection getSpecificationPath(MUseCase uc) {
		Set set = new HashSet();
		set.addAll(ModelManagementHelper.getHelper().getAllSurroundingNamespaces(uc));
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
    	
}

