// $Id$
// Copyright (c) 2009 The Regents of the University of California. All
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoHelpEvent;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.notation.Notation;
import org.argouml.notation.providers.MessageNotation;
import org.argouml.util.CustomSeparator;
import org.argouml.util.MyTokenizer;

/**
 * This abstract class provides the common functionality for
 * the UML notation of messages.<br/>
 * It is extended by {@link MessageNotationUml}, with the 
 * notation of messages as seen in collaboration diagrams, 
 * and {@link SDMessageNotationUml}, with the notation of 
 * messages as seen in sequence diagrams.<p>
 * 
 * Parses a message line of the form:
 *
 * <pre>
 * intno := integer|name
 * seq := intno ['.' intno]*
 * recurrence := '*'['//'] | '*'['//']'[' <i>iteration </i>']' | '['
 * <i>condition </i>']'
 * seqelem := {[intno] ['['recurrence']']}
 * seq_expr := seqelem ['.' seqelem]*
 * ret_list := lvalue [',' lvalue]*
 * arg_list := rvalue [',' rvalue]*
 * predecessor := seq [',' seq]* '/'
 * message := [predecessor] seq_expr ':' [ret_list :=] name ([arg_list])
 * </pre>
 *
 * Which is rather complex, so a few examples:<ul>
 * <li> 2: display(x, y)
 * <li> 1.3.1: p := find(specs)
 * <li> [x &lt; 0] 4: invert(color)
 * <li> A3, B4/ C3.1*: update()
 * </ul>
 *
 * This syntax is compatible with the UML 1.4.2 specification.<p>
 *
 * Actually, only a subset of this syntax is currently supported, and some
 * is not even planned to be supported. The exceptions are intno, which
 * allows a number possibly followed by a sequence of letters in the range
 * 'a' - 'z', seqelem, which does not allow a recurrence, and message, which
 * does allow one recurrence near seq_expr. (formerly: name: action )
 *
 *
 * @see MessageNotationUml
 * @see SDMessageNotationUml
 * @since 0.28.alpha1
 * @author penyaskito
 */
public abstract class AbstractMessageNotationUml extends MessageNotation {

    private static final Logger LOG =
        Logger.getLogger(AbstractMessageNotationUml.class);
    
    /**
     * The list of CustomSeparators to use when tokenizing parameters.
     */
    private final List<CustomSeparator> parameterCustomSep;

    protected static class MsgPtr {
            /**
             * The message pointed to.
             */
            public Object message;
    }

    /**
     * @param message the UML Message object
     */
    public AbstractMessageNotationUml(Object message) {
        super(message);
        parameterCustomSep = initParameterSeparators();
    }

    protected String toString(final Object umlMessage, 
            boolean showSequenceNumbers) {
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

        if (!showSequenceNumbers) {
            return action;
        }
        return predecessors + number + " : " + action;
    }

    
    protected List<CustomSeparator> initParameterSeparators() {
        List<CustomSeparator> separators = new ArrayList<CustomSeparator>();
        separators.add(MyTokenizer.SINGLE_QUOTED_SEPARATOR);
        separators.add(MyTokenizer.DOUBLE_QUOTED_SEPARATOR);
        separators.add(MyTokenizer.PAREN_EXPR_STRING_SEPARATOR);        
        return separators;
    }

    public void parse(final Object modelElement, final String text) {
        try {
            parseMessage(modelElement, text);
        } catch (ParseException pe) {
            final String msg = "statusmsg.bar.error.parsing.message";
            final Object[] args = {pe.getLocalizedMessage(),
                Integer.valueOf(pe.getErrorOffset()), };
            ArgoEventPump.fireEvent(new ArgoHelpEvent(
                    ArgoEventTypes.HELP_CHANGED, this,
                    Translator.messageFormat(msg, args)));
        }
    }

    public String getParsingHelp() {
        return "parsing.help.fig-message";
    }

    /**
     * TODO: Document syntax generated here. 
     * TODO: Document exceptional behaviour.
     * 
     * @param m the UML message object to generate the sequence number for
     * @param pre the predecessor message (UML object)
     * @param position the integer position of the given message
     * @return the generated sequence expression
     */
    protected String generateMessageNumber(Object/*MMessage*/ m, 
            Object/*MMessage*/ pre,
            int position) {
        Collection c;
        Iterator it;
        String mname = "";
        Object act;
        int subpos = 0, submax = 1;

        if (m == null) {
            return null;
        }

        act = Model.getFacade().getActivator(m);
        if (act != null) {
            mname = generateMessageNumber(act);
        }

        if (pre != null) {
            c = Model.getFacade().getSuccessors(pre);
            submax = c.size();
            it = c.iterator();
            while (it.hasNext() && it.next() != m) {
                subpos++;
            }
        }

        if (mname.length() > 0) {
            if (submax > 1) {
                return mname + "." + position + (char) ('a' + subpos);
            }
            return mname + "." + position;
        }

        if (submax > 1) {
            return Integer.toString(position) + (char) ('a' + subpos);
        }
        return Integer.toString(position);
    }

    /**
     * Finds the break between message number and (possibly) message order.
     *
     * @return The position of the end of the number.
     */
    private static int findMsgOrderBreak(String s) {
        int i, t;
    
        t = s.length();
        for (i = 0; i < t; i++) {
            char c = s.charAt(i);
            if (c < '0' || c > '9') {
                break;
            }
        }
        return i;
    }

