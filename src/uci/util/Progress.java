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

// File: Progress.java
// Classes: Progress
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.util;

import java.util.*;
import java.awt.*;

/** A general purpose progress bar dialog box that shows percentage
 *  completion of some long running operation, and allows the user to
 *  cancel. Clients of this code should construct a Progress object with
 *  a header and an integer for the total amount of work that will be
 *  done. Then do their own processing, calling advance() every so
 *  often. Eventually, the number of advance() calls should equal the
 *  total amount of work specified in the constructor call.
 *
 * @see Preloader
 * @see uci.gef.Example */

public class Progress extends Frame {

  /** Total work to be done. */
  private int _total;

  /** Work done so far. */
  private int _current = 0;

  /** True after the user presses the cancel button. */
  private boolean _canceled = false;

  /** AWT Canvas used to draw the progress bar. */
  private Canvas _barCanvas;

  /** Header text displayed above the progress bar. Usually describes
   *  the overall task being done. */
  private Label _header;

  /** Progress message. Usually describes the specific step being done
   *  now in the overall task. */
  private TextField _message;

  /** A button for bored users. */
  private Button _cancelButton;

  /** Constants defining progress bar appearance. */
  private static final int BAR_WIDTH = 250;
  private static final int BAR_HEIGHT = 20;
  private static final int BORDER = 10;


  /** Construct a new Progress object.
   *
   * @param header  Describes the long running task.
   * @param total   Number of units of work needed to complete the task. */
  public Progress(String header, int total) {
    /* constructor body */
    _header = new Label(header);
    _total = total;
    if (_total < 1) _total = 1;
    build();
    setTitle("Progress");
  }

  /** Client code calls advance() from inside the code for its long
   *  term task whenever one unit of work is completed. The number of
   *  calls shuold equal the total work specified in the contructor. */
  public void advance() { _current++; paintBar(); }

  /** Return amount of work done so far */
  public int current() { return _current; }

  /** Returns total work to be done */
  public int total() { return _total; }

  /** Returns percentage of total work done so far, as an integer in
   *  the range 0..100 */
  public int percentComplete() { return _current * 100 / _total; }

  /** Return true if the user has pressed the cancel button */
  public boolean canceled() { return _canceled; }

  /** Set the message text that describes the step that the long
   * running task is currently on. */
  public void setMessage(String m) {
    _message.setText(m);
    update(getGraphics());
  }

  /** Returns the proper size of the progress bar in pixels. */
  protected int pixelsComplete() {
    return _current * BAR_WIDTH / _total;
  }

  /** Build the Frame needed to display the progress bar. */
  protected void build() {
    add(_header);
    _header.setAlignment(Label.CENTER);
    _header.setFont(new Font("Times Roman", Font.BOLD, 18));

    setLayout(new GridLayout(4, 1, 10, 0));
    _barCanvas = new Canvas();
    _barCanvas.resize(BAR_WIDTH + 2*BORDER, BAR_HEIGHT +2*BORDER );
    add(_barCanvas);

    _message = new TextField("Working...");
    _message.setEditable(false);
    add(_message);

    Panel buttonPanel = new Panel();
    _cancelButton = new Button("Cancel");
    buttonPanel.add(_cancelButton);
    add(buttonPanel);

    pack();
  }

  /** Repaint the progress bar. */
  public void paint(Graphics g) { paintBar(); }

  /** Checks to see if the user clicked the cancel button. It is the
   *  client class's responsibility to check the canceled flag and
   *  hide and dispose of this window when the operation in progress
   *  is canceled. */
  public boolean action(Event e, Object what) {
    if (e.target == _cancelButton) {
      _canceled = true;
      _cancelButton.setBackground(Color.darkGray);
      return true;
    }
    return false;
  }

  public boolean handleEvent(Event e) {
    if (e.id == Event.WINDOW_DESTROY) {
      hide();
      dispose();
      return true;
    }
    return super.handleEvent(e);
  }

  /** Paint the progress bar and percent complete text. */
  protected void paintBar() {
    Graphics g = _barCanvas.getGraphics();
    Image off = createImage(BAR_WIDTH+2*BORDER, BAR_HEIGHT+2*BORDER);
    Graphics offG = off.getGraphics();
    offG.setColor(getBackground());
    offG.fillRect(0, 0, BAR_WIDTH+2*BORDER, BAR_HEIGHT+2*BORDER);
    offG.setColor(Color.gray);
    offG.fillRect(BORDER, BORDER, pixelsComplete(), BAR_HEIGHT);
    offG.setColor(Color.darkGray);
    offG.draw3DRect(BORDER+1, BORDER+1, pixelsComplete()-1, BAR_HEIGHT -
		 2, true);
    offG.setColor(Color.black);
    offG.drawRect(BORDER, BORDER, BAR_WIDTH, BAR_HEIGHT);
    String perString = Integer.toString(percentComplete()) + "% Complete";
    g.setFont(new Font("Times Roman", Font.PLAIN, 12));
    FontMetrics fm = g.getFontMetrics();
    int halfHeight = fm.getHeight() / 2;
    int halfWidth = fm.stringWidth(perString) / 2;
    offG.drawString(perString,
		 BORDER + BAR_WIDTH / 2 - halfWidth,
		 BORDER + BAR_HEIGHT / 2 + halfHeight);
    g.drawImage(off, 0, 0, null);
    offG.dispose();
    off.flush();
  }

  /** A main() to test out and demostrate the progress bar code */
  public static void main(String argv[]) {
    Progress _p = new Progress("Test", 100);
    _p.show();
    for (int i = 0; i < 100; i++) {
      System.out.println(i);
      if (_p.canceled()) break;
      _p.advance();
      try { Thread.sleep(100); }
      catch (java.lang.InterruptedException ignore) { }
    }
    _p.hide();
    _p.dispose();
  }

} /* end class Progress */
