// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import org.tigris.gef.ui.Highlightable;
import org.tigris.gef.util.EnumerationEmpty;

import org.argouml.cognitive.critics.Agency;

/**
 * Abstract class to represent the materials being used in
 * design. The nature of these materials is domain-dependent.  For
 * example, in the domain of software architecture the design
 * materials are software components and connectors. There is not much
 * that can be said about generic design materials at this level of
 * abstraction. But the Argo kernel provides for all DesignMaterials
 * to (1) be Observable, (2) have Properties (in addition
 * to instance variables supplied in subclasses) that can be shown in
 * a property sheet, (3) ask Agency to critique them, (4) keep a
 * list of the ToDoItem's that are generated from that critiquing,
 * (5) be notified when the user selects them in an editor, (6)
 * highlight themselves to draw the designer's attention. <p>
 *
 * TODO: should I implement virtual copying? or
 * instance-based inheritiance of properties? How are properties
 * shared (it at all)?
 *
 * For examples of subclasing see the package jargo.softarch.  For
 * instructions on how to define subclasses for DesignMaterials in
 * your domain, see the Argo cookbook entry for <a
 * HREF="../cookbook.html#define_design_material">define_design_material</a>
 * <p>
 *
 * @see Design
 * @author Jason Robbins
 */
public abstract class DesignMaterial extends Observable
        implements Highlightable, Serializable {
    /*
     * @see jargo.softarch.C2BrickDM
     * @see jargo.softarch.C2CompDM
     * @see jargo.softarch.C2ConnDM
     * @see jargo.ui.UiPropertyBrowser
     */

    ////////////////////////////////////////////////////////////////
    // instance variables

    /**
     * Observers of this object that should be saved and loaded with
     *  this object.
     */
    private Vector persistObservers = null;

    private transient Vector propertyListeners = null;

    /**
     * ToDoList items that are on the designers ToDoList because
     *  of this material.
     */
    private ToDoList pendingItems = new ToDoList();

    /**
     * Other DesignMaterial's that contain this one.
     */
    private Vector parents = new Vector();

    /**
     * "Soft" Properties that this DesignMaterial may have, but we do
     *  not want to allocate an instance variable to them.
     */
    private Hashtable props = new Hashtable();


    private boolean highlight;



    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * Construct a new DesignMaterial with empty Properties.
     */
    public DesignMaterial() {  }

    /**
     * Construct a new DesignMaterial with the given Properties.
     *
     * @param ht the hashtable
     */
    public DesignMaterial(Hashtable ht) {
	// TODO: construct a new Hashtable that "inherits"
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * Make the given Design be the parent of this DesignMaterial.
     */
    void addParent(Design d) {
	parents.removeElement(d); /* behave like a set */
	parents.addElement(d);
    }

    /**
     * Remove the given parent. For example, this could be called when
     * a DesignMaterial is removed from a Design.
     */
    void removeParent(Design d) { parents.removeElement(d); }

    /**
     * Enumerate over all my parents. For now most DesignMaterial's
     * have just one parent, but I think this might be useful in the
     * future.
     *
     * @return all the parents
     */
    public Enumeration parents() { return parents.elements(); }

    /**
     * Reply the value of one property, if not found reply
     * defaultValue. Even if subclasses store some properties in instance
     * variables, they should implement this method to give acces to
     * those instance variables by name.
     *
     * @param key the key of the property
     * @param defaultValue the default value if not found
     * @return the value of the property
     */
    public Object getProperty(String key, Object defaultValue) {
	return get(key, defaultValue);
    }

    /**
     * Reply the value of one property, if not found reply null.
     *
     * @param k the key of the property
     * @return the value of the property
     */
    public Object getProperty(String k) { return get(k); }

    /**
     * @param k the key of the property
     * @return the value of the property
     */
    public Object get(String k) { return props.get(k); }

    /**
     * @param key the key of the property
     * @param defaultValue the default value if not found
     * @return the value of the property
     */
    public Object get(String key, Object defaultValue) {
	Object res = get(key);
	if (res == null) {
	    res = defaultValue;
	}
	return res;
    }

    /**
     * Reply the value an int property, if not found reply 0.
     *
     * @param k the key of the property
     * @return the value of the property
     */
    public int getIntProperty(String k) {
	return getIntProperty(k, 0);
    }

    /**
     * Reply the value an int property, if not found reply defaultValue.
     *
     * @param k the key of the property
     * @param defaultValue the default value of the property
     * @return the value of the property
     */
    public int getIntProperty(String k, int defaultValue) {
	Object o = getProperty(k);
	if (o instanceof Integer) {
	    return ((Integer) o).intValue();
	}
	return defaultValue;
    }


    /**
     * Reply the value an boolean property, if not found reply false.
     *
     * @param k the key of the property
     * @return the value of the property
     */
    public boolean getBoolProperty(String k) {
	return getBoolProperty(k, false);
    }

    /**
     * Reply the value an int property, if not found reply defaultValue.
     *
     * @param k the key of the property
     * @param defaultValue the default value of the property
     * @return the value of the property
     */
    public boolean getBoolProperty(String k, boolean defaultValue) {
	Object o = getProperty(k);
	if (o instanceof Boolean) {
	    return ((Boolean) o).booleanValue();
	}
	return defaultValue;
    }

    /**
     * Set the value of a property.
     *
     * @param k the key of the property
     * @param v the value of the property
     * @return always true, because when the key is not yet present, we add it
     */
    public boolean put(String k, Object v) {
	if (v == null || k == null) {
	    return false;
	}
	props.put(k, v);
	changedProperty(k);
	return true;
    }

    /**
     * @param ht the hashtable
     */
    public void put(Hashtable ht) {
	Enumeration keys = ht.keys();
	while (keys.hasMoreElements()) {
	    String k = (String) keys.nextElement();
	    Object v = ht.get(k);
	    put(k, v);
	}
    }

    /**
     * Convenient function for setting the value of boolean
     * properties.
     *
     * @param k the key of the property
     * @param v the value of the property
     * @return always true, because when the key is not yet present, we add it
     */
    public boolean put(String k, boolean v) {
	return put(k, v ? Boolean.TRUE : Boolean.FALSE);
    }

    /**
     * Convenient function for setting the value of boolean
     * properties.
     *
     * @param k the key of the property
     * @param v the value of the property
     * @return always true, because when the key is not yet present, we add it
     */
    public boolean put(String k, int v) { return put(k, new Integer(v)); }

    /**
     * @param k the key of the property
     * @param v the value of the property
     */
    public void setProperty(String k, Object v) {
	if (getProperty(k) != null) {
	    put(k, v);
	}
    }

    /**
     * @param k the key of the property
     * @param v the value of the property
     */
    public void setProperty(String k, int v) {
	if (getProperty(k) != null) {
	    put(k, new Integer(v));
	}
    }

    /**
     * @param k the key of the property
     * @param v the value of the property
     */
    public void setProperty(String k, boolean v) {
	if (getProperty(k) != null) {
	    put(k, new Boolean(v));
	}
    }

    /**
     * @param k the key of the property
     * @param v the value of the property
     * @return true if a new property is created, false if it existed
     */
    public boolean define(String k, Object v) {
	if (getProperty(k) == null) {
	    return put(k, v);
	}
	return false;
    }

    /**
     * @param key the key of the property
     * @return always true
     */
    public boolean canPut(String key) { return true; }

    /**
     * @param cat the category
     * @return the key in this category
     */
    public Enumeration keysIn(String cat) {
	if (cat.equals("Model")) {
	    return props.keys();
	} else {
	    return EnumerationEmpty.theInstance();
	}
    }

    /**
     * Set the value of a property iff it is not already set.
     *
     * @param k the key of the property
     * @param v the value of the property
     */
    public void define(String k, boolean v) {
	if (getProperty(k) == null) {
	    put(k, v);
	}
    }

    /**
     * Set the value of a property iff it is not already set.
     *
     * @param k the key of the property
     * @param v the value of the property
     */
    public void define(String k, int v) {
	if (getProperty(k) == null) {
	    put(k, v);
	}
    }

    /**
     * Make the given property undefined.
     *
     * @param k the key of the property
     * @return the value to which the key had been mapped in this hashtable,
     *         or <code>null</code> if the key did not have a mapping
     */
    public Object removeProperty(String k) { return props.remove(k); }

    /**
     * Convenient function for getting the value of boolean
     * properties.
     *
     * @param k the key of the property
     * @return the value of the property
     */
    public boolean getBoolean(String k) {
	return getProperty(k, "false").equals("true");
    }

    ////////////////////////////////////////////////////////////////
    // critiquing

    /**
     * Critique this DesignMaterial and post ToDoItem's to the
     * Designer's list. By default this is done by asking Agency.
     *
     * @param dsgr the designer
     */
    public void critique(Designer dsgr) { Agency.applyAllCritics(this, dsgr); }

    /**
     * Remove all the ToDoItem's generated by this DesignMaterial from
     * the ToDoList of the given Designer. For example, this could be
     * called when the DesignMaterial is deleted from the design
     * document.
     *
     * @param dsgr the designer
     */
    public void removePendingItems(Designer dsgr) {
	dsgr.removeToDoItems(pendingItems);
	pendingItems.removeAllElements();
    }

    /**
     * Remove this DesignMaterial from the design document and free up
     * memory and other resources as much as possible.
     */
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

    /**
     * When a critic produces a ToDoItem, both the Designer and the
     * "offending" DesignMaterial's are notified. By default the
     * ToDoItem is added to the list of generated ToDoItem's. Subclasses
     * may, for example, visually change their appearance to indicate
     * the presence of an error. One paper called this 'clarifiers'. <p>
     *
     * TODO: do I need a Clarifier object in the framework?
     *
     * @param item the todo item
     */
    public void inform(ToDoItem item) { pendingItems.addElement(item); }

    /**
     * Draw the Designer's attention to this DesignMaterial in all
     * views.
     *
     * @see org.tigris.gef.ui.Highlightable#setHighlight(boolean)
     */
    public void setHighlight(boolean h) {
	highlight = h;
	setChanged();
	notifyObservers("HIGHLIGHT");
    }

    /**
     * @see org.tigris.gef.ui.Highlightable#getHighlight()
     */
    public boolean getHighlight() { return highlight; }


    ////////////////////////////////////////////////////////////////
    // notifications and updates

    /**
     * @param key the key of the property
     */
    public void changedProperty(String key) {
	setChanged();
	notifyObservers(key);
    }

    /**
     * @param o the observer to be added
     */
    public void addPersistantObserver(Observer o) {
	if (persistObservers == null) {
	    persistObservers = new Vector();
	}
	persistObservers.removeElement(o);
	persistObservers.addElement(o);
    }

    /**
     * @param o the observer to be removed
     */
    public void removePersistObserver(Observer o) {
	persistObservers.removeElement(o);
    }

    /**
     * @param arg the argument
     */
    public void notifyPersistantObservers(Object arg) {
	if (persistObservers == null) {
	    return;
	}
	Enumeration eachObs = persistObservers.elements();
	while (eachObs.hasMoreElements()) {
	    Observer obs = (Observer) eachObs.nextElement();
	    obs.update(this, arg);
	}
    }

    /**
     * @see java.util.Observable#notifyObservers(java.lang.Object)
     */
    public void notifyObservers(Object arg) {
	super.notifyObservers(arg);
	notifyPersistantObservers(arg);
    }


    ////////////////////////////////////////////////////////////////
    // property change events

    /**
     * @see org.tigris.gef.ui.Highlightable#addPropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public synchronized void addPropertyChangeListener(
            PropertyChangeListener listener) {
	if (propertyListeners == null) {
	    propertyListeners = new Vector();
	}
	propertyListeners.removeElement(listener);
	propertyListeners.addElement(listener);
    }

    /**
     * @see org.tigris.gef.ui.Highlightable#removePropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public synchronized void removePropertyChangeListener(
            PropertyChangeListener listener) {
	if (propertyListeners == null) {
	    return;
	}
	propertyListeners.removeElement(listener);
    }

    /**
     * @param propertyName the key of the property
     * @param oldValue the old value of the property
     * @param newValue the new value of the property
     */
    public void firePropertyChange(String propertyName,
				   boolean oldValue, boolean newValue) {
	firePropertyChange(propertyName,
			   new Boolean(oldValue),
			   new Boolean(newValue));
    }

    /**
     * @param propertyName the key of the property
     * @param oldValue the old value of the property
     * @param newValue the new value of the property
     */
    public void firePropertyChange(String propertyName,
				   int oldValue, int newValue) {
	firePropertyChange(propertyName,
			   new Integer(oldValue),
			   new Integer(newValue));
    }

    /**
     * @param propertyName the key of the property
     * @param oldValue the old value of the property
     * @param newValue the new value of the property
     */
    public void firePropertyChange(String propertyName,
				   Object oldValue, Object newValue) {
	if (propertyListeners == null) {
	    return;
	}
	if (oldValue != null && oldValue.equals(newValue)) {
	    return;
	}
	PropertyChangeEvent evt =
	    new PropertyChangeEvent(this, propertyName, oldValue, newValue);
	for (int i = 0; i < propertyListeners.size(); i++) {
	    PropertyChangeListener target =
		(PropertyChangeListener) propertyListeners.elementAt(i);
	    target.propertyChange(evt);
	}
    }


} /* end class DesignMaterial */
