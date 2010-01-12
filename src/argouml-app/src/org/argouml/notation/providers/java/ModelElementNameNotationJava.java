/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mvw
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2005-2009 The Regents of the University of California. All
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

package org.argouml.notation.providers.java;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoHelpEvent;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.notation.NotationSettings;
import org.argouml.notation.providers.ModelElementNameNotation;
import org.argouml.util.MyTokenizer;

/**
 * Java notation for the name of a modelelement.
 * 
 * @author Michiel
 */
public class ModelElementNameNotationJava extends ModelElementNameNotation {

    /**
     * The constructor.
     *
     * @param name the modelelement
     */
    public ModelElementNameNotationJava(Object name) {
        super(name);
    }

    /*
     * @see org.argouml.notation.providers.NotationProvider#parse(
     * java.lang.Object, java.lang.String)
     */
    public void parse(Object modelElement, String text) {
        try {
            parseModelElement(modelElement, text);
        } catch (ParseException pe) {
            String msg = "statusmsg.bar.error.parsing.node-modelelement";
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
     * @param modelElement the UML modelelement
     * @param text the string to parse
     */
    static void parseModelElement(Object modelElement, String text) 
        throws ParseException {
        MyTokenizer st;

        boolean abstrac = false;
        boolean fina = false;
        boolean publi = false;
        boolean privat = false;
        boolean protect = false;
        String token;
        List<String> path = null;
        String name = null;

        try {
            st = new MyTokenizer(text, 
                    " ,.,abstract,final,public,private,protected");
            while (st.hasMoreTokens()) {
                token = st.nextToken();

                if (" ".equals(token)) {
                    /* skip spaces */
                } else if ("abstract".equals(token)) {
                    abstrac = true;
                } else if ("final".equals(token)) {
                    fina = true;
                } else if ("public".equals(token)) {
                    publi = true;
                } else if ("private".equals(token)) {
                    privat = true;
                } else if ("protected".equals(token)) {
                    protect = true;
                } else if (".".equals(token)) {
                    if (name != null) {
                        name = name.trim();
                    }

                    if (path != null && (name == null || "".equals(name))) {
                        String msg = 
                            "parsing.error.model-element-name.anon-qualifiers";
                        throw new ParseException(Translator.localize(msg), 
                                st.getTokenIndex());
                    }

                    if (path == null) {
                        path = new ArrayList<String>();
                    }
                    if (name != null) {
                        path.add(name);
                    }
                    name = null;
                
                } else { // the name itself
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
        
        if (path != null && (name == null || "".equals(name))) {
            String msg = "parsing.error.model-element-name.must-end-with-name";
            throw new ParseException(Translator.localize(msg), 0);
        }
        
        /* Check the name for validity: */
        if (!isValidJavaClassName(name)) {
            throw new ParseException(
                    "Invalid class name for Java: "
                    + name, 0);
        }
        
        if (path != null) {
            Object nspe =
                Model.getModelManagementHelper().getElement(
                    path,
                    Model.getFacade().getRoot(modelElement));

            if (nspe == null || !(Model.getFacade().isANamespace(nspe))) {
                String msg = 
                        "parsing.error.model-element-name.namespace-unresolved";
                throw new ParseException(Translator.localize(msg), 
                        0);
            }
            if (!Model.getCoreHelper().isValidNamespace(
                    modelElement, nspe)) {
                String msg = 
                        "parsing.error.model-element-name.namespace-invalid";
                throw new ParseException(Translator.localize(msg), 
                        0);
            }
            Model.getCoreHelper().addOwnedElement(nspe, modelElement);
        }

        Model.getCoreHelper().setName(modelElement, name);
        
        if (abstrac) {
            Model.getCoreHelper().setAbstract(modelElement, abstrac);
        }
        if (fina) {
            Model.getCoreHelper().setLeaf(modelElement, fina);
        }
        if (publi) {
            Model.getCoreHelper().setVisibility(modelElement,
                Model.getVisibilityKind().getPublic());
        }
        if (privat) {
            Model.getCoreHelper().setVisibility(modelElement,
                Model.getVisibilityKind().getPrivate());
        }
        if (protect) {
            Model.getCoreHelper().setVisibility(modelElement,
                Model.getVisibilityKind().getProtected());
        }
    }

    /**
     * @param name the name of the element
     * @return true if the given name is a valid name according java syntax
     */
    private static boolean isValidJavaClassName(String name) {
        /* TODO: Check the name for validity. */
        return true;
    }

    /*
     * @see org.argouml.notation.providers.NotationProvider#getParsingHelp()
     */
    public String getParsingHelp() {
        return "parsing.help.java.fig-nodemodelelement";
    }

    public String toString(Object modelElement, NotationSettings settings) {
        String name;
        name = Model.getFacade().getName(modelElement);
        if (name == null) {
            return "";
        }
        String visibility = "";
        if (settings.isShowVisibilities()) {
            visibility = NotationUtilityJava.generateVisibility(modelElement);
        }
        String path = "";
        if (settings.isShowPaths()) {
            path = NotationUtilityJava.generatePath(modelElement);
        }
        return NotationUtilityJava.generateLeaf(modelElement)
            + NotationUtilityJava.generateAbstract(modelElement)
            + visibility
            + path
            + name;
    }

}
