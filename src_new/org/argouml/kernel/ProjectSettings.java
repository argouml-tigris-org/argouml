// $Id$
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

package org.argouml.kernel;

import java.beans.PropertyChangeEvent;

import org.argouml.application.api.Configuration;
import org.argouml.application.api.ConfigurationKey;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoNotationEvent;
import org.argouml.notation.Notation;
import org.argouml.notation.NotationName;

/**
 * A datastructure for settings for a Project. <p>
 * 
 * Most getters return a string, since they are used by "argo.tee".
 * This is also the reason all these attributes 
 * are not part of a Map or something.
 *
 * @author michiel
 */
public class ProjectSettings {

    /* The notation settings with project scope: */
    private boolean allowNotations;
    private String notationLanguage;
    private boolean useGuillemots;
    private boolean showVisibility;
    private boolean showMultiplicity;
    private boolean showInitialValue;
    private boolean showProperties;
    private boolean showTypes;
    private boolean showStereotypes;
    private int defaultShadowWidth;


    /**
     * The constructor is not public, since this 
     * class is only created from the Project..
     */
    ProjectSettings() {
        super();
        
        allowNotations = Configuration.getBoolean(
                Notation.KEY_UML_NOTATION_ONLY);
        notationLanguage = Notation.getConfigueredNotation().getName();
        useGuillemots = Configuration.getBoolean(
                Notation.KEY_USE_GUILLEMOTS, false);
        showVisibility = Configuration.getBoolean(
                Notation.KEY_SHOW_VISIBILITY);
        showMultiplicity = Configuration.getBoolean(
                Notation.KEY_SHOW_MULTIPLICITY);
        showInitialValue = Configuration.getBoolean(
                Notation.KEY_SHOW_INITIAL_VALUE);
        showProperties = Configuration.getBoolean(
                Notation.KEY_SHOW_PROPERTIES);
        /*
         * The next one defaults to TRUE, to stay compatible with older
         * ArgoUML versions that did not have this setting:
         */
        showTypes = Configuration.getBoolean(Notation.KEY_SHOW_TYPES, true);
        showStereotypes = Configuration.getBoolean(
                Notation.KEY_SHOW_STEREOTYPES);
        defaultShadowWidth = Configuration.getInteger(
                Notation.KEY_DEFAULT_SHADOW_WIDTH, 1);
    }


    /**
     * @return Returns true if we show properties.
     */
    public String getAllowNotations() {
        return Boolean.toString(allowNotations);
    }

    /**
     * @return Returns true if we show properties.
     */
    public boolean getAllowNotationsValue() {
        return allowNotations;
    }

    /**
     * @param allowem <code>true</code> if notations are to be allowed.
     */
    public void setAllowNotations(String allowem) {
        String oldValue = Boolean.toString(allowNotations);
        allowNotations = Boolean.valueOf(allowem).booleanValue();
        ArgoEventPump.fireEvent(
                new ArgoNotationEvent(ArgoEventTypes.NOTATION_CHANGED, 
                        new PropertyChangeEvent(this, 
                                Notation.KEY_UML_NOTATION_ONLY.getKey(), 
                                oldValue, allowem)));
    }

    /**
     * @param allowem <code>true</code> if properties are to be shown.
     */
    public void setAllowNotations(boolean allowem) {
        String oldValue = Boolean.toString(allowNotations);
        allowNotations = allowem;
        String newValue = Boolean.toString(allowNotations);
        ArgoEventPump.fireEvent(
                new ArgoNotationEvent(ArgoEventTypes.NOTATION_CHANGED, 
                        new PropertyChangeEvent(this, 
                                Notation.KEY_UML_NOTATION_ONLY.getKey(), 
                                oldValue, newValue)));
    }


    /**
     * @return Returns the notation language.
     */
    public String getNotationLanguage() {
        return notationLanguage;
    }

    /**
     * @return Returns the notation language.
     */
    public NotationName getNotationName() {
        return Notation.findNotation(notationLanguage);
    }

    /**
     * @param language <code>true</code> if notations are to be allowed.
     */
    public void setNotationLanguage(String language) {
        String oldValue = notationLanguage;
        notationLanguage = language;
        ArgoEventPump.fireEvent(
                new ArgoNotationEvent(ArgoEventTypes.NOTATION_CHANGED, 
                        new PropertyChangeEvent(this, 
                                Notation.KEY_DEFAULT_NOTATION.getKey(), 
                                oldValue, language)));
    }

    /**
     * @return Returns true if we show properties.
     */
    public String getUseGuillemots() {
        return Boolean.toString(useGuillemots);
    }

    /**
     * @return Returns true if we show properties.
     */
    public boolean getUseGuillemotsValue() {
        return useGuillemots;
    }

    /**
     * @param showem <code>true</code> if properties are to be shown.
     */
    public void setUseGuillemots(String showem) {
        String oldValue = Boolean.toString(useGuillemots);
        useGuillemots = Boolean.valueOf(showem).booleanValue();
        ArgoEventPump.fireEvent(
                new ArgoNotationEvent(ArgoEventTypes.NOTATION_CHANGED, 
                        new PropertyChangeEvent(this, 
                                Notation.KEY_USE_GUILLEMOTS.getKey(), 
                                oldValue, showem)));
    }

