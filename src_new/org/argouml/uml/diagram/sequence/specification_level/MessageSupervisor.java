/*
 * MessageSupervisor.java
 *
 * Created on 16. Dezember 2002, 22:16
 */

package org.argouml.uml.diagram.sequence.specification_level;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import org.argouml.uml.diagram.sequence.specification_level.ui.FigSeqClRole;
import org.argouml.uml.diagram.sequence.specification_level.ui.FigSeqInteraction;
import org.argouml.uml.diagram.sequence.specification_level.ui.FigSeqMessage;
import org.argouml.uml.diagram.sequence.specification_level.ui.SequenceDiagramLayout;

import org.tigris.gef.presentation.Fig;

import ru.novosoft.uml.behavior.collaborations.MInteraction;
import ru.novosoft.uml.behavior.collaborations.MMessage;
import ru.novosoft.uml.behavior.common_behavior.MAction;
import ru.novosoft.uml.behavior.common_behavior.MCallAction;
import ru.novosoft.uml.behavior.common_behavior.MCreateAction;
import ru.novosoft.uml.behavior.common_behavior.MDestroyAction;
import ru.novosoft.uml.behavior.common_behavior.MReturnAction;
import ru.novosoft.uml.behavior.common_behavior.MSendAction;
import ru.novosoft.uml.behavior.common_behavior.MSignal;

/**
 *
 * @author  Administrator
 */
public class MessageSupervisor {

    private static Hashtable _instances = new Hashtable();

    private MessageContainer _messages = new MessageContainer();
    private Hashtable _msgref = new Hashtable();

    /**
     * Creates a new instance of MessageSupervisor
     */
    public static void initialize(SequenceDiagramLayout layer) {
        _instances.put(layer, new MessageSupervisor(layer));
    }

    /**
     * Creates a new instance of MessageSupervisor
     */
    public static MessageSupervisor getSupervisor(SequenceDiagramLayout layer) {
        if (!_instances.contains(layer)) {
            initialize(layer);
        }
        return (MessageSupervisor)_instances.get(layer);
    }

    /**
     * Creates a new instance of MessageSupervisor
     */
    private MessageSupervisor(SequenceDiagramLayout layer) {
        _instances.put(layer, this);
    }

    public static void addMessageFig(SequenceDiagramLayout layer, FigSeqMessage fig) {
        ((MessageSupervisor)_instances.get(layer)).addMessageFig(fig);
    }
    public void addMessageFig(FigSeqMessage fig) {
        _msgref.put(fig.getOwner(), fig);
    }

    public static FigSeqMessage getMessageFig(SequenceDiagramLayout layer, MMessage m) {
        return ((MessageSupervisor)_instances.get(layer)).getMessageFig(m);
    }
    public FigSeqMessage getMessageFig(MMessage m) {
        return (FigSeqMessage)_msgref.get(m);
    }

    /**
     * This method <B>checks</B> and <B>completes</B> messages from old-style sequence diagrams.
     * It also <B>deletes</B> return actions without preceding calls.
     * @param layer The SequenceDiagramLayout for which to create the MessageContainer
     * @param messages The Vector of MMessages from which to create the MessageContainer
     */
    public static void createContainerFromConvertedMessages(SequenceDiagramLayout layer, Vector messages) {
        ((MessageSupervisor)_instances.get(layer)).createContainerFromConvertedMessages(messages);
    }
    public void createContainerFromConvertedMessages(Vector messages) {
        // Search for FigSeqMessages and add predecessors and successors
        if (messages == null || messages.size() < 1) {
            _messages = new MessageContainer();
        } else {
            MMessage[]    aMessages = (MMessage[])messages.toArray(new MMessage[0]);
            FigSeqMessage oFigMsgCu = null; // Current message
            FigSeqMessage oFigMsgPr = null; // Predecessor
            Vector msgs = new Vector();
            // Initialize figs
            for(int i = 0; i < aMessages.length; i++) {
                oFigMsgCu = (FigSeqMessage)_msgref.get(aMessages[i]);
                oFigMsgCu.clearPredecessors();
                oFigMsgCu.clearSuccessors();
                oFigMsgCu.clearSplitReference();
                msgs.add(oFigMsgCu);
            }
            aMessages = null;
            FigSeqMessage[] aFigMsg = (FigSeqMessage[])msgs.toArray(new FigSeqMessage[0]);
            // Set predecessors and successors
            for(int i = 1; i < aFigMsg.length; i++) {
                aFigMsg[i].addPredecessor(aFigMsg[i-1]);
                aFigMsg[i-1].addSuccessor(aFigMsg[i]);
            }
            // Set split references (= first figure in thread, after thread splitting)
            for(int i = 0; i < aFigMsg.length; i++) {
                aFigMsg[i].setSplitReference(aFigMsg[0]);
            }
            // Create MessageContainer
            _messages = new MessageContainer(new Vector(Arrays.asList(aFigMsg)), new int[]{0});
        }
    }

