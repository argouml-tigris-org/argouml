/*
 *    Geotools2 - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2002, Geotools Project Managment Committee (PMC)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */
package org.argouml.swingext;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;


/**
 * This is the glass pane class that intercepts screen interactions during 
 * system busy states.
 *
 * @author Yexin Chen
 * @author Andrea Aime
 */
public class GlassPane extends JComponent implements AWTEventListener {

    private static final long serialVersionUID = -1170784689759475601L;
	
    private Window theWindow;
    private Component activeComponent;

    /**
     * GlassPane constructor comment.
     *
     * @param component a
     */
    protected GlassPane(Component component) {
        // add adapters that do nothing for keyboard and mouse actions
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            }
        });

        addKeyListener(new KeyAdapter() {
        });

        setActiveComponent(component);
    }

    /**
     * Receives all key events in the AWT and processes the ones that
     * originated from the current window with the glass pane.
     *
     * @param event the AWTEvent that was fired
     */
    public void eventDispatched(AWTEvent event) {
        Object source = event.getSource();

        // discard the event if its source is not from the correct type
        boolean sourceIsComponent = (event.getSource() instanceof Component);

        if ((event instanceof KeyEvent) 
                && event.getID() != KeyEvent.KEY_RELEASED 
                && sourceIsComponent) {
            // If the event originated from the window w/glass pane, consume 
        	// the event
            if ((SwingUtilities.windowForComponent((Component) source) 
            		== theWindow)) {
                ((KeyEvent) event).consume();

                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    /**
     * Finds the glass pane that is related to the specified component.
     *
     * @param startComponent the component used to start the search for the 
     * 			glass pane
     * @param create a flag whether to create a glass pane if one does not exist
     *
     * @return GlassPane
     */
    public static synchronized GlassPane mount(Component startComponent, 
    		boolean create) {
        RootPaneContainer aContainer = null;
        Component aComponent = startComponent;

        // Climb the component hierarchy until a RootPaneContainer is found or 
        // until the very top
        while ((aComponent.getParent() != null) 
        		&& !(aComponent instanceof RootPaneContainer)) {
            aComponent = aComponent.getParent();
        }

        // Guard against error conditions if climb search wasn't successful
        if (aComponent instanceof RootPaneContainer) {
            aContainer = (RootPaneContainer) aComponent;
        }

        if (aContainer != null) {
            // Retrieve an existing GlassPane if old one already exist or 
        	// create a new one, otherwise return null
            if ((aContainer.getGlassPane() != null)
                    && (aContainer.getGlassPane() instanceof GlassPane)) {
                return (GlassPane) aContainer.getGlassPane();
            } else if (create) {
                GlassPane aGlassPane = new GlassPane(startComponent);
                aContainer.setGlassPane(aGlassPane);

                return aGlassPane;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Set the component that ordered-up the glass pane.
     *
     * @param aComponent the UI component that asked for the glass pane
     */
    private void setActiveComponent(Component aComponent) {
        activeComponent = aComponent;
    }

    /**
     * Sets the glass pane as visible or invisible. The mouse cursor will 
     * be set accordingly.
     *
     * @param value DOCUMENT ME!
     */
    public void setVisible(boolean value) {
        if (value) {
            // keep track of the visible window associated w/the component
            // useful during event filtering
            if (theWindow == null) {
                theWindow = SwingUtilities.windowForComponent(activeComponent);

                if (theWindow == null) {
                    if (activeComponent instanceof Window) {
                        theWindow = (Window) activeComponent;
                    }
                }
            }

            // Sets the mouse cursor to hourglass mode
            getTopLevelAncestor().setCursor(
            		Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            activeComponent = theWindow.getFocusOwner();

            // Start receiving all events and consume them if necessary
            Toolkit.getDefaultToolkit().addAWTEventListener(
            		this, AWTEvent.KEY_EVENT_MASK);

            this.requestFocus();

            // Activate the glass pane capabilities
            super.setVisible(value);
        } else {
            // Stop receiving all events
            Toolkit.getDefaultToolkit().removeAWTEventListener(this);

            // Deactivate the glass pane capabilities
            super.setVisible(value);

            // Sets the mouse cursor back to the regular pointer
            if (getTopLevelAncestor() != null) {
                getTopLevelAncestor().setCursor(null);
            }
        }
    }
}
