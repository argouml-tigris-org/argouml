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

package org.argouml.cognitive.ui;

import java.util.*;

import org.argouml.cognitive.*;

import org.argouml.application.api.*;


public class PriorityNode {
    // Private members.
    private static final String BUNDLE = "Cognitive";

    static final String high = Argo.localize(BUNDLE, "level.high");
    static final String medium = Argo.localize(BUNDLE, "level.medium");
    static final String low = Argo.localize(BUNDLE, "level.low");

    ////////////////////////////////////////////////////////////////
    // static variables and methods
    protected static Vector _PRIORITIES = null;

    public static Vector getPriorities() {
	if (_PRIORITIES == null) {
	    _PRIORITIES = new Vector();
	    _PRIORITIES.addElement(new PriorityNode(high, ToDoItem.HIGH_PRIORITY));
	    _PRIORITIES.addElement(new PriorityNode(medium, ToDoItem.MED_PRIORITY));
	    _PRIORITIES.addElement(new PriorityNode(low, ToDoItem.LOW_PRIORITY));
	}
	return _PRIORITIES;
    }


    ////////////////////////////////////////////////////////////////
    // instance variables

    protected String _name;
    protected int _priority;
  
    ////////////////////////////////////////////////////////////////
    // contrsuctors

    public PriorityNode(String name, int pri) {
	_name = name;
	_priority = pri;
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    public String getName() { return _name; }
    public int getPriority() { return _priority; }

    public String toString() { return getName(); }
  
} /* end class PriorityNode */
