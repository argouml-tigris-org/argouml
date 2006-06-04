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
     * Create a new set of project settings, 
     * based on the application defaults. <p>
     * 
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
     * Used by "argo.tee".
     * 
     * @return Returns "true" if we allow notations.
     */
    public String getAllowNotations() {
        return Boolean.toString(allowNotations);
    }

    /**
     * @return Returns <code>true</code> if we allow notations.
     */
    public boolean getAllowNotationsValue() {
        return allowNotations;
    }

    /**
     * @param allowem <code>true</code> if notations are to be allowed.
     */
    public void setAllowNotations(String allowem) {
        setAllowNotations(Boolean.valueOf(allowem).booleanValue());
    }

    /**
     * @param allowem <code>true</code> if properties are to be shown.
     */
    public void setAllowNotations(boolean allowem) {
        boolean oldValue = allowNotations;
        allowNotations = allowem;
        fireEvent(Notation.KEY_UML_NOTATION_ONLY, oldValue, allowNotations);
    }


    /**
     * Used by "argo.tee".
     * 
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
     * @param language the notation language.
     */
    public void setNotationLanguage(String language) {
        String oldValue = notationLanguage;
        notationLanguage = language;
        fireEvent(Notation.KEY_DEFAULT_NOTATION, oldValue, notationLanguage);
    }

    /**
     * Used by "argo.tee".
     * 
     * @return Returns "true" if we show guillemots.
     */
    public String getUseGuillemots() {
        return Boolean.toString(useGuillemots);
    }

    /**
     * @return Returns <code>true</code> if we show guillemots.
     */
    public boolean getUseGuillemotsValue() {
        return useGuillemots;
    }

    /**
     * @param showem <code>true</code> if guillemots are to be shown.
     */
    public void setUseGuillemots(String showem) {
        setUseGuillemots(Boolean.valueOf(showem).booleanValue());
    }

    /**
     * @param showem <code>true</code> if guillemots are to be shown.
     */

    public void setUseGuillemots(boolean showem) {
        boolean oldValue = useGuillemots;
        useGuillemots = showem;
        fireEvent(Notation.KEY_USE_GUILLEMOTS, oldValue, useGuillemots);
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
     * Used by "argo.tee".
     * 
     * @return Returns "true" if we show visibilities.
     */
    public String getShowVisibility() {
        return Boolean.toString(showVisibility);
    }

    /**
     * @return Returns <code>true</code> if we show visibilities.
     */
    public boolean getShowVisibilityValue() {
        return showVisibility;
    }

    /**
     * @param showem <code>true</code> if visibilities are to be shown.
     */
    public void setShowVisibility(String showem) {
        setShowVisibility(Boolean.valueOf(showem).booleanValue());
    }

    /**
     * @param showem <code>true</code> if visibilities are to be shown.
     */
    public void setShowVisibility(boolean showem) {
        boolean oldValue = showVisibility;
        showVisibility = showem;
        fireEvent(Notation.KEY_SHOW_VISIBILITY, oldValue, showVisibility);
    }

    /**
     * Used by "argo.tee".
     * 
     * @return Returns "true" if we show multiplicities.
     */
    public String getShowMultiplicity() {
        return Boolean.toString(showMultiplicity);
    }

    /**
     * @return Returns <code>true</code> if we show multiplicities.
     */
    public boolean getShowMultiplicityValue() {
        return showMultiplicity;
    }

    /**
     * @param showem <code>true</code> if multiplicity is to be shown.
     */
    public void setShowMultiplicity(String showem) {
        setShowMultiplicity(Boolean.valueOf(showem).booleanValue());
    }

    /**
     * @param showem <code>true</code> if the multiplicity is to be shown.
     */
    public void setShowMultiplicity(boolean showem) {
        boolean oldValue = showMultiplicity;
        showMultiplicity = showem;
        fireEvent(Notation.KEY_SHOW_MULTIPLICITY, oldValue, showMultiplicity);
    }

    /**
     * Used by "argo.tee".
     * 
     * @return Returns "true" if we show initial values.
     */
    public String getShowInitialValue() {
        return Boolean.toString(showInitialValue);
    }

    /**
     * @return Returns <code>true</code> if we show initial values.
     */
    public boolean getShowInitialValueValue() {
        return showInitialValue;
    }

    /**
     * @param showem <code>true</code> if initial values are to be shown.
     */
    public void setShowInitialValue(String showem) {
        setShowInitialValue(Boolean.valueOf(showem).booleanValue());
    }

    /**
     * @param showem <code>true</code> if initial values are to be shown.
     */
    public void setShowInitialValue(boolean showem) {
        boolean oldValue = showInitialValue;
        showInitialValue = showem;
        fireEvent(Notation.KEY_SHOW_INITIAL_VALUE, oldValue, showInitialValue);
    }

    /**
     * Used by "argo.tee".
     * 
     * @return Returns "true" if we show properties.
     */
    public String getShowProperties() {
        return Boolean.toString(showProperties);
    }

    /**
     * @return Returns <code>true</code> if we show properties.
     */
    public boolean getShowPropertiesValue() {
        return showProperties;
    }

    /**
     * @param showem <code>true</code> if properties are to be shown.
     */
    public void setShowProperties(String showem) {
        setShowProperties(Boolean.valueOf(showem).booleanValue());
    }

    /**
     * @param showem <code>true</code> if properties are to be shown.
     */
    public void setShowProperties(boolean showem) {
        boolean oldValue = showProperties;
        showProperties = showem;
        fireEvent(Notation.KEY_SHOW_PROPERTIES, oldValue, showProperties);
    }

    /**
     * Used by "argo.tee".
     * 
     * @return Returns "true" if we show types.
     */
    public String getShowTypes() {
        return Boolean.toString(showTypes);
    }

    /**
     * @return Returns <code>true</code> if we show types.
     */
    public boolean getShowTypesValue() {
        return showTypes;
    }

    /**
     * @param showem <code>true</code> if types are to be shown.
     */
    public void setShowTypes(String showem) {
        setShowTypes(Boolean.valueOf(showem).booleanValue());
    }

    /**
     * @param showem <code>true</code> if types are to be shown.
     */
    public void setShowTypes(boolean showem) {
        boolean oldValue = showTypes;
        showTypes = showem;
        fireEvent(Notation.KEY_SHOW_TYPES, oldValue, showTypes);
    }


    /**
     * Used by "argo.tee".
     * 
     * @return Returns "true" if we show stereotypes.
     */
    public String getShowStereotypes() {
        return Boolean.toString(showStereotypes);
    }

    /**
     * @return Returns <code>true</code> if we show stereotypes.
     */
    public boolean getShowStereotypesValue() {
        return showStereotypes;
    }

    /**
     * @param showem <code>true</code> if stereotypes are to be shown.
     */
    public void setShowStereotypes(String showem) {
        setShowStereotypes(Boolean.valueOf(showem).booleanValue());
    }

    /**
     * @param showem <code>true</code> if stereotypes are to be shown.
     */
    public void setShowStereotypes(boolean showem) {
        boolean oldValue = showStereotypes;
        showStereotypes = showem;
        fireEvent(Notation.KEY_SHOW_STEREOTYPES, oldValue, showStereotypes);
    }

    /**
     * Used by "argo.tee".
     * 
     * @return Returns the shadow width.
     */
    public String getDefaultShadowWidth() {
        return new Integer(defaultShadowWidth).toString();
    }

    /**
     * @return Returns the shadow width.
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
     * @param width The shadow width to set.
     */
    public void setDefaultShadowWidth(String width) {
        setDefaultShadowWidth(Integer.parseInt(width));
    }
    

    /**
     * Convenience methods to fire notation configuration change events.
     *
     * @param key the ConfigurationKey that is related to the change
     * @param oldValue the old value
     * @param newValue the new value
     */
    private void fireEvent(ConfigurationKey key, int oldValue, int newValue) {
        fireEvent(key, Integer.toString(oldValue), Integer.toString(newValue));
    }

    /**
     * Convenience methods to fire notation configuration change events.
     *
     * @param key the ConfigurationKey that is related to the change
     * @param oldValue the old value
     * @param newValue the new value
     */
    private void fireEvent(ConfigurationKey key, boolean oldValue,
            boolean newValue) {
        fireEvent(key, Boolean.toString(oldValue), Boolean.toString(newValue));
    }
    
    /**
     * Convenience methods to fire notation configuration change events.
     *
     * @param key the ConfigurationKey that is related to the change
     * @param oldValue the old value
     * @param newValue the new value
     */
    private void fireEvent(ConfigurationKey key, String oldValue,
            String newValue) {
        ArgoEventPump.fireEvent(new ArgoNotationEvent(
                ArgoEventTypes.NOTATION_CHANGED, new PropertyChangeEvent(this,
                        key.getKey(), oldValue, newValue)));
    }
}
