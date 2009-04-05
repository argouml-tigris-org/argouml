// $Id$
// Copyright (c) 1996-2009 The Regents of the University of California. All
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
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.base.Globals;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigText;

/**
 * A single line FigText class extension for editable 
 * FigClass/FigInterface/FigUseCase
 * compartments that use notation. 
 * When selected, this compartment is highlighted.<p>
 *
 * This implementation now supports the extension point compartment in
 * a use case.<p>
 *
 * @author thn
 */
public class CompartmentFigText extends FigSingleLineTextWithNotation
        implements TargetListener {
    
    private static final int MARGIN = 3;
    
    /**
     * The UID.
     */
    private static final long serialVersionUID = 3830572062785308980L;

    private static final Logger LOG =
	Logger.getLogger(CompartmentFigText.class);

    /**
     * The bounding figure of the compartment containing this fig text.
     * @deprecated for 0.27.3 by tfmorris.  Only used for line color which we
     * can get from the render settings or GEF.
     */
    @Deprecated
    private Fig refFig;

    /**
     * Set if the user has selected this component Fig inside the FigNode.
     */
    private boolean highlighted;
    
    /**
     * Construct a CompartmentFigText.
     * 
     * @param element owning uml element
     * @param bounds position and size
     * @param settings render settings
     */
    public CompartmentFigText(Object element, Rectangle bounds,
            DiagramSettings settings) {
        super(element, bounds, settings, true);
        TargetManager.getInstance().addTargetListener(this);

        setJustification(FigText.JUSTIFY_LEFT);
        setRightMargin(MARGIN);
        setLeftMargin(MARGIN);
        // TODO: We'd like these to not be filled, but GEF won't let us
        // select them if we do that.
//        setFilled(false);
    }

    /**
     * Build a new compartment figText of the given dimensions, within the
     * compartment described by <code>aFig</code>.
     * <p>
     * Invoke the parent constructor, then set the reference to the associated
     * compartment figure. The associated FigText is marked as expand only.
     * <p>
     * @param owner owning UML element
     * @param bounds position and size
     * @param settings render settings
     * @param property The property this Fig should listen for
     */
    public CompartmentFigText(Object owner, Rectangle bounds, 
            DiagramSettings settings, String property) {
        this(owner, bounds, settings, new String[] {property});
    }
    
    /**
     * Build a new compartment figText of the given dimensions, within the
     * compartment described by <code>aFig</code>.
     * <p>
     * Invoke the parent constructor, then set the reference to the associated
     * compartment figure. The associated FigText is marked as expand only.
     * <p>
     * @param owner owning UML element
     * @param bounds position and size
     * @param settings render settings
     * @param properties The properties this Fig should listen for
     */
    public CompartmentFigText(Object owner, Rectangle bounds, 
            DiagramSettings settings, String[] properties) {
        super(owner, bounds, settings, true, properties);
        TargetManager.getInstance().addTargetListener(this);
    }
    
    /*
     * @see org.argouml.uml.diagram.ui.FigSingleLineText#removeFromDiagram()
     */
    @Override
    public void removeFromDiagram() {
        super.removeFromDiagram();
        Fig fg = getGroup();
        if (fg instanceof FigGroup) {
            ((FigGroup) fg).removeFig(this);
            setGroup(null);
        }
        TargetManager.getInstance().removeTargetListener(this);
    }

    /**
     * @return  Current fill status&mdash;always <code>false</code>.
     */
    @Override
    public boolean isFilled() {
        return false;
    }

    /**
     * Override for correct graphical behaviour.<p>
     *
     * @return  Current fill colour&mdash;always the fill colour of the
     *          associated compartment fig.
     */
    @Override
    public Color getLineColor() {
        if (refFig != null) {
            return refFig.getLineColor();
        } else {
            // Get the right color from our settings
            return super.getLineColor();
        }
    }

    /**
     * This is actually used to mark this Fig as selected, however setSelected
     * is set final in GEF.
     * TODO: Can setSelected be used without side-effect if GEF is adjusted?
     * Otherwise consider renaming as setSelectedChild and try to make
     * protected.
     * @param flag  <code>true</code> if the entry is to be highlighted,
     *              <code>false</code> otherwise.
     */
    public void setHighlighted(boolean flag) {
        highlighted = flag;
    }
    
    /**
     * Extends the normal paint function in order to display a similar
     * selection-box to that given for a non-resizable FigNode.
     * @param g the graphics object
     * @see org.tigris.gef.presentation.FigText#paint(java.awt.Graphics)
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (highlighted) {
            final int x = getX();
            final int y = getY();
            final int w = getWidth();
            final int h = getHeight();
            g.setColor(Globals.getPrefs().handleColorFor(this));
            
            g.drawRect(x - 1, y - 1, w + 2, h + 2);
            g.drawRect(x, y, w, h);
        }
    }

    /**
     * Return whether this item is highlighted.<p>
     *
     * @return  <code>true</code> if the entry is highlighted,
     *          <code>false</code> otherwise.
     */
    public boolean isHighlighted() {
        return highlighted;
    }
    
    /**
     * Called when text editing has completed on this Fig.
     */
    protected void textEdited() {
        setHighlighted(true);
        super.textEdited();
    }
    
    public void targetAdded(TargetEvent e) {
        if (Arrays.asList(e.getNewTargets()).contains(getOwner())) {
            setHighlighted(true);
            this.damage();
        }
    }

    public void targetRemoved(TargetEvent e) {
        if (e.getRemovedTargetCollection().contains(getOwner())) {
            setHighlighted(false);
            this.damage();
        }
    }

    public void targetSet(TargetEvent e) {
        /* This is needed for when the selection changes from 
         * one compartment fig to an other object. 
         * Without this, the selection indicators would stay on the screen.
         * See issue 5681. */
        setHighlighted((Arrays.asList(e.getNewTargets()).contains(getOwner())));
    }
}
