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

import org.argouml.model.uml.AbstractUmlModelFactory;
import org.argouml.model.uml.UmlFactory;

import ru.novosoft.uml.MFactory;
import ru.novosoft.uml.behavior.use_cases.MActor;
import ru.novosoft.uml.behavior.use_cases.MExtend;
import ru.novosoft.uml.behavior.use_cases.MExtensionPoint;
import ru.novosoft.uml.behavior.use_cases.MInclude;
import ru.novosoft.uml.behavior.use_cases.MUseCase;
import ru.novosoft.uml.behavior.use_cases.MUseCaseInstance;

/**
 * Factory to create UML classes for the UML
 * BehaviorialElements::UseCases package.
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
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

}

