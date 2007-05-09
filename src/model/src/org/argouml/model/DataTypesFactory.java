// $Id$
// Copyright (c) 2005-2006 The Regents of the University of California. All
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

package org.argouml.model;

import java.util.List;


/**
 * The interface for the factory for DataTypes.
 */
public interface DataTypesFactory extends Factory {
    /**
     * Create an initialized instance of a UML ActionExpression.
     *
     * @param language the language for the expression
     * @param body the body for the expression
     * @return an initialized UML ActionExpression instance.
     */
    Object createActionExpression(String language,
            String body);

    /**
     * Create an initialized instance of a UML ArgListsExpression.
     *
     * @param language the language for the expression
     * @param body the body for the expression
     * @return an initialized UML ArgListsExpression instance.
     */
    Object createArgListsExpression(String language, String body);

    /**
     * Create an initialized instance of a UML BooleanExpression.
     *
     * @param language the language for the expression
     * @param body the body for the expression
     * @return an initialized UML BooleanExpression instance.
     */
    Object createBooleanExpression(String language, String body);

    /**
     * Create an initialized instance of a UML Expression.
     *
     * @param language the language for the expression
     * @param body the body for the expression
     * @return an initialized UML Expression instance.
     */
    Object createExpression(String language, String body);

    /**
     * Create an initialized instance of a UML IterationExpression.
     *
     * @param language the language for the expression
     * @param body the body for the expression
     * @return an initialized UML IterationExpression instance.
     */
    Object createIterationExpression(String language, String body);

    /**
     * Create an initialized instance of a UML MappingExpression.
     *
     * @param language the language for the expression
     * @param body the body for the expression
     * @return an initialized UML MappingExpression instance.
     */
    Object createMappingExpression(String language, String body);

    /**
     * Create an initialized instance of a UML ObjectSetExpression.
     *
     * @param language the language for the expression
     * @param body the body for the expression
     * @return an initialized UML ObjectSetExpression instance.
     */
    Object createObjectSetExpression(String language, String body);

    /**
     * Create an initialized instance of a UML ProcedureExpression.
     *
     * @param language the language for the expression
     * @param body the body for the expression
     * @return an initialized UML ProcedureExpression instance.
     */
    Object createProcedureExpression(String language, String body);

    /**
     * Create an initialized instance of a UML TimeExpression.
     *
     * @param language the language for the expression
     * @param body the body for the expression
     * @return an initialized UML TimeExpression instance.
     */
    Object createTimeExpression(String language, String body);

    /**
     * Create an initialized instance of a UML TypeExpression.
     *
     * @param language the language for the expression
     * @param body the body for the expression
     * @return an initialized UML TypeExpression instance.
     */
    Object createTypeExpression(String language, String body);

    /**
     * Create an initialized instance of a UML Multiplicity. Quote
     * from the standard: "In the metamodel a MultiplicityRange defines a range
     * of integers. The upper bound of the range cannot be below the lower
     * bound. The lower bound must be a nonnegative integer. The upper bound
     * must be a nonnegative integer or the special value unlimited, which
     * indicates there is no upper bound on the range."<p>
     *
     * Although the UML specification doesn't say so, the special
     * value 'unlimited' is encoded as -1 by convention.
     *
     * @param lower
     *            the lower bound of the range
     * @param upper
     *            the upper bound of the range. The integer value -1 represents
     *            the special UML value 'unlimited'
     * @return an initialized UML Multiplicity instance.
     */
    Object createMultiplicity(int lower, int upper);

    /**
     * Create an initialized instance of a UML Multiplicity.
     *
     * @param range a List containing the range
     * @return an initialized UML Multiplicity instance.
     */
    Object createMultiplicity(List range);

    /**
     * Create an initialized instance of a UML Multiplicity.
     *
     * @param str a String representing the multiplicity
     * @return an initialized UML Multiplicity instance.
     */
    Object createMultiplicity(String str);
    
    /**
     * Create a MultiplicityRange from a string.
     * 
     * @since UML 1.4
     * @param str the string definition of the range
     * @return MultiplicityRange A multiplicity range
     */
    public Object createMultiplicityRange(String str);
    
    /**
     * Create a MultiplicityRange from a pair of integers.
     * 
     * @since UML 1.4
     * @param lower
     *            the lower bound of the range
     * @param upper
     *            the upper bound of the range
     * @return MultiplictyRange A multiplicity range
     */
    public Object createMultiplicityRange(int lower, int upper);
}
