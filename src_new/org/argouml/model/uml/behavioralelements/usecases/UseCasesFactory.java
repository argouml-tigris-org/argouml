// $Id$
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

import java.util.Iterator;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.uml.AbstractUmlModelFactory;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.UmlHelper;

import ru.novosoft.uml.MBase;
import ru.novosoft.uml.MFactory;
import ru.novosoft.uml.behavior.use_cases.MActor;
import ru.novosoft.uml.behavior.use_cases.MExtend;
import ru.novosoft.uml.behavior.use_cases.MExtensionPoint;
import ru.novosoft.uml.behavior.use_cases.MInclude;
import ru.novosoft.uml.behavior.use_cases.MUseCase;
import ru.novosoft.uml.behavior.use_cases.MUseCaseInstance;
import ru.novosoft.uml.foundation.core.MNamespace;

/**
 * Factory to create UML classes for the UML
 * BehaviorialElements::UseCases package.
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
 * @stereotype singleton
 */
public class UseCasesFactory extends AbstractUmlModelFactory {

    /** Singleton instance.
     */
    private static UseCasesFactory SINGLETON =
	new UseCasesFactory();

    /** Singleton instance access method.
     */
    public static UseCasesFactory getFactory() {
        return SINGLETON;
    }

    /** Don't allow instantiation
     */
    private UseCasesFactory() {
    }

