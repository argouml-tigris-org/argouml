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
    Insets insets = parent.getInsets();
    int ncomponents = parent.getComponentCount();
    int w = _labelWidth + _minPropWidth + hgap;
    int h = 0;
    for (int i = 0 ; i < ncomponents ; i++) {
      Component comp = parent.getComponent(i);
      Dimension d = comp.getPreferredSize();
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
    Insets insets = parent.getInsets();
    int ncomponents = parent.getComponentCount();
    int w = _labelWidth + _minPropWidth + hgap;
    int h = 0;
    for (int i = 0 ; i < ncomponents ; i++) {
      Component comp = parent.getComponent(i);
      Dimension d = comp.getMinimumSize();
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
    Insets insets = parent.getInsets();
    int ncomponents = parent.getComponentCount();
    if (ncomponents == 0) return;
    int used = insets.left + insets.right + hgap + _labelWidth;
    int propWidth = Math.max(parent.getSize().width - used,
			     _minPropWidth);
    Graphics g = parent.getGraphics();
    int h = 0;
    for (int i = 0 ; i < ncomponents ; i++) {
      Component comp = parent.getComponent(i);
      Dimension d = comp.getPreferredSize();
      if (i %2 == 0) comp.setBounds(0, h, _labelWidth, _labelHeight);
      else {
	comp.setLocation(_labelWidth + hgap, h);
	comp.setSize(d);
	//comp.setSize(Math.min(d.width, propWidth),
	//	  Math.min(d.height, _labelHeight));
	h += Math.max(d.height, _labelHeight) + vgap;
      }
    }
  }

} /* end class PropsGridLayout */
