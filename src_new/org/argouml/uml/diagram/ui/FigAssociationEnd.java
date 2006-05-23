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
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.SwingUtilities;

import org.argouml.kernel.ProjectManager;
import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.argouml.notation.NotationProvider4;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.uml.notation.uml.NotationUtilityUml;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.PathConvPercentPlusConst;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigText;

/**
 * Class to display graphics for N-ary association edges (association ends).<p>
 *
 * This class represents an association End Fig on a diagram.
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
    private FigText srcMult, srcRole;
    private FigText srcOrdering;
    
    protected NotationProvider4 notationProviderSrcRole;

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

        srcRole = new FigText(10, 10, 90, 20);
        srcRole.setFont(getLabelFont());
        srcRole.setTextColor(Color.black);
        srcRole.setTextFilled(false);
        srcRole.setFilled(false);
        srcRole.setLineWidth(0);
        srcRole.setReturnAction(FigText.END_EDITING);
        srcRole.setJustification(FigText.JUSTIFY_CENTER);

        srcGroup.addFig(srcRole);
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

    /**
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#initNotationProviders(java.lang.Object)
     */
    protected void initNotationProviders(Object own) {
        if (Model.getFacade().isAAssociationEnd(own)) {
            notationProviderSrcRole =
                NotationProviderFactory2.getInstance().getNotationProvider(
                        NotationProviderFactory2.TYPE_ASSOCIATION_END_NAME, 
                        this, own);
        }
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#updateListeners(java.lang.Object)
     */
    public void updateListeners(Object newOwner) {
        Object oldOwner = getOwner();
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
            /* Now let's collect related elements: */
            ArrayList connections = new ArrayList();
            connections.addAll(Model.getFacade().getStereotypes(newOwner));
            for (Iterator i = connections.iterator(); i.hasNext();) {
                addElementListener(i.next());
            }
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

    /**
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#textEdited(org.tigris.gef.presentation.FigText)
     */
    protected void textEdited(FigText ft) {
        if (getOwner() == null) {
            return;
        }
        super.textEdited(ft);

        if (ft == srcRole) {
            ft.setText(notationProviderSrcRole.parse(ft.getText()));
        } else if (ft == srcMult) {
            Object multi =
                Model.getDataTypesFactory()
                    .createMultiplicity(srcMult.getText());
            Model.getCoreHelper().setMultiplicity(getOwner(), multi);
        }
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#textEditStarted(org.tigris.gef.presentation.FigText)
     */
    protected void textEditStarted(FigText ft) {
        if (ft == srcRole) {
            showHelp(notationProviderSrcRole.getParsingHelp());
        } else if (ft == srcMult) {
            showHelp("parsing.help.fig-association-source-multiplicity");
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

    /**
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    protected void modelChanged(PropertyChangeEvent e) {
        super.modelChanged(e);
        if (e instanceof AttributeChangeEvent
                || e instanceof AssociationChangeEvent) {
            renderingChanged();
            updateListeners(getOwner());
        }
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#renderingChanged()
     */
    public void renderingChanged() {
        updateEnd(srcMult, srcOrdering);
        if (notationProviderSrcRole != null) {
            srcRole.setText(notationProviderSrcRole.toString());
        }
        srcMult.calcBounds();
        srcGroup.calcBounds();
        super.renderingChanged();
        computeRoute();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#removeFromDiagram()
     */
    public void removeFromDiagram() {
        final Object association = Model.getFacade().getAssociation(getOwner());
        final Object owner = getOwner();
        final Layer layer = getLayer();
        super.removeFromDiagram();
        SwingUtilities.invokeLater(new Runnable() {
            public void run () {
                Fig associationFig = layer.presentationFor(association);
                try {
                    if (Model.getFacade().getClassifier(owner) != null
                            && associationFig instanceof FigNodeAssociation) {
                        ((FigNodeAssociation) associationFig).removeFromDiagram();
                    }
                } catch (InvalidElementException e) {
                    // if already deleted, just ignore
                }
            }
        });
    }

}  /* end class FigAssociationEnd */
