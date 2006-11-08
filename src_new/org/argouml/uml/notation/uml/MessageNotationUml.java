// $Id$
// Copyright (c) 2006 The Regents of the University of California. All
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

package org.argouml.uml.notation.uml;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.kernel.ProjectSettings;
import org.argouml.model.Model;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.notation.MessageNotation;
import org.argouml.util.MyTokenizer;

/**
 * The UML notation for a message, as shown on a collaboration diagram. <p>
 *
 * The parse method parses a message line of the form: <p>
 *
 * <pre>
 * intno := integer|name
 * seq := intno ['.' intno]*
 * recurrance := '*'['//'] | '*'['//']'[' <code>iteration </code>']' | '['
 * <code>condition </code>']'
 * seqelem := {[intno] ['['recurrance']']}
 * seq2 := seqelem ['.' seqelem]*
 * ret_list := lvalue [',' lvalue]*
 * arg_list := rvalue [',' rvalue]*
 * message := [seq [',' seq]* '/'] seq2 ':' [ret_list :=] name ([arg_list])
 * </pre> <p>
 *
 * Which is rather complex, so a few examples:<p><ul>
 * <li> 2: display(x, y)
 * <li> 1.3.1: p := find(specs)
 * <li> [x &lt; 0] 4: invert(color)
 * <li> A3, B4/ C3.1*: update()
 * </ul><p>
 *
 * This syntax is compatible with the UML 1.3 specification.<p>
 *
 * Actually, only a subset of this syntax is currently supported, and some
 * is not even planned to be supported. The exceptions are intno, which
 * allows a number possibly followed by a sequence of letters in the range
 * 'a' - 'z', seqelem, which does not allow a recurrance, and message, which
 * does allow one recurrance near seq2. (formerly: name: action )
 *
 * @author michiel
 */
public class MessageNotationUml extends MessageNotation {

    /**
     * The standard error etc. logger
     */
    private static final Logger LOG =
        Logger.getLogger(MessageNotationUml.class);

    /**
     * The vector of CustomSeparators to use when tokenizing parameters.
     */
    private Vector parameterCustomSep;

    /**
     * The constructor.
     *
     * @param message the UML object
     */
    public MessageNotationUml(Object message) {
        super(message);
        parameterCustomSep = new Vector();
        parameterCustomSep.add(MyTokenizer.SINGLE_QUOTED_SEPARATOR);
        parameterCustomSep.add(MyTokenizer.DOUBLE_QUOTED_SEPARATOR);
        parameterCustomSep.add(MyTokenizer.PAREN_EXPR_STRING_SEPARATOR);
    }

    /*
     * @see org.argouml.uml.notation.NotationProvider#parse(java.lang.Object, java.lang.String)
     */
    public void parse(Object modelElement, String text) {
        try {
            parseMessage(modelElement, text);
        } catch (ParseException pe) {
            String msg = "statusmsg.bar.error.parsing.message";
            Object[] args = {pe.getLocalizedMessage(),
                new Integer(pe.getErrorOffset()), };
            ProjectBrowser.getInstance().getStatusBar().showStatus(
                    Translator.messageFormat(msg, args));
        }
    }

    /*
     * @see org.argouml.uml.notation.NotationProvider#getParsingHelp()
     */
    public String getParsingHelp() {
        return "parsing.help.fig-message";
    }

