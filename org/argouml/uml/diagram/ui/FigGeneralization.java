

// $Id$
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

// File: FigGeneralization.java
// Classes: FigGeneralization
// Original Author: abonner@ics.uci.edu
// Author discriminator: jaap.branderhorst@xs4all.nl
// $Id$

package org.argouml.uml.diagram.ui;

import java.awt.Color;
import java.awt.Graphics;

import org.argouml.kernel.ProjectManager;
import org.argouml.model.uml.foundation.core.CoreFactory;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.PathConvPercent;
import org.tigris.gef.presentation.ArrowHeadTriangle;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigText;

import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.foundation.core.MGeneralizableElement;
import ru.novosoft.uml.foundation.core.MGeneralization;

public class FigGeneralization extends FigEdgeModelElement {
	
    /**
     * Text box for discriminator
     */
    FigText _discriminator = new FigText(10, 30, 90, 20);

    ////////////////////////////////////////////////////////////////
    // constructors

    protected ArrowHeadTriangle endArrow;

    public FigGeneralization() {
	addPathItem(_stereo, new PathConvPercent(this, 50, 10));
	endArrow = new ArrowHeadTriangle();

	_discriminator.setFont(LABEL_FONT);
	_discriminator.setTextColor(Color.black);
	_discriminator.setTextFilled(false);
	_discriminator.setFilled(false);
	_discriminator.setLineWidth(0);
	_discriminator.setExpandOnly(false);
	_discriminator.setMultiLine(false);
	_discriminator.setAllowsTab(false);
	addPathItem(_discriminator, new PathConvPercent(this, 40, -10));
	endArrow.setFillColor(Color.white);
	setDestArrowHead(endArrow);
	setBetweenNearestPoints(true);
    
	if (getLayer() == null) {
	    setLayer(ProjectManager.getManager().getCurrentProject().getActiveDiagram().getLayer());
	}
    
    }

    public FigGeneralization(Object edge, Layer lay) {
	this();
	setLayer(lay);
	setOwner(edge);
    
    }
  
    public FigGeneralization(Object edge) {
  	this();
  	setOwner(edge);
    }

    protected boolean canEdit(Fig f) { return false; }

    ////////////////////////////////////////////////////////////////
    // event handlers

    /** This is called aftern any part of the UML MModelElement has
     *  changed. This method automatically updates the name FigText.
     *  Subclasses should override and update other parts. */
    protected void modelChanged(MElementEvent e) {
	// do not set _name
	updateStereotypeText();
	updateDiscriminatorText();
    }
  
    /**
     * Updates the discriminator text. Called if the model is changed
     * and on construction time.
     */
    public void updateDiscriminatorText() {
  	MGeneralization me = (MGeneralization) getOwner();
  	if (me == null) {
	    return;
  	}
  	String disc = me.getDiscriminator();
  	if (disc == null) {
	    disc = "";
  	}
  	_discriminator.setText(disc);
    }

    public void paint(Graphics g) {
        endArrow.setLineColor(getLineColor());
        super.paint(g);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setOwner(Object)
     */
    public void setOwner(Object own) {
	super.setOwner(own);
	if (org.argouml.model.ModelFacade.isAGeneralization(own)) {
	    MGeneralization gen = (MGeneralization) own;
	    MGeneralizableElement subType = gen.getChild();
	    MGeneralizableElement superType = gen.getParent();
	    // due to errors in earlier releases of argouml it can
	    // happen that there is a generalization without a child
	    // or parent.
	    if (subType == null || superType == null) {
		delete();
		return;
	    }
	    FigNode subTypeFN = (FigNode) getLayer().presentationFor(subType);
	    FigNode superTypeFN =
		(FigNode) getLayer().presentationFor(superType);
	    setSourcePortFig(subTypeFN);
	    setSourceFigNode(subTypeFN);
	    setDestPortFig(superTypeFN);
	    setDestFigNode(superTypeFN);
      
	} else 
	    if (own != null) {
		throw new IllegalStateException("FigGeneralization has an illegal owner");
	    }
    }

    /**
     * This method is called after the fig is loaded from pgml. Implemented here
     * to fix errors with the model concerning the fig not having an owner.
     * @see org.tigris.gef.presentation.Fig#postLoad()
     */
    public void postLoad() {
        super.postLoad();
        Object own = getOwner();
        if (own == null) {
            fixModel();
        }
    }

    private void fixModel() {
        Fig sourceFig = getSourceFigNode();
        Fig destFig = getDestFigNode();
        Object source = sourceFig.getOwner();
        Object dest = destFig.getOwner();
        if (org.argouml.model.ModelFacade.isAGeneralizableElement(source)
	    && org.argouml.model.ModelFacade.isAGeneralizableElement(dest))
	{
            setOwner(CoreFactory.getFactory().buildGeneralization((MGeneralizableElement) source,
								  (MGeneralizableElement) dest));
        }
    }
} /* end class FigGeneralization */