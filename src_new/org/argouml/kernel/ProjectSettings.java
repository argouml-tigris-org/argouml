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
        return Boolean.valueOf(allowNotations).toString();
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
        String oldValue = Boolean.valueOf(allowNotations).toString();
        allowNotations = Boolean.valueOf(allowem).booleanValue();
        ArgoEventPump.fireEvent(
                new ArgoNotationEvent(ArgoEventTypes.NOTATION_CHANGED, 
                        new PropertyChangeEvent(this, 
                                Notation.KEY_UML_NOTATION_ONLY.getKey(), 
                                oldValue, allowem)));
    }

    /**
     * @param showproperties <code>true</code> if properties are to be shown.
     */
    public void setAllowNotations(boolean allowem) {
        String oldValue = Boolean.valueOf(allowNotations).toString();
        allowNotations = allowem;
        String newValue = Boolean.valueOf(allowNotations).toString();
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
     * @param allowem <code>true</code> if notations are to be allowed.
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
        return Boolean.valueOf(useGuillemots).toString();
    }

    /**
     * @return Returns true if we show properties.
     */
    public boolean getUseGuillemotsValue() {
        return useGuillemots;
    }

    /**
     * @param showproperties <code>true</code> if properties are to be shown.
     */
    public void setUseGuillemots(String showem) {
        String oldValue = Boolean.valueOf(useGuillemots).toString();
        useGuillemots = Boolean.valueOf(showem).booleanValue();
        ArgoEventPump.fireEvent(
                new ArgoNotationEvent(ArgoEventTypes.NOTATION_CHANGED, 
                        new PropertyChangeEvent(this, 
                                Notation.KEY_USE_GUILLEMOTS.getKey(), 
                                oldValue, showem)));
    }

    /**
     * @param showproperties <code>true</code> if properties are to be shown.
     */

    public void setUseGuillemots(boolean showem) {
        String oldValue = Boolean.valueOf(useGuillemots).toString();
        useGuillemots = showem;
        String newValue = Boolean.valueOf(useGuillemots).toString();
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
        return Boolean.valueOf(showVisibility).toString();
    }

    /**
     * @return Returns true if we show properties.
     */
    public boolean getShowVisibilityValue() {
        return showVisibility;
    }

    /**
     * @param showproperties <code>true</code> if properties are to be shown.
     */
    public void setShowVisibility(String showem) {
        String oldValue = Boolean.valueOf(showVisibility).toString();
        showVisibility = Boolean.valueOf(showem).booleanValue();
        ArgoEventPump.fireEvent(
                new ArgoNotationEvent(ArgoEventTypes.NOTATION_CHANGED, 
                        new PropertyChangeEvent(this, 
                                Notation.KEY_SHOW_VISIBILITY.getKey(), 
                                oldValue, showem)));
    }

    /**
     * @param showproperties <code>true</code> if properties are to be shown.
     */
    public void setShowVisibility(boolean showem) {
        String oldValue = Boolean.valueOf(showVisibility).toString();
        showVisibility = showem;
        String newValue = Boolean.valueOf(showVisibility).toString();
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
        return Boolean.valueOf(showMultiplicity).toString();
    }

    /**
     * @return Returns true if we show properties.
     */
    public boolean getShowMultiplicityValue() {
        return showMultiplicity;
    }

    /**
     * @param showproperties <code>true</code> if properties are to be shown.
     */
    public void setShowMultiplicity(String showem) {
        String oldValue = Boolean.valueOf(showMultiplicity).toString();
        showMultiplicity = Boolean.valueOf(showem).booleanValue();
        ArgoEventPump.fireEvent(
                new ArgoNotationEvent(ArgoEventTypes.NOTATION_CHANGED, 
                        new PropertyChangeEvent(this, 
                                Notation.KEY_SHOW_MULTIPLICITY.getKey(), 
                                oldValue, showem)));
    }

    /**
     * @param showproperties <code>true</code> if properties are to be shown.
     */
    public void setShowMultiplicity(boolean showem) {
        String oldValue = Boolean.valueOf(showMultiplicity).toString();
        showMultiplicity = showem;
        String newValue = Boolean.valueOf(showMultiplicity).toString();
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
        return Boolean.valueOf(showInitialValue).toString();
    }

    /**
     * @return Returns true if we show properties.
     */
    public boolean getShowInitialValueValue() {
        return showInitialValue;
    }

    /**
     * @param showproperties <code>true</code> if properties are to be shown.
     */
    public void setShowInitialValue(String showem) {
        String oldValue = Boolean.valueOf(showInitialValue).toString();
        showInitialValue = Boolean.valueOf(showem).booleanValue();
        ArgoEventPump.fireEvent(
                new ArgoNotationEvent(ArgoEventTypes.NOTATION_CHANGED, 
                        new PropertyChangeEvent(this, 
                                Notation.KEY_SHOW_INITIAL_VALUE.getKey(), 
                                oldValue, showem)));
    }

    /**
     * @param showproperties <code>true</code> if properties are to be shown.
     */
    public void setShowInitialValue(boolean showem) {
        String oldValue = Boolean.valueOf(showInitialValue).toString();
        showInitialValue = showem;
        String newValue = Boolean.valueOf(showInitialValue).toString();
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
        return Boolean.valueOf(showProperties).toString();
    }

    /**
     * @return Returns true if we show properties.
     */
    public boolean getShowPropertiesValue() {
        return showProperties;
    }

    /**
     * @param showproperties <code>true</code> if properties are to be shown.
     */
    public void setShowProperties(String showem) {
        String oldValue = Boolean.valueOf(showProperties).toString();
        showProperties = Boolean.valueOf(showem).booleanValue();
        ArgoEventPump.fireEvent(
                new ArgoNotationEvent(ArgoEventTypes.NOTATION_CHANGED, 
                        new PropertyChangeEvent(this, 
                                Notation.KEY_SHOW_PROPERTIES.getKey(), 
                                oldValue, showem)));
    }

    /**
     * @param showproperties <code>true</code> if properties are to be shown.
     */
    public void setShowProperties(boolean showem) {
        String oldValue = Boolean.valueOf(showProperties).toString();
        showProperties = showem;
        String newValue = Boolean.valueOf(showProperties).toString();
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
        return Boolean.valueOf(showTypes).toString();
    }

    /**
     * @return Returns true if we show properties.
     */
    public boolean getShowTypesValue() {
        return showTypes;
    }

    /**
     * @param showproperties <code>true</code> if properties are to be shown.
     */
    public void setShowTypes(String showem) {
        String oldValue = Boolean.valueOf(showTypes).toString();
        showTypes = Boolean.valueOf(showem).booleanValue();
        ArgoEventPump.fireEvent(
                new ArgoNotationEvent(ArgoEventTypes.NOTATION_CHANGED, 
                        new PropertyChangeEvent(this, 
                                Notation.KEY_SHOW_TYPES.getKey(), 
                                oldValue, showem)));
    }

    /**
     * @param showproperties <code>true</code> if properties are to be shown.
     */
    public void setShowTypes(boolean showem) {
        String oldValue = Boolean.valueOf(showTypes).toString();
        showTypes = showem;
        String newValue = Boolean.valueOf(showTypes).toString();
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
        return Boolean.valueOf(showStereotypes).toString();
    }

    /**
     * @return Returns true if we show properties.
     */
    public boolean getShowStereotypesValue() {
        return showStereotypes;
    }

    /**
     * @param showproperties <code>true</code> if properties are to be shown.
     */
    public void setShowStereotypes(String showem) {
        String oldValue = Boolean.valueOf(showStereotypes).toString();
        showStereotypes = Boolean.valueOf(showem).booleanValue();
        ArgoEventPump.fireEvent(
                new ArgoNotationEvent(ArgoEventTypes.NOTATION_CHANGED, 
                        new PropertyChangeEvent(this, 
                                Notation.KEY_SHOW_STEREOTYPES.getKey(), 
                                oldValue, showem)));
    }

    /**
     * @param showproperties <code>true</code> if properties are to be shown.
     */
    public void setShowStereotypes(boolean showem) {
        String oldValue = Boolean.valueOf(showStereotypes).toString();
        showStereotypes = showem;
        String newValue = Boolean.valueOf(showStereotypes).toString();
        ArgoEventPump.fireEvent(
                new ArgoNotationEvent(ArgoEventTypes.NOTATION_CHANGED, 
                        new PropertyChangeEvent(this, 
                                Notation.KEY_SHOW_STEREOTYPES.getKey(), 
                                oldValue, newValue)));
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
        String oldValue = Integer.valueOf(defaultShadowWidth).toString();
        defaultShadowWidth = width;
        String newValue = Integer.valueOf(defaultShadowWidth).toString();
        ArgoEventPump.fireEvent(
                new ArgoNotationEvent(ArgoEventTypes.NOTATION_CHANGED, 
                        new PropertyChangeEvent(this, 
                                Notation.KEY_DEFAULT_SHADOW_WIDTH.getKey(), 
                                oldValue, newValue)));
    }
    
    /**
     * @param defaultShadowWidth The defaultShadowWidth to set.
     */
    public void setDefaultShadowWidth(String width) {
        String oldValue = Integer.valueOf(defaultShadowWidth).toString();
        defaultShadowWidth = Integer.valueOf(width).intValue();
        ArgoEventPump.fireEvent(
                new ArgoNotationEvent(ArgoEventTypes.NOTATION_CHANGED, 
                        new PropertyChangeEvent(this, 
                                Notation.KEY_DEFAULT_SHADOW_WIDTH.getKey(), 
                                oldValue, width)));
    }
}
