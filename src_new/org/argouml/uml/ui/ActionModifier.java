// $Id$
// Copyright (c) 1996-2001 The Regents of the University of California. All
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

// File: ActionModifier.java
// Classes: ActionModifier
// Original Author: Bob Tarling
// $Id$

// 9 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Extended to support
// use cases


package org.argouml.uml.ui;

import org.argouml.uml.diagram.ui.*;
import org.tigris.gef.base.*;
import org.tigris.gef.presentation.*;
import java.awt.event.*;
import java.beans.PropertyVetoException;
import java.util.*;
import org.argouml.model.ModelFacade;
import ru.novosoft.uml.behavior.use_cases.MUseCase;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MInterface;
import ru.novosoft.uml.model_management.MPackage;

/**
 * <p>A class to perform the action of changing value of the modifiers on a
 *   package, use case, interface or class. </p>
 *
 * @author  Bob Tarling
 *
 * @author  Jeremy Bennett (mail@jeremybennett.com)&mdash; use case extensions.
 */

public class ActionModifier extends UMLAction {
    private UMLBooleanProperty _property;
    private Object object;
    Class mclassClass = (Class)ModelFacade.CLASS;
    Class mpackageClass = (Class)ModelFacade.PACKAGE;
    Class minterfaceClass = (Class)ModelFacade.INTERFACE;
    Class museCaseClass = (Class)ModelFacade.USE_CASE;      // Jeremy Bennett
    Object trueValue = null;
    Object falseValue = null;

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
			  MClass mclass) {
	super(name, NO_ICON);
	this.object = mclass;
	_property =
	    new UMLReflectionBooleanProperty(propertyName, mclassClass,
					     getMethod,
					     setMethod);
	putValue("SELECTED", new Boolean(_property.getProperty(object)));
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
     * @param     trueValue      The enumerated value representing true
     * @param     falseValue     The enumerated value representing false
     */
    public ActionModifier(String name, String propertyName,
			  String getMethod,
			  String setMethod,
			  MClass mclass,
			  Class enumClass,
			  Object trueValue,
			  Object falseValue)
    {
	super(name, NO_ICON);
	this.object = mclass;
	_property =
	    new UMLEnumerationBooleanProperty(propertyName, mclassClass,
					      getMethod,
					      setMethod,
					      enumClass,
					      trueValue,
					      falseValue);
	putValue("SELECTED", new Boolean(_property.getProperty(object)));
    }

    /**
     * Defines an Action object with the specified description which
     * will use the given reflection methods to modify boolean values
     * in an <code>MInterface</code> object.
     *
     * @param     name           the text for the actions menu item
     * @param     propertyName   the name of the modifier property to be amended
     * @param getMethod the name of the getter method to retrieve the
     * property
     * @param setMethod the name of the setter method to change the
     * property
     * @param minterface the <code>MInterface</code> object containing
     * the modifier property.
     */
    public ActionModifier(String name, String propertyName,
			  String getMethod,
			  String setMethod,
			  MInterface minterface)
    {
	super(name, NO_ICON);
	this.object = minterface;
	_property = new UMLReflectionBooleanProperty(propertyName, minterfaceClass, getMethod, setMethod);
	putValue("SELECTED", new Boolean(_property.getProperty(object)));
    }

    /**
     * Defines an Action object with the specified description which
     * will use the given reflection methods to modify an enumerated
     * values in an <code>MInterface</code> object.
     *
     * @param     name           the text for the actions menu item
     * @param     propertyName   the name of the modifier property to be amended
     * @param getMethod the name of the getter method to retrieve the
     * property
     * @param setMethod the name of the setter method to change the
     * property
     * @param minterface the <code>MInterface</code> object containing
     * the modifier property.
     * @param     enumClass      the class representing the enumeration
     * @param     trueValue      The enumerated value representing true
     * @param     falseValue     The enumerated value representing false
     */
    public ActionModifier(String name,
                          String propertyName,
			  String getMethod,
			  String setMethod,
			  MInterface minterface,
			  Class enumClass,
			  Object trueValue,
			  Object falseValue)
    {
	super(name, NO_ICON);
	this.object = minterface;
	_property =
	    new UMLEnumerationBooleanProperty(propertyName, minterfaceClass,
					      getMethod,
					      setMethod,
					      enumClass,
					      trueValue,
					      falseValue);
	putValue("SELECTED", new Boolean(_property.getProperty(object)));
    }

    /**
     * Defines an Action object with the specified description which
     * will use the given reflection methods to modify boolean values
     * in an <code>MPackage</code> object.
     *
     * @param     name           the text for the actions menu item
     * @param     propertyName   the name of the modifier property to be amended
     * @param getMethod the name of the getter method to retrieve the
     * property
     * @param setMethod the name of the setter method to change the
     * property
     * @param mpackage the <code>MPackage</code> object containing the
     * modifier property.
     */
    public ActionModifier(String name, String propertyName, String getMethod, String setMethod, MPackage mpackage) {
	super(name, NO_ICON);
	this.object = mpackage;
	_property =
	    new UMLReflectionBooleanProperty(propertyName, mpackageClass,
					     getMethod,
					     setMethod);
	putValue("SELECTED", new Boolean(_property.getProperty(object)));
    }

    /**
     * Defines an Action object with the specified description which
     * will use the given reflection methods to modify an enumerated
     * values in an <code>MPackage</code> object.
     *
     * @param     name           the text for the actions menu item
     * @param     propertyName   the name of the modifier property to be amended
     * @param getMethod the name of the getter method to retrieve the
     * property
     * @param setMethod the name of the setter method to change the
     * prvoperty
     * @param mpackage the <code>MPackage</code> object containing the
     * modifier property.
     * @param     enumClass      the class representing the enumeration
     * @param     trueValue      The enumerated value representing true
     * @param     falseValue     The enumerated value representing false
     */
    public ActionModifier(String name,
                          String propertyName,
			  String getMethod,
			  String setMethod,
			  MPackage mpackage,
			  Class enumClass,
			  Object trueValue,
			  Object falseValue) {
	super(name, NO_ICON);
	this.object = mpackage;
	_property =
	    new UMLEnumerationBooleanProperty(propertyName,
                                              mpackageClass,
					      getMethod,
					      setMethod,
					      enumClass,
					      trueValue,
					      falseValue);
	putValue("SELECTED", new Boolean(_property.getProperty(object)));
    }

    /**
     * <p>Defines an Action object with the specified description which will
     *   use the given reflection methods to modify boolean values in a {@link
     *   MUseCase} object.
     *
     * @param name          the text for the actions menu item
     *
     * @param propertyName  the name of the modifier property to be amended 
     *
     * @param getMethod     the name of the getter method to retrieve the
     *                      property 
     *
     * @param setMethod     the name of the setter method to change the
     *                      property 
     *
     * @param museCase      the use case object containing the modifier
     *                      property. 
     */ 

    public ActionModifier(String name, String propertyName, String getMethod,
                          String setMethod, MUseCase museCase) { 
        super(name, NO_ICON);
        this.object = museCase;
        _property   = new UMLReflectionBooleanProperty(propertyName,
                                                       museCaseClass,
                                                       getMethod, setMethod);
        putValue("SELECTED", new Boolean(_property.getProperty(object)));
    }


    /**
     * <p>Defines an Action object with the specified description which will
     *   use the given reflection methods to modify an enumerated values in a
     *   {@link MUseCase} object.</p>
     *
     * @param name          The text for the actions menu item.
     *
     * @param propertyName  The name of the modifier property to be amended.
     *
     * @param getMethod     The name of the getter method to retrieve the
     *                      property.
     *
     * @param setMethod     The name of the setter method to change the
     *                      property.
     *
     * @param museCase      The use case object containing the modifier
     *                      property.
     *
     * @param enumClass     The class representing the enumeration.
     *
     * @param trueValue     The enumerated value representing true.
     *
     * @param falseValue    The enumerated value representing false.
     */

    public ActionModifier(String name, String propertyName, String getMethod,
                          String setMethod, MUseCase museCase, Class enumClass,
                          Object trueValue, Object falseValue) {
        super(name, NO_ICON);
        this.object = museCase;
        _property   = new UMLEnumerationBooleanProperty(propertyName,
                                                        museCaseClass,
                                                        getMethod,
                                                        setMethod,
                                                        enumClass,
                                                        trueValue,
                                                        falseValue);
        putValue("SELECTED", new Boolean(_property.getProperty(object)));
    }


    ////////////////////////////////////////////////////////////////
    // main methods

    /**
     * To perform the action of changing a modifier
     */
    public void actionPerformed(ActionEvent ae) {
    	try {
	    _property.setProperty(object, !_property.getProperty(object));
    	}
    	catch (PropertyVetoException ve) { }
    }

    /**
     * The action is always enabled
     */
    public boolean shouldBeEnabled() {
	return true;
    }
} /* end class ActionModifier */