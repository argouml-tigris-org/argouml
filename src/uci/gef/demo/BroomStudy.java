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


package uci.gef.demo;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import uci.ui.*;
import uci.gef.*;

public class BroomStudy {

  ////////////////////////////////////////////////////////////////
  // static variables
  public static BroomStudy SINGLETON = new BroomStudy();
  public static String[] _Labels = {
    " CS 11\n Prof Adams\n Bldg. A",
    " CS 12\n Prof Adams\n Bldg. A",
    " CS 13\n Prof Adams\n Bldg. A",
    " CS 144\n Prof Carter\n Bldg. B",
    " CS 124\n Prof Adams\n Bldg. B",
    " CS 196\n Prof Hayes-Grant\n Bldg. B",
    " CS 162\n Prof Carter\n Bldg. B",
    " CS 204\n Prof Carter\n Bldg. A",
    " CS 254\n Prof Ford\n Bldg. A",
    " Math 6A\n Prof Taft\n Bldg. A",
    " Math 6B\n Prof Taft\n Bldg. A",
    " Math 6C\n Prof Taft\n Bldg. C",
    " Math 8\n Prof Arthur\n Bldg. C",
    " Math 132\n Prof Taft\n Bldg. B",
    " Math 142\n Prof Arthur\n Bldg. E",
    " Math 182\n Prof Hoover\n Bldg. B",
    " Hist 1A\n Prof Hayes-Grant\n Bldg. C",
    " Hist 1B\n Prof Hayes-Grant\n Bldg. C",
    " Hist 13\n Prof Hoover\n Bldg. C",
    " Hist 139\n Prof Hoover\n Bldg. D",
    " Math 242\n Prof Arthur\n Bldg. E",
    " Hist 269\n Prof Arthur\n Bldg. E",
    " Hist 287\n Prof Arthur\n Bldg. E",
    " Hist 289\n Prof Hoover\n Bldg. E"
//     " CS 11\n Intro to CS-1\n Prof Adams\n Bldg. A",
//     " CS 12\n Intro to CS-2\n Prof Adams\n Bldg. A",
//     " CS 13\n Intro to CS-3\n Prof Adams\n Bldg. A",
//     " CS 144\n Networking\n Prof Carter\n Bldg. B",
//     " CS 124\n Object-Oriented Programming\n Prof Adams\n Bldg. B",
//     " CS 196\n History of Computing\n Prof Hayes-Grant\n Bldg. B",
//     " CS 162\n User Interfaces\n Prof Carter\n Bldg. B",
//     " CS 204\n Adv. Topics in Databases\n Prof Carter\n Bldg. A",
//     " CS 254\n Intelectual Property\n Prof Ford\n Bldg. A",
//     " Math 6A\n Intro to Calculus-1\n Prof Taft\n Bldg. A",
//     " Math 6B\n Intro to Calculus-2\n Prof Taft\n Bldg. A",
//     " Math 6C\n Intro to Calculus-3\n Prof Taft\n Bldg. C",
//     " Math 8\n Descrete Math\n Prof Arthur\n Bldg. C",
//     " Math 132\n Adv. Real Analysis\n Prof Taft\n Bldg. B",
//     " Math 142\n Adv. Statistics\n Prof Arthur\n Bldg. E",
//     " Math 182\n History of Math\n Prof Hoover\n Bldg. B",
//     " Hist 1A\n Intro to Western Civilization-1\n Prof Hayes-Grant\n Bldg. C",
//     " Hist 1B\n Intro to Western Civilization-2\n Prof Hayes-Grant\n Bldg. C",
//     " Hist 13\n History of Science\n Prof Hoover\n Bldg. C",
//     " Hist 139\n Adv. Historiography\n Prof Hoover\n Bldg. D",
//     " Math 242\n Ethical Issues in Statistics\n Prof Arthur\n Bldg. E",
//     " Hist 269\n Statistics in History\n Prof Arthur\n Bldg. E"
  };

  ////////////////////////////////////////////////////////////////
  // instance variables

