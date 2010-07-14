// $Id$
/*******************************************************************************
 * Copyright (c) 2007,2010 Tom Morris and other contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Morris - initial framework
 *******************************************************************************/

package org.argouml.model.euml;

import java.util.Collection;

import org.argouml.model.AbstractModelFactory;
import org.argouml.model.ActivityGraphsFactory;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Include;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UseCase;

/**
 * The implementation of the ActivityGraphsFactory for EUML2.
 */
class ActivityGraphsFactoryEUMLlImpl implements ActivityGraphsFactory,
        AbstractModelFactory {

    /**
     * The model implementation.
     */
    private EUMLModelImplementation modelImpl;

    /**
     * Constructor.
     *
     * @param implementation The ModelImplementation.
     */
    public ActivityGraphsFactoryEUMLlImpl(
            EUMLModelImplementation implementation) {
        modelImpl = implementation;
    }

    public Object buildActivityGraph(final Object theContext) {
        if (!(theContext instanceof org.eclipse.uml2.uml.Package)) {
            throw new IllegalArgumentException("Didn't expect a " + theContext);
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                Activity activity = UMLFactory.eINSTANCE.createActivity();
                activity.setPackage((org.eclipse.uml2.uml.Package)theContext);
                getParams().add(activity);
            }
        };
        ChangeCommand cmd = new ChangeCommand(
                modelImpl, run,
                "Create the activity # in the package #");
        modelImpl.getEditingDomain().getCommandStack().execute(cmd);
        cmd.setObjects(run.getParams().get(0), theContext);

        return (Activity) run.getParams().get(0);
    }

    public Object buildClassifierInState(Object classifier, Collection state) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();
    }

    public Object buildObjectFlowState(Object compositeState) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();
    }

    public Object createActionState() {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();
    }

    public Object createActivityGraph() {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();
    }

    public Object createCallState() {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();
    }

    public Object createClassifierInState() {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();
    }

    public Object createObjectFlowState() {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();
    }

    public Object createPartition() {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();
    }

    public Object createSubactivityState() {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();
    }

}
