// $Id$
// Copyright (c) 2005-2006 The Regents of the University of California. All
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
import java.beans.PropertyChangeEvent;

import org.apache.log4j.Logger;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoHelpEvent;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.notation.providers.uml.NotationUtilityUml;
import org.argouml.ui.ProjectBrowser;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.PathConvPercentPlusConst;
import org.tigris.gef.presentation.FigText;

/**
 * Class to display graphics for N-ary association edges (association ends).<p>
 *
 * This class represents an association End Fig on a diagram, 
 * i.e. the line between the diamond and a node (like a class).
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
    private FigTextGroup srcGroup = new FigTextGroup();
    private FigText srcMult; 
    private FigText srcOrdering;
    
    private Logger LOG = Logger.getLogger(FigAssociationEnd.class);

    /**
     * The constructor.
     */
    public FigAssociationEnd() {
        super();

        srcMult = new FigText(10, 10, 90, 20);
        srcMult.setFont(getLabelFont());
        srcMult.setTextColor(Color.black);
        srcMult.setTextFilled(false);
        srcMult.setFilled(false);
        srcMult.setLineWidth(0);
        srcMult.setReturnAction(FigText.END_EDITING);
        srcMult.setJustification(FigText.JUSTIFY_CENTER);

        srcOrdering = new FigText(10, 10, 90, 20);
        srcOrdering.setFont(getLabelFont());
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
        // next line necessary for loading
        setLayer(ProjectManager.getManager().getCurrentProject()
                .getActiveDiagram().getLayer());
    }

    /**
     * The constructor.
     *
     * @param edge the UML object: association-end
     * @param lay the layer that contains this Fig
     */
    public FigAssociationEnd(Object edge, Layer lay) {
        this();
        setLayer(lay);
        setOwner(edge);
        if (Model.getFacade().isAAssociationEnd(edge)) {
            addElementListener(edge);
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#getNotationProviderType()
     */
    protected int getNotationProviderType() {
        return NotationProviderFactory2.TYPE_ASSOCIATION_END_NAME;
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#updateListeners(java.lang.Object)
     */
    public void updateListeners(Object oldOwner, Object newOwner) {
        if (oldOwner == newOwner) {
            LOG.warn("Listeners being added and removed from the same owner");
        }
        if (oldOwner != null) {
            removeAllElementListeners();
        }
        /* Now, let's register for events from all modelelements
         * that change the association-end representation: 
         */
        if (newOwner != null) {
            /* Many different event types are needed, 
             * so let's register for them all: */
            addElementListener(newOwner);
        }
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
    protected void textEdited(FigText ft) {
        if (getOwner() == null) {
            return;
        }
        super.textEdited(ft);
        if (ft == srcMult) {
            /* The text the user has filled in the textfield is first checked
             * to see if it's a valid multiplicity. If so then that is the 
             * multiplicity to be set. If not the input is rejected. */
            String msg =
                Translator.localize("statusmsg.bar.error.parsing.multiplicity");
            Object multi = null;
            try {
                multi = Model.getDataTypesFactory().createMultiplicity(
                        srcMult.getText());
                Model.getCoreHelper().setMultiplicity(getOwner(), multi);
            } catch (IllegalArgumentException e) {
                Object[] args = {e.getLocalizedMessage()};
                ArgoEventPump.fireEvent(new ArgoHelpEvent(
                        ArgoEventTypes.HELP_CHANGED, this,
                    Translator.messageFormat(msg, args)));
                srcMult.setText(NotationUtilityUml.generateMultiplicity(
                        Model.getFacade().getMultiplicity(getOwner())));
            }            
            
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#textEditStarted(org.tigris.gef.presentation.FigText)
     */
    protected void textEditStarted(FigText ft) {
        if (ft == srcMult) {
            showHelp("parsing.help.fig-association-source-multiplicity");
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

        Object multi = Model.getFacade().getMultiplicity(owner);
        multiToUpdate.setText(NotationUtilityUml.generateMultiplicity(multi));

        Object order = Model.getFacade().getOrdering(owner);
        orderingToUpdate.setText(getOrderingName(order));
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    protected void modelChanged(PropertyChangeEvent e) {
        super.modelChanged(e);
        if (e instanceof AttributeChangeEvent
                || e instanceof AssociationChangeEvent) {
            renderingChanged();
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#renderingChanged()
     */
    protected void renderingChanged() {
        updateEnd(srcMult, srcOrdering);
        srcMult.calcBounds();
        srcGroup.calcBounds();
        super.renderingChanged();
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#updateStereotypeText()
     */
    protected void updateStereotypeText() {
        /* There is none... */
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#paintClarifiers(java.awt.Graphics)
     */
    public void paintClarifiers(Graphics g) {
        indicateBounds(getNameFig(), g);
        indicateBounds(srcMult, g);
        super.paintClarifiers(g);
    }    
}  /* end class FigAssociationEnd */
