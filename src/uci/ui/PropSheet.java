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




// File: PropSheet.java
// Interfaces: PropSheet
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.ui;

import java.beans.*;
import java.lang.reflect.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;

import uci.gef.Fig;

/** Class that defines the interface for several different kinds of
 *  panels that can edit a group of properties. The only subclass
 *  supplied with GEF is PropSheetCategory, but others could be
 *  defined. */

public class PropSheet extends JPanel
implements PropertyChangeListener {
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


  public void setSelection(Object s) {
    if (_sel == s) return;
    if (_sel instanceof Fig) ((Fig)_sel).removePropertyChangeListener(this);
    _sel = s;
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

  public void setVisible(boolean b) {
    super.setVisible(b);
    if (!b) setSelection(null);
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
      updateComponents();
      _ignorePropChanges = false;
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
//   public void propertyChange(PropertyChangeEvent pce) {
//     // special case for bounding box, because too many updates make dragging
//     // choppy
//     Object arg = null;
//     Object src = pce.getSource();
//     if (src == _sel && arg instanceof PropertyDescriptor) {
//       String propName = ((PropertyDescriptor)arg).getName();
//       long now = System.currentTimeMillis();
//       if ("bounds".equals(propName) && _lastUpdateTime + MIN_UPDATE > now)
// 	return;
//       updateComponent((PropertyDescriptor)arg);
//       _lastUpdateTime = now;
//     }
//   }

  public void propertyChange(PropertyChangeEvent e) {
    if (_ignorePropChanges) return; //HACK!
    // special case for bounding box, because too many updates make dragging
    // choppy
    String pName = e.getPropertyName();
    Object src = e.getSource();
    if (src == _sel && !_ignorePropChanges) {
      long now = System.currentTimeMillis();
      //if ("bounds".equals(pName) && _lastUpdateTime + MIN_UPDATE > now)
      if (_lastUpdateTime + MIN_UPDATE > now) {
	return;
      }
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
