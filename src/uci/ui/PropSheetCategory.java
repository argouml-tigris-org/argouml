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

// File: PropSheetCategory.java
// Interfaces: PropSheetCategory
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.ui;
import java.awt.*;
import java.util.*;
import java.beans.*;
import java.lang.reflect.*;
//import symantec.itools.awt.NumericSpinner;

/** A PropSheet that shows only properties in a given Category. The
 *  display of the properties is in a fairly random order down the
 *  sheet.  This class also has several static registration methods
 *  that control how specific properties will be displayed and
 *  editied. */

public class PropSheetCategory extends PropSheet {

  ////////////////////////////////////////////////////////////////
  // constants
  public static final String dots = ".  .  .  .  .  .  .  .  .  .  .  .  .  .";

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** Given a property name, which AWT component edits its value. */
  protected Hashtable _keysComps;
  /** Given an AWT component, what is the name of the property it edits. */
  protected Hashtable _compsKeys;
  protected Hashtable _inUse; // used as a set
  protected Hashtable _labels;
  protected Hashtable _shown; // used as a set
  protected String _category = "Misc";

  protected PropertyDescriptor _properties[];
  protected Frame _frame;


  ////////////////////////////////////////////////////////////////
  // constructors

  public PropSheetCategory(Frame f) {
    super();
    _frame = f;
    setTabName("no key");
    //{{INIT_CONTROLS
    setLayout(new PropsGridLayout(4, 4));
    //setLayout(new GridLayout(0, 2, 4, 4));
    //setLayout(new FlowLayout());
    //addNotify();
    resize(insets().left + insets().right + 250,insets().top + insets().bottom + 300);
    setFont(getPropertiesFont());
    setBackground(new Color(12632256));
    //}}
    _keysComps = new Hashtable(20);
    _compsKeys = new Hashtable(20);
    _labels = new Hashtable(20);
    _inUse = new Hashtable(20);
    _shown = new Hashtable(20);
  }

