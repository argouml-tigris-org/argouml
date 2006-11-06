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

import org.argouml.model.UseCasesFactory;
import org.omg.uml.behavioralelements.usecases.Actor;
import org.omg.uml.behavioralelements.usecases.Extend;
import org.omg.uml.behavioralelements.usecases.ExtensionPoint;
import org.omg.uml.behavioralelements.usecases.Include;
import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.behavioralelements.usecases.UseCaseInstance;
import org.omg.uml.foundation.core.Namespace;

/**
 * Factory to create UML classes for the UML BehaviorialElements::UseCases
 * package.
 * 
 * TODO: Change visibility to package after reflection problem solved.
 * <p>
 * @since ARGO0.19.3
 * @author Thierry Lach & Bob Tarling
 * @author Ludovic Ma&icirc;tre
 * @author Tom Morris
 */
public class UseCasesFactoryMDRImpl extends AbstractUmlModelFactoryMDR
        implements UseCasesFactory {

    /**
     * The model implementation.
     */
    private MDRModelImplementation nsmodel;

    /**
     * Don't allow instantiation.
     * 
     * @param implementation
     *            To get other helpers and factories.
     */
    UseCasesFactoryMDRImpl(MDRModelImplementation implementation) {
        nsmodel = implementation;
    }

    /*
     * @see org.argouml.model.UseCasesFactory#createExtend()
     */
    public Object createExtend() {
        Extend myExtend = nsmodel.getUmlPackage().getUseCases().getExtend().
            createExtend();
        super.initialize(myExtend);
        return myExtend;
    }

    /*
     * @see org.argouml.model.UseCasesFactory#createExtensionPoint()
     */
    public Object createExtensionPoint() {
        ExtensionPoint myExtensionPoint = nsmodel.getUmlPackage().
            getUseCases().getExtensionPoint().createExtensionPoint();
        super.initialize(myExtensionPoint);
        return myExtensionPoint;
    }

    /*
     * @see org.argouml.model.UseCasesFactory#createActor()
     */
    public Object createActor() {
        Actor myActor = nsmodel.getUmlPackage().getUseCases().getActor().
            createActor();
        super.initialize(myActor);
        return myActor;
    }

    /*
     * @see org.argouml.model.UseCasesFactory#createInclude()
     */
    public Object createInclude() {
        Include myInclude = nsmodel.getUmlPackage().getUseCases().getInclude().
            createInclude();
        super.initialize(myInclude);
        return myInclude;
    }

    /*
     * @see org.argouml.model.UseCasesFactory#createUseCase()
     */
    public Object createUseCase() {
        UseCase myUseCase = nsmodel.getUmlPackage().getUseCases().getUseCase().
            createUseCase();
        super.initialize(myUseCase);
        return myUseCase;

    }

    /*
     * @see org.argouml.model.UseCasesFactory#createUseCaseInstance()
     */
    public Object createUseCaseInstance() {
        UseCaseInstance myUseCaseInstance = nsmodel.getUmlPackage().
            getUseCases().getUseCaseInstance().createUseCaseInstance();
        super.initialize(myUseCaseInstance);
        return myUseCaseInstance;
    }

    /*
     * @see org.argouml.model.UseCasesFactory#buildExtend(java.lang.Object, java.lang.Object)
     */
    public Object buildExtend(Object abase, Object anextension) {
        return buildExtend(abase, anextension, null);
    }

    /*
     * @see org.argouml.model.UseCasesFactory#buildExtend(java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public Object buildExtend(Object abase, Object anextension, Object apoint) {
        UseCase base = (UseCase) abase;
        UseCase extension = (UseCase) anextension;
        ExtensionPoint point = (ExtensionPoint) apoint;
        if (base == null || extension == null) {
            throw new IllegalArgumentException("Either the base usecase or "
                    + "the extension usecase is " + "null");
        }
        if (base.equals(extension)) {
            throw new IllegalArgumentException("The base usecase and "
                    + "the extension usecase must be different");
        }
        if (point != null) {
            if (!base.getExtensionPoint().contains(point)) {
                throw new IllegalArgumentException("The extensionpoint is not "
                        + "part of the base " + "usecase");
            }
        } else {
            point = (ExtensionPoint) buildExtensionPoint(base);
        }
        Extend extend = (Extend) createExtend();
        extend.setBase(base);
        extend.setExtension(extension);
        extend.getExtensionPoint().add(point);
        return extend;
    }

    /*
     * @see org.argouml.model.UseCasesFactory#buildExtensionPoint(java.lang.Object)
     */
    public Object buildExtensionPoint(Object modelElement) {
        if (!(modelElement instanceof UseCase)) {
            throw new IllegalArgumentException("An extension point can only "
                    + "be built on a use case");
        }

        UseCase useCase = (UseCase) modelElement;
        ExtensionPoint extensionPoint = 
            (ExtensionPoint) createExtensionPoint();

        extensionPoint.setUseCase(useCase);

        // For consistency with attribute and operation, give it a default
        // name and location
        extensionPoint.setName("newEP");
        extensionPoint.setLocation("loc");
        return extensionPoint;
    }

    /*
     * @see org.argouml.model.UseCasesFactory#buildInclude(java.lang.Object, java.lang.Object)
     */
    public Object buildInclude(Object/* MUseCase */abase,
            Object/* MUseCase */anaddition) {
        UseCase base = (UseCase) abase;
        UseCase addition = (UseCase) anaddition;
        Include include = (Include) createInclude();

        include.setAddition(addition);
        include.setBase(base);

        // Set the namespace to that of the base as first choice, or that of
        // the addition as second choice.

        if (base.getNamespace() != null) {
            include.setNamespace(base.getNamespace());
        } else if (addition.getNamespace() != null) {
            include.setNamespace(addition.getNamespace());
        }
        return include;
    }

    /**
     * Builds an actor in the given namespace.
     * 
     * @param ns
     *            the given namespace
     * @param model
     *            model to use for namespace if namespace is null
     * @return The newly build Actor.
     */
    private Actor buildActor(Namespace ns, Object model) {
        if (ns == null) {
            ns = (Namespace) model;
        }
        Actor actor = (Actor) createActor();
        actor.setNamespace(ns);
        actor.setLeaf(false);
        actor.setRoot(false);
        return actor;
    }


    /*
     * @see org.argouml.model.UseCasesFactory#buildActor(java.lang.Object, java.lang.Object)
     */
    public Object buildActor(Object actor, Object model) {
        if (actor instanceof Actor) {
            return buildActor(((Actor) actor).getNamespace(), model);
        }
        throw new IllegalArgumentException();
    }

    /**
     * @param elem
     *            the UML element to be deleted
     */
    void deleteActor(Object elem) {
        if (!(elem instanceof Actor)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem
     *            the UML element to be deleted
     */
    void deleteExtend(Object elem) {
        if (!(elem instanceof Extend)) {
            throw new IllegalArgumentException();
        }

        nsmodel.getUmlHelper().deleteCollection(
                ((Extend) elem).getExtensionPoint());
    }

    /**
     * @param elem
     *            the UML element to be deleted
     */
    void deleteExtensionPoint(Object elem) {
        if (!(elem instanceof ExtensionPoint)) {
            throw new IllegalArgumentException();
        }
        // Delete Extends which have this as their only ExtensionPoint
        Collection xtends = nsmodel.getUmlPackage().getUseCases()
                .getAExtensionPointExtend().getExtend((ExtensionPoint) elem);
        for (Iterator it = xtends.iterator(); it.hasNext(); ) {
            Extend extend = (Extend) it.next();
            Collection eps = extend.getExtensionPoint();
            if (eps.size() == 1 && eps.contains(elem)) {
                nsmodel.getUmlFactory().delete(extend);
            }
        }
    }

    /**
     * @param elem
     *            the UML element to be deleted
     */
    void deleteInclude(Object elem) {
        if (!(elem instanceof Include)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem
     *            the UML element to be deleted
     */
    void deleteUseCase(Object elem) {
        if (!(elem instanceof UseCase)) {
            throw new IllegalArgumentException();
        }

        UseCase useCase = ((UseCase) elem);
        nsmodel.getUmlHelper().deleteCollection(useCase.getExtend());
        nsmodel.getUmlHelper().deleteCollection(useCase.getInclude());
        // delete Extends where this is the base
        nsmodel.getUmlHelper().deleteCollection(
                nsmodel.getUmlPackage().getUseCases().getABaseExtender()
                        .getExtender(useCase));
        // delete Includes where this is the addition
        nsmodel.getUmlHelper().deleteCollection(
                nsmodel.getUmlPackage().getUseCases().getAIncluderAddition()
                        .getIncluder(useCase));
    }

    /**
     * @param elem
     *            the UML element to be deleted
     */
    void deleteUseCaseInstance(Object elem) {
        if (!(elem instanceof UseCaseInstance)) {
            throw new IllegalArgumentException();
        }
    }
}
