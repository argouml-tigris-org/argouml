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

package org.argouml.model.uml.behavioralelements.commonbehavior;

import org.argouml.model.uml.AbstractModelFactory;
import org.argouml.model.uml.UmlFactory;

import ru.novosoft.uml.MFactory;
import ru.novosoft.uml.behavior.common_behavior.MCallAction;
import ru.novosoft.uml.behavior.common_behavior.MComponentInstance;
import ru.novosoft.uml.behavior.common_behavior.MException;
import ru.novosoft.uml.behavior.common_behavior.MLink;
import ru.novosoft.uml.behavior.common_behavior.MLinkEnd;
import ru.novosoft.uml.behavior.common_behavior.MNodeInstance;
import ru.novosoft.uml.behavior.common_behavior.MObject;
import ru.novosoft.uml.behavior.common_behavior.MSignal;
import ru.novosoft.uml.behavior.common_behavior.MStimulus;

/**
 * Factory to create UML classes for the UML
 * BehaviorialElements::CommonBehavior package.
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
 */
public class CommonBehaviorFactory extends AbstractModelFactory {

    /** Singleton instance.
     */
    private static CommonBehaviorFactory SINGLETON =
                   new CommonBehaviorFactory();

    /** Singleton instance access method.
     */
    public static CommonBehaviorFactory getFactory() {
        return SINGLETON;
    }

    /** Don't allow instantiation
     */
    private CommonBehaviorFactory() {
    }

    /** Create an empty but initialized instance of a UML CallAction.
     *  
     *  @return an initialized UML CallAction instance.
     */
    public MCallAction createCallAction() {
        MCallAction modelElement = MFactory.getDefaultFactory().createCallAction();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML ComponentInstance.
     *  
     *  @return an initialized UML ComponentInstance instance.
     */
    public MComponentInstance createComponentInstance() {
        MComponentInstance modelElement = MFactory.getDefaultFactory().createComponentInstance();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Exception.
     *  
     *  @return an initialized UML Exception instance.
     */
    public MException createException() {
        MException modelElement = MFactory.getDefaultFactory().createException();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Link.
     *  
     *  @return an initialized UML Link instance.
     */
    public MLink createLink() {
        MLink modelElement = MFactory.getDefaultFactory().createLink();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML LinkEnd.
     *  
     *  @return an initialized UML LinkEnd instance.
     */
    public MLinkEnd createLinkEnd() {
        MLinkEnd modelElement = MFactory.getDefaultFactory().createLinkEnd();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML NodeInstance.
     *  
     *  @return an initialized UML NodeInstance instance.
     */
    public MNodeInstance createNodeInstance() {
        MNodeInstance modelElement = MFactory.getDefaultFactory().createNodeInstance();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Object.
     *  
     *  @return an initialized UML Object instance.
     */
    public MObject createObject() {
        MObject modelElement = MFactory.getDefaultFactory().createObject();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Signal.
     *  
     *  @return an initialized UML Signal instance.
     */
    public MSignal createSignal() {
        MSignal modelElement = MFactory.getDefaultFactory().createSignal();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Stimulus.
     *  
     *  @return an initialized UML Stimulus instance.
     */
    public MStimulus createStimulus() {
        MStimulus modelElement = MFactory.getDefaultFactory().createStimulus();
	super.initialize(modelElement);
	return modelElement;
    }

}

