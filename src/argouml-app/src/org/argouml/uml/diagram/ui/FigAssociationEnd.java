// $Id$
// Copyright (c) 2005-2008 The Regents of the University of California. All
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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.util.HashSet;
import java.util.Set;

import org.argouml.kernel.Project;
import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.notation.NotationProvider;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.PathConvPercentPlusConst;
import org.tigris.gef.presentation.FigText;

/**
 * Class to display graphics for N-ary association edges (association ends).<p>
 *
 * This class represents an association End Fig on a diagram, 
 * i.e. the line between the diamond and a node (like a class). <p>
 * 
 * This class makes use of 2 NotationProviders: 
 * one for the association end name, 
 * and one for the multiplicity.
 *
 * @author pepargouml@yahoo.es
 */
public class FigAssociationEnd extends FigEdgeModelElement {

    /**
     * Serial version generation by Eclipse for rev. 1.18
     */
    private static final long serialVersionUID = -3029436535288973358L;
    /**
     * Group for the FigTexts concerning the association end.
     */
    private FigTextGroup srcGroup;
    private FigText srcMult; 
    private FigText srcOrdering;

    /**
     * The notation provider for the multiplicity.
     */
    private NotationProvider multiplicityNotationProvider;

    /**
     * The constructor.
     * @deprecated for 0.27.3 by tfmorris.  Use 
     * {@link #FigAssociationEnd(Object, DiagramSettings)}.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public FigAssociationEnd() {
        super();
        srcGroup = new FigTextGroup();
        srcMult = new FigText(X0, Y0, 90, 20);
        srcOrdering = new FigText(X0, Y0, 90, 20);        
        constructFigs();
    }

    private void constructFigs() {
        
        srcMult.setTextColor(Color.black);
        srcMult.setTextFilled(false);
        srcMult.setFilled(false);
        srcMult.setLineWidth(0);
        srcMult.setReturnAction(FigText.END_EDITING);
        srcMult.setJustification(FigText.JUSTIFY_CENTER);

        srcOrdering.setTextColor(Color.black);
        srcOrdering.setTextFilled(false);
        srcOrdering.setFilled(false);
        srcOrdering.setLineWidth(0);
        srcOrdering.setReturnAction(FigText.END_EDITING);
        srcOrdering.setJustification(FigText.JUSTIFY_CENTER);
        srcOrdering.setEditable(false);

        srcGroup.addFig(getNameFig());
        srcGroup.addFig(srcOrdering);

        addPathItem(srcMult, new PathConvPercentPlusConst(this, 100, -15, -15));
        addPathItem(srcGroup, new PathConvPercentPlusConst(this, 100, -40, 20));

        setBetweenNearestPoints(true);
    }

    /**
     * The constructor.
     *
     * @param edge the UML object: association-end
     * @param lay the layer that contains this Fig
      * @deprecated for 0.27.3 by tfmorris.  Use 
     * {@link #FigAssociationEnd(Object, DiagramSettings)}.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public FigAssociationEnd(Object edge, Layer lay) {
        this();
        setLayer(lay);
        setOwner(edge);
        if (Model.getFacade().isAAssociationEnd(edge)) {
            addElementListener(edge);
        }
    }
    
    /**
     * Construct Fig.
     * 
     * @param edge owning UML element
     * @param settings rendering settings
     */
    public FigAssociationEnd(Object edge, DiagramSettings settings) {
        super(edge, settings);
        srcGroup = new FigTextGroup(settings);
        srcMult = new ArgoFigText(edge, new Rectangle(X0, Y0, 90, 20), 
                settings, false);
        srcOrdering = new ArgoFigText(edge, new Rectangle(X0, Y0, 90, 20), 
                settings, false);        
        constructFigs();    
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
        if (multiplicityNotationProvider != null) {
            multiplicityNotationProvider.cleanListener(this, own);
        }
        super.initNotationProviders(own);
        if (Model.getFacade().isAAssociationEnd(own)) {
            multiplicityNotationProvider =
                NotationProviderFactory2.getInstance().getNotationProvider(
                        NotationProviderFactory2.TYPE_MULTIPLICITY, own, this);
            Project p = getProject();
            if (p != null) {
                putNotationArgument("singularMultiplicityVisible", 
                        getSettings().isShowSingularMultiplicities());
                putNotationArgument("useGuillemets", 
                        getSettings().isUseGuillemets());
            }
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#updateListeners(java.lang.Object)
     */
    @Override
    public void updateListeners(Object oldOwner, Object newOwner) {
        Set<Object[]> l = new HashSet<Object[]>();
        if (newOwner != null) {
            l.add(new Object[] {newOwner, null});
        }
        updateElementListeners(l);
    }

    /** Returns the name of the OrderingKind.
     * @return "{ordered}", "{sorted}" or "" if null or "unordered"
     */
    private String getOrderingName(Object orderingKind) {
        if (orderingKind == null) {
            return "";
        }
        if (Model.getFacade().getName(orderingKind) == null) {
            return "";
        }
        if ("".equals(Model.getFacade().getName(orderingKind))) {
            return "";
        }
        if ("unordered".equals(Model.getFacade().getName(orderingKind))) {
            return "";
        }

        return "{" + Model.getFacade().getName(orderingKind) + "}";
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
        if (ft == srcMult) {
            /* The text the user has filled in the textfield is first checked
             * to see if it's a valid multiplicity. If so then that is the 
             * multiplicity to be set. If not the input is rejected. */
            multiplicityNotationProvider.parse(getOwner(), ft.getText());
            ft.setText(multiplicityNotationProvider.toString(getOwner(), 
                    getNotationArguments()));
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#textEditStarted(org.tigris.gef.presentation.FigText)
     */
    @Override
    protected void textEditStarted(FigText ft) {
        if (ft == srcMult) {
            showHelp(multiplicityNotationProvider.getParsingHelp());
        } else {
            super.textEditStarted(ft);
        }
    }

    private void updateEnd(FigText multiToUpdate, 
                           FigText orderingToUpdate) {

        Object owner = getOwner();
        if (!Model.getFacade().isAAssociationEnd(owner)) {
            throw new IllegalArgumentException();
        }

        if (multiplicityNotationProvider != null) {
            multiToUpdate.setText(
                    multiplicityNotationProvider.toString(getOwner(), 
                            getNotationArguments()));
        }

        Object order = Model.getFacade().getOrdering(owner);
        orderingToUpdate.setText(getOrderingName(order));
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    @Override
    protected void modelChanged(PropertyChangeEvent e) {
        super.modelChanged(e);
        if (e instanceof AttributeChangeEvent
                || e instanceof AssociationChangeEvent) {
            renderingChanged();
            if (multiplicityNotationProvider != null) {
                multiplicityNotationProvider.updateListener(this, 
                        getOwner(), e);
            }
        }
    }


    @Override
    public void renderingChanged() {
        super.renderingChanged();
        
        // Fonts and colors should get updated automatically for contained figs
        
        // Do we really need to update the text?  Why should it have changed?
        updateEnd(srcMult, srcOrdering);
        
        srcMult.calcBounds();
        srcGroup.calcBounds();
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#updateStereotypeText()
     */
    @Override
    protected void updateStereotypeText() {
        /* There is none... */
    }

    @Override
    protected void removeFromDiagramImpl() {
        multiplicityNotationProvider.cleanListener(this, getOwner());
        super.removeFromDiagramImpl();
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#paintClarifiers(java.awt.Graphics)
     */
    @Override
    public void paintClarifiers(Graphics g) {
        indicateBounds(getNameFig(), g);
        indicateBounds(srcMult, g);
        super.paintClarifiers(g);
    }    
}
