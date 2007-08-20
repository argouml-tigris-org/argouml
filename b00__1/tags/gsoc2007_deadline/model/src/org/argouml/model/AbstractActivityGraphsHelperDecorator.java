// $Id:AbstractActivityGraphsHelperDecorator.java 13374 2007-08-15 22:03:43Z bobtarling $
// Copyright (c) 1996-2007 The Regents of the University of California. All
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
 * The abstract Decorator for the {@link ActivityGraphsHelper}.
 *
 * @author Bob Tarling
 */
public abstract class AbstractActivityGraphsHelperDecorator
	implements ActivityGraphsHelper {

    /**
     * The component.
     */
    private ActivityGraphsHelper impl;

    /**
     * @param component The component to decorate.
     */
    protected AbstractActivityGraphsHelperDecorator(ActivityGraphsHelper component) {
        impl = component;
    }

    /**
     * The component we are decorating.
     *
     * @return Returns the component.
     */
    protected ActivityGraphsHelper getComponent() {
        return impl;
    }

    /*
     * @see org.argouml.model.ActivityGraphsHelper#findClassifierByName(java.lang.Object, java.lang.String)
     *
     * all methods below similarly implement methods from ActivityGraphsHelper 
     */
    public Object findClassifierByName(Object ofs, String s) {
        return impl.findClassifierByName(ofs, s);
    }

    public Object findStateByName(Object c, String s) {
        return impl.findStateByName(c, s);
    }

    public boolean isAddingActivityGraphAllowed(Object context) {
        return impl.isAddingActivityGraphAllowed(context);
    }

    public void addInState(Object classifierInState, Object state) {
        impl.addInState(classifierInState, state);
    }

    public void setInStates(Object classifierInState, Collection newStates) {
        impl.setInStates(classifierInState, newStates);
    }

    public void setContents(Object partition, Collection newContents) {
        impl.setContents(partition, newContents);
    }
    
    public void addContent(Object partition, Object modelElement) {
        impl.addContent(partition, modelElement);
    }
    
    public void removeContent(Object partition, Object modelElement) {
        impl.removeContent(partition, modelElement);
    }
    
    public void setSynch(Object objectFlowState, boolean isSynch) {
        impl.setSynch(objectFlowState, isSynch);
    }

    public void addParameter(Object objectFlowState, Object parameter) {
        impl.addParameter(objectFlowState, parameter);
    }

    public void removeParameter(Object objectFlowState, Object parameter) {
        impl.removeParameter(objectFlowState, parameter);
    }

    public void setParameters(Object objectFlowState, Collection parameters) {
        impl.setParameters(objectFlowState, parameters);
    }
}