    public static Hashtable getObjectReferences(SequenceDiagramLayout layer) {
        Hashtable reftable = new Hashtable();
        Iterator  it  = layer.getContents().iterator();
        Object    obj = null;
        while (it.hasNext()) {
            obj = it.next();
            if (obj instanceof FigSeqClRole) {
                reftable.put(((FigSeqClRole)obj).getOwner(), obj);
            }
        }
        return reftable;
    }

    /**
     * This method converts an interaction from XMI file.
     */
    public static void createContainerFromModel(SequenceDiagramLayout layer) {
        ((MessageSupervisor)_instances.get(layer)).createContainerFromModel((Fig[])layer.getContents().toArray(new Fig[0]));
    }

    public void createContainerFromModel(Fig aFigs[]) {
        MInteraction mi = null;
        for(int i = 0; i < aFigs.length; i++) {
            if (aFigs[i] instanceof FigSeqMessage) {
                addMessageFig((FigSeqMessage)aFigs[i]);
            } else if (aFigs[i] instanceof FigSeqInteraction) {
                mi = (MInteraction)aFigs[i].getOwner();
            }
        }
        createContainerFromModel(mi);
    }

    private void createContainerFromModel(MInteraction inter) {
        if (inter == null) {
            _messages =  new MessageContainer();
        } else {
            MMessage[] aMsgs = (MMessage[])inter.getMessages().toArray(new MMessage[0]);
            for(int i = 0; i < aMsgs.length; i++) {
                FigSeqMessage mFig = getMessageFig(aMsgs[i]);
                mFig.clearPredecessors();
                mFig.clearSuccessors();
                mFig.clearSplitReference();
                MMessage[] aPred = (MMessage[])aMsgs[i].getPredecessors().toArray(new MMessage[0]);
                for(int j = 0; j < aPred.length; j++) {
                    mFig.addPredecessor(getMessageFig(aPred[j]));
                }
            }
            for(int i = 0; i < aMsgs.length; i++) {
                FigSeqMessage mFig = getMessageFig(aMsgs[i]);
                FigSeqMessage[] aPred = (FigSeqMessage[])mFig.getPredecessors().toArray(new FigSeqMessage[0]);
                for(int j = 0; j < aPred.length; j++) {
                    aPred[j].addSuccessor(mFig);
                }
            }
            Vector vThd = new Vector();
            for(int i = 0; i < aMsgs.length; i++) {
                if (aMsgs[i].getPredecessors().isEmpty()) {
                    vThd.add(new Integer(i));
                }
            }
            FigSeqMessage[] aFigMsg = new FigSeqMessage[aMsgs.length];
            for(int i = 0; i < aMsgs.length; i++) {
                aFigMsg[i] = getMessageFig(aMsgs[i]);
            }
            int[] thd = new int[vThd.size()];
            for(int i = 0; i < thd.length; i++) {
                thd[i] = ((Integer)vThd.get(i)).intValue();
            }
            // Set split references (= first figure in thread, after thread splitting)
            for(int i = 0; i < thd.length; i++) {
                setSplitReferences(aFigMsg, aFigMsg[thd[i]]);
            }
            aMsgs = null;
            vThd  = null;
            _messages = new MessageContainer(new Vector(Arrays.asList(aFigMsg)), thd);
        }
    }

