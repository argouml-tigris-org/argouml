// Copyright (c) 1996-99 The Regents of the University of California. All
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

// File: FigClassifierRole.java
// Classes: FigClassifierRole
// Original Author: agauthie@ics.uci.edu
// $Id$

// 10 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Fixed to stop
// collaboration roles all stretching to the top left on reload. Problem was
// caused by setBounds not moving the stereotype. This is only a quick fix. It
// leaves space for the stereotype, even when it isn't being displayed.


package org.argouml.uml.diagram.collaboration.ui;

import java.awt.*;
import java.util.*;
import java.beans.*;
import java.awt.event.*;
import java.text.ParseException;
import javax.swing.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.behavior.collaborations.*;

import org.tigris.gef.presentation.*;
import org.tigris.gef.base.Layer;
import org.tigris.gef.graph.*;

import org.argouml.application.api.*;
import org.argouml.language.helpers.*;
import org.argouml.kernel.*;
import org.argouml.ui.*;
import org.argouml.uml.generator.*;
import org.argouml.uml.diagram.ui.*;


/**
 * <p>Class to display graphics for a UML classifier role in a  collaboration
 *   diagram.</p>
 *
 * <p>Stereotype (if there is one) and name are displayed in the centre of the
 *   box.</p>
 *
 * @author 10 Apr 2002. Jeremy Bennett (mail@jeremybennett.com). Modifications
 *         to ensure stereotypes are handled correctly.
 */

public class FigClassifierRole extends FigNodeModelElement {

    ///////////////////////////////////////////////////////////////////////////
    //
    // Constants
    //
    ///////////////////////////////////////////////////////////////////////////


    /**
     * <p>The minimum padding above and below the stereotype and name.</p>
     */

    protected int _PADDING = 5;


    ///////////////////////////////////////////////////////////////////////////
    //
    // Instance variables
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>The invisible fig that is used as the contact port for the
     *   classifier role.</p>
     */

    FigRect _bigPort;


    /**
     * <p>The fig that is used for the complete classifier role. Identical in
     *   size to {@link #_bigPort}.</p>
     */

    FigRect _cover;

    // add other Figs here as needed


    ///////////////////////////////////////////////////////////////////////////
    //
    // constructors
    //
    ///////////////////////////////////////////////////////////////////////////


    /**
     * <p>Constructor for a new classifier role.</p>
     *
     * <p>An invisible {@link FigRect} as the point of contact for connections
     *   ({@link #_bigPort}), with matching rectangle providing the graphic
     *   rendering {@link #_cover}). Stereotype and name are rendered centrally
     *   in the rectangle.</p>
     */

    public FigClassifierRole() {

        // The big port and cover. Color of the big port is irrelevant

        _bigPort = new FigRect(10, 10, 90, 50, Color.cyan, Color.cyan);
        _cover   = new FigRect(10, 10, 90, 50, Color.black, Color.white);

        // The stereotype. Width is the same as the cover, height is whatever
        // its minimum permitted is. The text should be centred.

        Dimension stereoMin = _stereo.getMinimumSize();

        _stereo.setLineWidth(0);
        _stereo.setFilled(false);
        _stereo.setJustifciaionByName("Center");
        _stereo.setDisplayed(false);

        _stereo.setBounds(10, 10, 90, stereoMin.height);

        // The name. Width is the same as the cover, height is whatever its
        // minimum permitted is. The text of the name will be centred by
        // default. In the same place as the stereotype, since at this stage
        // the stereotype is not displayed. Being a classifier role it is
        // underlined

        Dimension nameMin = _name.getMinimumSize();

        _name.setLineWidth(0);
        _name.setMultiLine(true);
        _name.setFilled(false);
        _name.setUnderline(true);

        _name.setBounds(10, 10, 90, nameMin.height);

        // add Figs to the FigNode in back-to-front order

        addFig(_bigPort);
        addFig(_cover);
        addFig(_stereo);
        addFig(_name);

        // Set our bounds to those we are given.

        Rectangle r = getBounds();
        setBounds(r.x, r.y, r.width, r.height);
    }


    /**
     * <p>Variant constructor that associates the classifier role with a
     *   particular NSUML object.</p>
     *
     * <p>Classifier role is constructed with {@link #FigClassifierRole()}.</p>
     *
     * @param gm    The graph model to use. Ignored in this implementation.
     *
     * @param node  The NSUML object to associate with this Fig.
     */

    public FigClassifierRole(GraphModel gm, Layer lay, Object node) {
        this();
        setLayer(lay);
        setOwner(node);
    }


    /**
     * <p>Return the default name to use for this classifier role.</p>
     *
     * <p>Seems to be immediately overwritten by the empty string, but may be
     *   useful in defining the default name size?</p>
     *
     * @return  The string to use ("new Classifier Role" in this case).
     */

