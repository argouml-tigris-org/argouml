package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Enumeration;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.argouml.application.api.Argo;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.swingext.PopupButton;
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
     * The localization bundle.
     */
    private static final String BUNDLE = "Cognitive";
    
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
    private JSlider             _slider = null;
    
    /**
     * The label which shows the current zoom magnification value.
     */
    private JLabel              _currentValueLabel = null;

    /**
     * Constructs a new ZoomSliderButton.
     */
    public ZoomSliderButton() {
        super();

        Icon icon = ResourceLoaderWrapper.getResourceLoaderWrapper().
            lookupIconResource(Translator.getImageBinding(RESOURCE_NAME),
            Translator.localize(RESOURCE_NAME));
                
        setIcon(icon);
        setToolTipText(Argo.localize(BUNDLE, "button.zoom"));
    }

    /**
     * Creates the slider popup component.
     */
    private void createPopupComponent() {
        _slider = new JSlider(
            JSlider.VERTICAL, 
            MINIMUM_ZOOM, 
            MAXIMUM_ZOOM, 
            0);
        _slider.setInverted(true);
        _slider.setMajorTickSpacing(MAXIMUM_ZOOM / 10);
        _slider.setMinorTickSpacing(MAXIMUM_ZOOM / 20);
        _slider.setPaintTicks(true);
        _slider.setPaintTrack(true);
        int sliderBaseWidth = _slider.getPreferredSize().width;
        _slider.setPaintLabels(true);
        
        for (Enumeration enum = _slider.getLabelTable().elements(); 
            enum.hasMoreElements();) {
            ((Component) enum.nextElement()).setFont(LABEL_FONT);
        }
        
        _slider.setToolTipText(Argo.localize(BUNDLE, 
            "button.zoom.slider-tooltip"));        
        
        _slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                handleSliderValueChange();
            }            
        });
        
        int labelWidth = _slider.getFontMetrics(LABEL_FONT).stringWidth(
            String.valueOf(MAXIMUM_ZOOM)) + 6;
        
        _slider.setPreferredSize(new Dimension(
            sliderBaseWidth + labelWidth, SLIDER_HEIGHT));
        
        _currentValueLabel = new JLabel();
        _currentValueLabel.setHorizontalAlignment(JLabel.CENTER);
        _currentValueLabel.setFont(LABEL_FONT);
        _currentValueLabel.setToolTipText(Argo.localize(
            BUNDLE, "button.zoom.current-zoom-magnification"));
        updateCurrentValueLabel();
        
        JPanel zoomPanel = new JPanel(new BorderLayout(0, 0));
        zoomPanel.add(_slider, BorderLayout.CENTER);
        zoomPanel.add(_currentValueLabel, BorderLayout.NORTH);
                
        setPopupComponent(zoomPanel);
    }
    
    /**
     * Update the slider value every time the popup is shown.
     */
    protected void showPopup() {
        if (_slider == null) {
            createPopupComponent();
        }
        
        Editor ed = Globals.curEditor();
        if (ed != null) {
            _slider.setValue((int) (ed.getScale() * 100d));
        } 
        
        super.showPopup();
    }

    /**
     * Called when the slider value changes.
     */
    private void handleSliderValueChange() {
        updateCurrentValueLabel();
                
        //if (!source.getValueIsAdjusting()) {
        double zoomPercentage = (double) _slider.getValue() / 100d;
                
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
     * Sets the current value label's text to the current slider value.
     */
    private void updateCurrentValueLabel() {
        _currentValueLabel.setText(String.valueOf(_slider.getValue()) + '%');
    }   
}
