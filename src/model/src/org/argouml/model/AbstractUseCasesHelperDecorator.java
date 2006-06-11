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

    /**
     * @see org.argouml.model.UseCasesHelper#getExtensionPoints(
     *         java.lang.Object)
     */
    public Collection getExtensionPoints(Object useCase) {
        return impl.getExtensionPoints(useCase);
    }

    /**
     * @see org.argouml.model.UseCasesHelper#getAllUseCases(java.lang.Object)
     */
    public Collection getAllUseCases(Object ns) {
        return impl.getAllUseCases(ns);
    }

    /**
     * @see org.argouml.model.UseCasesHelper#getAllActors(java.lang.Object)
     */
    public Collection getAllActors(Object ns) {
        return impl.getAllActors(ns);
    }

    /**
     * @see org.argouml.model.UseCasesHelper#getExtendedUseCases(
     *         java.lang.Object)
     */
    public Collection getExtendedUseCases(Object ausecase) {
        return impl.getExtendedUseCases(ausecase);
    }

    /**
     * @see org.argouml.model.UseCasesHelper#getExtendingUseCases(
     *         java.lang.Object)
     */
    public Collection getExtendingUseCases(Object usecase) {
        return impl.getExtendingUseCases(usecase);
    }

    /**
     * @see org.argouml.model.UseCasesHelper#getExtends(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public Object getExtends(Object abase, Object anextension) {
        return impl.getExtends(abase, anextension);
    }

    /**
     * @see org.argouml.model.UseCasesHelper#getIncludedUseCases(
     *         java.lang.Object)
     */
    public Collection getIncludedUseCases(Object ausecase) {
        return impl.getIncludedUseCases(ausecase);
    }

    /**
     * @see org.argouml.model.UseCasesHelper#getIncludes(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public Object getIncludes(Object abase, Object aninclusion) {
        return impl.getIncludes(abase, aninclusion);
    }

    /**
     * @see org.argouml.model.UseCasesHelper#getSpecificationPath(
     *         java.lang.Object)
     */
    public Collection getSpecificationPath(Object ausecase) {
        return impl.getSpecificationPath(ausecase);
    }

    /**
     * @see org.argouml.model.UseCasesHelper#setBase(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void setBase(Object extend, Object base) {
        impl.setBase(extend, base);
    }

    /**
     * @see org.argouml.model.UseCasesHelper#removeExtend(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void removeExtend(Object elem, Object extend) {
        impl.removeExtend(elem, extend);
    }

    /**
     * @see org.argouml.model.UseCasesHelper#removeExtensionPoint(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void removeExtensionPoint(Object elem, Object ep) {
        impl.removeExtensionPoint(elem, ep);
    }

    /**
     * @see org.argouml.model.UseCasesHelper#removeInclude(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void removeInclude(Object usecase, Object include) {
        impl.removeInclude(usecase, include);
    }

    /**
     * @see org.argouml.model.UseCasesHelper#addExtend(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void addExtend(Object elem, Object extend) {
        impl.addExtend(elem, extend);
    }

    /**
     * @see org.argouml.model.UseCasesHelper#addExtensionPoint(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void addExtensionPoint(Object handle, Object extensionPoint) {
        impl.addExtensionPoint(handle, extensionPoint);
    }

    /**
     * @see org.argouml.model.UseCasesHelper#addInclude(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void addInclude(Object usecase, Object include) {
        impl.addInclude(usecase, include);
    }

    /**
     * @see org.argouml.model.UseCasesHelper#setAddition(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void setAddition(Object handle, Object useCase) {
        impl.setAddition(handle, useCase);
    }

    /**
     * @see org.argouml.model.UseCasesHelper#setCondition(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void setCondition(Object handle, Object booleanExpression) {
        impl.setCondition(handle, booleanExpression);
    }

    /**
     * @see org.argouml.model.UseCasesHelper#setExtension(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void setExtension(Object handle, Object ext) {
        impl.setExtension(handle, ext);
    }

    /**
     * @see org.argouml.model.UseCasesHelper#setExtensionPoints(
     *         java.lang.Object,
     *         java.util.Collection)
     */
    public void setExtensionPoints(Object handle, Collection extensionPoints) {
        impl.setExtensionPoints(handle, extensionPoints);
    }

    /**
     * @see org.argouml.model.UseCasesHelper#setIncludes(
     *         java.lang.Object,
     *         java.util.Collection)
     */
    public void setIncludes(Object handle, Collection includes) {
        impl.setIncludes(handle, includes);
    }

    /**
     * @see org.argouml.model.UseCasesHelper#setLocation(
     *         java.lang.Object,
     *         java.lang.String)
     */
    public void setLocation(Object handle, String loc) {
        impl.setLocation(handle, loc);
    }

    /**
     * @see org.argouml.model.UseCasesHelper#setUseCase(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void setUseCase(Object elem, Object usecase) {
        impl.setUseCase(elem, usecase);
    }

}