  protected JGraphFrame _jgf;
  protected ToolBar _tools;
  protected EventMeter _meter = EventMeter.SINGLETON;

  ////////////////////////////////////////////////////////////////
  // constructors

  public BroomStudy() {
    _jgf = new JGraphFrame();
    _tools = new ToolBar();
    Vector buttons = new Vector();
    JButton b;
    b = _tools.add(new CmdSetMode(ModeSelect.class, "Select"));
    buttons.addElement(b);
    b = _tools.add(new CmdSetMode(ModeBroom.class, "Broom"));
    buttons.addElement(b);
    _tools.addSeparator();
//     b = _tools.add(new CmdSetMode(ModeCreateFigCircle.class,
// 				  "Circle"));
//     buttons.addElement(b);
//     b = _tools.add(new CmdSetMode(ModeCreateFigRect.class,
// 				  "Rectangle"));
//     buttons.addElement(b);
//     _tools.addSeparator();

    b = _tools.add(new CmdAlign(CmdAlign.ALIGN_TOPS));
    buttons.addElement(b);
    b = _tools.add(new CmdAlign(CmdAlign.ALIGN_V_CENTERS));
    buttons.addElement(b);
    b = _tools.add(new CmdAlign(CmdAlign.ALIGN_BOTTOMS));
    buttons.addElement(b);
    _tools.addSeparator();

    b = _tools.add(new CmdAlign(CmdAlign.ALIGN_LEFTS));
    buttons.addElement(b);
    b = _tools.add(new CmdAlign(CmdAlign.ALIGN_H_CENTERS));
    buttons.addElement(b);
    b = _tools.add(new CmdAlign(CmdAlign.ALIGN_RIGHTS));
    buttons.addElement(b);
//     b = _tools.add(new CmdAlign(CmdAlign.ALIGN_TO_GRID));
//     buttons.addElement(b);
    _tools.addSeparator();
    b = _tools.add(new CmdDistribute(CmdDistribute.H_SPACING));
    buttons.addElement(b);
//     b = _tools.add(new CmdDistribute(CmdDistribute.H_CENTERS));
//     buttons.addElement(b);
    b = _tools.add(new CmdDistribute(CmdDistribute.V_SPACING));
    buttons.addElement(b);
//     b = _tools.add(new CmdDistribute(CmdDistribute.V_CENTERS));
//     buttons.addElement(b);
//     _tools.addSeparator();
//     b = _tools.add(new CmdNudge(CmdNudge.LEFT));
//     buttons.addElement(b);
//     b = _tools.add(new CmdNudge(CmdNudge.RIGHT));
//     buttons.addElement(b);
//     b = _tools.add(new CmdNudge(CmdNudge.UP));
//     buttons.addElement(b);
//     b = _tools.add(new CmdNudge(CmdNudge.DOWN));
    //    buttons.addElement(b);
    _tools.addSeparator();
    _tools.add(CmdStartTask.SINGLETON);
    _tools.add(CmdFinished.SINGLETON);

    _jgf.setToolBar(_tools);

    JGraph jg = _jgf.getGraph();
    jg.addMouseListener(_meter);
    jg.addMouseMotionListener(_meter);
    jg.addKeyListener(_meter);

    Enumeration enum = buttons.elements();
    while (enum.hasMoreElements()) {
      b = (JButton) enum.nextElement();
      b.addMouseListener(_meter);
      b.addMouseMotionListener(_meter);
      b.addKeyListener(_meter);
    }

    // make the delete key remove elements from the underlying GraphModel
    //_jgf.getGraph().bindKey(new CmdDispose(), KeyEvent.VK_DELETE, 0);

  }

