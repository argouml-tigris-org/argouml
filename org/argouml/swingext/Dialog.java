// $Id$
// Copyright (c) 2003 The Regents of the University of California. All
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
 * @author Jeremy Jones
 */
public abstract class Dialog extends JDialog implements ActionListener {
    
    // The set of available optionTypes
    public  static final int CLOSE_OPTION              = 0;
    private static final int YES_NO_OPTION             = 1;
    private static final int YES_NO_HELP_OPTION        = 2;
    private static final int YES_NO_CANCEL_OPTION      = 3;
    private static final int YES_NO_CANCEL_HELP_OPTION = 4;
    public  static final int OK_CANCEL_OPTION          = 5;
    private static final int OK_CANCEL_HELP_OPTION     = 6;
    private static final int DEFAULT_OPTION            = CLOSE_OPTION;
    
    //TODO: These should be overridden on ArgoDialog to populate from
    //the config file
    private int leftBorder = 10;
    private int rightBorder = 10;
    private int topBorder = 10;
    private int bottomBorder = 10;
    protected int componentGap = 10;
    protected int labelGap = 5;
    private int buttonGap = 5;
    
    private JButton okButton = null;
    private JButton cancelButton = null;
    private JButton closeButton = null;
    private JButton yesButton = null;
    private JButton noButton = null;
    private JButton helpButton = null;
    
    private JPanel mainPanel;
    private JComponent content;
    private JPanel buttonPanel;
    
    private int optionType;

    /**
     * Creates a new Dialog with no content component. The default set of
     * button(s) will be displayed. After creating the Dialog, call setContent()
     * to configure the dialog before calling show() to display it.
     *
     * @param owner the owning Frame
     * @param title the title String for the dialog
     * @param modal true if the dialog is modal
     */
    public Dialog(Frame owner, String title, boolean modal) {
        this(owner, title, DEFAULT_OPTION, modal);
    }
            
