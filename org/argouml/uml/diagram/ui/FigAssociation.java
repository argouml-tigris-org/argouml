// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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
import java.util.Iterator;
import java.util.Vector;

import org.argouml.application.api.Notation;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.model.ModelFacade;
import org.argouml.ui.ArgoJMenu;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.PathConvPercent;
import org.tigris.gef.base.PathConvPercentPlusConst;
import org.tigris.gef.presentation.ArrowHead;
import org.tigris.gef.presentation.ArrowHeadComposite;
import org.tigris.gef.presentation.ArrowHeadDiamond;
import org.tigris.gef.presentation.ArrowHeadGreater;
import org.tigris.gef.presentation.ArrowHeadNone;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigText;


/**
 * This class represents an association Fig on a diagram
 *
 */
public class FigAssociation extends FigEdgeModelElement {
   
    // TODO: should be part of some preferences object
    private static final boolean SUPPRESS_BIDIRECTIONAL_ARROWS = true;

    /**
     * Group for the FigTexts concerning the source association end
     */
    private FigTextGroup srcGroup = new FigTextGroup();

    /**
     * Group for the FigTexts concerning the dest association end
     */
    private FigTextGroup destGroup = new FigTextGroup();

    /**
     * Group for the FigTexts concerning the name and stereotype of the 
     * association itself.
     * TODO: provide getter instead and maybe setter if needed
     */
    private FigTextGroup middleGroup = new FigTextGroup();
    
    private FigText srcMult, srcRole;
    private FigText destMult, destRole;
    private FigText srcOrdering, destOrdering;

    private ArrowHead sourceArrowHead, destArrowHead;



    /**
     * Main constructor
     * 
     * Don't call this constructor directly. It is public since this
     * is necessary for loading. Use the FigAssociation(Object, Layer)
     * constructor instead!
     */
    public FigAssociation() {
        super();
    
        // lets use groups to construct the different text sections at
        // the association
        middleGroup.addFig(getNameFig());
        middleGroup.addFig(getStereotypeFig());
        addPathItem(middleGroup, new PathConvPercent(this, 50, 25));
    
        srcMult = new FigText(10, 10, 90, 20);
        srcMult.setFont(getLabelFont());
        srcMult.setTextColor(Color.black);
        srcMult.setTextFilled(false);
        srcMult.setFilled(false);
        srcMult.setLineWidth(0);
        srcMult.setMultiLine(false);
        srcMult.setJustification(FigText.JUSTIFY_CENTER);

        srcRole = new FigText(10, 10, 90, 20);
        srcRole.setFont(getLabelFont());
        srcRole.setTextColor(Color.black);
        srcRole.setTextFilled(false);
        srcRole.setFilled(false);
        srcRole.setLineWidth(0);
        srcRole.setMultiLine(false);
        srcRole.setJustification(FigText.JUSTIFY_CENTER);

        srcOrdering = new FigText(10, 10, 90, 20);
        srcOrdering.setFont(getLabelFont());
        srcOrdering.setTextColor(Color.black);
        srcOrdering.setTextFilled(false);
        srcOrdering.setFilled(false);
        srcOrdering.setLineWidth(0);
        srcOrdering.setMultiLine(false);
        srcOrdering.setJustification(FigText.JUSTIFY_CENTER);

        srcGroup.addFig(srcRole);
        srcGroup.addFig(srcOrdering);
        addPathItem(srcMult, new PathConvPercentPlusConst(this, 0, 15, 15));
        addPathItem(srcGroup, new PathConvPercentPlusConst(this, 0, 35, -15));
   
        destMult = new FigText(10, 10, 90, 20);
        destMult.setFont(getLabelFont());
        destMult.setTextColor(Color.black);
        destMult.setTextFilled(false);
        destMult.setFilled(false);
        destMult.setLineWidth(0);
        destMult.setMultiLine(false);
        destMult.setJustification(FigText.JUSTIFY_CENTER);

        destRole = new FigText(0, 0, 90, 20);
        destRole.setFont(getLabelFont());
        destRole.setTextColor(Color.black);
        destRole.setTextFilled(false);
        destRole.setFilled(false);
        destRole.setLineWidth(0);
        destRole.setMultiLine(false);
        destRole.setJustification(FigText.JUSTIFY_CENTER);

        destOrdering = new FigText(0, 0, 90, 20);
        destOrdering.setFont(getLabelFont());
        destOrdering.setTextColor(Color.black);
        destOrdering.setTextFilled(false);
        destOrdering.setFilled(false);
        destOrdering.setLineWidth(0);
        destOrdering.setMultiLine(false);
        destOrdering.setJustification(FigText.JUSTIFY_CENTER);

        destGroup.addFig(destRole);
        destGroup.addFig(destOrdering);
        addPathItem(destMult,
		    new PathConvPercentPlusConst(this, 100, -15, 15));
        addPathItem(destGroup,
		    new PathConvPercentPlusConst(this, 100, -35, -15));
    
        setBetweenNearestPoints(true);
        // next line necessary for loading
        setLayer(ProjectManager.getManager().getCurrentProject()
		 .getActiveDiagram().getLayer());
    }

