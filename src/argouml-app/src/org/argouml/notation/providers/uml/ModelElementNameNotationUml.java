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

package org.argouml.notation.providers.uml;

import java.text.ParseException;

import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoHelpEvent;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.notation.NotationSettings;
import org.argouml.notation.providers.ModelElementNameNotation;

/**
 * Handles the notation of the name of a modelElement in UML,
 * i.e. a string on the format:<pre>
 *     [ &lt;&lt; stereotype &gt;&gt;] [+|-|#] [name]
 * </pre>
 *
 * @author Michiel van der Wulp
 */
public class ModelElementNameNotationUml extends ModelElementNameNotation {

    /**
     * The constructor.
     *
     * @param modelElement the UML object
     */
    public ModelElementNameNotationUml(Object modelElement) {
        super(modelElement);
    }

    /*
     * @see org.argouml.notation.providers.NotationProvider#parse(java.lang.Object, java.lang.String)
     */
    public void parse(Object modelElement, String text) {
        try {
            NotationUtilityUml.parseModelElement(modelElement, text);
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

    /*
     * @see org.argouml.notation.providers.NotationProvider#getParsingHelp()
     */
    public String getParsingHelp() {
        return "parsing.help.fig-nodemodelelement";
    }

    private String toString(Object modelElement, boolean handleStereotypes, 
            boolean useGuillemets, boolean showVisibility, boolean showPath) {
        String name = Model.getFacade().getName(modelElement);
        StringBuffer sb = new StringBuffer("");
        if (handleStereotypes) {
            sb.append(NotationUtilityUml.generateStereotype(
                    modelElement, 
                    useGuillemets));
        }
        if (showVisibility) {
            sb.append(generateVisibility(modelElement));
        }
        if (showPath) {
            sb.append(NotationUtilityUml.generatePath(modelElement));
        }
        if (name != null) {
            sb.append(name);
        }
        return sb.toString();
    }

    private String generateVisibility(Object modelElement) {
        String s = NotationUtilityUml.generateVisibility2(modelElement);
        /* When nothing is generated: omit the space. */
        if (s.length() > 0) {
            s = s + " ";
        }            
        return s;
    }

    @Override
    public String toString(Object modelElement, NotationSettings settings) {
        return toString(modelElement, settings.isFullyHandleStereotypes(),
                settings.isUseGuillemets(), settings.isShowVisibilities(),
                settings.isShowPaths());
    }

}
