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



// From Sun's Beanbox
// Support for drawing a property value in a Canvas.

//package sun.beanbox;
package uci.ui;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.beans.*;

class PropertyCanvas extends JPanel implements MouseListener {

  public PropertyCanvas(JFrame frame, PropertyEditor pe) {
    this.frame = frame;
    editor = pe;
    addMouseListener(this);
  }
  
  public void paint(Graphics g) {
    Rectangle box = new Rectangle(2, 2,
				  getSize().width - 4,
				  getSize().height - 4);
    editor.paintValue(g, box);
  }
  
  public Dimension getMinimumSize() {
    return new Dimension(80, 20);
  }

  public Dimension getPreferredSize() {
    return new Dimension(80, 20);
  }


  private static boolean ignoreClick = false;
  
  public void mouseClicked(MouseEvent evt) {
    if (! ignoreClick) {
      try {
	ignoreClick = true;
	int x = frame.getLocation().x - 30;
	int y = frame.getLocation().y + 50;
	new PropertyDialog(frame, editor, x, y);
      } finally {
	ignoreClick = false;
      }
    }
  }
  
  public void mousePressed(MouseEvent evt) { }  
  public void mouseReleased(MouseEvent evt) { } 
  public void mouseEntered(MouseEvent evt) { } 
  public void mouseExited(MouseEvent evt) { }
  
  private JFrame frame;
  private PropertyEditor editor;
}
