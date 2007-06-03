// $Id:DataTypesFactoryEUMLImpl.java 12721 2007-05-30 18:14:55Z tfmorris $
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

import org.argouml.model.DataTypesFactory;

/**
 * The implementation of the DataTypesFactory for EUML2.
 */
class DataTypesFactoryEUMLImpl implements DataTypesFactory {

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
        // TODO Auto-generated method stub
        return null;
    }

    public Object createArgListsExpression(String language, String body) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createBooleanExpression(String language, String body) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createExpression(String language, String body) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createIterationExpression(String language, String body) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createMappingExpression(String language, String body) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createMultiplicity(int lower, int upper) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createMultiplicity(List range) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createMultiplicity(String str) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createMultiplicityRange(String str) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createMultiplicityRange(int lower, int upper) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createObjectSetExpression(String language, String body) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createProcedureExpression(String language, String body) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createTimeExpression(String language, String body) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createTypeExpression(String language, String body) {
        // TODO Auto-generated method stub
        return null;
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
