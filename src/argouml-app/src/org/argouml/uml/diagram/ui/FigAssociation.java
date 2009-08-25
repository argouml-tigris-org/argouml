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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.model.AddAssociationEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.ui.ArgoJMenu;
import org.argouml.ui.targetmanager.TargetManager;
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

    private static final Logger LOG = Logger.getLogger(FigAssociation.class);

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
     * @param owner owning uml element
     * @param settings rendering settings
     */
    public FigAssociation(Object owner, DiagramSettings settings) {
        super(owner, settings);
        
        createNameLabel(owner, settings);
        
        createEndFigs(owner, settings);
        
        setBetweenNearestPoints(true);
        
        initializeNotationProvidersInternal(owner);
    }
    
    /**
     * Called by the constructor to create the Figs at each end
     * of the association.
     * TODO: This is temporary during refactoring process. We should
     * override setDestFigNode and setSourceFigNode and create the ends there.
     * That will allow the same pattern to work for UML2 where we cannot assume
     * the connection order.
     * 
     * @param owner
     * @param settings
     */
    private void createEndFigs(
            final Object owner,
            final DiagramSettings settings) {
        final Object[] ends = // UML objects of AssociationEnd type
            Model.getFacade().getConnections(owner).toArray();
        
        srcEnd = createEnd(ends[0], settings, 0, 5, 135, 5);
        destEnd = createEnd(ends[1], settings, 100, -5, 45, 5);
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
        middleGroup.renderingChanged();
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
        /* Check if multiple items are selected: */
        boolean ms = TargetManager.getInstance().getTargets().size() > 1;
        /* None of the menu-items below apply
         * when multiple modelelements are selected:*/
        if (ms) {
            return popUpActions;
        }

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
	    getSquaredDistance(me.getPoint(), firstPoint);
	int destDeterminingFactor =
	    getSquaredDistance(me.getPoint(), lastPoint);

	if (srcDeterminingFactor < rSquared
	    && srcDeterminingFactor < destDeterminingFactor) {

            ArgoJMenu multMenu =
		new ArgoJMenu("menu.popup.multiplicity");

            multMenu.add(ActionMultiplicity.getSrcMultOne());
            multMenu.add(ActionMultiplicity.getSrcMultZeroToOne());
            multMenu.add(ActionMultiplicity.getSrcMultOneToMany());
            multMenu.add(ActionMultiplicity.getSrcMultZeroToMany());
            popUpActions.add(popUpActions.size() - getPopupAddOffset(),
                    multMenu);

            ArgoJMenu aggMenu = new ArgoJMenu("menu.popup.aggregation");

	    aggMenu.add(ActionAggregation.getSrcAggNone());
	    aggMenu.add(ActionAggregation.getSrcAgg());
	    aggMenu.add(ActionAggregation.getSrcAggComposite());
	    popUpActions.add(popUpActions.size() - getPopupAddOffset(),
                    aggMenu);
	} else if (destDeterminingFactor < rSquared) {
            ArgoJMenu multMenu =
		new ArgoJMenu("menu.popup.multiplicity");
	    multMenu.add(ActionMultiplicity.getDestMultOne());
	    multMenu.add(ActionMultiplicity.getDestMultZeroToOne());
	    multMenu.add(ActionMultiplicity.getDestMultOneToMany());
	    multMenu.add(ActionMultiplicity.getDestMultZeroToMany());
	    popUpActions.add(popUpActions.size() - getPopupAddOffset(),
                    multMenu);

            ArgoJMenu aggMenu = new ArgoJMenu("menu.popup.aggregation");
	    aggMenu.add(ActionAggregation.getDestAggNone());
	    aggMenu.add(ActionAggregation.getDestAgg());
	    aggMenu.add(ActionAggregation.getDestAggComposite());
	    popUpActions
                    .add(popUpActions.size() - getPopupAddOffset(), aggMenu);
	}
	// else: No particular options for right click in middle of line

	// Options available when right click anywhere on line
	Object association = getOwner();
	if (association != null) {
	    // Navigability menu with suboptions built dynamically to
	    // allow navigability from atart to end, from end to start
	    // or bidirectional
	    Collection ascEnds = Model.getFacade().getConnections(association);
            Iterator iter = ascEnds.iterator();
	    Object ascStart = iter.next();
	    Object ascEnd = iter.next();

	    if (Model.getFacade().isAClassifier(
	            Model.getFacade().getType(ascStart))
                    && Model.getFacade().isAClassifier(
                            Model.getFacade().getType(ascEnd))) {
                ArgoJMenu navMenu =
		    new ArgoJMenu("menu.popup.navigability");

		navMenu.add(ActionNavigability.newActionNavigability(
                    ascStart,
		    ascEnd,
		    ActionNavigability.BIDIRECTIONAL));
		navMenu.add(ActionNavigability.newActionNavigability(
                    ascStart,
		    ascEnd,
		    ActionNavigability.STARTTOEND));
		navMenu.add(ActionNavigability.newActionNavigability(
                    ascStart,
                    ascEnd,
                    ActionNavigability.ENDTOSTART));

		popUpActions.add(popUpActions.size() - getPopupAddOffset(),
                        navMenu);
	    }
	}

	return popUpActions;
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
            LOG.error("Trying to paint a FigAssociation without an owner. ");
        } else {
            applyArrowHeads(); 
        }
        if (getSourceArrowHead() != null && getDestArrowHead() != null) {
            getSourceArrowHead().setLineColor(getLineColor());
            getDestArrowHead().setLineColor(getLineColor());
        }
        super.paint(g);
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#paintClarifiers(java.awt.Graphics)
     */
    @Override
    public void paintClarifiers(Graphics g) {
        indicateBounds(getNameFig(), g);
        indicateBounds(srcEnd.getMult(), g);
        indicateBounds(srcEnd.getRole(), g);
        indicateBounds(destEnd.getMult(), g);
        indicateBounds(destEnd.getRole(), g);
        super.paintClarifiers(g);
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
                "multiplicity");
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
        assert(getGroup() != null);
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

    // These are a list of arrow types. Positioning is important as we subtract
    // 3 to convert a navigable arrow to a non navigable with the same
    // aggregation
    private static final int NONE = 0;
    private static final int AGGREGATE = 1;
    private static final int COMPOSITE = 2;
    private static final int NAV_NONE = 3;
    private static final int NAV_AGGREGATE = 4;
    private static final int NAV_COMPOSITE = 5;
    
    /**
     * All the arrow head types.
     */
    public static final ArrowHead[] ARROW_HEADS = new ArrowHead[6];
    static {
        ARROW_HEADS[NONE] = ArrowHeadNone.TheInstance;
        ARROW_HEADS[AGGREGATE] = ArrowHeadDiamond.WhiteDiamond;
        ARROW_HEADS[COMPOSITE] = ArrowHeadDiamond.BlackDiamond;
        ARROW_HEADS[NAV_NONE] = new ArrowHeadGreater();
        ARROW_HEADS[NAV_AGGREGATE] = NAV_AGGR;
        ARROW_HEADS[NAV_COMPOSITE] = NAV_COMP;
    }
    
    private FigRole role;
    private FigOrdering ordering;
    private int arrowType = 0;
    private FigEdgeModelElement figEdge;

    FigAssociationEndAnnotation(FigEdgeModelElement edge, Object owner,
            DiagramSettings settings) {
        super(owner, settings);
        figEdge = edge;
        
        role = new FigRole(owner, settings);
        addFig(role);

        ordering = new FigOrdering(owner, settings);
        addFig(ordering);

        determineArrowHead();
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
            determineArrowHead();
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
     * Decide which arrow head should appear
     */
    private void determineArrowHead() {
        assert getOwner() != null;

        Object ak =  Model.getFacade().getAggregation(getOwner());
        boolean nav = Model.getFacade().isNavigable(getOwner());

        if (nav) {
            if (Model.getAggregationKind().getNone().equals(ak)
                    || (ak == null)) {
                arrowType = NAV_NONE;
            } else if (Model.getAggregationKind().getAggregate()
                    .equals(ak)) {
                arrowType = NAV_AGGREGATE;
            } else if (Model.getAggregationKind().getComposite()
                    .equals(ak)) {
                arrowType = NAV_COMPOSITE;
            }
        } else {
            if (Model.getAggregationKind().getNone().equals(ak)
                    || (ak == null)) {
                arrowType = NONE;
            } else if (Model.getAggregationKind().getAggregate()
                    .equals(ak)) {
                arrowType = AGGREGATE;
            } else if (Model.getAggregationKind().getComposite()
                    .equals(ak)) {
                arrowType = COMPOSITE;
            }
        }
    }
    
    /**
     * @return the current arrow type of this end of the association
     */
    public int getArrowType() {
        return arrowType;
    }

    FigRole getRole() {
        return role;
    }
}
