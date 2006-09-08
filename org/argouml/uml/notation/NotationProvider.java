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

package org.argouml.uml.notation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

import org.argouml.model.Model;

/**
 * A class that implements this abstract class manages a text
 * shown on a diagram. This means it is able to generate
 * text that represents one or more UML objects.
 * And when the user has edited this text, the model may be adapted
 * by parsing the text.
 * Additionally, a help text for the parsing is provided,
 * so that the user knows the syntax.
 * 
 * @author mvw@tigris.org
 */
public abstract class NotationProvider {

    /**
     * @return a i18 key that represents a help string
     *         giving an explanation to the user of the syntax
     */
    public abstract String getParsingHelp();

    
    /**
     * Utility function to determine the presence of a key. 
     * The default is false.
     * 
     * @param key the string for the key
     * @param map the hashmap to check for the presence 
     * and value of the key
     * @return true if the value for the key is true, otherwise false
     */
    public static boolean isValue(String key, HashMap map) {
        if (map == null) return false;
        Object o = map.get(key);
        if (!(o instanceof Boolean)) {
            return false;
        }
        return ((Boolean) o).booleanValue();
    }

    /**
     * Parses the given text, and adapts the modelElement and
     * maybe related elements accordingly.
     * 
     * @param modelElement the modelelement to adapt
     * @param text the string given by the user to be parsed
     * to adapt the model
     */
    public abstract void parse(Object modelElement, String text);

    /**
     * Generates a string representation for the given model element.
     * 
     * @param modelElement the base UML modelelement
     * @param args arguments that may determine the notation
     * @return the string written in the correct notation
     */
    public abstract String toString(Object modelElement, HashMap args);
    
    /**
     * Add the appropriate model change listeners 
     * for the given modelelement to the given listener.
     * 
     * @param listener the given listener
     * @param modelElement the modelelement that we provide 
     * notation for
     */
    public void addListener(PropertyChangeListener listener, 
            Object modelElement) {
        Model.getPump().addModelEventListener(
                listener, 
                modelElement, 
                "name");
    }
    
    /**
     * Remove the listeners registered before.
     * 
     * @param listener the given listener
     * @param modelElement the modelelement that we provide 
     * notation for
     */
    public void removeListener(PropertyChangeListener listener, 
            Object modelElement) {
        Model.getPump().removeModelEventListener(
                listener, 
                modelElement, 
                "name");
    }
    
    /**
     * Update the set of listeners based on the given event.
     * 
     * @param listener the given listener
     * @param modelElement the modelelement that we provide 
     * notation for
     * @param pce the received event, that we base the changes on
     */
    public void updateListener(PropertyChangeListener listener, 
            Object modelElement,
            PropertyChangeEvent pce) {
        // e.g. for an operation:
        // if pce.getSource() == modelElement
        // && event.propertyName = "parameter"
        //     if event instanceof AddAssociationEvent
        //         Get the parameter instance from event.newValue
        //         Call model to add listener on parameter on change 
        //             of "name", "type"
        //     else if event instanceof RemoveAssociationEvent
        //         Get the parameter instance from event.oldValue
        //         Call model to remove listener on parameter on change 
        //             of "name", "type"
        //     end if
        // end if 
    }
}
