// Copyright (c) 1996-99 The Regents of the University of California. All
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

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import javax.swing.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;

import org.tigris.gef.base.*;
import org.tigris.gef.presentation.*;

import org.argouml.application.api.*;
import org.argouml.uml.generator.*;
import org.argouml.ui.*;
import org.argouml.uml.ui.*;


public class FigAssociation extends FigEdgeModelElement {
    
   

  // needs-more-work: should be part of some preferences object
  public static boolean SUPPRESS_BIDIRECTIONAL_ARROWS = true;

  /**
   * Group for the FigTexts concerning the source association end
   */
  protected FigTextGroup _srcGroup = new FigTextGroup();
  /**
   * Group for the FigTexts concerning the dest association end
   */
  protected FigTextGroup _destGroup = new FigTextGroup();
  /**
   * Group for the FigTexts concerning the name and stereotype of the 
   * association itself.
   */
  protected FigTextGroup _middleGroup = new FigTextGroup();
    
  protected FigText _srcMult, _srcRole;
  protected FigText _destMult, _destRole;
  protected FigText _srcOrdering, _destOrdering;

  protected ArrowHead sourceArrowHead, destArrowHead;

  ////////////////////////////////////////////////////////////////
  // constructors

  public FigAssociation() {
    // lets use groups to construct the different text sections at the association
    _middleGroup.addFig(_name);
    _middleGroup.addFig(_stereo);
    addPathItem(_middleGroup, new PathConvPercent(this, 50, 10));
    
    _srcMult = new FigText(10, 10, 90, 20);
    _srcMult.setFont(LABEL_FONT);
    _srcMult.setTextColor(Color.black);
    _srcMult.setTextFilled(false);
    _srcMult.setFilled(false);
    _srcMult.setLineWidth(0);
    _srcMult.setJustification(FigText.JUSTIFY_CENTER);

    _srcRole = new FigText(10, 10, 90, 20);
    _srcRole.setFont(LABEL_FONT);
    _srcRole.setTextColor(Color.black);
    _srcRole.setTextFilled(false);
    _srcRole.setFilled(false);
    _srcRole.setLineWidth(0);
    _srcRole.setJustification(FigText.JUSTIFY_CENTER);

    _srcOrdering = new FigText(10,10,90,20);
    _srcOrdering.setFont(LABEL_FONT);
    _srcOrdering.setTextColor(Color.black);
    _srcOrdering.setTextFilled(false);
    _srcOrdering.setFilled(false);
    _srcOrdering.setLineWidth(0);
    _srcOrdering.setJustification(FigText.JUSTIFY_CENTER);

    _srcGroup.addFig(_srcRole);
    _srcGroup.addFig(_srcOrdering);
    _srcGroup.addFig(_srcMult);    
    addPathItem(_srcGroup, new PathConvPercent(this, 15, 0));
    
    _destMult = new FigText(10, 10, 90, 20);
    _destMult.setFont(LABEL_FONT);
    _destMult.setTextColor(Color.black);
    _destMult.setTextFilled(false);
    _destMult.setFilled(false);
    _destMult.setLineWidth(0);
    _destMult.setJustification(FigText.JUSTIFY_CENTER);
    
    _destRole = new FigText(0, 0, 90, 20);
    _destRole.setFont(LABEL_FONT);
    _destRole.setTextColor(Color.black);
    _destRole.setTextFilled(false);
    _destRole.setFilled(false);
    _destRole.setLineWidth(0);
    _destRole.setJustification(FigText.JUSTIFY_CENTER);
    
    _destOrdering = new FigText(0,0,90,20);
    _destOrdering.setFont(LABEL_FONT);
    _destOrdering.setTextColor(Color.black);
    _destOrdering.setTextFilled(false);
    _destOrdering.setFilled(false);
    _destOrdering.setLineWidth(0);
    _destOrdering.setJustification(FigText.JUSTIFY_CENTER);

    _destGroup.addFig(_destRole);
    _destGroup.addFig(_destMult);
    _destGroup.addFig(_destOrdering);
      addPathItem(_srcGroup, new PathConvPercent(this, 85, 0));
    
    setBetweenNearestPoints(true);
  }

    /*
  public FigAssociation(Object edge) {
    this(edge, null);   
  }
  */
  
  public FigAssociation(Object edge, Layer lay) {
    this();
    setLayer(lay);
    setOwner(edge);
  }

