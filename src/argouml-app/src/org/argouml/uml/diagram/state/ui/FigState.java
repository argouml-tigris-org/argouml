/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michiel van der Wulp
 *    Bob Tarling
 *    Tom Morris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2009 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.uml.diagram.state.ui;

import java.awt.Font;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;

import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.notation.Notation;
import org.argouml.notation.NotationName;
import org.argouml.notation.NotationProvider;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.notation.NotationSettings;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigRRect;
import org.tigris.gef.presentation.FigText;

/**
 * Abstract class with common functionality for all Figs representing a UML
 * State element.
 *
 * @author jaap.branderhorst@xs4all.nl
 * @since Dec 30, 2002
 */
public abstract class FigState extends FigStateVertex {

    protected static final int SPACE_TOP = 0;
    protected static final int SPACE_MIDDLE = 0;
    protected static final int DIVIDER_Y = 0;
    protected static final int SPACE_BOTTOM = 6;

    protected static final int MARGIN = 2;

    protected NotationProvider notationProviderBody;

    /**
     * The text inside the state.
     */
    private FigText internal;

    /**
     * Constructor used by PGML parser.
     * 
     * @param owner the owning UML element
     * @param bounds rectangle describing bounds
     * @param settings rendering settings
     */
    public FigState(Object owner, Rectangle bounds, DiagramSettings settings) {
        super(owner, bounds, settings);

        initializeState();

        NotationName notation = Notation.findNotation(
                getNotationSettings().getNotationLanguage());
        notationProviderBody =
            NotationProviderFactory2.getInstance().getNotationProvider(
                    NotationProviderFactory2.TYPE_STATEBODY, getOwner(), this, 
                    notation);
    }

    @Override
    protected Fig createBigPortFig() {
        return new FigRRect(getInitialX() + 1, getInitialY() + 1,
                getInitialWidth() - 2, getInitialHeight() - 2,
                DEBUG_COLOR, DEBUG_COLOR);
    }

    private void initializeState() {
        // TODO: Get rid of magic numbers!  Figure out which represent line
        // widths vs padding vs offsets
        getNameFig().setLineWidth(0);
        getNameFig().setBounds(getInitialX() + 2, getInitialY() + 2,
                       getInitialWidth() - 4,
                       getNameFig().getBounds().height);
        getNameFig().setFilled(false);

        internal =
            new FigText(getInitialX() + 2,
                    getInitialY() + 2 + NAME_FIG_HEIGHT + 4,
                    getInitialWidth() - 4,
                    getInitialHeight() 
                    - (getInitialY() + 2 + NAME_FIG_HEIGHT + 4));
        internal.setFont(getSettings().getFont(Font.PLAIN));
        internal.setTextColor(TEXT_COLOR);
        internal.setLineWidth(0);
        internal.setFilled(false);
        internal.setExpandOnly(true);
        internal.setReturnAction(FigText.INSERT);
        internal.setJustification(FigText.JUSTIFY_LEFT);
    }

    /*
     * @see org.argouml.uml.diagram.state.ui.FigStateVertex#initNotationProviders(java.lang.Object)
     */
    @Override
    protected void initNotationProviders(Object own) {
        if (notationProviderBody != null) {
            notationProviderBody.cleanListener();
        }
        super.initNotationProviders(own);
        NotationName notation = Notation.findNotation(
                getNotationSettings().getNotationLanguage());
        if (Model.getFacade().isAState(own)) {
            notationProviderBody =
                NotationProviderFactory2.getInstance().getNotationProvider(
                        NotationProviderFactory2.TYPE_STATEBODY, own, this, 
                        notation);
        }
    }

    @Override
    protected void modelChanged(PropertyChangeEvent mee) {
        super.modelChanged(mee);
        // TODO: Do we really need to be listening for both of these events?
        if (mee instanceof AssociationChangeEvent 
                || mee instanceof AttributeChangeEvent) {
            
            // TODO: We definitely don't want to react to addition and
            // removal of transitions. Can't we be more specific when
            // we register ourselves as a listener.
            if ((mee.getPropertyName().equals("incoming")
                        || mee.getPropertyName().equals("outgoing"))) {
                return;
            }
            
            renderingChanged();
            damage();
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#removeFromDiagramImpl()
     */
    @Override
    public void removeFromDiagramImpl() {
        if (notationProviderBody != null) {
            notationProviderBody.cleanListener();
        }
        super.removeFromDiagramImpl();
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#renderingChanged()
     */
    @Override
    public void renderingChanged() {
        super.renderingChanged();
        Object state = getOwner();
        // Entered in 0.29.3
        // Prove that the test below is not needed
        assert state != null;
        if (state == null) {
            return;
        }
        if (notationProviderBody != null) {
            internal.setText(notationProviderBody.toString(getOwner(), 
                    getNotationSettings()));
        }
        calcBounds();
        setBounds(getBounds());
    }

    /**
     * @return the initial X
     */
    protected abstract int getInitialX();

    /**
     * @return the initial Y
     */
    protected abstract int getInitialY();

    /**
     * @return the initial width
     */
    protected abstract int getInitialWidth();

    /**
     * @return the initial height
     */
    protected abstract int getInitialHeight();

    /**
     * @param theInternal The internal to set.
     */
    protected void setInternal(FigText theInternal) {
        this.internal = theInternal;
    }

    /**
     * @return Returns the internal.
     */
    protected FigText getInternal() {
        return internal;
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#textEditStarted(org.tigris.gef.presentation.FigText)
     */
    @Override
    protected void textEditStarted(FigText ft) {
        super.textEditStarted(ft);
        if (ft == internal) {
            showHelp(notationProviderBody.getParsingHelp());
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#textEdited(org.tigris.gef.presentation.FigText)
     */
    @Override
    public void textEdited(FigText ft) throws PropertyVetoException {
        super.textEdited(ft);
        if (ft == getInternal()) {
            Object st = getOwner();
            if (st == null) {
                return;
            }
            notationProviderBody.parse(getOwner(), ft.getText());
            ft.setText(notationProviderBody.toString(getOwner(), 
                    getNotationSettings()));
        }
    }

    /**
     * 
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateFont()
     */
    @Override
    protected void updateFont() {
        super.updateFont();
        Font f = getSettings().getFont(Font.PLAIN);
        internal.setFont(f);
    }

    public void notationRenderingChanged(NotationProvider np, String rendering) {
        super.notationRenderingChanged(np, rendering);
        if (notationProviderBody == np) {
            internal.setText(rendering);
            updateBounds();
            damage();
        }
    }

    public NotationSettings getNotationSettings(NotationProvider np) {
        // both have the same settings
        return getNotationSettings();
    }

    public Object getOwner(NotationProvider np) {
        // both have the same owner
        return getOwner();
    }

}