    private FigSeqMessage setSplitReferences(FigSeqMessage[] amsg, FigSeqMessage smsg) {
        FigSeqMessage[] succ;
        FigSeqMessage   msg = smsg;
        for(;;) {
            msg.setSplitReference(smsg);
            succ = (FigSeqMessage[])msg.getSuccessors().toArray(new FigSeqMessage[0]);
            if (succ.length < 1) break;
            if (succ.length > 1) {
                for(int i = 0; i < succ.length; i++) {
                    msg = setSplitReferences(amsg, succ[i]);
                }
                if (msg == null) break;
            } else {
                msg = succ[0];
                if (msg.getPredecessors().size() > 1) return msg;
            }
        }
        return null;
    }

    ////////////////////////////////////////////////////////////////
    // accessor methods

    public static MessageSupervisor getMessageSupervisorForDiagram(SequenceDiagramLayout layer) {
        return (MessageSupervisor)_instances.get(layer);
    }

    public FigSeqMessage getFigure(MMessage msg) {
        return (FigSeqMessage)_msgref.get(msg);
    }

    public static Vector calculateLines(SequenceDiagramLayout layer) {
        return ((MessageSupervisor)_instances.get(layer))._messages.calculateLines();
    }

    ////////////////////////////////////////////////////////////////
    // public class LineDescriptor

