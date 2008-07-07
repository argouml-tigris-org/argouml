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

import java.util.Collections;
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
        implements SettingsTypes.BooleanSelection2 {

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
        
        public final void setSelected(boolean selected) {
            this.value = selected;
        }
    }
    
    /**
     * A setting that allows a single selection from a list of choices.
     * 
     * @see SettingsTypes.UniqueSelection2
     * @author Bogdan Pistol
     * 
     */
    public static class UniqueSelection extends Setting implements
            SettingsTypes.UniqueSelection2 {

        /**
         * The list of String options
         */
        private List<String> options;
        
        /**
         * Default selection is UNDEFINED
         */
        private int defaultSelection = UNDEFINED_SELECTION;
        
        /**
         * The selection is UNDEFINED
         */
        private int selection = UNDEFINED_SELECTION;
        
        /**
         * Constructor
         * 
         * @param label the user visible string to associate with this setting
         * @param variants
         *            the list of String options
         * @param defaultVariant
         *            the default selection or UNDEFINED_SELECTION
         */
        public UniqueSelection(String label, List<String> variants,
                int defaultVariant) {
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

        /*
         * We return a new List with the options instead of the options themself
         * because we don't want the user to be able to change the options.
         * 
         * @see org.argouml.uml.reveng.SettingsTypes.UniqueSelection#getOptions()
         */
        public List<String> getOptions() {
            return Collections.unmodifiableList(options);
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
         * @return the 0-based index of the selected option or the default
         *         option if no other option was selected
         */
        public int getSelection() {
            if (selection == UNDEFINED_SELECTION) {
                return defaultSelection;
            } else {
                return selection;
            }
        }

    }

    /**
     * A selection for a single path (e.g. file system path or directory).
     */
    public static class PathSelection extends Setting implements
            SettingsTypes.PathSelection {

        private String path;

        private String defaultPath;

        /**
         * Construct a PathSelection with the given attributes.
         * 
         * @param labelText string to use for the label of the path list
         * @param descriptionText longer description of the purpose of this
         *                pathlist (appropriate for a tooltip)
         * @param defaultValue initial value of the path 
         */
        public PathSelection(String labelText, String descriptionText,
                String defaultValue) {
            super(labelText, descriptionText);
            defaultPath = defaultValue;
            path = defaultValue;
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

    /**
     * An implementation of the PathListSelection.
     */
    public static class PathListSelection extends Setting implements
            SettingsTypes.PathListSelection {

        private List<String> defaultPathList;

        private List<String> pathList;

        /**
         * Construct a new PathListSelection with the given attributes.
         * 
         * @param labelText string to use for the label of the path list
         * @param descriptionText longer description of the purpose of this
         *                pathlist (appropriate for a tooltip)
         * @param defaultList inital values of the path list
         */
        public PathListSelection(String labelText, String descriptionText,
                List<String> defaultList) {
            super(labelText, descriptionText);
            defaultPathList = defaultList;
            pathList = defaultList;
        }

        public List<String> getDefaultPathList() {
            return defaultPathList;
        }

        public List<String> getPathList() {
            return pathList;
        }

        public void setPathList(List<String> newPathList) {
            pathList = newPathList;
        }

    }

}