    /**
     * @param showem <code>true</code> if properties are to be shown.
     */

    public void setUseGuillemots(boolean showem) {
        String oldValue = Boolean.toString(useGuillemots);
        useGuillemots = showem;
        String newValue = Boolean.toString(useGuillemots);
        ArgoEventPump.fireEvent(
                new ArgoNotationEvent(ArgoEventTypes.NOTATION_CHANGED, 
                        new PropertyChangeEvent(this, 
                                Notation.KEY_USE_GUILLEMOTS.getKey(), 
                                oldValue, newValue)));
    }

    /**
     * @return the left pointing guillemot, i.e. << or the one-character symbol
     */
    public String getLeftGuillemot() {
        return useGuillemots ? "\u00ab" : "<<";
    }

    /**
     * @return the right pointing guillemot, i.e. >> or the one-character symbol
     */
    public String getRightGuillemot() {
        return useGuillemots ? "\u00bb" : ">>";
    }

    /**
     * @return Returns true if we show properties.
     */
    public String getShowVisibility() {
        return Boolean.toString(showVisibility);
    }

    /**
     * @return Returns true if we show properties.
     */
    public boolean getShowVisibilityValue() {
        return showVisibility;
    }

    /**
     * @param showem <code>true</code> if properties are to be shown.
     */
    public void setShowVisibility(String showem) {
        String oldValue = Boolean.toString(showVisibility);
        showVisibility = Boolean.valueOf(showem).booleanValue();
        ArgoEventPump.fireEvent(
                new ArgoNotationEvent(ArgoEventTypes.NOTATION_CHANGED, 
                        new PropertyChangeEvent(this, 
                                Notation.KEY_SHOW_VISIBILITY.getKey(), 
                                oldValue, showem)));
    }

    /**
     * @param showem <code>true</code> if properties are to be shown.
     */
    public void setShowVisibility(boolean showem) {
        String oldValue = Boolean.toString(showVisibility);
        showVisibility = showem;
        String newValue = Boolean.toString(showVisibility);
        ArgoEventPump.fireEvent(
                new ArgoNotationEvent(ArgoEventTypes.NOTATION_CHANGED, 
                        new PropertyChangeEvent(this, 
                                Notation.KEY_SHOW_VISIBILITY.getKey(), 
                                oldValue, newValue)));
    }

    /**
     * @return Returns true if we show properties.
     */
    public String getShowMultiplicity() {
        return Boolean.toString(showMultiplicity);
    }

    /**
     * @return Returns true if we show properties.
     */
    public boolean getShowMultiplicityValue() {
        return showMultiplicity;
    }

    /**
     * @param showem <code>true</code> if properties are to be shown.
     */
    public void setShowMultiplicity(String showem) {
        String oldValue = Boolean.toString(showMultiplicity);
        showMultiplicity = Boolean.valueOf(showem).booleanValue();
        ArgoEventPump.fireEvent(
                new ArgoNotationEvent(ArgoEventTypes.NOTATION_CHANGED, 
                        new PropertyChangeEvent(this, 
                                Notation.KEY_SHOW_MULTIPLICITY.getKey(), 
                                oldValue, showem)));
    }

    /**
     * @param showem <code>true</code> if properties are to be shown.
     */
    public void setShowMultiplicity(boolean showem) {
        String oldValue = Boolean.toString(showMultiplicity);
        showMultiplicity = showem;
        String newValue = Boolean.toString(showMultiplicity);
        ArgoEventPump.fireEvent(
                new ArgoNotationEvent(ArgoEventTypes.NOTATION_CHANGED, 
                        new PropertyChangeEvent(this, 
                                Notation.KEY_SHOW_MULTIPLICITY.getKey(), 
                                oldValue, newValue)));
    }

    /**
     * @return Returns true if we show properties.
     */
    public String getShowInitialValue() {
        return Boolean.toString(showInitialValue);
    }

    /**
     * @return Returns true if we show properties.
     */
    public boolean getShowInitialValueValue() {
        return showInitialValue;
    }

    /**
     * @param showem <code>true</code> if properties are to be shown.
     */
    public void setShowInitialValue(String showem) {
        String oldValue = Boolean.toString(showInitialValue);
        showInitialValue = Boolean.valueOf(showem).booleanValue();
        ArgoEventPump.fireEvent(
                new ArgoNotationEvent(ArgoEventTypes.NOTATION_CHANGED, 
                        new PropertyChangeEvent(this, 
                                Notation.KEY_SHOW_INITIAL_VALUE.getKey(), 
                                oldValue, showem)));
    }

    /**
     * @param showem <code>true</code> if properties are to be shown.
     */
    public void setShowInitialValue(boolean showem) {
        String oldValue = Boolean.toString(showInitialValue);
        showInitialValue = showem;
        String newValue = Boolean.toString(showInitialValue);
        ArgoEventPump.fireEvent(
                new ArgoNotationEvent(ArgoEventTypes.NOTATION_CHANGED, 
                        new PropertyChangeEvent(this, 
                                Notation.KEY_SHOW_INITIAL_VALUE.getKey(), 
                                oldValue, newValue)));
    }

