// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products
// must be negotiated with University of California. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.



// File: FigRealization.java
// Classes: FigRealization
// Original Author: agauthie@ics.uci.edu
// $Id$


package uci.uml.visual;

import java.awt.*;
import java.beans.*;

import uci.gef.*;
import uci.uml.ui.*;
import uci.uml.Foundation.Core.*;

public class FigRealization extends FigEdgeModelElement {

  public FigRealization(Object edge) {
    super(edge);
    addPathItem(_stereo, new PathConvPercent(this, 50, 10));
    ArrowHeadTriangle endArrow = new ArrowHeadTriangle();
    endArrow.setFillColor(Color.white);
    setDestArrowHead(endArrow);
    setBetweenNearestPoints(true);
    modelChanged();
  }

  public void setFig(Fig f) {
    super.setFig(f);
    _fig.setDashed(true);
  }

  public void dispose() {
    Realization r = (Realization) getOwner();
    if (r == null) return;
    Classifier sup = r.getSupertype();
    Classifier sub = r.getSubtype();
    try {
      sup.removeRealization(r);
      sub.removeSpecification(r);
    }
    catch (PropertyVetoException pve) {
      System.out.println("could not remove Realization");
    }
    super.dispose();
  }


  protected boolean canEdit(Fig f) { return false; }
  
  /** This is called aftern any part of the UML ModelElement has
   *  changed. This method automatically updates the name FigText.
   *  Subclasses should override and update other parts. */
  protected void modelChanged() {
    // do not set _name
    updateStereotypeText();
  }

  
} /* end class FigRealization */

