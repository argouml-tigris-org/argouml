// $Id$
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

package org.argouml.ui;

import java.util.*;
import java.lang.ref.*;

import org.argouml.model.ModelFacade;

/**
*    This class implements a navigation history
*
*    @author Curt Arnold
*    @since 0.9
*/
public class NavigationHistory { 

    private List _history;
    private int _position;
    //
    //  tri-state boolean (-1 unevaluated, 0 false, 1 true)
    //
    int _isForwardEnabled = -1;
    int _isBackEnabled = -1;
    
    /**
     *     Called by a user interface element when a request to 
     *     navigate to a model element has been received.
     */
    public void navigateTo(Object element) {
        if (_history == null) {
            _history = new ArrayList();
            _position = -1;
        }
        _position++;
        int size = _history.size();
        //
        //   if we have navigated back and taken off in 
        //      a new direction, truncate the navigation
        //      history after the previous element 
        if (_position < size) {
            _history.set(_position, new WeakReference(element));
            for (int i = size - 1; i > _position; i--) {
                _history.remove(i);
            }
        }
        else {
            _position = size;
            _history.add(new WeakReference(element));
        }
        _isForwardEnabled = -1;
        _isBackEnabled = -1;
    }
    
    
    /**
     *    Called by a user interface element when a request to 
     *    open a model element in a new window has been recieved.
     */
    public void open(Object element) {
    }


    /**
     *    Called to navigate to previous selection
     *    returns true if navigation performed
     *
     *    @param attempt false if navigation accomplished by earlier listener
     *    @return true if navigation performed
     */
    public Object navigateBack(boolean attempt) {
        Object target = null;
        if (_history == null || _position <= 0) {
            _isBackEnabled = 0;
        }
        else {
            //
            //   get the current entry so that we don't go back to ourselves
            //     (might happen if you delete an intermediate entry)
            WeakReference ref = (WeakReference) _history.get(_position);
            Object current = null;
            if (ref != null) {
                current = ref.get();
            }
            //
            //  starting at previous entry, 
            //      see if any of the references are still valid
            //   
            int index;            
            for (index = _position - 1; index >= 0 && target == null; index--) {
                ref = (WeakReference) _history.get(index);
                if (ref != null) {
                    target = ref.get();
                    //
                    //  weak ref has been reclaimed, remove it from
                    //     the list and continue looking back
                    if (target == null || target == current) {
                        _history.set(index, null);
                        target = null;
                    }
                    else {
                        //
                        //   these check for phantom model elements
                        //       those still alive but not attached to anything
                        if (ModelFacade.isAFeature(target)) {
                            if (ModelFacade.getOwner(target) == null) {
                                target = null;
                            }
                        }
                        else {
                            if (ModelFacade.isAModelElement(target)) {
                                if (ModelFacade.getNamespace(target) == null) {
                                    target = null;
                                }
                            }
                        }
                    }
                }
            }
            //
            //  if there were no tolerable back targets
            //
            if (target == null) {
                _isBackEnabled = 0;
            }
            else {
                if (attempt) {
                    _position = index + 1;
                    _isForwardEnabled = 1;
                    _isBackEnabled = -1;
                }
                else {
                    _isBackEnabled = 1;
                }
            }
        }
        return target;
    }
    
    /**
     *    Called to navigate to next selection
     *    returns true if navigation performed
     *
     *    @param attempt false if navigation accomplished by earlier listener
     *    @return true if navigation performed
     */
    public Object navigateForward(boolean attempt) {
        Object target = null;
        _isForwardEnabled = 0;
        if (_history != null) {
            int size = _history.size();
            if (_position < size - 1) {
                //
                //   get the current entry so that we don't go back to ourselves
                //     (might happen if you delete an intermediate entry)
                WeakReference ref = (WeakReference) _history.get(_position);
                Object current = null;
                if (ref != null) {
                    current = ref.get();
                }
                //
                //  starting at next entry, 
                //      see if any of the references are still valid
                //   
                int index;
                for (index = _position + 1; index < size && target == null; index++) {
                    ref = (WeakReference) _history.get(index);
                    if (ref != null) {
                        target = ref.get();
                        //
                        //  weak ref has been reclaimed, remove it from
                        //     the list and continue looking back
                        if (target == null || target == current) {
                            _history.set(index, null);
                            target = null;
                        }
                        else {
                            //
                            //   these check for phantom model elements
                            //       those still alive but not attached to anything
                            if (ModelFacade.isAFeature(target)) {
                                if (ModelFacade.getOwner(target) == null) {
                                    target = null;
                                }
                            }
                            else {
                                if (ModelFacade.isAModelElement(target)) {
                                    if (ModelFacade.getNamespace(target) == 
					null) 
				    {
                                        target = null;
                                    }
                                }
                            }
                        }
                    }
                }
                if (target == null) {
                    _isForwardEnabled = 0;
                }
                else {
                    if (attempt) {
                        _position = index - 1;
                        _isBackEnabled = 1;
                        _isForwardEnabled = -1;
                    }
                    else {
                        _isForwardEnabled = 1;
                    }
                }
            }
        }
        return target;
    }

    /**  
     *    Returns true if this listener has a target for
     *    a back navigation.  Only one listener needs to
     *    return true for the back button to be enabled.
     */
    public boolean isNavigateBackEnabled() {
        boolean enabled = false;
        if (_isBackEnabled == 1) {
            return true;
        }
        else {
            if (_isBackEnabled != 0) {
                enabled = navigateBack(false) != null;
            }
        }
        return enabled;
    }

    /**  
     *    Returns true if this listener has a target for
     *    a back navigation.  Only one listener needs to
     *    return true for the back button to be enabled.
     */
    public boolean isNavigateForwardEnabled() {
        boolean enabled = false;
        if (_isForwardEnabled == 1) {
            enabled = true;
        }
        else {
            if (_isForwardEnabled != 0) {
                enabled = navigateForward(false) != null;
            }
        }
        return enabled;
    }

    

} /* end class NavigationHistory */
