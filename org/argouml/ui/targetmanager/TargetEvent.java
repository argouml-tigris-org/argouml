// $Id$
// Copyright (c) 2002 The Regents of the University of California. All
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
package org.argouml.ui.targetmanager;

import java.util.EventObject;

/**
 * A targetevent indicating that the target of argouml is about to change 
 * from the given oldtargets (which still is the the target when this event is
 * fired) to newtargets (which will be the new targets).
 * @author jaap.branderhorst@xs4all.nl
 */
public class TargetEvent extends EventObject {
    
    /**
     * Indicates that a total new set of targets is set
     */
    public final static String TARGET_SET = "set";
    
    /**
     * Indicates that a target is being added to the list of targets
     */
    public final static String TARGET_ADDED = "added";
    
    /**
     * Indicates that a target is being removed from the list of targets
     */
    public final static String TARGET_REMOVED = "removed";
    
    /**
     * The name of the event
     */
    private String _name;
    
    /**
     * The old targets before the change took place
     */
    private Object[] _oldTargets;
    
    /**
     * The new targets after the change took place
     */
    private Object[] _newTargets;

    /**
     * Constructs a new TargetEvent
     * @param source The source that fired the TargetEvent, will allways be the TargetManager
     * @param name The name of the TargetEvent, can be TARGET_SET, TARGET_REMOVED or TARGET_ADDED
     * @param oldTargets The old targets before the change took place
     * @param newTargets The new targets after the change took place
     */
    public TargetEvent(Object source, String name, Object[] oldTargets, Object[] newTargets) {
	super(source);
	_name = name;
        _oldTargets = oldTargets;
        _newTargets = newTargets;
    }
    

    /**
     * Getter for the name
     * @return the name of the event
     */
    public String getName() {
	return _name;
    }

    /**
     * Getter for the old targets
     * @return an object array with the old targets
     */
    public Object[] getOldTargets() {
	return _oldTargets == null ? new Object[] {null} : _oldTargets;
    }

    /**
     * Getter for the new targets
     * @return an object array with the new targets
     */
    public Object[] getNewTargets() {
        return _newTargets == null ? new Object[] {null} : _newTargets;
    }

}