  public void reset() {
    Editor ce = Globals.curEditor();
    ce.getSelectionManager().deselectAll();
    Vector diagramContents = ce.getLayerManager().getContents();
    diagramContents = (Vector) diagramContents.clone();
    Enumeration enum = diagramContents.elements();
    while (enum.hasMoreElements()) ce.remove((Fig)enum.nextElement());
    Font font = new Font("sans", Font.PLAIN, 10);
    FigText figs[] = new FigText[_Labels.length];
    int row = 30, col = 30;
    int GAP = 24;
    for (int i = 0; i < figs.length; i++) {
      FigText f = new FigText(10, 10, 10, 10);
      figs[i] = f;
      f.setText(_Labels[i]);
      f.setFont(font);
      f.setJustification(FigText.JUSTIFY_LEFT);
      f.setLocation(col, row);
      f.setTextColor(Color.black);
      f.setLineSpacing(-4);
      col += f.getWidth() + GAP;
      if (col > 800) { col = 30; row += f.getHeight() + GAP; }
      ce.add(figs[i]);
    }
  }

  public void start() {
    _jgf.setBounds(0, 0, 1000, 760);
    _jgf.setVisible(true);
    reset();
  }

  ////////////////////////////////////////////////////////////////
  // main

  public static void main(String args[]) {
    Globals.setShowFigTips(false);
    BroomStudy.SINGLETON.start();
  }


} /* end class BroomStudy */


class CmdStartTask extends Cmd {
  public static CmdStartTask SINGLETON = new CmdStartTask();
  public static long StartTime = 0L;
  public static int RunNumber = 1;
  public CmdStartTask() { super("Start..."); }
  public void doIt() {
    System.out.println("\n\nRunNumber: " + RunNumber++);
    RandomPresent rp = new RandomPresent();
    rp.setVisible(true);
  }

  public void startCounting() {
    StartTime = System.currentTimeMillis();
    EventMeter.SINGLETON.reset();
    BroomStudy.SINGLETON.reset();
    CmdStartTask.SINGLETON.setEnabled(false);
    CmdFinished.SINGLETON.setEnabled(true);
  }

  public void undoIt() {
    System.out.println("Undo does not make sense for CmdStartTask");
  }

}

class CmdFinished extends Cmd {
  public static CmdFinished SINGLETON = new CmdFinished();
  public static long Duration = 0L;
  public CmdFinished() {
    super("Finished...");
    setEnabled(false);
  }
  public void doIt() {
    Duration = System.currentTimeMillis() - CmdStartTask.StartTime;
    System.out.println("duration: " + (Duration / 1000.0));
    EventMeter.SINGLETON.dump();
    EventMeter.SINGLETON.setCounting(false);
    CmdStartTask.SINGLETON.setEnabled(true);
    CmdFinished.SINGLETON.setEnabled(false);
    RandomCheck rc = new RandomCheck(false);
    rc.setVisible(true);
  }
  public void undoIt() {
    System.out.println("Undo does not make sense for CmdFinished");
  }

}


class EventMeter implements MouseListener, MouseMotionListener, KeyListener {

  public static EventMeter SINGLETON = new EventMeter();

  public int clicks = 0;
  public int drags = 0;
  public int keys = 0;
  public int exits = 0;
  public boolean dragging = false;
  public int distance = 0;
  public int dragDistance = 0;
  public boolean counting = false;

  public int lastX = -1, lastY = -1;

  private EventMeter() { }

  public void reset() {
    clicks = 0;
    drags = 0;
    keys = 0;
    exits = 0;
    dragging = false;
    distance = 0;
    dragDistance = 0;
    counting = true;
  }

  public void dump() {
    System.out.println("clicks = " + clicks);
    System.out.println("drags = " + drags);
    System.out.println("keys = " + keys);
    System.out.println("exits = " + exits);
    System.out.println("distance = " + distance);
    System.out.println("dragDistance = " + dragDistance);
  }

  public void setCounting(boolean b) { counting = b; }

