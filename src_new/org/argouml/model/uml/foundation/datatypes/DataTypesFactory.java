// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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

package org.argouml.model.uml.foundation.datatypes;

import org.argouml.model.uml.AbstractUmlModelFactory;

import java.util.List;

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
 * Foundation::DataTypes package.
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
 * @stereotype singleton
 */

public class DataTypesFactory extends AbstractUmlModelFactory {

    /** Singleton instance.
     */
    private static DataTypesFactory SINGLETON =
                   new DataTypesFactory();

    /** Singleton instance access method.
     */
    public static DataTypesFactory getFactory() {
        return SINGLETON;
    }

    /** Don't allow instantiation
     */
    private DataTypesFactory() {
    }

    /** Create an empty but initialized instance of a UML ActionExpression.
     *  
     *  @return an initialized UML ActionExpression instance.
     */
    public Object/*MActionExpression*/ createActionExpression(String language,
						    String body) {
        MActionExpression expression = new MActionExpression(language, body);
	super.initialize(expression);
	return expression;
    }

    /** Create an empty but initialized instance of a UML ArgListsExpression.
     *  
     *  @return an initialized UML ArgListsExpression instance.
     */
    public MArgListsExpression createArgListsExpression(String language,
							String body)
    {
        MArgListsExpression expression =
	    new MArgListsExpression(language, body);
	super.initialize(expression);
	return expression;
    }

    /** Create an empty but initialized instance of a UML BooleanExpression.
     *  
     *  @return an initialized UML BooleanExpression instance.
     */
    public Object/*MBooleanExpression*/ createBooleanExpression(String language,
						      String body) {
        MBooleanExpression expression = new MBooleanExpression(language, body);
	super.initialize(expression);
	return expression;
    }

    /** Create an empty but initialized instance of a UML Expression.
     *  
     *  @return an initialized UML Expression instance.
     */
    public MExpression createExpression(String language, String body) {
        MExpression expression = new MExpression(language, body);
	super.initialize(expression);
	return expression;
    }

    /** Create an empty but initialized instance of a UML IterationExpression.
     *  
     *  @return an initialized UML IterationExpression instance.
     */
    public Object/*MIterationExpression*/ createIterationExpression(String language,
							  String body) {
        MIterationExpression expression =
	    new MIterationExpression(language, body);
	super.initialize(expression);
	return expression;
    }

    /** Create an empty but initialized instance of a UML MappingExpression.
     *  
     *  @return an initialized UML MappingExpression instance.
     */
    public MMappingExpression createMappingExpression(String language,
						      String body) {
        MMappingExpression expression = new MMappingExpression(language, body);
	super.initialize(expression);
	return expression;
    }

    /** Create an empty but initialized instance of a UML ObjectSetExpression.
     *  
     *  @return an initialized UML ObjectSetExpression instance.
     */
    public MObjectSetExpression createObjectSetExpression(String language,
							  String body)
    {
        MObjectSetExpression expression =
	    new MObjectSetExpression(language, body);
	super.initialize(expression);
	return expression;
    }

    /** Create an empty but initialized instance of a UML ProcedureExpression.
     *  
     *  @return an initialized UML ProcedureExpression instance.
     */
    public MProcedureExpression createProcedureExpression(String language,
							  String body)
    {
        MProcedureExpression expression =
	    new MProcedureExpression(language, body);
	super.initialize(expression);
	return expression;
    }

    /** Create an empty but initialized instance of a UML TimeExpression.
     *  
     *  @return an initialized UML TimeExpression instance.
     */
    public MTimeExpression createTimeExpression(String language, String body) {
        MTimeExpression expression = new MTimeExpression(language, body);
	super.initialize(expression);
	return expression;
    }

    /** Create an empty but initialized instance of a UML TypeExpression.
     *  
     *  @return an initialized UML TypeExpression instance.
     */
    public MTypeExpression createTypeExpression(String language, String body) {
        MTypeExpression expression = new MTypeExpression(language, body);
	super.initialize(expression);
	return expression;
    }

    /** Create an empty but initialized instance of a UML Multiplicity.
     *  
     *  @return an initialized UML Multiplicity instance.
     */
    public MMultiplicity createMultiplicity(int lower, int upper) {
        MMultiplicity multiplicity = new MMultiplicity(lower, upper);
	super.initialize(multiplicity);
	return multiplicity;
    }

    /** Create an empty but initialized instance of a UML Multiplicity.
     *  
     *  @return an initialized UML Multiplicity instance.
     */
    public MMultiplicity createMultiplicity(List range) {
        MMultiplicity multiplicity = new MMultiplicity(range);
	super.initialize(multiplicity);
	return multiplicity;
    }

    /** Create an empty but initialized instance of a UML Multiplicity.
     *  
     *  @return an initialized UML Multiplicity instance.
     */
    public MMultiplicity createMultiplicity(String str) {
        MMultiplicity multiplicity = new MMultiplicity(str);
	super.initialize(multiplicity);
	return multiplicity;
    }


}

