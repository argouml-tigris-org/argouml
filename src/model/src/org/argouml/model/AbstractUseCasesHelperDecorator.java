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
 * An abstract Decorator for the {@link UseCasesHelper}.
 *
 * @author Bob Tarling
 */
public abstract class AbstractUseCasesHelperDecorator
	implements UseCasesHelper {

    /**
     * The component.
     */
    private UseCasesHelper impl;

    /**
     * @param component The component to decorate.
     */
    AbstractUseCasesHelperDecorator(UseCasesHelper component) {
        impl = component;
    }

    /**
     * The component we are decorating.
     *
     * @return Returns the component.
     */
    protected UseCasesHelper getComponent() {
        return impl;
    }

    public Collection getExtensionPoints(Object useCase) {
        return Model.getFacade().getExtensionPoints(useCase);
    }

    /*
     * @see org.argouml.model.UseCasesHelper#getAllUseCases(java.lang.Object)
     */
    public Collection getAllUseCases(Object ns) {
        return impl.getAllUseCases(ns);
    }

    public Collection getAllActors(Object ns) {
        return impl.getAllActors(ns);
    }

    public Collection getExtendedUseCases(Object ausecase) {
        return impl.getExtendedUseCases(ausecase);
    }

    @SuppressWarnings("deprecation")
    public Collection getExtendingUseCases(Object usecase) {
        return impl.getExtendingUseCases(usecase);
    }

    public Object getExtends(Object abase, Object anextension) {
        return impl.getExtends(abase, anextension);
    }

    public Collection getIncludedUseCases(Object ausecase) {
        return impl.getIncludedUseCases(ausecase);
    }

    public Object getIncludes(Object abase, Object aninclusion) {
        return impl.getIncludes(abase, aninclusion);
    }

    public Collection getSpecificationPath(Object ausecase) {
        return impl.getSpecificationPath(ausecase);
    }

    public void setBase(Object extend, Object base) {
        impl.setBase(extend, base);
    }

    public void removeExtend(Object elem, Object extend) {
        impl.removeExtend(elem, extend);
    }

    public void removeExtensionPoint(Object elem, Object ep) {
        impl.removeExtensionPoint(elem, ep);
    }

    public void removeInclude(Object usecase, Object include) {
        impl.removeInclude(usecase, include);
    }

    public void addExtend(Object elem, Object extend) {
        impl.addExtend(elem, extend);
    }

    public void addExtensionPoint(Object handle, Object extensionPoint) {
        impl.addExtensionPoint(handle, extensionPoint);
    }

    public void addExtensionPoint(Object handle, int position, 
            Object extensionPoint) {
        impl.addExtensionPoint(handle, position, extensionPoint);
    }

    public void addInclude(Object usecase, Object include) {
        impl.addInclude(usecase, include);
    }

    public void setAddition(Object handle, Object useCase) {
        impl.setAddition(handle, useCase);
    }

    public void setCondition(Object handle, Object booleanExpression) {
        impl.setCondition(handle, booleanExpression);
    }

    public void setExtension(Object handle, Object ext) {
        impl.setExtension(handle, ext);
    }

    public void setExtensionPoints(Object handle, Collection extensionPoints) {
        impl.setExtensionPoints(handle, extensionPoints);
    }

    public void setIncludes(Object handle, Collection includes) {
        impl.setIncludes(handle, includes);
    }

    public void setLocation(Object handle, String loc) {
        impl.setLocation(handle, loc);
    }

    public void setUseCase(Object elem, Object usecase) {
        impl.setUseCase(elem, usecase);
    }

}
