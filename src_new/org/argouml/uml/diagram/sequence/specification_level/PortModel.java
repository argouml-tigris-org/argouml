/*
 * PortModel.java
 *
 * Created on 31. Januar 2003, 18:21
 */

package org.argouml.uml.diagram.sequence.specification_level;

import java.awt.Rectangle;
import java.util.Vector;
import java.util.WeakHashMap;

import org.argouml.uml.diagram.sequence.specification_level.event.*;
import org.argouml.uml.diagram.sequence.specification_level.ui.FigSeqClRole;
import org.argouml.uml.diagram.sequence.specification_level.ui.FigSeqClRoleActivation;
import org.argouml.uml.diagram.sequence.specification_level.ui.FigSeqClRoleLifeline;
import org.argouml.uml.diagram.sequence.specification_level.ui.FigSeqClRoleObject;
import org.argouml.uml.diagram.sequence.specification_level.ui.FigSeqClRolePort;
import org.argouml.uml.diagram.sequence.specification_level.ui.FigSeqConstants;
import org.argouml.uml.diagram.sequence.specification_level.ui.FigSeqMessage;

import org.tigris.gef.presentation.FigPoly;

import ru.novosoft.uml.behavior.collaborations.MMessage;
import ru.novosoft.uml.behavior.common_behavior.MAction;
import ru.novosoft.uml.behavior.common_behavior.MDestroyAction;
import ru.novosoft.uml.behavior.common_behavior.MReturnAction;

/**
 * This class provides methods to connect messages to lifelines. The lines are to
 * be connected at every second port of a lifeline, so it will be easier to insert
 * a message between two others. Therefore the index of a connected port has to be
 * two times the line index plus one.
 *
 * Naming convention is idxLine (line index) for the vertical index of a port
 * and idxConn (connection index) for the index of (possibly) connected port.
 * <B>Note:</B> The connection index of a port can be the same for more than one
 * message (e.g. for messages which will split into different threads because of
 * different preconditions).
 * <B>Note:</B> If a method will be implemented to get the connection index of a
 * port, and the port does not belong to a message line (i.e. its line index is
 * even), the method must notify the caller by returning -1; otherwise it must
 * return (idxLine - 1) / 2.
 *
 * TODO: The flyweight pattern should be used for port implementation.
 *
 * @author Peter Nabbefeld
 * @version 0.0.1
 */
public class PortModel implements FigSeqConstants {

    ////////////////////////////////////////////////////////////////
    // instance variables

    /** The FigSeqClRoleLifeline to which the ports in this model belong to. */    
    private FigSeqClRoleLifeline oLine;
    /** The FigSeqClRoleObject to which the ports in this model belong to. */    
    private FigSeqClRoleObject oObject;
    /** The port data */    
    private FigSeqClRolePort[][][] aPorts;
    /** The listener registry */
    private WeakHashMap _listeners = new WeakHashMap();
    /** The creation line index, for position calculation of the object */
    private int _create = -1;
    /** The base number for line-to-port-index arithmetics */
    private int _base = 0;
    /**
     * The activation state of the object. Objects handled as active are:
     * - objects sending messages without previous receiving one
     * - objects created during the interaction are active during
     *   their lifetime.
     */
    private boolean _active = false;

    ////////////////////////////////////////////////////////////////
    // constructors

    /** Creates a new instance of PortModel
     * @param line The lifeline which will contain this PortModel instance.
     * @param ports An array containing at least two ports (the initial data of this PortModel).
     */
    public PortModel(FigSeqClRoleLifeline line, FigSeqClRolePort[] ports) {
        int mx = 0;
        int my = 0;
        int mz = 0;
        for(int i = 0; i < ports.length; i++) {
            if (ports[i].getThreadIndex() > mx) mx = ports[i].getThreadIndex();
            if (ports[i].getLineIndex()   > my) my = ports[i].getLineIndex();
            if (ports[i].getLevelIndex()  > mz) mz = ports[i].getLevelIndex();
        }
        if (my < 1) throw new RuntimeException("Portmodel cannot be created: too few message lines in initialization data");
        aPorts = new FigSeqClRolePort[my + 1][mx + 1][mz + 1];
        for(int i = 0; i < ports.length; i++) {
            aPorts[ports[i].getLineIndex()][ports[i].getThreadIndex()][ports[i].getLevelIndex()] = ports[i];
        }
        oLine = line;
System.out.println("oLine = " + oLine);
System.out.println("oLine.getBaseFig() = " + oLine.getBaseFig());
        oObject = line.getBaseFig().getFigObject();
    }

