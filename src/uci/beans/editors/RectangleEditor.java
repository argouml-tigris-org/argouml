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

// File: RectangleEditor.java
// Interfaces: RectangleEditor
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.beans.editors;

import java.awt.*;
import java.beans.*;

/** A widget that allows the user to edit rectangles in the property
 *  sheet window. Needs-More-Work: Currently only displays them without
 *  allowing editing.
 *  <A HREF="../features.html#rectangle_editor">
 *  <TT>FEATURE: rectangle_editor</TT></A>
 */

public class RectangleEditor extends Panel implements PropertyEditor {
  ////////////////////////////////////////////////////////////////
  // instance variables
  protected Rectangle _rect = null;
  private java.util.Vector listeners;

  protected TextField xField;
  protected TextField yField;
  protected TextField wField;
  protected TextField hField;

  public RectangleEditor() { // //Rectangle r
    setLayout(new GridLayout(1, 4));
    addNotify();
    resize(insets().left + insets().right + 20,
	   insets().top + insets().bottom + 20);
    xField = new TextField("0", 4);
    yField = new TextField("0", 4);
    hField = new TextField("0", 4);
    wField = new TextField("0", 4);
    add(xField);
    add(yField);
    add(hField);
    add(wField);
  }


  public void setValue(Object v) {
    if (!(v instanceof Rectangle)) return;
    _rect = (Rectangle) v;
    updateWidgets();
  }

  public Object getValue() {
    return _rect;
  }


  public String getJavaInitializationString() {
    return "new Rectangle(" + getAsText() + ")";
  }


  public String getAsText() {
    Rectangle r = (Rectangle) getValue();
    return "" + r.x + ", " + r.y + ", " + r.width + ", " + r.height;
  }

  public void setAsText(String text) { }

  public String[] getTags() { return null; }

  public boolean supportsCustomEditor() { return true; }
  public java.awt.Component getCustomEditor() { return this; }

  public boolean isPaintable() { return true; }
  public void paintValue(java.awt.Graphics g, java.awt.Rectangle box) {
    Rectangle r = (Rectangle) getValue();
    String rectString = "(" + r.x + "," + r.y + ")+(" +
      r.width + "x" + r.height + " )";
    g.drawString(rectString, box.x + 2, box.x + box.height - 4);
  }

  public void enable() {
    xField.setEditable(true);
    yField.setEditable(true);
    wField.setEditable(true);
    hField.setEditable(true);
  }

  public void disable() {
    xField.setEditable(false);
    yField.setEditable(false);
    wField.setEditable(false);
    hField.setEditable(false);
  }

  public void updateWidgets() {
    xField.setText(Integer.toString(_rect.x));
    yField.setText(Integer.toString(_rect.y));
    wField.setText(Integer.toString(_rect.width));
    hField.setText(Integer.toString(_rect.height));
  }

  protected void readFields() {
    try {
      int x = Integer.parseInt(xField.getText());
      int y = Integer.parseInt(yField.getText());
      int width = Integer.parseInt(wField.getText());
      int height = Integer.parseInt(hField.getText());
      setValue(new Rectangle(x, y, width, height));
    }
    catch (java.lang.NumberFormatException ignore) {
      updateWidgets();
    }
  }


  ////////////////////////////////////////////////////////////////
  // event handlers

  public boolean handleEvent(Event event) {
    if (event.target != this && event.id == Event.ACTION_EVENT) {
      readFields();
      // // postEvent(new Event(this, Event.ACTION_EVENT, _rect));
      return true;
    }
    return super.handleEvent(event);
  }

  public synchronized void
  addPropertyChangeListener(PropertyChangeListener listener) {
    if (listeners == null) {
      listeners = new java.util.Vector();
    }
    listeners.addElement(listener);
  }

  public synchronized void
  removePropertyChangeListener( PropertyChangeListener listener) {
    if (listeners == null) {
      return;
    }
    listeners.removeElement(listener);
  }


  public void firePropertyChange() {
    java.util.Vector targets;
    PropertyChangeListener target;
    synchronized (this) {
      if (listeners == null) return;
      targets = (java.util.Vector) listeners.clone();
    }
    // Tell our listeners that "everything" has changed.
    PropertyChangeEvent evt = new PropertyChangeEvent(this, null, null, null);
    for (int i = 0; i < targets.size(); i++) {
      target = (PropertyChangeListener) targets.elementAt(i);
      target.propertyChange(evt);
    }
  }

} /* end class RectangleEditor */

