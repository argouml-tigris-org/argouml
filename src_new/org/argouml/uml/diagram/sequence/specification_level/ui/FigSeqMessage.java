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

// File: FigSeqMessage.java
// Original Author: agauthie@ics.uci.edu
// $Id$


package org.argouml.uml.diagram.sequence.specification_level.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.argouml.application.api.Notation;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.diagram.sequence.specification_level.ui.SequenceDiagramLayout;
import org.argouml.uml.diagram.sequence.specification_level.MessageSupervisor;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.generator.ParserDisplay;

import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.PathConvPercent;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.ArrowHead;
import org.tigris.gef.presentation.ArrowHeadGreater;
import org.tigris.gef.presentation.ArrowHeadHalfTriangle;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdgePoly;
import org.tigris.gef.presentation.FigLine;
import org.tigris.gef.presentation.FigPoly;
import org.tigris.gef.presentation.FigText;

import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.behavior.collaborations.MAssociationRole;
import ru.novosoft.uml.behavior.collaborations.MClassifierRole;
import ru.novosoft.uml.behavior.collaborations.MMessage;
import ru.novosoft.uml.behavior.common_behavior.MAction;
import ru.novosoft.uml.behavior.common_behavior.MCallAction;
import ru.novosoft.uml.behavior.common_behavior.MCreateAction;
import ru.novosoft.uml.behavior.common_behavior.MReturnAction;
import ru.novosoft.uml.behavior.common_behavior.MSendAction;
// import ru.novosoft.uml.behavior.common_behavior.MInstance;
// import ru.novosoft.uml.behavior.common_behavior.MLink;
// import ru.novosoft.uml.behavior.common_behavior.MStimulus;



/** Class to display graphics for a UML collaboration in a diagram. */

public class FigSeqMessage extends FigNodeModelElement implements PropertyChangeListener {

    ////////////////////////////////////////////////////////////////
    // constants
    public int PADDING = 5;
    public static Vector ARROW_DIRECTIONS = new Vector();

    ////////////////////////////////////////////////////////////////
    // instance variables
    protected FigSeqMessage _splitref = null;
    protected Vector _predecessors = new Vector(2, 2);
    protected Vector _successors   = new Vector(2, 2);
    protected Fig _sndPortFig = null;
    protected Fig _rcvPortFig = null;

    ////////////////////////////////////////////////////////////////
    // constructors

    public FigSeqMessage() {
        _name.setLineWidth(0);
        _name.setFilled(false);
//        Dimension nameMin = _name.getMinimumSize();
//        _name.setBounds(10, 10, 90, nameMin.height);

        // add Figs to the FigNode in back-to-front order
        super.addFig(_name);

//        Rectangle r = getBounds();
//        setBounds(r.x, r.y, r.width, r.height);
    }

    public FigSeqMessage(GraphModel gm, Object node) {
        this();
        setOwner(node);
    }

    public void setOwner(Object node) {
        super.setOwner(node);
        if (node != null && node instanceof MMessage) {
            MMessage msg   = (MMessage)node;
            String   text  = msg.getName();
            _name.setText(text);
        }
    }

    public String placeString() {
        return "new SequenceMessage";
    }

    public Object clone() {
        FigSeqMessage figClone = (FigSeqMessage)super.clone();
        Vector v = figClone.getFigs();
        figClone._name = (FigText)v.elementAt(0);
        return figClone;
    }

    ////////////////////////////////////////////////////////////////
    // Fig accessors for message linking

    public void clearPredecessors() {
        _predecessors = new Vector(2, 2);
    }
    public void clearSuccessors() {
        _successors = new Vector(2, 2);
    }

    public void addPredecessor(FigSeqMessage msg) {
        _predecessors.add(msg);
    }
    public void addSuccessor(FigSeqMessage msg) {
        _successors.add(msg);
    }

    public void removePredecessor(FigSeqMessage msg) {
        _predecessors.remove(msg);
    }
    public void removeSuccessor(FigSeqMessage msg) {
        _successors.remove(msg);
    }

    public Vector getPredecessors() {
        return _predecessors;
    }
    public Vector getSuccessors() {
        return _successors;
    }

    public void clearSplitReference() {
        _splitref = null;
    }
    public void setSplitReference(FigSeqMessage msg) {
        _splitref = msg;
    }
    public FigSeqMessage getSplitReference() {
        return _splitref;
    }

    ////////////////////////////////////////////////////////////////
    // Fig accessors

    public void addFig(Fig f) {
        throw new RuntimeException("Figs should not be added to FigSeqMessage using addFig(fig)");
    }