    public class LineDescriptor {
        private int _idxLine;
        private Object msgs = null;
        class LineElement {
            public int thread;
            public FigSeqMessage figMsg;
            public int idxSndLifeline;
            public int idxRcvLifeline;
            public LineElement(int t, FigSeqMessage m, int s, int r) {
                thread = t;
                figMsg = m;
                idxSndLifeline = s;
                idxRcvLifeline = r;
            }
            public LineElement(int t, FigSeqMessage m) {
                this(t, m, -1, -1);
            }
        }
        public LineDescriptor(int idxLine) {
            _idxLine = idxLine;
        }
        public void addMessage(FigSeqMessage msg, int idxThread) {
            if (msgs == null) {
                msgs = new LineElement(idxThread, msg);
            } else if (msgs instanceof Vector) {
                ((Vector)msgs).add(new LineElement(idxThread, msg));
            } else {
                Vector v = new Vector(2, 2);
                v.add(msgs);
                v.add(new LineElement(idxThread, msg));
            }
        }
        public LineElement[] toArray() {
            if (msgs == null) {
                return new LineElement[0];
            } else if (msgs instanceof Vector) {
                return (LineElement[])((Vector)msgs).toArray(new LineElement[0]);
            } else {
                return new LineElement[]{(LineElement)msgs};
            }
        }
        public FigSeqMessage[] getMessages() {
            LineElement[]   lines = toArray();
            FigSeqMessage[] msgs  = new FigSeqMessage[lines.length];
            for(int i = 0; i < lines.length; i++) {
                msgs[i] = lines[i].figMsg;
            }
            return msgs;
        }
        public int[] getThreadNumbers() {
            LineElement[] lines = toArray();
            int[] threads = new int[lines.length];
            for(int i = 0; i < lines.length; i++) {
                threads[i] = lines[i].thread;
            }
            return threads;
        }
        public int[] getSenderThreads() {
            LineElement[] lines = toArray();
            int[] threads = new int[lines.length];
            for(int i = 0; i < lines.length; i++) {
                threads[i] = lines[i].idxSndLifeline;
            }
            return threads;
        }
        public int[] getReceiverThreads() {
            LineElement[] lines = toArray();
            int[] threads = new int[lines.length];
            for(int i = 0; i < lines.length; i++) {
                threads[i] = lines[i].idxRcvLifeline;
            }
            return threads;
        }
        LineElement get(int idx) {
            if (msgs == null) {
                return null;
            } else if (msgs instanceof Vector) {
                return (LineElement)((Vector)msgs).get(idx);
            } else if (idx == 0) {
                return (LineElement)msgs;
            }
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////
    // public class MessageContainer

    private class MessageContainer {
        Vector _messages = new Vector();    // Vector of FigSeqMessages
        int[]  _threads  = null;
        int    _idxThrd  = 0;
        /**
         * The private class LineContainer manages references to multiple messages
         * per line, caused by different threads.
         */
        public MessageContainer() {
        }
        public MessageContainer(Vector messages, int[] threads) {
            _messages = (Vector)messages.clone();
            _threads  = threads;
        }
        public Vector calculateLines() {
            Vector lines = new Vector();
            for(int i = 0; i < _threads.length; i++) {
                calculateLineValuesForSubMessages(lines, 0, (FigSeqMessage)_messages.get(_threads[i]));
            }
            calculateThreadIndices(lines);
//            for(int i = 0; i < lines.size(); i++) {
//                lines.set(i, ((LineDescriptor)lines.get(i)).toArray());
//            }
            return lines;
        }
        public void calculateThreadIndices(Vector lines) {
            Hashtable htbObj = new Hashtable();
            Object  o;
            Vector  v;
            Integer n;
            LineDescriptor ld;
            FigSeqMessage[] m;
            int[] t;
            LineDescriptor.LineElement le;
            for(int i = 0; i < lines.size(); i++) {
System.out.println("lines(i).type = " + lines.get(i).getClass().getName());
                ld = (LineDescriptor)lines.get(i);
                m  = ld.getMessages();
                t  = ld.getThreadNumbers();
                for(int j = 0; j < m.length; j++) {
                    o = ((MMessage)m[j].getOwner()).getSender();
                    v = (Vector)htbObj.get(o);
                    if (v == null) v = new Vector(2, 2);
                    n = new Integer(t[j]);
                    if (!v.contains(n)) v.add(n);
                    htbObj.put(o, v);
                    ld.get(j).idxSndLifeline = v.indexOf(n);
                    o = ((MMessage)m[j].getOwner()).getReceiver();
                    v = (Vector)htbObj.get(o);
                    if (v == null) v = new Vector(2, 2);
                    n = new Integer(t[j]);
                    if (!v.contains(n)) v.add(n);
                    htbObj.put(o, v);
                    ld.get(j).idxRcvLifeline = v.indexOf(n);
                }
            }
//            cntObj = htbObj.size();
        }
        private FigSeqMessage calculateLineValuesForSubMessages(Vector lines, int startLine, FigSeqMessage oMsgStart) {
            int myThread = _idxThrd++;
            FigSeqMessage oMsg    = oMsgStart;
            int           curLine = startLine;
            Vector        vSplit  = oMsgStart.getPredecessors();
            FigSeqMessage oSplit  = (vSplit == null || vSplit.size() != 1) ? null : ((FigSeqMessage)vSplit.get(0)).getSplitReference();
            Vector        vSucc;
            while (oMsg != null && oMsg.getSplitReference() != oSplit) {
                if (lines.size() <= curLine) {
                    lines.add(new LineDescriptor(curLine));
                }
                ((LineDescriptor)lines.get(curLine)).addMessage(oMsg, myThread);
                curLine++;
                vSucc = oMsg.getSuccessors();
                if (vSucc != null && vSucc.size() > 0) {
                    if (vSucc.size() == 1) {
                        oMsg = (FigSeqMessage)vSucc.get(0);
                    } else {
                        // Split threads
                        for(int i = 0; i < vSucc.size(); i++) {
                            oMsg = calculateLineValuesForSubMessages(lines, curLine, (FigSeqMessage)vSucc.get(i));
                        }
                        curLine = lines.size();
                    }
                } else {
                    oMsg = null;
                }
            }
            return oMsg;
        }
    }

}