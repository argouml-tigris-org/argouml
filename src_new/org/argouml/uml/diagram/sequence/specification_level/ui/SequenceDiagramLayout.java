/** New Class to layout the sequence diagram
 *  needs more work: yet not all methods are restructured
 */

// file: ActionAddLink.java
// author: 5kanzler@informatik.uni-hamburg.de

package org.argouml.uml.diagram.sequence.specification_level.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Category;

import org.argouml.uml.diagram.sequence.specification_level.MessageSupervisor;
import org.argouml.model.uml.UmlFactory;

import org.tigris.gef.base.Editor;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.Globals;
import org.tigris.gef.presentation.FigDynPort;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigPainter;
import org.tigris.gef.presentation.FigPoly;
import org.tigris.gef.presentation.FigText;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.GraphEvent;

import ru.novosoft.uml.behavior.collaborations.MAssociationRole;
import ru.novosoft.uml.behavior.collaborations.MMessage;
import ru.novosoft.uml.behavior.common_behavior.MAction;
import ru.novosoft.uml.behavior.common_behavior.MCreateAction;
import ru.novosoft.uml.behavior.common_behavior.MReturnAction;
import ru.novosoft.uml.behavior.common_behavior.MLink;
import ru.novosoft.uml.behavior.common_behavior.MStimulus;

public class SequenceDiagramLayout extends LayerPerspective implements FigSeqConstants {

    protected static Category cat = Category.getInstance(SequenceDiagramLayout.class);

    static final int OBJ_WIDTH = 0;

    private FigPainter _painter;

    public SequenceDiagramLayout(String name, GraphModel gm) {
        super(name,gm);
        _painter = new SequenceDiagramPainter();
    }

    public void nodeAdded(GraphEvent ge) {
        Object node = ge.getArg();
        Fig oldDE = presentationFor(node);
        // assumes each node can only appear once in a given layer
        if (null == oldDE) {
            if (!shouldShow(node)) {
                cat.debug("node rejected");
                return;
            }
            FigNode newFigNode = _nodeRenderer.getFigNodeFor(_gm, this, node);
            if (newFigNode != null) {
                putInPosition(newFigNode);      // CHECK: remove, because works as no-op (?)
                                                //        no regions for new figures are set
                                                //        from ArgoUML, but what about GEF ?
                add(newFigNode);
                placeAllFigures();
            }
        }
    }

    public void edgeAdded(GraphEvent ge) {
        Object edge = ge.getArg();
        Fig oldFig = presentationFor(edge);
        if (null == oldFig) {
            if (!shouldShow(edge)) { cat.debug("edge rejected"); return; }
            FigEdge newFigEdge = _edgeRenderer.getFigEdgeFor(_gm, this, edge);
            if (newFigEdge != null) {
                add(newFigEdge);
                newFigEdge.computeRoute();
                newFigEdge.endTrans();
            }
        }
    }

    Vector getFigSeqClassifierRoles() {
        Vector figSeqClassifierRoles = new Vector();
        for (int i=0; i<_contents.size(); i++) {
            if (_contents.elementAt(i) instanceof FigSeqClRole) figSeqClassifierRoles.add( _contents.elementAt(i) );
        }
        return figSeqClassifierRoles;
    }

    /**
     * Return all instances of FigSeqLink in the active layer
     */
    Vector getFigSeqAssociationRoles() {
        Vector figSeqAssociationRoles = new Vector();
        for (int i=0; i<_contents.size(); i++) {
            if (_contents.elementAt(i) instanceof FigSeqAsRole) figSeqAssociationRoles.add( _contents.elementAt(i) );        }
        return figSeqAssociationRoles;
    }

    Vector getFigSeqMessages() {
        Vector figSeqMessages = new Vector();
        for (int i=0; i<_contents.size(); i++) {
            if (_contents.elementAt(i) instanceof FigSeqMessage) figSeqMessages.add( _contents.elementAt(i) );
        }
        return figSeqMessages;
    }

    /** returns the maximal width over all messages between two objects  */
    int getMaxMessageWidth(FigSeqClRole clsfr1,FigSeqClRole clsfr2) {
        Vector figSeqMessages = getFigSeqMessages();
        int maxWidth = 0;
        for(int i = 0; i < figSeqMessages.size(); i++) {
            FigSeqMessage fig = (FigSeqMessage)figSeqMessages.elementAt(i);
            MMessage msg = (MMessage)fig.getOwner();
            if ( (msg.getReceiver() == clsfr1.getOwner() && msg.getSender() == clsfr2.getOwner() ) ||
            (msg.getReceiver() == clsfr2.getOwner() && msg.getSender() == clsfr1.getOwner() ) ) {
                if (fig.getBounds().width > maxWidth) maxWidth = fig.getBounds().width;
                // needs more work: include the width of end roles
            }
        }
        return maxWidth;
    }

