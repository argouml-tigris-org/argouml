package uci.uml.ui;

import java.awt.*;
import com.sun.java.swing.*;
import com.sun.java.swing.border.*;
import com.sun.java.swing.event.*;
import java.beans.*;

public class ToolBar extends JToolBar {

  /**
   * Add a new JButton which dispatches the action.
   *
   * @param a the Action object to add as a new menu item
   */
  public JButton add(Action a) {
    JButton b = new JButton((Icon)a.getValue(Action.SMALL_ICON));
    b.setToolTipText((String)a.getValue(Action.NAME));
    b.setEnabled(a.isEnabled());
    b.addActionListener(a);
    add(b);
    PropertyChangeListener actionPropertyChangeListener = 
      createActionChangeListener(b);
    a.addPropertyChangeListener(actionPropertyChangeListener);
    // needs-more-work: should buttons appear stuck down while action executes?
    return b;
  }

  protected ButtonGroup addRadioGroup(String name1, String name2) {
    ImageIcon icon1up = loadImageIcon(name1 + ".gif", name1);
    ImageIcon icon1down = loadImageIcon(name1 + "Inverse" + ".gif", name1);
    JRadioButton b1 = new JRadioButton(icon1up, true);
    b1.setSelectedIcon(icon1down);
    b1.setToolTipText(name1);
    b1.setMargin(new Insets(0,0,0,0));
    b1.getAccessibleContext().setAccessibleName(name1);
    ImageIcon icon2up = loadImageIcon(name2 + ".gif", name2);
    ImageIcon icon2down = loadImageIcon(name2 + "Inverse" + ".gif", name2);
    JRadioButton b2 = new JRadioButton(icon2up, false);
    b2.setSelectedIcon(icon2down);
    b2.setToolTipText(name2);
    b2.setMargin(new Insets(0,0,0,0));
    b2.getAccessibleContext().setAccessibleName(name2);
    JPanel p = new JPanel();
    p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
    p.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
    p.add(b1);
    p.add(b2);
    add(p);
    ButtonGroup bg = new ButtonGroup();
    bg.add(b1);
    bg.add(b2);
    return bg;
  }

  public ImageIcon loadImageIcon(String filename, String description) {
    // needs-more-work: use class resources
    String imageDir = "f:/jr/dev06/uci/uml/ui/images/";
    return new ImageIcon(imageDir + filename, description);  
  }


} /* end class ToolBar */
