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




// File: RectangleEditor.java
// Interfaces: RectangleEditor
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.beans.editors;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.beans.*;

/** A widget that allows the user to edit rectangles in the property
 *  sheet window. Needs-More-Work: Currently only displays them without
 *  allowing editing.
 *  <A HREF="../features.html#rectangle_editor">
 *  <TT>FEATURE: rectangle_editor</TT></A>
 */

public class RectangleEditor extends JPanel implements PropertyEditor {
  ////////////////////////////////////////////////////////////////
  // instance variables
  protected Rectangle _rect = null;
  private java.util.Vector listeners;

  protected JTextField xField;
  protected JTextField yField;
  protected JTextField wField;
  protected JTextField hField;

  public RectangleEditor() { // //Rectangle r
    setLayout(new GridLayout(1, 4));
    //addNotify();
    setSize(getInsets().left + getInsets().right + 20,
	    getInsets().top + getInsets().bottom + 20);
    xField = new JTextField("0", 4);
    yField = new JTextField("0", 4);
    hField = new JTextField("0", 4);
    wField = new JTextField("0", 4);
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

  public Object getValue() { return _rect; }


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

  public void setEnable(boolean enabled) {
    xField.setEditable(enabled);
    yField.setEditable(enabled);
    wField.setEditable(enabled);
    hField.setEditable(enabled);
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

  public void processEvent(AWTEvent event) {
    if (event.getSource() != this &&
	event.getID() == event.ACTION_EVENT_MASK) {
      readFields();
      return;
    }
    super.processEvent(event);
  }
//   public boolean handleEvent(Event event) {
//     if (event.target != this && event.id == Event.ACTION_EVENT) {
//       readFields();
//       // // postEvent(new Event(this, Event.ACTION_EVENT, _rect));
//       return true;
//     }
//     return super.handleEvent(event);
//   }

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