    /**
     * Generates the textual number of Message m. The number is a string
     * of numbers separated by points which describes the message's order
     * and level in a collaboration.<p>
     *
     * If you plan to modify this number, make sure that
     * the parsing of the Message is adapted accordingly to the change.
     *
     * @param message A Message to generate the number for.
     * @return A String with the message number of m.
     */
    private String generateMessageNumber(Object message) {
        MsgPtr ptr = new MsgPtr();
        int pos = recCountPredecessors(message, ptr) + 1;
        return generateMessageNumber(message, ptr.message, pos);
    }

    protected int countSuccessors(Object message) {
        int count = 0;
        final Object activator = Model.getFacade().getActivator(message);
        final Collection successors = Model.getFacade().getSuccessors(message);
        if (successors != null) {
            for (Object suc : successors) {
                if (Model.getFacade().getActivator(suc) != activator) {
                    continue;
                }
                count++;
            }
        }
        return count;
    }

    /**
     * Generates a textual description of a MIterationExpression.
     *
     * @param expr the given expression
     * @return the string
     */
    protected String generateRecurrence(Object expr) {
        if (expr == null) {
            return "";
        }
    
        return Model.getFacade().getBody(expr).toString();
    }

    /**
     * Parse a Message textual description.<p>
     *
     * TODO: - This method is too complex, lets break it up. <p>
     *
     * @param mes the MMessage to apply any changes to
     * @param s   the String to parse
     * @throws ParseException
     *            when it detects an error in the attribute string. See also
     *            ParseError.getErrorOffset().
     */
    protected void parseMessage(Object mes, String s)
        throws ParseException {
        String fname = null;
        StringBuilder guard = null;
        String paramExpr = null;
        String token;
        StringBuilder varname = null;
        List<List> predecessors = new ArrayList<List>();
        List<Integer> seqno = null;
        List<Integer> currentseq = new ArrayList<Integer>();
        List<String> args = null;
        boolean mustBePre = false;
        boolean mustBeSeq = false;
        boolean parallell = false;
        boolean iterative = false;
        boolean mayDeleteExpr = false;
        boolean refindOperation = false;
        boolean hasPredecessors = false;
        int i;

        currentseq.add(null);
        currentseq.add(null);

        try {
            MyTokenizer st = new MyTokenizer(s, " ,\t,*,[,],.,:,=,/,\\,",
                    MyTokenizer.PAREN_EXPR_STRING_SEPARATOR);

            while (st.hasMoreTokens()) {
                token = st.nextToken();

                if (" ".equals(token) || "\t".equals(token)) {
                    if (currentseq == null) {
                        if (varname != null && fname == null) {
                            varname.append(token);
                        }
                    }
                } else if ("[".equals(token)) {
                    if (mustBePre) {
                        String msg = "parsing.error.message.pred-unqualified";
                        throw new ParseException(Translator.localize(msg),
                                st.getTokenIndex());
                    }
                    mustBeSeq = true;

                    if (guard != null) {
                        String msg = "parsing.error.message.several-specs";
                        throw new ParseException(Translator.localize(msg),
                                st.getTokenIndex());
                    }

                    guard = new StringBuilder();
                    while (true) {
                        token = st.nextToken();
                        if ("]".equals(token)) {
                            break;
                        }
                        guard.append(token);
                    }
                } else if ("*".equals(token)) {
                    if (mustBePre) {
                        String msg = "parsing.error.message.pred-unqualified";
                        throw new ParseException(Translator.localize(msg),
                                st.getTokenIndex());
                    }
                    mustBeSeq = true;

                    if (currentseq != null) {
                        iterative = true;
                    }
                } else if (".".equals(token)) {
                    if (currentseq == null) {
                        String msg = "parsing.error.message.unexpected-dot";
                        throw new ParseException(Translator.localize(msg),
                                st.getTokenIndex());
                    }
                    if (currentseq.get(currentseq.size() - 2) != null
                            || currentseq.get(currentseq.size() - 1) != null) {
                        currentseq.add(null);
                        currentseq.add(null);
                    }
                } else if (":".equals(token)) {
                    if (st.hasMoreTokens()) {
                        String t = st.nextToken();
                        if ("=".equals(t)) {
                            st.putToken(":=");
                            continue;
                        }
                        st.putToken(t);
                    }

                    if (mustBePre) {
                        String msg = "parsing.error.message.pred-colon";
                        throw new ParseException(Translator.localize(msg),
                                st.getTokenIndex());
                    }

                    if (currentseq != null) {
                        if (currentseq.size() > 2
                            && currentseq.get(currentseq.size() - 2) == null
                            && currentseq.get(currentseq.size() - 1) == null) {
                            currentseq.remove(currentseq.size() - 1);
                            currentseq.remove(currentseq.size() - 1);
                        }

                        seqno = currentseq;
                        currentseq = null;
                        mayDeleteExpr = true;
                    }
                } else if ("/".equals(token)) {
                    if (st.hasMoreTokens()) {
                        String t = st.nextToken();
                        if ("/".equals(t)) {
                            st.putToken("//");
                            continue;
                        }
                        st.putToken(t);
                    }

                    if (mustBeSeq) {
                        String msg = "parsing.error.message.sequence-slash";
                        throw new ParseException(Translator.localize(msg),
                                st.getTokenIndex());
                    }

                    mustBePre = false;
                    mustBeSeq = true;

                    if (currentseq.size() > 2
                            && currentseq.get(currentseq.size() - 2) == null
                            && currentseq.get(currentseq.size() - 1) == null) {
                        currentseq.remove(currentseq.size() - 1);
                        currentseq.remove(currentseq.size() - 1);
                    }

                    if (currentseq.get(currentseq.size() - 2) != null
                            || currentseq.get(currentseq.size() - 1) != null) {

                        predecessors.add(currentseq);

                        currentseq = new ArrayList<Integer>();
                        currentseq.add(null);
                        currentseq.add(null);
                    }
                    hasPredecessors = true;
                } else if ("//".equals(token)) {
                    if (mustBePre) {
                        String msg = "parsing.error.message.pred-parallelized";
                        throw new ParseException(Translator.localize(msg),
                                st.getTokenIndex());
                    }
                    mustBeSeq = true;

                    if (currentseq != null) {
                        parallell = true;
                    }
                } else if (",".equals(token)) {
                    if (currentseq != null) {
                        if (mustBeSeq) {
                            String msg = "parsing.error.message.many-numbers";
                            throw new ParseException(Translator.localize(msg),
                                    st.getTokenIndex());
                        }
                        mustBePre = true;

                        if (currentseq.size() > 2
                            && currentseq.get(currentseq.size() - 2) == null
                            && currentseq.get(currentseq.size() - 1) == null) {

                            currentseq.remove(currentseq.size() - 1);
                            currentseq.remove(currentseq.size() - 1);
                        }

                        if (currentseq.get(currentseq.size() - 2) != null
                            || currentseq.get(currentseq.size() - 1) != null) {

                            predecessors.add(currentseq);

                            currentseq = new ArrayList<Integer>();
                            currentseq.add(null);
                            currentseq.add(null);
                        }
                        hasPredecessors = true;
                    } else {
                        if (varname == null && fname != null) {
                            varname = new StringBuilder(fname + token);
                            fname = null;
                        } else if (varname != null && fname == null) {
                            varname.append(token);
                        } else {
                            String msg = "parsing.error.message.found-comma";
                            throw new ParseException(
                                    Translator.localize(msg),
                                    st.getTokenIndex());
                        }
                    }
                } else if ("=".equals(token) || ":=".equals(token)) {
                    if (currentseq == null) {
                        if (varname == null) {
                            varname = new StringBuilder(fname);
                            fname = "";
                        } else {
                            fname = "";
                        }
                    }
                } else if (currentseq == null) {
                    if (paramExpr == null && token.charAt(0) == '(') {
                        if (token.charAt(token.length() - 1) != ')') {
                            String msg =
                                "parsing.error.message.malformed-parameters";
                            throw new ParseException(Translator.localize(msg),
                                    st.getTokenIndex());
                        }
                        if (fname == null || "".equals(fname)) {
                            String msg =
                                "parsing.error.message.function-not-found";
                            throw new ParseException(Translator.localize(msg),
                                    st.getTokenIndex());
                        }
                        if (varname == null) {
                            varname = new StringBuilder();
                        }
                        paramExpr = token.substring(1, token.length() - 1);
                    } else if (varname != null && fname == null) {
                        varname.append(token);
                    } else if (fname == null || fname.length() == 0) {
                        fname = token;
                    } else {
                        String msg = "parsing.error.message.unexpected-token";
                        Object[] parseExcArgs = {token};
                        throw new ParseException(
                                Translator.localize(msg, parseExcArgs),
                                st.getTokenIndex());
                    }
                } else {
                    boolean hasVal =
                        currentseq.get(currentseq.size() - 2) != null;
                    boolean hasOrd =
                        currentseq.get(currentseq.size() - 1) != null;
                    boolean assigned = false;
                    int bp = findMsgOrderBreak(token);

                    if (!hasVal && !assigned && bp == token.length()) {
                        try {
                            currentseq.set(
                                currentseq.size() - 2, Integer.valueOf(
                                    token));
                            assigned = true;
                        } catch (NumberFormatException nfe) { }
                    }

                    if (!hasOrd && !assigned && bp == 0) {
                        try {
                            currentseq.set(
                                currentseq.size() - 1, Integer.valueOf(
                                    parseMsgOrder(token)));
                            assigned = true;
                        } catch (NumberFormatException nfe) { }
                    }

                    if (!hasVal && !hasOrd && !assigned && bp > 0
                            && bp < token.length()) {
                        Integer nbr, ord;
                        try {
                            nbr = Integer.valueOf(token.substring(0, bp));
                            ord = Integer.valueOf(
                                    parseMsgOrder(token.substring(bp)));
                            currentseq.set(currentseq.size() - 2, nbr);
                            currentseq.set(currentseq.size() - 1, ord);
                            assigned = true;
                        } catch (NumberFormatException nfe) { }
                    }

                    if (!assigned) {
                        String msg = "parsing.error.message.unexpected-token";
                        Object[] parseExcArgs = {token};
                        throw new ParseException(
                                Translator.localize(msg, parseExcArgs),
                                st.getTokenIndex());
                    }
                }
            }
        } catch (NoSuchElementException nsee) {
            String msg = "parsing.error.message.unexpected-end-message";
            throw new ParseException(Translator.localize(msg), s.length());
        } catch (ParseException pre) {
            throw pre;
        }

        if (paramExpr != null) {
            MyTokenizer st = new MyTokenizer(paramExpr, "\\,",
                    parameterCustomSep);
            args = new ArrayList<String>();
            while (st.hasMoreTokens()) {
                token = st.nextToken();

                if (",".equals(token)) {
                    if (args.size() == 0) {
                        args.add(null);
                    }
                    args.add(null);
                } else {
                    if (args.size() == 0) {
                        if (token.trim().length() == 0) {
                            continue;
                        }
                        args.add(null);
                    }
                    String arg = args.get(args.size() - 1);
                    if (arg != null) {
                        arg = arg + token;
                    } else {
                        arg = token;
                    }
                    args.set(args.size() - 1, arg);
                }
            }
        } else if (mayDeleteExpr) {
            args = new ArrayList<String>();
        }

        if (LOG.isDebugEnabled()) {
            StringBuffer buf = new StringBuffer();
            buf.append("ParseMessage: " + s + "\n");
            buf.append("Message: ");
            for (i = 0; seqno != null && i + 1 < seqno.size(); i += 2) {
                if (i > 0) {
                    buf.append(", ");
                }
                buf.append(seqno.get(i) + " (" + seqno.get(i + 1) + ")");
            }
            buf.append("\n");
            buf.append("predecessors: " + predecessors.size() + "\n");
            for (i = 0; i < predecessors.size(); i++) {
                int j;
                List v = predecessors.get(i);
                buf.append("    Predecessor: ");
                for (j = 0; v != null && j + 1 < v.size(); j += 2) {
                    if (j > 0) {
                        buf.append(", ");
                    }
                    buf.append(v.get(j) + " (" + v.get(j + 1) + ")");
                }
            }
            buf.append("guard: " + guard + " it: " + iterative + " pl: "
                    + parallell + "\n");
            buf.append(varname + " := " + fname + " ( " + paramExpr + " )"
                    + "\n");
            LOG.debug(buf);
        }

        if (Model.getFacade().getAction(mes) == null) {
            Object a = Model.getCommonBehaviorFactory()
                .createCallAction();
            Model.getCoreHelper().addOwnedElement(Model.getFacade().getContext(
                    Model.getFacade().getInteraction(mes)), a);
            Model.getCollaborationsHelper().setAction(mes, a);
        }

        if (guard != null) {
            guard = new StringBuilder("[" + guard.toString().trim() + "]");
            if (iterative) {
                if (parallell) {
                    guard = guard.insert(0, "*//");
                } else {
                    guard = guard.insert(0, "*");
                }
            }
            Object expr =
                Model.getDataTypesFactory().createIterationExpression(
                        getExpressionLanguage(), guard.toString());
            Model.getCommonBehaviorHelper().setRecurrence(
                    Model.getFacade().getAction(mes), expr);
        }

        if (fname == null) {
            if (!mayDeleteExpr
                    && Model.getFacade().getScript(
                            Model.getFacade().getAction(mes))
                            != null) {
                String body =
                    (String) Model.getFacade().getBody(
                            Model.getFacade().getScript(
                                    Model.getFacade().getAction(mes)));

                int idx = body.indexOf(":=");
                if (idx >= 0) {
                    idx++;
                } else {
                    idx = body.indexOf("=");
                }

                if (idx >= 0) {
                    fname = body.substring(idx + 1);
                } else {
                    fname = body;
                }
            } else {
                fname = "";
            }
        }

        if (varname == null) {
            if (!mayDeleteExpr
                    && Model.getFacade().getScript(
                            Model.getFacade().getAction(mes))
                            != null) {
                String body =
                    (String) Model.getFacade().getBody(
                            Model.getFacade().getScript(
                                    Model.getFacade().getAction(mes)));
                int idx = body.indexOf(":=");
                if (idx < 0) {
                    idx = body.indexOf("=");
                }

                if (idx >= 0) {
                    varname = new StringBuilder(body.substring(0, idx));
                } else {
                    varname = new StringBuilder();
                }
            } else {
                varname = new StringBuilder();
            }
        }

        if (fname != null) {
            String expr = fname.trim();
            if (varname.length() > 0) {
                expr = varname.toString().trim() + " := " + expr;
            }

            if (Model.getFacade().getScript(
                    Model.getFacade().getAction(mes)) == null
                    || !expr.equals(Model.getFacade().getBody(
                            Model.getFacade().getScript(
                                    Model.getFacade().getAction(mes))))) {
                Object e =
                    Model.getDataTypesFactory()
                        .createActionExpression(
                            getExpressionLanguage(),
                            expr.trim());
                Model.getCommonBehaviorHelper().setScript(
                        Model.getFacade().getAction(mes), e);
                refindOperation = true;
            }
        }

        if (args != null) {
            Collection c = new ArrayList(
                    Model.getFacade().getActualArguments(
                            Model.getFacade().getAction(mes)));
            Iterator it = c.iterator();
            for (i = 0; i < args.size(); i++) {
                Object arg = (it.hasNext() ? /* (MArgument) */it.next() : null);
                if (arg == null) {
                    arg = Model.getCommonBehaviorFactory()
                        .createArgument();
                    Model.getCommonBehaviorHelper().addActualArgument(
                            Model.getFacade().getAction(mes), arg);
                    refindOperation = true;
                }
                if (Model.getFacade().getValue(arg) == null
                        || !args.get(i).equals(
                                Model.getFacade().getBody(
                                        Model.getFacade().getValue(arg)))) {
                    String value = (args.get(i) != null ? args.get(i)
                            : "");
                    Object e =
                        Model.getDataTypesFactory().createExpression(
                                getExpressionLanguage(),
                                value.trim());
                    Model.getCommonBehaviorHelper().setValue(arg, e);
                }
            }

            while (it.hasNext()) {
                Model.getCommonBehaviorHelper()
                    .removeActualArgument(Model.getFacade().getAction(mes),
                        it.next());
                refindOperation = true;
            }
        }

        if (seqno != null) {
            Object/* MMessage */root;
            // Find the preceding message, if any, on either end of the
            // association.
            StringBuilder pname = new StringBuilder();
            StringBuilder mname = new StringBuilder();
            String gname = generateMessageNumber(mes);
            boolean swapRoles = false;
            int majval = 0;
            if (seqno.get(seqno.size() - 2) != null) {
                majval =
                    Math.max((seqno.get(seqno.size() - 2)).intValue()
                            - 1,
                            0);
            }
            int minval = 0;
            if (seqno.get(seqno.size() - 1) != null) {
                minval =
                    Math.max((seqno.get(seqno.size() - 1)).intValue(),
                            0);
            }

            for (i = 0; i + 1 < seqno.size(); i += 2) {
                int bv = 1;
                if (seqno.get(i) != null) {
                    bv = Math.max((seqno.get(i)).intValue(), 1);
                }

                int sv = 0;
                if (seqno.get(i + 1) != null) {
                    sv = Math.max((seqno.get(i + 1)).intValue(), 0);
                }

                if (i > 0) {
                    mname.append(".");
                }
                mname.append(Integer.toString(bv) + (char) ('a' + sv));

                if (i + 3 < seqno.size()) {
                    if (i > 0) {
                        pname.append(".");
                    }
                    pname.append(Integer.toString(bv) + (char) ('a' + sv));
                }
            }

            root = null;
            if (pname.length() > 0) {
                root = findMsg(Model.getFacade().getSender(mes), 
                        pname.toString());
                if (root == null) {
                    root = findMsg(Model.getFacade().getReceiver(mes), 
                            pname.toString());
                    if (root != null) {
                        swapRoles = true;
                    }
                }
            } else if (!hasMsgWithActivator(Model.getFacade().getSender(mes),
                    null)
                    && hasMsgWithActivator(Model.getFacade().getReceiver(mes),
                            null)) {
                swapRoles = true;
            }

            if (compareMsgNumbers(mname.toString(), gname.toString())) {
                // Do nothing
            } else if (isMsgNumberStartOf(gname.toString(), mname.toString())) {
                String msg = "parsing.error.message.subtree-rooted-self";
                throw new ParseException(Translator.localize(msg), 0);
            } else if (Model.getFacade().getPredecessors(mes).size() > 1
                    && Model.getFacade().getSuccessors(mes).size() > 1) {
                String msg = "parsing.error.message.start-end-many-threads";
                throw new ParseException(Translator.localize(msg), 0);
            } else if (root == null && pname.length() > 0) {
                String msg = "parsing.error.message.activator-not-found";
                throw new ParseException(Translator.localize(msg), 0);
            } else if (swapRoles
                    && Model.getFacade().getActivatedMessages(mes).size() > 0
                    && (Model.getFacade().getSender(mes)
                            != Model.getFacade().getReceiver(mes))) {
                String msg = "parsing.error.message.reverse-direction-message";
                throw new ParseException(Translator.localize(msg), 0);
            } else {
                /* Disconnect the message from the call graph
                 * Make copies of returned live collections
                 * since we're modifying
                 */
                Collection c = new ArrayList(
                        Model.getFacade().getPredecessors(mes));
                Collection c2 = new ArrayList(
                        Model.getFacade().getSuccessors(mes));
                Iterator it;

                it = c2.iterator();
                while (it.hasNext()) {
                    Model.getCollaborationsHelper().removeSuccessor(mes,
                            it.next());
                }

                it = c.iterator();
                while (it.hasNext()) {
                    Iterator it2 = c2.iterator();
                    Object pre = /* (MMessage) */it.next();
                    Model.getCollaborationsHelper().removePredecessor(mes, pre);
                    while (it2.hasNext()) {
                        Model.getCollaborationsHelper().addPredecessor(
                                it2.next(), pre);
                    }
                }

                // Connect the message at a new spot
                Model.getCollaborationsHelper().setActivator(mes, root);
                if (swapRoles) {
                    Object/* MClassifierRole */r =
                        Model.getFacade().getSender(mes);
                    Model.getCollaborationsHelper().setSender(mes,
                            Model.getFacade().getReceiver(mes));
                    Model.getCommonBehaviorHelper().setReceiver(mes, r);
                }

                if (root == null) {
                    c =
                        filterWithActivator(
                                Model.getFacade().getSentMessages(
                                        Model.getFacade().getSender(mes)),
                                        null);
                } else {
                    c = Model.getFacade().getActivatedMessages(root);
                }
                c2 = findCandidateRoots(c, root, mes);
                it = c2.iterator();
                // If c2 is empty, then we're done (or there is a
                // cycle in the message graph, which would be bad) If
                // c2 has more than one element, then the model is
                // crappy, but we'll just use one of them anyway
                if (majval <= 0) {
                    while (it.hasNext()) {
                        Model.getCollaborationsHelper().addSuccessor(mes,
                                /* (MMessage) */it.next());
                    }
                } else if (it.hasNext()) {
                    Object/* MMessage */pre =
                        walk(/* (MMessage) */it.next(), majval - 1, false);
                    Object/* MMessage */post = successor(pre, minval);
                    if (post != null) {
                        Model.getCollaborationsHelper()
                            .removePredecessor(post, pre);
                        Model.getCollaborationsHelper()
                            .addPredecessor(post, mes);
                    }
                    insertSuccessor(pre, mes, minval);
                }
                refindOperation = true;
            }
        }

        if (fname != null && refindOperation) {
            Object role = Model.getFacade().getReceiver(mes);
            List ops =
                getOperation(
                        Model.getFacade().getBases(role),
                        fname.trim(),
                        Model.getFacade().getActualArguments(
                                Model.getFacade().getAction(mes)).size());

            // TODO: Should someone choose one, if there are more
            // than one?
            if (Model.getFacade().isACallAction(
                    Model.getFacade().getAction(mes))) {
                Object a = /* (MCallAction) */Model.getFacade().getAction(mes);
                if (ops.size() > 0) {
                    Model.getCommonBehaviorHelper().setOperation(a,
                            /* (MOperation) */ops.get(0));
                } else {
                    Model.getCommonBehaviorHelper().setOperation(a, null);
                }
            }
        }

        // TODO: Predecessors is not implemented, because it
        // causes some problems that I've not found an easy way to handle yet,
        // d00mst. The specific problem is that the notation currently is
        // ambiguous on second message after a thread split.

        // Why not implement it anyway? d00mst

        if (hasPredecessors) {
            Collection roots =
                findCandidateRoots(
                        Model.getFacade().getMessages(
                                Model.getFacade().getInteraction(mes)),
                                null,
                                null);
            List<Object> pre = new ArrayList<Object>();
            Iterator it;
        predfor:
            for (i = 0; i < predecessors.size(); i++) {
                it = roots.iterator();
                while (it.hasNext()) {
                    Object msg =
                        walkTree(it.next(), predecessors.get(i));
                    if (msg != null && msg != mes) {
                        if (isBadPreMsg(mes, msg)) {
                            String parseMsg = "parsing.error.message.one-pred";
                            throw new ParseException(
                                    Translator.localize(parseMsg), 0);
                        }
                        pre.add(msg);
                        continue predfor;
                    }
                }
                String parseMsg = "parsing.error.message.pred-not-found";
                throw new ParseException(Translator.localize(parseMsg), 0);
            }
            MsgPtr ptr = new MsgPtr();
            recCountPredecessors(mes, ptr);
            if (ptr.message != null && !pre.contains(ptr.message)) {
                pre.add(ptr.message);
            }
            Model.getCollaborationsHelper().setPredecessors(mes, pre);
        }
    }