    public void layoutTextOnPath(FigEdgePoly poly) {
        Dimension nameMin = _name.getMinimumSize();
        _name.setBounds(poly.center().x - nameMin.width / 2,
                        poly.getY() - 1 - _name.getHeight(),
                        nameMin.width,
                        nameMin.height);
        calcBounds();
    }

    public void setPath(FigPoly path) {
        if (getOwner() != null) {
            MMessage mm = (MMessage)getOwner();
            MAction  ma = mm.getAction();
            int linetype = 0;
            if (ma != null && ma instanceof MReturnAction) linetype = 1;
            if (linetype == 0) {
                path.setDashed(false);
            } else {
                path.setDashed(true);
            }
        }
        FigEdgePoly poly = new FigEdgePoly();
        poly.setBetweenNearestPoints(true);
        poly.setFig(path);
        if (_figs.size() > 1) {
            _figs.set(1, poly);
            path.setGroup(this);
            calcBounds();
        } else {
            super.addFig(poly);
        }
        if (_sndPortFig != null) {
            poly.setSourcePortFig(_sndPortFig);
            poly.setSourceFigNode(((FigSeqClRolePort)_sndPortFig).getBaseFig());
        }
        if (_rcvPortFig != null) {
            if (_rcvPortFig instanceof FigSeqClRolePort) {
                poly.setDestPortFig(_rcvPortFig);
                poly.setDestFigNode(((FigSeqClRolePort)_rcvPortFig).getBaseFig());
            } else {
                poly.setDestPortFig(((FigSeqClRole)_rcvPortFig).getFigObject());
                poly.setDestFigNode((FigSeqClRole)_rcvPortFig);
            }
        }
        if (getOwner() != null) {
            MMessage mm = (MMessage)getOwner();
            MAction  ma = mm.getAction();
            if (ma != null) {
                if (ma instanceof MSendAction) {
                    poly.setDestArrowHead(new ArrowHeadHalfTriangle());
                } else {
                    poly.setDestArrowHead(new ArrowHeadGreater());
                }
            }
        }
        layoutTextOnPath(poly);
    }

    public FigPoly getPath() {
        FigEdgePoly poly = (_figs.size() > 1) ? (FigEdgePoly)_figs.get(1) : null;
        FigPoly p = null;
        if (poly != null) {
            int[] x = poly.getXs();
            int[] y = poly.getYs();
            int   n = poly.getNumPoints();
            p = new FigPoly();
            for(int i = 0; i < n; i++) {
                p.addPoint(x[i], y[i]);
            }
        }
        return p;
    }

    public void setPortFigs(Fig[] p) {
        _sndPortFig = p[0];
        _rcvPortFig = p[1];
        if (_figs.size() > 1) {
            FigEdgePoly poly = (FigEdgePoly)_figs.get(1);
            poly.setSourcePortFig(p[0]);
            poly.setSourceFigNode(((FigSeqClRolePort)p[0]).getBaseFig());
            if (p[1] instanceof FigSeqClRolePort) {
                poly.setDestPortFig(p[1]);
                poly.setDestFigNode(((FigSeqClRolePort)p[1]).getBaseFig());
            } else {
                poly.setDestPortFig(((FigSeqClRole)p[1]).getFigObject());
                poly.setDestFigNode((FigSeqClRole)p[1]);
            }
            layoutTextOnPath(poly);
        }
    }

    public Fig getSourcePortFig() {
        return _sndPortFig;
    }
    public Fig getDestPortFig() {
        return _rcvPortFig;
    }

    public Fig getSourceFig() {
        if (_sndPortFig instanceof FigSeqClRoleObject) {
            return ((FigSeqClRoleObject)_sndPortFig).getBaseFig();
        } else if (_sndPortFig instanceof FigSeqClRolePort) {
            return ((FigSeqClRolePort)_sndPortFig).getBaseFig();
        }
        return _sndPortFig;
    }
    public Fig getDestFig() {
        if (_rcvPortFig instanceof FigSeqClRoleObject) {
            return ((FigSeqClRoleObject)_rcvPortFig).getBaseFig();
        } else if (_rcvPortFig instanceof FigSeqClRolePort) {
            return ((FigSeqClRolePort)_rcvPortFig).getBaseFig();
        }
        return _rcvPortFig;
    }

    public void setLineColor(Color col) {
        _name.setLineColor(col);
    }

    public void setFillColor(Color col) {
        _name.setFillColor(col);
    }
    public void setFilled(boolean f) {
    }
    public boolean getFilled() {
        return true;
    }

    public Dimension getMinimumSize() {
        Dimension nameMin = _name.getMinimumSize();
        int h = nameMin.height;
        int w = nameMin.width;
        return new Dimension(w, h);
    }

