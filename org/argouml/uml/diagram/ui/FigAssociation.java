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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.AddAssociationEvent;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.notation.NotationProvider4;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.ui.ArgoJMenu;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.notation.uml.NotationUtilityUml;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.PathConvPercentPlusConst;
import org.tigris.gef.presentation.ArrowHead;
import org.tigris.gef.presentation.ArrowHeadComposite;
import org.tigris.gef.presentation.ArrowHeadDiamond;
import org.tigris.gef.presentation.ArrowHeadGreater;
import org.tigris.gef.presentation.ArrowHeadNone;
import org.tigris.gef.presentation.FigText;


/**
 * This class represents the Fig of a binary association on a diagram.
 *
 */
public class FigAssociation extends FigEdgeModelElement {

    // TODO: should be part of some preferences object
    private static final boolean SUPPRESS_BIDIRECTIONAL_ARROWS = true;

    /**
     * Group for the FigTexts concerning the source association end.
     */
    private FigTextGroup srcGroup = new FigTextGroup();

    /**
     * Group for the FigTexts concerning the dest association end.
     */
    private FigTextGroup destGroup = new FigTextGroup();

    /**
     * Group for the FigTexts concerning the name and stereotype of the
     * association itself.
     */
    private FigTextGroup middleGroup = new FigTextGroup();

    private FigText srcMult, srcRole;
    private FigText destMult, destRole;
    private FigText srcOrdering, destOrdering;

    private ArrowHead sourceArrowHead, destArrowHead;
    
    protected NotationProvider4 notationProviderSrcRole;
    protected NotationProvider4 notationProviderDestRole;
    
    private static final Logger LOG = Logger.getLogger(FigAssociation.class);


