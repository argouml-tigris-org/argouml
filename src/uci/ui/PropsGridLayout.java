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

// File: PropsGridLayout.java
// Interfaces: PropsGridLayout
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.ui;
import java.awt.*;

/** A layout manager used by the PropSheets. Property names are in a
 *  fixed width column on the left. Property values are in an
 *  infinately wide column on the right. Rows are as tall as needed to
 *  display a value. */

public class PropsGridLayout implements LayoutManager {
  int hgap;
  int vgap;
  int _labelWidth = 100;
  int _labelHeight = 22;
  int _minPropWidth = 150;


  public PropsGridLayout(int hgap, int vgap) {
    this.hgap = hgap;
    this.vgap = vgap;
  }

  /** Adds the specified component with the specified name to the layout.
   *
   * @param name the name of the component
   * @param comp the component to be added
   */
  public void addLayoutComponent(String name, Component comp) {
  }

  /** Removes the specified component from the layout. Does not apply.
   *
   * @param comp the component to be removed
   */
  public void removeLayoutComponent(Component comp) {
  }

  /** Returns the preferred dimensions for this layout given the components
   *  int the specified panel.
   *
   * @param parent the component which needs to be laid out
   * @see #minimumLayoutSize
   */
  public Dimension preferredLayoutSize(Container parent) {
    Insets insets = parent.insets();
    int ncomponents = parent.countComponents();
    int w = _labelWidth + _minPropWidth + hgap;
    int h = 0;
    for (int i = 0 ; i < ncomponents ; i++) {
      Component comp = parent.getComponent(i);
      Dimension d = comp.preferredSize();
      h += d.height + vgap;
    }
    return new Dimension(insets.left + insets.right + w,
			 insets.top + insets.bottom + h);
  }

  /**
   * Returns the minimum dimensions needed to layout the components
   * contained in the specified panel.
   *
   * @param parent the component which needs to be laid out
   * @see #preferredLayoutSize
   */
  public Dimension minimumLayoutSize(Container parent) {
    Insets insets = parent.insets();
    int ncomponents = parent.countComponents();
    int w = _labelWidth + _minPropWidth + hgap;
    int h = 0;
    for (int i = 0 ; i < ncomponents ; i++) {
      Component comp = parent.getComponent(i);
      Dimension d = comp.minimumSize();
      h += d.height + vgap;
    }
    return new Dimension(insets.left + insets.right + w,
			 insets.top + insets.bottom + h);
  }

  /** Lays out the container in the specified panel.
   *
   * @param parent the specified component being laid out
   * @see Container
   */
  public void layoutContainer(Container parent) {
    Insets insets = parent.insets();
    int ncomponents = parent.countComponents();
    if (ncomponents == 0) return;
    int used = insets.left + insets.right + hgap + _labelWidth;
    int propWidth = Math.max(parent.size().width - used,
			     _minPropWidth);
    Graphics g = parent.getGraphics();
    int h = 0;
    for (int i = 0 ; i < ncomponents ; i++) {
      Component comp = parent.getComponent(i);
      Dimension d = comp.preferredSize();
      if (i %2 == 0) comp.reshape(0, h, _labelWidth, _labelHeight);
      else {
	comp.move(_labelWidth + hgap, h);
	comp.resize(d);
	//comp.resize(Math.min(d.width, propWidth),
	//	  Math.min(d.height, _labelHeight));
	h += Math.max(d.height, _labelHeight) + vgap;
      }
    }
  }

} /* end class PropsGridLayout */
