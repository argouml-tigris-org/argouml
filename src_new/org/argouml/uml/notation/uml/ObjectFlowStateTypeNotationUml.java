// $Id$
// Copyright (c) 2006 The Regents of the University of California. All
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

package org.argouml.uml.notation.uml;

import java.text.ParseException;
import java.util.HashMap;

import org.argouml.application.events.StatusMonitor;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.notation.ObjectFlowStateTypeNotation;

/**
 * @author Michiel
 */
public class ObjectFlowStateTypeNotationUml
    extends ObjectFlowStateTypeNotation {

    /**
     * The constructor.
     *
     * @param objectflowstate the ObjectFlowState represented by this notation
     */
    public ObjectFlowStateTypeNotationUml(Object objectflowstate) {
        super(objectflowstate);
    }

    /**
     * @see org.argouml.uml.notation.NotationProvider#parse(java.lang.Object, java.lang.String)
     */
    public void parse(Object modelElement, String text) {
        try {
            parseObjectFlowState1(modelElement, text);
        } catch (ParseException pe) {
            String msg = "statusmsg.bar.error.parsing.objectflowstate";
            Object[] args = {
                pe.getLocalizedMessage(),
                new Integer(pe.getErrorOffset()),
            };
            StatusMonitor.notify(this, Translator.messageFormat(msg, args));
        }
    }

    /**
     * Do the actual parsing.
     *
     * @param objectFlowState the given element to be altered
     * @param s the new string
     * @return the altered ObjectFlowState
     * @throws ParseException when the given text was rejected
     */
    protected Object parseObjectFlowState1(Object objectFlowState, String s)
        throws ParseException {
        Object c =
            Model.getActivityGraphsHelper()
                    .findClassifierByName(objectFlowState, s);
        if (c != null) {
            Model.getCoreHelper().setType(objectFlowState, c);
        } else {
            String msg = "parsing.error.object-flow-type.classifier-not-found";
            Object[] args = {s};
            throw new ParseException(
                    Translator.localize(msg, args), 
                    0);
        }
        return objectFlowState;
    }

    /**
     * @see org.argouml.uml.notation.NotationProvider#getParsingHelp()
     */
    public String getParsingHelp() {
        return "parsing.help.fig-objectflowstate1";
    }

    /**
     * @see org.argouml.uml.notation.NotationProvider#toString(java.lang.Object, java.util.HashMap)
     */
    public String toString(Object modelElement, HashMap args) {
        Object classifier = Model.getFacade().getType(modelElement);
        if (Model.getFacade().isAClassifierInState(classifier)) {
            classifier = Model.getFacade().getType(classifier);
        }
        if (classifier == null) {
            return "";
        }
        String name = Model.getFacade().getName(classifier);
        if (name == null) {
            name = "";
        }
        return name;
    }

}
