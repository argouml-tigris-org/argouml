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




// File: SamplePalette.java
// Classes: SamplePalette
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef.demo;

import java.awt.*;
import java.util.*;
import javax.swing.Action;

import uci.gef.*;

/** A class to define a custom palette for use in some demos.
 *
 * @see uci.gef.demo.FlexibleApplet
 * @see uci.gef.demo.BasicApplication
 */

public class SamplePalette extends PaletteFig {

  /** Construct a new palette of example nodes for the Example application */
  public SamplePalette() { super(); }

  /** Define a button to make for the Example application */
  public void defineButtons() {
    super.defineButtons();

    add(new CmdCreateNode(uci.gef.demo.SampleNode.class, "Node One"));
    add(new CmdCreateNode(uci.gef.demo.SampleNode2.class, "Node Two"));
    addSeparator();
    Cmd image1 = new CmdSetMode(ModeCreateFigImage.class,
				"imageURL",
				"http://www.ics.uci.edu/~jrobbins/images/"+
				"new.gif");
    image1.putValue(Action.NAME, "Image1");
    Cmd image2 = new CmdSetMode(ModeCreateFigImage.class,
				"imageURL",
				"http://www.ics.uci.edu/~jrobbins/images/"+
				"gef_banner.gif");
    image2.putValue(Action.NAME, "Image2");

    if (Globals.getAppletContext() != null) {
      add(image1, "Image1", "Image1");
      add(image2, "Image2", "Image2");
    }
  }

  static final long serialVersionUID = -2808153269152965859L;

} /* end class SamplePalette */
