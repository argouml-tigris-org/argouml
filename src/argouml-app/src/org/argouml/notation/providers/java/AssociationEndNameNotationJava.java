/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2010 Contributors - see below
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

package org.argouml.notation.providers.java;

import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoHelpEvent;
import org.argouml.model.Model;
import org.argouml.notation.NotationSettings;
import org.argouml.notation.providers.AssociationEndNameNotation;
import org.argouml.notation.providers.uml.NotationUtilityUml;

/**
 * The Java notation for an associationEnd name (i.e. the  role).
 * 
 * @author michiel
 */
public class AssociationEndNameNotationJava extends AssociationEndNameNotation {

    /**
     * The constructor.
     * 
     * @param associationEnd the UML element
     */
    public AssociationEndNameNotationJava(Object associationEnd) {
        super(associationEnd);
    }

    /*
     * @see org.argouml.notation.providers.NotationProvider#getParsingHelp()
     */
    public String getParsingHelp() {
//        return "parsing.help.fig-association-end-name";
        return "Parsing in Java not yet supported";
    }

    /*
     * @see org.argouml.notation.providers.NotationProvider#parse(java.lang.Object, java.lang.String)
     */
    public void parse(Object modelElement, String text) {
        ArgoEventPump.fireEvent(new ArgoHelpEvent(
                ArgoEventTypes.HELP_CHANGED, this,
            "Parsing in Java not yet supported"));
    }

    private String toString(Object modelElement, boolean useGuillemets) {
        String name = Model.getFacade().getName(modelElement);
        if (name == null) {
            name = "";
        }

        Object visi = Model.getFacade().getVisibility(modelElement);
        String visibility = "";
        if (visi != null) {
            visibility = NotationUtilityJava.generateVisibility(visi);
        }
        if (name.length() < 1) {
            visibility = "";
            //this is the temporary solution for issue 1011
        }

        String stereoString = 
            NotationUtilityUml.generateStereotype(modelElement, useGuillemets);

        return stereoString + visibility + name;
    }

    @Override
    public String toString(Object modelElement, NotationSettings settings) {
        return toString(modelElement, settings.isUseGuillemets());
    }
    
}
