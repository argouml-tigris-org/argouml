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




// File: PropSheetCategory.java
// Interfaces: PropSheetCategory
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.ui;

import java.awt.*;
import javax.swing.*;
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
  //public static final String dots = ".  .  .  .  .  .  .  .  .  .  .  .  .  .";
  public static final String dots = "";

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
  protected JFrame _jframe;
//   protected JScrollPane _scroller = new JScrollPane();
//   protected JPanel _innerPanel = new JPanel();


  ////////////////////////////////////////////////////////////////
  // constructors

  public PropSheetCategory(JFrame f) {
    super();
    _jframe = f;
    setTabName("no key");
    //setLayout(new BorderLayout());
    //_innerPanel.setLayout(new PropsGridLayout(4, 4));
    setLayout(new PropsGridLayout(4, 4));
    //setLayout(new GridLayout(0, 2, 4, 4));
    //setLayout(new FlowLayout());
    //addNotify();
    setSize(getInsets().left + getInsets().right + 250,
	   getInsets().top + getInsets().bottom + 300);
    setFont(getPropertiesFont());
//     _scroller.add(_innerPanel);
//     add(_scroller, BorderLayout.CENTER);
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
    PropertyEditorManager.registerEditor(java.awt.Color.class,
					 uci.beans.editors.ColorEditor.class);
    PropertyEditorManager.registerEditor(java.awt.Rectangle.class,
					 uci.beans.editors.RectangleEditor.class);
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
      JLabel lab = new JLabel(pd.getName() + dots);
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
	else comp.setEnabled(canPut);
      }
    }
  }

  public void show(PropertyDescriptor pd) {
    JLabel lab = (JLabel) _labels.get(pd);
    Component comp = (Component) _keysComps.get(pd);
    setComponentValue(pd, comp);
    if (_shown.containsKey(pd)) return;
    //_innerPanel.add(lab);
    //_innerPanel.add(comp);
    add(lab);
    add(comp);
    lab.setVisible(true);
    comp.setVisible(true);
    _shown.put(pd, pd);
  }

  public void hide(PropertyDescriptor pd) {
    if (!_shown.containsKey(pd)) return;
    JLabel lab = (JLabel) _labels.get(pd);
    Component comp = (Component) _keysComps.get(pd);
    lab.setVisible(false);
    comp.setVisible(false);
    //_innerPanel.remove(lab);
    //_innerPanel.remove(comp);
    remove(lab);
    remove(comp);
    _shown.remove(pd);
  }

  public Dimension getMinimumSize() {
    return new Dimension(100, 100);
  }

  public Dimension getPreferredSize() {
    return new Dimension(300, 400);
  }

  public Dimension getSize() {
    return new Dimension(300, 400);
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
	  System.out.println("exception in makecomp");
	  // Drop through.
	}
      }
      if (editor == null) {
	editor = PropertyEditorManager.findEditor(type);
      }
      if (editor == null) {
	return new JLabel(value == null ? "(null)" : value.toString());
      }
      editor.setValue(value);
      editor.addPropertyChangeListener(this);
      _editorsPds.put(editor, pd);
      _pdsEditors.put(pd, editor);

      if (editor.isPaintable() && editor.supportsCustomEditor())
	comp = new PropertyCanvas(_jframe, editor);
      else if (editor instanceof java.awt.Component) {
	comp = (java.awt.Component) editor;
      }
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


  protected boolean inRecursion = false;
  
  /** Display the value of a given property */
  public void setComponentValue(PropertyDescriptor pd, Component comp) {
    //System.out.println("setComponentValue");

    if (inRecursion) return;
    
    Object value = null;
    try {
       Method getter = pd.getReadMethod();
       Method setter = pd.getWriteMethod();
       Object args[] = { };
       value = getter.invoke(_sel, args);
    }
    catch (Exception ex) { System.out.println("unexpected Exception!"); }
    if (value == null) { System.out.println("null value"); return;}
    // System.out.println("set component value");

    if (comp == null) System.out.println("tag is null");
    
    inRecursion = true;
    if (comp instanceof PropertyEditor)
      ((PropertyEditor)comp).setValue(value);
    else if (comp instanceof PropertyText) {
      ((PropertyText)comp).setText(value.toString());
    }
    else if (comp instanceof PropertySelector) {
      //System.out.println("setComponentValue case 2:" + value);
      String tag = value.toString();
      if (value instanceof Boolean) {
	if (((Boolean)value).booleanValue()) tag = "True";
	else tag = "False";
      }
      if (tag == null) System.out.println("tag is null");
      ((PropertySelector)comp).setSelectedItem(tag);
      //        Vector items = (Vector) _enumProps.get(pd);
      //        if (items != null && items.contains(value))
      //  	((Choice)comp).select(items.indexOf(value));
    }
    //     else if (value instanceof Integer && comp instanceof TextComponent) {
    //       TextComponent tc = (TextComponent) comp;
    //       tc.setText(((Integer) value).toString());
    //     }
    //     else if (value instanceof Boolean && comp instanceof Checkbox) {
    //       ((Checkbox)comp).setState(value.equals(Boolean.TRUE));
    //     }
    else if (comp instanceof Label)
      ((Label)comp).setText(value.toString());
    else {
      PropertyEditor editor = (PropertyEditor) _pdsEditors.get(pd);
      //System.out.println("editor = " + editor);
      if (editor != null) {
	_ignorePropChanges = true;
	//System.out.println("safe update?");
	editor.setValue(value);
	_ignorePropChanges = false;
      }
      //else System.out.println("xxx");
    }
    inRecursion = false;    
  }


  ////////////////////////////////////////////////////////////////
  // update methods

  public void addNotify() {
    if (_keysComps != null) super.addNotify();
  }

  public boolean canEdit(Object item) {
    return item != null && super.canEdit(item);
  }

  public void updateComponents() {
    //System.out.println("updateComponents");
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
    //System.out.println("updateComponent");
    Component comp = (Component) _keysComps.get(pd);
    if (comp != null) setComponentValue(pd, comp);
  }

} /* end class PropSheetCategory */
