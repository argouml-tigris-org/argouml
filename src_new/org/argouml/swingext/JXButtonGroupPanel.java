// $Id$
// Copyright (c) 2006 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.swingext;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Iterator;

import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;
import javax.swing.LayoutFocusTraversalPolicy;

/**
 * Container which makes it easy to manage button groups
 * it also supports correct focus and selection transfer
 * with help of arrow keys
 * 
 * Example:
 *
 * JXButtonGroupPanel panel = new JXButtonGroupPanel();  
 * panel.add(new JRadioButton("One"));
 * panel.add(new JRadioButton("Two"));
 * panel.add(new JRadioButton("Three"));  
 * 
 * @author Alexander Potochkin
 * https://swinghelper.dev.java.net/
 */
public class JXButtonGroupPanel extends JPanel {
    /**
     * The UID
     */
    private static final long serialVersionUID = -8731489332460566404L;
    private JXButtonGroup[] group;

    /**
     * Creates a JXButtonGroupPanel with a group.
     */
    public JXButtonGroupPanel() {
        this(1);
    }

    /**
     * Creates a JXButtonGroupPanel with a group.
     * 
     * @param layoutManager     the LayoutManager to be set.
     */
    public JXButtonGroupPanel(LayoutManager layoutManager) {
        this(layoutManager, 1);
    }

    /**
     * Creates a JXButtonGroupPanel with the given layout and the given number 
     * of groups.
     * 
     * @param layoutManager     the LayoutManager to be set.
     * @param numberOfGroups    the number of JRadioButton groups to be created
     */
    public JXButtonGroupPanel(LayoutManager layoutManager, int numberOfGroups) {
        this(numberOfGroups);
        this.setLayout(layoutManager);
    }
    
