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
import java.awt.*;
import java.io.Serializable;
import javax.swing.tree.*;
import javax.swing.event.*;

import org.argouml.ui.*;
import org.argouml.cognitive.*;

public abstract class ToDoPerspective extends TreeModelComposite
implements Serializable {

  protected static Vector _registeredPerspectives = new Vector();
  protected static Vector _rules = new Vector();


  static {
    ToDoPerspective priority = new ToDoByPriority();
    ToDoPerspective decision = new ToDoByDecision();
    ToDoPerspective goal = new ToDoByGoal();
    ToDoPerspective offender = new ToDoByOffender();
    ToDoPerspective poster = new ToDoByPoster();
    ToDoPerspective type = new ToDoByType();
//     ToDoPerspective difficulty = new ToDoByDifficulty();
//     ToDoPerspective skill = new ToDoBySkill();


    registerPerspective(priority);
    registerPerspective(decision);
    registerPerspective(goal);
    registerPerspective(offender);
    registerPerspective(poster);
    registerPerspective(type);
//     registerPerspective(difficulty);
//     registerPerspective(skill);

    registerRule(new GoListToDecisionsToItems());
    registerRule(new GoListToGoalsToItems());
    registerRule(new GoListToPriorityToItem());
    registerRule(new GoListToTypeToItem());
    registerRule(new GoListToOffenderToItem());
    registerRule(new GoListToPosterToItem());

  }

  ////////////////////////////////////////////////////////////////
  // static methods

  public static void registerPerspective(ToDoPerspective np) {
    _registeredPerspectives.addElement(np);
  }

  public static void unregisterPerspective(ToDoPerspective np) {
    _registeredPerspectives.removeElement(np);
  }

  public static Vector getRegisteredPerspectives() {
    return _registeredPerspectives;
  }

  public static void registerRule(TreeModel rule) {
    _rules.addElement(rule);
  }

  public static Vector getRegisteredRules() { return _rules; }

  ////////////////////////////////////////////////////////////////
  // instance variables

  protected ToDoList _root;

  protected EventListenerList _listenerList = new EventListenerList();

  ////////////////////////////////////////////////////////////////
  // constructor
  public ToDoPerspective(String name) { super(name); }

  //
  //  Change Events
  //

  /*
   * Notify all listeners that have registered interest for
   * notification on this event type.  The event instance 
   * is lazily created using the parameters passed into 
   * the fire method.
   * @see EventListenerList
   */
  protected void fireTreeNodesChanged(Object source, Object[] path, 
				      int[] childIndices, 
				      Object[] children) {
    // Guaranteed to return a non-null array
    Object[] listeners = _listenerList.getListenerList();
    TreeModelEvent e = null;
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length-2; i>=0; i-=2) {
      if (listeners[i]==TreeModelListener.class) {
	// Lazily create the event:
	if (e == null)
	  e = new TreeModelEvent(source, path,
				 childIndices, children);
	((TreeModelListener)listeners[i+1]).treeNodesChanged(e);
      }
    }
  }

  /*
   * Notify all listeners that have registered interest for
   * notification on this event type.  The event instance 
   * is lazily created using the parameters passed into 
   * the fire method.
   * @see EventListenerList
   */
  protected void fireTreeNodesInserted(Object source, Object[] path, 
				       int[] childIndices, 
				       Object[] children) {
    // Guaranteed to return a non-null array
    Object[] listeners = _listenerList.getListenerList();
    TreeModelEvent e = null;
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length-2; i>=0; i-=2) {
      if (listeners[i]==TreeModelListener.class) {
	// Lazily create the event:
	if (e == null)
	  e = new TreeModelEvent(source, path, 
				 childIndices, children);
	((TreeModelListener)listeners[i+1]).treeNodesInserted(e);
      }
    }
  }

  /*
   * Notify all listeners that have registered interest for
   * notification on this event type.  The event instance 
   * is lazily created using the parameters passed into 
   * the fire method.
   * @see EventListenerList
   */
  protected void fireTreeNodesRemoved(Object source, Object[] path, 
				      int[] childIndices, 
				      Object[] children) {
    // Guaranteed to return a non-null array
    Object[] listeners = _listenerList.getListenerList();
    TreeModelEvent e = null;
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length-2; i>=0; i-=2) {
      if (listeners[i]==TreeModelListener.class) {
	// Lazily create the event:
	if (e == null)
	  e = new TreeModelEvent(source, path, 
				 childIndices, children);
	((TreeModelListener)listeners[i+1]).treeNodesRemoved(e);
      }
    }
  }

  /*
   * Notify all listeners that have registered interest for
   * notification on this event type.  The event instance 
   * is lazily created using the parameters passed into 
   * the fire method.
   * @see EventListenerList
   */
  protected void fireTreeStructureChanged(Object[] path) {
    // Guaranteed to return a non-null array
    Object[] listeners = _listenerList.getListenerList();
    TreeModelEvent e = null;
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length-2; i>=0; i-=2) {
      if (listeners[i]==TreeModelListener.class) {
	// Lazily create the event: only the path matters
	if (e == null) e = new TreeModelEvent(this, path, null, null);
	((TreeModelListener)listeners[i+1]).treeStructureChanged(e);
      }
    }
  }


  public void addTreeModelListener(TreeModelListener l) {
    _listenerList.add(TreeModelListener.class, l);
  }

  public void removeTreeModelListener(TreeModelListener l) {
    _listenerList.remove(TreeModelListener.class, l);
  }

} /* end class ToDoPerspective */
