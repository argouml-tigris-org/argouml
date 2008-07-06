// $Id$
// Copyright (c) 1996-2008 The Regents of the University of California. All
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

package org.argouml.uml.diagram.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoNotationEvent;
import org.argouml.application.events.ArgoNotationEventListener;
import org.argouml.kernel.Project;
import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.argouml.model.UmlChangeEvent;
import org.argouml.notation.NotationProvider;
import org.tigris.gef.presentation.FigText;

/**
 * A SingleLine FigText to provide consistency across Figs displaying single
 * lines of text.<ul>
 * <li>The display area is transparent
 * <li>Text is center justified
 * <li>There is no line border
 * <li>There is space below the line for a "Clarifier",
 * i.e. a red squiggly line.
 * </ul><p>
 * 
 * This Fig may have a NotationProvider to render the text.
 *
 * @author Bob Tarling
 */
public class FigSingleLineText extends ArgoFigText
    implements ArgoNotationEventListener  {

    /**
     * The UID.
     */
    private static final long serialVersionUID = -5611216741181499679L;
    
    private static final Logger LOG =
        Logger.getLogger(FigSingleLineText.class);

    /**
     * The properties of 'owner' that this is interested in
     */
    private String[] properties;
    
    /**
     * The notation provider for the text shown in this compartment.
     */
    private NotationProvider notationProvider;
    private HashMap<String, Object> npArguments = new HashMap<String, Object>();

    /*
     * @see org.tigris.gef.presentation.FigText#FigText(
     *         int, int, int, int, boolean)
     */
    public FigSingleLineText(int x, int y, int w, int h, boolean expandOnly) {
        super(x, y, w, h, expandOnly);

        setTextColor(Color.black);
        setFilled(false);
        setTabAction(FigText.END_EDITING);
        setReturnAction(FigText.END_EDITING);
        setLineWidth(0);

        initNotationArguments();
        ArgoEventPump.addListener(ArgoEventTypes.ANY_NOTATION_EVENT, this);
    }

    /*
     * @see org.tigris.gef.presentation.FigText#FigText(
     *         int, int, int, int, boolean)
     */
    public FigSingleLineText(int x, int y, int w, int h, boolean expandOnly, 
            String property) {
        this(x, y, w, h, expandOnly, new String[] {property});
    }

    /*
     * @see org.tigris.gef.presentation.FigText#FigText(
     *         int, int, int, int, boolean)
     */
    public FigSingleLineText(int x, int y, int w, int h, boolean expandOnly, 
            String[] allProperties) {
        this(x, y, w, h, expandOnly);
        this.properties = allProperties;
    }



    @Override
    public Dimension getMinimumSize() {
        Dimension d = new Dimension();

        Font font = getFont();

        if (font == null) {
            return d;
        }
        int maxW = 0;
        int maxH = 0;
        if (getFontMetrics() == null) {
            maxH = font.getSize();
        } else {
            maxH = getFontMetrics().getHeight();
            maxW = getFontMetrics().stringWidth(getText());
        }
        int overallH = (maxH + getTopMargin() + getBotMargin());
        int overallW = maxW + getLeftMargin() + getRightMargin();
        d.width = overallW;
        d.height = overallH;
        return d;
    }

    @Override
    protected boolean isStartEditingKey(KeyEvent ke) {
        if ((ke.getModifiers()
	     & (KeyEvent.META_MASK | KeyEvent.ALT_MASK)) == 0) {
            return super.isStartEditingKey(ke);
        } else {
            return false;
        }
    }
    
    @Override
    public void setOwner(Object owner) {
        super.setOwner(owner);
        if (owner != null && properties != null) {
            Model.getPump().addModelEventListener(
                    this, 
                    owner, 
                    properties);
            setText(); // TODO: MVW: Remove this!
        }
    }
    
    @Override
    public void removeFromDiagram() {
        if (getOwner() != null && properties != null) {
            Model.getPump().removeModelEventListener(
                    this, 
                    getOwner(), 
                    properties);
        }
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        if ("remove".equals(pce.getPropertyName()) 
                && (pce.getSource() == getOwner())) {
            deleteFromModel();
        } else if (notationProvider != null) {
            notationProvider.updateListener(this, getOwner(), pce);
        }
        
        if (pce instanceof UmlChangeEvent) {
            final UmlChangeEvent event = (UmlChangeEvent) pce;
            Runnable doWorkRunnable = new Runnable() {
                public void run() {
                    try {
                        updateLayout(event);
                    } catch (InvalidElementException e) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("event = "
                                    + event.getClass().getName());
                            LOG.debug("source = " + event.getSource());
                            LOG.debug("old = " + event.getOldValue());
                            LOG.debug("name = " + event.getPropertyName());
                            LOG.debug("updateLayout method accessed "
                                    + "deleted element ", e);
                        }
                    }
                }  
            };
            SwingUtilities.invokeLater(doWorkRunnable);
        }
    }
    
    /**
     * This is a template method called by the ArgoUML framework as the result
     * of a change to a model element. Do not call this method directly
     * yourself.
     * <p>Override this in any subclasses in order to redisplay the Fig
     * due to change of any model element that this Fig is listening to.</p>
     * <p>This method is guaranteed by the framework to be running on the 
     * Swing/AWT thread.</p>
     *
     * @param event the UmlChangeEvent that caused the change
     */
    protected void updateLayout(UmlChangeEvent event) {
        assert event != null;
        if (getOwner() == event.getSource()
                && properties != null
                && Arrays.asList(properties).contains(event.getPropertyName())
                && event instanceof AttributeChangeEvent) {
            /* TODO: Why does it fail for changing 
             * the name of an associationend?
             *  Why should it pass? */
            //assert Arrays.asList(properties).contains(event.getPropertyName()) 
            //  : event.getPropertyName(); 
            // TODO: Do we really always need to do this or only if
            // notationProvider is null?
            setText();
        }

        if (notationProvider != null
                && (!"remove".equals(event.getPropertyName())
                        || event.getSource() != getOwner())) {
            this.setText(notationProvider.toString(getOwner(), npArguments));
            damage();
        }
    }

    
    
    /**
     * This function without parameter shall
     * determine the text of the Fig taking values from the owner,
     * and then call {@link #setText(String)}.
     * TO be implemented as required by sub classes.
     */
    protected void setText() {
    }
    

    /**
     * @return Returns the notationProvider for the text in this compartment.
     */
    public NotationProvider getNotationProvider() {
        return notationProvider;
    }

    /**
     * @param np The notationProvider to set.
     */
    void setNotationProvider(NotationProvider np) {
        if (notationProvider != null) {
            notationProvider.cleanListener(this, getOwner());
        }
        this.notationProvider = np;
        initNotationArguments();
    }

    /**
     * @return Returns the Notation Provider Arguments.
     */
    public HashMap<String, Object> getNpArguments() {
        return npArguments;
    }

    private void initNotationArguments() {
        Project p = getProject();
        if (p != null) {
            npArguments.put("rightGuillemot", 
                    p.getProjectSettings().getRightGuillemot());
            npArguments.put("leftGuillemot", 
                    p.getProjectSettings().getLeftGuillemot());
        }
    }
    
    public void notationAdded(ArgoNotationEvent e) {
        // Do nothing
    }

    public void notationChanged(ArgoNotationEvent e) {
        initNotationArguments();
//        if (getOwner() == null) return;
//        setText();
    }

    public void notationProviderAdded(ArgoNotationEvent e) {
        // Do nothing
    }

    public void notationProviderRemoved(ArgoNotationEvent e) {
        // Do nothing    
    }

    public void notationRemoved(ArgoNotationEvent e) {
        // Do nothing        
    }
}
