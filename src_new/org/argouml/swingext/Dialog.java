/*
 * Dialog.java
 *
 * Created on 11 June 2003, 22:54
 */

package org.argouml.swingext;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
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
    
    private JButton _okButton;
    private JButton _cancelButton;
    private JButton _closeButton;
    private JButton _yesButton;
    private JButton _noButton;
    private JButton _helpButton;
    
    private JPanel _mainPanel;
    private JComponent _content;
    private JPanel _buttonPanel;
        
    /**
     * Creates a new Dialog with no content component and a single Close
     * button. After creating the Dialog, call setContent() and setButtons()
     * to configure the dialog before calling show() to display it.
    **/
    public Dialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);

        _okButton = new JButton();
        _cancelButton = new JButton();
        _closeButton = new JButton();
        _yesButton = new JButton();
        _noButton = new JButton();
        _helpButton = new JButton();
        
        _content = null;      

        nameButtons();
            
        _mainPanel = new JPanel();
        _mainPanel.setLayout(new BorderLayout(0, bottomBorder));
        _mainPanel.setBorder(BorderFactory.createEmptyBorder(
            topBorder, leftBorder, bottomBorder, rightBorder));
        getContentPane().add(_mainPanel);

        _buttonPanel = new JPanel(new SerialLayout(
            Horizontal.getInstance(), SerialLayout.EAST, 
            SerialLayout.LEFTTORIGHT, SerialLayout.TOP, buttonGap));
        _mainPanel.add(_buttonPanel, BorderLayout.SOUTH);

        setButtons(new JButton[] { _closeButton }, _closeButton);
    }
    
    public JComponent getContent() {
        return _content;
    }

    /**
     * Sets the main component to be displayed within the dialog.
     *
     * @param content   main component to display in dialog
    **/
    public void setContent(JComponent content) {
        if (_content != null) {
            _mainPanel.remove(_content);
        }
        _content = content;
        _mainPanel.add(_content, BorderLayout.CENTER);
    }
    
    /**
     * Sets the set of buttons to be displayed at the bottom of the dialog.
     * The default button (which must be in this set) may also be specified,
     * or null indicating no default button.
     * 
     * @param   buttons array of JButtons to add
     * @param   defaultButton   member of the array to set as the default
    **/
    public void setButtons(JButton[] buttons, JButton defaultButton) {
        _buttonPanel.removeAll();
        
        for (int i = 0; i < buttons.length; ++i) {
            _buttonPanel.add(buttons[i]);
            buttons[i].addActionListener(this);
        }
        
        getRootPane().setDefaultButton(defaultButton);
    }
    
    public JButton getOkButton() {
        return _okButton;
    }

    public JButton getCancelButton() {
        return _cancelButton;
    }

    public JButton getCloseButton() {
        return _closeButton;
    }

    public JButton getYesButton() {
        return _yesButton;
    }

    public JButton getNoButton() {
        return _noButton;
    }

    public JButton getHelpButton() {
        return _helpButton;
    }
    
    public void show() {
        pack();
        centerOnParent();        
        super.show();
    }
    
    /**
     * Default implementation simply closes the dialog when
     * any of the standard buttons is pressed except the Help button.
    **/
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == _okButton
            || e.getSource() == _cancelButton
            || e.getSource() == _closeButton
            || e.getSource() == _yesButton
            || e.getSource() == _noButton) {
            hide();
            dispose();
        }
    }
    
    private void centerOnParent() {
        Dimension size = getSize();
        Dimension p = getParent().getSize();
        int x = (getParent().getX() - size.width) + (int) ((size.width + p.width) / 2d);
        int y = (getParent().getY() - size.height) + (int) ((size.height + p.height) / 2d);
        setLocation(x, y);
    }
        
    /**
     * Subclasses may override this method to change the names of the
     * various JButtons which may appear at the bottom of the dialog.
    **/
    protected abstract void nameButtons();
}
