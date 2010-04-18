// $Id$
/*******************************************************************************
 * Copyright (c) 2007,2010 Tom Morris and other contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Morris - initial implementation
 *******************************************************************************/
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
                    + ".." //$NON-NLS-1$
                    + DataTypesFactoryEUMLImpl.boundToString(mult.getUpper());
        }
    }
    
    public Object setBody(Object handle, String body) {
        List<String> bodies = ((OpaqueExpression) handle).getBodies();
        // TODO: Support more than one body/language
        if (bodies.size() > 1) {
            throw new IllegalStateException("Only one body/lang supported"); //$NON-NLS-1$
        }
        bodies.clear();
        bodies.add(body);
        return handle;
    }

    public Object setLanguage(Object handle, String language) {
        List<String> langs = ((OpaqueExpression) handle).getLanguages();
        // TODO: Support more than one body/language
        if (langs.size() > 1) {
            throw new IllegalStateException("Only one body/lang supported"); //$NON-NLS-1$
        }
        langs.clear(); 
        langs.add(language);
        return handle;
    }

}