    /*
     * Generates a textual description for a MMessage m.
     *
     * @see org.argouml.uml.notation.NotationProvider#toString(java.lang.Object, java.util.HashMap)
     */
    public String toString(Object modelElement, HashMap args) {
        Iterator it;
        Collection pre;
        Object act;
        Object/*MMessage*/ rt;
        MsgPtr ptr;

        String action = "";
        String number;
        String predecessors = "";
        int lpn;

        if (modelElement == null) {
            return "";
        }

        ptr = new MsgPtr();
        lpn = recCountPredecessors(modelElement, ptr) + 1;
        rt = Model.getFacade().getActivator(modelElement);

        pre = Model.getFacade().getPredecessors(modelElement);
        it = (pre != null) ? pre.iterator() : null;
        if (it != null && it.hasNext()) {
            MsgPtr ptr2 = new MsgPtr();
            int precnt = 0;

            while (it.hasNext()) {
                Object msg = /*(MMessage)*/ it.next();
                int mpn = recCountPredecessors(msg, ptr2) + 1;

                if (mpn == lpn - 1
                    && rt == Model.getFacade().getActivator(msg)
                    && Model.getFacade().getPredecessors(msg).size() < 2
                    && (ptr2.message == null
                        || countSuccessors(ptr2.message) < 2)) {
                    continue;
                }

                if (predecessors.length() > 0) {
                    predecessors += ", ";
                }
                predecessors += generateMessageNumber(msg, ptr2.message, mpn);
                precnt++;
            }

            if (precnt > 0) {
                predecessors += " / ";
            }
        }

        number = generateMessageNumber(modelElement, ptr.message, lpn);

        act = Model.getFacade().getAction(modelElement);
        if (act != null) {
            if (Model.getFacade().getRecurrence(act) != null) {
                number =
                    generateRecurrence(Model.getFacade().getRecurrence(act))
                    + " "
                    + number;
            }

            action = generateAction(act);
            /* Dirty fix for issue 1758 (Needs to be amended
             * when we start supporting parameters):
             */
            if (!action.endsWith(")")) {
        	action = action + "()";
            }
        }

        return predecessors + number + " : " + action;
    }