  public void setOwner(Object own) {
    Object oldOwner = getOwner();
    super.setOwner(own);
    if (oldOwner instanceof MAssociation) {
	MAssociation oldAsc = (MAssociation)oldOwner;
	for (int i = 0; i < oldAsc.getConnections().size(); i++)
	    ((MAssociationEnd)((Object[]) oldAsc.getConnections().toArray())[i]).removeMElementListener(this);

	oldAsc.removeMElementListener(this);
    }

    if (own instanceof MAssociation) {
	MAssociation newAsc = (MAssociation)own;
	for (int i = 0; i < newAsc.getConnections().size(); i++)
	    ((MAssociationEnd)((Object[]) newAsc.getConnections().toArray())[i]).addMElementListener(this);

      newAsc.addMElementListener(this);
    }
    modelChanged();
  }

  ////////////////////////////////////////////////////////////////
  // event handlers

  protected void textEdited(FigText ft) throws PropertyVetoException {
    MAssociation asc = (MAssociation) getOwner();
    if (asc == null) return;
    super.textEdited(ft);

    Collection conn = asc.getConnections();
    if (conn == null || conn.size() == 0) return;

    if (ft == _srcRole) {
      MAssociationEnd srcAE = (MAssociationEnd)((Object[]) conn.toArray())[0];
      srcAE.setName(_srcRole.getText());
    }
    if (ft == _destRole) {
      MAssociationEnd destAE = (MAssociationEnd) ((Object[]) conn.toArray())[1];
      destAE.setName(_destRole.getText());
    }
    // needs-more-work: parse multiplicities
  }


  protected void modelChanged() {
    MAssociation as = (MAssociation) getOwner();
    if (as == null) return;
    String asNameStr = Notation.generate(this, as.getName());

    super.modelChanged();

    MAssociationEnd ae0 =  (MAssociationEnd)((Object[])(as.getConnections()).toArray())[0];
    MAssociationEnd ae1 =  (MAssociationEnd)((Object[])(as.getConnections()).toArray())[1];
    
    Fig oldDest = getDestFigNode();
    Fig oldSource = getSourceFigNode();
    
    setDestFigNode((FigNode)getLayer().presentationFor(ae0.getType()));
    setDestPortFig(getLayer().presentationFor(ae0.getType()));
    setSourceFigNode((FigNode)getLayer().presentationFor(ae1.getType())); 
    setSourcePortFig(getLayer().presentationFor(ae1.getType()));  
    
    MMultiplicity mult0 = ae0.getMultiplicity();
    MMultiplicity mult1 = ae1.getMultiplicity();
    _srcMult.setText(Notation.generate(this, mult0));
    // 2002-07-22
    // Jaap Branderhorst
    // changed next line to show 1..1 multiplicity. Old code
    // if ((mult0 == null) || (MMultiplicity.M1_1).equals(mult0)) _srcMult.setText("");
    // new code
    if (mult0 == null) _srcMult.setText("");
    _destMult.setText(Notation.generate(this, mult1));
   // 2002-07-22
    // Jaap Branderhorst
    // changed next line to show 1..1 multiplicity. Old code
    // if ((mult1 == null) || (MMultiplicity.M1_1).equals(mult1)) _srcMult.setText("");
    // new code
    if (mult1 == null) _srcMult.setText("");

    _srcRole.setText(Notation.generate(this, ae0.getName()));
    _destRole.setText(Notation.generate(this, ae1.getName()));

    // now add the stereotypes on associatenends if desired
    // need more work!

    // this should be configurable
    if (true == true) {

        if (ae0.getStereotype() != null)
            _srcRole.setText(Notation.generateStereotype(this,
                                                         ae0.getStereotype()) +
                             " " + _srcRole.getText());
        if (ae1.getStereotype() != null)
            _destRole.setText(Notation.generateStereotype(this,
                                                         ae1.getStereotype()) +
                              " " + _destRole.getText());
    }

    if (true == true ) {
       _srcOrdering.setText(getOrderingName(ae0.getOrdering()));
       _destOrdering.setText(getOrderingName(ae1.getOrdering()));
    }
    
    
    

    boolean srcNav = ae0.isNavigable();
    boolean destNav = ae1.isNavigable();
    if (srcNav && destNav && SUPPRESS_BIDIRECTIONAL_ARROWS)
      srcNav = destNav = false;
    sourceArrowHead = chooseArrowHead(ae0.getAggregation(), srcNav);
    destArrowHead = chooseArrowHead(ae1.getAggregation(), destNav);
    setSourceArrowHead(sourceArrowHead);
    setDestArrowHead(destArrowHead);
    _srcGroup.calcBounds();
    _destGroup.calcBounds();
    _middleGroup.calcBounds();
    Object oldOwner = ProjectBrowser.TheInstance.getTarget();
    ProjectBrowser.TheInstance.setTarget(getOwner());
    ProjectBrowser.TheInstance.setTarget(oldOwner);
    
  }

  