    /**
     * Put the figSeqClRoles in place; only used from figures
     */
    private void createLine(int line, int thdSnd, int thdRcv, FigSeqMessage msg, int idx) {
        int idxConn = (line - 1) / 2;
        FigPoly path = new FigPoly();
        FigSeqClRole role = null;
        Fig[] p = new Fig[2];
        Point[] coord  = (idx == 0) ? new Point[2] : new Point[4];
        int     ccount = coord.length;
        int     cbase;
        role  = (FigSeqClRole)presentationFor(((MMessage)msg.getOwner()).getSender());
        cbase = role.getCreationLineIndex();
System.out.println("Getting new PortFig for message " + ((FigText)msg.getFigs().get(0)).getText() + " in line " + line + " with cbase = " + cbase);
//        p[0]  = role.getNewPortFig(line - ((cbase < 1) ? 0 : cbase * 2), thdSnd, 0);
//        p[0]  = role.getNewPortFig(line - ((cbase < 1) ? 0 : cbase + 1), thdSnd, 0);
        cbase = (cbase < 1) ? 0 : cbase / 2 + 1;
        p[0]  = role.getNewPortFig(idxConn - cbase, thdSnd, 0);
        coord[0] = p[0].center();
        role = (FigSeqClRole)presentationFor(((MMessage)msg.getOwner()).getReceiver());
        MMessage mm = (MMessage)msg.getOwner();
        MAction  ma = (mm == null) ? null : mm.getAction();
        if (ma != null && ma instanceof MCreateAction) {
            p[1] = role;
            role.setCreationLineIndex(line + 1);
            coord[ccount - 1] = role.getFigObject().center();
        } else {
            cbase = role.getCreationLineIndex();
            cbase = (cbase < 1) ? 0 : cbase / 2 + 1;
            p[1]  = role.getNewPortFig(idxConn - cbase, thdRcv, 0);
            coord[ccount - 1] = p[1].center();
        }
        if (ccount > 2) {
            int d = LIFELINE_PORT_DIST + LIFELINE_PORT_SIZE;
            coord[1].x = coord[0].x + d;
            coord[1].y = coord[0].y + d;
            coord[2].x = coord[3].x - d;
            coord[1].y = coord[3].y - d;
        }
        for(int i = 0; i < coord.length; i++) {
            path.addPoint(coord[i]);
        }
        msg.setPath(path);
        msg.setPortFigs(p);
        ((FigSeqClRolePort)p[0]).addMessage(msg, true);
        if (p[1] instanceof FigSeqClRolePort) {
            ((FigSeqClRolePort)p[1]).addMessage(msg, false);
        }
        bringToFront(msg);
    }

    /**
     * Put the figSeqClRoles in place; only used from figures
     */
    private int placeMessagesInLine(int line, int[] thdSnd, int[] thdRcv, FigSeqMessage[] msgs) {
        int d = msgs.length;
        for(int i = 0; i < msgs.length; i++) {
            if (msgs[i].getSourceFig() == null && msgs[i].getDestFig() == null) {
                createLine(line, thdSnd[i], thdRcv[i], msgs[i], i);
            }
            MAction action = ((MMessage)msgs[i].getOwner()).getAction();
            if (action instanceof MCreateAction) {
                if (d < 3) d = 3;
            }
        }
        return d;
    }

    /**
     * Put the FigSeqClRoles in place; only used from figures and on startup
     */
    public void placeAllFigures() {
        int posx = ORIGIN_X;
        Iterator iterator = _contents.iterator();
        while(iterator.hasNext()) {
            Fig fig = (Fig)iterator.next();
            if (fig instanceof FigSeqClRole) {
                fig.setLocation(posx, ORIGIN_Y);
                posx += fig.getWidth() + OBJECT_DEFAULT_DIST;
            }
        }

        Vector lines   = MessageSupervisor.calculateLines(this);
        int[]  threads = null;
        FigSeqMessage[] aline;
        int my = 1;
        MessageSupervisor.LineDescriptor ldescr = null;
        for(int i = 0; i < lines.size(); i++) {
            ldescr  = (MessageSupervisor.LineDescriptor)lines.get(i);
            aline   = ldescr.getMessages();
            my += placeMessagesInLine(my, ldescr.getSenderThreads(), ldescr.getReceiverThreads(), aline);
            if (my % 2 == 0) my++;
        }

        for(int i = 0; i < 1 && i < lines.size(); i++) {
            ldescr  = (MessageSupervisor.LineDescriptor)lines.get(i);
            aline   = ldescr.getMessages();
            for(int j = 0; j < aline.length; j++) {
                ((FigSeqClRole)(aline[j].getSourceFig())).setActiveState(true);
            }
        }

        iterator = _contents.iterator();
        while(iterator.hasNext()) {
            Fig fig = (Fig)iterator.next();
            if (fig instanceof FigSeqClRole) {
                ((FigSeqClRole)fig).relocate();
                ((FigSeqClRole)fig).getLifeline().rebuild();
                ((FigSeqClRole)fig).calcBounds();
            }
        }
        Globals.curEditor().damageAll();
    }

