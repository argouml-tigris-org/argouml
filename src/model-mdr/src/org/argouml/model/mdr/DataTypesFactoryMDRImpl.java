// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.model.mdr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import org.argouml.model.DataTypesFactory;
import org.omg.uml.foundation.datatypes.ActionExpression;
import org.omg.uml.foundation.datatypes.ArgListsExpression;
import org.omg.uml.foundation.datatypes.BooleanExpression;
import org.omg.uml.foundation.datatypes.Expression;
import org.omg.uml.foundation.datatypes.IterationExpression;
import org.omg.uml.foundation.datatypes.MappingExpression;
import org.omg.uml.foundation.datatypes.Multiplicity;
import org.omg.uml.foundation.datatypes.MultiplicityRange;
import org.omg.uml.foundation.datatypes.ObjectSetExpression;
import org.omg.uml.foundation.datatypes.ProcedureExpression;
import org.omg.uml.foundation.datatypes.TimeExpression;
import org.omg.uml.foundation.datatypes.TypeExpression;

/**
 * Factory to create UML classes for the UML DataTypes package.
 * <p>
 * @since ARGO0.19.5
 * @author Ludovic Ma&icirc;tre
 * @author Tom Morris
 * derived from NSUML implementation by:
 * @author Thierry Lach
 */
