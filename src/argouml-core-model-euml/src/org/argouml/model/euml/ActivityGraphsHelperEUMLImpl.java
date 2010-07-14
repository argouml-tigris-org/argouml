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

import org.argouml.model.ActivityGraphsHelper;

/**
 * The implementation of the ActivityGraphsHelper for EUML.
 */
class ActivityGraphsHelperEUMLImpl implements ActivityGraphsHelper {

    /**
     * The model implementation.
     */
    private EUMLModelImplementation modelImpl;

    /**
     * Constructor.
     * 
     * @param implementation
     *            The ModelImplementation.
     */
    public ActivityGraphsHelperEUMLImpl(EUMLModelImplementation implementation) {
        modelImpl = implementation;
    }

    public void addContent(Object partition, Object modeElement) {
        // TODO Auto-generated method stub

    }

    public void addInState(Object classifierInState, Object state) {
        // TODO Auto-generated method stub

    }

    public void addParameter(Object objectFlowState, Object parameter) {
        // TODO Auto-generated method stub

    }

    public Object findClassifierByName(Object ofs, String s) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object findStateByName(Object c, String s) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isAddingActivityGraphAllowed(Object context) {
        return (context instanceof org.eclipse.uml2.uml.Package);
    }

    public void removeContent(Object partition, Object modeElement) {
        // TODO Auto-generated method stub

    }

    public void removeParameter(Object objectFlowState, Object parameter) {
        // TODO Auto-generated method stub

    }

    public void setContents(Object partition, Collection newContents) {
        // TODO Auto-generated method stub

    }

    public void setInStates(Object classifierInState, Collection newStates) {
        // TODO Auto-generated method stub

    }

    public void setParameters(Object objectFlowState, Collection parameters) {
        // TODO Auto-generated method stub

    }

    public void setSynch(Object objectFlowState, boolean isSynch) {
        // TODO Auto-generated method stub

    }

}
