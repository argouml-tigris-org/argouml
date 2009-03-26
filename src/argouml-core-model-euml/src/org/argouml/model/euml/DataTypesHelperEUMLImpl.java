// $Id$
// Copyright (c) 2007,2009 Tom Morris and other contributors
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//     * Redistributions of source code must retain the above copyright
//       notice, this list of conditions and the following disclaimer.
//     * Redistributions in binary form must reproduce the above copyright
//       notice, this list of conditions and the following disclaimer in the
//       documentation and/or other materials provided with the distribution.
//     * Neither the name of the project or its contributors may be used 
//       to endorse or promote products derived from this software without
//       specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE CONTRIBUTORS ``AS IS'' AND ANY
// EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
// DISCLAIMED. IN NO EVENT SHALL THE CONTRIBUTORS BE LIABLE FOR ANY
// DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
// LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
// ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
// SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package org.argouml.model.euml;

import java.util.List;

import org.argouml.model.DataTypesHelper;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.MultiplicityElement;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.PseudostateKind;


/**
 * The implementation of the DataTypesHelper for EUML2.
 */
class DataTypesHelperEUMLImpl implements DataTypesHelper {

    /**
     * The model implementation.
     */
    private EUMLModelImplementation modelImpl;

    /**
     * Constructor.
     *
     * @param implementation The ModelImplementation.
     */
    public DataTypesHelperEUMLImpl(EUMLModelImplementation implementation) {
        modelImpl = implementation;
    }

    public boolean equalsCHOICEKind(Object kind) {
        return PseudostateKind.CHOICE_LITERAL.equals(kind);
    }

    public boolean equalsDeepHistoryKind(Object kind) {
        return PseudostateKind.DEEP_HISTORY_LITERAL.equals(kind);
    }

    public boolean equalsFORKKind(Object kind) {
        return PseudostateKind.FORK_LITERAL.equals(kind);
    }

    public boolean equalsINITIALKind(Object kind) {
        return PseudostateKind.INITIAL_LITERAL.equals(kind);
    }

    public boolean equalsJOINKind(Object kind) {
        return PseudostateKind.JOIN_LITERAL.equals(kind);
    }

    public boolean equalsJUNCTIONKind(Object kind) {
        return PseudostateKind.JUNCTION_LITERAL.equals(kind);
    }

    public boolean equalsShallowHistoryKind(Object kind) {
        return PseudostateKind.SHALLOW_HISTORY_LITERAL.equals(kind);
    }

    public String getBody(Object handle) {
        EList<String> bodies = ((OpaqueExpression) handle).getBodies();
        if (bodies.size() < 1) {
            return null;
        }
        return bodies.get(0);
    }

    public String getLanguage(Object handle) {
        EList<String> languages = ((OpaqueExpression) handle).getLanguages();
        if (languages.size() < 1) {
            return null;
        }
        return languages.get(0);
    }

    public String multiplicityToString(Object multiplicity) {
        if (!(multiplicity instanceof MultiplicityElement)) {
            throw new IllegalArgumentException(
                    "multiplicity must be instance of MultiplicityElement"); //$NON-NLS-1$
        }
        MultiplicityElement mult = (MultiplicityElement) multiplicity;
        if (mult.getLower() == mult.getUpper()) {
            return DataTypesFactoryEUMLImpl.boundToString(mult.getLower());
        } else {
            return DataTypesFactoryEUMLImpl.boundToString(
                    mult.getLower())
                    + ".."
                    + DataTypesFactoryEUMLImpl.boundToString(mult.getUpper());
        }
    }
    
    public Object setBody(Object handle, String body) {
        List<String> bodies = ((OpaqueExpression) handle).getBodies();
        // TODO: Support more than one body/language
        if (bodies.size() > 1) {
            throw new IllegalStateException("Only one body/lang supported");
        }
        bodies.clear();
        bodies.add(body);
        return handle;
    }

    public Object setLanguage(Object handle, String language) {
        List<String> langs = ((OpaqueExpression) handle).getLanguages();
        // TODO: Support more than one body/language
        if (langs.size() > 1) {
            throw new IllegalStateException("Only one body/lang supported");
        }
        langs.clear(); 
        langs.add(language);
        return handle;
    }

}
