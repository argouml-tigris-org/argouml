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

// File: FigSeqInteraction.java
// Original Author: agauthie@ics.uci.edu
// $Id$


package org.argouml.uml.diagram.sequence.specification_level.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.util.Collection;
import java.util.Vector;

import org.argouml.application.api.Notation;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.diagram.sequence.specification_level.ui.SequenceDiagramLayout;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.generator.ParserDisplay;

import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.PathConvPercent;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigText;

import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.behavior.collaborations.MAssociationRole;
import ru.novosoft.uml.behavior.collaborations.MInteraction;
import ru.novosoft.uml.behavior.collaborations.MMessage;
import ru.novosoft.uml.behavior.common_behavior.MAction;
import ru.novosoft.uml.behavior.common_behavior.MInstance;
import ru.novosoft.uml.behavior.common_behavior.MLink;



/** Class to display graphics for a UML collaboration in a diagram. */

public class FigSeqInteraction extends FigNodeModelElement {

  ////////////////////////////////////////////////////////////////
  // constants
  public int PADDING = 5;
  public static Vector ARROW_DIRECTIONS = new Vector();

  ////////////////////////////////////////////////////////////////
  // instance variables

  //protected Polygon _polygon;

  ////////////////////////////////////////////////////////////////
  // constructors

  public FigSeqInteraction() {
      setDisplayed(false);
  }

  public FigSeqInteraction(GraphModel gm, Object inter) {
    this();
    setOwner(inter);
  }

  public String placeString() {
      return "new SequenceInteraction";
  }

  public Object clone() {
    FigSeqMessage figClone = (FigSeqMessage) super.clone();
//    Vector v = figClone.getFigs();
//    figClone._name = (FigText) v.elementAt(0);
    return figClone;
  }


  ////////////////////////////////////////////////////////////////
  // Fig accessors

  public void setLineColor(Color col) {
    _name.setLineColor(col);
  }

  public void setFillColor(Color col) {
    _name.setFillColor(col);
  }

  public void setFilled(boolean f) {  }
  public boolean getFilled() { return true; }

  public Dimension getMinimumSize() {
    Dimension nameMin = _name.getMinimumSize();

    int h = nameMin.height;
    int w = nameMin.width;
    return new Dimension(w, h);
  }

  protected void textEdited(FigText ft) throws PropertyVetoException {
    super.textEdited(ft);
//    MInteraction sti = (MInteraction) getOwner();
//    if (ft == _name) {
//       String s = ft.getText();
//       ParserDisplay.SINGLETON.parseInteraction(sti, s);
//    }
  }

  public String ownerName() {
    if (getOwner() != null) { return ( (MInteraction)getOwner()).getName(); }
    else return "null";
  }

  protected void modelChanged(MElementEvent mee) {
    super.modelChanged(mee);
  }
   

    public void addPathItemToAssociationRole(Layer lay) {
        MAssociationRole assnr = ((MMessage)getOwner()).getCommunicationConnection();
        if (assnr != null && lay != null) {
            FigSeqAsRole figSeqAsRole = (FigSeqAsRole)lay.presentationFor(assnr);
            if (figSeqAsRole != null) {
                int percent = 50;
                figSeqAsRole.addPathItem(this, new PathConvPercent(figSeqAsRole, percent, 10));
                figSeqAsRole.updatePathItemLocations();
                lay.bringToFront(this);
            }
        }
    }


  /** if you move a FigSeqObject around and place it onto  a FigSeqInteraction
     not the FigSeqObject gets the mouseReleased event but the FigSeqInteraction.
     For this case, the diagram has to be replaced, too. */
  public void mouseReleased(MouseEvent me) {
    super.mouseReleased(me);
    if (getLayer() != null && getLayer() instanceof SequenceDiagramLayout) {
    	((SequenceDiagramLayout)getLayer()).placeAllFigures();
    } else {
    	Diagram diagram = ProjectBrowser.TheInstance.getActiveDiagram();
    	Layer lay = null;
    	if (diagram != null) {
    		lay = diagram.getLayer();
    		if (lay instanceof SequenceDiagramLayout) {
    			setLayer(lay);
    		} else {
    			String name = null;
    			GraphModel gm = null;
    			if (lay != null && lay instanceof LayerPerspective) {
    				gm = ((LayerPerspective)lay).getGraphModel();
    				name = ((LayerPerspective)lay).getName();
    			} else {
    				Editor ed = Globals.curEditor();
    				if (ed != null && ed.getGraphModel() != null && ed.getLayerManager() != null && ed.getLayerManager().getActiveLayer() != null) {
    					lay = ed.getLayerManager().getActiveLayer();
    					name = lay.getName();
    					gm = ed.getGraphModel();
    				} else
    					throw new IllegalStateException("No way to get graphmodel. Project corrupted");
    			}
    			setLayer(new SequenceDiagramLayout(name, gm));
    		}
    	}
    	((SequenceDiagramLayout)getLayer()).placeAllFigures();
    }
  }

    /**
     * @see org.tigris.gef.presentation.Fig#dispose()
     */
    public void dispose() {
        super.dispose();
    }

} /* end class FigSeqInteraction */
