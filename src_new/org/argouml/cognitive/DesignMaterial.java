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



// File: DesignMaterial.java
// Classes: DesignMaterial
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.cognitive;

import java.util.*;
import java.beans.*;

import org.tigris.gef.ui.*;
import org.tigris.gef.util.*;

import org.argouml.cognitive.critics.*;

/** Abstract class to represent the materials being used in
 *  design. The nature of these materials is domain-dependent.  For
 *  example, in the domain of software architecture the design
 *  materials are software components and connectors. There is not much
 *  that can be said about generic design materials at this level of
 *  abstraction. But the Argo kernel provides for all DesignMaterials
 *  to (1) be Observable, (2) have Properties (in addition
 *  to instance variables supplied in subclasses) that can be shown in
 *  a property sheet, (3) ask Agency to critique them, (4) keep a
 *  list of the ToDoItem's that are generated from that critiquing,
 *  (5) be notified when the user selects them in an editor, (6)
 *  highlight themselves to draw the designer's attention. <p>
 *
 *  TODO: should I implement virtual copying? or
 *  instance-based inheritiance of properties? How are properties
 *  shared (it at all)?
 *
 *  For examples of subclasing see the package jargo.softarch.  For
 *  instructions on how to define subclasses for DesignMaterials in
 *  your domain, see the Argo cookbook entry for <A
 *  HREF="../cookbook.html#define_design_material">define_design_material</a>
 *  <p>
 *
 * @see Design
 */