    /**
     * Main constructor
     *
     * Don't call this constructor directly. It is public since this
     * is necessary for loading. Use the FigAssociation(Object, Layer)
     * constructor instead!
     */
    public FigAssociation() {
        super();

        // let's use groups to construct the different text sections at
        // the association
        middleGroup.addFig(getNameFig());
        middleGroup.addFig(getStereotypeFig());
        addPathItem(middleGroup,
                new PathConvPercent2(this, middleGroup, 50, 25));

        srcMult = new FigText(10, 10, 90, 20);
        srcMult.setFont(getLabelFont());
        srcMult.setTextColor(Color.black);
        srcMult.setTextFilled(false);
        srcMult.setFilled(false);
        srcMult.setLineWidth(0);
        srcMult.setReturnAction(FigText.END_EDITING);
        srcMult.setJustification(FigText.JUSTIFY_CENTER);

        srcRole = new FigText(10, 10, 90, 20);
        srcRole.setFont(getLabelFont());
        srcRole.setTextColor(Color.black);
        srcRole.setTextFilled(false);
        srcRole.setFilled(false);
        srcRole.setLineWidth(0);
        srcRole.setReturnAction(FigText.END_EDITING);
        srcRole.setJustification(FigText.JUSTIFY_CENTER);

        srcOrdering = new FigText(10, 10, 90, 20);
        srcOrdering.setFont(getLabelFont());
        srcOrdering.setTextColor(Color.black);
        srcOrdering.setTextFilled(false);
        srcOrdering.setFilled(false);
        srcOrdering.setLineWidth(0);
        srcOrdering.setReturnAction(FigText.END_EDITING);
        srcOrdering.setJustification(FigText.JUSTIFY_CENTER);
        srcOrdering.setEditable(false); // parsing not (yet) implemented

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
        destMult.setReturnAction(FigText.END_EDITING);
        destMult.setJustification(FigText.JUSTIFY_CENTER);

        destRole = new FigText(0, 0, 90, 20);
        destRole.setFont(getLabelFont());
        destRole.setTextColor(Color.black);
        destRole.setTextFilled(false);
        destRole.setFilled(false);
        destRole.setLineWidth(0);
        destRole.setReturnAction(FigText.END_EDITING);
        destRole.setJustification(FigText.JUSTIFY_CENTER);

        destOrdering = new FigText(0, 0, 90, 20);
        destOrdering.setFont(getLabelFont());
        destOrdering.setTextColor(Color.black);
        destOrdering.setTextFilled(false);
        destOrdering.setFilled(false);
        destOrdering.setLineWidth(0);
        destOrdering.setReturnAction(FigText.END_EDITING);
        destOrdering.setJustification(FigText.JUSTIFY_CENTER);
        destOrdering.setEditable(false); // parsing not (yet) implemented

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
     * Constructor that hooks the Fig to an existing UML element.
     *
     * @param edge the UMl element
     * @param lay the layer
     */
    public FigAssociation(Object edge, Layer lay) {
        this();
        setLayer(lay);
        setOwner(edge);
    }

    /**
     * @see org.argouml.uml.diagram.state.ui.FigStateVertex#initNotationProviders(java.lang.Object)
     */
    protected void initNotationProviders(Object own) {
        super.initNotationProviders(own);
        if (Model.getFacade().isAAssociation(own)) {
            Object[] ends = 
                Model.getFacade().getConnections(own).toArray(); 
            Object ae0 = ends[0];
            Object ae1 = ends[1];
            notationProviderSrcRole =
                NotationProviderFactory2.getInstance().getNotationProvider(
                        NotationProviderFactory2.TYPE_ASSOCIATION_END_NAME, 
                        ae0);
            notationProviderDestRole =
                NotationProviderFactory2.getInstance().getNotationProvider(
                        NotationProviderFactory2.TYPE_ASSOCIATION_END_NAME, 
                        ae1);
        }
    }
    
    /**
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#updateListeners(java.lang.Object)
     */
    public void updateListeners(Object oldOwner, Object newOwner) {
        if (oldOwner != null) {
            removeAllElementListeners();
        }
        /* Now, let's register for events from all modelelements
         * that change the association representation: 
         */
        if (newOwner != null) {
            /* Many different event types are needed, 
             * so let's register for them all: */
            addElementListener(newOwner);
            /* Now let's collect related elements: */
            ArrayList connections = new ArrayList();
            connections.addAll(Model.getFacade().getStereotypes(newOwner));
            Collection ends = Model.getFacade().getConnections(newOwner);
            connections.addAll(ends);
            for (Iterator i1 = ends.iterator(); i1.hasNext();) {
                connections.addAll(Model.getFacade().getStereotypes(i1.next()));
            }
            for (Iterator i = connections.iterator(); i.hasNext();) {
                addElementListener(i.next());
            }
        }
    }

    // //////////////////////////////////////////////////////////////
    // event handlers

    /**
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

	if (ft == srcRole) {
            ft.setText(notationProviderSrcRole.parse(ft.getText()));
	} else if (ft == destRole) {
            ft.setText(notationProviderDestRole.parse(ft.getText()));
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
	        srcMult.setText(Model.getFacade().toString(
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
                srcMult.setText(Model.getFacade().toString(
                        Model.getFacade().getMultiplicity(destAE)));
	    }
	}
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#textEditStarted(org.tigris.gef.presentation.FigText)
     */
    protected void textEditStarted(FigText ft) {
        if (ft == getNameFig()) {
            showHelp("parsing.help.fig-association-name");
        } else if (ft == srcRole) {
            showHelp(notationProviderSrcRole.getParsingHelp());
        } else if (ft == destRole) {
            showHelp(notationProviderDestRole.getParsingHelp());
        } else if (ft == srcMult) {
            showHelp("parsing.help.fig-association-source-multiplicity");
        } else if (ft == destMult) {
            showHelp("parsing.help.fig-association-destination-multiplicity");
        }
    }

    private void updateEnd(FigText multiToUpdate,
			   FigText orderingToUpdate,
			   Object end) {

        if (!Model.getFacade().isAAssociationEnd(end)) {
            throw new IllegalArgumentException();
	}

	Object multi = Model.getFacade().getMultiplicity(end);
	Object order = Model.getFacade().getOrdering(end);

	multiToUpdate.setText(NotationUtilityUml.generateMultiplicity(multi));
	orderingToUpdate.setText(getOrderingName(order));
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent e) {
        if ("stereotype".equals(e.getPropertyName()) && middleGroup != null) {
            if (e instanceof AddAssociationEvent) {
                addElementListener(e.getNewValue());
            } else if (e instanceof RemoveAssociationEvent) {
                removeElementListener(e.getOldValue());
            }
        }
        super.propertyChange(e);
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    protected void modelChanged(PropertyChangeEvent e) {
	super.modelChanged(e);
	if (getOwner() == null || getLayer() == null) {
	    return;
	}
        renderingChanged();
        updateListeners(getOwner(), getOwner());
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#renderingChanged()
     */
    public void renderingChanged() {
        Object association = getOwner();
        if (association == null) {
            return;
        }
        Collection ends = Model.getFacade().getConnections(association);
        if (ends.size() < 2) {
            return;
        }
        updateAbstract();
        updateEnds(association);
        chooseArrowHeads(association);
        srcGroup.calcBounds();
        destGroup.calcBounds();
        middleGroup.calcBounds();
        super.renderingChanged(); // last, since it calls damage()
        computeRoute(); //very last, since otherwise exception :-(
    }

    /**
     * Update the annotations on each end.
     * 
     * @param association
     */
    private void updateEnds(Object association) {
        Object[] ends = 
            Model.getFacade().getConnections(association).toArray();
        if (ends.length >= 2) {
            Object ae0 = ends[0];
            Object ae1 = ends[1];
            updateEnd(srcMult, srcOrdering, ae0);
            if (notationProviderSrcRole != null) {
                srcRole.setText(notationProviderSrcRole.toString());
            }
            updateEnd(destMult, destOrdering, ae1);
            if (notationProviderDestRole != null) {
                destRole.setText(notationProviderDestRole.toString());
            }
        }
    }

    /**
     * Choose the arrowhead style for each end.
     * 
     * @param association
     */
    private void chooseArrowHeads(Object association) {
        assert association != null;
        Object[] ends = 
            Model.getFacade().getConnections(association).toArray(); 
        Object ae0 = ends[0];
        Object ae1 = ends[1];

        boolean srcNav = Model.getFacade().isNavigable(ae0);
        boolean destNav = Model.getFacade().isNavigable(ae1);
        if (srcNav && destNav && SUPPRESS_BIDIRECTIONAL_ARROWS) {
            srcNav = false;
            destNav = false;
        }
        sourceArrowHead =
            chooseArrowHead(Model.getFacade().getAggregation(ae0), srcNav);
        destArrowHead =
            chooseArrowHead(Model.getFacade().getAggregation(ae1), destNav);
        setSourceArrowHead(sourceArrowHead);
        setDestArrowHead(destArrowHead);
    }
    
    private static final ArrowHead NAV_AGGREGATE =
	new ArrowHeadComposite(ArrowHeadDiamond.WhiteDiamond,
			       new ArrowHeadGreater());

    private static final ArrowHead NAV_COMP =
	new ArrowHeadComposite(ArrowHeadDiamond.BlackDiamond,
			       new ArrowHeadGreater());

    /**
     * @param ak Object of type AggregationKind
     * @param nav the result of a Model.getFacade().isNavigable(AssociationEnd)
     * @return the ArrowHead chosen
     */
    private ArrowHead chooseArrowHead(Object ak, boolean nav) {

	ArrowHead arrow = ArrowHeadNone.TheInstance;

	if (nav) {
	    if (Model.getAggregationKind().getNone().equals(ak)
	            || (ak == null)) {
		arrow = new ArrowHeadGreater();
            } else if (Model.getAggregationKind().getAggregate()
                    .equals(ak)) {
		arrow = NAV_AGGREGATE;
            } else if (Model.getAggregationKind().getComposite()
                    .equals(ak)) {
		arrow = NAV_COMP;
            }
	} else {
	    if (Model.getAggregationKind().getNone().equals(ak)
	            || (ak == null)) {
		arrow = ArrowHeadNone.TheInstance;
	    } else if (Model.getAggregationKind().getAggregate()
	            .equals(ak)) {
		arrow = ArrowHeadDiamond.WhiteDiamond;
	    } else if (Model.getAggregationKind().getComposite()
	            .equals(ak)) {
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
     * Returns the name of the OrderingKind.
     *
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
     * Updates the name if modelchanged receives an "isAbstract" event.
     */
    protected void updateAbstract() {
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

    /**
     * @see org.tigris.gef.presentation.Fig#paint(java.awt.Graphics)
     */
    public void paint(Graphics g) {
        if (sourceArrowHead == null || destArrowHead == null) {
            if (getOwner() == null ) {
                LOG.error("Trying to paint a FigAssociation without an owner. ");
            } else {
                chooseArrowHeads(getOwner()); 
            }
        }
        if (sourceArrowHead != null && destArrowHead != null) {
	    sourceArrowHead.setLineColor(getLineColor());
	    destArrowHead.setLineColor(getLineColor());
        }
        super.paint(g);
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#paintClarifiers(java.awt.Graphics)
     */
    public void paintClarifiers(Graphics g) {
        indicateBounds(getNameFig(), g);
        indicateBounds(srcMult, g);
        indicateBounds(srcRole, g);
        indicateBounds(destMult, g);
        indicateBounds(destRole, g);
        super.paintClarifiers(g);
    }

    /**
     * @return Returns the middleGroup.
     */
    protected FigTextGroup getMiddleGroup() {
        return middleGroup;
    }

    /**
     * The serial version id.
     */
    static final long serialVersionUID = 9100125695919853919L;

} /* end class FigAssociation */
