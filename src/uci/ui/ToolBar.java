package uci.ui;

import java.awt.*;
import com.sun.java.swing.*;
import com.sun.java.swing.border.*;
import com.sun.java.swing.event.*;
import java.beans.*;

public class ToolBar extends JToolBar {


  public ToolBar() {
    setFloatable(false);
  }
  
  /**
   * Add a new JButton which dispatches the action.
   *
   * @param a the Action object to add as a new menu item
   */
  public JButton add(Action a) {
    String name = (String) a.getValue(Action.NAME);
    Icon icon = (Icon) a.getValue(Action.SMALL_ICON);
    return add(a, name, icon);
  }
  
  public JButton add(Action a, String name, String iconResourceStr) {
    Icon icon = loadIconResource(imageName(iconResourceStr), name);
    //System.out.println(icon);
    return add(a, name, icon);
  }
  
  public JButton add(Action a, String name, Icon icon) {
    JButton b = new JButton(icon);
    b.setToolTipText(name);
    b.setEnabled(a.isEnabled());
    b.addActionListener(a);
    add(b);
    PropertyChangeListener actionPropertyChangeListener = 
      createActionChangeListener(b);
    a.addPropertyChangeListener(actionPropertyChangeListener);
    // needs-more-work: should buttons appear stuck down while action executes?
    return b;
  }


  public ButtonGroup addRadioGroup(String name1, ImageIcon oneUp,
				      ImageIcon oneDown,
				      String name2, ImageIcon twoUp,
				      ImageIcon twoDown) {
    JRadioButton b1 = new JRadioButton(oneUp, true);
    b1.setSelectedIcon(oneDown);
    b1.setToolTipText(name1);
    b1.setMargin(new Insets(0,0,0,0));
    b1.getAccessibleContext().setAccessibleName(name1);

    JRadioButton b2 = new JRadioButton(twoUp, false);
    b2.setSelectedIcon(twoDown);
    b2.setToolTipText(name2);
    b2.setMargin(new Insets(0,0,0,0));
    b2.getAccessibleContext().setAccessibleName(name2);

    add(b1);
    add(b2);

    //     JPanel p = new JPanel();
    //     p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
    //     p.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
    //     p.add(b1);
    //     p.add(b2);
    //     add(p);
    
    ButtonGroup bg = new ButtonGroup();
    bg.add(b1);
    bg.add(b2);
    return bg;
  }


  protected static ImageIcon loadIconResource(String imgName, String desc) {
    ImageIcon res = null;
    try {
      java.net.URL imgURL = ToolBar.class.getResource(imgName);
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

  
} /* end class ToolBar */