  ////////////////////////////////////////////////////////////////
  // static initializer
  static {
    String[] searchPath = { "uci.beans.editors", "sun.beans.editors" };
    // needs-more-work: doesn't seem to recognize my components!
    PropertyEditorManager.setEditorSearchPath(searchPath);
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public String getCategory() { return _category; }

  public void setCategory(String cat) {
    if (cat == _category) return;
    _category = cat;
    setTabName(cat);
    if (_sel != null) {
      updateComponents();
      repaint();
    }
  }

  public Component addKeyComp(PropertyDescriptor pd) {
    Component comp = (Component) _keysComps.get(pd);
    if (comp == null) {            // || !rightType(pd, comp))
      comp = makeComp(pd);
      _keysComps.put(pd, comp);
      _compsKeys.put(comp, pd);
      Label lab = new Label(pd.getName() + dots);
      _labels.put(pd, lab);
    }
    _inUse.put(pd, pd);
    return comp;
  }


  public void updateKeysComps() {
    _inUse.clear();
    if (_sel == null) return;
    Class selClass = _sel.getClass();
    //@
    try {
      BeanInfo bi = Introspector.getBeanInfo(selClass);
      _properties = bi.getPropertyDescriptors();
    } catch (IntrospectionException ex) {
      System.out.println("PropertySheet: Couldn't introspect\n" +
			 ex.toString());
      return;
    }

    for (int i = 0; i < _properties.length; i++) {
      // //addPropertyDescriptor(_properties[i], selClass);
      // Don't display hidden or expert properties.
      if (PropCategoryManager.inCategory(_category, _properties[i])) {
	Component comp = addKeyComp(_properties[i]);
	boolean canPut = (_properties[i].getWriteMethod() != null);
	if (comp instanceof TextComponent)
	  ((TextComponent)comp).setEditable(canPut);
	else comp.enable(canPut);
      }
    }
  }

  public void show(PropertyDescriptor pd) {
    Label lab = (Label) _labels.get(pd);
    Component comp = (Component) _keysComps.get(pd);
    setComponentValue(pd, comp);
    if (_shown.containsKey(pd)) return;
    add(lab);
    add(comp);
    lab.show();
    comp.show();
    _shown.put(pd, pd);
  }

  public void hide(PropertyDescriptor pd) {
    if (!_shown.containsKey(pd)) return;
    Label lab = (Label)_labels.get(pd);
    Component comp = (Component)_keysComps.get(pd);
    lab.hide();
    comp.hide();
    remove(lab);
    remove(comp);
    _shown.remove(pd);
  }


  /** If I don't have a comp for a given property name, or the last
   * comp is not suitable, then make a new AWT component (possibly a
   * PropertyEditor). */
  public Component makeComp(PropertyDescriptor pd) {
    Component comp = null;
    try {
      // // Object value = _sel.get(pd);
      Method getter = pd.getReadMethod();
      Method setter = pd.getWriteMethod();
      Object args[] = { };
      Object value = getter.invoke(_sel, args);
      Class type = pd.getPropertyType();
      PropertyEditor editor = null;
      Class pec = pd.getPropertyEditorClass();
      if (pec != null) {
	try {
	  editor = (PropertyEditor)pec.newInstance();
	} catch (Exception ex) {
	  // Drop through.
	}
      }
      if (editor == null) {
	editor = PropertyEditorManager.findEditor(type);
      }
      if (editor == null) {
	return new Label(value == null ? "(null)" : value.toString());
      }
      editor.setValue(value);
      editor.addPropertyChangeListener(this);
      _editorsPds.put(editor, pd);
      _pdsEditors.put(pd, editor);
      if (editor instanceof java.awt.Component) {
	comp = (java.awt.Component) editor;
      }
      else if (editor.isPaintable() && editor.supportsCustomEditor())
	comp = new PropertyCanvas(_frame, editor);
      // if it has a small custom editor, embed it
      // if it has a custom editor but not paintable, use an "Edit" button
      else if (editor.getTags() != null)
	comp = new PropertySelector(editor);
      else if (editor.getAsText() != null) {
	String init = editor.getAsText();
	comp = new PropertyText(editor);
      } else {
	System.err.println("Warning: Property \"" + pd.toString() +
			   "\" has non-displayabale editor. Skipping.");
      }

    } catch (InvocationTargetException ex) {
      System.err.println("Skipping property " + pd.toString() +
			 " ; exception on target: " + ex.getTargetException());
      ex.getTargetException().printStackTrace();
    } catch (IllegalAccessException ex) {
      System.err.println("Skipping property " + pd.toString() +
			 " ; Illegal Access on target: " +
			 ex.toString());
      ex.printStackTrace();
    }
    return comp;
  }


  /** Display the value of a given property */
  public void setComponentValue(PropertyDescriptor pd, Component comp) {
//     Object value = null;
//     try {
//       Method getter = pd.getReadMethod();
//       Method setter = pd.getWriteMethod();
//       Object args[] = { };
//       value = getter.invoke(_sel, args);
//       //Object value = _sel.get(pd);
//     }
//     catch (Exception ex) { System.out.println("unexpected Exception!"); }
//     if (value == null) return;
//     // System.out.println("set component value");
//     if (value instanceof String && comp instanceof TextComponent)
//       ((TextComponent)comp).setText((String) value);
//     else if (comp instanceof Choice) {
//       Vector items = (Vector) _enumProps.get(pd);
//       if (items != null && items.contains(value))
// 	((Choice)comp).select(items.indexOf(value));
//     }
//     else if (value instanceof Integer && comp instanceof TextComponent) {
//       TextComponent tc = (TextComponent) comp;
//       tc.setText(((Integer) value).toString());
//     }
//     else if (value instanceof Boolean && comp instanceof Checkbox) {
//       ((Checkbox)comp).setState(value.equals(Boolean.TRUE));
//     }
//     else if (comp instanceof Label)
//       ((Label)comp).setText(value.toString());
  }


  ////////////////////////////////////////////////////////////////
  // update methods

  public void addNotify() {
    if (_keysComps != null)
      super.addNotify();
  }

  public boolean canEdit(Object item) {
    return item != null &&
      super.canEdit(item);
  }

  public void updateComponents() {
    super.updateComponents();
    updateKeysComps();
    Enumeration keysEnum = _keysComps.keys();
    while (keysEnum.hasMoreElements()) {
      PropertyDescriptor pd = (PropertyDescriptor) keysEnum.nextElement();
      if (_inUse.containsKey(pd)) show(pd); else hide(pd);
    }
    validate();
  }

  public void updateComponent(PropertyDescriptor pd) {
    Component comp = (Component) _keysComps.get(pd);
    if (comp != null) setComponentValue(pd, comp);
  }

} /* end class PropSheetCategory */
