// Copyright (c) 1996-2001 The Regents of the University of California. All
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

// File: CompartmentFigText.java
// Classes: CompartmentFigText
// Original Author: thn

// 8 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Extended to support
// the display of extension points within use cases.


package org.argouml.uml.diagram.ui;

import java.awt.*;
import java.awt.event.*;

import org.argouml.ui.*;
import org.tigris.gef.presentation.*;
import ru.novosoft.uml.foundation.core.*;

/**
 * <p>A FigText class extension for FigClass/FigInterface/FigUseCase
 *   compartments.</p>
 *
 * <p>This implementation now supports the extension point compartment in a use
 *   case. The {@link #getFeature()} and {@link #setFeature(MFeature)} methods
 *   are now deprecated in favour of the more generic {@link
 *   #getModelElement()} and {@link #setModelElement(MModelElement)}
 *   methods.</p>
 */

public class CompartmentFigText extends FigText
{

    ///////////////////////////////////////////////////////////////////////////
    //
    // Instance variables
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>The bounding figure of the compartment containing this fig text.</p>
     */

    protected Fig           _refFig;


    /**
     * <p>Record whether we are currently highlighted.</p>
     */

    protected boolean       _isHighlighted = false;


    /**
     * <p>The model element with which we are associated.</p>
     */

    protected MModelElement _modelElement = null;


    ///////////////////////////////////////////////////////////////////////////
    //
    // constructors
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>Build a new compartment figText of the given dimensions, within the
     *   compartment described by <code>aFig</code>.</p>
     *
     * <p>Invoke the parent constructor, then set the reference to the
     *   associated compartment figure. The associated FigText is marked as
     *   expand only.</p>
     *
     * <p><em>Warning</em>. Won't work properly if <code>aFig</code> is
     *   null. A warning is printed.</p>
     *
     * @param x     X coordinate of the top left of the FigText.
     *
     * @param y     Y coordinate of the top left of the FigText.
     *
     * @param w     Width of the FigText.
     *
     * @param h     Height of the FigText.
     *
     * @param aFig  The figure describing the whole compartment
     */

    public CompartmentFigText(int x, int y, int w, int h, Fig aFig) {
        super(x,y,w,h,true);

        // Set the enclosing compartment fig. Warn if its null (which will
        // break).

        _refFig = aFig;

        if (_refFig == null) {
            System.out.println(this.getClass().toString() +
                               ": Cannot create with null compartment fig");
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    //
    // Accessors
    //
    ///////////////////////////////////////////////////////////////////////////


    // The following method overrides are necessary for proper graphical
    // behavior

    /**
     * <p>Override for correct graphical behaviour.</p>
     *
     * @param w  Desired line width. Overridden and set to zero anyway.
     */

    public void setLineWidth(int w) {
        super.setLineWidth(0);
    }


    /**
     * <p>Override for correct graphical behaviour.</p>
     *
     * @return  Current line width&mdash;always 1.
     */

    public int getLineWidth() {
        return 1;
    }

    
    /**
     * <p>Override for correct graphical behaviour.</p>
     *
     * @return  Current fill status&mdash;always <code>true</code>.
     */

    public boolean getFilled() {
        return true;
    }


    /**
     * <p>Override for correct graphical behaviour.</p>
     *
     * @return  Current fill colour&mdash;always the fill colour of the
     *          associated compartment fig.
     */

    public Color getFillColor() {
        return _refFig.getFillColor();
    }


    /**
     * <p>Override for correct graphical behaviour.</p>
     *
     * @return  Current fill colour&mdash;always the fill colour of the
     *          associated compartment fig.
     */

    public Color getLineColor() {
        return _refFig.getLineColor();
    }


    /**
     * <p>Mark whether this item is to be highlighted.</p>
     *
     * <p>If it is highlighted, make the superclass line width 1 rather than 0
     *   and set the associated component fig as the target in the browser.</p>
     *
     * @param flag  <code>true</code> if the entry is to be highlighted,
     *              <code>false</code> otherwise.
     */

    public void setHighlighted(boolean flag) {
        _isHighlighted = flag;
        super.setLineWidth(_isHighlighted ? 1 : 0);

        if (flag && (_modelElement != null)) {
	    ProjectBrowser.TheInstance.setTarget(_modelElement);
        }
    }


    /**
     * <p>Return whether this item is highlighted.</p>
     *
     * @return  <code>true</code> if the entry is highlighted,
     *          <code>false</code> otherwise.
     */

    public boolean isHighlighted() {
        return _isHighlighted;
    }


    /**
     * <p>Set the NSUML feature associated with this compartment.</p>
     *
     * <p><em>Note</em>. This is implemented using {@link
     *   #setModelElement(MModelElement)}.</p>
     *
     * @param feature  The feature to set.
     *
     * @deprecated  Use the more general {@link
     *              #setModelElement(MModelElement)} instead.
     */

    public void setFeature(MFeature feature) {
        setOwner(feature);
    }


    /**
     * <p>Get the NSUML feature associated with this compartment.</p>
     *
     * <p><em>Note</em>. This is implemented using {@link #getModelElement()}
     *   and will return <code>null</code> if that does not return an instance
     *   of {@link MFeature}.</p>
     *
     * @return  The feature associated with this compartment or 
     * 		null if none can be found.
     *
     * @deprecated  Use the more general {@link #getOwner()} instead.
     */

    public MFeature getFeature() {
        MModelElement modelElement = (MModelElement)getOwner();

        if (modelElement instanceof MFeature) {
            return (MFeature) getOwner();
        }
        else {
            return null;
        }
    }


    /**
     * <p>Set the NSUML model element associated with this compartment.</p>
     *
     * @param modelElement  The model element to set.
     * 
     * @deprecated use the more general setOwner
     */

    public void setModelElement(MModelElement modelElement) {

        setOwner(modelElement);
    }


    /**
     * <p>Get the NSUML modelElement associated with this compartment.</p>
     *
     * @return  The modelElement associated with this compartment.
     * 
     * @deprecated use getOwner()
     */

    public MModelElement getModelElement() {

        return (MModelElement)getOwner();
    }

} /* End of class CompartmentFigText */