    private String getExpressionLanguage() {
        return Notation.DEFAULT_NOTATION;
    }

    /**
     * Walks a call tree from a root node following the directions given in path
     * to a destination node. If the destination node cannot be reached, then
     * null is returned.
     *
     * @param root The root of the call tree.
     * @param path The path to walk in the call tree.
     * @return The message at the end of path, or <code>null</code>.
     */
    private Object walkTree(Object root, List path) {
        int i;
        for (i = 0; i + 1 < path.size(); i += 2) {
            int bv = 0;
            if (path.get(i) != null) {
                bv = Math.max(((Integer) path.get(i)).intValue() - 1, 0);
            }
    
            int sv = 0;
            if (path.get(i + 1) != null) {
                sv = Math.max(((Integer) path.get(i + 1)).intValue(), 0);
            }
    
            root = walk(root, bv - 1, true);
            if (root == null) {
                return null;
            }
            if (bv > 0) {
                root = successor(root, sv);
                if (root == null) {
                    return null;
                }
            }
            if (i + 3 < path.size()) {
                Iterator it =
                    findCandidateRoots(
                            Model.getFacade().getActivatedMessages(root),
                            root,
                            null).iterator();
                // Things are strange if there are more than one candidate root,
                // it has no obvious interpretation in terms of a call tree.
                if (!it.hasNext()) {
                    return null;
                }
                root = /* (MMessage) */it.next();
            }
        }
        return root;
    }