class DataTypesFactoryMDRImpl extends AbstractUmlModelFactoryMDR
        implements DataTypesFactory {

    /**
     * The model implementation.
     */
    private MDRModelImplementation modelImpl;

    /**
     * Constructor.
     * 
     * @param implementation
     *            To get other helpers and factories.
     */
    DataTypesFactoryMDRImpl(MDRModelImplementation implementation) {
        this.modelImpl = implementation;
    }


    public ActionExpression createActionExpression(String language, String body) {
        ActionExpression myActionExpression = modelImpl.getUmlPackage()
                .getDataTypes().getActionExpression()
                .createActionExpression(language, body);
        super.initialize(myActionExpression);
        return myActionExpression;
    }


    public ArgListsExpression createArgListsExpression(String language, String body) {
        ArgListsExpression myArgListsExpression = modelImpl.getUmlPackage()
                .getDataTypes().getArgListsExpression()
                .createArgListsExpression(language, body);
        super.initialize(myArgListsExpression);
        return myArgListsExpression;
    }


    public BooleanExpression createBooleanExpression(String language, String body) {
        BooleanExpression myBooleanExpression = modelImpl.getUmlPackage()
                .getDataTypes().getBooleanExpression()
                .createBooleanExpression(language, body);
        super.initialize(myBooleanExpression);
        return myBooleanExpression;
    }


    public Expression createExpression(String language, String body) {
        Expression myExpression = modelImpl.getUmlPackage().getDataTypes()
                .getExpression().createExpression(language, body);
        super.initialize(myExpression);
        return myExpression;
    }


    public IterationExpression createIterationExpression(String language, String body) {
        IterationExpression myIterationExpression = modelImpl.getUmlPackage()
                .getDataTypes().getIterationExpression()
                .createIterationExpression(language, body);
        super.initialize(myIterationExpression);
        return myIterationExpression;
    }


    public MappingExpression createMappingExpression(String language, String body) {
        MappingExpression myMappingExpression = modelImpl.getUmlPackage()
                .getDataTypes().getMappingExpression().createMappingExpression(
                        language, body);
        super.initialize(myMappingExpression);
        return myMappingExpression;
    }


    public ObjectSetExpression createObjectSetExpression(String language, String body) {
        ObjectSetExpression myObjectSetExpression = modelImpl.getUmlPackage()
                .getDataTypes().getObjectSetExpression()
                .createObjectSetExpression(language, body);
        super.initialize(myObjectSetExpression);
        return myObjectSetExpression;
    }


    public ProcedureExpression createProcedureExpression(String language, String body) {
        ProcedureExpression myProcedureExpression = modelImpl.getUmlPackage()
                .getDataTypes().getProcedureExpression()
                .createProcedureExpression(language, body);
        super.initialize(myProcedureExpression);
        return myProcedureExpression;
    }


    public TimeExpression createTimeExpression(String language, String body) {
        TimeExpression myTimeExpression = modelImpl.getUmlPackage()
                .getDataTypes().getTimeExpression()
                .createTimeExpression(language, body);
        super.initialize(myTimeExpression);
        return myTimeExpression;
    }


    public TypeExpression createTypeExpression(String language, String body) {
        TypeExpression myTypeExpression = modelImpl.getUmlPackage()
                .getDataTypes().getTypeExpression()
                .createTypeExpression(language, body);
        super.initialize(myTypeExpression);
        return myTypeExpression;
    }


    public Multiplicity createMultiplicity(int lower, int upper) {
        Multiplicity multiplicity = modelImpl.getUmlPackage().getDataTypes()
                .getMultiplicity().createMultiplicity();
        multiplicity.getRange().add(createMultiplicityRange(lower, upper));
        super.initialize(multiplicity);
        return multiplicity;
    }

    /*
     * TODO: As currently implemented, this expects a list of
     * MultiplicityRanges. Is this an interface change from the NSUML
     * implementation? I suspect it used to accept a list of Integers. - tfm
     * 
     * @see org.argouml.model.DataTypesFactory#createMultiplicity(java.util.List)
     */
    public Multiplicity createMultiplicity(List range) {
        Multiplicity multiplicity = modelImpl.getUmlPackage().getDataTypes()
                .getMultiplicity().createMultiplicity();
        multiplicity.getRange().addAll(range);
        super.initialize(multiplicity);
        return multiplicity;
    }


    public Multiplicity createMultiplicity(String str) {
        List<MultiplicityRange> ranges = 
            Collections.unmodifiableList(parseRanges(str));
        return createMultiplicity(ranges);
    }

    private List<MultiplicityRange> parseRanges(String str) {
        List<MultiplicityRange> rc = new ArrayList<MultiplicityRange>();
        // Return 1..1 multiplicity for empty string
        if ("".equals(str)) {
            rc.add(createMultiplicityRange("1..1"));
            return rc;
        }
        StringTokenizer stk = new StringTokenizer(str, ",");
        while (stk.hasMoreTokens()) {
            rc.add(createMultiplicityRange(stk.nextToken()));
        }
        return rc;
    }

    
    public MultiplicityRange createMultiplicityRange(String str) {
        StringTokenizer stk = new StringTokenizer(str, ". ");
        if (!stk.hasMoreTokens()) {
            throw new IllegalArgumentException("empty multiplicity range");
        }
        int lower = stringToBound(stk.nextToken());
        int upper = 0;
        if (!stk.hasMoreTokens()) {
            upper = lower;
            // Convert "*" to "0..*" instead of "*..*"
            if (lower == -1) {
                lower = 0;
            }
        } else {
            upper = stringToBound(stk.nextToken());
            if (stk.hasMoreTokens()) {
                throw new IllegalArgumentException(
                        "illegal range specification");
            }
        }
        return createMultiplicityRange(lower, upper);
    }
    

    public MultiplicityRange createMultiplicityRange(int lower, int upper) {
        MultiplicityRange range = 
            modelImpl.getUmlPackage().getDataTypes().getMultiplicityRange()
                .createMultiplicityRange(lower, upper);
        return range;
    }

    /**
     * Convert an integer to a string using MultiplicityRange notation.
     * 
     * @param i integer to convert
     * @return String version of integer or "*" for unlimited (-1)
     */
    static String boundToString(int i) {
        if (i == -1) {
            return "*";
        } else {
            return "" + i;
        }
    }

    /**
     * Convert a MultiplicityRange bound string to an integer.
     * 
     * @param b String containing a single MultiplicityRange bound
     * @return integer representation
     */
    private static int stringToBound(String b) {
        try {
            if (b.equals("n") || b.equals("*")) {
                return -1;
            } else {
                return Integer.parseInt(b);
            }
        } catch (Exception ex) {
            throw new IllegalArgumentException("illegal range bound : "
                    + (b == null ? "null" : b));
        }
    }
}
