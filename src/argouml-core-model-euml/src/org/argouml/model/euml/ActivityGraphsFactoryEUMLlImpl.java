// $Id$
/*****************************************************************************
 * Copyright (c) 2007,2010 Tom Morris and other contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Morris - initial framework
 *****************************************************************************/

package org.argouml.model.euml;

import java.util.Collection;

import org.argouml.model.AbstractModelFactory;
import org.argouml.model.ActivityGraphsFactory;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.UMLFactory;

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

    /**
     * ActivityGraph is a UML1.x element that has been dropped in UML2.
     * For convenience and prevent changes to interfaces this is currently
     * being used to build an Activity for UML2
     * @param theContext the package the activity is to be contained inside
     * @return the Activity created
     * @see org.argouml.model.ActivityGraphsFactory#buildActivityGraph(java.lang.Object)
     */
    public Object buildActivityGraph(final Object theContext) {
        if (!(theContext instanceof org.eclipse.uml2.uml.Package)) {
            throw new IllegalArgumentException(
                    "Didn't expect a " //$NON-NLS-1$
                    + theContext); 
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                Activity activity = (Activity) createActivityGraph();
                activity.setPackage((org.eclipse.uml2.uml.Package) theContext);
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
        throw new NotYetImplementedException();
    }

    public Object buildObjectFlowState(Object compositeState) {
        throw new NotYetImplementedException();
    }

    public Object createActionState() {
        throw new NotYetImplementedException();
    }

    public Object createActivityGraph() {
        return UMLFactory.eINSTANCE.createActivity();
    }

    public Object createCallState() {
        throw new NotYetImplementedException();
    }

    public Object createClassifierInState() {
        throw new NotYetImplementedException();
    }

    public Object createObjectFlowState() {
        throw new NotYetImplementedException();
    }

    public Object createPartition() {
        throw new NotYetImplementedException();
    }

    public Object createSubactivityState() {
        throw new NotYetImplementedException();
    }

}
