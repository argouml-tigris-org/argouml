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

package uci.ui;

import java.util.*;
import java.beans.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import uci.gef.*;
import uci.util.Util;

public class ToolBar extends JToolBar implements MouseListener {
  protected Vector _lockable = new Vector();
  protected Vector _modeButtons = new Vector();

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
    Icon icon = Util.loadIconResource(iconResourceStr, name);
    //System.out.println(icon);
    return add(a, name, icon);
  }

  public JButton add(Action a, String name, Icon icon) {
    JButton b = new JButton(icon);
    if (a instanceof CmdSetMode || a instanceof CmdCreateNode)
      _modeButtons.addElement(b);
    b.setToolTipText(name + " ");
    b.setEnabled(a.isEnabled());
    b.addActionListener(a);
    add(b);
    if (a instanceof CmdSetMode || a instanceof CmdCreateNode)
      _lockable.addElement(b);
    PropertyChangeListener actionPropertyChangeListener =
      createActionChangeListener(b);
    a.addPropertyChangeListener(actionPropertyChangeListener);
    b.addMouseListener(this);
    // needs-more-work: should buttons appear stuck down while action executes?
    return b;
  }

  public JToggleButton addToggle(Action a) {
    String name = (String) a.getValue(Action.NAME);
    Icon icon = (Icon) a.getValue(Action.SMALL_ICON);
    return addToggle(a, name, icon);
  }

  public JToggleButton addToggle(Action a, String name, String iconResourceStr) {
    Icon icon = Util.loadIconResource(iconResourceStr, name);
    //System.out.println(icon);
    return addToggle(a, name, icon);
  }

  public JToggleButton addToggle(Action a, String name, Icon icon) {
    JToggleButton b = new JToggleButton(icon);
    b.setToolTipText(name + " ");
    b.setEnabled(a.isEnabled());
    b.addActionListener(a);
    add(b);
    PropertyChangeListener actionPropertyChangeListener = 
      createActionToggleListener(b);
    a.addPropertyChangeListener(actionPropertyChangeListener);
    // needs-more-work: should buttons appear stuck down while action executes?
    return b;
  }

  public JToggleButton addToggle(Action a, String name,
				 String upRes, String downRes) {
    ImageIcon upIcon = Util.loadIconResource(upRes, name);
    ImageIcon downIcon = Util.loadIconResource(downRes, name);
    JToggleButton b = new JToggleButton(upIcon);
    b.setToolTipText(name + " ");
    b.setEnabled(a.isEnabled());
    b.addActionListener(a);
    b.setPressedIcon(downIcon);
    b.setMargin(new Insets(0,0,0,0));
    add(b);
    PropertyChangeListener actionPropertyChangeListener = 
      createActionToggleListener(b);
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
    b1.setToolTipText(name1 + " ");
    b1.setMargin(new Insets(0,0,0,0));
    b1.getAccessibleContext().setAccessibleName(name1);

    JRadioButton b2 = new JRadioButton(twoUp, false);
    b2.setSelectedIcon(twoDown);
    b2.setToolTipText(name2 + " ");
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

  protected PropertyChangeListener createActionToggleListener(JToggleButton b) {
    return new ActionToggleChangedListener(b);
  }

  private class ActionToggleChangedListener implements PropertyChangeListener {
        JToggleButton button;

        ActionToggleChangedListener(JToggleButton b) {
            super();
            this.button = b;
        }
        public void propertyChange(PropertyChangeEvent e) {
            String propertyName = e.getPropertyName();
            if (e.getPropertyName().equals(Action.NAME)) {
                String text = (String) e.getNewValue();
                button.setText(text);
                button.repaint();
            } else if (propertyName.equals("enabled")) {
                Boolean enabledState = (Boolean) e.getNewValue();
                button.setEnabled(enabledState.booleanValue());
                button.repaint();
            } else if (e.getPropertyName().equals(Action.SMALL_ICON)) {
                Icon icon = (Icon) e.getNewValue();
                button.setIcon(icon);
                button.invalidate();
                button.repaint();
            } 
        }
    }



  ////////////////////////////////////////////////////////////////
  // MouseListener implementation

  public void mouseEntered(MouseEvent me) { }
  public void mouseExited(MouseEvent me) { }
  public void mousePressed(MouseEvent me) { }
  public void mouseReleased(MouseEvent me) { }
  public void mouseClicked(MouseEvent me) {
    Object src = me.getSource();
    if (isModeButton(src)) {
      unpressAllButtonsExcept(src);
      Editor ce = uci.gef.Globals.curEditor();
      if (ce != null) ce.finishMode();
      uci.gef.Globals.setSticky(false);
    }
    if (me.getClickCount() >= 2) {
      if (!(src instanceof JButton)) return;
      JButton b = (JButton) src;
      if (canLock(b)) {
	b.getModel().setPressed(true);
	b.getModel().setArmed(true);
	uci.gef.Globals.setSticky(true);
      }
    }
    else if (me.getClickCount() == 1) {
      if (src instanceof JButton && isModeButton(src)) {
	JButton b = (JButton) src;
	b.setFocusPainted(false);
	b.getModel().setPressed(true);
      }
    }
  }


  protected boolean canLock(Object b) {
    return _lockable.contains(b);
  }

  protected boolean isModeButton(Object b) {
    return _modeButtons.contains(b);
  }

  protected void unpressAllButtonsExcept(Object src) {
    int size = getComponentCount();
    for (int i = 0; i < size; i++) {
      Component c = getComponent(i);
      if (!(c instanceof JButton)) continue;
      if (c == src) continue;
      ((JButton)c).getModel().setArmed(false);
      ((JButton)c).getModel().setPressed(false);
    }
  }

  public void unpressAllButtons() {
    int size = getComponentCount();
    for (int i = 0; i < size; i++) {
      Component c = getComponent(i);
      if (!(c instanceof JButton)) continue;
      ((JButton)c).getModel().setArmed(false);
      ((JButton)c).getModel().setPressed(false);
    }
    // press the first button (usually ModeSelect)
    for (int i = 0; i < size; i++) {
      Component c = getComponent(i);
      if (!(c instanceof JButton)) continue;
      JButton select = (JButton) c;
      select.getModel().setArmed(true);
      select.getModel().setPressed(true);
      return;
    }
  }

  static final long serialVersionUID = -633571897049780787L;


} /* end class ToolBar */