  static ArrowHead _NAV_AGGREGATE =
  new ArrowHeadComposite(ArrowHeadDiamond.WhiteDiamond,
			 ArrowHeadGreater.TheInstance);

  static ArrowHead _NAV_COMP =
  new ArrowHeadComposite(ArrowHeadDiamond.BlackDiamond,
			 ArrowHeadGreater.TheInstance);

  protected ArrowHead chooseArrowHead(MAggregationKind ak, boolean nav) {
      ArrowHead arrow = ArrowHeadNone.TheInstance;

      if (nav) {
	  if (MAggregationKind.NONE.equals(ak) || (ak == null))
	      arrow = ArrowHeadGreater.TheInstance;
	  else if (MAggregationKind.AGGREGATE.equals(ak))
              arrow = _NAV_AGGREGATE;
	  else if (MAggregationKind.COMPOSITE.equals(ak))
              arrow = _NAV_COMP;
      }
      else {
          if (MAggregationKind.NONE.equals(ak) || (ak == null)) {
              arrow = ArrowHeadNone.TheInstance;
          }
          else if (MAggregationKind.AGGREGATE.equals(ak)) {
              arrow = ArrowHeadDiamond.WhiteDiamond;
          }
          else if (MAggregationKind.COMPOSITE.equals(ak)) {
              arrow = ArrowHeadDiamond.BlackDiamond;
          }
      }
      return arrow;
  }

  public Vector getPopUpActions(MouseEvent me) {
    Vector popUpActions = super.getPopUpActions(me);
    // x^2 + y^2 = r^2  (equation of a circle)
    Point firstPoint = this.getFirstPoint();
    Point lastPoint = this.getLastPoint();
    int length = getPerimeterLength();

    int rSquared = (int) (.3 * length);

    // max distance is set at 100 pixels, (rSquared = 100^2)
    if( rSquared > 100 )
      rSquared = 10000;
    else
      rSquared *= rSquared;

    int srcDeterminingFactor = getSquaredDistance(me.getPoint(), firstPoint);
    int destDeterminingFactor = getSquaredDistance(me.getPoint(), lastPoint);

    if (srcDeterminingFactor < rSquared &&
	srcDeterminingFactor < destDeterminingFactor) {
      JMenu multMenu = new JMenu("Multiplicity");
      multMenu.add(ActionMultiplicity.SrcMultOne);
      multMenu.add(ActionMultiplicity.SrcMultZeroToOne);
      multMenu.add(ActionMultiplicity.SrcMultOneToMany);
      multMenu.add(ActionMultiplicity.SrcMultZeroToMany);
      popUpActions.insertElementAt(multMenu, popUpActions.size() - 1);

      JMenu aggMenu = new JMenu("Aggregation");
      aggMenu.add(ActionAggregation.SrcAggNone);
      aggMenu.add(ActionAggregation.SrcAgg);
      aggMenu.add(ActionAggregation.SrcAggComposite);
      popUpActions.insertElementAt(aggMenu, popUpActions.size() - 1);
    }
    else if (destDeterminingFactor < rSquared) {
      JMenu multMenu = new JMenu("Multiplicity");
      multMenu.add(ActionMultiplicity.DestMultOne);
      multMenu.add(ActionMultiplicity.DestMultZeroToOne);
      multMenu.add(ActionMultiplicity.DestMultOneToMany);
      multMenu.add(ActionMultiplicity.DestMultZeroToMany);
      popUpActions.insertElementAt(multMenu, popUpActions.size() - 1);

      JMenu aggMenu = new JMenu("Aggregation");
      aggMenu.add(ActionAggregation.DestAggNone);
      aggMenu.add(ActionAggregation.DestAgg);
      aggMenu.add(ActionAggregation.DestAggComposite);
      popUpActions.insertElementAt(aggMenu, popUpActions.size() - 1);
    }
    else {
        // No particular options for right click in middle of line
    }

    // Options available when right click anywhere on line (added by BobTarling 7-Jan-2002)
    MAssociation asc = (MAssociation) getOwner();
    if (asc != null) {
        // Navigability menu with suboptions built dynamically to allow navigability
        // from atart to end, from end to start or bidirectional

        java.util.List ascEnds = ((MAssociation) asc).getConnections();
        MAssociationEnd ascStart = (MAssociationEnd) (ascEnds.get(0));
        MAssociationEnd ascEnd = (MAssociationEnd) (ascEnds.get(1));

        if (ascStart.getType() instanceof MClassifier && ascEnd.getType() instanceof MClassifier) {
            JMenu navMenu = new JMenu("Navigability");
            navMenu.add(ActionNavigability.newActionNavigability(ascStart, ascEnd, ActionNavigability.BIDIRECTIONAL));
            navMenu.add(ActionNavigability.newActionNavigability(ascStart, ascEnd, ActionNavigability.STARTTOEND));
            navMenu.add(ActionNavigability.newActionNavigability(ascStart, ascEnd, ActionNavigability.ENDTOSTART));

            popUpActions.insertElementAt(navMenu, popUpActions.size() - 1);
        }
    }


    return popUpActions;
  }

