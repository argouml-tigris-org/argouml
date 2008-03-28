// $Id$
// Copyright (c) 2003-2008 The Regents of the University of California. All
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

package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.tigris.swidgets.PopupButton;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;

/**
 * A button that can be used to change the zoom magnification of the current
 * diagram. When the user presses the button, a popup is displayed which
 * contains a vertical slider representing the range of zoom magnifications.
 * Dragging the slider changes the zoom magnification for the current diagram.
 *
 * @author Jeremy Jones
 */
public class ZoomSliderButton extends PopupButton {

    /**
     * Used for loading the zoom icon from the Zoom Reset action.
     */
    private static final String RESOURCE_NAME = "Zoom Reset";

    /**
     * Font used for the slider tick labels and for the current magnification
     * value label.
     */
    private static final Font   LABEL_FONT = new Font("Dialog", Font.PLAIN, 10);

    /**
     * The minimum zoom magnification slider value.
     */
    private static final int    MINIMUM_ZOOM = 0;

    /**
     * The maximum zoom magnification slider value.
     */
    private static final int    MAXIMUM_ZOOM = 500;

    /**
     * The preferred height of the slider component.
     */
    private static final int    SLIDER_HEIGHT = 250;

    /**
     * The slider component.
     */
    private JSlider             slider = null;

    /**
     * The text field which shows the current zoom magnification value.
     */
    private JTextField          currentValue = null;
    
    /**
     * Used to enable/disable the popup button.
     */
    private boolean             popupButtonIsActive = true;
    
    /**
     * Indicates whether the popupmenu is showing or not.
     */
    private boolean             popupMenuIsShowing = false;
    
    /**
     * Indicates whether the mouse is over the popup button or not.
     */
    private boolean             mouseIsOverPopupButton = false;

