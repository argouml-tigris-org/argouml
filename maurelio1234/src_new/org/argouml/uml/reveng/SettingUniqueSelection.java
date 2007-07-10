// $Id$
// Copyright (c) 2006 The Regents of the University of California. All
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


/**
 * Implementation of the {@link SettingsTypes.UniqueSelection}.
 * 
 * @see SettingsTypes.UniqueSelection
 * @author Bogdan Pistol
 */
class SettingUniqueSelection extends Setting implements
        SettingsTypes.UniqueSelection {

    /**
     * The list of String options
     */
    private List options;
    
    /**
     * Default selection is UNDEFINED
     */
    private int defaultSelection = UNDEFINED_SELECTION;
    
    /**
     * The selection is UNDEFINED
     */
    private int selection = UNDEFINED_SELECTION;
    
    /**
     * Constructor (package access)
     * 
     * @param label the user visible string to associate with this setting
     * @param variants
     *            the list of String options
     * @param defaultVariant
     *            the default selection or UNDEFINED_SELECTION
     */
    SettingUniqueSelection(String label, List variants, int defaultVariant) {
        super(label);
        options = variants;
        if (isOption(defaultVariant)) {
            defaultSelection = defaultVariant;
        }
    }

    /**
     * Tests if this is a valid option.
     * 
     * @param opt
     *            the option to test
     * @return true if it's OK and false otherwise
     */
    private boolean isOption(int opt) {
        if (options == null) {
            return false;
        }
        return opt >= 0 && opt < options.size() ? true : false;        
    }

    /*
     * @see org.argouml.uml.reveng.ImportSettingTypes.UniqueSelection#getDefaultSelection()
     */
    public int getDefaultSelection() {
        return defaultSelection;
    }

    /**
     * We return a new List with the options instead of the options themself
     * because we don't want the user to be able to change the options.
     * 
     * @see org.argouml.uml.reveng.SettingsTypes.UniqueSelection#getOptions()
     */
    public List getOptions() {
        return new ArrayList(options);
    }

    /*
     * @see org.argouml.uml.reveng.ImportSettingTypes.UniqueSelection#setSelection(int)
     */
    public boolean setSelection(int sel) {
        if (isOption(sel)) {
            selection = sel;
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * This method (package access) determines the selected option.
     * 
     * @return the 0-based index of the selected option or the default option if
     *         no other option was selected
     */
    int getSelection() {
        if (selection == UNDEFINED_SELECTION) {
            return defaultSelection;
        } else {
            return selection;
        }
    }

}
