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


package uci.ui;

import java.awt.*;
import java.util.*;

import javax.swing.*;


public class Swatch implements Icon {
  protected static Hashtable _swatches = new Hashtable();

  Color _color = Color.black;

  public static Swatch forColor(Color c) {
    Swatch s = (Swatch) _swatches.get(c);
    if (s == null) {
      s = new Swatch(c);
      _swatches.put(c, s);
    }
    return s;
  }

  public Swatch(Color c) { _color = c; }

  public void paintIcon(Component c, Graphics g, int x, int y) {
    int w = getIconWidth(), h = getIconHeight();
    g.setColor(_color);
    g.fillRect(x, y, getIconWidth(), getIconHeight());
  }
  public int getIconWidth() { return 150; }
  public int getIconHeight() { return 12; }

} /* end class Swatch */
