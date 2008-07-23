// $Id$
// Copyright (c) 2008 The Regents of the University of California. All
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
import java.util.Map;
import java.util.NoSuchElementException;

import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoHelpEvent;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.notation.providers.EnumerationLiteralNotation;
import org.argouml.uml.StereotypeUtility;
import org.argouml.util.MyTokenizer;

/**
 * The notation for an Enumeration Literal. <p>
 * 
 * The supported notation is: <pre>
 *     [ "<<" stereotype ">>" ] name [ ";" name ]*
 * </pre>
 * This means:<ul><li> 
 * The name is not optional (i.e. leaving it blank 
 * means deletion of the literal). <li>
 * Multiple literals may be entered at once 
 * by separating the names with a semicolon. <li> 
 * Extra literals are inserted after the one being parsed. <li>
 * A stereotype may precede the name of any literal. </ul><p>
 * 
 * As explained in issue 5000, the real implemented BNF is something like: 
 * <pre>
 *     ["<<" stereotype ["," stereotype]* ">>"] name [ ";" ["<<" stereotype ["," stereotype]* ">>"] name]*
 * </pre>
 * 
 * @author Michiel
 */
public class EnumerationLiteralNotationUml extends EnumerationLiteralNotation {

    /**
     * The constructor.
     * 
     * @param enumLiteral the UML element
     */
    public EnumerationLiteralNotationUml(Object enumLiteral) {
        super(enumLiteral);
    }

    @Override
    public String getParsingHelp() {
        return "parsing.help.fig-enumeration-literal";
    }

    @Override
    public void parse(Object modelElement, String text) {
        try {
            parseEnumerationLiteralFig(
                    Model.getFacade().getEnumeration(modelElement),
                    modelElement, text);
        } catch (ParseException pe) {
            String msg = "statusmsg.bar.error.parsing.enumeration-literal";
            Object[] args = {
                pe.getLocalizedMessage(),
                Integer.valueOf(pe.getErrorOffset()),
            };
            ArgoEventPump.fireEvent(new ArgoHelpEvent(
                    ArgoEventTypes.HELP_CHANGED, this,
                Translator.messageFormat(msg, args)));
        }
    }
    
    /**
     * Parse a string representing one or more ";" separated 
     * enumeration literals.
     * 
     * @param enumeration the enumeration that the literal belongs to
     * @param literal the literal on which the editing will happen
     * @param text the string to parse
     * @throws ParseException for invalid input - so that the right 
     * message may be shown to the user
     */
    protected void  parseEnumerationLiteralFig(
            Object enumeration, Object literal, String text) 
        throws ParseException {
        
        if (enumeration == null || literal == null) {
            return;
        }
        Project project = ProjectManager.getManager().getCurrentProject();

        ParseException pex = null;
        int start = 0;
        int end = NotationUtilityUml.indexOfNextCheckedSemicolon(text, start);
        
        if (end == -1) {
            /* No text. We may remove the literal. */
            project.moveToTrash(literal);
            return;
        }
        String s = text.substring(start, end).trim();
        if (s.length() == 0) {
            /* No non-white chars in text? remove literal! */
            project.moveToTrash(literal);
            return;
        }
        parseEnumerationLiteral(s, literal);

        int i = Model.getFacade().getEnumerationLiterals(enumeration)
            .indexOf(literal);
        // check for more literals (';' separated):
        start = end + 1;
        end = NotationUtilityUml.indexOfNextCheckedSemicolon(text, start);
        while (end > start && end <= text.length()) {
            s = text.substring(start, end).trim();
            if (s.length() > 0) {
                // yes, there are more:
                Object newLiteral = 
                    Model.getCoreFactory().createEnumerationLiteral();
                if (newLiteral != null) {
                    try {
                        if (i != -1) {
                            Model.getCoreHelper().addLiteral(
                                    enumeration, ++i, newLiteral);
                        } else {
                            Model.getCoreHelper().addLiteral(
                                    enumeration, 0, newLiteral);
                        }
                        parseEnumerationLiteral(s, newLiteral);
                    } catch (ParseException ex) {
                        if (pex == null) {
                            pex = ex;
                        }
                    }
                }
            }
            start = end + 1;
            end = NotationUtilityUml.indexOfNextCheckedSemicolon(text, start);
        }
        if (pex != null) {
            throw pex;
        }
    }

    protected void parseEnumerationLiteral(String text, Object literal) 
        throws ParseException {
        text = text.trim();
        if (text.length() == 0) {
            return;
        }
        // strip any trailing semi-colons
        if (text.charAt(text.length() - 1) == ';') {
            text = text.substring(0, text.length() - 2);
        }
        MyTokenizer st;

        String name = null;
        StringBuilder stereotype = null;
        String token;

        try {
            st = new MyTokenizer(text, "<<,\u00AB,\u00BB,>>");
            while (st.hasMoreTokens()) {
                token = st.nextToken();

                if ("<<".equals(token) || "\u00AB".equals(token)) {
                    if (stereotype != null) {
                        String msg = 
                            "parsing.error.model-element-name.twin-stereotypes";
                        throw new ParseException(Translator.localize(msg),
                                st.getTokenIndex());
                    }

                    stereotype = new StringBuilder();
                    while (true) {
                        token = st.nextToken();
                        if (">>".equals(token) || "\u00BB".equals(token)) {
                            break;
                        }
                        stereotype.append(token);
                    }
                } else {
                    if (name != null) {
                        String msg = 
                            "parsing.error.model-element-name.twin-names";
                        throw new ParseException(Translator.localize(msg), 
                                st.getTokenIndex());
                    }

                    name = token;
                }
            }
        } catch (NoSuchElementException nsee) {
            String msg = 
                "parsing.error.model-element-name.unexpected-name-element";
            throw new ParseException(Translator.localize(msg),
                    text.length());
        } catch (ParseException pre) {
            throw pre;
        }

        if (name != null) {
            name = name.trim();
        }
        if (name != null) {
            Model.getCoreHelper().setName(literal, name);
        }

        StereotypeUtility.dealWithStereotypes(literal, stereotype, false);

        return;
    }

    @Override
    public String toString(Object modelElement, Map args) {
        String nameStr = "";
        /* Heuristic algorithm: do not show stereotypes if there is no name. */
        if (Model.getFacade().getName(modelElement) != null) {
            nameStr = NotationUtilityUml.generateStereotype(modelElement, args);
            if (nameStr.length() > 0) {
                nameStr += " ";
            }
            nameStr += Model.getFacade().getName(modelElement).trim();
        }
        return nameStr;
    }

}