    public void paint(Graphics g) {
        paint(g, _painter);
    }

    public void paint(Graphics g, FigPainter fp) {
        if (fp == null) {
            super.paint(g, _painter);
        } else {
            super.paint(g, fp);
        }
    }

    public void paintContents(Graphics g) {
        paintContents(g, _painter);
    }
    public void paintContents(Graphics g, FigPainter fp) {
        super.paintContents(g, fp);
    }

    /**
     * The common hit methods only find figures registered to an editor. The
     * hitPort method in FigSeqClRole determines if the hit fig is a
     * FigSeqClRoleObject or FigSeqClRolePort contained by the FigSeqClRole.
     * @param rect The rectangle used for finding the figure.
     * @return The contained figure, if hit, the containing figure otherwise.
     */
    public Fig hit(Rectangle rect) {
        Fig f = super.hit(rect);
        if (f instanceof FigSeqClRole) {
            f = ((FigSeqClRole)f).hitPort(rect);
        }
        return f;
    }

    // TODO: Currently does not predecessors ands successors; probably to
    //       implement in MessageSupervisor
    public FigSeqMessage addMessage(Class action, Fig dest, Fig source) {
        boolean mayCreate = true;
        GraphModel gm = getGraphModel();
        FigSeqClRoleLifeline[] lifeline = new FigSeqClRoleLifeline[2];
        lifeline[0] = ((FigSeqClRolePort)source).getBaseFig().getLifeline();
        if (dest instanceof FigSeqClRolePort) {
            lifeline[1] = ((FigSeqClRolePort)dest).getBaseFig().getLifeline();
            FigSeqClRolePort[] ports = new FigSeqClRolePort[4];
            ports[0] = lifeline[0].getConnectedPortBelow((FigSeqClRolePort)source);
            ports[1] = lifeline[0].getConnectedPortAbove((FigSeqClRolePort)source);
            ports[2] = lifeline[1].getConnectedPortBelow((FigSeqClRolePort)dest);
            ports[3] = lifeline[1].getConnectedPortAbove((FigSeqClRolePort)dest);
            int mx = lifeline[0].getLineCount() + lifeline[1].getLineCount() + 2;
            int[] pi = new int[4];
            pi[0] = (ports[0] == null) ? -1 : ports[0].getAbsoluteLineIndex();
            pi[1] = (ports[1] == null) ? mx : ports[1].getAbsoluteLineIndex();
            pi[2] = (ports[2] == null) ? -1 : ports[2].getAbsoluteLineIndex();
            pi[3] = (ports[3] == null) ? mx : ports[3].getAbsoluteLineIndex();
            if (pi[1] > pi[0]) pi[0] = pi[1];   // pi[0] is now the lower limit of both ends
            if (pi[2] > pi[3]) pi[2] = pi[3];   // pi[2] is now the upper limit of both ends
            if (pi[0] > pi[2]) mayCreate = false;   // messages are cross-ordered -> not allowed
            // TODO:
            // now create message
            //
            // createMessage(destPort, sourcePort, predecessors, successors);
            // createMessage(dest, source, ports[0].getMessages(), ports[1].getMessages);
        } else {
            lifeline[1] = ((FigSeqClRoleObject)dest).getBaseFig().getLifeline();
            FigSeqClRolePort[] ports = new FigSeqClRolePort[2];
            ports[0] = lifeline[0].getConnectedPortBelow((FigSeqClRolePort)source);
            ports[1] = lifeline[1].getFirstConnectedPort();
            int[] pi = new int[2];
            pi[0] = (ports[0] == null) ? -1 : ports[0].getAbsoluteLineIndex();
            pi[1] = (ports[1] == null) ? -1 : ports[1].getAbsoluteLineIndex();
            if (pi[1] >= 0 && pi[0] >= pi[1]) mayCreate = false;    // Object may not be created after usage
            // TODO:
            // now create message
            //
        }

        MMessage mm = UmlFactory.getFactory().getCollaborations().createMessage();
        try {
            MAction  ma = (MAction)action.newInstance();
            mm.setAction(ma);
            FigSeqMessage msg = new FigSeqMessage(gm, mm);
//            msg.setPath(path);
            msg.setPortFigs(new Fig[]{source, dest});
            return msg;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}