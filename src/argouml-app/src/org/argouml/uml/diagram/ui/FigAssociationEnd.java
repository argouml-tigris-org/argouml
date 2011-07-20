/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mvw
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2005-2009 The Regents of the University of California. All
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
import java.util.HashSet;
import java.util.Set;

import org.argouml.notation.NotationProviderFactory2;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.FigText;

/**
 * Class to display graphics for N-ary association edges (association ends).<p>
 *
 * This class represents an association End Fig on a diagram, 
 * i.e. the line between the diamond and a node (like a class). <p>
 * 
 * The direction of the lines is from the diamond outwards,
 * hence the destination is the side of the classifier,
 * where the labels are shown. <p>
 * 
 * There is no support for arrows indicating navigability. <p>
 * 
 * Showing qualifiers or aggregation is not permitted 
 * according the UML 1.4.2 standard.
 *
 * @author pepargouml@yahoo.es
 */
public class FigAssociationEnd extends FigEdgeModelElement {

    /**
     * Group for the FigTexts concerning the association end.
     */
    private FigAssociationEndAnnotation destGroup;
    private FigMultiplicity destMult;

    /**
     * Construct Fig.
     * 
     * @param owner owning UML element (i.e. an AssociationEnd)
     * @param settings rendering settings
     */
    public FigAssociationEnd(Object owner, DiagramSettings settings) {
        super(owner, settings);

        destMult = new FigMultiplicity(owner, settings);
        addPathItem(destMult, 
                new PathItemPlacement(this, destMult, 100, -5, 45, 5));
        ArgoFigUtil.markPosition(this, 100, -5, 45, 5, Color.green);
        
        destGroup = new FigAssociationEndAnnotation(this, owner, settings);
        addPathItem(destGroup, 
                new PathItemPlacement(this, destGroup, 100, -5, -45, 5));
        ArgoFigUtil.markPosition(this, 100, -5, -45, 5, Color.blue);

        setBetweenNearestPoints(true);
        
        initializeNotationProvidersInternal(owner);
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#getNotationProviderType()
     */
    @Override
    protected int getNotationProviderType() {
        return NotationProviderFactory2.TYPE_ASSOCIATION_END_NAME;
    }

    @Override
    protected void initNotationProviders(Object own) {
        initializeNotationProvidersInternal(own);
    }

    private void initializeNotationProvidersInternal(Object own) {
        super.initNotationProviders(own);
        destMult.initNotationProviders();
        initNotationArguments();
    }

    protected void initNotationArguments() {
        /* Nothing yet. Later maybe something like: */
//        putNotationArgument("showAssociationName", 
//                getSettings().isShowAssociationNames());
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#updateListeners(java.lang.Object)
     */
    @Override
    public void updateListeners(Object oldOwner, Object newOwner) {
        Set<Object[]> listeners = new HashSet<Object[]>();
        if (newOwner != null) {
            listeners.add(new Object[] {newOwner, 
                new String[] {"isAbstract", "remove"}
            });
        }
        updateElementListeners(listeners);
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#textEdited(org.tigris.gef.presentation.FigText)
     */
    @Override
    protected void textEdited(FigText ft) {
        if (getOwner() == null) {
            return;
        }
        super.textEdited(ft);
        if (getOwner() == null) {
            return;
        }
        if (ft == destGroup.getRole()) {
            destGroup.getRole().textEdited();
        } else if (ft == destMult) {
            /* The text the user has filled in the textfield is first checked
             * to see if it's a valid multiplicity. If so then that is the 
             * multiplicity to be set. If not the input is rejected. */
            destMult.textEdited();
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#textEditStarted(org.tigris.gef.presentation.FigText)
     */
    @Override
    protected void textEditStarted(FigText ft) {
        if (ft == destGroup.getRole()) {
            destGroup.getRole().textEditStarted();
        } else if (ft == destMult) {
            destMult.textEditStarted();
        } else {
            super.textEditStarted(ft);
        }
    }

    @Override
    public void renderingChanged() {
        super.renderingChanged();
        // Fonts and colors should get updated automatically for contained figs
        destMult.renderingChanged();
        destGroup.renderingChanged();
        initNotationArguments();
    }

    @Override
    protected void updateStereotypeText() {
        /* There is none... */
    }

    /**
     * Updates the multiplicity field.
     */
    protected void updateMultiplicity() {
        if (getOwner() != null 
                && destMult.getOwner() != null) {
            destMult.setText();
        }
    }

    /* TODO: Support navigability. 
     * The code below causes and exception in FigAssociationEndAnnotation. */
//  @Override
//  public void paint(Graphics g) {
//      if (getOwner() == null ) {
//          LOG.error(
//              "Trying to paint a FigAssociationEnd without an owner. ");
//      } else {
//          applyArrowHeads(); 
//      }
//      if (getSourceArrowHead() != null) {
//          getSourceArrowHead().setLineColor(getLineColor());
//      }
//      super.paint(g);
//  }
    
//    /**
//     * Choose the arrowhead style for each end. <p>
//     * 
//     * TODO: This is called from paint(). Would it not better 
//     * be called from renderingChanged()?
//     */
//    protected void applyArrowHeads() {
//        int sourceArrowType = destGroup.getArrowType();
//
//        if (!getSettings().isShowBidirectionalArrows()
//                && sourceArrowType > 2) {
//            sourceArrowType -= 3;
//        }
//        
//        setSourceArrowHead(FigAssociationEndAnnotation
//                .ARROW_HEADS[sourceArrowType]);
//    }

}