    public String placeString() {
        return "new Classifier Role";
    }


    /**
     * <p>Version of the clone to ensure all sub-figs are copied.</p>
     *
     * <p>Uses the generic superclass clone which gives a vector of all the
     *   figs. Then initialize our instance variables from this vector.</p>
     *
     * @return  A new copy of the the current fig.
     */

    public Object clone() {
        FigClassifierRole figClone = (FigClassifierRole) super.clone();
        Vector            v        = figClone.getFigs();

        figClone._bigPort = (FigRect) v.elementAt(0);
        figClone._cover   = (FigRect) v.elementAt(1);
        figClone._stereo  = (FigText) v.elementAt(2);
        figClone._name    = (FigText) v.elementAt(3);

        return figClone;
    }


    /**
     * <p>Update the stereotype text.</p>
     *
     * <p>If the stereotype text is non-existant, we must make sure it is
     *   marked not displayed, and update the display accordingly.</p>
     *
     * <p>Similarly if there is text, we must make sure it is marked
     *   displayed.</p>
     */

    protected void updateStereotypeText() {

        // Can't do anything if we haven't got an owner to have a stereotype!

        MModelElement me = (MModelElement) getOwner();

        if (me == null) {
            return;
        }

        // Record the old bounds and get the stereotype

        Rectangle   bounds = getBounds();
        MStereotype stereo = me.getStereotype();

        // Where we now have no stereotype, mark as not displayed. Were we do
        // have a stereotype, set the text and mark as displayed. If we remove
        // or add/change a stereotype we adjust the vertical bounds
        // appropriately. Otherwise we need not work out the bounds here. That
        // will be done in setBounds().

        if ((stereo == null) ||
            (stereo.getName() == null) ||
            (stereo.getName().length() == 0)) {

            if (_stereo.isDisplayed()) {
                bounds.height -= _stereo.getBounds().height;
                _stereo.setDisplayed(false);
            }
        }
        else {

            int oldHeight = _stereo.getBounds().height;

            // If we weren't currently displayed the effective height was
            // zero. Mark the stereotype as displayed

            if (!(_stereo.isDisplayed())) {
                oldHeight = 0;
                _stereo.setDisplayed(true);
            }

            // Set the text and recalculate its bounds

            _stereo.setText(Notation.generateStereotype(this, stereo));
            _stereo.calcBounds();

            bounds.height += _stereo.getBounds().height - oldHeight;
        }

        // Set the bounds to our old bounds (reduced if we have taken the
        // stereotype away). If the bounds aren't big enough when we've added a
        // stereotype, they'll get increased as needed.

        setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
    }


    ///////////////////////////////////////////////////////////////////////////
    //
    // Fig accessors
    //
    ///////////////////////////////////////////////////////////////////////////


    public void setLineColor(Color col) { _cover.setLineColor(col); }
    public Color getLineColor() { return _cover.getLineColor(); }

    public void setFillColor(Color col) { _cover.setFillColor(col); }
    public Color getFillColor() { return _cover.getFillColor(); }

    public void setFilled(boolean f) { _cover.setFilled(f); }
    public boolean getFilled() { return _cover.getFilled(); }

    public void setLineWidth(int w) { _cover.setLineWidth(w); }
    public int getLineWidth() { return _cover.getLineWidth(); }


    /**
     * <p>Change the owning NSUML object for this Fig.</p>
     *
     * <p>Use the superclass method, but then bind our big port to the NSUML
     *  object, and advise that the model has changed.</p>
     *
     * @param  node  The NSUML object to own this fig.
     */

    public void setOwner(Object node) {
        super.setOwner(node);
        bindPort(node, _bigPort);
    }


    /**
     * <p>Work out the minimum size that this Fig can be.</p>
     *
     * <p>This should be the size of the stereotype + name + padding. However
     *   we allow for the possible case that the cover or big port could be
     *   bigger still.</p>
     *
     * @return  The minimum size of this fig.
     */

    public Dimension getMinimumSize() {
      
        Dimension bigPortMin = _bigPort.getMinimumSize();
        Dimension coverMin   = _cover.getMinimumSize();
        Dimension stereoMin  = _stereo.getMinimumSize();
        Dimension nameMin    = _name.getMinimumSize();

        Dimension newMin    = new Dimension(nameMin.width, nameMin.height);

        // Work out whether we need to count in the stereotype

        if (_stereo.isDisplayed()) {
            newMin.width   = Math.max(newMin.width, stereoMin.width);
            newMin.height += stereoMin.height;
        }

        // Maximum should allow for bigPort and cover.

        newMin.height = Math.max(bigPortMin.height,
                                 Math.max(coverMin.height,
                                          newMin.height + _PADDING * 2));

        newMin.width  = Math.max(bigPortMin.width,
                                 Math.max(coverMin.width,
                                          newMin.width + _PADDING * 2));

        return newMin;
    }