    /**
     * Finds the steps'th successor of r in the sense that it is a successor of
     * a successor of r (steps times). The first successor with the same
     * activator as r is used in each step. If there are not enough successors,
     * then struct determines the result. If struct is true, then null is
     * returned, otherwise the last successor found.
     */
    private Object walk(Object/* MMessage */r, int steps, boolean strict) {
        Object/* MMessage */act = Model.getFacade().getActivator(r);
        while (steps > 0) {
            Iterator it = Model.getFacade().getSuccessors(r).iterator();
            do {
                if (!it.hasNext()) {
                    return (strict ? null : r);
                }
                r = /* (MMessage) */it.next();
            } while (Model.getFacade().getActivator(r) != act);
            steps--;
        }
        return r;
    }

    /**
     * Finds the root candidates in a collection c, ie the messages in c that
     * has the activator a (may be null) and has no predecessor with the same
     * activator. If veto isn't null, then the message in veto will not be
     * included in the Collection of candidates.
     *
     * @param c The collection of UML Message objects.
     * @param a The message.
     * @param veto The excluded message.
     * @return The found roots.
     */
    private Collection findCandidateRoots(Collection c, Object a, Object veto) {
        List<Object> candidates = new ArrayList<Object>();
        for (Object message : c) {
            if (message == veto) {
                continue;
            }
            if (Model.getFacade().getActivator(message) != a) {
                continue;
            }
            Collection predecessors =
                Model.getFacade().getPredecessors(message);
            boolean isCandidate = true;
            for (Object predecessor : predecessors) {
                if (Model.getFacade().getActivator(predecessor) == a) {
                    isCandidate = false;
                }
            }
            if (isCandidate) {
                candidates.add(message);
            }
        }
        return candidates;
    }

