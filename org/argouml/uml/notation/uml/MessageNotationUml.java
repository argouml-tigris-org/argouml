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

import org.argouml.i18n.Translator;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.generator.GeneratorDisplay;
import org.argouml.uml.generator.ParserDisplay;
import org.argouml.uml.notation.MessageNotation;

/**
 * The UML notation for a message, as shown on a collaboration diagram. <p>
 * 
 * The parse method parses a message line of the form: <p>
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
     * The constructor.
     * 
     * @param message the UML object
     */
    public MessageNotationUml(Object message) {
        super(message);
    }

    /*
     * @see org.argouml.notation.NotationProvider4#parse(java.lang.String)
     */
    public String parse(String text) {
        try {
            parseMessage(myMessage, text);
        } catch (ParseException pe) {
            String msg = "statusmsg.bar.error.parsing.message";
            Object[] args = {pe.getLocalizedMessage(),
                    new Integer(pe.getErrorOffset()), };
            ProjectBrowser.getInstance().getStatusBar().showStatus(
                    Translator.messageFormat(msg, args));
        }
        return toString();
    }
    
    private Object parseMessage(Object message, String s1)
        throws ParseException {

        String s = s1.trim();

        ParserDisplay.SINGLETON.parseMessage(message, s);

        return message;
    }

    /*
     * @see org.argouml.notation.NotationProvider4#getParsingHelp()
     */
    public String getParsingHelp() {
        return "parsing.help.fig-message";
    }

    /*
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return GeneratorDisplay.getInstance().generateMessage(myMessage);
    }
}
