package uci.uml.ui;

import java.awt.*;
import java.util.*;
import com.sun.java.swing.*;
import com.sun.java.swing.border.*;

public class StatusBar extends JPanel implements Runnable {
  ////////////////////////////////////////////////////////////////
  // instance variables
  protected JLabel _msg = new JLabel();
  protected JProgressBar _progress = new JProgressBar();
  protected String _statusText;
  
  ////////////////////////////////////////////////////////////////
  // constructor
  public StatusBar() {
    _progress.setMinimum(0);
    _progress.setMaximum(100);
    _progress.setMinimumSize(new Dimension(100, 20));
    _progress.setSize(new Dimension(100, 20));

    _msg.setMinimumSize(new Dimension(300, 20));
    _msg.setSize(new Dimension(300, 20));
    _msg.setFont(new Font("Dialog", Font.PLAIN, 10));
    _msg.setForeground(Color.black);

    setLayout(new BorderLayout());
    setBorder(new EtchedBorder(EtchedBorder.LOWERED));
    add(_msg, BorderLayout.CENTER);
    add(_progress, BorderLayout.EAST);
  }

  public void showStatus(String s) {
    _msg.setText(s);
  }

  public synchronized void doFakeProgress(String s, int work) {
    _statusText = s;
    showStatus(_statusText + "...");
    _progress.setMaximum(work);
    _progress.setValue(0);
    Thread t = new Thread(this);
    t.start();
  }

  public synchronized void run() {
    int work = _progress.getMaximum();
    for (int i = 0; i < work; i++) {
      _progress.setValue(i);
      repaint();
      try { wait(10); }
      catch (Exception ex) { }
    }
    showStatus(_statusText + "... done.");
    repaint();
    try { wait(1000); }
    catch (Exception ex) { }
    _progress.setValue(0);
    showStatus("");
    repaint();
  }


//   public boolean isOptimizedDrawingEnabled() {
//     return false;
//   }
  
} /* end class StatusBar */
