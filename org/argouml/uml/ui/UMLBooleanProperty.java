// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

import org.apache.log4j.Logger;
import ru.novosoft.uml.MElementEvent;

/**
 * This abstract base class is used to define derived classes that interact
 * with the UMLRadioButton and UMLCheckBox user interface components.<p>
 *
 * @author Curt Arnold
 *
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
 * this class is part of the 'old'(pre 0.13.*) implementation of proppanels
 * that used reflection a lot.
 * TODO: What is it replaced by?
 */
public abstract class UMLBooleanProperty  {

    private static final Logger LOG =
	Logger.getLogger(UMLBooleanProperty.class);

    private String propertyName;
    
    /** 
     * Creates new UMLBooleanProperty.
     * 
     * @param name name of property monitored, null will cause 
     *        component to be updated on any change to monitored model element
     */
    public UMLBooleanProperty(String name) {
        propertyName = name;
    }
    
    /**
     *   Sets property on element.
     *   @param element Element whose property will be changed.
     *   @param newState new state of property.
     */
    public abstract void setProperty(Object element, boolean newState)
	throws PropertyVetoException;
    
    /**
     *   Retreives current state of property.
     *   @param element Elements whose property will be retrieved.
     *   @return current state of property.
     */
    public abstract boolean getProperty(Object element);
    
    /**
     *   Returns true if a specific NSUML event should have an affect
     *   on this property.
     *
     *   @param event NSUML event
     *   @return returns true if property may have been affected by change.
     */
    public boolean isAffected(MElementEvent event) {
        String propName = event.getName();
	LOG.debug("eventName: " + propName);
        if (propertyName == null
	    || propName == null
	    || propName.equals(propertyName)) {

            return true;

	}
        return false;
    }
    
    /**
     * @return the property name
     */
    public String getPropertyName() {
        return propertyName;
    }
}