    /**
     * Constructs a new ZoomSliderButton.
     */
    public ZoomSliderButton() {
        super();
        setAction( new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                /* If action comes in with *no* modifiers, it is a pure
                 * keyboard event (e.g. spacebar), so do it.  Anything else
                 * is probably a mouse event, so ignore it. Mouse events are
                 * dealt with by mousePressed() instead (see bottom of page).
                 */ 
                if (e.getModifiers() == 0) {
                    showPopup();
                }
            }
        });

        Icon icon = ResourceLoaderWrapper.lookupIcon(RESOURCE_NAME);

        MyMouseListener myMouseListener = new MyMouseListener();
        addMouseMotionListener(myMouseListener);
        addMouseListener(myMouseListener);

        setIcon(icon);
        setToolTipText(Translator.localize("button.zoom"));
    }

    /**
     * Creates the slider popup component.
     */
    private void createPopupComponent() {
        slider =
            new JSlider(
                    JSlider.VERTICAL,
                    MINIMUM_ZOOM,
                    MAXIMUM_ZOOM,
                    MINIMUM_ZOOM);
        slider.setInverted(true);
        slider.setMajorTickSpacing(MAXIMUM_ZOOM / 10);
        slider.setMinorTickSpacing(MAXIMUM_ZOOM / 20);
        slider.setPaintTicks(true);
        slider.setPaintTrack(true);
        int sliderBaseWidth = slider.getPreferredSize().width;
        slider.setPaintLabels(true);

        for (Enumeration components = slider.getLabelTable().elements();
             components.hasMoreElements();) {
            ((Component) components.nextElement()).setFont(LABEL_FONT);
        }

        slider.setToolTipText(Translator.localize(
                "button.zoom.slider-tooltip"));

        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                handleSliderValueChange();
            }
        });

        int labelWidth =
            slider.getFontMetrics(LABEL_FONT).stringWidth(
                    String.valueOf(MAXIMUM_ZOOM)) + 6;

        slider.setPreferredSize(new Dimension(
            sliderBaseWidth + labelWidth, SLIDER_HEIGHT));

        currentValue = new JTextField(5);
        currentValue.setHorizontalAlignment(JLabel.CENTER);
        currentValue.setFont(LABEL_FONT);
        currentValue.setToolTipText(Translator.localize(
            "button.zoom.current-zoom-magnification"));
        updateCurrentValueLabel();
        currentValue.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleTextEntry();
            }
        });
        currentValue.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                handleTextEntry();
            }
        });

        JPanel currentValuePanel =
            new JPanel(new FlowLayout(
                    FlowLayout.CENTER, 0, 0));
        currentValuePanel.add(currentValue);

        JPanel zoomPanel = new JPanel(new BorderLayout(0, 0));
        zoomPanel.add(slider, BorderLayout.CENTER);
        zoomPanel.add(currentValuePanel, BorderLayout.NORTH);

        setPopupComponent(zoomPanel);
    }

    /**
     * Update the slider value every time the popup is shown.
     */
    @Override
    protected void showPopup() {
        if (slider == null) {
            createPopupComponent();
        }

        Editor ed = Globals.curEditor();
        if (ed != null) {
            slider.setValue((int) (ed.getScale() * 100d));
        }

        if ( popupButtonIsActive ) {
            super.showPopup();
            JPopupMenu pm = (JPopupMenu) this.getPopupComponent().getParent();
            PopupMenuListener pml = new MyPopupMenuListener();
            pm.addPopupMenuListener(pml);
            popupMenuIsShowing = true;
        }
        slider.requestFocus();
    }

    /**
     * Called when the slider value changes.
     */
    private void handleSliderValueChange() {
        updateCurrentValueLabel();

        //if (!source.getValueIsAdjusting()) {
        double zoomPercentage = slider.getValue() / 100d;

        Editor ed = Globals.curEditor();
        if (ed == null || zoomPercentage <= 0.0) {
            return;
        }

        if (zoomPercentage != ed.getScale()) {
            ed.setScale(zoomPercentage);
            ed.damageAll();
        }
        //}
    }

    /**
     * Called when the text field value changes.
     */
    private void handleTextEntry() {
        String value = currentValue.getText();
        if (value.endsWith("%")) {
            value = value.substring(0, value.length() - 1);
        }
        try {
            int newZoom = Integer.parseInt(value);
            if (newZoom < MINIMUM_ZOOM || newZoom > MAXIMUM_ZOOM) {
                throw new NumberFormatException();
            }
            slider.setValue(newZoom);
        } catch (NumberFormatException ex) {
            updateCurrentValueLabel();
        }
    }

    /**
     * Sets the current value label's text to the current slider value.
     */
    private void updateCurrentValueLabel() {
        currentValue.setText(String.valueOf(slider.getValue()) + '%');
    }
    
    private class MyPopupMenuListener extends AbstractAction implements
        PopupMenuListener {

	/**
         * Empty method to satisfy interface.
         */
	public void actionPerformed(ActionEvent e) {
        }

        /**
         * Method gets fired when the popup dies.
         * Conditionally re-enable the button depending on where the pointer 
         * is.
         */
        public void popupMenuCanceled(PopupMenuEvent e) {
            if (mouseIsOverPopupButton) {
                popupButtonIsActive = false;
            } else {
                popupButtonIsActive = true;
            }
            popupMenuIsShowing = false;
        }

        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        }

        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        }

    }

    private class MyMouseListener extends MouseInputAdapter {

        /**
         * Keeps track of mouseover status. 
         */
        @Override
        public void mouseEntered(MouseEvent me) {
            mouseIsOverPopupButton = true;
        }
        
        /**
         * Keeps track of mouseover status, and renables button if necessary.
         */
        @Override
        public void mouseExited(MouseEvent me) {
            mouseIsOverPopupButton = false;
            if (!popupButtonIsActive && !popupMenuIsShowing)
            {
                popupButtonIsActive = true;
            }
        }

        /**
         * Catch the down stroke of mouse click to make the popup appear a tiny
         * bit earlier.
         */
        @Override
        public void mousePressed(MouseEvent me) {
            if (popupButtonIsActive) {
                showPopup();
            }
            else if ( !popupMenuIsShowing )
            {
                popupButtonIsActive = true;
            }
        }
    }
}
