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
import org.workingfrog.i18n.util.Translator;

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

    protected JPanel mainPanel;
    
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
    
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(topBorder, leftBorder, bottomBorder, rightBorder));
        getContentPane().add(mainPanel);

        nameButtons();
        
        addComponents();
        
        addButtons();
        
        JPanel buttonPanel = new JPanel(new SerialLayout(Horizontal.getInstance(), SerialLayout.EAST, SerialLayout.LEFTTORIGHT, SerialLayout.TOP, buttonGap));
        buttonPanel.add(_okButton);
        buttonPanel.add(_cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(_okButton);
        _okButton.addActionListener(this);
        _cancelButton.addActionListener(this);

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
    
    protected abstract void addComponents();
    protected abstract void addButtons();
    protected abstract void nameButtons();
}