    /** Create an empty but initialized instance of a Extend
     *  
     *  @return an initialized Extend instance.
     */
    public MExtend createExtend() {
        MExtend modelElement = MFactory.getDefaultFactory().createExtend();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a ExtensionPoint
     *  
     *  @return an initialized ExtensionPoint instance.
     */
    public MExtensionPoint createExtensionPoint() {
        MExtensionPoint modelElement = MFactory.getDefaultFactory().createExtensionPoint();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a Actor
     *  
     *  @return an initialized Actor instance.
     */
    public MActor createActor() {
        MActor modelElement = MFactory.getDefaultFactory().createActor();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a Include
     *  
     *  @return an initialized Include instance.
     */
    public MInclude createInclude() {
        MInclude modelElement = MFactory.getDefaultFactory().createInclude();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UseCase
     *  
     *  @return an initialized UseCase instance.
     */
    public MUseCase createUseCase() {
        MUseCase modelElement = MFactory.getDefaultFactory().createUseCase();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UseCaseInstance
     *  
     *  @return an initialized UseCaseInstance instance.
     */
    public MUseCaseInstance createUseCaseInstance() {
        MUseCaseInstance modelElement = MFactory.getDefaultFactory().createUseCaseInstance();
	super.initialize(modelElement);
	return modelElement;
    }
    
    /**
     * <p>Build an extend relationship.</p>
     *
     * <p>Set the namespace to the base (preferred) or else extension's
     *   namespace. We don't do any checking on base and extension. They should
     *   be different, but that is someone else's problem.</p>
     *
     * @param base       The base use case for the relationship
     *
     * @param extension  The extension use case for the relationship
     *
     * @return           The new extend relationship or <code>null</code> if it
     *                   can't be created.
     */
    public MExtend buildExtend(MUseCase base, MUseCase extension) {

	MExtend extend = UmlFactory.getFactory().getUseCases().createExtend();
	// Set the ends

	extend.setBase(base);
	extend.setExtension(extension);

	// Set the namespace to that of the base as first choice, or that of
	// the extension as second choice.

	if (base.getNamespace() != null) {
	    extend.setNamespace(base.getNamespace());
	}
	else if (extension.getNamespace() != null) {
	    extend.setNamespace(extension.getNamespace());
	}
         
	// build an extensionpoint in the base
	MExtensionPoint point = buildExtensionPoint(base);
	extend.addExtensionPoint(point);

	return extend;
    }
     
    public MExtend buildExtend(MUseCase base, MUseCase extension, MExtensionPoint point) {
        if (base == null || extension == null) 
            throw new IllegalArgumentException("Either the base usecase or the extension usecase is null");
        if (point != null) {
            if (!base.getExtensionPoints().contains(point)) {
                throw new IllegalArgumentException("The extensionpoint is no part of the base usecase");
            }
        } else
            point = buildExtensionPoint(base);
        MExtend extend = createExtend();
        extend.setBase(base);
        extend.setExtension(extension);
        extend.addExtensionPoint(point);
        return extend;
    }
     
     
     
    /**
     * <p>Build an extension point for a use case.</p>
     *
     * <p>Set the namespace to that of the use case if possible.</p>
     *
     * @param modelElement  The owning use case for the extension point. May be
     *                      <code>null</code>.
     *
     * @return         The new extension point or <code>null</code> if it
     *                 can't be created.
     */
    public MExtensionPoint buildExtensionPoint(Object modelElement) {
        if (!(modelElement instanceof MUseCase)) 
            throw new IllegalArgumentException("An extension point can only be built on a use case");

        MUseCase useCase = (MUseCase) modelElement;
        MExtensionPoint extensionPoint = UmlFactory.getFactory().getUseCases().createExtensionPoint();

        // Set the owning use case if there is one given.

        if (useCase != null) {

            extensionPoint.setUseCase(useCase);

            // Set the namespace to that of the useCase if possible.
             
            // the usecase itself is a namespace...
            extensionPoint.setNamespace(useCase);
	    /*
	      if (useCase.getNamespace() != null) {
	      extensionPoint.setNamespace(useCase.getNamespace());
	      }
	    */
        }

        // For consistency with attribute and operation, give it a default
        // name and location

        extensionPoint.setName("newEP");
        extensionPoint.setLocation("loc");

        return extensionPoint;
    }
     
    /**
     * <p>Build an include relationship.</p>
     *
     * <p>Set the namespace to the base (preferred) or else extension's
     *   namespace. We don't do any checking on base and extension. They should
     *   be different, but that is someone else's problem.</p>
     *
     * <p><em>Note</em>. There is a bug in NSUML that gets the base and
     *   addition associations back to front. We reverse the use of their
     *   accessors in the code to correct this.</p>
     *
     * @param base       The base use case for the relationship
     *
     * @param extension  The extension use case for the relationship
     *
     * @return           The new include relationship or <code>null</code> if
     *                   it can't be created.
     */
    public MInclude buildInclude(MUseCase base, MUseCase addition) {

	MInclude include = UmlFactory.getFactory().getUseCases().createInclude();
  
	// Set the ends. Because of the NSUML bug we reverse the accessors
	// here.

	include.setAddition(base);
	include.setBase(addition);

	// Set the namespace to that of the base as first choice, or that of
	// the addition as second choice.

	if (base.getNamespace() != null) {
	    include.setNamespace(base.getNamespace());
	}
	else if (addition.getNamespace() != null) {
	    include.setNamespace(addition.getNamespace());
	}

	return include;
    }
     
    /**
     * Builds an actor in the project's model namespace.
     * @return MActor
     */
    public MActor buildActor() {
	MNamespace ns = ProjectManager.getManager().getCurrentProject().getModel();
	return buildActor(ns);
    }
     
    /**
     * Builds an actor in the given namespace.
     * @param ns
     * @return MActor
     */
    public MActor buildActor(MNamespace ns) {
     	if (ns == null) return buildActor();
     	MActor actor = createActor();
     	actor.setNamespace(ns);
     	actor.setLeaf(false);
     	actor.setRoot(false);
     	actor.setName("newActor");
     	return actor;
    }
     
    /**
     * Builds an actor in the same namespace of the given actor. If object is no
     * actor nothing is build. Did not give MActor as an argument but object to 
     * seperate argouml better from NSUML.
     * @param actor
     * @return MActor
     */
    public MActor buildActor(Object actor) {
        if (actor instanceof MActor) {
            return buildActor(((MActor) actor).getNamespace());
        }
        return null;
    }
     
    public void deleteActor(MActor elem) { }
     
    public void deleteExtend(MExtend elem) {
	UmlHelper.getHelper().deleteCollection(elem.getExtensionPoints());
    }
     
    public void deleteExtensionPoint(MExtensionPoint elem) { }
     
    public void deleteInclude(MInclude elem) { }
     
    public void deleteUseCase(MUseCase elem) {
	UmlHelper.getHelper().deleteCollection(elem.getExtends());
	UmlHelper.getHelper().deleteCollection(elem.getIncludes());       
    }
     
    public void deleteUseCaseInstance(MUseCaseInstance elem) { }




}

