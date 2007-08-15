// $Id$
// Copyright (c) 2007, The ArgoUML Project
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//     * Redistributions of source code must retain the above copyright
//       notice, this list of conditions and the following disclaimer.
//     * Redistributions in binary form must reproduce the above copyright
//       notice, this list of conditions and the following disclaimer in the
//       documentation and/or other materials provided with the distribution.
//     * Neither the name of the ArgoUML Project nor the
//       names of its contributors may be used to endorse or promote products
//       derived from this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE ArgoUML PROJECT ``AS IS'' AND ANY
// EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
// DISCLAIMED. IN NO EVENT SHALL THE ArgoUML PROJECT BE LIABLE FOR ANY
// DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
// LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
// ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
// SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package org.argouml.model.euml;

import java.util.List;

import org.argouml.model.AbstractModelFactory;
import org.argouml.model.DataTypesFactory;
import org.argouml.model.NotImplementedException;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.UMLFactory;

/**
 * The implementation of the DataTypesFactory for EUML2.
 */
class DataTypesFactoryEUMLImpl implements DataTypesFactory,
        AbstractModelFactory {

    /**
     * The model implementation.
     */
    private EUMLModelImplementation modelImpl;

    /**
     * Constructor.
     *
     * @param implementation The ModelImplementation.
     */
    public DataTypesFactoryEUMLImpl(EUMLModelImplementation implementation) {
        modelImpl = implementation;
    }

    public Object createActionExpression(String language, String body) {
        return createExpression(language, body);
    }

    public Object createArgListsExpression(String language, String body) {
        return createExpression(language, body);
    }

    public Object createBooleanExpression(String language, String body) {
        return createExpression(language, body);
    }

    public OpaqueExpression createExpression(String language, String body) {
        // TODO: We can choose between something which matches UML 1.4 in name
        // or something that matches in functionality.  We've chosen
        // functionality for now, but this will create a name conflict during
        // the migration process. - tfm
        OpaqueExpression expression =
                UMLFactory.eINSTANCE.createOpaqueExpression();
        expression.getLanguages().add(language);
        expression.getBodies().add(body);
        return expression;
    }

    public Object createIterationExpression(String language, String body) {
        return createExpression(language, body);
    }

    public Object createMappingExpression(String language, String body) {
        return createExpression(language, body);
    }

    
    // TODO: Callers will need to be refactored to work around the
    // change in the way multiplicities work - tfm
    
    public Object createMultiplicity(int lower, int upper) {
        throw new NotImplementedException();
    }

    public Object createMultiplicity(List range) {
        throw new NotImplementedException();
    }

    public Object createMultiplicity(String str) {
        throw new NotImplementedException();
    }

    public Object createMultiplicityRange(String str) {
        throw new NotImplementedException();
    }

    public Object createMultiplicityRange(int lower, int upper) {
        throw new NotImplementedException();
    }

    public Object createObjectSetExpression(String language, String body) {
        return createExpression(language, body);
    }

    public Object createProcedureExpression(String language, String body) {
        return createExpression(language, body);
    }

    public Object createTimeExpression(String language, String body) {
        return createExpression(language, body);
    }

    public Object createTypeExpression(String language, String body) {
        return createExpression(language, body);
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
            return Integer.toString(i);
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
            throw new IllegalArgumentException("illegal range bound : " + b);
        }
    }

}
