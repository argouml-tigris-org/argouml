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

// File: ColorEditor.java
// Interfaces: ColorEditor
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.beans.editors;

import java.awt.*;
import java.beans.*;

/** <A HREF="../features.html#color_picker">
 *  <TT>FEATURE: color_picker</TT></A>
 */

public class ColorEditor extends Panel implements PropertyEditor {
  ////////////////////////////////////////////////////////////////
  // instance variables

  public Color _color = Color.white;
  private java.util.Vector listeners;

  ////////////////////////////////////////////////////////////////
  // constructors

  public ColorEditor() {
    setLayout(null);
    addNotify();
    resize(insets().left + insets().right + 20,
	   insets().top + insets().bottom + 20);
    setForeground(Color.lightGray);
  }


  ////////////////////////////////////////////////////////////////
  // accessors

  public void setValue(Object c) {
    if (!(c instanceof Color)) return;
    _color = (Color) c;
    repaint();
    firePropertyChange();
    ColorPickerGrid.updateIfEditing(this);
  }

  public Object getValue() { return _color; }
  
  public boolean isPaintable() { return true; }

  ////////////////////////////////////////////////////////////////
  // drawing methods

  public void paintValue(java.awt.Graphics g, java.awt.Rectangle box) {
    g.setColor(Color.lightGray);
    g.draw3DRect(box.x, box.y, box.width - 1, box.height - 1, true);
    g.setColor((Color)getValue());
    g.fill3DRect(box.x + 1, box.y + 1, box.width - 2, box.height - 2, true);
  }

  public void paint(java.awt.Graphics g) {
    paintValue(g, getBounds());
  }


  public String getJavaInitializationString() {
    Color c = (Color) getValue();
    return "new Color(" + getAsText() + ")";
  }

  public String getAsText() {
    return "" + _color.getRed() + ", " +
      _color.getBlue() + ", " + _color.getGreen();
  }

  public void setAsText(String text) { }

  public String[] getTags() { return null; }

  public java.awt.Component getCustomEditor() { return this; }

  public boolean supportsCustomEditor() { return true; }


  ////////////////////////////////////////////////////////////////
  // event handlers

  public boolean handleEvent(Event event) {
    if (event.id == Event.MOUSE_DOWN) {
      int gx = event.x;
      int gy = event.y;
      Component c = this;
      while (c instanceof Component) {
	Point loc = c.location();
	gx += loc.x;
	gy += loc.y;
	c = c.getParent();
      }
      ColorPickerGrid.edit(this, gx, gy);
      return true;
    }
    return super.handleEvent(event);
  }

// //   public void colorPicked(Color newColor) {
// //     if (newColor == null) return;
// //     setColor(newColor);
// //     _changes.firePropertyChange(
// //     postEvent(new Event(this, Event.ACTION_EVENT, newColor));
// //   }

  public Frame getFrame() {
    Component c = this;
    while (c != null && !(c instanceof Frame)) {
      c = c.getParent();
    }
    return (Frame) c;
  }

  public void hide() {
    super.hide();
    ColorPickerGrid.stopIfEditing(this); 
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

} /* end class ColorEditor */