    /**
     * Finds the steps'th successor of message r in the sense that it is a
     * direct successor of r. Returns null if r has fewer successors.
     */
    private Object successor(Object/* MMessage */r, int steps) {
        Iterator it = Model.getFacade().getSuccessors(r).iterator();
        while (it.hasNext() && steps > 0) {
            it.next();
            steps--;
        }
        if (it.hasNext()) {
            return /* (MMessage) */it.next();
        }
        return null;
    }

    /**
     * Compares two message numbers n1, n2 with each other to determine if n1
     * specifies a the same position as n2 in a call tree or n1 specifies a
     * position that is a father of the position specified by n2.
     */
    private boolean isMsgNumberStartOf(String n1, String n2) {
        int i, j, len, jlen;
        len = n1.length();
        jlen = n2.length();
        i = 0;
        j = 0;
        for (; i < len;) {
            int ibv, isv;
            int jbv, jsv;
    
            ibv = 0;
            for (; i < len; i++) {
                char c = n1.charAt(i);
                if (c < '0' || c > '9') {
                    break;
                }
                ibv *= 10;
                ibv += c - '0';
            }
            isv = 0;
            for (; i < len; i++) {
                char c = n1.charAt(i);
                if (c < 'a' || c > 'z') {
                    break;
                }
                isv *= 26;
                isv += c - 'a';
            }
    
            jbv = 0;
            for (; j < jlen; j++) {
                char c = n2.charAt(j);
                if (c < '0' || c > '9') {
                    break;
                }
                jbv *= 10;
                jbv += c - '0';
            }
            jsv = 0;
            for (; j < jlen; j++) {
                char c = n2.charAt(j);
                if (c < 'a' || c > 'z') {
                    break;
                }
                jsv *= 26;
                jsv += c - 'a';
            }
    
            if (ibv != jbv || isv != jsv) {
                return false;
            }
    
            if (i < len && n1.charAt(i) != '.') {
                return false;
            }
            i++;
    
            if (j < jlen && n2.charAt(j) != '.') {
                return false;
            }
            j++;
        }
        return true;
    }