    /**
     * <p>Override setBounds to keep shapes looking right.</p>
     *
     * <p>Set the bounds of all components of the Fig. The stereotype (if any)
     *   and name are centred in the fig.</p>
     *
     * <p>We allow for the requested bounds being too small, and impose our
     *   minimum size if necessary.</p>
     *
     * @param x  X coordinate of upper left corner
     *
     * @param y  Y coordinate of upper left corner
     *
     * @param w  width of bounding box
     *
     * @param h  height of bounding box
     *
     * @author 10 Apr 2002. Jeremy Bennett (mail@jeremybennett.com). Patch to
     *         allow for stereotype as well.
     */

    public void setBounds(int x, int y, int w, int h) {

        // In the rather unlikely case that we have no name, we give up.

        if (_name == null) {
            return;
        }

        // Remember where we are at present, so we can tell GEF later. Then
        // check we are as big as the minimum size

        Rectangle oldBounds = getBounds();
        Dimension minSize   = getMinimumSize();

        int newW = (minSize.width  > w) ? minSize.width  : w;
        int newH = (minSize.height > h) ? minSize.height : h;

        Dimension stereoMin = _stereo.getMinimumSize();
        Dimension nameMin   = _name.getMinimumSize();

        // Work out the padding each side, depending on whether the stereotype
        // is displayed and set bounds accordingly

        if (_stereo.isDisplayed()) {
            int extra_each = (h - nameMin.height - stereoMin.height) / 2;

            _stereo.setBounds(x, y + extra_each, w, stereoMin.height);
            _name.setBounds(x, y + stereoMin.height + extra_each, w,
                            nameMin.height); 
        }
        else {
            int extra_each = (h - nameMin.height) / 2;

            _name.setBounds(x, y + extra_each, w, nameMin.height);
        }

        // Set the bounds of the bigPort and cover

        _bigPort.setBounds(x, y, newW, newH);
        _cover.setBounds(x, y, newW, newH);

        // Record the changes in the instance variables of our parent, tell GEF
        // and trigger the edges to reconsider themselves.

        _x = x;
        _y = y;
        _w = newW;
        _h = newH;
        
        firePropChange("bounds", oldBounds, getBounds());
        updateEdges();
    }

  
    ///////////////////////////////////////////////////////////////////////////
    //
    // event handlers
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>Called after text has been edited directly on the screen.</p>
     *
     * @param ft  The text that was edited.
     */

    protected void textEdited(FigText ft) throws PropertyVetoException {

        MClassifierRole cls = (MClassifierRole) getOwner();

        if (ft == _name) {
            String s = ft.getText();
	    try {
		ParserDisplay.SINGLETON.parseClassifierRole(cls, s);
		ProjectBrowser.TheInstance.getStatusBar().showStatus("");
	    } catch (ParseException pe) {
		ProjectBrowser.TheInstance.getStatusBar().showStatus("Error: " + pe + " at " + pe.getErrorOffset());
	    }
        }
    }


    /**
     * <p>Adjust the fig in the light of some change to the model.</p>
     *
     * <p><em>Note</em>. The current implementation does not properly use
     *   Notation.generate to generate the full name for a classifier role.</p>
     */

    protected void modelChanged() {

        // Let the superclass sort out any of its changes

        super.modelChanged();

        // Give up if we don't have an owner

        MClassifierRole cr = (MClassifierRole) getOwner();

        if (cr == null) {
            return;
        }

        // Note our current bounds

        Rectangle oldBounds = getBounds();

        // We only use the notation generator for the name itself. We ought to
        // do the whole thing.

        String nameStr    = Notation.generate(this, cr.getName()).trim();
        String baseString = "";

        // Loop through all base classes, building a comma separated list

        if (cr.getBases() != null && cr.getBases().size()>0) {
            Vector bases = new Vector(cr.getBases());
            baseString += ((MClassifier)bases.elementAt(0)).getName();

            for(int i=1; i<bases.size(); i++)
                baseString += ", "  +
                              ((MClassifier)bases.elementAt(i)).getName();
        }

        // Build the final string and set it as the name text.

        if (_readyToEdit) {
            if( nameStr.length() == 0 && baseString.length() == 0)
                _name.setText("");
            else
                _name.setText("/" + nameStr.trim() + " : " + baseString);
        }

        // Now recalculate all the bounds, using our old bounds.

	setBounds(oldBounds.x, oldBounds.y, oldBounds.width, oldBounds.height);
    }


} /* end class FigClassifierRole */