    /* Override setBounds to keep shapes looking right */
    public void setBounds(int x, int y, int w, int h) {
        if (_name == null) return;
        Rectangle oldBounds = getBounds();
        Dimension nameMin = _name.getMinimumSize();
        int ht = 0;
        _name.setBounds(x, y, nameMin.width, nameMin.height);
        firePropChange("bounds", oldBounds, getBounds());
        calcBounds(); //_x = x; _y = y; _w = w; _h = h;
        updateEdges();
    }

    protected void textEdited(FigText ft) throws PropertyVetoException {
        super.textEdited(ft);
        MMessage msg = (MMessage) getOwner();
        if (ft == _name) {
            String s = ft.getText();
            try {
                ParserDisplay.SINGLETON.parseMessage(msg, s);
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public String ownerName() {
        if (getOwner() != null) {
            return ( (MMessage)getOwner()).getName();
        } else {
            return "null";
        }
    }

    protected void modelChanged(MElementEvent mee) {
        super.modelChanged(mee);
        MMessage msg = (MMessage) getOwner();
        if (msg == null) return;
        String nameStr = Notation.generate(this, msg.getName()).trim();
        String actionString = "new Action";
        if (msg.getAction() != null && msg.getAction().getName() != null) {
            actionString = Notation.generate(this, ((MAction)msg.getAction()).getName()).trim();
        }
        if( nameStr.equals("") && actionString.equals("") ) {
            _name.setText("");
        } else {
            _name.setText(nameStr.trim() + " : " + actionString.trim());
        }
        if (msg.getCommunicationConnection() != null) {
            MAssociationRole assnr = (MAssociationRole)msg.getCommunicationConnection();
            if (assnr.getName() != null) {
                assnr.setName(assnr.getName());
            } else {
                assnr.setName("");
            }
        }
        if (msg.getSender() != null) {
            MClassifierRole clsfr = msg.getSender();
            if (clsfr.getName() != null) {
                clsfr.setName(clsfr.getName());
            } else {
                clsfr.setName("");
            }
        }
        if (msg.getReceiver() != null) {
            MClassifierRole clsfr = (MClassifierRole)msg.getReceiver();
            if (clsfr.getName() != null) {
                clsfr.setName(clsfr.getName());
            } else {
                clsfr.setName("");
            }
        }
        if (getLayer() != null && getLayer() instanceof SequenceDiagramLayout) {
            // TODO: must be re-activated after correcting some errors on displaying messages
//            ((SequenceDiagramLayout)getLayer()).placeAllFigures();
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
                        } else {
                            throw new IllegalStateException("No way to get graphmodel. Project corrupted");
                        }
                    }
                    setLayer(new SequenceDiagramLayout(name, gm));
                }
            }
            // TODO: must be re-activated after correcting some errors on displaying messages
//            ((SequenceDiagramLayout)getLayer()).placeAllFigures();
        }
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


  /** if you move a FigSeqObject around and place it onto  a FigSeqMessage
     not the FigSeqObject gets the mouseReleased event but the FigSeqMessage.
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

    /**
     * This method supports for debugging help as System.out.print() will not only
     * write the object address to output
     */
    public String toString() {
        MAction ma = (getOwner() == null) ? null : ((MMessage)getOwner()).getAction();
        String  sa = (ma == null) ? "null" : ma.getClass().getName();
        return this.getClass().getName() + "[Action=" + sa + "Path=[" + pathCoordinatesAsString() + "],Text=" + getText() + "]";
    }

    private String getText() {
        return getNameFig().getText();
    }
    private String pathCoordinatesAsString() {
        FigPoly f = getPath();
        if (f == null) return "";
        int[] x = f.getXs();
        int[] y = f.getYs();
        Point[] p = new Point[f.getNumPoints()];
        for(int i = 0; i < p.length; i++) {
            p[i] = new Point(x[i], y[i]);
        }
        if (p.length < 1) return "";
        StringBuffer sb = new StringBuffer(p.length * 8);
        sb.append(pointCoordinatesAsString(p[0]));
        for(int i = 1; i < p.length; i++) {
            sb.append(',');
            sb.append(pointCoordinatesAsString(p[i]));
        }
        return sb.toString();
    }

    private String pointCoordinatesAsString(Point p) {
        return "(" + p.x + ";" + p.y + ")";
    }

    public void propertyChange(PropertyChangeEvent pce) {
        super.propertyChange(pce);
//System.out.println("FigSeqMessage: propertyChange, src = " + pce.getSource());
        if (pce.getSource() != _name || !pce.getPropertyName().equals("bounds")) {
            FigEdgePoly poly = (_figs.size() > 1) ? (FigEdgePoly)_figs.get(1) : null;
            if (poly != null) 
                layoutTextOnPath(poly);
        }
    }
} /* end class FigSeqMessage */
