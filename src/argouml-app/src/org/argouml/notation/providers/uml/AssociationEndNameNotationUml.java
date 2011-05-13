/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2011 Contributors - see below
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
import java.util.NoSuchElementException;

import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoHelpEvent;
import org.argouml.i18n.Translator;
import org.argouml.model.Facade;
import org.argouml.model.Model;
import org.argouml.notation.NotationSettings;
import org.argouml.notation.providers.AssociationEndNameNotation;
import org.argouml.uml.StereotypeUtility;
import org.argouml.util.MyTokenizer;

/**
 * The UML notation for an association-end name (i.e. the  role). <p>
 * 
 * This notation supports the association end name, 
 * visibility, and stereotypes. <p>
 * 
 * There is no support for the interface specifier
 * (that maps to the "specification" of an AssociationEnd). <p>
 * 
 * This is the only notation (that I'm aware of) that requires state.
 * All others could become effectively singletons. - Bob.
 * 
 * @author michiel
 */
public class AssociationEndNameNotationUml extends AssociationEndNameNotation {
	
    /**
     * The constructor.
     * 
     * @param associationEnd the UML element
     */
    public AssociationEndNameNotationUml(Object associationEnd) {
        super(associationEnd);
    }

    /*
     * @see org.argouml.notation.providers.NotationProvider#getParsingHelp()
     */
    public String getParsingHelp() {
        return "parsing.help.fig-association-end-name";
    }

    /*
     * @see org.argouml.notation.providers.NotationProvider#parse(java.lang.Object, java.lang.String)
     */
    public void parse(Object modelElement, String text) {
        try {
            parseAssociationEnd(modelElement, text);
        } catch (ParseException pe) {
            String msg = "statusmsg.bar.error.parsing.association-end-name";
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
     * Parse a line of the form: <pre>
     *          [/] [visibility] name
     * </pre><p>
     * 
     * Stereotypes can be given between any element. It must be given 
     * in the form: 
     * &lt;&lt;stereotype1,stereotype2,stereotype3&gt;&gt;
     * 
     * @param role   The AssociationEnd <em>text</em> describes.
     * @param text A String on the above format.
     * @throws ParseException
     *             when it detects an error in the role string. See also
     *             ParseError.getErrorOffset().
     */
    protected void parseAssociationEnd(Object role, String text)
        throws ParseException {
        MyTokenizer st;

        String name = null;
        StringBuilder stereotype = null;
        String token;
        boolean derived = false;

        text = text.trim();
        /* Handle Derived: */
        if (text.length() > 0 && "/".indexOf(text.charAt(0)) >= 0) {
            derived = true;
            text = text.substring(1);
            text = text.trim();
        }
        
        try {
            st = new MyTokenizer(text, "<<,\u00AB,\u00BB,>>");
            while (st.hasMoreTokens()) {
                token = st.nextToken();

                if ("<<".equals(token) || "\u00AB".equals(token)) {
                    if (stereotype != null) {
                    	String msg = 
                            "parsing.error.association-name.twin-stereotypes";
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
                    		"parsing.error.association-name.twin-names";
                        throw new ParseException(Translator.localize(msg),
                                st.getTokenIndex());
                    }
                    name = token;
                }
            }
        } catch (NoSuchElementException nsee) {
    	    String ms = "parsing.error.association-name.unexpected-end-element";
            throw new ParseException(Translator.localize(ms),
                    text.length());
        } catch (ParseException pre) {
            throw pre;
        }

        dealWithDerived(role, derived);

        if (name != null) {
            name = name.trim();
        }

        if (name != null && name.startsWith("+")) {
            name = name.substring(1).trim();
            Model.getCoreHelper().setVisibility(role,
                            Model.getVisibilityKind().getPublic());
        }
        if (name != null && name.startsWith("-")) {
            name = name.substring(1).trim();
            Model.getCoreHelper().setVisibility(role,
                            Model.getVisibilityKind().getPrivate());
        }
        if (name != null && name.startsWith("#")) {
            name = name.substring(1).trim();
            Model.getCoreHelper().setVisibility(role,
                            Model.getVisibilityKind().getProtected());
        }
        if (name != null && name.startsWith("~")) {
            name = name.substring(1).trim();
            Model.getCoreHelper().setVisibility(role,
                            Model.getVisibilityKind().getPackage());
        }
        if (name != null) {
            Model.getCoreHelper().setName(role, name);
        }

        StereotypeUtility.dealWithStereotypes(role, stereotype, true);
    }

    private void dealWithDerived(Object umlObject, boolean derived) {
        NotationUtilityUml.setDerived(umlObject, derived);
    }

    private String toString(Object modelElement, boolean showVisibility,
            boolean useGuillemets) {
        // TODO: This whole block can be deleted when issue 6266 is resolved
        if (Model.getFacade().isAConnectorEnd(modelElement)) {
            return "";
        }
        // end of block
        String derived = "";
        Object tv = Model.getFacade().getTaggedValue(modelElement, 
                Facade.DERIVED_TAG);
        if (tv != null) {
            String tag = Model.getFacade().getValueOfTag(tv);
            if ("true".equalsIgnoreCase(tag)) {
                derived = "/";
            }
        }   
        
        String name = Model.getFacade().getName(modelElement);
        if (name == null) {
            name = "";
        }

        String visibility = "";
        if (showVisibility) {
            visibility = NotationUtilityUml.generateVisibility2(modelElement);

            if (name.length() < 1) {
                visibility = "";
                // this is the temporary solution for issue 1011
            }
        }

        String stereoString = 
            NotationUtilityUml.generateStereotype(modelElement, useGuillemets);

        if (stereoString.length() > 0) {
            stereoString += " ";
        }

        return derived + stereoString + visibility + name;
    }

    @Override
    public String toString(Object modelElement, NotationSettings settings) {
        return toString(modelElement, settings.isShowVisibilities(), 
                settings.isUseGuillemets());
    }

}
