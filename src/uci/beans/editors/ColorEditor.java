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




// File: ColorEditor.java
// Interfaces: ColorEditor
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.beans.editors;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.beans.*;

/** <A HREF="../features.html#color_picker">
 *  <TT>FEATURE: color_picker</TT></A>
 */

public class ColorEditor extends JPanel
implements PropertyEditor { //, MouseListener {
  ////////////////////////////////////////////////////////////////
  // instance variables

  public Color _color = Color.white;
  private java.util.Vector listeners;

  ////////////////////////////////////////////////////////////////
  // constructors

  public ColorEditor() {
    setLayout(null);
    //addNotify();
    setSize(getInsets().left + getInsets().right + 20,
	    getInsets().top + getInsets().bottom + 20);
    setForeground(Color.lightGray);
  }


  ////////////////////////////////////////////////////////////////
  // accessors

  public void setValue(Object c) {
    if (!(c instanceof Color)) return;
    _color = (Color) c;
    repaint();
    firePropertyChange();
    //ColorPickerGrid.updateIfEditing(this);
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
    //System.out.println("painting!");
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

  public java.awt.Component getCustomEditor() {
    ColorPickerGrid cpg = new ColorPickerGrid(Color.white);
    //System.out.println("made ColorPickerGrid");
    cpg.setPEColor(this);
    //System.out.println("set ColorPickerGrid editor");
    return cpg;
  }

  public boolean supportsCustomEditor() { return true; }

  public Dimension getMinimumSize() {
    return new Dimension(300, 400);
  }

  public Dimension getPreferredSize() {
    return new Dimension(300, 400);
  }

  

  ////////////////////////////////////////////////////////////////
  // event handlers

//   public void mouseEntered(MouseEvent me) { }
//   public void mouseExited(MouseEvent me) { }
//   public void mouseClicked(MouseEvent me) { }
//   public void mouseReleased(MouseEvent me) { }
  
//   public void mousePressed(MouseEvent me) {
//     int gx = me.getX();
//     int gy = me.getY();
//     Component c = this;
//     while (c instanceof Component) {
//       Point loc = c.location();
//       gx += loc.x;
//       gy += loc.y;
//       c = c.getParent();
//     }
//     ColorPickerGrid.edit(this, gx, gy);
//     me.consume();
//   }

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

//   public void hide() {
//     super.hide();
//     //ColorPickerGrid.stopIfEditing(this); 
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

} /* end class ColorEditor */