    private String generateMessageNumber(
            Object/*MMessage*/ m,
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

    static class MsgPtr {
        /**
         * The message pointed to.
         */
        public Object/*MMessage*/ message;
    }

    /**
     * Generates the textual number of MMessage m. The number is a string
     * of numbers separated by points which describes the message's order
     * and level in a collaboration.<p>
     *
     * If you plan to modify this number, make sure that
     * ParserDisplay.parseMessage is adapted to the change.
     *
     * @param m A Message to generate the number for.
     * @return A String with the message number of m.
     */
    private String generateMessageNumber(Object/*MMessage*/ m) {
        MsgPtr ptr = new MsgPtr();
        int pos = recCountPredecessors(m, ptr) + 1;
        return generateMessageNumber(m, ptr.message, pos);
    }

    private int recCountPredecessors(Object message, MsgPtr ptr) {
        Collection c;
        Iterator it;
        int pre = 0;
        int local = 0;
        Object/*MMessage*/ maxmsg = null;
        Object/*MMessage*/ act;

        if (message == null) {
            ptr.message = null;
            return 0;
        }

        act = Model.getFacade().getActivator(message);
        c = Model.getFacade().getPredecessors(message);
        it = c.iterator();
        while (it.hasNext()) {
            Object msg = it.next();
            if (Model.getFacade().getActivator(msg) != act) {
                continue;
            }
            int p = recCountPredecessors(msg, null) + 1;
            if (p > pre) {
                pre = p;
                maxmsg = msg;
            }
            local++;
        }

        if (ptr != null) {
            ptr.message = maxmsg;
        }

        return Math.max(pre, local);
    }

    private int countSuccessors(Object/*MMessage*/ m) {
        int count = 0;
        Object act = Model.getFacade().getActivator(m);
        Collection coll = Model.getFacade().getSuccessors(m);
        if (coll != null) {
            Iterator it = coll.iterator();
            while (it.hasNext()) {
                Object msg = /*(MMessage)*/ it.next();
                if (Model.getFacade().getActivator(msg) != act) {
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
    private String generateRecurrence(Object expr) {
        if (expr == null) {
            return "";
        }

        return Model.getFacade().getBody(expr).toString();
    }

    private String generateAction(Object m) {
        Collection c;
        Iterator it;
        String s;
        String p;
        boolean first;

        Object script = Model.getFacade().getScript(m);

        if ((script != null) && (Model.getFacade().getBody(script) != null)) {
            s = Model.getFacade().getBody(script).toString();
        } else {
            s = "";
        }

        p = "";
        c = Model.getFacade().getActualArguments(m);
        if (c != null) {
            it = c.iterator();
            first = true;
            while (it.hasNext()) {
                Object arg = /*(MArgument)*/ it.next();
                if (!first) {
                    p += ", ";
                }

                if (Model.getFacade().getValue(arg) != null) {
                    p += generateExpression(Model.getFacade().getValue(arg));
                }
                first = false;
            }
        }
        if (s.length() == 0 && p.length() == 0) {
            return "";
        }

        /* If there are no arguments, then do not show the ().
         * This solves issue 1758.
         * Arguments are not supported anyhow in the UI yet.
         * These brackets are easily confused with the brackets
         * for the Operation of a CallAction.
         */
        if (p.length() == 0) {
            return s;
        }

        return s + " (" + p + ")";
    }

    private String generateExpression(Object expr) {
        if (Model.getFacade().isAExpression(expr)) {
            return generateUninterpreted(
                    (String) Model.getFacade().getBody(expr));
        } else if (Model.getFacade().isAConstraint(expr)) {
            return generateExpression(Model.getFacade().getBody(expr));
        }
        return "";
    }

    /**
     * Make a string non-null.
     *
     * @param un the String or null
     * @return a string, guaranteed to be not null
     */
    private String generateUninterpreted(String un) {
        if (un == null) {
            return "";
        }
        return un;
    }

    /**
     * Parse a Message textual description.<p>
     *
     * TODO: - This method is too complex, lets break it up. <p>
     *
     * Parses a message line of the form:
     *
     * <pre>
     * intno := integer|name
     * seq := intno ['.' intno]*
     * recurrance := '*'['//'] | '*'['//']'[' <i>iteration </i>']' | '['
     * <i>condition </i>']'
     * seqelem := {[intno] ['['recurrance']']}
     * seq2 := seqelem ['.' seqelem]*
     * ret_list := lvalue [',' lvalue]*
     * arg_list := rvalue [',' rvalue]*
     * message := [seq [',' seq]* '/'] seq2 ':' [ret_list :=] name ([arg_list])
     * </pre>
     *
     * Which is rather complex, so a few examples:<ul>
     * <li> 2: display(x, y)
     * <li> 1.3.1: p := find(specs)
     * <li> [x &lt; 0] 4: invert(color)
     * <li> A3, B4/ C3.1*: update()
     * </ul>
     *
     * This syntax is compatible with the UML 1.3 specification.<p>
     *
     * Actually, only a subset of this syntax is currently supported, and some
     * is not even planned to be supported. The exceptions are intno, which
     * allows a number possibly followed by a sequence of letters in the range
     * 'a' - 'z', seqelem, which does not allow a recurrance, and message, which
     * does allow one recurrance near seq2. (formerly: name: action )
     *
     * @param mes the MMessage to apply any changes to
     * @param s   the String to parse
     * @throws ParseException
     *            when it detects an error in the attribute string. See also
     *            ParseError.getErrorOffset().
     */
    protected void parseMessage(Object mes, String s) throws ParseException {
        String fname = null;
        String guard = null;
        String paramExpr = null;
        String token;
        String varname = null;
        Vector predecessors = new Vector();
        Vector seqno = null;
        Vector currentseq = new Vector();
        Vector args = null;
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
                            varname += token;
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

                    guard = "";
                    while (true) {
                        token = st.nextToken();
                        if ("]".equals(token)) {
                            break;
                        }
                        guard += token;
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

                        currentseq = new Vector();
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

                            currentseq = new Vector();
                            currentseq.add(null);
                            currentseq.add(null);
                        }
                        hasPredecessors = true;
                    } else {
                        if (varname == null && fname != null) {
                            varname = fname + token;
                            fname = null;
                        } else if (varname != null && fname == null) {
                            varname += token;
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
                            varname = fname;
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
                            varname = "";
                        }
                        paramExpr = token.substring(1, token.length() - 1);
                    } else if (varname != null && fname == null) {
                        varname += token;
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
                            currentseq.set(currentseq.size() - 2, new Integer(
                                    token));
                            assigned = true;
                        } catch (NumberFormatException nfe) { }
                    }

                    if (!hasOrd && !assigned && bp == 0) {
                        try {
                            currentseq.set(currentseq.size() - 1, new Integer(
                                    parseMsgOrder(token)));
                            assigned = true;
                        } catch (NumberFormatException nfe) { }
                    }

                    if (!hasVal && !hasOrd && !assigned && bp > 0
                            && bp < token.length()) {
                        Integer nbr, ord;
                        try {
                            nbr = new Integer(token.substring(0, bp));
                            ord = new Integer(
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
            args = new Vector();
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
                    String arg = (String) args.get(args.size() - 1);
                    if (arg != null) {
                        arg = arg + token;
                    } else {
                        arg = token;
                    }
                    args.set(args.size() - 1, arg);
                }
            }
        } else if (mayDeleteExpr) {
            args = new Vector();
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
                Vector v = (Vector) predecessors.get(i);
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
            guard = "[" + guard.trim() + "]";
            if (iterative) {
                if (parallell) {
                    guard = "*//" + guard;
                } else {
                    guard = "*" + guard;
                }
            }
            Project project =
                ProjectManager.getManager().getCurrentProject();
            ProjectSettings ps = project.getProjectSettings();
            Object expr =
                Model.getDataTypesFactory().createIterationExpression(
                        ps.getNotationLanguage(), guard);
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
                    varname = body.substring(0, idx);
                } else {
                    varname = "";
                }
            } else {
                varname = "";
            }
        }

        Project project =
            ProjectManager.getManager().getCurrentProject();
        ProjectSettings ps = project.getProjectSettings();

        if (fname != null) {
            String expr = fname.trim();
            if (varname.length() > 0) {
                expr = varname.trim() + " := " + expr;
            }

            if (Model.getFacade().getScript(
                    Model.getFacade().getAction(mes)) == null
                    || !expr.equals(Model.getFacade().getBody(
                            Model.getFacade().getScript(
                                    Model.getFacade().getAction(mes))))) {
                Object e =
                    Model.getDataTypesFactory()
                        .createActionExpression(
                                ps.getNotationLanguage(),
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
                    String value = (args.get(i) != null ? (String) args.get(i)
                            : "");
                    Object e =
                        Model.getDataTypesFactory().createExpression(
                                ps.getNotationLanguage(),
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
            String pname = "";
            String mname = "";
            String gname = generateMessageNumber(mes);
            boolean swapRoles = false;
            int majval = 0;
            if (seqno.get(seqno.size() - 2) != null) {
                majval =
                    Math.max(((Integer) seqno.get(seqno.size() - 2)).intValue()
                             - 1,
                             0);
            }
            int minval = 0;
            if (seqno.get(seqno.size() - 1) != null) {
                minval =
                    Math.max(((Integer) seqno.get(seqno.size() - 1)).intValue(),
                            0);
            }

            for (i = 0; i + 1 < seqno.size(); i += 2) {
                int bv = 1;
                if (seqno.get(i) != null) {
                    bv = Math.max(((Integer) seqno.get(i)).intValue(), 1);
                }

                int sv = 0;
                if (seqno.get(i + 1) != null) {
                    sv = Math.max(((Integer) seqno.get(i + 1)).intValue(), 0);
                }

                if (i > 0) {
                    mname += ".";
                }
                mname += Integer.toString(bv) + (char) ('a' + sv);

                if (i + 3 < seqno.size()) {
                    if (i > 0) {
                        pname += ".";
                    }
                    pname += Integer.toString(bv) + (char) ('a' + sv);
                }
            }

            root = null;
            if (pname.length() > 0) {
                root = findMsg(Model.getFacade().getSender(mes), pname);
                if (root == null) {
                    root = findMsg(Model.getFacade().getReceiver(mes), pname);
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

            if (compareMsgNumbers(mname, gname)) {
                ;// Do nothing
            } else if (isMsgNumberStartOf(gname, mname)) {
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
                    && Model.getFacade().getMessages4(mes).size() > 0
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
                            Model.getFacade().getMessages2(
                                    Model.getFacade().getSender(mes)),
                                    null);
                } else {
                    c = Model.getFacade().getMessages4(root);
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
            Vector ops =
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
            Vector pre = new Vector();
            Iterator it;
        predfor:
            for (i = 0; i < predecessors.size(); i++) {
                it = roots.iterator();
                while (it.hasNext()) {
                    Object msg =
                        walkTree(it.next(), (Vector) predecessors.get(i));
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

    /**
     * Walks a call tree from a root node following the directions given in path
     * to a destination node. If the destination node cannot be reached, then
     * null is returned.
     *
     * @param root The root of the call tree.
     * @param path The path to walk in the call tree.
     * @return The message at the end of path, or <code>null</code>.
     */
    private Object walkTree(Object root, Vector path) {
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
                            Model.getFacade().getMessages4(root),
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
     * @param c The collection.
     * @param a The message.
     * @param veto The veto message.
     * @return The found roots.
     */
    private Collection findCandidateRoots(Collection c, Object a,
            Object veto) {
        Iterator it = c.iterator();
        List v = new ArrayList();
        while (it.hasNext()) {
            Object m = /* (MMessage) */it.next();
            if (m == veto) {
                continue;
            }
            if (Model.getFacade().getActivator(m) != a) {
                continue;
            }
            Iterator it2 = Model.getFacade().getPredecessors(m).iterator();
            boolean candidate = true;
            while (it2.hasNext()) {
                Object m2 = /* (MMessage) */it2.next();
                if (Model.getFacade().getActivator(m2) == a) {
                    candidate = false;
                }
            }
            if (candidate) {
                v.add(m);
            }
        }
        return v;
    }

    /**
     * Finds the steps'th successor of message r in the sense that it is a
     * direct successor of r. Returns null if r has fewer successors.
     */
    private Object/* MMessage */successor(Object/* MMessage */r, int steps) {
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
        Collection c = Model.getFacade().getMessages1(r);
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
        Iterator it = Model.getFacade().getMessages2(r).iterator();
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
    private Collection filterWithActivator(Collection c, Object/*MMessage*/a) {
        Iterator it = c.iterator();
        List v = new ArrayList();
        while (it.hasNext()) {
            Object m = /* (MMessage) */it.next();
            if (Model.getFacade().getActivator(m) == a) {
                v.add(m);
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
        Vector v = new Vector(Model.getFacade().getSuccessors(m));
        if (v.size() > p) {
            v.insertElementAt(s, p);
        } else {
            v.add(s);
        }
        Model.getCollaborationsHelper().setSuccessors(m, v);
    }

    /**
     * Finds the operations in Collection c with name name and params number of
     * parameters. If no operation is found, one is created. The applicable
     * operations are returned.
     *
     * @param c the collection of operations to be searched
     * @param name the name of the operation to be found
     * @param params the number of parameters of the operation to be found
     * @return the sought operation
     */
    private Vector getOperation(Collection c, String name, int params) {
        Vector options = new Vector();
        Iterator it;

        if (name == null || name.length() == 0) {
            return options;
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
                options.add(op);
            }
        }
        if (options.size() > 0) {
            return options;
        }

        it = c.iterator();
        if (it.hasNext()) {
            String expr = name + "(";
            int i;
            for (i = 0; i < params; i++) {
                if (i > 0) {
                    expr += ", ";
                }
                expr += "param" + (i + 1);
            }
            expr += ")";
            // Jaap Branderhorst 2002-23-09 added next lines to link
            // parameters and operations to the figs that represent
            // them
            Object cls = /* (MClassifier) */it.next();
            Object model =
                ProjectManager.getManager()
                        .getCurrentProject().getModel();
            Object voidType =
                ProjectManager.getManager()
                        .getCurrentProject().findType("void");
            Object op =
                Model.getCoreFactory()
                        .buildOperation(cls, model, voidType);

            try {
                (new OperationNotationUml(op)).parseOperation(expr, op);
            } catch (ParseException pe) {
                LOG.error("Unexpected ParseException in getOperation: " + pe,
                        pe);

            }
            options.add(op);
        }
        return options;
    }

    /**
     * Counts the number of parameters that are not return values.
     */
    private int countParameters(Object bf) {
        Collection c = Model.getFacade().getParameters(bf);
        Iterator it = c.iterator();
        int count = 0;

        while (it.hasNext()) {
            Object p = it.next();
            if (Model.getFacade().isReturn(p)) {
                continue;
            }
            count++;
        }

        return count;
    }

}
