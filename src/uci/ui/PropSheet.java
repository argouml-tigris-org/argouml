// Copyright (c) 1995, 1996 Regents of the University of California.
// All rights reserved.
//
// This software was developed by the Arcadia project
// at the University of California, Irvine.
//
// Redistribution and use in source and binary forms are permitted
// provided that the above copyright notice and this paragraph are
// duplicated in all such forms and that any documentation,
// advertising materials, and other materials related to such
// distribution and use acknowledge that the software was developed
// by the University of California, Irvine.  The name of the
// University may not be used to endorse or promote products derived
// from this software without specific prior written permission.
// THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
// IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
// WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.

// File: PropSheet.java
// Interfaces: PropSheet
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.ui;

import java.beans.*;
import java.lang.reflect.*;
import java.awt.*;
import java.util.*;
import uci.gef.Fig;

/** Class that defines the interface for several different kinds of
 *  panels that can edit a group of properties. The only subclass
 *  supplied with GEF is PropSheetCategory, but others could be
 *  defined. */

public class PropSheet extends Panel
implements Observer, PropertyChangeListener {
  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The object being edited. */
  protected Object _sel = null;

  /** The changes to that object that have not been carried out yet. */
  protected Hashtable _pendingStores = new Hashtable();
  // //protected Hashtable _propertyDescriptors = new Hashtable();
  protected Hashtable _pdsEditors = new Hashtable();
  protected Hashtable _editorsPds = new Hashtable();


  /** True iff every change shold be immeadiatly carried out. */
  protected boolean _autoApply = true;
  protected boolean _ignorePropChanges = false;

  /** Name used to identify this sheet to the user. */
  protected String _tabName = "Misc";

  protected Font _propertiesFont = new Font("Dialog", Font.PLAIN, 10);
  protected long _lastUpdateTime = System.currentTimeMillis();
  public static final int MIN_UPDATE = 200;


  ////////////////////////////////////////////////////////////////
  // constructors

  public PropSheet() { }

  ////////////////////////////////////////////////////////////////
  // accessors

// //   public void addPropertyDescriptor(PropertyDescriptor pd, Class c) {
// //     Hashtable classDesc = (Hashtable) _propertyDescriptors.get(c);
// //     if (classDesc == null) classDesc = new Hashtable();
// //     classDesc.put(pd.getName(), pd);
// //     _propertyDescriptors.put(c, classDesc);
// //   }

// //   public PropertyDescriptor findPD(String name, Class c) {
// //     Hashtable classDesc = (Hashtable) _propertyDescriptors.get(c);
// //     if (classDesc == null) return null;
// //     if (name == null) { System.out.println("Asdasd"); return null; }
// //     return (PropertyDescriptor) classDesc.get(name);
// //   }

  public void setSelection(Object s) {
    if (_sel == s) return;
    if (_sel instanceof Observable) ((Observable)_sel).deleteObserver(this);
    if (_sel instanceof Fig) ((Fig)_sel).removePropertyChangeListener(this);
    _sel = s;
    if (_sel instanceof Observable) ((Observable)_sel).addObserver(this);
    if (_sel instanceof Fig) ((Fig)_sel).addPropertyChangeListener(this);
    updateComponents();
  }

  public void setAutoApply(boolean aa) {
    _autoApply = aa;
    if (_autoApply) apply();
  }

  public String getTabName() { return _tabName; }
  public void setTabName(String tn) { _tabName = tn; }

  public boolean canEdit(Object item) { return true; }

  public void hide() {
    super.hide();
    setSelection(null);
  }

  public Font getPropertiesFont() { return _propertiesFont; }
  public void setPropertiesFont(Font f) {  _propertiesFont = f; }

  ////////////////////////////////////////////////////////////////
  // storing properties

  /** When the user changes a value in a widget, record that fact
   * until the next apply (which may be done immeadiatly). */
  public void store(PropertyDescriptor pd, Object value) {
    if (pd == null || value == null) return;
    _pendingStores.put(pd, value);
    if (_autoApply) apply();
  }

  /** Take all property changes that have been stored and actually
   * change the state of the selected object. */
  public void apply() {
    try {
      _ignorePropChanges = true;
      if (_sel instanceof Fig)
	((Fig)_sel).startTrans();
      if (_sel != null) {
	Enumeration pending = _pendingStores.keys();
	while (pending.hasMoreElements()) {
	  PropertyDescriptor pd = (PropertyDescriptor) pending.nextElement();
	  applyProperty(pd, _pendingStores.get(pd));
	}
      }
      //_sel.put(_pendingStores);
      if (_sel instanceof Fig)
	((Fig)_sel).endTrans();
      _pendingStores.clear();
    }
    finally {
      _ignorePropChanges = false;
      updateComponents();
    }
  }

  protected void applyProperty(PropertyDescriptor pd, Object value) {
    try {
      Object args[] = { value };
      args[0] = value;
      Method setter = pd.getWriteMethod();
      setter.invoke(_sel, args);
    } catch (InvocationTargetException ex) {
      if (ex.getTargetException() instanceof PropertyVetoException) {
	System.out.println("Vetoed; because: " +
			   ex.getTargetException().getMessage());
      }
      else
	System.out.println("InvocationTargetException while updating " +
		pd.getName() + "\n" +  ex.getTargetException().toString());
    } catch (Exception ex) {
      System.out.println("Unexpected exception while updating " +
	    pd.getName() + "\n" + ex.toString());
    }
  }


  /** Abandon any stored changes that have not been applied yet. */
  public void revert() {
    _pendingStores.clear();
    updateComponents();
  }


  ////////////////////////////////////////////////////////////////
  // notifications and updates

  public void updateComponents() { }

  public void updateComponent(PropertyDescriptor pd) { updateComponents(); }

  /** The selected object may have changed one of its properties. */
  public void update(Observable obs, Object arg) {
    // special case for bounding box, because too many updates make dragging
    // choppy
    if (obs == _sel && arg instanceof PropertyDescriptor) {
      String propName = ((PropertyDescriptor)arg).getName();
      long now = System.currentTimeMillis();
      if ("bounds".equals(propName) && _lastUpdateTime + MIN_UPDATE > now)
	return;
      updateComponent((PropertyDescriptor)arg);
      _lastUpdateTime = now;
    }
  }

  public void propertyChange(PropertyChangeEvent e) {
    // special case for bounding box, because too many updates make dragging
    // choppy
    String pName = e.getPropertyName();
    Object src = e.getSource();
    if (src == _sel && !_ignorePropChanges) {
      long now = System.currentTimeMillis();
      if ("bounds".equals(pName) && _lastUpdateTime + MIN_UPDATE > now)
	return;
      updateComponents(); //needs-more-work: narrow to sender?
      // pd?
      _lastUpdateTime = now;
    }
    else {
      PropertyDescriptor pd = (PropertyDescriptor) _editorsPds.get(src);
      if (pd != null) store(pd, ((PropertyEditor)src).getValue());
    }
  }

} /* end class PropSheet */