    /**
     * Compares two message numbers with each other to see if they are equal, in
     * the sense that they refer to the same position in a call tree.
     *
     * @param n1 The first number.
     * @param n2 The second number.
     * @return <code>true</code> if they are the same.
     */
    private boolean compareMsgNumbers(String n1, String n2) {
        return isMsgNumberStartOf(n1, n2) && isMsgNumberStartOf(n2, n1);
    }

    /**
     * Parses a message order specification.
     */
    private static int parseMsgOrder(String s) {
        int i, t;
        int v = 0;
    
        t = s.length();
        for (i = 0; i < t; i++) {
            char c = s.charAt(i);
            if (c < 'a' || c > 'z') {
                throw new NumberFormatException();
            }
            v *= 26;
            v += c - 'a';
        }
    
        return v;
    }

    /**
     * Finds the message in ClassifierRole r that has the message number written
     * in n. If it isn't found, null is returned.
     */
    private Object findMsg(Object/* MClassifierRole */r, String n) {
        Collection c = Model.getFacade().getReceivedMessages(r);
        Iterator it = c.iterator();
        while (it.hasNext()) {
            Object msg = /* (MMessage) */it.next();
            String gname = generateMessageNumber(msg);
            if (compareMsgNumbers(gname, n)) {
                return msg;
            }
        }
        return null;
    }

