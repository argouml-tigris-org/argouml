// Copyright (c) 1996-99 The Regents of the University of California. All
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

package org.argouml.uml.ui;
import java.beans.PropertyVetoException;
import java.lang.reflect.*;
import ru.novosoft.uml.*;

/** This abstract base class is used to define derived classes that interact
 * with the UMLRadioButton and UMLCheckBox user interface components.
 *
 *  @author Curt Arnold
 */
abstract public class UMLBooleanProperty  {

    private String _propertyName;
    
    /** Creates new UMLBooleanProperty
     * @param propertyName name of property monitored, null will cause 
             component to be updated on any change to monitored model element.
    */
    public UMLBooleanProperty(String propertyName) {
        _propertyName = propertyName;
    }
    
    /**
     *   Sets property on element.
     *   @param element Element whose property will be changed.
     *   @param newState new state of property.
     */
    abstract public void setProperty(Object element,boolean newState) throws PropertyVetoException;
    
    /**
     *   Retreives current state of property.
     *   @param element Elements whose property will be retrieved.
     *   @return current state of property.
     */
    abstract public boolean getProperty(Object element);
    
    /**
     *   Returns true if a specific NSUML event should have an affect
     *   on this property.
     *
     *   @param event NSUML event
     *   @return returns true if property may have been affected by change.
     */
    public boolean isAffected(MElementEvent event) {
        String propName = event.getName();
	//System.out.println("eventName: "+propName);
        if(_propertyName == null || propName == null || propName.equals(_propertyName)) 
            return true;
        return false;
    }
    
    public String getPropertyName() {
        return _propertyName;
    }
}