    /**
     * @return Returns true if we show properties.
     */
    public String getShowProperties() {
        return Boolean.toString(showProperties);
    }

    /**
     * @return Returns true if we show properties.
     */
    public boolean getShowPropertiesValue() {
        return showProperties;
    }

    /**
     * @param showem <code>true</code> if properties are to be shown.
     */
    public void setShowProperties(String showem) {
        String oldValue = Boolean.toString(showProperties);
        showProperties = Boolean.valueOf(showem).booleanValue();
        ArgoEventPump.fireEvent(
                new ArgoNotationEvent(ArgoEventTypes.NOTATION_CHANGED, 
                        new PropertyChangeEvent(this, 
                                Notation.KEY_SHOW_PROPERTIES.getKey(), 
                                oldValue, showem)));
    }

    /**
     * @param showem <code>true</code> if properties are to be shown.
     */
    public void setShowProperties(boolean showem) {
        String oldValue = Boolean.toString(showProperties);
        showProperties = showem;
        String newValue = Boolean.toString(showProperties);
        ArgoEventPump.fireEvent(
                new ArgoNotationEvent(ArgoEventTypes.NOTATION_CHANGED, 
                        new PropertyChangeEvent(this, 
                                Notation.KEY_SHOW_PROPERTIES.getKey(), 
                                oldValue, newValue)));
    }

    /**
     * @return Returns true if we show properties.
     */
    public String getShowTypes() {
        return Boolean.toString(showTypes);
    }

    /**
     * @return Returns true if we show properties.
     */
    public boolean getShowTypesValue() {
        return showTypes;
    }

    /**
     * @param showem <code>true</code> if properties are to be shown.
     */
    public void setShowTypes(String showem) {
        String oldValue = Boolean.toString(showTypes);
        showTypes = Boolean.valueOf(showem).booleanValue();
        ArgoEventPump.fireEvent(
                new ArgoNotationEvent(ArgoEventTypes.NOTATION_CHANGED, 
                        new PropertyChangeEvent(this, 
                                Notation.KEY_SHOW_TYPES.getKey(), 
                                oldValue, showem)));
    }

    /**
     * @param showem <code>true</code> if properties are to be shown.
     */
    public void setShowTypes(boolean showem) {
        String oldValue = Boolean.toString(showTypes);
        showTypes = showem;
        String newValue = Boolean.toString(showTypes);
        ArgoEventPump.fireEvent(
                new ArgoNotationEvent(ArgoEventTypes.NOTATION_CHANGED, 
                        new PropertyChangeEvent(this, 
                                Notation.KEY_SHOW_TYPES.getKey(), 
                                oldValue, newValue)));
    }


    /**
     * @return Returns true if we show properties.
     */
    public String getShowStereotypes() {
        return Boolean.toString(showStereotypes);
    }

    /**
     * @return Returns true if we show properties.
     */
    public boolean getShowStereotypesValue() {
        return showStereotypes;
    }

    /**
     * @param showem <code>true</code> if properties are to be shown.
     */
    public void setShowStereotypes(String showem) {
        setShowStereotypes(Boolean.valueOf(showem).booleanValue());
    }

    /**
     * @param showem <code>true</code> if properties are to be shown.
     */
    public void setShowStereotypes(boolean showem) {
        boolean oldValue = showStereotypes;
        showStereotypes = showem;
        fireEvent(Notation.KEY_SHOW_STEREOTYPES, oldValue, showStereotypes);
    }

    /**
     * @return Returns the defaultShadowWidth.
     */
    public String getDefaultShadowWidth() {
        return new Integer(defaultShadowWidth).toString();
    }

    /**
     * @return Returns the defaultShadowWidth.
     */
    public int getDefaultShadowWidthValue() {
        return defaultShadowWidth;
    }

    /**
     * @param width The Shadow Width.
     */
    public void setDefaultShadowWidth(int width) {
        int oldValue = width;
        defaultShadowWidth = width;
        fireEvent(Notation.KEY_DEFAULT_SHADOW_WIDTH, oldValue,
                defaultShadowWidth);
    }

    /**
     * @param width The defaultShadowWidth to set.
     */
    public void setDefaultShadowWidth(String width) {
        setDefaultShadowWidth(Integer.parseInt(width));
    }
    

    /**
     * Convenience methods to fire notation configuration change events.
     */

    private void fireEvent(ConfigurationKey key, int oldValue, int newValue) {
        fireEvent(key, Integer.toString(oldValue), Integer.toString(newValue));
    }

    private void fireEvent(ConfigurationKey key, boolean oldValue,
            boolean newValue) {
        fireEvent(key, Boolean.toString(oldValue), Boolean.toString(newValue));
    }
    
    private void fireEvent(ConfigurationKey key, String oldValue,
            String newValue) {
        ArgoEventPump.fireEvent(new ArgoNotationEvent(
                ArgoEventTypes.NOTATION_CHANGED, new PropertyChangeEvent(this,
                        key.getKey(), oldValue, newValue)));
    }
}
