package uci.uml.ui;

import java.awt.*;
import com.sun.java.swing.*;
import com.sun.java.swing.border.*;
import com.sun.java.swing.event.*;

public class Menu extends JMenu {

  public Menu(String name) { super(name); }

  public JCheckBoxMenuItem addCheckItem(Action a) {
    String name = (String) a.getValue(Action.NAME);
    Icon icon = (Icon) a.getValue(Action.SMALL_ICON);
    JCheckBoxMenuItem mi = new JCheckBoxMenuItem(name, icon, true);
    mi.setHorizontalTextPosition(JButton.RIGHT);
    mi.setVerticalTextPosition(JButton.CENTER);
    mi.setEnabled(a.isEnabled());	
    mi.addActionListener(a);
    add(mi);
    a.addPropertyChangeListener(createActionChangeListener(mi));
    return mi;
  }
  

} /* end class Menu */
