// $Id$
// Copyright (c) 2007 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.uml.diagram.sequence2.ui;

import java.awt.Point;
import java.util.List;

import org.apache.log4j.Logger;
import org.argouml.model.Model;
import org.argouml.uml.diagram.sequence2.SequenceDiagramGraphModel;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.ModeCreatePolyEdge;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigPoly;

/**
 * Mode to create a link between two figclassifierroles. 
 * @author penyaskito
 */
public class ModeCreateMessage extends ModeCreatePolyEdge {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(ModeCreateMessage.class);
    
    /**
     * The constructor.
     *
     * @param par the editor
     */
    public ModeCreateMessage(Editor par) {
        super(par);
        if (LOG.isDebugEnabled()) {
            LOG.debug("ModeCreateMessage created with editor:" + editor);
        }
    }

    /**
     * The constructor.
     */
    public ModeCreateMessage() {
        super();
        if (LOG.isDebugEnabled()) {
            LOG.debug("ModeCreateMessage created without editor.");
        }
    }
    
    @Override
    public void endAttached(FigEdge fe) {
        super.endAttached(fe);
        final SequenceDiagramGraphModel gm =
            (SequenceDiagramGraphModel) getEditor().getGraphModel();
        
        Object message = fe.getOwner();
        FigClassifierRole dcr = (FigClassifierRole) fe.getDestFigNode();
        FigClassifierRole scr = (FigClassifierRole) fe.getSourceFigNode();
        
        final Object action = Model.getFacade().getAction(message);
        if (Model.getFacade().isACallAction(action)) {
            
            // get the source of the return message
            final Object returnMessageSource =
                Model.getFacade().getReceiver(message);
            // get the dest of the return message
            final Object returnMessageDest =
                Model.getFacade().getSender(message);
            
            // create the return message modelelement with the interaction
            // and the collaboration
            final Object returnMessage = gm.connect(
                    returnMessageSource,
                    returnMessageDest, 
                    Model.getMetaTypes().getMessage(),
                    Model.getMetaTypes().getReturnAction());
            
            final LayerPerspective layer = 
                (LayerPerspective) editor.getLayerManager().getActiveLayer();
            
            FigMessage returnEdge = null;

            List<Fig> figs = layer.getContents();
            for (Fig fig : figs) {
                if (fig.getOwner() == returnMessage) {
                    returnEdge = (FigMessage) fig;
                    break;
                }
            }

            returnEdge.setSourcePortFig(fe.getDestPortFig());
            returnEdge.setSourceFigNode(dcr);
            returnEdge.setDestPortFig(fe.getSourcePortFig());
            returnEdge.setDestFigNode(scr);
            
            final Point[] points = returnEdge.getPoints();
            for (int i = 0; i < points.length; ++i) {
                // TODO: this shouldn't be hardcoded
                // 20 is the height of the spline
                // 50 is the default activation height
                points[i].y = fe.getY() + 50 + 20;
            }
            returnEdge.setPoints(points);
                           
            if (returnEdge.isSelfMessage()) {
                returnEdge.convertToArc();
            }
            
            // Mark the contain FigPoly as complete.
            // TODO: I think more work is needed in GEF to either do this
            // automatically when both ends are set or at the very least
            // Give a setComplete method on FigPolyEdge that calls its
            // contained poly
            FigPoly poly = (FigPoly) returnEdge.getFig();
            poly.setComplete(true);
        }
        dcr.createActivations();
        dcr.renderingChanged();
        if (dcr != scr) {
            scr.createActivations();
            scr.renderingChanged();
        }
    }
}
