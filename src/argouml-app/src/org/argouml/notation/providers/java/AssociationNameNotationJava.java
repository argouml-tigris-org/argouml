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

// Copyright (c) 2007-2009 The Regents of the University of California. All
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

package org.argouml.notation.providers.java;

import java.text.ParseException;

import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoHelpEvent;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.notation.NotationSettings;
import org.argouml.notation.providers.AssociationNameNotation;

/**
 * Java notation for the name of an association.
 *
 * @author Michiel van der Wulp
 */
public class AssociationNameNotationJava extends AssociationNameNotation {

    /**
     * The constructor.
     * 
     * @param modelElement the modelelement
     */
    public AssociationNameNotationJava(Object modelElement) {
        super(modelElement);
    }

    /*
     * For Java notation, we also need to listen to "leaf" changes, 
     * since they are shown as "final" on the diagram.
     */
    @Override
    public void initialiseListener(final Object modelElement) {
        addElementListener(modelElement, 
                new String[] {"isLeaf"});
        super.initialiseListener(modelElement);
    }

    /*
     * @see org.argouml.notation.providers.NotationProvider#getParsingHelp()
     */
    public String getParsingHelp() {
        return "parsing.help.java.fig-nodemodelelement";
    }

    /*
     * @see org.argouml.notation.providers.NotationProvider#parse(java.lang.Object, java.lang.String)
     */
    public void parse(final Object modelElement, final String text) {
        try {
            ModelElementNameNotationJava.parseModelElement(modelElement, text);
        } catch (ParseException pe) {
            final String msg = "statusmsg.bar.error.parsing.node-modelelement";
            final Object[] args = {
                pe.getLocalizedMessage(),
                Integer.valueOf(pe.getErrorOffset()),
            };
            ArgoEventPump.fireEvent(new ArgoHelpEvent(
                    ArgoEventTypes.HELP_CHANGED, this,
                Translator.messageFormat(msg, args)));
        }
    }

    @Override
    public String toString(final Object modelElement, 
            final NotationSettings settings) {
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
