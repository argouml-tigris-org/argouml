/*
 * Dialog.java
 *
 * Created on 11 June 2003, 22:54
 */

package org.argouml.swingext;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 * Base class for all dialogs, setting borders and component spacing.
 *
 * @author Bob Tarling
 */
public abstract class Dialog extends JDialog implements ActionListener {
    
    //TODO These should be overridden on ArgoDialog to populate from
    //the config file
    protected int leftBorder = 10;
    protected int rightBorder = 10;
    protected int topBorder = 10;
    protected int bottomBorder = 10;
    protected int componentGap = 10;
    protected int labelGap = 5;
    protected int buttonGap = 5;
    
    protected JButton _okButton;
    protected JButton _cancelButton;
    protected JButton _closeButton;
    protected JButton _yesButton;
    protected JButton _noButton;
    protected JButton _helpButton;
        
    /**
     * Create a new Dialog
     */
    public Dialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
    
        _okButton = new JButton();
        _cancelButton = new JButton();
        _closeButton = new JButton();
        _yesButton = new JButton();
        _noButton = new JButton();
        _helpButton = new JButton();
    
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, bottomBorder));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(topBorder, leftBorder, bottomBorder, rightBorder));
        getContentPane().add(mainPanel);

        nameButtons();
        
        JPanel contentPanel = addComponents();
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        JButton[] buttons = addButtons();
        if (buttons == null || buttons.length == 0) {
            buttons = new JButton[] { _closeButton };
        }
        
        JPanel buttonPanel = new JPanel(new SerialLayout(Horizontal.getInstance(), SerialLayout.EAST, SerialLayout.LEFTTORIGHT, SerialLayout.TOP, buttonGap));
        for (int i = 0; i < buttons.length; ++i) {
            buttonPanel.add(buttons[i]);
            buttons[i].addActionListener(this);
        }        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(buttons[0]);

        pack();
        
        centerOnParent();
    }

    private void centerOnParent() {
        Dimension size = getSize();
        Dimension p = getParent().getSize();
        int x = (getParent().getX() - size.width) + (int) ((size.width + p.width) / 2d);
        int y = (getParent().getY() - size.height) + (int) ((size.height + p.height) / 2d);
        setLocation(x, y);
    }
    
    /**
     * Subclasses may override this method to specify the set of buttons
     * to appear at the botton of the dialog. The first button in the array
     * will be the default.
     * 
     * @return array of JButtons in desired order of appearance (left to right)
    **/
    protected JButton[] addButtons() {
        return new JButton[] { _okButton, _cancelButton };
    }

    /**
     * Subclasses should implement this method to construct
     * a JPanel which contains the dialog's main UI components.
     * 
     * @return JPanel containing the dialog's main UI
    **/
    protected abstract JPanel addComponents();
        
    /**
     * Subclasses may override this method to change the names of the
     * various JButtons which may appear at the bottom of the dialog.
    **/
    protected abstract void nameButtons();
}
