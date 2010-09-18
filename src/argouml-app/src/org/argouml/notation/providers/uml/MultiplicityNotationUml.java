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

package org.argouml.notation.providers.uml;

import java.text.ParseException;

import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoHelpEvent;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.notation.NotationSettings;
import org.argouml.notation.providers.MultiplicityNotation;

/**
 * The UML notation for a Multiplicity. <p>
 * 
 * This NotationProvider is a bit special, in that it does not 
 * generate for the given UML element, but for its multiplicity.
 *
 * @author Michiel
 */
public class MultiplicityNotationUml extends MultiplicityNotation {

    /**
     * @param multiplicityOwner the UML element to represent the multiplicity of
     */
    public MultiplicityNotationUml(Object multiplicityOwner) {
        super(multiplicityOwner);
    }

    @Override
    public String getParsingHelp() {
        return "parsing.help.fig-multiplicity";
    }

    @Override
    public void parse(final Object multiplicityOwner, final String text) {
        try {
            setMultiplicity(multiplicityOwner, text);
        } catch (ParseException pe) {
            final String msg = "statusmsg.bar.error.parsing.multiplicity";
            final Object[] args = {pe.getLocalizedMessage(),
                             Integer.valueOf(pe.getErrorOffset()), };
            ArgoEventPump.fireEvent(new ArgoHelpEvent(
                    ArgoEventTypes.HELP_CHANGED, this,
                    Translator.messageFormat(msg, args)));
        }
    }

    /**
     * @deprecated by tfmorris for 0.31.6.  No application code should expect
     * to handle Multiplicty elements (the return value of this method).  Use
     * {@link #setMultiplicity(Object, String)}
     */
    @Deprecated
    protected Object parseMultiplicity(final Object multiplicityOwner,
            final String s1) throws ParseException {
        String s = s1.trim();
        Object multi = null;
        try {
            multi = Model.getDataTypesFactory().createMultiplicity(s);
        } catch (IllegalArgumentException iae) {
            throw new ParseException(iae.getLocalizedMessage(), 0);
        }
        setMultiplicity(multiplicityOwner, s1);
        return multi;
    }

    protected void setMultiplicity(final Object multiplicityOwner,
            final String arg) throws ParseException {
        try {
            Model.getCoreHelper().setMultiplicity(multiplicityOwner, arg);
        } catch (IllegalArgumentException iae) {
            throw new ParseException(iae.getLocalizedMessage(), 0);
        }
    }

    @Override
    public String toString(Object modelElement, NotationSettings settings) {
        return NotationUtilityUml.generateMultiplicity(modelElement, 
                settings.isShowSingularMultiplicities());
    }

}
