/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.model.AddAssociationEvent;
import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.ui.ArgoJMenu;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.DiagramEdgeSettings;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.ArrowHead;
import org.tigris.gef.presentation.ArrowHeadComposite;
import org.tigris.gef.presentation.ArrowHeadDiamond;
import org.tigris.gef.presentation.ArrowHeadGreater;
import org.tigris.gef.presentation.ArrowHeadNone;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigText;


/**
 * This class represents the Fig of a binary association on a diagram.
 *
 */
public class FigAssociation extends FigEdgeModelElement {

    private static final Logger LOG =
        Logger.getLogger(FigAssociation.class.getName());

    /**
     * Group for the FigTexts concerning the source association end.
     */
    private EndDecoration srcEnd;

    /**
     * Group for the FigTexts concerning the dest association end.
     */
    private EndDecoration destEnd;

    /**
     * Group for the FigTexts concerning the name and stereotype of the
     * association itself.
     */
    private FigTextGroup middleGroup;

    /**
     * Constructor used by PGML parser.
     *
     * @param diagramEdgeSettings the destination uml association-end element
     * @param settings rendering settings
     */
    public FigAssociation(
            final DiagramEdgeSettings diagramEdgeSettings,
            final DiagramSettings settings) {
        super(diagramEdgeSettings.getOwner(), settings);

        createNameLabel(getOwner(), settings);

        Object sourceAssociationEnd =
            diagramEdgeSettings.getSourceConnector();
        Object destAssociationEnd =
            diagramEdgeSettings.getDestinationConnector();
        if (sourceAssociationEnd == null || destAssociationEnd == null) {
            // If we have no source and dest connector then we assume this is
            // load of an old UML1.4 diagram from before this data was saved
            // in PGML. For UML1.4 we can assume the source is first connection
            // and destination is last connection stored in repository for this
            // association.
            Iterator it =
                Model.getFacade().getConnections(getOwner()).iterator();

            sourceAssociationEnd = it.next();
            destAssociationEnd = it.next();
        }

        createEndFigs(
                sourceAssociationEnd,
                destAssociationEnd,
                settings, 45);

        setBetweenNearestPoints(true);

        initializeNotationProvidersInternal(getOwner());

        if (Model.getFacade().getUmlVersion().charAt(0) == '2') {
            Model.getPump().addModelEventListener(this, getOwner(),
                    new String[] {"navigableOwnedEnd"});
        }
    }

    protected void removeFromDiagramImpl() {
        if (Model.getFacade().getUmlVersion().charAt(0) == '2') {
            Model.getPump().removeModelEventListener(this,
                    getOwner(),
                    new String[] {"navigableOwnedEnd"});
        }
        super.removeFromDiagramImpl();
    }

    public void propertyChange(PropertyChangeEvent pce) {
        if (Model.getFacade().getUmlVersion().charAt(0) == '2'
                && pce instanceof AssociationChangeEvent
                && pce.getPropertyName().equals("navigableOwnedEnd")) {
            applyArrowHeads();
            damage();
        }
        super.propertyChange(pce);
    }


    /**
     * Called by the constructor to create the Figs at each end
     * of the association.
     * TODO: This is temporary during refactoring process. We should
     * override setDestFigNode and setSourceFigNode and create the ends there.
     * That will allow the same pattern to work for UML2 where we cannot assume
     * the connection order.
     *
     * @param sourceAssociationEnd
     * @param destAssociationEnd
     * @param settings
     */
    private void createEndFigs(
            final Object sourceAssociationEnd,
            final Object destAssociationEnd,
            final DiagramSettings settings,
            final int displacementAngle) {
        srcEnd = createEnd(
                sourceAssociationEnd,
                settings, 0, 5, 180 - displacementAngle, 5);
        destEnd = createEnd(
                destAssociationEnd,
                settings, 100, -5, displacementAngle, 5);
    }

    private EndDecoration createEnd(
            final Object endOwner,
            final DiagramSettings settings,
            final int percentPostionOnLine,
            final int pathDelta,
            final int displacementAngle,
            final int displacementDistance) {
        return new EndDecoration(endOwner, settings,
                percentPostionOnLine,
                pathDelta,
                displacementAngle,
                displacementDistance);
    }