public abstract class DesignMaterial extends Observable
    implements Highlightable, java.io.Serializable 
{
    /*
     * @see jargo.softarch.C2BrickDM
     * @see jargo.softarch.C2CompDM
     * @see jargo.softarch.C2ConnDM
     * @see jargo.ui.UiPropertyBrowser
     */

    ////////////////////////////////////////////////////////////////
    // instance variables

    /** Observers of this object that should be saved and loaded with
     *  this object. */
    protected Vector _persistObservers = null;

    protected transient Vector _propertyListeners = null;
  
    /** ToDoList items that are on the designers ToDoList because
     *  of this material. */
    protected ToDoList _pendingItems = new ToDoList();

    /** Other DesignMaterial's that contain this one. */
    protected Vector _parents = new Vector();

    /** "Soft" Properties that this DesignMaterial may have, but we do
     *  not want to allocate an instance variable to them. */
    protected Hashtable _props = new Hashtable();


    protected boolean _highlight;

  
  
    ////////////////////////////////////////////////////////////////
    // constructors

    /** Construct a new DesignMaterial with empty Properties. */
    public DesignMaterial() {  }

    /** Construct a new DesignMaterial with the given Properties. */
    public DesignMaterial(Hashtable ht) {
	// TODO: construct a new Hashtable that "inherits"
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /** Make the given Design be the parent of this DesignMaterial. */
    void addParent(Design d) {
	_parents.removeElement(d); /* behave like a set */
	_parents.addElement(d);
    }

    /** Remove the given parent. For example, this could be called when
     * a DesignMaterial is removed from a Design. */
    void removeParent(Design d) { _parents.removeElement(d); }

    /** Enumerate over all my parents. For now most DesignMaterial's
     * have just one parent, but I think this might be useful in the
     * future. */
    public Enumeration parents() { return _parents.elements(); }

    /** Reply the value of one property, if not found reply
     * defaultValue. Even if subclasses store some properties in instance
     * variables, they should implement this method to give acces to
     * those instance variables by name. */
    public Object getProperty(String key, Object defaultValue) {
	return get(key, defaultValue);
    }

    /** Reply the value of one property, if not found reply null. */
    public Object getProperty(String k) { return get(k); }

    public Object get(String k) { return _props.get(k); }

    public Object get(String key, Object defaultValue) {
	Object res = get(key);
	if (res == null) res = defaultValue;
	return res;
    }

    /** Reply the value an int property, if not found reply 0. */
    public int getIntProperty(String k) {
	return getIntProperty(k, 0);
    }

    /** Reply the value an int property, if not found reply defaultValue. */
    public int getIntProperty(String k, int defaultValue) {
	Object o = getProperty(k);
	if (o instanceof Integer) return ((Integer) o).intValue();
	return defaultValue;
    }


    /** Reply the value an boolean property, if not found reply false. */
    public boolean getBoolProperty(String k) {
	return getBoolProperty(k, false);
    }

    /** Reply the value an int property, if not found reply defaultValue. */
    public boolean getBoolProperty(String k, boolean defaultValue) {
	Object o = getProperty(k);
	if (o instanceof Boolean) return ((Boolean) o).booleanValue();
	return defaultValue;
    }

    /** Set the value of a property. */
    public boolean put(String k, Object v) {
	if (v == null || k == null) return false;
	_props.put(k, v);
	changedProperty(k);
	return true;
    }

    public void put(Hashtable ht) {
	Enumeration keys = ht.keys();
	while (keys.hasMoreElements()) {
	    String k = (String) keys.nextElement();
	    Object v = ht.get(k);
	    put(k, v);
	}
    }

    /** Convenient function for setting the value of boolean
     * properties. */
    public boolean put(String k, boolean v) {
	return put(k, v ? Boolean.TRUE : Boolean.FALSE);
    }

    /** Convenient function for setting the value of boolean
     * properties. */
    public boolean put(String k, int v) { return put(k, new Integer(v)); }

    public void setProperty(String k, Object v) {
	if (getProperty(k) != null) put(k, v);
    }

    public void setProperty(String k, int v) {
	if (getProperty(k) != null) put(k, new Integer(v));
    }

    public void setProperty(String k, boolean v) {
	if (getProperty(k) != null) put(k, new Boolean(v));
    }

    public boolean define(String k, Object v) {
	if (getProperty(k) == null) return put(k, v);
	return false;
    }

    public boolean canPut(String key) { return true; }
    public Enumeration keysIn(String cat) {
	if (cat.equals("Model")) return _props.keys();
	else return EnumerationEmpty.theInstance();
    }

    /** Set the value of a property iff it is not already set. */
    public void define(String k, boolean v) {
	if (getProperty(k) == null) put(k, v);
    }

    /** Set the value of a property iff it is not already set. */
    public void define(String k, int v) {
	if (getProperty(k) == null) put(k, v);
    }

    /** Make the given property undefined. */
    public Object removeProperty(String k) { return _props.remove(k); }

    /** Convenient function for getting the value of boolean
     * properties. */
    public boolean getBoolean(String k) {
	return getProperty(k, "false").equals("true");
    }

    ////////////////////////////////////////////////////////////////
    // critiquing

    /** Critique this DesignMaterial and post ToDoItem's to the
     * Designer's list. By default this is done by asking Agency. */
    public void critique(Designer dsgr) { Agency.applyAllCritics(this, dsgr); }

    /** Remove all the ToDoItem's generated by this DesignMaterial from
     * the ToDoList of the given Designer. For example, this could be
     * called when the DesignMaterial is deleted from the design
     * document. */
    public void removePendingItems(Designer dsgr) {
	dsgr.removeToDoItems(_pendingItems);
	_pendingItems.removeAllElements();
    }

    /** Remove this DesignMaterial from the design document and free up
     * memory and other resources as much as possible. */
    public void dispose() {
	Enumeration pars = parents();
	while (pars.hasMoreElements()) {
	    Design d = (Design) pars.nextElement();
	    d.removeElement(this);
	}
	removePendingItems(Designer.theDesigner());
    }

    ////////////////////////////////////////////////////////////////
    // user interface

    /** When a critic produces a ToDoItem, both the Designer and the
     * "offending" DesignMaterial's are notified. By default the
     * ToDoItem is added to the list of generated ToDoItem's. Subclasses
     * may, for example, visually change their appearance to indicate
     * the presence of an error. One paper called this 'clarifiers'. <p>
     *
     * TODO: do I need a Clarifier object in the framework? */
    public void inform(ToDoItem item) { _pendingItems.addElement(item); }

    /** Draw the Designer's attention to this DesignMaterial in all
     * views. */
    public void setHighlight(boolean h) {
	_highlight = h;
	setChanged();
	notifyObservers("HIGHLIGHT");    
    }
    public boolean getHighlight() { return _highlight; }


    ////////////////////////////////////////////////////////////////
    // notifications and updates

    public void changedProperty(String key) {
	setChanged();
	notifyObservers(key);
    }

    public void addPersistantObserver(Observer o) {
	if (_persistObservers == null) _persistObservers = new Vector();
	_persistObservers.removeElement(o);
	_persistObservers.addElement(o);
    }

    public void removePersistObserver(Observer o) {
	_persistObservers.removeElement(o);
    }

    public void notifyPersistantObservers(Object arg) {
	if (_persistObservers == null) return;
	Enumeration eachObs = _persistObservers.elements();
	while (eachObs.hasMoreElements()) {
	    Observer obs = (Observer) eachObs.nextElement();
	    obs.update(this, arg);
	}
    }

    public void notifyObservers(Object arg) {
	super.notifyObservers(arg);
	notifyPersistantObservers(arg);
    }


    ////////////////////////////////////////////////////////////////
    // property change events
  
    public synchronized 
    void addPropertyChangeListener(PropertyChangeListener listener)
    {
	if (_propertyListeners == null) _propertyListeners = new Vector();
	_propertyListeners.removeElement(listener);
	_propertyListeners.addElement(listener);
    }

    public synchronized 
    void removePropertyChangeListener(PropertyChangeListener listener)
    {
	if (_propertyListeners == null) return;
	_propertyListeners.removeElement(listener);
    }

    public void firePropertyChange(String propertyName, 
				   boolean oldValue, boolean newValue) {
	firePropertyChange(propertyName,
			   new Boolean(oldValue),
			   new Boolean(newValue));
    }

    public void firePropertyChange(String propertyName, 
				   int oldValue, int newValue) {
	firePropertyChange(propertyName,
			   new Integer(oldValue),
			   new Integer(newValue));
    }



    public void firePropertyChange(String propertyName, 
				   Object oldValue, Object newValue) {
	if (_propertyListeners == null) return;
	if (oldValue != null && oldValue.equals(newValue)) return;
	PropertyChangeEvent evt =
	    new PropertyChangeEvent(this, propertyName, oldValue, newValue);
	for (int i = 0; i < _propertyListeners.size(); i++) {
	    PropertyChangeListener target = 
		(PropertyChangeListener) _propertyListeners.elementAt(i);
	    target.propertyChange(evt);
	}    
    }

  
} /* end class DesignMaterial */
