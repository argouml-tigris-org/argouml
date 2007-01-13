// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoNotationEvent;
import org.argouml.application.events.ArgoNotationEventListener;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.AddAssociationEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.ui.ArgoJMenu;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.notation.NotationProvider;
import org.argouml.uml.notation.uml.NotationUtilityUml;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.PathConvPercentPlusConst;
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

    /**
     * The serial version id.
     */
    static final long serialVersionUID = 9100125695919853919L;

    /**
     * We suppress the arrow heads if both ends of an association
     * are navigable.
     */
    private static final boolean SUPPRESS_BIDIRECTIONAL_ARROWS = true;

    private static final Logger LOG = Logger.getLogger(FigAssociation.class);

    /**
     * Group for the FigTexts concerning the source association end.
     */
    private FigAssociationEndAnnotation srcGroup;

    /**
     * Group for the FigTexts concerning the dest association end.
     */
    private FigAssociationEndAnnotation destGroup;

    /**
     * Group for the FigTexts concerning the name and stereotype of the
     * association itself.
     */
    private FigTextGroup middleGroup = new FigTextGroup();

    private FigMultiplicity srcMult;
    private FigMultiplicity destMult;

    /**
     * Don't call this constructor directly. It is public since this
     * is necessary for loading. Use the FigAssociation(Object, Layer)
     * constructor instead!
     */
    public FigAssociation() {
        super();

        // let's use groups to construct the different text sections at
        // the association
        if (getNameFig() != null) {
            middleGroup.addFig(getNameFig());
        }
        middleGroup.addFig(getStereotypeFig());
        addPathItem(middleGroup,
                new PathConvPercent2(this, middleGroup, 50, 25));

        srcMult = new FigMultiplicity();
        addPathItem(srcMult, new PathConvPercentPlusConst(this, 0, 15, 15));
        
        srcGroup = new FigAssociationEndAnnotation(this);
        addPathItem(srcGroup, new PathConvPercentPlusConst(this, 0, 35, -15));

        destMult = new FigMultiplicity();
        addPathItem(destMult,
		    new PathConvPercentPlusConst(this, 100, -15, 15));
        
        destGroup = new FigAssociationEndAnnotation(this);
        addPathItem(destGroup,
		    new PathConvPercentPlusConst(this, 100, -35, -15));

        setBetweenNearestPoints(true);
        
        // next line necessary for loading
        setLayer(ProjectManager.getManager().getCurrentProject()
		 .getActiveDiagram().getLayer());
    }

    /**
     * Constructor that hooks the Fig to an existing UML element.
     *
     * @param edge the UMl element
     * @param lay the layer
     */
    public FigAssociation(Object edge, Layer lay) {
        this();
        setOwner(edge);
        setLayer(lay);
    }

    public void setOwner(Object owner) {
        super.setOwner(owner);
        
        Object[] ends = 
            Model.getFacade().getConnections(owner).toArray();
        
        Object source = ends[0];
        Object dest = ends[1];
        
        srcGroup.setOwner(source);
        srcMult.setOwner(source);
        
        destGroup.setOwner(dest);
        destMult.setOwner(dest);
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#updateListeners(java.lang.Object, java.lang.Object)
     */
    public void updateListeners(Object oldOwner, Object newOwner) {
        removeAllElementListeners();
        if (newOwner != null) {
            addElementListener(newOwner, new String[] {"isAbstract", "remove"});
        }
        /* No further listeners required in this case - the rest is handled 
         * by the notationProvider and sub-Figs. */
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#getNotationProviderType()
     */
    protected int getNotationProviderType() {
        return NotationProviderFactory2.TYPE_ASSOCIATION_NAME;
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#textEdited(org.tigris.gef.presentation.FigText)
     */
    protected void textEdited(FigText ft) {

        if (getOwner() == null) {
            return;
        }
        super.textEdited(ft);
        
        Collection conn = Model.getFacade().getConnections(getOwner());
        if (conn == null || conn.size() == 0) {
            return;
        }

	String msg =
	    Translator.localize("statusmsg.bar.error.parsing.multiplicity");

	if (ft == srcGroup.role) {
            ((FigRole) ft).parse();
	} else if (ft == destGroup.role) {
            ((FigRole) ft).parse();
	} else if (ft == srcMult) {
	    Object srcAE = (conn.toArray())[0];
	    try {
	        Object multi = Model.getDataTypesFactory()
	                        .createMultiplicity(srcMult.getText());
	        Model.getCoreHelper().setMultiplicity(srcAE, multi);
	    } catch (IllegalArgumentException e) {
	        Object[] args = {e.getLocalizedMessage()};
	        ProjectBrowser.getInstance().getStatusBar().showStatus(
                    Translator.messageFormat(msg, args));
	        srcMult.setText(NotationUtilityUml.generateMultiplicity(
                        Model.getFacade().getMultiplicity(srcAE)));
	    }
	} else if (ft == destMult) {
	    Object destAE = (conn.toArray())[1];
	    try {
	        Object multi = Model.getDataTypesFactory()
	                        .createMultiplicity(destMult.getText());
	        Model.getCoreHelper().setMultiplicity(destAE, multi);
	    } catch (IllegalArgumentException e) {
	        Object[] args = {e.getLocalizedMessage()};
	        ProjectBrowser.getInstance().getStatusBar().showStatus(
                    Translator.messageFormat(msg, args));
                destMult.setText(NotationUtilityUml.generateMultiplicity(
                        Model.getFacade().getMultiplicity(destAE)));
	    }
	}
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#textEditStarted(org.tigris.gef.presentation.FigText)
     */
    protected void textEditStarted(FigText ft) {
        if (ft == srcGroup.role) {
            showHelp(srcGroup.role.getParsingHelp());
        } else if (ft == destGroup.role) {
            showHelp(destGroup.role.getParsingHelp());
        } else if (ft == srcMult) {
            showHelp("parsing.help.fig-association-source-multiplicity");
        } else if (ft == destMult) {
            showHelp("parsing.help.fig-association-destination-multiplicity");
        } else {
            super.textEditStarted(ft);
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    protected void modelChanged(PropertyChangeEvent e) {
        if (getOwner() == null || getLayer() == null) {
            return;
        }
        super.modelChanged(e);
        if (e instanceof AttributeChangeEvent) {
            if (e.getPropertyName().equals("isAbstract")/*
                    || e.getPropertyName().equals("name")*/) {
                updateAbstract();
            }
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#renderingChanged()
     */
    protected void renderingChanged() {
        // We don't want to redraw everything everytime 
        // one things changes - Bob.
        // So, do not call this unless really needed! We need 
        // this for e.g. when the Notation Language changes,
        // and this is called by setOwner() - Michiel.
        updateAbstract();
        updateMultiplicity();
        super.renderingChanged();
    }

    /**
     * Choose the arrowhead style for each end. <p>
     * 
     * TODO: This is called from paint(). Would it not better 
     * be called from renderingChanged()?
     */
    protected void applyArrowHeads() {
        int sourceArrowType = srcGroup.getArrowType();
        int destArrowType = destGroup.getArrowType();
        
        if (SUPPRESS_BIDIRECTIONAL_ARROWS
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
    public Vector getPopUpActions(MouseEvent me) {
	Vector popUpActions = super.getPopUpActions(me);
        /* Check if multiple items are selected: */
        boolean ms = TargetManager.getInstance().getTargets().size() > 1;
        /* None of the menu-items below apply
         * when multiple modelelements are selected:*/
        if (ms) return popUpActions;

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
            popUpActions.insertElementAt(multMenu,
                popUpActions.size() - getPopupAddOffset());

            ArgoJMenu aggMenu = new ArgoJMenu("menu.popup.aggregation");

	    aggMenu.add(ActionAggregation.getSrcAggNone());
	    aggMenu.add(ActionAggregation.getSrcAgg());
	    aggMenu.add(ActionAggregation.getSrcAggComposite());
	    popUpActions.insertElementAt(aggMenu,
					 (popUpActions.size()
					  - getPopupAddOffset()));
	} else if (destDeterminingFactor < rSquared) {
            ArgoJMenu multMenu =
		new ArgoJMenu("menu.popup.multiplicity");
	    multMenu.add(ActionMultiplicity.getDestMultOne());
	    multMenu.add(ActionMultiplicity.getDestMultZeroToOne());
	    multMenu.add(ActionMultiplicity.getDestMultOneToMany());
	    multMenu.add(ActionMultiplicity.getDestMultZeroToMany());
	    popUpActions.insertElementAt(multMenu,
					 (popUpActions.size()
					  - getPopupAddOffset()));

            ArgoJMenu aggMenu = new ArgoJMenu("menu.popup.aggregation");
	    aggMenu.add(ActionAggregation.getDestAggNone());
	    aggMenu.add(ActionAggregation.getDestAgg());
	    aggMenu.add(ActionAggregation.getDestAggComposite());
	    popUpActions.insertElementAt(aggMenu,
					 (popUpActions.size()
					  - getPopupAddOffset()));
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

		popUpActions.insertElementAt(navMenu,
					     (popUpActions.size()
					      - getPopupAddOffset()));
	    }
	}

	return popUpActions;
    }

    /**
     * Updates the multiplicity fields.
     */
    protected void updateMultiplicity() {
        if (getOwner() != null 
                && srcMult.getOwner() != null 
                && destMult.getOwner() != null) {
            srcMult.setText();
            destMult.setText();
        }
    }

    /**
     * Updates the name if modelchanged receives an "isAbstract" event.
     */
    protected void updateAbstract() {
        if (getNameFig() == null) {
            return;
        }
        Rectangle rect = getBounds();
        if (getOwner() == null) {
            return;
        }
        Object assoc =  getOwner();
        if (Model.getFacade().isAbstract(assoc)) {
            getNameFig().setFont(getItalicLabelFont());
        } else {
            getNameFig().setFont(getLabelFont());
        }
        super.updateNameText();
        setBounds(rect.x, rect.y, rect.width, rect.height);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#paint(java.awt.Graphics)
     */
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
    public void paintClarifiers(Graphics g) {
        indicateBounds(getNameFig(), g);
        indicateBounds(srcMult, g);
        indicateBounds(srcGroup.role, g);
        indicateBounds(destMult, g);
        indicateBounds(destGroup.role, g);
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
    
} /* end class FigAssociation */

/**
 * A Fig representing the multiplicty of some model element.
 * This has potential reuse for other edges showing multiplicity
 * @author Bob Tarling
 */
class FigMultiplicity extends FigSingleLineText 
    implements PropertyChangeListener {

    private static final long serialVersionUID = 5385230942216677015L;

    FigMultiplicity() {
        super(10, 10, 90, 20, false, "multiplicity");

        setTextFilled(false);
        setJustification(FigText.JUSTIFY_CENTER);
    }

    protected void setText() {
        assert getOwner() != null;
        Object multi = Model.getFacade().getMultiplicity(getOwner());
        setText(NotationUtilityUml.generateMultiplicity(multi));
        damage();
    }
}

/**
 * A textual Fig representing the ordering of some model element,
 * i.e. "{ordered}" or "{sorted}".
 * This has potential reuse for other edges showing ordering.
 * @author Bob Tarling
 */
class FigOrdering extends FigSingleLineText {

    private static final long serialVersionUID = 5385230942216677015L;

    FigOrdering() {
        super(10, 10, 90, 20, false, "ordering");
        setTextFilled(false);
        setJustification(FigText.JUSTIFY_CENTER);
    }

    protected void setText() {
        assert getOwner() != null;
        setText(getOrderingName(Model.getFacade().getOrdering(getOwner())));
        damage();
    }

    /**
     * Returns the name of the OrderingKind.
     *
     * @param orderingKind the kind of ordering
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
}

/**
 * A Fig representing the ordering of some model element.
 * This has potential reuse for other edges showing ordering
 * @author Bob Tarling
 */
class FigRole extends FigSingleLineText 
    implements ArgoNotationEventListener {

    private static final long serialVersionUID = 5385230942216677015L;

    private NotationProvider notationProviderRole;

    FigRole() {
        super(10, 10, 90, 20, false/*, 
        // no need to listen to these property changes - the 
        // notationProvider takes care of this.
                new String[] {"name", "visibility", "stereotype"}*/);
        setTextFilled(false);
        setJustification(FigText.JUSTIFY_CENTER);
        ArgoEventPump.addListener(
                ArgoEventTypes.ANY_NOTATION_EVENT, this);
    }

    public void setOwner(Object owner) {
        super.setOwner(owner);
        getNewNotation();
    }

    private void getNewNotation() {
        if (notationProviderRole != null) {
            notationProviderRole.cleanListener(this, getOwner());
        }
        if (getOwner() != null) {
            notationProviderRole = 
                NotationProviderFactory2.getInstance().getNotationProvider(
                        NotationProviderFactory2.TYPE_ASSOCIATION_END_NAME, 
                        getOwner(),
                        this);
        }
    }
    
    protected void setText() {
        assert getOwner() != null;
        assert notationProviderRole != null;
        setText(notationProviderRole.toString(getOwner(), null));
    }

    /**
     * @return the help-text for parsing
     */
    public String getParsingHelp() {
        return notationProviderRole.getParsingHelp();
    }
    
    /**
     * Parse the edited text to adapt the UML model.
     */
    public void parse() {
        notationProviderRole.parse(getOwner(), getText());
        setText();
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigSingleLineText#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent pce) {
        notationProviderRole.updateListener(this, getOwner(), pce);
        setText();
        damage();
        super.propertyChange(pce);  // do we need this?
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigSingleLineText#removeFromDiagram()
     */
    public void removeFromDiagram() {
        notationProviderRole.cleanListener(this, getOwner());
        ArgoEventPump.removeListener(
                ArgoEventTypes.ANY_NOTATION_EVENT, this);
        super.removeFromDiagram();
    }

    /*
     * @see org.argouml.application.events.ArgoNotationEventListener#notationAdded(org.argouml.application.events.ArgoNotationEvent)
     */
    public void notationAdded(ArgoNotationEvent e) {
        getNewNotation();
        setText();
    }

    /*
     * @see org.argouml.application.events.ArgoNotationEventListener#notationChanged(org.argouml.application.events.ArgoNotationEvent)
     */
    public void notationChanged(ArgoNotationEvent e) {
        getNewNotation();
        setText();
    }

    /*
     * @see org.argouml.application.events.ArgoNotationEventListener#notationProviderAdded(org.argouml.application.events.ArgoNotationEvent)
     */
    public void notationProviderAdded(ArgoNotationEvent e) {
        getNewNotation();
        setText();
    }

    /*
     * @see org.argouml.application.events.ArgoNotationEventListener#notationProviderRemoved(org.argouml.application.events.ArgoNotationEvent)
     */
    public void notationProviderRemoved(ArgoNotationEvent e) {
        getNewNotation();
        setText();
    }

    /*
     * @see org.argouml.application.events.ArgoNotationEventListener#notationRemoved(org.argouml.application.events.ArgoNotationEvent)
     */
    public void notationRemoved(ArgoNotationEvent e) {
        getNewNotation();
        setText();
    }
}

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
    
    public static final ArrowHead[] ARROW_HEADS = new ArrowHead[6];
    static {
        ARROW_HEADS[NONE] = ArrowHeadNone.TheInstance;
        ARROW_HEADS[AGGREGATE] = ArrowHeadDiamond.WhiteDiamond;
        ARROW_HEADS[COMPOSITE] = ArrowHeadDiamond.BlackDiamond;
        ARROW_HEADS[NAV_NONE] = new ArrowHeadGreater();
        ARROW_HEADS[NAV_AGGREGATE] = NAV_AGGR;
        ARROW_HEADS[NAV_COMPOSITE] = NAV_COMP;
    }
    
    FigRole role;
    FigOrdering ordering;
    int arrowType = 0;
    private FigEdgeModelElement figEdge;
    
    public FigAssociationEndAnnotation(FigEdgeModelElement figEdge) {
        this.figEdge = figEdge;
        
        role = new FigRole();
        addFig(role);

        ordering = new FigOrdering();
        addFig(ordering);
    }
    
    /*
     * @see org.tigris.gef.presentation.Fig#setOwner(java.lang.Object)
     */
    public void setOwner(Object owner) {
        if (owner != null) {
            if (!Model.getFacade().isAAssociationEnd(owner)) {
                throw new IllegalArgumentException(
                        "An AssociationEnd was expected");
            }
            super.setOwner(owner);
            ordering.setOwner(owner);
            role.setOwner(owner);
            role.setText();
            determineArrowHead();
            Model.getPump().addModelEventListener(this, owner, 
                    new String[] {"isNavigable", "aggregation", "participant"});
        }
    }

    /*
     * @see org.tigris.gef.presentation.Fig#removeFromDiagram()
     */
    public void removeFromDiagram() {
        Model.getPump().removeModelEventListener(this, 
                getOwner(), 
                new String[] {"isNavigable", "aggregation", "participant"});
        super.removeFromDiagram();
    }
    
    /*
     * @see org.tigris.gef.presentation.Fig#propertyChange(java.beans.PropertyChangeEvent)
     */
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
    
    public int getArrowType() {
        return arrowType;
    }
}