  public void mouseMoved(MouseEvent me) {
    if (!counting) return;
    if (lastY != -1) {
      int dx = me.getX() - lastX;
      int dy = me.getY() - lastY;
      distance += Math.sqrt(dx*dx+dy*dy);
    }
    dragging = false;
    lastX = me.getX();
    lastY = me.getY();
  }
  public void mouseDragged(MouseEvent me) {
    if (!counting) return;
    if (lastY != -1) {
      int dx = me.getX() - lastX;
      int dy = me.getY() - lastY;
      dragDistance += Math.sqrt(dx*dx+dy*dy);
    }
    if (!dragging) drags++;
    dragging = true;
    lastX = me.getX();
    lastY = me.getY();
  }
  public void mousePressed(MouseEvent me) {
    if (!counting) return;
    clicks++;
  }
  public void mouseReleased(MouseEvent me) { }
  public void mouseExited(MouseEvent me) {
    if (!counting) return;
    exits++;
  }
  public void mouseEntered(MouseEvent me) { }
  public void mouseClicked(MouseEvent me) {
  }
  public void keyPressed(KeyEvent ke) {  }
  public void keyReleased(KeyEvent ke) { }
  public void keyTyped(KeyEvent ke) {
    if (!counting) return;
    keys++;
  }
} /* end class EventMeter */


class RandomPresent extends JFrame implements ActionListener {
  public static int NUM_RANDOMS = 6;
  public static int[] nums = new int[NUM_RANDOMS];

  public RandomPresent() {
    super("Short term memory load");
    String numsStr = "";
    Random r = new Random();
    for (int i = 0; i < nums.length; i++) {
      nums[i] = Math.abs(r.nextInt() % 89) + 10;
      numsStr += nums[i] + "  ";
    }
    getContentPane().setLayout(new BorderLayout());
    JLabel instr = new JLabel("Please memorize the following numbers");
    JTextField text = new JTextField(80);
    text.setText(numsStr);
    text.setFont(new Font("Monospaced", Font.BOLD, 24));
    instr.setFont(new Font("Sans", Font.BOLD, 18));
    JButton next = new JButton("Next >");
    getContentPane().add(instr, BorderLayout.NORTH);
    getContentPane().add(text, BorderLayout.CENTER);
    getContentPane().add(next, BorderLayout.SOUTH);
    next.addActionListener(this);
    setBounds(100, 150, 450, 100);
    text.setEditable(false);
    System.out.println("Generated: " + text.getText());
  }

  public void actionPerformed(ActionEvent ae) {
    setVisible(false);
    RandomCheck rc = new RandomCheck(true);
    rc.setVisible(true);
  }
}


class RandomCheck extends JFrame implements ActionListener {
  boolean _isStart = false;
  JTextField text;
  public RandomCheck(boolean isStart) {
    super("Check your memory");
    String numsStr = "";
    _isStart = isStart;
    for (int i = 0; i < RandomPresent.nums.length; i++) {
      numsStr += RandomPresent.nums[i] + "  ";
    }
    getContentPane().setLayout(new BorderLayout());
    JLabel instr = new JLabel("Please enter the numbers you memorized");
    text = new JTextField(80);
    text.setFont(new Font("Monospaced", Font.BOLD, 24));
    instr.setFont(new Font("Sans", Font.BOLD, 18));
    JButton next = new JButton("Done");
    getContentPane().add(instr, BorderLayout.NORTH);
    getContentPane().add(text, BorderLayout.CENTER);
    getContentPane().add(next, BorderLayout.SOUTH);
    next.addActionListener(this);
    setBounds(100, 150, 450, 100);
  }
  public void setVisible(boolean b) {
    super.setVisible(b);
    if (b) text.requestFocus();
  }

  public void actionPerformed(ActionEvent ae) {
    setVisible(false);
    int entered[] = new int[100];
    int numEntered = 0, score = 0;
    StringTokenizer st = new StringTokenizer(text.getText(), ", \t\n");
    while (st.hasMoreElements()) {
      String tok = st.nextToken();
      int r = Integer.parseInt(tok);
      entered[numEntered++] = r;
      for (int i = 0; i < RandomPresent.nums.length; i++) {
	if (r == RandomPresent.nums[i]) { score++; break; }
      }
    }
    System.out.println("Entered: " + text.getText());
    System.out.println("score: " + score);
    if (_isStart) CmdStartTask.SINGLETON.startCounting();
  }
}


