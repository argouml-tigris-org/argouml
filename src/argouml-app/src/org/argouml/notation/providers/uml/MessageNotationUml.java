// $Id$
// Copyright (c) 2006-2009 The Regents of the University of California. All
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

package org.argouml.notation.providers.uml;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.argouml.model.Model;
import org.argouml.notation.NotationSettings;

/**
 * The UML notation for a message, as shown on a collaboration diagram. <p>
 *
 * The Message notation syntax is a line of the following form,
 * which we can generate and parse: <p>
 *
 * <pre>
 * intno := integer|name
 * seq := intno ['.' intno]*
 * recurrance := '*'['//'] 
 *      | '*'['//']'[' <code>iteration </code>']' 
 *      | '['<code>condition </code>']'
 * seqelem := {[intno] ['['recurrance']']}
 * seq_expr := seqelem ['.' seqelem]*
 * ret_list := lvalue [',' lvalue]*
 * arg_list := rvalue [',' rvalue]*
 * predecessor := seq [',' seq]* '/'
 * message := [predecessor] seq_expr ':' [ret_list :=] name ([arg_list])
 * </pre> <p>
 *
 * Which is rather complex, so a few examples:<p><ul>
 * <li> 2: display(x, y)
 * <li> 1.3.1: p := find(specs)
 * <li> [x &lt; 0] 4: invert(color)
 * <li> A3, B4/ C3.1*: update()
 * </ul><p>
 *
 * This syntax is compatible with the UML 1.4.2 specification.<p>
 * 
 * TODO: The '//' in the recurrance should be '||' according the standard.
See issue 5606. <p>
 *
 * Actually, only a subset of this syntax is currently supported, and some
 * is not even planned to be supported. The exceptions are intno, which
 * allows a number possibly followed by a sequence of letters in the range
 * 'a' - 'z', seqelem, which does not allow a recurrance, and message, which
 * does allow one recurrance near seq_expr. (formerly: name: action )
 *
 * @author michiel
 */
public class MessageNotationUml extends AbstractMessageNotationUml {

    /**
     * The standard error etc. logger
     */
    static final Logger LOG =
        Logger.getLogger(MessageNotationUml.class);

    /**
     * The constructor.
     *
     * @param message the UML object
     */
    public MessageNotationUml(Object message) {
        super(message);
    }

    @Override
    public String toString(Object modelElement, NotationSettings settings) {
        return toString(modelElement);
    }

    /*
     * Generate a textual description for a Message m.
     *
     * @see org.argouml.notation.NotationProvider#toString(java.lang.Object, 
     * java.util.Map)
     * @deprecated
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public String toString(final Object modelElement, final Map args) {
        return toString(modelElement);
    }

    private String toString(final Object umlMessage) {
        Iterator it;
        Collection umlPredecessors;
        Object umlAction;
        Object umlActivator; // this is a Message UML object
        MsgPtr ptr;
        int lpn;

        /* Supported format: 
         *     predecessors number ":" action
         * The 3 parts of the string to generate: */
        StringBuilder predecessors = new StringBuilder(); // includes the "/"
        String number; // the "seq_expr" from the header javadoc
        // the ":" is not included in "number" - it is always present
        String action = "";

        if (umlMessage == null) {
            return "";
        }

        ptr = new MsgPtr();
        lpn = recCountPredecessors(umlMessage, ptr) + 1;
        umlActivator = Model.getFacade().getActivator(umlMessage);

        umlPredecessors = Model.getFacade().getPredecessors(umlMessage);
        it = (umlPredecessors != null) ? umlPredecessors.iterator() : null;
        if (it != null && it.hasNext()) {
            MsgPtr ptr2 = new MsgPtr();
            int precnt = 0;

            while (it.hasNext()) {
                Object msg = /*(MMessage)*/ it.next();
                int mpn = recCountPredecessors(msg, ptr2) + 1;

                if (mpn == lpn - 1
                    && umlActivator == Model.getFacade().getActivator(msg)
                    && Model.getFacade().getPredecessors(msg).size() < 2
                    && (ptr2.message == null
                        || countSuccessors(ptr2.message) < 2)) {
                    continue;
                }

                if (predecessors.length() > 0) {
                    predecessors.append(", ");
                }
                predecessors.append(
                        generateMessageNumber(msg, ptr2.message, mpn));
                precnt++;
            }

            if (precnt > 0) {
                predecessors.append(" / ");
            }
        }

        number = generateMessageNumber(umlMessage, ptr.message, lpn);

        umlAction = Model.getFacade().getAction(umlMessage);
        if (umlAction != null) {
            if (Model.getFacade().getRecurrence(umlAction) != null) {
                number = generateRecurrence(
                        Model.getFacade().getRecurrence(umlAction))
                    + " "
                    + number;
                /* TODO: The recurrence goes in front of the action? 
                 * Does this not contradict the header JavaDoc? */
            }

            action = NotationUtilityUml.generateActionSequence(umlAction);

            /* Dirty fix for issue 1758 (Needs to be amended
             * when we start supporting parameters):
             */
            if (!action.endsWith(")")) {
        	action = action + "()";
            }
        }

        return predecessors + number + " : " + action;
    }

}
