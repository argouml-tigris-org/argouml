// Copyright (c) 1996-99 The Regents of the University of California. All
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


package org.argouml.uml.ui;
import javax.swing.*;
import java.awt.*;
import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;

/**
 *  This class is used to dispatch a NSUML change event (which may occur on a non-UI)
 *  thread) to user interface components.  The class is created in response to a 
 *  NSUML change event being captures by a UMLUserInterfaceContainer and then
 *  is passed as an argument to InvokeLater to be run on the user interface thread.
 */
public class UMLNameEventListener implements MElementListener {
    private Container _container;
    private Class[] _metaClasses;
    
    /**
     *  Creates a UMLChangeDispatch.  eventType is overriden if a call to 
     *  one of the event functions is called.
     *  @param container user interface container to which changes are dispatched.
     *  @param metaClasses an array of classes (possibly null) whose names should
     *      monitored.  For example, passing MClassifier.class will monitor name
     *      changes on classifiers.
     *      
     */
    public UMLNameEventListener(Container container,Class[] metaClasses) {
        _container = container;
        _metaClasses = metaClasses;
    }
    
    /**
     *   configures this instance to dispatch a propertySet event.
     *   @param mee NSUML event
     */
    public void propertySet(MElementEvent mee) {
        boolean dispatchEvent = false;
        String eventName = mee.getName();
        
        if(eventName != null && (eventName.equals("name") || eventName.equals("baseClass"))) {
            Object target = mee.getSource();
            if(target != null) {
                //
                //   if the metaClasses weren't provided or
                //      the source can be assigned to one of the meta classes
                //         then proceed
                boolean isMatch = (_metaClasses == null);
                for(int i = 0; !isMatch && i < _metaClasses.length; i++) {
                    isMatch = _metaClasses[i].isAssignableFrom(target.getClass());
                }
                dispatchEvent = isMatch;
            }
        }
        
        if(dispatchEvent) {
            UMLChangeDispatch dispatch = new UMLChangeDispatch(_container,1);
            dispatch.propertySet(mee);
            SwingUtilities.invokeLater(dispatch);
        }
        
    }
           
    /**
     *   configures this instance to dispatch a listRoleItemSet event.
     *   @param mee NSUML event
     */
    public void listRoleItemSet(MElementEvent mee) {
    }

    /**
     *   configures this instance to dispatch a recovered event.
     *   @param mee NSUML event.
     */
    public void recovered(MElementEvent mee) {
    }
    
    /**  
     *    configures this instance to dispatch a removed event.
     *    @param mee NSUML event.
     */
    public void removed(MElementEvent mee) {
        Object target = mee.getSource();
        if(target != null) {
            //
            //   if the metaClasses weren't provided or
            //      the source can be assigned to one of the meta classes
            //         then proceed
            boolean isMatch = (_metaClasses == null);
            for(int i = 0; !isMatch && i < _metaClasses.length; i++) {
                isMatch = _metaClasses[i].isAssignableFrom(target.getClass());
            }
            if(isMatch) {
                UMLChangeDispatch dispatch = new UMLChangeDispatch(_container,0);
                dispatch.removed(mee);
                SwingUtilities.invokeLater(dispatch);
            }        
        }
    }
	
    /**
     *    configures this instance to dispatch a roleAdded event.
     *    @param mee NSUML event.
     */
    public void roleAdded(MElementEvent mee) {
        String eventName = mee.getName();
        if(eventName != null && eventName.equals("ownedElement")) {
            Object target = mee.getAddedValue();
            if(target != null) {
                //
                //   if the metaClasses weren't provided or
                //      the source can be assigned to one of the meta classes
                //         then proceed
                boolean isMatch = (_metaClasses == null);
                for(int i = 0; !isMatch && i < _metaClasses.length; i++) {
                    isMatch = _metaClasses[i].isAssignableFrom(target.getClass());
                }
                if(isMatch) {
                    UMLChangeDispatch dispatch = new UMLChangeDispatch(_container,0);
                    dispatch.removed(mee);
                    SwingUtilities.invokeLater(dispatch);
                }        
            }
        }                
    }
	
    /**
     *    configures this instance to dispatch a roleRemoved event.
     *    @param mee NSUML event
     */
    public void roleRemoved(MElementEvent mee) {
    }
    
    
}

