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

package org.argouml.uml.diagram.ui;

import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;

import org.argouml.model.ModelFacade;
import org.argouml.uml.ui.UMLAction;
import org.argouml.uml.ui.UMLBooleanProperty;
import org.argouml.uml.ui.UMLEnumerationBooleanProperty;
import org.argouml.uml.ui.UMLReflectionBooleanProperty;

/**
 * A class to perform the action of changing value of the modifiers on a
 * package, use case, interface or class.<p>
 *
 * @author  Bob Tarling
 *
 * @author  Jeremy Bennett (mail@jeremybennett.com)&mdash; use case extensions.
 */
public class ActionModifier extends UMLAction {
    private UMLBooleanProperty property;
    private Object object;
    private Class mclassClass = (Class) ModelFacade.CLASS;

    /**
     * Defines an Action object with the specified description which
     * will use the given reflection methods to modify boolean values
     * in an <code>MClass</code> object.
     *
     * @param     name           the text for the actions menu item
     * @param     propertyName   the name of the modifier property to be amended
     * @param getMethod the name of the getter method to retrieve the
     * property
     * @param setMethod the name of the setter method to change the
     * property
     * @param mclass the <code>MClass</code> object containing the
     * modifier property.
     */
    public ActionModifier(String name, String propertyName,
			  String getMethod,
			  String setMethod,
			  Object mclass) {
	super(name, NO_ICON);
	this.object = mclass;
	property =
	    new UMLReflectionBooleanProperty(propertyName, mclassClass,
					     getMethod,
					     setMethod);
	putValue("SELECTED", new Boolean(property.getProperty(object)));
    }

    /**
     * Defines an Action object with the specified description which
     * will use the given reflection methods to modify an enumerated
     * values in an <code>MClass</code> object.
     *
     * @param     name           the text for the actions menu item
     * @param     propertyName   the name of the modifier property to be amended
     * @param getMethod the name of the getter method to retrieve the
     * property
     * @param setMethod the name of the setter method to change the
     * property
     * @param mclass the <code>MClass</code> object containing the
     * modifier property.
     * @param     enumClass      the class representing the enumeration
     * @param     theTrueValue      The enumerated value representing true
     * @param     theFalseValue     The enumerated value representing false
     */
    public ActionModifier(String name, String propertyName,
			  String getMethod,
			  String setMethod,
			  Object mclass,
			  Class enumClass,
			  Object theTrueValue,
			  Object theFalseValue)
    {
	super(name, NO_ICON);
	this.object = mclass;
	property =
	    new UMLEnumerationBooleanProperty(propertyName, mclassClass,
					      getMethod,
					      setMethod,
					      enumClass,
					      theTrueValue,
					      theFalseValue);
	putValue("SELECTED", new Boolean(property.getProperty(object)));
    }

    ////////////////////////////////////////////////////////////////
    // main methods

    /**
     * To perform the action of changing a modifier.
     *
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
    	try {
	    property.setProperty(object, !property.getProperty(object));
    	}
    	catch (PropertyVetoException ve) { }
    }

    /**
     * The action is always enabled
     *
     * @see org.argouml.uml.ui.UMLAction#shouldBeEnabled()
     */
    public boolean shouldBeEnabled() {
	return true;
    }
} /* end class ActionModifier */