    /** Creates a new instance of PortModel
     * @param line The lifeline which will contain this PortModel instance.
     * @param ports A vector containing at least two ports (the initial data of this PortModel).
     */
    public PortModel(FigSeqClRoleLifeline line, Vector ports) {
        this(line, (FigSeqClRolePort[])ports.toArray(new FigSeqClRolePort[0]));
    }

    /** Creates a new instance of PortModel
     * @param line The lifeline which will contain this PortModel instance.
     */
    public PortModel(FigSeqClRoleLifeline line) {
        this(line, new FigSeqClRolePort[]{new FigSeqClRolePort(line, 0, 0, 0), new FigSeqClRolePort(line, 0, 0, 1)});
    }

    ////////////////////////////////////////////////////////////////
    // model change support

    /** This method inserts a line into the port model by copying the data of the
     * preceeding message index twice. If the port data is not sufficent, ports will
     * be appended upto the needed size. After the insertion the port is informed
     * which message fig should be informed about moving the port fig (as a result of
     * moving the object to which this port belongs).
     * @param idxConn The message line number; the line index of the inserted port
     * will be twice the line number plus one.
     * @param idxThread The thread (branch) index of the lifeline.
     * @param idxLevel The nesting level for calls to the object activation.
     * @param msg The message fig object to be connected to the inserted port.
     * @param sender The directional attribute of this port:
     * - true: this port (or, more exactly: the object to which this port belongs) is the sender of the message;
     * - false: this port is the receiver of the message.
     */    
    public void insertConnection(int idxConn, int idxThread, int idxLevel, FigSeqMessage msg, boolean sender) {
        insertConnectionPoint(idxConn);
        FigSeqClRolePort p = getPortForConnection(idxConn, idxThread, idxLevel);
        p.addMessage(msg, sender);
    }

    /** This method inserts a line into the port model by copying the data of the
     * preceeding message index twice. If the port data is not sufficent, ports will be
     * appended upto the needed size.
     * @param idxConn The message line number; the line index of the inserted port
     * will be twice the line number plus one.
     */    
    public void insertConnectionPoint(int idxConn) {
        if (idxConn < 0) return;
        int idxLine = idxConn * 2 + 1;
        if ((idxConn * 2 + 2) >= aPorts.length) {
            appendLines(idxLine - aPorts.length + 2);
        } else {
            insertConnectionPoint0(idxConn);
            fireModelChanged();
        }
    }

    /** This is a supporting method for <CODE>insertLine</CODE>. The parameter needs to have correct values, so it may only be used
     * from the method <CODE>insertLine</CODE>.
     * @param idxConn The message line number; the line index of the inserted port
     * will be twice the line number plus one.
     */
    private void insertConnectionPoint0(int idxConn) {
        int idxLine = idxConn * 2 + 1;
        FigSeqClRolePort[][][] nap = new FigSeqClRolePort[aPorts.length + 2][aPorts[0].length][aPorts[0][0].length];
        System.arraycopy(aPorts, 0, nap, 0, idxLine);
        System.arraycopy(aPorts, idxLine, nap, idxLine + 2, aPorts.length - idxLine);
        // Create new ports to insert from line before insertion
        for(int i = 2; i < 4; i++) {
            for(int j = 0; j < nap[i].length; j++) {
                for(int k = 0; k < nap[i][j].length; k++) {
                    if (aPorts[idxLine][j][k] != null) {
                        nap[idxConn * 2 + i][j][k] = new FigSeqClRolePort(oLine, j, k, i);
                    }
                }
            }
        }
        // Correct message indices
        for(int i = idxLine + 2; i < nap.length; i++) {
            for(int j = 0; j < nap[i].length; j++) {
                for(int k = 0; k < nap[i][j].length; k++) {
                    if (nap[i][j][k] != null) {
                        nap[i][j][k].setLineIndex(i);
                    }
                }
            }
        }
        aPorts = nap;
        nap = null;
    }

