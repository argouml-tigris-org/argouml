// $Id$
// Copyright (c) 2005-2006 The Regents of the University of California. All
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

import java.util.HashMap;

import org.argouml.model.Model;
import org.argouml.uml.notation.ActionStateNotation;


/**
 * The Notation for an ActionState.
 * 
 * @author mvw@tigris.org
 */
public class ActionStateNotationUml extends ActionStateNotation {

    /**
     * The constructor.
     *
     * @param actionState the UML ActionState
     */
    public ActionStateNotationUml(Object actionState) {
        super(actionState);
    }

    /**
     * @see org.argouml.uml.notation.NotationProvider#parse(java.lang.Object, java.lang.String)
     */
    public void parse(Object modelElement, String text) {
        Object entry = Model.getFacade().getEntry(modelElement);
        String language = "";
        if (entry == null) {
            entry =
                Model.getCommonBehaviorFactory()
                        .buildUninterpretedAction(modelElement);
        } else {
            Object script = Model.getFacade().getScript(entry);
            if (script != null) {
                language = Model.getDataTypesHelper().getLanguage(script);
            }
        }
        Object actionExpression =
            Model.getDataTypesFactory().createActionExpression(language, text);
        Model.getCommonBehaviorHelper().setScript(entry, actionExpression);
    }

    /**
     * @see org.argouml.uml.notation.NotationProvider#getParsingHelp()
     */
    public String getParsingHelp() {
        return "parsing.help.fig-actionstate";
    }

    /**
     * @see org.argouml.uml.notation.NotationProvider#toString(java.lang.Object, java.util.HashMap)
     */
    public String toString(Object modelElement, HashMap args) {
        String ret = "";
        Object action = Model.getFacade().getEntry(modelElement);
        if (action != null) {
            Object expression = Model.getFacade().getScript(action);
            if (expression != null) {
                ret = (String) Model.getFacade().getBody(expression);
            }
        }
        return (ret == null) ? "" : ret;
    }

}