    /**
     * Create the main draggable label for the association.
     * This can be overridden in subclasses to change behaviour.
     * TODO: Consider introducing this to FigEdgeModelElement and
     * using throughout all edges.
     *
     * @param owner owning uml element
     * @param settings rendering settings
     */
    protected void createNameLabel(Object owner, DiagramSettings settings) {
        middleGroup = new FigTextGroup(owner, settings);

        // let's use groups to construct the different text sections at
        // the association
        if (getNameFig() != null) {
            middleGroup.addFig(getNameFig());
        }
        middleGroup.addFig(getStereotypeFig());
        addPathItem(middleGroup,
                new PathItemPlacement(this, middleGroup, 50, 25));
        ArgoFigUtil.markPosition(this, 50, 0, 90, 25, Color.yellow);
    }

    @Override
    public void renderingChanged() {
        super.renderingChanged();
        /* This fixes issue 4987: */
        srcEnd.renderingChanged();
        destEnd.renderingChanged();
        if (middleGroup != null) {
            middleGroup.renderingChanged();
        }
    }


    @Override
    protected void initNotationProviders(Object own) {
        initializeNotationProvidersInternal(own);
    }

    private void initializeNotationProvidersInternal(Object own) {
        super.initNotationProviders(own);
        srcEnd.initNotationProviders();
        destEnd.initNotationProviders();
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#updateListeners(java.lang.Object, java.lang.Object)
     */
    @Override
    public void updateListeners(Object oldOwner, Object newOwner) {
        Set<Object[]> listeners = new HashSet<Object[]>();
        if (newOwner != null) {
            listeners.add(
                    new Object[] {newOwner,
                                  new String[] {"isAbstract", "remove"}
                    });
        }
        updateElementListeners(listeners);
        /* No further listeners required in this case - the rest is handled
         * by the notationProvider and sub-Figs. */
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#getNotationProviderType()
     */
    @Override
    protected int getNotationProviderType() {
        return NotationProviderFactory2.TYPE_ASSOCIATION_NAME;
    }

    /**
     * Get the source classifier that the association was drawn from.
     * Note that source and destination are not necessarily meaningful
     * regarding associations. Although the edge may originally have been
     * drawn by the user in a certain direction it in no way indicates the
     * direction of the association.
     * @return the classifier at the source end of the association or null
     * if the association is not yet attached (TODO can we ensure that this is
     * never null?).
     */
    protected Object getSource() {
        if (srcEnd == null) {
            return null;
        }
        return Model.getFacade().getClassifier(srcEnd.getOwner());
    }

    /**
     * Get the destination classifier that the association was drawn from.
     * Note that source and destination are not necessarily meaningful
     * regarding associations. Although the edge may originally have been
     * drawn by the user in a certain direction it in no way indicates the
     * direction of the association.
     * @return the classifier at the destination end of the association or null
     * if the association is not yet attached (TODO can we ensure that this is
     * never null?).
     */
    protected Object getDestination() {
        if (destEnd == null) {
            return null;
        }
        return Model.getFacade().getClassifier(destEnd.getOwner());
    }

    /**
     * Get the model element at the source end of the edge. This is not the
     * same as the owner of the node at the source end, rather it is the
     * element that connects the element of the edge to the element of the
     * node.
     * Mostly this returns null as the edge connects directly to the node but
     * implementations such as the Fig for association will return an
     * association end that connects the association to the classifier.
     * @return the model element that connects the edge to the node (or null
     * if the edge requires no such connector.
     */
    public Object getSourceConnector() {
        if (srcEnd == null) {
            return null;
        }
        return srcEnd.getOwner();
    }

    /**
     * Get the model element at the destination end of the edge. This is not
     * the same as the owner of the node at the source end, rather it is the
     * element that connects the element of the edge to the element of the
     * node.
     * Mostly this returns null as the edge connects directly to the node but
     * implementations such as the Fig for association will return an
     * association end that connects the association to the classifier.
     * @return the model element that connects the edge to the node (or null
     * if the edge requires no such connector.
     */
    public Object getDestinationConnector() {
        if (destEnd == null) {
            return null;
        }
        return destEnd.getOwner();
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

        Collection conn = Model.getFacade().getConnections(getOwner());
        if (conn == null || conn.size() == 0) {
            return;
        }

	if (ft == srcEnd.getRole()) {
	    srcEnd.getRole().textEdited();
	} else if (ft == destEnd.getRole()) {
	    destEnd.getRole().textEdited();
	} else if (ft == srcEnd.getMult()) {
            srcEnd.getMult().textEdited();
	} else if (ft == destEnd.getMult()) {
            destEnd.getMult().textEdited();
	}
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#textEditStarted(org.tigris.gef.presentation.FigText)
     */
    @Override
    protected void textEditStarted(FigText ft) {
        if (ft == srcEnd.getRole()) {
            srcEnd.getRole().textEditStarted();
        } else if (ft == destEnd.getRole()) {
            destEnd.getRole().textEditStarted();
        } else if (ft == srcEnd.getMult()) {
            srcEnd.getMult().textEditStarted();
        } else if (ft == destEnd.getMult()) {
            destEnd.getMult().textEditStarted();
        } else {
            super.textEditStarted(ft);
        }
    }

    /**
     * Choose the arrowhead style for each end. <p>
     *
     * TODO: This is called from paint(). Would it not better
     * be called from renderingChanged()?
     */
    protected void applyArrowHeads() {
        if (srcEnd == null || destEnd == null) {
            /* This only happens if model-change events arrive
             * before we are completely constructed. */
            return;
        }
        int sourceArrowType = srcEnd.getArrowType();
        int destArrowType = destEnd.getArrowType();

        if (!getSettings().isShowBidirectionalArrows()
                && sourceArrowType > 2
                && destArrowType > 2) {
            sourceArrowType -= 3;
            destArrowType -= 3;
        }

        setSourceArrowHead(FigAssociationEndAnnotation
                .ARROW_HEADS[sourceArrowType]);
        setDestArrowHead(FigAssociationEndAnnotation
                .ARROW_HEADS[destArrowType]);
    }

    /*
     * @see org.tigris.gef.ui.PopupGenerator#getPopUpActions(java.awt.event.MouseEvent)
     */
    @Override
    public Vector getPopUpActions(MouseEvent me) {
	Vector popUpActions = super.getPopUpActions(me);
        if (TargetManager.getInstance().getTargets().size() > 1) {
            return popUpActions;
        }

	if (isPointCloseToEdgeEnd(me.getPoint())) {
            buildMultiplicityMenu(popUpActions);
	}

	Object association = getOwner();
        Collection ascEnds = Model.getFacade().getConnections(association);
        Iterator iter = ascEnds.iterator();
        Object ascStart = iter.next();
        Object ascEnd = iter.next();

        buildNavigationMenu(popUpActions, ascStart, ascEnd);
        buildAggregationMenu(popUpActions, ascStart, ascEnd);

	return popUpActions;
    }

    private boolean isPointCloseToEdgeEnd(Point p) {
        // x^2 + y^2 = r^2  (equation of a circle)
        Point firstPoint = this.getFirstPoint();
        Point lastPoint = this.getLastPoint();
        int length = getPerimeterLength();

        int rSquared = (int) (.3 * length);

        // max distance is set at 100 pixels, (rSquared = 100^2)
        if (rSquared > 100) {
            rSquared = 10000;
        } else {
            rSquared *= rSquared;
        }

        int srcDeterminingFactor =
            getSquaredDistance(p, firstPoint);
        int destDeterminingFactor =
            getSquaredDistance(p, lastPoint);

        return destDeterminingFactor < rSquared
                || (srcDeterminingFactor < rSquared
                        && srcDeterminingFactor < destDeterminingFactor);
    }

    private void buildMultiplicityMenu(
            final Vector popUpActions) {
        ArgoJMenu menu =
            new ArgoJMenu("menu.popup.multiplicity");
        menu.add(ActionMultiplicity.getDestMultOne());
        menu.add(ActionMultiplicity.getDestMultZeroToOne());
        menu.add(ActionMultiplicity.getDestMultOneToMany());
        menu.add(ActionMultiplicity.getDestMultZeroToMany());
        popUpActions.add(popUpActions.size() - getPopupAddOffset(),
                menu);
    }


    private void buildNavigationMenu(
            final Vector popUpActions,
            final Object ascStart,
            final Object ascEnd) {
        ArgoJMenu menu =
            new ArgoJMenu("menu.popup.navigability");

        menu.add(ActionNavigability.newActionNavigability(
            ascStart,
            ascEnd,
            ActionNavigability.BIDIRECTIONAL));
        menu.add(ActionNavigability.newActionNavigability(
            ascStart,
            ascEnd,
            ActionNavigability.STARTTOEND));
        menu.add(ActionNavigability.newActionNavigability(
            ascStart,
            ascEnd,
            ActionNavigability.ENDTOSTART));

        popUpActions.add(
                popUpActions.size() - getPopupAddOffset(),
                menu);
    }

    private void buildAggregationMenu(
            final Vector popUpActions,
            final Object ascStart,
            final Object ascEnd) {
        ArgoJMenu menu =
            new ArgoJMenu("menu.popup.aggregation");

        menu.add(ActionAggregation.newActionAggregation(
            ascStart,
            ascEnd,
            ActionAggregation.NONE));
        menu.add(ActionAggregation.newActionAggregation(
            ascStart,
            ascEnd,
            ActionAggregation.AGGREGATE_END1));
        menu.add(ActionAggregation.newActionAggregation(
            ascStart,
            ascEnd,
            ActionAggregation.AGGREGATE_END2));
        menu.add(ActionAggregation.newActionAggregation(
            ascStart,
            ascEnd,
            ActionAggregation.COMPOSITE_END1));
        menu.add(ActionAggregation.newActionAggregation(
            ascStart,
            ascEnd,
            ActionAggregation.COMPOSITE_END2));
        popUpActions.add(
                popUpActions.size() - getPopupAddOffset(),
                menu);
    }

    /**
     * Updates the multiplicity fields.
     */
    protected void updateMultiplicity() {
        if (getOwner() != null
                && srcEnd.getOwner() != null
                && destEnd.getOwner() != null) {
            srcEnd.getMult().setText();
            destEnd.getMult().setText();
        }
    }

    /*
     * @see org.tigris.gef.presentation.Fig#paint(java.awt.Graphics)
     */
    @Override
    public void paint(Graphics g) {
        if (getOwner() == null ) {
            LOG.log(Level.SEVERE, "Trying to paint a FigAssociation without an owner. ");
        } else {
            if (!Model.getFacade().isAConnector(getOwner())) {
                // If we're a UML2 Connector then we don't need arrows.
                // TODO: When issue 6266 is implemented we can
                // get rid of the condition and always call applyArrowHeads
                applyArrowHeads();
            }
        }
        if (getSourceArrowHead() != null && getDestArrowHead() != null) {
            getSourceArrowHead().setLineColor(getLineColor());
            getDestArrowHead().setLineColor(getLineColor());
        }
        super.paint(g);
    }

    /**
     * @return Returns the middleGroup.
     */
    protected FigTextGroup getMiddleGroup() {
        return middleGroup;
    }

    /**
     * Lays out the association edges as any other edge except for
     * special rules for an association that loops back to the same
     * class. For this it is snapped back to the bottom right corner
     * if it resized to the point of not being visible.
     * @see org.tigris.gef.presentation.FigEdgePoly#layoutEdge()
     */
    @Override
    protected void layoutEdge() {
        FigNode sourceFigNode = getSourceFigNode();
        Point[] points = getPoints();
        if (points.length < 3
                && sourceFigNode != null
                && getDestFigNode() == sourceFigNode) {
            Rectangle rect = new Rectangle(
                    sourceFigNode.getX() + sourceFigNode.getWidth() - 20,
                    sourceFigNode.getY() + sourceFigNode.getHeight() - 20,
                    40,
                    40);
            points = new Point[5];
            points[0] = new Point(rect.x, rect.y + rect.height / 2);
            points[1] = new Point(rect.x, rect.y + rect.height);
            points[2] = new Point(rect.x + rect.width , rect.y + rect.height);
            points[3] = new Point(rect.x + rect.width , rect.y);
            points[4] = new Point(rect.x + rect.width / 2, rect.y);
            setPoints(points);
        } else {
            super.layoutEdge();
        }
    }

    /**
     * If the name is updated, update the bounds of the middle group.
     * This makes the selection box appear correctly during prop-panel edit.
     * This is a temporary solution, until a better architecture is decided
     * upon, see issue 5477 and
     * http://argouml.tigris.org/issues/show_bug.cgi?id=5621#desc19.
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#updateNameText()
     */
    protected void updateNameText() {
        super.updateNameText();
        // TODO: Without the null check the following throws a NPE so many
        // times when it is called from FigEdgeModelElement.modelChanged(),
        // we need to think about it.
        if (middleGroup != null) {
            middleGroup.calcBounds();
        }
    }


    /**
     *
     */
    class EndDecoration {
        private FigAssociationEndAnnotation group;
        private FigMultiplicity mult;

        EndDecoration(
            final Object endOwner,
            final DiagramSettings settings,
            final int percentPostionOnLine,
            final int pathDelta,
            final int displacementAngle,
            final int displacementDistance) {
            mult = new FigMultiplicity(endOwner, settings);
            addPathItem(mult,
                    new PathItemPlacement(FigAssociation.this, mult,
                            percentPostionOnLine, pathDelta,
                            displacementAngle, displacementDistance));
            ArgoFigUtil.markPosition(
                    FigAssociation.this, percentPostionOnLine, pathDelta,
                    displacementAngle, displacementDistance, Color.green);

            group = new FigAssociationEndAnnotation(
                    FigAssociation.this, endOwner, settings);
            addPathItem(group,
                    new PathItemPlacement(FigAssociation.this, group,
                            percentPostionOnLine, pathDelta,
                            -displacementAngle, displacementDistance));
            ArgoFigUtil.markPosition(
                    FigAssociation.this, percentPostionOnLine, pathDelta,
                    -displacementAngle, displacementDistance, Color.blue);
        }

        public FigAssociationEndAnnotation getGroup() {
            return group;
        }

        public FigMultiplicity getMult() {
            return mult;
        }

        public FigRole getRole() {
            return group.getRole();
        }

        public int getArrowType() {
            return group.getArrowType();
        }

        public void renderingChanged() {
            mult.renderingChanged();
            group.renderingChanged();
        }

        public void initNotationProviders() {
            mult.initNotationProviders();
        }

        public Object getOwner() {
            return mult.getOwner();
        }
    }

} /* end class FigAssociation */

/**
 * A Fig representing the multiplicity of some model element.
 * This has potential reuse for other edges showing multiplicity. <p>
 *
 * The owner is an AssociationEnd.
 *
 * @author Bob Tarling
 */
class FigMultiplicity extends FigSingleLineTextWithNotation {

    FigMultiplicity(Object owner, DiagramSettings settings) {
        super(owner, new Rectangle(X0, Y0, 90, 20), settings, false,
                new String[] {"multiplicity", "upperValue"});
        // Note that "multiplicity" is what is the notation is listening
        // for in UML1.4 "uppervalue" are listened to in UML2. It is not
        // currently why it is not required to register for "lowerValue"
        // also.
        setTextFilled(false);
        setJustification(FigText.JUSTIFY_CENTER);
    }

    @Override
    protected int getNotationProviderType() {
        return NotationProviderFactory2.TYPE_MULTIPLICITY;
    }

}

/**
 * A textual Fig representing the ordering of some model element,
 * i.e. "{ordered}" or nothing.
 * This has potential reuse for other edges showing ordering. <p>
 *
 * This Fig is not editable by the user.
 *
 * @author Bob Tarling
 */
class FigOrdering extends FigSingleLineText {

    private static final long serialVersionUID = 5385230942216677015L;

    FigOrdering(Object owner, DiagramSettings settings) {
        super(owner, new Rectangle(X0, Y0, 90, 20), settings, false,
                "ordering");
        setTextFilled(false);
        setJustification(FigText.JUSTIFY_CENTER);
        setEditable(false);
    }

    @Override
    protected void setText() {
        assert getOwner() != null;
        if (getSettings().getNotationSettings().isShowProperties()) {
            setText(getOrderingName(Model.getFacade().getOrdering(getOwner())));
        } else {
            setText("");
        }
        damage();
    }

    /**
     * Returns the name of the OrderingKind.
     *
     * @param orderingKind the kind of ordering
     * @return "{ordered}" or "", the latter if null or unordered
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
        // TODO: I18N
        return "{" + Model.getFacade().getName(orderingKind) + "}";
    }
}

/**
 * A Fig representing the association end role of some model element.
 * This class is designed as a composite part and should always be
 * part of a FigGroup.
 * @author Bob Tarling
 */
class FigRole extends FigSingleLineTextWithNotation {

    FigRole(Object owner, DiagramSettings settings) {
        super(owner, new Rectangle(X0, Y0, 90, 20), settings, false,
                (String[]) null
        // no need to listen to these property changes - the
        // notationProvider takes care of this.
                /*, new String[] {"name", "visibility", "stereotype"}*/
                );
        setTextFilled(false);
        setJustification(FigText.JUSTIFY_CENTER);
        setText();
    }

    protected int getNotationProviderType() {
        return NotationProviderFactory2.TYPE_ASSOCIATION_END_NAME;
    }

    /**
     * Property change listener to recalculate bounds of enclosing
     * group whenever any properties of the FigRole get changed.
     * This is only really needed for the name, see issue 5621.
     *
     * @param pce The property change event to process.
     * @see org.argouml.uml.diagram.ui.FigSingleLineTextWithNotation#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        super.propertyChange(pce);
        assert (getGroup() != null);
        this.getGroup().calcBounds();
    }

}

/**
 * The arrowhead and the group of labels shown at the association end:
 * the role name and the ordering property.
 * This does not include the multiplicity. <p>
 *
 * This class does not yet support arrows for a FigAssociationEnd,
 * as is used for N-ary associations.
 */
class FigAssociationEndAnnotation extends FigTextGroup {

    private static final long serialVersionUID = 1871796732318164649L;

    private static final ArrowHead NAV_AGGR =
        new ArrowHeadComposite(ArrowHeadDiamond.WhiteDiamond,
                   new ArrowHeadGreater());

    private static final ArrowHead NAV_COMP =
        new ArrowHeadComposite(ArrowHeadDiamond.BlackDiamond,
                   new ArrowHeadGreater());

    // These are a list of arrow types.
    private static final int NONE = 0;
    private static final int AGGREGATE = 1;
    private static final int COMPOSITE = 2;

    // Added to the arrow type for navigable
    private static final int NAV = 3;

    /**
     * All the arrow head types.
     */
    public static final ArrowHead[] ARROW_HEADS = new ArrowHead[6];
    static {
        ARROW_HEADS[NONE] = ArrowHeadNone.TheInstance;
        ARROW_HEADS[AGGREGATE] = ArrowHeadDiamond.WhiteDiamond;
        ARROW_HEADS[COMPOSITE] = ArrowHeadDiamond.BlackDiamond;
        ARROW_HEADS[NAV + NONE] = new ArrowHeadGreater();
        ARROW_HEADS[NAV + AGGREGATE] = NAV_AGGR;
        ARROW_HEADS[NAV + COMPOSITE] = NAV_COMP;
    }

    private FigRole role;
    private FigOrdering ordering;
    private FigEdgeModelElement figEdge;

    FigAssociationEndAnnotation(FigEdgeModelElement edge, Object owner,
            DiagramSettings settings) {
        super(owner, settings);
        figEdge = edge;

        role = new FigRole(owner, settings);
        addFig(role);

        ordering = new FigOrdering(owner, settings);
        addFig(ordering);

        Model.getPump().addModelEventListener(this, owner,
                new String[] {"isNavigable", "aggregation", "participant"});
    }

    /*
     * @see org.tigris.gef.presentation.Fig#removeFromDiagram()
     */
    @Override
    public void removeFromDiagram() {
        Model.getPump().removeModelEventListener(this,
                getOwner(),
                new String[] {"isNavigable", "aggregation", "participant"});
        super.removeFromDiagram();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        if (pce instanceof AttributeChangeEvent
            && (pce.getPropertyName().equals("isNavigable")
                || pce.getPropertyName().equals("aggregation"))) {
            ((FigAssociation) figEdge).applyArrowHeads();
            damage();
        }
        if (pce instanceof AddAssociationEvent
                && pce.getPropertyName().equals("participant")) {
            figEdge.determineFigNodes();
        }

        String pName = pce.getPropertyName();
        if (pName.equals("editing")
                && Boolean.FALSE.equals(pce.getNewValue())) {
            // Finished editing.
            // Parse the text that was edited.
            // Only the role is editable, hence:
            role.textEdited();
            calcBounds();
            endTrans();
        } else if (pName.equals("editing")
                && Boolean.TRUE.equals(pce.getNewValue())) {
//            figEdge.showHelp(role.getParsingHelp());
//            role.setText();
            role.textEditStarted();
        } else {
            // Pass everything else to superclass
            super.propertyChange(pce);
        }
    }

    /**
     * @return the current arrow type of this end of the association
     */
    public int getArrowType() {
        assert getOwner() != null;


        final Object ak = Model.getFacade().getAggregation1(getOwner());
        final boolean nav = Model.getFacade().isNavigable(getOwner());

        int arrowType;
        if (Model.getAggregationKind().getAggregate().equals(ak)) {
            arrowType = AGGREGATE;
        } else if (Model.getAggregationKind().getComposite().equals(ak)) {
            arrowType = COMPOSITE;
        } else {
            arrowType = NONE;
        }
        if (nav) {
            arrowType += NAV;
        }
        return arrowType;
    }

    FigRole getRole() {
        return role;
    }
}
