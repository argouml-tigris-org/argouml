// $Id:Setting.java 11595 2006-12-14 16:04:01Z linus $
// Copyright (c) 2006 The Regents of the University of California. All
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

package org.argouml.uml.reveng;


/**
 * Common class that all settings types inherit from.  It provides
 * a label to be associated with the setting in the user interface.
 */
public class Setting implements SettingsTypes.Setting {

    /**
     * The message of the Label.
     */
    private String label;

    public Setting(String labelText) {
        super();
        label = labelText;
    }

    /* 
     * @see org.argouml.uml.reveng.SettingsTypes.Setting#getLabel()
     */
    public final String getLabel() {
        return label;
    }

    /**
     * Setting which specifies a boolean value.  Typical user presentation
     * would be labelled checkbox.
     */
    public static class BooleanSelection extends Setting
        implements SettingsTypes.BooleanSelection {

        private boolean defaultValue;
        private boolean value;


        /**
         * Construct a new setting object which specifies a boolean selection.
         *
         * @param labelText the string to use for the user visible label
         * @param initialValue the default value (true or false)
         */
        public BooleanSelection(String labelText, boolean initialValue) {
            super(labelText);
            this.defaultValue = initialValue;
            value = initialValue;
        }

        /*
         * @see org.argouml.uml.reveng.SettingsTypes.BooleanSelection#isSelected()
         */
        public final boolean isSelected() {
            return value;
        }

        /*
         * @see org.argouml.uml.reveng.SettingsTypes.BooleanSelection#getDefaultValue()
         */
        public final boolean getDefaultValue() {
            return defaultValue;
        }
    }
}