    /**
     * Creates a new Dialog with no content component, using the specified
     * optionType to determine the set of available buttons.
     * After creating the Dialog, call setContent()
     * to configure the dialog before calling show() to display it.
     *
     * @param owner the owning Frame
     * @param title the title String for the dialog
     * @param theOptionType defines which buttons will be 
     *                      available on the dialog
     * @param modal true if the dialog is modal
     */
    public Dialog(Frame owner, String title, int theOptionType, boolean modal) {
        super(owner, title, modal);
        
        optionType = theOptionType;
        
        JButton[] buttons = createButtons();

        nameButtons();

        content = null;      
            
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, bottomBorder));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(topBorder,
							     leftBorder,
							     bottomBorder,
							     rightBorder));
        getContentPane().add(mainPanel);

        buttonPanel = new JPanel(new SerialLayout(Horizontal.getInstance(),
						   SerialLayout.EAST, 
						   SerialLayout.LEFTTORIGHT,
						   SerialLayout.TOP,
						   buttonGap));
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        for (int i = 0; i < buttons.length; ++i) {
            buttonPanel.add(buttons[i]);
            buttons[i].addActionListener(this);
        }

        getRootPane().setDefaultButton(buttons[0]);
    }

    /**
     * Returns the main component that is displayed within the dialog.
     * 
     * @return  main component displayed in dialog
     **/
    public JComponent getContent() {
        return content;
    }

    /**
     * Sets the main component to be displayed within the dialog.
     * Note: this method is final because it is most likely to be used
     * in subclass constructors, and calling a class's overridable methods in
     * its own constructor is not good practice.
     *
     * @param theContent   main component to display in dialog
     **/
    public final void setContent(JComponent theContent) {
        if (content != null) {
            mainPanel.remove(content);
        }
        content = theContent;
        mainPanel.add(content, BorderLayout.CENTER);
        
        pack();
        centerOnParent();
    }
    
    /**
     * Adds a new button to the set of available option buttons on the dialog.
     * The button will appear after the buttons specified by the optionType.
     * 
     * @param button the button to add to the dialog.
     **/
    public void addButton(JButton button) {
        buttonPanel.add(button);
    }
    
    /**
     * Adds a new button to the set of available option buttons on the dialog.
     * The button will appear at the specified index.
     * 
     * @param button the button to add to the dialog.
     * @param index  index at which to insert new button (0 for first button)
     **/
    public void addButton(JButton button, int index) {
        buttonPanel.add(button, index);
    }    
    
    /**
     * @return the requested button
     */
    protected JButton getOkButton() {
        return okButton;
    }

    /**
     * @return the requested button
     */
    protected JButton getCancelButton() {
        return cancelButton;
    }

    /**
     * @return the requested button
     */
    protected JButton getCloseButton() {
        return closeButton;
    }

    /**
     * @return the requested button
     */
    protected JButton getYesButton() {
        return yesButton;
    }

    /**
     * @return the requested button
     */
    protected JButton getNoButton() {
        return noButton;
    }

    /**
     * @return the requested button
     */
    protected JButton getHelpButton() {
        return helpButton;
    }
    
    /**
     * Default implementation simply closes the dialog when
     * any of the standard buttons is pressed except the Help button.
     *
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okButton
            || e.getSource() == cancelButton
            || e.getSource() == closeButton
            || e.getSource() == yesButton
            || e.getSource() == noButton) {
            hide();
            dispose();
        }
    }
    
    /**
     * Creates the set of JButtons for the current optionType.
     **/
    private JButton[] createButtons() {
        JButton[] buttons;       
        switch(optionType) {
	case YES_NO_OPTION:
	    yesButton = new JButton();
	    noButton = new JButton();
	    buttons = new JButton[] {
		yesButton, noButton 
	    };
	    break;
        
	case YES_NO_HELP_OPTION:
	    yesButton = new JButton();
	    noButton = new JButton();
	    helpButton = new JButton();
	    buttons = new JButton[] {
		yesButton, noButton, helpButton 
	    };
	    break;
        
	case YES_NO_CANCEL_OPTION:
	    yesButton = new JButton();
	    noButton = new JButton();
	    cancelButton = new JButton();
	    buttons = new JButton[] {
		yesButton, noButton, cancelButton 
	    };
	    break;
        
	case YES_NO_CANCEL_HELP_OPTION:
	    yesButton = new JButton();
	    noButton = new JButton();
	    cancelButton = new JButton();
	    helpButton = new JButton();
	    buttons = new JButton[] {
		yesButton, noButton, cancelButton, helpButton 
	    };
	    break;
        
	case OK_CANCEL_OPTION:
	    okButton = new JButton();
	    cancelButton = new JButton();
	    buttons = new JButton[] {
		okButton, cancelButton 
	    };
	    break;
        
	case OK_CANCEL_HELP_OPTION:
	    okButton = new JButton();
	    cancelButton = new JButton();
	    helpButton = new JButton();
	    buttons = new JButton[] {
		okButton, cancelButton, helpButton 
	    };
	    break;
                
	case CLOSE_OPTION:
	default:
	    closeButton = new JButton();
	    buttons = new JButton[] {
		closeButton 
	    };
	    break;
        }
        return buttons;
    }
        
    /**
     * Moves the dialog to be centered on its parent's location on the screen.
     **/
    private void centerOnParent() {
        Dimension size = getSize();
        Dimension p = getParent().getSize();
        int x = (getParent().getX() - size.width)
	    + (int) ((size.width + p.width) / 2d);
        int y = (getParent().getY() - size.height)
	    + (int) ((size.height + p.height) / 2d);
        setLocation(x, y);
    }
        
    /**
     * Subclasses may override this method to change the names and mnemonics of
     * the various JButtons which appear at the bottom of the dialog.
     **/
    protected abstract void nameButtons();
}
