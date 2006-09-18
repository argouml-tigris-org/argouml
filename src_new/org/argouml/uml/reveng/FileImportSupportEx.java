// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.uml.reveng;

import java.util.ArrayList;
import java.util.List;

import org.argouml.application.api.PluggableImportTypes;
import org.argouml.i18n.Translator;

/**
 * Extension to FileImportSupport to provide UI independent methods.
 */
public abstract class FileImportSupportEx extends FileImportSupport {

    /**
     * Specific import option
     */
    private PluggableImportTypes.UniqueSelection attributes;
    
    /**
     * Specific import option
     */
    private PluggableImportTypes.UniqueSelection arrays;
 
    /**
     * Initialize the settings.
     */
    public FileImportSupportEx() {
        List values = new ArrayList();
        values.add(Translator.localize("action.import-java-UML-attr"));
        values.add(Translator.localize("action.import-java-UML-assoc"));
        attributes = new UniqueSelectionImportSetting(values, 0);
        List values2 = new ArrayList();
        values2.add(Translator
                .localize("action.import-java-array-model-datatype"));
        values2
                .add(Translator
                        .localize("action.import-java-array-model-multi"));
        arrays = new UniqueSelectionImportSetting(values, 0);
    }

    /*
     * @see org.argouml.application.api.PluggableImport#getSpecificImportSettings()
     */
    public List getSpecificImportSettings() {
        List list = new ArrayList();
        list.add(new LabelImportSetting(Translator
                .localize("action.import-java-attr-model")));
        list.add(attributes);
        list.add(new LabelImportSetting(Translator
                .localize("action.import-java-array-model")));
        list.add(arrays);
        return list;
    }

    /**
     * Returns the selected option for arrays (a import option).
     * @return the arrays
     */
    protected int getArraysSelection() {
        return ((UniqueSelectionImportSetting) arrays).getSelection();
    }

    /**
     * Return how to import the attributes (a import option).
     * @return the attributes
     */
    protected int getAttributesSelection() {
        return ((UniqueSelectionImportSetting) attributes).getSelection();
    }

}
