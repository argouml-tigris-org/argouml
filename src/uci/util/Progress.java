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




// File: Progress.java
// Classes: Progress
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.util;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

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

public class Progress extends Frame implements ActionListener {

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
    _barCanvas.setSize(BAR_WIDTH + 2*BORDER, BAR_HEIGHT +2*BORDER );
    add(_barCanvas);

    _message = new TextField("Working...");
    _message.setEditable(false);
    add(_message);

    Panel buttonPanel = new Panel();
    _cancelButton = new Button("Cancel");
    _cancelButton.addActionListener(this);
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
  public void actionPerformed(ActionEvent ae) {
    if (ae.getSource() == _cancelButton) {
      _canceled = true;
      _cancelButton.setBackground(Color.darkGray);
    }
  }

//   public boolean handleEvent(Event e) {
//     if (e.id == Event.WINDOW_DESTROY) {
//       setVisible(false);
//       dispose();
//       return true;
//     }
//     return super.handleEvent(e);
//   }

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
    _p.setVisible(false);
    _p.dispose();
  }

  static final long serialVersionUID = -4554332933828014635L;
  
} /* end class Progress */