    /** This method appends the indicated number of connections.
     * @param cntConns The number of connections to be added.
     */    
    public void appendConnectionPoints(int cntConns) {
        if (cntConns < 1) return;
        int n = (aPorts.length == 2) ? 1 : 0;
        appendLines(cntConns * 2 - n);
    }

    /** This method appends the indicated number of lines.
     * @param cntLines The number of lines to be added.
     */    
    public void appendLines(int cntLines) {
        if (cntLines < 1) return;
        int max = aPorts.length + cntLines;
        int lst = aPorts.length - 1;
        // append lines upto max
        FigSeqClRolePort[][][] nap = new FigSeqClRolePort[max][aPorts[0].length][aPorts[0][0].length];
        System.arraycopy(aPorts, 0, nap, 0, aPorts.length);
        for(int i = aPorts.length; i < max; i++) {
            for(int j = 0; j < aPorts[lst].length; j++) {
                for(int k = 0; k < aPorts[lst][j].length; k++) {
                    if (aPorts[lst][j][k] != null) {
                        nap[i][j][k] = new FigSeqClRolePort(oLine, j, k, i);
                    }
                }
            }
        }
        aPorts = nap;
        nap = null;
        fireModelChanged();
    }

    /** This message delete a message line.
     * @param idxConn The number of the message line to delete.
     */    
    public void deleteConnection(int idxConn) {
        int idxLine = idxConn * 2 + 1;
        int cntPrt  = aPorts.length;
        if (cntPrt == 2 || idxLine >= cntPrt) return;
        if (cntPrt == 3) {
            // idxConn = 0, idxLine = 1
            deleteLine0(idxLine, 1);
        } else {
            deleteLine0(idxLine - 1, 2);
        }
        fireModelChanged();
    }