    /**
     * Constructor that hooks the Fig to an existing UML element
     * @param edge the UMl element
     * @param lay the layer
     */
    public FigAssociation(Object edge, Layer lay) {
	this();
	setLayer(lay);
	setOwner(edge);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setOwner(java.lang.Object)
     */
    public void setOwner(Object association) {
	super.setOwner(association);

	if (org.argouml.model.ModelFacade.isAAssociation(association)) {
	    Collection connections = ModelFacade.getConnections(association);
	    for (int i = 0; i < connections.size(); i++) {
		Object assEnd = (connections.toArray())[i];
		Model.getPump()
		    .removeModelEventListener(this, assEnd);
		Model.getPump()
		    .addModelEventListener(this, assEnd);
	    }
	    Model.getPump()
		.removeModelEventListener(this, association);
	    Model.getPump()
		.addModelEventListener(this, association);
	    Object assEnd1 = (connections.toArray())[0];
	    FigNode destNode =
		(FigNode) getLayer()
		    .presentationFor(ModelFacade.getType(assEnd1));
	    FigNode srcNode =
		(FigNode) getLayer()
		    .presentationFor(ModelFacade.getType(assEnd1));
	    if (destNode != null) {
		setDestFigNode(destNode);
		setDestPortFig(destNode);
	    }
	    if (srcNode != null) {
		setSourceFigNode(srcNode); 
		setSourcePortFig(srcNode);  
	    }
	}
   
	modelChanged(null);
    }

    ////////////////////////////////////////////////////////////////
    // event handlers

    /**
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#textEdited(org.tigris.gef.presentation.FigText)
     */
    protected void textEdited(FigText ft) {
	
        if (getOwner() == null) return;
	super.textEdited(ft);

	Collection conn = ModelFacade.getConnections(getOwner());
	if (conn == null || conn.size() == 0) return;

	if (ft == srcRole) {
	    Object srcAE = (conn.toArray())[0];
	    ModelFacade.setName(srcAE, srcRole.getText());
	} else if (ft == destRole) {
	    Object destAE = (conn.toArray())[1];
	    ModelFacade.setName(destAE, destRole.getText());
	} else if (ft == srcMult) {
	    Object srcAE = (conn.toArray())[0];
	    Object multi = Model.getUmlFactory().getDataTypes()
			   .createMultiplicity(srcMult.getText());
	    ModelFacade.setMultiplicity(srcAE, multi);
	} else if (ft == destMult) {
	    Object destAE = (conn.toArray())[1];
	    Object multi = Model.getUmlFactory().getDataTypes()
			   .createMultiplicity(destMult.getText());
	    ModelFacade.setMultiplicity(destAE, multi);
	}
    }
  
    private void updateEnd(FigText multiToUpdate, FigText roleToUpdate,
			   FigText orderingToUpdate,
			   Object end) {
                               
        if (!ModelFacade.isAAssociationEnd(end)) {
            throw new IllegalArgumentException();
	}
        
	Object multi = ModelFacade.getMultiplicity(end);
	String name = ModelFacade.getName(end);
	Object order = ModelFacade.getOrdering(end);
        String visi = ""; 
        Object stereo = null;
        if (ModelFacade.isNavigable(end)
	    && (ModelFacade.isAClass(ModelFacade.getType(end))
		|| ModelFacade.isAInterface(ModelFacade.getType(end)))) {
	    visi = Notation.generate(this, ModelFacade.getVisibility(end));
        }
        if (ModelFacade.getStereotypes(end).size() > 0) {
            stereo = ModelFacade.getStereotypes(end).iterator().next();
        }
    
	multiToUpdate.setText(Notation.generate(this, multi));
	orderingToUpdate.setText(getOrderingName(order));
	if (stereo != null) {
	    roleToUpdate.setText(Notation.generate(this, stereo)
				 + " " + visi
				 + Notation.generate(this, name));
	} else
	    roleToUpdate.setText(visi + Notation.generate(this, name));
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    protected void modelChanged(PropertyChangeEvent e) {
	super.modelChanged(e);
	Object associationEnd = getOwner(); //MAssociation
	if (associationEnd == null || getLayer() == null) return;
        
        if (e == null || e.getPropertyName().equals("isAbstract")) {
            updateAbstract();
            damage();
        }
    
	//MAssociationEnd
	Object ae0 =
	    ((ModelFacade.getConnections(associationEnd)).toArray())[0];
	//MAssociationEnd
	Object ae1 =
	    ((ModelFacade.getConnections(associationEnd)).toArray())[1];
	updateEnd(srcMult, srcRole, srcOrdering, ae0);
	updateEnd(destMult, destRole, destOrdering, ae1);
    
	boolean srcNav = ModelFacade.isNavigable(ae0);
	boolean destNav = ModelFacade.isNavigable(ae1);
	if (srcNav && destNav && SUPPRESS_BIDIRECTIONAL_ARROWS) {
	    srcNav = false;
	    destNav = false;
	}
	sourceArrowHead =
	    chooseArrowHead(ModelFacade.getAggregation(ae0), srcNav);
	destArrowHead =
	    chooseArrowHead(ModelFacade.getAggregation(ae1), destNav);
	setSourceArrowHead(sourceArrowHead);
	setDestArrowHead(destArrowHead);
	srcGroup.calcBounds();
	destGroup.calcBounds();
	middleGroup.calcBounds();
	this.computeRoute();
    }

    private static final ArrowHead NAV_AGGREGATE =
	new ArrowHeadComposite(ArrowHeadDiamond.WhiteDiamond,
			       new ArrowHeadGreater());

    private static final ArrowHead NAV_COMP =
	new ArrowHeadComposite(ArrowHeadDiamond.BlackDiamond,
			       new ArrowHeadGreater());

    /**
     * @param ak Object of type AggregationKind
     * @param nav the result of a ModelFacade.isNavigable(AssociationEnd)
     * @return the ArrowHead chosen
     */
    private ArrowHead chooseArrowHead(Object ak, boolean nav) {
        
	ArrowHead arrow = ArrowHeadNone.TheInstance;

	if (nav) {
	    if (ModelFacade.NONE_AGGREGATIONKIND.equals(ak) || (ak == null)) {
		arrow = new ArrowHeadGreater();
            } else if (ModelFacade.AGGREGATE_AGGREGATIONKIND.equals(ak)) {
		arrow = NAV_AGGREGATE;
            } else if (ModelFacade.COMPOSITE_AGGREGATIONKIND.equals(ak)) {
		arrow = NAV_COMP;
            }
	} else {
	    if (ModelFacade.NONE_AGGREGATIONKIND.equals(ak) || (ak == null)) {
		arrow = ArrowHeadNone.TheInstance;
	    } else if (ModelFacade.AGGREGATE_AGGREGATIONKIND.equals(ak)) {
		arrow = ArrowHeadDiamond.WhiteDiamond;
	    } else if (ModelFacade.COMPOSITE_AGGREGATIONKIND.equals(ak)) {
		arrow = ArrowHeadDiamond.BlackDiamond;
	    }
	}
	return arrow;
    }

    /**
     * @see org.tigris.gef.ui.PopupGenerator#getPopUpActions(java.awt.event.MouseEvent)
     */
    public Vector getPopUpActions(MouseEvent me) {
	Vector popUpActions = super.getPopUpActions(me);
	// x^2 + y^2 = r^2  (equation of a circle)
	Point firstPoint = this.getFirstPoint();
	Point lastPoint = this.getLastPoint();
	int length = getPerimeterLength();

	int rSquared = (int) (.3 * length);

	// max distance is set at 100 pixels, (rSquared = 100^2)
	if ( rSquared > 100 ) {
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
                popUpActions.size() - POPUP_ADD_OFFSET);

            ArgoJMenu aggMenu = new ArgoJMenu("menu.popup.aggregation");
        
	    aggMenu.add(ActionAggregation.getSrcAggNone());
	    aggMenu.add(ActionAggregation.getSrcAgg());
	    aggMenu.add(ActionAggregation.getSrcAggComposite());
	    popUpActions.insertElementAt(aggMenu, 
					 (popUpActions.size()
					  - POPUP_ADD_OFFSET));
	}
	else if (destDeterminingFactor < rSquared) {
            ArgoJMenu multMenu =
		new ArgoJMenu("menu.popup.multiplicity");
	    multMenu.add(ActionMultiplicity.getDestMultOne());
	    multMenu.add(ActionMultiplicity.getDestMultZeroToOne());
	    multMenu.add(ActionMultiplicity.getDestMultOneToMany());
	    multMenu.add(ActionMultiplicity.getDestMultZeroToMany());
	    popUpActions.insertElementAt(multMenu, 
					 (popUpActions.size()
					  - POPUP_ADD_OFFSET));

            ArgoJMenu aggMenu = new ArgoJMenu("menu.popup.aggregation");
	    aggMenu.add(ActionAggregation.getDestAggNone());
	    aggMenu.add(ActionAggregation.getDestAgg());
	    aggMenu.add(ActionAggregation.getDestAggComposite());
	    popUpActions.insertElementAt(aggMenu, 
					 (popUpActions.size()
					  - POPUP_ADD_OFFSET));
	}
	// else: No particular options for right click in middle of line

	// Options available when right click anywhere on line
	Object association = getOwner();
	if (association != null) {
	    // Navigability menu with suboptions built dynamically to
	    // allow navigability from atart to end, from end to start
	    // or bidirectional
	    Collection ascEnds = ModelFacade.getConnections(association);
            Iterator iter = ascEnds.iterator();
	    Object ascStart = iter.next();
	    Object ascEnd = iter.next();

	    if (ModelFacade.isAClassifier(ModelFacade.getType(ascStart))
                    && ModelFacade.isAClassifier(ModelFacade.getType(ascEnd))) {
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
					      - POPUP_ADD_OFFSET));
	    }
	}

