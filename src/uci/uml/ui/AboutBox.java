// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products
// must be negotiated with University of California. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.





package uci.uml.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.tree.*;
// import com.sun.java.swing.text.*;
// import com.sun.java.swing.border.*;
import com.sun.java.swing.plaf.metal.MetalLookAndFeel;



public class AboutBox extends JFrame {

  ////////////////////////////////////////////////////////////////
  // instance varaibles

  JTabbedPane _tabs = new JTabbedPane();
  JButton _splashButton = new JButton("");
  JTextArea _version = new JTextArea();
  JTextArea _credits = new JTextArea();
  JTextArea _contact = new JTextArea();
  JTextArea _legal = new JTextArea();

  ////////////////////////////////////////////////////////////////
  // constructor
  public AboutBox() {
    super("About Argo/UML");
    String iconName = "Splash";
    ImageIcon splashImage = loadIconResource(iconName, iconName);
    int imgWidth = splashImage.getIconWidth();
    int imgHeight = splashImage.getIconHeight();
    Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
    setLocation(scrSize.width/2 - imgWidth/2,
		       scrSize.height/2 - imgHeight/2);
    //setSize(new Dimension(imgWidth + 10, imgHeight + 40));
    getContentPane().setLayout(new BorderLayout(0, 0));

    _splashButton.setMargin(new Insets(0, 0, 0, 0));
    _splashButton.setIcon(splashImage);


    Font ctrlFont = MetalLookAndFeel.getControlTextFont();
//     _version.setFont(ctrlFont);
//     _credits.setFont(ctrlFont);
//     _legal.setFont(ctrlFont);
//     _contact.setFont(ctrlFont);
    
    _version.setText("Argo/UML Version 0.3\n"+
		     "Built on June 10, 1998\n"+
		     "Includes:\n"+
		     "  GEF v0.6\n"+
		     "  Argo v0.6\n"+
		     "  uci.uml v0.2\n"
		     );

    _credits.setText("Argo/UML was developed by the following:\n"+
		     "\nResearchers:  \n"+
		     "  Jason Robbins\n"+
		     "  David Redmiles\n"+
		     "  David Hilbert\n"+
		     "\nProject Lead:  \n"+
		     "  Jason Robbins\n"+
		     "\nDevelopers and Testers:  \n"+
		     "  Jason Robbins\n"+
		     "  Adam Bonner\n"+
		     "  David Hilbert\n"+
		     "  ICS 125 team Spring 1996\n"+
		     "  ICS 125 teams Spring 1998\n"+
		     "\nSpecial Thanks To:\n"+
		     "  (names of people who submitted bug reports)"
		     );

    _contact.setText("For more information on the Argo project:\n"+
		     " + Visit our web site:\n"+
		     "   http://www.ics.uci.edu/pub/arch/argo\n"+
		     " + Send email to Jason Robbins at:\n"+
		     "   jrobbins@ics.uci.edu\n"+
		     " + Read our conference and journal papers:\n"+
		     "   (list of publications: KBSE'96, IUI'98, ICSE'98, etc.)"
		     );
    
    String s = "";
    s+="Copyright (c) 1996-98 The Regents of the University of California. All\n";
    s+="Rights Reserved. Permission to use, copy, modify, and distribute this\n";
    s+="software and its documentation for educational, research and non-profit\n";
    s+="purposes, without fee, and without a written agreement is hereby granted,\n";
    s+="provided that the above copyright notice and this paragraph appear in all\n";
    s+="copies. Permission to incorporate this software into commercial products must\n";
    s+="be negotiated with the University of California. This software\n";
    s+="program and documentation are copyrighted by The Regents of the University\n";
    s+="of California. The software program and documentation are supplied 'as is',\n";
    s+="without any accompanying services from The Regents. The Regents do not\n";
    s+="warrant that the operation of the program will be uninterrupted or\n";
    s+="error-free. The end-user understands that the program was developed for\n";
    s+="research purposes and is advised not to rely exclusively on the program for\n";
    s+="any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY\n";
    s+="PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,\n";
    s+="INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS\n";
    s+="DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE\n";
    s+="POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY\n";
    s+="DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED\n";
    s+="WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE\n";
    s+="SOFTWARE PROVIDED HEREUNDER IS ON AN 'AS IS' BASIS, AND THE UNIVERSITY OF\n";
    s+="CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,\n";
    s+="ENHANCEMENTS, OR MODIFICATIONS.\n";
    _legal.setText(s);



    
    _tabs.addTab("Splash", _splashButton);
    _tabs.addTab("Version", new JScrollPane(_version));
    _tabs.addTab("Credits", new JScrollPane(_credits));
    _tabs.addTab("Contact Info", new JScrollPane(_contact));
    _tabs.addTab("Legal", new JScrollPane(_legal));
    
    
    getContentPane().setLayout(new BorderLayout(0, 0));
    getContentPane().add(_tabs, BorderLayout.CENTER);
    // add preloading progress bar?
    setSize(imgWidth + 20, imgHeight + 50);
    //pack();
    
  }
  
  ////////////////////////////////////////////////////////////////
  // static methods

  protected static ImageIcon loadIconResource(String imgName, String desc) {
    ImageIcon res = null;
    try {
      java.net.URL imgURL = AboutBox.class.getResource(imageName(imgName));
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
      if (Character.isJavaLetterOrDigit(c)) res += c;
    }
    return res;
  }


} /* end class AboutBox */
