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



package uci.uml.ui;

import java.awt.*;
import java.util.*;
import javax.swing.*;

import uci.ui.IStatusBar;
import uci.util.Util;


// JWindow? I don't want a frame or close widgets
public class SplashScreen extends JFrame
implements IStatusBar {

  protected StatusBar _statusBar = new StatusBar();


  public SplashScreen(String title, String iconName) {
    super(title);
    ImageIcon splashImage = Util.loadIconResource(iconName);
    JLabel splashButton = new JLabel("");
    if (splashImage != null) {
      int imgWidth = splashImage.getIconWidth();
      int imgHeight = splashImage.getIconHeight();
      Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
      setLocation(scrSize.width/2 - imgWidth/2,
		       scrSize.height/2 - imgHeight/2);
      splashButton.setIcon(splashImage);
    }
    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    getContentPane().setLayout(new BorderLayout(0, 0));
    //splashButton.setMargin(new Insets(0, 0, 0, 0));
    getContentPane().add(splashButton, BorderLayout.CENTER);
    getContentPane().add(_statusBar, BorderLayout.SOUTH);
    // add preloading progress bar?
    //setSize(imgWidth + 50, imgHeight + 50);
    setResizable(false);
    pack();
  }


  public void preload(Vector classnames) {
    //preload classes?
  }


  public StatusBar getStatusBar() { return _statusBar; }
  
  ////////////////////////////////////////////////////////////////
  // IStatusBar
  public void showStatus(String s) { _statusBar.showStatus(s); }
  
  public void setVisible(boolean b) {
    super.setVisible(b);
  }

  ////////////////////////////////////////////////////////////////
  // static methods
    protected static ImageIcon loadIconResource(String imgName, String desc) {
    ImageIcon res = null;
    try {
      java.net.URL imgURL = SplashScreen.class.getResource(imageName(imgName));
      if (imgURL == null) return null;
      //System.out.println(imgName);
      //System.out.println(imgURL);
      return new ImageIcon(imgURL, desc);
    }
    catch (Exception ex) {
      System.out.println("Exception in loadIconResource");
      ex.printStackTrace();
      return new ImageIcon(desc);
    }
  }

  protected static String imageName(String name) {
    return "/uci/Images/" + stripJunk(name) + ".gif";
  }


  protected static String stripJunk(String s) {
    String res = "";
    int len = s.length();
    for (int i = 0; i < len; i++) {
      char c = s.charAt(i);
      if (Character.isJavaIdentifierPart(c)) res += c;
    }
    return res;
  }

} /* end class SplashScreen */
