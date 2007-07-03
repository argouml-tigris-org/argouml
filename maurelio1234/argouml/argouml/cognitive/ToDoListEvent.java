// $Id: ToDoListEvent.java 12950 2007-07-01 08:10:04Z tfmorris $
// Copyright (c) 1996-2007 The Regents of the University of California. All
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


package org.argouml.cognitive;

import java.util.List;
import java.util.Vector;

/**
 * Event issued when the todo list changes.
 *
 */
public class ToDoListEvent {

    private List<ToDoItem> items;

    /**
     * The constructor.
     *
     */
    public ToDoListEvent() {
        items = null;
    }
    
    /**
     * The constructor.
     *
     * @param i the Vector of ToDoItems that were changed/added/removed 
     */
    public ToDoListEvent(Vector<ToDoItem> i) {
        items = i;
    }

    /**
     * The constructor.
     *
     * @param i the Vector of ToDoItems that were changed/added/removed 
     */
    public ToDoListEvent(List<ToDoItem> i) {
        items = i;
    }
    
    /**
     * @return the todo list events
     * @deprecated for 0.25.4 by tfmorris. Use {@link #getToDoItemList()}.
     */
    @Deprecated
    public Vector<ToDoItem> getToDoItems() {
        return new Vector<ToDoItem>(items);
    }

    /**
     * @return the todo list events
     */
    public List<ToDoItem> getToDoItemList() {
        return items;
    }
    
}
