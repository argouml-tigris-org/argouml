/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michiel van der Wulp
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2006-2009 The Regents of the University of California. All
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

package org.argouml.notation.providers.uml;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoHelpEvent;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.notation.NotationSettings;
import org.argouml.notation.providers.ClassifierRoleNotation;
import org.argouml.util.MyTokenizer;

/**
 * The UML notation for a ClassifierRole. <p>
 * 
 * The following is supported: <p>
 * 
 * <pre>
 * baselist := [base] [, base]*
 * classifierRole := [name] [/ role] [: baselist]
 * </pre>
 *
 * The <code>role </code> and <code>baselist</code> can be given in
 * any order.<p>
 * 
 * The <code>name</code> is the Instance name, not used for a ClassifierRole.<p>
 *
 * This syntax is compatible with the UML 1.3 and 1.4 specification.
 * 
 * @author Michiel van der Wulp
 */
public class ClassifierRoleNotationUml extends ClassifierRoleNotation {


    /**
     * The Constructor.
     * 
     * @param classifierRole the UML ClassifierRole
     */
    public ClassifierRoleNotationUml(Object classifierRole) {
        super(classifierRole);
    }

    /*
     * @see org.argouml.notation.providers.NotationProvider#getParsingHelp()
     */
    public String getParsingHelp() {
        return "parsing.help.fig-classifierrole";
    }

    /*
     * @see org.argouml.notation.providers.NotationProvider#parse(java.lang.Object, java.lang.String)
     */
    public void parse(Object modelElement, String text) {
        try {
            parseClassifierRole(modelElement, text);
        } catch (ParseException pe) {
            String msg = "statusmsg.bar.error.parsing.classifierrole";
            Object[] args = {pe.getLocalizedMessage(),
                             Integer.valueOf(pe.getErrorOffset()), };
            ArgoEventPump.fireEvent(new ArgoHelpEvent(
                    ArgoEventTypes.HELP_CHANGED, this,
                    Translator.messageFormat(msg, args)));
        }
    }
    
    /**
     * Parses a ClassifierRole represented by the following line of the format:
     *
     * <pre>
     * baselist := [base] [, base]*
     * classifierRole := [name] [/ role] [: baselist]
     * </pre>
     *
     * <code>role </code> and <code>baselist</code> can be given in
     * any order.<p>
     *
     * This syntax is compatible with the UML 1.3 specification.
     *
     * (formerly: "name: base" )
     *
     * @param cls the classifier role to apply any changes to
     * @param s the String to parse
     * @return the classifier role with the applied changes
     * @throws ParseException when it detects an error in the attribute string. 
     *                  See also ParseError.getErrorOffset().
     */
    protected Object parseClassifierRole(Object cls, String s)
        throws ParseException {
        
        String name = null;
        String token;
        String role = null;
        String base = null;
        List<String> bases = null;
        boolean hasColon = false;
        boolean hasSlash = false;

        try {
            MyTokenizer st = new MyTokenizer(s, " ,\t,/,:,\\,");

            while (st.hasMoreTokens()) {
                token = st.nextToken();
                if (" ".equals(token) || "\t".equals(token)) {
                    /* Do nothing. */
                } else if ("/".equals(token)) {
                    hasSlash = true;
                    hasColon = false;

                    if (base != null) {
                        if (bases == null) {
                            bases = new ArrayList<String>();
                        }
                        bases.add(base);
                    }
                    base = null;
                } else if (":".equals(token)) {
                    hasColon = true;
                    hasSlash = false;

                    if (bases == null) {
                        bases = new ArrayList<String>();
                    }
                    if (base != null) {
                        bases.add(base);
                    }
                    base = null;
                } else if (",".equals(token)) {
                    if (base != null) {
                        if (bases == null) {
                            bases = new ArrayList<String>();
                        }
                        bases.add(base);
                    }
                    base = null;
                } else if (hasColon) {
                    if (base != null) {
                    	String msg = "parsing.error.classifier.extra-test";
                        throw new ParseException(
                                Translator.localize(msg), 
                                st.getTokenIndex());
                    }

                    base = token;
                } else if (hasSlash) {
                    if (role != null) {
                    	String msg = "parsing.error.classifier.extra-test";
                        throw new ParseException(
                                Translator.localize(msg), 
                                st.getTokenIndex());
                    }

                    role = token;
                } else {
                    if (name != null) {
                    	String msg = "parsing.error.classifier.extra-test";
                        throw new ParseException(
                                Translator.localize(msg), 
                                st.getTokenIndex());
                    }

                    name = token;
                }
            }
        } catch (NoSuchElementException nsee) {
            String msg = "parsing.error.classifier.unexpected-end-attribute";
            throw new ParseException(Translator.localize(msg), s.length());
        }

        if (base != null) {
            if (bases == null) {
                bases = new ArrayList<String>();
            }
            bases.add(base);
        }

        // TODO: What to do about object name???
        //    if (name != null)
        //      ;

        if (role != null) {
            Model.getCoreHelper().setName(cls, role.trim());
        }

        if (bases != null) {
            // Remove bases that aren't there anymore

            // copy - can't iterate modify live collection while iterating it
            Collection b = new ArrayList(Model.getFacade().getBases(cls));
            Iterator it = b.iterator();
            Object c;
            Object ns = Model.getFacade().getNamespace(cls);
            if (ns != null && Model.getFacade().getNamespace(ns) != null) {
                ns = Model.getFacade().getNamespace(ns);
            } else {
                ns = Model.getFacade().getRoot(cls);
            }

            while (it.hasNext()) {
                c = it.next();
                if (!bases.contains(Model.getFacade().getName(c))) {
                    Model.getCollaborationsHelper().removeBase(cls, c);
                }
            }

            it = bases.iterator();
        addBases:
            while (it.hasNext()) {
                String d = ((String) it.next()).trim();

                Iterator it2 = b.iterator();
                while (it2.hasNext()) {
                    c = it2.next();
                    if (d.equals(Model.getFacade().getName(c))) {
                        continue addBases;
                    }
                }
                c = NotationUtilityUml.getType(d, ns);
                if (Model.getFacade().isACollaboration(
                        Model.getFacade().getNamespace(c))) {
                    Model.getCoreHelper().setNamespace(c, ns);
                }
                Model.getCollaborationsHelper().addBase(cls, c);
            }
        }
        
        return cls;
    }

    private String toString(Object modelElement) {
        String nameString = Model.getFacade().getName(modelElement);
        if (nameString == null) { 
            nameString = "";
        }
        nameString = nameString.trim();
        // Loop through all base classes, building a comma separated list
        StringBuilder baseString = NotationUtilityUml.formatNameList(
                Model.getFacade().getBases(modelElement));
        baseString = new StringBuilder(baseString.toString().trim());       
        // Build the final string
        if (nameString.length() != 0) {
            nameString = "/" + nameString;
        }
        if (baseString.length() != 0) {
            baseString = baseString.insert(0, ":");
        }
        return nameString + baseString.toString();
    }

    @Override
    public String toString(Object modelElement, NotationSettings settings) {
        return toString(modelElement);
    }

}
