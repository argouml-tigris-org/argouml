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



// File: FigNodeWithCompartments.java
// Classes: FigNodeWithCompartments
// Original Author: elefevre
// $Id$

package uci.uml.visual;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import uci.gef.*;
import uci.graph.*;
import uci.argo.kernel.*;
import uci.uml.ui.*;
import uci.uml.generate.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;

/** This class is a FigNodeModeleElement with compartments.<BR>
 *  i.e. abstract class to display diagram icons for UML ModelElements that
 *  look like nodes and that have editable names, can be
 *  resized, and have compartments that can be displayed or hidden. */

public abstract class FigNodeWithCompartments extends FigNodeModelElement {

  /** This one draws a rectangle surrounding the shape. */
  protected FigRect _bigPort;

  /** This contains the list of FigCompartment. */
  protected Vector _compartments = new Vector(); 

  ////////////////////////////////////////////////////////////////
  // constructors

  public FigNodeWithCompartments() {
    super();
  }

  /** Partially construct a new FigNodeWithCompartments.  This method creates the
   *  _name element that holds the name of the model element and adds
   *  itself as a listener. */
  public FigNodeWithCompartments(GraphModel gm, Object node) {
    super(gm, node);
  }

  ////////////////////////////////////////////////////////////////
  // Fig API

  /** Adds a FigCompartment to the Vector _compartments. */
  // added by Eric Lefevre 25 Mar 1999
  public void addCompartment(FigCompartment compartment) {
    addFig(compartment);     // I think we need this if we don't
                             // want to override a bunch of methods
    _compartments.addElement(compartment);
  }

  /** Returns the list of compartments. */
  // added by Eric Lefevre 25 Mar 1999
  public Vector getCompartments() { return _compartments; }

  /** Returns a list of the displayable Figs enclosed. 
   *  e.g. returns the list of enclosed Figs, without
   *  the Compartments that should not be displayed. */
  // added by Eric Lefevre 25 Mar 1999
  public Vector getDisplayedFigs() { 
    Vector displayed = (Vector)getFigs().clone();

    FigCompartment compartment;
    for (int i=0; i<_compartments.size(); i++) {
      compartment = (FigCompartment)_compartments.elementAt(i);
      // removes the compartments that are not to be displayed
      if ( !(compartment).isDisplayed() ) {
        displayed.removeElement((Object)compartment);
      }
    }

    return displayed;
  }

  /** Returns a list of the regular Figs enclosed, without the
   *  FigCompartments, or the FigRect. */
  // added by Eric Lefevre 25 Mar 1999
  public Vector getRegularFigs() {
    Vector figs = (Vector)getFigs().clone(); // was getEnclosedFigs()
    figs.removeElement(_bigPort);
    for (int i=0; i<getCompartments().size(); i++ )
      figs.removeElement(getCompartments().elementAt(i));
    return figs;
  }

  // added by Eric Lefevre 12 Apr 1999
  public void setPort(FigRect port) {
    removeFig(_bigPort);
    _bigPort = port;
    addFig(_bigPort);
  }

  /** Returns the surrounding rectangle. */
  // added by Eric Lefevre 12 Apr 1999
  public FigRect getPort() { return _bigPort; }

  /* Overrides setBounds to keep shapes looking right */
  // added by Eric Lefevre 13 Mar 1999
  public void setBounds(int x, int y, int w, int h) {
    Rectangle oldBounds = getBounds();
    Fig fig;

    // first, get the height of all "non-compartment" figs
    // these are non-hideable, and non-expandable
    int cumulatedHeight = 0;
    int height;
    Vector basicFigs = getRegularFigs();
    for (int i=0; i<basicFigs.size(); i++) {
      fig = (Fig)basicFigs.elementAt(i);
      height = fig.getMinimumSize().height;
      fig.setBounds(x, y+cumulatedHeight, w, height+1);
      cumulatedHeight = cumulatedHeight + height;
    }

    // calculate the total minimum height ("cumulatedHeight")
    FigCompartment comp;
    int yAfterRegularFigs = y+cumulatedHeight;
    int nbCompartmentsDisplayed = 0;
    for (int i=0; i<_compartments.size(); i++) {
      comp = (FigCompartment)_compartments.elementAt(i);
      if ( comp.isDisplayed() ) {
        nbCompartmentsDisplayed++;
        cumulatedHeight = cumulatedHeight + comp.getMinimumSize().height;
      }
    }

    int extra_each = h - cumulatedHeight;
    if ( nbCompartmentsDisplayed > 0 ) extra_each = extra_each / nbCompartmentsDisplayed;
    // extra_each is the height for each compartment

    for (int i=0; i<_compartments.size(); i++) {
      comp = (FigCompartment)_compartments.elementAt(i);
      height = ((Fig)comp).getMinimumSize().height + extra_each;
      if ( comp.isDisplayed() ) {
        comp.setBounds(x, yAfterRegularFigs, w, height+nbCompartmentsDisplayed-1);
        yAfterRegularFigs = yAfterRegularFigs + height;
      }
      else
        comp.setBounds(x, yAfterRegularFigs-height, 0, height+1);
// note: this last line is to make sure that FigCompartments
// that are not displayed are keeping the proper height
    }

    if ( nbCompartmentsDisplayed <= 0 )  h = yAfterRegularFigs - y;

    _bigPort.setBounds(x+1, y+1, w-2, h-2);

    calcBounds(); //_x = x; _y = y; _w = w; _h = h;
    updateEdges();
    firePropChange("bounds", oldBounds, getBounds());
  }

  ////////////////////////////////////////////////////////////////
  // event handlers

  ////////////////////////////////////////////////////////////////
  // event handlers - MouseListener implementation

  ////////////////////////////////////////////////////////////////
  // internal methods

  ////////////////////////////////////////////////////////////////
  // painting methods

  /** Accumulate a bounding box for all the Figs in the group. */
  // added by Eric Lefevre 25 Apr 1999
  public void calcBounds() {
    Vector allFigs = (Vector)getFigs().clone();
    _figs = getDisplayedFigs();
// cannot call setFigs() because setFigs() calls calcBounds()

    super.calcBounds();
    _figs = allFigs;
  }

} /* end class FigNodeWithCompartments */
