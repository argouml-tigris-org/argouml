// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

package org.argouml.model.uml;


import java.util.List;

import org.argouml.model.DataTypesFactory;

import ru.novosoft.uml.foundation.data_types.MActionExpression;
import ru.novosoft.uml.foundation.data_types.MArgListsExpression;
import ru.novosoft.uml.foundation.data_types.MBooleanExpression;
import ru.novosoft.uml.foundation.data_types.MExpression;
import ru.novosoft.uml.foundation.data_types.MIterationExpression;
import ru.novosoft.uml.foundation.data_types.MMappingExpression;
import ru.novosoft.uml.foundation.data_types.MMultiplicity;
import ru.novosoft.uml.foundation.data_types.MObjectSetExpression;
import ru.novosoft.uml.foundation.data_types.MProcedureExpression;
import ru.novosoft.uml.foundation.data_types.MTimeExpression;
import ru.novosoft.uml.foundation.data_types.MTypeExpression;

/**
 * Factory to create UML classes for the UML
 * Foundation::DataTypes package.<p>
 *
 * TODO: Change visibility to package after reflection problem solved.
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
 */
public class DataTypesFactoryImpl
	extends AbstractUmlModelFactory
	implements DataTypesFactory {

    /**
     * The model implementation.
     */
    private NSUMLModelImplementation nsmodel;

    /**
     * Don't allow instantiation.
     *
     * @param implementation To get other helpers and factories.
     */
    DataTypesFactoryImpl(NSUMLModelImplementation implementation) {
        nsmodel = implementation;
    }

    /**
     * @see org.argouml.model.DataTypesFactory#createActionExpression(java.lang.String, java.lang.String)
     */
    public Object createActionExpression(String language,
							      String body) {
        MActionExpression expression = new MActionExpression(language, body);
	super.initialize(expression);
	return expression;
    }

    /**
     * @see org.argouml.model.DataTypesFactory#createArgListsExpression(java.lang.String, java.lang.String)
     */
    public Object createArgListsExpression(String language,
            				   String body) {
        MArgListsExpression expression =
	    new MArgListsExpression(language, body);
	super.initialize(expression);
	return expression;
    }

    /**
     * @see org.argouml.model.DataTypesFactory#createBooleanExpression(java.lang.String, java.lang.String)
     */
    public Object createBooleanExpression(String language,
					  String body) {
        MBooleanExpression expression = new MBooleanExpression(language, body);
	super.initialize(expression);
	return expression;
    }


    /**
     * @see org.argouml.model.DataTypesFactory#createExpression(java.lang.String, java.lang.String)
     */
    public Object createExpression(String language, String body) {
        MExpression expression = new MExpression(language, body);
	super.initialize(expression);
	return expression;
    }

    /**
     * @see org.argouml.model.DataTypesFactory#createIterationExpression(java.lang.String, java.lang.String)
     */
    public Object createIterationExpression(String language,
					    String body) {
        MIterationExpression expression =
	    new MIterationExpression(language, body);
	super.initialize(expression);
	return expression;
    }

    /**
     * @see org.argouml.model.DataTypesFactory#createMappingExpression(java.lang.String, java.lang.String)
     */
    public Object createMappingExpression(String language,
						      String body) {
        MMappingExpression expression = new MMappingExpression(language, body);
	super.initialize(expression);
	return expression;
    }

    /**
     * @see org.argouml.model.DataTypesFactory#createObjectSetExpression(java.lang.String, java.lang.String)
     */
    public Object createObjectSetExpression(String language,
							  String body) {
        MObjectSetExpression expression =
	    new MObjectSetExpression(language, body);
	super.initialize(expression);
	return expression;
    }

    /**
     * @see org.argouml.model.DataTypesFactory#createProcedureExpression(java.lang.String, java.lang.String)
     */
    public Object createProcedureExpression(String language,
							  String body) {
        MProcedureExpression expression =
	    new MProcedureExpression(language, body);
	super.initialize(expression);
	return expression;
    }

    /**
     * @see org.argouml.model.DataTypesFactory#createTimeExpression(java.lang.String, java.lang.String)
     */
    public Object createTimeExpression(String language, String body) {
        MTimeExpression expression = new MTimeExpression(language, body);
	super.initialize(expression);
	return expression;
    }


    /**
     * @see org.argouml.model.DataTypesFactory#createTypeExpression(java.lang.String, java.lang.String)
     */
    public Object createTypeExpression(String language, String body) {
        MTypeExpression expression = new MTypeExpression(language, body);
	super.initialize(expression);
	return expression;
    }


    /**
     * @see org.argouml.model.DataTypesFactory#createMultiplicity(int, int)
     */
    public Object createMultiplicity(int lower, int upper) {
        MMultiplicity multiplicity = new MMultiplicity(lower, upper);
	super.initialize(multiplicity);
	return multiplicity;
    }


    /**
     * @see org.argouml.model.DataTypesFactory#createMultiplicity(java.util.List)
     */
    public Object createMultiplicity(List range) {
        MMultiplicity multiplicity = new MMultiplicity(range);
	super.initialize(multiplicity);
	return multiplicity;
    }


    /**
     * @see org.argouml.model.DataTypesFactory#createMultiplicity(java.lang.String)
     */
    public Object createMultiplicity(String str) {
        MMultiplicity multiplicity = new MMultiplicity(str);
	super.initialize(multiplicity);
	return multiplicity;
    }


}