    /**
     * Examines a collection to see if any message has the given message as an
     * activator.
     *
     * @param r
     *            MClassifierRole
     * @param m
     *            MMessage
     */
    private boolean hasMsgWithActivator(Object r, Object m) {
        Iterator it = Model.getFacade().getSentMessages(r).iterator();
        while (it.hasNext()) {
            if (Model.getFacade().getActivator(it.next()) == m) {
                return true;
            }
        }
        return false;
    }

    /**
     * Examines the call tree from chld to see if ans is an ancestor.
     *
     * @param ans
     *            MMessage
     * @param chld
     *            MMessage
     */
    private boolean isBadPreMsg(Object ans, Object chld) {
        while (chld != null) {
            if (ans == chld) {
                return true;
            }
            if (isPredecessorMsg(ans, chld, 100)) {
                return true;
            }
            chld = Model.getFacade().getActivator(chld);
        }
        return false;
    }

    /**
     * Examines the call tree from suc to see if pre is a predecessor. This
     * function is recursive and md specifies the maximum level of recursions
     * allowed.
     *
     * @param pre
     *            MMessage
     * @param suc
     *            MMessage
     */
    private boolean isPredecessorMsg(Object pre, Object suc, int md) {
        Iterator it = Model.getFacade().getPredecessors(suc).iterator();
        while (it.hasNext()) {
            Object m = /* (MMessage) */it.next();
            if (m == pre) {
                return true;
            }
            if (md > 0 && isPredecessorMsg(pre, m, md - 1)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds the messages in Collection c that has message a as activator.
     */
    private Collection<Object> filterWithActivator(Collection c, 
            Object/*MMessage*/a) {
        List<Object> v = new ArrayList<Object>();
        for (Object msg : c) {
            if (Model.getFacade().getActivator(msg) == a) {
                v.add(msg);
            }            
        }
        return v;
    }

    /**
     * Inserts message s as the p'th successor of message m.
     *
     * @param m
     *            MMessage
     * @param s
     *            MMessage
     */
    private void insertSuccessor(Object m, Object s, int p) {
        List<Object> successors = 
            new ArrayList<Object>(Model.getFacade().getSuccessors(m));
        if (successors.size() > p) {
            successors.add(p, s);
        } else {
            successors.add(s);
        }
        Model.getCollaborationsHelper().setSuccessors(m, successors);
    }

    /**
     * Finds the operations in Collection c with the given name 
     * and the given number of parameters. 
     * If no operation is found, one is created. The applicable
     * operations are returned.
     *
     * @param c the collection of operations to be searched
     * @param name the name of the operation to be found
     * @param params the number of parameters of the operation to be found
     * @return a list of the sought operations
     */
    private List getOperation(Collection c, String name, int params) {
        List<Object> ops = new ArrayList<Object>();
        Iterator it;
    
        if (name == null || name.length() == 0) {
            return ops;
        }
    
        it = c.iterator();
        while (it.hasNext()) {
            Object clf = /* (MClassifier) */it.next();
            Collection oe = Model.getFacade().getFeatures(clf);
            Iterator it2 = oe.iterator();
            while (it2.hasNext()) {
                Object me = /* (MModelElement) */it2.next();
                if (!(Model.getFacade().isAOperation(me))) {
                    continue;
                }
    
                Object op = /* (MOperation) */me;
                if (!name.equals(Model.getFacade().getName(op))) {
                    continue;
                }
                if (params != countParameters(op)) {
                    continue;
                }
                ops.add(op);
            }
        }
        if (ops.size() > 0) {
            return ops;
        }
    
        it = c.iterator();
        if (it.hasNext()) {
            StringBuilder expr = new StringBuilder(name + "(");
            int i;
            for (i = 0; i < params; i++) {
                if (i > 0) {
                    expr.append(", ");
                }
                expr.append("param" + (i + 1));
            }
            expr.append(")");
            // Jaap Branderhorst 2002-23-09 added next lines to link
            // parameters and operations to the figs that represent
            // them
            Object cls = it.next();
            Object returnType =
                ProjectManager.getManager()
                        .getCurrentProject().getDefaultReturnType();
            Object op = Model.getCoreFactory().buildOperation(cls, returnType);
    
            try {
                (new OperationNotationUml(op)).parseOperation(
                        expr.toString(), op);
            } catch (ParseException pe) {
                LOG.error("Unexpected ParseException in getOperation: " + pe,
                        pe);
    
            }
            ops.add(op);
        }
        return ops;
    }

    /**
     * Counts the number of parameters that are not return values.
     */
    private int countParameters(Object bf) {
        int count = 0;
        for (Object parameter : Model.getFacade().getParameters(bf)) {
            if (!Model.getFacade().isReturn(parameter)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Recursively count the number of predecessors of the given Message, 
     * and return (a pointer to) the first Message in the chain.
     * 
     * @param message the UML Message to count the predecessors for
     * @param ptr
     * @return
     */
    protected int recCountPredecessors(Object message, MsgPtr ptr) {
        int pre = 0;
        int local = 0;
        Object/*MMessage*/ maxmsg = null;
        Object activator;

        if (message == null) {
            ptr.message = null;
            return 0;
        }

        activator = Model.getFacade().getActivator(message);
        for (Object predecessor : Model.getFacade().getPredecessors(message)) {
            if (Model.getFacade().getActivator(predecessor) 
                    != activator) {
                continue;
            }
            int p = recCountPredecessors(predecessor, null) + 1;
            if (p > pre) {
                pre = p;
                maxmsg = predecessor;
            }
            local++;
        }

        if (ptr != null) {
            ptr.message = maxmsg;
        }

        return Math.max(pre, local);
    }
    
}