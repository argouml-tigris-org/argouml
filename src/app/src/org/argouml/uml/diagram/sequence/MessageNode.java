// $Id$
// Copyright (c) 2003-2006 The Regents of the University of California. All
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

package org.argouml.uml.diagram.sequence;

import java.util.List;

import org.argouml.uml.diagram.sequence.ui.FigMessagePort;
import org.argouml.uml.diagram.sequence.ui.FigClassifierRole;

/**
 * A Message Node for the sequence diagram.
 *
 */
public class MessageNode extends Object {

    /**
     * Nothing has happened yet.
     */
    public static final int INITIAL = 0;

    /**
     * At some later node, this object is created.
     */
    public static final int PRECREATED = 1;

    /**
     * Having done something without being called.
     */
    public static final int DONE_SOMETHING_NO_CALL = 2;

    /**
     * Having been called.
     */
    public static final int CALLED = 3;

    /**
     * Implicitly returned from a call.
     */
    public static final int IMPLICIT_RETURNED = 4;

    /**
     * Having been called with a return pending.
     */
    public static final int CREATED = 5;

    /**
     * After having returned from a call.
     */
    public static final int RETURNED = 6;

    /**
     * After the object is destroyed.
     */
    public static final int DESTROYED = 7;

    /**
     * Implicitly returned from being created.
     */
    public static final int IMPLICIT_CREATED = 8;

    // TODO: Need to replace this with FigMessage
    private FigMessagePort figMessagePort;
    private FigClassifierRole figClassifierRole;
    private int state;
    private List callers;

    /**
     * The constructor.
     *
     * @param owner the owner object
     */
    public MessageNode(FigClassifierRole owner) {
        figClassifierRole = owner;
        figMessagePort = null;
        state = INITIAL;
    }

    /**
     * TODO: Need to replace this with getFigMessage.
     *
     * @return the figMessagePort
     */
    public FigMessagePort getFigMessagePort() {
        return figMessagePort;
    }

    /**
     * @return the state
     */
    public int getState() {
        return state;
    }

    /**
     * @param st the state
     */
    public void setState(int st) {
        state = st;
    }

    /**
     * @param theCallers the callers
     */
    public void setCallers(List theCallers) {
        this.callers = theCallers;
    }
    
    /**
     * Get the list of callers
     * @return the caller list
     */
    public List getCallers() {
    	return callers;
    }

    /**
     * @return the figclassifierrole that is the owner
     */
    public FigClassifierRole getFigClassifierRole() {
        return figClassifierRole;
    }

    /**
     * @return the classifierrole
     */
    public Object getClassifierRole() {
        return figClassifierRole.getOwner();
    }

    /**
     * @param fmp the fig messageport
     */
    public void setFigMessagePort(FigMessagePort fmp) {
        figMessagePort = fmp;
    }

    public boolean canCall() {
        return figMessagePort == null
            && (state == INITIAL
                    || state == CREATED
                    || state == CALLED
                    || state == DONE_SOMETHING_NO_CALL
                    || state == IMPLICIT_RETURNED
                    || state == IMPLICIT_CREATED);
    }

    public boolean canBeCalled() {
        return figMessagePort == null
            && (state == INITIAL
                    || state == CREATED
                    || state == DONE_SOMETHING_NO_CALL
                    || state == CALLED
                    || state == RETURNED
                    || state == IMPLICIT_RETURNED
                    || state == IMPLICIT_CREATED);
    }

    public boolean canReturn(Object caller) {
        return figMessagePort == null
            && callers != null
            && callers.contains(caller);
    }

    public boolean canBeReturnedTo() {
        return figMessagePort == null
            && (state == DONE_SOMETHING_NO_CALL
                    || state == CALLED
                    || state == CREATED
                    || state == IMPLICIT_RETURNED
                    || state == IMPLICIT_CREATED);
    }

    public boolean matchingCallerList(Object caller, int callerIndex) {
        if (callers != null && callers.lastIndexOf(caller) == callerIndex) {
            if (state == IMPLICIT_RETURNED) {
                state = CALLED;
            }
            return true;
        }
        return false;
    }

    public boolean canCreate() {
        return canCall();
    }

    public boolean canDestroy() {
        return canCall();
    }

    public boolean canBeCreated() {
        return figMessagePort == null && state == INITIAL;
    }

    public boolean canBeDestroyed() {
        boolean destroyableNode =
            (figMessagePort == null
            && (state == DONE_SOMETHING_NO_CALL
                || state == CREATED
                || state == CALLED || state == RETURNED
                || state == IMPLICIT_RETURNED
                || state == IMPLICIT_CREATED));
        if (destroyableNode) {
            for (int i = figClassifierRole.getIndexOf(this) + 1;
                destroyableNode && i < figClassifierRole.getNodeCount(); ++i) {
                MessageNode node = figClassifierRole.getNode(i);
                if (node.getFigMessagePort() != null) {
                    destroyableNode = false;
                }
            }
        }
        return destroyableNode;
    }
}