    /* returns the name of the OrderingKind.
     * @return "{ordered}", "{sorted}" or "" if null or "unordered"
     */
    private String getOrderingName(MOrderingKind ok) {
	if (ok == null) return "";
	if (ok.getName() == null) return "";
	if ("unordered".equals(ok.getName())) return "";
	return "{" + ok.getName() + "}";
    }

  static final long serialVersionUID = 9100125695919853919L;
  
     public void paint(Graphics g) {
        sourceArrowHead.setLineColor(getLineColor());
        destArrowHead.setLineColor(getLineColor());   
        super.paint(g);
     }

} /* end class FigAssociation */

/**
 * Custom class to group FigTexts in such a way that they don't overlap and that 
 * the group is shrinked to fit (no whitespace in group).
 * 
 * @author jaap.branderhorst@xs4all.nl
 */
class FigTextGroup extends FigGroup {
	 public final static int ROWHEIGHT = 17;
     protected boolean supressCalcBounds = false;

	/**
     * Adds a FigText to the list with figs. Makes sure that the figtexts do not overlap.
	 * @see org.tigris.gef.presentation.FigGroup#addFig(Fig)
	 */
	public void addFig(FigText f) {
		super.addFig(f);
        updateFigTexts();
        calcBounds();
	}
    
	/**
	 * Updates the FigTexts. FigTexts without text (equals "") are not shown. 
     * The rest of the figtexts are shown non-overlapping. The first figtext 
     * added (via addFig) is shown at the bottom of the FigTextGroup.
	 */
    protected void updateFigTexts() {
        Iterator it = getFigs().iterator();
        int height = 0;
        while (it.hasNext()) {
            FigText fig = (FigText)it.next();
            if (fig.getText().equals("")) {
                fig.setHeight(0);
            } else {
                fig.setHeight(ROWHEIGHT);
            }
            fig.startTrans();
            fig.setX(getX());
            fig.setY(getY()+height);
            fig.endTrans();
            height += fig.getHeight();
        }
        // calcBounds();
    }
            

	/**
	 * @see org.tigris.gef.presentation.Fig#calcBounds()
	 */
	public void calcBounds() {
        if (!supressCalcBounds) {
    		super.calcBounds();
            // get the widest of all textfigs
            // calculate the total height
            int maxWidth = 0;
            int height = 0;
            for (int i = 0;i < getFigs().size();i++) {
                FigText fig = (FigText)getFigs().get(i);
                if (fig.getText().equals("")) {
                    fig.setBounds(fig.getX(), fig.getY(), fig.getWidth(), 0);
                } 
                else {
                    if (fig.getWidth() > maxWidth) {
                        maxWidth = fig.getWidth();
                    }
                    if (!fig.getText().equals("")) {
                        fig.setHeight(ROWHEIGHT);
                    }
                    height += fig.getHeight();
                }
            }        
            _w = maxWidth;
            _h = height;
        }
	}   
    
	/**
	 * @see org.tigris.gef.presentation.Fig#paint(Graphics)
	 */
	public void paint(Graphics g) {
		super.paint(g);
        updateFigTexts();
	}

}