    /**
     * Creates a JXButtonGroupPanel with the given number of groups
     *  
     * @param numberOfGroups    the number of JRadioButton groups to be created
     */
    public JXButtonGroupPanel(int numberOfGroups) {
        assert (numberOfGroups > 0);
        
        group = new JXButtonGroup[numberOfGroups];
        for (int i = 0; i < numberOfGroups; i++) {
            group[i] = new JXButtonGroup();
        }
        
//        setFocusTraversalPolicyProvider(true);
        setFocusTraversalPolicy(new JXButtonPanelFocusTraversalPolicy());

        ActionListener actionHandler = new ActionHandler(this);

        registerKeyboardAction(actionHandler, ActionHandler.FORWARD,
                KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        registerKeyboardAction(actionHandler, ActionHandler.FORWARD,
                KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        registerKeyboardAction(actionHandler, ActionHandler.BACKWARD,
                KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        registerKeyboardAction(actionHandler, ActionHandler.BACKWARD,
                KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * Add the given component of the given group to the panel.
     * If the component is a JRadioButton, it's also added to the button 
     * group.
     * 
     * @see java.awt.Container#addImpl(java.awt.Component, java.lang.Object, int)
     */
    protected void addImpl(Component c, Object constraints, int index, 
            int groupIndex) {
        if (c instanceof JRadioButton) {
            JRadioButton button = (JRadioButton) c;
            group[groupIndex].add(button);
        }
        super.addImpl(c, constraints, index);
    }


    /**
     * Add the given component to the given group of the panel.
     * If the component is a JRadioButton, it's also added to the button 
     * group.
     * 
     * @see java.awt.Container#add(java.awt.Component, int)
     */
    public Component add(Component c, int index, int groupIndex) {
        this.add(c, null, index, groupIndex);
        return c;
    }

    /**
     * Add the given component to the first group of the panel.
     * If the component is a JRadioButton, it's also added to the button 
     * group.
     * 
     * @see java.awt.Container#add(java.awt.Component, int)
     */
    public Component add(Component c, int index) {
        return this.add(c, index, 0);
    }
    
    /**
     * Add the given component to the given group of the panel.
     * If the component is a JRadioButton, it's also added to the button 
     * group.
     * 
     * @see java.awt.Container#add(java.awt.Component, java.lang.Object, int)
     */
    public void add(Component c, Object constraints, int index, 
            int groupIndex) {
        this.addImpl(c, constraints, -1, groupIndex);
    }
    
    /**
     * Add the given component to the first group of the panel.
     * If the component is a JRadioButton, it's also added to the button 
     * group.
     * 
     * @see java.awt.Container#add(java.awt.Component, java.lang.Object, int)
     */
    public void add(Component c, Object constraints, int index) {
        this.add(c, constraints, -1, 0);
    }

    /**
     * Add the given component to the first group of the panel.
     * If the component is a JRadioButton, it's also added to the button 
     * group.
     * 
     * @see java.awt.Container#add(java.awt.Component)
     */
    public Component add(Component c) {
        this.addImpl(c, null, -1, 0);
        return c;
    }

    /**
     * Add the given component to the first group of the panel.
     * If the component is a JRadioButton, it's also added to the button 
     * group.
     * 
     * @see java.awt.Container#add(java.awt.Component)
     */
    public void add(Component c, Object constraints) {
        this.addImpl(c, constraints, -1, 0);
    }
    
    /**
     * Remove the given button from the given group
     * 
     * @see java.awt.Container#remove(int)
     */
    public void remove(int index, int groupIndex) {
        Component c = getComponent(index);
        if (c instanceof JRadioButton) {
            JRadioButton button = (JRadioButton) c;
            group[groupIndex].remove(button);
        }
        super.remove(index);
    }
    
    /**
     * Remove the given button from the first group
     * 
     * @see java.awt.Container#remove(int)
     */
    public void remove(int index) {
        this.remove(index, 0);
    }

    /**
     * Remove every button from the given group
     * 
     * @see java.awt.Container#removeAll()
     */
    public void removeAll(int groupIndex) {
        super.removeAll();
        group[groupIndex].removeAll();
    }

    /**
     * Remove every button from the first group
     * 
     * @see java.awt.Container#removeAll()
     */
    public void removeAll() {
        this.removeAll(0);
    }
    
    /**
     * Counts the number of buttons of the given group
     * 
     * @param groupIndex        the index of the group
     * @return  the number of the buttons
     */
    public int getButtonCount(int groupIndex) {
        return group[groupIndex].getButtonCount();
    }

    /**
     * Counts the number of buttons of the first group
     * 
     * @return  the number of the buttons
     */
    public int getButtonCount() {
        return getButtonCount(0);
    }
    
    /**
     * Getter for a button of the given group
     * 
     * @param i                 the index of the button
     * @param groupIndex        the index of the group
     * @return                  the JRadioButton
     */
    public JRadioButton getButton(int i, int groupIndex) {
        return group[groupIndex].getButton(i);
    }
    
    /**
     * Getter for a button of the first group
     * 
     * @param i         the index of the button
     * @return          the JRadioButton
     */
    public JRadioButton getButton(int i) {
        return this.getButton(i, 0);
    }

    /**
     * Clear selection of all buttons of the given group from the panel
     *
     * @param groupIndex        the index of the group
     */
    public void clearSelection(int groupIndex) {
        group[groupIndex].clearSelection();
    }

    /**
     * Clear selection of all buttons of the first group from the panel
     */
    public void clearSelection() {
        this.clearSelection(0);
    }
    
    /**
     * Selects the radiobutton belonging to the given group with the given 
     * actionCommand
     * 
     * @param actionCommand The actionCommand of the button that should be
     * selected.
     * @param groupIndex        the index of the group
     */
    public void setSelected(String actionCommand, int groupIndex) {
        Iterator buttonsIt = group[groupIndex].getButtons().iterator();
        ButtonModel model = null;
        while (buttonsIt.hasNext()) {
            model = ((JRadioButton) buttonsIt.next()).getModel();
            if (actionCommand.equals(model.getActionCommand())) {
                model.setSelected(true);
                break;
            }
        }
    }
    
    /**
     * Selects the radiobutton belonging to the first group with the given 
     * actionCommand
     * 
     * @param actionCommand The actionCommand of the button that should be
     * selected.
     */
    public void setSelected(String actionCommand) {
        this.setSelected(actionCommand, 0);
    }
    
    /**
     * Set the selected button of a group
     * 
     * @param m         the ButtonModel
     * @param b         true if the button should be selected
     * @param groupIndex        the index of the group
     */
    public void setSelected(ButtonModel m, boolean b, int groupIndex) {
        group[groupIndex].setSelected(m, b);
    }
    
    /**
     * Check if the given buttons belong to the same group.
     * 
     * @param button1   the first button
     * @param button2   the second button
     * @return          true if the buttons belong to the same group
     */
    public boolean buttonsAreGrouped(Component button1, Component button2) {
        for (int i = 0; i < group.length; i++) {
            if (group[i].contains(button1)) {
                
                return group[i].contains(button2);
            }
        }
        // this means that buttons are not grouped.. this shouldn't happen
        return false;
    }
    
    /**
     * Set the selected button of a group
     * 
     * @param m         the ButtonModel
     * @param b         true if the button should be selected
     */
    public void setSelected(ButtonModel m, boolean b) {
        this.setSelected(m, b, 0);
    }
    
    private static class ActionHandler implements ActionListener {
        private final JXButtonGroupPanel panel;
        private JXButtonPanelFocusTraversalPolicy ftp;

        private static final String FORWARD = "moveSelectionForward";
        private static final String BACKWARD = "moveSelectionBackward";

        public ActionHandler(JXButtonGroupPanel jXButtonGroupPanel) {
            this.panel = jXButtonGroupPanel;
            this.ftp = 
                (JXButtonPanelFocusTraversalPolicy)
                    panel.getFocusTraversalPolicy();
        }

        public void actionPerformed(ActionEvent e) {
            String actionCommand = e.getActionCommand();
            Component fo =
                    KeyboardFocusManager.getCurrentKeyboardFocusManager()
                    .getFocusOwner();
            Component next = null;
            
            ftp.isSkipUnselected = false;

            if (FORWARD.equals(actionCommand)) {
                if (fo != ftp.getLastComponent(panel)) {
                    next = getNextElement(fo);
                }
            } else if (BACKWARD.equals(actionCommand)) {
                if (fo != ftp.getFirstComponent(panel)) {
                    next = getPreviousElement(fo);
                }
            } else {
                throw new AssertionError("Unexpected action command: " 
                        + actionCommand);
            }

            ftp.isSkipUnselected = true;

            if (fo instanceof JRadioButton) {
                JRadioButton b = (JRadioButton) fo;
                b.getModel().setPressed(false);                
            }
            if (next != null) {
                next.requestFocusInWindow();
                for (int i = 0; i < panel.group.length; i++) {
                    if (panel.group[i].contains(next)) {
                        JRadioButton b = (JRadioButton) next;
                        if (panel.group[i].getSelection() != null
                                && !b.isSelected()) {
                            b.setSelected(true);
                        }
                    }
                }
            }
        }
        
        private Component getNextElement(Component fo) {
            Component next = ftp.getComponentAfter(panel, fo);
            if (next == null || !panel.buttonsAreGrouped(fo, next)) {
                next = null;
            } else if (!(next instanceof JRadioButton)) {
                next = getNextElement(next);
            }
            return next;
        }
        
        private Component getPreviousElement(Component fo) {
            Component next = ftp.getComponentBefore(panel, fo);
            if (next == null || !panel.buttonsAreGrouped(fo, next)) {
                next = null;
            } else if (!(next instanceof JRadioButton)) {
                next = getPreviousElement(next);
            }
            return next;
        }        
    }

    /**
     * Private class JXButtonPanelFocusTraversalPolicy
     */
    private class JXButtonPanelFocusTraversalPolicy 
        extends LayoutFocusTraversalPolicy {
        /**
         * The UID
         */
        private static final long serialVersionUID = -5752691817123671168L;
        private boolean isSkipUnselected = true;

        protected boolean accept(Component c) {
            for (int i = 0; i < group.length; i++) {
                if (isSkipUnselected && group[i].contains(c)) {
                    JRadioButton b = (JRadioButton) c;
                    if (group[i].getSelection() != null && !b.isSelected()) {
                        return false;
                    }
                }
            }
            return super.accept(c);
        }
    }
}
    