    /** This ist a support method for <code>deleteLine</CODE>.
     * @param idxLine
     * @param count
     */    
    private void deleteLine0(int idxLine, int count) {
        FigSeqClRolePort[][][] nap = new FigSeqClRolePort[aPorts.length - count][aPorts[0].length][aPorts[0][0].length];
        System.arraycopy(aPorts, 0, nap, 0, idxLine);
        System.arraycopy(aPorts, idxLine + count, nap, idxLine, nap.length - idxLine + 1);
        for(int i = idxLine; i < nap.length; i++) {
            for(int j = idxLine; j < nap[i].length; j++) {
                for(int k = idxLine; k < nap[i][j].length; k++) {
                    if (nap[i][j][k] != null) {
                        nap[i][j][k].setLineIndex(i);
                    }
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////
    // model access

    /** This method returns the Port at a given index in the port data.
     * @param idxLine The index to the port data.
     * @param idxThread The thread index, giving the index of the lifeline branch.
     * @param idxLevel The level index for nested calls to the same activation.
     * @return The FigSeqClRolePort object.
     */
    public FigSeqClRolePort getPort(int idxLine, int idxThread, int idxLevel) {
        if (aPorts.length <= idxLine) {
            int i = ((idxLine % 2) == 0) ? idxLine : idxLine - 1;
            insertConnectionPoint(i / 2);
        }
        return aPorts[idxLine][idxThread][idxLevel];
    }

    /** This method returns the Port at a given connection index.
     * @param idxConn The index to the connection.
     * @param idxThread The thread index, giving the index of the lifeline branch.
     * @param idxLevel The level index for nested calls to the same activation.
     * @return The FigSeqClRolePort object.
     */
    public FigSeqClRolePort getPortForConnection(int idxConn, int idxThread, int idxLevel) {
        return getPort(idxConn * 2 + 1, idxThread, idxLevel);
    }

    public void setPortsDisplayed(boolean d) {
        int i, j, k;
        int c = getLineCount();
        for(i = 0; i < c; i++) {
            for(j = 0; j < aPorts[i].length; j++) {
                for(k = 0; k < aPorts[i][j].length; k++) {
                    if (aPorts[i][j][k] != null) {
                        aPorts[i][j][k].setDisplayed(d);
                    }
                }
            }
        }
        for(i = c; i < aPorts.length; i++) {
            for(j = 0; j < aPorts[i].length; j++) {
                for(k = 0; k < aPorts[i][j].length; k++) {
                    if (aPorts[i][j][k] != null) {
                        aPorts[i][j][k].setDisplayed(false);
                    }
                }
            }
        }
        oLine.getBaseFig().damage();
    }


    /**
     * This method sets the creation line index. The line index is a counting number,
     * starting from the first port on any lifeline of an object, which is existing,
     * not created by any message in the diagram. An index less than 1 means an
     * existing object, any other index means that this object is created by a message
     * in the given line.
     * @param idx The creation line index.
     */
    public void setCreationLineIndex(int idx) {
        _create = idx;
        _base   = getBase(_create);
        _active = (_base > 0) ? true : _active;
    }

    /**
     * Informs the PortModel about the active state of the object. Requesting the
     * activations collection will produce a single activation bar in thread 0
     * for active objects.
     * @param a The new active state.
     */
    public void setActiveState(boolean a) {
        _active = a;
    }

    ////////////////////////////////////////////////////////////////
    // model query support

    /**
     * Get the line count. This is defined as the maximum count of ports in y-direction.
     * Needed for rendering.
     * @return The line count.
     */
    public int getLineCount() {
        MMessage mm = null;
        MAction  ma = null;
        int mp = -1;
        int md = -1;
        for(int i = aPorts.length - 1; i >= 0; i--) {
            if (aPorts[i][0][0] != null && aPorts[i][0][0].isConnected()) {
                if (mp < 0) mp = i;
                if (aPorts[i][0][0].isReceiver() && !aPorts[i][0][0].getMessagesForAction(MDestroyAction.class).isEmpty()) {
                    md = i;
                    mp = i;
                    break;
                }
            }
        }
        if (md < 0 && mp < 0) {
            return 2;
        } else if (md < 0) {
            return mp + 2;
        } else {
            return md + 1;
        }
    }

    /**
     * Get the termination state.
     * @return The termination state.
     */
    public boolean isTerminated() {
        MMessage mm = null;
        MAction  ma = null;
        int md = -1;
        for(int i = aPorts.length - 1; i >= 0; i--) {
            if (aPorts[i][0][0].isConnected()) {
                if (aPorts[i][0][0].isReceiver() && !aPorts[i][0][0].getMessagesForAction(MDestroyAction.class).isEmpty()) {
                    md = i;
                    break;
                }
            }
        }
        return (md >= 0);
    }

    /**
     * Get the maximum count of threads on this lifeline. Needed for rendering.
     * @return The maximum thread count.
     */
    public int getMaximumThreadCount() {
        return aPorts[0].length;
    }
    /**
     * Get the thread count in one line.
     * @param idxLine The line number.
     * @return The thread count.
     */
    public int getThreadCount(int idxLine) {
        int i = 0;
        for(; i < aPorts[idxLine].length && aPorts[idxLine][i][0] != null; i++);
        return i;
    }
    /**
     * Get the maximum number of nested call to an activation. An activation without
     * subsequent calls to itself has a level count of 1.
     * @return The level count.
     */
    public int getMaximumLevelCount() {
        return aPorts[0][0].length;
    }
    /**
     * Get the maximum number of nested call to an activation for a specific branch
     * (thread). An activation without subsequent calls to itself has a level count of 1.
     * @param idxLine The thread number.
     * @return The level count.
     */
    public int getMaximumLevelCount(int idxThread) {
        int i, j, r = 0;
        for(i = 0; i < aPorts.length; i++) {
            for(j = 0; j < aPorts[i].length && aPorts[i][j][0] != null; j++) {
                if (j > r) r = j;
            }
        }
        return r;
    }
    /**
     * Get the maximum number of nested call to an activation for the last (righmost)
     * thread. An activation without subsequent calls to itself has a level count of 1.
     * Needed for rendering.
     * @return The level count.
     */
    public int getMaximumLevelCountInLastThread() {
        return getMaximumLevelCount(getMaximumThreadCount() - 1);
    }

    /** This method returns the creation line index.
     * @see setCreationLineIndex
     * @return The creation line index.
     */
    public int getCreationLineIndex() {
        return _create;
    }

    private int getBase(int idxCreate) {
        return (idxCreate < 1) ? 0 : idxCreate + 2;
    }

    public int getObjectCreationY(int idxCreate) {
        if (idxCreate < 1) {
            return ORIGIN_Y;
        } else {
            int hs = LIFELINE_PORT_DIST + LIFELINE_PORT_SIZE;           // segment height
            int vs = (hs - 1) - (oObject.getHeight() - 1) % hs;
//        return (hs - 1) - (b.y + b.height - ORIGIN_Y - 1) % hs;
            int v0 = ORIGIN_Y + oObject.getHeight() / 2 + vs + hs * idxCreate;
            v0 += 2;            // Don't currently know why this is needed, but doesn't work
                                // correctly otherwise
            return v0;
        }
    }

    private int calculateHorizontalPortOffset() {
        Rectangle b = oObject.getBounds();
        return b.width / 2 - LIFELINE_PORT_SIZE / 2;
    }

    private int calculateHorizontalPortStart() {
        Rectangle b = oObject.getBounds();
        return b.x + calculateHorizontalPortOffset();
    }

    /**
     * Calculates the filler length from object to lifeline. This will enable
     * horizontal lines by aligning ports of different lifelines
     * (non-horizontal lines are used for delayed messages; this is currently
     * not supported)
     * @return The filler length
     */
    private int calculateVerticalPortOffset() {
        Rectangle b = oObject.getBounds();
        int hs = LIFELINE_PORT_DIST + LIFELINE_PORT_SIZE;           // segment height
        return (hs - 1) - (b.y + b.height - ORIGIN_Y - 1) % hs;
    }

    private int calculateVerticalPortStart() {
        Rectangle b = oObject.getBounds();
        return b.y + b.height + calculateVerticalPortOffset() + LIFELINE_PORT_DIST;
    }

    private int calculateLevelOffset() {
        return LIFELINE_ACTIVATION_WIDTH / 2;
    }

    private int calculateLineOffset() {
        return LIFELINE_PORT_DIST + LIFELINE_PORT_SIZE;
    }

    /**
     * As this method is called in preparation for painting, the x- and y-coordinates
     * are re-calculated.
     * @return A vector containing all FigSeqClRolePort objects in this model.
     */
    public Vector getPortFigs() {
        Vector r = new Vector();
        int x0 = calculateHorizontalPortStart();
        int y0 = calculateVerticalPortStart();
        int w1 = calculateLevelOffset();
        int w2 = LIFELINE_ACTIVATION_DIST;
        int h1 = calculateLineOffset();
        int[] aOff = new int[getMaximumThreadCount()];
        for(int i = 0; i < aOff.length; i++) {
            aOff[i] = getMaximumLevelCount(i) * w1 + w2 + LIFELINE_ACTIVATION_WIDTH;
        }
        int ofs;
        for(int i = 0; i < aPorts.length; i++) {                // line
            ofs = 0;
            for(int j = 0; j < aPorts[i].length; j++) {         // thread
                for(int k = 0; k < aPorts[i][j].length; k++) {  // level
                    if (aPorts[i][j][k] != null) {
                        aPorts[i][j][k].setLocation(x0 + ofs + k * w1, y0 + h1 * i);
                        r.add(aPorts[i][j][k]);
                    }
                }
                ofs += aOff[j];
            }
        }
        return r;
    }

    /**
     * This method is called in preparation for painting. To be sure the coordinates
     * are correct, getPortFigs() must be called first.
     * @return A vector containing all line segments.
     */
    public Vector getLineSegments() {
        Vector res = new Vector();
        int i, j, k;
        int s = 0;      // Must be initialized because of compiler check;
                        // value does not matter because split condition
                        // should be entered before join
        int w1 = LIFELINE_PORT_SIZE / 2;
        int h1 = calculateLineOffset() - LIFELINE_PORT_SIZE / 2;
        int h2 = calculateLineOffset();
        int h3 = h2 / 2;
        h1 += h3;
        int xt, xs, yt, ys;
        int tc = getMaximumThreadCount();
        FigPoly sgm;
        FigSeqClRoleActivation[] aActZero = (FigSeqClRoleActivation[])getActivations(0).toArray(new FigSeqClRoleActivation[0]);
        boolean[] aFlg = new boolean[aPorts.length];
        for(i = 0; i < aActZero.length; i++) {
            if (aActZero[i].getThreadIndex() == 0 && aActZero[i].getLevelIndex() == 0) {
                k = aActZero[i].getPortStartIndex();
                for(j = 0; j < aActZero[i].getPortCount(); j++) {
                    aFlg[k + j] = true;
                }
            }
        }
        k = -1;
        Rectangle b = oObject.getBounds();
        for(i = 0; i < aFlg.length; i++) {
            if (aFlg[i]) {
                if (i == 0 || !aFlg[i-1]) {
                    sgm = new FigPoly();
                    if (k < 0) {
                        sgm.addPoint(aPorts[0][0][0].getX() + w1, b.y + b.height);
                    } else {
                        sgm.addPoint(aPorts[0][0][0].getX() + w1, aPorts[k][0][0].getY() - h1 + h2 * 2);
                    }
                    sgm.addPoint(aPorts[i][0][0].getX() + w1, aPorts[i][0][0].getY() - h1 + h2);
                    sgm.setDashed(true);
                    res.add(sgm);
                }
            } else {
                if (i > 0 && aFlg[i-1]) {
                    k = i;
                }
            }
        }
        if (!isTerminated()) {
            sgm = new FigPoly();
            if (k < 0) {
                sgm.addPoint(aPorts[0][0][0].getX() + w1, b.y + b.height);
            } else {
                sgm.addPoint(aPorts[0][0][0].getX() + w1, aPorts[k][0][0].getY() - h1 + h2 * 2 + LIFELINE_TAIL);
            }
            sgm.addPoint(aPorts[i-1][0][0].getX() + w1, aPorts[i-1][0][0].getY() - h1 + h2 * 2 + LIFELINE_TAIL);
            sgm.setDashed(true);
            res.add(sgm);
        }
        for(j = 1; j < tc; j++) {                    // thread
            for(i = 1; i < aPorts.length; i++) {     // line
                if (aPorts[i][j][0] == null) {
                    if (aPorts[i - 1][j][0] != null) {
                        // join
                        xt = aPorts[i][i][0].getX();
                        yt = aPorts[i][i][0].getY();
                        xs = aPorts[s][i][0].getX();
                        ys = aPorts[s][i][0].getY();
                        sgm = new FigPoly();
                        sgm.addPoint(aPorts[i][j-1][0].getX() + w1, aPorts[i][j][0].getY() - h1);
                        sgm.addPoint(aPorts[i][j][0].getX() + w1, aPorts[i-1][0][0].getCenterY());
                        sgm.addPoint(aPorts[i][j][0].getX() + w1, aPorts[i][j][0].getY() - h1 + h2);
                        sgm.setDashed(true);
                        res.add(sgm);
                    }
                } else {
                    if (aPorts[i - 1][j][0] == null) {
                        // split
                        s = i;
                    }
                }
            }
        }
        return res;
    }

    public FigSeqClRolePort hitPort(Rectangle r) {
        FigSeqClRolePort f = null;
        boolean testP0 = (r.height < 2 && r.width < 2);
        Rectangle b = null;
        for(int i = 0; i < getLineCount(); i++) {
            for(int j = 0; j < aPorts[i].length; j++) {
                for(int k = 0; k < aPorts[i][j].length; k++) {
                    if (aPorts[i][j][k] != null) {
                        b = aPorts[i][j][k].getBounds();
                        if (testP0) {
                            if (b.contains(r.getLocation())) {
                                f = aPorts[i][j][k];
                                break;
                            }
                        } else {
                            if (r.contains(b)) {
                                f = aPorts[i][j][k];
                                break;
                            }
                        }
                    }
                }
            }
        }
        return f;
    }

    public FigSeqClRolePort getFirstConnectedPort() {
        FigSeqClRolePort p = null;
        int m = getLineCount() - 1;
        int j = 0;
        int k = 0;
        for(int i = 0; i <= m; i++) {
            if (aPorts[i][j][k].isConnected()) {
                p = aPorts[i][j][k];
                break;
            }
        }
        return p;
    }

    public FigSeqClRolePort getConnectedPortBelow(FigSeqClRolePort port) {
        FigSeqClRolePort p = null;
        int y = port.getLineIndex();
        if (y > 0) {
            int j = port.getThreadIndex();
            int k = port.getLevelIndex();
            for(int i = y - 1; i >= 0; i--) {
                if (aPorts[i][j][k].isConnected()) {
                    p = aPorts[i][j][k];
                    break;
                }
            }
        }
        return p;
    }

    public FigSeqClRolePort getConnectedPortAbove(FigSeqClRolePort port) {
        FigSeqClRolePort p = null;
        int m = getLineCount() - 1;
        int y = port.getLineIndex();
        if (y < m) {
            int j = port.getThreadIndex();
            int k = port.getLevelIndex();
            for(int i = y + 1; i <= m; i++) {
                if (aPorts[i][j][k].isConnected()) {
                    p = aPorts[i][j][k];
                    break;
                }
            }
        }
        return p;
    }

    ////////////////////////////////////////////////////////////////
    // activation rendering

    public Vector getActivations() {
        Vector res = new Vector();
        int m = getMaximumThreadCount();
        for(int i = 0; i < m; i++) {
            getActivations0(res, i);
        }
        return res;
    }
    public Vector getActivations(int idxThread) {
        Vector res = new Vector();
        getActivations0(res, idxThread);
        return res;
    }
    private void getActivations0(Vector v, int idxThread) {
        if (_active && idxThread == 0) {
            FigSeqClRoleActivation act = new FigSeqClRoleActivation(oLine, new FigSeqClRolePort[]{aPorts[0][0][0], aPorts[getLineCount() - 1][0][0]});
            act.setActiveState(_active);
            v.add(act);
            if (getMaximumLevelCount(0) > 0) getNestedCalls(v, idxThread, 0, getLineCount(), 1);
        } else {
            getNestedCalls(v, idxThread, 0, getLineCount(), 0);
        }
    }
    private void getNestedCalls(Vector v, int idxThread, int minPort, int maxPort, int idxLevel) {
        int prtStart = -1;
        int prtCount =  0;
        for(int i = 0; i < aPorts.length; i++) {
            if (aPorts[i][idxThread][idxLevel] == null || 
                  (aPorts[i][idxThread][idxLevel].isConnected() && 
                   aPorts[i][idxThread][idxLevel].isSender() && 
                   !aPorts[i][0][0].getMessagesForAction(MReturnAction.class).isEmpty()) ||
                   (idxThread == 0 && i == (aPorts.length - 1))
                   ) {
                if (prtStart >= 0) {
                    // end of activation reached
                    prtCount = i - prtStart;
                    if (aPorts[i][idxThread][idxLevel] != null &&
                        aPorts[i][idxThread][idxLevel].isConnected() &&
                        aPorts[i][idxThread][idxLevel].isSender() &&
                        !aPorts[i][0][0].getMessagesForAction(MReturnAction.class).isEmpty()) {
                        prtCount++;
                    }
//                    v.add(new FigSeqClRoleActivation(oLine, idxThread, idxLevel, prtStart, prtCount));
                    v.add(new FigSeqClRoleActivation(oLine, new FigSeqClRolePort[]{aPorts[prtStart][idxThread][idxLevel], aPorts[prtStart + prtCount - 1][idxThread][idxLevel]}));
                    if ((idxLevel + 1) < getMaximumLevelCount()) {
                        getNestedCalls(v, idxThread, prtStart, prtStart + prtCount, idxLevel + 1);
                    }
                    prtStart = -1;
                }
            } else {
                if (prtStart < 0 && aPorts[i][idxThread][idxLevel].isConnected()) {
                    // start of activation reached
                    prtStart = i;
                    prtCount = 0;
                }
            }
        }
        if (prtStart >= 0) {
            // end of activation reached
            prtCount = aPorts.length - prtStart;
            v.add(new FigSeqClRoleActivation(oLine, new FigSeqClRolePort[]{aPorts[prtStart][idxThread][idxLevel], aPorts[prtStart + prtCount - 1][idxThread][idxLevel]}));
            if ((idxLevel + 1) < getMaximumLevelCount()) {
                getNestedCalls(v, idxThread, prtStart, prtStart + prtCount, idxLevel + 1);
            }
            prtStart = -1;
        }
    }

    ////////////////////////////////////////////////////////////////
    // event handling

    public void addPortModelChangeListener(PortModelChangeListener pmcl) {
        _listeners.put(pmcl, null);
    }
    public void removePortModelChangeListener(PortModelChangeListener pmcl) {
        _listeners.remove(pmcl);
    }
    public void fireModelChanged() {
        PortModelChangeListener[] aPmcl = (PortModelChangeListener[])_listeners.keySet().toArray(new PortModelChangeListener[0]);
        PortModelChangeEvent      oPmce = new PortModelChangeEvent(this, PortModelChangeEvent.ANY_CHANGE);
        for(int i = 0; i < aPmcl.length; i++) {
            aPmcl[i].portModelChanged(oPmce);
        }
    }
}