	return popUpActions;
    }

    /** Returns the name of the OrderingKind.
     * @return "{ordered}", "{sorted}" or "" if null or "unordered"
     */
    private String getOrderingName(Object orderingKind) {
	if (orderingKind == null) return "";
	if (ModelFacade.getName(orderingKind) == null) return ""; 
	if ("".equals(ModelFacade.getName(orderingKind))) return "";
	if ("unordered".equals(ModelFacade.getName(orderingKind))) return "";
        
	return "{" + ModelFacade.getName(orderingKind) + "}";
    }

    /**
     * Updates the name if modelchanged receives an "isAbstract" event
     */
    protected void updateAbstract() {
        Rectangle rect = getBounds();
        if (getOwner() == null)
            return;
        Object assoc =  getOwner();
        if (ModelFacade.isAbstract(assoc))
            getNameFig().setFont(getItalicLabelFont());
        else
            getNameFig().setFont(getLabelFont());
        super.updateNameText();
        setBounds(rect.x, rect.y, rect.width, rect.height);
    }
    
    static final long serialVersionUID = 9100125695919853919L;
  
    /**
     * @see org.tigris.gef.presentation.Fig#paint(java.awt.Graphics)
     */
    public void paint(Graphics g) {
        if (sourceArrowHead == null || destArrowHead == null) {
	    modelChanged(null);
        }
        if (sourceArrowHead != null && destArrowHead != null) {
	    sourceArrowHead.setLineColor(getLineColor());
	    destArrowHead.setLineColor(getLineColor());   
        }
        super.paint(g);
    }

    /**
     * @return Returns the middleGroup.
     */
    protected FigTextGroup getMiddleGroup() {
        return middleGroup;
    }

} /* end class FigAssociation */
