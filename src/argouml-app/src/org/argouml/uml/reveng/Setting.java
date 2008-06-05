// $Id$
// Copyright (c) 2006-2008 The Regents of the University of California. All
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

import java.util.List;


/**
 * Common class that all settings types inherit from.  It provides
 * a label to be associated with the setting in the user interface.
 */
public class Setting implements SettingsTypes.Setting2 {

    /**
     * The message of the Label.
     */
    private String label;

    private String description;
    
    /**
     * Construct a new Setting with the given label text.
     * 
     * @param labelText string to use as the label
     */
    public Setting(String labelText) {
        super();
        label = labelText;
    }
    
    /**
     * Construct a new Setting with the given label text and description.
     * 
     * @param labelText string to use as the label
     * @param descriptionText string to use as description
     */
    public Setting(String labelText, String descriptionText) {
        this(labelText);
        description = descriptionText;
    }


    public final String getLabel() {
        return label;
    }
    
    public String getDescription() {
        return description;
    }

    /**
     * Setting which specifies a boolean value.  Typical user presentation
     * would be labeled checkbox.
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
    
    public static class UniqueSelection extends Setting implements
            SettingsTypes.UniqueSelection {

        private List<String> options;

        private int selection;

        public UniqueSelection(String labelText, List<String> optionLabels,
                int defaultSelection) {
            super(labelText);
            options = optionLabels;
            this.selection = selection;
        }

        public int getDefaultSelection() {
            return selection;
        }

        public List<String> getOptions() {
            return options;
        }

        public boolean setSelection(int selection) {
            this.selection = selection;
            return true;
        }
    }

    public static class PathSelection extends Setting implements
            SettingsTypes.PathSelection {

        private String path;

        private String defaultPath;

        public PathSelection(String labelText, String descriptionText,
                String defaultPath) {
            super(labelText, descriptionText);
            this.defaultPath = defaultPath;
            path = defaultPath;
        }

        public String getDefaultPath() {
            return defaultPath;
        }

        public String getPath() {
            return path;
        }

        /**
         * Set the path selection so that it is available to the importer.
         * 
         * @param newPath string representing the new path
         */
        public void setPath(String newPath) {
            path = newPath;
        }

    }

    public static class PathListSelection extends Setting implements
            SettingsTypes.PathListSelection {

        private List<String> defaultPathList;

        private List<String> pathList;

        public PathListSelection(String labelText, String descriptionText,
                List<String> defaultPathList) {
            super(labelText, descriptionText);
            this.defaultPathList = defaultPathList;
            pathList = defaultPathList;
        }

        public List<String> getDefaultPathList() {
            return defaultPathList;
        }

        public List<String> getPathList() {
            return pathList;
        }

    }

}