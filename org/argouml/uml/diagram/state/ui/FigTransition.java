// Copyright (c) 1996-2001 The Regents of the University of California. All
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

// File: FigTransition.java
// Classes: FigTransition
// Original Author: your email address here
// $Id$

package org.argouml.uml.diagram.state.ui;

import java.awt.Color;
import java.awt.Point;
import java.beans.PropertyVetoException;

import org.apache.log4j.Category;
import org.argouml.application.api.Notation;
import org.argouml.model.uml.behavioralelements.statemachines.StateMachinesHelper;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.generator.ParserDisplay;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.PathConvPercent;
import org.tigris.gef.presentation.ArrowHeadGreater;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigText;
import ru.novosoft.uml.behavior.state_machines.MStateVertex;
import ru.novosoft.uml.behavior.state_machines.MTransition;
import ru.novosoft.uml.foundation.core.MModelElement;

public class FigTransition extends FigEdgeModelElement {
    protected static Category cat = Category.getInstance(FigTransition.class);

  ////////////////////////////////////////////////////////////////
  // constructors
  public FigTransition() {
    super();
    addPathItem(_name, new PathConvPercent(this, 50, 10));
    _fig.setLineColor(Color.black);
    setDestArrowHead(new ArrowHeadGreater());
  }
  
  public FigTransition(Object edge, Layer lay) {
    this();
    if (edge instanceof MTransition) {
      MTransition tr = (MTransition)edge;
      MStateVertex sourceSV = tr.getSource();
      MStateVertex destSV = tr.getTarget();
      FigNode sourceFN = (FigNode) lay.presentationFor(sourceSV);
      FigNode destFN = (FigNode) lay.presentationFor(destSV);
      setSourcePortFig(sourceFN);
      setSourceFigNode(sourceFN);
      setDestPortFig(destFN);
      setDestFigNode(destFN);
    }
    setLayer(lay);
    setOwner(edge);
    
  }

  public FigTransition(Object edge) {
    this(edge, ProjectBrowser.TheInstance.getActiveDiagram().getLayer());
  }

  ////////////////////////////////////////////////////////////////
  // event handlers

  /** This method is called after the user finishes editing a text
   *  field that is in the FigEdgeModelElement.  Determine which field
   *  and update the model.  This class handles the name, subclasses
   *  should override to handle other text elements. */
  protected void textEdited(FigText ft) throws PropertyVetoException {
    
    MTransition t = (MTransition) getOwner();
    if (t == null) return;
    String s = ft.getText();
    ParserDisplay.SINGLETON.parseTransition(t, s);
   
  }

  /** This is called aftern any part of the UML MModelElement has
   *  changed. This method automatically updates the name FigText.
   *  Subclasses should override and update other parts. */
  protected void modelChanged() {
    super.modelChanged();
    MModelElement me = (MModelElement) getOwner();
    if (me == null) return;
    cat.debug("FigTransition modelChanged: " + me.getClass());
    String nameStr = Notation.generate(this, me);
    _name.setText(nameStr);
    _name.calcBounds();
    _name.damage();
  }
  
  protected int[] flip(int[] Ps) {
    int[] r = new int[Ps.length];
    for (int i = Ps.length; i == 0; i--) {
        r[Ps.length-i] = Ps[i];
    }
    return r;
  }
  
  private double calculateLength(Point point1, Point point2) {
  	return Math.sqrt(Math.abs(point1.x-point2.x)*Math.abs(point1.x-point2.x)+Math.abs(point1.y-point2.y)*Math.abs(point1.y-point2.y));
  }
  
  
    /**
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#getDestination()
     */
    protected Object getDestination() {
        if (getOwner() != null) {
            return StateMachinesHelper.getHelper().getDestination((MTransition)getOwner());
        }
        return null;
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#getSource()
     */
    protected Object getSource() {
        if (getOwner() != null) {
            return StateMachinesHelper.getHelper().getSource((MTransition)getOwner());
        }
        return null;
    }
    
    

} /* end class FigTransition */

